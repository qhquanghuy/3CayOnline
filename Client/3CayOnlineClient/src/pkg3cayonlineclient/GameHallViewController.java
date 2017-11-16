/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameHallModel;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class GameHallViewController extends ViewController<GameHallView> implements GameHallDelegate {
    
    private final UserInfo user;
    private GameHallModel gameHallData;
    private boolean shouldKeepListening = true;
    
    public GameHallViewController(UserInfo user) {
        super(new GameHallView());
        
        this.user = user;
        
        SwingUtilities.invokeLater(() -> {
            this.view.bind(user);
            new Thread(() -> this.viewDidLoad()).start();
        });
        
    }
    
    private Result<GameHallModel> getGameHallData() {
        return SocketHandler.sharedIntance()
                .get(new Request(Common.RequestURI.GetOnlineUsers, null), GameHallModel.class);
    }
    
    public void viewDidLoad() {
        this.view.setDelegate(this);
        Result<GameHallModel> result = this.getGameHallData();
            
        if(result.isError()) {
            this.view.showAlert(result.errorVal());
        } else {
            this.gameHallData = result.value();
            Vector onlineUserData = this.gameHallData.getOnlinePlayers()
                                            .stream()
                                            .map(this::parseOnlineUser)
                                            .collect(Collectors.toCollection(Vector::new));
                                            
            this.view.bind(onlineUserData, this.view.getTblOnlineUser());
            
            Vector gameRoomData = this.gameHallData.getGameRooms()
                                            .stream()
                                            .map(this::parseGameRoom)
                                            .collect(Collectors.toCollection(Vector::new));
            this.view.bind(gameRoomData, this.view.getTblRoomList());
        }
        
        this.listening();
    }
    
    private Vector parseGameRoom(GameRoom room) {
        Vector data = new Vector();
        data.add(room.getId());
        data.add(room.getTitle());
        data.add(String.valueOf(room.getPlayersInRoom()) + "/" + String.valueOf(room.getMaximumPlayer()));
        data.add(room.getStatus());
        return data;
    }
     
    private Vector parseOnlineUser(UserInfo user) {
        Vector data = new Vector();
        data.add(user.getUsername());
        data.add(user.getScore());
        return data;
    }
    
    private void updatingRoom(GameRoom gameRoom) {
        int index = this.gameHallData.getGameRooms().indexOf(gameRoom);
        this.view.updateGameRoomEntry(parseGameRoom(gameRoom), index);
    }
    
    private <T> void addNewEntryForTable(List<T> entries, T entry, Function<T, Vector> parser, JTable table) {
        this.view.addNewEntry(parser.apply(entry), table);
        entries.add(entry);
    }
    
    private <T> void removeNewEntryForTable(List<T> entries, T entry, JTable table) {
        int index = entries.indexOf(entry);
        this.view.removeEntry(index, table);
        entries.remove(index);
    }
        
    public void listening() {
        System.out.println("Start listening");
        while(this.shouldKeepListening) {
            SocketHandler.sharedIntance().receiving(response -> {
                switch (response.getHeader()) {
                    case Error:
                        this.view.showAlert((String) response.getData());
                        break;
                    case AUserOnline:
                        Helper.parse(response, UserInfo.class)
                              .either(userInfo -> {
                                  this.addNewEntryForTable(this.gameHallData.getOnlinePlayers(), 
                                          userInfo, 
                                          this::parseOnlineUser, 
                                          this.view.getTblOnlineUser());
                              },this.view::showAlert);
                        break;
                    case AUserLeftGame:
                        Helper.parse(response, UserInfo.class)
                              .either(userInfo -> {
                                  this.removeNewEntryForTable(this.gameHallData.getOnlinePlayers(), 
                                          userInfo, 
                                          this.view.getTblOnlineUser());
                                },this.view::showAlert);
                        break;
                    case ARoomCreated:
                        Helper.parse(response, GameRoom.class)
                              .either(gameRoom -> {
                                  this.addNewEntryForTable(this.gameHallData.getGameRooms(), 
                                          gameRoom, 
                                          this::parseGameRoom, 
                                          this.view.getTblRoomList());
                              },this.view::showAlert);
                        break;
                    case ARoomRemoved:
                         Helper.parse(response, GameRoom.class)
                              .either(gameRoom -> {
                                  this.removeNewEntryForTable(this.gameHallData.getGameRooms(), 
                                          gameRoom, 
                                          this.view.getTblOnlineUser());
                                },this.view::showAlert);
                        break;
                        case ARoomUpdated:
                            Helper.parse(response, GameRoom.class)
                              .either(this::updatingRoom,this.view::showAlert);
                            break;
                    case Success:
                        GameRoom gameRoom = (GameRoom) response.getData();
                        this.router.push(new GameRoomViewController(gameRoom, this.user));
                        this.shouldKeepListening = false;
                        break;
                    default: break;                            
                }
            });
        }
        System.out.println("Stop listening");
    }
    
    private void sendingGameRoomTo(GameRoom gameRoom, Common.RequestURI uri) {
        try {
            SocketHandler.sharedIntance().sending(new Request(uri, gameRoom));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.view.showAlert("Something went wrong");
        }
    }

    @Override
    public void onTapBtnProfile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTapBtnSignOut() {
        this.shouldKeepListening = false;
        try {
            SocketHandler.sharedIntance().sending(new Request(Common.RequestURI.SignOut, null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.router.pop();
    }

    @Override
    public void joinARoom(int roomIdx) {
        GameRoom gameRoom = this.gameHallData.getGameRooms().get(roomIdx);
        new Thread(() -> this.sendingGameRoomTo(gameRoom, Common.RequestURI.JoinRoom)).start(); 
    }
    
    
    

    @Override
    public void onTapBtnCreate() {
        CreateRoomController createRoomVC =  new CreateRoomController(this.user);
        this.view.presentConfirm(createRoomVC, isOk -> {
            if(isOk) {
                GameRoom gameRoom = createRoomVC.getGameRoom();
                new Thread(() -> this.sendingGameRoomTo(gameRoom, Common.RequestURI.CreateRoom)).start();
            }
        });
        
        
        
    }
    
    
    
}

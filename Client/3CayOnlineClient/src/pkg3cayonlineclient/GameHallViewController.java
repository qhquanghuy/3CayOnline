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
public class GameHallViewController extends ViewController implements GameHallDelegate {
    
    private final UserInfo user;
    private GameHallModel gameHallData;
    private boolean shouldKeepListening = true;
    
    public GameHallViewController(UserInfo user) {
        super(new GameHallView());
        
        this.user = user;
        
        SwingUtilities.invokeLater(() -> {
            ((GameHallView) this.view).bind(user);
            
            new Thread(() -> this.viewDidLoad()).start();
        });
        
    }
    
    private Result<GameHallModel> getGameHallData() {
        return SocketHandler.sharedIntance()
                .get(new Request(Common.RequestURI.GetOnlineUsers, null), GameHallModel.class);
    }
    
    public void viewDidLoad() {
        GameHallView gameView = ((GameHallView) this.view);
        gameView.setDelegate(this);
        Result<GameHallModel> result = this.getGameHallData();
            
        if(result.isError()) {
            this.view.showAlert(result.errorVal());
        } else {
            this.gameHallData = result.value();
            Vector onlineUserData = this.gameHallData.getOnlinePlayers()
                                            .stream()
                                            .map(this::parseOnlineUser)
                                            .collect(Collectors.toCollection(Vector::new));
                                            
            gameView.bind(onlineUserData, gameView.getTblOnlineUser());
            
            Vector gameRoomData = this.gameHallData.getGameRooms()
                                            .stream()
                                            .map(this::parseGameRoom)
                                            .collect(Collectors.toCollection(Vector::new));
            gameView.bind(gameRoomData, gameView.getTblRoomList());
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
    
    
    private <T> void addNewEntryForTable(List<T> entries, T entry, Function<T, Vector> parser, JTable table) {
        GameHallView gameView = ((GameHallView) this.view);
        gameView.addNewEntry(parser.apply(entry), table);
        entries.add(entry);
    }
    
    private <T> void removeNewEntryForTable(List<T> entries, T entry, JTable table) {
        GameHallView gameView = ((GameHallView) this.view);
        int index = entries.indexOf(entry);
        gameView.removeEntry(index, table);
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
                                          ((GameHallView) this.view).getTblOnlineUser());
                              }, error -> this.view.showAlert(error));
                        break;
                    case AUserLeftGame:
                        Helper.parse(response, UserInfo.class)
                              .either(userInfo -> {
                                  this.removeNewEntryForTable(this.gameHallData.getOnlinePlayers(), 
                                          userInfo, 
                                          ((GameHallView) this.view).getTblOnlineUser());
                                }, error -> this.view.showAlert(error));
                        break;
                    case ARoomCreated:
                        Helper.parse(response, GameRoom.class)
                              .either(gameRoom -> {
                                  this.addNewEntryForTable(this.gameHallData.getGameRooms(), 
                                          gameRoom, 
                                          this::parseGameRoom, 
                                          ((GameHallView) this.view).getTblRoomList());
                              }, error -> this.view.showAlert(error));
                        break;
                    case ARoomRemoved:
                         Helper.parse(response, GameRoom.class)
                              .either(gameRoom -> {
                                  this.removeNewEntryForTable(this.gameHallData.getGameRooms(), 
                                          gameRoom, 
                                          ((GameHallView) this.view).getTblOnlineUser());
                                }, error -> this.view.showAlert(error));
                        break;
                    case Success:
                        this.shouldKeepListening = false;
                        GameRoom gameRoom = (GameRoom) response.getData();
                        this.router.push(new GameRoomViewController(gameRoom, this.user));
                        
                    default: break;                            
                }
            });
        }
        System.out.println("Stop listening");
    }
    
    
    private void createRoom(GameRoom gameRoom) {
        try {
            SocketHandler.sharedIntance().sending(new Request(Common.RequestURI.CreateRoom, gameRoom));
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
    public void onTapBtnJoin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    

    @Override
    public void onTapBtnCreate() {
        CreateRoomController createRoomVC =  new CreateRoomController();
        this.view.presentConfirm(createRoomVC, (isOk) -> {
            GameRoom gameRoom = createRoomVC.getGameRoom();
            gameRoom.setHostedPlayer(this.user);
            new Thread(() -> this.createRoom(gameRoom)).start();
        });
        
        
        
    }
    
    
    
}

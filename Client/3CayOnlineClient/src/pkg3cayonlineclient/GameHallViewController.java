/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
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
        data.add(room.getPlayersInRoom());
        data.add(room.getStatus());
        return data;
    }
     
    private Vector parseOnlineUser(UserInfo user) {
        Vector data = new Vector();
        data.add(user.getUsername());
        data.add(user.getScore());
        return data;
    }
    
    
    private void addNewUserOnline(UserInfo user) {
        GameHallView gameView = ((GameHallView) this.view);
        gameView.addNewEntry(parseOnlineUser(user), gameView.getTblOnlineUser());
        this.gameHallData.getOnlinePlayers().add(user);
    }
    
    private void doUserOffline(UserInfo user) {
        GameHallView gameView = ((GameHallView) this.view);
        int index = this.gameHallData.getOnlinePlayers().indexOf(user);
        gameView.removeEntry(index, gameView.getTblOnlineUser());
        this.gameHallData.getOnlinePlayers().remove(index);
                
                
    }
        
    public void listening() {
        while(this.shouldKeepListening) {
            SocketHandler.sharedIntance().receiving(response -> {
                switch (response.getHeader()) {
                    case Error:
                        this.view.showAlert((String) response.getData());
                        break;
                    case AUserOnline:
                        Helper.parse(response, UserInfo.class)
                              .either(this::addNewUserOnline, error -> this.view.showAlert(error));
                        break;
                    case AUserLeftGame:
                        Helper.parse(response, UserInfo.class)
                              .either(this::doUserOffline, error -> this.view.showAlert(error));
                        break;
                    case ARoomCreated:
                        break;
                    default: break;                            
                }
            });
        }
    }

    @Override
    public void onTapBtnProfile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTapBtnSignOut() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTapBtnJoin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTapBtnCreate() {
        Consumer<Result<GameRoom>> createdHandler = result -> {
            result.either(gameRoom -> this.router.show(new GameRoomViewController()), error -> this.view.showAlert(error));
        };
        new CreateRoomController(this.user,createdHandler).setVisible(true);
    }
    
    
    
}

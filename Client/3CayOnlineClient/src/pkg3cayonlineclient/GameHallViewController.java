/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
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
public class GameHallViewController extends ViewController {
    
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
        Result<GameHallModel> result = this.getGameHallData();
            
        if(result.isError()) {
            this.view.showAlert(result.errorVal());
        } else {
            this.gameHallData = result.value();
            Vector onlineUserData = this.gameHallData.getOnlinePlayers()
                                            .stream()
                                            .filter(e -> !e.equals(this.user))
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
        data.add(room.numberOfPlayers());
        data.add(room.getStatus());
        return data;
    }
     
    private Vector parseOnlineUser(UserInfo user) {
        Vector data = new Vector();
        data.add(user.getUsername());
        return data;
    }
    
    public void listening() {
        while(this.shouldKeepListening) {
            SocketHandler.sharedIntance().receiving(response -> {
                switch (response.getHeader()) {
                    case Error:
                        this.view.showAlert((String) response.getData());
                    
                    case NewSignedUser:
                        GameHallView gameView = ((GameHallView) this.view);
                        
                        gameView.addNewEntry(parseOnlineUser((UserInfo) response.getData()), 
                                             gameView.getTblOnlineUser());
                    
                    default: break;
                                
                            
                }
            });
        }
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author HuyNguyen
 */
public class GameRoomViewController extends ViewController<GameRoomView> {
    private final GameRoom gameRoom;
    private final UserInfo user;
    private boolean shouldKeepListening = true;
    public GameRoomViewController(GameRoom gameRoom,UserInfo user) {
        super(new GameRoomView());
        this.user = user;
        this.view.bindRoom(gameRoom);
        this.view.bindPlayer(user);
        this.gameRoom = gameRoom;
        this.gameRoom.addPlayer(user);        
        
        this.gameRoom.getPlayers().forEach(p -> {
            if(!p.equals(this.user)) {
                this.view.bindPlayer(p);
            }
        });
        this.view.showStartGameBtn(this.user.equals(this.gameRoom.getHostedPlayer()));
        new Thread(this::listening).start();
    }
    
    private void addAPlayer(UserInfo user) {
        this.gameRoom.addPlayer(user);
        this.view.bindPlayer(user);
    }
   
    
    private void removeAPlayer(UserInfo user) {
        this.view.removeAPlayer(user);
        this.gameRoom.removePlayer(user);
    }
    
    public void listening() {
        System.out.println("Start listening");
        while(this.shouldKeepListening) {
            SocketHandler.sharedIntance().receiving(response -> {
                switch (response.getHeader()) {
                    case Error:
                        this.view.showAlert((String) response.getData());
                        break;
                    case AUserLeftRoom:
                        Helper.parse(response, UserInfo.class)
                              .either(this::removeAPlayer, this.view::showAlert);
                        break;
                    case AUserJoinGame:
                        Helper.parse(response, UserInfo.class)
                              .either(this::addAPlayer, this.view::showAlert);
                        break;
                    case Success:
//                        this.shouldKeepListening = false;
//                        this.router.push(new GameRoomViewController((GameRoom) response.getData(), this.user));
                        
                    default: break;                            
                }
            });
        }
        System.out.println("Stop listening");
    }
    
}

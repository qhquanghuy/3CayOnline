/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.io.IOException;
import javax.swing.SwingUtilities;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Player;

/**
 *
 * @author HuyNguyen
 */
public class GameRoomViewController extends ViewController<GameRoomView> implements GameRoomDelegate {
    private final GameRoom gameRoom;
    private final Player user;
    private boolean shouldKeepListening = true;
    public GameRoomViewController(GameRoom gameRoom,Player user) {
        super(new GameRoomView());
        this.user = user;
        this.gameRoom = gameRoom;
        this.gameRoom.addPlayer(user);        
        SwingUtilities.invokeLater(this::viewDidLoad);
        
        
    }
    
    
    private void viewDidLoad() {
        this.view.bindRoom(gameRoom);
        this.view.bindPlayer(user);
        this.gameRoom.getPlayers().forEach(p -> {
            if(!p.equals(this.user)) {
                this.view.bindPlayer(p);
            }
        });
        this.view.setDelegate(this);
        this.view.showStartGameBtn(this.user.equals(this.gameRoom.getHostedPlayer()));
        new Thread(this::listening).start();
    }
    
    private void addAPlayer(Player user) {
        this.gameRoom.addPlayer(user);
        this.view.bindPlayer(user);
    }
   
    
    private void removeAPlayer(Player user) {
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
                        Helper.parse(response, Player.class)
                              .either(this::removeAPlayer, this.view::showAlert);
                        break;
                    case AUserJoinGame:
                        Helper.parse(response, Player.class)
                              .either(this::addAPlayer, this.view::showAlert);
                        break;
                    case Card:
                        System.out.println(((Player) response.getData()).getDeck());
                    case Update:
                        break;
//                        System.out.println("cards for" + ((Player) response.getData()).getUsername());
                    case EndGame:
                        System.out.println(response.getData() + " is winner");
                        if(this.user.equals(this.gameRoom.getHostedPlayer())) {
                            this.view.showStartGameBtn(true);
                        }
                    default: break;                            
                }
            });
        }
        System.out.println("Stop listening");
    }

    @Override
    public void onTapStart() {
        try {
            SocketHandler.sharedIntance().sending(new Request(Common.RequestURI.StartGame, this.gameRoom));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}

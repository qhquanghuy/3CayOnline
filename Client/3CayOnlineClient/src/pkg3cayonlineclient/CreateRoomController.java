/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author HuyNguyen
 */
public class CreateRoomController {
    private final CreateRoomView frame;

    public CreateRoomController(UserInfo user, Consumer<Result<GameRoom>> createdRoomHandler) {       
        this.frame = new CreateRoomView();
        this.frame.setOnTapCreate(gameRoom -> {
            gameRoom.setHostedPlayer(user);
            Result<GameRoom> createdRoom =  SocketHandler.sharedIntance().get(new Request<GameRoom>(Common.RequestURI.CreateRoom, gameRoom), GameRoom.class);
            createdRoomHandler.accept(createdRoom);
            SwingUtilities.invokeLater(() -> this.frame.dispose());
        });
    }
    
    
    public void setVisible(boolean isVisible) {
        this.frame.setVisible(true);
    }
    
}

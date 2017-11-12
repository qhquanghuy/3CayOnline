/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.util.List;
import pkg3cayonlinesharedmodel.GameRoom;

/**
 *
 * @author huynguyen
 */
public class GameRoomHandler {
    private GameRoom gameRoom;
    private List<UserHandler> userHandler;

    public GameRoomHandler(GameRoom gameRoom, List<UserHandler> userHandler) {
        this.gameRoom = gameRoom;
        this.userHandler = userHandler;
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public List<UserHandler> getUserHandler() {
        return userHandler;
    }

    public void setUserHandler(List<UserHandler> userHandler) {
        this.userHandler = userHandler;
    }
    
    
    
    
}

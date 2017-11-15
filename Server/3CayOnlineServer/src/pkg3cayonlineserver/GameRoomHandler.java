/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.util.ArrayList;
import java.util.List;
import pkg3cayonlinesharedmodel.GameRoom;

/**
 *
 * @author huynguyen
 */
public class GameRoomHandler {
    private GameRoom gameRoom;
    private List<UserHandler> userHandlers;

    public GameRoomHandler(GameRoom gameRoom, UserHandler userHandler) {
        this.gameRoom = gameRoom;
        this.userHandlers = new ArrayList<>();
        this.userHandlers.add(userHandler);
    }
    
    public synchronized void addPlayer(UserHandler userHandler) {
        boolean canAdd = this.gameRoom.addPlayer(userHandler.getUser());
        if(canAdd) {
            this.userHandlers.add(userHandler);
        }
    }
    
    
    public UserHandler getHost() {
        return this.userHandlers.get(0);
    }

    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public List<UserHandler> getUserHandler() {
        return userHandlers;
    }

    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class GameRoomHandler {
    private final GameRoom gameRoom;
    private final List<UserHandler> userHandlers;

    public GameRoomHandler(GameRoom gameRoom, UserHandler userHandler) {
        this.gameRoom = gameRoom;
        this.userHandlers = new ArrayList<>();
        this.userHandlers.add(userHandler);
    }
    
    public Result<GameRoom> addedPlayer(UserHandler userHandler) {
        boolean canAdd = this.gameRoom.addPlayer(userHandler.getUser());
        if(canAdd) {
            this.userHandlers.add(userHandler);
            this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.AUserJoinGame, userHandler.getUser()));
            return Result.ok(this.gameRoom);
        } else {
            return Result.error("This room is full");
        }
    }
    public synchronized void removePlayer(UserHandler user) {
        if(this.userHandlers.remove(user)) {
            this.gameRoom.removePlayer(user.getUser());
            this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.AUserLeftRoom, user.getUser()));
        }

    }
    
    private void notifyAllOnlineUsers(Response message) {
        userHandlers.forEach((onlineUser) -> {
            try {
                onlineUser.sending(message);
            } catch (IOException ex) {
                ex.printStackTrace();
                onlineUser.closing();
            }
        });
    }
    
//    public UserHandler getHost() {
//        return this.userHandlers.get(0);
//    }
    public boolean contains(UserInfo user) {
        return this.userHandlers.stream().anyMatch(u -> u.getUser().equals(user));
    }
    public boolean isHandleRoom(GameRoom room) {
        return this.gameRoom.equals(room);
    }
    public GameRoom getGameRoom() {
        return gameRoom;
    }

    public List<UserHandler> getUserHandler() {
        return userHandlers;
    }

    
    
    
    
}

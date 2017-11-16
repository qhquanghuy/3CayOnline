/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameHallModel;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class GameHallController implements GameDelegate  {
    private final List<UserHandler> atGameHallUsers = new ArrayList<>();
    private final List<GameRoomHandler> gameRooms = new ArrayList<>();
    private int gameRoomId = 1;
    public GameHallController() {
    }

    public synchronized Response joinRoom(GameRoom gameRoom, UserHandler client) {
        for(GameRoomHandler r: this.gameRooms) {
            if(r.isHandleRoom(gameRoom)) {
                Result<GameRoom> result =  r.addedPlayer(client);
                if(!result.isError()) {
                    this.atGameHallUsers.remove(client);
                    this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.AUserLeftGame, client.getUser()));
                    this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.ARoomUpdated, result.value()));
                    return new Response<>(Common.ResponseHeader.Success, result.value());
                }
                return new Response<>(Common.ResponseHeader.Error, result.errorVal());
            }
        }
        return new Response<>(Common.ResponseHeader.Error, "Room not found");
    }
    
    public synchronized GameRoom createdRoom(GameRoom gameRoom, UserHandler client) {
        gameRoom.setId(this.gameRoomId);
        this.gameRoomId += 1;
        GameRoomHandler roomHandler = new GameRoomHandler(gameRoom, client);
        this.gameRooms.add(roomHandler);
        this.atGameHallUsers.remove(client);
        this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.ARoomCreated, gameRoom));
        this.notifyAllOnlineUsers(new Response(Common.ResponseHeader.AUserLeftGame, client.getUser()));
        return gameRoom;
    }
    
        
        
    public synchronized void disconnecting(UserHandler user) {
        if(!this.atGameHallUsers.remove(user)){
            this.gameRooms.forEach(room -> room.removePlayer(user));
            
        } else {
            this.notifyAllOnlineUsers(new Response<UserInfo>(Common.ResponseHeader.AUserLeftGame, user.getUser()));
        }
    }
    
    public void signingOutUser(UserHandler user) {
        synchronized(this.atGameHallUsers) {
            this.atGameHallUsers.remove(user);
            this.notifyAllOnlineUsers(new Response<UserInfo>(Common.ResponseHeader.AUserLeftGame, user.getUser()));
        }
    }

    public void signingInUser(UserHandler client, UserInfo user) {
        client.setUser(user);
        synchronized(this.atGameHallUsers) {
            this.notifyAllOnlineUsers(new Response<UserInfo>(Common.ResponseHeader.AUserOnline, user));
            this.atGameHallUsers.add(client);
        }
    }
    
    private void notifyAllOnlineUsers(Response message) {
        atGameHallUsers.forEach((onlineUser) -> {
            try {
                onlineUser.sending(message);
            } catch (IOException ex) {
                ex.printStackTrace();
                onlineUser.closing();
            }
        });
    }
        
    public GameHallModel getGameHallModel(UserInfo user) {
        List<GameRoom> gameRoomes = this.gameRooms
                                            .stream()
                                            .map(e -> e.getGameRoom())
                                            .collect(Collectors.toList());
        List<UserInfo> users = this.atGameHallUsers
                                    .stream()
                                    .map(e -> e.getUser())
                                    .filter(e -> !e.equals(user))
                                    .collect(Collectors.toList());
                                    
        return new GameHallModel(gameRoomes, users);
    }
    
    

    
     @Override
    public synchronized boolean isSignedIn(UserInfo user) {
        return atGameHallUsers.stream()
                        .anyMatch(onlineUser -> (onlineUser.getUser().equals(user))) || 
                this.gameRooms.stream().anyMatch(r -> r.contains(user));
    }
}

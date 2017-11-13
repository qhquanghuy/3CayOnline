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
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class GameHallController implements GameDelegate  {
    private final List<UserHandler> onlineUsers = new ArrayList<>();
    private final List<GameRoomHandler> gameRooms = new ArrayList<>();
    public GameHallController() {
    }

    public List<UserHandler> getSignedInUsers() {
        return onlineUsers;
    }
    
    public void removeUser(UserHandler user) {
        synchronized(this.onlineUsers) {
            this.onlineUsers.remove(user);
        }
    }


    public void signingUser(UserHandler client, UserInfo user) {
        client.setUser(user);
        synchronized(this.onlineUsers) {
            this.notifyNewSignedUser(client, user);
            this.onlineUsers.add(client);
        }
    }
    
    private void notifyNewSignedUser(UserHandler client, UserInfo user) {
        for (UserHandler onlineUser : onlineUsers) {
            try {
                onlineUser.sending(new Response(Common.ResponseHeader.NewSignedUser, user));

            } catch (IOException ex) {
                ex.printStackTrace();
                client.closing();
            }
            
        }
    }
    
    public GameHallModel getGameHallModel() {
        List<GameRoom> gameRoomes = this.gameRooms
                                            .stream()
                                            .map(e -> e.getGameRoom())
                                            .collect(Collectors.toList());
        List<UserInfo> users = this.onlineUsers
                                    .stream()
                                    .map(e -> e.getUser())
                                    .collect(Collectors.toList());
                                    
        return new GameHallModel(gameRoomes, users);
    }
    
    

    
     @Override
    public synchronized boolean isSignedIn(UserInfo user) {
        return onlineUsers.stream()
                        .anyMatch(onlineUser -> (onlineUser.getUser().equals(user)));
    }
}

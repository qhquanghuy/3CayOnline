/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.DeckOfCards;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.Player;

/**
 *
 * @author huynguyen
 */
public class GameRoomController {
    private final GameRoom gameRoom;
    private final List<UserHandler> userHandlers;
    private DeckOfCards deck;
    public GameRoomController(GameRoom gameRoom, UserHandler userHandler, GameDelegate delegate) {
        this.gameRoom = gameRoom;
        this.userHandlers = new ArrayList<>();
        this.userHandlers.add(userHandler);
    }
    
    public Result<GameRoom> addPlayer(UserHandler userHandler) {
        boolean canAdd = this.gameRoom.addPlayer(userHandler.getUser());
        if(canAdd) {
            this.userHandlers.add(userHandler);
            this.notifyAllUsersInRoom(new Response(Common.ResponseHeader.AUserJoinGame, userHandler.getUser()));
            return Result.ok(this.gameRoom);
        } else {
            return Result.error("This room is full");
        }
    }
    public synchronized void removePlayer(UserHandler user) {
        if(this.userHandlers.remove(user)) {
            this.gameRoom.removePlayer(user.getUser());
            this.notifyAllUsersInRoom(new Response(Common.ResponseHeader.AUserLeftRoom, user.getUser()));
        }

    }
    
    private void notifyAllUsersInRoom(Response message) {
        userHandlers.forEach((onlineUser) -> {
            try {
                onlineUser.sending(message);
            } catch (IOException ex) {
                ex.printStackTrace();
                onlineUser.closing();
            }
        });
    }
    
    public boolean contains(Player user) {
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

    public void start(UserHandler client) {
        synchronized(this.userHandlers) {
            this.deck = new DeckOfCards();
            this.userHandlers.forEach(u -> {
            Player player = u.getUser();
                if(!player.isEmpty()) {
                    player.setDeck(deck.take(3));
                }
            });
        }
        
        
    }
    
    
    
    
}

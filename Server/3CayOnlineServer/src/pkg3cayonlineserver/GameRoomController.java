/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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
    
    public Player getWinner() {
        DeckOfCards maxDeck = this.userHandlers.stream()
                .map(c -> c.getUser())
                .map(p -> p.getDeck())
                .map(DeckOfCards::new)
                .max(DeckOfCards::compareTo)
                .get();
        
        return this.userHandlers.stream().map(u -> u.getUser())
                                        .filter(p -> {
                                            DeckOfCards deck = new DeckOfCards(p.getDeck());
                                            return deck.compareTo(maxDeck) == 0;
                                            
                                        }).findFirst()
                                            .get();
    }

    public void start(Consumer completion) {
        synchronized(this.userHandlers) {
            this.deck = new DeckOfCards();
            for(int i = 0; i < 3; ++i) {
                DeckOfCards temp = this.deck.take(this.userHandlers.size());
                for(int j = 0; j < temp.getCards().size(); ++j) {
                    UserHandler current = this.userHandlers.get(j);
                    
                    try {
                        current.getUser().getDeck().add(temp.getCards().get(j));
                        current.sending(new Response(Common.ResponseHeader.Card, 
                                                        current.getUser()));
                        this.userHandlers.stream()
                                        .filter(u -> !u.equals(current))
                                        .forEach(u -> {
                                            try {
                                                u.sending(new Response(Common.ResponseHeader.Update,
                                                        current.getUser()));
                                            } catch (IOException ex) {
                                                u.closing();
                                            }
                                        });
                        
                        Thread.sleep(500);
                    } catch (IOException |InterruptedException ex) {
                        ex.printStackTrace();
                        this.userHandlers.get(j).closing();
                    }
                            
                }
            }
            this.notifyAllUsersInRoom(new Response(Common.ResponseHeader.EndGame, this.getWinner().getUsername()));   
            this.userHandlers.forEach(u -> u.getUser().getDeck().removeAll(u.getUser().getDeck()));
            this.gameRoom.setStatus(Common.GameRoomStatus.Waiting);
            completion.accept(true);
                    
        }

        
    }
    
    
    
    
}

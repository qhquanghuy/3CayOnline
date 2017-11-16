/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author huynguyen
 */
public class GameRoom implements Serializable {
    private int id;
    private String title;
    private Player hostPlayer;
    private final List<Player> players;
    private final int maximumPlayer;
    private Common.GameRoomStatus status;

    public int getMaximumPlayer() {
        return maximumPlayer;
    }
    

    public GameRoom(String title, Player player, int maximumPlayer) {
        this.title = title;
        
        this.players = new ArrayList<>();
        this.players.add(player);
        this.players.add(Player.empty());
        this.players.add(Player.empty());
        this.players.add(Player.empty());
        this.hostPlayer = player;
        
        this.maximumPlayer = maximumPlayer;
        this.status = Common.GameRoomStatus.Waiting;
    }
    
//    public Player getRightOf(Player user) {
//        int i = 0;
//        for(i = 0; i < this.players.size(); ++i) {
//            if(this.players.get(i).equals(user)) {
//                return i < 3 ? this.players.get(i + 1) : 
//                               this.players.get(0);
//            }
//        }
//        return Player.empty();
//    }
    
    public void setHostedPlayer(Player user) {
        this.hostPlayer = user;
    }
    
    public Player getHostedPlayer() {
        return this.hostPlayer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public int getPlayersInRoom() {
        return (int) this.players
                        .stream()
                        .filter(e -> !e.equals(Player.empty()))
                        .count();
    }
    
    public void removePlayer(Player player) {
        int length = this.players.size();
        for(int i = 0; i < length; ++i) {
            if (this.players.get(i).equals(player)) {
                this.players.set(i, Player.empty());
                break;
            }
        }
    }
    
//    public Optional<Common.GameRoomPosition> positionOf(Player user) {
//        int index = this.players.indexOf(user);
//        Common.GameRoomPosition postion = null;
//        if(index == 0) {
//            postion = Common.GameRoomPosition.Bottom;
//        }
//        if(index == 1) {
//            postion = Common.GameRoomPosition.Right;
//        }
//        if(index == 2) {
//            postion = Common.GameRoomPosition.Left;
//        }
//        if(index == 3) {
//            postion = Common.GameRoomPosition.Top;
//        }
//        return Optional.ofNullable(postion);
//    }
    
    public boolean contains(Player player) {
        return this.players.stream().anyMatch((p) -> (p.equals(player)));
    }

    public boolean addPlayer(Player player) {
       
        if(this.getPlayersInRoom() >= this.maximumPlayer) {
            return false;
        }
        
        if(this.contains(player)) {
            return false;
        }
        
        if(this.status == Common.GameRoomStatus.Playing) {
            return false;
        }
        
        int length = this.players.size();
        for(int i = 0; i < length; ++i) {
            if (this.players.get(i).equals(Player.empty())) {
                this.players.set(i, player);
                break;
            }
        }
        return true;
    }

    public Common.GameRoomStatus getStatus() {
        return status;
    }

    public void setStatus(Common.GameRoomStatus status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GameRoom) {
            return this.id == ((GameRoom) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.id;
        return hash;
    }
    
    public boolean isEmpty() {
        return this.players.stream()
                .allMatch(player -> player.equals(Player.empty()));
    }
    
}

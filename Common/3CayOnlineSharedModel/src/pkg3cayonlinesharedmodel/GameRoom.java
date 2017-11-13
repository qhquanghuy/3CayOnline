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
    private final List<UserInfo> players;
    private final int maximumPlayer;
    private Common.GameRoomStatus status;
    

    public GameRoom(int id, String title, UserInfo player, int maximumPlayer) {
        this.id = id;
        this.title = title;
        this.players = new ArrayList<>(4);
        this.maximumPlayer = maximumPlayer;
        this.players.set(0, player);
        this.status = Common.GameRoomStatus.Waiting;
    }
    
    public UserInfo getHostedPlayer() {
        return this.players.get(0);
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

    public List<UserInfo> getPlayers() {
        return players;
    }
    
    public int numberOfPlayers() {
        return (int) this.players
                        .stream()
                        .filter(e -> e != null)
                        .count();
    }
    
    public void removePlayer(UserInfo player) {
        int length = this.players.size();
        for(int i = 0; i < length; ++i) {
            if (this.players.get(i).equals(player)) {
                this.players.set(i, null);
                break;
            }
        }
    }

    public void addPlayer(UserInfo player) {
       
        if(this.numberOfPlayers() >= this.maximumPlayer) {
            return;
        }
        int length = this.players.size();
        for(int i = 0; i < length; ++i) {
            if (this.players.get(i) == null) {
                this.players.set(i, player);
                break;
            }
        }
    }

    public Common.GameRoomStatus getStatus() {
        return status;
    }

    public void setStatus(Common.GameRoomStatus status) {
        this.status = status;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author huynguyen
 */
public class GameHallModel implements Serializable {
    private List<GameRoom> gameRooms;
    private List<UserInfo> onlinePlayers;

    public GameHallModel(List<GameRoom> gameRooms, List<UserInfo> onlinePlayers) {
        this.gameRooms = gameRooms;
        this.onlinePlayers = onlinePlayers;
    }

    public List<UserInfo> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(List<UserInfo> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public List<GameRoom> getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(List<GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }
    
    
}

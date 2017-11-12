/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huynguyen
 */
public class GameHallController {
    private final List<UserHandler> signedInUsers = new ArrayList<>();

    public GameHallController() {
    }

    public List<UserHandler> getSignedInUsers() {
        return signedInUsers;
    }
    
    public void notifyNewSignedPlayer() {
        
    }
    
    
}

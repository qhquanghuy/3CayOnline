/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Player;


/**
 *
 * @author HuyNguyen
 */
public class CreateRoomController extends ViewController<CreateRoomView> {
    
    private final Player user;

    public CreateRoomController(Player user) {
        super(new CreateRoomView());
        this.user = user;
        
    }

    
    public GameRoom getGameRoom() {
        return this.view.getGameRoom(this.user);
    }
        
}

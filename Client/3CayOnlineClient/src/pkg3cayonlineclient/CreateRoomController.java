/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import pkg3cayonlinesharedmodel.GameRoom;


/**
 *
 * @author HuyNguyen
 */
public class CreateRoomController extends ViewController {

    public CreateRoomController() {
        super(new CreateRoomView());
    }

    
    public GameRoom getGameRoom() {
        return ((CreateRoomView) this.view).getGameRoom();
    }
        
}

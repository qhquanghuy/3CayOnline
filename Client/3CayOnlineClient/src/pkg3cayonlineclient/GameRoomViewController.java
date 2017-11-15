/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameHallModel;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author HuyNguyen
 */
public class GameRoomViewController extends ViewController {
    private final GameRoom gameRoom;
    private final UserInfo user;
    public GameRoomViewController(GameRoom gameRoom, UserInfo user) {
        super(new GameRoomView());
        this.gameRoom = gameRoom;
        this.user = user;
    }
    
}

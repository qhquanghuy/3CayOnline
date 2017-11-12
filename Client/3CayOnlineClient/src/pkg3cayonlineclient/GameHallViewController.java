/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameHallModel;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class GameHallViewController extends ViewController {
    
    private final UserInfo user;
    private GameHallModel gameHallData;
    public GameHallViewController(UserInfo user) {
        super(new GameHallView());
        this.user = user;
        
        SwingUtilities.invokeLater(() -> {
            ((GameHallView) this.view).bind(user);
            new Thread(() -> this.viewDidLoad()).start();
        });
        
    }
    
    private Result<GameHallModel> getGameHallData() {
        return SocketHandler.sharedIntance()
                .get(new Request(Common.RequestURI.GetOnlineUsers, null), GameHallModel.class);
    }
    
    public void viewDidLoad() {
        GameHallView gameView = ((GameHallView) this.view);
        Result<GameHallModel> result = this.getGameHallData();
            
        if(result.isError()) {
            Helper.showMessage(view, result.errorVal());
        } else {
            this.gameHallData = result.value();
            gameView.bind(this.gameHallData);
        }
        gameView.bind(user);
        this.listening();
    }
    
    public void listening() {
        while(true) {
            try {
                Result<UserInfo> user = SocketHandler.sharedIntance().received(UserInfo.class);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
}

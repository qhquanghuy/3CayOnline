/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import javax.swing.WindowConstants;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.Player;

/**
 *
 * @author huynguyen
 */
public class LoginViewController extends ViewController<LoginView> implements LoginViewDelegate {
    
    public LoginViewController(LoginView view) {
        super(view);
    }

    @Override
    public void onTapRegister() {
        RMIRegistryClientView registerView = new RMIRegistryClientView();
        registerView.setVisible(true);
        registerView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    @Override
    public void onTapSignIn(Account acc) {
        Request req = new Request(Common.RequestURI.SignIn, acc);
        Result<Player> result = SocketHandler.sharedIntance().get(req,Player.class);
        result.either(val -> this.router.push(new GameHallViewController(val)),
                    err -> this.view.showAlert(err));
    }
    
}

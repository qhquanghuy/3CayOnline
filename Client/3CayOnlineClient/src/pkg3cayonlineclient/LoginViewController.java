/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.ViewController;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Result;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class LoginViewController extends ViewController implements LoginViewDelegate {
    
    public LoginViewController(LoginView view) {
        super(view);
    }

    @Override
    public void onTapRegister() {
        this.getRouter().show(new RegisterViewController());
    }

    @Override
    public void onTapSignIn(Account acc) {
        try {
            Request req = new Request(Common.RequestURI.Login, acc);
            SocketHandler.sharedIntance().sending(req);
            Result<UserInfo> result = SocketHandler.sharedIntance().received(UserInfo.class);
            
            if(result.isError()) {
                Helper.showMessage(view, result.errorVal());
                
            } else {
                this.router.show(new GameHallViewController(result.value()));
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            Helper.showMessage(view, "Something went wrong");
        }
    }
    
}

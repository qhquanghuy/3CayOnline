/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BasedComponents.ViewController;

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
        this.getRouter().show(new RegisterViewController(new RegisterView()));
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import pkg3cayonlinesharedmodel.Account;

/**
 *
 * @author huynguyen
 */
public interface LoginViewDelegate {
    public void onTapRegister();
    public void onTapSignIn(Account acc);
}

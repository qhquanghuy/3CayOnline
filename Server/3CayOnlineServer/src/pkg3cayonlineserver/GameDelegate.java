/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public interface GameDelegate {
    public boolean isSignedIn(UserInfo user);
}

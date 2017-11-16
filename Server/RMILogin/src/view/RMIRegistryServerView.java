/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.RMIRegistryServerControl;

/**
 *
 * @author Administrator
 */
public class RMIRegistryServerView {
    public RMIRegistryServerView(){
        try {
            new RMIRegistryServerControl();
            showMessage("RMI server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void showMessage(String msg){
        System.out.println(msg);
    }
    public static void main(String[] args) {
        RMIRegistryServerView view= new RMIRegistryServerView();
    }
}

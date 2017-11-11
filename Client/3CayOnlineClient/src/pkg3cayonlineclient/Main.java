/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.Router;
import BaseComponents.ViewController;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author HuyNguyen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ViewController loginVC = createdRootViewController();
            Router router = new Router(mainFrame.getContentPane(), loginVC);
           
            
            mainFrame.pack();
            mainFrame.setVisible(true);
        });
        
    }
    
    private static ViewController createdRootViewController() {
        LoginView loginView = new LoginView();
        LoginViewController loginVC = new LoginViewController(loginView);
        loginView.delegate = loginVC;
        return loginVC;
    }
    
    
}

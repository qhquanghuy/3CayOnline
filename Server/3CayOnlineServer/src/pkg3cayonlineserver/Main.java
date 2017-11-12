/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import pkg3cayonlinesharedmodel.Common;

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
        Server server;
        try {
            server = new Server(Common.Config.TCPServer.Port);
            server.listening();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}

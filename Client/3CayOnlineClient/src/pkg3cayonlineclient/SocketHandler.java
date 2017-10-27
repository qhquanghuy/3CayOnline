/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import java.net.Socket;

/**
 *
 * @author HuyNguyen
 */
public class SocketHandler {
    
    public static final SocketHandler shared = new SocketHandler();
    
    private Socket socket;
    
                
    private SocketHandler() {}
}

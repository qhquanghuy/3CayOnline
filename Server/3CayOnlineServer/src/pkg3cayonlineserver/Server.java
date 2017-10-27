/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author HuyNguyen
 */
public class Server {
    private final int serverPort = 1352;
    private final ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(serverPort);
    }
    
    
    
    
    public void listening() {
        while(true) {
            try {
                Socket aceptedSocket = this.serverSocket.accept();
                
                new ClientHandler(aceptedSocket).start();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
}

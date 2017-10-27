/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author HuyNguyen
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream ouput;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
        
    }
    
    
    public void send() {
        
    }
        
    public void received() {
        
    }
    
    
    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                if(this.input == null) {
                    this.input = new ObjectInputStream(socket.getInputStream());
                }
                
                
                String req = (String) this.input.readObject();
                
                if (req.equalsIgnoreCase("someaction")) {
                    Thread.sleep(0);                
                }
                

                if (this.ouput == null) {
                    this.ouput = new ObjectOutputStream(socket.getOutputStream());
                }
                this.ouput.writeObject("res for" + req);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    this.socket.close();
                    break;
                } catch (IOException ex1) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex); 
            }

        }
                                
    }
    
}

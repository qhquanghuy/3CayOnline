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
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;


/**
 *
 * @author HuyNguyen
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                if (this.input == null) { 
                    this.input = new ObjectInputStream(socket.getInputStream());
                }
                if (this.output == null) {
                    this.output = new ObjectOutputStream(socket.getOutputStream());
                }
                Request request = (Request) this.input.readObject();
                this.output.writeObject(Router.parse(request));
                Thread.sleep(200);
            }
        } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                ex.printStackTrace();
        } finally {
            try {
                this.socket.close();
                this.input.close();
                this.output.close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }
        
                                
    }
    
}

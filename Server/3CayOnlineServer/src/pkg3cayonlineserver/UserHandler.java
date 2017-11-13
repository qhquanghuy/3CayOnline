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
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.UserInfo;



/**
 *
 * @author HuyNguyen
 */
public class UserHandler extends Thread {

    private final Socket socket;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private final Responseable serverDelegate;
    private UserInfo user;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
    public UserHandler(Socket socket, Responseable delegate) {
        this.socket = socket;
        this.serverDelegate = delegate;
    }
    
    public Request received() throws IOException, ClassNotFoundException {
        if (this.input == null) { 
            this.input = new ObjectInputStream(socket.getInputStream());
        }
        return (Request) this.input.readObject();
    }
    
    public void sending(Response res) throws IOException {
        if (this.output == null) {
            this.output = new ObjectOutputStream(socket.getOutputStream());
        }
        this.output.writeObject(res);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                Request request = this.received();
                this.sending(this.serverDelegate.getResponseForRequest(this, request));
                Thread.sleep(200);
            }
        } catch (ClassNotFoundException | IOException | InterruptedException ex) {
                ex.printStackTrace();
        } finally {
            this.closing();
        }                
    }
    
    public void closing() {
        try {
            this.input.close();
            this.output.close();
            this.socket.close();
            this.serverDelegate.clientUnreachable(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
                
    }
    
}

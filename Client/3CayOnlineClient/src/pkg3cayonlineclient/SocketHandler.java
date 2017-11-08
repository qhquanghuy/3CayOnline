/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;

/**
 *
 * @author HuyNguyen
 */
final public class SocketHandler {
    
    private static SocketHandler shared = null;
    
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public static SocketHandler sharedIntance() {
        if(shared == null) {
            shared = new SocketHandler();
        }
        return shared;
    }
                
    private SocketHandler() {
        try {
            this.socket = new Socket(Common.Config.TCPServer.Host,
                                     Common.Config.TCPServer.Port);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void sending(Request req) throws IOException {
        if(this.output == null) {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }
        this.output.writeObject(req);
        
    }
    
    public Response received() throws IOException, ClassNotFoundException {
        if(this.input == null) {
            this.input = new ObjectInputStream(this.socket.getInputStream());
        }
        return (Response) this.input.readObject();
    }
    
    public void closing() throws IOException {
        this.socket.close();
        this.input.close();
        this.output.close();
    }
}

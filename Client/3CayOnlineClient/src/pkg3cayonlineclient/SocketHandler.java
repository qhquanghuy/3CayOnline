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
import java.util.function.Consumer;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.Result;

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
            this.opening();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public <O> Result<O> get(Request req, Class<O> outputType) {
        try {
            if(this.isClosed()) {
                this.opening();
            }
            this.sending(req);
            return this.received(outputType);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Result.error("Something went wrong");
        }
    }
    
    public void sending(Request req) throws IOException {
        if(this.isClosed()) {
            this.opening();
        }
        if(this.output == null) {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
        }
        this.output.writeObject(req);
        
    }
    
    
    
    public <O> Result<O> received(Class<O> type) {
        
        Response response;
        try {
            if(this.isClosed()) {
                this.opening();
            }
            if(this.input == null) {
            this.input = new ObjectInputStream(this.socket.getInputStream());
        }
            response = (Response) this.input.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            response = Response.systemError();
        }
        return Helper.parse(response, type);
    }
    
    public <O> void receiving(Consumer<Response<O>> parser) {
        Response response;
        try {
            if(this.input == null) {
            this.input = new ObjectInputStream(this.socket.getInputStream());
        }
            Object obj = this.input.readObject();
            response = (Response) obj;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            response = Response.systemError();
        }
        parser.accept(response);
    }
    
    public void closing() throws IOException {
        this.socket.close();
        this.input.close();
        this.output.close();
        this.input = null;
        this.output = null;
        this.socket = null;
    }
    
    public boolean isClosed() {
        return this.socket == null;
    }
    
    public void opening() throws IOException {
        this.socket = new Socket(Common.Config.TCPServer.Host,
                                  Common.Config.TCPServer.Port);
    }

    


}

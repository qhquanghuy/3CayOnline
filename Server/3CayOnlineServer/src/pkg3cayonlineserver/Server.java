/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author HuyNguyen
 */
public class Server implements Responseable {
    
    private final ServerSocket serverSocket;
    private LoginController loginController;
    private final GameHallController gameHallController = new GameHallController();
    
    
    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);        
    }
    
    
    
    
    public void listening() {
        while(true) {
            try {
                Socket aceptedSocket = this.serverSocket.accept();
                System.out.println("####Connected Client" + aceptedSocket.getInetAddress() 
                                    + " on " + aceptedSocket.getPort());
                
                new UserHandler(aceptedSocket, this).start();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    synchronized private LoginController getLoginController() throws SQLException {
        if (this.loginController == null) {
            this.loginController = new LoginController(this.gameHallController);
        }
        return this.loginController;
    }
    
    
    
    
    
    
    @Override
    public Response getResponseForRequest(UserHandler client, Request request) {
        Common.RequestURI uri = (Common.RequestURI) request.getHeader();
        LoginController loginControl;
        
        try {
            loginControl = getLoginController();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.systemError();
        }
        
        switch (uri) {
            case SignIn:
                Response<UserInfo> res = loginControl.getUserInfo((Account) request.getData());
                
                if(res.getHeader() != Common.ResponseHeader.Error) {
                    this.gameHallController.signingUser(client, res.getData());
                }
                
                return res;
            case SignOut:
            case CreateRoom:
                
            case LeaveRoom:
            case JoinRoom:
            case GetOnlineUsers:
                return new Response(Common.ResponseHeader.Success, 
                                    this.gameHallController.getGameHallModel(client.getUser()));
            default:
                return Response.resourceNotFound();
        }
    }
    
    @Override
    public void clientUnreachable(UserHandler client) {
        this.gameHallController.removeUser(client);
    }
    
}

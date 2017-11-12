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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.GameHallModel;
import pkg3cayonlinesharedmodel.GameRoom;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author HuyNguyen
 */
public class Server implements Responseable, ServerDelegate {
    
    private final ServerSocket serverSocket;
    private LoginController loginController;
    private GameHallController gameHallController;
    
    private final List<UserHandler> onlineUsers = new ArrayList<>();
    private final List<GameRoomHandler> gameRooms = new ArrayList<>();
    
    
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
            this.loginController = new LoginController(this);
        }
        return this.loginController;
    }
    
    synchronized private GameHallController getGameHallController() {
        if (this.gameHallController == null) {
            this.gameHallController = new GameHallController();
        }
        return this.gameHallController;
    }
    
    private void signingUser(UserHandler client, UserInfo user) {
        client.setUser(user);
        synchronized(this.onlineUsers) {
            this.notifyNewSignedUser(client, user);
            this.onlineUsers.add(client);
        }
    }
    
    private void notifyNewSignedUser(UserHandler client, UserInfo user) {
        for (UserHandler onlineUser : onlineUsers) {
            try {
                onlineUser.sending(new Response(Common.ResponseHeader.NewSignedUser, user));

            } catch (IOException ex) {
                ex.printStackTrace();
                client.closing();
        }
            
        }
    }
    
    private GameHallModel getGameHallModel() {
        List<GameRoom> gameRoomes = this.gameRooms
                                            .stream()
                                            .map(e -> e.getGameRoom())
                                            .collect(Collectors.toList());
        List<UserInfo> users = this.onlineUsers
                                    .stream()
                                    .map(e -> e.getUser())
                                    .collect(Collectors.toList());
                                    
        return new GameHallModel(gameRoomes, users);
    }
    
    

    @Override
    public Response getResponseForRequest(UserHandler client, Request request) {
        Common.RequestURI uri = (Common.RequestURI) request.getHeader();
        LoginController loginControl;
        
        try {
            loginControl = getLoginController();
        } catch (SQLException ex) {
            return Response.systemError();
        }
        
        switch (uri) {
            case SignIn:
                Response<UserInfo> res = loginControl.getUserInfo((Account) request.getData());
                
                if(res.getHeader() != Common.ResponseHeader.Error) {
                    this.signingUser(client, res.getData());
                }
                
                return res;
            case SignOut:
            case CreateRoom:
            case LeaveRoom:
            case JoinRoom:
            case GetOnlineUsers:
                return new Response(Common.ResponseHeader.Success, this.getGameHallModel());
            default:
                return Response.resourceNotFound();
        }
    }

    @Override
    public synchronized boolean isSignedIn(UserInfo user) {
        for (UserHandler onlineUser : onlineUsers) {
            if(onlineUser.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void clientUnreachable(UserHandler client) {
        this.onlineUsers.remove(client);
    }
    
    
    
    
    
    
}

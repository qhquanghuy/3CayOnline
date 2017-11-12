/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import Config.ServerConfig;
import java.sql.SQLException;
import java.util.Optional;
import pkg3cayonlineserversharedmodel.UserDAO;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class LoginController {
    
    private final UserDAO dao;
    private final ServerDelegate serverDelegate;
    public LoginController(ServerDelegate serverDelegate) throws SQLException {
        this.dao = new UserDAO(ServerConfig.MysqlDBURL, ServerConfig.DBUsername, ServerConfig.DBPassword);
        System.out.println("Connected to SQLServer at " + ServerConfig.MysqlDBURL);
        this.serverDelegate = serverDelegate;
    }
    
    public Response getUserInfo(Account account) {
        try {
            if(this.serverDelegate.isSignedIn(new UserInfo(account.getUsername()))) {
                return new Response(Common.ResponseHeader.Error, "This account is already signed in");
            }
            
            Optional<UserInfo> user = dao.getUserInfo(account);
            return user.map((userInfo) -> new Response(Common.ResponseHeader.Success, userInfo))
                        .orElse(new Response(Common.ResponseHeader.Error, "Your username or password was wrong!"));
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.systemError();
        }
    }

//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
}

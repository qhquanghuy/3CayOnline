/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import Config.ServerConfig;
import java.sql.SQLException;
import pkg3cayonlineserversharedmodel.UserDAO;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Response;

/**
 *
 * @author huynguyen
 */
public class LoginController {
    
    private final UserDAO dao;
//    private Account account;

//    public LoginController(Account account) {
//        this.account = account;
//    }
    
    public LoginController() throws SQLException {
        this.dao = new UserDAO(ServerConfig.MysqlDBURL, ServerConfig.DBUsername, ServerConfig.DBPassword);
        System.out.println("Connected to SQLServer at " + ServerConfig.MysqlDBURL);
    }
    
    public Response getUserInfo(Account account) {
        try {
            return dao.getUserInfo(account)
                    .map((userInfo) -> new Response(200, userInfo))
                    .orElse(new Response(403, "Your username or password was wrong!"));
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

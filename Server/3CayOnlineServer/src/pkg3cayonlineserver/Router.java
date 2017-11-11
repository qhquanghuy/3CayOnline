/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import java.sql.SQLException;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;

/**
 *
 * @author huynguyen
 */
public class Router {
    private static LoginController loginController;
    
    
    public static LoginController getLoginController() throws SQLException {
        if(loginController == null) {
            return new LoginController();
        }
        return loginController;
    }
    
    public static Response parse(Request request) {
        try {
            loginController = getLoginController();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.systemError();
        }
        Common.RequestURI uri = (Common.RequestURI) request.getHeader();
        switch (uri) {
            case Login:
                return loginController.getUserInfo((Account) request.getData());
            case Register:
            default:
                return Response.resourceNotFound();
        }
    }
}

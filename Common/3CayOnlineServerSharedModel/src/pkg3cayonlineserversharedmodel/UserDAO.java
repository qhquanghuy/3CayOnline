/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserversharedmodel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.UserInfo;

/**
 *
 * @author huynguyen
 */
public class UserDAO implements Serializable {
    
    private final Connection conn;
    
    public UserDAO(String dbURL, String username, String password) throws SQLException {
        this.conn = DriverManager.getConnection(dbURL, username, password);
    }
    
    public Optional<UserInfo> getUserInfo(Account acc) throws SQLException {
        String sql = "select id, username, firstname, lastname, score from User "
                   + "where username = ? and password = ?";
        PreparedStatement preparedStm = this.conn.prepareStatement(sql);
        preparedStm.setString(1, acc.getUsername());
        preparedStm.setString(2, acc.getPassword());
        ResultSet result = preparedStm.executeQuery();
        
        if(result.next()) {
            UserInfo userInfo = new UserInfo(result.getInt(1),
                                            result.getString(2),
                                            result.getString(3), 
                                            result.getString(4), 
                                            result.getInt(5));
            return Optional.of(userInfo);
        }
        return Optional.empty();
    }   
    
//    public Optional<UserInfo> getUserInfo(int id) {
//        
//    } 
}

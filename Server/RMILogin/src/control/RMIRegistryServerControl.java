package control;



import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import pkg3cayonlinesharedmodel.RMIRegistryInterface;
import pkg3cayonlineserversharedmodel.UserDAO;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Player;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class RMIRegistryServerControl extends UnicastRemoteObject implements RMIRegistryInterface {
    private Registry registry;
    private Connection con;
    private String rmiService="rmiRegistryServer";
//    private PlayerDAO dao;
    
    public RMIRegistryServerControl() throws RemoteException, SQLException{
        this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/3cayonline", "root", "");
        try {
            registry = LocateRegistry.createRegistry(Common.Config.RMIServer.Port);
            registry.rebind(rmiService, this);
        } catch (RemoteException e) {
            throw e;
        }
    }
    
    public String checkRegistry(Player user) throws RemoteException{
        String result="";
        if(checkPlayer(user))
                result="ok";
        return result; 
    }
    public boolean checkPlayer(Player user){
        String query="Select * FROM user WHERE username='"+user.getUsername()+"'";
        try {
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void addPlayer (Player user) throws SQLException{
        String query="INSERT INTO user(username,password,firstname,lastname) VALUES(?,?,?,?)";
        PreparedStatement prstm=null;
        try {
            prstm = con.prepareStatement(query);
            prstm.setString(1, user.getUsername());
            prstm.setString(2, user.getPassword());
            prstm.setString(3, user.getFirstname());
            prstm.setString(4, user.getLastname());
            prstm.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public String addRegistry(Player user) throws RemoteException {
        try {
            addPlayer(user);
        } catch (SQLException ex) {
            Logger.getLogger(RMIRegistryServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ok";//To change body of generated methods, choose Tools | Templates.
    }





}

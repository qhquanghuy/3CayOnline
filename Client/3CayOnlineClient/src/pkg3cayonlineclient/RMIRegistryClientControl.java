/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Player;
import pkg3cayonlinesharedmodel.RMIRegistryInterface;

/**
 *
 * @author Administrator
 */
public class RMIRegistryClientControl {
    private RMIRegistryInterface rmiServer;
    private Registry registry;
    private String rmiService="rmiRegistryServer";
    
    public RMIRegistryClientControl(){
        try {
            registry=LocateRegistry.getRegistry(Common.Config.RMIServer.Host,Common.Config.RMIServer.Port);
            rmiServer= (RMIRegistryInterface)(registry.lookup(rmiService));
            
        } catch (RemoteException e) {
            e.printStackTrace();
        }catch(NotBoundException e){
            e.printStackTrace();
        }
    }
    
    public String remoteCheckRegistry(Player user){
        String result=null;
        try {
            result= rmiServer.checkRegistry(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
    public String remoteAddRegistry(Player user){
        String result=null;
        try {
            result= rmiServer.addRegistry(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}

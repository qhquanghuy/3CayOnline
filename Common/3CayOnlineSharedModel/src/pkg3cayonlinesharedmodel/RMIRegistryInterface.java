/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Administrator
 */
public interface RMIRegistryInterface extends Remote{
    public String checkRegistry(Player user) throws RemoteException;
    public String addRegistry(Player user) throws RemoteException;
}

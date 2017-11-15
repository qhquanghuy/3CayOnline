/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

/**
 *
 * @author huynguyen
 */

public final class Common {
    /// Configuration...
    public final class Config {
        public final class TCPServer {
            static public final String Host = "localhost";
            static public final int Port = 1352;
        }
        
        public final class RMIServer {
            static public final String Host = "localhost";
            static public final int Port = 1353;
        }
        
        
    }
    
    /// Request name
    
    public enum RequestURI {
        SignIn,
        SignOut,
        CreateRoom,
        LeaveRoom,
        JoinRoom,
        GetOnlineUsers,
        
    }
    public enum ResponseHeader {
        Error,
        Success,
        
        // NOTIFCATIONs
        AUserOnline,
        AUserLeftRoom,
        AUserLeftGame,
        
        ARoomCreated,
        ARoomRemoved,
        ARoomUpdated,
    }
    
    public enum GameRoomStatus {
        Playing,
        Waiting
    }
}
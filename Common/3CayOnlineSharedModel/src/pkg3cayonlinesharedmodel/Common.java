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
            static public final int Port = 1551;
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
        StartGame,
    }
    public enum ResponseHeader {
        Error,
        Success,
        // NOTIFCATIONs
        AUserOnline,
        AUserLeftRoom,
        AUserLeftGame,
        AUserJoinGame,
        
        ARoomCreated,
        ARoomRemoved,
        ARoomUpdated,
        ARoomStarted,
    }
    
    public enum GameRoomStatus {
        Playing,
        Waiting
    }
    
    public enum CardType {
        Heart(3),
        Diamonds(4),
        Clubs(2),
        Spades(1);

        private final int intVal;

        public int getIntVal() {
            return intVal;
        }

        private CardType(int intVal) {
            this.intVal = intVal;
        }

        @Override
        public String toString() {
            switch (this) {
                case Diamonds: return "♦";
                case Heart: return "♥";
                case Spades: return "♠";
                case Clubs: return "♣";
            }
            return null;
        }
        
        
        
    }
    
//    public enum GameRoomPosition {
//        Bottom(0),
//        Right(1),
//        Left(2),
//        Top(3);
//        private int value;
//        GameRoomPosition(int val) {
//            this.value = val;
//        }
//        public int getVal() {
//            return value;
//        }
//        
//    }
}
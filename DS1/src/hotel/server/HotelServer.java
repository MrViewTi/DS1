package hotel.server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import hotel.IBookingManager;
import hotel.BookingManager;

public class HotelServer {
    private static final String _hotelName = "My Hotel";
    private static Logger logger = Logger.getLogger(HotelServer.class.getName());

    public static void main(String[] args) throws RemoteException{
        // set security manager if non existent
        if(System.getSecurityManager() != null)
            System.setSecurityManager(null);

        IBookingManager bookingManager= new BookingManager();

        // locate registry
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry();
        } catch(RemoteException e) {
            logger.log(Level.SEVERE, "Could not locate RMI registry.");
            System.exit(-1);
        }

        // register hotel
        IBookingManager stub;
        try {
            stub = (IBookingManager) UnicastRemoteObject.exportObject(bookingManager, 0);
            registry.rebind(_hotelName, stub);
            logger.log(Level.INFO, "<{0}> is registered in the RMI registry.", _hotelName);
            logger.log(Level.INFO, "<{0}> is ready to serve requests.", _hotelName);
        } catch(RemoteException e) {
            logger.log(Level.SEVERE, "<{0}> Could not get stub bound in the RMI registry.", _hotelName);
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

package staff;

import java.awt.print.Book;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import hotel.BookingDetail;
import hotel.BookingManager;
import hotel.IBookingManager;

public class BookingClient extends AbstractScriptedSimpleTest {

	private IBookingManager bm = null;
	private static final String _hotelName = "My Hotel";

	public static void main(String[] args) throws Exception {
		BookingClient client = new BookingClient();
		client.run();
	}

	/***************
	 * CONSTRUCTOR *
	 ***************/
	/*
			if (System.getSecurityManager() != null)
			System.setSecurityManager(null);

		// Lookup calculator from RMI registry
		Registry registry = LocateRegistry.getRegistry();
		ICalculator calculator = (ICalculator)registry.lookup(_calculatorName);
		System.out.println("Calculator name " + _calculatorName + " found from the RMI registry.");

		new CalculatorGUI(calculator);
	 */
	public BookingClient() {
		try {
			if (System.getSecurityManager() != null)
				System.setSecurityManager(null);

			Registry registry = LocateRegistry.getRegistry();
			IBookingManager bookingManager = (IBookingManager) registry.lookup(_hotelName);
			System.out.println("Hotel name " + _hotelName + " found from the RMI registry.");

			bm = bookingManager;
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date) {
		try {
			return bm.isRoomAvailable(roomNumber, date);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void addBooking(BookingDetail bookingDetail){
		try {
			bm.addBooking(bookingDetail);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date) {
		try {
			return bm.getAvailableRooms(date);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_SET;
	}

	@Override
	public Set<Integer> getAllRooms() {
		try {
			return bm.getAllRooms();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_SET;
	}
}

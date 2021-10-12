package hotel;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingManager implements IBookingManager{

	private Room[] rooms;

	public BookingManager() {
		this.rooms = initializeRooms();
	}

	@Override
	public Set<Integer> getAllRooms() throws RemoteException{
		Set<Integer> allRooms = new HashSet<Integer>();
		Iterable<Room> roomIterator = Arrays.asList(rooms);
		for (Room room : roomIterator) {
			allRooms.add(room.getRoomNumber());
		}
		return allRooms;
	}

	@Override
	public boolean isRoomAvailable(Integer roomNumber, LocalDate date)  throws RemoteException{
		return getAvailableRooms(date).contains(roomNumber);
	}

	@Override
	public void addBooking(BookingDetail bookingDetail)  throws RemoteException{
		int roomNumber = bookingDetail.getRoomNumber();
		for(Room room : rooms){
			if(room.getRoomNumber() == roomNumber){
				room.getBookings().add(bookingDetail);
			}
		}
	}

	@Override
	public Set<Integer> getAvailableRooms(LocalDate date)  throws RemoteException{
		Set<Integer> availableRooms = getAllRooms();
		for(Room room : rooms){
			List<BookingDetail> bookings = room.getBookings();
			for(BookingDetail booking : bookings){
				if(booking.getDate().equals(date))
					availableRooms.remove(booking.getRoomNumber());
			}

		}
		return availableRooms;
	}

	private static Room[] initializeRooms() {
		Room[] rooms = new Room[4];
		rooms[0] = new Room(101);
		rooms[1] = new Room(102);
		rooms[2] = new Room(201);
		rooms[3] = new Room(203);
		return rooms;
	}
}

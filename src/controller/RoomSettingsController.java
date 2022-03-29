package controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import model.Appointment;
import model.Room;
import view.viewAUI.RoomSettingsViewAUI;

public class RoomSettingsController {
	/**
	 * Interface used to refresh the View .
	 */
	private RoomSettingsViewAUI roomSettingsViewAUI;
	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private ManTheSController manTheSController;

	/**
	 * Searches Room with the given name = searchTerm
	 * 
	 * @param searchTerm
	 * @preconditions searchTerm != Null
	 */
	public RoomSettingsController(RoomSettingsViewAUI roomSettingsViewAUI) {
		this.roomSettingsViewAUI = roomSettingsViewAUI;
	}

	public RoomSettingsController() {

	}

	/**
	 * Update the name and the capacity of a given room
	 * 
	 * @param room     the room to Update
	 * @param name     the new name to set
	 * @param capacity the new capacity to set
	 */
	public void updateRoom(Room room, String name, int capacity) {
		Room locRoom = room;
		Optional<Room> oRoom = manTheSController.getOffice().getRooms().stream()
				.filter(currentRoom -> currentRoom.equals(locRoom)).findFirst();
		
		
		Room newR = new Room();
		newR.setMaxCustomer(capacity);
		newR.setName(name);
		if(manTheSController.getOffice().getRooms().stream().anyMatch(roo -> roo.getName().equals(newR.getName()))) {
			manTheSController.getMainWindowAUI().showError("Raum existiert bereits");
			return;
		}
		if (oRoom.isPresent()) {
			room = oRoom.get();
			room.setName(name);
			room.setMaxCustomer(capacity);
		} else {
			Room newRoom = new Room();
			newRoom.setMaxCustomer(capacity);
			newRoom.setName(name);
			manTheSController.getOffice().getRooms().add(newRoom);
		}

		roomSettingsViewAUI.refreshRoomList();
	}

	/**
	 * Delete a Room
	 * 
	 * @param room the room to delete
	 */
	public void deleteRoom(Room room) {
		List<Appointment> futureAppointments = manTheSController.getOffice()
				.getAppointmentsForDates(LocalDateTime.now(), LocalDateTime.MAX);
		boolean cantBeDeleted = futureAppointments.stream().anyMatch(app -> app.getRoom().equals(room));
		if (cantBeDeleted) {
			manTheSController.getMainWindowAUI()
					.showError("Mindestens ein zukünftiger Termin findet in diesem Raum statt.");
		} else {
			this.manTheSController.getOffice().getRooms().remove(room);
			roomSettingsViewAUI.refreshRoomList();
		}

	}

	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	public void setRoomSettingsViewAUI(RoomSettingsViewAUI roomSettingsViewAUI) {

		this.roomSettingsViewAUI = roomSettingsViewAUI;
	}

}
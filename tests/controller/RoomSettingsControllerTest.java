package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Office;
import model.Room;
import view.viewAUI.RoomSettingsViewAUI;

public class RoomSettingsControllerTest {
	
 
	private RoomSettingsController rSc;
	private Office office = new Office();
	private Room testRoom = new Room();
	private int capacity;
	private boolean done;
	

	@Before
	public void setUp() throws Exception {
		
		rSc = new RoomSettingsController();
		
		ManTheSController manTheSController = new ManTheSController();
		rSc.setManTheSController(manTheSController);
		manTheSController.setOffice(office);
		manTheSController.setRoomSettingsController(rSc);
		testRoom.setName("TestName");
		rSc.setRoomSettingsViewAUI(new RoomSettingsViewAUI() {
			@Override
			public void refreshRoomList() {
			}
		});
		
	}

	
	@Test
	public void updateRoomTest() {
		rSc.updateRoom(testRoom, "NewNameTest", capacity);
		done = office.getRooms().contains(testRoom);
		assertTrue(done);
	}
	
	@Test
	public void deleteRoomTest() {
		rSc.deleteRoom(testRoom);
		done = !office.getRooms().contains(testRoom);
		assertTrue(done);
	}

}

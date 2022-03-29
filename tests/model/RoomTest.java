package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RoomTest {
	
	Room room1;
	Room room2;

	@Before
	public void setUp() throws Exception {
		room1 = new Room();
		room1.setMaxCustomer(3);
		room1.setName("room1");
		
		room2 = new Room();
		room2.setMaxCustomer(3);
		room2.setName("room1");
		
	}

	@Test
	public void test() {
		assertTrue(room1.equals(room2));
		room2.setName("room2");
		assertFalse(room1.equals(room2));
	}
	
	@Test
	public void testValidFalse() {
		Room room = new Room();
		assertFalse(room.isValid());
	}
	
	@Test
	public void testValidTrue() {
		assertTrue(room1.isValid());
	}

}

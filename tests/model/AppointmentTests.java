package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AppointmentTests {
	Appointment appointment1;
	Appointment appointment2;

	LocalDateTime localDateTime1 = LocalDateTime.now();
	LocalDateTime localDateTime2 = LocalDateTime.now().plusMinutes(10);
	LocalDateTime localDateTime3 = LocalDateTime.now().plusMinutes(20);
	LocalDateTime localDateTime4 = LocalDateTime.now().plusMinutes(30);

	/**
	 * Sets up both appointments having the same data.
	 */
	@Before
	public void setup() {
		appointment1 = new Appointment();
		appointment2 = new Appointment();

		Employee employee = new Employee();
		appointment1.setEmployee(employee);
		appointment2.setEmployee(employee);

		List<Customer> customers = new ArrayList<Customer>();
		customers.add(new Customer());
		appointment1.setCustomers(customers);
		appointment2.setCustomers(customers);

		Room room = new Room();
		appointment1.setRoom(room);
		appointment2.setRoom(room);

		TreatmentType treatmentType = new TreatmentType();
		appointment1.setTreatmentType(treatmentType);
		appointment2.setTreatmentType(treatmentType);
		treatmentType.addVariant(10, 30);
		appointment1.setPriceDurationTupel(treatmentType.getVariants().get(0));
		appointment2.setPriceDurationTupel(treatmentType.getVariants().get(0));

		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime3);
		appointment1.setTimeFrame(timeFrame1);
		appointment2.setTimeFrame(timeFrame1);
	}

	/**
	 * Should be true because the same employee and overlapping timeFrames.
	 */
	@Test
	public void testCollideOnEmployeeTrue() {

		appointment2.setCustomers(new ArrayList<Customer>());
		appointment2.setRoom(new Room());

		boolean collides = appointment1.collides(appointment2);
		assertTrue(collides);
	}

	/**
	 * Should be true because the same customer and overlapping timeFrames.
	 */
	@Test
	public void testCollideOnRoomTrue() {

		// Set different customer and employee
		appointment2.setCustomers(new ArrayList<Customer>());
		appointment2.setEmployee(new Employee());

		boolean collides = appointment1.collides(appointment2);
		assertTrue(collides);
	}

	/**
	 * Should be true because the same room and overlapping timeFrames.
	 */
	@Test
	public void testCollideOnCustomerTrue() {

		// set different employee and room
		appointment2.setEmployee(new Employee());
		appointment2.setRoom(new Room());

		boolean collides = appointment1.collides(appointment2);
		assertTrue(collides);
	}

	/**
	 * Should be false because timeFrames don't overlap.
	 */
	@Test
	public void testCollideFalse() {
		TimeFrame timeFrame1 = new TimeFrame(localDateTime1, localDateTime2);
		TimeFrame timeFrame2 = new TimeFrame(localDateTime3, localDateTime4);

		appointment1.setTimeFrame(timeFrame1);
		appointment2.setTimeFrame(timeFrame2);

		boolean collides = appointment1.collides(appointment2);
		assertFalse(collides);
	}

	@Test
	public void testEquals() {
		assertTrue(appointment1.equals(appointment2));
		appointment1.setCustomers(new ArrayList<Customer>());
		appointment1.setEmployee(new Employee());
		assertFalse(appointment1.equals(appointment2));
	}

	@Test
	public void testEqualsEmpty() {
		Appointment empty1 = new Appointment();
		Appointment empty2 = new Appointment();
		assertTrue(empty1.equals(empty2));
	}

	@Test
	public void testEmptyTrue() {
		Appointment appointment = new Appointment();
		assertTrue(appointment.isEmpty());
	}

	@Test
	public void testEmptyFalse() {
		Appointment appointment = new Appointment();
		appointment.setEmployee(new Employee());
		appointment.setRoom(new Room());
		appointment.setTreatmentType(new TreatmentType());
		appointment.setTimeFrame(new TimeFrame(LocalDateTime.MIN, LocalDateTime.MAX));
		assertFalse(appointment.isEmpty());
	}

	@Test
	public void testAddBillsForCustomerList() {
		Customer customer = new Customer();
		appointment1.setCustomers(Collections.singletonList(customer));

		assertTrue(customer.getBills().isEmpty());
		appointment1.addBillsForCustomerList();
		assertTrue(customer.getBills().get(0).getPrice() == appointment1.getPriceDurationTupel().getPrice());

		TreatmentType treatmentType = appointment1.getTreatmentType();
		treatmentType.addVariant(30, 200);

		appointment1.setPriceDurationTupel(treatmentType.getVariants().get(1));
		assertTrue(customer.getBills().get(0).getPrice() == appointment1.getPriceDurationTupel().getPrice());
	}
}

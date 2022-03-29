package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class OfficeTests {
	
	private Office testOffice = new Office();
	
	@Test
    public void testGetActiveTreatmentTypes() {
		TreatmentType testTreatmentType1 = new TreatmentType();
		testTreatmentType1.setActive(false);
		TreatmentType testTreatmentType2 = new TreatmentType();
		testTreatmentType2.setActive(true);
		testOffice.getTreatmentTypes().add(testTreatmentType1);
		testOffice.getTreatmentTypes().add(testTreatmentType2);
		Set<TreatmentType> testActiveTreatmentTypes = testOffice.getActiveTreatmentTypes();
		assertEquals(testActiveTreatmentTypes.size(), 1);
    }
	
	@Test
    public void testGetAppointmentsForDates() {
		Appointment testAppointment1 = new Appointment();
		Appointment testAppointment2 = new Appointment();
		
		LocalDateTime localDateTime10 = LocalDateTime.now();
		LocalDateTime localDateTime20 = LocalDateTime.now().plusDays(2);
		LocalDateTime localDateTime30 = LocalDateTime.now().plusDays(3);
		LocalDateTime localDateTime11 = LocalDateTime.now().plusMinutes(20);
		LocalDateTime localDateTime31 = LocalDateTime.now().plusDays(3).plusMinutes(20);
		
		TimeFrame timeFrame1 = new TimeFrame(localDateTime10, localDateTime11);
		TimeFrame timeFrame3 = new TimeFrame(localDateTime30, localDateTime31);
		
		testAppointment1.setTimeFrame(timeFrame1);
		testAppointment2.setTimeFrame(timeFrame3);
		
		testOffice.getAppointments().add(testAppointment1);
		testOffice.getAppointments().add(testAppointment2);
		
		List<Appointment> testAppointmentsForDates = testOffice.getAppointmentsForDates(localDateTime10, localDateTime20);
		assertEquals(testAppointmentsForDates.size(), 1);
    }
	
	@Test
    public void testGetAppointmentsForWrongDates() {
		Appointment testAppointment1 = new Appointment();
		Appointment testAppointment2 = new Appointment();
		
		LocalDateTime localDateTime10 = LocalDateTime.now();
		LocalDateTime localDateTime20 = LocalDateTime.now().plusDays(2);
		LocalDateTime localDateTime30 = LocalDateTime.now().plusDays(3);
		LocalDateTime localDateTime11 = LocalDateTime.now().plusMinutes(20);
		LocalDateTime localDateTime31 = LocalDateTime.now().plusDays(3).plusMinutes(20);
		
		TimeFrame timeFrame1 = new TimeFrame(localDateTime10, localDateTime11);
		TimeFrame timeFrame3 = new TimeFrame(localDateTime30, localDateTime31);
		
		testAppointment1.setTimeFrame(timeFrame1);
		testAppointment2.setTimeFrame(timeFrame3);
		
		testOffice.getAppointments().add(testAppointment1);
		testOffice.getAppointments().add(testAppointment2);
		
		List<Appointment> testAppointmentsForDates = testOffice.getAppointmentsForDates(localDateTime20, localDateTime10);
		assertEquals(testAppointmentsForDates.size(), 0);
    }
	
	@Test
	public void testEquals() {
		Office office1 = new Office();
		Office office2 = new Office();
		Office office3 = new Office();
		
		office1.setName("office1");
		office1.setStreet("street");
		office1.setHouseNumber("13");
		office1.setPostalCode("44134");
		office1.setCity("city");
		
		office2.setName("office1");
		office2.setStreet("street");
		office2.setHouseNumber("13");
		office2.setPostalCode("44134");
		office2.setCity("city");
		
		office3.setName("office3");
		office3.setStreet("street");
		office3.setHouseNumber("13");
		office3.setPostalCode("44134");
		office3.setCity("city");
		
		assertTrue(office1.equals(office2));
		assertFalse(office1.equals(office3));
	}
	
	@Test
	public void testIsValidTrue() {
		Office office1 = new Office();
		
		office1.setName("office1");
		office1.setStreet("street");
		office1.setHouseNumber("13");
		office1.setPostalCode("44134");
		office1.setCity("city");
		office1.setPhoneNumber("123455");
		
		assertTrue(office1.isValid());
	}
	
	@Test
	public void testIsValidFalse() {
		Office office = new Office();
		assertFalse(office.isValid());
	}
}

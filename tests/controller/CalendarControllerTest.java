package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Appointment;
import model.Customer;
import model.Employee;
import model.Office;
import model.Role;
import model.Room;
import model.TimeFrame;
import model.TreatmentType;
import view.viewAUI.AppointmentViewAUI;
import view.viewAUI.CalendarViewAUI;

public class CalendarControllerTest {
	final LocalDateTime localDateTime1 = LocalDateTime.now();
	final LocalDateTime localDateTime2 = LocalDateTime.now().plusMinutes(10);
	final LocalDateTime localDateTime3 = LocalDateTime.now().plusMinutes(20);
	final LocalDateTime localDateTime4 = LocalDateTime.now().plusMinutes(30);
	private Office testOffice;
	private CalendarController testController;
	private Appointment oldAppointment;
	private Appointment newAppointment;
	private Employee hans;
	private Employee franz;
	AppointmentViewAUIImpl appointmentViewAUIImpl;

	@Before
	public void setup() {
		testOffice = setUpOffice();
		testController = new CalendarController();
		appointmentViewAUIImpl = new AppointmentViewAUIImpl();
		testController.setAppointmentViewAUI(appointmentViewAUIImpl);
		testController.setCalendarViewAUI(new CalendarViewAUI() {

			@Override
			public void refreshCalendarView() {
			}
		});
		ManTheSController manTheSController = new ManTheSController();
		manTheSController.setOffice(testOffice);
		testController.setManTheSController(manTheSController);
		oldAppointment = setUpOld();
		hans = setUpHans();
		oldAppointment.setEmployee(hans);
		hans.addSkill(oldAppointment.getTreatmentType());
		testOffice.getAppointments().add(oldAppointment);
		newAppointment = setUpNew();
		franz = setUpFranz();
		newAppointment.setEmployee(franz);
		franz.addSkill(newAppointment.getTreatmentType());
	}

	private Office setUpOffice() {
		Office office = new Office();
		TimeFrame allDayEveryDay = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
				LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)));
		TimeFrame noWork = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
				LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
		TimeFrame[] workingHours = { allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, noWork, noWork };
		office.setOpeningTimes(workingHours);
		return office;
	}

	private Employee setUpFranz() {
		Employee employee = new Employee();
		employee.setStreet("Musterstraße");
		employee.setHouseNumber("30");
		employee.setPostalCode("44269");
		employee.setCity("Dortmund");
		employee.setDayOfBirth(LocalDate.now().minusYears(30));
		employee.setFirstName("Franz");
		employee.setLastName("Nachname");
		employee.setMobile("2352jnk23");
		employee.setPassword("password");
		employee.setRole(Role.EMPLOYEE);
		employee.setUsername("franz");
		TimeFrame allDayEveryDay = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
				LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)));
		TimeFrame noWork = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
				LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
		TimeFrame[] workingHours = { allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, noWork, noWork };
		employee.setWorkingHours(workingHours);
		return employee;
	}

	private Employee setUpHans() {
		Employee hans = setUpFranz();
		hans.setFirstName("hans");
		hans.setUsername("hans");
		return hans;
	}

	@Test
	public void updateAppointmentTestUpdateValid() {
		testController.updateAppointment(oldAppointment, newAppointment);
		boolean isUpdated = testOffice.getAppointments().stream()
				.anyMatch(appointment -> appointment.equals(oldAppointment) && appointment.equals(newAppointment));
		assertTrue(isUpdated);
	}

	@Test
	public void updateAppointTestAdd() {
		Appointment emptyAppointment = new Appointment();
		testController.updateAppointment(emptyAppointment, newAppointment);
		assertTrue(testOffice.getAppointments().contains(newAppointment));
	}

	@Test
	public void updateAppointTestUpdateInvalid() {
		Appointment inValidAppointment = new Appointment();
		testController.updateAppointment(oldAppointment, inValidAppointment);
		assertTrue(testOffice.getAppointments().contains(oldAppointment)
				&& !testOffice.getAppointments().contains(inValidAppointment));
	}

	@Test
	public void updateAppointTestAddInvalid() {
		Appointment emptyAppointment = new Appointment();
		Appointment inValidAppointment = new Appointment();
		testController.updateAppointment(emptyAppointment, inValidAppointment);
		assertTrue(!testOffice.getAppointments().contains(emptyAppointment)
				&& !testOffice.getAppointments().contains(inValidAppointment));
	}

	@Test
	public void updateAppointTestValidCollision() {
		newAppointment.setTimeFrame(oldAppointment.getTimeFrame());
		testController.updateAppointment(oldAppointment, newAppointment);
		boolean isUpdated = testOffice.getAppointments().stream()
				.anyMatch(appointment -> appointment.equals(oldAppointment) && appointment.equals(newAppointment));
		assertTrue(isUpdated);
	}

	@Test
	public void updateAppointTestInvalidCollision() {
		newAppointment.setTimeFrame(oldAppointment.getTimeFrame());
		Appointment emptyAppointment = new Appointment();
		testController.updateAppointment(emptyAppointment, newAppointment);
		assertFalse(testOffice.getAppointments().contains(newAppointment));
	}

	@Test
	public void deleteAppointmentTest() {
		Appointment testAppointment = new Appointment();
		testOffice.getAppointments().add(testAppointment);
		testController.deleteAppointment(testAppointment);
		Collection<Appointment> allAppointments = testOffice.getAppointments();
		boolean success = false;
		if (!allAppointments.contains(testAppointment)) {
			success = true;
		}
		assertTrue(success);
	}

	@Test
	public void deleteAppointmentInThePastTest() {
		oldAppointment.setTimeFrame(new TimeFrame(LocalDateTime.MIN, LocalDateTime.MIN.plusSeconds(1)));
		testController.deleteAppointment(oldAppointment);
		assertTrue(testOffice.getAppointments().contains(oldAppointment));
	}

	@Test
	public void updateTreatmentInvalidPriceDurTuple() {
		newAppointment = oldAppointment;
		TreatmentType treatmentType = new TreatmentType();
		treatmentType.setName("newType");
		treatmentType.addVariant(10, 50);
		newAppointment.getEmployee().getSkills().add(treatmentType);
		newAppointment.setPriceDurationTupel(treatmentType.getVariants().get(0));
		testController.updateAppointment(oldAppointment, newAppointment);
		assertTrue(appointmentViewAUIImpl.errorOccured);
	}

	@Test
	public void updateTreatmentValidPriceDurTuple() {
		newAppointment = setUpOld();
		newAppointment.setEmployee(oldAppointment.getEmployee());
		TreatmentType treatmentType = new TreatmentType();
		treatmentType.setName("newType");
		treatmentType.addVariant(10, 50);
		testOffice.getTreatmentTypes().add(treatmentType);
		newAppointment.getEmployee().getSkills().add(treatmentType);
		newAppointment.setTreatmentType(treatmentType);
		newAppointment.setPriceDurationTupel(treatmentType.getVariants().get(0));
		testController.updateAppointment(oldAppointment, newAppointment);
		assertFalse(appointmentViewAUIImpl.errorOccured);
	}

	private Appointment setUpOld() {
		Customer oldCustomer = new Customer();
		List<Customer> oldCustomerList = new ArrayList<Customer>();
		Room oldRoom = new Room();
		TimeFrame oldTimeFrame = new TimeFrame(localDateTime1, localDateTime2);
		TreatmentType oldTreatmentType = new TreatmentType();
		oldTreatmentType.setName("oldType");
		oldTreatmentType.addVariant(10, 20);

		Appointment oldAppointment = new Appointment();
		oldCustomerList.add(oldCustomer);
		oldAppointment.setCustomers(oldCustomerList);
		oldAppointment.setEmployee(hans);
		oldAppointment.setRoom(oldRoom);
		oldAppointment.setTimeFrame(oldTimeFrame);
		oldAppointment.setTreatmentType(oldTreatmentType);
		oldAppointment.setPriceDurationTupel(oldTreatmentType.getVariants().get(0));
		return oldAppointment;
	}

	private Appointment setUpNew() {
		Customer newCustomer = new Customer();
		List<Customer> newCustomerList = new ArrayList<Customer>();
		Room newRoom = new Room();
		TimeFrame newTimeFrame = new TimeFrame(localDateTime3, localDateTime4);
		TreatmentType newTreatmentType = new TreatmentType();
		newTreatmentType.addVariant(10, 20);
		newTreatmentType.setName("Stuhlyoga");
		newCustomerList.add(newCustomer);

		Appointment newAppointment = new Appointment();
		newAppointment.setCustomers(newCustomerList);
		newAppointment.setEmployee(franz);
		newAppointment.setRoom(newRoom);
		newAppointment.setTimeFrame(newTimeFrame);
		newAppointment.setTreatmentType(newTreatmentType);
		newAppointment.setPriceDurationTupel(newTreatmentType.getVariants().get(0));
		return newAppointment;
	}

	class AppointmentViewAUIImpl implements AppointmentViewAUI {

		boolean errorOccured = false;

		@Override
		public void closeWindow() {
			// TODO Auto-generated method stub

		}

		@Override
		public void showValidationError(String error) {
			errorOccured = true;
		}

		@Override
		public void refreshAppointmentView(Appointment appointment) {
			// TODO Auto-generated method stub

		}
	}
}
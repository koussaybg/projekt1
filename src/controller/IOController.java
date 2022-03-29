package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import model.Appointment;
import model.Bill;
import model.Customer;
import model.Employee;
import model.HTMLBill;
import model.Office;
import model.PriceDurationTupel;
import model.Role;
import model.Room;
import model.TimeFrame;
import model.TreatmentType;
import sun.dc.path.PathException;

/**
 * @author Konstantin (sopr052), Severin
 */

public class IOController {

	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private ManTheSController manTheSController;
	private String dataFilePath = "office.ser";;

	/**
	 * default constructor
	 *
	 * @param manTheSController the manTheSConroller
	 */

	public IOController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	/**
	 * Exports the List of Appointments given in the Paramter according to the
	 * iCalender-format. The List is based on the filtered Appointments from the
	 * Calender-Tab. Throws illegal argument exception if list is empty.
	 *
	 * @param appointmentList List of Appointments, which are supposed to be
	 *                        exported.
	 * @param filePath        the path where the appointments should be saved.
	 *                        Should have ending .ics
	 */
	public void exportAppointments(List<Appointment> appointmentList, String filePath) throws IOException {
		if (appointmentList.isEmpty()) {
			throw new IllegalArgumentException("You should pass at least one appointment!");
		}
		List<String> lines = new ArrayList<>();
		lines.add("BEGIN:VCALENDAR");
		lines.add("PRODID:-//Sopra/ManTheS//DE");
		lines.add("VERSION:2.0");
		for (Appointment appointment : appointmentList) {
			List<String> eventLines = createEventICal(appointment);
			lines.addAll(eventLines);
		}
		lines.add("END:VCALENDER");
		Path file = Paths.get(filePath);
		Files.write(file, lines, StandardCharsets.UTF_8);
	}

	/**
	 * create iCal event lines from appointment as list of string
	 *
	 * @param appointment appointment to create list from. Should not be empty!
	 * @return event as list of strings
	 */
	private List<String> createEventICal(Appointment appointment) {

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
		List<String> lines = new ArrayList<>();

		lines.add("BEGIN:VEVENT");
		lines.add("SUMMARY:" + appointment.getTreatmentType().getName());
		TimeFrame time = appointment.getTimeFrame();
		lines.add("DTSTART:" + dateFormatter.format(time.getStart()));
		lines.add("DTEND:" + dateFormatter.format(time.getStart()));
		lines.add("LOCATION:" + appointment.getRoom().getName());
		lines.add("DESCRIPTION:" + appointment.getEmployee().getUsername()); // TODO: extend description
		return lines;
	}

	/**
	 * Imports a Backup from the given filePath. Shows an Error on the GUI if an
	 * Exception occurs. Stores is back to the ManTheSController
	 *
	 * @param filePath The Path where the Backup is supposed to be stored.
	 * @throws IOException thrown if file not found
	 */
	public void importBackup(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			final Office importedOffice = (Office) ois.readObject();
			this.manTheSController.setOffice(importedOffice);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
	}

	/**
	 * Exports a Backup to the given filePath. Shows an Error on the GUI if an
	 * Exception occurs. The Office and all Model-classes related to the Office must
	 * implement the <<serialize>>-Interface!
	 *
	 * @param filePath The Path where the Backup is supposed to be stored.
	 * @throws IOException thrown if backup file couldn't be created
	 */
	public void exportBackup(String filePath) throws IOException {
		Office currentOffice = manTheSController.getOffice();
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(currentOffice);
		oos.close();
	}

	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	/**
	 * loads office from harddisk to manTheSController. If no office exists, it
	 * creates a new one with a default user.
	 * 
	 * @return
	 */
	public void loadOffice() {
		try {
			importBackup(dataFilePath);
		} catch (IOException e) {
			Office office = createDefaultOffice();
			
			addTreatmentTypes(office);
			addRooms(office);
			addEmployees(office);
			addCustomers(office);
			addDefaultAppointment(office);
			
			manTheSController.setOffice(office);
		}
	}

	private void addDefaultAppointment(Office office) {
		Appointment app1 = new Appointment();
		app1.setEmployee(office.getEmployees().get(0));
		app1.setTreatmentType(app1.getEmployee().getSkills().get(0));
		app1.setPriceDurationTupel(app1.getTreatmentType().getVariants().get(0));
		app1.setRoom(office.getRooms().get(0));
		app1.setTimeFrame(new TimeFrame(LocalDateTime.parse("2019-12-06T10:00:00"),
				LocalDateTime.parse("2019-12-06T10:00:00").plusMinutes(app1.getPriceDurationTupel().getDuration())));
		List<Customer> attendees = Arrays.asList(office.getCustomers().get(0));
		app1.setCustomers(attendees);
		office.getAppointments().add(app1);
	}

	private void addCustomers(Office office) {
		Customer customer1 = new Customer();
		customer1.setFirstName("Max");
		customer1.setLastName("Mustermann");
		customer1.setPostalCode("311144");
		customer1.setCity("Dorf");
		customer1.setStreet("Hauptstraße");
		customer1.setHouseNumber("10");
		customer1.setPhone("0341235121");
		customer1.setMobile("01123412341");
		customer1.setDayOfBirth(LocalDate.now().minusYears(70).minusDays(20));
		office.getCustomers().add(customer1);
		Customer customer2 = new Customer();
		customer2.setFirstName("Otto");
		customer2.setLastName("Normal-Verbraucher");
		customer2.setPostalCode("445423");
		customer2.setCity("Gassenhalle");
		customer2.setStreet("Stiftstraße");
		customer2.setHouseNumber("4c");
		customer2.setPhone("01312414");
		customer2.setMobile("0124661124");
		customer2.setDayOfBirth(LocalDate.now().minusYears(10).minusDays(80));
		customer2.setPublicInsurance(true);
		office.getCustomers().add(customer2);
	}

	private void addEmployees(Office office) {
		Employee chef = new Employee();
		chef.addSkill(office.getTreatmentTypes().get(0));
		chef.addSkill(office.getTreatmentTypes().get(2));
		chef.addSkill(office.getTreatmentTypes().get(1));
		chef.addVacation(new TimeFrame(LocalDateTime.now(), LocalDateTime.now().plusDays(2)), false);
		chef.addVacation(new TimeFrame(LocalDateTime.now().minusDays(5), LocalDateTime.now()), true);
		chef.addVacation(new TimeFrame(LocalDateTime.now(), LocalDateTime.now().plusDays(5)), false);
		chef.setFirstName("Karl");
		chef.setLastName("Dr. Hess");
		chef.setUsername("admin");
		chef.setPassword("admin");
		chef.setRole(Role.BOSS);
		chef.setPostalCode(office.getPostalCode());
		chef.setCity(office.getCity());
		chef.setStreet(office.getStreet());
		chef.setHouseNumber(office.getHouseNumber());
		chef.setPhone("0134124124");
		chef.setMobile("012316612");
		chef.setDayOfBirth(LocalDate.now().minusYears(53).plusDays(80));
		chef.setWorkingHours(createOpeningTimes());
		office.getEmployees().add(chef);
		Employee deputy = new Employee();
		deputy.addSkill(office.getTreatmentTypes().get(1));
		deputy.addSkill(office.getTreatmentTypes().get(2));
		deputy.setFirstName("Mila");
		deputy.setLastName("Müller");
		deputy.setUsername("deputy");
		deputy.setPassword("deputy");
		deputy.setRole(Role.DEPUTY);
		deputy.setPostalCode(office.getPostalCode());
		deputy.setCity(office.getCity());
		deputy.setStreet("woanders");
		deputy.setHouseNumber("9");
		deputy.setPhone("0131234124");
		deputy.setMobile("0114146664");
		deputy.setDayOfBirth(LocalDate.now().minusYears(44).minusDays(201));
		deputy.setWorkingHours(createOpeningTimes());
		office.getEmployees().add(deputy);
		Employee empl = new Employee();
		empl.addSkill(office.getTreatmentTypes().get(0));
		empl.addSkill(office.getTreatmentTypes().get(2));
		empl.addSkill(office.getTreatmentTypes().get(1));
		empl.setFirstName("Mandy");
		empl.setLastName("McLaren");
		empl.setUsername("empl");
		empl.setPassword("empl");
		empl.setRole(Role.EMPLOYEE);
		empl.setPostalCode(office.getPostalCode());
		empl.setCity(office.getCity());
		empl.setStreet("sonstwostr.");
		empl.setHouseNumber("1a");
		empl.setPhone("0324334124");
		empl.setMobile("0114123441");
		empl.setDayOfBirth(LocalDate.now().minusYears(23));
		empl.setWorkingHours(createOpeningTimes());
		office.getEmployees().add(empl);
	}

	private void addRooms(Office office) {
		Room roomA = new Room();
		roomA.setName("0-01");
		roomA.setMaxCustomer(1);
		office.getRooms().add(roomA);
		Room roomB = new Room();
		roomB.setName("0-02");
		roomB.setMaxCustomer(5);
		office.getRooms().add(roomB);
		Room roomC = new Room();
		roomC.setName("1-01");
		roomC.setMaxCustomer(22);
		office.getRooms().add(roomC);
	}

	private void addTreatmentTypes(Office office) {
		List<PriceDurationTupel> treatmentVariants = createDefaultTreatmentVariants();
		TreatmentType treatmentA = new TreatmentType();
		List<PriceDurationTupel> treatmentASet = new ArrayList<PriceDurationTupel>();
		treatmentASet.addAll(treatmentVariants);
		treatmentA.setActive(true);
		treatmentA.setGroup(true);
		treatmentA.setName("Physio");
		treatmentA.setVariants(treatmentASet);
		office.getTreatmentTypes().add(treatmentA);
		TreatmentType treatmentB = new TreatmentType();
		List<PriceDurationTupel> treatmentBSet = new ArrayList<PriceDurationTupel>();
		treatmentBSet.add(treatmentVariants.get(0));
		treatmentBSet.add(treatmentVariants.get(2));
		treatmentB.setActive(true);
		treatmentB.setGroup(false);
		treatmentB.setName("Yoga");
		treatmentB.setVariants(treatmentBSet);
		office.getTreatmentTypes().add(treatmentB);
		TreatmentType treatmentC = new TreatmentType();
		List<PriceDurationTupel> treatmentCSet = new ArrayList<PriceDurationTupel>();
		treatmentCSet.add(treatmentVariants.get(0));
		treatmentCSet.add(treatmentVariants.get(1));
		treatmentC.setActive(false);
		treatmentC.setGroup(true);
		treatmentC.setName("Massage");
		treatmentC.setVariants(treatmentCSet);
		office.getTreatmentTypes().add(treatmentC);
	}

	private List<PriceDurationTupel> createDefaultTreatmentVariants() {
		PriceDurationTupel variationA = new PriceDurationTupel();
		variationA.setDuration(100);
		variationA.setPrice(50);

		PriceDurationTupel variationB = new PriceDurationTupel();
		variationB.setDuration(5);
		variationB.setPrice(10);

		PriceDurationTupel variationC = new PriceDurationTupel();
		variationC.setDuration(20);
		variationC.setPrice(5);

		PriceDurationTupel variationD = new PriceDurationTupel();
		variationD.setDuration(15);
		variationD.setPrice(10);
		
		ArrayList<PriceDurationTupel> treatmentVariants = new ArrayList<PriceDurationTupel>();
		treatmentVariants.add(variationA);
		treatmentVariants.add(variationB);
		treatmentVariants.add(variationC);
		treatmentVariants.add(variationD);
		return treatmentVariants;

	}

	private Office createDefaultOffice() {
		Office office = new Office();
		office.setStreet("Musterstraße");
		office.setHouseNumber("30");
		office.setPostalCode("44269");
		office.setCity("Dortmund");
		office.setName("MusterOffice");
		office.setPhoneNumber("0123456789");
		office.setOpeningTimes(this.createOpeningTimes());
		return office;
	}

	public void saveOffice() {
		try {
			exportBackup(dataFilePath);
		} catch (IOException e) {
			System.out.println("Cant save office");
			// Do nothing maybe show error
		}
	}

	private TimeFrame[] createOpeningTimes() {
		TimeFrame[] openingTimes = new TimeFrame[7];

		openingTimes[0] = new TimeFrame(LocalDateTime.parse("2019-01-01T01:00:00"),
				LocalDateTime.parse("2019-01-01T08:00:00"));
		openingTimes[1] = new TimeFrame(LocalDateTime.parse("2019-02-02T02:00:00"),
				LocalDateTime.parse("2019-02-02T20:00:00"));
		openingTimes[2] = new TimeFrame(LocalDateTime.parse("2019-03-03T03:00:00"),
				LocalDateTime.parse("2019-03-03T20:00:00"));
		openingTimes[3] = new TimeFrame(LocalDateTime.parse("2019-04-04T04:00:00"),
				LocalDateTime.parse("2019-04-04T20:00:00"));
		openingTimes[4] = new TimeFrame(LocalDateTime.parse("2019-05-05T05:00:00"),
				LocalDateTime.parse("2019-05-05T20:00:00"));
		openingTimes[5] = new TimeFrame(LocalDateTime.parse("2019-06-06T06:00:00"),
				LocalDateTime.parse("2019-06-06T20:00:00"));
		openingTimes[6] = new TimeFrame(LocalDateTime.parse("2019-07-07T07:00:00"),
				LocalDateTime.parse("2019-07-07T20:00:00"));
		return openingTimes;
	}

	public void printBill(Customer customer, List<Bill> bills, String filePath) throws PathException {
		String billId = String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits() / 1000000000));
		byte[] bill = buildBill(customer, bills, billId).getBytes(StandardCharsets.UTF_8);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			fos.write(bill);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PathException("Pfad kann nicht genutzt werden.");
		} finally {
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				//Can't happen.
			}
		}
	}

	private String buildBill(Customer customer, List<Bill> bills, String billId) {
		HTMLBill htmlBill = new HTMLBill(bills, customer, manTheSController.getOffice(), billId);
		String str = htmlBill.toString();
		return str;
	}

}

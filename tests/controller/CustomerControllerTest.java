package controller;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import model.Appointment;
import model.Bill;
import model.Customer;
import model.Employee;
import model.Office;
import model.Room;
import model.TimeFrame;
import model.TreatmentType;
import view.viewController.CalendarViewController;
import view.viewController.CustomerViewController;
import view.viewController.MainWindowViewController;
import view.viewController.SettingsViewController;

public class CustomerControllerTest {
    private Office testOffice;
    private CostumerController testController;

    @Before
    public void setup() {
        testOffice = new Office();
        testController = new CostumerController(new CustomerViewController());
        MainWindowViewController mainWindowViewController = new MainWindowViewController();
        mainWindowViewController.setCustomerViewController(new CustomerViewController());
        mainWindowViewController.setCalendarViewController(new CalendarViewController());
        SettingsViewController settingsViewController = new SettingsViewController();
        mainWindowViewController.setSettingsViewController(settingsViewController);
        ManTheSController manTheSController = new ManTheSController(mainWindowViewController);
        manTheSController.setOffice(testOffice);
        testController.setManTheSController(manTheSController);
    }
    @Test
    public void updateCustomerTest(){
        Customer oldcustomer = new Customer();
        Customer newcustommer = new Customer();
        testController.updateCustomer(oldcustomer , newcustommer);
        assertTrue(testOffice.getCustomers().contains(newcustommer) && !testOffice.getCustomers().contains(oldcustomer));
    }

    /**
     * Creates an appointment in the past and creates bills for the customer. Afterwards, the customer should have no unpaid bills.
     */
    @Ignore
    @Test
    public void createbillTest(){
    	Appointment appointment = new Appointment();

		Employee employee = new Employee();
		appointment.setEmployee(employee);

		List<Customer> customers = new ArrayList<Customer>();
		Customer customer = new Customer();
		customer.setFirstName("Typ");
		customer.setLastName("mit Nachnamen");
		customer.setCity("Stadt");
		customer.setPostalCode("12345");
		customer.setStreet("Stra�e");
		customer.setHouseNumber("17");
		customers.add(customer);
		appointment.setCustomers(customers);
		
		testOffice.setName("Praxis");
		testOffice.setCity("Dorf");
		testOffice.setPostalCode("123541");
		testOffice.setStreet("Keller");
		testOffice.setHouseNumber("5");

		Room room = new Room();
		appointment.setRoom(room);

		TreatmentType treatmentType = new TreatmentType();
		treatmentType.setName("Stuhlyoga");
		appointment.setTreatmentType(treatmentType);
		treatmentType.addVariant(50, 10);
		appointment.setPriceDurationTupel(treatmentType.getVariants().get(0));

		LocalDateTime localDateTime1 = LocalDateTime.now().minusMinutes(20);
		LocalDateTime localDateTime2 = LocalDateTime.now().minusMinutes(10);
		TimeFrame timeFrame = new TimeFrame(localDateTime1, localDateTime2);
		appointment.setTimeFrame(timeFrame);
		
		appointment.addBillsForCustomerList();
		
		Bill bill1 = new Bill();
		bill1.setItem("Unterhosen");
		bill1.setPrice(8);
		testController.sellItem(customer, bill1);
		List<Bill> unpaidBills = customer.getBills().stream().filter(bill -> !bill.isPaid()).collect(Collectors.toList());
		assertTrue(!unpaidBills.isEmpty());
		
		testController.createBill(customer, "test-bill.html");
		unpaidBills = customer.getBills().stream().filter(bill -> !bill.isPaid()).collect(Collectors.toList());
		
		assertTrue(unpaidBills.isEmpty());
    }
}
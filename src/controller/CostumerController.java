package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import model.Bill;
import model.Customer;
import sun.dc.path.PathException;
import view.viewAUI.CostumerViewAUI;
import view.viewController.CustomerViewController;

/**
 * @author Sopr051(Ben koussay )
 */
public class CostumerController {
	/**
	 * Reference to manTheSController , needed to communicate with the controller
	 * layer.
	 */
	private ManTheSController manTheSController;
	/**
	 * Interface used to refresh the View .
	 */

	private CostumerViewAUI costumerViewAUI;

	public CostumerController(CustomerViewController customerViewController) {
		this.costumerViewAUI = customerViewController;
	}

	public ManTheSController getManTheSController() {
		return manTheSController;
	}

	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	public CostumerViewAUI getCostumerViewAUI() {
		return costumerViewAUI;
	}

	public void setCostumerViewAUI(CostumerViewAUI costumerViewAUI) {
		this.costumerViewAUI = costumerViewAUI;
	}

	/**
	 * Create a new Bill for the given Customer
	 *
	 * @param customer , the new Customer to create .
	 * @param path path to store bill
	 */
	public void createBill(Customer customer, String path) {
		if (customer == null) {
			throw new IllegalArgumentException();
		}
		List<Bill> bills = customer.getBills().stream().filter(bill -> !bill.isPaid()).collect(Collectors.toList());
		try {
			manTheSController.getiOController().printBill(customer, bills, path);
			//only set the bills as paid if the bill could be printed.
			bills.stream().forEach(bill -> bill.setPaid(true));
		} catch (PathException e) {
			manTheSController.getMainWindowAUI().showError(e.getMessage());
		}
	}

	public void createBill(Customer customer) {
		createBill(customer, null);
	}

	public void sellItem(Customer customer, Bill bill) {
		if (customer == null) {
			throw new IllegalArgumentException();
		}
		if (!isValid(bill)) {
			return;
		}
		customer.getBills().add(bill);
	}

	/**
	 * Updates or creates a new Customer. Will also call the view to refresh. If
	 * parameter oldCustomer is null, a new customer will be created.
	 * 
	 * @param oldCustomer old customer to replace. If null, a new customer will be
	 *                    created
	 * @param newCustomer customer to save
	 */
	public void updateCustomer(Customer oldCustomer, Customer newCustomer) {
		if (!isValid(newCustomer)) {
			return;
		}

		Customer searched = oldCustomer;
		Optional<Customer> oldOpt = manTheSController.getOffice().getCustomers().stream()
				.filter(customer -> customer.equals(searched)).findFirst();

		if (oldOpt.isPresent()) {
			oldCustomer = oldOpt.get();
			oldCustomer.setStreet(newCustomer.getStreet());
			oldCustomer.setHouseNumber(newCustomer.getHouseNumber());
			oldCustomer.setPostalCode(newCustomer.getPostalCode());
			oldCustomer.setCity(newCustomer.getCity());
			oldCustomer.setDayOfBirth(newCustomer.getDayOfBirth());
			oldCustomer.setFirstName(newCustomer.getFirstName());
			oldCustomer.setLastName(newCustomer.getLastName());
			oldCustomer.setMobile(newCustomer.getMobile());
			oldCustomer.setPhone(newCustomer.getPhone());
			oldCustomer.setPublicInsurance(newCustomer.isPublicInsurance());
			oldCustomer.setNotes(newCustomer.getNotes());
			oldCustomer.setBills(newCustomer.getBills());
			oldCustomer.setFavoriteEmployee(newCustomer.getFavoriteEmployee());
			oldCustomer.setTreatmentTypes(newCustomer.getTreatmentTypes());

		} else {
			manTheSController.getOffice().getCustomers().add(newCustomer);
		}
		costumerViewAUI.refreshCustomerList();
	}

	private boolean isValid(Customer newCustomer) {
		List<String> errorList = new ArrayList<String>();

		errorList.addAll(newCustomer.getValidationErrors());

		if (errorList.size() > 0) {
			costumerViewAUI.showValidationError(errorList.toString());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks the validity of given bill and shows possible errors to the
	 * mainWindowAui.
	 * 
	 * @param bill
	 * @return
	 */
	private boolean isValid(Bill bill) {
		List<String> errorList = bill.getValidationErrors();
		if (errorList.size() > 0) {
			manTheSController.getMainWindowAUI().showValidationErrors(errorList);
			return false;
		}
		return true;
	}
}

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Appointment implements Serializable, Validatable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -5798137526478133186L;

	/***
	 * Stores {@link Customer}(s) and Bills which attend given appointment.
	 */
	private List<CustomerBillTuple> customerBillTuples = new ArrayList<CustomerBillTuple>();

	private List<Customer> customers = new ArrayList<Customer>();

	/**
	 * Stores {@link Employee} who guides given appointment.
	 */
	private Employee employee;

	/**
	 * Stores {@link Room} in which given appointment takes place.
	 */
	private Room room;

	/**
	 * Stores {@link TreatmentType} which is matter of given appointment.
	 */
	private TreatmentType treatmentType;

	/**
	 * Stores {@link PriceDurationTupel} which is matter of given appointments
	 * tresatmentType.
	 */
	private PriceDurationTupel priceDurationTupel;

	/**
	 * Stores {@link Appointment} which is follow-up appointment of given
	 * appointment
	 */
	private Appointment successor;

	/**
	 * Uses {@link TimeFrame} to represent time at which given appointment takes
	 * place.
	 */
	private TimeFrame timeFrame;

	/**
	 * Checks whether given appointment isEmpty - no values set.
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		if (employee == null && room == null && treatmentType == null && timeFrame == null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Checks whether current appointment collides with the given appointment.
	 * 
	 * @return true if appointment collides, false if not
	 */
	public boolean collides(Appointment other) {
		if (this.getTimeFrame().collidesWith(other.getTimeFrame())) {
			List<Customer> intersection = this.customerBillTuples.stream().map(tuple -> tuple.getCustomer())
					.collect(Collectors.toList());
			intersection.retainAll(other.getCustomerBillTuples().stream().map(tuple -> tuple.getCustomer())
					.collect(Collectors.toList()));
			boolean intersects = !intersection.isEmpty();
			if (this.getRoom().equals(other.getRoom()) || this.getEmployee().equals(other.getEmployee())
					|| intersects) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @return the customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the customers as List<Customer>
	 */
	public List<CustomerBillTuple> getCustomersWithBill() {
		return customerBillTuples;
	}

	/**
	 * Removes all previously set customers and their bills and adds the new
	 * customers with their bills.
	 * 
	 * @param customers the customers to set
	 */
	public void addBillsForCustomerList() {
		// Remove old customers and their bills
		this.customerBillTuples.stream().forEach(tuple -> tuple.getCustomer().getBills().remove(tuple.getBill()));
		// add new
		customers.stream().forEach(customer -> addBillForCustomer(customer));
	}

	/**
	 * Adds the customer to the list as well as his bill. Also adds the bill to the
	 * customers bills.
	 * 
	 * @param customer
	 */
	private void addBillForCustomer(Customer customer) {
		Bill bill = new Bill();
		bill.setItem(createItemString());
		bill.setPrice(this.priceDurationTupel.getPrice());
		bill.setPaid(false);
		CustomerBillTuple tuple = new CustomerBillTuple(customer, bill);
		customer.getBills().add(bill);
		this.customerBillTuples.add(tuple);
	}
	
	private String createItemString() {
		return this.treatmentType.getName() + " : " + this.priceDurationTupel.getDuration() + " Minuten";
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @return the customerBillTuples
	 */
	public List<CustomerBillTuple> getCustomerBillTuples() {
		return customerBillTuples;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * @return the treatmentType
	 */
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}

	/**
	 * @param treatmentType the treatmentType to set
	 */
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	/**
	 * 
	 * @param priceDurationTupel the PriceDurationTupel to set
	 */
	public void setPriceDurationTupel(PriceDurationTupel priceDurationTupel) {
		this.priceDurationTupel = priceDurationTupel;
		this.customerBillTuples.stream().forEach(tuple -> {
			tuple.getBill().setPrice(priceDurationTupel.getPrice());
			tuple.getBill().setItem(createItemString());
		});
	}

	/**
	 * 
	 * @return the priceDurationTupel
	 */
	public PriceDurationTupel getPriceDurationTupel() {
		return this.priceDurationTupel;
	}

	/**
	 * @return the successor
	 */
	public Appointment getSuccessor() {
		return successor;
	}

	/**
	 * @param successor the successor to set
	 */
	public void setSuccessor(Appointment successor) {
		this.successor = successor;
	}

	/**
	 * @return the timeFrame
	 */
	public TimeFrame getTimeFrame() {
		return timeFrame;
	}

	/**
	 * @param timeFrame the timeFrame to set
	 */
	public void setTimeFrame(TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Appointment other = (Appointment) obj;
		if (customers == null) { if (other.customers != null) return false;
		} else if (!customers.equals(other.customers)) return false;
		if (employee == null) { if (other.employee != null) return false;
		} else if (!employee.equals(other.employee)) return false;
		if (priceDurationTupel == null) { if (other.priceDurationTupel != null) return false;
		} else if (!priceDurationTupel.equals(other.priceDurationTupel)) return false;
		if (room == null) { if (other.room != null) return false;
		} else if (!room.equals(other.room)) return false;
		if (successor == null) { if (other.successor != null) return false;
		} else if (!successor.equals(other.successor)) return false;
		if (timeFrame == null) { if (other.timeFrame != null) return false;
		} else if (!timeFrame.equals(other.timeFrame)) return false;
		if (treatmentType == null) { if (other.treatmentType != null) return false;
		} else if (!treatmentType.equals(other.treatmentType)) return false;
		return true;
	}

	@Override
	public List<String> getValidationErrors() {
		String postfix = " muss angegeben werden.";
		List<String> errors = new ArrayList<String>();
		if (Objects.isNull(employee)) {
			errors.add("Mitarbeiter" + postfix);
		}
		if (Objects.isNull(room)) {
			errors.add("Raum" + postfix);
		}
		if (Objects.isNull(treatmentType)) {
			errors.add("Behandlungsart" + postfix);
		}
		if (Objects.isNull(priceDurationTupel)) {
			errors.add("Preis und Dauer müssen angegeben werden.");
		}
		if(treatmentType != null && priceDurationTupel != null && !treatmentType.getVariants().contains(priceDurationTupel)) {
			errors.add("Falsche Dauer angegeben");
		}
		if (Objects.isNull(timeFrame)) {
			errors.add("Datum und Uhrzeit müssen angegeben werden.");
		}
		return errors;
	}
}
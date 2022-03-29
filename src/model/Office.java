package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Office implements Serializable, Validatable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -6650133831019176355L;

	/**
	 * the Name of the Office
	 */
	private String name;

	/**
	 * street of the person
	 */
	private String street;

	/**
	 * house number of the person
	 */
	private String houseNumber;

	/**
	 * postal code of the person
	 */
	private String postalCode;

	/**
	 * city of the person
	 */
	private String city;
	/**
	 * phone Number of the office
	 */
	private String phoneNumber;
	/**
	 * the CurrentUser using the the ManTheS
	 */
	private Employee currentUser;
	/**
	 * the Openinghours of the office
	 */
	private TimeFrame[] openingTimes;
	/**
	 * All Employees of the office
	 */
	private List<Employee> employees;
	/**
	 * All rooms of the office
	 */
	private List<Room> rooms;
	/**
	 * All TreatmentType ,,,,,
	 */
	private List<TreatmentType> treatmentTypes;
	/**
	 * All Customers of the office
	 */
	private List<Customer> customers;
	/**
	 * All Appointment of the office
	 */
	private List<Appointment> appointments;

	/**
	 * default constructor
	 */
	public Office() {
		this.appointments = new ArrayList<>();
		this.customers = new ArrayList<>();
		this.rooms = new ArrayList<>();
		this.employees = new ArrayList<>();
		this.treatmentTypes = new ArrayList<>();
		this.openingTimes = new TimeFrame[7];
	}

	/**
	 * @return all active TreatmentTypes
	 */
	public Set<TreatmentType> getActiveTreatmentTypes() {
		Set<TreatmentType> activeTreatmentsTypes = new HashSet<>();
		for (TreatmentType curr : treatmentTypes) {
			if (curr.isActive()) {
				activeTreatmentsTypes.add(curr);
			}
		}
		return activeTreatmentsTypes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the houseNumber
	 */
	public String getHouseNumber() {
		return houseNumber;
	}

	/**
	 * @param houseNumber the houseNumber to set
	 */
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Employee getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Employee currentUser) {
		this.currentUser = currentUser;
	}

	public TimeFrame[] getOpeningTimes() {
		return openingTimes;
	}

	public void setOpeningTimes(TimeFrame[] openingTimes) {
		this.openingTimes = openingTimes;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}

	/**
	 * @return return all customers
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @return return All Appointments
	 */
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * @return return Appointments in selected time frame
	 */
	public List<Appointment> getAppointmentsForDates(LocalDateTime timeFrameFrom, LocalDateTime timeFrameTo) {
		if (timeFrameFrom.isAfter(timeFrameTo)) {
			return Collections.emptyList();
		}
		TimeFrame timeFrame = new TimeFrame(timeFrameFrom, timeFrameTo);
		List<Appointment> filteredAppointments = this.appointments.stream()
				.filter(appointment -> appointment.getTimeFrame().collidesWith(timeFrame)).collect(Collectors.toList());
		return filteredAppointments;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Office)) {
			return false;
		}

		Office other = (Office) obj;
		if (this.street.equals(other.street) && this.houseNumber.equals(other.houseNumber)
				&& this.postalCode.equals(other.postalCode) && this.city.equals(other.city)
				&& this.name.equals(other.getName())) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> getValidationErrors() {
		List<String> errorList = new ArrayList<String>();
		String postFix = " muss angegeben werden.";
		if (isNullOrEmpty(this.name)) {
			errorList.add("Name" + postFix);
		}
		if (isNullOrEmpty(this.street)) {
			errorList.add("Straße" + postFix);
		}
		if (isNullOrEmpty(this.houseNumber)) {
			errorList.add("Hausnummer" + postFix);
		}
		if (isNullOrEmpty(this.postalCode)) {
			errorList.add("Postleitzahl" + postFix);
		}
		if (isNullOrEmpty(this.city)) {
			errorList.add("Stadt" + postFix);
		}
		if (isNullOrEmpty(this.phoneNumber)) {
			errorList.add("Telefonnummer" + postFix);
		} else {
			if (!isValidPhoneNumber(phoneNumber)) {
				errorList.add("Telefonnummer ist ungültig.");
			}
		}
		return errorList;
	}
}

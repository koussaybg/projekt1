package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class to store data of a person.
 * Used to be infered by {@link Customer} and {@link Employee}
 * @author severin
 * 
 */
/**
 * @author severin
 *
 */
public class Person implements Serializable, Validatable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -7490344228138246517L;

	/**
	 * First name of the person
	 */
	private String firstName;

	/**
	 * Last name of the person
	 */
	private String lastName;

	/**
	 * day of birth of the person
	 */
	private LocalDate dayOfBirth;
	
// TODO remove
//	/**
//	 * address of the person (should contain street, house number, postal code and
//	 * city)
//	 */
//	private String address;
	
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
	 * phone number of the person
	 */
	private String phone;
	
	/**
	 * mobile of the person
	 */
	private String mobile;

	/**
	 * default constructor
	 */
	public Person() {
		super();
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the dayOfBirth
	 */
	public LocalDate getDayOfBirth() {
		return dayOfBirth;
	}

	/**
	 * @param dayOfBirth the dayOfBirth to set
	 */
	public void setDayOfBirth(LocalDate dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

// TODO remove
//	/**
//	 * @return the address
//	 */
//	public String getAddress() {
//		return address;
//	}
//
//	/**
//	 * @param address the address to set
//	 */
//	public void setAddress(String address) {
//		this.address = address;
//	}
	

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

	/**
	 * @return the telephone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the telephone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Person other = (Person) obj;
		if (street == null) { if (other.street != null) return false;
		} else if (!street.equals(other.street)) return false;
		if (houseNumber == null) { if (other.houseNumber != null) return false;
		} else if (!houseNumber.equals(other.houseNumber)) return false;
		if (postalCode == null) { if (other.postalCode != null) return false;
		} else if (!postalCode.equals(other.postalCode)) return false;
		if (city == null) { if (other.city != null) return false;
		} else if (!city.equals(other.city)) return false;
		if (dayOfBirth == null) { if (other.dayOfBirth != null) return false;
		} else if (!dayOfBirth.equals(other.dayOfBirth)) return false;
		if (firstName == null) { if (other.firstName != null) return false;
		} else if (!firstName.equals(other.firstName)) return false;
		if (lastName == null) { if (other.lastName != null) return false;
		} else if (!lastName.equals(other.lastName)) return false;
		if (mobile == null) { if (other.mobile != null) return false;
		} else if (!mobile.equals(other.mobile)) return false;
		if (phone == null) { if (other.phone != null) return false;
		} else if (!phone.equals(other.phone)) return false;
		return true;
	}

	@Override
	public List<String> getValidationErrors() {
		String postfix = " muss angegeben werden.";
		List<String> errors = new ArrayList<String>();
		if (isNullOrEmpty(firstName)) {
			errors.add("Vorname" + postfix);
		}
		if (isNullOrEmpty(lastName)) {
			errors.add("Nachname" + postfix);
		}
		if (Objects.isNull(dayOfBirth) || dayOfBirth.isAfter(LocalDate.now())) {
			errors.add("Geburtstag" + postfix);
		}
		if (isNullOrEmpty(street)) {
			errors.add("Straße");
		}
		if (isNullOrEmpty(houseNumber)) {
			errors.add("Hausnummer");
		}
		if (isNullOrEmpty(postalCode)) {
			errors.add("PLZ");
		}
		if (isNullOrEmpty(city)) {
			errors.add("Ort");
		}
		if (!isValidPhoneNumber(phone) && !isValidPhoneNumber(mobile)) {
			errors.add("Mindestens eine gültige Telefonnummer" + postfix);
		}
		return errors;
	}
}

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class to store data of an employee. An employee is an employee of the office.
 *
 * @author severin
 */

public class Employee extends Person implements Serializable {

    /**
     * UID for serialization
     */
    private static final long serialVersionUID = 6273616223939090768L;

    /**
     * unique user name of the employee Used for login.
     */
    private String username;

    /**
     * password of the employee. Needed for logging in into the application.
     */
    private String password;

    /**
     * skills of the employee. Skills are different treatments, the employee could
     * offer.
     */
    private List<TreatmentType> skills = new ArrayList<TreatmentType>();

    /**
     * unpaid vacations (disease-days) of the employee.
     */
    private List<TimeFrame> vacations = new ArrayList<TimeFrame>();

    /**
     * paid vacations (holidays) of the employee Don't confuse with vacation!
     */
    private List<TimeFrame> paidVacations = new ArrayList<TimeFrame>();

    /**
     * working hours of the employee
     */
    private TimeFrame[] workingHours = new TimeFrame[7];

    /**
     * Role of the employee. Different roles have different permissions.
     */
    private Role role;

    /**
     * Default constructor
     */
    public Employee() {

    }

    /**
     * Adds a skill to the employee.
     *
     * @param skill skill to add
     */
    public void addSkill(TreatmentType skill) {
        this.skills.add(skill);
    }

    /**
     * adds vacation to employee.
     *
     * @param vacation selected time for vacation
     * @param paid     if true, vacation is paid else not
     */
    public void addVacation(TimeFrame vacation, boolean paid) {
        if (paid) {
            this.paidVacations.add(vacation);
        } else {
            this.vacations.add(vacation);
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the workingHours
     */
    public TimeFrame[] getWorkingHours() {
        return workingHours;
    }

    /**
     * @param workingHours the workingHours to set
     */
    public void setWorkingHours(TimeFrame[] workingHours) {
        this.workingHours = workingHours;
    }

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the {@link Role} role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @return the vacations
     */
    public List<TimeFrame> getVacations() {
        return vacations;
    }

    /**
     * @return the skills
     */
    public List<TreatmentType> getSkills() {
        return skills;
    }

    /**
     * @return the paidVacations
     */
    public List<TimeFrame> getPaidVacations() {
        return paidVacations;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Employee other = (Employee) obj;
		if (paidVacations == null) { if (other.paidVacations != null) return false;
		} else if (!paidVacations.equals(other.paidVacations)) return false;
		if (password == null) { if (other.password != null) return false;
		} else if (!password.equals(other.password)) return false;
		if (role != other.role) return false;
		if (skills == null) { if (other.skills != null) return false;
		} else if (!skills.equals(other.skills)) return false;
		if (username == null) { if (other.username != null) return false;
		} else if (!username.equals(other.username)) return false;
		if (vacations == null) { if (other.vacations != null) return false;
		} else if (!vacations.equals(other.vacations)) return false;
		if (!Arrays.equals(workingHours, other.workingHours)) return false;
		return true;
	}

    @Override
    public String toString() {
        return  this.getFirstName() +
                " " +
                this.getLastName();
    }
	// @Override
    // public String toString() {
    //     return "Employee{" +
    //             "firstName='" + getFirstName() + '\'' +
    //             ", lastName='" + getLastName() + '\'' +
    //             ", dayOfBirth=" + getDayOfBirth() +
    //             ", street='" + getStreet() + '\'' +
    //             ", housenumber='" + getHouseNumber() + '\'' +
    //             ", postalcode='" + getPostalCode() + '\'' +
    //             ", city='" + getCity() + '\'' +
    //             ", phone='" + getPhone() + '\'' +
    //             ", mobile='" + getMobile() + '\'' +
    //             "username='" + username + '\'' +
    //             ", password='" + password + '\'' +
    //             ", skills=" + skills +
    //             ", vacations=" + vacations +
    //             ", paidVacations=" + paidVacations +
    //             ", workingHours=" + Arrays.toString(workingHours) +
    //             ", role=" + role +
    //             '}';
    // }
	
	@Override
	public List<String> getValidationErrors() {
		String postfix = " muss angegeben werden";
		List<String> errors = super.getValidationErrors();
		if (isNullOrEmpty(username)) {
			errors.add("Username" + postfix);
		}
		if (isNullOrEmpty(password)) {
			errors.add("Passwort"+postfix);
		}
		if (Objects.isNull(role)) {
			errors.add("Rolle" + postfix);
		}
		return errors;
	}
}

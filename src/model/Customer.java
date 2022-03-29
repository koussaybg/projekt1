package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person implements Serializable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 3470327102950954585L;

	private String notes;
	/**
	 * check if the Customer is Public-insured
	 * if yes , publicInsurance=TRUE .
	 */
	private boolean publicInsurance;
	/**
	 *  the favorite Employee of a the customer
	 */
	private Employee favoriteEmployee;
	/**
	 *  An Array , where all Treatments of the Customer are stored
	 */
	private List<TreatmentType> treatmentTypes = new ArrayList<TreatmentType>();
	/**
	 * An Array , where all of paid and unpaid Bills of the customer are stored
	 */
	private List<Bill> bills = new ArrayList<Bill>();
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the publicInsurance
	 */
	public boolean isPublicInsurance() {
		return publicInsurance;
	}
	/**
	 * @param publicInsurance the publicInsurance to set
	 */
	public void setPublicInsurance(boolean publicInsurance) {
		this.publicInsurance = publicInsurance;
	}
	/**
	 * @return the favoriteEmployee
	 */
	public Employee getFavoriteEmployee() {
		return favoriteEmployee;
	}
	/**
	 * @param favoriteEmployee the favoriteEmployee to set
	 */
	public void setFavoriteEmployee(Employee favoriteEmployee) {
		this.favoriteEmployee = favoriteEmployee;
	}
	/**
	 * @return the treatmentTypes
	 */
	public List<TreatmentType> getTreatmentTypes() {
		return treatmentTypes;
	}
	/**
	 * @param treatmentTypes the treatmentTypes to set
	 */
	public void setTreatmentTypes(List<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	/**
	 * @return the bills
	 */
	public List<Bill> getBills() {
		return bills;
	}
	/**
	 * @param bills the bills to set
	 */
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if(!(obj instanceof Customer)){
			return false;
		}
		
		Customer other = (Customer) obj;
		if(super.equals(other)){
			
			return true;
		}else {
			return false;
		}
	}

	@Override
    public String toString() {
        return  this.getFirstName() +
                " " +
                this.getLastName();
    }
	
}

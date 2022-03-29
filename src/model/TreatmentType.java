package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A treatmenttype is a treatment, offered by the office
 * 
 * @author onurba
 *
 */
public class TreatmentType implements Serializable, Validatable{
	
	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the Treatmenttype
	 */
	private String name;
	
	/**
	 * True, when treatment can be offered for groups
	 */
	private boolean group;
	
	/**
	 * True, when treatment is still offered
	 */
	private boolean active;

	/**
	 * List of variants
	 */
	private List<PriceDurationTupel> variants = new ArrayList<PriceDurationTupel>();

	/**
	 * Adds a variant to the treatmentType.
	 * 
	 * @param price price of the variant
	 * @param duration duration of the variant
	 */
	public void addVariant(int price, int duration) {
		PriceDurationTupel tupel = new PriceDurationTupel();
		tupel.setPrice(price);
		tupel.setDuration(duration);
		variants.add(tupel);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the group
	 */
	public boolean isGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the variants
	 */
	public List<PriceDurationTupel> getVariants() {
		return variants;
	}

	/**
	 * @param variants the variants to set
	 */
	public void setVariants(List<PriceDurationTupel> variants) {
		this.variants = variants;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==this){
			return true;
		}
		if(!(obj instanceof TreatmentType)){
			return false;
		}
		
		TreatmentType other = (TreatmentType) obj;
		if(this.active==other.isActive() && this.name.equals(other.getName())){
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<String> getValidationErrors() {
		List<String> errorList = new ArrayList<String>();
		if(isNullOrEmpty(name)) {
			errorList.add("Name muss angegeben werden.");
		}
		return errorList;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}

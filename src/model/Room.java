package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable, Validatable{

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = 536001775697538527L;

	/**
	 * the max number of customer a room can handle
	 */
	private int maxCustomer;
	/**
	 * the Name of the room
	 */
	private String name;

	public int getMaxCustomer() {
		return maxCustomer;
	}

	public void setMaxCustomer(int maxCustomer) {
		this.maxCustomer = maxCustomer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (maxCustomer != other.maxCustomer)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public List<String> getValidationErrors() {
		List<String> errorList = new ArrayList<String>();
		if(isNullOrEmpty(this.name)) {
			errorList.add("Name darf nicht leer sein.");
		}
		if(maxCustomer <= 0) {
			errorList.add("Personenzahl ist ungültig.");
		}
		return errorList;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
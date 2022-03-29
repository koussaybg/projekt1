package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Sopr051 ( Ben Koussay )
 */

public class Bill implements Serializable, Validatable {

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -4998720409834753418L;
	/**
	 * Variable to check if the Bill is already paid or not paid=true --> Bill paid
	 * !
	 */
	private boolean paid;
	/**
	 * the price to be paid by the Customer
	 */
	private int price;

	/**
	 * treatment or Items written on the Bill
	 */
	private String item;

	/**
	 * the Customer ,who paid , or must pay this bill
	 */
	private Customer customer;

	/**
	 * @return the paid
	 */
	public boolean isPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Bill)) {
			return false;
		}

		Bill other = (Bill) obj;

		if (this.customer.equals(other.getCustomer()) && this.item.equals(other.getItem())
				&& this.paid == other.isPaid() && this.price == other.getPrice()) {
			return true;
		} else {
			return false;

		}
	}

	@Override
	public List<String> getValidationErrors() {
		List<String> errorList = new ArrayList<String>();
		if (this.item.trim().isEmpty()) {
			errorList.add("Bezeichnung darf nicht leer sein.");
		}
		if (this.price <= 0) {
			errorList.add("Preis darf nicht weniger oder gleich 0 sein.");
		}
		return errorList;
	}
}

package model;

import java.util.Objects;

public class CustomerBillTuple {
	private Customer customer;
	private Bill bill;

	public CustomerBillTuple(Customer customer, Bill bill) {
		if(Objects.isNull(customer) || Objects.isNull(bill)) {
			throw new IllegalArgumentException();
		}
		this.customer = customer;
		this.bill = bill;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the bill
	 */
	public Bill getBill() {
		return bill;
	}

}

package model;

import java.util.List;

public class HTMLBill {
	List<Bill> billList;
	Customer customer;
	Office office;
	String billId;
	
	public HTMLBill(List<Bill> billList, Customer customer, Office office, String billId) {
		super();
		this.billList = billList;
		this.customer = customer;
		this.office = office;
		this.billId = billId;
	}
	
	public String toString() {
		String str = "<!DOCTYPE html>" + 
				"<html>" + 
				"	<head>" + 
				"		<meta charset=\"utf-8\" />" + 
				"		<title>Bill</title>" + 
				"		<style type=\"text/css\">" + 
				"		</style>" + 
				"	</head>" + 
				"	<body style = \"padding-left:2cm; padding-right:2cm; padding-top:2cm\">" + 
				"		<b>Rechnung </b>" + 
				"		<div>" + 
				getRecipient(customer) +
				getOffice(office) +
				"		<br>" + 
				"		<p> Rechnungsnummer : " + billId + " </p>" + 
				getBillList() +
				"			<br> <br>" + 
				"			<p style=\"padding-bottom: 2cm\">" + 
				"				Der Beitrag wurde dankend erhalten. <br> <br>" + 
				"				<b>" + office.getName() + "</b>" + 
				"			</p>" + 
				"		</div>" + 
				"	</body>" + 
				"</html>";
		return str;
	}

	private String getBillList() {
		StringBuilder builder = new StringBuilder();
		billList.stream().forEach(item -> {
			builder.append("<li><div style=\"float : left\">" + item.getItem() + "</div><div style=\"float:right\">" + item.getPrice() + "€</div></li>");
		});
		String list = builder.toString();
		
		return "<div style=\"padding-left: 1cm; padding-right:1.5cm\"> <ol>" + 
				list +
				"		</ol>" + 
				"		<hr>" + 
				"	<div style=\"float:right\">" + getSum() + "€</div>" + 
				"</div>";
	}

	private String getSum() {
		int sum = billList.stream().mapToInt(bill -> bill.getPrice()).sum();
		return String.valueOf(sum);
	}

	private String getOffice(Office office) {
		return "<p style=\"text-align: right;\">" + 
				office.getName() + "<br>" + 
				office.getStreet() + " " + office.getHouseNumber()+ "<br>" + 
				office.getPostalCode() + " " + office.getCity() + 
				"</p>";
	}

	private String getRecipient(Customer customer) {
		return "<p style=\"text-align: left;\">" + 
				customer.getFirstName() + " " + customer.getLastName() + "<br>" + 
				customer.getStreet() + " " + customer.getHouseNumber()+ "<br>" + 
				customer.getPostalCode() + " " + customer.getCity() + 
				"</p>";
	}
}

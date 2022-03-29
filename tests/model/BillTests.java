package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BillTests {
	
	Bill testBill;
	Bill testBill2;
	Bill testBill3;

	@Before
	public void setup() {
		Customer testCustomer = new Customer();
		
		testBill = new Bill();
		testBill.setCustomer(testCustomer);
		testBill.setItem("testitem");
		testBill.setPaid(false);
		testBill.setPrice(3);
		
		testBill2 = new Bill();
		testBill2.setCustomer(testCustomer);
		testBill2.setItem("testitem");
		testBill2.setPaid(false);
		testBill2.setPrice(3);
		
		testBill3 = new Bill();
		testBill3.setCustomer(testCustomer);
		testBill3.setItem("testitem2");
		testBill3.setPrice(3);
		
	}


	@Test
	public void testEquals() {

		
		assertTrue(testBill.equals(testBill2));
		assertFalse(testBill.equals(testBill3));

	}

}

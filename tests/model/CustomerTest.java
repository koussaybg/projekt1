package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class CustomerTest {
	
	Customer customer1;
	Customer customer2;
	Employee testEmployee2=new Employee() ;
	@Before
	public void setUp(){
		Employee testEmployee=new Employee() ;
		List<TreatmentType> treatmentType=new LinkedList<>();
		customer1 = new Customer();
		customer1.setFirstName("fn");
		customer1.setLastName("ln");
		customer1.setPhone("phone");
		customer1.setMobile("mobile");
		customer1.setPublicInsurance(true);
		customer1.setFavoriteEmployee(testEmployee);
		customer1.setTreatmentTypes(treatmentType);

		
		customer2 = new Customer();
		customer2.setFirstName("fn");
		customer2.setLastName("ln");
		customer2.setPhone("phone");
		customer2.setMobile("mobile");
		customer2.setPublicInsurance(true);
		customer2.setFavoriteEmployee(testEmployee);
		customer2.setTreatmentTypes(treatmentType);
	}
    @Test
	public void setFirstNametest() {
		assertEquals(customer1,customer2);
		customer2.setFirstName("fn2");
		assertNotEquals(customer1,customer2);
	}
	@Test
	public void setLastNametest() {
		assertEquals(customer1,customer2);
		customer2.setLastName("ln2");
		assertNotEquals(customer1,customer2);
	}
	@Test
	public void setphonetest() {
		assertEquals(customer1,customer2);
		customer2.setPhone("phone2");
		assertNotEquals(customer1,customer2);
	}
	@Test
	public void setMobiletest() {
		assertEquals(customer1, customer2);
		customer2.setMobile("mobile2");
		assertNotEquals(customer1, customer2);
	}

	@Test
	public void testsetPublicInsurance() {
		assertEquals(customer1, customer2);
		customer2.setPublicInsurance(false);
		assertNotEquals(customer1.isPublicInsurance(), customer2.isPublicInsurance());

	}

	@Test
	public void testsetFavoriteEmployee() {
		assertEquals(customer1,customer2);
		testEmployee2.setFirstName("test"); 
		customer2.setFavoriteEmployee(testEmployee2);
		assertNotEquals(customer1.getFavoriteEmployee(),customer2.getFavoriteEmployee());
	}

	@Test
	public void setTreatmentTypes() {
		assertEquals(customer1.getTreatmentTypes(),customer2.getTreatmentTypes());
		TreatmentType treatmentType=new TreatmentType() ;
		List<TreatmentType> testlist=new LinkedList<>() ;
		testlist.add(treatmentType) ;
		customer2.setTreatmentTypes(testlist);
        assertNotEquals(customer1.getTreatmentTypes(),customer2.getTreatmentTypes());
	}

	@Test
	public void testEquals() {
		assertEquals(customer1,customer2);
		customer2=new Customer() ;
		assertNotEquals(customer1,customer2);
	}
}





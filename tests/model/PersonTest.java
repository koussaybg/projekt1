package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {
	
	Person person1;
	Person person2;

	@Before
	public void setUp() throws Exception {
		person1 = new Person();
		person1.setFirstName("fn");
		person1.setLastName("ln");
		person1.setPhone("phone");
		person1.setMobile("mobile");
		
		person2 = new Person();
		person2.setFirstName("fn");
		person2.setLastName("ln");
		person2.setPhone("phone");
		person2.setMobile("mobile");
	}

	@Test
	public void testEquals() {
		assertTrue(person1.equals(person2));
		person2.setMobile("mobile2");
		assertFalse(person1.equals(person2));

	}

}

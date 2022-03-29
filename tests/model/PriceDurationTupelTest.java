package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PriceDurationTupelTest {
	
	PriceDurationTupel priceDurationTupel1;
	PriceDurationTupel priceDurationTupel2;

	@Before
	public void setUp() throws Exception {
		priceDurationTupel1 = new PriceDurationTupel();
		priceDurationTupel1.setDuration(1);
		priceDurationTupel1.setPrice(3);
		

		priceDurationTupel2 = new PriceDurationTupel();
		priceDurationTupel2.setDuration(1);
		priceDurationTupel2.setPrice(3);
		
	}

	@Test
	public void test() {
		assertTrue(priceDurationTupel1.equals(priceDurationTupel2));
		priceDurationTupel2.setDuration(3);
		assertFalse(priceDurationTupel1.equals(priceDurationTupel2));
	}

}

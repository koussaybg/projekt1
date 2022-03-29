package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class TreatmentTypeTest {
    @Test
    public void addVariantTest(){
        TreatmentType treatmentType = new TreatmentType();
        PriceDurationTupel tupel = new PriceDurationTupel();
        tupel.setPrice(50);
        tupel.setDuration(20);
        treatmentType.addVariant(tupel.getPrice() , tupel.getDuration());
        assertTrue(treatmentType.getVariants().contains(tupel));
    }
    
    @Test
    public void testEqual() {
    	TreatmentType treatmentType1= new TreatmentType();
    	treatmentType1.setName("treatmentType1");
    	
    	TreatmentType treatmentType2= new TreatmentType();
    	treatmentType2.setName("treatmentType1");
    	
    	assertTrue(treatmentType1.equals(treatmentType2));
    	
    	treatmentType1.setActive(true);
    	treatmentType2.setActive(true);
    	
    	assertTrue(treatmentType1.equals(treatmentType2));
    	
    	treatmentType2.setName("treatmentType2");
    	
    	assertFalse(treatmentType1.equals(treatmentType2));
    }
}
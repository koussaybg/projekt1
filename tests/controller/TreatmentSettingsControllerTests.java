package controller;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Office;
import model.PriceDurationTupel;
import model.TreatmentType;
import view.viewAUI.MainWindowAUI;
import view.viewAUI.TreatmentSettingsViewAUI;

public class TreatmentSettingsControllerTests {

	private Office office = new Office();
	private TreatmentSettingsController tSC;
	private TreatmentType testTreatmentType;
	private int durationTest;
	private int priceTest;
	private boolean isGroup;

	@Before
	public void setUp() throws Exception {
		ManTheSController manTheSController = new ManTheSController();
		manTheSController.setOffice(office);
		tSC = new TreatmentSettingsController();
		tSC.setTreatmentSettingsViewAUI(new TreatmentSettingsViewAUI() {
			@Override
			public void refreshTreatmentTypeList() {
			}
		});
		tSC.setManTheSController(manTheSController);
		manTheSController.setTreatmentSettingsController(tSC);
		manTheSController.setMainWindowAUI(new MainWindowAUI() {
			
			@Override
			public void showValidationErrors(List<String> errors) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void showError(String error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void refreshMainWindow() {
				// TODO Auto-generated method stub
				
			}
		});
		testTreatmentType = new TreatmentType();
		testTreatmentType.setName("treatmentName");
		testTreatmentType.setActive(true);
		testTreatmentType.addVariant(10, 30);
		office.getTreatmentTypes().add(testTreatmentType);
	}

	@Test
	public void addNewVariantTest() {
		durationTest = 10;
		priceTest = 10;
		tSC.addVariantOrTreatment(testTreatmentType, -1, -1);
		tSC.addVariantOrTreatment(testTreatmentType, durationTest, priceTest);
		testTreatmentType.addVariant(durationTest, priceTest);
		boolean done = office.getTreatmentTypes().contains(testTreatmentType);
		assertTrue(done);
	}
	
	@Test
	public void updateVariantTest() {
		durationTest = 10;
		PriceDurationTupel oldTuple = new PriceDurationTupel();
		oldTuple.setDuration(durationTest);
		oldTuple.setPrice(10);
		PriceDurationTupel newTuple = new PriceDurationTupel();
		newTuple.setDuration(durationTest);
		newTuple.setPrice(30);
		TreatmentType treatmentType = new TreatmentType();
		treatmentType.setName("name");
		treatmentType.setActive(true);
		TreatmentType tr = treatmentType;
		tSC.addVariantOrTreatment(treatmentType, oldTuple.getPrice(), oldTuple.getDuration());
		treatmentType = office.getTreatmentTypes().stream().filter(tt -> tt.equals(tr)).findFirst().get();
		tSC.addVariantOrTreatment(treatmentType, newTuple.getPrice(), newTuple.getDuration());
		assertTrue(treatmentType.getVariants().get(0).getPrice() == newTuple.getPrice());
	}

	@Test
	public void addTreatmentTypeTest() {
		testTreatmentType = new TreatmentType();
		testTreatmentType.setName("testName");
		testTreatmentType.setGroup(isGroup);
		tSC.addVariantOrTreatment(testTreatmentType, 2342, 23423);
		boolean done = office.getTreatmentTypes().contains(testTreatmentType);

		if (isGroup) {
			assertFalse(done);
		} else {
			assertTrue(done);
		}

	}

	@Test
	public void deactivateTreatmentTypeTest() {
		tSC.deactivateTreatmentType(testTreatmentType);
		boolean done = office.getActiveTreatmentTypes().contains(testTreatmentType);
		assertFalse(done);
	}

}

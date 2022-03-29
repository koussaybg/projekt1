package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.PriceDurationTupel;
import model.TreatmentType;
import view.viewAUI.TreatmentSettingsViewAUI;

/**
 * Treatment settings controller.
 * 
 * @author onurba
 *
 */
public class TreatmentSettingsController {

	/**
	 * Reference to manTheSController. Must be provided.
	 */
	private ManTheSController manTheSController;

	/**
	 * Callback for refreshing the view. Must be provided
	 */
	private TreatmentSettingsViewAUI treatmentSettingsViewAUI;

	public TreatmentSettingsController(TreatmentSettingsViewAUI treatmentSettingsViewAUI) {
		this.treatmentSettingsViewAUI = treatmentSettingsViewAUI;
	}

	/**
	 * @param treatmentSettingsViewAUI the treatmentSettingsViewAUI to set
	 */
	public void setTreatmentSettingsViewAUI(TreatmentSettingsViewAUI treatmentSettingsViewAUI) {
		this.treatmentSettingsViewAUI = treatmentSettingsViewAUI;
	}

	public TreatmentSettingsController() {

	}

	/**
	 * Adds variants to the given treatment. Will also call the view to refresh. Set
	 * price and duration to -1 if you want to add a new TreatmentType without
	 * Variation
	 * 
	 * @param treatmenttype treatmentType to add variant
	 * @param price         price of the variant
	 * @param duration      duration of the variant
	 */
	public void addVariantOrTreatment(TreatmentType newTreatmentType, int price, int duration) {
		if (!isValid(newTreatmentType, price, duration)) {
			return;
		}
		Optional<TreatmentType> treatmentTypeOpt = manTheSController.getOffice().getTreatmentTypes().stream()
				.filter(entity -> entity.getName().equals(newTreatmentType.getName())).findFirst();

		if (treatmentTypeOpt.isPresent()) {
			TreatmentType oldTreatmentType = treatmentTypeOpt.get();
			copyValuesFromTo(newTreatmentType, oldTreatmentType);
			Optional<PriceDurationTupel> tupleOpt = oldTreatmentType.getVariants().stream()
					.filter(pdTuple -> pdTuple.getDuration() == duration).findFirst();
			if (tupleOpt.isPresent()) {
				tupleOpt.get().setPrice(price);
			} else {
				oldTreatmentType.addVariant(price, duration);
			}
		} else {
			TreatmentType treatmentType = new TreatmentType();
			copyValuesFromTo(newTreatmentType, treatmentType);
			treatmentType.addVariant(price, duration);
			manTheSController.getOffice().getTreatmentTypes().add(treatmentType);
		}
		treatmentSettingsViewAUI.refreshTreatmentTypeList();
	}

	private void copyValuesFromTo(TreatmentType newTreatmentType, TreatmentType oldTreatmentType) {
		oldTreatmentType.setActive(newTreatmentType.isActive());
		oldTreatmentType.setGroup(newTreatmentType.isGroup());
		oldTreatmentType.setName(newTreatmentType.getName());
	}

	private boolean isValid(TreatmentType treatmentType, int price, int duration) {
		List<String> errorList = new ArrayList<String>();
		errorList.addAll(treatmentType.getValidationErrors());
		if (price < 0) {
			errorList.add("Preis darf nicht negativ sein.");
		}
		if (duration < 0) {
			errorList.add("Länge dar nicht negativ sein.");
		}
		if (errorList.size() > 0) {
			manTheSController.getMainWindowAUI().showValidationErrors(errorList);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * deactivates the given treatment. Will also call the view to refresh.
	 * 
	 * @param treatmenttype treatmenttype to deactivate
	 */
	public void deactivateTreatmentType(TreatmentType treatmentType) {
		if (treatmentType.isActive())
			treatmentType.setActive(false);
		else
			treatmentType.setActive(true);
		treatmentSettingsViewAUI.refreshTreatmentTypeList();

	}

	public void setManTheSController(ManTheSController manTheSController2) {
		this.manTheSController = manTheSController2;

	}

}

package controller;

import model.Office;
import view.viewAUI.OfficeSettingsViewAUI;

public class OfficeSettingsController {
	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private ManTheSController manTheSController;
	/**
	 * Interface used to refresh the View .
	 */
	private OfficeSettingsViewAUI officeSettingsViewAUI;

	public OfficeSettingsController(OfficeSettingsViewAUI officeSettingsViewAUI) {
		this.officeSettingsViewAUI = officeSettingsViewAUI;
	}

	public OfficeSettingsController() {

	}

	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}
	
	public void updateOffice(Office newOffice) {
		if(!newOffice.isValid()) {
			manTheSController.getMainWindowAUI().showValidationErrors(newOffice.getValidationErrors());
		} else {
			Office office = manTheSController.getOffice();
			office.setName(newOffice.getName());
			office.setStreet(newOffice.getStreet());
			office.setHouseNumber(newOffice.getHouseNumber());
			office.setPostalCode(newOffice.getPostalCode());
			office.setCity(newOffice.getCity());
			office.setPhoneNumber(newOffice.getPhoneNumber());
			office.setOpeningTimes(newOffice.getOpeningTimes());
			officeSettingsViewAUI.refreshOfficeSettings();
		}
	}
}

package view.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.Role;

public class SettingsViewController implements ChangeListener<Tab>{

	@FXML
	private TabPane settingsTabs;
	@FXML
	private Tab employeeSettingsTab;
	@FXML
	private Tab treatmentSettingsTab;
	@FXML
	private Tab roomSettingsTab;
	@FXML
	private Tab officeSettingsTab;
	private MainWindowViewController mainWindowViewController;

	@FXML
	private OfficeSettingsViewController officeSettingsViewController;

	@FXML
	private EmployeeSettingsViewController employeeSettingsViewController;

	@FXML
	private TreatmentSettingsViewController treatmentSettingsViewController;

	@FXML
	private RoomSettingsViewController roomSettingsViewController;

	@FXML
	public void initialize(){
		this.employeeSettingsViewController.setSettingsViewController(this);
		this.officeSettingsViewController.setSettingsViewController(this);
		this.treatmentSettingsViewController.setSettingsViewController(this);
		this.roomSettingsViewController.setSettingsViewController(this);
		settingsTabs.getSelectionModel().selectedItemProperty().addListener(this);
	}

	void setMainWindowViewController(MainWindowViewController mwvc) {
		this.mainWindowViewController = mwvc;
	}

	public MainWindowViewController getMainWindowViewController() {
		return this.mainWindowViewController;
	}


	public void refresh() {
		Role currentUserRole = mainWindowViewController.getManTheSController().getOffice().getCurrentUser().getRole();
		boolean isBoss = currentUserRole == Role.BOSS;
		officeSettingsTab.setDisable(!isBoss);
		treatmentSettingsTab.setDisable(!isBoss);
		roomSettingsTab.setDisable(!isBoss);
		this.employeeSettingsViewController.refresh();
		this.officeSettingsViewController.refreshOfficeSettings();
		this.treatmentSettingsViewController.refreshTreatmentTypeList();
		this.roomSettingsViewController.refreshRoomList();
	}

	public EmployeeSettingsViewController getEmployeeSettingsViewController() {
		return this.employeeSettingsViewController;
	}

	public OfficeSettingsViewController getOfficeSettingsViewController() {
		return this.officeSettingsViewController;
	}

	public TreatmentSettingsViewController getTreatmentSettingsViewController() {
		return this.treatmentSettingsViewController;
	}

	public RoomSettingsViewController getRoomSettingsViewController() {
		return this.roomSettingsViewController;
	}

	/**
	 * called if tab changed
	 * @param observable
	 * @param oldTab
	 * @param newTab
	 */
	@Override
	public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		if (newTab.equals(employeeSettingsTab)) {
			employeeSettingsViewController.refresh();
		}
		if(newTab.equals(officeSettingsTab)){
			officeSettingsViewController.refreshOfficeSettings();
		}
		if(newTab.equals(roomSettingsTab)){
			roomSettingsViewController.refreshRoomList();
		}
		if(newTab.equals(treatmentSettingsTab)){
			treatmentSettingsViewController.refreshTreatmentTypeList();
		}
	}
}

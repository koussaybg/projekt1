package view.viewController;

import controller.ManTheSController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import jfxtras.samples.controls.agenda.CalendarView;
import model.Role;
import view.viewAUI.MainWindowAUI;
import view.viewController.*;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class MainWindowViewController implements MainWindowAUI, ChangeListener<Tab> {

	@FXML
	private CustomerViewController customerViewController;

	@FXML
	private CalendarViewController calendarViewController;

	@FXML
	private SettingsViewController settingsViewController;
	@FXML
	private ManTheSController manTheSController;
	@FXML
	private LoginViewController loginViewController;
	@FXML
	private Tab settingsTab;
	@FXML
	private Tab customerTab;
	@FXML
	private Tab calendarTab;
	@FXML
	private TabPane mainViewTabs;




	@FXML
	public void initialize(){
		this.settingsViewController.setMainWindowViewController(this);
		this.customerViewController.setMainWindowViewController(this);
		this.calendarViewController.setMainWindowViewController(this);
		mainViewTabs.getSelectionModel().selectedItemProperty().addListener(this);
	}
	
	public void refresh() {
		Role currentUserRole = getManTheSController().getOffice().getCurrentUser().getRole();
		System.out.println(currentUserRole);
		if(currentUserRole.equals(Role.EMPLOYEE) ){
			settingsTab.setDisable(true); // invisible isn't a function
		}else{
			settingsTab.setDisable(false);
		}
		calendarViewController.init();
		customerViewController.refreshCustomerList();
		settingsViewController.refresh();
	}

	/**
	 * @see view.viewAUI.MainWindowAUI#refreshMainWindow()
	 * 
	 * 
	 */
	public void refreshMainWindow() {
	}

	/**
	 * @see view.viewAUI.MainWindowAUI#showError(String)
	 */
	public void showError(String error) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Error");
		alert.setContentText(error);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}

	public void showValidationErrors(List<String> errors){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Error");
		StringBuilder errorStringBuilder = new StringBuilder();
		for(String error: errors){
			errorStringBuilder.append(error +"\n");
		}
		alert.setContentText(errorStringBuilder.toString());
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}

	/**
	 * @return the customerViewController
	 */
	public CustomerViewController getCustomerViewController() {
		return customerViewController;
	}

	/**
	 * @param customerViewController the customerViewController to set
	 */
	public void setCustomerViewController(CustomerViewController customerViewController) {
		this.customerViewController = customerViewController;
	}

	/**
	 * @return the calendarViewController
	 */
	public CalendarViewController getCalendarViewController() {
		return calendarViewController;
	}

	/**
	 * @param calendarViewController the calendarViewController to set
	 */
	public void setCalendarViewController(CalendarViewController calendarViewController) {
		this.calendarViewController = calendarViewController;
	}

	/**
	 * @return the settingsViewController
	 */
	public SettingsViewController getSettingsViewController() {
		return settingsViewController;
	}

	/**
	 * @param settingsViewController the settingsViewController to set
	 */
	public void setSettingsViewController(SettingsViewController settingsViewController) {
		this.settingsViewController = settingsViewController;
	}

	/**
	 * @return the manTheSController
	 */
	public ManTheSController getManTheSController() {
		return manTheSController;
	}

	/**
	 * @param manTheSController the manTheSController to set
	 */
	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	/**
	 * @return the loginViewController
	 */
	public LoginViewController getLoginViewController() {
		return loginViewController;
	}

	/**
	 * @param loginViewController the loginViewController to set
	 */
	public void setLoginViewController(LoginViewController loginViewController) {
		this.loginViewController = loginViewController;
	}


	/**
	 * called if new tab is selected
	 * @param observable
	 * @param
	 * @param
	 */
	@Override
	public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
		if(newTab.equals(calendarTab)){
			System.out.println("calendar tab selected");
		}
		if(newTab.equals(customerTab)){
			customerViewController.refresh();
		}
		//SettingsTab not needed here.
	}
}

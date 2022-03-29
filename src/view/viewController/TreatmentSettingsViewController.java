package view.viewController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import controller.ManTheSController;
import controller.TreatmentSettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Office;
import model.PriceDurationTupel;
import model.TreatmentType;
import view.viewAUI.TreatmentSettingsViewAUI;

public class TreatmentSettingsViewController implements TreatmentSettingsViewAUI {

	@FXML
	private TableView<TreatmentType> treatmentTypeTable;

	@FXML
	private TableColumn<TreatmentType,String> nameColumn;

	@FXML
	private TableColumn<TreatmentType,Boolean> typeColumn;

	@FXML
	private TableColumn<TreatmentType,Boolean> activeStateColumn;

	@FXML
	private TextField searchTreatmentTextField;

	@FXML
	private CheckBox showDeactivatedCheckBox;

	@FXML
	private ComboBox<Integer> durationComboBox;

	@FXML
	private TextField treatmentNameTextField;

	@FXML
	private TextField durationTextField;

	@FXML
	private TextField priceTextField;

	@FXML
	private CheckBox groupTherapyCheckBox;


	private SettingsViewController settingsViewController;
	private TreatmentType currentTreatment;

	@FXML
	public void initialize() {
		this.refreshOptions(-1);

		nameColumn.setCellValueFactory(new PropertyValueFactory<TreatmentType, String>("name"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<TreatmentType, Boolean>("group"));
		activeStateColumn.setCellValueFactory(new PropertyValueFactory<TreatmentType, Boolean>("active"));

    	treatmentTypeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null)
				onTreatmentSelected(newSelection);
			else
				currentTreatment = null;
		});

		searchTreatmentTextField.textProperty().addListener((obs, oldText, newText) -> {
			if (newText == null)
				return;
			if (oldText == null)
				oldText = "";
			if(!oldText.equals(newText))
				onSearchBoxAction(newText);
		});

		showDeactivatedCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelectedCount) -> {
			onSearchBoxAction(searchTreatmentTextField.getText());
		});

		durationComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue)
				this.refreshOptions(newValue);
		});

		treatmentTypeTable.setPlaceholder(new Label("Es sind noch keine Behandlungen vorhanden."));
	}

	private void onTreatmentSelected(TreatmentType selectedTreatmentType) {
		treatmentNameTextField.setDisable(true);
		durationTextField.setDisable(false);
		priceTextField.setDisable(false);
		groupTherapyCheckBox.setSelected(selectedTreatmentType.isGroup());
		groupTherapyCheckBox.setDisable(true);

		currentTreatment = selectedTreatmentType;
		ObservableList<Integer> allDurations = FXCollections.observableArrayList();
		
		if(selectedTreatmentType.getVariants() != null)
		{
			for (PriceDurationTupel variation : selectedTreatmentType.getVariants()) {
				allDurations.add(variation.getDuration());
			}
		}
		
		Collections.sort(allDurations);
		durationComboBox.getItems().clear();
		durationComboBox.setItems(allDurations);
		durationComboBox.getSelectionModel().select(0);
		
		treatmentNameTextField.setText(currentTreatment.getName());
	}

	public void onAddNewTreatmentButtonClicked(ActionEvent action)
	{
		currentTreatment = null;
		this.refreshOptions(0);
	}

	public void onSearchBoxAction(String searchTerm) {
		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		Office office = manthes.getOffice();

		if(office == null)
			return;

		List<TreatmentType> treatmentTypes = office.getTreatmentTypes();
		Set<TreatmentType> filteredTreatmentTypes;

		if(treatmentTypes == null)
			return;

		if (searchTerm == null)
			refreshTreatmentTypeList();
		else{
			if(showDeactivatedCheckBox.isSelected())
				filteredTreatmentTypes = treatmentTypes.stream().filter(e ->  e.getName().contains(searchTerm)).collect(Collectors.toSet());
			else
				filteredTreatmentTypes = treatmentTypes.stream().filter(e ->  e.isActive() && e.getName().contains(searchTerm)).collect(Collectors.toSet());
			treatmentTypeTable.setItems(FXCollections.observableArrayList(filteredTreatmentTypes));
		}
	}

	public void onNewTreatmentTypeButtonClicked(ActionEvent action) {
		String newName = treatmentNameTextField.getText().trim();
		String newPrice = priceTextField.getText().trim();
		String newDuration = durationTextField.getText().trim();
		boolean isGroup = groupTherapyCheckBox.isSelected();

		int priceValue = 0;
		int durationValue = 0;

		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		TreatmentSettingsController tsc = manthes.getTreatmentSettingsController();
		
		List<String> errorList = new ArrayList<String>();

		if(newName.isEmpty())
			errorList.add("Der Name einer Behandlung darf nicht leer sein oder nur aus Leerzeichen bestehen.");

		TreatmentType newTreatmentType = new TreatmentType();
		newTreatmentType.setName(newName);
		newTreatmentType.setGroup(isGroup);
		newTreatmentType.setActive(true);

		if(!errorList.isEmpty()) {
			mwvc.showValidationErrors(errorList);
			return;
		}

		if(newPrice.isEmpty() && newDuration.isEmpty())
			mwvc.showError("Zu einer Variation muss auch immer eine Dauer und ein Preis festgelegt werden.");
		
		try {
			priceValue = Integer.parseInt(newPrice);
			if(priceValue <= 0)
				errorList.add("Der Preis einer Behandlung muss positiv sein");
		} catch (Exception e) {
			errorList.add("Der angegebene Preis entspricht keiner gültigen Zahl.");
		}

		try {
			durationValue = Integer.parseInt(newDuration);
			if(durationValue <= 0)
				errorList.add("Die Dauer einer Behandlung muss positiv sein");
		} catch (Exception e) {
			errorList.add("Die angegebene Dauer entspricht keiner gültigen Zahl.");
		}

		if(newPrice.isEmpty() || newDuration.isEmpty())
			errorList.add("Zu dem Preis einer Variation muss auch immer eine Dauer festgelegt werden und umgekehrt.");
		
		if(!errorList.isEmpty()) {
			mwvc.showValidationErrors(errorList);
			return;
		}

		tsc.addVariantOrTreatment(newTreatmentType, priceValue, durationValue);
	}

	public void onDeActivateTreatmentTypeButtonClicked(ActionEvent action) {
		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		TreatmentSettingsController tsc = manthes.getTreatmentSettingsController();
		
		if(currentTreatment != null)
			tsc.deactivateTreatmentType(currentTreatment);
	}

	void setSettingsViewController(SettingsViewController svc) {
		this.settingsViewController = svc;
	}


	/**
	 * @see view.viewAUI.TreatmentSettingsViewAUI#refreshTreatmentTypeList()
	 * 
	 *  
	 */
	public void refreshTreatmentTypeList() {
		currentTreatment = null;
		this.refreshOptions(-1);
		searchTreatmentTextField.setText("");

		MainWindowViewController mwvc = settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		Office office = manthes.getOffice();

		if(office == null)
			return;

		ObservableList<TreatmentType> displayedTreatmentTypes = FXCollections.observableArrayList();
		List<TreatmentType> possibleTreatmentTypes = office.getTreatmentTypes();

		if(possibleTreatmentTypes == null)
			return;
		
		if(showDeactivatedCheckBox.isSelected()) {
			for (TreatmentType candidate: possibleTreatmentTypes)
			{
				if(candidate.isActive())
					displayedTreatmentTypes.add(candidate);
			}
			treatmentTypeTable.getSelectionModel().clearSelection();
			treatmentTypeTable.getItems().setAll(displayedTreatmentTypes);	
		}
		else {
			displayedTreatmentTypes.addAll(possibleTreatmentTypes);
			treatmentTypeTable.getSelectionModel().clearSelection();	
			treatmentTypeTable.getItems().setAll(displayedTreatmentTypes);
		}
	}

	/**
	 * 
	 * @param newDuration -1: Alles löschen und disablen. 0: Alles löschen und enablen. Ansonsten: teilw. disablen eintragen
	 */
	private void refreshOptions(int newDuration) {
		switch (newDuration) {
			case -1:
				treatmentNameTextField.setText("");
				durationTextField.setText("");
				priceTextField.setText("");
				treatmentNameTextField.setDisable(true);
				durationTextField.setDisable(true);
				priceTextField.setDisable(true);
				groupTherapyCheckBox.setDisable(true);
				groupTherapyCheckBox.setSelected(false);
				durationComboBox.getItems().clear();
				break;
			case 0:
				treatmentNameTextField.setText("");
				durationTextField.setText("");
				priceTextField.setText("");
				treatmentNameTextField.setDisable(false);
				durationTextField.setDisable(false);
				priceTextField.setDisable(false);
				groupTherapyCheckBox.setDisable(false);
				groupTherapyCheckBox.setSelected(false);
				durationComboBox.getItems().clear();
				break;
			default:
				durationTextField.setText(String.valueOf(newDuration));
				if(currentTreatment != null) {
					for (PriceDurationTupel candidate : currentTreatment.getVariants()) {
						if(candidate.getDuration() == newDuration)
							priceTextField.setText(String.valueOf(candidate.getPrice()));
					}
				}
				break;
		}
	}
}

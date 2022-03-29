package view.viewController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import controller.ManTheSController;
import controller.RoomSettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Office;
import model.Room;
import view.viewAUI.RoomSettingsViewAUI;

public class RoomSettingsViewController implements RoomSettingsViewAUI {

	private SettingsViewController settingsViewController;

	@FXML
	private TableView<Room> roomTableView;

	@FXML
	private TableColumn<Room,String> nameColumn;

	@FXML
	private TableColumn<Room,String> capacityColumn;

	@FXML
	private TextField roomNameTextField;

	@FXML
	private TextField roomCapacityTextField;

	@FXML
	private TextField searchRoomTextField;

    @FXML
    public void initialize() {
		roomNameTextField.setText(null);
		roomCapacityTextField.setText(null);

        nameColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("maxCustomer"));
    	roomTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onRoomSelected(newSelection);
			}
		});

		searchRoomTextField.textProperty().addListener((obs, oldText, newText) -> {
			if (newText == null)
				return;
			if (oldText == null)
				oldText = "";
			if(!oldText.equals(newText))
				onSearchBoxAction(newText);
		});

		roomTableView.setPlaceholder(new Label("Es sind noch keine Räume vorhanden."));
	}

	void setSettingsViewController(SettingsViewController svc) {
		this.settingsViewController = svc;
	}
	
	private void onRoomSelected(Room selectedRoom)
	{
		roomNameTextField.setText(selectedRoom.getName());
		roomCapacityTextField.setText(String.valueOf(selectedRoom.getMaxCustomer()));
	}

	public void onSearchBoxAction(String searchTerm) {
		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		Office office = manthes.getOffice();

		if(office == null)
			return;

		List<Room> rooms = office.getRooms();

		if(rooms == null)
			return;

		if (searchTerm != null) {
			Set<Room> filteredRooms = rooms.stream().filter(e ->  e.getName().contains(searchTerm)).collect(Collectors.toSet());
			roomTableView.setItems(FXCollections.observableArrayList(filteredRooms));
		}
	}

	public void onNewRoomButtonClicked(ActionEvent action) {
		String newName = roomNameTextField.getText();
		String newRoomCapacity = roomCapacityTextField.getText();
		Room selectedRoom = roomTableView.getSelectionModel().getSelectedItem();

		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		RoomSettingsController rsc = manthes.getRoomSettingsController();

		if(selectedRoom != null) {
			mwvc.showError("Es ist ein Raum ausgewählt. Daher kann kein neuer Raum erzeugt werden.");
			return;
		}

		if(newName == "")
			mwvc.showError("Der Name des Raumes darf nicht leer sein.");

		if(newRoomCapacity == "")
			mwvc.showError("Die Raumkapazität darf nicht leer sein.");

		int newCapacity = 0;

		try {
			newCapacity = Integer.parseInt(newRoomCapacity);
			if(newCapacity <= 0)
				mwvc.showError("Die Raumkapazität enthält einen ungültigen Wert (Wert muss größer 0 sein).");
		} catch (Exception e) {
			mwvc.showError("Die angegebene Raumkapazität entspricht keiner gültigen Zahl.");
			return;
		}

		rsc.updateRoom(null, newName, newCapacity);
	}

	public void onDeleteRoomButtonClicked(ActionEvent action) {
		Room selectedRoom = roomTableView.getSelectionModel().getSelectedItem();

		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		RoomSettingsController rsc = manthes.getRoomSettingsController();

		rsc.deleteRoom(selectedRoom);
	}

	public void onCancelButtonClicked(ActionEvent action) {
		searchRoomTextField.setText(null);
		roomNameTextField.setText(null);
		roomCapacityTextField.setText(null);

		refreshRoomList();
	}

	public void onSaveButtonClicked(ActionEvent action) {
		String newName = roomNameTextField.getText();
		String newRoomCapacity = roomCapacityTextField.getText();
		Room selectedRoom = roomTableView.getSelectionModel().getSelectedItem();

		MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		RoomSettingsController rsc = manthes.getRoomSettingsController();

		if(newName == "")
			mwvc.showError("Der Name des Raumes darf nicht leer sein.");

		if(newRoomCapacity == "")
			mwvc.showError("Die Raumkapazität darf nicht leer sein.");

		if(selectedRoom == null)
			mwvc.showError("Es wurde kein Raum zum aktualisieren ausgewählt.");

		int newCapacity = 0;

		try {
			newCapacity = Integer.parseInt(newRoomCapacity);
			if(newCapacity <= 0)
				mwvc.showError("Die Raumkapazität enthält einen ungültigen Wert (Wert muss größer 0 sein).");
		} catch (Exception e) {
			mwvc.showError("Die angegebene Raumkapazität entspricht keiner gültigen Zahl.");
			return;
		}

		rsc.updateRoom(selectedRoom, newName, newCapacity);
	}


	/**
	 * @see view.viewAUI.RoomSettingsViewAUI#refreshRoomList()
	 * 
	 *  
	 */
	public void refreshRoomList() {
		MainWindowViewController mwvc = settingsViewController.getMainWindowViewController();
		ManTheSController manthes = mwvc.getManTheSController();
		Office office = manthes.getOffice();

		final ObservableList<Room> allRooms = FXCollections.observableArrayList();
		allRooms.addAll(office.getRooms());

		roomTableView.getSelectionModel().clearSelection();
		roomTableView.getItems().setAll(allRooms);	
	}
}

package view.viewController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.Employee;
import model.Office;
import model.PriceDurationTupel;
import model.Room;
import model.TimeFrame;
import model.TreatmentType;
import view.viewAUI.AppointmentViewAUI;

/**
 * This is the ViewController for all operations in the appointment GUI.
 * @author sopr054
 */
public class AppointmentViewController implements AppointmentViewAUI {

	private MainWindowViewController mainWindowViewController;
	private Appointment oldAppointment;
	private Appointment newAppointment;

	@FXML
	public DatePicker datePicker;
	public ComboBox<Customer> customerBox;
	public TableView<Customer> attendeeTable;
	public TableColumn<Customer, String> attendeeFirstName;
	public TableColumn<Customer, String> attendeeLastName;
	public ComboBox<TreatmentType> treatmentTypeBox;
	public ComboBox<Employee> employeeBox;
	public ComboBox<Room> roomBox;
	public ComboBox<LocalTime> timeBox;
	public ComboBox<Integer> durationBox;

	private Customer selectedAttendee;
	private Office office;
	private boolean validation;

	private boolean statusCreate = false;

	//ViewLists
	private ObservableList<Employee> employeesAvailable;
	private ObservableList<Customer> attendees;
	private ObservableList<TreatmentType> treatmentTypesAvailable;
	private ObservableList<Room> roomsAvailable;
	private ObservableList<LocalTime> startTimesAvailable;
	private ObservableList<Integer> durationsAvailable;
	private ObservableList<Customer> customersAvailable;
	
	
	// all initialization stuff
	@FXML
	public void initialize() {
		attendeeFirstName.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
		attendeeLastName.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));

		attendeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) { this.selectedAttendee = newSelection; }
		});

		employeeBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue)
				this.refreshEmployeeBox(newValue);
		});

		treatmentTypeBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue)
				this.refreshTreatmentTypeBox(newValue);
		});

		durationBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue)
				this.refreshDurationBox(newValue);
		});

		roomBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			if(newValue != null && newValue != oldValue)
				this.refreshRoomBox(newValue);
		});


		employeesAvailable =FXCollections.observableArrayList();
		attendees = FXCollections.observableArrayList();
		treatmentTypesAvailable = FXCollections.observableArrayList();
		roomsAvailable = FXCollections.observableArrayList();
		startTimesAvailable = FXCollections.observableArrayList();
		durationsAvailable = FXCollections.observableArrayList();
		customersAvailable = FXCollections.observableArrayList();

	}

	public void setMainWindowViewController(MainWindowViewController mwvc) {
		this.mainWindowViewController = mwvc;
	}

	
	// all the action
	public void onCancelButtonClicked(ActionEvent action) {
		System.out.println("CancelButtonClicked");
		setAppointmentToEdit(this.oldAppointment);
	}

	public void onSaveButtonClicked(ActionEvent action) {
		System.out.println("SaveButtonClicked");
		createAppointmentFromInputs();
		this.validation = true;
		mainWindowViewController.getManTheSController().getCalendarController().updateAppointment(this.oldAppointment, this.newAppointment);
		if(validation){
			closeWindow();
		} else {
			setAppointmentToEdit(this.newAppointment);
		}
	}

	public void onSaveAndFollowingButtonClicked(ActionEvent action) {
		System.out.println("SaveAndFollwoingButtonClicked");
		createAppointmentFromInputs();
		Appointment nextAppointment = new Appointment();
		this.newAppointment.setSuccessor(nextAppointment);
		this.validation = true;
		mainWindowViewController.getManTheSController().getCalendarController().updateAppointment(oldAppointment, newAppointment);
		if(validation){
			setAppointmentToEdit(nextAppointment);
		} else {
			setAppointmentToEdit(this.newAppointment);
		}
	}

	public void onAddCustomerButtonClicked(ActionEvent action) {
		System.out.println("AddCustomerButtonClicked");
		if ((this.attendees.size() < 1) || (((treatmentTypeBox.getValue() != null) && treatmentTypeBox.getValue().isGroup()))) {
			if (this.customerBox.getValue() != null  && !this.attendees.contains(this.customerBox.getValue())) {
				this.attendees.add(customerBox.getValue());
				for (Customer customer : attendees) {
					if(!this.newAppointment.getCustomers().contains(customer)) {this.newAppointment.getCustomers().add(customer);}
				}
			}
		} else {
			showValidationError("Gewählte Behandlung muss eine Gruppenbehandlung sein um mehr als einen Patienten hinzuzufügen."); 
		}
		refreshAttendeesTable();
	}

	public void onDeleteCustomerButtonClicked(ActionEvent action) {
		System.out.println("DeleteCustomerButtonClicked");
		if ((treatmentTypeBox.getValue() != null) && treatmentTypeBox.getValue().isGroup()) {
			if (this.attendeeTable.getSelectionModel().getSelectedItem() != null ) {
				this.attendees.remove((Customer) this.attendeeTable.getSelectionModel().getSelectedItem());
				for (Customer customer : attendees) {
					if(this.newAppointment.getCustomers().contains(customer)) {this.newAppointment.getCustomers().remove(customer);}
				}
			}		
		} else {
			showValidationError("Gewählte Behandlung muss eine Gruppenbehandlung sein um Patienten zu entfernen.");
		}
		refreshAttendeesTable();
	}

	public void onDatePicked() {
		
	}
	
	// viewbasics
	@Override
	public void closeWindow() {
		// TODO Auto-generated method stub
		Stage stage = (Stage) roomBox.getScene().getWindow();
		// do what you have to do
		stage.close();
	}

	@Override
	public void showValidationError(String error) {
		// TODO Auto-generated method stub
		this.validation = false;
		Alert a = new Alert(AlertType.WARNING);
		a.setContentText(error);
		a.show();
	}

	//@Override
	public void refreshAppointmentView(Appointment appointment) {
		// TODO Auto-generated method stub
		//refreshDurationBox();
		//refreshAttendeesTable();
		//refreshEmployeeBox();
		//refreshTimeBox();
	}
	
	// all the writing and reading viewstuff
	public void setAppointmentToEdit(Appointment appointment) {
		this.office = mainWindowViewController.getManTheSController().getOffice();
		if(this.office==null) {  return; }
		this.oldAppointment = appointment;
		this.newAppointment = this.oldAppointment;

		// if(this.oldAppointment.isValid())
		// 	statusCreate = false;
		// else
		// 	statusCreate = true;
			
		initializeComboBox();

		if(!newAppointment.isValid()) {
			// durationBox.setDisable(true);
			// employeeBox.setDisable(true);
			// roomBox.setDisable(true);
			// timeBox.setDisable(true);

			treatmentTypeBox.setItems(treatmentTypesAvailable);
			roomBox.setItems(roomsAvailable);
			employeeBox.setItems(employeesAvailable);
			roomBox.setItems(roomsAvailable);
			datePicker.setValue(LocalDateTime.now().toLocalDate());
			customerBox.setItems(customersAvailable);
			timeBox.setItems(startTimesAvailable);
		}
		else {
			durationBox.setDisable(false);
			employeeBox.setDisable(false);
			roomBox.setDisable(false);
			timeBox.setDisable(false);

			treatmentTypeBox.setItems(treatmentTypesAvailable);
			treatmentTypeBox.setValue(this.newAppointment.getTreatmentType());
		
			for (PriceDurationTupel candidate : newAppointment.getTreatmentType().getVariants()) {
				durationsAvailable.add(candidate.getDuration());
			}
			Collections.sort(durationsAvailable);
			durationBox.setItems(durationsAvailable);
			durationBox.setValue(this.newAppointment.getPriceDurationTupel().getDuration());
			employeeBox.setItems(employeesAvailable);
			employeeBox.setValue(newAppointment.getEmployee());
			roomBox.setItems(roomsAvailable);
			roomBox.setValue(newAppointment.getRoom());
			
			datePicker.setValue(LocalDateTime.now().toLocalDate());
			timeBox.setItems(FXCollections.observableArrayList(this.startTimesAvailable));
			timeBox.setValue(LocalDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MINUTES));
			if (this.newAppointment.getTimeFrame() != null) {
				datePicker.setValue(newAppointment.getTimeFrame().getStart().toLocalDate());
				timeBox.setValue(newAppointment.getTimeFrame().getStart().toLocalTime());
			}
			customerBox.setItems(customersAvailable);
			attendees.addAll(newAppointment.getCustomers());
			refreshAttendeesTable();
		}
	}

	private void initializeComboBox() {
		this.treatmentTypesAvailable.clear();
		this.durationsAvailable.clear();
		this.employeesAvailable.clear();
		this.roomsAvailable.clear();
		this.customersAvailable.clear();
		this.startTimesAvailable.clear();

		this.treatmentTypesAvailable.addAll(this.office.getActiveTreatmentTypes());		
		this.employeesAvailable.addAll(this.office.getEmployees());
		this.roomsAvailable.addAll(this.office.getRooms());
		this.customersAvailable.addAll(this.office.getCustomers());
		LocalDateTime startTime = LocalDateTime.now();;
		if (newAppointment.getTimeFrame() != null) {
			startTime = newAppointment.getTimeFrame().getStart();
		}
		LocalTime time = (LocalTime) this.office.getOpeningTimes()[startTime.getDayOfWeek().getValue()-1].getStart().toLocalTime(); 
		while (time.isBefore(this.office.getOpeningTimes()[startTime.getDayOfWeek().getValue()-1].getEnd().toLocalTime())) {
			startTimesAvailable.add(time);
			time = time.plusMinutes(5);
		}

	}

	private void createAppointmentFromInputs() {
		try {
			this.newAppointment.setEmployee(employeeBox.getValue());
			this.newAppointment.setRoom(roomBox.getValue());
			this.newAppointment.setTreatmentType(treatmentTypeBox.getValue());
			List<PriceDurationTupel> priceDurationTupels = this.newAppointment.getTreatmentType().getVariants();
			for (PriceDurationTupel pdt : priceDurationTupels) {
				if (pdt.getDuration() == durationBox.getValue()) {
					this.newAppointment.setPriceDurationTupel(pdt);
				}
			}
			ObservableList<Customer> attendeesList = attendeeTable.getItems();
			for (Customer customer : attendeesList) {
				if(!attendees.contains(customer)) { attendees.add(customer); }
			}
			this.newAppointment.setCustomers(attendees);
			LocalDateTime start = LocalDateTime.of(datePicker.getValue(), timeBox.getValue());
			LocalDateTime end = LocalDateTime.of(datePicker.getValue(), timeBox.getValue()).plusMinutes(durationBox.getValue());
			TimeFrame timeFrame = new TimeFrame(start, end);
			this.newAppointment.setTimeFrame(timeFrame);
		} catch (Exception e) {
			//TODO: handle exception
		}
	}

	private void refreshTreatmentTypeBox(TreatmentType newTreatmentType) {
		//if(statusCreate) {
			durationBox.getItems().clear();
			// employeeBox.getItems().clear();
			// timeBox.getItems().clear();

			// durationBox.setDisable(false);
			// employeeBox.setDisable(false);

			for (PriceDurationTupel candidate : newTreatmentType.getVariants()) {
				durationsAvailable.add(candidate.getDuration());
			}
			durationBox.setItems(durationsAvailable);

		// 	employeesAvailable.clear();
		// 	for (Employee candidate : office.getEmployees()) {
		// 		if(candidate.getSkills().contains(newTreatmentType))
		// 			employeesAvailable.add(candidate);
		// 	}
		// 	employeeBox.setItems(employeesAvailable);
		//}
	}

	private void refreshDurationBox(Integer newDuration)
	{
		if(statusCreate) {
			if(roomBox.getSelectionModel().getSelectedItem() != null)
			timeBox.setDisable(false);
		}

	}

	private void refreshRoomBox(Room newRoom) {
		if(statusCreate) {
			List<Appointment> allAppointments = office.getAppointments();
			Employee currentEmployee = employeeBox.getSelectionModel().getSelectedItem();
			Room currentRoom = roomBox.getSelectionModel().getSelectedItem();
			Integer currentDuration = durationBox.getSelectionModel().getSelectedItem();
			TimeFrame dayOfWork = currentEmployee.getWorkingHours()[datePicker.getValue().getDayOfWeek().getValue()-1];

			if(durationBox.getSelectionModel().getSelectedItem() != null)
				timeBox.setDisable(false);
			
			if(currentDuration == null)
				return;

			int hours = currentDuration/60;
			int minutes = currentDuration%60;
			LocalTime limit = LocalTime.of(hours, minutes);
			List<LocalTime> available = this.getTimelots(limit);
			List<LocalTime> updateTimes = this.getTimelots(limit);

			for (Appointment candidate : allAppointments) {

				if(candidate.getEmployee().equals(currentEmployee)) {
					for (LocalTime timeStamp : available) {

						if(timeStamp.isAfter(candidate.getTimeFrame().getStart().toLocalTime())
							&& timeStamp.isBefore(candidate.getTimeFrame().getEnd().toLocalTime()))
							updateTimes.remove(timeStamp);
						
					}
				}

				if(candidate.getRoom().equals(currentRoom)) {
					for(LocalTime timeStamp : available) {

						if(timeStamp.isAfter(candidate.getTimeFrame().getStart().toLocalTime())
						&& timeStamp.isBefore(candidate.getTimeFrame().getEnd().toLocalTime()))
						updateTimes.remove(timeStamp);
					}
				}
			}

			for (LocalTime timeStamp : available) {
				
				if(timeStamp.isBefore(dayOfWork.getStart().toLocalTime())
				|| timeStamp.isAfter(dayOfWork.getEnd().toLocalTime()))
				updateTimes.remove(timeStamp);
			}

			startTimesAvailable.clear();
			startTimesAvailable.setAll(updateTimes);
			timeBox.getItems().clear();
			timeBox.setItems(startTimesAvailable);
		}
	}

	private void refreshEmployeeBox(Employee newEmployee) {

		roomBox.setDisable(false);
		
		for (Appointment candidate : this.office.getAppointments()) {
				if(candidate.getEmployee().equals(newEmployee))
				employeeBox.getItems().add(newEmployee);
		}	
	}

	private void refreshAttendeesTable() {
		this.attendees.clear();
		this.attendeeTable.getItems().clear();
		if (this.newAppointment.getCustomers() != null) {
			this.attendees.addAll(newAppointment.getCustomers());	
		}
		attendeeTable.getItems().addAll(this.attendees);
	}

	private List<LocalTime> getTimelots(LocalTime newTime) {

		LocalTime time = (LocalTime) office.getOpeningTimes()[datePicker.getValue().getDayOfWeek().getValue()-1].getStart().toLocalTime(); 
		List<LocalTime> times = new ArrayList<LocalTime>();
		LocalTime limit;
		if (newTime != null)
			limit = office.getOpeningTimes()[datePicker.getValue().getDayOfWeek().getValue()-1].getEnd().toLocalTime().minusMinutes(newTime.getMinute());
		else
			limit = office.getOpeningTimes()[datePicker.getValue().getDayOfWeek().getValue()-1].getEnd().toLocalTime();
		while (time.isBefore(limit)) {
			times.add(time);
			time = time.plusMinutes(5);
		}
		return times;
	}
}
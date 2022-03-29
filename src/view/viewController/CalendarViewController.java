package view.viewController;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.Employee;
import model.Office;
import model.Room;
import model.TimeFrame;
import model.TreatmentType;
import view.viewAUI.CalendarViewAUI;
import view.viewController.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.samples.controls.agenda.CalendarView;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import jfxtras.scene.layout.GridPane;
 

/**
 * Controller for all operations in the calendar window.
 * 
 * @author sopr050
 *
 */

public class CalendarViewController implements CalendarViewAUI {
	
	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private MainWindowViewController mainWindowViewController;

	@FXML
	private AppointmentViewController appointmentViewController;
	
	CalendarView calendarView;
	
	@FXML StackPane calendarPane;
	
	@FXML JFXDatePicker calendarDatePicker;
	
	@FXML JFXDatePicker startDatePicker;
	@FXML JFXDatePicker endDatePicker;
	
	@FXML JFXComboBox<String> customerFilter;
	@FXML JFXComboBox<String> employeeFilter;
	@FXML JFXComboBox<String> roomFilter;
	@FXML JFXComboBox<String> treatmentFilter;
	
	List<Customer> customerToShow;
	List<Employee> employeeToShow;
	List<Room> roomToShow;
	List<TreatmentType> treatmentToShow;
	
	List<Appointment> appointmentsToShow;
	
	List<String> customerNamesList;
	List<String> employeeNamesList;
	List<String> roomNamesList;
	List<String> treatmentNamesList;
	
	private Agenda agenda;
	
	public void init() {
		calendarView= new CalendarView();
		Office office = mainWindowViewController.getManTheSController().getOffice();
		this.appointmentsToShow = office.getAppointments();
		Node agenda = calendarView.getPanel(appointmentsToShow);
		this.agenda = (Agenda) agenda;
	
		((Agenda) agenda).newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(LocalDateTimeRange dateTimeRange)
            {
            	onAddAppointmentCalendarClicked(dateTimeRange.getStartLocalDateTime());
                return null;
            }
        });
		
		((Agenda) agenda).setEditAppointmentCallback( (appointment) -> {
			onEditAppointmentCalendarClicked(appointment);
	        return null;
	    });
		
		((Agenda) agenda).setActionCallback( (appointment) -> {
			onEditAppointmentCalendarClicked(appointment);
	        return null;
        });
		
		/*((Agenda) agenda).appointments().add(new Agenda.AppointmentImplLocal()
				.withStartLocalDateTime(LocalDateTime.now().minusMinutes(20))
				.withEndLocalDateTime(LocalDateTime.now().plusMinutes(20))
                .withSummary("K kk kkkkk k k k k ")
                .withDescription("A description "));*/
		//((Agenda) agenda).displayedLocalDateTime().bindBidirectional(calendarDatePicker.getValue().atStartOfDay());
		calendarPane.getChildren().add(0, agenda);
		
		this.customerToShow =  office.getCustomers();
		this.customerNamesList = new ArrayList<String>();
		this.customerNamesList.add("all");
		this.customerToShow.forEach(customer -> {
			customerNamesList.add(customer.getName());
		});
		
		this.employeeToShow =  office.getEmployees();
		this.employeeNamesList = new ArrayList<String>();
		this.employeeNamesList.add("all");
		this.employeeToShow.forEach(employee -> {
			employeeNamesList.add(employee.getName());
		});
		
		this.roomToShow =  office.getRooms();
		this.roomNamesList = new ArrayList<String>();
		this.roomNamesList.add("all");
		this.roomToShow.forEach(room -> {
			roomNamesList.add(room.getName());
		});
		
		this.treatmentToShow =  office.getTreatmentTypes();
		this.treatmentNamesList = new ArrayList<String>();
		this.treatmentNamesList.add("all");
		this.treatmentToShow.forEach(treatment -> {
			treatmentNamesList.add(treatment.getName());
		});
		
		this.customerFilter.setItems(FXCollections.observableArrayList(customerNamesList));
		this.customerFilter.setValue("all");
		this.employeeFilter.setItems(FXCollections.observableArrayList(employeeNamesList));
		this.employeeFilter.setValue("all");
		this.roomFilter.setItems(FXCollections.observableArrayList(roomNamesList));
		this.roomFilter.setValue("all");
		this.treatmentFilter.setItems(FXCollections.observableArrayList(treatmentNamesList));
		this.treatmentFilter.setValue("all");
	}
	/**
	 * Starts process of creating a new appointment. Opens window to create an appointment. Creates an empty appointment object.
	 * 
	 * @param action click on AddAppointmentButton
	 */
	public void onAddAppointmentButtonClicked(ActionEvent action) {
		System.out.println("AddAppointment Button Clicked");
		System.out.println("create empty appointment");
		Appointment newAppointment = new Appointment();
		System.out.println("open AppointmentView");
		openAppointmentView(newAppointment);
	}

	public void onAddAppointmentCalendarClicked(LocalDateTime localDateTime) {
		System.out.println("AddAppointment Button Clicked");
		System.out.println("create empty appointment");
		Appointment newAppointment = new Appointment();
		TimeFrame timeFrame = new TimeFrame(localDateTime, localDateTime.plusMinutes(5));
		newAppointment.setTimeFrame(timeFrame);
		System.out.println("open AppointmentView");
		openAppointmentView(newAppointment);
	}
	
	public void onDateChanged(ActionEvent action) {
		System.out.println("DateChanged");
		agenda.displayedLocalDateTime().set(calendarDatePicker.getValue().atStartOfDay());
	}

	/**
	 * Starts process of editing an existing appointment. Opens editing window with the data of the chosen appointment.
	 * 
	 * @param action click on EditAppointmentButton
	 */
	public void onEditAppointmentButtonClicked(ActionEvent action) {
		System.out.println("EditAppointment Button Clicked");
		System.out.println("get Appointment to edit");
		
		Collection<jfxtras.scene.control.agenda.Agenda.Appointment> selectedAppointments = agenda.selectedAppointments();
		
		if (selectedAppointments.size() == 1) {
			selectedAppointments.forEach(appointment -> {
				String roomName = appointment.getLocation();
				LocalDateTime startDate = appointment.getStartLocalDateTime();
				Office office = mainWindowViewController.getManTheSController().getOffice();
				office.getAppointments().forEach(appointmentInOffice -> {
					if (appointmentInOffice.getRoom().getName().equals(roomName) 
							&& appointmentInOffice.getTimeFrame().getStart().equals(startDate)) {

						openAppointmentView(appointmentInOffice);
					}
						
				});
			});
		}
	}
	
	public void onEditAppointmentCalendarClicked(jfxtras.scene.control.agenda.Agenda.Appointment appointmentCalendar) {
		System.out.println("EditAppointment Button Clicked");
		System.out.println("get Appointment to edit");
		/*List<Appointment> appointments = mainWindowViewController.getManTheSController().getOffice().getAppointments();
		Appointment appointment = appointments.get(0);
		openAppointmentView(appointment);*/
		String roomName = appointmentCalendar.getLocation();
		LocalDateTime startDate = appointmentCalendar.getStartLocalDateTime();
		Office office = mainWindowViewController.getManTheSController().getOffice();
		office.getAppointments().forEach(appointmentInOffice -> {
			if (appointmentInOffice.getRoom().getName().equals(roomName) 
				&& appointmentInOffice.getTimeFrame().getStart().equals(startDate)) {
				openAppointmentView(appointmentInOffice);
			}						
		});		
	}
	
	/**
	 * Starts process of deleting an existing appointment. Opens dialog window for confirmation. 
	 * If action is confirmed, deletes the chosen appointment. Otherwise, interrupts the process. 
	 * 
	 * @param action click on DeleteAppointmentButton
	 */
	public void onDeleteAppointmentButtonClicked(ActionEvent action) {
		System.out.println("DeleteAppointment Button Clicked");
		Collection<jfxtras.scene.control.agenda.Agenda.Appointment> selectedAppointments = agenda.selectedAppointments();
		
		if (selectedAppointments.size() == 1) {
			selectedAppointments.forEach(appointment -> {
				String roomName = appointment.getLocation();
				LocalDateTime startDate = appointment.getStartLocalDateTime();
				Office office = mainWindowViewController.getManTheSController().getOffice();
				Appointment appointmentToDelete = null;
				for(Appointment app : office.getAppointments())
				{
					if (app.getRoom().getName().equals(roomName) 
							&& app.getTimeFrame().getStart().equals(startDate)) {
						appointmentToDelete = app;
						
					}
				}
				if (appointmentToDelete != null) {
					this.mainWindowViewController.getManTheSController().getCalendarController().deleteAppointment(appointmentToDelete);
				}
				
				/*office.getAppointments().forEach(appointmentInOffice -> {
					if (appointmentInOffice.getRoom().getName().equals(roomName) 
							&& appointmentInOffice.getTimeFrame().getStart().equals(startDate)) {
						this.mainWindowViewController.getManTheSController().getCalendarController().deleteAppointment(appointmentInOffice);
						
					}
						
				});*/
			});
		}
		this.filterAppointmentsToShow();
		this.refreshCalendarView();
	}

	/**
	 * Starts process of exporting calendar in the chosen timeline. Shows error if given dates are incorrect. 
	 * Requests list of appointments in timeline. Exports requsted list.
	 * 
	 * @param action click on EditAppointmentButton
	 */
	public void onExportButtonClicked(ActionEvent action) {
		System.out.println("Export Button Clicked");
		if (this.startDatePicker.getValue() != null && this.endDatePicker.getValue() != null) {

			if (this.startDatePicker.getValue().isAfter(this.endDatePicker.getValue())) {
				throw new IllegalArgumentException("Start date can't occure after end date");
			}
			LocalDateTime start = this.startDatePicker.getValue().atStartOfDay();
			LocalDateTime end = this.endDatePicker.getValue().atStartOfDay();
			List<Appointment> appointments = this.mainWindowViewController.getManTheSController().getOffice().getAppointmentsForDates(start, end);
			try {
				mainWindowViewController.getManTheSController().getiOController().exportAppointments(appointments, "iCal.ics");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * Filters appointments in the calendar overview based on given customer.
	 * 
	 * @param action input in CustomerFilter
	 */
	public void onCustomerFilterAction(ActionEvent action) {
		System.out.println("CustomerFilter Button Clicked");
		this.filterAppointmentsToShow();
	}

	/**
	 * Filters appointments in the calendar overview based on given employee.
	 * 
	 * @param action input in EmployeeFilter
	 */
	public void onEmployeeFilterAction(ActionEvent action) {
		System.out.println("EmployeeFilter Button Clicked");
		this.filterAppointmentsToShow();
	}

	/**
	 * Filters appointments in the calendar overview based on given room.
	 * 
	 * @param action input in RoomFilter
	 */
	public void onRoomFilterAction(ActionEvent action) {
		System.out.println("RoomFilter Button Clicked");
		this.filterAppointmentsToShow();
	}

	/**
	 * Filters appointments in the calendar overview based on given treatment type.
	 * 
	 * @param action input in TreatmentFilter
	 */
	public void onTreatmentFilterAction(ActionEvent action) {
		System.out.println("TreatmentFilter Button Clicked");
		this.filterAppointmentsToShow();
	}

	public void filterAppointmentsToShow() {
		System.out.println("Filter Appointments");
		Office office = mainWindowViewController.getManTheSController().getOffice();
		//this.appointmentsToShow.removeAll(this.appointmentsToShow);
		this.appointmentsToShow = new ArrayList<Appointment>();
		office.getAppointments().forEach(appointment -> {
			if (this.customerFilter.getValue() != null && this.employeeFilter.getValue() != null && this.roomFilter.getValue() != null && this.treatmentFilter.getValue() != null) {
				if ( (this.customerNamesList.indexOf(this.customerFilter.getValue()) == 0  || appointment.getCustomers().contains(this.customerToShow.get(this.customerNamesList.indexOf(this.customerFilter.getValue())-1)) )
						&& (this.employeeNamesList.indexOf(this.employeeFilter.getValue()) == 0 || appointment.getEmployee().equals(this.employeeToShow.get(this.employeeNamesList.indexOf(this.employeeFilter.getValue())-1)))
						&& (this.roomNamesList.indexOf(this.roomFilter.getValue()) == 0 || appointment.getRoom().equals(this.roomToShow.get(this.roomNamesList.indexOf(this.roomFilter.getValue())-1)))
						&& (this.treatmentNamesList.indexOf(this.treatmentFilter.getValue()) == 0 || appointment.getTreatmentType().equals(this.treatmentToShow.get(this.treatmentNamesList.indexOf(this.treatmentFilter.getValue())-1)))
						) {
					this.appointmentsToShow.add(appointment);
				}
			}
			
		});
		/*this.appointmentsToShow = office.getAppointments();
		
		this.appointmentsToShow.forEach(appointment -> {
			if (this.customerNamesList.indexOf(this.customerFilter.getValue()) != -1 && !appointment.getCustomers().contains(this.customerToShow.get(this.customerNamesList.indexOf(this.customerFilter.getValue())))) {
				this.appointmentsToShow.remove(appointment);
			}
			else if (this.employeeNamesList.indexOf(this.employeeFilter.getValue()) != -1 && !appointment.getEmployee().equals(this.employeeToShow.get(this.employeeNamesList.indexOf(this.employeeFilter.getValue())))) {
				this.appointmentsToShow.remove(appointment);
			} 
			else if (this.roomNamesList.indexOf(this.roomFilter.getValue()) != -1 && !appointment.getRoom().equals(this.roomToShow.get(this.roomNamesList.indexOf(this.roomFilter.getValue())))) {
				this.appointmentsToShow.remove(appointment);
			} 
			else if (this.treatmentNamesList.indexOf(this.treatmentFilter.getValue()) != -1 && !appointment.getTreatmentType().equals(this.treatmentToShow.get(this.treatmentNamesList.indexOf(this.treatmentFilter.getValue())))) {
				this.appointmentsToShow.remove(appointment);
			}
		});*/
		
		this.refreshCalendarView();
	}

	void setMainWindowViewController(MainWindowViewController mwvc) {
		this.mainWindowViewController = mwvc;
	}

	public AppointmentViewController getAppointmentViewController() {
		return this.appointmentViewController;
	}


	/**
	 * @see view.CalendarViewAUI#refreshCalendarView()
	 */
	public void refreshCalendarView() {
		//this.init();
		//this.appointmentsToShow = mainWindowViewController.getManTheSController().getOffice().getAppointments();
		//this.filterAppointmentsToShow();
		//agenda = (Agenda) calendarView.getPanel(appointmentsToShow);
		calendarView.setNewAppointments(appointmentsToShow);
		calendarPane.getChildren().remove(0);
		calendarPane.getChildren().add(0, agenda);
	}

	private void openAppointmentView(Appointment appointment) {
		System.out.println("open MainView");
		try {
			FXMLLoader appointmentViewLoader = new FXMLLoader(getClass().getResource("/AppointmentView.fxml"));
			AnchorPane appointmentPane = appointmentViewLoader.load();
			this.appointmentViewController = appointmentViewLoader.getController();
			mainWindowViewController.getManTheSController().getCalendarController().setAppointmentViewAUI(this.appointmentViewController);
			appointmentViewController.setMainWindowViewController(this.mainWindowViewController);
			appointmentViewController.setAppointmentToEdit(appointment);
			Stage stage = new Stage();
			Scene scene = new Scene(appointmentPane);
			stage.setTitle("Terminverwaltung");
			stage.setScene(scene);
			stage.initOwner(calendarPane.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL); 
			stage.showAndWait();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}

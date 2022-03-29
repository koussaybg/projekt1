package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import model.Appointment;
import model.TimeFrame;
import view.viewAUI.AppointmentViewAUI;
import view.viewAUI.CalendarViewAUI;

/**
 * The CalendarController handles functionality for the calendarView as well as
 * the appointmentView.
 * 
 * @author sopr057
 *
 */
public class CalendarController {

	/**
	 * Must be provided to be able to communicate with the controller layer.
	 */
	private ManTheSController manTheSController;

	/**
	 * Must be provided to refresh the calendar view.
	 */
	private CalendarViewAUI calendarViewAUI;

	/**
	 * Must be provided to refresh the appointment view.
	 */
	private AppointmentViewAUI appointmentViewAUI;

	public CalendarController(CalendarViewAUI calendarViewAUI) {
		this.calendarViewAUI = calendarViewAUI;
	}

	public CalendarController() {

	}

	/**
	 * To replace the oldAppointment with the newAppointment. Checks first if the
	 * newAppointment is valid and shows an error if not. If the oldAppointment is
	 * null, this operation is a create method.
	 *
	 * @param oldAppointment The old appointment to be replaced. May be null.p
	 * @param newAppointment The new appointment to be saved.
	 */
	public void updateAppointment(Appointment oldAppointment, Appointment newAppointment) {
		if (!isValid(oldAppointment, newAppointment)) {
			return;
		}
		Appointment searched = oldAppointment;
		Optional<Appointment> oldOpt = manTheSController.getOffice().getAppointments().stream()
				.filter(appointment -> appointment.equals(searched)).findFirst();

		if (oldOpt.isPresent()) {
			oldAppointment = oldOpt.get();
			oldAppointment.setCustomers(newAppointment.getCustomers());
			oldAppointment.addBillsForCustomerList();
			oldAppointment.setEmployee(newAppointment.getEmployee());
			oldAppointment.setRoom(newAppointment.getRoom());
			oldAppointment.setSuccessor(newAppointment.getSuccessor());
			oldAppointment.setTimeFrame(newAppointment.getTimeFrame());
			oldAppointment.setTreatmentType(newAppointment.getTreatmentType());
		} else {
			manTheSController.getOffice().getAppointments().add(newAppointment);
		}
		manTheSController.getMainWindowViewController().getCalendarViewController().filterAppointmentsToShow();
	}

	/**
	 * Checks if the newAppointment is valid. That means, no illogical empty
	 * Strings, null values, combination of data, or any collisions with other
	 * ongoing appointments (except oldAppointment), as well as no creation of
	 * appointments in the past or in a time where the employee doesn't work.
	 * 
	 * @param oldAppointment the old Appointment
	 * @param newAppointment the new Appointment
	 * @return true iff valid
	 */
	private boolean isValid(Appointment oldAppointment, Appointment newAppointment) {
		if (isInPast(oldAppointment)) {
			appointmentViewAUI.showValidationError("Vergangene Termine d�rfen nicht ge�ndert werden.");
			return false;
		} else {
			List<String> errorList = new ArrayList<String>();
			errorList.addAll(newAppointment.getValidationErrors());
			if (newAppointment.getEmployee() != null && !newAppointment.getEmployee().getSkills().contains(newAppointment.getTreatmentType())) {
				errorList.add("Der Mitarbeiter ist für die angegebene Behandlungsart nicht zugelassen");
			}
			if (isInPast(newAppointment)) {
				errorList.add("Der Termin darf nicht in der Vergangenheit erstellt werden.");
			}
			if (appointmentCollidesWithoutOld(oldAppointment, newAppointment)) {
				errorList.add("Dem Termin kollidiert mit einem anderen Termin.");
			}
			if (!Objects.isNull(newAppointment.getTimeFrame()) && !isWithinAllowedHours(newAppointment)) {
				errorList.add("Dem Termin findet außerhalb der Arbeitszeiten statt.");
			}
			if (errorList.size() > 0) {
				appointmentViewAUI.showValidationError(errorList.toString());
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Checks if the appointment ended in the past. If there is no timeFrame, it is
	 * considered as ending in the past.
	 * 
	 * @param appointment
	 * @return
	 */
	private boolean isInPast(Appointment appointment) {
		if (appointment == null || appointment.getTimeFrame() == null)
			return false;
		if (appointment.getTimeFrame().getEnd().isBefore(LocalDateTime.now()))
			return true;
		return false;
	}

	/**
	 * Checks whether the appointments timeFrame is within the opening hours of the
	 * office and the working hours (respecting vacations) of the employee.
	 * 
	 * @param appointment
	 * @return true iff the appointment is within the allowed hours
	 */
	private boolean isWithinAllowedHours(Appointment appointment) {
		TimeFrame appointmentTime = appointment.getTimeFrame();
		
		if (!appointmentTime.isInOneDay()) {
			return false;
		}

		// Check if the employee is on vacation or not
		Set<TimeFrame> vacations = new HashSet<TimeFrame>();
		vacations.addAll(appointment.getEmployee().getPaidVacations());
		vacations.addAll(appointment.getEmployee().getVacations());

		boolean employeeHasVacation = vacations.stream().anyMatch(vacation -> appointmentTime.collidesWith(vacation));

		if (employeeHasVacation) {
			return false;
		}

		// Check if the appointments time frame is within working hours of office and
		// employee
		int dayOfWeek = appointmentTime.getStart().getDayOfWeek().getValue() - 1;
		TimeFrame openingHours = manTheSController.getOffice().getOpeningTimes()[dayOfWeek];
		TimeFrame workingHours = appointment.getEmployee().getWorkingHours()[dayOfWeek];
		if(openingHours == null){
			return false;
		}
		if(workingHours == null){
			return false;
		}

		return appointmentTime.isWithinIgnoringDate(openingHours) && appointmentTime.isWithinIgnoringDate(workingHours);
	}

	/**
	 * Checks if the newAppointment collides with any other appointment except the
	 * oldAppointment. Two appointments collide iff their TimeFrames collide and
	 * they share the same room, employee or customer.
	 * 
	 * @param oldAppointment the old Appointment
	 * @param newAppointment the new Appointment
	 * @return true iff newAppointment collides with any in the system except
	 *         oldAppointment.
	 */
	private boolean appointmentCollidesWithoutOld(Appointment oldAppointment, Appointment newAppointment) {
		if (newAppointment.getTimeFrame() == null) {
			return false;
		}

		List<Appointment> appointmentSet = manTheSController.getOffice().getAppointmentsForDates(
				newAppointment.getTimeFrame().getStart(), newAppointment.getTimeFrame().getEnd());
		appointmentSet.remove(oldAppointment);
		return appointmentSet.stream().anyMatch(appointment -> appointment.collides(newAppointment));
	}

	/**
	 * Deletes the given appointment.
	 * 
	 * @param appointment
	 */
	public void deleteAppointment(Appointment appointment) {
		if (isInPast(appointment)) {
			appointmentViewAUI.showValidationError("Vergangene Termine dürfen nicht ge�ndert werden.");
		} else {
			this.manTheSController.getOffice().getAppointments().remove(appointment);
		}
	}

	/**
	 * sets manTheSController
	 * 
	 * @return the ManTheSController
	 */
	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	/**
	 * @param calendarViewAUI the calendarViewAUI to set
	 */
	public void setCalendarViewAUI(CalendarViewAUI calendarViewAUI) {
		this.calendarViewAUI = calendarViewAUI;
	}

	/**
	 * @param appointmentViewAUI the appointmentViewAUI to set
	 */
	public void setAppointmentViewAUI(AppointmentViewAUI appointmentViewAUI) {
		this.appointmentViewAUI = appointmentViewAUI;
	}

}

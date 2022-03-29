package view.viewAUI;

import model.Appointment;
import view.viewController.AppointmentViewController;

/**
 * AUI for {@link AppointmentViewController}
 * 
 * @author sopr057
 *
 */
public interface AppointmentViewAUI {

	/**
	 * To close the appointmentView window.
	 */
	public abstract void closeWindow();

	/**
	 * To show a validation error.
	 * @param error The error to display.
	 */
	public abstract void showValidationError(String error);

	/**
	 * To give and show an appointment to edit.
	 * @param appointment
	 */
	public abstract void refreshAppointmentView(Appointment appointment);
}

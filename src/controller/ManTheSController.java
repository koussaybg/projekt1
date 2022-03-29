package controller;

import java.time.LocalDateTime;

import model.Office;
import model.TimeFrame;
import view.viewAUI.MainWindowAUI;
import view.viewController.MainWindowViewController;

/**
 * Mediator class for all controllers and controller layer to model layer interface.
 * @author sopr057
 *
 */
public class ManTheSController {

	private Office office;

	private EmployeeSettingsController employeeSettingsController;

	private OfficeSettingsController officeSettingsController;

	private TreatmentSettingsController treatmentSettingsController;

	private CostumerController costumerController;

	private CalendarController calendarController;

	private IOController iOController;

	private LoginController loginController;

	private RoomSettingsController roomSettingsController;

	private MainWindowAUI mainWindowAUI;

	private MainWindowViewController mainWindowViewController;

	public ManTheSController(MainWindowViewController mainWindowViewController) {
		office = new Office();
		this.mainWindowViewController = mainWindowViewController;
		this.mainWindowAUI = mainWindowViewController;

		employeeSettingsController = new EmployeeSettingsController(mainWindowViewController.getSettingsViewController().getEmployeeSettingsViewController());
		employeeSettingsController.setManTheSController(this);
		
		officeSettingsController = new OfficeSettingsController(mainWindowViewController.getSettingsViewController().getOfficeSettingsViewController());
		officeSettingsController.setManTheSController(this);
		
		treatmentSettingsController = new TreatmentSettingsController(mainWindowViewController.getSettingsViewController().getTreatmentSettingsViewController());
		treatmentSettingsController.setManTheSController(this);

		roomSettingsController = new RoomSettingsController(mainWindowViewController.getSettingsViewController().getRoomSettingsViewController());
		roomSettingsController.setManTheSController(this);

		costumerController = new CostumerController(mainWindowViewController.getCustomerViewController());
		costumerController.setManTheSController(this);
		
		//TODO: Konstruktor mit Initialisierung anpassen
		calendarController = new CalendarController(mainWindowViewController.getCalendarViewController()); //, mainWindowViewController.getCalendarViewController().getAppointmentViewController());
		calendarController.setManTheSController(this);

		calendarController = new CalendarController();
		calendarController.setManTheSController(this);
		
		iOController = new IOController(this);
		iOController.setManTheSController(this);
		
		loginController = new LoginController(mainWindowViewController.getLoginViewController());
		loginController.setManTheSController(this);
	}

	/**
	 * only for testing!
	 */
	public ManTheSController() {
		mainWindowViewController = new MainWindowViewController();
		//new ManTheSController(mainWindowViewController);
	}

	/**
	 * @return the office
	 */
	public Office getOffice() {
		return office;
	}

	/**
	 * @param office the office to set
	 */
	public void setOffice(Office office) {
		this.office = office;

			TimeFrame[] openingTimes = new TimeFrame[7];

			openingTimes[0] = new TimeFrame(LocalDateTime.parse("2019-01-01T01:00:00"),
					LocalDateTime.parse("2019-01-01T08:00:00"));
			openingTimes[1] = new TimeFrame(LocalDateTime.parse("2019-02-02T02:00:00"),
					LocalDateTime.parse("2019-02-02T09:00:00"));
			openingTimes[2] = new TimeFrame(LocalDateTime.parse("2019-03-03T03:00:00"),
					LocalDateTime.parse("2019-03-03T10:00:00"));
			openingTimes[3] = new TimeFrame(LocalDateTime.parse("2019-04-04T04:00:00"),
					LocalDateTime.parse("2019-04-04T11:00:00"));
			openingTimes[4] = new TimeFrame(LocalDateTime.parse("2019-05-05T05:00:00"),
					LocalDateTime.parse("2019-05-05T12:00:00"));
			openingTimes[5] = new TimeFrame(LocalDateTime.parse("2019-06-06T06:00:00"),
					LocalDateTime.parse("2019-06-06T13:00:00"));
			openingTimes[6] = new TimeFrame(LocalDateTime.parse("2019-07-07T07:00:00"),
					LocalDateTime.parse("2019-07-07T14:00:00"));
	}

	/**
	 * @return the employeeSettingsController
	 */
	public EmployeeSettingsController getEmployeeSettingsController() {
		return employeeSettingsController;
	}

	/**
	 * @param employeeSettingsController the employeeSettingsController to set
	 */
	public void setEmployeeSettingsController(EmployeeSettingsController employeeSettingsController) {
		this.employeeSettingsController = employeeSettingsController;
	}

	/**
	 * @return the officeSettingsController
	 */
	public OfficeSettingsController getOfficeSettingsController() {
		return officeSettingsController;
	}

	/**
	 * @param officeSettingsController the officeSettingsController to set
	 */
	public void setOfficeSettingsController(OfficeSettingsController officeSettingsController) {
		this.officeSettingsController = officeSettingsController;
	}

	/**
	 * @return the treatmentSettingsController
	 */
	public TreatmentSettingsController getTreatmentSettingsController() {
		return treatmentSettingsController;
	}

	/**
	 * @param treatmentSettingsController the treatmentSettingsController to set
	 */
	public void setTreatmentSettingsController(TreatmentSettingsController treatmentSettingsController) {
		this.treatmentSettingsController = treatmentSettingsController;
	}

	/**
	 * @return the costumerController
	 */
	public CostumerController getCostumerController() {
		return costumerController;
	}

	/**
	 * @param costumerController the costumerController to set
	 */
	public void setCostumerController(CostumerController costumerController) {
		this.costumerController = costumerController;
	}

	/**
	 * @return the calendarController
	 */
	public CalendarController getCalendarController() {
		return calendarController;
	}

	/**
	 * @param calendarController the calendarController to set
	 */
	public void setCalendarController(CalendarController calendarController) {
		this.calendarController = calendarController;
	}

	/**
	 * @return the iOController
	 */
	public IOController getiOController() {
		return iOController;
	}

	/**
	 * @param iOController the iOController to set
	 */
	public void setiOController(IOController iOController) {
		this.iOController = iOController;
	}

	/**
	 * @return the loginController
	 */
	public LoginController getLoginController() {
		return loginController;
	}

	/**
	 * @param loginController the loginController to set
	 */
	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	/**
	 * @return the roomSettingsController
	 */
	public RoomSettingsController getRoomSettingsController() {
		return roomSettingsController;
	}

	/**
	 * @param roomSettingsController the roomSettingsController to set
	 */
	public void setRoomSettingsController(RoomSettingsController roomSettingsController) {
		this.roomSettingsController = roomSettingsController;
	}

	/**
	 * @return the mainWindowAUI
	 */
	public MainWindowAUI getMainWindowAUI() {
		return mainWindowAUI;
	}

	/**
	 * @param mainWindowAUI the mainWindowAUI to set
	 */
	public void setMainWindowAUI(MainWindowAUI mainWindowAUI) {
		this.mainWindowAUI = mainWindowAUI;
	}

	/**
	 * @return the mainWindowViewController
	 */
	public MainWindowViewController getMainWindowViewController() {
		return mainWindowViewController;
	}

	/**
	 * @param mainWindowViewController the mainWindowViewController to set
	 */
	public void setMainWindowViewController(MainWindowViewController mainWindowViewController) {
		this.mainWindowViewController = mainWindowViewController;
	}

}

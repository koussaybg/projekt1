package view.viewAUI;

public interface EmployeeSettingsViewAUI {

	/**
	 * Refresh employee list in view
	 */
	public abstract void refreshEmployeeList();

	/**
	 * Refresh skill list of currently displayed employee.
	 */
	public abstract void refreshSkillList();

	/**
	 * Refress vacation list of currently displayed employee
	 */
	public abstract void refreshVacationsList();
	
	/**
	 * To show a validation error.
	 * @param error The error to display.
	 */
	public abstract void showValidationError(String error);

}

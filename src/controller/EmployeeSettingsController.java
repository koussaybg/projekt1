package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Employee;
import model.Role;
import model.TimeFrame;
import model.TreatmentType;
import view.viewAUI.EmployeeSettingsViewAUI;

/**
 * Employee settings controller.
 * Changes the model and also call view callbacks to refresh after every operation.
 * @author severin
 *
 */
public class EmployeeSettingsController {

	/**
	 * Reference to manTheSController. Must be provided.
	 */
	private ManTheSController manTheSController;

	/**
	 * Callbacks for refreshing the view. Must be provided
	 */
	private EmployeeSettingsViewAUI employeeSettingsViewAUI;

	public EmployeeSettingsController(EmployeeSettingsViewAUI employeeSettingsViewAUI) {
		this.employeeSettingsViewAUI = employeeSettingsViewAUI;
	}

	/**
	 * Only for tests!
	 */

	public EmployeeSettingsController(){

	}

	/**
	 * Updates or creates a new Employee.
	 * Will also call the view to refresh.
	 * If parameter oldEmployee is null, a new employee will be created.
	 * @param oldEmployee old employee to replace. If null, a new employee will be created
	 * @param newEmployee employee to save
	 */
	public void updateEmployee(Employee oldEmployee, Employee newEmployee) {
		if (manTheSController.getOffice().getCurrentUser().getRole().equals(Role.EMPLOYEE) ||
				manTheSController.getOffice().getCurrentUser().getRole().equals(Role.DEPUTY)) {
			employeeSettingsViewAUI.showValidationError("Only the admin have the privilege to Update or add an Employee ! .");
			return;
		}
		if(!isValid(newEmployee)) {
			return;
		}
		
		Employee searched = oldEmployee;
		Optional<Employee> oldOpt = manTheSController.getOffice().getEmployees().stream().filter(employee -> employee.equals(searched)).findFirst();
		
		if(oldOpt.isPresent()) {
			oldEmployee = oldOpt.get();
			oldEmployee.setStreet(newEmployee.getStreet());
			oldEmployee.setHouseNumber(newEmployee.getHouseNumber());
			oldEmployee.setPostalCode(newEmployee.getPostalCode());
			oldEmployee.setCity(newEmployee.getCity());
			oldEmployee.setDayOfBirth(newEmployee.getDayOfBirth());
			oldEmployee.setFirstName(newEmployee.getFirstName());
			oldEmployee.setLastName(newEmployee.getLastName());
			oldEmployee.setMobile(newEmployee.getMobile());
			oldEmployee.setPassword(newEmployee.getPassword());
			oldEmployee.setRole(newEmployee.getRole());
			oldEmployee.setPhone(newEmployee.getPhone());
			oldEmployee.setMobile(newEmployee.getMobile());
			oldEmployee.setUsername(newEmployee.getUsername());
			oldEmployee.setWorkingHours(newEmployee.getWorkingHours());
		} else {
			manTheSController.getOffice().getEmployees().add(newEmployee);
		}
		employeeSettingsViewAUI.refreshEmployeeList();
	}

	
	/**
	 * TODO
	 * Validates the employee and shows possible errors to the mainWindowAUI
	 * @param newEmployee
	 * @return if the employee is valid
	 */
	private boolean isValid(Employee newEmployee) {
		List<String> errorList = new ArrayList<String>();

		errorList.addAll(newEmployee.getValidationErrors());

		if (errorList.size() > 0) {
			employeeSettingsViewAUI.showValidationError(errorList.toString());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Deletes the given employee.
	 * Will also call the view to refresh.
	 * @param employee employee to delete.
	 */
	public void deleteEmployee(Employee employee) {
		if (manTheSController.getOffice().getCurrentUser().getRole().equals(Role.EMPLOYEE) ||
		manTheSController.getOffice().getCurrentUser().getRole().equals(Role.DEPUTY)) {
			employeeSettingsViewAUI.showValidationError("Only the admin have the privilege to delete an Employee ! .");
			return;
		}
		if(employee.getRole().equals(Role.BOSS)) {
			employeeSettingsViewAUI.showValidationError("Admin kann nicht gelöscht werden.");
		}
		if(employee.equals(manTheSController.getOffice().getCurrentUser())) {
			employeeSettingsViewAUI.showValidationError("Sie können sich nicht selbst löschen.");
		}else{
			manTheSController.getOffice().getEmployees().remove(employee) ;
			employeeSettingsViewAUI.refreshEmployeeList();
		}
	}

	/**
	 * Adds vacations to the given employee.
	 * Will also call the view to refresh.
	 * @param employee employee to add vacation
	 * @param vacation time of vacation
	 * @param paid whether the vacation is paid or not.
	 */
	public void addVacation(Employee employee, TimeFrame vacation, boolean paid) {
        if (manTheSController.getOffice().getCurrentUser().getRole().equals(Role.EMPLOYEE)) {
        	employeeSettingsViewAUI.showValidationError("you have no privileges to update the Vacation List");
        	return ;
		}
		if (paid) {
			List<TimeFrame> employeeSet = employee.getPaidVacations();
			if (employeeSet.contains(vacation)) return;  // the Paid vacation exist
			employee.getPaidVacations().add(vacation);
		} else {
			List<TimeFrame> employeeSet = employee.getVacations();
			if (employeeSet.contains(vacation)) return;
			employee.getVacations().add(vacation); // The Unpaid vacation exist


		}
		employeeSettingsViewAUI.refreshVacationsList();
	}
	
	
	/**
	 * Adds a skill to an employee.
	 * Will also call the view to refresh.
	 * @param employee employee to add skill to.
 	 * @param skill skill to add to the employee.
	 */
	public void addSkill(Employee employee, TreatmentType skill) {
		if(skill == null){
			employeeSettingsViewAUI.showValidationError("Es muss eine Qualifikation ausgewählt werden!");
			return;
		}
		if (manTheSController.getOffice().getCurrentUser().getRole().equals(Role.EMPLOYEE) ||
				manTheSController.getOffice().getCurrentUser().getRole().equals(Role.DEPUTY)) {
			employeeSettingsViewAUI.showValidationError("Only the admin have the privilege to add skill ! .");
			return;
		}
		List<TreatmentType> treatmentTypes=employee.getSkills() ;
		if(treatmentTypes.contains(skill)) return;
		employee.getSkills().add(skill) ;
		employeeSettingsViewAUI.refreshSkillList();
	}

	/**
	 * sets manTheSController
	 * @return the ManTheSController
	 */
	public void setManTheSController(ManTheSController manTheSController) {
		this.manTheSController = manTheSController;
	}

	/**
	 * @param employeeSettingsViewAUI the employeeSettingsViewAUI to set
	 */
	public void setEmployeeSettingsViewAUI(EmployeeSettingsViewAUI employeeSettingsViewAUI) {
		this.employeeSettingsViewAUI = employeeSettingsViewAUI;
	}
}

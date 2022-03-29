package controller;

import view.viewAUI.LoginViewAUI;
import model.* ;
import view.viewController.LoginViewController;

import java.util.List;
import java.util.Set;

public class LoginController {

	/**
	 * reference used to communicate with the Controller Layer
	 */
	private ManTheSController manTheSController;
	/**
	 * reference to the LoginAUI to refresh the View
	 */
	private LoginViewAUI loginViewAUI;
	public LoginController(LoginViewAUI loginViewController) {
		this.loginViewAUI = loginViewController;
	}

	/**
	 * Check the LoginDATA
	 *
	 * @param username e.g the Username given
	 * @param password e.g the given Password
	 */

	public void requestLogin(String username, String password) {
		List<Employee> employeeSet = manTheSController.getOffice().getEmployees();
		for (Employee employee : employeeSet) {
			if (username.equals(employee.getUsername())) {
				if (password.equals(employee.getPassword())) {
					manTheSController.getOffice().setCurrentUser(employee);
					loginViewAUI.onLoginResult(true);
					return;
				}
			}
		}
		loginViewAUI.onLoginResult(false);
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

	
	
}

package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import model.Employee;
import model.Office;
import model.Role;
import view.viewAUI.LoginViewAUI;

public class LoginControllerTest {

	Office office;
	LoginController testController;
	Employee employee;

	LoginViewAuiImpl loginViewAui;

	@Before
	public void setUp() throws Exception {
		office = new Office();
		ManTheSController manTheSController = new ManTheSController();
		manTheSController.setOffice(office);
		loginViewAui = new LoginViewAuiImpl();
		testController = new LoginController(loginViewAui);
		testController.setManTheSController(manTheSController);
		employee = new Employee();
		employee.setRole(Role.BOSS);
		employee.setUsername("username");
		employee.setPassword("password");
		office.getEmployees().add(employee);
	}

	@Test
	public void requestLoginTestSuccess() {
		testController.requestLogin(employee.getUsername(), employee.getPassword());
		assertTrue(loginViewAui.result);
		assertTrue(office.getCurrentUser().equals(employee));
	}
	
	@Test
	public void requestLoginTestWrongPassword() {
		testController.requestLogin(employee.getUsername(), "some bs");
		assertFalse(loginViewAui.result);
		assertTrue(Objects.isNull(office.getCurrentUser()));
	}
	
	@Test
	public void requestLoginTestWrongUsername() {
		testController.requestLogin("some bs", employee.getPassword());
		assertFalse(loginViewAui.result);
		assertTrue(Objects.isNull(office.getCurrentUser()));
	}

	class LoginViewAuiImpl implements LoginViewAUI {
		public boolean result = false;

		@Override
		public void onLoginResult(boolean result) {
			this.result = result;
		}
	}
}

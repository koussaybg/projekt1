package controller;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import model.Employee;
import model.Office;
import model.Role;
import model.TimeFrame;
import model.TreatmentType;
import view.viewAUI.EmployeeSettingsViewAUI;

public class EmployeeSettingsControllerTest {

	private Office office;
	private EmployeeSettingsController esc;

	@Before
	public void setUp() throws Exception {
		office = new Office();
		Employee boss = new Employee();
		boss.setRole(Role.BOSS);
		office.setCurrentUser(boss);

		ManTheSController manTheSController = new ManTheSController();
		manTheSController.setOffice(office);

		esc = new EmployeeSettingsController();
		esc.setManTheSController(manTheSController);
		esc.setEmployeeSettingsViewAUI(new EmployeeSettingsViewAUI() {
			@Override
			public void refreshVacationsList() {
			}

			@Override
			public void refreshSkillList() {
			}

			@Override
			public void refreshEmployeeList() {
			}
			
			@Override
			public void showValidationError(String error) {
			}
		});
	}

	@Test
	public void testUpdateEmployeeAddEmployee() {
		Employee testEmployee = generateEmployee();
		esc.updateEmployee(null, testEmployee);
		boolean added = office.getEmployees().contains(testEmployee);
		assertTrue(added);
	}

	@Test
	public void testUpdateEmployeeUpdateEmployee() {		
		Employee oldTestEmployee = new Employee();
		oldTestEmployee.setUsername("TEST");
		Employee newTestEmployee = generateEmployee();
		office.getEmployees().add(oldTestEmployee);
		esc.updateEmployee(oldTestEmployee, newTestEmployee);
		boolean contains = office.getEmployees().contains(oldTestEmployee);
		boolean updated = oldTestEmployee.equals(newTestEmployee);
		assertTrue((contains && updated));
	}

	@Test
	public void testAddVacationUnpaid() {
		Employee employee = new Employee();
		employee.setUsername("TEST");
		office.getEmployees().add(employee);

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		TimeFrame vacation = new TimeFrame(start, end);

		esc.addVacation(employee, vacation, false);

		boolean containsUnpaid = employee.getVacations().contains(vacation);
		boolean containsPaid = employee.getPaidVacations().contains(vacation);

		assertTrue((containsUnpaid && !containsPaid));
	}

	@Test
	public void testAddVacationPaid() {
		Employee employee = new Employee();
		employee.setUsername("TEST");
		office.getEmployees().add(employee);

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		TimeFrame vacation = new TimeFrame(start, end);

		esc.addVacation(employee, vacation, true);

		boolean containsUnpaid = employee.getVacations().contains(vacation);
		boolean containsPaid = employee.getPaidVacations().contains(vacation);

		assertTrue((!containsUnpaid && containsPaid));
	}

	@Test
	public void addSkillTest() {
		Employee employee = new Employee();
		employee.setUsername("TEST");
		office.getEmployees().add(employee);

		TreatmentType testSkill = new TreatmentType();
		testSkill.setName("Test treatment");

		esc.addSkill(employee, testSkill);

		boolean containsSkill = employee.getSkills().contains(testSkill);

		assertTrue(containsSkill);
	}

	@Test
	public void testDeleteEmployee() {
		Employee employeeToDelete = new Employee();
		employeeToDelete.setRole(Role.EMPLOYEE);
		employeeToDelete.setUsername("TEST");
		office.getEmployees().add(employeeToDelete);
		esc.deleteEmployee(employeeToDelete);
		boolean deleted = !office.getEmployees().contains(employeeToDelete);
		assertTrue(deleted);
	}
	
	private Employee generateEmployee() {
		Employee empl = new Employee();
		empl.setCity("Stadt");
		empl.setDayOfBirth(LocalDate.now().minusYears(1));
		empl.setFirstName("sdvsA");
		empl.setLastName("sdvsA");
		empl.setStreet("sgafs");
		empl.setHouseNumber("3");
		empl.setUsername("sdvvd");
		empl.setPassword("asfdbsda");
		empl.setPhone("12312");
		empl.setMobile("12312");
		empl.setPostalCode("asfdvafs");
		empl.setRole(Role.EMPLOYEE);
		TimeFrame allDayEveryDay = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
				LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)));
		TimeFrame noWork = new TimeFrame(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
				LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
		TimeFrame[] workingHours = { allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, allDayEveryDay, noWork, noWork };
		empl.setWorkingHours(workingHours);
		return empl;
	}
}
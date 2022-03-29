package model;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;

public class EmployeeTests {

	@Test
	public void test() {
		Employee employee = new Employee();
		employee.setUsername("TEST");

		TreatmentType testSkill = new TreatmentType();
		testSkill.setName("Test treatment");

		employee.addSkill(testSkill);

		boolean containsSkill = employee.getSkills().contains(testSkill);

		assertTrue(containsSkill);
	}

	@Test
	public void testAddVacationUnpaid() {
		Employee employee = new Employee();
		employee.setUsername("TEST");

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		TimeFrame vacation = new TimeFrame(start, end);

		employee.addVacation(vacation, false);

		boolean containsUnpaid = employee.getVacations().contains(vacation);
		boolean containsPaid = employee.getPaidVacations().contains(vacation);
		
		assertTrue((containsUnpaid && !containsPaid));
	}

	@Test
	public void testAddVacationPaid() {
		Employee employee = new Employee();
		employee.setUsername("TEST");

		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		TimeFrame vacation = new TimeFrame(start, end);

		employee.addVacation(vacation, true);

		boolean containsUnpaid = employee.getVacations().contains(vacation);
		boolean containsPaid = employee.getPaidVacations().contains(vacation);

		assertTrue((!containsUnpaid && containsPaid));
	}
	
	@Test
	public void addSkillTest(){
		TreatmentType skill1= new TreatmentType();
		Employee employee = new Employee();
		employee.addSkill(skill1);
		
		boolean containsSKill = employee.getSkills().contains(skill1);
		assertTrue(containsSKill);
	}

	@Test
	public void testEmployee() {
		Employee testEmployee = new Employee();
		assertNotNull(testEmployee.getPaidVacations());
		assertNotNull(testEmployee.getSkills());
		assertNotNull(testEmployee.getVacations());
	}
	
	@Test
	public void testEquals() {
		Employee employee1 = new Employee();
		employee1.setFirstName("fn");
		employee1.setLastName("ln");
		employee1.setPhone("phone");
		employee1.setMobile("mobile");
		employee1.setUsername("u1");
		employee1.setPassword("pw");
		
		Employee employee2 = new Employee();
		employee2.setFirstName("fn");
		employee2.setLastName("ln");
		employee2.setPhone("phone");
		employee2.setMobile("mobile");
		employee2.setUsername("u1");
		employee2.setPassword("pw");
		
		
		assertTrue(employee1.equals(employee2));
		employee2.setFirstName("fn2");
		assertFalse(employee1.equals(employee2));
		employee2.setFirstName("fn");
		
		employee2.setUsername("u2");
		assertFalse(employee1.equals(employee2));
	}
	
	// @Test
	// public void testToString(){
	// 	Employee employee = new Employee();
	// 	String employeetoString = employee.toString();
	// 	String employeeTest = "Employee{" +
    //             "firstName='" + employee.getFirstName() + '\'' +
    //             ", lastName='" + employee.getLastName() + '\'' +
    //             ", dayOfBirth=" + employee.getDayOfBirth() +
    //             ", street='" + employee.getStreet() + '\'' +
    //             ", housenumber='" + employee.getHouseNumber() + '\'' +
    //             ", postalcode='" + employee.getPostalCode() + '\'' +
    //             ", city='" + employee.getCity() + '\'' +
    //             ", phone='" + employee.getPhone() + '\'' +
    //             ", mobile='" + employee.getMobile() + '\'' +
    //             "username='" + employee.getUsername() + '\'' +
    //             ", password='" + employee.getPassword() + '\'' +
    //             ", skills=" + employee.getSkills() +
    //             ", vacations=" + employee.getVacations() +
    //             ", paidVacations=" + employee.getPaidVacations() +
    //             ", workingHours=" + Arrays.toString(employee.getWorkingHours()) +
    //             ", role=" + employee.getRole() +
    //             '}';
		
	// 	assertEquals(employeetoString, employeeTest);
		
	// }

	@Test
	public void testToString(){
		Employee employee = new Employee();
		employee.setFirstName("test1");
		employee.setLastName("test2");
		String employeetoString = employee.toString();
		String employeeTest = employee.getFirstName() +
								" " + 
								employee.getLastName();
		assertEquals(employeetoString, employeeTest);
		
	}
	
	

}

package view.viewController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.*;
import view.viewAUI.EmployeeSettingsViewAUI;
import view.viewController.customControls.PersonInputControl;
import view.viewController.customControls.WeekdayTimeControl;

/**
 * ViewController for actions in employee view
 *
 * @author severin
 */
public class EmployeeSettingsViewController implements EmployeeSettingsViewAUI {

    public TableView<Employee> employeeTable;
    public TableColumn<Employee, String> lastNameColumn;
    public TableColumn<Employee, String> firstNameColumn;


    public ComboBox<Role> roleComboBox;
    public TextField userNameTextField;
    public PasswordField passwordTextField;
    public ComboBox<String> vacationTypeComboBox;
    public DatePicker startVacationDatePicker;
    public DatePicker endVacationDatePicker;

    public TableView<TimeFrame> vacationPaidTable;
    public TableColumn<TimeFrame, LocalDateTime> vacationPaidStartColumn;
    public TableColumn<TimeFrame, LocalDateTime> vacationPaidEndColumn;
    public TableView<TimeFrame> vacationUnpaidTable;
    public TableColumn<TimeFrame, LocalDateTime> vacationUnpaidStartColumn;
    public TableColumn<TimeFrame, LocalDateTime> vacationUnpaidEndColumn;

    public ComboBox<TreatmentType> qualificationComboBox;
    public ListView<TreatmentType> qualificationListView;
    public WeekdayTimeControl workingsHoursControl;
    public TextField searchTextField;
    public Button addEmployeeButton;
    public Button deleteEmployeeButton;
    public Button saveEmployeeButton;
    public Button qualificationAddButton;
    public PersonInputControl personInputControl;
    public Button addVacationButton;

    private Employee currentEmployee;


    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.values());
        roleComboBox.setValue(Role.EMPLOYEE);

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onEmployeeSelected(newSelection);
            }
        });
        vacationPaidStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        vacationPaidEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        vacationPaidStartColumn.setCellFactory(column -> createLocalDateCell());
        vacationPaidEndColumn.setCellFactory(column -> createLocalDateCell());

        vacationUnpaidStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        vacationUnpaidEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        vacationUnpaidStartColumn.setCellFactory(column -> createLocalDateCell());
        vacationUnpaidEndColumn.setCellFactory(column -> createLocalDateCell());

        vacationTypeComboBox.setItems(FXCollections.observableArrayList("Bezahlt", "Unbezahlt"));

        qualificationListView.setCellFactory(column -> createQualificationCell());
        qualificationComboBox.setCellFactory(column -> createQualificationCell());
        qualificationComboBox.setButtonCell(createQualificationCell());

        vacationPaidTable.setPlaceholder(new Label("Es ist kein bezahlter Urlaub eingetragen."));
        vacationUnpaidTable.setPlaceholder(new Label("Es ist kein unbezahlter Urlaub eingetragen."));
    }


    /**
     * settingsViewController must be provided for affecting changes.
     */
    private SettingsViewController settingsViewController;

    /**
     * Called on actions in SearchBox e.g. if a name is entered.
     * Triggers filtering of employee list.
     *
     * @param action ActionEvent passed by view.
     */
    public void onSearchBoxAction(KeyEvent action) {
        List<Employee> employees = settingsViewController.getMainWindowViewController()
                .getManTheSController()
                .getOffice().getEmployees();
        String filterString = searchTextField.getText();
        if (filterString != null) {
            List<Employee> filteredEmployees = employees.stream()
                    .filter(e -> e.getFirstName().contains(filterString) || e.getLastName().contains(filterString)).collect(Collectors.toList());
            employeeTable.setItems(FXCollections.observableArrayList(filteredEmployees));
        }
    }

    /**
     * Called if save button is clicked.
     * Triggers saving of current employee.
     *
     * @param action ActionEvent passed by view.
     */
    public void onSaveButtonClicked(ActionEvent action) {
        Employee newEmployee = new Employee();
        Person person = personInputControl.getValue();
        newEmployee.setFirstName(person.getFirstName());
        newEmployee.setLastName(person.getLastName());
        newEmployee.setStreet(person.getStreet());
        newEmployee.setHouseNumber(person.getHouseNumber());
        newEmployee.setPostalCode(person.getPostalCode());
        newEmployee.setCity(person.getCity());
        newEmployee.setDayOfBirth(person.getDayOfBirth());
        newEmployee.setMobile(person.getMobile());
        newEmployee.setPhone(person.getPhone());
        newEmployee.setRole(roleComboBox.getValue());
        newEmployee.setUsername(userNameTextField.getText());
        newEmployee.setPassword(passwordTextField.getText());
        try {
            TimeFrame[] workingHours = workingsHoursControl.getValue();
            newEmployee.setWorkingHours(workingHours);
        } catch (DateTimeParseException e) {
            showValidationError("Die Zeitangaben müssen mit dem Format HH:mm formatiert werden.");
            return;
        }catch (IllegalArgumentException e) {
            showValidationError("Der Startzeitpunkt darf nicht nach dem Endzeitpunkt sein.");
            return;
        }
        settingsViewController.getMainWindowViewController().getManTheSController()
                .getEmployeeSettingsController()
                .updateEmployee(currentEmployee, newEmployee);
    }


    /**
     * Called if cancel button is clicked.
     * Discards changes
     *
     * @param action ActionEvent passed by view.
     */
    public void onCancelButtonClicked(ActionEvent action) {
        resetView();
    }

    private void onEmployeeSelected(Employee selectedEmployee) {
        this.currentEmployee = selectedEmployee;
        clearAllFields();
        refreshVacationsList();
        refreshSkillList();
        userNameTextField.setText(selectedEmployee.getUsername());
        roleComboBox.setValue(selectedEmployee.getRole());
        passwordTextField.setText(selectedEmployee.getPassword());
        workingsHoursControl.setValue(selectedEmployee.getWorkingHours());
        personInputControl.setValue(selectedEmployee);
        Role currentUserRole = settingsViewController.getMainWindowViewController().getManTheSController()
                .getOffice().getCurrentUser().getRole();
        boolean isDeputy = currentUserRole == Role.DEPUTY;
        setFormFieldsDisable(isDeputy);
        setVacationFieldsDisable(false);
    }

    /**
     * Called if add skill button is clicked.
     * Reads selected skill from combobox.
     * Triggers employee change in controller.
     *
     * @param action ActionEvent passed by view.
     */
    public void onAddSkillButtonClicked(ActionEvent action) {
        if (this.currentEmployee != null) {
            TreatmentType qualification = this.qualificationComboBox.getValue();
            settingsViewController.getMainWindowViewController()
                    .getManTheSController()
                    .getEmployeeSettingsController().addSkill(currentEmployee, qualification);
        }
    }

    /**
     * Called if add vacation button is clicked.
     * Retrieves time period from both calendar view
     *
     * @param action ActionEvent passed by view.
     */
    public void onAddVacationButtonClicked(ActionEvent action) {
        if (currentEmployee != null) {
            LocalDateTime vacationStart = LocalDateTime.from(startVacationDatePicker.getValue().atTime(LocalTime.MIDNIGHT));
            LocalDateTime vacationEnd = LocalDateTime.from(endVacationDatePicker.getValue().atTime(LocalTime.MIDNIGHT));
            boolean vacationPaid = vacationTypeComboBox.getValue().equals("Bezahlt");
            TimeFrame vacationTimeFrame = new TimeFrame(vacationStart, vacationEnd);
            this.settingsViewController.getMainWindowViewController()
                    .getManTheSController()
                    .getEmployeeSettingsController()
                    .addVacation(this.currentEmployee, vacationTimeFrame, vacationPaid);
            clearVacationAddFields();
        }
    }


    /**
     * Called if delete employee button is clicked.
     * Triggers removing of selected employee.
     *
     * @param action ActionEvent passed by view.
     */
    public void onDeleteEmployeeButtonClicked(ActionEvent action) {
        if (currentEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setWidth(480);
            alert.setTitle("Bestätigung");
            alert.setHeaderText("Löschen");
            alert.setContentText("Soll der Mitarbeiter " + currentEmployee.getFirstName() + " "
                    + currentEmployee.getLastName() + " wirklich gelöscht werden?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                settingsViewController.getMainWindowViewController()
                        .getManTheSController()
                        .getEmployeeSettingsController().deleteEmployee(this.currentEmployee);
            }
        }
    }

    /**
     * Called if add employee button is clicked.
     * Creates a new empty form for entering data of a new employee.
     *
     * @param action ActionEvent passed by view.
     */
    public void onAddEmployeeButtonClicked(ActionEvent action) {
        this.currentEmployee = null;
        resetView();
        setFormFieldsDisable(false);
    }

    private void resetView() {
        currentEmployee = null;
        employeeTable.getSelectionModel().select(null);
        clearAllFields();
        refreshSkillList();
        refreshVacationsList();
        setFormFieldsDisable(true);
        setVacationFieldsDisable(true);
    }

    private void setFormFieldsDisable(boolean disable) {
        personInputControl.setDisable(disable);
        userNameTextField.setDisable(disable);
        roleComboBox.setDisable(disable);
        passwordTextField.setDisable(disable);
        workingsHoursControl.setFieldsDisabled(disable);
        saveEmployeeButton.setDisable(disable);
        qualificationComboBox.setDisable(disable);
        qualificationAddButton.setDisable(disable);
    }

    private void setVacationFieldsDisable(boolean disable){
        this.vacationTypeComboBox.setDisable(disable);
        this.startVacationDatePicker.setDisable(disable);
        this.endVacationDatePicker.setDisable(disable);
        this.addVacationButton.setDisable(disable);
    }

    private void clearAllFields() {
        this.personInputControl.clearFields();
        userNameTextField.setText(null);
        roleComboBox.setValue(null);
        passwordTextField.setText(null);
        workingsHoursControl.setValue(null);
        qualificationComboBox.getSelectionModel().select(null);
        clearVacationAddFields();
    }

    private void clearVacationAddFields() {
        startVacationDatePicker.setValue(null);
        endVacationDatePicker.setValue(null);
        vacationTypeComboBox.setValue(null);
    }


    /**
     * Setter view settingsViewController
     *
     * @param settingsViewController settingsViewcontroller to set
     */
    void setSettingsViewController(SettingsViewController settingsViewController) {
        this.settingsViewController = settingsViewController;
    }

    public void refresh() {
        qualificationComboBox.getItems().clear();
        qualificationComboBox.getItems().addAll(settingsViewController.getMainWindowViewController()
                .getManTheSController().getOffice().getTreatmentTypes());
        Role currentUserRole = settingsViewController.getMainWindowViewController().getManTheSController()
                .getOffice().getCurrentUser().getRole();
        boolean isDeputy = currentUserRole == Role.DEPUTY;
        deleteEmployeeButton.setDisable(isDeputy);
        addEmployeeButton.setDisable(isDeputy);
        refreshEmployeeList();
        resetView();
    }


    /**
     * @see view.viewAUI.EmployeeSettingsViewAUI#refreshEmployeeList()
     */
    public void refreshEmployeeList() {
        List<Employee> employees = settingsViewController.getMainWindowViewController()
                .getManTheSController()
                .getOffice().getEmployees();
        employeeTable.getItems().setAll(employees);
        employeeTable.getSelectionModel().select(null);
        resetView();
    }

    /**
     * @see view.viewAUI.EmployeeSettingsViewAUI#refreshSkillList()
     */
    public void refreshSkillList() {
        qualificationListView.getItems().clear();
        if (currentEmployee != null) {
            qualificationListView.getItems().addAll(currentEmployee.getSkills());
        }
    }

    /**
     * @see view.viewAUI.EmployeeSettingsViewAUI#refreshVacationsList()
     */
    public void refreshVacationsList() {
        vacationPaidTable.getItems().clear();
        vacationUnpaidTable.getItems().clear();
        if (this.currentEmployee != null) {
            vacationPaidTable.getItems().addAll(currentEmployee.getPaidVacations());
            vacationUnpaidTable.getItems().addAll(currentEmployee.getVacations());
        }

    }

	public void showValidationError(String error) {
		Alert a = new Alert(AlertType.WARNING);
		a.setContentText(error);
		a.show();
	}

    private TableCell<TimeFrame, LocalDateTime> createLocalDateCell() {
        return new TableCell<TimeFrame, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        };
    }

    private ListCell<TreatmentType> createQualificationCell() {
        return new ListCell<TreatmentType>() {
            @Override
            protected void updateItem(TreatmentType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        };
    }
}

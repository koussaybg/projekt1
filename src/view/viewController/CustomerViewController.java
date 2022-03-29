package view.viewController;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import controller.CostumerController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import model.*;
import view.viewAUI.CostumerViewAUI;
import view.viewController.customControls.PersonInputControl;

public class CustomerViewController implements CostumerViewAUI {

    public TableView<Customer> customerTable;
    public TableColumn<Customer, String> lastNameColumn;
    public TableColumn<Customer, String> firstNameColumn;
    public TextArea notesTextArea;
    public CheckBox publicInsuranceCheckbox;
    public ListView<Appointment> appointmentListView;
    public ComboBox<Employee> favouriteEmployeeComboBox;
    public TextField nameOfArticleTextField;
    public TextField priceOfArticleTextField;
    public TextField searchCostumerTextField;
    public Button addArticleButton;
    public Button createBillButton;
    public PersonInputControl personInputControl;

    private MainWindowViewController mainWindowViewController;
    private Customer currentCustomer;

    public void initialize() {
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onCustomerSelected(newSelection);
            }
        });
        customerTable.setPlaceholder(new Label("Es existieren noch keine Kunden"));
        favouriteEmployeeComboBox.setCellFactory(column -> createEmployeeCell());
        favouriteEmployeeComboBox.setButtonCell(createEmployeeCell());
        appointmentListView.setCellFactory(column -> createAppointmentCell());
        appointmentListView.setPlaceholder(new Label("Es existieren noch keine alten Termine"));
    }

    private ListCell<Appointment> createAppointmentCell() {
        return new ListCell<Appointment>() {
            @Override
            protected void updateItem(Appointment appointment, boolean empty) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.YY");
                super.updateItem(appointment, empty);
                if (empty) {
                    setText("");
                } else {
                    String dateString = dateTimeFormatter.format(appointment.getTimeFrame().getStart());
                    setText(dateString + " " + appointment.getTreatmentType().getName());
                }
            }
        };
    }

    private ListCell<Employee> createEmployeeCell() {
        return new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(employee.getFirstName() + " " + employee.getLastName());
                }
            }
        };
    }


    private void onCustomerSelected(Customer selectedCustomer) {
        this.currentCustomer = selectedCustomer;
        clearFields();
        this.setFormFieldsDisable(false);
        personInputControl.setValue(selectedCustomer);
        notesTextArea.setText(selectedCustomer.getNotes());
        publicInsuranceCheckbox.setSelected(selectedCustomer.isPublicInsurance());
        favouriteEmployeeComboBox.setValue(selectedCustomer.getFavoriteEmployee());
        List<Appointment> oldAppointments = mainWindowViewController.getManTheSController().getOffice()
                .getAppointments()
                .stream().filter(a ->
                        a.getCustomers().contains(currentCustomer) && a.getTimeFrame().getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        this.appointmentListView.getItems().setAll(oldAppointments);
    }


    public void onNewCustomerButtonClicked(ActionEvent action) {
        this.currentCustomer = null;
        this.customerTable.getSelectionModel().select(null);
        setFormFieldsDisable(false);
        clearFields();
    }


    public void onCreateBillButtonClicked(ActionEvent action) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Rechnung speichern unter");
        fileChooser.setInitialFileName("rechnung.html");
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            mainWindowViewController.getManTheSController().getCostumerController().createBill(this.currentCustomer, selectedFile.getAbsolutePath());
        }
    }

    public void onCancelButtonClicked(ActionEvent action) {
        this.customerTable.getSelectionModel().select(null);
        clearFields();
        setFormFieldsDisable(true);
    }

    public void onSaveButtonClicked(ActionEvent action) {
        Customer newCustomer = new Customer();
        Person person = personInputControl.getValue();
        newCustomer.setFirstName(person.getFirstName());
        newCustomer.setLastName(person.getLastName());
        newCustomer.setStreet(person.getStreet());
        newCustomer.setHouseNumber(person.getHouseNumber());
        newCustomer.setPostalCode(person.getPostalCode());
        newCustomer.setCity(person.getCity());
        newCustomer.setDayOfBirth(person.getDayOfBirth());
        newCustomer.setMobile(person.getMobile());
        newCustomer.setPhone(person.getPhone());
        newCustomer.setPublicInsurance(publicInsuranceCheckbox.isSelected());
        newCustomer.setNotes(notesTextArea.getText());
        newCustomer.setFavoriteEmployee(favouriteEmployeeComboBox.getValue());
        mainWindowViewController.getManTheSController().getCostumerController().updateCustomer(currentCustomer, newCustomer);
    }

    public void onSearchBoxAction(KeyEvent action) {
        List<Customer> customers = mainWindowViewController.getManTheSController()
                .getOffice().getCustomers();
        String filterString = searchCostumerTextField.getText();
        if (filterString != null) {
            List<Customer> filteredCustomers = customers.stream()
                    .filter(c -> c.getFirstName().contains(filterString) || c.getLastName().contains(filterString)).collect(Collectors.toList());
            customerTable.setItems(FXCollections.observableArrayList(filteredCustomers));
        }
    }

    public void onSellItemButtonClicked(ActionEvent action) {
        int price;
        try {
            price = Integer.parseInt(priceOfArticleTextField.getText());
        } catch (NumberFormatException e) {
            mainWindowViewController.showValidationErrors(Arrays.asList("Preis muss eine Zahl sein"));
            return;
        }
        CostumerController customerController = mainWindowViewController.getManTheSController().getCostumerController();
        Bill itemBill = new Bill();
        itemBill.setItem(nameOfArticleTextField.getText());
        itemBill.setPrice(price);
        customerController.sellItem(this.currentCustomer, itemBill);
        priceOfArticleTextField.setText("");
        nameOfArticleTextField.setText("");
    }

    private void clearFields() {
        personInputControl.clearFields();
        notesTextArea.setText("");
        publicInsuranceCheckbox.setSelected(false);
        favouriteEmployeeComboBox.setValue(null);
        nameOfArticleTextField.setText("");
        priceOfArticleTextField.setText("");
        searchCostumerTextField.setText("");
        priceOfArticleTextField.setText("");
        nameOfArticleTextField.setText("");
        appointmentListView.getItems().clear();
    }

    private void setFormFieldsDisable(boolean disable) {
        personInputControl.setDisable(disable);
        notesTextArea.setDisable(disable);
        publicInsuranceCheckbox.setDisable(disable);
        favouriteEmployeeComboBox.setDisable(disable);
        nameOfArticleTextField.setDisable(disable);
        priceOfArticleTextField.setDisable(disable);
        addArticleButton.setDisable(disable);
        createBillButton.setDisable(disable);
    }


    void setMainWindowViewController(MainWindowViewController mwvc) {
        this.mainWindowViewController = mwvc;
    }

    public void refresh() {
        refreshCustomerList();
        favouriteEmployeeComboBox.setItems(FXCollections.observableArrayList(mainWindowViewController.getManTheSController()
                .getOffice().getEmployees()));
    }

    /**
     * @see view.viewAUI.CostumerViewAUI#refreshCustomerList()
     */
    public void refreshCustomerList() {
        List<Customer> customers = mainWindowViewController
                .getManTheSController()
                .getOffice().getCustomers();
        customerTable.getItems().setAll(customers);
        customerTable.getSelectionModel().select(null);
        setFormFieldsDisable(true);
        this.clearFields();
    }

    public void showValidationError(String error) {
        Alert a = new Alert(AlertType.WARNING);
        a.setContentText(error);
        a.show();
    }

}

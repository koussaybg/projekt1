package view.viewController.customControls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Person;
import model.TimeFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PersonInputControl extends AnchorPane {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField streetNumberTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField mobileTextField;

    public PersonInputControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/PersonInputControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Person getValue() {
        Person person = new Person();
        person.setFirstName(firstNameTextField.getText());
        person.setLastName(lastNameTextField.getText());
        person.setStreet(streetTextField.getText());
        person.setHouseNumber(streetNumberTextField.getText());
        person.setPostalCode(postCodeTextField.getText());
        person.setCity(cityTextField.getText());
        person.setDayOfBirth(birthdayDatePicker.getValue());
        person.setPhone(phoneTextField.getText());
        person.setMobile(mobileTextField.getText());
        return person;
    }

    public void setValue(Person person) {
        firstNameTextField.setText(person.getFirstName());
        lastNameTextField.setText(person.getLastName());
        birthdayDatePicker.setValue(person.getDayOfBirth());
        phoneTextField.setText(person.getPhone());
        mobileTextField.setText(person.getMobile());
        streetTextField.setText(person.getStreet());
        streetNumberTextField.setText(person.getHouseNumber());
        postCodeTextField.setText(person.getPostalCode());
        cityTextField.setText(person.getCity());
    }


    public void clearFields() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        birthdayDatePicker.setValue(null);
        phoneTextField.setText("");
        mobileTextField.setText("");
        streetTextField.setText("");
        streetNumberTextField.setText("");
        postCodeTextField.setText("");
        cityTextField.setText("");
    }

    /**
     * disable/undisable textfields.
     * @param disable
     */
    public void setFieldsDisabled(boolean disable){
        firstNameTextField.setDisable(disable);
        firstNameTextField.setDisable(disable);
        lastNameTextField.setDisable(disable);
        birthdayDatePicker.setDisable(disable);
        phoneTextField.setDisable(disable);
        mobileTextField.setDisable(disable);
        streetTextField.setDisable(disable);
        streetNumberTextField.setDisable(disable);
        postCodeTextField.setDisable(disable);
        cityTextField.setDisable(disable);
    }

}

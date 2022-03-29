package view.viewController.customControls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.TimeFrame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class WeekdayTimeControl extends GridPane {

    @FXML
    private TextField monStartTextField;
    @FXML
    private TextField monEndTextField;
    @FXML
    private TextField tueStartTextField;
    @FXML
    private TextField tueEndTextField;
    @FXML
    private TextField wedStartTextField;
    @FXML
    private TextField wedEndTextField;
    @FXML
    private TextField thuStartTextField;
    @FXML
    private TextField thuEndTextField;
    @FXML
    private TextField friStartTextField;
    @FXML
    private TextField friEndTextField;
    @FXML
    private TextField satStartTextField;
    @FXML
    private TextField satEndTextField;
    @FXML
    private TextField sunStartTextField;
    @FXML
    private TextField sunEndTextField;

    private String parsedStringPrefix;
    private String parsedStringSuffix;
    private boolean disable;

    public WeekdayTimeControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/WeekdayTimeControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public TimeFrame[] getValue() throws DateTimeParseException, IllegalArgumentException{
        return getWeekdayTimes();
    }

    public void setValue(TimeFrame[] weekDayTimes) {
        if (weekDayTimes != null) {
            setWeekdayTimes(weekDayTimes);
        } else {
            clearFields();
        }
    }


    private void setWeekdayTimes(TimeFrame[] weekDayTimes) {
        setWeekdayFields(weekDayTimes[0], monStartTextField, monEndTextField);
        setWeekdayFields(weekDayTimes[1], tueStartTextField, tueEndTextField);
        setWeekdayFields(weekDayTimes[2], wedStartTextField, wedEndTextField);
        setWeekdayFields(weekDayTimes[3], thuStartTextField, thuEndTextField);
        setWeekdayFields(weekDayTimes[4], friStartTextField, friEndTextField);
        setWeekdayFields(weekDayTimes[5], satStartTextField, satEndTextField);
        setWeekdayFields(weekDayTimes[6], sunStartTextField, sunEndTextField);
    }

    private void setWeekdayFields(TimeFrame weekdayTime, TextField startTextField, TextField endTextField) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        if (weekdayTime != null) {
            startTextField.setText(dateTimeFormatter.format(weekdayTime.getStart()));
            endTextField.setText(dateTimeFormatter.format(weekdayTime.getEnd()));
        }else{
            startTextField.setText("");
            endTextField.setText("");
        }
    }

    // TODO add error handling and validation
    private TimeFrame[] getWeekdayTimes()  throws DateTimeParseException{
        TimeFrame[] weekdayTimes = new TimeFrame[7];
        parsedStringPrefix = "2000-01-01T";
        parsedStringSuffix = ":00";
        weekdayTimes[0] = getTimeFrameFromFields(monStartTextField, monEndTextField);
        weekdayTimes[1] = getTimeFrameFromFields(tueStartTextField, tueEndTextField);
        weekdayTimes[2] = getTimeFrameFromFields(wedStartTextField, wedEndTextField);
        weekdayTimes[3] = getTimeFrameFromFields(thuStartTextField, thuEndTextField);
        weekdayTimes[4] = getTimeFrameFromFields(friStartTextField, friEndTextField);
        weekdayTimes[5] = getTimeFrameFromFields(satStartTextField, satEndTextField);
        weekdayTimes[6] = getTimeFrameFromFields(sunStartTextField, sunEndTextField);
        return weekdayTimes;
    }

    private TimeFrame getTimeFrameFromFields(TextField startField, TextField endField) throws DateTimeParseException, IllegalArgumentException{
        if (!startField.getText().isEmpty() && !endField.getText().isEmpty()) {
            LocalDateTime startDateTime = LocalDateTime.parse(parsedStringPrefix + startField.getText() + parsedStringSuffix);
            LocalDateTime endDateTime = LocalDateTime.parse(parsedStringPrefix + endField.getText() + parsedStringSuffix);
            return new TimeFrame(startDateTime, endDateTime);
        }
        return null;
    }

    private void clearFields() {
        monStartTextField.setText("");
        monEndTextField.setText("");
        tueStartTextField.setText("");
        tueEndTextField.setText("");
        wedStartTextField.setText("");
        wedEndTextField.setText("");
        thuStartTextField.setText("");
        thuEndTextField.setText("");
        friStartTextField.setText("");
        friEndTextField.setText("");
        satStartTextField.setText("");
        satEndTextField.setText("");
        sunStartTextField.setText("");
        sunEndTextField.setText("");
    }

    /**
     * disable/undisable textfields.
     * @param disable
     */
    public void setFieldsDisabled(boolean disable){
        monStartTextField.setDisable(disable);
        monEndTextField.setDisable(disable);
        tueStartTextField.setDisable(disable);
        tueEndTextField.setDisable(disable);
        wedStartTextField.setDisable(disable);
        wedEndTextField.setDisable(disable);
        thuStartTextField.setDisable(disable);
        thuEndTextField.setDisable(disable);
        friStartTextField.setDisable(disable);
        friEndTextField.setDisable(disable);
        satStartTextField.setDisable(disable);
        satEndTextField.setDisable(disable);
        sunStartTextField.setDisable(disable);
        sunEndTextField.setDisable(disable);
    }

}

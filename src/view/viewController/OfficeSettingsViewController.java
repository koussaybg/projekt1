package view.viewController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import controller.ManTheSController;
import controller.OfficeSettingsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Office;
import model.TimeFrame;
import view.viewAUI.OfficeSettingsViewAUI;

public class OfficeSettingsViewController implements OfficeSettingsViewAUI {

    @FXML
    private SettingsViewController settingsViewController;

    public TextField officeNameTextField;
    public TextField streetTextField;
    public TextField streetNumberTextField;
    public TextField postCodeTextField;
    public TextField cityTextField;
    public TextField phoneTextField;

    public TextField monStartTextField;
    public TextField monEndTextField;
    public TextField tueStartTextField;
    public TextField tueEndTextField;
    public TextField wedStartTextField;
    public TextField wedEndTextField;
    public TextField thuStartTextField;
    public TextField thuEndTextField;
    public TextField friStartTextField;
    public TextField friEndTextField;
    public TextField satStartTextField;
    public TextField satEndTextField;
    public TextField sunStartTextField;
    public TextField sunEndTextField;

    // @FXML
    // public void initialize(){
    // 	refreshOfficeSettings();
    // }
    public void onImportBackupButtonClicked(ActionEvent action) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Backup importieren aus");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Serialisierung", "*.ser"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                settingsViewController.getMainWindowViewController().getManTheSController().getiOController().importBackup(selectedFile.getAbsolutePath());
                settingsViewController.getMainWindowViewController().refresh();
            }
        } catch (IOException e) {
            settingsViewController.getMainWindowViewController().showError("Import fehlgeschlagen.");
        }
    }

    /**
     * TODO: Aktuell wegen NullPointerException ab Zeile 61 fehlerhaft
     */
    public void onExportBackupButtonClicked(ActionEvent action) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Backup speichern unter");
            fileChooser.setInitialFileName("office.ser");
            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                settingsViewController.getMainWindowViewController().getManTheSController().getiOController().exportBackup(selectedFile.getAbsolutePath());
            }
        } catch (IOException e) {
            settingsViewController.getMainWindowViewController().showError("Export fehlgeschlagen.");
        }
    }

    public void onCancelButtonClicked(ActionEvent action) {
        refreshOfficeSettings();
    }

    public void onSaveButtonClicked(ActionEvent action) {
        String newName = officeNameTextField.getText().trim();

        String newStreet = streetTextField.getText().trim();
        String newHouseNumber = streetNumberTextField.getText().trim();
        String newPostalCode = postCodeTextField.getText().trim();
        String newCity = cityTextField.getText().trim();

        String phoneNumber = phoneTextField.getText();

        TimeFrame[] newOpeningTimes = buildTimeFrames();

        if (newOpeningTimes == null)
            return;

        MainWindowViewController mwvc = this.settingsViewController.getMainWindowViewController();
        ManTheSController manthes = mwvc.getManTheSController();
        OfficeSettingsController osc = manthes.getOfficeSettingsController();
        Office newOffice = new Office();
        newOffice.setName(newName);
        newOffice.setStreet(newStreet);
        newOffice.setHouseNumber(newHouseNumber);
        newOffice.setPostalCode(newPostalCode);
        newOffice.setCity(newCity);
        newOffice.setPhoneNumber(phoneNumber);
        newOffice.setOpeningTimes(newOpeningTimes);
        osc.updateOffice(newOffice);
    }

    private TimeFrame[] buildTimeFrames() {
        TimeFrame[] newOpeningTimes = new TimeFrame[7];
        String dateFormat = "2000-01-01T";

        try {
            LocalDateTime start = LocalDateTime.parse(dateFormat + monStartTextField.getText());
            LocalDateTime end = LocalDateTime.parse(dateFormat + monEndTextField.getText());
            newOpeningTimes[0] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + tueStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + tueEndTextField.getText());
            newOpeningTimes[1] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + wedStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + wedEndTextField.getText());
            newOpeningTimes[2] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + thuStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + thuEndTextField.getText());
            newOpeningTimes[3] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + friStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + friEndTextField.getText());
            newOpeningTimes[4] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + satStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + satEndTextField.getText());
            newOpeningTimes[5] = new TimeFrame(start, end);

            start = LocalDateTime.parse(dateFormat + sunStartTextField.getText());
            end = LocalDateTime.parse(dateFormat + sunEndTextField.getText());
            newOpeningTimes[6] = new TimeFrame(start, end);
        } catch (Exception e) {
            settingsViewController.getMainWindowViewController().showError("Fehlerhafte Eingaben der Öffnungszeiten!");
            return null;
        }

        return newOpeningTimes;
    }

    public void setSettingsViewController(SettingsViewController svc) {
        this.settingsViewController = svc;
    }

    /**
     * @see view.viewAUI.OfficeSettingsViewAUI#refreshOfficeSettings()
     */
    public void refreshOfficeSettings() {
        MainWindowViewController mwvc = settingsViewController.getMainWindowViewController();
        ManTheSController manthes = mwvc.getManTheSController();
        Office office = manthes.getOffice();

        officeNameTextField.setText(office.getName());

        streetTextField.setText(office.getStreet());
        streetNumberTextField.setText(office.getHouseNumber());
        postCodeTextField.setText(office.getPostalCode());
        cityTextField.setText(office.getCity());

        phoneTextField.setText(office.getPhoneNumber());

        this.displayOpeningTimes(office);
    }

    private void displayOpeningTimes(Office office) {
        TimeFrame[] openingTimes = office.getOpeningTimes();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime currentTime = openingTimes[0].getStart();
        currentTime = openingTimes[0].getStart();
        monStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[0].getEnd();
        monEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[1].getStart();
        tueStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[1].getEnd();
        tueEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[2].getStart();
        wedStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[2].getEnd();
        wedEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[3].getStart();
        thuStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[3].getEnd();
        thuEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[4].getStart();
        friStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[4].getEnd();
        friEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[5].getStart();
        satStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[5].getEnd();
        satEndTextField.setText(currentTime.format(formatter));

        currentTime = openingTimes[6].getStart();
        sunStartTextField.setText(currentTime.format(formatter));
        currentTime = openingTimes[6].getEnd();
        sunEndTextField.setText(currentTime.format(formatter));
    }
}

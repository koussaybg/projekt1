package controller;

import model.Office;
import model.TimeFrame;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class OfficeSettingsControllerTest {
//
//    private OfficeSettingsController testOfficeSettingsController;
//    private Office testOffice;
//
//    @Before
//    public void setUp() throws Exception {
//        testOfficeSettingsController = new OfficeSettingsController();
//        testOffice = new Office();
//        ManTheSController manTheSController = new ManTheSController();
//        manTheSController.setOffice(testOffice);
//        testOfficeSettingsController.setManTheSController(manTheSController);
//    }
//
//    @Test
//    public void updateOfficeUpdateUpdateName() {
//        String newName = "TestName";
//        testOfficeSettingsController.updateOffice(newName, null,null, null, null, null, null);
//        assertEquals(newName, testOffice.getName());
//    }
//
//    @Test
//    public void updateOfficeUpdateUpdateStreet() {
//        String newStreet = "TestStreet";
//        testOfficeSettingsController.updateOffice(null, newStreet,null, null, null, null, null);
//        assertEquals(newStreet, testOffice.getStreet());
//    }
//    
//    @Test
//    public void updateOfficeUpdateUpdateHouseNumber() {
//        String newHouseNumber = "12";
//        testOfficeSettingsController.updateOffice(null, newHouseNumber,null, null, null, null, null);
//        assertEquals(newHouseNumber, testOffice.getHouseNumber());
//    }
//    
//    @Test
//    public void updateOfficeUpdateUpdatePostalCode() {
//        String newPostalCode = "43432";
//        testOfficeSettingsController.updateOffice(null, newPostalCode,null, null, null, null, null);
//        assertEquals(newPostalCode, testOffice.getPostalCode());
//    }
//    
//    @Test
//    public void updateOfficeUpdateUpdateCity() {
//        String newCity = "TestCity";
//        testOfficeSettingsController.updateOffice(null, newCity,null, null, null, null, null);
//        assertEquals(newCity, testOffice.getCity());
//    }
//
//    @Test
//    public void updateOfficeUpdateUpdateOpeningHoursValid() {
//        TimeFrame[] openingHours = new TimeFrame[7];
//        for(int i = 0; i < openingHours.length; i++){
//            openingHours[i] = new TimeFrame(LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));
//        }
//        testOfficeSettingsController.updateOffice(null,null, null, null, null, null, openingHours);
//        assertArrayEquals(openingHours, testOffice.getOpeningTimes());
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void updateOfficeUpdateUpdateOpeningHoursWrongArraySize() {
//        TimeFrame[] openingHours = new TimeFrame[6];
//        for(int i = 0; i < openingHours.length; i++){
//            openingHours[i] = new TimeFrame(LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));
//        }
//        testOfficeSettingsController.updateOffice(null,null, null, null, null, null, openingHours);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void updateOfficeUpdateUpdateOpeningHoursWrongEmptyTimeFrames() {
//        TimeFrame[] openingHours = new TimeFrame[7];
//        testOfficeSettingsController.updateOffice(null,null, null, null, null, null, openingHours);
//    }
//
//    @Test
//    public void updateOfficeUpdateUpdateAllValues() {
//        String newName = "TestName";
//        String newStreet = "TestStreet";
//        String newHouseNumber = "12";
//        String newPostalCode = "43432";
//        String newCity = "TestCity";
//        TimeFrame[] openingHours = new TimeFrame[7];
//        for(int i = 0; i < openingHours.length; i++){
//            openingHours[i] = new TimeFrame(LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));
//        }
//        testOfficeSettingsController.updateOffice(newName, newStreet, newHouseNumber, newPostalCode, newCity, null, openingHours);
//        assertArrayEquals(openingHours, testOffice.getOpeningTimes());
//        assertEquals(newName, testOffice.getName());
//        assertEquals(newStreet, testOffice.getStreet());
//        assertEquals(newHouseNumber, testOffice.getHouseNumber());
//        assertEquals(newPostalCode, testOffice.getPostalCode());
//        assertEquals(newCity, testOffice.getCity());
//    }
//

}
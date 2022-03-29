package jfxtras.samples.controls.agenda;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.scene.control.agenda.AgendaSkinSwitcher;
import jfxtras.scene.layout.GridPane;
 
public class CalendarView extends JFXtrasSampleBase
{
	final Agenda agenda;
	private List<model.Appointment> appointmentsToShow;
	
	final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
	
    public CalendarView() {

        agenda = new Agenda();
 
        agenda.allowDraggingProperty().setValue(false);
        agenda.allowResizeProperty().setValue(false);
        agenda.displayedLocalDateTime().set(LocalDateTime.now());
        // setup appointment groups
        
        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
            lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
        }
 
        // accept new appointments
        /*agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(LocalDateTimeRange dateTimeRange)
            {
                return new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime() )
                .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime() )
                .withSummary("new")
                .withDescription("new")
                .withAppointmentGroup(lAppointmentGroupMap.get("group01"));
            }
        });*/
 
        int idx = 0;
        
        if (appointmentsToShow != null) {
        	Appointment[] lTestAppointments = new Appointment[appointmentsToShow.size()];
        	for (int i = 0; i < appointmentsToShow.size(); i++) {
        		String description = appointmentsToShow.get(i).getTreatmentType().getName() + ", ";
        		for (model.Customer cust : appointmentsToShow.get(i).getCustomers()) {
        			description += cust.getName() + ", ";
        		}
            	lTestAppointments[i] = new Agenda.AppointmentImplLocal()
            			.withStartLocalDateTime(appointmentsToShow.get(i).getTimeFrame().getStart())
            			.withEndLocalDateTime(appointmentsToShow.get(i).getTimeFrame().getEnd())
            			.withSummary(description)
            			.withLocation(appointmentsToShow.get(i).getRoom().getName())
            			.withAppointmentGroup(lAppointmentGroupMap.get("group20"));
            }
        	

            agenda.appointments().addAll(lTestAppointments);
                      
        	
        }
         /*{
            new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(8, 00)))
                .withEndLocalDateTime(LocalDateTime.of(lTodayLocalDate, LocalTime.of(11, 30)))
                .withSummary("A")
                .withDescription("A much longer test description")
                .withAppointmentGroup(lAppointmentGroupMap.get("group07"))};*/
        

         
        // action
        /*agenda.setActionCallback( (appointment) -> {
            showPopup(agenda, "Action on " + appointment);
            return null;
        });*/
    }
    
    public Node setNewAppointments(List<model.Appointment> appointmentsToShow) {
    	agenda.appointments().clear();
    	if (appointmentsToShow != null) {
        	Appointment[] lTestAppointments = new Appointment[appointmentsToShow.size()];
        	for (int i = 0; i < appointmentsToShow.size(); i++) {
        		String description = appointmentsToShow.get(i).getTreatmentType().getName() + ", ";
        		for (model.Customer cust : appointmentsToShow.get(i).getCustomers()) {
        			description += cust.getName() + ", ";
        		}
            	lTestAppointments[i] = new Agenda.AppointmentImplLocal()
            			.withStartLocalDateTime(appointmentsToShow.get(i).getTimeFrame().getStart())
            			.withEndLocalDateTime(appointmentsToShow.get(i).getTimeFrame().getEnd())
            			.withSummary(description)
            			.withLocation(appointmentsToShow.get(i).getRoom().getName())
                        .withAppointmentGroup(lAppointmentGroupMap.get("group20"));
            }
        	

            agenda.appointments().addAll(lTestAppointments);
            
        }
    	return agenda;
    }
    
    public static void main(String[] args) {
    	 launch(args);
    }
    
     
    public void print() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            agenda.print(job);
            job.endJob();
        }
    }


    
    @Override
    public String getSampleName() {
        //return this.getClass().getSimpleName();
    	return null;
    }
 
    @Override
    public String getSampleDescription() {
        return "";
    }
 
    public Node getPanel(List<model.Appointment> appointmentsToShow) {
    	this.appointmentsToShow = appointmentsToShow;
        return agenda;
    }
 
    @Override
    public Node getControlPanel() {
        return null;
    }

    static private Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c)
    {
        // result
        int lFirstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
        int lCurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int lDelta = 0;
        if (lFirstDayOfWeek <= lCurrentDayOfWeek)
        {
            lDelta = -lCurrentDayOfWeek + lFirstDayOfWeek;
        }
        else
        {
            lDelta = -lCurrentDayOfWeek - (7-lFirstDayOfWeek);
        }
        c = ((Calendar)c.clone());
        c.add(Calendar.DATE, lDelta);
        return c;
    }
    

	@Override
	public String getJavaDocURL() {
		return null;
	}

	@Override
	public Node getPanel(Stage arg0) {
		// TODO Auto-generated method stub
		return null;
	}
 
}

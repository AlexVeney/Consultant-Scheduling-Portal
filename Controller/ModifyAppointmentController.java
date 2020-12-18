/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import DAO.CustomerTableDAO;
import Model.Appointment;
import Model.AppointmentTable;
import Model.Customer;
import Model.CustomerTable;
import static DAO.AppointmentDAO.apptTimeCheck;
import static DAO.AppointmentDAO.overlapCheck;
import Utilities.SchedulingException;
import static Utilities.Time.fromTimeOfDay;
import static Utilities.Time.stringToLDT;
import alexveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static Utilities.Time.localToUTC;

/**
 * FXML Controller class
 *
 * @author Alex Veney
 */
public class ModifyAppointmentController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> hourBox;
    @FXML
    private ComboBox<String> minuteBox;
    @FXML
    private ComboBox<String> ampmBox;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TableView<CustomerTable> customerTableView;
    @FXML
    private TableColumn<CustomerTable, String> customerNameColumn;
    @FXML
    private TableColumn<CustomerTable, String> customerAddressColumn;
    @FXML
    private TableColumn<CustomerTable, String> customerPhoneColumn;

    // Fields to hold original customer details
    String origDate;
    String origHour;
    String origMinute;
    String origAmPm;
    String origType;
    int origCusId;
    String origDateTime;
    
    // Lists to hold options for time and type
    ObservableList<String> hoursList = FXCollections.observableArrayList();
    ObservableList<String> minutesList = FXCollections.observableArrayList();
    ObservableList<String> ampmList = FXCollections.observableArrayList();
    ObservableList<String> typeList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        try {
            
            customerTableView.setItems(CustomerTableDAO.getAllCustomers());

        
        } catch (Exception ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        
         // Populate Hour ComboBox
        hoursList.addAll("12","1","2","3","4","5","6","7","8","9","10","11");

        hourBox.setItems(hoursList);


        // Populate Minute ComboBox
        minutesList.addAll("00","15","30","45");

        minuteBox.setItems(minutesList);



        // Populate ampm ComboBox
        ampmList.addAll("AM","PM");

        ampmBox.setItems(ampmList);

        
        // Populate type ComboBox
        typeList.addAll("Presentation","Scrum", "Signing");

        typeBox.setItems(typeList);

        

    }    

    @FXML
    // Save appointment modifications to database
    private void onActionSaveModifiedAppointment(ActionEvent event) throws IOException, Exception {
        
            // Get original appt obj from DB
            Appointment origAppt = AppointmentDAO.getAppointment(origDateTime,origType, origCusId);
            
            if(origAppt == null){
                System.out.println("Appt null");
                return;
            }
        
        

         // Modify appt
        CustomerTable customerT = customerTableView.getSelectionModel().getSelectedItem();
        Customer customer = CustomerDAO.getCustomer(customerT.getCustomerName());
        
        
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        if(apptTimeCheck(hourBox.getValue(), minuteBox.getValue(), ampmBox.getValue()) == null){
            return;
        }
        
        try{
        String time = apptTimeCheck(hourBox.getValue(), minuteBox.getValue(), ampmBox.getValue());
        
        if(overlapCheck(localToUTC(date+" "+time))){
             throw new SchedulingException("Appointment date and time cannot overlap!");
        }
        System.out.println(SchedulingApp.getUserId());
        AppointmentDAO.updateAppointment(origAppt, date, hourBox.getValue(),minuteBox.getValue(),
                ampmBox.getValue(), typeBox.getValue(), customer.getCustomerId(), SchedulingApp.getUserId());
   
        }catch(SchedulingException ex){
            System.out.println("Overlapping Appointments");
            return;
        }
        
        // Transition to Add Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
        
    }

    @FXML
    // Cancel appointment modification and return to main screen
    private void onActionCancel(ActionEvent event) throws IOException {
        
        // Transition to Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }
    
    // Transfer selected appointment details to modify appointment screen
    public void transferAppointment(AppointmentTable appointmentT) throws Exception{
       
        Customer cus = CustomerDAO.getCustomer(appointmentT.getCustomerName());
        String type = appointmentT.getType();
        String start = appointmentT.getStart();
        
        
        String formattedStart = fromTimeOfDay(start);
        LocalDateTime  date = stringToLDT(formattedStart);

        
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int year = date.getYear();
        
        LocalDate fullDate = LocalDate.of(year, month, day);
        
        String hour = String.valueOf(date.getHour());
        String minute = String.valueOf(date.getMinute());
       

        if(minute.equalsIgnoreCase("0")){
            minute = "00";
        }
  

        
        String amPm;
        if(date.getHour() > 12){
            amPm="PM";
            int num; 
            num = date.getHour() - 12;
            hour = String.valueOf(num);
            
        }else if (date.getHour() == 0){
            hour = String.valueOf(12);
            amPm="AM";
        } else if(date.getHour() == 12){
            amPm="PM";
        }else{
            amPm="AM";
        }
        
        
        datePicker.setValue(fullDate);
        hourBox.setValue(hour);
        minuteBox.setValue(minute);
        ampmBox.setValue(amPm);
        typeBox.setValue(type);
        
        
        //Save original details
        origDate = month +"/"+day+"/"+year;
        origHour = hour;
        origMinute = minute;
        origAmPm = amPm;
        origType = type;
        origCusId = cus.getCustomerId();

        
        String time = origHour+":"+origMinute+" "+origAmPm;
        origDateTime = origDate +" "+ time;
                
    }
}

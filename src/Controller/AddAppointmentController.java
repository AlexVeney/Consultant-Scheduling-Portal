/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AppointmentDAO;
import static DAO.AppointmentDAO.apptTimeCheck;
import static DAO.AppointmentDAO.overlapCheck;
import DAO.CustomerDAO;
import DAO.CustomerTableDAO;
import DAO.UserDAO;
import Model.Appointment;
import Model.Customer;
import Model.CustomerTable;
import Model.User;
import Utilities.SchedulingException;
import java.io.IOException;
import java.net.URL;
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
import static Utilities.Time.stringToLDT;
import alexandraveneyschedulingapp.SchedulingApp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static Utilities.Time.localToUTC;

/**
 * FXML Controller class
 *
 * @author avhomefolder
 */
public class AddAppointmentController implements Initializable {

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
    private TableColumn<CustomerTable, String>customerAddressColumn;
    @FXML
    private TableColumn<CustomerTable, String> customerPhoneColumn;


    
    
    // Lists to store box options for time and type
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
            
            // Select Default Item
            hourBox.getSelectionModel().select("8");
            
            
            
            
            // Populate Minute ComboBox
            minutesList.addAll("00","15","30","45");
            
            minuteBox.setItems(minutesList);
            
            // Select Default Item
            minuteBox.getSelectionModel().selectFirst();
            
            
            
            
            // Populate ampm ComboBox
            ampmList.addAll("AM","PM");
            
            ampmBox.setItems(ampmList);
            
            // Select Default Item
            ampmBox.getSelectionModel().selectFirst();
            
            
            // Populate ComboBox
            typeList.addAll("Presentation","Scrum", "Signing");
            
            typeBox.setItems(typeList);
            
            // Select Default Item
            typeBox.getSelectionModel().selectFirst();
            
        
        
         
    }   
    
    
    @FXML
    private void onActionSaveAddAppointment(ActionEvent event) throws Exception {
        
        
       
        // Date
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        

        // Check if appointment time set during business hours
        String time = apptTimeCheck(hourBox.getValue(), minuteBox.getValue(), ampmBox.getValue());
        
        // if time is null exit method 
        if(time == null){
            return;
        }
        
        String dateTime = date +" "+time;
        LocalDateTime ldt = stringToLDT(dateTime);
        
        // Type
        String type = typeBox.getValue();
        
        // Customer
        CustomerTable customerT = customerTableView.getSelectionModel().getSelectedItem();
        Customer customer = CustomerDAO.getCustomer(customerT.getCustomerName());
                
        User user = UserDAO.getUser(SchedulingApp.getUserId());
        
        
        // Create Appointment
        try{
        if(overlapCheck(localToUTC(dateTime))){
             throw new SchedulingException("Appointment date and time cannot overlap!");
        }
        
        Appointment appt;
        appt = new Appointment(customer.getCustomerId(), user.getUserId(),
                "","","","",type,"", ldt, ldt, LocalDateTime.now(),
                user.getUserName(),LocalDateTime.now(), user.getUserName() );
        
        AppointmentDAO.createAppointment(appt);
        
        }catch(SchedulingException ex){
            System.out.println("Overlapping Appointment");
            return;
        }
        
        
        
        
        
        
        // Transition to Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }

    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        
        // Transition to Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }

   


   
    
}

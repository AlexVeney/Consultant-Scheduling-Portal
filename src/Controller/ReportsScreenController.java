/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AppointmentDAO;
import DAO.AppointmentTableDAO;
import DAO.CityDAO;
import DAO.UserDAO;
import Model.AppointmentTable;
import static Utilities.Time.fromTimeOfDay;
import static Utilities.Time.stringToLDT;
import alexandraveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Alexandra Veney
 */
public class ReportsScreenController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private TextArea reportText;
    @FXML
    private RadioButton apptCountRB;
    @FXML
    private RadioButton userScheduleRB;
    @FXML
    private RadioButton cityEngagementRB;
    @FXML
    private ToggleGroup repToggleGroup;
    @FXML
    private Text reportDescription;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    


    @FXML
    private void onActionChangeToMainScreen(ActionEvent event) throws IOException {
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
        
    }

    @FXML
    private void onActionApptCount(ActionEvent event) throws Exception{
        reportDescription.setText("Report displays appointment count by month");
        reportText.clear();

        
       // LAMBDA EXPRESSION used to efficiently iterate through TreeMap and append to text
       // box with less code
       getAppointmentCounts().forEach((k, v) -> {
                reportText.appendText("Month: " + k + ", Count: " + v+"\n");
	});
       
      
    }

    @FXML
    private void onActionUserSchedule(ActionEvent event) throws Exception  {
        reportDescription.setText("Report displays the schedule by user");
        reportText.clear();
        
        
        //LAMBDA EXPRESSION (NESTED) used to efficiently iterate through arraylist and append to text
        // box with less code. For each user the appointments list is search to get schedule for user
        UserDAO.getUsers().forEach((userId, userName) ->{
            reportText.appendText(userName.toUpperCase()+"\n");
            
            try {
                AppointmentDAO.getAppointments(userId).forEach((appt) ->{
                    reportText.appendText(appt+"\n");
                });
            } catch (Exception ex) {
                Logger.getLogger(ReportsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            reportText.appendText("\n");
        });
        
       
       
    }

    @FXML
    private void onActionCityEngagement(ActionEvent event) throws Exception {
        reportDescription.setText("Report displays cities with a customer base");
        reportText.clear();
        
        // LAMBDA EXPRESSION used to efficiently iterate through arraylist and append to text
        // box with less code
        CityDAO.getCities().forEach((city) ->{
            reportText.appendText(city+"\n");
        });
    }
    
    
    private TreeMap<String, Integer> getAppointmentCounts() throws Exception{
        
        TreeMap<String,Integer> apptCount = new TreeMap<>();
        
        for(AppointmentTable appt : AppointmentTableDAO.getAllAppointments()){
            LocalDate date = stringToLDT(fromTimeOfDay(appt.getStart())).toLocalDate();
            int month = date.getMonthValue();
            int year = date.getYear();
            String key = month + "/"+ year;
            
            if(apptCount.containsKey(key)){
                int value = apptCount.get(key);
                apptCount.put(key, value+1);
            }else{
                apptCount.put(key, 1);
            } 
        }
        return apptCount;
    }
    
    
  
    
}

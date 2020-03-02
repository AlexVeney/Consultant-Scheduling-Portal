/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AppointmentTableDAO;
import Model.AppointmentTable;
import static Utilities.Time.fromTimeOfDay;
import static Utilities.Time.stringToLDT;
import alexandraveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Alexandra Veney
 */
public class CalendarScreenController implements Initializable {

    @FXML
    private TableView<AppointmentTable> appointmentTableView;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentDateColumn;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentTypeColumn;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentCustomerNameColumn;    

    
        
    private ObservableList<AppointmentTable> filteredAppts = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        try {
            filteredAppts = getAppointments(1);
          
        } catch (Exception ex) {
            Logger.getLogger(CalendarScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
            appointmentTableView.setItems(filteredAppts);
            appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
            appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            appointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
    }    

    @FXML
    private void onActionMonthView(ActionEvent event) {
        filteredAppts.clear();
        appointmentTableView.setItems(getAppointments(1));
    }

    @FXML
    private void onActionWeekView(ActionEvent event) {
        filteredAppts.clear();
        appointmentTableView.setItems(getAppointments(2));
    }

    @FXML
    private void onActionChangeToMainScreen(ActionEvent event) throws IOException {
        
        // Transition to main application screen
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }
    
    
    private ObservableList<AppointmentTable> getAppointments(int option){
        ObservableList<AppointmentTable> list = FXCollections.observableArrayList();
        // Amount of time from today (month or week)
        LocalDate timeFromToday; 
        
        try {
            LocalDate today = LocalDate.now();
            
            switch (option) {
            case 1:
                timeFromToday = today.plusMonths(1);
                break;
            case 2:
                timeFromToday = today.plusWeeks(1);
                break;
            default:
                throw new Exception("Invalid option selected");
            }
            
            
            for(AppointmentTable appt: AppointmentTableDAO.getAllAppointments()){                                

                LocalDate apptDate = stringToLDT(fromTimeOfDay(appt.getStart())).toLocalDate();
                
                if(apptDate.equals(today) || apptDate.equals(timeFromToday) || 
                        (apptDate.isAfter(today) && apptDate.isBefore(timeFromToday))){
                    System.out.println(appt.getStart());
                    list.add(appt);
                }  
               
            }
            
            return list;
        } catch (Exception ex) {
            Logger.getLogger(CalendarScreenController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
}

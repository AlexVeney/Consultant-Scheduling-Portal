/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import DAO.AppointmentTableDAO;
import DAO.CustomerDAO;
import DAO.CustomerTableDAO;
import Model.AppointmentTable;
import Model.Customer;
import Model.CustomerTable;
import DAO.AppointmentDAO;
import Model.Appointment;
import alexandraveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author avhomefolder
 */
public class MainScreenController implements Initializable {

   
    @FXML
    private TableView<CustomerTable> customerTableView;
    @FXML
    private TableColumn<CustomerTable, String> customerNameColumn;
    @FXML
    private TableColumn<CustomerTable, String> customerAddressColumn;
    @FXML
    private TableColumn<CustomerTable, String> customerPhoneColumn;
   
    @FXML
    private TableView<AppointmentTable> appointmentTableView;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentDateColumn;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentTypeColumn;
    @FXML
    private TableColumn<AppointmentTable, String> appointmentCustomerNameColumn;



  
    /**
     * Initializes the controller class.
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        
        try {
            
            customerTableView.setItems(CustomerTableDAO.getAllCustomers());

        
        } catch (Exception ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("fullAddress"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
         
        
        try {
            //
            appointmentTableView.setItems(AppointmentTableDAO.getAllAppointments());
        } catch (Exception ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        
     
        
    }    

    
    
    @FXML
    private void onActionAddCustomerScreen(ActionEvent event) throws IOException {
        
  
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/AddCustomerScreen.fxml");
  
        
    
    }

    @FXML
    private void onActionModifyCustomerScreen(ActionEvent event) throws IOException, Exception {

        
        try{
        FXMLLoader loader = new FXMLLoader();
    
        
        loader.setLocation(getClass().getResource("/View/ModifyCustomerScreen.fxml"));
        loader.load();
        ModifyCustomerController ADMController = loader.getController();
        ADMController.transferCustomer(customerTableView.getSelectionModel().getSelectedItem());        
         
        Stage stage;
        
        stage =(Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene1 = loader.getRoot();
        stage.setScene(new Scene(scene1));
        stage.show();
        
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setContentText("Please select a customer to modify");
            alert.showAndWait();
        }
        
        refreshTableViews();
    }

    @FXML
    private void onActionDeleteSelectedCustomer(ActionEvent event) throws IOException, Exception {
        
        try{
        if(customerTableView.getSelectionModel().getSelectedItem()== null){
                throw new NullPointerException();
            }
        // Ask for confirmation to delete customer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Do you want to delete item?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes){
            
        CustomerTable customerT = customerTableView.getSelectionModel().getSelectedItem();

        
        Customer customer = CustomerDAO.getCustomer(customerT.getCustomerName());

        AppointmentDAO.deleteCustomerAppointments(customer.getCustomerId());
        CustomerDAO.deleteCustomer(customer.getCustomerId());
    
        refreshTableViews();
        }
        
        }catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setContentText("Please select an customer to delete");
            alert.showAndWait();      
        }
         
        
    }

    
    @FXML
    private void onActionAddAppointmentScreen(ActionEvent event) throws IOException {
        
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/AddAppointmentScreen.fxml");
    }

    @FXML
    private void onActionModifyAppointmentScreen(ActionEvent event) throws IOException, Exception {
        
        try{
            
        FXMLLoader loader = new FXMLLoader();
    
        loader.setLocation(getClass().getResource("/View/ModifyAppointmentScreen.fxml"));
        loader.load();
        ModifyAppointmentController ADMController = loader.getController();
        ADMController.transferAppointment(appointmentTableView.getSelectionModel().getSelectedItem());        
         
        Stage stage;
        
        stage =(Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene1 = loader.getRoot();
        stage.setScene(new Scene(scene1));
        stage.show();
        
        refreshTableViews();
        
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please select an appointment to modify");
            alert.showAndWait();
        }
    }
    

    @FXML
    private void onActionDeleteSelectedAppointment(ActionEvent event) throws Exception {
        
        try{
            if(appointmentTableView.getSelectionModel().getSelectedItem()== null){
                throw new NullPointerException();
            }
        // Ask for confirmation to delete appointment
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setContentText("Do you want to delete item?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes){
            
        AppointmentTable appointmentT = appointmentTableView.getSelectionModel().getSelectedItem();
        Customer customer = CustomerDAO.getCustomer(appointmentT.getCustomerName());
        int cusId = customer.getCustomerId();
        Appointment appointment = AppointmentDAO.getAppointment(appointmentT.getStart(), appointmentT.getType(),cusId );
        AppointmentDAO.deleteAppointment(appointment.getAppointmentId());
       

        refreshTableViews();
        
        } 
        
        }catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please select an appointment to delete");
            alert.showAndWait();      
        }
    }

    @FXML
    private void onActionExitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onActionCalendarPage(ActionEvent event) throws IOException {
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/CalendarScreen.fxml");
    }

    @FXML
    private void onActionReportsScreen(ActionEvent event) throws IOException {
        
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/ReportsScreen.fxml");
    }
    
    // Refresh table views to show modifications
    private void refreshTableViews() throws Exception{
        customerTableView.setItems(CustomerTableDAO.getAllCustomers());
        appointmentTableView.setItems(AppointmentTableDAO.getAllAppointments());
    }

}

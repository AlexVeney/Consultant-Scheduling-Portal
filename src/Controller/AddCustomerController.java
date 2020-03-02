/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.CustomerDAO;
import alexandraveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author avhomefolder
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField address2TextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneTextField;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onActionAddCustomer(ActionEvent event) throws Exception{
     
            
            
            if(CustomerDAO.validInput(nameTextField,
            addressTextField, cityTextField, countryTextField, postalCodeTextField, phoneTextField)){
            
                CustomerDAO.createCustomer(nameTextField,
                addressTextField,address2TextField, cityTextField, countryTextField, postalCodeTextField, phoneTextField);
                SchedulingApp action = new SchedulingApp();
                action.changePage(event, "/View/MainScreen.fxml");
            
            }else{
                
                System.out.println("Input entered is not valid");
            
            }
     
            
    }

    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        
        // Transition to Add Customer Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }
    
}


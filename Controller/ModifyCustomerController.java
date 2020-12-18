/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AddressDAO;
import DAO.CityDAO;
import DAO.CountryDAO;
import DAO.CustomerDAO;
import Model.Address;
import Model.City;
import Model.Country;
import Model.Customer;
import Model.CustomerTable;
import alexveneyschedulingapp.SchedulingApp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Alex Veney
 */
public class ModifyCustomerController implements Initializable {

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

    String origName; 
    String origAdd;
    String origAdd2;
    String origCountry;
    String origCity;
    String origPCode;
    String origPhone;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    
    @FXML
    // Modify selected customer with new details
    private void onActionModifyCustomer(ActionEvent event) throws SQLException, Exception {
        
        // Get original customer details from database
        Customer origCustomer = CustomerDAO.getCustomer(origName);



         // Modify customer
        CustomerDAO.updateCustomer(origCustomer, nameTextField, 
                    addressTextField,address2TextField, cityTextField, countryTextField, postalCodeTextField, phoneTextField );
   
        
        
        // Transition to Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");

    }

    // Transfer selected customer details from main screen to modify customer screen
    public void transferCustomer(CustomerTable customerT) throws Exception{
        
        // Set text fields
        Customer customer = CustomerDAO.getCustomer(customerT.getCustomerName());
        
        nameTextField.setText(String.valueOf(customer.getCustomerName()));
        
        int addressId = customer.getAddressId();
        Address address = AddressDAO.getAddress(addressId);
        
        addressTextField.setText(String.valueOf(address.getAddress()));
        address2TextField.setText(String.valueOf(address.getAddress2()));
        postalCodeTextField.setText(String.valueOf(address.getPostalCode()));
        phoneTextField.setText(String.valueOf(address.getPhone()));
        
        int cityId = address.getCityId();
        City city = CityDAO.getCity(cityId);
        
        cityTextField.setText(String.valueOf(city.getCity()));
     
        int countryId = city.getCountryId();
        Country country = CountryDAO.getCountry(countryId);

        
        countryTextField.setText(String.valueOf(country.getCountry()));
        
        
        // Save original details
        origName = String.valueOf(nameTextField.getText());
        origAdd = addressTextField.getText();
        origAdd2 =  address2TextField.getText();
        origCountry = countryTextField.getText();
        origCity = cityTextField.getText();
        origPCode = postalCodeTextField.getText();
        origPhone = phoneTextField.getText();
        

 
    }
    
    @FXML
    // Cancel modifying customer and return to main screen 
    private void onActionCancel(ActionEvent event) throws IOException {
        
        // Transition to Main Screen 
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
    }
    
}

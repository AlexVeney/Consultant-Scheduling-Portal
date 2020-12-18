/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Address;
import Model.City;
import Model.Country;
import Model.Customer;
import Model.User;
import Utilities.SchedulingException;
import static Utilities.Time.LDTToString;
import static Utilities.Time.stringToLDT;
import alexveneyschedulingapp.SchedulingApp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

/**
 *
 * @author Alex Veney
 */
public class CustomerDAO {
    

    
    // INSERT Customer into database
    public static void createCustomer(TextField nameTF, TextField addressTF,
            TextField address2TF, TextField cityTF, TextField countryTF, 
            TextField postalCodeTF, TextField phoneTF) throws  SQLException, Exception
    {
        
       
       String name = nameTF.getText();
       String address = addressTF.getText();
       String address2 = address2TF.getText();
       String city = cityTF.getText();
       String country = countryTF.getText();
       String postalCode = postalCodeTF.getText();
       String phone = phoneTF.getText();
        
       

      // Get COUNTRY id for CUSTOMER or creates new COUNTRY in database if DNE
        

            // Object used to obtain current user name
            User userObj = UserDAO.getUser(SchedulingApp.getUserId());
            //username
            String usernameOfCurrUser = userObj.getUserName();

            // Object used to get countryId of entered country
            int countryIdForCustomer;
            Country retCountry;
            
            // if country dne, create country
            if( CountryDAO.getCountry(country) == null){
                
                LocalDateTime ldt = LocalDateTime.now();
                System.out.println(country);
                System.out.println(ldt);
                System.out.println(usernameOfCurrUser);

                                
                Country newCountry = new Country(country, ldt, usernameOfCurrUser, ldt, usernameOfCurrUser);
                
                CountryDAO.insertCountry(newCountry);

                retCountry = CountryDAO.getCountry(country);
                countryIdForCustomer = retCountry.getId();
             
            }else{
                retCountry = CountryDAO.getCountry(country);
                countryIdForCustomer = retCountry.getId();
            }
       
     
            
      // Get CITY id for CUSTOMER or creates new CITY  in database if DNE
      

        // Object used to get cityId of entered country
        City retrievedCity;
        int cityIdForCustomer;   
        
        
        // if city dne, create city
        if(CityDAO.getCity(city)==null){
          
            
            City newCity = new City(city, countryIdForCustomer,LocalDateTime.now(),
            usernameOfCurrUser, LocalDateTime.now(),usernameOfCurrUser);
            
            
            CityDAO.insertCity(newCity, newCity.getCountryId());
            
            retrievedCity = CityDAO.getCity(city);
            cityIdForCustomer = retrievedCity.getCityId();
            
        }else{
            retrievedCity = CityDAO.getCity(city);
            cityIdForCustomer = retrievedCity.getCityId();
        }
      
      
      // Get ADDRESS id for CUSTOMER or creates new ADDRESS in database if DNE      
      
      int addressIdForCustomer;

      // Object used to get addressId of entered address
      Address enteredAddress = new Address(address, address2, cityIdForCustomer, 
      postalCode, phone, LocalDateTime.now(), "", LocalDateTime.now(),"");

      
      Address retAddress;
      
      // if address dne, create address
      if(AddressDAO.getAddress(enteredAddress) == null){
          
          Address newAddress = new Address(address, address2, cityIdForCustomer, 
                                postalCode, phone,LocalDateTime.now(), usernameOfCurrUser, 
                                LocalDateTime.now(), usernameOfCurrUser);
          
          AddressDAO.insertAddress(newAddress);
          
          retAddress = AddressDAO.getAddress(newAddress);
          addressIdForCustomer = retAddress.getAddressId();
      }else{
          
          retAddress = AddressDAO.getAddress(enteredAddress);

          addressIdForCustomer = retAddress.getAddressId();
      }
      
      
      // Create new CUSTOMER if DNE
      Customer newCustomer = new Customer(name, addressIdForCustomer, true,
                            LocalDateTime.now(), usernameOfCurrUser, 
                            LocalDateTime.now(), usernameOfCurrUser);
      
      
      DBConnection.makeConnection();
      
  
      
       // Add customer if does not exists in the database 
      if(!exists(newCustomer.getCustomerName())){
       DBConnection.makeConnection();

       String sqlStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy"+
                ", lastUpdate, lastUpdateBy) VALUES("+
                "'" + newCustomer.getCustomerName()+ "'" +"," +
                newCustomer.getAddressId() + ",1" + ", '" +
                LDTToString(newCustomer.getCreateDate())+ "', " +
                "'" + newCustomer.getCreatedBy()+ "'" + ",'" +
                LDTToString(newCustomer.getLastUpdate())+ "', " +
                "'" + newCustomer.getLastUpdateBy()+ "')";
       Query.makeQuery(sqlStatement);
      }else{
          System.out.println("Customer already exists in the database!");
      }
       DBConnection.closeConnection();
  
    }
    
     // SELECT single CUSTOMER from database by customerId or customerName
    public static Customer getCustomer(Object obj) throws SQLException, Exception{
        DBConnection.makeConnection();
        
        String sqlStatement=""; 
        
        if (obj instanceof Integer){
           sqlStatement =  "SELECT * FROM customer WHERE customerId ="+
                   ((Integer) obj);
        }else if (obj instanceof String){
           sqlStatement = "SELECT * FROM customer WHERE customerName ='"+
                obj.toString()+"'";
        }else{
            System.out.println("Invalid argument passed to method!");
        }
                
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        Customer retrievedCustomer;
        
        while(queryResult.next()){
            
            int id = queryResult.getInt("customerId");
            String name = queryResult.getString("customerName");
            int addId = queryResult.getInt("addressId");
            boolean activeCust = queryResult.getBoolean("active");
            String createDate = queryResult.getString("createDate");
            String createdBy = queryResult.getString("createdBy");
            String lastUpdate = queryResult.getString("lastUpdate");
            String lastUpdateBy = queryResult.getString("lastUpdateBy");
            LocalDateTime createDateCust = stringToLDT(createDate);
            LocalDateTime lastUpdateCust = stringToLDT(lastUpdate);

            retrievedCustomer = new Customer(id, name, addId, activeCust,
                createDateCust, createdBy, lastUpdateCust, lastUpdateBy);
            
            return retrievedCustomer;
        }
        
        DBConnection.closeConnection();
        
        return null;
        
    }
    
    

    // SELECT all CUSTOMERs from database
    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception{
        
        DBConnection.makeConnection();
        String sqlStatement = "SELECT * FROM customer";
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        
        Customer retrievedCustomer;
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        
        while(queryResult.next()){
           
            int id = queryResult.getInt("customerId");
            String name = queryResult.getString("customerName");
            int addId = queryResult.getInt("addressId");
            boolean activeCust = queryResult.getBoolean("active");
            String createDate = queryResult.getString("createDate");
            String createdBy = queryResult.getString("createDate");
            String lastUpdate = queryResult.getString("lastUpdate");
            String lastUpdateBy = queryResult.getString("lastUpdateBy");
            LocalDateTime createDateCust = stringToLDT(createDate);
            LocalDateTime lastUpdateCust = stringToLDT(lastUpdate);

            retrievedCustomer = new Customer(id, name, addId, activeCust,
                createDateCust, createdBy, lastUpdateCust, lastUpdateBy);
            
            customerList.add(retrievedCustomer);
        }
        
        DBConnection.closeConnection();
        return customerList;
  
        
    }
    
    // DELETE CUSTOMER from database by customerId
    public static void deleteCustomer(int customerId) throws SQLException, Exception{
        
        DBConnection.makeConnection();
        String sqlStatement = "DELETE FROM customer WHERE customerId="+customerId;
        Query.makeQuery(sqlStatement);
        
       
        DBConnection.closeConnection();
  
        
    }
    
    // UPDATE CUSTOMER in database  
    public static void updateCustomer(Customer origCus, TextField nameTF, TextField addressTF,
            TextField address2TF, TextField cityTF, TextField countryTF, 
            TextField postalCodeTF, TextField phoneTF) throws SQLException, Exception{
        
        DBConnection.makeConnection();

         // get id  and name from old customer
        int origId = origCus.getCustomerId();
       
        
        // Set old customer name to "TEMP_NAME"
        // To easily find during modification process
        String sqlStatement1 = "UPDATE customer "
                            + "SET customerName='TEMP_NAME' "
                            + "WHERE customerId="+origId;
        Query.makeQuery(sqlStatement1);
        
        
        // Create new customer with modifications
        CustomerDAO.createCustomer(nameTF, addressTF, address2TF, cityTF, countryTF, postalCodeTF, phoneTF);
        
        // Get new customer from DB
        Customer newCus = CustomerDAO.getCustomer(nameTF.getText());
        
            // Re-open DB connection
            DBConnection.makeConnection();

        // Set new customer createDate, createdBy to original customer info
        String createDate = LDTToString(origCus.getCreateDate());
        String createdBy = origCus.getCreatedBy();
        String now = LDTToString(LocalDateTime.now());
        String sqlStatement2 = "UPDATE customer "
                            + "SET createDate ='"+createDate+"' " 
                            + ", createdBy ='"+createdBy+"' "
                            + ", lastUpdate='"+now+"' "
                            + "WHERE customerId="+newCus.getCustomerId();
        Query.makeQuery(sqlStatement2);
        
        // Update the customer's appointments to the correct customerId
        AppointmentDAO.updateCustomerAppointments(origCus.getCustomerId(), newCus.getCustomerId());
        
            // Re-open DB connection
            DBConnection.makeConnection();
        
        // Delete original customer 
        String sqlStatement3 = "DELETE FROM customer "+
                             "WHERE customerName='TEMP_NAME'";
        Query.makeQuery(sqlStatement3);
   
        
        
        DBConnection.closeConnection();
  
        
    }
    
    // Check is customer exists in database
    public static boolean exists(String name) throws Exception{
        
        ObservableList<Customer> customers = getAllCustomers();
        
        for(Customer c: customers){
            if(c.getCustomerName().equalsIgnoreCase(name)){
                System.out.println("The customer already exists!");
                return true;
            }
        }
        
        return false;
    }
    
    // Check if information entered for customer is valid and not empty
    public static boolean validInput(TextField nameTF, TextField addressTF,
       TextField cityTF, TextField countryTF, 
        TextField postalCodeTF, TextField phoneTF) throws SchedulingException{

        try{
            // Checks that no fields are left blank 
            if( nameTF.getText().isEmpty() 
                || addressTF.getText().isEmpty() 
                || cityTF.getText().isEmpty() 
                || countryTF.getText().isEmpty()
                || phoneTF.getText().isEmpty()
                || postalCodeTF.getText().isEmpty()){

                throw new SchedulingException("One or more fields are left blank!");
                
            }
            
        }catch(SchedulingException e){
            
            System.out.println(e.getMessage());
            return false;
        }
        
        try{
            // Checks that Name, City and Country contain only letters
            if(!isAlpha(nameTF.getText().replaceAll(" ", ""))
                ||   !isAlpha(cityTF.getText())
                || !isAlpha(countryTF.getText())){

                throw new SchedulingException("Name, City or Country should contain only letters!");


            }
            
        }catch(SchedulingException e){
            
            System.out.println(e.getMessage());
            return false;
        }
        
        int start = 0;
        int end = addressTF.getText().length() - 1;
        
        String firstChar = addressTF.getText().charAt(start)+"";
        String lastChar = addressTF.getText().charAt(end)+"";
        
        try{
            // Checks that address starts with a number and ends with a letter
            if(isAlpha(firstChar) || !isAlpha(lastChar) || !addressTF.getText().contains(" ")){

                 throw new SchedulingException("Address must begin with a number,"+
                         " contain a space and end with letter!");
            }
        
 
       }catch(SchedulingException e){
           
            System.out.println(e.getMessage());
            return false;
       }
       
        
        try{
            // Checks that postal code consists of 5 digits
            if(!postalCodeTF.getText().matches("^[0-9]*$") 
                    || postalCodeTF.getText().length() != 5){

                throw new SchedulingException("Postal Code must contain exactly 5 numbers!");

            }
            
        }catch(SchedulingException e){
            
            System.out.println(e.getMessage());
            return false;
        }
        
        
        
       try{ 
            if(!phoneTF.getText().matches("\\d{3}-\\d{4}")){
                  //System.out.println("Postal Code must be in the following format: '555-5555'");

                  //return false;
                  throw new SchedulingException("Postal Code must be in the following format: '555-5555'");
            }

       }catch(SchedulingException e){
           
              System.out.println(e.getMessage());
              return false;
       }
       
       
        return true;
    
    }
    
    public static boolean isAlpha(String input) {
    return input.matches("[a-zA-Z]+");
    }
}

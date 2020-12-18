/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;



import Model.CustomerTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Alex Veney
 */
public class CustomerTableDAO {


     // SELECT CUSTOMER details for table
     public static CustomerTable getCustomer(int customerId) throws SQLException, Exception{
        DBConnection.makeConnection();
        String sqlStatement = 
                "SELECT customerName, address, address2, city, country, postalCode, phone" 
                +" FROM customer INNER JOIN address INNER JOIN city INNER JOIN country"        
                +" WHERE customer.addressId = address.addressId AND address.cityId = city.cityId"
                +" AND city.countryId = country.countryId";
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        CustomerTable retrievedCustomer;
        
        while(queryResult.next()){
            String name = queryResult.getString("customerName");
            String address = queryResult.getString("address");
            String address2 = queryResult.getString("address2");
            String city = queryResult.getString("city");
            String country = queryResult.getString("country");
            String postalCode = queryResult.getString("postalCode");
            String phone = queryResult.getString("phone");

            String fullAddress = address + " " + address2 + " " + city + " " + 
                    country + " " + postalCode;
            retrievedCustomer = new CustomerTable(name,fullAddress, phone);
            
            return retrievedCustomer;
        }
        
        DBConnection.closeConnection();
        
        return null;
        
    }
     
     
     // SELECT multiple CUSTOMER details for table
     public static ObservableList<CustomerTable> getAllCustomers() throws SQLException, Exception{
        DBConnection.makeConnection();
        String sqlStatement = 
                "SELECT customerName, address, address2, city, country, postalCode, phone" 
                +" FROM customer INNER JOIN address INNER JOIN city INNER JOIN country"        
                +" WHERE customer.addressId = address.addressId AND address.cityId = city.cityId"
                +" AND city.countryId = country.countryId";
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        
        ObservableList<CustomerTable> customerList = FXCollections.observableArrayList();
        CustomerTable retrievedCustomer;
        
        while(queryResult.next()){
            String name = queryResult.getString("customerName");
            String address = queryResult.getString("address");
            String address2 = queryResult.getString("address2");
            String city = queryResult.getString("city");
            String country = queryResult.getString("country");
            String postalCode = queryResult.getString("postalCode");
            String phone = queryResult.getString("phone");

            String fullAddress = address + " " + address2 + " " + city + " " + 
                    country + " " + postalCode;
            retrievedCustomer = new CustomerTable(name,fullAddress, phone);
            customerList.add(retrievedCustomer);
            
        }
        
        
        DBConnection.closeConnection();
        return customerList;
      
        
    }
     
     
     
     
}

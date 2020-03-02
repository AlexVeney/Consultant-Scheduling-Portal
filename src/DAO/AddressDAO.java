/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Address;
import static Utilities.Time.stringToLDT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author avhomefolder
 */
public class AddressDAO {
    
    // SELECT ADDRESS from database based on addressId
    public static Address getAddress(int addressId) throws SQLException, Exception{

       DBConnection.makeConnection();
       
       String sqlStatement = "SELECT * FROM address WHERE addressId ="+addressId;
       
       Query.makeQuery(sqlStatement);
       
       Address retrievedAddress;
       
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           //int addressId = queryResult.getInt("addressId");
           String address = queryResult.getString("address");
           String address2 = queryResult.getString("address2");
           int cityId = queryResult.getInt("cityId");
           String postalCode = queryResult.getString("postalCode");
           String phone = queryResult.getString("phone");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           LocalDateTime createDateAddress = stringToLDT(createDate);
           LocalDateTime lastUpdateAddress = stringToLDT(lastUpdate);
           
           retrievedAddress = new Address(addressId, address, address2, cityId, postalCode, phone, createDateAddress, 
                   createdBy, lastUpdateAddress, lastUpdateBy);
           
           return retrievedAddress;
       }
       
       DBConnection.closeConnection();
       return null;
    }
    
    // SELECT ADDRESS from database with fully matching details
    public static Address getAddress(Address address) throws SQLException, Exception{
                   
       DBConnection.makeConnection();
       String sqlStatement = 
               "SELECT * FROM address WHERE address='"+address.getAddress()+"'"+
               " AND address2='"+address.getAddress2()+"'"+
               " AND cityId="+address.getCityId()+
               " AND postalCode='"+address.getPostalCode()+"'"+
               " AND phone='"+address.getPhone()+"'";
       Query.makeQuery(sqlStatement);
       Address retrievedAddress;
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           int addressId = queryResult.getInt("addressId");
           String address1 = queryResult.getString("address");
           String address2 = queryResult.getString("address2");
           int cityId = queryResult.getInt("cityId");
           String postalCode = queryResult.getString("postalCode");
           String phone = queryResult.getString("phone");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           LocalDateTime createDateAddress = stringToLDT(createDate);
           LocalDateTime lastUpdateAddress = stringToLDT(lastUpdate);
           
           retrievedAddress = new Address(addressId, address1, address2, cityId, postalCode, phone, createDateAddress, 
                   createdBy, lastUpdateAddress, lastUpdateBy);
           
           return retrievedAddress;
       }
       
       DBConnection.closeConnection();
       return null;
    }
    
    // INSERT ADDRESS into database
    public static void insertAddress(Address address) throws SQLException, Exception{
        DBConnection.makeConnection();
        
        String sqlStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy"+
                ", lastUpdate, lastUpdateBy) VALUES("+
                "'" + address.getAddress()+ "'" + "," +
                "'" + address.getAddress2()+ "'" + "," +
                address.getCityId()+", "+
                "'" + address.getPostalCode()+ "'" + "," +
                "'" + address.getPhone()+ "'" + ",'" +
                address.getCreateDate()+ "', " +
                "'" + address.getCreatedBy()+ "'" + ",'" +
                address.getLastUpdate()+ "', " +
                "'" + address.getLastUpdateBy()+ "')";
        Query.makeQuery(sqlStatement);
        
        DBConnection.closeConnection();
    }
   
}

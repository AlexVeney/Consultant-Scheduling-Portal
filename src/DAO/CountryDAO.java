/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Country;
import static Utilities.Time.LDTToString;
import static Utilities.Time.stringToLDT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author avhomefolder
 */
public class CountryDAO {
    
    
    // INSERT COUNTRY into database
    public static void insertCountry(Country newCountry) throws SQLException, Exception{
        
        DBConnection.makeConnection();
        
               String sqlStatement = "INSERT INTO country(country, createDate, createdBy"+
                ", lastUpdate, lastUpdateBy) VALUES("+
                "'" + newCountry.getCountry()+ "'" + ",'" +
                LDTToString(newCountry.getCreateDate())+ "', " +
                "'" + newCountry.getCreatedBy()+ "'" + ",'" +
                LDTToString(newCountry.getLastUpdate())+ "', " +
                "'" + newCountry.getLastUpdateBy()+ "')";

        
        Query.makeQuery(sqlStatement);
        
        DBConnection.closeConnection();
        
    }
    
    // SELECT COUNTRY from database
    public static Country getCountry(String countryName) throws SQLException, Exception{
       
       DBConnection.makeConnection();
       
       String sqlStatement = "SELECT * FROM country WHERE country ='"+countryName+"'";
       
       Query.makeQuery(sqlStatement);
       
       Country retrievedCountry;
       
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           int countryId = queryResult.getInt("countryId");
           String country = queryResult.getString("country");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           LocalDateTime createDateCountry = stringToLDT(createDate);
           LocalDateTime lastUpdateCountry = stringToLDT(lastUpdate);
           
           retrievedCountry = new Country(countryId, country, createDateCountry, 
                   createdBy, lastUpdateCountry, lastUpdateBy);
           
           return retrievedCountry;
       }
       
       DBConnection.closeConnection();
       
       return null;
    }
    
    // SELECT COUNTRY from database
    public static Country getCountry(int countryId) throws SQLException, Exception{
        
       DBConnection.makeConnection();
       
       String sqlStatement = "SELECT * FROM country WHERE countryId ="+countryId;
       
       Query.makeQuery(sqlStatement);
       
       Country retrievedCountry;
       
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           
           //int countryId = queryResult.getInt("countryId");
           String country = queryResult.getString("country");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           LocalDateTime createDateCountry = stringToLDT(createDate);
           LocalDateTime lastUpdateCountry = stringToLDT(lastUpdate);
           
           retrievedCountry = new Country(countryId, country, createDateCountry, 
                   createdBy, lastUpdateCountry, lastUpdateBy);
           
           return retrievedCountry;
       }
       
       DBConnection.closeConnection();
       
       return null;
    }
    
    
    // UPDATE method not needed
    public static void updateCountry(Country updatedCountry){
        
    }
    
    // DELETE method not needed
    public static void deleteCountry(int countryId){
        
    }
    
}

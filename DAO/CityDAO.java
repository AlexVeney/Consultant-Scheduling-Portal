/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.City;
import static Utilities.Time.LDTToString;
import static Utilities.Time.stringToLDT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author Alex Veney
 */
public class CityDAO {
    
    // INSERT CITY into database
    public static void insertCity(City city, int countryId) throws SQLException, Exception{
        
        DBConnection.makeConnection();
        
        String sqlStatement = "INSERT INTO city(city, countryId, createDate, createdBy"+
                ", lastUpdate, lastUpdateBy) VALUES("+
                "'" + city.getCity()+ "'" + "," +
                countryId+ ", '" +
                LDTToString(city.getCreateDate())+ "', " +
                "'" + city.getCreatedBy()+ "'" + ",'" +
                LDTToString(city.getLastUpdate())+ "', " +
                "'" + city.getLastUpdateBy()+ "')";
        Query.makeQuery(sqlStatement);
        
        
        DBConnection.closeConnection();
    }
    
    // SELECT CITY from database based on cityId or city (name)
    public static City getCity(Object obj) throws SQLException, Exception{
      
       DBConnection.makeConnection();
       
       String sqlStatement = "";
       
       if(obj instanceof Integer){
           sqlStatement = "SELECT * FROM city WHERE cityId ="+((Integer) obj);
       }else if (obj instanceof String){
           sqlStatement = "SELECT * FROM city WHERE city ='"+obj.toString()+"'";
       }
       
       
       Query.makeQuery(sqlStatement);
       
       City retrievedCity;
       
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           
           int cityId = queryResult.getInt("cityId");
           String city = queryResult.getString("city");
           int countryId = queryResult.getInt("countryId");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           LocalDateTime createDateCity = stringToLDT(createDate);
           LocalDateTime lastUpdateCity = stringToLDT(lastUpdate);
           
           retrievedCity = new City(cityId, city, countryId,createDateCity, 
                   createdBy, lastUpdateCity, lastUpdateBy);
           
           return retrievedCity;
       }
       
       DBConnection.closeConnection();
       
       return null;
       
       
    }
    
    public static ArrayList<String> getCities() throws SQLException, Exception{
       DBConnection.makeConnection();
       
       String sqlStatement = "SELECT city, country FROM city AS ci INNER JOIN country AS co "+
                             "where ci.countryId = co.countryId";
       
       Query.makeQuery(sqlStatement);

       ResultSet queryResult = Query.getResult();
       
       ArrayList<String> list = new ArrayList<>();
       
       while(queryResult.next()){
           
           String city = queryResult.getString("city");
           String country = queryResult.getString("country");
           list.add(city+", "+country);
       }
       
       DBConnection.closeConnection();
       
       return list;
    }
    
   
}

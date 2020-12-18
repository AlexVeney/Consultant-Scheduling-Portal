/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.User;
import static Utilities.Time.stringToLDT;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 *
 * @author Alex Veney
 */
public class UserDAO {
    
    // Select User from database
    public static User getUser(int id) throws SQLException, Exception{
       
       DBConnection.makeConnection();
       String sqlStatement = "SELECT * FROM user WHERE userId ="+id;
       Query.makeQuery(sqlStatement);
       User retrievedUser;
       ResultSet queryResult = Query.getResult();
       
       while(queryResult.next()){
           
           int userId = queryResult.getInt("userId");
           String userName = queryResult.getString("userName");
           Boolean active = queryResult.getBoolean("active");
           String createDate = queryResult.getString("createDate");
           String createdBy = queryResult.getString("createdBy");
           String lastUpdate = queryResult.getString("lastUpdate");
           String lastUpdateBy = queryResult.getString("lastUpdateBy");
           
           // LocalDateTime version of variables
           LocalDateTime createDateCountry = stringToLDT(createDate);
           LocalDateTime lastUpdateCountry = stringToLDT(lastUpdate);
           
           retrievedUser = new User(userId, userName, active, createDateCountry, 
                   createdBy, lastUpdateCountry, lastUpdateBy);
           
           return retrievedUser;
       }
       
       DBConnection.closeConnection();
       
       return null;
    }
    
    // Select all Users from database
    public static TreeMap<Integer,String> getUsers() throws SQLException, Exception{
       
       DBConnection.makeConnection();
       String sqlStatement = "SELECT * FROM user";
       Query.makeQuery(sqlStatement);
       
       ResultSet queryResult = Query.getResult();
       
       TreeMap<Integer,String> list = new TreeMap<>();
       
       while(queryResult.next()){
           int userId = queryResult.getInt("userId");
           String userName = queryResult.getString("userName");
           
           list.put(userId, userName);
       }
       
       DBConnection.closeConnection();
       
       return list;
    }
    
    
    
}

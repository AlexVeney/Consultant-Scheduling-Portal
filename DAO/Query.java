/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.Statement;
import static DAO.DBConnection.conn;
import java.sql.SQLException;

/**
 *
 * @author Alex Veney
 */
public class Query {
    
    private static String query;
    private static Statement stmt;
    private static ResultSet result;
    
    public static void makeQuery(String q){
        query = q;
        
        try{
            
            // Create Statement Object
            @SuppressWarnings("LocalVariableHidesMemberVariable")
            Statement stmt = conn.createStatement();
            
            // Determine query type for exceution
            if(q.toLowerCase().startsWith("select")){
                
                result = stmt.executeQuery(query);
            }
            if(q.toLowerCase().startsWith("insert") || 
                    q.toLowerCase().startsWith("delete") ||
                    q.toLowerCase().startsWith("update")){
                
                stmt.executeUpdate(query);
            }
            
            
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
   
    }
    
    public static ResultSet getResult(){
        
        return result;
    }
    
}

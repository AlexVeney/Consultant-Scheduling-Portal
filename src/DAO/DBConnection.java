/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author avhomefolder
 */
public class DBConnection {
    
    // Database connection details
    private static final String DATABASE_NAME = "U06bm0";
    private static final String DB_URL = "jdbc:mysql://3.227.166.251/" + DATABASE_NAME;
    private static final String USERNAME = "U06bm0";
    private static final String PASSWORD = "53688719552";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    
    public static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception{
       
       Class.forName(DRIVER);
       conn = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception{
        
        conn.close();

    }
    
}

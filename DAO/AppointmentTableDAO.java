/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import Model.AppointmentTable;
import static Utilities.Time.toTimeOfDay;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static Utilities.Time.utcToLocal;


/**
 *
 * @author Alex Veney
 */
public class AppointmentTableDAO {

    
     // SELECT APPOINTMENT details for table
     public static AppointmentTable getAppointment(int customerId) throws SQLException, Exception{
        DBConnection.makeConnection();
        
        String sqlStatement = 
                "SELECT start, type, customerName" +
                " FROM appointment INNER JOIN customer" +
                " WHERE appointment.customerId = customer.customerId";
        
        Query.makeQuery(sqlStatement);
        
        ResultSet queryResult = Query.getResult();
        
        AppointmentTable retrievedAppointment;
        
        while(queryResult.next()){
            
            String start = queryResult.getString("start");
            
            String type = queryResult.getString("type");
            String customerName = queryResult.getString("customerName");
           
            String startLocal = utcToLocal(start);
            retrievedAppointment = new AppointmentTable(startLocal,type, customerName);

            return retrievedAppointment;
        }
        
        DBConnection.closeConnection();
        
        return null;
        
    }
     
     
     // SELECT all APPOINTMENT details for table
     public static ObservableList<AppointmentTable> getAllAppointments() throws SQLException, Exception{
        
        DBConnection.makeConnection();
        
        String sqlStatement = 
                "SELECT start, type, customerName" +
                " FROM appointment INNER JOIN customer" +
                " WHERE appointment.customerId = customer.customerId order by start";
        
        Query.makeQuery(sqlStatement);
        
        ResultSet queryResult = Query.getResult();
        
        ObservableList<AppointmentTable> appointmentList = FXCollections.observableArrayList();
        
        AppointmentTable retrievedAppointment;
        
        while(queryResult.next()){
            String start = queryResult.getString("start");
            String type = queryResult.getString("type");
            String customerName = queryResult.getString("customerName");
            
            String timeOfDay = toTimeOfDay(utcToLocal(start));
            
           
            retrievedAppointment = new AppointmentTable(timeOfDay,type, customerName);
            appointmentList.add(retrievedAppointment);
 
        }

        DBConnection.closeConnection();
        return appointmentList;

    }


     
     
     
     
}

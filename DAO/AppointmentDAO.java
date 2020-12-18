/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Appointment;
import Utilities.SchedulingException;
import static Utilities.Time.LDTToString;
import static Utilities.Time.fromTimeOfDay;
import static Utilities.Time.stringToLDT;
import alexveneyschedulingapp.SchedulingApp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import static Utilities.Time.utcToLocal;
import static Utilities.Time.localToUTC;
import static Utilities.Time.toTimeOfDay;
import java.util.ArrayList;


/**
 *
 * @author Alex Veney
 */
public class AppointmentDAO {
    
    // SELECT APPOINTMENT from database based on appointmentId
    public static Appointment getAppointment(int apptId) throws SQLException, Exception{
        DBConnection.makeConnection();
        String sqlStatement = "SELECT * FROM appointment WHERE appointmentId ="+
                apptId;
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        Appointment retrievedAppointment;
        
        while(queryResult.next()){
            
            int aId = queryResult.getInt("appointmentId");
            int cId = queryResult.getInt("customerId");
            int uId = queryResult.getInt("userId");
            String title = queryResult.getString("title");
            String desc = queryResult.getString("description");
            String location = queryResult.getString("location");
            String contact = queryResult.getString("contact");
            String type = queryResult.getString("type");
            String url = queryResult.getString("url");
            String start = queryResult.getString("start");
            String end = queryResult.getString("end");
            String createDate = queryResult.getString("createDate");
            String createdBy = queryResult.getString("createDate");
            String lastUpdate = queryResult.getString("lastUpdate");
            String lastUpdateBy = queryResult.getString("lastUpdateBy");
            
            // Change from UTC to Local Time 
            LocalDateTime startAppt = stringToLDT(utcToLocal(start));
            LocalDateTime endAppt = stringToLDT(utcToLocal(end));
            LocalDateTime createDateAppt = stringToLDT(utcToLocal(createDate));
            LocalDateTime lastUpdateAppt = stringToLDT(utcToLocal(lastUpdate));

            retrievedAppointment = new Appointment(aId, cId, uId, title, desc, 
             location, contact, type, url, startAppt, endAppt, createDateAppt, 
             createdBy, lastUpdateAppt,lastUpdateBy);
            
            return retrievedAppointment;
        }
        
        DBConnection.closeConnection();
        
        return null;
        
    }
    
    // SELECT APPOINTMENT from database based on start, type and customerId
    public static Appointment getAppointment(String aStart, String aType, int cusId) throws SQLException, Exception{
        DBConnection.makeConnection();
        
        //Convert local start time to UTC before DB query 
        
        String startUTC = localToUTC(fromTimeOfDay(aStart));
        String sqlStatement = "SELECT * FROM appointment WHERE  customerId="+cusId+
                              " AND type='"+aType+"' AND start='"+startUTC+"'";
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        Appointment retrievedAppointment;
        
        while(queryResult.next()){
            
            int aId = queryResult.getInt("appointmentId");
            int cId = queryResult.getInt("customerId");
            int uId = queryResult.getInt("userId");
            String title = queryResult.getString("title");
            String desc = queryResult.getString("description");
            String location = queryResult.getString("location");
            String contact = queryResult.getString("contact");
            String type = queryResult.getString("type");
            String url = queryResult.getString("url");
            String start = queryResult.getString("start");
            String end = queryResult.getString("end");
            String createDate = queryResult.getString("createDate");
            String createdBy = queryResult.getString("createDate");
            String lastUpdate = queryResult.getString("lastUpdate");
            String lastUpdateBy = queryResult.getString("lastUpdateBy");
            
            // Change from UTC to Local Time 
            LocalDateTime startAppt = stringToLDT(utcToLocal(start));
            LocalDateTime endAppt = stringToLDT(utcToLocal(end));
            LocalDateTime createDateAppt = stringToLDT(utcToLocal(createDate));
            LocalDateTime lastUpdateAppt = stringToLDT(utcToLocal(lastUpdate));

            retrievedAppointment = new Appointment(aId, cId, uId, title, desc, 
             location, contact, type, url, startAppt, endAppt, createDateAppt, 
             createdBy, lastUpdateAppt,lastUpdateBy);
            
            return retrievedAppointment;
        }
        
        DBConnection.closeConnection();
        
        return null;
        
    }
    // SELECT APPOINTMENT from database based on appointmentId
    public static ArrayList<String> getAppointments(int userId) throws SQLException, Exception{
        DBConnection.makeConnection();
        String sqlStatement = "SELECT * FROM appointment WHERE userId="+userId+" order by start";
        Query.makeQuery(sqlStatement);
        ResultSet queryResult = Query.getResult();
        Appointment retrievedAppointment;
        
        ArrayList<String> list = new ArrayList<>();
        
        while(queryResult.next()){
           
            String type = queryResult.getString("type");
            String start = queryResult.getString("start");
            
            
            // Change from UTC to Local Time 
            String startStr = toTimeOfDay(utcToLocal(start));
        
            list.add(startStr+" "+type);
        }
        
       
        DBConnection.closeConnection();
        
        return list;
        
    }

     
    // INSERT APPOINTMENT into database
    public static void createAppointment(Appointment appt) throws SQLException, Exception{
        DBConnection.makeConnection();
        
        //Changes local time to UTC before entering into DB
        String timeUTC = localToUTC(LDTToString(appt.getStart()));
        String nowUTC = localToUTC(LDTToString(LocalDateTime.now()));
        String lastUpdateUTC = localToUTC(LDTToString(appt.getLastUpdate()));
        
        if(overlapCheck(timeUTC)){
            throw new SchedulingException("Appointment Date and Time cannot overlap!");
        }

       String sqlStatement = 
               "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end,createDate,createdBy, lastUpdate, lastUpdateBy)"+
               " VALUES(" + appt.getCustomerId()+ ","+ appt.getUserId()+", '"+ appt.getTitle()+
                        "', '"+ appt.getDescription()+"', '"+
                       appt.getLocation()+"', '"+
                       appt.getContact()+"', '"+
                       appt.getType()+"', '"+
                       appt.getUrl()+"', '"+
                       timeUTC+"', '"+
                       timeUTC+"', '"+
                       nowUTC+"', '"+
                       appt.getCreatedBy()+"', '"+
                       lastUpdateUTC+"', '"+
                       appt.getLastUpdateBy()+"')";
                       
           
       
       Query.makeQuery(sqlStatement);
      
       DBConnection.closeConnection();
    }
    
    // DELETE all appointments for a customer
    public static void deleteCustomerAppointments(int customerId) throws SQLException, Exception{
        DBConnection.makeConnection();

       String sqlStatement = 
               "DELETE FROM appointment WHERE customerId="+customerId;

       Query.makeQuery(sqlStatement);
      
       DBConnection.closeConnection();
    }
    
    
    
    
    //UPDATE customer appointments to reflect new customerId
    public static void updateCustomerAppointments(int origCusId, int newCusId) throws SQLException, Exception{
        DBConnection.makeConnection();

       String sqlStatement = 
               "UPDATE appointment SET customerId="+newCusId+" WHERE customerId="+origCusId;

       Query.makeQuery(sqlStatement);
      
       DBConnection.closeConnection();
    }
    
    //UPDATE appointments to reflect modifications
    public static void updateAppointment(Appointment origAppt, String date, 
            String hour, String minute, String amPm, String type, int newCusId, int newUserId) throws SQLException, Exception{
        
                DBConnection.makeConnection();
                
                // Set old appt time to "00-00-0000 00:00:00"
                // To easily find during modification process
                String sqlStatement1 = "UPDATE appointment "
                            + "SET start='1900-01-01 00:00:00' "
                            + "WHERE appointmentId="+origAppt.getAppointmentId();
                
                Query.makeQuery(sqlStatement1);
                
                // Create new appointment with modifications
                String time;
                
                if(Integer.parseInt(hour) != 12 &&amPm.equalsIgnoreCase("PM") ){
                    hour = String.valueOf(Integer.parseInt(hour)+12);
                }
                if(Integer.parseInt(hour) == 12 && amPm.equalsIgnoreCase("AM")){
                    hour = "0";
                }
                if(Integer.parseInt(hour) <= 9){
                    hour = "0"+hour;
                }
                
                time = hour+":"+minute+":00";
                
                
                LocalDateTime start = stringToLDT(date+" "+time);
                LocalDateTime end = start;
                
                
                System.out.println("Orig user: "+ UserDAO.getUser(origAppt.getUserId()).getUserName());
                System.out.println("Orig new: "+UserDAO.getUser(SchedulingApp.getUserId()).getUserName());
                String origUserName = UserDAO.getUser(origAppt.getUserId()).getUserName();
                String newUserName = UserDAO.getUser(SchedulingApp.getUserId()).getUserName();
                // Creates new/updated appointment with local time
                Appointment apptHolder = 
                        new Appointment(newCusId, newUserId, "", "", 
                        "", "", type, "",start, end, 
                         origAppt.getCreateDate(), origUserName, 
                         LocalDateTime.now(), newUserName);
               
                // Adds new/updated appointment to DB
                AppointmentDAO.createAppointment(apptHolder);
                
                
                // Re-open DB connection
                DBConnection.makeConnection();
                
                // delete original appointment
                String sqlStatement2 = "DELETE FROM appointment "+
                                     "WHERE start='1900-01-01 00:00:00'";
                Query.makeQuery(sqlStatement2);

    }
    
    
    // DELETE APPOINTMENT from database
    public static void deleteAppointment(int apptId) throws SQLException, Exception{
        
        DBConnection.makeConnection();
        String sqlStatement = "DELETE FROM appointment WHERE appointmentId="+apptId;
        Query.makeQuery(sqlStatement);
        
       
        DBConnection.closeConnection();
  
        
    }
    
    // Verifies whether appointment creation would cause overlap
    public static boolean overlapCheck(String utcDateTime) throws SQLException, Exception{

            DBConnection.makeConnection();
            String sql = "SELECT * FROM appointment";
            Query.makeQuery(sql);
            ResultSet queryResult = Query.getResult();
            
            while(queryResult.next()){
                

                String start = queryResult.getString("start");
                start = start.substring(0, 19);
                
                
                if(utcDateTime.equals(start)){
                    return true;
                }
            
            }
            

        return false;
        
        
    }
    
    
    public static String apptTimeCheck(String hour, String minute, String ampm){
        // Time

        try{
            String time;
            String hourStr;

            // Convert into 24 hour clock 
            if(ampm.equalsIgnoreCase("PM") && Integer.parseInt(hour) < 12){
                int hourInt = Integer.parseInt(hour);
                hourInt +=12;

                hourStr = String.valueOf(hourInt);
                
            }else if (ampm.equalsIgnoreCase("AM") && Integer.parseInt(hour) == 12){
                
               // 12 AM = 24:00:00 = 00:00:00
               hourStr = String.valueOf("0");
            }else{
                hourStr = hour;
            }
            
            
            if(Integer.parseInt(hourStr) >= 0 && Integer.parseInt(hourStr) <= 9){
                time = "0"+ hourStr +":"+minute+":00";
            }else{
                time = hourStr +":"+minute+":00";

            }


            
            
            if((time.compareTo("08:00:00") < 0) || (time.compareTo("17:00:00") > 0)){

                throw new SchedulingException("Appointment must be scheduled between 8:00 AM - 5:00 PM");
                
            }
            
            return time;
            
        }catch(SchedulingException ex){
            
            System.out.println("Invalid Appointment time!");
            return null;
        }
        
    }
    
}

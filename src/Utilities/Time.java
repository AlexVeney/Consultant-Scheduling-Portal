/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author avhomefolder
 */
public class Time {

    
    // Converts string into LocalDateTime object
    public static LocalDateTime stringToLDT(String str){
        
        if(str.length() > 19){
            str = str.substring(0, 19);
        }
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(str, format);

        return ldt;
    }
    
    // Converts LocalDateTime object into string
    public static String LDTToString(LocalDateTime ldt){
        
       
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatter = format.format(ldt);
        
        return formatter;
    }

    // Converts Date with 24-Hr time into MM/dd/yyyy AM/PM format date 
        // Used for appt table display
    public static String toTimeOfDay(String time) throws ParseException{
 

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa",Locale.getDefault());


        String inputText = time;


        Date date = inputFormat.parse(inputText);
        String outputText = outputFormat.format(date);

        return outputText;
    
    }
    
    //Converts Date with MM/dd/yyyy AM/PM  format into yyyy-MM-dd and 24-Hr format
        // Used to extract time from appt table display
    public static String fromTimeOfDay(String time) throws ParseException{
 

        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault());
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());


        String inputText = time;


        Date date = inputFormat.parse(inputText);
        String outputText = outputFormat.format(date);

        return outputText;
    
    }
    


    public static String utcToLocal(String utc){
        
        String pattern = "yyyy-MM-dd HH:mm:ss";
        
        DateFormat sdf = new SimpleDateFormat(pattern);
        
        try {
            Date date;
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = sdf.parse(utc);
            ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            return zdt.format(DateTimeFormatter.ofPattern(pattern));
        
        } catch (ParseException ex) {
            System.out.println("Something went wrong converting from UTC to Local Time!");
            Logger.getLogger(Time.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            
        
        
    }
    
    public static String localToUTC(String local){
        
        
        String pattern = "yyyy-MM-dd HH:mm:ss";

        DateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(local);
            ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
            
            return zdt.format(DateTimeFormatter.ofPattern(pattern));
        } catch (ParseException ex) {
            System.out.println("Something went wrong converting from Local Time to UTC!");
            Logger.getLogger(Time.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
        
        
    }


   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.AppointmentTableDAO;
import DAO.DBConnection;
import DAO.Query;
import DAO.UserDAO;
import Model.AppointmentTable;
import static Utilities.Time.LDTToString;
import static Utilities.Time.fromTimeOfDay;
import static Utilities.Time.stringToLDT;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import alexveneyschedulingapp.SchedulingApp;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;


/**
 * FXML Controller class
 *
 * @author Alex Veney
 */
public class LoginScreenController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button submitButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label appLabel;
        

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        rb = ResourceBundle.getBundle("alexveneyschedulingapp/Nat", Locale.getDefault());
        changeScreenLanguage(rb);
        
  
        
    }  
    
    private void changeScreenLanguage(ResourceBundle rb){
        
        if(Locale.getDefault().getLanguage().equals("en") ||
            Locale.getDefault().getLanguage().equals("es") ){
            appLabel.setText(rb.getString("Scheduling") + " " + rb.getString("System"));
            usernameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));

            submitButton.setText(rb.getString("Submit"));
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Language not supported");
            alert.showAndWait();
            System.exit(0);
        }
    }
    
    // Verifies whether the entered credentials are correct
    private boolean checkCredentials( TextField u, TextField p) throws SQLException, IOException{
        

        // Get text entered at login screen
                String usernameEntry = u.getText();
                String passwordEntry = p.getText();
        
        try {
            Statement stmt = DBConnection.conn.createStatement();
            String sql = "SELECT * FROM user";
            Query.makeQuery(sql);
            ResultSet result = Query.getResult();
            
            while(result.next()){
            
                if(usernameEntry.equals(result.getString("userName")) &&
                       passwordEntry.equals(result.getString("password"))){
                    SchedulingApp.setUserId(result.getInt("userId"));
                    System.out.println(SchedulingApp.getUserId()+"and  "+ UserDAO.getUser(SchedulingApp.getUserId()).getUserName());
                    
                    appendToLoginFile(UserDAO.getUser(SchedulingApp.getUserId()).getUserName());
                    
                    return true;
                }
            
            }
            
            throw new Exception("Wrong credentials entered");
           
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

 
        return false;
    }

    
    @FXML
    private void signInOnAction(ActionEvent event) throws IOException, SQLException {

   
    if(checkCredentials(usernameTextField, passwordTextField)){
       
        // Transition to main application screen
        SchedulingApp action = new SchedulingApp();
        action.changePage(event, "/View/MainScreen.fxml");
        
        try {
            
            generateAlert();
        
        } catch (Exception ex) {
            Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
    }else{

       
        if(Locale.getDefault().getLanguage().equals("es") ||
           Locale.getDefault().getLanguage().equals("en") ){

            SchedulingApp.alert("Error","Incorrect credentials entered", Alert.AlertType.ERROR);
            
        }
        
    }

    }
   

    private static void  generateAlert() throws Exception{
        ObservableList<AppointmentTable> apptList = AppointmentTableDAO.getAllAppointments();
        
        LocalDateTime now = LocalDateTime.now().minusMinutes(1); // Minus 1 minute to provide alert for apppointment of that exact time
        
        // LAMBDA EXPRESSION: used to cycle through list of appointments with the use
        //                    of fewer lines of code which promotes readability
        apptList.stream().forEach((AppointmentTable) -> {

            try {
                LocalDateTime apptStart = stringToLDT(fromTimeOfDay(AppointmentTable.getStart()));
                
                alertInfo(now, apptStart, AppointmentTable.getType(), 
                        AppointmentTable.getCustomerName(), AppointmentTable.getStart());
            } catch (ParseException ex) {
                Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }
    
    
    private static void alertInfo(LocalDateTime now, LocalDateTime start, String type, String name, String time){
       
            LocalDateTime nowPlus15 = now.plusMinutes(15);
            
               
                if(start.isEqual(now) || start.isEqual(nowPlus15) || 
                        (start.isAfter(now) && start.isBefore(nowPlus15))){
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointment");
                    alert.setContentText(
                            "You have an upcoming "+type+
                            " appointment with " + name +
                            " at " + time);
                    alert.showAndWait();
                }
           
    }
    
    private static void appendToLoginFile(String username) throws IOException{
        
        
        // Create File object for reading
        
        File file = new File("src/Utilities/LoginFile.txt");
      
        // Check if file exists
        if(!file.exists()){
            System.out.println("File not found");
            System.exit(0);
        }
        
        LocalDateTime ldt = LocalDateTime.now();        
        String textToAppend = LDTToString(ldt)+ " " + username;
        
        FileWriter fileWriter = new FileWriter(file.getPath(), true); //Set true for append mode
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(textToAppend); 
            printWriter.close();
        } 
        
        
    }
    }


   
    


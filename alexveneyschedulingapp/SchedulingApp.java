/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alexveneyschedulingapp;

import DAO.DBConnection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Alex Veney
 */
public class SchedulingApp extends Application {
    
    Stage stage;
    Parent scene;
    private static int userId;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException, Exception {
       DBConnection.makeConnection();
       launch(args);
       DBConnection.closeConnection();
        
        
    }
    
    public void changePage(ActionEvent event, String fxmlpath) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        scene = FXMLLoader.load(getClass().getResource(fxmlpath));
        stage.setScene(new Scene(scene));
        stage.show();

    }
    
    public static void alert(String title, String message, AlertType alertType){
        
        ResourceBundle rb = ResourceBundle.getBundle("alexveneyschedulingapp/Nat", Locale.getDefault());
        
        String titleArray[] = title.split(" ");
        String messageArray[] = message.split(" ");
        
        String translatedTitle ="";
        String translatedMessage = "";
        
        for(String word: titleArray){
            
            translatedTitle += rb.getString(word) + " ";
        }
        
        for(String word: messageArray){
            
            translatedMessage += rb.getString(word) + " ";
        }
      
        Alert alert = new Alert(alertType);
        alert.setTitle(translatedTitle);
        alert.setContentText(translatedMessage);
        alert.showAndWait();
    }
    
 
    
    public static boolean isInt(TextField text){

        if (text.getText().matches("^[0-9]*$")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void setUserId(int number){
        userId=number;
    }
    
    public static int getUserId(){
        return userId;
    }
}

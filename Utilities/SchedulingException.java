/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import javafx.scene.control.Alert;

/**
 *
 * @author Alexandra Veney
 */
public class SchedulingException extends Exception{
    
    public SchedulingException(String message){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setContentText(message);
            alert.showAndWait();
        
    }
    

    
}

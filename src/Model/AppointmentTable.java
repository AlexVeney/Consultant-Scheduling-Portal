/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author avhomefolder
 */
public class AppointmentTable {
    
    String start;
    String type;
    String customerName;
    
 

    public AppointmentTable(String start, String type, String customerName) {
        this.start = start;
        this.type = type;
        this.customerName = customerName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    
    
    
}

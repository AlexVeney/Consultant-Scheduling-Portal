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
public class CustomerTable {
    
    String customerName;
    String fullAddress;
    String phone;

    public CustomerTable(String customerName, String fullAddress, String phone) {
        this.customerName = customerName;
        this.fullAddress = fullAddress;
        this.phone = phone;
    }

 

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    

   
    
    
    
}

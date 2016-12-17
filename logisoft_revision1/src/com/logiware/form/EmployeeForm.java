/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.form;

import com.gp.cong.hibernate.Address;
import com.gp.cong.hibernate.Email;
import com.gp.cong.hibernate.Employee;
import com.gp.cong.hibernate.Phone;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author omi
 */
public class EmployeeForm extends ActionForm { 

   private Employee employee; 
   private Address address; 
   private Email email;
   private Phone phone;
   
    public EmployeeForm(){
        this.employee=new Employee();
        this.address=new Address();
        this.email=new Email();
        this.phone=new Phone();
   }   

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    

    public void setAddress(Address address) {
        this.address = address;
    }
   
  
    public Long getId() {
        return employee.getId();
    }
    public void setId(Long id) {
        this.employee.setId(id);
    }

    public String getName() {
        return employee.getName();
    }

    public void setName(String name) {
        this.employee.setName(name);
    }

    public String getLastName() {
        return employee.getLastName();
    }

    public void setLastName(String lastName) {
        this.employee.setLastName(lastName);
    }
    public String getCountry() {
        return address.getCountry();
    }
     public void setCountry(String country) {
        this.address.setCountry(country);
    }

    public String getZipcode() {
        return address.getZipcode();
    }

    public void setZipcode(String zipcode) {
        this.address.setZipcode(zipcode);
    }    
     

    public void setMail(String mail) {
        this.email.setMail(mail);
    }
   
    public String getMail() {
        return email.getMail();
    }
   public String getPhoneNumber() {
        return phone.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone.setPhoneNumber(phoneNumber);
    }
       
     
    public List<Address> getAddressList() {
        return employee.getAddressList();
    }

    public void setAddressList(List<Address> addressList) {
        this.employee.setAddressList(addressList);
    }
     public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }  
    public void addAddress(Address address){
     this.employee.addressAdd(address);
    }
    
    public void resetValue(){
        this.getAddressList().clear();
        
    }
}

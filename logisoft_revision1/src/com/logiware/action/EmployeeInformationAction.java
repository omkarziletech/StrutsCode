/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.action;

import com.gp.cong.hibernate.Address;
import com.gp.cong.hibernate.Email;
import com.gp.cong.hibernate.Employee;
import com.gp.cong.hibernate.Phone;
import com.logiware.employee.dao.AddressDAO;
import com.logiware.employee.dao.EmailDAO;
import com.logiware.employee.dao.EmployeeDAO;
import com.logiware.employee.dao.PhoneDAO;
import com.logiware.form.EmployeeForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author omi
 */
public class EmployeeInformationAction extends DispatchAction {
    
      public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
           
            EmployeeForm employeeForm=(EmployeeForm)form;
            EmployeeDAO employeeDAO=new EmployeeDAO();           
            
            Employee employee=employeeForm.getEmployee();
            int count=0;
            Address address=null;
            for(Address addr:employeeForm.getAddressList()){
                
                address=new Address();               
                address.setEmployee(employee);     
                count++;
             }
             
             employee.getAddressList().add(address);
             employeeDAO.save(employee);
             System.out.println(count);
            
             employeeForm.resetValue();   
            
            
            
            
            
            
//            Address address  =employeeForm.getAddress();
//            address.setEmployee(employee);
//            
//            Email email=employeeForm.getEmail();
//            email.setAddress(address);
//            address.getEmailList().add(email);
//            
//            
//            employee.getAddressList().add(address);
//            employeeDAO.save(employee);
//            employeeForm.resetValue();         
           return mapping.findForward("success");
      }
      public ActionForward addRow(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
            EmployeeForm employeeForm=(EmployeeForm)form;
            Employee employee=new Employee();
           employeeForm.getEmployee().getAddressList().add(new Address());
           
            
            System.out.println("new row");
                    
           
             return mapping.findForward("success");
      }
       public ActionForward removeRow(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
            EmployeeForm employeeForm=(EmployeeForm)form;
           
            employeeForm.getAddressList().remove(new Address());            
            System.out.println("remove row");
            
            return mapping.findForward("success");
      }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.employee.dao;

import com.gp.cong.hibernate.Employee;
import com.gp.cong.hibernate.BaseHibernateDAO;

/** *
 * @author omi
 */
public class EmployeeDAO extends BaseHibernateDAO<Employee> { 
    
       public EmployeeDAO() {
        super(Employee.class);
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.employee.dao;

import com.gp.cong.hibernate.BaseHibernateDAO; 
import com.gp.cong.hibernate.Phone;


/** *
 * @author omi
 * */
 
public class PhoneDAO extends BaseHibernateDAO<Phone> { 
    
      public PhoneDAO() {
        super(Phone.class);
    }

    
}

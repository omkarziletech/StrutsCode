/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.CustomerContact;

/**
 *
 * @author Administrator
 */
public class LclContactDetailsDAO extends BaseHibernateDAO<CustomerContact> {

    public LclContactDetailsDAO() {
        super(CustomerContact.class);
    }

    public void delete(Integer id) throws Exception {
        getSession().delete(findById(id));
        getSession().flush();
    }
}

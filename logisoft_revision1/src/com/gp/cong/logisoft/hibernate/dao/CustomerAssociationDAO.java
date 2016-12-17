package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.Customer;

public class CustomerAssociationDAO extends BaseHibernateDAO {
    public List findAccountNumber(Customer accountNo) throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerAssociation where associd=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            list = queryObject.list();
        return list;
    }
}

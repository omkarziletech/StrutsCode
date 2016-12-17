/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;


public class DispositionDAO extends BaseHibernateDAO<Disposition> {

    private static final Log log = LogFactory.getLog(Disposition.class);

    public DispositionDAO() {
        super(Disposition.class);
    }

    public Disposition findByEliteCode(String eliteCode) throws Exception {
        Query query = getCurrentSession().createQuery("SELECT d FROM Disposition d WHERE d.eliteCode = :eliteCode").setString("eliteCode",eliteCode);
        return (Disposition) query.uniqueResult();
    }
}

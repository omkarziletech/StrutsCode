package com.logiware.edi.tracking;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;

public class EdiAckSystemDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(EdiAckSystemDAO.class);

    public void save(EdiAckSystem ediAck) throws Exception {
        getSession().save(ediAck);
        getSession().flush();
    }

    public EdiAckSystem findById(java.lang.String id)throws Exception {
            EdiAckSystem instance = (EdiAckSystem) getSession().get("com.logiware.edi.tracking.EdiAckSystem", Integer.parseInt(id));
            return instance;
    }
}

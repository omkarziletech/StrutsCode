/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImportAms;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Logiware
 */
public class LclQuoteImportAmsDAO extends BaseHibernateDAO<LclQuoteImportAms> {

    public LclQuoteImportAmsDAO() {
        super(LclQuoteImportAms.class);
    }

    /*** Get very first ams no. ***/
    public LclQuoteImportAms getQuoteAms(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclQuoteImportAms where lclFileNumber.id = :fileId order by enteredDatetime asc");
        query.setMaxResults(1);
        query.setLong("fileId", fileId);
        return (LclQuoteImportAms) query.uniqueResult();
    }

    /*** Find all with order by entered date ***/
    public List<LclQuoteImportAms> findAll(Long fileId) throws Exception {
        Query query = getCurrentSession().createQuery("from LclQuoteImportAms where lclFileNumber.id = :fileId order by enteredDatetime asc");
        query.setLong("fileId", fileId);
        return query.list();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import org.hibernate.SQLQuery;

/**
 *
 * @author Thamizh
 */
public class LclQuotePieceDAO extends BaseHibernateDAO<LclQuotePiece> {

    public LclQuotePieceDAO() {
        super(LclQuotePiece.class);
    }

    public Domain saveAndReturn(Domain instance) throws Exception {
        getCurrentSession().saveOrUpdate(instance);
        getCurrentSession().flush();
        getCurrentSession().clear();
        return (LclQuotePiece) (getCurrentSession().createQuery("FROM LclQuotePiece ORDER by id DESC").setMaxResults(1)).uniqueResult();
    }

    public String hasQuotePiece(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM lcl_quote_piece");
        queryBuilder.append(" WHERE file_number_id = ").append(fileId);
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }
    
     public void deleteNotesForCommodity(Long fileId, Integer userId) throws Exception {
        String query = "call delete_notes_for_commodity(:fileId,:userId)";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("userId", userId);
        queryObject.executeUpdate();
    }
}

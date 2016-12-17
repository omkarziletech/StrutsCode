/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHazmat;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author Administrator
 */
public class LclQuoteHazmatDAO extends BaseHibernateDAO<LclQuoteHazmat> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclQuoteHazmatDAO() {
        super(LclQuoteHazmat.class);
    }

    public LclQuoteHazmat findByFileAndCommodity(Long fileNumberId, Long quotePieceId) throws Exception {
        String queryString = "from LclQuoteHazmat where lclFileNumber='" + fileNumberId + "' and lclQuotePiece='" + quotePieceId + "'";
        Query query = getSession().createQuery(queryString);
        return (LclQuoteHazmat) query.setMaxResults(1).uniqueResult();
    }

    public List<LclQuoteHazmat> findByFileAndCommodityList(Long fileNumberId, Long bookingPieceId) throws Exception {
        String queryString = "from LclQuoteHazmat where lclFileNumber='" + fileNumberId + "' and lclQuotePiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        return query.list();
    }

    public List<GenericCode> getAllPackageComposition() throws Exception {
        String queryString = "from GenericCode where codetypeid=50 order by codedesc";
        return getSession().createQuery(queryString).list();
    }

    public String isHazmat(String fileId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT IF(COUNT(*)>0 ,'true','false') FROM lcl_quote_hazmat ");
        queryStr.append(" WHERE quote_piece_id IN(SELECT id FROM lcl_quote_piece ");
        queryStr.append("WHERE file_number_id=:fileId AND hazmat=TRUE)");
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        return (String) query.uniqueResult();
    }
}

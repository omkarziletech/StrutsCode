/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHsCode;
import java.util.List;
import org.hibernate.SQLQuery;

/**
 *
 * @author Thamizh
 */
public class LclQuoteHsCodeDAO extends BaseHibernateDAO<LclQuoteHsCode> {

    public LclQuoteHsCodeDAO() {
        super(LclQuoteHsCode.class);
    }

    public List<LclQuoteHsCode> getHsCodeList(Long fileId) throws Exception {
        String queryString = "from LclQuoteHsCode where lclFileNumber.id= " + fileId;
        return (List<LclQuoteHsCode>) getSession().createQuery(queryString).list();
    }

    public List<LclQuoteHsCode> getHsCodeByList(Long fileId) throws Exception {
        String queryString = "from LclQuoteHsCode where lclFileNumber.id= " + fileId + " and (noPieces is null or weightMetric is null)";
        return (List<LclQuoteHsCode>) getSession().createQuery(queryString).list();
    }

    public String isCheckedQteHsCode(Long fileId) throws Exception {
        String query = "SELECT IF(COUNT(*)>0,'true','false') FROM lcl_quote_hs_code WHERE file_number_id=" + fileId;
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String hsCodeAlreadyExist(String fileNumberId, String hsCode, String bookingHsCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') from lcl_quote_hs_code ");
        queryBuilder.append(" where file_number_id = ").append(Long.parseLong(fileNumberId)).append(" ");
        queryBuilder.append(" and (codes = '").append(hsCode).append("' or codes = '").append(bookingHsCode).append("')");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }
        public String validateQuoteSubHouseBl(String SubHouseBl, String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(t.result <> '', result, 'available') as result ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    if(");
        queryBuilder.append("      count(*) > 0,");
        queryBuilder.append("      concat(");
        queryBuilder.append("        'Quote - ',");
        queryBuilder.append("        (select fn.`file_number` from `lcl_file_number` fn where fn.`id` = lcl.`file_number_id`),");
        queryBuilder.append("        ' already has this Sub-HouseBL - ',");
        queryBuilder.append("        UPPER(lcl.`sub_house_bl`),");
        queryBuilder.append("        '. Please enter another one.'");
        queryBuilder.append("      ),");
        queryBuilder.append("      'available'");
        queryBuilder.append("    ) as result ");
        queryBuilder.append("  from");
        queryBuilder.append("    `lcl_quote_import` lcl ");
        queryBuilder.append("  where lcl.`sub_house_bl`  = :SubHouseBl ");
        queryBuilder.append("    and lcl.`file_number_id` <> :fileNumberId");
        queryBuilder.append("  ) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("SubHouseBl", SubHouseBl);
        query.setString("fileNumberId", fileNumberId);
        return (String) query.uniqueResult();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Owner
 */
public class LCLContactDAO extends BaseHibernateDAO<LclContact> {

    public LCLContactDAO() {
        super(LclContact.class);
    }

//    public TradingContactForm getTradingAddressDetails(String queryString)throws Exception{
//        TradingContactForm tcf = null;
//            Query query = getCurrentSession().createSQLQuery(queryString);
//            List list = query.list();
//            if (!list.isEmpty()) {
//                Object row = list.get(0);
//                tcf = new TradingContactForm((Object[]) row);
//            }
//        return tcf;
//    }
    public LclContact getContact(Long fileId, String remarks) throws Exception {
        Query query = getCurrentSession().createQuery("from LclContact where lclFileNumber.id = :fileId and remarks = :remarks order by id").setString("remarks", remarks).setLong("fileId", fileId).setMaxResults(1);
        return (LclContact) query.setMaxResults(1).uniqueResult();
    }

    public LclContact getLclContactsByFileId(String fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclContact.class, "LclContact");
        criteria.createAlias("LclContact.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", new Long(fileId)));
        }
        return (LclContact) criteria.uniqueResult();
    }

    public void updateCompanyName(Long contactId, Long fileId, String acctName, String acctNo, String columnName) throws Exception {
        StringBuilder queryBuilder1 = new StringBuilder();
        queryBuilder1.append("update lcl_booking set ").append(columnName).append(" = '");
        queryBuilder1.append(acctNo);
        queryBuilder1.append("'");
        queryBuilder1.append(" where file_number_id = ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder1.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_contact set company_name = '");
        queryBuilder.append(acctName);
        queryBuilder.append("'");
        queryBuilder.append(" where file_number_id = ");
        queryBuilder.append(fileId).append(" AND id=").append(contactId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }
        public void deleteByFileNumberIdAndContactType(Long fileId , List<String> contactType) throws Exception {
        String queryString = "delete from lcl_contact where file_number_id =:fileId and remarks IN(:contactType)";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameterList("contactType", contactType);
        queryObject.executeUpdate();
    }
}

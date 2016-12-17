package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.util.CommonFunctions;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

public class CustomerContactDAO extends BaseHibernateDAO {

    public List findAccountNumberWithOutContact(String accountNo, String contactName) throws Exception {
        List list = new ArrayList();
        String queryString = "from CustomerContact where accountNo like ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", accountNo + "%");
        list = queryObject.list();
        return list;
    }

    public List findAccountNumberWithOutContact1(String accountNo, String contactName) throws Exception {
        List list = new ArrayList();
        String queryString = "from CustomerContact where firstName like ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", contactName + "%");
        list = queryObject.list();
        return list;
    }

    public List findAccountNumber(String accountNo, String contactName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CustomerContact.class);
        if (accountNo != null && !accountNo.equals("")) {
            criteria.add(Restrictions.like("accountNo", accountNo.trim() + "%"));
        }
        if (CommonFunctions.isNotNull(contactName)) {
            criteria.add(Restrictions.like("firstName", contactName.trim() + "%"));
        }
        return criteria.list();

    }

    public List findByAccountNumber(String accountNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CustomerContact.class);
        if (accountNo != null && !accountNo.equals("")) {
            criteria.add(Restrictions.like("accountNo", accountNo.trim() + "%"));
        }
        return criteria.list();
    }

    public List findContactsByAcctNo(String accountNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from CustomerContact where accountNo like ?0 order by id";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", accountNo + "%");
        list = queryObject.list();
        return list;
    }

    public void delete(CustomerContact persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public CustomerContact findById(Integer userid) throws Exception {
        CustomerContact instance = (CustomerContact) getSession().get("com.gp.cong.logisoft.domain.CustomerContact", userid);
        return instance;
    }

    public void update(CustomerContact customerContact) throws Exception {
        getSession().update(customerContact);
        getSession().flush();
    }

    public void saveOrUpdate(CustomerContact customerContact) throws Exception {
        getCurrentSession().saveOrUpdate(customerContact);
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void copyCustomerContactFromDisabledToFwdAcc(String disabledAccount, String forwardedAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update cust_contact a, cust_contact b");
        queryBuilder.append(" set b.code_a = if(b.code_a != 0, b.code_a, a.code_a),");
        queryBuilder.append(" b.code_b = if(b.code_b != 0, b.code_b, a.code_b),");
        queryBuilder.append(" b.code_c = if(b.code_c != 0, b.code_c, a.code_c),");
        queryBuilder.append(" b.code_d = if(b.code_d != 0, b.code_d, a.code_d),");
        queryBuilder.append(" b.code_e = if(b.code_e != 0, b.code_e, a.code_e),");
        queryBuilder.append(" b.code_f = if(b.code_f != 0, b.code_f, a.code_f),");
        queryBuilder.append(" b.code_g = if(b.code_g != 0, b.code_g, a.code_g),");
        queryBuilder.append(" b.code_h = if(b.code_h != 0, b.code_h, a.code_h),");
        queryBuilder.append(" b.code_i = if(b.code_i != 0, b.code_i, a.code_i),");
        queryBuilder.append(" b.position = if(b.position != '', b.position, a.position),");
        queryBuilder.append(" b.phone = if(b.phone != '', b.phone, a.phone),");
        queryBuilder.append(" b.extension = if(b.extension != '', b.extension, a.extension),");
        queryBuilder.append(" b.fax = if(b.fax != '', b.fax, a.fax),");
        queryBuilder.append(" b.comment = if(b.comment != '', b.comment, a.comment)");
        queryBuilder.append(" where a.acct_no = '").append(disabledAccount).append("'");
        queryBuilder.append(" and b.acct_no = '").append(forwardedAccount).append("'");
        queryBuilder.append(" and concat(a.first_name, a.last_name, a.email) = concat(b.first_name, b.last_name, b.email)");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder();
        queryBuilder.append("insert into cust_contact");
        queryBuilder.append("(acct_no,first_name,last_name,position,phone,fax,email,comment,code_a,code_b,code_c,code_d,code_e,code_f,code_g,code_h,code_i,extension)");
        queryBuilder.append("(select '").append(forwardedAccount).append("' as acct_no,first_name,last_name,position,phone,fax,email,comment,code_a,code_b,code_c,code_d,code_e,code_f,code_g,code_h,code_i,extension ");
        queryBuilder.append("from cust_contact where acct_no='").append(disabledAccount).append("'");
        queryBuilder.append("and concat(first_name,last_name,email) not in (select concat(first_name,last_name,email)");
        queryBuilder.append("from cust_contact where acct_no='").append(forwardedAccount).append("'))");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<Object> getAllEMailIdsByCodeJE1E3(String acctNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT cc.email FROM cust_contact cc  JOIN genericcode_dup gen_dup ON cc.code_j = gen_dup.id WHERE cc.acct_no = '").append(acctNo).append("' AND (gen_dup.code = '").append(TradingPartnerConstants.E1).append("' OR gen_dup.code = '").append(TradingPartnerConstants.E3).append("')");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public List<CustomerContact> findByAcctNo(String accountNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CustomerContact.class);
        criteria.add(Restrictions.eq("accountNo", accountNo));
        criteria.addOrder(Order.asc("firstName"));
        return criteria.list();
    }

    public List<ImportsManifestBean> getAllEMailIdsByCodeF(String acctNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT cc.id as invoiceNo, cc.acct_no as agentNo,cc.first_name as agentName,cc.email as email,");
        qBuilder.append(" cc.fax as consigneeFax,ge.code as chargeCode FROM cust_contact cc JOIN genericcode_dup ge ON cc.code_f = ge.id  ");
        qBuilder.append(" WHERE cc.acct_no IN(").append(acctNo).append(") AND (");
        qBuilder.append("(ge.code IN ('E1', 'E2', 'E3') AND cc.email != '') OR ");
        qBuilder.append("  (ge.code IN ('F1', 'F2', 'F3')AND cc.fax != ''))");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("consigneeFax", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        return query.list();
    }

    public List<ImportsManifestBean> getAlLEmailIdsOfCodeFA1(String acctNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT cc.id as invoiceNo, cc.acct_no as agentNo,cc.first_name as agentName,cc.email as email,cc.fax as consigneeFax,");
        qBuilder.append("ge.code as chargeCode FROM cust_contact cc LEFT JOIN genericcode_dup ge ON cc.code_f = ge.id  ");
        qBuilder.append(" WHERE cc.acct_no ='").append(acctNo).append("' AND ge.code='A1' AND ");
        qBuilder.append(" ((cc.email IS NOT NULL AND cc.email!='') OR (cc.fax IS NOT NULL AND cc.fax!=''))");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("consigneeFax", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        return query.list();
    }

    public List<CustomerContact> checkA1CodeF(String accountNo) throws Exception {
        String queryString = "from CustomerContact cc LEFT JOIN fetch cc.codef gc where cc.accountNo='" + accountNo + "' and gc.code='A1'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<ImportsManifestBean> getAlLEmailIdsOfExportCodeJA1(String acctNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT cc.id as invoiceNo, cc.acct_no as agentNo,cc.first_name as agentName,cc.email as email,cc.fax as consigneeFax,");
        qBuilder.append("ge.code as chargeCode FROM cust_contact cc LEFT JOIN genericcode_dup ge ON cc.code_j = ge.id  ");
        qBuilder.append(" WHERE cc.acct_no ='").append(acctNo).append("' AND ge.code='A1' AND ");
        qBuilder.append(" ((cc.email IS NOT NULL AND cc.email!='') OR (cc.fax IS NOT NULL AND cc.fax!=''))");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("consigneeFax", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        return query.list();
    }

    public List<ImportsManifestBean> getAlLEmailIdsOfExportCodeK(String acctNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT cc.id as invoiceNo, cc.acct_no as agentNo,cc.first_name as agentName,cc.email as email,cc.fax as consigneeFax,");
        qBuilder.append("ge.code as chargeCode FROM cust_contact cc LEFT JOIN genericcode_dup ge ON cc.code_k = ge.id  ");
        qBuilder.append(" WHERE cc.acct_no ='").append(acctNo).append("' AND (ge.code='E' or ge.code='F') AND ");
        qBuilder.append(" ((cc.email IS NOT NULL AND cc.email!='') OR (cc.fax IS NOT NULL AND cc.fax!='')) AND cc.lcl_exports = TRUE");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("consigneeFax", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        return query.list();
    }

    public List<CustomerContact> checkA1CodeJ(String accountNo) throws Exception {
        String queryString = "from CustomerContact cc LEFT JOIN fetch cc.codej gc where cc.accountNo='" + accountNo + "' and gc.code='A1'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<CustomerContact> checkCodeKForInvoiceNotification(String accountNo) throws Exception {
        String queryString = "from CustomerContact cc LEFT JOIN fetch cc.codek gc where cc.accountNo='" + accountNo + "' and (gc.code='E' or  gc.code='F') AND cc.lclExports = TRUE";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<ImportsManifestBean> getCorrectionCreditDebitEmailForExport(String acctNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT cc.email as email,cc.fax as fax,");
        qBuilder.append("ge.code as chargeCode FROM cust_contact cc LEFT JOIN genericcode_dup ge ON cc.code_k = ge.id  ");
        qBuilder.append(" WHERE cc.acct_no =:acctNo AND ge.code='E'  AND cc.email IS NOT NULL AND cc.email!='' ");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setParameter("acctNo", acctNo);
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("email", StringType.INSTANCE);
        query.addScalar("fax", StringType.INSTANCE);
        return query.list();
    }
    
    public List<CustomerContact> checkCodeK(String accountNo) throws Exception {
        String queryString = "from CustomerContact cc LEFT JOIN fetch cc.codek gc where cc.accountNo='" + accountNo + "' and gc.code IN ('E', 'F') AND cc.lclImports = TRUE";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }
    
    public String getEmailAndFaxOfCodeK(String acctNo) {
        String query = "SELECT `ContactGetEmailFaxByAcctNoCodeK`(:acctNo, 'E', 'F')";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("acctNo", acctNo);
        return (String) queryObject.uniqueResult();
    }
}

package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.GeneralInformation;
import java.util.Arrays;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;

public class GeneralInformationDAO extends BaseHibernateDAO {

    public void save(GeneralInformation transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public String getAccountType(String acctNumber) throws Exception {
        String accountType = null;
        String queryString = "select generalinformation.accounttype from GeneralInformation generalinformation where generalinformation.accountNo='" + acctNumber + "'";
        List acctTypeList = getCurrentSession().createQuery(queryString).list();
        for (Iterator iterator = acctTypeList.iterator(); iterator.hasNext();) {
            accountType = (String) iterator.next();
        }
        return accountType;
    }

    public String getImportRatingColoadRetail(String acctNo) {
        String queryString = "select generalinformation.importQuoteColoadRetail from GeneralInformation generalinformation where generalinformation.accountNo='" + acctNo + "'";
        Object importsRatingColoadRetail = getCurrentSession().createQuery(queryString).uniqueResult();
        return null != importsRatingColoadRetail ? importsRatingColoadRetail.toString() : "";

    }

    public GeneralInformation getGeneralInformationByAccountNumber(String accountNumber) throws Exception {
        GeneralInformation generalInformation = null;
        Criteria criteria = getCurrentSession().createCriteria(GeneralInformation.class);
        criteria.add(Restrictions.eq("accountNo", accountNumber));
        generalInformation = (GeneralInformation) criteria.uniqueResult();
        return generalInformation;
    }

    public String isCommodityChangeApplyForThisCustomer(String acctNo) throws Exception {
        String q = "select group_concat(g.code,',',g.codedesc) from cust_general_info c join genericcode_dup g on c.fcl_commodity=g.id where acct_no='" + acctNo + "' and fcl_webquote_use_commodity='y'";
        Object s = getCurrentSession().createSQLQuery(q).uniqueResult();
        return null != s ? s.toString() : "";
    }

    public String[] getCommodity(String acctNo, String fromField, String notifyNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        if (CommonUtils.in(fromField, "Client", "Consignee")) {
            queryBuilder.append("  (select c.`code` from `genericcode_dup` c where c.`id` = g.`cons_coload_commodity` and c.`code` <> '000000') as commodityNo,");
            queryBuilder.append("  'Consignee Coload' as commodityType ");
        } else if (CommonUtils.in(fromField, "Notify")) {
            queryBuilder.append("  (select IF(g.apply_customer_commodity_rates='Y',c.`code`,NULL)  from `genericcode_dup` c where c.`id` = g.`cons_coload_commodity` and c.`code` <> '000000') as commodityNo,");
            queryBuilder.append("  'Consignee Coload' as commodityType ");
        } else {
            if (CommonUtils.isNotEmpty(notifyNo)) {
                queryBuilder.append("  if(n.`import_quote_coload_retail` <> 'C' and sc.`code` <> '000000', sc.`code`, if(si.`code` <> '000000', si.`code`, null)) as commodityNo,");
                queryBuilder.append("  if(n.`import_quote_coload_retail` <> 'C' and sc.`code` <> '000000', 'Ship/FF Retail', 'Origin Agent Import') as commodityType ");
            } else {
                queryBuilder.append("  (select c.`code` from `genericcode_dup` c where c.`id` = g.`imp_comm_no` and c.`code` <> '000000') as commodityNo,");
                queryBuilder.append("  'Origin Agent Import' as commodityType ");
            }
        }
        queryBuilder.append("from");
        queryBuilder.append("  `cust_general_info` g ");
        if (CommonUtils.isEqualIgnoreCase(fromField, "Agent") && CommonUtils.isNotEmpty(notifyNo)) {
            queryBuilder.append("  join (select n.`import_quote_coload_retail` from `cust_general_info` n where n.`acct_no` = :notifyNo) as n");
            queryBuilder.append("  left join `genericcode_dup` sc");
            queryBuilder.append("    on (sc.`id` = g.`retail_commodity`)");
            queryBuilder.append("  left join `genericcode_dup` si");
            queryBuilder.append("    on (si.`id` = g.`imp_comm_no`) ");
        }
        queryBuilder.append("where");
        queryBuilder.append("  g.`acct_no` = :acctNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("acctNo", acctNo);
        if (CommonUtils.isEqualIgnoreCase(fromField, "Agent") && CommonUtils.isNotEmpty(notifyNo)) {
            query.setString("notifyNo", notifyNo);
        }
        query.addScalar("commodityNo", StringType.INSTANCE);
        query.addScalar("commodityType", StringType.INSTANCE);
        query.setMaxResults(1);
        Object result = query.uniqueResult();
        if (null != result) {
            Object[] cols = (Object[]) result;
            return Arrays.copyOf(cols, cols.length, String[].class);
        }
        return new String[]{"", ""};
    }

    public List<Object[]> getHotCodesForAccount(String acctno) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT gc.id as id ,CONCAT(gc.`code` ,'/', gc.`codedesc`)as hotCode FROM genericcode_dup gc  WHERE gc.`code`  ");
        sb.append(" IN (SELECT cu.`hot_codes` FROM  cust_general_info cu WHERE  cu.`acct_no` =:acctno   AND  cu.hot_codes <> '' ");
        sb.append(" UNION  SELECT cu.`hot_codes1` FROM  cust_general_info cu  WHERE  cu.`acct_no`=:acctno  AND  cu.hot_codes1 <> '' ) ");
        sb.append(" and gc.codetypeid = (SELECT c.codetypeid FROM codetype c WHERE c.description = 'Hot Codes'); ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("acctno", acctno);
        List<Object[]> resultList = query.list();
        return resultList;
    }

    public List getHotCodesForMultieAccount(List acctnoList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT distinct CONCAT(gc.`code` ,'/', gc.`codedesc`)as hotCode FROM genericcode_dup gc  WHERE gc.`code`  ");
        sb.append(" IN (SELECT cu.`hot_codes` FROM  cust_general_info cu WHERE  cu.`acct_no` IN (:acctno)   AND  cu.hot_codes <> '' ");
        sb.append(" UNION  SELECT cu.`hot_codes1` FROM  cust_general_info cu  WHERE  cu.`acct_no` IN (:acctno)  AND  cu.hot_codes1 <> '' ) ");
        sb.append(" and gc.codetypeid = (SELECT c.codetypeid FROM codetype c WHERE c.description = 'Hot Codes'); ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("acctno", acctnoList);
        return query.list();
    }

    public String getEdiCode(String acctNo) throws Exception {
        String queryStr = "SELECT cu.shipping_code FROM cust_general_info cu where cu.acct_no = :acctNo ";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("acctNo", acctNo);
        String ediCode = (String) query.setMaxResults(1).uniqueResult();
        return null != ediCode ? ediCode : "";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Thamizh
 */
public class commodityTypeDAO extends BaseHibernateDAO<CommodityType> {

    public commodityTypeDAO() {
        super(CommodityType.class);
    }

    public CommodityType getCommodityCode(String commoditCode) throws Exception {
        Criteria criteria = getSession().createCriteria(CommodityType.class, "commodityType");
        if (!CommonUtils.isEmpty(commoditCode)) {
            criteria.add(Restrictions.eq("code", commoditCode));
        }
        return (CommodityType) criteria.setMaxResults(1).uniqueResult();
    }

    public List<CommodityType> findAllCommodityTypeList(String codeDesc, String orgId, String destId, int start, int end) throws Exception {
        List<CommodityType> commodityTypeList = new ArrayList<CommodityType>();
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select ct.desc_en,ct.code,ct.hazmat,ct.id FROM ");
        queryStr.append(database).append(".ofrate ofr ");
        queryStr.append(" left JOIN commodity_type ct ON ofr.comcde = ct.code ");
        queryStr.append(" WHERE ofr.oabcod!='A' and  ");
        queryStr.append(" ofr.trmnum  ='").append(orgId).append("' AND ");
        queryStr.append(" ofr.prtnum = '").append(destId).append("' ");
        if (CommonUtils.isNotEmpty(codeDesc)) {
            queryStr.append(" AND ct.desc_en LIKE '").append(codeDesc).append("%'");
        }
        queryStr.append(" AND id IS NOT NULL ORDER BY ct.desc_en ");
//        if (!CommonUtils.isEmpty(orgId)) {
//            if (!CommonUtils.isEmpty(codeDesc)) {
//                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' AND ct.desc_en LIKE '" + codeDesc + "%' ORDER BY ct.desc_en";
//            } else {
//                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' ORDER BY ct.desc_en";
//            }
//        } else {
//            if (!CommonUtils.isEmpty(codeDesc)) {
//                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id from commodity_type ct where ct.desc_en like '" + codeDesc + "%' ORDER BY ct.desc_en";
//            } else {
//                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id from commodity_type ct ORDER BY ct.desc_en";
//            }
//        }
        Query queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setFirstResult(start).setMaxResults(end);
        List<Object> queryResult = queryObject.list();
        if (!queryResult.isEmpty()) {
            for (Object row : queryResult) {
                Object[] col = (Object[]) row;
                CommodityType commodityType = new CommodityType();
                commodityType.setDescEn((String) col[0]);
                commodityType.setCode((String) col[1]);
                commodityType.setHazmat((Boolean) col[2]);
                commodityType.setId((Long) Long.parseLong(col[3].toString()));
                commodityTypeList.add(commodityType);
            }
        }
        return commodityTypeList;
    }

    public List<CommodityType> findAllCommodityTypeListWithoutCarrier(String codeDesc, String orgId, String destId, int start, int end) throws Exception {
        List<CommodityType> commodityTypeList = new ArrayList<CommodityType>();
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        if (!CommonUtils.isEmpty(orgId)) {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' AND ct.desc_en LIKE '" + codeDesc + "%' ORDER BY ct.desc_en";
            } else {
                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' ORDER BY ct.desc_en";
            }
        } else {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id from commodity_type ct where ct.desc_en like '" + codeDesc + "%' and code !=032500 ORDER BY ct.desc_en";
            } else {
                queryString = "select ct.desc_en,ct.code,ct.hazmat,ct.id from commodity_type ct where code !=032500 ORDER BY ct.desc_en";
            }
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setFirstResult(start).setMaxResults(end);
        List<Object> queryResult = queryObject.list();
        if (!queryResult.isEmpty()) {
            for (Object row : queryResult) {
                Object[] col = (Object[]) row;
                CommodityType commodityType = new CommodityType();
                commodityType.setDescEn((String) col[0]);
                commodityType.setCode((String) col[1]);
                commodityType.setHazmat((Boolean) col[2]);
                commodityType.setId((Long) Long.parseLong(col[3].toString()));
                commodityTypeList.add(commodityType);
            }
        }
        return commodityTypeList;
    }

    public int getTotalCommodityWithoutCarrier(String orgId, String destId) throws Exception {
        int count = 0;
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        if (orgId != null && !orgId.equals("")) {
            queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' ORDER BY ct.desc_en";
        } else {
            queryString = "select COUNT(ct.desc_en) from commodity_type ct where ct.code !=032500";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public int getTotalCommodity(String orgId, String destId) throws Exception {
        int count = 0;
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        if (orgId != null && !orgId.equals("")) {
            queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' ORDER BY ct.desc_en";
        } else {
            queryString = "select COUNT(ct.desc_en) from commodity_type ct";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public int getCommodityCount(String codeDesc, String orgId, String destId) throws Exception {
        int count = 0;
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        if (!CommonUtils.isEmpty(orgId)) {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' AND ct.desc_en LIKE '" + codeDesc + "%' ORDER BY ct.desc_en";
            } else {
                queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' ORDER BY ct.desc_en";
            }
        } else {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select count(ct.desc_en) from commodity_type ct where ct.desc_en like '" + codeDesc + "%' ORDER BY ct.desc_en";
            } else {
                queryString = "select count(ct.desc_en) from commodity_type ct ORDER BY ct.desc_en";
            }
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public int getCommodityCountWithoutCarrier(String codeDesc, String orgId, String destId) throws Exception {
        int count = 0;
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryString;
        if (!CommonUtils.isEmpty(orgId)) {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' AND ct.desc_en LIKE '" + codeDesc + "%' ORDER BY ct.desc_en";
            } else {
                queryString = "select count(ct.desc_en) FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' ORDER BY ct.desc_en";
            }
        } else {
            if (!CommonUtils.isEmpty(codeDesc)) {
                queryString = "select count(ct.desc_en) from commodity_type ct where ct.desc_en like '" + codeDesc + "%' and code !=032500 ORDER BY ct.desc_en";
            } else {
                queryString = "select count(ct.desc_en) from commodity_type ct where code !=032500 ORDER BY ct.desc_en";
            }
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        count = Integer.parseInt(queryObject.uniqueResult().toString());
        return count;
    }

    public List<CommodityType> getCommodityList(String orgId, String destId, int start, int end) throws Exception {
        List<CommodityType> commodityTypeList = new ArrayList<CommodityType>();
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        String queryBuilder = "select ct.desc_en,ct.code,ct.hazmat,ct.id FROM " + database + ".ofrate ofr left JOIN commodity_type ct ON ofr.comcde = ct.code WHERE ofr.oabcod!='A' and ofr.trmnum = '" + orgId + "' AND ofr.prtnum = '" + destId + "' and ct.code!='032500' ORDER BY ct.desc_en";
        Query query = getSession().createSQLQuery(queryBuilder);
        query.setFirstResult(start).setMaxResults(end);
        List<Object> queryResult = query.list();
        if (!queryResult.isEmpty()) {
            for (Object row : queryResult) {
                Object[] col = (Object[]) row;
                CommodityType commodityType = new CommodityType();
                commodityType.setDescEn((String) col[0]);
                commodityType.setCode((String) col[1]);
                commodityType.setHazmat((Boolean) col[2]);
                commodityType.setId((Long) Long.parseLong(col[3].toString()));
                commodityTypeList.add(commodityType);
            }
        }
        return commodityTypeList;
    }

    public String[] defaultImportComm(String agentNumber) throws Exception {
        String commDetails[] = new String[3];
        String query = "select ct.id,ct.code,ct.desc_en FROM cust_general_info cgi join genericcode_dup gd on gd.id = if(cgi.`import_quote_coload_retail` = 'R' and cgi.`commodity_no` <> '', cgi.`commodity_no`, cgi.`imp_comm_no`) join commodity_type ct on gd.code=ct.code WHERE acct_no='" + agentNumber + "'";
        Query queryObj = getCurrentSession().createSQLQuery(query);
        List commdetailsList = queryObj.list();
        if (commdetailsList != null && !commdetailsList.isEmpty()) {
            Object[] commDetailsObj = (Object[]) commdetailsList.get(0);
            if (commDetailsObj[0] != null && !commDetailsObj[0].toString().trim().equals("")) {
                commDetails[0] = commDetailsObj[0].toString();
            }
            if (commDetailsObj[1] != null && !commDetailsObj[1].toString().trim().equals("")) {
                commDetails[1] = commDetailsObj[1].toString();
            }
            if (commDetailsObj[2] != null && !commDetailsObj[2].toString().trim().equals("")) {
                commDetails[2] = commDetailsObj[2].toString();
            }
        }
        return commDetails;
    }

    public CommodityType getColoadCommodity(String accoutNumber) throws Exception {
        String queryString = "select c from CommodityType c,GeneralInformation g where g.accountNo = '" + accoutNumber + "' and g.consColoadCommodity.code = c.code";
        Query query = getCurrentSession().createQuery(queryString);
        query.setMaxResults(1);
        return (CommodityType) query.uniqueResult();
    }

    public List getCommodityList(String code) throws Exception {
        String sqlquery = "select id as id,code as code,active as active,desc_En as descEn,hazmat as hazmat,high_volume_discount as highVolumeDiscount,refrigeration_required as refrigerationRequired,default_ert as defaultErt,remarks as remarks from commodity_type";
        if ((null != code && CommonUtils.isNotEmpty(code))) {
            sqlquery += " where code='" + code + "'";
        }
        SQLQuery query = getSession().createSQLQuery(sqlquery);
        query.setResultTransformer(Transformers.aliasToBean(CommodityType.class));
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("code", StringType.INSTANCE);
        query.addScalar("active", BooleanType.INSTANCE);
        query.addScalar("descEn", StringType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("highVolumeDiscount", BooleanType.INSTANCE);
        query.addScalar("refrigerationRequired", BooleanType.INSTANCE);
        query.addScalar("defaultErt", BooleanType.INSTANCE);
        query.addScalar("remarks", StringType.INSTANCE);
        List<CommodityType> commList = query.list();
        return commList;
    }

    public String[] defaultExportCommodity(String commCode) throws Exception {
        String commDetails[] = new String[3];
        String query = "select id,code,desc_en FROM commodity_type where code=" + commCode;
        Query queryObj = getCurrentSession().createSQLQuery(query);
        List commdetailsList = queryObj.list();
        if (commdetailsList != null && !commdetailsList.isEmpty()) {
            Object[] commDetailsObj = (Object[]) commdetailsList.get(0);
            if (commDetailsObj[0] != null && !commDetailsObj[0].toString().trim().equals("")) {
                commDetails[0] = commDetailsObj[0].toString();
            }
            if (commDetailsObj[1] != null && !commDetailsObj[1].toString().trim().equals("")) {
                commDetails[1] = commDetailsObj[1].toString();
            }
            if (commDetailsObj[2] != null && !commDetailsObj[2].toString().trim().equals("")) {
                commDetails[2] = commDetailsObj[2].toString();
            }
        }
        return commDetails;
    }

    public BigInteger getClientTraffi(String clientAcctNo) throws Exception {
        StringBuilder clientTaraffi = new StringBuilder();
        clientTaraffi.append(" SELECT ct.id FROM cust_general_info cgi");
        clientTaraffi.append(" JOIN genericcode_dup gd ON cgi.cons_retail_commodity = gd.id");
        clientTaraffi.append(" JOIN commodity_type ct ON gd.code = ct.code WHERE acct_no =?");
        Query queryObject = getCurrentSession().createSQLQuery(clientTaraffi.toString());
        queryObject.setParameter(0, clientAcctNo);
        return (BigInteger) queryObject.uniqueResult();
    }
}

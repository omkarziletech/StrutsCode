/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;

public class LclSsDetailDAO extends BaseHibernateDAO<LclSsDetail> {

    public LclSsDetailDAO() {
        super(LclSsDetail.class);
    }

    public Long getIdbyAsc(Long ssHeaderId) throws Exception {
        String queryStr = "Select lsd.id as ssDetailId from lcl_ss_detail lsd where lsd.ss_header_id=:ssHeaderId order by lsd.id asc";
        SQLQuery queryObject = getSession().createSQLQuery(queryStr);
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.addScalar("ssDetailId", LongType.INSTANCE);
        return (Long) queryObject.setMaxResults(1).uniqueResult();
    }

    public List getAllTransModesForDisplay() throws Exception {
        List transModeList = new ArrayList();
        transModeList.add(new LabelValueBean("A=Air", "A"));
        transModeList.add(new LabelValueBean("R=Rail", "R"));
        transModeList.add(new LabelValueBean("T=Truck", "T"));
        transModeList.add(new LabelValueBean("V=Vessel", "V"));
        return transModeList;
    }

    public int getTransModeCountByHeader(String headerId, String detailId, String transMode) throws Exception {
        BigInteger count = new BigInteger("0");
        String queryString = "SELECT COUNT(*) FROM lcl_ss_detail WHERE ss_header_id ='" + headerId + "' and trans_mode='" + transMode + "' and id!='"
                + detailId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String findCarrierScac(final String spAcctNO, final Long lclSSDeatailId) throws Exception {
        String carrierScac = "";
        String queryString = "SELECT col.scac FROM carriers_or_line col "
                + "JOIN trading_partner tp ON col.carrier_code=tp.ssline_number "
                + "JOIN lcl_ss_detail lsd ON lsd.sp_acct_no=tp.acct_no WHERE lsd.sp_acct_no ='" + spAcctNO + "'AND lsd.id = " + lclSSDeatailId;
        Query queryObj = getSession().createSQLQuery(queryString);
        Object obj = queryObj.uniqueResult();
        if (obj != null) {
            carrierScac = (String) obj;
        }

        return carrierScac;
    }

    public String findContTypeAndSize(final String spAcctNO, final Long lclSSDeatailId) throws Exception {
        String carrierScac = "";
        String queryString = "SELECT col.scac FROM carriers_or_line col "
                + "JOIN trading_partner tp ON col.carrier_code=tp.ssline_number "
                + "JOIN lcl_ss_detail lsd ON lsd.sp_acct_no=tp.acct_no WHERE lsd.sp_acct_no ='" + spAcctNO + "'AND lsd.id = " + lclSSDeatailId;
        Query queryObj = getSession().createSQLQuery(queryString);
        Object obj = queryObj.uniqueResult();
        if (obj != null) {
            carrierScac = (String) obj;
        }

        return carrierScac;
    }

    public String getContractNumber(Long lclSSHeaderId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT FCL_contract_number FROM carriers_or_line ca ");
        queryBuilder.append("   JOIN trading_partner tp ON tp.ssline_number = ca.carrier_code ");
        queryBuilder.append(" JOIN lcl_ss_detail lclssdet ON tp.acct_no = lclssdet.sp_acct_no ");
        queryBuilder.append(" WHERE  lclssdet.trans_mode='V' AND ");
        queryBuilder.append(" lclssdet.ss_header_id =:ssheaderId ");
        SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
        query.setLong("ssheaderId", lclSSHeaderId);
        String contractNo = (String) query.uniqueResult();
        return null != contractNo ? contractNo : "";
    }

    public LclSsDetail findByTransMode(Long headerId, String transMode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsDetail.class, "lclSsDetail");
        criteria.createAlias("lclSsDetail.lclSsHeader", "ssHeader");
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("ssHeader.id", headerId));
        }
        if (!CommonUtils.isEmpty(transMode)) {
            criteria.add(Restrictions.eq("transMode", transMode));
        }
        criteria.addOrder(Order.desc("id"));
        return (LclSsDetail) criteria.setMaxResults(1).uniqueResult();
    }

    public String getAcctName(String headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select tp.acct_name FROM lcl_unit_ss lus join lcl_ss_detail lsd on lsd.ss_header_id = lus.ss_header_id ");
        sb.append(" join trading_partner tp on lsd.sp_acct_no=tp.acct_no ").append("WHERE lsd.ss_header_id = ").append(headerId);
        Object queryObjct = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return queryObjct.toString();
    }

    public String[] getAddress(String polUnlocationCode) throws Exception {

        String orginAddress[] = new String[4];
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT t.trmnam,t.addres1,t.city1,t.state,t.zipcde,t.phnnum1,t.faxnum1,t.tp_acct_no FROM ports p JOIN  lcl_port_configuration  lpc ON lpc.schnum= p.id ");
        queryString.append(" JOIN terminal t ON  p.govschnum = t.govschcode ");
        queryString.append(" WHERE p.unlocationcode= '").append(polUnlocationCode).append("' AND t.actyon='Y' limit 1");
        Query queryObj = getSession().createSQLQuery(queryString.toString());
        Object obj = queryObj.uniqueResult();
        if (obj != null) {
            Object[] orginObj = (Object[]) obj;
            if (orginObj[0] != null && !orginObj[0].toString().trim().equals("")) {
                orginAddress[0] = orginObj[0].toString();
            }
            //Checking Null Condition of every object to Remove 
            if (orginObj[1] == null || orginObj[1].toString().trim().equals("")) {
                orginAddress[1] = orginObj[2].toString() + "  , " + orginObj[3].toString() + " , "
                        + orginObj[4].toString() + "\nPhone : " + orginObj[5].toString() + "\nFax : " + orginObj[6].toString();
            } else if (orginObj[2] == null || orginObj[2].toString().trim().equals("")) {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[3].toString() + " , "
                        + orginObj[4].toString() + "\nPhone : " + orginObj[5].toString() + "\nFax : " + orginObj[6].toString();
            } else if (orginObj[3] == null || orginObj[3].toString().trim().equals("")) {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[2].toString() + " , "
                        + orginObj[4].toString() + "\nPhone : " + orginObj[5].toString() + "\nFax : " + orginObj[6].toString();
            } else if (orginObj[4] == null || orginObj[4].toString().trim().equals("")) {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[2].toString() + " , "
                        + orginObj[3].toString() + "\nPhone : " + orginObj[5].toString() + "\nFax : " + orginObj[6].toString();
            } else if (orginObj[5] == null || orginObj[5].toString().trim().equals("")) {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[2].toString() + " , "
                        + orginObj[3].toString() + " , " + orginObj[4].toString() + "\nFax : " + orginObj[6].toString();
            } else if (orginObj[6] == null || orginObj[6].toString().trim().equals("")) {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[2].toString() + " , "
                        + orginObj[3].toString() + " , " + orginObj[4].toString() + "\nPhone : " + orginObj[5].toString();
            } else {
                orginAddress[1] = orginObj[1].toString() + "\n" + orginObj[2].toString() + "  , " + orginObj[3].toString() + " , "
                        + orginObj[4].toString() + "\nPhone : " + orginObj[5].toString() + "\nFax : " + orginObj[6].toString();
            }
            if (orginObj[7] != null && !orginObj[7].toString().trim().equals("")) {
                orginAddress[2] = orginObj[7].toString();
            }
        }
        return orginAddress;
    }

    public String[] getAgentAddress(String fdUnlocationCode) throws Exception {
        String fdAddress[] = new String[8];
        StringBuilder conquery = new StringBuilder();
        conquery.append(" SELECT  tp.acct_name, a.agentid, ca.`address1`,ca.`state`,ca.city1,ca.zip,ca.`phone`,ca.fax ");
        conquery.append(" FROM agency_info a JOIN ports p ON p.id = a.schnum AND a.type = 'L'  AND a.default_agent = 'Y' ");
        conquery.append(" JOIN trading_partner tp ON a.agentid = tp.acct_no LEFT JOIN cust_address ca ON ca.acct_no = tp.acct_no ");
        conquery.append(" AND ca.`prime` = 'on' WHERE p.unlocationcode = '").append(fdUnlocationCode).append(" '  limit 1");
        Query query = getSession().createSQLQuery(conquery.toString());
        Object object = query.uniqueResult();
        if (object != null) {
            Object[] fdObj = (Object[]) object;
            if (fdObj[0] != null && !fdObj[0].toString().trim().equals("")) {
                fdAddress[0] = fdObj[0].toString();
            }
            if (fdObj[1] != null && !fdObj[1].toString().trim().equals("")) {
                fdAddress[1] = fdObj[1].toString();
            }
            if (fdObj[2] != null && !fdObj[2].toString().trim().equals("")) {
                fdAddress[2] = fdObj[2].toString();
            }
            if (fdObj[3] != null && !fdObj[3].toString().trim().equals("")) {
                fdAddress[3] = fdObj[3].toString();
            }
            if (fdObj[4] != null && !fdObj[4].toString().trim().equals("")) {
                fdAddress[4] = fdObj[4].toString();
            }
            if (fdObj[5] != null && !fdObj[5].toString().trim().equals("")) {
                fdAddress[5] = fdObj[5].toString();
            }
            if (fdObj[6] != null && !fdObj[6].toString().trim().equals("")) {
                fdAddress[6] = fdObj[6].toString();
            }
            if (fdObj[7] != null && !fdObj[7].toString().trim().equals("")) {
                fdAddress[7] = fdObj[7].toString();
            }
        }
        return fdAddress;
    }

    public List<LclSsDetail> getLclDetailList(Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsDetail.class, "lclSsDetail");
        criteria.createAlias("lclSsDetail.lclSsHeader", "ssHeader");
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("ssHeader.id", headerId));
        }
        return criteria.list();
    }

    public List getLclDetailArrivalCities(Long headerId) throws Exception {
        List<LabelValueBean> arrivalCityList = new ArrayList<LabelValueBean>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT un.id as unLocId , CONCAT(un.`un_loc_name`,CONCAT('(',un.`un_loc_code`,')'))  FROM  un_location  un  JOIN  ");
        queryString.append("lcl_ss_detail  lss  ON lss.`arrival_id` = un.`id` WHERE lss.`ss_header_id`=:headerId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setParameter("headerId", headerId);
        List<Object[]> resultList = query.list();
        for (Object[] row : resultList) {
            Integer cid = (Integer) row[0];
            String cname = (String) row[1];
            if (null != row[1] && null != row[0]) {
                arrivalCityList.add(new LabelValueBean(cname, cid.toString()));
            }
        }
        return arrivalCityList;
    }

    public LclSsDetail getLclDetailByArrivalId(Long headerId, Integer arrivalId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsDetail.class, "lclSsDetail");
        criteria.createAlias("lclSsDetail.lclSsHeader", "ssHeader");
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("ssHeader.id", headerId));
        }
        if (!CommonUtils.isEmpty(arrivalId)) {
            criteria.add(Restrictions.eq("arrival.id", arrivalId));
        }
        return (LclSsDetail) criteria.setMaxResults(1).uniqueResult();
    }

    public String getFlagFromElite(String vessel) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        String query = "SELECT el.flag FROM  " + databaseSchema + ".vessel el WHERE el.vesnam='" + vessel + "'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public Long getId(Long ssHeaderId) throws Exception {
        String queryStr = "Select lsd.id as ssDetailId from lcl_ss_detail lsd where lsd.ss_header_id=:ssHeaderId order by lsd.id desc";
        SQLQuery queryObject = getSession().createSQLQuery(queryStr);
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.addScalar("ssDetailId", LongType.INSTANCE);
        return (Long) queryObject.setMaxResults(1).uniqueResult();
    }

    public String getVesselCode(Long ssDetailId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT g.code FROM genericcode_dup g");
        queryStr.append(" WHERE g.codetypeid = (SELECT codetypeid FROM ");
        queryStr.append(" codetype WHERE description = 'Vessel Codes')  ");
        queryStr.append(" AND g.codedesc=(SELECT sp_reference_name FROM lcl_ss_detail WHERE id=:ssDetailId AND trans_mode='V') ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssDetailId", ssDetailId);
        return (String) queryObject.setMaxResults(1).uniqueResult();
    }
    
    public String[] getVoyageDetails(Long ssHeaderId) throws Exception {
        String voyageDetails[] = new String[4];
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT DATE_FORMAT(lsd.std,'%m/%d/%Y'),DATE_FORMAT(lsd.sta,'%m/%d/%Y'), ");
        queryString.append("lsd.sp_reference_no,lsd.sp_reference_name ");
        queryString.append("FROM lcl_ss_detail lsd WHERE lsd.ss_header_id= :ssHeaderId ORDER BY lsd.id DESC LIMIT 1");
        Query query = getSession().createSQLQuery(queryString.toString());
        query.setParameter("ssHeaderId", ssHeaderId);
        Object object = query.uniqueResult();
        if (object != null) {
            Object[] result = (Object[]) object;
            if (result[0] != null && !result[0].toString().trim().equals("")) {
                voyageDetails[0] = result[0].toString();
            }
            if (result[1] != null && !result[1].toString().trim().equals("")) {
                voyageDetails[1] = result[1].toString();
            }
            if (result[2] != null && !result[2].toString().trim().equals("")) {
                voyageDetails[2] = result[2].toString();
            }
            if (result[3] != null && !result[3].toString().trim().equals("")) {
                voyageDetails[3] = result[3].toString();
            }
        }
        return voyageDetails;
    }
     public String getCityAndState(String polUnlocationCode) throws Exception {
        String orginAddress = "";
        StringBuilder queryString = new StringBuilder();
        queryString.append(" SELECT t.city1,t.state FROM ports p JOIN  lcl_port_configuration  lpc ON lpc.schnum= p.id ");
        queryString.append(" JOIN terminal t ON  p.govschnum = t.govschcode ");
        queryString.append(" WHERE p.unlocationcode= '").append(polUnlocationCode).append("' AND t.actyon='Y' limit 1");
        Query queryObj = getSession().createSQLQuery(queryString.toString());
        Object obj = queryObj.uniqueResult();
        if (obj != null) {
            Object[] orginObj = (Object[]) obj;
            if (orginObj[0] != null && !orginObj[0].toString().trim().equals("")) {
                orginAddress = orginObj[0].toString();
                if (orginObj[1] != null && !orginObj[1].toString().trim().equals("")) {
                    orginAddress = orginAddress+","+orginObj[1].toString();
                }
            }
        }
        return orginAddress;
    }
}

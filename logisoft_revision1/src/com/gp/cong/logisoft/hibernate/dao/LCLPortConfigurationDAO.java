package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.gp.cong.logisoft.domain.LCLPortConfiguration;
import com.gp.cong.logisoft.domain.UnLocation;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

public class LCLPortConfigurationDAO extends BaseHibernateDAO {

    public void save(LCLPortConfiguration transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public LCLPortConfiguration findBySchnum(Integer schnum) throws Exception {
        String queryString = "from LCLPortConfiguration where shedulenumber=:schnum";
        Query query = getCurrentSession().createQuery(queryString);
        query.setInteger("schnum", schnum);
        return (LCLPortConfiguration) query.setMaxResults(1).uniqueResult();
    }

    public List findOnCarriage() throws Exception {
        List list = new ArrayList();
        String queryString = "from LCLPortConfiguration where onCarriage='O'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public String[] lclDefaultDestinationRemarks(String fdUnlocationcode, String podUnlocationcode) throws Exception {
        String results[] = new String[6];
        Query fdRemarks = null;
        String query = new String();
        if (fdUnlocationcode != null && podUnlocationcode != null) {
            if (fdUnlocationcode.equalsIgnoreCase(podUnlocationcode)) {
                query = "SELECT lpc.lcl_special_remarks_in_english,lpc.intrm,CONCAT(IF(lpc.frm IS NULL,'',lpc.frm) ,IF(gd.Field4 IS NULL,'',gd.Field4))  FROM ports p LEFT JOIN lcl_port_configuration lpc ON p.id = lpc.schnum LEFT JOIN genericcode_dup gd ON p.regioncode = gd.id WHERE p.unlocationcode ='" + fdUnlocationcode.trim() + "'";
                fdRemarks = getCurrentSession().createSQLQuery(query);
                List podList = fdRemarks.list();
                if (podList != null && podList.size() > 0) {
                    Object[] fdRemarksObj = (Object[]) podList.get(0);
                    if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                        results[0] = fdRemarksObj[0].toString();
                    } else {
                        results[0] = "";
                    }
                    if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                        results[1] = fdRemarksObj[1].toString();
                    }
                    if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                        results[2] = fdRemarksObj[2].toString();
                    }
                }
            } else {

                query = "SELECT lpc.lcl_special_remarks_in_english,lpc.intrm,CONCAT(IF(lpc.frm IS NULL,'',lpc.frm))   FROM lcl_port_configuration lpc LEFT JOIN ports p ON p.id = lpc.schnum WHERE  p.unlocationcode='" + podUnlocationcode.trim() + "' AND  p.port_city = 'Y'";
                fdRemarks = getCurrentSession().createSQLQuery(query);
                List podList = fdRemarks.list();
                if (podList != null && podList.size() > 0) {
                    Object[] fdRemarksObj = (Object[]) podList.get(0);
                    if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                        results[3] = fdRemarksObj[0].toString();
                    }
                    if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                        results[4] = fdRemarksObj[1].toString();
                    }
                    if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                        results[5] = fdRemarksObj[2].toString();
                    }
                }

                query = "SELECT lpc.lcl_special_remarks_in_english,lpc.intrm,CONCAT(IF(lpc.frm IS NULL,'',lpc.frm) ,IF(gd.Field4 IS NULL,'',gd.Field4))  FROM ports p LEFT JOIN lcl_port_configuration lpc ON p.id = lpc.schnum LEFT JOIN genericcode_dup gd ON p.regioncode = gd.id WHERE p.unlocationcode ='" + fdUnlocationcode.trim() + "'";
                fdRemarks = getCurrentSession().createSQLQuery(query);

                podList = fdRemarks.list();
                if (podList != null && podList.size() > 0) {
                    Object[] fdRemarksObj = (Object[]) podList.get(0);
                    if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                        results[0] = fdRemarksObj[0].toString();
                    }
                    if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                        results[1] = fdRemarksObj[1].toString();
                    }
                    if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                        results[2] = fdRemarksObj[2].toString();
                    }
                }
            }
        }
        return results;
    }

    public Object[] lclPortConfiguration(String fdUnlocationcode) throws Exception {
        Object warehs[] = null;
        Query fdRemarks = null;
        String query = new String();
        if (fdUnlocationcode != null) {
            query = "SELECT lpc.lcl_special_remarks_in_english,lpc.intrm,lpc.collect_charge_on_lcl_bls,lpc.ins_charges_lcl_bl FROM ports p LEFT JOIN lcl_port_configuration lpc ON p.id = lpc.schnum LEFT JOIN genericcode_dup gd ON p.regioncode = gd.id WHERE p.unlocationcode ='" + fdUnlocationcode.trim() + "' limit 1";
            fdRemarks = getCurrentSession().createSQLQuery(query);
            List<LCLPortConfiguration[]> list = fdRemarks.list();
            if (list != null && !list.isEmpty()) {
                warehs = (Object[]) list.get(0);
            }
        }
        return warehs;
    }

    public String[] lclDefaultDestinationImportRemarks(String podUnlocationcode) throws Exception {
        String results[] = new String[3];
        Query fdRemarks = null;
        String query = new String();
        query = "SELECT lpc.lcl_special_remarks_in_english,lpc.intrm,CONCAT(IF(lpc.frm IS NULL,'',lpc.frm) ,IF(gd.Field4 IS NULL,'',gd.Field4))  FROM ports p LEFT JOIN lcl_port_configuration lpc ON p.id = lpc.schnum LEFT JOIN genericcode_dup gd ON p.regioncode = gd.id WHERE p.unlocationcode ='" + podUnlocationcode.trim() + "'";
        fdRemarks = getCurrentSession().createSQLQuery(query);
        List podList = fdRemarks.list();
        if (!podList.isEmpty()) {
            Object[] fdRemarksObj = (Object[]) podList.get(0);
            if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                results[0] = fdRemarksObj[0].toString();
                results[0] = results[0];
            }
            if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                results[1] = fdRemarksObj[1].toString();
            }
            if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                results[2] = fdRemarksObj[2].toString();
            }
        }
        return results;
    }

    public List getTermsType(Integer unLocId) throws Exception {
        List termsTypeList = new ArrayList();
        if (null != unLocId) {
            UnLocation unLocation = new UnLocationDAO().findById(unLocId);
            termsTypeList.add(new LabelValueBean("Select", ""));
            if (null != unLocation) {
                if (unLocation.isExpressRelease()) {
                    termsTypeList.add(new LabelValueBean("Express Release", "ER"));
                }
                if (unLocation.isDoNotExpressRelease()) {
                    termsTypeList.add(new LabelValueBean("Do Not Express Release", "DER"));
                }
                if (unLocation.isOriginalsRequired()) {
                    termsTypeList.add(new LabelValueBean("Originals Required", "OR"));
                }
                if (unLocation.isOriginalsReleasedAtDestination()) {
                    termsTypeList.add(new LabelValueBean("Originals Released at Destination", "ORD"));
                }
                if (unLocation.isMemoHouseBillofLading()) {
                    termsTypeList.add(new LabelValueBean("MEMO HOUSE BILL OF LADING", "MBL"));
                }
            }
        }
        return termsTypeList;
    }

    public Boolean isForceAgentReleased(Integer unlocCodeId) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lpc.force_agent_released_dr as result FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.id=:unlocCodeId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unlocCodeId", unlocCodeId);
        query.addScalar("result", BooleanType.INSTANCE);
        Object result =  query.setMaxResults(1).uniqueResult();
        return null != result ? (Boolean) result : false;
    }

    public String getDefaultPortSetUpCode(String unLocCode) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF (lpc.fclrcl <> '',lpc.fclrcl,'') as result FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unLocCode", unLocCode);
        query.addScalar("result", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public Boolean isPrintImpOnMetric(String unLocCode) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lpc.print_imp_on_metric as result FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unLocCode", unLocCode);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.setMaxResults(1).uniqueResult();
    }

    public String getprintOFdollars(String unLocCode) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT printofdollars FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unLocCode", unLocCode);
        query.addScalar("printofdollars", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public Double getFTFFEECharge(String unLocCode) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT ftf_fee FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unLocCode", unLocCode);
        query.addScalar("ftf_fee", DoubleType.INSTANCE);
        return (Double) query.setMaxResults(1).uniqueResult();
    }

    // mantis Item: 14117
    public String getLclOceanbl(String unLocCode) {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lcl_ocean_bl FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unLocCode", unLocCode);
        query.addScalar("lcl_ocean_bl", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public boolean getSchnumValue(Integer schnum) throws Exception {
        String query = "SELECT print_invoice_value FROM lcl_port_configuration WHERE schnum=:schnum";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("schnum", schnum);
        Object result =  queryObject.uniqueResult();
        return null != result  ? (boolean) result : false;
    }

    public List getHazardousStatusYNR(String podUnlocationcode, String fdUnlocationcode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT  lpc.hazall,p.`search_port_name`    ");
        queryBuilder.append("  FROM lcl_port_configuration lpc   ");
        queryBuilder.append("   JOIN ports p  ON p.id = lpc.schnum  ");
        queryBuilder.append("  WHERE p.unlocationcode =:podUnlocationcode  ");
        queryBuilder.append("  UNION ALL  ");
        queryBuilder.append(" SELECT  lpc.hazall,p.`search_port_name`    ");
        queryBuilder.append("  FROM lcl_port_configuration lpc   ");
        queryBuilder.append("  JOIN ports p  ON p.id = lpc.schnum   ");
        queryBuilder.append(" WHERE p.unlocationcode =:fdUnlocationcode ");       
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());        
        query.setParameter("podUnlocationcode", podUnlocationcode);
        query.setParameter("fdUnlocationcode", fdUnlocationcode); 
        return query.list();      
    } 
    // Mantis item: 14601
    public boolean getLockport(String unLocCode) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT lpc.lock_port FROM lcl_port_configuration lpc  ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum WHERE p.unlocationcode=:unLocCode ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("unLocCode", unLocCode);
        return (boolean) queryObject.uniqueResult();
    } 
    public List getDefaultRateStatus(String pod) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT  lpc.default_rate,p.`search_port_name`    ");
        queryBuilder.append("  FROM lcl_port_configuration lpc   ");
        queryBuilder.append("   JOIN ports p  ON p.id = lpc.schnum  ");
        queryBuilder.append("  WHERE p.unlocationcode =:pod  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());        
        query.setParameter("pod", pod);
        return query.list();      
    } 
}

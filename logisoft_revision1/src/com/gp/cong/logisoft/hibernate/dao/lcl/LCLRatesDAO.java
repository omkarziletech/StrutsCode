package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.LclRatesInfoBean;
import com.gp.cong.logisoft.beans.LclRatesPrtChgBean;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.LclRatesForm;
import java.util.List;
import java.util.Properties;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class StdCharges.
 * @see com.logiware.webtool.hibernate.domain.StdCharges
 * @author MyEclipse - Hibernate Tools
 */
public class LCLRatesDAO extends BaseHibernateDAO {

    private String databaseSchema;

    public LCLRatesDAO(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    //property constants
    public Object[] findByOrgnDestComCde(String origin, String destination, String comcde) throws Exception {
        Object[] instance = null;
        String queryString = "SELECT cd01,cd02,cd03,cd04,cd05,cd06,cd07,cd08,cd09,cd10,cd11,cd12,pcof1,pcof2,"
                + "pcof3,pcof4,pcof5,pcof6,pcof7,pcof8,pcof9,pcof10,pcof11,pcof12 FROM " + databaseSchema
                + ".stdchg where term=?0 and port=?1 and commod=?2";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        List l = queryObject.list();
        if (!l.isEmpty()) {
            instance = (Object[]) l.get(0);
        }
        return instance;
    }

    public double getFFCommision(String origin, String pod, String fd) throws Exception {
        double ffCommission = 0.0;
        Object instance = null;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ffocom from ").append(databaseSchema).append(".stdchg where term=?0 and port=?1 and ffocom!=0");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", fd);
        instance = queryObject.setMaxResults(1).uniqueResult();
        if (null == instance && !pod.equals(fd)) {
            queryObject.setParameter("1", pod);
            instance = queryObject.setMaxResults(1).uniqueResult();
        }
        if (null != instance) {
            ffCommission = Double.parseDouble(instance.toString());
        }
        return ffCommission;
    }

    public List findByChdcod(String origin, String destination, String comcde, String chdcod) throws Exception {
        List l = null;
//         if (includeDestFees) {
//		if (!fdCharge) {
//		    String []str=chdcod.split(",");
//		    chdcod = "";
//		    for (int i = 0; i < str.length; i++) {
//			if (i != 0 && !"350".equals(str[i]) && !"351".equals(str[i])) {
//			    chdcod += ",";
//			}
//			if (!"350".equals(str[i]) && !"351".equals(str[i])) {
//			    chdcod += str[i];
// 		}
//		    }
//		    if("".equals(chdcod)){
//			chdcod ="' '";
//		    }
//		}
//	    }
        String queryString = "SELECT chdcod,chgtyp,flatrt,totpct,cuftrt,wghtrt,minchg,insurt,insamt,cbmrt,kgsrt "
                + " FROM " + databaseSchema + ".prtchg where trmnum=?0 and prtnum=?1 and commod=?2 and chdcod IN(" + chdcod + ")";

        Query queryObject = getSession().createSQLQuery(queryString);

        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        l = queryObject.list();
        return l;
    }

    public Double findMinchgByOrgDestComcdeChdcod(String origin, String destination, String comcde) throws Exception {
        String queryString = "select minchg from " + databaseSchema + ".prtchg where "
                + "trmnum=?0 and prtnum=?1 and commod=?2 and chdcod=1";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        Object minchg = queryObject.uniqueResult();
        return null != minchg ? Double.parseDouble(minchg.toString()) : 0.0;
    }

    public Object[] findByOrgnDestComCdeOfrate(String origin, String destination, String comcde) throws Exception {
        String queryString = "select engcft,engwgt,metcft,metwgt,ofmin from " + databaseSchema + ".ofrate where "
                + "trmnum=?0 and prtnum=?1 and comcde=?2";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        return (Object[]) queryObject.uniqueResult();
    }

    public List getDistinctOriginWithRates(String rateType, String origin, String destination, String comnumarray) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t.trmnum,un.un_loc_name,g.code,un.id,un.lat, un.lng,un.un_loc_code FROM terminal t JOIN un_location un ON ");
        queryBuilder.append("t.unlocationcode1=un.un_loc_code JOIN genericcode_dup g ON un.statecode=g.id JOIN genericcode_dup cntry ON un.countrycode=cntry.id AND cntry.code!='CA' JOIN ");
        queryBuilder.append(databaseSchema).append(".ofrate ofr ON t.trmnum=ofr.trmnum WHERE t.actyon=?0 AND ofr.prtnum=?1");
        if (comnumarray != null && comnumarray.length() > 0) {
            comnumarray = comnumarray.substring(0, comnumarray.length() - 1);
            queryBuilder.append(" and ofr.comcde IN(").append(comnumarray).append(") ");
        }
        if (origin != null && origin.length() > 0) {
            queryBuilder.append(" and ofr.trmnum = ").append(origin);
        }
        queryBuilder.append(" GROUP BY t.trmnum");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("1", destination);
        queryObject.setParameter("0", rateType);
        return queryObject.list();
    }

    public List getUnLocationArrayByTrmnumArray(String trmnumarray) throws Exception {
        String queryString = "select u.id from terminal t,un_location u where t.unLocationCode1=u.un_loc_code AND "
                + "t.trmnum IN(" + trmnumarray + ") order BY t.trmnum";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public List getDirectSailingList(Integer destination, String unlocarray, String trmnumArray) throws Exception {
        List l = null;
        String queryString = "SELECT poo_unloc.un_loc_code AS poo_code,pol_unloc.un_loc_code AS pol_code,pod_unloc.un_loc_code AS pod_code,"
                + "poo_unloc.un_loc_name AS poo_name,gen_dup.code AS state_code,( lcl_relay_poo.transit_time +  lcl_relay.co_dbd + "
                + "lcl_relay.transit_time + lcl_relay_fd.transit_time ) AS total_transit_time,poo_unloc.id AS poo_id,"
                + "fd_unloc.un_loc_code AS fd_code,poo_unloc.lat as latitude,poo_unloc.lng as logitude FROM lcl_relay_poo, lcl_relay_fd,lcl_relay, un_location poo_unloc LEFT JOIN "
                + "genericcode_dup gen_dup ON gen_dup.id=poo_unloc.statecode,un_location pol_unloc LEFT JOIN genericcode_dup gen_dup1 ON "
                + "gen_dup1.id = pol_unloc.statecode, un_location pod_unloc LEFT JOIN genericcode_dup gen_dup2 ON "
                + "gen_dup2.id = pod_unloc.statecode,un_location fd_unloc LEFT JOIN genericcode_dup gen_dup3 ON gen_dup3.id = "
                + "fd_unloc.statecode  WHERE lcl_relay_poo.poo_id IN (" + unlocarray + ") AND lcl_relay_poo.poo_id NOT IN (" + trmnumArray + ") "
                + "AND lcl_relay_poo.poo_id=lcl_relay.pol_id AND lcl_relay_poo.active = TRUE "
                + "AND lcl_relay_fd.fd_id = ?0 AND lcl_relay_fd.active = TRUE  AND lcl_relay_fd.relay_id = lcl_relay_poo.relay_id AND "
                + "lcl_relay.id = lcl_relay_poo.relay_id  AND lcl_relay.active = TRUE AND NOT EXISTS (SELECT lcl_relay_exception.id "
                + "FROM lcl_relay_exception WHERE lcl_relay_exception.poo_id = lcl_relay_poo.poo_id  AND  lcl_relay_exception.pol_id = "
                + "lcl_relay.pol_id  AND  lcl_relay_exception.pod_id = lcl_relay.pod_id AND  lcl_relay_exception.fd_id = "
                + "lcl_relay_fd.fd_id  AND lcl_relay_exception.active = TRUE) AND poo_unloc.id = lcl_relay_poo.poo_id AND pol_unloc.id ="
                + "lcl_relay.pol_id  AND pod_unloc.id = lcl_relay.pod_id AND fd_unloc.id = lcl_relay_fd.fd_id  ORDER BY lcl_relay_poo.poo_id";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", destination);
        l = queryObject.list();
        return l;
    }

    public Object[] getBarrelRate(String origin, String destination, String comcde) throws Exception {
        String queryString = "select brlofa,brltta from " + databaseSchema + ".ofrate where "
                + "trmnum=?0 and prtnum=?1 and comcde=?2";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        return (Object[]) queryObject.setMaxResults(1).uniqueResult();
    }

    public String findBarrelRate(String trmnum, String prtnum, String commodityNo) throws Exception {
        String SQL_QUERY = "select count(*) from " + databaseSchema + ".ofrate where trmnum=?0 and prtnum=?1 and comcde=?2 AND  (brlofa!=0.00 OR brltta!=0.00)";
        Query query = getCurrentSession().createSQLQuery(SQL_QUERY);
        query.setParameter("0", trmnum);
        query.setParameter("1", prtnum);
        query.setParameter("2", commodityNo);
        Object barrelRate = query.setMaxResults(1).uniqueResult();
        return null != barrelRate ? barrelRate.toString() : null;
    }

    public List<LclRatesInfoBean> findRates(String trmnum, String eciPortCode, String comcode) throws Exception {
        String queryString = "select engcft as engCft,engwgt as engWgt,metcft as metCft,metwgt as metWgt,ofmin as ofMin from " + databaseSchema + ".ofrate where "
                + "trmnum=" + trmnum + " and prtnum=" + eciPortCode + " and comcde=" + comcode;
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setResultTransformer(Transformers.aliasToBean(LclRatesInfoBean.class));
        query.addScalar("engCft", BigDecimalType.INSTANCE);
        query.addScalar("engWgt", BigDecimalType.INSTANCE);
        query.addScalar("metCft", BigDecimalType.INSTANCE);
        query.addScalar("metWgt", BigDecimalType.INSTANCE);
        query.addScalar("ofMin", BigDecimalType.INSTANCE);
        List<LclRatesInfoBean> lclratesList = query.list();
        return lclratesList;
    }

    public Object[] calculateInsuranceChargeCode(String origin, String destination, String commod) throws Exception {
        String queryString = "SELECT insurt,insamt,minchg"
                + " FROM " + databaseSchema + ".prtchg where trmnum=?0 and prtnum=?1 and commod=?2 and chdcod ='0006'";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", commod);
        return (Object[]) queryObject.setMaxResults(1).uniqueResult();
    }

    public Object[] findByChdcodForDeliveryMetro(String origin, String destination, String comcde, String chdcod) throws Exception {
        String queryString = "SELECT chgtyp,flatrt,cuftrt,wghtrt,minchg,cbmrt,kgsrt "
                + " FROM " + databaseSchema + ".prtchg where trmnum=?0 and prtnum=?1 and commod=?2 and chdcod=?3";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", origin);
        queryObject.setParameter("1", destination);
        queryObject.setParameter("2", comcde);
        queryObject.setParameter("3", chdcod);
        return (Object[]) queryObject.setMaxResults(1).uniqueResult();
    }

    public List<LclRatesPrtChgBean> findPrtChgRates(String origin, String destination, String comcde) throws Exception {
        String queryString = "SELECT chdcod as chgCode,flatrt as ofRate,minchg as minChg,cuftrt as cft,wghtrt as lbs,cbmrt as cbm,kgsrt as kgs,chgtyp as chargeTyp"
                + " FROM " + databaseSchema + ".prtchg where trmnum=" + origin + " and prtnum=" + destination + " and commod=" + comcde;
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setResultTransformer(Transformers.aliasToBean(LclRatesPrtChgBean.class));
        query.addScalar("chgCode", StringType.INSTANCE);
        query.addScalar("ofRate", BigDecimalType.INSTANCE);
        query.addScalar("minChg", BigDecimalType.INSTANCE);
        query.addScalar("cft", BigDecimalType.INSTANCE);
        query.addScalar("lbs", BigDecimalType.INSTANCE);
        query.addScalar("kgs", BigDecimalType.INSTANCE);
        query.addScalar("cbm", BigDecimalType.INSTANCE);
        query.addScalar("chargeTyp", StringType.INSTANCE);
        List<LclRatesPrtChgBean> lclratesList = query.list();
        return lclratesList;
    }

    public List<LclRatesPrtChgBean> findPrtChgRateswithChgCode(String origin, String destination, String comcde, String chgCode) throws Exception {
        String queryString = "SELECT chdcod as chgCode,flatrt as ofRate,minchg as minChg"
                + " FROM " + databaseSchema + ".prtchg where trmnum=" + origin + " and prtnum=" + destination + " and commod=" + comcde + " and chdcod= " + chgCode;
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.setResultTransformer(Transformers.aliasToBean(LclRatesPrtChgBean.class));
        query.addScalar("chgCode", StringType.INSTANCE);
        query.addScalar("ofRate", BigDecimalType.INSTANCE);
        query.addScalar("minChg", BigDecimalType.INSTANCE);
        List<LclRatesPrtChgBean> lclratesList = query.list();
        return lclratesList;
    }

    public void insertOfrates(LclRatesForm lclRatesForm, String databaseSchema, String username) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO " + databaseSchema + ".ofrate (trmnum,prtnum,comcde,");
        if (CommonUtils.isNotEmpty(lclRatesForm.getUom())) {
            if (lclRatesForm.getUom().equals("I")) {
                queryBuilder.append("engcft,engwgt,");
            } else {
                queryBuilder.append("metcft,metwgt,");
            }
        }
        queryBuilder.append("ofmin,");
        queryBuilder.append("effby,oabcod").append(") VALUES ('").append(lclRatesForm.getTrmnum()).append("','").append(lclRatesForm.getEciportcode()).append("','").append(lclRatesForm.getComCode()).append("',");
        queryBuilder.append(lclRatesForm.getMeasure()).append(",").append(lclRatesForm.getWeight()).append(",").append(lclRatesForm.getMinimum()).append(",");
        queryBuilder.append("'").append(username).append("','O'").append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateRates(LclRatesForm lclRatesForm, String databaseSchema, String username) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(databaseSchema).append(".ofrate set ");
        if (CommonUtils.isNotEmpty(lclRatesForm.getUom())) {
            if (lclRatesForm.getUom().equals("I")) {
                queryBuilder.append("engcft=").append(lclRatesForm.getMeasure()).append(",engwgt=").append(lclRatesForm.getWeight()).append(",");
            } else {
                queryBuilder.append("metcft=").append(lclRatesForm.getMeasure()).append(",metwgt=").append(lclRatesForm.getWeight()).append(",");
            }
        }
        queryBuilder.append("oabcod='O',").append("ofmin=").append(lclRatesForm.getMinimum()).append(",effby='").append(username).append("'").append(" where trmnum= '").append(lclRatesForm.getTrmnum()).append("' and prtnum='").append(lclRatesForm.getEciportcode()).append("' and comcde='").append(lclRatesForm.getComCode()).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void insertOfratesPrtChg(LclRatesForm lclRatesForm, String databaseSchema, String username) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO " + databaseSchema + ".prtchg (trmnum,prtnum,commod,chgtyp,");
        if (CommonUtils.isNotEmpty(lclRatesForm.getMinChg())) {
            queryBuilder.append("minchg,");
        }
        if (CommonUtils.isNotEmpty(lclRatesForm.getChgCode())) {
            queryBuilder.append("chdcod,");
        }
        if (CommonUtils.isNotEmpty(lclRatesForm.getFlatRate())) {
            queryBuilder.append("flatrt,");
        } else if (CommonUtils.isNotEmpty(lclRatesForm.getUom())) {
            if (lclRatesForm.getUom().equals("I")) {
                queryBuilder.append("cuftrt,wghtrt,");
            } else {
                queryBuilder.append("cbmrt,kgsrt,");
            }
        }
        queryBuilder.append("effby) VALUES ('").append(lclRatesForm.getTrmnum()).append("','").append(lclRatesForm.getEciportcode()).append("','").append(lclRatesForm.getComCode()).append("',");
        if (CommonUtils.isNotEmpty(lclRatesForm.getFlatRate())) {
            queryBuilder.append("'1',");
        } else {
            queryBuilder.append("'3',");
        }
        if (CommonUtils.isNotEmpty(lclRatesForm.getMinChg())) {
            queryBuilder.append(lclRatesForm.getMinChg()).append(",");
        }
        if (CommonUtils.isNotEmpty(lclRatesForm.getChgCode())) {
            queryBuilder.append("'").append(lclRatesForm.getChgCode()).append("',");
        }
        if (CommonUtils.isNotEmpty(lclRatesForm.getFlatRate())) {
            queryBuilder.append(lclRatesForm.getFlatRate()).append(",");
        } else {
            queryBuilder.append(lclRatesForm.getPrtMeasure()).append(",").append(lclRatesForm.getPrtWeight()).append(",");
        }
        queryBuilder.append("'").append(username).append("')");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public List getOfcertCommodityList(String commCode) {
        List list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(databaseSchema).append(".ofcert where comcde=:commCode");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("commCode", commCode);
        list = query.list();
        return list;
    }

    public List getOfcertList(String origin, String destination, String commCode) {
        List list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(databaseSchema).append(".ofcert where origin=:origin and destin=:destination and comcde=:commCode");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("origin", origin);
        query.setString("destination", destination);
        query.setString("commCode", commCode);
        list = query.list();
        return list;
    }

    public List getOfcert(String orginCode, String orginRegion, String destinationCode, String destinationRegion, String commCode) {
        List list = null;
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(databaseSchema).append(".ofcert ot where");
        sb.append(" (ot.`origin`=?").append(" OR ot.`orgreg`=?) AND");
        sb.append(" (ot.`destin`=?").append(" OR ot.`desreg`=?) AND");
        sb.append(" ot.`comcde`=?");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter(0, orginCode);
        query.setParameter(1, orginRegion);
        query.setParameter(2, destinationCode);
        query.setParameter(3, destinationRegion);
        query.setParameter(4, commCode);
        list = query.list();
        return list;
    }
    
    
//    public Object[] destinationCode(String comcde){
//        Properties prop = null;
//        Object[] instance = null;
//        try{
//            String query="SELECT code_001,code_002,code_003,code_004,code_005,code_006,code_007,code_008,code_009,"
//                    + "code_010,code_011,code_012,code_013,code_014,code_015,code_016,code_017,code_018,code_019,code_020 "
//                    + "FROM class_charge_codes c LEFT JOIN "+databaseSchema+".ofcomm o ON o.class=c.class_code WHERE o.comcde=?";
//            Query queryobject=getSession().createSQLQuery(query);
//            queryobject.setParameter(0, comcde);
//             List l = queryobject.list();
//            if (!l.isEmpty()) {
//                instance = (Object[]) l.get(0);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        
//        return instance;
//    }
    
}

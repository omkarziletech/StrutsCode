/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingPlan;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

/**
 *
 * @author Administrator
 */
public class LclBookingPlanDAO extends BaseHibernateDAO<LclBookingPlan> {

    public LclBookingPlanDAO() {
        super(LclBookingPlan.class);
    }

    public String lclSSHeaderGetNextScheduleNo() throws Exception {
        synchronized (this) {
            return (String) getCurrentSession().createSQLQuery("SELECT LCLSSHeaderGetNextScheduleNo()").uniqueResult();
        }
    }

    /**
     * Get Relay Value
     *
     * @param pooId,FdId
     * @param relayFlag Y-override,N-relayActive,All-None of these
     */
    public LclBookingPlanBean getRelay(Integer pooId, Integer fdId, String relayFlag) throws Exception {
        String queryStr = "call LCLRelayFind(:pooId,:fdId,:relayFlag)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setInteger("pooId", pooId);
        query.setInteger("fdId", fdId);
        query.setString("relayFlag", relayFlag);
        query.setResultTransformer(Transformers.aliasToBean(LclBookingPlanBean.class));
        query.addScalar("poo_id", IntegerType.INSTANCE);
        query.addScalar("poo_code", StringType.INSTANCE);
        query.addScalar("poo_name", StringType.INSTANCE);
        query.addScalar("poo_co_dow", IntegerType.INSTANCE);
        query.addScalar("poo_co_tod", TimestampType.INSTANCE);
        query.addScalar("poo_transit_time", IntegerType.INSTANCE);

        query.addScalar("pol_id", IntegerType.INSTANCE);
        query.addScalar("pol_code", StringType.INSTANCE);
        query.addScalar("pol_name", StringType.INSTANCE);
        query.addScalar("pol_co_dbd", IntegerType.INSTANCE);
        query.addScalar("pol_co_dow", IntegerType.INSTANCE);
        query.addScalar("pol_co_tod", TimestampType.INSTANCE);
        query.addScalar("pol_transit_time", IntegerType.INSTANCE);

        query.addScalar("pod_id", IntegerType.INSTANCE);
        query.addScalar("pod_code", StringType.INSTANCE);
        query.addScalar("pod_name", StringType.INSTANCE);
        query.addScalar("fd_id", IntegerType.INSTANCE);
        query.addScalar("fd_code", StringType.INSTANCE);
        query.addScalar("fd_name", StringType.INSTANCE);
        query.addScalar("fd_transit_time", IntegerType.INSTANCE);
        query.addScalar("total_transit_time", IntegerType.INSTANCE);

        List list = query.list();
        if (null != list && !list.isEmpty()) {
            return (LclBookingPlanBean) list.get(0);
        }
        return null;
    }

    public List<LclBookingVoyageBean> getUpComingSailingsSchedule(Integer poo, Integer pol,
            Integer pod, Integer fd, String voyageType, LclBookingPlanBean lclBookingPlanBean, String cfcl) throws Exception {
        List<LclBookingVoyageBean> voyageList = new ArrayList<LclBookingVoyageBean>();
        if (lclBookingPlanBean != null) {
            SQLQuery query = getCurrentSession().createSQLQuery("call LCLScheduleList(:pooId,:polId,:podId,:fdId,:voyageType,:cfcl)");
            query.setInteger("pooId", poo);
            query.setInteger("polId", pol);
            query.setInteger("podId", pod);
            query.setInteger("fdId", fd);
            query.setString("voyageType", voyageType);
            query.setString("cfcl", cfcl);
            List voyagelist = query.list();
            for (Object voyageObj : voyagelist) {
                Object[] row = (Object[]) voyageObj;
                LclBookingVoyageBean voyageBean = new LclBookingVoyageBean(row, poo, pol, pod, fd, lclBookingPlanBean);
                if (DateUtils.getDateDiffByTotalDaysWithNegative(voyageBean.getOriginLrd(), new Date()) >= 0) {
                    voyageList.add(voyageBean);
                }
            }
        }
        return voyageList;
    }

    public List<LclBookingVoyageBean> getUpComingSailingsScheduleOlder(Integer poo, Integer pol,
            Integer pod, Integer fd, String voyageType, LclBookingPlanBean lclBookingPlanBean, String prevSailing, String cfcl) throws Exception {
        List<LclBookingVoyageBean> voyageList = new ArrayList<LclBookingVoyageBean>();
        List<LclBookingVoyageBean> prevVoyageList = new ArrayList<LclBookingVoyageBean>();
        int olderVoyages = 0;
        if (CommonUtils.isNotEmpty(LoadLogisoftProperties.getProperty("OlderVoyages"))) {
            olderVoyages = Integer.parseInt(LoadLogisoftProperties.getProperty("OlderVoyages"));
        }
        if (lclBookingPlanBean != null) {
            SQLQuery query = getCurrentSession().createSQLQuery("call LCLScheduleListShowOlder(:pooId,:polId,:podId,:fdId,:voyageType,:cfcl,:olderVoyages)");
            query.setInteger("pooId", poo);
            query.setInteger("polId", pol);
            query.setInteger("podId", pod);
            query.setInteger("fdId", fd);
            query.setString("voyageType", voyageType);
            query.setString("cfcl", cfcl);
            query.setInteger("olderVoyages", olderVoyages);
            List voyagelist = query.list();
            for (Object voyageObj : voyagelist) {
                Object[] row = (Object[]) voyageObj;
                LclBookingVoyageBean voyageBean = new LclBookingVoyageBean(row, poo, pol, pod, fd, lclBookingPlanBean);
                if (DateUtils.getDateDiffByTotalDaysWithNegative(voyageBean.getOriginLrd(), new Date()) >= 0) {
                    voyageList.add(voyageBean);
                } else if (prevSailing.equalsIgnoreCase("true")) {
                    prevVoyageList.add(voyageBean);
                }
            }
            if (CommonUtils.isNotEmpty(prevVoyageList)) {
                prevVoyageList.addAll(voyageList);
                return prevVoyageList;
            }

        }
        return voyageList;
    }

    public LclBookingVoyageBean getSailingSchedule(Integer poo, Integer pol, Integer pod,
            Integer fd, String voyageType, LclBookingPlanBean lclBookingPlanBean, String cfcl) throws Exception {
        LclBookingVoyageBean voyageBean = null;
        if (lclBookingPlanBean != null && CommonUtils.isNotEmpty(poo)
                && CommonUtils.isNotEmpty(pol) && CommonUtils.isNotEmpty(pod) && CommonUtils.isNotEmpty(fd)) {
            SQLQuery query = getCurrentSession().createSQLQuery("call LCLScheduleList(:pooId,:polId,:podId,:fdId,:voyageType,:cfcl)");
            query.setInteger("pooId", poo);
            query.setInteger("polId", pol);
            query.setInteger("podId", pod);
            query.setInteger("fdId", fd);
            query.setString("voyageType", voyageType);
            query.setString("cfcl", cfcl);
            List list = query.list();
            if (list != null && !list.isEmpty()) {
                for (Object voyageObj : list) {
                    Object[] row = (Object[]) voyageObj;
                    voyageBean = new LclBookingVoyageBean(row, poo, pol, pod, fd, lclBookingPlanBean);
                    if (DateUtils.getDateDiffByTotalDaysWithNegative(voyageBean.getOriginLrd(), new Date()) >= 0) {
                        return voyageBean;
                    }
                }
                voyageBean.setOriginLrd(null);
            }
        }
        return voyageBean;
    }

    public Boolean isValidateRelay(String pooId, String fdId) throws Exception {
        String queryStr = "SELECT IF(COUNT(*)>0,TRUE,FALSE) as result FROM lcl_relay  WHERE pol_id=:pooId AND pod_id=:fdId LIMIT 1";
        SQLQuery queryObj = getCurrentSession().createSQLQuery(queryStr);
        queryObj.setParameter("pooId", pooId);
        queryObj.setParameter("fdId", fdId);
        queryObj.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) queryObj.uniqueResult();
    }

    public Date getAltDateByOriginalDate(Integer poo, Integer pol, String dateType, Date originalDate) throws Exception {
        StringBuilder dateBuilder = new StringBuilder();
        dateBuilder.append(" SELECT alt_date FROM  lcl_ss_alt_date  ");
        dateBuilder.append(" WHERE  original_date =  DATE_FORMAT(:date,'%Y-%m-%d') ");
        dateBuilder.append(" and origin_id=:pooId and destination_id=:polId and date_type=:dateType limit 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(dateBuilder.toString());
        query.setParameter("date", originalDate);
        query.setParameter("pooId", poo);
        query.setParameter("polId", pol);
        query.setParameter("dateType", dateType);
        Object altDate = query.uniqueResult();
        if (altDate != null) {
            Date altDates = (Date) query.uniqueResult();
            return altDates;
        }
        return originalDate;
    }

    public LclBookingPlanBean getRelayOverride(Integer pooId, Integer polId,
            Integer podId, Integer fdId, Integer fdTransTime) throws Exception {
        //StringBuilder queryStr = new StringBuilder();
//        queryStr.append(" select poo.co_dow AS `poo_co_dow`, ");
//        queryStr.append(" poo.co_tod AS `poo_co_tod`,poo.transit_time  AS `poo_transit_time`, ");
//        queryStr.append(" relay.co_dbd AS `pol_co_dbd`,relay.co_dow AS `pol_co_dow`, ");
//        queryStr.append(" relay.co_tod AS `pol_co_tod`,relay.transit_time  AS `pol_transit_time`, ");
//        queryStr.append(" fd.fd_id as fd_id,fd.transit_time as fd_transit_time FROM lcl_relay relay  ");
//        queryStr.append("  JOIN lcl_relay_poo poo ON poo.relay_id=relay.id ");
//        queryStr.append("  JOIN lcl_relay_fd fd ON fd.relay_id=relay.id ");
//        queryStr.append(" WHERE poo.poo_id=:pooId ");
//        queryStr.append(" AND relay.pol_id=:polId AND relay.pod_id=:podId");
//        queryStr.append(" and fd.fd_id=:fdId ");
//        queryStr.append("  LIMIT 1 ");
        String queryStr = "call LCLRelayFind(:polId,:podId,:relayFlag)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameter("polId", polId);
        query.setParameter("podId", podId);
        query.setParameter("relayFlag", "N");
        query.setResultTransformer(Transformers.aliasToBean(LclBookingPlanBean.class));
        query.addScalar("poo_co_dow", IntegerType.INSTANCE);
        query.addScalar("poo_co_tod", TimestampType.INSTANCE);
        query.addScalar("poo_transit_time", IntegerType.INSTANCE);

        query.addScalar("pol_co_dbd", IntegerType.INSTANCE);
        query.addScalar("pol_co_dow", IntegerType.INSTANCE);
        query.addScalar("pol_co_tod", TimestampType.INSTANCE);
        query.addScalar("pol_transit_time", IntegerType.INSTANCE);

        query.addScalar("fd_id", IntegerType.INSTANCE);
        query.addScalar("fd_transit_time", IntegerType.INSTANCE);

        List list = query.list();
        if (null != list && !list.isEmpty()) {
            LclBookingPlanBean relayOveridePlan = (LclBookingPlanBean) list.get(0);
            if (CommonUtils.isEmpty(relayOveridePlan.getFd_id())) {
                relayOveridePlan.setFd_transit_time(fdTransTime);
            }
            return relayOveridePlan;
        }
        return null;
    }

    public Boolean validateRelayOverride(String pooId, String polId, String podId, String fdId) throws Exception {
        LclBookingPlanBean lclBookingPlanBean = this.getRelayOverride(Integer.parseInt(pooId), Integer.parseInt(polId),
                Integer.parseInt(podId), Integer.parseInt(fdId), 0);
        if (lclBookingPlanBean != null && CommonUtils.isEmpty(lclBookingPlanBean.getFd_id())) {
            return true;
        }
        return false;
    }

    public List<LclBookingVoyageBean> getUpComingSailingsScheduleByBl(Integer poo, Integer pol,
            Integer pod, Integer fd, String voyageType, LclBookingPlanBean lclBookingPlanBean, String cfcl) throws Exception {
        List<LclBookingVoyageBean> voyageList = new ArrayList<LclBookingVoyageBean>();
        if (lclBookingPlanBean != null) {
            SQLQuery query = getCurrentSession().createSQLQuery("call BL_LCLScheduleListUpComing(:pooId,:polId,:podId,:fdId,:voyageType,:cfcl)");
            query.setInteger("pooId", poo);
            query.setInteger("polId", pol);
            query.setInteger("podId", pod);
            query.setInteger("fdId", fd);
            query.setString("voyageType", voyageType);
            query.setString("cfcl", cfcl);
            List voyagelist = query.list();
            for (Object voyageObj : voyagelist) {
                Object[] row = (Object[]) voyageObj;
                LclBookingVoyageBean voyageBean = new LclBookingVoyageBean(row, poo, pol, pod, fd, lclBookingPlanBean);
                voyageList.add(voyageBean);
            }
        }
        return voyageList;
    }

    public List<LclBookingVoyageBean> getSailingsScheduleSearch(Integer poo, Integer pol,
            Integer pod, Integer fd, String voyageType, LclBookingPlanBean lclBookingPlanBean) throws Exception {
        List<LclBookingVoyageBean> voyageList = new ArrayList<LclBookingVoyageBean>();
        if (lclBookingPlanBean != null) {
            SQLQuery query = getCurrentSession().createSQLQuery("call LCLScheduleListUpComing(:pooId,:polId,:podId,:fdId,:voyageType)");
            query.setInteger("pooId", poo);
            query.setInteger("polId", pol);
            query.setInteger("podId", pod);
            query.setInteger("fdId", fd);
            query.setString("voyageType", voyageType);
            List voyagelist = query.list();
            for (Object voyageObj : voyagelist) {
                Object[] row = (Object[]) voyageObj;
                LclBookingVoyageBean voyageBean = new LclBookingVoyageBean(row, poo, pol, pod, fd, lclBookingPlanBean);
                if (DateUtils.getDateDiffByTotalDaysWithNegative(voyageBean.getOriginLrd(), new Date()) >= 0) {
                    voyageList.add(voyageBean);
                }
            }
        }
        return voyageList;
    }

}

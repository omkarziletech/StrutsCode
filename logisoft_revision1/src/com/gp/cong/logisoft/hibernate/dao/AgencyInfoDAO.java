package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.AgencyInfo;
import com.gp.cong.logisoft.util.CommonFunctions;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

public class AgencyInfoDAO extends BaseHibernateDAO {

    public void save(AgencyInfo transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public void update(AgencyInfo transientInstance) throws Exception {
        getCurrentSession().update(transientInstance);
        getCurrentSession().flush();
    }

    public void delete(AgencyInfo persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
    }

    public List<AgencyInfo> getAgencyInfoList(Integer schnum, String type) {
        Criteria criteria = getCurrentSession().createCriteria(AgencyInfo.class);
        criteria.add(Restrictions.eq("schnum", schnum));
        criteria.add(Restrictions.eq("type", type));
        return criteria.list();
    }

    public String getTpAcct(Integer schnum) throws Exception {
        String queryString = "SELECT agentid FROM agency_info WHERE default_agent = 'Y' AND TYPE ='F' AND schnum=:schnum";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("schnum", schnum);
        String keyValue = (String) query.setMaxResults(1).uniqueResult();
        return CommonUtils.isNotEmpty(keyValue) ? keyValue : "";
    }

    public String getAgentLevelBrand(String agentNo, String unLocation) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.lcl_agent_level_brand FROM agency_info a JOIN ports p ON p.id = a.schnum  WHERE a.type='L' AND p.unlocationcode = '").append(unLocation).append("' AND  a.agentid='").append(agentNo).append("'");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        String keyValue = (String) queryObject.setMaxResults(1).uniqueResult();
        return CommonUtils.isNotEmpty(keyValue) ? keyValue : "";
    }

    public String[] getAgentPickAcctNo(Integer schnum, String agentNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select if(ag.`consigneeid` <> '' ,ag.`consigneeid`,ag.`agentid`) AS freightAcct,");
        sb.append("if(ag.port_of_discharge IS NOT NULL,`UnLocationGetNameStateCntryByID`(ag.port_of_discharge),'') AS pod,");
        sb.append("ag.final_deliveryTo AS fd FROM agency_info ag  WHERE ag.TYPE ='L' AND ag.agentid =:agentNo AND ag.schnum=:schnum");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("schnum", schnum);
        query.setParameter("agentNo", agentNo);
        query.addScalar("freightAcct", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("fd", StringType.INSTANCE);
        Object result = query.uniqueResult();
        if (CommonFunctions.isNotNull(result)) {
            String[] data = new String[3];
            int index = 0;
            for (Object value : (Object[]) result) {
                data[index] = (String) value;
                index++;
            }
            return data;
        } else {
            return null;
        }
    }
}

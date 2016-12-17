package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.struts.form.ManagePortsForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.TradeRouteBean;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;

public class PortsDAO extends BaseHibernateDAO {

    public Iterator getAllPortCodesForDisplay(StringBuffer portIds) throws Exception {
        String queryString = "select distinct id,shedulenumber from Ports where id not in ";
        queryString = queryString + "(" + portIds.toString() + ")";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public List findportsCon(String pierCode, String cnt) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.eq("shedulenumber", pierCode));
            criteria.addOrder(Order.asc("shedulenumber"));
        }
        if (cnt != null && !cnt.equals("")) {
            criteria.add(Restrictions.eq("controlNo", cnt));
            criteria.addOrder(Order.asc("controlNo"));
        }
        return criteria.list();
    }

    public List getAllPortCodesForCheck(StringBuffer portIds, StringBuffer portIds1) throws Exception {
        String queryString = "from Ports where id not in and id in (?)";
        queryString = queryString + "(" + portIds.toString() + ")";
        queryString = queryString + "(" + portIds1.toString() + ")";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List getPortsForAgentInfo(String pod) throws Exception {
        String queryString = "select a.agentId.accountno from AgencyInfo a,Ports p where p.portname=?0"
                + " and p.id=a.schnum and a.type='F'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        return queryObject.list();
    }

    public List<TradingPartner> getPortsForAgentInfoWithDefault(String pod, String type) throws Exception {
        pod = pod.trim();
        String queryString = "select a.agentId from AgencyInfo a,Ports p where p.unLocationCode=?0 and p.id=a.schnum and a.type='" + type + "' and a.defaultValue='Y'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        return queryObject.list();
    }

    public List<CustomerTemp> getPortsForAgentInfoWithDefaultForBooking(String pod, String type) throws Exception {
        pod = pod.trim();
        String queryString = "select a.agentId from AgencyInfo a,Ports p where p.unLocationCode=?0 and p.id=a.schnum and a.type='" + type + "' and a.defaultValue='Y'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        return queryObject.list();
    }

    public List getPortsofDefaultValues(String pod, String defaultAgentValues) throws Exception {
        pod = pod.trim();
        String queryString = "select a.agentId from AgencyInfo a,Ports p where p.unLocationCode=?0 and p.id=a.schnum and a.type='F' and a.agentId.accountno=?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        queryObject.setParameter("1", defaultAgentValues);
        return queryObject.list();
    }

    public String getPortsofDefaultConsignee(String pod, String defaultAgentValues) throws Exception {
        Object consigneeId = null;
        try {
            pod = pod.trim();
            String queryString = "SELECT a.consigneeid FROM agency_info a LEFT JOIN ports p ON a.schnum =p.id WHERE p.unLocationCode='" + pod + "' AND a.default_agent='Y' AND a.type='F' AND a.agentid='" + defaultAgentValues + "'";
            SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("consigneeid", StringType.INSTANCE);
            consigneeId = queryObject.uniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null != consigneeId ? consigneeId.toString() : "";
    }

    public List getPortsForConsigneeInfoWithDefault(String pod) throws Exception {
        pod = pod.trim();
        String queryString = "select a.consigneeId from AgencyInfo a,Ports p where p.unLocationCode=?0 and p.id=a.schnum and a.type='F' and a.defaultValue='Y'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        return queryObject.list();
    }

    public String getPortsForAgentInfoWithDefault1(String pod) throws Exception {
        int j = pod.indexOf("/");
        if (j != -1) {
            pod = pod.substring(0, j);
        }
        pod = pod.trim();
        String queryString = "select a.agentId.accountno from AgencyInfo a,Ports p where p.portname=?0 and p.id=a.schnum and a.type='F' and a.defaultValue='Y'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        Object accountNo = queryObject.setMaxResults(1).uniqueResult();
        return null != accountNo ? accountNo.toString() : "";
    }

    public String getPortsForAgentInfoWithDefault2(String pod) throws Exception {
        int j = pod.indexOf("/");
        if (j != -1) {
            pod = pod.substring(0, j);
        }
        pod = pod.trim();
        String queryString = "select a.agentId.accountName from AgencyInfo a,Ports p where p.portname=?0 and p.id=a.schnum and a.type='F' and a.defaultValue='Y'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", pod);
        Object accountName = queryObject.setMaxResults(1).uniqueResult();
        return null != accountName ? accountName.toString() : "";
    }

    public String getCountryName(String cityName) throws Exception {
        String queryString = "select countryName from Ports where unLocationCode=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", cityName);
        Object countryName = queryObject.setMaxResults(1).uniqueResult();
        return null != countryName ? countryName.toString() : "";
    }

    public String getPortId(String cityName) throws Exception {
        String queryString = "select id from Ports where unLocationCode=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", cityName);
        Object countryName = queryObject.setMaxResults(1).uniqueResult();
        return null != countryName ? countryName.toString() : "";
    }

    public Iterator getAllPortCodeForDisplay() throws Exception {
        String queryString = "select distinct id,shedulenumber from Ports";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public Iterator getAllPortForDisplay() throws Exception {
        String queryString = "select id,shedulenumber from Ports";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public Iterator getAllPortForDisplay1(String shedulenumber, String controlNo) throws Exception {
        String queryString = "select p.id,p.shedulenumber,p.portname from Ports p where p.shedulenumber=?0 and p.controlNo=?1 order by p.portname asc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", shedulenumber);
        queryObject.setParameter("1", controlNo);
        return queryObject.list().iterator();
    }

    public List findAllPorts() throws Exception {
        String queryString = " from PortsTemp";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setMaxResults(1000);
        return queryObject.list();
    }

    public void save(Ports transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public void update(Ports persistanceInstance, String userName) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(Ports persistanceInstance, String userName) throws Exception {
        getSession().delete(persistanceInstance);
    }

    public List findForExport(String code, String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (code != null && !code.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", code + "%"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("portname", codeDesc + "%"));
        }
        criteria.setMaxResults(50);

        criteria.addOrder(Order.asc("portname"));
        return criteria.list();
    }

    public Ports findById(Integer id) throws Exception {
        Ports instance = (Ports) getCurrentSession().get("com.gp.cong.logisoft.domain.Ports", id);
        return instance;
    }

    public PortsTemp findById1(Integer id) throws Exception {
        PortsTemp instance = (PortsTemp) getCurrentSession().get("com.gp.cong.logisoft.domain.PortsTemp", id);
        return instance;
    }

    public List findForManagement(ManagePortsForm managePortsForm) throws Exception {
        String scheduleCode = managePortsForm.getSheduleCode();
        String eciPortCode = managePortsForm.getEciPortCode();
        String portName = managePortsForm.getPortName();
        String pierCode = managePortsForm.getPierCode();
        String cityCode = managePortsForm.getCityCode();
        String country = managePortsForm.getCountry();
        String type = managePortsForm.getType();
        /* boolean SCHCcode=(CommonFunctions.isNotNull(managePortsForm.getSCHCode()))?true:false;
        boolean ECICode=(CommonFunctions.isNotNull(managePortsForm.getECICode()))?true:false;
         */ Integer limit = (CommonFunctions.isNotNull(managePortsForm.getLimit())) ? new Integer(managePortsForm.getLimit()) : null;
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (country != null && !country.equals("")) {
            criteria.add(Restrictions.like("countryName", country + "%"));
            criteria.addOrder(Order.asc("countryName"));
        }
        if (scheduleCode != null && !scheduleCode.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", scheduleCode + "%"));
            criteria.addOrder(Order.asc("shedulenumber"));
        }
        if (eciPortCode != null && !eciPortCode.equals("")) {
            criteria.add(Restrictions.like("eciportcode", eciPortCode + "%"));
            criteria.addOrder(Order.asc("eciportcode"));
        }
        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.like("portname", portName.trim() + "%"));
            criteria.addOrder(Order.asc("portname"));
        }
        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.like("piercode", pierCode + "%"));
            criteria.addOrder(Order.asc("piercode"));
        }
        if (cityCode != null && !cityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", cityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        if (type != null && !type.equals("")) {
            criteria.add(Restrictions.like("type", type + "%"));
            criteria.addOrder(Order.asc("type"));
        }

        Criterion price = Restrictions.isNotNull("shedulenumber");
        Criterion name = Restrictions.isNotNull("eciportcode");
        LogicalExpression orExp = Restrictions.or(price, name);
        criteria.add(orExp);
        criteria.addOrder(Order.asc("portname"));
        if (limit != null) {
            return criteria.setMaxResults(limit).list();
        } else {
            return criteria.list();
        }
    }

    public List findPierCode(String pierCode, String portName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", pierCode + "%"));
            criteria.addOrder(Order.asc("shedulenumber"));
        }
        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.like("portname", portName + "%"));
            criteria.addOrder(Order.asc("portname"));
        }

        criteria.add(Restrictions.like("controlNo", "1"));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findPierCode1(String pierCode, String portName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", pierCode + "%"));
            criteria.addOrder(Order.asc("shedulenumber"));
        }
        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.like("portname", portName + "%"));
            criteria.addOrder(Order.asc("portname"));
        }

        criteria.add(Restrictions.like("controlNo", "0001"));
        criteria.setMaxResults(50);

        return criteria.list();
    }

    public List findports(String pierCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", pierCode + "%"));
            criteria.addOrder(Order.asc("shedulenumber"));
        }
        return criteria.list();
    }

    public List findports1(String portname) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (portname != null && !portname.equals("")) {
            criteria.add(Restrictions.like("portname", portname + "%"));
            criteria.addOrder(Order.asc("portname"));
        }
        return criteria.setMaxResults(100).list();
    }

    public List findPortUsingUnlocaCode(String unLocationCode) {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (unLocationCode != null && !unLocationCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unLocationCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        return criteria.list();
    }

    public List findForManagementaction(ManagePortsForm managePortsForm) throws Exception {
        String scheduleCode = managePortsForm.getSheduleCode();
        String eciPortCode = managePortsForm.getEciPortCode();
        String portName = managePortsForm.getPortName();
        String pierCode = managePortsForm.getPierCode();
        String cityCode = managePortsForm.getCityCode();
        String country = managePortsForm.getCountry();
        String type = managePortsForm.getType();
        boolean SCHCcode = (CommonFunctions.isNotNull(managePortsForm.getSCHCode())) ? true : false;
        boolean ECICode = (CommonFunctions.isNotNull(managePortsForm.getECICode())) ? true : false;
        Integer limit = (CommonFunctions.isNotNull(managePortsForm.getLimit())) ? new Integer(managePortsForm.getLimit()) : 0;
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (country != null && !country.equals("0") && !country.equals("")) {
            criteria.add(Restrictions.ge("countryName", country));
            criteria.addOrder(Order.asc("countryName"));
        }
        if (scheduleCode != null && !scheduleCode.equals("")) {
            criteria.add(Restrictions.ge("shedulenumber", scheduleCode));
            criteria.addOrder(Order.asc("shedulenumber"));

        }
        if (eciPortCode != null && !eciPortCode.equals("")) {
            criteria.add(Restrictions.ge("eciportcode", eciPortCode));
            criteria.addOrder(Order.asc("eciportcode"));
        }
        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.ge("portname", portName));
            criteria.addOrder(Order.asc("portname"));
        }
        if (pierCode != null && !pierCode.equals("")) {
            criteria.add(Restrictions.ge("piercode", pierCode));
            criteria.addOrder(Order.asc("piercode"));
        }
        if (cityCode != null && !cityCode.equals("")) {
            criteria.add(Restrictions.ge("unLocationCode", cityCode));
            criteria.addOrder(Order.asc("unLocationCode"));
        }

        if (type != null && !type.equals("")) {
            criteria.add(Restrictions.ge("type", type));
            criteria.addOrder(Order.asc("type"));
        }
        if (SCHCcode) {
            criteria.add(Restrictions.isNotNull("shedulenumber"));
            criteria.add(Restrictions.ne("shedulenumber", ""));
        }
        if (ECICode) {
            criteria.add(Restrictions.isNotNull("eciportcode"));
            criteria.add(Restrictions.ne("eciportcode", ""));
        }
        if (limit != null) {
            return criteria.setMaxResults(limit).list();
        } else {
            return criteria.list();
        }
    }

    public List findForschcodeandcity(String scheduleCode, String portName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (scheduleCode != null && !scheduleCode.equals("")) {
            criteria.add(Restrictions.like("shedulenumber", scheduleCode + "%"));
            criteria.addOrder(Order.asc("shedulenumber"));
        }

        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.like("portname", portName + "%"));
            criteria.addOrder(Order.asc("portname"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findPortCode(String scheduleCode, String controlNo) throws Exception {
        String queryString = "from PortsTemp where shedulenumber=?0 and controlNo=?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", scheduleCode);
        queryObject.setParameter("1", controlNo);
        return queryObject.list();
    }

    public Iterator getPortsForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select id,shedulenumber from Ports").list().iterator();
        return results;
    }

    public Iterator getAllPortForDisplayNames() throws Exception {
        String queryString = "select id,portname from Ports";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public List findForUnlocCodeAndPortName(String unCityCode, String portName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (unCityCode != null && !unCityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unCityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        if (portName != null && !portName.equals("")) {
            criteria.add(Restrictions.like("portname", portName + "%"));
            criteria.addOrder(Order.asc("portname"));
        }
        criteria.setMaxResults(50);

        return criteria.list();
    }

    public List findForSchnumAndPortNameforPortsTemp(String portName) throws Exception {
        String portNameDup = "";
        StringBuilder queryBuilder = new StringBuilder();
        if (portName.length() > 1) {
            portNameDup = portName.replaceAll(" ", "-");
        }
        queryBuilder.append("select portName,stateCode,countryName,unLocationCode,");
        queryBuilder.append("govschnum as shedulenumber  from ports where govschnum is not null ");
        if (CommonFunctions.isNotNull(portName) && CommonFunctions.isNotNull(portNameDup)) {
            queryBuilder.append("and (portname like '").append(portName);
            queryBuilder.append("%'  or portname like '").append(portNameDup).append("%') ");
        } else {
            if (portName != null && !portName.equals("")) {
                queryBuilder.append("and (portname like '").append(portName).append("%') ");
            }
        }
        queryBuilder.append("limit 50 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.setResultTransformer(Transformers.aliasToBean(TradeRouteBean.class)).list();
    }

    public List findForUnlocCodeAndPortNameforPortsTemp(String unCityCode, String portName) throws Exception {
        String portNameDup = "";
        if (portName.length() > 1) {
            portNameDup = portName.replaceAll(" ", "-");
        }
        Criterion portNameCriteria;
        Criterion portNameDupCriteria;
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (unCityCode != null && !unCityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unCityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        if (CommonFunctions.isNotNull(portName) && CommonFunctions.isNotNull(portNameDup)) {
            portNameCriteria = Restrictions.like("portname", portName + "%");
            portNameDupCriteria = Restrictions.like("portname", portNameDup + "%");
            LogicalExpression orExp = Restrictions.or(portNameCriteria, portNameDupCriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("portname"));
        } else {
            if (portName != null && !portName.equals("")) {
                criteria.add(Restrictions.like("portname", portName + "%"));
                criteria.addOrder(Order.asc("portname"));
            }
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List getPorts(String[] port) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (null != port[0] && !"".equals(port[0])
                && null != port[1] && !"".equals(port[1])) {
            criteria.add(Restrictions.like("portname", port[0]));
            criteria.add(Restrictions.like("stateCode", port[1]));
            criteria.addOrder(Order.asc("portname"));
        }
        criteria.setMaxResults(1);
        return criteria.list();
    }

    public List<TradeRouteBean> searchForUnlocCodeAndPortNameforPortsTemp(String portName, String importFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select portname as portName,statecode as stateCode,countryname as countryName,unlocationcode as unLocationCode");
        queryBuilder.append(" from ports where (search_port_name like '").append(portName).append("%'");
        queryBuilder.append(" or unlocationcode like '").append(portName).append("%')");
        if (null != importFlag && importFlag.equalsIgnoreCase("true")) {
            queryBuilder.append(" and (unlocationcode not like 'US%' or unlocationcode = 'USJAX')");
        }
        queryBuilder.append(" order by portname asc limit 50");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryBuilder.toString());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(TradeRouteBean.class));
        return sqlQuery.list();
    }

    public List findPodForAgent(String portsName) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  CONCAT(UPPER(ul.un_loc_name), '/',IFNULL(UPPER(gd.code), UPPER(gc.codedesc)),'(',ul.un_loc_code,')') AS pod ");
        sb.append("FROM  un_location ul JOIN genericcode_dup gc ON ul.countrycode = gc.id LEFT JOIN genericcode_dup gd ON ul.statecode = gd.id WHERE ");
        sb.append("(ul.un_loc_name LIKE '").append(portsName).append("%' OR ul.search_un_loc_name LIKE '").append(portsName);
        sb.append("%' OR ul.un_loc_code LIKE '").append(portsName).append("%') and ul.un_loc_code <> '' GROUP BY ul.un_loc_code LIMIT 100");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sb.toString());
        return sqlQuery.list();
    }
    
    public List findForUnlocCodeAndPortNameForOriginService(String unCityCode, String portName, String identy) throws Exception {
        String portNameDup = "";
        SQLQuery query;
        StringBuilder queryBuilder = new StringBuilder();
        if (portName.length() > 1) {
            portNameDup = portName.replaceAll(" ", "-");
        }
        queryBuilder.append("select portName,unLocationCode,stateCode,countryName from ports where ");
        if (unCityCode != null && !unCityCode.equals("")) {
            queryBuilder.append("unlocationcode like '").append(unCityCode).append("%' and ");
        }
        if (CommonFunctions.isNotNull(portName) && CommonFunctions.isNotNull(portNameDup)) {
            queryBuilder.append("(search_port_name like '").append(portName).append("%' or search_port_name like '");
            queryBuilder.append(portNameDup).append("%') ");
        } else {
            if (portName != null && !portName.equals("")) {
                queryBuilder.append(" search_port_name like '").append(portName).append("%' ");
            }
        }
        queryBuilder.append("and ocean_origin_service='Y' limit 50");
        query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (identy.equalsIgnoreCase("getUnlocationCode")) {
            return query.setResultTransformer(Transformers.aliasToBean(TradeRouteBean.class)).list();
        } else {
            return query.setResultTransformer(Transformers.aliasToBean(PortsTemp.class)).list();
        }
    }

    public List findForUnlocCodeAndPortNameForDestinationService(String unCityCode, String portName) throws Exception {
        String portNameDup = "";
        if (portName.length() > 1) {
            portNameDup = portName.replaceAll(" ", "-");
        }
        Criterion portNameCriteria;
        Criterion portNameDupCriteria;
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (unCityCode != null && !unCityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unCityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        if (CommonFunctions.isNotNull(portName) && CommonFunctions.isNotNull(portNameDup)) {
            portNameCriteria = Restrictions.like("portname", portName + "%");
            portNameDupCriteria = Restrictions.like("portname", portNameDup + "%");
            LogicalExpression orExp = Restrictions.or(portNameCriteria, portNameDupCriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("portname"));
        } else {
            if (portName != null && !portName.equals("")) {
                criteria.add(Restrictions.like("portname", portName + "%"));
                criteria.addOrder(Order.asc("portname"));
            }
        }
        criteria.add(Restrictions.like("oceanDestinationService", "Y"));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List<TradeRouteBean> getAllTheCitiesBasedOnCountry(String country) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select portname as portName,statecode as stateCode,countryname as countryName,");
        stringBuilder.append("unlocationcode as unLocationCode  from ports ");
        if (country != null && !country.equals("")) {
            stringBuilder.append(" where countryname like '").append(country).append("%' ");
        }
        stringBuilder.append(" order by portname asc");
        stringBuilder.append(" limit 50");
        SQLQuery query = getCurrentSession().createSQLQuery(stringBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(TradeRouteBean.class));
        return query.list();
    }

    public List findForUnlocCodeAndPortNameForDestinationServiceBYPortCity(String unCityCode, String portName) throws Exception {
        String portNameDup = "";
        if (null != portName && portName.length() > 1) {
            portNameDup = portName.replaceAll(" ", "-");
        }
        Criterion portNameCriteria;
        Criterion portNameDupCriteria;
        Criteria criteria = getCurrentSession().createCriteria(PortsTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (unCityCode != null && !unCityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unCityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));

        }
        if (CommonFunctions.isNotNull(portName) && CommonFunctions.isNotNull(portNameDup)) {
            portNameCriteria = Restrictions.like("portname", portName + "%");
            portNameDupCriteria = Restrictions.like("portname", portNameDup + "%");
            LogicalExpression orExp = Restrictions.or(portNameCriteria, portNameDupCriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("portname"));
        } else {
            if (portName != null && !portName.equals("")) {
                criteria.add(Restrictions.like("portname", portName + "%"));
                criteria.addOrder(Order.asc("portname"));
            }
        }

        criteria.add(Restrictions.like("portCity", "Y"));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List<TradeRouteBean> searchForUnlocCodeAndPortNameForDestinationServiceBYPortCity(String portName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select portname as portName,statecode as stateCode,countryname as countryName,");
        queryBuilder.append("unlocationcode as unLocationCode from ports  where ");
        queryBuilder.append("(search_port_name like '").append(portName).append("%'");
        queryBuilder.append(" or unlocationcode like '").append(portName).append("%')");
        queryBuilder.append(" order by portname asc limit 50");
        SQLQuery sQLQuery = getCurrentSession().createSQLQuery(queryBuilder.toString());
        sQLQuery.setResultTransformer(Transformers.aliasToBean(TradeRouteBean.class));
        return sQLQuery.list();
    }

    /**
     * @param govschnum
     * @return
     */
    public String getEmailIdsForOperationContactsOfArCreditHold(String govschnum) throws Exception {
        String queryString = "Select u.email from User u,FCLPortConfiguration fclport,Ports p where u.userId=fclport.lineManager.userId and p.id=fclport.shedulenumber and p.shedulenumber='" + govschnum + "'";
        Object emailid = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != emailid ? emailid.toString() : "";
    }

    public List getAllRegion() throws Exception {
        String queryString = "select id, codedesc from genericcode_dup where codetypeid = 19 order by codedesc";
        return getSession().createSQLQuery(queryString).list();
    }

    public List getAllRegions() throws Exception {
        List regionList = new ArrayList();
        String queryString = "select id, codedesc from genericcode_dup where codetypeid = 19 order by codedesc";
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object regions : queryObject.list()) {
            Object[] region = (Object[]) regions;
            regionList.add(new LabelValueBean(region[1].toString(), region[0].toString()));
        }
        return regionList;
    }

    public List getAllCountryByRegion(String regionCode) throws Exception {
        List regionList = new ArrayList();
        String queryString = "";
        if (regionCode == "1") {
            queryString = "select distinct countryname from ports order by countryname";
        } else {
            queryString = "select distinct countryname from ports where regioncode = " + regionCode + " order by countryname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        regionList.add(new LabelValueBean("Select One", "0"));
        for (Object country : queryObject.list()) {
            String region = null != country ? country.toString() : "";
            regionList.add(new LabelValueBean(region, region));
        }
        return regionList;
    }

    public List getAllCountryByRegion1(String regionCode, String origin) throws Exception {
        List regionList = new ArrayList();
        String queryString = "";
        if (regionCode == "1") {
            queryString = "select distinct countryname from ports order by countryname";
        } else {
            origin = origin.replace("'", "'+'");
            queryString = "select distinct countryname from ports where regioncode = " + regionCode + " and portname='" + origin + "' order by countryname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object country : queryObject.list()) {
            String region = null != country ? country.toString() : "";
            regionList.add(new LabelValueBean(region, region));
        }

        return regionList;
    }

    public List getAllUnLocationByRegionCodeAndCountry(Integer regionCode, String country) throws Exception {
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        List regionList = new ArrayList();
        String queryString = "";
        if (regionCode == 1) {
            queryString = "select portname, unlocationcode from ports where countryname = '" + country + "' order by portname";
        } else {
            queryString = "select portname, unlocationcode from ports where  regioncode = " + regionCode + " and countryname = '" + country + "' order by portname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object regions : queryObject.list()) {
            Object[] region = (Object[]) regions;
            String unLocationCode = null != region[1] ? region[1].toString() : "";
            String unLocationName = null != region[0] ? region[0].toString() : "";
            UnLocation unLocation = unLocationDAO.getUnlocation(unLocationCode);
            regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode, StringFormatter.formatForPolPodDoorOrgDoorDest(unLocation)));
        }
        return regionList;
    }

    public List getAllUnLocationByRegionCodeAndCountryforiginServiceByPortCity(Integer regionCode, String country, String typeOfMove) throws Exception {
        List regionList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        String queryString = "";

        if (regionCode == 1) {
            queryString = "select portname, unlocationcode,stateCode from ports where countryname = '" + country + "' and port_city='Y' order by portname";
        } else {
            queryString = "select portname, unlocationcode,stateCode from ports where  regioncode = " + regionCode + " and countryname = '" + country + "' and port_city='Y' order by portname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object regions : queryObject.list()) {
            Object[] region = (Object[]) regions;
            String unLocationName = null != region[0] ? region[0].toString() : "";
            String unLocationCode = null != region[1] ? region[1].toString() : "";
            String stateCode = null != region[2] ? region[2].toString() : "";
            UnLocation unLocation = unLocationDAO.getUnlocation(unLocationCode);
            if (stateCode.equals("")) {
                regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode + "/" + stateCode, StringFormatter.formatForPolPodDoorOrgDoorDest(unLocation)));
            } else {
                regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode + "/" + stateCode, StringFormatter.formatForPolPodDoorOrgDoorDest(unLocation)));
            }
        }
        return regionList;
    }

    public List getAllUnLocationByRegionCodeAndCountryforiginService(Integer regionCode, String country, String typeOfMove) throws Exception {
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        List regionList = new ArrayList();
        String queryString = "";

        if (regionCode == 1) {
            queryString = "select portname, unlocationcode,stateCode from ports where countryname = '" + country + "' and ocean_origin_service='Y' order by portname";
        } else {
            queryString = "select portname, unlocationcode,stateCode from ports where  regioncode = " + regionCode + " and countryname = '" + country + "' and ocean_origin_service='Y' order by portname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);
        for (Object regions : queryObject.list()) {
            Object[] region = (Object[]) regions;
            String unLocationName = null != region[0] ? region[0].toString() : "";
            String unLocationCode = null != region[1] ? region[1].toString() : "";
            UnLocation unLocation = unLocationDAO.getUnlocation(unLocationCode);
            String stateCode = null != region[2] ? region[2].toString() : "";
            if (stateCode.equals("")) {
                regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode + "/" + stateCode, StringFormatter.formatForOrigin(unLocation)));
            } else {
                regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode + "/" + stateCode, StringFormatter.formatForOrigin(unLocation)));
            }
        }
        return regionList;
    }

    public List getAllUnLocationByRegionCodeAndCountryForDestinationService(Integer regionCode, String country) throws Exception {
        List regionList = new ArrayList();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        String queryString = "";
        if (regionCode == 1) {
            queryString = "select portname, unlocationcode from ports where countryname = '" + country + "' and ocean_destination_service='Y'  order by portname";
        } else {
            queryString = "select portname, unlocationcode from ports where  regioncode = " + regionCode + " and countryname = '" + country + "' and ocean_destination_service='Y' order by portname";
        }
        Query queryObject = getSession().createSQLQuery(queryString);

        for (Object regions : queryObject.list()) {
            Object[] region = (Object[]) regions;
            String unLocationCode = null != region[1] ? region[1].toString() : "";
            UnLocation unLocation = unLocationDAO.getUnlocation(unLocationCode);
            String unLocationName = null != region[0] ? region[0].toString() : "";
            regionList.add(new LabelValueBean(unLocationName + "/" + unLocationCode, StringFormatter.formatForDestination(unLocation)));
        }
        return regionList;
    }

    public String getSpecialRemarks(String portName) throws Exception {
        String[] city = portName.split("/");
        if (null != portName) {
            portName = city[0];
        }
        String queryString = "SELECT special_remarks_for_quot FROM fcl_port_configuration, ports WHERE portname = \"" + portName + "\" AND fcl_port_configuration.schnum = ports.id limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("special_remarks_for_quot", StringType.INSTANCE);
        Object specialRemarks = queryObject.uniqueResult();
        return null != specialRemarks ? specialRemarks.toString() : "";
    }

    public String getFieldsByUnlocCode(String field, String unlocCode) throws Exception {
        String queryString = "SELECT " + field + " FROM ports WHERE unlocationcode= '" + unlocCode + "' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar(field, StringType.INSTANCE);
        Object result = queryObject.uniqueResult();
        return null != result ? result.toString() : "";
    }

    public List getPortNameAndUnLocCode(Integer regionCode) throws Exception {
        List regionList = new ArrayList();
        String queryString = "";
        queryString = "select portname, unlocationcode,stateCode,countryname from ports where  regioncode = " + regionCode + "  order by portname";
        Query queryObject = getSession().createSQLQuery(queryString);
        regionList = queryObject.list();
        return regionList;
    }

    public List getPortNameLocationAndUnLocCode(Integer regionCode) throws Exception {
        List regionList = new ArrayList();
        String queryString = "";
        queryString = " from Ports where  regioncode = " + regionCode;
        Query queryObject = getSession().createQuery(queryString);
        regionList = queryObject.list();
        return regionList;
    }

    public List getDestionationPort(String destination_port) throws Exception {
        List regionList = new ArrayList();
        String queryString = "";
        queryString = " from Ports where  portname = '" + destination_port + "'";
        Query queryObject = getSession().createQuery(queryString);
        regionList = queryObject.list();
        return regionList;
    }

    public String getRegionBasedOnDest(String unLocCode) throws Exception {
        String queryString = "select f.code from genericcode_dup f where f.id = "
                + "(select p.regioncode from ports p where p.unlocationcode = '" + unLocCode + "' limit 1) limit 1";
        Object region = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != region ? region.toString() : "";
    }

    public List getAgentInfo(String agendId, Integer portId) throws Exception {
        List agendList = new ArrayList();
        Query queryObject = getSession().createQuery("from AgencyInfo where  "
                + "agentId.accountNo= '" + agendId + "'and schnum=" + portId + " and type='F'");

        agendList = queryObject.list();
        return agendList;
    }

    public List getListOfFclAgents(Integer id) throws Exception {
        List agentList = new ArrayList();
        Query queryObject = getSession().createQuery(" from AgencyInfo a  where a.schnum=" + id + " and a.type='F'");
        agentList = queryObject.list();
        return agentList;
    }

    public Ports getPorts(String unLocationCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.add(Restrictions.eq("unLocationCode", unLocationCode));
        criteria.setMaxResults(1);
        return (Ports) criteria.uniqueResult();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }

    public List getPortName(String portName) throws Exception {
        List portsList = null;
        String query = "from Ports where portname ='" + portName + "'";
        portsList = getCurrentSession().createQuery(query).list();
        return portsList;
    }

    public List getOriginTerminal(String portName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT fclorg.origin_terminal,pr.portname");
        queryBuilder.append(" FROM ports pr, fcl_org_dest_misc_data fclorg");
        queryBuilder.append(" WHERE fclorg.origin_terminal=pr.id AND pr.portname like ?0");
        queryBuilder.append(" group by fclorg.origin_terminal order by pr.portname");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", portName + "%");
        return query.list();
    }

    public List getDestinationPorts(String portName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT fclorg.destination_port,pr.portname");
        queryBuilder.append(" FROM ports pr, fcl_org_dest_misc_data fclorg");
        queryBuilder.append(" WHERE fclorg.destination_port=pr.id AND pr.portname like '").append(portName).append("%'");
        queryBuilder.append(" group by fclorg.destination_port order by pr.portname");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List getDPorts(String portName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT rd.destin,pr.portname");
        queryBuilder.append(" FROM ports pr join  retadd rd on rd.destin=pr.govschnum ");
        queryBuilder.append(" WHERE  pr.portname like '").append(portName).append("%'");
        queryBuilder.append(" group by rd.destin order by pr.portname");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public String[] getAllSynonymousCity(String destPortId, String originPortId) throws Exception {
        String unLocDest = null;
        String unLocOrigin = null;
        String[] ports = new String[2];
        if (null != destPortId && destPortId.indexOf("(") > -1) {
            unLocDest = destPortId.substring(destPortId.indexOf("(") + 1, destPortId.indexOf(")"));
        }
        List resultDest = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un JOIN un_location un1 ON un1.un_loc_code='" + unLocDest + "' AND un1.synonymous_city='Y' AND un.synonymous_group=un1.synonymous_group AND un.countrycode=un1.countrycode JOIN fcl_buy f ON f.destination_port=un.id GROUP BY un.id").
                list();
        if (null != originPortId && originPortId.indexOf("(") > -1) {
            unLocOrigin = originPortId.substring(originPortId.indexOf("(") + 1, originPortId.indexOf(")"));
        }
        List resultOrigin = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un JOIN un_location un1 ON un1.un_loc_code='" + unLocOrigin + "' AND un1.synonymous_city='Y' AND un.synonymous_group=un1.synonymous_group AND un.countrycode=un1.countrycode JOIN fcl_buy f ON f.origin_terminal=un.id GROUP BY un.id").
                list();
        if ((null != resultDest && resultDest.size() > 0)) {
            for (Object obj : resultDest) {
                if (null == ports[0]) {
                    ports[0] = obj.toString();
                } else {
                    ports[0] = ports[0] + "," + obj.toString();
                }
            }

        } else {
            resultDest = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un where un.un_loc_code='" + unLocDest + "' ").list();
            for (Object obj : resultDest) {
                if (null == ports[0]) {
                    ports[0] = obj.toString();
                }
            }
        }
        if (null != resultOrigin && resultOrigin.size() > 0) {
            for (Object obj : resultOrigin) {
                if (null == ports[1]) {
                    ports[1] = obj.toString();
                } else {
                    ports[1] = ports[1] + "," + obj.toString();
                }
            }

        } else {
            resultOrigin = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un where un.un_loc_code='" + unLocOrigin + "' ").list();
            for (Object obj : resultDest) {
                if (null == ports[1]) {
                    ports[1] = obj.toString();
                }
            }
        }
        return ports;
    }

    public String getAllSynonymousCityforDest(String unLoc) {
        String ports = null;
        List result = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un JOIN un_location un1 ON un1.un_loc_code='" + unLoc + "' AND un1.synonymous_city='Y' AND un.synonymous_group=un1.synonymous_group AND un.countrycode=un1.countrycode JOIN fcl_buy f ON f.destination_port=un.id GROUP BY un.id").
                list();
        if (null != result && result.size() > 1) {
            for (Object obj : result) {
                if (null == ports) {
                    ports = obj.toString();
                } else {
                    ports = ports + "," + obj.toString();
                }
            }
            return ports;
        }
        return null;
    }

    public String getAllSynonymousCityforOrigin(String terminal) throws Exception {
        String unLoc = StringFormatter.orgDestStringFormatter(terminal);
        String ports = null;
        List result = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un JOIN un_location un1 ON un1.un_loc_code='" + unLoc + "' AND un1.synonymous_city='Y' AND un.synonymous_group=un1.synonymous_group AND un.countrycode=un1.countrycode JOIN fcl_buy f ON f.origin_terminal=un.id GROUP BY un.id").
                list();
        if (null != result && result.size() > 1) {
            for (Object obj : result) {
                if (null == ports) {
                    ports = obj.toString();
                } else {
                    ports = ports + "," + obj.toString();
                }
            }
            return ports;
        }
        return null;
    }

    public String getAllSynonymousCityById(String id) throws Exception {
        String ports = null;
        List result = getCurrentSession().createSQLQuery("SELECT un.id FROM un_location un JOIN un_location un1 ON un1.id='" + id + "' AND un1.synonymous_city='Y' AND un.synonymous_group=un1.synonymous_group AND un.countrycode=un1.countrycode JOIN fcl_buy f ON f.origin_terminal=un.id GROUP BY un.id").
                list();
        if (null != result && result.size() > 1) {
            for (Object obj : result) {
                if (null == ports) {
                    ports = obj.toString();
                } else {
                    ports = ports + "," + obj.toString();
                }
            }
            return ports;
        } else {
            return "" + id;
        }
    }

    public String getAllCountryPorts(String portId) {
        String country = portId.substring(portId.lastIndexOf("/") + 1, portId.indexOf("("));
        String ports = null;
        String queryString = "SELECT CONCAT(dest.id,'==',IF(LOCATE('(',portname)>0,SUBSTRING(portname,1,LOCATE('(',portname)-1),portname)) as city FROM ports dest JOIN fcl_buy buy ON buy.destination_port=dest.id WHERE dest.countryname=?0 GROUP BY dest.id";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameter("0", country);
        List result = query.list();
        if (null != result) {
            for (Object obj : result) {
                if (null == ports) {
                    ports = obj.toString();
                } else {
                    ports = ports + "||" + obj.toString();
                }
            }
        }
        return ports;
    }

    public List<Ports> getDestination(String port) {
        StringBuilder queryBuilder = new StringBuilder("from Ports where eciportcode is not null");
        queryBuilder.append(" and (eciportcode like '").append(port).append("%' or portname like '").append(port).append("%')");
        queryBuilder.append(" order by portname");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public Double getAdjustmentFactor(String dest) throws Exception {
        String queryString = "SELECT fpc.current_adj_factor FROM fcl_port_configuration fpc WHERE fpc.schnum = (SELECT p.id FROM ports p  WHERE p.unlocationcode = '" + dest + "' limit 1) limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("current_adj_factor", DoubleType.INSTANCE);
        Object result = queryObject.uniqueResult();
        return null == result ? 0d : Double.parseDouble(result.toString());
    }

    public Ports getByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Ports.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return (Ports) criteria.setMaxResults(1).uniqueResult();
    }

    public String getConcatenatedPort(String unLocationCode) throws Exception {
        Object concatenatedPorts = getCurrentSession().createSQLQuery("SELECT CONCAT(UPPER(p.portname),'/',UPPER(p.countryname),'(',p.unlocationcode,')') FROM ports p WHERE unlocationcode='" + unLocationCode + "' limit 1").uniqueResult();
        return null != concatenatedPorts ? concatenatedPorts.toString() : "";
    }

    public Ports findByPortNameAndStateCode(String portName, String stateCode) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Ports");
        queryBuilder.append(" where remove_special_characters(portname) = '").append(portName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "")).append("'");
        queryBuilder.append(" and stateCode = '").append(stateCode.trim()).append("'");
        queryBuilder.append(" order by id asc");
        Query query = getCurrentSession().createQuery(queryBuilder.toString());
        query.setMaxResults(1);
        return (Ports) query.uniqueResult();
    }

    public List<Object[]> getCountryListforDestination(String countryName) throws Exception {
        List<Object[]> destinationList = null;
        String queryString = "SELECT un.un_loc_name,gc.codedesc,un.un_loc_code,un.lat,un.lng FROM lcl_relay_fd lrf"
                + " INNER JOIN un_location un ON lrf.fd_id = un.id INNER JOIN genericcode_dup gc ON un.countrycode = gc.id"
                + " LEFT JOIN genericcode_dup gd ON un.statecode = gd.id WHERE gc.codedesc LIKE '" + countryName + "%' GROUP BY un.un_loc_code";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        destinationList = queryObject.list();
        return destinationList;
    }

    public List<Object[]> getCountryListforOrigin(String unlocationcode) throws Exception {
        List<Object[]> destinationList = null;
        //String queryString = "SELECT un.un_loc_name,p.statecode,un.un_loc_code,un.lat,un.lng from un_location un,ports p where un.un_loc_code = p.unlocationcode "
        // + "AND un.un_loc_code LIKE '" + unlocationcode + "%' GROUP BY un.un_loc_code";
        String queryString = "SELECT un.un_loc_name,p.statecode,un.un_loc_code,un.lat,un.lng,gc.codedesc from un_location un JOIN ports p LEFT JOIN  genericcode_dup gc ON un.countrycode = gc.id where un.un_loc_code = p.unlocationcode "
                + "AND un.un_loc_code LIKE '" + unlocationcode + "%' GROUP BY un.un_loc_code";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        destinationList = queryObject.list();
        return destinationList;
    }

    public String getAllSynonymousCities(String cityIds) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(syn.id as char character set latin1) as id");
        queryBuilder.append(" from un_location ori");
        queryBuilder.append(" join un_location syn");
        queryBuilder.append(" on (ori.synonymous_group = syn.synonymous_group");
        queryBuilder.append(" and ori.countrycode = syn.countrycode)");
        queryBuilder.append(" join fcl_buy fcl");
        queryBuilder.append(" on (syn.id = fcl.origin_terminal)");
        queryBuilder.append(" where (ori.id in (").append(cityIds).append(")");
        queryBuilder.append(" and ori.synonymous_city = 'Y')");
        queryBuilder.append(" group by syn.id");
        List<String> synonymousCities = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(synonymousCities)) {
            List<String> oriIds = Arrays.asList(cityIds.split(","));
            for (Iterator<String> cityId = synonymousCities.iterator(); cityId.hasNext();) {
                if (oriIds.contains(cityId.next())) {
                    cityId.remove();
                }
            }
        }
        return CommonUtils.isNotEmpty(synonymousCities) ? synonymousCities.toString().replace("[", "").replace("]", "") : "";
    }

    public List<Object[]> getDoorOriginCity(String cityName, String zipCode) throws Exception {
        List<Object[]> doorOrigin = null;
        String sqlQuery = "SELECT city,state,zip,lat,lng FROM zip_code WHERE city='" + cityName + "' and zip='" + zipCode + "'";
        Query queryObject = getCurrentSession().createSQLQuery(sqlQuery);
        doorOrigin = queryObject.list();
        return doorOrigin;
    }

    public String[] getDefaultAgentForLcl(String unLocationCode, String type) throws Exception {
        String agentValues[] = new String[5];
        StringBuilder sb = new StringBuilder();
        sb.append("select a.agentid ,tp.acct_name,tp.disabled,tp.forward_account,a.lcl_agent_level_brand from agency_info a join ports p on p.id=a.schnum ");
        sb.append("and a.type='").append(type).append("' and a.default_agent='Y' join trading_partner tp on a.agentid= tp.acct_no ");
        sb.append("WHERE p.unlocationcode='").append(unLocationCode).append("'");
        Object queryObject = getCurrentSession().createSQLQuery(sb.toString()).setMaxResults(1).uniqueResult();
        if (queryObject != null) {
            Object[] agentObj = (Object[]) queryObject;
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                agentValues[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                agentValues[1] = agentObj[1].toString();
            }
            if (agentObj[2] != null && !agentObj[2].toString().trim().equals("")) {
                agentValues[2] = agentObj[2].toString();
            }
            if (agentObj[3] != null && !agentObj[3].toString().trim().equals("")) {
                agentValues[3] = agentObj[3].toString();
            }
            if (agentObj[4] != null && !agentObj[4].toString().trim().equals("")) {
                agentValues[4] = agentObj[4].toString();
            }
        }
        return agentValues;
    }

    public Integer getAgentCount(String unlocationCode, String agentType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        BigInteger count = new BigInteger("0");
        queryBuilder.append("select count(*) from agency_info a join ports p on p.id=a.schnum  ");
        queryBuilder.append("and a.type= ").append("'").append(agentType).append("'").append(" join trading_partner tp on a.agentid= tp.acct_no ");
        queryBuilder.append("WHERE p.unlocationcode= ").append("'").append(unlocationCode).append("'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.setMaxResults(1).uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String[] getPortsForLclExportAgentInfo(String pod) throws Exception {
        String agentValues[] = new String[2];
        String queryString = "select a.agentid ,tp.acct_name from agency_info a join ports p on p.id=a.schnum "
                + "and a.type='L' and a.default_agent='Y' join trading_partner tp on a.agentid= tp.acct_no "
                + "WHERE p.unlocationcode=?0 ";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("0", pod);
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                agentValues[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                agentValues[1] = agentObj[1].toString();
            }
        }
        return agentValues;
    }

    public String getShedulenumber(String unlocCode) throws Exception {
        String queryString = "SELECT govschnum FROM ports where unlocationcode='" + unlocCode + "'";
        Object query = (String) getSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        return null != query ? query.toString() : "";
    }

    public List getSheduleNo(String unlocCode) throws Exception {
        String queryString = "SELECT govschnum,CODE FROM ports p JOIN `genericcode_dup` gd ON p.`regioncode`=gd.`id` where unlocationcode='" + unlocCode + "'";
        Query query = getCurrentSession().createSQLQuery(queryString);
        List list = query.list();
        return list;
    }

    public String getInsuranceAllowed() throws Exception {
        String queryString = "SELECT GROUP_CONCAT(p.unlocationcode) FROM fcl_port_configuration fclport JOIN ports p ON p.id=fclport.schnum WHERE fclport.insurance_allowed='N'";
        Object insuranceAllowed = getSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        return null != insuranceAllowed ? insuranceAllowed.toString() : "";
    }

    public Integer findId(String destinationSchdNo) {
        Integer count = new Integer("0");
        String queryString = "SELECT id FROM ports where govschnum='" + destinationSchdNo + "'";
        Object query = getSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        if (query != null) {
            count = (Integer) query;
        }
        return count.intValue();
    }

    public String getDefaultMasterSettings(Integer id) throws Exception {
        String queryString = "SELECT `use_default_master_settings` FROM `fcl_port_configuration` fcl JOIN ports p ON (fcl.schnum=p.`id`) WHERE p.`id`='" + id + "'";
        Object defaultMasterSettings = getSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        return null != defaultMasterSettings ? defaultMasterSettings.toString() : "";
    }

    public TradingPartner getDefaultAgent(String unlocationCode, String type) {
        PersistentClass persistentClass = HibernateSessionFactory.getConfiguration().getClassMapping(TradingPartner.class.getName());
        Iterator<Property> properties = persistentClass.getPropertyIterator();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  t.acct_no as accountno,");
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                queryBuilder.append("  t.").append(((Column) property.getColumnIterator().next()).getName()).append(" as ").append(property.getName()).append(",");
            }
        }
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
        queryBuilder.append(" ");
        queryBuilder.append("from");
        queryBuilder.append("  ports p");
        queryBuilder.append("  join agency_info a");
        queryBuilder.append("    on (");
        queryBuilder.append("      p.id = a.schnum");
        queryBuilder.append("      and a.type = ?0");
        queryBuilder.append("      and a.default_agent = 'Y'");
        queryBuilder.append("    ) ");
        queryBuilder.append("  join trading_partner t");
        queryBuilder.append("     on (a.agentid = t.acct_no) ");
        queryBuilder.append("where");
        queryBuilder.append("  p.unlocationcode = ?1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", type);
        query.setParameter("1", unlocationCode);
        query.addScalar("accountno", StringType.INSTANCE);
        properties = persistentClass.getPropertyIterator();
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                query.addScalar(property.getName(), property.getType());
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(TradingPartner.class));
        query.setMaxResults(1);
        return (TradingPartner) query.uniqueResult();
    }

    public String getPortValue(String columnName, String unlocCode) throws Exception {
        String queryString = "SELECT " + columnName + " as columnName FROM ports where unlocationcode='" + unlocCode + "'";
        SQLQuery query = getSession().createSQLQuery(queryString);
        query.addScalar("columnName", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public String getDefaultAgentNo(String unLocationCode, String type) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  tp.`acct_no` as agentNo ");
        queryBuilder.append("from");
        queryBuilder.append("  `ports` p ");
        queryBuilder.append("  join `agency_info` a ");
        queryBuilder.append("    on (");
        queryBuilder.append("      p.`id` = a.`schnum` ");
        queryBuilder.append("      and a.`type` = :type ");
        queryBuilder.append("      and a.`default_agent` = 'Y' ");
        queryBuilder.append("    )");
        queryBuilder.append("  join `trading_partner` tp ");
        queryBuilder.append("    on (a.`agentid` = tp.`acct_no`) ");
        queryBuilder.append("where p.`unlocationcode` = :unLocationCode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("type", type);
        query.setString("unLocationCode", unLocationCode);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public String[] getSchedNoAndRegion(String unLocationCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  p.`govschnum` as schedNo,");
        queryBuilder.append("  (select r.`code` from `genericcode_dup` r where r.`id` = p.`regioncode`) as region ");
        queryBuilder.append("from");
        queryBuilder.append("  `ports` p ");
        queryBuilder.append("where");
        queryBuilder.append("  p.`unlocationcode` = :unLocationCode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("unLocationCode", unLocationCode);
        query.addScalar("schedNo", StringType.INSTANCE);
        query.addScalar("region", StringType.INSTANCE);
        query.setMaxResults(1);
        Object result = query.uniqueResult();
        if (null != result) {
            Object[] cols = (Object[]) result;
            return Arrays.copyOf(cols, cols.length, String[].class);
        }
        return new String[]{"", ""};
    }

    public String getLaneValue(String portNo) throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append("select LEFT(lane,1) from ").append(databaseSchema).append(".ports where prtnum = :portNo");
        Query query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("portNo", portNo);
        return query.uniqueResult() != null ? query.uniqueResult().toString() : "";
    }

    public String fetchOrginRemarks(String orgin) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  f.`origin_remarks`");
        sb.append("FROM");
        sb.append("  ports p ");
        sb.append("  JOIN fcl_port_configuration f ");
        sb.append("    ON p.id = f.schnum ");
        sb.append("WHERE REPLACE(p.portname, \"'\", \"\") LIKE '");
        sb.append(orgin.replace("'", ""));
        sb.append("%'");
        sb.append("LIMIT 1 ");
        String queryObject = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return null != queryObject ? queryObject.toString() : "";
    }

    public String getEngmet(String unLocCode) throws Exception {
        String query = "SELECT engmet FROM ports WHERE unlocationcode=:unLocCode";
        SQLQuery queryObj = getCurrentSession().createSQLQuery(query);
        queryObj.setParameter("unLocCode", unLocCode);
        String engmet = (String) queryObj.setMaxResults(1).uniqueResult();
        return null != engmet ? engmet : "";
    }

    public String[] getAlternatePorts(String unlocCode) {
        String alternatePort[] = new String[2];
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(lpc.default_port_of_discharge<> '', ");
        queryStr.append(" (SELECT id FROM un_location WHERE un_loc_code=lpc.default_port_unloc LIMIT 1),'') AS podId, ");
        queryStr.append(" IF(lpc.transhipment<> '',(SELECT id FROM un_location WHERE un_loc_code = lpc.default_trashipment_unloc LIMIT 1),'') AS fdId ");
        queryStr.append(" FROM lcl_port_configuration lpc ");
        queryStr.append(" JOIN ports p ON p.id=lpc.schnum  ");
        queryStr.append("  WHERE p.unlocationcode=:unlocCode ");
        SQLQuery queryObj = getCurrentSession().createSQLQuery(queryStr.toString());
        queryObj.setParameter("unlocCode", unlocCode);
        List alternatePortsList = queryObj.list();
        if (alternatePortsList != null && !alternatePortsList.isEmpty()) {
            Object[] alternateObj = (Object[]) alternatePortsList.get(0);
            if (alternateObj[0] != null && !alternateObj[0].toString().trim().equals("")) {
                alternatePort[0] = alternateObj[0].toString();
            }
            if (alternateObj[1] != null && !alternateObj[1].toString().trim().equals("")) {
                alternatePort[1] = alternateObj[1].toString();
            }
        }
        return alternatePort;
    }

    public String brandField(String destination) {
        String queryString = "select f.brand_field from fcl_port_configuration f, ports p where f.schnum=p.id and p.unlocationcode='" + destination + "'";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        Object brandField = queryObject.uniqueResult();
        return null != brandField ? brandField.toString() : "";
    }
}

package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

public class RefTerminalDAO extends BaseHibernateDAO {

    public List findAllTerminals() throws Exception {
        String queryString = " from RefTerminalTemp where trmnum is not null and trmnum!='0' order by trmnum ";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public Iterator getAllTerminalsForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select terminal.trmnum,terminal.terminalLocation from RefTerminal terminal where terminal.trmnum !='' and terminalLocation is not null").list().iterator();
        return results;
    }

    public List<RefTerminal> getAllTerminalsForDisplayForDojo(String trmname) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select terminal_Location as terminalLocation,trmnum from terminal");
        queryBuilder.append(" where fcl_exp_iss_term='Y' and (terminal_location like '").append(trmname).append("%')");
        queryBuilder.append(" order by terminal_location");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.setResultTransformer(Transformers.aliasToBean(RefTerminal.class)).list();
    }

    public List<RefTerminal> getAllTerminalsForDisplayForImportDojo(String trmname) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select terminal_Location as terminalLocation,trmnum from terminal");
        queryBuilder.append(" where actyon='F' and (terminal_location like '").append(trmname).append("%')");
        queryBuilder.append(" order by terminal_location");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.setResultTransformer(Transformers.aliasToBean(RefTerminal.class)).list();
    }

    public List findForManagement(String terminalId, String terminalName, String terminaltype, String city) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        GenericCode genericCode1 = new GenericCode();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (terminalId != null && !terminalId.equals("")) {
            criteria.add(Restrictions.like("trmnum", terminalId + "%"));
            criteria.addOrder(Order.asc("trmnum"));
        }
        if (terminalName != null && !terminalName.equals("")) {
            criteria.add(Restrictions.like("terminalLocation", terminalName + "%"));
            criteria.addOrder(Order.asc("terminalLocation"));
        }
        if (terminaltype != null && !terminaltype.equals("") && !terminaltype.equals("0")) {
            genericCode1 = genericCodeDAO.findById(Integer.parseInt(terminaltype));
            criteria.add(Restrictions.like("genericCode", genericCode1));
            criteria.addOrder(Order.asc("genericCode"));
        }
        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.like("city1", city + "%"));
            criteria.addOrder(Order.asc("city1"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findForManagementaction(String terminalId, String terminalName, String terminaltype, String city) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        GenericCode genericCode1 = new GenericCode();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

        if (terminalId != null && !terminalId.equals("")) {
            criteria.add(Restrictions.ge("trmnum", terminalId));
            criteria.addOrder(Order.asc("trmnum"));
        }
        if (terminalName != null && !terminalName.equals("")) {
            criteria.add(Restrictions.ge("terminalLocation", terminalName));
            criteria.addOrder(Order.asc("terminalLocation"));
        }
        if (terminaltype != null && !terminaltype.equals("") && !terminaltype.equals("0")) {
            genericCode1 = genericCodeDAO.findById(Integer.parseInt(terminaltype));
            criteria.add(Restrictions.ge("genericCode", genericCode1));
            criteria.addOrder(Order.asc("genericCode"));
        }

        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.like("city1", city + "%"));
            criteria.addOrder(Order.asc("city1"));
        }
        return criteria.list();
    }

    // Testing purpose plz remove later
    public List findForTerminalAjaxSuggest(String terminalId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminal.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (terminalId != null && !terminalId.equals("")) {
            criteria.add(Restrictions.like("trmnum", terminalId + "%"));
            criteria.addOrder(Order.asc("trmnum"));
        }
        return criteria.list();
    }

    public List findForManagement(String terminalId, String terminalLoc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (terminalId != null && !terminalId.equals("")) {
            criteria.add(Restrictions.like("trmnum", terminalId + "%"));
            criteria.addOrder(Order.asc("trmnum"));
        }
        if (terminalLoc != null && !terminalLoc.equals("")) {
            criteria.add(Restrictions.like("terminalLocation", terminalLoc + "%"));
            criteria.addOrder(Order.asc("terminalLocation"));
        }
        return criteria.list();
    }

    public List findForExport(String code, String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (code != null && !code.equals("")) {
            criteria.add(Restrictions.like("trmnum", code + "%"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("terminalLocation", codeDesc + "%"));
        }
        criteria.setMaxResults(50);

        criteria.addOrder(Order.asc("terminalLocation"));
        return criteria.list();
    }

    public List findByName(String terminalName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (terminalName != null && !terminalName.equals("")) {
            criteria.add(Restrictions.like("terminalLocation", terminalName + "%"));
            criteria.addOrder(Order.asc("terminalLocation"));
        }
        return criteria.list();
    }

    public List findForManagementac0tion(String terminalId, String terminalName, String terminaltype, String city, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminalTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        GenericCode genericCode1 = new GenericCode();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        UnLocation unLocation = new UnLocation();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        if (terminalId != null && !terminalId.equals("")) {
            criteria.add(Restrictions.ge("trmnum", terminalId));
            criteria.addOrder(Order.asc("trmnum"));
        }
        if (terminalName != null && !terminalName.equals("")) {
            criteria.add(Restrictions.ge("terminalLocation", terminalName));
            criteria.addOrder(Order.asc("terminalLocation"));
        }
        if (terminaltype != null && !terminaltype.equals("") && !terminaltype.equals("0")) {
            genericCode1 = genericCodeDAO.findById(Integer.parseInt(terminaltype));
            criteria.add(Restrictions.like("genericCode", genericCode1));
            criteria.addOrder(Order.asc("genericCode"));
        }
        if (city != null && !city.equals("")) {

            criteria.add(Restrictions.ge("city1", city));
            criteria.addOrder(Order.asc("city1"));

        }
        return criteria.list();
    }

    public void save(RefTerminal transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public RefTerminal findById(String terminalid) throws Exception {
        if (null != terminalid && !terminalid.trim().equals("")) {
            RefTerminal instance = (RefTerminal) getSession().get("com.gp.cong.logisoft.domain.RefTerminal", terminalid);
            return instance;
        }
        return null;
    }

    public RefTerminal impfindById(String importTerminalid) throws Exception {
        RefTerminal instance = (RefTerminal) getSession().get("com.gp.cong.logisoft.domain.RefTerminal", importTerminalid);
        return instance;
    }

    public RefTerminalTemp findById1(String terminalid) throws Exception {
        RefTerminalTemp instance = (RefTerminalTemp) getSession().get("com.gp.cong.logisoft.domain.RefTerminalTemp", terminalid);
        return instance;
    }

    public void update(RefTerminal persistanceInstance, String userName) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(RefTerminal persistanceInstance, String userName) throws Exception {
        getSession().delete(persistanceInstance);
    }

    public Iterator getTerminalNumbersForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select trmnum from RefTerminal").list().iterator();
        return results;
    }

    public List<RefTerminal> findTermNumber(String termNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from RefTerminal where trmnum like '" + termNo + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List findTermNumber1(String termNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from RefTerminalTemp where trmnum like '" + termNo + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List findTermNumber2(String termNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from RefTerminalTemp where trmnam like '" + termNo + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public String getReferenceLocation(String terminalNumber) throws Exception {
        String query = "select unLocationCode1 from terminal where trmnum = '" + terminalNumber + "'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getFCLExportMasterShipper(String terminalNumber) throws Exception {
        String query = "select zaccount from terminal where trmnum = '" + terminalNumber + "'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getTrmnum(String unLocationCode, String actyon) throws Exception {
        String query = "select trmnum FROM terminal WHERE unLocationCode1='" + unLocationCode + "' and actyon ='" + actyon + "' AND trmnum <'70' LIMIT 1";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getFclDocumentDeptEmail(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select doc_dept_email from terminal ");
        queryBuilder.append(" where trmnum = ");
        queryBuilder.append("(select substring_index(billing_terminal,'-', -1) as trmnum from fcl_bl ");
        queryBuilder.append(" where file_no = '").append(fileNo).append("')");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
    }

    public RefTerminal getByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(RefTerminal.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return (RefTerminal) criteria.uniqueResult();
    }

    public RefTerminal getTerminalByUnLocation(String unLocationCode, String actyon) throws Exception {
        String queryString = "FROM RefTerminal WHERE unLocationCode1 ='" + unLocationCode + "' AND actyon='" + actyon + "'";
        Query query = getCurrentSession().createQuery(queryString);
        return (RefTerminal) query.setMaxResults(1).uniqueResult();
    }

    public RefTerminal getTerminal(String terminalNo) throws Exception {
        String queryString = "FROM RefTerminal WHERE trmnum =:trmnum";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("trmnum", terminalNo);
        return (RefTerminal) query.setMaxResults(1).uniqueResult();
    }

    public List<String> getAllTerminalsForCOBReport() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  concat(t.`terminal_location`, '-', t.`trmnum`) as terminalLocation ");
        queryBuilder.append("from");
        queryBuilder.append("  `terminal` t ");
        queryBuilder.append("where ");
        queryBuilder.append("  t.`trmnum` in ('01', '05', '08', '09', '15', '18', '19', '79') ");
        queryBuilder.append("order by terminalLocation");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("terminalLocation", StringType.INSTANCE);
        return query.list();
    }


    public String getfileNumber(String fileNumberId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  CONCAT(t.`trmnum`, '-', fn.`fileNumber`) AS fileNumber,");
        sb.append("  fn.unCode,");
        sb.append("  fn.TYPE,");
        sb.append("  fn.fileId ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    fn.`id` AS fileId,");
        sb.append("    fn.`file_number` AS fileNumber,");
        sb.append("    ul.`un_loc_code` AS unCode,");
        sb.append("    IF(");
        sb.append("      bk.`rate_type` = 'R',");
        sb.append("      'Y',");
        sb.append("      bk.`rate_type`");
        sb.append("    ) AS TYPE ");
        sb.append("  FROM");
        sb.append("    `lcl_file_number` fn ");
        sb.append("    JOIN `lcl_booking` bk ");
        sb.append("      ON (fn.`id` = bk.`file_number_id`) ");
        sb.append("    JOIN `un_location` ul ");
        sb.append("      ON (ul.`id` = bk.`poo_id`)) AS fn ");
        sb.append("  JOIN `terminal` t ");
        sb.append("    ON (fn.unCode = t.`unLocationCode1`) ");
        sb.append(" WHERE fn.fileId = :fileId ");
        sb.append("  AND fn.TYPE IN ('C', 'N', 'Y', 'F') ");
        sb.append("LIMIT 1  ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileId", fileNumberId);
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }

    public String getActYon(String terminalNo, String ratesFromTerminalNo, String rateType) throws Exception {
        Object actYonValues;
        if ("F".equals(rateType)) {
            String queryString = "SELECT IF(actyon ='F','1', '0')AS actyons FROM terminal WHERE trmnum =:terminalNo  OR  trmnum=:ratesFromTerminalNo GROUP BY actyons HAVING actyons<>0";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setParameter("terminalNo", terminalNo);
            query.setParameter("ratesFromTerminalNo", ratesFromTerminalNo);          
            actYonValues = query.setMaxResults(1).uniqueResult();
            actYonValues = "1".equals(actYonValues) ? "F" : "N";
        } else {
            String queryString = "select actyon from terminal where trmnum = :terminalNo";
            SQLQuery query = getCurrentSession().createSQLQuery(queryString);
            query.setParameter("terminalNo", terminalNo);
            actYonValues = query.setMaxResults(1).uniqueResult();
        }
        return null != actYonValues ? actYonValues.toString() : "";
    }

}

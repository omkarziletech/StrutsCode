package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.GenericCodeBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.FclBl;
import com.logiware.accounting.model.SalesModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class Genericcode.
 *
 * @see com.gp.cong.logisoft.hibernate.dao.GenericCode
 * @author MyEclipse - Hibernate Tools
 */
public class GenericCodeDAO extends BaseHibernateDAO {

    // property constants
    public static final String CODETYPEID = "codetypeid";
    public static final String CODE = "code";
    public static final String CODEDESC = "codedesc";
    public static final String FIELD1 = "field1";
    public static final String FIELD2 = "field2";
    public static final String FIELD3 = "field3";
    public static final String FIELD4 = "field4";
    public static final String FIELD5 = "field5";
    public static final String FIELD6 = "field6";

    public void save(GenericCode transientInstance, String userName) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void save(GenericCode transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public GenericCode findById1(Integer vesselNo) throws Exception {
        GenericCode instance = (GenericCode) getSession().get(
                "com.gp.cong.logisoft.domain.GenericCode", vesselNo);
        return instance;
    }

    public void delete(GenericCode persistentInstance, String userName) throws Exception {
        getSession().delete(persistentInstance);
    }

    public void delete(GenericCode persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
    }

    public GenericCode findById(java.lang.Integer id) throws Exception {
        GenericCode instance = (GenericCode) getSession().get(
                "com.gp.cong.logisoft.domain.GenericCode", id);
        return instance;
    }

    public Integer getRowCount(GenericCode genericCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", genericCode.getCodetypeid()));
        if (CommonUtils.isNotEmpty(genericCode.getCode())) {
            criteria.add(Restrictions.like("code", "%" + genericCode.getCode() + "%"));
        }
        if (CommonUtils.isNotEmpty(genericCode.getCodedesc())) {
            criteria.add(Restrictions.like("codedesc", "%" + genericCode.getCodedesc() + "%"));
        }
        criteria.setProjection(Projections.rowCount());
        return Integer.parseInt(criteria.uniqueResult().toString());
    }

    public List<GenericCode> getGenericCodes(GenericCode genericCode, String sortBy, String orderBy, int start, int limit) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", genericCode.getCodetypeid()));
        if (CommonUtils.isNotEmpty(genericCode.getCode())) {
            criteria.add(Restrictions.like("code", "%" + genericCode.getCode() + "%"));
        }
        if (CommonUtils.isNotEmpty(genericCode.getCodedesc())) {
            criteria.add(Restrictions.like("codedesc", "%" + genericCode.getCodedesc() + "%"));
        }
        if ("asc".equals(orderBy)) {
            criteria.addOrder(Order.asc(sortBy));
        } else {
            criteria.addOrder(Order.desc(sortBy));
        }
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    public List<GenericCode> getGenericCodes(Integer codeTypeId, String code, String description) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        if (CommonUtils.isNotEmpty(code)) {
            criteria.add(Restrictions.like("code", code + "%"));
        }
        if (CommonUtils.isNotEmpty(description)) {
            criteria.add(Restrictions.like("codedesc", description + "%"));
        }
        return criteria.list();
    }

    public GenericCode getId(String code) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public GenericCode getSpecialCode(String code, String field) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("codetypeid", 22));
        criteria.add(Restrictions.eq("field1", field));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public Iterator getAllGenericCodesForDisplay(Integer codeTypeId, String desc) throws Exception {
        String queryString = "";

        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by code,codedesc";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        // queryObject.setMaxResults(50);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCostCodesForDisplay(Integer codeTypeId,
            String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        // queryObject.setMaxResults(50);
        return queryObject.list().iterator();
    }

    public List getAllGenericCostCodesForDisplay(Integer codeTypeId) throws Exception {
        List codeList = new ArrayList();
        String queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        for (Object codes : queryObject.list()) {
            Object[] code = (Object[]) codes;
            codeList.add(new LabelValueBean(code[1].toString(), code[0].toString()));
        }
        return codeList;
    }

    public Iterator getAllGenericCostCodesForTypeOfMove(Integer codeTypeId, String code, String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,code,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCostCodesForTypeOfMovebooking(Integer codeTypeId, String code, String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "SELECT id,code,codedesc FROM GenericCode WHERE codetypeid=?0 AND codedesc NOT IN('RAMP TO DOOR','RAMP TO PORT','RAMP TO RAIL') ORDER BY codedesc";
        } else {
            queryString = "SELECT id,code,codedesc FROM GenericCode WHERE codetypeid=?0 AND codedesc NOT IN('RAMP TO DOOR','RAMP TO PORT','RAMP TO RAIL') order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCostCodesForCorrections(Integer codeTypeId, String code, String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,code,codedesc from GenericCode where codetypeid=?0 order by code";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericUnitTypeForDisplay(Integer codeTypeId,
            String desc) throws Exception {
        String queryString = "";

        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCosttypeForDisplay(Integer codeTypeId,
            String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllNotesForDisplay(Integer codeTypeId, String desc) throws Exception {
        String queryString = "";
        queryString = "select codedesc,codedesc from GenericCode where codetypeid=?0 order by codedesc";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllNotesForDisplay1(Integer codeTypeId) throws Exception {
        String queryString = "";
        queryString = "select code,code from GenericCode where codetypeid=?0 order by id";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllNotesForDisplay2(Integer codeTypeId) throws Exception {
        String queryString = "";
        queryString = "select code,codedesc from GenericCode where codetypeid=?0 order by code";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllWeekForDisplay(Integer codeTypeId, String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 order by code";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public List getConsolidatorList() throws Exception {
        List consolidatorList = new ArrayList();
        String queryString = "from GenericCode where codetypeid='36'";
        Query queryObject = getSession().createQuery(queryString);
        consolidatorList = queryObject.list();
        return consolidatorList;
    }

    public List getUnitTypesList() throws Exception {
        List consolidatorList = new ArrayList();
        String queryString = "from GenericCode where codetypeid='38'";
        Query queryObject = getSession().createQuery(queryString);
        consolidatorList = queryObject.list();
        return consolidatorList;
    }

    public Iterator getAllWeekForDisplayForFlightShedule(Integer codeTypeId,
            String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,code,codedesc from GenericCode where codetypeid=?0 order by code";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCodesForDisp1(Integer codeTypeId, String desc,
            StringBuffer oceanIds) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 and id not in ";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 and id not in ";
        }
        queryString = queryString + "(" + oceanIds.toString() + ") order by codedesc";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from GenericCode as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByCodeTypeid(Integer id) throws Exception {
        String queryString = "from GenericCode where codetypeid=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public List findByCodeTypeDestination(Integer Id, String description) throws Exception {
        String queryString = "SELECT gc.codedesc FROM genericcode_dup AS gc JOIN codetype AS ct ON gc.Codetypeid=ct.codetypeid WHERE ct.Codetypeid='" + Id + "' or ct.description='" + description + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public GenericCode findByCodeName(String code, Integer codetypeid) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("codetypeid", codetypeid));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public GenericCode findByCodeDescName(String codedesc, Integer codetypeid) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codedesc", codedesc.trim()));
        criteria.add(Restrictions.eq("codetypeid", codetypeid));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List findByCodetypeid(Object codetypeid) throws Exception {
        return findByProperty(CODETYPEID, codetypeid);
    }

    public List findByCode(Object code) throws Exception {
        return findByProperty(CODE, code);
    }

    public List findByCodedesc(Object codedesc) throws Exception {
        return findByProperty(CODEDESC, codedesc);
    }

    public List findByField1(Object field1) throws Exception {
        return findByProperty(FIELD1, field1);
    }

    public List findByField2(Object field2) throws Exception {
        return findByProperty(FIELD2, field2);
    }

    public List findByField3(Object field3) throws Exception {
        return findByProperty(FIELD3, field3);
    }

    public List findByField4(Object field4) throws Exception {
        return findByProperty(FIELD4, field4);
    }

    public List findByField5(Object field5) throws Exception {
        return findByProperty(FIELD5, field5);
    }

    public List findByField6(Object field6) throws Exception {
        return findByProperty(FIELD6, field6);
    }

    public GenericCode merge(GenericCode detachedInstance) throws Exception {
        GenericCode result = (GenericCode) getSession().merge(
                detachedInstance);
        return result;
    }

    public void attachDirty(GenericCode instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public List findByExample(GenericCode instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cong.logisoft.domain.GenericCode").add(
                Example.create(instance)).list();
        return results;
    }

    public void attachClean(GenericCode instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findforTableData(int codeTypeId, String codeValue,
            String codeDesc) throws Exception {
        List genericData = null;
        if (codeTypeId != 0) {
            genericData = getCurrentSession().createQuery(
                    "select generic.id,generic.code,codetype.description,generic.codedesc,generic.field1,generic.field2,generic.field3,generic.field4,generic.field5,generic.field6 from GenericCode generic,Codetype codetype where generic.codetypeid=codetype.codetypeid and generic.codetypeid=" + codeTypeId + " and  generic.code like '" + codeValue + "%' and generic.codedesc like '" + codeDesc + "%'").list();
        }
        if (codeTypeId == 0) {
            genericData = getCurrentSession().createQuery(
                    "select generic.id,generic.code,codetype.description,generic.codedesc,generic.field1,generic.field2,generic.field3,generic.field4,generic.field5,generic.field6 from GenericCode generic,Codetype codetype where generic.codetypeid=codetype.codetypeid and generic.code like '" + codeValue + "%'" + " and generic.codedesc like '" + codeDesc + "%'").list();
        }
        return genericData;
    }

    public List findGenericSearchData(int codeTypeId, String codeValue,
            String codeDesc) throws Exception {
        List genericData = null;
        genericData = getCurrentSession().createQuery("from GenericCode generic where "
                + "  generic.code like '" + codeValue + "%'" + " and generic.codedesc like '" + codeDesc + "%'"
                + " order by code asc").list();
        return genericData;
    }

    public List findGenericCode(int codeTypeId, String codeValue, String codeDesc) throws Exception {
        List genericData = null;
        genericData = getCurrentSession().createQuery("from GenericCode generic where codetypeid=" + codeTypeId
                + " and generic.code like '" + codeValue + "%'" + " and generic.codedesc like '" + codeDesc + "%'"
                + " order by code asc").list();
        return genericData;
    }

    public List getAllChargeCodes() throws Exception {
        List chargeCodeList = null;
        String queryString = "from GenericCode";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setMaxResults(50);
        chargeCodeList = queryObject.list();
        return chargeCodeList;
    }

    public List<GenericCode> findForGenericAction(int codeTypeId, String codeValue,
            String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeTypeId > 0) {
            criteria.add(Restrictions.eq("codetypeid", codeTypeId));
            criteria.addOrder(Order.asc("codetypeid"));
        }

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue));
            criteria.addOrder(Order.asc("code"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));

        }
        criteria.addOrder(Order.asc("codedesc"));
        criteria.setMaxResults(100);
        return criteria.list();
    }

    public List findForShowAll() throws Exception {
        GenericCodeBean objGenericCodeBean = null;
        List<GenericCodeBean> lstGenericIfo = new ArrayList<GenericCodeBean>();
        String queryString = "select generic.id,generic.code,generic.codedesc,codetype.description from GenericCode generic,Codetype codetype where generic.codetypeid=codetype.codetypeid order by codetype.description";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            objGenericCodeBean = new GenericCodeBean();
            Object[] row = (Object[]) iter.next();

            /**
             * id1 is local variable id is id in GenericCode table strCode is
             * code in GenericCode table strDesc is codedesc in GenericCode
             * table strCodedesc is description in Codetype table
             */
            String id = String.valueOf(row[0]);
            String strCode = (String) row[1];
            String strDesc = (String) row[2];
            String strCodedesc = (String) row[3];
            objGenericCodeBean.setId(id);
            objGenericCodeBean.setCode(strCode);
            objGenericCodeBean.setDesc(strDesc);
            objGenericCodeBean.setCodedesc(strCodedesc);
            lstGenericIfo.add(objGenericCodeBean);
            objGenericCodeBean = null;
        }

        return lstGenericIfo;
    }

    public void update(GenericCodeBean persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public void updateFieldByCodeAndType(String columnName, String value, String codeDescription, String code) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update genericcode_dup set ").append(columnName).append(" = '").append(value).append("' where Codetypeid = (select codetypeid from codetype where description = '").append(codeDescription).append("') and  CODE = '").append(code).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public Iterator getAllcountriesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.code,generic.codedesc from GenericCode generic where generic.codetypeid=11").list().iterator();
        return results;
    }

    public Iterator getCountryForDisplay(String s) throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.codedesc from GenericCode generic where generic.codetypeid=11 and generic.code=" + s).list().iterator();
        return results;
    }

    // done by pravin--17.01.08
    public Iterator getAllRegionsForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.code,generic.codedesc from GenericCode generic where generic.codetypeid=19").list().iterator();
        return results;
    }

    public Iterator getAllUomCodes() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.code,generic.codedesc from GenericCode generic where generic.codetypeid=71").list().iterator();
        return results;
    }

    public Iterator getAllSpecialCodesForDisplay(Integer codeTypeId,
            String desc, String field) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,code,codedesc from GenericCode where codetypeid=?0 and field1=?1 order by code";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 and field1=?1 order by code";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        queryObject.setParameter("1", field);
        return queryObject.list().iterator();
    }

    public Iterator getAllRegionsForShowall(String Value) throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select generic.codedesc from GenericCode generic where generic.codetypeid=19 and generic.code=" + Value).list().iterator();
        return results;
    }

    public List findForAirRatesDup(String codeValue, String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));

        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }

        criteria.add(Restrictions.ge("code", codeValue));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findForAirRates(String codeValue, String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        criteria.add(Restrictions.eq("codetypeid", new Integer(4)));
        if (codeValue != null) {
            criteria.add(Restrictions.ge("code", codeValue));
        }
        criteria.setMaxResults(500);
        return criteria.list();

        // return criteria.setMaxResults(50).list();
    }

    public List<Integer> getLastRecords(String codeType) throws Exception {
        String queryString = "select max(id) FROM GenericCode WHERE   codetypeid='" + codeType + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findForChargeCodesForAirRates(String codeValue,
            String codeDesc, String codeType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
        }
        Integer codeTypeId = null;
        if (codeType != null && !codeType.equals("")) {
            codeTypeId = Integer.parseInt(codeType);
        }
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.addOrder(Order.asc("codedesc"));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findForChargeCodesForAirRates(String codeValue,
            String codeDesc, String codeType, Integer pageNo, Integer pageSize) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        Integer codeTypeId = null;
        if (codeType != null && !codeType.equals("")) {
            codeTypeId = Integer.parseInt(codeType);
        }
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.setFirstResult(pageSize * (pageNo - 1)).setMaxResults(pageSize);
        return criteria.list();
    }

    public Integer importRemarksSize(String codeValue,
            String codeDesc, String codeType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        Integer codeTypeId = null;
        if (codeType != null && !codeType.equals("")) {
            codeTypeId = Integer.parseInt(codeType);
        }
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.setMaxResults(50);
        return null != criteria.list() ? criteria.list().size() : 0;
    }

    public List<GenericCode> findForCommodityCode(String codeValue, String codeType, boolean isBulletRates) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (isBulletRates) {
            queryBuilder.append("select code,codedesc from genericcode_dup where field10='Y' and (code like '");
            queryBuilder.append(codeValue).append("%' or codedesc like '").append(codeValue).append("%')");
            queryBuilder.append(" and Codetypeid='").append(codeType).append("'").append(" limit 50");
        } else {
            queryBuilder.append("select code,codedesc from genericcode_dup where ");
            queryBuilder.append("((Field7='FCL' or Field7='BOTH') and (code like '");
            queryBuilder.append(codeValue).append("%' or codedesc like '").append(codeValue).append("%'))");
            queryBuilder.append("and (field10 != 'Y' or field10 is null)");
            queryBuilder.append("and Codetypeid='").append(codeType).append("'").append(" limit 50");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return query.setResultTransformer(Transformers.aliasToBean(GenericCode.class)).list();
    }

    public List<LabelValueBean> findForAllCommodityCode(String sslineNos, String originTerminal, String destinationPort) throws Exception {
        List<LabelValueBean> bulletRates = new ArrayList<LabelValueBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  f.ssline_no,");
        queryBuilder.append("  concat(g.code, '<-->', g.codedesc) ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_buy f");
        queryBuilder.append("  join genericcode_dup g");
        queryBuilder.append("    on f.com_num = g.id ");
        queryBuilder.append("where f.origin_terminal in (").append(originTerminal).append(")");
        queryBuilder.append("  and f.destination_port in (").append(destinationPort).append(")");
        queryBuilder.append("  and f.ssline_no in (").append(sslineNos).append(")");
        queryBuilder.append("  and g.field10 = 'Y' ");
        queryBuilder.append("group by f.ssline_no, g.id ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List<Object> result = query.list();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object row : result) {
                Object[] col = (Object[]) row;
                bulletRates.add(new LabelValueBean((String) col[1], (String) col[0]));
            }
        }
        return bulletRates;
    }

    public List findChargeCodesForApplyPayments(String codeValue,
            String codeDesc, String codeType, String field7) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        if (field7 != null && !field7.equals("")) {
            criteria.add(Restrictions.like("field7", field7 + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        Integer codeTypeId = null;
        if (codeType != null && !codeType.equals("")) {
            codeTypeId = Integer.parseInt(codeType);
        }
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findCodesForContactConfig(String codeType, String field1) throws Exception {
        Integer codeTypeid = Integer.parseInt(codeType);
        List list = new ArrayList();
        String queryString = " from GenericCode where codetypeid='" + codeTypeid + "' and field1='" + field1 + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List findForGenericCode(String codeValue) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (codeValue != null && !codeValue.equals("")) {
            criteria.add(Restrictions.like("code", codeValue + "%"));
            criteria.addOrder(Order.asc("code"));
        }

        criteria.add(Restrictions.eq("codetypeid", new Integer(4)));

        return criteria.list();
    }

    public List findForGenericCode1(String codeValue) throws Exception {
        String codeType = "23";
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.like("code", codeValue + "%"));
        criteria.addOrder(Order.asc("code"));
        Integer codeTypeId = Integer.parseInt(codeType);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        return criteria.list();
    }

    public Iterator getAllGenericCodesForWeightRange(Integer codeTypeId,
            String desc, StringBuffer oceanIds) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 and id not in " : "select id,code from GenericCode where codetypeid=?0 and id not in ";
        if (oceanIds != null) {
            queryString = queryString + "(" + oceanIds.toString() + ") order by codedesc";
        }
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllGenericCodesForFCLWeightRange(Integer codeTypeId,
            String desc) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 " : "select id,code from GenericCode where codetypeid=?0 ";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    // QUERY FOR COMMODITY SPECIFIC ACESSORIAL CHARGES
    public Iterator getAllGenericCodesForAGSS(Integer codeTypeId, String desc,
            StringBuffer oceanIds) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 and id not in " : "select id,code from GenericCode where codetypeid=?0 and id not in ";
        queryString = queryString + "(" + oceanIds.toString() + ")";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    // QUERY FOR ADDFCL UNIT TYPE
    public Iterator getAllUnitCodeForFCL(Integer codeTypeId, String desc,
            StringBuffer oceanIds) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 and id not in " : "select id,code from GenericCode where codetypeid=?0 and id not in ";
        queryString = queryString + "(" + oceanIds.toString() + ")";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllUnitCodeForFCLTest(Integer codeTypeId, String desc) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 " : "select id,code from GenericCode where codetypeid=?0 ";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public Iterator getAllUnitCodeForFCLTest1(Integer codeTypeId, String desc) throws Exception {
        String queryString = desc.equals("yes") ? "select codedesc,codedesc from GenericCode where codetypeid=?0 " : "select id,code from GenericCode where codetypeid=?0 ";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    // QUERY FOR STANDARD SPECIFIC ACESSORIAL CHARGES
    public Iterator getAllGenericCodesForAGSS1(Integer codeTypeId, String desc,
            StringBuffer oceanIds) throws Exception {
        String queryString = desc.equals("yes") ? "select id,codedesc from GenericCode where codetypeid=?0 and id not in " : "select id,code from GenericCode where codetypeid=?0 and id not in ";
        queryString = queryString + "(" + oceanIds.toString() + ")";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public GenericCode findByCodeDesc(String codedesc, Integer codetypeid) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.like("codedesc", "%" + codedesc + "%"));
        criteria.add(Restrictions.eq("codetypeid", codetypeid));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List findGenericCode(String codeType, String codeValue) throws Exception {
        if (codeType != null && codeType.equals("2")) {
            codeType = "36";
        }
        Integer codeTpe = Integer.parseInt(codeType);
        String queryString = "from GenericCode where codetypeid=?0 and code=?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", codeTpe);
        queryObject.setParameter("1", codeValue);
        return queryObject.list();
    }

    // for batches in Accounting Module
    public Iterator getSourceLedgers() throws Exception {
        return getCurrentSession().createQuery(
                "select generic.code from GenericCode generic where generic.codetypeid=17").list().iterator();
    }

    public Iterator getScanDetails(Integer codetypeid) throws Exception {
        return getCurrentSession().createQuery(
                "select generic.codedesc,generic.field1 from GenericCode generic where generic.codetypeid=" + codetypeid).list().iterator();
    }

    public Iterator getDocumentType(Integer codetypeid) throws Exception {
        return getCurrentSession().createQuery(
                "select generic.codedesc,generic.code from GenericCode generic where generic.codetypeid=" + codetypeid).list().iterator();
    }

    public Iterator getCurrency() throws Exception {
        return getCurrentSession().createQuery(
                "select generic.codedesc from GenericCode generic where generic.codetypeid=32").list().iterator();
    }

    public Iterator getSourcecode() throws Exception {
        return getCurrentSession().createQuery(
                "select generic.id,generic.code from GenericCode generic where generic.codetypeid=33").list().iterator();
    }

    // for GC Report
	/*
     * public List getCodesForcodeTypeId(String codetypeid,String desc) { List
     * <GcCodeDTO> gclist=new ArrayList<GcCodeDTO>();
     *
     * StringqueryString=
     * "select gc.code,gc.codedesc from GenericCode gc where gc.codetypeid='"
     * +codetypeid+"'"; List queryObject =
     * getCurrentSession().createQuery(queryString).list();
     *
     * Iterator itr = queryObject.iterator(); GcCodeDTO gcdto=null; while
     * (itr.hasNext()) { gcdto=new GcCodeDTO(); Object[] row = (Object[])
     * itr.next(); String code=(String)row[0]; String des=(String)row[1];
     * gcdto.setCode(code); gcdto.setDescription(des); gclist.add(gcdto);
     * gcdto=null;
     *
     * }
     *
     *
     * return gclist; }
     */
    public List getAllUnitCodeForFCLTestforList(Integer codeTypeId) throws Exception {
        String queryString = "select id from GenericCode where codetypeid=?0 ";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list();
    }

    public List getAllUnitCodeForFCLTestforListforFclRates(Integer codeTypeId) throws Exception {
        String queryString = "from GenericCode where codetypeid=?0 ";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list();
    }

    public List getAllCommentCodesForReports() throws Exception {
        String queryString = "select gc.codedesc from GenericCode gc where gc.codetypeid='39' and gc.code like 'QC%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List getAllCommentCodesForImportReports() throws Exception {
        String queryString = "select gc.codedesc from GenericCode gc where gc.codetypeid='39' and gc.code like 'IQ%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List getAllCommentCodesForInvoiceReports() throws Exception {
        String queryString = "select gc.codedesc from GenericCode gc where gc.codetypeid='39' and gc.code like 'IN%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List getAllCommentCodesForBookingReports() throws Exception {
        String queryString = "SELECT gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'BC%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list();
    }

    public Iterator getAllCommentForArrivalNoticeReports() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'AN%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public String getcommentcodeDescforReport(String code) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("codetypeid", 39));
        criteria.setProjection(Projections.property("codedesc"));
        criteria.setMaxResults(1);
        return (String) criteria.uniqueResult();
    }

    public List getDisclaimerForQuoationReport() throws Exception {
        String queryString = "select gc.codedesc from GenericCode gc where gc.codetypeid='39' and gc.code='RC001' or gc.code='RC002' or gc.code='RC003' or gc.code='RC004' or gc.code='RC005' or gc.code='RC006' or gc.code='RC007' or gc.code='RC008'";
        List resultsList = getCurrentSession().createQuery(queryString).list();
        return resultsList;
    }

    public List getCommentListForFclBlReport() throws Exception {
        String queryString = "select gc.codedesc from GenericCode gc where gc.codetypeid='39' and gc.code='BL101' or gc.code='BL102' or gc.code='BL200' or gc.code='BL201' or gc.code='AN200' or gc.code='AN300' or gc.code='AN301' or gc.code='AN200' or gc.code='AN302'"
                + " or gc.code='AN400' or gc.code='AN401' or gc.code='AN402' or gc.code='AN403' or gc.code='AN404' or gc.code='AN405' or gc.code='AN406' or gc.code='AN407' or gc.code='AN408'  or gc.code='AN409'  or gc.code='MB100' or gc.code='BL100' or gc.code='FI102'"
                + " or gc.code='FI100' or gc.code='FI101' or gc.code='MB101'";
        List resultsList = getCurrentSession().createQuery(queryString).list();
        return resultsList;
    }

    public Iterator getCommentsListForBlReport() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'BL%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public Iterator getCommentsList() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc,gc.field1 FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'BL%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public Iterator getCommentsListForCTS() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc,gc.field1 FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'CTS%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public Iterator getCommentsListForStreamShipReport() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'MB%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public Iterator getCommentsListForFreightInvoiceReport() throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='39' AND gc.code LIKE 'FI%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public Iterator getAllGenericCodesForUni(Integer codeTypeId, String desc,
            StringBuffer oceanIds) throws Exception {
        String queryString = "";
        if (desc.equals("yes")) {
            queryString = "select id,codedesc from GenericCode where codetypeid=?0 and id not in";
        } else {
            queryString = "select id,code from GenericCode where codetypeid=?0 and id not in";
        }
        queryString = queryString + "(" + oceanIds.toString() + ")";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", codeTypeId);
        return queryObject.list().iterator();
    }

    public List findForVessel(String vesselNo, String vesselName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        Integer codetypeid = 14;
        if (vesselNo != null && !vesselNo.equals("")) {
            criteria.add(Restrictions.like("code", vesselNo + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        if (vesselName != null && !vesselName.equals("")) {
            criteria.add(Restrictions.like("codedesc", vesselName + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        criteria.add(Restrictions.eq("codetypeid", codetypeid));
        criteria.addOrder(Order.asc("codetypeid"));

        return criteria.list();
    }

    public List findForVesselNo(String vesselNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        Integer codetypeid = 14;
        if (vesselNo != null && !vesselNo.equals("")) {
            criteria.add(Restrictions.like("code", vesselNo + "%"));
            criteria.addOrder(Order.asc("code"));
        }
        criteria.add(Restrictions.eq("codetypeid", codetypeid));
        criteria.addOrder(Order.asc("codetypeid"));
        return criteria.list();
    }

    public Iterator getAllUserDetails(Integer userId, String desc) throws Exception {
        String queryString = "";
        if (desc.equals("yes") && !userId.equals(new Integer(35))) {
            queryString = "select userId,loginName from User order by loginName";
        } else {
            queryString = "select userId,loginName from User  order by userId";
        }
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list().iterator();
    }

    public List getCode(String codeTypeId) throws Exception {
        String queryString = "select code from GenericCode where codetypeid='4' and code like '" + codeTypeId + "%'";
        List resultsList = getCurrentSession().createQuery(queryString).setMaxResults(50).list();
        return resultsList;
    }

    public List getCodeDescForPackType(String codeDesc) throws Exception {
        String queryString = "from GenericCode where codetypeid='49' and codedesc like '" + codeDesc + "%'";
        List resultsList = getCurrentSession().createQuery(queryString).setMaxResults(50).list();
        return resultsList;
    }

    public List getFieldForPackType(String field1) throws Exception {
        String queryString = "from GenericCode where codetypeid='49' and Field1 like '" + field1 + "%'";
        List resultsList = getCurrentSession().createQuery(queryString).setMaxResults(50).list();
        return resultsList;
    }

    public List getFieldForSchedbNo(String code) throws Exception {
        String queryString = "from GenericCode where codetypeid='68' and code like '" + code + "%'";
        List resultsList = getCurrentSession().createQuery(queryString).setMaxResults(50).list();
        return resultsList;
    }

    public List getFieldForSchedbName(String codedesc) throws Exception {
        String queryString = "from GenericCode where codetypeid='68' and codedesc like ?0";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("0", "%" + codedesc + "%");
        query.setMaxResults(50);
        List resultsList = query.list();
        return resultsList;
    }

    public List<String> getPropertyFieldList() throws Exception {
        String queryString = "select distinct field1 from GenericCode where codetypeid='65' order by id asc";
        List resultsList = getCurrentSession().createQuery(queryString).list();
        return resultsList;
    }

    public String getChargeCodeDescForCharges(String id) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("id", Integer.parseInt(id)));
        criteria.setProjection(Projections.property("codedesc"));
        criteria.setMaxResults(1);
        return (String) criteria.uniqueResult();
    }

    public String getCostCodebyCostCode(String costCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", costCode));
        criteria.add(Restrictions.eq("codetypeid", 36));
        criteria.setProjection(Projections.property("code"));
        criteria.setMaxResults(1);
        String result = (String) criteria.uniqueResult();
        return null != result && !result.trim().isEmpty() ? result : "error";
    }

    public String getCodeDescById(String id, String codeTypeId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("id", Integer.parseInt(id)));
        criteria.add(Restrictions.eq("codetypeid", Integer.parseInt(codeTypeId)));
        criteria.setProjection(Projections.property("code"));
        criteria.setMaxResults(1);
        String result = (String) criteria.uniqueResult();
        return null != result && !result.trim().isEmpty() ? result : "Due Upon Receipt";
    }

    public String getCodeDescription(String codeTypeId, String code) throws Exception {
        String queryString = "select codedesc from genericcode_dup where codetypeid=?0 and code=?1 limit 1";
        Query query = getSession().createSQLQuery(queryString);
        query.setParameter("0", codeTypeId);
        query.setParameter("1", code);
        Object returnString = query.uniqueResult();
        return null != returnString ? returnString.toString() : "";
    }

    public String getCode(String codeTypeId, String codedesc) throws Exception {
        String queryString = "select codedesc from GenericCode where codetypeid=?0 and codedesc=?1";
        Query query = getSession().createQuery(queryString);
        query.setParameter("0", codeTypeId);
        query.setParameter("1", codedesc);
        query.setMaxResults(1);
        String returnString = (String) query.uniqueResult();
        return null != returnString ? returnString : "";
    }

    public String getFieldByCodeAndCodetypeId(String codeDescription, String code, String field) throws Exception {
        String queryString = "Select " + field + " from genericcode_dup where codetypeid=(select codetypeid from codetype where description = '" + codeDescription + "') and code= '" + code + "' ";
        String returnString = null != getSession().createSQLQuery(queryString).uniqueResult() ? getSession().createSQLQuery(queryString).uniqueResult().toString() : "";
        return returnString;
    }

    public String getFieldByCodeDescAndCodetypeId(String codeTypeId, String codeDesc, String field) throws Exception {
        String queryString = "Select " + field + " from GenericCode where codetypeid='" + codeTypeId + "' and codedesc='" + codeDesc + "'";
        String returnString = null != getSession().createQuery(queryString).uniqueResult() ? getSession().createQuery(queryString).uniqueResult().toString() : "";
        return returnString;
    }

    public String getPackageType(String codeTypeId, String codeDesc) throws Exception {
        String queryString = "Select field1 from genericcode_dup where codetypeid=(select codetypeid from codetype where description = '" + codeTypeId + "') and codedesc like '" + codeDesc + "%' limit 1";
        String returnString = null != getSession().createSQLQuery(queryString).uniqueResult() ? getSession().createSQLQuery(queryString).uniqueResult().toString() : "";
        return returnString;
    }

    public List<GenericCode> findByCodeNameByInOpreator(Integer codeTypeid, String code, FclBl fclBl) throws Exception {
        if (fclBl != null && !CommonFunctions.isNotNull(fclBl.getAgentNo())) {
            code = code.replace(",'B',", ",");
        }
        String queryString = "FROM GenericCode WHERE codetypeid=" + codeTypeid + " AND code IN(" + code + ")";

        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List<GenericCode> findByCodeNameByInOpreator(Integer codeTypeid, String code, String agentNo) throws Exception {
        if (!CommonFunctions.isNotNull(agentNo)) {
            code = code.replace(",'B',", ",");
        }
        String queryString = "FROM GenericCode WHERE codetypeid=" + codeTypeid + " AND code IN(" + code + ")";

        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findChargeCodes(String codeValue, String codeDesc, String codeType) throws Exception {
        StringBuilder queryBilder = new StringBuilder(" from GenericCode where (");
        Boolean isBefore = false;
        if (codeValue != null && !codeValue.equals("")) {
            queryBilder.append(" code like '").append(codeValue).append("%'");
            isBefore = true;
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            if (isBefore) {
                queryBilder.append(" or ");
            }
            queryBilder.append(" codedesc like '").append(codeDesc).append("%'");
        }
        queryBilder.append(")");
        if (codeType != null && !codeType.equals("")) {
            queryBilder.append(" and codetypeid = ").append(codeType);
        }
        Query query = getCurrentSession().createQuery(queryBilder.toString());
        query.setMaxResults(50);
        return query.list();
    }

    public GenericCode getGenericCodeId(String codeTypeId, String code) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("codetypeid", Integer.parseInt(codeTypeId)));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List<GenericCode> getAllEventCode() throws Exception {
        String codetypeIdGetter = "SELECT c.codetypeid FROM codetype c WHERE c.description='EventCode' limit 1";
        int codetypeid = (Integer) getCurrentSession().createSQLQuery(codetypeIdGetter).uniqueResult();
        String queryString = "from GenericCode where codetypeid='" + codetypeid + "'";
        List resultsList = getCurrentSession().createQuery(queryString).setMaxResults(50).list();
        return resultsList;
    }

    public List<GenericCode> getProperties(String field1) throws Exception {
        String locGetter = "SELECT codetypeid FROM codetype WHERE description='Property Management' limit 1";
        int loc = (Integer) getCurrentSession().createSQLQuery(locGetter).uniqueResult();
        String queryString = "from GenericCode where codetypeid='" + loc + "' and field1='" + field1 + "'";
        List resultsList = getCurrentSession().createQuery(queryString).list();
        return resultsList;
    }

    public List<GenericCode> getAllLPropertiesCode() throws Exception {
        String locGetter = "SELECT codetypeid FROM codetype WHERE description='Property Management' limit 1";
        int loc = (Integer) getCurrentSession().createSQLQuery(locGetter).uniqueResult();
        String queryString = "from GenericCode where codetypeid='" + loc + "'";
        List resultsList = getCurrentSession().createQuery(queryString).list();
        return resultsList;
    }

    public String getRegionRemarks(int codeTypeId, String code, String module) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.add(Restrictions.eq("code", code));
        criteria.setProjection(Projections.property("field1"));
        criteria.setMaxResults(1);
        String result = (String) criteria.uniqueResult();
        return null == result ? "" : result;
    }

    public GenericCode findByCodeDesc(String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codedesc", codeDesc));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List findByProperties(List<String> properties, List<String> operators, List<Object> value) throws Exception {
        Criteria criteria = getSession().createCriteria(GenericCode.class);
        int i = 0;
        for (String property : properties) {
            if (CommonUtils.isEqual(operators.get(i), "=")) {
                criteria.add(Restrictions.eq(property, value.get(i)));
            }
            if (CommonUtils.isEqual(operators.get(i), "like")) {
                criteria.add(Restrictions.like(property, value.get(i) + "%"));
            }
            if (CommonUtils.isEqual(operators.get(i), "<")) {
                criteria.add(Restrictions.lt(property, value.get(i)));
            }
            if (CommonUtils.isEqual(operators.get(i), ">")) {
                criteria.add(Restrictions.gt(property, value.get(i)));
            }
            if (CommonUtils.isEqual(operators.get(i), "<=")) {
                criteria.add(Restrictions.le(property, value.get(i)));
            }
            if (CommonUtils.isEqual(operators.get(i), ">=")) {
                criteria.add(Restrictions.ge(property, value.get(i)));
            }
            if (CommonUtils.isEqual(operators.get(i), "!=") || CommonUtils.isEqual(operators.get(i), "<>")) {
                criteria.add(Restrictions.ne(property, value.get(i)));
            }
            i++;
        }
        return criteria.list();
    }

    public List<String> getUnitCostTypeListInOrder() throws Exception {
        String queryString = "select distinct ge.codedesc FROM genericcode_dup ge WHERE ge.Codetypeid =38";
        return getSession().createSQLQuery(queryString).addScalar("ge.codedesc", StringType.INSTANCE).list();
    }

    public List<String> codesToShownInGetRatesExpand() {
        List<String> chargeList = new ArrayList<String>();
        String query = "Select UPPER(codedesc) from GenericCode where codetypeid='' and field1='on'";
        chargeList = getCurrentSession().createQuery(query).list();
        return chargeList;
    }

    public List<Object[]> getChargeCode(String code, String codedesc, String shipmentType, String transactionType) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT g.code, g.codedesc FROM genericcode_dup g, gl_mapping gl");
        queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        queryString.append(" AND g.code=gl.charge_code AND gl.Shipment_Type='").append(shipmentType).append("' AND gl.Transaction_Type='").append(transactionType).append("'");
        queryString.append(" AND gl.bluescreen_chargecode is not null AND gl.bluescreen_chargecode != ''");
        queryString.append(" AND g.code <> 'INSURE'");
        if ("FCLI".equals(shipmentType)) {
            queryString.append(" AND (g.field9 is null or g.field9 not in ('B','I'))");
        } else {
            queryString.append(" AND (g.field9 is null or g.field9 not in ('B','E'))");
        }
        queryString.append(" AND (g.code like'").append(code).append("%' OR g.codedesc like '").append(codedesc).append("%') GROUP BY g.codedesc limit 50");
        return getCurrentSession().createSQLQuery(queryString.toString()).list();
    }

    public List<Object[]> getChargeCodeId(String code, String codedesc, String shipmentType, String transactionType) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT g.id, g.codedesc FROM genericcode_dup g, gl_mapping gl");
        queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        if ("FCLI".equals(shipmentType)) {
            queryString.append(" AND (g.field9 is null or g.field9 not in ('B','I'))");
        } else {
            queryString.append(" AND (g.field9 is null or g.field9 not in ('B','E'))");
        }
        queryString.append(" AND g.code=gl.charge_code AND g.code <> 'INSURE' AND gl.Shipment_Type='").append(shipmentType).append("' AND gl.Transaction_Type='").append(transactionType).append("' GROUP BY g.codedesc order by g.codedesc");
        return getCurrentSession().createSQLQuery(queryString.toString()).list();
    }

    public boolean checkCostCodeInGeneralLedger(String code, String shipmentType) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT g.codedesc FROM genericcode_dup g, gl_mapping gl");
        queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        queryString.append(" AND g.code=gl.charge_code AND gl.Shipment_Type='").append(shipmentType).append("' AND gl.Transaction_Type='AC'");
        queryString.append(" AND g.code like '").append(code).append("%'");
        List resultList = getCurrentSession().createSQLQuery(queryString.toString()).list();
        if (!resultList.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean checkCostCodeHasBlueScreenCodeInGeneralLedger(String code, String shipmentType) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT if(count(*) > 0, 'true', 'false') FROM genericcode_dup g, gl_mapping gl");
        queryString.append(" WHERE g.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Cost Code' LIMIT 1)");
        queryString.append(" AND g.code=gl.charge_code AND gl.Shipment_Type='").append(shipmentType).append("' AND gl.Transaction_Type='AC'");
        queryString.append(" AND g.code like '").append(code).append("%'");
        queryString.append(" AND gl.bluescreen_chargecode <> ''");
        String result = (String) getCurrentSession().createSQLQuery(queryString.toString()).uniqueResult();
        return null != result ? Boolean.valueOf(result) : false;
    }

    public GenericCode getGenericCodeByCode(String code) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public String getGenericCodeDesc(String code) throws Exception {
        if (null != code && !code.equals("")) {
            String queryString = "SELECT codedesc FROM genericcode_dup WHERE codetypeid = (SELECT codetypeid FROM codetype WHERE description ='Cost Code') AND CODE = '" + code + "'";
            String returnString = null != getSession().createSQLQuery(queryString).uniqueResult() ? getSession().createSQLQuery(queryString).uniqueResult().toString() : "";
            return returnString;
        }
        return "";
    }

    public List<GenericCode> getAllCommodity() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", 4));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public List<GenericCode> getAllPackageType() {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", 49));
        criteria.addOrder(Order.asc("code"));
        return criteria.list();
    }

    public List<GenericCode> getGenericCodeDesc(String code, String codedesc) throws Exception {
        List<GenericCode> codes = null;
        String queryString = "from GenericCode where codetypeid='" + code + "' and field1='" + codedesc + "'";
        Query query = getCurrentSession().createQuery(queryString);
        codes = query.list();
        return codes;
    }

    public List<GenericCode> getGenericCode(String code, String codedesc) throws Exception {
        List<GenericCode> codes = null;
        String queryString = "from GenericCode where codetypeid='" + code + "' and code='" + codedesc + "'";
        Query query = getCurrentSession().createQuery(queryString);
        codes = query.list();
        return codes;
    }

    public List findGenericCodeDatas(int codeTypeId, String codeValue, String codeDesc) throws Exception {
        List genericData = null;
        if (null != codeDesc && !codeDesc.trim().isEmpty()) {
            genericData = getCurrentSession().createQuery("from GenericCode generic where generic.codetypeid=" + codeTypeId
                    + " and generic.code like '%" + codeValue + "%'" + " and generic.codedesc like '%" + codeDesc + "%'"
                    + " order by code asc").list();
        } else {
            genericData = getCurrentSession().createQuery("from GenericCode generic where generic.codetypeid=" + codeTypeId
                    + " and generic.code like '%" + codeValue + "%'" + " order by code asc").list();
        }
        return genericData;
    }

    public void saveCodeFromGlMapping(String code, String codeDesc) throws Exception {
        GenericCode genericCode = new GenericCode();
        genericCode.setCode(code);
        genericCode.setCodedesc(codeDesc);
        genericCode.setCodetypeid(new CodetypeDAO().getCodeTypeId("Cost Code"));
        save(genericCode);
    }

    public GenericCode getGenericCode(String codeDesc, Integer codeTypeId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codedesc", codeDesc));
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public GenericCode getGenericCodeByCode(String code, Integer codeTypeId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List<GenericCode> getNvoMoveTypes(int codeTypeId, boolean isRamp) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        String[] rampTypes = new String[]{"RAMP TO DOOR", "RAMP TO PORT", "RAMP TO RAIL"};
        if (isRamp) {
            criteria.add(Restrictions.in("codedesc", rampTypes));
        } else {
            criteria.add(Restrictions.not(Restrictions.in("codedesc", rampTypes)));
        }
        criteria.addOrder(Order.asc("codedesc"));
        return criteria.list();
    }

    public String getByCodeAndCodetypeId(String codeTypeId, String code, String field) throws Exception {
        String queryString = "Select " + field + " from GenericCode where codetypeid='" + codeTypeId + "' and code='" + code + "'";
        String returnString = null != getSession().createQuery(queryString).uniqueResult() ? getSession().createQuery(queryString).uniqueResult().toString() : "";
        return returnString;
    }

    public Iterator getLclPrintComments(int codeTypeId, String code) throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM GenericCode gc WHERE gc.codetypeid='" + codeTypeId + "' AND gc.code LIKE '" + code + "%' ORDER BY gc.code asc";
        return getCurrentSession().createQuery(queryString).list().iterator();
    }

    public List getLclPrintComment(int codeTypeId, String code) throws Exception {
        String queryString = "SELECT gc.code, gc.codedesc FROM genericcode_dup gc ";
        queryString += "WHERE gc.codetypeid=:codeTypeId AND gc.code LIKE :code ORDER BY gc.code asc";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setInteger("codeTypeId", codeTypeId);
        query.setParameter("code", code + "%");
        return query.list();
    }

    //-- get generic code Ids based on codedesc --//
    public String getGenericodeIdList(String codeDesc) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT CAST(GROUP_CONCAT(id) AS CHAR CHARACTER SET latin1) AS id FROM genericcode_dup WHERE codedesc IN ('").append(codeDesc).append("')");
        Object result = (String) getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public String getCodeDescription(String id) throws Exception {
        String queryString = "select codedesc from genericcode_dup where id = " + id;
        return getCurrentSession().createSQLQuery(queryString).uniqueResult().toString();
    }

    public List<LabelValueBean> getRegions() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select codedesc as description,");
        queryBuilder.append("id as id");
        queryBuilder.append(" from genericcode_dup");
        queryBuilder.append(" where codetypeid = (select codetypeid from codetype where description = 'Region Codes')");
        queryBuilder.append(" order by codedesc");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        List<LabelValueBean> regions = new ArrayList<LabelValueBean>();
        regions.add(new LabelValueBean("Select One", ""));
        for (Object row : result) {
            Object[] col = (Object[]) row;
            regions.add(new LabelValueBean(CommonUtils.capitalize(col[0].toString()), col[1].toString()));
        }
        return regions;
    }

    public String getAllUnits(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT GROUP_CONCAT(codedesc) FROM genericcode_dup WHERE id IN");
        queryBuilder.append("(SELECT unit_type FROM charges WHERE qoute_id = ");
        queryBuilder.append("(SELECT quote_id FROM quotation WHERE file_no ='").append(fileNo).append("')");
        queryBuilder.append("GROUP BY unit_type)");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result && !result.isEmpty() ? result : "";
    }

    public Object[] getUnitTypeByCode(int codeTypeId, String code, String module) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("codetypeid", codeTypeId));
        criteria.add(Restrictions.eq("code", code));
        criteria.addOrder(Order.asc("id"));
        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.property("field1"));
        projList.add(Projections.property("field2"));
        criteria.setProjection(Projections.distinct(projList));
        criteria.setMaxResults(1);
        Object result = criteria.uniqueResult();
        return null != result ? (Object[]) result : null;
    }

    public GenericCode getId(String code, String field1) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq("code", code));
        criteria.add(Restrictions.eq("field1", field1));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public String getCreditStatus(String acctNo) throws Exception {
        String query = "SELECT gd.codedesc FROM cust_accounting ca JOIN genericcode_dup gd ON gd.id=ca.credit_status WHERE ca.acct_no='" + acctNo + "'";
        String creditStatus = (String) getCurrentSession().createSQLQuery(query).uniqueResult();
        return null != creditStatus ? creditStatus : "";
    }

    public String getCreditStatusForImports(String acctNo) throws Exception {
        String query = "SELECT gd.codedesc FROM cust_accounting ca JOIN genericcode_dup gd ON gd.id=ca.credit_status WHERE ca.acct_no='" + acctNo + "' and ca.import_credit='Y'";
        String creditStatus = (String) getCurrentSession().createSQLQuery(query).uniqueResult();
        return null != creditStatus ? creditStatus : "No Credit";
    }

    public String getScanSopForImports(String acctNo) throws Exception {
        String query = "SELECT IF(COUNT(*) > 0, 'SOP', '') FROM document_store_log WHERE screen_name = 'TRADINGPARTNER' AND document_name = 'SOP' AND document_id = '" + acctNo + "'";
        String scanSop = (String) getCurrentSession().createSQLQuery(query).uniqueResult();
        return scanSop;
    }

    public GenericCode findByPropertyForChargeCode(String propertyName, Integer value, String chargeCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.add(Restrictions.eq("code", chargeCode));
        criteria.setMaxResults(1);
        return (GenericCode) criteria.uniqueResult();
    }

    public List getBookingGenericCodesForUnits(String bol) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select u.id,u.codedesc from fcl_bl f join booking_fcl b on (f.file_no = b.file_no) join bookingfcl_units bu ");
        queryBuilder.append("on (b.bookingnumber = bu.bookingnumber) join genericcode_dup u on (bu.unittype = u.id) where f.bol =").append(bol);
        queryBuilder.append(" group by u.id");
        Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public String getDescFromCode(String codeDesc) throws Exception {
        String queryString = "select code from genericcode_dup where codetypeid = '49' and codedesc= '" + codeDesc + "'";
        String result = getCurrentSession().createSQLQuery(queryString).uniqueResult().toString();
        return null != result ? result : "";
    }

    public boolean displayBulletRatesRemarks(String commcode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT if(isnull(b.id), 0, 1) as result FROM genericcode_dup c LEFT JOIN fclbuyothercomm o ");
        queryBuilder.append(" ON (c.id = o.commodity_code) LEFT JOIN genericcode_dup b ON (b.id = o.base_commodity_code ");
        queryBuilder.append(" AND b.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Commodity Codes') AND b.field10 = 'Y')");
        queryBuilder.append(" WHERE c.code = '").append(commcode).append("'");
        queryBuilder.append(" AND c.codetypeid = (SELECT codetypeid FROM codetype WHERE description = 'Commodity Codes')");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public List getCountryList(String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(GenericCode.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("codedesc", codeDesc + "%"));
            criteria.addOrder(Order.asc("codedesc"));
        }
        criteria.add(Restrictions.eq("codetypeid", 11));
        criteria.setMaxResults(100);
        return criteria.list();
    }

    public List<SalesModel> getSalesManagers() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  `codedesc` as managerName,");
        queryBuilder.append("  `field3` as managerEmail,");
        queryBuilder.append("  group_concat(`field4`) as rsmEmail,");
        queryBuilder.append("  group_concat(`code`) as salesCode,");
        queryBuilder.append("  group_concat(`id`) as salesId ");
        queryBuilder.append("from");
        queryBuilder.append("  `genericcode_dup` ");
        queryBuilder.append("where `codetypeid` = (select `codetypeid` from `codetype` where `description` = 'Sales Code')");
        queryBuilder.append("  and `codedesc` <> '' ");
        queryBuilder.append("  and `field3` <> '' ");
        queryBuilder.append("group by `codedesc`,");
        queryBuilder.append("  `field3`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("managerName", StringType.INSTANCE);
        query.addScalar("managerEmail", StringType.INSTANCE);
        query.addScalar("rsmEmail", StringType.INSTANCE);
        query.addScalar("salesCode", StringType.INSTANCE);
        query.addScalar("salesId", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(SalesModel.class));
        return query.list();
    }

    public String getImportCredit(String acctNo) throws Exception {
        String queryString = "SELECT import_credit FROM cust_accounting WHERE acct_no = :acctNo limit 1";
        String creditStatus = "";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("acctNo", acctNo);
        creditStatus = (String) query.uniqueResult();
        return null != creditStatus ? creditStatus : "";
    }

    public List<LabelValueBean> getRegionsByCode() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select codedesc as description,");
        queryBuilder.append("code as code");
        queryBuilder.append(" from genericcode_dup");
        queryBuilder.append(" where codetypeid = (select codetypeid from codetype where description = 'Region Codes')");
        queryBuilder.append(" order by codedesc");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        List<LabelValueBean> regions = new ArrayList<LabelValueBean>();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            regions.add(new LabelValueBean(CommonUtils.capitalize(col[0].toString()), col[1].toString()));
        }
        return regions;
    }

    public List<String> getCode(int codeTypeId) throws Exception {
        String queryString = "select code from genericcode_dup where codetypeid=:codeTypeId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setInteger("codeTypeId", codeTypeId);
        return query.list();
    }

    public List<LabelValueBean> getCodebyCodeTypeId(int codeTypeId, String field1) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select codedesc as description,");
        queryBuilder.append("code as code");
        queryBuilder.append(" from genericcode_dup");
        queryBuilder.append(" where codetypeid = :codeTypeId");
        queryBuilder.append(" and field1=:field1 ");
        queryBuilder.append(" order by codedesc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setInteger("codeTypeId", codeTypeId);
        query.setString("field1", field1);
        List<Object> result = query.list();
        List<LabelValueBean> code = new ArrayList<LabelValueBean>();
        code.add(new LabelValueBean("Select", ""));
        for (Object row : result) {
            Object[] col = (Object[]) row;
            code.add(new LabelValueBean(CommonUtils.capitalize(col[0].toString()), col[1].toString()));
        }
        return code;
    }

    public String getCodeByCodeType(String codeDesc, String codeTypeDesc) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT g.code AS code FROM genericcode_dup g WHERE g.codedesc=:codeDesc AND ");
        queryStr.append(" g.codetypeid=(SELECT codetypeid FROM codetype WHERE description=:codeTypeDesc) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setString("codeDesc", codeDesc);
        query.setString("codeTypeDesc", codeTypeDesc);
        query.addScalar("code", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }
    public String findByChargeCode(String chargeCode) {
        String query = "SELECT id FROM `genericcode_dup` WHERE CODE=:chargeCode LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("chargeCode", chargeCode);
        return queryObject.uniqueResult().toString();
    }

    public List findIdByCodedesc() throws Exception {
        String query = "SELECT id FROM `genericcode_dup` WHERE codedesc IN('In Good Standing','Net 30 Days')";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        return queryObject.list();
    }
    public void updateGenericcodeDupDeleted(Integer Id, User loginUser) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update genericcode_dup_deleted set deleted_by = '").append(loginUser.getLoginName()).append("' where genericcode_dup_id = ").append(Id);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }
}

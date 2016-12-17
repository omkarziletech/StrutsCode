package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.form.SearchUnLocationForm;
import org.hibernate.SQLQuery;

public class UnLocationDAO extends BaseHibernateDAO {

    public void save(UnLocation transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public Iterator getAllCityForDisplay(GenericCode countryId) throws Exception {
        Iterator iter = null;
        String queryString = "select id,unLocationName from UnLocation where countryId=?0 order by unLocationName";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", countryId);
        iter = queryObject.list().iterator();
        return iter;
    }

    public Iterator getAllCitiesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select id,unLocationName from UnLocation order by unLocationName").list().iterator();
        return results;
    }

    public UnLocation findById(Integer id) throws Exception {
        UnLocation instance = (UnLocation) getCurrentSession().get("com.gp.cong.logisoft.domain.UnLocation", id);
        return instance;
    }

    public Iterator getAllUnCityCodesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select id,unLocationCode from UnLocation where unLocationCode is not null order by unLocationCode").list().iterator();

        return results;
    }

    public Iterator getAllUnCityCodeForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select id,unLocationName from UnLocation order by unLocationName").list().iterator();
        return results;
    }

    public List findAllRoles() throws Exception {
        String queryString = "  from Role order by roleDesc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findForManagement(String code, String codeDesc) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(UnLocation.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (code != null && !code.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", code + "%"));
        }
        if (codeDesc != null && !codeDesc.equals("")) {
            criteria.add(Restrictions.like("unLocationName", codeDesc + "%"));
        }
        criteria.setMaxResults(50);
        criteria.addOrder(Order.asc("unLocationName"));
        return criteria.list();
    }

    public List findRoleName(Role roleId) throws Exception {
        List list = new ArrayList();
        String queryString = "from User where role=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", roleId);
        list = queryObject.list();
        return list;
    }

    public void delete(Role persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public void update(Role persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }
    //city

    public List getCity(String s) throws Exception {
        List results = null;
        results = getCurrentSession().createQuery(
                "select unl.unLocationName from UnLocation unl where unl.id=" + s).list();
        return results;
    }
    //hydoffice

    public List findbyCity(String city) throws Exception {
        List results = null;
        Criteria criteria = getCurrentSession().createCriteria(UnLocation.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (city != null && !city.equals("")) {
            criteria.add(Restrictions.like("unLocationName", city + "%"));
        }
        criteria.setMaxResults(50);
        results = criteria.list();
        return results;
    }

    public List findCity(String city) throws Exception {
        return findbyCity(city);
    }

    public UnLocation getUnlocation(String unLocationCode) throws Exception {
        Criteria criteria = getSession().createCriteria(UnLocation.class);
        criteria.add(Restrictions.eq("unLocationCode", unLocationCode));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (UnLocation) criteria.uniqueResult();
    }

    public List<UnLocation> getUnLocationList(SearchUnLocationForm searchUnLocationForm) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(UnLocation.class);
        criteria.createAlias("countryId", "country");
        if (CommonUtils.isNotEmpty(searchUnLocationForm.getUnLocationCode())) {
            criteria.add(Restrictions.like("unLocationCode", searchUnLocationForm.getUnLocationCode() + "%"));
        }
        if (CommonUtils.isNotEmpty(searchUnLocationForm.getUnLocationName())) {
            criteria.add(Restrictions.like("unLocationName", searchUnLocationForm.getUnLocationName() + "%"));
        }
        if (CommonUtils.isNotEmpty(searchUnLocationForm.getCountryName())) {
            criteria.add(Restrictions.like("country.codedesc", searchUnLocationForm.getCountryName() + "%"));
        }
        if (CommonUtils.isNotEmpty(searchUnLocationForm.getStateName())) {
            criteria.createAlias("stateId", "state");
            criteria.add(Restrictions.like("state.codedesc", searchUnLocationForm.getStateName() + "%"));
        }
        return criteria.list();
    }

    public void updatePorts(UnLocation unLocation) throws Exception {
        String query = "update Ports set portname=\"" + unLocation.getUnLocationName() + "\" where unLocationCode = \"" + unLocation.getUnLocationCode() + "\"";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public void updateUnlocation(String blNumbering, boolean expressRelease, boolean doNotexpressRelease, boolean originalsReleasedAtDestination, boolean originalsRequired, boolean memoHouseBillofLading, String unLocCode) throws Exception {
        if (blNumbering == null) {
            blNumbering = "N";
        }
        String queryStr = "update un_location set bl_numbering=:blNumbering,express_release=:expressRele,do_not_express_release=:donotExpressRele,originals_released_at_destination=:release,originals_required=:original,memo_house_bill_of_lading=:memoHouseBillLading where un_loc_code =:unloc";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setParameter("blNumbering", blNumbering);
        query.setParameter("expressRele", expressRelease);
        query.setParameter("donotExpressRele", doNotexpressRelease);
        query.setParameter("release", originalsReleasedAtDestination);
        query.setParameter("original", originalsRequired);
        query.setParameter("memoHouseBillLading", memoHouseBillofLading);
        query.setParameter("unloc", unLocCode);
        query.executeUpdate();
    }

    public String getDestinationTempRemarks(String unlocCode) throws Exception {
        String queryString = "SELECT f.temporary_text FROM ports p JOIN fcl_port_configuration f ON p.id = f.schnum WHERE REPLACE(p.portname,\"\'\",\"\") LIKE '" + unlocCode.replace("'", "") + "%' AND p.port_city = 'Y' limit 1";
        Object tempObj = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != tempObj ? tempObj.toString() : "";
    }

    public String getDestinationGRIRemarks(String unlocCode) throws Exception {
        String queryString = "SELECT f.special_remarks_for_quot FROM ports p JOIN fcl_port_configuration f ON p.id = f.schnum WHERE REPLACE(p.portname,\"\'\",\"\") LIKE '" + unlocCode.replace("'", "") + "%' AND p.port_city = 'Y' limit 1";
        Object griRemarks = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != griRemarks ? griRemarks.toString() : "";
    }

    public String getDestinationGRIRemarks(String portName, String ssline, String unlocCode) throws Exception {
        String remarks = "";
        StringBuilder queryBuilder = new StringBuilder();
        if (null != unlocCode && !unlocCode.equals("")) {
            queryBuilder.append("SELECT f.special_remarks_for_quot FROM ports p JOIN fcl_port_configuration f ON p.id = f.schnum WHERE REPLACE(p.portname,\"\'\",\"\") LIKE '").append(portName.replace("'", "")).append("%' AND p.port_city = 'Y' limit 1");
            Object griRemarks = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
            remarks = null != griRemarks ? griRemarks.toString() : "";
            queryBuilder.setLength(0);
        }
        String database = LoadLogisoftProperties.getProperty("elite.database.name");
        if (null != database) {
            queryBuilder.append("SELECT t.rmks1,t.rmks2,t.rmks3,t.rmks4,t.rmks5,t.rmks6,t.rmks7,t.rmks8,t.rmks9,t.rmks10 FROM ").append(database).append(".tliperm t ");
            queryBuilder.append("LEFT JOIN ports p ON t.cyex01 != p.govschnum AND t.cyex02 != p.govschnum AND t.cyex03 != p.govschnum AND t.cyex04 != p.govschnum AND ");
            queryBuilder.append("t.cyex05 != p.govschnum AND t.cyex06 != p.govschnum AND t.cyex07 != p.govschnum AND t.cyex08 != p.govschnum AND t.cyex09 != p.govschnum ");
            queryBuilder.append("AND t.cyex10 != p.govschnum AND t.cyex11 != p.govschnum AND t.cyex12 != p.govschnum AND t.cyex13 != p.govschnum AND t.cyex14 != p.govschnum ");
            queryBuilder.append("AND t.cyex15 != p.govschnum AND (t.cyin01 = p.govschnum OR t.cyin02 = p.govschnum OR t.cyin03 = p.govschnum OR t.cyin04 = p.govschnum OR ");
            queryBuilder.append("t.cyin05 = p.govschnum OR t.cyin06 = p.govschnum OR t.cyin07 = p.govschnum OR t.cyin08 = p.govschnum OR t.cyin09 = p.govschnum OR t.cyin10 = ");
            queryBuilder.append("p.govschnum OR t.cyin11 = p.govschnum OR t.cyin12 = p.govschnum OR t.cyin13 = p.govschnum OR t.cyin15 = p.govschnum  OR t.cyex14 = p.govschnum ");
            queryBuilder.append("OR (t.cyin01 = '00000' AND t.cyin02 = '00000' AND t.cyin03 = '00000' AND t.cyin04 = '00000' AND t.cyin05 = '00000' AND t.cyin06 = '00000' AND ");
            queryBuilder.append("t.cyin07 = '00000' AND t.cyin08 = '00000' AND t.cyin09 = '00000' AND t.cyin10 = '00000' AND t.cyin11 = '00000' AND t.cyin12 = '00000' AND ");
            queryBuilder.append("t.cyin13 = '00000' AND t.cyin15 = '00000' AND t.cyex14 = '00000')) LEFT JOIN trading_partner tp ON tp.ssline_number = t.ssline LEFT JOIN ");
            queryBuilder.append("genericcode_dup gc ON gc.id = p.regioncode  WHERE tp.acct_no = '").append(ssline).append("'  AND p.unlocationcode = '").append(unlocCode).append("' ");
            queryBuilder.append("AND gc.code = t.gblreg and p.govschnum != '' AND p.govschnum IS NOT NULL");
            Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
            List l = queryObject.list();
            if (!l.isEmpty()) {
                Object[] obj = (Object[]) l.get(0);
                if (null != obj[0] && !"".equals(obj[0].toString())) {
                    remarks = remarks + "\n" + obj[0].toString();
                }
                if (null != obj[1] && !"".equals(obj[1].toString())) {
                    remarks = remarks + "\n" + obj[1].toString();
                }
                if (null != obj[2] && !"".equals(obj[2].toString())) {
                    remarks = remarks + "\n" + obj[2].toString();
                }
                if (null != obj[3] && !"".equals(obj[3].toString())) {
                    remarks = remarks + "\n" + obj[3].toString();
                }
                if (null != obj[4] && !"".equals(obj[4].toString())) {
                    remarks = remarks + "\n" + obj[4].toString();
                }
                if (null != obj[5] && !"".equals(obj[5].toString())) {
                    remarks = remarks + "\n" + obj[5].toString();
                }
                if (null != obj[6] && !"".equals(obj[6].toString())) {
                    remarks = remarks + "\n" + obj[6].toString();
                }
                if (null != obj[7] && !"".equals(obj[7].toString())) {
                    remarks = remarks + "\n" + obj[7].toString();
                }
                if (null != obj[8] && !"".equals(obj[8].toString())) {
                    remarks = remarks + "\n" + obj[8].toString();
                }
                if (null != obj[9] && !"".equals(obj[9].toString())) {
                    remarks = remarks + "\n" + obj[9].toString();
                }
            }
        }
        return remarks;
    }

    public String getOriginRemarks(String code) throws Exception {
        Object originRemarks = "";
        if (null != code && !code.equals("")) {
            String queryString = "SELECT origin_remarks FROM fcl_port_configuration WHERE schnum = (SELECT id FROM ports WHERE unlocationcode = '" + code + "' limit 1) limit 1";
            Query query = getCurrentSession().createSQLQuery(queryString);
            originRemarks = null != query ? query.uniqueResult() : "";
        }
        return null != originRemarks ? originRemarks.toString() : "";
    }

    public String noDuplicateunLocationCode(String uncode) throws Exception {
        String results = "";
        if (null != uncode && !uncode.equals("")) {
            String queryString = "SELECT un_loc_code FROM un_location WHERE un_loc_code='" + uncode + "' limit 1";
            results = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        }
        return results;
    }

    public List findForUnlocCode(String unCityCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(UnLocation.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (unCityCode != null && !unCityCode.equals("")) {
            criteria.add(Restrictions.like("unLocationCode", unCityCode + "%"));
            criteria.addOrder(Order.asc("unLocationCode"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from UnLocation as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public Integer getUnlocationIdByTrmnum(String trmnum) throws Exception {
        Integer unLocationId = 0;
        String queryString = "select uc.id from un_location uc,terminal t where t.unLocationCode1=uc.un_loc_code AND t.trmnum = ?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", trmnum);
        Object obj = queryObject.uniqueResult();
        if (obj != null) {
            unLocationId = (Integer) obj;
        }
        return unLocationId;
    }

    public Integer getUnLocIdByUnLocCode(String unLocCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id");
        queryBuilder.append(" FROM un_location ");
        queryBuilder.append(" WHERE un_loc_code = '").append(unLocCode).append("' limit 1");
        return (Integer) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getNearByCyYardCity(String zip, double shortDistance) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select dest.un_loc_name as city,");
        queryBuilder.append("cast(distance(orig.lat,orig.lng,dest.lat,dest.lng) as signed) as distance");
        queryBuilder.append(" from zip_code orig,un_location dest");
        queryBuilder.append(" where dest.cy_yard='Y'");
        queryBuilder.append(" and dest.lat is not null");
        queryBuilder.append(" and orig.zip='").append(zip).append("'");
        queryBuilder.append(" order by distance limit 1");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            Object[] col = (Object[]) result;
            String city = (String) col[0];
            int distance = Integer.parseInt(col[1].toString());
            if (CommonUtils.isNotEmpty(distance) && distance < (int) shortDistance) {
                return city + " (" + distance + " miles)";
            }
        }
        return null;
    }

    public String getpropertyRemarks(String originCode) throws Exception {
        Object propertyRemark = null;
        if (originCode != null) {
            String queryString = "SELECT cts_remarks FROM un_location WHERE un_loc_code='" + originCode + "' limit 1";
            propertyRemark = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        }
        return null != propertyRemark ? propertyRemark.toString() : null;
    }

    public List<UnLocation> getGroupCityList(Integer id, Integer cityId, String unLocationName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT un_loc_name,un_loc_code,statecode,countrycode,id FROM un_location WHERE countrycode= '").append(id).append("' AND id != '").append(cityId).append("' ");
        queryBuilder.append("AND id NOT IN(SELECT group_city FROM grouped_city WHERE city= '").append(cityId).append("') ");
        queryBuilder.append("AND id NOT IN(SELECT city FROM grouped_city WHERE group_city= '").append(cityId).append("') ");
        queryBuilder.append("AND un_loc_name LIKE '").append(unLocationName).append("%'");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public List<UnLocation> getAllGroupCityList(Integer id) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(UnLocation.class);
        criteria.createAlias("countryId", "country");
        if (CommonUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.like("country.id", id));
        }
        return criteria.list();
    }

    public String getAlternatePortName(String unlocCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select alternate_port_name from un_location where un_loc_code='").append(unlocCode).append("' limit 1");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? result : "";
    }

    public String getUnLocCode(String city, String state) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT un.un_loc_code FROM un_location un JOIN genericcode_dup gds ON gds.id=un.statecode ");
        queryBuilder.append("WHERE un.un_loc_name='").append(city).append("' AND  gds.code='").append(state).append("' limit 1");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? result : "";
    }

    public List<String> getBlueToLogiFile(Integer destId) throws Exception {
        StringBuilder querBuilder = new StringBuilder();
        querBuilder.append("SELECT CONVERT(id USING utf8) FROM lcl_file_number fl LEFT JOIN lcl_booking b ON b.file_number_id=fl.id WHERE b.fd_id=");
        querBuilder.append(destId).append(" AND fl.datasource='E'");
        // querBuilder.append("");
        return getCurrentSession().createSQLQuery(querBuilder.toString()).list();
    }

    public String getDocDeptEmail(String billingterminal) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        String queryString = "select doc_dept_email from terminal where trmnum='" + splitterminal + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getcustomerserviceEmail(String billingterminal) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        String queryString = "select customer_service_dept_email from terminal where trmnum='" + splitterminal + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getPhoneNobyTerminal(String billingterminal) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        String queryString = "select phnnum1 from terminal where trmnum='" + splitterminal + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getFaxNobyTerminal(String billingterminal) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        String queryString = "select faxnum1 from terminal where trmnum='" + splitterminal + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getdocNameTerminal(String billingterminal, String docemail) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        if (docemail == null) {
            docemail = "";
        }
        String queryString = "select doc_dept_name from terminal where trmnum='" + splitterminal + "' and doc_dept_email='" + docemail + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getcustNameTerminal(String billingterminal, String custemail) {
        String splitterminal = billingterminal.substring(billingterminal.lastIndexOf("-") + 1);
        if (custemail == null) {
            custemail = "";
        }
        String queryString = "select customer_service_dept_name from terminal where trmnum='" + splitterminal + "' and customer_service_dept_email='" + custemail + "' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public boolean getPierPass(String origin) throws Exception {
        String originFor = "";
        String result = "";
        if (origin != null && !origin.isEmpty()) {
            if (origin.lastIndexOf("(") > -1 && origin.lastIndexOf(")") > -1) {
                originFor = origin.substring(origin.lastIndexOf("(") + 1, origin.lastIndexOf(")"));
            }

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT pier_pass FROM un_location WHERE un_loc_code='");
            queryBuilder.append(originFor).append("' limit 1");
            result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();

        }
        return "Y".equals(result);
    }
    /* public List getUnId(String s) throws Exception {
     List results = null;
     results = getCurrentSession().createQuery(
     "SELECT unl.id FROM un_location unl WHERE unl.un_loc_code="+ s).list();
     return results;
     }*/

    public String getUnlocationCode(String fileId) {
        String queryString = "SELECT u.un_loc_code FROM un_location u  WHERE u.id = (SELECT pod_id FROM lcl_bl WHERE file_number_id='" + fileId + "' limit 1)";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }

    public String getStateCountryCode(String unLocCode) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT  COALESCE(st.code,cy.codedesc)  FROM un_location un LEFT JOIN genericcode_dup cy ");
        queryBuilder.append("ON (un.countrycode = cy.id) LEFT JOIN genericcode_dup st ON (un.statecode = st.id) ");
        queryBuilder.append("LEFT JOIN ports po ON (po.id=un.id) WHERE  un.un_loc_code = '").append(unLocCode).append("'");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? result : "";
    }

    public String getUnNameCity(String polID) throws Exception {
        String query = "SELECT  UnLocationGetNameStateCntryByID("+polID+")";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }
}

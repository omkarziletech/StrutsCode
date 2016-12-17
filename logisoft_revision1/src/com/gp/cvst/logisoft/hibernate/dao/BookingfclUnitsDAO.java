package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.bc.fcl.BookingConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.beans.SearchBookingBean;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.struts.form.BookingChargesForm;
import java.util.Arrays;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class BookingfclUnits.
 * @see com.gp.cvst.logisoft.hibernate.dao.BookingfclUnits
 * @author MyEclipse - Hibernate Tools
 */
public class BookingfclUnitsDAO extends BaseHibernateDAO {

    public void save(BookingfclUnits transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(BookingfclUnits persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public BookingfclUnits findById(java.lang.Integer id) throws Exception {
        BookingfclUnits instance = (BookingfclUnits) getSession().get("com.gp.cvst.logisoft.domain.BookingfclUnits", id);
        return instance;
    }

    public List findByExample(BookingfclUnits instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.BookingfclUnits").add(Example.create(instance)).list();
        return results;
    }

    public void update(BookingfclUnits persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from BookingfclUnits as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List getbookingfcl(String bookingid) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber=?0 and unitType.id is not null order by unitType.id,newFlag";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bookingid);
        String temp = "";
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        if (queryObject.list().size() > 0) {
            for (Object object : queryObject.list()) {
                BookingfclUnits bookingfclUnits = (BookingfclUnits) object;
                if (!temp.equals(bookingfclUnits.getUnitType().getCode())) {
                    k = j;
                }
                if (bookingfclUnits.getChargeCodeDesc().equals("OCNFRT") || bookingfclUnits.getChargeCodeDesc().equals("OFIMP")) {
                    linkedList.add(k, bookingfclUnits);
                } else {
                    linkedList.add(bookingfclUnits);
                }
                temp = bookingfclUnits.getUnitType().getCode();
                j++;
            }
        }
        newList.addAll(linkedList);
        return newList;
    }

    public int getBookingUnits(String bookingid) throws Exception {
        String queryString = "SELECT SUM(`b`.`numbers`) AS numbers FROM bookingfcl_units b WHERE b.BookingNumber = '" + bookingid + "'  AND b.approve_bl = 'Yes' AND b.ChargeCodeDesc = 'OCNFRT' AND b.UnitType IS NOT NULL GROUP BY b.BookingNumber LIMIT 1;";
        Object object = getSession().createSQLQuery(queryString).uniqueResult();
        int numbers = 0;
        if (object != null) {
            numbers = Double.valueOf(object.toString()).intValue();
        }
        return numbers;
    }

    public List getbookingfcl1(String bookingid) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber=?0 and unitType.id is null order by id";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bookingid);
        return queryObject.list();
    }

    public List getbookingfcl2(String bookingid) throws Exception {
        getSession().flush();
        String queryString = "from BookingfclUnits where bookingNumber='" + bookingid + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List getbookingFclToUpdate(String bookingid, String chargeCode) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber='" + bookingid + "' and costType != 'PER BL CHARGES' and chgCode = '" + chargeCode + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List getbookingfclWhenUnitIsNull(String bookingid) throws Exception {
        String queryString = "from BookingfclUnits where costType='" + BookingConstants.COSTTYPE + "' and bookingNumber='" + bookingid + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public BookingfclUnits merge(BookingfclUnits detachedInstance) throws Exception {
        BookingfclUnits result = (BookingfclUnits) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(BookingfclUnits instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(BookingfclUnits instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List getBookingfclid(String quoteno) throws Exception {
        List result = null;
        String qeury = "select * from BookingPopUp q where q.BookingId='" + quoteno + "' ";
        result = getCurrentSession().createQuery(qeury).list();
        return result;
    }

    public List getBookingFCLunits(String BookingNo) throws Exception {
        NumberFormat number = new DecimalFormat("##,###,##0.00");
        List<SearchBookingBean> result = new ArrayList<SearchBookingBean>();
        SearchBookingBean searchBookingBean = null;
        String queryString = "";
        queryString = "select bl.unitType.id,bl.numbers,bl.rates,bl.ratesdescription from BookingfclUnits bl where bl.bookingNumber ='" + BookingNo + "' ";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObj.iterator();
        while (itr.hasNext()) {
            searchBookingBean = new SearchBookingBean();
            Object[] row = (Object[]) itr.next();
            Integer unitType = (Integer) row[0];
            String numbers = (String) row[1];
            Double rates = (Double) row[2];
            String ratesDesc = (String) row[3];
            if (unitType == null) {
                unitType = 0;
            }
            searchBookingBean.setUnitType(String.valueOf((unitType)));
            searchBookingBean.setNumbers(numbers);
            if (rates != null) {
                searchBookingBean.setRates(number.format(rates));
            }
            searchBookingBean.setRatesDesc(ratesDesc);
            result.add(searchBookingBean);
            searchBookingBean = null;
        }
        return result;
    }

    public List getChargesBasedOnContainerSize(String bookingNumber, String size) throws Exception {
        List bookingChargesList = new ArrayList();
        String queryString = "from BookingfclUnits where bookingNumber='" + bookingNumber + "' and unitType='" + size + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        bookingChargesList = queryObject.list();
        return bookingChargesList;
    }

    public List<Object[]> getAllCharges(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select comment,id,chg_code");
        queryBuilder.append(" from bookingfcl_units");
        queryBuilder.append(" where bookingnumber = (select bookingid from booking_fcl where file_no = '").append(fileNo).append("')");
        return (List<Object[]>) getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List<BookingfclUnits> checkChargeCode(String bookingId, String chargeCode, Integer unitType, String costtype) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingfclUnits.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (CommonFunctions.isNotNull(bookingId)) {
            criteria.add(Restrictions.eq("bookingNumber", bookingId));
        }
        if (CommonFunctions.isNotNull(chargeCode)) {
            criteria.add(Restrictions.eq("ChargeCodeDesc", chargeCode));
        }
        if (CommonFunctions.isNotNull(unitType)) {
            criteria.add(Restrictions.eq("unitType.id", unitType));
        }
        if (CommonFunctions.isNotNull(costtype)) {
            criteria.add(Restrictions.eq("costType", costtype));
        }
        return criteria.list();
    }

    public List<Object[]> getADVFFandADVSHPAmount(Integer bookingId) throws Exception {
        String queryString = "SELECT unitType.id,SUM(amount),ChargeCodeDesc,SUM(markUp) FROM BookingfclUnits  WHERE bookingNumber=" + bookingId + "  AND"
                + "	 (ChargeCodeDesc = '" + FclBlConstants.ADVANCEFFCODE + "' OR ChargeCodeDesc ='" + FclBlConstants.ADVANCESHIPPERCODE + "') GROUP BY unitType";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List getVendorOptionalList(String bookingid) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber='" + bookingid + "' and newFlag is null and (approveBl = 'yes' or costType = 'PER BL CHARGES')";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public String getInlandVendor(String bookingid) throws Exception {
        String queryString = "Select account_no from bookingfcl_units where BookingNumber='" + bookingid + "' and chg_code = 'INLAND' limit 1";
        String returnString = null != getSession().createSQLQuery(queryString).uniqueResult() ? getSession().createSQLQuery(queryString).uniqueResult().toString() : "";
        return returnString;
    }

    public boolean updateInlandVendor(String bookingid, String accountNo, String accountName) throws Exception {
        String queryString = "update  bookingfcl_units set account_no = '" + accountNo + "',account_name = '" + accountName + "' where BookingNumber='" + bookingid + "' and chg_code = 'INLAND'";
        int update = getCurrentSession().createSQLQuery(queryString).executeUpdate();
        return update > 0;
    }

    public List getByBookingNumberAndChargeCode(String bookingid, String chargeCode) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber='" + bookingid + "' and chgCode = '" + chargeCode + "' and costType != 'PER BL CHARGES'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findByPropertyUsingBookingId(String propertyName, Object value, String secondProperty, Object secondValue) throws Exception {
        String queryString = "from BookingfclUnits as model where model." + propertyName + "= ?0 and "
                + "model." + secondProperty + "= ?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        queryObject.setParameter("1", secondValue);
        return queryObject.list();
    }

    public List getSpecialEquipmentCharges(String qid, String code, String desc) throws Exception {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.specialEquipment='" + desc + "' and c.specialEquipmentUnit='" + code + "'";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List checkSpecialEquipmentCharges(String qid, int unitType, String code) throws Exception {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType.id= " + unitType + " and c.specialEquipmentUnit='" + code + "'";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List checkSpecialEquipmentGroupCharges(String qid, int unitType, String code) {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType.id= " + unitType + " and c.specialEquipmentUnit='" + code + "' and c.standardCharge != 'Y' group by c.unitType,c.specialEquipmentUnit,c.specialEquipment,c.standardCharge";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List getGroupByUnitType(String qid) throws Exception {
        List specialEquipmentUnitList = new ArrayList();
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType is not null GROUP BY c.unitType";
        List l = getCurrentSession().createQuery(queryString).list();
        if (!l.isEmpty()) {
            for (Iterator it = l.iterator(); it.hasNext();) {
                BookingfclUnits bookingfclUnits = (BookingfclUnits) it.next();
                if (null != bookingfclUnits.getUnitType()) {
                    specialEquipmentUnitList.add(new LabelValueBean(bookingfclUnits.getUnitType().getCodedesc(), bookingfclUnits.getUnitType().getCodedesc()));
                }
            }
        }
        return specialEquipmentUnitList;
    }

    public List getGroupByCharges(String qid) {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType is not null GROUP BY c.unitType,c.specialEquipmentUnit,c.standardCharge";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List getGroupByCharges(String qid, String chargeCode) {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.ChargeCodeDesc = '" + chargeCode + "' and c.unitType is not null GROUP BY c.unitType,c.specialEquipmentUnit,c.standardCharge";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public void deleteCharges(String bookingId, String chargeCode) throws Exception {
        String queryString = "Delete from BookingfclUnits where bookingNumber=" + bookingId + " and  ChargeCodeDesc='" + chargeCode + "'";
        getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getStandardCharge(String qid, int unitType) throws Exception {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType.id=" + unitType + " and c.standardCharge = 'Y'";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List getChargeByEquipmentUnit(String qid, int unitType, String standardCharge) throws Exception {
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType.id=" + unitType + " and c.standardCharge = '" + standardCharge + "'";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public List getEquipmentChargeToAdd(String qid, int unitType, int standardIndex) throws Exception {
        String standardCharge = "";
        if (standardIndex == 0) {
            standardCharge = "Y";
        } else {
            standardCharge = "" + standardIndex;
        }
        String queryString = "from BookingfclUnits c where c.bookingNumber='" + qid + "' and c.unitType.id=" + unitType + " and c.standardCharge = '" + standardCharge + "'";
        List queryObj = getCurrentSession().createQuery(queryString).list();
        return queryObj;
    }

    public Integer getStandardChargeIndex(String qid, String code, String codeDesc) throws Exception {
        String queryString = "select max(c.standard_charge) from bookingfcl_units c where c.BookingNumber='" + qid + "' and c.special_equipment_unit='" + code + "'  and standard_charge != 'Y'";
        Integer result = 0;
        Iterator itr = null;
        itr = getCurrentSession().createSQLQuery(queryString).list().iterator();
        if (itr.hasNext()) {
            String s = (String) itr.next();
            if (CommonUtils.isNotEmpty(s)) {
                result = Integer.parseInt(s);
            }
        }
        return result;
    }

    public Integer getContainerSize(String qid) {
        String queryString = "SELECT SUM(t.total)FROM (SELECT SUM(DISTINCT(numbers)) AS total FROM bookingfcl_units WHERE bookingnumber = '" + qid + "' AND approve_bl = 'Yes' GROUP BY unittype,standard_charge)AS t";
        Integer result = 0;
        Iterator itr = getCurrentSession().createSQLQuery(queryString).list().iterator();
        if (itr.hasNext()) {
            Double s = (Double) itr.next();
            if (CommonUtils.isNotEmpty(s)) {
                result = s.intValue();
            }
        }
        return result;
    }

    public void saveManualSpecialEquipmentCharges(BookingfclUnits fclUnits, String code) throws Exception {
        List l = checkSpecialEquipmentGroupCharges(fclUnits.getBookingNumber(), fclUnits.getUnitType().getId(), code);
        if (!l.isEmpty()) {
            for (Iterator it = l.iterator(); it.hasNext();) {
                BookingfclUnits existingCharge = (BookingfclUnits) it.next();
                BookingfclUnits equipmentCharges = new BookingfclUnits();
                PropertyUtils.copyProperties(equipmentCharges, fclUnits);
                equipmentCharges.setSpecialEquipmentUnit(code);
                equipmentCharges.setSpecialEquipment(existingCharge.getSpecialEquipment());
                equipmentCharges.setStandardCharge(existingCharge.getStandardCharge());
                save(equipmentCharges);
            }
        }
    }

    public void updateAdjustmentChargeComments(String bookingId, String chargeCodeDesc, String comments, Double adjustAmtValue) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE bookingfcl_units SET adjustment = ");
        queryBuilder.append(adjustAmtValue).append(",");
        queryBuilder.append(" adjustment_charge_comments = '");
        queryBuilder.append(comments).append("'");
        queryBuilder.append(" WHERE BookingNumber = '");
        queryBuilder.append(bookingId).append("'");
        queryBuilder.append(" AND ChargeCodeDesc='");
        queryBuilder.append(chargeCodeDesc).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public List<BookingfclUnits> getConsolidatedCharges(String bookingId, String chargeCode, Integer unitType) throws Exception {
        String trimmedChgCode = chargeCode.substring(0, 3);
        String[] otherCostCodes = {"HAZFEE", "BKRSUR", "INTMDL", "INTFS", "INTRAMP", "DEFER", "INSURE", "FAEXP", "FFCOMM", "FAECOMM", "NASLAN", "OCNFRT"};
        Criteria criteria = getCurrentSession().createCriteria(BookingfclUnits.class);
        criteria.add(Restrictions.eq("bookingNumber", bookingId))
                .add(Restrictions.eq("unitType.id", unitType));
        if ("OCNFRT".equalsIgnoreCase(chargeCode)) {
            criteria.add(Restrictions.not(Restrictions.in("ChargeCodeDesc", otherCostCodes)))
                    .add(Restrictions.isNull("manualCharges"));

        } else {
            criteria.add(Restrictions.like("ChargeCodeDesc", trimmedChgCode + "%"));
        }
        return criteria.list();
    }

    public List<BookingfclUnits> findByPropertyForApprovedUnits(String bookingNumber) throws Exception {
	String queryString = "from BookingfclUnits where bookingNumber='"+bookingNumber+"' and approveBl='Yes' group by unitType";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    public boolean isBookingWithoutContainer(String bookinNo)throws Exception{
        String q="select count(*) from bookingfcl_units where bookingnumber='"+bookinNo+"' and approve_bl='Yes'";
        String result=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return result.equals("0");
    }
    public boolean isContainDeferral(String bookingNumber)throws Exception{
        String q="select count(*) from bookingfcl_units where bookingnumber='"+bookingNumber+"' and new_flag is null and chargecodedesc='DEFER'";
        String r=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return !r.equals("0");
    }

    public List<BookingfclUnits> findByChargeCode(String bookingNumber, String chargeCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BookingfclUnits.class);
        criteria.add(Restrictions.eq("bookingNumber", bookingNumber))
                .add(Restrictions.eq("ChargeCodeDesc", chargeCode))
                .add(Restrictions.isNull("newFlag"));
        return criteria.list();
    }

    public void updateOfrSpotrate(BookingChargesForm bookingChargesForm,BookingfclUnits bookingUnits ){
        StringBuilder sb=new StringBuilder();
        sb.append("update bookingfcl_units set spotrate_amt=").append(bookingChargesForm.getSpotRateAmt()).append(",spotrate_markup=((amount+mark_up)-");
        sb.append(bookingChargesForm.getSpotRateAmt()).append("),standard_chk='off',spotrate_chk='off',sell_rate=(spotrate_amt+spotrate_markup),profit=(sell_rate-buy_rate)");
        sb.append(" where bookingnumber='").append(bookingUnits.getBookingNumber());
        sb.append("' and chargecodedesc='OCNFRT' and unittype=").append(bookingChargesForm.getUnitSelect()).append(" and chargecodedesc <>'DEFER' and new_flag is null");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);

        sb.append("update bookingfcl_units set spotrate_amt=0.00,spotrate_markup=(amount+mark_up),standard_chk='off',spotrate_chk='on',");
        sb.append("sell_rate=(spotrate_amt+spotrate_markup),profit=(sell_rate-buy_rate) where bookingnumber=");
        sb.append(bookingUnits.getBookingNumber()).append(" and unittype=").append(bookingChargesForm.getUnitSelect()).append(" and new_flag is null");
        sb.append(" and chargecodedesc not in('OCNFRT','HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','DEFER')");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);
        
        String spotRateamt = getspotRateamt(bookingUnits.getBookingNumber(),bookingChargesForm.getUnitSelect());
        if(spotRateamt != null){
        Double spotRateamtD = Double.valueOf(spotRateamt);
        sb.append("update bookingfcl_units set spotrate_amt=").append(spotRateamtD).append(",spotrate_markup=(amount+mark_up),standard_chk='on',spotrate_chk='off'");
        sb.append(",sell_rate=(spotrate_amt+spotrate_markup),profit=(sell_rate-buy_rate) where bookingnumber=");
        sb.append(bookingUnits.getBookingNumber()).append(" and unittype=").append(bookingChargesForm.getUnitSelect()).append(" and new_flag is null");
        sb.append(" and chargecodedesc in('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN')");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);
        }

    }

    public String getOfrBundleCharge(String bookingNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT SUM(amount+mark_up) FROM bookingfcl_units WHERE bookingnumber='");
        queryBuilder.append(bookingNumber).append("' AND manual_charges IS NULL AND approve_bl='Yes' AND ");
        queryBuilder.append("chargeCodeDesc NOT IN ('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN')");
        return getSession().createSQLQuery(queryBuilder.toString()).uniqueResult().toString();
    }

    public String brandValue(String bookingId){
      String q="select brand from booking_fcl where bookingId ='"+bookingId+"'"; 
      Object result=getCurrentSession().createSQLQuery(q).uniqueResult();
        return null != result.toString() ? result.toString() : "";
    }

    public BookingfclUnits getbookingfclUnitCharges(Object bookingid, String chargeCode) throws Exception {
        String queryString = "from BookingfclUnits where bookingNumber=?0 and ChargeCodeDesc =?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bookingid);
        queryObject.setParameter("1", chargeCode);
        BookingfclUnits instance =(BookingfclUnits) queryObject.uniqueResult();
        return instance;
    }

    public void saveOrUpdate(BookingfclUnits transientInstance) throws Exception {
        getCurrentSession().saveOrUpdate(transientInstance);
        getCurrentSession().flush();
    }

    public Integer getBookingFclUnit(String bookingId, String chargeCode, Integer unitType) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lu.`ID` ");
        sb.append("FROM");
        sb.append("  `bookingfcl_units` lu ");
        sb.append("WHERE lu.`BookingNumber` = :BookingNumber ");
        sb.append("  AND lu.`UnitType` = :id ");
        sb.append("  AND lu.`ChargeCodeDesc` = :chargeCode ");
        sb.append("  AND lu.`manual_charges` IS NULL ");
        sb.append("LIMIT 1 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("BookingNumber", bookingId);
        queryObject.setParameter("id", unitType);
        queryObject.setParameter("chargeCode", chargeCode);
        return (Integer) queryObject.uniqueResult();
    }

    public Integer getTopContainerId(String bookingId, String chargeCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lu.`ID` ");
        sb.append("FROM");
        sb.append("  `bookingfcl_units` lu ");
        sb.append("WHERE lu.`BookingNumber` =:BookingNumber ");
        sb.append(" AND lu.`ChargeCodeDesc`=:chargeCode");
        sb.append("  AND lu.`manual_charges` IS NULL ");
        sb.append("  AND lu.`UnitType` IS NOT NULL ");
        sb.append("ORDER BY lu.`UnitType` LIMIT 1 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("BookingNumber", bookingId);
        queryObject.setParameter("chargeCode", chargeCode);
        return (Integer) queryObject.uniqueResult();
    }

    public Boolean getBookingFclChargeFlag(String bookingId, String chargeCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  IF(COUNT(*) > 0, TRUE, FALSE) AS flag ");
        sb.append("FROM");
        sb.append("  `bookingfcl_units` lu ");
        sb.append("WHERE lu.`BookingNumber` =:bookingId ");
        sb.append("  AND lu.`ChargeCodeDesc` =:chargeCode ");
        sb.append("  AND lu.`manual_charges` IS NULL ");
        SQLQuery queryObject =getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("bookingId", bookingId);
        queryObject.setParameter("chargeCode", chargeCode);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }
    
    public List getCustNoAndName (String BookingNumber) {
    String query ="SELECT lu.`account_name` AS acctName , lu.`account_no` AS acctNo FROM `bookingfcl_units` lu WHERE lu.`BookingNumber` =:BookingNumber AND lu.`manual_charges`='M' ORDER BY id DESC LIMIT 1";
    Query queryObject = getCurrentSession().createSQLQuery(query);
    queryObject.setParameter("BookingNumber", BookingNumber);
    return queryObject.list();
    }
    
    public Integer getOldChargeId(String bookingId, String chargeCode) {
        String query = "SELECT id FROM `bookingfcl_units` WHERE `BookingNumber`=:bookingNumber AND `manual_charges`IS NOT NULL AND `ChargeCodeDesc`=:chargeCode LIMIT 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("bookingNumber", bookingId);
        queryObject.setParameter("chargeCode", chargeCode); 
        queryObject.addScalar("id", IntegerType.INSTANCE);
        return (Integer) queryObject.uniqueResult();
    }
    public List<String> getCFCLChargeCodeList(String bookingId) {
    String query ="SELECT ChargeCodeDesc FROM `bookingfcl_units` WHERE `BookingNumber`=:bookingNumber AND `manual_charges` IS NOT NULL";
    Query queryObject =getCurrentSession().createSQLQuery(query);
    queryObject.setParameter("bookingNumber", bookingId);
    return queryObject.list();
    }
    
    public void deleteCFCLCharge(List<Long> listId) {
    String queryString="delete from bookingfcl_units where id IN (:listId)";
    Query queryObject= getCurrentSession().createSQLQuery(queryString);
    queryObject.setParameterList("listId", listId);
    queryObject.executeUpdate();
    }
    
    public List<Long> getOldCFCLChargeIdList(String bookingId) {
    String queryString= "SELECT id FROM `bookingfcl_units` WHERE BookingNumber=:bookingId AND `cfcl_flag` IS TRUE";
    Query queryObject =getCurrentSession().createSQLQuery(queryString);
    queryObject.setParameter("bookingId", bookingId);
    return queryObject.list();
    }
       public String[] checkvendorForChassisCharge(String fileno) throws Exception{
         StringBuilder queryBuilder = new StringBuilder();
         queryBuilder.append("select ");
         queryBuilder.append(" c.account_name as AccountName,");
         queryBuilder.append(" c.account_no as AccountNo ");
         queryBuilder.append("from");
         queryBuilder.append(" `bookingfcl_units` c ");
         queryBuilder.append(" LEFT JOIN `booking_fcl` q  ON ");
         queryBuilder.append(" q.`bookingNumber`= c.`bookingNumber` ");
         queryBuilder.append(" where c.chargecodedesc = 'inland' ");
         queryBuilder.append("and q.file_no = :fileno");
         SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
         query.setMaxResults(1);
         query.setString("fileno", fileno);
         query.addScalar("AccountName", StringType.INSTANCE);
         query.addScalar("AccountNo", StringType.INSTANCE);
         Object[] cols = (Object[]) query.uniqueResult();
         return Arrays.copyOf(cols, cols.length, String[].class);
     }
  
    public String checkvendorForChassisChargeOnload(String bookingnumber) throws Exception {
        String queryString = "SELECT account_no FROM bookingfcl_units WHERE bookingNumber = '" + bookingnumber + "'and chargecodedesc='inland' limit 1";
        String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != result ? result : "";
    }
    private String getspotRateamt(String id, String unitType) {
        String queryString = "select spotrate_amt from bookingfcl_units where bookingNumber = '" + id + "' and unittype='" + unitType + "' and chargecodedesc in('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN') and spotrate_amt is not null";
        Object result = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        if (null != result) {
            return (null != result ? result.toString() : "");
        }
        return null;
    }
}

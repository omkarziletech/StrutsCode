package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.struts.util.MessageResources;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.struts.form.QuoteChargeForm;
import java.util.Arrays;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class Charges.
 * @see com.gp.cvst.logisoft.hibernate.dao.Charges
 * @author MyEclipse - Hibernate Tools
 */
public class ChargesDAO extends BaseHibernateDAO {

    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

    public void save(Charges transientInstance)throws Exception {
	    getSession().save(transientInstance);
	    getSession().flush();
    }

    public void update(Charges persistanceInstance)throws Exception {
	    getSession().save(persistanceInstance);
	    getSession().flush();
    }

    public void delete(Charges persistentInstance)throws Exception {
	    getCurrentSession().delete(persistentInstance);
	    getCurrentSession().flush();
    }

    public Charges findById(java.lang.Integer id)throws Exception {
	    Charges instance = (Charges) getSession().get("com.gp.cvst.logisoft.domain.Charges", id);
	    return instance;
    }

    public List findByExample(Charges instance)throws Exception {
	    List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.Charges").add(Example.create(instance)).list();
	    return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
	    String queryString = "from Charges as model where model." + propertyName + "= ?0";
	    Query queryObject = getSession().createQuery(queryString);
	    queryObject.setParameter("0", value);
	    return queryObject.list();
    }

    public List findByPropertyUsingQuoteId(String propertyName, Object value, String secondProperty, Object secondValue)throws Exception {
	    String queryString = "from Charges as model where model." + propertyName + "= ?0 and "
		    + "model." + secondProperty + "= ?1";
	    Query queryObject = getSession().createQuery(queryString);
	    queryObject.setParameter("0", value);
	    queryObject.setParameter("1", secondValue);
	    return queryObject.list();
    }
    
    public Charges merge(Charges detachedInstance)throws Exception {
	    Charges result = (Charges) getSession().merge(detachedInstance);
	    return result;
    }

    public void attachDirty(Charges instance)throws Exception {
	    getSession().saveOrUpdate(instance);
	    getSession().flush();
    }

    public void attachClean(Charges instance)throws Exception {
	    getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List getChargesforQuotation(int qid, MessageResources messageResources)throws Exception {
	List<CostBean> costbeanlist = new ArrayList<CostBean>();

	String queryString = "Select c.chgCode,c.unitType,c.amount,c.number,c.include,c.print,"
		+ "c.costType,c.markUp,c.chargeCodeDesc,c.currecny,c.efectiveDate,c.futureRate,c.id,c.accountName,c.accountNo,c.chargeFlag from Charges c where c.qouteId='" + qid + "' order by c.unitType.id";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	Iterator itr = queryObj.iterator();
	CostBean cb = null;
	Integer id = null;
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	//String act[]=new String[100];
	int j = 0;
	while (itr.hasNext()) {
	    Object[] row = (Object[]) itr.next();
	    cb = new CostBean();
	    String chargecode = (String) row[0];
	    String unitType = (String) row[1];
	    Double amt = (Double) row[2];
	    String num = (String) row[3];
	    String include = (String) row[4];
	    String print = (String) row[5];
	    String costType = (String) row[6];
	    Double markUp = 0.00;
	    if (row[7] != null) {
		markUp = (Double) row[7];
	    }
	    String chargeCodeDesc = (String) row[8];
	    String currency = (String) row[9];
	    Date effectiveDate = (Date) row[10];
	    Double futureRate = (Double) row[11];
	    id = (Integer) row[12];
	    String accountName = (String) row[13];
	    String accountNo = (String) row[14];
	    String unitTypes[] = messageResources.getMessage("unittype").split(",");
	    if (unitType != null) {
		if (unitType.equals("0.00")) {
		    cb.setRetail(number.format(amt));
		}
		if (unitType.equals(unitTypes[0])) {
		    if (amt != null && !amt.equals("")) {
			cb.setSetA(number.format(amt));
		    }

		    if (markUp == null) {
			markUp = 0.00;
		    }
		    if (row[15] != null) {
			cb.setChargeFlag((String) row[15]);
		    }
		    cb.setMarkUpA(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateA(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[2])) {
		    cb.setSetB(number.format(amt));
		    cb.setMarkUpB(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateB(number.format(futureRate));
		    }

		} else if (unitType.equals(unitTypes[1])) {
		    cb.setSetD(number.format(amt));
		    cb.setMarkUpD(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateD(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[3])) {
		    cb.setSetE(number.format(amt));
		    cb.setMarkUpE(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateE(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[4])) {
		    cb.setSetC(number.format(amt));
		    cb.setMarkUpC(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateC(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[5])) {
		    cb.setSetF(number.format(amt));
		    cb.setMarkUpF(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateF(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[6])) {
		    cb.setSetG(number.format(amt));
		    cb.setMarkUpG(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateG(number.format(futureRate));
		    }
		} // cb.setSet(act);
		else if (unitType.equals(unitTypes[7])) {
		    cb.setSetH(number.format(amt));
		    cb.setMarkUpH(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateH(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[8])) {
		    cb.setSetI(number.format(amt));
		    cb.setMarkUpI(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateI(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[9])) {
		    cb.setSetJ(number.format(amt));
		    cb.setMarkUpJ(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateJ(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[10])) {
		    cb.setSetK(number.format(amt));
		    cb.setMarkUpK(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateK(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[11])) {
		    cb.setSetL(number.format(amt));
		    cb.setMarkUpL(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateL(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[12])) {
		    cb.setSetM(number.format(amt));
		    cb.setMarkUpM(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateM(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[13])) {
		    cb.setSetN(number.format(amt));
		    cb.setMarkUpN(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateN(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[14])) {
		    cb.setSetO(number.format(amt));
		    cb.setMarkUpO(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateO(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[15])) {
		    cb.setSetP(number.format(amt));
		    cb.setMarkUpP(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateP(number.format(futureRate));
		    }
		} else if (unitType.equals(unitTypes[16])) {
		    cb.setSetQ(number.format(amt));
		    cb.setMarkUpQ(number.format(markUp));
		    if (futureRate != null) {
			cb.setFutureRateQ(number.format(futureRate));
		    }
		}
		cb.setCostType(costType);
		cb.setChargecode(chargecode);
		cb.setChargeCodedesc(chargeCodeDesc);
		cb.setUnitType(unitType);
		GenericCode gen = genericCodeDAO.findById(Integer.parseInt(unitType));
		if (gen != null) {
		    cb.setUnitTypeName(gen.getCodedesc());
		}
		cb.setNumber(num);
		cb.setInclude(include);
		cb.setPrint(print);
		cb.setCurrency(currency);

		cb.setFclCostId(id);
		cb.setAccountName(accountName);
		cb.setAccountNo(accountNo);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		if (effectiveDate != null) {
		    cb.setEffectiveDate(sdf.format(effectiveDate));
		} else {
		    cb.setEffectiveDate(" ");
		}

		if (costType.equals(messageResources.getMessage("FlatRatePerConatinerSize")) || costType.equals(messageResources.getMessage("Flatratepercontainer")) || costType.equals(messageResources.getMessage("percentofr"))) {
		    costbeanlist.add(cb);
		}

	    }
	    cb = null;
	    j++;
	}



	return costbeanlist;
    }

    public List getChargesforQuotation2(int qid, MessageResources messageResources)throws Exception {

	List<CostBean> costbeanlist = new ArrayList<CostBean>();

	String queryString = "Select c.chgCode,c.costType,c.amount,c.ctc,c.ftf,c.minimum,"
		+ "c.otherinclude,c.otherprint,c.chargeCodeDesc,c.currecny,c.efectiveDate,"
		+ "c.futureRate,c.id,c.accountName,c.accountNo,c.chargeFlag,c.markUp from Charges c where c.qouteId='" + qid + "' order by id";
	List queryObj = getCurrentSession().createQuery(queryString).list();

	Iterator itr = queryObj.iterator();
	CostBean cb = null;
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	//String act[]=new String[100];
	int j = 0;

	while (itr.hasNext()) {


	    Object[] row = (Object[]) itr.next();
	    cb = new CostBean();
	    String chargecode = (String) row[0];
	    String costType = (String) row[1];

	    if (!costType.equals(messageResources.getMessage("FlatRatePerConatinerSize"))) {
		Double retail = 0.00;
		if (row[2] != null) {
		    retail = (Double) row[2];
		}
		Double ctc = 0.00;
		if (row[3] != null) {
		    ctc = (Double) row[3];
		}

		Double ftf = 0.00;
		if (row[4] != null) {
		    ftf = (Double) row[4];
		}
		Double minimum = 0.00;
		if (row[5] != null) {
		    minimum = (Double) row[5];
		}

		String otherinclude = (String) row[6];
		String otherprint = (String) row[7];
		String chargeCodeDesc = (String) row[8];
		String currency = (String) row[9];
		Date effectiveDate = (Date) row[10];
		Double futureRate = (Double) row[11];
		cb.setFclCostId((Integer) (row[12]));
		if (row[13] != null) {
		    cb.setAccountName((String) row[13]);
		}
		if (row[14] != null) {
		    cb.setAccountNo((String) row[14]);
		}
		if (row[15] != null) {
		    cb.setChargeFlag((String) row[15]);
		}
		if (row[16] != null) {
		    cb.setMarkup(String.valueOf(row[16]));
		}
		cb.setCostType(costType);
		cb.setChargecode(chargecode);
		cb.setChargeCodedesc(chargeCodeDesc);
		cb.setRetail(number.format(retail));
		cb.setCTC(number.format(ctc));
		cb.setFtf(number.format(ftf));
		cb.setMinimum(number.format(minimum));
		cb.setOtherinclude(otherinclude);
		cb.setOtherprint(otherprint);
		cb.setCurrency(currency);
		if (futureRate != null) {
		    cb.setRetailFuture(number.format(futureRate));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		if (effectiveDate != null) {
		    cb.setOtherEffectiveDate(sdf.format(effectiveDate));
		} else {
		    cb.setEffectiveDate(" ");
		}

		if (costType.equalsIgnoreCase(messageResources.getMessage("perbl")) || costType.equalsIgnoreCase(messageResources.getMessage("per1000kg")) || costType.equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
		    costbeanlist.add(cb);
		}

	    }


	    //cb=null;
	    j++;
	}


	return costbeanlist;
    }

    public List getAllChargesForUpdate(int qid, String chargeCode)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.chgCode = '" + chargeCode + "' and c.unitType is not null order by c.unitType,c.chargeFlag,specialEquipmentUnit";
	return getCurrentSession().createQuery(queryString).list();
    }

    public List getChargesforQuotation1(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType is not null order by c.unitType,c.chargeFlag,specialEquipmentUnit";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	String temp = "";
	int j = 0;
	int k = 0;
	LinkedList linkedList = new LinkedList();
	List newList = new ArrayList();
	String unitTyep = "";
	for (Iterator iterator = queryObj.iterator(); iterator.hasNext();) {
	    Charges charges = (Charges) iterator.next();
	    if (!temp.equals(charges.getUnitType())) {
		k = j;
	    }
	    if (null != charges.getChargeCodeDesc() && charges.getChargeCodeDesc().equals("OCNFRT")) {
		linkedList.add(k, charges);
	    } else {
                    linkedList.add(charges);
                }
	    temp = charges.getUnitType();
	    j++;
	}
	newList.addAll(linkedList);
	return newList;
    }

    public List getOtherChargesforCopyQuote(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and (c.unitType is null or c.chargeFlag is not null)";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getChargesforCopyQuote(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType is not null and c.chargeFlag is null order by c.unitType";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	String temp = "";
	int j = 0;
	int k = 0;
	LinkedList linkedList = new LinkedList();
	List newList = new ArrayList();
	String unitTyep = "";
	for (Iterator iterator = queryObj.iterator(); iterator.hasNext();) {
	    Charges charges = (Charges) iterator.next();
	    if (!temp.equals(charges.getUnitType())) {
		k = j;
	    }
	    if (null != charges.getChargeCodeDesc() && (charges.getChargeCodeDesc().equals("OCNFRT") || charges.getChargeCodeDesc().equals("OFIMP"))) {
		linkedList.add(k, charges);
	    } else {
		linkedList.add(charges);
	    }
	    temp = charges.getUnitType();
	    j++;
	}
	newList.addAll(linkedList);
	return newList;
    }

    public List getChargesforQuotationForSettingTrucker(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "'  order by c.unitType,c.chargeFlag";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	String temp = "";
	int j = 0;
	int k = 0;
	LinkedList linkedList = new LinkedList();
	List newList = new ArrayList();
	String unitTyep = "";
	for (Iterator iterator = queryObj.iterator(); iterator.hasNext();) {
	    Charges charges = (Charges) iterator.next();
	    linkedList.add(k, charges);
	    k++;
	}
	newList.addAll(linkedList);
	return newList;
    }

    public List getChargesforQuotation8(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType is null";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getChargesforSpecialEquipment(int qid)throws Exception {
        getCurrentSession().clear();
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType is not null";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getQuoteId(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getQuoteId1(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.chargeFlag is null";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }
    public List<Charges> getRatesByUnitType(int qid,String unitType)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='"+unitType+"' and c.chargeFlag is null";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getAllCharges(int quoteId)throws Exception {
	List chargesList = new ArrayList();
	    String queryString = "from Charges where qouteId=" + quoteId;
	    chargesList = getCurrentSession().createQuery(queryString).list();
	return chargesList;
    }

    public List<Object[]> getAllCharges(String FileNo)throws Exception {
	List<Object[]> chargesList = new ArrayList<Object[]>();
	    String queryString = "SELECT comment,id,chgCode FROM Charges WHERE qouteId=(SELECT quoteId FROM Quotation WHERE fileNo='" + FileNo + "')";
	    chargesList = getCurrentSession().createQuery(queryString).list();
	return chargesList;
    }

    public List getQuoteIdCharges(int qid, String chargCode)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.chargeCodeDesc='" + chargCode + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getSpecialEquipmentCharges(int qid, String code, String desc)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.specialEquipment='" + desc + "' and c.specialEquipmentUnit='" + code + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List checkSpecialEquipmentCharges(int qid, String unitType, String code)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.specialEquipmentUnit='" + code + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List checkSpecialEquipmentGroupCharges(int qid, String unitType, String code)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.specialEquipmentUnit='" + code + "'  and c.standardCharge != 'Y' group by c.unitType,c.specialEquipmentUnit,c.specialEquipment,c.standardCharge";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getStandardGroupCharges(int qid, String unitType, String code)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.specialEquipmentUnit='" + code + "'  group by c.unitType,c.specialEquipmentUnit,c.specialEquipment,c.standardCharge";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List checkSpecialEquipmentHazmatCharges(int qid, String unitType, String code, String chgCode)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.specialEquipmentUnit='" + code + "' and c.chargeCodeDesc = '" + chgCode + "' group by c.specialEquipmentUnit,c.standardCharge";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getGroupByCharges(int qid)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType is not null GROUP BY c.unitType,c.specialEquipmentUnit,c.standardCharge";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getGroupByCharges(int qid, String chargeCode)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and  c.chargeCodeDesc='" + chargeCode + "' and c.unitType is not null GROUP BY c.unitType,c.specialEquipmentUnit,c.standardCharge";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public void deleteCharges(int quoteId, String chargeCode)throws Exception {
	    String queryString = "Delete from Charges where qouteId=" + quoteId + " and  chargeCodeDesc='" + chargeCode + "'";
	    getCurrentSession().createQuery(queryString).executeUpdate();
    }
    public String deleteAllCharges(int quoteId)throws Exception {
	    String queryString = "Delete from Charges where qouteId=" + quoteId + "";
	    int affectedRowRount=getCurrentSession().createQuery(queryString).executeUpdate();
            return affectedRowRount!=0?"CHARGES DELETED":"";
    }

    public void deletePerblCharges(int quoteId, String chargeCode)throws Exception {
	    String queryString = "Delete from Charges where qouteId=" + quoteId + " and  chargeCodeDesc='" + chargeCode + "' and unitType is null";
	    getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getStandardCharge(int qid, String unitType)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.standardCharge = 'Y'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getStandardCharge(int qid, String unitType, String chargeCode)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.standardCharge = 'Y' and c.chargeCodeDesc = '" + chargeCode + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getChargeByEquipmentUnit(int qid, String unitType, String index)throws Exception {
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.standardCharge = '" + index + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public List getEquipmentChargeToAdd(int qid, String unitType, int standardIndex)throws Exception {
	String standardCharge = "";
	if (standardIndex == 0) {
	    standardCharge = "Y";
	} else {
	    standardCharge = "" + standardIndex;
	}
	String queryString = "from Charges c where c.qouteId='" + qid + "' and c.unitType='" + unitType + "' and c.standardCharge = '" + standardCharge + "'";
	List queryObj = getCurrentSession().createQuery(queryString).list();
	return queryObj;
    }

    public Integer getStandardChargeIndex(int qid, String code, String codeDesc) throws Exception {
	String queryString = "select max(c.standard_charge) from Charges c where c.Qoute_ID='" + qid + "' and c.special_equipment_unit='" + code + "'  and standard_charge != 'Y'";
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

    public List getGroupByUnitType(int qid) throws Exception {
	List specialEquipmentUnitList = new ArrayList();
	String queryString = "select unit_type from Charges  where Qoute_ID='" + qid + "' and unit_type is not null group by unit_type";
	List l = getCurrentSession().createSQLQuery(queryString).list();
	if (!l.isEmpty()) {
	    for (Iterator it = l.iterator(); it.hasNext();) {
		String unit = (String) it.next();
		if (CommonUtils.isNotEmpty(unit)) {
		    GenericCode genericcode = new GenericCodeDAO().findById(Integer.parseInt(unit));
		    if (null != genericcode) {
			specialEquipmentUnitList.add(new LabelValueBean(genericcode.getCodedesc(), genericcode.getCodedesc()));
		    }
		}
	    }
	}
	return specialEquipmentUnitList;
    }

    public void saveManualSpecialEquipmentCharges(Charges charges, String code)throws Exception {
	List l = checkSpecialEquipmentGroupCharges(charges.getQouteId(), charges.getUnitType(), code);
	if (!l.isEmpty()) {
	    for (Iterator it = l.iterator(); it.hasNext();) {
		Charges existingCharge = (Charges) it.next();
		Charges equipmentCharges = new Charges();
		    PropertyUtils.copyProperties(equipmentCharges, charges);
		    equipmentCharges.setSpecialEquipmentUnit(code);
		    equipmentCharges.setSpecialEquipment(existingCharge.getSpecialEquipment());
		    equipmentCharges.setStandardCharge(existingCharge.getStandardCharge());
		    save(equipmentCharges);
	    }
	}
    }

    public void saveManualHazmatSpecialEquipmentCharges(Charges charges, String code)throws Exception {
	List l = getStandardGroupCharges(charges.getQouteId(), charges.getUnitType(), code);
	if (!l.isEmpty()) {
	    for (Iterator it = l.iterator(); it.hasNext();) {
		Charges existingCharge = (Charges) it.next();
		Charges equipmentCharges = new Charges();
		    PropertyUtils.copyProperties(equipmentCharges, charges);
		    equipmentCharges.setSpecialEquipmentUnit(code);
		    equipmentCharges.setSpecialEquipment(existingCharge.getSpecialEquipment());
		    equipmentCharges.setStandardCharge(existingCharge.getStandardCharge());
		    save(equipmentCharges);
	    }
	}
    }

    public String getAdjustedValue(String fileNumber)throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select concat('OCNFRT : ',format(ocean_freight_adjustment,2),'<br/>','ADMIN : ',format(admin_adjustment,2)) as result");
	queryBuilder.append(" from quotation");
	queryBuilder.append(" where file_no = '").append(fileNumber).append("' and (ocean_freight_adjustment <> 0.00 or admin_adjustment <>0.00) limit 1");
	return (String)getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public void deleteDocumentCharges(int quoteId)throws Exception {
        String queryString = "delete from charges where qoute_id = " + quoteId + " and chargeCodeDesc = 'DOCUM'";
	getCurrentSession().createSQLQuery(queryString).executeUpdate();
        getCurrentSession().flush();
    }
    public void deletePierPassCharges(int quoteId)throws Exception {
        String queryString = "delete from charges where qoute_id = " + quoteId + " and chargeCodeDesc = 'PIERPA'";
	getCurrentSession().createSQLQuery(queryString).executeUpdate();
        getCurrentSession().flush();
    }
    public void updateAdjustmentChargeComments(String quoteId, String chargeCodeDesc, String comments, Double adjustAmtValue)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE charges SET adjestment = ");
            queryBuilder.append(adjustAmtValue).append(",");
            queryBuilder.append(" adjustment_charge_comments = '");
            queryBuilder.append(comments).append("'");
            queryBuilder.append(" WHERE qoute_id = ");
            queryBuilder.append(quoteId);
            queryBuilder.append(" AND ChargeCodeDesc='");
            queryBuilder.append(chargeCodeDesc).append("'");
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
            getCurrentSession().flush();
    }
    public List<Charges> getConsolidatedCharges(String quoteId, String chargeCode, String unitType) throws Exception  {
        Criteria criteria = getCurrentSession().createCriteria(Charges.class);
        criteria.add(Restrictions.eq("qouteId", Integer.parseInt(quoteId)))
                .add(Restrictions.like("chargeCodeDesc", "INT%"))
                .add(Restrictions.eq("unitType", unitType));
        return criteria.list();
    }

    //-- update number of container(s) --//
    public void updateNoOfContainer(Integer unitType, String quoteId, String noOfContainer)
            throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE charges SET number = ");
        queryBuilder.append(noOfContainer);
        queryBuilder.append(" WHERE unit_type = ").append(unitType);
        queryBuilder.append(" AND qoute_id = ").append(quoteId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    //-- get all charges by quote id, and unit type not null --//
    public List<Charges> getCharges(int quoteId) throws Exception {
        String queryString = "from Charges where qouteId=?0 and unitType is not null order by unitType,newFlag";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", quoteId);
        return queryObject.list();
    }

    //-- get all charges by quote id, and unit types--//
    public List<Charges> getChargesByUnitType(int quoteId, String unitTypes, String splEquipment) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from Charges where qouteId = ").append(quoteId).append(" AND unitType IN (").append(unitTypes).append(") AND CONCAT(specialEquipmentUnit,standardCharge) = '").append(splEquipment).append("'");
        return getSession().createQuery(queryBuilder.toString()).list();
    }
    public List<Charges> getGroupChargesByUnitType(int quoteId, String unitTypes, String splEquipment)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("from Charges c where c.qouteId='").append(quoteId).append("' and c.unitType IN ('").append(unitTypes).append("') AND CONCAT(specialEquipmentUnit,standardCharge) ='").append(splEquipment).append("'")
                    .append("GROUP BY c.unitType,c.specialEquipmentUnit,c.standardCharge");
	return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Charges> getOtherCharges(int quoteId)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("from Charges where qouteId=").append(quoteId).append(" and unitType is null");
	return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }
    public void addAfrfeeForJapan(Integer quoteId, Double afrFee)throws Exception{
        GenericCode genericCode=genericCodeDAO.findByPropertyForChargeCode("codetypeid",36,QuotationConstants.AFRFEEFORJAPAN );
        if(null!=genericCode){
            Charges charges=new Charges();
            charges.setQouteId(quoteId);
            charges.setChgCode(genericCode.getCodedesc());
            charges.setChargeCodeDesc(genericCode.getCode());
            charges.setAmount(0d);
            charges.setMarkUp(afrFee);
            charges.setCostType("PER BL CHARGES");
            charges.setCurrecny("USD");
            charges.setChargeFlag("M");
            charges.setMinimum(0d);
            save(charges);
        }
}
     public List<Charges> getAutoRatesList(String quoteId)throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("from Charges where qouteId='").append(quoteId).append("' and (chargeFlag is null or chargeFlag ='')");
        queryBuilder.append("and (newFlag is null or newFlag='')");
	return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }
    
    public boolean isNotSpotRate(String quoteId)throws Exception{
        String q="select count(*) from charges where Qoute_ID='"+quoteId+"' and charge_flag is null and spotrate_amt is null";
        String r=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return r.equals("0");
    }
    public boolean isContainDeferral(String quoteId)throws Exception{
        String q="select count(*) from charges where Qoute_ID='"+quoteId+"' and charge_flag is null and chargecodedesc='DEFER'";
        String r=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return !r.equals("0");
    }
    
    public List<Charges> findByChargeCode(String quoteId, String chargeCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Charges.class);
        criteria.add(Restrictions.eq("qouteId", Integer.parseInt(quoteId)))
                .add(Restrictions.eq("chargeCodeDesc", chargeCode))
                .add(Restrictions.isNull("newFlag"));
        return criteria.list();
    }
    
    public void updateOfrSpotrate(QuoteChargeForm chargeForm,Charges charges ){
        StringBuilder sb=new StringBuilder();
        sb.append("update charges set spotrate_amt=").append(chargeForm.getSpotRateAmt()).append(",spotrate_markup=((amount+mark_up)-");
        sb.append(chargeForm.getSpotRateAmt()).append("),standard_chk='off',spotrate_chk='off'").append(" where qoute_id='").append(charges.getQouteId());
        sb.append("' and chargecodedesc='OCNFRT' and unit_type=").append(charges.getUnitType()).append(" and chargecodedesc <>'DEFER' and new_flag is null");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);
        
        sb.append("update charges set spotrate_amt=0.00,spotrate_markup=(amount+mark_up),standard_chk='off',spotrate_chk='on' ");
        sb.append(" where qoute_id=").append(charges.getQouteId()).append(" and unit_type=").append(charges.getUnitType()).append(" and new_flag is null");
        sb.append(" and chargecodedesc not in('OCNFRT','HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN','DEFER')");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);
        
        String spotRateamt = getspotRateamt(charges.getQouteId(),charges.getUnitType());
        if(spotRateamt != null){
         Double spotRateamtD = Double.valueOf(spotRateamt);
        sb.append("update charges set spotrate_amt=").append(spotRateamtD).append(",spotrate_markup=(amount+mark_up),standard_chk='on',spotrate_chk='off' ");
        sb.append(" where qoute_id=").append(charges.getQouteId()).append(" and unit_type=").append(charges.getUnitType()).append(" and new_flag is null");
        sb.append(" and chargecodedesc in('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN')");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        sb.setLength(0);
        }
        
    }
    public void updateNewChargesForQuote(List<BookingfclUnits> bookingFclUnitsList, Integer quoteId) throws Exception {
        StringBuilder updateQuery = new StringBuilder();
        for (BookingfclUnits bookingfclUnits : bookingFclUnitsList) {
            if (null!=bookingfclUnits.getUnitType() && CommonUtils.isNotEmpty(bookingfclUnits.getUnitType().getId())) {
                updateQuery.append("update charges set amount=").append(bookingfclUnits.getAmount()).append(" ,mark_up=").append(bookingfclUnits.getMarkUp());
                updateQuery.append(" where Qoute_ID=").append(quoteId).append(" and Unit_Type='").append(bookingfclUnits.getUnitType().getId().toString());
                updateQuery.append("' and chargeCodeDesc='").append(bookingfclUnits.getChargeCodeDesc()).append("'");
                getCurrentSession().createSQLQuery(updateQuery.toString()).executeUpdate();
                updateQuery.setLength(0);
            }
        }
    }
     public void deleteChassisCharges(int quoteId)throws Exception {
        String queryString = "delete from charges where qoute_id = " + quoteId + " and chargeCodeDesc = 'CHASFEE'";
	getCurrentSession().createSQLQuery(queryString).executeUpdate();
        getCurrentSession().flush();
}
     public String[] checkvendorForChassisCharge(String fileno) throws Exception{
         StringBuilder queryBuilder = new StringBuilder();
         queryBuilder.append("select ");
         queryBuilder.append(" c.account_name as AccountName,");
         queryBuilder.append(" c.account_no as AccountNo ");
         queryBuilder.append("from");
         queryBuilder.append(" `charges` c ");
         queryBuilder.append(" LEFT JOIN `quotation` q  ON ");
         queryBuilder.append(" q.`Quote_ID`= c.`Qoute_ID` ");
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
       public String checkvendorForChassisChargeOnload(String quoteId) throws Exception{
         String queryString = "SELECT account_no FROM charges WHERE qoute_id = '"+quoteId +"'and chargecodedesc='inland' limit 1";
         String result = (String) getCurrentSession().createSQLQuery(queryString).uniqueResult();
         return null != result ? result : "";
     }

    private String getspotRateamt(Integer id, String unitType) {
        String queryString = "select spotrate_amt from charges where qoute_id = '" + id + "' and unit_type='" + unitType + "' and chargecodedesc in('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN') and spotrate_amt is not null";
        Object result = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        if (null != result) {
            return(null != result ? result.toString() : "" );
        }
        return null;
    }
}


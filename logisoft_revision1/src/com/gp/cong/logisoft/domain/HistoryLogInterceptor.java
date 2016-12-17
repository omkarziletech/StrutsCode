package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.gp.cong.logisoft.hibernate.dao.AirRatesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFDetailsHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.FTFStandardChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadDetailsHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadStandardChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.AirCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailCommodityChargesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailOceanDetailsRatesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.RetailStandardCharges1HistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseFlatRateHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseAirFreightHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseCommodityChrgHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UniverseInsuranceChrgHistoryDAO;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import com.gp.cong.logisoft.hibernate.dao.AirAccesorialRatesHistoryDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionBuilder;
import org.hibernate.engine.spi.SessionImplementor;

public class HistoryLogInterceptor implements Interceptor,Serializable {

    private static final Logger log = Logger.getLogger(HistoryLogInterceptor.class);
    private SessionFactory sessionFactory;
    private static final String UPDATE = "update";
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private Set inserts = new HashSet();
    private Set updates = new HashSet();
    private Set deletes = new HashSet();
    boolean flag = false;
    private String userName;
    private String orgTerm;
    private String disport;
    private Integer standardId;

    public Integer getStandardId() {
	return standardId;
    }

    public void setStandardId(Integer standardId) {
	this.standardId = standardId;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Set getUpdates() {
	return updates;
    }

    public void setUpdates(Set updates) {
	this.updates = updates;
    }

    public void afterTransactionBegin(Transaction arg0) {
	// TODO Auto-generated method stub
    }

    public void afterTransactionCompletion(Transaction arg0) {
	// TODO Auto-generated method stub
    }

    public void beforeTransactionCompletion(Transaction arg0) {
	// TODO Auto-generated method stub
    }

    public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2, Object[] arg3, String[] arg4, Type[] arg5) {
	// TODO Auto-generated method stub
	return null;
    }

    public Object getEntity(String arg0, Serializable arg1) throws CallbackException {
	// TODO Auto-generated method stub
	return null;
    }

    public String getEntityName(Object arg0) throws CallbackException {
	// TODO Auto-generated method stub
	return null;
    }

    public Object instantiate(String arg0, EntityMode arg1, Serializable arg2) throws CallbackException {
	// TODO Auto-generated method stub
	return null;
    }

    public Boolean isTransient(Object arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    public void onCollectionRecreate(Object arg0, Serializable arg1) throws CallbackException {
	// TODO Auto-generated method stub
    }

    public void onCollectionRemove(Object arg0, Serializable arg1) throws CallbackException {
	// TODO Auto-generated method stub
    }

    public void onCollectionUpdate(Object arg0, Serializable arg1) throws CallbackException {
	// TODO Auto-generated method stub
    }

    public void onDelete(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
	// TODO Auto-generated method stub
    }

    public boolean onFlushDirty(Object obj, Serializable id, Object[] newValues, Object[] oldValues, String[] properties, Type[] types) throws CallbackException {
	// TODO Auto-generated method stub
	Session session1 = sessionFactory.getCurrentSession();
	SessionBuilder sb = sessionFactory.withOptions();
	Session session = sb.connection(((SessionImplementor) session1).connection()).openSession();

	Class objectClass = obj.getClass();

	String className = objectClass.getName();

	// Just get the class name without the package structure 
	String[] tokens = className.split("\\.");
	int lastToken = tokens.length - 1;
	className = tokens[lastToken];

	// Use the id and class to get the pre-update state from the database
	Serializable persistedObjectId = getObjectId(obj);
	Object preUpdateState = session.get(objectClass, persistedObjectId);


	try {
	    logChanges(obj, preUpdateState, null, persistedObjectId.toString(), UPDATE, className);
	} catch (Exception ex) {
	    log.info("Exception Happened ",ex);
	}

	session.close();
	return flag;
    }

    public boolean onLoad(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
	// TODO Auto-generated method stub
	return false;
    }

    public String onPrepareStatement(String arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean onSave(Object arg0, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
	// TODO Auto-generated method stub
	return false;
    }

    public void postFlush(Iterator arg0) throws CallbackException {
	// TODO Auto-generated method stub
	log.debug("In postFlush of AuditLogInterceptor..");

	Session session1 = sessionFactory.getCurrentSession();
	SessionBuilder sb = sessionFactory.withOptions();
	Session session = sb.connection(((SessionImplementor) session1).connection()).openSession();

	//Session session = getCurrentSession();
	try {
	    for (Iterator itr = updates.iterator(); itr.hasNext();) {
		if (itr instanceof AirStandardCharges) {
		    AirStandardCharges airStd = (AirStandardCharges) itr.next();
		    session.save(airStd);
		}
	    }

	} catch (HibernateException e) {

	    throw new CallbackException(e);
	} finally {

	    updates.clear();

	    session.flush();
	    session.close();
	}
    }

    public void preFlush(Iterator arg0) throws CallbackException {
	// TODO Auto-generated method stub
    }

    private Serializable getObjectId(Object obj) {

	Class objectClass = obj.getClass();
	Method[] methods = objectClass.getMethods();

	Serializable persistedObjectId = null;
	for (int ii = 0; ii < methods.length; ii++) {

	    // If the method name equals 'getId' then invoke it to get the id of the object.
	    if (obj instanceof RetailCommodityCharges) {
		if (methods[ii].getName().equals("getRetailCmdId")) {
		    try {


			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: ",e);
		    }
		}
	    } else if (obj instanceof RetailOceanDetailsRates) {
		if (methods[ii].getName().equals("getId")) {
		    try {


			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof RetailStandardCharges1) {
		if (methods[ii].getName().equals("getRetailStdId")) {
		    try {


			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof AirStandardCharges) {
		if (methods[ii].getName().equals("getAirStdId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof AirCommodityCharges) {
		if (methods[ii].getName().equals("getAirCmdId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof AirWeightRangesRates) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof LCLColoadStandardCharges) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    }//=========================1========================
	    else if (obj instanceof LCLColoadCommodityCharges) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    }//=========================2========================
	    else if (obj instanceof LCLColoadDetails) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } //=========================3========================
	    else if (obj instanceof FTFStandardCharges) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    }//=========================1========================
	    else if (obj instanceof FTFCommodityCharges) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    }//=========================2========================
	    //=========================3========================
	    else if (obj instanceof FTFDetails) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } else if (obj instanceof UniverseFlatRate) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);

			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } //==================================================================
	    else if (obj instanceof UniverseCommodityChrg) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } //===========================================================================
	    else if (obj instanceof UniverseAirFreight) {
		if (methods[ii].getName().equals("getId")) {
		    try {
			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    } //------------------------------------------------------
	    else if (obj instanceof UniverseInsuranceChrg) {
		if (methods[ii].getName().equals("getId")) {
		    try {

			persistedObjectId = (Serializable) methods[ii].invoke(obj, (Object) null);
			break;
		    } catch (Exception e) {
			log.info("Audit Log Failed - Could not get persisted object id: " ,e);
		    }
		}
	    }
	}
	return persistedObjectId;
    }

    public SessionFactory getSessionFactory() {
	return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    private void logChanges(Object newObject, Object existingObject, Object parentObject,
	    String persistedObjectId, String event, String className)
	    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {


	Class objectClass = newObject.getClass();

//get an array of all fields in the class including those in superclasses if this is a subclass.
	Field[] fields = getAllFields(objectClass, null);


// Iterate through all the fields in the object

	fieldIteration:
	for (int ii = 0; ii < fields.length; ii++) {

//make private fields accessible so we can access their values
	    fields[ii].setAccessible(true);

//if the current field is static, transient or final then don't log it as 
//these modifiers are v.unlikely to be part of the data model.
	    if (Modifier.isTransient(fields[ii].getModifiers()) || Modifier.isFinal(fields[ii].getModifiers()) || Modifier.isStatic(fields[ii].getModifiers())) {
		continue fieldIteration;
	    }

	    String fieldName = fields[ii].getName();

	    if (!fieldName.equals("id") && !fieldName.equals("chargeCode") && !fieldName.equals("changedDate") && !fieldName.equals("effectiveDate") && !fieldName.equals("unitType") && !fieldName.equals("chargeType") && !fieldName.equals("hash") && !fieldName.equals("weightRange")) {


		Object newComponent = fields[ii].get(newObject);

		Object existingComponent = null;

		if (event.equals(UPDATE)) {
		    existingComponent = fields[ii].get(existingObject);

		    if (existingComponent != null && newComponent != null) {
			if (!existingComponent.equals(newComponent)) {
			    //airAccesorialRatesHistory.setAirStdId((AirStandardCharges(existingObject.)
			    if (existingObject instanceof AirStandardCharges) {

				AirStandardCharges airStandardCharges = (AirStandardCharges) existingObject;
				AirAccesorialRatesHistory airAccesorialRatesHistory = new AirAccesorialRatesHistory();
				airAccesorialRatesHistory.setAirStdId(airStandardCharges.getAirStdId());
				airAccesorialRatesHistory.setStandardId(airStandardCharges.getStandardId());
				airAccesorialRatesHistory.setChargeCode(airStandardCharges.getChargeCode());
				airAccesorialRatesHistory.setChargeType(airStandardCharges.getChargeType());
				airAccesorialRatesHistory.setStandard(airStandardCharges.getStandard());
				//airAccesorialRatesHistory.setOrigin(this.getOrgTerm());
				// airAccesorialRatesHistory.setDestination(this.getDisport());
				airAccesorialRatesHistory.setAmtPerCft(airStandardCharges.getAmtPerCft());
				airAccesorialRatesHistory.setAmtPer100lbs(airStandardCharges.getAmtPer100lbs());
				airAccesorialRatesHistory.setAmtPerCbm(airStandardCharges.getAmtPerCbm());
				airAccesorialRatesHistory.setAmtPer1000kg(airStandardCharges.getAmtPer1000kg());
				airAccesorialRatesHistory.setAmount(airStandardCharges.getAmount());
				airAccesorialRatesHistory.setPercentage(airStandardCharges.getPercentage());
				airAccesorialRatesHistory.setMinAmt(airStandardCharges.getMinAmt());
				airAccesorialRatesHistory.setEffectiveDate(airStandardCharges.getEffectiveDate());
				airAccesorialRatesHistory.setChangedDate(new Date());
				airAccesorialRatesHistory.setWhoChanged(this.getUserName());
				airAccesorialRatesHistory.setInsuranceRate(airStandardCharges.getInsuranceRate());
				airAccesorialRatesHistory.setInsuranceAmt(airStandardCharges.getInsuranceAmt());
				airAccesorialRatesHistory.setAsFrfgted(airStandardCharges.getAsFrfgted());
				AirAccesorialRatesHistoryDAO airAccesorialRatesHistoryDAO = new AirAccesorialRatesHistoryDAO();

				airAccesorialRatesHistoryDAO.save(airAccesorialRatesHistory);


			    } else if (existingObject instanceof RetailStandardCharges1) {

				RetailStandardCharges1 retailStandardCharges = (RetailStandardCharges1) existingObject;
				RetailStandardCharges1History retailStandardCharges1History = new RetailStandardCharges1History();
				retailStandardCharges1History.setRetailStdId(retailStandardCharges.getRetailStdId());

				retailStandardCharges1History.setStandardId(retailStandardCharges.getStandardId());
				retailStandardCharges1History.setChargeCode(retailStandardCharges.getChargeCode());
				retailStandardCharges1History.setChargeType(retailStandardCharges.getChargeType());
				retailStandardCharges1History.setStandard(retailStandardCharges.getStandard());

				retailStandardCharges1History.setAmtPerCft(retailStandardCharges.getAmtPerCft());
				retailStandardCharges1History.setAmtPer100lbs(retailStandardCharges.getAmtPer100lbs());
				retailStandardCharges1History.setAmtPerCbm(retailStandardCharges.getAmtPerCbm());
				retailStandardCharges1History.setAmtPer1000kg(retailStandardCharges.getAmtPer1000kg());
				retailStandardCharges1History.setAmount(retailStandardCharges.getAmount());
				retailStandardCharges1History.setPercentage(retailStandardCharges.getPercentage());
				retailStandardCharges1History.setMinAmt(retailStandardCharges.getMinAmt());
				retailStandardCharges1History.setEffectiveDate(retailStandardCharges.getEffectiveDate());
				retailStandardCharges1History.setChangedDate(new Date());
				retailStandardCharges1History.setWhoChanged(this.getUserName());
				retailStandardCharges1History.setInsuranceRate(retailStandardCharges.getInsuranceRate());
				retailStandardCharges1History.setInsuranceAmt(retailStandardCharges.getInsuranceAmt());
				retailStandardCharges1History.setAsFrfgted(retailStandardCharges.getAsFrfgted());
				RetailStandardCharges1HistoryDAO retailStandardCharges1HistoryDAO = new RetailStandardCharges1HistoryDAO();
				retailStandardCharges1HistoryDAO.save(retailStandardCharges1History);
			    } else if (existingObject instanceof RetailCommodityCharges) {
				RetailCommodityCharges retailCommodityCharges = (RetailCommodityCharges) existingObject;
				RetailCommodityChargesHistory retailCommodityChargesHistory = new RetailCommodityChargesHistory();
				retailCommodityChargesHistory.setRetailCmdId(retailCommodityCharges.getRetailCmdId());
				retailCommodityChargesHistory.setRetailRatesId(retailCommodityCharges.getRetailRatesId());
				retailCommodityChargesHistory.setAmount(retailCommodityCharges.getAmount());
				retailCommodityChargesHistory.setAmtPer1000kg(retailCommodityCharges.getAmtPer1000kg());
				retailCommodityChargesHistory.setAmtPer100lbs(retailCommodityCharges.getAmtPer100lbs());
				retailCommodityChargesHistory.setAmtPerCbm(retailCommodityCharges.getAmtPerCbm());
				retailCommodityChargesHistory.setAmtPerCft(retailCommodityCharges.getAmtPerCft());
				retailCommodityChargesHistory.setAsFreightedCheckBox(retailCommodityCharges.getAsFreightedCheckBox());
				retailCommodityChargesHistory.setChangedDate(new Date());
				retailCommodityChargesHistory.setChargeCode(retailCommodityCharges.getChargeCode());
				retailCommodityChargesHistory.setChargeType(retailCommodityCharges.getChargeType());
				retailCommodityChargesHistory.setEffectiveDate(retailCommodityCharges.getEffectiveDate());
				retailCommodityChargesHistory.setInsuranceAmt(retailCommodityCharges.getInsuranceAmt());
				retailCommodityChargesHistory.setInsuranceRate(retailCommodityCharges.getInsuranceRate());
				retailCommodityChargesHistory.setMinAmt(retailCommodityCharges.getMinAmt());
				retailCommodityChargesHistory.setPercentage(retailCommodityCharges.getPercentage());
				retailCommodityChargesHistory.setStandard(retailCommodityCharges.getStandard());

				retailCommodityChargesHistory.setWhoChanged(this.getUserName());
				RetailCommodityChargesHistoryDAO retailCommodityChargesHistoryDAO = new RetailCommodityChargesHistoryDAO();
				retailCommodityChargesHistoryDAO.save(retailCommodityChargesHistory);
			    } else if (existingObject instanceof RetailOceanDetailsRates) {



				RetailOceanDetailsRates retailOceanDetailsRates = (RetailOceanDetailsRates) existingObject;
				RetailOceanDetailsRatesHistory retailOceanDetailsRatesHistory = new RetailOceanDetailsRatesHistory();
				retailOceanDetailsRatesHistory.setRetailRatesId(retailOceanDetailsRates.getRetailRatesId());
				retailOceanDetailsRatesHistory.setChangedDate(new Date());
				retailOceanDetailsRatesHistory.setMetric1000kg(retailOceanDetailsRates.getMetric1000kg());
				retailOceanDetailsRatesHistory.setMetricCmb(retailOceanDetailsRates.getMetricCmb());
				retailOceanDetailsRatesHistory.setMetricMinAmt(retailOceanDetailsRates.getMetricMinAmt());
				retailOceanDetailsRatesHistory.setEnglish1000lb(retailOceanDetailsRates.getEnglish1000lb());
				retailOceanDetailsRatesHistory.setEnglishLbs(retailOceanDetailsRates.getEnglishLbs());
				retailOceanDetailsRatesHistory.setEnglishMinAmt(retailOceanDetailsRates.getEnglishMinAmt());
				retailOceanDetailsRatesHistory.setAOcean(retailOceanDetailsRates.getAOcean());
				retailOceanDetailsRatesHistory.setATt(retailOceanDetailsRates.getATt());
				retailOceanDetailsRatesHistory.setBOcean(retailOceanDetailsRates.getBOcean());
				retailOceanDetailsRatesHistory.setBTt(retailOceanDetailsRates.getBTt());
				retailOceanDetailsRatesHistory.setCOcean(retailOceanDetailsRates.getCOcean());
				retailOceanDetailsRatesHistory.setCTt(retailOceanDetailsRates.getCTt());
				retailOceanDetailsRatesHistory.setDOcean(retailOceanDetailsRates.getDOcean());
				retailOceanDetailsRatesHistory.setDTt(retailOceanDetailsRates.getDTt());
				retailOceanDetailsRatesHistory.setEffectiveDate(retailOceanDetailsRates.getEffectiveDate());
				retailOceanDetailsRatesHistory.setWhoChanged(this.getUserName());
				RetailOceanDetailsRatesHistoryDAO retailOceanDetailsRatesHistoryDAO = new RetailOceanDetailsRatesHistoryDAO();
				retailOceanDetailsRatesHistoryDAO.save(retailOceanDetailsRatesHistory);


			    } else if (existingObject instanceof AirWeightRangesRates) {


				AirWeightRangesRates airWeightRangesRates = (AirWeightRangesRates) existingObject;
				AirRatesHistory airRatesHistory = new AirRatesHistory();
				airRatesHistory.setAirRatesId(airWeightRangesRates.getAirRatesId());
				airRatesHistory.setChangedDate(new Date());
				airRatesHistory.setDeferredMinAmt(airWeightRangesRates.getDeferredMinAmt());
				airRatesHistory.setDeferredRate(airWeightRangesRates.getDeferredRate());
				airRatesHistory.setExpressMinAmt(airWeightRangesRates.getExpressMinAmt());
				airRatesHistory.setExpressRate(airWeightRangesRates.getExpressRate());
				airRatesHistory.setGeneralMinAmt(airWeightRangesRates.getGeneralMinAmt());
				airRatesHistory.setGeneralRate(airWeightRangesRates.getGeneralRate());
				airRatesHistory.setStandardId(airWeightRangesRates.getAirRatesId());
				airRatesHistory.setWeightRange(airWeightRangesRates.getWeightRange());
				//airRatesHistory.setOrigin(this.getOrgTerm());
				// airRatesHistory.setDestination(this.getDisport());
				airRatesHistory.setWhoChanged(this.getUserName());
				AirRatesHistoryDAO airRatesHistoryDAO = new AirRatesHistoryDAO();
				airRatesHistoryDAO.save(airRatesHistory);


			    } else if (existingObject instanceof AirCommodityCharges) {

				AirCommodityCharges airCommodityCharges = (AirCommodityCharges) existingObject;
				AirCommodityChargesHistory airCommodityChargesHistory = new AirCommodityChargesHistory();
				airCommodityChargesHistory.setAirCmdId(airCommodityCharges.getAirCmdId());
				airCommodityChargesHistory.setStandardId(airCommodityCharges.getAirRatesId());
				airCommodityChargesHistory.setAmount(airCommodityCharges.getAmount());
				airCommodityChargesHistory.setAmtPer1000kg(airCommodityCharges.getAmtPer1000kg());
				airCommodityChargesHistory.setAmtPer100lbs(airCommodityCharges.getAmtPer100lbs());
				airCommodityChargesHistory.setAmtPerCbm(airCommodityCharges.getAmtPerCbm());
				airCommodityChargesHistory.setAmtPerCft(airCommodityCharges.getAmtPerCft());
				airCommodityChargesHistory.setAsFrfgted(airCommodityCharges.getAsFrfgted());
				airCommodityChargesHistory.setChangedDate(new Date());
				airCommodityChargesHistory.setChargeCode(airCommodityCharges.getChargeCode());
				airCommodityChargesHistory.setChargeType(airCommodityCharges.getChargeType());
				airCommodityChargesHistory.setEffectiveDate(airCommodityCharges.getEffectiveDate());
				airCommodityChargesHistory.setInsuranceAmt(airCommodityCharges.getInsuranceAmt());
				airCommodityChargesHistory.setInsuranceRate(airCommodityCharges.getInsuranceRate());
				airCommodityChargesHistory.setMinAmt(airCommodityCharges.getMinAmt());
				airCommodityChargesHistory.setPercentage(airCommodityCharges.getPercentage());
				airCommodityChargesHistory.setStandard(airCommodityCharges.getStandard());
				//airCommodityChargesHistory.setOrigin(this.getOrgTerm());
				// airCommodityChargesHistory.setDestination(this.getDisport());
				airCommodityChargesHistory.setWhoChanged(this.getUserName());

				AirCommodityChargesHistoryDAO airCommodityChargesHistoryDAO = new AirCommodityChargesHistoryDAO();
				airCommodityChargesHistoryDAO.save(airCommodityChargesHistory);

			    } else if (existingObject instanceof LCLColoadStandardCharges) {

				//
				LCLColoadStandardCharges lCLColoadStandardCharges = (LCLColoadStandardCharges) existingObject;
				LCLColoadStandardChargesHistory lCLColoadStandardChargesHistory = new LCLColoadStandardChargesHistory();
				lCLColoadStandardChargesHistory.setAmount(lCLColoadStandardCharges.getAmount());
				lCLColoadStandardChargesHistory.setAmtPer1000Kg(lCLColoadStandardCharges.getAmtPer1000Kg());
				lCLColoadStandardChargesHistory.setAmtPer100lbs(lCLColoadStandardCharges.getAmtPer100lbs());
				lCLColoadStandardChargesHistory.setAmtPerCbm(lCLColoadStandardCharges.getAmtPerCbm());
				lCLColoadStandardChargesHistory.setAmtPerCft(lCLColoadStandardCharges.getAmtPerCft());
				lCLColoadStandardChargesHistory.setAsFrfgted(lCLColoadStandardCharges.getAsFrfgted());
				lCLColoadStandardChargesHistory.setChangedDate(new Date());
				lCLColoadStandardChargesHistory.setChargeCode(lCLColoadStandardCharges.getChargeCode());
				lCLColoadStandardChargesHistory.setChargeType(lCLColoadStandardCharges.getChargeType());
				lCLColoadStandardChargesHistory.setEffectiveDate(lCLColoadStandardCharges.getEffectiveDate());
				lCLColoadStandardChargesHistory.setInsuranceAmt(lCLColoadStandardCharges.getInsuranceAmt());
				lCLColoadStandardChargesHistory.setInsuranceRate(lCLColoadStandardCharges.getInsuranceRate());
				lCLColoadStandardChargesHistory.setMinAmt(lCLColoadStandardCharges.getMinAmt());
				lCLColoadStandardChargesHistory.setPercentage(lCLColoadStandardCharges.getPercentage());
				lCLColoadStandardChargesHistory.setStandard(lCLColoadStandardCharges.getStandard());
				lCLColoadStandardChargesHistory.setWhoChanged(this.getUserName());


				lCLColoadStandardChargesHistory.setLclCoLoadId(lCLColoadStandardCharges.getLclCoLoadId());
				LCLColoadStandardChargesHistoryDAO lCLColoadStandardChargesHistoryDAO = new LCLColoadStandardChargesHistoryDAO();
				lCLColoadStandardChargesHistoryDAO.save(lCLColoadStandardChargesHistory);

			    } else if (existingObject instanceof LCLColoadCommodityCharges) {



				LCLColoadCommodityCharges lCLColoadCommodityCharges = (LCLColoadCommodityCharges) existingObject;
				LCLColoadCommodityChargesHistory lCLColoadCommodityChargesHistory = new LCLColoadCommodityChargesHistory();
				lCLColoadCommodityChargesHistory.setAmount(lCLColoadCommodityCharges.getAmount());
				lCLColoadCommodityChargesHistory.setAmtPer1000kg(lCLColoadCommodityCharges.getAmtPer1000kg());
				lCLColoadCommodityChargesHistory.setAmtPer100lbs(lCLColoadCommodityCharges.getAmtPer100lbs());
				lCLColoadCommodityChargesHistory.setAmtPerCbm(lCLColoadCommodityCharges.getAmtPerCbm());
				lCLColoadCommodityChargesHistory.setAmtPerCft(lCLColoadCommodityCharges.getAmtPerCft());
				lCLColoadCommodityChargesHistory.setAsFrfgted(lCLColoadCommodityCharges.getAsFrfgted());
				lCLColoadCommodityChargesHistory.setChangedDate(new Date());
				lCLColoadCommodityChargesHistory.setChargeCode(lCLColoadCommodityCharges.getChargeCode());
				lCLColoadCommodityChargesHistory.setChargeType(lCLColoadCommodityCharges.getChargeType());
				lCLColoadCommodityChargesHistory.setEffectiveDate(lCLColoadCommodityCharges.getEffectiveDate());
				lCLColoadCommodityChargesHistory.setInsuranceAmt(lCLColoadCommodityCharges.getInsuranceAmt());
				lCLColoadCommodityChargesHistory.setInsuranceRate(lCLColoadCommodityCharges.getInsuranceRate());
				lCLColoadCommodityChargesHistory.setLclCoLoadId(lCLColoadCommodityCharges.getLclCoLoadId());
				lCLColoadCommodityChargesHistory.setMinAmt(lCLColoadCommodityCharges.getMinAmt());
				lCLColoadCommodityChargesHistory.setPercentage(lCLColoadCommodityCharges.getPercentage());
				lCLColoadCommodityChargesHistory.setStandard(lCLColoadCommodityCharges.getStandard());
				lCLColoadCommodityChargesHistory.setWhoChanged(this.getUserName());


				LCLColoadCommodityChargesHistoryDAO lCLColoadCommodityChargesHistoryDAO = new LCLColoadCommodityChargesHistoryDAO();
				lCLColoadCommodityChargesHistoryDAO.save(lCLColoadCommodityChargesHistory);
			    } else if (existingObject instanceof LCLColoadDetails) {


				LCLColoadDetails lCLColoadDetails = (LCLColoadDetails) existingObject;
				LCLColoadDetailsHistory lCLColoadDetailsHistory = new LCLColoadDetailsHistory();
				lCLColoadDetailsHistory.setEnglish100lb(lCLColoadDetails.getEnglish100lb());
				lCLColoadDetailsHistory.setChangedDate(new Date());
				lCLColoadDetailsHistory.setEnglishCft(lCLColoadDetails.getEnglishCft());
				lCLColoadDetailsHistory.setEnglishOfMinamt(lCLColoadDetails.getEnglishOfMinamt());
				lCLColoadDetailsHistory.setLclCoLoadId(lCLColoadDetails.getLclCoLoadId());
				lCLColoadDetailsHistory.setMeasureType(lCLColoadDetails.getMeasureType());
				lCLColoadDetailsHistory.setMetric1000kg(lCLColoadDetails.getMetric1000kg());
				lCLColoadDetailsHistory.setMetricCbm(lCLColoadDetails.getMetricCbm());
				lCLColoadDetailsHistory.setMetricOfMinamt(lCLColoadDetails.getMetricOfMinamt());
				lCLColoadDetailsHistory.setSizeAOf(lCLColoadDetails.getSizeAOf());
				lCLColoadDetailsHistory.setSizeATt(lCLColoadDetails.getSizeATt());
				lCLColoadDetailsHistory.setSizeBOf(lCLColoadDetails.getSizeBOf());
				lCLColoadDetailsHistory.setSizeBTt(lCLColoadDetails.getSizeBTt());
				lCLColoadDetailsHistory.setSizeCOf(lCLColoadDetails.getSizeCOf());
				lCLColoadDetailsHistory.setSizeCTt(lCLColoadDetails.getSizeCTt());
				lCLColoadDetailsHistory.setSizeDOf(lCLColoadDetails.getSizeDOf());
				lCLColoadDetailsHistory.setSizeDTt(lCLColoadDetails.getSizeDTt());
				lCLColoadDetailsHistory.setEffectiveDate(lCLColoadDetails.getEffectiveDate());
				lCLColoadDetailsHistory.setWhoChanged(this.getUserName());


				LCLColoadDetailsHistoryDAO lCLColoadDetailsHistoryDAO = new LCLColoadDetailsHistoryDAO();
				lCLColoadDetailsHistoryDAO.save(lCLColoadDetailsHistory);
			    } //for ftf history
			    else if (existingObject instanceof FTFStandardCharges) {

				FTFStandardCharges ftfStandardCharges = (FTFStandardCharges) existingObject;
				FTFStandardChargesHistory ftfStandardChargesHistory = new FTFStandardChargesHistory();
				ftfStandardChargesHistory.setAmount(ftfStandardCharges.getAmount());
				ftfStandardChargesHistory.setAmtPer1000Kg(ftfStandardCharges.getAmtPer1000Kg());
				ftfStandardChargesHistory.setAmtPer100lbs(ftfStandardCharges.getAmtPer100lbs());
				ftfStandardChargesHistory.setAmtPerCbm(ftfStandardCharges.getAmtPerCbm());
				ftfStandardChargesHistory.setAmtPerCft(ftfStandardCharges.getAmtPerCft());
				ftfStandardChargesHistory.setAsFrfgted(ftfStandardCharges.getAsFrfgted());
				ftfStandardChargesHistory.setChangedDate(new Date());
				ftfStandardChargesHistory.setChargeCode(ftfStandardCharges.getChargeCode());
				ftfStandardChargesHistory.setEffectiveDate(ftfStandardCharges.getEffectiveDate());
				ftfStandardChargesHistory.setInsuranceAmt(ftfStandardCharges.getInsuranceAmt());
				ftfStandardChargesHistory.setInsuranceRate(ftfStandardCharges.getInsuranceRate());
				ftfStandardChargesHistory.setMinAmt(ftfStandardCharges.getMinAmt());
				ftfStandardChargesHistory.setPercentage(ftfStandardCharges.getPercentage());
				ftfStandardChargesHistory.setStandard(ftfStandardCharges.getStandard());
				ftfStandardChargesHistory.setWhoChanged(this.getUserName());
				ftfStandardChargesHistory.setFtfId(ftfStandardCharges.getFtfId());
				FTFStandardChargesHistoryDAO ftfStandardChargesHistoryDAO = new FTFStandardChargesHistoryDAO();
				ftfStandardChargesHistoryDAO.save(ftfStandardChargesHistory);

			    } else if (existingObject instanceof FTFCommodityCharges) {
				FTFCommodityCharges ftfCommodityCharges = (FTFCommodityCharges) existingObject;
				FTFCommodityChargesHistory ftfCommodityChargesHistory = new FTFCommodityChargesHistory();
				ftfCommodityChargesHistory.setAmount(ftfCommodityCharges.getAmount());
				ftfCommodityChargesHistory.setAmtPer1000kg(ftfCommodityCharges.getAmtPer1000kg());
				ftfCommodityChargesHistory.setAmtPer100lbs(ftfCommodityCharges.getAmtPer100lbs());
				ftfCommodityChargesHistory.setAmtPerCbm(ftfCommodityCharges.getAmtPerCbm());
				ftfCommodityChargesHistory.setAmtPerCft(ftfCommodityCharges.getAmtPerCft());
				ftfCommodityChargesHistory.setAsFrfgted(ftfCommodityCharges.getAsFrfgted());
				ftfCommodityChargesHistory.setChangedDate(new Date());
				ftfCommodityChargesHistory.setChargeCode(ftfCommodityCharges.getChargeCode());
				ftfCommodityChargesHistory.setChargeType(ftfCommodityCharges.getChargeType());
				ftfCommodityChargesHistory.setEffectiveDate(ftfCommodityCharges.getEffectiveDate());
				ftfCommodityChargesHistory.setInsuranceAmt(ftfCommodityCharges.getInsuranceAmt());
				ftfCommodityChargesHistory.setInsuranceRate(ftfCommodityCharges.getInsuranceRate());
				ftfCommodityChargesHistory.setFtfId(ftfCommodityCharges.getFtfId());
				ftfCommodityChargesHistory.setMinAmt(ftfCommodityCharges.getMinAmt());
				ftfCommodityChargesHistory.setPercentage(ftfCommodityCharges.getPercentage());
				ftfCommodityChargesHistory.setStandard(ftfCommodityCharges.getStandard());
				ftfCommodityChargesHistory.setWhoChanged(this.getUserName());

				FTFCommodityChargesHistoryDAO ftfCommodityChargesHistoryDAO = new FTFCommodityChargesHistoryDAO();
				ftfCommodityChargesHistoryDAO.save(ftfCommodityChargesHistory);
			    } else if (existingObject instanceof FTFDetails) {
				FTFDetails ftfDetails = (FTFDetails) existingObject;
				FTFDetailsHistory ftfDetailsHistory = new FTFDetailsHistory();
				ftfDetailsHistory.setEnglish100lb(ftfDetails.getEnglish100lb());
				ftfDetailsHistory.setChangedDate(new Date());
				ftfDetailsHistory.setEnglishCft(ftfDetails.getEnglishCft());
				ftfDetailsHistory.setEnglishOfMinamt(ftfDetails.getEnglishOfMinamt());
				ftfDetailsHistory.setFtfId(ftfDetails.getFtfId());
				ftfDetailsHistory.setMeasureType(ftfDetails.getMeasureType());
				ftfDetailsHistory.setMetric1000kg(ftfDetails.getMetric1000kg());
				ftfDetailsHistory.setMetricCbm(ftfDetails.getMetricCbm());
				ftfDetailsHistory.setMetricOfMinamt(ftfDetails.getMetricOfMinamt());
				ftfDetailsHistory.setSizeAOf(ftfDetails.getSizeAOf());
				ftfDetailsHistory.setSizeATt(ftfDetails.getSizeATt());
				ftfDetailsHistory.setSizeBOf(ftfDetails.getSizeBOf());
				ftfDetailsHistory.setSizeBTt(ftfDetails.getSizeBTt());
				ftfDetailsHistory.setSizeCOf(ftfDetails.getSizeCOf());
				ftfDetailsHistory.setSizeCTt(ftfDetails.getSizeCTt());
				ftfDetailsHistory.setSizeDOf(ftfDetails.getSizeDOf());
				ftfDetailsHistory.setSizeDTt(ftfDetails.getSizeDTt());
				ftfDetailsHistory.setWhoChanged(this.getUserName());
				ftfDetailsHistory.setEffectiveDate(ftfDetails.getEffectiveDate());
				FTFDetailsHistoryDAO ftfDetailsHistoryDAO = new FTFDetailsHistoryDAO();
				ftfDetailsHistoryDAO.save(ftfDetailsHistory);
			    } else if (existingObject instanceof UniverseFlatRate) {

				UniverseFlatRate universeFlatRate = (UniverseFlatRate) existingObject;
				UniverseFlatRateHistory universeFlatRateHistory = new UniverseFlatRateHistory();
				universeFlatRateHistory.setChangedDate(new Date());
				universeFlatRateHistory.setAmount(universeFlatRate.getAmount());
				universeFlatRateHistory.setUniverseId(universeFlatRate.getUniverseId());
				universeFlatRateHistory.setUnitType(universeFlatRate.getUnitType());
				universeFlatRateHistory.setWhoChanged(this.getUserName());
				UniverseFlatRateHistoryDAO universeFlatRateHistoryDAO = new UniverseFlatRateHistoryDAO();
				universeFlatRateHistoryDAO.save(universeFlatRateHistory);
			    } else if (existingObject instanceof UniverseCommodityChrg) {


				UniverseCommodityChrg uniommodityCharges = (UniverseCommodityChrg) existingObject;
				UniverseCommodityChrgHistory uniCommodityChargesHistory = new UniverseCommodityChrgHistory();
				uniCommodityChargesHistory.setAmount(uniommodityCharges.getAmount());
				uniCommodityChargesHistory.setAmtPer1000kg(uniommodityCharges.getAmtPer1000kg());
				uniCommodityChargesHistory.setAmtPer100lbs(uniommodityCharges.getAmtPer100lbs());
				uniCommodityChargesHistory.setAmtPerCbm(uniommodityCharges.getAmtPerCbm());
				uniCommodityChargesHistory.setAmtPerCft(uniommodityCharges.getAmtPerCft());
				uniCommodityChargesHistory.setAsFrfgted(uniommodityCharges.getAsFrfgted());
				uniCommodityChargesHistory.setChangedDate(new Date());
				uniCommodityChargesHistory.setChargeCode(uniommodityCharges.getChargeCode());
				uniCommodityChargesHistory.setChargeType(uniommodityCharges.getChargeType());
				uniCommodityChargesHistory.setEffectiveDate(uniommodityCharges.getEffectiveDate());
				uniCommodityChargesHistory.setInsuranceAmt(uniommodityCharges.getInsuranceAmt());
				uniCommodityChargesHistory.setInsuranceRate(uniommodityCharges.getInsuranceRate());
				uniCommodityChargesHistory.setUniverseId(uniommodityCharges.getUniverseId());
				uniCommodityChargesHistory.setMinAmt(uniommodityCharges.getMinAmt());
				uniCommodityChargesHistory.setPercentage(uniommodityCharges.getPercentage());
				uniCommodityChargesHistory.setStandard(uniommodityCharges.getStandard());
				uniCommodityChargesHistory.setWhoChanged(this.getUserName());

				UniverseCommodityChrgHistoryDAO uniCommodityChargesHistoryDAO = new UniverseCommodityChrgHistoryDAO();
				uniCommodityChargesHistoryDAO.save(uniCommodityChargesHistory);
			    } else if (existingObject instanceof UniverseAirFreight) {
				UniverseAirFreight universeAirFreight = (UniverseAirFreight) existingObject;
				UniverseAirFreightHistory universeFlatRateHistory = new UniverseAirFreightHistory();
				universeFlatRateHistory.setChangedDate(new Date());
				universeFlatRateHistory.setAmount(universeAirFreight.getAmount());
				universeFlatRateHistory.setUniverseId(universeAirFreight.getUniverseId());
				universeFlatRateHistory.setWeightRange(universeAirFreight.getWeightRange());
				universeFlatRateHistory.setWhoChanged(this.getUserName());
				UniverseAirFreightHistoryDAO universeAirFreightHistoryDAO = new UniverseAirFreightHistoryDAO();
				universeAirFreightHistoryDAO.save(universeFlatRateHistory);
			    } else if (existingObject instanceof UniverseInsuranceChrg) {
				UniverseInsuranceChrg universeAirFreight = (UniverseInsuranceChrg) existingObject;
				UniverseInsuranceChrgHistory universeInsuranceChrgHistory = new UniverseInsuranceChrgHistory();
				universeInsuranceChrgHistory.setChangedDate(new Date());
				universeInsuranceChrgHistory.setInsuranceAmount(universeAirFreight.getInsuranceAmount());
				universeInsuranceChrgHistory.setUniverseId(universeAirFreight.getUniverseId());
				universeInsuranceChrgHistory.setPerValue(universeAirFreight.getPerValue());
				universeInsuranceChrgHistory.setWhoChanged(this.getUserName());
				UniverseInsuranceChrgHistoryDAO universeInsuranceChrgHistoryDAO = new UniverseInsuranceChrgHistoryDAO();
				universeInsuranceChrgHistoryDAO.save(universeInsuranceChrgHistory);

			    }
			    flag = true;
			    break;
			}
		    } else {
			//if neither objects contain the component then don't log anything
			continue fieldIteration;
		    }
		}

		if (newComponent == null) {
		    continue fieldIteration;
		}

		logChanges(newComponent, existingComponent, newObject, persistedObjectId, event, className);
		continue fieldIteration;

	    }


	    /*String propertyNewState;
	     String propertyPreUpdateState;
	     List newSet=new ArrayList();
	     List oldSet=new ArrayList();
	     //get new field values
            
	     try {
	     Object objPropNewState = fields[ii].get(newObject);
            
            
	     if (objPropNewState != null) {
            
            
	     propertyNewState = objPropNewState.toString();
            
	     } else {
	     propertyNewState = "";
	     }
            
	     } catch (Exception e) {
	     propertyNewState = "";
	     }
            
            
	     if(event.equals(UPDATE)) {
            
	     try {
	     Object objPreUpdateState = fields[ii].get(existingObject);
            
            
	     if (objPreUpdateState != null) {
	     propertyPreUpdateState=objPreUpdateState.toString();
	     } else {
	     propertyPreUpdateState = "";
	     }
	     } catch (Exception e) {
	     propertyPreUpdateState = "";
	     }
            
	     // Now we have the two property values - compare them
            
	     if (propertyNewState.equals(propertyPreUpdateState)) {
            
	     continue; // Values haven't changed so loop to next property
	     } else  {
            
	     flag=true;
	     break;
	     }
            
            
	     } */
	}

    }

    private Field[] getAllFields(Class objectClass, Field[] fields) {

	Field[] newFields = objectClass.getDeclaredFields();


	int fieldsSize = 0;
	int newFieldsSize = 0;

	if (fields != null) {
	    fieldsSize = fields.length;
	}

	if (newFields != null) {
	    newFieldsSize = newFields.length;

	}

	Field[] totalFields = new Field[fieldsSize + newFieldsSize];

	if (fieldsSize > 0) {
	    System.arraycopy(fields, 0, totalFields, 0, fieldsSize);
	}

	if (newFieldsSize > 0) {
	    System.arraycopy(newFields, 0, totalFields, fieldsSize, newFieldsSize);
	}

	Class superClass = objectClass.getSuperclass();

	Field[] finalFieldsArray;

	if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
	    finalFieldsArray = getAllFields(superClass, totalFields);
	} else {
	    finalFieldsArray = totalFields;
	}

	return finalFieldsArray;

    }

    public String getDisport() {
	return disport;
    }

    public void setDisport(String disport) {
	this.disport = disport;
    }

    public String getOrgTerm() {
	return orgTerm;
    }

    public void setOrgTerm(String orgTerm) {
	this.orgTerm = orgTerm;
    }
}

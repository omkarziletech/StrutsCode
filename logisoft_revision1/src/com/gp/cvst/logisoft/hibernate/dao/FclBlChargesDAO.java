package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.hibernate.FclBlCostcodes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;
import java.sql.Statement;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.FclBlChargeBean;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import org.hibernate.LockOptions;
import org.hibernate.engine.spi.SessionImplementor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.ObjectType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class FclBl.
 * 
 * @see com.gp.cvst.logisoft.hibernate.dao.FclBl
 * @author MyEclipse Persistence Tools
 */
public class FclBlChargesDAO extends BaseHibernateDAO {

    public void save(FclBlCharges transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(FclBlCharges persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public void delete(FclBlCharges persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public FclBlCharges findById(java.lang.Integer id) throws Exception {
        FclBlCharges instance = (FclBlCharges) getSession().get(
                "com.gp.cvst.logisoft.domain.FclBlCharges", id);
        return instance;
    }

    public List findByExample(FclBlCharges instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.hibernate.dao.FclBlCharges").add(
                Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria=getSession().createCriteria(FclBlCharges.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }

    public List findByPropertyAndBlNumber(String propertyName, Object value, Integer bolId) throws Exception {
        String queryString = "from FclBlCharges as model where model." + propertyName + "= ?0 and bolId=?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        queryObject.setParameter("1", bolId);
        return queryObject.list();
    }

    public List findAll() throws Exception {
        String queryString = "from FclBlCharges";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findByParentId(Integer bolId) throws Exception {
        String queryString = "from FclBlCharges where bolId='" + bolId + "' order by chargesId";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List deleteCharges(Integer bolId) throws Exception {
        String queryString = "FROM FclBlCharges	 WHERE bolId=" + bolId + " AND chargeCode IN("
                + "'" + FclBlConstants.ADVANCEFFCODE + "','" + FclBlConstants.ADVANCESURCHARGECODE + "','" + FclBlConstants.ADVANCESHIPPERCODE + "')";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public void deleteInsuranceCharges(Integer bolId) throws Exception {
        String queryString = "Delete FROM FclBlCharges where  bolId=" + bolId + " AND chargeCode = 'INSURE'";
        getSession().createQuery(queryString).executeUpdate();
    }

    public void deleteCharges(Integer bolId, String chargeCode) throws Exception {
        String queryString = "Delete FROM FclBlCharges where  bolId=" + bolId + " AND chargeCode = '" + chargeCode + "'";
        getSession().createQuery(queryString).executeUpdate();
    }

    public List findAATransactions(Integer bolId) throws Exception {
        List resultSet = new ArrayList();
        String queryString = "select sum(amount),billTo,currencyCode from FclBlCharges where bolId=?0 group by bolId,billTo";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", bolId);
        for (int i = 0; i < queryObject.list().size(); i++) {
            Object[] row = (Object[]) queryObject.list().get(i);
            FclBlCharges fclCharges = new FclBlCharges();
            fclCharges.setAmount((Double) row[0]);
            fclCharges.setBillTo((String) row[1]);
            fclCharges.setCurrencyCode((String) row[2]);
            resultSet.add(fclCharges);
        }
        return resultSet;
    }

    public void attachDirty(FclBlCharges instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(FclBlCharges instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public int readytoUpdate(String readyToPost, Integer bolId) throws Exception {
        String queryString = "update FclBlCharges set readyToPost='" + readyToPost + "' where bolId='" + bolId + "'";
        int id = getCurrentSession().createQuery(queryString).executeUpdate();
        return id;
    }

    public List getChargesForaBL(Integer fclblId) throws Exception {
        List<FclBlChargeBean> chargeList = new ArrayList<FclBlChargeBean>();
        String queryString = "Select fblc.bolId,fblc.chargeCode,fblc.billTo,fblc.amount,"
                + "fblc.billTrdPrty,fblc.thirdPartyName,fblc.currencyCode,fblc.readyToPost,fblc.chargesId from "
                + "FclBlCharges fblc where fblc.bolId='" + fclblId + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        FclBlChargeBean fclbean = null;
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            fclbean = new FclBlChargeBean();
            Integer bolid = (Integer) row[0];
            String charges = (String) row[1];
            String billTo = (String) row[2];
            Double amount = (Double) row[3];
            String billTrdPrty = (String) row[4];
            String thirdPartyName = (String) row[5];
            String currencyCode = (String) row[6];
            String readyToPost = (String) row[7];
            Integer chargeId = (Integer) row[8];
            fclbean.setBillofLaddingNo(bolid.toString());
            fclbean.setBillTo(billTo);
            fclbean.setChargeAmt(String.valueOf(amount));
            fclbean.setChargeCode(charges);
            fclbean.setCurrencyCode(currencyCode);
            fclbean.setThirdPartyName(thirdPartyName);
            fclbean.setThirdPartyNo(billTrdPrty);
            fclbean.setReadyToPost(readyToPost);
            fclbean.setChargeId(chargeId);
            chargeList.add(fclbean);
            fclbean = null;

        }
        return chargeList;

    }

    public FclBlCharges getPerticularCharge(String chargeCode, String bol) throws Exception {
        String queryString = "from FclBlCharges where chargeCode like  '" + chargeCode + "%' and bolId = '" + bol + "'";
        return (FclBlCharges)getSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public List<Object[]> getAllCharges(String FileNo) throws Exception {
        List<Object[]> chargesList = new ArrayList<Object[]>();
        String queryString = "SELECT chargesRemarks,chargesId,chargeCode FROM FclBlCharges WHERE bolid=(SELECT bol FROM FclBl WHERE fileNo='" + FileNo + "')";
        chargesList = getCurrentSession().createQuery(queryString).list();
        return chargesList;
    }

    public Object sumOfADVFFandADVSHP(Integer bolId) throws Exception {
        String queryString = "select sum(amount) FROM FclBlCharges	 WHERE bolId=" + bolId + " AND chargeCode IN("
                + "'" + FclBlConstants.ADVANCEFFCODE + "','" + FclBlConstants.ADVANCESHIPPERCODE + "')";
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }

    public Object sumOFCharges(Integer bolId) throws Exception {
        String queryString = "select sum(amount) FROM FclBlCharges	 WHERE bolId=" + bolId;
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }

    public List sumOFChargesForCafCalculation(Integer bolId) throws Exception {
        String queryString = "select sum(amount),billTo FROM FclBlCharges	 WHERE bolId=" + bolId + " and chargeCode != 'CAF' group by bolId";
        return getSession().createQuery(queryString).list();
    }

    public Object sumOfCollectCharges(Integer bolId) throws Exception {
        String queryString = "select sum(amount) FROM FclBlCharges	 WHERE bolId=" + bolId + " and billTo = 'Consignee'";
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }

    public List<String> getDistinctBillTo(String bolId) throws Exception {
        String queryString = "select DISTINCT(billTo) FROM FclBlCharges WHERE bolId='" + bolId + "'";
        List billToList = getSession().createQuery(queryString).list();
        return billToList;
    }

    public List<String> getDistinctBillToParty(String bolId) throws Exception {
        String queryString = "select DISTINCT(bill_to) FROM update_party_value  WHERE bolid='" + bolId + "'";
        List billToList = getSession().createSQLQuery(queryString).list();
        return billToList;
    }

    public void deleteRecordsFromUpdatedPartyTable(Integer bolId) throws Exception {
	SessionImplementor sim = (SessionImplementor)getSession();
	Connection conn = sim.connection();
        Statement st = conn.createStatement();
        st.execute("DELETE FROM update_party_value where  bolid=" + bolId + "");
            st.close();
	
        }

    public List getFclBlChargeBillTo(String billTo, Integer bolId) throws Exception {
        String queryString = "FROM FclBlCharges WHERE billTo='" + billTo + "'and  bolId='" + bolId + "'";
        List billToList = getSession().createQuery(queryString).list();
        return billToList;
    }
    
    public void updateFclCharge(Serializable id, Map<Serializable, Serializable> fields) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("update");
	queryBuilder.append("  fcl_bl_charges ");
	queryBuilder.append("set");
	int rowCount = 0;
	for (Serializable key : fields.keySet()) {
	    Serializable value = fields.get(key);
	    queryBuilder.append("  ").append(key).append(" = ");
	    if (null == value) {
		queryBuilder.append(" null");
	    } else {
		queryBuilder.append(" '").append(value).append("'");
	    }
	    if (rowCount < fields.size() - 1) {
		queryBuilder.append(",");
	    }
	    rowCount++;
	}
	queryBuilder.append("  where charges_id = ").append(id);
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
    
    public void deleteIncentAndAdvSurCharge(Integer bolId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from fcl_bl_charges where  bolid=").append(bolId).append(" and charge_code = 'INCENT' and fae_incent_flag='Y'");
        getSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder.setLength(0);
        queryBuilder.append("delete from fcl_bl_charges where bolid='").append(bolId).append("' and charge_code in('ADVSHP','ADVFF','ADVSUR')");
        getSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        
    }
     public void deleteFaeIncentCharge(Integer bolId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from fcl_bl_charges where  bolid=").append(bolId).append(" and charge_code = 'INCENT' and fae_incent_flag='Y'");
        getSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        
    }
    
    public double getAmountBychargeCode(Integer bol,String chargeCode) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(amount) from fcl_bl_charges where bolid='").append(bol).append("' and charge_code='").append(chargeCode).append("'");
        String result=getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult().toString();
        return Double.parseDouble(result);
    } 
    public String getChargCodeType(Integer bol) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select (group_concat( distinct charge_code)) as chargecode from fcl_bl_charges where bolid='").append(bol).append("' and charge_code in ('ADVSHP','ADVFF')");
        Object result=getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        int resultLenght=null!=result ?result.toString().length():0;
        return resultLenght==12?"ADVSHP,ADVFF":resultLenght==6?"ADVSHP":resultLenght==5?"ADVFF":"";
    }
    public Double getIncentAmount(Integer bolId) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select amount from fcl_bl_charges where bolid='").append(bolId).append("' and  charge_code='INCENT' and fae_incent_flag='Y'");
        Object result=getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return null!=result?Double.parseDouble(result.toString()):0;
    }
    public void deleteTransactionLedger(String fileNO, Integer bolid) throws Exception {
        StringBuilder deleteTransactionLedger = new StringBuilder();
//        String filePre = CommonConstants.loadMessageResources().getMessage("fileNumberPrefix").replace("-", "");
//        String newFileNo = filePre + fileNO;
        deleteTransactionLedger.append("delete from transaction_ledger where drcpt='").append(fileNO).append("' and Transaction_Type='AC' and status='Open' ").append(" and (shipment_type ='FCLI' or  shipment_type ='FCLE') ");
        deleteTransactionLedger.append("and cost_id in (select code_id from fcl_bl_costcodes where bolid=").append(bolid).append(" and (Transaction_Type not in ('AP','IP','DS','PN') or Transaction_Type is null))");
        getSession().createSQLQuery(deleteTransactionLedger.toString()).executeUpdate();
    }

    public void deleteBookingChargesAndCost(Integer bolid) throws Exception {
        String deleteCharge = "delete from fcl_bl_charges where bolid='" + bolid + "' and not (read_only_flag is null and booking_flag is null)";
        getSession().createSQLQuery(deleteCharge).executeUpdate();
        String deleteCost = "delete from fcl_bl_costcodes where bolid = " + bolid + " and (Transaction_Type not in ('AP', 'IP', 'DS', 'PN') or Transaction_Type is null) and  not (read_only_flag is null and booking_flag is null)";
        getSession().createSQLQuery(deleteCost).executeUpdate();
    }

    public List getbookingContainerChargesAndCostForBl(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(if(book.spot_rate = 'Y' and bu.spotrate_amt is not null, bu.spotrate_amt, bu.amount))");
        queryBuilder.append(",sum(if(book.spot_rate = 'Y'and bu.spotrate_amt is not null,bu.spotrate_amt,bu.amount)");
        queryBuilder.append(" + if(book.spot_rate = 'Y'and bu.spotrate_markup is not null,bu.spotrate_markup,bu.mark_up))");
        queryBuilder.append(",bu.Chg_Code,bu.ChargeCodeDesc,");
        queryBuilder.append("bu.account_no,bu.account_name,bu.new_flag,bu.comment,sum(bu.mark_up),sum(bu.adjustment) ");
        queryBuilder.append("from booking_fcl book join fcl_bl bl on book.file_no=bl.file_no ");
        queryBuilder.append("join fcl_bl_container_dtls con on con.BolId=bl.Bol ");
        queryBuilder.append("join bookingfcl_units bu on (bu.BookingNumber=book.BookingNumber and (bu.UnitType=con.size_legend or bu.UnitType is null) ");
        queryBuilder.append("and (con.disabled_flag = 'E' or con.disabled_flag is null))");
        queryBuilder.append("where book.file_no='").append(fileNo);
        queryBuilder.append("' group by bu.ChargeCodeDesc");
        return getSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public String getBlLevelCost() throws Exception {
        String q = "select group_concat(charge_code) from gl_mapping where shipment_type='fcle' and transaction_type = 'ac' and bl_level_cost =1";
        Object result = getSession().createSQLQuery(q).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public Double getSingleCostAmountForBlLevlCostCode(String chargeCode, String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(t.amount) ");
        queryBuilder.append("from (select sum(bu.amount) as amount from booking_fcl book join fcl_bl bl on book.file_no=bl.file_no ");
        queryBuilder.append("join fcl_bl_container_dtls con on con.BolId=bl.Bol ");
        queryBuilder.append("join bookingfcl_units bu on (bu.BookingNumber=book.BookingNumber and bu.UnitType=con.size_legend ");
        queryBuilder.append("and (bu.UnitType=con.size_legend and con.disabled_flag = 'E' or con.disabled_flag is null)) ");
        queryBuilder.append("where book.file_no='").append(fileNo);
        queryBuilder.append("' and bu.ChargeCodeDesc='").append(chargeCode).append("' ");
        queryBuilder.append("group by bu.UnitType) as t");
        Object costAmount = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != costAmount ? Double.parseDouble(costAmount.toString()) : 0d;
    }

    public String getMultibleContainerCostCodes(String fileNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select group_concat(distinct bu.ChargeCodeDesc) ");
        queryBuilder.append(" from booking_fcl book ");
        queryBuilder.append("join fcl_bl bl on book.file_no=bl.file_no ");
        queryBuilder.append("join fcl_bl_container_dtls con on con.BolId=bl.Bol ");
        queryBuilder.append("join bookingfcl_units bu on (bu.BookingNumber=book.BookingNumber and bu.UnitType=con.size_legend ");
        queryBuilder.append("and (bu.UnitType=con.size_legend and con.disabled_flag = 'E' or con.disabled_flag is null)) ");
        queryBuilder.append("where book.file_no ='").append(fileNo).append("' and numbers>1 ");
        Object r = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != r ? r.toString() : "";
    }

    public Set<FclBlCharges> geBookingChargesList(Integer bol) throws Exception {
        String q = "from FclBlCharges where bolId='" + bol + "' and not (readOnlyFlag is null and bookingFlag is null)";
        return new HashSet<FclBlCharges>(getSession().createQuery(q).list());
    }

    public Set<FclBlCostCodes> getBookingCostList(Integer bol) throws Exception {
        String q = "from FclBlCostCodes where bolId='" + bol + "' and not (readOnlyFlag is null and bookingFlag is null) and (transactionType not in ('AP', 'IP', 'DS', 'PN') or transactionType is null)";
        return new HashSet<FclBlCostCodes>(getSession().createQuery(q).list());
    }

    public String itHasFaeAndFFCom(String fileNo) throws Exception {
        String q = "select group_concat(distinct c.cost_code) from fcl_bl f join fcl_bl_costcodes c on (f.bol=c.bolid) where f.file_no='" + fileNo + "' and c.cost_code in ('FFCOMM','FAECOMM')";
        Object r=getSession().createSQLQuery(q).uniqueResult();
        return null!=r?r.toString():"";
    }
    public void deleteNonCollapseChargesForOcfr(Integer bol,String chargeCodes) throws Exception {
        String q="delete from fcl_bl_charges where bolId="+bol+" and charge_code not in("+chargeCodes+")";
        getCurrentSession().createSQLQuery(q).executeUpdate();
    }
    
    public List getAccountForChargeCodeFromGlAndTerminal(String fileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  t.`charge_code` as chargeCode, ");
        sb.append("  `IsValidGlAccount` (t.`account`) AS valid, ");
        sb.append("  IF(t.read_only_flag IS NULL OR t. booking_flag IS NOT NULL,'manual','') AS manualStatus");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    chg.`charge_code`,");
        sb.append("    `DeriveGlAccount` (");
        sb.append("      chg.`charge_code`,");
        sb.append("      IF(");
        sb.append("        bl.`importflag` = 'I',");
        sb.append("        'FCLI',");
        sb.append("        'FCLE'");
        sb.append("      ),");
        sb.append("      'AR',");
        sb.append("      SUBSTRING_INDEX(bl.`billing_terminal`, '-', - 1)");
        sb.append("    ) AS account,");
        sb.append(" chg.`read_only_flag`,");
        sb.append(" chg.`booking_flag`");
        sb.append("  FROM");
        sb.append("    `fcl_bl` bl ");
        sb.append("    JOIN `fcl_bl_charges` chg ");
        sb.append("      ON (bl.`bol` = chg.`bolid`) ");
        sb.append("  WHERE bl.`file_no` = :fileNo) AS t");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileNo", fileNumber);
        queryObject.addScalar("chargeCode", StringType.INSTANCE);
        queryObject.addScalar("valid", BooleanType.INSTANCE);
        queryObject.addScalar("manualStatus", StringType.INSTANCE);
        return queryObject.list();
    }
    public List getAccountForCostCodeFromGlAndTerminal(String fileNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  t.`cost_code` AS costCode,");
        sb.append("  `IsValidGlAccount` (t.`account`) AS valid,");
        sb.append("  IF(t.read_only_flag IS NULL OR t. booking_flag IS NOT NULL,'manual','') AS manualStatus ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    cost.`cost_code`,");
        sb.append(" cost.`read_only_flag`,");
        sb.append(" cost.`booking_flag`,");
        sb.append("    `DeriveGlAccount` (");
        sb.append("      cost.`cost_code`,");
        sb.append("      IF(");
        sb.append("        bl.`importflag` = 'I',");
        sb.append("        'FCLI',");
        sb.append("        'FCLE'");
        sb.append("      ),");
        sb.append("      'AC',");
        sb.append("      SUBSTRING_INDEX(bl.`billing_terminal`, '-', - 1)");
        sb.append("    ) AS account ");
        sb.append("  FROM");
        sb.append("    `fcl_bl` bl ");
        sb.append("    JOIN `fcl_bl_costcodes` cost ");
        sb.append("      ON (bl.`bol` = cost.`BolId`) ");
        sb.append("  WHERE bl.`file_no` = :fileNo ");
        sb.append("  AND cost.`delete_flag` <> 'yes') AS t ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("fileNo", fileNumber);
        queryObject.addScalar("costCode", StringType.INSTANCE);
        queryObject.addScalar("valid", BooleanType.INSTANCE);
        queryObject.addScalar("manualStatus", StringType.INSTANCE);
        return queryObject.list();
    }
}

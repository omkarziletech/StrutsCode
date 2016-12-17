package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadApplicationProperties;
import com.gp.cvst.logisoft.beans.FclBlChargeBean;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

/**
 * Data access object (DAO) for domain model class FclBl.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.FclBl
 * @author MyEclipse Persistence Tools
 */
public class FclBlCostCodesDAO extends BaseHibernateDAO {
    
    public void save(FclBlCostCodes transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }
    
    public Integer saveAndReturnId(FclBlCostCodes transientInstance) throws Exception {
        getSession().save(transientInstance);
        return transientInstance.getCodeId();
    }
    
    public int readytoUpdate(String readyToPost, Integer bolId) throws Exception {
        String queryString = "update FclBlCostCodes set readyToPost='" + readyToPost + "' "
                + "where bolId='" + bolId + "'";
        int id = getCurrentSession().createQuery(queryString).executeUpdate();
        return id;
    }
    
    public void update(FclBlCostCodes persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
    }
    
    public void delete(FclBlCostCodes persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }
    
    public List findByExample(FclBlCostCodes instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.hibernate.dao.FclBlCharges").add(
                Example.create(instance)).list();
        return results;
    }
    
    public List findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria=getSession().createCriteria(FclBlCostCodes.class);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }
    
    public List findByPropertyAndBlNumber(String propertyName, Object value, Integer bolId) throws Exception {
        String queryString = "from FclBlCostCodes as model where model." + propertyName + "= ?0 and bolId=?1 and  deleteFlag='no' order by codeId";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        queryObject.setParameter("1", bolId);
        return queryObject.list();
    }
    
    public List findAll() throws Exception {
        String queryString = "from FclBlCostCodes";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public List findByParenId(Integer bolId) throws Exception {
        String queryString = "from FclBlCostCodes where bolId='" + bolId + "'and deleteFlag = 'no' order by codeId";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public List findByParentIdforManifest(Integer bolId) {
        String queryString = "from FclBlCostCodes where bolId='" + bolId + "' order by codeId";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public List findByParentIdAndCostCode(Integer bolId, String costCode) throws Exception {
        String queryString = "from FclBlCostCodes where bolId='" + bolId + "' and costCode='" + costCode + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public void attachDirty(FclBlCostCodes instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }
    
    public void attachClean(FclBlCostCodes instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
    
    public List getChargesForaBL(Integer fclblId) throws Exception {
        List<FclBlChargeBean> chargeList = new ArrayList<FclBlChargeBean>();
        String queryString = "Select fblc.bolId,fblc.costCode,fblc.amount,fblc.currencyCode,"
                + "fblc.accNo,fblc.accName,fblc.readyToPost,fblc.codeId from FclBlCostCodes fblc "
                + "where fblc.bolId='" + fclblId + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator itr = queryObject.iterator();

        // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        FclBlChargeBean fclbean = null;
        while (itr.hasNext()) {
            Object[] row = (Object[]) itr.next();
            fclbean = new FclBlChargeBean();
            Integer bolid = (Integer) row[0];
            String charges = (String) row[1];
            Double amount = (Double) row[2];
            amount = (amount == null) ? 0.0d : amount;
            String currencyCode = (String) row[3];
            String acctNo = (String) row[4];
            String accountName = (String) row[5];
            String readyToPost = (String) row[6];
            Integer costId = (Integer) row[7];
            fclbean.setBillofLaddingNo(bolid.toString());
            fclbean.setChargeAmt(String.valueOf(amount));
            fclbean.setChargeCode(charges);
            fclbean.setCurrencyCode(currencyCode);
            fclbean.setAcctNo(acctNo);
            fclbean.setAcctName(accountName);
            fclbean.setReadyToPost(readyToPost);
            fclbean.setCostId(costId);
            chargeList.add(fclbean);
            fclbean = null;
            
        }
        return chargeList;
        
    }
    
    public Integer findLastInsertedFclCostCodeId() throws Exception {
        String queryString = "SELECT code_id FROM fcl_bl_costcodes order by code_id desc limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("code_id", IntegerType.INSTANCE);
        Object costId = queryObject.uniqueResult();
        return null != costId ? (Integer) costId : 0;
    }
    
    public FclBlCostCodes findById(java.lang.Integer id) throws Exception {
        FclBlCostCodes instance = (FclBlCostCodes) getSession().get(
                "com.gp.cvst.logisoft.domain.FclBlCostCodes", id);
        return instance;
    }
    
    public List getAllFclCosts(String bol) throws Exception {
        String queryString = "from FclBlCostCodes where bolId='" + bol + "' and deleteFlag='no'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public List<FclBlCostCodes> getAllUnManifestedCosts(Integer bol) throws Exception {
        List<FclBlCostCodes> costList = new ArrayList<FclBlCostCodes>();
        String queryString = "from FclBlCostCodes where bolId= " + bol + "  and (transactionType is null or transactionType = '') and deleteFlag='no'";
        Query queryObject = getSession().createQuery(queryString);
        costList = queryObject.list();
        return costList;
    }
    
    public List<FclBlCostCodes> postedAccrualBeforeManifest(Integer bol) throws Exception {
        List<FclBlCostCodes> costList = new ArrayList<FclBlCostCodes>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select * from fcl_bl_costcodes where bolId=").append(bol).append(" and");
        queryBuilder.append(" (transaction_type != '' and transaction_type != 'AC') and ");
        queryBuilder.append(" delete_flag='no' and read_only_flag = 'on'");
        costList = getSession().createSQLQuery(queryBuilder.toString()).list();
        return costList;
    }
    
    public List getRecordWithThisCostCode(String bol, String costCode, String vendor, String codeId) throws Exception {
        List costList = new ArrayList();
        StringBuilder stringBuilder = null;
        if (CommonUtils.isNotEmpty(codeId)) {
            stringBuilder = new StringBuilder("from FclBlCostCodes where bolId='" + bol + "' and costCode='" + costCode + "' and accNo='" + vendor + "' and codeId!='" + codeId + "' and deleteFlag='no'");
            if (costCode.equals("INTMDL")) {
                stringBuilder.append(" AND costCode NOT IN('INTRAMP','INTFS')");
            } else if (costCode.equals("INTFS")) {
                stringBuilder.append(" AND costCode NOT IN('INTRAMP','INTMDL')");
            } else if (costCode.equals("INTRAMP")) {
                stringBuilder.append(" AND costCode NOT IN('INTFS','INTMDL')");
            }
        } else {
            stringBuilder = new StringBuilder("from FclBlCostCodes where bolId='" + bol + "' and accNo='" + vendor + "'  and deleteFlag='no'");
            if (costCode.equals("INTMDL") || costCode.equals("INTFS") || costCode.equals("INTRAMP")) {
                stringBuilder.append(" AND costCode IN('INTRAMP','INTFS','INTMDL')");
            } else {
                stringBuilder.append(" and costCode='").append(costCode).append("'");
            }
        }
        stringBuilder.append(" and transactionType !='AP'");
        Query queryObject = getSession().createQuery(stringBuilder.toString());
        costList = queryObject.list();
        return costList;
    }
    
    public List checkInvoiceNumberDuplicate(String vendor, String costCode, String invoiceNumber) throws Exception {
        List costList = new ArrayList();
        String queryString = "from FclBlCostCodes where accNo='" + vendor + "' and costCode='" + costCode + "' and invoiceNumber='" + invoiceNumber + "' and deleteFlag='no'";
        Query queryObject = getSession().createQuery(queryString);
        costList = queryObject.list();
        return costList;
    }
    
    public List<Object[]> getAllCharges(String FileNo) throws Exception {
        List<Object[]> chargesList = new ArrayList<Object[]>();
        String queryString = "SELECT costComments,codeId,costCode FROM FclBlCostCodes"
                + " WHERE bolid=(SELECT bol FROM FclBl WHERE fileNo='" + FileNo + "')";
        chargesList = getCurrentSession().createQuery(queryString).list();
        return chargesList;
    }
    
    public List deleteCharges(Integer bolId) throws Exception {
        String queryString = "FROM FclBlCostCodes	 WHERE bolId=" + bolId + " AND costCode IN("
                + "'" + FclBlConstants.ADVANCEFFCODE + "','" + FclBlConstants.ADVANCESURCHARGECODE + "','" + FclBlConstants.ADVANCESHIPPERCODE + "')";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
    
    public void updateDeleteFlag(Integer codeId) throws Exception {
        String queryString = "UPDATE fcl_bl_costcodes fclblcost SET fclblcost.delete_flag = 'yes',fclblcost.process_status = ''  WHERE fclblcost.code_id = '" + codeId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public void updateOFRDeleteFlag(Integer bolId) throws Exception {
        String queryString = "UPDATE fcl_bl_costcodes fclblcost SET fclblcost.delete_flag = 'yes',fclblcost.process_status = ''  WHERE fclblcost.BolId = '" + bolId + "' and "
                + "fclblcost.cost_code not IN('HAZFEE','INTMDL','BKRSUR','INTFS','INTRAMP','NASLAN') and fclblcost.read_only_flag = 'on' and fclblcost.booking_flag is null";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public void deleteUpdatedFlag(Integer codeId) throws Exception {
        String queryString = "Delete from fcl_bl_costcodes  WHERE delete_flag = 'yes'and code_id = '" + codeId + "' and (charge_index IS NULL or charge_index = '')";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public void updateOldAmount(Integer codeId, double oldCostCodeAmount) throws Exception {
        String queryString = "UPDATE fcl_bl_costcodes fclblcost SET fclblcost.old_amount = '" + oldCostCodeAmount + "',fclblcost.manifest_modify_flag = 'yes' WHERE fclblcost.code_id = '" + codeId + "' and fclblcost.ready_to_post ='M' and (fclblcost.old_amount IS NULL or fclblcost.old_amount = 0.0)";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public void resetOldAmount(Integer codeId, double oldCostCodeAmount) throws Exception {
        String queryString = "UPDATE fcl_bl_costcodes fclblcost SET fclblcost.old_amount = NULL   WHERE fclblcost.code_id = '" + codeId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public void updateManifestFlag(Integer codeId) throws Exception {
        String queryString = "UPDATE fcl_bl_costcodes fclblcost SET fclblcost.manifest_modify_flag = 'yes'  WHERE fclblcost.code_id = '" + codeId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
    }
    
    public boolean checkCarrierForCost(String codeId, String carrierName) throws Exception {
        carrierName=carrierName.replace("'", "''");
        String queryString = "select count(*) from fcl_bl_costcodes where bolid = '" + codeId + "' and account_name = '" + carrierName + "'";
        String size = getSession().createSQLQuery(queryString).uniqueResult().toString();
        return !size.equals("0");
    }
    
    public boolean getCostTransactionType(String codeId) throws Exception {
        String queryString = "select count(*)  from fcl_bl_costcodes where bolid = '" + codeId + "' and transaction_type = 'AP'";
        String size = getSession().createSQLQuery(queryString).uniqueResult().toString();
        return !size.equals("0");
    }
    
    public List checkAPStatus(String bolId) throws Exception {
        List resultList = new ArrayList();
        String queryString = "select cost_code_desc, date_paid  from fcl_bl_costcodes where bolid = '" + bolId + "' and transaction_type = 'AP'";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
        resultList = queryObject.list();
        return resultList;
    }
    
    public List getAPStatusCost(Integer bolId, String accountNo) throws Exception {
        List resultList = new ArrayList();
        String queryString = "FROM FclBlCostCodes	 WHERE bolId=" + bolId + " AND accNo = '" + accountNo + "' AND transactionType = 'AP' AND (datePaid IS NOT NULL OR datePaid != '')";
        Query queryObject = getSession().createQuery(queryString);
        resultList = queryObject.list();
        return resultList;
    }
    
    public Object sumOfCost(Integer bolId) throws Exception {
        String queryString = "select sum(amount) FROM FclBlCostCodes where bolId=" + bolId + " and deleteFlag = 'no'";
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }
    
    public Object sumOfSSLCost(Integer bolId, String accountName, String costCode) throws Exception {
        String queryString = "select sum(amount) FROM fcl_bl_costcodes where BolId=" + bolId + " and account_no = '" + accountName + "' and delete_flag = 'no' and cost_code != '" + costCode + "'";
        Object object = getSession().createSQLQuery(queryString).uniqueResult();
        return object;
    }
    
    public String IsFFCommissionRegionCode(String bolId) throws Exception {
        String queryString = "SELECT IF(CODE IN(01,02,03,12,13),'true','false') FROM genericcode_dup WHERE id IN (SELECT regioncode FROM ports WHERE portname=(SELECT SUBSTRING_INDEX(PORT, '/', 1) AS port_name_split FROM fcl_bl WHERE bol='" + bolId + "'))";
        Object object = getSession().createSQLQuery(queryString).uniqueResult();
        return null != object ? object.toString() : "";
    }
    
    public List<FclBlCostCodes> getOpenCosts(Integer bol, String vendorNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCostCodes.class);
        criteria.add(Restrictions.eq("bolId", bol));
        criteria.add(Restrictions.eq("accNo", vendorNumber));
        criteria.add(Restrictions.ne("costCode", "DEFER"));
        criteria.add(Restrictions.ne("costCode", "FFCOMM"));
        criteria.add(Restrictions.ne("costCode", "FAECOMM"));
        Disjunction type = Restrictions.disjunction();
        type.add(Restrictions.isNull("transactionType"));
        type.add(Restrictions.eq("transactionType", ""));
        type.add(Restrictions.eq("transactionType", "AC"));
        criteria.add(type);
        return criteria.list();
    }
    
    public List<FclBlCostCodes> getOFRCostCodes(Integer bolId) throws Exception {
        String[] otherCostCodes = {"HAZFEE", "BKRSUR", "INTMDL", "INTFS", "INTRAMP", "DEFER", "INSURE", "FAEXP", "FFCOMM", "FAECOMM", "NASLAN"};
        Criteria criteria = getCurrentSession().createCriteria(FclBlCostCodes.class);
        criteria.add(Restrictions.eq("bolId", bolId)).add(Restrictions.not(Restrictions.in("costCode", otherCostCodes))).add(Restrictions.ne("readOnlyFlag", "")).add(Restrictions.isNull("bookingFlag"));
        return criteria.list();
    }
    
    public boolean costIsAlreadyPaid(Integer bol) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM fcl_bl_costcodes WHERE bolid=").append(bol);
        queryBuilder.append(" AND NOT(transaction_type IS NULL OR transaction_type = ''OR transaction_type = 'AC')");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }
    
    public void updateFclCost(Serializable id, Map<Serializable, Serializable> fields) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  fcl_bl_costcodes ");
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
        queryBuilder.append("  where code_id = ").append(id);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public boolean checkAPStatus(Integer bolId, String costCode, String transactionType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM fcl_bl_costcodes WHERE bolid=").append(bolId);
        queryBuilder.append(" AND transaction_type= 'AP' AND cost_code='").append(costCode).append("'");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }
    
    public boolean hasTransactionType(String bolId)throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*) > 0, 'true', 'false') from fcl_bl_costcodes where bolid='");
        queryBuilder.append(bolId).append("' and (transaction_type <> '' and transaction_type is not null) and delete_flag='no'");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }
     public List findByIdI(Integer bolId) throws Exception {
        String queryString = "from FclBlCostCodes where bolId='" + bolId + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }
     
        public String getBillOfLaddingForIicon(List costCodesList,int bol) throws Exception {
        String prevendor ="";
        String curvendor ="";
        ArrayList<String> prevlist = new ArrayList<String>();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><BODY>");
        sb.append("<table width=\"100%\">");
        sb.append("<tr>");
        sb.append("<td align='left'>");
        sb.append("<FONT size='2' COLOR=#008000>");
        sb.append("<b>");
        sb.append("VendorName");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
      
        sb.append("<td>");
        sb.append("<FONT size='2' COLOR=#0000FF>");
        sb.append("<b>");
        sb.append("VendorAccount");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
  
        sb.append("<td>");
        sb.append("<FONT size='2' COLOR=#F01E1E>");
        sb.append("<b>");
        sb.append("Amount");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
     if (!costCodesList.isEmpty()) {
           for(int i =0;i<costCodesList.size();i++){  
               
           FclBlCostCodes fclBlCostCodes = (FclBlCostCodes)(costCodesList.get(i));
           curvendor=fclBlCostCodes.getAccName();
           
           if(!prevlist.contains(curvendor)){
           String totalam = sumOfVendorCost(fclBlCostCodes.getAccNo(),bol);
           if(!totalam.equalsIgnoreCase(""))
                sb.append("<tr>");
                sb.append("<td>");
                sb.append("<b>");
                sb.append(fclBlCostCodes.getAccName());
                sb.append("</b>");
                sb.append("</td>");
                sb.append("<td>");
                sb.append("<b>");
                if(fclBlCostCodes.getCostCode().equals("DEFER")){
                sb.append("<FONT size='4' COLOR=#F01E1E>");
                sb.append("*");
                sb.append("</FONT>");  
                sb.append(fclBlCostCodes.getAccNo());
                }else{
                sb.append(fclBlCostCodes.getAccNo());  
                }
                sb.append("</b>");
                sb.append("</td>");
                sb.append("<td>");
                sb.append("<b>");
                sb.append("$");
                sb.append(totalam);
                sb.append("</b>");
                sb.append("</td>");
                sb.append("</tr>");
               prevlist.add(curvendor);
           }
          
     }   
     }       
        sb.append("</table>");
        sb.append("<table>");
        sb.append("<tr>");
         for(int i =0;i<costCodesList.size();i++){  
               
           FclBlCostCodes fclBlCostCodes = (FclBlCostCodes)(costCodesList.get(i));
         if(fclBlCostCodes.getCostCode().equals("DEFER")){
             sb.append("<FONT size='2' COLOR=#F01E1E>");
             sb.append("*");
             sb.append("Deferral not included");
             break;
           }
        sb.append("</tr>");
        sb.append("</table>");
      
         }
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

   public String sumOfVendorCost(String accountno,int bol) throws Exception {
        String queryString = "select sum(amount) FROM fcl_bl_costcodes where  account_no = '" + accountno + "' and bolid='"+ bol+"' and cost_code!='DEFER' group by account_no='"+accountno+"'";
        Object object = getSession().createSQLQuery(queryString).uniqueResult();
        return null != object ? object.toString() : "";
    }
   public String getBolfromBillOfLadding(String BillofLaddingNo) throws Exception{
       String querystring = "select bol from fcl_bl where bolid = '" +BillofLaddingNo+"'";
       Object object = getSession().createSQLQuery(querystring).uniqueResult();
       return null != object ? object.toString() : "";
   }
   
     public List consolidateRatesForCosts(List fclRates, FclBl fclBl, boolean importFlag) throws Exception {
        List list = new ArrayList();
        List newList = new ArrayList();
        List finalList = new ArrayList();
        String consolidator = "";
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            list.add(fclBlCostCodes);
        }

        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (importFlag == false) {
                consolidator = LoadApplicationProperties.getProperty("OceanFreight");
            } else if (importFlag == true) {
                consolidator = LoadApplicationProperties.getProperty("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = LoadApplicationProperties.getProperty("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCostCodes.getCostCode().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCostCodes.getCostCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightcharge")) || fclBlCostCodes.getCostCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightImpcharge"))) {
                        FclBlCostCodes costcodes = new FclBlCostCodes();
                        PropertyUtils.copyProperties(costcodes, fclBlCostCodes);
                        newList.add(costcodes);
                        flag = true;
                    } else {
                        newList.add(fclBlCostCodes);
                        flag = true;
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if ((fclBlCostCodes.getCostCode().equals("DRAY")
                            || fclBlCostCodes.getCostCode().equals("INSURE")
                            || fclBlCostCodes.getCostCode().equals("PIERPA")
                            || fclBlCostCodes.getCostCode().equals("CHASFEE")
                            || fclBlCostCodes.getCostCode().equals("005")
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE)
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODETWO)
                            || fclBlCostCodes.getCostCode().equals("DEFER")
                            || fclBlCostCodes.getCostCode().startsWith("FAE")
                            || (fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new"))
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewMasterBL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost())) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewMasterBL());
                          
                        }
                        newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightcharge"))
                                    || tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                } else if (importFlag == true) {
                    if ((fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new")) 
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")) || (fclBlCostCodes.getCostCode().equals("INSURE"))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewMasterBL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost())) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewMasterBL());
                        
                        }
                      newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightcharge"))
                                    || tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }

        int k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCostCodes costCodes = (FclBlCostCodes) iter.next();
            if (costCodes.getCostCode() != null && (costCodes.getCostCode().equals("OCNFRT") || costCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, costCodes);
                k++;
            } else {
                linkedList.add(costCodes);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
}
    public FclBlCostCodes findByBookingId(Integer bookingId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCostCodes.class);
        criteria.add(Restrictions.eq("bookingId", bookingId));
        criteria.setMaxResults(1);
        return (FclBlCostCodes) criteria.uniqueResult();
    }
    // convert to Quotation
     public boolean costIsAlreadyPaidByBookingId(String bookingId) {
        int bookingid = Integer.parseInt(bookingId);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM fcl_bl_costcodes WHERE booking_id=").append(bookingid);
        queryBuilder.append(" AND NOT(transaction_type IS NULL OR transaction_type = ''OR transaction_type = 'AC')");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }
}


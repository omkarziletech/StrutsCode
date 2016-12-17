package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class VendorInvoice.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.VendorInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class FclBlCorrectionsDAO extends BaseHibernateDAO {

    public void save(FclBlCorrections persistentInstance) throws Exception {
        getSession().saveOrUpdate(persistentInstance);
        getSession().flush();
    }

    public void update(FclBlCorrections persistentInstance) throws Exception {
        getSession().update(persistentInstance);
        getSession().flush();
    }

    public void delete(FclBlCorrections persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public FclBlCorrections findById(Integer id) throws Exception {
        FclBlCorrections instance = (FclBlCorrections) getSession().get("com.gp.cvst.logisoft.domain.FclBlCorrections", id);
        getSession().flush();
        return instance;
    }

    public List getAllCorrectionsList(String crCode, String blno, String fileno, Date date, String shipper,
            String forwarder, String consignee, String origin, String desti, String pol,
            String pod, String noticeNo, String approvedBY, String createdBy, String filterBy, String fileType) throws Exception {
        int id = 0;
        // file number searcing from bl number becoz file number is concetnating with bl number...
        if (crCode != null && !crCode.equals("")) {
            id = Integer.parseInt(crCode);
        }
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (id != 0) {
            criteria.createCriteria("correctionCode").add(Restrictions.like("id", id));
        }
        if (blno != null && !blno.equals("")) {
            criteria.add(Restrictions.like("blNumber", blno + "%"));
        }
        if (fileno != null && !fileno.equals("")) {
            criteria.add(Restrictions.like("fileNo", fileno));
        }
        if (fileType != null && !fileType.equals("")) {
            criteria.add(Restrictions.eq("fileType", fileType));
        }
        if (noticeNo != null && !noticeNo.equals("")) {
            criteria.add(Restrictions.like("noticeNo", noticeNo + "%"));
        }
        if (date != null && !date.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String newDate = dateFormat.format(date);
            Date soStartDate = (Date) dateFormat.parse(newDate);
            //Date soEndDate = new Date(soStartDate.getYear(),soStartDate.getMonth(),soStartDate.getDate()+1);
            criteria.add(Restrictions.eq("date", soStartDate));
            //criteria.addOrder(Order.asc("date"));
            if (shipper != null && !shipper.equals("")) {
                criteria.add(Restrictions.like("shipper", shipper + "%"));
            }
            if (forwarder != null && !forwarder.equals("")) {
                criteria.add(Restrictions.like("forwarder", forwarder + "%"));
            }
            if (origin != null && !origin.equals("")) {
                criteria.add(Restrictions.like("origin", origin + "%"));
            }
            if (desti != null && !desti.equals("")) {
                criteria.add(Restrictions.like("destination", desti + "%"));
            }
            if (pol != null && !pol.equals("")) {
                criteria.add(Restrictions.like("pol", pol + "%"));
            }
            if (pod != null && !pod.equals("")) {
                criteria.add(Restrictions.like("pod", pod + "%"));
            }
            if (createdBy != null && !createdBy.equals("")) {
                criteria.add(Restrictions.like("userName", createdBy + "%"));
            }
            if (approvedBY != null && !approvedBY.equals("")) {
                criteria.add(Restrictions.like("approval", approvedBY + "%"));
            }
            if ("Approved".equalsIgnoreCase(filterBy)) {
                criteria.add(Restrictions.eq("status", "Approved"));
                criteria.add(Restrictions.eq("manifest", "M"));
            } else if ("UnApproved".equalsIgnoreCase(filterBy)) {
                criteria.add(Restrictions.isNull("manifest"));
                Criterion statusApprove = Restrictions.eq("status", "Approved");
                Criterion statusNull = Restrictions.isNull("status");
                LogicalExpression orExp = Restrictions.or(statusApprove, statusNull);
                criteria.add(orExp);
            } else {
                Criterion statusDisabled = Restrictions.ne("status", FclBlConstants.DISABLE);
                Criterion statusNull = Restrictions.isNull("status");
                LogicalExpression orExp = Restrictions.or(statusDisabled, statusNull);
                criteria.add(orExp);
            }
        }
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(100);
        return criteria.list();
    }

    public FclBl getFclBlCharges(String blNumber) throws Exception {
        String queryString = "from FclBl where bolId like '" + blNumber + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return (FclBl) queryObject.setMaxResults(1).uniqueResult();
    }

    public List getFclBlCorrections(String blNumber, String id) throws Exception {
        String queryString = null;
        queryString = "from FclBlCorrections where blNumber = '" + blNumber + "' and noticeNo='" + id + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List<Object[]> getSumOfOleAndNewAmount(String blNumber, String id) throws Exception {
        String queryString = null;
        queryString = "select sum(amount),sum(newAmount),accountNumber,accountName from FclBlCorrections where blNumber like '" + blNumber + "%' and noticeNo=?0 group by  accountNumber";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", id);
        return queryObject.list();
    }

    public Double getLatestAmountOfFclBlCorrections(String blNumber, String ChargeCode) throws Exception {
        String queryString = "select max(newAmount) FROM FclBlCorrections WHERE "
                + "blNumber like '" + blNumber + "%' and chargeCode='" + ChargeCode + "'and  status!='" + FclBlConstants.DISABLE + "' ";
        Query queryObject = getCurrentSession().createQuery(queryString);
        Object amountObject = queryObject.uniqueResult();
        return null != amountObject ? Double.parseDouble(amountObject.toString()) : 0.0d;
    }

    public String getLatestUnPostedNotice(String blNumber, String screen) throws Exception {
        String queryString = null;
        String returnString = null;
        if (screen != null && screen.equalsIgnoreCase("Correction")) {
            queryString = "SELECT noticeNo from FclBlCorrections where blNumber like "
                    + "'" + blNumber + "' and manifest is null and (status!='" + FclBlConstants.DISABLE + "' or status is null)";
        } else {
            queryString = "SELECT noticeNo from FclBlCorrections where blNumber like '" + blNumber + "' and"
                    + "(status!='" + FclBlConstants.DISABLE + "' or status is null) ";
        }
        Query queryObject = getCurrentSession().createQuery(queryString);
        if (queryObject.list() != null && !queryObject.list().isEmpty()) {
            returnString = "exist";
        }
        return returnString;
    }

    public List getFclBlCorrectionForTheBLNumbertoDisplay(String blNumber) throws Exception {
        List correctionsList = null;
        String queryString = null;
        queryString = "from FclBlCorrections where blNumber like '" + blNumber + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        correctionsList = queryObject.list();
        return correctionsList;
    }

    public List getCorrectionToDisplayOnPopup(String blNumber, boolean flag) throws Exception {
        List correctionsList = null;
        String queryString = null;
        queryString = (flag) ? "from FclBlCorrections where blNumber like '" + blNumber + "' and (STATUS!='Disable' OR STATUS IS null)"
                : "from FclBlCorrections where blNumber like '" + blNumber + "' and STATUS='Disable' GROUP By noticeNo ";
        Query queryObject = getCurrentSession().createQuery(queryString);
        correctionsList = queryObject.list();
        return correctionsList;
    }

    public FclBlCorrections getLatestPostedCorrection(String blNumber, int id) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.add(Restrictions.eq("blNumber", blNumber));
        criteria.add(Restrictions.eq("manifest", "M"));
        criteria.add(Restrictions.eq("status", "Approved"));
        Criteria c = criteria.createAlias("correctionType", "type");
        c.add(Restrictions.ne("id", id));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (FclBlCorrections) criteria.uniqueResult();
    }

    public FclBlCorrections getLatestPostedCorrectionByBl(String blNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.add(Restrictions.eq("blNumber", blNumber));
        criteria.add(Restrictions.eq("manifest", "M"));
        criteria.add(Restrictions.eq("status", "Approved"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (FclBlCorrections) criteria.uniqueResult();
    }

    public FclBlCorrections getLatestPostedCorrection(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        criteria.add(Restrictions.eq("manifest", "M"));
        criteria.add(Restrictions.eq("status", "Approved"));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(1);
        return (FclBlCorrections) criteria.uniqueResult();
    }
    //depends on the Bl Number getting notice number

    public Integer getNoticeNumber(String blNumber) throws Exception {
        Integer noticeNumber = null;
        String queryString = null;
        queryString = "select max(cast(notice_no AS UNSIGNED)) from FCLBLCORRECTIONS where bl_number=?0";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("0", blNumber);
        if (queryObject.uniqueResult() != null) {
            BigInteger bigInteger = (BigInteger) queryObject.uniqueResult();
            noticeNumber = bigInteger.intValue();
        }
        return noticeNumber;
    }

    public Integer getNoticeNumberForPostedCorrection(String blNumber) throws Exception {
        Integer noticeNumber = null;
        String queryString = null;
        queryString = "select max(cast(notice_no AS UNSIGNED)) from FCLBLCORRECTIONS where bl_number=?0 and manifest is not null and status!='Disable'";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("0", blNumber);
        if (queryObject.uniqueResult() != null) {
            BigInteger bigInteger = (BigInteger) queryObject.uniqueResult();
            noticeNumber = bigInteger.intValue();
        }
        return noticeNumber;
    }

    public Integer getNoticeNumberForLatestAtypeCorrection(String blNumber, Integer id) throws Exception {
        Integer noticeNumber = null;
        String queryString = null;
        queryString = "select max(cast(notice_no AS UNSIGNED)) from FCLBLCORRECTIONS where bl_number=?0 and manifest = 'M' and status ='Approved' and correction_type = " + id;
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        queryObject.setParameter("0", blNumber);
        if (queryObject.uniqueResult() != null) {
            BigInteger bigInteger = (BigInteger) queryObject.uniqueResult();
            noticeNumber = bigInteger.intValue();
        }
        return noticeNumber;
    }

    public Integer getNoticeNumberForLatestCorrection(String blNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  max(cast(notice_no as unsigned)) as notice_number ");
        queryBuilder.append("from");
        queryBuilder.append("  fclblcorrections ");
        queryBuilder.append("where bl_number = '").append(blNumber).append("'");
        queryBuilder.append("  and manifest = 'M'");
        queryBuilder.append("  and status = 'Approved'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("notice_number", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }
    // test...................

    public List getAllUnApprovedCorrections() throws Exception {
        List correctionsList = null;
        String queryString = null;
        queryString = "from FclBlCorrections where approval IS NOT NULL and manifest IS NULL";
        Query queryObject = getCurrentSession().createQuery(queryString);
        correctionsList = queryObject.list();
        return correctionsList;
    }

    public void setDisApprove(String notice, String blNumber) throws Exception {
        String query = "update fclblcorrections f set f.status = null,f.approval=null where f.bl_number = '" + blNumber + "' and f.notice_no='" + notice + "' ";
        Query querObject = getCurrentSession().createSQLQuery(query);
        querObject.executeUpdate();
    }

    public void deleteCorrectedBL(String blNo) throws Exception {
        String query = "delete from fcl_bl  where BolId ='" + blNo + "'";
        Query querObject = getCurrentSession().createSQLQuery(query);
        querObject.executeUpdate();
    }

    public List getRevrseAccruals(String blNo) throws Exception {
        List correctionsList = null;
        String queryString = null;
        queryString = "from FclBlCorrections  where blNumber ='" + blNo + "'";// and revrseAccruals is not null
        Query queryObject = getCurrentSession().createQuery(queryString);
        correctionsList = queryObject.list();
        return correctionsList;
    }

    public List getBillToParty(String billtoParty, String noticeNumber, String blNumber) throws Exception {
        List correctionsList = null;
        String queryString = null;
        queryString = "select DISTINCT(" + billtoParty + ") from  FclBlCorrections  where blNumber ='" + blNumber + "' and noticeNo='" + noticeNumber + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        correctionsList = queryObject.list();
        return correctionsList;
    }

    public Object sumOfADVFFandADVSHP(String bolId, String noticeNo) throws Exception {
        String queryString = "select sum(differeceAmount) FROM FclBlCorrections	 WHERE blNumber='" + bolId + "' AND noticeNo='" + noticeNo + "' AND  chargeCode IN("
                + "'" + FclBlConstants.ADVANCEFFCODE + "','" + FclBlConstants.ADVANCESHIPPERCODE + "')";
        Object object = getSession().createQuery(queryString).uniqueResult();
        return object;
    }

    public List getCorrection(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getChargeCode())) {
            criteria.add(Restrictions.eq("chargeCode", fclBlCorrectionsForm.getChargeCode()));
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getBlNumber())) {
            criteria.add(Restrictions.eq("blNumber", fclBlCorrectionsForm.getBlNumber()));
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getNoticeNo())) {
            criteria.add(Restrictions.eq("noticeNo", fclBlCorrectionsForm.getNoticeNo()));
        }
        return criteria.list();
    }

    public List<FclBlCorrections> getCorrections(String blNumber, String noticeNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.add(Restrictions.eq("blNumber", blNumber));
        if (CommonUtils.isNotEmpty(noticeNo)) {
            criteria.add(Restrictions.eq("noticeNo", noticeNo));
        }
        return criteria.list();
    }

    public List<Object> getCreditDebitNoteType(String cnType, String newBlNumber, String noticeNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.in(cnType, "A", "Y")) {
            queryBuilder.append("select");
            queryBuilder.append("  cn.account_number as customerNumber,");
            queryBuilder.append("  cn.account_name as customerName,");
            queryBuilder.append("  if(ar.send_debit_credit_notes = 'Y', true, false) as creditDebit,");
            queryBuilder.append("  if(sum(cn.amount) > sum(cn.new_amount),");
            queryBuilder.append("     '").append(FclBlConstants.CREDTINOTE).append("',");
            queryBuilder.append("     '").append(FclBlConstants.DEBITNOTE).append("'");
            queryBuilder.append("  ) as noteType ");
            queryBuilder.append("from");
            queryBuilder.append("  fclblcorrections cn ");
            queryBuilder.append("  left join cust_accounting ar");
            queryBuilder.append("    on (cn.account_number = ar.acct_no)");
            queryBuilder.append("where");
            queryBuilder.append("  cn.new_bl_number = '").append(newBlNumber).append("'");
            queryBuilder.append("  and cn.notice_no = '").append(noticeNo).append("' ");
            queryBuilder.append("group by cn.account_number");
        } else {
            queryBuilder.append("select");
            queryBuilder.append("  cn.cust_no as customerNumber,");
            queryBuilder.append("  cn.cust_name as customerName,");
            queryBuilder.append("  if(ar.send_debit_credit_notes = 'Y', true, false) as creditDebit,");
            queryBuilder.append("  if(cn.correction_notice = '").append(noticeNo).append(FclBlConstants.CNS).append("',");
            queryBuilder.append("     '").append(FclBlConstants.CREDTINOTE).append("',");
            queryBuilder.append("     '").append(FclBlConstants.DEBITNOTE).append("'");
            queryBuilder.append("  ) as noteType ");
            queryBuilder.append("from");
            queryBuilder.append("  transaction_ledger cn ");
            queryBuilder.append("  left join cust_accounting ar");
            queryBuilder.append("    on (cn.cust_no = ar.acct_no)");
            queryBuilder.append("where");
            queryBuilder.append("  cn.bill_ladding_no like '").append(newBlNumber.substring(0, newBlNumber.indexOf("=="))).append("%'");
            queryBuilder.append("  and cn.notice_number = '").append(noticeNo).append("' ");
            queryBuilder.append("group by cn.cust_no");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("customerName", StringType.INSTANCE);
        query.addScalar("creditDebit", BooleanType.INSTANCE);
        query.addScalar("noteType", StringType.INSTANCE);
        return query.list();
    }

    public void updateCorrection(Serializable id, Map<Serializable, Serializable> fields) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  fclblcorrections ");
        queryBuilder.append("set ");
        int rowCount = 0;
        for (Serializable key : fields.keySet()) {
            Serializable value = fields.get(key);
            queryBuilder.append(key);
            if (null == value) {
                queryBuilder.append(" = null");
            } else {
                queryBuilder.append(" = '").append(value).append("'");
            }
            if (rowCount < fields.size() - 1) {
                queryBuilder.append(",");
            }
            rowCount++;
        }
        queryBuilder.append("  where id = ").append(id);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String correctionProfits(String fileNo, String noticeNo) throws Exception {
        String originalBolid = "";
        String newBolid = "";
        double chargeAmt = 0.00;
        double costAmt = 0.00;
        double diffAmt = 0.00;
        double invoiceAmt = 0.00;
        String orginalBolidQuery = "select bol as original_bol_id from fcl_bl where file_no = '" + fileNo + "' limit 1";
        originalBolid = getCurrentSession().createSQLQuery(orginalBolidQuery).uniqueResult().toString();

        String costQuery = "select sum(amount) as cost_amount from fcl_bl_costcodes where bolid = '" + originalBolid + "' and delete_flag='no'";
        Object cost = getCurrentSession().createSQLQuery(costQuery).uniqueResult();
        costAmt = null != cost ? Double.parseDouble(cost.toString()) : 0.00;

        if (CommonUtils.isEmpty(noticeNo)) {
            String newBolidQuery = "select new_bl_number from fclblcorrections where file_no = '" + fileNo + "' and status <> 'Disable' order by notice_no desc limit 1";
            Object newBol = getCurrentSession().createSQLQuery(newBolidQuery).uniqueResult();
            newBolid = null != newBol ? newBol.toString() : "";
        } else if (CommonUtils.isNotEmpty(noticeNo)) {
            String newBolidQuery = "select new_bl_number from fclblcorrections where file_no = '" + fileNo + "' and status <> 'Disable' and notice_no < " + noticeNo + " order by notice_no desc limit 1";
            Object newBol = getCurrentSession().createSQLQuery(newBolidQuery).uniqueResult();
            newBolid = null != newBol ? newBol.toString() : "";
        }

        if (CommonUtils.isNotEmpty(newBolid)) {
            String chargeAmtQuery = "select sum(amount) as charge_amount from fcl_bl_charges where bolid = (select bol from fcl_bl where bolid = '" + newBolid + "')";
            Object charge = getCurrentSession().createSQLQuery(chargeAmtQuery).uniqueResult();
            chargeAmt = null != charge ? Double.parseDouble(charge.toString()) : 0.00;
        } else if (null == newBolid || newBolid.isEmpty()) {
            String chargeAmtQuery = "select sum(amount) as charge_amount from fcl_bl_charges where bolid = '" + originalBolid + "'";
            Object charge = getCurrentSession().createSQLQuery(chargeAmtQuery).uniqueResult();
            chargeAmt = null != charge ? Double.parseDouble(charge.toString()) : 0.00;
        }

        if (CommonUtils.isNotEmpty(noticeNo)) {
            String diffAmtQuery = "select sum(difference_amount) as correction_amount from fclblcorrections where file_no = '" + fileNo + "' and notice_no = '" + noticeNo + "'";
            Object diff = getCurrentSession().createSQLQuery(diffAmtQuery).uniqueResult();
            diffAmt = null != diff ? Double.parseDouble(diff.toString()) : 0.00;
        }
        invoiceAmt = getArinvoiceAmount(fileNo);

        Double currentProfit = (chargeAmt + invoiceAmt) - costAmt;
        Double profitAfterCn = currentProfit + diffAmt;
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(currentProfit) + "," + format.format(profitAfterCn);
    }

    public void updateCorrectionProfits(double currentProfit, double profitAfterCn, String id) {
        String q = "update fclBlCorrections set current_profit=" + currentProfit + ",profit_after_cn=" + profitAfterCn + " where id='" + id + "'";
        getCurrentSession().createSQLQuery(q).executeUpdate();
    }

    public List findByFileNo(String fileNo) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(FclBlCorrections.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        return criteria.list();
    }

    public double getArinvoiceAmount(String fileNo) throws Exception {
        String arInvoiceAmtQuery = "select sum(invoice_amount) from ar_red_invoice where file_no='" + fileNo + "' and posted_date is not null and posted_date <>''";
        Object invoiceAmount = getCurrentSession().createSQLQuery(arInvoiceAmtQuery).uniqueResult();
        return null != invoiceAmount ? Double.parseDouble(invoiceAmount.toString()) : 0.00;
    }

    public String getPreviousNoticeNo(String fileNo, String noticeNo) {
        String queryString = "select max(notice_no) as noticeNo from fclblcorrections where file_no = :fileNo and notice_no < :noticeNo and status <> 'Disable'";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("fileNo", fileNo);
        query.setString("noticeNo", noticeNo);
        query.addScalar("noticeNo", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }
}

package com.gp.cvst.logisoft.hibernate.dao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.BatchesBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.reports.dto.BatchReportDTO;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Batch.
 * @see com.gp.cvst.logisoft.hibernate.dao.Batch
 * @author MyEclipse - Hibernate Tools
 */
public class BatchDAO extends BaseHibernateDAO {

    //property constants
    public static final String BATCH_DESC = "batchDesc";
    public static final String SOURCE_LEDGER = "sourceLedger";
    public static final String TYPE = "type";
    public static final String TOTAL_CREDIT = "totalCredit";
    public static final String TOTAL_DEBIT = "totalDebit";
    public static final String READY_TO_POST = "readyToPost";
    public static final String STATUS = "status";

    public void save(Batch transientInstance)throws Exception {
            getSession().saveOrUpdate(transientInstance);
            getSession().flush();
    }

    public Batch saveAndReturn(Batch transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        return transientInstance;
    }

    public void delete(Batch persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public Batch findById(java.lang.String batchId)throws Exception {
            Batch instance = (Batch) getSession().get("com.gp.cvst.logisoft.domain.Batch", batchId);
            return instance;
    }

    public List findByExample(Batch instance)throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.Batch").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from Batch as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findBatchList()throws Exception {
        List list = new ArrayList();
            String queryString = "from Batch";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list();
        return list;
    }

    public List findByBatchDesc(Object batchDesc)throws Exception {
        return findByProperty(BATCH_DESC, batchDesc);
    }

    public List findBySourceLedger(Object sourceLedger)throws Exception {
        return findByProperty(SOURCE_LEDGER, sourceLedger);
    }

    public List findByType(Object type)throws Exception {
        return findByProperty(TYPE, type);
    }

    public List findByTotalCredit(Object totalCredit)throws Exception {
        return findByProperty(TOTAL_CREDIT, totalCredit);
    }

    public List findByTotalDebit(Object totalDebit)throws Exception {
        return findByProperty(TOTAL_DEBIT, totalDebit);
    }

    public List findByReadyToPost(Object readyToPost)throws Exception {
        return findByProperty(READY_TO_POST, readyToPost);
    }

    public List findByStatus(Object status)throws Exception {
        return findByProperty(STATUS, status);
    }

    public Batch merge(Batch detachedInstance)throws Exception {
            Batch result = (Batch) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Batch instance)throws Exception {
            getSession().saveOrUpdate(instance);
    }

    public void attachClean(Batch instance)throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getAllBatchNumbers() throws Exception {
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select batch.batchId from Batch batch").list().iterator();
        return results;
    }

    public List getGLBatches(String batchId, String sourceLedger, String status)throws Exception {
        List<BatchesBean> glBacthes = new ArrayList<BatchesBean>();
            StringBuilder queryBuilder = new StringBuilder(" from Batch where");
            if (CommonUtils.isEqual(status, "no")) {
                queryBuilder.append(" status='Open' or status='ready to post'");
            } else {
                queryBuilder.append(" status is not null");
            }
            if (CommonUtils.isNotEmpty(batchId)) {
                queryBuilder.append(" and batchId='" + batchId + "'");
            }
            queryBuilder.append(" order by batchId desc");
            List<Batch> batches = getSession().createQuery(queryBuilder.toString()).list();
            for (Batch batch : batches) {
                BatchesBean bbean = new BatchesBean();
                bbean.setBatchno(batch.getBatchId().toString());
                bbean.setDesc(batch.getBatchDesc());
                if (batch.getSourceLedger() != null) {
                    bbean.setSourceLedger(batch.getSourceLedger().getCode());
                }
                bbean.setType(batch.getType());
                if (batch.getTotalCredit() != null) {
                    bbean.setTotalCredit(null!=batch.getTotalCredit()?NumberUtils.formatNumber(batch.getTotalCredit(),"##,###,##0.00"):"0.00");
                }
                if (batch.getTotalDebit() != null) {
                    bbean.setTotalDebit(null!=batch.getTotalDebit()?NumberUtils.formatNumber(batch.getTotalDebit(),"##,###,##0.00"):"0.00");
                }
                bbean.setPost(batch.getReadyToPost());
                if(CommonUtils.isEqual(batch.getReadyToPost(), "yes")){
                    bbean.setReadyToPost("on");
                }else{
                    bbean.setReadyToPost("off");
                }
                bbean.setStatus(batch.getStatus());
                glBacthes.add(bbean);
            }
        return glBacthes;
    }

    public List getAllBatchDetails(String status)throws Exception {
        BatchesBean bbean = null;
        List<BatchesBean> batchesList = new ArrayList<BatchesBean>();
        String queryString = "";
        if (status == null || status.equals("no")) {
            queryString = "from Batch batch where status='Open' or status='ready to post' order by batch.batchId desc ";
        } else {
            queryString = "from Batch batch order by batch.batchId desc ";
        }

        List queryObject = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < queryObject.size(); i++) {
            Batch b1 = (Batch) queryObject.get(i);
            bbean = new BatchesBean();
            bbean.setBatchno(b1.getBatchId().toString());
            bbean.setDesc(b1.getBatchDesc());
            if (b1.getSourceLedger() != null) {
                bbean.setSourceLedger(b1.getSourceLedger().getCode());
            }
            bbean.setType(b1.getType());
            if (b1.getTotalCredit() != null) {
                bbean.setTotalCredit(b1.getTotalCredit().toString());
            }
            if (b1.getTotalDebit() != null) {
                bbean.setTotalDebit(b1.getTotalDebit().toString());
            }
            bbean.setPost(b1.getReadyToPost());
            bbean.setStatus(b1.getStatus());
            batchesList.add(bbean);
            bbean = null;
        }
        return batchesList;
    }

    public List getBatchDetails(String batchId, String sourceLedger, String status)throws Exception {
        BatchesBean bbean = null;
        List<BatchesBean> batchesList = new ArrayList<BatchesBean>();
        String queryString = "";
        if (status == null || status.equals("no")) {
            queryString = "from Batch batch where status='Open' and batchId='" + batchId + "' order by batch.batchId desc ";
        } else {
            queryString = "from Batch batch where batchId='" + batchId + "' order by batch.batchId desc ";
        }

        List queryObject = getCurrentSession().createQuery(queryString).list();
        for (int i = 0; i < queryObject.size(); i++) {
            Batch b1 = (Batch) queryObject.get(i);
            bbean = new BatchesBean();
            bbean.setBatchno(b1.getBatchId().toString());
            bbean.setDesc(b1.getBatchDesc());
            if (b1.getSourceLedger() != null) {
                bbean.setSourceLedger(b1.getSourceLedger().getCode());
            }
            bbean.setType(b1.getType());
            bbean.setTotalCredit(b1.getTotalCredit().toString());
            bbean.setTotalDebit(b1.getTotalDebit().toString());
            bbean.setPost(b1.getReadyToPost());
            bbean.setStatus(b1.getStatus());
            batchesList.add(bbean);
            bbean = null;
        }
        return batchesList;
    }

    public String getSubLedgerStatus(String sourceLedger)throws Exception {
        String subLedgerStatus = null;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String fiscalPeriod = sdf.format(date);
        StringBuffer queryString = new StringBuffer("select b.status from batch b inner join journal_entry je on "
                + " je.Batch_Id = b.Batch_Id and je.Source_Code = b.source_Ledger inner join fiscal_period fp on fp.Period_Dis=je.Period"
                + " and fp.Period_Dis ='" + fiscalPeriod + "' where b.source_Ledger='" + sourceLedger + "'");
        List<String> queryResult = getCurrentSession().createSQLQuery(queryString.toString()).list();
        if (null != queryResult && !queryResult.isEmpty()) {
            for (String status : queryResult) {
                if (null != status) {
                    if (status.trim().equalsIgnoreCase(CommonConstants.STATUS_OPEN)) {
                        subLedgerStatus = "Not Posted";
                        break;
                    } else {
                        subLedgerStatus = "Posted";
                    }
                } else {
                    subLedgerStatus = "Not Posted";
                }
            }
        } else {
            subLedgerStatus = "Not Posted";
        }
        return subLedgerStatus;
    }

    public String getMaxBatchNumber()throws Exception {
            Object result = getCurrentSession().createQuery("select max(batch.batchId) from Batch batch").uniqueResult();
            if(null!=result){
                return result.toString();
            }
        return null;
    }

    public void update(Batch persistanceInstance)throws Exception {
            getSession().update(persistanceInstance);
            getSession().flush();
    }

    //search batch popup

    public List findBatch(String batchid, String desc)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Batch.class);

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            String status = "open";
            if (batchid != null && !batchid.equals(0)) {
                criteria.add(Restrictions.like("batchId", batchid));
            }
            if (desc != null) {
                criteria.add(Restrictions.like("batchDesc", desc + "%"));
            }
            criteria.add(Restrictions.like("status", status));
            return criteria.list();
    }

    public List findBatchsearch(String batchid, String desc)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Batch.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (batchid != null && !batchid.equals(0)) {
                criteria.add(Restrictions.like("batchId", batchid + "%"));
            }
            if (desc != null) {
                criteria.add(Restrictions.like("batchDesc", desc + "%"));
            }
            criteria.add(Restrictions.like("status", "open"));
            return criteria.list();
    }

    public List findBatchsearchforDojo(String batchid, String desc)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Batch.class);
            if (batchid != null && !batchid.equals(0)) {
                criteria.add(Restrictions.like("batchId", batchid + "%"));
            }
            return criteria.list();
    }

    public String getNumberOFBatches()throws Exception {
            Object count = (Object)getCurrentSession().createSQLQuery("select count(*) from batch").uniqueResult();
            if(null!=count){
                return count.toString();
            }
        return null;
    }

    public List findByBatch(int batchid)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Batch.class);

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (batchid != 0) {
                Criterion batch = Restrictions.like("batchId", batchid);
                criteria.add(batch);
            }
            return criteria.list();
    }

    public void updateBatchDebitCredit(String crdr, String bid)throws Exception {
        int updatedrecords = 0;
            String queryString = "update Batch b set b.totalCredit='" + crdr + "',b.totalDebit='" + crdr + "' where b.batchId='" + bid + "'";
            updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public void updateBatchDebitAndCredit(String credit, String debit, String bid)throws Exception {
        int updatedrecords = 0;
            String queryString = "update Batch b set b.totalCredit='" + credit + "',b.totalDebit='" + debit + "' where b.batchId='" + bid + "'";
            updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();
    }

    public List getbatchDetails(String batchId)throws Exception {
        List<BatchesBean> batchlist = new ArrayList<BatchesBean>();
        String queryString = "select batch.batchId,batch.batchDesc,batch.totalCredit,batch.totalDebit from Batch batch where batch.batchId='" + batchId + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        Iterator itr = queryObject.iterator();
        BatchesBean batchdto = null;
        while (itr.hasNext()) {
            batchdto = new BatchesBean();
            Object[] row = (Object[]) itr.next();
            String batchno = (String) row[0];
            String batchdesc = (String) row[1];
            Double totaldebit = (Double) row[2];
            Double totalcredit = (Double) row[3];
            batchdto.setBatchno(batchno);
            batchdto.setDesc(batchdesc);
            if (totaldebit != null) {
                batchdto.setTotalDebit(number.format(totaldebit));
            }
            if (totaldebit != null) {
                batchdto.setTotalCredit(number.format(totalcredit));
            }
            batchlist.add(batchdto);
            batchdto = null;
        }


        return batchlist;
    }

    public List getJEandLineDetailsofBatch(String batchno)throws Exception {
        List<BatchReportDTO> batchlist = new ArrayList<BatchReportDTO>();
        String queryStringJE = "select je.journalEntryId,je.sourceCode.code from JournalEntry je where je.batchId='" + batchno + "'";
        List queryObject = getCurrentSession().createQuery(queryStringJE).list();
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat number = new DecimalFormat("0.00");
        Iterator itr1 = queryObject.iterator();
        while (itr1.hasNext()) {
            Object[] row1 = (Object[]) itr1.next();
            String je = (String) row1[0];
            String jesourcecode = (String) row1[1];

            String queryString = "select je.journalEntryId,je.journalEntryDesc,je.jeDate,je.debit,je.credit,"
                    + "li.lineItemId,li.account,li.accountDesc,li.debit,li.credit,je.period from JournalEntry je,LineItem li "
                    + "where li.journalEntryId='" + je + "' and je.journalEntryId='" + je + "' ORDER BY  je.journalEntryId";
            List queryObjectJeLI = getCurrentSession().createQuery(queryString).list();
            Iterator itr = queryObjectJeLI.iterator();

            BatchReportDTO bdto = null;
                int j = 0;
                while (itr.hasNext()) {
                    bdto = new BatchReportDTO();
                    Object[] row = (Object[]) itr.next();
                    String jeid = (String) row[0];
                    String jedesc = (String) row[1];
                    Date jdate = (Date) row[2];
                    Double jdebit = (Double) row[3];
                    Double jcredit = (Double) row[4];
                    String lid = (String) row[5];
                    String acct = (String) row[6];
                    String acctdesc = (String) row[7];
                    Double ldebit = (Double) row[8];
                    Double lcredit = (Double) row[9];
                    String period = (String) row[10];
                    String jedate = sdf.format(jdate);
                    if (j > 0) {
                        jeid = "";
                        jedesc = "";
                        jedate = "";
                        jesourcecode = "";
                    }
                    bdto.setJeid(jeid);
                    bdto.setJedesc(jedesc);
                    bdto.setJedate(jedate);
                    String jedebit = "";
                    String jecredit = "";
                    String debit = "";
                    String credit = "";
                    if (jdebit == null) {
                        jdebit = 0.0;

                    }
                    if (jcredit == null) {
                        jcredit = 0.0;

                    }
                    if (ldebit == null) {
                        ldebit = 0.0;

                    }
                    if (lcredit == null) {
                        lcredit = 0.0;

                    }
                    jedebit = number.format(jdebit);
                    jecredit = number.format(jcredit);
                    debit = number.format(ldebit);
                    credit = number.format(lcredit);
                    if (j > 0) {
                        jedebit = "";
                        jecredit = "";
                    }
                    bdto.setJedebit(jedebit);
                    bdto.setJecredit(jecredit);
                    bdto.setLid(lid);
                    bdto.setAcct(acct);
                    bdto.setAcctdesc(acctdesc);
                    bdto.setDebit(debit);
                    bdto.setCredit(credit);
                    bdto.setJesourcecode(jesourcecode);
                    bdto.setJeperiod(period);
                    batchlist.add(bdto);
                    bdto = null;
                    j++;
                }
            i++;
        }
        if (queryObject.isEmpty()) {
            BatchReportDTO bdto = new BatchReportDTO();
            bdto.setJeperiod("");
            batchlist.add(bdto);
            bdto = null;
        }
        return batchlist;
    }
}

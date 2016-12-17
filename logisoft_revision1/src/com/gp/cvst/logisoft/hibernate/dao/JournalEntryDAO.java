package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.JournalEntry;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class JournalEntry.
 * @see com.gp.cvst.logisoft.hibernate.dao.JournalEntry
 * @author MyEclipse - Hibernate Tools
 */
public class JournalEntryDAO extends BaseHibernateDAO {

    //property constants
    public static final String JOURNAL_ENTRY_DESC = "journalEntryDesc";
    public static final String BATCH_ID = "batchId";
    public static final String JE_DATE = "jeDate";
    public static final String PERIOD = "period";
    public static final String SOURCE_CODE = "sourceCode";
    public static final String SOURCE_CODE_DESC = "sourceCodeDesc";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    public static final String MEMO = "memo";

    public void save(JournalEntry transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(JournalEntry transientInstance) throws Exception {
            getSession().update(transientInstance);
            getSession().flush();
    }

    public void delete(JournalEntry persistentInstance) throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public JournalEntry findById(java.lang.String id) throws Exception {
            JournalEntry instance = (JournalEntry) getSession().get("com.gp.cvst.logisoft.domain.JournalEntry", id);
            return instance;
    }

    public List findByBatchId(java.lang.String batchId) throws Exception {
            String queryString = "from JournalEntry where batchId=?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", batchId);
            return queryObject.list();
    }

    public List findByExample(JournalEntry instance) throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.JournalEntry").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
            String queryString = "from JournalEntry as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByJournalEntryDesc(Object journalEntryDesc)  throws Exception {
        return findByProperty(JOURNAL_ENTRY_DESC, journalEntryDesc);
    }

    public List findByBatchId(Object batchId)  throws Exception {
        return findByProperty(BATCH_ID, batchId);
    }

    public List findByJeDate(Object jeDate)  throws Exception {
        return findByProperty(JE_DATE, jeDate);
    }

    public List findByPeriod(Object period) throws Exception {
        return findByProperty(PERIOD, period);
    }

    public List findBySourceCode(Object sourceCode) throws Exception {
        return findByProperty(SOURCE_CODE, sourceCode);
    }

    public List findBySourceCodeDesc(Object sourceCodeDesc) throws Exception {
        return findByProperty(SOURCE_CODE_DESC, sourceCodeDesc);
    }

    public List findByDebit(Object debit) throws Exception {
        return findByProperty(DEBIT, debit);
    }

    public List findByCredit(Object credit) throws Exception {
        return findByProperty(CREDIT, credit);
    }

    public List findByMemo(Object memo) throws Exception {
        return findByProperty(MEMO, memo);
    }

    public JournalEntry merge(JournalEntry detachedInstance) throws Exception {
            JournalEntry result = (JournalEntry) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(JournalEntry instance)  throws Exception {
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(JournalEntry instance) throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findJesByBatch(int batchid)  throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(JournalEntry.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);          
            if (batchid != 0) {                
                Criterion je = Restrictions.like("batchId", batchid);
                criteria.add(je);
            }
            return criteria.list();
    }

    public String getNumberOFJournalEntry(String batchId) throws Exception {
            Object count = (Object)getCurrentSession().createSQLQuery("select count(*) from journal_entry where batch_id='"+batchId+"'").uniqueResult();
            if(null!=count){
                return count.toString();
            }
        return null;
    }

    public void updateJECredit(String jeID, Double cr) throws Exception {
        String credit = Double.toString(cr);
        int updatedrecords = 0;
            String queryString = "update JournalEntry je set je.credit='" + credit + "',je.debit='" + credit + "' where je.journalEntryId='" + jeID + "'";
            updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();            
            BatchDAO bdao = new BatchDAO();
            StringTokenizer st = new StringTokenizer(jeID, "-");
            String batchid = "";
            if (st.hasMoreTokens()) {
                batchid = st.nextToken();
            }
            bdao.updateBatchDebitCredit(credit, batchid);
    }
    public void updateJournalEntry(String jeId, Double jeCredit,Double jeDebit)  throws Exception {
        String credit = jeCredit.toString();
        String debit = jeDebit.toString();
        int updatedrecords = 0;
            String queryString = "update JournalEntry je set je.credit='" + credit + "',je.debit='" + jeDebit + "' where je.journalEntryId='" + jeId + "'";
            updatedrecords = getCurrentSession().createQuery(queryString).executeUpdate();            
            BatchDAO bdao = new BatchDAO();
            StringTokenizer st = new StringTokenizer(jeId, "-");
            String batchid = "";
            if (st.hasMoreTokens()) {
                batchid = st.nextToken();
            }
            bdao.updateBatchDebitAndCredit(credit, debit, batchid);
    }
}

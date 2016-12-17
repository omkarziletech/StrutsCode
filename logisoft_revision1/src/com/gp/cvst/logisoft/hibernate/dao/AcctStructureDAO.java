package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.AccountStructureBean;
import com.gp.cvst.logisoft.domain.AcctStructure;
//import com.gp.cvst.logisoft.hibernate.dao.AcctStructureDAO;
import com.gp.cvst.logisoft.reports.dto.AccountStructureDTO;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class AcctStructure.
 * @see com.gp.cvst.logisoft.hibernate.dao.AcctStructure
 * @author MyEclipse - Hibernate Tools
 */
public class AcctStructureDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(AcctStructureDAO.class);

    public void save(AcctStructure transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(AcctStructure persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public AcctStructure findById(java.lang.Integer id) throws Exception {
	AcctStructure instance = (AcctStructure) getSession().get("com.gp.cvst.logisoft.domain.AcctStructure", id);
	return instance;
    }

    public String findByAcctStructureName(java.lang.String acctStructureName) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select cast(id as char character set latin1)");
	queryBuilder.append(" from acct_structure");
	queryBuilder.append(" where acct_structure_name = '").append(acctStructureName).append("'");
	queryBuilder.append(" limit 1");
	return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public List findByExample(AcctStructure instance) throws Exception {
	List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.AcctStructure").add(Example.create(instance)).list();
	return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from AcctStructure as model where model." + propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public AcctStructure merge(AcctStructure detachedInstance) throws Exception {
	AcctStructure result = (AcctStructure) getSession().merge(detachedInstance);
	return result;
    }

    public void attachDirty(AcctStructure instance) throws Exception {
	getSession().saveOrUpdate(instance);
    }

    public void attachClean(AcctStructure instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public void update(AcctStructure persistanceInstance) throws Exception {
	getSession().update(persistanceInstance);
	getSession().flush();
    }

    public Iterator getAcctStructureList() throws Exception {
	Iterator results = null;
	results = getCurrentSession().createQuery(
		"select acctstructure.id,acctstructure.acctStructureName from AcctStructure acctstructure order by acctstructure.acctStructureName").list().iterator();
	return results;
    }

    // to get the accoutnstructure id value
    public String getAcctStructDesc1(String acctgrop) throws Exception {
	String result = null;
	String qeury = "select  acct.acctStructureDesc from AcctStructure acct where acct.id='" + acctgrop + "'";
	Iterator it = getCurrentSession().createQuery(qeury).list().iterator();
	result = (String) it.next();
	return result;
    }

    /* to set the values in dispaly tag for show all method
    
     * to set genenic object in DAO 
     * */
    public List findForShowAll() throws Exception {
	AccountStructureBean objAccountStructureBean = null;
	List<AccountStructureBean> lstGenericIfo = new ArrayList<AccountStructureBean>();
	String queryString = "select acct.acctStructureName,acct.acctStructureDesc from AcctStructure acct";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	while (iter.hasNext()) {
	    //objChartCodeBean = new ChartOfAccountBean();
	    objAccountStructureBean = new AccountStructureBean();
	    Object[] row = (Object[]) iter.next();
	    String seg_code = (String) (row[0]);
	    String seg_desc = (String) row[1];
	    Integer seg_leng = (Integer) row[2];
	    objAccountStructureBean.setSeg_code(seg_code);
	    objAccountStructureBean.setSeg_desc(seg_desc);
	    objAccountStructureBean.setSeg_leng(seg_leng);
	    lstGenericIfo.add(objAccountStructureBean);
	    objAccountStructureBean = null;
	}
	return lstGenericIfo;
    }

    public Iterator deleteseg(int id) throws Exception {
	Iterator results = null;
	results = getCurrentSession().createQuery(
		"delete from Segment seg where seg.id='" + id + "'").list().iterator();
	return results;
    }

    public List findForShowAll1(int acctdesc) throws Exception {
	AccountStructureBean objAccountStructureBean = null;
	List<AccountStructureBean> lstGenericIfo = new ArrayList<AccountStructureBean>();
	String queryString = "select acct.id,acct.acctStructureName,acct.acctStructureDesc from AcctStructure acct where acct.id='" + acctdesc + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	while (iter.hasNext()) {
	    objAccountStructureBean = new AccountStructureBean();
	    Object[] row = (Object[]) iter.next();
	    Integer id = (Integer) (row[0]);
	    String acct_desc = (String) (row[1]);
	    String acct_name = (String) row[2];
	    objAccountStructureBean.setAcctId(id);
	    objAccountStructureBean.setAcctDesc(acct_desc);
	    objAccountStructureBean.setAcctName("acctname:>" + acct_name);
	    lstGenericIfo.add(objAccountStructureBean);
	    objAccountStructureBean = null;
	}
	return lstGenericIfo;
    }

    public boolean deleteacctstructure(AcctStructure acctStructure) throws Exception {
	boolean flag = true;
	try {
	    org.hibernate.Transaction txt;
	    txt = getSession().beginTransaction();
	    getSession().delete(acctStructure);
	} catch (RuntimeException e) {
	    flag = false;
	} finally {
	    closeSession();
	}
	return flag;
    }

    public boolean deleteacct(AccountStructureBean coab) throws Exception {
	boolean flag = true;
	try {

	    org.hibernate.Transaction txt;
	    AcctStructure acctstruct = new AcctStructure();
	    acctstruct.setId(coab.getId());
	    acctstruct.setAcctStructureName(coab.getAcctName());
	    acctstruct.setAcctStructureDesc(coab.getAcctDesc());

	    txt = getSession().beginTransaction();
	    getSession().delete(acctstruct);
	} catch (RuntimeException e) {
	    flag = false;
	} finally {
	    closeSession();
	}
	return flag;
    }

    //added by Bhanu
    public List ForPrint(String acctstructure) throws Exception {
	AccountStructureDTO acctStructureDTO = null;
	List<AccountStructureDTO> lstGenericIfo = new ArrayList<AccountStructureDTO>();
	String queryString = "select acct.acctStructureName,acct.acctStructureDesc,seg.segmentCode,seg.segmentDesc,seg.Segment_leng,seg.id from AcctStructure acct,Segment seg where  acct.id='" + acctstructure + "' and seg.Account_structure_Id='" + acctstructure + "'";
	//String queryString = "select seg.segmentCode,seg.segmentDesc,seg.Segment_leng from Segment seg where  seg.Account_structure_Id='"+acct+"'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	while (iter.hasNext()) {
	    acctStructureDTO = new AccountStructureDTO();
	    Object[] row = (Object[]) iter.next();
	    String acctName1 = (String) (row[0]);
	    String acctDesc1 = (String) row[1];
	    String segCode = (String) row[2];
	    String segDesc = (String) row[3];
	    Integer segLen = (Integer) row[4];
	    String SegLen = (String.valueOf(segLen));
	    Integer segid = (Integer) row[5];

	    String queryString1 = "select segval.segmentValue,segval.segmentValueDesc from SegmentValues segval where segval.segmentCodeId='" + segid + "'";
	    List queryObject1 = getCurrentSession().createQuery(queryString1).list();
	    Iterator iter1 = queryObject1.iterator();
	    if (!iter1.hasNext()) {
		acctStructureDTO = new AccountStructureDTO();
		acctStructureDTO.setSegCode(segCode);
		acctStructureDTO.setSegDesc(segDesc);
		acctStructureDTO.setSegmentlength(SegLen);
		lstGenericIfo.add(acctStructureDTO);
		acctStructureDTO = null;
	    }
	    int i = 0;
	    while (iter1.hasNext()) {

		acctStructureDTO = new AccountStructureDTO();
		Object[] row1 = (Object[]) iter1.next();
		String segmentValue = (String) (row1[0]);
		String segmentValueDesc = (String) row1[1];

		if (segmentValue == null) {
		    segmentValue = "";
		}
		if (segmentValueDesc == null) {
		    segmentValueDesc = "";
		}
		if (acctDesc1 == null) {
		    acctDesc1 = "";
		}
		if (acctName1 == null) {
		    acctName1 = "";
		}
		if (segCode == null) {
		    segCode = "";
		}
		if (segDesc == null) {
		    segDesc = "";
		}
		if (SegLen == null) {
		    SegLen = "";
		}
		if (i == 0) {
		    acctStructureDTO.setAcctDesc1(acctDesc1);
		    acctStructureDTO.setAcctName1(acctName1);
		    acctStructureDTO.setSegCode(segCode);
		    acctStructureDTO.setSegDesc(segDesc);
		    acctStructureDTO.setSegmentlength(SegLen);
		}

		acctStructureDTO.setSegvalues(segmentValue);
		acctStructureDTO.setSegdesc(segmentValueDesc);
		lstGenericIfo.add(acctStructureDTO);
		acctStructureDTO = null;
		i++;
	    }

	}

	return lstGenericIfo;

    }
    //aded by Bhanu
}

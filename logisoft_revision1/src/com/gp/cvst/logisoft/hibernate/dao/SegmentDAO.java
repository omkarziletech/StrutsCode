package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.AccountStructureBean;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.gp.cvst.logisoft.beans.SegmentBean;
import com.gp.cvst.logisoft.domain.Segment;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Segment.
 * @see com.gp.cvst.logisoft.hibernate.dao.Segment
 * @author MyEclipse - Hibernate Tools
 */
public class SegmentDAO extends BaseHibernateDAO {

    public void save(Segment transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Segment persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public Segment findById(java.lang.Integer id) throws Exception {
        Segment instance = (Segment) getSession().get("com.gp.cvst.logisoft.domain.Segment", id);
        return instance;
    }

    public List findByExample(Segment instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.Segment").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from Segment as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public Segment merge(Segment detachedInstance) throws Exception {
        Segment result = (Segment) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(Segment instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(Segment instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findForShowAll() throws Exception {
        ChartOfAccountBean objChartCodeBean = null;
        List<ChartOfAccountBean> lstGenericIfo = new ArrayList<ChartOfAccountBean>();
        String queryString = "select acctsdetails.account,acctsdetails.acctDesc,acctsdetails.acctStatus,acctsdetails.normalBalance,acctsdetails.multiCurrency,acctsdetails.acctGroup from AccountDetails acctsdetails";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            objChartCodeBean = new ChartOfAccountBean();
            Object[] row = (Object[]) iter.next();


            /**
             *id1 is local variable
             * id is id in GenericCode table
             * strCode is code in GenericCode table
             * strDesc is codedesc in GenericCode table
             * strCodedesc is description in Codetype table
             */
            String account = (String) (row[0]);
            String desc = (String) row[1];
            String strstatus = (String) row[2];
            String strbalance = (String) row[3];
            String strCurrency = (String) row[4];
            String strgroup = (String) row[5];
            objChartCodeBean.setAcct(account);
            objChartCodeBean.setDesc(desc);
            objChartCodeBean.setMulticurrency(strCurrency);
            objChartCodeBean.setNormalbalance(strbalance);
            objChartCodeBean.setStatus(strstatus);
            objChartCodeBean.setGroup(strgroup);
            lstGenericIfo.add(objChartCodeBean);
            objChartCodeBean = null;
        }

        return lstGenericIfo;
    }
    // this is to reterive value for account struture id

    public List selectvalues(String acctdesc) throws Exception {
        List queryObject = null;
        String query = "select seg.segmentCode,seg.segmentDesc,seg.Segment_leng from Segment seg where seg.Account_structure_Id='" + acctdesc + "'";
        getCurrentSession().createQuery(query).list().iterator();
        return queryObject;
    }

    // this is to get the account structure value to dispaly tags.......
    public List findForShow(String acctdesc) throws Exception {
        AccountStructureBean objAccountStructureBean = null;
        List<AccountStructureBean> lstGenericIfo = new ArrayList<AccountStructureBean>();

        String queryString = "select seg.id,seg.segmentCode,seg.segmentDesc,seg.Segment_leng,seg.validateList from Segment seg where seg.Account_structure_Id='" + acctdesc + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();

        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            //objChartCodeBean = new ChartOfAccountBean();
            objAccountStructureBean = new AccountStructureBean();
            Object[] row = (Object[]) iter.next();

            /**
             *id1 is local variable
             * id is id in GenericCode table
             * strCode is code in GenericCode table
             * strDesc is codedesc in GenericCode table
             * strCodedesc is description in Codetype table
             */
            Integer id = (Integer) (row[0]);
            String seg_code = (String) (row[1]);
            String seg_desc = (String) row[2];
            Integer seg_leng = (Integer) row[3];
            String validateList = (String) row[4];
            objAccountStructureBean.setId(id);
            objAccountStructureBean.setSeg_code(seg_code);
            objAccountStructureBean.setSeg_desc(seg_desc);
            objAccountStructureBean.setSeg_leng(seg_leng);
            objAccountStructureBean.setValidateList(validateList);
            lstGenericIfo.add(objAccountStructureBean);
            objAccountStructureBean = null;

        }

        return lstGenericIfo;
    }

    public String countAcctStru(int acctdesc) throws Exception {
        String queryString = "select count (*) from Segment seg  where seg.Account_structure_Id='" + acctdesc + "'";
        String count = (String)getCurrentSession().createQuery(queryString).uniqueResult().toString();
        return count;
    }

    public void savesegment(String editdesc, String editcode, int leng) throws Exception {
        String query = "UPDATE Segment seg set seg.segmentDesc='" + editdesc + "' where seg.segmentCode='" + editcode + "' and  seg.Segment_leng='" + leng + "' ";
        getCurrentSession().createQuery(query).executeUpdate();
    }

    public void segdescupdate(int id, String editdesc) throws Exception {
        String query = " UPDATE Segment seg set seg.segmentDesc='" + editdesc + "' where seg.id='" + id + "' ";
        getCurrentSession().createQuery(query).executeUpdate();
    }

    public String selectsegid(String segCodeBeanAcctid, String segmentcode) throws Exception {
        String query = "SELECT seg.id from Segment seg where seg.Account_structure_Id='" + segCodeBeanAcctid + "' and seg.segmentCode='" + segmentcode + "' limit 1";
        Object result = getCurrentSession().createQuery(query).uniqueResult();
        return null!=result?result.toString():"";
    }

    public String selectAccountCodeid(int acct_id, String segmentcode) throws Exception {
        String query = "SELECT seg.id from Segment seg where seg.Account_structure_Id='" + acct_id + "' and seg.segmentCode='" + segmentcode + "' limit 1";
        Object result =getCurrentSession().createQuery(query).uniqueResult();
        return null!=result?result.toString():"";
    }

    public boolean deleteseg(AccountStructureBean coab) throws Exception {
        boolean flag = true;
        org.hibernate.Transaction txt;
        Segment segment = new Segment();
        segment.setId(coab.getId());
        segment.setSegment_leng(coab.getSeg_leng());
        segment.setSegmentCode(coab.getSeg_code());
        segment.setSegmentDesc(coab.getSeg_desc());
        txt = getSession().beginTransaction();
        getSession().delete(segment);
        return flag;
    }

    public List selectlist(int asId1) throws Exception {
        SegmentBean objSegmentBean = null;
        List<SegmentBean> lstGenericIfo = new ArrayList<SegmentBean>();
        String queryString = "select seg.segmentCode,seg.segmentDesc,seg.Account_structure_Id,seg.Segment_leng from Segment seg where seg.Account_structure_Id='" + asId1 + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            objSegmentBean = new SegmentBean();
            Object[] row = (Object[]) iter.next();
            String segmentCode = (String) (row[0]);
            String segmentDesc = (String) (row[1]);
            int Segment_leng = (Integer) row[3];
            objSegmentBean.setSegcode(segmentCode);
            objSegmentBean.setSegdesc(segmentDesc);
            objSegmentBean.setSegleng(String.valueOf(Segment_leng));
            objSegmentBean = null;
        }
        return lstGenericIfo;
    }

    public String getValidateListforSegment(Integer id) throws Exception {
        String qeury = "select  seg.validateList from Segment seg where seg.id='" + id + "'";
        Object result=getCurrentSession().createQuery(qeury).setMaxResults(1).uniqueResult();
        return null!=result?result.toString():"";

    }
}

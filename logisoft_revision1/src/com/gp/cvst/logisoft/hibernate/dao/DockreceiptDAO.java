package com.gp.cvst.logisoft.hibernate.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.beans.DocRecieptBean;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.Dockreceipt;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Dockreceipt.
 * @see com.gp.cvst.logisoft.hibernate.dao.Dockreceipt
 * @author MyEclipse - Hibernate Tools
 */
public class DockreceiptDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(DockreceiptDAO.class);

    public void save(Dockreceipt transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Dockreceipt persistentInstance)throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public Dockreceipt findById(java.lang.Integer id)throws Exception {
            Dockreceipt instance = (Dockreceipt) getSession().get("com.gp.cvst.logisoft.domain.Dockreceipt", id);
            return instance;
    }

    public List findByExample(Dockreceipt instance)throws Exception {
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.Dockreceipt").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception {
            String queryString = "from Dockreceipt as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public Dockreceipt merge(Dockreceipt detachedInstance)throws Exception {
            Dockreceipt result = (Dockreceipt) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Dockreceipt instance)throws Exception {
            getSession().saveOrUpdate(instance);
    }

    public void attachClean(Dockreceipt instance)throws Exception {
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List docrecipets() throws Exception {
        List<DocRecieptBean> docreciept = new ArrayList<DocRecieptBean>();
        DocRecieptBean DRB = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String querystring = "";
        querystring = "select d.dockReceipt,d.piece,d.cuftWarehouse,d.remarks,d.hazmat,d.dateIn,d.eta,d.etd,d.status,d.consignee,d.hazNotes,d.genNotes,d.loadingInstr,d.weight,d.cft from Dockreceipt d where d.loadingInstr='released' ";

        List queryObject = getCurrentSession().createQuery(querystring).list();
        Iterator iter = queryObject.iterator();

        while (iter.hasNext()) {
            DRB = new DocRecieptBean();
            Object row[] = (Object[]) iter.next();
            Integer dockreceipt = (Integer) row[0];
            Integer piece = (Integer) row[1];
            String cutfwarehouse = (String) row[2];
            String remarks = (String) row[3];
            String hazmat = (String) row[4];
            Date tdate = (Date) row[5];
            String docDate = "";
            if (tdate == null) {
                docDate = "";
            } else {
                docDate = sdf.format(tdate);
            }
            Date ETA = (Date) row[6];
            String eta = "";
            if (ETA == null) {
                eta = "";
            } else {
                eta = sdf.format(ETA);
            }
            Date ETD = (Date) row[7];
            String etd = "";
            if (ETD == null) {
                etd = "";
            } else {
                etd = sdf.format(ETD);
            }
            String status = (String) row[8];
            String consignee = (String) row[9];
            String hazNotes = (String) row[10];
            String genNotes = (String) row[11];
            String loadingInstr = (String) row[12];
            String weight = (String) row[13];
            String cft = (String) row[14];

            DRB.setDockReceipt(dockreceipt);
            DRB.setPiece(piece);
            DRB.setCuftWarehouse(cutfwarehouse);
            DRB.setRemarks(remarks);
            DRB.setHazmat(hazmat);
            DRB.setDateIn(docDate);
            DRB.setEta(eta);
            DRB.setEtd(etd);
            DRB.setStatus(status);
            DRB.setConsignee(consignee);
            DRB.setHazNotes(hazNotes);
            DRB.setGenNotes(genNotes);
            DRB.setLoadingInstr(loadingInstr);
            DRB.setWeight(weight);
            DRB.setCft(cft);

            docreciept.add(DRB);
            DRB = null;
        }


        return docreciept;
    }

    public int getDocid(String paramid)throws Exception {
        int result = 0;
        String qeury = "select  q. dockId from Dockreceipt q where q.dockReceipt='" + paramid + "'";
            Iterator it = getCurrentSession().createQuery(qeury).list().iterator();
            result = (Integer) it.next();
        return result;
    }

    public void updateStatus(String id, String flagstatus, String UnitNum)throws Exception {
        String query = "";
            query = "update Dockreceipt dt  SET loadingInstr ='" + flagstatus + "', unitnumbers='" + UnitNum + "' where dt.dockReceipt='" + id + "'";
            int query1 = getCurrentSession().createQuery(query).executeUpdate();
    }

    public List docrecipets1(String UnitNum)throws Exception {
        List<DocRecieptBean> docreciept = new ArrayList<DocRecieptBean>();
        DocRecieptBean DRB = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String querystring = "";
        querystring = "select d.dockReceipt,d.piece,d.cuftWarehouse,d.remarks,d.hazmat,d.dateIn,d.eta,d.etd,d.status,d.consignee,d.hazNotes,d.genNotes,d.loadingInstr,d.weight,d.cft from Dockreceipt d where  d.loadingInstr='loaded' and d.unitnumbers='" + UnitNum + "' ";

        List queryObject = getCurrentSession().createQuery(querystring).list();
        Iterator iter = queryObject.iterator();
        while (iter.hasNext()) {
            DRB = new DocRecieptBean();
            Object row[] = (Object[]) iter.next();
            Integer dockreceipt = (Integer) row[0];
            Integer piece = (Integer) row[1];
            String cutfwarehouse = (String) row[2];
            String remarks = (String) row[3];
            String hazmat = (String) row[4];
            Date tdate = (Date) row[5];
            String docDate = "";
            if (tdate == null) {
                docDate = "";
            } else {
                docDate = sdf.format(tdate);
            }
            Date ETA = (Date) row[6];
            String eta = "";
            if (ETA == null) {
                eta = "";
            } else {
                eta = sdf.format(ETA);
            }
            Date ETD = (Date) row[7];
            String etd = "";
            if (ETD == null) {
                etd = "";
            } else {
                etd = sdf.format(ETD);
            }
            String status = (String) row[8];
            String consignee = (String) row[9];
            String hazNotes = (String) row[10];
            String genNotes = (String) row[11];
            String loadingInstr = (String) row[12];
            String weight = (String) row[13];
            String cft = (String) row[14];
            DRB.setDockReceipt(dockreceipt);
            DRB.setPiece(piece);
            DRB.setCuftWarehouse(cutfwarehouse);
            DRB.setRemarks(remarks);
            DRB.setHazmat(hazmat);
            DRB.setDateIn(docDate);
            DRB.setEta(eta);
            DRB.setEtd(etd);
            DRB.setStatus(status);
            DRB.setConsignee(consignee);
            DRB.setHazNotes(hazNotes);
            DRB.setGenNotes(genNotes);
            DRB.setLoadingInstr(loadingInstr);
            DRB.setWeight(weight);
            DRB.setCft(cft);

            docreciept.add(DRB);
            DRB = null;
        }


        return docreciept;
    }

    public String getTotal(String flagvalue, String unitNum)throws Exception {
        String result = "";
            String query = "select sum(cft) from Dockreceipt where loadingInstr='" + flagvalue + "' and unitnumbers='" + unitNum + "'";


            Iterator itr = getCurrentSession().createQuery(query).iterate();
            if (result == null) {
                result = "0";
            }
            while (itr.hasNext()) {

                result = (String) itr.next();
            }
        return result;
    }

    public String getWeights(String flagvalue, String unitNum)throws Exception {
        String result = "";
            String query = "select sum(weight) from Dockreceipt where loadingInstr='" + flagvalue + "' and unitnumbers='" + unitNum + "'";
            Iterator itr = getCurrentSession().createQuery(query).iterate();
            if (result == null) {
                result = "0";
            }
            while (itr.hasNext()) {

                result = (String) itr.next();


            }
        return result;
    }
}

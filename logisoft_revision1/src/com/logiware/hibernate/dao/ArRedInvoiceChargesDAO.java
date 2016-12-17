package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object (DAO) for domain model class ApInvoice.
 * @see com.gp.cvst.logisoft.hibernate.dao.ApInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class ArRedInvoiceChargesDAO extends BaseHibernateDAO {

    // property constants
    public void save(ArRedInvoiceCharges arRedInvoiceCharges) throws Exception {
        getSession().save(arRedInvoiceCharges);
        getSession().flush();
    }

    /**
     * @param persistentInstance
     */
    public void delete(ArRedInvoiceCharges arRedInvoiceCharges) throws Exception {
	getSession().delete(arRedInvoiceCharges);
	getSession().flush();
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from ArRedInvoiceCharges as model where model." + propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public ArRedInvoiceCharges findById(Integer id) throws Exception {
	ArRedInvoiceCharges instance = (ArRedInvoiceCharges) getSession().get("com.logiware.hibernate.domain.ArRedInvoiceCharges", id);
	return instance;
    }

    public Object sumOfCharges(String fileNumber) throws Exception {
	String queryString = "SELECT SUM(c.amount) FROM ar_red_invoice_charges c,ar_red_invoice a WHERE a.id = c.ar_red_invoice_id AND bl_dr_number = '" + fileNumber + "' AND a.ready_to_post = 'M'";
	Object object = getSession().createSQLQuery(queryString).uniqueResult();
	return object;
    }

    public List<ArRedInvoiceCharges> getCharges(Integer arRedInvoiceId) {
	Criteria criteria = getCurrentSession().createCriteria(ArRedInvoiceCharges.class);
	criteria.add(Restrictions.eq("arRedInvoiceId", arRedInvoiceId));
	return criteria.list();
    }

    public List<ArRedInvoiceCharges> getCharges(String invoiceNumber, String fileNumber) {
	Criteria criteria = getCurrentSession().createCriteria(ArRedInvoiceCharges.class);
	criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
	if(CommonUtils.isNotEmpty(fileNumber)){
	    criteria.add(Restrictions.eq("blDrNumber", "04-"+fileNumber));
	}
	return criteria.list();
    }
    
    public void insertArRedInvoiceCharge(String arRedInvoiceId, String chargeCode, String amount, String quantity, String description, String glAcct,
        String shipmentType,String terminal,String invoiceNumber,String blDrNumber,String lclDrNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ar_red_invoice_charges (ar_red_invoice_id,charge_code,amount,quantity,description,gl_account,shipment_type,");
        queryBuilder.append("terminal,invoice_number,bl_dr_number,lcl_dr_number) VALUES (").append(arRedInvoiceId).append(",'").append(chargeCode).append("',");
        queryBuilder.append(amount).append(",'").append(quantity).append("','").append(description).append("','").append(glAcct).append("','");
        queryBuilder.append(shipmentType).append("','").append(terminal).append("','").append(invoiceNumber).append("','");
        queryBuilder.append(blDrNumber).append("','").append(lclDrNumber).append("')");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }
    
    public boolean isUniqueInvoceCharge(String invoiveNo,String fileNo){ 
       return true?getCharges(invoiveNo,fileNo).size()<1:false;
    }
}

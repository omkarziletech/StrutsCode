package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.ArinvoiceCharges;

public class ArInvoiceChargesDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(ArInvoiceDAO.class);

	public List getInvoiceChargesList(Integer invoiceNumber){
		List resultList=new ArrayList();
		String queryString="select sum(amount),id from ArinvoiceCharges where arInvoice.id=?0 group by arInvoice.id";
		Query queryObject = getSession().createQuery(queryString);
		queryObject.setParameter("0", invoiceNumber);
		for (int i = 0; i < queryObject.list().size(); i++) {
			Object[] row =  (Object[]) queryObject.list().get(i);
			ArinvoiceCharges arinvoiceCharges=new ArinvoiceCharges();
			arinvoiceCharges.setAmount((Double)row[0]);
			resultList.add(arinvoiceCharges);
		}
		return resultList;
	}
}

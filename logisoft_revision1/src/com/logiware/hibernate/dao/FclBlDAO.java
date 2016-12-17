/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.dao;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;

/**
 *
 * @author Owner
 */
public class FclBlDAO extends BaseHibernateDAO<FclBlNew> {

    public FclBlDAO() {
	super(FclBlNew.class);
    }

    /**
     * Save or Update FclBlNew into the table.
     * @param instance - Instance to be updated
     */
    public void saveOrUpdate(FclBlNew instance)throws Exception {
	    getCurrentSession().saveOrUpdate(instance);
	    getCurrentSession().flush();
	    new SedFilingsDAO().updateBookingNumber(instance.getFileNo(), instance.getBookingNo());
	    getCurrentSession().clear();
    }
    public void updateBolIdForBookingAccruals(String bolId)throws Exception{
        String query="update fcl_bl_costcodes set bolid=null where bolid = '"+bolId+"' and booking_id != 0";
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getSession().flush();
    }
    public String getBilltoForIncent(Integer bol,String chargeCode) throws Exception{
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select bl.house_bl from fcl_bl bl join fcl_bl_charges charges on charges.bolid=bl.bol where bl.bol=").append(bol);
        queryBuilder.append(" and charges.charge_code='").append(chargeCode).append("' and charges.fae_incent_flag='Y'");
        Object result=getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return null!=result?result.toString():"";
    }
    
    public String getSpotRateStatus(String fileNo) throws Exception{
        String q="select spot_rate from fcl_bl where file_no='"+fileNo+"' limit 1";
        return getCurrentSession().createSQLQuery(q).uniqueResult().toString();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTa;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 *
 * @author Logiware
 */
public class LclBookingAcTaDAO extends BaseHibernateDAO<LclBookingAcTa> {

    public LclBookingAcTaDAO() {
        super(LclBookingAcTa.class);
    }

    public String getlclbookingAcid(String fileId) throws Exception {
        String bookingacId = "";
        String queryString = "SELECT CONVERT(GROUP_CONCAT(lbacta.lcl_booking_ac_id)USING utf8) FROM lcl_booking_ac_ta  lbacta"
                + " LEFT JOIN lcl_booking_ac_trans lbctrans ON lbacta.lcl_booking_ac_trans_id = lbctrans.id "
                + "WHERE lbctrans.file_number_id = '" + fileId + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            bookingacId = o.toString();
        }
        return bookingacId;
    }

    public List findByTransChargesList(Long bkgtransacId) throws Exception {
        String queryString = "SELECT gm.charge_code,lbact.amount FROM lcl_booking_ac_ta lbact "
                + "JOIN lcl_booking_ac lbc ON lbact.lcl_booking_ac_id=lbc.id "
                + "JOIN gl_mapping gm ON lbc.ar_gl_mapping_id=gm.id "
                + "where lbact.lcl_booking_ac_trans_id='" + bkgtransacId + "'";
        SQLQuery query = getSession().createSQLQuery(queryString);
        List list = query.list();
        return list;
    }

    public void deleteBkgacTa(Long bkgtransId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM lcl_booking_ac_ta WHERE lcl_booking_ac_trans_id= ").append(bkgtransId);
        Query queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.executeUpdate();
    }

    public String getPaymentBalanceAmt(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT FORMAT(IF(lb.amt IS NOT NULL,SUM(lbc.ar_amount+lbc.adjustment_amount)-lb.amt,SUM(lbc.ar_amount+lbc.adjustment_amount)),2 ) AS balanceAmt FROM ");
        sb.append("(SELECT  SUM(lbata.amount) AS amt FROM lcl_booking_ac_trans lbat JOIN lcl_booking_ac_ta lbata ON lbata.lcl_booking_ac_trans_id=lbat.id ");
        sb.append("where lbat.file_number_id=").append(fileId).append(")").append(" lb join lcl_booking_ac lbc on lbc.file_number_id= ").append(fileId);
        sb.append(" AND lbc.ar_bill_to_party IN('N','C','T')");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void deleteBkgAcTaByBkgAcIds(String bookingAcIds) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from lcl_booking_ac_ta ");
        queryBuilder.append("where lcl_booking_ac_id in (").append(bookingAcIds).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }
}

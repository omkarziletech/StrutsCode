/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.LclImpProfitLossBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 *
 * @author Thamizh
 */
public class LclBookingPieceUnitDAO extends BaseHibernateDAO<LclBookingPieceUnit> {

    private static Logger log = Logger.getLogger(LclBookingPieceUnitDAO.class);

    public LclBookingPieceUnitDAO() {
        super(LclBookingPieceUnit.class);
    }

    public List<LclImpProfitLossBean> getUnitChargeCost(String unitSsId) throws Exception {
        List<LclImpProfitLossBean> chargeCostList = new ArrayList<LclImpProfitLossBean>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lbpu.lcl_unit_ss_id AS unitSsId,chargeAc.chargeNo AS chargeNo,chargeAc.chargeCode AS chargeCode,");
        sb.append("GROUP_CONCAT(lba.ar_amount) AS chargeAmount,GROUP_CONCAT(costAc.costNo) AS costNo,GROUP_CONCAT(costAc.costCode) AS costCode,GROUP_CONCAT(lba.ap_amount) ");
        sb.append("AS costAmount FROM lcl_booking_piece_unit lbpu JOIN lcl_booking_piece lbp ON lbpu.booking_piece_id = lbp.id ");
        sb.append("LEFT JOIN lcl_booking_ac lba ON lbp.file_number_id=lba.file_number_id ");
        sb.append("LEFT JOIN(SELECT charge.id AS glIdCharge, charge.bluescreen_chargecode AS chargeNo,");
        sb.append("charge.Charge_code AS chargeCode FROM gl_mapping charge)chargeAc ON lba.ar_gl_mapping_id = chargeAc.glIdCharge ");
        sb.append("LEFT JOIN(SELECT cost.id AS glIdCost,cost.bluescreen_chargecode AS costNo,");
        sb.append("cost.Charge_code AS costCode FROM gl_mapping cost)costAc ON lba.ap_gl_mapping_id = costAc.glIdCost ");
        sb.append("WHERE lbpu.lcl_unit_ss_id =").append(unitSsId).append(" AND lba.deleted=FALSE GROUP BY chargeAc.chargeNo");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclImpProfitLossBean.class));
        query.addScalar("unitSsId", StringType.INSTANCE);
        query.addScalar("chargeNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("chargeAmount", StringType.INSTANCE);
        query.addScalar("costNo", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("costAmount", StringType.INSTANCE);
        chargeCostList = query.list();
        return chargeCostList;
    }

    public void deletePickedBookedPieceUnit(Long fileId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery(" delete bu from lcl_booking_piece_unit bu join lcl_booking_piece bp on bp.id = bu.booking_piece_id "
                + " where bp.file_number_id =:fileId and bu.lcl_unit_ss_id is not null");
        query.setParameter("fileId", fileId);
        query.executeUpdate();
    }

    public void updateUnitSsId(Long fileId, Long unitSsId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_booking_piece_unit bu ");
        queryStr.append(" JOIN lcl_booking_piece bp ON bp.id = bu.booking_piece_id ");
        queryStr.append(" SET bu.lcl_unit_ss_id=:unitSsId WHERE bp.file_number_id =:fileId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("fileId", fileId);
        query.setLong("unitSsId", unitSsId);
        query.executeUpdate();
    }

    public void unPickByFile(Long bookingPieceId, Long unitSsId) throws Exception {
        String queryString = "delete from lcl_booking_piece_unit where booking_piece_id=?0 and  lcl_unit_ss_id =?1";
        SQLQuery queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", bookingPieceId);
        queryObject.setParameter("1", unitSsId);
        queryObject.executeUpdate();
    }
}

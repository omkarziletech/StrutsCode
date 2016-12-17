/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Logiware
 */
public class LclBookingPieceWhseDAO extends BaseHibernateDAO<LclBookingPieceWhse> {

    public LclBookingPieceWhseDAO() {
        super(LclBookingPieceWhse.class);
    }

    public List<LclBookingPieceWhse> findByFileAndCommodityList(Long bookingPieceId) throws Exception {
        String queryString = "from LclBookingPieceWhse where lclBookingPiece='" + bookingPieceId + "' order by id desc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public LclBookingPieceWhse findByFileAndCommodity(Long bookingPieceId) throws Exception {
        LclBookingPieceWhse lclBookingPieceWhse = null;
        String queryString = "from LclBookingPieceWhse where lclBookingPiece='" + bookingPieceId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        if (!list.isEmpty()) {
            lclBookingPieceWhse = (LclBookingPieceWhse) list.get(0);
        }
        return lclBookingPieceWhse;
    }

    @Override
    public List findByProperty(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(LclBookingPieceWhse.class, "whse");
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.addOrder(Order.asc("whse.enteredDatetime"));//end
        criteria.addOrder(Order.asc("whse.id"));
        return criteria.list();
    }

    public LclBookingPieceWhse findByWhseAndCommodity(Long bookingPieceId, Integer whseId) throws Exception {
        LclBookingPieceWhse lclBookingPieceWhse = null;
        String queryString = "from LclBookingPieceWhse where lclBookingPiece='" + bookingPieceId + "' and warehouse='" + whseId + "'";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        if (!list.isEmpty()) {
            lclBookingPieceWhse = (LclBookingPieceWhse) list.get(0);
        }
        return lclBookingPieceWhse;
    }

    public void insertLclBookingPieceWhse(Long pieceId, Integer warehouse, Integer userId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append(" INSERT INTO lcl_booking_piece_whse(booking_piece_id,warehouse_id,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) ");
        queryString.append(" values(:pieceId,:warehouse,:tdate,:userId,:tdate,:userId)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setLong("pieceId", pieceId);
        query.setInteger("warehouse", warehouse);
        query.setInteger("userId", userId);
        query.setParameter("tdate", new Date());
        query.executeUpdate();
    }

    public String getWareHouseOrder(String fileId) throws Exception {
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT lw.location FROM lcl_file_number lf ");
        queryString.append("JOIN lcl_booking_piece lb ON lf.id = lb.file_number_id ");
        queryString.append("JOIN lcl_booking_piece_whse lw ON lw.booking_piece_id = lb.id ");
        queryString.append("WHERE lf.id =:fileId AND lw.location <> '' ORDER BY lw.id DESC LIMIT 1 ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setString("fileId", fileId);
        return (String) query.uniqueResult();

    }

    public Integer getWarehouseId(Long bookingPieceId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT warehouse_id FROM `lcl_booking_piece_whse` ");
        queryBuilder.append(" WHERE booking_piece_id = :bookingPieceId  ORDER BY id DESC LIMIT 1");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("bookingPieceId", bookingPieceId);
        return (Integer) queryObject.uniqueResult();
    }

    public LclBookingPieceWhse findByFileAndCommodityBarrel(Long bookingPieceId) throws Exception {
        LclBookingPieceWhse lclBookingPieceWhse = null;
        String queryString = "from LclBookingPieceWhse lwh where lwh.lclBookingPiece='" + bookingPieceId + "' ORDER BY lwh.id DESC";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        if (!list.isEmpty()) {
            lclBookingPieceWhse = (LclBookingPieceWhse) list.get(0);
        }
        return lclBookingPieceWhse;
    }

    public boolean isContainWarehouseId(Long bookingPieceId, int warehouseId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT if(count(*) > 0,TRUE,FALSE) as result FROM `lcl_booking_piece_whse` ");
        queryBuilder.append(" WHERE booking_piece_id = :bookingPieceId  and warehouse_id=:warehouseId");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("bookingPieceId", bookingPieceId);
        queryObject.setInteger("warehouseId", warehouseId);
        return (boolean) queryObject.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
}

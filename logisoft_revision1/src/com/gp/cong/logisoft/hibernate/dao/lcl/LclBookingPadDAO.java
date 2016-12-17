/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.common.CommonUtils;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_DR_AUTO_NOTES;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.util.CommonFunctions;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
public class LclBookingPadDAO extends BaseHibernateDAO<LclBookingPad> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclBookingPadDAO() {
        super(LclBookingPad.class);
    }

    public LclBookingPad createInstance(LclFileNumber lclFileNumber, User loginUser,
            Date now, RefTerminal terminal) throws Exception {
        LclBookingPad lclBookingPad = this.getLclBookingPadByFileNumber(lclFileNumber.getId());
        if (lclBookingPad == null) {
            lclBookingPad = new LclBookingPad();
            lclBookingPad.setLclFileNumber(lclFileNumber);
            lclBookingPad.setEnteredBy(loginUser);
            lclBookingPad.setEnteredDatetime(now);
            lclBookingPad.setIssuingTerminal(terminal);
            lclBookingPad.setPickupContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
            lclBookingPad.setDeliveryContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
        }
        lclBookingPad.setModifiedBy(loginUser);
        lclBookingPad.setModifiedDatetime(now);
        return lclBookingPad;
    }

    public LclBookingPad getLclBookingPadByFileNumber(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingPad.class, "lclBookingPad");
        criteria.createAlias("lclBookingPad.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        return (LclBookingPad) criteria.setMaxResults(1).uniqueResult();
    }

    public LclBookingPad getLclBookingPadWithLclBookingAc(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingPad.class, "lclBookingPad");
        criteria.createAlias("lclBookingPad.lclFileNumber", "lclFileNumber");
        criteria.createAlias("lclBookingPad.lclBookingAc", "lclBookingAc");
        criteria.setFetchMode("lclBookingAc", FetchMode.JOIN);
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        return (LclBookingPad) criteria.uniqueResult();
    }

    public int deleteBookingPadByFileId(Long fileId) throws Exception {
        int deletedRows = 0;
        String queryString = "delete from lcl_booking_pad where file_number_id=?0";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        deletedRows = queryObject.executeUpdate();
        return deletedRows;
    }

    /*  public String getCarrierNameUsingScac(String scac) throws Exception {
     StringBuilder sb = new StringBuilder();
     sb.append("SELECT DISTINCT IF(acct_name IS NULL ,\"\",acct_name) AS acctName FROM ");
     sb.append("trading_partner tp LEFT JOIN carriers_or_line col ON tp.ssline_number = col.carrier_code ");
     sb.append("LEFT JOIN lcl_booking_pad lbp ON col.SCAC = lbp.scac ");
     sb.append("WHERE lbp.scac = '").append(scac).append("' Limit 1");
     return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
     } */
    public String getCarrierNameUsingScac(String scac) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT carrier_name FROM carriers_or_line WHERE scac='" + scac + "' LIMIT 1");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public void deleteBkgPadByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingPad where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public List getScacCodeList(String scac, String carrier) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SCAC,carrier_name FROM carriers_or_line ");
        if (scac == null && carrier == null) {
            sb.append("WHERE scac IS NOT NULL AND scac!='' ");
        } else if (scac != null && carrier != null) {
            if (!scac.equals("") && !carrier.equals("")) {
                sb.append("where scac like '").append(scac).append("%' and carrier_name like '").append(carrier).append("%' ");
            } else if (!scac.equals("") && carrier.equals("")) {
                sb.append("where scac like '").append(scac).append("%' and carrier_name IS NOT NULL and carrier_name!='' ");
            } else if (scac.equals("") && !carrier.equals("")) {
                sb.append("where carrier_name like '").append(carrier).append("%' and scac IS NOT NULL and scac!='' ");
            } else if (scac.equals("") && carrier.equals("")) {
                sb.append("WHERE scac IS NOT NULL AND scac!='' ");
            }
        }
        sb.append("order by carrier_name");
        SQLQuery query = getSession().createSQLQuery(sb.toString());
        List scacList = query.list();
        return scacList;
    }

    public boolean getlclBookingPadList(LclBooking lclBooking) throws Exception {
        boolean flag = false;
        if (lclBooking != null && lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getLclBookingPadList() != null
                && !lclBooking.getLclFileNumber().getLclBookingPadList().isEmpty()) {
            LclBookingPad lclBookingPad = lclBooking.getLclFileNumber().getLclBookingPadList().get(0);
            if (CommonUtils.isNotEmpty(lclBookingPad.getPickUpTo())
                    || CommonUtils.isNotEmpty(lclBookingPad.getDeliveryHours())
                    || CommonFunctions.isNotNull(lclBookingPad.getPickupReadyDate())
                    || CommonFunctions.isNotNull(lclBookingPad.getPickupCutoffDate())
                    || CommonUtils.isNotEmpty(lclBookingPad.getPickupReadyNote())
                    || CommonUtils.isNotEmpty(lclBookingPad.getTermsOfService())) {
                flag = true;
            }
        }
        return flag;
    }

    public String[] updateDeliveryContactForExport(String fileId, String originUnLocCode) throws Exception {
        if (CommonUtils.isNotEmpty(originUnLocCode)) {
            String warehouse[] = new LclDwr().getdeliverCargoDetails(originUnLocCode);
            if (CommonUtils.isNotEmpty(fileId)) {
                LclBookingPad lclBookingPad = new LclBookingPadDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
                if (null != lclBookingPad) {
                    LclContact deliveryContact = lclBookingPad.getDeliveryContact();
                    if (null == deliveryContact) {
                        deliveryContact = new LclContact();
                        deliveryContact.setEnteredBy(lclBookingPad.getEnteredBy());
                        deliveryContact.setModifiedBy(lclBookingPad.getEnteredBy());
                        deliveryContact.setEnteredDatetime(new Date());
                        deliveryContact.setModifiedDatetime(new Date());
                    }
                    deliveryContact.setCompanyName(null != warehouse[0] ? warehouse[1] + "-" + warehouse[0] : "");
                    deliveryContact.setAddress(null != warehouse[2] ? warehouse[2] : "");
                    deliveryContact.setCity(null != warehouse[3] ? warehouse[3] : "");
                    deliveryContact.setState(null != warehouse[4] ? warehouse[4] : "");
                    deliveryContact.setZip(null != warehouse[5] ? warehouse[5] : "");
                    deliveryContact.setPhone1(null != warehouse[6] ? warehouse[6] : "");
                    deliveryContact.setFax1(null != warehouse[7] ? warehouse[7] : "");
                    new LCLContactDAO().saveOrUpdate(deliveryContact);
                }
            }
            return warehouse;
        }
        return null;
    }

    public String getCountryForBkgDestinationServices(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  REPLACE(CASE WHEN gl.charge_code = 'ONCARR'  ");
        sb.append(" THEN CONCAT(bt.`country`, ' CY') WHEN gl.charge_code = 'DELIV'  ");
        sb.append(" THEN CONCAT('DOOR ', bt.`country`) WHEN gl.charge_code = 'DAP' ");
        sb.append(" THEN CONCAT('DAP ', bt.`country`) WHEN gl.charge_code = 'DDP' ");
        sb.append(" THEN CONCAT('DDP ', bt.`country`) END,'/',',') AS concatedCity  ");
        sb.append(" FROM lcl_booking_dstsvc bt JOIN lcl_booking_ac ac ON ac.`id` = bt.`booking_ac_id` ");
        sb.append(" JOIN gl_mapping gl ON gl.`id` = ac.`ar_gl_mapping_id`  ");
        sb.append(" WHERE bt.`file_number_id` =:fileId AND ac.`ar_amount` <> '0.00' AND gl.`Charge_code` <> 'DTHC-PREPAID'  ");
        sb.append(" HAVING concatedCity IS NOT NULL ORDER BY bt.id DESC ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("concatedCity", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

   // for Exports #mantis :13725
    public void updateDeliveryContact(LclBooking booking, String warehouse, String warhsAddress,
            String city, String state, String zip, String phone, String code, HttpServletRequest request) throws Exception {
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getByProperty("lclFileNumber.id", booking.getFileNumberId());
        User thisUser = (User) request.getSession().getAttribute("loginuser");
        if (null != lclBookingPad) {
            LclContact deliveryContact = lclBookingPad.getDeliveryContact();
            if (null == deliveryContact) {
                deliveryContact = new LclContact();
                deliveryContact.setEnteredBy(thisUser);
                deliveryContact.setModifiedBy(thisUser);
                deliveryContact.setEnteredDatetime(new Date());
                deliveryContact.setModifiedDatetime(new Date());
            }
            deliveryContact.setCompanyName(warehouse);
            deliveryContact.setAddress(warhsAddress);
            deliveryContact.setCity(city);
            deliveryContact.setState(state);
            deliveryContact.setZip(zip);
            deliveryContact.setPhone1(phone);
            new LCLContactDAO().saveOrUpdate(deliveryContact);
        }
        if (null != booking.getPooWhseContact()
                && !booking.getPooWhseContact().getCompanyName().equalsIgnoreCase(warehouse)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Deliver Cargo to-> (").append(booking.getPooWhseContact().getCompanyName().toUpperCase());
            stringBuilder.append(" to ").append(warehouse);
            new LclRemarksDAO().insertLclRemarks(booking.getLclFileNumber(), REMARKS_DR_AUTO_NOTES, stringBuilder.toString(), thisUser);
        }
    }
}

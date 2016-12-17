/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.common.CommonUtils;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_QT_AUTO_NOTES;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
public class LclQuotePadDAO extends BaseHibernateDAO<LclQuotePad> {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);

    public LclQuotePadDAO() {
        super(LclQuotePad.class);
    }

    public LclQuotePad createInstance(LclFileNumber lclFileNumber, User loginUser,
            Date now, RefTerminal terminal) throws Exception {
        LclQuotePad quotePad = this.getLclQuotePadByFileNumber(lclFileNumber.getId());
        if (quotePad == null) {
            quotePad = new LclQuotePad();
            quotePad.setLclFileNumber(lclFileNumber);
            quotePad.setEnteredBy(loginUser);
            quotePad.setEnteredDatetime(now);
            quotePad.setIssuingTerminal(terminal);
            quotePad.setPickupContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
            quotePad.setDeliveryContact(new LclContact(null, "", now, now, loginUser, loginUser, lclFileNumber));
        }
        quotePad.setModifiedBy(loginUser);
        quotePad.setModifiedDatetime(now);
        return quotePad;
    }

    public LclQuotePad getLclQuotePadByFileNumber(Long fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclQuotePad.class, "lclQuotePad");
        criteria.createAlias("lclQuotePad.lclFileNumber", "lclFileNumber");
        if (!CommonUtils.isEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        return (LclQuotePad) criteria.setMaxResults(1).uniqueResult();
    }

    public void deleteQuotePad(String fileId) throws Exception {
        String query = "delete from lcl_Quote_pad where file_number_id=" + fileId;
        SQLQuery deleteQuery = getSession().createSQLQuery(query);
        deleteQuery.executeUpdate();
    }

    public String[] updateQuoteDeliveryContactForExp(String fileId, String originUnLocCode) throws Exception {
        if (CommonUtils.isNotEmpty(originUnLocCode)) {
            String warehouse[] = new LclDwr().getdeliverCargoDetails(originUnLocCode);
            if (CommonUtils.isNotEmpty(fileId)) {
                LclQuotePad lclQuotePad = new LclQuotePadDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
                if (null != lclQuotePad) {
                    LclContact deliveryContact = lclQuotePad.getDeliveryContact();
                    if (null == deliveryContact) {
                        deliveryContact = new LclContact();
                        deliveryContact.setEnteredBy(lclQuotePad.getEnteredBy());
                        deliveryContact.setModifiedBy(lclQuotePad.getEnteredBy());
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

    public String getCountryForQuoteDestinationServices(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  REPLACE(CASE WHEN gl.charge_code = 'ONCARR'  ");
        sb.append(" THEN CONCAT(qt.`country`, ' CY') WHEN gl.charge_code = 'DELIV'  ");
        sb.append(" THEN CONCAT('DOOR ', qt.`country`) WHEN gl.charge_code = 'DAP' ");
        sb.append(" THEN CONCAT('DAP ', qt.`country`) WHEN gl.charge_code = 'DDP' ");
        sb.append(" THEN CONCAT('DDP ', qt.`country`) END,'/',',') AS concatedCity  ");
        sb.append(" FROM lcl_quote_dstsvc qt JOIN lcl_quote_ac ac ON ac.`id` = qt.`quote_ac_id` ");
        sb.append(" JOIN gl_mapping gl ON gl.`id` = ac.`ar_gl_mapping_id`  ");
        sb.append(" WHERE qt.`file_number_id` =:fileId  AND  ac.`ar_amount` <> '0.00' AND gl.`Charge_code` <> 'DTHC-PREPAID' ");
        sb.append(" HAVING concatedCity IS NOT NULL ORDER BY qt.id DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.addScalar("concatedCity", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }
    // for Exports #mantis :13725 
    public void updateDeliveryContact(LclQuote quote, String warehouse, String warhsAddress,
            String city, String state, String zip, String phone, String code, HttpServletRequest request) throws Exception {
        LclQuotePad lclQuotePad = new LclQuotePadDAO().getByProperty("lclFileNumber.id", quote.getFileNumberId());
        User thisUser = (User) request.getSession().getAttribute("loginuser");
        if (null != lclQuotePad) {
            LclContact deliveryContact = lclQuotePad.getDeliveryContact();
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
        if (null != quote.getPooWhseContact()
                && !quote.getPooWhseContact().getCompanyName().equalsIgnoreCase(warehouse)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Deliver Cargo to-> (").append(quote.getPooWhseContact().getCompanyName().toUpperCase());
            stringBuilder.append(" to ").append(warehouse);
            new LclRemarksDAO().insertLclRemarks(quote.getLclFileNumber(), REMARKS_QT_AUTO_NOTES, stringBuilder.toString(), thisUser);
        }
    }
}

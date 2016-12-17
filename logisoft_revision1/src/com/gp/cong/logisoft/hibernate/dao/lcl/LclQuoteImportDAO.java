/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import java.util.Date;
import org.hibernate.SQLQuery;

/**
 *
 * @author lakshh
 */
public class LclQuoteImportDAO extends BaseHibernateDAO<LclQuoteImport> implements LclCommonConstant {

    public LclQuoteImportDAO() {
        super(LclQuoteImport.class);
    }

    public String updateFreightRelease(String fileId, String releaseName, String userName, String buttonValue, String[] dateTimeArray, Date dateFormat, User user, LclQuoteImportDAO lclQuoteImportDAO) throws Exception {
        LclUtils lclUtils = new LclUtils();
        LclQuoteImport lclQuoteImport = lclQuoteImportDAO.findById(Long.parseLong(fileId));
        if (releaseName.equalsIgnoreCase("originalblbutton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setOriginalBlReceived(dateFormat);
                lclQuoteImport.setOriginalBlUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setOriginalBlReceived(null);
                lclQuoteImport.setOriginalBlUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Original B/L Received Reset", REMARKS_QT_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("customButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setCustomsClearanceReceived(dateFormat);
                lclQuoteImport.setCustomsClearanceUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setCustomsClearanceReceived(null);
                lclQuoteImport.setCustomsClearanceUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Customs Clearance Received Reset", REMARKS_QT_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("deliveryButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setDeliveryOrderReceived(dateFormat);
                lclQuoteImport.setDeliveryOrderUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setDeliveryOrderReceived(null);
                lclQuoteImport.setDeliveryOrderUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Delivery Order Received Reset", REMARKS_QT_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("releasebButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setReleaseOrderReceived(dateFormat);
                lclQuoteImport.setReleaseOrderUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setReleaseOrderReceived(null);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Release Order Received Reset", REMARKS_QT_AUTO_NOTES, user);
                lclQuoteImport.setReleaseOrderUserId(user);
            }
        }
        if (releaseName.equalsIgnoreCase("freightButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setFreightReleaseDateTime(dateFormat);
                lclQuoteImport.setFreightReleaseUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setFreightReleaseDateTime(null);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Freight Release Reset", REMARKS_QT_AUTO_NOTES, user);
                lclQuoteImport.setFreightReleaseUserId(user);
            }
        }
        if (releaseName.equalsIgnoreCase("paymentButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclQuoteImport.setPaymentReleaseReceived(dateFormat);
                lclQuoteImport.setPaymentReleaseUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclQuoteImport.setPaymentReleaseReceived(null);
                lclQuoteImport.setPaymentReleaseUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Payment Release Reset", REMARKS_QT_AUTO_NOTES, user);
            }
        }
        lclQuoteImportDAO.saveOrUpdate(lclQuoteImport);
        return "";
    }

    public void deleteFreightRelease(String fileId, String releaseName, String userId, LclQuoteImportDAO lclQuoteImportDAO, LclUtils lclUtils) throws Exception {
        LclQuoteImport lclQuoteImport = lclQuoteImportDAO.findById(Long.parseLong(fileId));
        if (releaseName.equalsIgnoreCase("Reset")) {
            lclQuoteImport.setOriginalBlReceived(null);
            User user = new UserDAO().findById(Integer.parseInt(userId));
            lclQuoteImport.setOriginalBlUserId(user);
            lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Original B/L Received Reset", REMARKS_QT_AUTO_NOTES, user);
        }
    }

    public void deleteDoorDeliveryData(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE ");
        queryBuilder.append("  `lcl_quote_import` lq ");
        queryBuilder.append(" SET");
        queryBuilder.append("  lq.`door_delivery_status` = NULL,");
        queryBuilder.append("  lq.`door_delivery_eta` = NULL ");
        queryBuilder.append(" WHERE lq.`file_number_id` =:fileNumberId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("fileNumberId", fileNumberId);
        queryObject.executeUpdate();
    }
}

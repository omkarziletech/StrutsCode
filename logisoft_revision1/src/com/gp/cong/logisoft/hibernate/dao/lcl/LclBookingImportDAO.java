/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.SQLQuery;

/**
 *
 * @author lakshh
 */
public class LclBookingImportDAO extends BaseHibernateDAO<LclBookingImport> implements LclCommonConstant {

    public LclBookingImportDAO() {
        super(LclBookingImport.class);
    }

    public void updatelclBkgImp(String fileId, String columnName1, String columnName2, Date dateTime, String userId) throws Exception {
        String dateTimeV = DateUtils.formatDate(dateTime, "dd-MMM-yyyy hh:mm a");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_import set ").append(columnName1).append("=' ").append(dateTimeV).append("', ").append(columnName2).append("=").append(userId);
        queryBuilder.append(" where file_number_id= ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String updateFreightRelease(String fileId, String releaseName, String userName, String buttonValue, String[] dateTimeArray, Date dateFormat, User user, LclBookingImportDAO lclBookingImportDAO) throws Exception {
        LclUtils lclUtils = new LclUtils();
        LclBookingImport lclBookingImport = lclBookingImportDAO.findById(Long.parseLong(fileId));
        lclBookingImport.setModifiedBy(user);
        lclBookingImport.setModifiedDatetime(new Date());
        if (releaseName.equalsIgnoreCase("originalblbutton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setOriginalBlReceived(dateFormat);
                lclBookingImport.setOriginalBlUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setOriginalBlReceived(null);
                lclBookingImport.setOriginalBlUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Original B/L Received Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("customButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setCustomsClearanceReceived(dateFormat);
                lclBookingImport.setCustomsClearanceUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setCustomsClearanceReceived(null);
                lclBookingImport.setCustomsClearanceUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Customs Clearance Received Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("deliveryButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setDeliveryOrderReceived(dateFormat);
                lclBookingImport.setDeliveryOrderUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setDeliveryOrderReceived(null);
                lclBookingImport.setDeliveryOrderUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Delivery Order Received Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("releasebButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setReleaseOrderReceived(dateFormat);
                lclBookingImport.setReleaseOrderUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setReleaseOrderReceived(null);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Release Order Received Reset", REMARKS_DR_AUTO_NOTES, user);
                lclBookingImport.setReleaseOrderUserId(user);
            }
        }
        if (releaseName.equalsIgnoreCase("freightButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setFreightReleaseDateTime(dateFormat);
                lclBookingImport.setFreightReleaseUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setFreightReleaseDateTime(null);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Freight Release Reset", REMARKS_DR_AUTO_NOTES, user);
                lclBookingImport.setFreightReleaseUserId(user);
            }
        }
        if (releaseName.equalsIgnoreCase("paymentButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setPaymentReleaseReceived(dateFormat);
                lclBookingImport.setPaymentReleaseUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setPaymentReleaseReceived(null);
                lclBookingImport.setPaymentReleaseUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Payment Release Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("cargoOnHoldButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setCargoOnHold(dateFormat);
                lclBookingImport.setCargoHoldUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setCargoOnHold(null);
                lclBookingImport.setCargoHoldUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Cargo On Hold Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        if (releaseName.equalsIgnoreCase("cargoGeneralOrderButton")) {
            if (buttonValue.equalsIgnoreCase("No")) {
                lclBookingImport.setCargoGeneralOrder(dateFormat);
                lclBookingImport.setCargoOrderUserId(user);
                return userName + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2];
            } else {
                lclBookingImport.setCargoGeneralOrder(null);
                lclBookingImport.setCargoOrderUserId(user);
                lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Cargo In general Order Reset", REMARKS_DR_AUTO_NOTES, user);
            }
        }
        lclBookingImportDAO.saveOrUpdate(lclBookingImport);
        return "";
    }

    public void deleteFreightRelease(String fileId, String releaseName, String userId, LclBookingImportDAO lclBookingImportDAO, LclUtils lclUtils) throws Exception {
        LclBookingImport lclBookingImport = lclBookingImportDAO.findById(Long.parseLong(fileId));
        if (releaseName.equalsIgnoreCase("Reset")) {
            lclBookingImport.setOriginalBlReceived(null);
            User user = new UserDAO().findById(Integer.parseInt(userId));
            lclBookingImport.setOriginalBlUserId(user);
            lclBookingImport.setModifiedBy(user);
            lclBookingImport.setModifiedDatetime(new Date());
            lclUtils.insertLCLRemarks(Long.parseLong(fileId), "Original B/L Received Reset", REMARKS_DR_AUTO_NOTES, user);
        }
    }

    public void deleteBkgImportByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingImport where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteDoorDeliveryData(Long fileNumberId, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" UPDATE ");
        queryBuilder.append("  `lcl_booking_import` lb ");
        queryBuilder.append(" SET");
        queryBuilder.append("  lb.`door_delivery_status` = NULL,");
        queryBuilder.append("  lb.`door_delivery_eta` = NULL,");
        queryBuilder.append("  lb.modified_by_user_id =:userId, ");
        queryBuilder.append("  lb.modified_datetime = SYSDATE() ");
        queryBuilder.append(" WHERE lb.`file_number_id` =:fileNumberId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("fileNumberId", fileNumberId);
        queryObject.setLong("userId", userId);
        queryObject.executeUpdate();
    }
    public String reverseImportsCharges(String fileNumberId, String unitSsId, HttpServletRequest request) throws Exception {
        String textMsg = "";
        try {

            User user = (User) request.getSession().getAttribute("loginuser");
            String realPath = request.getSession().getServletContext().getRealPath("/");
            new LclManifestDAO().getAllManifestImportsBookingsByUnitSS(Long.parseLong(unitSsId), null, null, user, false, realPath, true, fileNumberId);
            this.updateTrasshipment(Long.parseLong(fileNumberId), user.getUserId());
            textMsg = "success";
        } catch (Exception e) {
            return textMsg = "failure";
        }
        return textMsg;
    }

    public void updateTrasshipment(Long fileId, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE lcl_booking_import lb SET lb.transshipment = TRUE, ");
        queryBuilder.append(" lb.modified_datetime = SYSDATE(), lb.modified_by_user_id =:userId ");
        queryBuilder.append(" WHERE lb.`file_number_id` =:fileNumberId");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        queryObject.setLong("fileNumberId", fileId);
        queryObject.setInteger("userId", userId);
        queryObject.executeUpdate();
    }
}

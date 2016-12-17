/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.struts.form.lcl.ImportReleaseForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author mei
 */
public class ImportReleaseAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportReleaseForm importReleaseForm = (ImportReleaseForm) form;
        if (importReleaseForm.getModuleName() != null && !"".equals(importReleaseForm.getModuleName()) && LclCommonConstant.LCL_FILE_TYPE_QUOTE.equalsIgnoreCase(importReleaseForm.getModuleName())) {
            setFreightReleaseQuote(importReleaseForm, request);
        } else {
            setFreightReleaseBkg(importReleaseForm, request);
        }
        User user = (User) request.getSession().getAttribute("loginuser");
        importReleaseForm.setCurrentLoginName(user.getLoginName());
        importReleaseForm.setCurrentuserId(String.valueOf(user.getUserId()));
        request.setAttribute("importReleaseForm", importReleaseForm);
        return mapping.findForward("displayRelease");
    }

    public ActionForward saveRelease(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportReleaseForm importReleaseForm = (ImportReleaseForm) form;
        if (importReleaseForm.getModuleName() != null && !"".equals(importReleaseForm.getModuleName()) && LclCommonConstant.LCL_FILE_TYPE_QUOTE.equalsIgnoreCase(importReleaseForm.getModuleName())) {
            saveFreightReleaseQuote(importReleaseForm, request);
        } else {
            saveFreightReleaseBkg(importReleaseForm, request);
        }
        request.setAttribute("importReleaseForm", importReleaseForm);
        return mapping.findForward("displayRelease");
    }

    public void setFreightReleaseBkg(ImportReleaseForm importReleaseForm, HttpServletRequest request) throws Exception {
        LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
        LclBookingImport lclBookingImport = lclBookingImportDAO.findById(Long.parseLong(importReleaseForm.getFileNumberId()));
        if (CommonFunctions.isNotNull(lclBookingImport)) {
            if (CommonFunctions.isNotNull(lclBookingImport.getOriginalBlReceived())) {
                String date = DateUtils.formatDate(lclBookingImport.getOriginalBlReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("originalBl", (CommonFunctions.isNotNull(lclBookingImport.getOriginalBlUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getOriginalBlUserId().getLoginName())
                        ? lclBookingImport.getOriginalBlUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getCustomsClearanceReceived())) {
                String date = DateUtils.formatDate(lclBookingImport.getCustomsClearanceReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("customsClear", (CommonFunctions.isNotNull(lclBookingImport.getCustomsClearanceUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getCustomsClearanceUserId().getLoginName())
                        ? lclBookingImport.getCustomsClearanceUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getDeliveryOrderReceived())) {
                String date = DateUtils.formatDate(lclBookingImport.getDeliveryOrderReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("deliveryDate", (CommonFunctions.isNotNull(lclBookingImport.getDeliveryOrderUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getDeliveryOrderUserId().getLoginName())
                        ? lclBookingImport.getDeliveryOrderUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getReleaseOrderReceived())) {
                String date = DateUtils.formatDate(lclBookingImport.getReleaseOrderReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("releaseOrder", (CommonFunctions.isNotNull(lclBookingImport.getReleaseOrderUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getReleaseOrderUserId().getLoginName())
                        ? lclBookingImport.getReleaseOrderUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getFreightReleaseDateTime())) {
                String date = DateUtils.formatDate(lclBookingImport.getFreightReleaseDateTime(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("freightOrder", (CommonFunctions.isNotNull(lclBookingImport.getFreightReleaseUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getFreightReleaseUserId().getLoginName())
                        ? lclBookingImport.getFreightReleaseUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getPaymentReleaseReceived())) {
                String date = DateUtils.formatDate(lclBookingImport.getPaymentReleaseReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("paymentOrder", (CommonFunctions.isNotNull(lclBookingImport.getPaymentReleaseUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getPaymentReleaseUserId().getLoginName())
                        ? lclBookingImport.getPaymentReleaseUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getCargoOnHold())) {
                String date = DateUtils.formatDate(lclBookingImport.getCargoOnHold(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("cargoOnHold", (CommonFunctions.isNotNull(lclBookingImport.getCargoHoldUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getCargoHoldUserId().getLoginName())
                        ? lclBookingImport.getCargoHoldUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclBookingImport.getCargoGeneralOrder())) {
                String date = DateUtils.formatDate(lclBookingImport.getCargoGeneralOrder(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("cargoGeneralOrder", (CommonFunctions.isNotNull(lclBookingImport.getCargoOrderUserId()) && CommonUtils.isNotEmpty(lclBookingImport.getCargoOrderUserId().getLoginName())
                        ? lclBookingImport.getCargoOrderUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
        }
        request.setAttribute("lclBookingImport", lclBookingImport);
    }

    public void setFreightReleaseQuote(ImportReleaseForm importReleaseForm, HttpServletRequest request) throws Exception {
        LclQuoteImport lclQuoteImport = new LclQuoteImportDAO().findById(Long.parseLong(importReleaseForm.getFileNumberId()));
        if (CommonFunctions.isNotNull(lclQuoteImport)) {
            if (CommonFunctions.isNotNull(lclQuoteImport.getOriginalBlReceived())) {
                String date = DateUtils.formatDate(lclQuoteImport.getOriginalBlReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("originalBl", (CommonFunctions.isNotNull(lclQuoteImport.getOriginalBlUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getOriginalBlUserId().getLoginName())
                        ? lclQuoteImport.getOriginalBlUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclQuoteImport.getCustomsClearanceReceived())) {
                String date = DateUtils.formatDate(lclQuoteImport.getCustomsClearanceReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("customsClear", (CommonFunctions.isNotNull(lclQuoteImport.getCustomsClearanceUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getCustomsClearanceUserId().getLoginName())
                        ? lclQuoteImport.getCustomsClearanceUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclQuoteImport.getDeliveryOrderReceived())) {
                String date = DateUtils.formatDate(lclQuoteImport.getDeliveryOrderReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("deliveryDate", (CommonFunctions.isNotNull(lclQuoteImport.getDeliveryOrderUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getDeliveryOrderUserId().getLoginName())
                        ? lclQuoteImport.getDeliveryOrderUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclQuoteImport.getReleaseOrderReceived())) {
                String date = DateUtils.formatDate(lclQuoteImport.getReleaseOrderReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("releaseOrder", (CommonFunctions.isNotNull(lclQuoteImport.getReleaseOrderUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getReleaseOrderUserId().getLoginName())
                        ? lclQuoteImport.getReleaseOrderUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclQuoteImport.getFreightReleaseDateTime())) {
                String date = DateUtils.formatDate(lclQuoteImport.getFreightReleaseDateTime(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("freightOrder", (CommonFunctions.isNotNull(lclQuoteImport.getFreightReleaseUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getFreightReleaseUserId().getLoginName())
                        ? lclQuoteImport.getFreightReleaseUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
            if (CommonFunctions.isNotNull(lclQuoteImport.getPaymentReleaseReceived())) {
                String date = DateUtils.formatDate(lclQuoteImport.getPaymentReleaseReceived(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray = date.split(" ");
                request.setAttribute("paymentOrder", (CommonFunctions.isNotNull(lclQuoteImport.getPaymentReleaseUserId()) && CommonUtils.isNotEmpty(lclQuoteImport.getPaymentReleaseUserId().getLoginName())
                        ? lclQuoteImport.getPaymentReleaseUserId().getLoginName().toUpperCase() : "STG EDI") + "/" + dateTimeArray[0] + "/" + dateTimeArray[1] + " " + dateTimeArray[2]);
            }
        }
        request.setAttribute("lclBookingImport", lclQuoteImport);
    }

    public void saveFreightReleaseBkg(ImportReleaseForm importReleaseForm, HttpServletRequest request) throws Exception {
        LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
        LclBookingImport lclBookingImport = lclBookingImportDAO.findById(Long.parseLong(importReleaseForm.getFileNumberId()));
        if (lclBookingImport == null) {
            lclBookingImport = new LclBookingImport();
            lclBookingImport.setDestWhse(new WarehouseDAO().findById(17));
        }
        if (CommonUtils.isNotEmpty(importReleaseForm.getEntryNo())) {
            lclBookingImport.setEntryNo(importReleaseForm.getEntryNo());
        } else {
            lclBookingImport.setEntryNo(null);
        }
        if (CommonUtils.isNotEmpty(importReleaseForm.getFreightReleaseNote())) {
            lclBookingImport.setFreightReleaseBlNote(importReleaseForm.getFreightReleaseNote());
        } else {
            lclBookingImport.setFreightReleaseBlNote(null);
        }
        lclBookingImportDAO.saveOrUpdate(lclBookingImport);
    }

    public void saveFreightReleaseQuote(ImportReleaseForm importReleaseForm, HttpServletRequest request) throws Exception {
        LclQuoteImportDAO lclQuoteImportDAO = new LclQuoteImportDAO();
        LclQuoteImport lclQuoteImport = lclQuoteImportDAO.findById(Long.parseLong(importReleaseForm.getFileNumberId()));
        if (lclQuoteImport == null) {
            lclQuoteImport = new LclQuoteImport();
            lclQuoteImport.setDestWhse(new WarehouseDAO().findById(17));
        }
        if (CommonUtils.isNotEmpty(importReleaseForm.getEntryNo())) {
            lclQuoteImport.setEntryNo(importReleaseForm.getEntryNo());
        } else {
            lclQuoteImport.setEntryNo(null);
        }
        if (CommonUtils.isNotEmpty(importReleaseForm.getFreightReleaseNote())) {
            lclQuoteImport.setFreightReleaseBlNote(importReleaseForm.getFreightReleaseNote());
        } else {
            lclQuoteImport.setFreightReleaseBlNote(null);
        }
        lclQuoteImportDAO.saveOrUpdate(lclQuoteImport);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclDoorDelivery;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclDoorDeliveryDAO;
import com.gp.cvst.logisoft.struts.form.FclDoorDeliveryInfoForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author User
 */
public class FclDoorDeliveryInfoAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclDoorDeliveryInfoForm fclDoorDeliveryInfoForm = (FclDoorDeliveryInfoForm) form;
        FclDoorDeliveryDAO fclDoorDeliveryDAO = new FclDoorDeliveryDAO();
        FclDoorDelivery fclDoorDelivery = fclDoorDeliveryDAO.getFclDoorDeliveryByBol(fclDoorDeliveryInfoForm.getBolId());
        if (fclDoorDelivery != null) {
            if (null != fclDoorDelivery.getDeliveryDate()) {
                fclDoorDeliveryInfoForm.setDeliveryDate(DateUtils.formatDate(fclDoorDelivery.getDeliveryDate(), "MM/dd/yyyy HH:mm a"));
            }
            if (null != fclDoorDelivery.getFreeDate()) {
                fclDoorDeliveryInfoForm.setFreeDate(DateUtils.formatDate(fclDoorDelivery.getFreeDate(), "MM/dd/yyyy"));
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getLocalDeliveryOrTransferBy())) {
                fclDoorDeliveryInfoForm.setLocalDeliveryOrTransferBy(fclDoorDelivery.getLocalDeliveryOrTransferBy());
            }
            if (fclDoorDelivery.getManualDeliveryTo() != null && fclDoorDelivery.getManualDeliveryTo().equalsIgnoreCase("on")) {
                fclDoorDeliveryInfoForm.setDeliveryToDup(fclDoorDelivery.getDeliveryTo());
                fclDoorDeliveryInfoForm.setManualDeliveryTo(fclDoorDelivery.getManualDeliveryTo());
            } else {
                fclDoorDeliveryInfoForm.setDeliveryTo(fclDoorDelivery.getDeliveryTo());
                fclDoorDeliveryInfoForm.setDeliveryToAcctNo(fclDoorDelivery.getDeliveryToAcctNo());
                fclDoorDelivery.setManualDeliveryTo("off");
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryEmail())) {
                fclDoorDeliveryInfoForm.setDeliveryEmail(fclDoorDelivery.getDeliveryEmail());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryAddress())) {
                fclDoorDeliveryInfoForm.setDeliveryAddress(fclDoorDelivery.getDeliveryAddress());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryContactName())) {
                fclDoorDeliveryInfoForm.setDeliveryContactName(fclDoorDelivery.getDeliveryContactName());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryCity())) {
                fclDoorDeliveryInfoForm.setDeliveryCity(fclDoorDelivery.getDeliveryCity());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryState())) {
                fclDoorDeliveryInfoForm.setDeliveryState(fclDoorDelivery.getDeliveryState());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryZip())) {
                fclDoorDeliveryInfoForm.setDeliveryZip(fclDoorDelivery.getDeliveryZip());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryPhone())) {
                fclDoorDeliveryInfoForm.setDeliveryPhone(fclDoorDelivery.getDeliveryPhone());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryFax())) {
                fclDoorDeliveryInfoForm.setDeliveryFax(fclDoorDelivery.getDeliveryFax());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getPo())) {
                fclDoorDeliveryInfoForm.setPo(fclDoorDelivery.getPo());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getReferenceNumbers())) {
                fclDoorDeliveryInfoForm.setReferenceNumbers(fclDoorDelivery.getReferenceNumbers());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getBilling())) {
                fclDoorDeliveryInfoForm.setBilling(fclDoorDelivery.getBilling());
            }
        }
        return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclDoorDeliveryInfoForm fclDoorDeliveryInfoForm = (FclDoorDeliveryInfoForm) form;
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclDoorDeliveryDAO fclDoorDeliveryDAO = new FclDoorDeliveryDAO();
        FclBl fclBl = fclBlDAO.getFileNoObject(fclDoorDeliveryInfoForm.getFileNumber());
        fclBl.setUpdateBy(loginUser.getLoginName());
        fclBl.setUpdatedDate(now);
        fclBlDAO.save(fclBl);
        FclDoorDelivery doorDelivery = fclDoorDeliveryDAO.getFclDoorDeliveryByBol(fclDoorDeliveryInfoForm.getBolId());
        if (null == doorDelivery) {
            doorDelivery = new FclDoorDelivery();
        }
        doorDelivery.setBolId(fclBl.getBol());
        if (CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getManualDeliveryTo())) {
            doorDelivery.setManualDeliveryTo(fclDoorDeliveryInfoForm.getManualDeliveryTo());
        } else {
            doorDelivery.setManualDeliveryTo("off");
        }
        doorDelivery.setDeliveryDate(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryDate())
                ? DateUtils.parseDate(fclDoorDeliveryInfoForm.getDeliveryDate(), "MM/dd/yyyy HH:mm a") : null);
        doorDelivery.setFreeDate(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getFreeDate())
                ? DateUtils.parseToDate(fclDoorDeliveryInfoForm.getFreeDate()) : null);
            doorDelivery.setLocalDeliveryOrTransferBy(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getLocalDeliveryOrTransferBy()) ? fclDoorDeliveryInfoForm.getLocalDeliveryOrTransferBy() : null);
        if (fclDoorDeliveryInfoForm.getManualDeliveryTo() != null && fclDoorDeliveryInfoForm.getManualDeliveryTo().equalsIgnoreCase("on")) {
            doorDelivery.setDeliveryTo(fclDoorDeliveryInfoForm.getDeliveryToDup());
            doorDelivery.setDeliveryToAcctNo(null);
        } else {
            doorDelivery.setDeliveryTo(fclDoorDeliveryInfoForm.getDeliveryTo());
            doorDelivery.setDeliveryToAcctNo(fclDoorDeliveryInfoForm.getDeliveryToAcctNo());
        }
            doorDelivery.setDeliveryEmail(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryEmail()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryEmail(): "");
            doorDelivery.setDeliveryAddress(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryAddress()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryAddress() : "");
            doorDelivery.setDeliveryContactName(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryContactName()) ?
                    fclDoorDeliveryInfoForm.getDeliveryContactName() : "");
            doorDelivery.setDeliveryCity(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryCity()) ?
                    fclDoorDeliveryInfoForm.getDeliveryCity() : "");
            doorDelivery.setDeliveryState(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryState()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryState() : "");
            doorDelivery.setDeliveryZip(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryZip()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryZip() : "");
            doorDelivery.setDeliveryPhone(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryPhone()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryPhone() : "");
            doorDelivery.setDeliveryFax(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getDeliveryFax()) ? 
                    fclDoorDeliveryInfoForm.getDeliveryFax() : "");
            doorDelivery.setPo(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getPo()) ?
                    fclDoorDeliveryInfoForm.getPo() : "");
            doorDelivery.setReferenceNumbers(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getReferenceNumbers()) ? 
                    fclDoorDeliveryInfoForm.getReferenceNumbers() : "");
        doorDelivery.setBilling(CommonUtils.isNotEmpty(fclDoorDeliveryInfoForm.getBilling())
                ? fclDoorDeliveryInfoForm.getBilling() : "");
        fclDoorDeliveryDAO.save(doorDelivery);
        request.setAttribute("saveFlag", "Saved");
        return mapping.findForward("success");

    }
}

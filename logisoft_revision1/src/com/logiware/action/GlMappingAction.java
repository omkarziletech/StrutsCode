package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.logiware.bc.GlMappingBC;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.form.GlMappingForm;
import com.logiware.utils.AuditNotesUtils;
import com.oreilly.servlet.ServletUtils;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.apache.struts.actions.DispatchAction;

public class GlMappingAction extends DispatchAction {

    public ActionForward searchGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        request.getSession().setAttribute("glMappings", new GlMappingDAO().getGlMappings(glMappingForm.getSearchBychargeCode(), glMappingForm.getStartAccount(), glMappingForm.getEndAccount()));
        return mapping.findForward("success");
    }

    public ActionForward addGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        glMappingForm.setGlMapping(new GlMapping());
        return mapping.findForward("success");
    }

    public ActionForward viewGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        if (null != glMappingForm.getGlId() && glMappingForm.getGlId() != 0) {
            glMappingForm.setGlMapping(new GlMappingDAO().findById(glMappingForm.getGlId()));
        }
        return mapping.findForward("success");
    }

    public ActionForward editGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        if (null != glMappingForm.getGlId() && glMappingForm.getGlId() != 0) {
            glMappingForm.setGlMapping(new GlMappingDAO().findById(glMappingForm.getGlId()));
        }
        return mapping.findForward("success");
    }

    public ActionForward saveGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        try {
            if (null != glMappingForm.getGlMapping()) {
                User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
                GlMapping newGlMapping = glMappingForm.getGlMapping();
                GlMappingDAO glMappingDAO = new GlMappingDAO();
                if (CommonUtils.isEmpty(newGlMapping.getId())) {
                    StringBuilder message = new StringBuilder();
                    message.append("Charge/Cost code save failed : ");
                    if (!glMappingDAO.isValidChargeCode(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType())) {
                        message.append("Charge/Cost Code - ").append(newGlMapping.getChargeCode());
                        message.append(" is already mapped");
                        throw new AccountingException(message.toString());
                    } else if (!glMappingDAO.isValidBlueScreenChargeCode(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType())) {
                        message.append("Blue Screen Charge Code - ").append(newGlMapping.getBlueScreenChargeCode());
                        message.append(" is already mapped with different Charge/Cost Code");
                        throw new AccountingException(message.toString());
                    } else if (newGlMapping.isBluescreenFeedback() && !glMappingDAO.isValidBlueScreenFeedBack(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType(), newGlMapping.getRevExp(), newGlMapping.isBluescreenFeedback())) {
                        message.append("Blue Screen Feedback is already selected");
                        throw new AccountingException(message.toString());
                    } else {
                        newGlMapping.setId(null);
                        List<AccountDetails> accountDetailses = new AccountDetailsDAO().findByProperty("account", "%" + newGlMapping.getGlAcct() + "%");
                        if (CommonUtils.isEmpty(accountDetailses)) {
                            message.append(newGlMapping.getGlAcct()).append(" is not a valid GL account");
                            throw new AccountingException(message.toString());
                        } else {
                            newGlMapping.setCreatedDate(new Date());
                            newGlMapping.setCreatedBy(loginUser.getLoginName());
                            new GlMappingDAO().save(newGlMapping);
                            String codeTypeId = "" + new CodetypeDAO().getCodeTypeId("Cost Code");
                            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                            GenericCode genericCode = genericCodeDAO.getGenericCodeId(codeTypeId, newGlMapping.getChargeCode());
                            if (null == genericCode) {
                                genericCodeDAO.saveCodeFromGlMapping(newGlMapping.getChargeCode(), newGlMapping.getChargeDescriptions());
                            }
                            glMappingForm.setMessage("Charge/Cost code saved successfully");
                        }
                    }
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("Charge/Cost code update failed : ");
                    GlMapping oldGlMapping = glMappingDAO.findById(newGlMapping.getId());
                    if ((CommonUtils.isNotEqual(oldGlMapping.getBlueScreenChargeCode(), newGlMapping.getBlueScreenChargeCode())
                            || CommonUtils.isNotEqual(oldGlMapping.getChargeCode(), newGlMapping.getChargeCode())
                            || CommonUtils.isNotEqual(oldGlMapping.getTransactionType(), newGlMapping.getTransactionType())
                            || CommonUtils.isNotEqual(oldGlMapping.getShipmentType(), newGlMapping.getShipmentType()))
                            && !glMappingDAO.isValidChargeCode(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType())) {
                        message.append("Charge/Cost Code - ").append(newGlMapping.getChargeCode());
                        message.append(" is already mapped");
                        throw new AccountingException(message.toString());
                    } else if (!glMappingDAO.isValidBlueScreenChargeCode(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType())) {
                        message.append("Blue Screen Charge Code - ").append(newGlMapping.getBlueScreenChargeCode());
                        message.append(" is already mapped with different Charge/Cost Code");
                        throw new AccountingException(message.toString());
                    } else if (newGlMapping.isBluescreenFeedback() && !glMappingDAO.isValidBlueScreenFeedBack(newGlMapping.getBlueScreenChargeCode(), newGlMapping.getChargeCode(), newGlMapping.getTransactionType(), newGlMapping.getShipmentType(), newGlMapping.getRevExp(), newGlMapping.isBluescreenFeedback())) {
                        message.append("Blue Screen Feedback is already selected");
                        throw new AccountingException(message.toString());
                    } else {
                        List<AccountDetails> accountDetailses = new AccountDetailsDAO().findByProperty("account", "%" + newGlMapping.getGlAcct() + "%");
                        if (CommonUtils.isEmpty(accountDetailses)) {
                            message.append(newGlMapping.getGlAcct()).append(" is not a valid GL account");
                            throw new AccountingException(message.toString());
                        } else {
                            newGlMapping.setCreatedDate(oldGlMapping.getCreatedDate());
                            newGlMapping.setCreatedBy(oldGlMapping.getCreatedBy());
                            if(oldGlMapping.isDestinationServices()){
                                newGlMapping.setDestinationServices(true);
                            }else{                                
                                 newGlMapping.setDestinationServices(false);
                            }   
                            PropertyUtils.copyProperties(oldGlMapping, newGlMapping);
                            oldGlMapping.setUpdatedDate(new Date());
                            oldGlMapping.setUpdatedBy(loginUser.getLoginName());
                            String codeTypeId = "" + new CodetypeDAO().getCodeTypeId("Cost Code");
                            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                            GenericCode genericCode = genericCodeDAO.getGenericCodeId(codeTypeId, oldGlMapping.getChargeCode());
                            if (null == genericCode) {
                                genericCodeDAO.saveCodeFromGlMapping(oldGlMapping.getChargeCode(), oldGlMapping.getChargeDescriptions());
                            } else if (CommonUtils.isNotEmpty(oldGlMapping.getChargeDescriptions())) {
                                genericCode.setCodedesc(oldGlMapping.getChargeDescriptions());
                            }
                            glMappingForm.setMessage("Charge/Cost code updated successfully");
                        }
                    }
                }
            }
        } catch (AccountingException e) {
            glMappingForm.setMessage(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return mapping.findForward("success");
    }

    public ActionForward deleteGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        GlMapping glMapping = glMappingDAO.findById(glMappingForm.getGlId());
        if (null != glMapping) {
            User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
            StringBuilder desc = new StringBuilder("Charge Code '").append(glMapping.getChargeCode()).append("'");
            desc.append(" deleted by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.CHARGE_CODE, glMapping.getId().toString(),
                    NotesConstants.CHARGE_CODE, loginUser);
            String chargeCode = glMapping.getChargeCode();
            glMappingDAO.delete(glMapping);
            if (!glMappingDAO.isChargeCodeFound(chargeCode)) {
                String codeTypeId = "" + new CodetypeDAO().getCodeTypeId("Cost Code");
                GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                GenericCode genericCode = genericCodeDAO.getGenericCodeId(codeTypeId, chargeCode);
                if (null != genericCode) {
                    genericCodeDAO.delete(genericCode);
                }
            }

        }
        return mapping.findForward("success");
    }

    public ActionForward uploadGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        try {
            User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
            new GlMappingBC().uploadGlMapping(glMappingForm, loginUser.getLoginName());
            glMappingForm.setMessage("Charge/Cost code sheet uploaded successfully");
        } catch (Exception e) {
            glMappingForm.setMessage("Charge/Cost code sheet upload failed : " + e.getMessage());
        }
        return mapping.findForward("success");
    }

    public ActionForward exportGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        GlMappingForm glMappingForm = (GlMappingForm) form;
        try {
            String fileName = new GlMappingBC().exportGlMapping(glMappingForm);
            if (CommonUtils.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(fileName, response.getOutputStream());
                return null;
            }
        } catch (Exception e) {
            glMappingForm.setMessage("Charge/Cost code sheet export failed");
        }
        return mapping.findForward("success");
    }

    public ActionForward refreshGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("success");
    }
}

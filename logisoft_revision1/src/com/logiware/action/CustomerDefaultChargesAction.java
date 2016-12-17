package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.form.CustomerDefaultChargesForm;
import com.logiware.hibernate.dao.CustomerDefaultChargesDAO;
import com.logiware.hibernate.domain.CustomerDefaultCharges;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Shanmugam
 */
public class CustomerDefaultChargesAction extends DispatchAction {
    CustomerDefaultChargesDAO customerDefaultChargesDAO = new CustomerDefaultChargesDAO();
    public ActionForward showHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDefaultChargesForm customerDefaultChargesForm = (CustomerDefaultChargesForm) form;
        request.setAttribute("chargesList", customerDefaultChargesDAO.findByProperty("acctNo", customerDefaultChargesForm.getAcctNo()));
        return mapping.findForward("success");
    }
    public ActionForward addCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDefaultChargesForm customerDefaultChargesForm = (CustomerDefaultChargesForm) form;
        CustomerDefaultCharges customerDefaultCharges = new  CustomerDefaultCharges(customerDefaultChargesForm);
        customerDefaultChargesDAO.save(customerDefaultCharges);
        request.setAttribute("chargesList", customerDefaultChargesDAO.findByProperty("acctNo", customerDefaultChargesForm.getAcctNo()));
        return mapping.findForward("success");
    }
    public ActionForward editCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDefaultChargesForm customerDefaultChargesForm = (CustomerDefaultChargesForm) form;
        if(CommonUtils.isNotEmpty(customerDefaultChargesForm.getId())){
            CustomerDefaultCharges customerDefaultCharges = customerDefaultChargesDAO.findById(Integer.parseInt(customerDefaultChargesForm.getId()));
            request.setAttribute("charges", customerDefaultCharges);
            request.setAttribute("previousComment", customerDefaultCharges.getComment());
        }
        request.setAttribute("chargesList", customerDefaultChargesDAO.findByProperty("acctNo", customerDefaultChargesForm.getAcctNo()));
        return mapping.findForward("success");
    }
    public ActionForward updateCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDefaultChargesForm customerDefaultChargesForm = (CustomerDefaultChargesForm) form;
        if(CommonUtils.isNotEmpty(customerDefaultChargesForm.getId())){
            CustomerDefaultCharges customerDefaultCharges = customerDefaultChargesDAO.findById(Integer.parseInt(customerDefaultChargesForm.getId()));
            if(null != customerDefaultCharges){
                customerDefaultCharges.setChargeCode(customerDefaultChargesForm.getChargeCode());
                customerDefaultCharges.setChargeCodeDesc(CommonUtils.isNotEmpty(customerDefaultChargesForm.getChargeCodeDesc())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getChargeCodeDesc())):null);
                customerDefaultCharges.setCostType(CommonUtils.isNotEmpty(customerDefaultChargesForm.getCostType())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getCostType())):null);
                customerDefaultCharges.setUnitType(CommonUtils.isNotEmpty(customerDefaultChargesForm.getUnitType()) && !"0".equalsIgnoreCase(customerDefaultChargesForm.getUnitType())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getUnitType())):null);
                customerDefaultCharges.setVendorName(customerDefaultChargesForm.getVendorName());
                customerDefaultCharges.setVendorNumber(customerDefaultChargesForm.getVendorNumber());
                customerDefaultCharges.setCurrency(customerDefaultChargesForm.getCurrency());
                customerDefaultCharges.setDefaultCarrier(customerDefaultChargesForm.getDefaultCarrier());
                customerDefaultCharges.setComment(customerDefaultChargesForm.getComment());
                customerDefaultCharges.setCost(CommonUtils.isNotEmpty(customerDefaultChargesForm.getCost())?Double.parseDouble(customerDefaultChargesForm.getCost()):0d);
                customerDefaultCharges.setSell(CommonUtils.isNotEmpty(customerDefaultChargesForm.getSell())?Double.parseDouble(customerDefaultChargesForm.getSell()):0d);
                customerDefaultCharges.setAcctNo(customerDefaultChargesForm.getAcctNo());
            }
        }
        request.setAttribute("chargesList", customerDefaultChargesDAO.findByProperty("acctNo", customerDefaultChargesForm.getAcctNo()));
        return mapping.findForward("success");
    }
    public ActionForward deleteCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CustomerDefaultChargesForm customerDefaultChargesForm = (CustomerDefaultChargesForm) form;
        if(CommonUtils.isNotEmpty(customerDefaultChargesForm.getId())){
            CustomerDefaultCharges customerDefaultCharges = customerDefaultChargesDAO.findById(Integer.parseInt(customerDefaultChargesForm.getId()));
            if(null != customerDefaultCharges){
                customerDefaultChargesDAO.delete(customerDefaultCharges);
            }
        }
        request.setAttribute("chargesList", customerDefaultChargesDAO.findByProperty("acctNo", customerDefaultChargesForm.getAcctNo()));
        return mapping.findForward("success");
    }
    
}

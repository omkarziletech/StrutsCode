package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.logiware.form.PaymentReleaseForm;
import com.logiware.hibernate.dao.PaymentReleaseDAO;
import com.logiware.hibernate.domain.PaymentRelease;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Lakshmi Narayanan
 */
public class PaymentReleaseAction extends DispatchAction {

    public ActionForward showHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PaymentReleaseForm paymentReleaseForm = (PaymentReleaseForm) form;
        if(CommonUtils.isNotEmpty(paymentReleaseForm.getBolId())){
            FclBl fclBl = new FclBlDAO().findById(paymentReleaseForm.getBolId());
            if(null != fclBl){
                if(null != fclBl.getImportVerifiedEta()){
                    paymentReleaseForm.setImportReleaseOn(DateUtils.formatDate(fclBl.getImportVerifiedEta(), "MM/dd/yyyy hh:mm a"));
                }
                if(fclBl.isOverPaidStatus()){
                    paymentReleaseForm.setOverPaid("Over Paid");
                }
                if(CommonUtils.isNotEmpty(fclBl.getImportRelease())){
                    paymentReleaseForm.setImportRelease(fclBl.getImportRelease());
                }else{
                    paymentReleaseForm.setImportRelease("N");
                }
                if(CommonUtils.isNotEmpty(fclBl.getPaymentRelease())){
                    paymentReleaseForm.setPaymentRelease(fclBl.getPaymentRelease());
                }else{
                    paymentReleaseForm.setPaymentRelease("N");
                }
                if(CommonUtils.isNotEmpty(fclBl.getExpressRelease())){
                    paymentReleaseForm.setExpressRelease(fclBl.getExpressRelease());
                }else {
                    paymentReleaseForm.setExpressRelease("N");
                }
                if(null != fclBl.getExpressReleasedOn()){
                    paymentReleaseForm.setExpressReleasedOn(DateUtils.formatDate(fclBl.getExpressReleasedOn(), "MM/dd/yyyy hh:mm a"));
                }
                if(CommonUtils.isNotEmpty(fclBl.getDeliveryOrder())){
                    paymentReleaseForm.setDeliveryOrder(fclBl.getDeliveryOrder());
                }else {
                    paymentReleaseForm.setDeliveryOrder("N");
                }
                if(CommonUtils.isNotEmpty(fclBl.getCustomsClearance())){
                    paymentReleaseForm.setCustomsClearance(fclBl.getCustomsClearance());
                }else {
                    paymentReleaseForm.setCustomsClearance("N");
                }
                if(null != fclBl.getDeliveryOrderOn()){
                    paymentReleaseForm.setDeliveryOrderOn(DateUtils.formatDate(fclBl.getDeliveryOrderOn(), "MM/dd/yyyy hh:mm a"));
                }
                if(null != fclBl.getCustomsClearanceOn()){
                    paymentReleaseForm.setCustomsClearanceOn(DateUtils.formatDate(fclBl.getCustomsClearanceOn(), "MM/dd/yyy hh:mm a"));
                }
                paymentReleaseForm.setCustomsClearanceComment(fclBl.getCustomsClearanceComment());
                paymentReleaseForm.setDeliveryOrderComment(fclBl.getDeliveryOrderComment());
                paymentReleaseForm.setExpressReleaseComment(fclBl.getExpressReleaseComment());
                paymentReleaseForm.setImportReleaseComments(fclBl.getImportReleaseComments());
                paymentReleaseForm.setComment(fclBl.getPaymentReleaseComments());
                paymentReleaseForm.setFileNumber(fclBl.getFileNo());
                if(null != fclBl.getPaymentReleasedOn()){
                    paymentReleaseForm.setReleasedOn(DateUtils.formatDate(fclBl.getPaymentReleasedOn(), "MM/dd/yy hh:mm a"));
                }
                Object collectObject = new FclBlChargesDAO().sumOfCollectCharges(fclBl.getBol());
                if(null != collectObject){
                    Double collectAmount = (Double)collectObject;
                    paymentReleaseForm.setCollectAmount(""+collectAmount);
                }
                Object paidObject = new PaymentReleaseDAO().sumOfPaidCharges(fclBl.getBol());
                if(null != paidObject){
                    Double totalPaidAmount = (Double)paidObject;
                    request.setAttribute("totalPaidAmount", totalPaidAmount);
                }
                if(null != fclBl.getOriginalBlRequired()){
                    request.setAttribute("originlaBlrequiredForRelease", fclBl.getOriginalBlRequired());
                }
            }else{
                paymentReleaseForm.setImportRelease("N");
                paymentReleaseForm.setPaymentRelease("N");
                paymentReleaseForm.setExpressRelease("N");
                paymentReleaseForm.setDeliveryOrder("N");
                paymentReleaseForm.setCustomsClearance("N");
            }
        }
        
        request.setAttribute("paymentReleaseList", new PaymentReleaseDAO().findAll(paymentReleaseForm.getBolId()));
        return mapping.findForward("success");
    }
    public ActionForward Save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PaymentReleaseForm paymentReleaseForm = (PaymentReleaseForm) form;
        HttpSession session = request.getSession(true);
        String userName = "";
        User user = new User();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
         if(CommonUtils.isNotEmpty(paymentReleaseForm.getAmount()) && paymentReleaseForm.getAmount().contains(",")){
           paymentReleaseForm.setAmount(paymentReleaseForm.getAmount().replace(",", ""));
        }
        PaymentRelease paymentRelease = new PaymentRelease(paymentReleaseForm);
            paymentRelease.setPaidDate(DateUtils.parseToDateForMonthMMM(paymentReleaseForm.getPaidDate()));
            paymentRelease.setReleasedOn(DateUtils.parseDate(paymentReleaseForm.getReleasedOn(), "MM/dd/yyyy"));

        new PaymentReleaseDAO().save(paymentRelease);

        Object paidObject = new PaymentReleaseDAO().sumOfPaidCharges(paymentReleaseForm.getBolId());
        if(null != paidObject){
            Double totalPaidAmount = (Double)paidObject;
            overPaidStatus(paymentReleaseForm, totalPaidAmount);
            request.setAttribute("totalPaidAmount", totalPaidAmount);
        }
        new NotesBC().saveNotesWhileAddingPaymentCharges(paymentReleaseForm.getFileNumber(), userName, paymentRelease,"Added");
        request.setAttribute("paymentReleaseList", new PaymentReleaseDAO().findAll(paymentReleaseForm.getBolId()));
        return mapping.findForward("success");
    }
    public ActionForward Update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PaymentReleaseForm paymentReleaseForm = (PaymentReleaseForm) form;
        HttpSession session = request.getSession(true);
        String userName = "";
        User user = new User();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
        PaymentRelease paymentRelease = new PaymentReleaseDAO().findById(paymentReleaseForm.getId());
        paymentRelease.setCheckNumber(paymentReleaseForm.getCheckNumber());
        if(CommonUtils.isNotEmpty(paymentReleaseForm.getAmount()) && paymentReleaseForm.getAmount().contains(",")){
           paymentReleaseForm.setAmount(paymentReleaseForm.getAmount().replace(",", ""));
        }
        paymentRelease.setAmount(CommonUtils.isNotEmpty(paymentReleaseForm.getAmount())?Double.parseDouble(paymentReleaseForm.getAmount()):0d);
        paymentRelease.setComment(paymentReleaseForm.getComment());
        paymentRelease.setPaidBy(paymentReleaseForm.getPaidBy());
        paymentRelease.setPaymentRelease(paymentReleaseForm.getPaymentRelease());
            paymentRelease.setPaidDate(DateUtils.parseToDateForMonthMMM(paymentReleaseForm.getPaidDate()));
            paymentRelease.setReleasedOn(DateUtils.parseDate(paymentReleaseForm.getReleasedOn(), "MM/dd/yyyy"));
        new PaymentReleaseDAO().saveOrUpdate(paymentRelease);
        Object paidObject = new PaymentReleaseDAO().sumOfPaidCharges(paymentReleaseForm.getBolId());
        if(null != paidObject){
            Double totalPaidAmount = (Double)paidObject;
            overPaidStatus(paymentReleaseForm, totalPaidAmount);
            request.setAttribute("totalPaidAmount", totalPaidAmount);
        }
        new NotesBC().saveNotesWhileAddingPaymentCharges(paymentReleaseForm.getFileNumber(), userName, paymentRelease,"Updated");
        request.setAttribute("paymentReleaseList", new PaymentReleaseDAO().findAll(paymentReleaseForm.getBolId()));
        return mapping.findForward("success");
    }
    public ActionForward Delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PaymentReleaseForm paymentReleaseForm = (PaymentReleaseForm) form;
        HttpSession session = request.getSession(true);
        String userName = "";
        User user = new User();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
        }
        PaymentRelease paymentRelease = new PaymentReleaseDAO().findById(paymentReleaseForm.getId());
        new NotesBC().saveNotesWhileAddingPaymentCharges(paymentReleaseForm.getFileNumber(), userName, paymentRelease,"Deleted");
        new PaymentReleaseDAO().delete(paymentRelease);
        Object paidObject = new PaymentReleaseDAO().sumOfPaidCharges(paymentReleaseForm.getBolId());
        Double totalPaidAmount = 0.0;
        if(null != paidObject){
            totalPaidAmount = (Double)paidObject;
            overPaidStatus(paymentReleaseForm, totalPaidAmount);
            request.setAttribute("totalPaidAmount", totalPaidAmount);
        }else{
             overPaidStatus(paymentReleaseForm, totalPaidAmount);
        }
        request.setAttribute("paymentReleaseList", new PaymentReleaseDAO().findAll(paymentReleaseForm.getBolId()));
        return mapping.findForward("success");
    }
   public void overPaidStatus(PaymentReleaseForm paymentReleaseForm, double paidAmount) throws Exception{
        Double collectAmount;
        boolean overPaidResult = false;
        FclBl fclBl = new FclBlDAO().findById(paymentReleaseForm.getBolId());
        FclBlDAO fclBlDAO = new FclBlDAO();
        Object collectObject = new FclBlChargesDAO().sumOfCollectCharges(paymentReleaseForm.getBolId());
        if(collectObject != null){
           collectAmount =  (Double)collectObject;
           overPaidResult = paidAmount > collectAmount?true:false;
        }
        if(overPaidResult){
            paymentReleaseForm.setOverPaid("Over Paid");
        }else{
            paymentReleaseForm.setOverPaid("");
        }
        if(null != fclBl){
            fclBl.setOverPaidStatus(overPaidResult);
            fclBlDAO.update(fclBl);
        }
   }
}

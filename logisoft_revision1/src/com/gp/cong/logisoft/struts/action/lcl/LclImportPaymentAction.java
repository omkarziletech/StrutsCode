/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTa;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTrans;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingAcTaDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclImportPaymentForm;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclImportPaymentAction extends LogiwareDispatchAction implements LclCommonConstant {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        getAllChargesValues(request, lclImportPaymentForm);
        return mapping.findForward("displayCharges");
    }

    public ActionForward deletePayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        LclBookingAcTaDAO lclBookingAcTaDAO = new LclBookingAcTaDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        User loginUser = getCurrentUser(request);
        List<LclBookingAcTa> lclBookingAcTaList = lclBookingAcTaDAO.findByProperty("lclBookingAcTrans.id", lclImportPaymentForm.getBkgTransid());
        StringBuilder deletedNotes = new StringBuilder();
        StringBuilder amountNotes = new StringBuilder();
        deletedNotes.append("Deleted-> ");
        LclBookingAcTa lclBookingAcTa1 = lclBookingAcTaList.get(0);
        deletedNotes.append(" Cheque Number -> ").append(lclBookingAcTa1.getLclBookingAcTrans().getReferenceNo());
        deletedNotes.append(" Cheque Amount -> ").append(lclBookingAcTa1.getLclBookingAcTrans().getAmount());
        if (lclBookingAcTa1.getLclBookingAcTrans().getReferenceName() != null && !"".equalsIgnoreCase(lclBookingAcTa1.getLclBookingAcTrans().getReferenceName())) {
            deletedNotes.append(" Paid By -> ").append(lclBookingAcTa1.getLclBookingAcTrans().getReferenceName().toUpperCase());
        }
        amountNotes.append(" ( ");
        for (LclBookingAcTa lclBookingAcTa : lclBookingAcTaList) {
            amountNotes.append(" Code-> ").append(lclBookingAcTa.getLclBookingAc().getArglMapping().getChargeCode());
            amountNotes.append(" Charge Amount-> ").append(lclBookingAcTa.getAmount());
        }
        amountNotes.append(" )");
        deletedNotes.append(amountNotes);
        if (lclBookingAcTa1.getLclBookingAcTrans().getRemarks() != null && !"".equalsIgnoreCase(lclBookingAcTa1.getLclBookingAcTrans().getRemarks())) {
            deletedNotes.append(" Comments -> ").append(lclBookingAcTa1.getLclBookingAcTrans().getRemarks().toUpperCase());
        }
        lclRemarksDAO.insertLclRemarks(Long.parseLong(lclImportPaymentForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, deletedNotes.toString(), loginUser.getUserId());
        lclBookingAcTaDAO.deleteBkgacTa(lclImportPaymentForm.getBkgTransid());
        LclBookingAcTrans lclBookingAcTrans = lclBookingAcTransDAO.findById(lclImportPaymentForm.getBkgTransid());
        lclBookingAcTransDAO.delete(lclBookingAcTrans);
        getAllChargesValues(request, lclImportPaymentForm);
        return mapping.findForward("displayCharges");
    }

    public ActionForward viewPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        List translist = new LclBookingAcTaDAO().findByTransChargesList(lclImportPaymentForm.getBkgTransid());
        request.setAttribute("translist", translist);
        request.setAttribute("lclImportPaymentForm", lclImportPaymentForm);
        return mapping.findForward("viewPayment");
    }

    public ActionForward savePayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportPaymentForm lclImportPaymentForm = (LclImportPaymentForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        StringBuilder note = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclImportPaymentForm.getAmount())) {
            Date d = new Date();
            LclBookingAcTrans lclBookingAcTrans = new LclBookingAcTrans();
            lclBookingAcTrans.setEntryType("A");
            lclBookingAcTrans.setTransDatetime(DateUtils.parseDate(lclImportPaymentForm.getPaidDate(), "dd-MMM-yyyy"));
            lclBookingAcTrans.setTransType("");
            lclBookingAcTrans.setLclFileNumber(new LclFileNumber(Long.parseLong(lclImportPaymentForm.getFileNumberId())));
            lclBookingAcTrans.setAmount(new BigDecimal(lclImportPaymentForm.getAmount()));
            lclBookingAcTrans.setReferenceName(lclImportPaymentForm.getPaidBy().toUpperCase());
            lclBookingAcTrans.setReferenceNo(lclImportPaymentForm.getCheckNumber().toUpperCase());
            lclBookingAcTrans.setPaymentType(lclImportPaymentForm.getPaymentType());
            if (CommonUtils.isNotEmpty(lclImportPaymentForm.getRemarks())) {
                lclBookingAcTrans.setRemarks(lclImportPaymentForm.getRemarks().toUpperCase());
            } else {
                lclBookingAcTrans.setRemarks("");
            }
            lclBookingAcTrans.setEnteredBy(user);
            lclBookingAcTrans.setEnteredDatetime(d);
            lclBookingAcTrans.setModifiedBy(user);
            lclBookingAcTrans.setModifiedDatetime(d);
            new LCLBookingAcTransDAO().saveOrUpdate(lclBookingAcTrans);
            note.append(" Cheque Number -> ").append(lclBookingAcTrans.getReferenceNo());
            note.append(" Cheque Amount -> ").append(lclBookingAcTrans.getAmount());
            note.append(" Code -> ");
            String bookingacId[] = lclImportPaymentForm.getBookingAcId().split(",");
            String chargeAmt[] = lclImportPaymentForm.getChargesAmount().split(",");
            for (int i = 0; i < bookingacId.length; i++) {
                if (!"0.00".equals(chargeAmt[i])) {
                    LclBookingAc lclBookingAc = new LclCostChargeDAO().findById(Long.parseLong(bookingacId[i]));
                    LclBookingAcTa lclBookingAcTa = new LclBookingAcTa();
                    lclBookingAcTa.setLclBookingAcTrans(lclBookingAcTrans);
                    lclBookingAcTa.setLclBookingAc(lclBookingAc);
                    lclBookingAcTa.setAmount(new BigDecimal(chargeAmt[i]));
                    lclBookingAcTa.setEnteredByUserId(user.getUserId());
                    lclBookingAcTa.setEnteredDatetime(d);
                    lclBookingAcTa.setModifiedByUserId(user.getUserId());
                    lclBookingAcTa.setModifiedDatetime(d);
                    new LclBookingAcTaDAO().saveOrUpdate(lclBookingAcTa);
                    note.append(lclBookingAc.getArglMapping().getChargeCode() + ", ");
                }
            }
            note.append(" Paid Date -> ").append(new SimpleDateFormat("yyyy-MM-dd").format(lclBookingAcTrans.getTransDatetime()));
            note.append(" Payment Type -> ").append(lclBookingAcTrans.getPaymentType());
            lclRemarksDAO.insertLclRemarks(Long.parseLong(lclImportPaymentForm.getFileNumberId()), REMARKS_DR_AUTO_NOTES, note.toString(), user.getUserId());
        }
        lclImportPaymentForm.setAmount("");
        lclImportPaymentForm.setPaidBy("");
        lclImportPaymentForm.setCheckNumber("");
        lclImportPaymentForm.setRemarks("");
        lclImportPaymentForm.setFormChangeFlag(true);
        getAllChargesValues(request, lclImportPaymentForm);
        return mapping.findForward("displayCharges");
    }

    public void getAllChargesValues(HttpServletRequest request, LclImportPaymentForm lclImportPaymentForm) throws Exception {
        String acctNo = "";
        String acctName = "";
        LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(lclImportPaymentForm.getFileNumberId()));
        if (lclBooking.getBillToParty().equalsIgnoreCase("C")) {
            acctNo = lclBooking.getConsAcct() != null ? lclBooking.getConsAcct().getAccountno() : "";
            acctName = lclBooking.getConsAcct() != null ? lclBooking.getConsAcct().getAccountName() : "";
        } else if (lclBooking.getBillToParty().equalsIgnoreCase("N")) {
            acctNo = lclBooking.getNotyAcct() != null ? lclBooking.getNotyAcct().getAccountno() : "";
            acctName = lclBooking.getNotyAcct() != null ? lclBooking.getNotyAcct().getAccountName() : "";
        } else if (lclBooking.getBillToParty().equalsIgnoreCase("T")) {
            acctNo = lclBooking.getThirdPartyAcct() != null ? lclBooking.getThirdPartyAcct().getAccountno() : "";
            acctName = lclBooking.getThirdPartyAcct() != null ? lclBooking.getThirdPartyAcct().getAccountName() : "";
        }
        request.setAttribute("importCreditStatus", CommonUtils.isNotEmpty(acctNo) ? new GenericCodeDAO().getCreditStatusForImports(acctNo) : "");
        request.setAttribute("importCreditAccount", acctName);
        List<LclBookingAcTrans> lclBookingAcTransesList = new LCLBookingAcTransDAO().findByFileNumber(Long.parseLong(lclImportPaymentForm.getFileNumberId()));
        request.setAttribute("lclBookingAcTransesList", lclBookingAcTransesList);
        // String bookingAcId = new LclBookingAcTaDAO().getlclbookingAcid(lclImportPaymentForm.getFileNumberId());
        List<BookingChargesBean> lclBookingAcList = new LclCostChargeDAO().findBybookingAcIdForPayment(lclImportPaymentForm.getFileNumberId());
        request.setAttribute("totalAmt", new LclCostChargeDAO().getTotalAmtByARBillToParty(Long.parseLong(lclImportPaymentForm.getFileNumberId())));
        request.setAttribute("totalBalanceAmt", new LclBookingAcTaDAO().getPaymentBalanceAmt(Long.parseLong(lclImportPaymentForm.getFileNumberId())));
        lclImportPaymentForm.setPaidDate(DateUtils.formatDate(new Date(), "dd-MMM-yyyy"));
        lclImportPaymentForm.setPaymentType(null);
        request.setAttribute("lclImportPaymentForm", lclImportPaymentForm);
        request.setAttribute("lclBookingAcList", lclBookingAcList);
        request.setAttribute("isDisableCheckCopy", new ScanDAO().isScanDocument("LCL IMPORTS DR",
                lclBooking.getLclFileNumber().getFileNumber(), "CHECK COPY"));

    }
}

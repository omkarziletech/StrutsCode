package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineEdiDao;
import com.gp.cong.logisoft.lcl.bc.InvoiceUtils;
import com.gp.cong.logisoft.lcl.bc.LclBookingUtils;
import com.gp.cvst.logisoft.struts.form.lcl.EculineInvoiceForm;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.model.EdiInvoiceCharges;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Rajesh
 */
public class EculineInvoiceAction extends LogiwareDispatchAction {

    private static final String POST_CHARGE = "P";
    private static final String FORWARD_INVOICE = "invoice";
    //Submit all invoices

    public ActionForward postInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        EculineInvoiceForm invoiceForm = (EculineInvoiceForm) form;
//        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
//        InvoiceUtils invoiceUtils = new InvoiceUtils();
//        User user = getCurrentUser(request);
//        List<EdiInvoiceCharges> charges = invoiceDao.getCharges(invoiceForm.getBlNo(), invoiceForm.getInvoiceNo(), null, Long.parseLong(invoiceForm.getContainerId()));
//        invoiceUtils.setBookingAc(user, charges);
//        invoiceUtils.invoiceDetails(invoiceForm.getBlNo(), request, null, null);
//        invoiceUtils.setValues(invoiceForm, request);
        return mapping.findForward(FORWARD_INVOICE);
    }
    //Submit one invoice

    public ActionForward submitInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EculineInvoiceForm invoiceForm = (EculineInvoiceForm) form;
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        User user = getCurrentUser(request);
        invoiceDao.updateCharges(invoiceForm, getCurrentUser(request).getUserId());
        invoiceUtils.mappingChargeDesc(invoiceForm);
        List<EdiInvoiceCharges> charges = invoiceDao.getCharges(invoiceForm.getBlNo(), invoiceForm.getInvoiceNo(), invoiceForm.getChargeId().toString(), invoiceForm.getContainerId(), null);
        invoiceUtils.setBookingAc(user, charges);
        invoiceUtils.invoiceDetails(invoiceForm.getBlNo(), request, invoiceForm.getContainerId(), invoiceForm.getInvoiceNo(), invoiceForm.getFileNumberId());
        invoiceUtils.setValues(invoiceForm, request);
        return mapping.findForward(FORWARD_INVOICE);
    }

    public ActionForward updateCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EculineInvoiceForm invoiceForm = (EculineInvoiceForm) form;
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        invoiceDao.updateCharges(invoiceForm, getCurrentUser(request).getUserId());
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        invoiceUtils.mappingChargeDesc(invoiceForm);
        invoiceUtils.setValues(invoiceForm, request);
        invoiceUtils.invoiceDetails(invoiceForm.getBlNo(), request, invoiceForm.getContainerId(), invoiceForm.getInvoiceNo(), invoiceForm.getFileNumberId());
        return mapping.findForward(FORWARD_INVOICE);
    }

    public ActionForward doDispute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EculineInvoiceForm invoiceForm = (EculineInvoiceForm) form;
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        EdiInvoiceDAO invoiceDao = new EdiInvoiceDAO();
        invoiceDao.updateCharges(invoiceForm, getCurrentUser(request).getUserId());
        invoiceUtils.mappingChargeDesc(invoiceForm);
        invoiceDao.doDispute(invoiceForm, getCurrentUser(request).getUserId());
        invoiceUtils.invoiceDetails(invoiceForm.getBlNo(), request, invoiceForm.getContainerId(), invoiceForm.getInvoiceNo(), invoiceForm.getFileNumberId());
        invoiceUtils.setValues(invoiceForm, request);
        return mapping.findForward(FORWARD_INVOICE);
    }

    public ActionForward validateChargeandCostCode(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EculineInvoiceForm eculineInvoiceForm = (EculineInvoiceForm) form;
        LclBookingUtils lclBookingUtils = new LclBookingUtils();
        String errorMessage = null;
        if (CommonUtils.isNotEmpty(eculineInvoiceForm.getChargeCode())) {
            String data[] = lclBookingUtils.validateForChargeandCost(eculineInvoiceForm.getChargeCode(), LclCommonConstant.LCL_SHIPMENT_TYPE_IMPORT);
            if ("N".equalsIgnoreCase(data[0])) {
                errorMessage = "Charge Code is Not found in GlMapping.";
            }
            if ("N".equalsIgnoreCase(data[1])) {
                errorMessage = "Cost Code is Not found in GlMapping.";
            }
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
        }
        PrintWriter out = response.getWriter();
        out.print(errorMessage);
        return null;
    }

    public ActionForward openNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EculineInvoiceForm eculineInvoiceForm = (EculineInvoiceForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        request.setAttribute("audits", eculineEdiDao.getNotesDetails(eculineInvoiceForm.getInvoiceNo(), "", "", LclCommonConstant.REMARKS_TYPE_AUTO));
        return mapping.findForward("notes");
    }

    public ActionForward openCodeMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EculineInvoiceForm eculineInvoiceForm = (EculineInvoiceForm) form;
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        int totalRows = ediInvoiceDAO.getTotalRowsForCodeMapping(null);
        if (totalRows > 0) {
            int limit = eculineInvoiceForm.getLimit();
            int start = limit * (eculineInvoiceForm.getSelectedPage() - 1);
            if (start < 0) {
                start = 0;
            }
            List<EdiInvoiceCharges> chargesList = ediInvoiceDAO.searchInvoiceMappingCharges(null, start, limit);
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            eculineInvoiceForm.setTotalPages(totalPages);
            eculineInvoiceForm.setTotalRows(totalRows);
            // eculineInvoiceForm.setSelectedRows(eculineInvoiceForm.size());
            request.setAttribute("chargesList", chargesList);
            request.setAttribute("eculineInvoiceForm", eculineInvoiceForm);
        }
        return mapping.findForward("invoiceMapping");
    }

    public ActionForward searchCodeMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EculineInvoiceForm eculineInvoiceForm = (EculineInvoiceForm) form;
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        int totalRows = ediInvoiceDAO.getTotalRowsForCodeMapping(eculineInvoiceForm.getSrcChargeDesc());
        if (totalRows > 0) {
            int limit = eculineInvoiceForm.getLimit();
            int start = limit * (eculineInvoiceForm.getSelectedPage() - 1);
            if (start < 0) {
                start = 0;
            }
            List<EdiInvoiceCharges> chargesList = ediInvoiceDAO.searchInvoiceMappingCharges(eculineInvoiceForm.getSrcChargeDesc(), start, limit);
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            eculineInvoiceForm.setTotalPages(totalPages);
            eculineInvoiceForm.setTotalRows(totalRows);
            request.setAttribute("chargesList", chargesList);
        }
        request.setAttribute("ecuChargeDesc", eculineInvoiceForm.getSrcChargeDesc());
        return mapping.findForward("invoiceMapping");
    }

    public ActionForward resetCodeMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("invoiceMapping");
    }

    public ActionForward updateChargeMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EculineInvoiceForm eculineInvoiceForm = (EculineInvoiceForm) form;
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        if (CommonUtils.isNotEmpty(eculineInvoiceForm.getMappingSaveAllFlag()) && eculineInvoiceForm.getMappingSaveAllFlag().equals("true")) {
            invoiceUtils.mappingAllChargesDescForEculine(eculineInvoiceForm);
        } else {
            invoiceUtils.mappingChargeDesc(eculineInvoiceForm);
        }
        int totalRows = ediInvoiceDAO.getTotalRowsForCodeMapping(eculineInvoiceForm.getSrcChargeDesc());
        if (totalRows > 0) {
            int limit = eculineInvoiceForm.getLimit();
            int start = limit * (eculineInvoiceForm.getSelectedPage() - 1);
            if (start < 0) {
                start = 0;
            }
            List<EdiInvoiceCharges> chargesList = ediInvoiceDAO.searchInvoiceMappingCharges(eculineInvoiceForm.getSrcChargeDesc(), start, limit);
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            eculineInvoiceForm.setTotalPages(totalPages);
            eculineInvoiceForm.setTotalRows(totalRows);
            request.setAttribute("chargesList", chargesList);
        }
        return mapping.findForward("invoiceMapping");
    }
}

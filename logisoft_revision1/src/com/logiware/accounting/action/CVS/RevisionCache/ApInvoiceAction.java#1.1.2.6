package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.accounting.dao.ApInvoiceDAO;
import com.logiware.accounting.form.ApInvoiceForm;
import com.logiware.accounting.model.ApInvoiceModel;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApInvoiceAction extends BaseAction {

    private final static String SEARCH = "search";

    public ActionForward apInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        boolean recurring = apInvoiceForm.isRecurring();
        Integer id = apInvoiceForm.getId();
        if (CommonUtils.isNotEmpty(id)) {
            new ApInvoiceDAO().editInvoice(apInvoiceForm);
        } else {
            apInvoiceForm = new ApInvoiceForm();
            GenericCode credit = new GenericCodeDAO().findByCodeDesc("Due Upon Receipt");
            apInvoiceForm.setCreditTerm(Integer.parseInt(credit.getCode()));
            apInvoiceForm.setCreditDesc(credit.getCodedesc());
            apInvoiceForm.setCreditId(credit.getId());
            apInvoiceForm.setMode(FULL_MODE);
            apInvoiceForm.setRecurring(recurring);
            request.setAttribute("apInvoiceForm", apInvoiceForm);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward gotoSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        boolean recurring = apInvoiceForm.isRecurring();
        apInvoiceForm = new ApInvoiceForm();
        apInvoiceForm.setRecurring(recurring);
        apInvoiceForm.setStatus("AP");
        apInvoiceForm.setSortBy("inv.date");
        apInvoiceForm.setOrderBy("desc");
        apInvoiceForm.setLimit(100);
        request.setAttribute("apInvoiceForm", apInvoiceForm);
        return mapping.findForward(SEARCH);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        new ApInvoiceDAO().search(apInvoiceForm);
        request.setAttribute("apInvoiceForm", apInvoiceForm);
        return mapping.findForward(SEARCH);
    }

    public ActionForward calculateDueDate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        String dueDate = new ApInvoiceDAO().calculateDueDate(apInvoiceForm.getInvoiceDate(), apInvoiceForm.getCreditTerm());
        PrintWriter out = response.getWriter();
        out.print(dueDate);
        return null;
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        new ApInvoiceDAO().saveOrUpdate(apInvoiceForm, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addLineItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return save(mapping, form, request, response);
    }

    public ActionForward removeLineItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        new ApInvoiceDAO().removeLineItem(apInvoiceForm, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        new ApInvoiceDAO().delete(apInvoiceForm.getId());
        return search(mapping, form, request, response);
    }

    public ActionForward reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        boolean result = apInvoiceDAO.validateUploads(apInvoiceForm.getVendorNumber(), apInvoiceForm.getInvoiceNumber());
        PrintWriter out = response.getWriter();
        if (result) {
            apInvoiceDAO.reject(apInvoiceForm, request);
            return apInvoice(mapping, apInvoiceForm, request, response);
        } else {
            out.print("no upload");
            return null;
        }
    }

    public ActionForward unreject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        User user = (User) request.getSession().getAttribute(LOGIN_USER);
        apInvoiceDAO.unreject(apInvoiceForm, user);
        return apInvoice(mapping, apInvoiceForm, request, response);
    }

    public ActionForward post(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        synchronized (this) {
            ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
            ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
            User user = (User) request.getSession().getAttribute(LOGIN_USER);
            if (new ApInvoiceDAO().isOpenInvoice(apInvoiceForm.getVendorNumber(), apInvoiceForm.getInvoiceNumber())) {
                apInvoiceDAO.post(apInvoiceForm, user);
            }
            if (apInvoiceForm.isRecurring()) {
                return gotoSearch(mapping, form, request, response);
            } else {
                ApInvoiceForm newApInvoiceForm = new ApInvoiceForm();
                newApInvoiceForm.setVendorName(apInvoiceForm.getVendorName());
                newApInvoiceForm.setVendorNumber(apInvoiceForm.getVendorNumber());
                newApInvoiceForm.setCreditTerm(apInvoiceForm.getCreditTerm());
                newApInvoiceForm.setCreditDesc(apInvoiceForm.getCreditDesc());
                newApInvoiceForm.setCreditId(apInvoiceForm.getCreditId());
                newApInvoiceForm.setMode(FULL_MODE);
                request.setAttribute("apInvoiceForm", newApInvoiceForm);
                return mapping.findForward(SUCCESS);
            }
        }
    }

    public ActionForward findDuplicates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInvoiceForm apInvoiceForm = (ApInvoiceForm) form;
        List<ApInvoiceModel> duplicates = new ApInvoiceDAO().findDuplicates(apInvoiceForm.getVendorNumber(), apInvoiceForm.getInvoiceNumber());
        PrintWriter out = response.getWriter();
        if (CommonUtils.isNotEmpty(duplicates)) {
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<div>");
            htmlBuilder.append("<div>Potential duplicate").append(duplicates.size() > 1 ? "s" : "").append(" found.</div>");
            htmlBuilder.append("<div style='padding:0 0 0 30px'>");
            htmlBuilder.append("<table class='confirm-box-table'>");
            boolean noExactMatches = true;
            int count = 0;
            for (ApInvoiceModel duplicate : duplicates) {
                if (duplicate.isExactMatch()) {
                    out.print(duplicate.getStatus());
                    noExactMatches = false;
                    break;
                } else {
                    count++;
                    htmlBuilder.append("<tr class='").append(count % 2 == 0 ? "odd" : "even").append("'>");
                    if (duplicates.size() > 1) {
                        htmlBuilder.append("<td class='align-right'>");
                        htmlBuilder.append(count).append(")");
                        htmlBuilder.append("</td>");
                    }
                    htmlBuilder.append("<td>");
                    htmlBuilder.append(StringEscapeUtils.escapeHtml4(duplicate.getInvoiceNumber().toUpperCase()));
                    htmlBuilder.append("</td>");
                    htmlBuilder.append("<td> <--> ");
                    htmlBuilder.append(StringEscapeUtils.escapeHtml4(duplicate.getStatus()));
                    htmlBuilder.append("</td>");
                    htmlBuilder.append("</tr>");
                }
            }
            htmlBuilder.append("</table>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("<div>Do you want to proceed?</div>");
            htmlBuilder.append("</div>");
            if (noExactMatches) {
                out.print(htmlBuilder.toString());
            }
        } else {
            out.print("Available");
        }
        return null;
    }
}

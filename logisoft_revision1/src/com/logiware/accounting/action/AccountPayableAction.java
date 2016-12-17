package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.accounting.dao.AccountPayableDAO;
import com.logiware.accounting.form.AccountPayableForm;
import com.logiware.accounting.form.SessionForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.bean.PaymentBean;
import com.logiware.excel.DiscardedInvoicesExcelCreator;
import com.logiware.hibernate.dao.ArBatchDAO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccountPayableAction extends BaseAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        AccountPayableForm accountPayableForm = (AccountPayableForm) form;
        SessionForm oldAccountPayableForm = new SessionForm();
        PropertyUtils.copyProperties(oldAccountPayableForm, accountPayableForm);
        session.setAttribute("oldAccountPayableForm", oldAccountPayableForm);
        new AccountPayableDAO().search(accountPayableForm, user.getUserId());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward sortingAndPaging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        SessionForm oldAccountPayableForm = (SessionForm) session.getAttribute("oldAccountPayableForm");
        AccountPayableForm accountPayableForm = (AccountPayableForm) form;
        PropertyUtils.copyProperties(accountPayableForm, oldAccountPayableForm);
        accountPayableForm.setSortBy(request.getParameter("sortBy"));
        accountPayableForm.setOrderBy(request.getParameter("orderBy"));
        accountPayableForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
        new AccountPayableDAO().search(accountPayableForm, user.getUserId());
        request.setAttribute("accountPayableForm", accountPayableForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("accountPayableForm", new AccountPayableForm());
        request.getSession().removeAttribute("oldAccountPayableForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearFilters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccountPayableForm accountPayableForm = (AccountPayableForm) form;
        AccountPayableForm newAccountPayableForm = new AccountPayableForm();
        newAccountPayableForm.setVendorName(accountPayableForm.getVendorName());
        newAccountPayableForm.setVendorNumber(accountPayableForm.getVendorNumber());
        newAccountPayableForm.setToggled(accountPayableForm.isToggled());
        request.setAttribute("accountPayableForm", newAccountPayableForm);
        request.getSession().removeAttribute("oldAccountPayableForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccountPayableForm accountPayableForm = (AccountPayableForm) form;
        String payIds = accountPayableForm.getPayIds();
        String holdIds = accountPayableForm.getHoldIds();
        String deleteIds = accountPayableForm.getDeleteIds();
        User user = (User) request.getSession().getAttribute("loginuser");
        new AccountPayableDAO().save(payIds, holdIds, deleteIds, user);
        StringBuilder message = new StringBuilder();
        if (CommonUtils.isNotEmpty(payIds)) {
            int count = StringUtils.split(payIds, ",").length;
            message.append(count).append(count > 1 ? " Invoices are" : " Invoice is").append(" set Ready to Pay");
        }
        if (CommonUtils.isNotEmpty(holdIds)) {
            message.append(CommonUtils.isNotEmpty(message) ? " and " : "");
            int count = StringUtils.split(holdIds, ",").length;
            message.append(count).append(count > 1 ? " Invoices are" : " Invoice is").append(" holded");
        }
        if (CommonUtils.isNotEmpty(deleteIds)) {
            message.append(CommonUtils.isNotEmpty(message) ? " and " : "");
            int count = StringUtils.split(deleteIds, ",").length;
            message.append(count).append(count > 1 ? " Invoices are" : " Invoice is").append(" deleted");
        }
        request.setAttribute("message", message.toString());
        return search(mapping, form, request, response);
    }

    public ActionForward importStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccountPayableForm payableForm = (AccountPayableForm) form;
        InputStream inputStream = null;
        try {
            inputStream = payableForm.getImportfile().getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<PaymentBean> uploadedInvoices = new ArrayList<PaymentBean>();
            List<ResultModel> results = new ArrayList<ResultModel>();
            Map<String, String> queries = new HashMap<String, String>();
            payableForm.setFullSearch(true);
            payableForm.setOnly("");
            AccountPayableDAO payableDAO = new AccountPayableDAO();
            ArBatchDAO batchDAO = new ArBatchDAO();
            DataFormatter formatter = new DataFormatter(); //creating formatter using the default locale
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                String invoiceOrBl = formatter.formatCellValue(row.getCell(0));  //Returns the formatted value of a cell as a String regardless of the cell type.
                Double invoiceAmt = row.getCell(1).getNumericCellValue();
                payableForm.setInvoiceNumber(invoiceOrBl);

                Boolean Openflag = batchDAO.getApInvoiceAmt(payableForm.getVendorNumber(), invoiceOrBl, invoiceAmt);
                if (Openflag != null && Openflag != false) {
                    queries.clear();
                    String apQuery = payableDAO.buildApQuery(payableForm, null);
                    queries.put("apQuery", apQuery);
                    for (ResultModel invoice : payableDAO.getResults(queries, "ap.id", "asc", 0, 100)) {
                        invoice.setPayCheck(true);
                        results.add(invoice);
                    }
                } else {
                    uploadedInvoices.add(batchDAO.getInvoice(payableForm.getVendorNumber(), "AP", invoiceOrBl, invoiceAmt));
                }
            }

            if (results.size() > 0) {
                payableForm.setResults(results);
            }
            if (uploadedInvoices.size() > 0) {
                String fileName = new DiscardedInvoicesExcelCreator().createExcel(payableForm.getVendorNumber(), uploadedInvoices);
                request.setAttribute("fileName", fileName);
            }
            payableForm.setInvoiceNumber(null);
            return mapping.findForward(SUCCESS);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public boolean isStringCellNotEmpty(Cell cell) {
        return null != cell && CommonUtils.isNotEmpty(cell.getStringCellValue());
    }

}

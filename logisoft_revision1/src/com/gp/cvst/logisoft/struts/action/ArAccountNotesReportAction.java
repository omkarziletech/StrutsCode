/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.bc.accounting.ARReportsBC;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cvst.logisoft.struts.form.ArAccountNotesReportForm;
import com.logiware.excel.ArAccountNotesExcelCreator;
import com.logiware.reports.ArAccountNotesReportCreator;
import com.oreilly.servlet.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author logiware
 */
public class ArAccountNotesReportAction extends DispatchAction implements ConstantsInterface {

    private static String AR_ACCOUNT_NOTES_REPORT_FORM = "arAccountNotesReportForm";
    private static String FORWARD_TO_AR_ACCOUNT_NOTES_REPORT = "arAccountNotesReport";

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArAccountNotesReportForm arAccountNotesReportForm = new ArAccountNotesReportForm();
        request.setAttribute(AR_ACCOUNT_NOTES_REPORT_FORM, arAccountNotesReportForm);
        return mapping.findForward(FORWARD_TO_AR_ACCOUNT_NOTES_REPORT);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArAccountNotesReportForm arAccountNotesReportForm = new ArAccountNotesReportForm();
        request.setAttribute(AR_ACCOUNT_NOTES_REPORT_FORM, arAccountNotesReportForm);
        return clear(mapping, form, request, response);
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArAccountNotesReportForm arAccountNotesReportForm = (ArAccountNotesReportForm) form;
        arAccountNotesReportForm.setReportType(AR_ACCOUNT_NOTES_REPORT);
        String realPath = this.getServlet().getServletContext().getRealPath("/");
        request.setAttribute("reportFileName", new ArAccountNotesReportCreator(arAccountNotesReportForm, realPath).createReport());
        return mapping.findForward(FORWARD_TO_AR_ACCOUNT_NOTES_REPORT);
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArAccountNotesReportForm arAccountNotesReportForm = (ArAccountNotesReportForm) form;
        arAccountNotesReportForm.setReportType(AR_ACCOUNT_NOTES_REPORT);
        String excelFileName = new ArAccountNotesExcelCreator(arAccountNotesReportForm).createExcel();
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(FORWARD_TO_AR_ACCOUNT_NOTES_REPORT);
    }
}

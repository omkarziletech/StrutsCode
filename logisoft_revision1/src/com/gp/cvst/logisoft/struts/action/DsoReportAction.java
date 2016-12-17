/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.bc.accounting.ARReportsBC;
import com.gp.cvst.logisoft.struts.form.DsoReportForm;
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
public class DsoReportAction extends DispatchAction implements ConstantsInterface {

    private static String DSO_REPORT_FORM = "dsoReportForm";
    private static String FORWARD_TO_DSO_REPORT = "dsoReport";

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DsoReportForm dsoReportForm = new DsoReportForm();
        dsoReportForm.setReportType(AR_DSO_REPORT);
        request.setAttribute(DSO_REPORT_FORM, dsoReportForm);
        return mapping.findForward(FORWARD_TO_DSO_REPORT);
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DsoReportForm dsoReportForm = new DsoReportForm();
        request.setAttribute(DSO_REPORT_FORM, dsoReportForm);
        return clear(mapping, form, request, response);
    }

    public ActionForward printDSO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        DsoReportForm dsoReportForm = (DsoReportForm) form;
        String realPath = this.getServlet().getServletContext().getRealPath("/");
        dsoReportForm.setExcel(false);
        dsoReportForm.setReportType(AR_DSO_REPORT);
        request.setAttribute("reportFileName", new ARReportsBC().printArReports(dsoReportForm, realPath));
        return mapping.findForward(FORWARD_TO_DSO_REPORT);
    }

    public ActionForward exportDSOToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DsoReportForm dsoReportForm = (DsoReportForm) form;
        dsoReportForm.setExcel(true);
        dsoReportForm.setReportType(AR_DSO_REPORT);
        String excelFileName = new ARReportsBC().exportToExcelApReports(dsoReportForm);
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(FORWARD_TO_DSO_REPORT);
    }

}

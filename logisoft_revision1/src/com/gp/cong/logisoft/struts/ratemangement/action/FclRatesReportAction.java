/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFCLForm;
import com.gp.cong.logisoft.struts.ratemangement.utills.FclRatesReportExcelCreator;
import com.logiware.accounting.action.BaseAction;
import com.oreilly.servlet.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class FclRatesReportAction extends BaseAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchFCLForm searchFCLForm = (SearchFCLForm) form;
        searchFCLForm.setCommodityNumber("006100");
        searchFCLForm.setCommodityName("DEPARTMENT STORE MERCHANDISE");
        request.setAttribute("searchFCLForm", searchFCLForm);
        return mapping.findForward("success");
    }

    public ActionForward downloadXlsReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchFCLForm searchFCLForm = (SearchFCLForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        String excelFileName = new FclRatesReportExcelCreator().createExcel(searchFCLForm, loginUser);
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        request.setAttribute("searchFCLForm", searchFCLForm);
        return mapping.findForward("success");
    }
}

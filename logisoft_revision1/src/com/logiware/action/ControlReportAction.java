package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.logiware.excel.ControlReportExcelCreator;
import com.logiware.form.ControlReportForm;
import com.logiware.hibernate.dao.ControlReportDAO;
import com.logiware.reports.ControlReportCreator;
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
 * @author Lakshminarayanan
 */
public class ControlReportAction extends DispatchAction {

    public ActionForward generateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ControlReportForm controlReportForm = (ControlReportForm) form;
	String createdDate = DateUtils.formatDate(DateUtils.parseDate(controlReportForm.getCreatedDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	ControlReportDAO controlReportDAO = new ControlReportDAO();
	if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCRUALS)) {
	    String blueScreenQuery = controlReportDAO.buildQueryForBlueAccruals(createdDate);
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccruals(blueScreenQuery));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccruals(blueScreenQuery));
	    String logiwareQuery = controlReportDAO.buildQueryForLogiwareAccruals(createdDate);
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccruals(logiwareQuery));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccruals(logiwareQuery));
	    String missingQuery = controlReportDAO.buildQueryForMissingAccruals(createdDate);
	    controlReportForm.setBlueScreenAccruals(controlReportDAO.getBlueScreenAccruals(missingQuery));
	    controlReportForm.setLogiwareAccruals(controlReportDAO.getLogiwareAccruals(missingQuery));
	} else {
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setBlueScreenAccountReceivables(controlReportDAO.getBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setLogiwareAccountReceivables(controlReportDAO.getLogiwareAccountReceivables(createdDate,createdDate));
	}
	return mapping.findForward("success");
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ControlReportForm controlReportForm = (ControlReportForm) form;
	String createdDate = DateUtils.formatDate(DateUtils.parseDate(controlReportForm.getCreatedDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	ControlReportDAO controlReportDAO = new ControlReportDAO();
	if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCRUALS)) {
	    String blueScreenQuery = controlReportDAO.buildQueryForBlueAccruals(createdDate);
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccruals(blueScreenQuery));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccruals(blueScreenQuery));
	    String logiwareQuery = controlReportDAO.buildQueryForLogiwareAccruals(createdDate);
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccruals(logiwareQuery));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccruals(logiwareQuery));
	    String missingQuery = controlReportDAO.buildQueryForMissingAccruals(createdDate);
	    controlReportForm.setBlueScreenAccruals(controlReportDAO.getBlueScreenAccruals(missingQuery));
	    controlReportForm.setLogiwareAccruals(controlReportDAO.getLogiwareAccruals(missingQuery));
	} else {
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setBlueScreenAccountReceivables(controlReportDAO.getBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setLogiwareAccountReceivables(controlReportDAO.getLogiwareAccountReceivables(createdDate,createdDate));
	}
	controlReportForm.setFileName(new ControlReportCreator().createReport(controlReportForm, this.getServlet().getServletContext().getRealPath("/")));
	return mapping.findForward("success");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	ControlReportForm controlReportForm = (ControlReportForm) form;
	String createdDate = DateUtils.formatDate(DateUtils.parseDate(controlReportForm.getCreatedDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
	ControlReportDAO controlReportDAO = new ControlReportDAO();
	if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCRUALS)) {
	    String blueScreenQuery = controlReportDAO.buildQueryForBlueAccruals(createdDate);
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccruals(blueScreenQuery));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccruals(blueScreenQuery));
	    String logiwareQuery = controlReportDAO.buildQueryForLogiwareAccruals(createdDate);
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccruals(logiwareQuery));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccruals(logiwareQuery));
	    String missingQuery = controlReportDAO.buildQueryForMissingAccruals(createdDate);
	    controlReportForm.setBlueScreenAccruals(controlReportDAO.getBlueScreenAccruals(missingQuery));
	    controlReportForm.setLogiwareAccruals(controlReportDAO.getLogiwareAccruals(missingQuery));
	} else {
	    controlReportForm.setNumberOfBluScreenRecords(controlReportDAO.getNumberOfBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setNumberOfLogiwareRecords(controlReportDAO.getNumberOfLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInBlueScreen(controlReportDAO.getTotalAmountInBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setTotalAmountInLogiware(controlReportDAO.getTotalAmountInLogiwareAccountReceivables(createdDate,createdDate));
	    controlReportForm.setBlueScreenAccountReceivables(controlReportDAO.getBlueScreenAccountReceivables(createdDate,createdDate));
	    controlReportForm.setLogiwareAccountReceivables(controlReportDAO.getLogiwareAccountReceivables(createdDate,createdDate));
	}
	String excelFileName = new ControlReportExcelCreator().exportToExcel(controlReportForm);
	if (CommonUtils.isNotEmpty(excelFileName)) {
	    response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
	    response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
	    ServletUtils.returnFile(excelFileName, response.getOutputStream());
	    return null;
	}
	return mapping.findForward("success");
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("success");
    }
}

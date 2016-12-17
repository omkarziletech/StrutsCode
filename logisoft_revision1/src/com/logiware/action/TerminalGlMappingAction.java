package com.logiware.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.form.TerminalGlMappingForm;
import com.logiware.hibernate.dao.TerminalGlMappingDAO;
import com.logiware.hibernate.domain.TerminalGlMapping;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TerminalGlMappingAction extends DispatchAction {
private static final Logger log = Logger.getLogger(TerminalGlMappingAction.class);
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	try {
	    terminalGlMappingForm.setTerminalGlMappings(new TerminalGlMappingDAO().getTerminalGlMappings(terminalGlMappingForm));
	} catch (Exception e) {
	    terminalGlMappingForm.setMessage("Unknow error occurs while searching");
	}
	return mapping.findForward("success");
    }

    public ActionForward importTerminalGlMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	try {
	    Workbook workbook = Workbook.getWorkbook(terminalGlMappingForm.getTerminalGlMappingSheet().getInputStream());
	    Sheet sheet = workbook.getSheet(0);
	    if (null != sheet && sheet.getRows() >= 2) {
		List<TerminalGlMapping> terminalGlMappings = new ArrayList<TerminalGlMapping>();
		for (int i = 4; i < sheet.getRows(); i++) {
		    Cell[] cells = sheet.getRow(i);
		    if (null != cells && CommonUtils.isNotEmpty(cells[0].getContents())) {
			Integer terminal = Integer.parseInt(cells[0].getContents());
			boolean save = false;
			TerminalGlMapping terminalGlMapping = new TerminalGlMappingDAO().findById(terminal);
			if (null == terminalGlMapping) {
			    terminalGlMapping = new TerminalGlMapping();
			    terminalGlMapping.setTerminal(terminal);
			    save = true;
			}
			terminalGlMapping.setLclExportBilling(cells.length > 1 && CommonUtils.isNotEmpty(cells[1].getContents()) ? Integer.parseInt(cells[1].getContents()) : null);
			terminalGlMapping.setLclExportLoading(cells.length > 2 && CommonUtils.isNotEmpty(cells[2].getContents()) ? Integer.parseInt(cells[2].getContents()) : null);
			terminalGlMapping.setLclExportDockreceipt(cells.length > 3 && CommonUtils.isNotEmpty(cells[3].getContents()) ? Integer.parseInt(cells[3].getContents()) : null);
			terminalGlMapping.setFclExportBilling(cells.length > 4 && CommonUtils.isNotEmpty(cells[4].getContents()) ? Integer.parseInt(cells[4].getContents()) : null);
			terminalGlMapping.setFclExportLoading(cells.length > 5 && CommonUtils.isNotEmpty(cells[5].getContents()) ? Integer.parseInt(cells[5].getContents()) : null);
			terminalGlMapping.setFclExportDockreceipt(cells.length > 6 && CommonUtils.isNotEmpty(cells[6].getContents()) ? Integer.parseInt(cells[6].getContents()) : null);
			terminalGlMapping.setAirExportBilling(cells.length > 7 && CommonUtils.isNotEmpty(cells[7].getContents()) ? Integer.parseInt(cells[7].getContents()) : null);
			terminalGlMapping.setAirExportLoading(cells.length > 8 && CommonUtils.isNotEmpty(cells[8].getContents()) ? Integer.parseInt(cells[8].getContents()) : null);
			terminalGlMapping.setAirExportDockreceipt(cells.length > 9 && CommonUtils.isNotEmpty(cells[9].getContents()) ? Integer.parseInt(cells[9].getContents()) : null);
			terminalGlMapping.setLclImportBilling(cells.length > 10 && CommonUtils.isNotEmpty(cells[10].getContents()) ? Integer.parseInt(cells[10].getContents()) : null);
			terminalGlMapping.setFclImportBilling(cells.length > 11 && CommonUtils.isNotEmpty(cells[11].getContents()) ? Integer.parseInt(cells[11].getContents()) : null);
			terminalGlMapping.setInlandExportLoading(cells.length > 12 && CommonUtils.isNotEmpty(cells[12].getContents()) ? Integer.parseInt(cells[12].getContents()) : null);
			if (save) {
			    terminalGlMapping.setCreatedDate(new Date());
			    terminalGlMapping.setCreatedBy(loginUser.getLoginName());
			    new TerminalGlMappingDAO().save(terminalGlMapping);
			} else {
			    terminalGlMapping.setUpdatedDate(new Date());
			    terminalGlMapping.setUpdatedBy(loginUser.getLoginName());
			    new TerminalGlMappingDAO().update(terminalGlMapping);
			}
		    }
		}
		terminalGlMappingForm.setTerminalGlMappings(terminalGlMappings);
		terminalGlMappingForm.setMessage("Terminal Mappings imported sucessfully");
	    } else {
		terminalGlMappingForm.setMessage("Import Terminal Mappings failed - format not correct");
	    }
	    terminalGlMappingForm.setTerminalGlMappings(new TerminalGlMappingDAO().getTerminalGlMappings(terminalGlMappingForm));
	} catch (Exception e) {
            log.info("Import Terminal Mappings failed" + new Date(),e);
	    terminalGlMappingForm.setMessage("Import Terminal Mappings failed");
	}
	return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
	User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	TerminalGlMapping terminalGlMapping = terminalGlMappingForm.getTerminalGlMapping();
	try {
	    TerminalGlMapping terminalGlMappingDomain = new TerminalGlMappingDAO().findById(terminalGlMappingForm.getTerminalGlMapping().getTerminal());
	    if (null != terminalGlMappingDomain) {
		terminalGlMappingForm.setMessage("Terminal Mapping save failed.Terminal already stored in data base");
	    } else {
		terminalGlMapping.setUpdatedDate(new Date());
		terminalGlMapping.setUpdatedBy(loginUser.getLoginName());
		new TerminalGlMappingDAO().save(terminalGlMapping);
		terminalGlMappingForm.setMessage("Terminal Mapping saved successfully");
	    }
	    terminalGlMappingForm.setTerminalGlMappings(new TerminalGlMappingDAO().getTerminalGlMappings(terminalGlMappingForm));
	} catch (Exception e) {
	    log.info("Terminal Mapping save failed" + new Date(),e);
	    terminalGlMappingForm.setMessage("Terminal Mapping save failed");
	}
	return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	User loginUser = (User) request.getSession().getAttribute(CommonConstants.LOGIN_USER);
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	TerminalGlMapping terminalGlMapping = terminalGlMappingForm.getTerminalGlMapping();
	try {
	    terminalGlMapping.setUpdatedDate(new Date());
	    terminalGlMapping.setUpdatedBy(loginUser.getLoginName());
	    new TerminalGlMappingDAO().update(terminalGlMapping);
	    terminalGlMappingForm.setMessage("Terminal Mapping updated successfully");
	    terminalGlMappingForm.setTerminalGlMappings(new TerminalGlMappingDAO().getTerminalGlMappings(terminalGlMappingForm));
	} catch (Exception e) {
	    terminalGlMappingForm.setMessage("Terminal Mapping update failed");
            throw e;
	}
	return mapping.findForward("success");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	terminalGlMappingForm.setTerminalGlMapping(new TerminalGlMapping());
	return mapping.findForward("success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	terminalGlMappingForm.setTerminalGlMapping(new TerminalGlMappingDAO().findById(terminalGlMappingForm.getTerminal()));
	return mapping.findForward("success");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	TerminalGlMappingForm terminalGlMappingForm = (TerminalGlMappingForm) form;
	TerminalGlMapping terminalGlMapping = new TerminalGlMappingDAO().findById(terminalGlMappingForm.getTerminal());
	new TerminalGlMappingDAO().delete(terminalGlMapping);
	terminalGlMappingForm.setTerminalGlMappings(new TerminalGlMappingDAO().getTerminalGlMappings(terminalGlMappingForm));
	return mapping.findForward("success");
    }

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	return mapping.findForward("success");
    }
}

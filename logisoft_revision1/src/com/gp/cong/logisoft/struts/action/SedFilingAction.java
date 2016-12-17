package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.SedFilingBC;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.SedSchedulebDetails;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.struts.form.SedFilingForm;
import com.gp.cong.logisoft.util.EdiUtil;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import org.apache.commons.beanutils.PropertyUtils;

public class SedFilingAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        SedFilingForm sedFilingForm = (SedFilingForm) form;
        SedSchedulebDetails sedSchedulebDetails = new SedSchedulebDetails();
        SedFilingsDAO sedFilingsDAO = new SedFilingsDAO();
        SedSchedulebDetailsDAO sedSchedulebDetailsDAO = new SedSchedulebDetailsDAO();
        EdiUtil ediUtil = new EdiUtil();
        SedFilings sedFilings = new SedFilings();
        String userName = "";
        String email = "";
        User user = new User();
        request.setAttribute("exportCodeList", new SedFilingBC().getFieldList(69, "Export Code"));
        request.setAttribute("licenseCodeList", new SedFilingBC().getFieldList(67, "License Code"));
        request.setAttribute("stateCodeList", new SedFilingBC().getFieldList(70, "Vehicle State"));
        request.setAttribute("status", sedFilingForm.getStatus());
        request.setAttribute("shpdr", sedFilingForm.getShpdr());
        request.setAttribute("schedId", sedFilingForm.getSchedId());
        if (sedFilingForm.getBkgnum() != null && !sedFilingForm.getBkgnum().trim().equals("")) {
            request.setAttribute("bkgNu", sedFilingForm.getBkgnum().toUpperCase());
        }
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userName = user.getLoginName();
            email = user.getEmail();
        }
        String buttonValue = sedFilingForm.getButtonValue();
        if (CommonUtils.isEmpty(buttonValue)) {
            buttonValue = request.getParameter("buttonValue");
        }
        request.setAttribute("buttonValue", buttonValue);
        if (CommonUtils.isNotEmpty(request.getParameter("mode"))) {
            request.setAttribute("mode", request.getParameter("mode"));
        }
        if (CommonUtils.isNotEmpty(buttonValue)) {
            if (buttonValue.trim().equals("submit") || buttonValue.trim().equals("update")) {
                    if ("update".equals(buttonValue)) {
                        sedSchedulebDetails = sedSchedulebDetailsDAO.findById(Integer.parseInt(sedFilingForm.getSchedId()));
                    }
                    PropertyUtils.copyProperties(sedSchedulebDetails, sedFilingForm);
                    sedSchedulebDetails.setScheduleBName(sedFilingForm.getScheduleB_Name());
                    sedSchedulebDetails.setScheduleBNumber(sedFilingForm.getScheduleB_Number());
                    sedSchedulebDetails.setShipment(sedFilingForm.getShpdr());
                    sedSchedulebDetails.setEntnam(userName);
                    sedSchedulebDetails.setEntrdt(new Date());
                    sedSchedulebDetailsDAO.save(sedSchedulebDetails);
                    editMethod(sedFilingForm.getTrnref(), request);
                return mapping.findForward("schedPage");
                //editMethod(sedFilingForm.getShpdr(), request);
            } else if (buttonValue.trim().equals("showSched")) {
                editMethod(sedFilingForm.getTrnref(), request);
            } else if (buttonValue.trim().equals("goToSedFiling")) {
                    String bol = request.getParameter("bol");
                    if (CommonUtils.isNotEmpty(bol)) {
                        FclBl fclbl = new FclBlDAO().findById(null != bol ? Integer.parseInt(bol) : 0);
                        SedFilingForm sedForm = new SedFilingForm(fclbl);
                        int count = new SedFilingsDAO().getSedCount(fclbl.getFileNo());
                        count = count + 1;
                        String refNum = "04" + fclbl.getFileNo() + "-0" + count;
                        sedForm.setTrnref(refNum);
//                        sedForm.setBkgnum(refNum);
                        sedForm.setEmail(email);
                        sedForm.setStsusr(userName);
                        sedForm.setStsDate(DateUtils.formatStringDateToAppFormatMMM(new Date()));
                        request.setAttribute("sedFilingForm", sedForm);
                        request.setAttribute("status", sedForm.getStatus());
                    } else {
                        request.setAttribute("sedFilingForm", new SedFilingForm());
                    }
                return mapping.findForward("goToSedFiling");
            } else if ("editSedFiling".equalsIgnoreCase(buttonValue)) {
                    String trnref = request.getParameter("trnref");
                    sedFilings = new SedFilingsDAO().findByTrnref(trnref);
                    SedFilingForm sedForm = new SedFilingForm();
                    PropertyUtils.copyProperties(sedForm, sedFilings);
                    sedForm.setDepDate(null != sedFilings.getDepdat() ? DateUtils.parseDateToString(sedFilings.getDepdat()) : null);
                    request.setAttribute("sedFilingForm", sedForm);
                    request.setAttribute("action", "updateAes");
                    request.setAttribute("bkgNu", sedForm.getBkgnum().toUpperCase());
                return mapping.findForward("goToSedFiling");
            } else if (buttonValue.trim().equals("edit")) {
                sedSchedulebDetails = sedSchedulebDetailsDAO.findById(Integer.parseInt(sedFilingForm.getSchedId()));
                request.setAttribute("schedB", sedSchedulebDetails);
                editMethod(sedFilingForm.getTrnref(), request);
            } else if (buttonValue.trim().equals("delete")) {
                sedSchedulebDetails = sedSchedulebDetailsDAO.findById(Integer.parseInt(sedFilingForm.getSchedId()));
                sedSchedulebDetailsDAO.delete(sedSchedulebDetails);
                editMethod(sedFilingForm.getTrnref(), request);
            } else if ("goToSchedB".equals(buttonValue)) {
                editMethod(sedFilingForm.getTrnref(), request);
            } else if ("saveAes".equalsIgnoreCase(buttonValue) || "updateAes".equalsIgnoreCase(buttonValue)) {
                    if ("updateAes".equalsIgnoreCase(buttonValue)) {
                        sedFilings = new SedFilingsDAO().findByTrnref(sedFilingForm.getTrnref());
                    } else {
                        sedFilings = new SedFilings();
                    }
                    PropertyUtils.copyProperties(sedFilings, sedFilingForm);
                    if (null != sedFilingForm.getOrigin() && sedFilingForm.getOrigin().lastIndexOf("(") != -1 && sedFilingForm.getOrigin().lastIndexOf(")") != -1) {
                        sedFilings.setExpctry(sedFilingForm.getOrigin().substring(sedFilingForm.getOrigin().lastIndexOf("(") + 1, sedFilingForm.getOrigin().lastIndexOf("(") + 3));
                    }
                    sedFilings.setEntrdt(new Date());
                    sedFilings.setEntnam(userName);
                    sedFilings.setDepdat(CommonUtils.isNotEmpty(sedFilingForm.getDepDate()) ? DateUtils.parseDate(sedFilingForm.getDepDate(), "MM/dd/yyyy") : null);
                    sedFilings.setStsdte(CommonUtils.isNotEmpty(sedFilingForm.getStsDate()) ? DateUtils.parseDate(sedFilingForm.getStsDate(), "dd-MMM-yyyy") : null);
                    if ("saveAes".equalsIgnoreCase(buttonValue)) {
                        new SedFilingsDAO().save(sedFilings);
                    }
                    request.setAttribute("hideGbShow", "hideGbShow");
                    request.setAttribute("sedFilingForm", new SedFilingForm());
                    return mapping.findForward("goToSedFiling");
            } else if ("submitAes".equals(buttonValue)) {
                new SedFilingBC().createFlatFile(sedFilings);
            }
        }
        return mapping.findForward("schedPage");
    }

    public void editMethod(String trnref, HttpServletRequest request) throws Exception {
        if (null != trnref) {
            request.setAttribute("schedList", new SedSchedulebDetailsDAO().findByTrnref(trnref));
            request.setAttribute("index", request.getParameter("index"));
        }
    }
}

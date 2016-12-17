package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.lcl.comparator.LclVoyageComparator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LclUnitsScheduleAction extends LogiwareDispatchAction {

    private static String ADD_VOYAGE = "addVoyage";
    private LclUtils lclUtils = new LclUtils();

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        setVoyageDetails(request);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm schForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> lclScheduleList = null;
        schForm.setPolId(schForm.getPortOfOriginId());
        schForm.setPodId(schForm.getFinalDestinationId());
        schForm.setPodName(schForm.getDestination());
        schForm.setPolName(schForm.getOrigin());
        if (CommonUtils.isNotEmpty(schForm.getVoyageNo())) {
            lclScheduleList = new ExportUnitQueryUtils().searchVoyageDetails(schForm.getVoyageNo());
            String filterByChange = "";
            if (lclScheduleList.get(0).getServiceType().equals("N")) {
                filterByChange = "lclDomestic";
            }
            if (lclScheduleList.get(0).getServiceType().equals("C")) {
                filterByChange = "lclCfcl";
            }
            if (lclScheduleList.get(0).getServiceType().equals("E")) {
                filterByChange = "lclExport";
            }
            schForm.setFilterByChanges(filterByChange);
            request.setAttribute("lclVoyageList", lclScheduleList);
        } else if (schForm.getFilterByChanges() != null && !schForm.getFilterByChanges().trim().equals("")) {
            if (!"lclDomestic".equalsIgnoreCase(schForm.getFilterByChanges())) {
                lclScheduleList = new ExportUnitQueryUtils().searchByVoyageList(schForm);
            } else {
                lclScheduleList = new ExportUnitQueryUtils().getInandVoyageList(schForm);
                request.setAttribute("lclVoyageList", lclScheduleList);
            }
            request.setAttribute("lclVoyageList", lclScheduleList);
        }
        setVoyageDetails(request);
        request.setAttribute("lclUnitsScheduleForm", schForm);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward deleteLclVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm scheduleForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> lclScheduleList = null;
        if (scheduleForm.getVoyageId() != null && !scheduleForm.getVoyageId().trim().equals("")) {
            LclSsHeaderDAO lclssheaderdao = new LclSsHeaderDAO();
            LclSsHeader lclssheader = lclssheaderdao.findById(Long.parseLong(scheduleForm.getVoyageId()));
            //***** delete operation on Voyage will put Voyag into void Status *
            lclssheader.setStatus("V");
            lclssheaderdao.update(lclssheader);
            scheduleForm.setPolId(scheduleForm.getPortOfOriginId());
            scheduleForm.setPodId(scheduleForm.getFinalDestinationId());
            scheduleForm.setPodName(scheduleForm.getDestination());
            scheduleForm.setPolName(scheduleForm.getOrigin());
            if ("lclDomestic".equalsIgnoreCase(scheduleForm.getFilterByChanges())) {
                lclScheduleList = new ExportUnitQueryUtils().getInandVoyageList(scheduleForm);
            } else {
                lclScheduleList = new ExportUnitQueryUtils().searchByVoyageList(scheduleForm);
            }
            request.setAttribute("lclVoyageList", lclScheduleList);
            setVoyageDetails(request);
        }
        request.setAttribute("lclUnitsScheduleForm", scheduleForm);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward addVoyage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        LclUnitsScheduleForm scheduleForm = (LclUnitsScheduleForm) form;
        request.setAttribute("pooId", scheduleForm.getPortOfOriginId());
        request.setAttribute("fdId", scheduleForm.getFinalDestinationId());
        request.setAttribute("polId", scheduleForm.getPolId());
        request.setAttribute("podId", scheduleForm.getPodId());
        request.setAttribute("originalOriginName", scheduleForm.getOrigin());
        request.setAttribute("originValue", scheduleForm.getOrigin());
        request.setAttribute("originalDestinationName", scheduleForm.getDestination());
        request.setAttribute("destinationValue", scheduleForm.getDestination());
        request.setAttribute("cfclAcctName", scheduleForm.getCfclAcctName());
        request.setAttribute("cfclAcctNo", scheduleForm.getCfclAcctNo());
        request.setAttribute("serviceType", scheduleForm.getServiceType());
        if (!"lclDomestic".equalsIgnoreCase(scheduleForm.getFilterByChanges())
                && CommonUtils.isNotEmpty(scheduleForm.getPolId())
                && CommonUtils.isNotEmpty(scheduleForm.getPodId())) {
            lclUtils.setRelayTTDBD(request, scheduleForm.getPolId(), scheduleForm.getPodId());
        }
        // line added stopOff function included in Inland  voyage
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setStopOffList(null);

        request.setAttribute("openPopup", "openPopupOnly");
        LclAddVoyageForm lclAddVoyageForm = new LclAddVoyageForm();
        lclAddVoyageForm.setFilterByChanges(scheduleForm.getFilterByChanges());
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("lclUnitsScheduleForm", scheduleForm);
        return mapping.findForward(ADD_VOYAGE);
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm scheduleForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> lclScheduleList = null;
        scheduleForm.setPolId(scheduleForm.getPortOfOriginId());
        scheduleForm.setPodId(scheduleForm.getFinalDestinationId());
        scheduleForm.setPodName(scheduleForm.getDestination());
        scheduleForm.setPolName(scheduleForm.getOrigin());
        if ("Y".equalsIgnoreCase(scheduleForm.getUnitVoyageSearch()) && !scheduleForm.isShowUnCompleteUnits()) {
            String transMode = "V";
            if ("lclDomestic".equalsIgnoreCase(scheduleForm.getFilterByChanges())) {
                transMode = "T";
            }
            List<ExportVoyageSearchModel> viewAllVoyageList = new ExportUnitQueryUtils().getViewAllList(scheduleForm.getPortOfOriginId(),
                    scheduleForm.getFinalDestinationId(), scheduleForm.getServiceType(), transMode);
            request.setAttribute("viewAllVoyageList", viewAllVoyageList);
        } else if (scheduleForm.isShowUnCompleteUnits()) {
            List<ExportVoyageSearchModel> uncompleteunitsList = new ExportUnitQueryUtils().searchByUnCompleteUnit(
                    scheduleForm.getPortOfOriginId(), scheduleForm.getFinalDestinationId(), scheduleForm.getServiceType());
            request.setAttribute("uncompleteunitsList", uncompleteunitsList);
        } else if ("currentProcess".equalsIgnoreCase(scheduleForm.getFilterByChanges())) {
            List<ExportVoyageSearchModel> voyageList = new ExportUnitQueryUtils().getVoyageSearch(scheduleForm);
            Collections.sort(voyageList, new LclVoyageComparator());
            request.setAttribute("voyageList", voyageList);
        } else if ("lclDomestic".equalsIgnoreCase(scheduleForm.getFilterByChanges())) {
            lclScheduleList = new ExportUnitQueryUtils().getInandVoyageList(scheduleForm);
            request.setAttribute("lclVoyageList", lclScheduleList);
        } else {
            lclScheduleList = new ExportUnitQueryUtils().searchByVoyageList(scheduleForm);
            request.setAttribute("lclVoyageList", lclScheduleList);
        }
        request.setAttribute("goBackVoyNo", request.getParameter("goBackVoyNo"));
        setVoyageDetails(request);
        request.setAttribute("lclUnitsScheduleForm", scheduleForm);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward searchByViewAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm schForm = (LclUnitsScheduleForm) form;
        String transMode = "V";
        if ("lclDomestic".equalsIgnoreCase(schForm.getFilterByChanges())) {
            transMode = "T";
        }
        schForm.setPolName(schForm.getOrigin());
        schForm.setPodName(schForm.getDestination());
        List<ExportVoyageSearchModel> viewAllVoyageList = new ExportUnitQueryUtils().getViewAllList(schForm.getPortOfOriginId(),
                schForm.getFinalDestinationId(), schForm.getServiceType(), transMode);

        setVoyageDetails(request);
        request.setAttribute("viewAllVoyageList", viewAllVoyageList);
        request.setAttribute("lclUnitsScheduleForm", schForm);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward searchByUnAssignUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm scheduleForm = (LclUnitsScheduleForm) form;
        // only for delete Option in Listed Unit
        String unitId = request.getParameter("unitId");
        if (CommonUtils.isNotEmpty(unitId)) {
            LclUnitDAO lclunitDAO = new LclUnitDAO();
            LclUnit lclunit = new LclUnitDAO().findById(Long.parseLong(unitId));
            lclunitDAO.delete(lclunit);
        }
        if (CommonUtils.isNotEmpty(scheduleForm.getWarehouseId())) {
            List<ExportVoyageSearchModel> unassignedContainerList = new ExportUnitQueryUtils().searchByUnAssignUnit(scheduleForm.getWarehouseId());
            request.setAttribute("unassignedContainerList", unassignedContainerList);
        }
        setVoyageDetails(request);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward searchByUnCompleteUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm scheduleForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> uncompleteunitsList = new ExportUnitQueryUtils().searchByUnCompleteUnit(
                scheduleForm.getPortOfOriginId(), scheduleForm.getFinalDestinationId(), scheduleForm.getServiceType());
        request.setAttribute("uncompleteunitsList", uncompleteunitsList);
        setVoyageDetails(request);
        request.setAttribute("lclUnitsScheduleForm", scheduleForm);
        return mapping.findForward("displaySchedule");
    }

    public ActionForward voyageSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm unitsScheduleForm = (LclUnitsScheduleForm) form;
        User loginUser = getCurrentUser(request);
        if (CommonUtils.isEmpty(unitsScheduleForm.getPortOfOriginId()) && null != loginUser.getTerminal()) {
            String unlocationCode = new RefTerminalDAO().getReferenceLocation(loginUser.getTerminal().getTrmnum());
            if (CommonUtils.isNotEmpty(unlocationCode)) {
                UnLocation unLocation = new UnLocationDAO().getUnlocation(unlocationCode);
                unitsScheduleForm.setPortOfOriginId(unLocation.getId());
                unitsScheduleForm.setOrigin(new LclFileNumberDAO().getConcatedUnlocationById(unLocation.getId()));
            }
        }
        if (CommonUtils.isNotEmpty(unitsScheduleForm.getPortOfOriginId())) {
            List<ExportVoyageSearchModel> voyageList = new ExportUnitQueryUtils().getVoyageSearch(unitsScheduleForm);
            if (unitsScheduleForm.getFilterByChanges().equalsIgnoreCase("currentProcess")) {
                Collections.sort(voyageList, new LclVoyageComparator());
            }
            unitsScheduleForm.setPolName(unitsScheduleForm.getOrigin());
            unitsScheduleForm.setPodName(unitsScheduleForm.getDestination());
            request.setAttribute("voyageList", voyageList);
        }
        setVoyageDetails(request);
        request.setAttribute("lclUnitsScheduleForm", unitsScheduleForm);
        request.setAttribute("goBackVoyNo", request.getParameter("goBackVoyNo"));
        return mapping.findForward("displaySchedule");
    }

    public ActionForward searchMutiUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> unitList = new ExportUnitQueryUtils().getMultiUnitSearchList(lclUnitsScheduleForm);
        request.setAttribute("unitList", unitList);
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        return mapping.findForward("multiUnitPopUp");
    }

    public ActionForward searchMutiSSLBooking(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        List<ExportVoyageSearchModel> bookingList = new ExportUnitQueryUtils().getMultiBookingSearchList(lclUnitsScheduleForm);
        request.setAttribute("bookingList", bookingList);
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        return mapping.findForward("multiSSLBookingPopUp");
    }

    public void setVoyageDetails(HttpServletRequest request) throws Exception {
        RoleDutyDAO dutyDAO = new RoleDutyDAO();
        Role role = getCurrentUser(request).getRole();
        if (null != role) {
            request.setAttribute("expdeletevoyage", dutyDAO.getRoleDetails("exp_delete_voyage", role.getRoleId()));
            request.setAttribute("showUnCompleteUnits", dutyDAO.getRoleDetails("show_unComplete_units", role.getRoleId()));
        }
    }

    public ActionForward viewReleaseEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String origin = request.getParameter("origin");
        String unLocName = new UnLocationDAO().getUnNameCity(origin);
        request.setAttribute("unlocation", "All ReleaseReport - " + unLocName);
        request.setAttribute("origin", origin);
        return mapping.findForward("sendReport");
    }

    public ActionForward sendReleaseReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm unitsScheduleForm = (LclUnitsScheduleForm) form;
        String originId = unitsScheduleForm.getOrigin();
        String unLocName = new UnLocationDAO().getUnNameCity(originId);
        String fileName = new SendReleaseReportXls().createExcel(originId, unLocName);
        User user = getCurrentUser(request);
        EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
        if (unitsScheduleForm.isEmailMe()) {
            unitsScheduleForm.setToEmailAddress(user.getEmail());
            mailTransaction.setCcAddress("");
            mailTransaction.setBccAddress("");
        } else {
            mailTransaction.setCcAddress(unitsScheduleForm.getCcEmailAddress() != null ? unitsScheduleForm.getCcEmailAddress() : "");
            mailTransaction.setBccAddress(unitsScheduleForm.getBccEmailAddress() != null ? unitsScheduleForm.getBccEmailAddress() : "");
        }
        String companyName = LoadLogisoftProperties.getProperty("application.email.companyName");
        String subject = CommonUtils.isNotEmpty(unitsScheduleForm.getEmailSubject()) ? unitsScheduleForm.getEmailSubject().toUpperCase() : "";
        String emailMessage = CommonUtils.isNotEmpty(unitsScheduleForm.getEmailMessage()) ? unitsScheduleForm.getEmailMessage().toUpperCase() : "";
        String message = this.getEmailBody(user, companyName, emailMessage, request);
        mailTransaction.setFileLocation(fileName);
        mailTransaction.setToAddress(unitsScheduleForm.getToEmailAddress());
        mailTransaction.setFromAddress(user.getEmail());
        mailTransaction.setModuleId("Release Report");
        mailTransaction.setUserName(user.getFirstName());
        mailTransaction.setName("LCL ReleaseReport");
        mailTransaction.setFileLocation(fileName);
        mailTransaction.setType("Email");
        mailTransaction.setStatus("Pending");
        mailTransaction.setNoOfTries(0);
        mailTransaction.setEmailDate(new Date());
        mailTransaction.setToAddress(unitsScheduleForm.getToEmailAddress());
        mailTransaction.setFromAddress(user.getEmail());
        mailTransaction.setSubject(subject);
        mailTransaction.setHtmlMessage(message);
        mailTransaction.setTextMessage(message);
        mailTransaction.setModuleName("LCL Export Voyage");
        mailTransaction.setModuleId(unLocName);
        mailTransaction.setUserName(user.getFirstName());
        new EmailschedulerDAO().save(mailTransaction);
        request.setAttribute("origin", originId);
        return mapping.findForward("displaySchedule");
    }

    public String getEmailBody(User user, String company, String emailMessage, HttpServletRequest req) throws Exception {
        StringBuilder emailBody = new StringBuilder();
        String imagePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String companyLogo = imagePath + LoadLogisoftProperties.getProperty("application.image.econo.logo");
        String companyWebsite = LoadLogisoftProperties.getProperty(company.equalsIgnoreCase("ECONOCARIBE") ? "application.Econo.website"
                : "application.OTI.website");
        emailBody.append("<HTML><BODY>");
        emailBody.append("<div style='font-family: sans-serif;'>");
        emailBody.append("<font color='red'><b>").append(emailMessage).append("<b></font><br>");
        emailBody.append("<b>Please DO NOT reply to this message, see note 3 below.<b><br>");
        emailBody.append("<a href='http://").append(companyWebsite).append("' target='_blank'><img src='");
        emailBody.append(companyLogo).append("'></a>");
        emailBody.append("<br>");
        emailBody.append("<p></p>");
        emailBody.append("<b>To Name:</b>").append("").append("<br>");
        emailBody.append("<b>To Company:</b>").append("").append("<p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>From Name:</b>").append(user.getFirstName()).append("<br>");
        emailBody.append("<b>From Fax #:</b>").append(user.getFax()).append("<br>");
        emailBody.append("<b>From Phone #:</b>").append(user.getTelephone()).append("<p></p>");
        emailBody.append("<pre>");
        emailBody.append("</pre><p></p>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<br>");
        emailBody.append("<b>Did you know?</b><br>");
        emailBody.append("NEED LCL TRANS-ATLANTIC/PACIFIC SERVICES?  WE CAN ASSIST WITH YOUR IMPORT AND<br>");
        emailBody.append("EXPORT NEEDS TO AND FROM ASIA, EUROPE, THE MED, MIDDLE EAST AND AFRICA.<br>");
        emailBody.append("<br>");
        if (company.equalsIgnoreCase("ECONOCARIBE")) {
            emailBody.append("CALL 1-866-ECONO IT  OR BOOK ON LINE AT <a href='http://WWW.ECONOCARIBE.COM' target='_blank'>WWW.ECONOCARIBE.COM</a><br>");
            emailBody.append("<p></p>");
            emailBody.append("<a href='http://www.inttra.com/econocaribe/shipping-instructions?msc=ecbkem' target='_blank'><img src'http://www.econocaribe.com/media/mail/inttra_ad.jpg'></a><p></p>");
        } else {
            emailBody.append("CALL 1-866-ECONO IT  OR BOOK ON LINE AT <a href='http://WWW.OTICARGO.COM' target='_blank'>WWW.OTICARGO.COM</a><br>");
            emailBody.append("<p></p>");
            emailBody.append("<a href='http://www.inttra.com/econocaribe/shipping-instructions?msc=ecbkem' target='_blank'><img src'http://www.econocaribe.com/media/mail/inttra_ad.jpg'></a><p></p>");
        }
        emailBody.append("<b>Helpful Information:</b><br>");
        emailBody.append("1. Open the attached PDF image with Adobe Acrobat Reader. This software can<br>");
        if (company.equalsIgnoreCase("ECONOCARIBE")) {
            emailBody.append("be downloaded for free, just visit <a href='http://www.econocaribe.com' target='_blank'>www.econocaribe.com</a>.<br>");
        } else {
            emailBody.append("be downloaded for free, just visit <a href='http://www.oticargo.com' target='_blank'>www.oticargo.com</a>.<br>");
        }
        emailBody.append("2. The attached image may contain multiple pages.<br>");
        emailBody.append("3. Please do not reply to this email, it is sent from an automated<br>");
        emailBody.append("system, there will be no response from this address. For assistance contact<br>");
        emailBody.append("your sales representative or your local office at (866) 326-6648.<br>");
        emailBody.append("</b></b>");
        emailBody.append("</div>");
        emailBody.append("</BODY>");
        emailBody.append("</HTML>");
        return emailBody.toString();
    }
}

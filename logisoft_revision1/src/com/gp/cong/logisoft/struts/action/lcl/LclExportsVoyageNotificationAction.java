/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.gp.cvst.logisoft.struts.form.lcl.LclExportNotiFicationForm;
import com.logiware.common.utils.ExportNotificationUtil;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author aravindhan.v
 */
public class LclExportsVoyageNotificationAction extends LogiwareDispatchAction implements LclReportConstants {

    private String STATUS = "preview";
    LclAddVoyageForm lclAddVoyageForm = null;

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        String forwardMessage = null;
        if (CommonUtils.isNotEmpty(exportNotiFicationForm.getButtonValue())) {
            if (exportNotiFicationForm.getButtonValue().equalsIgnoreCase("notificationPopUp")) {
                request.setAttribute("reasonCodeList", new LclExportsVoyageNotificationDAO().reasonCodesorVoyageNotification("EXPORT VOYAGE REASON CODES"));
                forwardMessage = "notificationPopUp";
            }
        } else {
            forwardMessage = "voyageNotification";
        }
        return mapping.findForward(forwardMessage);
    }

    public ActionForward sendNotificationReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        LclExportsVoyageNotificationDAO lclExportsVoyageNotificationDAO = new LclExportsVoyageNotificationDAO();
        ExportNotificationUtil exportNotificationUtil = new ExportNotificationUtil();
        if (CommonUtils.isNotEmpty(exportNotiFicationForm.getHeaderId())) {
            String fileNumbersIds;
            if (CommonUtils.isNotEmpty(exportNotiFicationForm.getUnitId())) {
                fileNumbersIds = new LclExportsVoyageNotificationDAO().getFileNumberByHeaderIdUnitId(exportNotiFicationForm.getHeaderId(), exportNotiFicationForm.getUnitId());
            } else {
                fileNumbersIds = new LclExportsVoyageNotificationDAO().getFileNumbersByHeaderId(exportNotiFicationForm.getHeaderId());
            }
            Long unitId = CommonUtils.isNotEmpty(exportNotiFicationForm.getUnitId()) ? exportNotiFicationForm.getUnitId() : null;
            String remarks = exportNotiFicationForm.getRemarks() != null ? exportNotiFicationForm.getRemarks() : "";
            String title = exportNotiFicationForm.getVoyageReason() != null ? exportNotiFicationForm.getVoyageReason() : "";
            Integer reasonCode = exportNotiFicationForm.getVoyageReasonId() != null ? Integer.parseInt(exportNotiFicationForm.getVoyageReasonId()) : null;
            Long notificationId = new LclExportsVoyageNotificationDAO().saveLclExportVoyageNotification(exportNotiFicationForm.getHeaderId(), null, true, true, true, true, true, true, true, remarks, reasonCode, title, STATUS, getCurrentUser(request));
            exportNotificationUtil.sendVoyageNotificationCodeI(exportNotiFicationForm.getHeaderId(), unitId, "E1", "F1", notificationId);
            String outPutFileName = exportNotificationUtil.voyageNotificationReport(fileNumbersIds, notificationId);
            if (CommonUtils.isNotEmpty(outPutFileName)) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                FileInputStream fileStream = new FileInputStream(outPutFileName);
                byte[] buffer = new byte[1024];
                int read = -1;
                while ((read = fileStream.read(buffer)) > 0) {
                    byteStream.write(buffer, 0, read);
                }
                lclExportsVoyageNotificationDAO.saveExportGeneralNotification(exportNotiFicationForm.getHeaderId(), unitId, remarks, title, byteStream.toByteArray(), getCurrentUser(request).getUserId(), getCurrentUser(request).getUserId());
            }
        }
        request.setAttribute("exportNotiFicationForm", exportNotiFicationForm);
        return mapping.findForward("voyageNotification");
    }

    public ActionForward sendDistributionList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        ExportNotificationUtil exportNotificationUtil = new ExportNotificationUtil();
        if (CommonUtils.isNotEmpty(exportNotiFicationForm.getHeaderId())) {
            String fileNumbersIds;
            if (CommonUtils.isNotEmpty(exportNotiFicationForm.getUnitId())) {
                fileNumbersIds = new LclExportsVoyageNotificationDAO()
                        .getFileNumberByHeaderIdUnitId(exportNotiFicationForm.getHeaderId(), exportNotiFicationForm.getUnitId());
            } else {
                fileNumbersIds = new LclExportsVoyageNotificationDAO()
                        .getFileNumbersByHeaderId(exportNotiFicationForm.getHeaderId());
            }
            String message = exportNotiFicationForm.getRemarks() != null ? exportNotiFicationForm.getRemarks() : "";
            String title = exportNotiFicationForm.getVoyageReason() != null ? exportNotiFicationForm.getVoyageReason() : "";
            Long notificationId = new LclExportsVoyageNotificationDAO()
                    .saveLclExportVoyageNotification(exportNotiFicationForm.getHeaderId(),
                            null, true, true, true, true, true, true, true, message, null,
                            title, STATUS, getCurrentUser(request));
            String outPutFileName = exportNotificationUtil.voyageNotificationReport(fileNumbersIds, notificationId);
            String voyageRemarks = new LclExportsVoyageNotificationDAO().subjectforVoyageNotification(notificationId);
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            String companyName = companyCode.equalsIgnoreCase("03") ? "ECU Worldwide" : "OTI";
            LclSsHeader header = new LclSsHeaderDAO().findById(exportNotiFicationForm.getHeaderId());
            String subject = companyName + " Voyage Change Notification  (" + header.getScheduleNo() + ")";
            String fromAddress[] = new LclExportsVoyageNotificationDAO().fromEmailAtRetadd(header.getOrigin().getUnLocationCode(),
                    header.getDestination().getUnLocationCode(), "L");
            exportNotificationUtil.sendEmailFax(fromAddress[1], fromAddress[0], getCurrentUser(request).getEmail(), subject,
                    outPutFileName, EMAIL_STATUS_PENDING, "", "Automated Voyage Notification  Detail Changed As " + voyageRemarks,
                    "Voyage Detail Change", "Email", LclReportConstants.LCL_EXPORTUNITS,
                    exportNotiFicationForm.getVoyageNo(), "system", notificationId);
            request.setAttribute("confirmMessage", "The Distribution List Send Sucessfully");
            request.setAttribute("reasonCodeList", new LclExportsVoyageNotificationDAO()
                    .reasonCodesorVoyageNotification("EXPORT VOYAGE REASON CODES"));
        }
        return mapping.findForward("notificationPopUp");
    }

    public ActionForward displayNotificationList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        List<LclExportNotiFicationForm> notificationList = new LclExportsVoyageNotificationDAO().getExportGeneralNotificationList(exportNotiFicationForm.getHeaderId(), exportNotiFicationForm.getUnitId());
        request.setAttribute("notificationList", notificationList);
        request.setAttribute("exportNotiFicationForm", exportNotiFicationForm);
        return mapping.findForward("voyageNotification");
    }

    public ActionForward deleteNotification(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        Long notificationId = exportNotiFicationForm.getNotificationId() != 0 ? exportNotiFicationForm.getNotificationId() : 0;
        new LclExportsVoyageNotificationDAO().deleteNotificationId(notificationId);
        displayNotificationList(mapping, form, request, response);
        request.setAttribute("exportNotiFicationForm", exportNotiFicationForm);
        return mapping.findForward("voyageNotification");
    }

    public ActionForward viewNotificationReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclExportNotiFicationForm exportNotiFicationForm = (LclExportNotiFicationForm) form;
        request.setAttribute("exportNotiFicationForm", exportNotiFicationForm);
        byte[] content = new LclExportsVoyageNotificationDAO().viewNotificationReport(exportNotiFicationForm.getNotificationId());
        String filePath = LoadLogisoftProperties.getProperty("reportLocation");
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath + "/voyage.pdf");
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(content);
            bos.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print(file.getAbsolutePath());
        return null;
    }
}

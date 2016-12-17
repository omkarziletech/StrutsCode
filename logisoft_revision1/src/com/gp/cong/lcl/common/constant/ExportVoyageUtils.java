/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.LclModel;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRelayDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsExportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.report.LclBLPdfCreator;
import com.gp.cong.logisoft.lcl.report.LclPdfCreator;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.logiware.common.utils.RegexUtil;
import com.logiware.lcl.dao.ExportNotificationDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mei
 */
public class ExportVoyageUtils implements LclCommonConstant, ConstantsInterface {

    public void setVoyageRequestVal(HttpServletRequest request,
            LclSsHeader lclssheader, LclAddVoyageForm lclAddVoyageForm, User loginUser) throws Exception {
        LclUtils lclUtils = new LclUtils();
        String origin = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getOrigin());
        String destination = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getDestination());
        request.setAttribute("serviceType", lclssheader.getServiceType());
        lclAddVoyageForm.setSchServiceType(lclssheader.getServiceType());
        request.setAttribute("originalOriginName", origin);
        request.setAttribute("originalDestinationName", destination);
        request.setAttribute("pooId", lclssheader.getOrigin().getId());
        request.setAttribute("fdId", lclssheader.getDestination().getId());
        request.setAttribute("polId", lclssheader.getOrigin().getId());
        request.setAttribute("podId", lclssheader.getDestination().getId());
        request.setAttribute("originValue", origin);
        request.setAttribute("destinationValue", destination);
        if (!"lclDomestic".equalsIgnoreCase(lclAddVoyageForm.getFilterByChanges())) {
            LclBookingPlanBean lclBookingPlanBean = new LclBookingPlanDAO().getRelay(lclssheader.getOrigin().getId(),
                    lclssheader.getDestination().getId(), "All");
            // lclUtils.setRelayTTDBD(request, lclAddVoyageForm.getOriginId(), lclAddVoyageForm.getFinalDestinationId());
            if (lclBookingPlanBean != null) {
                if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPol_transit_time())) {
                    request.setAttribute("polPodTT", lclBookingPlanBean.getPol_transit_time());
                }
                if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPol_co_dbd())) {
                    request.setAttribute("co_dbd", lclBookingPlanBean.getPol_co_dbd());
                }
//                if (null != lclBookingPlanBean.getPol_co_tod()) {
//                    String[] times=lclBookingPlanBean.getPol_co_tod().toString().split(" ");
//                    request.setAttribute("co_tod", times[1]);
//                }
            }
        }

        LclSSMasterBlDAO sSMasterBlDAO = new LclSSMasterBlDAO();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        if (lclssheader.getHazmatPermitted()) {
            lclssheader.setDisplayhazmatPermitted("Yes");
        } else {
            lclssheader.setDisplayhazmatPermitted("No");
        }
        if (lclssheader.getRefrigerationPermitted()) {
            lclssheader.setDisplayrefrigerationPermitted("Yes");
        } else {
            lclssheader.setDisplayrefrigerationPermitted("No");
        }
        request.setAttribute("lclUnitSSList", formatUnitSSList(lclssheader, documentStoreLogDAO));
        LclSsDetailDAO lclssdetaildao = new LclSsDetailDAO();
        List<LclSsDetail> lclssDetailList = lclssdetaildao.findByProperty("lclSsHeader", lclssheader);
        if (lclAddVoyageForm.getFilterByChanges().equalsIgnoreCase("lclDomestic") && lclssDetailList.size() > 1) {
            List ssDetailList = new ArrayList();
            for (int i = lclssDetailList.size(); i > 0; i--) {
                ssDetailList.add(lclssDetailList.get(i - 1));
            }
            if (null != lclssDetailList.get(0).getSpAcctNo()) {
                request.setAttribute("carrierSpAccountno", lclssDetailList.get(0).getSpAcctNo().getAccountno());
            }
            request.setAttribute("lclssdetailsList", ssDetailList);
        } else {
            request.setAttribute("lclssdetailsList", lclssDetailList);
            if (lclssDetailList.size() == 1 || lclssDetailList.size() > 1) {
                if (null != lclssDetailList.get(0).getSpAcctNo()) {
                    request.setAttribute("carrierSpAccountno", lclssDetailList.get(0).getSpAcctNo().getAccountno());
                }
            }    
        }
        List<LclSSMasterBl> lclSSMasterDetailsList = sSMasterBlDAO.findByProperty("lclSsHeader", lclssheader);
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getMethodName()) && !lclSSMasterDetailsList.isEmpty()
                && !lclssDetailList.isEmpty()) {
            for (int i = 0; i < lclSSMasterDetailsList.size(); i++) {
                LclSSMasterBl ssMasterBl = lclSSMasterDetailsList.get(i);
                if (null != lclssDetailList.get(0).getSpAcctNo()) {
                    String ediCode = new GeneralInformationDAO().getEdiCode(lclssDetailList.get(0).getSpAcctNo().getAccountno());
                    if (!"".equalsIgnoreCase(ediCode)) {
                        String statusSendEdi;
                        String ediCompanyName = "N".equalsIgnoreCase(ediCode) ? "" : ("I".equalsIgnoreCase(ediCode) ? "INTRA" : "GTNEXUS");
                        lclSSMasterDetailsList.get(i).setEdi(ediCompanyName);
                        String LclSSMasterBl_id = (lclSSMasterDetailsList.get(i).getId()).toString();
                        if (LclSSMasterBl_id != null) {
                            String ackStatus = logFileEdiDAO.find304Status(ssMasterBl.getSpBookingNo(), ssMasterBl.getLclSsHeader().getScheduleNo());
                            String status = logFileEdiDAO.findAckStatus(ssMasterBl.getSpBookingNo(), ssMasterBl.getLclSsHeader().getScheduleNo());
                            if (ackStatus == null ? "success" == null : ackStatus.equals("success")) {
                                statusSendEdi = "green";
                            } else if (ackStatus == null ? "failure" == null : ackStatus.equals("failure")) {
                                statusSendEdi = "red";
                            } else if (status == null ? "success" == null : status.equals("success")) {
                                statusSendEdi = "yellow";
                            } else {
                                statusSendEdi = "";
                            }
                            lclSSMasterDetailsList.get(i).setStatusSendEdi(statusSendEdi);
                        }
                    }
                    lclSSMasterDetailsList.get(i).setScanAttach(documentStoreLogDAO.isScanCountChecked("LCL SS MASTER BL", ssMasterBl.getSpBookingNo() + '-' + ssMasterBl.getId()));
                    lclSSMasterDetailsList.get(i).setMasterBlApproveStatus(documentStoreLogDAO.getScanStatus("LCL SS MASTER BL", ssMasterBl.getSpBookingNo() + '-' + ssMasterBl.getId(), "SS LINE MASTER BL"));
                }
            }
        }
        request.setAttribute("lclSSMasterDetailsList", lclSSMasterDetailsList);
        if (lclssheader.getLclSsExports() == null) {
            lclssheader.setLclSsExports(new LclSsExportsDAO().getByProperty("lclSsHeader.id", lclssheader.getId()));
        }
        request.setAttribute("lclssheader", lclssheader);
        setRoleDuty(request, loginUser, lclAddVoyageForm);
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
    }

    public List<LclUnitSs> formatUnitSSList(LclSsHeader lclssheader, DocumentStoreLogDAO documentStoreLogDAO) throws Exception {
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        List<LclUnitSs> lclUnitSsListForDisplay = lclunitssdao.findByProperty("lclSsHeader", lclssheader);
        if (lclssheader.getOrigin() != null && lclssheader.getOrigin().getId() != null && lclssheader.getOrigin().getId() > 0
                && lclssheader.getDestination() != null && lclssheader.getDestination().getId() != null
                && lclssheader.getDestination().getId() > 0) {
            LclRelayDAO lclRelayDAO = new LclRelayDAO();
            Object lclRelayObj[] = lclRelayDAO.getRelayTTCOW(lclssheader.getOrigin().getId(), lclssheader.getDestination().getId());
            if (lclRelayObj != null && lclRelayObj.length > 0) {
                if (lclRelayObj[0] != null && !lclRelayObj[0].toString().trim().equals("")) {
                    lclssheader.setTotalTransitTime(Integer.parseInt(lclRelayObj[0].toString()));
                }
                if (lclRelayObj[1] != null && !lclRelayObj[1].toString().trim().equals("")) {
                    lclssheader.setCutOffDays(Integer.parseInt(lclRelayObj[1].toString()));
                }
            }
        }
        LclSsAcDAO lclSsAcDAO = new LclSsAcDAO();
        if (lclUnitSsListForDisplay != null && lclUnitSsListForDisplay.size() > 0) {
            for (int i = 0; i < lclUnitSsListForDisplay.size(); i++) {
                LclUnitSs lclUnitSs = lclUnitSsListForDisplay.get(i);
                lclUnitSs.setIsCheckedRates(lclSsAcDAO.isCheckedRates(lclUnitSs.getLclSsHeader().getId(), lclUnitSs.getId(), false));
                if (lclUnitSs.getStatus() != null) {
                    lclUnitSs.setDisplayStatus(lclUnitSs.getStatus());
                }
                if (null != lclUnitSs.getId()) {
                    lclUnitSs.setHazStatus(new LclUnitDAO().getHazmat(lclUnitSs.getId()));
                }
                if (null != lclUnitSs.getId()) {
                    lclUnitSs.setInBondStatus(new LclUnitDAO().getInbond(lclUnitSs.getId()));
                }
                if (null != lclUnitSs.getLclUnit()) {
                    Integer dispoId = new LclUnitSsDispoDAO().getDispoId(lclUnitSs.getLclUnit().getId());
                    if (null != dispoId && CommonUtils.isNotEmpty(dispoId)) {
                        DispositionDAO dispDAO = new DispositionDAO();
                        Disposition disp = dispDAO.getByProperty("id", dispoId);
                        if (disp != null && disp.getEliteCode() != null) {
                            lclUnitSs.setDispoCode(disp.getEliteCode());
                            lclUnitSs.setDispoDesc(disp.getDescription());
                        }
                    }
                }
                if (null != lclUnitSs.getLclUnit()) {
                    Object loadByUserId = new LclUnitWhseDAO().getLoadbyName(lclUnitSs.getLclUnit().getId(), lclssheader.getScheduleNo());
                    if (null != loadByUserId) {
                        User loadBy = new UserDAO().findById(Integer.parseInt(loadByUserId.toString()));
                        lclUnitSs.setLoadedByUserId(loadBy);
                    }
                }
                if (null != lclUnitSs.getId()) {
                    lclUnitSs.setNumDR(new LclUnitDAO().getNumDRCount(lclUnitSs.getId()));
                }
                if (null != lclUnitSs.getId()) {
                    List commList = new LclUnitDAO().getCommodityValues(lclUnitSs.getId());
                    if (!commList.isEmpty()) {
                        Object[] obj = (Object[]) commList.get(0);
                        if (obj[0] != null && !obj[0].toString().trim().equals("")) {
                            lclUnitSs.setCFT(obj[0].toString());
                        }
                        if (obj[1] != null && !obj[1].toString().trim().equals("")) {
                            lclUnitSs.setCBM(obj[1].toString());
                        }
                        if (obj[2] != null && !obj[2].toString().trim().equals("")) {
                            lclUnitSs.setLBS(obj[2].toString());
                        }
                        if (obj[3] != null && !obj[3].toString().trim().equals("")) {
                            lclUnitSs.setKGS(obj[3].toString());
                        }
                    }
                }
                if (lclUnitSs.getLclUnit().getHazmatPermitted()) {
                    lclUnitSs.getLclUnit().setDisplayhazmatPermitted("Yes");
                } else {
                    lclUnitSs.getLclUnit().setDisplayhazmatPermitted("No");
                }
                if (lclUnitSs.getLclUnit().getRefrigerated()) {
                    lclUnitSs.getLclUnit().setDisplayrefrigerationPermitted("Yes");
                } else {
                    lclUnitSs.getLclUnit().setDisplayrefrigerationPermitted("No");
                }
                lclUnitSs.setScanAttachStatus(documentStoreLogDAO.isScanCountChecked(LCL_UNITSEXP_SCREENNAME, lclUnitSs.getLclUnit().getUnitNo() + '-' + lclUnitSs.getId()));
                String statusSendEdi;
                String bookingNo = lclUnitSs.getSpBookingNo();
                Long fileNo = null;
                LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
                List lclSsMasterList = lclSsHeader.getLclSsMasterBlList();
                for (int j = 0; j < lclSsMasterList.size(); j++) {
                    LclSSMasterBl lclSsMasterBl = (LclSSMasterBl) lclSsMasterList.get(j);
                    if (lclSsMasterBl.getSpBookingNo() == null ? bookingNo == null : lclSsMasterBl.getSpBookingNo().equals(bookingNo)) {
                        fileNo = lclSsMasterBl.getId();
                    }
                }
                if (fileNo != null) {
                    String ackStatus = logFileEdiDAO.findAckStatus(fileNo.toString());
                    String status = logFileEdiDAO.findStatusDrNumber(fileNo.toString());
                    if (ackStatus == null ? "success" == null : ackStatus.equals("success")) {
                        statusSendEdi = "green";
                    } else if (ackStatus == null ? "failure" == null : ackStatus.equals("failure")) {
                        statusSendEdi = "red";
                    } else if (status == null ? "success" == null : status.equals("success")) {
                        statusSendEdi = "yellow";
                    } else {
                        statusSendEdi = "";
                    }
                    lclUnitSs.setStatusSendEdi(statusSendEdi);
                }
                //********************Code for checking Exception contained by unitss Dr
                LclUnitDAO lclUnitDAO = new LclUnitDAO();
                List<ManifestBean> manifestBeanList = lclUnitDAO.getAllDRSForViewUnitExceptionByUnitSSId(lclUnitSs.getId(), "false");
                if (manifestBeanList.isEmpty()) {
                    lclUnitSs.setUnitExceptionFlag("false");
                } else {
                    lclUnitSs.setUnitExceptionFlag("true");
                }
                //**************************************************************
            }
        }
        return lclUnitSsListForDisplay;
    }

    public void setRoleDuty(HttpServletRequest req, User user, LclAddVoyageForm addVoyageForm) throws Exception {
        String openLCLUnit = "N";
        String unmanifestLclUnit = "N";
        if (user.getRole() != null && CommonUtils.isNotEmpty(user.getRole().getRoleDesc())) {
            RoleDuty roleDuty = new RoleDutyDAO().getByProperty("roleName", user.getRole().getRoleDesc());
            req.setAttribute("roleDuty", roleDuty);
            if (roleDuty != null) {
                if (roleDuty.isOpenLclUnit()) {
                    openLCLUnit = "Y";
                }
                if (roleDuty.isUnmanifestLclUnit()) {
                    unmanifestLclUnit = "Y";
                }
            }
        }
        addVoyageForm.setOpenLCLUnit(openLCLUnit);
        addVoyageForm.setUnmanifestLCLUnit(unmanifestLclUnit);
    }

    public void sendNotification(LclAddVoyageForm addVoyForm, ManifestBean fileId, String reportLocation,
            Date today, String companyName, String realPath, ExportNotificationDAO exportNotificationDAO,
            HttpServletRequest request, String loginName, String emailBody) throws Exception {
        StringBuilder emailAppend = new StringBuilder();
        String bkgReportLocation = "", blReportLocation = "", subject = "";
        String bkgContactEmail = exportNotificationDAO.getBkgContact(fileId.getFileId());
        if (CommonUtils.isNotEmpty(bkgContactEmail)) {
            emailAppend.append(bkgContactEmail).append(",");
        }

//        if (addVoyForm.isInternalEmployees()) {
//            emailAppend.append("voyagenotifications@econocaribe.com").append(",");
//        }
        Long consolidateBlId = null;
        boolean isConsFileId = new LclConsolidateDAO().isConsolidated(fileId.getFileId());
        if (isConsFileId) {
            consolidateBlId = new LCLBlDAO().findConsolidateBl(fileId.getFileId());
            consolidateBlId = null == consolidateBlId ? fileId.getFileId() : consolidateBlId;
        } else {
            consolidateBlId = fileId.getFileId();
        }
        if (addVoyForm.isCustomerEmployee()) {
            if (null != consolidateBlId) {
                String blAcctNo = exportNotificationDAO.getBlAcctNo(consolidateBlId);
                if (CommonUtils.isNotEmpty(blAcctNo)) {
                    System.out.println("blAcctNo==" + blAcctNo);
                    List<String> blAcctNoList = new ArrayList<String>(Arrays.asList(blAcctNo.split(",")));
                    String emailsList = exportNotificationDAO.getCodeIList(blAcctNoList);
                    if (CommonUtils.isNotEmpty(emailsList)) {
                        emailAppend.append(emailsList).append(",");
                    }
                }
            }
        }
        System.out.println("consolidateBlId===" + consolidateBlId + "fileId.getFileId()===" + fileId.getFileId() + "fileNo=" + fileId.getFileNo());
        if (addVoyForm.isBillingTerminal()) {
            String terminalEmail = exportNotificationDAO.getTerminalEmail(fileId.getFileId());
            if (CommonUtils.isNotEmpty(terminalEmail)) {
                emailAppend.append(terminalEmail).append(",");
            }
        }

        if (addVoyForm.isBookingPdf() && CommonUtils.isNotEmpty(emailAppend)) {
            LCLBookingDAO bookingDAO = new LCLBookingDAO();
            File file = new File(reportLocation + "/Documents/LCL/BookingConfirmationWithRate/"
                    + DateUtils.formatDate(today, "yyyy/MM/dd") + "/");
            if (!file.exists()) {
                file.mkdirs();
            }
            LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", fileId.getFileId());
            bookingDAO.getCurrentSession().evict(lclBooking);
            String fileNumber = lclBooking.getLclFileNumber().getFileNumber();
            bkgReportLocation = reportLocation + "/Documents/LCL/BookingConfirmationWithRate/"
                    + DateUtils.formatDate(today, "yyyy/MM/dd") + "/" + "LCL_Booking" + "_" + fileNumber
                    + DateUtils.formatDate(today, "_yyyyMMdd_HHmmss") + ".pdf";
            LclPdfCreator lclPdfCreator = new LclPdfCreator(lclBooking);
            lclPdfCreator.createPdf(realPath, bkgReportLocation, "Booking Confirmation With Rate", request);
            subject = companyName + " " + fileNumber + " DOCK RECEIPT";
            for (String emails : emailAppend.toString().split(",")) {
                String email = "", fax = "";
                if (RegexUtil.isEmail(emails)) {
                    email = emails;
                } else if (RegexUtil.isFax(emails)) {
                    fax = emails;
                }
                if (!"".equalsIgnoreCase(email)) {
                    saveMailTransacations("LclBooking", subject, email, bkgReportLocation, CONTACT_MODE_EMAIL, today, companyName,
                            fileNumber, "LCLBooking", emailBody, "LCL DOCK RECEIPT", loginName);
                } else if (!"".equalsIgnoreCase(fax)) {
                    saveMailTransacations("LclBooking", subject, fax, bkgReportLocation, CONTACT_MODE_FAX, today, companyName,
                            fileNumber, "LCLBooking", emailBody, "LCL DOCK RECEIPT", loginName);
                }
            }
        }
        if (addVoyForm.isNonRatedBl() && null != consolidateBlId && "BL".equalsIgnoreCase(fileId.getFileState())
                && CommonUtils.isNotEmpty(emailAppend)) {
            LclBl lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", consolidateBlId);
            if (lclbl != null) {
                new LCLBlDAO().getCurrentSession().evict(lclbl);
                File file = new File(reportLocation + "/Documents/LCL/UnratedBillOfLading/"
                        + DateUtils.formatDate(today, "yyyy/MM/dd") + "/");
                if (!file.exists()) {
                    file.mkdirs();
                }
                blReportLocation = reportLocation + "/Documents/LCL/UnratedBillOfLading/"
                        + DateUtils.formatDate(today, "yyyy/MM/dd") + "/" + "UnratedBillOfLading_" + fileId.getFileNo()
                        + DateUtils.formatDate(today, "_yyyyMMdd_HHmmss") + ".pdf";
                new LclBLPdfCreator().createReportJob(realPath, blReportLocation, "Bill Of Lading",
                        consolidateBlId.toString(), false);
                subject = companyName + " " + fileId.getFileNo() + " Bill Of Lading UnRated";
                for (String emails : emailAppend.toString().split(",")) {
                    String email = "", fax = "";
                    if (RegexUtil.isEmail(emails)) {
                        email = emails;
                    } else if (RegexUtil.isFax(emails)) {
                        fax = emails;
                    }
                    if (!"".equalsIgnoreCase(email)) {
                        saveMailTransacations("LclBL", subject, email, blReportLocation, CONTACT_MODE_EMAIL, today, companyName,
                                fileId.getFileNo(), "LCLBL", emailBody, "Bill Of Lading UnRated", loginName);
                    } else if (!"".equalsIgnoreCase(fax)) {
                        saveMailTransacations("LclBL", subject, fax, blReportLocation, CONTACT_MODE_FAX, today, companyName,
                                fileId.getFileNo(), "LCLBL", emailBody, "Bill Of Lading UnRated", loginName);
                    }
                }
            }
        }
    }

    public void saveMailTransacations(String screenName, String subject, String toEmail, String outputFileName,
            String emailType, Date today, String companyName, String fileNumber,
            String documentName, String emailBody, String textMsg, String loginName) throws Exception {
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setFromName("");
        email.setFromAddress("");
        email.setToAddress(toEmail);
        email.setSubject(subject);
        email.setFileLocation(outputFileName);
        email.setStatus("Pending");
        email.setTextMessage("Voyage Notification " + textMsg);
        email.setHtmlMessage(emailBody);
        email.setName(screenName);
        email.setType(emailType);
        email.setModuleName(documentName);
        email.setModuleId(fileNumber);
        email.setUserName(loginName);
        email.setEmailDate(today);
        new EmailschedulerDAO().save(email);
    }

    public String[] validatePickFileNo(String voyageNo, String unitNo,
            String fileId, String fileNo, String serviceType) throws Exception {
        String[] errorMsg = new String[3];
        String validateCharge = validateChargesBillingByBl(fileId, fileNo);
        if (CommonUtils.isNotEmpty(validateCharge)) {
            errorMsg[0] = validateCharge;
            errorMsg[1] = "chargeFlag";
        } else {
            LclModel picked = new ExportUnitQueryUtils().getPickedVoyageByFileId(Long.parseLong(fileId), serviceType);
            if (null != picked) {
                if (picked.getScheduleNo().equalsIgnoreCase(voyageNo) && picked.getUnitNo().equalsIgnoreCase(unitNo)) {
                    errorMsg[0] = "BL# " + fileNo + " is already picked but not yet Manifested.Do you want to Manifest?";
                } else {
                    errorMsg[0] = "BL# " + fileNo + " is already picked in the Voyage# " + picked.getScheduleNo() + " and Unit# " + picked.getUnitNo()
                            + ". Do you want to Pick BL in this Unit?";
                }
            } else {
                errorMsg[0] = "Do you want to Pick BL in this Unit?";
            }
        }
        return errorMsg;
    }

    public String validateChargesBillingByBl(String fileId, String fileNo) throws Exception {
        StringBuilder errorMsg = new StringBuilder();
        List<LclModel> chargeList = new ExportUnitQueryUtils().getBlChargesValidate(Long.parseLong(fileId));
        if (chargeList != null && !chargeList.isEmpty()) {
            for (LclModel charge : chargeList) {
                String partyLabel = "A".equalsIgnoreCase(charge.getBillToParty()) ? " Agent"
                        : "S".equalsIgnoreCase(charge.getBillToParty()) ? " Shipper"
                                : "T".equalsIgnoreCase(charge.getBillToParty()) ? " Third Party"
                                        : "F".equalsIgnoreCase(charge.getBillToParty()) ? " Forwarder" : "";
                errorMsg.append("File# ").append(fileNo).append(partyLabel).append(" is required in BL. <br>");
            }
        }
        return errorMsg.toString();
    }
}

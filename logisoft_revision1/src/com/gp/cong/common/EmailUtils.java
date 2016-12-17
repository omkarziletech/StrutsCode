package com.gp.cong.common;

import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclPrintUtil;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.job.JobScheduler;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;

/**
 *
 * @author meiyazhakan.r
 */
public class EmailUtils implements ConstantsInterface {

    public Boolean isEmailorFaxChecked(String email) throws Exception {
        String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return null == email ? false : email.trim().matches(emailreg);
    }

    public void sendDispositionEmail(List<ImportsManifestBean> emailList, String columnName, LclUnitSsDAO lclUnitSsDAO) throws Exception {
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        Set<String> emailSet;
        Set<ImportsManifestBean> emailSets;
        Map<Long, Set<ImportsManifestBean>> emailMap = new LinkedHashMap();
        for (ImportsManifestBean manifest : emailList) {
            String[] emailArray = manifest.getEmail().split(",");
            emailSets = new HashSet<ImportsManifestBean>();
            emailSet = new HashSet<String>();
            emailSet.addAll(Arrays.asList(emailArray));
            if (!emailSet.isEmpty()) {
                manifest.setEmail(emailSet.toString().replace("[", "").replace("]", ""));
            }
            emailSets.add(manifest);
            emailMap.put(manifest.getFileId(), emailSets);
        }
        for (Map.Entry<Long, Set<ImportsManifestBean>> entry : emailMap.entrySet()) {
            String emailId = "";
            emailSets = emailMap.get(entry.getKey());
            for (ImportsManifestBean emails : emailSets) {
                if (CommonUtils.isAtLeastOneNotEmpty(emails.getAgentAcctType(), emails.getDestinationCountry())) {
                    emailId = emails.getEmail();
                    LclPrintUtil lclPrintUtil = new LclPrintUtil();
                    ServletContext servletContext = JobScheduler.servletContext;
                    String contextPath = servletContext.getRealPath("/");
                    String fileLocation = lclPrintUtil.createImportBkgReport("", String.valueOf(entry.getKey()), emails.getFileNo(), LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS, contextPath, emailId, null);
                    String[] splitEmails = emailId.split(",");
                    for (String splitEmail : splitEmails) {
                        if (CommonUtils.isNotEmpty(splitEmail)) {
                            EmailSchedulerVO email = new EmailSchedulerVO();
                            if (CommonUtils.isNotEmpty(emails.getAgentAcctType())) {
                                email.setFromName(emails.getAgentName());
                                email.setFromAddress(emails.getAgentAcctType());
                            } else {
                                email.setFromName(emails.getDestinationName());
                                email.setFromAddress(emails.getDestinationCountry());
                            }
                            email.setToAddress(splitEmail.trim());
                            email.setFileLocation(fileLocation);
                            email.setName("Disposition Change");
                            String headingName = lclUnitSsDispoDAO.getPdfDocumentName(emails.getUnitId().toString(), null);
                            String companyMnemonicCode = null != LoadLogisoftProperties.getProperty("application.email.companyName") ? LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase() : "";
                            email.setSubject(companyMnemonicCode + " " + headingName + " " + emails.getFileNo());
                            Boolean emailFlag = isEmailorFaxChecked(splitEmail.trim());
                            if (emailFlag) {
                                email.setType(CONTACT_MODE_EMAIL);
                            } else {
                                email.setType(CONTACT_MODE_FAX);
                                email.setFromName(LclCommonConstant.NOTIFICATION_FROM_NAME);
                            }
                            email.setStatus(EMAIL_STATUS_PENDING);
                            email.setEmailDate(new Date());
                            email.setTextMessage(LclCommonConstant.NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + headingName.toUpperCase());
                            email.setModuleName(LclReportConstants.SCREENNAMELCLIMPORTBOOKINGREPORT);
                            email.setUserName(USER_SYSTEM);
                            email.setModuleId(emails.getFileNo());
                            email.setHtmlMessage(LclCommonConstant.NOTIFICATION_TEXT_MESSAGE + "\n" + "LCL " + headingName.toUpperCase());
                            emailschedulerDAO.saveOrUpdate(email);
                        }
                    }
                }
                lclUnitSsDAO.updateSchedulerStatus(columnName, emails.getChargeId(), "Closed");
            }
        }
    }
}

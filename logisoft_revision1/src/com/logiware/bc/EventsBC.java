package com.logiware.bc;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Lakshmi Narayanan
 */
public class EventsBC {

    public void sendManifestEmail(String action, FclBl fclBl, User user, String realPath, HttpServletRequest request, String frieghtInvoiceContacts) throws Exception {
        if (null != fclBl) {
            if ("sendFrieghtInvoice".equalsIgnoreCase(action)) {
                String outputFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
                File file = new File(outputFileName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                MessageResources messageResources = CommonConstants.loadMessageResources();
                List<String> billToList = null;
                List li = new FclBlChargesDAO().getDistinctBillToParty(fclBl.getBol().toString());
                if (fclBl.getManifestRev() != null && !fclBl.getManifestRev().equals("0") && !CommonFunctions.isNotNullOrNotEmpty(li)) {
                    billToList = new ArrayList<String>();
                } else {
                    billToList = new FclBlChargesDAO().getDistinctBillTo(fclBl.getBol().toString());
                }
                for (String billTo : billToList) {
                    if (outputFileName.contains(".pdf") && outputFileName.indexOf("/") != -1) {
                        outputFileName = outputFileName.replace(outputFileName.substring(outputFileName.lastIndexOf("/") + 1), "");
                    }
                    if (CommonUtils.isEqualIgnoreCase(billTo, CommonConstants.FORWARDER)) {
                        outputFileName = outputFileName + PrintReportsConstants.FREIGHT_INVOICE_FORWARDER + "_" + fclBl.getFileNo() + ".pdf";
                        new FclBlBC().createFclBillLadingReport(fclBl.getBolId(), outputFileName, realPath, messageResources, user, PrintReportsConstants.FREIGHT_INVOICE_FORWARDER);
                        this.sendMailOrFaxToBillToParty(fclBl.getForwardAgentNo(), outputFileName, CommonConstants.FORWARDER, fclBl.getFileNo(), user, null, null, null, frieghtInvoiceContacts,fclBl.getImportFlag());
                    } else if (CommonUtils.isEqualIgnoreCase(billTo, CommonConstants.SHIPPER)) {
                        outputFileName = outputFileName + PrintReportsConstants.FREIGHT_INVOICE_SHIPPER + "_" + fclBl.getFileNo() + ".pdf";
                        new FclBlBC().createFclBillLadingReport(fclBl.getBolId(), outputFileName, realPath, messageResources, user, PrintReportsConstants.FREIGHT_INVOICE_SHIPPER);
                        this.sendMailOrFaxToBillToParty(fclBl.getShipperNo(), outputFileName, CommonConstants.SHIPPER, fclBl.getFileNo(), user, null, null, null, frieghtInvoiceContacts,fclBl.getImportFlag());
                    } else if (CommonUtils.isEqualIgnoreCase(billTo, CommonConstants.THIRDPARTY)) {
                        outputFileName = outputFileName + PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY + "_" + fclBl.getFileNo() + ".pdf";
                        new FclBlBC().createFclBillLadingReport(fclBl.getBolId(), outputFileName, realPath, messageResources, user, PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY);
                        this.sendMailOrFaxToBillToParty(fclBl.getBillTrdPrty(), outputFileName, CommonConstants.THIRDPARTY, fclBl.getFileNo(), user, null, null, null, frieghtInvoiceContacts,fclBl.getImportFlag());
                    } else if (CommonUtils.isEqualIgnoreCase(billTo, CommonConstants.AGENT)) {
                        outputFileName = outputFileName + PrintReportsConstants.FREIGHT_INVOICE_AGENT + "_" + fclBl.getFileNo() + ".pdf";
                        new FclBlBC().createFclBillLadingReport(fclBl.getBolId(), outputFileName, realPath, messageResources, user, PrintReportsConstants.FREIGHT_INVOICE_AGENT);
                        this.sendMailOrFaxToBillToParty(fclBl.getAgentNo(), outputFileName, CommonConstants.AGENT, fclBl.getFileNo(), user, null, null, null, frieghtInvoiceContacts,fclBl.getImportFlag());
                    } else if (CommonUtils.isEqualIgnoreCase(billTo, FclBlConstants.CONSIGNEE)) {
                        outputFileName = outputFileName + PrintReportsConstants.FREIGHT_INVOICE_AGENT + "_" + fclBl.getFileNo() + ".pdf";
                        new FclBlBC().createFclBillLadingReport(fclBl.getBolId(), outputFileName, realPath, messageResources, user, PrintReportsConstants.FREIGHT_INVOICE_AGENT);
                        this.sendMailOrFaxToBillToParty(fclBl.getConsigneeNo(), outputFileName, FclBlConstants.CONSIGNEE, fclBl.getFileNo(), user, null, null, null, frieghtInvoiceContacts,fclBl.getImportFlag());
                    }
                }
                new NotesBC().saveNotesWhileTransferCost(fclBl.getFileNo(), user.getLoginName(), "Freight Invoice Auto Send Upon Manifest (Invoice Sent)");
            } else if ("doNotSendFrieghtInvoice".equalsIgnoreCase(action)) {
                new NotesBC().saveNotesWhileTransferCost(fclBl.getFileNo(), user.getLoginName(), "Freight Invoice Auto Send Upon Manifest (Invoice NOT Sent)");
            }
            new FclBlChargesDAO().deleteRecordsFromUpdatedPartyTable(fclBl.getBol());
        }
    }

    public void sendMailOrFaxToBillToParty(String accountNo, String fileLocation, String billToParty, String fileNo, User user, String postedEmail, String postedUser, String fax, String frieghtInvoiceContacts,String importFlag) throws Exception {
        if (null != frieghtInvoiceContacts && CommonUtils.isNotEmpty(frieghtInvoiceContacts)) {
            String[] ids = StringUtils.split(frieghtInvoiceContacts, ",");
            boolean isCodeKAvailable = false;
           
            for (String id : ids) {
                CustomerContact customerContact = new CustomerContactDAO().findById(Integer.parseInt(id));
                 if (!"I".equalsIgnoreCase(importFlag)) {
                    isCodeKAvailable = customerContact.isFclExports();
                }else {
                     isCodeKAvailable = customerContact.isFclImports();
                 }
                if (null != customerContact && accountNo.equalsIgnoreCase(customerContact.getAccountNo()) && null != customerContact.getCodek() && isCodeKAvailable) {
                    this.saveEmailOrFax(customerContact, fileLocation, billToParty, fileNo, user, postedEmail, postedUser, fax,importFlag);
                }
            }
        } else {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(accountNo);
            if (null != tradingPartner && CommonUtils.isNotEmpty(tradingPartner.getCustomerContact())) {
                for (CustomerContact customerContact : (Set<CustomerContact>) tradingPartner.getCustomerContact()) {
                    boolean isCodeKAvailable = false;
                     if (!"I".equalsIgnoreCase(importFlag)) {
                    isCodeKAvailable = customerContact.isFclExports();
                }else {
                     isCodeKAvailable = customerContact.isFclImports();
                 }
                    if (null != customerContact.getCodek() && isCodeKAvailable) {
                        this.saveEmailOrFax(customerContact, fileLocation, billToParty, fileNo, user, postedEmail, postedUser, fax,importFlag);
                    }
                }
            }
        }
    }

    private void saveEmailOrFax(CustomerContact customerContact, String fileLocation, String billToParty, String fileNo, User user, String postedEmail, String postedUser, String postedFax, String importFlag) throws Exception {
        File file = new File(fileLocation);
        if (file.exists()) {
            String subject = null;
            if (billToParty != null && billToParty.indexOf("REMOVE") > -1) {
                subject = "FCL-04-" + fileNo + billToParty.replace("REMOVE", "");
            } else {
                subject = "FCL-04-" + fileNo + " FreightInvoice(" + billToParty + ")";
            }
            if ("I".equalsIgnoreCase(importFlag)) {
                if (CommonUtils.isEqual(customerContact.getCodek().getCode(), "E")) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    if (CommonUtils.isNotEmpty(postedEmail) && CommonUtils.isNotEmpty(postedUser)) {
                        emailSchedulerVO.setEmailData(customerContact.getFirstName(), customerContact.getEmail(), postedUser, postedEmail, null, null, subject, "");
                    } else {
                        emailSchedulerVO.setEmailData(customerContact.getFirstName(), customerContact.getEmail(), user.getFirstName(), user.getEmail(), null, null, subject, "");
                    }
                    emailSchedulerVO.setEmailInfo(CommonConstants.SCREEN_NAME_BL, fileLocation, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), CommonConstants.SCREEN_NAME_BL, fileNo, user.getLoginName());
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    new EmailschedulerDAO().save(emailSchedulerVO);
                } else if (CommonUtils.isEqual(customerContact.getCodek().getCode(), "F")) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    if (CommonUtils.isNotEmpty(postedFax)) {
                        emailSchedulerVO.setToAddress(postedFax);
                    } else {
                        emailSchedulerVO.setToAddress(customerContact.getFax());
                    }
                    emailSchedulerVO.setToName(customerContact.getFirstName());
                    emailSchedulerVO.setModuleName(CommonConstants.SCREEN_NAME_BL);
                    emailSchedulerVO.setName(CommonConstants.SCREEN_NAME_BL);
                    emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_FAX);
                    emailSchedulerVO.setCoverLetter("");
                    emailSchedulerVO.setFileLocation(fileLocation);
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    emailSchedulerVO.setModuleId(fileNo);
                    emailSchedulerVO.setHtmlMessage("");
                    emailSchedulerVO.setSubject(subject);
                    emailSchedulerVO.setUserName(user.getLoginName());
                    emailSchedulerVO.setEmailDate(new Date());
                    new EmailschedulerDAO().save(emailSchedulerVO);
                }
            } else {
               if (CommonUtils.isEqual(customerContact.getCodek().getCode(), "E")) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    if (CommonUtils.isNotEmpty(postedEmail) && CommonUtils.isNotEmpty(postedUser)) {
                        emailSchedulerVO.setEmailData(customerContact.getFirstName(), customerContact.getEmail(), postedUser, postedEmail, null, null, subject, "");
                    } else {
                        emailSchedulerVO.setEmailData(customerContact.getFirstName(), customerContact.getEmail(), user.getFirstName(), user.getEmail(), null, null, subject, "");
                    }
                    emailSchedulerVO.setEmailInfo(CommonConstants.SCREEN_NAME_BL, fileLocation, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), CommonConstants.SCREEN_NAME_BL, fileNo, user.getLoginName());
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    new EmailschedulerDAO().save(emailSchedulerVO);
                } else if (CommonUtils.isEqual(customerContact.getCodek().getCode(), "F")) {
                    EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
                    if (CommonUtils.isNotEmpty(postedFax)) {
                        emailSchedulerVO.setToAddress(postedFax);
                    } else {
                        emailSchedulerVO.setToAddress(customerContact.getFax());
                    }
                    emailSchedulerVO.setToName(customerContact.getFirstName());
                    emailSchedulerVO.setModuleName(CommonConstants.SCREEN_NAME_BL);
                    emailSchedulerVO.setName(CommonConstants.SCREEN_NAME_BL);
                    emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_FAX);
                    emailSchedulerVO.setCoverLetter("");
                    emailSchedulerVO.setFileLocation(fileLocation);
                    emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
                    emailSchedulerVO.setModuleId(fileNo);
                    emailSchedulerVO.setHtmlMessage("");
                    emailSchedulerVO.setSubject(subject);
                    emailSchedulerVO.setUserName(user.getLoginName());
                    emailSchedulerVO.setEmailDate(new Date());
                    new EmailschedulerDAO().save(emailSchedulerVO);
                }
            }
        }
    }
}

package com.logiware.utils;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.util.Date;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArCreditHoldUtils {
    
     public static String getEmailCompanyName() {
        String emailCompanyName = "";
        try {
            emailCompanyName = LoadLogisoftProperties.getProperty("application.email.companyName");
            if (CommonUtils.isNotEmpty(emailCompanyName)) {
                emailCompanyName = emailCompanyName.toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailCompanyName;
    }

    public static void sendEmail(Transaction transaction, User loginUser, boolean creditHold, String imagePath) throws Exception {
        CustomerAccounting accounting = new CustomerAccountingDAO().findByAccountNumber(transaction.getCustNo());
        if (null != accounting && null != accounting.getArcode() && CommonUtils.isNotEmpty(accounting.getArcode().getEmail())) {
            String collector = accounting.getArcode().getLoginName();
            String collectorEmail = accounting.getArcode().getEmail();
            String customerNumber = transaction.getCustNo();
            String blNumber = transaction.getBillLaddingNo();
            String consignee = null != transaction.getConsName() ? transaction.getConsName() : "";
            String shipper = null != transaction.getShipperName() ? transaction.getShipperName() : "";
            String container = null != transaction.getContainerNo() ? transaction.getContainerNo() : "";
            String seal = null != transaction.getSealNo() ? transaction.getSealNo() : "";
            String voyage = null != transaction.getVoyageNo() ? transaction.getVoyageNo() : "";
            String eta = null != transaction.getEta() ? DateUtils.formatDate(transaction.getEta(), "MM/dd/yyyy") : "";
            String steamShipLine = null != transaction.getSteamShipLine() ? transaction.getSteamShipLine() : "";
            String vesselName = null != transaction.getVesselName() ? transaction.getVesselName() : "";
            sendEmail(creditHold, imagePath, collector, collectorEmail, customerNumber, blNumber, shipper, consignee, container, seal, voyage, eta, steamShipLine, vesselName, loginUser);
        }
    }

    public static void sendEmail(boolean creditHold, String imagePath, String collector, String collectorEmail, String customerNumber, String blNumber, String shipper, String consignee,
            String container, String seal, String voyage, String eta, String steamShipLine, String vesselName, User loginUser) throws Exception {
        if (CommonUtils.isNotEmpty(collector) && CommonUtils.isNotEmpty(collectorEmail)) {
            BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();
            String toName = collector;
            String toAddress = collectorEmail;
            String fromName = collector;
            String fromAddress = collectorEmail;
            String ccAddress = "";
            String bccAddress = null;
            StringBuilder subject = new StringBuilder();
            if (creditHold) {
                subject.append("Credit Holds ").append(getEmailCompanyName()).append(" - Do Not ");               
            }
            subject.append("Release Notice bl # ").append(blNumber);
            subject.append(" Consignee: ").append(consignee);
            subject.append(" Shipper: ").append(shipper);
            String htmlMessage = getHtmlMessage(creditHold, imagePath, blNumber, consignee, container, seal, voyage, eta, steamShipLine, vesselName);
            String textMessage = getTextMessage(creditHold, blNumber, consignee, container, seal, voyage, eta, steamShipLine, vesselName);
            EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
            emailSchedulerVO.setEmailData(toName, toAddress, fromName, fromAddress, ccAddress, bccAddress, subject.toString(), htmlMessage);
            String userName = null != loginUser ? loginUser.getLoginName() : null;
            emailSchedulerVO.setEmailInfo("ARCreditHold", null, CommonConstants.CONTACT_MODE_EMAIL, 0, new Date(), "ARCreditHold", customerNumber, userName);
            emailSchedulerVO.setTextMessage(textMessage);
            emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
            baseHibernateDAO.getCurrentSession().save(emailSchedulerVO);
            StringBuilder desc = new StringBuilder("Email sent to '").append(toAddress);
            if (CommonUtils.isNotEmpty(ccAddress)) {
                desc.append("' and '").append(ccAddress);
            }
            desc.append("' with subject '").append(subject).append("' by ");
            if(null!=loginUser){
              desc.append(loginUser.getLoginName());
            }else{
              desc.append("System");
            }
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, customerNumber + "-" + blNumber, "AR Credit Hold", loginUser);
        }
    }

    private static String getHtmlMessage(boolean creditHold, String imagePath, String blNumber, String consignee,
            String container, String seal, String voyage, String eta, String steamShipLine, String vesselName) throws Exception {
        String companyName = new SystemRulesDAO().getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_COMPANY_NAME);
        StringBuilder htmlMessage = new StringBuilder("<html>");
        htmlMessage.append("<body>");
        htmlMessage.append("<p style=\"font-size: 14pt;\">Dear ECI Agent,</p>");
        htmlMessage.append("<p style=\"font-size: 14pt;\">");
        if (creditHold) {
            htmlMessage.append("The above mentioned cargo has been placed on credit hold by ").append(companyName).append(". ");
            htmlMessage.append("As such, please <b><span style=\"color: red;\">DO NOT RELEASE</b></span> to consignee ");
            htmlMessage.append("until you receive written instructions from our offices.");
            htmlMessage.append("</p>");
            htmlMessage.append("<p style=\"font-size: 14pt;\">");
            htmlMessage.append("These instructions are issued based on current account standing in the US and ");
            htmlMessage.append("override any instructions written on the bill of lading. All information ");
            htmlMessage.append("resulting in the hold of this shipment is confidential and should not, under ");
            htmlMessage.append("any circumstance, be conveyed to other parties.  Should the consignee have ");
            htmlMessage.append("any questions regarding this hold, please have them contact their shipper/forwarder.");
            htmlMessage.append("</p>");
        } else {
            htmlMessage.append("This cargo which was previously placed on hold, is now off credit hold.	Cargo can be released according to bl instructions.");
        }
        htmlMessage.append("<p style=\"font-size: 14pt;\">Please acknowledge receipt and compliance of this message.</p>");
        htmlMessage.append("<table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin: 0pt 20px 0 0;\">");
        htmlMessage.append("<tbody>");
        String tdStyle = "padding: 0in 5.4pt;font-weight:bolder;font-size:20px;width:173px;";
        String tdBorder = "border-top:1px solid black;border-left:1px solid black;border-right:1px solid black;";
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">BL#</td>");
        consignee = CommonUtils.isNotEmpty(consignee) ? consignee : "&nbsp;";
        container = CommonUtils.isNotEmpty(container) ? container : "&nbsp;";
        seal = CommonUtils.isNotEmpty(seal) ? seal : "&nbsp;";
        voyage = CommonUtils.isNotEmpty(voyage) ? voyage : "&nbsp;";
        eta = CommonUtils.isNotEmpty(eta) ? eta : "&nbsp;";
        steamShipLine = CommonUtils.isNotEmpty(steamShipLine) ? steamShipLine : "&nbsp;";
        vesselName = CommonUtils.isNotEmpty(vesselName) ? vesselName : "&nbsp;";
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(blNumber).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">Container#</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(container).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">Seal#</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(seal).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">Controlling Voyage</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(voyage).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">ETA</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(eta).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">Steamship Line</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(steamShipLine).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append(tdBorder).append("\">Vessel Name</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append(tdBorder).append("\">").append(vesselName).append("</td></tr>");
        htmlMessage.append("<tr><td style=\"").append(tdStyle).append("border: 1px solid black;\">Consignee</td>");
        htmlMessage.append("<td style=\"").append(tdStyle).append("border: 1px solid black;\">").append(consignee).append("</td></tr>");
        htmlMessage.append("</tbody>");
        htmlMessage.append("</table>");
        htmlMessage.append("<br clear=\"all\">");
        htmlMessage.append("<br clear=\"all\">");
        htmlMessage.append("<p style=\"font-size: 14pt;\">If you have any question, please let us know A.S.A.P. by replying to this message.</p>");
        htmlMessage.append("<p style=\"font-size: 14pt;\">Regards,</p>");
        htmlMessage.append("<p style=\"font-size: 14pt;\">Credit & Collections Dept</p>");
        htmlMessage.append("<p style=\"font-size: 14pt;\"><img src=\"").append(imagePath).append("\" alt=\"econo\" width=\"252\" height=\"64\"></p>");
        htmlMessage.append("</body>");
        htmlMessage.append("</html>");
        return htmlMessage.toString();
    }

    private static String getTextMessage(boolean creditHold, String blNumber, String consignee,
            String container, String seal, String voyage, String eta, String steamShipLine, String vesselName) throws Exception {
        String companyName = new SystemRulesDAO().getSystemRulesByCode(CommonConstants.SYSTEM_RULE_CODE_COMPANY_NAME);
        StringBuilder testMessage = new StringBuilder();
        testMessage.append("Dear ECI agent,\n");
        if (creditHold) {
            testMessage.append("\tThe above mentioned cargo has been placed on credit hold by ").append(companyName).append(". ");
            testMessage.append("As such, please DO NOT RELEASE to consignee until you receive written instructions from our offices.\n");
            testMessage.append("These instructions are issued based on current account standing in the US and ");
            testMessage.append("override any instructions written on the bill of lading. All information ");
            testMessage.append("resulting in the hold of this shipment is confidential and should not, under ");
            testMessage.append("any circumstance, be conveyed to other parties.  Should the consignee have ");
            testMessage.append("any questions regarding this hold, please have them contact their shipper/forwarder.\n");
            testMessage.append("Please acknowledge receipt and compliance of this message.\n");
        } else {
            testMessage.append("\tThis cargo which was previously placed on hold, is now off credit hold. Cargo can be released according to bl instructions.\n");
        }
        testMessage.append("    BL#:                    ").append(blNumber).append("\n");
        testMessage.append("    Container#:             ").append(container).append("\n");
        testMessage.append("    Seal#:                  ").append(seal).append("\n");
        testMessage.append("    Controlling Voyage:     ").append(voyage).append("\n");
        testMessage.append("    ETA:                    ").append(eta).append("\n");
        testMessage.append("    Steamship Line:         ").append(steamShipLine).append("\n");
        testMessage.append("    Vessel Name:            ").append(vesselName).append("\n");
        testMessage.append("    Consignee:              ").append(consignee).append("\n");
        testMessage.append("\nIf you have any question, please let us know A.S.A.P. by replying to this message.\n");
        testMessage.append("Regards,\n");
        testMessage.append("Credit & Collections Dept");
        return testMessage.toString();
    }
}

package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import javax.servlet.http.HttpServletRequest;

public class ExportVoyageHblBatchForm extends LogiwareActionForm {

    private Integer original;
    private Integer nonNegotiable;
    private Integer signedNonNegotiable;
    private Integer unsignedOriginal;
    private Integer frieghtInvoice;
    private String billOfLading;
    private boolean negotiableEmail;
    private boolean frieghtEmail;
    private String headerId;
    private String voyageNumber;
    private String pickedCargoBkg;
    private String documentTypes;
    private String toEmailAddress;
    private String ccEmailAddress;
    private String bccEmailAddress;
    private String emailSubject;
    private boolean emailMe;
    private String unitSSId;
    private String printerName;
    private String originUnLocCode;
    private TradingPartner defaultAgent;
    private String agentEmailAddress;
    private Integer destinationId;
    private String emailMessage;
    private Integer frieghtInvoiceCollect;
    private boolean frieghtEmailCollect;
    private String fileNumberId;
    private String fileNumber;
    private Integer unitManifest;
    private boolean unitManifestEmail;
    private Integer unitLargePrintManifest;
    private boolean unitLargePrintManifestEmail;
    private Integer unitMiniConsolidationManifest;
    private boolean unitMiniConsolidationManifestEmail;
    private Integer unitUnratedDockReceipt;
    private boolean unitUnratedDockReceiptEmail;

    public Integer getOriginal() {
        return original;
    }

    public void setOriginal(Integer original) {
        this.original = original;
    }

    public Integer getNonNegotiable() {
        return nonNegotiable;
    }

    public void setNonNegotiable(Integer nonNegotiable) {
        this.nonNegotiable = nonNegotiable;
    }

    public Integer getSignedNonNegotiable() {
        return signedNonNegotiable;
    }

    public void setSignedNonNegotiable(Integer signedNonNegotiable) {
        this.signedNonNegotiable = signedNonNegotiable;
    }

    public Integer getUnsignedOriginal() {
        return unsignedOriginal;
    }

    public void setUnsignedOriginal(Integer unsignedOriginal) {
        this.unsignedOriginal = unsignedOriginal;
    }

    public Integer getFrieghtInvoice() {
        return frieghtInvoice;
    }

    public void setFrieghtInvoice(Integer frieghtInvoice) {
        this.frieghtInvoice = frieghtInvoice;
    }

    public String getBillOfLading() {
        return billOfLading;
    }

    public void setBillOfLading(String billOfLading) {
        this.billOfLading = billOfLading;
    }

    public boolean isNegotiableEmail() {
        return negotiableEmail;
    }

    public void setNegotiableEmail(boolean negotiableEmail) {
        this.negotiableEmail = negotiableEmail;
    }

    public boolean isFrieghtEmail() {
        return frieghtEmail;
    }

    public void setFrieghtEmail(boolean frieghtEmail) {
        this.frieghtEmail = frieghtEmail;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getPickedCargoBkg() {
        return pickedCargoBkg;
    }

    public void setPickedCargoBkg(String pickedCargoBkg) {
        this.pickedCargoBkg = pickedCargoBkg;
    }

    public String getToEmailAddress() {
        return toEmailAddress;
    }

    public void setToEmailAddress(String toEmailAddress) {
        this.toEmailAddress = toEmailAddress;
    }

    public String getCcEmailAddress() {
        return ccEmailAddress;
    }

    public void setCcEmailAddress(String ccEmailAddress) {
        this.ccEmailAddress = ccEmailAddress;
    }

    public String getBccEmailAddress() {
        return bccEmailAddress;
    }

    public void setBccEmailAddress(String bccEmailAddress) {
        this.bccEmailAddress = bccEmailAddress;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public boolean isEmailMe() {
        return emailMe;
    }

    public void setEmailMe(boolean emailMe) {
        this.emailMe = emailMe;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

    public String getOriginUnLocCode() throws Exception {
        UnLocation unLoction = new UnLocationDAO().findById(this.destinationId);
        return unLoction.getUnLocationCode();
    }

    public void setOriginUnLocCode(String originUnLocCode) {
        this.originUnLocCode = originUnLocCode;
    }

    public TradingPartner getDefaultAgent() throws Exception {
        String agentInfo[] = new PortsDAO().getDefaultAgentForLcl(getOriginUnLocCode(), "L");
        if (CommonUtils.isNotEmpty(agentInfo)) {
            if (agentInfo[0] != null) {
                TradingPartner tradingPartner = new TradingPartnerDAO().findById(agentInfo[0]);
                return tradingPartner;
            }
        }
        return null;
    }

    public void setDefaultAgent(TradingPartner defaultAgent) {
        this.defaultAgent = defaultAgent;
    }

    public String getAgentEmailAddress() throws Exception {
        String email = "", email1 = "";
        TradingPartner tradingPartner = getDefaultAgent();
        if (tradingPartner != null && tradingPartner.getCustAddr() != null) {
            email = tradingPartner.getCustAddr().getEmail1();
            email1 = tradingPartner.getCustAddr().getEmail2();
        }
        return !"".equalsIgnoreCase(email) ? email : email1;
    }

    public void setAgentEmailAddress(String agentEmailAddress) {
        this.agentEmailAddress = agentEmailAddress;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public Integer getFrieghtInvoiceCollect() {
        return frieghtInvoiceCollect;
    }

    public void setFrieghtInvoiceCollect(Integer frieghtInvoiceCollect) {
        this.frieghtInvoiceCollect = frieghtInvoiceCollect;
    }

    public boolean isFrieghtEmailCollect() {
        return frieghtEmailCollect;
    }

    public void setFrieghtEmailCollect(boolean frieghtEmailCollect) {
        this.frieghtEmailCollect = frieghtEmailCollect;
    }

    public String getDocumentTypes() {
        String types = this.original != null && this.original != 0 ? "Original," : "";
        types += (this.nonNegotiable != null && this.nonNegotiable != 0) || this.negotiableEmail ? "NonNegotiable," : "";
        types += this.signedNonNegotiable != null && this.signedNonNegotiable != 0 ? "SignedNonNegotiable," : "";
        types += this.unsignedOriginal != null && this.unsignedOriginal != 0 ? "UnsignedOriginal," : "";
        types += (this.frieghtInvoice != null && this.frieghtInvoice != 0) || this.frieghtEmail ? "FreightInvoice," : "";
        types += (this.unitMiniConsolidationManifest != null && this.unitMiniConsolidationManifest != 0)
                || this.unitMiniConsolidationManifestEmail ? "MiniConsolidationManifest," : "";
        types += (this.frieghtInvoiceCollect != null && this.frieghtInvoiceCollect != 0) || this.frieghtEmailCollect ? "FreightInvoiceCollect," : "";
        types += (this.unitLargePrintManifest != null && this.unitLargePrintManifest != 0) || this.unitLargePrintManifestEmail ? "LargePrintManifest," : "";
        types += (this.unitManifest != null && this.unitManifest != 0) || this.unitManifestEmail ? "Manifest," : "";
        types += (this.unitUnratedDockReceipt != null && this.unitUnratedDockReceipt != 0) || this.unitUnratedDockReceiptEmail ? "UnratedDockReceipt" : "";
        return types;
    }

    public void setDocumentTypes(String documentTypes) {
        this.documentTypes = documentTypes;
    }

    public String getUnitSSId() {
        return unitSSId;
    }

    public void setUnitSSId(String unitSSId) {
        this.unitSSId = unitSSId;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Integer getUnitManifest() {
        return unitManifest;
    }

    public void setUnitManifest(Integer unitManifest) {
        this.unitManifest = unitManifest;
    }

    public boolean isUnitManifestEmail() {
        return unitManifestEmail;
    }

    public void setUnitManifestEmail(boolean unitManifestEmail) {
        this.unitManifestEmail = unitManifestEmail;
    }

    public Integer getUnitLargePrintManifest() {
        return unitLargePrintManifest;
    }

    public void setUnitLargePrintManifest(Integer unitLargePrintManifest) {
        this.unitLargePrintManifest = unitLargePrintManifest;
    }

    public boolean isUnitLargePrintManifestEmail() {
        return unitLargePrintManifestEmail;
    }

    public void setUnitLargePrintManifestEmail(boolean unitLargePrintManifestEmail) {
        this.unitLargePrintManifestEmail = unitLargePrintManifestEmail;
    }

    public Integer getUnitMiniConsolidationManifest() {
        return unitMiniConsolidationManifest;
    }

    public void setUnitMiniConsolidationManifest(Integer unitMiniConsolidationManifest) {
        this.unitMiniConsolidationManifest = unitMiniConsolidationManifest;
    }

    public boolean isUnitMiniConsolidationManifestEmail() {
        return unitMiniConsolidationManifestEmail;
    }

    public void setUnitMiniConsolidationManifestEmail(boolean unitMiniConsolidationManifestEmail) {
        this.unitMiniConsolidationManifestEmail = unitMiniConsolidationManifestEmail;
    }

    public Integer getUnitUnratedDockReceipt() {
        return unitUnratedDockReceipt;
    }

    public void setUnitUnratedDockReceipt(Integer unitUnratedDockReceipt) {
        this.unitUnratedDockReceipt = unitUnratedDockReceipt;
    }

    public boolean isUnitUnratedDockReceiptEmail() {
        return unitUnratedDockReceiptEmail;
    }

    public void setUnitUnratedDockReceiptEmail(boolean unitUnratedDockReceiptEmail) {
        this.unitUnratedDockReceiptEmail = unitUnratedDockReceiptEmail;
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

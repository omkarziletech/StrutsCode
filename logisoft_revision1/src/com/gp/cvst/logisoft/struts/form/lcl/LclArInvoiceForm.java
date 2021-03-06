/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclArInvoiceForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclArInvoiceForm.class);
    private ArRedInvoice arRedInvoice;
    private ArRedInvoiceCharges arRedInvoiceCharges;
    private String fileNumber;
    private String fileNumberId;
    private String notification;
    private String terminal;
    private String chargeCode;
    private String chargeId;
    private Integer arRedInvoiceId;
    private String date;
    private String dueDate;
    private String status;
    private String pageName;
    private String moduleName;//module Name exports or imports
    private String voyTerminalNo;
    private String headerId;
    private String hiddenInvoiceComments;
    private String comments;
    private String consoTerminate;

    public LclArInvoiceForm() {
        if (arRedInvoice == null) {
            arRedInvoice = new ArRedInvoice();
        }
        if (arRedInvoiceCharges == null) {
            arRedInvoiceCharges = new ArRedInvoiceCharges();
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHiddenInvoiceComments() {
        return hiddenInvoiceComments;
    }

    public void setHiddenInvoiceComments(String hiddenInvoiceComments) {
        this.hiddenInvoiceComments = hiddenInvoiceComments;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public ArRedInvoice getArRedInvoice() {
        return arRedInvoice;
    }

    public void setArRedInvoice(ArRedInvoice arRedInvoice) {
        this.arRedInvoice = arRedInvoice;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Integer getArRedInvoiceId() {
        return arRedInvoiceId;
    }

    public void setArRedInvoiceId(Integer arRedInvoiceId) {
        this.arRedInvoiceId = arRedInvoiceId;
    }

    public String getCustomerName() {
        return arRedInvoice.getCustomerName();
    }

    public void setCustomerName(String customerName) {
        arRedInvoice.setCustomerName(customerName);
    }

    public String getCustomerNumber() {
        return arRedInvoice.getCustomerNumber();
    }

    public void setCustomerNumber(String customerNumber) {
        arRedInvoice.setCustomerNumber(customerNumber);
    }

    public String getStatus() {
        return arRedInvoice.getStatus();
    }

    public void setStatus(String status) {
        arRedInvoice.setStatus(status);
    }

    public String getCustomerType() {
        return arRedInvoice.getCustomerType();
    }

    public void setCustomerType(String customerType) {
        arRedInvoice.setCustomerType(customerType);
    }

    public String getContactName() {
        return arRedInvoice.getContactName();
    }

    public void setContactName(String contactName) {
        arRedInvoice.setContactName(contactName);
    }

    public String getAddress() {
        return arRedInvoice.getAddress();
    }

    public void setAddress(String address) {
        arRedInvoice.setAddress(address);
    }

    public String getPhoneNumber() {
        return arRedInvoice.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        arRedInvoice.setPhoneNumber(phoneNumber);
    }

    public String getInvoiceNumber() {
        return arRedInvoice.getInvoiceNumber();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        arRedInvoice.setInvoiceNumber(invoiceNumber);
    }

    public String getDate() throws Exception {
        if (arRedInvoice.getDate() != null) {
            String d = DateUtils.formatDate(arRedInvoice.getDate(), "dd-MMM-yyyy");
            return null == d ? "" : d;
        }
        return "";
    }

    public void setDate(String date) throws Exception {
        if (CommonUtils.isNotEmpty(date)) {
            arRedInvoice.setDate(DateUtils.parseDate(date, "dd-MMM-yyyy"));
        } else {
            arRedInvoice.setDate(null);
        }
    }

    public String getTerm() {
        return arRedInvoice.getTerm();
    }

    public void setTerm(String term) {
        arRedInvoice.setTerm(term);
    }

    public String getTermDesc() {
        return arRedInvoice.getTermDesc();
    }

    public void setTermDesc(String termDesc) {
        arRedInvoice.setTermDesc(termDesc);
    }

    public String getDueDate() throws Exception {
        if (arRedInvoice.getDueDate() != null) {
            String d = DateUtils.formatDate(arRedInvoice.getDueDate(), "dd-MMM-yyyy");
            return null == d ? "" : d;
        }
        return "";
    }

    public void setDueDate(String dueDate) throws Exception {
        if (CommonUtils.isNotEmpty(dueDate)) {
            arRedInvoice.setDueDate(DateUtils.parseDate(dueDate, "dd-MMM-yyyy"));
        } else {
            arRedInvoice.setDueDate(null);
        }
    }

//   public String getDueDate() {
//
//        String d = "";
//        if (arRedInvoice.getDueDate() != null) {
//            d = DateUtils.formatStringDateToAppFormatMMM(arRedInvoice.getDueDate());
//        } else {
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.MONTH, 1);
//            d = DateUtils.formatStringDateToAppFormatMMM(cal.getTime());
//        }
//        return null == d ? "" : d;
//    }
//
//    public void setDueDate(String dueDate) {
//        arRedInvoice.setDate(null);
//    }
    public String getNotes() {
        return arRedInvoice.getNotes();
    }

    public void setNotes(String notes) {
        arRedInvoice.setNotes(notes);
    }

    public String getDescription() {
        return arRedInvoice.getDescription();
    }

    public void setDescription(String description) {
        arRedInvoice.setDescription(description);
    }

    //charge section
    public ArRedInvoiceCharges getArRedInvoiceCharges() {
        return arRedInvoiceCharges;
    }

    public void setArRedInvoiceCharges(ArRedInvoiceCharges arRedInvoiceCharges) {
        this.arRedInvoiceCharges = arRedInvoiceCharges;
    }

    public Double getAmount() {
        return 0.000;
        //return arRedInvoiceCharges.getAmount();
    }

    public void setAmount(Double amount) {
        arRedInvoiceCharges.setAmount(amount);
    }

    public String getChargeCodeId() {
        return arRedInvoiceCharges.getChargeCode();
    }

    public void setChargeCodeId(String chargeCodeId) {
        arRedInvoiceCharges.setChargeCode(chargeCodeId);
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getChargeDescription() {
        return arRedInvoiceCharges.getDescription();
    }

    public void setChargeDescription(String description) {
        arRedInvoiceCharges.setDescription(description);
    }

    public String getGlAccount() {
        return arRedInvoiceCharges.getGlAccount();
    }

    public void setGlAccount(String glAccount) {
        arRedInvoiceCharges.setGlAccount(glAccount);
    }

    public Integer getId() {
        return arRedInvoiceCharges.getId();
    }

    public void setId(Integer id) {
        arRedInvoiceCharges.setId(id);
    }

    public String getQuantity() {
        return arRedInvoiceCharges.getQuantity();
    }

    public void setQuantity(String quantity) {
        arRedInvoiceCharges.setQuantity(quantity);
    }

    public String getShipmentType() {
        return arRedInvoiceCharges.getShipmentType();
    }

    public void setShipmentType(String shipmentType) {
        arRedInvoiceCharges.setShipmentType(shipmentType);
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTerminalNumber() {
        return arRedInvoiceCharges.getTerminal();
    }

    public void setTerminalNumber(String terminal) {
        arRedInvoiceCharges.setTerminal(terminal);
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getVoyTerminalNo() {
        return voyTerminalNo;
    }

    public void setVoyTerminalNo(String voyTerminalNo) {
        this.voyTerminalNo = voyTerminalNo;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getConsoTerminate() {
        return consoTerminate;
    }

    public void setConsoTerminate(String consoTerminate) {
        this.consoTerminate = consoTerminate;
    }
    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String invoiceId = request.getParameter("arRedInvoiceId");
        if (CommonUtils.isNotEmpty(invoiceId)) {
            try {
                arRedInvoice = new ArRedInvoiceDAO().findById(Integer.parseInt(invoiceId));
            } catch (Exception ex) {
                log.info("reset() in LclArInvoiceForm failed on " + new Date(),ex);
            }
        }
        if (arRedInvoice == null) {
            arRedInvoice = new ArRedInvoice();
        }
    }
}

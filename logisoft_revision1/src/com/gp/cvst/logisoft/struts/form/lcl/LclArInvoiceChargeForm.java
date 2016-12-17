/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.logiware.hibernate.domain.ArRedInvoiceCharges;

/**
 *
 * @author lakshh
 */
public class LclArInvoiceChargeForm extends LogiwareActionForm {

    private ArRedInvoiceCharges arRedInvoiceCharges;
    private String fileNumberId;
    private String invoiceId;

    public LclArInvoiceChargeForm() {
        if (arRedInvoiceCharges == null) {
            arRedInvoiceCharges = new ArRedInvoiceCharges();
        }
    }

    public ArRedInvoiceCharges getArRedInvoiceCharges() {
        return arRedInvoiceCharges;
    }

    public void setArRedInvoiceCharges(ArRedInvoiceCharges arRedInvoiceCharges) {
        this.arRedInvoiceCharges = arRedInvoiceCharges;
    }

    public String getFileNumber() {
        return arRedInvoiceCharges.getBlDrNumber();
    }

    public void setFileNumber(String fileNumber) {
        arRedInvoiceCharges.setBlDrNumber(fileNumber);
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Double getAmount() {
        return arRedInvoiceCharges.getAmount();
    }

    public void setAmount(Double amount) {
        arRedInvoiceCharges.setAmount(amount);
    }

    public Integer getArRedInvoiceId() {
        return arRedInvoiceCharges.getArRedInvoiceId();
    }

    public void setArRedInvoiceId(Integer arRedInvoiceId) {
        arRedInvoiceCharges.setArRedInvoiceId(arRedInvoiceId);
    }

    public String getInvoiceNumber() {
        return arRedInvoiceCharges.getInvoiceNumber();
    }

    public void setInvoiceNumber(String invoiceNumber) {
        arRedInvoiceCharges.setInvoiceNumber(invoiceNumber);
    }

    public String getChargeCode() {
        return arRedInvoiceCharges.getChargeCode();
    }

    public void setChargeCode(String chargeCode) {
        arRedInvoiceCharges.setChargeCode(chargeCode);
    }

    public String getDescription() {
        return arRedInvoiceCharges.getDescription();
    }

    public void setDescription(String description) {
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
        return arRedInvoiceCharges.getTerminal();
    }

    public void setTerminal(String terminal) {
        arRedInvoiceCharges.setTerminal(terminal);
    }

}

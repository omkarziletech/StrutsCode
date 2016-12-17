package com.gp.cong.logisoft.domain;

public class CreditDebitNote implements java.io.Serializable {

    private Integer id;
    private String bolid;
    private String correctionNumber;
    private String customerName;
    private String customerNumber;
    private String debitCreditNote;
    private String billToParty;

    public CreditDebitNote() {
    }

    public CreditDebitNote(String bolid, String correctionNumber,
            String customerName, String customerNumber, String debitCreditNote) {
        this.bolid = bolid;
        this.correctionNumber = correctionNumber;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.debitCreditNote = debitCreditNote;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBolid() {
        return this.bolid;
    }

    public void setBolid(String bolid) {
        this.bolid = bolid;
    }

    public String getCorrectionNumber() {
        return this.correctionNumber;
    }

    public void setCorrectionNumber(String correctionNumber) {
        this.correctionNumber = correctionNumber;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return this.customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDebitCreditNote() {
        return this.debitCreditNote;
    }

    public void setDebitCreditNote(String debitCreditNote) {
        this.debitCreditNote = debitCreditNote;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }
}

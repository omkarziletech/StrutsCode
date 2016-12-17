package com.gp.cvst.logisoft.struts.action;

import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;

public class customerStatementVO {

    private String overdue;
    private String minamt;
    private String agingzeero;
    private String agingthirty;
    private String greaterthanthirty;
    private String agingsixty;
    private String greaterthansixty;
    private String agingninty;
    private String greaterthanninty;
    private String nintyplus;
    private String subject;
    private String textArea;
    private String footerName;
    private String company;
    private String terminal;
    private String collector;
    private String allcollectors;
    private String customerName;
    private String customerNumber;
    private String stmtWithCredit;
    private String includeInvoiceCredit;
    private String printCoverLetter;
    private String includeNetSettlement;
    private String includeAP;
    private String includeAccruals;

    public String getIncludeNetSettlement() {
        return includeNetSettlement;
    }

    public void setIncludeNetSettlement(String includeNetSettlement) {
        this.includeNetSettlement = includeNetSettlement;
    }

    public String getIncludeAP() {
        return includeAP;
    }

    public void setIncludeAP(String includeAP) {
        this.includeAP = includeAP;
    }

    public String getIncludeAccruals() {
        return includeAccruals;
    }

    public void setIncludeAccruals(String includeAccruals) {
        this.includeAccruals = includeAccruals;
    }

    public String getStmtWithCredit() {
        return stmtWithCredit;
    }

    public void setStmtWithCredit(String stmtWithCredit) {
        this.stmtWithCredit = stmtWithCredit;
    }

    public String getIncludeInvoiceCredit() {
        return includeInvoiceCredit;
    }

    public void setIncludeInvoiceCredit(String includeInvoiceCredit) {
        this.includeInvoiceCredit = includeInvoiceCredit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public String getMinamt() {
        return minamt;
    }

    public void setMinamt(String minamt) {
        this.minamt = minamt;
    }

    public String getAgingzeero() {
        return agingzeero;
    }

    public void setAgingzeero(String agingzeero) {
        this.agingzeero = agingzeero;
    }

    public String getAgingthirty() {
        return agingthirty;
    }

    public void setAgingthirty(String agingthirty) {
        this.agingthirty = agingthirty;
    }

    public String getGreaterthanthirty() {
        return greaterthanthirty;
    }

    public void setGreaterthanthirty(String greaterthanthirty) {
        this.greaterthanthirty = greaterthanthirty;
    }

    public String getAgingsixty() {
        return agingsixty;
    }

    public void setAgingsixty(String agingsixty) {
        this.agingsixty = agingsixty;
    }

    public String getGreaterthansixty() {
        return greaterthansixty;
    }

    public void setGreaterthansixty(String greaterthansixty) {
        this.greaterthansixty = greaterthansixty;
    }

    public String getAgingninty() {
        return agingninty;
    }

    public void setAgingninty(String agingninty) {
        this.agingninty = agingninty;
    }

    public String getGreaterthanninty() {
        return greaterthanninty;
    }

    public void setGreaterthanninty(String greaterthanninty) {
        this.greaterthanninty = greaterthanninty;
    }

    public String getNintyplus() {
        return nintyplus;
    }

    public void setNintyplus(String nintyplus) {
        this.nintyplus = nintyplus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public String getFooterName() {
        return footerName;
    }

    public void setFooterName(String footerName) {
        this.footerName = footerName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public customerStatementVO(String overdue, String minamt,
            String agingzeero, String agingthirty, String greaterthanthirty,
            String agingsixty, String greaterthansixty, String agingninty,
            String greaterthanninty, String nintyplus, String subject,
            String textArea, String footerName, String company,
            String terminal, String collector, String customerName, String customerNumber, String stmtWithCredit, String includeInvoiceCredit, String printCoverLetter, String includeAP, String includeAccruals, String includeNetSettlement) {
        this.overdue = overdue;
        this.minamt = minamt;
        this.agingzeero = agingzeero;
        this.agingthirty = agingthirty;
        this.greaterthanthirty = greaterthanthirty;
        this.agingsixty = agingsixty;
        this.greaterthansixty = greaterthansixty;
        this.agingninty = agingninty;
        this.greaterthanninty = greaterthanninty;
        this.nintyplus = nintyplus;
        this.subject = subject;
        this.textArea = textArea;
        this.footerName = footerName;
        this.company = company;
        this.terminal = terminal;
        this.collector = collector;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.stmtWithCredit = stmtWithCredit;
        this.includeInvoiceCredit = includeInvoiceCredit;
        this.printCoverLetter = printCoverLetter;
        this.includeAP = includeAP;
        this.includeAccruals = includeAccruals;
        this.includeNetSettlement = includeNetSettlement;
    }
    public customerStatementVO(CustomerStatementForm customerStatementForm) {
        this.overdue = customerStatementForm.getOverdue();
        this.minamt = customerStatementForm.getMinamt();
        this.agingzeero = customerStatementForm.getAgingzeero();
        this.agingthirty = customerStatementForm.getAgingthirty();
        this.greaterthanthirty = customerStatementForm.getGreaterthanthirty();
        this.agingsixty = customerStatementForm.getAgingsixty();
        this.greaterthansixty = customerStatementForm.getGreaterthansixty();
        this.agingninty = customerStatementForm.getAgingninty();
        this.greaterthanninty = customerStatementForm.getGreaterthanninty();
        this.nintyplus = customerStatementForm.getNintyplus();
        this.subject = customerStatementForm.getSubject();
        this.textArea = customerStatementForm.getTextArea();
        this.footerName = customerStatementForm.getFooterName();
        this.company = customerStatementForm.getCompany();
        this.terminal = customerStatementForm.getTerminal();
        this.collector = customerStatementForm.getCollector();
        this.customerName = customerStatementForm.getCustomerName();
        this.customerNumber = customerStatementForm.getCustomerNumber();
//        this.stmtWithCredit = customerStatementForm.getStmtWithCredit();
//        this.includeInvoiceCredit = customerStatementForm.getIncludeInvoiceCredit();
//        this.printCoverLetter = customerStatementForm.getPrintCoverLetter();
//        this.includeAP = customerStatementForm.getIncludeAP();
//        this.includeAccruals = customerStatementForm.getIncludeAccruals();
        this.includeNetSettlement = customerStatementForm.getIncludeNetSettlement();
    }

    public customerStatementVO() {
    }

    public String getAllcollectors() {
        return allcollectors;
    }

    public void setAllcollectors(String allcollectors) {
        this.allcollectors = allcollectors;
    }

    public String getPrintCoverLetter() {
        return printCoverLetter;
    }

    public void setPrintCoverLetter(String printCoverLetter) {
        this.printCoverLetter = printCoverLetter;
    }
}

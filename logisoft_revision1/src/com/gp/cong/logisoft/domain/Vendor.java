package com.gp.cong.logisoft.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.io.Serializable;

public class Vendor implements Serializable {

    private Integer id;
    private String legalname;
    private String dba;
    private String tin;
    private String wfile;
    private String apname;
    private String phone;
    private String fax;
    private String email;
    private String web;
    private String eamanager;
    private String credit;
    private Double climit;
    private GenericCode cterms;
    private String baccount;
    private String deactivate;
    private String custaccount; //foreign key
    private Integer index;
    private String Address1;
    private String city2;
    private String state;
    private String zip;
    private String company;
    private GenericCode cuntry;
    private String accountno;
    private User apSpecialist;
    private String fileLocation;
    private String apSpecialistUpdatedBy;
    private String ediCode;
    private boolean exemptInactivate;

    public User getApSpecialist() {
        return apSpecialist;
    }

    public void setApSpecialist(User apSpecialist) {
        this.apSpecialist = apSpecialist;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getApSpecialistUpdatedBy() {
        return apSpecialistUpdatedBy;
    }

    public void setApSpecialistUpdatedBy(String apSpecialistUpdatedBy) {
        this.apSpecialistUpdatedBy = apSpecialistUpdatedBy;
    }

    public Vendor() {
    }

    public Vendor(TradingPartnerForm tradingPartnerForm) {
        ediCode = tradingPartnerForm.getEdiCode();
        exemptInactivate = tradingPartnerForm.isExemptInactivate();
        accountno = tradingPartnerForm.getTradingPartnerId();
        legalname = tradingPartnerForm.getLegalname();
        dba = tradingPartnerForm.getDba();
        tin = tradingPartnerForm.getTin();
        wfile = tradingPartnerForm.getWfile();
        apname = tradingPartnerForm.getApname();
        phone = tradingPartnerForm.getPhone();
        fax = tradingPartnerForm.getFax();
        email = tradingPartnerForm.getEmail();
        web = tradingPartnerForm.getWeb();

        // converting into integer

        credit = tradingPartnerForm.getCredit();
        if (tradingPartnerForm.getClimit() != null && !tradingPartnerForm.getClimit().trim().equals("")) {
            climit = new Double(tradingPartnerForm.getClimit().replaceAll(",", ""));
        }
        baccount = tradingPartnerForm.getBaccount();
        deactivate = tradingPartnerForm.getDeactivate();
        Address1 = tradingPartnerForm.getAddress1();
        city2 = tradingPartnerForm.getCity();
        state = tradingPartnerForm.getState();
        zip = tradingPartnerForm.getZip();
        company = tradingPartnerForm.getCompanyName();
        eamanager = tradingPartnerForm.getEamanager();
        fileLocation = tradingPartnerForm.getFileLocation();
    }

    public TradingPartnerForm loadAPConfiguration(Vendor vendor, TradingPartnerForm tradingPartnerForm) {
        tradingPartnerForm.setEdiCode(vendor.getEdiCode());
        tradingPartnerForm.setExemptInactivate(vendor.isExemptInactivate());
        tradingPartnerForm.setIndex(vendor.getId().toString());
        tradingPartnerForm.setLegal(vendor.getLegalname());
        tradingPartnerForm.setLegalname(vendor.getLegalname());
        tradingPartnerForm.setDba(vendor.getDba());
        tradingPartnerForm.setTin(vendor.getTin());
        tradingPartnerForm.setWfile(vendor.getWfile());
        tradingPartnerForm.setApname(vendor.getApname());
        tradingPartnerForm.setWeb(vendor.getWeb());

        tradingPartnerForm.setCredit(vendor.getCredit());
        if (null != vendor.getClimit()) {
            NumberFormat numberFormat = new DecimalFormat("##,###,##0.00");
            tradingPartnerForm.setClimit(numberFormat.format(vendor.getClimit()));
        } else {
            tradingPartnerForm.setClimit("0.00");
        }
        if (null != vendor.getCterms()) {
            tradingPartnerForm.setCterms("" + vendor.getCterms().getId());
        } else {
            tradingPartnerForm.setCterms("11344");
        }
        if (null != vendor.getEamanager()) {
            tradingPartnerForm.setEamanager(vendor.getEamanager());
        }
        if (null != vendor.getApSpecialist()) {
            tradingPartnerForm.setApSpecialistId(vendor.getApSpecialist().getUserId().toString());
            tradingPartnerForm.setApSpecialistName(vendor.getApSpecialist().getLoginName());
        }
        tradingPartnerForm.setCredit(vendor.getCredit());
        tradingPartnerForm.setBaccount(vendor.getBaccount());
        tradingPartnerForm.setBaddr(vendor.getAddress1());
        tradingPartnerForm.setRfax(vendor.getFax());
        tradingPartnerForm.setDeactivate(vendor.getDeactivate());
        tradingPartnerForm.setFileLocation(vendor.getFileLocation());
        tradingPartnerForm.setEmail(vendor.getEmail());
        tradingPartnerForm.setPhone(vendor.getPhone());
        tradingPartnerForm.setFax(vendor.getFax());
        tradingPartnerForm.setCompanyName(vendor.getCompany());
        return tradingPartnerForm;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public GenericCode getCuntry() {
        return cuntry;
    }

    public void setCuntry(GenericCode cuntry) {
        this.cuntry = cuntry;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getEamanager() {
        return eamanager;
    }

    public void setEamanager(String eamanager) {
        this.eamanager = eamanager;
    }

    public String getCustaccount() {
        return custaccount;
    }

    public void setCustaccount(String custaccount) {
        this.custaccount = custaccount;
    }

    public String getApname() {
        return apname;
    }

    public void setApname(String apname) {
        this.apname = apname;
    }

    public String getBaccount() {
        return baccount;
    }

    public void setBaccount(String baccount) {
        this.baccount = baccount;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDba() {
        return dba;
    }

    public void setDba(String dba) {
        this.dba = dba;
    }

    public String getDeactivate() {
        return deactivate;
    }

    public void setDeactivate(String deactivate) {
        this.deactivate = deactivate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLegalname() {
        return legalname;
    }

    public void setLegalname(String legalname) {
        this.legalname = legalname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getWfile() {
        return wfile;
    }

    public void setWfile(String wfile) {
        this.wfile = wfile;
    }

    public Double getClimit() {
        return climit;
    }

    public void setClimit(Double climit) {
        this.climit = climit;
    }

    public GenericCode getCterms() {
        return cterms;
    }

    public void setCterms(GenericCode cterms) {
        this.cterms = cterms;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getEdiCode() {
        return ediCode;
    }

    public void setEdiCode(String ediCode) {
        this.ediCode = ediCode;
    }

    public boolean isExemptInactivate() {
        return exemptInactivate;
    }

    public void setExemptInactivate(boolean exemptInactivate) {
        this.exemptInactivate = exemptInactivate;
    }
}

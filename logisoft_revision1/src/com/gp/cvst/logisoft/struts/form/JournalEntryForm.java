package com.gp.cvst.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/** 
 * @author Lakshmi Narayanan
 */
public class JournalEntryForm extends ActionForm {

    private static final long serialVersionUID = -3375109022323273356L;
    private String batch;
    private String journalid;
    private String acctno;
    private String acctname;
    private String jedebit;
    private String jecredit;
    private String memo;
    private String jecurrency;
    private String buttonValue;
    private String jetotalcredit;
    private String jetotaldebit;
    private String jedesc;
    private String year;
    private String month;
    private String itemNo;
    private String reference;
    private String description;
    private String account;
    private String accountDesc;
    private String debits;
    private String credits;
    private String currency;
    private String txtCal;
    private String bdesc;
    private String btotaldebit;
    private String btotalcredit;
    private String bstatus;
    private String jper;
    private String jesourcecode;
    private String jescdesc;
    private String jememo;
    private String reverse;
    private String tempLineItemId;
    private String[] displineItemId;
    private String[] dispreference;
    private String[] dispreferenceDesc;
    private String[] dispaccount;
    private String[] dispaccountDesc;
    private String[] dispdebit;
    private String[] dispcredit;
    private String[] dispcurrency;
    private String hiddenjeperiod;
    private String hiddenItemNo;
    private String index;
    private FormFile journalEntrySheet;

    public String getIndex() {
	return index;
    }

    public void setIndex(String index) {
	this.index = index;
    }

    public String getHiddenItemNo() {
	return hiddenItemNo;
    }

    public void setHiddenItemNo(String hiddenItemNo) {
	this.hiddenItemNo = hiddenItemNo;
    }

    public String getHiddenjeperiod() {
	return hiddenjeperiod;
    }

    public void setHiddenjeperiod(String hiddenjeperiod) {
	this.hiddenjeperiod = hiddenjeperiod;
    }

    public String[] getDispaccount() {
	return dispaccount;
    }

    public void setDispaccount(String[] dispaccount) {
	this.dispaccount = dispaccount;
    }

    public String[] getDispaccountDesc() {
	return dispaccountDesc;
    }

    public void setDispaccountDesc(String[] dispaccountDesc) {
	this.dispaccountDesc = dispaccountDesc;
    }

    public String[] getDispcredit() {
	return dispcredit;
    }

    public void setDispcredit(String[] dispcredit) {
	this.dispcredit = dispcredit;
    }

    public String[] getDispcurrency() {
	return dispcurrency;
    }

    public void setDispcurrency(String[] dispcurrency) {
	this.dispcurrency = dispcurrency;
    }

    public String[] getDispdebit() {
	return dispdebit;
    }

    public void setDispdebit(String[] dispdebit) {
	this.dispdebit = dispdebit;
    }

    public String[] getDisplineItemId() {
	return displineItemId;
    }

    public void setDisplineItemId(String[] displineItemId) {
	this.displineItemId = displineItemId;
    }

    public String[] getDispreference() {
	return dispreference;
    }

    public void setDispreference(String[] dispreference) {
	this.dispreference = dispreference;
    }

    public String[] getDispreferenceDesc() {
	return dispreferenceDesc;
    }

    public void setDispreferenceDesc(String[] dispreferenceDesc) {
	this.dispreferenceDesc = dispreferenceDesc;
    }

    public String getReverse() {
	return reverse;
    }

    public void setReverse(String reverse) {
	this.reverse = reverse;
    }

    public String getJememo() {
	return jememo;
    }

    public void setJememo(String jememo) {
	this.jememo = jememo;
    }

    public String getJescdesc() {
	return jescdesc;
    }

    public void setJescdesc(String jescdesc) {
	this.jescdesc = jescdesc;
    }

    public String getJesourcecode() {
	return jesourcecode;
    }

    public void setJesourcecode(String jesourcecode) {
	this.jesourcecode = jesourcecode;
    }

    public String getBstatus() {
	return bstatus;
    }

    public void setBstatus(String bstatus) {
	this.bstatus = bstatus;
    }

    public String getBtotalcredit() {
	return btotalcredit;
    }

    public void setBtotalcredit(String btotalcredit) {
	this.btotalcredit = btotalcredit;
    }

    public String getBtotaldebit() {
	return btotaldebit;
    }

    public void setBtotaldebit(String btotaldebit) {
	this.btotaldebit = btotaldebit;
    }

    public String getBdesc() {
	return bdesc;
    }

    public void setBdesc(String bdesc) {
	this.bdesc = bdesc;
    }

    public String getTxtCal() {
	return txtCal;
    }

    public void setTxtCal(String txtCal) {
	this.txtCal = txtCal;
    }

    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }

    public String getAccountDesc() {
	return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
	this.accountDesc = accountDesc;
    }

    public String getCredits() {
	return credits;
    }

    public void setCredits(String credits) {
	this.credits = credits;
    }

    public String getCurrency() {
	return currency;
    }

    public void setCurrency(String currency) {
	this.currency = currency;
    }

    public String getDebits() {
	return debits;
    }

    public void setDebits(String debits) {
	this.debits = debits;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getItemNo() {
	return itemNo;
    }

    public void setItemNo(String itemNo) {
	this.itemNo = itemNo;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public String getMonth() {
	return month;
    }

    public void setMonth(String month) {
	this.month = month;
    }

    public String getYear() {
	return year;
    }

    public void setYear(String year) {
	this.year = year;
    }

    public String getJedesc() {
	return jedesc;
    }

    public void setJedesc(String jedesc) {
	this.jedesc = jedesc;
    }

    public String getJetotalcredit() {
	return jetotalcredit;
    }

    public void setJetotalcredit(String jetotalcredit) {
	this.jetotalcredit = jetotalcredit;
    }

    public String getJetotaldebit() {
	return jetotaldebit;
    }

    public void setJetotaldebit(String jetotaldebit) {
	this.jetotaldebit = jetotaldebit;
    }

    public String getAcctname() {
	return acctname;
    }

    public void setAcctname(String acctname) {
	this.acctname = acctname;
    }

    public String getAcctno() {
	return acctno;
    }

    public void setAcctno(String acctno) {
	this.acctno = acctno;
    }

    public String getBatch() {
	return batch;
    }

    public void setBatch(String batch) {
	this.batch = batch;
    }

    public String getJecredit() {
	return jecredit;
    }

    public void setJecredit(String jecredit) {
	this.jecredit = jecredit;
    }

    public String getJecurrency() {
	return jecurrency;
    }

    public void setJecurrency(String jecurrency) {
	this.jecurrency = jecurrency;
    }

    public String getJedebit() {
	return jedebit;
    }

    public void setJedebit(String jedebit) {
	this.jedebit = jedebit;
    }

    public String getJournalid() {
	return journalid;
    }

    public void setJournalid(String journalid) {
	this.journalid = journalid;
    }

    public String getMemo() {
	return memo;
    }

    public void setMemo(String memo) {
	this.memo = memo;
    }

    public ActionErrors validate(ActionMapping mapping,
	    HttpServletRequest request) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	// TODO Auto-generated method stub
    }

    public String getButtonValue() {
	return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
    }

    public String getTempLineItemId() {
	return tempLineItemId;
    }

    public void setTempLineItemId(String tempLineItemId) {
	this.tempLineItemId = tempLineItemId;
    }

    public String getJper() {
	return jper;
    }

    public void setJper(String jper) {
	this.jper = jper;
    }

    public FormFile getJournalEntrySheet() {
	return journalEntrySheet;
    }

    public void setJournalEntrySheet(FormFile journalEntrySheet) {
	this.journalEntrySheet = journalEntrySheet;
    }
}

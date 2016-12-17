package com.logiware.accounting.form;

import com.logiware.accounting.dao.SubledgerDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SubledgerForm extends BaseForm {

    private String subledger;
    private String glPeriod;
    private String searchBy;
    private String searchValue;
    private String glAccount;
    private String fromAmount = "0.00";
    private String toAmount = "0.00";
    private String subledgerId;
    private boolean posted;
    private boolean accruals;

    public String getSubledger() {
	return subledger;
    }

    public void setSubledger(String subledger) {
	this.subledger = subledger;
    }

    public String getGlPeriod() {
	return glPeriod;
    }

    public void setGlPeriod(String glPeriod) {
	this.glPeriod = glPeriod;
    }

    public String getSearchBy() {
	return searchBy;
    }

    public void setSearchBy(String searchBy) {
	this.searchBy = searchBy;
    }

    public String getSearchValue() {
	return searchValue;
    }

    public void setSearchValue(String searchValue) {
	this.searchValue = searchValue;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getFromAmount() {
	return fromAmount;
    }

    public void setFromAmount(String fromAmount) {
	this.fromAmount = fromAmount;
    }

    public String getToAmount() {
	return toAmount;
    }

    public void setToAmount(String toAmount) {
	this.toAmount = toAmount;
    }

    public String getSubledgerId() {
	return subledgerId;
    }

    public void setSubledgerId(String subledgerId) {
	this.subledgerId = subledgerId;
    }

    public boolean isPosted() {
	return posted;
    }

    public void setPosted(boolean posted) {
	this.posted = posted;
    }

    public boolean isAccruals() {
	return accruals;
    }

    public void setAccruals(boolean accruals) {
	this.accruals = accruals;
    }

    public List<LabelValueBean> getSubledgers() {
	return new SubledgerDAO().getSubledgerTypes();
    }

    public List<LabelValueBean> getSearchByOptions() {
	List<LabelValueBean> options = new ArrayList<LabelValueBean>();
	options.add(new LabelValueBean("Select", ""));
	options.add(new LabelValueBean("Blank GL", "blank_gl"));
	options.add(new LabelValueBean("GL not in COA", "gl_not_in_coa"));
	options.add(new LabelValueBean("GL Account", "gl_account_number"));
	options.add(new LabelValueBean("BL Number", "bill_ladding_no"));
	options.add(new LabelValueBean("Invoice Number", "invoice_number"));
	options.add(new LabelValueBean("Charge Code", "charge_code"));
	options.add(new LabelValueBean("Voyage", "voyage_no"));
	options.add(new LabelValueBean("Amount", "transaction_amt"));
	options.add(new LabelValueBean("Journal Entry", "journal_entry_number"));
	options.add(new LabelValueBean("Line Item", "line_item_number"));
	return Collections.unmodifiableList(options);
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	this.accruals = false;
    }
}

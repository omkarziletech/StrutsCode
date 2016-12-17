package com.logiware.accounting.form;

import com.gp.cong.common.ConstantsInterface;
import com.logiware.accounting.model.VendorModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApInquiryForm extends BaseForm {

    private String searchBy;
    private String searchValue;
    private String fromAmount = "0.00";
    private String toAmount = "0.00";
    private String showInvoices = "Open";
    private String showAccruals;
    private String searchDateBy = "invoice_date";
    private String ar = ConstantsInterface.NO;
    private String showSubsidiairy = ConstantsInterface.NO;
    private VendorModel vendor;

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

    public String getShowInvoices() {
	return showInvoices;
    }

    public void setShowInvoices(String showInvoices) {
	this.showInvoices = showInvoices;
    }

    public String getShowAccruals() {
	return showAccruals;
    }

    public void setShowAccruals(String showAccruals) {
	this.showAccruals = showAccruals;
    }

    public String getSearchDateBy() {
	return searchDateBy;
    }

    public void setSearchDateBy(String searchDateBy) {
	this.searchDateBy = searchDateBy;
    }

    public String getAr() {
	return ar;
    }

    public void setAr(String ar) {
	this.ar = ar;
    }

    public VendorModel getVendor() {
	return vendor;
    }

    public void setVendor(VendorModel vendor) {
	this.vendor = vendor;
    }

    public String getShowSubsidiairy() {
        return showSubsidiairy;
    }

    public void setShowSubsidiairy(String showSubsidiairy) {
        this.showSubsidiairy = showSubsidiairy;
    }
    
    public List<LabelValueBean> getSearchByOptions() {
	List<LabelValueBean> options = new ArrayList<LabelValueBean>();
	options.add(new LabelValueBean("Select", ""));
	options.add(new LabelValueBean("Invoice Number", "invoice_number"));
	options.add(new LabelValueBean("Invoice Amount", "invoice_amount"));
	options.add(new LabelValueBean("Dock Receipt", "drcpt"));
	options.add(new LabelValueBean("Voyage", "voyage_no"));
	options.add(new LabelValueBean("Container Number", "container_no"));
	options.add(new LabelValueBean("Booking Number", "booking_no"));
	options.add(new LabelValueBean("House Bill", "bill_ladding_no"));
	options.add(new LabelValueBean("Sub-House Bill", "sub_house_bl"));
	options.add(new LabelValueBean("Master Bill", "master_bl"));
	options.add(new LabelValueBean("Check Number", "check_number"));
	options.add(new LabelValueBean("Check Amount", "check_amount"));
	return Collections.unmodifiableList(options);
    }

    public List<LabelValueBean> getShowInvoicesOptions() {
	List<LabelValueBean> options = new ArrayList<LabelValueBean>();
	options.add(new LabelValueBean("Select", ""));
	options.add(new LabelValueBean("All", "All"));
	options.add(new LabelValueBean("Open", "Open"));
	options.add(new LabelValueBean("Paid", "Paid"));
	options.add(new LabelValueBean("Posted", "Posted"));
	options.add(new LabelValueBean("Disputed", "Disputed"));
	options.add(new LabelValueBean("Rejected", "Rejected"));
	options.add(new LabelValueBean("In Progress", "In Progress"));
	return Collections.unmodifiableList(options);
    }

    public List<LabelValueBean> getShowAccrualsOptions() {
	List<LabelValueBean> options = new ArrayList<LabelValueBean>();
	options.add(new LabelValueBean("Select", ""));
	options.add(new LabelValueBean("All", "All"));
	options.add(new LabelValueBean("Open", "Open"));
	options.add(new LabelValueBean("Deleted", "Deleted"));
	options.add(new LabelValueBean("Inactive", "Inactive"));
	options.add(new LabelValueBean("Assigned", "Assigned"));
	return Collections.unmodifiableList(options);
    }
}

package com.logiware.accounting.form;

import com.gp.cong.common.ConstantsInterface;
import com.logiware.accounting.model.VendorModel;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccountPayableForm extends BaseForm {

    private String only = "My AP Accounts";
    private String hold = ConstantsInterface.YES;
    private String ar = ConstantsInterface.NO;
    private String payIds;
    private String holdIds;
    private String deleteIds;
    private VendorModel vendor;
    private FormFile importfile;

    public String getOnly() {
	return only;
    }

    public void setOnly(String only) {
	this.only = only;
    }

    public String getHold() {
	return hold;
    }

    public void setHold(String hold) {
	this.hold = hold;
    }

    public String getAr() {
	return ar;
    }

    public void setAr(String ar) {
	this.ar = ar;
    }

    public String getPayIds() {
	return payIds;
    }

    public void setPayIds(String payIds) {
	this.payIds = payIds;
    }

    public String getHoldIds() {
	return holdIds;
    }

    public void setHoldIds(String holdIds) {
	this.holdIds = holdIds;
    }

    public String getDeleteIds() {
	return deleteIds;
    }

    public void setDeleteIds(String deleteIds) {
	this.deleteIds = deleteIds;
    }

    public VendorModel getVendor() {
	return vendor;
    }

    public void setVendor(VendorModel vendor) {
	this.vendor = vendor;
    }

    public FormFile getImportfile() {
        return importfile;
    }

    public void setImportfile(FormFile importfile) {
        this.importfile = importfile;
    }

 }

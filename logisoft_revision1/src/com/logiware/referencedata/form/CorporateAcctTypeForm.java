/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.form;

import com.gp.cong.logisoft.hibernate.dao.CorporateAccountType;
import java.util.List;

/**
 *
 * @author Mei
 */
public class CorporateAcctTypeForm extends BaseForm {

    private String acctDescription;
    private boolean acctDisabled;
    private String searchDescription;
    private Long corporateAcctId;
    private List<CorporateAccountType> corporateAcctTypeList;

    public String getAcctDescription() {
        return acctDescription;
    }

    public void setAcctDescription(String acctDescription) {
        this.acctDescription = acctDescription;
    }

    public boolean isAcctDisabled() {
        return acctDisabled;
    }

    public void setAcctDisabled(boolean acctDisabled) {
        this.acctDisabled = acctDisabled;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    public Long getCorporateAcctId() {
        return corporateAcctId;
    }

    public void setCorporateAcctId(Long corporateAcctId) {
        this.corporateAcctId = corporateAcctId;
    }

    public List<CorporateAccountType> getCorporateAcctTypeList() {
        return corporateAcctTypeList;
    }

    public void setCorporateAcctTypeList(List<CorporateAccountType> corporateAcctTypeList) {
        this.corporateAcctTypeList = corporateAcctTypeList;
    }
}

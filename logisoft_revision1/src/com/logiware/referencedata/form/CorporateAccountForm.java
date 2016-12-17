/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.form;

import com.gp.cong.logisoft.hibernate.dao.CorporateAccount;
import com.gp.cong.logisoft.hibernate.dao.CorporateAcctTypeDAO;
import java.util.List;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Wsware
 */
public class CorporateAccountForm extends BaseForm {

    private String searchAcctName;
    private String searchCommCode;
    private String searchCommName;
    private String acctName;
    private String commCode;
    private String corporateAcctType;
    private Long corporateAcctId;
    private List<CorporateAccount> corporateAcctList;

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getCommCode() {
        return commCode;
    }

    public void setCommCode(String commCode) {
        this.commCode = commCode;
    }

    public String getSearchAcctName() {
        return searchAcctName;
    }

    public void setSearchAcctName(String searchAcctName) {
        this.searchAcctName = searchAcctName;
    }

    public String getSearchCommCode() {
        return searchCommCode;
    }

    public void setSearchCommCode(String searchCommCode) {
        this.searchCommCode = searchCommCode;
    }

    public String getSearchCommName() {
        return searchCommName;
    }

    public void setSearchCommName(String searchCommName) {
        this.searchCommName = searchCommName;
    }

    public List<CorporateAccount> getCorporateAcctList() {
        return corporateAcctList;
    }

    public void setCorporateAcctList(List<CorporateAccount> corporateAcctList) {
        this.corporateAcctList = corporateAcctList;
    }

    public String getCorporateAcctType() {
        return corporateAcctType;
    }

    public void setCorporateAcctType(String corporateAcctType) {
        this.corporateAcctType = corporateAcctType;
    }

    public Long getCorporateAcctId() {
        return corporateAcctId;
    }

    public void setCorporateAcctId(Long corporateAcctId) {
        this.corporateAcctId = corporateAcctId;
    }

    public List<LabelValueBean> getCorporateAcctTypeList() throws Exception {
        return new CorporateAcctTypeDAO().getCorporateAcctType();
    }
}

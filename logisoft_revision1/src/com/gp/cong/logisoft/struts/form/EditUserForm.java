package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class EditUserForm extends ActionForm {

    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String buttonValue;
    private String telephone;
    private String address1;
    private String address2;
    private String terminal;
    private String importTerminal;
    private String billingTerminal;
    private String city;
    private String zipCode;
    private String loginName;
    private String role;
    private String password;
    private String status;
    private String email;
    private String retypePassword;
    private String extension;
    private String fax;
    private String officeCityLocation;
    private String modifyTest;
    private String login;
    private String userName;
    private boolean achApprover;
    private boolean searchScreenReset;
    private String outsourceEmail;
    private String ctsPackageType;
    private String ctsPalletType;
    private String difflclBookedDimsActual = "0";
    private String ctsAccount;
    private String ctsAccountNo;
    private Double minAmount;
    private Double fuelMarkUp;
    private Double lineMarkUp;
    private Double flatFee;
    private boolean warehouse;
    private FormFile signatureImageOutput;
    private String warehouseNo;
    private String templateId;
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getModifyTest() {
        return modifyTest;
    }

    public void setModifyTest(String modifyTest) {
        this.modifyTest = modifyTest;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOfficeCityLocation() {
        return officeCityLocation;
    }

    public void setOfficeCityLocation(String officeCityLocation) {
        this.officeCityLocation = officeCityLocation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        this.retypePassword = retypePassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getImportTerminal() {
        return importTerminal;
    }

    public void setImportTerminal(String importTerminal) {
        this.importTerminal = importTerminal;
    }

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public boolean isAchApprover() {
        return achApprover;
    }

    public void setAchApprover(boolean achApprover) {
        this.achApprover = achApprover;
    }

    public String getOutsourceEmail() {
        return outsourceEmail;
    }

    public String getDifflclBookedDimsActual() {
        return difflclBookedDimsActual;
    }

    public void setDifflclBookedDimsActual(String difflclBookedDimsActual) {
        this.difflclBookedDimsActual = difflclBookedDimsActual;
    }

    public void setOutsourceEmail(String outsourceEmail) {
        this.outsourceEmail = outsourceEmail;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.achApprover = false;
        this.searchScreenReset = false;
    }

    public boolean isSearchScreenReset() {
        return searchScreenReset;
    }

    public void setSearchScreenReset(boolean searchScreenReset) {
        this.searchScreenReset = searchScreenReset;
    }

    public String getCtsPackageType() {
        return ctsPackageType;
    }

    public void setCtsPackageType(String ctsPackageType) {
        this.ctsPackageType = ctsPackageType;
    }

    public String getCtsPalletType() {
        return ctsPalletType;
    }

    public void setCtsPalletType(String ctsPalletType) {
        this.ctsPalletType = ctsPalletType;
    }

    public String getCtsAccount() {
        return ctsAccount;
    }

    public void setCtsAccount(String ctsAccount) {
        this.ctsAccount = ctsAccount;
    }

    public String getCtsAccountNo() {
        return ctsAccountNo;
    }

    public void setCtsAccountNo(String ctsAccountNo) {
        this.ctsAccountNo = ctsAccountNo;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getFuelMarkUp() {
        return fuelMarkUp;
    }

    public void setFuelMarkUp(Double fuelMarkUp) {
        this.fuelMarkUp = fuelMarkUp;
    }

    public Double getLineMarkUp() {
        return lineMarkUp;
    }

    public void setLineMarkUp(Double lineMarkUp) {
        this.lineMarkUp = lineMarkUp;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public boolean getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(boolean warehouse) {
        this.warehouse = warehouse;
    }

    public FormFile getSignatureImageOutput() {
        return signatureImageOutput;
    }

    public void setSignatureImageOutput(FormFile signatureImageOutput) {
        this.signatureImageOutput = signatureImageOutput;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
}

package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.logisoft.domain.GenericCode;
import com.logiware.fcl.form.BaseForm;
import java.io.Serializable;

/**
 *
 * @author NambuRajasekar
 */
public class MultiQuotesForm extends BaseForm implements Serializable {

    // NEW value
    private String portofDischarge;
    private String isTerminal;
    private String commcode;
    private String hazmat = "N";
    private String fileType; //use ImportFlag
    private String customerName1;
    private String customerName;
    private String clientConsigneeCheck;
    private String newClient;
    private String contactName;
    private String clientNumber;
    private String email;
    private String phone;
    private String fax;
    private String clienttype;
    private String multiQuoteRadioId;
    private boolean convertButtonFlag;
    private String quoteComplete;
    private String clientname;
    //Used Value
    private String methodName;
    private String origin;
    private String destination;
    private String commodity;
    private String ssLine;
    private String selectionInsert;
    private String unitTypes;
    private boolean chargeFlag;
    private String fileNo;
    private String quoteBy;
    private String quoteDate;
    private GenericCode costtype;
    private Integer quoteId;
    private boolean importFlag;
    private String focusValue;
    private String amount;
    private String amount1;
    private String costofgoods;
    private String bulletRatesCheck;
    //Client Search Options
    private String searchClientBy;
    private String clientState;
    private String clientZipCode;
    private String clientSalesCode;
    private boolean displayClientOneLine;
    
    public String getSearchClientBy() {
        return searchClientBy;
    }

    public void setSearchClientBy(String searchClientBy) {
        this.searchClientBy = searchClientBy;
    }

    public String getClientState() {
        return clientState;
    }

    public void setClientState(String clientState) {
        this.clientState = clientState;
    }

    public String getClientZipCode() {
        return clientZipCode;
    }

    public void setClientZipCode(String clientZipCode) {
        this.clientZipCode = clientZipCode;
    }

    public String getClientSalesCode() {
        return clientSalesCode;
    }

    public void setClientSalesCode(String clientSalesCode) {
        this.clientSalesCode = clientSalesCode;
    }

    public boolean isDisplayClientOneLine() {
        return displayClientOneLine;
    }

    public void setDisplayClientOneLine(boolean displayClientOneLine) {
        this.displayClientOneLine = displayClientOneLine;
    }

    public boolean isImportFlag() {
        return importFlag;
    }

    public void setImportFlag(boolean importFlag) {
        this.importFlag = importFlag;
    }

    public String getPortofDischarge() {
        return portofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        this.portofDischarge = portofDischarge;
    }

    public String getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(String isTerminal) {
        this.isTerminal = isTerminal;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCustomerName1() {
        return customerName1;
    }

    public void setCustomerName1(String customerName1) {
        this.customerName1 = customerName1;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getClientConsigneeCheck() {
        return clientConsigneeCheck;
    }

    public void setClientConsigneeCheck(String clientConsigneeCheck) {
        this.clientConsigneeCheck = clientConsigneeCheck;
    }

    public String getNewClient() {
        return newClient;
    }

    public void setNewClient(String newClient) {
        this.newClient = newClient;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getSsLine() {
        return ssLine;
    }

    public void setSsLine(String ssLine) {
        this.ssLine = ssLine;
    }

    public String getSelectionInsert() {
        return selectionInsert;
    }

    public void setSelectionInsert(String selectionInsert) {
        this.selectionInsert = selectionInsert;
    }

    public String getUnitTypes() {
        return unitTypes;
    }

    public void setUnitTypes(String unitTypes) {
        this.unitTypes = unitTypes;
    }

    public boolean isChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(boolean chargeFlag) {
        this.chargeFlag = chargeFlag;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public GenericCode getCosttype() {
        return costtype;
    }

    public void setCosttype(GenericCode costtype) {
        this.costtype = costtype;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getMultiQuoteRadioId() {
        return multiQuoteRadioId;
    }

    public void setMultiQuoteRadioId(String multiQuoteRadioId) {
        this.multiQuoteRadioId = multiQuoteRadioId;
    }

    public boolean getConvertButtonFlag() {
        return convertButtonFlag;
    }

    public void setConvertButtonFlag(boolean convertButtonFlag) {
        this.convertButtonFlag = convertButtonFlag;
    }

    public String getQuoteComplete() {
        return quoteComplete;
    }

    public void setQuoteComplete(String quoteComplete) {
        this.quoteComplete = quoteComplete;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(String quoteBy) {
        this.quoteBy = quoteBy;
    }

    public String getFocusValue() {
        return focusValue;
    }

    public void setFocusValue(String focusValue) {
        this.focusValue = focusValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount1() {
        return amount1;
    }

    public void setAmount1(String amount1) {
        this.amount1 = amount1;
    }

    public String getCostofgoods() {
        return costofgoods;
    }

    public void setCostofgoods(String costofgoods) {
        this.costofgoods = costofgoods;
    }

    public String getBulletRatesCheck() {
        return bulletRatesCheck;
    }

    public void setBulletRatesCheck(String bulletRatesCheck) {
        this.bulletRatesCheck = bulletRatesCheck;
    }

}

package com.gp.cvst.logisoft.struts.form.lcl;


/**
 *
 * @author Rajesh
 */
public class EculineInvoiceForm extends LogiwareActionForm {

    private Integer chargeId;
    private Integer arGlId;
    private Integer apGlId;
    private String containerId;
    private String invoiceNo;
    private String blNo;
    private String chargeCode;//autocompletor
    private String blueChargeCode;
    private String chargeDesc;
    private String apAmount;
    private String arAmount;
    private String methodName;
    private String rowIndex;
    private String eculineChargedesc;
    private String chargeCodes;
    private String costCode;
    private String blueCostCode;
    private String dispute;
    private String isPosted;
    private String chargeAmount;
    private String costAmount;
    private String price;
    private String billToParty;
    private String chargeBillToParty;
    private String hiddenBillToParty;
    private String srcChargeDesc;
    private String mappingSaveAllFlag;
    private String[] ecuChargedesc;
    private String[] ecuBlueChargeCode;
    private String[] ecuBlueCostCode;
    private String[] ecuCostCode;
    private String[] ecuChargeCode;
    private Integer limit = 18;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String fileNumberId;

    public EculineInvoiceForm() {
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEculineChargedesc() {
        return eculineChargedesc;
    }

    public void setEculineChargedesc(String eculineChargedesc) {
        this.eculineChargedesc = eculineChargedesc;
    }

    public String getChargeCodes() {
        return chargeCodes;
    }

    public void setChargeCodes(String chargeCodes) {
        this.chargeCodes = chargeCodes;
    }

    public Integer getArGlId() {
        return arGlId;
    }

    public void setArGlId(Integer arGlId) {
        this.arGlId = arGlId;
    }

    public Integer getApGlId() {
        return apGlId;
    }

    public void setApGlId(Integer apGlId) {
        this.apGlId = apGlId;
    }

    public String getApAmount() {
        return apAmount;
    }

    public void setApAmount(String apAmount) {
        this.apAmount = apAmount;
    }

    public String getArAmount() {
        return arAmount;
    }

    public void setArAmount(String arAmount) {
        this.arAmount = arAmount;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getDispute() {
        return dispute;
    }

    public void setDispute(String dispute) {
        this.dispute = dispute;
    }

    public String getBlueChargeCode() {
        return blueChargeCode;
    }

    public void setBlueChargeCode(String blueChargeCode) {
        this.blueChargeCode = blueChargeCode;
    }

    public String getBlueCostCode() {
        return blueCostCode;
    }

    public void setBlueCostCode(String blueCostCode) {
        this.blueCostCode = blueCostCode;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getChargeBillToParty() {
        return chargeBillToParty;
    }

    public void setChargeBillToParty(String chargeBillToParty) {
        this.chargeBillToParty = chargeBillToParty;
    }

    public String getHiddenBillToParty() {
        return hiddenBillToParty;
    }

    public void setHiddenBillToParty(String hiddenBillToParty) {
        this.hiddenBillToParty = hiddenBillToParty;
    }

    public String getSrcChargeDesc() {
        return srcChargeDesc;
    }

    public void setSrcChargeDesc(String srcChargeDesc) {
        this.srcChargeDesc = srcChargeDesc;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(Integer selectedPage) {
        this.selectedPage = selectedPage;
    }

    public Integer getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(Integer selectedRows) {
        this.selectedRows = selectedRows;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public String getMappingSaveAllFlag() {
        return mappingSaveAllFlag;
    }

    public void setMappingSaveAllFlag(String mappingSaveAllFlag) {
        this.mappingSaveAllFlag = mappingSaveAllFlag;
    }

    public String[] getEcuBlueChargeCode() {
        return ecuBlueChargeCode;
    }

    public void setEcuBlueChargeCode(String[] ecuBlueChargeCode) {
        this.ecuBlueChargeCode = ecuBlueChargeCode;
    }

    public String[] getEcuBlueCostCode() {
        return ecuBlueCostCode;
    }

    public void setEcuBlueCostCode(String[] ecuBlueCostCode) {
        this.ecuBlueCostCode = ecuBlueCostCode;
    }

    public String[] getEcuChargeCode() {
        return ecuChargeCode;
    }

    public void setEcuChargeCode(String[] ecuChargeCode) {
        this.ecuChargeCode = ecuChargeCode;
    }

    public String[] getEcuChargedesc() {
        return ecuChargedesc;
    }

    public void setEcuChargedesc(String[] ecuChargedesc) {
        this.ecuChargedesc = ecuChargedesc;
    }

    public String[] getEcuCostCode() {
        return ecuCostCode;
    }

    public void setEcuCostCode(String[] ecuCostCode) {
        this.ecuCostCode = ecuCostCode;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }
}

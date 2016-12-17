package com.logiware.accounting.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccrualsForm extends BaseForm {

    private String invoiceAmount = "0.00";
    private String allocatedAmount = "0.00";
    private String remainingAmount = "0.00";
    private Integer creditId;
    private Integer creditTerm = 0;
    private String creditDesc = "Due Upon Receipt";
    private String dueDate;
    private boolean reject;
    private boolean dispute;
    private String searchVendorName;
    private String searchVendorNumber;
    private boolean openAccruals = true;
    private String searchBy;
    private String searchValue;
    private String hideAccruals;
    private boolean auto;
    private String ar = ConstantsInterface.NO;
    private String accrualIds;
    private String arIds;
    private String assignedAccrualIds;
    private String disputedAccrualIds;
    private String inactiveAccrualIds;
    private String activeAccrualIds;
    private String deleteAccrualIds;
    private String undeleteAccrualIds;
    private String assignedArIds;
    private String disputedArIds;
    private String sortBy = "reportingDate";
    private String orderBy = "desc";
    private boolean rowsOnly;
    //For Add Accrual
    private String newVendorName;
    private String newVendorNumber;
    private String newInvoiceNumber;
    private String newBlNumber;
    private String newVoyageNumber;
    private String newDockReceipt;
    private String newInvoiceDate;
    private String newAmount;
    private String newCostCode;
    private String newGlAccount;
    private String newShipmentType;
    private String newBluescreenCostCode;
    private String newAccrualId;
    private String newTerminal;
    //For Update Accrual
    private String accrualId;
    private String updateVendorName;
    private String updateVendorNumber;
    private String updateInvoiceNumber;
    private String updateAmount;
    private String updateCostCode;
    private String updateGlAccount;
    private String updateShipmentType;
    private String updateBluescreenCostCode;
    private String updateTerminal;
    private boolean leaveBalance;
    private Integer remainingRows = 0;
    private Integer removedRows = 0;
    private String comments;
    private String from;
    private Integer ediInvoiceId;
    private String ediInvoiceNumber;
    private String fileNo;
    private String inactivateBy;
    private String inactivateVendorName;
    private String inactivateVendorNumber;
    private String fromAmount = "-5.00";
    private String toAmount = "1.00";
    private boolean firstClear;
    /* hidden fields */
    private Long unitId;
    private Long fileId;
    private String loginName;
    private String ssMasterId;
    private String ssMasterBl;

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(String allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getCreditId() throws Exception {
        if (CommonUtils.isEmpty(creditId) || CommonUtils.isEmpty(creditDesc)) {
            creditDesc = "Due Upon Receipt";
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
            GenericCode genericCode = new GenericCodeDAO().getGenericCode(creditDesc, codeTypeId);
            creditId = genericCode.getId();
            creditTerm = Integer.parseInt(genericCode.getCode());
        }
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getCreditTerm() {
        return null != creditTerm ? creditTerm :0;
    }

    public void setCreditTerm(Integer creditTerm) {
        this.creditTerm = creditTerm;
    }

    public String getCreditDesc() throws Exception {
        if (CommonUtils.isEmpty(creditId) || CommonUtils.isEmpty(creditDesc)) {
            creditDesc = "Due Upon Receipt";
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
            GenericCode genericCode = new GenericCodeDAO().getGenericCode(creditDesc, codeTypeId);
            creditId = genericCode.getId();
            creditTerm = Integer.parseInt(genericCode.getCode());
        }
        return creditDesc;
    }

    public void setCreditDesc(String creditDesc) {
        this.creditDesc = creditDesc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public boolean isDispute() {
        return dispute;
    }

    public void setDispute(boolean dispute) {
        this.dispute = dispute;
    }

    public String getSearchVendorName() {
        return searchVendorName;
    }

    public void setSearchVendorName(String searchVendorName) {
        this.searchVendorName = searchVendorName;
    }

    public String getSearchVendorNumber() {
        return searchVendorNumber;
    }

    public void setSearchVendorNumber(String searchVendorNumber) {
        this.searchVendorNumber = searchVendorNumber;
    }

    public boolean isOpenAccruals() {
        return openAccruals;
    }

    public void setOpenAccruals(boolean openAccruals) {
        this.openAccruals = openAccruals;
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

    public String getHideAccruals() {
        return hideAccruals;
    }

    public void setHideAccruals(String hideAccruals) {
        this.hideAccruals = hideAccruals;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getAccrualIds() {
        return accrualIds;
    }

    public void setAccrualIds(String accrualIds) {
        this.accrualIds = accrualIds;
    }

    public String getArIds() {
        return arIds;
    }

    public void setArIds(String arIds) {
        this.arIds = arIds;
    }

    public String getAssignedAccrualIds() {
        return assignedAccrualIds;
    }

    public void setAssignedAccrualIds(String assignedAccrualIds) {
        this.assignedAccrualIds = assignedAccrualIds;
    }

    public String getDisputedAccrualIds() {
        return disputedAccrualIds;
    }

    public void setDisputedAccrualIds(String disputedAccrualIds) {
        this.disputedAccrualIds = disputedAccrualIds;
    }

    public String getInactiveAccrualIds() {
        return inactiveAccrualIds;
    }

    public void setInactiveAccrualIds(String inactiveAccrualIds) {
        this.inactiveAccrualIds = inactiveAccrualIds;
    }

    public String getDeleteAccrualIds() {
        return deleteAccrualIds;
    }

    public void setDeleteAccrualIds(String deleteAccrualIds) {
        this.deleteAccrualIds = deleteAccrualIds;
    }

    public String getActiveAccrualIds() {
        return activeAccrualIds;
    }

    public void setActiveAccrualIds(String activeAccrualIds) {
        this.activeAccrualIds = activeAccrualIds;
    }

    public String getUndeleteAccrualIds() {
        return undeleteAccrualIds;
    }

    public void setUndeleteAccrualIds(String undeleteAccrualIds) {
        this.undeleteAccrualIds = undeleteAccrualIds;
    }

    public String getAssignedArIds() {
        return assignedArIds;
    }

    public void setAssignedArIds(String assignedArIds) {
        this.assignedArIds = assignedArIds;
    }

    public String getDisputedArIds() {
        return disputedArIds;
    }

    public void setDisputedArIds(String disputedArIds) {
        this.disputedArIds = disputedArIds;
    }

    @Override
    public String getSortBy() {
        return this.sortBy;
    }

    @Override
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public String getOrderBy() {
        return this.orderBy;
    }

    @Override
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isRowsOnly() {
        return rowsOnly;
    }

    public void setRowsOnly(boolean rowsOnly) {
        this.rowsOnly = rowsOnly;
    }

    public String getNewVendorName() {
        return newVendorName;
    }

    public void setNewVendorName(String newVendorName) {
        this.newVendorName = newVendorName;
    }

    public String getNewVendorNumber() {
        return newVendorNumber;
    }

    public void setNewVendorNumber(String newVendorNumber) {
        this.newVendorNumber = newVendorNumber;
    }

    public String getNewInvoiceNumber() {
        return newInvoiceNumber;
    }

    public void setNewInvoiceNumber(String newInvoiceNumber) {
        this.newInvoiceNumber = newInvoiceNumber;
    }

    public String getNewBlNumber() {
        return newBlNumber;
    }

    public void setNewBlNumber(String newBlNumber) {
        this.newBlNumber = newBlNumber;
    }

    public String getNewVoyageNumber() {
        return newVoyageNumber;
    }

    public void setNewVoyageNumber(String newVoyageNumber) {
        this.newVoyageNumber = newVoyageNumber;
    }

    public String getNewDockReceipt() {
        return newDockReceipt;
    }

    public void setNewDockReceipt(String newDockReceipt) {
        this.newDockReceipt = newDockReceipt;
    }

    public String getNewInvoiceDate() {
        return newInvoiceDate;
    }

    public void setNewInvoiceDate(String newInvoiceDate) {
        this.newInvoiceDate = newInvoiceDate;
    }

    public String getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(String newAmount) {
        this.newAmount = newAmount;
    }

    public String getNewCostCode() {
        return newCostCode;
    }

    public void setNewCostCode(String newCostCode) {
        this.newCostCode = newCostCode;
    }

    public String getNewGlAccount() {
        return newGlAccount;
    }

    public void setNewGlAccount(String newGlAccount) {
        this.newGlAccount = newGlAccount;
    }

    public String getNewShipmentType() {
        return newShipmentType;
    }

    public void setNewShipmentType(String newShipmentType) {
        this.newShipmentType = newShipmentType;
    }

    public String getNewBluescreenCostCode() {
        return newBluescreenCostCode;
    }

    public void setNewBluescreenCostCode(String newBluescreenCostCode) {
        this.newBluescreenCostCode = newBluescreenCostCode;
    }

    public String getNewAccrualId() {
        return newAccrualId;
    }

    public void setNewAccrualId(String newAccrualId) {
        this.newAccrualId = newAccrualId;
    }

    public String getAccrualId() {
        return accrualId;
    }

    public void setAccrualId(String accrualId) {
        this.accrualId = accrualId;
    }

    public String getUpdateVendorName() {
        return updateVendorName;
    }

    public void setUpdateVendorName(String updateVendorName) {
        this.updateVendorName = updateVendorName;
    }

    public String getUpdateVendorNumber() {
        return updateVendorNumber;
    }

    public void setUpdateVendorNumber(String updateVendorNumber) {
        this.updateVendorNumber = updateVendorNumber;
    }

    public String getUpdateInvoiceNumber() {
        return updateInvoiceNumber;
    }

    public void setUpdateInvoiceNumber(String updateInvoiceNumber) {
        this.updateInvoiceNumber = updateInvoiceNumber;
    }

    public String getUpdateAmount() {
        return updateAmount;
    }

    public void setUpdateAmount(String updateAmount) {
        this.updateAmount = updateAmount;
    }

    public String getUpdateCostCode() {
        return updateCostCode;
    }

    public void setUpdateCostCode(String updateCostCode) {
        this.updateCostCode = updateCostCode;
    }

    public String getUpdateGlAccount() {
        return updateGlAccount;
    }

    public void setUpdateGlAccount(String updateGlAccount) {
        this.updateGlAccount = updateGlAccount;
    }

    public String getUpdateShipmentType() {
        return updateShipmentType;
    }

    public void setUpdateShipmentType(String updateShipmentType) {
        this.updateShipmentType = updateShipmentType;
    }

    public String getUpdateBluescreenCostCode() {
        return updateBluescreenCostCode;
    }

    public void setUpdateBluescreenCostCode(String updateBluescreenCostCode) {
        this.updateBluescreenCostCode = updateBluescreenCostCode;
    }

    public String getUpdateTerminal() {
        return updateTerminal;
    }

    public void setUpdateTerminal(String updateTerminal) {
        this.updateTerminal = updateTerminal;
    }

    public boolean isLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(boolean leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public Integer getRemainingRows() {
        return remainingRows;
    }

    public void setRemainingRows(Integer remainingRows) {
        this.remainingRows = remainingRows;
    }

    public Integer getRemovedRows() {
        return removedRows;
    }

    public void setRemovedRows(Integer removedRows) {
        this.removedRows = removedRows;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getEdiInvoiceId() {
        return ediInvoiceId;
    }

    public void setEdiInvoiceId(Integer ediInvoiceId) {
        this.ediInvoiceId = ediInvoiceId;
    }

    public String getEdiInvoiceNumber() {
        return ediInvoiceNumber;
    }

    public void setEdiInvoiceNumber(String ediInvoiceNumber) {
        this.ediInvoiceNumber = ediInvoiceNumber;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getInactivateBy() {
        return inactivateBy;
    }

    public void setInactivateBy(String inactivateBy) {
        this.inactivateBy = inactivateBy;
    }

    public String getInactivateVendorName() {
        return inactivateVendorName;
    }

    public void setInactivateVendorName(String inactivateVendorName) {
        this.inactivateVendorName = inactivateVendorName;
    }

    public String getInactivateVendorNumber() {
        return inactivateVendorNumber;
    }

    public void setInactivateVendorNumber(String inactivateVendorNumber) {
        this.inactivateVendorNumber = inactivateVendorNumber;
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

    public boolean isFirstClear() {
        return firstClear;
    }

    public void setFirstClear(boolean firstClear) {
        this.firstClear = firstClear;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getNewTerminal() {
        return newTerminal;
    }

    public void setNewTerminal(String newTerminal) {
        this.newTerminal = newTerminal;
    }
    
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSsMasterId() {
        return ssMasterId;
    }

    public void setSsMasterId(String ssMasterId) {
        this.ssMasterId = ssMasterId;
    }

    public String getSsMasterBl() {
        return ssMasterBl;
    }

    public void setSsMasterBl(String ssMasterBl) {
        this.ssMasterBl = ssMasterBl;
    }
    
    public List<LabelValueBean> getSearchByTypes() {
        List<LabelValueBean> searchByTypes = new ArrayList<LabelValueBean>();
        searchByTypes.add(new LabelValueBean("Select", ""));
        searchByTypes.add(new LabelValueBean("Invoice Number", "invoice_number"));
        searchByTypes.add(new LabelValueBean("Container Number", "container_no"));
        searchByTypes.add(new LabelValueBean("Dock Receipt", "drcpt"));
        searchByTypes.add(new LabelValueBean("House Bill", "bill_ladding_no"));
        searchByTypes.add(new LabelValueBean("Sub-House Bill", "sub_house_bl"));
        searchByTypes.add(new LabelValueBean("Voyage", "voyage_no"));
        searchByTypes.add(new LabelValueBean("Master Bill", "master_bl"));
        searchByTypes.add(new LabelValueBean("Booking Number", "booking_no"));
        return Collections.unmodifiableList(searchByTypes);
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.reject = false;
        this.dispute = false;
        this.auto = false;
    }
}

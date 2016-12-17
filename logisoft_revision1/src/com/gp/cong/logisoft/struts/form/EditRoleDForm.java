/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vinay
 */
public class EditRoleDForm extends ActionForm {

    private int id;
    private String roleName;
    private String action;
    private boolean accessVoidButton;
    private boolean apSpec;
    private boolean arInqMngr;
    private boolean batchAccMngr;
    private boolean creditHolder;
    private boolean supervisor;
    private boolean usrLevelRoles;
    private boolean accessDisputedBlNotesAndAck;
    private boolean audit;
    private boolean cancelAudit;
    private boolean unmanifest;
    private boolean reopenBl;
    private boolean closeBl;
    private boolean revCorrections;
    private boolean postCorrections;
    private boolean disableOrEnableTp;
    private boolean showDetailedCharges;
    private boolean vendorOtherthanFF;
    private boolean editEciAcct;
    private boolean addPredefinedRemarks;
    private boolean accessCorrectionPrintFax;
    private boolean changeTpType;
    private boolean changeMaster;
    private boolean takeOwnershipOfDisputedBL;
    private int roleId;
    private boolean arInquiryChangeCustomer;
    private boolean arInquiryMakeAdjustments;
    private boolean arBatchShowallUsersBatch;
    private boolean arBatchDirectGlAccount;
    private boolean accrualsCreateNew;
    private boolean bankAccountCreateNew;
    private boolean journalEntryClosedPeriod;
    private boolean tpSetVendorType;
    private boolean tpShowAddress;
    private boolean tpShowGeneralInfo;
    private boolean tpShowArConfig;
    private boolean tpShowApConfig;
    private boolean tpShowContactConfig;
    private boolean tpShowConsigneeInfo;
    private boolean resendAes;
    private boolean viewAccountingScanAttach;
    private boolean resendAccruals;
    private boolean checkRegisterController;
    private boolean allowRoutedAgent;
    private boolean deleteLclCommodity;
    private boolean showFollowUpTasks;
    private boolean displayDefaults;
    private boolean auditOverride;
    private boolean sslPrepaidCollect;
    private boolean byPassVoyage;
    private boolean editLclBlOwner;
    private boolean deleteVoyage;
    private boolean terminateWithoutInvoice;
    private boolean arBatchReversal;
    private boolean apPayment;
    private boolean reversePostedInvoices;
    private boolean creditHoldOpsUser;
    private boolean inactivateAccruals;
    private boolean openLclUnit;
    private boolean unmanifestLclUnit;
    private boolean lclCurrentLocation;
    private boolean unPost;
    private boolean changeVoyage;
    private boolean bookingTerminate;
    private boolean tpShowCtsConfig;
    private boolean lclVoyageOwner;//LCL Voyage Owner
    private boolean ecuDesignation;
    private boolean deleteDisposition;
    private boolean lclEcuInvoiceMapp;
    private boolean deleteImportContainers;
    private boolean deleteImportsUnit;
    private boolean lclImportVoyageClose;
    private boolean lclImportVoyageReopen;
    private boolean lclImportVoyageAudit;
    private boolean deleteCostandCharges;
    private boolean lclUnitOSD;
    private boolean allowtoEnterSpotRate;
    private boolean importsVoyagePod;
    private boolean deleteAttachedDocuments;
    private boolean editDeferralCharge;
    private boolean arConfigTabReadOnly;
    private boolean allowImportCfsVendor;
    private boolean deleteManualNotes;
    private boolean changeSalesCode;
    private boolean linkDrAfterDispositionPort;
    private boolean changeLogoPreference;
    private boolean disabledContainerwithAPcosts;
    private boolean showUncompleteUnits;
    private boolean lclExpVoyageOwner;
    private boolean expDeleteVoyage;
    private boolean reverseCob;
    private boolean voidLCLBLafterCOB;
    private boolean deleteNotes;
    private boolean addTemplates;
    private boolean aesRequiredForReleasingDRs;
    private boolean aesRequiredForPostingBLs;
    private boolean voyageCloseAuditUndo;
    private boolean manageECIAccountLink;
    private boolean batchHsCode;
    private boolean warehouseQuickBkg;
    private boolean bkgVoyageReleaseDr;
    private boolean lclBookingContact;
    private boolean lclManifestPostedBl;
    private boolean weightChangeAfterRelease;
    private boolean preventExpRelease;
    private boolean deleteUnits;
    private boolean removeDrHold;
    private boolean no997EdiSubmission;
    private boolean bypassRelayCheck;
    private boolean defaultLoadAllReleased;
    private boolean lclQuoteClient;
    private boolean pickDrWarnings;
    private boolean changeBLCommodityAfterCOB;
    private boolean changeDefaultFF;
    private boolean defaultDocsRcvd;
    private boolean defaultNoeeiLowVal;
    private boolean allowChangeDisposition;
    private boolean batchHotCode;
    private boolean lclBookingDefaultERT;
    private boolean lclImportAllowTransshipment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isAccessVoidButton() {
        return accessVoidButton;
    }

    public void setAccessVoidButton(boolean accessVoidButton) {
        this.accessVoidButton = accessVoidButton;
    }

    public boolean isApSpec() {
        return apSpec;
    }

    public void setApSpec(boolean apSpec) {
        this.apSpec = apSpec;
    }

    public boolean isArInqMngr() {
        return arInqMngr;
    }

    public void setArInqMngr(boolean arInqMngr) {
        this.arInqMngr = arInqMngr;
    }

    public boolean isBatchAccMngr() {
        return batchAccMngr;
    }

    public void setBatchAccMngr(boolean batchAccMngr) {
        this.batchAccMngr = batchAccMngr;
    }

    public boolean isCreditHolder() {
        return creditHolder;
    }

    public void setCreditHolder(boolean creditHolder) {
        this.creditHolder = creditHolder;
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isUsrLevelRoles() {
        return usrLevelRoles;
    }

    public void setUsrLevelRoles(boolean usrLevelRoles) {
        this.usrLevelRoles = usrLevelRoles;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isAccessDisputedBlNotesAndAck() {
        return accessDisputedBlNotesAndAck;
    }

    public void setAccessDisputedBlNotesAndAck(boolean accessDisputedBlNotesAndAck) {
        this.accessDisputedBlNotesAndAck = accessDisputedBlNotesAndAck;
    }

    public boolean isReopenBl() {
        return reopenBl;
    }

    public void setReopenBl(boolean reopenBl) {
        this.reopenBl = reopenBl;
    }

    public boolean isCloseBl() {
        return closeBl;
    }

    public void setCloseBl(boolean closeBl) {
        this.closeBl = closeBl;
    }

    public boolean isUnmanifest() {
        return unmanifest;
    }

    public void setUnmanifest(boolean unmanifest) {
        this.unmanifest = unmanifest;
    }

    public boolean isDisableOrEnableTp() {
        return disableOrEnableTp;
    }

    public void setDisableOrEnableTp(boolean disableOrEnableTp) {
        this.disableOrEnableTp = disableOrEnableTp;
    }

    public boolean isShowDetailedCharges() {
        return showDetailedCharges;
    }

    public void setShowDetailedCharges(boolean showDetailedCharges) {
        this.showDetailedCharges = showDetailedCharges;
    }

    public boolean isPostCorrections() {
        return postCorrections;
    }

    public void setPostCorrections(boolean postCorrections) {
        this.postCorrections = postCorrections;
    }

    public boolean isRevCorrections() {
        return revCorrections;
    }

    public void setRevCorrections(boolean revCorrections) {
        this.revCorrections = revCorrections;
    }

    public boolean isAddPredefinedRemarks() {
        return addPredefinedRemarks;
    }

    public void setAddPredefinedRemarks(boolean addPredefinedRemarks) {
        this.addPredefinedRemarks = addPredefinedRemarks;
    }

    public boolean isEditEciAcct() {
        return editEciAcct;
    }

    public void setEditEciAcct(boolean editEciAcct) {
        this.editEciAcct = editEciAcct;
    }

    public boolean isVendorOtherthanFF() {
        return vendorOtherthanFF;
    }

    public void setVendorOtherthanFF(boolean vendorOtherthanFF) {
        this.vendorOtherthanFF = vendorOtherthanFF;
    }

    public boolean isAccessCorrectionPrintFax() {
        return accessCorrectionPrintFax;
    }

    public void setAccessCorrectionPrintFax(boolean accessCorrectionPrintFax) {
        this.accessCorrectionPrintFax = accessCorrectionPrintFax;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.accessVoidButton = false;
        this.apSpec = false;
        this.arInqMngr = false;
        this.batchAccMngr = false;
        this.creditHolder = false;
        this.supervisor = false;
        this.usrLevelRoles = false;
        this.accessDisputedBlNotesAndAck = false;
        this.audit = false;
        this.cancelAudit = false;
        this.unmanifest = false;
        this.reopenBl = false;
        this.closeBl = false;
        this.revCorrections = false;
        this.postCorrections = false;
        this.showDetailedCharges = false;
        this.disableOrEnableTp = false;
        this.vendorOtherthanFF = false;
        this.editEciAcct = false;
        this.addPredefinedRemarks = false;
        this.accessCorrectionPrintFax = false;
        this.changeTpType = false;
        this.changeMaster = false;
        this.takeOwnershipOfDisputedBL = false;
        this.arInquiryChangeCustomer = false;
        this.arInquiryMakeAdjustments = false;
        this.arBatchShowallUsersBatch = false;
        this.arBatchDirectGlAccount = false;
        this.accrualsCreateNew = false;
        this.bankAccountCreateNew = false;
        this.journalEntryClosedPeriod = false;
        this.tpSetVendorType = false;
        this.tpShowAddress = false;
        this.tpShowGeneralInfo = false;
        this.tpShowArConfig = false;
        this.tpShowApConfig = false;
        this.tpShowContactConfig = false;
        this.tpShowConsigneeInfo = false;
        this.checkRegisterController = false;
        this.deleteLclCommodity = false;
        this.showFollowUpTasks = false;
        this.displayDefaults = false;
        this.auditOverride = false;
        this.byPassVoyage = false;
        this.editLclBlOwner = false;
        this.deleteVoyage = false;
        this.openLclUnit = false;
        this.terminateWithoutInvoice = false;
        this.arBatchReversal = false;
        this.apPayment = false;
        this.reversePostedInvoices = false;
        this.creditHoldOpsUser = false;
        this.inactivateAccruals = false;
        this.unPost = false;
        this.changeVoyage = false;
        this.bookingTerminate = false;
        this.lclVoyageOwner = false;
        this.tpShowCtsConfig = false;
        this.ecuDesignation = false;
        this.deleteDisposition = false;
        this.lclEcuInvoiceMapp = false;
        this.deleteImportsUnit = false;
        this.lclUnitOSD = false;
        this.lclImportVoyageClose = false;
        this.lclImportVoyageReopen = false;
        this.lclImportVoyageAudit = false;
        this.allowtoEnterSpotRate = false;
        this.deleteAttachedDocuments = false;
        this.editDeferralCharge = false;
        this.arConfigTabReadOnly = false;
        this.allowImportCfsVendor = false;
        this.deleteManualNotes = false;
        this.changeSalesCode = false;
        this.changeLogoPreference = false;
        this.reverseCob = false;
        this.voyageCloseAuditUndo = false;
        this.batchHsCode = false;
        this.changeLogoPreference = false;
        this.reverseCob = false;
        this.voyageCloseAuditUndo = false;
        this.weightChangeAfterRelease = false;
        this.preventExpRelease=false;
        this.deleteUnits=false;
        this.lclImportAllowTransshipment=false;
      //  this.updateWeightMeasureAfterCOB=false;
    }

    public boolean isChangeMaster() {
        return changeMaster;
    }

    public void setChangeMaster(boolean changeMaster) {
        this.changeMaster = changeMaster;
    }

    public boolean isChangeTpType() {
        return changeTpType;
    }

    public void setChangeTpType(boolean changeTpType) {
        this.changeTpType = changeTpType;
    }

    public boolean isTakeOwnershipOfDisputedBL() {
        return takeOwnershipOfDisputedBL;
    }

    public void setTakeOwnershipOfDisputedBL(boolean takeOwnershipOfDisputedBL) {
        this.takeOwnershipOfDisputedBL = takeOwnershipOfDisputedBL;
    }

    public boolean isAccrualsCreateNew() {
        return accrualsCreateNew;
    }

    public void setAccrualsCreateNew(boolean accrualsCreateNew) {
        this.accrualsCreateNew = accrualsCreateNew;
    }

    public boolean isArBatchShowallUsersBatch() {
        return arBatchShowallUsersBatch;
    }

    public void setArBatchShowallUsersBatch(boolean arBatchShowallUsersBatch) {
        this.arBatchShowallUsersBatch = arBatchShowallUsersBatch;
    }

    public boolean isArBatchDirectGlAccount() {
        return arBatchDirectGlAccount;
    }

    public void setArBatchDirectGlAccount(boolean arBatchDirectGlAccount) {
        this.arBatchDirectGlAccount = arBatchDirectGlAccount;
    }

    public boolean isArInquiryChangeCustomer() {
        return arInquiryChangeCustomer;
    }

    public void setArInquiryChangeCustomer(boolean arInquiryChangeCustomer) {
        this.arInquiryChangeCustomer = arInquiryChangeCustomer;
    }

    public boolean isArInquiryMakeAdjustments() {
        return arInquiryMakeAdjustments;
    }

    public void setArInquiryMakeAdjustments(boolean arInquiryMakeAdjustments) {
        this.arInquiryMakeAdjustments = arInquiryMakeAdjustments;
    }

    public boolean isBankAccountCreateNew() {
        return bankAccountCreateNew;
    }

    public void setBankAccountCreateNew(boolean bankAccountCreateNew) {
        this.bankAccountCreateNew = bankAccountCreateNew;
    }

    public boolean isJournalEntryClosedPeriod() {
        return journalEntryClosedPeriod;
    }

    public void setJournalEntryClosedPeriod(boolean journalEntryClosedPeriod) {
        this.journalEntryClosedPeriod = journalEntryClosedPeriod;
    }

    public boolean isTpSetVendorType() {
        return tpSetVendorType;
    }

    public void setTpSetVendorType(boolean tpSetVendorType) {
        this.tpSetVendorType = tpSetVendorType;
    }

    public boolean isTpShowAddress() {
        return tpShowAddress;
    }

    public void setTpShowAddress(boolean tpShowAddress) {
        this.tpShowAddress = tpShowAddress;
    }

    public boolean isTpShowApConfig() {
        return tpShowApConfig;
    }

    public void setTpShowApConfig(boolean tpShowApConfig) {
        this.tpShowApConfig = tpShowApConfig;
    }

    public boolean isTpShowArConfig() {
        return tpShowArConfig;
    }

    public void setTpShowArConfig(boolean tpShowArConfig) {
        this.tpShowArConfig = tpShowArConfig;
    }

    public boolean isTpShowConsigneeInfo() {
        return tpShowConsigneeInfo;
    }

    public void setTpShowConsigneeInfo(boolean tpShowConsigneeInfo) {
        this.tpShowConsigneeInfo = tpShowConsigneeInfo;
    }

    public boolean isTpShowContactConfig() {
        return tpShowContactConfig;
    }

    public void setTpShowContactConfig(boolean tpShowContactConfig) {
        this.tpShowContactConfig = tpShowContactConfig;
    }

    public boolean isTpShowGeneralInfo() {
        return tpShowGeneralInfo;
    }

    public void setTpShowGeneralInfo(boolean tpShowGeneralInfo) {
        this.tpShowGeneralInfo = tpShowGeneralInfo;
    }

    public boolean isResendAes() {
        return resendAes;
    }

    public void setResendAes(boolean resendAes) {
        this.resendAes = resendAes;
    }

    public boolean isViewAccountingScanAttach() {
        return viewAccountingScanAttach;
    }

    public void setViewAccountingScanAttach(boolean viewAccountingScanAttach) {
        this.viewAccountingScanAttach = viewAccountingScanAttach;
    }

    public boolean isAudit() {
        return audit;
    }

    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public boolean isCancelAudit() {
        return cancelAudit;
    }

    public void setCancelAudit(boolean cancelAudit) {
        this.cancelAudit = cancelAudit;
    }

    public boolean isResendAccruals() {
        return resendAccruals;
    }

    public void setResendAccruals(boolean resendAccruals) {
        this.resendAccruals = resendAccruals;
    }

    public boolean isCheckRegisterController() {
        return checkRegisterController;
    }

    public void setCheckRegisterController(boolean checkRegisterController) {
        this.checkRegisterController = checkRegisterController;
    }

    public boolean isAllowRoutedAgent() {
        return allowRoutedAgent;
    }

    public void setAllowRoutedAgent(boolean allowRoutedAgent) {
        this.allowRoutedAgent = allowRoutedAgent;
    }

    public boolean isDeleteLclCommodity() {
        return deleteLclCommodity;
    }

    public void setDeleteLclCommodity(boolean deleteLclCommodity) {
        this.deleteLclCommodity = deleteLclCommodity;
    }

    public boolean isByPassVoyage() {
        return byPassVoyage;
    }

    public void setByPassVoyage(boolean byPassVoyage) {
        this.byPassVoyage = byPassVoyage;
    }

    public boolean isEditLclBlOwner() {
        return editLclBlOwner;
    }

    public void setEditLclBlOwner(boolean editLclBlOwner) {
        this.editLclBlOwner = editLclBlOwner;
    }

    public boolean isTerminateWithoutInvoice() {
        return terminateWithoutInvoice;
    }

    public void setTerminateWithoutInvoice(boolean terminateWithoutInvoice) {
        this.terminateWithoutInvoice = terminateWithoutInvoice;
    }

    public boolean isShowFollowUpTasks() {
        return showFollowUpTasks;
    }

    public void setShowFollowUpTasks(boolean showFollowUpTasks) {
        this.showFollowUpTasks = showFollowUpTasks;
    }

    public boolean isDisplayDefaults() {
        return displayDefaults;
    }

    public void setDisplayDefaults(boolean displayDefaults) {
        this.displayDefaults = displayDefaults;
    }

    public boolean isAuditOverride() {
        return auditOverride;
    }

    public void setAuditOverride(boolean auditOverride) {
        this.auditOverride = auditOverride;
    }

    public boolean isSslPrepaidCollect() {
        return sslPrepaidCollect;
    }

    public void setSslPrepaidCollect(boolean sslPrepaidCollect) {
        this.sslPrepaidCollect = sslPrepaidCollect;
    }

    public boolean isArBatchReversal() {
        return arBatchReversal;
    }

    public void setArBatchReversal(boolean arBatchReversal) {
        this.arBatchReversal = arBatchReversal;
    }

    public boolean isApPayment() {
        return apPayment;
    }

    public void setApPayment(boolean apPayment) {
        this.apPayment = apPayment;
    }

    public boolean isReversePostedInvoices() {
        return reversePostedInvoices;
    }

    public void setReversePostedInvoices(boolean reversePostedInvoices) {
        this.reversePostedInvoices = reversePostedInvoices;
    }

    public boolean isCreditHoldOpsUser() {
        return creditHoldOpsUser;
    }

    public void setCreditHoldOpsUser(boolean creditHoldOpsUser) {
        this.creditHoldOpsUser = creditHoldOpsUser;
    }

    public boolean isInactivateAccruals() {
        return inactivateAccruals;
    }

    public void setInactivateAccruals(boolean inactivateAccruals) {
        this.inactivateAccruals = inactivateAccruals;
    }

    public boolean isDeleteVoyage() {
        return deleteVoyage;
    }

    public void setDeleteVoyage(boolean deleteVoyage) {
        this.deleteVoyage = deleteVoyage;
    }

    public boolean isOpenLclUnit() {
        return openLclUnit;
    }

    public void setOpenLclUnit(boolean openLclUnit) {
        this.openLclUnit = openLclUnit;
    }

    public boolean isUnmanifestLclUnit() {
        return unmanifestLclUnit;
    }

    public void setUnmanifestLclUnit(boolean unmanifestLclUnit) {
        this.unmanifestLclUnit = unmanifestLclUnit;
    }

    public boolean isUnPost() {
        return unPost;
    }

    public void setUnPost(boolean unPost) {
        this.unPost = unPost;
    }

    public boolean isLclCurrentLocation() {
        return lclCurrentLocation;
    }

    public void setLclCurrentLocation(boolean lclCurrentLocation) {
        this.lclCurrentLocation = lclCurrentLocation;
    }

    public boolean isChangeVoyage() {
        return changeVoyage;
    }

    public void setChangeVoyage(boolean changeVoyage) {
        this.changeVoyage = changeVoyage;
    }

    public boolean isBookingTerminate() {
        return bookingTerminate;
    }

    public void setBookingTerminate(boolean bookingTerminate) {
        this.bookingTerminate = bookingTerminate;
    }

    public boolean isLclVoyageOwner() {
        return lclVoyageOwner;
    }

    public void setLclVoyageOwner(boolean lclVoyageOwner) {
        this.lclVoyageOwner = lclVoyageOwner;
    }

    public boolean isTpShowCtsConfig() {
        return tpShowCtsConfig;
    }

    public void setTpShowCtsConfig(boolean tpShowCtsConfig) {
        this.tpShowCtsConfig = tpShowCtsConfig;
    }

    public boolean isEcuDesignation() {
        return ecuDesignation;
    }

    public void setEcuDesignation(boolean ecuDesignation) {
        this.ecuDesignation = ecuDesignation;
    }

    public boolean isDeleteDisposition() {
        return deleteDisposition;
    }

    public void setDeleteDisposition(boolean deleteDisposition) {
        this.deleteDisposition = deleteDisposition;
    }

    public boolean isDeleteImportContainers() {
        return deleteImportContainers;
    }

    public void setDeleteImportContainers(boolean deleteImportContainers) {
        this.deleteImportContainers = deleteImportContainers;
    }

    public boolean isDeleteImportsUnit() {
        return deleteImportsUnit;
    }

    public void setDeleteImportsUnit(boolean deleteImportsUnit) {
        this.deleteImportsUnit = deleteImportsUnit;
    }

    public boolean isLclImportVoyageAudit() {
        return lclImportVoyageAudit;
    }

    public void setLclImportVoyageAudit(boolean lclImportVoyageAudit) {
        this.lclImportVoyageAudit = lclImportVoyageAudit;
    }

    public boolean isLclImportVoyageClose() {
        return lclImportVoyageClose;
    }

    public void setLclImportVoyageClose(boolean lclImportVoyageClose) {
        this.lclImportVoyageClose = lclImportVoyageClose;
    }

    public boolean isLclImportVoyageReopen() {
        return lclImportVoyageReopen;
    }

    public void setLclImportVoyageReopen(boolean lclImportVoyageReopen) {
        this.lclImportVoyageReopen = lclImportVoyageReopen;
    }

    public boolean isDeleteCostandCharges() {
        return deleteCostandCharges;
    }

    public void setDeleteCostandCharges(boolean deleteCostandCharges) {
        this.deleteCostandCharges = deleteCostandCharges;
    }

    public boolean isLclUnitOSD() {
        return lclUnitOSD;
    }

    public void setLclUnitOSD(boolean lclUnitOSD) {
        this.lclUnitOSD = lclUnitOSD;
    }

    public boolean isLclEcuInvoiceMapp() {
        return lclEcuInvoiceMapp;
    }

    public void setLclEcuInvoiceMapp(boolean lclEcuInvoiceMapp) {
        this.lclEcuInvoiceMapp = lclEcuInvoiceMapp;
    }

    public boolean isAllowtoEnterSpotRate() {
        return allowtoEnterSpotRate;
    }

    public void setAllowtoEnterSpotRate(boolean allowtoEnterSpotRate) {
        this.allowtoEnterSpotRate = allowtoEnterSpotRate;
    }

    public boolean isImportsVoyagePod() {
        return importsVoyagePod;
    }

    public void setImportsVoyagePod(boolean importsVoyagePod) {
        this.importsVoyagePod = importsVoyagePod;
    }

    public boolean isDeleteAttachedDocuments() {
        return deleteAttachedDocuments;
    }

    public void setDeleteAttachedDocuments(boolean deleteAttachedDocuments) {
        this.deleteAttachedDocuments = deleteAttachedDocuments;
    }

    public boolean isEditDeferralCharge() {
        return editDeferralCharge;
    }

    public void setEditDeferralCharge(boolean editDeferralCharge) {
        this.editDeferralCharge = editDeferralCharge;
    }

    public boolean isArConfigTabReadOnly() {
        return arConfigTabReadOnly;
    }

    public void setArConfigTabReadOnly(boolean arConfigTabReadOnly) {
        this.arConfigTabReadOnly = arConfigTabReadOnly;
    }

    public boolean isAllowImportCfsVendor() {
        return allowImportCfsVendor;
    }

    public void setAllowImportCfsVendor(boolean allowImportCfsVendor) {
        this.allowImportCfsVendor = allowImportCfsVendor;
    }

    public boolean isDeleteManualNotes() {
        return deleteManualNotes;
    }

    public void setDeleteManualNotes(boolean deleteManualNotes) {
        this.deleteManualNotes = deleteManualNotes;
    }

    public boolean isChangeSalesCode() {
        return changeSalesCode;
    }

    public void setChangeSalesCode(boolean changeSalesCode) {
        this.changeSalesCode = changeSalesCode;
    }

    public boolean isLinkDrAfterDispositionPort() {
        return linkDrAfterDispositionPort;
    }

    public void setLinkDrAfterDispositionPort(boolean linkDrAfterDispositionPort) {
        this.linkDrAfterDispositionPort = linkDrAfterDispositionPort;
    }

    public boolean isChangeLogoPreference() {
        return changeLogoPreference;
    }

    public void setChangeLogoPreference(boolean changeLogoPreference) {
        this.changeLogoPreference = changeLogoPreference;
    }

    public boolean isDisabledContainerwithAPcosts() {
        return disabledContainerwithAPcosts;
    }

    public void setDisabledContainerwithAPcosts(boolean disabledContainerwithAPcosts) {
        this.disabledContainerwithAPcosts = disabledContainerwithAPcosts;
    }

    public boolean isShowUncompleteUnits() {
        return showUncompleteUnits;
    }

    public void setShowUncompleteUnits(boolean showUncompleteUnits) {
        this.showUncompleteUnits = showUncompleteUnits;
    }

    public boolean isLclExpVoyageOwner() {
        return lclExpVoyageOwner;
    }

    public void setLclExpVoyageOwner(boolean lclExpVoyageOwner) {
        this.lclExpVoyageOwner = lclExpVoyageOwner;
    }

    public boolean isExpDeleteVoyage() {
        return expDeleteVoyage;
    }

    public void setExpDeleteVoyage(boolean expDeleteVoyage) {
        this.expDeleteVoyage = expDeleteVoyage;
    }

    public boolean isReverseCob() {
        return reverseCob;
    }

    public void setReverseCob(boolean reverseCob) {
        this.reverseCob = reverseCob;
    }

    public boolean isVoidLCLBLafterCOB() {
        return voidLCLBLafterCOB;
    }

    public void setVoidLCLBLafterCOB(boolean voidLCLBLafterCOB) {
        this.voidLCLBLafterCOB = voidLCLBLafterCOB;
    }

    public boolean isDeleteNotes() {
        return deleteNotes;
    }

    public void setDeleteNotes(boolean deleteNotes) {
        this.deleteNotes = deleteNotes;
    }

    public boolean isAddTemplates() {
        return addTemplates;
    }

    public void setAddTemplates(boolean addTemplates) {
        this.addTemplates = addTemplates;
    }

    public boolean isAesRequiredForPostingBLs() {
        return aesRequiredForPostingBLs;
    }

    public void setAesRequiredForPostingBLs(boolean aesRequiredForPostingBLs) {
        this.aesRequiredForPostingBLs = aesRequiredForPostingBLs;
    }

    public boolean isAesRequiredForReleasingDRs() {
        return aesRequiredForReleasingDRs;
    }

    public void setAesRequiredForReleasingDRs(boolean aesRequiredForReleasingDRs) {
        this.aesRequiredForReleasingDRs = aesRequiredForReleasingDRs;
    }

    public boolean isVoyageCloseAuditUndo() {
        return voyageCloseAuditUndo;
    }

    public void setVoyageCloseAuditUndo(boolean voyageCloseAuditUndo) {
        this.voyageCloseAuditUndo = voyageCloseAuditUndo;
    }

    public boolean isManageECIAccountLink() {
        return manageECIAccountLink;
    }

    public void setManageECIAccountLink(boolean manageECIAccountLink) {
        this.manageECIAccountLink = manageECIAccountLink;
    }

    public boolean isBatchHsCode() {
        return batchHsCode;
    }

    public void setBatchHsCode(boolean batchHsCode) {
        this.batchHsCode = batchHsCode;
    }

    public boolean isBkgVoyageReleaseDr() {
        return bkgVoyageReleaseDr;
    }

    public void setBkgVoyageReleaseDr(boolean bkgVoyageReleaseDr) {
        this.bkgVoyageReleaseDr = bkgVoyageReleaseDr;
    }

    public boolean isLclBookingContact() {
        return lclBookingContact;
    }

    public void setLclBookingContact(boolean lclBookingContact) {
        this.lclBookingContact = lclBookingContact;
    }

    public boolean isWarehouseQuickBkg() {
        return warehouseQuickBkg;
    }

    public void setWarehouseQuickBkg(boolean warehouseQuickBkg) {
        this.warehouseQuickBkg = warehouseQuickBkg;
    }

    public boolean isLclManifestPostedBl() {
        return lclManifestPostedBl;
    }

    public void setLclManifestPostedBl(boolean lclManifestPostedBl) {
        this.lclManifestPostedBl = lclManifestPostedBl;
    }

    public boolean isWeightChangeAfterRelease() {
        return weightChangeAfterRelease;
    }

    public void setWeightChangeAfterRelease(boolean weightChangeAfterRelease) {
        this.weightChangeAfterRelease = weightChangeAfterRelease;
    }

    public boolean isPreventExpRelease() {
        return preventExpRelease;
    }

    public void setPreventExpRelease(boolean preventExpRelease) {
        this.preventExpRelease = preventExpRelease;
    }

    public boolean isDeleteUnits() {
        return deleteUnits;
    }

    public void setDeleteUnits(boolean deleteUnits) {
        this.deleteUnits = deleteUnits;
    }

    public boolean isRemoveDrHold() {
        return removeDrHold;
    }

    public void setRemoveDrHold(boolean removeDrHold) {
        this.removeDrHold = removeDrHold;
    }

    public boolean isNo997EdiSubmission() {
        return no997EdiSubmission;
    }

    public void setNo997EdiSubmission(boolean no997EdiSubmission) {
        this.no997EdiSubmission = no997EdiSubmission;
    }   

    public boolean isBypassRelayCheck() {
        return bypassRelayCheck;
    }

    public void setBypassRelayCheck(boolean bypassRelayCheck) {
        this.bypassRelayCheck = bypassRelayCheck;
    }

    public boolean isDefaultLoadAllReleased() {
        return defaultLoadAllReleased;
    }

    public void setDefaultLoadAllReleased(boolean defaultLoadAllReleased) {
        this.defaultLoadAllReleased = defaultLoadAllReleased;
    }

    public boolean isLclQuoteClient() {
        return lclQuoteClient;
    }

    public void setLclQuoteClient(boolean lclQuoteClient) {
        this.lclQuoteClient = lclQuoteClient;
    }

    public boolean isPickDrWarnings() {
        return pickDrWarnings;
    }

    public void setPickDrWarnings(boolean pickDrWarnings) {
        this.pickDrWarnings = pickDrWarnings;
    }

    public boolean isChangeBLCommodityAfterCOB() {
        return changeBLCommodityAfterCOB;
    }

    public void setChangeBLCommodityAfterCOB(boolean changeBLCommodityAfterCOB) {
        this.changeBLCommodityAfterCOB = changeBLCommodityAfterCOB;
    }  

    public boolean isChangeDefaultFF() {
        return changeDefaultFF;
    }

    public void setChangeDefaultFF(boolean changeDefaultFF) {
        this.changeDefaultFF = changeDefaultFF;
    }

    public boolean isAllowChangeDisposition() {
        return allowChangeDisposition;
    }

    public void setAllowChangeDisposition(boolean allowChangeDisposition) {
        this.allowChangeDisposition = allowChangeDisposition;
    }

    public boolean isBatchHotCode() {
        return batchHotCode;
    }

    public void setBatchHotCode(boolean batchHotCode) {
        this.batchHotCode = batchHotCode;
    }

    public boolean isLclBookingDefaultERT() {
        return lclBookingDefaultERT;
    }

    public void setLclBookingDefaultERT(boolean lclBookingDefaultERT) {
        this.lclBookingDefaultERT = lclBookingDefaultERT;
    }       

    public boolean isDefaultNoeeiLowVal() {
        return defaultNoeeiLowVal;
    }

    public void setDefaultNoeeiLowVal(boolean defaultNoeeiLowVal) {
        this.defaultNoeeiLowVal = defaultNoeeiLowVal;
    }

    public boolean isLclImportAllowTransshipment() {
        return lclImportAllowTransshipment;
    }

    public void setLclImportAllowTransshipment(boolean lclImportAllowTransshipment) {
        this.lclImportAllowTransshipment = lclImportAllowTransshipment;
    }
    
    public boolean isDefaultDocsRcvd() {
        return defaultDocsRcvd;
    }

    public void setDefaultDocsRcvd(boolean defaultDocsRcvd) {
        this.defaultDocsRcvd = defaultDocsRcvd;
    }
}

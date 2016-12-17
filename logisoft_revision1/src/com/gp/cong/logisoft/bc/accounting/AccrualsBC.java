package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.APConfigurationBC;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ApInvoiceDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.logiware.excel.ExportAccrualsToExcel;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.reports.AccrualsReportCreator;
import com.logiware.utils.AuditNotesUtils;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.WebContextFactory;
import org.json.JSONObject;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.accounting.dao.AccrualsDAO;
import java.util.Arrays;
import org.apache.commons.beanutils.PropertyUtils;

public class AccrualsBC implements ConstantsInterface {

    public List getCustomers(AccrualsForm accrualsForm) {
        List accrualCustomerList = null;
        if (accrualsForm.getNumber() == null || !accrualsForm.getNumber().equals("")) {
            accrualCustomerList = new TransactionLedgerDAO().getTransactionsForVendor(accrualsForm.getNumber());
        } else {
            accrualCustomerList = new TransactionLedgerDAO().getTransactionsForVendor(accrualsForm.getVendornumber());
        }
        return accrualCustomerList;
    }

    public List getCustomers(String acctNumber) {
        List accrualCustomerList = null;
        accrualCustomerList = new TransactionLedgerDAO().getTransactionsForVendor(acctNumber);
        return accrualCustomerList;
    }

    public List getAssignedAccrualCustomers(AccrualsForm accrualsForm)throws Exception {
        List assignedAccrualCustomerList = null;
        String status = "AS";
        if (accrualsForm.getNumber() == null || !accrualsForm.getNumber().equals("")) {
            assignedAccrualCustomerList = new TransactionLedgerDAO().getPaidTransactionsForVendor(accrualsForm.getNumber(), status);
        } else {
            assignedAccrualCustomerList = new TransactionLedgerDAO().getPaidTransactionsForVendor(accrualsForm.getVendornumber(), status);
        }
        return assignedAccrualCustomerList;
    }

    public List getRejectedAccrualCustomers(AccrualsForm accrualsForm)throws Exception {
        List rejectedAccrualCustomerList = null;
        String status = "R";
        if (accrualsForm.getNumber() == null || !accrualsForm.getNumber().equals("")) {
            rejectedAccrualCustomerList = new TransactionLedgerDAO().getPaidTransactionsForVendor(accrualsForm.getNumber(), status);
        } else {
            rejectedAccrualCustomerList = new TransactionLedgerDAO().getPaidTransactionsForVendor(accrualsForm.getVendornumber(), status);
        }
        return rejectedAccrualCustomerList;
    }

    public void convertToPayableCustomer(AccrualsForm accrualsForm)throws Exception {
        if (accrualsForm.getNumber() == null || !accrualsForm.getNumber().equals("")) {
            new TransactionLedgerDAO().saveToTransaction(accrualsForm.getNumber());
        } else {
            new TransactionLedgerDAO().saveToTransaction(accrualsForm.getVendornumber());
        }
        if (accrualsForm.getPaidAccrual().equals("yes")) {
            accrualsForm.setPaidAccrual("no");
        }
        if (accrualsForm.getAssignedAccruals().equals("yes")) {
            accrualsForm.setAssignedAccruals("no");
        }
        if (accrualsForm.getRejectedAccruals().equals("yes")) {
            accrualsForm.setRejectedAccruals("no");
        }
    }

    public List searchAccuralsByInvoiceNumber(String vendorName, String vendorNumber, String invoiceNumber, String status)throws Exception {
        return new TransactionLedgerDAO().searchAccuralsByInvoiceNumber(vendorName, vendorNumber, invoiceNumber, status);
    }

    public String validateGLAccountForAccruals(String transactionIdsForValidation) {
        String ids = StringUtils.removeEnd(StringUtils.removeStart(transactionIdsForValidation.replaceAll("AC", ""), ","), ",");
        return new AccountingLedgerDAO().validateGlAccount(ids);
    }

    public List<TransactionBean> getInvalidGLAccountForAccruals(String transactionIdsForValidation) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        String ids = StringUtils.removeEnd(StringUtils.removeStart(transactionIdsForValidation.replaceAll("AC", ""), ","), ",");
        List<TransactionBean> accrualsList = new TransactionLedgerDAO().findByTransactionIds(ids);
        List<TransactionBean> invalidGLAccountAccruals = new ArrayList<TransactionBean>();
        for (TransactionBean accruals : accrualsList) {
            if (!accountDetailsDAO.validateAccount(accruals.getGlAcctNo())) {
                invalidGLAccountAccruals.add(accruals);
            }
        }
        return invalidGLAccountAccruals;
    }

    public List<TransactionBean> updateGLAccountsForAccruals(String accrualIds, String shipmentTypes, String chargeCodes, String suffixValues) throws Exception {
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        String[] ids = StringUtils.splitByWholeSeparator(StringUtils.removeEnd(StringUtils.removeStart(accrualIds, ","), ","), ",");
        String[] shipmentType = StringUtils.splitByWholeSeparator(StringUtils.removeEnd(StringUtils.removeStart(shipmentTypes, ","), ","), ",");
        String[] chargeCode = StringUtils.splitByWholeSeparator(StringUtils.removeEnd(StringUtils.removeStart(chargeCodes, ","), ","), ",");
        String[] suffixValue = StringUtils.splitByWholeSeparator(StringUtils.removeEnd(StringUtils.removeStart(suffixValues, ","), ","), ",");
        int i = 0;
        List<TransactionBean> invalidGLAccountAccruals = new ArrayList<TransactionBean>();
        for (String id : ids) {
            TransactionLedger transactionLedger = new TransactionLedgerDAO().findById(Integer.parseInt(id));
            TransactionBean accrual = new TransactionBean(transactionLedger);
            String glAccount = glMappingDAO.getGLAccountNoWithPrefixAndSuffix(shipmentType[i], chargeCode[i], "E", suffixValue[i]);
            if (null != glAccount && accountDetailsDAO.validateAccount(glAccount)) {
                transactionLedger.setGlAccountNumber(glAccount);
                transactionLedger.setChargeCode(chargeCode[i]);
                transactionLedger.setShipmentType(shipmentType[i]);
            } else {
                invalidGLAccountAccruals.add(accrual);
            }
            i++;
        }
        return invalidGLAccountAccruals;
    }

    public TransactionLedger getTransactionLedgerById(Integer id) throws Exception {
        return new TransactionLedgerDAO().findById(id);
    }

    public String saveTransactionLedger(TransactionBean transactionBean) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        Double amount = Double.parseDouble(transactionBean.getAmount());
        TransactionLedger accrual = new TransactionLedger(transactionBean);
        accrual.setBillLaddingNo(transactionBean.getBillofLadding().trim());
        accrual.setShipmentType(transactionBean.getShipmentType().trim());
        accrual.setGlAccountNumber(transactionBean.getGlAcctNo().trim());
        accrual.setBlueScreenChargeCode(transactionBean.getBlueScreenChargeCode());
        accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
        accrual.setTransactionAmt(amount);
        accrual.setBalance(amount);
        accrual.setBalanceInProcess(amount);
        accrual.setStatus(STATUS_OPEN);
        accrual.setDueDate(CommonUtils.isNotEmpty(transactionBean.getInvoiceDate()) ? DateUtils.parseDate(transactionBean.getInvoiceDate(), "MM/dd/yyyy H:mm") : new Date());
        accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
        accrual.setCustomerReferenceNo(transactionBean.getBillofLadding());
        accrual.setBillLaddingNo(transactionBean.getBillofLadding());
        accrual.setDocReceipt(transactionBean.getDocReceipt());
        accrual.setVoyageNo(transactionBean.getVoyagenumber());
        accrual.setCreatedOn(new Date());
        accrual.setCreatedBy(loginUser.getUserId());
        accrual.setPostedDate(null);
        Integer id = new TransactionLedgerDAO().saveAndReturnId(accrual);
        StringBuilder desc = new StringBuilder("Accrual Created for ");
        boolean addAnd = false;
        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo()) || CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
            new AccrualsDAO().createFclBlCost(accrual, loginUser.getLoginName());
        }
        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
            desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
            if (addAnd) {
                desc.append(" and ");
            }
            desc.append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
            if (addAnd) {
                desc.append(" and ");
            }
            desc.append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
        }
        desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy")).append(" by ").append(loginUser.getLoginName());
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, id.toString(), NotesConstants.ACCRUALS, loginUser);
        return id.toString();
    }

    public String updateAccruals(TransactionBean transactionBean) throws Exception {
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        TransactionLedger accrual = new TransactionLedgerDAO().findById(Integer.parseInt(transactionBean.getTransactionId()));
        if (accountDetailsDAO.validateAccount(transactionBean.getGlAcctNo())) {
            AuditNotesUtils.insertAuditNotes(accrual.getShipmentType(), transactionBean.getShipmentType(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), "Shipment Type", loginUser);
            accrual.setShipmentType(transactionBean.getShipmentType().trim());
            AuditNotesUtils.insertAuditNotes(accrual.getGlAccountNumber(), transactionBean.getGlAcctNo(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), "GL Account", loginUser);
            accrual.setGlAccountNumber(null != transactionBean.getGlAcctNo() ? transactionBean.getGlAcctNo().trim() : accrual.getGlAccountNumber());
            AuditNotesUtils.insertAuditNotes(accrual.getBlueScreenChargeCode(), transactionBean.getBlueScreenChargeCode(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), "Blue Screen Charge Code", loginUser);
            accrual.setBlueScreenChargeCode(null != transactionBean.getBlueScreenChargeCode() ? transactionBean.getBlueScreenChargeCode() : accrual.getBlueScreenChargeCode());
            AuditNotesUtils.insertAuditNotes(accrual.getChargeCode(), transactionBean.getChargeCode(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), "Charge Code", loginUser);
            accrual.setChargeCode(transactionBean.getChargeCode());
            new TransactionLedgerDAO().update(accrual);
            return "Accrual is Updated with GL account";
        } else {
            return "GL Account - " + accrual.getGlAccountNumber() + " is not a valid one";
        }
    }

    public String updateTransactionLedger(TransactionBean transactionBean, String leaveBalance) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        TransactionLedger oldAccrual = new TransactionLedgerDAO().findById(Integer.parseInt(transactionBean.getTransactionId()));
        Integer transactionId = 0;
        Double transactionAmount = Double.parseDouble(transactionBean.getAmount());
        Double oldAmount = oldAccrual.getTransactionAmt();
        String description = "For Cost - " + oldAccrual.getChargeCode() + ",";
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getChargeCode(), transactionBean.getChargeCode(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Charge Code", loginUser);
        oldAccrual.setChargeCode(transactionBean.getChargeCode());
        description = "For Cost - " + oldAccrual.getChargeCode() + ",";
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getShipmentType(), transactionBean.getShipmentType(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Shipment Type", loginUser);
        oldAccrual.setShipmentType(transactionBean.getShipmentType().trim());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getBillLaddingNo(), transactionBean.getBillofLadding(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Bill of Ladding", loginUser);
        oldAccrual.setBillLaddingNo(transactionBean.getBillofLadding().trim());
        oldAccrual.setCustomerReferenceNo(transactionBean.getBillofLadding());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getGlAccountNumber(), transactionBean.getGlAcctNo(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "GL Account", loginUser);
        oldAccrual.setGlAccountNumber(null != transactionBean.getGlAcctNo() ? transactionBean.getGlAcctNo().trim() : oldAccrual.getGlAccountNumber());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getBlueScreenChargeCode(), transactionBean.getBlueScreenChargeCode(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Blue Screen Charge Code", loginUser);
        oldAccrual.setBlueScreenChargeCode(null != transactionBean.getBlueScreenChargeCode() ? transactionBean.getBlueScreenChargeCode() : oldAccrual.getBlueScreenChargeCode());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getDocReceipt(), transactionBean.getDocReceipt(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Doc Receipt", loginUser);
        oldAccrual.setDocReceipt(transactionBean.getDocReceipt().trim());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getVoyageNo(), transactionBean.getVoyagenumber(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Voyage Number", loginUser);
        oldAccrual.setVoyageNo(transactionBean.getVoyagenumber().trim());
        AuditNotesUtils.insertAuditNotes(description, oldAccrual.getInvoiceNumber(), transactionBean.getInvoiceOrBl(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Invoice Number", loginUser);
        oldAccrual.setInvoiceNumber(transactionBean.getInvoiceOrBl());
        oldAccrual.setDueDate(null);
        String amountFromDomain = NumberUtils.formatNumber(oldAmount, "###,###,##0.00");
        String amountFromForm = NumberUtils.formatNumber(transactionAmount, "###,###,##0.00");
        AuditNotesUtils.insertAuditNotes(description, amountFromDomain, amountFromForm, NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), "Transaction Amount", loginUser);
        oldAccrual.setTransactionAmt(transactionAmount);
        oldAccrual.setBalanceInProcess(transactionAmount);
        oldAccrual.setBalance(transactionAmount);
        oldAccrual.setUpdatedOn(new Date());
        oldAccrual.setUpdatedBy(loginUser.getUserId());
        oldAccrual.setPostedDate(null);
        new TransactionLedgerDAO().update(oldAccrual); // update old Accrual
                        /*This is to Update fclBlCostCose */
        if (CommonUtils.isNotEmpty(oldAccrual.getCostId())) {
            FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(oldAccrual.getCostId());
            if (null != fclBlCostCodes) {
                if ("OCNFRT".equalsIgnoreCase(fclBlCostCodes.getCostCode())) {
                    fclBlCostCodes.setAmount(transactionAmount - (oldAmount - fclBlCostCodes.getAmount()));
                } else {
                    fclBlCostCodes.setAmount(transactionAmount);
                }
                fclBlCostCodes.setCostCode(oldAccrual.getChargeCode());
                fclBlCostCodes.setInvoiceNumber(transactionBean.getInvoiceOrBl());
                fclBlCostCodes.setManifestModifyFlag("yes");//coded by suriya
                fclBlCostCodes.setProcessedStatus("");
                new FclBlCostCodesDAO().update(fclBlCostCodes);
            }
        } else if (CommonUtils.isNotEmpty(oldAccrual.getBillLaddingNo()) || CommonUtils.isNotEmpty(oldAccrual.getDocReceipt())) {
            new AccrualsDAO().createFclBlCost(oldAccrual, loginUser.getLoginName());
        }
        if (YES.equals(leaveBalance) && transactionAmount < oldAmount) {
            //create new Accrual
            double newAmount = oldAmount - transactionAmount;
            TransactionLedger newAccrual = new TransactionLedger();
            PropertyUtils.copyProperties(newAccrual, oldAccrual);
            newAccrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            newAccrual.setStatus(STATUS_OPEN);
            newAccrual.setTransactionAmt(newAmount);
            newAccrual.setBalance(newAmount);
            newAccrual.setBalanceInProcess(newAmount);
            newAccrual.setTransactionDate(new Date());
            newAccrual.setCreatedOn(new Date());
            newAccrual.setCreatedBy(loginUser.getUserId());
            newAccrual.setPostedDate(null);
            if (CommonUtils.isNotEmpty(newAccrual.getApCostKey())) {
                newAccrual.setApCostKey(accrualsDAO.getIncrementalApcostkey(newAccrual.getApCostKey()));
            }
            transactionId = new TransactionLedgerDAO().saveAndReturnId(newAccrual);
            if (CommonUtils.isNotEmpty(newAccrual.getBillLaddingNo()) || CommonUtils.isNotEmpty(newAccrual.getDocReceipt())) {
                accrualsDAO.createFclBlCost(newAccrual, loginUser.getLoginName());
            }
            StringBuilder desc = new StringBuilder("New Accrual Created for remaining amount '").append(NumberUtils.formatNumber(newAmount, "###,###,##0.00")).append("'");
            desc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, oldAccrual.getTransactionId().toString(), NotesConstants.ACCRUALS, loginUser);
            desc = new StringBuilder("New Accrual Created for");
            desc.append(" Cost - '").append(newAccrual.getChargeCode()).append("'");
            if (CommonUtils.isNotEmpty(newAccrual.getBillLaddingNo())) {
                desc.append(" and B/L - '").append(newAccrual.getBillLaddingNo()).append("'");
            }
            if (CommonUtils.isNotEmpty(newAccrual.getDocReceipt())) {
                desc.append(" and Doc Receipt - '").append(newAccrual.getDocReceipt()).append("'");
            }
            if (CommonUtils.isNotEmpty(newAccrual.getVoyageNo())) {
                desc.append(" and Voyage - '").append(newAccrual.getVoyageNo()).append("'");
            }
            desc.append(" with amount - '").append(NumberUtils.formatNumber(newAccrual.getTransactionAmt(), "###,###,##0.00")).append("'");
            desc.append(" remaining from amount '").append(NumberUtils.formatNumber(oldAmount, "###,###,##0.00")).append("'");
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy")).append(" by ").append(loginUser.getLoginName());
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, transactionId.toString(), NotesConstants.ACCRUALS, loginUser);
        }
        return transactionId.toString();
    }

    public String deriveGlAccount(String chargeCode, String shipmentType, String orginTerminal) throws Exception {
        return new GlMappingDAO().dervieGlAccount(chargeCode, shipmentType, orginTerminal, GLMappingConstant.EXPENSE);
    }

    public boolean updateAccrualStatus(String id, String status) throws Exception {
        TransactionLedger accrual = new TransactionLedgerDAO().findById(Integer.parseInt(id.replaceAll("AC", "")));
        accrual.setStatus(status);
        if (CommonUtils.isNotEmpty(accrual.getCostId())) {
            FclBlCostCodes fclBlCostCodes = new FclBlCostCodesDAO().findById(accrual.getCostId());
            if (null != fclBlCostCodes) {
                if (CommonUtils.isEqualIgnoreCase(status, STATUS_INACTIVE)) {
                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_IN_PROGRESS)) {
                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)) {
                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_DISPUTE);
                } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_OPEN)) {
                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                }
            }
        }
        return true;
    }

    public String printAccruals(AccrualsForm accrualsForm) throws Exception {
        List<TransactionBean> accruals = new TransactionLedgerDAO().searchAccuralsById(accrualsForm.getAccrualIds());
        String contextPath = WebContextFactory.get().getServletContext().getRealPath("/");
        return new AccrualsReportCreator().createReport(accrualsForm, accruals, contextPath);
    }

    public String exportAccrualsToExcel(AccrualsForm accrualsForm) throws Exception {
        List<TransactionBean> accruals = new TransactionLedgerDAO().searchAccuralsById(accrualsForm.getAccrualIds());
        return new ExportAccrualsToExcel().exportToExcel(accrualsForm, accruals);
    }

    public Integer inactivateOrActivateAccruals(Integer days, boolean canInactivate) throws Exception {
        return new AccountingLedgerDAO().inactivateOrActivateAccruals(days, canInactivate);
    }

    public String getCustomerDetails(String custNumber) throws Exception {
        JSONObject jsonObj = new JSONObject();
        String contactName = "";
        String address = "";
        String phoneNumber = "";
        String term = "11344";
        String termDesc = "Due Upon Receipt";
        String accountType = "";
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(custNumber);
        String subType = tradingPartner.getAcctType().contains("V") && null != tradingPartner.getSubType() && tradingPartner.getSubType().equalsIgnoreCase("Overhead") ? "Y" : "N";
        if (tradingPartner.getAcctType() != null) {
            accountType = tradingPartner.getAcctType();
        }
        CustAddress custAddress = new CustAddressDAO().getCustAddressForCheck(custNumber);
        if (null != custAddress) {
            contactName = custAddress.getContactName();
            address = custAddress.getAddress1();
            phoneNumber = custAddress.getPhone();
        }
        Vendor vendor = new APConfigurationBC().getVendorByCustomerNumber(custNumber);
        if (null != vendor) {
            GenericCode code = vendor.getCterms();
            if (null != code && null != code.getId() && null != code.getCodedesc()) {
                term = code.getId().toString();
                termDesc = code.getCodedesc();
            }
        }
        jsonObj.put("accountType", accountType);
        jsonObj.put("subType", subType);
        jsonObj.put("contactName", contactName);
        jsonObj.put("address", address);
        jsonObj.put("phoneNumber", phoneNumber);
        jsonObj.put("term", term);
        jsonObj.put("termDesc", termDesc);
        return jsonObj.toString();
    }

    public String updateAccrualInvoiceNumber(String transactionId, String invoiceNumber) throws Exception {
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        TransactionLedger transactionLedger = accountingLedgerDAO.findById(Integer.parseInt(transactionId.replace(TRANSACTION_TYPE_ACCRUALS, "")));
        boolean isNoApInvoice = new AccountingTransactionDAO().isApInvoiceAvailable(invoiceNumber, transactionLedger.getCustNo());
        if (isNoApInvoice) {
            boolean isNoEdiInvoice = new EdiInvoiceDAO().isInvoiceAvailable(transactionLedger.getCustNo(), invoiceNumber);
            if (isNoEdiInvoice) {
                transactionLedger.setInvoiceNumber(invoiceNumber);
                accountingLedgerDAO.update(transactionLedger);
                return "success";
            } else {
                return "EDI Invoice is found for this invoice number.";
            }
        } else {
            return "AP Invoice is found for this invoice number.";
        }
    }

    public boolean rejectInvoice(AccrualsForm accrualsForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
        Double invoiceAmount = Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", ""));
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber().trim();
        if (null == apInvoice) {
            apInvoice = new ApInvoice();
            StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
            desc.append(" of '").append(accrualsForm.getVendor()).append("'");
            desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
            desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
        }
        apInvoice.setCustomerName(accrualsForm.getVendor());
        apInvoice.setAccountNumber(accrualsForm.getVendornumber());
        apInvoice.setInvoiceNumber(accrualsForm.getInvoicenumber());
        apInvoice.setInvoiceAmount(invoiceAmount);
        apInvoice.setTerm(accrualsForm.getTerm());
        apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoicedate(), "MM/dd/yyyy"));
        apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDuedate(), "MM/dd/yyyy"));
        apInvoice.setStatus(STATUS_REJECT);
        StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
        desc.append(" of '").append(accrualsForm.getVendor()).append("'");
        desc.append(" rejected by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
        if (CommonUtils.isNotEmpty(accrualsForm.getComments().trim())) {
            AuditNotesUtils.insertAuditNotes(accrualsForm.getComments(), moduleId, moduleRefId, moduleId, loginUser);
        }
        new ApInvoiceDAO().saveOrUpdate(apInvoice);
        return true;
    }

    public boolean unRejectInvoice(AccrualsForm accrualsForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
        Double invoiceAmount = Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", ""));
        if (null != apInvoice) {
            apInvoice.setStatus(STATUS_OPEN);
            String invoiceNumber = accrualsForm.getInvoicenumber();
            String vendorNumber = accrualsForm.getVendornumber();
            String moduleRefId = vendorNumber + "-" + invoiceNumber;
            StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
            desc.append(" of '").append(accrualsForm.getVendor()).append("'");
            desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
            desc.append(" unrejected by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
            new ApInvoiceDAO().saveOrUpdate(apInvoice);
        }
        return true;
    }

    public boolean disputeInvoice(AccrualsForm accrualsForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
        Double invoiceAmount = Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", ""));
        String moduleRefId = accrualsForm.getVendornumber() + "-" + accrualsForm.getInvoicenumber();
        String invoiceNumber = accrualsForm.getInvoicenumber();
        if (null == apInvoice) {//create new invoice if not exists
            apInvoice = new ApInvoice();
            StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
            desc.append(" of '").append(accrualsForm.getVendor()).append("'");
            desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
            desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
        }
        apInvoice.setCustomerName(accrualsForm.getVendor());
        apInvoice.setAccountNumber(accrualsForm.getVendornumber());
        apInvoice.setInvoiceNumber(accrualsForm.getInvoicenumber());
        apInvoice.setInvoiceAmount(invoiceAmount);
        apInvoice.setTerm(accrualsForm.getTerm());
        apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoicedate(), "MM/dd/yyyy"));
        apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDuedate(), "MM/dd/yyyy"));
        apInvoice.setDisputeDate(new Date());
        apInvoice.setResolvedDate(null);
        apInvoice.setStatus(STATUS_DISPUTE);
        StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
        desc.append(" of '").append(accrualsForm.getVendor()).append("'");
        desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
        desc.append(" marked as dispute by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        List<EmailSchedulerVO> disputeEmailList = emailschedulerDAO.getDisputeEmail(moduleRefId);
        for (EmailSchedulerVO emailSchedulerVO : disputeEmailList) {
            emailSchedulerVO.setStatus(EMAIL_STATUS_PENDING);
            emailschedulerDAO.update(emailSchedulerVO);
            apInvoice.setFromAddress(emailSchedulerVO.getFromAddress());
            apInvoice.setToAddress(emailSchedulerVO.getToAddress());
        }
        new ApInvoiceDAO().saveOrUpdate(apInvoice);
        List ids = new ArrayList();
        if (CommonUtils.isNotEmpty(accrualsForm.getDisputedTransactions())) {
            String[] transactionIds = StringUtils.split(StringUtils.removeEnd(accrualsForm.getDisputedTransactions(), ","), ",");
            ids.addAll(Arrays.asList(transactionIds));
            for (String transactionId : transactionIds) {
                TransactionLedger accrual = accountingLedgerDAO.findById(Integer.parseInt(transactionId));
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is marked as dispute for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(STATUS_DISPUTE);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_DISPUTE);
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedTransactions())) {
            String[] transactionIds = StringUtils.split(StringUtils.removeEnd(accrualsForm.getAssignedTransactions(), ","), ",");
            ids.addAll(Arrays.asList(transactionIds));
            for (String transactionId : transactionIds) {
                TransactionLedger accrual = accountingLedgerDAO.findById(Integer.parseInt(transactionId));
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is marked as In Progress for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(STATUS_IN_PROGRESS);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndoDisputedTransactions())) {
            String[] transactionIds = StringUtils.split(StringUtils.removeEnd(accrualsForm.getUndoDisputedTransactions(), ","), ",");
            ids.addAll(Arrays.asList(transactionIds));
            for (String transactionId : transactionIds) {
                TransactionLedger accrual = accountingLedgerDAO.findById(Integer.parseInt(transactionId));
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is unmarked as dispute for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(STATUS_OPEN);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndoInProgressTransactions())) {
            String[] transactionIds = StringUtils.split(StringUtils.removeEnd(accrualsForm.getUndoInProgressTransactions(), ","), ",");
            ids.addAll(Arrays.asList(transactionIds));
            for (String transactionId : transactionIds) {
                TransactionLedger accrual = accountingLedgerDAO.findById(Integer.parseInt(transactionId));
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(accrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is unmarked as In Progress for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(STATUS_OPEN);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(ids)) {
            accountingLedgerDAO.setConvertToApFlagInSSMasterApprovedBL(ids);
        }
        return true;
    }

    public boolean unDisputeInvoice(AccrualsForm accrualsForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ApInvoice apInvoice = apInvoiceDAO.findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
        Double invoiceAmount = Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", ""));
        if (null != apInvoice) {
            apInvoice.setStatus(STATUS_OPEN);
            apInvoice.setResolvedDate(new Date());
            String invoiceNumber = accrualsForm.getInvoicenumber();
            String vendorNumber = accrualsForm.getVendornumber();
            String moduleRefId = vendorNumber + "-" + invoiceNumber;
            StringBuilder desc = new StringBuilder("Invoice '").append(accrualsForm.getInvoicenumber()).append("'");
            desc.append(" of '").append(accrualsForm.getVendor()).append("'");
            desc.append(" for amount '").append(NumberUtils.formatNumber(invoiceAmount, "###,###,##0.00")).append("'");
            desc.append(" unmarked as dispute by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, moduleRefId, NotesConstants.AP_INVOICE, loginUser);
            new ApInvoiceDAO().saveOrUpdate(apInvoice);
            List<TransactionLedger> disputedAccruals = accountingLedgerDAO.getDisputedAccruals(vendorNumber, invoiceNumber);
            for (TransactionLedger disputedAccrual : disputedAccruals) {
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(disputedAccrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(disputedAccrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(disputedAccrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(disputedAccrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(disputedAccrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(disputedAccrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(disputedAccrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is unmarked as dispute for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                String transactionId = disputedAccrual.getTransactionId().toString();
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                disputedAccrual.setInvoiceNumber(invoiceNumber);
                disputedAccrual.setStatus(STATUS_OPEN);
                disputedAccrual.setUpdatedOn(new Date());
                disputedAccrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(disputedAccrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(disputedAccrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                    }
                }
            }
            List<TransactionLedger> inprogressAccruals = accountingLedgerDAO.getInProgressAccruals(vendorNumber, invoiceNumber);
            for (TransactionLedger inprogressAccrual : inprogressAccruals) {
                StringBuilder notesDesc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(inprogressAccrual.getBillLaddingNo())) {
                    notesDesc.append("B/L - '").append(inprogressAccrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(inprogressAccrual.getDocReceipt())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Doc Receipt - '").append(inprogressAccrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(inprogressAccrual.getVoyageNo())) {
                    if (addAnd) {
                        notesDesc.append(" and ");
                    }
                    notesDesc.append("Voyage - '").append(inprogressAccrual.getVoyageNo()).append("'");
                }
                notesDesc.append(" for amount '").append(NumberUtils.formatNumber(inprogressAccrual.getTransactionAmt(), "###,###,##0.00")).append("'");
                notesDesc.append(" is unmarked as In Progress for Invoice - '").append(invoiceNumber).append("'");
                notesDesc.append(" by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                String transactionId = inprogressAccrual.getTransactionId().toString();
                AuditNotesUtils.insertAuditNotes(notesDesc.toString(), NotesConstants.ACCRUALS, transactionId, NotesConstants.ACCRUALS, loginUser);
                inprogressAccrual.setInvoiceNumber(invoiceNumber);
                inprogressAccrual.setStatus(STATUS_OPEN);
                inprogressAccrual.setUpdatedOn(new Date());
                inprogressAccrual.setUpdatedBy(loginUser.getUserId());
                if (CommonUtils.isNotEmpty(inprogressAccrual.getCostId())) {
                    FclBlCostCodes fclBlCostCodes = fclBlCostCodesDAO.findById(inprogressAccrual.getCostId());
                    if (null != fclBlCostCodes) {
                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                    }
                }
            }
        }
        return true;
    }

    public boolean overwriteInvoiceDocumentsId(String oldDocumentId, String newDocumentId) throws Exception {
        new DocumentStoreLogDAO().overwriteDocumentId("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
        return true;
    }
}

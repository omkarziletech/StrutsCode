package com.gp.cong.logisoft.bc.fcl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.STATUS_OPEN;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.beans.FclBlChargeBean;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;

public class ManifestBC {

    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    TransactionDAO transactionDAO = new TransactionDAO();
    GlMappingDAO glMappingDAO = new GlMappingDAO();
    ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();

    public void getTransactionObject(FclBl bl, List<FclBlChargeBean> fclBlChargeOrCostCodeList, HttpServletRequest request) throws Exception {
        String chargeCode = null;
        User user = null;
        String billingTerminal = "";
        TradingPartner tradingPartner = null;
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        if (null != request) {
            HttpSession session = request.getSession();
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
            }
        }
        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            bl.setShipmentType("FCLI");
        } else {
            bl.setShipmentType("FCLE");
        }
        if (CommonUtils.isNotEmpty(bl.getBillingTerminal()) && bl.getBillingTerminal().lastIndexOf("-") > -1) {
            billingTerminal = bl.getBillingTerminal().substring(bl.getBillingTerminal().lastIndexOf("-") + 1);
        }
        Set containerSet = bl.getFclcontainer();
        String containerNo = "";
        for (Object object : containerSet) {
            FclBlContainer fclBlContainer = (FclBlContainer) object;
            if (CommonUtils.isEmpty(containerNo)) {
                containerNo = fclBlContainer.getTrailerNo();
            } else {
                containerNo = containerNo + "," + fclBlContainer.getTrailerNo();
            }
        }
        for (FclBlChargeBean fclbean : fclBlChargeOrCostCodeList) {
            TransactionLedger transactionLadger = new TransactionLedger();
            transactionLadger.setBillLaddingNo(bl.getBolId());
            transactionLadger.setContainerNo(containerNo);
            chargeCode = null != fclbean.getChargeCode() ? fclbean.getChargeCode().trim() : "";
            if (chargeCode != null && chargeCode.equalsIgnoreCase(FclBlConstants.INTRAMP)) {
                transactionLadger.setChargeCode(FclBlConstants.INTMDL);
            } else {
                transactionLadger.setChargeCode(chargeCode);
            }
            transactionLadger.setTransactionAmt(Double.parseDouble(fclbean.getChargeAmt()));
            transactionLadger.setBalance(Double.parseDouble(fclbean.getChargeAmt()));
            transactionLadger.setBalanceInProcess(Double.parseDouble(fclbean.getChargeAmt()));
            transactionLadger.setCurrencyCode(fclbean.getCurrencyCode());
            transactionLadger.setDescription(fclbean.getComment());
            transactionLadger.setThirdptyName(bl.getThirdPartyName());
            transactionLadger.setThirdptyNo(bl.getBillTrdPrty());
            // Date today = new Date();
            if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                transactionLadger.setTransactionDate(bl.getEta());
            } else {
                transactionLadger.setTransactionDate(bl.getSailDate());
            }
//            transactionLadger.setCustomerReferenceNo(fclbean.getComment());
            transactionLadger.setShipName(bl.getShipperName());
            transactionLadger.setShipNo(bl.getShipperNo());
            transactionLadger.setFwdName(bl.getForwardingAgentName());
            transactionLadger.setFwdNo(bl.getForwardAgentNo());
            transactionLadger.setBlTerms(bl.getHouseBl());
            transactionLadger.setVoyageNo(String.valueOf(bl.getVoyages()));
            transactionLadger.setMasterBl(bl.getMasterBl());
            transactionLadger.setReadyToPost("on");
            transactionLadger.setOrgTerminal(bl.getOriginalTerminal());
            transactionLadger.setDestination(bl.getPortofDischarge());
            transactionLadger.setConsName(bl.getConsigneeName());
            transactionLadger.setConsNo(bl.getConsigneeNo());
            transactionLadger.setBookingNo(bl.getBookingNo());
            transactionLadger.setAgentName(bl.getAgent());
            transactionLadger.setAgentNo(bl.getAgentNo());
            transactionLadger.setShipmentType(bl.getShipmentType());
            transactionLadger.setDocReceipt(bl.getFileNo());
            transactionLadger.setBillingTerminal(bl.getBillingTerminal());
            if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                transactionLadger.setSailingDate(bl.getEta());
            } else {
                transactionLadger.setSailingDate(bl.getSailDate());
            }
            if (null != user) {
                transactionLadger.setCreatedBy(user.getUserId());
                transactionLadger.setCreatedOn(new Date());
            }
            if (bl.getCorrectionNumber() != null) {
                transactionLadger.setCustomerReferenceNo(bl.getCorrectionNumber());
            }
            transactionLadger.setMasterBl(bl.getStreamShipBl());
            transactionLadger.setSubHouseBl(bl.getHouseBl());
            if (bl.getVessel() != null) {
                transactionLadger.setVesselNo(bl.getVessel().getCode());
            }
            transactionLadger.setStatus("Open");
            transactionLadger.setBillTo("Y");
            if (fclbean.getBillTo() != null) {
                if (CommonFunctions.isNotNull(fclbean.getAcctName())) {
                    tradingPartner = tradingPartnerDAO.findById(fclbean.getAcctNo());
                    if (null != tradingPartner) {
                        transactionLadger.setCustName(tradingPartner.getAccountName());
                        transactionLadger.setCustNo(fclbean.getAcctNo());
                    }
                } else if (bl.getCustName() != null) {
                    tradingPartner = tradingPartnerDAO.findById(bl.getCustNo());
                    if (null != tradingPartner) {
                        transactionLadger.setCustName(tradingPartner.getAccountName());
                        transactionLadger.setCustNo(bl.getCustNo());
                    }
                } else {
                    if (fclbean.getBillTo().equalsIgnoreCase("Forwarder")) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getForwardAgentNo());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getForwardAgentNo());
                        }
                    } else if (fclbean.getBillTo().equalsIgnoreCase("Shipper")) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getShipperNo());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getShipperNo());
                        }
                    } else if (fclbean.getBillTo().equalsIgnoreCase("ThirdParty")) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getBillTrdPrty());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getBillTrdPrty());
                        }
                    } else if (fclbean.getBillTo().equalsIgnoreCase("Agent")) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getAgentNo());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getAgentNo());
                        }
                    } else if (fclbean.getBillTo().equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getConsigneeNo());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getConsigneeNo());
                        }
                    } else if ("NotifyParty".equalsIgnoreCase(fclbean.getBillTo())) {
                        tradingPartner = tradingPartnerDAO.findById(bl.getNotifyParty());
                        if (null != tradingPartner) {
                            transactionLadger.setCustName(tradingPartner.getAccountName());
                            transactionLadger.setCustNo(bl.getNotifyParty());
                        }
                    }
                }
                if (bl.getCorrectionNoticeTemp() != null) {
                    transactionLadger.setSubledgerSourceCode(FclBlConstants.SUBLADGER);
                } else {
                    transactionLadger.setSubledgerSourceCode("AR-" + bl.getShipmentType());
//                    transactionLadger.setSubledgerSourceCode(glMappingDAO.getSubLedgerSourceCode(fbl.getShipmentType(),fclbean.getChargeCode(), "AR", "R"));
                }
                transactionLadger.setBlueScreenChargeCode(glMappingDAO.getBlueScreenChargeCode(bl.getShipmentType(), fclbean.getChargeCode(), "AR", "R"));
                transactionLadger.setTransactionType("AR");
                transactionLadger.setPostedDate(postedDate);
                transactionLadger.setInvoiceNumber(fclbean.getInvoiceNumber());
//                transactionLadger.setGlAccountNumber(glMappingDAO.getGLAccountNoWithTransactionType(fbl.getShipmentType(), fclbean.getChargeCode(),fbl.getPortOfLoading(), fbl.getTerminal(), "R","AR"));
                transactionLadger.setGlAccountNumber(glMappingDAO.dervieGlAccount(fclbean.getChargeCode(), bl.getShipmentType(), billingTerminal, "R"));
            } else {
                tradingPartner = tradingPartnerDAO.findById(fclbean.getAcctNo());
                transactionLadger.setCustName((null != tradingPartner ? tradingPartner.getAccountName() : ""));
                transactionLadger.setCustNo((null != fclbean.getAcctNo() ? fclbean.getAcctNo() : ""));
                transactionLadger.setSubledgerSourceCode("ACC");
//                transactionLadger.setSubledgerSourceCode(glMappingDAO.getSubLedgerSourceCode(fbl.getShipmentType(),fclbean.getChargeCode(),"AC", "E"));
                transactionLadger.setBlueScreenChargeCode(glMappingDAO.getBlueScreenChargeCode(bl.getShipmentType(), fclbean.getChargeCode(), "AC", "E"));
                transactionLadger.setTransactionType("AC");
                transactionLadger.setTerminal(billingTerminal.trim());
//                transactionLadger.setGlAccountNumber(glMappingDAO.getGLAccountNoWithTransactionType(fbl.getShipmentType(), fclbean.getChargeCode(),fbl.getPortOfLoading(), fbl.getTerminal(), "E","AC"));
                transactionLadger.setGlAccountNumber(glMappingDAO.dervieGlAccount(fclbean.getChargeCode(), bl.getShipmentType(), billingTerminal, "E"));
                transactionLadger.setInvoiceNumber(fclbean.getInvoiceNumber());
            }
            transactionLadger.setCorrectionFlag("N");
            if (fclbean.getChargeId() != null) {
                transactionLadger.setChargeId(fclbean.getChargeId());
            }
            if (fclbean.getCostId() != null) {
                transactionLadger.setCostId(fclbean.getCostId());
            }
            if (bl.getCorrectionNoticeTemp() != null) {
                transactionLadger.setCorrectionNotice(FclBlConstants.CNA00 + bl.getCorrectionNoticeTemp());
                transactionLadger.setNoticeNumber(FclBlConstants.CNA00 + bl.getCorrectionNoticeTemp());
            } else {
                transactionLadger.setCorrectionNotice(FclBlConstants.CNA0);
                transactionLadger.setNoticeNumber(FclBlConstants.CNA0);
            }
            transactionLadger.setManifestFlag("Y");
            transactionLedgerDAO.save(transactionLadger);
        }
    }//for..

    public boolean updateTransactionForAC(FclBl fclBl, String chargeCode, TransactionLedger newTransactionLadger, String updateFuntion) throws Exception {
        boolean flag = false;
        List<TransactionLedger> shipperPBAList = transactionLedgerDAO.getTransactionList(
                fclBl.getBolId(), "AC", chargeCode);
        boolean isListHasObects = CommonFunctions.isNotNullOrNotEmpty(shipperPBAList);
        if (isListHasObects) {
            for (TransactionLedger transactionLedger : shipperPBAList) {
                if (updateFuntion != null && updateFuntion.equalsIgnoreCase(FclBlConstants.UPDATEFUNTION)) {
                    transactionLedger.setTransactionAmt(newTransactionLadger.getTransactionAmt());
                    transactionLedger.setBalance(newTransactionLadger.getBalance());
                    transactionLedger.setBalanceInProcess(newTransactionLadger.getBalanceInProcess());
                    if (fclBl.getCorrectionNoticeTemp() != null) {
                        transactionLedger.setCorrectionNotice(fclBl.getCorrectionNoticeTemp() + FclBlConstants.CNA);
                        transactionLedger.setNoticeNumber(fclBl.getCorrectionNoticeTemp() + FclBlConstants.CNA);
                    }
                    flag = true;
                } else {
                    transactionLedger.setCustName(newTransactionLadger.getCustName());
                    transactionLedger.setCustNo(newTransactionLadger.getCustNo());
                    if (newTransactionLadger.getTransactionAmt() != null) {
                        transactionLedger.setTransactionAmt(newTransactionLadger.getTransactionAmt());
                        transactionLedger.setBalanceInProcess(newTransactionLadger.getTransactionAmt());
                        transactionLedger.setBalance(newTransactionLadger.getTransactionAmt());
                    }
                }
            }
        }
        return flag;
    }

    public void createTransactionAfterManifest(List<TransactionLedger> transactionLadgerList, FclBl bl, String userName) throws Exception {
        Set containerSet = bl.getFclcontainer();
        String containerNo = "";
        String sealNo = "";
        for (Object object : containerSet) {
            FclBlContainer fclBlContainer = (FclBlContainer) object;
            if (CommonUtils.isEmpty(containerNo)) {
                containerNo = fclBlContainer.getTrailerNo();
            } else {
                containerNo = containerNo + "," + fclBlContainer.getTrailerNo();
            }
            if (CommonUtils.isNotEmpty(fclBlContainer.getSealNo())) {
                if (CommonUtils.isEmpty(sealNo)) {
                    sealNo = fclBlContainer.getSealNo();
                } else {
                    sealNo = sealNo + "," + fclBlContainer.getSealNo();
                }
            }
        }
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        for (TransactionLedger transactionLedger : transactionLadgerList) {
            Double transactionAmount = 0.0d;
            transactionAmount = transactionLedgerDAO.gettransactionamount(bl.getBolId(),
                    transactionLedger.getCustNo(), transactionLedger.getTransactionType());
            Transaction transaction = new Transaction();
            PropertyUtils.copyProperties(transaction, transactionLedger);
            transaction.setCustomerReferenceNo(bl.getExportReference());
            transaction.setCorrectionFlag("N");
            transaction.setTransactionDate(new Date());
            transaction.setTransactionAmt(transactionAmount);
            transaction.setBalance(transactionAmount);
            transaction.setBalanceInProcess(transactionAmount);
            transaction.setContainerNo(containerNo);
            transaction.setSealNo(sealNo);
            transaction.setBookingNo(bl.getBookingNo());
            transaction.setVesselName(null != bl.getVessel() ? bl.getVessel().getCodedesc() : "");
            transaction.setSteamShipLine(bl.getStreamShipLine());
            transaction.setEta(bl.getEta());
            transaction.setSailingDate(bl.getSailDate());
            String creditStatus = customerAccountingDAO.getCreditStatus(transaction.getCustNo());
            transaction.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? "N" : "Y");
            transactionDAO.save(transaction);
            ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
            arTransactionHistory.setBlNumber(bl.getBolId());
            arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
            arTransactionHistory.setPostedDate(postedDate);
            arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
            arTransactionHistory.setTransactionAmount(transaction.getTransactionAmt());
            arTransactionHistory.setCustomerNumber(transaction.getCustNo());
            arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
            arTransactionHistory.setCreatedBy(userName);
            arTransactionHistory.setCreatedDate(new Date());
            arTransactionHistory.setTransactionType("FCL BL");
            arTransactionHistoryDAO.save(arTransactionHistory);
        }
    }

    public void createTransactionObject(List<TransactionLedger> transactionLadgerList, FclBl bl, boolean amount, String userName) throws Exception {
        Set containerSet = bl.getFclcontainer();
        String containerNo = "";
        String sealNo = "";
        for (Object object : containerSet) {
            FclBlContainer fclBlContainer = (FclBlContainer) object;
            if (CommonUtils.isEmpty(containerNo)) {
                containerNo = fclBlContainer.getTrailerNo();
            } else {
                containerNo = containerNo + "," + fclBlContainer.getTrailerNo();
            }
            if (CommonUtils.isNotEmpty(fclBlContainer.getSealNo())) {
                if (CommonUtils.isEmpty(sealNo)) {
                    sealNo = fclBlContainer.getSealNo();
                } else {
                    sealNo = sealNo + "," + fclBlContainer.getSealNo();
                }
            }
        }
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        for (TransactionLedger transactionLedger : transactionLadgerList) {
            Double transactionAmount = 0.0d;
            transactionAmount = transactionLedgerDAO.gettransactionamount(bl.getBolId(),
                    transactionLedger.getCustNo(), transactionLedger.getTransactionType());
            Transaction transaction = transactionDAO.getTransactionByPassingBLNumberAndCostomerNo(
                    transactionLedger.getBillLaddingNo(),transactionLedger.getCustNo(),bl.getFileNo());
            if (null != transaction) {
                transaction.setBillLaddingNo(transactionLedger.getBillLaddingNo());
                transaction.setCustomerReferenceNo(bl.getExportReference());
                transaction.setCorrectionFlag("N");
                if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                    transaction.setTransactionDate(bl.getEta());
                } else {
                    transaction.setTransactionDate(bl.getSailDate());
                }
                transaction.setTransactionAmt(transactionAmount);
                transaction.setBalance(transaction.getBalance() + transactionAmount);
                transaction.setBalanceInProcess(transaction.getBalanceInProcess() + transactionAmount);
                transaction.setContainerNo(containerNo);
                transaction.setSealNo(sealNo);
                transaction.setBookingNo(bl.getBookingNo());
                transaction.setVesselName(null != bl.getVessel() ? bl.getVessel().getCodedesc() : "");
                transaction.setSteamShipLine(bl.getStreamShipLine());
                transaction.setEta(bl.getEta());
                transaction.setSailingDate(bl.getSailDate());
            } else {
                transaction = new Transaction();
                PropertyUtils.copyProperties(transaction, transactionLedger);
                transaction.setCustomerReferenceNo(bl.getExportReference());
                transaction.setCorrectionFlag("N");
                if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                    transaction.setTransactionDate(bl.getEta());
                } else {
                    transaction.setTransactionDate(bl.getSailDate());
                }
                transaction.setTransactionAmt(transactionAmount);
                transaction.setBalance(transactionAmount);
                transaction.setBalanceInProcess(transactionAmount);
                transaction.setContainerNo(containerNo);
                transaction.setSealNo(sealNo);
                transaction.setBookingNo(bl.getBookingNo());
                transaction.setVesselName(null != bl.getVessel() ? bl.getVessel().getCodedesc() : "");
                transaction.setSteamShipLine(bl.getStreamShipLine());
                transaction.setEta(bl.getEta());
                transaction.setSailingDate(bl.getSailDate());
                String creditStatus = customerAccountingDAO.getCreditStatus(transaction.getCustNo());
                transaction.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? "N" : "Y");
                transactionDAO.save(transaction);
            }
            ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
            arTransactionHistory.setCustomerReferenceNumber(bl.getExportReference());
            arTransactionHistory.setBlNumber(bl.getBolId());
            arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
            arTransactionHistory.setPostedDate(postedDate);
            arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
            arTransactionHistory.setTransactionAmount(transaction.getTransactionAmt());
            arTransactionHistory.setCustomerNumber(transaction.getCustNo());
            arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
            arTransactionHistory.setCreatedBy(userName);
            arTransactionHistory.setCreatedDate(new Date());
            arTransactionHistory.setTransactionType("FCL BL");
            new ArTransactionHistoryDAO().save(arTransactionHistory);
        }
    }

    public void updateTransactionAmount(String blNumber, String fileNumber, String tansactionType, String userName, boolean isHistory, boolean isCorrection) throws Exception {
        //tansactionType is true means AR else AC
        List<TransactionLedger> transactionLadgerList = transactionLedgerDAO.getDistinctTransactionLadger(blNumber, tansactionType);
        CustomerAccountingDAO customerAccountingDAO = new CustomerAccountingDAO();
        FclBl bl = new FclBlDAO().findById(blNumber);
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        for (TransactionLedger transactionLedger : transactionLadgerList) {
            double transactionAmount = 0.0d;
            if (null != blNumber && null != transactionLedger && null != transactionLedger.getCustNo()) {
                transactionAmount = transactionLedgerDAO.gettransactionamount(blNumber, transactionLedger.getCustNo(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            }
            FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
            Integer maxNoticeNo = fclBlCorrectionsDAO.getNoticeNumber(blNumber);
            String noticeNumber = (null != maxNoticeNo) ? maxNoticeNo.toString() : transactionLedger.getCorrectionNotice();
            Transaction transaction = transactionDAO.getTransactionByPassingBLNumberAndCostomerNo(transactionLedger.getBillLaddingNo(), transactionLedger.getCustNo(), fileNumber);
            boolean isCreditDebitNote = customerAccountingDAO.isCreditDebitNote(transactionLedger.getCustNo());
            if (transaction != null && ((!isCorrection) || (isCorrection && !isCreditDebitNote))) {
                transaction.setBillLaddingNo(transactionLedger.getBillLaddingNo());
                double amount = transaction.getTransactionAmt();
                double correctedAmount = transactionAmount - amount;
                transaction.setTransactionAmt(transactionAmount);
                transaction.setBalance(transaction.getBalance() + correctedAmount);
                transaction.setBalanceInProcess(transaction.getBalanceInProcess() + correctedAmount);
                transaction.setCorrectionNotice(noticeNumber);
                transaction.setTransactionDate(bl.getSailDate());
                transaction.setCustomerReferenceNo(bl.getExportReference());
                if (isHistory) {
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerReferenceNumber(bl.getExportReference());
                    arTransactionHistory.setBlNumber(blNumber);
                    arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                    arTransactionHistory.setPostedDate(postedDate);
                    arTransactionHistory.setTransactionDate(new Date());
                    arTransactionHistory.setTransactionAmount(correctedAmount);
                    arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                    arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                    arTransactionHistory.setCreatedBy(userName);
                    arTransactionHistory.setCreatedDate(new Date());
                    arTransactionHistory.setTransactionType(isCorrection ? "FCL CN" : "FCL BL");
                    arTransactionHistoryDAO.save(arTransactionHistory);
                }
            } else {
                Transaction transactionobject = new Transaction();
                PropertyUtils.copyProperties(transactionobject, transactionLedger);
                transactionobject.setCustomerReferenceNo(bl.getExportReference());
                transactionobject.setCorrectionFlag(isCorrection ? "Y" : "N");
                if (isCorrection && isCreditDebitNote) {
                    double oldAmount = transactionDAO.getTransactionAmt(transactionobject.getCustNo(), transactionobject.getBillLaddingNo(), "AR");
                    double correctedAmount = transactionAmount - oldAmount;
                    transactionobject.setTransactionAmt(correctedAmount);
                    transactionobject.setBalance(correctedAmount);
                    transactionobject.setBalanceInProcess(correctedAmount);
                } else {
                    transactionobject.setTransactionAmt(transactionAmount);
                    transactionobject.setBalance(transactionAmount);
                    transactionobject.setBalanceInProcess(transactionAmount);
                }
                transactionobject.setCorrectionNotice(noticeNumber);
                transactionobject.setTransactionDate(bl.getSailDate());
                String creditStatus = customerAccountingDAO.getCreditStatus(transactionobject.getCustNo());
                transactionobject.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? "N" : "Y");
                transactionDAO.save(transactionobject);
                if (isHistory) {
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerReferenceNumber(bl.getExportReference());
                    arTransactionHistory.setBlNumber(blNumber);
                    arTransactionHistory.setInvoiceDate(transactionobject.getTransactionDate());
                    arTransactionHistory.setPostedDate(postedDate);
                    arTransactionHistory.setTransactionDate(transactionobject.getTransactionDate());
                    arTransactionHistory.setTransactionAmount(transactionobject.getTransactionAmt());
                    arTransactionHistory.setCustomerNumber(transactionobject.getCustNo());
                    arTransactionHistory.setVoyageNumber(transactionobject.getVoyageNo());
                    arTransactionHistory.setCreatedBy(userName);
                    arTransactionHistory.setCreatedDate(new Date());
                    arTransactionHistory.setTransactionType(isCorrection ? "FCL CN" : "FCL BL");
                    arTransactionHistoryDAO.save(arTransactionHistory);
                }
            }
        }
    }

    public TransactionLedger currentCustomerNameAndNumber(String bolId) throws Exception {
        List<TransactionLedger> tList = transactionLedgerDAO.currentCustomerNameAndNumber(bolId);
        TransactionLedger transactionLedger = null;
        if (CommonFunctions.isNotNullOrNotEmpty(tList)) {
            transactionLedger = tList.get(0);
        }
        return transactionLedger;
    }

    public void createNagativeRecordsInTL(List<TransactionLedger> transactionLadgerList, FclBl bl, User user, boolean isCorrection) throws Exception {
        double historyAmount = 0d;
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        for (TransactionLedger transactionLedger : transactionLadgerList) {
            TransactionLedger newTransactionLedger = new TransactionLedger();
            PropertyUtils.copyProperties(newTransactionLedger, transactionLedger);
            newTransactionLedger.setTransactionId(null);
            newTransactionLedger.setJournalEntryNumber(null);
            newTransactionLedger.setLineItemNumber(null);
            newTransactionLedger.setPostedToGlDate(null);
            newTransactionLedger.setStatus(CommonConstants.STATUS_OPEN);
            newTransactionLedger.setPostedDate(postedDate);
            Double transactionAmt = transactionLedger.getTransactionAmt();
            historyAmount = historyAmount + transactionAmt;
            Double amount = (null != transactionAmt) ? 0 - transactionAmt : 0.0d;
            newTransactionLedger.setTransactionAmt(amount);
            transactionLedger.setManifestFlag("N");
            newTransactionLedger.setManifestFlag("N");
            newTransactionLedger.setCorrectionNotice(null);
            transactionLedger.setCorrectionNotice(null);
            transactionLedgerDAO.save(newTransactionLedger);
        }
        updateTransactionAmount(bl.getBolId(), bl.getFileNo(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, user.getLoginName(), true, isCorrection);
    }

    public void getFFCOMMAccrualList(FclBl fclBl, FclBlCorrections fclBlCorrections, String chargeCode) throws Exception {
        TransactionLedger transactionLedger = new TransactionLedger();
        transactionLedger.setBillLaddingNo(fclBl.getBolId());
        transactionLedger.setTransactionType(CommonConstants.TRANSACTION_TYPE_ACCRUALS);
        transactionLedger.setChargeCode(chargeCode);
        List<TransactionLedger> list = transactionLedgerDAO.getFFCommCostCodeAccruals(transactionLedger);
        if (CommonFunctions.isNotNullOrNotEmpty(list)) {
            TransactionLedger tranLedger = (TransactionLedger) list.get(0);
            if (!tranLedger.getCustNo().equals(fclBlCorrections.getAccountNumber())) {
                TradingPartner tradingPartner = new TradingPartnerDAO().findById(fclBlCorrections.getAccountNumber());
                if (null != tradingPartner && "Forwarder".equalsIgnoreCase(tradingPartner.getSubType())) {
                    TransactionLedger transactionLedgerNew = new TransactionLedger();
                    PropertyUtils.copyProperties(transactionLedgerNew, tranLedger);
                    transactionLedgerNew.setTransactionId(null);
                    transactionLedgerNew.setCustNo(fclBlCorrections.getAccountNumber());
                    transactionLedgerNew.setCustName(fclBlCorrections.getAccountName());
                    transactionLedgerNew.setCorrectionNotice(fclBlCorrections.getNoticeNo());
                    transactionLedgerDAO.save(transactionLedgerNew);
                    TransactionLedger transactionLedgerObject = new TransactionLedger();
                    PropertyUtils.copyProperties(transactionLedgerObject, tranLedger);
                    transactionLedgerObject.setTransactionId(null);
                    Double transactionAmt = tranLedger.getTransactionAmt();
                    Double amount = (null != transactionAmt) ? 0 - transactionAmt : 0.0d;
                    transactionLedgerObject.setTransactionAmt(amount);
                    transactionLedgerObject.setAccrualsCorrectionFlag("Y");
                    tranLedger.setAccrualsCorrectionFlag("Y");
                    tranLedger.setAccrualsCorrectionNumber(fclBl.getCorrectionNumber());
                    transactionLedgerObject.setAccrualsCorrectionNumber(fclBl.getCorrectionNumber());
                    transactionLedgerObject.setCorrectionNotice(fclBlCorrections.getNoticeNo());
                    transactionLedgerDAO.save(transactionLedgerObject);
                }
            }
        }
    }

    public void getADVFFAccrualList(FclBl fclBl, FclBlChargeBean fclBlCorrections, String chargeCode) throws Exception {
        Object object = transactionLedgerDAO.getSumOfAccruals(fclBl.getBolId(), chargeCode);
        if (null != object) {
            Double amount = (Double) object;
            if (amount > 0) {
                String[] ChargeCodes = {chargeCode};
                TransactionLedger transactionLedger = new TransactionLedger();
                transactionLedger.setBillLaddingNo(fclBl.getBolId());
                transactionLedger.setTransactionType(CommonConstants.TRANSACTION_TYPE_ACCRUALS);
                transactionLedger.setChargeCode(chargeCode);
                // transactionLedger.setNoticeNumber("No");
                List<TransactionLedger> list = transactionLedgerDAO.getCostCodeAccruals(transactionLedger);
//                      if(!CommonFunctions.isNotNullOrNotEmpty(list)){
//                         transactionLedger.setNoticeNumber(null);
//                         list = transactionLedgerDAO.getCostCodeAccruals(transactionLedger);
//                         }
                if (CommonFunctions.isNotNullOrNotEmpty(list)) {
                    reverseAccruals(fclBl, fclBlCorrections, list);
                }

            }
        }
    }

    public void reverseAccruals(FclBl fclBl, FclBlChargeBean fclBlCorrections, List<TransactionLedger> list) throws Exception {
        // String[] ChargeCodes = {FclBlConstants.ADVANCEFFCODE, FclBlConstants.ADVANCESHIPPERCODE};
        //List<TransactionLedger> list = transactionLedgerDAO.getTransactionListForPBASHIPER(fclBl.getBolId(), CommonConstants.TRANSACTION_TYPE_ACCRUALS, ChargeCodes, null);
        FclBlBC fclBlBC = new FclBlBC();
        for (TransactionLedger transactionLedger : list) {
            if (fclBlCorrections != null && fclBlCorrections.getChargeCode() != null && fclBlCorrections.getChargeCode().equalsIgnoreCase(transactionLedger.getChargeCode())) {
                TransactionLedger transactionLedgerNew = new TransactionLedger();
                PropertyUtils.copyProperties(transactionLedgerNew, transactionLedger);
                transactionLedgerNew.setTransactionId(null);
                if (fclBlCorrections.getChargeAmt() != null) {
                    transactionLedgerNew.setTransactionAmt(new Double(fclBlCorrections.getChargeAmt()));
                    //transactionLedgerNew.setNoticeNumber("No");
                }
                transactionLedgerNew.setCorrectionNotice(fclBl.getCorrectionNumber());
                transactionLedgerDAO.save(transactionLedgerNew);
            } else {
                TransactionLedger transactionLedger2 = new TransactionLedger();
                PropertyUtils.copyProperties(transactionLedger2, transactionLedger);
                transactionLedger2.setTransactionId(null);
                Double transactionAmt = transactionLedger.getTransactionAmt();
                Double amount = (null != transactionAmt) ? 0 - transactionAmt : 0.0d;
                transactionLedger2.setTransactionAmt(amount);
                transactionLedger2.setCorrectionNotice(fclBl.getCorrectionNumber());
                transactionLedger2.setAccrualsCorrectionFlag("Y");
                transactionLedger.setAccrualsCorrectionFlag("Y");
                transactionLedger2.setAccrualsCorrectionNumber(fclBl.getCorrectionNumber());
                transactionLedger.setAccrualsCorrectionNumber(fclBl.getCorrectionNumber());
                transactionLedgerDAO.save(transactionLedger2);
            }

//            }
        }
    }

    public void deleteTransationRecord(FclBl fclBl, String chargeCode) throws Exception {
        String[] ChargeCodes = {chargeCode};
        List<TransactionLedger> list = transactionLedgerDAO.getTransactionListForPBASHIPER(fclBl.getBolId(), CommonConstants.TRANSACTION_TYPE_ACCRUALS, ChargeCodes, null);
        for (TransactionLedger transactionLedger : list) {
            if (CommonUtils.isEqualIgnoreCase(transactionLedger.getStatus(), STATUS_OPEN)) {
                transactionLedgerDAO.delete(transactionLedger);
            }
        }
    }

    /**
     * @param customerNumber displaying records for credtiDebit Notes
     */
    public List<TransactionLedger> getListToDisplayOnCreditDebitReport(String customerNumber,
            FclBlCorrections fclBlCorrections, String noticeNumber, MessageResources messageResources, String debitOrCredit) throws Exception {
        List<TransactionLedger> transactionLedgersList = new ArrayList<TransactionLedger>();
        if (null != fclBlCorrections.getCorrectionType() && null != fclBlCorrections.getCorrectionType().getCode()
                && (fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("A") || fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("Y"))) {
            // getting current list
            String previousNoticeNo = new FclBlCorrectionsDAO().getPreviousNoticeNo(fclBlCorrections.getFileNo(), fclBlCorrections.getNoticeNo());
            String blNO = fclBlCorrections.getBlNumber() + (CommonUtils.isNotEmpty(previousNoticeNo) ? (FclBlConstants.DELIMITER + previousNoticeNo) : "");
            FclBl fclBl = new FclBlDAO().findById(blNO);
            FclBlCorrectionsBC fbcbc = new FclBlCorrectionsBC();
            List<FclBlCorrections> blCorrectionsList = null;
            if (fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("Y")) {
                blCorrectionsList = fbcbc.getChargesListFotTypeY(fclBl, fclBlCorrections.getBlNumber(), fclBlCorrections.getNoticeNo(), messageResources);
            } else {
                blCorrectionsList = fbcbc.getChargesListForTypeAViewMode(fclBl, fclBlCorrections.getBlNumber(), fclBlCorrections.getNoticeNo(),messageResources);
            }
            if (CommonFunctions.isNotNullOrNotEmpty(blCorrectionsList)) {
                for (FclBlCorrections fclBlCorrectionsObject : blCorrectionsList) {
                    TransactionLedger transactionLedger = new TransactionLedger();
                    transactionLedger.setChargeCode(fclBlCorrectionsObject.getChargeCode());
                    transactionLedger.setTransactionAmt(fclBlCorrectionsObject.getAmount());
                    if (fclBlCorrectionsObject.getNewAmount() == null) {
                        transactionLedger.setNewAmount(fclBlCorrectionsObject.getAmount());
                    } else {
                        transactionLedger.setNewAmount(fclBlCorrectionsObject.getNewAmount());
                    }
                    transactionLedger.setDifferenceAmount(fclBlCorrectionsObject.getDiffereceAmount());

                    transactionLedgersList.add(transactionLedger);
                }

            }
        } else {
            transactionLedgersList = transactionLedgerDAO.getListOfTaransactionToDisplayOnCDReport(fclBlCorrections.getBlNumber(), noticeNumber, customerNumber);
            Map<String, TransactionLedger> map = new HashMap<String, TransactionLedger>();
            if (CommonFunctions.isNotNullOrNotEmpty(transactionLedgersList)) {
                for (TransactionLedger transactionLedger : transactionLedgersList) {
                    if (null != fclBlCorrections.getCorrectionType() && FclBlConstants.DEBITNOTE.equalsIgnoreCase(debitOrCredit) && ("C".equals(fclBlCorrections.getCorrectionType().getCode()) || "D".equals(fclBlCorrections.getCorrectionType().getCode()) || "Q".equals(fclBlCorrections.getCorrectionType().getCode()) || "R".equals(fclBlCorrections.getCorrectionType().getCode()) || "S".equals(fclBlCorrections.getCorrectionType().getCode())) && "CAF".equalsIgnoreCase(transactionLedger.getChargeCode())) {
                    } else {
                        if (map.containsKey(transactionLedger.getChargeCode())) {
                            TransactionLedger tempTransactionLedger = map.get(transactionLedger.getChargeCode());
                            if (transactionLedger.getTransactionAmt() != null) {
                                tempTransactionLedger.setNewAmount(transactionLedger.getTransactionAmt() + tempTransactionLedger.getNewAmount());
                            }
                        } else {
                            transactionLedger.setNewAmount(transactionLedger.getTransactionAmt());
                            map.put(transactionLedger.getChargeCode(), transactionLedger);
                        }
                    }
                }
            }
            Set<String> set = map.keySet();
            transactionLedgersList = new ArrayList<TransactionLedger>();
            if (CommonFunctions.isNotNullOrNotEmpty(set)) {
                for (String chargeCode : set) {
                    transactionLedgersList.add(map.get(chargeCode));
                }
            }
        }
        //---------------------------------------
        return transactionLedgersList;
    }

    public void getAccrualsForCorrectionTypeT(FclBl bl, FclBlCorrections fclBlCorrections) throws Exception {
        TransactionLedger transactionLedger = new TransactionLedger();
        transactionLedger.setBillLaddingNo(bl.getBolId());
        transactionLedger.setTransactionType("AC");
        Double object = null;
        if (fclBlCorrections.getCorrectionType() != null && fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("T")) {
            transactionLedger.setCustNo(bl.getForwardAgentNo());
            object = (Double) transactionLedgerDAO.getSumOfAccrualsForFF(bl.getBolId(), FclBlConstants.ADVANCEFFCODE);
        } else if (fclBlCorrections.getCorrectionType() != null && fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("U")) {
            if ("I".equalsIgnoreCase(bl.getImportFlag())) {
                transactionLedger.setCustNo(bl.getHouseShipper());
            } else {
                transactionLedger.setCustNo(bl.getShipperNo());
            }
            object = (Double) transactionLedgerDAO.getSumOfAccruals(bl.getBolId(), FclBlConstants.ADVANCESHIPPERCODE);
        }

        List<TransactionLedger> accrualList = transactionLedgerDAO.getCostCodeAccruals(transactionLedger);
        if (object != null && object > 0) {
            Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
            for (TransactionLedger transactionLedger1 : accrualList) {
                TransactionLedger transactionLedgerNew = new TransactionLedger();
                PropertyUtils.copyProperties(transactionLedgerNew, transactionLedger1);
                transactionLedgerNew.setTransactionId(null);
                transactionLedgerNew.setCustNo(fclBlCorrections.getAccountNumber());
                transactionLedgerNew.setCustName(fclBlCorrections.getAccountName());
                transactionLedgerNew.setCorrectionNotice(fclBlCorrections.getNoticeNo());
                transactionLedgerNew.setJournalEntryNumber(null);
                transactionLedgerNew.setLineItemNumber(null);
                transactionLedgerNew.setPostedToGlDate(null);
                transactionLedgerNew.setTransactionDate(new Date());
                transactionLedgerNew.setPostedDate(postedDate);
                transactionLedgerNew.setStatus(CommonConstants.STATUS_OPEN);
                transactionLedgerDAO.save(transactionLedgerNew);

                TransactionLedger transactionLedgerObject = new TransactionLedger();
                PropertyUtils.copyProperties(transactionLedgerObject, transactionLedger1);
                transactionLedgerObject.setTransactionId(null);
                Double transactionAmt = transactionLedger1.getTransactionAmt();
                Double amount = (null != transactionAmt) ? 0 - transactionAmt : 0.0d;
                transactionLedgerObject.setTransactionAmt(amount);
                transactionLedgerObject.setAccrualsCorrectionFlag("Y");
                transactionLedger1.setAccrualsCorrectionFlag("Y");
                transactionLedger1.setAccrualsCorrectionNumber(bl.getCorrectionNumber());
                transactionLedgerObject.setAccrualsCorrectionNumber(bl.getCorrectionNumber());
                transactionLedgerObject.setCorrectionNotice(fclBlCorrections.getNoticeNo());
                transactionLedgerObject.setJournalEntryNumber(null);
                transactionLedgerObject.setLineItemNumber(null);
                transactionLedgerObject.setPostedToGlDate(null);
                transactionLedgerObject.setTransactionDate(new Date());
                transactionLedgerObject.setPostedDate(postedDate);
                transactionLedgerObject.setStatus(CommonConstants.STATUS_OPEN);
                transactionLedgerDAO.save(transactionLedgerObject);
            }
        }
    }
}

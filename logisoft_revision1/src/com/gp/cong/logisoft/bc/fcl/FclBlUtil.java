package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadApplicationProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.logiware.hibernate.dao.ArRedInvoiceChargesDAO;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import com.gp.cvst.logisoft.beans.Comparator;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.FclManifestDAO;
import com.logiware.bc.EventsBC;
import com.logiware.form.FclBlForm;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.dao.FclDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class FclBlUtil {

    private static final Logger log = Logger.getLogger(FclBlUtil.class);
    FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
    FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();

    public void calculateCAF(Integer bolId, String destination) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        if (null != destination && destination.lastIndexOf("(") > -1) {
            String destinationCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            double adjustmentFactor = null != portsDAO.getAdjustmentFactor(destinationCode) ? portsDAO.getAdjustmentFactor(destinationCode) : 0d;
            if (adjustmentFactor != 0d) {
                List l = fclBlChargesDAO.sumOFChargesForCafCalculation(bolId);
                if (!l.isEmpty()) {
                    Object[] object = (Object[]) l.get(0);
                    double sumOfCharges = (Double) object[0];
                    String billTo = (String) object[1];
                    double cafAmount = sumOfCharges * adjustmentFactor;
                    FclBlCharges fclBlCharges = new FclBlCharges();
                    fclBlCharges.setBolId(bolId);
                    fclBlCharges.setChargeCode("CAF");
                    fclBlCharges.setCharges("CAF");
                    fclBlCharges.setCurrencyCode("USD");
                    fclBlCharges.setPrintOnBl("yes");
//                    fclBlCharges.setReadOnlyFlag("on");
                    fclBlCharges.setBookingFlag("new");
                    fclBlCharges.setAmount(cafAmount);
                    fclBlCharges.setOldAmount(cafAmount);
                    fclBlCharges.setBillTo(billTo);
                    fclBlCharges.setPcollect("prepaid");
                    fclBlChargesDAO.save(fclBlCharges);
                }

            }
        }
    }

    public void recalculateCAF(Integer bolId, String destination) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        if (null != destination) {
            String destinationCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            double adjustmentFactor = portsDAO.getAdjustmentFactor(destinationCode);
            if (adjustmentFactor != 0d) {
                List l = fclBlChargesDAO.sumOFChargesForCafCalculation(bolId);
                if (!l.isEmpty()) {
                    Object[] object = (Object[]) l.get(0);
                    double sumOfCharges = (Double) object[0];
                    double cafAmount = sumOfCharges * adjustmentFactor;
                    List list = fclBlChargesDAO.findByPropertyAndBlNumber("chargeCode", "CAF", bolId);
                    if (!list.isEmpty()) {
                        FclBlCharges fclBlCharges = (FclBlCharges) list.get(0);
                        fclBlCharges.setAmount(cafAmount);
                        fclBlCharges.setOldAmount(cafAmount);
                    } else {
                        FclBlCharges fclBlCharges = new FclBlCharges();
                        fclBlCharges.setBolId(bolId);
                        fclBlCharges.setChargeCode("CAF");
                        fclBlCharges.setCharges("CAF");
                        fclBlCharges.setCurrencyCode("USD");
                        fclBlCharges.setPrintOnBl("yes");
                        //                    fclBlCharges.setReadOnlyFlag("on");
//                        fclBlCharges.setBookingFlag("new");
                        fclBlCharges.setAmount(cafAmount);
                        fclBlCharges.setOldAmount(cafAmount);
                        fclBlCharges.setBillTo("Agent");
                        fclBlCharges.setPcollect("prepaid");
                        fclBlChargesDAO.save(fclBlCharges);
                    }
                }

            }
        }
    }

    public void calculateChargesAndProfit(FclBl fclBl, HttpServletRequest request, MessageResources messageResources) throws Exception {
        double currentSumOfCharges = 0d;
        FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
        DecimalFormat df = new DecimalFormat("0.00");
        String id = new GenericCodeDAO().getFieldByCodeAndCodetypeId("Bl Correction Type", "A", "id");
        String noticeNumber = null;
        if (null != id) {
            Integer correctionNumber = fclBlCorrectionsDAO.getNoticeNumberForLatestAtypeCorrection(fclBl.getBolId(), Integer.parseInt(id));
            if (null != correctionNumber) {
                noticeNumber = "" + correctionNumber;
            }
        }
        if (null != noticeNumber) {
            String blNO = fclBl.getBolId() + FclBlConstants.DELIMITER + noticeNumber;
            FclBl bl = new FclBlDAO().findById(blNO);
//            List<FclBlCorrections> correctionList = new FclBlCorrectionsBC().getChargesListFotTypeA(fcl, fcl.getBolId(), noticeNumber);
            if (null != bl) {
                Object totalAmount = fclBlChargesDAO.sumOFCharges(bl.getBol());
                if (null != totalAmount) {
                    currentSumOfCharges = (Double) totalAmount;
                }
                request.setAttribute("correctedAmount", currentSumOfCharges);
            } else {
                Object totalAmount = fclBlChargesDAO.sumOFCharges(fclBl.getBol());
                if (null != totalAmount) {
                    currentSumOfCharges = (Double) totalAmount;
                }
            }
        } else {
            Object totalAmount = fclBlChargesDAO.sumOFCharges(fclBl.getBol());
            if (null != totalAmount) {
                currentSumOfCharges = (Double) totalAmount;
            }
        }
        double invoiceSumOfCharges = 0d;
        Object invoiceAmount = new ArRedInvoiceChargesDAO().sumOfCharges("04-" + fclBl.getFileNo());
        if (null != invoiceAmount) {
            BigDecimal sumOfCharges = (BigDecimal) invoiceAmount;
            invoiceSumOfCharges = sumOfCharges.doubleValue();
            currentSumOfCharges = currentSumOfCharges + invoiceSumOfCharges;
            if (invoiceSumOfCharges != 0d) {
                request.setAttribute("invoiceAmount", df.format(invoiceSumOfCharges));
            }
        }
        Object costAmount = fclBlCostCodesDAO.sumOfCost(fclBl.getBol());
        if (null != costAmount) {
            double totalCost = (Double) costAmount;
            double profit = currentSumOfCharges - totalCost;
            request.setAttribute("profit", df.format(profit));
            request.setAttribute("totalCost", df.format(totalCost));
        }
        request.setAttribute("currentSumOfCharges", df.format(currentSumOfCharges));
        FclBlCorrections fclBlCorrections = fclBlCorrectionsDAO.getLatestPostedCorrection(fclBl.getBolId(), Integer.parseInt(id));
        if (null != fclBlCorrections && CommonUtils.isNotEmpty(fclBlCorrections.getAccountNumber()) && !"A".equals(fclBlCorrections.getCorrectionType().getCode())) {
            request.setAttribute("latestCorrection", fclBlCorrections);
        }
        Object sslCostAmount = 0d;
        String sslBlPrepaidCollectName = "";
        if ("P-Prepaid".equalsIgnoreCase(fclBl.getStreamShipBl())) {
            sslCostAmount = fclBlCostCodesDAO.sumOfSSLCost(fclBl.getBol(), fclBl.getSslineNo(), "DEFER");
            sslBlPrepaidCollectName = fclBl.getSslineNo();
        } else {
            sslCostAmount = fclBlCostCodesDAO.sumOfSSLCost(fclBl.getBol(), fclBl.getAgentNo(), "FAECOMM");
            sslBlPrepaidCollectName = fclBl.getAgentNo();
        }
        request.setAttribute("sslBlPrepaidCollectName", sslBlPrepaidCollectName);
        if (CommonUtils.isNotEmpty((Double) sslCostAmount)) {
            double amount = (Double) sslCostAmount;
            request.setAttribute("totalCostForSSL", "$ " + df.format(amount));
        } else {
            request.setAttribute("totalCostForSSL", "$ 0.00");
        }

    }

    public void calculateCostChargesAndProfit(FclBlNew fclBl, HttpServletRequest request) throws Exception {
        double currentSumOfCharges = 0d;
        FclBlCorrectionsDAO fclBlCorrectionsDAO = new FclBlCorrectionsDAO();
        DecimalFormat df = new DecimalFormat("0.00");
        String noticeNumber = null;
        Integer correctionNumber = fclBlCorrectionsDAO.getNoticeNumberForLatestCorrection(fclBl.getBolId());
        if (null != correctionNumber) {
            noticeNumber = "" + correctionNumber;
        }
        if (null != noticeNumber) {
            String blNO = fclBl.getBolId() + FclBlConstants.DELIMITER + noticeNumber;
            FclBl bl = new FclBlDAO().findById(blNO);
            if (null != bl) {
                Object totalAmount = fclBlChargesDAO.sumOFCharges(bl.getBol());
                if (null != totalAmount) {
                    currentSumOfCharges = (Double) totalAmount;
                }
                request.setAttribute("correctedAmount", currentSumOfCharges);
            } else {
                Object totalAmount = fclBlChargesDAO.sumOFCharges(fclBl.getBol());
                if (null != totalAmount) {
                    currentSumOfCharges = (Double) totalAmount;
                }
            }
        } else {
            Object totalAmount = fclBlChargesDAO.sumOFCharges(fclBl.getBol());
            if (null != totalAmount) {
                currentSumOfCharges = (Double) totalAmount;
            }
        }
        double invoiceSumOfCharges = 0d;
        Object invoiceAmount = new ArRedInvoiceChargesDAO().sumOfCharges("04-" + fclBl.getFileNo());
        if (null != invoiceAmount) {
            BigDecimal sumOfCharges = (BigDecimal) invoiceAmount;
            invoiceSumOfCharges = sumOfCharges.doubleValue();
            currentSumOfCharges = currentSumOfCharges + invoiceSumOfCharges;
            if (invoiceSumOfCharges != 0d) {
                request.setAttribute("invoiceAmount", df.format(invoiceSumOfCharges));
            }
        }
        List fclCostCodeList = fclBlCostCodesDAO.findByProperty("bolId", fclBl.getBol());
        double costAmt = 0d;
        for (Iterator iterator = fclCostCodeList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (!"yes".equalsIgnoreCase(fclBlCostCodes.getDeleteFlag())) {
                costAmt += fclBlCostCodes.getAmount();
            }
        }
//        Object costAmount = fclBlCostCodesDAO.sumOfCost(fclBl.getBol());
        if (costAmt != 0) {
            double totalCost = costAmt;
            double profit = currentSumOfCharges - costAmt;
            request.setAttribute("profit", df.format(profit));
            request.setAttribute("totalCost", df.format(totalCost));
        } else {
            double profit = currentSumOfCharges;
            if (profit != 0) {
                request.setAttribute("profit", df.format(profit));
            } else {
                request.setAttribute("profit", "0.00");
            }
            request.setAttribute("totalCost", "0.00");
        }
        request.setAttribute("currentSumOfCharges", df.format(currentSumOfCharges));
        FclBlCorrections fclBlCorrections = fclBlCorrectionsDAO.getLatestPostedCorrectionByBl(fclBl.getBolId());
        if (null != fclBlCorrections && CommonUtils.isNotEmpty(fclBlCorrections.getAccountNumber()) && !"A".equals(fclBlCorrections.getCorrectionType().getCode())) {
            request.setAttribute("latestCorrection", fclBlCorrections);
        }
        Object sslCostAmount = 0d;
        String sslBlPrepaidCollectName = "";
        if ("P-Prepaid".equalsIgnoreCase(fclBl.getStreamShipBL())) {
            sslCostAmount = fclBlCostCodesDAO.sumOfSSLCost(fclBl.getBol(), fclBl.getSslineNo(), "DEFER");
            sslBlPrepaidCollectName = fclBl.getSslineNo();
        } else {
            sslCostAmount = fclBlCostCodesDAO.sumOfSSLCost(fclBl.getBol(), fclBl.getAgentNo(), "FAECOMM");
            sslBlPrepaidCollectName = fclBl.getAgentNo();
        }
        request.setAttribute("sslBlPrepaidCollectName", sslBlPrepaidCollectName);
        if (CommonUtils.isNotEmpty((Double) sslCostAmount)) {
            double amount = (Double) sslCostAmount;
            request.setAttribute("totalCostForSSL", "$ " + df.format(amount));
        } else {
            request.setAttribute("totalCostForSSL", "$ 0.00");
        }
        //Total Revenue Calculation
        List multipleBlList = new FclBlDAO().getAllBlUsingFileNumber(fclBl.getFileNo() + "-");
        if (!multipleBlList.isEmpty()) {
            for (Object object : multipleBlList) {
                FclBl bl = (FclBl) object;
                if (null != bl.getBolId() && !bl.getBolId().contains("==")) {
                    noticeNumber = null;
                    correctionNumber = fclBlCorrectionsDAO.getNoticeNumberForLatestCorrection(bl.getBolId());
                    if (null != correctionNumber) {
                        noticeNumber = "" + correctionNumber;
                    }
                    if (null != noticeNumber) {
                        String blNO = bl.getBolId() + FclBlConstants.DELIMITER + noticeNumber;
                        FclBl correctedBl = new FclBlDAO().findById(blNO);
                        if (null != correctedBl) {
                            Object totalAmount = fclBlChargesDAO.sumOFCharges(correctedBl.getBol());
                            if (null != totalAmount) {
                                currentSumOfCharges += (Double) totalAmount;
                            }
                            request.setAttribute("correctedAmount", currentSumOfCharges);
                        } else {
                            Object totalAmount = fclBlChargesDAO.sumOFCharges(bl.getBol());
                            if (null != totalAmount) {
                                currentSumOfCharges += (Double) totalAmount;
                            }
                        }
                    } else {
                        Object totalAmount = fclBlChargesDAO.sumOFCharges(bl.getBol());
                        if (null != totalAmount) {
                            currentSumOfCharges += (Double) totalAmount;
                        }
                    }
                    invoiceAmount = new ArRedInvoiceChargesDAO().sumOfCharges("04-" + bl.getFileNo());
                    if (null != invoiceAmount) {
                        BigDecimal sumOfCharges = (BigDecimal) invoiceAmount;
                        invoiceSumOfCharges += sumOfCharges.doubleValue();
                        currentSumOfCharges = currentSumOfCharges + sumOfCharges.doubleValue();
                    }
                }
            }
            request.setAttribute("multipleBl", "multipleBl");
            if (invoiceSumOfCharges != 0d) {
                request.setAttribute("invoiceAmount", df.format(invoiceSumOfCharges));
            }
            request.setAttribute("currentSumOfCharges", df.format(currentSumOfCharges));
            if (costAmt != 0) {
                double totalCost = costAmt;
                double profit = currentSumOfCharges - totalCost;
                request.setAttribute("profit", df.format(profit));
            }
        }

    }

    public void calculateInsurance(Integer bol, HttpServletRequest request, String userName) throws Exception {
        FclBl bl = new FclBlDAO().findById(bol);
        Double currentSumOfCharges = 0.0;
        DecimalFormat formatter = new DecimalFormat("#0.00");
        Object totalAmount = new FclBlChargesDAO().sumOFCharges(bl.getBol());
        if (null != totalAmount && null != bl.getCostOfGoods() && null != bl.getInsuranceRate()) {
            currentSumOfCharges = (Double) totalAmount;
            Double a = bl.getCostOfGoods();
            Double insureAmt = bl.getInsuranceRate();
            Double b = currentSumOfCharges;
            Double c = ((a + b) * insureAmt) / 100;
            Double d = ((a + b + c) * 10) / 100;
            Double cif = a + b + c + d;
            Double insuranceCharge = Double.parseDouble(formatter.format((cif * insureAmt) / 100));
            List l = new FclBlChargesDAO().findByPropertyAndBlNumber("chargeCode", "INSURE", bl.getBol());
            FclBlCharges fclBlCharges = null;
            Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
            if (!l.isEmpty()) {
                fclBlCharges = (FclBlCharges) l.get(0);
                double balanceAmount = insuranceCharge - fclBlCharges.getAmount();
                fclBlCharges.setAmount(insuranceCharge);
                fclBlCharges.setOldAmount(insuranceCharge);
                if ("M".equals(bl.getReadyToPost())) {
                    TransactionLedger transactionLedger = new TransactionLedgerDAO().getArTaransactionByChargeId(fclBlCharges.getChargesId());
                    if (null != transactionLedger) {
                        transactionLedger.setTransactionAmt(insuranceCharge);
                        Transaction transaction = new TransactionDAO().findByTransactionByBillNoAndCustomer(bl.getBolId(), transactionLedger.getCustNo());
                        if (null != transaction) {
                            transaction.setTransactionAmt(transaction.getTransactionAmt() + balanceAmount);
                            transaction.setBalance(transaction.getBalance() + balanceAmount);
                            transaction.setBalanceInProcess(transaction.getBalanceInProcess() + balanceAmount);
                            if (fclBlCharges.getAmount() != 0d) {
                                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                                arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                                arTransactionHistory.setPostedDate(postedDate);
                                arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                                arTransactionHistory.setTransactionAmount(balanceAmount);
                                arTransactionHistory.setBlNumber(bl.getBolId());
                                arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                                arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                                arTransactionHistory.setCreatedBy(userName);
                                arTransactionHistory.setCreatedDate(new Date());
                                arTransactionHistory.setTransactionType("FCL BL");
                                new ArTransactionHistoryDAO().save(arTransactionHistory);
                            }

                        }
                    }
                }
            } else {
                fclBlCharges = new FclBlCharges();
                fclBlCharges.setBolId(bl.getBol());
                fclBlCharges.setAmount(insuranceCharge);
                fclBlCharges.setOldAmount(insuranceCharge);
                fclBlCharges.setCharges("INSURANCE");
                fclBlCharges.setChargeCode("INSURE");
                fclBlCharges.setCurrencyCode("USD");
                fclBlCharges.setPrintOnBl("Yes");
                if ("F".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.FORWARDER);
                } else if ("S".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.SHIPPER);
                } else if ("T".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.THIRDPARTY);
                } else if ("C".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(FclBlConstants.CONSIGNEE);
                } else if ("A".equalsIgnoreCase(bl.getBillToCode())) {
                    fclBlCharges.setBillTo(CommonConstants.AGENT);
                }
                if (null != bl && "M".equals(bl.getReadyToPost())) {
                    bl.setCorrectedAfterManifest("Y");
                    fclBlCharges.setReadyToPost("M");
                    List fclBlChargesSummeryList = new ArrayList();
                    fclBlChargesSummeryList.add(fclBlCharges);
                    if (null != bl && "M".equals(bl.getReadyToPost())) {
                        new ManifestBC().getTransactionObject(bl, new FclBlBC().getFclBlCostBeanobject(fclBlChargesSummeryList, null, null, null, null), request);
                        TransactionLedger transactionLedger = new TransactionLedgerDAO().getArTaransactionByChargeId(fclBlCharges.getChargesId());
                        Transaction transaction = new TransactionDAO().findByTransactionByBillNoAndCustomer(bl.getBolId(), transactionLedger.getCustNo());
                        if (null != transaction) {
                            transaction.setTransactionAmt(transaction.getTransactionAmt() + fclBlCharges.getAmount());
                            transaction.setBalance(transaction.getBalance() + fclBlCharges.getAmount());
                            transaction.setBalanceInProcess(transaction.getBalanceInProcess() + fclBlCharges.getAmount());
                            if (fclBlCharges.getAmount() != 0d) {
                                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                                arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
                                arTransactionHistory.setPostedDate(postedDate);
                                arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
                                arTransactionHistory.setTransactionAmount(fclBlCharges.getAmount());
                                arTransactionHistory.setBlNumber(bl.getBolId());
                                arTransactionHistory.setCustomerNumber(transaction.getCustNo());
                                arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
                                arTransactionHistory.setCreatedBy(userName);
                                arTransactionHistory.setCreatedDate(new Date());
                                arTransactionHistory.setTransactionType("FCL BL");
                                new ArTransactionHistoryDAO().save(arTransactionHistory);
                            }

                        }
                    }
                }
                new FclBlChargesDAO().save(fclBlCharges);
            }
        }
    }

    public void doManifest(FclBlForm fclBlForm, MessageResources messageResources, HttpServletRequest request, User user, String realPath) throws Exception {
        FclBlBC fclBlBC = new FclBlBC();
        FclBl bl = new FclBlDAO().findById(fclBlForm.getFclBl().getBolId());
        fclBlBC.calculateDateOfYard(bl);
	fclBlBC.manifest(bl,user);
        setRevisionFlagAndSendFreightPDF(fclBlForm.getAction(), request, bl, user, realPath, "manifest", fclBlForm.getFreightInvoiceContacts());
    }

    public void doUnManifest(FclBlForm fclBlForm, MessageResources messageResources, HttpServletRequest request, User user, String realPath) throws Exception {
        FclBlBC fclBlBC = new FclBlBC();
        FclBl bl = new FclBlDAO().findById(fclBlForm.getFclBl().getBolId());
        fclBlBC.unmanifest(bl, user);
        List<FclBlCharges> chargeList = (null != bl.getFclcharge()) ? new ArrayList(bl.getFclcharge()) : Collections.EMPTY_LIST;
        fclBlBC.deleteChargeCode(FclBlConstants.PERDMCODE, chargeList, bl);
        String manifestRev = setRevisionFlagAndSendFreightPDF("", null, bl, null, null, "unManifest", null);
        fclBlForm.getFclBl().setManifestRev(manifestRev);
    }

    public FclBl createMultipleBl(String bolId, User user) throws Exception {
        List fclBl = new FclBlDAO().findBolId(bolId);
        FclBl saveFclBl = new FclBl();
        if (fclBl.size() > 0) {
            saveFclBl = (FclBl) fclBl.get(0);
        }
        ProcessInfoBC processInfoBC = new ProcessInfoBC();
        if (CommonFunctions.isNotNull(saveFclBl.getFileNo())) {
            Integer userId = (user != null) ? user.getUserId() : 0;
            processInfoBC.releaseLoack(LoadApplicationProperties.getProperty("lockFclBlModule"),
                    saveFclBl.getFileNo(), userId);
        }
        return new FclBlBC().copy(saveFclBl, bolId);
    }

    public void updateDeleteFlag(FclBl fclBl) throws Exception {
        List costList = fclBlCostCodesDAO.findByParentIdforManifest(fclBl.getBol());
        double resetAmount = 0.0;
        for (Iterator iterator = costList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (fclBlCostCodes != null) {
                fclBlCostCodesDAO.deleteUpdatedFlag(fclBlCostCodes.getCodeId());
                fclBlCostCodesDAO.resetOldAmount(fclBlCostCodes.getCodeId(), resetAmount);
            }
        }
    }

    public String setRevisionFlagAndSendFreightPDF(String sendNotSend, HttpServletRequest request, FclBl fclBl,
            User user, String realPath, String action, String frieghtInvoiceContacts) throws Exception {
        if (CommonFunctions.isNotNull(fclBl.getManifestRev())) {
            if (action != null && action.equalsIgnoreCase("manifest")) {
                if (!"I".equalsIgnoreCase(fclBl.getImportFlag())) {
                    new EventsBC().sendManifestEmail(sendNotSend, fclBl, user, realPath, request, frieghtInvoiceContacts);
                }

            } else if (action != null && action.equalsIgnoreCase("unManifest")) {
                if (CommonFunctions.isNotNull(fclBl.getManifestRev())) {
                    Integer revsionNumber = new Integer(fclBl.getManifestRev());
                    revsionNumber = (revsionNumber != null) ? revsionNumber + 1 : revsionNumber;
                    fclBl.setManifestRev(revsionNumber != null ? revsionNumber.toString() : null);
                }
            }
        } else {
            fclBl.setManifestRev("0");
        }
        return fclBl.getManifestRev();
    }

    public void getContactConfigDetailsForCodeC(List costList, FclBlNew fclBl, HttpServletRequest request) throws Exception {
        //------------------- 1-----------------
        // seeting message if there is no changes in charges and bill to partygetCustomerAccountNo
        if (CommonUtils.isNotEmpty(costList)) {
            List li = new FclBlChargesDAO().getDistinctBillToParty(fclBl.getBol().toString());
            if (fclBl.getManifestRev() != null && !fclBl.getManifestRev().equals("0") && !CommonFunctions.isNotNullOrNotEmpty(li)) {
                request.setAttribute("invoiceMessage", "Freight Invoice will NOT be sent because of no changes to charges");
            } else {
                List billToList = new FclBlChargesDAO().getDistinctBillTo(fclBl.getBol().toString());
                List<CustomerContact> returnList = new ArrayList();
                if (billToList.size() > 0) {
                    for (Iterator iter = billToList.iterator(); iter.hasNext();) {
                        String Object = (String) iter.next();
                        if (null != Object) {
                            if (Object.equalsIgnoreCase(CommonConstants.FORWARDER)) {
                                returnList.addAll(checkForE1andE2OfCodeC(fclBl.getForwardagentNo(),fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.SHIPPER)) {
                                String customerNumber = "I".equalsIgnoreCase(fclBl.getImportFlag()) ? fclBl.getShipperNo()
                                        : fclBl.getHouseShipperNo();
                                returnList.addAll(checkForE1andE2OfCodeC(customerNumber,fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.THIRDPARTY)) {
                                returnList.addAll(checkForE1andE2OfCodeC(fclBl.getBillTrdPrty(),fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                                String customerNumber = fclBl.getHouseConsignee();
                                returnList.addAll(checkForE1andE2OfCodeC(customerNumber,fclBl.getImportFlag()));
                            } else if (Object.equalsIgnoreCase(CommonConstants.AGENT)) {
                                String customerNumber = fclBl.getAgentNo();
                                returnList.addAll(checkForE1andE2OfCodeC(customerNumber,fclBl.getImportFlag()));
                            }
                        }
                    }
                }
                // getting email from booking and displaying
                if (CommonFunctions.isNotNull(fclBl.getSendCopyTo())) {
                    CustomerContact customerContact = new CustomerContact();
                    customerContact.setAccountName(fclBl.getSendCopyTo());
                    returnList.add(customerContact);
                }
                if (CommonUtils.isNotEmpty(returnList)) {
                    request.setAttribute("ContactConfigE1andE2", returnList);
                }
            }
        } else {
            request.setAttribute("manifestWithoutCharges", "yes");
        }
    }

    public List checkForE1andE2OfCodeC(String accountNo,String importFlag) throws Exception {
        // ------------------ 2 ------------------------------
        List contactConfigList = new ArrayList();
        List resultList = new ArrayList();
        TradingPartner tradingPartner = new TradingPartner();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != accountNo) {
            tradingPartner = tradingPartnerDAO.findById(accountNo);
            if (tradingPartner != null) {
                String accountingEmail = tradingPartnerDAO.getAccountingEmail(accountNo);
                String accountingFax = tradingPartnerDAO.getAccountingFax(accountNo);
                contactConfigList.addAll(tradingPartner.getCustomerContact());
                for (Iterator iter = contactConfigList.iterator(); iter.hasNext();) {
                    CustomerContact customerContact = (CustomerContact) iter.next();
                    customerContact.setAccountName(tradingPartner.getAccountName());
                    customerContact.setAccountNo(tradingPartner.getAccountno());
                    customerContact.setAccountType(tradingPartner.getAcctType());
                    customerContact.setSubType(tradingPartner.getSubType());
                    String code = (null != customerContact.getCodek()) ? customerContact.getCodek().getCode() : null;
                    if ("I".equalsIgnoreCase(importFlag)) {
                        if (null != code && CommonUtils.in(code, "E", "F") && customerContact.isFclImports()) {
                            if (CommonUtils.isEqualIgnoreCase(customerContact.getEmail(), accountingEmail)
                                    || CommonUtils.isEqualIgnoreCase(customerContact.getFax(), accountingFax)) {
                                customerContact.setAccountingSelected(true);
                            }
                            resultList.add(customerContact);
                        }
                    } else {
                        if (null != code && CommonUtils.in(code, "E", "F") && customerContact.isFclExports()) {
                            if (CommonUtils.isEqualIgnoreCase(customerContact.getEmail(), accountingEmail)
                                    || CommonUtils.isEqualIgnoreCase(customerContact.getFax(), accountingFax)) {
                                customerContact.setAccountingSelected(true);
                            }
                            resultList.add(customerContact);
                        }
                    }
                }
            }
        }
        return resultList;
    }

    public void setRequestObjects(FclBlForm fclBlForm, HttpServletRequest request) throws Exception {
        String fileNumber = fclBlForm.getFclBl().getFileNo();
        List<SedFilings> aesList = new ArrayList<SedFilings>();
        List list = new SedFilingsDAO().findByDr(fclBlForm.getFclBl().getFileNo());
        if (null != list) {
            for (Object object : list) {
                SedFilings sedFilings = (SedFilings) object;
                sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref()))) {
                    sedFilings.setSched(true);
                }
                aesList.add(sedFilings);
            }
        }
        SetRequestForFclChargesandCostCode(request, fclBlForm.getFclBl(), true);
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        NotesDAO notesDAO = new NotesDAO();
        if (CommonFunctions.isNotNull(fileNumber)) {
            String screenName = "FCLFILE";
            request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "", "Scan or Attach"));
            request.setAttribute("ManualNotes", notesDAO.isManualNotes(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
            request.setAttribute("MasterScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL", "Scan or Attach"));
            request.setAttribute("MasterStatus", documentStoreLogDAO.getSsMasterStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL"));
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getShipperNo())) {
                request.setAttribute("isShipperNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getShipperNo()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getConsigneeNo())) {
                request.setAttribute("isConsigneeNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getConsigneeNo()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getNotifyParty())) {
                request.setAttribute("isMNotifyNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getNotifyParty()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getHouseShipperNo())) {
                request.setAttribute("isHShipperNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getHouseShipperNo()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getHouseConsignee())) {
                request.setAttribute("isHConsigneeNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getHouseConsignee()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getHouseNotifyPartyNo())) {
                request.setAttribute("isHNotifyNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getHouseNotifyPartyNo()));
            }
            if (CommonUtils.isNotEmpty(fclBlForm.getFclBl().getForwardagentNo())) {
                request.setAttribute("isFreightNotes", notesDAO.isCustomerNotes(fclBlForm.getFclBl().getForwardagentNo()));
            }
        }
        request.setAttribute("showReverseToBooking", true);
        if (CommonFunctions.isNotNull(new FclBlBC().getFileNoObjectForMultipleBl(fileNumber + FclBlConstants.EQUALDELIMITER))) {
            request.setAttribute("showReverseToBooking", false);
        }
        FclBl bl =
                new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO().findById(fclBlForm.getBol());
        if ("Y".equals(fclBlForm.getFclBl().getHazmat())) {
            List l = new FclBlContainerDAO().findByProperty("bolId", fclBlForm.getFclBl().getBol());
            List containerList = new ArrayList();
            if (null != l) {
                for (Object object : l) {
                    FclBlContainer fclBlContainer = (FclBlContainer) object;
                    List hazmatMaterialList = new HazmatMaterialDAO().findbydoctypeid1("FclBl", fclBlContainer.getTrailerNoId());
                    if (!hazmatMaterialList.isEmpty()) {
                        fclBlContainer.setHazmat(true);
                    }
                    containerList.add(fclBlContainer);
                }
                request.setAttribute("fclBlContainerDtlsList", containerList);
            }
        } else {
            request.setAttribute("fclBlContainerDtlsList", new FclBlContainerDAO().findByProperty("bolId", fclBlForm.getFclBl().getBol()));
        }
        request.setAttribute("correction", new FclBlCorrectionsDAO().getFclBlCorrectionForTheBLNumbertoDisplay(fclBlForm.getFclBl().getBolId()));
        request.setAttribute("fclBlForm", fclBlForm);
        request.setAttribute("fclBl", fclBlForm.getFclBl());
        request.setAttribute("bl", bl);
        request.setAttribute("sedFilingsList", aesList);
    }

    public void SetRequestForFclChargesandCostCode(HttpServletRequest request, FclBlNew fclBl, boolean hasMasterBlChanged) throws Exception {
        List fclCostCodeList = new ArrayList();
        List fclChargesList = new ArrayList();
        FclBlBC fclBlBC = new FclBlBC();
        fclChargesList = fclBlChargesDAO.findByProperty("bolId", fclBl.getBol());
        Collections.sort(fclChargesList, new Comparator());
        fclCostCodeList = fclBlCostCodesDAO.findByProperty("bolId", fclBl.getBol());
        Collections.sort(fclCostCodeList, new Comparator());
        int j = 0;
        int k = 0;
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        for (Iterator iterator = fclChargesList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && charges.getChargeCode().equals("OCNFRT")) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        request.setAttribute("addchargeslist", newList);
        List consolidatorList = consolidateRates(newList, importFlag);
        request.setAttribute("consolidatorList", consolidatorList);
        linkedList = new LinkedList();
        newList = new ArrayList();
        List manifestedCostList = new ArrayList();
        for (Iterator iterator = fclCostCodeList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (!"yes".equalsIgnoreCase(fclBlCostCodes.getDeleteFlag())) {
                if (fclBlCostCodes.getCostCode() != null && fclBlCostCodes.getCostCode().equals("OCNFRT")) {
                    linkedList.add(0, fclBlCostCodes);
                } else {
                    linkedList.add(fclBlCostCodes);
                }
                manifestedCostList.add(fclBlCostCodes);
            }
        }
        newList.addAll(linkedList);
        request.setAttribute(FclBlConstants.FCLBL_COSTS_LIST, newList);
        //------COLLAPSE OR SUMMARY COST LIST...
        List consolidatorCostList = consolidateRatesForCosts(newList, fclBl, hasMasterBlChanged);

        request.setAttribute("consolidatorCostList", consolidatorCostList);
        calculateCostChargesAndProfit(fclBl, request);
        if (null != fclBl.getBol() && CommonFunctions.isNotNull(fclCostCodeList) && CommonFunctions.isNotNull(manifestedCostList)) {
            List newManifestList = new ArrayList();
            //---getting summary or collapsed manifested list---
            manifestedCostList = fclBlBC.shortManifestedCostCodeList(manifestedCostList);
            newManifestList = consolidateRatesForCosts(manifestedCostList, fclBl, hasMasterBlChanged);
            request.setAttribute("ManifestedCostList", newManifestList);
        }
    }

    public List consolidateRatesForCosts(List fclRates, FclBlNew fclBl, boolean hasMasterBlChanged) throws Exception {
        Map hashMap = new HashMap();
        List list = new ArrayList();
        List newList = new ArrayList();
        List finalList = new ArrayList();
        String interModelRate = "";
        String interModelRate1 = "";
        String consolidator = "";
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            list.add(fclBlCostCodes);
        }

        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (importFlag == false) {
                consolidator = LoadApplicationProperties.getProperty("OceanFreight");
            } else if (importFlag == true) {
                consolidator = LoadApplicationProperties.getProperty("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.contains(",")) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = LoadApplicationProperties.getProperty("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.contains(",")) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCostCodes.getCostCode().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCostCodes.getCostCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightcharge"))) {
                        try {
                            FclBlCostCodes costcodes = new FclBlCostCodes();
                            PropertyUtils.copyProperties(costcodes, fclBlCostCodes);
                            newList.add(costcodes);
                            flag = true;
                        } catch (IllegalAccessException ex) {
                            log.info("consolidateRatesForCosts failed on " + new Date(), ex);
                        } catch (InvocationTargetException ex) {
                            log.info("consolidateRatesForCosts failed on " + new Date(), ex);
                        }
                    } else {
                        newList.add(fclBlCostCodes);
                        flag = true;
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if ((fclBlCostCodes.getCostCode().equals("DRAY")
                            || fclBlCostCodes.getCostCode().equals("INSURE")
                            || fclBlCostCodes.getCostCode().equals("PIERPA")
                            || fclBlCostCodes.getCostCode().equals("CHASFEE")
                            || fclBlCostCodes.getCostCode().equals("005")
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODE)
                            || fclBlCostCodes.getCostCode().equals(FclBlConstants.FFCODETWO)
                            || fclBlCostCodes.getCostCode().equals("DEFER")
                            || fclBlCostCodes.getCostCode().startsWith("FAE")
                            || (fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new"))
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals("")))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewmasterbL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost()) && hasMasterBlChanged) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewmasterbL());
                            TransactionLedger tarLedger = (TransactionLedger) new TransactionLedgerDAO().findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
                            if (tarLedger != null && tarLedger.getStatus() != null && tarLedger.getStatus().equalsIgnoreCase("open")) {
                                tarLedger.setInvoiceNumber(fclBl.getNewmasterbL());
                            }
                        }
                        newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightcharge"))
                                  ||  tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                } else {
                    if ((fclBlCostCodes.getBookingFlag() != null && fclBlCostCodes.getBookingFlag().equals("new"))
                            || (fclBlCostCodes.getReadOnlyFlag() == null || fclBlCostCodes.getReadOnlyFlag().equals(""))) {
                        if ("DEFER".equals(fclBlCostCodes.getCostCode())
                                && CommonUtils.isNotEmpty(fclBl.getNewmasterbL()) && "M".equalsIgnoreCase(fclBl.getReadyToPost()) && hasMasterBlChanged) {
                            fclBlCostCodes.setInvoiceNumber(fclBl.getNewmasterbL());
                            TransactionLedger tarLedger = (TransactionLedger) new TransactionLedgerDAO().findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
                            if (tarLedger != null && tarLedger.getStatus() != null && tarLedger.getStatus().equalsIgnoreCase("open")) {
                                tarLedger.setInvoiceNumber(fclBl.getNewmasterbL());
                            }
                        }
                        newList.add(fclBlCostCodes);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCostCodes tempFclBlCostCodes = (FclBlCostCodes) itr.next();
                            if ((tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightImpcharge"))
                                   || tempFclBlCostCodes.getCostCode().equals(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && null != tempFclBlCostCodes.getReadOnlyFlag() && tempFclBlCostCodes.getReadOnlyFlag().equals("on")) {
                                tempFclBlCostCodes.setAmount(tempFclBlCostCodes.getAmount() + fclBlCostCodes.getAmount());
                                break;
                            }
                        }
                    }
                }
            }
        }

        int k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCostCodes costCodes = (FclBlCostCodes) iter.next();
            if (costCodes.getCostCode() != null && (costCodes.getCostCode().equals("OCNFRT") || costCodes.getCostCode().equals("OFIMP"))) {
                linkedList.add(k, costCodes);
                k++;
            } else {
                linkedList.add(costCodes);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
    }

    public List consolidateRates(List fclRates, boolean importFlag) throws Exception {
        List newList = new ArrayList();
        List finalList = new ArrayList();
        LinkedList sortedList = new LinkedList();
        int k = 0;
        String consolidator = "";
        boolean noAutoOfr = true;
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            if (fclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightcharge"))
                    && CommonUtils.isNotEmpty(fclBlCharges.getReadOnlyFlag())) {
                noAutoOfr = false;
            }
            if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("OCNFRT")) {
                sortedList.add(k, fclBlCharges);
                k++;
            } else {
                sortedList.add(fclBlCharges);
            }
        }

        for (Iterator iterator = sortedList.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            if (importFlag == false) {
                consolidator = LoadApplicationProperties.getProperty("OceanFreight");
            } else {
                consolidator = LoadApplicationProperties.getProperty("OceanFreightImports");
            }
            String colsolidatorRates[] = new String[5];
            if (consolidator.contains(",")) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = LoadApplicationProperties.getProperty("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.contains(",")) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightcharge"))) {
                        try {
                            FclBlCharges charges = new FclBlCharges();
                            PropertyUtils.copyProperties(charges, fclBlCharges);
                            newList.add(charges);
                            flag = true;
                        } catch (IllegalAccessException ex) {
                            log.info("consolidateRates failed on " + new Date(), ex);
                        } catch (InvocationTargetException ex) {
                            log.info("consolidateRates failed on " + new Date(), ex);
                        }
                    } else {
                        interModelFlag = false;
                        for (int j = 0; j < interModelRates.length; j++) {
                            if (fclBlCharges.getReadOnlyFlag() != null
                                    && fclBlCharges.getChargeCode().equalsIgnoreCase(interModelRates[j])) {
                                interModelFlag = true;
                                break;
                            }
                        }
                        if (interModelFlag) {
                            newList.add(fclBlCharges);
                        }
                        if (!interModelFlag) {
                            newList.add(fclBlCharges);
                            flag = true;
                        }
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (importFlag == false) {
                    if (null != fclBlCharges.getChargeCode() && (fclBlCharges.getChargeCode().equals("DRAY")
                            || fclBlCharges.getChargeCode().equals("INSURE")
                            || fclBlCharges.getChargeCode().equals("PIERPA")
                            || fclBlCharges.getChargeCode().equals("CHASFEE")
                            || (fclBlCharges.getBookingFlag() != null
                            && fclBlCharges.getBookingFlag().equals("new")
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODE)
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETWO)
                            && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETHREE))
                            || (fclBlCharges.getReadOnlyFlag() == null
                            || fclBlCharges.getReadOnlyFlag().equals("")))) {
                        newList.add(fclBlCharges);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                            if ((tempFclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightcharge")) 
                                    || tempFclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag()) || noAutoOfr)) {
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                break;
                            }
                        }
                    }
                } else {
                    if (null != fclBlCharges.getChargeCode() && fclBlCharges.getBookingFlag() != null && fclBlCharges.getBookingFlag().equals("new") || (fclBlCharges.getReadOnlyFlag() == null || fclBlCharges.getReadOnlyFlag().equals("")) || (fclBlCharges.getChargeCode().equals("INSURE"))) {
                          newList.add(fclBlCharges);
                    } else {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                            if ((tempFclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightImpcharge"))
                                    || tempFclBlCharges.getChargeCode().equalsIgnoreCase(LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                                    && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag()) || noAutoOfr)) {
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                break;
                            }
                        }
                    }
                }
            }
        }
        k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCharges chargeCode = (FclBlCharges) iter.next();
            if (chargeCode.getChargeCode() != null && (chargeCode.getChargeCode().equals("OCNFRT") || chargeCode.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, chargeCode);
                k++;
            } else {
                linkedList.add(chargeCode);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
    }

    public void setFileList(HttpSession session, Quotation quotation) throws Exception {
        if (session.getAttribute("SearchListByfileNumber") != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking =
                        (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && quotation != null && quotation.getFileNo() != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(quotation.getFileNo().toString())) {
                    fileNumberForQuotaionBLBooking.setQuotId(quotation.getQuoteId());
                    FclBl fclBl = new FclBlDAO().getFileNoObject(quotation.getFileNo().toString());
                    if (fclBl != null) {
                        fileNumberForQuotaionBLBooking.setFclBlId(fclBl.getBol());
                    }
                    fileNumberForQuotaionBLBooking.setFclBlId(null);
                    fileNumberForQuotaionBLBooking.setDisplayColor("RED");
                    getFileList.set(i, fileNumberForQuotaionBLBooking);
                    break;
                } else {
                    fileNumberForQuotaionBLBooking.setDisplayColor(null);
                }
            }
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }

    public void setFileList(HttpSession session, BookingFcl bookingFcl) throws Exception {
        BookingFclBC bookingFclBC = new BookingFclBC();
        if (session.getAttribute("SearchListByfileNumber") != null) {
            //QuotationDAO  quotationDAO = new QuotationDAO();
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking =
                        (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && bookingFcl != null && bookingFcl.getFileNo() != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(bookingFcl.getFileNo().toString())) {
                    //--updating the session along with all the status -------
                    bookingFclBC.updateBookingInSession(fileNumberForQuotaionBLBooking, bookingFcl);
                    fileNumberForQuotaionBLBooking.setFclBlId(null);
                    getFileList.set(i, fileNumberForQuotaionBLBooking);
                    break;
                } else {
                    fileNumberForQuotaionBLBooking.setDisplayColor(null);
                }
            }
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);

        }
    }

    public void setFileList(HttpSession session, FclBl saveFclBl) throws Exception {
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        FclBlBC fclBlBC = new FclBlBC();
        StringFormatter stringFormatter = new StringFormatter();
        boolean falg = true;
        String status = "";
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        if (session.getAttribute("SearchListByfileNumber") != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking =
                        (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && saveFclBl != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(saveFclBl.getFileNo())) {
                    falg = false;
                    fileNumberForQuotaionBLBooking.setDisplayColor("RED");
                    if (null != fileNumberForQuotaionBLBooking.getFclBlStatus()) {
                        status = fileNumberForQuotaionBLBooking.getFclBlStatus().replaceAll("null", "");
                    }
                    if (fileNumberForQuotaionBLBooking.getFclBlId() != null) {
                        if (saveFclBl != null) {

                            //-----COMPARING ALL THE STATUS HERE------------
                            status = stringFormatter.compareStatus(status, saveFclBl);
                            //--ends.
                            QuotationBC quotationBC = new QuotationBC();
                            if (saveFclBl.getFclcontainer() != null) {
                                Iterator iter = saveFclBl.getFclcontainer().iterator();
                                while (iter.hasNext()) {
                                    FclBlContainer fclBlConatiner = (FclBlContainer) iter.next();
                                    List hazmatList = quotationBC.getHazmatList("FclBl", fclBlConatiner.getTrailerNoId().toString());
                                    if (hazmatList.size() > 0) {
                                        fileNumberForQuotaionBLBooking.setHazmat("H");
                                        break;
                                    }
                                }
                            }
                            if (saveFclBl.getBlVoid() != null) {
                                fileNumberForQuotaionBLBooking.setBlvoid("Y");
                            }
                            if (CommonUtils.isNotEmpty(saveFclBl.getReadyToPost())) {
                                fileNumberForQuotaionBLBooking.setManifest("M");
                            } else {
                                fileNumberForQuotaionBLBooking.setManifest("");
                            }
                            //---make fileicon appear red color if corrections are present---
                            if (saveFclBl.getBolId() != null) {
                                FclBlCorrectionsDAO fbcdao = new FclBlCorrectionsDAO();
                                if (null != fbcdao.getLatestUnPostedNotice(saveFclBl.getBolId(), null)) {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent("Corrections Exist");
                                } else {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent(null);
                                }
                            }
                        }
                        fileNumberForQuotaionBLBooking.setFclBlStatus(CommonFunctions.isNotNull(status) ? status.replaceAll(",,", ",") : "");
                        fileNumberForQuotaionBLBooking.setDoorOrigin(saveFclBl.getDoorOfOrigin());
                        fileNumberForQuotaionBLBooking.setBlClosed(saveFclBl.getBlClosed());
                        fileNumberForQuotaionBLBooking.setBlAudit(saveFclBl.getBlAudit());
                        Integer aesStatus = logFileEdiDAO.getSedCount(saveFclBl.getFileNo());
                        String masterstatus = logFileEdiDAO.findMasterStatusFileNo(saveFclBl.getFileNo());
                        String dockReceipt = "04" + saveFclBl.getFileNo();
                        String _304Succcess = logFileEdiDAO.findDrNumberStatus(dockReceipt, "success");
                        String _304Failure = logFileEdiDAO.findDrNumberStatus(dockReceipt, "failure");
                        Integer _997Succcess = logFileEdiDAO.find997Status(saveFclBl.getFileNo());
                        fileNumberForQuotaionBLBooking.setAesCount(aesStatus);
                        fileNumberForQuotaionBLBooking.setDocumentStatus(masterstatus);
                        fileNumberForQuotaionBLBooking.set304Success(_304Succcess);
                        fileNumberForQuotaionBLBooking.set304Failure(_304Failure);
                        fileNumberForQuotaionBLBooking.set997Success(null != _997Succcess ? "" + _997Succcess : "");
                    }
                    getFileList.set(i, fileNumberForQuotaionBLBooking);
                    break;
                }
            }
            if (falg) {
                BookingFclDAO bookingFclDAO = new BookingFclDAO();
                QuotationDAO quotationDAO = new QuotationDAO();
                BookingFclBC bookingFclBC = new BookingFclBC();
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking(null, null, saveFclBl);
                BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
                Quotation quotation = quotationDAO.getFileNoObject(fclBlBC.getFileNumber(saveFclBl.getFileNo()));
                if (bookingFcl != null) {
                    fileNumberForQuotaionBLBooking.setBookingId(bookingFcl.getBookingId());
                    fileNumberForQuotaionBLBooking.setSsBkgNo(bookingFcl.getSSBookingNo());
                    fileNumberForQuotaionBLBooking.setBookedBy(bookingFcl.getBookedBy());
                    status = bookingFclBC.setStatus(bookingFcl);
                }
                if (quotation != null) {
                    fileNumberForQuotaionBLBooking.setQuotId(quotation.getQuoteId());
                    fileNumberForQuotaionBLBooking.setClient(quotation.getClientname());
                }
                QuotationBC quotationBC = new QuotationBC();
                if (saveFclBl.getFclcontainer() != null) {
                    Iterator iter = saveFclBl.getFclcontainer().iterator();
                    while (iter.hasNext()) {
                        FclBlContainer fclBlConatiner = (FclBlContainer) iter.next();
                        List hazmatList = quotationBC.getHazmatList("FclBl", fclBlConatiner.getTrailerNoId().toString());
                        if (hazmatList.size() > 0) {
                            fileNumberForQuotaionBLBooking.setHazmat("H");
                            break;
                        }
                    }
                }
                if (saveFclBl.getBlVoid() != null) {
                    fileNumberForQuotaionBLBooking.setBlvoid("Y");
                }
                if (saveFclBl.getReadyToPost() != null) {
                    fileNumberForQuotaionBLBooking.setManifest("M");
                } else {
                    fileNumberForQuotaionBLBooking.setManifest("");
                }
                //---make fileicon appear red color if corrections are present---
                if (saveFclBl.getBolId() != null) {
                    FclBlCorrectionsDAO fbcdao = new FclBlCorrectionsDAO();
                    if (null != fbcdao.getLatestUnPostedNotice(saveFclBl.getBolId(), null)) {
                        fileNumberForQuotaionBLBooking.setCorrectionsPresent("Corrections Exist");
                    } else {
                        fileNumberForQuotaionBLBooking.setCorrectionsPresent(null);
                    }
                }
                status = stringFormatter.compareStatus(status, saveFclBl);
                fileNumberForQuotaionBLBooking.setFclBlStatus(status);
                fileNumberForQuotaionBLBooking.setDisplayColor("RED");

                fileNumberForQuotaionBLBooking.setUser(saveFclBl.getBlBy());
                fileNumberForQuotaionBLBooking.setDoorOrigin(saveFclBl.getDoorOfOrigin());
                String importRelease = "";
                if (CommonFunctions.isNotNull(saveFclBl.getPaymentRelease())) {
                    importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + saveFclBl.getPaymentRelease() : "N" + saveFclBl.getPaymentRelease().toString();
                } else {
                    importRelease = (CommonFunctions.isNotNull(saveFclBl.getImportRelease())) ? saveFclBl.getImportRelease() + "N" : "NN";
                }
                fileNumberForQuotaionBLBooking.setImportRelease(importRelease);
                fileNumberForQuotaionBLBooking.setBlClosed(saveFclBl.getBlClosed());
                fileNumberForQuotaionBLBooking.setBlAudit(saveFclBl.getBlAudit());
                getFileList.add(fileNumberForQuotaionBLBooking);
            }
            Collections.sort(getFileList, new FileNumberForQuotaionBLBooking());
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }

    public void deleteCharges(String chargeId, Integer bol, String userName,MessageResources messageResources, HttpServletRequest request) throws Exception {
        String charges = new FclBlBC().deleteChargesDetails(chargeId, userName,messageResources,request);
        if (CommonUtils.isNotEmpty(charges)) {
            FclBl fclBl = new FclBlDAO().findById(bol);
            // delete advff and advsho from costcode and update pbasurcharge
            new FclBlBC().deletecodeCodes(fclBl, charges);
            new FclBlDAO().lockFclBlTemp(fclBl);
        }
    }

    public void postAccrualsBeforeManifest(String bolId, User user) throws Exception {
        FclBl bl = new FclBlDAO().findById(bolId);
        new FclManifestDAO(bl, user).postAccrualsBeforeManifest();
    }

    public void FaeCalculation(Integer bol, HttpServletRequest request) throws Exception {
        if (bol != null) {
            FclBl fclBl = new FclBlDAO().findById(bol);
            if (null != fclBl && fclBl.getFinalDestination() != null) {
                StringFormatter stringFormatter = new StringFormatter();
                String cityName = stringFormatter.getBreketValue(fclBl.getFinalDestination());
                List<Ports> portList = new PortsDAO().findPortUsingUnlocaCode(cityName);
                if (portList != null && !portList.isEmpty()) {
                    Ports ports = portList.get(0);
                    List<FCLPortConfiguration> postConfigSetList = (ports.getFclPortConfigSet() != null) ? new ArrayList(ports.getFclPortConfigSet()) : Collections.EMPTY_LIST;
                    for (FCLPortConfiguration fclPortConfiguration : postConfigSetList) {
                        calculateFAE(fclBl, request, fclPortConfiguration);
                        break;
                    }
                }
                request.setAttribute("hasTransactionType", new FclBlCostCodesDAO().hasTransactionType(fclBl.getBol().toString()));
            }
        }
    }

    public void calculateFAE(FclBl fclBl, HttpServletRequest request, FCLPortConfiguration fCLPortConfiguration) throws Exception {
        if (CommonUtils.notMatches(fclBl.getFileNo(), "(\\d+)-([a-zA-Z])")) {
            GenericCode commisionRule = null;
            GenericCode adminRule = null;
            double commisionAmount = 0d;
            double commisionTierAmount = 0d;
            double adminAmount = 0d;
            double adminTierAmount = 0d;
            if ("Yes".equalsIgnoreCase(fclBl.getRoutedAgentCheck())) {
                commisionRule = fCLPortConfiguration.getRcomRule();
                adminRule = fCLPortConfiguration.getRadmRule();
                commisionAmount = null != fCLPortConfiguration.getRcomAm() ? fCLPortConfiguration.getRcomAm() : 0d;
                commisionTierAmount = null != fCLPortConfiguration.getRcomTierAmt() ? fCLPortConfiguration.getRcomTierAmt() : 0d;
                adminAmount = null != fCLPortConfiguration.getRadmAm() ? fCLPortConfiguration.getRadmAm() : 0d;
                adminTierAmount = null != fCLPortConfiguration.getRadmTierAmt() ? fCLPortConfiguration.getRadmTierAmt() : 0d;
            } else {
                commisionRule = fCLPortConfiguration.getNcomRule();
                adminRule = fCLPortConfiguration.getNadmRule();
                commisionAmount = null != fCLPortConfiguration.getNcomAm() ? fCLPortConfiguration.getNcomAm() : 0d;
                commisionTierAmount = null != fCLPortConfiguration.getNcomTierAmt() ? fCLPortConfiguration.getNcomTierAmt() : 0d;
                adminAmount = null != fCLPortConfiguration.getNadmAm() ? fCLPortConfiguration.getNadmAm() : 0d;
                adminTierAmount = null != fCLPortConfiguration.getNadmTierAmt() ? fCLPortConfiguration.getNadmTierAmt() : 0d;
            }
            double faeAmount = 0d;
            double adminFee = 0d;
            boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
            FclBlDAO fclBlDAO = new FclBlDAO();
            int numberOfContainer = fclBlDAO.getContainerCountForMainAndMultibleBl(fclBl.getFileNo());
            List<FclBl> fclBlList = fclBlDAO.findByFileNo(fclBl.getFileNo());
            if (null != commisionRule) {
                request.setAttribute("faeCalculation", "FAE");
                if (commisionRule.getCodedesc().equalsIgnoreCase(FclBlConstants.CONTAINERRUEL)) {
                    faeAmount = numberOfContainer * commisionAmount;
                } else if (commisionRule.getCodedesc().equalsIgnoreCase(FclBlConstants.PERBLRUEL)) {
                    faeAmount = commisionAmount;
                } else if (commisionRule.getCodedesc().equalsIgnoreCase(FclBlConstants.MULTICONTAINERRUEL)) {
                    if (numberOfContainer > 0) {
                        faeAmount = commisionAmount + ((numberOfContainer - 1) * commisionTierAmount);
                    }
                } else if (commisionRule.getCodedesc().equalsIgnoreCase(FclBlConstants.PERCENTAGERUEL)) {
                    int latestBol = fclBlDAO.getLatestBol(fclBlDAO.getFileNo(fclBl.getBol().toString()));
                    FclBl latestFclBl = fclBlDAO.findById(latestBol);
                    Set fclBlChargesSet = latestFclBl.getFclcharge();
                    Set fclBlCostCodeSet = fclBl.getFclblcostcodes();
                    List chargesList = (fclBlChargesSet != null && !fclBlChargesSet.isEmpty()) ? new ArrayList(
                            fclBlChargesSet) : Collections.EMPTY_LIST;
                    List<FclBlCharges> fclBlChargesList = new ArrayList<FclBlCharges>();
                    Set<FclBlCharges> fclChargesSet = new HashSet<FclBlCharges>();
                    if (!fclBlList.isEmpty()) {
                        for (FclBl fcl : fclBlList) {
                            fclChargesSet.addAll(fcl.getFclcharge());
                        }
                        chargesList.addAll(fclChargesSet);
                    }
                    fclBlChargesList = consolidateRates(new FclBlBC().getSortedList(chargesList), importFlag);
                    List costCodeList = (fclBlCostCodeSet != null && !fclBlCostCodeSet.isEmpty()) ? new ArrayList(fclBlCostCodeSet) : Collections.EMPTY_LIST;
                    FclBlNew fclBlNew = new FclDAO().findById(fclBl.getBol());
                    List fclBlCostCodeList = consolidateRatesForCosts(new FclBlBC().shortManifestedCostCodeList(costCodeList), fclBlNew, false);
                    double chargeForFAE = 0d;
                    double costForFAE = 0d;
                    for (FclBlCharges fclBlCharges : fclBlChargesList) {
                        if ("OCNFRT".equalsIgnoreCase(fclBlCharges.getChargeCode()) || "HAZFEE".equalsIgnoreCase(fclBlCharges.getChargeCode())
                                || "BKRSUR".equalsIgnoreCase(fclBlCharges.getChargeCode()) || "INTRAMP".equalsIgnoreCase(fclBlCharges.getChargeCode())
                                || "INTFS".equalsIgnoreCase(fclBlCharges.getChargeCode())) {
                            chargeForFAE = chargeForFAE + fclBlCharges.getAmount();
                        }
                    }
                    for (Iterator it = fclBlCostCodeList.iterator(); it.hasNext();) {
                        FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) it.next();
                        if(fclBlCostCodes.getDeleteFlag()==null || (null!=fclBlCostCodes.getDeleteFlag() && fclBlCostCodes.getDeleteFlag().equals("no"))){
                            if ("OCNFRT".equalsIgnoreCase(fclBlCostCodes.getCostCode()) || "HAZFEE".equalsIgnoreCase(fclBlCostCodes.getCostCode())
                                    || "DEFER".equalsIgnoreCase(fclBlCostCodes.getCostCode()) || "INTRAMP".equalsIgnoreCase(fclBlCostCodes.getCostCode())
                                    || "INTFS".equalsIgnoreCase(fclBlCostCodes.getCostCode()) || "FFCOMM".equalsIgnoreCase(fclBlCostCodes.getCostCode())
                                    || "TERCOMM".equalsIgnoreCase(fclBlCostCodes.getCostCode()) || "BKRSUR".equalsIgnoreCase(fclBlCostCodes.getCostCode())) {
                                costForFAE = costForFAE + fclBlCostCodes.getAmount();
                            }
                        }
                    }
                    if (null != adminRule) {
                        if (adminRule.getCodedesc().equalsIgnoreCase(FclBlConstants.CONTAINERRUEL)) {
                            adminFee = numberOfContainer * adminAmount;
                        } else if (adminRule.getCodedesc().equalsIgnoreCase(FclBlConstants.PERBLRUEL)) {
                            adminFee = adminAmount;
                        } else if (adminRule.getCodedesc().equalsIgnoreCase(FclBlConstants.MULTICONTAINERRUEL)) {
                            if (numberOfContainer > 0) {
                                adminFee = adminAmount + ((numberOfContainer - 1) * adminTierAmount);
                            }
                        }
                    }
                    faeAmount = (chargeForFAE - costForFAE - adminFee) * commisionAmount;
                }
            } else if ((fclBl.getReadyToPost() == null || (null != fclBl.getReadyToPost() && fclBl.getReadyToPost().equals(""))) && adminRule != null) {
                fclBlChargesDAO.deleteCharges(fclBl.getBol(), FclBlConstants.ADMINFEEWITHNOCOMMISION);
                double incentAmount = 0d;
                request.setAttribute("faeCalculation", "INCENT");
                if (adminRule.getCodedesc().equalsIgnoreCase(FclBlConstants.MULTICONTAINERRUEL)) {
                    if (numberOfContainer > 0) {
                        incentAmount = adminAmount + ((numberOfContainer - 1) * adminTierAmount);
                    }
                } else {
                    incentAmount = adminAmount * numberOfContainer;
                }
                FclBlCharges fclBlCharges = new FclBlCharges();
                fclBlCharges.setBolId(fclBl.getBol());
                fclBlCharges.setChargeCode(FclBlConstants.ADMINFEEWITHNOCOMMISION);
                fclBlCharges.setCharges("INCENTIVE");
                fclBlCharges.setCurrencyCode("USD");
                fclBlCharges.setAmount(incentAmount);
                fclBlCharges.setOldAmount(incentAmount);
                fclBlCharges.setBillTo("Agent");
                fclBlCharges.setBundleIntoOfr("Yes");
                fclBlCharges.setPcollect("collect");
                fclBlCharges.setFaeIncentFlag("Y");
                fclBlCharges.setPrintOnBl("No");
                fclBlChargesDAO.save(fclBlCharges);
                fclBlDAO.update(fclBl);
                new FclBlBC().notesForIncentCharge(fclBl.getBol(), request, "INSERTED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
            }
            if (CommonUtils.isNotEmpty(faeAmount)) {
                createFaeCostRecord(faeAmount, fclBl, request);
            }
        }
    }

    public void createFaeCostRecord(Double faeAmount, FclBl fclBl, HttpServletRequest request) throws Exception {
        List<FclBlCostCodes> faeCosts = fclBlCostCodesDAO.findByPropertyAndBlNumber("costCode", "FAECOMM", fclBl.getBol());
        String fileNoPrefix=CommonConstants.loadMessageResources().getMessage("fileNumberPrefix");
        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
        FclBlCostCodes newFclBlCostCodes = null;
        boolean addAccrual = false;
        if (!faeCosts.isEmpty()) {
            for (FclBlCostCodes faeCost : faeCosts) {
                if (CommonUtils.in(faeCost.getTransactionType(), "AP", "PN", "IP")) {
                    faeAmount -= faeCost.getAmount();
                }
                fclBlCostCodes = faeCost;
            }
            if (CommonUtils.isNotEmpty(faeAmount)
                    && null != fclBlCostCodes.getAmount() && CommonUtils.isNotEqual(faeAmount, fclBlCostCodes.getAmount())) {
                if (!"AP".equalsIgnoreCase(fclBlCostCodes.getTransactionType())
                        && !"PN".equalsIgnoreCase(fclBlCostCodes.getTransactionType()) && !"IP".equalsIgnoreCase(fclBlCostCodes.getTransactionType())) {
                    double oldFaeAmount = fclBlCostCodes.getAmount();
                    fclBlCostCodes.setAmount(faeAmount);
                    fclBlCostCodes.setAccName(fclBl.getAgent());
                    fclBlCostCodes.setInvoiceNumber(fileNoPrefix.concat(fclBl.getFileNo()));
                    fclBlCostCodes.setAccNo(fclBl.getAgentNo());
                    fclBlCostCodes.setReadyToPost("M");
                    fclBlCostCodes.setManifestModifyFlag("yes");
                    User user = (User) request.getSession().getAttribute("loginuser");
                    fclBlCostCodes.setAccrualsUpdatedBy(user.getLoginName());
                    fclBlCostCodes.setAccrualsUpdatedDate(new Date());
                    new FclBlBC().notesFAECalForOldamount(fclBl, faeAmount, oldFaeAmount, fclBlCostCodes, request, "UPDATED");
                    if (CommonUtils.isNotEmpty(fclBlCostCodes.getTransactionType())) {
                        addAccrual = true;
                    }
                    fclBlCostCodesDAO.update(fclBlCostCodes);
                } else {
                    newFclBlCostCodes = new FclBlCostCodes();
                    newFclBlCostCodes.setAmount(faeAmount);
                    newFclBlCostCodes.setAccName(fclBl.getAgent());
                    newFclBlCostCodes.setAccNo(fclBl.getAgentNo());
                    newFclBlCostCodes.setCurrencyCode("USD");
                    newFclBlCostCodes.setInvoiceNumber(fileNoPrefix.concat(fclBl.getFileNo()));
                    newFclBlCostCodes.setCostCode(FclBlConstants.FAECODE);
                    newFclBlCostCodes.setCostCodeDesc(FclBlConstants.FAECODEDESC);
                    newFclBlCostCodes.setReadyToPost("M");
                    newFclBlCostCodes.setManifestModifyFlag("yes");
                    newFclBlCostCodes.setTransactionType("AC");
                    User user = (User) request.getSession().getAttribute("loginuser");
                    newFclBlCostCodes.setAccrualsCreatedBy(user.getLoginName());
                    newFclBlCostCodes.setAccrualsCreatedDate(new Date());
                    addAccrual = true;
                    TradingPartner tradingPartner = new TradingPartnerDAO().findById(fclBl.getRoutedByAgent());
                    if (tradingPartner != null) {
                        newFclBlCostCodes.setAccName(tradingPartner.getAccountName());
                        newFclBlCostCodes.setAccNo(tradingPartner.getAccountno());
                    }
                    newFclBlCostCodes.setBolId(fclBl.getBol());
                    fclBlCostCodesDAO.save(newFclBlCostCodes);
                    new FclBlBC().notesFAECal(fclBl, faeAmount, newFclBlCostCodes, request, "INSERTED");
                }
            }
        } else {
            fclBlCostCodes.setAmount(faeAmount);
            fclBlCostCodes.setAccName(fclBl.getAgent());
            fclBlCostCodes.setAccNo(fclBl.getAgentNo());
            fclBlCostCodes.setCurrencyCode("USD");
            fclBlCostCodes.setInvoiceNumber(fileNoPrefix.concat(fclBl.getFileNo()));
            fclBlCostCodes.setCostCode(FclBlConstants.FAECODE);
            fclBlCostCodes.setCostCodeDesc(FclBlConstants.FAECODEDESC);
            fclBlCostCodes.setReadyToPost("M");
            fclBlCostCodes.setManifestModifyFlag("yes");
            fclBlCostCodes.setTransactionType("AC");
            User user = (User) request.getSession().getAttribute("loginuser");
            fclBlCostCodes.setAccrualsCreatedBy(user.getLoginName());
            fclBlCostCodes.setAccrualsCreatedDate(new Date());
            addAccrual = true;
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(fclBl.getRoutedByAgent());
            if (tradingPartner != null) {
                fclBlCostCodes.setAccName(tradingPartner.getAccountName());
                fclBlCostCodes.setAccNo(tradingPartner.getAccountno());
            }
            fclBlCostCodes.setBolId(fclBl.getBol());
            fclBlCostCodesDAO.save(fclBlCostCodes);
            new FclBlBC().notesFAECal(fclBl, faeAmount, fclBlCostCodes, request, "INSERTED");
        }
        if (addAccrual) {
            TransactionLedger tarLedger = (TransactionLedger) new TransactionLedgerDAO().findByCostId(fclBlCostCodes.getCodeId(), fclBl.getBolId());
            if (tarLedger != null && tarLedger.getStatus() != null) {
                if (tarLedger.getStatus().equalsIgnoreCase("open")) {
                    double transactionAmount = fclBlCostCodes.getAmount();
                    tarLedger.setTransactionAmt(transactionAmount);
                    tarLedger.setBalance(transactionAmount);
                    tarLedger.setBalanceInProcess(transactionAmount);
                } else {
                    List<FclBlCostCodes> list = new ArrayList<FclBlCostCodes>();
                    if (null != newFclBlCostCodes) {
                        list.add(newFclBlCostCodes);
                    } else {
                        list.add(fclBlCostCodes);
                    }
                    new ManifestBC().getTransactionObject(fclBl, new FclBlBC().getFclBlCostBeanobject(null, list, null, null, null), null);
                }
            } else {
                List<FclBlCostCodes> list = new ArrayList<FclBlCostCodes>();
                if (null != newFclBlCostCodes) {
                    list.add(newFclBlCostCodes);
                } else {
                    list.add(fclBlCostCodes);
                }
                new ManifestBC().getTransactionObject(fclBl, new FclBlBC().getFclBlCostBeanobject(null, list, null, null, null), null);
            }
        }
    }
}

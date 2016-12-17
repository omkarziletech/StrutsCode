package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PrintConfig;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.dwr.PrintUtil;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.web.HibernateSessionRequestFilter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BlClauses;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.dao.FclManifestDAO;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.gp.cvst.logisoft.beans.Comparator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

public class FclBlCorrectionsBC {

    private final FclBlCorrectionsDAO correctionsDAO = new FclBlCorrectionsDAO();
    private final GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    private FclBlCorrections fclBlCorrectionsIntance = null;
    private SimpleDateFormat sdf = null;
    private final FclBlDAO blDAO = new FclBlDAO();
    private DBUtil dbUtil = null;
    private TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    private ManifestBC manifestBc = new ManifestBC();
    private final FclBlBC fclBlBC = new FclBlBC();
    private HttpServletRequest request = null;
    private final UserDAO userDAO = new UserDAO();

    /**
     * @param fclBlCorrectionsForm
     * @return getting all corrections object
     */
    public List getAllCorrections(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        List correctionList = null;
        Date date1 = null;
        sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (fclBlCorrectionsForm.getDate() != null && !fclBlCorrectionsForm.getDate().trim().equals("")) {
            date1 = sdf.parse(fclBlCorrectionsForm.getDate());
        } else {
            date1 = null;
        }
        correctionList = correctionsDAO.getAllCorrectionsList(fclBlCorrectionsForm.getCorrectionCode(),
                fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getFileNo(),
                date1, fclBlCorrectionsForm.getShipper(), fclBlCorrectionsForm.getForwarder(), fclBlCorrectionsForm.getThirdParty(),
                fclBlCorrectionsForm.getOrigin(), fclBlCorrectionsForm.getDestination(), fclBlCorrectionsForm.getPol(), fclBlCorrectionsForm.getPod(), fclBlCorrectionsForm.getNoticeNo(), fclBlCorrectionsForm.getApprovedBy(),
                fclBlCorrectionsForm.getCreatedBy(), fclBlCorrectionsForm.getFilerBy(), fclBlCorrectionsForm.getFileType());
        return correctionList;
    }

    /**
     * @param fclBlCorrectionsForm
     * @param loginName
     * @param request when we pressed on save button from fclblcorrection parent
     * page this function wil get called.....
     */
    public void saveFclBlCorrectionDetails(FclBlCorrectionsForm fclBlCorrectionsForm, String loginName,
            HttpServletRequest request) throws Exception {
        Integer noticeNumber = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        GenericCode genericCodeObject = null;
        if (fclBlCorrectionsForm.getCorrectionType() != null && !fclBlCorrectionsForm.getCorrectionType().equals("")) {
            genericCodeObject = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType()));
        }
        String code = (null != genericCodeObject ? genericCodeObject.getCode() : "");
        FclBlCorrections fclBlCorrectionsSave = new FclBlCorrections(fclBlCorrectionsForm);
        FclBl fclBl = blDAO.findById(fclBlCorrectionsForm.getBlNumber());
        if (CommonFunctions.isNotNull(code) && !code.equalsIgnoreCase("A") && !code.equalsIgnoreCase("Y")) {
            fclBlCorrectionsSave.setDebitMemoEmail((null != fclBlCorrectionsForm.getDebitMemoEmail()
                    && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getDebitMemoEmail()))
                    ? fclBlCorrectionsForm.getDebitMemoEmail() : "");
            fclBlCorrectionsSave.setCreditMemoEmail((null != fclBlCorrectionsForm.getCreditMemoEmail()
                    && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getCreditMemoEmail()))
                    ? fclBlCorrectionsForm.getCreditMemoEmail() : "");
            if (code.equalsIgnoreCase("F") || code.equalsIgnoreCase("H") || code.equalsIgnoreCase("D")) {
                fclBlCorrectionsSave.setAccountName(fclBl.getForwardingAgentName());
                fclBlCorrectionsSave.setAccountNumber(fclBl.getForwardAgentNo());
            } else if (code.equalsIgnoreCase("G") || code.equalsIgnoreCase("I") || code.equalsIgnoreCase("C")) {//shipper
                if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                    fclBlCorrectionsSave.setAccountName(fclBl.getHouseShipperName());
                    fclBlCorrectionsSave.setAccountNumber(fclBl.getHouseShipper());
                } else {
                    fclBlCorrectionsSave.setAccountName(fclBl.getShipperName());
                    fclBlCorrectionsSave.setAccountNumber(fclBl.getShipperNo());
                }

            } else if (code.equalsIgnoreCase("B")) {
                if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                    fclBlCorrectionsSave.setAccountName(fclBl.getConsigneeName());
                    fclBlCorrectionsSave.setAccountNumber(fclBl.getConsigneeNo());
                } else {
                    fclBlCorrectionsSave.setAccountName(fclBl.getAgent());
                    fclBlCorrectionsSave.setAccountNumber(fclBl.getAgentNo());
                }
            } else {
                if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getAccountName())) {
                    fclBlCorrectionsSave.setAccountName(fclBlCorrectionsForm.getAccountName());
                }
                if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getAccountNumber())) {
                    fclBlCorrectionsSave.setAccountNumber(fclBlCorrectionsForm.getAccountNumber());
                }
                fclBlCorrectionsSave.setDebitMemoEmail((null != fclBlCorrectionsForm.getDebitMemoEmail()
                        && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getDebitMemoEmail()))
                        ? fclBlCorrectionsForm.getDebitMemoEmail() : "");
                fclBlCorrectionsSave.setCreditMemoEmail((null != fclBlCorrectionsForm.getCreditMemoEmail()
                        && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getCreditMemoEmail()))
                        ? fclBlCorrectionsForm.getCreditMemoEmail() : "");
            }
        } else if (CommonFunctions.isNotNull(code) && code.equalsIgnoreCase("A") || code.equalsIgnoreCase("Y")) {
            String profits = correctionsDAO.correctionProfits(fclBlCorrectionsForm.getFileNo(), fclBlCorrectionsForm.getNoticeNo());
            String currentProfit = profits.substring(0, profits.indexOf(","));
            String profitAftrCn = profits.substring(profits.indexOf(",") + 1);
            double current = Double.parseDouble(currentProfit);
            double profitAfrCn = Double.parseDouble(profitAftrCn);
            correctionsDAO.updateCorrectionProfits(current, profitAfrCn, fclBlCorrectionsForm.getIndex1());
            fclBlCorrectionsSave.setCurrentProfit(current);
            fclBlCorrectionsSave.setProfitAfterCn(profitAfrCn);
            fclBlCorrectionsForm.setCurrentProfit(currentProfit);
            fclBlCorrectionsForm.setProfitAfterCn(profitAftrCn);
        }
        fclBlCorrectionsSave.setCorrectionType(genericCodeObject);
        if (fclBlCorrectionsForm.getCorrectionCode() != null && !fclBlCorrectionsForm.getCorrectionCode().equals("")) {
            genericCodeObject = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionCode()));
            fclBlCorrectionsSave.setCorrectionCode(genericCodeObject);
        }
        //--to set Notice number-----
        if (fclBlCorrectionsForm.getNoticeNo() == null || fclBlCorrectionsForm.getNoticeNo().equals("")) {
            noticeNumber = correctionsDAO.getNoticeNumber(fclBlCorrectionsForm.getBlNumber());
            if (null == noticeNumber) {
                noticeNumber = 1;
            } else {
                noticeNumber = noticeNumber + 1;
            }
            fclBlCorrectionsSave.setNoticeNo(noticeNumber.toString());
            fclBlCorrectionsSave.setUserName(loginName);
            fclBlCorrectionsSave.setEmail(user.getEmail());
            fclBlCorrectionsSave.setFax(user.getFax());
            fclBlCorrectionsSave.setPrepaidCollect(fclBlCorrectionsForm.getHouseBl());
            String quoteId = new QuotationDAO().findQuoteId(fclBl.getFileNo());
            fclBlCorrectionsSave.setQuoteId(CommonUtils.isNotEmpty(quoteId) ? Integer.parseInt(quoteId) : 0);
            String bookingId = new BookingFclDAO().getBookingId(fclBl.getFileNo());
            fclBlCorrectionsSave.setBookingId(CommonUtils.isNotEmpty(bookingId) ? Integer.parseInt(bookingId) : 0);
            fclBlCorrectionsSave.setBolId(fclBl.getBol());
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                fclBlCorrectionsSave.setFileType("I");
            } else {
                fclBlCorrectionsSave.setFileType("E");
            }
            correctionsDAO.save(fclBlCorrectionsSave);
        } else {
            List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo());
            if (CommonFunctions.isNotNullOrNotEmpty(fclBlCorrectionsList)) {
                for (FclBlCorrections fclBlCorrections : fclBlCorrectionsList) {
                    setFromValueToFclBlCorrectionsDomain(fclBlCorrections, fclBlCorrectionsForm, request);
                    if (fclBlCorrections.getCorrectionType() != null && !fclBlCorrections.getCorrectionType().getCode().equals("A")
                            && !fclBlCorrections.getCorrectionType().getCode().equals("Y")) {
                        fclBlCorrections.setNewAmount(null);
                        fclBlCorrections.setDiffereceAmount(null);
                    }
                }
            }
            if (fclBlCorrectionsSave.getCorrectionType() != null && !fclBlCorrectionsSave.getCorrectionType().getCode().equalsIgnoreCase("A") && !fclBlCorrectionsSave.getCorrectionType().getCode().equalsIgnoreCase("Y")) {
                List<FclBlCorrections> fclBlCorrectionsDeleteList = correctionsDAO.getFclBlCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo());
                if (CommonFunctions.isNotNullOrNotEmpty(fclBlCorrectionsList)) {
                    for (FclBlCorrections fbc : fclBlCorrectionsDeleteList) {
                        if (fbc.getCorrectionType() != null && fbc.getCorrectionType().getCode().equals("A") && fbc.getCorrectionType().getCode().equals("Y")) {
                            correctionsDAO.delete(fbc);
                        }
                    }
                }
            }
            fclBlCorrectionsSave.setNoticeNo(fclBlCorrectionsForm.getNoticeNo());
        }
        fclBlCorrectionsForm.setNoticeNo(fclBlCorrectionsSave.getNoticeNo());
        request.setAttribute("NoticeNumber", fclBlCorrectionsSave.getNoticeNo());
    }

    /**
     * @param fclBlCorrectionsForm
     * @param loginName
     *
     * @param request when we clicked from div popup this save function wil get
     * excute..........
     */
    // this save is called for Type a Correction
    public void saveCorrections(FclBlCorrectionsForm fclBlCorrectionsForm, String loginName, HttpServletRequest request, MessageResources messageResources) throws Exception {
        String noticeNumber = null;
        dbUtil = new DBUtil();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        FclBlCorrections fclBlCorrections = new FclBlCorrections(fclBlCorrectionsForm);
        FclBl fclBl = blDAO.findById(fclBlCorrectionsForm.getBlNumber());
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getCorrectionType())) {
            fclBlCorrections.setCorrectionType(genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType())));
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getCorrectionCode())) {
            fclBlCorrections.setCorrectionCode(genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionCode())));
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getNoticeNo())) {
            fclBlCorrections.setNoticeNo(fclBlCorrectionsForm.getNoticeNo());
        } else {
            int notice = getNoticeNumber(fclBlCorrectionsForm.getBlNumber());
            fclBlCorrections.setNoticeNo(Integer.toString(notice));
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getId())) {
            fclBlCorrections.setId(new Integer(fclBlCorrectionsForm.getId()));
        }
        fclBlCorrections.setUserName(loginName);
        fclBlCorrections.setEmail(user.getEmail());
        fclBlCorrections.setFax(user.getFax());
        fclBlCorrections.setAmount(Double.parseDouble(dbUtil.removeComma(fclBlCorrectionsForm.getAmount())));
        fclBlCorrections.setNewAmount(Double.parseDouble(dbUtil.removeComma(fclBlCorrectionsForm.getNewAmount())));
        fclBlCorrections.setDiffereceAmount(Double.parseDouble(dbUtil.removeComma(fclBlCorrectionsForm.getDifferenAmount())));
        fclBlCorrections.setBillToParty(fclBlCorrectionsForm.getBillTo());
        fclBlCorrections.setOriginalBillToPartyCorrectionTypeY(fclBlCorrectionsForm.getOriginalBillToPartyCorrectionTypeY());
        if (CommonUtils.isNotEmpty(fclBlCorrections.getDiffereceAmount())) {
            if (fclBlCorrections.getDiffereceAmount() > 0d) {
                fclBlCorrections.setDebitMemoEmail(fclBlCorrectionsForm.getDebitMemoEmail());
            } else {
                fclBlCorrections.setCreditMemoEmail(fclBlCorrectionsForm.getCreditMemoEmail());
            }
        }
        if (fclBlCorrectionsForm.getChargeCode() != null) {
            fclBlCorrections.setChargeCode(fclBlCorrectionsForm.getChargeCode().trim());
        }
        if (fclBlCorrectionsForm.getChargeDescription() != null) {
            fclBlCorrections.setChargeCodeDescription(fclBlCorrectionsForm.getChargeDescription().trim());
        }
        String quoteId = new QuotationDAO().findQuoteId(fclBlCorrectionsForm.getFileNo());
        fclBlCorrections.setQuoteId(CommonUtils.isNotEmpty(quoteId) ? Integer.parseInt(quoteId) : 0);
        String bookingId = new BookingFclDAO().getBookingId(fclBlCorrectionsForm.getFileNo());
        fclBlCorrections.setBookingId(CommonUtils.isNotEmpty(bookingId) ? Integer.parseInt(bookingId) : 0);
        String bolId = blDAO.getBol(fclBlCorrectionsForm.getFileNo());
        fclBlCorrections.setBolId(Integer.parseInt(bolId));
        if (CommonFunctions.isNotNull(fclBlCorrections.getCorrectionType().getCode()) && fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("A") || fclBlCorrections.getCorrectionType().getCode().equalsIgnoreCase("Y")) {
            String profits = correctionsDAO.correctionProfits(fclBlCorrectionsForm.getFileNo(), fclBlCorrectionsForm.getNoticeNo());
            String currentProfit = profits.substring(0, profits.indexOf(","));
            String profitAftrCn = profits.substring(profits.indexOf(",") + 1);
            double current = Double.parseDouble(currentProfit);
            double profitAfrCn = Double.parseDouble(profitAftrCn);
            correctionsDAO.updateCorrectionProfits(current, profitAfrCn, fclBlCorrectionsForm.getIndex1());
            fclBlCorrections.setCurrentProfit(current);
            fclBlCorrections.setProfitAfterCn(profitAfrCn);
            fclBlCorrectionsForm.setCurrentProfit(currentProfit);
            fclBlCorrectionsForm.setProfitAfterCn(profitAftrCn);
        }
        fclBlCorrections.setFclBlChargeId(null != fclBlCorrectionsForm.getChargeId() && !"".equals(fclBlCorrectionsForm.getChargeId()) ? Integer.parseInt(fclBlCorrectionsForm.getChargeId()) : null);
        accountNumberAndNameFortypeA(fclBlCorrections, fclBlCorrectionsForm);
        if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
            fclBlCorrections.setFileType("I");
        } else {
            fclBlCorrections.setFileType("E");
        }
        correctionsDAO.save(fclBlCorrections);
        fclBlCorrectionsForm.setNoticeNo(fclBlCorrections.getNoticeNo());
        fclBlCorrectionsForm.setBlNumber(fclBlCorrections.getBlNumber());
        if (FclBlConstants.ADVANCEFFCODE.equalsIgnoreCase(fclBlCorrections.getChargeCode())
                || FclBlConstants.ADVANCESHIPPERCODE.equalsIgnoreCase(fclBlCorrections.getChargeCode())) {
            updatePBaSurCahrges(fclBlCorrectionsForm, fclBlCorrections.getBlNumber(), fclBlCorrections.getNoticeNo(), fclBlCorrections);
        }
        editCorrectionRecord(fclBlCorrectionsForm, request, messageResources);
        // deleting correction other then type A....
        List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo());
        if (CommonFunctions.isNotNullOrNotEmpty(fclBlCorrectionsList)) {
            for (FclBlCorrections fbc : fclBlCorrectionsList) {
                if (fbc.getCorrectionType() != null && !fbc.getCorrectionType().getCode().equals("A") && !fbc.getCorrectionType().getCode().equals("Y")) {
                    correctionsDAO.delete(fbc);
                }
            }
        }
    }

    public void updatePBaSurCahrges(FclBlCorrectionsForm fclBlCorrectionsForm, String blumber, String noticeNumber, FclBlCorrections fclBlCorrections) throws Exception {
        FclBl bl = getLatestBlObject(fclBlCorrectionsForm);
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        Double sumAmountFromCharges = (Double) fclBlChargesDAO.sumOfADVFFandADVSHP(bl.getBol());
        Double sumAnountFromCorrection = (Double) correctionsDAO.sumOfADVFFandADVSHP(blumber, noticeNumber);
        Double pbaDifference = 0.0;
        Double pbaAmountOld = 0.0;
        Double pbaAmountNew = 0.0;
        FclBlCorrectionsForm fclBlCorrectionsFormToSend = new FclBlCorrectionsForm();
        PropertyUtils.copyProperties(fclBlCorrectionsFormToSend, fclBlCorrectionsForm);
        fclBlCorrectionsFormToSend.setChargeCode(FclBlConstants.ADVANCESURCHARGECODE);
        if (null != sumAnountFromCorrection && null != sumAmountFromCharges) {
            pbaAmountOld = CommonFunctions.getPercentOf(sumAmountFromCharges, new Integer(LoadLogisoftProperties.getProperty("advanceSurchargePercentage")));
            pbaDifference = CommonFunctions.getPercentOf(sumAnountFromCorrection, new Integer(LoadLogisoftProperties.getProperty("advanceSurchargePercentage")));
            pbaAmountNew = pbaDifference + pbaAmountOld;
            List<FclBlCorrections> PBAList = correctionsDAO.getCorrection(fclBlCorrectionsFormToSend);
            if (CommonFunctions.isNotNullOrNotEmpty(PBAList)) {
                FclBlCorrections fclBlCorrectionsNew = PBAList.get(0);
                fclBlCorrectionsNew.setNewAmount(pbaAmountNew);
                fclBlCorrectionsNew.setDiffereceAmount(pbaDifference);

            } else {
                FclBlCorrections fclBlCorrectionsNew = new FclBlCorrections();
                PropertyUtils.copyProperties(fclBlCorrectionsNew, fclBlCorrections);
                fclBlCorrectionsNew.setId(null);
                fclBlCorrectionsNew.setChargeCode(FclBlConstants.ADVANCESURCHARGECODE);
                fclBlCorrectionsNew.setChargeCodeDescription(FclBlConstants.ADVANCESURCHARGEDESC);
                fclBlCorrectionsNew.setAmount(pbaAmountOld);
                fclBlCorrectionsNew.setNewAmount(pbaAmountNew);
                fclBlCorrectionsNew.setDiffereceAmount(pbaDifference);
                correctionsDAO.save(fclBlCorrectionsNew);
            }
        } else {
            List<FclBlCorrections> PBAList = correctionsDAO.getCorrection(fclBlCorrectionsFormToSend);
            if (CommonFunctions.isNotNullOrNotEmpty(PBAList)) {
                FclBlCorrections fclBlCorrectionsNew = PBAList.get(0);
                correctionsDAO.delete(fclBlCorrectionsNew);

            }
        }
    }

    public void accountNumberAndNameFortypeA(FclBlCorrections fclBlCorrections, FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        FclBl fclBl = getLatestBlObject(fclBlCorrectionsForm);
        if (fclBlCorrections.getBillToParty().equalsIgnoreCase("Forwarder")) {
            fclBlCorrections.setAccountName(fclBl.getForwardingAgentName());
            fclBlCorrections.setAccountNumber(fclBl.getForwardAgentNo());
        } else if (fclBlCorrections.getBillToParty().equalsIgnoreCase("Shipper")) {
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                fclBlCorrections.setAccountName(fclBl.getHouseShipperName());
                fclBlCorrections.setAccountNumber(fclBl.getHouseShipper());
            } else {
                fclBlCorrections.setAccountName(fclBl.getShipperName());
                fclBlCorrections.setAccountNumber(fclBl.getShipperNo());
            }

        } else if (fclBlCorrections.getBillToParty().equalsIgnoreCase("ThirdParty")) {
            fclBlCorrections.setAccountName(fclBl.getThirdPartyName());
            fclBlCorrections.setAccountNumber(fclBl.getBillTrdPrty());
        } else if (fclBlCorrections.getBillToParty().equalsIgnoreCase("Agent")) {
            fclBlCorrections.setAccountName(fclBl.getAgent());
            fclBlCorrections.setAccountNumber(fclBl.getAgentNo());
        } else if (fclBlCorrections.getBillToParty().equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
            fclBlCorrections.setAccountName(fclBl.getConsigneeName());
            fclBlCorrections.setAccountNumber(fclBl.getConsigneeNo());
        }
        if (null != fclBlCorrections.getCorrectionType() && "Y".equalsIgnoreCase(fclBlCorrections.getCorrectionType().getCode())) {
            if (CommonUtils.isNotEmpty(fclBlCorrections.getOriginalBillToPartyCorrectionTypeY()) && !fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equals(fclBlCorrections.getBillToParty())) {
                if (CommonUtils.isNotEmpty(fclBlCorrections.getAmount())) {
                    if (fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equalsIgnoreCase("Forwarder")) {
                        fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getForwardingAgentName());
                        fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getForwardAgentNo());
                    } else if (fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equalsIgnoreCase("Shipper")) {
                        if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                            fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getHouseShipperName());
                            fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getHouseShipper());
                        } else {
                            fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getShipperName());
                            fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getShipperNo());
                        }
                    } else if (fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equalsIgnoreCase("ThirdParty")) {
                        fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getThirdPartyName());
                        fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getBillTrdPrty());
                    } else if (fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equalsIgnoreCase("Agent")) {
                        fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getAgent());
                        fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getAgentNo());
                    } else if (fclBlCorrections.getOriginalBillToPartyCorrectionTypeY().equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                        fclBlCorrections.setOriginalCustomerNameCorrectionTypeY(fclBl.getConsigneeName());
                        fclBlCorrections.setOriginalCustomerNumberCorrectionTypeY(fclBl.getConsigneeNo());
                    }
                    fclBlCorrections.setOriginalAmountCorrectionTypeY(fclBlCorrections.getAmount());
                }
            }
        }
    }

    public Integer getNoticeNumber(String blNumber) throws Exception {
        Integer noticeNumber = correctionsDAO.getNoticeNumber(blNumber);
        if (null != noticeNumber) {
            noticeNumber = noticeNumber + 1;
        } else {
            noticeNumber = 1;
        }
        return noticeNumber;
    }

    /**
     * @param fclBlCorrectionsForm
     * @param request
     * @return when we edit records from search page this records wil get excute
     */
    public FclBlCorrections editCorrectionRecord(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request, MessageResources messageResources) throws Exception {
        List fclBlCorrectionsList = listToDisplayOnSearchPage(fclBlCorrectionsForm);
        if (fclBlCorrectionsList.size() > 0) {
            fclBlCorrectionsIntance = (FclBlCorrections) fclBlCorrectionsList.get(0);
        }
        if (fclBlCorrectionsIntance != null) {
            if (fclBlCorrectionsIntance.getPrepaidCollect() != null) {
                fclBlCorrectionsForm.setHouseBl(fclBlCorrectionsIntance.getPrepaidCollect());
            }
            if (fclBlCorrectionsIntance.getCorrectionType() != null) {
                fclBlCorrectionsIntance.setCorrectionType(fclBlCorrectionsIntance.getCorrectionType());
                fclBlCorrectionsForm.setCorrectionType(fclBlCorrectionsIntance.getCorrectionType().getCode());
                fclBlCorrectionsIntance.setTempCrType(fclBlCorrectionsIntance.getCorrectionType().getId().toString());
            }
            if (fclBlCorrectionsIntance.getCorrectionCode() != null) {
                fclBlCorrectionsIntance.setCorrectionCode(fclBlCorrectionsIntance.getCorrectionCode());
                fclBlCorrectionsIntance.setTempCrCode(fclBlCorrectionsIntance.getCorrectionCode().getId().toString());
            }
            fclBlCorrectionsForm.setNoticeNo(fclBlCorrectionsIntance.getNoticeNo());
            request.setAttribute(FclBlConstants.FCL_BL_CORRECTION, fclBlCorrectionsIntance);
            if (fclBlCorrectionsIntance.getAccountName() != null) {
                fclBlCorrectionsIntance.setAccountName(fclBlCorrectionsIntance.getAccountName());
                fclBlCorrectionsForm.setAccountName(fclBlCorrectionsIntance.getAccountName());
            }
            if (fclBlCorrectionsIntance.getCurrentProfit() != null) {
                fclBlCorrectionsIntance.setCurrentProfit(fclBlCorrectionsIntance.getCurrentProfit());
                fclBlCorrectionsForm.setCurrentProfit(fclBlCorrectionsIntance.getCurrentProfit().toString());
            }
            if (fclBlCorrectionsIntance.getProfitAfterCn() != null) {
                fclBlCorrectionsIntance.setProfitAfterCn(fclBlCorrectionsIntance.getProfitAfterCn());
                fclBlCorrectionsForm.setProfitAfterCn(fclBlCorrectionsIntance.getProfitAfterCn().toString());
            }
            if (fclBlCorrectionsIntance.getAccountNumber() != null) {
                fclBlCorrectionsIntance.setAccountNumber(fclBlCorrectionsIntance.getAccountNumber());
                fclBlCorrectionsForm.setAccountNumber(fclBlCorrectionsIntance.getAccountNumber());
            }
            fclBlCorrectionsForm.setHouseBl(fclBlCorrectionsIntance.getPrepaidCollect());
        }
        // passing BL Number from parent page(fclbilladding.jsp) and getting the object of fclBl
        FclBl fclBl = blDAO.findById(fclBlCorrectionsForm.getBlNumber());
        if (null != fclBl) {
            //--based on BillToCode display the prepaid or collect customer name-----
            if (fclBl.getBillToCode() != null) {
                if (fclBl.getBillToCode().equals("F")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getForwardingAgentName());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getForwardAgentNo());
                } else if (fclBl.getBillToCode().equals("S")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getHouseShipperName());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getHouseShipper());
                } else if (fclBl.getBillToCode().equals("T")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getThirdPartyName());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getBillTrdPrty());
                } else if (fclBl.getBillToCode().equals("A")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getAgent());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getAgentNo());
                } else if (fclBl.getBillToCode().equals("C")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getConsigneeName());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getConsigneeNo());
                } else if (fclBl.getBillToCode().equalsIgnoreCase("N")) {
                    fclBlCorrectionsForm.setTempCustomerName(fclBl.getNotifyPartyName());
                    fclBlCorrectionsForm.setTempCustomerNo(fclBl.getNotifyParty());
                } else {
                    fclBlCorrectionsForm.setTempCustomerName("");
                    fclBlCorrectionsForm.setTempCustomerNo("");
                }
            }
        }
//        request.setAttribute("fclBl", fclBl);
        request.setAttribute(FclBlConstants.BLCORRECTIONFORM, fclBlCorrectionsForm);
        if (null != fclBlCorrectionsForm.getViewMode() && fclBlCorrectionsForm.getViewMode().equals("view")) {
            if ("A".equalsIgnoreCase(fclBlCorrectionsIntance.getCorrectionType().getCode()) || "Y".equalsIgnoreCase(fclBlCorrectionsIntance.getCorrectionType().getCode())) {
                String previousNoticeNo = new FclBlCorrectionsDAO().getPreviousNoticeNo(fclBlCorrectionsForm.getFileNo(), fclBlCorrectionsForm.getNoticeNo());
                String blNO = fclBlCorrectionsForm.getBlNumber() + (CommonUtils.isNotEmpty(previousNoticeNo) ? (FclBlConstants.DELIMITER + previousNoticeNo) : "");
                fclBl = blDAO.findById(blNO);
                if (null != fclBl) {
                    if ("A".equalsIgnoreCase(fclBlCorrectionsIntance.getCorrectionType().getCode())) {
                        request.setAttribute(FclBlConstants.FCL_BL_CHARGESLIST, getChargesListForTypeAViewMode(fclBl, fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), messageResources));
                    } else if ("Y".equalsIgnoreCase(fclBlCorrectionsIntance.getCorrectionType().getCode())) {
                        request.setAttribute(FclBlConstants.FCL_BL_CHARGESLIST, getChargesListFotTypeY(fclBl, fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), messageResources));
                    }
                    setPartyName(fclBlCorrectionsForm, fclBl);
                } else {
                    request.setAttribute(FclBlConstants.FCL_BL_CHARGESLIST, displayChargesDetailsForCTypeA(fclBlCorrectionsForm, request, messageResources));
                }
            } else {
                request.setAttribute(FclBlConstants.FCL_BL_CHARGESLIST, displayChargesDetailsForCTypeA(fclBlCorrectionsForm, request, messageResources));
            }
        } else {
            request.setAttribute(FclBlConstants.FCL_BL_CHARGESLIST, displayChargesDetailsForCTypeA(fclBlCorrectionsForm, request, messageResources));
        }

        request.setAttribute(FclBlConstants.FCLBL, fclBl);
        if (fclBlCorrectionsIntance == null) {
            fclBlCorrectionsIntance = new FclBlCorrections();
            fclBlCorrectionsIntance.setBlNumber(fclBlCorrectionsForm.getBlNumber());
        }
        request.setAttribute(FclBlConstants.FCL_BL_CORRECTION, fclBlCorrectionsIntance);
        return fclBlCorrectionsIntance;
    }

    /**
     * @param fclBlCorrections
     * @param fclBlCorrectionsForm setting Form value to FclBlCorrection Object
     */
    public void setFromValueToFclBlCorrectionsDomain(FclBlCorrections fclBlCorrections, FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        fclBlCorrections.setSendCopyTo(fclBlCorrectionsForm.getSendCopyTo());
        fclBlCorrections.setFileNo(fclBlCorrectionsForm.getFileNo());
        if (fclBlCorrectionsForm.getCorrectionType() != null && !fclBlCorrectionsForm.getCorrectionType().equals("")) {
            GenericCode crType = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType()));
            fclBlCorrections.setCorrectionType(crType);
        }
        if (fclBlCorrectionsForm.getCorrectionCode() != null && !fclBlCorrectionsForm.getCorrectionCode().equals("")) {
            GenericCode crCode = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionCode()));
            fclBlCorrections.setCorrectionCode(crCode);
        }
        if (CommonUtils.isNotEmpty(fclBlCorrections.getCorrectionType().getCode())) {
            if ("A".equalsIgnoreCase(fclBlCorrections.getCorrectionType().getCode())) {
                if (CommonUtils.isNotEmpty(fclBlCorrections.getDiffereceAmount()) && fclBlCorrections.getDiffereceAmount() > 0) {
                    fclBlCorrections.setDebitMemoEmail((null != fclBlCorrectionsForm.getDebitMemoEmail()
                            && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getDebitMemoEmail()))
                            ? fclBlCorrectionsForm.getDebitMemoEmail() : "");
                } else {
                    fclBlCorrections.setCreditMemoEmail((null != fclBlCorrectionsForm.getCreditMemoEmail()
                            && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getCreditMemoEmail()))
                            ? fclBlCorrectionsForm.getCreditMemoEmail() : "");
                }
            } else {
                fclBlCorrections.setDebitMemoEmail((null != fclBlCorrectionsForm.getDebitMemoEmail()
                        && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getDebitMemoEmail()))
                        ? fclBlCorrectionsForm.getDebitMemoEmail() : "");
                fclBlCorrections.setCreditMemoEmail((null != fclBlCorrectionsForm.getCreditMemoEmail()
                        && CommonUtils.isNotEmpty(fclBlCorrectionsForm.getCreditMemoEmail()))
                        ? fclBlCorrectionsForm.getCreditMemoEmail() : "");
            }

        }
        fclBlCorrections.setEmail(user.getEmail());
        fclBlCorrections.setComments(CommonFunctions.isNotNull(fclBlCorrectionsForm.getComments()) ? fclBlCorrectionsForm.getComments().toUpperCase() : fclBlCorrectionsForm.getComments());
        fclBlCorrections.setRemarks(fclBlCorrectionsForm.getRemarks());
        fclBlCorrections.setVoyages(fclBlCorrectionsForm.getVoyages());
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getAccountName())) {
            fclBlCorrections.setAccountName(fclBlCorrectionsForm.getAccountName());
        }
        if (CommonFunctions.isNotNull(fclBlCorrectionsForm.getAccountNumber())) {
            fclBlCorrections.setAccountNumber(fclBlCorrectionsForm.getAccountNumber());
        }
        fclBlCorrections.setShipper(fclBlCorrectionsForm.getShipper());
        fclBlCorrections.setForwarder(fclBlCorrectionsForm.getForwarder());
        fclBlCorrections.setThirdParty(fclBlCorrectionsForm.getThirdParty());
        fclBlCorrections.setOrigin(fclBlCorrectionsForm.getOrigin());
        fclBlCorrections.setPol(fclBlCorrectionsForm.getPol());
        fclBlCorrections.setPod(fclBlCorrectionsForm.getPod());
        fclBlCorrections.setDestination(fclBlCorrectionsForm.getDestination());
        fclBlCorrections.setRampCity(fclBlCorrectionsForm.getRampCity());
        fclBlCorrections.setAgent(fclBlCorrectionsForm.getAgent());
        sdf = new SimpleDateFormat("MM/dd/yyyy");
        fclBlCorrections.setSailDate(CommonFunctions.isNotNull(fclBlCorrectionsForm.getSailDate())
                ? sdf.parse(fclBlCorrectionsForm.getSailDate()) : null);
        fclBlCorrections.setUserName(fclBlCorrections.getUserName());
        fclBlCorrections.setPrepaidCollect(fclBlCorrectionsForm.getHouseBl());
    }

    /**
     * when deleting FCLBLCorretion object using trash Icon
     */
    public void deleteCorrectionRecord(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        List correctionsList = correctionsDAO.getFclBlCorrections(fclBlCorrectionsForm.getTestBoxBlNumber(),
                fclBlCorrectionsForm.getTestBoxNoticeNumber());
        // need to cheack once unmanifest will work
		/*cancelManifestCorrections(fclBlCorrectionsForm.getBlNumber(),
        fclBlCorrectionsForm.getNoticeNo());*/
        fclBlBC.disAproveCorrectedBL(fclBlCorrectionsForm.getTestBoxNoticeNumber(),
                fclBlCorrectionsForm.getTestBoxBlNumber());
        for (Iterator iterator = correctionsList.iterator(); iterator.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iterator.next();
            fclBlCorrections.setStatus(FclBlConstants.DISABLE);
            fclBlCorrections.setWhoPosted(fclBlCorrectionsForm.getUserName());
            fclBlCorrections.setPostedDate(new Date());
            // fclBlCorrectionsDAO.delete(fclBlCorrections);
        }
    }

    /**
     * when clicked on Add new Button from search page it wil get all
     * FclBlCharges Object and add into List
     */
    public void getListFromFCLBLForAddnew(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request,
            MessageResources messageResources) throws Exception {
        fclBlCorrectionsIntance = new FclBlCorrections();
        // passing BL Number from parent page(fclbilladding.jsp) and getting the object of fclBl
        FclBl fclBl = getLatestBlObject(fclBlCorrectionsForm);
        //getting object of session....
        HttpSession session = ((HttpServletRequest) request).getSession();
        //setting into request object...
        request.setAttribute(FclBlConstants.FCLBL, fclBl);
        //--setting streamshipline----
        setPartyName(fclBlCorrectionsForm, fclBl);
        //--session set for adding new charges----
        session.setAttribute("FclBlChargesList", getListOfChargesFromFclBl(fclBl, messageResources, request, fclBlCorrectionsForm.getBlNumber()));
        fclBlCorrectionsIntance.setBlNumber(fclBlCorrectionsForm.getBlNumber());
        request.setAttribute(FclBlConstants.FCL_BL_CORRECTION, fclBlCorrectionsIntance);
    }

    public void setPartyName(FclBlCorrectionsForm fclBlCorrectionsForm, FclBl bl) throws Exception {
        FclBl previousBl = new FclBlDAO().getPreviousBl(bl.getFileNo(), bl.getBol());
        if (null == previousBl) {
            previousBl = bl;
        }
        if (previousBl.getHouseBl() != null) {
            if (previousBl.getHouseBl().equalsIgnoreCase("P-Prepaid")) {
                fclBlCorrectionsForm.setStreamShipBlFromBlPage("P");
                fclBlCorrectionsForm.setHouseBl("P");
            } else if (previousBl.getHouseBl().equalsIgnoreCase("C-Collect")) {
                fclBlCorrectionsForm.setStreamShipBlFromBlPage("C");
                fclBlCorrectionsForm.setHouseBl("C");
            } else {
                fclBlCorrectionsForm.setStreamShipBlFromBlPage("B");
                fclBlCorrectionsForm.setHouseBl("B");
            }
        }
        //--based on BillToCode display the prepaid or collect customer name-----
        if (previousBl.getBillToCode() != null) {
            if (previousBl.getBillToCode().equals("F")) {
                fclBlCorrectionsForm.setTempCustomerName(previousBl.getForwardingAgentName());
                fclBlCorrectionsForm.setTempCustomerNo(previousBl.getForwardAgentNo());
            } else if (previousBl.getBillToCode().equals("S")) {
                if (previousBl.getFileType().equalsIgnoreCase("I")) {
                    fclBlCorrectionsForm.setTempCustomerName(previousBl.getHouseShipperName());
                    fclBlCorrectionsForm.setTempCustomerNo(previousBl.getHouseShipper());
                } else {
                    fclBlCorrectionsForm.setTempCustomerName(previousBl.getShipperName());
                    fclBlCorrectionsForm.setTempCustomerNo(previousBl.getShipperNo());
                }
            } else if (previousBl.getBillToCode().equals("T")) {
                fclBlCorrectionsForm.setTempCustomerName(previousBl.getThirdPartyName());
                fclBlCorrectionsForm.setTempCustomerNo(previousBl.getBillTrdPrty());
            } else if (previousBl.getBillToCode().equals("A")) {
                fclBlCorrectionsForm.setTempCustomerName(previousBl.getAgent());
                fclBlCorrectionsForm.setTempCustomerNo(previousBl.getAgentNo());
            } else if (previousBl.getBillToCode().equals("C")) {
                fclBlCorrectionsForm.setTempCustomerName(previousBl.getConsigneeName());
                fclBlCorrectionsForm.setTempCustomerNo(previousBl.getConsigneeNo());
            } else if (previousBl.getBillToCode().equalsIgnoreCase("N")) {
                fclBlCorrectionsForm.setTempCustomerName(previousBl.getNotifyPartyName());
                fclBlCorrectionsForm.setTempCustomerNo(previousBl.getNotifyParty());
            }
        }
    }

    public List getChargesListFotTypeA(FclBl fclBl, String blNumber, String noticeNumber, MessageResources messageResources) throws Exception {
        List<FclBlCorrections> correctionsList = new ArrayList<FclBlCorrections>();
        List<FclBlCorrections> newCorrectionsList = new ArrayList<FclBlCorrections>();
        List fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(blNumber, noticeNumber);
        Map<String, FclBlCorrections> hashMap = new HashMap<String, FclBlCorrections>(); //----adding to hash map----
        List<String> uniqeKeys = new ArrayList<String>(); //----adding to hash map----
        List<FclBlCharges> collapseList = new ArrayList<FclBlCharges>();
        if (fclBl.getFclcharge() != null) {
            collapseList = getFclBlChargesList(fclBl, messageResources, noticeNumber);
            for (FclBlCharges fclBlCharges : collapseList) {
                uniqeKeys.add(fclBlCharges.getChargeCode().trim() + "" + fclBlCharges.getChargesId());
            }
        }
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (fclBlCorrections.getChargeCode() != null) {
                if (null != fclBlCorrections.getFclBlChargeId()) {
                    if (uniqeKeys.contains(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId())) {
                        hashMap.put(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId(), fclBlCorrections);
                    } else {
                        newCorrectionsList.add(fclBlCorrections);
                    }
                } else {
                    newCorrectionsList.add(fclBlCorrections);
                    // hashMap.put(fclBlCorrections.getChargeCode().trim(), fclBlCorrections);
                }
            }
        }
        if (fclBl.getFclcharge() != null) {
            for (FclBlCharges fclBlCharges : collapseList) {
                FclBlCorrections newFclBlCorrections = new FclBlCorrections();
                if (hashMap.containsKey(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId())) {
                    correctionsList.add(hashMap.get(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId()));
                } else {
                    newFclBlCorrections.setChargeCode(fclBlCharges.getChargeCode());
                    newFclBlCorrections.setChargeCodeDescription(fclBlCharges.getCharges());
                    newFclBlCorrections.setAmount(fclBlCharges.getAmount());
                    newFclBlCorrections.setBillToParty(fclBlCharges.getBillTo());
                    newFclBlCorrections.setFclBlChargeId(fclBlCharges.getChargesId());
                    GenericCode crType = genericCodeDAO.findByCodeName("A", FclBlConstants.CODETYPEID);
                    newFclBlCorrections.setCorrectionType(crType);
                    correctionsList.add(newFclBlCorrections);
                }
            }
        }
        correctionsList.addAll(newCorrectionsList);
        return correctionsList;
    }

    public List getChargesListFotTypeA(FclBl fclBl, String blNumber, String noticeNumber) throws Exception {
        List correctionsList = new ArrayList();
        List fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(blNumber, noticeNumber);
        Map hashMap = new HashMap(); //----adding to hash map----
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (fclBlCorrections.getChargeCode() != null) {
                if (null != fclBlCorrections.getFclBlChargeId()) {
                    hashMap.put(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId(), fclBlCorrections);
                } else {
                    hashMap.put(fclBlCorrections.getChargeCode().trim(), fclBlCorrections);
                }
                correctionsList.add(fclBlCorrections);
            }
        }
        if (fclBl.getFclcharge() != null) {
            List<FclBlCharges> CollapsList = getFclBlChargesList(fclBl, noticeNumber);
            for (FclBlCharges fclBlCharges : CollapsList) {
                FclBlCorrections newFclBlCorrections = new FclBlCorrections();
                if (!hashMap.containsKey(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId())) {
                    newFclBlCorrections.setChargeCode(fclBlCharges.getChargeCode());
                    newFclBlCorrections.setChargeCodeDescription(fclBlCharges.getCharges());
                    newFclBlCorrections.setAmount(fclBlCharges.getAmount());
                    newFclBlCorrections.setBillToParty(fclBlCharges.getBillTo());
                    newFclBlCorrections.setFclBlChargeId(fclBlCharges.getChargesId());
                    GenericCode crType = genericCodeDAO.findByCodeName("A", FclBlConstants.CODETYPEID);
                    newFclBlCorrections.setCorrectionType(crType);
                    correctionsList.add(newFclBlCorrections);
                }
            }
        }

        return correctionsList;
    }

    public List getChargesListForTypeAViewMode(FclBl fclBl, String blNumber, String noticeNumber, MessageResources messageResources) throws Exception {
        List correctionsList = new ArrayList();
        List fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(blNumber, noticeNumber);
        List<FclBlCharges> collapseList = new ArrayList<FclBlCharges>();
        List<String> uniqeKeys = new ArrayList<String>();
        Map<String, FclBlCorrections> hashMap = new HashMap<String, FclBlCorrections>(); //----adding to hash map----
        if (fclBl.getFclcharge() != null) {
            collapseList = getFclBlChargesList(fclBl, messageResources, noticeNumber);
            for (FclBlCharges fclBlCharges : collapseList) {
                uniqeKeys.add(fclBlCharges.getChargeCode().trim() + "" + fclBlCharges.getChargesId());
            }
        }
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (fclBlCorrections.getChargeCode() != null) {
                if (null != fclBlCorrections.getFclBlChargeId()) {
                    if (uniqeKeys.contains(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId())) {
                        hashMap.put(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId(), fclBlCorrections);
                    } else {
                        hashMap.put(fclBlCorrections.getChargeCode().trim(), fclBlCorrections);
                    }
                }
//	     correctionsList.add(fclBlCorrections.getChargeCode().trim());
            }
        }
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (!hashMap.containsKey(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId())) {
                correctionsList.add(fclBlCorrections);
            }
        }
        if (fclBl.getFclcharge() != null) {
            for (FclBlCharges fclBlCharges : collapseList) {
                FclBlCorrections newFclBlCorrections = new FclBlCorrections();
                if (hashMap.containsKey(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId())) {
                    correctionsList.add(hashMap.get(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId()));
                } else {
                    newFclBlCorrections.setChargeCode(fclBlCharges.getChargeCode());
                    newFclBlCorrections.setChargeCodeDescription(fclBlCharges.getCharges());
                    newFclBlCorrections.setAmount(fclBlCharges.getAmount());
                    newFclBlCorrections.setBillToParty(fclBlCharges.getBillTo());
                    newFclBlCorrections.setFclBlChargeId(fclBlCharges.getChargesId());
                    GenericCode crType = genericCodeDAO.findByCodeName("A", FclBlConstants.CODETYPEID);
                    newFclBlCorrections.setCorrectionType(crType);
                    correctionsList.add(newFclBlCorrections);
                }
            }
        }
        return getSortedList(correctionsList);
    }

    public List getChargesListFotTypeY(FclBl bl, String blNumber, String noticeNumber, MessageResources messageResources) throws Exception {
        FclBl previousBl = new FclBlDAO().getPreviousBl(bl.getFileNo(), bl.getBol());
        if (null == previousBl) {
            previousBl = bl;
        }
        List correctionsList = new ArrayList();
        List fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(blNumber, noticeNumber);
        Map hashMap = new HashMap(); //----adding to hash map----
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (fclBlCorrections.getChargeCode() != null) {
                if (null != fclBlCorrections.getFclBlChargeId()) {
                    hashMap.put(fclBlCorrections.getChargeCode().trim() + "" + fclBlCorrections.getFclBlChargeId(), fclBlCorrections);
                } else {
                    hashMap.put(fclBlCorrections.getChargeCode().trim(), fclBlCorrections);
                }
                correctionsList.add(fclBlCorrections);
            }
        }
        if (previousBl.getFclcharge() != null) {
            List<FclBlCharges> CollapsList = getFclBlChargesList(previousBl, messageResources, noticeNumber);
            for (FclBlCharges fclBlCharges : CollapsList) {
                FclBlCorrections newFclBlCorrections = new FclBlCorrections();
                if (!hashMap.containsKey(fclBlCharges.getChargeCode() + "" + fclBlCharges.getChargesId())) {
                    List fclBlChargeCodeList = new ArrayList();
                    boolean checkChargeCode = false;
                    for (Iterator iterator = fclBlCorrectionsList.iterator(); iterator.hasNext();) {
                        FclBlCorrections fclBlCorrections = (FclBlCorrections) iterator.next();
                        fclBlChargeCodeList.add(fclBlCorrections.getChargeCode());
                    }
                    for (int i = 0; i < fclBlChargeCodeList.size(); i++) {
                        String chargeCode = fclBlChargeCodeList.get(i).toString();
                        if (!fclBlCharges.getChargeCode().equalsIgnoreCase(chargeCode)) {
                            checkChargeCode = true;
                        }
                    }
                    if (checkChargeCode) {
                        newFclBlCorrections.setChargeCode(fclBlCharges.getChargeCode());
                        newFclBlCorrections.setChargeCodeDescription(fclBlCharges.getCharges());
                        newFclBlCorrections.setAmount(fclBlCharges.getAmount());
                        newFclBlCorrections.setBillToParty(fclBlCharges.getBillTo());
                        newFclBlCorrections.setFclBlChargeId(fclBlCharges.getChargesId());
                        GenericCode crType = genericCodeDAO.findByCodeName("Y", FclBlConstants.CODETYPEID);
                        newFclBlCorrections.setCorrectionType(crType);
                        correctionsList.add(newFclBlCorrections);
                    }
                }
            }
        }
        return getSortedList(correctionsList);
    }

    /**
     * this wil call when charges wil display on addfclblcorrection page charges
     * are including FclBl and Corrections
     */
    public List displayChargesDetailsForCTypeA(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request, MessageResources messageResources) throws Exception {
        List correctionsList = new ArrayList();
        FclBl fclBl = new FclBl();
        fclBl = getLatestBlObject(fclBlCorrectionsForm);
        if (null != fclBl) {
            correctionsList = getChargesListFotTypeA(fclBl, fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), messageResources);
            setPartyName(fclBlCorrectionsForm, fclBl);
        }
        return getSortedList(correctionsList);
    }

    public List displayChargesDetailsForCTypeY(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request, MessageResources messageResources) throws Exception {
        List correctionsList = new ArrayList();
        FclBl fclBl = new FclBl();
        fclBl = getLatestBlObject(fclBlCorrectionsForm);
        if (null != fclBl) {
            correctionsList = getChargesListFotTypeY(fclBl, fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getNoticeNo(), messageResources);
            setPartyName(fclBlCorrectionsForm, fclBl);
        }
        return correctionsList;
    }

    public void setValuesFclBLCorrections(FclBlCorrectionsForm fclBlCorrectionsForm, List chargeslist, HttpServletRequest request) throws Exception {
    }

    public void addNewRowofCharges(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request) throws Exception {
    }

    public void deleteEachRowofCharges(FclBlCorrectionsForm fclBlCorrectionsForm, HttpServletRequest request, MessageResources messageResources) throws Exception {
        if (fclBlCorrectionsForm.getId() != null && !fclBlCorrectionsForm.getId().equals("")) {
            FclBlCorrections fclBlCorrections = correctionsDAO.findById(new Integer(fclBlCorrectionsForm.getId()));
            String chargCode = fclBlCorrections.getChargeCode();
            String blNumber = fclBlCorrections.getBlNumber();
            String noticeNumber = fclBlCorrections.getNoticeNo();
            correctionsDAO.delete(fclBlCorrections);
            if (FclBlConstants.ADVANCEFFCODE.equalsIgnoreCase(chargCode)
                    || FclBlConstants.ADVANCESHIPPERCODE.equalsIgnoreCase(chargCode)) {
                updatePBaSurCahrges(fclBlCorrectionsForm, blNumber, noticeNumber, null);
            }

        }

        fclBlCorrectionsForm.setId(null);
        editCorrectionRecord(fclBlCorrectionsForm, request, messageResources);
    }

    public FclBlCorrections getFclBlCorrectionObjforDisplay(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        fclBlCorrectionsIntance = new FclBlCorrections();
        if (!fclBlCorrectionsForm.getCorrectionType().equals("") && !fclBlCorrectionsForm.getCorrectionType().equals("0")) {
            GenericCode crType = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType()));
            fclBlCorrectionsIntance.setTempCrType(crType.getId().toString());
        }
        if (fclBlCorrectionsForm.getCorrectionCode() != null && !fclBlCorrectionsForm.getCorrectionCode().equals("0")) {
            GenericCode crCode = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionCode()));
            fclBlCorrectionsIntance.setTempCrCode(crCode.getId().toString());
        }
        fclBlCorrectionsIntance.setBlNumber(fclBlCorrectionsForm.getBlNumber());
        fclBlCorrectionsIntance.setAccountNumber(fclBlCorrectionsForm.getAccountNumber());
        fclBlCorrectionsIntance.setEmail(fclBlCorrectionsForm.getEmail());
        fclBlCorrectionsIntance.setComments(CommonFunctions.isNotNull(fclBlCorrectionsForm.getComments())
                ? fclBlCorrectionsForm.getComments().toUpperCase() : fclBlCorrectionsForm.getComments());

        fclBlCorrectionsIntance.setRemarks(fclBlCorrectionsForm.getRemarks());
        fclBlCorrectionsIntance.setNoticeNo(fclBlCorrectionsForm.getNoticeNo());
        fclBlCorrectionsIntance.setAccountName(fclBlCorrectionsForm.getAccountName());
        return fclBlCorrectionsIntance;
    }

    public String checkPassword(String UserName, FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        return userDAO.findByPassword(UserName, fclBlCorrectionsForm.getPassword());
    }

    public void createdLatesBl(FclBlCorrectionsForm fclBlCorrectionsForm, String loginName, HttpServletRequest request) throws Exception {
        List fclBlCorrectionsList = correctionsDAO.getFclBlCorrections(fclBlCorrectionsForm.getBlNumber(), fclBlCorrectionsForm.getId());
        // getting all Fcl bl correction from fclblCorrection table......

        if (!fclBlCorrectionsList.isEmpty()) {
            List<FclBlCharges> fclBlChargesList = new ArrayList<FclBlCharges>();//List to add existing charges
            Set<FclBlCharges> fclBlChargesSet = new HashSet<FclBlCharges>();//set to add existing List of charges
            Set<FclBlCostCodes> fclBlCostCodesSet = new HashSet<FclBlCostCodes>();//set to add existing List of  cost code
            Set<FclBlContainer> fclBlContainerSet = new HashSet<FclBlContainer>();//set to add existing container..
            Set<BlClauses> fclBlClausesSet = new HashSet<BlClauses>();//set add existing BlClauses
            Set<FclAESDetails> fclBlAESSet = new HashSet<FclAESDetails>();
            Set<FclInbondDetails> fclBlInbondSet = new HashSet<FclInbondDetails>();
            FclBl fclBlObject = getLatestBlObject(fclBlCorrectionsForm); //
            FclBl originalBl = blDAO.findById(fclBlCorrectionsForm.getBlNumber());
            FclBl fclBl = new FclBl();
            // copying old Fcl Bl to new Fcl Bl Object
            PropertyUtils.copyProperties(fclBl, fclBlObject);
            fclBl.setBol(null);

            if (fclBl.getFclcontainer() != null) {//adding old Bl conatiner objects to new fclbl container object
                QuotationBC quotationBC = new QuotationBC();
                int i = 1;
                for (Iterator iter = fclBl.getFclcontainer().iterator(); iter.hasNext();) {
                    Set<FclBlMarks> fclBlMarksList = new HashSet<FclBlMarks>();
                    FclBlContainer fclBlContainerOld = (FclBlContainer) iter.next();
                    FclBlContainer fclBlContainer = new FclBlContainer();
                    PropertyUtils.copyProperties(fclBlContainer, fclBlContainerOld);
                    fclBlContainer.setBolId(null);
                    // setting marks and number.....
                    if (fclBlContainer.getFclBlMarks() != null) {// adding fclMarks to set.....
                        for (Iterator iterator = fclBlContainer.getFclBlMarks().iterator(); iterator.hasNext();) {
                            FclBlMarks fclBlMarksOld = (FclBlMarks) iterator.next();
                            FclBlMarks fclBlMarks = new FclBlMarks();
                            PropertyUtils.copyProperties(fclBlMarks, fclBlMarksOld);
                            fclBlMarks.setId(null);
                            fclBlMarks.setTrailerNoId(null);
                            fclBlMarksList.add(fclBlMarks);
                        }
                        fclBlContainer.setFclBlMarks(fclBlMarksList);
                    }
                    // setting hazmat.......
                    List hazmatList = quotationBC.getHazmatList("FclBl", String.valueOf(fclBlContainer.getTrailerNoId()));
                    Set<HazmatMaterial> hazmatMaterialList = new HashSet<HazmatMaterial>();
                    for (Iterator iterator = hazmatList.iterator(); iterator.hasNext();) {
                        HazmatMaterial hazmatMaterialObject = (HazmatMaterial) iterator.next();
                        HazmatMaterial hazmatMaterial = new HazmatMaterial();
                        PropertyUtils.copyProperties(hazmatMaterial, hazmatMaterialObject);
                        hazmatMaterial.setId(null);
                        hazmatMaterialList.add(hazmatMaterial);
                    }
                    // adding HazmatMaterail object to fclBlContainer set(this is temprory set....)
                    fclBlContainer.setHazmatMaterialSet(hazmatMaterialList);
                    fclBlContainer.setTrailerNoId(null);
                    fclBlContainerSet.add(fclBlContainer);
                }
            }
            if (fclBl.getFclInbondDetails() != null) {// adding FclInbond to set.....
                for (Iterator iterator = fclBl.getFclInbondDetails().iterator(); iterator.hasNext();) {
                    FclInbondDetails fclInbondDetailsOld = (FclInbondDetails) iterator.next();
                    FclInbondDetails fclInbondDetails = new FclInbondDetails();
                    PropertyUtils.copyProperties(fclInbondDetails, fclInbondDetailsOld);
                    fclInbondDetails.setId(null);
//                        fclInbondDetails.setBolId(null);
                    fclBlInbondSet.add(fclInbondDetails);
                }
            }
            // looping list of charges code from fclBl correction table to create new FclBl record in fclbl table.....
            String correctionType = "";
            for (int i = 0; i < fclBlCorrectionsList.size(); i++) {
                FclBlCorrections fclBlCorrections = (FclBlCorrections) fclBlCorrectionsList.get(i);
                fclBlCorrections.setStatus("Approved");
                fclBlCorrections.setApproval(loginName);
                String newBolNumber = fclBlCorrections.getBlNumber() + FclBlConstants.DELIMITER + fclBlCorrections.getNoticeNo();
                fclBlCorrections.setNewBolIdForApprovedBl(newBolNumber);
                // setting status as Approved.........to fclCorrection record...
                if (i == 0) {// fetching frist record to set fileds to fclBl property....
                    // for FCl Bl....
                    if (fclBlCorrections.getCorrectionType() != null) {
                        correctionType = fclBlCorrections.getCorrectionType().getCode();
                        if (correctionType != null && (correctionType.equalsIgnoreCase("C")
                                || correctionType.equalsIgnoreCase("G") || correctionType.equalsIgnoreCase("I")
                                || correctionType.equalsIgnoreCase("J") || correctionType.equalsIgnoreCase("N")
                                || correctionType.equalsIgnoreCase("Q") || correctionType.equalsIgnoreCase("X"))) {
                            // shipper
                            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                                fclBl.setHouseShipperName(fclBlCorrections.getAccountName());
                                fclBl.setHouseShipper(fclBlCorrections.getAccountNumber());
                            } else {
                                fclBl.setShipperName(fclBlCorrections.getAccountName());
                                fclBl.setShipperNo(fclBlCorrections.getAccountNumber());
                                if ("J".equalsIgnoreCase(correctionType) || "Q".equalsIgnoreCase(correctionType) || "U".equalsIgnoreCase(correctionType) || "X".equalsIgnoreCase(correctionType) || "N".equalsIgnoreCase(correctionType)) {
                                    fclBl.setShipperAddress(getAddress(fclBlCorrections.getAccountNumber()));
                                }
                            }
                            fclBl.setBillToCode("S");
                            fclBl.setHouseBl("P-Prepaid");
                        } else if (correctionType != null && (correctionType.equalsIgnoreCase("D")
                                || correctionType.equalsIgnoreCase("F") || correctionType.equalsIgnoreCase("H")
                                || correctionType.equalsIgnoreCase("K") || correctionType.equalsIgnoreCase("R")
                                || correctionType.equalsIgnoreCase("W") || correctionType.equalsIgnoreCase("M"))) {
                            //forwarder
                            fclBl.setForwardingAgentName(fclBlCorrections.getAccountName());
                            fclBl.setForwardAgentNo(fclBlCorrections.getAccountNumber());
                            if ("K".equalsIgnoreCase(correctionType) || "M".equalsIgnoreCase(correctionType) || "R".equalsIgnoreCase(correctionType)
                                    || "T".equalsIgnoreCase(correctionType) || "W".equalsIgnoreCase(correctionType)) {
                                fclBl.setForwardingAgent(getAddress(fclBlCorrections.getAccountNumber()));
                            }
                            fclBl.setBillToCode("F");
                            fclBl.setHouseBl("P-Prepaid");
                        } else if (correctionType != null && (correctionType.equalsIgnoreCase("E")
                                || correctionType.equalsIgnoreCase("L") || correctionType.equalsIgnoreCase("P")
                                || correctionType.equalsIgnoreCase("S") || correctionType.equalsIgnoreCase("V"))) {
                            //third party
                            fclBl.setThirdPartyName(fclBlCorrections.getAccountName());
                            fclBl.setBillTrdPrty(fclBlCorrections.getAccountNumber());
                            fclBl.setBillToCode("T");
                            fclBl.setHouseBl("P-Prepaid");
                        } else if (correctionType != null && correctionType.equalsIgnoreCase("B")) {
                            // agent
                            fclBl.setAgent(fclBlCorrections.getAccountName());
                            fclBl.setAgentNo(fclBlCorrections.getAccountNumber());
                            fclBl.setBillToCode("A");
                            fclBl.setHouseBl("C-Collect");
                        } else if (correctionType != null && (correctionType.equalsIgnoreCase("T") || correctionType.equalsIgnoreCase("U"))) {
                            if (correctionType.equalsIgnoreCase("T")) {
                                fclBl.setForwardingAgentName(fclBlCorrections.getAccountName());
                                fclBl.setForwardAgentNo(fclBlCorrections.getAccountNumber());
                            }
                            if (correctionType.equalsIgnoreCase("U")) {
                                fclBl.setShipperName(fclBlCorrections.getAccountName());
                                fclBl.setShipperNo(fclBlCorrections.getAccountNumber());
                            }
                        } else {
                            List billtoPartyList = correctionsDAO.getBillToParty("billToParty", fclBlCorrections.getNoticeNo(), fclBlCorrections.getBlNumber());
                            if (CommonFunctions.isNotNullOrNotEmpty(billtoPartyList) && billtoPartyList.size() > 1) {
                                fclBl.setBillToCode(null);
                                fclBl.setHouseBl("B-Both");
                            } else if (CommonFunctions.isNotNullOrNotEmpty(billtoPartyList) && billtoPartyList.size() == 1) {
                                String biltoFromCorrection = (String) billtoPartyList.get(0);
                                FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
                                List li = fclBlChargesDAO.getFclBlChargeBillTo(biltoFromCorrection, fclBlObject.getBol());
                                if (!CommonFunctions.isNotNullOrNotEmpty(li)) {
                                    fclBl.setBillToCode(null);
                                    fclBl.setHouseBl("B-Both");
                                }
                            }

                        }
                    }
                    fclBl.setBolId(newBolNumber);
                    // Updating charge code to FclBl object if it exist....
                    if (fclBl.getFclcharge() != null) {//add existing charges
                        for (Iterator iter = fclBl.getFclcharge().iterator(); iter.hasNext();) {
                            FclBlCharges fclBlChargesOld = (FclBlCharges) iter.next();
                            FclBlCharges fclBlCharges = new FclBlCharges();
                            if (("C".equals(correctionType) || "D".equals(correctionType) || "Q".equals(correctionType) || "R".equals(correctionType) || "S".equals(correctionType)) && "CAF".equalsIgnoreCase(fclBlChargesOld.getChargeCode())) {
                                //remove caf
                            } else if (null != fclBlChargesOld.getFaeIncentFlag() && fclBlChargesOld.getFaeIncentFlag().equals("Y") && fclBlChargesOld.getChargeCode().equals(FclBlConstants.ADMINFEEWITHNOCOMMISION) && !"ABUTY".contains(correctionType)) {
                                //remove fae INCENT
                                FclBl fclbl = blDAO.findById(fclBlCorrections.getBlNumber());
                                FclBlChargesDAO chagesDao = new FclBlChargesDAO();
                                new FclBlBC().notesForIncentCharge(fclbl.getBol(), request, "DELETED using Correction type '" + correctionType + "' ", "INCENT");
                                chagesDao.deleteFaeIncentCharge(fclbl.getBol());
                            } else {
                                PropertyUtils.copyProperties(fclBlCharges, fclBlChargesOld);
                                if ((!FclBlConstants.ADVANCESURCHARGECODE.equals(fclBlCharges.getChargeCode()) && !FclBlConstants.ADVANCESHIPPERCODE.equals(fclBlCharges.getChargeCode())
                                        && !FclBlConstants.ADVANCEFFCODE.equals(fclBlCharges.getChargeCode())) || ("A".equals(correctionType) || "U".equals(correctionType) || "T".equals(correctionType) || "Y".equals(correctionType))) {
                                    fclBlCharges.setChargesId(null);
                                    fclBlCharges.setTempChargeId(fclBlChargesOld.getChargesId());
                                    fclBlCharges.setBolId(null);
                                    if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("S")) {
                                        fclBlCharges.setBillTo("Shipper");
                                    } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("F")) {
                                        fclBlCharges.setBillTo("Forwarder");
                                    } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("T")) {
                                        fclBlCharges.setBillTo("ThirdParty");
                                    } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("A")) {
                                        fclBlCharges.setBillTo("Agent");
                                    } else if (fclBl.getBillToCode() != null && fclBl.getBillToCode().equalsIgnoreCase("C")) {
                                        fclBlCharges.setBillTo(FclBlConstants.CONSIGNEE);
                                    }
                                    fclBlChargesList.add(fclBlCharges);
                                }
                            }
                        }

                    }
                    updateChargesFromCorrectionToFclBlCharges(fclBlChargesList, fclBlCorrections);
                } else {
                    updateChargesFromCorrectionToFclBlCharges(fclBlChargesList, fclBlCorrections);
                }
            }
            // adding fclBlCharges List to set object
            fclBlChargesSet.addAll(fclBlChargesList);
            // calculating pbasur charge
            // setting all set to FclBl Object.....
            fclBl.setFclcharge(fclBlChargesSet);
            fclBl.setFclblcostcodes(null);
            fclBl.setFclcontainer(fclBlContainerSet);
            fclBl.setFclBlClauses(fclBlClausesSet);
            fclBl.setFclAesDetails(null);
            fclBl.setFclInbondDetails(null);
            fclBl.setImportPaymentRelease(null);
            fclBl.setFclBlClauses(null);
            //setting print options to corrected bls
            fclBl.setAgentsForCarrier(originalBl.getAgentsForCarrier());
            fclBl.setShipperLoadsAndCounts(originalBl.getShipperLoadsAndCounts());
            fclBl.setPrintContainersOnBL(originalBl.getPrintContainersOnBL());
            fclBl.setCountryOfOrigin(originalBl.getCountryOfOrigin());
            fclBl.setNoOfPackages(originalBl.getNoOfPackages());
            fclBl.setAlternateNoOfPackages(originalBl.getAlternateNoOfPackages());
            fclBl.setTotalContainers(originalBl.getTotalContainers());
            fclBl.setProof(originalBl.getProof());
            fclBl.setPreAlert(originalBl.getPreAlert());
            fclBl.setNonNegotiable(originalBl.getNonNegotiable());
            fclBl.setPrintRev(originalBl.getPrintRev());
            fclBl.setPrintAlternatePort(originalBl.getPrintAlternatePort());
            fclBl.setHblPOLOverride(originalBl.getHblPOLOverride());
            fclBl.setHblPODOverride(originalBl.getHblPODOverride());
            fclBl.setHblFDOverride(originalBl.getHblFDOverride());
            fclBl.setHblPlaceReceiptOverride(originalBl.getHblPlaceReceiptOverride());
            fclBl.setDoorOriginAsPlor(originalBl.getDoorOriginAsPlor());
            fclBl.setDoorOriginAsPlorHouse(originalBl.getDoorOriginAsPlorHouse());
            fclBl.setDoorDestinationAsFinalDeliveryToHouse(originalBl.getDoorDestinationAsFinalDeliveryToHouse());
            fclBl.setDoorDestinationAsFinalDeliveryToMaster(originalBl.getDoorDestinationAsFinalDeliveryToMaster());
            fclBl.setCollectThirdParty(originalBl.getCollectThirdParty());
            fclBl.setTrimTrailingZerosForQty(originalBl.getTrimTrailingZerosForQty());
            fclBl.setOmitTermAndPort(originalBl.getOmitTermAndPort());
            fclBl.setServiceContractNo(originalBl.getServiceContractNo());
            fclBl.setCertifiedTrueCopy(originalBl.getCertifiedTrueCopy());
            fclBl.setRatedManifest(originalBl.getRatedManifest());
            fclBl.setOmit2LetterCountryCode(originalBl.getOmit2LetterCountryCode());
            fclBl.setDockReceipt(originalBl.getDockReceipt());
            // saving fclBl Object......
            blDAO.save(fclBl);
            if (fclBl.getFclcontainer() != null) {
                //outer:
                for (Iterator iter = fclBl.getFclcontainer().iterator(); iter.hasNext();) {
                    FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                    HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
                    for (Iterator iterator2 = fclBlContainer.getHazmatMaterialSet().iterator(); iterator2.hasNext();) {
                        HazmatMaterial hazmatMaterial = (HazmatMaterial) iterator2.next();
                        hazmatMaterial.setBolId(fclBlContainer.getTrailerNoId());
                        hazmatMaterial.setDocTypeId(String.valueOf(fclBlContainer.getTrailerNoId()));
                        // getting hazmatMaterial object from fclContainer temporary set and saving in DB..........
                        hazmatMaterialDAO.save(hazmatMaterial);
                    }
                } //end of for
            }// end of if....
        }

    }

    public String checkStatus(String bolId, HttpServletRequest request) throws Exception {
        List fclBlCorrectionsList = correctionsDAO.getCorrectionToDisplayOnPopup(bolId, true);
        String flag = null;
        HashMap searchMap = new HashMap();
        for (Iterator iter = fclBlCorrectionsList.iterator(); iter.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iter.next();
            if (fclBlCorrections.getStatus() != null && fclBlCorrections.getStatus().equalsIgnoreCase("Approved")) {
                //flag =String.valueOf(fclBlCorrections.getId());
                flag = fclBlCorrections.getBlNumber();
                break;
            }

        }
        for (Iterator iterator = fclBlCorrectionsList.iterator(); iterator.hasNext();) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) iterator.next();
            searchMap.put(fclBlCorrections.getBlNumber() + "-" + fclBlCorrections.getNoticeNo(), fclBlCorrections);
        }
        Set searchResultSet = searchMap.keySet();
        Iterator iter = searchResultSet.iterator();
        List resultList = new ArrayList();
        while (iter.hasNext()) {
            fclBlCorrectionsIntance = new FclBlCorrections();
            fclBlCorrectionsIntance = (FclBlCorrections) searchMap.get(iter.next());
            resultList.add(fclBlCorrectionsIntance);
        }
        List voidedCorrectionsList = correctionsDAO.getCorrectionToDisplayOnPopup(bolId, false);
        request.setAttribute(FclBlConstants.BLVOIDEDCORRECTIONLIST, voidedCorrectionsList);
        request.setAttribute(FclBlConstants.BLCORRECTIONLIST, resultList);
        return flag;
    }

    public void updateChargesFromCorrectionToFclBlCharges(List<FclBlCharges> fclBlChargesList, FclBlCorrections fclBlCorrections) throws Exception {
        boolean chargeFlag = false;
        // Updating charge code to FclBl object if it exist....
        FclBlCharges fclBlChargesObjectForNewCorrection = new FclBlCharges();
        for (int j = 0; j < fclBlChargesList.size(); j++) {
            FclBlCharges fclBlChargesObject = fclBlChargesList.get(j);
            PropertyUtils.copyProperties(fclBlChargesObjectForNewCorrection, fclBlChargesObject);
            fclBlChargesObjectForNewCorrection.setBolId(null);
            fclBlChargesObjectForNewCorrection.setChargesId(null);
            String chargeCode = fclBlCorrections.getChargeCode();
            String chargeId = "" + fclBlCorrections.getFclBlChargeId();
            String blChargeId = "" + fclBlChargesObject.getTempChargeId();
            String blChargeCode = fclBlChargesObject.getChargeCode();

            if (blChargeCode != null && chargeCode != null
                    && (blChargeCode.trim().equals(chargeCode.trim()) && blChargeId.equals(chargeId)
                    || (chargeCode.equalsIgnoreCase(FclBlConstants.INTRAMP) && (blChargeCode.equalsIgnoreCase(FclBlConstants.INTRA) || blChargeCode.equalsIgnoreCase(FclBlConstants.INTRAMP))))) {
                if (null != fclBlChargesObject.getAmount() && null != fclBlCorrections.getDiffereceAmount()) {
                    fclBlChargesObject.setAmount(fclBlChargesObject.getAmount() + fclBlCorrections.getDiffereceAmount());
                }
                fclBlChargesObject.setOldAmount(fclBlChargesObject.getAmount());
                fclBlChargesObject.setBillTrdPrty(fclBlCorrections.getBillToParty());
                if (null != fclBlCorrections.getCorrectionType() && "Y".equalsIgnoreCase(fclBlCorrections.getCorrectionType().getCode())) {
                    fclBlChargesObject.setBillTo(fclBlCorrections.getBillToParty());
                }
                fclBlChargesList.set(j, fclBlChargesObject);
                chargeFlag = true;
            }
        }
        if (!chargeFlag && CommonFunctions.isNotNull(fclBlCorrections.getChargeCode())) {
            fclBlChargesObjectForNewCorrection = new FclBlCharges();
            fclBlChargesObjectForNewCorrection.setAmount(fclBlCorrections.getNewAmount());
            fclBlChargesObjectForNewCorrection.setOldAmount(fclBlCorrections.getNewAmount());
            fclBlChargesObjectForNewCorrection.setChargeCode(fclBlCorrections.getChargeCode());
            fclBlChargesObjectForNewCorrection.setCharges(fclBlCorrections.getChargeCodeDescription());
            fclBlChargesObjectForNewCorrection.setBillTrdPrty(fclBlCorrections.getBillToParty());
            fclBlChargesObjectForNewCorrection.setBillTo(fclBlCorrections.getBillToParty());
            fclBlChargesObjectForNewCorrection.setReadOnlyFlag(null);
            fclBlChargesObjectForNewCorrection.setPrintOnBl("Yes");
            //adding new  charge code to FclBl object if it is not exist....
            fclBlChargesList.add(fclBlChargesObjectForNewCorrection);
        }

    }

    public String getCorrection(String blnumber, String noticeNumber) throws Exception {
        FclBl fclbl = null;
        List listofBl = correctionsDAO.getFclBlCorrectionForTheBLNumbertoDisplay(blnumber);
        String notice = null;
        FclBl fclBl = blDAO.findById(blnumber);
        if (null != fclBl && null != fclBl.getFileNo()) {
            fclbl = blDAO.getOriginalBl(fclBl.getFileNo());
        }
        Integer correctionCount = 1;
        if (null != fclbl && null != fclbl.getCorrectionCount()) {
            correctionCount = fclbl.getCorrectionCount() + correctionCount;
        }
        if (null != fclbl && null != fclbl.getBolId()) {
            blDAO.saveCorrectionCount(correctionCount, fclbl.getBolId());
        }
        for (int i = 0; i < listofBl.size(); i++) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) listofBl.get(i);
            if (noticeNumber != null) {
                int noticenum = new Integer(noticeNumber);
                int notice_no = new Integer(fclBlCorrections.getNoticeNo());
                if (notice_no < noticenum && (fclBlCorrections.getStatus() == null
                        || fclBlCorrections.getStatus().equals(""))) {
                    notice = fclBlCorrections.getNoticeNo();
                }
            }
        }
        return notice;
    }

    public String getLatesNoticeNumber(String blnumber, String noticeNumber) throws Exception {
        String notice = null;
        List listofBl = correctionsDAO.getFclBlCorrectionForTheBLNumbertoDisplay(blnumber);
        for (int i = 0; i < listofBl.size(); i++) {
            FclBlCorrections fclBlCorrections = (FclBlCorrections) listofBl.get(i);
            if (noticeNumber != null) {
                int noticenum = new Integer(noticeNumber);
                int notice_no = new Integer(fclBlCorrections.getNoticeNo());
                if (notice_no > noticenum
                        && CommonFunctions.isNotNull(fclBlCorrections.getManifest())) {
                    notice = fclBlCorrections.getNoticeNo();
                    break;
                }
                if (fclBlCorrections.getManifest() == null
                        && (fclBlCorrections.getStatus() == null || !fclBlCorrections.getStatus().equalsIgnoreCase(
                        FclBlConstants.DISABLE))) {
                    notice = "post";
                }
            }
        }
        return notice;
    }

    public String approvedExistingCorrectionBeforeCreateNew(String blNumber, String screen) throws Exception {
        return correctionsDAO.getLatestUnPostedNotice(blNumber, screen);
    }

    public List listToDisplayOnSearchPage(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        List searchList = getAllCorrections(fclBlCorrectionsForm);
        HashMap searchMap = new HashMap();
        List resultList = new ArrayList();
        for (Iterator iter = searchList.iterator(); iter.hasNext();) {
            fclBlCorrectionsIntance = (FclBlCorrections) iter.next();
            if (searchMap.get(fclBlCorrectionsIntance.getBlNumber()) == null) {
                searchMap.put(fclBlCorrectionsIntance.getBlNumber() + "-" + fclBlCorrectionsIntance.getNoticeNo(), fclBlCorrectionsIntance);
            }
        }
        Set searchResultSet = searchMap.keySet();
        Iterator iter = searchResultSet.iterator();
        while (iter.hasNext()) {
            fclBlCorrectionsIntance = new FclBlCorrections();
            fclBlCorrectionsIntance = (FclBlCorrections) searchMap.get(iter.next());
            resultList.add(fclBlCorrectionsIntance);

        }
        return resultList;
    }

    public FclBl getLatestBlObject(Object object) throws Exception {
        String blNumber = null;

        if (object instanceof FclBlCorrectionsForm) {
            FclBlCorrectionsForm fclBlCorrectionsForm = (FclBlCorrectionsForm) object;
            blNumber = fclBlCorrectionsForm.getBlNumber();
        } else if (object instanceof String) {
            blNumber = (String) object;
        }
        FclBl fclBl = null;
        Integer noticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
        /*getNoticeNumberForPostedCorrection will fetch the latest notice number that got approved, based on that getting latest
         * bl from fcl_bl and setting correction type if its null then it orignal bl will get fetch */
        if (null != noticeNumber) {
            String blNO = blNumber + FclBlConstants.DELIMITER + noticeNumber;
            fclBl = blDAO.findById(blNO);
        } else {
            fclBl = blDAO.findById(blNumber);
        }
        return fclBl;
    }

    public boolean checkBillToParty(String blNumber, String billTo) throws Exception {
        boolean billToParty = false;
        FclBl fclBl = null;
        Integer noticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
        if (null != noticeNumber) {
            String blNO = blNumber + FclBlConstants.DELIMITER + noticeNumber;
            fclBl = blDAO.findById(blNO);
        } else {
            fclBl = blDAO.findById(blNumber);
        }
        if (billTo.equalsIgnoreCase("Forwarder") && CommonFunctions.isNotNull(fclBl.getForwardingAgentName())
                && !fclBl.getForwardingAgentName().contains("NO FF ASSIGNED")) {
            billToParty = true;
        } else if (billTo.equalsIgnoreCase("Shipper") && CommonFunctions.isNotNull(fclBl.getShipperName())) {
            billToParty = true;
        } else if (billTo.equalsIgnoreCase("ThirdParty") && CommonFunctions.isNotNull(fclBl.getThirdPartyName())) {
            billToParty = true;
        } else if (billTo.equalsIgnoreCase("Agent") && CommonFunctions.isNotNull(fclBl.getAgent())) {
            billToParty = true;
        } else if (billTo.equalsIgnoreCase(FclBlConstants.CONSIGNEE) && CommonFunctions.isNotNull(fclBl.getConsigneeNo())) {
            billToParty = true;
        }
        return billToParty;
    }

    /**
     * @param request
     * @param fclBlCorrectionsForm
     * @param messageResources setting costType according to HouseBl Preparid
     * condition
     */
    public void setRequestObject(HttpServletRequest request, FclBlCorrectionsForm fclBlCorrectionsForm,
            MessageResources messageResources) throws Exception {
        class CostTypeForForwarder {
            // if not prepared forwarder. means P redio button selected but forwarder is empty or
            // No FF  Forwarder
            // if not prepared Shipper . means P redio button selected but Shipper is empty

            public String getPreparedValue(String forwarder, String forwarderForCollect, MessageResources messageResources) throws Exception {
                String returnValue = null;
                if (forwarder != null && !forwarder.trim().equals(messageResources.getMessage("notForwarder"))
                        && !forwarder.trim().equals(messageResources.getMessage("notForwarderSecond"))) {
                    returnValue = messageResources.getMessage("notPrepaidForwarder");
                } else if (forwarderForCollect != null
                        && !forwarderForCollect.trim().equals(messageResources.getMessage("notForwarder"))
                        && !forwarderForCollect.trim().equals(messageResources.getMessage("notForwarderSecond"))) {
                    returnValue = messageResources.getMessage("notColltecForwarder");
                }
                return returnValue;
            }
        }
        dbUtil = new DBUtil();
        //---------------------------------------------------------------------------------------
        FclBl fclBl = getLatestBlObject(fclBlCorrectionsForm);
        //---------------------------------------------------------------------------------------
        if (null != fclBl) {
            CostTypeForForwarder costTypeForForwarder = new CostTypeForForwarder();
            String houseBl = fclBl.getHouseBl() != null ? fclBl.getHouseBl() : "";
            String billToCode = fclBl.getBillToCode() != null ? fclBl.getBillToCode() : "";
            List<LabelValueBean> selectList = new ArrayList<LabelValueBean>();
            List<GenericCode> correctionTypeList = Collections.EMPTY_LIST;
            String descptionValue = null;
            if (houseBl.equals(messageResources.getMessage("prepaidValue"))
                    && billToCode.equals(messageResources.getMessage("shipperValue"))) {
                descptionValue = costTypeForForwarder.getPreparedValue(
                        fclBl.getForwardAgentNo(), null, messageResources);
                if (descptionValue != null) {
                    descptionValue += "," + messageResources.getMessage("prepaidShipper");
                } else {
                    descptionValue = messageResources.getMessage("prepaidShipper");
                }
                correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, descptionValue, fclBl);
            } else if (houseBl.equals(messageResources.getMessage("prepaidValue"))
                    && billToCode.equals(messageResources.getMessage("thirdPartyValue"))) {
                descptionValue = messageResources.getMessage("prepaidThirdParty");
                correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, descptionValue, fclBl);
            } else if (houseBl.equals(messageResources.getMessage("prepaidValue"))
                    && billToCode.equals(messageResources.getMessage("forwarderValue"))) {
                descptionValue = messageResources.getMessage("prepaidForwarder");
                correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, descptionValue, fclBl);
            } else if (houseBl.equals(messageResources.getMessage("collectValue"))) {
                descptionValue = costTypeForForwarder.getPreparedValue(null, fclBl.getForwardAgentNo(), messageResources);
                if (descptionValue != null) {
                    descptionValue += "," + messageResources.getMessage("collect");
                } else {
                    descptionValue = messageResources.getMessage("collect");
                }
                correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, descptionValue, fclBl);
            } else if (houseBl.equals(messageResources.getMessage("bothCollectAndPrepaid"))) {
                descptionValue = messageResources.getMessage("allParties");
                correctionTypeList = genericCodeDAO.findByCodeNameByInOpreator(51, descptionValue, fclBl);
            }
            selectList.add(new LabelValueBean("Select Correction Type", "0"));
            for (GenericCode genericCode : correctionTypeList) {
                selectList.add(new LabelValueBean(genericCode.getCode() + "-" + genericCode.getCodedesc(), genericCode.getId().toString()));
            }
            request.setAttribute("correctionTypeList", selectList);
            request.setAttribute(FclBlConstants.CUSTOMER_LIST, fclBlBC.getListOfPartyDetails(fclBl.getBolId().toString()));
            //  request.setAttribute(FclBlConstants.SENDCREDIT_LIST, fclBlBC.getsendCreditMemoList(fclBl.getBol().toString()));
            request.setAttribute(FclBlConstants.CODEC_CUSTOMER_LIST, fclBlBC.getCodeCContactList(fclBl.getBolId().toString()));
        } else {
            request.setAttribute("correctionTypeList", dbUtil.getGenericCodeListWithDesc(51, "no", "yes", "Select Correction Type"));
        }

        com.gp.cvst.logisoft.util.DBUtil dButil = new com.gp.cvst.logisoft.util.DBUtil();
        boolean importFlag = null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag());
        request.setAttribute("billToVendor", dButil.getBilltype(importFlag));
        request.setAttribute("correctionCodeList", dbUtil.getGenericCodeListWithDesc(52, "no", "yes", "Select Correction Code"));

    }

    public List getListOfChargesFromFclBl(FclBl fclBl, MessageResources messageResources, HttpServletRequest request,
            String BlNumber) throws Exception {
        List<FclBlCorrections> fclBlCorrectionList = new ArrayList<FclBlCorrections>();
        List<FclBlCharges> CollapsList = getFclBlChargesList(fclBl, messageResources, BlNumber);
        for (FclBlCharges fclBlCharges : CollapsList) {
            FclBlCorrections newFclBlCorrections = new FclBlCorrections();
            newFclBlCorrections.setChargeCode(fclBlCharges.getChargeCode());
            newFclBlCorrections.setChargeCodeDescription(fclBlCharges.getCharges());
            newFclBlCorrections.setAmount(fclBlCharges.getAmount());
            newFclBlCorrections.setBillToParty(fclBlCharges.getBillTo());
            newFclBlCorrections.setFclBlChargeId(fclBlCharges.getChargesId());
            fclBlCorrectionList.add(newFclBlCorrections);
        }

        return getSortedList(fclBlCorrectionList);
    }

    /**
     * @param fclBl
     * @param messageResources
     * @param BlNumber
     * @return getting Collaps Charges List from DB..
     */
    public List getFclBlChargesList(FclBl fclBl, MessageResources messageResources,
            String BlNumber) throws Exception {
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        boolean importFlag = null != fclBl && "I".equalsIgnoreCase(fclBl.getImportFlag());
        List<FclBlCharges> newList = new ArrayList<FclBlCharges>();
        List chargesList = fclBlChargesDAO.findByParentId(fclBl.getBol());
        String temp = "";
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();

        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        return fclBlBC.consolidateRates(newList, messageResources, importFlag);
    }

    public List getFclBlChargesList(FclBl fclBl,
            String BlNumber) throws Exception {
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        List<FclBlCharges> newList = new ArrayList<FclBlCharges>();
        List chargesList = fclBlChargesDAO.findByParentId(fclBl.getBol());
        String temp = "";
        int j = 0;
        int k = 0;
        boolean importFlag = "I".equalsIgnoreCase(fclBl.getImportFlag());
        LinkedList linkedList = new LinkedList();

        for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
            FclBlCharges charges = (FclBlCharges) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        return new FclBlUtil().consolidateRates(newList, importFlag);
    }

    /**
     * @param fclBlCorrectionsForm posting Bl corrections
     */
    public String getGenericeCode(FclBlCorrectionsForm fclBlCorrectionsForm) throws Exception {
        String returnString = null;
        GenericCode genericCodeObject = null;
        if (fclBlCorrectionsForm.getCorrectionType() != null && !fclBlCorrectionsForm.getCorrectionType().equals("")) {
            genericCodeObject = genericCodeDAO.findById(Integer.parseInt(fclBlCorrectionsForm.getCorrectionType()));
            String type = genericCodeObject.getCode();
            Character code = type.charAt(0);
            returnString = code.toString();
        }
        return returnString;
    }

    public String getLatesCorrectionNumber(Integer number, String blNumber) throws Exception {
        String notice = null;
        for (; number > 1; number--) {
            number--;
            notice = number.toString() + FclBlConstants.CNS;
            List<TransactionLedger> updateLedgerList2 = transactionLedgerDAO.getListOfTaransactionToDelete(blNumber, notice);
            if (CommonFunctions.isNotNullOrNotEmpty(updateLedgerList2)) {
                break;
            }
        }
        return notice;
    }

    public void sendCorrectionEmail(FclBlCorrections correction, String toAddress, String subject, String fileName) throws Exception {
        User user = userDAO.getUserInfo(correction.getUserName());
        EmailSchedulerVO email = new EmailSchedulerVO();
        email.setFromName(user.getLoginName());
        email.setFromAddress(user.getEmail());
        email.setToName(null);
        if (CommonUtils.isNotEmpty(toAddress)) {
            email.setToAddress(toAddress);
        } else {
            email.setToAddress(user.getEmail());
        }
        if (CommonUtils.isNotEmpty(correction.getSendCopyTo())) {
            email.setCcAddress(correction.getSendCopyTo().replaceAll("(\r\n|\r|\n|\n\r)", ""));
        }
        email.setModuleName(CommonConstants.SCREEN_NAME_BL);
        email.setName(CommonConstants.SCREEN_NAME_BL);
        email.setType(CommonConstants.CONTACT_MODE_EMAIL);
        email.setCoverLetter("");
        email.setFileLocation(fileName);
        email.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
        email.setModuleId(correction.getFileNo());
        email.setHtmlMessage("");
        email.setSubject(subject);
        email.setUserName(user.getLoginName());
        email.setEmailDate(new Date());
        new EmailschedulerDAO().save(email);
    }

    private void saveCreditDebitNotes(FclBlCorrections correction, String customerName, String customerNumber, String noteType) throws Exception {
        CreditDebitNoteDAO creditDebitNoteDAO = new CreditDebitNoteDAO();
        CreditDebitNote creditDebitNote = new CreditDebitNote();
        creditDebitNote.setBolid(correction.getBlNumber());
        creditDebitNote.setCorrectionNumber(correction.getNoticeNo());
        creditDebitNote.setCustomerName(customerName);
        creditDebitNote.setDebitCreditNote(noteType);
        creditDebitNote.setCustomerNumber(customerNumber);
        creditDebitNoteDAO.save(creditDebitNote);
    }

    private void sendCreditDebitOrFreightInvoice(FclBl correctedBl, FclBlCorrections correction, boolean quickCn) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        PrintConfigBC printConfigBC = new PrintConfigBC();
        List<Object> list = correctionsDAO.getCreditDebitNoteType(correction.getCorrectionType().getCode(), correction.getNewBolIdForApprovedBl(), correction.getNoticeNo());
        StringBuilder toAddress = new StringBuilder();
        PrintUtil printUtil = new PrintUtil();
        for (Object row : list) {
            Object[] col = (Object[]) row;
            String blId = correctedBl.getBolId();
            String fileNo = correctedBl.getFileNo();
            String customerNumber = (String) col[0];
            String customerName = (String) col[1];
            boolean creditDebit = (Boolean) col[2];
            String noteType = (String) col[3];
            saveCreditDebitNotes(correction, customerName, customerNumber, noteType);
            toAddress.delete(0, toAddress.length());
            if (!quickCn) {
                if (creditDebit) {
                    String document = customerName.replace("'", "") + PrintReportsConstants.DELIMETER + customerNumber + ")" + PrintReportsConstants.DELIMETER + noteType.toUpperCase() + ")";
                    String reportLocation = LoadLogisoftProperties.getProperty("reportLocation");
                    String fileName = printUtil.debitCreditNotes(reportLocation, correction.getBlNumber(), correction.getNoticeNo(), correction.getFileNo(), document, request);
                    String subject = "FCL-04-" + fileNo + "-" + noteType + " For " + customerName + "(" + customerNumber + ")";
                    if (FclBlConstants.CREDTINOTE.equalsIgnoreCase(noteType) && CommonUtils.isNotEmpty(correction.getCreditMemoEmail())) {
                        toAddress.append(correction.getCreditMemoEmail().replaceAll("(\r\n|\r|\n|\n\r)", ""));
                    } else if (FclBlConstants.DEBITNOTE.equalsIgnoreCase(noteType) && CommonUtils.isNotEmpty(correction.getDebitMemoEmail())) {
                        toAddress.append(correction.getDebitMemoEmail().replaceAll("(\r\n|\r|\n|\n\r)", ""));
                    }
                    sendCorrectionEmail(correction, toAddress.toString(), subject, fileName);
                } else {
                    String[] documents = new String[2];
                    if (customerNumber != null) {
                        if (customerNumber.equalsIgnoreCase(correctedBl.getAgentNo())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT;
                        } else if (customerNumber.equalsIgnoreCase(correctedBl.getBillTrdPrty())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY;
                        } else if (customerNumber.equalsIgnoreCase(correctedBl.getForwardAgentNo())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER;
                        } else if (customerNumber.equalsIgnoreCase(correctedBl.getShipperNo())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER;
                        } else if (customerNumber.equalsIgnoreCase(correctedBl.getConsigneeNo())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE;
                        } else if (customerNumber.equalsIgnoreCase(correctedBl.getNotifyParty())) {
                            documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY;
                        }
                    } else {
                        documents[0] = "";
                    }
                    List<PrintConfig> printList = (List) printConfigBC.findPrintConfigByScreenNameFroImport("BL", documents, user.getUserId());
                    if (CommonUtils.isNotEmpty(printList)) {
                        for (PrintConfig printConfig : printList) {
                            String fileName = null;
                            String toParty = null;
                            String toPartyNo = null;
                            if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_AGENT, request);
                                toParty = CommonConstants.AGENT;
                                toPartyNo = correctedBl.getAgentNo();
                            } else if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_SHIPPER, request);
                                toParty = CommonConstants.SHIPPER;
                                toPartyNo = correctedBl.getShipperNo();
                            } else if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_FORWARDER, request);
                                toParty = CommonConstants.FORWARDER;
                                toPartyNo = correctedBl.getForwardAgentNo();
                            } else if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_THIRDPARTY, request);
                                toParty = CommonConstants.THIRDPARTY;
                                toPartyNo = correctedBl.getBillTrdPrty();
                            } else if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_CONSIGNEE, request);
                                toParty = FclBlConstants.CONSIGNEE;
                                toPartyNo = correctedBl.getConsigneeNo();
                            } else if (CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY.equals(printConfig.getDocumentName())) {
                                fileName = createCorrectFreightInvoice(blId, fileNo, PrintReportsConstants.FREIGHT_INVOICE_NOTIFYPARTY, request);
                                toParty = "Notify Party";
                                toPartyNo = correctedBl.getNotifyParty();
                            }
                            if (CommonUtils.isAllNotEmpty(fileName, toPartyNo)) {
                                String subject = "FCL-04-" + fileNo + "-" + " FreightInvoice(" + toParty + ")";
                                if (CommonUtils.isNotEmpty(correction.getCreditMemoEmail())) {
                                    toAddress.append(correction.getCreditMemoEmail().replaceAll("(\r\n|\r|\n|\n\r)", "")).append(";");
                                }
                                if (CommonUtils.isNotEmpty(correction.getDebitMemoEmail())) {
                                    toAddress.append(correction.getDebitMemoEmail().replaceAll("(\r\n|\r|\n|\n\r)", ""));
                                }
                                sendCorrectionEmail(correction, toAddress.toString(), subject, fileName);
                            }
                        }
                    }
                }
            }
        }
    }

    public String manifestCorrections(String blNumber, String noticeNumber, boolean quickCn, HttpServletRequest request) throws Exception {
        this.request = request;
        blDAO.getCurrentSession().flush();
        List<FclBlCorrections> corrections = correctionsDAO.getCorrections(blNumber, noticeNumber);
        if (CommonUtils.isNotEmpty(corrections)) {
            User user = (User) request.getSession().getAttribute("loginuser");
            FclBl originalBl = blDAO.findById(blNumber);
            FclBl correctedBl = blDAO.getFileNoObject(originalBl.getFileNo());
            boolean isFirst = true;
            Map<Serializable, Serializable> fields = new HashMap<Serializable, Serializable>();
            String postedDate = DateUtils.formatDate(new Date(), "yyyy-MM-dd kk:mm:ss");
            for (FclBlCorrections correction : corrections) {
                if (isFirst) {
                    originalBl.setCorrectionNumber(correction.getNoticeNo());
                    new FclManifestDAO(correctedBl, user).postCorrection();
                    sendCreditDebitOrFreightInvoice(correctedBl, correction, quickCn);
                    isFirst = false;
                }
                fields.clear();
                fields.put("manifest", "M");
                fields.put("posted_date", postedDate);
                fields.put("who_posted", user.getLoginName());
                fields.put("status", "Approved");
                fields.put("Approval", user.getLoginName());
//                correction.setManifest("M");
//                correction.setPostedDate(new Date());
//                correction.setWhoPosted(user.getLoginName());
//                correction.setStatus("Approved");
//                correction.setApproval(user.getLoginName());
                correctionsDAO.updateCorrection(correction.getId(), fields);
            }
            blDAO.getCurrentSession().flush();
            return "manifested";
        } else {
            return null;
        }
    }

    public void manifestingForCNTypeB(FclBl bl, FclBlCorrections fclBlCorrections, HttpServletRequest request) throws Exception {
        transactionLedgerDAO = new TransactionLedgerDAO();
        manifestBc = new ManifestBC();
        //List<FclBlCorrections> li=fclBlCorrectionsDAO.getRevrseAccruals(fclBl.getBolId());
        manifestBc.getADVFFAccrualList(bl, null, FclBlConstants.ADVANCEFFCODE);
        manifestBc.getADVFFAccrualList(bl, null, FclBlConstants.ADVANCESHIPPERCODE);
        manifestBc.getFFCOMMAccrualList(bl, fclBlCorrections, FclBlConstants.FFCODE);
        HttpSession session = request.getSession();
        User user = null;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
        }

//        if (CommonFunctions.isNotNullOrNotEmpty(li)) {
//            for(FclBlCorrections fclBlCorrectionsNew:li ){
//               manifestBc.getADVFFAccrualList(fclBl,null,fclBlCorrectionsNew.getChargeCode());
//            }
//        }
        List<TransactionLedger> transactionLadgerList = transactionLedgerDAO.getAllTransactionLadgerByPassingBillNumber(bl.getBolId(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        int i = 0;
        String customer = "";
        Date postedDate = new AccrualsDAO().getPostedDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
        Map<String, String> map = new HashMap<String, String>();
        for (TransactionLedger transactionLedger : transactionLadgerList) {
            TransactionLedger newTransactionLedger = new TransactionLedger();
            TransactionLedger nTransactionLedger = new TransactionLedger();
            //for existing customer creating negiative values
            PropertyUtils.copyProperties(nTransactionLedger, transactionLedger);
            double transactionAmount = (transactionLedger.getTransactionAmt() != null) ? transactionLedger.getTransactionAmt() : 0.0d;
            double amount = (transactionAmount > 0) ? 0 - transactionAmount : Math.abs(transactionAmount);
            nTransactionLedger.setTransactionAmt(amount);
            nTransactionLedger.setBalance(amount);
            nTransactionLedger.setBalanceInProcess(amount);
            nTransactionLedger.setManifestFlag("N");
            transactionLedger.setManifestFlag("N");
            nTransactionLedger.setTransactionId(null);
            nTransactionLedger.setJournalEntryNumber(null);
            nTransactionLedger.setLineItemNumber(null);
            nTransactionLedger.setPostedToGlDate(null);
            nTransactionLedger.setStatus(CommonConstants.STATUS_OPEN);
            nTransactionLedger.setPostedDate(postedDate);
            nTransactionLedger.setCorrectionNotice(fclBlCorrections.getNoticeNo() + FclBlConstants.CNS);
            nTransactionLedger.setNoticeNumber(fclBlCorrections.getNoticeNo() + FclBlConstants.CNS);
            //nTransactionLedger.setSubledgerSourceCode(FclBlConstants.SUBLADGER);
            map.put(nTransactionLedger.getCustNo(), nTransactionLedger.getCustName());
            transactionLedgerDAO.save(nTransactionLedger);

            //for new customer assiging values
            if (null != transactionLedger.getChargeCode() && (!transactionLedger.getChargeCode().equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)
                    && !transactionLedger.getChargeCode().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE) && !transactionLedger.getChargeCode().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE))) {
                PropertyUtils.copyProperties(newTransactionLedger, transactionLedger);
                newTransactionLedger.setCustName(fclBlCorrections.getAccountName());
                newTransactionLedger.setCustNo(fclBlCorrections.getAccountNumber());
                newTransactionLedger.setManifestFlag("Y");
                newTransactionLedger.setCustomerReferenceNo(bl.getCorrectionNumber());
                newTransactionLedger.setCorrectionNotice(fclBlCorrections.getNoticeNo() + FclBlConstants.CNA);
                newTransactionLedger.setNoticeNumber(fclBlCorrections.getNoticeNo() + FclBlConstants.CNA);
                newTransactionLedger.setTransactionId(null);
                newTransactionLedger.setJournalEntryNumber(null);
                newTransactionLedger.setLineItemNumber(null);
                newTransactionLedger.setPostedToGlDate(null);
                newTransactionLedger.setStatus(CommonConstants.STATUS_OPEN);
                newTransactionLedger.setPostedDate(postedDate);
                newTransactionLedger.setSubledgerSourceCode(FclBlConstants.SUBLADGER);
                if (null != fclBlCorrections.getCorrectionType() && ("C".equals(fclBlCorrections.getCorrectionType().getCode()) || "D".equals(fclBlCorrections.getCorrectionType().getCode()) || "Q".equals(fclBlCorrections.getCorrectionType().getCode()) || "R".equals(fclBlCorrections.getCorrectionType().getCode()) || "S".equals(fclBlCorrections.getCorrectionType().getCode())) && "CAF".equalsIgnoreCase(transactionLedger.getChargeCode())) {
                } else {
                    transactionLedgerDAO.save(newTransactionLedger);
                }
            }
        }
        if (!map.isEmpty()) {
            Set<Map.Entry<String, String>> enterySet = map.entrySet();
            for (Iterator iterator = enterySet.iterator(); iterator.hasNext();) {
                Map.Entry entryMap = (Map.Entry) iterator.next();
                saveCreditDebitNotes(fclBlCorrections, (String) entryMap.getValue(), (String) entryMap.getKey(), FclBlConstants.CREDTINOTE);
            }
            saveCreditDebitNotes(fclBlCorrections, fclBlCorrections.getAccountName(), fclBlCorrections.getAccountNumber(), FclBlConstants.DEBITNOTE);
        }
        manifestBc.updateTransactionAmount(bl.getBolId(), bl.getFileNo(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, user.getLoginName(), true, true);
        fclBlCorrections.setRevrseAccruals("Done");
    }

    public String createCorrectFreightInvoice(String blId, String fileNo, String documentName, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        MessageResources messageResources = CommonConstants.loadMessageResources();
        User user = (User) session.getAttribute("loginuser");
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/" + ReportConstants.BILLOFLADINGFILENAME + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String contextPath = HibernateSessionRequestFilter.servletContext.getRealPath("/");
        outputFileName = outputFileName + documentName + "_" + fileNo + ".pdf";
        fclBlBC.createFclBillLadingReport(blId, outputFileName, contextPath, messageResources, user, documentName);
        return outputFileName;
    }

    public void deleteAllCorrectionAfterUnmanifest(String blNumber) throws Exception {
        List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getFclBlCorrectionForTheBLNumbertoDisplay(blNumber);
        for (FclBlCorrections fclBlCorrections : fclBlCorrectionsList) {
            correctionsDAO.delete(fclBlCorrections);
        }
    }

    public Double comparingLatestAmountWithNewAmount(String blNumber, String chargeCode) throws Exception {
        return correctionsDAO.getLatestAmountOfFclBlCorrections(blNumber, chargeCode);
    }

    public String getAddress(String accoutNo) throws Exception {
        StringBuilder address = new StringBuilder();
        if (null != accoutNo && !"".equalsIgnoreCase(accoutNo)) {
            CustAddressDAO customerDAO = new CustAddressDAO();
            CustAddress custAddress = (CustAddress) customerDAO.getUniqueAddress(accoutNo);
            if (null != custAddress) {
                address.append((null != custAddress.getCoName() && !custAddress.getCoName().equals("")) ? custAddress.getCoName() : "");
                address.append("\n");
                address.append((null != custAddress.getAddress1() && !custAddress.getAddress1().equals("")) ? custAddress.getAddress1() : "");
                address.append("\n");
                address.append((null != custAddress.getCity1() && !custAddress.getCity1().equals("")) ? custAddress.getCity1() + "," : "");
                address.append((null != custAddress.getState() && !custAddress.getState().equals("")) ? custAddress.getState() + "," : "");
                address.append((null != custAddress.getZip() && !custAddress.getZip().equals("")) ? custAddress.getZip() : "");
                address.append(".");
            }
        }
        return address.toString();
    }

    public void getMemoEmailIds(String blNumber, String noticeNo, String cnType,
            String newAcctNo, String differenceAmt, String viewMode,
            HttpServletRequest request) throws Exception {
        // declare variables
        String acctNo = "";
        String unApprovedAcctNo = "";
        String arEmail = "";
        String creditArEmail = "";
        String debitArEmail = "";
        Set<String> arCreditEmails = new TreeSet<String>();
        Set<String> arDebitEmails = new TreeSet<String>();
        List<String> debitEmailList = new ArrayList<String>();
        List<String> creditEmailList = new ArrayList<String>();
        Set<String> creditEmails = new TreeSet<String>();
        Set<String> debitEmails = new TreeSet<String>();
        Set<String> creditContacts = new TreeSet<String>();
        Set<String> debitContacts = new TreeSet<String>();
        FclBl fclBl = blDAO.findById(blNumber);
        if ("view".equalsIgnoreCase(viewMode)) {
            List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, noticeNo);
            for (FclBlCorrections fclBlCorrection : fclBlCorrectionsList) {
                if (null != fclBlCorrection.getCreditMemoEmail()) {
                    creditEmailList.addAll(Arrays.asList((fclBlCorrection.getCreditMemoEmail().toLowerCase()).split(",")));
                }
                if (null != fclBlCorrection.getDebitMemoEmail()) {
                    debitEmailList.addAll(Arrays.asList((fclBlCorrection.getDebitMemoEmail().toLowerCase()).split(",")));
                }
            }
            creditEmails = new TreeSet<String>(creditEmailList);
            debitEmails = new TreeSet<String>(debitEmailList);
        } else {
            // Assign account number based on Bill to code
            if (CommonUtils.isNotEmpty(fclBl.getBillToCode()) && CommonUtils.isEmpty(acctNo)) {
                if ("F".equalsIgnoreCase(fclBl.getBillToCode())) {
                    acctNo = fclBl.getForwardAgentNo();
                } else if ("S".equalsIgnoreCase(fclBl.getBillToCode())) {
                    acctNo = fclBl.getShipperNo();
                } else if ("T".equalsIgnoreCase(fclBl.getBillToCode())) {
                    acctNo = fclBl.getConsigneeNo();
                } else if ("A".equalsIgnoreCase(fclBl.getBillToCode())) {
                    acctNo = fclBl.getAgentNo();
                }
            }
            // Get previous corrections if notice number not empty
            if (CommonUtils.isNotEmpty(noticeNo)) {
                Integer postedCnNoticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
                if (CommonUtils.isNotEmpty(postedCnNoticeNumber)) {
                    List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, postedCnNoticeNumber.toString());
                    if (CommonUtils.isNotEmpty(fclBlCorrectionsList)) {
                        FclBlCorrections fclBlCorrection = fclBlCorrectionsList.get(0);
                        acctNo = fclBlCorrection.getAccountNumber();
                    }
                }
                List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, noticeNo);
                for (FclBlCorrections fclBlCorrection : fclBlCorrectionsList) {
                    if (null != fclBlCorrection.getCreditMemoEmail()) {
                        creditEmailList.addAll(Arrays.asList((fclBlCorrection.getCreditMemoEmail().toLowerCase()).split(",")));
                    }
                    if (null != fclBlCorrection.getDebitMemoEmail()) {
                        debitEmailList.addAll(Arrays.asList((fclBlCorrection.getDebitMemoEmail().toLowerCase()).split(",")));
                    }
                    unApprovedAcctNo = fclBlCorrection.getAccountNumber();
                    if ("A".equalsIgnoreCase(fclBlCorrection.getCorrectionType().getCode())) {
                        acctNo = unApprovedAcctNo;
                    }
                }
                creditArEmail = getArEmail(acctNo);
                debitArEmail = getArEmail(unApprovedAcctNo);
                creditContacts.addAll(getEmailIdSet(acctNo));
                debitContacts.addAll(getEmailIdSet(unApprovedAcctNo));
                creditEmails = new TreeSet<String>(creditEmailList);
                debitEmails = new TreeSet<String>(debitEmailList);
                if (CommonUtils.isNotEmpty(creditEmails)) {
                    creditContacts.removeAll(creditEmails);
                    if (CommonUtils.isNotEmpty(creditArEmail)) {
                        creditContacts.remove(creditArEmail.toLowerCase());
                        creditEmails.remove(creditArEmail.toLowerCase());
                        arCreditEmails.add(creditArEmail);
                    }
                }
                if (CommonUtils.isNotEmpty(debitEmails)) {
                    debitContacts.removeAll(debitEmails);
                    if (CommonUtils.isNotEmpty(debitArEmail)) {
                        debitContacts.remove(debitArEmail.toLowerCase());
                        debitEmails.remove(debitArEmail.toLowerCase());
                        arDebitEmails.add(debitArEmail);
                    }
                }
            }

            // Operations based on correction type
            if (CommonUtils.isNotEmpty(cnType)) {
                if ("A".equalsIgnoreCase(cnType)) { // correction type A
                    // get account number from recent posted correction
                    Integer postedCnNoticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
                    if (CommonUtils.isNotEmpty(postedCnNoticeNumber)) {
                        List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, postedCnNoticeNumber.toString());
                        if (CommonUtils.isNotEmpty(fclBlCorrectionsList)) {
                            FclBlCorrections fclBlCorrection = fclBlCorrectionsList.get(0);
                            acctNo = fclBlCorrection.getAccountNumber();
                        }
                    }
                    // based on difference amount add into credit/debit memo Set
                    if (CommonUtils.isNotEmpty(differenceAmt)) {
                        arEmail = getArEmail(acctNo);
                        if (Double.parseDouble(differenceAmt) > 0d) {
                            debitEmails.addAll(getEmailIdSet(acctNo));
                            if (null != arEmail) {
                                arDebitEmails.add(arEmail);
                                debitEmails.remove(arEmail);
                            }
                        } else {
                            creditEmails.addAll(getEmailIdSet(acctNo));
                            if (null != arEmail) {
                                arCreditEmails.add(arEmail);
                                creditEmails.remove(arEmail);
                            }
                        }
                    }
                } else if ("C".equalsIgnoreCase(cnType) || "G".equalsIgnoreCase(cnType)
                        || "I".equalsIgnoreCase(cnType)) { // correction type C,G,I
                    Integer postedCnNoticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
                    if (CommonUtils.isNotEmpty(postedCnNoticeNumber)) {
                        List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, postedCnNoticeNumber.toString());
                        if (CommonUtils.isNotEmpty(fclBlCorrectionsList)) {
                            FclBlCorrections fclBlCorrection = fclBlCorrectionsList.get(0);
                            acctNo = fclBlCorrection.getAccountNumber();
                        }
                    }
                    debitArEmail = getArEmail(newAcctNo);
                    creditArEmail = getArEmail(acctNo);
                    debitEmails.addAll(getEmailIdSet(newAcctNo));
                    arDebitEmails.add(debitArEmail);
                    if (null != debitArEmail) {
                        debitEmails.remove(debitArEmail.toLowerCase());
                    }
                    creditEmails.addAll(getEmailIdSet(acctNo));
                    arCreditEmails.add(creditArEmail);
                    if (null != creditArEmail) {
                        creditEmails.remove(creditArEmail.toLowerCase());
                    }
                } else if ("T".equalsIgnoreCase(cnType) || "U".equalsIgnoreCase(cnType)) { // Correction type T, U
                    // For Correction type T and U donot require to show email ID's
                } else {
                    Integer postedCnNoticeNumber = correctionsDAO.getNoticeNumberForPostedCorrection(blNumber);
                    if (CommonUtils.isNotEmpty(postedCnNoticeNumber)) {
                        List<FclBlCorrections> fclBlCorrectionsList = correctionsDAO.getCorrections(blNumber, postedCnNoticeNumber.toString());
                        if (CommonUtils.isNotEmpty(fclBlCorrectionsList)) {
                            FclBlCorrections fclBlCorrection = fclBlCorrectionsList.get(0);
                            acctNo = fclBlCorrection.getAccountNumber();
                        }
                    }
                    debitArEmail = getArEmail(newAcctNo);
                    creditArEmail = getArEmail(acctNo);
                    debitEmails.addAll(getEmailIdSet(newAcctNo));
                    arDebitEmails.add(debitArEmail);
                    if (null != debitArEmail) {
                        debitEmails.remove(debitArEmail);
                    }
                    creditEmails.addAll(getEmailIdSet(acctNo));
                    arCreditEmails.add(creditArEmail);
                    if (null != creditArEmail) {
                        creditEmails.remove(creditArEmail);
                    }
                }
            }
        }
        request.setAttribute("debitEmails", debitEmails);
        request.setAttribute("arDebitEmails", arDebitEmails);
        request.setAttribute("creditEmails", creditEmails);
        request.setAttribute("arCreditEmails", arCreditEmails);
        request.setAttribute("creditContacts", creditContacts);
        request.setAttribute("debitContacts", debitContacts);
    }

    public String getArEmail(String acctNo) throws Exception {
        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByAccountNumber(acctNo);
        return customerAccounting.getAcctRecEmail();
    }

    public Set<String> getEmailIdSet(String acctNo) throws Exception {
        Set<String> emailIdSet = new TreeSet<String>();
        List<CustomerContact> contactsList = fclBlBC.checkForE1andE2OfCodeC(acctNo);
        for (CustomerContact contact : contactsList) {
            if (CommonUtils.isNotEmpty(contact.getEmail())) {
                emailIdSet.add(contact.getEmail().toLowerCase());
            }
        }
        return emailIdSet;
    }

    public String getEmailIds(String acctNo) throws Exception {
        Set<String> emailIdSet = getEmailIdSet(acctNo);
        StringBuilder emailIds = new StringBuilder();
        for (String emailId : emailIdSet) {
            if (!"".equals(emailId)) {
                emailIds.append(emailId).append(",");
            }
        }
        return emailIds.toString();
    }

    public List getSortedList(List tempList) {
        int j = 0;
        int k = 0;
        LinkedList linkedList = new LinkedList();
        List newList = new ArrayList();
        Collections.sort(tempList, new Comparator());
        for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
            FclBlCorrections charges = (FclBlCorrections) iterator.next();
            if (charges.getChargeCode() != null && (charges.getChargeCode().equals("OCNFRT") || charges.getChargeCode().equals("OFIMP"))) {
                linkedList.add(k, charges);
            } else {
                linkedList.add(charges);
            }
            j++;
        }
        newList.addAll(linkedList);
        return newList;
    }
}

package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.GenerateFileNumber;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.bc.fcl.CustAddressBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlUtil;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadApplicationProperties;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.Comparator;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.model.ResultModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.logiware.form.FclBlForm;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.dao.FclBlDAO;
import com.logiware.hibernate.dao.FclDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.MessageResources;

public class FclBlAction extends LogiwareEventAction {

    FclBlBC fclBlBC = new FclBlBC();
    FclBlUtil fclBlUtil = new FclBlUtil();
    FclBlDAO fclBlDAO = new FclBlDAO();
    CustAddressDAO custAddressDAO = new CustAddressDAO();
    CustAddressBC custAddressBC = new CustAddressBC();

    public ActionForward saveFclBl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        List fclCostCodeList = new ArrayList();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        fclCostCodeList = fclBlCostCodesDAO.findByIdI(fclBlForm.getFclBl().getBol());
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String company = "";

        if (CommonUtils.isEmpty(fclBlForm.getFclBl().getFileNo())) {
            GenerateFileNumber generateFileNumber = new GenerateFileNumber();// wil generate file number
            generateFileNumber.join();// it wil force thread to complete the task before move to next step
            fclBlForm.getFclBl().setFileNo("" + generateFileNumber.getFileNumber());
            fclBlForm.getFclBl().setBolId(new StringFormatter().getBolid(fclBlForm.getFclBl().getBillingTerminal(), fclBlForm.getFclBl().getPort(), fclBlForm.getFclBl().getFileNo()));
        }
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        if (!"M".equalsIgnoreCase(fclBlForm.getFclBl().getReadyToPost())) {
            List list = new FclBlChargesDAO().findByPropertyAndBlNumber("chargeCode", "CAF", fclBlForm.getFclBl().getBol());
            if ("P".equalsIgnoreCase(fclBlForm.getFclBl().getStreamShipBL())) {
                if (!list.isEmpty()) {
                    new FclBlChargesDAO().deleteCharges(fclBlForm.getFclBl().getBol(), "CAF");
                }
            } else if (list.isEmpty()) {
                fclBlUtil.recalculateCAF(fclBlForm.getFclBl().getBol(), fclBlForm.getFclBl().getPort());
            }

        }
        if (CommonUtils.isEmpty(fclBlForm.getFclBl().getRoutedAgentcheck())) {
            List l = new FclBlCostCodesDAO().findByPropertyAndBlNumber("costCode", "FAECOMM", fclBlForm.getFclBl().getBol());
            FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
            if (!l.isEmpty()) {
                fclBlCostCodes = (FclBlCostCodes) l.get(0);
                if (!"FAE".equalsIgnoreCase(fclBlCostCodes.getBookingFlag()) && (CommonUtils.isEmpty(fclBlCostCodes.getTransactionType()) || "AC".equalsIgnoreCase(fclBlCostCodes.getTransactionType()))) {
                    fclBlBC.deleteCostDetails("" + fclBlCostCodes.getCodeId(), fclBlForm.getFclBl().getBolId(), user.getLoginName(), fclBlForm.getFclBl().getRatesNonRates(), request);
                }
            }
        }
        LinkedList linkedList = new LinkedList();
        List manifestedCostList = new ArrayList();
        for (Iterator iterator = fclCostCodeList.iterator(); iterator.hasNext();) {
            FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
            if (!"yes".equalsIgnoreCase(fclBlCostCodes.getDeleteFlag())) {
                if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                    linkedList.add(0, fclBlCostCodes);
                } else {
                    linkedList.add(fclBlCostCodes);
                }
                manifestedCostList.add(fclBlCostCodes);
            }
        }

        List consolidatorCostList = fclBlUtil.consolidateRatesForCosts(manifestedCostList, fclBlForm.getFclBl(), false);

        setCorrectedParties(fclBlForm);
        GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
        GeneralInformation generalInformation = generalInformationDAO.getGeneralInformationByAccountNumber(fclBlForm.getFclBl().getSslineNo());
        if (generalInformation != null && generalInformation.getShippingCode() != null && !generalInformation.getShippingCode().equalsIgnoreCase("N")) {
            fclBlForm.getFclBl().setInttra(generalInformation.getShippingCode());
        } else {
            fclBlForm.getFclBl().setInttra("");
        }
        List<FclBl> correctedFclBlList = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO().getAllCorrectedBls(fclBlForm.getFclBl().getBolId());
        //--Thie is to update corrected bls
        if (CommonFunctions.isNotNullOrNotEmpty(correctedFclBlList)) {
            fclBlBC.updateCorrectedBlPrintOptions(fclBlForm.getFclBl(), correctedFclBlList);
        }
        if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        } else if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        } else if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.ECU.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        }
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlForm.setiIconBillToolTip(fclBlCostCodesDAO.getBillOfLaddingForIicon(consolidatorCostList, fclBlForm.getFclBl().getBol()));
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward goToHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        List fclCostCodeList = new ArrayList();
        List fclChargesList = new ArrayList();
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String company = "";
        String fclExportMasterShipper = null;
        if (CommonUtils.isNotEmpty(request.getParameter("blId"))) {
            FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(request.getParameter("blId")));
            if (fclBl.getBillingTerminal() != null) {
                String BillTerm[] = fclBl.getBillingTerminal().split("-");
                String billingTerminal = BillTerm[1];
                fclExportMasterShipper = new RefTerminalDAO().getFCLExportMasterShipper(billingTerminal);
            }
            fclChargesList = fclBlChargesDAO.findByProperty("bolId", fclBl.getBol());
            Collections.sort(fclChargesList, new Comparator());
            fclCostCodeList = fclBlCostCodesDAO.findByIdI(fclBl.getBol());

            LinkedList linkedList = new LinkedList();
            List newList = new ArrayList();

            List manifestedCostList = new ArrayList();
            for (Iterator iterator = fclCostCodeList.iterator(); iterator.hasNext();) {
                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) iterator.next();
                if (!"yes".equalsIgnoreCase(fclBlCostCodes.getDeleteFlag())) {
                    if (fclBlCostCodes.getCostCode() != null && (fclBlCostCodes.getCostCode().equals("OCNFRT") || fclBlCostCodes.getCostCode().equals("OFIMP"))) {
                        linkedList.add(0, fclBlCostCodes);
                    } else {
                        linkedList.add(fclBlCostCodes);
                    }
                    manifestedCostList.add(fclBlCostCodes);
                }
            }
            newList.addAll(linkedList);
            List consolidatorCostList = fclBlUtil.consolidateRatesForCosts(manifestedCostList, fclBl, false);

            if (fclBl.getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
                company = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                fclBl.setEdiShipperCheck("on");

            } else if (fclBl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
                company = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                fclBl.setEdiShipperCheck("on");
            } else if (fclBl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
                company = LoadLogisoftProperties.getProperty("application.ECU.companyname");
                fclBl.setEdiShipperCheck("on");
            }
            if (null != fclBl && CommonUtils.isEmpty(fclBl.getShipperName())) {
                fclBl.setShipperName(company);
                if (CommonUtils.isNotEmpty(fclExportMasterShipper) && CommonUtils.isNotEmpty(fclBl.getFiletype()) && fclBl.getFiletype().equalsIgnoreCase("S")) {
                    CustomerAddress customerAddress = custAddressDAO.findByAgentName(fclExportMasterShipper);
                    if (customerAddress != null) {
                        // fclBl.setShipperName(customerAddress.getAcctname());
                        fclBl.setShipperNo(customerAddress.getAccountNo());
                        String address = custAddressBC.getCompleteAddress(customerAddress.getAccountNo());
                        fclBl.setShipperAddress(address);
                    }
                } else {
                    CustAddress custAddress = new CustAddressBC().getCustInfo("", "Z");
                    if (null != custAddress) {
                        //  fclBl.setShipperName(custAddress.getAcctName());
                        fclBl.setShipperNo(custAddress.getAcctNo());
                        fclBl.setShipperAddress(new CustAddressBC().getCustomerAddress(custAddress));
                    }
                }
            } else if (null != fclBl) {
                fclBl.setShipperName(company);
            }
            Quotation quotation = fclBlBC.getQuoteByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));
            BookingFcl bookingFcl = fclBlBC.getBookingByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));
            if (quotation != null) {
                fclBlForm.setQuoteBy(quotation.getQuoteBy());
                fclBlForm.setQuoteDate(DateUtils.formatDate(quotation.getQuoteDate(), "dd-MMM-yy HH:mm"));
            }
            if (bookingFcl != null) {
                fclBlForm.setBookedBy(bookingFcl.getUsername());
                fclBlForm.setBookedDate(DateUtils.formatDate(bookingFcl.getBookingDate(), "dd-MMM-yy HH:mm"));
                fclBlForm.setSpotRate(bookingFcl.getSpotRate());
                fclBl.setSpotRate(bookingFcl.getSpotRate());
            }
            fclBlForm.setFclBl(fclBl);
            fclBlForm.setiIconBillToolTip(fclBlCostCodesDAO.getBillOfLaddingForIicon(consolidatorCostList, fclBl.getBol()));
            fclBlUtil.setRequestObjects(fclBlForm, request);
        }
        setCorrectedParties(fclBlForm);
        String returnResult = new ProcessInfoBC().cheackFileNumberForLoack(fclBlForm.getFclBl().getFileNo(), user.getUserId().toString(), FclBlConstants.FCLBL);
        if (CommonFunctions.isNotNull(returnResult)) {
            request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult);
            request.setAttribute("view", "3");
        }
        String mandatoryFieldForBl = "Mandatory Fields Needed<br>1.Container Unit Number<BR>2.Confirm On Board<BR>3.AES Details<BR>4.Description of Package";
        session.setAttribute("mandatoryFieldForBl", mandatoryFieldForBl);
        return mapping.findForward("success");
    }

    public ActionForward readyToPost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        request.setAttribute("readyToPost", "true");
        List fclCostCodeList = new FclBlCostCodesDAO().findByProperty("bolId", fclBlForm.getFclBl().getBol());
        fclBlUtil.getContactConfigDetailsForCodeC(fclCostCodeList, fclBlForm.getFclBl(), request);
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        FclBlNew bl = fclBlForm.getFclBl();
        if (CommonUtils.isNotEmpty(bl.getSslineNo()) && CommonUtils.isNotEmpty(bl.getAgentNo())) {
            String oldVendorName = CommonUtils.isEqualIgnoreCase(bl.getStreamShipBL(), "C") ? bl.getSslineName() : bl.getAgent();
            String oldVendorNumber = CommonUtils.isEqualIgnoreCase(bl.getStreamShipBL(), "C") ? bl.getSslineNo() : bl.getAgentNo();
            String newVendorName = CommonUtils.isEqualIgnoreCase(bl.getStreamShipBL(), "C") ? bl.getAgent() : bl.getSslineName();
            String newVendorNumber = CommonUtils.isEqualIgnoreCase(bl.getStreamShipBL(), "C") ? bl.getAgentNo() : bl.getSslineNo();
            List<FclBlCostCodes> openCosts = new FclBlCostCodesDAO().getOpenCosts(bl.getBol(), oldVendorNumber);
            int count = 0;
            for (FclBlCostCodes cost : openCosts) {
                cost.setAccName(newVendorName);
                cost.setAccNo(newVendorNumber);
                transactionLedgerDAO.updateAccrual(cost.getCodeId(), newVendorName, newVendorNumber);
                count++;
            }
            if (count > 0) {
                StringBuilder desc = new StringBuilder();
                desc.append("All costs in this file transferred to ");
                desc.append(newVendorName);
                desc.append(" from ");
                desc.append(oldVendorName);
                new NotesBC().saveNotesWhileTransferCost(bl.getFileNo(), user.getLoginName(), desc.toString());
            }
        }
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward manifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        synchronized (this) {
            FclBlNew bl = new FclBlDAO().findById(fclBlForm.getFclBl().getBol());
            if (!"M".equalsIgnoreCase(bl.getReadyToPost())) {
                super.registerEvent(form, request, response);
                HttpSession session = request.getSession(true);
                MessageResources messageResources = getResources(request);
                User user = (User) session.getAttribute("loginuser");
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                fclBlForm.getFclBl().setReadyToPost("M");
                fclBlForm.getFclBl().setManifestedBy(user.getLoginName());
                fclBlForm.getFclBl().setManifestedDate(new Date());
                fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
                fclBlForm.getFclBl().setUpdateDate(new Date());
                PropertyUtils.copyProperties(bl, fclBlForm.getFclBl());
                fclBlDAO.saveOrUpdate(bl);
                fclBlUtil.doManifest(fclBlForm, messageResources, request, user, realPath);
                fclBlUtil.setRequestObjects(fclBlForm, request);
            }
        }
        return mapping.findForward("success");

    }

    public ActionForward unManifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        synchronized (this) {
            FclBlNew bl = new FclBlDAO().findById(fclBlForm.getFclBl().getBol());
            if ("M".equalsIgnoreCase(bl.getReadyToPost())) {
                super.registerEvent(form, request, response);
                HttpSession session = request.getSession(true);
                MessageResources messageResources = getResources(request);
                User user = (User) session.getAttribute("loginuser");
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                fclBlForm.getFclBl().setReadyToPost("");
                fclBlForm.getFclBl().setManifestedBy(null);
                fclBlForm.getFclBl().setManifestedDate(null);
                fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
                fclBlForm.getFclBl().setUpdateDate(new Date());
                PropertyUtils.copyProperties(bl, fclBlForm.getFclBl());
                fclBlDAO.saveOrUpdate(bl);
                fclBlUtil.doUnManifest(fclBlForm, messageResources, request, user, realPath);
                fclBlUtil.setRequestObjects(fclBlForm, request);
            }
        }
        return mapping.findForward("success");
    }

    public ActionForward FAECalculation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        fclBlBC.FaeCalculation(fclBlForm.getBol(), request);
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward deleteInsurance(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.getFclBl().setInsurance("N");
        fclBlForm.getFclBl().setCostOfGoods(0d);
        fclBlForm.getFclBl().setInsuranceRate(0d);
        new FclBlChargesDAO().deleteInsuranceCharges(fclBlForm.getFclBl().getBol());
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward calculateInsurance(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.getFclBl().setInsurance("Y");
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.calculateInsurance(fclBlForm.getFclBl().getBol(), request, user.getLoginName());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward disableContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.deductChargesAndCostAmountOnContainerDisabled(fclBlForm.getFclBl().getFileNo(), fclBlForm.getFclBl().getBol().toString(), fclBlForm.getSelectedId(), fclBlForm.getSize(), fclBlForm.getComment(), user);
        fclBlBC.FaeCalculation(fclBlForm.getBol(), request);
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward disableForAes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.disableAes(fclBlForm.getFclBl().getFileNo(), fclBlForm.getSelectedId(), fclBlForm.getAesComment(), user, request);
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward enableForAes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.enableAes(fclBlForm.getFclBl().getFileNo(), fclBlForm.getSelectedId(), fclBlForm.getAesComment(), user, request);
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward enableContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.addChargesAndCostAmountOnContainerEnabled(fclBlForm.getFclBl().getFileNo(), fclBlForm.getFclBl().getBol().toString(), fclBlForm.getSelectedId(), fclBlForm.getSize(), fclBlForm.getComment(), user);
        fclBlBC.FaeCalculation(fclBlForm.getBol(), request);
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward createMultipleBL(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        FclBl fclBl = fclBlUtil.createMultipleBl(fclBlForm.getFclBl().getBolId(), user);
        FclBlNew fclBlNew = new FclDAO().findById(fclBl.getBol());
        fclBlForm.setFclBl(fclBlNew);
        Quotation quotation = fclBlBC.getQuoteByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));
        BookingFcl bookingFcl = fclBlBC.getBookingByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));
        if (quotation != null) {
            fclBlForm.setQuoteBy(quotation.getQuoteBy());
            fclBlForm.setQuoteDate(DateUtils.formatDate(quotation.getQuoteDate(), "dd-MMM-yy HH:mm"));
        }
        if (bookingFcl != null) {
            fclBlForm.setBookedBy(bookingFcl.getUsername());
            fclBlForm.setBookedDate(DateUtils.formatDate(bookingFcl.getBookingDate(), "dd-MMM-yy HH:mm"));
        }
        String returnResult = new ProcessInfoBC().cheackFileNumberForLoack(fclBlNew.getFileNo(), user.getUserId().toString(), FclBlConstants.FCLBL);
        if (CommonFunctions.isNotNull(returnResult)) {
            request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult);
            request.setAttribute("view", "3");
        }
        session.setAttribute("check", "edit");
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward resendAccrual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.updateManifestModifyFlag(fclBlForm.getSelectedId(), user);
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward blClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.setBlClosed("Y");
        fclBlForm.setClosedBy(user.getLoginName());
        fclBlForm.setClosedDate(DateUtils.formatDate(new Date(), "dd-MMM-yyyy HH:mm:ss"));
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward blOpen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.setBlClosed(null);
        fclBlForm.setClosedBy(null);
        fclBlForm.setClosedDate(null);
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward blAudit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.setBlAudited("Y");
        fclBlForm.setAuditedBy(user.getLoginName());
        fclBlForm.setAuditedDate(DateUtils.formatDate(new Date(), "dd-MMM-yyyy HH:mm:ss"));
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward blCancelAudit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlForm.setBlAudited(null);
        fclBlForm.setAuditedBy(null);
        fclBlForm.setAuditedDate(null);
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        fclBlForm.getFclBl().setUpdateDate(new Date());
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward reverseToBooking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlDAO.updateBolIdForBookingAccruals(fclBlForm.getFclBl().getBol().toString());
        BookingFcl bookingFcl = fclBlBC.reverseToBook(fclBlForm.getFclBl().getBolId());
        if (CommonFunctions.isNotNull(bookingFcl)) {
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            if (CommonFunctions.isNotNull(bookingFcl.getFileNo())) {
                fclBlUtil.setFileList(session, bookingFcl);
                Integer userId = (user != null) ? user.getUserId() : 0;
                processInfoBC.releaseLoack(LoadApplicationProperties.getProperty("lockFclBlModule"),
                        bookingFcl.getFileNo(), userId);
                session.setAttribute("selectedFileNumber", bookingFcl.getFileNo());
            }
        }
        session.setAttribute("screenName", "Bookings");
        return mapping.findForward("closeSearch");
    }

    public ActionForward reverseToQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        super.registerEvent(form, request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        Quotation quotation = fclBlBC.reverseToQuote(fclBlForm.getFclBl().getBolId());
        if (CommonFunctions.isNotNull(quotation)) {
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            if (CommonFunctions.isNotNull(quotation.getFileNo())) {
                fclBlUtil.setFileList(session, quotation);
                Integer userId = (user != null) ? user.getUserId() : 0;
                processInfoBC.releaseLoack(LoadApplicationProperties.getProperty("lockFclBlModule"),
                        quotation.getFileNo(), userId);
                session.setAttribute("selectedFileNumber", quotation.getFileNo());
            }
        }
        session.setAttribute("screenName", "Quotes");
        return mapping.findForward("closeSearch");
    }

    public ActionForward deleteBl(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        FclBlNew fclBlNew = new FclDAO().findById(fclBlForm.getBol());
        fclBlDAO.delete(fclBlNew);
        setFileListAfterDeletingBl(session, fclBlForm.getFclBl().getFileNo());
        session.setAttribute("selectedFileNumber", fclBlForm.getFclBl().getFileNo());
        session.setAttribute("screenName", "Bookings");
        return mapping.findForward("closeSearch");
    }

    public ActionForward deleteBlCharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        MessageResources messageResources = getResources(request);
        fclBlUtil.deleteCharges(fclBlForm.getSelectedId(), fclBlForm.getFclBl().getBol(), user.getLoginName(), messageResources, request);
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward deleteBlCost(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlBC.deleteCostDetails(fclBlForm.getSelectedId(), fclBlForm.getFclBl().getBolId(), user.getLoginName(), fclBlForm.getFclBl().getRatesNonRates(), request);
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward postAccrualsBeforeManifest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        fclBlUtil.postAccrualsBeforeManifest(fclBlForm.getFclBl().getBolId(), user);
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public ActionForward editContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        request.setAttribute(FclBlConstants.FCL_BL_CONTAINER, new FclBlContainerDAO().findById(Integer.parseInt(fclBlForm.getSelectedId())));
        request.setAttribute("fclBl", fclBlForm.getFclBl());
        return mapping.findForward("fclBlContainer");
    }

    public ActionForward addContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        request.setAttribute(FclBlConstants.FCL_BL_CONTAINER, new FclBlContainerDAO().findById(Integer.parseInt(fclBlForm.getSelectedId())));
        request.setAttribute("fclBl", fclBlForm.getFclBl());
        return mapping.findForward("fclBlContainer");
    }

    public ActionForward goToSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        FclBl saveFclBl
                = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO().findById(fclBlForm.getBol());
        ProcessInfoBC processInfoBC = new ProcessInfoBC();
        if (null != saveFclBl) {
            if (CommonFunctions.isNotNull(saveFclBl.getFileNo())) {
                Integer userId = (user != null) ? user.getUserId() : 0;
                processInfoBC.releaseLoack(LoadApplicationProperties.getProperty("lockFclBlModule"),
                        saveFclBl.getFileNo(), userId);
                session.setAttribute("selectedFileNumber", saveFclBl.getFileNo());
            }
            fclBlUtil.setFileList(session, saveFclBl);
        }
        session.setAttribute("screenName", "fileSearch");
        if ("save".equals(fclBlForm.getAction())) {
            List<FclBl> correctedFclBlList = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO().getAllCorrectedBls(fclBlForm.getFclBl().getBolId());
            //--Thie is to update corrected bls
            if (CommonFunctions.isNotNullOrNotEmpty(correctedFclBlList)) {
                fclBlBC.updateCorrectedBlPrintOptions(fclBlForm.getFclBl(), correctedFclBlList);
            }
            fclBlForm.getFclBl().setSpotRate(fclBlDAO.getSpotRateStatus(fclBlForm.getFclBl().getFileNo()));
            fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        }
        return mapping.findForward("closeSearch");
    }

    public ActionForward addBrandValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String company = "";
        fclBlForm.getFclBl().setUpdateBy(user.getLoginName());
        if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        } else if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        } else if (fclBlForm.getFclBl().getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            if (fclBlForm.getFclBl().getEdiShipperCheck() != null && fclBlForm.getFclBl().getEdiShipperCheck().equalsIgnoreCase("on")) {
                company = LoadLogisoftProperties.getProperty("application.ECU.companyname");
                fclBlForm.getFclBl().setShipperName(company);
            }
        }
        fclBlDAO.saveOrUpdate(fclBlForm.getFclBl());
        fclBlUtil.setRequestObjects(fclBlForm, request);
        return mapping.findForward("success");
    }

    public void setFileListAfterDeletingBl(HttpSession session, String fileno) {
        if (session.getAttribute("SearchListByfileNumber") != null) {
            List getFileList = (List) session.getAttribute("SearchListByfileNumber");
            for (int i = 0; i < getFileList.size(); i++) {
                FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                if (fileNumberForQuotaionBLBooking.getFileNo() != null && fileno != null
                        && fileNumberForQuotaionBLBooking.getFileNo().equals(fileno.toString())) {
                    fileNumberForQuotaionBLBooking.setFclBlId(null);
                    getFileList.remove(fileNumberForQuotaionBLBooking);
                    break;
                }
            }
            session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
        }
    }

    public void setCorrectedParties(FclBlForm fclBlForm) throws Exception {
        FclBlCorrections fclBlCorrections = new FclBlCorrectionsDAO().getLatestPostedCorrection(fclBlForm.getFclBl().getFileNo());
        if (null != fclBlCorrections) {
            String correctionType = fclBlCorrections.getCorrectionType().getCode();
            List l = new FclDAO().findByProperty("bolId", fclBlCorrections.getNewBolIdForApprovedBl());
            if (!l.isEmpty()) {
                FclBlNew correctedBl = (FclBlNew) l.get(0);
                if ("J".equalsIgnoreCase(correctionType) || "Q".equalsIgnoreCase(correctionType) || "U".equalsIgnoreCase(correctionType) || "X".equalsIgnoreCase(correctionType) || "N".equalsIgnoreCase(correctionType)) {
                    fclBlForm.setCorrectedShipperName(correctedBl.getHouseShipperName());
                    fclBlForm.setCorrectedShipperNo(correctedBl.getHouseShipperNo());
                    fclBlForm.setCorrectedShipperAddress(CommonUtils.isNotEmpty(fclBlForm.getCorrectedShipperAddress()) ? fclBlForm.getCorrectedShipperAddress() : CommonUtils.isNotEmpty(correctedBl.getHouseShipperAddress()) ? correctedBl.getHouseShipperAddress() : getAddress(correctedBl.getHouseShipperNo()));
                    if (CommonUtils.isNotEmpty(fclBlForm.getCorrectedShipperAddress())) {
                        correctedBl.setHouseShipperAddress(fclBlForm.getCorrectedShipperAddress());
                    }
                } else if ("K".equalsIgnoreCase(correctionType) || "M".equalsIgnoreCase(correctionType) || "R".equalsIgnoreCase(correctionType)
                        || "T".equalsIgnoreCase(correctionType) || "W".equalsIgnoreCase(correctionType)) {
                    fclBlForm.setCorrectedForwarderName(correctedBl.getForwardingAgentName());
                    fclBlForm.setCorrectedForwarderNo(correctedBl.getForwardagentNo());
                    fclBlForm.setCorrectedForwarderAddress(CommonUtils.isNotEmpty(fclBlForm.getCorrectedForwarderAddress()) ? fclBlForm.getCorrectedForwarderAddress() : CommonUtils.isNotEmpty(correctedBl.getForwardagentNo()) ? correctedBl.getForwardingAgent() : getAddress(correctedBl.getForwardagentNo()));
                    if (CommonUtils.isNotEmpty(fclBlForm.getCorrectedForwarderAddress())) {
                        correctedBl.setForwardingAgent(fclBlForm.getCorrectedForwarderAddress());
                    }

                } else if ("V".equalsIgnoreCase(correctionType) || "S".equalsIgnoreCase(correctionType) || "P".equalsIgnoreCase(correctionType) || "L".equalsIgnoreCase(correctionType)) {
                    fclBlForm.setCorrectedThirdPartyName(correctedBl.getThirdPartyName());
                    fclBlForm.setCorrectedThirdPartyNo(correctedBl.getBillTrdPrty());
                }
            }
        } else {
            fclBlForm.setCorrectedShipperName("");
            fclBlForm.setCorrectedShipperNo("");
            fclBlForm.setCorrectedShipperAddress("");
            fclBlForm.setCorrectedForwarderName("");
            fclBlForm.setCorrectedForwarderNo("");
            fclBlForm.setCorrectedForwarderAddress("");
            fclBlForm.setCorrectedThirdPartyName("");
            fclBlForm.setCorrectedThirdPartyNo("");
        }
    }

    public String getAddress(String accoutNo) throws Exception {
        StringBuilder address = new StringBuilder();
        if (null != accoutNo && !"".equalsIgnoreCase(accoutNo)) {
            CustAddressDAO customerDAO = new CustAddressDAO();
            List custAddressList = (List) customerDAO.findByAccountNo(null, accoutNo, null, null);
            if (!custAddressList.isEmpty()) {
                CustAddress custAddress = (CustAddress) customerDAO.findByAccountNo(null, accoutNo, null, null).get(0);
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

    public ActionForward showArTransactions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlForm fclBlForm = (FclBlForm) form;
        String fileNo = request.getParameter("fileNo");
        List<ResultModel> postedTransactions = new ArTransactionHistoryDAO().getPostedTransactions(null, fileNo, "FCL");
        request.setAttribute("postedTransactions", postedTransactions);
        return mapping.findForward("showArTransactionDetails");
    }
}

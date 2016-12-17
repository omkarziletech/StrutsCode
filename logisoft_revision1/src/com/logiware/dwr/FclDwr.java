package com.logiware.dwr;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.FclBlUtil;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.domain.FCLPortConfiguration;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.dao.FclManifestDAO;
import com.logiware.form.FclBlForm;
import com.logiware.hibernate.dao.FclBlDAO;
import com.logiware.hibernate.dao.FclDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.util.MessageResources;

/**
 * @description ArDwr
 * @author LakshmiNarayanan
 */
public class FclDwr implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = 2104673553314712631L;

    public void showFclBlContainer(String bol, HttpServletRequest request) throws Exception {
        FclBlForm fclBlForm = new FclBlForm();
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        fclBlForm.setFclBl(fclBl);
        request.setAttribute("fclBlForm", fclBlForm);
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
    }

    public void showFclBlCharges(String bol, HttpServletRequest request) throws Exception {
        FclBlForm fclBlForm = new FclBlForm();
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        fclBlForm.setFclBl(fclBl);
        request.setAttribute("fclBlForm", fclBlForm);
        new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
    }

    public void postAccrualsAfterContainerUpdate(String bol, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        com.gp.cvst.logisoft.hibernate.dao.FclBlDAO fclBlDao = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO();
        FclBl fclBl = fclBlDao.findById(Integer.parseInt(bol));
        new FclManifestDAO(fclBl, user).postAccrualsForContainerUpdation();
    }

    public String updateFclBlChargesAfterContainerUpdation(String bol, HttpServletRequest request) throws Exception {
        updateFclChargesBasedOnContainersUpdation(Integer.parseInt(bol), request);
        return "true";
    }

    public void calculateFae(String bol, HttpServletRequest request) throws Exception {
        FclBlForm fclBlForm = new FclBlForm();
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        fclBlForm.setFclBl(fclBl);
        request.setAttribute("fclBlForm", fclBlForm);
        new FclBlUtil().FaeCalculation(Integer.parseInt(bol), request);
        new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
    }

    public void showAesFiling(String bol, HttpServletRequest request) throws Exception {
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        List<SedFilings> aesList = new ArrayList<SedFilings>();
        List list = new SedFilingsDAO().findByDr(fclBl.getFileNo());
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
        request.setAttribute("sedFilingsList", aesList);
        request.setAttribute("fclBl", fclBl);
    }

    public void deleteAesFiling(String bol, String trnref, HttpServletRequest request) throws Exception {
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        SedFilings sedFilings = new SedFilingsDAO().findByTrnref(trnref);
        if (null != sedFilings) {
            new SedFilingsDAO().delete(sedFilings);
            new NotesBC().deleteAesDetails(fclBl.getFileNo(), user.getLoginName(), sedFilings.getTrnref() + " AES details is deleted");
        }
        List<SedFilings> aesList = new ArrayList<SedFilings>();
        List list = new SedFilingsDAO().findByDr(fclBl.getFileNo());
        if (null != list) {
            for (Object object : list) {
                SedFilings sed = (SedFilings) object;
                sed.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sed.getTrnref()));
                if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sed.getTrnref()))) {
                    sedFilings.setSched(true);
                }
                aesList.add(sed);
            }
        }
        request.setAttribute("sedFilingsList", aesList);
        request.setAttribute("fclBl", fclBl);
    }

    public void setBillToParty(String bol, String billToParty, String houseBl, HttpServletRequest request) throws Exception {
        if (CommonFunctions.isNotNull(bol)) {
            List chargesList = new FclBlChargesDAO().findByParentId(Integer.parseInt(bol));
            for (Iterator iterator = chargesList.iterator(); iterator.hasNext();) {
                FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
                if ("CAF".equalsIgnoreCase(fclBlCharges.getChargeCode())) {
                    fclBlCharges.setBillTo("Agent");
                } else {
                    fclBlCharges.setBillTo(billToParty);
                }
                new FclBlChargesDAO().update(fclBlCharges);
            }
            // updating billto code
            FclDAO fclDAO = new FclDAO();
            FclBlNew fclBl = fclDAO.findById(Integer.parseInt(bol));
            if (!"M".equalsIgnoreCase(fclBl.getReadyToPost())) {
                List list = new FclBlChargesDAO().findByPropertyAndBlNumber("chargeCode", "CAF", fclBl.getBol());
                if ("P".equalsIgnoreCase(houseBl)) {
                    if (!list.isEmpty()) {
                        new FclBlChargesDAO().deleteCharges(fclBl.getBol(), "CAF");
                    }
                } else {
                    if (list.isEmpty()) {
                        new FclBlUtil().recalculateCAF(fclBl.getBol(), fclBl.getPort());
                    }
                }
            }
            if (billToParty != null) {
                if (billToParty.equalsIgnoreCase("Agent")) {
                    fclBl.setBillToCode("A");
                } else if (billToParty.equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                    fclBl.setBillToCode("C");
                } else if (billToParty.equalsIgnoreCase("Shipper")) {
                    fclBl.setBillToCode("S");
                } else if (billToParty.equalsIgnoreCase("Forwarder")) {
                    fclBl.setBillToCode("F");
                } else if (billToParty.equalsIgnoreCase("ThirdParty")) {
                    fclBl.setBillToCode("T");
                } else if (billToParty.equalsIgnoreCase("NotifyParty")) {
                    fclBl.setBillToCode("N");
                }
                fclBl.setHouseBL(houseBl);
                fclDAO.update(fclBl);
                fclDAO.getCurrentSession().flush();
            }
            FclBlForm fclBlForm = new FclBlForm();
            fclBlForm.setFclBl(fclBl);

            request.setAttribute("fclBlForm", fclBlForm);
            new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
        }
    }

    public void setHouseBlToBoth(String bol, HttpServletRequest request) throws Exception {
        if (CommonFunctions.isNotNull(bol)) {
            FclDAO fclDAO = new FclDAO();
            FclBlNew fclBl = fclDAO.findById(Integer.parseInt(bol));
            fclBl.setHouseBL("B");
            fclDAO.update(fclBl);
            fclDAO.getCurrentSession().flush();
            FclBlForm fclBlForm = new FclBlForm();
            fclBlForm.setFclBl(fclBl);
            request.setAttribute("fclBlForm", fclBlForm);
            new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
        }
    }

    public boolean checkAccrualPosted(String bol) throws Exception {
        List l = new FclBlCostCodesDAO().postedAccrualBeforeManifest(Integer.parseInt(bol));
        if (!l.isEmpty()) {
            return true;
        }
        return false;
    }

    public void changeCostAcctName(String bol, String acctNo, String accountName, String accountNo, HttpServletRequest request) throws Exception {
        if (CommonFunctions.isNotNull(bol) && CommonFunctions.isNotNull(acctNo) && CommonFunctions.isNotNull(accountName)
                && CommonFunctions.isNotNull(accountNo)) {
            List costList = new ArrayList();
            List accrulsList = new ArrayList();
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
            FclBlForm fclBlForm = new FclBlForm();
            User user = (User) request.getSession().getAttribute("loginuser");
            costList = fclBlCostCodesDAO.getAPStatusCost(Integer.parseInt(bol), accountNo);
            for (Iterator it = costList.iterator(); it.hasNext();) {
                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) it.next();
                fclBlCostCodes.setAccNo(acctNo);
                fclBlCostCodes.setAccName(accountName);
                new FclBlCostCodesDAO().update(fclBlCostCodes);
                if (fclBlCostCodes.getCodeId() != null) {
                    accrulsList = new TransactionLedgerDAO().findByProperty("costId", fclBlCostCodes.getCodeId());
                    for (Iterator it1 = accrulsList.iterator(); it1.hasNext();) {
                        TransactionLedger transactionLedger = (TransactionLedger) it1.next();
                        transactionLedger.setCustName(accountName);
                        transactionLedger.setCustNo(acctNo);
                        new TransactionLedgerDAO().update(transactionLedger);
                    }
                }
                new NotesBC().saveNotesWhileTransferCost(fclBlForm.getFclBl().getFileNo(), user.getLoginName(), fclBlCostCodes.getCostCodeDesc() + " costs in this file  transferred to " + acctNo + " from " + accountNo + "");
            }
            fclBlForm.setFclBl(fclBl);
            request.setAttribute("fclBlForm", fclBlForm);
            new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
        }
    }

    public String validateMasterAccount(String accountNumber) throws Exception {
        String result = "";
        String[] account = accountNumber.split(",");
        for (String string : account) {
            if (new TradingPartnerDAO().isMaster(string)) {
                if (!"".equals(result)) {
                    result = result + "," + string;
                } else {
                    result = string;
                }
            }
        }
        return result;
    }

    public String validateCustomerOnManifest(String bol) throws Exception {
        StringBuilder sb = new StringBuilder("");
        FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(bol));
        boolean isFirst = true;
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != fclBl) {
            if (CommonUtils.isNotEmpty(fclBl.getShipperNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getShipperNo())) {
                sb.append("MasterShipper");
                isFirst = false;
            }
            if (CommonUtils.isNotEmpty(fclBl.getConsigneeNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getConsigneeNo())) {
                if (isFirst) {
                    sb.append("MasterConsignee");
                    isFirst = false;
                } else {
                    sb.append(", MasterConsignee");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getNotifyParty()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getNotifyParty())) {
                if (isFirst) {
                    sb.append("MasterNotify");
                    isFirst = false;
                } else {
                    sb.append(", MasterNotify");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getHouseShipperNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getHouseShipperNo())) {
                if (isFirst) {
                    sb.append("HouseShipper");
                    isFirst = false;
                } else {
                    sb.append(", HouseShipper");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getHouseConsignee()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getHouseConsignee())) {
                if (isFirst) {
                    sb.append("HouseConsignee");
                    isFirst = false;
                } else {
                    sb.append(", HouseConsignee");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getHouseNotifyPartyNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getHouseNotifyPartyNo())) {
                if (isFirst) {
                    sb.append("HouseNotify");
                    isFirst = false;
                } else {
                    sb.append(", HouseNotify");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getForwardagentNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getForwardagentNo())) {
                if (isFirst) {
                    sb.append("Forwarder");
                    isFirst = false;
                } else {
                    sb.append(", Forwarder");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getAgentNo()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getAgentNo())) {
                if (isFirst) {
                    sb.append("Agent");
                    isFirst = false;
                } else {
                    sb.append(", Agent");
                }
            }
            if (CommonUtils.isNotEmpty(fclBl.getBillTrdPrty()) && tradingPartnerDAO.checkDisabledAccount(fclBl.getBillTrdPrty())) {
                if (isFirst) {
                    sb.append("ThirdParty");
                    isFirst = false;
                } else {
                    sb.append(", ThirdParty");
                }
            }
            if (CommonUtils.isNotEmpty(sb.toString())) {
                sb.append(" account on the file are disabled,Please reselect a valid account");
            }
        }
        return sb.toString();
    }

    public void updateAccountNameAC(String bolId, String sslpc, String agentName, String agentNumber, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(bolId)) {
            FclBlDAO fclBlDAO = new FclBlDAO();
            TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
            FclBlNew bl = new FclDAO().findById(Integer.parseInt(bolId));
            if (CommonUtils.isEqualIgnoreCase(sslpc, "C")) {
                bl.setStreamShipBL("C");
                bl.setAgent(agentName);
                bl.setAgentNo(agentNumber);
            } else {
                bl.setStreamShipBL("P");
            }
            FclBlForm fclBlForm = new FclBlForm();
            User user = (User) request.getSession().getAttribute("loginuser");
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
            fclBlForm.setFclBl(bl);
            fclBlDAO.save(bl);
            request.setAttribute("fclBlForm", fclBlForm);
            new FclBlUtil().SetRequestForFclChargesandCostCode(request, bl, true);
        }
    }

    public boolean checkCostIsPaid(String bol) throws Exception {
        return new FclBlCostCodesDAO().costIsAlreadyPaid(Integer.parseInt(bol));
    }

    public boolean checkOCNFRTStatus(String bolId) throws Exception {
        return new FclBlCostCodesDAO().checkAPStatus(Integer.parseInt(bolId), "OCNFRT", "AP");
    }

    public String getBilltoForIncent(Integer bol) throws Exception {
        return new FclBlDAO().getBilltoForIncent(bol, FclBlConstants.ADMINFEEWITHNOCOMMISION);
    }

    public String IsFaeCommisionSetupOrAdvSurCharge(String bol, String readyTOCheck) throws Exception {
        String faeCommisionSetupOrAdvSurCharge = "";
        boolean isCommisionRule = false;
        if (bol != null) {
            faeCommisionSetupOrAdvSurCharge = new FclBlChargesDAO().getChargCodeType(Integer.parseInt(bol));
            FclBl fclBl = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO().findById(Integer.parseInt(bol));
            if (null != fclBl && fclBl.getFinalDestination() != null) {
                StringFormatter stringFormatter = new StringFormatter();
                String cityName = stringFormatter.getBreketValue(fclBl.getFinalDestination());
                List<Ports> portList = new PortsDAO().findPortUsingUnlocaCode(cityName);
                if (portList != null && !portList.isEmpty()) {
                    Ports ports = portList.get(0);
                    List<FCLPortConfiguration> postConfigSetList = (ports.getFclPortConfigSet() != null) ? new ArrayList(ports.getFclPortConfigSet()) : Collections.EMPTY_LIST;
                    for (FCLPortConfiguration fCLPortConfiguration : postConfigSetList) {
                        if (CommonUtils.notMatches(fclBl.getFileNo(), "(\\d+)-([a-zA-Z])")) {
                            if (("Yes".equalsIgnoreCase(fclBl.getRoutedAgentCheck()) && null == fCLPortConfiguration.getRcomRule())
                                    || (null == fCLPortConfiguration.getNcomRule())) {
                                isCommisionRule = true;
                            }
                        }
                        break;
                    }
                }
            }
            boolean isIncent = !new com.logiware.hibernate.dao.FclBlDAO().getBilltoForIncent(Integer.parseInt(bol), FclBlConstants.ADMINFEEWITHNOCOMMISION).equals("");
            if (readyTOCheck.equals("yes") && isCommisionRule) {
                faeCommisionSetupOrAdvSurCharge = isIncent ? "INCENTADDED" : "INCENTNOTADDED";
            } else if (!faeCommisionSetupOrAdvSurCharge.equals("") && isCommisionRule && isIncent) {
                faeCommisionSetupOrAdvSurCharge = FclBlConstants.ADMINFEEWITHNOCOMMISION + "," + faeCommisionSetupOrAdvSurCharge;
            } else if (isCommisionRule && isIncent) {
                faeCommisionSetupOrAdvSurCharge = FclBlConstants.ADMINFEEWITHNOCOMMISION;
            }
        }
        return faeCommisionSetupOrAdvSurCharge;
    }

    public void deleteIncentForThrdParty(String bol, HttpServletRequest request) throws Exception {
        new FclBlBC().notesForIncentCharge(Integer.parseInt(bol), request, "DELETED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
        new FclBlChargesDAO().deleteIncentAndAdvSurCharge(Integer.parseInt(bol));
    }

    public void deleteIncentAndAdvsur(String bol, String incentOrAdv, HttpServletRequest request) throws Exception {
        FclBlBC fclBlBC = new FclBlBC();
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        FclBlForm fclBlForm = new FclBlForm();
        Integer bolId = Integer.parseInt(bol);
        if (incentOrAdv.equals("INCENT,ADVSHP,ADVFF")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCESHIPPERCODE);
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCEFFCODE);
        } else if (incentOrAdv.equals("INCENT")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
        } else if (incentOrAdv.equals("ADVSHP,ADVFF")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCESHIPPERCODE);
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCEFFCODE);
        } else if (incentOrAdv.equals("INCENT,ADVFF")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCEFFCODE);
        } else if (incentOrAdv.equals("INCENT,ADVSHP")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADMINFEEWITHNOCOMMISION);
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCESHIPPERCODE);
        } else if (incentOrAdv.equals("ADVSHP")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCESHIPPERCODE);
        } else if (incentOrAdv.equals("ADVFF")) {
            fclBlBC.notesForIncentCharge(bolId, request, "DELETED", FclBlConstants.ADVANCEFFCODE);
        }
        fclBlChargesDAO.deleteIncentAndAdvSurCharge(bolId);
        FclBlNew fclBl = new FclDAO().findById(bolId);
        fclBlForm.setFclBl(fclBl);
        request.setAttribute("fclBlForm", fclBlForm);
        new FclBlUtil().SetRequestForFclChargesandCostCode(request, fclBl, true);
    }

    public void updateFclChargesBasedOnContainersUpdation(Integer bol, HttpServletRequest request) throws Exception {
        com.gp.cvst.logisoft.hibernate.dao.FclBlDAO fclBlDao = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO();
        FclBl fclBl = fclBlDao.findById(bol);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        if (null != fclBl.getRatesNonRates() && fclBl.getRatesNonRates().equals("R")) {
            String fileNo = fclBl.getFileNo();
            boolean importFlag = null != fclBl.getImportFlag() && fclBl.getImportFlag().equals("I");
            // getting original(Main) file no for multible bl case
            fileNo = fileNo.contains("-") ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo;
            FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            // checking FAE and FFcom precence in costcode
            String itHasFaeFFCost = fclBlChargesDAO.itHasFaeAndFFCom(fileNo);
            List bookingChargeCostList = fclBlChargesDAO.getbookingContainerChargesAndCostForBl(fileNo);
            fclBlChargesDAO.deleteTransactionLedger(fileNo, bol);
            if (importFlag) {
                //for imports we keep cost and charges in Fclblcostcodes and Fclcharge Set
                fclBl.getFclcharge().removeAll(fclBlChargesDAO.geBookingChargesList(bol));
                fclBl.getFclblcostcodes().removeAll(fclBlChargesDAO.getBookingCostList(bol));
            } else {
                fclBlChargesDAO.deleteBookingChargesAndCost(bol);
            }
            if (!bookingChargeCostList.isEmpty()) {
                String billToCode = null != fclBl.getBillToCode() ? fclBl.getBillToCode() : "";
                billToCode = billToCode.equals("F") ? "Forwarder" : billToCode.equals("S") ? "Shipper" : billToCode.equals("T") ? "ThirdParty"
                        : billToCode.equals("C") ? "Consignee" : billToCode.equals("A") ? "Agent" : "";
                String houseBl = fclBl.getHouseBl();
                String blLevelCost = fclBlChargesDAO.getBlLevelCost();
                String costCodeForMultibleContainers = fclBlChargesDAO.getMultibleContainerCostCodes(fileNo);
                Iterator chargeCostItr = bookingChargeCostList.iterator();
                while (chargeCostItr.hasNext()) {
                    Object[] chargeCost = (Object[]) chargeCostItr.next();
                    Double costAmount = Double.parseDouble(chargeCost[0].toString());
                    Double chargeAmount = Double.parseDouble(chargeCost[1].toString());
                    String chgCostCodeDesc = (String) chargeCost[2];
                    String chgCostCode = (String) chargeCost[3];
                    String accountNo = (String) chargeCost[4];
                    String accountName = (String) chargeCost[5];
                    String bookingFlag = (String) chargeCost[6];
                    String comments = (String) chargeCost[7];
                    Double chargeAmountForManualRates = Double.parseDouble(chargeCost[8].toString());
                    Double adjustmentChargeAmount = Double.parseDouble(chargeCost[9].toString());
                    FclBlCharges fclBlCharges = new FclBlCharges();
                    FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
                    fclBlCharges.setAdjustment(0d);
                    fclBlCharges.setAdjustmentChargesRemarks("");
                    fclBlCharges.setBillTo(billToCode);
                    fclBlCharges.setBolId(bol);
                    fclBlCharges.setChargeCode(chgCostCode);
                    fclBlCharges.setCharges(chgCostCodeDesc);
                    fclBlCharges.setCurrencyCode("USD");
                    if (null != bookingFlag && !bookingFlag.equals("") && !bookingFlag.equals("PP")) {
                        fclBlCharges.setAmount(chargeAmountForManualRates + adjustmentChargeAmount);
                        fclBlCharges.setOldAmount(chargeAmountForManualRates + adjustmentChargeAmount);
                    } else {
                        fclBlCharges.setAmount(chargeAmount + adjustmentChargeAmount);
                        fclBlCharges.setOldAmount(chargeAmount + adjustmentChargeAmount);
                    }
                    fclBlCharges.setReadOnlyFlag("on");
                    fclBlCharges.setBundleIntoOfr("No");
                    fclBlCharges.setBookingFlag(bookingFlag);
                    fclBlCharges.setPrintOnBl("Yes");
                    if (houseBl.equals("C-Collect")) {
                        fclBlCharges.setPcollect("collect");
                    } else {
                        fclBlCharges.setPcollect("prepaid");
                    }
                    if (blLevelCost.contains(chgCostCode) && costCodeForMultibleContainers.contains(chgCostCode)) {
                        costAmount = fclBlChargesDAO.getSingleCostAmountForBlLevlCostCode(chgCostCode, fileNo);
                    }
                    fclBlCostCodes.setBolId(bol);
                    fclBlCostCodes.setAmount(costAmount);
                    fclBlCostCodes.setCostCode(chgCostCode);
                    fclBlCostCodes.setCostCodeDesc(chgCostCodeDesc);
                    fclBlCostCodes.setReadOnlyFlag("on");
                    fclBlCostCodes.setAccNo(accountNo);
                    fclBlCostCodes.setAccName(accountName);
                    fclBlCostCodes.setCurrencyCode("USD");
                    fclBlCostCodes.setDeleteFlag("no");
                    fclBlCostCodes.setBookingFlag(bookingFlag);
                    fclBlCostCodes.setCostComments(comments);
                    if (importFlag) {
                        fclBl.getFclcharge().add(fclBlCharges);
                        if (costAmount != 0.00 && !chgCostCode.equals("INSURE")) {
                            fclBl.getFclblcostcodes().add(fclBlCostCodes);
                        }
                    } else {
                        fclBlChargesDAO.save(fclBlCharges);
                        if (costAmount != 0.00 && !chgCostCode.equals("INSURE")) {
                            fclBlCostCodesDAO.save(fclBlCostCodes);
                        }
                    }
                }
                if (!itHasFaeFFCost.equals("")) {
                    BookingfclUnitsDAO bookingUnits = new BookingfclUnitsDAO();
                    BookingFcl bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
                    List bookingFclRatesList = bookingUnits.getbookingfcl(bookingFcl.getBookingId().toString());
                    BookingFclBC bookingFclBC = new BookingFclBC();
                    FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
                    if (!importFlag) {
                        if (itHasFaeFFCost.equals("FAECOMM")) {
                            new FclBlUtil().FaeCalculation(bol, request);
                        } else if (itHasFaeFFCost.equals("FFCOMM")) {
                            MessageResources messageResources = CommonConstants.loadMessageResources();
                            fclBlCostCodes = bookingFclBC.multipleConatinerByInputValue(messageResources, bookingFclRatesList, bookingFcl);
                        } else if (itHasFaeFFCost.equals("FFCOMM,FAECOMM")) {
                            new FclBlUtil().FaeCalculation(bol, request);
                            MessageResources messageResources = CommonConstants.loadMessageResources();
                            fclBlCostCodes = bookingFclBC.multipleConatinerByInputValue(messageResources, bookingFclRatesList, bookingFcl);
                        }
                        if(null!=fclBlCostCodes.getCostCode() && null!= fclBlCostCodes.getAccNo()){
                            fclBlCostCodes.setBolId(bol);
                            fclBlCostCodesDAO.save(fclBlCostCodes);
                        }
                    } else {
                        if (itHasFaeFFCost.equals("FFCOMM")) {
                            MessageResources messageResources = CommonConstants.loadMessageResources();
                            fclBlCostCodesDAO.save(bookingFclBC.multipleConatinerByInputValue(messageResources, bookingFclRatesList, bookingFcl));
                        }
                    }
                }
                if (importFlag) {
                    fclBlDao.update(fclBl);
                    new FclManifestDAO(fclBl, user).postAccrualsForContainerUpdation();
                }
            }
        }
    }
    public String checkBrandForDestination(String destination) {
        String brandField = new PortsDAO().brandField(destination);
        return brandField;
    }

    public String checkBrandForClient(String acctno) {
        String brandField = new TradingPartnerDAO().brandFieldForClient(acctno);
        return brandField;
    }
    public String checkBrandForBl(String bol) throws Exception {
        com.gp.cvst.logisoft.hibernate.dao.FclBlDAO fclBlDao = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO();
        String queryString = fclBlDao.brandValue(bol);
        return queryString;
    }
}

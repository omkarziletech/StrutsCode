/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.bc.fcl.CustAddressBC;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.struts.form.SedFilingForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.FclBLForm;

public class FclBLAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBLForm fclBLForm = (FclBLForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        String forward = "";
        String buttonValue = fclBLForm.getButtonValue();
        MessageResources messageResources = getResources(request);
        User user = (User) session.getAttribute("loginuser");
        FclBlDAO fclblDAO = new FclBlDAO();
        FclBl fclBl = new FclBl();
        FclBlBC fclBlBC = new FclBlBC();
        Quotation quotation = new Quotation();
        BookingFcl bookingFcl = new BookingFcl();
        DBUtil dbUtil = new DBUtil();
        CustAddressBC custAddressBC = new CustAddressBC();
        ScanBC scanBC = new ScanBC();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Integer userId = 0;
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();

        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if ("02".equals(companyCode)) {
            request.setAttribute("companyCode", "OTIC");
        } else {
            request.setAttribute("companyCode", "ECCI");
        }
        if (request.getParameter("newFclBL") != null) {
            fclBl.setBlBy(user.getLoginName());
            fclBl.setBolDate(new Date());
            DateUtils.formatDate(new Date(), "");
            CustAddress custAddress = custAddressBC.getCustInfo("", "Z");
            String address = null;
            if (null != session.getAttribute(ImportBc.sessionName)) {
                fclBl.setHouseConsigneeName("");
                fclBl.setHouseConsignee("");
                fclBl.setHouseConsigneeAddress("");
            } else if (custAddress != null) {
                address = custAddressBC.getCustomerAddress(custAddress);
                fclBl.setHouseConsigneeName(custAddress.getAcctName());
                fclBl.setHouseConsignee(custAddress.getAcctNo());
                fclBl.setHouseConsigneeAddress(address);
            }
            fclBl.setForwardingAgentName(FclBlConstants.DEFAULT_FREIGHT_FORWARDER_NAME);
            fclBl.setForwardAgentNo(FclBlConstants.DEFAULT_FREIGHT_FORWARDER_NO);
            fclBl.setImportFlag((null != session.getAttribute(ImportBc.sessionName)) ? "I" : "");
            request.setAttribute(FclBlConstants.FCLBL, fclBl);
            request.setAttribute("sedFilingForm", new SedFilingForm());

            return mapping.findForward("edit");
        }
        if (request.getParameter("paramid") != null) {
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            userId = user.getUserId();
            String paramId = request.getParameter("paramid");
            fclBl = fclblDAO.findById(Integer.parseInt(paramId));
            session.setAttribute("check", "edit");
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            if (fclBl.getFileNo() != null) {
                String returnResult = processInfoBC.cheackFileNumberForLoack(fclBl.getFileNo(), userId.toString(), FclBlConstants.FCLBL);
                if (CommonFunctions.isNotNull(returnResult)) {
                    // setting request.
                    request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult);
                }
            }
            request.setAttribute(FclBlConstants.NOTICE_NO_LIST, fclBlBC.getCorrectionList(fclBl.getBolId()));
            //--GETTING ALL COST AND CHARGES DETAILS------
            fclBlBC.SetRequestForFclChargesandCostCode(request, fclBl, buttonValue, messageResources, false);

            TransactionBean transactionBean = new TransactionBean();
            if (fclBl != null) {
                //--GETTING ALL CONTAINER DETAILS------
                request.setAttribute("fclBlContainerList", fclBlBC.getFclBlContrainerList(fclBl));
                request.setAttribute("aa", "bb");
                if ("P".equals(fclBl.getStreamShipBl())) {
                    transactionBean.setStreamShipBL("P");
                } else if ("C".equals(fclBl.getStreamShipBl())) {
                    transactionBean.setStreamShipBL("C");
                } else if ("T".equals(fclBl.getStreamShipBl())) {
                    transactionBean.setStreamShipBL("T");
                }
                if ("P-Prepaid".equals(fclBl.getHouseBl())) {
                    transactionBean.setHouseBL("P");
                } else if ("C-Collect".equals(fclBl.getHouseBl())) {
                    transactionBean.setHouseBL("C");
                } else if ("T-Third Party".equals(fclBl.getHouseBl())) {
                    transactionBean.setHouseBL("T");
                }
                if (fclBl.getDestinationChargesPreCol() == null) {
                    transactionBean.setDestinationChargesPreCol("C");
                }
                transactionBean.setBillToCode(fclBl.getBillToCode());
                if (fclBl.getOPrinting() == null) {
                    transactionBean.setOriginalBL("Y");
                }
                transactionBean.setOPrinting("Yes");
                if (fclBl.getNPrinting() == null) {
                    transactionBean.setNPrinting("Yes");
                }
                if (fclBl.getBLPrinting() == null) {
                    transactionBean.setBLPrinting("Yes");
                }
                if (fclBl.getFileType() == null) {
                    transactionBean.setFileType("S");
                }
                if (fclBl.getImportsFreightRelease() == null) {
                    transactionBean.setImportsFreightRelease("No");
                }
                if (fclBl.getInsurance() == null) {
                    transactionBean.setInsurance("No");
                }
                if (fclBl.getOriginalBlRequired() == null) {
                    transactionBean.setOriginalBlRequired("No");
                }
                if (fclBl.getSsBldestinationChargesPreCol() == null) {
                    transactionBean.setSsBldestinationChargesPreCol("P");
                }
                if (fclBl.getRoutedAgentCheck() == null) {
                    transactionBean.setRoutedAgentCheck("off");
                }
            }
            //---TO SET QUOTEBY AND BOOKINGBY AND CREATION DATE----
            quotation = fclBlBC.getQuoteByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));
            if (quotation != null && quotation.getHazmat() != null && quotation.getHazmat().equalsIgnoreCase("Y")) {
                request.setAttribute("message", "HAZARDOUS CARGO");
                request.setAttribute("hazmatidentity", "hazmatidentity");
                fclBl.setHazmat("Y");
            }
            if (quotation != null) {
                fclBl.setQuoteBy(quotation.getQuoteBy());
                fclBl.setQuoteDate(quotation.getQuoteDate());
            }
            bookingFcl = fclBlBC.getBookingByFileNo(fclBlBC.getFileNumber(fclBl.getFileNo()));

            if (bookingFcl != null) {
                fclBl.setBookingBy(bookingFcl.getBookedBy());
                fclBl.setBookingDate(bookingFcl.getBookingDate());
            }
            if (fclBlBC.finLatestBl(fclBl.getBolId()) != null) {
                fclBl.setLatesBolId(fclBlBC.finLatestBl(fclBl.getBolId()));
            }
            List scanList = scanBC.findScanConfigByScreenName("I".equalsIgnoreCase(fclBl.getFileType()) ? "IMPORT FILE" : "FCLFILE", fclBl.getFileNo());
            boolean flag = false;
            for (Iterator iterator = scanList.iterator(); iterator.hasNext();) {
                ScanConfig scanConfig = (ScanConfig) iterator.next();
                if (scanConfig.getDocumentName() != null && scanConfig.getDocumentName().equalsIgnoreCase("SS LINE MASTER BL")) {
                    if (scanConfig.getTotalScan() != null && !scanConfig.getTotalScan().equals("")) {
                        int scanCount = Integer.parseInt(scanConfig.getTotalScan());
                        int attachCount = Integer.parseInt(scanConfig.getTotalAttach());
                        if (scanCount > 0 || attachCount > 0) {
                            fclBl.setMaster("Yes");
                            flag = true;
                            new ScanDAO().updateMasterReceived(fclBl.getFileNo(), "Yes");
                            break;
                        }
                    }
                }
            }
            if (!flag) {
                fclBl.setMaster("No");
                new ScanDAO().updateMasterReceived(fclBl.getFileNo(), "No");
            }
            //--GET  SHIPPERNAME AND ADDRESS FROM CUSTADDRESS WHERE ACCOUNTTYPE IS 'Z'-----
            String address = "";
            CustAddress custAddress = custAddressBC.getCustInfo("", "Z");
            if (null != custAddress) {
                address = custAddressBC.getCustomerAddress(custAddress);
            }
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                if (null == fclBl.getHouseConsigneeName() || fclBl.getHouseConsigneeName().equals("")) {
                    fclBl.setHouseConsigneeName(custAddress.getAcctName());
                }
                if (null == fclBl.getHouseConsignee() || fclBl.getHouseConsignee().equals("")) {
                    fclBl.setHouseConsignee(custAddress.getAcctNo());
                }
                if (null == fclBl.getHouseConsigneeAddress() || fclBl.getHouseConsigneeAddress().equals("")) {
                    fclBl.setHouseConsigneeAddress(address);
                }
            } else {
                if (null == fclBl.getHouseShipperName() || fclBl.getHouseShipperName().equals("")) {
                    fclBl.setHouseShipperName(custAddress.getAcctName());
                }
                if (null == fclBl.getHouseShipper() || fclBl.getHouseShipper().equals("")) {
                    fclBl.setHouseShipper(custAddress.getAcctNo());
                }
                if (null == fclBl.getHouseShipperAddress() || fclBl.getHouseShipperAddress().equals("")) {
                    fclBl.setHouseShipperAddress(address);
                }
            }
            //-- Get Schedule B details--//
            if (CommonUtils.isNotEmpty(fclBl.getFileNo())) {
                List list = new SedFilingsDAO().findByDr(fclBl.getFileNo());
                List<SedFilings> aesList = new ArrayList<SedFilings>();
                for (Object object : list) {
                    SedFilings sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                    aesList.add(sedFilings);
                }
                request.setAttribute("aesList", aesList);
            }
            request.setAttribute(FclBlConstants.TRANSACTIONBEAN, transactionBean);
            request.setAttribute(FclBlConstants.FCLBL, fclBl);
            String fileNumber = fclBl.getFileNo();
            String fileType = fclBl.getFileType();
            if (CommonFunctions.isNotNull(fileNumber)) {
                String screenName = "I".equalsIgnoreCase(fileType) ? "IMPORT FILE" : "FCLFILE";
                request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "", "Scan or Attach"));
                request.setAttribute("ManualNotes", new NotesDAO().isManualNotes(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
                request.setAttribute("MasterScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL", "Scan or Attach"));
                request.setAttribute("MasterStatus", documentStoreLogDAO.getSsMasterStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL"));
                request.setAttribute("HouseBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "AGENT HOUSE BL"));
                request.setAttribute("OrginBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "ORIGIN INVOICE"));
                request.setAttribute("newBl", dbUtil.checkNewBl(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
            }
            return mapping.findForward("edit");
        } else if (request.getParameter("param") != null) {
            session.setAttribute("view", "3");
            String paramId = request.getParameter("param");
            fclBl = fclblDAO.findById(Integer.parseInt(paramId));
            TransactionBean transactionBean = new TransactionBean();
            if (fclBl.getHouseBl().equals("P-Prepaid")) {
                transactionBean.setHouseBL("P");
            } else if (fclBl.getHouseBl().equals("C-Collect")) {
                transactionBean.setHouseBL("C");
            } else if (fclBl.getHouseBl().equals("B-Both")) {
                transactionBean.setHouseBL("B");
            } else if (fclBl.getHouseBl().equals("T-Third Party")) {
                transactionBean.setHouseBL("T");
            }

            if (fclBl.getStreamShipBl().equals("P-Prepaid")) {
                transactionBean.setStreamShipBL("P");
            } else if (fclBl.getStreamShipBl().equals("C-Collect")) {
                transactionBean.setStreamShipBL("C");
            } else if (fclBl.getStreamShipBl().equals("T-Third Party")) {
                transactionBean.setStreamShipBL("T");
            }
            request.setAttribute(FclBlConstants.TRANSACTIONBEAN, transactionBean);
            request.setAttribute(FclBlConstants.FCLBL, fclBl);
            String fileNumber = fclBl.getFileNo();
            String fileType = fclBl.getFileType();
            if (CommonFunctions.isNotNull(fileNumber)) {
                String screenName = "I".equalsIgnoreCase(fileType) ? "IMPORT FILE" : "FCLFILE";
                request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "", "Scan or Attach"));
                request.setAttribute("ManualNotes", new NotesDAO().isManualNotes(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
                request.setAttribute("MasterScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL", "Scan or Attach"));
                request.setAttribute("MasterStatus", documentStoreLogDAO.getSsMasterStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "SS LINE MASTER BL"));
                request.setAttribute("HouseBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "AGENT HOUSE BL"));
                request.setAttribute("OrginBlStatus", documentStoreLogDAO.getHouseBlStatusOrOrginStatus(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length()), screenName, "ORIGIN INVOICE"));
                request.setAttribute("newBl", dbUtil.checkNewBl(fileNumber.substring(0, -1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") : fileNumber.length())));
            }
            return mapping.findForward("edit");
        } else if (null != buttonValue && (buttonValue.equalsIgnoreCase("searchDisputedBl") || buttonValue.equalsIgnoreCase("ackDisputedBl"))) {
            if ("ackDisputedBl".equals(buttonValue)) {
                String ackComments = "ACKNOWLEDGED BY---->" + user.getLoginName() + "   ON----->" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy hh:mm a");
                new DocumentStoreLogDAO().updateAck(ackComments, fclBLForm.getDisputedFileNo());
            }

            List resultedList = new ArrayList();
            List<Object[]> disputedBlList = new ArrayList<Object[]>();
            StringFormatter stringFormatter = new StringFormatter();
            //--GET THE LIST OF DISPUTED BL BASED ON CRITERIA-----
            disputedBlList = fclBlBC.getDisputedBL(fclBLForm, CommonConstants.SEARCH_BY_DISPUTED);
            for (Object[] objects : disputedBlList) {
                FclBl fclbl = new FclBl();
                fclbl.setFileNo((String) objects[0]);
                fclbl.setTerminal((String) objects[1]);
                fclbl.setFinalDestination((String) objects[2]);
                fclbl.setPortOfLoading((String) objects[3]);
                fclbl.setPortofDischarge((String) objects[4]);
                fclbl.setScanComments((String) objects[5]);
                fclbl.setSslineName((String) objects[8]);
                fclbl.setStatus((String) objects[13]);
                fclbl.setEta(null != objects[6] ? DateUtils.parseDate(objects[6].toString(), "yyyy-MM-dd") : null);
                fclbl.setSailDate(null != objects[7] ? DateUtils.parseDate(objects[7].toString(), "yyyy-MM-dd") : null);
                fclbl.setOriginCode(stringFormatter.getBreketValue((String) objects[1]));
                fclbl.setDestinationCode(stringFormatter.getBreketValue((String) objects[2]));
                fclbl.setPolCode(stringFormatter.getBreketValue((String) objects[3]));
                fclbl.setPodCode(stringFormatter.getBreketValue((String) objects[4]));
                String ackComments = new DocumentStoreLogDAO().getAckComments(fclbl.getFileNo());
                fclbl.setAckComments(ackComments);
                fclbl.setBol((Integer) objects[11]);
                fclbl.setHouseBl((String) objects[12]);
                fclbl.setQuuoteNo(new QuotationDAO().findQuoteId((String) objects[0]));
                fclbl.setBookingNo(new BookingFclDAO().getBookingId((String) objects[0]));
                resultedList.add(fclbl);
            }
            //Giving access to acknowledge disputed bl only for certain roles
            String userRole = null != user ? user.getRole().getRoleDesc() : "";
            RoleDuty roleDuty = (RoleDuty) session.getAttribute("roleDuty");
            if (null != roleDuty && roleDuty.isAccessDisputedBlNotesAndAck()) {
                request.setAttribute(CommonConstants.ACKNOWLEDGE, CommonConstants.ACKNOWLEDGE);
            }
            request.setAttribute("userId", user.getUserId());
            request.setAttribute("itemId", new ItemDAO().getItemId("Quotes, Bookings, and BLs"));
            request.setAttribute("selectedMenu", "Exports");
            request.setAttribute(CommonConstants.DISPUTED_LIST, resultedList);
            request.setAttribute(CommonConstants.FCLBL_FORM, fclBLForm);
            forward = "goToDisputedFclBlJsp";
        } /*......CONTROL COMING FROM ITEM_MATER TABLE WHEN WE HIT ON 'BL CORRECTION NOTICE POOL' MENU
         TO GET DISPUTED BL JSP..........*/ else {
            forward = "goToDisputedFclBlJsp";
        }
        return mapping.findForward(forward);
    }
}

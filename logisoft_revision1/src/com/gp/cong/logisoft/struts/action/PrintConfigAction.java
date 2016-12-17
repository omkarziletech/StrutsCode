package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.bc.accounting.ARInvoiceBC;
import com.gp.cong.logisoft.bc.accounting.ArRedInvoiceBC;
import com.gp.cong.logisoft.bc.fcl.BookingFclBC;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuoteDwrBC;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PrintConfig;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.struts.form.PrintConfigForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrintConfigAction extends Action implements LclCommonConstant {

    final String PRINT_LIST_PAGE = "printListPage";
    final String PRINT_PAGE = "printPage";
    final String PRINT_FORM = "printForm";
    final String PAGE_ACTION_PRINT = "print";
    final String PAGE_ACTION_EMAIL_ME = "emailMe";
    final String PAGE_ACTION_FAX = "fax";
    final String REQUEST_MAP = "requestMap";
    final String PRINT_CONFIG_LIST = "printConfigList";
    final String PRINT_QUOTE = "Econo Quick Rate Quote";
    final String PRINT_BKG = "BOOKING";
    final String PRINT_CMP = "ECI";
    User user = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintConfigDAO printConfigDAO = new PrintConfigDAO();
        PrintConfigBC printConfigBC = new PrintConfigBC();
        BookingFclBC bookingFclBC = new BookingFclBC();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        FclBlBC fclBlBC = new FclBlBC();
        String fileNo = "";
        String spmailId = "";
        boolean emailSp = false;
        String module = "";
        String issuingTerminal = "";
        String importflag = "";
        String subject = "";
        String fclBrand = "";
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        PrintConfigForm printConfigForm = (PrintConfigForm) form;
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("loginuser");
        boolean importFlag = (null != session.getAttribute(ImportBc.sessionName));
        String screenName = request.getParameter("screenName");
        String filesToPrint = (null != request.getParameter("filesToPrint") && !request.getParameter("filesToPrint").equals("")) ? request.getParameter("filesToPrint") : "";
        //Get Report Location
        String pageAction = null;
        request.setAttribute("loginUserEmail", new UserDAO().getEmail(user.getUserId()));
        request.setAttribute("userTerminalPhoneNo", new UserDAO().getUserValueWithUserId(user.getUserId().toString(), "phnnum1"));
        request.setAttribute("userTerminalFaxNo", new UserDAO().getUserValueWithUserId(user.getUserId().toString(), "faxnum1"));
        ArRedInvoice arRedInvoice = null;
        if (null != printConfigForm) {
            pageAction = printConfigForm.getPageAction();
            if (null == pageAction || pageAction.trim().equals("")) {
                Map<String, String> requestMap = new HashMap<String, String>();
                Set<String> requestNames = request.getParameterMap().keySet();
                for (String requestName : requestNames) {
                    requestMap.put(requestName, request.getParameter(requestName));
                }
                session.setAttribute(REQUEST_MAP, requestMap);
                session.removeAttribute("changeInTerminalEmail");

                String fileNumberId = requestMap.get("fileId");
                String arInvoiceId = requestMap.get("arInvoiceId");
                String company = "", property = "", brandValue = "";
                if (CommonUtils.isNotEmpty(arInvoiceId)) {
                    arRedInvoice = new ArRedInvoiceBC().getArRedInvoice(Integer.valueOf(arInvoiceId));
                }
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    if (CommonUtils.isNotEmpty(screenName) && "LCLImpUnits".equalsIgnoreCase(screenName)) {
                        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                        String originActtNo = tradingPartnerDAO.getOriginAgentAcctNo(fileNumberId);
                        brandValue = tradingPartnerDAO.getBusinessUnit(originActtNo);
                    } else {
                        brandValue = new LclFileNumberDAO().getBusinessUnit(fileNumberId);
                    }
                } else if (null != arRedInvoice) {
                    if ("LCLI DR".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                        brandValue = new LclFileNumberDAO().getBusinessUnit(arRedInvoice.getFileNo());
                    } else if ("IMP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                        brandValue = new TradingPartnerDAO().getBusinessUnit(new LclUnitSsDAO().getUnitOriginAcct(arRedInvoice.getFileNo()));
                    }
                }
                if (CommonUtils.isNotEmpty(brandValue)) {
                    property = brandValue.equalsIgnoreCase("ECI") ? "application.Econo.companyname"
                            : brandValue.equalsIgnoreCase("ECU") ? "application.ECU.companyname" : "application.OTI.companyname";
                    company = LoadLogisoftProperties.getProperty(property);
                    printConfigForm.setBrandCompanyName(company);
                    printConfigForm.setBrand(brandValue);
                    request.setAttribute("applicationEmailCompanyName", company);
                } else {
                    String code = new SystemRulesDAO().getSystemRules("CompanyCode");
                    company = LoadLogisoftProperties.getProperty(code.equalsIgnoreCase("03") ? "application.Econo.companyname" : "application.OTI.companyname");
                    brandValue = code.equalsIgnoreCase("03") ? "ECI" : "OTI";
                    printConfigForm.setBrand(brandValue);
                    request.setAttribute("applicationEmailCompanyName", company);
                }

                if (CommonFunctions.isNotNull(request.getParameter("bolNo"))) {
                    //--GETTING THE CONTACTS LIST FOR BL------
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST,
                            fclBlBC.getListOfPartyDetails(request.getParameter("bolNo")));
                    request.setAttribute(FclBlConstants.CODEC_CUSTOMER_LIST,
                            fclBlBC.getCodeCContactList(request.getParameter("bolNo")));
                    request.setAttribute("bolNo", request.getParameter("bolNo"));
                } else if (CommonFunctions.isNotNull(request.getParameter("quotationNo"))) {
                    QuotationBC quotationBC = new QuotationBC();
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST,
                            quotationBC.getListOfPartyDetails(request.getParameter("quotationNo")));
                    fileNo = requestMap.get("fileNo");
                    fileNo = null != fileNo ? fileNo.substring(-1 != fileNo.indexOf("-") ? fileNo.indexOf("-") + 1 : 0) : "";
                    importflag = (null != request.getParameter("importFlag") && !request.getParameter("importFlag").equals("")) ? request.getParameter("importFlag") : "";
                    spmailId = emailschedulerDAO.getSalesPersonMailId(fileNo, importflag);

                    subject = (null != request.getParameter("subject") && !request.getParameter("subject").equals("")) ? request.getParameter("subject") : "";
                    String[] innermodule = subject.split(" ");
                    for (int i = 0; i < innermodule.length; i++) {
                        module = innermodule[0];
                    }
                    issuingTerminal = (null != request.getParameter("issuingTerminal") && !request.getParameter("issuingTerminal").equals("")) ? request.getParameter("issuingTerminal") : "";
                    if (null != spmailId && !spmailId.equals("")) {
                        emailSp = true;
                    }
                    request.setAttribute("emailSp", emailSp);
                    request.setAttribute("spEmailId", spmailId);
                    request.setAttribute(FclBlConstants.CODEC_CUSTOMER_LIST,
                            quotationBC.getCodeCContactList(request.getParameter("quotationNo")));
                    if (CommonUtils.isNotEmpty(new QuotationBC().getIsuingTerminalEmail(request.getParameter("quotationNo")))) {
                        session.setAttribute("changeInTerminalEmail", new QuotationBC().getIsuingTerminalEmail(request.getParameter("quotationNo")));
                    }
                    request.setAttribute("billingTerminal", issuingTerminal);
                    request.setAttribute("module", module);
                    request.setAttribute("importflag", importflag);
                    String terminalNo = issuingTerminal.substring(issuingTerminal.lastIndexOf("-") + 1);
                    RefTerminal refTerminal = new RefTerminalDAO().getTerminal(terminalNo);
                    if (null != refTerminal) {
                        if (null != refTerminal.getDocDeptEmail()) {
                            request.setAttribute("docTerminalEmail", refTerminal.getDocDeptEmail());
                            request.setAttribute("docTerminalName", refTerminal.getDocDeptName());
                        }
                        if (null != refTerminal.getCustomerServiceEmail()) {
                            request.setAttribute("terminalCustomerName", refTerminal.getCustomerServiceName());
                            request.setAttribute("terminalCustomerEmail", refTerminal.getCustomerServiceEmail());
                        }
                    }
                } else if (CommonFunctions.isNotNull(request.getParameter("bookingId"))) {
                    //--GETTING THE CONTACTS LIST FOR BOOKING------
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST,
                            bookingFclBC.getListOfPartyDetails(request.getParameter("bookingId")));
                    request.setAttribute(FclBlConstants.CODEC_CUSTOMER_LIST,
                            bookingFclBC.getCodeCContactList(request.getParameter("bookingId")));
                    issuingTerminal = (null != request.getParameter("issuingTerminal") && !request.getParameter("issuingTerminal").equals("")) ? request.getParameter("issuingTerminal") : "";
                    importflag = (null != request.getParameter("moduleName") && !request.getParameter("moduleName").equals("")) ? request.getParameter("moduleName") : "";
                    subject = (null != request.getParameter("subject") && !request.getParameter("subject").equals("")) ? request.getParameter("subject") : "";
                    String[] innermodule = subject.split(" ");
                    for (int i = 0; i < innermodule.length; i++) {
                        module = innermodule[0];
                    }

//                                        if(CommonUtils.isNotEmpty(request.getParameter("toAddress"))){
//                                            request.setAttribute("clientEmail",request.getParameter("toAddress"));
//                                        }else if(CommonUtils.isNotEmpty(request.getParameter("fileNo"))){
//                                            String fileNo = (String)request.getParameter("fileNo");
//                                            fileNo = fileNo.substring(fileNo.indexOf("-")+1);
//                                            Quotation quotation = new QuotationDAO().getFileNoObject(Integer.parseInt(fileNo));
//                                            if(null != quotation){
//                                                request.setAttribute("clientEmail",quotation.getEmail1());
//                                            }
//                                        }
                    request.setAttribute("fileNo", request.getParameter("fileNo"));
                    request.setAttribute("bookingId", request.getParameter("bookingId"));
                    request.setAttribute("billingTerminal", issuingTerminal);
                    request.setAttribute("module", module);
                    request.setAttribute("importflag", importflag);

                    String terminalNo = issuingTerminal.substring(issuingTerminal.lastIndexOf("-") + 1);
                    RefTerminal refTerminal = new RefTerminalDAO().getTerminal(terminalNo);
                    if (null != refTerminal) {
                        if (null != refTerminal.getDocDeptEmail()) {
                            request.setAttribute("docTerminalEmail", refTerminal.getDocDeptEmail());
                            request.setAttribute("docTerminalName", refTerminal.getDocDeptName());
                        }
                        if (null != refTerminal.getCustomerServiceEmail()) {
                            request.setAttribute("terminalCustomerName", refTerminal.getCustomerServiceName());
                            request.setAttribute("terminalCustomerEmail", refTerminal.getCustomerServiceEmail());
                        }
                    }
                }

                if (screenName.equals("LCLQuotation") || screenName.equals("LCLBooking") || screenName.equals("LCLBL")
                        || screenName.equals("LCLSSMaster") || screenName.equals("LCLUnits") || screenName.equals("LclCreditDebitNote")) {
                    issuingTerminal = (null != printConfigForm.getIssuingTerminal() && !printConfigForm.getIssuingTerminal().equals("")) ? printConfigForm.getIssuingTerminal() : "";
                    request.setAttribute("billingTerminal", issuingTerminal);
                    String terminalNo = issuingTerminal.substring(issuingTerminal.lastIndexOf("/") + 1);
                    RefTerminal refTerminal = new RefTerminalDAO().getTerminal(terminalNo);
                    if (null != refTerminal) {
                        if (null != refTerminal.getLclDocDeptEmail()) {
                            request.setAttribute("docTerminalEmail", refTerminal.getLclDocDeptEmail());
                            request.setAttribute("docTerminalName", refTerminal.getLclDocDeptName());
                        }
                        if (null != refTerminal.getLclCustomerServiceEmail()) {
                            request.setAttribute("terminalCustomerName", refTerminal.getLclCustomerServiceName());
                            request.setAttribute("terminalCustomerEmail", refTerminal.getLclCustomerServiceEmail());
                        }
                        request.setAttribute("phoneTerminalNo", refTerminal.getPhnnum1());
                        request.setAttribute("faxTerminalNo", refTerminal.getFaxnum1());
                    }
                }

                if (null != request.getParameter("bolNo") && !"".equals(request.getParameter("bolNo"))
                        && (!CommonFunctions.isNotNull(request.getParameter("noticeNo")))) {
                    List correctionList = fclBlBC.getCorrectionList(request.getParameter("bolNo"));
                    issuingTerminal = (null != request.getParameter("billingTerminal") && !request.getParameter("billingTerminal").equals("")) ? request.getParameter("billingTerminal") : "";
                    importflag = (null != request.getParameter("importFlag") && !request.getParameter("importFlag").equals("")) ? request.getParameter("importFlag") : "";
                    subject = (null != request.getParameter("subject") && !request.getParameter("subject").equals("")) ? request.getParameter("subject") : "";
                    String[] innermodule = subject.split(" ");
                    for (int i = 0; i < innermodule.length; i++) {
                        module = innermodule[0];
                    }
                    String terminalNo = issuingTerminal.substring(issuingTerminal.lastIndexOf("-") + 1);
                    RefTerminal refTerminal = new RefTerminalDAO().getTerminal(terminalNo);
                    if (null != refTerminal) {
                        if (null != refTerminal.getDocDeptEmail()) {
                            request.setAttribute("docTerminalEmail", refTerminal.getDocDeptEmail());
                            request.setAttribute("docTerminalName", refTerminal.getDocDeptName());
                        }
                        if (null != refTerminal.getCustomerServiceEmail()) {
                            request.setAttribute("terminalCustomerName", refTerminal.getCustomerServiceName());
                            request.setAttribute("terminalCustomerEmail", refTerminal.getCustomerServiceEmail());
                        }
                    }

                    request.setAttribute("RadioButtonCheck", (String) session.getAttribute("TempBolId"));
                    Collections.sort(correctionList);
                    request.setAttribute(FclBlConstants.NOTICE_NO_LIST, correctionList);
                    // setting list into request to display all email and Fax
                    session.setAttribute(CommonConstants.PRINT_LIST, getRequiredList(request, request.getParameter("bolNo"), importFlag, filesToPrint));
                    request.setAttribute("fileNo", request.getParameter("fileNo"));
                    request.setAttribute("bookingId", request.getParameter("bolNo"));
                    request.setAttribute("billingTerminal", issuingTerminal);
                    request.setAttribute("module", module);
                    request.setAttribute("importflag", importFlag);

                } else if (CommonUtils.isEqualIgnoreCase(request.getParameter("action"), "CorrectedFreightInvoice")) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.setFrieghtInvoiceParty(request, session));
                } else if (CommonUtils.isNotEmpty(request.getParameter("CreditDebitNotePrint"))) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.getCreditDebitNotePrint(
                            request.getParameter("noticeNo"), request.getParameter("CreditDebitNotePrint"), screenName,
                            request.getParameter("selectedMenu")));
                } else if (null != request.getParameter("cutomerNumber") && !"".equals(request.getParameter("cutomerNumber"))) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.setFrieghtInvoiceParty(request, session));
                } else if (null != request.getParameter("action") && request.getParameter("action").equalsIgnoreCase("search")) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigDAO.findAllPrintConfig());//                                       
                    return mapping.findForward(PRINT_CONFIG_LIST);
                } else if (CommonFunctions.isNotNull(request.getParameter("arInvoice"))) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.setArInvoicePrintList(request, session));
                } else if (CommonUtils.isEqual(screenName, "LCLBooking")) {
                    String cob = request.getParameter("cob");
                    List<PrintConfig> printList = printConfigBC.findPrintConfigByScreenName(request.getParameter("screenName"), null, user.getUserId());
                    if (!"true".equalsIgnoreCase(cob)) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"), "COB Notification");
                        printList.remove(printConfig);
                    }
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST, lclBookingDAO.getListOfLCLPartyDetails(request.getParameter("fileId")));
                    request.setAttribute(FclBlConstants.CODEJ_CUSTOMER_LIST, lclBookingDAO.getCodeJContactList(request.getParameter("fileId")));
                    session.setAttribute(CommonConstants.PRINT_LIST, printList);
                } else if ((CommonUtils.isEqual(screenName, "LCLQuotation")) || (CommonUtils.isEqual(screenName, "Quotation"))) {
                    if ((CommonUtils.isEqual(screenName, "LCLQuotation"))) {
                        GenericCode salesPerson = emailschedulerDAO.getSalesPersonDetails(Long.parseLong(request.getParameter("fileId")));
                        if (null != salesPerson) {
                            request.setAttribute("salesPerson", salesPerson);
                            request.setAttribute("spEmailId", salesPerson.getField3());
                        }
                    } else if (CommonUtils.isEqual(screenName, "Quotation")) {
                        GenericCode salesPerson = emailschedulerDAO.getSalesPersonDetailsForFcl(fileNo, importflag);
                        if (null != salesPerson) {
                            request.setAttribute("salesPerson", salesPerson);
                            request.setAttribute("spEmailId", salesPerson.getField3());
                        }
                    }
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST, lclQuoteDAO.getListOfLCLPartyDetails(request.getParameter("fileId")));
                    request.setAttribute(FclBlConstants.CODEJ_CUSTOMER_LIST, lclQuoteDAO.getCodeJContactList(request.getParameter("fileId")));
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.findPrintConfigByScreenName(request.getParameter("screenName"), null, user.getUserId()));
                } else if (CommonUtils.isEqual(screenName, "LCLIMPBooking")) {
                    request.setAttribute("lclDocumentName", request.getParameter("pdfDocumentName"));
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST, lclBookingDAO.getListOfLCLPartyDetails(request.getParameter("fileId")));
                    request.setAttribute(FclBlConstants.CODEF_CUSTOMER_LIST, lclBookingDAO.getCodeFContactList(request.getParameter("fileId")));
                    request.setAttribute(FclBlConstants.CODEF_NAME_EMAIL_LIST, printConfigBC.getEmailAndName(request.getParameter("fileId")));
                    LclUnitSsImportsDAO lclUnitSsImportsDAO = new LclUnitSsImportsDAO();
                    String cons[] = lclUnitSsImportsDAO.getEmail1OfHouseConsignee(request.getParameter("fileId"), "cons");
                    request.setAttribute(FclBlConstants.HOUSE_CONSIGNEE_EMAIL1, cons[0]);
                    request.setAttribute(FclBlConstants.HOUSE_CONSIGNEE_FAX, cons[1]);
                    String notify[] = lclUnitSsImportsDAO.getEmail1OfHouseConsignee(request.getParameter("fileId"), "noty");
                    request.setAttribute(FclBlConstants.NOTIFY_PARTY_EMAIL1, notify[0]);
                    request.setAttribute(FclBlConstants.NOTIFY_PARTY_FAX, notify[1]);
                    String notify2[] = lclUnitSsImportsDAO.getEmail1OfNotify2(request.getParameter("fileId"));
                    request.setAttribute(FclBlConstants.NOTIFY2_PARTY_EMAIL1, notify2[0]);
                    request.setAttribute(FclBlConstants.NOTIFY2_PARTY_FAX, notify2[1]);
                    if (null != request.getParameter("unitSsId") && !"".equalsIgnoreCase(request.getParameter("unitSsId"))) {
                        String coloaderValues[] = lclUnitSsImportsDAO.getEmail1OfColoader(request.getParameter("unitSsId"));
                        request.setAttribute(FclBlConstants.COLOADER_EMAIL1, coloaderValues[0]);
                        request.setAttribute(FclBlConstants.COLOADER_FAX, coloaderValues[1]);
                        String cfsDevValues[] = lclUnitSsImportsDAO.getManagerOfCFSWarse(request.getParameter("unitSsId"));
                        request.setAttribute(FclBlConstants.CFS_DEV_WARHSE_MANAGER_EMAIL, cfsDevValues[0]);
                        request.setAttribute(FclBlConstants.CFS_DEV_WARHSE_MANAGER_FAX, cfsDevValues[1]);
                    }
                    boolean statusUpdateOnly = Boolean.valueOf(request.getParameter("statusUpdateOnly"));
                    if (statusUpdateOnly) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"), "Pre Advice/Arrival Notice/Status Update");
                        if (CommonUtils.isNotEmpty(printConfig.getPrinterName())) {
                            printConfig.setEnableDisablePrint("Yes");
                            printConfig.setPrinterName(printConfig.getPrinterName());
                        } else {
                            printConfig.setEnableDisablePrint("No");
                        }
                        List<PrintConfig> printList = new ArrayList<PrintConfig>();
                        printList.add(printConfig);
                        session.setAttribute(CommonConstants.PRINT_LIST, printList);
                    } else {
                        List<PrintConfig> printList = printConfigBC.findPrintConfigByScreenName(request.getParameter("screenName"), null, user.getUserId());
                        //if document is not thirtd party notice then remove third part charge code***
                        List<LclBookingAc> chargeList = new LclCostChargeDAO().getLclCostByFileNumberAsc(Long.parseLong(request.getParameter("fileId")), "Imports");
                        boolean thirdPartyFlag = false;
                        for (LclBookingAc lbac : chargeList) {
                            if (lbac != null) {
                                if (lbac.getArBillToParty() != null) {
                                    if (lbac.getArBillToParty().equalsIgnoreCase("T")) {
                                        thirdPartyFlag = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (thirdPartyFlag == false) {
                            PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"), "Third Party Invoice");
                            printList.remove(printConfig);
                        }
                        session.setAttribute(CommonConstants.PRINT_LIST, printList);
                    }
                } else if (CommonUtils.isEqual(screenName, "LCLImpUnits")) {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.findLclPrintConfigByScreenName(request.getParameter("screenName"), null, user.getUserId(), request.getParameter("transhipment"), ""));
                    request.setAttribute("emailE1F1Map", lclBookingDAO.getAllDrEmailsByUnit(LclCommonConstant.EMAIL_TYPES, LclCommonConstant.FAX_TYPES, false, Long.parseLong(request.getParameter("fileId"))));
                } else if (CommonUtils.isEqualIgnoreCase(screenName, "LCLBL")
                        && CommonUtils.isNotEmpty(request.getParameter("fileId"))) {
                    Long fileId = Long.parseLong(request.getParameter("fileId"));
                    LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
                    List<LCLCorrectionNoticeBean> lclCorrectionNoticeList = lclCorrectionDAO.getAllApprovedCorrectionsByFileId(fileId);
                    request.setAttribute("lclCorrectionNoticeBeanList", lclCorrectionNoticeList);
                    session.removeAttribute("printLclFileId");
                    session.removeAttribute("lclPrintFaxRadioIndex");
                    LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                    if (CommonUtils.isNotEmpty(lclCorrectionNoticeList)) {
                        LCLCorrectionNoticeBean lclCorrectionNoticeBean = lclCorrectionNoticeList.get(0);
                        lclSession.setPrintFaxRadioLclBl(lclCorrectionNoticeBean.getNoticeNo());
                        session.setAttribute("printLclFileId", request.getParameter("fileId"));
                        session.setAttribute("lclPrintFaxRadioIndex", "1");
                        request.setAttribute("lclPrintFaxRadioIndex", session.getAttribute("lclPrintFaxRadioIndex"));
                    } else {
                        lclSession.setPrintFaxRadioLclBl("0");
                    }
                    session.setAttribute("lclSession", lclSession);
                    request.setAttribute("screenName", "LCLBL");
                    List<PrintConfig> exist_printList = printConfigBC.findLclPrintConfigByScreenName(request.getParameter("screenName"),
                            null, user.getUserId(), printConfigForm.getPostedLclBl(), request.getParameter("fileNo"));
                    Object correctionId = lclCorrectionDAO.getLastApprovedFieldsByFileId(fileId, "id");
                    if (correctionId != null) {
                        printConfigForm.setBlCorrectedId(Long.parseLong(correctionId.toString()));
                    }

                    if (!"true".equalsIgnoreCase(request.getParameter("cob"))) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"), "LCL Cargo Confirmed On-Board");
                        exist_printList.remove(printConfig);
                        printConfigForm.setExportFileCob(true);
                    }
                    if (!request.getParameter("postedLclBl").equalsIgnoreCase("postBl")) {
                        String[] documentName = new String[]{"Unrated Bill Of Lading (Original)", "Unrated Bill of Lading (Original UNSIGNED)",
                            "Bill of Lading (Original)", "Bill of Lading (Original UNSIGNED)", "Unrated Bill Of Lading(Non-Negotiable)", "LCL Freight Invoice"};
                        List<PrintConfig> printList = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentNameList(documentName);
                        for (PrintConfig printConfig : printList) {
                            exist_printList.remove(printConfig);
                        }
                    }

                    String fromScreenName = request.getParameter("fromScreen");
                    if (null == fromScreenName) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"),
                                "Freight Invoice");
                        exist_printList.remove(printConfig);
                    } else if (fromScreenName.equalsIgnoreCase("AR_Inquiry")) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"),
                                "LCL Freight Invoice");
                        exist_printList.remove(printConfig);
                        request.setAttribute("lclCorrectionNoticeBeanList", null);
                    }
                    setExportBlPrintList(exist_printList, printConfigForm, fileId);
                    session.setAttribute(CommonConstants.PRINT_LIST, exist_printList);

                } else {
                    session.setAttribute(CommonConstants.PRINT_LIST, printConfigBC.findLclPrintConfigByScreenName(request.getParameter("screenName"),
                            null, user.getUserId(), request.getParameter("transhipment"), request.getParameter("fileNo")));
                }
            }
            if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_PRINT)) {
                savePrintTemplate(printConfigForm.getFileLocation(), printConfigForm.getFileNumber(), null, PAGE_ACTION_PRINT);
                File file = new File(printConfigForm.getFileLocation());
                String displayMessage = file.getName() + " printed Successfully ";
                request.setAttribute("displayMessage", displayMessage);
            } else if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_EMAIL_ME)) {
                savePrintTemplate(printConfigForm.getFileLocation(), printConfigForm.getFileNumber(), null, PAGE_ACTION_EMAIL_ME);
                String displayMessage = "Email Sent Successfully to " + user.getEmail();
                request.setAttribute("displayMessage", displayMessage);
            } else if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_FAX)) {
                String realPath = this.getServlet().getServletContext().getRealPath("/");
                if (null != user && null != user.getLoginName()) {
                    String coverLetter = printConfigBC.createFaxCoverLetter(printConfigForm.getToFaxNumber(),
                            printConfigForm, realPath, user.getLoginName());
                    if (null != coverLetter && !coverLetter.trim().equals("")) {
                        savePrintTemplate(printConfigForm.getFileLocation(), printConfigForm.getFileNumber(), coverLetter, PAGE_ACTION_FAX);
                        String displayMessage = " Fax Sent Successfully to #" + printConfigForm.getToFaxNumber();
                        request.setAttribute("displayMessage", displayMessage);
                    }
                }
            } else if (null != pageAction && pageAction.trim().equals("doNothing")) {
                if (null != session.getAttribute("TempBolId") && !((String) session.getAttribute("TempBolId")).equals("")) {
                    String tempBolId = (String) session.getAttribute("TempBolId");
                    if (tempBolId.indexOf("=") != -1) {
                        tempBolId = tempBolId.substring(0, tempBolId.indexOf("="));
                    }
                    List correctionList = fclBlBC.getCorrectionList(tempBolId);
                    Collections.sort(correctionList);
                    request.setAttribute(FclBlConstants.NOTICE_NO_LIST, correctionList);
                    request.setAttribute(FclBlConstants.CUSTOMER_LIST,
                            fclBlBC.getListOfPartyDetails(tempBolId));
                    request.setAttribute(FclBlConstants.CODEC_CUSTOMER_LIST,
                            fclBlBC.getCodeCContactList(tempBolId));
                    session.setAttribute(CommonConstants.PRINT_LIST, getRequiredList(request, (String) session.getAttribute("TempBolId"), importFlag, filesToPrint));
                    request.setAttribute("RadioButtonCheck", (String) session.getAttribute("TempBolId"));
                    session.removeAttribute("TempBolId");
                    request.setAttribute("noReload", "noReload");
                } else if (CommonUtils.isEqualIgnoreCase(screenName, "LCLBL")
                        && session.getAttribute("printLclFileId") != null) {
                    Long fileId = Long.parseLong((String) session.getAttribute("printLclFileId"));

                    LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
                    List<LCLCorrectionNoticeBean> lclCorrectionNoticeList = lclCorrectionDAO.
                            getAllApprovedCorrectionsByFileId(fileId);
                    request.setAttribute("lclCorrectionNoticeBeanList", lclCorrectionNoticeList);
                    request.setAttribute("lclPrintFaxRadioIndex", session.getAttribute("lclPrintFaxRadioIndex"));
                    List<PrintConfig> exist_printList = printConfigBC.findLclPrintConfigByScreenName(request.getParameter("screenName"),
                            null, user.getUserId(), printConfigForm.getPostedLclBl(), request.getParameter("fileNo"));
                    setExportBlPrintList(exist_printList, printConfigForm, fileId);
                    if (printConfigForm.isExportFileCob()) {
                        PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"),
                                "LCL Cargo Confirmed On-Board");
                        exist_printList.remove(printConfig);
                    }
                    PrintConfig printConfig = new PrintConfigDAO().findPrintConfigByScreenNameAndDocumentName(request.getParameter("screenName"),
                            "Freight Invoice");
                    exist_printList.remove(printConfig);
                    session.setAttribute(CommonConstants.PRINT_LIST, exist_printList);
                }

            } else if (null != pageAction && pageAction.trim().equals("deletePrintConfig")) {
                if (null != printConfigForm.getId()) {
                    PrintConfig printConfig = printConfigDAO.findById(printConfigForm.getId());
                    printConfigDAO.delete(printConfig);
                }
                session.setAttribute(CommonConstants.PRINT_LIST, printConfigDAO.findAllPrintConfig());//                           
                return mapping.findForward(PRINT_CONFIG_LIST);
            } else if (null != pageAction && pageAction.trim().equals("UpdatePrintConfig")) {
                if (null != printConfigForm.getId()) {
                    PrintConfig printConfig = printConfigDAO.findById(printConfigForm.getId());
                    if (null != printConfigForm.getAllowPrint() && printConfigForm.getAllowPrint().equalsIgnoreCase("on")) {
                        printConfig.setAllowPrint("Yes");
                    } else {
                        printConfig.setAllowPrint("No");
                    }
                    printConfigDAO.update(printConfig);
                }
                session.setAttribute(CommonConstants.PRINT_LIST, printConfigDAO.findAllPrintConfig());//                            
                return mapping.findForward(PRINT_CONFIG_LIST);
            }
        }
        printConfigForm.setSystemRule(new SystemRulesDAO().getSystemRules("CompanyName"));
        if (null != request.getParameter("emailSubject")) {
            if (CommonUtils.isEqual(screenName, "LCLQuotation")) {
                printConfigForm.setEmailSubject(request.getParameter("fileNo") + " " + PRINT_QUOTE);
            }
            if (CommonUtils.isEqual(screenName, "LCLBooking")) {
                printConfigForm.setEmailSubject(PRINT_CMP + " " + request.getParameter("fileNo") + " " + PRINT_BKG);
            }
        }
        if (null != request.getParameter("subject")) {
            if (CommonUtils.isEqual(screenName, "LCLQuotation")) {
                printConfigForm.setSubject(request.getParameter("fileNo") + " " + PRINT_QUOTE);
            }
            if (CommonUtils.isEqual(screenName, "LCLBooking")) {
                printConfigForm.setSubject(PRINT_CMP + " " + request.getParameter("fileNo") + " " + PRINT_BKG);
            }
        }
        if (null != request.getParameter("comment")) {
            printConfigForm.setComment(request.getParameter("comment"));

        }
        if (module.equalsIgnoreCase("FCL")) {
            String fileNumber = request.getParameter("fileNo");
//            fileNumber = null != fileNumber ? fileNumber.substring(-1 != fileNumber.indexOf("-") ? fileNumber.indexOf("-") + 1 : 0) : "";
            BookingFcl bookingFcl = new BookingFclDAO().findbyFileNo(fileNumber);
            FclBl fclBl = new FclBlDAO().getOriginalBl(fileNumber);
            if (null != fclBl && null != fclBl.getBrand()) {
                fclBrand = fclBl.getBrand();
            } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
                fclBrand = bookingFcl.getBrand();
            } else {
                fclBrand = new QuoteDwrBC().checkBrandForQuote(request.getParameter("quotationNo"));
            }
        }
        if (CommonUtils.isEmpty(fclBrand) && null != arRedInvoice) {
            BookingFclDAO bookingFclDAO = new BookingFclDAO();
            if ("QUOTE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                fclBrand = bookingFclDAO.getBrandStatus(arRedInvoice.getFileNo(), arRedInvoice.getScreenName());
            } else if ("BOOKING".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                fclBrand = bookingFclDAO.getBrandStatus(arRedInvoice.getFileNo(), arRedInvoice.getScreenName());
            } else if ("BL".equalsIgnoreCase(arRedInvoice.getScreenName())) {
                fclBrand = bookingFclDAO.getBrandStatus(arRedInvoice.getFileNo(), arRedInvoice.getScreenName());
            }
        }
        if (CommonUtils.isNotEmpty(fclBrand)) {
            String property = "", emailSubject = "", brandValue = "";
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            property = "Econo".equalsIgnoreCase(fclBrand) && "03".equalsIgnoreCase(companyCode) ? "application.Econo.companyname"
                    : "OTI".equalsIgnoreCase(fclBrand) && "02".equalsIgnoreCase(companyCode) ? "application.OTI.companyname" : "application.ECU.companyname";
            brandValue = "Econo".equalsIgnoreCase(fclBrand) && "03".equalsIgnoreCase(companyCode) ? "ECI"
                    : "OTI".equalsIgnoreCase(fclBrand) && "02".equalsIgnoreCase(companyCode) ? "OTI" : "ECU";
            printConfigForm.setBrand(brandValue);
            emailSubject = LoadLogisoftProperties.getProperty(property);
            request.setAttribute("applicationEmailCompanyName", emailSubject);
        }
        request.setAttribute(PRINT_FORM, printConfigForm);
        request.setAttribute("user", user);
        request.setAttribute("screenName", screenName);
        request.setAttribute("unitNo", CommonUtils.isNotEmpty(request.getParameter("unitNo")) ? request.getParameter("unitNo") : "");
        return mapping.findForward(PRINT_LIST_PAGE);
    }

    private void savePrintTemplate(String fileLocation, String moduleId,
            String coverLetter, String pageAction) throws Exception {
        ARInvoiceBC aRInvoiceBC = new ARInvoiceBC();
        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
        if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_EMAIL_ME)
                && null != user && null != user.getEmail()) {
            emailSchedulerVO.setToAddress(user.getEmail());
            emailSchedulerVO.setToName(null != user.getLoginName() ? user.getLoginName() : "");
            emailSchedulerVO.setModuleName(CommonConstants.CONTACT_MODE_EMAIL);
            emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_EMAIL);
        } else if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_PRINT)
                && null != user && null != user.getEmail()) {
            emailSchedulerVO.setModuleName(CommonConstants.CONTACT_MODE_PRINT);
            emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_PRINT);
        } else if (null != pageAction && pageAction.trim().equals(PAGE_ACTION_FAX)
                && null != user && null != user.getEmail()) {
            emailSchedulerVO.setModuleName(CommonConstants.CONTACT_MODE_FAX);
            emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_FAX);
            emailSchedulerVO.setCoverLetter(coverLetter);
        }
        emailSchedulerVO.setFileLocation(fileLocation);
        emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
        emailSchedulerVO.setModuleId(moduleId);
        emailSchedulerVO.setHtmlMessage("Check Printing");
        emailSchedulerVO.setSubject("Check Printing");
        if (null != user && null != user.getLoginName()) {
            emailSchedulerVO.setUserName(user.getLoginName());
        }
        emailSchedulerVO.setEmailDate(new Date());
        aRInvoiceBC.save(emailSchedulerVO);
    }

    private List getRequiredList(HttpServletRequest request, String BolNo, boolean importFlag, String filesToPrint) throws Exception {
        List<FclBlCorrections> fclBlCorrectionsList = null;
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclBlChargesDAO fclBlChargesDAO = new FclBlChargesDAO();
        FclBl fclBl = fclBlDAO.findById(BolNo);
        if (BolNo.indexOf("=") != -1) {
            String data[] = BolNo.split("==");
            if (data.length > 1) {
                String tempBolId = data[0];
                String noticeNo = data[1];
                fclBlCorrectionsList = new FclBlCorrectionsDAO().getFclBlCorrections(tempBolId, noticeNo);
            }

        }
        boolean chargeFlag = false;
        if (importFlag) {
            List fclblChargesList = fclBlChargesDAO.findByParentId(fclBl.getBol());
            if (fclblChargesList != null && fclblChargesList.size() > 0) {
                chargeFlag = true;
            }
        }
        boolean shipper = false;
        boolean forwarder = false;
        boolean agent = false;
        boolean thirdParty = false;
        boolean consignee = false;
        boolean notifyParty = false;
        boolean manifest = true;
        boolean manifesting = true;
        boolean steamShip = true;
        boolean freightedNonNeg = true;
        boolean unFreightedNonNeg = true;

        boolean check = false;
        if (!filesToPrint.equals("confirmOnBoard")) {
            if (fclBlCorrectionsList != null) {
                for (FclBlCorrections fclBlCorrections : fclBlCorrectionsList) {
                    String thridPartyName = fclBlCorrections.getBillToParty();
                    if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Shipper")) {
                        shipper = true;
                        check = true;
                    } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Forwarder")) {
                        forwarder = true;
                        check = true;
                    } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Agent")) {
                        agent = true;
                        check = true;
                    } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("ThirdParty")) {
                        thirdParty = true;
                        check = true;
                    } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Consignee")) {
                        consignee = true;
                        check = true;
                    } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("NotifyParty")) {
                        notifyParty = true;
                        check = true;
                    }
                }
            }
            if (!check) {
                List<String> billToList = new FclBlChargesDAO().getDistinctBillTo(fclBl.getBol().toString());
                if (billToList != null) {
                    for (String thridPartyName : billToList) {
                        if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Shipper")) {
                            shipper = true;
                        } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Forwarder")) {
                            forwarder = true;
                        } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Agent")) {
                            agent = true;
                        } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("ThirdParty")) {
                            thirdParty = true;
                        } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("Consignee")) {
                            consignee = true;
                        } else if (null != thridPartyName && thridPartyName.equalsIgnoreCase("NotifyParty")) {
                            notifyParty = true;
                        }
                    }
                }
            }
        } else {
            steamShip = false;
            freightedNonNeg = false;
            unFreightedNonNeg = false;
            manifesting = false;
        }

        if (CommonFunctions.isNotNull(fclBl.getReadyToPost())) {
            manifest = false;
        }
        PrintConfigBC printConfigBC = new PrintConfigBC();
        List printList = new ArrayList();
        if (importFlag) {
            String documents;
            if (chargeFlag != true) {
                documents = CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE_NONRATED + "=";
            } else {
                documents = CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE + "=";
                documents += CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE_NONRATED + "=";
            }
            if (shipper) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER + "=";
            }
            if (forwarder) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER + "=";
            }
            if (agent) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT + "=";
            }
            if (thirdParty) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY + "=";
            }
            if (consignee) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE + "=";
            }
            if (notifyParty) {
                documents += CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY + "=";
            }
            documents += CommonConstants.CONTAINER_RESPONSIBILITY_WAIVER + "=";
            documents += CommonConstants.AUTHORITY_TO_MAKE_ENTRY + "=";
            documents += CommonConstants.DELIVERY_ORDER + "=";

            String[] documentsArray = documents.split("=");
            printList = (List) printConfigBC.findPrintConfigByScreenNameFroImport(request.getParameter("screenName"), documentsArray, user.getUserId());
//                if(CommonUtils.isNotEmpty(fclBl.getBookingBy())){
//                    printList.addAll(printConfigBC.findPrintConfigByScreenName("Booking",null,user.getUserId()));
//                }
        } else {
            String documents = CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE;
            if (!filesToPrint.equals("booking")) {
                printList = (List) printConfigBC.findPrintConfigByScreenNameForExport(request.getParameter("screenName"), null, documents, user.getUserId());
            }
            if (!filesToPrint.equals("confirmOnBoard")) {
                printList.addAll(printConfigBC.findPrintConfigByScreenName("Booking", null, user.getUserId()));
            }
        }
        Map map = new HashMap();
        for (Iterator iterator = printList.iterator(); iterator.hasNext();) {
            PrintConfig printConfig = (PrintConfig) iterator.next();
            map.put(printConfig.getId(), printConfig);
        }
        for (Iterator iterator = printList.iterator(); iterator.hasNext();) {
            PrintConfig printConfig = (PrintConfig) iterator.next();
            if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER)) {
                if (shipper == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER)) {
                if (forwarder == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT)) {
                if (agent == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY)) {
                if (thirdParty == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE)) {
                if (consignee == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY)) {
                if (notifyParty == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.FREIGHTED_ORIGINAL_HOUSE_BL)) {
                if (manifest) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.UNFREIGHTED_ORIGINAL_HOUSE_BL)) {
                if (manifest) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_CONFIRM_ONBOARD_NOTICE)) {
                if (fclBl.getConfirmOnBorad() == null || fclBl.getConfirmOnBorad().equalsIgnoreCase("N")) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_BL_MANIFESTED)) {
                if (manifesting == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_FREIGHTED_NONNEGOTIABLE)) {
                if (freightedNonNeg == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_UNFREIGHTED_NONNEGOTIABLE)) {
                if (unFreightedNonNeg == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_STREAMSHIP_BL)) {
                if (steamShip == false) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DOCUMENT_NAME_AR_INVOICE)) {
                map.remove(printConfig.getId());
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.UNMARKED_HOUSE_BILLOFLADDING)) {
                if (!"M".equalsIgnoreCase(fclBl.getReadyToPost())) {
                    map.remove(printConfig.getId());
                }
            } else if (printConfig.getDocumentName().equalsIgnoreCase(CommonConstants.DELIVERY_ORDER) && !importFlag) {
                map.remove(printConfig.getId());
            }
        }
        List orgPrintList = new ArrayList();
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            Long key = (Long) iter.next();
            orgPrintList.add(map.get(key));
        }

        Collections.sort(orgPrintList);
        return orgPrintList;
    }

    public void setExportBlPrintList(List<PrintConfig> exist_printList,
            PrintConfigForm printConfigForm, Long fileId) throws Exception {
        List<PrintConfig> new_print_list = new ArrayList<PrintConfig>();
        for (PrintConfig printList : exist_printList) {
            if (CommonUtils.in(printList.getDocumentName(), "LCL Freight Invoice", "Freight Invoice")) {
                exist_printList.remove(printList);
                String billToParty = new PrintConfigDAO().getBillToPartyListForExport(fileId);
                if (printConfigForm.getBlCorrectedId() != null && printConfigForm.getBlCorrectedId() != 0) {
                    billToParty = new LCLCorrectionDAO().getCorrectedBillToPartyListForExport(fileId,
                            printConfigForm.getBlCorrectedId());
                }
                if (CommonUtils.isNotEmpty(billToParty) && billToParty.length() > 1) {
                    for (String party : billToParty.split(",")) {
                        PrintConfig new_print = new PrintConfig();
                        PropertyUtils.copyProperties(new_print, printList);
                        new_print.setExportBillToParty(party);
                        new_print_list.add(new_print);
                    }
                } else {
                    printList.setExportBillToParty(billToParty);
                    new_print_list.add(printList);
                }
                break;
            }
        }
        int i = 0;
        for (PrintConfig print : new_print_list) {
            if (!print.getDocumentName().equalsIgnoreCase("Freight Invoice")) {
                exist_printList.add(++i, print);
            } else {
                exist_printList.add(print);
            }
        }
    }
}

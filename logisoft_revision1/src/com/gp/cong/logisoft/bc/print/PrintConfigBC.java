package com.gp.cong.logisoft.bc.print;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.PrintConfig;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UserPrinterAssociation;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.UserPrinterAssociationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.reports.FaxCoverLetterPdfCreator;
import com.gp.cong.logisoft.struts.form.PrintConfigForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class PrintConfigBC {

    private static final Logger log = Logger.getLogger(PrintConfigBC.class);
    PrintConfigDAO printConfigDAO = new PrintConfigDAO();
    SystemRulesDAO systemRuleDAO = new SystemRulesDAO();
    FaxCoverLetterPdfCreator faxCoverLetterPdfCreator = new FaxCoverLetterPdfCreator();

    public List<PrintConfig> findPrintConfigByScreenName(String screenName, String documentId, Integer userId) throws Exception {
        return setAllowedPrint(printConfigDAO.findPrintConfigByScreenName(screenName, documentId), userId);
    }

    public List<PrintConfig> findLclPrintConfigByScreenName(String screenName, String documentId, Integer userId, String transhipment, String fileNo) throws Exception {
        return setAllowedPrint(printConfigDAO.findLclPrintConfigByScreenName(screenName, documentId, transhipment, fileNo), userId);
    }

    public List<PrintConfig> findPrintConfigByScreenNameForExport(String screenName, String documentId, String documentName, Integer userId) throws Exception {
        return setAllowedPrint(printConfigDAO.findPrintConfigByScreenName(screenName, documentId, documentName), userId);
    }

    public List<PrintConfig> findPrintConfigByScreenNameFroImport(String screenName, String[] documentName, Integer userId) throws Exception {
        return setAllowedPrint(printConfigDAO.findPrintConfigByScreenName(screenName, null, documentName), userId);
    }

    public String getSystemRulesByCode(String ruleCode) throws Exception {
        return systemRuleDAO.getSystemRulesByCode(ruleCode);
    }

    public String createFaxCoverLetter(String toFaxNumber, PrintConfigForm printConfigForm, String realPath, String userName) throws Exception {
        String reportLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/Fax_Cover_Letter/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(reportLocation);
        if (!file.exists()) {
            file.mkdirs();
        }
        reportLocation += userName + "_" + CommonConstants.FAX_COVER_LETTER + "_" + toFaxNumber + ".pdf";
        reportLocation = faxCoverLetterPdfCreator.createFaxCoverLetter(toFaxNumber, printConfigForm, reportLocation, realPath);
        if ((new File(reportLocation).exists())) {
            return reportLocation;
        } else {
            return "";
        }
    }

    public boolean updatePrintConfigByScreenName(PrintConfigForm printConfigForm) throws Exception {
        PrintConfig printConfig = new PrintConfig();
        printConfig.setScreenName(printConfigForm.getScreenName());
        printConfig.setFileLocation(printConfigForm.getFileLocation());
        printConfig.setDocumentName(printConfigForm.getDocumentName());
//        printConfig.setDocumentType(printConfigForm.getDocumentType());
        return printConfigDAO.updatePrintConfigByScreenName(printConfig);
    }

    public List<PrintConfig> getCreditDebitNotePrint(String correctionNoticeNumber,
            String blNo, String screenName, String module) throws Exception {
        CreditDebitNoteDAO creditDebitNoteDAO = new CreditDebitNoteDAO();
        List<PrintConfig> printConfigList = new ArrayList();
        List<CreditDebitNote> CreditDebitNoteList = creditDebitNoteDAO.searchThroughCriteria(correctionNoticeNumber, blNo);
        if (CommonFunctions.isNotNullOrNotEmpty(CreditDebitNoteList)) {
            PrintConfig printConfig = null;
            for (CreditDebitNote creditDebitNote : CreditDebitNoteList) {
                printConfig = new PrintConfig();
                printConfig.setDocumentName(creditDebitNote.getCustomerName().replace("'", "") + PrintReportsConstants.DELIMETER + creditDebitNote.getCustomerNumber() + ")"
                        + PrintReportsConstants.DELIMETER + creditDebitNote.getDebitCreditNote().toUpperCase() + ")");
                printConfig.setDocumentType("IN");
                printConfig.setScreenName(screenName);
                if (module != null && module.equalsIgnoreCase("Exports")) {
                    printConfig.setId(creditDebitNote.getId().longValue());
                }
                printConfigList.add(printConfig);
            }
        }
        return printConfigList;
    }

    public List setFrieghtInvoiceParty(HttpServletRequest request, HttpSession session) throws Exception {
        String blNumber = request.getParameter("blId");
        String noticeNumber = request.getParameter("noticeNumber");
        String customerNumber = request.getParameter("cutomerNumber");
        List printList = null;
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclBl fclBl = new FclBl();
        User user = (User) session.getAttribute("loginuser");
        if (CommonFunctions.isNotNull(noticeNumber) && !"null".equals(noticeNumber) && !FclBlConstants.CNA0.equals(noticeNumber)) {
            fclBl = fclBlDAO.findById(blNumber + FclBlConstants.DELIMITER + noticeNumber);
        } else {
            fclBl = fclBlDAO.findById(blNumber);
        }
        // setting update Bolid to list
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        if (fclBl != null) {
            requestMap.put(PrintReportsConstants.BL_ID, fclBl.getBolId());
            if (CommonFunctions.isNotNull(customerNumber)) {
                String[] documents = new String[2];
                if (customerNumber.equalsIgnoreCase(fclBl.getAgentNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getBillTrdPrty())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getForwardAgentNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getShipperNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getConsigneeNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getNotifyParty())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY;
                } else {
                    documents[0] = "";
                }
                if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                    documents[1] = CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE;
                }
                printList = (List) new PrintConfigBC().findPrintConfigByScreenNameFroImport("BL", documents, user.getUserId());
            }
        }
        return printList;

    }

    public List<PrintConfig> getFclBlDocumentsList(HttpServletRequest request, HttpSession session) throws Exception {
        String blNumber = request.getParameter("blId");
        String noticeNumber = request.getParameter("noticeNumber");
        String customerNumber = request.getParameter("cutomerNumber");
        List printList = null;
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclBl fclBl = new FclBl();
        User user = (User) session.getAttribute("loginuser");
        if (CommonFunctions.isNotNull(noticeNumber) && !"null".equals(noticeNumber) && !FclBlConstants.CNA0.equals(noticeNumber)) {
            fclBl = fclBlDAO.findById(blNumber + FclBlConstants.DELIMITER + noticeNumber);
        } else {
            fclBl = fclBlDAO.findById(blNumber);
        }
        // setting update Bolid to list
        Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
        if (fclBl != null) {
            requestMap.put(PrintReportsConstants.BL_ID, fclBl.getBolId());
            if (CommonFunctions.isNotNull(customerNumber)) {
                String[] documents = new String[2];
                if (customerNumber.equalsIgnoreCase(fclBl.getAgentNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_AGENT;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getBillTrdPrty())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_THIRDPARTY;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getForwardAgentNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_FORWARDER;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getShipperNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_SHIPPER;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getConsigneeNo())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_CONSIGNEE;
                } else if (customerNumber.equalsIgnoreCase(fclBl.getNotifyParty())) {
                    documents[0] = CommonConstants.DOCUMENT_NAME_FREIGHT_INVOICE_NOTIFYPARTY;
                } else {
                    documents[0] = "";
                }
                if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                    documents[1] = CommonConstants.DOCUMENT_NAME_FCL_ARRIVALNOTICE;
                }
                printList = (List) new PrintConfigBC().findPrintConfigByScreenNameFroImport("BL", documents, user.getUserId());
            }
        }
        return printList;

    }

    public List setArInvoicePrintList(HttpServletRequest request, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("loginuser");
        String[] documents = new String[1];
        documents[0] = CommonConstants.DOCUMENT_NAME_AR_INVOICE;
        return (List) new PrintConfigBC().findPrintConfigByScreenNameFroImport("BL", documents, user.getUserId());

    }

    private List<PrintConfig> setAllowedPrint(List<PrintConfig> printConfigList, Integer userId) throws Exception {
        UserPrinterAssociationDAO printerAssociationDAO = new UserPrinterAssociationDAO();
        for (PrintConfig printConfig : printConfigList) {
            UserPrinterAssociation printerAssco = printerAssociationDAO.findBy(printConfig.getId(), userId);
            if (null != printerAssco && CommonUtils.isNotEmpty(printerAssco.getPrinterName())) {
                printConfig.setEnableDisablePrint("Yes");
                printConfig.setPrinterName(printerAssco.getPrinterName());
                printConfig.setPrinterTray(CommonUtils.isNotEmpty(printerAssco.getPrinterTray())
                        ? printerAssco.getPrinterTray() : "");
            } else {
                printConfig.setEnableDisablePrint("No");
            }
        }
        return printConfigList;
    }

    public List consolidateExportRates(List<FclBlCharges> chargesList, MessageResources messageResources) throws Exception {
        List newList = new ArrayList();
        List finalList = new ArrayList();
        String interModelRate = "";
        String interModelRate1 = "";
        boolean noAutoOfr = true;
        int k = 0;
        LinkedList sortedList = new LinkedList();
        boolean ofr = false;
        for (FclBlCharges fclBlCharges : chargesList) {
            if (fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                    && CommonUtils.isNotEmpty(fclBlCharges.getReadOnlyFlag())) {
                noAutoOfr = false;
            }
            if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().equals("OCNFRT") || fclBlCharges.getChargeCode().equals("OFIMP")) {
                sortedList.add(k, fclBlCharges);
                ofr = true;
                k++;
            } else {
                sortedList.add(fclBlCharges);
            }
        }
        for (Iterator iterator = sortedList.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            String consolidator = messageResources.getMessage("OceanFreight");
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            if (fclBlCharges.getChargeCode() != null && CommonUtils.in(fclBlCharges.getChargeCode(), colsolidatorRates)) {
                if (fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))) {
                    FclBlCharges charges = new FclBlCharges();
                    PropertyUtils.copyProperties(charges, fclBlCharges);
                    newList.add(charges);
                    flag = true;
                } else {
                    if ("Yes".equalsIgnoreCase(fclBlCharges.getBundleIntoOfr())) {
                        for (Iterator itr = newList.iterator(); itr.hasNext();) {
                            FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                            if (tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                                    && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag())) || noAutoOfr) {
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                flag = true;
                                break;
                            }
                        }
                    } else {
                        interModelFlag = false;
                        if (fclBlCharges.getReadOnlyFlag() != null
                                && CommonUtils.in(fclBlCharges.getChargeCode(), interModelRates)) {
                            interModelFlag = true;
                        }
                        if (interModelFlag) {
                            newList.add(fclBlCharges);
                        }
                        if (!interModelFlag) {
                            newList.add(fclBlCharges);
                            flag = true;
                        }
                    }
                }
            }
            if (!flag && !interModelFlag) {
                if (!ofr && "Yes".equalsIgnoreCase(fclBlCharges.getBundleIntoOfr()) && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODE)
                        && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETWO)
                        && !fclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETHREE)) {
                    newList.add(fclBlCharges);
                } else if (!"Yes".equalsIgnoreCase(fclBlCharges.getBundleIntoOfr())
                        && null != fclBlCharges.getChargeCode()
                        && (fclBlCharges.getChargeCode().equals("DRAY")
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
                        if (tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                                && (CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag()) && !interModel.contains(fclBlCharges.getChargeCode()) || noAutoOfr)) {
                            tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                            tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                            tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                            break;
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

    public List<FclBlCharges> getExportRatesForFclPrintForBounleOfr(List<FclBlCharges> fclRates, MessageResources messageResources, String billTo) throws Exception {
        List<FclBlCharges> chargesList = new ArrayList<FclBlCharges>();
        if (CommonUtils.isNotEmpty(billTo)) {
            for (FclBlCharges charge : fclRates) {
                if (null != charge && CommonUtils.isEqualIgnoreCase(charge.getBillTo(), billTo)) {
                    chargesList.add(charge);
                }
            }
            chargesList = consolidateExportRates(chargesList, messageResources);
        } else {
            List<FclBlCharges> shipperList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> forwarderList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> agentList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> thirdPartyList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> consigneeList = new ArrayList<FclBlCharges>();
            List<FclBlCharges> notifyPartyList = new ArrayList<FclBlCharges>();
            for (FclBlCharges charge : fclRates) {
                if (null != charge) {
                    if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Shipper")) {
                        shipperList.add(charge);
                    } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Forwarder")) {
                        forwarderList.add(charge);
                    } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Agent")) {
                        agentList.add(charge);
                    } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "ThirdParty")) {
                        thirdPartyList.add(charge);
                    } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Consignee")) {
                        consigneeList.add(charge);
                    } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "NotifyParty")) {
                        notifyPartyList.add(charge);
                    }
                }
            }
            if (CommonUtils.isNotEmpty(shipperList)) {
                shipperList = consolidateExportRates(shipperList, messageResources);
                if (CommonUtils.isNotEmpty(shipperList)) {
                    chargesList.addAll(shipperList);
                }
            }
            if (CommonUtils.isNotEmpty(forwarderList)) {
                forwarderList = consolidateExportRates(forwarderList, messageResources);
                if (CommonUtils.isNotEmpty(forwarderList)) {
                    chargesList.addAll(forwarderList);
                }
            }
            if (CommonUtils.isNotEmpty(agentList)) {
                agentList = consolidateExportRates(agentList, messageResources);
                if (CommonUtils.isNotEmpty(agentList)) {
                    chargesList.addAll(agentList);
                }
            }
            if (CommonUtils.isNotEmpty(thirdPartyList)) {
                thirdPartyList = consolidateExportRates(thirdPartyList, messageResources);
                if (CommonUtils.isNotEmpty(thirdPartyList)) {
                    chargesList.addAll(thirdPartyList);
                }
            }
            if (CommonUtils.isNotEmpty(consigneeList)) {
                consigneeList = consolidateExportRates(consigneeList, messageResources);
                if (CommonUtils.isNotEmpty(consigneeList)) {
                    chargesList.addAll(consigneeList);
                }
            }
            if (CommonUtils.isNotEmpty(notifyPartyList)) {
                notifyPartyList = consolidateExportRates(notifyPartyList, messageResources);
                if (CommonUtils.isNotEmpty(notifyPartyList)) {
                    chargesList.addAll(notifyPartyList);
                }
            }
        }
        return chargesList;
    }

    public List getRatesForFclPrint(List fclRates, MessageResources messageResources) throws Exception {
        Map hashMap = new HashMap();
        Map newHashMap = new HashMap();
        String interModelRate = "";
        String interModelRate1 = "";
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            FclBlCharges newFclBlCharges = new FclBlCharges();
            PropertyUtils.copyProperties(newFclBlCharges, fclBlCharges);
            newHashMap.put(newFclBlCharges.getCharges(), newFclBlCharges);
        }
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            FclBlCharges newFclBlCharges = new FclBlCharges();
            PropertyUtils.copyProperties(newFclBlCharges, fclBlCharges);
            String consolidator = messageResources.getMessage("OceanFreight");
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if ((newFclBlCharges.getChargeCode() != null && newFclBlCharges.getChargeCode().
                        equalsIgnoreCase(colsolidatorRates[i]))) {
                    if (newFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                            && newHashMap.get(messageResources.getMessage("oceanfreightcharge")) != null) {
                        FclBlCharges tempFclBlCharges = (FclBlCharges) newHashMap.get(messageResources.getMessage("oceanfreightcharge"));
                        hashMap.put(messageResources.getMessage("oceanfreightcharge"), tempFclBlCharges);
                        flag = true;
                    } else {
                        if ("Yes".equalsIgnoreCase(newFclBlCharges.getBundleIntoOfr())) {
                            if (hashMap.get(messageResources.getMessage("oceanfreightcharge")) != null) {
                                FclBlCharges tempFclBlCharges = (FclBlCharges) hashMap.get(messageResources.getMessage("oceanfreightcharge"));
                                tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + newFclBlCharges.getAmount());
                                tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + newFclBlCharges.getOldAmount());
                                tempFclBlCharges.setAdjustment(tempFclBlCharges.getAdjustment() + newFclBlCharges.getAdjustment());
                                hashMap.put(messageResources.getMessage("oceanfreightcharge"), tempFclBlCharges);
                                flag = true;
                            }
                        } else {
                            interModelFlag = false;
                            for (int j = 0; j < interModelRates.length; j++) {
                                if (newFclBlCharges.getReadOnlyFlag() != null
                                        && newFclBlCharges.getChargeCode().equalsIgnoreCase(interModelRates[j])) {
                                    interModelFlag = true;
                                    break;
                                }
                            }
                            if (interModelFlag) {
                                interModelRate1 = interModelRate;
                                if (hashMap.containsKey(interModelRate1)) {
                                    FclBlCharges tempFclBlCharges = (FclBlCharges) hashMap.get(interModelRate1);
                                    tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + newFclBlCharges.getAmount());
                                    tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + newFclBlCharges.getOldAmount());
                                    tempFclBlCharges.setAdjustment(tempFclBlCharges.getAdjustment() + newFclBlCharges.getAdjustment());
                                    if ("INTRAMP".equals(newFclBlCharges.getChargeCode())) {
                                        tempFclBlCharges.setChargesId(newFclBlCharges.getChargesId());
                                    }
                                    hashMap.put(tempFclBlCharges.getChargeCode(), tempFclBlCharges);
                                } else {
                                    hashMap.put(newFclBlCharges.getChargeCode(), newFclBlCharges);
                                    interModelRate = newFclBlCharges.getChargeCode();
                                }
                            }
                            if (!interModelFlag) {
                                hashMap.put(newFclBlCharges.getChargeCode(), newFclBlCharges);
                                flag = true;
                            }
                        }
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (!"Yes".equalsIgnoreCase(newFclBlCharges.getBundleIntoOfr())
                        && null != newFclBlCharges.getChargeCode()
                        && (newFclBlCharges.getChargeCode().equals("DRAY")
                        || newFclBlCharges.getChargeCode().equals("INSURE")
                        || newFclBlCharges.getChargeCode().equals("PIERPA")
                        || newFclBlCharges.getChargeCode().equals("CHASFEE")
                        || (newFclBlCharges.getBookingFlag() != null
                        && newFclBlCharges.getBookingFlag().equals("new")
                        && !newFclBlCharges.getChargeCode().equals(FclBlConstants.FFCODE)
                        && !newFclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETWO)
                        && !newFclBlCharges.getChargeCode().equals(FclBlConstants.FFCODETHREE))
                        || (newFclBlCharges.getReadOnlyFlag() == null
                        || newFclBlCharges.getReadOnlyFlag().equals("")))) {
                    hashMap.put(newFclBlCharges.getChargeCode(), newFclBlCharges);
                } else {
                    if (hashMap.get(messageResources.getMessage("oceanfreightcharge")) != null) {
                        FclBlCharges tempFclBlCharges = (FclBlCharges) hashMap.get(messageResources.getMessage("oceanfreightcharge"));
                        tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + newFclBlCharges.getAmount());
                        tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + newFclBlCharges.getOldAmount());
                        tempFclBlCharges.setAdjustment(tempFclBlCharges.getAdjustment() + newFclBlCharges.getAdjustment());
                        hashMap.put(messageResources.getMessage("oceanfreightcharge"), tempFclBlCharges);
                    }
                }
            }
        }
        Set hashSet = hashMap.keySet();
        List tempList = new ArrayList();
        for (Iterator iterator = hashSet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            FclBlCharges fclBlCharges = (FclBlCharges) hashMap.get(key);
            tempList.add(fclBlCharges);
        }
        List newList = new ArrayList();
        newList = new FclBlBC().getSortedList(tempList);
        return newList;
    }

    public List getExportRatesForFclPrint(List fclRates, MessageResources messageResources) throws Exception {
        List newList = new ArrayList();
        List finalList = new ArrayList();
        LinkedList sortedList = new LinkedList();
        int k = 0;
        boolean noAutoOfr = true;
        for (Iterator iterator = fclRates.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            if (fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                    && CommonUtils.isNotEmpty(fclBlCharges.getReadOnlyFlag())) {
                noAutoOfr = false;
            }
            if (fclBlCharges.getChargeCode() != null && (fclBlCharges.getChargeCode().equals("OCNFRT") || fclBlCharges.getChargeCode().equals("OFIMP"))) {
                sortedList.add(k, fclBlCharges);
                k++;
            } else {
                sortedList.add(fclBlCharges);
            }
        }
        for (Iterator iterator = sortedList.iterator(); iterator.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iterator.next();
            String consolidator = messageResources.getMessage("OceanFreight");
            String colsolidatorRates[] = new String[5];
            if (consolidator.indexOf(",") != -1) {
                colsolidatorRates = consolidator.split(",");
            }
            String interModel = messageResources.getMessage("IntermodelAccrual");
            String interModelRates[] = new String[10];
            if (interModel.indexOf(",") != -1) {
                interModelRates = interModel.split(",");
            } else {
                interModelRates[0] = interModel;
            }
            boolean interModelFlag = false;
            boolean flag = false;
            for (int i = 0; i < colsolidatorRates.length; i++) {
                if (fclBlCharges.getChargeCode() != null && fclBlCharges.getChargeCode().
                        equalsIgnoreCase(colsolidatorRates[i])) {
                    if (fclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))) {
                        try {
                            FclBlCharges charges = new FclBlCharges();
                            PropertyUtils.copyProperties(charges, fclBlCharges);
                            newList.add(charges);
                            flag = true;
                        } catch (IllegalAccessException ex) {
                            log.info("Exception on class PrintConfigBC in method getExportRatesForFclPrint" + new Date(), ex);
                        } catch (InvocationTargetException ex) {
                            log.info("Exception on class PrintConfigBC in method getExportRatesForFclPrint" + new Date(), ex);
                        }
                    } else {
                        if ("Yes".equalsIgnoreCase(fclBlCharges.getBundleIntoOfr())) {
                            for (Iterator itr = newList.iterator(); itr.hasNext();) {
                                FclBlCharges tempFclBlCharges = (FclBlCharges) itr.next();
                                if (tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
                                        && CommonUtils.isNotEmpty(tempFclBlCharges.getReadOnlyFlag())) {
                                    tempFclBlCharges.setAmount(tempFclBlCharges.getAmount() + fclBlCharges.getAmount());
                                    tempFclBlCharges.setOldAmount(tempFclBlCharges.getOldAmount() + fclBlCharges.getOldAmount());
                                    tempFclBlCharges.setAdjustment((null != tempFclBlCharges.getAdjustment() ? tempFclBlCharges.getAdjustment() : 0d) + (null != fclBlCharges.getAdjustment() ? fclBlCharges.getAdjustment() : 0d));
                                    flag = true;
                                    break;
                                }
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
                    }
                    break;
                }
            }
            if (!flag && !interModelFlag) {
                if (!"Yes".equalsIgnoreCase(fclBlCharges.getBundleIntoOfr())
                        && null != fclBlCharges.getChargeCode()
                        && (fclBlCharges.getChargeCode().equals("DRAY")
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
                        if (tempFclBlCharges.getChargeCode().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))
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
        k = 0;
        LinkedList linkedList = new LinkedList();
        for (Iterator iter = newList.iterator(); iter.hasNext();) {
            FclBlCharges chargeCode = (FclBlCharges) iter.next();
            if (chargeCode.getChargeCode() != null && (chargeCode.getChargeCode().equals("OCNFRT") || chargeCode.getChargeCode().equals("OFIMP"))
                    && CommonUtils.isNotEmpty(chargeCode.getReadOnlyFlag())) {
                linkedList.add(k, chargeCode);
                k++;
            } else {
                linkedList.add(chargeCode);
            }
        }
        finalList.addAll(linkedList);
        return finalList;
    }

    public String getEmailAndName(String bol) throws Exception {
        LCLBookingDAO bkgDao = new LCLBookingDAO();
        List<CustomerContact> contacts = bkgDao.getCodeFEmailContactList(bol);
        StringBuilder nameAndEmailList = new StringBuilder();
        for (CustomerContact contact : contacts) {
            nameAndEmailList.append(contact.getAccountName());
            if (CommonUtils.isNotEmpty(contact.getEmail())) {
                nameAndEmailList.append("&nbsp;<&nbsp;");
                nameAndEmailList.append(contact.getEmail().toLowerCase());
                nameAndEmailList.append("&nbsp;>&nbsp;");
            }
            nameAndEmailList.append("<br>");
        }
        return nameAndEmailList.toString();
    }
}

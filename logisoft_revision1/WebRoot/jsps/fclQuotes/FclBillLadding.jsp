<%@page import="com.gp.cong.logisoft.hibernate.dao.NotesDAO,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.FclBlContainer"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.FclBl" />
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>

<%@ page language="java" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.*"%>
<%@ page import="com.gp.cong.logisoft.bc.notes.NotesConstants,java.text.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.User"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlConstants,com.gp.cvst.logisoft.struts.form.FclBillLaddingForm"/>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO,com.gp.cong.logisoft.util.CommonFunctions"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.QuotationConstants"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlBC"/>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc,com.gp.cong.struts.LoadLogisoftProperties"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="uns"%>
<%@include file="../includes/jspVariables.jsp"  %>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<jsp:useBean id="fclBlBean" class="com.gp.cvst.logisoft.beans.FclBlBean" scope="request"/>
<html
    <bean:define id="fileNumberPrefix" type="String">
        <bean:message key="fileNumberPrefix"/>
    </bean:define>
    <bean:define id="defaultAgent" type="String">
        <bean:message key="defaultAgent"/>
    </bean:define>
    <head>
        <title>FclBillAddingPart</title>
        <%
            boolean importFlag;
            if (request.getAttribute("fclBl") != null) {
                FclBl fcl = (FclBl) request.getAttribute("fclBl");
                importFlag = (null != fcl.getImportFlag() && fcl.getImportFlag().equalsIgnoreCase("I") ? true : false);
            } else {
                importFlag = ((null != session.getAttribute(ImportBc.sessionName)) ? true : false);
            }
            String companyCodeValue = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            request.setAttribute("companyCodeValue", companyCodeValue);
            String shipperName = "", shipperNo = "";
            String path = request.getContextPath();
            String autoDeductFF = "";
            DBUtil dbUtil = new DBUtil();
            PortsDAO portsDAO = new PortsDAO();
            request.setAttribute("insuranceAllowed",portsDAO.getInsuranceAllowed());
            request.setAttribute("typeOfMoveList", dbUtil.getGenericFCLforTypeOfMovebooking(new Integer(48), "yes", "yes"));
            User user = null;
            String userName = "", userRole = "";
            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
                userName = user.getLoginName();
                userRole = user.getRole().getRoleDesc();
            }
            FclBillLaddingForm fclBillLaddingform = new FclBillLaddingForm();
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setReadyToPost("off");
            transactionBean.setBLPrinting("Yes");
            transactionBean.setNPrinting("Yes");
            transactionBean.setOPrinting("Yes");
            transactionBean.setOriginalBL("Y");
            transactionBean.setInsurance("N");
            transactionBean.setSsBldestinationChargesPreCol("P");
            //--PRINT OPTIONS--
            fclBillLaddingform.setTotalContainers("Yes");
            fclBillLaddingform.setConturyOfOrigin("Yes");
            fclBillLaddingform.setPrintContainersOnBL("Yes");
            fclBillLaddingform.setAgentsForCarrier("No");
            fclBillLaddingform.setNoOfPackages("Yes");
            fclBillLaddingform.setShipperLoadsAndCounts("Yes");
            fclBillLaddingform.setProof("Yes");
            fclBillLaddingform.setPreAlert("Yes");
            fclBillLaddingform.setNonNegotiable("Yes");
            fclBillLaddingform.setPrintRev("Yes");
            fclBillLaddingform.setDoorOriginAsPlor("Yes");
            fclBillLaddingform.setDoorOriginAsPlorHouse("Yes");
            fclBillLaddingform.setDoorDestinationAsFinalDeliveryToMaster("No");
            fclBillLaddingform.setDoorDestinationAsFinalDeliveryToHouse("No");
            fclBillLaddingform.setTrimTrailingZerosForQty("Yes");
            fclBillLaddingform.setCertifiedTrueCopy("No");
            fclBillLaddingform.setCollectThirdParty("No");
            fclBillLaddingform.setPrintAlternatePort("No");
            if (importFlag) {
                fclBillLaddingform.setAgentsForCarrier("No");
                fclBillLaddingform.setResendCostToBlue("No");
                fclBillLaddingform.setNoOfPackages("Yes");
                fclBillLaddingform.setShipperLoadsAndCounts("No");
                fclBillLaddingform.setProof("No");
                fclBillLaddingform.setPreAlert("No");
                fclBillLaddingform.setNonNegotiable("No");
                fclBillLaddingform.setNonNegotiable("No");
               if (fclBillLaddingform.getDirectConsignment() == null){
                   fclBillLaddingform.setDirectConsignment("off");
               }
                else{
                    fclBillLaddingform.setDirectConsignment("on");
                }
                 if (request.getAttribute("companyCode").equals("OTIC")) {
                    fclBillLaddingform.setDefaultAgent("N");
                } else {
                    fclBillLaddingform.setDefaultAgent("Y");
                }
            }


            //---

            if (request.getAttribute("transactionBean") != null) {
                transactionBean = (TransactionBean) request.getAttribute("transactionBean");
            }

            String view = "";
            if (session.getAttribute("view") != null) {
                view = (String) session.getAttribute("view");
            }
            String lockMessage = "";
            if (request.getAttribute(QuotationConstants.LOCK) != null) {
                lockMessage = (String) request.getAttribute(QuotationConstants.LOCK);
                view = "3";
            }
            request.setAttribute("fclblbean", fclBlBean);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yyyy");
            String billOfLadding = "";
            String vessel = "";
            String vesselName = "";
            String buttonValue = "";
            String action = "";
            String readyToPost = "";
            String sailDate = "";
            String eta = "";
            String etaFd = "";
            String billofladding = null;
            String fileNo = "";
            String message = "";
            String ready = "";
            String noticeNo = null;
            String doorOfOrigin = "";
            String doorOfDestination = "";
            String billThirdPartyName = "";
            String billThirdParty = "";
            String blVoid = null;
            String blClosed = null;
            String ssl = "", breakBulk = "";
            String bol = "", intraGtnexus = "";
            String latestBolid = null;
            String completeFileNo = "", newFileNo = "";
            String manaualVesselName = "";
            String vesselCheck = "";
            String brand = "";
            boolean showReverseToBooking = false;
            String confirmedBoard = "", verifiedEta = "", confirmedComment = "", importEta = "", importReleaseComment = "", importReleaseVal = "";

            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            if (request.getAttribute("loackRecord") != null) {
                message = (String) request.getAttribute("loackRecord");
            }
            if (session.getAttribute("action") != null) {
                action = (String) session.getAttribute("action");
            }
            if (request.getAttribute("fclBl") != null) {
                FclBl fclbl = (FclBl) request.getAttribute("fclBl");
                if (null != fclbl.getPrintRev()) {
                    fclBillLaddingform.setPrintRev(fclbl.getPrintRev());
                }
                ssl = (null != fclbl.getSslineName()) ? fclbl.getSslineName() : "";
                ssl = ssl.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;");
                if (fclbl.getAutoDeductFFCom() != null) {
                    autoDeductFF = fclbl.getAutoDeductFFCom();
                }
                if (fclbl.getThirdPartyName() != null) {
                    billThirdPartyName = fclbl.getThirdPartyName();
                }
                if (fclbl.getBreakBulk() != null) {
                    breakBulk = fclbl.getBreakBulk();
                }
           if (importFlag) {
                    shipperName = fclbl.getHouseShipperName();
                    shipperNo = fclbl.getHouseShipper();
                } else {
                    shipperName = fclbl.getShipperName();
                    shipperNo = fclbl.getShipperNo();
                }
                if (fclbl.getBillTrdPrty() != null) {
                    billThirdParty = fclbl.getBillTrdPrty();
                }
                if (fclbl.getDoorOfOrigin() != null) {
                    doorOfOrigin = fclbl.getDoorOfOrigin();
                }
                if (fclbl.getDoorOfDestination() != null) {
                    doorOfDestination = fclbl.getDoorOfDestination();
                }

                if (fclbl.getLatesBolId() != null) {
                    latestBolid = fclbl.getLatesBolId();
                }
                if (fclbl.getReadyToPost() != null && !fclbl.getReadyToPost().equals("")) {
                    ready = fclbl.getReadyToPost();
                    if (fclbl.getBlVoid() != null) {
                        blVoid = "Y";
                    }
                    if (fclbl.getBlClosed() != null && !fclbl.getBlClosed().equals("")) {
                        blClosed = "Y";
                    }
                    //session.setAttribute("view", "3");
                }
                if (fclbl.getBol() != null) {
                    bol = fclbl.getBol().toString();
                }
                if (bol != null) {
                    completeFileNo = bol.toString();
                }
                if (fileNo != null && fclbl.getFileNo() != null) {
                    newFileNo = fclbl.getFileNo().toString();
                }
                if (fclbl.getBolId() != null && fclbl.getBolId() != null) {
                    billOfLadding = fclbl.getBolId().toString();
                    if (billOfLadding.indexOf(FclBlConstants.DELIMITER) > -1) {
                        String array[] = StringUtils.split(billOfLadding, FclBlConstants.DELIMITER);
                        for (int k = 0; k < array.length; k++) {
                            if (array.length == k + 1) {
                                noticeNo = array[k];
                            }
                        }
                    }
                }
                if (fclbl.getFileNo() != null) {
                    fileNo = fclbl.getFileNo();
                    FclBlBC fclBlBC = new FclBlBC();
                    if (CommonFunctions.isNotNull(fclBlBC.getFileNoObjectForMultipleBl(fileNo + FclBlConstants.EQUALDELIMITER))) {
                        showReverseToBooking = true;
                    }
                }
                if (fclbl.getReadyToPost() != null) {
                    readyToPost = fclbl.getReadyToPost();
                }
                if (fclbl.getSailDate() != null) {
                    sailDate = sdf3.format(fclbl.getSailDate());
                }
                if (fclbl.getEta() != null) {
                    eta = sdf3.format(fclbl.getEta());
                }
                if (fclbl.getEtaFd() != null) {
                    etaFd = sdf3.format(fclbl.getEtaFd());
                }
                if (fclbl.getVessel() != null) {
                    vessel = fclbl.getVessel().getCode();
                    vesselName = fclbl.getVessel().getCodedesc();
                }
                if (fclbl.getManualVesselName() != null) {
                    manaualVesselName = fclbl.getManualVesselName();
                }
                if (fclbl.getVesselNameCheck() != null) {
                    vesselCheck = fclbl.getVesselNameCheck();
                }
                if(fclbl.getBrand() != null){
                  brand = fclbl.getBrand();
                 transactionBean.setBrand(fclbl.getBrand());
                }
                if (fclbl.getReadyToPost() != null) {
                    transactionBean.setReadyToPost(fclbl.getReadyToPost());
                }
                if (null != fclbl.getDestinationChargesPreCol()) {
                    if (fclbl.getDestinationChargesPreCol() != null && fclbl.getDestinationChargesPreCol().equals("P-Prepaid")) {
                        transactionBean.setDestinationChargesPreCol("P");
                    } else {
                        transactionBean.setDestinationChargesPreCol("C");
                    }
                }
                if (fclbl.getSsBldestinationChargesPreCol() != null) {
                    transactionBean.setSsBldestinationChargesPreCol(fclbl.getSsBldestinationChargesPreCol());
                }
                if (fclbl.getReadyToPost() != null) {
                    transactionBean.setReadyToPost("on");
                }
                if (fclbl.getOPrinting() != null) {
                    transactionBean.setOriginalBL(fclbl.getOPrinting());
                }
                if (fclbl.getInsurance() != null) {
                    transactionBean.setInsurance(fclbl.getInsurance());
                }
                if (fclbl.getNPrinting() != null) {
                    transactionBean.setNPrinting(fclbl.getNPrinting());
                }
                if (fclbl.getBLPrinting() != null) {
                    transactionBean.setBLPrinting(fclbl.getBLPrinting());
                }
                if (fclbl.getImportsFreightRelease() != null) {
                    transactionBean.setImportsFreightRelease(fclbl.getImportsFreightRelease());
                }
                if (fclbl.getOriginalBlRequired() != null) {
                    transactionBean.setOriginalBlRequired(fclbl.getOriginalBlRequired());
                }
                if (fclbl.getMaster() != null) {
                    transactionBean.setMasterCheckBox(fclbl.getMaster());
                }
                if (fclbl.getReadyToEDI() != null) {
                    transactionBean.setEdiCheckBox(fclbl.getReadyToEDI());
                }
                if (fclbl.getBlClosed() != null) {
                    transactionBean.setBlColsed(fclbl.getBlClosed());
                }
                if (fclbl.getHouseShipperCheck() != null) {
                    transactionBean.setHouseShipperCheck(fclbl.getHouseShipperCheck());
                }
                if (fclbl.getEditHouseShipperCheck() != null) {
                    transactionBean.setEditHouseShipperCheck(fclbl.getEditHouseShipperCheck());
                }
                if (fclbl.getEditHouseNotifyCheck() != null) {
                    transactionBean.setEditHouseNotifyCheck(fclbl.getEditHouseNotifyCheck());
                }
                if (fclbl.getEditHouseConsigneeCheck() != null) {
                    transactionBean.setEditHouseConsigneeCheck(fclbl.getEditHouseConsigneeCheck());
                }
                if (fclbl.getPaymentRelease() != null) {
                    transactionBean.setPaymentRelease(fclbl.getPaymentRelease());
                }
                if (fclbl.getFclInttgra() != null && (fclbl.getFclInttgra().equalsIgnoreCase("I")
                        || fclbl.getFclInttgra().equalsIgnoreCase("G") || fclbl.getFclInttgra().equalsIgnoreCase("N"))) {
                    transactionBean.setFclInttgra(fclbl.getFclInttgra());
                    if (fclbl.getFclInttgra().equalsIgnoreCase("I")) {
                        intraGtnexus = FclBlConstants.INTRA;
                    } else if (fclbl.getFclInttgra().equalsIgnoreCase("G")) {
                        intraGtnexus = FclBlConstants.GTNEXUS;
                    } else {
                        intraGtnexus = "";
                    }
                    request.setAttribute("intraGtnexus", intraGtnexus);
                }
                if (fclbl.getFileType() != null) {
                    transactionBean.setFileType(fclbl.getFileType());
                }
                if (fclbl.getPrintContainersOnBL() != null) {
                    transactionBean.setPrintContainersOnBL(fclbl.getPrintContainersOnBL());
                }
                if (fclbl.getPrintPhrase() != null) {
                    transactionBean.setPrintPhrase(fclbl.getPrintPhrase());
                }
                if (fclbl.getAlternatePOL() != null) {
                    transactionBean.setAlternatePOL(fclbl.getAlternatePOL());
                }
                if (fclbl.getManifestPrintReport() != null) {
                    transactionBean.setManifestPrintReport(fclbl.getManifestPrintReport());
                }
                if (fclbl.getRoutedAgentCheck() != null) {
                    transactionBean.setRoutedAgentCheck(fclbl.getRoutedAgentCheck());
                }
                transactionBean.setConfirmOnBoard("N");
                if (fclbl.getConfirmOnBorad() != null) {
                    confirmedBoard = fclbl.getConfirmOnBorad();
                    transactionBean.setConfirmOnBoard(fclbl.getConfirmOnBorad());
                }
                if (null != fclbl.getHouseBl()) {
                    if (fclbl.getHouseBl() != null && fclbl.getHouseBl().equals("P-Prepaid")) {
                        transactionBean.setHouseBL("P");
                    } else if (fclbl.getHouseBl() != null && fclbl.getHouseBl().equals("B-Both")) {
                        transactionBean.setHouseBL("B");
                    } else {
                        transactionBean.setHouseBL("C");
                    }
                } else {
                    transactionBean.setHouseBL("P");
                }
                if (null != fclbl.getStreamShipBl()) {
                    if (fclbl.getStreamShipBl() != null && fclbl.getStreamShipBl().equals("P-Prepaid")) {
                    } else {
                        transactionBean.setStreamShipBL("C");
                    }
                } else {
                    transactionBean.setStreamShipBL("C");
                }
                //--CHECKBOXES FOR DISABLING DOJO ---
                if (null != fclbl.getMasterConsigneeCheck()) {
                    transactionBean.setMasterConsigneeCheck(fclbl.getMasterConsigneeCheck());
                }
                if (null != fclbl.getMasterNotifyCheck()) {
                    transactionBean.setMasterNotifyCheck(fclbl.getMasterNotifyCheck());
                }
                if (null != fclbl.getConsigneeCheck()) {
                    transactionBean.setConsigneeCheck(fclbl.getConsigneeCheck());
                }
                if (null != fclbl.getNotifyCheck()) {
                    transactionBean.setNotifyCheck(fclbl.getNotifyCheck());
                }
                if (null != fclbl.getEdiConsigneeCheck()) {
                    transactionBean.setEdiConsigneeCheck(fclbl.getEdiConsigneeCheck());
                }
                if (null != fclbl.getEdiNotifyPartyCheck()) {
                    transactionBean.setEdiNotifyPartyCheck(fclbl.getEdiNotifyPartyCheck());
                }
                if (null != fclbl.getEdiShipperCheck()) {
                    transactionBean.setEdiShipperCheck(fclbl.getEdiShipperCheck());
                }
                if (null != fclbl.getFileType()) {
                    transactionBean.setFileType(fclbl.getFileType());
                }
                //---TO SET PRINT OPTIONS----
                if (null != fclbl.getAgentsForCarrier()) {
                    fclBillLaddingform.setAgentsForCarrier(fclbl.getAgentsForCarrier());
                }
                if (null != fclbl.getResendCostToBlue()) {
                    fclBillLaddingform.setResendCostToBlue(fclbl.getResendCostToBlue());
                }
                if (null != fclbl.getTotalContainers()) {
                    fclBillLaddingform.setTotalContainers(fclbl.getTotalContainers());
                }
                if (null != fclbl.getCountryOfOrigin()) {
                    fclBillLaddingform.setConturyOfOrigin(fclbl.getCountryOfOrigin());
                }
                if (null != fclbl.getNoOfPackages()) {
                    fclBillLaddingform.setNoOfPackages(fclbl.getNoOfPackages());
                } else if ("Y".equals(fclbl.getBreakBulk())) {
                    fclBillLaddingform.setNoOfPackages("No");
                }
                if (null != fclbl.getPrintContainersOnBL()) {
                    fclBillLaddingform.setPrintContainersOnBL(fclbl.getPrintContainersOnBL());
                } else if ("Y".equals(fclbl.getBreakBulk())) {
                    fclBillLaddingform.setPrintContainersOnBL("No");
                }
                if (null != fclbl.getShipperLoadsAndCounts()) {
                    fclBillLaddingform.setShipperLoadsAndCounts(fclbl.getShipperLoadsAndCounts());
                }
                if (null != fclbl.getProof()) {
                    fclBillLaddingform.setProof(fclbl.getProof());
                }
                if (null != fclbl.getPreAlert()) {
                    fclBillLaddingform.setPreAlert(fclbl.getPreAlert());
                }
                if (null != fclbl.getNonNegotiable()) {
                    fclBillLaddingform.setNonNegotiable(fclbl.getNonNegotiable());
                }
                if (null != fclbl.getDoorOriginAsPlor()) {
                    fclBillLaddingform.setDoorOriginAsPlor(fclbl.getDoorOriginAsPlor());
                }
                fclBillLaddingform.setDoorOriginAsPlorHouse(null != fclbl.getDoorOriginAsPlorHouse() ? fclbl.getDoorOriginAsPlorHouse() : "Yes");
                if (null != fclbl.getDoorDestinationAsFinalDeliveryToMaster()) {
                    fclBillLaddingform.setDoorDestinationAsFinalDeliveryToMaster(fclbl.getDoorDestinationAsFinalDeliveryToMaster());
                }
                if (null != fclbl.getDoorDestinationAsFinalDeliveryToHouse()) {
                    fclBillLaddingform.setDoorDestinationAsFinalDeliveryToHouse(fclbl.getDoorDestinationAsFinalDeliveryToHouse());
                }
                if (null != fclbl.getTrimTrailingZerosForQty()) {
                    fclBillLaddingform.setTrimTrailingZerosForQty(fclbl.getTrimTrailingZerosForQty());
                }
                if (null != fclbl.getCertifiedTrueCopy()) {
                    fclBillLaddingform.setCertifiedTrueCopy(fclbl.getCertifiedTrueCopy());
                }
                if (null != fclbl.getCollectThirdParty()) {
                    fclBillLaddingform.setCollectThirdParty(fclbl.getCollectThirdParty());
                }
                fclBillLaddingform.setDockReceipt(fclbl.getDockReceipt());
                if (null != fclbl.getBreakBulk()) {
                    fclBillLaddingform.setBreakBulk(fclbl.getBreakBulk());
                }
                if (null != fclbl.getPrintAlternatePort()) {
                    fclBillLaddingform.setPrintAlternatePort(fclbl.getPrintAlternatePort());
                }
                if (fclbl.getDefaultAgent() != null) {
                    fclBillLaddingform.setDefaultAgent(fclbl.getDefaultAgent());
                }
                if (null != fclbl.getOmitTermAndPort()) {
                    fclBillLaddingform.setOmitTermAndPort(fclbl.getOmitTermAndPort());
                }
                if (null != fclbl.getServiceContractNo()) {
                    fclBillLaddingform.setServiceContractNo(fclbl.getServiceContractNo());
                }
                if (null != fclbl.getHblPOLOverride()) {
                    fclBillLaddingform.setHblPOLOverride(fclbl.getHblPOLOverride());
                }
                if (null != fclbl.getHblPODOverride()) {
                    fclBillLaddingform.setHblPODOverride(fclbl.getHblPODOverride());
                }
                if (null != fclbl.getHblFDOverride()) {
                    fclBillLaddingform.setHblFDOverride(fclbl.getHblFDOverride());
                }
                if (null != fclbl.getReplaceArrival()) {
                    fclBillLaddingform.setReplaceArrival(fclbl.getReplaceArrival());
                }
                if (fclbl.getHouseShipper() != null && !"".equalsIgnoreCase(fclbl.getHouseShipper())) {
                    request.setAttribute("isShipperNotes", new NotesDAO().isCustomerNotes(fclbl.getHouseShipper()));
                }
                if (fclbl.getHouseConsignee() != null && !"".equalsIgnoreCase(fclbl.getHouseConsignee())) {
                    request.setAttribute("isConsigneeNotes", new NotesDAO().isCustomerNotes(fclbl.getHouseConsignee()));
                }
                 if (fclbl.getHouseNotifyPartyNo() != null && !"".equalsIgnoreCase(fclbl.getHouseNotifyPartyNo())) {
                    request.setAttribute("isMNotifyNotes", new NotesDAO().isCustomerNotes(fclbl.getHouseNotifyPartyNo()));
                }
                if (fclbl.getShipperNo() != null && !"".equalsIgnoreCase(fclbl.getShipperNo())) {
                    request.setAttribute("isHShipperNotes", new NotesDAO().isCustomerNotes(fclbl.getShipperNo()));
                }
                if (fclbl.getConsigneeNo() != null && !"".equalsIgnoreCase(fclbl.getConsigneeNo())) {
                    request.setAttribute("isHConsigneeNotes", new NotesDAO().isCustomerNotes(fclbl.getConsigneeNo()));
                }
                if (fclbl.getNotifyParty() != null && !"".equalsIgnoreCase(fclbl.getNotifyParty())) {
                    request.setAttribute("isHNotifyNotes", new NotesDAO().isCustomerNotes(fclbl.getNotifyParty()));
                }
                if (fclbl.getForwardAgentNo() != null && !"".equalsIgnoreCase(fclbl.getForwardAgentNo())) {
                    request.setAttribute("isFreightNotes", new NotesDAO().isCustomerNotes(fclbl.getForwardAgentNo()));
                }
                request.setAttribute("fclBillLaddingform", fclBillLaddingform);
                //---
                verifiedEta = (fclbl.getVerifyETA() != null) ? fclbl.getVerifyETA().toString() : "";
                confirmedComment = (fclbl.getConfOnBoardComments() != null) ? fclbl.getConfOnBoardComments().replaceAll("[\r\n]", "\t") : "";

                //--FOR IMPORT RELEASE----
                transactionBean.setImportRelease("N");
                if (null != fclbl.getImportRelease()) {
                    importReleaseVal = fclbl.getImportRelease();
                    transactionBean.setImportRelease(fclbl.getImportRelease());
                }
                importEta = (null != fclbl.getImportVerifiedEta()) ? fclbl.getImportVerifiedEta().toString() : "";
                importReleaseComment = (null != fclbl.getImportReleaseComments()) ? fclbl.getImportReleaseComments().replaceAll("[\r\n]", "\t") : "";
            } else {
                transactionBean.setHouseBL("P");
            }
            request.setAttribute("transactionBean", transactionBean);
            request.setAttribute("countrycode", dbUtil.getCountryList());
            request.setAttribute("shipmenttypelist", dbUtil.getShipmentList());
            if (request.getAttribute("buttonValue") != null) {
                buttonValue = (String) request.getAttribute("buttonValue");
            }
            if (request.getParameter("billLaddingNumber") != null) {
                billofladding = (String) request.getParameter("billLaddingNumber");
            }
            List contactList = (List) request.getAttribute("ContactConfigE1andE2");

            String mandatoryFieldForBl = "";
            if (importFlag) {
                mandatoryFieldForBl = "Mandatory Fields Needed<br>1.Origin<BR>2.Destination<BR>3.Issuing TM"
                        + "<BR>4.SSLName<BR>5.Vessel Name<BR>6.SS Voyage<BR>7.ETD/ETA<BR>8.MASTER BL #<BR>9.AMS House BL#<BR>10.Forwarder Name<BR>11.House Shipper Name<BR>12.House Consignee Name<BR>13.Container Details<BR>14.Description Of Package<BR>15.Charge Code";
            } else {
                mandatoryFieldForBl = "Mandatory Fields Needed<br>1.Container Unit Number<BR>2.Confirm On Board<BR>3.AES Details<BR>4.Description of Package";
            }
            request.setAttribute("importFlag", importFlag);

        %>


        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <%--<link rel="stylesheet" href="${path}/css/jquery-tabs/jquery.tabs.css" type="text/css" media="print, projection, screen" />--%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <link rel="stylesheet" href="${path}/css/jquery-tabs/jquery-ui-1.8.10.custom.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="${path}/css/default/style.css" type="text/css" media="print, projection, screen" />
        <link rel="alternate stylesheet" type="text/css" media="all" href="${path}/css/cal/calendar-win2k-cold-2.css"  title="win2k-cold-2" />
        <link rel="stylesheet" type="text/css" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua" />
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/fclBillLanding.js"></script>
        <script language="javascript" src="${path}/js/fclBl.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script src="${path}/js/jquery/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script language="javascript">
            start = function () {
                var importFlag = "${importFlag}";
                setFocusToTab('${selectedTab}');
                checkEdi();
                hideMasterShipper();
                hideMasterConsignee();
                hideMasterNotify();
                hideHouseShipper();
                hideHouseConsignee();
                hideHouseNotify();
                agentReadOnly();
                getBillTO();
                disableBillToCodeonLoad();//---- bill to code wil be disabled and enabled
                makeButtonRedOnSave();//---making AES and PKGS button red if details exist--.
                loadData('<%=confirmedBoard%>', '<%=importReleaseVal%>');// confirm on board information wil display
                makeFclBlCorrectionButtonRed();//--making FclBlCorrection,ConfirmOnBoard,Import release button green if their data exists--.
                makeInbondButtonGreen();
                makeARInvoiceButtonGreen('${fclBl.fileNo}', "");
                disableBillToThirdParty();
                disabledFClBL('<%=blVoid%>', '<%=view%>', '<%=ready%>',<%=roleDuty.isUnmanifest()%>, '${importFlag}', '<%=blClosed%>');
                setTabName('${selectedTab}');
                serializeForm();
                displayHeading();
                accountDetails();
                loadOldArray();
                showBrandValuesFromBooking('<%=brand%>', '${importFlag}', '${companyCodeValue}');
            }
            window.onload = start;
        </script>

        <script language="javascript" type="text/javascript">
            var agentOrCongineeLable = ' Please select Agent Name And Number  from TradeRoute Tab ';
            var shipperForImportLable = ' Please select  House B/L Shipper Name And Number from Shipper Forwarder Consignee Notify ';
            var shipperForImportValue = '';
            var agentOrConsignee = '';
            var shipperAccountNoForImportValue = '';
            var insuranceAllowed = "${insuranceAllowed}";
            function checkForAgentOrConsignee() {
                agentOrConsignee = document.fclBillLaddingform.consignee.value;
                shipperForImportValue = document.fclBillLaddingform.houseName.value;
                shipperAccountNoForImportValue = document.fclBillLaddingform.houseShipper.value;
            }
            function disabledFClBL(blVoid, view, ready, flag, importFlag, blClosed) {
                var accessMode = '${param.accessMode}';
                var voidBl = '${fclBl.blVoid}';
                agentOrCongineeLable = ' Please select House B/L Consignee Name And Number from Shipper Forwarder Consignee Notify';
                shipperForImportLable = ' Please select Master B/L Shipper Name And Number from Shipper Forwarder Consignee Notify ';
                checkPaymentRelease();
                if (view == '3' || accessMode == '0' || voidBl == 'Y') {
                    if (voidBl == 'Y') {
                        disableFieldsWhileVoid(document.getElementById("fclbl"));
                    } else {
                        window.parent.disableFieldsWhileLocking(document.getElementById("fclbl"));
                    }
                    var imgs = document.getElementsByTagName("img");
                    for (var k = 0; k < imgs.length; k++) {
                        imgs[k].style.visibility = "hidden";
                    }
                } else {
                    if (null != blClosed && blClosed == 'Y') {
                        disableFieldsWhileBlClosed(document.getElementById("fclbl"));
                        var imgs = document.getElementsByTagName("img");
                        for (var k = 0; k < imgs.length; k++) {
                            imgs[k].style.visibility = "hidden";
                        }

                    } else if (ready == 'M' || ready == 'm') {
                        makeSomeFieldsEditableOnManifest(document.getElementById("fclbl"), '${fclBl.importFlag}', '${roleDuty.allowRoutedAgent}');
                    }
                }
                if (!flag) {
                    if (null != document.getElementById("unManifestButton")) {
                        document.getElementById("unManifestButton").style.visibility = 'hidden';
                        document.getElementById("unManifestButtonDown").style.visibility = 'hidden';
                    }
                    if (null != document.getElementById("blOpnedButton")) {
                        document.getElementById("blOpnedButton").style.visibility = 'hidden';
                        document.getElementById("blOpnedButtonBottom").style.visibility = 'hidden';
                    }
                    if (null != document.getElementById("blAuditedCancelButton")) {
                        document.getElementById("blAuditedCancelButton").style.visibility = 'hidden';
                        document.getElementById("blAuditedCancelButtonBottom").style.visibility = 'hidden';
                    }
                }
            }
            function disableBillToThirdParty() {
                if (document.getElementById("billThirdPartyName").value !== ""
                        && document.getElementById("billTrePty").value !== "") {
                    document.getElementById("billThirdPartyName").disabled = false;
                    document.getElementById("billThirdPartyName").className = "textlabelsBoldForTextBox";
                    document.getElementById("billTrePty").disabled = false;
                    document.getElementById("billTrePty").className = "textlabelsBoldForTextBox";
                } else if (document.getElementById("billThirdPartyName").value === ""
                        && document.getElementById("billTrePty").value === "") {
                    document.getElementById("billThirdPartyName").disabled = true;
                    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                    document.getElementById("billThirdPartyName").style.border = 0;
                    document.getElementById("billTrePty").disabled = true;
                    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                    document.getElementById("billTrePty").style.border = 0;
                }
            }
            function makeFclBlCorrectionButtonRed() {
                var blNumber = '<%=billOfLadding%>';
                var bol = '<%=bol%>';
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "getCorrectionsForThisBL",
                        param1: blNumber,
                        param2: bol
                    },
                    success: function (data) {
                        if (data != "") {
                            if (data.indexOf(",") > -1) {
                                var result = data.split(",");
                                if (null != document.getElementById("correctionButton") && null != document.getElementById("correctionButtonDown")) {
                                    if (result[0] == "Corrections Exist") {
                                        document.getElementById("correctionButton").className = "buttonColor";
                                        document.getElementById("correctionButtonDown").className = "buttonColor";
                                    } else {
                                        document.getElementById("correctionButton").className = "buttonStyleNew";
                                        document.getElementById("correctionButtonDown").className = "buttonStyleNew";
                                    }
                                }
                            }
                        }
                    }
                });
                if ((document.fclBillLaddingform.importRelease[0].checked && document.fclBillLaddingform.paymentRelease[0].checked)) {
                    document.getElementById("importReleaseButton").className = "buttonColor";
                    document.getElementById("importReleaseDown").className = "buttonColor";
                } else if (document.fclBillLaddingform.importRelease[1].checked && document.fclBillLaddingform.paymentRelease[1].checked) {
                    // No color needed
                } else {
                    document.getElementById("importReleaseButton").className = "buttonColorRed";
                    document.getElementById("importReleaseDown").className = "buttonColorRed";
                }
            }
            function FCLBLCorrection(val1, val2) {
                GB_show('FclBlCorrections', '${path}/fclBlCorrections.do?buttonValue=FclBl&blNumber=' + val1 + '&fileNo=' + val2 + '&quickCn=' + true,
                        width = "550", height = "1050");
            }
            function latestBl(val1) {
                window.location.href = '${path}/fclBL.do?paramid=' + val1;
            }
            function disableThirdParty() {
                if (document.fclBillLaddingform.accountName.value == "") {
                    document.getElementById('billThirdPartyName').disabled = true;
                    if (document.getElementById("contactNameButtonForT")) {
                        document.getElementById("contactNameButtonForT").style.visibility = 'hidden';
                    }
                    document.getElementById('billTrePty').disabled = true;
                    document.getElementById('toggle').style.visibility = "hidden";
                }
                if (document.fclBillLaddingform.billToCode[0].checked) {
                    if (document.fclBillLaddingform.forwardingAgentName.value == "") {
                        alertNew("Please select Forwarder Name and Number in ShipperForwarderConsigneeNotify Tab");
                        setBillToCodeForPreviousValue();
                        document.fclBillLaddingform.billToCode[0].checked = false;
                        return;
                    }
                    if (document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED' ||
                            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED / B/L PROVIDED' ||
                            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FRT. FORWARDER ASSIGNED') {
                        alertNew("Please change Bill To Code or change Forwarder, because Forwarder is NO FF ASSIGNED");
                        setBillToCodeForPreviousValue();
                        document.fclBillLaddingform.billToCode[0].checked = false;
                        return;
                    }
                    confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "forwarder");
                } else if (document.fclBillLaddingform.billToCode[1].checked) {
                    checkForAgentOrConsignee();
                    if (shipperForImportValue == "") {
                        alertNew(shipperForImportLable);
                        setBillToCodeForPreviousValue();
                        document.fclBillLaddingform.billToCode[1].checked = false;
                        return;
                    }
                    confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "shipper");
                } else if (document.fclBillLaddingform.billToCode[3].checked) {
                    var collectThirdParty = 'true';
                    checkForAgentOrConsignee();
                    if (agentOrConsignee == "") {
                        alertNew(agentOrCongineeLable);
                        document.fclBillLaddingform.billToCode[3].checked = false;
                        setBillToCodeForPreviousValue(collectThirdParty);
                        return;
                    }
                    var partyName = document.fclBillLaddingform.billToCode[3].value;
                    if (partyName == 'C') {
                        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "fromPrepaidAgent");
                    } else {
                        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "agent");
                    }
                } else if (document.fclBillLaddingform.billToCode[4].checked) {
                    if (document.fclBillLaddingform.notifyParty.value == "") {
                        alertNew("Please select House NotifyParty Name and Number in ShipperForwarderConsigneeNotify Tab");
                        setBillToCodeForPreviousValue();
                        document.fclBillLaddingform.billToCode[4].checked = false;
                        return;
                    }
                    confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "notify");
                }
            }
            function disableBillToCode() {
                disableAddContainer();
                if (document.fclBillLaddingform.houseBL[1].checked) {
                    if (document.fclBillLaddingform.billToCode[3].checked) {
                        checkForAgentOrConsignee();
                        if (agentOrConsignee == "") {
                            alertNew(agentOrCongineeLable);
                            var code = "";
                            var dest = document.getElementById("finalDestination").value;
                            if (dest.lastIndexOf("(") != -1) {
                                code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
                            }
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "getDestCodeforHBL",
                                    param1: code
                                },
                                async: false,
                                success: function (data) {
                                    if (data != null && data != '') {
                                        if (data == 'Y') {
                                            document.fclBillLaddingform.houseBL[0].checked = true;
                                            document.fclBillLaddingform.billToCode[0].checked = true;
                                            document.fclBillLaddingform.billToCode[0].disabled = false;
                                            document.fclBillLaddingform.billToCode[1].disabled = false;
                                            document.fclBillLaddingform.billToCode[2].disabled = false;
                                            setBillToCodeForPreviousValue();
                                            document.getElementById('billThirdPartyName').value = '';
                                            document.getElementById('billTrePty').value = '';
                                            document.getElementById("billThirdPartyName").disabled = true;
                                            document.getElementById("billThirdPartyName").style.border = 0;
                                            document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                                            if (document.getElementById("contactNameButtonForT")) {
                                                document.getElementById("contactNameButtonForT").style.visibility = "hidden";
                                            }
                                            document.getElementById("billTrePty").disabled = true;
                                            document.getElementById("billTrePty").style.border = 0;
                                            document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                                            setBillToParty("Shipper");
                                            document.fclBillLaddingform.billToCode[3].checked = false;
                                            //   document.fclBillLaddingform.billToCode[1].checked = true;
                                        }
                                    }
                                }
                            });
                            return;
                        }
                        document.fclBillLaddingform.billToCode[3].disabled = false;
                    }
                } else {
                    document.fclBillLaddingform.billToCode[0].disabled = false;
                    document.fclBillLaddingform.billToCode[1].disabled = false;
                    document.fclBillLaddingform.billToCode[2].disabled = false;
                    document.fclBillLaddingform.billToCode[3].disabled = false;
                }
                accountDetails();
            }
            function setBillToParty(partyName) {
                if (document.fclBillLaddingform.billTo != undefined) {
                    if (document.fclBillLaddingform.billTo.length != undefined) {
                        for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                            document.fclBillLaddingform.billTo[i].value = partyName;
                        }
                    } else {
                        document.fclBillLaddingform.billTo.value = partyName;
                    }

                }
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "setBillToParty",
                        param1: "<%=bol%>",
                        param2: partyName
                    },
                    success: function (data) {
                        getForwarder(data);
                    }
                });
            }

            function setBillToCodeForPreviousValue(collectThirdParty) {
                if (document.fclBillLaddingform.houseBL[1].checked) {
                    document.fclBillLaddingform.billToCode[3].checked = false;
                    billtoBasedonParty(collectThirdParty);
                    return true;
                } else if (document.fclBillLaddingform.billTo != undefined && document.fclBillLaddingform.billTo.length != undefined) {
                    for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                        if (((document.fclBillLaddingform.billTo[i].value).toLowerCase()) == "forwarder") {
                            document.fclBillLaddingform.billToCode[0].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Shipper") {
                            document.fclBillLaddingform.billToCode[1].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "ThirdParty") {
                            if (collectThirdParty == 'true') {
                            } else {
                                document.fclBillLaddingform.billToCode[2].checked = true;
                            }
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Agent") {
                            document.fclBillLaddingform.billToCode[3].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Consignee") {
                            document.fclBillLaddingform.billToCode[3].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "NotifyParty") {
                            document.fclBillLaddingform.billToCode[4].checked = true;
                            break;
                        }
                    }
                } else {
                    document.fclBillLaddingform.billToCode[1].checked = true;
                }
                accountDetails();
            }
            function billtoBasedonParty(collectThirdParty) {
                if (document.fclBillLaddingform.billTo != undefined && document.fclBillLaddingform.billTo.length != undefined) {
                    for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                        if (((document.fclBillLaddingform.billTo[i].value).toLowerCase()) == "forwarder") {
                            document.fclBillLaddingform.billToCode[0].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Shipper") {
                            document.fclBillLaddingform.billToCode[1].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "ThirdParty") {
                            if (collectThirdParty == 'true') {
                                document.fclBillLaddingform.billToCode[2].checked = true;
                            } else {
                                document.fclBillLaddingform.billToCode[2].checked = true;
                            }
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Agent") {
                            document.fclBillLaddingform.billToCode[3].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "Consignee") {
                            document.fclBillLaddingform.billToCode[3].checked = true;
                            break;
                        } else if (document.fclBillLaddingform.billTo[i].value == "NotifyParty") {
                            document.fclBillLaddingform.billToCode[4].checked = true;
                            break;
                        }
                    }
                }
            }
            function confirmMessageFunction(id1, id2) {
                //  alert(id1);
                if (id1 == 'goBack' && id2 == 'yes') {
                    yesFunction();
                } else if (id1 == 'postAccrualsBeforeManifest' && id2 == 'yes') {
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'deleteBl' && id2 == 'yes') {
                    document.fclBillLaddingform.buttonValue.value = "deleteBL";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'convert' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100008';
                    document.fclBillLaddingform.buttonValue.value = "reverseToBooking";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'convertToQuote' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100022'; //-- refer 'code' in 'genericcode_dup' table --//
                    document.fclBillLaddingform.buttonValue.value = "reverseToQuote";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'goBack' && id2 == 'no') {
                    noFunction();
                } else if (id1 == 'goBack' && id2 == 'cancel') {
                    return;
                } else if (id1 == "forwarder" && id2 == "yes") {
                    document.getElementById('billThirdPartyName').value = '';
                    document.getElementById("billThirdPartyName").disabled = true;
                    document.getElementById("billThirdPartyName").style.border = 0;
                    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                    document.getElementById('billTrePty').value = '';
                    document.getElementById("billTrePty").disabled = true;
                    document.getElementById("billTrePty").style.border = 0;
                    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                    document.fclBillLaddingform.readyToPost.checked = false;
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = "Forwarder";
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = "Forwarder";
                        }
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "Forwarder"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == "forwarder" && id2 == "no") {
                    setBillToCodeForPreviousValue();
                } else if (id1 == "notify" && id2 == "yes") {
                    document.getElementById('billThirdPartyName').value = '';
                    document.getElementById("billThirdPartyName").disabled = true;
                    document.getElementById("billThirdPartyName").style.border = 0;
                    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                    document.getElementById('billTrePty').value = '';
                    document.getElementById("billTrePty").disabled = true;
                    document.getElementById("billTrePty").style.border = 0;
                    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                    document.fclBillLaddingform.readyToPost.checked = false;
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = "NotifyParty";
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = "NotifyParty";
                        }
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "NotifyParty"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == "notify" && id2 == "no") {
                    setBillToCodeForPreviousValue();
                }
                if (id1 == "shipper" && id2 == "yes") {
                    document.getElementById('billThirdPartyName').value = '';
                    document.getElementById("billThirdPartyName").disabled = true;
                    document.getElementById("billThirdPartyName").style.border = 0;
                    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                    document.getElementById('billTrePty').value = '';
                    document.getElementById("billTrePty").disabled = true;
                    document.getElementById("billTrePty").style.border = 0;
                    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                    document.fclBillLaddingform.readyToPost.checked = false;
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = "Shipper";
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = "Shipper";
                        }
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "Shipper"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == "shipper" && id2 == "no") {
                    setBillToCodeForPreviousValue();
                }
                if (id1 == "thirdparty" && id2 == "yes") {
                    document.fclBillLaddingform.readyToPost.checked = false;
                    if (document.fclBillLaddingform.houseBL[2].checked) {
                    } else {
                        document.fclBillLaddingform.billToCode[2].checked = true;
                    }
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = "ThirdParty";
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = "ThirdParty";
                        }
                        document.fclBillLaddingform.billToCode[2].checked = true;
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "ThirdParty"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == "thirdparty" && id2 == "no") {
                    document.getElementById('billThirdPartyName').value = '';
                    document.getElementById("billThirdPartyName").disabled = true;
                    document.getElementById("billThirdPartyName").style.border = 0;
                    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                    document.getElementById('billTrePty').value = '';
                    document.getElementById("billTrePty").disabled = true;
                    document.getElementById("billTrePty").style.border = 0;
                    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                    setBillToCodeForPreviousValue();
                } else if (id1 == "agent" && id2 == "yes") {
                    document.fclBillLaddingform.readyToPost.checked = false;
                    clearThirdParty();
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = "Agent";
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = "Agent";
                        }
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "Agent"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == "agent" && id2 == "no") {
                    var collectThirdParty = 'true';
                    setBillToCodeForPreviousValue(collectThirdParty);
                } else if (id1 == 'deleteCostCode' && id2 == 'yes') {
                    makePageEditableWhileSaving(document.getElementById("fclbl"));
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'Chargedelete' && id2 == 'yes') {
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'readyToPost' && id2 == 'ok') {
                    manifestBl();
                } else if (id1 == 'transferCost' && id2 == 'yes') {
                    document.fclBillLaddingform.transferCost.value = "Yes";
                    autuNotificationMessage('${importFlag}');
                } else if (id1 == 'transferCost' && id2 == 'no') {
                    autuNotificationMessage('${importFlag}');
                } else if (id1 == 'autoNotification' && id2 == 'ok') {
                    document.fclBillLaddingform.readyToPost.checked = false;
                    saved('manifest', '${importFlag}');
                } else if (id1 == 'autoNotification' && id2 == 'cancel') {
                    document.fclBillLaddingform.readyToPost.checked = false;
                } else if (id1 == 'readyToPost' && id2 == 'cancel') {
                    document.getElementById('readyToPost').checked = false;
                } else if (id1 == 'agentFalg' && id2 == 'yes') {
                    autuNotificationMessage(${importFlag});
                } else if (id1 == 'agentFalg' && id2 == 'no') {
                    document.fclBillLaddingform.readyToPost.checked = false;
                } else if (id1 == 'cancelReadyToPost' && id2 == 'ok') {
                    document.fclBillLaddingform.buttonValue.value = "cancelReadyToPost";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'unManifest' && id2 == 'ok') {
                    document.getElementById('eventCode').value = '100010';
                    makePageEditableWhileSaving(document.getElementById("fclbl"));
                    document.fclBillLaddingform.buttonValue.value = "unManifest";
                    document.getElementById("unManifestButton").disabled = true;
                    document.fclBillLaddingform.submit();
                    document.getElementById("manifestButtonUp").enabled = true;
                }
                else if (id1 == 'FAECalculation' && id2 == 'yes') {
                    makePageEditableWhileSaving(document.getElementById("fclbl"));
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'multipleBl' && id2 == 'yes') {
                    if (null == document.getElementById('manifestButtonUp')) {
                        makePageEditableWhileSaving(document.getElementById("fclbl"));
                    }
                    document.getElementById('eventCode').value = '100011';
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'blAudit' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100014';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'blOpned' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100013';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'blClosed' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100012';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'blAuditCancel' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100015';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'voidBl' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100020';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'UnVoidBl' && id2 == 'yes') {
                    document.getElementById('eventCode').value = '100021';
                    saved(id1, '${importFlag}');
                } else if (id1 == 'fromPrepaidAgent' && id2 == 'yes') {
                    document.fclBillLaddingform.readyToPost.checked = false;
                    clearThirdParty();
                    var partyName = document.fclBillLaddingform.billToCode[3].value;
                    var agentOrConsigneeName = 'Agent';
                    if (partyName == 'C') {
                        agentOrConsigneeName = 'Consignee';
                        document.getElementById("consigneeCheck").style.visibility = "hidden";
                    }
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                document.fclBillLaddingform.billTo[i].value = agentOrConsigneeName;
                            }
                        } else {
                            document.fclBillLaddingform.billTo.value = agentOrConsigneeName;
                        }
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: agentOrConsigneeName
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                } else if (id1 == 'fromPrepaidAgent' && id2 == 'no') {
                    setBillToCodeForPreviousValue();
                } else if (id1 == 'clearNotifyParty' && id2 == 'ok') {
                    clearNotifyParty();
                    document.getElementById("notifyParty").disabled = true;
                    document.getElementById("notifyParty").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearNotifyParty' && id2 == 'cancel') {
                    document.getElementById("notifyCheck").checked = false;
                } else if (id1 == 'clearConsignee' && id2 == 'ok') {
                    clearConsignee();
                    document.getElementById("consignee").disabled = true;
                    document.getElementById("consignee").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearConsignee' && id2 == 'cancel') {
                    document.getElementById("consigneeCheck").checked = false;
                } else if (id1 == 'clearMasterNotifyParty' && id2 == 'ok') {
                    clearMasterNotify();
                    document.getElementById("houseNotifyParty").disabled = true;
                    document.getElementById("houseNotifyParty").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearMasterNotifyParty' && id2 == 'cancel') {
                    document.getElementById("masterNotifyCheck").checked = false;
                } else if (id1 == 'clearMasterConsignee' && id2 == 'ok') {
                    clearMasterConsignee();
                    document.getElementById("houseConsignee").disabled = true;
                    document.getElementById("houseConsignee").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearMasterConsignee' && id2 == 'cancel') {
                    document.getElementById("masterConsigneeCheck").checked = false;
                } else if (id1 == 'OnUncheckClearNotify' && id2 == 'ok') {
                    clearNotifyParty();
                    document.getElementById("notifyParty").disabled = false;
                    document.getElementById("notifyParty").style.border = '1px solid #C4C5C4';
                    document.getElementById("notifyParty").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearNotify' && id2 == 'cancel') {
                    document.getElementById("notifyCheck").checked = true;
                } else if (id1 == 'OnUncheckClearConsignee' && id2 == 'ok') {
                    clearConsignee();
                    document.getElementById("consignee").disabled = false;
                    document.getElementById("consignee").style.border = '1px solid #C4C5C4';
                    document.getElementById("consignee").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearConsignee' && id2 == 'cancel') {
                    document.getElementById("consigneeCheck").checked = true;
                } else if (id1 == 'OnUncheckClearMasterNotify' && id2 == 'ok') {
                    clearMasterNotify();
                    document.getElementById("houseNotifyParty").disabled = false;
                    document.getElementById("houseNotifyParty").style.border = '1px solid #C4C5C4';
                    document.getElementById("houseNotifyParty").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearMasterNotify' && id2 == 'cancel') {
                    document.getElementById("masterNotifyCheck").checked = true;
                } else if (id1 == 'OnUncheckClearMasterConsignee' && id2 == 'ok') {
                    clearMasterConsignee();
                    document.getElementById("houseConsignee").disabled = false;
                    document.getElementById("houseConsignee").style.border = '1px solid #C4C5C4';
                    document.getElementById("houseConsignee").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearMasterConsignee' && id2 == 'cancel') {
                    document.getElementById("masterConsigneeCheck").checked = true;
                } else if (id1 == 'clearHouseShipper' && id2 == 'ok') {
                    clearHouseShipper();
                    document.getElementById("shipper").disabled = true;
                    document.getElementById("shipper").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearHouseShipper' && id2 == 'cancel') {
                    document.getElementById("houseShipperCheck").checked = false;
                } else if (id1 == 'onUnCheckClearHouseShipper' && id2 == 'ok') {
                    clearHouseShipper();
                    document.getElementById("shipper").disabled = false;
                    document.getElementById("shipper").style.border = '1px solid #C4C5C4';
                    document.getElementById("shipper").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'onUnCheckClearHouseShipper' && id2 == 'cancel') {
                    document.getElementById("houseShipperCheck").checked = true;
                } else if (id1 == 'UncheckClearNotify' && id2 == 'yes') {
                    clearNotifyParty();
                } else if (id1 == 'UncheckClearConsignee' && id2 == 'yes') {
                    clearConsignee();
                } else if (id1 == 'UncheckClearMasterNotify' && id2 == 'yes') {
                    clearMasterNotify();
                } else if (id1 == 'UncheckClearMasterConsignee' && id2 == 'yes') {
                    clearMasterConsignee();
                } else if (id1 == 'UnCheckClearHouseShipper' && id2 == 'yes') {
                    clearHouseShipper();
                } else if (id1 == 'UncheckClearConsignee' && id2 == 'no') {
                    document.getElementById("consigneeCheck").checked = true;
                } else if (id1 == 'UncheckClearMasterNotify' && id2 == 'no') {
                    document.getElementById("masterNotifyCheck").checked = true;
                } else if (id1 == 'UncheckClearMasterConsignee' && id2 == 'no') {
                    document.getElementById("masterConsigneeCheck").checked = true;
                } else if (id1 == 'UncheckClearNotify' && id2 == 'no') {
                    document.getElementById("notifyCheck").checked = true;
                } else if (id1 == 'clearShipperForEdi' && id2 == 'ok') {
                    clearShipperForEdi();
                } else if (id1 == 'clearShipperForEdi' && id2 == 'cancel') {
                    document.getElementById("ediShipperCheck").checked = false;
                } else if (id1 == 'clearConsigneeForEdi' && id2 == 'ok') {
                    clearConsigneeForEdi();
                } else if (id1 == 'clearConsigneeForEdi' && id2 == 'cancel') {
                    document.getElementById("ediConsigneeCheck").checked = false;
                } else if (id1 == 'clearNotifyForEdi' && id2 == 'ok') {
                    clearNotifyForEdi();
                } else if (id1 == 'clearNotifyForEdi' && id2 == 'cancel') {
                    document.getElementById("ediNotifyPartyCheck").checked = false;
                } else if (id1 == 'deleteInsurance' && id2 == 'ok') {
                    document.fclBillLaddingform.buttonValue.value = "deleteInsurance";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'deleteInsurance' && id2 == 'cancel') {
                    document.fclBillLaddingform.insurance[0].checked = true;
                } else if (id1 == 'addBreakBulkHazmat' && id2 == 'yes') {
                    var idOfContainer = document.getElementById("idOfContainer").value;
                    var containerIndex = document.getElementById("containerIndex").value;
                    openHazmatPopUp(containerIndex, idOfContainer, '${fclBl.bol}', '${fclBl.fileNo}', '${fclBl.readyToPost}');
                } else if (id1 == 'aesDetails' && id2 == 'yes') {
                    document.fclBillLaddingform.buttonValue.value = "deleteAesDetails";
                    document.fclBillLaddingform.submit();
                } else if (id1 == 'deleteContainer' && id2 == 'yes') {
                    document.fclBillLaddingform.buttonValue.value = "deleteContainer";
                    document.fclBillLaddingform.submit();
                } else if (id1 == "Econo/Ecu Worldwide" && id2 == "yes") {
                    document.fclBillLaddingform.brand.value = "Econo";
                    submitBrand("addBrandValue");
                } else if (id1 == "OTI/Ecu Worldwide" && id2 == "yes") {
                    document.fclBillLaddingform.brand.value = "OTI";
                    submitBrand("addBrandValue");
                }
                else if (id1 == "Ecu Worldwide/Econo" && id2 == "yes") {
                    document.fclBillLaddingform.brand.value = "Ecu Worldwide";
                    submitBrand("addBrandValue");
                } else if (id1 === "Ecu Worldwide/OTI" && id2 === "yes") {
                    document.fclBillLaddingform.brand.value = "Ecu Worldwide";
                    submitBrand("addBrandValue");
                } else if (id1 === "Econo/Ecu Worldwide" && id2 == "no") {
                    var data = "Econo/Ecu Worldwide";
                    var splitarray = data.split("/");
                    var splitarray1 = splitarray[1];

                    if (splitarray1 === "Ecu Worldwide") {
                        document.getElementById('brandEcono').checked = false;
                        document.getElementById('brandEcuworldwide').checked = true;
                    }
                } else if (id1 === "OTI/Ecu Worldwide" && id2 == "no") {
                    var data = "OTI/Ecu Worldwide";
                    var splitarray = data.split("/");
                    var splitarray1 = splitarray[1];

                    if (splitarray1 === "Ecu Worldwide") {
                        document.getElementById('brandOti').checked = false;
                        document.getElementById('brandEcuworldwide').checked = true;
                    }
                } else if (id1 === "Ecu Worldwide/Econo" && id2 == "no") {
                    var data = "Ecu Worldwide/Econo";
                    var splitarray = data.split("/");
                    var splitarray1 = splitarray[1];

                    if (splitarray1 === "Econo") {
                        document.getElementById('brandEcono').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    }
                }
                else if (id1 === "Ecu Worldwide/OTI" && id2 == "no") {
                    var data = "Ecu Worldwide/OTI";
                    var splitarray = data.split("/");
                    var splitarray1 = splitarray[1];

                    if (splitarray1 === "OTI") {
                        document.getElementById('brandOti').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    }
                }
            }
            function submitBrand(btnValue) {
                makePageEditableWhileSaving(document.getElementById("fclbl"));
                document.fclBillLaddingform.buttonValue.value = btnValue;
                document.fclBillLaddingform.submit();
            }
            function displayContacts(manifestWithoutCharges) {
                document.fclBillLaddingform.readyToPost.checked = true;
            }
            function displayAutoNotificationContacts(manifestWithoutCharges) {
         
    if (document.getElementById('contactConfigDetails') !== null && manifestWithoutCharges !== 'yes') {
        showPopUp();
        document.getElementById('contactConfigDetails').style.display = 'block';
    } else {
        jQuery("#readyToPost").attr("checked", true);
    }
            }
            var veriftETA = '', Comments = '', radioBoxValue = '';
            function openImportReleasePopUp() {
                if (!saveMessage('Adding Import Release')) {
                    return false;
                }
                var bol = '<%=bol%>';
                GB_show('Payments', "${path}/paymentRelease.do?action=showHome&bolId=" + bol, width = "400", height = "1000");

            }
            function closeImportReleasePopUp() {
                document.fclBillLaddingform.importVerifiedEta.value = veriftETA;
                document.fclBillLaddingform.importReleaseComments.value = Comments;
                if (radioBoxValue == 'Y') {
                    document.fclBillLaddingform.importRelease[0].checked = true;
                    if (null != document.getElementById("cal99")) {
                        document.getElementById("cal99").style.visibility = "visible";
                    }
                    if (null != document.getElementById("importCommentsId")) {
                        document.getElementById("importCommentsId").disabled = false;
                    }
                } else if (radioBoxValue == 'N') {
                    document.fclBillLaddingform.importRelease[1].checked = true;
                    if (null != document.getElementById("cal99")) {
                        document.getElementById("cal99").style.visibility = "hidden";
                    }
                    if (null != document.getElementById("importCommentsId")) {
                        document.getElementById("importCommentsId").disabled = true;
                    }
                }
                closePopUp();
                document.getElementById('importReleasePopUp').style.display = 'none';
            }
            function closeChargesPopup() {
                document.fclBillLaddingform.verifyEta.value = veriftETA;
                document.fclBillLaddingform.confOnBoardComments.value = Comments;
                if (radioBoxValue == 'Y') {
                    document.fclBillLaddingform.confirmOnBoard[0].checked = true;
                    document.getElementById("cal4").style.visibility = "visible";
                    document.getElementById("confOnBoardComments").disabled = false;
                    document.getElementById("verifiedEtaCheck").disabled = false;
                    document.getElementById("verifiedEtaCheck").checked = false;
                } else if (radioBoxValue == 'N') {
                    document.fclBillLaddingform.confirmOnBoard[1].checked = true;
                    document.getElementById("cal4").style.visibility = "hidden";
                    document.getElementById("confOnBoardComments").disabled = true;
                    document.getElementById("verifiedEtaCheck").disabled = true;
                }
                closePopUp();
                document.getElementById('confirmOnBoard').style.display = 'none';
            }
            function backToBooking() {
                var bol = '<%=bol%>';
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "checkForAssignedHazmats",
                        param1: bol
                    },
                    success: function (data) {
                        if (data != null && data != "") {
                            alertNew("Unassign all assigned hazmats");
                            return;
                        } else {
                            jQuery.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.logiware.dwr.FclDwr",
                                    methodName: "checkCostIsPaid",
                                    param1: bol,
                                    dataType: "json"
                                },
                                success: function (result) {
                                    if (result == true) {
                                        alertNew("Can't able to reverse to booking for already cost paid files");
                                        return;
                                    } else {
                                        confirmYesOrNo("Are you sure? You want to convert this BL to Booking", "convert");
                                    }
                                }
                            });
                        }
                    }
                });
            }
            function gotoInbond(bol, fileNo) {
                var pod = document.getElementById('portofdischarge').value;
                if (!saveMessage('Adding INBONDS')) {
                    return false;
                }
                GB_show('Inbond Details', '${path}/fclInbondDetails.do?buttonValue=' + 'fclbl&bolid=' + bol + '&fileNo=' + fileNo + '&podValues=' + pod, 320, 1000);
            }
            function disableBillToCodeonLoad() {
                if ('${fclBl.fileNo}' === '') {
                    document.fclBillLaddingform.houseBL[1].checked = true;
                    document.fclBillLaddingform.billToCode[3].checked = true;
                }
            }

            //            function directConsign(obj) {
            //                if (obj.checked) {
            //                    directConsignCheck();
            //                } else {
            //                    document.getElementById("defaultAgentY").checked = true;
            //                    fillDefaultAgent();
            //                }
            //            }

            function getBillTO() {//HOUSE bl GO COLLECT
                var code = "";
                var ready = '<%=ready%>';
                var importFlag = '${importFlag}';
                var dest = document.getElementById("finalDestination").value;
                if (dest.lastIndexOf("(") != -1) {
                    code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
                }
                if (ready != 'M' && ready != 'm') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "getDestCodeforHBL",
                            param1: code
                        },
                        async: false,
                        success: function (data) {
                            if (data != null && data != '') {
                                if (data == 'N') {//pripaid and both
                                    document.fclBillLaddingform.houseBL[0].disabled = false;
                                    document.fclBillLaddingform.houseBL[1].checked = false;
                                    document.fclBillLaddingform.houseBL[1].disabled = true;
                                    document.fclBillLaddingform.houseBL[2].disabled = false;
                                    document.fclBillLaddingform.billToCode[0].disabled = false;
                                    document.fclBillLaddingform.billToCode[1].disabled = false;
                                    document.fclBillLaddingform.billToCode[2].disabled = false;
                                    document.fclBillLaddingform.billToCode[3].disabled = false;
                                    document.fclBillLaddingform.billToCode[3].checked = false;
                                    if ((document.fclBillLaddingform.houseBL[0].checked == true) || (document.fclBillLaddingform.houseBL[0].checked == false && document.fclBillLaddingform.houseBL[1].checked == false
                                            && document.fclBillLaddingform.houseBL[2].checked == false && document.fclBillLaddingform.billToCode[0].checked == false
                                            && document.fclBillLaddingform.billToCode[1].checked == false && document.fclBillLaddingform.billToCode[2].checked == false && document.fclBillLaddingform.billToCode[3].checked == false)) {
                                        if (document.fclBillLaddingform.houseBL[0].checked == false && document.fclBillLaddingform.houseBL[1].checked == false
                                                && document.fclBillLaddingform.houseBL[2].checked == false && document.fclBillLaddingform.billToCode[0].checked == false
                                                && document.fclBillLaddingform.billToCode[1].checked == false && document.fclBillLaddingform.billToCode[2].checked == false
                                                && document.fclBillLaddingform.billToCode[3].checked == false) {
                                            document.fclBillLaddingform.houseBL[1].checked = true;
                                            document.fclBillLaddingform.billToCode[3].checked = true;
                                            //setBillToParty("Shipper");
                                        }
                                    }
                                    if (ready == 'M' || ready == 'm') {
                                        document.fclBillLaddingform.houseBL[0].disabled = true;
                                        document.fclBillLaddingform.houseBL[1].disabled = true;
                                        document.fclBillLaddingform.houseBL[2].disabled = true;
                                        document.fclBillLaddingform.billToCode[0].disabled = true;
                                        document.fclBillLaddingform.billToCode[1].disabled = true;
                                        document.fclBillLaddingform.billToCode[2].disabled = true;
                                        document.fclBillLaddingform.billToCode[3].disabled = true;
                                    }
                                } else if (data == 'Y') {//pripaid collect and both
                                    document.fclBillLaddingform.houseBL[0].disabled = false;
                                    document.fclBillLaddingform.houseBL[1].disabled = false;
                                    document.fclBillLaddingform.houseBL[2].disabled = false;
                                    if (ready == 'M' || ready == 'm') {
                                        document.fclBillLaddingform.houseBL[0].disabled = true;
                                        document.fclBillLaddingform.houseBL[1].disabled = true;
                                        document.fclBillLaddingform.houseBL[2].disabled = true;
                                        document.fclBillLaddingform.billToCode[0].disabled = true;
                                        document.fclBillLaddingform.billToCode[1].disabled = true;
                                        document.fclBillLaddingform.billToCode[2].disabled = true;
                                        document.fclBillLaddingform.billToCode[3].disabled = true;
                                    }
                                }
                                document.fclBillLaddingform.fclSsblGoCollect.value = data;
                            }
                        }
                    });
                }
            }
            function checkFFcomm() {
                if (document.fclBillLaddingform.houseBL[2].checked && (document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED' ||
                        document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED / B/L PROVIDED' ||
                        document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FRT. FORWARDER ASSIGNED')) {
                    if (document.fclBillLaddingform.billTo != undefined) {
                        if (document.fclBillLaddingform.billTo.length != undefined) {
                            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                                if (((document.fclBillLaddingform.billTo[i].value).toLowerCase()) == "forwarder")
                                {
                                    document.fclBillLaddingform.billTo[i].value = "Shipper";
                                }
                            }
                        }
                        accountDetails();
                    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "setBillToParty",
                            param1: "<%=bol%>",
                            param2: "Shipper"
                        },
                        success: function (data) {
                            getForwarder(data);
                        }
                    });
                    disableEditShipperCheck();
                }
                hideAgentNameCheck();
            }

        </script>
        <style>
            .info-box
            {
                border: 1px solid #C4C5C4;float: left;color: #000000;padding:1px;
                margin: 2px;
            }

            .pagelinks{
                float:left;
            }
        </style>
    </head>
    <div id="cover" style="width: 906px ;height: 1000px;"></div>
    <div id="bubble_tooltip_forComments" style="display: none;">
        <div class="bubble_top_forComments"><span></span></div>
        <div class="bubble_middle_forComments"><span id="bubble_tooltip_content_forComments"></span></div>
        <div class="bubble_bottom_forComments"></div></div>

    <body class="whitebackgrnd" topmargin="1">
        <%@include file="../../../../jsps/preloader.jsp"%>
        <uns:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="commentDiv"   class="comments">
            <table border="1" id="commentTableInfo">
                <tbody border="0"></tbody>
            </table>
        </div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>

        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>
        <div id="AlertBoxOk" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText3" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="confirmFunction()">
            </form>
        </div>
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
                       onclick="No()">
            </form>
        </div>

        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()">
            </form>
        </div>

        <div id="ConfirmYesNoCancelDiv" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="confirmMessagePara" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmOptionYes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="No"
                       onclick="confirmOptionNo()">
                <input type="button" id="confirmCancel"  class="buttonStyleForAlert" value="Cancel"
                       onclick="confirmOptionCancel()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <html:form action="/fclBillLadding?accessMode=${param.accessMode}" styleId="fclbl" name="fclBillLaddingform"
                   type="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm" scope="request">
            <b style="margin-left:200px;color: #000080;font-size: 15px;"><%=lockMessage%></b>
            <%@include file="../fclQuotes/FclPopUps.jsp"  %>
            <html:hidden property="buttonValue" value="<%=buttonValue%>"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="textlabels">
                        <table   border="0" width="90%">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>File BOL No :
                                                <font color="red" size="4">
                                                <c:out value="${companyCode}-"/><c:out value="<%=billOfLadding%>"/>
                                                </font>
                                            </td>
                                        </tr>
                                    </table>
                                </td>

                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>

                                                <div style="float:left">
                                                    <c:if test="${fclBl.readyToPost=='M'}">
                                                        <font color="green" size="4"><b id="mes"><%="Manifested,"%></b></font>
                                                    </c:if>
                                                    <c:if test="${fclBl.closedBy!=null}">
                                                        <font color="green" size="4"><b ><%="Closed,"%></b></font>
                                                    </c:if>
                                                    <c:if test="${fclBl.auditedBy!=null}">
                                                        <font color="green" size="4"><b ><%="Audited,"%></b></font>
                                                    </c:if>
                                                    <c:if test="${fclBl.voidBy!=null}">
                                                        <font color="green" size="4"><b ><%="Voided,"%></b></font>
                                                    </c:if>
                                                </div>
                                                <div style="float:left">
                                                    <c:choose>
                                                        <c:when test="${fclBl.overPaidStatus}">
                                                            <c:set var="overPaid" value="Over Paid"/>
                                                            <input type="text" value="${overPaid}," readonly="true" style="display: block;color: green;float:left;border: 0;font-size: 18px;width:100px;font-weight: bold"  id="paidStatus"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="text" value="${overPaid}," readonly="true" style="display: none"  id="paidStatus"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <c:if test="${hazmatidentity=='hazmatidentity'}">
                                                <td><font color="red" size="2"><b>HAZARDOUS CARGO</b></font>
                                                </td>
                                            </c:if>
                                            <c:if test="${bulletRates eq true}">
                                                <td colspan="6" align="center"  style="font-size:large;font-weight: bolder;color: #fd0000"> BULLET RATES </td>
                                            </c:if>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td><c:choose>
                                                    <c:when test="${fclBl.readyToPost!='M'}">
                                                        <font color="blue" size="2"><b>${msg}</b></font>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <font color="red" size="2"><b>${msg}</b>
                                                            <%if (noticeNo != null) {%>
                                                        Corrected BL	Notice No : <%=noticeNo%>
                                                        <%}%>
                                                        </font>
                                                    </c:otherwise>
                                                </c:choose></td>
                                        </tr>
                                    </table>
                                </td>
                                <c:choose>
                                    <c:when test="${fclBl.ratesNonRates == 'N'}">
                                        <td align="right"  style="padding-left:250px;" class="textlabelsBold">
                                            Non-Rated<input type="radio" id="nonRated" checked  disabled="true"/>&nbsp;&nbsp;
                                            Break Bulk
                                            <html:radio property="breakBulk" value="Y" name="fclBillLaddingform" styleId="breakBulkY"  disabled="true"/>Y
                                            <html:radio property="breakBulk" value="N" name="fclBillLaddingform" styleId="breakBulkN"  disabled="true"/>N
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td align="right"  style="padding-left:350px;" class="textlabelsBold">
                                            <span id="rateId">Rated<input type="radio" id="rated" checked disabled="true"/></span>&nbsp;&nbsp;
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <td align="right" class="textlabelsBold">
                                    Brand
                                    <c:choose>
                                        <c:when test="${companyCodeValue == '03'}">
                                            <html:radio property="brand" value="Econo" name="fclBillLaddingform" styleId="brandEcono" onclick="checkBrand('Econo','${fclBl.bolId}','${companyCodeValue}')"/>Econo
                                        </c:when>
                                        <c:otherwise>
                                            <html:radio property="brand" value="OTI"  name="fclBillLaddingform" styleId="brandOti" onclick="checkBrand('OTI','${fclBl.bolId}','${companyCodeValue}')"/>OTI
                                        </c:otherwise>
                                    </c:choose>
                                    <html:radio property="brand" value="${commonConstants.ECU_Worldwide}" name="fclBillLaddingform" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${fclBl.bolId}','${companyCodeValue}')"/>Ecu Worldwide

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td class="textlabels">`
                        <table border="0">
                            <tr>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabels">
                                            <td class="textlabelsBold">Quote By :&nbsp;<b class="headerlabel" style="color:blue">
                                                    <c:out value="${fn:toUpperCase(fclBl.quoteBy)}" /></b></td>
                                            <td class="textlabelsBold">On :&nbsp;
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="quoteDate1" value="${fclBl.quoteDate}"/>
                                                <b class="headerlabel" style="color:blue"><c:out value="${quoteDate1}" ></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr>
                                                <td class="textlabelsBold">Booking By :&nbsp;
                                                    <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBl.bookingBy)}" /></b></td>
                                            <td style="padding-left:5px;" class="textlabelsBold">On :&nbsp;
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="bookingDate1" value="${fclBl.bookingDate}"/>
                                                <b class="headerlabel" style="color:blue"><c:out value="${bookingDate1}"></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr>
                                                <td class="textlabelsBold">BL By :&nbsp; <b class="headerlabel"    style="text-transform:uppercase;color:blue">${fclBl.blBy}
                                                </b></td>
                                            <td style="padding-left: 5px;" class="textlabelsBold">
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="billofDate" value="${fclBl.bolDate}"/>
                                                <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" var="fclBolDate" value="${fclBl.bolDate}"/>
                                                On :&nbsp;<b class="headerlabel" style="color:blue"><c:out value="${billofDate}"></c:out></b>
                                                <html:hidden property="billofdate" value="${fclBolDate}"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr>
                                            <td id="manifestid" class="textlabelsBold"><span>Manifested By :&nbsp;<b id="manifestedby" style="color:blue" class="headerlabel">
                                                        <c:out value="${fn:toUpperCase(fclBl.manifestedBy)}"/></b></span>
                                                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="manifestedDate" value="${fclBl.manifestedDate}"/>
                                                <span>On :&nbsp;<b id="manifestedon" style="color:blue" class="headerlabel">
                                                        <c:out value="${manifestedDate}"/></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr>
                                            <td id="ediStatusDiv" class="textlabelsBold"><span>EDI By :&nbsp;<b id="ediby" style="color:blue"class="headerlabel" >
                                                        <c:out value="${fn:toUpperCase(fclBl.ediCreatedBy)}"/></b>&nbsp;</span>
                                                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="ediCreatedOn" value="${fclBl.ediCreatedOn}"/>
                                                <span>On :&nbsp;<b id="edion" style="color:blue" class="headerlabel">
                                                        <c:out value="${ediCreatedOn}"/></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="textlabels">
                        <table border="0">
                            <tr>
                                <td id="confirmBoardid">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td >
                                                <span>Confirm on board By :&nbsp;<b id="confirmBoardby" style="color:blue" class="headerlabel">
                                                        <c:out value="${fn:toUpperCase(fclBl.confirmBy)}"/></b>
                                                </span>
                                                <span style="padding-left:5px;">On :&nbsp;
                                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="confirmdDate"
                                                                    value="${fclBl.confirmOn}"/>
                                                    <b id="confirmBoardon" style="color:blue" class="headerlabel"><c:out value="${confirmdDate}"/></b>
                                                </span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td id="closedid">
                                                <span  style="padding-left:5px;">Closed By :&nbsp;
                                                    <input type="hidden" id="blClosedBy" value="${fclBl.closedBy}"/>
                                                    <b id="closedby" style="color:blue" class="headerlabel"><c:out value="${fclBl.closedBy}"/></b></span>
                                                <span style="padding-left:5px;">On :&nbsp;
                                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="closedDate"
                                                                    value="${fclBl.closedDate}"/>
                                                    <b id="closedon" style="color:blue" class="headerlabel"><c:out value="${closedDate}"/></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td class="textlabelsBold">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td id="auditid">
                                                <span style="padding-left:5px;">Audited By :&nbsp;
                                                    <input type="hidden" id="blAuditedBy" value="${fclBl.auditedBy}"/>
                                                    <b id="auditedby" style="color:blue"><c:out value="${fclBl.auditedBy}"/></b></span>
                                                <span style="padding-left:5px;">On :&nbsp;
                                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="auditedDate"
                                                                    value="${fclBl.auditedDate}"/>
                                                    <b id="auditedon" style="color:blue">
                                                        <c:out value="${auditedDate}"/></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td><table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td id="receivedid">
                                                <span style="padding-left:5px;">Received Master By :&nbsp;<b id="receivedby" style="color:blue"></b></span>
                                                <span>On :&nbsp;<b id="receivedon" style="color:blue"></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr>
                                            <td id="paymentStatusDiv" class="textlabelsBold"><span>Payment By :&nbsp;<b id="paymentBy" style="color:blue">
                                                        <c:out value="${fn:toUpperCase(fclBl.amountAndPaidBy)}"/></b>&nbsp;</span>
                                                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="paymentReleasedOn" value="${fclBl.paymentReleasedOn}"/>
                                                <span>On :&nbsp;<b id="paymentOn" style="color:blue">
                                                        <c:out value="${paymentReleasedOn}"/></b></span>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <div class="info-box">
                                        <b class="textlabelsBold">AR Balance :</b>&nbsp;
                                        <c:set var="fileNo" value="${fclBl.fileNo}"/>
                                        <c:choose>
                                            <c:when test="${!fn:contains(fclBl.fileNo,'-')}">
                                                <c:set var="query" value="select format(coalesce(sum(balance), 0.00), 2) as balance"/>
                                                <c:set var="query" value="${query} from transaction"/>
                                                <c:set var="query" value="${query} where transaction_type='AR'"/>
                                                <c:set var="query" value="${query} and drcpt = '${fclBl.fileNo}'"/>
                                                <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                                <b class="headerlabel" style="color:blue">${result}</b>&nbsp
                                                <img src="${path}/images/icons/currency_blue.png" onmouseover="tooltip.show('<strong>Show Transactions History</strong>', null, event);"
                                                     onmouseout="tooltip.hide();"align="top" onclick="showTransactions('${path}', '${importFlag}', '${fileNo}');"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="query" value="select format(coalesce(sum(balance), 0.00), 2) as balance"/>
                                                <c:set var="query" value="${query} from transaction"/>
                                                <c:set var="query" value="${query} where transaction_type='AR'"/>
                                                <c:set var="query" value="${query} and bill_ladding_no = '${fclBl.bolId}'"/>
                                                <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                                <b class="headerlabel" style="color:blue">${result}</b>&nbsp
                                                <img src="${path}/images/icons/currency_blue.png" onmouseover="tooltip.show('<strong>Show Transactions History</strong>', null, event);"
                                                     onmouseout="tooltip.hide();"align="top" onclick="showTransactions('${path}', '${importFlag}', '${fileNo}');"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td>
                                    <c:if test="${goBackToInquiry!=null}">
                                        <input type="button" value="Go Back" class="buttonStyleNew"
                                               onclick="goToARInquiryPage()"/>
                                    </c:if>
                                    <c:if test="${goBackToInquiry==null}">
                                        <input type="button" value="Go Back" id="goBack1" class="buttonStyleNew"
                                               onclick="compareWithOldArray('<%=billOfLadding%>', '${fclBl.bol}')"/>
                                    </c:if>
                                    <input type="button" value="Save" id="save" class="buttonStyleNew" onclick="saved('<%=action%>', '${importFlag}')" onmouseover="tooltip.show('<strong><%=mandatoryFieldForBl%></strong>', null, event);" onmouseout="tooltip.hide();"/>

                                    <%if (!readyToPost.equals("M")) {
                                            if (fileNo.indexOf("-") < 1 && !showReverseToBooking) {%>
                                    <c:if test="${newBl == 'edit'}">
                                        <input type="button" value="ReverseToBooking" id="reverseToBooking"
                                               class="buttonStyleNew"  onclick="backToBooking()" style="width:120px;"/>
                                    </c:if>
                                    <c:if test="${newBl == 'fromQuote'}">
                                        <input type="button" value="Reverse To Quote" id="reverseToQuoteHeader"
                                               class="buttonStyleNew"  onclick="reverseToQuotation('${fclBl.bol}')" style="width:120px;"/>
                                    </c:if>
                                    <%}%>
                                    <input type="button" value="Manifest" id="manifestButtonUp" class="buttonStyleNew"
                                           onclick="manifest1('${importFlag}')"/>
                                    <%} else {%>
                                    <input type="button" value="UnManifest" class="buttonStyleNew"
                                           id="unManifestButton"   onclick="unManifest('<%=billOfLadding%>');"/>
                                    <c:choose>
                                        <c:when test="${fclBl.closedDate==null}">
                                            <c:if test="${roleDuty.closeBl}">
                                                <input type="button" class="buttonStyleNew" value="Close"
                                                       id="blClosedButton" onclick="closedAudit('blClosed', '${importFlag}', '${roleDuty.auditOverride}');"/>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${roleDuty.reopenBl}">
                                                <input type="button" class="buttonStyleNew" value="Open"
                                                       id="blOpnedButton" onclick="closedAudit('blOpned');"/>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:choose>
                                        <c:when test="${fclBl.auditedDate==null}">
                                            <c:if test="${roleDuty.audit}">
                                                <input type="button" class="buttonStyleNew" value="Audit"
                                                       id="blAuditedButton" style="width:50px" onclick="closedAudit('blAudit')"/>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${roleDuty.cancelAudit}">
                                                <input type="button" class="buttonStyleNew" value="Cancel Audit"
                                                       id="blAuditedCancelButton" style="width:90px" onclick="closedAudit('blAuditCancel')"/>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>


                                    <%if (latestBolid != null) {%>

                                    <input type="button" value="FCLBLCorrection" class="buttonStyleNewRed" id="correctionButton"
                                           onclick="FCLBLCorrection('<%=billOfLadding%>', '${fclBl.fileNo}')"
                                           style="width:100px"/>
                                    <%} else {
                                        if (billOfLadding != null && billOfLadding.indexOf(FclBlConstants.DELIMITER) < 0) {
                                    %>
                                    <input type="button" value="FCLBLCorrection" class="buttonStyleNew" id="correctionButton"
                                           onclick="FCLBLCorrection('<%=billOfLadding%>', '${fclBl.fileNo}')"
                                           style="width:100px"/>
                                    <%
                                                }
                                            }
                                        }%>

                                    <c:if test="${not empty fclBl.fileNo && newBl != 'edit' && fclBl.readyToPost != 'M'}">
                                        <c:choose>
                                            <c:when test="${fclBl.blVoid == 'Y'}">
                                                <input type="button" class="buttonStyleNew" value="UnVoid"
                                                       id="voidButton"  onclick="unVoidBl()"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" class="buttonStyleNew" value="Void"
                                                       id="voidButton"  onclick="voidBl('${fclBl.bolId}')"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:set var="manualNotesCount" value="buttonStyleNew"/>
                                    <c:if test="${ManualNotes}">
                                        <c:set var="manualNotesCount" value="buttonColor"/>
                                    </c:if>
                                    <input type="button" class="${manualNotesCount}" id="note"  name="search" value="Note"
                                           onclick="return GB_show('Notes', '${path}/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '${fclBl.fileNo}', 350, 750);" />

                                    <input type="button" id="importReleaseButton" value="Import Release" class="buttonStyleNew"
                                           onclick="openImportReleasePopUp()" style="width:100px"/>
                                    <input id="inbondButton" type="button" value="Inbond" class="buttonStyleNew"
                                           onclick="gotoInbond('<%=bol%>', '${fclBl.fileNo}')" style="width:60px;" />
                                    <input type="button" id="printFax" value="Print/Fax/Email" class="buttonStyleNew"
                                           onclick="PrintReports('${importFlag}')" style="width:100px"/>
                                    <c:choose>
                                        <c:when test="${null!=TotalScan && TotalScan!='0'}">
                                            <input id="scanButton" class="buttonColor" type="button"
                                                   value="Scan/Attach" onClick="scan('${fclBl.fileNo}', '${importFlag}')"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input id="scanButton" class="buttonStyleNew" type="button"
                                                   value="Scan/Attach" onClick="scan('${fclBl.fileNo}', '${importFlag}')"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="button" id="arRedInvoice" value="AR Invoice" class="buttonStyleNew"
                                           onclick="arInvoice('${fclBl.fileNo}', '${importFlag}')"/>
                                    <input type="button" id="sendEdi" value="Send EDI" class="buttonStyleNew"
                                           onclick="generateXml('${fclBl.fileNo}', '<%=intraGtnexus%>', '<%=bol%>', '<%=userName%>', 'create')"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <br style="margin-top:0px"/>
            <table  border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td valign="top" style="border: 1px solid #C4C5C4;">
                        <table border="0"  width="100%" cellpadding="2" cellspacing="0" >
                            <tr class="tableHeadingNew"><td colspan="4">General Information</td></tr>
                            <tr class="textlabelsBold">
                                <td align="right" colspan="3">
                                    House BL Prepaid/Collect
                                </td>
                                <td>
                                    <html:radio property="houseBL" value="P"  name="transactionBean"
                                                onclick="disableBillToCode()" styleId="streamShipBLPrepaid"/>P
                                    <html:radio property="houseBL" value="C"  name="transactionBean"
                                                onclick="disableBillToCode()" />C
                                    <html:radio property="houseBL" value="B" styleId="houseBlBoth" name="transactionBean"
                                                onclick="disableBillToCode();"/>Both
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">
                                    SSL BL Destination Charges
                                </td>
                                <td colspan="2">
                                    <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                        <tr class="textlabelsBold">
                                            <td align="left">
                                                <html:radio property="destinationChargesPreCol" value="P" name="transactionBean" styleId="sslBlPrepaid"/>P
                                                <html:radio property="destinationChargesPreCol" value="C" name="transactionBean" styleId="sslBlCollect"/>C
                                            </td>
                                            <td align="right">
                                                &nbsp;&nbsp;<span id="warningMessage" class="warningStyle" dir="rtl">
                                                </span>
                                                &nbsp;&nbsp;<span id="autosCredit" class="warningStyle" dir="rtl"></span>
                                                <img alt="Credit Status" src="${path}/img/icons/iicon.png" id="creditStatusInfo" onmouseover="tooltip.show('<strong>Click to see all Credit Status</strong>', null, event);"
                                                     onmouseout="tooltip.hide();" onclick="creditStatusBillToBothImport();" height="16" width="16">
                                            </td>
                                            <td align="right">
                                                Bill To Code
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td align="left">
                                    <html:radio property="billToCode" value="F"  name="transactionBean" onclick="disableThirdParty()" styleId="billToCodeF"/>F
                                    <html:radio property="billToCode" value="S"  name="transactionBean" onclick="disableThirdParty()" styleId="billToCodeS"/>S
                                    <html:radio property="billToCode" value="T"  name="transactionBean" onclick="enableThirdParty()"  styleId="billToCodeT"/>T
                                    <html:radio property="billToCode" value="C"  name="transactionBean" onclick="disableThirdParty()" styleId="billToCodeA"/>C
                                    <html:radio property="billToCode" value="N"  name="transactionBean" onclick="disableThirdParty()" styleId="billToCodeN"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">
                                    Third Party Account Name </td>
                                <td>
                                    &nbsp;&nbsp;<input name="billThirdPartyName" style="color: #4040FF;font-weight: bold;" class="textlabelsBoldForTextBox"
                                                       style="text-transform: uppercase"  value="<%=billThirdPartyName%>" id="billThirdPartyName" size="27" maxlength="80"/>
                                    <input name="billThirdPartyCheck" id="billThirdPartyCheck" type="hidden"
                                           value="<%=billThirdPartyName%>"/>
                                    <div id="thirdParty_choices"  style="display:none;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initOPSAutocomplete("billThirdPartyName", "thirdParty_choices", "billTrePty", "billThirdPartyCheck",
                                                "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=0&acctTyp=T", "checkDisableThirdParty()");
                                    </script>
                                    <c:if test="${importFlag eq false}">
                                        &nbsp;<img src="${path}/img/icons/comparison.gif" alt="Add Contact" align="middle" id="contactNameButtonForT"
                                                   onclick="getContactInfo('T')"/>
                                    </c:if>
                                </td>
                                <td style="padding-left:10px;" align="right">
                                    Account#</td>
                                <td>
                                    &nbsp;<input class="textlabelsBoldForTextBox" name="billTrePty" style="color: #4040FF;font-weight: bold;"
                                                 style="text-transform: uppercase" value="<%=billThirdParty%>" id="billTrePty" readonly="readonly"  size="10" tabindex="-1"/>

                                </td>
                            </tr>
                            <tr class="textlabelsBold" >
                                <td class="setTopBorderForTable" align="right">Received SSL Master</td>
                                <td class="setTopBorderForTable" style="color:green;font-weight:bold;font-size:13" id="masterDiv">&nbsp;
                                    <c:choose>
                                        <c:when test="${null!=MasterScan && MasterScan!='0' && null != MasterStatus && not empty MasterStatus}">
                                            <c:out value="Yes (${MasterStatus})"></c:out>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${fclBl.master}"></c:out>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td align="right" class="setTopBorderForTable" >File Type</td>
                                <td align="left" style="padding-bottom:5px;" class="setTopBorderForTable">
                                    <input  value="IMPORT" class="BackgrndColorForTextBox" size="4" readonly="true" tabindex="-1" />
                                    <input type="hidden" name="fileType" value="I" />
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td colspan="4">
                                    <table border="0" width="100%"><tr class="textlabelsBold">
                                            <td align="right">Ready to send SS Master via EDI</td>
                                            <td>
                                                <input type="checkbox" id="ediCheckBox" name="ediCheckBox"
                                                       onclick="checkUncheckEdi('${importFlag}')"/>

                                            </td>
                                            <td align="right">Ready To Post/Manifest</td>
                                            <td>
                                                <html:checkbox property="readyToPost" name="transactionBean" styleId="readyToPost"
                                                               onclick="checkConfirmBoard('false','true')"/>&nbsp;
                                            </td>
                                        </tr></table></td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top" style="border: 1px solid #C4C5C4;">
                        <table  border="0"  width="100%" cellpadding="2" cellspacing="0" >
                            <tr class="tableHeadingNew"><td colspan="4">Carrier/Voyage/Vessel
                                    <span style="padding-left: 80px;">EDI:<b class="BackgrndColorForTextBox">
                                            <c:out value="<%=intraGtnexus%>"/>
                                        </b></span></td>
                            </tr>
                            <tr>
                                <td align="right" class="textlabelsBold" >
                                    <span onmouseover="tooltip.show('<strong>Steam ship Line Name</strong>', null, event);"
                                          onmouseout="tooltip.hide();">SSLName</span>
                                </td>
                                <td>
                                    <input class="textlabelsBoldForTextBox mandatory" name="streamShipName" id="streamShipName" size="30"
                                           value="<%=ssl%>"    />
                                    <div id="streamShipName_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initOPSAutocomplete("streamShipName", "streamShipName_choices", "sslinenumber", "sslname_check",
                                                "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=15", "setSSLNameForImportBL()");
                                    </script>
                                </td>
                                <td class="textlabelsBold"  align="right">SSL # </td>
                                <td>
                                    <input  class="BackgrndColorForTextBox" name="sslinenumber" size="11" readonly="readonly"
                                            value="${fclBl.sslineNo}" id="sslinenumber"  tabindex="-1"/>

                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" align="right" >Vessel Name</td>
                                <td>
                                    <input  class="textlabelsBoldForTextBox mandatory" style="text-transform: uppercase" name="manualVesselName" id="vesselname_checkn"  size="13" value="<%=manaualVesselName%>" />
                                    <input  class="textlabelsBoldForTextBox mandatory" name="vesselname" value="<%=vesselName%>" id="vesselname"  size="13"/>
                                    <c:set var="test" value="<%=vesselCheck%>"/>
                                    <c:choose>
                                        <c:when test="${test=='on'}">
                                            <input type="checkbox" name="vesselNameCheck" id="vesselname_check1" onclick="checkVesselName()" checked/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="vesselNameCheck" id="vesselname_check1" onclick="checkVesselName()"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <div id="vesselname_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("vesselname", "vesselname_choices", "vessel", "vesselNameCheck",
                                                "${path}/actions/getVesselName.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "setFocusFromDojo('voyage')");
                                    </script>

                                </td>
                                <td class="textlabelsBold"  align="right">Vessel #<font class="mandatoryStarColor"></font></td>
                                <td>
                                    <input class="BackgrndColorForTextBox" readonly="readonly" name="vessel" value="<%=vessel%>" id="vessel" size="13"  tabindex="-1"/>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td class="textlabelsBold" align="right" valign="baseline">SS Voyage</td>
                                <td>
                                    <input  class="textlabelsBoldForTextBox mandatory" name="voyage" value="${fclBl.voyages}" id="voyage" size="13" style="text-transform: uppercase" maxlength="50"/>
                                    <div id="voyage_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initOPSAutocomplete("voyage", "voyage_choices", "", "",
                                                "${path}/actions/getVoyageNo.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "");
                                    </script>
                                </td>
                                <td class="textlabelsBold" align="right" valign="baseline">Voy Internal</td>
                                <td>
                                    <html:text property="vaoyageInternational" styleClass="textlabelsBoldForTextBox" styleId="voyageInternal" maxlength="20"
                                               size="13" value="${fclBl.vaoyageInternational}" ></html:text>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold" valign="top">
                                    <td align="right" valign="baseline">ETD</td>
                                    <td>
                                    <fmt:formatDate pattern="MM/dd/yyyy" var="sailDate" value="${fclBl.sailDate}" />
                                    <html:text styleClass="textlabelsBoldForTextBox mandatory" property="sailDate"
                                               value="${sailDate}" styleId="txtetdCal" onchange="validateBlETD(this)"
                                               size="10" />
                                    <input type="hidden" id="etdDate" value="${sailDate}"/>
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="etdCal"
                                         onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                </td>
                                <td align="right" valign="baseline">ETA</td>
                                <td>
                                    <fmt:formatDate pattern="MM/dd/yyyy" var="eta" value="${fclBl.eta}" />
                                    <html:text styleClass="textlabelsBoldForTextBox mandatory"  property="eta"
                                               value="${eta}" styleId="txtetaCal" onchange="validateBlETA('true',this)"
                                               size="10" />
                                    <input type="hidden" id="etaDate" value="${eta}"/>
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="etaCal"
                                         onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                </td>
                            </tr>
                            <tr class="textlabelsBold" valign="top">
                                <td align="right" valign="baseline">ETA at FD</td>
                                <td>
                                    <fmt:formatDate pattern="MM/dd/yyyy" var="etaFd" value="${fclBl.etaFd}" />
                                    <html:text styleClass="textlabelsBoldForTextBox mandatory" property="etaFd"
                                               value="${etaFd}" styleId="txtetaFd" size="10" onchange="validateBlETAFD(this)"/>
                                    <input type="hidden" id="etaDate" value="${etaFd}"/>
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="etaFd"
                                         onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                </td>
                                <td align="right" valign="baseline" colspan="2"></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right" style="padding-bottom:5px;">SSL Booking # </td>
                                <td style="padding-bottom:5px;">
                                    <input  class="textlabelsBoldForTextBox" name="booking" size="13" value="${fclBl.bookingNo}"
                                            id="booking" style="text-transform: uppercase" maxlength="20">
                                    <div id="booking_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initOPSAutocomplete("booking", "booking_choices", "notInUse", "notInUse2",
                                                "${path}/actions/BookingNo.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "");
                                    </script>
                                    <input type="text" id="notInUse" style="display: none;" />
                                    <input type="text" id="notInUse2" style="display: none;"/>
                                </td>
                                <td align="right" style="padding-bottom:5px;">MASTER BL # </td>
                                <td style="padding-bottom:5px;">
                                    <input  class="textlabelsBoldForTextBox mandatory" maxlength="20" name="newMasterBL"
                                            size="22" value="${fclBl.newMasterBL}"  style="text-transform: uppercase"
                                            id="newMasterBL" />
                                    <div id="newMasterBL_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initOPSAutocomplete("newMasterBL", "newMasterBL_choices", "notInUse2", "notInUse4",
                                                "${path}/actions/BookingNo.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false", "");
                                    </script>
                                    <span style="padding-right:10px;" onmouseover="tooltip.show('<strong>Copy SSLBKG# </strong>', '100', event);"onmouseout="tooltip.hide();">
                                        <input  type="checkbox" id="newMasterBLCheckBox" onclick="setSSLBookingNo()"/>
                                    </span>
                                    <input type="text" id="notInUse2" style="display: none;" />
                                    <input type="text" id="notInUse4" style="display: none;"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <div  id="fclBLContainer" style="width:97%;padding-left:5px;padding-top:10px;">
                <ul class="newtab">
                    <li>
                        <a href="#trade" onclick='setTabName("tradeRoute");'>
                            <span class="newtabRight"> Trade Route
                                <font color="red">
                                *
                                </font>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#paries" onclick='setTabName("shipperForwarder");'>
                            <span class="newtabRight">
                                Shipper Forwarder Consignee Notify<font color="red"> * &nbsp; </font>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#general" onclick='setTabName("general");'>
                            <span class="newtabRight">
                                General
                                <font color="red">
                                <c:if test="${importFlag}">
                                    *
                                </c:if>
                                </font>
                            </span>
                        </a>
                    </li>
                    <li><a href="#containerDetails" onclick='setTabName("container");'><span class="newtabRight">ContainerDetails<font color="red">* &nbsp; </font></span></a></li>
                    <li><a href="#costCharges" onclick='setTabName("charges");'><span class="newtabRight">Cost&Charges</span></a></li>
                    <li><a href="#print" onclick='setTabName("print");'><span class="newtabRight">Print Options</span></a></li>
                </ul>
                <div id="trade">
                    <%@include file="../fclQuotes/FclTradeRoute.jsp"  %>
                </div>
                <div id="paries">
                    <%@include file="../fclQuotes/FclParties.jsp"  %>
                </div>
                <div id="general">
                    <%@include file="../fclQuotes/FclGeneral.jsp"  %>
                </div>
                <div id="containerDetails">
                    <%@include file="../fragment/fclcontainer.jspf"  %>
                </div>
                <div id="costCharges">
                    <jsp:include page="../fclQuotes/fclblcharges.jsp" flush="true"  >
                        <jsp:param name="streamShipLineNo" value="${fclBl.sslineNo}"/>
                        <jsp:param name="streamShipLineName" value="${fclBl.sslineName}"/>
                        <jsp:param name="commodityCode" value="${fclBl.commodityCode}"/>
                        <jsp:param name="finalDestination" value="${fclBl.finalDestination}"/>
                        <jsp:param name="terminalName" value="${fclBl.terminal}"/>
                        <jsp:param name="bol" value="${fclBl.bol}"/>
                        <jsp:param name="routedByAgent" value="${fclBl.routedByAgent}"/>
                        <jsp:param name="accountName" value="<%=shipperName%>"/>
                        <jsp:param name="shipper" value="<%=shipperNo%>"/>
                        <jsp:param name="forwardingAgentName" value="${fclBl.forwardingAgentName}"/>
                        <jsp:param name="forwardingAgent1" value="${fclBl.forwardAgentNo}"/>
                        <jsp:param name="agent" value="${fclBl.agent}"/>
                        <jsp:param name="agentNo" value="${fclBl.agentNo}"/>
                        <jsp:param name="thirdPartyName" value="<%=billThirdPartyName%>"/>
                        <jsp:param name="billTrdPrty" value="<%=billThirdParty%>"/>
                        <jsp:param name="consigneeName" value="${fclBl.consigneeName}"/>
                        <jsp:param name="consigneeNo" value="${fclBl.consigneeNo}"/>
                    </jsp:include>
                </div>
                <div id="print">
                    <%@include file="../fclQuotes/PrintOptions.jsp"  %>
                </div>
            </div><%--main div ends --%>
            <%--table to display buttons at the end of page --%>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="textlabels">

                        <table>
                            <tr class="textlabelsBold">
                                <td>File BOL No :
                                    <font color="red" size="4">
                                    <c:out value="${companyCode}-"/><c:out value="<%=billOfLadding%>"/>
                                    </font>
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>

                <tr>
                    <td>
                        <c:if test="${goBackToInquiry!=null}">
                            <input type="button" value="Go Back" class="buttonStyleNew" onclick="goToARInquiryPage()"/>
                        </c:if>
                        <c:if test="${goBackToInquiry==null}">
                            <input type="button" value="Go Back" id="goBack2" class="buttonStyleNew"
                                   onclick="compareWithOldArray('<%=billOfLadding%>', '${fclBl.bol}')"/>
                        </c:if>
                        <input type="button" value="Save" id="save" class="buttonStyleNew"  onclick="saved('<%=action%>', '${importFlag}')" onmouseover="tooltip.showTopText('<strong><%=mandatoryFieldForBl%></strong>', null, event);" onmouseout="tooltip.hide();"/>


                        <%if (!readyToPost.equals("M")) {
                                if (fileNo.indexOf("-") < 1 && !showReverseToBooking) {
                        %>
                        <c:if test="${newBl == 'edit'}">
                            <input type="button" value="ReverseToBooking" id="reverseToBookingForBottom"
                                   class="buttonStyleNew"  onclick="backToBooking()" style="width:120px;"/>
                        </c:if>
                        <c:if test="${newBl == 'fromQuote'}">
                            <input type="button" value="Reverse To Quote" id="reverseToQuote"
                                   class="buttonStyleNew"  onclick="reverseToQuotation('${fclBl.bol}')" style="width:120px;"/>
                        </c:if>
                        <%}%>
                        <input type="button" value="Manifest" id="manifestButtonDown" class="buttonStyleNew" onclick="manifest1('${importFlag}')"/>
                        <%} else {%>

                        <input type="button" value="UnManifest" class="buttonStyleNew" id="unManifestButtonDown"
                               onclick="unManifest('<%=billOfLadding%>');"/>
                        <c:choose>
                            <c:when test="${fclBl.closedDate==null}">
                                <c:if test="${roleDuty.closeBl}">
                                    <input type="button" class="buttonStyleNew" value="Close"
                                           id="blClosedButtonBottom" onclick="closedAudit('blClosed', '${importFlag}',${roleDuty.auditOverride});"/>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${roleDuty.reopenBl}">
                                    <input type="button" class="buttonStyleNew" value="Open"
                                           id="blOpnedButtonBottom" onclick="closedAudit('blOpned')"/>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fclBl.auditedDate==null}">
                                <c:if test="${roleDuty.audit}">
                                    <input type="button" class="buttonStyleNew" value="Audit"
                                           id="blAuditedButtonBottom" style="width:50px" onclick="closedAudit('blAudit')"/>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${roleDuty.cancelAudit}">
                                    <input type="button" class="buttonStyleNew" value="Cancel Audit"
                                           id="blAuditedCancelButtonBottom" style="width:90px" onclick="closedAudit('blAuditCancel')"/>
                                </c:if>
                            </c:otherwise>
                        </c:choose>




                        <%if (latestBolid != null) {%>

                        <input type="button" value="FCLBLCorrection" class="buttonStyleNewRed" id="correctionButtonDown"
                               onclick="FCLBLCorrection('<%=billOfLadding%>', '${fclBl.fileNo}')" style="width:100px"/>
                        <%} else {
                            if (billOfLadding != null && billOfLadding.indexOf(FclBlConstants.DELIMITER) < 0) {%>
                        <input type="button" value="FCLBLCorrection" class="buttonStyleNew" id="correctionButtonDown"
                               onclick="FCLBLCorrection('<%=billOfLadding%>', '${fclBl.fileNo}')" style="width:100px"/>
                        <%}
                                }
                            }%>
                        <c:if test="${not empty fclBl.fileNo && newBl != 'edit' && fclBl.readyToPost != 'M'}">
                            <c:choose>
                                <c:when test="${fclBl.blVoid == 'Y'}">
                                    <input type="button" class="buttonStyleNew" value="UnVoid"
                                           id="voidButton"  onclick="unVoidBl()"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="button" class="buttonStyleNew" value="Void"
                                           id="voidButton"  onclick="voidBl('${fclBl.bolId}')"/>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <input type="button" class="${manualNotesCount}" id="noteButtonDown"  name="search" value="Note"
                               onclick="return GB_show('Notes', '${path}/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '${fclBl.fileNo}', 630, 900);" />
                        <input type="button" id="importReleaseDown" value="Import Release" class="buttonStyleNew"
                               onclick="openImportReleasePopUp()" style="width:100px"/>
                        <input id="inbondButtonDown" type="button" value="Inbond" class="buttonStyleNew"
                               onclick="gotoInbond('<%=bol%>', '${fclBl.fileNo}')" style="width:60px;" />
                        <input type="button" id="printFax" value="Print/Fax/Email" class="buttonStyleNew"
                               onclick="PrintReports('${importFlag}')" style="width:100px"/>
                        <c:choose>
                            <c:when test="${null!=TotalScan && TotalScan!='0'}">
                                <input id="scanButtonDown" class="buttonColor" type="button"
                                       value="Scan/Attach" onClick="scan('${fclBl.fileNo}', '${importFlag}')"/>
                            </c:when>
                            <c:otherwise>
                                <input id="scanButtonDown" class="buttonStyleNew" type="button"
                                       value="Scan/Attach" onClick="scan('${fclBl.fileNo}', '${importFlag}')"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="button" id="arRedInvoiceDown" value="AR Invoice" class="buttonStyleNew"
                               onclick="arInvoice('${fclBl.fileNo}', '${importFlag}')"/>
                        <input type="button" id="sendEdi1" value="Send EDI" class="buttonStyleNew"
                               onclick="generateXml('${fclBl.fileNo}', '<%=intraGtnexus%>', '<%=bol%>', '<%=userName%>', 'create')"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="terminalId"/>
            <html:hidden property="eventCode" styleId="eventCode"/>
            <html:hidden property="eventDesc" styleId="eventDesc"/>
            <html:hidden property="moduleId" value="<%=NotesConstants.FILE%>"/>
            <html:hidden property="moduleRefId" value="${fclBl.fileNo}"/>
            <html:hidden property="fileNo" value="${fclBl.fileNo}"/>
            <html:hidden property="importFlag" styleId="importFlag" value="${importFlag}"/>
            <html:hidden property="portId"/>
            <html:hidden property="portIdLoading"/>
            <html:hidden property="portIdDischarge"/>
            <html:hidden property="carrierId"/>
            <html:hidden property="quotesId"/>
            <html:hidden property="bookingId"/>
            <html:hidden property="voyageId"/>
            <html:hidden property="vesselId"/>
            <html:hidden property="bol" styleId="bol" value="${fclBl.bol}"/>
            <html:hidden property="toprintCharges"/>
            <html:hidden property="copyBol"/>
            <html:hidden property="quoteBy" value="${fclBl.quoteBy}"/>
            <html:hidden property="blBy" value="${fclBl.blBy}"/>
            <html:hidden property="OPrinting"/>
            <html:hidden property="NPrinting"/>
            <html:hidden property="BLPrinting"/>
            <html:hidden property="action"/>
            <html:hidden property="noOfOriginals"/>
            <html:hidden property="printContainersOnBL"/>
            <html:hidden property="shipperLoadsAndCounts"/>
            <html:hidden property="printPhrase"/>
            <html:hidden property="agentsForCarrier"/>
            <html:hidden property="alternatePOL"/>
            <html:hidden property="manifestPrintReport"/>
            <html:hidden property="houseFreightedBL"/>
            <html:hidden property="houseFreightedNNBL"/>
            <html:hidden property="houseFreightedOriginal"/>
            <html:hidden property="houseUnFreightedBL"/>
            <html:hidden property="houseUnFreightedNNBL"/>
            <html:hidden property="houseUnFreightedOriginal"/>
            <html:hidden property="steamShipMaster"/>
            <html:hidden property="manifest"/>
            <html:hidden property="houseFreightInvoice"/>
            <html:hidden property="confirmOnBoardNotice"/>
            <html:hidden property="fclArrivalNotice"/>
            <html:hidden property="importArrivalNotice"/>
            <html:hidden property="printCorrected"/>
            <html:hidden property="freightInvoice"/>
            <html:hidden property="freightNonNego"/>
            <html:hidden property="unFreightNonNego"/>
            <html:hidden property="trnref"/>
            <html:hidden property="transactionRefId"/>
            <html:hidden property="transferCost"/>
            <html:hidden property="billofladding" value="<%=billOfLadding%>"/>
            <html:hidden property="portremarks" value="${fclBl.destRemarks}"/>
            <html:hidden property="ratesRemarks" value="${fclBl.ratesRemarks}"/>
            <html:hidden property="bookingBy" value="${fclBl.bookingBy}"/>
            <html:hidden property="blColsed" value="${fclBl.blClosed}"/>
            <html:hidden property="auditCheckBox" value="${fclBl.blAudit}"/>
            <html:hidden property="costOfGoods" styleId="costOfGoods" value="${fclBl.costOfGoods}"/>
            <html:hidden property="insuranceRate" value="${fclBl.insuranceRate}"/>
            <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="41a390f7a1077ae74371625475760a7a"/>
            <html:hidden property="fclSsblGoCollect" styleId="fclSsblGoCollect" value="${fclSsblGoCollect}"/>
            <input type="hidden" name="hazmatidentity" value="${hazmatidentity}">
            <input type="hidden" name="selectedTab" id="selectedTab"/>
            <input type="hidden" name="masterScanStatus" id="masterScanStatus" value="${MasterStatus}"/>
            <input type="hidden" name="houseBlScanStatus" id="houseBlScanStatus" value="${HouseBlStatus}"/>
            <input type="hidden" name="orginBlStatus" id="orginBlStatus" value="${OrginBlStatus}"/>
            <input type="hidden" name="ratesNonRates" id="ratesNonRates" value="${fclBl.ratesNonRates}"/>
            <input type="hidden" id="companyCode" value="${companyCodeValue}"/>

            <c:if test="${not empty fclBl.quoteDate}">
                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a"  value= "${fclBl.quoteDate}" var="date1"/>
                <html:hidden property="quoteDate" value="${date1}"/>
            </c:if>

            <c:if test="${not empty fclBl.bookingDate}">
                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a"  value= "${fclBl.bookingDate}" var="date2"/>
                <html:hidden property="bookingDate" value="${date2}"/>
            </c:if>
             <html:hidden property="freightInvoiceContacts" styleId="freightInvoiceContacts" value=""/>

        </html:form>
        <div id="creditStatus" class="static-popup" style="display: none;">
            <table class="table" border="0" style="margin: 1px;width: 500px;">
                <tr>
                    <th colspan="3" >
                <div class="float-left" >Credit Status</div>
                <div class="float-right">
                    <a href="javascript: hideCreditStatus()" style="">
                        <img alt="Close" src="${path}/images/icons/close.png"/>
                    </a>
                </div>
                </th>
                </tr>
                <tr id="shipperId">
                    <td class="label">SHIPPER</td>
                    <td class="label">: <span id="shipperWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="shipperStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="forwarderId">
                    <td class="label">FORWARDER</td>
                    <td class="label">: <span id="forwarderWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="forwarderStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="thirdPartyId">
                    <td class="label">THIRD PARTY</td>
                    <td class="label">: <span id="thirdpartyWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="thirdpartyStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="consigneeId">
                    <td class="label">CONSIGNEE</td>
                    <td class="label">: <span id="consigneeWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="consigneeStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="notifyPartyId">
                    <td class="label">NOTIFY PARTY</td>
                    <td class="label">: <span id="notifyPartyWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="notifyPartyStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
            </table>
        </div>
        <script>setFocusToSSL();</script>
    </body>

    <%--// these functions are called while page is loaded--%>
    <script type="text/javascript">
        function openTradingPartner(ev) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "clearTradingPartnerSession",
                    request: "true"
                },
                success: function () {
                    window.parent.GB_showFullScreen("Trading Partner", "${path}/jsps/Tradingpartnermaintainance/SearchCustomer.jsp?callFrom=fclBillLadding&programid=156&field=" + ev)
                }
            });
        }
        function checkDisabledRouted() {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("RoutedDup").value
                },
                success: function (dataDup) {
                    if (dataDup == true) {
                        alertNew(disableMessage);
                        document.getElementById("routedByAgent").value = "";
                    } else {
                        document.getElementById("routedByAgent").value = document.getElementById("RoutedDup").value;
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                methodName: "getUnlocDetail",
                                param1: document.getElementById("routedByAgent").value
                            },
                            success: function (data) {
                                if (data == "false") {
                                    if (document.fclBillLaddingform.routedAgentCheck.value == "yes" && '${fclBl.readyToPost}' == 'M') {
                                        document.getElementById("routedAgentCheck").selectedIndex = 2;
                                    } else if (document.fclBillLaddingform.routedAgentCheck.value == "yes") {
                                        document.getElementById("routedAgentCheck").selectedIndex = 0;
                                    }
                                    document.fclBillLaddingform.routedByAgent.value = "";
                                    alertNew("Please Enter UnLocation Code for selected Routed Agent");
                                }
                            }
                        });
                    }
                }
            });
        }
    </script>
    <c:if test="${not empty showCostTab}">
        <script>
            jQuery('#fclBLContainer').tabs(5);
            setTimeout("slide()", 500);
            function slide() {
                document.getElementById("costadd").scrollIntoView(true);
            }
        </script>
    </c:if>
    <c:if test="${fileName!=null}">
        <script type="text/javascript">
            GB_show('Bill Of Lading Report', '${path}/servlet/PdfServlet?fileName=${fileName}', 630, 900);
        </script>
    </c:if>
        <c:if test="${!empty manifest}">
            <script language="javascript">
                    displayAutoNotificationContacts('${manifestWithoutCharges}');
            </script>
        </c:if>
        
    <c:choose>
        <c:when test="${! empty printRequest}">
            <script language="javascript">
                var fileNo = "${fclBl.fileNo}";
                var path = "${path}/printConfig.do?screenName=BL&fileNo=" + fileNo + "&blId=${fclBl.bol}&bolNo=${fclBl.bolId}&destination=${fclBl.finalDestination}filesToPrint=all&subject=FCL BL";
                GB_show('Print/Fax/Email FCL BL  FileNumber' + " " + fileNo, path, 400, 1000);
            </script>
        </c:when>
        <c:when test="${! empty manifest}">
            <script language="javascript">
                displayContacts('${manifestWithoutCharges}');
            </script>
        </c:when>
        <c:when test="${! empty confirmBoardPrintRequest}">
            <script language="javascript">
                var fileNo = "${fclBl.fileNo}";
                var path = "${path}/printConfig.do?screenName=BL&fileNo=" + fileNo + "&blId=${fclBl.bol}&bolNo=${fclBl.bolId}&destination=${fclBl.finalDestination}&filesToPrint=confirmOnBoard&subject=FCL BL";
                GB_show('Print/Fax/Email FCL BL ConfirmOnBoard  FileNumber' + " " + fileNo, path, 400, 1000);
            </script>
        </c:when>
        <c:when test="${! empty sendEdi}">
            <script language="javascript">
                readyToSendEdi('${fclBl.fileNo}', '<%=intraGtnexus%>', '<%=bol%>', '<%=userName%>', 'validate');
            </script>
        </c:when>
        <c:otherwise>
            <c:if test="${! empty printBookingReport}">
                <script language="javascript">
                    var fileNo = "${fclBl.fileNo}";
                    var path = "${path}/printConfig.do?screenName=BL&fileNo=" + fileNo + "&blId=${fclBl.bol}&bolNo=${fclBl.bolId}&destination=${fclBl.finalDestination}&filesToPrint=booking";
                    GB_show('Print/Fax/Email FCL BL  FileNumber' + " " + fileNo, path, 400, 1000);
                </script>
            </c:if>
        </c:otherwise>
    </c:choose>
    <script>
        changeSelectBoxOnViewMode();
    </script>
</html>

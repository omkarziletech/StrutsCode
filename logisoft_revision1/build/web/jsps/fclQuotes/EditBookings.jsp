<%@page import="com.gp.cvst.logisoft.hibernate.dao.QuotationDAO"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO"%>
<%@ page language="java"  import=" com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.beans.*,com.gp.cvst.logisoft.beans.*,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.*"%>
<%@ page import="java.util.*,java.text.NumberFormat,java.text.SimpleDateFormat,java.text.*,com.gp.cong.logisoft.util.CommonFunctions,com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO,com.gp.cvst.logisoft.struts.form.EditBookingsForm,com.gp.cong.logisoft.hibernate.dao.PortsDAO"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.*"/>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@ include file="../includes/jspVariables.jsp" %>
<bean:define id="fileNumberPrefix" type="String">
    <bean:message key="fileNumberPrefix"/>
</bean:define>
<bean:define id="oceanfreightcharge" type="String">
    <bean:message key="oceanfreightcharge"/>
</bean:define>
<html>
    <head>
        <%  request.setAttribute("HAZARDOUS", BookingConstants.HAZARDOUS_CODE_DESC);
                    String path = request.getContextPath();
                    DBUtil dbUtil = new DBUtil();
                    PortsDAO portsDAO = new PortsDAO();
                    String quoteId = "";
                    String bookingId = "";
                    String shipCheck = "";
                    String forwCheck = "";
                    String consCheck = "";
                    String msg = "";
                    String truckerCheckbox = "N";
                    boolean importFlag = false;
                    GenericCodeDAO gdDAO = new GenericCodeDAO();
                    request.setAttribute("insuranceAllowed", portsDAO.getInsuranceAllowed());
                    request.setAttribute("RATESLIST", dbUtil.getUnitListForFCLTest(new Integer(38), "yes", "Select Unit code"));
                    request.setAttribute("NTRLIST", gdDAO.getAllUnitCodeForFCLTestforList(new Integer(38)));
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    BookingFcl bookinFCL = new BookingFcl();
                    String blFlag = "";
                    String fileNo = "", fileNumb = "";
                    String mandatoryFieldForBooking = "";
                    String bookingDate1 = "", bookingComplete = "", destination = "", regionRemarks = "", breakBulk = "";
                    String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                    request.setAttribute("companyCode", companyCode);
                    TransactionBean transactionBean = new TransactionBean();
                    if (request.getAttribute("bookingValues") != null) {
                        bookinFCL = (BookingFcl) request.getAttribute("bookingValues");
                        if (bookinFCL.getHazmat() != null && bookinFCL.getHazmat().equals("Y")) {
                            msg = "HAZARDOUS CARGO";
                            request.setAttribute("msg", msg);
                            transactionBean.setHazmat("Y");
                        }
                        if (bookinFCL.getQuoteNo() != null) {
                            quoteId = bookinFCL.getQuoteNo();
                            request.setAttribute("quoteId", quoteId);
                        }
                        if (bookinFCL.getBookingId() != null) {
                            bookingId = bookinFCL.getBookingId().toString();
                            request.setAttribute("bookingId", bookingId);
                            request.setAttribute("fclBlCostCodesList", new BookingFclDAO().getBookingAccruals(bookinFCL.getBookingId()));
                        }
                        if (bookinFCL.getBlFlag() != null) {
                            blFlag = bookinFCL.getBlFlag();
                        } else {
                            blFlag = "off";
                        }
                        request.setAttribute("blFlag", blFlag);
                        if (bookinFCL.getFileNo() != null) {
                            fileNo = fileNumberPrefix + bookinFCL.getFileNo();
                            fileNumb = bookinFCL.getFileNo().toString();
                            request.setAttribute("fileNo", fileNo);
                            request.setAttribute("bulletRates", new QuotationDAO().isBulletRateByFileNo(bookinFCL.getFileNo()));
                        } else {
                            request.setAttribute("bulletRates", false);
                        }
                        if (bookinFCL.getBookingDate() != null) {
                            bookingDate1 = sdf.format(bookinFCL.getBookingDate());
                        }
                        if (bookinFCL.getBookingComplete() != null) {
                            bookingComplete = bookinFCL.getBookingComplete();
                        }
                        if (bookinFCL.getBreakBulk() != null) {
                            breakBulk = bookinFCL.getBreakBulk();
                        }
                        if (bookinFCL.getTruckerCheckbox() != null) {

                            transactionBean.setTruckerCheckbox(bookinFCL.getTruckerCheckbox());
                        } else {

                            transactionBean.setTruckerCheckbox("off");
                        }
                        transactionBean.setSpotRate(bookinFCL.getSpotRate());
                        if (null != bookinFCL.getBrand()) {
                                transactionBean.setBrand(bookinFCL.getBrand());
                            }
                        transactionBean.setBrand(bookinFCL.getBrand());
                        if (null != bookinFCL.getPortofDischarge()) {
                            destination = bookinFCL.getPortofDischarge();
                            regionRemarks = new QuotationBC().fetchRegionRemarks(destination, null);
                        }
                        if (null != regionRemarks) {
                            request.setAttribute("regionRemarks", regionRemarks.replaceAll("(\\r\\n|\\n)", "<br/>"));
                        }
                        if (bookinFCL.getAlternateAgent() != null) {
                            transactionBean.setAlternateAgent(bookinFCL.getAlternateAgent());
                        } else {
                            transactionBean.setAlternateAgent("N");
                        }
                        importFlag = (null != bookinFCL.getImportFlag() && bookinFCL.getImportFlag().equalsIgnoreCase("I") ? true : false);
                    } else {
                        importFlag = (null != session.getAttribute(ImportBc.sessionName)) ? true : false;
                        transactionBean.setAlternateAgent("N");
                        transactionBean.setCarrierPrint("on");
                    }
                    request.setAttribute("importFlag", importFlag);
                    request.setAttribute("checkDefaultAgent", bookinFCL.getAlternateAgent());
                    request.setAttribute("checkDirectConsign", bookinFCL.getDirectConsignmntCheck());
                    if (request.getAttribute("transactionBean") != null) {
                        transactionBean = (TransactionBean) request.getAttribute("transactionBean");
                        shipCheck = transactionBean.getShippercheck();
                        forwCheck = transactionBean.getForwardercheck();
                        consCheck = transactionBean.getConsigneecheck();

                        if (bookinFCL.getTruckerCheckbox() != null) {

                            transactionBean.setTruckerCheckbox(bookinFCL.getTruckerCheckbox());
                        } else {

                            transactionBean.setTruckerCheckbox("off");
                        }
                        if (null == transactionBean.getBreakBulk()) {
                            transactionBean.setBreakBulk(breakBulk);
                        }
                        if (bookinFCL.getFileType() != null) {
                            transactionBean.setFileType(bookinFCL.getFileType());
                        }
                    }
                    if (bookinFCL.getCarrierPrint() != null && bookinFCL.getCarrierPrint().equals("on")) {
                        transactionBean.setCarrierPrint(bookinFCL.getCarrierPrint());
                    }
                    if (bookinFCL.getDirectConsignmntCheck() != null && bookinFCL.getDirectConsignmntCheck().equalsIgnoreCase("on")) {
                        transactionBean.setDirectConsignmntCheck(bookinFCL.getDirectConsignmntCheck());
                    }
                    request.setAttribute("transactionBean", transactionBean);

                    if (request.getAttribute("loackRecord") != null) {
                        msg = (String) request.getAttribute("loackRecord");
                    }

                    if (request.getAttribute("message") != null) {
                        msg = (String) request.getAttribute("message");
                    }
                    String view = "";
                    if (session.getAttribute("view") != null) {
                        view = (String) session.getAttribute("view");
                    }
                    String loackMessage = "";
                    if (request.getAttribute(QuotationConstants.LOCK) != null) {
                        loackMessage = (String) request.getAttribute(QuotationConstants.LOCK);
                        view = "3";
                    }
                    String bookingBy = "";
                    User user1 = null;
                    if (session.getAttribute("loginuser") != null) {
                        user1 = (User) session.getAttribute("loginuser");
                        bookingBy = user1.getLoginName();
                    }
                    String bookingDate = dateFormat.format(new Date());
                    request.setAttribute("bookingDate", bookingDate);
                    request.setAttribute("optionList", dbUtil.getVendortypeIncudingThirdParty());
                    if (bookinFCL.getRampCheck() != null && bookinFCL.getRampCheck().equalsIgnoreCase("on")) {
                        request.setAttribute("typeOfMoveList", new QuoteDwrBC().getrampNvoMoveList());
                    } else {
                        request.setAttribute("typeOfMoveList", new QuoteDwrBC().getNvoMoveList());
                    }
                    request.setAttribute("lineMoveList", dbUtil.getGenericFCLforTypeOfMovebooking(new Integer(48), "yes", "yes"));
                    request.setAttribute("specialEquipmentList", dbUtil.getUnitListForFCLTest1(new Integer(41), "yes", "Select Special Equipments"));
                    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
                    if (bookinFCL.getFileNo() != null) {
                        if (importFlag) {
                            request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(bookinFCL.getFileNo().toString(), "IMPORT FILE", "", "Scan or Attach"));
                        } else {
                            request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(bookinFCL.getFileNo().toString(), "FCLFILE", "", "Scan or Attach"));
                        }
                        request.setAttribute("newBooking", dbUtil.checkNewBooking(bookinFCL.getFileNo().toString()));
                        request.setAttribute("ManualNotes", new NotesDAO().isManualNotes(bookinFCL.getFileNo().toString()));
                        if (bookinFCL.getShipNo() != null && !"".equalsIgnoreCase(bookinFCL.getShipNo())) {
                            request.setAttribute("isShipperBlueNotes", new NotesDAO().isCustomerNotes(bookinFCL.getShipNo().toString()));
                        }
                        if (bookinFCL.getConsNo() != null && !"".equalsIgnoreCase(bookinFCL.getConsNo())) {
                            request.setAttribute("isConsigneeBlueNotes", new NotesDAO().isCustomerNotes(bookinFCL.getConsNo().toString()));
                        }
                        if (bookinFCL.getForwNo() != null && !"".equalsIgnoreCase(bookinFCL.getForwNo())) {
                            request.setAttribute("isForwarderBlueNotes", new NotesDAO().isCustomerNotes(bookinFCL.getForwNo().toString()));
                        }
                        request.setAttribute("isSpotRated", new BookingFclDAO().isNotSpotRate(bookinFCL.getBookingId().toString()));
                    }
                    if (importFlag) {
                        mandatoryFieldForBooking = "Mandatory Fields Needed<br>1)SS Bkg #<BR>2)Vessel<br>3)SS Voy<BR>4)"
                                + "Container Cut Off  <br>5)Doc Cut Off<BR>6)ETD<br>7)Line Move<br>8)ERT<br>"
                                + "9)Docs Received<br>10)Forwarder Name<br>11)Date out of Yard<br>12)Select Units";
                    } else {
                        mandatoryFieldForBooking = "Mandatory Fields Needed<br>1)SS Bkg #<BR>2)Vessel<br>3)SS Voy<BR>4)"
                                + "Container Cut Off  <br>5)Doc Cut Off<BR>6)ETD<br>7)ETA<br>8)Line Move<br>9)ERT<br>"
                                + "10)Docs Received<br>11)Forwarder Name<br>12)Date out of Yard<br>13)Select Units";
                    }
                    request.setAttribute("mandatoryFieldForBooking", mandatoryFieldForBooking);
        %>

        <%@include file="../includes/resources.jsp"%>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="<%=path%>/js/editBooking.js"></script>
        <link rel="stylesheet" href="<%=path%>/css/jquery-tabs/jquery-ui-1.8.10.custom.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=path%>/css/popup.css" type="text/css"  />
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
        <script src="<%=path%>/js/jquery/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
        <link type="text/css" rel="stylesheet" href="<%=path%>/js/lightbox/lightbox.css"/>
        <script type="text/javascript" src="<%=path%>/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript">
            start = function() {
                loadFunction('${bookingValues.fileNo}');
                getBillTO('<%=view%>', '<%=blFlag%>');
                spotMsgForBook('${bookingValues.spotRate}',true);
                showCancelEdiOnload('${cancelEdi}', '${createOrChange}');
                directmentOnload('${checkDefaultAgent}', '${checkDirectConsign}');

                serializeForm();
                inlandLabel('${importFlag}');
                greyOutTruckerCheckBox(document.getElementById('editbook'));
                setFocusToTab('${selectedTab}');
                makeARInvoiceButtonGreen('${bookingValues.fileNo}','${bookingValues.voyageInternal}');
                hidebkgvesselvoy('${bookingValues.fileNo}', '${bookingValues.blFlag}');
                carrierStyle();
                defaultPOA('${bookingValues.shipperPoa}', '${bookingValues.forwarderPoa}', '${bookingValues.consigneePoa}');
                setPOAYESNO();
                ratesFocusForBooking();
                checkVesselName();
                modifyAccountDetails();
                setNvoOnLoad();
                setAlternateAgent('${importFlag}', '${companyCode}');
                enableOrDisablePierPassBooking();
               // setbrandvalueBasedONDestination();
               showBrandValuesFromQuote('${bookingValues.brand}','${importFlag}', '${companyCode}');
               // addBrandvalueForanAccount();
               enableOrDisableChassis();
            }
            window.onload = start;
        </script>
        <script language="javascript" type="text/javascript">
            var HAZARDOUS = "${HAZARDOUS}";
            var importFlag = "${importFlag}"
            var insuranceAllowed="${insuranceAllowed}";
            var SSBookingNo = "${bookingValues.SSBookingNo}";
            var portofDischarge = "${bookingValues.portofDischarge}";
            var isSpotRated = "${isSpotRated}";
            var bookingNO;
            bookingNO = (trim('${bookingValues.bookingNumber}') != '') ? '${bookingValues.bookingNumber}' : '';

            function getInland(ev) {
                if (document.EditBookingsForm.zip == undefined || document.EditBookingsForm.zip.value == '') {
                    alertNew("zip is not present");
                    document.EditBookingsForm.inland[1].checked = true;
                    ;
                    return;
                } else {
                    var ratedOption = '';
                    if (undefined != ev && 'N' == ev) {
                        ratedOption = 'NonRated';
                    } else {
                        ratedOption = 'rated';
                    }
                    if (document.getElementById("rampCheck").checked) {
                        GB_show('Booking Charges', rootPath +'/bookingCharges.do?hazmat=' + document.EditBookingsForm.hazmat.value +
                            '&spcleqpmt=' + document.EditBookingsForm.specialequipment.value + '&provisions=' + 'Intermodal Ramp' +
                            '&bkgNo=' + bookingNO + '&button=' + 'editBooking' + '&fileNo=' + '<%=fileNo%>' + '&ratedOption=' + ratedOption + '&importFlag=${importFlag}', 500, 900);
                    } else {
                        GB_show('Booking Charges', rootPath +'/bookingCharges.do?hazmat=' + document.EditBookingsForm.hazmat.value +
                            '&spcleqpmt=' + document.EditBookingsForm.specialequipment.value + '&provisions=' + 'inland' +
                            '&bkgNo=' + bookingNO + '&button=' + 'editBooking' + '&fileNo=' + '<%=fileNo%>' + '&ratedOption=' + ratedOption + '&importFlag=${importFlag}', 500, 900);
                    }
                }
            }
            function load(e1, e2, e3, e4) {
                if (document.EditBookingsForm.bookingComplete[0].checked) {
                    if (document.getElementById("ReversetoQuote")) {
                        document.getElementById("ReversetoQuote").style.visibility = 'hidden';
                        document.getElementById("ReversetoQuote1").style.visibility = 'hidden';
                    }
                    //--make the page readonly when booking complete------
                    makePageReadOnlyOnBookingComplete(document.getElementById("editbook"), '<%=view%>', '<%=blFlag%>');
                }
                if (document.EditBookingsForm.billToCode[2] && !document.EditBookingsForm.billToCode[2].checked) {
                    document.getElementById('accountName').disabled = true;
                    document.getElementById('accountNumber1').disabled = true;
                    document.getElementById('toggle').style.visibility = "hidden";
                }
                enableAgentLookUp();
                //disableSpecialEqpmt();

                var collapseObj = document.getElementById("collapseRatesTable");
                if (collapseObj != null) {
                    for (i = 0; i < collapseObj.rows.length; i++) {
                        var rowObj = collapseObj.rows[i];
                        if (rowObj.cells && rowObj.cells.length > 4) {
                            var chargecode = rowObj.cells[4].innerHTML;
                            var redStarIndex = chargecode.indexOf("DOCUMENT CHARGE");
                            if (redStarIndex != -1) {
                                document.EditBookingsForm.docCharge[0].checked = true;
                                break;
                            }
                        }

                    }
                }

                document.getElementById("collapseRates").style.display = "block";
                document.getElementById("expandRates").style.display = "none";
                if (e4 == 'BKGConfReportWithoutSave' || e4 == 'BKGConfReport') {
                    var email = "";
                    var quoteBy = '${bookingValues.quoteBy}';
                    if (null != quoteBy && quoteBy != '') {
                        var frameRef = window.parent.$("#tab" + 2);
                        var tab = window.parent.$("#tab" + 2).attr("title");
                        if (undefined != tab && tab == 'Quotes') {
                            if (frameRef.document.getElementById("email")) {
                                email = frameRef.document.getElementById("email").value;
                            }
                        }
                    }
                    var fileNo = '${bookingValues.fileNo}';
                    var path = "<%=path%>/printConfig.do?screenName=Booking&bookingId=${bookingValues.bookingId}&ssBookingNo=${bookingValues.SSBookingNo}&moduleName=${importFlag}&destination=${bookingValues.portofDischarge}&subject=FCL Booking-" + fileNo + "&toAddress=" + email
                        + "&emailMessage=Please find the attached File&fileNo=" + fileNo + "&destination=" + document.getElementById("portOfDischarge").value + "&issuingTerminal=" + document.EditBookingsForm.issuingTerminal.value;
                    GB_show('Print/Fax/Email FCL Booking FileNumber ' + fileNo, path, 400, 1000);
                }
            }

            function goToBookingCharges(ev) {
                if (document.EditBookingsForm.bookingId.value == '') {
                    alertNew('Please Save Booking Before create Manually charges ');
                    return false;
                }
                var breakBulk = "";
                if (document.EditBookingsForm.breakBulk != null) {
                    if (document.EditBookingsForm.breakBulk[0].checked) {
                        breakBulk = 'Y';
                    } else {
                        breakBulk = 'N';
                    }
                }
                var ratedOption = '';
                if (undefined != ev && 'N' == ev) {
                    ratedOption = 'NonRated';
                } else {
                    ratedOption = 'rated';
                }
                document.getElementById('fileNo').scrollIntoView(true);
                var hzmat = document.EditBookingsForm.hazmat.value = undefined ? null : document.EditBookingsForm.hazmat.value;
                var spcleqpmt = document.EditBookingsForm.specialequipment.value = undefined ? null : document.EditBookingsForm.specialequipment.value;
                GB_show('Booking Charges', rootPath +'/bookingCharges.do?hazmat=' + hzmat + '&spcleqpmt=' + spcleqpmt + '&bkgNo=' + bookingNO + '&breakBulk=' + breakBulk + '&fileNo=' + '<%=fileNo%>' + '&ratedOption=' + ratedOption + '&importFlag=${importFlag}', 400, 1055);
            }
            function editArCharges(val1, val2) {
                GB_show('Edit Booking Charges', rootPath +'/bookingCharges.do?buttonValue=edit&id=' + val1 + '&fileNo=' + val2 + '&msg=<%=msg%>' + '&importFlag=${importFlag}', 500, 1055);
            }
            function gotoInbond(bol, fileNo) {
                if (document.EditBookingsForm.bookingId.value == '') {
                    alertNew('Please Save Booking, Before Add Inbond Details ');
                    return false;
                }
                GB_show('Inbond Details', '<%=path%>/bookingInbondDetails.do?buttonValue=' + 'booking&bookingId=' + bol + '&fileNo=' + fileNo, 320, 900);
            }
            //---part of Confirm function of New Alertbox -----
            function confirmMessageFunction(id1, id2) {
                if (id1 == 'convertToBl' && id2 == 'ok' || id1 == 'copyBooking' && id2 == 'ok') {
                    document.getElementById('eventCode').value = '100004';
                    var d = new Date();
                    var haz;
                    if (document.EditBookingsForm.hazmat[0].checked) {
                        haz = 'Y';
                    } else {
                        haz = 'N';
                    }
                    var car = document.EditBookingsForm.sslDescription.value;
                    var career = escape(car);
                    var origin = document.EditBookingsForm.originTerminal.value;
                    var pol = document.EditBookingsForm.portOfOrigin.value;
                    var pod = document.EditBookingsForm.destination.value;
                    var dest = document.EditBookingsForm.portOfDischarge.value;
                    var comid = document.EditBookingsForm.commcode.value;
                    var ratesNonRates = document.EditBookingsForm.ratesNonRates.value;
                    var carrier = career;
                    var quoteDate = '<%=bookingDate1%>';
                    var mm = (d.getMonth()  < 9 ? "0" : "") + (d.getMonth() + 1);
                    var dd = (d.getDate() < 10 ? "0" : "") + d.getDate();
                    var yyyy = d.getFullYear();
                    var bookingDate = mm + "/" + dd + "/" + yyyy;
                    var fileNo = '${bookingValues.fileNo}';
                    var hazmat = haz;
                    document.getElementById('eventCode').value = '100004';
                    if(null != ratesNonRates && ratesNonRates !== 'N' || jQuery("#spotRateY").is(":checked")){
                        jQuery.ajaxx({
                            dataType : "json",
                            data: {
                                className : "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName : "rateChangeAlert",
                                param1 : origin,
                                param2 : pol,
                                param3 : pod,
                                param4 : dest,
                                param5 : comid,
                                param6 : carrier,
                                param7 : quoteDate,
                                param8 : bookingDate,
                                param9 : fileNo,
                                param10 : hazmat,
                                request : "true",
                                dataType : "json"
                            },
                            preloading : true,
                            success: function(data) {
                                if(id1 === 'copyBooking'){
                                    if (data) {
                                        document.EditBookingsForm.buttonValue.value = id1;
                                        document.EditBookingsForm.submit();
                                    }else{
                                        document.EditBookingsForm.buttonValue.value = "copyBookingWithNewRates";
                                        document.EditBookingsForm.submit();
                                    }
                                }else{
                                    if (data) {
                                        getConverttoBook('oldgetRatesBKG', '');
                                    } else {
                                        var ofrAmount = document.getElementById("ofrRollUpAmount").value;
                                        var url = rootPath+'/fclQuotes.do?buttonValue=getRatesBooking&origin=' + document.EditBookingsForm.originTerminal.value
                                            + "&pol=" + document.EditBookingsForm.portOfOrigin.value + "&pod=" + document.EditBookingsForm.destination.value
                                            + "&destn=" + document.EditBookingsForm.portOfDischarge.value + "&comid=" + document.EditBookingsForm.commcode.value
                                            + "&carrier=" + career + "&quoteDate=" + '<%=bookingDate1%>'
                                            + "&bookingDate=" + bookingDate + "&OFRRollUpAmount=" + ofrAmount + "&fileNo=" + '${bookingValues.fileNo}' + "&hazmat=" + haz + '&page=BookingPage';
                                        GB_show("Rate Change Alert", url, 500, 950);
                                    }
                                }
                            }
                        });
                    }else{
                        if(id1 === 'copyBooking'){
                            document.EditBookingsForm.buttonValue.value = id1;
                            document.EditBookingsForm.submit();
                        }else{
                            getConverttoBook('oldgetRatesBKG', '');
                        }
                    }
                } else if (id1 == 'inland' && id2 == 'ok') {
                    document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
                    document.EditBookingsForm.buttonValue.value = "deleteInland";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'inland' && id2 == 'cancel') {
                    document.getElementById("inlandVal").innerHTML = "Inland";
                    document.EditBookingsForm.inland[0].checked = true;
                } else if (id1 == 'IntermodalRamp' && id2 == 'ok') {
                    document.getElementById("inlandVal").innerHTML = "Inland";
                    document.EditBookingsForm.buttonValue.value = "deleteIntermodalRamp";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'IntermodalRamp' && id2 == 'cancel') {
                    document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
                    document.EditBookingsForm.inland[0].checked = true;
                } else if (id1 == 'docCharge' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "deleteDocumentCharge";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'docCharge' && id2 == 'cancel') {
                    document.EditBookingsForm.docCharge[0].checked = true;
                       } else if (id1 == "deletePierPassCharge" && id2 == "ok") {
                        document.EditBookingsForm.buttonValue.value = "deletePierPassCharge";
                        document.EditBookingsForm.submit();
                    } else if (id1 == "deletePierPassCharge" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                    } else if (id1 == "polChangeDeletePierPassChargeBooking" && id2 == "ok") {
                        document.getElementById("pierPassN").checked = true;
                        document.EditBookingsForm.buttonValue.value = "deletePierPassCharge";
                        document.EditBookingsForm.submit();
                    } else if (id1 == "polChangeDeletePierPassChargeBooking" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                        var placeofReceiptForPierPassBooking = jQuery("#placeofReceiptForPierPassBooking").val();
                        jQuery("#portOfOrigin").val(placeofReceiptForPierPassBooking);
                        jQuery("#portOfOrigin_check").val(placeofReceiptForPierPassBooking); 
                    } else if (id1 == "originChangeDeletePierPassChargeBooking" && id2 == "ok") {
                        document.getElementById("pierPassN").checked = true;
                        document.EditBookingsForm.buttonValue.value = "deletePierPassCharge";
                        document.EditBookingsForm.submit();
                    } else if (id1 == "originChangeDeletePierPassChargeBooking" && id2 == "cancel") {
                        document.getElementById("pierPassY").checked = true;
                        var originTerminalForPierPassBooking = jQuery("#originTerminalForPierPassBooking").val();
                        jQuery("#originTerminal").val(originTerminalForPierPassBooking);
//                        jQuery("#placeofReceipt_check").val(originTerminalForPierPassBooking);
                } else if (id1 == 'deleteCharge' && id2 == 'ok') {
                    document.EditBookingsForm.submit();
                } else if (id1 == 'changeToPerBl' && id2 == 'ok') {
                    document.EditBookingsForm.submit();
                } else if (id1 == 'deleteBlCharge' && id2 == 'ok') {
                    document.EditBookingsForm.submit();
                } else if (id1 == 'getFFCommission' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "FFCommssion";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'deleteFFCommission' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "deleteFFCommssion";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'convertToQuotation' && id2 == 'ok') {
                    document.getElementById('eventCode').value = '100007';
                    document.EditBookingsForm.buttonValue.value = "convertToQuotation";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'convertToQuotation' && id2 == 'cancel') {
                    //do nothing
                } else if (id1 == 'getFFCommission' && id2 == 'cancel') {
                    document.getElementById('n5').checked = true;
                } else if (id1 == 'deleteFFCommission' && id2 == 'cancel') {
                    document.getElementById('y5').checked = true;
                } else if ((id1 == 'Forwarder' || id1 == 'Shipper' || id1 == 'Agent') && id2 == 'ok') {
                    document.EditBookingsForm.accountName.value = "";
                    document.EditBookingsForm.accountNumber1.value = "";
                    document.getElementById('accountName').disabled = true;
                    document.getElementById('accountNumber1').disabled = true;
                    document.getElementById('toggle').style.visibility = "hidden";
                    if (id1 == 'Forwarder') {
                        document.EditBookingsForm.billToCode[0].checked = true;
                    } else if (id1 == 'Shipper') {
                        document.EditBookingsForm.billToCode[1].checked = true;
                    } else {
                        document.EditBookingsForm.billToCode[3].checked = true;
                    }
                } else if ((id1 == 'Forwarder' || id1 == 'Shipper' || id1 == 'Agent') && id2 == 'cancel') {
                    document.EditBookingsForm.billToCode[2].checked = true;
                } else if (id1 == 'goBack' && id2 == 'yes') {
                    yesFunction();
                } else if (id1 == 'goBack' && id2 == 'no') {
                    noFunction();
                } else if (id1 == 'goBack' && id2 == 'cancel') {
                    return;
                } else if (id1 == 'allowBookingToComplete' && id2 == 'ok') {
                    document.getElementById("bookingCompleteY").disabled = true;
                    call();//--to display processing image---
                    SAVE();
                    //--make the page readonly when booking complete------
                    //makePageReadOnlyOnBookingComplete(document.getElementById("editbook"),'');
                } else if (id1 == 'allowBookingToComplete' && id2 == 'cancel') {
                    document.EditBookingsForm.bookingComplete[1].checked = true;
                } else if (id1 == 'deletePBACharges' && id2 == 'ok') {
                    document.EditBookingsForm.prepaidToCollect[0].checked = true;
                    document.EditBookingsForm.billToCode[0].disabled = false;
                    document.EditBookingsForm.billToCode[1].disabled = false;
                    document.EditBookingsForm.billToCode[2].disabled = false;
                    document.EditBookingsForm.billToCode[3].disabled = false;
                    document.EditBookingsForm.billToCode[3].checked = false;
                    document.EditBookingsForm.billToCode[3].disabled = true;
                    document.EditBookingsForm.action.value = "deletePBACharges";
                    var action = "deletePba";
                    call();//--to display processing image---
                    SAVE(action);
                } else if (id1 == 'deletePBACharges' && id2 == 'cancel') {
                    document.EditBookingsForm.prepaidToCollect[1].checked = true;
                } else if (id1 == 'clearConsigneeDetails' && id2 == 'ok') {
                    clearConsigneeDetails();
                    document.getElementById("consignee").disabled = true;
                    document.getElementById("consignee").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearConsigneeDetails' && id2 == 'cancel') {
                    document.getElementById("consigneeTpCheck").checked = false;
                } else if (id1 == 'clearShipperDetails' && id2 == 'ok') {
                    clearShipperDetails();
                    document.getElementById("shipper").disabled = true;
                    document.getElementById("shipper").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearShipperDetails' && id2 == 'cancel') {
                    document.getElementById("shipperTpCheck").checked = false;
                } else if (id1 == 'clearSpotAddrDetails' && id2 == 'ok') {
                    clearSpottAddrDetails();
                    document.getElementById("spottingAccountNo").disabled = true;
                    document.getElementById("spottingAccountNo").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearSpotAddrDetails' && id2 == 'cancel') {
                    document.getElementById("spottAddrTpCheck").checked = false;
                } else if (id1 == 'clearTruckerDetails' && id2 == 'ok') {
                    clearTruckerDetails();
                    document.getElementById("truckerCode").disabled = true;
                    document.getElementById("truckerCode").style.backgroundColor = '#CCEBFF';
                } else if (id1 == 'clearTruckerDetails' && id2 == 'cancel') {
                    document.getElementById("truckerTpCheck").checked = false;
                }
                else if (id1 == 'OnUncheckClearConsignee' && id2 == 'ok') {
                    clearConsigneeDetails();
                    document.getElementById("consignee").disabled = false;
                    document.getElementById("consignee").style.border = '1px solid #C4C5C4';
                    document.getElementById("consignee").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearConsignee' && id2 == 'cancel') {
                    document.getElementById("consigneeTpCheck").checked = true;
                } else if (id1 == 'OnUncheckClearShipper' && id2 == 'ok') {
                    clearShipperDetails();
                    document.getElementById("shipper").disabled = false;
                    document.getElementById("shipper").style.border = '1px solid #C4C5C4';
                    document.getElementById("shipper").style.backgroundColor = '#FFFFFF';
                } else if (id1 == 'OnUncheckClearShipper' && id2 == 'cancel') {
                    document.getElementById("shipperTpCheck").checked = true;
                } else if (id1 == 'deleteSpecialEquipment' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "deleteSpecialEquipment";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'deleteSpecialEquipment' && id2 == 'cancel') {
                    document.EditBookingsForm.specialequipment[0].checked = true;
                    enableSpecialEquipment();
                } else if (id1 == 'deleteSpecialEquipmentUnit' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "deleteSpecialEquipmentUnit";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'addHazmatRates' && id2 == 'ok') {
                    document.EditBookingsForm.inland[1].checked = true;
                    document.EditBookingsForm.buttonValue.value = "addhazmat";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'addHazmatRates' && id2 == 'cancel') {
                    document.EditBookingsForm.hazmat[1].checked = true;
                } else if (id1 == 'deleteHazmatRates' && id2 == 'ok') {
                    document.EditBookingsForm.buttonValue.value = "deletehazmat";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'deleteHazmatRates' && id2 == 'cancel') {
                    document.EditBookingsForm.hazmat[0].checked = true;
                } else if (id1 == 'deleteBookingCostCode' && id2 == 'ok') {
                    document.EditBookingsForm.costCode.value = costId;
                    document.EditBookingsForm.buttonValue.value = "deleteBookingCostCode";
                    document.EditBookingsForm.submit();
                } else if (id1 == 'cancelBkgRequest' && id2 == 'ok') {
                    generate300Xml('cancel');
                }
                
            }
            function enableOrDisableChassis(){
               
                if(!${importFlag}){
                   jQuery.ajaxx({
                      data: {
                          className: "com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO",
                          methodName: "checkvendorForChassisChargeOnload",
                          param1: "${bookingValues.bookingId}"       
                      },
                       success: function(data) {
                          
                       if(data === "" || data === null){
                           
                           jQuery("#chassisChargeY").attr("disabled",true);
                           jQuery("#chassisChargeN").attr("disabled",true);
                           
                        } else {
                            jQuery("#chassisChargeY").attr("disabled",false);
                           jQuery("#chassisChargeN").attr("disabled",false);  
                       }
                   }
                   }); 
                  }
                  }
             function enableOrDisablePierPassBooking(){
                 if(!${importFlag}){
               var origin = document.EditBookingsForm.originTerminal.value;
               var pol = document.EditBookingsForm.portOfOrigin.value;
               var nvoMove = document.getElementById("moveType").value;                    
               if(origin === pol && (nvoMove === "DOOR TO DOOR" || nvoMove === "DOOR TO PORT" || nvoMove === "DOOR TO RAIL")){                          
                  jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.UnLocationDAO",
                    methodName: "getPierPass",
                    param1: origin
                },
                success: function (data) {
                    if(data === "true"){
                     jQuery("#pierPassY").attr("disabled", false);
                     jQuery("#pierPassN").attr("disabled", false); 
                    }
                  }
                });                      
               }else{
                  jQuery("#pierPassY").attr("disabled", true);
                  jQuery("#pierPassN").attr("disabled", true);
                  document.EditBookingsForm.pierPass.value ="N";
               }               
             }
       }
         function polChangeBooking() {                   
                   var placeofReceiptForPierPass = jQuery("#placeofReceiptForPierPassBooking").val();
                   var pol = document.EditBookingsForm.portOfOrigin.value;
                    if(pol !== placeofReceiptForPierPass && document.EditBookingsForm.pierPass.value === "Y" && document.EditBookingsForm.originTerminal.value !== ''){
                        confirmNew("Pol value is chaged Pier pass charge will be removed, are you sure you want to continue? ", "polChangeDeletePierPassChargeBooking");
               
                }
                enableOrDisablePierPassBooking();
            }
                function originChangeBooking() {                   
                   var originTerminalForPierPass = jQuery("#originTerminalForPierPassBooking").val();
                    var origin = document.EditBookingsForm.originTerminal.value;
                    if(origin !== originTerminalForPierPass && document.EditBookingsForm.pierPass.value === "Y" && document.EditBookingsForm.portOfOrigin.value !== ''){
                       
                     confirmNew("Origin value is chaged Pier pass charge will be removed, are you sure you want to continue? ", "originChangeDeletePierPassChargeBooking");             
                }
                enableOrDisablePierPassBooking();
            }
            function calculatePierPassCharge() { 
                document.EditBookingsForm.buttonValue.value = "addPierPassCharge";
                document.EditBookingsForm.submit();
                }

            function deletePierPassCharge() {
               confirmNew("Pier Pass charges will be deleted from the Grid and all the Booking changes will be saved. Are you sure? ", "deletePierPassCharge");
             }
            var costId = "";
            function deleteBookingAccruals(id) {
                costId = id;
                confirmNew("Are you sure you want to delete this CostCode", "deleteBookingCostCode");
            }
        </script>
        <style type="text/css">
            .poaGreen{
                color:green;
                background-color: white;
                font-weight:bold;
                font-size:13;

            }
            .poaRed{
                background-color: white;
                color:red;
                font-weight:bold;
                font-size:13;
            }
            #outOfGaugeCommentDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: white;
                _height: expression(document.body.offsetHeight + "px");
            }
            .blackBG {font-size:15;color:Black;}
            #commentsPopupDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: white;
                _height: expression(document.body.offsetHeight + "px");
            }
        </style>
        <link rel="stylesheet" href="<%=path%>/css/jquery-tabs/jquery.tabs.css" type="text/css"
              media="print, projection, screen" />
        <link rel="stylesheet" href="<%=path%>/css/default/style.css" type="text/css"
              media="print, projection, screen" />
        <link rel="alternate stylesheet" type="text/css" media="all"
              href="<%=path%>/css/cal/calendar-win2k-cold-2.css"  title="win2k-cold-2" />
        <link rel="stylesheet" type="text/css"
              href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
    </head>
    <div id="ConfirmYesOrNo" class="alert">
        <p class="alertHeader"><b>Confirmation</b></p>
        <p id="innerText2" class="containerForAlert"></p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="Yes"
                   onclick="newConfirmYes()">
            <input type="button"  class="buttonStyleForAlert" value="No"
                   onclick="newConfirmNo()">
        </form>
    </div>

    <div id="AlertBoxOk" class="alert">
        <p class="alertHeader"><b>Alert</b></p>
        <p id="innerText3" class="containerForAlert"></p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="Ok"
                   onclick="newConfirmBoxOk()">
        </form>
    </div>
    <div id="reminderBox" class="alert">
        <p class="alertHeader"><b>REMINDER:</b></p>
        <p id="innerTextReminder" class="containerForAlert">
        </p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="OK"
                   onclick="okForReminderBook()">
        </form>
    </div>
    <div id="cover" style="width: 100% ;height: 1000px;"></div>
    <body class="whitebackgrnd"  topmargin="0">
        <%@include file="../preloader.jsp"%>
         <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="commentDiv"   class="comments">
            <table border="1" id="commentTableInfo">
                <tbody border="0"></tbody>
            </table>
        </div>
        <div id="popup-cover">

        </div>
        <div id="popup-msg">

        </div>
        <div id="popup-skin">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image"src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <div id="bubble_tooltip_forComments" style="display: none;">
            <div class="bubble_top_forComments"><span></span></div>
            <div class="bubble_middle_forComments"><span id="bubble_tooltip_content_forComments"></span></div>
            <div class="bubble_bottom_forComments"></div></div>
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

        <!--DESIGN FOR NEW ALERT BOX AND CONFIRM BOX---->
        <div class="alert1">
            <div id="AlertBox" class="alert">
                <p class="alertHeader"><b>Alert</b></p>
                <p id="innerText" class="containerForAlert"></p>

                <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                    <input type="button"  class="buttonStyleForAlert" value="OK"
                           onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
                </form>
            </div>
        </div>

        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert"></p>

            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"  onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"  onclick="No()">
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
        <div id="commentsPopupDivForAmendment" class="remarks-popup" style="display: none; width:510px;left:30%;top:40%">
            <div align="right"></div>
            <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <tr class="tableHeadingNew" >
                    <th align="left" style="font-family: Trebuchet MS; font-size: 14px; cursor: help" title="Amendment remarks">Comments</th>
                    <th align="right"><div id="commentsTitleMessage" style="font-family: Trebuchet MS; font-size: 14px; cursor: help" title="Reason Amendment"></div></th>
                </tr>
                <tr>
                    <td class="textlabelsBold" colspan="2">
                        <textarea rows="5" cols="81" id="commentsAboutAmendment" onkeyup="getUpperCaseValue(this)" style="font-family: Trebuchet MS; font-size: 13px" onkeypress="return checkTextAreaLimit(this, 549)" title="Reason Amendment goes here!"></textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="button" value="Submit"  class="buttonStyleNew"  onclick="submitAmendmentComments()">
                        <input type="button" value="Cancel"  class="buttonStyleNew" onclick="closeAmendmentCommentsPopup()"/>
                    </td>
                </tr>
            </table>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <html:form  action="/editBooks?accessMode=${param.accessMode}" styleId="editbook" name="EditBookingsForm" type="com.gp.cvst.logisoft.struts.form.EditBookingsForm" scope="request">
            <span id="msgSpan" style="color:#FF4A4A;font-size:16px;font-weight:bold;margin-left:10px;">
                <%=msg%>
            </span>
            <c:if test="${bookingValues.localdryage=='Y'}">
                <b style="font-size: 15px;color: #FF4A4A;">(Local Drayage Included)</b>
            </c:if>
            <b style="margin-left:150px;color: #000080;font-size: 15px;"  id="scroll"><%=loackMessage%></b><br>

            <c:if test="${bookingValues.bookingComplete=='Y'}">
                <font color="#FF4A4A" size="2"><b style="padding-left:10px;">Booking Complete</b></font>
            </c:if>
            <c:choose>
                <c:when test="${bookingValues.soc!=null && bookingValues.soc=='N'}">
                    <font color="#FF4A4A" size="3"><b style="padding-left:10px;"><%="Rate Change is applied"%></b></font>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${bookingValues.soc!=null && bookingValues.soc=='Y'}">
                            <font color="#FF4A4A" size="3"><b style="padding-left:10px;"><%="Rate change is not applied"%></b></font>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr class="textlabelsBold">
                    <td align="right" style="padding-right: 15px;font-size: 13;">Booking Complete
                        <html:radio property="bookingComplete" value="Y" name="transactionBean" styleId="bookingCompleteY"
                                    onclick="setBookingComplete('${importFlag}')"/>Y
                        <html:radio property="bookingComplete" value="N"  name="transactionBean" styleId="bookingCompleteN" onclick="setBookingCompleteN('${cancelEdi}', '${createOrChange}','${checkDefaultAgent}', '${checkDirectConsign}', '${importFlag}', '${companyCode}')"/>N</td>
                </tr>
                <tr>
                    <td>
                        <table  border="0">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td id="fileNo">File No :<font color="#FF4A4A" size="4"><%=fileNo%></font></td>
                                        <input type="hidden" id="fileNumber" value="${bookingValues.fileNo}"/>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>Quote By :<b class="headerlabel" style="color:blue;">
                                                    <c:out value="${fn:toUpperCase(bookingValues.quoteBy)}"></c:out></b></td>
                                            <td style="padding-left: 5px;">On :
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="quoteDate1" value="${bookingValues.quoteDate}"/>
                                                <b class="headerlabel" style="color:blue;"><c:out value="${quoteDate1}"></c:out></b>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <c:choose>
                                                    <c:when  test="${bookingValues.username!=null}">
                                                        Booking By :<b class="headerlabel" style="color:blue;">${fn:toUpperCase(bookingValues.username)}</b>
                                                    </c:when>
                                                    <c:otherwise>
                                                        Booking By :<b class="headerlabel" style="color:blue;"><%=bookingBy%></b>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="padding-left:5px;">On :
                                                <c:choose>
                                                    <c:when  test="${bookingValues.bookingDate ne null}">
                                                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="dateBook"  value= "${bookingValues.bookingDate}"/>
                                                        <b class="headerlabel" style="color:blue;"><c:out value="${dateBook}"></c:out></b>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <b class="headerlabel" style="color:blue;"><c:out value="${bookingDate}"></c:out></b>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>BL By :<b class="headerlabel" style="color:blue;"><c:out value="${fn:toUpperCase(bookingValues.blBy)}" ></c:out></b></td>
                                            <td style="padding-left: 5px;">On :
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="blDate1" value="${bookingValues.blDate}"/>
                                                <b class="headerlabel" style="color:blue;"><c:out value="${blDate1}"></c:out></b>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <c:choose>
                                    <c:when test="${bookingValues.ediCreatedBy ne null and bookingValues.ediCanceledBy eq null}">
                                        <td>
                                            <table class="tableBorderNew" style="visibility: visible">
                                                <tr class="textlabelsBold">
                                                    <td>
                                                        <div class="info-box">
                                                            <b class="textlabelsBold">EDI Bkg By :</b>&nbsp;
                                                            <b class="headerlabel" style="color:blue" id="ediby">${fn:toUpperCase(bookingValues.ediCreatedBy)}</b>&nbsp
                                                            <b class="textlabelsBold">On :</b>&nbsp;
                                                            <c:if  test="${bookingValues.ediCreatedOn!=null}">
                                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="ediDate"  value= "${bookingValues.ediCreatedOn}"/>
                                                                <b class="headerlabel" style="color:blue;" id="edion"><c:out value="${ediDate}"></c:out></b>&nbsp
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="display:none" id="ediCreatedByDiv">
                                            <table class="tableBorderNew">
                                                <tr class="textlabelsBold">
                                                    <td>
                                                        <div class="info-box">
                                                            <b class="textlabelsBold">EDI Bkg By :</b>&nbsp;
                                                            <b class="headerlabel" style="color:blue" id="ediby">${fn:toUpperCase(bookingValues.ediCreatedBy)}</b>&nbsp
                                                            <b class="textlabelsBold">On :</b>&nbsp;
                                                            <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="ediDate"  value= "${bookingValues.ediCreatedOn}"/>
                                                            <b class="headerlabel" style="color:blue;" id="edion"><c:out value="${ediDate}"></c:out></b>&nbsp
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${bookingValues.ediCanceledBy ne null}">
                                        <td style="visibility: visible">
                                            <table class="tableBorderNew">
                                                <tr class="textlabelsBold">
                                                    <td>
                                                        <div class="info-box">
                                                            <b class="textlabelsBold">EDI Bkg Canceled By :</b>&nbsp;
                                                            <b class="headerlabel" style="color:red" id="ediCancelBy">${fn:toUpperCase(bookingValues.ediCanceledBy)}</b>&nbsp
                                                            <b class="textlabelsBold">On :</b>&nbsp;
                                                            <c:if  test="${bookingValues.ediCreatedOn!=null}">
                                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="ediCanceledDate"  value= "${bookingValues.ediCanceledOn}"/>
                                                                <b class="headerlabel" style="color:red;" id="ediCancelOn"><c:out value="${ediCanceledDate}"></c:out></b>&nbsp
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="display:none" id="ediCancelByDiv">
                                            <table class="tableBorderNew">
                                                <tr class="textlabelsBold">
                                                    <td>
                                                        <div class="info-box">
                                                            <b class="textlabelsBold">EDI Bkg Canceled By :</b>&nbsp;
                                                            <b class="headerlabel" style="color:red" id="ediCancelBy">${fn:toUpperCase(bookingValues.ediCanceledBy)}</b>&nbsp
                                                            <b class="textlabelsBold">On :</b>&nbsp;
                                                            <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="ediCanceledDate"  value= "${bookingValues.ediCanceledOn}"/>
                                                            <b class="headerlabel" style="color:red;" id="ediCancelOn"><c:out value="${ediCanceledDate}"></c:out></b>&nbsp
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${bulletRates eq true}">
                                    <td>
                                        <table class="tableBorderNew">
                                            <tr class="textlabelsBold">
                                                <td>
                                                    <div class="red bold">**BULLET RATES**</div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </c:if>
                                <td id="spotRateMsgDiv" style="display:none">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <div id="spotRateMsgStatus" class="red bold"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <c:if test="${roleDuty.allowtoEnterSpotRate and bookingValues.ratesNonRates eq 'R' && importFlag eq false}">
                                    <td  align="right" style="width:80%;padding-left:10px;" class="textlabelsBold">Spot/Bullet Rate
                                        <html:radio property="spotRate" value="Y" name="transactionBean" styleId="spotRateY" onclick="confirmVid('Y',false)"/>Y
                                        <html:radio property="spotRate" value="N" name="transactionBean" styleId="spotRateN" onclick="confirmVid('N',false)"/>N &nbsp;&nbsp;
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </td>
                </tr><%--1st row ends--%>

                <tr valign="top"><%--2nd row--%>
                    <td>
                        <table width="100%" cellspacing="0" border="0"  cellpadding="0">
                            <tr class="textlabelsBold"></tr>
                            <tr>
                                <td>
                                    <input type="button" value="Go Back" id="cancel" class="buttonStyleNew"
                                           onclick="compareWithOldArray()"/>
                                    <input type="button" value="Save" id="save" class="buttonStyleNew" onclick="saveOrUpdate()" style="width:50px;" onmouseover="tooltip.show('<strong><%=mandatoryFieldForBooking%></strong>', null, event);" onmouseout="tooltip.hide();"/>
                                    <c:if test="${bookingValues.blFlag!='on'}">
                                        <input type="button"  class="buttonStyleNew" id="charge" value="ConvertToBL"
                                               onclick="converttobl('${importFlag}')" style="width:80px"/>
                                    </c:if>
                                    <input type="button"  class="buttonStyleNew" id="copyBooking" value="Copy"
                                           onclick="copyBookings()" />
                                    <input type="button" id="bConf" value="Print/Fax/Email" class="buttonStyleNew"
                                           onclick="PrintReports('<%=blFlag%>','${importFlag}')" style="width:100px"/>
                                    <c:set var="manualNotesCount" value="buttonStyleNew"/>
                                    <c:if test="${ManualNotes}">
                                        <c:set var="manualNotesCount" value="buttonColor"/>
                                    </c:if>
                                    <input type="button" class="${manualNotesCount}" id="note" name="search" value="Note" style="width:50px;"
                                           onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '${bookingValues.fileNo}', 300, 700);" />
                                    <c:choose>
                                        <c:when test="${null!=TotalScan && TotalScan!='0'}">
                                            <input id="scanButton" class="buttonColor" type="button"
                                                   value="Scan/Attach" onClick="scan('${bookingValues.fileNo}', '${importFlag}')"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input id="scanButton" class="buttonStyleNew" type="button"
                                                   value="Scan/Attach" onClick="scan('${bookingValues.fileNo}', '${importFlag}')"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${newBooking == 'edit'}">
                                        <input type="button" class="buttonStyleNew" id="ReversetoQuote"  value="Reverse to Quote" style="width:100px;"
                                               onclick="convertToQuote('${bookingValues.fileNo}')"   />
                                    </c:if>
                                    <input type="button" class="buttonStyleNew" value="Hazmat"  id="hazmatButton" onclick="getPopHazmat('<%=fileNo%>')"/>
                                    <input id="inbondButton" type="button" value="Inbond" class="buttonStyleNew"
                                           onclick="gotoInbond('<%=bookingId%>', '<%=fileNo%>')" style="width:60px;" />
                                    <input type="button" id="arRedInvoice" value="AR Invoice" class="buttonStyleNew"
                                           onclick="return GB_show('AR Invoice', '<%=path%>/arRedInvoice.do?action=listArInvoice&fileNo=${bookingValues.fileNo}&screenName=BOOKING&fileType=${bookingValues.importFlag}' + '&voyInternal=${bookingValues.voyageInternal}', 550, 1100)"/>
                                    <c:if test="${loginuser.role.roleDesc == 'Admin'}">
                                        <input type="button" value="ResendToBlue" id="resend"  class="buttonStyleNew" onclick="resendToBlueScreen('${bookingValues.fileNo}')"/>
                                    </c:if>
                                    <input type="button" value="Send EDI Bkg" id="sendEdi" style="visibility:hidden" class="buttonStyleNew" onclick="amendmentOr300Xml('${createOrChange}')"/>
                                    <input type="button" value="Cancel EDI Bkg" id="cancelEdi" style="visibility:hidden" class="buttonStyleNew" onclick="confirmCancelBkgEdi()"/>
                                </td>
                                <td align="right" style="padding-left:10px;" class="textlabelsBold">
                                    Brand
                                    <c:choose>
                                        <c:when test="${companyCode == '03'}">
                                            <html:radio property="brand" value="Econo"  name="transactionBean" styleId="brandEcono" onclick="checkBrand('Econo','${bookingValues.bookingId}','${companyCode}')"/>Econo
                                        </c:when>
                                        <c:otherwise>
                                            <html:radio property="brand" value="OTI"  name="transactionBean" styleId="brandOti" onclick="checkBrand('OTI','${bookingValues.bookingId}','${companyCode}')"/>OTI
                                        </c:otherwise>
                                    </c:choose>
                                    <html:radio property="brand" value="${commonConstants.ECU_Worldwide}" name="transactionBean" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${bookingValues.bookingId}','${companyCode}')"/>Ecu Worldwide 

                                    &nbsp;&nbsp;&nbsp;
                                    <c:choose>
                                        <c:when test="${bookingValues.ratesNonRates=='N'}">
                                            Non-Rated<html:radio property="ratesNonRates" value="N" styleId="nonRated" name="transactionBean" disabled="true"/>&nbsp;&nbsp;
                                            Break Bulk<html:radio property="breakBulk" value="Y" name="transactionBean" styleId="breakBulkY"  disabled="true"/>Y
                                            <html:radio property="breakBulk" value="N" name="transactionBean" styleId="breakBulkN"  disabled="true"/>N
                                        </c:when>
                                        <c:otherwise>
                                            Rated<html:radio property="ratesNonRates" value="R"  name="transactionBean" disabled="true" styleId="rated-yes"/>&nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr><%--2nd ends--%>
                <tr valign="top" style="padding-top:10px;"><%--3rd row--%>
                    <td width="100%">
                        <table width="100%" border="0" class="tableBorderForFcl" cellpadding="0" cellspacing="0" height="10px;" >
                            <tr>
                                <td valign="top">
                                    <table width="100%" border="0" cellpadding="1" cellspacing="0">
                                        <tr class="tableHeadingNew"><td colspan="3">Booking Information</td></tr>
                                        <tr class="textlabelsBold">
                                            <td>SS Bkg #</td>
                                            <td align="left">Contact/Carrier Tel #</td>
                                        </tr>
                                        <tr>
                                            <td><html:text property="SSBooking" style="text-transform: uppercase" styleId="SSBooking"
                                                       styleClass="textlabelsBoldForTextBox mandatory" value="${bookingValues.SSBookingNo}" size="20"  maxlength="20"  /></td>
                                            <td align="left">&nbsp;<html:text property="carrierTel" styleClass="textlabelsBoldForTextBox" styleId="carrierTel"  onblur="checkForNumberAndDecimal(this)"  value="${bookingValues.telNo}" size="15" maxlength="30"  tabindex="-1" onkeyup="getIt(this)"/></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Booking Contact /<br>Carrier Name</td>
                                            <td align="left">Contact/Carrier Email</td>
                                        </tr>
                                        <tr>
                                            <td align="left"><html:text property="SSLineBookingRep" styleClass="textlabelsBoldForTextBox"
                                                       size="20" value="${bookingValues.SSLineBookingRepresentative}" maxlength="200" style="text-transform: uppercase"  tabindex="-1"/>
                                            </td>
                                            <td aligh="right">&nbsp;<html:text property="bookingemail" styleClass="textlabelsBoldForTextBox" onblur="emailValidate(this)"
                                                       value="${bookingValues.bookingemail}" size="15" maxlength="50"  tabindex="-1"></html:text></td>
                                        </tr>
                                    </table>
                                </td>
                                <td  valign="top" style="width: 10%">
                                    <table width="100%" border="0" cellpadding="1" cellspacing="0"  style="padding-bottom:10px;">
                                        <tr class="tableHeadingNew">
                                            <td colspan="4">Carrier Information</td>
                                        </tr>
                                        <tr>
                                            <td  class="textlabelsBold"  align="right">Vessel</td>
                                            <c:choose>
                                                <c:when test="${importFlag}">
                                                    <td align="left"><span class="textlabelsBold">
                                                            <input  class="textlabelsBoldForTextBox mandatory" name="manualVesselName" id="vesselname_checkn" style="text-transform: uppercase"  size="13" value="${bookingValues.manualVesselName}" />
                                                            <input name="vessel" Class="textlabelsBoldForTextBox mandatory"
                                                                   id="vessel" maxlength= "30" size="17" onkeydown="setFocusToSSVOY()" onblur="tabVessel();" value="${bookingValues.vessel}"  />
                                                            <c:choose>
                                                                <c:when test="${bookingValues.vesselNameCheck=='on'}">
                                                                    <input type="checkbox" name="vesselNameCheck" onclick="checkVesselName()" id="vessel_check" checked/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="vesselNameCheck" onclick="checkVesselName()" id="vessel_check" />
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <input type="hidden" id="vessel_check" name="vessel_check" value="${bookingValues.vessel}"/>
                                                            <div id="vessel_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                            <script type="text/javascript">
                                                                initAutocompleteWithFormClear("vessel", "vessel_choices", "", "vessel_check",
                                                                "<%=path%>/actions/getVesselName.jsp?tabName=BOOKING&from=0&isDojo=false", "setVessName('${bookingValues.blFlag}','${bookingValues.fileNo}')");
                                                            </script>
                                                        </span>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td align="left"><span class="textlabelsBold">
                                                            <input name="vessel" Class="textlabelsBoldForTextBox mandatory"
                                                                   id="vessel" maxlength= "30" size="17" onkeydown="setFocusToSSVOY()" onblur="tabVessel();" value="${bookingValues.vessel}"  />
                                                            <input type="hidden" id="vessel_check" name="vessel_check" value="${bookingValues.vessel}"/>
                                                            <div id="vessel_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                            <script type="text/javascript">
                                                                initAutocompleteWithFormClear("vessel", "vessel_choices", "", "vessel_check",
                                                                "<%=path%>/actions/getVesselName.jsp?tabName=BOOKING&from=0&isDojo=false", "setVessName('${bookingValues.blFlag}','${bookingValues.fileNo}')");
                                                            </script>
                                                        </span>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td class="textlabelsBold" align="right">Carrier</td>
                                            <%if (quoteId != null && quoteId.equals("")) {
                                                            BookingFcl bookingFcl = (BookingFcl) request.getAttribute("bookingValues");
                                                            String ssl = (null != bookingFcl && null != bookingFcl.getSslname()) ? bookingFcl.getSslname() : "";
                                                            ssl = ssl.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;");
                                            %>
                                            <td align="left">
                                                <span onmouseover="tooltip.show('<strong><%=ssl%></strong>', null, event);"
                                                      onmouseout="tooltip.hide();"  class="textlabelsBold">
                                                    <input name="sslDescription" class="textlabelsBoldForTextBox" id="sslDescription" maxlength="50"
                                                           size="28" value="<%=ssl%>" />
                                                    <input name="sslname_check" class="textlabelsBoldForTextBox" id="sslname_check" maxlength="50" type="hidden"
                                                           size="28" value="<%=ssl%>" />
                                                    <div id="streamShipName_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initOPSAutocomplete("sslDescription", "streamShipName_choices", "", "sslname_check",
                                                        "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=15", "");
                                                    </script>
                                                    <img src="<%=path%>/img/icons/display.gif" onclick="getSslCode()" />
                                                </span>
                                            </td>
                                            <%} else {
                                                BookingFcl bookingFcl = (BookingFcl) request.getAttribute("bookingValues");
                                                String ssl = (null != bookingFcl.getSslname()) ? bookingFcl.getSslname() : "";
                                                ssl = ssl.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;");
                                            %>
                                            <td align="left"><span onmouseover="tooltip.show('<strong><%=ssl%></strong>', null, event);"
                                                                   onmouseout="tooltip.hide();"  class="textlabelsBold">
                                                    <input name="sslDescription" class="BackgrndColorForTextBox" readonly ="true"
                                                           id="sslDescription" maxlength="50" size="35" value="<%=ssl%>"/>
                                                    <input name="sslname_check" class="textlabelsBoldForTextBox" id="sslname_check" maxlength="50" type="hidden"
                                                           size="28" value="<%=ssl%>" />
                                                    <div id="streamShipName_choices"  style="display: none;width: 5px;"  align="left"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initOPSAutocomplete("sslDescription", "streamShipName_choices", "steamShipNumber", "sslname_check",
                                                        "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=15", " bkgCarrier()");
                                                    </script>
                                                    <input type="hidden" id="steamShipNumber" name="steamShipNumber">
                                                    <img id="carrierContact" src="<%=path%>/img/icons/display.gif" onclick="getSslCode()" />
                                                </span>
                                            </td>
                                            <%}%>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">SS Voy</td>
                                            <td><html:text property="voyage" styleClass="textlabelsBoldForTextBox mandatory" styleId="ssVoy" size="17" onchange="setSSVoyage('${bookingValues.blFlag}','${bookingValues.fileNo}')"
                                                       value="${bookingValues.voyageCarrier}"   maxlength="50" style="text-transform: uppercase"  /></td>
                                            <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Carrier Print</td>
                                            <td>
                                                <html:checkbox property="carrierPrint" name="transactionBean" tabindex="-1"></html:checkbox>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold" >
                                            <td style="padding-bottom:7px;" align="right">&nbsp;&nbsp;&nbsp;Voy Internal(CFCL)</td>
                                            <td style="padding-bottom:7px;">
                                                <input name="vaoyageInternational" Class="textlabelsBoldForTextBox" id="vaoyageInternational" maxlength= "20" size="17" value="${bookingValues.voyageInternal}" tabindex="-1" />
                                            <div id="voyage_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                                initAutocompleteWithFormClear("vaoyageInternational", "voyage_choices", "", "",
                                                                "<%=path%>/actions/getLclExportsVoyage.jsp?&origin=${bookingValues.originTerminal}&destination=${bookingValues.portofDischarge}", "saveCFCLLinkedDrs();");
                                            </script>
                                            
                                            <c:if test="${not empty bookingValues.voyageInternal}">
                                                <img src="<%=path%>/img/icons/astar.gif" alt="clearRatesIcon" width="12" height="12" onclick="confirmYesOrNo('Are you sure do you want to remove CFCL voyage details?','removedOldCFCLVoyageDetails');"/> 
                                                <input type="button" class="buttonStyleNew" value="DRs" onclick="viewCFCLLinkedDrs('viewCFCLLinkedDrs','${bookingId}'),'${bookingValues.fileNo}'"
                                            </c:if></td>
                                            <td align="right">Hazmat</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${importFlag && newBooking == 'new'}">
                                                        <html:radio property="hazmat" value="Y"  onclick="getHazmat()" name="transactionBean" styleId="hazmatY"/>Yes
                                                        <html:radio property="hazmat" value="N"  name="transactionBean" styleId="hazmatN"/>No
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:radio property="hazmat" value="Y"  onclick="getHazmat()" name="transactionBean" styleId="hazmatY"/>Yes
                                                        <html:radio property="hazmat" value="N"  onclick="deleteHazmatRates()" name="transactionBean" styleId="hazmatN"/>No
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top">
                                    <table class="textlabelsBold" width="100%" border="0" cellpadding="1" cellspacing="0">
                                        <tr class="tableHeadingNew">   
                                            <td colspan="2">
                                                <c:choose>
                                                    <c:when test="${shippingCodeInttra}">
                                                        <span style="visibility: visible"> EDI: <b class="BackgrndColorForTextBox">INTTRA</b></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="visibility: hidden"> EDI: <b class="BackgrndColorForTextBox">INTTRA</b></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        <tr>
                                            <td class="align-right">Cargo ready Date</td>
                                            <td style="padding-right: 2px;">
                                                <fmt:formatDate pattern="MM/dd/yyyy"  var="cargoReady" value="${bookingValues.cargoReadyDate}"/>
                                                <html:text property="cargoReadyDate"   styleClass="textlabelsBoldForTextBox" size="19"
                                                           value="${cargoReady}" styleId="txtcargoReadyDateCal"  onblur="dateValidation(this)"  />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cargoReadyDateCal"
                                                     onmousedown="insertDateFromCalendar(this.id, 0);"/>
                                            </td>
                                        </tr>
                                        <c:if test="${shippingCodeInttra}">
                                            <tr>
                                                <td class="align-right">EDI Booking Comments</td>
                                                <td>
                                                    <html:textarea property="bookingComments" styleId="bookingComments" rows="3" cols="50"
                                                                   onkeyup="getUpperCaseValue(this)" styleClass="textarea"
                                                                   onkeypress="return checkTextAreaLimit(this, 500)" value="${bookingValues.bookingComments}"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <c:choose>
                                                    <c:when test="${cancelEdi}">
                                                        <td class="align-right">Ready to send Bkg Request via EDI</td>
                                                        <td>
                                                            <html:checkbox property="readyToSendEdi" disabled="false" styleId="readyToSendEdi" tabindex="-1" onclick="readyForEdi()"></html:checkbox>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="align-right" class="hotspot" onmouseover="tooltip.show('<strong>Cancelled Booking cannot Resurrected</strong>', null, event);"
                                                            onmouseout="tooltip.hide();" align="left">Ready to send Bkg Request via EDI</td>
                                                        <td>
                                                            <html:checkbox property="readyToSendEdi" styleId="readyToSendEdi" tabindex="-1"
                                                                           disabled="true" onclick="readyForEdi()"></html:checkbox>
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <td align="right">Carrier Doc Cut</td>
                                            <td style="width: 140px"><fmt:formatDate pattern="MM/dd/yyyy HH:mm a"  var="carrierDocCut" value="${bookingValues.carrierDocCut}"/>
                                                <html:text property="carrierDocCut"   styleClass="textlabelsBoldForTextBox" size="19" style="color:red;text-transform: uppercase"
                                                           value="${carrierDocCut}" styleId="txtcal314"    />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal314"
                                                     onmousedown="insertDateFromCalendar(this.id, 9);"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="0" class="tableBorderNewForFcl" cellpadding="0" cellspacing="0" height="10px;" >
                            <tr>
                                <td valign="top" width="500">
                                    <table  width="110%" border="0" cellpadding="1" cellspacing="0"  style="margin-top:2px;">
                                        <tr class="tableHeadingNew"><td colspan="4">Dates</td></tr>
                                        <tr class="textlabelsBold">
                                            <td align="right" width="120">Container Cut Off</td>
                                            <td style="width: 140px"><fmt:formatDate pattern="MM/dd/yyyy HH:mm a"  var="portCutOff" value="${bookingValues.portCutOff}"/>
                                                <html:text property="portCutOff"   styleClass="textlabelsBoldForTextBox mandatory" size="19" style="color:red;text-transform: uppercase"
                                                           value="${portCutOff}" styleId="txtcal313"  onchange="AlertMessage('${importFlag}',this)"  />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal313"
                                                     onmousedown="insertDateFromCalendar(this.id, 9);"/>
                                            </td>
                                            <c:if test="${importFlag eq false}">
                                            <td align="right" width="120">VGM Cut Off</td>
                                            <td style="width: 140px"><fmt:formatDate pattern="MM/dd/yyyy HH:mm a"  var="vgmCuttOff" value="${bookingValues.vgmCuttOff}"/>
                                                <html:text property="vgmCuttOff"   styleClass="textlabelsBoldForTextBox" size="19" style="color:red;text-transform: uppercase"
                                                           value="${vgmCuttOff}" styleId="txtcal315"  onchange="AlertMessage('${importFlag}',this)"  />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal315"
                                                     onmousedown="insertDateFromCalendar(this.id, 9);"/>
                                            </td>
                                            </c:if>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Doc Cut Off</td>
                                            <td align="left"><fmt:formatDate pattern="MM/dd/yyyy HH:mm a" var="docCutOff" value="${bookingValues.docCutOff}"/>
                                                <html:text property="docCutOff" styleClass="textlabelsBoldForTextBox mandatory" size="19" value="${docCutOff}"
                                                           style="color:red;text-transform: uppercase" styleId="txtcal71" onchange="AlertMessage('${importFlag}',this)" />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal71"
                                                     onmousedown="insertDateFromCalendar(this.id, 9);" />
                                            </td>

                                        </tr>
                                        <tr>
                                            <td class="textlabelsBold" align="right">ETD</td>
                                            <td align="left">
                                                <fmt:formatDate pattern="MM/dd/yyyy" var="estimatedDate" value="${bookingValues.etd}"/>
                                                <html:text property="estimatedDate" styleClass="textlabelsBoldForTextBox mandatory" size="19"
                                                           value="${estimatedDate}" onchange="validateDate1('${importFlag}',this);" styleId="txtcal22"  />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal22" onmousedown="insertDateFromCalendar(this.id, 0);"/>
                                            </td>

                                        </tr>
                                        <tr>
                                            <td  class="textlabelsBold" align="right">ETA</td>
                                            <td>
                                                <fmt:formatDate pattern="MM/dd/yyyy" var="estimatedAtten" value="${bookingValues.eta}"/>
                                                <html:text property="estimatedAtten" onkeydown="if(event.keyCode==9){tabLineMove();}"  styleClass="textlabelsBoldForTextBox mandatory" size="19"
                                                           value= "${estimatedAtten}" onchange="validateDate('${importFlag}',this);" styleId="txtcal5"  />
                                                <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal5"
                                                     onmousedown="insertDateFromCalendar(this.id, 0);" />
                                            </td>

                                        </tr>
                                        <tr class="textlabelsBold">
                                            <c:choose>
                                                <c:when test="${importFlag}">
                                                    <td style="width: 90px" align="right">
                                                        <span class="textlabelsBold" style="color:black;" >Docs Received</span>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td style="width: 90px" align="right">
                                                        <span class="hotspot" style="color:black;" onmouseover="tooltip.show('<strong>Document Received</strong>', null, event);"
                                                              onmouseout="tooltip.hide();">Docs Received<font class="mandatoryStarColor">*</font></span>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>
                                                <html:radio property="documentsReceived" value="Y" name="transactionBean" styleId="docsReceivedY" onclick="disableBookingContact(this)" />Y
                                                <html:radio property="documentsReceived" value="N" name="transactionBean" styleId="docsReceivedN" onclick="disableBookingContact(this)" />N
                                            </td>
                                            <td class="textlabelsBold" style="width: 100px;">
                                                Booking Contact
                                            </td>
                                            <td>
                                                <input type="text" Class="textlabelsBoldForTextBox" name="bookingContact" id="bookingContact" size="25" maxlength="100"
                                                       value="${bookingValues.bookingContact}" onblur="emailValidate(this)" />
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td align="left" valign="top" colspan="2">
                                    <table width="100%" border="0" cellpadding="1" cellspacing="0"  style="margin-top:2px;">
                                        <tr class="tableHeadingNew"><td colspan="5">Billing</td></tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Prepaid/Collect</td>
                                            <td align="left" colspan="3">
                                                <div style="float:left;">
                                                    <div style="float:left">
                                                        <html:radio property="prepaidToCollect" value="P" styleId="pRadio" name="transactionBean"
                                                                    onclick="disableBillToCode('radio')"/>P
                                                        <html:radio property="prepaidToCollect" styleId="cRadio" value="C"
                                                                    name="transactionBean" onclick="disableBillToCodeDup()"/>C
                                                    </div>
                                                    <div style="float:left;text-align:right;padding-left: 5px;">
                                                        <span id="warningMessage" class="warningStyle" dir="rtl"></span>
                                                        <span id="autosCredit" class="warningStyle" dir="rtl"></span>
                                                    </div>
                                            </td><td align="left" style="width: 100%">File Type
                                                <c:choose>
                                                    <c:when test="${importFlag}">
                                                        <input  value="IMPORT" class="BackgrndColorForTextBox" size="4" readonly="true" tabindex="-1" />
                                                        <input type="hidden" name="fileType" id="fileType" value="I"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:radio property="fileType" name="transactionBean" value="S"  styleId="fileTypeS" onclick="disableTextBox('vaoyageInternational');">
                                                            <span onmouseover="tooltip.show('<strong>Standard</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">S</span>
                                                        </html:radio>
                                                        <html:radio property="fileType" name="transactionBean" value="C" styleId="fileTypeC" onclick="enableInternalVoy();">
                                                            <span onmouseover="tooltip.show('<strong>CFCL</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">C</span>
                                                        </html:radio>

                                                        <html:radio property="fileType" name="transactionBean" value="P"  styleId="fileTypeP" onclick="disableTextBox('vaoyageInternational');">
                                                            <span onmouseover="tooltip.show('<strong>Project</strong>', null, event);"
                                                                  onmouseout="tooltip.hide();">P</span>
                                                        </html:radio>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            </div>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td width="15%" align="right">Bill To Code </td>
                                            <td width="15%" align="left">
                                                <html:radio property="billToCode" value="F"  name="transactionBean" onclick="disableThirdParty()"/>F
                                                <html:radio property="billToCode" value="S"  name="transactionBean" onclick="disableThirdParty()"/>S
                                                <html:radio property="billToCode" value="T" name="transactionBean" onclick="enableThirdParty()"/>T
                                                <c:choose>
                                                    <c:when test="${importFlag}">
                                                        <html:radio property="billToCode" value="C" name="transactionBean" onclick="disableThirdParty()"/>C
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:radio property="billToCode" value="A" name="transactionBean" onclick="disableThirdParty()"/>A
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td style="padding-bottom:15px;">Third Party Account Name</td>
                                            <td align="left" style="padding-bottom:15px;" >
                                                <input type="text" Class="textlabelsBoldForTextBox" name="accountName" id="accountName" size="25" maxlength="50"
                                                       value="${bookingValues.accountName}"   />
                                                <input Class="textlabelsBoldForTextBox"  name="accountName_check" id="accountName_check"   type="hidden"
                                                       value="${bookingValues.accountName}" />
                                                <div id="accountName_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("accountName", "accountName_choices", "accountNumber1", "accountName_check",
                                                    "<%=path%>/actions/tradingPartner.jsp?tabName=BOOKING&from=0", "checkForDisable()");
                                                </script>
                                                <img src="<%=path%>/img/icons/display.gif" id="toggle" onclick="getAccountDetails(this.value)"/>
                                            </td>
                                            <td style="padding-bottom:15px;">Account #
                                                <input type="text" name="accountNumber" Class="textlabelsBoldForTextBox" id="accountNumber1"
                                                       Class="textlabelsBoldForTextBox" size="15" value="${bookingValues.accountNumber}" maxlength="10" />
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
           </table>
            <%--maintable ends--%>

            <table width="100%" class="fontsize13">
                <tr class="textlabelsBold ">
                    <td>&nbsp;</td>
                    <td style="color:green">Origin :
                        <span style="color:olive;" >
                            <c:out value="${bookingValues.originTerminal}"></c:out></span></td>
                    <td style="color:green">Destination :
                        <span style="color:olive;">
                            <c:out value="${bookingValues.portofDischarge}"></c:out></span></td>
                    <td style="color:green">Commodity :
                        <span style="color:olive;">
                            <c:out value="${bookingValues.comcode}"></c:out></span></td>
                    <td style="color:green">Carrier :
                        <span style="color:olive;">
                            <c:out value="${bookingValues.sslname}"></c:out></span></td>
                </tr>
            </table>
            <c:import url="fragment/tableBookingRates.jsp"/>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <html:hidden property="numbIdx"/>
            <html:hidden property="numbers1"/>
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="unitSelect"/>
            <html:hidden property="costCode" styleId="costCode"/>
            <html:hidden property="number"/>
            <html:hidden property="chargeCode"/>
            <html:hidden property="chargeCodeDesc"/>
            <html:hidden property="costSelect"/>
            <html:hidden property="currency1"/>
            <html:hidden property="chargeAmt"/>
            <html:hidden property="minimumAmt"/>
            <html:hidden property="insuranceAmount" value="${bookingValues.insurancamt}"/>
            <html:hidden property="blNumber"/>
            <html:hidden property="selectedCheck"/>
            <html:hidden property="outOfGuageComment"/>
            <html:hidden property="unitselected"/>
            <html:hidden property="ssline" styleId="ssline"/>
            <html:hidden property="selectedOrigin"/>
            <html:hidden property="selectedDestination"/>
            <html:hidden property="selectedComCode"/>
            <html:hidden property="eventCode" styleId="eventCode"/>
            <html:hidden property="eventDesc" styleId="eventDesc"/>
            <html:hidden property="moduleId" value="<%=NotesConstants.FILE%>"/>
            <html:hidden property="moduleRefId" value="${bookingValues.fileNo}" styleId="moduleRefId"/>
            <html:hidden property="quoteBy" value="${bookingValues.quoteBy}"/>
            <html:hidden property="fileNo" value="${bookingValues.fileNo}"/>
            <html:hidden property="blBy" value="${bookingValues.blBy}"/>
            <html:hidden property="bookingId" value="<%=bookingId%>" styleId="bkgId"/>
            <html:hidden property="quoteId" value="<%=quoteId%>" styleId="quoteId"/>
            <html:hidden property="collapseid" value=""/>
            <html:hidden property="bundleOfr"/>
            <html:hidden property="docChargeAmount"/>
            <html:hidden property="otherChargesBundleOfr"/>
            <html:hidden property="standardChargeIndex"/>
            <html:hidden property="portremarks" value="${bookingValues.destRemarks}"/>
            <html:hidden property="ratesRemarks" value="${bookingValues.ratesRemarks}"/>
            <html:hidden property="importFlag" value="${importFlag}" styleId="importFlag"/>
            <html:hidden property="vendorAccountName" styleId="vendorAccountName"/>
            <html:hidden property="vendorAccountNo" styleId="vendorAccountNo"/>
            <html:hidden property="amount" styleId="amount"/>
            <html:hidden property="markUp" styleId="markUp"/>
            <input type="hidden" name="ofrRollUpAmount" id="ofrRollUpAmount" value="${ocenFrightRollUpAmount}"/>
            <input type="hidden"  id="oceanFrightForCollapse" value="${oceanFrightForCollapse}"/>
            <input type="hidden" name="bkgAlert" id="bkgAlert"/>
            <input type="hidden" name="selectedTab" id="selectedTab"/>
            <input type="hidden" name="path" id="path" value="${path}"/>
            <input type="hidden" id="hiddenRemarks" value="${bookingValues.onCarriageRemarks}"/>
            <input type="hidden" id="placeofReceiptForPierPassBooking" value="${bookingValues.portofOrgin}"/>
            <input type="hidden" id="originTerminalForPierPassBooking" value="${bookingValues.originTerminal}"/>
            <input type="hidden" id="companyCode" value="${companyCode}"/>
            <input type="hidden" id="fileTypeValue" value="${bookingValues.fileType}"/>
          
            <c:if test="${not empty bookingValues.quoteDate}">
                <fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.quoteDate}" var="date1"/>
                <html:hidden property="quoteDate" value="${date1}"/>
            </c:if>
            <c:if test="${not empty bookingValues.blDate}">
                <fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.blDate}" var="date2"/>
                <html:hidden property="blDate" value="${date2}"/>
            </c:if>
            <c:choose>
                <c:when  test="<%=bookingBy != null%>">
                    <html:hidden property="userName" value="<%=bookingBy%>" styleId="userName"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="userName" value="${bookingValues.username}" styleId="userName"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when  test="${bookingValues.bookingDate!=null}">
                    <fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.bookingDate}" var="date"/>
                    <html:hidden property="bookingDate" value="${date}"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="bookingDate" value="<%=bookingDate%>"/>
                </c:otherwise>
            </c:choose>
        </html:form>
        <c:if test="${! empty bookingValues.fileNo}">
            <script>load('<%=shipCheck%>', '<%=forwCheck%>', '<%=consCheck%>', '${buttonValue}');</script>
        </c:if>
        <script>setFocusToBookingNo();</script>
        <script type="text/javascript">
            var path = "/" + window.location.pathname.split('/')[1];
            jQuery(document).ready(function () {
        jQuery("#editbook").submit(function () {
            Lightbox.close();
            jQuery.startPreloader();
        });
        Lightbox.initialize({
            color: 'black',
            dir: path + '/js/lightbox/images',
            moveDuration: 1,
            resizeDuration: 1,
            parent: "#editbook"
        });
        jQuery("[title != '']").not("link").tooltip();
        jQuery(".message").fadeOut(10000, function () {
            $(this).html("");
        });
    });

            var checkDefault = '${checkDefaultAgent}';
            var checkDirect = '${checkDirectConsign}';
            
            if ((checkDefault != null && checkDefault != undefined) && (checkDirect != null && checkDirect != undefined)) {
                if (checkDefault == 'Y' && checkDirect == 'off') {
                    // document.searchQuotationform.defaultAgent[0].checked = true;
                    document.getElementById("alternateAgentY").checked = true;
                    document.getElementById("directConsignmentN").checked = true;
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                } else if (checkDefault == 'N' && checkDirect == 'on') {
                    document.getElementById("alternateAgentN").checked = true;
                    document.getElementById("directConsignmentY").checked = true;
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
                    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBoxDisabledLook";
                } else  if(checkDefault == 'N' && checkDirect == 'off') {
                    document.getElementById("alternateAgentN").checked = true;
                    document.getElementById("directConsignmentN").checked = true;
                    document.getElementById("agent").className = "textlabelsBoldForTextBox";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                }
            }
            function openTradingPartner(ev) {
                jQuery.ajaxx({
                    data: {
                        className : "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName : "clearTradingPartnerSession",
                        request : "true"
                    },
                    success: function(data) {
                        window.parent.GB_showFullScreen("Trading Partner", "<%=path%>/jsps/Tradingpartnermaintainance/SearchCustomer.jsp?callFrom=booking&programid=156&field=" + ev);
                    }
                });
            }
            if (typeof window.event != 'undefined')
                document.onclick = function() {
                    var agentOrConsignee = document.EditBookingsForm.billToCode[3].value;
                    var customerName = '';
                    if (agentOrConsignee == 'A' && null != document.getElementById('agent')) {
                        customerName = document.getElementById('agent').value;
                    } else if (null != document.getElementById('consignee')) {
                        customerName = document.getElementById('consignee').value;
                    }
            }
            function checkPrepaid(ev) {
                if (ev.value == '') {
                    document.getElementById("pRadio").checked = true;
                    disableBillToCode('');
                }
            }
            //setAlternateAgent('${importFlag}', '${companyCode}');
         function saveCFCLLinkedDrs() {
              var voyageNo = jQuery('#vaoyageInternational').val();
              var result =getLclArInvoiceStatus(voyageNo);
              if(result !== "" && result !== null) {
              alertNew("DR- <span style=color:red;font-weight: bold;>" + result + "</span>" + " has unposted AR Invoices, please Post these invoices before proceeding.");
              jQuery('#vaoyageInternational').val('');
              return false;    
              } else {
              confirmYesOrNo("Do you want to save CFCL Charges?", "saveCFCLLinkedDrs");
              }
         }     
       function viewCFCLLinkedDrs(buttonValue, bookingId, fileNo) {
        showPreloading();   
        var voyageNo = jQuery('#vaoyageInternational').val();
        var path = "/" + window.location.pathname.split('/')[1];
        document.EditBookingsForm.buttonValue.value = buttonValue;
        document.EditBookingsForm.bookingId.value = bookingId;
        document.EditBookingsForm.fileNo.value = fileNo;
        document.EditBookingsForm.vaoyageInternational.value = voyageNo;
        var params = jQuery('#editbook').serialize();
        jQuery.post(jQuery('#editbook').attr("action"), params,
                function (data) {
                    Lightbox.close();
                    closePreloading();
                    var url = path + "?TB_html&height=400&width=850";
                    var callback = "";
                    if(buttonValue === "saveCFCLLinkedDrs") {
                     callback = "refreshCFCLLinkedDrs();";   
                    }
                    Lightbox.showPopUp("CFCL Voyage", url, "sexylightbox", "", callback, data, "");
                });
    }
     function refreshCFCLLinkedDrs() {
        var voyageNo = jQuery('#vaoyageInternational').val();
        var bookingId = jQuery('#bkgId').val();
        var result =getHazmatStatus(voyageNo);
        var bookingDetails =getBookingDetails(bookingId, voyageNo);
        document.EditBookingsForm.estimatedDate.value = bookingDetails[0];
        document.EditBookingsForm.estimatedAtten.value = bookingDetails[1];
        document.EditBookingsForm.voyage.value = bookingDetails[2];
        document.EditBookingsForm.vessel.value = bookingDetails[3];
        document.EditBookingsForm.buttonValue.value = "update";
        if(result === "Y") {
        document.EditBookingsForm.hazmat.value = result;
        }
        document.EditBookingsForm.submit();
    }
    
    function removedOldCFCLVoyageDetails() {
     jQuery("#fileTypeS").attr('disabled',false);
     jQuery("#fileTypeP").attr('disabled',false);   
     document.EditBookingsForm.buttonValue.value = "removedOldCFCLVoyageDetails";
     document.EditBookingsForm.submit();   
    }
        </script>
        <%if (blFlag.equals("on")) {%>
        <script>
            window.parent.makeFormBorderless(document.getElementById("editbook"));
            makeFormBorderless(document.getElementById("editbook"));
        </script>
        <%} else {%>
        <script>disableAutoFF();
            checkHazmat();
            makeInbondButtonGreen('${bookingValues.bookingNumber}');
            disableBillToCode('');</script>
            <%}%>
            <%if (view.equals("3")) {%>
        <script>
            window.parent.disableFieldsWhileLocking(document.getElementById("editbook"));
            disableImages();
        </script>
        <%}%>
        <c:if test="${param.accessMode == '0'}">
            <script>
                window.parent.disableFieldsWhileLocking(document.getElementById("editbook"));
                disableImages();
            </script>
        </c:if>
        <script type="text/javascript">setTabName('<%=request.getAttribute("selectedTab")%>');
            call2();
        </script>
        <c:if test="${sendEdi eq 'true' and shippingCodeInttra}">
            <script language="javascript">
                jQuery("#action").val("");
                validateDataForEdi('${createOrChange}');
            </script>
        </c:if>
    </body>
    <script type="text/javascript">
        changeSelectBoxOnViewMode();
    </script>
</html>

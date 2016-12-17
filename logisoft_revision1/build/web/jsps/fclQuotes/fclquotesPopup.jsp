<%@ page language="java"
         import="java.util.*,com.gp.cong.logisoft.util.*,com.gp.cong.logisoft.beans.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.FclBuyCost,java.text.*,com.gp.cong.logisoft.util.CommonFunctions"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesConstants"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
           prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template"
           prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested"
           prefix="nested"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<bean:define id="RoleQuote" type="String">
    <bean:message key="RoleQuote" />
</bean:define>
<bean:define id="Hazardous" type="String">
    <bean:message key="HazardousPopUp" />
</bean:define>
<%    String path = request.getContextPath();
    String temp1 = "";
    RoleDuty rDuty = (RoleDuty) session.getAttribute("roleDuty");
    if (null == rDuty) {
        rDuty = new RoleDuty();
    }
    boolean hasUserLevelAccess = rDuty.isShowDetailedCharges();
    Quotation quotesDomain = new Quotation();
    FclBuy fclBuy = new FclBuy();
    DBUtil dbUtil = new DBUtil();
    List RatesList = new ArrayList();
    String noOfDays = "";
    if (request.getAttribute("noOfDays") != null) {
        noOfDays = (String) request.getAttribute("noOfDays");
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String QuickReview = "";
    if (request.getParameter("QuickReview") != null) {
        QuickReview = request.getParameter("QuickReview");
    }
    if (request.getAttribute("QuickReview") != null) {
        QuickReview = (String) request.getAttribute("QuickReview");
    }
    String comCode = "006100";
    String fts[] = null;
    String buttonValue = "";
    if (request.getAttribute("buttonValue") != null) {
        buttonValue = (String) request.getAttribute("buttonValue");
    }
    if (request.getAttribute("RATESLIST") != null) {
        RatesList = (List) request.getAttribute("RATESLIST");
    }
    List rateslist = new ArrayList();
    if (request.getAttribute("rateslist") != null) {
        rateslist = (List) request.getAttribute("rateslist");
    }
    List compressList = new ArrayList();
    if (request.getAttribute("compressList") != null) {
        compressList = (List) request.getAttribute("compressList");
    }
    List collapseList = new ArrayList();
    if (request.getAttribute("collapseList") != null) {
        collapseList = (List) request.getAttribute("collapseList");
    }
    String hazmat = "";
    if (request.getAttribute("hazmat") != null) {
        hazmat = (String) request.getAttribute("hazmat");
    }
    String msg = "";
    if (request.getAttribute("msg") != null) {
        msg = (String) request.getAttribute("msg");
    }
    User userid = null;
    if (session.getAttribute("loginuser") != null) {
        userid = (User) session.getAttribute("loginuser");
    }
    String CargoOriginvalue = "";
    String destPortvalue = "";
    String commIdvalue = "";
    String protsId = "";
    String originId = "";
    String commid = "";
    String buttonVal = "";
    String pol = "";
    String pod = "";
    String rampCity = "";

    if (request.getParameter("button") != null) {
        buttonVal = request.getParameter("button");

    }
    if (request.getAttribute("butval") != null) {
        buttonVal = (String) request.getAttribute("butval");
    }
    if (request.getAttribute("pId") != null && request.getAttribute("oId") != null && request.getAttribute("cId") != null) {
        protsId = (String) request.getAttribute("pId");
        originId = (String) request.getAttribute("oId");
        commid = (String) request.getAttribute("cId");
    }

    if (request.getAttribute("cargoOrigins") != null) {
        CargoOriginvalue = (String) request.getAttribute("cargoOrigins");
    }
    if (request.getAttribute("destinationPort") != null) {
        destPortvalue = (String) request.getAttribute("destinationPort");
    }
    if (request.getAttribute("commdValue") != null) {
        commIdvalue = (String) request.getAttribute("commdValue");
    }
    if (request.getAttribute("rampCity") != null) {
        rampCity = (String) request.getAttribute("rampCity");
    }
    String portofDischarge = "";
    String disNo = "";
    List searchFCLLIST = new ArrayList();
    NumberFormat numb = new DecimalFormat("###,###,##0.00");
    String ssLine = "";
    if (request.getAttribute("sslinenumber") != null) {
        ssLine = (String) request.getAttribute("sslinenumber");
    }

    String editpath = path + "/fclQuotes.do";
    String path1 = "";

    if (request.getAttribute("buttonValue") != null && request.getAttribute("buttonValue").equals("completed") && request.getAttribute("multiquote") == null) {
%>
<script>
    parent.parent.GB_hide();
    parent.parent.call();
    parent.parent.selectedMenu('${ratesPopupList[0]}', '${ratesPopupList[1]}', '${ratesPopupList[2]}', '${ratesPopupList[3]}', '${ratesPopupList[4]}', '${ratesPopupList[5]}', '${ratesPopupList[6]}');
</script>
<%}
    else if (request.getAttribute("multiquote") != null && request.getAttribute("buttonValue").equals("completed") && request.getAttribute("buttonValue") != null) {
%>
<script>
    parent.parent.GB_hide();
    parent.parent.call();
    parent.parent.selectedMenu('${ratesPopupList[6]}','${multiquote}');
</script>
<%}
//Forwarding and printing the message in parent page-----	
    if (!QuickReview.equals("hiddensubmit") && request.getAttribute("msg") != null && request.getAttribute("msg").equals("Rates")) {%>
<script>
    parent.parent.GB_hide();
</script>

<%}
    request.setAttribute("msg", msg);
    if (!QuickReview.equals("hiddensubmit") && request.getAttribute("msg") != null
            && request.getAttribute("msg").equals("BkgRates")) {%>
<script>
    parent.parent.GB_hide();
    alert('${ratesPopupList[6]}' + '==' + '${ratesPopupList[2]}');
    parent.parent.getConverttoBook('${ratesPopupList[6]}', '${ratesPopupList[2]}');
</script>
<%}%>
<%if (QuickReview.equals("QuickReview")) {%>

<html>

    <%} else {%>

    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
        <%}%>

        <%@include file="../includes/jspVariables.jsp"%>
        <head>

            <title>FCL Quotation Rates</title>

            <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
            <script type='text/javascript' src='/logisoft/dwr/util.js'></script>
            <script type='text/javascript'
            src='/logisoft/dwr/interface/PortsDAO.js'></script>
            <script type='text/javascript'
            src='/logisoft/dwr/interface/QuoteDwrBC.js'></script>
            <script type="text/javascript"
            src="<%=path%>/js/prototype/prototype.js"></script>
            <script type="text/javascript"
            src="<%=path%>/js/script.aculo.us/effects.js"></script>
            <script type="text/javascript"
            src="<%=path%>/js/script.aculo.us/controls.js"></script>
            <script language="javascript" src="<%=path%>/js/common.js"></script>
            <script language="javascript" src="<%=path%>/js/Script.js"></script>
            <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>

            <script type="text/javascript">
    dojo.hostenv.setModulePrefix('utils', 'utils');
    dojo.widget.manager.registerWidgetPackage('utils');
    dojo.require("utils.AutoComplete");
    dojo.require("dojo.io.*");
    dojo.require("dojo.event.*");
    dojo.require("dojo.html.*");
    start = function() {
        loadFunction();
    }
    dojo.addOnLoad(start);

            </script>
            <script language="javascript" type="text/javascript">
                function loadFunction() {
                    disableProgressBar();
                }
                function enableProgressBar() {
                    var cvr = document.getElementById("cover");
                    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
                        var pageWidth1 = document.body.scrollWidth + 'px';
                    } else if (document.body.offsetWidth) {
                        var pageWidth1 = document.body.offsetWidth + 'px';
                    } else {
                        var pageWidth1 = '100%';
                    }
                    cvr.style.width = pageWidth1;
                    cvr.style.height = '100%';
                    cvr.style.display = "block";
                    document.getElementById('newProgressBar').style.display = "block";
                }
                function disableProgressBar() {
                    document.getElementById("cover").style.display = "none";
                    document.getElementById('newProgressBar').style.display = "none";
                }
                function call() {
                    var disabledImageZone;
                    var cvr = document.getElementById("cover");
                    cvr.style.display = "block";
                    disabledImageZone = document.createElement('div');
                    disabledImageZone.style.position = "absolute";
                    disabledImageZone.style.zIndex = "1000";
                    disabledImageZone.style.left = "10%";
                    disabledImageZone.style.top = "10%";
                    disabledImageZone.style.right = "50%";
                    disabledImageZone.style.bottom = "60%";
                    var imageZone = document.createElement('img');
                    imageZone.setAttribute('src', "/logisoft/img/icons/ajax-loader.gif");
                    imageZone.style.position = "absolute";
                    imageZone.style.width = "100";
                    imageZone.style.height = "100";
                    disabledImageZone.appendChild(imageZone);
                    document.body.appendChild(disabledImageZone);
                    disabledImageZone.style.visibility = 'visible';

                }
                function call2() {
                    var disabledImageZone;
                    var cvr = document.getElementById("cover");
                    cvr.style.display = "none";
                    disabledImageZone = document.createElement('div');
                    disabledImageZone.style.position = "absolute";
                    disabledImageZone.style.zIndex = "1000";
                    disabledImageZone.style.left = "50%";
                    disabledImageZone.style.top = "40%";
                    disabledImageZone.style.right = "50%";
                    disabledImageZone.style.bottom = "60%";
                    var imageZone = document.createElement('img');
                    imageZone.setAttribute('src', "/logisoft/img/icons/ajax-loader.gif");
                    imageZone.style.position = "absolute";
                    imageZone.style.width = "100";
                    imageZone.style.height = "100";
                    disabledImageZone.appendChild(imageZone);
                    document.body.appendChild(disabledImageZone);
                    disabledImageZone.style.visibility = 'hidden';
                }
                function getrates() {
                    document.fclQuotesPopupForm.originId.value = "<%=originId%>";
                    document.fclQuotesPopupForm.portId.value = "<%=protsId%>";
                    document.fclQuotesPopupForm.commodId.value = "<%=commid%>";
                    document.fclQuotesPopupForm.pol.value = "<%=pol%>";
                    document.fclQuotesPopupForm.pod.value = "<%=pod%>";
                    document.fclQuotesPopupForm.rampCity.value = "<%=rampCity%>";
                    document.fclQuotesPopupForm.cargoOrigins.value = "<%=CargoOriginvalue%>";
                    document.fclQuotesPopupForm.destinationPort.value = "<%=destPortvalue%>";
                    document.fclQuotesPopupForm.commodityId.value = "<%=commIdvalue%>";
                    document.fclQuotesPopupForm.butval.value = "<%=buttonVal%>";

                    document.fclQuotesPopupForm.buttonValue.value = "getRates";
                    document.fclQuotesPopupForm.submit();
                }
                function selectedMenu(obj, val, val1) {
                    var sc = "";
                    var sci = "";
                    var i = 0;
                    if (val != 'getRatesBooking') {
                        if (document.fclQuotesPopupForm.unitwiseselect.length != undefined) {
                            for (i = 0; i < document.fclQuotesPopupForm.unitwiseselect.length; i++) {
                                var u = document.fclQuotesPopupForm.unitwiseselect[i].value;
                                if (document.fclQuotesPopupForm.unitwiseselect[i].checked) {
                                    sc = sc + u + ",";
                                }
                            }
                        } else {
                            sc = sc + 0 + ",";
                        }
                        if (sc == "") {
                            alertNew("Please Select atlest one Unit Type");
                            return;
                        }
                    }
                    if (val != 'getRatesBooking') {
                        var vlen = document.fclQuotesPopupForm.unitselected.length;
                        if (document.fclQuotesPopupForm.unitselected.length != undefined) {
                            for (var j = 0; j < document.fclQuotesPopupForm.unitselected.length; j++) {
                                if (document.fclQuotesPopupForm.unitselected[j].checked) {
                                    sci = sci + j;
                                }
                            }
                        } else {
                            if (document.fclQuotesPopupForm.unitselected.checked) {
                                sci = sci + 0;
                            }
                        }
                        if (sci == "") {
                            alertNew("Please Select Ssline");
                            return;
                        }
                        if (typeof vlen == "undefined") {
                            sci = "0";
                        }
                        var rowindex = obj.parentNode.parentNode.rowIndex;
                        var x = document.getElementById('fclratestable').rows[rowindex].cells;
                        var slno = Number(sci);
                        var ssline = "";

                        if (sci == '0') {
                            if (document.fclQuotesPopupForm.unitselected != undefined && document.fclQuotesPopupForm.unitselected.length > 0) {
                                ssline = document.fclQuotesPopupForm.unitselected[0].value;
                            } else {
                                ssline = document.fclQuotesPopupForm.unitselected.value;
                            }
                        } else {
                            ssline = document.fclQuotesPopupForm.unitselected[slno].value;
                        }
                        var tempArr = ssline.split("==");
                    }

                    if (val == 'getRatesBooking') {
                        document.fclQuotesPopupForm.ssline.value = '${carrier}';
                        document.fclQuotesPopupForm.commodityId.value = '${commodity}';
                    } else {
                        document.fclQuotesPopupForm.ssline.value = tempArr[0];
                        document.fclQuotesPopupForm.commodityId.value = tempArr[1];
                        document.fclQuotesPopupForm.unitselected.value = sci;
                    }

                    document.fclQuotesPopupForm.selectedCheck.value = sc;
                    document.fclQuotesPopupForm.butval.value = "<%=buttonVal%>";
                    if (val == 'getRatesBooking') {
                        if (val1 == 'yes') {
                            //----------------Event for appy new get rates....
                            if (parent.parent.document.searchQuotationform != undefined) {
                                document.getElementById('eventCode').value = '100002';
                            } else {
                                document.getElementById('eventCode').value = '100005';
                            }
                            alert("New Rates will be applied");
                            document.fclQuotesPopupForm.buttonValue.value = "newgetRatesBKG";
                            document.fclQuotesPopupForm.submit();
                        } else {
                            //----------------Event for appy old get rates....
                            if (parent.parent.document.searchQuotationform != undefined) {
                                document.getElementById('eventCode').value = '100003';
                            } else {
                                document.getElementById('eventCode').value = '100006';
                            }
                            document.fclQuotesPopupForm.buttonValue.value = "oldgetRatesBKG";
                            document.fclQuotesPopupForm.submit();
                        }
                    } else {
                        document.fclQuotesPopupForm.buttonValue.value = "newgetRates";
                        document.fclQuotesPopupForm.submit();
                    }
                }
                function load(val1, val2, val3, val4) {
                    if (val4 != 'QuickReview') {
                        document.getElementById('collapseRates').style.display = 'block';
                        document.getElementById('expandRates').style.visibility = 'hidden';
                        document.getElementById('bundle').style.display = 'none';
                        document.getElementById('ExpandButton').style.visibility = 'visible';
                        document.getElementById('CollapseButton').style.visibility = 'hidden';

                        if (val1 == 0) {
                            parent.parent.document.getElementById("message").innerHTML = val2;
                            parent.parent.document.getElementById("message").style.color = "Red";
                        }
                    }
                }
                function closePopUp() {
                    parent.parent.GB_hide();
                }
                function getExpandButton() {
                    document.getElementById('collapseRates').style.display = 'none';
                    document.getElementById('expandRates').style.visibility = 'hidden';
                    document.getElementById('bundle').style.display = 'block';
                    document.getElementById('ExpandButton').style.visibility = 'hidden';
                    document.getElementById('CollapseButton').style.visibility = 'visible';
                }
                function getExpand(val) {
                    document.getElementById('collapseRates').style.display = 'none';
                    document.getElementById('expandRates').style.visibility = 'visible';
                    document.getElementById('bundle').style.display = 'none';
                    if (document.getElementById('ExpandButton').style.visibility == 'hidden') {
                        document.getElementById('ExpandButton').style.visibility = 'hidden';
                        document.getElementById('CollapseButton').style.visibility = 'visible';
                    } else {
                        document.getElementById('ExpandButton').style.visibility = 'visible';
                        document.getElementById('CollapseButton').style.visibility = 'hidden';
                    }
                }
                function getCollapse() {
                    document.getElementById('collapseRates').style.display = 'block';
                    document.getElementById('expandRates').style.visibility = 'hidden';
                    document.getElementById('bundle').style.display = 'none';
                    document.getElementById('ExpandButton').style.visibility = 'visible';
                    document.getElementById('CollapseButton').style.visibility = 'hidden';
                }
                function load1(val1) {
                    enableProgressBar();
                    if (document.fclQuotesPopupForm.hazmat[1] != null && document.fclQuotesPopupForm.hazmat[1] != undefined) {
                        document.fclQuotesPopupForm.hazmat[1].checked = true;
                    }
                    if (val1 != 'QuickReview') {
                        setTimeout("set()", 150);
                    }
                }
                function set() {
                    if (document.getElementById('cargoOrigins') != null && document.getElementById('cargoOrigins') != undefined) {
                        document.getElementById('cargoOrigins').focus();
                    }
                }
                function popupAddRates(windows) {
                    if (trim(document.getElementById('portofDischarge').value) == '' && trim(document.getElementById('isTerminal').value) == '') {
                        alertNew("Please enter Destination and Origin fields");
                        return;
                    } else if (trim(document.getElementById('portofDischarge').value) == '') {
                        alertNew("Please enter Destination field");
                        return;
                    } else if (trim(document.getElementById('isTerminal').value) == '') {
                        alertNew("Please enter Origin field");
                        return;
                    }
                    if (document.getElementById('commcode').value == '') {
                        alertNew("Please enter Commodity field");
                        return;
                    }
                    document.getElementById('origin').value = document.fclQuotesPopupForm.isTerminal.value;
                    document.getElementById('destn').value = document.fclQuotesPopupForm.portofDischarge.value;
                    document.getElementById('comid').value = document.fclQuotesPopupForm.commcode.value;
                    document.fclQuotesPopupForm.buttonValue.value = "getRates";
                    var cvr = document.getElementById("cover");
                    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
                        var pageWidth1 = document.body.scrollWidth + 'px';
                    } else if (document.body.offsetWidth) {
                        var pageWidth1 = document.body.offsetWidth + 'px';
                    } else {
                        var pageWidth1 = '100%';
                    }
                    cvr.style.width = pageWidth1;
                    cvr.style.height = '100%';
                    cvr.style.display = "block";
                    document.getElementById('newProgressBar').style.display = "block";
                    document.fclQuotesPopupForm.submit();
                }
                function getTemp() {
                    var path = "";
                    if (document.getElementById('destinationCity').checked) {
                        path = "&nonRated="
                                + "no" + "&radio=city&origin=" + document.fclQuotesPopupForm.isTerminal.value;
                    } else {
                        path = "&nonRated="
                                + "no" + "&radio=country&origin=" + document.fclQuotesPopupForm.isTerminal.value;
                    }
                    appendEncodeUrl(path);
                }
                function getTemp1() {
                    var path = "";
                    if (document.getElementById('originCountry').checked) {
                        path = "&nonRated="
                                + "no" + "&radio=city&destination=" + document.fclQuotesPopupForm.portofDischarge.value;
                    } else {
                        path = "&nonRated="
                                + "no" + "&radio=country&destination=" + document.fclQuotesPopupForm.portofDischarge.value;
                    }
                    appendEncodeUrl(path);
                }

//---part of Confirm function of New Alertbox -----
                function confirmMessageFunction(id1, id2) {
                    if (id1 == 'getRates' && id2 == 'ok') {
                        document.fclQuotesPopupForm.buttonValue.value = "newgetRatesBKG";
                        document.fclQuotesPopupForm.submit();
                    } else if (id1 == 'getRates' && id2 == 'cancel') {
                        document.fclQuotesPopupForm.buttonValue.value = "oldgetRatesBKG";
                        document.fclQuotesPopupForm.submit();
                    }
                }
                function checkSsline() {

                    for (var i = 0; i < document.fclQuotesPopupForm.unitwiseselect.length; i++) {
                        document.fclQuotesPopupForm.unitwiseselect[i].checked = false;
                        document.fclQuotesPopupForm.unitwiseselect[i].disabled = false;

                    }
                    var displayFlag = false;
                    var rowCount = 0;
                    for (var i = 0; i < document.fclQuotesPopupForm.unitselected.length; i++) {
                        if (document.fclQuotesPopupForm.unitselected[i].checked) {
                            rowCount = i + 1;
                            displayFlag = true;
                        }
                    }
                    if (document.getElementById('collapseRates').style.display == 'block') {
                        if (displayFlag) {
                            var rowElements = document.getElementById('fclratestable').rows[rowCount].cells;
                            var unitNumber = 0;
                            for (var i = 5; i < rowElements.length; i++) {
                                var unitValue = rowElements[i].innerHTML;
                                if (unitValue != '') {
                                    document.fclQuotesPopupForm.unitwiseselect[unitNumber].checked = true;
                                } else {
                                    document.fclQuotesPopupForm.unitwiseselect[unitNumber].disabled = true;
                                }
                                unitNumber++;
                            }
                        } else {
                            // need to cun
                            for (var i = 0; i < document.fclQuotesPopupForm.unitwiseselect.length; i++) {
                                document.fclQuotesPopupForm.unitwiseselect[i].checked = false;
                                document.fclQuotesPopupForm.unitwiseselect[i].disabled = false;

                            }
                        }

                    }

                }
                function restart() {
                    document.fclQuotesPopupForm.buttonValue.value = "restart";
                    document.fclQuotesPopupForm.submit();
                }
//this is for port remarks-----
                function showSpecialRemarks() {
                    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
                        document.getElementById('isTerminal').select();
                        var city = document.getElementById('portofDischarge').value;
                        var index = city.indexOf('/');
                        var cityName = city.substring(0, index);
                        PortsDAO.getSpecialRemarks(cityName, openDiv);
                    }
                }
                function getRemarksonComparision(val, buttonValue, button) {
                    if (buttonValue != 'QuickReview' && button != 'getRatesBooking') {
                        var index = val.indexOf('/');
                        var cityName = val.substring(index + 1, val.length);
                        PortsDAO.getSpecialRemarks(cityName, openDiv);
                    }
                }
                function openDiv(data) {
                    var data1;
                    if (data != "") {
                        data1 = trim(data);
                    }
                    if (data1 != "" && data1 != undefined) {
                        document.getElementById('portRemarks').style.display = 'block';
                        document.getElementById('remark').value = data1;
                    } else {
                        document.getElementById('portRemarks').style.display = 'none';
                        document.getElementById('remark').value = "";
                    }
                }
                function showSpecialRemarksForBooking(city) {

                    var index = city.indexOf('/');
                    var cityName = city.substring(index + 1, city.length);
                    PortsDAO.getSpecialRemarks(cityName, openDiv1);
                }

                function openDiv1(data) {
                    var data1;
                    if (data != "") {
                        data1 = trim(data);
                    }
                    if (data1 != "" && data1 != undefined) {
                        document.getElementById('portRemarksForBooking').style.display = 'block';
                        document.getElementById('remarkForBooking').value = data1;
                    } else {
                        document.getElementById('portRemarksForBooking').style.display = 'none';
                        document.getElementById('remarkForBooking').value = "";
                    }
                }
                function trim(stringToTrim) {
                    return stringToTrim.replace(/^\s+|\s+$/g, "");
                }

//end of port remarks
            </script>

            <link rel="stylesheet" href="<%=path%>/css/default/style.css"
                  type="text/css" media="print, projection, screen" />
            <link rel="alternate stylesheet" type="text/css" media="all"
                  href="<%=path%>/css/cal/calendar-win2k-cold-2.css"
                  title="win2k-cold-2" />
            <link rel="stylesheet" type="text/css"
                  href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
            <link rel="stylesheet" type="text/css" href="<%=path%>/css/sweetTitles.css" />


        </head>
        <div id="cover" style="width: 906px "></div>

        <div id="newProgressBar" class="progressBar"
             style="position: absolute;z-index: 
             100;left:35% ;top: 40%;right: 50%;">
            <p class="progressBarHeader" style="">
                <b>Processing......Please Wait</b>
            </p>
            <form
                style="text-align:center;padding-right:4px;padding-bottom: 4px;padding-top: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif">
            </form>
        </div>

        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader">
                <b>Alert</b>
            </p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');" />
            </form>
        </div>

        <div id="ConfirmBox" class="alert">
            <p class="alertHeader">
                <b>Confirmation</b>
            </p>
            <p id="innerText1" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" class="buttonStyleForAlert" value="OK"
                       onclick="yes()" />
                <input type="button" class="buttonStyleForAlert" value="Cancel"
                       onclick="No()" />
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <body class="whitebackgrnd">
            <html:form action="/fclQuotes" name="fclQuotesPopupForm"
                       type="com.gp.cvst.logisoft.struts.form.fclQuotesPopupForm" scope="request">

                <%if (rateslist.size() > 0) {%>
                <font color="red" size="4"><%=msg%>
                </font>
                <%} else {%>
                <font color="red" size="4"><%=msg%>
                </font>
                <%} %>

                <%if (QuickReview.equals("QuickReview")) {%>
                <table width="100%" border="0" cellpadding="2" cellspacing="0"
                       class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        Rates Quick Review
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            <table border="0" width="100%" cellpadding="3">
                                <tr class="textlabelsBold">
                                    <td>
                                        Destination
                                    </td>
                                    <td>
                                        Origin
                                    </td>
                                    <td>
                                        Commodity
                                    </td>
                                    <td>
                                        Hazmat
                                    </td>
                                </tr>

                                <tr class="textlabelsBold">
                                    <td>
                                        <input Class="textlabelsBoldForTextBox" name="portofDischarge" id="portofDischarge" size="22"
                                               onkeydown="getTemp()"/>
                                        <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initOPSAutocomplete("portofDischarge", "destination_port_choices", "", "",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7", "");
                                        </script>
                                        <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <input type="checkbox" id="destinationCity" checked="checked"/></span>
                                    </td>
                                    <td id="isTerminal1">
                                        <input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox"
                                               size="22" onkeydown="getTemp1()"/>
                                        <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <input type="checkbox" checked="checked" id="originCountry"/>   </span>
                                        <div id="isTerminal_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initOPSAutocomplete("isTerminal", "isTerminal_choices", "", "",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&typeOfmove=&isDojo=false", "");
                                        </script>
                                    </td>
                                    <td>
                                        <input Class="textlabelsBoldForTextBox" name="commcode"
                                               id="commcode"  size="22" value="<%=comCode%>"  maxlength="6"/>
                                        <input id="commcode_check" type="hidden" value="<%=comCode%>"/>
                                        <div id="commcode_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocomplete("commcode", "commcode_choices", "", "commcode_check",
                                                    "<%=path%>/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false", "");
                                        </script>

                                    </td>
                                    <td>
                                        <html:radio property="hazmat" value="Y" />
                                        Y
                                        <html:radio property="hazmat" value="N" />
                                        N
                                    </td>
                                    <td>
                                        <input type="button" id="getRates"
                                               onClick="return popupAddRates('windows')" Value="Rates"
                                               style="width: 52px;" class="buttonStyleNew" />
                                    </td>

                                </tr>
                                <tr>
                                    <td id="portRemarks" style="display: none;" colspan="3">
                                        <table>
                                            <tr class="textlabelsBold">
                                                <td valign="top">
                                                    Port Remarks
                                                </td>
                                                <td>
                                                    <textarea rows="4" cols="100" id="remark" name="remark"
                                                              readonly="true" class="textlabelsBoldForTextBox"></textarea>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <%} else { %>

                <table>
                    <tr class="textlabels">
                        <td id="portRemarks" style="display: none;" colspan="3">
                            <table>
                                <tr class="textlabelsBold">
                                    <td valign="top">
                                        Port Remarks
                                    </td>
                                    <td>
                                        <textarea rows="4" cols="100" id="remark" name="remark"
                                                  readonly="true" class="textlabelsBoldForTextBox"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <%if (buttonValue.equals("getRatesBooking")) { %>
                        <td class="headerbluelarge">
                            Rate Change Alert
                        </td>
                        <td id="portRemarksForBooking" style="display: none;">
                            <table>
                                <tr class="textlabelsBold">
                                    <td valign="top">
                                        Port Remarks
                                    </td>
                                    <td>
                                        <textarea rows="4" cols="100" id="remarkForBooking"
                                                  name="remarkForBooking" readonly="true"
                                                  class="textlabelsBoldForTextBox"></textarea>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="right">
                            <table>
                                <tr>
                                    <td class="headerbluelarge" align="right" colspan="2">
                                        Apply New Rates
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <input type="button" value="Yes" class="buttonStyleNew"
                                               onclick="selectedMenu(this, '${buttonValue}', 'yes')" />
                                    </td>
                                    <td align="right">
                                        <input type="button" value="No" class="buttonStyleNew"
                                               onclick="selectedMenu(this, '${buttonValue}', 'no')" />
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <%} else { %>
                        <td class="headerbluelarge"><!--
                                FCL Rates Comparison Grid
                            --></td>
                            <%}%>
                    </tr>
                    <tr>
                        <td></td>
                    </tr>
                </table>


                <table width="501" border="0" cellpadding="0" cellspacing="0">
                    <tr class="textlabels">
                        <td>
                            File No:
                            <b style="color:red;"><c:out value="${fileNo}"></c:out>
                                </b>
                            </td>
                        <%if (buttonValue.equals("getRatesBooking")) { %>
                        <td>
                            <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="quoteDate1"
                                            value="${quoteDate}" />
                            Quote created on:
                            <b style="color:red;"><c:out value="${quoteDate1}"></c:out>
                                </b>
                            </td>
                        <%}%>
                    </tr>
                    <tr class="textlabels">
                        <td>
                            Origin :&nbsp;&nbsp;
                            <b><c:out value="<%=CargoOriginvalue%>" />
                            </b>
                        </td>
                        <td class="textlabels">
                            &nbsp;&nbsp;&nbsp;&nbsp;Destination:&nbsp;&nbsp;
                            <b><c:out value="${destination}"></c:out>
                                </b>
                            </td>
                            <td class="textlabels">
                                &nbsp;&nbsp;&nbsp;&nbsp;Commodity:&nbsp;&nbsp;
                                <b><c:out value="${commodity}"></c:out>
                                </b>
                            </td>
                        <%if (buttonValue.equals("getRatesBooking")) { %>
                        <td class="textlabels">
                            &nbsp;&nbsp;&nbsp;&nbsp;Carrier:&nbsp;&nbsp;
                            <b><c:out value="${carrier}"></c:out>
                                </b>
                            </td>
                        <%}%>
                    </tr>
                </table>

                <table>
                    <tr>
                        <tr></tr>
                </table>

                <table>
                    <tr></tr>
                    <tr class="textlabels">
                        <td>
                            <%if (!QuickReview.equals("hiddensubmit") && !buttonValue.equals("getRatesBooking")) { %>
                            <input type="button" value="Submit" onclick="selectedMenu(this, '${buttonValue}', '')"
                                   class="buttonStyleNew" />
                            <%}%>
                        </td>
                        <%
                    if (userid != null && hasUserLevelAccess) {%>
                        <td>
                            <img src="<%=path%>/img/icons/up.gif" border="0"
                                 onclick="getExpand('<%=buttonValue%>')" />
                        </td>
                        <%} %>
                        <td>
                            <input type="button" name="Expand" onclick="getExpandButton()"
                                   value="Expand" id="ExpandButton" class="buttonStyleNew">
                        </td>
                        <td>
                            <input type="button" name="Cancel" onclick="closePopUp()"
                                   value="Cancel" id="cancelButton" class="buttonStyleNew">
                        </td>
                        <td>
                            <input type="button" name="Collapse" onclick="getCollapse()"
                                   value="Collapse" id="CollapseButton" class="buttonStyleNew">
                        </td>
                        <%if (QuickReview.equals("hiddensubmit")) { %>
                        <td>
                            <input type="button" name="Restart" onclick="restart()"
                                   value="Restart" class="buttonStyleNew">
                        </td>
                        <%} %>
                    </tr>
                </table>

                <table width="100%" border="0" cellpadding="0" cellspacing="0"
                       class="displaytagstyleNew">
                    <tr>
                        <td id="collapseRates">
                            <%int k = 0;
                                FclBuyCost cb = new FclBuyCost();
                                String chargeCode = "";
                                String ssline = "";
                                String temp = "";
                                String retail = "";
                                String futureRetail = "";
                                String transitTime = "";
                                String commodity = "";
                                String tempCheck = "";
                                String oldAmount = "";
                                String effetciveDate = "";
                                String retailEffectiveDate = "";
                                String tempSsline = "";
                                String orgDestRemarks = "";
                                String portOfExit = "";
                                if (collapseList.size() > 0 && collapseList != null) {
                                    int rsize = collapseList.size();
                            %>
                            <display:table name="${collapseList}" pagesize="500"
                                           class="displaytagstyle" id="fclratestable" sort="list"
                                           style="width:60%">
                                <%
                                    cb = (FclBuyCost) collapseList.get(k);
                                    if (cb.getCommodityCodeDesc() != null) {
                                        commodity = cb.getCommodityCodeDesc();
                                    }
                                    if (cb.getOrgDestCarrierRemarks() != null) {
                                        orgDestRemarks = cb.getOrgDestCarrierRemarks().replace("\"", "");
                                    } else {
                                        orgDestRemarks = "";
                                    }
                                    if (cb.getSsLineName() != null) {
                                        ssline = (String) cb.getSsLineName().getAccountName() + "//" + (String) cb.getSsLineName().getAccountno();
                                        tempSsline = (String) cb.getSsLineName().getAccountName();
                                    }
                                    tempCheck = ssline + "==" + commodity;
                                    if (cb.getRetail() != null) {
                                        retail = numb.format(cb.getRetail());
                                    } else {
                                        retail = "";
                                    }
                                    if (cb.getCostCode() != null) {
                                        chargeCode = cb.getCostCode();
                                    }
                                    if (cb.getFutureRetail() != null) {
                                        futureRetail = numb.format(cb.getFutureRetail());
                                    }
                                    if (cb.getEffectiveDate() != null) {
                                        retailEffectiveDate = sdf.format(cb.getEffectiveDate());
                                    } else {
                                        retailEffectiveDate = "";
                                    }
                                    if (null != cb.getPortOfExit()) {
                                        portOfExit = cb.getPortOfExit();
                                    } else {
                                        portOfExit = "";
                                    }

                                %>

                                <%if (!ssline.equals(temp)) { %>
                                <%if (!buttonValue.equals("getRatesBooking")) { %>
                                <display:column title="">
                                    <%if (!orgDestRemarks.equals("")) {%>
                                    <a style="cursor: hand;" title="<%=orgDestRemarks.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                        <font  class="destinationMarks">*</font>
                                    </a>
                                    <%}%>
                                </display:column>
                                <display:column title="SSLine" style="text-align:right;">
                                    <c:out value="<%=tempSsline%>" />

                                    <%if (!QuickReview.equals("hiddensubmit")) {%>
                                    <html:radio property="unitselected" value="<%=tempCheck%>"
                                                onclick="checkSsline()" />
                                    <%} %>
                                </display:column>

                                <display:column title="Transit Days" style="text-align: center;" property="transitTime"></display:column>
                                <%
                                                            if (null != portOfExit && portOfExit.equals("")) {%>
                                <display:column title="POE" style="text-align: center;" ></display:column>
                                <%} else {%>
                                <display:column title="POE" style="text-align: center;" >
                                    <a style="cursor: hand;" title="<%=portOfExit.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                        <font  class="destinationMarks"><%="*"%></font>
                                        </display:column>
                                        <%}%>

                                    <%} %>
                                    <%} else {
                                    %>
                                    <%if (!buttonValue.equals("getRatesBooking")) {%>
                                    <display:column title=""></display:column>
                                    <display:column title="SSLine"></display:column>

                                    <display:column title="Transit Days" style="text-align: center;">
                                        <%=transitTime%>
                                    </display:column>
                                    <%
                                                            if (null != portOfExit && portOfExit.equals("")) {%>
                                    <display:column title="Port of Exit" style="text-align: center;" ></display:column>
                                    <%} else {%>
                                    <display:column title="POE" style="text-align: center;" >
                                        <a style="cursor: hand;" title="<%=portOfExit.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                            <font  class="destinationMarks"><%="*"%></font>
                                            </display:column><%}%><%} %>
                                            <%}%>
                                            <display:column title="ChargeCode">
                                                <%=chargeCode%>
                                            </display:column>
                                            <%
                                                for (int j = 0; j < cb.getUnitTypeList().size(); j++) {

                                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) cb.getUnitTypeList().get(j);
                                                    request.setAttribute("title", fclBuyCostTypeRates.getUnitname());
                                                    String amount = "", markup = "";
                                                    oldAmount = "";
                                                    effetciveDate = "";
                                                    if (fclBuyCostTypeRates.getActiveAmt() != null) {
                                                        amount = numb.format(fclBuyCostTypeRates.getActiveAmt());
                                                    }
                                                    if (fclBuyCostTypeRates.getOldAmount() != null) {
                                                        oldAmount = numb.format(fclBuyCostTypeRates.getOldAmount());
                                                    }
                                                    if (fclBuyCostTypeRates.getMarkup() != null) {
                                                        markup = numb.format(fclBuyCostTypeRates.getMarkup());
                                                    }
                                                    if (fclBuyCostTypeRates.getEffectiveDate() != null) {
                                                        effetciveDate = sdf.format(fclBuyCostTypeRates.getEffectiveDate());
                                                    }
                                            %>
                                            <%if (!QuickReview.equals("hiddensubmit") && !buttonValue.equals("getRatesBooking")) {%>

                                        <display:column
                                            title="${title}<input type=checkbox onmouseover=setCursor(this)  onclick=setCheckBox(this) name=unitwiseselect value='${title}'>">
                                            <%=amount%>
                                        </display:column>

                                        <%} else if (buttonValue.equals("getRatesBooking")) {
                                        %>

                                        <display:column title="Quoted Rate">
                                            <%=!"0.00".equals(oldAmount) ? oldAmount : ""%>
                                        </display:column>

                                        <display:column style="color:red" title="New Rate ${title}">
                                            <%=!"0.00".equals(amount) ? amount : ""%>
                                        </display:column>
                                        <display:column title="EffectiveDate">
                                            <%=effetciveDate%>
                                        </display:column>
                                        <%} else {%>
                                        <display:column title="${title}">
                                            <%=amount%>
                                        </display:column>
                                        <%} %>

                                        <%
                                                temp = ssline;
                                                rsize--;
                                                            }%>
                                        <%  k++;
                                            rsize--;
                                        %>
                                    </display:table>
                                    <%} %>
                                    </td>
                                    </tr>
                                    <tr>
                                        <td id="bundle">
                                            <%k = 0;
                                                cb = new FclBuyCost();
                                                chargeCode = "";
                                                ssline = "";
                                                temp = "";
                                                retail = "";
                                                futureRetail = "";
                                                transitTime = "";
                                                commodity = "";
                                                tempCheck = "";
                                                tempSsline = "";
                                                orgDestRemarks = "";
                                                String portOfExit2 = "";
                                                if (compressList.size() > 0 && compressList != null) {
                                                    int rsize = compressList.size();
                                            %>
                                            <display:table name="${compressList}" pagesize="500"
                                                           class="displaytagstyle" id="fclratestable" sort="list"
                                                           style="width:60%">
                                                <%
                                                    cb = (FclBuyCost) compressList.get(k);
                                                    if (cb.getCommodityCodeDesc() != null) {
                                                        commodity = cb.getCommodityCodeDesc();
                                                    }
                                                    if (null != cb.getPortOfExit()) {
                                                        portOfExit2 = cb.getPortOfExit();
                                                    } else {
                                                        portOfExit2 = "";
                                                    }
                                                    if (cb.getSsLineName() != null) {
                                                        ssline = (String) cb.getSsLineName().getAccountName() + "//" + (String) cb.getSsLineName().getAccountno();
                                                        tempSsline = (String) cb.getSsLineName().getAccountName();
                                                    }
                                                    tempCheck = ssline + "==" + commodity;
                                                    if (cb.getRetail() != null) {
                                                        retail = numb.format(cb.getRetail());
                                                    } else {
                                                        retail = "";
                                                    }
                                                    if (cb.getCostCode() != null) {
                                                        chargeCode = cb.getCostCode();
                                                    }
                                                    if (cb.getOrgDestCarrierRemarks() != null) {
                                                        orgDestRemarks = cb.getOrgDestCarrierRemarks().replace("\"", "");
                                                    } else {
                                                        orgDestRemarks = "";
                                                    }
                                                %>
                                                <%if (cb.getTempRate() != null && cb.getTempRate().equals("A")) {%>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <% for (int j = 0; j < cb.getUnitTypeList().size(); j++) {  %>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                <%}%>
                                                <%} else { %>
                                                <%if (!buttonValue.equals("getRatesBooking")) { %>
                                                <%if (!ssline.equals(temp)) { %>

                                                <display:column title="">
                                                    <%if (!orgDestRemarks.equals("")) {%>
                                                    <a style="cursor: hand;" title="<%=orgDestRemarks.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                                        <font  class="destinationMarks"><%="*"%></font>
                                                    </a>
                                                    <%}%>
                                                </display:column>

                                                <display:column title="SSLine" style="text-align:right;">
                                                    <c:out value="<%=tempSsline%>" />
                                                    <%if (!QuickReview.equals("hiddensubmit")) {%>
                                                    <html:radio property="unitselected" value="<%=tempCheck%>"
                                                                onclick="checkSsline()" />
                                                    <%} %>
                                                </display:column>
                                                <display:column title="Transit Days" style="text-align: center;" property="transitTime"></display:column>
                                                <display:column title="POE" style="text-align:center;">
                                                    <%if (!portOfExit2.equals("")) {%>
                                                    <a style="cursor: hand;" title="<%=portOfExit2.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                                        <font  class="destinationMarks"><%="*"%></font>
                                                            <%}%>
                                                        </display:column>
                                                        <%} else {
                                                        %>
                                                        <display:column></display:column>
                                                        <display:column title="SSLine"></display:column>
                                                        <display:column title="Transit Days" style="text-align: center;">
                                                            <%=transitTime%>
                                                        </display:column>
                                                        <display:column></display:column>
                                                        <%} %>
                                                        <%}%>
                                                        <display:column title="ChargeCode">
                                                            <%=chargeCode%>
                                                        </display:column>
                                                        <display:column style="text-align:right;color:red">
                                                            <%=futureRetail%>
                                                        </display:column>
                                                        <%
                                                            for (int j = 0; j < cb.getUnitTypeList().size(); j++) {
                                                                FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) cb.getUnitTypeList().get(j);
                                                                request.setAttribute("title", fclBuyCostTypeRates.getUnitname());
                                                                String amount = "", markup = "";
                                                                oldAmount = "";
                                                                effetciveDate = "";
                                                                if (fclBuyCostTypeRates.getActiveAmt() != null) {
                                                                    amount = numb.format(fclBuyCostTypeRates.getActiveAmt());
                                                                }
                                                                if (fclBuyCostTypeRates.getOldAmount() != null) {
                                                                    oldAmount = numb.format(fclBuyCostTypeRates.getOldAmount());
                                                                }
                                                                if (fclBuyCostTypeRates.getMarkup() != null) {
                                                                    markup = numb.format(fclBuyCostTypeRates.getMarkup());
                                                                }
                                                                if (fclBuyCostTypeRates.getEffectiveDate() != null) {
                                                                    effetciveDate = sdf.format(fclBuyCostTypeRates.getEffectiveDate());
                                                                }
                                                        %>
                                                        <%if (!QuickReview.equals("hiddensubmit") && !buttonValue.equals("getRatesBooking")) {%>
                                                        <display:column
                                                            title="${title}<input type=checkbox  name=unitwiseselect value='${title}'>"
                                                            class="borderStyleRight">

                                                        <%=amount%>
                                                    </display:column>
                                                    <%} else if (buttonValue.equals("getRatesBooking")) {%>

                                                    <display:column title="Quoted Rate">
                                                        <%=!"0.00".equals(oldAmount) ? oldAmount : ""%>
                                                    </display:column>

                                                    <display:column style="color:red" title="New Rate ${title}">
                                                        <%=!"0.00".equals(amount) ? amount : ""%>
                                                    </display:column>
                                                    <display:column title="EffectiveDate">
                                                        <%=effetciveDate%>
                                                    </display:column>
                                                    <%} else {%>
                                                    <display:column title="${title}" class="borderStyleRight">

                                                        <%=amount%>
                                                    </display:column>
                                                    <%} %>
                                                    <%
                                                            temp = ssline;
                                                            rsize--;
                                                            }%>
                                                    <%} %>
                                                    <%  k++;
                                                        rsize--;
                                                    %>
                                                </display:table>
                                                <%} %>
                                        </td>
                                    </tr>
                                    </table>
                                    <!-- We have Applied script.js for the below table to freez first 3 coloumns scrolling horizontally -->
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td id="expandRates" height="100%" >
                                                <%	k = 0;
                                                    cb = new FclBuyCost();
                                                    chargeCode = "";
                                                    ssline = "";
                                                    tempSsline = "";
                                                    temp = "";
                                                    retail = "";
                                                    futureRetail = "";
                                                    transitTime = "";
                                                    commodity = "";
                                                    tempCheck = "";
                                                    if (rateslist.size() > 0 && rateslist != null) {
                                                        int rsize = rateslist.size();
                                                        String transitDays = "";
                                                        orgDestRemarks = "";
                                                        String portOfExit1 = "";
                                                %>
                                                <div id="outerDiv">
                                                    <div id="rateListTable" style="height: 100%">
                                                        <display:table name="${rateslist}" pagesize="500" class="displaytagstyle" id="fclratestable"		sort="list" style="height:100%">
                                                            <display:setProperty name="paging.banner.some_items_found">
                                                                <span class="pagebanner"><font color="blue">{0}</font>
                                                                    Search Rates details displayed,For more code click on page
                                                                    numbers.
                                                                </span>
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.one_item_found">
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.all_items_found">
                                                                <span class="pagebanner">{0} {1} Displayed, Page Number</span>
                                                            </display:setProperty>
                                                            <display:setProperty name="basic.msg.empty_list">
                                                                <span class="pagebanner">No Records Found.</span>
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.placement" value="" />
                                                            <display:setProperty name="paging.banner.item_name" value="" />
                                                            <display:setProperty name="paging.banner.items_name" value="" />
                                                            <%
                                                                cb = (FclBuyCost) rateslist.get(k);
                                                                if (cb.getCommodityCodeDesc() != null) {
                                                                    commodity = cb.getCommodityCodeDesc();
                                                                }
                                                                if (cb.getSsLineName() != null) {
                                                                    ssline = (String) cb.getSsLineName().getAccountName() + "//" + (String) cb.getSsLineName().
                                                                            getAccountno();
                                                                    tempSsline = (String) cb.getSsLineName().getAccountName();
                                                                }
                                                                tempCheck = ssline + "==" + commodity;
                                                                if (cb.getRetail() != null) {
                                                                    retail = numb.format(cb.getRetail());
                                                                } else {
                                                                    retail = "";
                                                                }
                                                                if (cb.getCostCode() != null) {
                                                                    chargeCode = cb.getCostCode();
                                                                }
                                                                if (cb.getTransitTime() != null) {
                                                                    transitDays = cb.getTransitTime();
                                                                }
                                                                if (cb.getOrgDestCarrierRemarks() != null) {
                                                                    orgDestRemarks = cb.getOrgDestCarrierRemarks().replace("\"", "");
                                                                } else {
                                                                    orgDestRemarks = "";
                                                                }
                                                                if (null != cb.getPortOfExit()) {
                                                                    portOfExit1 = cb.getPortOfExit();
                                                                } else {
                                                                    portOfExit1 = "";
                                                                }
                                                                if (cb.getTempRate() != null && cb.getTempRate().equals("A")) {
                                                            %>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <%
                                                                for (int j = 0; j < cb.getUnitTypeList().size(); j++) {
                                                            %>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <display:column class="borderStyleBottom">&nbsp;</display:column>
                                                            <%
                                                                }
                                                            } else {
                                                                if (!ssline.equals(temp)) {
                                                                    if (!buttonValue.equals("getRatesBooking")) {
                                                            %>
                                                            <display:column title="
                                                                            <input type=text value=SSline readOnly=readOnly  size =15
                                                                            class=staticHeader style=colour:white;background-color:transparent;border:0px;>
                                                                            <input type=text value=TransitDays  size=10 readOnly=readOnly class=staticHeader  
                                                                            style=colour:white;background-color:transparent;border:0px;text-align:center;>

                                                                            <input type=text value=POE  size=10 readOnly=readOnly class=staticHeader  
                                                                            style=colour:white;background-color:transparent;border:0px;text-align:center;>

                                                                            <input type=text value=ChargeCode  size=20 readOnly=readOnly class=staticHeader  
                                                                            style=colour:white;background-color:transparent;border:0px;>">
                                                                <b style="color:Blue;">
                                                                    <%
                                                                        if (!orgDestRemarks.equals("")) {
                                                                    %>
                                                                    <a  style=cursor:hand title="<%=orgDestRemarks.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                                                        <font  class="destinationMarks"><%="*"%></font>
                                                                    </a>
                                                                    <%
                                                                        }
                                                                    %>
                                                                    <input type="text" readonly="readonly" name="chg" size="15"
                                                                           style="border:0px;background: transparent;font-size:9px;"
                                                                           value="<%=tempSsline%>" /> 
                                                                    <%
                                                                        if (!QuickReview.equals("hiddensubmit")) {
                                                                    %>
                                                                    <html:radio property="unitselected" value="<%=tempCheck%>"
                                                                                onclick="checkSsline()" /> 
                                                                    <%					}
                                                                    %> 				  <input type="text"
                                                                           readonly="readonly" name="chg" value="<%=transitDays%>"
                                                                           size="10" style="text-align: center;border:0px;padding-left: 60px;background: transparent;font-size:10px;" />
                                                                    <% if (null != portOfExit1 && !portOfExit1.equals("")) {
                                                                    %>
                                                                    <a  style=cursor:hand title="<%=portOfExit1.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n", "<br>")%>">
                                                                        <font style="padding-left: 50px;" class="destinationMarks"><%="*"%></font>
                                                                    </a>
                                                                    <input type="text" readonly="readonly"   name="chg" value="<%=chargeCode%>" size="20"
                                                                           style="font-size:10px;padding-left: 54px;background: transparent;border: 0;" />
                                                                    <%
                                                        } else if (!orgDestRemarks.equals("")) {%>
                                                                    <input type="text" readonly="readonly"   name="chg" value="<%=chargeCode%>" size="20"
                                                                           style="font-size:10px;text-align: center;padding-left: 85px;background: transparent;border: 0;" />
                                                                    <%} else {%>
                                                                    <input type="text" readonly="readonly"   name="chg" value="<%=chargeCode%>" size="20"
                                                                           style="font-size:10px;text-align: center;padding-left: 41px;background: transparent;border: 0;" />
                                                                    <%}%>

                                                                </b>
                                                            </display:column>
                                                            <%
                                                                }
                                                            %>
                                                            <%
                                                                if (buttonValue.equals("getRatesBooking")) {
                                                            %>
                                                            <display:column title="<input type=text value=ChargeCode  size=20 readOnly=readOnly		class=staticHeader  style=colour:white;background-color:transparent;border:0px;>">
                                                                <b><input type="text" readonly="readonly" name="chg"
                                                                          value="<%=chargeCode%>" size="20"
                                                                          style="border:0px;background:transparent;font-size:10px;" />
                                                                </b>
                                                            </display:column>
                                                            <%
                                                                }
                                                            } else {
                                                                if (!buttonValue.equals("getRatesBooking")) {
                                                            %>
                                                            <display:column title="
                                                                            <input type=text value=SSLine readOnly=readOnly  size =15	class=staticHeader  
                                                                            style=colour:white;border:0px;background: transparent;>	
                                                                            <input type=text value=TransitDays size=10 readOnly=readOnly
                                                                            class=staticHeader style=text-align:center;colour:white;border:0px;background:transparent;>
                                                                            <input type=text value=ChargeCode size=20 readOnly=readOnly class=staticHeader			        style=colour:white;border:0px;background:transparent;>">
                                                                <input type="text" readonly="readonly" name="chg" size="15"
                                                                       style="border:0px;background: transparent;" />
                                                                <input type="text" readonly="readonly" name="chg" size="10"
                                                                       style="border:0px;background: transparent;" />
                                                                <input type="text" readonly="readonly" name="chg" size="10"
                                                                       style="border:0px;background: transparent;width: 14%" />
                                                                <input type="text" readonly="readonly" name="chg"
                                                                       value="<%=chargeCode%>" size="20"
                                                                       style="border:0px;background:transparent;font-size:10px;text-align: left;padding-left: 80px;" />
                                                            </display:column>
                                                            <%
                                                                }
                                                                if (buttonValue.equals("getRatesBooking")) {
                                                            %>
                                                            <display:column title="<input type=text value=ChargeCode  size =20 readOnly=readOnly class=staticHeader style=colour:white;border:0px;background: transparent;>">
                                                                <input type="text" readonly="readonly" name="chg"
                                                                       value="<%=chargeCode%>" size="20"
                                                                       style="border:0px;background:transparent;font-size:10px;" />
                                                            </display:column>
                                                            <%
                                                                    }
                                                                }
                                                                for (int j = 0; j < cb.getUnitTypeList().size(); j++) {
                                                                    FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) cb.getUnitTypeList().get(j);
                                                                    request.setAttribute("title", fclBuyCostTypeRates.getUnitname());
                                                                    Double total = 0.00;
                                                                    String total1 = "", markUp = "";
                                                                    oldAmount = "";
                                                                    effetciveDate = "";
                                                                    if (fclBuyCostTypeRates.getMarkup() != null) {
                                                                        markUp = numb.format(fclBuyCostTypeRates.getMarkup());
                                                                    }
                                                                    if (markUp.contains(",")) {
                                                                        markUp = markUp.replace(",", "");
                                                                    }
                                                                    String amount = "";
                                                                    if (fclBuyCostTypeRates.getActiveAmt() != null) {
                                                                        amount = numb.format(fclBuyCostTypeRates.getActiveAmt());
                                                                    }
                                                                    if (amount.contains(",")) {
                                                                        amount = amount.replace(",", "");
                                                                    }
                                                                    if (!amount.trim().equals("") || !markUp.trim().equals("")) {
                                                                        total = Double.parseDouble(amount) + Double.parseDouble(markUp);
                                                                        total1 = numb.format(total);
                                                                    }
                                                                    if (fclBuyCostTypeRates.getOldAmount() != null) {
                                                                        oldAmount = numb.format(fclBuyCostTypeRates.getOldAmount());
                                                                    }
                                                                    if (fclBuyCostTypeRates.getEffectiveDate() != null) {
                                                                        effetciveDate = sdf.format(fclBuyCostTypeRates.getEffectiveDate());
                                                                    }
                                                                    if (!QuickReview.equals("hiddensubmit") && !buttonValue.equals("getRatesBooking")) {
                                                            %>
                                                            <display:column title="${title}<input type=checkbox name=unitwiseselect value='${title}'>">
                                                                <%=amount%>
                                                            </display:column>
                                                            <display:column title="MarkUp" style="text-align:right;"> 
                                                                <%=markUp%>
                                                            </display:column>
                                                            <display:column title="Total" class="borderStyleRight" style="text-align:right;">
                                                                <%=total1%>
                                                            </display:column>
                                                            <%
                                                            } else if (buttonValue.equals("getRatesBooking")) {
                                                            %>

                                                            <display:column title="Quoted Rate">
                                                                <%=!"0.00".equals(oldAmount) ? oldAmount : ""%>
                                                            </display:column>
                                                            <display:column style="color:red" title="New Rate ${title}">
                                                                <%=!"0.00".equals(amount) ? amount : ""%>
                                                            </display:column>
                                                            <display:column title="MarkUp"> 
                                                                <%=!"0.00".equals(markUp) ? markUp : ""%>
                                                            </display:column>
                                                            <display:column title="Total">
                                                                <%=!"0.00".equals(total1) ? total1 : ""%>
                                                            </display:column>
                                                            <display:column title="EffectiveDate">
                                                                <%=effetciveDate%>
                                                            </display:column>
                                                            <%
                                                            } else {
                                                            %>
                                                            <display:column title="${title}">
                                                                <%=amount%>
                                                            </display:column>
                                                            <display:column title="MarkUp" style="text-align:right;">
                                                                <%=markUp%>
                                                            </display:column>
                                                            <display:column title="Total" class="borderStyleRight" style="text-align:right;">
                                                                <%=total1%>
                                                            </display:column>
                                                            <%
                                                                        }
                                                                        temp = ssline;
                                                                        rsize--;
                                                                    }
                                                                }
                                                                k++;
                                                                rsize--;
                                                            %>

                                                        </display:table>
                                                    </div>
                                                </div>
                                                <%
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                    </table>


                                    <%}%>

                                    <html:hidden property="originId" />
                                    <html:hidden property="portId" />
                                    <html:hidden property="commodId" />
                                    <html:hidden property="selectedCheck" />
                                    <html:hidden property="hazmat" value="<%=hazmat%>" />
                                    <c:if test="${fileNo!=null}">
                                        <c:set var="fileNumber"  value="${fn:substring(fileNo, fn:indexOf(fileNo, '-')+1,fn:length(fileNo))}"/>
                                        <html:hidden property="eventCode" styleId="eventCode"/>
                                        <html:hidden property="moduleId" value="<%=NotesConstants.FILE%>"/>
                                        <html:hidden property="moduleRefId" value="${fileNumber}"/>
                                    </c:if>
                                    <html:hidden property="ssline" />
                                    <html:hidden property="chargecode" />
                                    <html:hidden property="butval" />
                                    <html:hidden property="pol" />
                                    <html:hidden property="pod" />
                                    <html:hidden property="rampCity" />
                                    <html:hidden property="noOfDays" value="<%=noOfDays%>" />
                                    <html:hidden property="cargoOrigins" value="<%=CargoOriginvalue%>" />
                                    <html:hidden property="destinationPort" value="<%=destPortvalue%>" />
                                    <html:hidden property="commodityId" />
                                    <html:hidden property="multiQuoteId" value="${multiQuoteId}" />
                                    <input type="hidden" name="origin" id="origin" />
                                    <input type="hidden" name="destn" id="destn" />
                                    <input type="hidden" name="comid" id="comid" />
                                    <input type="hidden" name="QuickReview" value="<%=QuickReview%>" />
                                    <html:hidden property="buttonValue" />
                                </html:form>
                                <script>load1('<%=QuickReview%>');</script>
                                <script>load('<%=rateslist.size()%>', '<%=msg%>', '<%=buttonValue%>', '<%=QuickReview%>')</script>
                                </body>
                                <%@include file="../includes/baseResourcesForJS.jsp"%>
                                <%@include file="../includes/resources.jsp"%>

                                <script language="javascript">
                                    CreateScrollHeader(document.getElementById("rateListTable"), true, true);
                                    getRemarksonComparision('${destination}', '<%=QuickReview%>', '<%=buttonValue%>');
                                </script>
                                <%if (buttonValue.equals("getRatesBooking")) { %>
                                <script>showSpecialRemarksForBooking('${destination}')</script>
                                <%}%>
                                <script type="text/javascript"
                                src="<%=path%>/js/sweet-titles-tooltip/addEvent.js"></script>
                                <script type="text/javascript"
                                src="<%=path%>/js/sweet-titles-tooltip/sweetTitles.js"></script>
                                <script type="text/javascript"
                                src="<%=path%>/js/tablesorter/jquery-latest.js"></script>
                                <script type="text/javascript"
                                src="<%=path%>/js/tablesorter/jquery.tablesorter.js"></script>
                                <script type="text/javascript"
                                src="<%=path%>/js/tablesorter/jquery.tablesorter.pager.js"></script>
                                <script type="text/javascript">
                                    jQuery.noConflict();
                                    jQuery(document).ready(function()
                                    {
                                        jQuery("#fclratestable")
                                                .tablesorter({widgets: ['zebra']});
                                    }
                                    );

                                </script>
                                <script>
                                    function setCheckBox(ev) {
                                        event.keyCode = 39;
                                    }
                                    function setCursor(ev) {
                                        ev.style.cursor = 'default';
                                    }
                                </script>
                                </html>

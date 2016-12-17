
<%@ page language="java" pageEncoding="ISO-8859-1" import="org.apache.struts.util.LabelValueBean,
         org.apache.struts.util.MessageResources,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.Charges,java.text.*,com.gp.cong.logisoft.beans.CostBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<bean:define id="localDrayage" type="String">
    <bean:message key="localDrayage"/>
</bean:define>
<bean:define id="interModal" type="String">
    <bean:message key="interModal"/>
</bean:define>
<bean:define id="inland" type="String">
    <bean:message key="inland"/>
</bean:define>
<bean:define id="minimumForIntermodal" type="String">
    <bean:message key="minimumForIntermodal"/>
</bean:define>
<style type="text/css">
    #addCharge {
        position: fixed;
        _position: absolute;
        z-index: 99;
        border-style:solid solid solid solid;
        background-color: #FFFFFF;
        left: 2%;
        right: 2%;
        top: 10%;
        _height: expression(document.body.offsetHeight-200 + "px");
    }
</style>
<%    User user = new User();
    String userName = "";
    String ratedOption = "";
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    String todaysDate = format1.format(date);
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
        userName = user.getLoginName();
    }
    if (session.getAttribute("view") != null) {
    }
    request.setAttribute("minimumForIntermodal", minimumForIntermodal);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String chargeCode = "";
    HashMap hashMap = new HashMap();
    DBUtil dbUtil = new DBUtil();
    request.setAttribute("unitTypeList", dbUtil.uniTypeList1(hashMap));
    Charges charges = new Charges();
    request.setAttribute("fileNo", request.getParameter("fileNo"));
    String importFlag = (null != request.getParameter("importFlag") && request.getParameter("importFlag").equals("true") ? "importNavigation" : "exportNavigation");
    request.setAttribute("importFlag", importFlag);
    request.setAttribute("defaultcurrency", dbUtil.getGenericFCL(new Integer(32), "yes"));
    String unitType = "";
    String number = "1";
    String vendorName = "";
    String vendorNo = "";
    String chargeCodeDesc = "";
    String costType = "";
    String currency = "";
    String amount = "0.00";
    String button = "";
    String minimum = "0.00";
    String markUp = "0.00";
    String spotRateAmt = "";
    String spotRateChk = "";
    String standardRateChk = "";
    String hazmat = "";
    String comment = "";
    String buttonValue = "";
    String newFlag = "";
    String defaultCarrier = "";
    if (request.getAttribute("buttonValue") != null) {
        buttonValue = (String) request.getAttribute("buttonValue");
    }
    if (request.getParameter("hazmat") != null && !request.getParameter("hazmat").equals("")) {
        hazmat = request.getParameter("hazmat");
    }
    if (request.getAttribute("hazmat") != null && !request.getAttribute("hazmat").equals("")) {
        hazmat = (String) request.getAttribute("hazmat");
    }
    String spclEquipment = "";
    if (request.getParameter("spcleqpmt") != null && !request.getParameter("spcleqpmt").equals("")) {
        spclEquipment = request.getParameter("spcleqpmt");
    }
    if (request.getAttribute("spcleqpmt") != null && !request.getAttribute("spcleqpmt").equals("")) {
        spclEquipment = (String) request.getAttribute("spcleqpmt");
    }

    String breakBulk = "";
    if (request.getParameter("breakBulk") != null && !request.getParameter("breakBulk").equals("")) {
        breakBulk = request.getParameter("breakBulk");
    }
    if (request.getAttribute("breakBulk") != null && !request.getAttribute("breakBulk").equals("")) {
        breakBulk = (String) request.getAttribute("breakBulk");
    }
    String quoteNo = "";
    if (request.getParameter("quoteNo") != null && !request.getParameter("quoteNo").equals("")) {
        quoteNo = request.getParameter("quoteNo");
    }
    if (request.getAttribute("quoteNo") != null && !request.getAttribute("quoteNo").equals("")) {
        quoteNo = (String) request.getAttribute("quoteNo");
    }
    NumberFormat numformat = new DecimalFormat("##,###,##0.00");
    if (request.getParameter("button") != null) {
        if (session.getAttribute("newCharges") != null) {
            session.removeAttribute("newCharges");
        }
        button = request.getParameter("button");
        session.setAttribute("search", button);
    }
    if (session.getAttribute("newCharges") != null) {
        charges = (Charges) session.getAttribute("newCharges");
        if (charges.getComment() != null) {
            comment = charges.getComment();
            comment = comment.replaceAll("[\r\n]", "\t");
            request.setAttribute("comment", comment);
        }
        if (charges.getUnitType() != null) {
            unitType = charges.getUnitType();
        }
        if (charges.getNumber() != null) {
            number = charges.getNumber();
        }
        if (charges.getChargeCode() != null && charges.getChargeCode().getId() != null) {
            chargeCode = charges.getChargeCode().getCode();
            chargeCodeDesc = charges.getChargeCode().getId().toString();
        } else {
            chargeCode = "";
        }
        request.setAttribute("chargeCode", chargeCode);
        if (charges.getCosttype() != null && charges.getCosttype().getId() != null) {
            costType = charges.getCosttype().getId().toString();
        }
        if (charges.getCurrecny() != null) {
            currency = charges.getCurrecny();
        }
        request.setAttribute("standardRate", null != charges.getAmount() ? charges.getAmount() : 0d);
        if (charges.getAmount() != null && !"DOCUM".equals(chargeCode)) {
            if (request.getAttribute("collapseAmount") != null) {
                amount = numformat.format((Double) request.getAttribute("collapseAmount"));
            } else {
                amount = numformat.format(charges.getAmount());
            }
        }
        if (charges.getAccountName() != null) {
            vendorName = charges.getAccountName();
        }
        if (charges.getAccountNo() != null) {
            vendorNo = charges.getAccountNo();
        }
        if (null != charges.getSpotRateAmt()) {
            spotRateAmt = numformat.format(charges.getSpotRateAmt());
        }
        standardRateChk = charges.getStandardChk();
        spotRateChk = charges.getSpotRateChk();
        if (charges.getMinimum() != null) {
            minimum = numformat.format(charges.getMinimum());
        }
        if ("I".equals(charges.getChargeFlag())) {
            request.setAttribute("chargeFlag", "I");
        }
        if ("M".equals(charges.getChargeFlag()) ||"CH".equals(charges.getChargeFlag())) {
            newFlag = "new";
            request.setAttribute("chargeFlag", "M");
        } else {
            newFlag = "";
        }
        if (newFlag.equals("new") && (charges.getMarkUp() != null)) {
            markUp = numformat.format(charges.getMarkUp());
        } else {
            if (request.getAttribute("collapseMarkupAmount") != null) {
                double markUp1 = (request.getAttribute("collapseMarkupAmount") != null) ? (Double) request.getAttribute("collapseMarkupAmount") : 0.00;
                double amount1 = (request.getAttribute("collapseAmount") != null) ? (Double) request.getAttribute("collapseAmount") : 0.00;
                markUp = numformat.format(markUp1 + amount1);
            } else {
                double markUp1 = (charges.getMarkUp() != null) ? charges.getMarkUp() : 0.00;
                double amount1 = (charges.getAmount() != null) ? charges.getAmount() : 0.00;
                markUp = numformat.format(markUp1 + amount1);
            }

        }
        if ("Y".equals(charges.getDefaultCarrier())) {
            defaultCarrier = "Y";
        } else if ("N".equals(charges.getDefaultCarrier())) {
            defaultCarrier = "N";
        }
    }
    if (null != amount) {
        request.setAttribute("amount", amount);
    } else {
        request.setAttribute("amount", "0.00");
    }
    if (null != markUp) {
        request.setAttribute("markUp", markUp);
    } else {
        request.setAttribute("markUp", "0.00");
    }
    session.removeAttribute("view");
    String path1 = "";
    if (request.getAttribute("path1") != null) {
        path1 = (String) request.getAttribute("path1");
    }
    String msg = "";
    if (request.getAttribute("msg") != null) {
        msg = (String) request.getAttribute("msg");
    }
    if ((request.getAttribute("markup") != null && ((String) request.getAttribute("markup")).equalsIgnoreCase("inland")) || (request.getParameter("markup") != null && ((String) request.getParameter("markup")).equalsIgnoreCase("inland"))) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        String desc=importFlag.equals("importNavigation")?"deliv":"yes";
        List list = dbUtil.getGenericCodeCostListInland(new Integer(36), desc, "Select Cost Code");
        request.setAttribute("costcodelist", list);
        request.setAttribute("markup", "inland");
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        request.setAttribute("amountForMarkup", inland);
    } else if ((request.getAttribute("markup") != null && ((String) request.getAttribute("markup")).equalsIgnoreCase("Intermodal Ramp")) || (request.getParameter("markup") != null && ((String) request.getParameter("markup")).equalsIgnoreCase("Intermodal Ramp"))) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getGenericCodeCostListIntrampMod(new Integer(36), "yes", "Select Cost Code");
        request.setAttribute("costcodelist", list);
        request.setAttribute("markup", "inland");
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        request.setAttribute("amountForMarkup", inland);
    } else if ((request.getAttribute("markup") != null && ((String) request.getAttribute("markup")).equalsIgnoreCase("deliveryCharge")) || (request.getParameter("markup") != null && ((String) request.getParameter("markup")).equalsIgnoreCase("deliveryCharge"))) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getChargeCodes(new Integer(36), "yes", "Select Cost Code", "Delivery");
        request.setAttribute("costcodelist", list);
        request.setAttribute("markup", "deliveryCharge");
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
    } else if (breakBulk != null && breakBulk.equals("Y")) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForBreakBulk(new Integer(37), "yes", "Select Cost type"));
        request.setAttribute("costcodelist", dbUtil.getGenericChargeCostList(new Integer(36), "yes", "Select Cost Code", (String) request.getAttribute("importFlag")));
    } else if ((null == request.getAttribute("markup") || !((String) request.getAttribute("markup")).equalsIgnoreCase("onCarriage")) || (null == request.getParameter("markup") || !((String) request.getParameter("markup")).equalsIgnoreCase("onCarriage"))) {
        //if markup value not "onCarriage" then this will get execute.
        request.setAttribute("markup", "no");
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteCharge(new Integer(37), "yes", "Select Cost type"));
        request.setAttribute("costcodelist", dbUtil.getGenericChargeCostList(new Integer(36), "yes", "Select Cost Code", (String) request.getAttribute("importFlag")));
    }
    if (null != request.getParameter("ratedOption")) {
        ratedOption = request.getParameter("ratedOption");
    } else if (null != request.getAttribute("ratedOption")) {
        ratedOption = request.getParameter("ratedOption");
    }

    if (null != request.getParameter("ratedOption") && request.getParameter("ratedOption").equals("NonRated")) {
        if (null != request.getAttribute("costtypelist") && !"Y".equals(breakBulk)) {
            request.setAttribute("costtypelist", dbUtil.getCodeCostListForNonRated(((List) request.getAttribute("costtypelist"))));
        }
    }
    request.setAttribute("breakBulk", breakBulk);
%>
<%
    if (request.getAttribute("buttonvalue") != null && request.getAttribute("buttonvalue").equals("completed")) {
%>
<script>
    parent.parent.GB_hide();
    parent.parent.refreshPage('<%=quoteNo%>');
</script>
<%
} else if (request.getAttribute("buttonvalue") != null && request.getAttribute("buttonvalue").equals("dontAddInsure")) {
%>
<script>
    parent.parent.GB_hide();
    parent.parent.dontAddInsureToCharges('<%=quoteNo%>');
</script>
<%
    }
%>
<html>
    <head>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript">
    function readyOnlyFields() {
        if (parent.parent.document.searchQuotationform.finalized.value === 'on') {
            parent.parent.document.makeFormBorderless1(document.getElementById("quoteChargeId"));
            document.getElementById('quotesChargeUpdate').style.visibility = "hidden";
        }
        populateMarkupValue('<%=markUp%>');
    }
        </script>
        <title>JSP for QuoteChargeForm form</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" type="text/javascript">
        var standardRate='${standardRate}';
        function closeChargeDiv() {
            document.getElementById("addCharge").style.display = 'none';
            closePopUp();
        }
        function addNonRatedCharge() {
            document.quoteChargeForm.costType.value = '0';
            document.getElementById('vendorNumber').value = '';
            document.getElementById('vendorName').value = '';
            document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
            document.getElementById("nvoCheckBox").checked = false;
            document.getElementById("defaultCarrierY").checked = false;
            document.getElementById("addCharge").style.display = 'block';
            if (document.getElementById("unitType")) {
                document.getElementById("unitType").style.visibility = 'hidden';
                document.getElementById("unitSelect").style.visibility = 'hidden';
                document.getElementById("unitSelect").value = 0;
            }
            resetAll();
            showPopUp();
        }
        function costSellValidation(obj) {
            var str = obj.value;
            var n = str.substr(str.indexOf("."));
            if (n.length > 3 && str.indexOf(".") !== -1) {
                obj.value = obj.value.substring(0, str.indexOf(".") + 3).trim();
            } else {
                return obj.value;
            }
        }
        function addCharge() {
            document.quoteChargeForm.costType.value = '0';
            document.getElementById("costId").style.visibility = 'visible';
            document.getElementById("sellId").style.visibility = 'visible';
            document.getElementById("amount").style.display = 'block';
            document.getElementById("markUp").style.display = 'block';
            document.getElementById("unitsId").style.display = 'none';
            document.getElementById('vendorNumber').value = '';
            document.getElementById('vendorName').value = '';
            document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
            document.getElementById("nvoCheckBox").checked = false;
            document.getElementById("defaultCarrierY").checked = false;
            resetAll();
            document.getElementById("addCharge").style.display = 'block';
            showPopUp();
        }
        function checkDefaultCarrier() {
            if ("<%=defaultCarrier%>" === "Y") {
                document.getElementById("defaultCarrierY").checked = true;
                document.getElementById("nvoCheckBox").checked = false;
                document.getElementById('vendorNumber').readOnly = true;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorNumber').tabIndex = -1;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorNumber').tabIndex = "-1";
                document.getElementById('vendorName').tabIndex = "-1";
            } else if ("<%=defaultCarrier%>" === "N") {
                document.getElementById("nvoCheckBox").checked = true;
                document.getElementById("defaultCarrierY").checked = false;
                document.getElementById('vendorNumber').readOnly = true;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorNumber').tabIndex = -1;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById("vendorName").value = "";
                document.getElementById("vendorNumber").value = "";
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorNumber').tabIndex = "-1";
                document.getElementById('vendorName').tabIndex = "-1";
            }
        }
        function getEdit(ev1, ev2, ev3) {
            //check common.js for the function
            var chargeCode = document.quoteChargeForm.chargeCode.value;
            var amount = document.quoteChargeForm.amount.value;
            var amountNew = amount.replace(/\,/g, "");
            var cost = parseFloat(amountNew);
            var sell = document.quoteChargeForm.markUp.value;
            var sellnew = sell.replace(/\,/g, "");
            sellnew = parseFloat(sellnew);
            var spotAmt = jQuery("#spotRateAmt").val();
            if (parent.parent.document.getElementById('spotRateY') && parent.parent.document.getElementById('spotRateY').checked && ev3 == "nonManualCharges") {
                if (chargeCode != 'INSURE' && chargeCode != 'DOCUM' && chargeCode != 'ADMIN' && chargeCode != 'AFRFEE'
                        && chargeCode != 'FFCOMM' && jQuery("#spotRateAmt") && spotAmt == "") {
                    alertNew("On Spot Rate Files Spot Costs MUST be entered Manually");
                    return;
                }
                if (jQuery("#spotRateAmt") && !jQuery("#spotRateChk").attr('checked') && parseFloat(spotAmt) === 0) {
                    jQuery("#spotRateAmt").val("");
                    if (chargeCode === 'OCNFRT') {
                        alertNew("On Spot Rate Files Spot Costs MUST be entered Manually");
                        return;
                    } else {
                        alertNew("Please check the 'Zero Cost' check box if there is no cost associated with this item");
                        return;
                    }
                }
            }
            if ((document.quoteChargeForm.amount.disabled === false) && (document.quoteChargeForm.markUp.disabled === false) && (chargeCode !== 'INSURE')) {
                if (sellnew < cost) {
                    alertNew("Sell always greater than or equals to Cost");
                    return;
                }
            }
            appendUserInfoForComments(document.getElementById('comment'), ev1, ev2);
            var commentValue = document.getElementById('comment').value.trim();
            commentValue = document.getElementById('previousComments').value + commentValue;

            document.quoteChargeForm.comment.value = commentValue;
            if (!document.getElementById('nvoCheckBox').checked && document.getElementById('vendorName').value === '') {
                if (chargeCode !== 'INSURE' && chargeCode !== 'DOCUM' && chargeCode !== 'ADMIN' && chargeCode !== 'AFRFEE' && chargeCode !== "DEFER") {
                    alertNew("Please enter Vendor Name");
                    return;
                } else if (chargeCode === "DEFER") {
                    alertNew("Please Enter SSL Name in Quote");
                    return;
                }
            }
            if (document.getElementById('costType').value === '11317') {
                var size = parent.parent.document.getElementById("selectedUnits").value;
                if (undefined !== size && null !== size) {
                    var split = size.split(",");
                    if (split.length > 1) {
                        confirmNew("Do you want to apply the changes for all the container?", 'updateall');
                    } else {
                        document.getElementById('quotesChargeUpdate').disabled = true;
                        if ('${amount}' !== amount || '${markUp}' !== sell) {
                            document.quoteChargeForm.buttonValue.value = "update";
                        } else {
                            document.quoteChargeForm.buttonValue.value = "updateWithOutInsure";
                        }
                        document.quoteChargeForm.submit();
                    }
                } else {
                    confirmNew("Do you want to apply the changes for all the container?", 'updateall');
                }
            } else {

                makePageEditableWhileSaving(document.getElementById("quoteChargeId"));
                document.getElementById('quotesChargeUpdate').disabled = true;
                if ('${amount}' !== amount || '${markUp}' !== sell) {
                    document.quoteChargeForm.buttonValue.value = "update";
                } else {
                    document.quoteChargeForm.buttonValue.value = "updateWithOutInsure";
                }
                document.quoteChargeForm.submit();
            }
        }
        function makePageEditableWhileSaving(form) {
            var element;
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if (element.type === "text" || element.type === "textarea") {
                    element.style.border = "1px solid #C4C5C4";
                    element.style.backgroundColor = "#FCFCFC";
                    element.readOnly = false;
                    element.tabIndex = -1;
                    element.disabled = false;
                }
                if (element.type === "select-one") {
                    element.style.border = 0;
                    element.disabled = false;
                }
                if (element.type === "checkbox" || element.type === "radio") {
                    element.disabled = false;
                }
            }
        }
        function confirmMessageFunction(id1, id2) {
            var amount = document.quoteChargeForm.amount.value;
            var sell = document.quoteChargeForm.markUp.value;
            var comment = document.quoteChargeForm.comment.value;
            if (id1 === 'updateall' && id2 === 'ok') {
                parent.parent.call('insurance');
                if (document.getElementById('quotesChargeUpdate')) {
                    document.getElementById('quotesChargeUpdate').disabled = true;
                }
                if ('${amount}' !== amount || '${markUp}' !== sell || '${comment}' !== comment) {
                    document.quoteChargeForm.buttonValue.value = "updateall";
                } else {
                    document.quoteChargeForm.buttonValue.value = "updateAllWithOutInsure";
                }
                document.quoteChargeForm.submit();
            } else if (id1 === 'updateall' && id2 === 'cancel') {
                parent.parent.call('insurance');
                if ('${amount}' !== amount || '${markUp}' !== sell) {
                    document.quoteChargeForm.buttonValue.value = "update";
                } else {
                    document.quoteChargeForm.buttonValue.value = "updateWithOutInsure";
                }
                document.quoteChargeForm.submit();
            } else if (id1 === 'noCost' && id2 === 'ok') {
                if (document.getElementById('quotesChargeUpdate')) {
                    document.getElementById('quotesChargeUpdate').disabled = true;
                }
                document.quoteChargeForm.buttonValue.value = "accept";
                document.getElementById("defaultCarrierY").checked = false;
                document.quoteChargeForm.submit();
            }
        }
        function getAccept(ev, ev1, ev2) {
            //check common.js for the function
            var chargeCode = document.quoteChargeForm.chargeCode.value;
            appendUserInfoForComments(document.getElementById('comment'), ev1, ev2);
            var commentValue = document.getElementById('comment').value.trim();
            commentValue = document.getElementById('previousComments').value + commentValue;//"\r"
            document.quoteChargeForm.comment.value = commentValue;
            if (document.quoteChargeForm.costType.value === 0) {
                alertNew("please select the Cost Type");
                return;
            }
            if ((${(param.ratedOption == 'NonRated' || ratedOption == 'NonRated') && (breakBulk ne 'Y')}) && document.quoteChargeForm.unitSelect.value === 0) {
                alertNew("Please Select The Unit Type");
                return;
            }
            if (document.quoteChargeForm.chargeCode.value === '') {
                alertNew("please select the Charge Code");
                return;
            }
            if (!document.getElementById("nvoCheckBox").checked && document.getElementById('vendorName').value === '') {
                if (chargeCode !== 'INSURE' && chargeCode !== 'ADMIN' && chargeCode !== 'AFRFEE' && chargeCode !== "DEFER") {
                    alertNew("Please enter Vendor Name");
                    return;
                } else if (chargeCode === "DEFER") {
                    alertNew("Please Enter SSL Name in Quote");
                    return;
                }
            }
            if (document.quoteChargeForm.costType.value === "11300") {
                var units = '${selectedUnits}';
                var unit = units.split(",");
                for (i = 0; i < unit.length; i++) {
                    if (unit[i] !== '') {
                        var size = "";
                        if (unit[i] === 'E=45102') {
                            size = "G";
                        } else {
                            size = unit[i].substring(0, 1);
                        }
                        if (document.getElementById("amount" + size) && document.getElementById("markUp" + size)) {
                            if (!document.getElementById("amount" + size).style.disabled && !document.getElementById("markUp" + size).style.disabled) {
                                var cost = parseFloat(document.getElementById("amount" + size).value);
                                var sell = document.getElementById("markUp" + size).value;
                                var sellnew = sell.replace(/\,/g, "");
                                sellnew = parseFloat(sellnew);
                                if (sellnew < cost) {
                                    alertNew("Sell always greater than or equals to Cost");
                                    return;
                                }
                            }
                        }
                    }
                }
                if ((chargeCode === 'HAZFEE' || chargeCode === 'HAZCER' || chargeCode === 'HAZCERT') && parent.parent.document.getElementById('hazmatNo').checked) {
                    alertNew("Please select Hazmat = yes, for selecting the Hazardous charge code");
                    return;
                }
                for (i = 0; i < unit.length; i++) {
                    if (unit[i] !== '') {
                        var size = "";
                        if (unit[i] === 'E=45102') {
                            size = "G";
                        } else {
                            size = unit[i].substring(0, 1);
                        }
                        if (document.getElementById("amount" + size) && document.getElementById("markUp" + size)) {
                            if ((document.getElementById("amount" + size).value === "0.00" || document.getElementById("amount" + size).value === "")
                                    && document.getElementById("markUp" + size).value !== "0.00"
                                    && document.getElementById("markUp" + size).value !== ""
                                    && chargeCode !== 'INSURE' && chargeCode !== 'ADMIN' && chargeCode !== 'AFRFEE') {
                                confirmNew("Are you sure that there is no associated cost for this item", "noCost");
                                return;
                            }
                        }
                    }
                }
                document.getElementById('submitCharges').disabled = true;
                document.quoteChargeForm.buttonValue.value = "accept";
                document.quoteChargeForm.submit();
            } else {
                if (ev === 'localdrayage' || ev === 'intermodal') {
                    document.quoteChargeForm.forMarkUp.value = "closeThePopUP";
                } else {
                    document.quoteChargeForm.forMarkUp.value = "dontClose";
                }
                if (document.quoteChargeForm.costType.value === "11300") {
                    if (document.quoteChargeForm.unitSelect.value === 0) {
                        alertNew("please select the unit");
                        return;
                    }
                }
                var cost = parseFloat(document.quoteChargeForm.amount.value);
                var sell = document.quoteChargeForm.markUp.value;
                var sellnew = sell.replace(/\,/g, "");
                sellnew = parseFloat(sellnew);
                if ((document.quoteChargeForm.amount.disabled === false) && (document.quoteChargeForm.markUp.disabled === false)) {
                    if (sellnew < cost) {
                        alertNew("Sell always greater than or equals to Cost");
                        return;
                    }
                }
                if ((document.getElementById("amount" + size) && document.getElementById("markUp" + size))
                        && (document.getElementById('amount').value === "0.00" || document.getElementById('amount').value === "")
                        && document.getElementById('markUp').value !== "0.00"
                        && document.getElementById('markUp').value !== ""
                        && chargeCode !== 'INSURE' && chargeCode !== 'ADMIN' && chargeCode !== 'AFRFEE') {
                    confirmNew("Are you sure that there is no associated cost for this item", "noCost");
                } else {
                    if (parent.parent.document.getElementById('rampCheck').value === 'on') {
                        document.quoteChargeForm.rampCheck.value = 'on';
                    } else {
                        document.quoteChargeForm.rampCheck.value = 'off';
                    }
                    document.getElementById('submitCharges').disabled = true;
                    document.quoteChargeForm.buttonValue.value = "accept";
                    document.quoteChargeForm.submit();
                }
            }
        }
        function getChargeCode(chargeDesc) {
            if (chargeDesc.value !== 0) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getChargeByChargeDescId",
                        param1: chargeDesc.value,
                        dataType: "json"
                    },
                    success: function(data) {
                        document.quoteChargeForm.chargeCode.value = data.code;
                        toDisableAmountandMarkUp('', '');
                         if (data.code==='DEFER'){
                            setSteamshipline();
                     }else{
                        document.getElementById('vendorName').value='';
                        document.getElementById('vendorNumber').value='';
                        }
                    }
                });
            } else {
                document.quoteChargeForm.chargeCode.value = "";
            }
            //toDisableAmountandMarkUp();
        }

        function setSteamshipline()
        {
            document.getElementById('vendorName').value = parent.parent.document.getElementById('sslDescription').value;
            document.getElementById('custname_check').value = document.getElementById('vendorName').value;
            document.getElementById('vendorNumber').value = parent.parent.document.getElementById('sslcode').value;
            document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById('vendorName').tabIndex = "0";
            document.getElementById('vendorName').readOnly = true;
            document.getElementById('vendorName').tabIndex = 0;
            document.getElementById("defaultCarrierY").checked = false;
            document.getElementById("defaultCarrierY").disabled = true;
            document.getElementById("nvoCheckBox").disabled = true;
        }

        var nvoCompanyName;
        var nvoCompanyNumber;
        function getload(val, val1, val2, val3) {
            var chargeCode = document.quoteChargeForm.chargeCode.value;
            if (document.getElementById("defaultCarrierY").checked) {
                document.getElementById('vendorName').value = parent.parent.document.getElementById('sslDescription').value;
                document.getElementById('vendorNumber').value = parent.parent.document.getElementById('sslcode').value;
            } else {
                document.getElementById('vendorName').value = "";
                document.getElementById('vendorNumber').value = "";
                document.getElementById("defaultCarrierY").checked = false;
            }
            if (val1 === 'edit') {
                document.getElementById('vendorName').value = "<%=vendorName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%>";
                document.getElementById('oldVendorName').value = "<%=vendorName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%>";
                document.getElementById('vendorNumber').value = "<%=vendorNo%>";
                document.getElementById('oldVendor').value = "<%=vendorNo%>";
                document.getElementById('custname_check').value = document.getElementById('vendorName').value;
                document.getElementById("unitType").style.display = 'none';
                document.getElementById("unitSelect").style.display = 'none';
                document.quoteChargeForm.chargeCode.disabled = true;
                document.quoteChargeForm.costType.disabled = true;
                document.quoteChargeForm.chargeCodeDesc.disabled = true;
                var spotChkStatus='<%=spotRateChk%>';
                var standRateChk='<%=standardRateChk%>';
                if(spotChkStatus==="on"){
                    jQuery("#spotRateChk").attr("checked", true);
                }else{
                    jQuery("#spotRateChk").attr("checked", false);
                }
                if(standRateChk==="on"){
                    jQuery("#standardChk").attr("checked", true);
                }else{
                    jQuery("#standardChk").attr("checked", false);
                }
                if(jQuery("#spotRateChk").attr('checked') || jQuery("#standardChk").attr('checked')){
                    jQuery("#spotRateAmt").attr("disabled", "disabled").css('background-color', '#CCEBFF');
                }else{
                    jQuery("#spotRateAmt").attr("disabled", false).css('background-color', '#FFFFFF');
                }
                if (parent.parent.document.getElementById('spotRateY') && parent.parent.document.getElementById('spotRateY').checked && val2 == 'nonManualCharges' &&
                        chargeCode != 'INSURE' && chargeCode != 'DOCUM' && chargeCode != 'ADMIN' && chargeCode != 'AFRFEE'
                        && chargeCode != 'FFCOMM') {
                    jQuery("#spotRateLable,#spotRateAmt,#zeroCost,#spotRateChkTd,#standardRateLable,#standardChkTd").css({
                        display: "block"
                    });
                }else if(parent.parent.document.getElementById('spotRateN') && parent.parent.document.getElementById('spotRateN').checked){
                    jQuery("#spotRateLable,#spotRateAmt,#zeroCost,#spotRateChkTd,#standardRateLable,#standardChkTd").css({
                        display: "none"
                    });
                }
            }
            if (val3 === 'new') {
                if (chargeCode === 'ADMIN' || chargeCode === 'AFRFEE') {
                    document.quoteChargeForm.amount.disabled = true;
                    document.quoteChargeForm.amount.style.backgroundColor = '#CCEBFF';
                    document.quoteChargeForm.vendorName.disabled = true;
                    document.quoteChargeForm.vendorNumber.disabled = true;
                    document.getElementById("defaultCarrierY").disabled = true;
                    document.getElementById("nvoCheckBox").disabled = true;
                } else if (chargeCode === 'DEFER') {
                    document.getElementById('vendorName').value = parent.parent.document.getElementById('sslDescription').value;
                    document.getElementById('vendorNumber').value = parent.parent.document.getElementById('sslcode').value;
                    document.getElementById('vendorName').readOnly = true;
                    document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("defaultCarrierY").disabled = true;
                    document.getElementById("nvoCheckBox").disabled = true;
                } else {
                    document.quoteChargeForm.amount.disabled = false;
                    document.quoteChargeForm.markUp.disabled = false;
                    if (document.getElementById("nvoCheckBox").checked) {
                        document.getElementById('vendorName').readOnly = true;
                        document.getElementById('vendorName').tabIndex = -1;
                        document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                        document.getElementById('vendorName').tabIndex = "-1";
                    } else if (document.getElementById("defaultCarrierY").checked) {
                        document.getElementById('vendorName').readOnly = true;
                        document.getElementById('vendorName').tabIndex = -1;
                        document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                        document.getElementById('vendorName').tabIndex = "-1";
                    }
                    document.getElementById("defaultCarrierY").disabled = false;
                    document.getElementById("nvoCheckBox").disabled = false;
                }
            } else if (val2 === 'nonManualCharges') {
                document.quoteChargeForm.amount.disabled = true;
                document.quoteChargeForm.amount.style.backgroundColor = '#CCEBFF';
                document.quoteChargeForm.vendorName.disabled = true;
                document.quoteChargeForm.vendorNumber.disabled = true;
                document.getElementById("defaultCarrierY").disabled = true;
                document.getElementById("nvoCheckBox").disabled = true;
            }
            getcosttype('${markup}');

            //--to get nvocompany details----
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getNvo",
                    dataType: "json"
                },
                success: function(data) {
                    if (data !== null) {
                        if (data.accountName !== null && data.accountName !== '') {
                            nvoCompanyName = data.accountName;
                        }
                        if (data.accountno !== null && data.accountno !== '') {
                            nvoCompanyNumber = data.accountno;
                        }
                    }
                }
            });
            //--to check  amount/markup for each chargecodes in order to disable them based on Field7 value of genericcodedup table
            if (val1 === 'edit' && val2 !== 'nonManualCharges') {
                toDisableAmountandMarkUp('', val1);
            }


        }
        function getcosttype(ev) {
            if (document.getElementById("unitType")) {
                if (document.quoteChargeForm.costType.value === "11300") {
                    document.getElementById("unitType").style.visibility = 'visible';
                    document.getElementById("unitSelect").style.visibility = 'visible';
                } else {
                    document.getElementById("unitType").style.visibility = 'hidden';
                    document.getElementById("unitSelect").style.visibility = 'hidden';
                    document.getElementById("unitSelect").value = 0;
                }
            }
        }
        function selectCostType() {
            jQuery("#submitCharges").removeAttr("disabled");
            if (document.quoteChargeForm.costType.value === "11300") {
                if ('${selectedUnits}') {
                    document.getElementById("costId").style.visibility = 'hidden';
                    document.getElementById("sellId").style.visibility = 'hidden';
                    document.getElementById("amount").style.display = 'none';
                    document.getElementById("markUp").style.display = 'none';
                    document.getElementById("unitsId").style.display = 'block';
                } else {
                    jQuery("#submitCharges").attr('disabled', true);
                    alertNew("Please select atleast one unit size in main page to proceed.");
                }
            } else {
                document.getElementById("costId").style.visibility = 'visible';
                document.getElementById("sellId").style.visibility = 'visible';
                document.getElementById("amount").style.display = 'block';
                document.getElementById("markUp").style.display = 'block';
                document.getElementById("unitsId").style.display = 'none';
            }
            resetAll();
        }
        function resetAll() {
            var markUp = '${markup}';
            if (markUp !== 'inland') {
                document.quoteChargeForm.chargeCodeDesc.value = '0';
                document.quoteChargeForm.chargeCode.value = '';
            } else {
                returnChargeCode(document.quoteChargeForm.chargeCodeDesc);
            }
            if (document.quoteChargeForm.costType.value === "11300") {
                var units = '${selectedUnits}';
                var unit = units.split(",");
                for (i = 0; i < unit.length; i++) {
                    if (unit[i] !== '') {
                        var size = "";
                        if (unit[i] === 'E=45102') {
                            size = "G";
                        } else {
                            size = unit[i].substring(0, 1);
                        }
                        if (document.getElementById("amount" + size)) {
                            document.getElementById("amount" + size).value = '0.00';
                        }
                        if (document.getElementById("markUp" + size)) {
                            document.getElementById("markUp" + size).value = '0.00';
                        }
                        if (document.getElementById("repeat" + size)) {
                            document.getElementById("repeat" + size).checked = false;
                        }
                    }
                }
            } else {
                document.getElementById("markUp").value = '0.00';
                document.getElementById("amount").value = '0.00';
            }

        }
        function getClose() {
            document.quoteChargeForm.buttonValue.value = "close";
            document.quoteChargeForm.submit();
        }
        function setZeroSpot(){
            if (jQuery("#spotRateChk").attr('checked')) {
                jQuery("#spotRateAmt").val("0.00").attr("disabled", "disabled").css('background-color', '#CCEBFF');
            }else{
                jQuery("#spotRateAmt").val("").attr("disabled", false).css('background-color', '#FFFFFF');
            }
            jQuery("#standardChk").attr("checked", false);
        }
        function setStandardSpot(){
            if (jQuery("#standardChk").attr('checked')) {
                jQuery("#spotRateAmt").val(standardRate).attr("disabled", "disabled").css('background-color', '#CCEBFF');
            }else{
                jQuery("#spotRateAmt").val("").attr("disabled", false).css('background-color', '#FFFFFF');
            }
            jQuery("#spotRateChk").attr("checked", false);
        }
        function getDefaultCarrier() {
            if (document.getElementById("defaultCarrierY").checked) {
                document.getElementById("nvoCheckBox").checked = false;
                document.getElementById('vendorName').value = parent.parent.document.getElementById('sslDescription').value;
                document.getElementById('custname_check').value = document.getElementById('vendorName').value;
                document.getElementById('vendorNumber').value = parent.parent.document.getElementById('sslcode').value;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').tabIndex = "-1";
                document.getElementById("nvoCheckBox").checked = false;
            } else {
                var chargeCode = document.quoteChargeForm.chargeCode.value;
                if (!document.getElementById("defaultCarrierY").checked && chargeCode==='DEFER'){
                    setSteamshipline();
                }else{
                    document.getElementById('vendorName').value = "";
                    document.getElementById('vendorNumber').value = "";
                    document.getElementById('vendorName').readOnly = false;
                    document.getElementById('vendorName').tabIndex = 0;
                    document.getElementById("defaultCarrierY").checked = false;
                    document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                    document.getElementById('vendorName').tabIndex = "0";
                }
            }
        }
        function setFocus(ev, val) {
            if ((ev !== 'undefined' && ev === 'inland') || val === 'inland') {
                document.getElementById('percentMarkUp').style.display = 'block';
                document.getElementById('percentMarkUp1').style.display = 'block';
            }
            setTimeout("set()", 150);
        }
        function set() {
            if (document.getElementById('unitSelect')) {
                document.getElementById('unitSelect').focus();
            }
        }
        function getMarkUp(val1, val2, minimum, val) {
            if (val === 'inland' || val1 === 'inland') {
                if (document.getElementById('amount').value === '') {
                    alertNew("Please enter Cost");
                    return;
                } else if (document.getElementById('percentage').value === '') {
                    alertNew("Please enter MarkUp");
                    return;
                } else {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.QuoatteDwrBC",
                            methodName: "getMarkUpValue",
                            param1: document.getElementById('amount').value,
                            param2: document.getElementById('percentage').value,
                            param3: minimum
                        },
                        success: function(data) {
                            populateMarkupValue(data);
                        }
                    });
                }
            } else {
                return;
            }
        }
        function checkForNumberAndDecimal(obj) {
            if (!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
                obj.value = "";
                alertNew("The Amount You Entered is not a Valid");
                return;
            } else {
                costSellValidation(obj);
            }
        }
        function checkForNumberAndDecimalForSpot(obj) {
            var condition;
            if(jQuery("#chargeCode").val()==='DEFER'){
                condition= !/^-([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)
            }else{
                condition= !/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)
            }
            if (condition) {
                if(obj.value===""){
                    alertNew("On Spot Rate Files Spot Costs MUST be entered Manually");
                }else{
                    alertNew("The Amount You Entered is not a Valid");
                }
                obj.value = "";
                return;
            } else {
                if(parseFloat(obj.value)===0){
                    alertNew("Please check the 'Zero Cost' check box if there is no cost associated with this item");
                    obj.value = "";
                }
                costSellValidation(obj);
            }
        }
        function populateMarkupValue(data) {
            document.getElementById('markUp').value = data;
            if (document.getElementById('minimumApplied')) {
                var value = parseInt(data);
                var minumumAmount = '${minimumForIntermodal}';
                minumumAmount = minumumAmount.replace('$', '');
                minumumAmount = parseInt(minumumAmount);
                if (minumumAmount === value && (document.quoteChargeForm.chargeCode.value === 'INTMDL' || document.quoteChargeForm.chargeCode.value === 'INLAND')) {
                    document.getElementById('minimumApplied').innerHTML = 'Min Charge Applied';
                } else {
                    document.getElementById('minimumApplied').innerHTML = '';
                }
            }
        }
        function disableVendorCheckBox() {
            if (document.getElementById("nvoCheckBox").checked) {
                document.getElementById("defaultCarrierY").checked = false;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById("vendorName").value = "";
                document.getElementById("vendorNumber").value = "";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').tabIndex = "-1";
            } else {
                var chargeCode = document.quoteChargeForm.chargeCode.value;
                if (!document.getElementById("nvoCheckBox").checked && chargeCode==='DEFER'){
                    setSteamshipline();
                }else{
                    document.getElementById('vendorName').value = "";
                    document.getElementById('vendorNumber').value = "";
                    document.getElementById('vendorName').readOnly = false;
                    document.getElementById('vendorName').tabIndex = 0;
                    document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                    document.getElementById('vendorName').tabIndex = "0";
                }
            }
        }
        function getNvoNameAndNumber() {
            var path = "";
            //--to display records with acctype 'Z'--------
            if (document.getElementById("nvoCheckBox").checked) {
                path = "&nvo=booking";
            } else {
                path = "&nvo=";
            }
            appendEncodeUrl(path);
        }
        function checkForDisable() {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("vendorNumber").value
                },
                success: function doNothing(data) {
                    if (data !== "") {
                        alertNew(data);
                        document.getElementById("vendorNumber").value = '';
                        document.getElementById("vendorName").value = '';
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                                methodName: "getCustInfoForCustNo",
                                param1: document.getElementById("vendorNumber").value,
                                dataType: "json"
                            },
                            success: function(data) {
                                fillVendorData(data);
                            }
                        });
                    }
                }
            });
        }
        function  fillVendorData(data) {
            var type;
            var array1 = new Array();
            if (data.acctType !== null) {
                type = data.acctType;
                array1 = type.split(",");
                if (null !== data.acctType && array1.length > 0 && array1.contains("V")) {
                    document.getElementById("vendorNumber").value = data.acctNo;
                } else {
                    alertNew("Select the customers with Account Type V");
                    document.getElementById("vendorName").value = "";
                    document.getElementById("vendorNumber").value = "";
                }
            }
        }
        function toDisableAmountandMarkUp(val1, action) {
            //--to check  amount/markup for each chargecodes in order to disable them based on Field7 value of genericcodedup table----
            var chargeCode = document.quoteChargeForm.chargeCode.value;
            if (val1 === '') {
                document.quoteChargeForm.markUp.disabled = false;
                document.quoteChargeForm.amount.disabled = false;
                document.quoteChargeForm.markUp.className = "textlabelsBoldForTextBox";
                document.quoteChargeForm.amount.className = "textlabelsBoldForTextBox";
                document.quoteChargeForm.vendorName.disabled = false;
                document.quoteChargeForm.vendorNumber.disabled = false;
//                document.getElementById("defaultCarrierY").disabled = false;
//                document.getElementById("nvoCheckBox").disabled = false;
                if (document.quoteChargeForm.chargeCode.value !== '') {
                    if (chargeCode === 'INSURE' || chargeCode === 'ADMIN' || chargeCode === 'AFRFEE') {
                        document.quoteChargeForm.amount.disabled = true;
                        document.quoteChargeForm.amount.style.backgroundColor = '#CCEBFF';
                        document.quoteChargeForm.vendorName.disabled = true;
                        document.quoteChargeForm.vendorNumber.disabled = true;
                        document.getElementById("defaultCarrierY").disabled = true;
                        document.getElementById("nvoCheckBox").disabled = true;
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "checkAmountMarkup",
                                param1: chargeCode
                            },
                            success: function(data) {
                                if (null !== data && data !== '') {
                                    if (data === 'C') {
                                        document.quoteChargeForm.markUp.disabled = true;
                                        document.quoteChargeForm.markUp.style.backgroundColor = '#CCEBFF';
                                    } else if (data === 'S') {
                                        document.quoteChargeForm.amount.disabled = true;
                                        document.quoteChargeForm.amount.style.backgroundColor = '#CCEBFF';
                                    } else if (data === 'CS') {
                                        //---allow both amount and markup to enter---
                                    }
                                }
                            }
                        });
                    }
                    var shipmentType;
                    if (parent.parent.document.searchQuotationform.fileType && parent.parent.document.searchQuotationform.fileType.value === 'I') {
                        shipmentType = "FCLI";
                    } else {
                        shipmentType = "FCLE";
                    }
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "checkCostCodeInGeneralLedger",
                            param1: document.quoteChargeForm.chargeCode.value,
                            param2: shipmentType,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data) {
                                if (action !== 'edit') {
                                    alertNew("No Cost code set up in the General Ledger Charges");
                                    document.quoteChargeForm.amount.value = "0.00";
                                    document.quoteChargeForm.chargeCodeDesc.value = "0";
                                    document.quoteChargeForm.chargeCode.value = "";
                                }
                                document.quoteChargeForm.amount.disabled = true;
                                document.quoteChargeForm.amount.style.backgroundColor = '#CCEBFF';
                            }
                        }
                    });
                }
            }
        }
        function returnChargeCode(chargeDesc) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "getChargeByChargeDescId",
                    param1: chargeDesc.value,
                    dataType: "json"
                },
                success: function(data) {
                    document.quoteChargeForm.chargeCode.value = null !== data ? data.code : "";
                    if (document.quoteChargeForm.costType.value === "11300") {
                        toDisableAllAmountandMarkUp('', '');
                    } else {
                        toDisableAmountandMarkUp('', '');
                    }
                    if (data.code==='DEFER'){
                        setSteamshipline();
                    } else {
                        document.getElementById('vendorName').value='';
                        document.getElementById('vendorNumber').value='';
                    }
                }
            });
            //toDisableAmountandMarkUp();
        }
        function toDisableAllAmountandMarkUp(val1, action) {
            //--to check  amount/markup for each chargecodes in order to disable them based on Field7 value of genericcodedup table----
            var chargeCode = document.quoteChargeForm.chargeCode.value;
            if (val1 === '') {
                jQuery(".textlabelsBoldAmount").attr("disabled", false);
                jQuery(".textlabelsBoldMarkUp").attr("disabled", false);
                jQuery(".textlabelsBoldMarkUp").css("background-color", "white");
                jQuery(".textlabelsBoldAmount").css("background-color", "white");
                if (chargeCode !== '') {
                    if (chargeCode === 'INSURE' || chargeCode === 'ADMIN' || chargeCode === 'AFRFEE') {
                        jQuery(".textlabelsBoldAmount").attr("disabled", true);
                        jQuery(".textlabelsBoldAmount").css("background-color", "#CCEBFF");
                        document.quoteChargeForm.vendorName.disabled = true;
                        document.quoteChargeForm.vendorNumber.disabled = true;
                        document.getElementById("defaultCarrierY").disabled = true;
                        document.getElementById("nvoCheckBox").disabled = true;
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "checkAmountMarkup",
                                param1: chargeCode
                            },
                            success: function(data) {
                                if (null !== data && data !== '') {
                                    if (data === 'C') {
                                        jQuery(".textlabelsBoldMarkUp").attr("disabled", true);
                                        jQuery(".textlabelsBoldMarkUp").css("background-color", "#CCEBFF");
                                    } else if (data === 'S') {
                                        jQuery(".textlabelsBoldAmount").attr("disabled", true);
                                        jQuery(".textlabelsBoldAmount").css("background-color", "#CCEBFF");
                                    } else if (data === 'CS') {
                                        //---allow both amount and markup to enter---
                                    }
                                }
                            }
                        });
                    }
                    var importFlag = '${importFlag}';
                    var shipmentType;
                    if (importFlag === 'importNavigation') {
                        shipmentType = "FCLI";
                    } else {
                        shipmentType = "FCLE";
                    }
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "checkCostCodeInGeneralLedger",
                            param1: chargeCode,
                            param2: shipmentType,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data) {
                                if (action !== 'edit') {
                                    alertNew("No Cost code set up in the General Ledger Charges");
                                    document.quoteChargeForm.chargeCodeDesc.value = "0";
                                    document.quoteChargeForm.chargeCode.value = "";
                                    jQuery(".textlabelsBoldAmount").val("0.00");
                                }
                                jQuery(".textlabelsBoldAmount").attr("disabled", true);
                                jQuery(".textlabelsBoldAmount").css("background-color", "#CCEBFF");
                            }
                        }
                    });
                }
            }
        }
        function saveToQuote() {
            if (parent.parent.document.getElementById('rampCheck').value === 'on') {
                document.quoteChargeForm.rampCheck.value = 'on';
            } else {
                document.quoteChargeForm.rampCheck.value = 'off';
            }
            document.getElementById('saveToQuoteScreen').style.display = 'none';
            document.quoteChargeForm.buttonValue.value = "SaveToQuote";
            document.quoteChargeForm.submit();
        }
        function allowOnlyWholeNumbers(obj) {
            var result;
            if (!/^[1-9 . ]\d*$/.test(obj.value)) {
                result = obj.value.replace(/[^0-9 . ]+/g, '');
                obj.value = result;
                return false;
            }
            return true;
        }
        function repeatLast(obj) {
            var units = '${selectedUnits}';
            var unit = units.split(",");
            var amount = '0.00';
            var markup = '0.00';
            for (i = 0; i < unit.length; i++) {
                if (unit[i] !== '') {
                    var size = "";
                    if (unit[i] === 'E=45102') {
                        size = "G";
                    } else {
                        size = unit[i].substring(0, 1);
                    }
                    var id = 'repeat' + size;
                    if (obj.id === id) {
                        if (obj.checked) {
                            document.getElementById("amount" + size).value = amount;
                            document.getElementById("markUp" + size).value = markup;
                        } else {
                            document.getElementById("amount" + size).value = '0.00';
                            document.getElementById("markUp" + size).value = '0.00';
                        }
                    }
                    amount = document.getElementById("amount" + size).value;
                    markup = document.getElementById("markUp" + size).value;
                }
            }
        }
        </script>
    </head>
    <body class="whitebackgrnd"  >
        <div id="cover" style="width: 100% ;height: 100%"></div>

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

        <div id="ConfirmBox" class="alert">
            <p class="alertHeader" ><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert" >

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes" onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="No" onclick="No()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->
        <%List otherChargesList = (List) session.getAttribute("chargesList");%>
        <html:form action="/quoteCharge"  styleId="quoteChargeId" scope="request">
            <input type="hidden" value="<%=comment.replace("\"", "&#39;")%>" id="previousComments"/>
            <font color="blue" style="font-family:Arial;font-size: medium;font-weight:bolder;" size="2"><%=msg%></font>
            &nbsp;File No:<font color="red">&nbsp;<c:out value="${fileNo}"></c:out></font>
                <table class="tableBorderNew" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="tableHeadingNew"><td>Charges </td><td><span style="float:right">
                            <c:choose>
                                <c:when test="${param.ratedOption == 'NonRated' || ratedOption == 'NonRated'}">
                                    <input type="button" value="Add Charge" onclick="addNonRatedCharge();" class="buttonStyleNew"/>&nbsp;
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="Add Charge" onclick="addCharge();" class="buttonStyleNew"/>&nbsp;
                                </c:otherwise>
                            </c:choose>
                            <%if (null == otherChargesList || otherChargesList.isEmpty()) {%>
                            <input type="button" value="Cancel" onclick="getClose();" class="buttonStyleNew"/>
                            <%}%></span></td>
                </tr>

                <tr>
                    <td align="left" colspan="2" width="100%" style="padding-top: 10px;"><div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:80%">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                                <%
                                    if ((otherChargesList != null && otherChargesList.size() > 0)) {
                                        int l = 0;
                                        String unitTypes = "";
                                        String dispAmount = "0.00";
                                        String dispMarkUp = "0.00";
                                        String dispMinimum = "0.00";
                                        Charges cb = new Charges();
                                %>
                                <display:table name="<%=otherChargesList%>"  class="displaytagstyleNew"
                                id="lclcoloadratestable" sort="list" style="width:100%" pagesize="<%=pageSize%>">
                                    <%
                                        cb = (Charges) otherChargesList.get(l);
                                        if (cb.getUnitName() != null) {
                                            unitTypes = cb.getUnitName();
                                        }
                                        if (cb.getAmount() != null) {
                                            dispAmount = numformat.format(cb.getAmount());
                                        } else if (cb.getRetail() != null) {
                                            dispAmount = numformat.format(cb.getRetail());
                                        }
                                        if (cb.getMarkUp() != null) {
                                            dispMarkUp = numformat.format(cb.getMarkUp());
                                        }
                                    %>
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"><font color="blue">{0}</font><br></span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">One {0} displayed. Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">{0} {1} Displayed, Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner">No Records Found.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
                                    <display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
                                    <display:column title="Unit Type" ><%=unitTypes%></display:column>
                                    <display:column title="Charge Code" property="chgCode"></display:column>
                                    <display:column title="CostType" property="costType"></display:column>
                                    <display:column title="Currency" property="currecny"> </display:column>
                                    <display:column title="Cost"><%=dispAmount%></display:column>
                                    <display:column title="Sell"><%=dispMarkUp%></display:column>
                                    <%
                                        l++;
                                    %>
                                </display:table>
                                <%
                                    }
                                %>
                            </table>
                        </div>
                    </td>
                </tr>
                <%if (null != otherChargesList && !otherChargesList.isEmpty()) {%>
                <tr>
                    <td colspan="2"><table align="center"><tr><td align="center">
                                    <input type="button" align="center" id="saveToQuoteScreen" value="Save To Quote" style="width: 90px" onclick="saveToQuote();" class="buttonStyleNew"/>
                                    <input type="button" align="center" value="Cancel" onclick="getClose()" style="max-width:120px" class="buttonStyleNew"/></td></tr></table>
                    </td>
                </tr>
                <%}%>
            </table>
            <c:choose>
                <c:when test="${buttonValue == 'edit'}">
                    <div id="addCharge" >
                        <table class="tableBorderNew" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class="tableHeadingNew"><td>Add Charges</td><td align="right" style="color: red" id="minimumApplied"></td></tr>
                            <tr style="padding-top:3px;">
                            <table width="100%">
                                <tr>
                                    <td align="left" width="100%" style="padding-top: 10px;">
                                        <table border="0" width="100%">
                                            <tr class="textlabelsBold">
                                                <td >Cost Type</td>
                                                <td id="unitType">Unit </td>
                                                <td >Charge Code Desc</td>
                                                <td >Charge Code</td>
                                                <td >Currency</td>
                                                <td >Cost</td>
                                                <td id="percentMarkUp" style="display:none;">%MarkUp</td>
                                                <td id="markUp1">Sell</td>
                                                <td id="spotRateLable" style="display:none;">Spot Cost</td>
                                                <c:if test="${chargeCode ne 'OCNFRT'}">
                                                    <td id="zeroCost" style="display:none;">Zero Cost</td>
                                                </c:if>
                                                <td id="standardRateLable" style="display:none"> Standard Cost</td>
                                            <tr>
                                                <td><html:select property="costType" value="<%=costType%>" onchange="getcosttype('${markup}')" style="width:150px;"
                                                             styleClass="textlabelsBoldForTextBox" styleId="costType">
                                                        <html:optionsCollection name="costtypelist"/>
                                                    </html:select></td>
                                                <td id="unitSelect"><html:select property="unitSelect" styleId="unitSelect" value="<%=unitType%>"
                                                             style="width:110px;" styleClass="textlabelsBoldForTextBox"><html:optionsCollection name="unitTypeList"/></html:select></td>
                                                <td><html:select property="chargeCodeDesc" onchange="getChargeCode(this)" value="<%=chargeCodeDesc%>" style="width:183px;" styleClass="textlabelsBoldForTextBox">
                                                        <html:optionsCollection name="costcodelist"/></html:select></td>
                                                <td><html:text property="chargeCode"  styleId="chargeCode" value="<%=chargeCode%>" size="7" readonly="true" styleClass="BackgrndColorForTextBox" tabindex="-1" disabled="true" /></td>

                                                <td><html:select property="currency" style="width:53px;" disabled="true" tabindex="-1" styleClass="BackgrndColorForTextBox">
                                                        <c:forEach var="defaultcurrency" items="${defaultcurrency}" >
                                                    <option value="${defaultcurrency.value}" <c:if test="${defaultcurrency.label=='USD'}">
                                                            selected</c:if> >${defaultcurrency.label}</option>
                                                </c:forEach>
                                            </html:select></td>
                                        <c:choose>
                                            <c:when test="${chargeFlag=='I'}">
                                            <td><html:text property="amount" value="0.00" maxlength="8" styleId="amount" styleClass="textlabelsBoldForTextBox"
                                                       size="7" onchange="checkForNumberAndDecimal(this)"/></td>
                                            </c:when>
                                            <c:otherwise>
                                            <td><html:text property="amount" value="<%=amount%>" maxlength="8" styleId="amount" styleClass="textlabelsBoldForTextBox"
                                                       size="7" onchange="checkForNumberAndDecimal(this)"/></td>
                                            </c:otherwise>
                                        </c:choose>
                                    <td id="percentMarkUp1"  style="display: none;">
                                        <input type="text"  value="${amountForMarkup}" id="percentage" size="5" maxlength="8" class="textlabelsBoldForTextBox">
                                        <span onmouseover="tooltip.show('<strong>Calculate </strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/calc.png"  onclick="getMarkUp('${markup}', '${amountForMarkup}', '${minimumForIntermodal}', '${interchargecode}')"/>
                                        </span>
                                    </td>
                                    <c:choose>
                                        <c:when test="${chargeFlag=='I' || chargeFlag=='M'}">
                                            <td ><html:text property="markUp" onchange="checkForNumberAndDecimal(this)"  value="<%=markUp%>" size="7" maxlength="8"  styleId="markUp" styleClass="textlabelsBoldForTextBox"/></td>
                                        </c:when><c:otherwise>
                                            <td ><html:text property="markUp" onchange="checkForNumberAndDecimal(this)"  value="<%=markUp%>" size="7" maxlength="8" disabled="true" styleId="markUp" tabindex="-1" styleClass="BackgrndColorForTextBox"/></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td style="white-space: nowrap"><html:text property="spotRateAmt" style="display:none;" onchange="checkForNumberAndDecimalForSpot(this)"  value="<%=spotRateAmt%>" size="7" maxlength="8" disabled="false" styleId="spotRateAmt" styleClass="textlabelsBoldForTextBox"/>
                                    </td>
                                    <c:if test="${chargeCode ne 'OCNFRT'}">
                                        <td id="spotRateChkTd" style="display:none;" align="center">
                                            <input type="checkbox" name="spotRateChk" id="spotRateChk"
                                                   onclick="setZeroSpot()"/>
                                        </td>
                                    </c:if>

                                    <td id="standardChkTd" style="display:none;" align="center">
                                        <input type="checkbox" name="standardChk" id="standardChk"
                                               onclick="setStandardSpot()"/>

                                    </td>
                            </table>
                            <table  border="0" width="95%" style="padding-top:20px;">
                                <tr class="textlabelsBold">
                                    <td id="text1" width="6%">
                                        Vendor Name<br>(Carrier<span id="defaultCarrier"><html:checkbox property="defaultCarrier" value="Y" styleId="defaultCarrierY"
                                                       onclick="getDefaultCarrier()"></html:checkbox></span>
                                            <span>None<input type="checkbox" name="defaultCarrier" id="nvoCheckBox"  value="N" onclick="disableVendorCheckBox()" />)</span>
                                        </td>
                                        <td id="vendorName1">
                                            <input type="text" size="26" name="vendorName"  id="vendorName" class="textlabelsBoldForTextBox"
                                                   onkeydown="getNvoNameAndNumber()" />
                                            <input name="custname_check" id="custname_check"   type="hidden" />
                                            <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initAutocomplete("vendorName", "custname_choices", "vendorNumber", "custname_check",
                                                        "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=5&acctTyp=V", "checkForDisable()");
                                        </script>
                                    </td>
                                    <td id="text2" style="padding-left:5px;">Vendor Number</td>
                                    <td id="vendorNumber1">
                                        <input name="vendorNumber" Class="BackgrndColorForTextBox"
                                               size="11"  readonly="readonly" tabindex="-1" id="vendorNumber"/>
                                    </td>
                                    <td align="right">Comment</td>
                                    <td>
                                        <textarea rows="4" cols="39" name="comment" id="comment" value=""
                                                  class="textlabelsBoldForTextBox"   style="text-transform: uppercase;"
                                                  onkeypress="return testCommentsLength('<%=comment.replaceAll("(\r\n|\r|\n|\n\r)", "\t")%>', this, 460)"></textarea>
                                    </td>
                                    <html:hidden property="oldCost" styleId="oldCost" value="<%=amount%>"/>
                                    <html:hidden property="oldSell" styleId="oldSell"  value="<%=markUp%>"/>
                                    <html:hidden property="oldVendor" styleId="oldVendor"/>
                                    <html:hidden property="oldVendorName" styleId="oldVendorName"/>
                                    <html:hidden property="oldComment" styleId="oldComment"  value="<%=comment%>"/>
                                    <html:hidden property="rampCheck"/>
                                </tr>
                                <%
                                if (CommonFunctions.isNotNull(comment) && comment.contains(").")) {%>
                                <tr class="textlabelsBold">
                                    <td colspan="3" align="right" valign="top">Previous Comments</td>
                                    <td  colspan="3" align="right" valign="top">
                                        <div class="commentScrollForDiv">
                                            <table border="0" >
                                                <%
                                                    String[] arrys = comment.split("\\).");
                                                    int size = (null != arrys) ? arrys.length - 1 : 0;
                                                    int label = 1;
                                                    com.gp.cong.logisoft.util.DBUtil dUtil = new com.gp.cong.logisoft.util.DBUtil();
                                                    for (int i = size; i >= 0; i--, label++) {
                                                        String resultData = arrys[i];
                                                        resultData = dUtil.getData(resultData, 65);
                                                %>
                                                <tr class="textlabelsSmallBold">
                                                    <td><%=resultData + ")."%></td>
                                                </tr>
                                                <%
                                                }%>

                                            </table></div>
                                    </td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td align="center" colspan="10" style="padding-top: 10px;">
                                        <%if (buttonValue.equals("edit")) {%>
                                    <input type="button" value="Update" id="quotesChargeUpdate" onclick="getEdit('<%=userName%>', '<%=todaysDate%>','${nonManualCharges}')" style="width: 65px;" class="buttonStyleNew"/>
                                        <input type="button" value="Cancel"  onclick="parent.parent.GB_hide();" style="width: 65px;" class="buttonStyleNew"/>
                                        <%} else {%>
                                        <input type="button" value="Submit" id="submitCharges" onclick="getAccept('${markup}', '<%=userName%>', '<%=todaysDate%>')" style="width: 65px;" class="buttonStyleNew"/>
                                        <input type="button" value="Cancel"  onclick="closeChargeDiv()" style="width: 65px;" class="buttonStyleNew"/>
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                            <td></tr>
                        </table>
                    </tr></table>
            </div>
        </c:when>
        <c:when test="${param.ratedOption == 'NonRated' || ratedOption == 'NonRated'}">
            <div id="addCharge" style="display:none">
                <table class="tableBorderNew" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="tableHeadingNew"><td>Add Charges</td><td align="right" style="color: red" id="minimumApplied"></td></tr>
                    <tr style="padding-top:3px;">
                    <table width="100%">
                        <tr>
                            <td align="left" width="100%" style="padding-top: 10px;">
                                <table border="0" width="100%">
                                    <tr class="textlabelsBold">
                                        <td >Cost Type</td>
                                        <td id="unitType">Unit </td>
                                        <td>Charge Code Desc</td>
                                        <td >Charge Code</td>
                                        <td >Currency</td>
                                        <td >Cost</td>
                                        <td id="percentMarkUp" style="display:none;">%MarkUp</td>
                                        <td id="markUp1">Sell</td>
                                        <%--<td id="minimum1">Minimum</td>
                                        --%></tr>
                                    <tr>
                                        <td><html:select property="costType" value="<%=costType%>" onchange="getcosttype('${markup}')" style="width:150px;"
                                                     styleClass="textlabelsBoldForTextBox" styleId="costType">
                                                <html:optionsCollection name="costtypelist"/>
                                            </html:select></td>
                                        <td><html:select property="unitSelect" styleId="unitSelect" value="<%=unitType%>"
                                                     style="width:110px;" styleClass="dropdown_accounting mandatory"><html:optionsCollection name="unitTypeList"/></html:select></td>
                                        <td><html:select property="chargeCodeDesc" onchange="getChargeCode(this)" value="<%=chargeCodeDesc%>" style="width:183px;" styleClass="textlabelsBoldForTextBox">
                                                <html:optionsCollection name="costcodelist"/></html:select></td>
                                        <td><html:text property="chargeCode"  styleId="chargeCode" value="<%=chargeCode%>" size="7" readonly="true" styleClass="BackgrndColorForTextBox" tabindex="-1" disabled="true" /></td>

                                        <td><html:select property="currency" style="width:53px;" disabled="true" tabindex="-1" styleClass="BackgrndColorForTextBox">
                                                <c:forEach var="defaultcurrency" items="${defaultcurrency}" >
                                            <option value="${defaultcurrency.value}" <c:if test="${defaultcurrency.label=='USD'}">
                                                    selected</c:if> >${defaultcurrency.label}</option>
                                        </c:forEach>
                                    </html:select></td>
                            <td><html:text property="amount" value="<%=amount%>" maxlength="8" styleId="amount" styleClass="textlabelsBoldForTextBox"
                                       size="7" onchange="checkForNumberAndDecimal(this)"/></td>
                            <td id="percentMarkUp1"  style="display: none;">
                                <input type="text"  value="${amountForMarkup}" id="percentage" size="5" maxlength="8" class="textlabelsBoldForTextBox">
                                <span onmouseover="tooltip.show('<strong>Calculate </strong>', null, event);" onmouseout="tooltip.hide();">
                                    <img src="<%=path%>/img/icons/calc.png"  onclick="getMarkUp('${markup}', '${amountForMarkup}', '${minimumForIntermodal}', '${interchargecode}')"/>
                                </span>
                            </td>

                            <td ><html:text property="markUp" onchange="checkForNumberAndDecimal(this)"  value="<%=markUp%>" size="7" maxlength="8"  styleId="markUp" styleClass="textlabelsBoldForTextBox"/></td>
                            <%--<td ><html:text property="minimum" value="<%=minimum%>" styleId="minimum" size="15"/></td>
                            --%></tr>
                    </table>
                    <table  border="0" width="95%" style="padding-top:20px;">
                        <tr class="textlabelsBold">
                            <td id="text1" width="6%">
                                Vendor Name<br>(Carrier<span id="defaultCarrier"><html:checkbox property="defaultCarrier" value="Y" styleId="defaultCarrierY"
                                               onclick="getDefaultCarrier()"></html:checkbox></span>
                                    <!--                                            <span>None<input type="checkbox" name="nvoCheckBox" id="nvoCheckBox"  value="N" onclick="disableVendorCheckBox()" />)</span>-->
                                    <span>None<input type="checkbox" name="defaultCarrier" id="nvoCheckBox"  value="N" onclick="disableVendorCheckBox()" />)</span>
                                </td>
                                <td id="vendorName1">
                                    <input type="text" size="26" name="vendorName"  id="vendorName" class="textlabelsBoldForTextBox"
                                           onkeydown="getNvoNameAndNumber()" />
                                    <input name="custname_check" id="custname_check"   type="hidden" />
                                    <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("vendorName", "custname_choices", "vendorNumber", "custname_check",
                                                "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=5&acctTyp=V", "checkForDisable()");
                                </script>
                            </td>
                            <td id="text2" style="padding-left:5px;">Vendor Number</td>
                            <td id="vendorNumber1">
                                <input name="vendorNumber" Class="BackgrndColorForTextBox"
                                       size="11"  readonly="readonly" tabindex="-1" id="vendorNumber"/>
                            </td>
                            <td align="right">Comment</td>
                            <td>
                                <textarea rows="4" cols="39" name="comment" id="comment" value=""
                                          class="textlabelsBoldForTextBox"   style="text-transform: uppercase;"
                                          onkeypress="return testCommentsLength('<%=comment%>', this, 460)"></textarea>
                            </td>
                            <html:hidden property="oldCost" styleId="oldCost" value="<%=amount%>"/>
                            <html:hidden property="oldSell" styleId="oldSell"  value="<%=markUp%>"/>
                            <html:hidden property="oldVendor" styleId="oldVendor"/>
                            <html:hidden property="oldVendorName" styleId="oldVendorName"/>
                            <html:hidden property="oldComment" styleId="oldComment"  value="<%=comment%>"/>
                            <html:hidden property="rampCheck"/>
                        </tr>
                        <%
                        if (CommonFunctions.isNotNull(comment) && comment.contains(").")) {%>
                        <tr class="textlabelsBold">
                            <td colspan="3" align="right" valign="top">Previous Comments</td>
                            <td  colspan="3" align="right" valign="top">
                                <div class="commentScrollForDiv">
                                    <table border="0" >
                                        <%
                                            String[] arrys = comment.split("\\).");
                                            int size = (null != arrys) ? arrys.length - 1 : 0;
                                            int label = 1;
                                            com.gp.cong.logisoft.util.DBUtil dUtil = new com.gp.cong.logisoft.util.DBUtil();
                                            for (int i = size; i >= 0; i--, label++) {
                                                String resultData = arrys[i];
                                                resultData = dUtil.getData(resultData, 65);
                                        %>
                                        <tr class="textlabelsSmallBold">
                                            <td><%=resultData + ")."%></td>
                                        </tr>
                                        <%
                                        }%>

                                    </table></div>
                            </td>
                        </tr>
                        <%}%>
                        <tr>
                            <td align="center" colspan="10" style="padding-top: 10px;">
                                <%if (buttonValue.equals("edit")) {%>
                            <input type="button" value="Update" id="quotesChargeUpdate" onclick="getEdit('<%=userName%>', '<%=todaysDate%>','${nonManualCharges}')" style="width: 65px;" class="buttonStyleNew"/>
                                <input type="button" value="Cancel"  onclick="parent.parent.GB_hide();" style="width: 65px;" class="buttonStyleNew"/>
                                <%} else {%>
                                <input type="button" value="Submit" id="submitCharges" onclick="getAccept('${markup}', '<%=userName%>', '<%=todaysDate%>')" style="width: 65px;" class="buttonStyleNew"/>
                                <input type="button" value="Cancel"  onclick="closeChargeDiv()" style="width: 65px;" class="buttonStyleNew"/>
                                <%}%>
                            </td>
                        </tr>
                    </table>
                    <td></tr>
                </table>
            </tr>
        </table>
    </div>
</c:when>
<c:otherwise>
    <div id="addCharge" style="display:none">
        <table  width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr class="tableHeadingNew">
                <td colspan="9">Add Charges</td>
            </tr>
            <tr class="textlabelsBold">
                <td>Cost Type</td>
                <td>Charge Code Desc</td>
                <td >Charge Code</td>
                <td>Currency</td>
                <td id="costId" style="visibility:  hidden">Cost</td>
                <td id="sellId" style="visibility: hidden">Sell</td>
            </tr>
            <tr class="textlabelsBold">
                <td><html:select property="costType" value="<%=costType%>" onchange="selectCostType()" style="width:150px;"
                             styleClass="textlabelsBoldForTextBox" styleId="costType">
                        <html:optionsCollection name="costtypelist"/>
                    </html:select></td>
                <td><html:select property="chargeCodeDesc" onchange="returnChargeCode(this)" value="<%=chargeCodeDesc%>" style="width:183px;" styleClass="textlabelsBoldForTextBox">
                        <html:optionsCollection name="costcodelist"/></html:select></td>
                <td><html:text property="chargeCode"  styleId="chargeCode" value="<%=chargeCode%>" size="7" readonly="true" styleClass="BackgrndColorForTextBox" tabindex="-1" disabled="true" /></td>
                <td><html:select property="currency" style="width:53px;" disabled="true" tabindex="-1" styleClass="BackgrndColorForTextBox">
                        <c:forEach var="defaultcurrency" items="${defaultcurrency}" >
                    <option value="${defaultcurrency.value}" <c:if test="${defaultcurrency.label=='USD'}">
                            selected</c:if> >${defaultcurrency.label}</option>
                </c:forEach>
            </html:select></td>
            <td ><html:text property="amount"  maxlength="8" styleId="amount" styleClass="textlabelsBoldForTextBox"
                size="7" onchange="checkForNumberAndDecimal(this)" value="<%=amount%>"/></td>
            <td ><html:text property="markUp" onchange="checkForNumberAndDecimal(this)"  value="<%=markUp%>" size="7" maxlength="8"  styleId="markUp" styleClass="textlabelsBoldForTextBox"/></td>
            </tr>
        </table>
        <table  border="0" width="100%" id="unitsId" style="display: none">
            <tr class="textlabelsBold"  id="unitsHeadingId">
                <td>Unit</td>
                <td>Cost</td>
                <td>Sell</td>
                <td></td>
            </tr>
            <c:set var="index" value="0"/>
            <c:forTokens items="${selectedUnits}" delims="," var="unit">
                <c:if test="${not empty unit}">
                    <c:set var="unitSize" value="${fn:substring(unit,0,1)}"/>
                    <c:if test="${unit == 'E=45102'}">
                        <c:set var="unitSize" value="G"/>
                    </c:if>
                    <tr>
                        <td ><html:text property="unitSelect${unitSize}"  maxlength="8" styleId="unitSelect${unitSize}" styleClass="textlabelsBoldForTextBoxDisabledLook"
                                   size="7"  value="${unit}" readonly="true" tabindex="-1"/></td>
                        <td ><html:text property="amount${unitSize}"  maxlength="8" styleId="amount${unitSize}" styleClass="textlabelsBoldAmount"
                            size="7" onchange="checkForNumberAndDecimal(this)" value="<%=amount%>"/></td>
                        <td ><html:text property="markUp${unitSize}" onchange="checkForNumberAndDecimal(this)"  value="<%=markUp%>" size="7" maxlength="8"  styleId="markUp${unitSize}" styleClass="textlabelsBoldMarkUp"/>&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:if test="${index != 0}">
                                <input type="checkbox" name="repeat${unitSize}" id="repeat${unitSize}" onclick="repeatLast(this)" onmouseover="tooltip.show('<strong>Repeat Last </strong>', null, event);" onmouseout="tooltip.hide();"/>
                            </c:if>
                        </td>
                        <c:set var="index" value="${index+1}"/>
                    <tr>
                    </c:if>
                </c:forTokens>
        </table>
        <table  border="0" width="95%">
            <tr class="textlabelsBold">
                <td id="text1" width="6%">
                    Vendor Name<br>(Carrier<span id="defaultCarrier"><html:checkbox property="defaultCarrier" value="Y" styleId="defaultCarrierY"
                                   onclick="getDefaultCarrier()"></html:checkbox></span>
                        <span>None<input type="checkbox" name="defaultCarrier" id="nvoCheckBox"  value="N" onclick="disableVendorCheckBox()" />)</span>
                    </td>
                    <td id="vendorName1">
                        <input type="text" size="26" name="vendorName"  id="vendorName" class="textlabelsBoldForTextBox"
                               onkeydown="getNvoNameAndNumber()" />
                        <input name="custname_check" id="custname_check"   type="hidden" />
                        <div id="custname_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("vendorName", "custname_choices", "vendorNumber", "custname_check",
                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=5&acctTyp=V", "checkForDisable()");
                    </script>
                </td>
                <td id="text2" style="padding-left:5px;">Vendor Number</td>
                <td id="vendorNumber1">
                    <input name="vendorNumber" Class="BackgrndColorForTextBox"
                           size="11"  readonly="readonly" tabindex="-1" id="vendorNumber"/>
                </td>
                <td align="right">Comment</td>
                <td>
                    <textarea rows="4" cols="39" name="comment" id="comment" value=""
                              class="textlabelsBoldForTextBox"   style="text-transform: uppercase;"
                              onkeypress="return testCommentsLength('<%=comment%>', this, 460)"></textarea>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="10" style="padding-top: 10px;">
                    <input type="button" value="Submit"  id="submitCharges" onclick="getAccept('${markup}', '<%=userName%>', '<%=todaysDate%>')" style="width: 65px;" class="buttonStyleNew"/>
                    <input type="button" value="Cancel"  onclick="closeChargeDiv()" style="width: 65px;" class="buttonStyleNew"/>
                </td>
            </tr>
        </table>
    </div>
</c:otherwise>
</c:choose>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<input type="hidden" name="hazmat" value="<%=hazmat%>">
<input type="hidden" name="spcleqpmt" value="<%=spclEquipment%>">
<input type="hidden" name="breakBulk" value="<%=breakBulk%>">
<input type="hidden" name="quoteNo" value="<%=quoteNo%>">
<input type="hidden" name="ratedOption" value="<%=ratedOption%>">
<input type="hidden" name="fileNo" value="${fileNo}">
<input type="hidden" name="id" value="${id}">
<input type="hidden" name="markup" value="${markup}">
<html:hidden property="forMarkUp"/>
<html:hidden property="rampCheck"/>
<script type="text/javascript">getload('<%=costType%>', '<%=buttonValue%>', '${nonManualCharges}', '<%=newFlag%>');</script>
</html:form>
</body>
<script>setFocus('${markup}', '${interchargecode}');
    <%if (buttonValue.equals("edit")) {%>
    showPopUp();
    <%}%>
</script>
<script>readyOnlyFields();</script>
<script>changeSelectBoxOnViewMode();</script>
<script>checkDefaultCarrier();</script>
</html>


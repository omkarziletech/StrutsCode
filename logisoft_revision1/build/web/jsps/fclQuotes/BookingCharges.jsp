
<%@ page language="java" pageEncoding="ISO-8859-1"
         import="org.apache.struts.util.LabelValueBean,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.BookingfclUnits,java.text.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>

<bean:define id="localDrayage" type="String">
    <bean:message key="localDrayage" />
</bean:define>
<bean:define id="interModal" type="String">
    <bean:message key="interModal" />
</bean:define>
<bean:define id="inland" type="String">
    <bean:message key="inland" />
</bean:define>
<bean:define id="minimumForIntermodal" type="String">
    <bean:message key="minimumForIntermodal" />
</bean:define>
<style type="text/css">
    #addCharge {
        position: fixed;
        _position: absolute;
        z-index: 99;
        border-style:solid solid solid solid;
        background-color: #FFFFFF;
        left: 2%;
        top: 10%;
        _height: expression(document.body.offsetHeight-200 + "px");
    }
</style>
<%    //This is used for appending date and user information to comment through java script
    User user = new User();
    String userName = "";
    Date date = new Date();
    DateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
        userName = user.getLoginName();
    }
    String todaysDate = format1.format(date);
    //--request for the value from ApplicationResources.properties file----
    request.setAttribute("minimumForIntermodal", minimumForIntermodal);
    //--to get fileNo---
    request.setAttribute("fileNo", request.getParameter("fileNo"));
    String importFlag = (null != request.getParameter("importFlag") && request.getParameter("importFlag").equals("true") ? "importNavigation" : "exportNavigation");
    request.setAttribute("importFlag", importFlag);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    HashMap hashMap = new HashMap();
    DBUtil dbUtil = new DBUtil();
    request.setAttribute("unitTypeList", dbUtil.uniTypeList1(hashMap));
    BookingfclUnits b1 = new BookingfclUnits();
    request.setAttribute("defaultcurrency", dbUtil.getGenericFCL(
            new Integer(32), "yes"));
    String unitType = "";
    String number = "1";
    String chargeCode = "";
    String chargeCodeDesc = "";
    String costType = "";
    String currency = "";
    String amount = "0.00";
    String button = "";
    String minimum = "0.00";
    String bookingNo = "";
    String hazmat = "";
    String soc = "";
    String markUp = "0.00";
    String spcleqpmt = "", comment = "", ratedOption = "";
    String buttonValue = "", vendorName = "", vendorNo = "";
    String newFlag = "";
    String invoiceNumber = "";
    String vendorCheckBox = "";
    String spotRateAmt = "";
    String spotRateChk = "";
    String standardRateChk = "";
    if (request.getAttribute("buttonValue") != null) {
        buttonValue = (String) request.getAttribute("buttonValue");
    }
    if (request.getParameter("bkgNo") != null) {
        bookingNo = request.getParameter("bkgNo");
    }
    if (request.getAttribute("bkgNo") != null) {
        bookingNo = (String) request.getAttribute("bkgNo");
    }
    if (request.getParameter("hazmat") != null) {
        hazmat = request.getParameter("hazmat");
    }
    if (request.getAttribute("hazmat") != null) {
        hazmat = (String) request.getAttribute("hazmat");
    }
    if (request.getParameter("spcleqpmt") != null) {
        spcleqpmt = request.getParameter("spcleqpmt");
    }
    if (request.getAttribute("spcleqpmt") != null) {
        spcleqpmt = (String) request.getAttribute("spcleqpmt");
    }
    String breakBulk = "";
    if (request.getParameter("breakBulk") != null
            && !request.getParameter("breakBulk").equals("")) {
        breakBulk = request.getParameter("breakBulk");
    }
    if (request.getAttribute("breakBulk") != null
            && !request.getAttribute("breakBulk").equals("")) {
        breakBulk = (String) request.getAttribute("breakBulk");
    }
    NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
    if (request.getParameter("button") != null) {
        if (session.getAttribute("newBooking") != null) {
            session.removeAttribute("newBooking");
        }
        button = request.getParameter("button");
        session.setAttribute("search", button);
    }
    if (session.getAttribute("newBooking") != null) {
        b1 = (BookingfclUnits) session.getAttribute("newBooking");
        if (b1.getUnitType() != null) {
            unitType = b1.getUnitType().getId().toString();
        }
        if (b1.getNumbers() != null) {
            number = b1.getNumbers();
        }
        if (b1.getChargeCode() != null
                && b1.getChargeCode().getId() != null) {
            chargeCode = b1.getChargeCode().getCode();
            chargeCodeDesc = b1.getChargeCode().getId().toString();
            request.setAttribute("chargeCode", chargeCode);
        }
        if (b1.getCosttype() != null
                && b1.getCosttype().getId() != null) {
            costType = b1.getCosttype().getId().toString();
        }
        if (b1.getCurrency() != null) {
            currency = b1.getCurrency();
        }
        if (b1.getInvoiceNumber() != null) {
            invoiceNumber = b1.getInvoiceNumber();
        }
        request.setAttribute("standardRate", null!=b1.getAmount()?b1.getAmount():0d);
        standardRateChk=b1.getStandardChk();
        if (b1.getAmount() != null && !"DOCUM".equals(chargeCode)) {
            amount = numberFormat.format(b1.getAmount());
        }
        if ("INSURE".equals(chargeCode)) {
            request.setAttribute("chargeCodeFlag", "I");
        }
        if (b1.getMinimum() != null) {
            minimum = numberFormat.format(b1.getMinimum());
        }
        if (b1.getNewFlag() != null) {
            newFlag = b1.getNewFlag();
        } else {
            newFlag = "";
        }
        if ((newFlag.equals("new")) && (b1.getMarkUp() != null)) {
            markUp = numberFormat.format(b1.getMarkUp());
        } else {
            double markUp1=0d;
            double amount1=0d;
            markUp1 = (b1.getMarkUp() != null) ? b1.getMarkUp() : 0.00;
            amount1 = (b1.getAmount() != null) ? b1.getAmount() : 0.00;
            if(b1.getSpotRateAmt()!=null){
                markUp1 = (b1.getSpotRateMarkUp() != null) ? b1.getSpotRateMarkUp() : 0.00;
                amount1 = (b1.getSpotRateAmt() != null) ? b1.getSpotRateAmt() : 0.00;
            }
            markUp = numberFormat.format(markUp1 + amount1);
        }
        if (null != amount) {
            request.setAttribute("amount", amount);
        } else {
            request.setAttribute("amount", "0.00");
        }
        if (null != b1.getSpotRateAmt()) {
            spotRateAmt = numberFormat.format(b1.getSpotRateAmt());
        }
        spotRateChk = b1.getSpotRateChk();
        if (null != markUp) {
            request.setAttribute("markUp", markUp);
        } else {
            request.setAttribute("markUp", "0.00");
        }
        if (b1.getComment() != null) {
            String temp = b1.getComment();
            int i = 0;
            if (!temp.equals("")) {
                i = temp.indexOf("|");
                if (i != -1) {
                    comment = temp.substring(0, i);
                } else {
                    comment = temp;
                }
                request.setAttribute("comment", comment);
            }
        }
        if (b1.getAccountName() != null) {
            vendorName = b1.getAccountName();
        }
        if (b1.getAccountNo() != null) {
            vendorNo = b1.getAccountNo();
        }
        if (b1.getVendorCheckBox() != null) {
            vendorCheckBox = b1.getVendorCheckBox();
        }
    }
    String path1 = "";
    if (request.getAttribute("path1") != null) {
        path1 = (String) request.getAttribute("path1");
    }
    String msg = "";
    if (request.getAttribute("msg") != null) {
        msg = (String) request.getAttribute("msg");
    }

    if (request.getAttribute("provisions") != null&& ((String) request.getAttribute("provisions")).equalsIgnoreCase("localDrayage")) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getGenericCodeCostListForLocalDrayage(new Integer(36), "yes", "Select Cost Code");
        request.setAttribute("costcodelist", list);
        request.setAttribute("SpecialProvision", "localDrayage");
        //---for chargeCode---
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        //---localDrayage value from .properties file---
        request.setAttribute("amountForMarkup", localDrayage);
    } else if (request.getAttribute("provisions") != null && ((String) request.getAttribute("provisions")).equalsIgnoreCase("interModal")) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getGenericCodeCostListInterModal(new Integer(36), "yes", "Select Cost Code");
        request.setAttribute("costcodelist", list);
        request.setAttribute("SpecialProvision", "interModal");
        //---for chargeCode---
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        //---interModal value from .properties file---
        request.setAttribute("amountForMarkup", interModal);
    } else if (request.getAttribute("provisions") != null && ((String) request.getAttribute("provisions")).equalsIgnoreCase("inland")) {
        String desc = importFlag.equals("importNavigation") ? "deliv" : "yes";
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getGenericCodeCostListInland(new Integer(36), desc, "Select Cost Code");
        request.setAttribute("costcodelist", list);
        request.setAttribute("SpecialProvision", "inland");
        //---for chargeCode---
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        //---interModal value from .properties file---
        request.setAttribute("amountForMarkup", inland);
    } else if (request.getAttribute("provisions") != null && ((String) request.getAttribute("provisions")).equalsIgnoreCase("Intermodal Ramp")) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForLocalDrayage(new Integer(37), "yes", "Select Cost type"));
        List list = dbUtil.getGenericCodeCostListIntrampMod(new Integer(36), "yes", "Select Cost Code");
        request.setAttribute("costcodelist", list);
        LabelValueBean bean = (LabelValueBean) list.get(0);
        chargeCode = dbUtil.getCostCodeForDrayage(bean.getValue());
        request.setAttribute("SpecialProvision", "inland");
        request.setAttribute("amountForMarkup", inland);
    } else if (breakBulk != null && breakBulk.equals("Y")) {
        request.setAttribute("costtypelist", dbUtil.getGenericCodeCostListForQuoteChargeForBreakBulk(new Integer(37), "yes", "Select Cost type"));
        request.setAttribute("costcodelist", dbUtil.getGenericChargeCostList(new Integer(36), "yes", "Select Cost Code", (String) request.getAttribute("importFlag")));
    } else if (null == request.getAttribute("provisions")
            || (null != request.getAttribute("provisions") && !((String) request.getAttribute("provisions")).equalsIgnoreCase("onCarriage"))) {
        // anything except oncarriage
        request.setAttribute("costcodelist", dbUtil.getGenericChargeCostList(new Integer(36), "yes", "Select Cost Code", (String) request.getAttribute("importFlag")));
        request.setAttribute("costtypelist", dbUtil
                .getGenericCodeCostListForQuoteCharge(new Integer(37),
                        "yes", "Select Cost type"));
    } else {
        chargeCode = (String) request.getAttribute("chargeCode");
    }
    if (null != request.getParameter("ratedOption")) {
        ratedOption = request.getParameter("ratedOption");
    } else if (null != request.getAttribute("ratedOption")) {
        ratedOption = request.getParameter("ratedOption");
    }
    request.setAttribute("ratedOption", ratedOption);
    if (null != request.getParameter("ratedOption") && request.getParameter("ratedOption").equals("NonRated") && !"Y".equals(breakBulk)) {
        if (null != request.getAttribute("costtypelist")) {
            request.setAttribute("costtypelist", dbUtil.getCodeCostListForNonRated(((List) request.getAttribute("costtypelist"))));
        }
    }

    if (request.getAttribute("closePopUp") != null && request.getAttribute("closePopUp").equals("closePopUp")) {
        if (session.getAttribute("displayBookingList") != null) {
            session.removeAttribute("displayBookingList");
        }
        if (session.getAttribute("hashMap") != null) {
            session.removeAttribute("hashMap");
        }
        request.setAttribute("breakBulk", breakBulk);
%>
<script>
    parent.parent.dontAddInsureToCharges();
    parent.parent.GB_hide();
</script>
<%
    }

    if (request.getAttribute("buttonvalue") != null && request.getAttribute("buttonvalue").equals("completed")) {
%>
<script>
    parent.parent.refreshPage();
    parent.parent.GB_hide();
</script>
<%
} else if (request.getAttribute("buttonvalue") != null && request.getAttribute("buttonvalue").equals("dontAddInsure")) {
%>
<script>
    parent.parent.dontAddInsureToCharges();
    parent.parent.GB_hide();
</script>
<%
    }
    currency = "11281";
%>
<html>
    <head>
        <title>JSP for QuoteChargeForm form</title>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script type="text/javascript"	src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript"	src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript"	src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <%@include file="../includes/baseResources.jsp"%>

        <script language="javascript" type="text/javascript">
    var standardRate = '${standardRate}';
    function closeChargeDiv() {
        document.getElementById("addCharge").style.display = 'none';
        closePopUp();
    }
    function addCharge() {
        document.getElementById("addCharge").style.display = 'block';
        showPopUp();
    }
    function checkDefaultCarrier() {
        if ("<%=vendorCheckBox%>" == "Y") {
            document.getElementById("default").checked = true;
            document.getElementById("nvoCheckBox").checked = false;
            document.getElementById('vendorNumber').readOnly = true;
            document.getElementById('vendorNumber').tabIndex = -1;
            document.getElementById('vendorName').readOnly = true;
            document.getElementById('vendorName').tabIndex = -1;
            document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById('vendorNumber').tabIndex = "-1";
            document.getElementById('vendorName').tabIndex = "-1";
        } else if ("<%=vendorCheckBox%>" == "N") {
            document.getElementById("nvoCheckBox").checked = true;
            document.getElementById("default").checked = false;
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
    function accept1(val, ev1, ev2) {
        var chargeCode = document.bookingChargesForm.chargeCode.value;
        if (val == 'localDrayage' || val == 'interModal' || val == 'inland') {
            document.bookingChargesForm.tempButton.value = "closePopUp";
        } else {
            document.bookingChargesForm.tempButton.value = "dontClose";
        }
        if (document.bookingChargesForm.costType.value == 0) {
            alertNew("please select the Cost Type");
            return;
        }
        if (document.bookingChargesForm.costType.selectedIndex == "3" || ${ratedOption eq 'NonRated' && breakBulk ne 'Y'}) {
            if (document.bookingChargesForm.unitSelect.value == 0) {
                alertNew("Please Select The Unit Type");
                return;
            }
        }
        if (document.bookingChargesForm.chargeCode.value == '') {
            alertNew("please select the Charge Code");
            return;
        }
        var cost = parseFloat(document.bookingChargesForm.amount.value);
        var sell = parseFloat(document.bookingChargesForm.markUp.value);
        if (document.getElementById('amount').value == 0.00
                || document.getElementById('amount').value == ""
                || document.getElementById('amount').value == 0.0
                || document.getElementById('amount').value == 0) {
            cost = parseFloat("0.00");
        }
        if (document.getElementById('markUpValueId').value == 0.00
                || document.getElementById('markUpValueId').value == ""
                || document.getElementById('markUpValueId').value == 0.0
                || document.getElementById('markUpValueId').value == 0) {
            sell = parseFloat("0.00");
        }

        if ((document.bookingChargesForm.amount.disabled == false) && (document.bookingChargesForm.markUp.disabled == false)) {
            if (sell < cost) {
                alertNew("Sell always greater than or equals to Cost");
                return;
            }
        }

        if (!document.getElementById("nvoCheckBox").checked && document.getElementById('vendorName').value == '' && chargeCode != 'INSURE' && chargeCode != 'ADMIN' && chargeCode != 'DOCUM') {
            alertNew("Please enter Vendor Name");
            return;
        }
        appendUserInfoForComments(document.getElementById('comment'), ev1, ev2);
        var commentValue = document.getElementById('comment').value.trim();
        commentValue = document.getElementById('previousComments').value + commentValue;
        document.bookingChargesForm.comment.value = commentValue;
        //check common.js for the function
        document.bookingChargesForm.buttonValue.value = "accept";
        document.bookingChargesForm.submit();
    }
    function getChargeCode(chargeDesc) {
        var chargecode = document.bookingChargesForm.chargeCodeDesc;
        var costType = document.bookingChargesForm.costType;
        var bookingId = '<%=bookingNo%>';

        //--ADVFF & ADVSHP charges can be added only for PER BL----
        if (costType[costType.selectedIndex].text != 'PER BL CHARGES' && (chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - FF'
                || chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - SHR')) {
            alertNew("ADVANCE CHGS - SHR and ADVANCE CHGS - FF charges can be added only for PER BL CHARGES");
            document.bookingChargesForm.chargeCodeDesc.value = "";
            document.bookingChargesForm.chargeCode.value = "";
            return;
        }
        if (chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - FF' || chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - SHR'
                || chargecode[chargecode.selectedIndex].text == 'PBA') {
            if (parent.parent.document.EditBookingsForm.prepaidToCollect[1].checked) {
                if (chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - FF' && document.getElementById('vendorName').value == '') {
                    var forworderName = parent.parent.EditBookingsForm.fowardername.value;
                    if (forworderName == "" || forworderName == "NO FF ASSIGNED"
                            || forworderName == "NO FF ASSIGNED / B/L PROVIDED"
                            || forworderName == "NO FRT. FORWARDER ASSIGNED") {
                        document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                        alertNew("Forwarder Cannot be Empty or No FF ASSIGNED Please update details in Shipper Forwarder Consignee tab");
                        return;
                    }
                } else if (chargecode[chargecode.selectedIndex].text == 'ADVANCE CHGS - SHR' && document.getElementById('vendorName').value == '') {
                    if (parent.parent.EditBookingsForm.shipperName.value == "") {
                        document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                        alertNew("Please enter shipper details in Shipper Forwarder Consignee tab");
                        return;
                    }
                }
            } else {
                document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                alertNew("Booking should be collect to add these charges ");
                return false;
            }
        }
        chargecode = chargecode.value;
        var unitTypeId = "0";
        if (document.bookingChargesForm.unitSelect != null) {
            unitTypeId = document.bookingChargesForm.unitSelect.value;
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getChargeByChargeDescId",
                param1: chargeDesc.value,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                document.bookingChargesForm.chargeCode.value = data.code;
                if (data.code === 'DEFER') {
                    setSteamshiplinebooking();
                } else {
                    document.getElementById('vendorName').value = '';
                    document.getElementById('vendorNumber').value = '';
                    document.getElementById('vendorName').readOnly = false;
                    document.getElementById('vendorName').tabIndex = 0;
                }
            }
        });
        if (document.bookingChargesForm.chargeCode.value == 'ADVSHP') {
            var shipperNumber = parent.parent.EditBookingsForm.shipper.value;
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "getCustomerAccountNo",
                    param1: shipperNumber,
                    dataType: "json"
                },
                success: function (data) {
                    if (data.acctType != null && data.acctType.indexOf("V") == -1) {
                        alertNew('Shipper must also be a Vendor to add This charge');
                        document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                        document.bookingChargesForm.chargeCode.value = "";
                        return false;
                    }
                }
            });
        }                  // ....checking for the existing charge code.....
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkChargeCode",
                param1: bookingId,
                param2: chargecode,
                param3: unitTypeId
            },
            async: false,
            success: function (data) {
                if (data != null && data != '') {
                    flag = false;
                    document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                    document.bookingChargesForm.chargeCode.value = "";
                    alertNew("Please Enter the Different Charge Code or Cost Type, as this already exist");
                    return;
                }
            }
        });
        setFocus('', document.bookingChargesForm.chargeCode.value);
        setTimeout(toDisableAmountandMarkUp('', ''), 120);
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
                alertNew("The amount you entered is not a valid");
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
    var nvoCompanyName;
    var nvoCompanyNumber;
    function getload(val1, val2, val3) {
        if (document.getElementById('default').checked) {
            document.getElementById('default').checked = false;
        }
        if (val2 == 'edit') {
            if (document.bookingChargesForm.chargeCode.value == 'INSURE') {
                document.getElementById('vendorName').value = "";
                document.getElementById('vendorNumber').value = "";
                document.getElementById('vendorName').disabled = true;
                document.getElementById('costType').disabled = true;
                document.getElementById('unitSelect').disabled = true;
                document.bookingChargesForm.chargeCodeDesc.disabled = true;
                document.bookingChargesForm.vendorName.style.backgroundColor = '#CCEBFF';
                document.bookingChargesForm.costType.style.backgroundColor = '#CCEBFF';
                document.bookingChargesForm.unitSelect.style.backgroundColor = '#CCEBFF';
                document.bookingChargesForm.chargeCodeDesc.style.backgroundColor = '#CCEBFF';
                document.getElementById('vendorNumber').disabled = true;
                document.getElementById("default").disabled = true;
                document.getElementById("nvoCheckBox").disabled = true;
            } else if (document.bookingChargesForm.chargeCode.value === 'DEFER') {
                document.getElementById('vendorName').value = "<%=vendorName%>";
                document.getElementById('vendorNumber').value = "<%=vendorNo%>";
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.bookingChargesForm.unitSelect.disabled = true;
                document.bookingChargesForm.chargeCode.disabled = true;
                document.bookingChargesForm.costType.disabled = true;
                document.bookingChargesForm.chargeCodeDesc.disabled = true;
                document.getElementById("default").disabled = true;
                document.getElementById("nvoCheckBox").disabled = true;
            } else {
                document.getElementById('vendorName').value = "<%=vendorName%>";
                document.getElementById('vendorNumber').value = "<%=vendorNo%>";
                document.getElementById('oldVendorName').value = "<%=vendorName%>";
                document.getElementById('oldVendor').value = "<%=vendorNo%>";
                document.bookingChargesForm.unitSelect.disabled = true;
                document.bookingChargesForm.chargeCode.disabled = true;
                document.bookingChargesForm.costType.disabled = true;
                document.bookingChargesForm.chargeCodeDesc.disabled = true;
            }
            var spotChkStatus='<%=spotRateChk%>';
            var standRateChk='<%=standardRateChk%>';

            if (parent.parent.document.getElementById('spotRateY') && parent.parent.document.getElementById('spotRateY').checked && val3 == 'nonManualChargesBooking') {
                jQuery("#spotRateLable,#spotRateAmt,#zeroCost,#spotRateChkTd,#standardRateLable,#standardChkTd").css({
                    display: "block"
                });
            } else if (parent.parent.document.getElementById('spotRateN') && parent.parent.document.getElementById('spotRateN').checked) {
                jQuery("#spotRateLable,#spotRateAmt,#zeroCost,#spotRateChkTd,#standardRateLable,#standardChkTd").css({
                    display: "none"
                });
            }
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
        }
        if (val3 == 'nonManualChargesBooking') {
            if (null != document.bookingChargesForm.amount && document.bookingChargesForm.amount != 'undefined') {
                document.bookingChargesForm.amount.disabled = true;
            }
            if (null != document.bookingChargesForm.markUp && document.bookingChargesForm.markUp != 'undefined') {
                document.bookingChargesForm.markUp.disabled = true;
            }
            document.bookingChargesForm.vendorName.disabled = true;
            document.bookingChargesForm.vendorNumber.disabled = true;
            document.getElementById("default").disabled = true;
            document.getElementById("nvoCheckBox").disabled = true;
        } else {
            document.bookingChargesForm.amount.disabled = true;
            document.bookingChargesForm.markUp.disabled = true;
            if (document.getElementById("nvoCheckBox").checked) {
                document.getElementById('vendorNumber').readOnly = true;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorNumber').tabIndex = -1;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorNumber').tabIndex = "-1";
                document.getElementById('vendorName').tabIndex = "-1";
            } else if (document.getElementById("default").checked) {
                document.getElementById('vendorNumber').readOnly = true;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorNumber').tabIndex = -1;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorNumber').tabIndex = "-1";
                document.getElementById('vendorName').tabIndex = "-1";
            }
//        document.getElementById("default").disabled = false;
//        document.getElementById("nvoCheckBox").disabled = false;
        }

        getcosttype('${SpecialProvision}');

        //--to get nvo Company details---
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getNvo",
                dataType: "json"
            },
            success: function (data) {
                if (data != null) {
                    if (data.accountName != null && data.accountName != '') {
                        nvoCompanyName = data.accountName;
                    }
                    if (data.accountno != null && data.accountno != '') {
                        nvoCompanyNumber = data.accountno;
                    }
                }
            }
        });

        //--to check  amount/markup for each chargecodes in order to disable them based on Field7 value of genericcodedup table----
        toDisableAmountandMarkUp(val3, val2);
        populateMarkupValue('<%=markUp%>');
    }
    function getcosttype(ev) {
        if (ev == 'localDrayage' || ev == 'interModal' || ev == 'inland') {
            if (document.bookingChargesForm.costType.value == "11300") {
                document.getElementById("unitType").style.visibility = 'visible';
                document.getElementById("unitValue").style.visibility = 'visible';
            } else {
                document.getElementById("unitType").style.visibility = 'hidden';
                document.getElementById("unitValue").style.visibility = 'hidden';
                document.getElementById("unitSelect").value = 0;
            }
        } else {
            if (document.bookingChargesForm.costType.value == "11300") {
                document.getElementById("unitType").style.visibility = 'visible';
                document.getElementById("unitValue").style.visibility = 'visible';
            } else {
                document.getElementById("unitType").style.visibility = 'hidden';
                document.getElementById("unitValue").style.visibility = 'hidden';
                document.getElementById("unitSelect").value = 0;
            }
        }
    }
    function close1() {
        document.bookingChargesForm.buttonValue.value = "close";
        document.bookingChargesForm.submit();
    }
    function getVendorFromParent() {
        if (document.getElementById('default').checked) {
            var vendor = parent.parent.document.getElementById("sslDescription").value;
            var index = vendor.indexOf("//");
            var vendorName = "";
            var vendorNumber = "";
            if (index != -1) {
                vendorName = vendor.substring(0, index);
                vendorNumber = vendor.substring(index + 2, vendor.length);
            } else {
                vendorName = parent.parent.document.getElementById("sslDescription").value;
            }
            if (document.bookingChargesForm.chargeCode.value == 'INLAND') {
                if (parent.parent.document.getElementById("truckerName").value != "") {
                    vendorName = parent.parent.document.getElementById("truckerName").value;
                }
                if (parent.parent.document.getElementById("truckerCode").value != "") {
                    vendorNumber = parent.parent.document.getElementById("truckerCode").value;
                }
            }
            document.getElementById('vendorName').value = vendorName;
            document.getElementById('vendorNumber').value = vendorNumber;
            document.getElementById('custname_check1').value = vendorName;
            document.getElementById('vendorName').readOnly = true;
            document.getElementById('vendorName').tabIndex = -1;
            document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById('vendorName').tabIndex = "-1";
            document.getElementById("nvoCheckBox").checked = false;
        } else {
            var chargeCode = document.bookingChargesForm.chargeCode.value;
                if (!document.getElementById("default").checked && chargeCode==='DEFER'){
                setSteamshiplinebooking();
            }
                else{
                document.getElementById('default').checked = false;
                document.getElementById('vendorName').value = '';
                document.getElementById('vendorNumber').value = '';
                document.getElementById('vendorName').readOnly = false;
                document.getElementById('vendorName').tabIndex = 0;
                document.getElementById("default").checked = false;
                document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                document.getElementById('vendorName').tabIndex = "0";
            }
        }
    }
    function setSteamshiplinebooking(){
        var vendor = parent.parent.document.getElementById("sslDescription").value;
        var index = vendor.indexOf("//");
        var vendorName = "";
        var vendorNumber = "";
        if (index != -1) {
            vendorName = vendor.substring(0, index);
            vendorNumber = vendor.substring(index + 2, vendor.length);
        } else {
            vendorName = parent.parent.document.getElementById("sslDescription").value;
        }
        document.getElementById('vendorName').value = vendorName;
        document.getElementById('vendorNumber').value = vendorNumber;
        document.getElementById('custname_check1').value = vendorName;
        document.getElementById('vendorName').readOnly = true;
        document.getElementById('vendorName').tabIndex = -1;
        document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById('vendorName').tabIndex = "-1";
        document.getElementById("default").checked = false;
        document.getElementById("default").disabled = true;
        document.getElementById("nvoCheckBox").disabled = true;
    }
    function getMarkUp(val1, val2, minimum) {
        if (val1 == 'inland' || val1 == 'inland') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "getMarkUpValue",
                    param1: document.getElementById('amount').value,
                    param2: document.getElementById('percentage').value,
                    param3: minimum
                },
                success: function (data) {
                    populateMarkupValue(data);
                }
            });
        } else {
            return;
        }
    }
    function populateMarkupValue(data) {
        if (document.getElementById('markUpValueId')) {
            document.getElementById('markUpValueId').value = data;
        }
        var value = parseInt(data);
        var minumumAmount = '${minimumForIntermodal}';
        minumumAmount = minumumAmount.replace('$', '');
        minumumAmount = parseInt(minumumAmount);
        if (minumumAmount == value && (document.bookingChargesForm.chargeCode.value == 'INTMDL' || document.bookingChargesForm.chargeCode.value == 'INLAND')) {
            document.getElementById('minimumApplied').innerHTML = 'Min Charge Applied';
        } else {
            document.getElementById('minimumApplied').innerHTML = '';
        }
    }
    function setFocus(ev, chargeCode) {
        if (chargeCode != null && chargeCode.trim() == 'ADVFF') {
            document.getElementById('vendorName').value = parent.parent.EditBookingsForm.fowardername.value;
            document.getElementById("vendorNumber").value = parent.parent.EditBookingsForm.forwarder.value;
        }
        if (chargeCode != null && chargeCode.trim() == 'ADVSHP') {
            document.getElementById('vendorName').value = parent.parent.EditBookingsForm.shipperName.value;
            document.getElementById("vendorNumber").value = parent.parent.EditBookingsForm.shipper.value;
        }
        if (ev != 'undefined' && (ev == 'interModal' || ev == 'Intermodal' || ev == 'inland')) {
            document.getElementById('percentMarkUp').style.display = 'block';
            document.getElementById('percentMarkUp1').style.display = 'block';
        }
        setTimeout("set()", 150);
    }
    function set() {
        document.getElementById('unitValue').focus();
    }
    function updateCharges(ev1, ev2, ev3) {
        var amount = null != document.bookingChargesForm.amount ? document.bookingChargesForm.amount.value : "0.00";
        var amountNew = amount.replace(/\,/g, "");
        var cost = parseFloat(amountNew);
        var sell = null != document.bookingChargesForm.markUp ? document.bookingChargesForm.markUp.value : "0.00";
        var sellnew = sell.replace(/\,/g, "");
        sellnew = parseFloat(sellnew);
        var spotAmt = jQuery("#spotRateAmt").val();
        if ((null != document.bookingChargesForm.amount && document.bookingChargesForm.amount.disabled == false) && (null != document.bookingChargesForm.markUp && document.bookingChargesForm.markUp.disabled == false)) {
            if (sellnew < cost) {
                alertNew("Sell always greater than or equals to Cost");
                return;
            }
        }
        if (parent.parent.document.getElementById('spotRateY') && parent.parent.document.getElementById('spotRateY').checked && ev3 == 'nonManualChargesBooking') {
            if (jQuery("#spotRateAmt") && spotAmt === "") {
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
        appendUserInfoForComments(document.getElementById('comment'), ev1, ev2);
        var commentValue = document.getElementById('comment').value.trim();
        commentValue = document.getElementById('previousComments').value + commentValue;
        document.bookingChargesForm.comment.value = commentValue;

        //check common.js for the function
        jQuery("#markUpValueId").attr('disabled',false).attr('readOnly',false);
        jQuery("#amount").attr('disabled',false).attr('readOnly',false);
        if (document.getElementById('costType').value == '11317') {
            confirmNew("Do you want to apply the changes for all the container?", "updateToAll");
        } else {
            if ('${amount}' != amount || '${markUp}' != sell) {
                document.bookingChargesForm.buttonValue.value = "update";
            } else {
                document.bookingChargesForm.buttonValue.value = "updateWithOutInsure";
            }
            document.bookingChargesForm.submit();
        }
    }
    function validateAmount(obj) {
        if (obj.value.indexOf(".") >= 0) {
            var num = obj.value.split(".");
            alert(num.length);
            if (num.length > 2) {

            }
        }
    }

    function costSellValidation(obj) {
        var str = obj.value;
        var n = str.substr(str.indexOf("."));
        if (n.length > 3 && str.indexOf(".") != -1) {
            obj.value = obj.value.substring(0, str.indexOf(".") + 3).trim();
        } else {
            return obj.value;
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
    function getDefaultCarrier() {
        if (document.getElementById("default").checked) {
            document.getElementById('vendorName').value = parent.parent.document.getElementById('ssline').value;
            //document.getElementById('custname_check1').value = document.getElementById('vendorName').value;
            document.getElementById('vendorNumber').value = parent.parent.document.getElementById('ssline').value;
            document.getElementById('vendorName').readOnly = true;
            document.getElementById('vendorName').tabIndex = -1;
            document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById("nvoCheckBox").checked = false;
        } else {
            document.getElementById('vendorName').value = "";
            document.getElementById('vendorNumber').value = "";
            document.getElementById('vendorName').readOnly = false;
            document.getElementById('vendorName').tabIndex = 0;
            document.getElementById("default").checked = false;
            document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
        }
    }
    function disableVendorCheckBox() {
        if (document.getElementById("nvoCheckBox").checked) {
            document.getElementById("default").checked = false;
            document.getElementById('vendorName').readOnly = true;
            document.getElementById('vendorName').tabIndex = -1;
            document.getElementById("vendorName").value = "";
            document.getElementById("vendorNumber").value = "";
            document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById('vendorName').tabIndex = "-1";
        } else {
            var chargeCode = document.bookingChargesForm.chargeCode.value;
                if (!document.getElementById("nvoCheckBox").checked && chargeCode==='DEFER'){
                setSteamshiplinebooking();
                }else{
                document.getElementById("vendorName").value = "";
                document.getElementById("vendorNumber").value = "";
                document.getElementById('vendorName').readOnly = false;
                document.getElementById('vendorName').tabIndex = 0;
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
    //---part of Confirm function of New Alertbox -----
    function confirmMessageFunction(id1, id2) {
        var amount = null != document.bookingChargesForm.amount ? document.bookingChargesForm.amount.value : "0.00";
        var sell = null != document.bookingChargesForm.markUp ? document.bookingChargesForm.markUp.value : "0.00";
        if (id1 == 'updateToAll' && id2 == 'ok') {
            parent.parent.call('insurance');
            if ('${amount}' != amount || '${markUp}' != sell) {
                document.bookingChargesForm.buttonValue.value = "updateAll";
            } else {
                document.bookingChargesForm.buttonValue.value = "updateAllWithOutInsure";
            }
            document.bookingChargesForm.submit();
        } else if (id1 == 'updateToAll' && id2 == 'cancel') {
            parent.parent.call('insurance');
            if ('${amount}' != amount || '${markUp}' != sell) {
                document.bookingChargesForm.buttonValue.value = "update";
            } else {
                document.bookingChargesForm.buttonValue.value = "updateWithOutInsure";
            }
            document.bookingChargesForm.submit();
        }
    }
    function checkForDisableAgentVendor() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: document.getElementById("vendorNumber").value
            },
            success: function (data) {
                if (data != "") {
                    alertNew(data);
                    document.getElementById("vendorName").value = "";
                    document.getElementById("vendorNumber").value = "";
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("vendorNumber").value,
                            dataType: "json"
                        },
                        success: function (data) {
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
        if (data.acctType != null) {
            type = data.acctType;
            array1 = type.split(",");
            if (null != data.acctType && array1.length > 0 && array1.contains("V")) {
                document.getElementById("vendorNumber").value = data.acctNo;
            } else {
                alertNew("Select the customers with Account Type V");
                document.getElementById("vendorName").value = "";
                document.getElementById("vendorNumber").value = "";
            }
        }
    }
    function toDisableAmountandMarkUp(val1, val2) {
        //--to check  amount/markup for each chargecodes in order to disable them based on Field7 value of genericcodedup table----
        var chargeCode = document.bookingChargesForm.chargeCode.value;
        if (val1 == '') {
            document.bookingChargesForm.markUp.disabled = false;
            document.bookingChargesForm.amount.disabled = false;
            document.bookingChargesForm.markUp.style.backgroundColor = 'white';
            document.bookingChargesForm.amount.style.backgroundColor = 'white';
            if (chargeCode != '') {
                if (chargeCode == 'INSURE' || chargeCode == 'ADMIN' || chargeCode == 'DOCUM') {
                    document.bookingChargesForm.amount.disabled = true;
                    document.bookingChargesForm.amount.style.backgroundColor = '#CCEBFF';
                    document.bookingChargesForm.vendorName.disabled = true;
                    document.bookingChargesForm.vendorNumber.disabled = true;
                    document.getElementById("default").disabled = true;
                    document.getElementById("nvoCheckBox").disabled = true;
                } else if ((document.bookingChargesForm.chargeCode.value == 'FFCOMM') && val2 == 'edit') {
                    document.bookingChargesForm.amount.disabled = true;
                    document.bookingChargesForm.amount.style.backgroundColor = '#CCEBFF';
                    document.bookingChargesForm.markUp.disabled = true;
                    document.bookingChargesForm.markUp.style.backgroundColor = '#CCEBFF';
                    document.bookingChargesForm.vendorName.disabled = true;
                    document.bookingChargesForm.vendorNumber.disabled = true;
                    document.getElementById("default").disabled = true;
                    document.getElementById("nvoCheckBox").disabled = true;
                } else {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "checkAmountMarkup",
                            param1: document.bookingChargesForm.chargeCode.value
                        },
                        success: function (data) {
                            if (data != '') {
                                if (data == 'C') {
                                    document.bookingChargesForm.markUp.disabled = true;
                                    document.bookingChargesForm.markUp.style.backgroundColor = '#CCEBFF';
                                } else if (data == 'S') {
                                    document.bookingChargesForm.amount.disabled = true;
                                    document.bookingChargesForm.amount.style.backgroundColor = '#CCEBFF';
                                } else if (data == 'CS') {
                                    //---allow both amount and markup to enter---
                                }
                            }
                        }
                    });
                }
                var importFlag = '${importFlag}';
                var shipmentType;
                if (importFlag == 'importNavigation') {
                    shipmentType = "FCLI";
                } else {
                    shipmentType = "FCLE";
                }
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "checkCostCodeInGeneralLedger",
                        param1: document.bookingChargesForm.chargeCode.value,
                        param2: shipmentType,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data) {
                            if (val2 != 'edit') {
                                document.bookingChargesForm.chargeCodeDesc.selectedIndex = 0;
                                document.bookingChargesForm.chargeCode.value = "";
                                alertNew("No Cost code set up in the General Ledger Charges");
                                document.bookingChargesForm.amount.value = "";
                            }
                            document.bookingChargesForm.amount.disabled = true;
                            document.bookingChargesForm.amount.style.backgroundColor = '#CCEBFF';
                        }
                    }
                });
            }
        }
    }
    function getClose() {
        document.bookingChargesForm.buttonValue.value = "closeSave";
        document.bookingChargesForm.submit();
    }
    function saveToBooking() {
        if (parent.parent.document.EditBookingsForm.rampCheck.checked) {
            document.bookingChargesForm.rampCheck.value = 'on';
        } else {
            document.bookingChargesForm.rampCheck.value = 'off';
        }
        document.bookingChargesForm.buttonValue.value = "SaveInTobooking";
        document.bookingChargesForm.submit();
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
        </script>
    </head>

    <div id="cover" style="width: 906px ;height: 1000px;"></div>
    <body class="whitebackgrnd">

        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;">
                <b>Alert</b>
            </p>
            <p id="innerText" class="containerForAlert"
               style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>

        <div id="ConfirmBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;">
                <b>Confirmation</b>
            </p>
            <p id="innerText1" class="containerForAlert"
               style="width: 100%;padding-left: 3px;">

            </p>
            <form
                style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button" class="buttonStyleForAlert" value="OK"
                       onclick="yes()">
                <input type="button" class="buttonStyleForAlert" value="Cancel"
                       onclick="No()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <html:form action="/bookingCharges" scope="request">
            <input type="hidden" value="<%=comment.replace("\"", "&#39;")%>" id="previousComments"/>
            <font color="blue"
                  style="font-family:monospace;font-size: medium;font-weight:bolder;"
                  size="4"><%=msg%>
            </font>
            <table class="tableBorderNew" width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr class="tableHeadingNew"><td>Charges </td><td><span style="float:right"><input type="button" value="Add Charge" onclick="addCharge();" class="buttonStyleNew"/>&nbsp;
                            <%List otherChargesList = (List) session.getAttribute("displayBookingList");%>
                            <%if (null == otherChargesList || otherChargesList.isEmpty()) {%>
                            <input type="button" value="Cancel" onclick="close1();" class="buttonStyleNew"/>
                            <%}%></span></td>
                </tr><tr>
                    <td>
                        &nbsp;File No:
                        <font color="red">&nbsp;<c:out value="${fileNo}"></c:out>
                            </font>
                        </td>
                    </tr>

                    <tr style="padding-top:3px;">
                        <td>
                        <%if (buttonValue.equals("edit")) { %>
                        <div id="addCharge">
                            <%} else {%>
                            <div id="addCharge" style="display:none">
                                <%}%>
                                <table width="100%" cellpadding="1" cellspacing="1">
                                    <tr class="tableHeadingNew"><td>Add Charges</td><td colspan="10"  align="right" style="color: red" id="minimumApplied"></td></tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            Cost Type
                                        </td>
                                        <td id="unitType">
                                            Unit
                                        </td>
                                        <td>
                                            Charge Code Desc
                                        </td>
                                        <td>
                                            Charge Code
                                        </td>
                                        <td>
                                            Currency
                                        </td>
                                        <td>
                                            Cost
                                        </td>
                                        <td id="percentMarkUp" style="display:none;">
                                            %MarkUp
                                        </td>
                                        <td id="markUpId">
                                            Sell
                                        </td>
                                        <td id="spotRateLable" style="display:none;">Spot Cost</td>
                                        <c:if test="${chargeCode ne 'OCNFRT'}">
                                            <td id="zeroCost" style="display:none;">Zero Cost&nbsp;</td>
                                        </c:if>
                                        <td id="standardRateLable" style="display:none"> Standard Cost</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <html:select property="costType"
                                                         styleClass="dropdown_accounting"
                                            value="<%=costType%>" style="width:150px;"
                                                         onchange="getcosttype('${SpecialProvision}')"
                                                         styleId="costType">
                                                <html:optionsCollection name="costtypelist" />
                                            </html:select>
                                        </td>
                                        <c:choose>
                                            <c:when test="${ratedOption eq 'NonRated'}">
                                                <td id="unitValue">
                                                    <html:select property="unitSelect"
                                                                 styleClass="dropdown_accounting mandatory"
                                                    value="<%=unitType%>" style="width:110px;" styleId="unitSelect">
                                                        <html:optionsCollection name="unitTypeList" />
                                                    </html:select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td id="unitValue">
                                                    <html:select property="unitSelect"
                                                                 styleClass="dropdown_accounting"
                                                    value="<%=unitType%>" style="width:110px;" styleId="unitSelect">
                                                        <html:optionsCollection name="unitTypeList" />
                                                    </html:select>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>
                                            <html:select property="chargeCodeDesc"
                                                         styleClass="dropdown_accounting"
                                            onchange="getChargeCode(this)" value="<%=chargeCodeDesc%>"
                                                         style="width:200px;">
                                                <html:optionsCollection name="costcodelist" />
                                            </html:select>
                                        </td>
                                        <td>
                                            <html:text property="chargeCode" styleId="chargeCode" readonly="true" styleClass="BackgrndColorForTextBox"
                                            value="<%=chargeCode%>" size="11" tabindex="-1" />
                                        </td>
                                        <td>
                                            <html:select property="currency"  tabindex="-1"   disabled="true"
                                                         styleClass="BackgrndColorForTextBox"
                                            value="<%=currency%>" style="width:70px;">
                                                <html:optionsCollection name="defaultcurrency" />
                                            </html:select>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${chargeCodeFlag=='I'}">
                                                    <input type="text" id="collapseAmount" class="BackgrndColorForTextBox" value="0.00" onchange="checkForNumberAndDecimal(this)"
                                                           size="8" maxlength="8" disabled readonly/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${not empty collapseCostAmount}">
                                                            <input type="text" id="collapseAmount" class="textlabelsBoldForTextBox" value="${collapseCostAmount}" onchange="checkForNumberAndDecimal(this)"
                                                                   size="8" maxlength="8" disabled readonly/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="amount" styleId="amount"
                                                            styleClass="textlabelsBoldForTextBox" value="<%=amount%>"
                                                                       size="8" maxlength="8" onchange="checkForNumberAndDecimal(this)" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td id="percentMarkUp1" style="display:none;">
                                            <input type="text" Class="textlabelsBoldForTextBox" maxlength="8"
                                                   value="${amountForMarkup}" id="percentage" size="8" >
                                            <span onmouseover="tooltip.show('<strong>Calculate </strong>', null, event);" onmouseout="tooltip.hide();">
                                                <img src="<%=path%>/img/icons/calc.png"  onclick="getMarkUp('${SpecialProvision}', '${amountForMarkup}', '${minimumForIntermodal}')"/>
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty collapseSellAmount}">
                                                    <input type="text" id="collapseSellAmount" class="textlabelsBoldForTextBox" value="${collapseSellAmount}" onchange="checkForNumberAndDecimal(this)"
                                                           size="8" maxlength="8" disabled readonly/>
                                                </c:when>
                                                <c:otherwise>
                                                    <html:text property="markUp" maxlength="8" onchange="checkForNumberAndDecimal(this)"
                                                               styleClass="textlabelsBoldForTextBox" styleId="markUpValueId"
                                                    value="<%=markUp%>" size="8" />
                                                </c:otherwise>
                                            </c:choose>
                                        <td style="white-space: nowrap"><html:text property="spotRateAmt" style="display:none;" onchange="checkForNumberAndDecimalForSpot(this)"  value="<%=spotRateAmt%>" size="7" maxlength="8" styleId="spotRateAmt" styleClass="textlabelsBoldForTextBox"/>
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
                                        </td>
                                    </tr>
                                </table>
                                <table width="85%">
                                    <tr class="textlabelsBold">
                                        <td id="text1" width="6%">
                                            Vendor Name<br>(Carrier<span id="defaultCarrier">
                                                <html:checkbox property="vendorCheckBox" styleId="default" value="Y" onclick="getVendorFromParent()"></html:checkbox></span>
                                                <span>None<input type="checkbox" name="vendorCheckBox" id="nvoCheckBox"  value="N" onclick="disableVendorCheckBox()" />)</span>
                                            </td>
                                            <td>
                                                <input name="vendorName" Class="textlabelsBoldForTextBox"
                                                       id="vendorName" size="29" onkeydown="getNvoNameAndNumber()" />
                                                <input name="custname_check1" id="custname_check1"
                                                       type="hidden" value="Y"/>
                                                <div id="custname_choices1" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("vendorName", "custname_choices1", "vendorNumber", "custname_check1",
                                                            "<%=path%>/actions/tradingPartner.jsp?tabName=BOOKING&from=8&isDojo=false&acctTyp=V", "checkForDisableAgentVendor()");
                                            </script>
                                        </td>
                                        <td style="padding-left:20px;">
                                            Vendor Number
                                        </td>
                                        <td>
                                            <input name="vendorNumber" Class="BackgrndColorForTextBox" id="vendorNumber"
                                                   size="13" maxlength="20" readonly="readonly" tabindex="-1"/>
                                        </td>
                                        <td style="padding-left:20px;" >
                                            Comment
                                        </td>
                                        <td style="padding-left:5px;">
                                            <textarea name="comment" id="comment"
                                                      class="textlabelsBoldForTextBox" rows="4" cols="39"
                                                      style="text-transform: uppercase;"
                                                      onkeypress="return testCommentsLength('<%=comment.replaceAll("(\r\n|\r|\n|\n\r)", "\t")%>', this, 460)" ></textarea>
                                        </td>
                                        <html:hidden property="oldCost" styleId="oldCost" value="<%=amount%>"/>
                                        <html:hidden property="oldSell" styleId="oldSell"  value="<%=markUp%>"/>
                                        <html:hidden property="oldVendor" styleId="oldVendor"/>
                                        <html:hidden property="oldVendorName" styleId="oldVendorName"/>
                                        <html:hidden property="oldComment" styleId="oldComment"  value="<%=comment%>"/>
                                        <html:hidden property="rampCheck" />
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            Invoice Number
                                        </td>
                                        <td>
                                            <input name="invoiceNumber" Class="textlabelsBoldForTextBox"
                                                   id="invoiceNumber" size="24" maxlength="30" value="<%=invoiceNumber%>">
                                        </td>
                                    </tr>
                                    <%
                                        if (CommonFunctions.isNotNull(comment) && comment.contains(").")) {%>
                                    <tr class="textlabelsBold">
                                        <td colspan="3" align="right" >

                                            Previous Comments</td>
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
                                                        } %>


                                                    <%} %>
                                                    <tr class="textlabels">
                                                        <td colspan="7" align="center" style="padding-top: 10px;">
                                                            <%
                                                                if (buttonValue.equals("edit")) {
                                                            %>
                                                            <input type="button" value="Update"
                                                                   onclick="updateCharges('<%=userName%>', '<%=todaysDate%>', '${nonManualChargesBooking}')"
                                                                   style="width:65px;" class="buttonStyleNew" />
                                                            <input type="button" value="Cancel" onclick="closeChargeDiv()"
                                                                   style="width:65px;" class="buttonStyleNew" />
                                                            <%
                                                            } else {
                                                            %>
                                                            <input type="button" value="Add" class="buttonStyleNew"
                                                                   onclick="accept1('${SpecialProvision}', '<%=userName%>', '<%=todaysDate%>')" />
                                                            <input type="button" value="Cancel" class="buttonStyleNew"
                                                                   onclick="closeChargeDiv()" />
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>
                                                </table></div>
                                        </td>
                                    </tr></table>
                            </div>
                            <tr>
                                <td align="left" colspan="7" style="padding-top: 10px;">
                                    <div id="divtablesty1"
                                         style="border:thin;overflow:scroll;width:100%;height:80%">
                                        <table width="80%" border="0" cellpadding="0" cellspacing="0">
                                            <%
                                                if ((otherChargesList != null && otherChargesList.size() > 0)) {
                                                    int l = 0;
                                                    String unitTypes = "";
                                                    BookingfclUnits b = new BookingfclUnits();
                                            %>
                                            <display:table name="<%=otherChargesList%>"
                                                           class="displaytagstyleNew" id="lclcoloadratestable" sort="list"
                                            style="width:90%" pagesize="<%=pageSize%>">
                                                <%
                                                    b = (BookingfclUnits) otherChargesList.get(l);
                                                    if (b.getUnitType() != null) {
                                                        unitTypes = b.getUnitType().getCodedesc();
                                                    }
                                                %>
                                                <display:setProperty name="paging.banner.some_items_found">
                                                    <span class="pagebanner"> <font color="blue">{0}</font>
                                                        <br> </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.one_item_found">
                                                    <span class="pagebanner"> One {0} displayed. Page
                                                        Number </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.all_items_found">
                                                    <span class="pagebanner"> {0} {1} Displayed, Page
                                                        Number </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="basic.msg.empty_list">
                                                    <span class="pagebanner"> No Records Found. </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.placement"
                                                                     value="bottom" />
                                                <display:setProperty name="paging.banner.item_name"
                                                                     value="FCL BUY Rates" />
                                                <display:setProperty name="paging.banner.items_name"
                                                                     value="FCL BUY Rates" />
                                                <display:column title="Unit Type">
                                                    <%=unitTypes%>
                                                </display:column>
                                                <display:column title="Charge Code" property="chgCode"></display:column>
                                                <display:column title="CostType" property="costType"></display:column>
                                                <display:column title="Currency" property="currency">
                                                </display:column>
                                                <display:column title="Cost" property="amount"></display:column>
                                                <display:column title="Sell" property="markUp"></display:column>

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
                                                <input type="button" align="center" value="SaveToBooking" style="width: 100px"
                                                       onclick="saveToBooking();" class="buttonStyleNew"/>
                                                <input type="button" align="center" value="Cancel" onclick="getClose()" style="max-width:120px" class="buttonStyleNew"/></td></tr></table>
                                </td><%}%>
                            </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" />
            <html:hidden property="bookingNo" value="<%=bookingNo%>" />
            <html:hidden property="hazmat" value="<%=hazmat%>" />
            <html:hidden property="soc" value="<%=soc%>" />
            <input type="hidden" name="ratedOption" value="<%=ratedOption%>">
            <html:hidden property="spcleqpmt" value="<%=spcleqpmt%>" />
            <html:hidden property="tempButton" />
            <html:hidden property="rampCheck" />
            <input type="hidden" name="breakBulk" value="<%=breakBulk%>">
            <input type="hidden" name="id" value="${id}">
            <input type="hidden" name="provisions" value="${provisions}">
        </html:form>
        <script type="text/javascript">getload('<%=costType%>', '<%=buttonValue%>', '${nonManualChargesBooking}');</script>
        <script>setFocus('${SpecialProvision}', '<%=chargeCode%>');</script>
        <script>changeSelectBoxOnViewMode();</script>
        <script>checkDefaultCarrier();</script>
    </body>

</html>


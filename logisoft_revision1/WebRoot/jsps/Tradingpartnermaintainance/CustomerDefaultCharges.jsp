<%@ page language="java" import="com.gp.cvst.logisoft.util.DBUtil,java.text.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<html>
    <%        DBUtil dbUtil = new DBUtil();
        HashMap hashMap = new HashMap();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        Date date = new Date();
        String todaysDate = df.format(date);
        String previousComment = "";
        if (null != request.getAttribute("previousComment")) {
            previousComment = (String) request.getAttribute("previousComment");
        }
        request.setAttribute("costcodelist", dbUtil.getGenericChargeCostList(new Integer(36), "yes", "Select Cost Code", ""));
        request.setAttribute("unitTypeList", dbUtil.uniTypeList1(hashMap));
        request.setAttribute("costtypelist", dbUtil.getCodeCostListForDefaultCustomer(new Integer(37), "yes", "Select Cost type"));
    %>
    <head>
        <title>JSP for default charges</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp" %>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
    </head>
    <body class="whitebackgrnd" >
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>
        <!--DESIGN FOR NEW Confirm BOX ---->
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
        <html:form action="/customerDefaultCharges" styleId="quoteChargeId" scope="request">
            <div id="addCharge">
                <table class="tableBorderNew" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr class="tableHeadingNew">
                        <td>Add Charges</td>
                        <td align="right" style="color: red" id="minimumApplied"></td>
                    </tr>
                    <tr style="padding-top:3px;">
                    <table width="100%">
                        <tr>
                            <td align="left" width="100%" style="padding-top: 10px;">
                                <table border="0" width="100%">
                                    <tr class="textlabelsBold">
                                        <td >Cost Type</td>
                                        <td id="unitTypeLabel" style="visibility: hidden">Unit </td>
                                        <td >Charge Code Desc</td>
                                        <td >Charge Code</td>
                                        <td >Currency</td>
                                        <td >Cost</td>
                                        <td id="markUp1">Sell</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <html:select property="costType" value="${charges.costType.id}" onchange="getcosttype()" style="width:150px;"
                                                         styleClass="textlabelsBoldForTextBox" styleId="costType" >
                                                <html:optionsCollection name="costtypelist"/>
                                            </html:select>
                                        </td>
                                        <td>
                                            <c:set var="showUnit" value="hidden"/>
                                            <c:if test="${not empty charges.unitType.id}">
                                                <c:set var="showUnit" value="visible"/>
                                            </c:if>
                                            <html:select property="unitType" styleId="unitType" value="${charges.unitType.id}"
                                                         style="width:110px;visibility: ${showUnit}" styleClass="textlabelsBoldForTextBox">
                                                <html:optionsCollection name="unitTypeList"/>
                                            </html:select>
                                        </td>
                                        <td>
                                            <html:select property="chargeCodeDesc" onchange="getChargeCode(this)" value="${charges.chargeCodeDesc.id}" style="width:183px;" styleClass="textlabelsBoldForTextBox">
                                                <html:optionsCollection name="costcodelist"/>
                                            </html:select>
                                        </td>
                                        <td>
                                            <html:text property="chargeCode"  styleId="chargeCode"  size="7" readonly="true" styleClass="BackgrndColorForTextBox" tabindex="-1" disabled="true" value="${charges.chargeCode}"/>
                                        </td>
                                        <td>
                                            <html:text property="currency"  styleId="currency" value="USD" size="7" readonly="true" styleClass="BackgrndColorForTextBox" tabindex="-1" disabled="true"/>

                                        </td>
                                        <td>
                                            <html:text property="cost" value="${charges.cost}" maxlength="8" styleId="cost" styleClass="textlabelsBoldForTextBox"
                                                       size="7" onchange="checkForNumberAndDecimal(this)"/>
                                        </td>
                                        <td>
                                            <html:text property="sell" onchange="checkForNumberAndDecimal(this)"  value="${charges.sell}" size="7" maxlength="8"  styleId="sell" styleClass="textlabelsBoldForTextBox"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <table  border="0" width="95%" style="padding-top:20px;">
                        <tr class="textlabelsBold">
                            <td id="text1" width="6%">
                                <span>Vendor Name<br>(None<html:checkbox property="defaultCarrier" styleId="nvoCheckBox"  value="N" onclick="disableVendorCheckBox(this)" />)</span>
                            </td>
                            <td id="vendorName1">
                                <html:text size="26" property="vendorName"  styleId="vendorName" styleClass="textlabelsBoldForTextBox"
                                           onkeydown="getNvoNameAndNumber()" value="${charges.vendorName}"/>
                                <input name="custname_check" id="custname_check"   type="hidden" value="${charges.vendorName}"/>
                                <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                    initAutocomplete("vendorName", "custname_choices", "vendorNumber", "custname_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=5", "checkForDisable()");
                                </script>
                            </td>
                            <td id="text2" style="padding-left:5px;">Vendor Number</td>
                            <td id="vendorNumber1">
                                <html:text property="vendorNumber" styleClass="BackgrndColorForTextBox"
                                           size="11"  readonly="readonly" tabindex="-1" styleId="vendorNumber" value="${charges.vendorNumber}"/>
                            </td>
                            <td align="right">Comment</td>
                            <td>
                                <html:textarea rows="4" cols="39" property="comment" styleId="comment" value=""
                                               styleClass="textlabelsBoldForTextBox"   style="text-transform: uppercase;"
                                               ></html:textarea>
                                </td>
                            </tr>
                        <c:if test="${not empty charges.comment}">
                            <tr class="textlabelsBold">
                                <td colspan="3" align="right" valign="top">Previous Comments</td>
                                <td  colspan="3" align="right" valign="top">
                                    <div class="commentScrollForDiv">
                                        <table border="0" >
                                            <c:forTokens var="comment" items="${charges.comment}" delims=").">
                                                <tr class="textlabelsSmallBold">
                                                    <td>${comment}).</td>
                                                </tr>
                                            </c:forTokens>
                                        </table></div>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td align="center" colspan="10" style="padding-top: 10px;">
                                <c:choose>
                                    <c:when test="${not empty charges}">
                                        <input type="button" value="Update"  onclick="updateCharge(${charges.id})" style="width: 60px;" class="buttonStyleNew"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="button" value="Add"  onclick="addCharge()" style="width: 60px;" class="buttonStyleNew"/>
                                    </c:otherwise>
                                </c:choose>
                                <input type="button" value="Cancel"  onclick="parent.parent.GB_hide();" style="width: 65px;" class="buttonStyleNew"/>
                            </td>
                        </tr>
                    </table>
                    </tr>
                </table>
            </div>
            <br>
            <table width="100%" class="tableBorderNew">
                <tr>
                    <td>
                        <div id="inbondDisplayTableId">
                            <table border="0" cellpadding="0" cellspacing="0" id="inbondDetailstable">
                                <c:set var="i" value="0"></c:set>
                                <display:table name="${chargesList}" id="charges"
                                               class="displaytagstyleNew"   pagesize="50">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"> <font color="blue">{0}</font>
                                            LineItem details displayed,For more LineItems click on page
                                            numbers. <br> </span>
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
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Charge"/>
                                    <display:setProperty name="paging.banner.items_name" value="Charges"/>
                                    <display:column title="Vendor Name" property="vendorName"></display:column>
                                    <display:column title="Charge Code" property="chargeCode"></display:column>
                                    <display:column title="Cost" property="cost"/>
                                    <display:column title="Sell" property="sell"/>
                                    <display:column title="Unit Type" property="unitType.codedesc"/>
                                    <display:column title="Cost Type" property="costType.codedesc"/>
                                    <display:column title="Action" >
                                        <img src="${path}/img/icons/delete.gif" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"
                                             onclick="deleteCharge('${charges.id}')" id="deleteimg" />
                                        <img src="${path}/img/icons/edit.gif" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();"
                                             onclick="editCharge('${charges.id}')" id="editimg" />
                                    </display:column>
                                    <c:set var="i" value="${i+1}"></c:set>
                                </display:table>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="acctNo"/>
            <input type="hidden" id="oldComment" value="${charges.comment}"/>
            <html:hidden property="action" value="${charges.comment}"/>
            <html:hidden property="id"/>
        </html:form>
        <script type="text/javascript">
            function confirmMessageFunction(id1, id2) {
                if (id1 == 'deleteCharge' && id2 == 'yes') {
                    document.customerDefaultChargesForm.action.value = "deleteCharge";
                    document.customerDefaultChargesForm.submit();
                }
            }
            function addCharge() {
                if (document.customerDefaultChargesForm.costType.value == 0) {
                    alertNew("please select the Cost Type");
                    return;
                }
                if (document.customerDefaultChargesForm.costType.value == "11300") {
                    if (document.customerDefaultChargesForm.unitType.value == 0) {
                        alertNew("please select the unit");
                        return;
                    }
                }
                if (document.customerDefaultChargesForm.chargeCode.value == '') {
                    alertNew("please select the Charge Code");
                    return;
                }
                var cost = parseFloat(document.customerDefaultChargesForm.cost.value);
                var sell = document.customerDefaultChargesForm.sell.value;
                var sellnew = sell.replace(/\,/g, "");
                sellnew = parseFloat(sellnew);
                if ((document.customerDefaultChargesForm.cost.disabled == false) && (document.customerDefaultChargesForm.sell.disabled == false)) {
                    if (sellnew < cost) {
                        alertNew("Sell always greater than or equals to Cost");
                        return;
                    }
                }
                if (!document.getElementById("nvoCheckBox").checked && document.getElementById('vendorName').value == '' && document.customerDefaultChargesForm.chargeCode.value != 'INSURE') {
                    alertNew("Please enter Vendor Name");
                    return;
                }
                appendUserInfoForComments(document.getElementById('comment'), '${loginuser.loginName}', '<%=todaysDate%>');
                var commentValue = document.getElementById('comment').value.trim();
                document.customerDefaultChargesForm.comment.value = commentValue;
                makePageEditableWhileSaving(document.getElementById("quoteChargeId"));
                document.customerDefaultChargesForm.action.value = "addCharge";
                document.customerDefaultChargesForm.submit();
            }
            function deleteCharge(id) {
                document.customerDefaultChargesForm.id.value = id;
                confirmYesOrNo("Are you sure want to delete this charge", "deleteCharge");
            }
            function editCharge(id) {
                document.customerDefaultChargesForm.action.value = "editCharge";
                document.customerDefaultChargesForm.id.value = id;
                document.customerDefaultChargesForm.submit();
            }
            function updateCharge(id) {
                if (document.customerDefaultChargesForm.costType.value == 0) {
                    alertNew("please select the Cost Type");
                    return;
                }
                if (document.customerDefaultChargesForm.costType.value == "11300") {
                    if (document.customerDefaultChargesForm.unitType.value == 0) {
                        alertNew("please select the unit");
                        return;
                    }
                }
                if (document.customerDefaultChargesForm.chargeCode.value == '') {
                    alertNew("please select the Charge Code");
                    return;
                }
                var cost = parseFloat(document.customerDefaultChargesForm.cost.value);
                var sell = document.customerDefaultChargesForm.sell.value;
                var sellnew = sell.replace(/\,/g, "");
                sellnew = parseFloat(sellnew);
                if ((document.customerDefaultChargesForm.cost.disabled == false) && (document.customerDefaultChargesForm.sell.disabled == false)) {
                    if (sellnew < cost) {
                        alertNew("Sell always greater than or equals to Cost");
                        return;
                    }
                }
                if (!document.getElementById("nvoCheckBox").checked && document.getElementById('vendorName').value == '' && document.customerDefaultChargesForm.chargeCode.value != 'INSURE') {
                    alertNew("Please enter Vendor Name");
                    return;
                }
                appendUserInfoForComments(document.getElementById('comment'), '${loginuser.loginName}', '<%=todaysDate%>');
                var commentValue = document.getElementById('comment').value.trim();
                document.customerDefaultChargesForm.comment.value = '<%=previousComment%>' + commentValue;
                makePageEditableWhileSaving(document.getElementById("quoteChargeId"));
                document.customerDefaultChargesForm.action.value = "updateCharge";
                document.customerDefaultChargesForm.id.value = id;
                document.customerDefaultChargesForm.submit();
            }
            function getcosttype() {
                if (document.customerDefaultChargesForm.costType.value == "11300") {
                    document.getElementById("unitType").style.visibility = 'visible';
                    document.getElementById("unitTypeLabel").style.visibility = 'visible';
                } else {
                    document.getElementById("unitType").style.visibility = 'hidden';
                    document.getElementById("unitTypeLabel").style.visibility = 'hidden';
                    document.getElementById("unitType").value = 0;
                }
            }
            function getChargeCode(chargeDesc) {
                if (chargeDesc.value != 0) {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                            methodName: "getChargeByChargeDescId",
                            param1: chargeDesc.value,
                            dataType: "json"
                        },
                        success: function (data) {
                            document.customerDefaultChargesForm.chargeCode.value = data.code;
                        }
                    });
                    setTimeout("toDisableAmountandMarkUp('','')", 150);
                } else {
                    document.customerDefaultChargesForm.chargeCode.value = "";
                }
                //toDisableAmountandMarkUp();
            }
            function toDisableAmountandMarkUp(val1, action) {
                document.customerDefaultChargesForm.sell.disabled = false;
                document.customerDefaultChargesForm.cost.disabled = false;
                document.customerDefaultChargesForm.sell.style.backgroundColor = 'white'
                document.customerDefaultChargesForm.cost.style.backgroundColor = 'white';
                if (document.customerDefaultChargesForm.chargeCode.value != '') {
                    if (document.customerDefaultChargesForm.chargeCode.value == 'INSURE') {
                        document.customerDefaultChargesForm.cost.disabled = true;
                        document.customerDefaultChargesForm.cost.style.backgroundColor = '#CCEBFF';
                        document.customerDefaultChargesForm.vendorName.disabled = true;
                        document.customerDefaultChargesForm.vendorNumber.disabled = true;
                        document.getElementById("defaultCarrierY").disabled = true;
                        document.getElementById("nvoCheckBox").disabled = true;
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "checkAmountMarkup",
                                param1: document.customerDefaultChargesForm.chargeCode.value
                            },
                            success: function (data) {
                                if (null != data && data != '') {
                                    if (data == 'C') {
                                        document.customerDefaultChargesForm.sell.value = "0.00";
                                        document.customerDefaultChargesForm.sell.disabled = true;
                                        document.customerDefaultChargesForm.sell.style.backgroundColor = '#CCEBFF';
                                    } else if (data == 'S') {
                                        document.customerDefaultChargesForm.cost.value = "0.00";
                                        document.customerDefaultChargesForm.cost.disabled = true;
                                        document.customerDefaultChargesForm.cost.style.backgroundColor = '#CCEBFF';
                                    }
                                }
                            }
                        });
                    }

                    var shipmentType = "FCLE";
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "checkCostCodeInGeneralLedger",
                            param1: document.customerDefaultChargesForm.chargeCode.value,
                            param2: shipmentType
                        },
                        success: function (data) {
                            if (data) {
                                if (action != 'edit') {
                                    alertNew("No Cost code set up in the General Ledger Charges");
                                    document.customerDefaultChargesForm.cost.value = "0.00";
                                }
                                document.customerDefaultChargesForm.cost.disabled = true;
                                document.customerDefaultChargesForm.cost.style.backgroundColor = '#CCEBFF';
                            }
                        }
                    });
                }
            }
            function disableVendorCheckBox(obj) {
                if (obj.checked) {
                    document.getElementById('vendorName').readOnly = true;
                    document.getElementById('vendorName').tabIndex = -1;
                    document.getElementById("vendorName").value = "";
                    document.getElementById("vendorNumber").value = "";
                    document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById('vendorName').tabIndex = "-1";
                } else {
                    document.getElementById("vendorName").value = "";
                    document.getElementById("vendorNumber").value = "";
                    document.getElementById('vendorName').readOnly = false;
                    document.getElementById('vendorName').tabIndex = 0;
                    document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                    document.getElementById('vendorName').tabIndex = "0";
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
                    success: function (data) {
                        if (data != "") {
                            alertNew(data);
                            document.getElementById("vendorNumber").value = '';
                            document.getElementById("vendorName").value = '';
                        } else {
                            jQuery.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
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
                    alertNew("The amount you entered is not a valid");
                    return;
                } else {
                    costSellValidation(obj);
                }
            }
            function makePageEditableWhileSaving(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if (element.type == "text" || element.type == "textarea") {
                        element.style.border = "1px solid #C4C5C4";
                        element.style.backgroundColor = "#FCFCFC";
                        element.readOnly = false;
                        element.tabIndex = -1;
                        element.disabled = false;
                    }
                    if (element.type == "select-one") {
                        element.style.border = 0;
                        element.disabled = false;
                    }
                    if (element.type == "checkbox" || element.type == "radio") {
                        element.disabled = false;
                    }
                }
            }
        </script>
    </body>
</html>


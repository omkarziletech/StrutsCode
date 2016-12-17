<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,java.text.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>My JSP 'AddFclBlCharges.jsp' starting page</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        </head>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script language="javascript" src="${path}/js/fclBillLanding.js"></script>
    <script language="javascript" src="${path}/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <c:if test="${not empty fclBillLaddingformForContainer}">
        <script language="javascript">
            parent.parent.getUpdatedCompleteBL();
        </script>
    </c:if>
    <c:if test="${not empty param.importFlag}">
        <c:set var="importFlag" value="${param.importFlag}"></c:set>
    </c:if>
    <script type="text/javascript">
        start = function() {
            importDisableParty();
        }
        window.onload = start;
    </script>
    <script type="text/javascript">
        function disableVendorCheckBox() {
            if (document.getElementById("nvoCheckBox").checked) {
                document.fclBillLaddingform.vendorCheckBox.checked = false;
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
            } else {
                document.getElementById("vendorName").value = "";
                document.getElementById("vendorNumber").value = "";
                document.getElementById('vendorNumber').readOnly = false;
                document.getElementById('vendorName').readOnly = false;
                document.getElementById('vendorNumber').tabIndex = 0;
                document.getElementById('vendorName').tabIndex = 0;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBox";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                document.getElementById('vendorNumber').tabIndex = "0";
                document.getElementById('vendorName').tabIndex = "0";
            }
        }
        function importDisableParty() {
            if (parent.parent.document.fclBillLaddingform.houseBL[2].checked && null !== document.getElementById('notCollapse')) {
                document.getElementById('notCollapse').disabled = false;
                document.getElementById('notCollapse').className = "textlabelsBoldForTextBox";
            } else {
                if (parent.parent.document.fclBillLaddingform.billToCode[0].checked) {
                    document.fclBillLaddingform.chargeBillTo.value = 'Forwarder';
                } else if (parent.parent.document.fclBillLaddingform.billToCode[1].checked) {
                    document.fclBillLaddingform.chargeBillTo.value = 'Shipper';
                } else if (parent.parent.document.fclBillLaddingform.billToCode[2].checked) {
                    document.fclBillLaddingform.chargeBillTo.value = 'ThirdParty';
                } else if (parent.parent.document.fclBillLaddingform.billToCode[3].checked) {
                    document.fclBillLaddingform.chargeBillTo.value = 'Consignee';
                } else if (parent.parent.document.fclBillLaddingform.billToCode[4].checked) {
                    document.fclBillLaddingform.chargeBillTo.value = 'NotifyParty';
                }
                if (document.getElementById("notCollapse").type === "select-one") {
                    var notCollapseValue = document.getElementById("notCollapse").value;
                    var notCollapse = document.getElementById("notCollapse");
                    var newNotCollapse = document.createElement('input');
                    newNotCollapse.style.width = "126px";
                    newNotCollapse.name = notCollapse.name;
                    newNotCollapse.id = notCollapse.id;
                    newNotCollapse.className = "BackgrndColorForTextBox";
                    newNotCollapse.value = notCollapseValue;
                    newNotCollapse.readOnly = true;
                    notCollapse.parentNode.replaceChild(newNotCollapse, notCollapse);
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
        function checkForDisableAgentVendor() {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("vendorNumber").value
                },
                success: function(data) {
                    if (data !== "") {
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
        function getVendorFromParent() {
            if (document.getElementById('default').checked) {
                var vendorName = parent.parent.document.getElementById("streamShipName").value;
                var vendorNumber = parent.parent.document.getElementById("sslinenumber").value;
                document.getElementById('vendorName').value = vendorName;
                document.getElementById('vendorNumber').value = vendorNumber;
                document.getElementById('vendorNumber').readOnly = true;
                document.getElementById('vendorNumber').tabIndex = -1;
                document.getElementById('vendorName').readOnly = true;
                document.getElementById('vendorName').tabIndex = -1;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBoxDisabledLook";
                document.getElementById('vendorNumber').tabIndex = "-1";
                document.getElementById('vendorName').tabIndex = "-1";
                document.fclBillLaddingform.nvoCheckBox.checked = false;
            } else {
                document.getElementById('default').checked = false;
                document.getElementById('vendorName').value = '';
                document.getElementById('vendorNumber').value = '';
                document.getElementById('vendorName').readOnly = false;
                document.getElementById('vendorNumber').readOnly = false;
                document.getElementById('vendorNumber').tabIndex = 0;
                document.getElementById('vendorName').tabIndex = 0;
                document.fclBillLaddingform.vendorCheckBox.checked = false;
                document.getElementById('vendorNumber').className = "textlabelsBoldForTextBox";
                document.getElementById('vendorName').className = "textlabelsBoldForTextBox";
                document.getElementById('vendorNumber').tabIndex = "0";
                document.getElementById('vendorName').tabIndex = "0";
            }
        }
        function addCostAndCharges(user, date, readyToPost, fileNo) {
            document.fclBillLaddingform.costAmount.disabled = false;
            if (document.getElementById('chargeCode').value === '') {
                alertNew("PLEASE ENTER CHARGECODE");
                return;
            }
            if (document.getElementById('chargeCodeDesc').value === '') {
                alertNew("PLEASE ENTER CHARGECODE DESCRIPTION");
                return;
            }
            if (document.getElementById('chargeAmount').value === '') {
                alertNew("PLEASE ENTER AMOUNT");
                return;
            }
            var sell = parseFloat(document.fclBillLaddingform.chargeAmount.value);
            var cost = parseFloat(document.fclBillLaddingform.costAmount.value);
            if (document.getElementById('chargeAmount').value === 0.00
                    || document.getElementById('chargeAmount').value === ""
                    || document.getElementById('chargeAmount').value === 0.0
                    || document.getElementById('chargeAmount').value === 0) {
                sell = parseFloat("0.00");
            }
            if (document.getElementById('costAmount').value === 0.00
                    || document.getElementById('costAmount').value === ""
                    || document.getElementById('costAmount').value === 0.0
                    || document.getElementById('costAmount').value === 0) {
                cost = parseFloat("0.00");
            }
            if (sell < cost) {
                alertNew("Sell always greater than or equals to Cost");
                return;
            }

            if (!document.fclBillLaddingform.nvoCheckBox.checked && document.getElementById('vendorName').value === '' && document.fclBillLaddingform.chargeCode.value !== 'INSURE') {
                alertNew("Please enter Vendor Name");
                return;
            }
            if (getBillToParty() && checkAddCostMappingWithGL(document.fclBillLaddingform.chargeCode.value,fileNo)) {
                appendUserInfoForComments(document.getElementById('comment'), user, date);
                var commentValue = document.getElementById('comment').value.trim();
                document.fclBillLaddingform.comment.value = commentValue;
                document.getElementById("addCharge").style.display = "none";
                if (null !== readyToPost && readyToPost === 'M') {
                    if (confirm('New AR will be created')) {
                        document.fclBillLaddingform.buttonValue.value = "addCostAndChargesInfo";
                        document.fclBillLaddingform.submit();
                    }
                } else {
                    document.fclBillLaddingform.buttonValue.value = "addCostAndChargesInfo";
                    document.fclBillLaddingform.submit();
                }
            }
        }
        function getBillToParty() {
            var chargeCode = document.getElementById('chargeCode').value;
            if (chargeCode.trim() === 'ADVSHP' || chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') {
                if ((chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') && (parent.parent.document.fclBillLaddingform.forwardingAgentName.value === "" ||
                        parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FF ASSIGNED' ||
                        parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FRT. FORWARDER ASSIGNED')) {
                    alertNew("Forwarder cannot be Empty and  NO FF ASSIGNED to add ADVFF charge");
                    return false;
                } else if (chargeCode.trim() === 'ADVSHP') {
                    var shipperNumber = '';
                    var alertMessage = '';
                    shipperNumber = parent.parent.document.fclBillLaddingform.houseShipper.value;
                    alertMessage = "Please Select Master BL Shipper before adding ADVSHP charge";
                    if (shipperNumber === '') {
                        alertNew(alertMessage);
                        return false;
                    }
                }
                if (parent.parent.document.fclBillLaddingform.houseBL[1].checked ||
                        parent.parent.document.fclBillLaddingform.houseBL[2].checked) {
                    if (parent.parent.document.fclBillLaddingform.consignee.value === '') {
                        alertNew("Please Enter Consignee name and Number in BL to Add these charges");
                        return false;
                        document.getElementById('chargeCode').value = '';
                        document.getElementById('chargeCodeDesc').value = '';
                    }

                } else {
                    alertNew("Only for Collect and Both BL you can add these charges");
                    return false;
                    document.getElementById('chargeCode').value = '';
                    document.getElementById('chargeCodeDesc').value = '';
                }
            }
            if (document.getElementById('notCollapse').value === 'Forwarder') {
                if (parent.parent.document.fclBillLaddingform.forwardingAgentName.value === "") {
                    alertNew("Please Select Forwarder before selecting Bill To Party");
                    return false;
                } else if (parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FF ASSIGNED'
                        || parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FRT. FORWARDER ASSIGNED') {
                    alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
                    return false;
                } else {
                    return true;
                }
            } else if (document.getElementById('notCollapse').value === 'Shipper') {
                if (parent.parent.document.fclBillLaddingform.houseShipper.value === "") {
                    alertNew("Please Select Master BL Shipper Before selecting Bill To Party");
                    return false;
                } else if (parent.parent.document.fclBillLaddingform.shipper.value === "") {
                    alertNew("Please Select House BL Shipper Before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.getElementById('notCollapse').value === 'ThirdParty') {
                if (parent.parent.document.fclBillLaddingform.billThirdPartyName.value === "") {
                    alertNew("Please Select Third Party before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.getElementById('notCollapse').value === 'Agent') {
                if (parent.parent.document.fclBillLaddingform.agent.value === "") {
                    alertNew("Please Select Agent before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.getElementById('notCollapse').value === 'Consignee') {
                if (parent.parent.document.fclBillLaddingform.consignee.value === "") {
                    alertNew("Please Select Consignee before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.getElementById('notCollapse').value === 'NotifyParty') {
                if (parent.parent.document.fclBillLaddingform.notifyParty.value === "") {
                    alertNew("Please Select Notify before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
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
        function checkForNumberAndDecimal(obj) {
            if (!/^-?([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
                obj.value = "";
                alertNew("The amount you entered is not a valid");
                return;
            } else {
                costSellValidation(obj);
            }
        }

        function checkChargeCode() {
            var chargeCode = document.fclBillLaddingform.chargeCode.value;
            document.fclBillLaddingform.costAmount.disabled = false;
            document.fclBillLaddingform.costAmount.style.backgroundColor = 'white';
            if (chargeCode.trim() === 'ADVSHP' || chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') {
                document.fclBillLaddingform.chargeBillTo.value = 'Consignee';
            }
            var shipperNumber = '';
            if (chargeCode === 'ADVSHP') {
                shipperNumber = parent.parent.document.fclBillLaddingform.houseShipper.value;
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "getCustomerAccountNo",
                        param1: shipperNumber,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (data.acctType !== null && data.acctType.indexOf("V") === -1) {
                            alertNew('Shipper must also be a Vendor to add This charge');
                            document.fclBillLaddingform.chargeCode.value = "";
                            document.fclBillLaddingform.chargeCodeDesc.value = "";
                            return false;
                        }
                    }
                });
            }
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkCostCodeInGeneralLedger",
                    param1: chargeCode,
                    param2: "FCLI",
                    dataType: "json"
                },
                success: function(data) {
                    if (data) {
                        alertNew("No Cost code set up in the General Ledger Charges");
                        document.fclBillLaddingform.costAmount.value = "";
                        document.fclBillLaddingform.costAmount.disabled = true;
                        document.fclBillLaddingform.costAmount.style.backgroundColor = '#CCEBFF';
                    }
                }
            });
        }
         function checkAddCostMappingWithGL(costCode,fileNo) {
        var flag = true;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkChargeAndCostMappingWithGL",
                param1: costCode,
                param2: fileNo,
                param3: 'AC',
                param4: 'BL'
            },
            async: false,
            success: function (data) {
                if (data !== "") {
                    alertNew("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                    flag = false;
                }
            }
        });
        return flag;
    }
    </script>
    <%        DBUtil dbUtil = new DBUtil();
        request.setAttribute("defaultcurrency", dbUtil.getGenericFCL1(new Integer(32)));
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        String todaysDate = format.format(date);
    %>
    <body class="whitebackgrnd" />
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
    <html:form action="/fclBillLadding"  name="fclBillLaddingform" type="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm" scope="request">
        <table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew">
                <td colspan="10">Add Charges</td>
            </tr>
            <tr class="textlabelsBold">
                <td>Charge Code Desc</td>
                <td>
                    <input type="text" name="chargeCodeDesc" maxlength="60"
                           id= "chargeCodeDesc" size="20" Class="textlabelsBoldForTextBox"
                           value="${fclBillLaddingform.chargeCodeDesc}"/>
                    <div id="chargeCodeChoices" style="display: none"class="autocomplete"></div>
                    <script type="text/javascript">
                        initOPSAutocomplete("chargeCodeDesc", "chargeCodeChoices", "chargeCode", "chargeCodeCheck",
                                "${path}/actions/autoCompleterForChargeCode.jsp?tabName=FCL_BL_CHARGES&from=6&import=${importFlag}", "checkChargeCode()");
                    </script>
                </td>
                <td>Charge Code</td>
                <td>
                    <input type="text" name="chargeCode" id = "chargeCode" readonly="readonly" size="15" Class="BackgrndColorForTextBox"
                           value="${fclBillLaddingform.chargeCode}" tabindex="-1" />
                </td>
                <td>Currency</td>
                <td>
                    <html:text property="chargeCurrency" value="USD" styleClass="BackgrndColorForTextBox" size="3" readonly="true" tabindex="-1"></html:text>
                    </td>
                    <td>Cost</td>
                    <td>
                    <html:text property="costAmount"  maxlength="8" styleId="costAmount" size="8"
                               onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"></html:text>
                    </td>
                    <td>Sell</td>
                    <td>
                    <html:text property="chargeAmount"  maxlength="8" styleId="chargeAmount" size="8"
                               onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"></html:text>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Invoice Number
                    </td>
                    <td>
                        <input name="invoiceNumber" Class="textlabelsBoldForTextBox" id="invoiceNumber" size="24" maxlength="30" />
                    </td>
                    <td>
                        Bill To party
                    </td>
                    <td>
                    <html:select property="chargeBillTo" styleId="notCollapse" styleClass="textlabelsBoldForTextBox" value="${param.billTo}">
                        <html:option value="Forwarder">Forwarder</html:option>
                        <html:option value="Shipper">Shipper</html:option>
                        <html:option value="Consignee">Consignee</html:option>
                        <html:option value="ThirdParty">ThirdParty</html:option>
                        <html:option value="NotifyParty">NotifyParty</html:option>
                    </html:select>
                </td>
                <td >
                    Comment
                </td>
                <td colspan="5">
                    <textarea name="comment" id="comment"
                              class="textlabelsBoldForTextBox" rows="4" cols="39"
                              style="text-transform: uppercase;"
                              onkeypress="return testCommentsLength('', this, 460)" ></textarea>
                </td>
            </tr>
            <tr class="textlabelsBold">
                <td id="text1" width="6%">
                    Vendor Name<br>(Carrier<span id="defaultCarrier">
                        <html:checkbox property="vendorCheckBox" styleId="default" onclick="getVendorFromParent()"></html:checkbox></span>
                        <span>None
                            <input type="checkbox" name="nvoCheckBox" id="nvoCheckBox" onclick="disableVendorCheckBox()" />)
                        </span>
                    </td>
                    <td>
                        <input name="vendorName" Class="textlabelsBoldForTextBox"
                               id="vendorName" size="29" onkeydown="getNvoNameAndNumber()" />
                        <input name="custname_check1" id="custname_check1"
                               type="hidden" />
                        <div id="custname_choices1" style="display: none"
                             class="autocomplete"></div>
                        <script type="text/javascript">
                            initOPSAutocomplete("vendorName", "custname_choices1", "vendorNumber", "custname_check1",
                                    "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=8&isDojo=false&acctTyp=V", "checkForDisableAgentVendor()");
                    </script>
                </td>
                <td style="padding-left:20px;">
                    Vendor Number
                </td>
                <td>
                    <input name="vendorNumber" Class="textlabelsBoldForTextBox"
                           id="vendorNumber" size="13">
                </td>
                <td>
                    Print To BL
                </td>
                <td>
                    <html:select property="chargePrintBl" styleClass="textlabelsBoldForTextBox">
                        <html:option value="Yes">Yes</html:option>
                        <html:option value="No">No</html:option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td colspan="10" align="center">
                    <input type="button" value="Add" class="buttonStyleNew" id="addCharge"
                           onclick="addCostAndCharges('${loginuser.loginName}', '<%=todaysDate%>', '${param.readyToPost}','${param.fileNo}')" />
                </td>
            </tr>
        </table>
        <html:hidden property="bol" value="${param.bolId}"/>
        <input type="hidden" id="readyToPost" value="${param.readyToPost}"/>
        <html:hidden property="buttonValue"/>
    </html:form>
</body>
</html>

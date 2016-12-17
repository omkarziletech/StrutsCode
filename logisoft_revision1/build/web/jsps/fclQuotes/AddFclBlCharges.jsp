<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.domain.User"%> 
<%@ page import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,java.text.*,java.text.NumberFormat,java.text.DecimalFormat,com.gp.cvst.logisoft.domain.FclBl,com.gp.cvst.logisoft.domain.FclBlCharges,com.gp.cvst.logisoft.domain.FclBlCostCodes,java.text.SimpleDateFormat,com.gp.cvst.logisoft.beans.TransactionBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlBC"/>
<jsp:directive.page import="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%    boolean importFlag = (null != request.getAttribute("importFlag") && request.getAttribute("importFlag").equals("true") ? true : false);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    DBUtil dbUtil = new DBUtil();
    String comment = "";
    if (request.getAttribute("fclBillLaddingform") != null) {
        FclBillLaddingForm fclBillLaddingForm = (FclBillLaddingForm) request.getAttribute("fclBillLaddingform");
        if (CommonFunctions.isNotNull(fclBillLaddingForm.getChargesRemarks())) {
            comment = fclBillLaddingForm.getChargesRemarks();
        }

    }
    request.setAttribute("billToVendor", dbUtil.getBilltype(importFlag));
    request.setAttribute("printOnBill", dbUtil.getPrintOnBill());
    request.setAttribute("defaultcurrency", dbUtil.getGenericFCL1(new Integer(32)));
    String bolId = request.getParameter("bolId");

    String userName = "";
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    String todaysDate = format.format(date);
    User user = new User();
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
        userName = user.getLoginName();
    }
%>

<c:if test="${not empty fclBillLaddingformForContainer}">
    <script type="text/javascript">
        parent.parent.getUpdatedCompleteBL();
    </script>
</c:if>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>My JSP 'AddFclBlCharges.jsp' starting page</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">


    </head>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script language="javascript" src="<%=path%>/js/fclBillLanding.js"></script>
    <script language="javascript" src="<%=path%>/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script type="text/javascript">
        function disableParty() {
            var manifest = '${fclBillLaddingform.readyToPost}';
            if (manifest === 'M' || manifest === 'm') {
                document.getElementById('chargesRemarks').readOnly = true;
                document.getElementById('chargesRemarks').className = "BackgrndColorForTextBox";
                document.getElementById('adjustmentAmount').readOnly = true;
                document.getElementById('adjustmentAmount').className = "BackgrndColorForTextBox";
                if ('${importFlag}' !== 'true') {
                    document.getElementById('chargeAmount').readOnly = true;
                    document.getElementById('chargeAmount').className = "BackgrndColorForTextBox";
                }
            }
            if (parent.parent.document.fclBillLaddingform.houseBL[2].checked && null !== document.getElementById('party')) {
                document.getElementById('party').disabled = false;
            } else {
                document.getElementById('party').disabled = false;
                if ('${importFlag}' === 'true') {
                    document.fclBillLaddingform.chargeBillTo.value = '${fclBillLaddingform.billToCode}';
                    if (parent.parent.document.fclBillLaddingform.houseBL[2].checked && null !== document.getElementById('party')) {
                        document.getElementById('party').disabled = false;
                    } else {
                        document.getElementById('party').disabled = true;
                    }
                    if (manifest === 'M' || manifest === 'm') {
                        document.getElementById('party').disabled = true;
                    }
                } else {
                    if (parent.parent.document.fclBillLaddingform.billToCode[0].checked) {
                        document.fclBillLaddingform.chargeBillTo.value = 'Forwarder';
                    } else if (parent.parent.document.fclBillLaddingform.billToCode[1].checked) {
                        document.fclBillLaddingform.chargeBillTo.value = 'Shipper';
                    } else if (parent.parent.document.fclBillLaddingform.billToCode[2].checked) {
                        document.fclBillLaddingform.chargeBillTo.value = 'ThirdParty';
                    } else if (parent.parent.document.fclBillLaddingform.billToCode[3].checked) {
                        document.fclBillLaddingform.chargeBillTo.value = 'Agent';
                    } else if (parent.parent.document.fclBillLaddingform.billToCode[4].checked) {
                        document.fclBillLaddingform.chargeBillTo.value = 'NotifyParty';
                    }
                    document.getElementById('party').disabled = true;
                }
            }
            if (manifest === 'M' || manifest === 'm') {
                document.getElementById('party').disabled = true;
                document.getElementById('party').className = "BackgrndColorForTextBox";
            }
        }
        function addChargesDetails(ev1, ev2) {
            if (null !== document.getElementById('party')) {
                document.getElementById('party').disabled = false;
            }
            if (getBillToParty()) {
                appendUserInfoForComments(document.getElementById('chargesRemarks'), ev1, ev2);
                var commentValue = document.getElementById('chargesRemarks').value.trim();
                commentValue = '<%=comment.replaceAll("[\r\n]", "\t")%>' + commentValue;//"\r"
                document.fclBillLaddingform.chargesRemarks.value = commentValue;
                //check common.js for function
                //parent.parent.GB_hide();

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

                document.fclBillLaddingform.buttonValue.value = "addChargesInfo";
                document.fclBillLaddingform.submit();
                //parent.parent.getUpdatedBL();
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
        function updateChargesDetails(ev1, ev2) {
            if (document.getElementById('chargePrintBl')) {
                document.getElementById('chargePrintBl').disabled = false;
            }
            if (null !== document.getElementById('party')) {
                document.getElementById('party').disabled = false;
            }
            if (getBillToParty()) {
                appendUserInfoForComments(document.getElementById('chargesRemarks'), ev1, ev2);
                var commentValue = document.getElementById('chargesRemarks').value.trim();
                commentValue = '<%=comment.replaceAll("[\r\n]", "\t")%>' + commentValue;//"\r"
                document.fclBillLaddingform.chargesRemarks.value = commentValue;
                document.fclBillLaddingform.buttonValue.value = "updateChargesInfo";
                document.fclBillLaddingform.submit();
            }
        }
        function getBillToParty() {
            var chargeCode = document.getElementById('chargeCode').value;
            if (chargeCode.trim() === 'ADVSHP' || chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') {
                if ((chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') && (parent.parent.document.fclBillLaddingform.forwardingAgentName.value === "" ||
                        parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FF ASSIGNED' ||
                        parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FRT. FORWARDER ASSIGNED'))
                {
                    alertNew("Forwarder cannot be Empty and  NO FF ASSIGNED to add ADVFF charge");
                    return false;
                } else if (chargeCode.trim() === 'ADVSHP') {
                    var shipperNumber = '';
                    var alertMessage = '';
                    if ('${importFlag}' === 'true') {
                        shipperNumber = parent.parent.document.fclBillLaddingform.houseShipper.value;
                        alertMessage = "Please Select Master BL Shipper before adding ADVSHP charge";
                    } else {
                        shipperNumber = parent.parent.document.fclBillLaddingform.shipper.value;
                        alertMessage = "Please Select House BL Shipper before adding ADVSHP charge";
                    }
                    if (shipperNumber === '') {
                        alertNew(alertMessage);
                        return false;
                    }
                }
                if (parent.parent.document.fclBillLaddingform.houseBL[1].checked ||
                        parent.parent.document.fclBillLaddingform.houseBL[2].checked) {
                    if ('${importFlag}' === 'true') {
                        if (parent.parent.document.fclBillLaddingform.consignee.value === '') {
                            alertNew("Please Enter Consignee name and Number in BL to Add these charges");
                            return false;
                            document.getElementById('chargeCode').value = '';
                            document.getElementById('chargeCodeDesc').value = '';
                        }
                    } else {
                        if (parent.parent.document.fclBillLaddingform.agent.value === '') {
                            alertNew("Please Enter Agent name and Number in BL to Add these charges");
                            return false;
                            document.getElementById('chargeCode').value = '';
                            document.getElementById('chargeCodeDesc').value = '';
                        }

                    }

                } else {
                    alertNew("Only for Collect and Both BL you can add these charges");
                    return false;
                    document.getElementById('chargeCode').value = '';
                    document.getElementById('chargeCodeDesc').value = '';
                }

            }
            if (document.fclBillLaddingform.chargeBillTo.value === 'Forwarder') {
                if (parent.parent.document.fclBillLaddingform.forwardingAgentName.value === "") {
                    alertNew("Please Select Forwarder before selecting Bill To Party");
                    return false;
                } else if (parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FF ASSIGNED' ||
                        parent.parent.document.fclBillLaddingform.forwardingAgentName.value.trim() === 'NO FRT. FORWARDER ASSIGNED') {
                    alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
                    return false;
                } else {
                    return true;
                }
            } else if (document.fclBillLaddingform.chargeBillTo.value === 'Shipper') {
                if ('${importFlag}' === 'true' && parent.parent.document.fclBillLaddingform.houseShipper.value === "") {
                    alertNew("Please Select Master BL Shipper Before selecting Bill To Party");
                    return false;
                } else if (parent.parent.document.fclBillLaddingform.shipper.value === "" && '${importFlag}' === 'false') {
                    alertNew("Please Select House BL Shipper Before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.fclBillLaddingform.chargeBillTo.value === 'ThirdParty') {
                if (parent.parent.document.fclBillLaddingform.billThirdPartyName.value === "") {
                    alertNew("Please Select Third Party before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.fclBillLaddingform.chargeBillTo.value === 'Agent') {
                if (parent.parent.document.fclBillLaddingform.agent.value === "") {
                    alertNew("Please Select Agent before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            } else if (document.fclBillLaddingform.chargeBillTo.value === 'Consignee') {
                if (parent.parent.document.fclBillLaddingform.consignee.value === "") {
                    alertNew("Please Select Consignee before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }

            } else if (document.fclBillLaddingform.chargeBillTo.value === 'Notify') {
                if (parent.parent.document.fclBillLaddingform.notifyParty.value === "") {
                    alertNew("Please Select Notify before selecting Bill To Party");
                    return false;
                } else {
                    return true;
                }
            }
        }

        function checkChargeCode() {
            var chargeCode = document.fclBillLaddingform.chargeCode.value;
            if (chargeCode.trim() === 'ADVSHP' || chargeCode.trim() === 'ADVFF' || chargeCode.trim() === 'PBA') {
                if ('${importFlag}' === 'true') {
                    document.fclBillLaddingform.chargeBillTo.value = 'Consignee';
                } else {
                    document.fclBillLaddingform.chargeBillTo.value = 'Agent';
                }
                document.getElementById('party').disabled = true;
            } else {
                //document.getElementById('party').disabled = false;
                disableParty();
            }
            var shipperNumber = '';
            if (chargeCode === 'ADVSHP') {
                if ('${importFlag}' === 'true') {
                    shipperNumber = parent.parent.document.fclBillLaddingform.houseShipper.value;
                } else {
                    shipperNumber = parent.parent.document.fclBillLaddingform.shipper.value;
                }
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

        }
    </script>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>

    <body class="whitebackgrnd" onload="disableParty()"/>

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
        <font color="blue"><b>${message}</b></font>
        <table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew"><font style="font-weight: bold">Charges Details</font></tr>
        <tr class="textlabelsBold">
            <c:choose>
                <c:when test="${manifested}">
                    <td>Charge Code Desc</td>
                    <td>
                        <input type="text" name="chargeCodeDesc" id= "chargeCodeDesc" maxlength="60" readonly
                               size="20" Class="BackgrndColorForTextBox" value="${fclBillLaddingform.chargeCodeDesc}" tabindex="-1">
                    </td>
                    <td>Charge Code</td>
                    <td>
                        <input type="text" name="chargeCode" id = "chargeCode" readonly 
                               size="15" Class="BackgrndColorForTextBox" value="${fclBillLaddingform.chargeCode}" tabindex="-1"/>
                    </td>
                    <td>Old Amount</td>
                    <td>
                        <html:text property="oldAmount" styleId="oldAmount" maxlength="8" readonly="true"
                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
                    </td>
                    <td>Amount</td>
                    <td>
                        <html:text property="chargeAmount" maxlength="8" styleId="chargeAmount" readonly="true"
                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
                    </td>
                    <td>Adjustment</td>
                    <td>
                        <html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" readonly="true"/>
                    </td>
                </c:when>
                <c:when test="${not empty fclBillLaddingform.chargeFlag}">
                    <td>Charge Code Desc</td>
                    <td>
                        <input type="text" name="chargeCodeDesc" id= "chargeCodeDesc" maxlength="60" readonly
                               size="20" Class="BackgrndColorForTextBox" value="${fclBillLaddingform.chargeCodeDesc}" tabindex="-1">
                    </td>
                    <td>Charge Code</td>
                    <td>
                        <input type="text" name="chargeCode" id = "chargeCode" readonly 
                               size="15" Class="BackgrndColorForTextBox" value="${fclBillLaddingform.chargeCode}" tabindex="-1"/>
                    </td>
                    <td>Old Amount</td>
                    <td>
                        <html:text property="oldAmount" styleId="oldAmount" maxlength="8" readonly="true"
                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
                    </td>
                    <td>Amount</td>
                    <td>
                        <c:choose>
                            <c:when test="${(param.button=='addNewCharges' || manualCharge=='manualCharge')}">
                                <html:text property="chargeAmount" styleId="chargeAmount" maxlength="8"
                                           size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/>
                            </c:when>
                            <c:otherwise>
                                <html:text property="chargeAmount" maxlength="8" styleId="chargeAmount" readonly="true"
                                           size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>Adjustment</td>
                    <td>
                        <c:choose>
                            <c:when test="${importFlag eq true || manualCharge == 'manualCharge'}">
                                <c:choose>
                                    <c:when test="${manualCharge == 'manualCharge'}">
                                        <html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" readonly="true"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty fclBillLaddingform.readyToPost}">
                                        <html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                                   readonly="true"  size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                                   size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </c:when>
                <c:otherwise>
                    <td>Charge Code Desc</td>
                    <td><input type="text" name="chargeCodeDesc"  maxlength="60" id = "chargeCodeDesc"
                               size="20"  value="${fclBillLaddingform.chargeCodeDesc}" Class="textlabelsBoldForTextBox">
                        <input name="chargeCodeCheck" id="chargeCodeCheck" type="hidden"/>
                        <div id="chargeCodeChoices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initOPSAutocomplete("chargeCodeDesc", "chargeCodeChoices", "chargeCode", "chargeCodeCheck",
                                    "<%=path%>/actions/autoCompleterForChargeCode.jsp?tabName=FCL_BL_CHARGES&from=6&import=${importFlag}", "checkChargeCode()");
                        </script>
                    </td>
                    <td>Charge Code</td>
                    <td>
                        <input type="text" name="chargeCode" maxlength="60" id = "chargeCode" size="15"
                               Class="BackgrndColorForTextBox" readonly="true" value="${fclBillLaddingform.chargeCode}" tabindex="-1"/>
                    </td>
                    <td>Old Amount</td>
                    <td><html:text property="oldAmount" maxlength="8" size="8" styleId="oldAmount"
                               readonly="true"  onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/></td>
                    <td>Amount</td>

                    <c:choose>
                        <c:when test="${param.button=='addNewCharges' || manualCharge=='manualCharge'}">
                            <td><html:text property="chargeAmount"  maxlength="8" styleId="chargeAmount" size="8"
                                       onchange="checkForNumberAndDecimal(this)"  styleClass="textlabelsBoldForTextBox"/></td>
                            </c:when>
                            <c:otherwise>
                            <td><html:text property="chargeAmount"  maxlength="8" styleId="chargeAmount" size="8"
                                       onchange="checkForNumberAndDecimal(this)" readonly="true"  styleClass="BackgrndColorForTextBox" tabindex="-1"/></td>
                            </c:otherwise>
                        </c:choose>

                    <td>Adjustment</td>
                    <c:choose>
                        <c:when test="${importFlag eq true || manualCharge == 'manualCharge' || param.button=='addNewCharges'}">
                            <td><html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                       readonly="true"  size="8" onchange="checkForNumberAndDecimal(this)" styleClass="BackgrndColorForTextBox" tabindex="-1"/></td>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${not empty fclBillLaddingform.readyToPost}">
                                    <td><html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                               readonly="true"  size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/></td>
                                    </c:when>
                                    <c:otherwise>
                                    <td><html:text property="adjustmentAmount"  maxlength="8" styleId="adjustmentAmount"
                                               size="8" onchange="checkForNumberAndDecimal(this)" styleClass="textlabelsBoldForTextBox"/></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
        </tr>
        <tr class="textlabelsBold">
            <td>Currency</td>
            <td>
                <html:text property="chargeCurrency" value="USD" styleClass="BackgrndColorForTextBox" size="3" readonly="true" tabindex="-1"/>
            </td>
            <td>Bill To Party</td>
            <td>
                <c:choose>
                    <c:when test="${collexpand=='collapse'}">
                        <html:select property="chargeBillTo"  styleId="party"
                                     styleClass="dropdown_accounting" onchange="getBillToParty()">
                            <c:if test="${noFFComm == 'false'}">
                                <html:option value="Forwarder">Forwarder</html:option>
                            </c:if>
                            <html:option value="Shipper">Shipper</html:option>
                            <c:choose>
                                <c:when test="${importFlag eq true}">
                                    <html:option value="Consignee">Consignee</html:option>
                                    <html:option value="NotifyParty">NotifyParty</html:option>
                                </c:when>
                                <c:when test="${not empty fclSsblGoCollect && fclSsblGoCollect == 'N'}">

                                </c:when>
                                <c:otherwise>
                                    <html:option value="Agent">Agent</html:option>
                                </c:otherwise>
                            </c:choose>
                            <html:option value="ThirdParty">ThirdParty</html:option>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:select property="chargeBillTo" styleId="party" styleClass="dropdown_accounting">
                            <c:if test="${noFFComm == 'false'}">
                                <html:option value="Forwarder">Forwarder</html:option>
                            </c:if>
                            <html:option value="Shipper">Shipper</html:option>
                            <c:choose>
                                <c:when test="${importFlag eq true}">
                                    <html:option value="Consignee">Consignee</html:option>
                                    <html:option value="NotifyParty">NotifyParty</html:option>
                                </c:when>
                                <c:when test="${not empty fclSsblGoCollect && fclSsblGoCollect == 'Y'}">

                                </c:when>
                                <c:otherwise>
                                    <html:option value="Agent">Agent</html:option>
                                </c:otherwise>
                            </c:choose>
                            <html:option value="ThirdParty">ThirdParty</html:option>
                        </html:select>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>Print To BL</td>
            <td>
                <html:select property="chargePrintBl" styleClass="dropdown_accounting" styleId="chargePrintBl">
                    <html:optionsCollection name="printOnBill"/>
                </html:select>
            </td>
            <c:if test="${importFlag eq false}">
                <td>Bundle Into OFR</td>
                <td>
                    <html:select property="bundleIntoOfr" styleClass="dropdown_accounting">
                        <html:option value="No">No</html:option>
                        <html:option value="Yes">Yes</html:option>
                    </html:select>
                </td>
            </c:if>
        </tr>
        <tr class="textlabelsBold">
            <td  colspan="3">
                <table>
                    <tr><td class="textlabelsBold">Comments</td></tr>
                    <tr><td  colspan="3" >
                            <textarea rows="4" cols="39" name="chargesRemarks" id="chargesRemarks"
                                      class="textlabelsBoldForTextBox"  style="text-transform: uppercase;"
                                      onkeypress="return testCommentsLength('<%=comment.replaceAll("(\r\n|\r|\n|\n\r)", "\t")%>', this, 460)"></textarea>
                        </td></tr>
                </table>
            </td>
            <%if (CommonFunctions.isNotNull(comment) && comment.contains(").")) {%>
            <td  colspan="6" align="right" valign="top">
                <table>
                    <tr><td class="textlabelsBold">Previous Comments</td></tr>
                    <tr><td>
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
                                    <%}%>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
            <%}%>
        </tr>
        <tr>
            <td align="center" colspan="10">
                <c:choose>
                    <c:when test="${manifested}">
                        <input type="button" value="Quick CN" id="update" class="buttonStyleNew"
                               onclick="updateChargesDetails('<%=userName%>', '<%=todaysDate%>')"/>
                    </c:when>
                    <c:when test="${param.button=='addNewCharges'}">
                        <input type="button" value="Save" id="add" class="buttonStyleNew"
                               onclick="addChargesDetails('<%=userName%>', '<%=todaysDate%>', '${manualCharge}')"/>
                    </c:when>
                    <c:otherwise>
                        <input type="button" value="Update" id="update" class="buttonStyleNew"
                               onclick="updateChargesDetails('<%=userName%>', '<%=todaysDate%>')"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>

    <html:hidden property="buttonValue"/>
    <html:hidden property="bol" value="<%=bolId%>"></html:hidden>
    <html:hidden property="chargeId"/>
    <html:hidden property="rollUpAmount"/>
    <html:hidden property="manualChargeAmount" value="${fclBillLaddingform.chargeAmount}"/>
    <html:hidden property="editFlag" value="${manualCharge}"/>
</html:form>
</body>
<script>changeSelectBoxOnViewMode();</script>
</html>

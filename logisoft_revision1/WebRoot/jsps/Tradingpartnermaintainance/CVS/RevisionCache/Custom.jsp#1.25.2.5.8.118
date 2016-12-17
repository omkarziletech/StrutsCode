<%@ page language="java" pageEncoding="ISO-8859-1" 
         import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.*,org.apache.commons.lang3.StringUtils,com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants,com.gp.cong.logisoft.struts.form.*,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.CommonFunctions"%>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%><%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO"%>
<jsp:useBean id="custAddr"
             class="com.gp.cong.logisoft.domain.CustomerAddress" scope="request"></jsp:useBean>
<bean:define id="RoleQuote" type="String">
    <bean:message key="RoleQuote" />
</bean:define>
<%    User userid = null;
    if (session.getAttribute("loginuser") != null) {
        userid = (User) session.getAttribute("loginuser");
    }
    String closeThePopUp = request.getAttribute("callFrom") != null ? (String) request.getAttribute("callFrom") : "";
    String field = request.getAttribute("field") != null ? (String) request.getAttribute("field") : "";

    String path = request.getContextPath();
    String accountNameTemp = "";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

    if (request.getAttribute("tradingPartnerAcctType") != null) {
        request.setAttribute("tradingPartnerForm", request.getAttribute("tradingPartnerAcctType"));
    }
    if (request.getAttribute("accountNameTemp") != null) {
        accountNameTemp = (String) request.getAttribute("accountNameTemp");
    }
    String primary = "";
    String notifyParty = "";
    if (request.getAttribute("CUST_ADDRESS") != null) {
        CustomerAddress customerAddress = (CustomerAddress) request.getAttribute("CUST_ADDRESS");
        primary = customerAddress.getPrimary();
        notifyParty = customerAddress.getNotifyParty();
        if (primary != null && primary.equals("on")) {
            custAddr.setPrimary("on");
        } else {
            custAddr.setPrimary("off");
        }
        if (notifyParty != null && notifyParty.equals("on")) {
            custAddr.setNotifyParty("on");
        } else {
            custAddr.setNotifyParty("off");
        }
    }
    //------SCAN AND ATTACH CODE---------------
    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
    if (accountNo != null) {
        request.setAttribute("TotalScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(accountNo, "TRADINGPARTNER", "", "Scan or Attach"));
    }
%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <base href="<%=basePath%>">
        <title>JSP for CustomerForm form</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/baseResourcesForJS.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js" ></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
    </head>
    <body class="whitebackgrnd">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/tradingPartner" name="tradingPartnerForm" styleId="customer" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <input type="hidden" id="notifyParty" value="<%=notifyParty%>"/>
            <div id="disabledMessage" style="color: red;font-size: larger;font-weight: bolder"></div>
            <div id="msg" style="color: red;font-size: medium;font-weight: bolder"></div>
            <table width="70%" cellpadding="0" cellspacing="0" border="0">
                <tr class="textlabelsBold">
                    <td>Account Name:&nbsp;<html:text property="accountName" styleId="accountName" value="${tradingPartnerForm.accountName}" 
                               readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/> </td>
                    <td>Account No:&nbsp;<html:text property="accountNo" styleId="accountNo" value="${tradingPartnerForm.accountNo}" 
                               readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/></td>
                    <td>
                        <c:if test="${not master}">
                            Master:&nbsp;&nbsp;&nbsp;&nbsp;
                            <html:select property="master" styleClass="textlabelsBoldForTextBoxDisabledLook" value="<%=master%>" disabled="true">
                                <html:optionsCollection name="mastertypelist"/>
                            </html:select>
                        </c:if>
                    </td>
                    <td>
                        <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()"/>
                        <input type="button" class="buttonStyleNew" value="Restart" onclick="restartTradingPartner()"/>
                        <input type="button" class="buttonStyleNew" id="note"  name="search" value="Note"
                               onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '<%=NotesConstants.TRADINGPARTNER%>&moduleRefId=' + '<%=accountNo%>', 350, 750);" />
                        <c:choose>
                            <c:when test="${null!=TotalScan && TotalScan!='0'}">
                                <input id="scanButton" class="buttonColor" type="button" value="Scan/Attach" onClick="scanAttach('<%=accountNo%>')"/>
                            </c:when>
                            <c:otherwise>
                                <input id="scanButton" class="buttonStyleNew" type="button" value="Scan/Attach" onClick="scanAttach('<%=accountNo%>')"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${isNotes}">
                                <img src="<%=path%>/img/icons/e_contents_view1.gif" width="14" height="14" id="noteIcon"
                                     onclick="openBlueScreenNotesInfo(document.getElementById('accountNo').value, document.getElementById('accountName').value)"/>
                            </c:when>
                            <c:otherwise>
                                <img src="<%=path%>/img/icons/e_contents_view.gif" width="14" height="14" id="noteIcon"
                                     onclick="openBlueScreenNotesInfo(document.getElementById('accountNo').value, document.getElementById('accountName').value)"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="textlabels">
                    <td><b>ECI Shpr/FF#:&nbsp;</b><html:text styleId="eciAccountNo" property="eciAccountNo" value="${tradingPartnerForm.eciAccountNo}" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/></td>
                    <td><b>ECI Consignee:&nbsp;</b><html:text styleId="eciAccountNo2" property="eciAccountNo2" value="${tradingPartnerForm.eciAccountNo2}" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/></td>
                    <td><b>ECI Vendor:&nbsp;</b><html:text styleId="eciAccountNo3" property="eciAccountNo3" value="${tradingPartnerForm.eciAccountNo3}" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/></td>
                    <td id="ssLineId" style="display: none;">
                        <b>SSLine Number :</b>
                        <html:text property="sslineNumber" readonly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" value="${tradingPartnerForm.sslineNumber}" style="text-transform: uppercase;"/>
                    </td>
                </tr>
                <tr class="textlabels">
                    <td colspan="2"><b>Account Type</b>&nbsp;
                        <input id="accountType" value="${tradingPartnerForm.accountType}" readonly class="textlabelsBoldForTextBoxDisabledLook">
                    </td>
                    <td id="subTypeContent" style="display: none;">
                        <b>Sub Type</b>&nbsp;&nbsp;
                        <c:set var="subType" value="${tradingPartnerForm.subType}"/>
                        <c:if test="${subType == 'Steamship Line' && not empty tradingPartnerForm.sslineNumber}">
                            <c:set var="subType" value="${subType} (${tradingPartnerForm.sslineNumber})"/>
                        </c:if>
                        <input type="text" name="subType" id="subType" value="${subType}" readonly="readonly" class="BackgrndColorForTextBox"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="tradingPartnerId" styleId="tradingPartnerId" value="<%=accountNo%>"/>
            <html:hidden property="accountNameTemp" styleId="accountNameTemp"  value="<%=accountName%>"/>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <html:hidden property="callFrom" styleId="callFrom" value="${callFrom}"/>
            <html:hidden property="field" styleId="field" value="${field}"/>
            <html:hidden property="address1" styleId="address1" value="${tradingPartnerForm.address1}"/>
            <c:if test="${view=='3'}">
                <script type="text/javascript">view(document.getElementById("customer"))</script>
            </c:if>
            <c:set var="isApSpecialist" value="false"/>
            <c:choose>
                <c:when test="${!empty sessionScope.loginuser
                                && (sessionScope.loginuser.role.roleDesc ==commonConstants.ROLE_NAME_APSPECIALIST)}">
                    <c:set var="isApSpecialist" value="true"/>
                </c:when>
            </c:choose>
            <input type="hidden" id="isApSpecialist" value="${isApSpecialist}">
        </html:form>
    </body>
    <script type="text/javascript" src="<%=path%>/js/FormChek.js" ></script>
    <script type="text/javascript">

                    function goBack() {
//            var callFrom = document.tradingPartnerForm.callFrom.value;
//            var field = document.tradingPartnerForm.field.value;
//            var accountNameTemp = document.tradingPartnerForm.accountNameTemp.value;
                        var callFrom = document.getElementById("callFrom").value;
                        var field = document.getElementById("field").value;
                        var accountNameTemp = document.getElementById("accountNameTemp").value;
                        if (null != callFrom && undefined != callFrom && callFrom != '') {
                            closePopUP(callFrom, field, accountNameTemp);
                        } else {
                            document.tradingPartnerForm.buttonValue.value = "cancelCustomer";
                            document.tradingPartnerForm.submit();
                        }
                    }

                    function restartTradingPartner() {
                        document.tradingPartnerForm.buttonValue.value = "cancelCustomer";
                        document.tradingPartnerForm.submit();
                    }

                    function getAccountType() {
                        var acct_type = '';
                        if (document.tradingPartnerForm.accountType1.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "S";
                            } else {
                                acct_type = "S";
                            }
                        }
                        if (document.tradingPartnerForm.accountType3.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "N";
                            } else {
                                acct_type = "N";
                            }
                        }
                        if (document.tradingPartnerForm.accountType4.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "C";
                            } else {
                                acct_type = "C";
                            }
                        }
                        if (document.tradingPartnerForm.accountType8.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "I";
                            } else {
                                acct_type = "I";
                            }
                        }
                        if (document.tradingPartnerForm.accountType9.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "E";
                            } else {
                                acct_type = "E";
                            }
                        }
                        if (document.tradingPartnerForm.accountType10.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "V";
                            } else {
                                acct_type = "V";
                            }
                        }
                        if (document.tradingPartnerForm.accountType11.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "O";
                            } else {
                                acct_type = "O";
                            }
                        }
                        if (document.tradingPartnerForm.accountType13 && document.tradingPartnerForm.accountType13.checked) {
                            if (acct_type != '') {
                                acct_type = acct_type + "," + "Z";
                            } else {
                                acct_type = "Z";
                            }
                        }
                        return acct_type
                    }
                    function Save() {
                        if (!document.tradingPartnerForm.accountType1.checked && !document.tradingPartnerForm.accountType3.checked && !document.tradingPartnerForm.accountType4.checked
                                && !document.tradingPartnerForm.accountType8.checked && !document.tradingPartnerForm.accountType9.checked
                                && !document.tradingPartnerForm.accountType10.checked && !document.tradingPartnerForm.accountType11.checked) {
                            alert("Please select atleast one Account Type");
                            return;
                        }
                        if (document.getElementById("isApSpecialist").value == "true") {
                            savingAllInfo();
                        } else if (document.geniframe.checkForAddressDetails()) { //---Method in Customer.jsp page-----
                            savingAllInfo();
                        }
                    }
                    function savingAllInfo() {//---method invoked facciframerom Customer.jsp----
                        document.tradingPartnerForm.buttonValue.value = "saveAccountType";
                        document.tradingPartnerForm.submit();
                    }
                    function showSubType() {
                        if (document.getElementById('accountType').value.indexOf("V") >= 0) {
                            if (null != document.vendorframe && null != document.vendorframe.document.getElementById('subTypeValue')) {
                                document.vendorframe.document.getElementById('subTypeValue').style.display = "block";
                                document.vendorframe.document.getElementById('subTypeLabel').style.display = "block";
                            }
                            document.getElementById('subTypeContent').style.display = "block";
                            document.getElementById('ssLineId').style.display = "block";
                        } else {
                            document.getElementById('subTypeContent').style.display = "none";
                            document.getElementById('ssLineId').style.display = "none";
                            if (null != document.vendorframe && null != document.vendorframe.document.getElementById('subTypeValue')) {
                                document.vendorframe.document.getElementById('subTypeValue').style.display = "none";
                                document.vendorframe.document.getElementById('subTypeLabel').style.display = "none";
                            }
                        }
                    }
                    function getNotify() {
                        if (document.geniframe != undefined) {
                            if (document.getElementById('accountType').value.indexOf("C") >= 0) {
                                document.geniframe.getNotifyParty('<%=notifyParty%>', true);
                                //parent.mainFrame.document.geniframe.tradingPartnerForm.notifyParty.checked=true;
                            } else {
                                document.geniframe.getNotifyParty('<%=notifyParty%>', false);
                                //parent.mainFrame.document.geniframe.tradingPartnerForm.notifyParty.checked=false;
                            }
                        }
                    }
                    function closePopUP(callFrom, fromField, accountName) {
                        if (callFrom != undefined && callFrom != null && callFrom != '') {
                            window.parent.parent.addNewCustomer(fromField, accountName, "<%=accountNo%>", callFrom);
                        }
                    }
                    function checkForExistingRecord() {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                methodName: "getCustomerOfAccountTypeZ"
                            },
                            async: false,
                            success: function (data) {
                                if (data != "") {
                                    document.getElementById("msg").innerHTML = "Company with account type Z already exists";
                                    document.tradingPartnerForm.accountType13.checked = false;
                                    document.tradingPartnerForm.accountType13.disabled = true;
                                } else {
                                    document.tradingPartnerForm.accountType13.disabled = false;
                                }
                            }
                        });
                    }
                    function changeScanButtonColor(masterStatus, documentName, docList) {
                        if (docList === 0) {
                            document.getElementById("scanButton").className = "buttonStyleNew";
                        } else {
                            document.getElementById("scanButton").className = "buttonColor";
                        }
                    }
                    function openBlueScreenNotesInfo(customerNo, customerName) {
                        GB_show("Customer Notes", rootPath + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
                    }
    </script>
    <script type="text/javascript">
        showSubType();
        getNotify();
    </script>
    <c:if test="${view=='3'}">
        <script type="text/javascript">
            parent.parent.parent.parent.makeFormBorderless(document.getElementById("customer"));
            view(document.getElementById("customer"));
        </script>
    </c:if>

</html>



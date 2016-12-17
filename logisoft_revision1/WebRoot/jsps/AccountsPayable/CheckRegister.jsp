<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <head>
            <base href="${basePath}"/>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1"/>
        <title>Check Register</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>

        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/UploadDownloaderDWR.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false"/>
        </c:if>
        <%@include file="../includes/resources.jsp"%>
        <style type="text/css">
            #checkListDiv {
                position: fixed;
                _position: absolute;
                z-index: 999;
                left: 1%;
                top: 12%;
                border-style:solid solid solid solid;
                background-color: white;
            }
            tr td img{
                margin-left: 10px;
            }
        </style>

    </head>
    <body class="whitebackgrnd" style="cursor: auto;">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <html:form action="/checkRegister?accessMode=${accessMode}" name="checkRegisterForm" type="com.gp.cvst.logisoft.struts.form.CheckRegisterForm" scope="request">
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew">
                <tr>
                <td>
                    <table width="100%" border="0" cellpadding="2" cellspacing="0">
                        <tr class="tableHeadingNew">
                        <td colspan="4">Check Register</td>
                        </tr>
                        <tr>
                        <td align="center">
                        <span id="message" style="font-size:medium;color:blue"></span>
                        <span id="error" style="font-size:medium;color:blue" ></span>
                </td>
            </tr>
            <tr class="textlabelsBold">
            <td>Account</td>
            <td>
                <html:text property="glAccountNo" styleId="glAccountNo" styleClass="textlabelsBoldForTextBox"/>
                <html:hidden property="bankAccountNo" styleId="bankAccountNo"/>
                <input name="glAccountNoCheck" id="glAccountNoCheck"  type="hidden"/>
                <div id="glAccountNoChoices" style="display: none" class="autocomplete"></div>
            </td>
            <td>Bank Reconciled Date</td>
            <td>
                <div class="float-left">
                    <html:text property="bankReconcileDate" styleClass="textlabelsBoldForTextBox" styleId="txtcal4"/>
                </div>
                <div class="calendar-img">
                    <img src="${path}/img/CalendarIco.gif" alt="cal"width="14" height="14" align="top" id="cal4" onmousedown="insertDateFromCalendar(this.id,0);"/>
                </div>
            </td>
        </tr>
        <tr class="textlabelsBold">
        <td>Check Number From</td>
        <td>
            <html:text property="startCheckNumber" styleId="startCheckNumber" styleClass="textlabelsBoldForTextBox uppercase"/>
        </td>
        <td>Check Number To</td>
        <td>
            <html:text property="endCheckNumber" styleId="endCheckNumber" styleClass="textlabelsBoldForTextBox uppercase"/>
        </td>
    </tr>
    <tr class="textlabelsBold">
    <td>Vendor Name</td>
    <td>
        <html:text property="vendorName" styleId="vendorName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
        <html:hidden property="vendorNumber" styleId="vendorNumber" styleClass="textlabelsBoldForTextBox"/>
        <input name="vendorNameCheck" id="vendorNameCheck"  type="hidden"/>
        <div id="vendorNameChoices" style="display: none" class="autocomplete"></div>
    </td>
    <td>Payment Amount</td>
    <td>
        <html:select property="invoiceOperator" styleId="invoiceOperator" styleClass="dropdown_accounting" style="width:35px">
            <html:optionsCollection name="checkType"/>
        </html:select>
        <html:text property="invoiceAmount" styleId="invoiceAmount" styleClass="textlabelsBoldForTextBox" style="width:83px"/>
    </td>
</tr>
<tr class="textlabelsBold">
<td>Show Status</td>
<td>
    <input type="radio" name="showStatus" value="Y"/>Cleared
    <input type="radio" name="showStatus" value="N"/>Not Cleared
    <input type="radio" name="showStatus" value="Void"/>Voided
    <input type="radio" name="showStatus" checked="checked" value="All"/>All
</td>
<td>Batch Number</td><td>
    <html:text property="batchNumber" styleId="batchNumber" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
</td>
</tr>
<tr class="textlabelsBold">
<td>Payment Method</td>
<td>
    <html:select property="payMethod" styleId="payMethod" styleClass="dropdown_accounting" style="width: 125px">
        <html:option value="All">All</html:option>
        <html:option value="Check">Checks</html:option>
        <html:option value="Ach">ACH</html:option>
        <html:option value="Wire">Wire</html:option>
        <html:option value="Ach Debit">ACH debits</html:option>
    </html:select>
</td>
</tr>
<tr class="textlabelsBold">
<td align="center" colspan="4">
    <input type="button" class="buttonStyleNew" style="width: 40px;" value="Clear" onclick="clearDetails();"/>
    <input type="button" class="buttonStyleNew" style="width: 60px;" value="Search" onclick="search();"/>
</td>
</tr>
</table>
</td>
</tr>
<tr>
<td align="center">
<span id="checkRegisterMessage" style="color:blue;font-size:medium;"></span>
</td>
</tr>
<tr>
<td>
    <div id="checkRegisterListDiv" style="width: 100%"></div>
</td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<html:hidden property="sortBy" styleId="sortBy"/>
<html:hidden property="sortOrder" value="asc" styleId="sortOrder"/>
<html:hidden property="transactionIds" styleId="transactionIds"/>
<html:hidden property="custNo" styleId="custNo"/>
<html:hidden property="paymentDate" styleId="paymentDate"/>
<html:hidden property="paymentMethod" styleId="paymentMethod"/>
<html:hidden property="checkNo" styleId="checkNo"/>
<input type="hidden" id="canEdit" value="${canEdit}"/>
</html:form>
</body>
<script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/CheckRegister.js"></script>
<%@include file="../includes/baseResourcesForJS.jsp" %>
<script type="text/javascript">
    jQuery.noConflict();
</script>
</html>

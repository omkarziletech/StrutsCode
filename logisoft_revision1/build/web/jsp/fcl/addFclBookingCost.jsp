<%-- 
    Document   : AddFclBookingCost.jsp
    Created on : 3 May, 2013, 12:56:39 PM
    Author     : Balaji.E(Logiware)
--%>

<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@page import="com.gp.cvst.logisoft.struts.form.EditBookingsForm"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            request.setAttribute("defaultcurrency", new DBUtil().getGenericFCL1(new Integer(32)));
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
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Booking Cost</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript">
	    var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css">
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
        <link type="text/css" rel="stylesheet" href="${path}/css/default/TabContainer.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/accordionStyle.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/editBooking.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/fcl/addFclBookingCost.js"></script>
        <c:if test="${not empty editBookingsForm.bookingId}">
            <c:set var="bookId" value="${editBookingsForm.bookingId}"></c:set>
        </c:if>
        <c:if test="${not empty param.bookingId}">
            <c:set var="bookId" value="${param.bookingId}"></c:set>
        </c:if>
        <c:if test="${not empty param.importFlag}">
            <c:set var="importFlag" value="${param.importFlag}"></c:set>
        </c:if>
        <c:if test="${not empty param.fileNo}">
            <c:set var="fileNo" value="${param.fileNo}"></c:set>
        </c:if>
        <c:if test="${buttonValue=='updateBookingCost' || buttonValue=='addBookingCost'}">
            <script type="text/javascript">
                parent.parent.getUpdatedBookingChargesDetails('${bookId}');
                parent.parent.GB_hide();
            </script>
        </c:if>
    </head>
    <body>
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <div style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                           grayOut(false,'');">
            </div>
        </div>
        <!-- ALERT BOX DESIGN ENDS---->
        <html:form action="/editBooks" name="EditBookingsForm" enctype="multipart/form-data"
                   styleId="editbookingForm" type="com.gp.cvst.logisoft.struts.form.EditBookingsForm"
                   scope="request" method="post">
            <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0">
                <tr class="tableHeadingNew">Add Accruals</tr>
                <tr class="textlabelsBold">
                    <td>
                        <table width="100%">
                            <tr class="textlabelsBold">
                                <td>Cost Code Desc</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${(! empty editBookingsForm.costCode && editBookingsForm.costCode == 'FAECOMM')
                                                        || (buttonValue=='editBookingAccruals' || buttonValue=='updateBookingCost')}">
                                                <input type="text" name="costCodeDesc" id="costCodeDesc" class="BackgrndColorForTextBox"
                                                       size="25" maxlength="60" readonly="readonly" tabindex="-1" value="${editBookingsForm.costCodeDesc}" />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="costCodeDesc" id="costCodeDesc" class="textlabelsBoldForTextBox"
                                                   size="25" maxlength="60" value="${editBookingsForm.costCodeDesc}" />
                                        </c:otherwise>
                                    </c:choose>
                                    <input name="costCodeCheck" id="costCodeCheck" type="hidden" value="${editBookingsForm.costCodeDesc}"/>
                                    <div id="costCodeChoices" style="display: none" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("costCodeDesc","costCodeChoices","costCode","costCodeCheck",
                                        "${path}/actions/autoCompleterForChargeCode.jsp?tabName=FCL_BL_CHARGES&from=7&import=${importFlag}","fillInvoiceNumber('${editBookingsForm.invoiceNumber}')","");
                                    </script>
                                </td>
                                <td>Cost Code</td>
                                <td>
                                    <input type="text" name="costCode" value="${editBookingsForm.costCode}" id="costCode" class="BackgrndColorForTextBox"
                                           size="15" maxlength="10" readonly="readonly" tabindex="-1" />

                                </td>
                                <td align="right">Amount</td>
                                <td>
                                    <fmt:formatNumber var="costAmount" value="${editBookingsForm.costAmount}" pattern="##########0.00"/>
                                    <html:text property="costAmount" styleId="checkAmount" value="${costAmount}"
                                                   maxlength="8" size="8" onchange="checkForNumberAndDecimal(this);" styleClass="textlabelsBoldForTextBox"/>
                                </td>
                                <td align="right">Currency</td>
                                <td>
                                    <html:text property="costCurrency" value="USD" styleClass="BackgrndColorForTextBox" size="3" readonly="true" tabindex="-1"></html:text>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">Vendor Name</td>
                                <td valign="top">
                                    <c:choose>
                                        <c:when test="${! empty editBookingsForm.costCode && editBookingsForm.costCode == 'FAECOMM'}">
                                            <input type="text" name="vendorAccountName" id="accountName" value="${editBookingsForm.accountName}"
                                                   class="textlabelsBoldForTextBox" size="25" maxlength="60" readonly="readonly" tabindex="-1"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="vendorAccountName" id="accountName" value="${editBookingsForm.vendorAccountName}"
                                                   class="textlabelsBoldForTextBox" size="25" maxlength="60"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input name="accountNameCheck" id="accountNameCheck" type="hidden" value="${editBookingsForm.vendorAccountName}"/>
                                    <div id="accountNameChoices" style="display: none" class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("accountName","accountNameChoices","accountNo","accountNameCheck",
                                        "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=11&acctTyp=V","checkDisabledPopUP()","");
                                    </script>
                                </td>
                                <td valign="top">Vendor Account No</td>
                                <td valign="top">
                                    <input type="text" name="vendorAccountNo" id="accountNo" value="${editBookingsForm.vendorAccountNo}"
                                           class="BackgrndColorForTextBox" size="10"  maxlength="20" readonly="readonly" tabindex="-1"/></td>
                                <td align="right">Invoice Number</td>
                                <td colspan="3">
                                    <html:text  property="invoiceNumber" value="${editBookingsForm.invoiceNumber}"
                                                styleClass="textlabelsBoldForTextBox"  size="29" maxlength="30"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Comments</td>
                                <td valign="top" >
                                    <textarea rows="4" cols="32" name="costComments" id="costComments"   style="text-transform: uppercase;"
                                              onkeypress="return validateCommentsLength('${editBookingsForm.costComments}',this,500)"   class="textlabelsBoldForTextBox">${editBookingsForm.costComments}</textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td align="center">
                                    <c:choose>
                                        <c:when test="${buttonValue=='editBookingAccruals' || buttonValue=='updateBookingCost'}">
                                            <input type="button" value="Update" id="updateCost" class="buttonStyleNew"
                                                   onclick="updateBookingAccruals('<%=userName%>','<%=todaysDate%>');" />
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" value="Add" id="addCost" class="buttonStyleNew"
                                                   onclick="addBookingAccruals('<%=userName%>','<%=todaysDate%>','${fileNo}');" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="bookingId" value="${bookId}"/>
            <html:hidden property="buttonValue"/>
            <html:hidden property="costCodeId"/>
            <html:hidden property="rollUpAmount"/>
        </html:form>
    </body>
</html>

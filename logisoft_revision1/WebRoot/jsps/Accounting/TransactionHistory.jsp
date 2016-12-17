<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display" %>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.struts.form.*,com.gp.cvst.logisoft.hibernate.dao.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Tranasaction History</title>
        <%            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            if (path == null) {
                path = "../..";
            }
        %>
        <%
            DBUtil dbUtil = new DBUtil();
            String buttonValue = "";
            String acct1 = "";
            String acct = "";
            session.setAttribute("SourcecodeList", dbUtil.getGenericCodeList(33, "no", "Select Source Code"));
            List itemdetails = new ArrayList();
            if (session.getAttribute("itemdetails") != null) {
                itemdetails = (List) session.getAttribute("itemdetails");
            }
            if (!(request.getAttribute("buttonValue") == null)) {
                buttonValue = (String) request.getAttribute("buttonValue");
            }
            List aclist = (List) session.getAttribute("aclist");
            String desc = "";
            if (aclist != null && aclist.size() > 0) {
                acct = (String) aclist.get(0);
            } else {
                acct = "";
            }
            if (aclist != null && aclist.size() > 0) {
                desc = (String) aclist.get(1);
            } else {
                desc = "";
            }
            //this is for current year
            Calendar rightNow = Calendar.getInstance();
            int year = rightNow.get(Calendar.MONTH);
            String currentyear = String.valueOf(year);
            Date date = new Date();
            int date1 = 0;
            date1 = (Integer) date.getMonth() + 1;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String datefrom = format.format(date1);
            String dateto = "";
            String sourcecode = "";
            String sd = "";
            String ed = "";
            if (request.getAttribute("StartDate") != null) {
                sd = (String) request.getAttribute("StartDate");
            }
            if (request.getAttribute("EndDate") != null) {
                ed = (String) request.getAttribute("EndDate");
            }
            String editPath = path + "/acctHistory.do?";
            if (request.getAttribute("account") != null) {
                acct = request.getAttribute("account").toString();
            }
            if (request.getAttribute("account1") != null) {
                acct1 = request.getAttribute("account1").toString();
            }
            String periodFrom = "";
            String periodTo = "";
            List details = null;
            if (session.getAttribute("dateFrom") != null) {
                details = (List) session.getAttribute("dateFrom");
                if (details != null && details.size() == 3) {
                    periodFrom = (String) details.get(0);
                    periodTo = (String) details.get(1);
                    acct1 = (String) details.get(2);
                }
                session.removeAttribute("dateFrom");
            }
            if (request.getAttribute("periodFrom") != null) {
                periodFrom = request.getAttribute("periodFrom").toString();
            } else {
                if (request.getAttribute("currentFiscalPeriod") != null) {
                    periodFrom = ((FiscalPeriod) request.getAttribute("currentFiscalPeriod")).getPeriodDis();
                }
            }

            if (request.getAttribute("periodTo") != null) {
                periodTo = request.getAttribute("periodTo").toString();
            } else {
                if (request.getAttribute("currentFiscalPeriod") != null) {

                    periodTo = ((FiscalPeriod) request.getAttribute("currentFiscalPeriod")).getPeriodDis();
                }
            }
            if (request.getAttribute("fromFiscalPeriod") != null) {
                periodFrom = ((FiscalPeriod) request.getAttribute("fromFiscalPeriod")).getPeriodDis();
            }

            if (request.getAttribute("toFiscalPeriod") != null) {
                periodTo = ((FiscalPeriod) request.getAttribute("toFiscalPeriod")).getPeriodDis();
            }

        %>

        <%@include file="../includes/baseResources.jsp" %>


        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/baseResourcesForJS.jsp" %>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript">
            function sub() {
                if (document.forms[0].account.value != "") {
                    if (document.forms[0].account.value.indexOf("-") == -1) {
                        alert("You have to select an account from the list");
                        document.forms[0].account.focus();
                        return false;
                    }
                }
                if (document.forms[0].account1.value != "") {
                    if (document.forms[0].account1.value.indexOf("-") == -1) {
                        alert("You have to select an account from the list");
                        document.forms[0].account1.focus();
                        return false;
                    }
                }
                if (document.forms[0].datefrom.value == "") {
                    alert("Please Enter Account Starting date ");
                    document.TransactionHistoryForm.datefrom.focus();
                    return false;
                }
                if (document.forms[0].dateto.value == "") {
                    alert("Please Enter Account Ending date ");
                    document.TransactionHistoryForm.dateto.focus();
                    return false;
                }
                var DATEFROM = document.forms[0].datefrom.value;
                var DATETO = document.forms[0].dateto.value;
                if (DATEFROM > DATETO) {
                    alert("Please check the date");
                    document.TransactionHistoryForm.dateto.focus();
                    return false;
                }
                document.forms[0].buttonValue.value = "go";
                document.TransactionHistoryForm.submit();
            }

            function loadTransactionHistory() {
                if (document.TransactionHistoryForm.accountNo.value == "") {
                    document.TransactionHistoryForm.accountNo.focus();
                    return false;
                }
                document.forms[0].buttonValue.value = "loadTransactionForAccount";
                document.TransactionHistoryForm.submit();
            }
            function createExcelSheet() {
                if (document.forms[0].datefrom.value == "") {
                    alert("Please Enter Account Starting date ");
                    document.TransactionHistoryForm.datefrom.focus();
                    return false;
                }
                if (document.forms[0].dateto.value == "") {
                    alert("Please Enter Account Ending date ");
                    document.TransactionHistoryForm.dateto.focus();
                    return false;
                }
                document.TransactionHistoryForm.buttonValue.value = "createExcel";
                document.TransactionHistoryForm.submit();
            }
            function loadReference(journalEntryId) {
                var caption = "Journal Entry - " + journalEntryId;
                var url = rootPath + "/glBatch.do?action=gotoJournalEntry&journalEntry.id=" + journalEntryId;
                window.parent.showGreyBox(caption, url);
            }
            function addMoreParams(element, entry) {
                return entry;
            }
            function goBackGeneralLeger() {
                document.TransactionHistoryForm.buttonValue.value = "goBack";
                document.TransactionHistoryForm.submit();
            }
        </script>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/TransHistory" name="TransactionHistoryForm" type="com.gp.cvst.logisoft.struts.form.TransactionHistoryForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="2">Transaction History</td>
                    <td valign="top" colspan="4" align="right">
                        <c:choose>
                            <c:when test="${navigateTransHistory eq 'navigateTransHistory'}">
                                <input type="button" name="search" onclick="parent.history.back();" value="Go Back" class="buttonStyleNew"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" name="search" onclick="goBackGeneralLeger();" value="Go Back" class="buttonStyleNew"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="button" name="exporttoexcel" value="ExportToExcel" class="buttonStyleNew" style="width: 90px;"
                               onclick="createExcelSheet()"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Starting Account</td>
                    <td>
                        <input name="account" id="account"  value="<%=acct%>" class="textlabelsBoldForTextBox"/>
                        <input type="hidden" name="accountValid" id="accountValid" value="<%=acct%>"/>
                        <div class="newAutoComplete" id="accountDiv"></div>
                        <script type="text/javascript">
                            initAjaxAutoCompleter("account", "accountDiv", "accountValid", "<%=path%>/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=account&tabName=ACCOUNT_HISTORY", "");
                        </script>                    </td>
                    <td>Description</td>
                    <td><html:text property="desc"  value="<%=desc%>" readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                    <td>Sort by</td>
                    <td>
                        <html:radio property="sortBy" value="yes"/>Account
                        <html:radio property="sortBy" value="no"/>Period
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Ending Account</td>
                    <td>
                        <input name="account1" id="account1" value="<%=acct1%>" class="textlabelsBoldForTextBox"/>
                        <input type="hidden" name="account1Valid" id="account1Valid" value="<%=acct%>"/>
                        <div class="newAutoComplete" id="account1Div"></div>
                        <script type="text/javascript">
                            initAjaxAutoCompleter("account1", "account1Div", "account1Valid", "<%=path%>/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=account1&tabName=ACCOUNT_HISTORY", "");
                        </script>
                    </td>
                    <td>Period From</td>
                    <td><html:text property="datefrom"  value="<%=periodFrom%>" maxlength="6" styleClass="textlabelsBoldForTextBox"/></td>
                    <td>Period To</td>
                    <td> <html:text property="dateto"  value="<%=periodTo%>" maxlength="6" styleClass="textlabelsBoldForTextBox"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td valign="top" colspan="6" align="center">
                        <input type="button" name="search" onclick="sub()" value="Search" class="buttonStyleNew"/>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr>
                    <td class="tableHeadingNew">List Of Transaction History</td>
                </tr>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${not empty transactions}">
                                <div id="result-header" class="table-banner green">
                                    <div class="float-right">
                                        <c:choose>
                                            <c:when test="${fn:length(transactions) gt 1}">${fn:length(transactions)} records found.</c:when>
                                            <c:otherwise>1 record found.</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="result-container" style="width: 100%">
                                    <table cellpadding="0" cellspacing="1" class="display-table">
                                        <thead>
                                            <tr>
                                                <th>Account</th>
                                                <th>Period</th>
                                                <th>Date</th>
                                                <th>Source Code</th>
                                                <th>Reference</th>
                                                <th>Description</th>
                                                <th>Debit</th>
                                                <th>Credit</th>
                                                <th>Net Change</th>
                                                <th>Balance</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="zebra" value="odd"/>
                                            <c:set var="account" value=""/>
                                            <c:set var="period" value=""/>
                                            <c:set var="totalBalance" value="0.00"/>
                                            <c:forEach var="row" items="${transactions}" varStatus="status">
                                                <c:if test="${account ne row.account or status.index eq 0}">
                                                    <c:set var="account" value="${row.account}"/>
                                                    <c:set var="totalBalance" value="${closingBalances[row.account]}"/>
                                                    <c:set var="closingBalance" value="${closingBalances[row.account]}"/>
                                                    <tr class="bg-thistle bold">
                                                        <td colspan="9" class="align-right">Opening Balance</td>
                                                        <c:choose>
                                                            <c:when test="${closingBalance < 0.00}">
                                                                <td class="amount red">
                                                                    <fmt:formatNumber value="${-closingBalance}" pattern="(###,###,##0.00)"/>
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="amount black">
                                                                    <fmt:formatNumber 
                                                                        value="${empty closingBalance ? 0.00 : closingBalance}" pattern="###,###,##0.00"/>
                                                                </td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                </c:if>
                                                <fmt:parseNumber var="balance" value="${fn:replace(row.netChange, ',', '')}"/>
                                                <c:set var="totalBalance" value="${totalBalance + balance}"/>
                                                <fmt:formatNumber var="netBalance" value="${totalBalance}" pattern="###,###,##0.00"/>
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(row.debit, '-')}">
                                                        <c:set var="debit" value="(${fn:replace(row.debit, '-', '')})"/>
                                                        <c:set var="debitClass" value="amount red"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="debit" value="${row.debit}"/>
                                                        <c:set var="debitClass" value="amount black"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(row.credit, '-')}">
                                                        <c:set var="credit" value="(${fn:replace(row.credit, '-', '')})"/>
                                                        <c:set var="creditClass" value="amount red"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="credit" value="${row.credit}"/>
                                                        <c:set var="creditClass" value="amount black"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(row.netChange, '-')}">
                                                        <c:set var="netChange" value="(${fn:replace(row.netChange, '-', '')})"/>
                                                        <c:set var="netChangeClass" value="amount red"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="netChange" value="${row.netChange}"/>
                                                        <c:set var="netChangeClass" value="amount black"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(netBalance, '-')}">
                                                        <c:set var="netBalance" value="(${fn:replace(netBalance, '-', '')})"/>
                                                        <c:set var="netBalanceClass" value="amount red"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="netBalanceClass" value="amount black"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <tr class="${zebra}">
                                                    <td>${row.account}</td>
                                                    <td>${row.period}</td>
                                                    <td>${row.date}</td>
                                                    <td>${row.sourceCode}</td>
                                                    <td><a href="javascript: loadReference('${row.reference}')">${row.reference}</a></td>
                                                    <td>${row.description}</td>
                                                    <td class="${debitClass}">${debit}</td>
                                                    <td class="${creditClass}">${credit}</td>
                                                    <td class="${netChangeClass}">${netChange}</td>
                                                    <td class="${netBalanceClass}">${netBalance}</td>
                                                </tr>
                                                <c:if test="${(status.index + 1) lt fn:length(transactions)
                                                              and row.period ne transactions[status.index + 1].period}">
                                                      <tr class="bg-pale-green bold">
                                                          <td colspan="9" class="align-right">Period Closing Balance</td>
                                                          <td class="${netBalanceClass}">${netBalance}</td>
                                                      </tr>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${status.index % 2 eq 0}">
                                                        <c:set var="zebra" value="even"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="zebra" value="odd"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-banner green">No transaction history found</div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <c:if test="${!empty fileName}">
        <script type="text/javascript">
            window.location.href = "<%=path%>/servlet/FileViewerServlet?fileName=${fileName}";
        </script>
    </c:if>
</html>
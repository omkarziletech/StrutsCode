<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
        DBUtil dbUtil = new DBUtil();
        String buttonValue = "";
        List accountdetail = new ArrayList();
        List ad = new ArrayList();
        if (null != request.getAttribute("buttonValue")) {
            buttonValue = (String) request.getAttribute("buttonValue");
        }
        request.setAttribute("currency", dbUtil.getCurrencyList());
        request.setAttribute("Currencytype", dbUtil.getcurrencytype());
        request.setAttribute("yearlist", dbUtil.getFiscalYearForStatus("ALL"));

        List aclist = (List) session.getAttribute("aclist");
        String acct = "";
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
        String index = (String) request.getAttribute("index");
        GregorianCalendar calendar = new GregorianCalendar();
        int y1 = calendar.get(Calendar.YEAR);
        if (request.getAttribute("accountdetail") == null) {
            y1 = y1 + 1;
            String yer = Integer.toString(y1);
            if (acct != "" && yer != null) {
                AccountBalanceDAO abdao = new AccountBalanceDAO();
                accountdetail = abdao.findforSearch(yer, acct);
                ad = accountdetail;
                request.setAttribute("accountdetail", accountdetail);
            }
        }
        if (null != request.getAttribute("accountdetail")) {
            accountdetail = (List) request.getAttribute("accountdetail");
            ad = accountdetail;
        }
        if (null != ad) {
            session.setAttribute("ad", ad);
        }
//this is for current year
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        String currentyear = String.valueOf(year);
        request.setAttribute("currentFiscalYear",currentyear);
%>
<%@include file="../includes/baseResources.jsp" %>
<html:html locale="true">
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <c:set var="editPath" value="${path}/acctHistory.do"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}"/>
        <title>Account Histroy</title>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    </head>
    <body class="whitebackgrnd">
        <html:form  action="/acctHistory" name="AcctHistoryForm" type="com.gp.cvst.logisoft.struts.form.AcctHistoryForm" scope="request">
            <html:hidden property="buttonValue"/>
            <html:hidden property="index"  />
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew" >
                <tr class="tableHeadingNew">
                    <td>Account Selection</td>
                    <td valign="top" align="right">
                        <input type="button" name="search" onclick="window.location.href='${path}/jsps/Accounting/ChartOfAccounts.jsp';" value="Go Back" class="buttonStyleNew"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="2">
                        Load for Another Account
                        <html:text property="accountNo"  value="Click to Change Account No" onclick="this.value='';" styleClass="textlabelsBoldForTextBox"/>
                        <input type="hidden" name="accountNoValid" id="accountNoValid" value="Click to Change Account No"/>
                        <div class="newAutoComplete" id="accountNoDiv"></div>
                        <input type="button" name="search" onclick="loadAccountHistory()" value="Search" class="buttonStyleNew"/>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="6">Filter Options</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Account</td>
                    <td>
                        <html:text property="account"  value="<%=acct%>" readonly="true" styleClass="textlabelsBoldForTextBox"/>
                    </td>
                    <td>Description</td>
                    <td>
                        <html:text property="desc" value="<%=desc %>" readonly="true" styleClass="textlabelsBoldForTextBox"/>
                    </td>
                    <td>Year</td>
                    <td>
                        <html:select property="year" value="${currentFiscalYear}" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="yearlist" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Currency</td>
                    <td>
                        <html:select property="currency" value="USD" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="currency" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                    <td>Currency Type
                    </td>
                    <td>
                        <html:select property="currencytype" styleClass="dropdown_accounting" style="width:125px;">
                            <html:optionsCollection name="Currencytype" styleClass="unfixedtextfiledstyle"/>
                        </html:select>
                    </td>
                </tr >
                <tr>
                    <td valign="top" colspan="6" align="center" style="padding-top:10px;">
                        <input type="button" name="search" onclick="sub()" value="Search" class="buttonStyleNew"/></td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>List of Account History</td>
                </tr>
                <tr>
                    <td>
                        <c:if test="${!empty accountdetail && fn:length(accountdetail)>0}">
                            <c:set var="YTDBalance" value="0"></c:set>
                            <div id="divtablesty1" class="scrolldisplaytable">
                                <c:set var="i" value="0"></c:set>
                                <display:table name="${accountdetail}" pagesize="<%=pageSize%>"
                                               class="displaytagstyleNew"  id="accounthistory" style="width:100%">
                                    <c:set var="periodBalance" value="${accounthistory.periodbalance}"/>
                                    <c:if test="${!empty periodBalance && fn:contains(periodBalance, ',')}">
                                        <c:set var="periodBalance" value="${fn:replace(periodBalance,',', '')}"/>
                                    </c:if>
                                    <c:if test="${!empty YTDBalance && fn:contains(YTDBalance, ',')}">
                                        <c:set var="YTDBalance" value="${fn:replace(YTDBalance,',', '')}"/>
                                    </c:if>
                                    <fmt:parseNumber type="number" value="${periodBalance}" var="periodBalance"/>
                                    <fmt:parseNumber type="number" value="${YTDBalance}" var="YTDBalance"/>
                                    <c:set var="total" value="${periodBalance+YTDBalance}"/>
                                    <fmt:formatNumber pattern="###,###,##0.00" value="${total}" var="YTDBalance"/>
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Account History displayed.
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner">
                                            No Records Found.
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Account"/>
                                    <display:setProperty name="paging.banner.items_name" value="Accounts"/>
                                    <display:column  property="period" paramId="paramid" title="Period" ></display:column>
                                    <display:column property="enddate" title="Enddate" ></display:column>
                                    <display:column  title="Netchange">
                                        <c:if test="${i>0}">
                                            <a href="${editPath}?index=${i}"><fmt:formatNumber pattern="###,###,##0.00" value="${periodBalance}"/></a>
                                        </c:if>
                                    </display:column>
                                    <display:column title="YTD Balance">
                                        <c:if test="${i>0}">
                                            <c:out value="${YTDBalance}"/>
                                        </c:if>
                                    </display:column>
                                    <c:set var="i" value="${i+1}"></c:set>
                                </display:table>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <script type="text/javascript">
        function sub() {
            if(document.AcctHistoryForm.year.value==""){
                document.AcctHistoryForm.year.focus();
                return false;
            }
            document.forms[0].buttonValue.value="go";
            document.AcctHistoryForm.submit();
        }


        function loadAccountHistory() {
            if(document.AcctHistoryForm.accountNo.value==""){
                document.AcctHistoryForm.accountNo.focus();
                return false;
            }
            document.forms[0].buttonValue.value="loadAccountHistory";
            document.AcctHistoryForm.submit();
        }




        function netChange(obj,val){
            var x=document.getElementById('accounthistory').rows[++val].cells;
            document.AcctHistoryForm.index.value=val;
            document.AcctHistoryForm.buttonValue.value="Netchange";
            document.AcctHistoryForm.submit();
        }
        function addMoreParams(element, entry) {
            return entry;
        }
        if(document.getElementById("accountNo")){
            initAjaxAutoCompleter("accountNo", "accountNoDiv","accountNoValid","${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=accountNo&tabName=ACCOUNT_HISTORY","");
        }

    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery(document).ready(function(){
            jQuery("#accounthistory").tablesorter({widgets: ['zebra'],textExtraction: ['complex']});
        });
    </script>
</html:html>

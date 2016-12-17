<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ page
    import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.struts.form.*,com.gp.cvst.logisoft.hibernate.dao.*"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html>
        <head>
            <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
            <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <base href="${basePath}"/>
            <title>Fiscal Comparision Set</title>
            <%
                    DBUtil dbUtil = new DBUtil();
                    request.setAttribute("currency", dbUtil.getCurrencyList());
                    request.setAttribute("Currencytype", dbUtil.getcurrencytype());
                    request.setAttribute("yearlist", dbUtil.getFiscalYearForStatus("ALL"));
                    request.setAttribute("budgetset", dbUtil.getbudgetset());
                    request.setAttribute("fiscalset", dbUtil.getfiscalset());
                    //request.setAttribute("fiscalset11",dbUtil.getfiscalset1());
                    String fiscalset1 = "";
                    String fiscalset2 = "";
                    fiscalset1 = (String) request.getAttribute("fiscalset1");
                    fiscalset2 = (String) request.getAttribute("fiscalset2");
                    String year1 = "";
                    String year2 = "";
                    year1 = (String) request.getAttribute("year1");
                    year2 = (String) request.getAttribute("year2");
                    List aclist = (List) session.getAttribute("aclist");
                    List comparisondetails = new ArrayList();
                    comparisondetails = (List) request
                                    .getAttribute("comparisondetails");

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
                    String amt = "NetAmt";

                    if (request.getAttribute("amount") != null) {
                            amt = (String) request.getAttribute("amount");
                                                }

                    //this list for loading condition
                    Calendar rightNow = Calendar.getInstance();
                    int year = rightNow.get(Calendar.YEAR);
                    String currentyear = String.valueOf(year);
                    String bs1 = "Budget";
                    String bs2 = "Actuals";

                    if ((request.getAttribute("comparisondetails") == null)) {
                            BudgetDAO bdao = new BudgetDAO();
                            comparisondetails = bdao.findforBudgetforSet1(acct,
                            currentyear, currentyear, bs1);
                            comparisondetails = bdao.findforBudgetforSet2(acct,
                            currentyear, currentyear, bs2);
                            request.setAttribute("comparisondetails", comparisondetails);
                    }
            %>
            <%@include file="../includes/baseResources.jsp" %>
            <%@include file="../includes/baseResourcesForJS.jsp" %>
            <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
            <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
            <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        </head>

        <body class="whitebackgrnd">
            <html:form action="/fiscalcomparison" name="FiscalComarisonsetForm"
                       type="com.gp.cvst.logisoft.struts.form.FiscalComarisonsetForm" scope="request">
                <html:hidden property="buttonValue" />


                <table width="100%" border="0" cellpadding="3" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td>Fiscal Set Comparison</td>
                        <td align="right">
                            <input type="button" value="Go Back" class="buttonStyleNew"
                                   onclick="window.location.href='${path}/jsps/Accounting/ChartOfAccounts.jsp';"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td colspan="2">
                            Load for Another Account
                            <html:text property="accountNo"  value="Click to Change Account No" onclick="this.value='';" styleClass="textlabelsBoldForTextBox"/>
                            <input type="hidden" name="accountNoValid" id="accountNoValid" value="Click to Change Account No"/>
                            <div class="newAutoComplete" id="accountNoDiv"></div>
                            <input type="button" name="search" onclick="loadFiscalComparisionSet()" value="Search" class="buttonStyleNew"/>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew"><td colspan="6">Fiscal Set Comparision Details</td></tr>
                    <tr class="textlabelsBold">
                        <td>Account</td>
                        <td><html:text property="account" value="<%=acct%>" readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                        <td>Description</td>
                        <td><html:text property="desc" value="<%=desc%>" readonly="true" styleClass="textlabelsBoldForTextBox"/></td>
                        <td>Amount</td>
                        <td>
                            <html:radio property="amount" value="NetAmt"/>Net Amt
                            <html:radio property="amount" value="Balance"/>Balance
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Fiscal Set1</td>
                        <td>
                            <html:select property="fiscalset1" onchange="checkForBudget()" styleClass="dropdown_accounting" style="width:100px;">
                                <html:optionsCollection name="fiscalset" />
                            </html:select>
                            <span id="hiddentablesty">
                                <html:select property="budgetset1" styleClass="dropdown_accounting" style="width:40px;">
                                    <html:optionsCollection name="budgetset" />
                                </html:select>
                            </span>
                        </td>
                        <td>Year 1</td>
                        <td>
                            <html:select property="year1" styleClass="dropdown_accounting" value="${year1}">
                                <html:optionsCollection name="yearlist" />
                            </html:select>
                        </td>
                        <td></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Fiscal Set2</td>
                        <td>
                            <html:select property="fiscalset2" onchange="checkForBudget()" value="<%=fiscalset2%>"  styleClass="dropdown_accounting" style="width:100px;">
                                <html:optionsCollection name="fiscalset" />
                            </html:select>
                            <span id="hiddentablesty1">
                                <html:select property="budgetset2" styleClass="dropdown_accounting" style="width:40px;">
                                    <html:optionsCollection name="budgetset" />
                                </html:select>
                            </span>
                        </td>
                        <td>Year 2</td>
                        <td>
                            <html:select property="year2" value="${year1}" styleClass="dropdown_accounting">
                                <html:optionsCollection name="yearlist" />
                            </html:select>
                        </td>

                        <td></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Currency</td>
                        <td>
                            <html:select property="currency" styleClass="dropdown_accounting">
                                <html:optionsCollection name="currency" />
                            </html:select>
                        </td>
                        <td>Currency Type</td>
                        <td>
                            <html:select property="currencytype" styleClass="dropdown_accounting">
                                <html:optionsCollection name="Currencytype" />
                            </html:select>
                        </td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" name="search" onclick="sub()" value="Search" class="buttonStyleNew"/>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="border-left:0px;border-right:0px;border-bottom:0px;">
                    <tr class="tableHeadingNew">List of Values</tr>
                    <tr>
                        <td>
                            <%
                            String path = request.getContextPath();
                            if (path == null) {
                                path = "../..";
                            }
                            if ((comparisondetails != null && comparisondetails.size() > 0)) {
                            int i = 0;
                            String diff = "";
                            String editPath=path+"/acctHistory.do";
                            %>
                            <div id="divtablesty1"  class="scrolldisplaytable">
                                <display:table name="<%=comparisondetails%>" pagesize="<%=pageSize%>"
                                               class="displaytagstyle" sort="list" id="row">

                                    <%
                                     comparisonbean objChartCodeBean = (comparisonbean) comparisondetails.get(i);
                                    String j = objChartCodeBean.getActuals();
                                    String k = objChartCodeBean.getBudget();
                                    NumberFormat number1 = new DecimalFormat("###,###,##0.00");
                                    if (j.contains(",")) {
                                            j = j.replace(",", "");
                                    }
                                    if (k.contains(",")) {
                                            k = k.replace(",", "");
                                    }

                                    double a = Double.parseDouble(j)
                                                    - Double.parseDouble(k);
                                    diff = number1.format(a);
                                     String tempPath1=editPath+"?fiscalCompare="+objChartCodeBean.getPeriod()+"&year="+year1;
                                      String tempPath2=editPath+"?fiscalCompare="+objChartCodeBean.getPeriod()+"&year="+year2;
                                    %>
                                    <!--<display:setProperty name="paging.banner.some_items_found">
    <span class="pagebanner">
    <font color="blue">{0}</font> Comparison Details displayed,For more code click on page numbers.
    </span>
                                    </display:setProperty>-->
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner"> One {0} displayed. Page Number </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner"> {0} {1} Displayed, Page Number </span>
                                    </display:setProperty>

                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner"> No Records Found. </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Period" />
                                    <display:setProperty name="paging.banner.items_name"
                                                         value="Periods" />

                                    <display:column property="period" paramId="paramid" title="Period"></display:column>
                                    <%if(fiscalset1!=null && fiscalset1.equals("Actuals")){
                                    %>
                                    <display:column  paramId="paramid" style="text-align:right;"
                                    title="<%=fiscalset1 + amt + year1%>"><a href="<%=tempPath1%>"><c:out value="${row.actuals}" /></a></display:column>
                                    <%}else{ %>
                                    <display:column property="actuals" style="text-align:right;"
                                    title="<%=fiscalset1 + amt + year1%>"></display:column>
                                    <%} %>
                                    <%if(fiscalset2!=null && fiscalset2.equals("Actuals")){
                                    %>
                                    <display:column property="budget" href="<%=tempPath2%>" style="text-align:right;"
                                    title="<%=fiscalset2 + amt + year2%>"><a href="<%=tempPath2%>"><c:out value="${row.budget}" /></a></display:column>
                                    <%}else{ %>
                                    <display:column property="budget" style="text-align:right;"
                                    title="<%=fiscalset2 + amt + year2%>"></display:column>
                                    <%} %>
                                    <display:column title="Difference" style="text-align:right;">
                                        <c:out value="<%=diff%>" />
                                    </display:column>
                                    <%
                                    i++;
                                    %>
                                </display:table>
                            </div>
                            <%
                            }
                            %>
                        </td></tr></table>
                    </html:form>
        </body>
        <script type="text/javascript">

            function sub()
            {
                if(document.FiscalComarisonsetForm.fiscalset1.value=="Budget" && document.FiscalComarisonsetForm.budgetset1.value=="")
                {

                    alert("Please select BudgetSet");
                    document.FiscalComarisonsetForm.budgetset1.focus();
                    return false;

                }

                if(document.FiscalComarisonsetForm.fiscalset2.value=="Budget"  && document.FiscalComarisonsetForm.budgetset2.value=="")
                {
                    alert("Please select BudgetSet");
                    document.FiscalComarisonsetForm.budgetset2.focus();
                    return false;
                }

                document.forms[0].buttonValue.value="go";
                document.FiscalComarisonsetForm.submit();
            }
            function checkForBudget()
            {
                if(document.FiscalComarisonsetForm.fiscalset1.value=="Budget")
                { toggleTable('hiddentablesty',1);
                }
                else{
                    document.getElementById('hiddentablesty').style.visibility='hidden';
                }
                if(document.FiscalComarisonsetForm.fiscalset2.value=="Budget")
                {
                    // document.getElementById('hiddentablesty1').style.visibility='hidden';
                    toggleTable('hiddentablesty1',1);
                }
                else{
                    document.getElementById('hiddentablesty1').style.visibility='hidden';
                    //toggleTable('hiddentablesty1',1);
                }
            }
            function load()
            {
                if(document.FiscalComarisonsetForm.fiscalset1.value=="Budget")
                { toggleTable('hiddentablesty',1);
                }
                if(document.FiscalComarisonsetForm.fiscalset2.value=="Budget")
                { toggleTable('hiddentablesty1',1);
                }
            }


            function loadFiscalComparisionSet() {
                if(document.FiscalComarisonsetForm.accountNo.value==""){
                    document.FiscalComarisonsetForm.accountNo.focus();
                    return false;
                }
                document.forms[0].buttonValue.value = "loadFiscalComparisionSet";
                document.FiscalComarisonsetForm.submit();
            }
            load();
            function addMoreParams(element, entry) {
                return entry;
            }
            if(document.getElementById("accountNo")){
                initAjaxAutoCompleter("accountNo", "accountNoDiv","accountNoValid","${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=accountNo&tabName=ACCOUNT_HISTORY","");
            }
        </script>
    </html>

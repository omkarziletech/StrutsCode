<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <title>Budget</title>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/AccountDetailsDAO.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <style type="text/css">
        .hideField{
            display: none;
        }
        .showField{
            display: block;
        }
        .headerdiv{
            background-color: #8DB7D6;
            font-family: Arial;
            color: black;
            font-size: 12px;
        }
    </style>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form  action="/budget" method="post" enctype="multipart/form-data"
                    type="com.gp.cvst.logisoft.struts.form.BudgetForm" name="budgetForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <html:hidden property="action"/>
                <tr class="tableHeadingNew">
                    <td colspan="3">Budget</td>
                    <td colspan="3" align="right">
                        <input value="Go Back" onclick="window.location='${path}/budget.do?action=gotoChartOfAccounts'" class="buttonStyleNew"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        <table width="100%" border="0" cellpadding="0" cellspacing="3">
                            <tr class="textlabelsBold">
                                <td colspan="2" align="center"class="headerdiv">Main Account</td>
                                <td colspan="2" align="center"class="headerdiv">Copy Account</td>
                                <td colspan="2" align="center"class="headerdiv">Operations</td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Account</td>
                                <td>
                                    <html:text property="mainAccount" styleClass="textlabelsBoldForTextBox"/>
                                    <input type="hidden" name="mainAccountValid" id="mainAccountValid" value="${budgetForm.mainAccount}"/>
                                    <div class="newAutoComplete" id="mainAccountDiv"></div>
                                </td>
                                <td>Account</td>
                                <td>
                                    <html:text property="copyAccount" styleClass="textlabelsBoldForTextBox"/>
                                    <input type="hidden" name="copyAccountValid" id="copyAccountValid" value="${copy.mainAccount}"/>
                                    <div class="newAutoComplete" id="copyAccountDiv"></div>
                                </td>
                                <td>Budget Method</td>
                                <td>
                                    <html:select property="budgetMethod" onchange="displayIncreaseBy(this)" styleClass="dropdown_accounting" style="width:125px">
                                        <html:optionsCollection property="budgetMethods"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Description</td>
                                <td><html:text property="mainDescription" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>Description</td>
                                <td><html:text property="copyDescription" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>Amount</td>
                                <td>
                                    <html:text property="amount" styleClass="textlabelsBoldForTextBox"/>
                                    <input type="button" name="search" value="Update" onclick="updateBudget()" class="buttonStyleNew"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Budget Set</td>
                                <td>
                                    <html:select property="mainBudgetSet" onchange="getMainAccountBudget()" styleClass="dropdown_accounting" style="width:90px;">
                                        <html:optionsCollection property="budgetSets"/>
                                    </html:select>
                                </td>
                                <td>Fiscal Set</td>
                                <td>
                                    <html:radio property="fiscalSet" value="actual" onclick="document.budgetForm.copyBudgetSet.style.display='none';getCopyAccountBudget();"/>Actual
                                    <html:radio property="fiscalSet" value="budget" onclick="document.budgetForm.copyBudgetSet.style.display='block';getCopyAccountBudget();"/>Budget
                                    <c:set var="display" value="hideField"/>
                                    <c:if test="${budgetForm.fiscalSet=='budget'}">
                                        <c:set var="display" value="showField"/>
                                    </c:if>
                                    <html:select property="copyBudgetSet" onchange="getCopyAccountBudget()" styleClass="dropdown_accounting ${display}" style="width:90px;">
                                        <html:optionsCollection property="budgetSets"/>
                                    </html:select>
                                </td>
                                <c:set var="display" value="hideField"/>
                                <c:if test="${budgetForm.budgetMethod=='Base,Amount Increase' || budgetForm.budgetMethod=='Base,Percent Increase'}">
                                    <c:set var="display" value="showField"/>
                                </c:if>
                                <td id="increaseByLabel" class="${display}">Increase by</td>
                                <td id="increaseByValue" class="${display}"><html:text property="increaseBy" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Year</td>
                                <td>
                                    <html:select property="mainYear" onchange="getMainAccountBudget()" styleClass="dropdown_accounting" style="width:90px;">
                                        <html:optionsCollection property="years"/>
                                    </html:select>
                                </td>
                                <td>Year</td>
                                <td>
                                    <html:select property="copyYear" onchange="getCopyAccountBudget()" styleClass="dropdown_accounting" style="width:90px;">
                                        <html:optionsCollection property="years"/>
                                    </html:select>
                                </td>
                                <td>Budget Sheet</td>
                                <td>
                                    <html:file property="budgetSheet" styleClass="textlabelsBoldForTextBox" />
                                    <input type="button" value="Import" onclick="importBudget()" class="buttonStyleNew"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Currency</td>
                                <td>
                                    <html:select property="mainCurrency" styleClass="dropdown_accounting" style="width:90px;">
                                        <html:optionsCollection property="currencies"/>
                                    </html:select>
                                </td>
                                <td>Currency</td>
                                <td>
                                    <html:select property="copyCurrency" styleClass="dropdown_accounting" style="width:90px;">
                                        <html:optionsCollection property="currencies"/>
                                    </html:select>
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td colspan="6" align="center">
                                    <input type="button" value="Edit" onclick="editBudget()" class="buttonStyleNew"/>
                                    <input type="button" value="Save" onclick="saveBudget()" class="buttonStyleNew"/>
                                    <input type="button" value="Copy" onclick="copyBudget()" class="buttonStyleNew"/>
                                </td>
                            </tr>
                            <tr class="tableHeadingNew">
                                <td colspan="6">Budget Details</td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <div id="budgetsdiv" class="scrolldisplaytable textlabelsBold">
                                        <c:choose>
                                            <c:when test="${not empty budgetForm.budgets}">
                                                <c:set var="index" value="0"/>
                                                <display:table name="${budgetForm.budgets}" pagesize="${pageSize}" id="budget" class="displaytagstyleNew">
                                                    <display:setProperty name="paging.banner.placement" value="none"/>
                                                    <display:column title="Account" property="account"/>
                                                    <display:column title="Budget Set" property="budgetSet"/>
                                                    <display:column title="Period" property="period"/>
                                                    <display:column title="End Date" property="endDate"/>
                                                    <display:column title="Amount">
                                                        <fmt:formatNumber var="budgetAmount" value="${budget.budgetAmount}" pattern="########0.00"/>
                                                        <c:choose>
                                                            <c:when test="${budgetForm.action=='editBudget' || budgetForm.action=='updateBudget' 
                                                                            || budgetForm.action=='copyBudget' || budgetForm.action=='importBudget'}">
                                                                <html:text property="budgets[${index}].budgetAmount" value="${budgetAmount}" styleClass="textlabelsBoldForTextBox"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:out value="${budgetAmount}"/>
                                                                <html:hidden property="budgets[${index}].budgetAmount" value="${budget.budgetAmount}"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <html:hidden property="budgets[${index}].id" value="${budget.id}"/>
                                                        <html:hidden property="budgets[${index}].account" value="${budget.account}"/>
                                                        <html:hidden property="budgets[${index}].period" value="${budget.period}"/>
                                                        <html:hidden property="budgets[${index}].budgetSet" value="${budget.budgetSet}"/>
                                                        <html:hidden property="budgets[${index}].year" value="${budget.year}"/>
                                                        <html:hidden property="budgets[${index}].endDate" value="${budget.endDate}"/>
                                                    </display:column>
                                                    <c:set var="index" value="${index+1}"/>
                                                </display:table>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="No budget details found"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <script type="text/javascript" src="${path}/js/Accounting/GeneralLedger/budget.js"></script>
        </html:form>
    </body>
</html>

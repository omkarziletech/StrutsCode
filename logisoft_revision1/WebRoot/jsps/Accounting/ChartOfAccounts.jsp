<%@include file="../includes/jspVariables.jsp" %>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,java.util.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ page import="com.gp.cong.logisoft.domain.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <c:set var="editPath" value="${path}/chartOfAccts.do"></c:set>
        <base href="${basePath}"/>
        <title>ChartOfAccounts.jsp</title>
        <c:if test="${!empty sessionScope.TransactionHistory}">
            <c:remove var="TransactionHistory" scope="session"/>
            <jsp:forward page="TransactionHistory.jsp" />
        </c:if>
        <%            DBUtil dbUtil = new DBUtil();
            String buttonValue = "";
            List accountdetail = new ArrayList();
            if (null != request.getAttribute("buttonValue")) {
                buttonValue = (String) request.getAttribute("buttonValue");
            }
            request.setAttribute("accountList", dbUtil.getAcctDetails());
            request.setAttribute("Accttype", dbUtil.getAccttype());
            String actype = (String) request.getAttribute("acctype");
            List accountGroupList = (List) session.getAttribute("AcctGroup");
            request.setAttribute("accountGroupList", accountGroupList);
            accountdetail = (List) session.getAttribute("accountdetail");
            String acct = "";
            String acct1 = "";
            String group = "";
            LineItem line = new LineItem();
            LineItem line1 = new LineItem();
            if (request.getAttribute("Acctgroup") != null) {
                group = (String) request.getAttribute("Acctgroup");
            }

            if (session.getAttribute("acct") != null) {
                acct = (String) session.getAttribute("acct");
            }
            if (session.getAttribute("acct1") != null) {
                acct1 = (String) session.getAttribute("acct1");
            }
            String acctStatus = "";
            if (request.getAttribute("acctStatus") != null) {
                acctStatus = (String) request.getAttribute("acctStatus");
            }


        %>

        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <c:set var="accessMode" value="2"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <style type="text/css">
                .displayMode{
                    display:none;
                }
            </style>
        </c:if>
    </head>
    <body class="whitebackgrnd">
        <%@include file="../preloader.jsp"%>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form  action="/chartOfAccts.do?accessMode=${accessMode}" name="ChartOfAccountsForm"  type="com.gp.cvst.logisoft.struts.form.ChartOfAccountsForm" scope="request" styleId="chartOfAccountsForm">
            <html:hidden property="buttonValue" value="<%=buttonValue%>" styleId="buttonValue"/>
            <html:hidden property="index"/>
            <table  border="0" width="100%" cellpadding="0" cellspacing="0">
                <tr class="tableHeadingNew" >
                    <td colspan="2" onclick="$('#search_vertical_slide').slideToggle()">Chart of Accounts</td>
                </tr>
                <tr>
                    <td colspan="2" class="tableBorderNew">
                        <div id="search_vertical_slide">
                            <div>
                                <table width="100%" border="0" cellpadding="3" cellspacing="0">
                                    <tr class="textlabelsBold">
                                        <td>Starting Account </td>
                                        <td>
                                            <input name="acct" id="acct" value="<%=acct%>" size="20" class="textlabelsBoldForTextBox"/>
                                            <input type="hidden" name="acctCheck" id="acctCheck" value="<%=acct%>"/>
                                        </td>
                                        <td>Ending Account </td>
                                        <td>
                                            <input name="acct1" id="acct1" value="<%=acct1%>" size="20" class="textlabelsBoldForTextBox"/>
                                            <input type="hidden" name="acct1Check" id="acct1Check" value="<%=acct1%>"/>
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Account Type</td>
                                        <td>
                                            <html:select property="accttype"   value="<%=actype%>" styleClass="dropdown_accounting" style="width: 125px;border:1px solid #c4c5c4">
                                                <html:optionsCollection name="Accttype" styleClass="unfixedtextfiledstyle"/>
                                            </html:select>
                                        </td>
                                        <td>Account Group </td>
                                        <td>
                                            <%if (accountGroupList != null) {%>
                                            <html:select property="acctGroup" value="<%=group%>"  styleClass="dropdown_accounting" style="width: 125px; border:1px solid #c4c5c4">
                                                <html:optionsCollection name="accountGroupList" styleClass="unfixedtextfiledstyle"/>
                                            </html:select>
                                            <%} else { %>
                                            <select name="acctGroup" class="dropdown_accounting" style="width: 125px;  border:1px solid #c4c5c4">
                                                <option value="" class="unfixedtextfiledstyle">Select Acct Group</option>
                                            </select>
                                            <%} %>
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td width="97">Account Status </td>
                                        <td>
                                            <% if (acctStatus.equals("InActive")) {%>
                                            <input type="radio" name="acctStatus" value="Active" />Active
                                            <input type="radio" name="acctStatus" value="InActive" checked="checked"/> Inactive
                                            <%} else {%>
                                            <input type="radio" name="acctStatus" value="Active" checked="checked" />Active
                                            <input type="radio" name="acctStatus" value="InActive"/>Inactive
                                            <%}%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top" colspan="5" align="center">
                                            <input type="button" name="search" onclick="search1()" value="Search" class="buttonStyleNew" />
                                            <input type="button" name="search" onclick="showall1();" value="Show All"  class="buttonStyleNew" />
                                            <input type="button" name="button1" onclick="window.location.href = '${path}/jsps/Accounting/AccountMaintenance.jsp'" value="Add New "  class="buttonStyleNew displayMode" />
                                            <input type="button" name="export" onclick="exportToExcel()" value="Export To Excel" class="buttonStyleNew displayMode" style="width: 100px"/>
                                            <%session.removeAttribute("subLedgeraddList");
                                                session.removeAttribute("SegList");%>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>List Of Accounts</td>
                </tr>
                <tr>
                    <td>
                        <c:if test="${!empty ChartofaccountsList && fn:length(ChartofaccountsList)>0}">
                            <c:set var="i" value="0"/>
                            <div id="divtablesty1" class="scrolldisplaytable" style="width:100%;overflow: auto">
                                <display:table name="${ChartofaccountsList}" pagesize="250"
                                               class="displaytagstyleNew" sort="list"  id="accounthistory" style="width:100%;">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Account details displayed,For more code click on page numbers.
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">
                                            One {0} displayed. Page Number
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">
                                            {0} {1} Displayed, Page Number
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
                                    <c:choose>
                                        <c:when test="${accessMode==0}">
                                            <display:column property="acct" paramId="paramid" title="Acct" sortable="true" headerClass="sortable"></display:column>
                                        </c:when>
                                        <c:otherwise>
                                            <display:column href="${editPath}" property="acct" paramId="paramid" title="Acct" sortable="true" headerClass="sortable"></display:column>
                                        </c:otherwise>
                                    </c:choose>
                                    <display:column title="Desc" sortable="true" headerClass="sortable" style="text-transform:uppercase;">
                                        <c:if test="${fn:length(accounthistory.desc)<=20}">
                                            <span class="hotspot">
                                                <c:out  value="${accounthistory.desc}"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${fn:length(accounthistory.desc)>20}">
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>${accounthistory.desc}</strong>', null, event);"
                                                  onmouseout="tooltip.hide();">
                                                <c:out  value="${fn:substring(accounthistory.desc,0,20)}..."/>
                                            </span>
                                        </c:if>
                                    </display:column>
                                    <display:column property="normalbalance" title="Normalbalance"  paramId="paramid" maxLength="15" 
                                                    sortable="true" headerClass="sortable" style="text-transform:uppercase;"></display:column>
                                    <display:column property="multicurrency" title="MultiCurrency"  paramId="paramid" maxLength="15" 
                                                    sortable="true" headerClass="sortable" style="text-transform:uppercase;"></display:column>
                                    <display:column property="status" title="Status"  paramId="paramid" maxLength="15" sortable="true" headerClass="sortable"></display:column>
                                    <display:column property="acctType" title="AcctType"  paramId="paramid" maxLength="15" sortable="true" headerClass="sortable"></display:column>
                                    <display:column title="Group Description"  paramId="paramid" sortable="true" headerClass="sortable" style="text-transform:uppercase;">
                                        <c:if test="${fn:length(accounthistory.description)<=20}">
                                            <span class="hotspot">
                                                <c:out  value="${accounthistory.description}"/>
                                            </span>
                                        </c:if>
                                        <c:if test="${fn:length(accounthistory.description)>20}">
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>${accounthistory.description}</strong>', null, event);"
                                                  onmouseout="tooltip.hide();">
                                                <c:out  value="${fn:substring(accounthistory.description,0,20)}..."/>
                                            </span>
                                        </c:if>
                                    </display:column>
                                    <display:column property="controlAcct" title="Control Acct"  paramId="paramid" maxLength="10" 
                                                    sortable="true" headerClass="sortable" style="text-transform:uppercase;"></display:column>
                                    <c:choose>
                                        <c:when test="${accessMode==0}">
                                        </c:when>
                                        <c:otherwise>
                                            <display:column title="Actions">
                                                <img alt="" title="Account" src="${path}/img/icons/acct.gif" border="0" onclick="accountHistory(this,${i})" />
                                                <img alt="" title="Transaction" src="${path}/img/icons/transaction.gif" border="0" onclick="TransHistory(this,${i})" />
                                                <img alt="" title="Comparison" src="${path}/img/icons/comparison.gif" border="0" onclick="FiscalComparison(this,${i})" />
                                                <img alt="" title="Budget" src="${path}/img/icons/budget.gif" border="0" onclick="gotoBudget('${accounthistory.acct}')" />
                                                <img alt="" title="Currency" src="${path}/img/icons/currency.gif" border="0" onclick="alert('Coming Soon...');"  />
                                                <img alt="" title="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=' + '${notesConstants.GL_ACCOUNT}&moduleRefId=${accounthistory.acct}', 300, 900);"/>
                                            </display:column>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:set var="i" value="${i+1}"></c:set>
                                </display:table>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
            <c:choose>
                <c:when test="${accessMode==0}">
                </c:when>
                <c:otherwise>
                    <table>
                        <tr>
                            <td>
                                <img border="0" src="${path}/img/icons/acct_des.gif"/>
                                <img border="0" src="${path}/img/icons/transaction_des.gif"/>
                                <img border="0" src="${path}/img/icons/comp_des.gif"/>
                                <img border="0" src="${path}/img/icons/budget_des.gif"/>
                                <img border="0" src="${path}/img/icons/currency_des.gif"/>
                            </td>
                        </tr>
                    </table>
                </c:otherwise>
            </c:choose>
        </html:form>
    </body>
    <script type="text/javascript">
        var path = "${path}";
        function showall1() {
            document.ChartOfAccountsForm.buttonValue.value = "showall";
            document.ChartOfAccountsForm.submit();
        }
        function accountHistory(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "AcctHistory";
            document.ChartOfAccountsForm.submit();
        }
        function TransHistory(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "Transactionhitory";
            document.ChartOfAccountsForm.submit();
        }

        function FiscalComparison(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "Fiscalcomparisonset";
            document.ChartOfAccountsForm.submit();
        }
        function gotoBudget(account) {
            window.location = "${path}/budget.do?action=getMainAccountBudget&mainAccount=" + account;
        }
        function searchTransHistory(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "serachTransactionhitory";
            document.ChartOfAccountsForm.submit();
        }
        function searchaccountHistory(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "searchaccthistory";
            document.ChartOfAccountsForm.submit();
        }
        function searchFiscalComparison(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "searchFiscalcomparisonset";
            document.ChartOfAccountsForm.submit();
        }
        function searchbudget(obj, val) {
            var x = document.getElementById('accounthistory').rows[++val].cells;
            document.ChartOfAccountsForm.index.value = val;
            document.forms[0].buttonValue.value = "searchbudget";
            document.ChartOfAccountsForm.submit();
        }
        function removeAcctValues() {
            document.ChartOfAccountsForm.buttonValue.value = "removeAcctValues";
            document.ChartOfAccountsForm.submit();
        }
        function search1() {
            document.ChartOfAccountsForm.buttonValue.value = "search";
            document.ChartOfAccountsForm.submit();
        }

        function submit1() {
            if (document.forms[0].accttype.value == "Select AcctType") {
                alert("please enter Account Type");
                return false;
            }
            document.forms[0].buttonValue.value = "AccttypeSelected";
        }

        $(document).ready(function () {
            $("#acct,#acct1").initautocomplete({
                url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
                width: "350px",
                resultsClass: "ac_results z-index",
                resultPosition: "fixed",
                scroll: true,
                scrollHeight: 300
            });
            $(document).keypress(function (event) {
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if (keycode == 13) {
                    search1();
                }
            });
        });
        function exportToExcel() {
            var params = new Object();
            params.className = "com.gp.cong.logisoft.dwr.UploadDownloaderDWR";
            params.methodName = "exportToExcelForChartOfAccounts";
            params.formName = "com.gp.cvst.logisoft.struts.form.ChartOfAccountsForm";
            params.request = "true";
            params.acct = $("#acct").val();
            params.acctStatus = $("input[name='acctStatus']:checked").val();
            params.accttype = $("#accttype").val();
            params.acct1 = $("#acct1").val();
            params.acctGroup = $("#acctGroup").val();
            params.buttonValue = $("#buttonValue").val();
            $.ajaxx({
                data: params,
                preloading: true,
                success: function (fileName) {
                    var url = path + "/servlet/FileViewerServlet.do?fileName=" + fileName;
                    var iframe = $("<iframe/>", {
                        name: "iframe",
                        id: "iframe",
                        src: url
                    }).appendTo("body").hide();
                    iframe.load(function () {
                        iframe.remove();
                    });
                }
            });
        }
    </script>

</html>



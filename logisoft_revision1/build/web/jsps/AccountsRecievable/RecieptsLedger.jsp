<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<html:html>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <head>
            <base href="${basePath}">
        <title> Reciepts Ledger</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <%@include file="../includes/baseResources.jsp" %>
        <style type="text/css">
            #undoBatchDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
            }
            #invalid {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
            }
            #newcover {
                display: none;
                position: absolute;
                overflow: visible;
                vertical-align: super;
                left: 0px;
                top: 0px;
                width: 100%;
                height: 100%;
                background: #000;
                filter: alpha(Opacity = 50);
                opacity: 0.5;
                -moz-opacity: 0.5;
                -khtml-opacity: 0.5
            }
        </style>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/ArDwr.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/GeneralLedgerDwr.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/ARApplypaymentsBC.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/UploadDownloaderDWR.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/FiscalPeriodBC.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript">
            function addMoreParams(element, entry) {
                return entry + "&tabName=SUBLEDGER";
            }
        </script>
    </head>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newcover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form  action="/receiptsLedger" method="post" name="recieptsLedgerForm" type="com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm" scope="request" >
            <html:hidden property="buttonValue"/>
            <html:hidden property="index"/>
            <c:if test="${!empty errorMsg}">
                <div class="error" id="error">
                    <c:out value="${errorMsg}"></c:out>
                    </div>
            </c:if>
            <table class="tableBorderNew" width="100%" cellpadding="2" cellspacing="0">
                <tr class="tableHeadingNew">
                <td colspan="6">Sub Ledgers</td>
            </tr>
            <tr class="textlabelsBold">
            <td>Sub Ledger Type</td>
            <td>
                <html:select property="subLedgerType" styleId="subLedgerType" styleClass="dropdown_accounting" style="width:100px" onchange="showFilters()">
                    <html:optionsCollection name="sourceCodeList" styleClass="unfixedtextfiledstyle"/>
                </html:select>
            </td>
            <td>GL Period</td>
            <td>
                <input type="text" name="periodDis" id="periodDis" value="${periodDis}" class="textlabelsBoldForTextBox" style="width:100px"/>
                <html:hidden property="period" styleId="period" value="${recieptsLedgerForm.period}"/>
                <input type="hidden" name="periodValid" id="periodValid" value="${periodDis}"/>
                <div id="periodChoices" style="display: none" class="newAutoComplete"></div>
            </td>
            <td colspan="2">Show Posted
                <html:radio value="yes" property="posted" onclick="showOrHidePost()">Yes</html:radio>
                <html:radio value="no" property="posted" onclick="showOrHidePost()">No</html:radio>
                </td>
            </tr>
            <tr class="textlabelsBold">
            <td>Start Date</td>
            <td>
            <html:text property="startDate" styleId="startDate"  value="${startDate}" styleClass="textlabelsBoldForTextBox" style="width:100px" onchange="validateStartDate()"></html:text>
            </td>
            <td>End Date</td>
            <td>
            <html:text property="endDate" styleId="endDate" value="${endDate}"  styleClass="textlabelsBoldForTextBox" style="width:100px" onchange="validateEndDate()"></html:text>
            </td>
            <td id="filterForPJ" colspan="2" style="display:none;">
                Sort by
                <select name="sortForPJSubLedger" id="sortForPJSubLedger" style="width:100px" onChange="changeSortByValue(this)" class="textlabelsBoldForTextBox">
                <c:forEach var="labelValueBean" items="${sortByList}">
                    <option value="${labelValueBean.value}" class="unfixedtextfiledstyle"><c:out value="${labelValueBean.label}"/></option>
                </c:forEach>
            </select>
        </td>
    </tr>

    <tr id="filterForAll" class="textlabelsBold">
    <td>Rev/Exp</td>
    <td>
        <html:select property="revOrExp"  style="width:100px" onchange="changeChargeCodeOptions()" styleClass="dropdown_accounting">
            <html:optionsCollection name="revOrExpList" styleClass="unfixedtextfiledstyle"/>
        </html:select>
    </td>
    <td>Charge Code</td>
    <td>
        <html:select property="chargeCode"  style="width:100px" onchange="enableGLAccts()" styleClass="dropdown_accounting">
            <html:optionsCollection name="chargeCodeList" styleClass="unfixedtextfiledstyle"/>
        </html:select>
    </td>
    <td colspan="2">
        Sort by
        <select name="sortForAllSubLedgers" id="sortForAllSubLedgers" class="dropdown_accounting" style="width:100px" onChange="changeSortByValue(this)">
            <c:forEach var="labelValueBean" items="${sortByListForAllSubledgers}">
                <option value="${labelValueBean.value}" class="unfixedtextfiledstyle"><c:out value="${labelValueBean.label}"/></option>
            </c:forEach>
        </select>
    </td>
</tr>
<tr id="glAcctsForAll" style="display:none;" class="textlabelsBold">
<td>Start GLAccount</td>
<td><html:text property="startGLAccount" styleClass="textlabelsBoldForTextBox" style="width:100px"></html:text>
    <input type="hidden" name="startGLAccountValid" id="startGLAccountValid" value="${recieptsLedgerForm.startGLAccount}"/>
    <div class="newAutoComplete" id="startGLAccountDiv"></div>
</td>
<td>End GLAccount</td>
<td><html:text property="endGLAccount" styleClass="textlabelsBoldForTextBox" style="width:100px"></html:text>
    <input type="hidden" name="endGLAccountValid" id="endGLAccountValid" value="${recieptsLedgerForm.endGLAccount}"/>
    <div class="newAutoComplete" id="endGLAccountDiv"></div></td>
<td colspan="2"></td>
</tr>

<tr>
<td colspan="6" align="center">
    <input type="button" class="buttonStyleNew" value="Search" onClick="detials()"/>
    <input type="button" class="buttonStyleNew" value="Summary" onClick="go()"/>
    <input type="button" id="postButton" class="buttonStyleNew" value="Post"  onclick="postGeneralLedger()"  style="width:65px"/>
    <input type="button" class="buttonStyleNew" value="Undo" onClick="showUndoBatchDiv()"/>
    <input type="button" class="buttonStyleNew" value="ExportToExcel" onClick="exportToExcel()" style="width:80px"/>
    <input type="button" class="buttonStyleNew" value="Print" onClick="generateReport()"/>
    <input type="button" class="buttonStyleNew" value="Show Records without reporting date" onClick="getRecordsOfNullReportingDate()" style="width:200px"/>
    <input type="button" class="buttonStyleNew" value="Export Records without reporting date" onClick="exportRecordsOfNullReportingDate()" style="width:200px"/>
</td>
</tr>
</table>
<div id="undoBatchDiv" style="display: none;width: 150px;height: 50px;">
    <table width="100%" cellpadding="0" cellspacing="0">
        <tbody>
            <tr class="tableHeadingNew">
            <td class="lightBoxHeader">
				Undo Batch
            </td>
            <td align="right">
                <div>
                    <a id="lightBoxClose"  href="javascript:closeUndoBatchDiv();">
                        <img  alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
            </tr>
            <tr class="textlabelsBold">
            <td>GL Batch</td>
            <td>
                <html:text property="undoBatch" styleClass="textlabelsBoldForTextBox"/>
                <input type="button" class="buttonStyleNew" value="Go" onclick="undoGlBatch()" style="width: 20px;"/>
            </td>
            </tr>
        </tbody>
    </table>
</div>
<br/>
<table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0">
    <tr class="tableHeadingNew">
    <td>
        <c:out value="List of ${subLedgerType}"/>
    <td>
        </tr>
    <tr>
    <td>
        <c:if test="${not empty subledgerList}">
            <div class="scrolldisplaytable">
                <c:set var="i" value="0"></c:set>
                <c:choose>
                    <c:when test="${summary=='yes'}">
                        <display:table name="${subledgerList}" pagesize="1000"
                                       class="displaytagstyleNew" id="transactionBean" sort="list"  excludedParams="*">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner"> <font color="blue">{0}</font>
                                    SubLedger Displayed,For more Data click on Page Numbers.
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.one_item_found">
                                <span class="pagebanner"> One {0} displayed. Page Number </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.all_items_found">
                                <span class="pagebanner"> {0} {1} Displayed, Page Number </span>
                            </display:setProperty>
                            <display:setProperty name="basic.msg.empty_list">
                                <span class="pagebanner"> </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.placement" value="bottom"/>
                            <display:setProperty name="paging.banner.item_name" value="SubLedger"/>
                            <display:setProperty name="paging.banner.items_name" value="SubLedgers"/>
                            <display:column title="GLAccount#" property="glAcctNo" sortable="true" headerClass="sortable"/>
                            <display:column title="Transaction<br>Amount" property="transactionAmount" sortProperty="transactionAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                            <display:column title="Debit" property="debitAmount"  sortProperty="debitAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                            <display:column title="Credit" property="creditAmount"  sortProperty="creditAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                        </display:table>
                    </c:when>
                    <c:otherwise>
                        <display:table name="${subledgerList}" pagesize="1000"
                                       class="displaytagstyleNew" id="transactionBean" sort="list"  excludedParams="*">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner"> <font color="blue">{0}</font>
						SubLedger Displayed,For more Data click on Page Numbers.
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.one_item_found">
                                <span class="pagebanner"> One {0} displayed. Page Number </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.all_items_found">
                                <span class="pagebanner"> {0} {1} Displayed, Page Number </span>
                            </display:setProperty>
                            <display:setProperty name="basic.msg.empty_list">
                                <span class="pagebanner"> </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.placement" value="bottom"/>
                            <display:setProperty name="paging.banner.item_name" value="SubLedger"/>
                            <display:setProperty name="paging.banner.items_name" value="SubLedgers"/>
                            <display:column title="SubLedger" sortable="true" headerClass="sortable">
                                <c:choose>
                                    <c:when test="${summary=='yes' && recieptsLedgerForm.subLedgerType!='ALL'}">
                                        <c:out value="${recieptsLedgerForm.subLedgerType}"/>
                                    </c:when>
                                    <c:when test="${summary=='yes' && recieptsLedgerForm.subLedgerType=='ALL'}">
                                        <c:out value="${transactionBean.subLedgerCode}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when
                                                test="${(empty transactionBean.subLedgerCode || fn:trim(transactionBean.subLedgerCode)=='')
                                                        && summary!='yes'}">
                                                <input type="hidden" id="subLedgerCodeIndex" value="${i}"/>
                                                <input type="text" name="subLedgerCode${i}"
                                                       id="subLedgerCode${i}" maxlength="19" class="smalltextstyle"/>
                                                <input type="hidden" name="subLedgerCodeValid${i}" id="subLedgerCodeValid${i}"/>
                                                <div class="newAutoComplete" id="subLedgerCodeDiv${i}"></div>
                                                <img src="${path}/img/icons/ok.gif"
                                                     onclick="saveSubLedgerCode('subLedgerCode${i}','${transactionBean.transactionId}')"
                                                     onmouseover="tooltip.show('<strong>Save SubLedger</strong>',null,event);"
                                                     onmouseout="tooltip.hide();" style="cursor: pointer;"/>
                                                <script type="text/javascript">
                                                    initAjaxAutoCompleter("subLedgerCode${i}", "subLedgerCodeDiv${i}", "subLedgerCodeValid${i}", "${path}/servlet/AutoCompleterServlet?action=SubLedger&textFieldId=subLedgerCode${i}","");
                                                </script>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${transactionBean.subLedgerCode}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="ApBatchId" sortable="true" headerClass="sortable">
                                <c:if test="${!empty transactionBean.apBatchId && transactionBean.apBatchId!=0}">
                                    <c:out value="${transactionBean.apBatchId}"></c:out>
                                </c:if>
                            </display:column>
                            <display:column title="ArBatchId" sortable="true" headerClass="sortable">
                                <c:if test="${!empty transactionBean.arBatchId && transactionBean.arBatchId!=0}">
                                    <c:out value="${transactionBean.arBatchId}"></c:out>
                                </c:if>
                            </display:column>
                            <display:column title="GLAccount#" sortable="true" headerClass="sortable">
                                <input type="hidden" id="glAcctIndex" value="${i}"/>
                                <input type="text" name="glAccountNo${i}" value="${transactionBean.glAcctNo}"
                                       id="glAccountNo${i}" maxlength="19" class="textlabelsBoldForTextBox" size="10"/>
                                <input type="hidden" name="glAccountNoValid${i}" id="glAccountNoValid${i}" value="${transactionBean.glAcctNo}"/>
                                <div class="newAutoComplete" id="glAccountDiv${i}"></div>
                                <img alt="" src="${path}/img/icons/ok.gif" onclick="saveGlAccount('glAccountNo${i}','${transactionBean.transactionId}')"
                                     onmouseover="tooltip.show('<strong>Save GLAccount#</strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer;"/>
                                <script type="text/javascript">
                                    initAjaxAutoCompleter("glAccountNo${i}", "glAccountDiv${i}", "glAccountNoValid${i}", "${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=glAccountNo${i}","");
                                </script>
                            </display:column>
                            <display:column title="BL#" property="billofLadding" sortable="true" headerClass="sortable"/>
                            <display:column title="Invoice#" property="invoiceOrBl" sortable="true" headerClass="sortable"/>
                            <display:column title="Charge<br>Code" property="chargeCode" sortable="true" headerClass="sortable"/>
                            <display:column title="Voyage" property="voyagenumber" sortable="true" headerClass="sortable"/>
                            <display:column title="Transaction<br>Date" property="transDate" sortable="true" headerClass="sortable"/>
                            <display:column title="Reporting<br>Date" property="sailingDate" sortable="true" headerClass="sortable" sortProperty="sailingDate" format="{0,date,MM/dd/yyyy}"/>
                            <display:column title="Posted<br>Date" property="postedDate" sortable="true" headerClass="sortable" sortProperty="postedToGlDate" format="{0,date,MM/dd/yyyy}"/>
                            <display:column title="Transaction<br>Amount" property="transactionAmount" sortProperty="transactionAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                            <display:column title="Debit" property="debitAmount"  sortProperty="debitAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                            <display:column title="Credit" property="creditAmount"  sortProperty="creditAmount" format="{0,number,###,###,##0.00}"
                                            sortable="true" style="text-align:right" headerClass="sortable"/>
                            <display:column title="Record<br>Type" property="recordType" sortable="true" headerClass="sortable"/>
                            <display:column title="LineItem#" property="lineitemNo" sortable="true" headerClass="sortable"/>
                            <display:column title="JournalEntry#" property="journalentryNo" sortable="true" headerClass="sortable"/>
                            <c:set var="i" value="${i+1}"/>
                        </display:table>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </td>
</tr>
</table>
<html:hidden property="sortBy"/>
</html:form>
</body>
<script type="text/javascript">
    dwr.engine.setTextHtmlHandler(dwrSessionError);
    function showFilters(){
        if(document.recieptsLedgerForm.subLedgerType.value=="PJ"){
            document.getElementById("filterForAll").style.visibility="hidden";
            document.getElementById("filterForPJ").style.visibility="visible";
            document.getElementById("sortForPJSubLedger").value=document.recieptsLedgerForm.sortBy.value;
        }else if(document.recieptsLedgerForm.subLedgerType.value=="ALL"){
            document.getElementById("filterForAll").style.visibility="visible";
            document.getElementById("filterForPJ").style.visibility="hidden";
            document.getElementById("sortForAllSubLedgers").value=document.recieptsLedgerForm.sortBy.value;
        }else{
            document.getElementById("filterForAll").style.visibility="hidden";
            document.getElementById("filterForPJ").style.visibility="hidden";
        }
        enableGLAccts();
    }
    function detials(){
        if(trim(document.recieptsLedgerForm.startDate.value)==""){
            alert("Please Enter Start Date");
            document.recieptsLedgerForm.startDate.focus();
        }else if(trim(document.recieptsLedgerForm.endDate.value)==""){
            alert("Please Enter End Date");
            document.recieptsLedgerForm.endDate.focus();
        }else{
            document.recieptsLedgerForm.buttonValue.value="searchDetails";
            document.recieptsLedgerForm.submit();
        }
    }
    function go(){
        if(trim(document.recieptsLedgerForm.startDate.value)==""){
            alert("Please Enter Start Date");
            document.recieptsLedgerForm.startDate.focus();
        }else if(trim(document.recieptsLedgerForm.endDate.value)==""){
            alert("Please Enter End Date");
            document.recieptsLedgerForm.endDate.focus();
        }else{
            document.recieptsLedgerForm.buttonValue.value="summary";
            document.recieptsLedgerForm.submit();
        }
    }
    function postGeneralLedger(){
        if(document.getElementById("invalid")){
            closeInvalid();
        }
        var startDate = document.recieptsLedgerForm.startDate;
        var endDate = document.recieptsLedgerForm.endDate;
        if(trim(startDate.value)==""){
            alert("Please Enter Start Date");
            startDate.focus();
        }else if(trim(endDate.value)==""){
            alert("Please Enter End Date");
            endDate.focus();
        }else{
            if(dwr.util.getValue("period")!=""){
                ArDwr.isAnyArBatchesOpen(startDate.value,endDate.value,function(hasOpenBatches){
                    if(hasOpenBatches){
                        alert("Cannot Post Subledger - one or more Ar Batches are open for this period");
                        return false;
                    }else{
                        postToGl(startDate.value,endDate.value);
                    }
                });
            }
        }
    }

    function postToGl(startDate,endDate){
        var subLedgerType = document.getElementById("subLedgerType").value;
        GeneralLedgerDwr.validateGlAccountForSubledgers(subLedgerType,startDate,endDate,function(result){
            if(result=="canPost"){
                document.recieptsLedgerForm.buttonValue.value="postGL";
                document.recieptsLedgerForm.submit();
            }else{
                if(confirm("Some records are missing GL Accounts - they will not be posted to the GL - Do you want to continue or correct them?")){
                    document.recieptsLedgerForm.buttonValue.value="postGL";
                    document.recieptsLedgerForm.submit();
                }else{
                    showNewPopUp();
                    createHTMLElement("div","invalid","90%","60%",document.body);
                    dwr.util.setValue("invalid", result, {
                        escapeHtml:false
                    });
                    floatDiv("invalid", document.body.offsetLeft+30,document.body.offsetTop+40).floatIt();
                    var transaction = document.getElementsByName("transactionId");
                    for(var i=0;i<transaction.length;i++){
                        var url =rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&tabName=SUBLEDGER&textFieldId=glAccount"+i;
                        AjaxAutocompleter("glAccount"+i, "glAccountChoices"+i,"", "glAccountValid"+i,url,"","");
                    }
                }
            }
        });
    }

    function closeInvalid(){
        document.body.removeChild(document.getElementById("invalid"));
        closeNewPopUp();
    }

    function getRecordsOfNullReportingDate(){
        document.recieptsLedgerForm.buttonValue.value="getRecordsOfNullReportingDate";
        document.recieptsLedgerForm.submit();
    }
    function exportRecordsOfNullReportingDate(){
        document.recieptsLedgerForm.buttonValue.value="exportRecordsOfNullReportingDate";
        document.recieptsLedgerForm.submit();
    }
    function generateReport(){
        if(trim(document.recieptsLedgerForm.startDate.value)==""){
            alert("Please Enter Start Date");
            document.recieptsLedgerForm.startDate.focus();
        }else if(trim(document.recieptsLedgerForm.endDate.value)==""){
            alert("Please Enter End Date");
            document.recieptsLedgerForm.endDate.focus();
        }else{
            document.recieptsLedgerForm.buttonValue.value="generateReport";
            document.recieptsLedgerForm.submit();
        }
    }
    function exportToExcel(){
        if(trim(document.recieptsLedgerForm.startDate.value)==""){
            alert("Please Enter Start Date");
            document.recieptsLedgerForm.startDate.focus();
        }else if(trim(document.recieptsLedgerForm.endDate.value)==""){
            alert("Please Enter End Date");
            document.recieptsLedgerForm.endDate.focus();
        }else{
            document.recieptsLedgerForm.buttonValue.value="exportToExcel";
            document.recieptsLedgerForm.submit();
        }
    }

    function undoGlBatch(){
        if(document.recieptsLedgerForm.undoBatch.value==""){
            alert("Please Enter Batch Number");
            return false;
        }
        document.recieptsLedgerForm.buttonValue.value="undoBatch";
        document.recieptsLedgerForm.submit();
    }
    function showUndoBatchDiv(){
        showPopUp();
        document.getElementById('undoBatchDiv').style.display = 'block';
        document.recieptsLedgerForm.undoBatch.focus();
        floatDiv("undoBatchDiv", document.body.offsetWidth/3,document.body.offsetHeight/3).floatIt();
    }
    function closeUndoBatchDiv(){
        closePopUp();
        document.getElementById('undoBatchDiv').style.display = 'none';
    }
    function saveGlAccount(textId,transId){
        var obj=document.getElementById(textId);
        if(null!=obj && obj.value!=""){
            if(confirm("Gl Account will be updated with "+obj.value)){
                GeneralLedgerDwr.updateGLAccount(obj.value,transId,function(result){
                    if(result){
                        alert("GL account is updated with "+obj.value)
                    }else{
                        alert("GL account update failed.")
                    }
                });
            }
        }else{
            alert("Please Enter Valid Gl Account");
        }
    }
    function saveSubLedgerCode(textId,transId){
        var obj=document.getElementById(textId);
        if(null!=obj && obj.value!=""){
            var result=confirm("SubLedgerCode will be updated with "+obj.value);
            if(result==true){
                DwrUtil.updateSubLedgerCodeForSubLedgers(obj.value,transId,message);
            }else{
                alert("SubLedgerCode is not updated..");
            }
        }else{
            alert("Please Enter Valid SubLedgerCode");
        }
    }
    function message(data){
        alert(data);
    }

    function enableGLAccts(){
        var chargeCode = document.getElementById("chargeCode");
        var filterForAll = document.getElementById("filterForAll");
        if(filterForAll.style.display=="block" && chargeCode.value=="ALL"){
            document.getElementById("glAcctsForAll").style.display="block";
        }else{
            document.getElementById("glAcctsForAll").style.display="none";
        }
    }

    function checkRangeOfGLAccount(){
        if(event.keyCode==9 || event.keyCode==13 || event.button==0){
            var startGLAccount = document.getElementById("startGLAccount");
            var endGLAccount = document.getElementById("endGLAccount");
            if(null!=startGLAccount && startGLAccount.value!=""){
                if(null!=endGLAccount && endGLAccount.value!="" && startGLAccount.value!=endGLAccount.value){
                    var start = startGLAccount.value.split("-");
                    var end = endGLAccount.value.split("-");
                    for(var i=0;i<start.length;i++){
                        if(Number(end[i])>Number(start[i])){
                            return true;
                        }else if(Number(end[i])<Number(start[i])){
                            endGLAccount.value="";
                            alert("Please ensure End GLAccount greater than Start GLAccount");
                            return false;
                        }else if(Number(end[i])==Number(start[i])){
                            if(i+1<start.length){
                                if(Number(end[i+1])<Number(start[i+1])){
                                    endGLAccount.value="";
                                    alert("Please ensure End GLAccount greater than Start GLAccount");
                                    return false;
                                }else if(Number(end[i+1])==Number(start[i+1])){
                                    if(i+2<start.length){
                                        if(Number(end[i+2])<Number(start[i+2])){
                                            endGLAccount.value="";
                                            alert("Please ensure End GLAccount greater than Start GLAccount");
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                endGLAccount.value="";
                alert("Please Enter Start GLAccount");
                return false;
            }
        }
    }

    function changeSortByValue(object){
        if(object.value!=""){
            document.recieptsLedgerForm.sortBy.value=object.value;
        }
    }
    function changeChargeCodeOptions(){
        document.recieptsLedgerForm.buttonValue.value="changeChargeCodeOptions";
        document.recieptsLedgerForm.submit();
    }
    function getStartAndEndDate(){
        if(dwr.util.getValue("period")!=""){
            FiscalPeriodBC.getStartDateAndEndDate(dwr.util.getValue("period"),function(data){
                var json = ( function(){
                    return eval( '(' + data + ')');
                })();
                if(json){
                    dwr.util.setValue("startDate",json.startDate);
                    dwr.util.setValue("endDate",json.endDate);
                }
            });
        }
    }

    function validateStartDate(startDate){
        var startDate = document.recieptsLedgerForm.startDate;
        if(null!=startDate && startDate!=undefined && startDate.value!=""){
            startDate.value = startDate.value.getValidDateTime("/","",false);
            if(startDate.value==""){
                alert("Please Enter valid Date");
                startDate.focus();
            }
        }
    }

    function validateEndDate() {
        var endDate = document.recieptsLedgerForm.endDate;
        if(null!=endDate && endDate!=undefined && endDate.value!=""){
            var startDate = document.recieptsLedgerForm.startDate;
            if(startDate.value==""){
                alert("Please Enter Start Date");
                startDate.focus();
            }else{
                endDate.value = endDate.value.getValidDateTime("/",":",false);
                if(endDate.value==""){
                    alert("Please Enter valid Date");
                    endDate.focus();
                }else{
                    if(Date.parse(startDate.value) >= Date.parse(endDate.value)){
                        alert("End Date should be greater than Start Date");
                        endDate.value="";
                        endDate.focus();
                    }else{
                        var period = document.getElementById("period");
                        if(endDate.value!="" && period.value!=""){
                            DwrUtil.validateEndDateForSubLedger(period.value,endDate.value,function(data){
                                if(null!=data && data!="Available"){
                                    alert(data);
                                    endDate.value="";
                                    endDate.focus();
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    if(document.getElementById("period")){
        initAutocomplete("periodDis", "periodChoices","period", "periodValid", "${path}/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=periodDis","getStartAndEndDate()");
    }
    if(document.getElementById("startGLAccount")){
        initAjaxAutoCompleter("startGLAccount", "startGLAccountDiv", "startGLAccountValid", "${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=startGLAccount","checkRangeOfGLAccount()");
    }
    if(document.getElementById("endGLAccount")){
        initAjaxAutoCompleter("endGLAccount", "endGLAccountDiv", "endGLAccountValid", "${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=endGLAccount","checkRangeOfGLAccount()");
    }
    useLogisoftLodingMessageNew();
    jQuery(document).ready(function(){
        jQuery(document).keypress(function(event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if(keycode == 13) {
                detials();
            }
        });
    });
</script>
<%@include file="../includes/resources.jsp"%>
<c:if test="${!empty reportFileName}">
    <script type="text/javascript">
        window.parent.showGreyBox("SubLedger Report","${path}/servlet/FileViewerServlet?fileName=${reportFileName}");
    </script>
</c:if>
<c:if test="${!empty excelFileName}">
    <script type="text/javascript">
        window.location.replace("${path}/servlet/FileViewerServlet?fileName=${excelFileName}");
    </script>
</c:if>
<script type="text/javascript">
    function changeTableSortingPath(){
        posted = "no"
        if(document.recieptsLedgerForm.posted[0].checked){
            posted = "yes";
        }
        var hrefs = document.getElementsByTagName("a");
        for (var i=0; i < hrefs.length; i++){
            var refs = hrefs[i];
            var xt = getURLParam(refs.href,posted);
            if (xt.length > 0){
                hrefs[i].href = xt;
            }
        }
    }
    function getURLParam(oldHref,posted){
        if (oldHref.indexOf("d-") > -1 ){
            if (oldHref.indexOf("-p") > -1){
                oldHref = oldHref.replace("jsps/AccountsRecievable/RecieptsLedger.jsp","receiptsLedger.do")
                oldHref+="&buttonValue="+document.recieptsLedgerForm.buttonValue.value;
                oldHref+="&posted="+posted;
                oldHref+="&startDate="+document.recieptsLedgerForm.startDate.value;
                oldHref+="&endDate="+document.recieptsLedgerForm.endDate.value;
                oldHref+="&subLedgerType="+document.recieptsLedgerForm.subLedgerType.value;
                oldHref+="&period="+document.recieptsLedgerForm.period.value;
            }
        }
        return oldHref;
    }
    function showOrHidePost(){
        if(document.recieptsLedgerForm.posted[0].checked){
            document.getElementById("postButton").disabled=true;
        }else{
            document.getElementById("postButton").disabled=false;
        }
    }
    showFilters();
    changeTableSortingPath();
    showOrHidePost();
    function showNewPopUp() {
        var cover = document.getElementById("newcover");
        cover.style.display = "block";
        cover.style.width = document.body.scrollWidth;
        cover.style.height = document.body.scrollHeight;
        cover.style.opacity = 0.5;
        cover.style.filter = "alpha(opacity="+50+")";
    }
    function closeNewPopUp() {
        var cover = document.getElementById("newcover");
        cover.style.display = "none";
    }

</script>
</html:html>

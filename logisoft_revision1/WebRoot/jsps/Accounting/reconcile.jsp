<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="../includes/jspVariables.jsp" %>
<%@ include file="../includes/resources.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Reconcile</title>
        <%@include file="../includes/baseResources.jsp"%>
    </head>
    <script type="text/javascript" src="${path}/dwr/engine.js"></script>
    <script type="text/javascript" src="${path}/dwr/util.js"></script>
    <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
    <script type="text/javascript" src="${path}/dwr/interface/UploadDownloaderDWR.js"></script>
    <script type="text/javascript" src="${path}/dwr/interface/APReconcilationBC.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
    <script type="text/javascript" src="${path}/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>

    <body class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif"/>
            </div>
        </div>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form  action="/reconcile"  method="post" enctype="multipart/form-data" name="reconcileForm" type="com.gp.cvst.logisoft.struts.form.ReconcileForm" scope="request">
            <html:hidden  property="action"/>
            <html:hidden  property="transactionIds"/>
            <html:hidden  property="selectedIds"/>
            <html:hidden  property="unSelectedIds"/>
            <html:hidden  property="clearedDates"/>
            <table width="100%" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <c:if test="${displayMessage!=null}">
                    <tr style="color:blue">
                        <td colspan="4"><c:out value="${displayMessage}"/></td>
                    </tr>
                </c:if>
                <c:if test="${errorMessage!=null}">
                    <tr style="color:red">
                        <td colspan="4"><c:out value="${errorMessage}"/></td>
                    </tr>
                </c:if>
                <c:if test="${message!=null}">
                    <tr class="error">
                        <td colspan="4"><c:out value="${message}"/></td>
                    </tr>
                </c:if>
                <tr class="tableHeadingNew">
                    <td colspan="4">Search Account Details</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Bank GL Account</td>
                    <td>
                        <html:text property="glAccountNumber" styleId="glAccountNumber" size="20" styleClass="textlabelsBoldForTextBox"/>
                        <html:hidden property="bankAccount" styleId="bankAccount"/>
                        <input name="glAccountValid" id="glAccountValid"  type="hidden" value="${reconcileForm.glAccountNumber}"/>
                        <div id="glAccountDiv" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("glAccountNumber","glAccountDiv","bankAccount","glAccountValid","${path}/servlet/AutoCompleterServlet?action=Bank&textFieldId=glAccountNumber&tabName=Reconcile","");
                        </script>
                    </td>
                    <td>Bank Reconcile Date</td>
                    <td>
                        <div class="float-left">
                            <html:text property="bankReconcileDate"
                                       styleId="txtCalendar" size="17" styleClass="textlabelsBoldForTextBox" onchange="checkLastReconciled()"/>
                        </div>
                        <div class="float-left" style="margin:0 5px;">
                            <img src="${path}/img/CalendarIco.gif" alt="cal" id="Calendar" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        </div>
                        <div class="float-left">
                            <input type="button" class="buttonStyleNew" value="Search" onclick="search()"/>
                        </div>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Bank Statement Balance</td>
                    <td>
                        <html:text property="bankStatementBalance" maxlength="30" onchange="changeAmounts()" styleClass="textlabelsBoldForTextBox"/>
                    </td>
                    <td colspan="2"></td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="4">Account Details</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Balance of Account from GL</td>
                    <td>
                        <html:text property="glAccountBalance" readonly="true" size="20" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </td>
                    <td>Last Reconciled Date</td>
                    <td>
                        <html:text property="lastReconciledDate" styleId="lastReconciledDate" readonly="true" size="20" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Checks Open</td>
                    <td>
                        <html:text property="openChecks" styleId="openChecks" size="20" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                        <input type="hidden" id="tempopenChecks" value="${reconcileForm.openChecks}"/>
                    </td>
                    <td>Deposits Open</td>
                    <td>
                        <html:text property="openDeposits" styleId="openDeposits" readonly="true" size="20" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                        <input type="hidden" id="tempopenDeposits" value="${reconcileForm.openDeposits}"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Difference</td>
                    <td>
                        <html:text property="difference" styleId="difference" size="20" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </td>
                    <td>Show Status:</td>
                    <td>
                        <html:radio property="cleared" value="${commonConstants.YES}"/>Cleared
                        <html:radio property="cleared" value="${commonConstants.NO}"/>Not Cleared
                        <html:radio property="cleared" value="${commonConstants.ALL}"/>All
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td align="center" colspan="4">
                        <input type="button" class="buttonStyleNew" value="Refresh" onclick="refreshDetails();"/>
                        <input type="button" class="buttonStyleNew" value="Reconcile" onclick="reconcile();"/>
                        <input type="button" class="buttonStyleNew" value="Save" onclick="save();"/>
                        <input type="button" class="buttonStyleNew" value="Notes" onclick="showNotes('${notesConstants.RECONCILATION}')"/>
                        CSV File<html:file property="bankCSVFile" styleClass="textlabelsBoldForTextBox"/>
                        <input type="button" class="buttonStyleNew" style="width:100px" value="Upload CSV" onclick="uploadCSVFile()"/>
                    </td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="4">Reconcile Details</td>
                </tr>
                <tr>
                    <td colspan="4">
                        <c:set var="i" value="0"></c:set>
                        <div id="reconcileListDiv" class="scrolldisplaytable">
                            <display:table name="${reconcileForm.reconcileTransactions}" id="reconcileTransaction"
                                           sort="list" defaultsort="1" defaultorder="descending"
                                           pagesize="4000" class="displaytagstyleNew" style="width:100%">
                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner">{0} transactions displayed,
					For more transactions click on page numbers.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                    <span class="pagebanner"> One {0} displayed. page number</span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.all_items_found">
                                    <span class="pagebanner"> {0} {1} displayed, page number</span>
                                </display:setProperty>
                                <display:setProperty name="basic.msg.empty_list">
                                    <span class="pagebanner"> No transactions found.</span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.placement" value="bottom"/>
                                <display:setProperty name="paging.banner.item_name" value="transaction"/>
                                <display:setProperty name="paging.banner.items_name" value="transactions"/>
                                <display:column title="Transaction<br>Type" property="paymentMethod" sortable="true"/>
                                <display:column title="Refrence<br>Number" property="customerReference" sortable="true"/>
                                <display:column title="Batch<br> No" property="batchId" sortable="true"/>
                                <display:column title="Date" property="transDate" sortable="true"/>
                                <display:column title="Debits" style="text-align:right;" sortable="true">
                                    <c:if test="${not empty reconcileTransaction.debit && reconcileTransaction.debit!='0.00'}">
                                        <c:out value="${reconcileTransaction.debit}"/>
                                    </c:if>
                                </display:column>
                                <display:column title="Credits" style="text-align:right;" sortable="true">
                                    <c:if test="${not empty reconcileTransaction.credit && reconcileTransaction.credit!='0.00'}">
                                        <c:out value="${reconcileTransaction.credit}"/>
                                    </c:if>
                                </display:column>
                                <display:column title="Cleared">
                                    <c:choose>
                                        <c:when test="${reconcileTransaction.status==commonConstants.STATUS_RECONCILE_IN_PROGRESS}">
                                            <input type="checkbox" id="clear" name="clear" checked="checked" onclick ="clearedCheck(this)" value="${reconcileTransaction.transactionId}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" id="clear" name="clear"  onclick ="clearedCheck(this)" value="${reconcileTransaction.transactionId}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </display:column>
                                <display:column title="Cleared Date">
                                    <input type="text" name="clearedDate" id="txtcal${i}" size="8" value="${reconcileForm.bankReconcileDate}" class="textlabelsBoldForTextBox">
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal${i}" onmousedown="insertDateFromCalendar(this.id, 0);"/>
                                </display:column>
                                <display:column style="display:none">
                                    <input type="hidden" id="credit" name="credit" value="${reconcileTransaction.credit}"/>
                                    <input type="hidden" id="debit" name="debit" value="${reconcileTransaction.debit}"/>
                                </display:column>
                                <c:set var="i" value="${i+1}"/>
                            </display:table>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>

    <script language="javascript" type="text/javascript">
        dwr.engine.setTextHtmlHandler(dwrSessionError);
        function refreshDetails(){
            document.reconcileForm.action.value="refresh";
            document.reconcileForm.submit();
        }
        function search(){
            if(document.reconcileForm.glAccountNumber.value==""){
                alert("Please Enter GL Account");
                document.reconcileForm.glAccountNumber.focus();
                return;
            }
            if(document.reconcileForm.bankReconcileDate.value==""){
                alert("Please Select Reconcile Date");
                document.reconcileForm.bankReconcileDate.focus();
                return;
            }
            document.reconcileForm.action.value="search";
            document.reconcileForm.submit();
        }
        function uploadCSVFile(){
            if(trim(document.reconcileForm.bankCSVFile.value)==""){
                alert("Please Include CSV File to Upload");
                return;
            }
            document.reconcileForm.action.value="uploadCSVFile";
            document.reconcileForm.submit();
        }
        function clearedCheck(obj){
            var rowIndex=obj.parentNode.parentNode.rowIndex;
            var creditArray = document.getElementsByName("credit");
            var debitArray = document.getElementsByName("debit");
            var bankStatementBalance = dwr.util.getValue("bankStatementBalance").replace(/,/g,'');
            var glAccountBalance = dwr.util.getValue("glAccountBalance").replace(/,/g,'');
            var openChecks = dwr.util.getValue("openChecks").replace(/,/g,'');
            var openDeposits = dwr.util.getValue("openDeposits").replace(/,/g,'');
            var credit = creditArray[rowIndex-1].value;
            var debit =  debitArray[rowIndex-1].value;
            credit = credit.replace(/,/g,'');
            debit = debit.replace(/,/g,'');
            if(obj.checked){
                if(trim(credit)!=""){
                    openChecks=Number(Number(openChecks)-Number(credit)).toFixed(2);
                }
                if(trim(debit)!=""){
                    openDeposits=Number(Number(openDeposits)-Number(debit)).toFixed(2);
                }
            }else{
                if(trim(credit)!=""){
                    openChecks=Number(Number(openChecks)+Number(credit)).toFixed(2);
                }
                if(trim(debit)!=""){
                    openDeposits=Number(Number(openDeposits)+Number(debit)).toFixed(2);
                }
            }
            var difference=Number(bankStatementBalance)-Number(glAccountBalance)-Number(openChecks)+Number(openDeposits);
            dwr.util.setValue("difference",addCommas(Number(difference).toFixed(2)));
            dwr.util.setValue("openChecks",addCommas(Number(openChecks).toFixed(2)));
            dwr.util.setValue("openDeposits",addCommas(Number(openDeposits).toFixed(2)));
        }
        function addCommas(str){
            str += '';
            x = str.split('.');
            x1 = x[0];
            x2 = x.length > 1 ? '.' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }

        function changeAmounts() {
            var bankStatementBalance = dwr.util.getValue("bankStatementBalance").replace(/,/g,'');
            var glAccountBalance = dwr.util.getValue("glAccountBalance").replace(/,/g,'');
            var openChecks = dwr.util.getValue("openChecks").replace(/,/g,'');
            var openDeposits = dwr.util.getValue("openDeposits").replace(/,/g,'');
            if(trim(glAccountBalance)!="" && trim(openChecks)!="" && trim(openDeposits)!=""){
                var difference = Number(bankStatementBalance)-Number(openChecks)+Number(openDeposits)-Number(glAccountBalance);
                dwr.util.setValue("difference",addCommas(Number(difference).toFixed(2)));
            }
            dwr.util.setValue("bankStatementBalance",addCommas(Number(bankStatementBalance).toFixed(2)));
        }

        function save(){
            if(document.getElementById("reconcileTransaction")){
                var selectedIds = "";
                var unSelectedIds = "";
                var clear = document.getElementsByName("clear");
                for(var i=0;i<clear.length;i++){
                    if(clear[i].checked){
                        selectedIds+=","+clear[i].value;
                    }else{
                        unSelectedIds+=","+clear[i].value;
                    }
                }
                document.reconcileForm.selectedIds.value=selectedIds;
                document.reconcileForm.unSelectedIds.value=unSelectedIds;
                document.reconcileForm.action.value="save";
                document.reconcileForm.submit();
            }else{
                alert("No Transactions to save");
                return;
            }
        }

        function reconcile() {
            var difference = dwr.util.getValue("difference").replace(/,/g,'');
            if(Number(difference)!=Number(0)){
                alert("Please make sure difference should be zero");
                return;
            }
            if(document.reconcileForm.glAccountNumber.value==""){
                alert("Please Enter GL Account");
                document.reconcileForm.glAccountNumber.focus();
                return;
            }
            if(document.reconcileForm.bankReconcileDate.value==""){
                alert("Please Select Reconcile Date");
                document.reconcileForm.bankReconcileDate.focus();
                return;
            }
            var clear = document.getElementsByName("clear");
            var clearedDate = document.getElementsByName("clearedDate");
            var transactionIds = "";
            var clearedDates = "";
            for(var i=0;i<clear.length;i++){
                if(clear[i].checked){
                    if(i==0){
                        transactionIds=clear[i].value;
                        clearedDates = clearedDate[i].value;
                    }else{
                        transactionIds=transactionIds+":-"+clear[i].value;
                        clearedDates = clearedDates+":-"+clearedDate[i].value;
                    }
                }
            }
            document.reconcileForm.transactionIds.value=transactionIds;
            document.reconcileForm.clearedDates.value=clearedDates;
            document.reconcileForm.action.value="reconcile";
            document.reconcileForm.submit();
        }

        function showNotes(moduleId){
            glAccountNumber = document.reconcileForm.glAccountNumber;
            if(glAccountNumber.value==""){
                alert("Please Enter GL Account");
                glAccountNumber.focus();
                return;
            }
            GB_show('Notes', rootPath+'/notes.do?moduleId='+moduleId+'&moduleRefId='+glAccountNumber.value,375,700);
        }

        function checkLastReconciled(){
            var newReconciledDate = new Date(document.getElementById("txtCalendar").value).getTime();
            var lastReconciledDate = new Date(document.getElementById("lastReconciledDate").value).getTime();
            if(!isNaN(lastReconciledDate) && !isNaN(newReconciledDate) && lastReconciledDate>newReconciledDate){
                alert("The date cannot be prior to last reconciled date");
                document.getElementById("txtCalendar").value="";
                document.getElementById("txtCalendar").focus();
            }
        }

        useLogisoftLodingMessageNew();
    </script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery-latest.js" ></script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery.tablesorter.js" ></script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery.tablesorter.pager.js"></script>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery("#reconcileTransaction").tablesorter({widgets:['zebra'],headers:{4:{sorter:'commaDigit'},5:{sorter:'commaDigit'}}});
    </script>
    <c:if test="${reconcileForm.action=='reconcile'}">
        <script type="text/javascript">
            document.reconcileForm.action.value="downloadFile";
            document.reconcileForm.submit();
        </script>
    </c:if>
</html>


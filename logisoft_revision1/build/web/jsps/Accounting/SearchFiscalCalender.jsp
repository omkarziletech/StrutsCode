<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.text.SimpleDateFormat,java.util.*"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.hibernate.dao.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>Search Fiscal Calender</title>
        <%
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        if(path==null){
        path="../..";
        }
        String buttonvalue=(String)request.getAttribute("buttonValue");
        if(buttonvalue!=null && buttonvalue.equals("completed")){
        %>
        <script>
            self.close();
            opener.location.href = "<%=path%>/jsps/Accounting/changingFiscal.jsp";
        </script>
        <style>
            #curtain {
                position: fixed;
                _position: absolute;
                z-index: 99;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                _height: expression(document.body.offsetHeight + "px");
                background: url(curtain.png);
                _background: url(curtain.gif);
            }

        </style>
        <%}
        Date year=new Date();
        int years=year.getYear();
        List monthLists=null;
        List forcondition3=null;
        List forcondition2=null;
        String year111="";
        String fiscalPerid1="";
        String month1="";
        int y=2007;
         String active1="";
        DBUtil dbUtil=new DBUtil();
        request.setAttribute("budgetset",dbUtil.getbudgetsetForFiscalPeriod());
        request.setAttribute("monthLists",dbUtil.getmonthList1());
        monthLists=(List)request.getAttribute("monthLists");
        request.setAttribute("findyerlist",dbUtil.getFiscalYearForStatus(null));
        String active="";
        FiscalYear fiscalYear = null;
        if(session.getAttribute("fiscalYear")!=null){
                fiscalYear = (FiscalYear)session.getAttribute("fiscalYear");
                if(fiscalYear.getActive()!=null){
                active=fiscalYear.getActive();
                }
        }
        if((List)session.getAttribute("abc")!=null){
          forcondition3=(List)session.getAttribute("abc");
        }
        if((List)session.getAttribute("forcondition1")!=null){
          forcondition2=(List)session.getAttribute("forcondition1");
        }
        if((String)session.getAttribute("year")!=null){
          year111=(String)session.getAttribute("year");
        }
        if((String)session.getAttribute("fiscalPerid")!=null){
          fiscalPerid1=(String)session.getAttribute("fiscalPerid");
        }
        if((String)session.getAttribute("month")!=null){
          month1=(String)session.getAttribute("month");
        }
        String status2="";
        if(session.getAttribute("status1")!=null){
        status2=(String)session.getAttribute("status1");
        }
        List saveList2=null;
        if(session.getAttribute("saveList")!=null){
        saveList2=(List)session.getAttribute("saveList");
        }
        List searchlist=null;
        if(session.getAttribute("searchlist")!=null){
        searchlist=(List)session.getAttribute("searchlist");
        }
        String message="";
        if(request.getAttribute("message")!=null){
         message=(String)request.getAttribute("message");
        }
        String message1="";
        if(request.getAttribute("msg1")!=null){
         message1=(String)request.getAttribute("msg1");
        }
        String msg="";
        if(request.getAttribute("msg")!=null){
        msg=(String)request.getAttribute("msg");
        }
        String feedbackMessage="";
        if(request.getAttribute("feedbackMessage")!=null){
        feedbackMessage=(String)request.getAttribute("feedbackMessage");
        }
        if(request.getAttribute("active1")!=null) {
         active1=(String)request.getAttribute("active1");
        }
        %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
        <script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
        <script type='text/javascript' src='<%=path%>/dwr/interface/FiscalPeriodBC.js'></script>
        <script type='text/javascript' src='<%=path%>/dwr/interface/UploadDownloaderDWR.js'></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type='text/javascript' src="<%=path%>/js/common.js"></script>

        <script type="text/javascript">
            dwr.engine.setTextHtmlHandler(dwrSessionError);
            function searchyear() {
                if (document.fiscalPeriodForm.year.value == "00") {
                    alert("Please Select Year");
                    return false;
                }
                //alert("text"+document.fiscalPeriodForm.year[document.fiscalPeriodForm.year.selectedIndex].text);
                document.fiscalPeriodForm.buttonValue.value = "searchyear";
                document.fiscalPeriodForm.submit();
            }
            function openstatus(val) {
                var value = val;
                //var x=document.getElementById('divtablesty1').rows[++val].cells;
                document.fiscalPeriodForm.index.value = value;
                document.fiscalPeriodForm.buttonValue.value = "openstatus";
                document.fiscalPeriodForm.submit();
            }
            function save() {
                document.fiscalPeriodForm.buttonValue.value = "save";
                document.fiscalPeriodForm.submit();
            }
            function editfiscal(val, windowname) {
                alert("Edit Only Date ");
                var valexact = val;
                //var x=document.getElementById('divtablesty1').rows[++val].cells;
                if (!window.focus)
                    return true;
                var href;
                var mylink = "<%=path%>/jsps/Accounting/EditSearchFiscalCalender.jsp?val=" + valexact;
                if (typeof (mylink) == 'string')
                    href = mylink;
                else
                    href = mylink.href;
                window.open(href, windowname, 'width=600,height=200,scrollbars=yes');
                return false;
                document.fiscalPeriodForm.submit();
            }
            function addnew() {
                document.fiscalPeriodForm.buttonValue.value = "addnew";
                document.fiscalPeriodForm.submit();
            }

            function incomeStatementReport(val) {
                var periodToValue = ++val;
                //document.fiscalPeriodForm.toPeriod.value = ++val;
                // document.fiscalPeriodForm.buttonValue.value = "IncomeStatementReport";
                // document.fiscalPeriodForm.submit();
                window.location.href = "<%=path%>/jsps/Accounting/IncomeStatementReport.jsp?peridTo=" + periodToValue + "&year=" + document.fiscalPeriodForm.year.value;
            }

            function closeYear(val) {
                FiscalPeriodBC.checkSubledgersPendingForYearClosing(document.getElementById('year').value, function(data) {
                    if (data) {
                        alert("Some Subledgers are still open for this year - " + document.getElementById('year').value);
                    } else {
                        var datatableobj = document.getElementById("fiscalyear");
                        for (i = 1; i < datatableobj.rows.length; i++) {
                            var tablerowobj = datatableobj.rows[i];

                            if (tablerowobj.cells[3].innerHTML == 'Close') {
                                val = 'Close';
                            } else if ((tablerowobj.cells[0].innerHTML == 'AD' || tablerowobj.cells[0].innerHTML == 'CL') && tablerowobj.cells[3].innerHTML == 'Open') {
                                val = 'Close';
                            } else {
                                val = 'Open';
                                break;
                            }
                        }
                        if (val == 'Close') {
                            if (document.fiscalPeriodForm.year.value == "00") {
                                alert("Please Select Year");
                                return false;
                            }
                            FiscalPeriodBC.getFiscalYearStatus(document.getElementById('year').value, function(status) {
                                if (status != "Close") {
                                    var answer = confirm('Do you want to close the year ?');
                                    if (!answer) {
                                        return false;
                                    }
                                    FiscalPeriodBC.completeYearClose(document.getElementById('year').value, function(batch) {
                                        showPopUp();
                                        var curtain = document.body.appendChild(document.createElement('div'));
                                        var mainDiv = curtain.appendChild(document.createElement('div'));
                                        curtain.appendChild(document.createElement('br'));
                                        var buttonDiv = curtain.appendChild(document.createElement('div'));
                                        mainDiv.setAttribute("align", "center");
                                        mainDiv.style.color = "red";
                                        buttonDiv.setAttribute("align", "center");
                                        curtain.id = "curtain";
                                        curtain.style.position = "absolute";
                                        curtain.style.zIndex = "1000";
                                        curtain.style.left = "40%";
                                        curtain.style.top = "30%";
                                        curtain.style.right = "60%";
                                        curtain.style.bottom = "70%";
                                        curtain.style.width = "250px";
                                        curtain.style.height = "160px";
                                        curtain.style.background = "white";
                                        curtain.style.border = "solid";
                                        var okButton = document.createElement('input');
                                        okButton.setAttribute("type", "button");
                                        okButton.setAttribute("value", "OK");
                                        var cancelButton = document.createElement('input');
                                        cancelButton.setAttribute("type", "button");
                                        cancelButton.setAttribute("value", "Cancel");
                                        cancelButton.setAttribute("style", "float:right");
                                        cancelButton.onclick = new Function("removeDiv(" + document.getElementById('year').value + ");");
                                        buttonDiv.appendChild(okButton);
                                        okButton.onclick = new Function("postBatch(" + batch.batchId + ");");
                                        buttonDiv.appendChild(cancelButton);
                                        var batchIdSpan = document.createElement('span');
                                        var batchValueSpan = document.createElement('span');
                                        batchValueSpan.setAttribute("style", "float:right");
                                        batchIdSpan.setAttribute("style", "align:right");
                                        var creditAmtSpan = document.createElement('span');
                                        var creditAmtValueSpan = document.createElement('span');
                                        creditAmtValueSpan.setAttribute("style", "float:right");
                                        creditAmtSpan.setAttribute("style", "align:right");
                                        var debitAmtSpan = document.createElement('span');
                                        var debitAmtValueSpan = document.createElement('span');
                                        debitAmtValueSpan.setAttribute("style", "float:right");
                                        debitAmtSpan.setAttribute("style", "align:right");
                                        var contentSpan = document.createElement('span');
                                        mainDiv.appendChild(batchIdSpan);
                                        mainDiv.appendChild(batchValueSpan);
                                        mainDiv.appendChild(document.createElement('br'));
                                        mainDiv.appendChild(creditAmtSpan);
                                        mainDiv.appendChild(creditAmtValueSpan);
                                        mainDiv.appendChild(document.createElement('br'));
                                        mainDiv.appendChild(debitAmtSpan);
                                        mainDiv.appendChild(debitAmtValueSpan);
                                        mainDiv.appendChild(document.createElement('br'));
                                        mainDiv.appendChild(document.createElement('br'));
                                        mainDiv.appendChild(contentSpan);
                                        batchIdSpan.innerHTML = "BatchId : ";
                                        creditAmtSpan.innerHTML = "Credit Amount : ";
                                        debitAmtSpan.innerHTML = "Debit Amount : ";
                                        batchValueSpan.innerHTML = batch.batchId;
                                        creditAmtValueSpan.innerHTML = batch.totalCredit;
                                        debitAmtValueSpan.innerHTML = batch.totalDebit;
                                        contentSpan.innerHTML = "Do you Want to Post this Batch?";

                                    });
                                } else {
                                    document.getElementById('feedbackMessage').innerHTML = "";
                                    document.getElementById('message').innerHTML = "Year " + document.getElementById('year').value + " already closed";
                                }
                            });
                        } else if (val == 'Open') {
                            alert("Year cannot be closed until all the periods are closed.");
                            return false;
                        }
                    }
                });
            }
            function closeyear1(val) {
                document.fiscalPeriodForm.index1.value = "val";
                document.fiscalPeriodForm.buttonValue.value = "closeyear";
                document.fiscalPeriodForm.submit();
            }
            function closestatus(val) {
                var value = val;
                // var x=document.getElementById('divtablesty1').rows[++value].cells;
                document.fiscalPeriodForm.index.value = value;
                document.fiscalPeriodForm.buttonValue.value = "closestatus";
                document.fiscalPeriodForm.submit();
            }
            function print(val) {
                return GB_show('Account Range', '<%=path%>/jsps/Accounting/AccountRange.jsp?value=' + val, 200, 600);
            }
            function print1(val) {
                var value = val;
                document.fiscalPeriodForm.index.value = value;
                document.fiscalPeriodForm.buttonValue.value = "fiscalperiodReport";
                document.fiscalPeriodForm.submit();
            }
            function ablefiscalset1() {
                toggleTable('bs1', 1);
                toggleTable('cbs1', 1);
                //toggleTable('div2',1);
                //toggleTable('divstyle3',1);
            }
            function load() {
                useLogisoftLodingMessage();
                //document.getElementById('bs1').style.visibility='hidden';
                //document.getElementById('cbs1').style.visibility='hidden';
            }
            function setBudget() {
                if (document.fiscalPeriodForm.year.value == "00") {
                    alert("Please Select Year");
                    return false;
                }
                if (document.fiscalPeriodForm.copybudgetset.value == 0) {
                    alert("Please select Budget Set");
                    return false;
                }
                var val1 = document.fiscalPeriodForm.year.value;
                var val2 = document.fiscalPeriodForm.copybudgetset.value;
                if (val2 != 0) {
                    alert("Setting Budget " + val2 + " for " + val1);
                    document.fiscalPeriodForm.buttonValue.value = "setBudget";
                    document.fiscalPeriodForm.submit();
                }
            }
            function importExcel() {
                document.fiscalPeriodForm.myFile.style.display = 'block';
                document.fiscalPeriodForm.submitValue.style.display = 'block';
                document.fiscalPeriodForm.cancelButton.style.display = 'block';
                document.fiscalPeriodForm.importFromExcel.style.display = 'none';

            }
            function hideImport() {
                document.fiscalPeriodForm.myFile.style.display = 'none';
                document.fiscalPeriodForm.submitValue.style.display = 'none';
                document.fiscalPeriodForm.cancelButton.style.display = 'none';
                document.fiscalPeriodForm.importFromExcel.style.display = 'block';

            }
            function printTrialBalance(val, startingAccount, endingAccount, ecuReport) {
                document.fiscalPeriodForm.index.value = val;
                document.fiscalPeriodForm.startingAccount.value = startingAccount;
                document.fiscalPeriodForm.endingAccount.value = endingAccount;
                document.fiscalPeriodForm.ecuReport.value = ecuReport;
                document.fiscalPeriodForm.buttonValue.value = "printTrialBalance";
                document.fiscalPeriodForm.submit();
            }
            function exportTrialBalance(val, startingAccount, endingAccount, ecuReport) {
                document.fiscalPeriodForm.index.value = val;
                document.fiscalPeriodForm.startingAccount.value = startingAccount;
                document.fiscalPeriodForm.endingAccount.value = endingAccount;
                document.fiscalPeriodForm.ecuReport.value = ecuReport;
                document.fiscalPeriodForm.buttonValue.value = "exportTrialBalance";
                document.fiscalPeriodForm.submit();
            }
            function exportExcel() {
                if (document.fiscalPeriodForm.copybudgetset.value == 0) {
                    alert("Please select Budget Set");
                    return;
                }
                document.fiscalPeriodForm.buttonValue.value = "getExcelReport";
                document.fiscalPeriodForm.submit();
            }

            function unlock(id) {
                FiscalPeriodBC.unlockFiscalPeriod(id, function(data) {
                    document.getElementById('message').innerHTML = "<%=message%>";
                    document.getElementById('feedbackMessage').innerHTML = "Fiscal Period is 'UnLocked' Sucessfully";
                });
            }

            function postBatch(batchId) {
                var year = document.getElementById('year').value;
                document.body.removeChild(document.getElementById('curtain'));
                closePopUp();
                FiscalPeriodBC.postBatchAndCloseYear(batchId, year, function(message) {
                    document.getElementById('message').innerHTML = message;
                });
            }

            function removeDiv(year) {
                document.body.removeChild(document.getElementById('curtain'));
                closePopUp();
                return false;
            }
            jQuery(document).ready(function() {
                jQuery(document).keypress(function(event) {
                    var keycode = (event.keyCode ? event.keyCode : event.which);
                    if (keycode == 13) {
                        searchyear();
                    }
                });
            });
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body onload="load()" >
        <div id="cover"></div>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form action="/fiscalperiod" name="fiscalPeriodForm"  enctype="multipart/form-data" type="com.gp.cvst.logisoft.struts.form.FiscalPeriodForm" scope="request" >
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorder" style="padding-left:10px">
                <tr class="tableHeading" >Search Fiscal Calender</tr>
                <tr><td colspan="2" align="center"><font color=red><span id="message"><%=message%></span></font><font color=green><span id="feedbackMessage"><%=feedbackMessage%></span></font></td></tr>
                <tr><td colspan="2" align="center"><font color=red><%=message1%></font></td></tr>
                <tr><td colspan="2" align="center"><font color=red><%=msg%></font></td></tr>

                <tr><td><table border="0">
                            <tr>
                                <td width="15%" class="textlabels"><b>Fiscal Year</b></td>
                                <td width="39%">
                                    <html:select property="year" styleId="year" styleClass="dropdown_accounting" value="<%=year111%>" onchange="searchyear()">
                                        <html:optionsCollection name="findyerlist" styleClass="unfixedtextfiledstyle" />
                                    </html:select>
                                </td>
                                <td><input type="button" name="search" onclick="print1()" value="Print" class="buttonStyleNew"/> </td>
                            </tr></table></td>
                </tr>
                <tr>
                    <td valign="top" colspan="4" align="left" style="padding-top:10px;">
                        <table>
                            <tr>
                                <td><input type="button" name="search" onclick="searchyear()" value="Search" class="buttonStyleNew"/></td>
                                <td><input type="button" name="search" onclick="addnew()" value="Add New" class="buttonStyleNew"/></td>
                                <td><input type="button" name="search" onclick="closeYear('<%=active%>')" value="Close Year" class="buttonStyleNew"/>   </td>
                                <td><input type="button" name="search" onclick="setBudget()"  value="Create Budget" class="buttonStyleNew"/></td>
                                <%--onclick="ablefiscalset1()" --%>
                                <td id="bs1" align="left" class="textlabels">Budget Set&nbsp;</td>
                                <td id="cbs1">
                                    <html:select property="copybudgetset"  styleClass="dropdown_accounting">
                                        <html:optionsCollection  name="budgetset" /></html:select>
                                    </td>
                                </tr>
                                <tr><td>
                                        <input type="button" name="cancelButton" style="display:none;" value="Cancel" onclick="hideImport()" class="buttonStyleNew"/>
                                        <input type="button" name="importFromExcel" value="Import" onclick="importExcel()" class="buttonStyleNew"/>
                                        <input type="button" name="exporttoexcel" style="width:90px" value="ExportToExcel" onclick="exportExcel()" class="buttonStyleNew"/>
                                    </td>
                                </tr>
                                <tr><td colspan="5"><html:file property="myFile" style="display:none;" value="Import" />
                                    <html:submit property="submitValue" style="display:none;" styleClass="buttonStyleNew">Upload</html:submit>
                                    </td>
                                </tr>
                            </table></td></tr>

                <%-- <input type="button" name="incomeStatement" onclick="incomeStatementReport()" value="Income Statement Report" class="buttonStyle" style="width:150px"/>
                --%>
            </table>
            <br/><br/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorder">
                <tr class="tableHeading">List Of Values</tr>
                <tr>
                    <td>
                        <%
                        if(searchlist!=null && searchlist.size()>0){
                          int i=0;
                        %>
                        <div id="divtablesty1" class="scrolldisplaytable" style="border: thin; overflow: scroll; width: 100%; height: 320px;">
                            <display:table name="<%=searchlist%>"  id="fiscalyear" pagesize="<%=pageSize%>" class="displaytagstyleNew" sort="list">
                                <%
                               SimpleDateFormat sid=new SimpleDateFormat("MM/dd/yyyy");
                               Date d=new Date();
                               Date e=new Date();
                               String d1="";
                               String e1="";
                               FiscalPeriod fis=new FiscalPeriod();
                               fis=(FiscalPeriod)searchlist.get(i);
                               d=(Date)fis.getStartDate();
                               e=(Date)fis.getEndDate();
                               d1=(String)sid.format(d);
                               e1=(String)sid.format(e);
                                %>
                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner">
                                        <font color="blue">{0}</font> Fiscal Period details displayed,For more code click on page numbers.
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
                                <display:setProperty name="paging.banner.item_name" value="FiscalPeriod"/>
                                <display:setProperty name="paging.banner.items_name" value="FiscalPeriod Details"/>
                                <display:column   paramId="paramid"  property="period">  </display:column>
                                <display:column    title="startDate"> <c:out value="<%=d1 %>" /> </display:column>
                                <display:column    title="endDate"><c:out value="<%=e1%>"/></display:column>
                                <display:column property="status"></display:column>
                                <display:column title="Locked">
                                    <c:if test="${fiscalyear.locked == 'Y'}">
                                        YES
                                    </c:if>
                                    <c:if test="${fiscalyear.locked == 'N'}">
                                        NO
                                    </c:if>
                                </display:column>
                                <display:column title="Actions">
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();" >
                                        <img src="<%=path%>/img/icons/edit.gif" border="0" alt="" onclick="editfiscal('<%=i%>', 'windows')"/> </span>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Open</strong>', null, event);" onmouseout="tooltip.hide();" >
                                        <img src="<%=path%>/img/icons/generate_code.gif" border="0" alt="" onclick="openstatus('<%=i %>')"/> </span>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Close</strong>', null, event);" onmouseout="tooltip.hide();" >
                                        <img src="<%=path%>/img/icons/close.gif" border="0" alt="" onclick="closestatus('<%=i%>')"/> </span>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Trial Balance Report</strong>', null, event);" onmouseout="tooltip.hide();" >
                                        <img src="<%=path%>/img/icons/trial_balance.gif" border="0" alt="" onclick="print('<%=i%>')"/> </span>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Income Statement Report</strong>', null, event);" onmouseout="tooltip.hide();" >
                                        <img src="<%=path%>/img/icons/currency.gif" border="0" alt="" onclick="incomeStatementReport('<%=i%>')"/>
                                    </span>
                                    <c:if test="${fiscalyear.period == 'CL'}">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>UnLock</strong>', null, event);" onmouseout="tooltip.hide();" >
                                            <img style="border: red;border: 1" src="<%=path%>/img/icons/lock.gif" border="0" alt="" onclick="unlock('${fiscalyear.id}')"/> </span>
                                        </c:if>
                                        <c:if test="${fiscalyear.period != 'CL'}">
                                        <span disabled="true" class="hotspot" onmouseover="tooltip.show('<strong>UnLock</strong>', null, event);" onmouseout="tooltip.hide();" >
                                            <img disabled="true" src="<%=path%>/img/icons/lock.gif" border="0" alt=""/> </span>
                                        </c:if>
                                    <img alt="" onmouseover="tooltip.show('<strong>Notes</strong>', null, event);" onmouseout="tooltip.hide();" src="<%=path%>/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '${notesConstants.FISCAL_PERIOD}&moduleRefId=${fiscalyear.periodDis}', 300, 900);"/>
                                </display:column>
                                <%i++;%>
                            </display:table>
                        </div>
                        <%}%>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
            <html:hidden property="startingAccount"/>
            <html:hidden property="endingAccount"/>
            <html:hidden property="id"/>
            <html:hidden property="index"/>
            <html:hidden property="toPeriod"/>
            <html:hidden property="ecuReport" styleId="ecuReport"/>
        </html:form>
    </body>
    <c:if test="${!empty fileName}">
        <script type="text/javascript">
            window.parent.showGreyBox("Trial Balance Report", "<%=path%>/servlet/FileViewerServlet?fileName=${fileName}");
        </script>
    </c:if>
    <c:if test="${fiscalPeriod!=null}">
        <script type="text/javascript">
            window.parent.showGreyBox("Fiscal Period Report", "<%=path%>/servlet/FileViewerServlet?fileName=${fiscalPeriod}");
        </script>
    </c:if>
    <c:if test="${incomeStatement!=null}">
        <script type="text/javascript">
            window.parent.showGreyBox("Income Statement Report", "<%=path%>/servlet/FileViewerServlet?fileName=${incomeStatement}");
        </script>
    </c:if>
    <c:if test="${!empty exportToExcelFile}">
        <script type="text/javascript">
            UploadDownloaderDWR.downloadFile('${exportToExcelFile}', function(file) {
                if (null != file) {
                    dwr.engine.openInDownload(file);
                }
            });
        </script>
    </c:if>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

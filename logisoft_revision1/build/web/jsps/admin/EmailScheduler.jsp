<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil"%>
<%@page import="com.gp.cong.logisoft.domain.EmailSchedulerVO"%>
<%@page import="com.gp.cong.logisoft.bc.scheduler.EmailSchedulerBC,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<jsp:useBean id="emailscheduler" class="com.gp.cong.logisoft.domain.EmailSchedulerVO" scope="request"></jsp:useBean>
<%@include file="../includes/jspVariables.jsp" %>
<%
            String path = request.getContextPath();
%>

<html> 
    <head>
        <title>JSP for EmailSchedulerForm form</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/util.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/EmailSchedulerBC.js'></script>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
    </head>
    <body class="whitebackgrnd">
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                           grayOut(false,'');">
            </form>
        </div>
        <html:form action="/emailScheduler" name="emailSchedulerForm1" type="com.gp.cong.logisoft.struts.form.EmailSchedulerForm" scope="request">

            <table class="tableBorderNew" width="100%" cellpadding="4" cellspacing="0">
                <tr class="tableHeadingNew"><td colspan="8">Search Scheduler</td><td align="right"><input type="button" value="New Mail" class="buttonStyleNew" onclick="openMailWindow();"/></td></tr>
                <tr style="padding-top:10px;padding-bottom:10px;" class="textlabelsBold">
                    <td>Sort By</td>
                    <td><html:select property="sortBy" styleId="sortBy" value="${emailSchedulerForm.sortBy}" style="width: 130px;" styleClass="dropdown_accounting">
                            <html:optionsCollection name="calenderList"/></html:select></td>
                        <td>Start Date</td>
                        <td><html:text property="startDate" styleClass="textlabelsBoldForTextBox float-left" onblur="validateDate(this);" styleId="txtcal1" size="14" value="${emailSchedulerForm.startDate}"> </html:text>
                        <img src="<%=path%>/img/CalendarIco.gif" alt="cal1" width="16" height="16" align="middle" id="cal1" style="margin-top:3px" class="calendar-img"
                             onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                    <td>End Date</td>
                    <td>
                        <html:text property="endDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox float-left" onblur="validateDate(this);" size="14" value="${emailSchedulerForm.endDate}"></html:text>
                        <img src="<%=path%>/img/CalendarIco.gif" alt="cal2" width="16" height="16" align="middle" id="cal2" style="margin-top:3px" class="calendar-img"
                             onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                    <td>To Email/Fax</td>
                    <td><input name="toEmailOrFax" class="textlabelsBoldForTextBox" id="toEmailOrFax"  value="${emailSchedulerForm.toEmailOrFax}" size="20"/>
                        <input type="hidden" name="toEmailOrFaxValid" id="toEmailOrFaxValid"  value="${emailSchedulerForm.toEmailOrFax}"/>
                        <%--	<div id="toEmailOrFaxDiv" class="newAutoComplete"></div>--%>
                    </td>
                </tr>
                <tr style="padding-top:10px;padding-bottom:10px;" class="textlabelsBold">
                    <td>Status</td>
                    <td>
                        <html:select property="status" styleId="status" value="${emailSchedulerForm.status}" style="width: 130px;" styleClass="dropdown_accounting">
                            <html:optionsCollection name="statusList"/></html:select>
                        </td>
                    <c:choose>
                        <c:when test="${empty emailSchedulerForm.fileName}">
                            <td>Document Id</td>
                            <td>
                                <input id="fileName" name="fileName" class="textlabelsBoldForTextBox" size="18" value="${emailSchedulerForm.fileName}"/>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>Document Id</td>
                            <td>
                                <input id="fileName" name="fileName" class="textlabelsBoldForTextBox" size="15" value="${emailSchedulerForm.fileName}" readonly="readonly"/>
                            </td>
                        </c:otherwise>
                    </c:choose>

                    <td>User Name</td>
                    <td>
                        <input name="userName" id="userName" class="textlabelsBoldForTextBox" size="20" value="${emailSchedulerForm.userName}" />
                        <input type="hidden" name="userNameValid" id="userNameValid"  value="${emailSchedulerForm.userName}"/>
                        <div id="userNameDiv" class="newAutoComplete"></div>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td align="center" colspan="8"  style="padding-bottom:10px;">
                        <input type="button" class="buttonStyleNew" onclick="searchMail()" value="Search" name="search" />
                        <input type="button" class="buttonStyleNew" onclick="resetForm()" value="Reset" name="reset" />
                    </td>
                </tr>
            </table>
            <br>
            <table class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                    <td>Search Results
                    </td>
                    <td width="10%" style="color:red;" id="pendingSize"></td>
                    <td align="right">
                        <input type="button" value="Send" class="buttonStyleNew" onclick="sendMail()"/>
                    </td>
                </tr>
            </table>
            <div id="divtablesty1" style="border: thin; overflow: scroll;">
                <table class="displaytagstyle" width="100%" border="0" cellpadding="1" cellspacing="1" id="EmailLists">
                    <thead id="EmailLists" align="center">
                        <tr  align="center">
                            <th width="5%"></th>
                            <th width="5%">Name</th>
                            <th width="30%">FileLocation</th>
                            <th width="20%">To Email/Fax</th>
                            <th width="5%">Type</th>
                            <th width="10%">Status</th>
                            <th width="5%">NoOfTries</th>
                            <th width="10%">UserName</th>
                            <th width="10%">EmailDate</th>
                            <th width="5%">Action</th>
                        </tr>
                    </thead>
                </table>
                <tr>
                    <td colspan="3">
                        <table class="displaytagstyle" width="100%" border="0" cellpadding="1" cellspacing="1">
                            <tbody id="EmailList" >
                                <tr id="EmailPattern" align="center" style="display:none;">
                                    <td width="5%" id="tablemailCheck"</td>
                                    <td width="5%"id="tableName"</td>
                                    <td width="30%" id="tableFileLocation"</td>
                                    <td width="20%"id="tableEmailIdOrFaxNo"</td>
                                    <td width="5%"id="tableType"</td>
                                    <td width="10%"id="tableStatus"</td>
                                    <td width="5%" id="tableNoOfTries"</td>
                                    <td width="10%" id="tableUserName"</td>
                                    <td width="10%" id="tableEmailDate"</td>
                                    <td width="5%" id="tableAction"</td>
                                </tr>
                            </tbody>
                        </table>
            </div>
            <html:hidden property="action"/>
            <html:hidden property="sortBy" styleId="sortBy" value="${emailSchedulerForm.sortBy}"/>
            <html:hidden property="status" styleId="status" value="${emailSchedulerForm.status}"/>
            <%--<html:hidden property="startDate" value="${emailSchedulerForm.startDate}"/>
			<html:hidden property="endDate" value="${emailSchedulerForm.endDate}"/>--%>
            <input type="hidden" id="moduleId" value="${moduleId}"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript">
        function fillSearchList(data){
            var trClass = "odd";
            dwr.util.removeAllRows("EmailList", { filter:function(tr) {
                    return (tr.id != "EmailPattern");
                }});
            var status = document.getElementById("status");
            if(status && status.value == 'PENDING'){
                if(data.length > 0){
                    document.getElementById("pendingSize").innerHTML="Pending Count :"+data.length;
                }else{
                    document.getElementById("pendingSize").innerHTML="";
                }
            }else{
                document.getElementById("pendingSize").innerHTML="";
            }
            for(var i =0; i < data.length; i++) {

                var EmailRecord = data[i];
                var id = EmailRecord.id;
                dwr.util.cloneNode("EmailPattern", { idSuffix:id });
                dwr.util.setValue("tableName" + id, EmailRecord.name);
                dwr.util.setValue("tableFileLocation" + id, EmailRecord.tempFileLocation);
                var toAddress = EmailRecord.toAddress;
                if(null!=toAddress && toAddress.length>2){
                    toAddress=toAddress.replace(/,/g, "\n");
                }
                dwr.util.setValue("tableEmailIdOrFaxNo" + id,toAddress );
                dwr.util.setValue("tableType" + id, EmailRecord.type);
                dwr.util.setValue("tableStatus" + id, EmailRecord.status);
                dwr.util.setValue("tableNoOfTries" + id, EmailRecord.noOfTries);
                dwr.util.setValue("tableEmailDate" + id,EmailRecord.formatedEmailDate);
                dwr.util.setValue("tableUserName" + id,EmailRecord.userName);
                document.getElementById("EmailPattern" + id).style.display = "block";
                document.getElementById("EmailPattern" + id).className = trClass;
                var mailCheck = document.getElementById("tablemailCheck"+ id);
                var action = document.getElementById("tableAction" + id);
                action.className = "hotspot";
                var assignCHK = document.createElement("input");
                assignCHK.setAttribute("type","checkbox");
                assignCHK.setAttribute("name","mailCheck");
                assignCHK.setAttribute("id","mailCheck");
                assignCHK.setAttribute("value",EmailRecord.id);
                mailCheck.appendChild(assignCHK);
                var img = document.createElement("img");
                img.setAttribute("src", "<%=path%>/img/icons/preview.gif");
                img.onmouseover=new Function("tooltip.show('Report',null,event);");
                img.onmouseout=new Function("tooltip.hide();");
                if(EmailRecord.multiAttachment) {
                    img.onclick = new Function("GB_show('View Attachments','<%=path%>/emailScheduler.do?fileName="+EmailRecord.fileLocation+"&action=showAttachements','210','300');");
                }else {
                    img.onclick = new Function("GB_show('PDF Report','<%=path%>/servlet/PdfServlet?fileName=" + EmailRecord.fileLocation+"','400','650');");
                }
                //img.onmouseover = new Function("tooltip.show('<strong>Preview</strong>');");
                //img.onmouseout = new Function("tooltip.hide();");
                action.appendChild(img);
                if(EmailRecord.type=="Email"){
                    var img1 = document.createElement("img");
                    img1.setAttribute("src", "<%=path%>/img/icons/view.gif");
                    img1.onmouseover=new Function("tooltip.show('View Email',null,event);");
                    img1.onmouseout=new Function("tooltip.hide();");
                    img1.onclick = new Function("GB_show('View Email','<%=path%>/emailScheduler.do?mailId="+EmailRecord.id +"','400','650');");
                    //img1.onmouseover = new Function("tooltip.show('<strong>View</strong>');");
                    //img1.onmouseout = new Function("tooltip.hide();");
                    action.appendChild(img1);
                }
                if(EmailRecord.type=="Fax"){
                    var img1 = document.createElement("img");
                    img1.setAttribute("src", "<%=path%>/img/icons/view.gif");
                    img1.onmouseover=new Function("tooltip.show('View Email',null,event);");
                    img1.onmouseout=new Function("tooltip.hide();");
                    img1.onclick = new Function("GB_show('View Cover Letter','<%=path%>/servlet/PdfServlet?fileName=" + EmailRecord.coverLetter+"','400','650');");
                    //img1.onmouseover = new Function("tooltip.show('<strong>View</strong>');");
                    //img1.onmouseout = new Function("tooltip.hide();");
                    action.appendChild(img1);
                }
                if(trClass == "odd") {
                    trClass = "even";
                }else {
                    trClass = "odd";
                }
            }
            document.body.style.cursor = "auto";

        }
        function loadDate(data){
            var startDate = document.getElementById("txtcal1").value;
            var endDate = document.getElementById("txtcal2").value;
            var status = document.getElementById("status").value;
            var userName = document.getElementById("userName").value;
            var fileName = document.getElementById("fileName").value;
            var toEmailOrFax = document.getElementById("toEmailOrFax").value;
            EmailSchedulerBC.searchMail(data[0],data[1],startDate,endDate,status,userName,fileName,toEmailOrFax,fillSearchList);
        }
        function validateDate(data) {
            if(data.value!=""){
                data.value = data.value.getValidDateTime("/","",false);
                if(data.value==""||data.value.length>10){
                    alertNew("Please enter valid date");
                    data.value="";
                    document.getElementById(data.id).focus();
                }
            }
        }
        function resetForm(){
            document.getElementById("txtcal1").value='';
            document.getElementById("txtcal2").value='';
            document.getElementById("userName").value='';
            document.getElementById("fileName").value='';
            document.getElementById("toEmailOrFax").value='';
        }
        function searchMail(){
            if(document.emailSchedulerForm1.txtcal1.value==''&& document.emailSchedulerForm1.txtcal2.value!=''){
                alertNew("Please Enter Start Date");
                return;
            }else if(document.emailSchedulerForm1.txtcal1.value!=''&& document.emailSchedulerForm1.txtcal2.value==''){
                alertNew("Please Enter End Date");
                return;
            }else{
                var sortBy = document.getElementById("sortBy").value;
                EmailSchedulerBC.getDatesForCriteria(sortBy,loadDate);
            }
        }
        function loadMail(moduleId){
            var sortBy = 'TODAY';
            EmailSchedulerBC.getDatesForCriteria(sortBy,function(data){
                EmailSchedulerBC.searchMail(data[0],data[1],'','','Completed','All',moduleId,fillSearchList);
            });
        }

        function sendMail(){
            var list = document.getElementById("EmailPattern").value;
            document.emailSchedulerForm1.action.value="sendMail";
            document.emailSchedulerForm1.submit();
        }
        function openContentsofMail(val){
            var url = "<%=path%>/jsps/fclQuotes/viewEmailTransaction.jsp?id="+val;
            window.open(url ,"EmailWindow","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=450");
            return true;
        }
        function sendStatus() {
            document.emailSchedulerForm1.submit();
        }
		
        function openPDF(file) {
            GB_show('PDF Report','<%=path%>/servlet/PdfServlet?fileName='+file,'400','650');
        }
		
        function openMailWindow() {
            GB_show('Mail Window','<%=path%>/jsps/admin/Mail.jsp','500','650');
        }
        function addMoreParams(element, entry) {
            return entry;
        }
        initAjaxAutoCompleter("userName", "userNameDiv", "userNameValid", "<%=path%>/servlet/AutoCompleterServlet?action=User&textFieldId=userName&requestFrom=emailScheduler","");
        <%--initAjaxAutoCompleter("moduleName", "moduleNameDiv", "moduleNameValid", "<%=path%>/servlet/AutoCompleterServlet?action=Module&textFieldId=moduleName","");--%>
        <%--initAjaxAutoCompleter("toEmailOrFax", "toEmailOrFaxDiv", "toEmailOrFaxValid", "<%=path%>/servlet/AutoCompleterServlet?action=Email&textFieldId=toEmailOrFax&requestFrom=emailScheduler","");--%>
            loadMail('${moduleId}');
    </script>
</html>


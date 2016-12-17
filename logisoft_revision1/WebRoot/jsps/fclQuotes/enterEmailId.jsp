<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ include file="../includes/jspVariables.jsp"%>
<%@ include file="../includes/baseResources.jsp"%>
<%@ include file="../includes/resources.jsp"%>
<html>
    <head>
        <c:if test="${emailForm.buttonValue=='ACCRUALS'}">
            <script type="text/javascript">
                parent.parent.GB_hide();
            </script>
        </c:if>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title></title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/cbrte/html2xhtml.js"></script>
        <script type="text/javascript" src="${path}/js/cbrte/richtext_compressed.js"></script>
        <script type="text/javascript" src="${path}/js/isValidEmail.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <style type="text/css">
            div.newAutoComplete ul{
                width: 420px;
                height: 200px;
            }
        </style>
    </head>
    <body>
        <form name="emailForm" action="${path}/sendEmail.do" type="com.gp.cvst.logisoft.struts.form.EmailForm">
            <script type="text/javascript">
                function sendDetails(){
                    updateRTEs();
                    window.opener.document.getElementById("mailToAddress").value=document.getElementById("toAddress").value;
                    window.opener.document.getElementById("mailCcAddress").value=document.getElementById("ccAddress").value;
                    window.opener.document.getElementById("mailBccAddress").value=document.getElementById("bccAddress").value;
                    window.opener.document.getElementById("mailSubject").value=document.getElementById("subject").value;
                    window.opener.document.getElementById("mailBody").value=htmlDecode(document.mailForm.rte1.value);
                    window.opener.sendReportByEmail();
                    self.close();
                    return true;
                }
                function cancelDetails(){
                    self.close();
                    parent.parent.GB_hide();
                }
                function sendEmail() {
                    if(trim(document.getElementById("toAddress").value)==""){
                        alert("Please Enter To Address");
                    }else if(!isValidEmail(document.getElementById("toAddress").value)){
                        alert("Please Enter Valid To Address");
                    }else{
                        updateRTEs();
                        document.emailForm.body.value = htmlDecode(document.emailForm.rte1.value);
                        document.emailForm.submit();
                <c:if test="${empty emailForm.moduleName || emailForm.moduleName!='ACCRUALS'}">
                                    parent.parent.GB_hide()
                </c:if>
                                }
                            }
                            function loadEmailAddress(){
                                var email="${emailForm.toAddress}";
                                document.emailForm.toAddress.value=email;
                            }
                            function refreshOnce(){
                                window.location.reload();
                            }
                            initRTE("${path}/js/cbrte/images/", "${path}/js/cbrte/", "", true);
            </script>
            <font color="red" size="4" ><b><c:out value="${emailForm.fileNo}"/></b></font>
            <table width="100%" class="tableBorderNew" style="border: 10px;" border="0">
                <tr class="tableHeadingNew">
                <td colspan="2">Compose Mail</td>
                </tr>
                <c:if test="${emailForm.moduleName=='ACCRUALS'}">
                    <tr>
                    <td class="textlabelsBold"> UserName </td>
                    <td>
                        <input type="text" name="userName" id="userName" size="79" class="textlabelsBoldForTextBox"/> 
                        <input type="hidden" name="userNameCheck" id="userNameCheck"/>
                        <div id="userNameChoices" class="newAutoComplete"/>
                    </td>
                    </tr>
                </c:if>
                <tr>
                <td class="textlabelsBold" width="10%"> TO ${emailForm.buttonValue}</td>
                <td>
                    <input type="text" name="toAddress" id="toAddress" size="79" class="textlabelsBoldForTextBox"/>
                </td>
                </tr>
                <tr>
                <td class="textlabelsBold"> CC </td>
                <td>
                    <input type="text" name="ccAddress" id="ccAddress" size="79" value="${emailForm.ccAddress}" class="textlabelsBoldForTextBox"/>
                </td>
                </tr>
                <tr>
                <td class="textlabelsBold"> BCC </td>
                <td>
                    <input type="text" name="bccAddress" id="bccAddress" size="79" class="textlabelsBoldForTextBox"/>
                </td>
                </tr>
                <tr>
                <td class="textlabelsBold">Subject</td>
                <td>
                    <input type="text" name="subject" id="subject" size="79" value="${emailForm.subject}" class="textlabelsBoldForTextBox"/>
                </td>
                </tr>
                <tr>
                <td class="textlabelsBold" valign="top">Message</td>
                <td>
                    <script language="JavaScript" type="text/javascript">
                        <!--
                        var rte1 = new richTextEditor('rte1');
                        <c:choose>
                            <c:when test="${not empty emailForm.body && fn:contains(emailForm.body,'\\\'')}">
                                    rte1.html = "${emailForm.body}";
                            </c:when>
                            <c:otherwise>
                                    rte1.html = '${emailForm.body}';
                            </c:otherwise>
                        </c:choose>
                            rte1.toggleSrc = false;
                            rte1.build();
                            //-->
                    </script>
                </td>
                </tr>
                <tr>
                <td colspan="2" align="center">
                    <input type="button" class="buttonStyleNew" value="Send" onClick="sendEmail()"/>
                    <input type="button" value="Cancel" onclick="cancelDetails()" class="buttonStyleNew"/>
                </td>
                </tr>
            </table>
            <input type="hidden" name="buttonValue" id="buttonValue" value="${emailForm.moduleName}"/>
            <input type="hidden" name="emailOption" id="emailOption" value="${emailForm.emailOption}"/>
            <input type="hidden" name="customerName" id="customerName" value="${emailForm.customerName}"/>
            <input type="hidden" name="customerId" id="customerName" value="${emailForm.customerId}"/>
            <input type="hidden" name="ssBookingNo" id="ssBookingNo" value="${emailForm.ssBookingNo}"/>
            <input type="hidden" name="exclude" id="exclude" value="${emailForm.exclude}"/>
            <input type="hidden" name="body" id="body" value="${emailForm.body}"/>
            <input type="hidden" name="id" value="${emailForm.id}"/>
            <input type="hidden" name="toPeriod" value="${emailForm.toPeriod}"/>
            <input type="hidden" name="fromPeriod" value="${emailForm.fromPeriod}"/>
            <input type="hidden" name="reportTitle" value="${emailForm.reportTitle}"/>
            <input type="hidden" name="transactionId" value="${emailForm.transactionId}">
            <input type="hidden" name="screenName" value="${emailForm.screenName}">
            <input type="hidden" name="fileLocation" value="${emailForm.fileLocation}">
        </form>
        <%!            public static String rteSafe(String strText) {
                //returns safe code for preloading in the RTE
                String tmpString = strText;
                //convert all types of single quotes
                tmpString = tmpString.replace((char) 145, (char) 39);
                tmpString = tmpString.replace((char) 146, (char) 39);
                tmpString = tmpString.replace("'", "&#39;");
                //convert all types of double quotes
                tmpString = tmpString.replace((char) 147, (char) 34);
                tmpString = tmpString.replace((char) 148, (char) 34);
                //	tmpString = tmpString.replace("\"", "\"");
                //replace carriage returns & line feeds
                tmpString = tmpString.replace((char) 10, (char) 32);
                tmpString = tmpString.replace((char) 13, (char) 32);
                return tmpString;
            }
        %>
        <script type="text/javascript">
            if(document.getElementById("userName")){
                var url =  rootPath+"/servlet/AutoCompleterServlet?action=User&get=emailAddress&textFieldId=userName";
                AddressAutocompleter("userName", "userNameChoices", "toAddress", "userNameCheck",url);
                function AddressAutocompleter(textField,divToPopulate,idField,validateField,url) {
                    new Ajax.ScrollAutocompleter(textField, divToPopulate, url, {
                        paramName: textField,
                        tokens:"<-->",
                        afterUpdateElement : function (text, li) {
                            if(li.id!="No Record"){
                                if(trim($(idField).value)!=""){
                                    $(idField).value = trim($(idField).value)+";"+li.id;
                                }else{
                                    $(idField).value = li.id;
                                }
                                $(validateField).value = text.value;
                                $(textField).blur();
                            }else{
                                $(textField).value = "";
                                $(validateField).value = "";
                                $(textField).focus();
                            }
                        }
                    });

                    Event.observe(textField, "blur", function (event){
                        var element = Event.element(event);
                        if($(validateField)!=undefined && element.value!=$(validateField).value){
                            $(textField).value = "";
                            $(validateField).value = "";
                        }
                    });
                }
            }
        </script>
    </body>
    <c:if test="${refresh}">
        <script>refreshOnce();</script>
    </c:if>
</html>

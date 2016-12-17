<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesConstants"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<%@include file="../includes/jspVariables.jsp"%>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    if (request.getAttribute("valid") != null && request.getAttribute("valid").equals("inValid")) {
%>
<font color="red">In valid Password....</font>
    <%
    } else if (request.getAttribute("valid") != null) {
    %>
<script type="text/javascript">
    manifestCorrection('${path}', '${FclBlCorrectionForm.id}', '${FclBlCorrectionForm.blNumber}');
    function manifestCorrection(path, noticeNumber, blnumber) {
        alert("Correction Notice " + blnumber + "-" + noticeNumber + " is Approved and Posted.....");
        parent.parent.document.fclBlCorrectionsForm.submit();
        parent.parent.GB_hide();
        parent.parent.location.href = path + "/fclBlCorrections.do?buttonValue=search";
    }
</script>
<%}%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>My JSP 'authanticatePassword.jsp' starting page</title>
        <base href="<%=basePath%>">
        <%@include file="../includes/baseResources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>


        <script type="text/javascript">
    function checkPassword() {
        document.getElementById("submitButton").style.display = "none";
        if (typeof window.event !== 'undefined')
            document.onkeydown = function() {
                return (event.keyCode !== 8);
            };
        else
            document.onkeypress = function(e) {
                return (e.keyCode !== 8);
            };
        var val2 = document.fclBlCorrectionsForm.id.value;
        var val1 = document.fclBlCorrectionsForm.index1.value;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                methodName: "getCorrection",
                param1: val1,
                param2: val2
            },
            success: function(data) {
                checkNoticeNo(data);
            }
        });
    }
    function checkNoticeNo(data) {
        if (data === null || data === "null") {
            document.getElementById('eventCode').value = '100016';
            document.getElementById('moduleRefId').value = "${param.fileNo}";
            var blNumber = document.fclBlCorrectionsForm.index1.value;
            var notice = document.fclBlCorrectionsForm.id.value;
            document.getElementById('eventDesc').value = "(" + blNumber + "-" + notice + ") is got Approved and Post";
            document.getElementById("submitButton").style.display = "none";
            document.fclBlCorrectionsForm.buttonValue.value = "CheckPassword";
            document.getElementById('cover').style.display = 'block';
            document.fclBlCorrectionsForm.submit();
        } else {
            alert("Please Approve " + data + " notice number before this.... ");
            parent.parent.GB_hide();
        }
    }
    var singleClick = true;
    (function() {
        document.addEventListener('keypress', function(event) {
            if (event.keyCode === 13) {
                if (document.getElementById('password').value !== "" && singleClick) {
                    event.preventDefault();
                    singleClick = false;
                    document.getElementById('submitButton').click();
                    document.getElementById("submitButton").style.display = "none";
                } else {
                    event.preventDefault();
                }
            }
        });
    }());
    document.oncontextmenu = mischandler;
    document.onmousedown = mousehandler;
    document.onmouseup = mousehandler;
        </script>
    </head>

    <body  class="whitebackgrnd">
        <div id="cover" style="width: 906px ;height: 1000px;"></div>
        <html:form action="/fclBlCorrections" scope="request">
            <table cellpadding="2" cellspacing="2" border="0" width="100%">
                <tr><td style="font:bold;color:Blue;font-size:11px"><c:choose>
                            <c:when test="${not empty param.blId}">
                                <c:out value="${param.blId}"/>&nbsp;  Correction Notice will be posted
                            </c:when>
                            <c:otherwise>
                                <c:out value="${FclBlCorrectionForm.index1}"/>&nbsp; Correction Notice will be posted
                            </c:otherwise>
                        </c:choose>
                    </td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr class="textlabels" align="center">
                    <td>Enter Password</td>
                </tr>
                <tr>
                    <td align="center"><input id="password" type="password" name="password"/></td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="button" value="Submit" id="submitButton" class="buttonStyleNew"  onclick="checkPassword()"/>
                        <input type="button" value="Close" class="buttonStyleNew" onclick="parent.parent.GB_hide();"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
            <c:choose>
                <c:when test="${not empty param.blId}">
                    <html:hidden property="index1" value="${param.blId}"  />
                    <html:hidden property="blNumber" value="${param.blId}"/>
                    <html:hidden property="id" value="${param.notice}"  />
                    <html:hidden property="moduleRefId" styleId="moduleRefId" value="${param.fileNo}" />
                </c:when>
                <c:otherwise>
                    <html:hidden property="index1" value="${FclBlCorrectionForm.index1}"  />
                    <html:hidden property="id" value="${FclBlCorrectionForm.id}"  />
                    <html:hidden property="blNumber" value="${FclBlCorrectionForm.index1}"/>
                    <html:hidden property="moduleRefId" styleId="moduleRefId" value="${FclBlCorrectionForm.moduleRefId}" />
                </c:otherwise>
            </c:choose>

            <html:hidden property="blNumber"/>
            <html:hidden property="temp" />
            <html:hidden property="eventCode" styleId="eventCode"/>
            <html:hidden property="eventDesc" styleId="eventDesc"/>
            <html:hidden property="moduleId" value="<%=NotesConstants.MODULE_ID_CORRECTION%>"/>

        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
</html>

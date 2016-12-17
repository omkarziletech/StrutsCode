<%-- 
    Document   : addTerminalManagers
    Created on : Jul 27, 2016, 10:51:26 AM
    Author     : Nambu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Terminal Managers</title>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css" />
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>

    </head>
    <body>
        <html:form action="/editTerminal" name="TerminalForm"  type="com.gp.cong.logisoft.struts.form.EditTerminalForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="1" class="tableBorderNew">
                <tr class="tableHeadingNewGreen">
                    <th>User Name</th>
                    <th>Full Name</th>
                    <th>Email Address</th>
                    <th></th>
                    <th>Action</th>
                </tr>
                <tr><td colspan="4">&nbsp&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp; <html:text property="userName" styleId="userName"/></td>
                    <td ><input type="text" name="managerName" id="managerName" class="textlabelsBoldForTextBoxDisabledLookWidth" readonly="true">
                    </td>
                    <td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;
                        <input type="text"  name="managerEmail" id="managerEmail"  class="textlabelsBoldForTextBoxDisabledLookWidth"  readonly="true"> 
                    </td>
                    <td></td>
                    <td><input type="button" value="Save" class="buttonStyleNew" onclick="saveManager();"> </td>
                </tr>
                <tr><td>&nbsp&nbsp;</td></tr>
                <tr><td>&nbsp&nbsp;</td></tr>
                <tr><td>&nbsp&nbsp;</td></tr>

                <c:forEach var="terminal" items="${managerList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td class="boldPurple">&nbsp&nbsp;${terminal.userName}</td>
                        <td class="boldPurple">${terminal.managerName}</td>
                        <td class="boldPurpleSmall">${terminal.managerEmail}<td>
                        <td ><img src="${path}/images/trash.png" title="Delete" 
                                  onclick="deleteManager('${terminal.terminalManagerId}', '${terminal.userName}');"/></td>
                    </tr>
                </c:forEach>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" />
            <html:hidden property="termNo" styleId="termNo" value="${termNo}"/>
            <html:hidden property="terminalManagerId" styleId="terminalManagerId"/>
        </html:form>

        <script type="text/javascript">
            jQuery(document).ready(function () {
                initManagerAutocomplete();
            });

            function initManagerAutocomplete() {
                jQuery("#userName").initautocomplete({
                    url: "${path}" + "/autocompleter/action/getAutocompleterResults.jsp?query=USER_DETAILS&template=vendor&fieldIndex=1,2,3&",
                    width: "425px",
                    otherFields: "managerName^managerEmail",
                    resultsClass: "ac_results z-index",
                    resultPosition: "absolute",
                    scroll: true,
                    scrollHeight: 200,
                    onblur: "clearData()"
                });
            }
            function clearData() {
                jQuery("#userName").val('');
                jQuery("#managerName").val('');
                jQuery("#managerEmail").val('');
            }
            function saveManager() {
                if (jQuery("#userName").val() === "") {
                    alert("Please Enter UserName");
                    jQuery("#userName").focus();
                } else {
                    document.TerminalForm.buttonValue.value = "saveManager";
                    document.TerminalForm.submit();
                }
            }
            function deleteManager(terminalId, userName) {
                var result = confirm("Are you sure want to delete?");
                if (result) {
                    document.TerminalForm.buttonValue.value = "deleteManager";
                    document.TerminalForm.terminalManagerId.value = terminalId;
                    document.TerminalForm.userName.value = userName;
                    document.TerminalForm.submit();
                }
            }
        </script>
    </body>
</html>
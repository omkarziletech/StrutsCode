<%-- 
    Document   : RetAddEdit
    Created on : Jul 28, 2010, 4:45:27 PM
    Author     : Vinay
--%>
<%@taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/baseResources.jsp" %>
<%@include file="includes/resources.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript">
            start = function(){
                serializeForm();
            }
            window.onload=start;
        </script>
        <title>Edit RetAdd</title>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/retAddSearch" type="com.gp.cong.logisoft.struts.form.RetAddSearchForm"
                   name="retAddSearchForm" method="post" scope="request">
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr><td colspan="6" class="tableHeadingNew">Edit RetAdd</td></tr>
                <tr><td colspan="6" style="border-bottom: 2px"></td></tr>
                <tr>
                    <td class="textlabelsBold">Origin</td>
                    <td><input class="textlabelsreadOnlyForTextBox" type="text" value="${currentRetAdd.originTerminal}" readonly /></td>
                    <td class="textlabelsBold">Destination</td>
                    <td><input class="textlabelsreadOnlyForTextBox" type="text" value="${currentRetAdd.destinationPort}" readonly /></td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Email</td>
                    <td>
                        <input class="textlabelsBoldForTextBox" type="text" name="email"
                               value="${currentRetAdd.email}"/></td>
                    <td class="textlabelsBold">Code</td>
                    <td><input class="textlabelsreadOnlyForTextBox" type="text" name="code" value="${currentRetAdd.code}" maxlength="1" readonly/></td>
                </tr>
                <tr>
                    <td colspan="4" align="center">
                        <input type="submit"  value="Save" class="buttonStyleNew" />
                        <input type="submit"  value="Cancel" class="buttonStyleNew" onclick="cancel()"/>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="action" value="edit"/>
            <input type="hidden" name="originId" value="${currentRetAdd.originId}"/>
            <html:hidden property="retAddId" styleId="retAddId" value="${retAddSearchForm.retAddId}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function cancel(){
            document.retAddSearchForm.action.value = "cancel";
        }
    </script>
</html>

<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
            String message = "";
            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            String rolecode = "";
            if (request.getAttribute("rolecode") != null) {
                rolecode = (String) request.getAttribute("rolecode");
            }
            if (rolecode.equals("addrole")) {
%>
<script>
    parent.parent.GB_hide();
    parent.parent.getAddRole();
</script>
<%            }
%>

<script language="javascript" type="text/javascript">
    function searchform(){
        if(document.roleCodeForm.roleName.value==""){
            alert("Please enter the Role Name");
            return;
        }
        document.roleCodeForm.buttonValue.value="search";
        document.roleCodeForm.submit();
    }
</script>
<html> 
    <head>
        <title>New Role Jsp Page</title>
        <%@include file="../includes/resources.jsp" %>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
    <%@include file="../includes/baseResources.jsp" %>
</head>
<body>
    <html:form action="/roleCode" scope="request">
        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr><td class="tableHeadingNew" colspan="2">Add Role Name</td></tr>
            <tr><td colspan="2"><font color="blue" size="2"><%=message%></font></td></tr>
            <tr><td colspan="2" style="padding-top: 3px"></td></tr>
            <tr>
                <td width="80" id="labelText">Role Name&nbsp;</td>
                <td>
                    <html:text property="roleName" maxlength="50" size="10"
                               styleClass="textlabelsBoldForTextBox width-145px" style="text-transform:uppercase" ></html:text>
                    <input type="button" class="buttonStyleNew"
                           onclick="searchform()"  id="search"  name="search" value="Submit"/>
                </td>
            </tr>
            <tr><td colspan="2" style="padding-top: 3px"></td></tr>
        </table>
        <html:hidden property="buttonValue" styleId="buttonValue" />
    </html:form>
</body>
</html>


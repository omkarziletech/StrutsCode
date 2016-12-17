<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
            String message = "";
            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            String warehousecode = "";
            if (request.getAttribute("usercode") != null) {
                warehousecode = (String) request.getAttribute("usercode");
            }
            if (warehousecode.equals("adduser")) {
%>
<script>
	
    parent.parent.GB_hide();
    parent.parent.getUserPage();
    <%--		self.close();--%>
    <%--		opener.location.href="<%=path%>/jsps/admin/NewUser.jsp";--%>
</script>
<%            }
%>

<html> 
    <head>
        <title>Add User Login</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/common.js" ></script>
        <script language="javascript" type="text/javascript">
            function searchform(){
                if(document.userCodeForm.loginName.value==""){
                    alert("Please enter the Login Name");
                    return;
                }
                if(document.userCodeForm.loginName.value.length<3){
                    alert("Please enter minimum of 3 characters");
                    return;
                }
                if(document.userCodeForm.loginName.value.match(" ")){
                    alert("Space is not allowed for LoginName");
                    return;
                }
                if (isSpecialExceptPeriod(document.userCodeForm.loginName.value) == false){
                    alert("Special Characters not allowed for Login Name");
                    return;
                }
                document.userCodeForm.buttonValue.value="search";
                document.userCodeForm.submit();
            }
        </script>
    </head>
    <body>
        <html:form action="/userCode" scope="request">
            <table width="100%"  border="0" cellpadding="0"
                   cellspacing="0" align="center" class="tableBorderNew" >
                <tr><td class="tableHeadingNew" colspan="2">Add Login Name</td></tr>
                <tr>
                    <td colspan="2">
                        <font color="blue" size="2"><%=message%></font>
                    </td></tr>
                <tr>
                    <td id="labelText">Login Name
                        <label style="color: red;font-size: 18px;font-weight: bold">*</label>
                        &nbsp;
                    </td>
                    <td>
                        <html:text property="loginName" maxlength="100" size="10" styleClass="textlabelsBoldForTextBox width-200px">
                        </html:text>
                        <input type="button" class="buttonStyleNew" value="Submit"
                               id="search" onclick="searchform();">
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" />
        </html:form>
    </body>
</html>


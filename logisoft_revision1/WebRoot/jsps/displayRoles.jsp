<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%-- 
    Document   : displayRoles
    Created on : Jul 14, 2010, 5:46:28 PM
    Author     : Vinay
--%>

<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@page import="java.util.Collections"%>
<%@page import="org.hibernate.mapping.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/baseResources.jsp" %>
<%@include file="includes/resources.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Roles List</title>
    </head>
    <body class="whitebackgrnd" topmargin="1" >
        <html:form action="/editRoleDuties" name="editRoleDForm" type="com.gp.cong.logisoft.struts.form.EditRoleDForm" method="post" scope="request">
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr class="tableHeadingNew">&nbsp;Current Roles</tr>
                <tr>
                    <td align="center">
                        <display:table name="${rolesList}" defaultorder="descending" class="displaytagstyleNew"
                                       id="documentListItem" style="width:100%" sort="list" requestURI="/editRoleDuties.do" >
                            <display:column title="Role Name" property="roleDesc" sortable="true" headerClass="sortable" />
                            <display:column title="Created On" property="roleCreatedOn" sortable="true" headerClass="sortable" format="{0,date,MMM-dd-yyyy hh:mm a}"/>
                            <display:column title="Updated By" property="updatedBy" sortable="true" headerClass="sortable"/>
                            <display:column title="Updated On" property="updatedDate" sortable="true" headerClass="sortable"  format="{0,date,MMM-dd-yyyy hh:mm a}"/>
                            <display:column title="Action" class="buttonStyleNew"  sortable="false" >
                                <img src="${path}/img/icons/edit.gif" onclick="fill('${documentListItem.roleId}', '${documentListItem.roleDesc}')"
                                     onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/>
                            </display:column>
                        </display:table>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="action" value="edit" />
            <input type="hidden" name="roleId" id="roleId" />
            <input type="hidden" name="roleName" id="roleName" />
            <script type="text/javascript">
                function fill(roleId, roleName) {
                    document.editRoleDForm.roleName.value = roleName;
                    document.editRoleDForm.roleId.value = roleId;
                    document.editRoleDForm.action.value = 'edit';
                    document.editRoleDForm.submit();

                }
            </script>
        </html:form>
    </body>
</html>

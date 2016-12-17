<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1"/>
        <title>Show Online Users</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="<c:url value="/js/common.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/admin/usersList.js"/>"></script>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/onlineUsers.do" name="onlineUsersForm" styleId="onlineUsersForm"
                   type="com.logiware.form.OnlineUsersForm" scope="request">
            <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                    <td>Users Online</td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='Export to Excel' onclick="exportUsersToExcel()"/>
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='Show Online Users' onclick="getOnlineUsers()"/>
                      <%--  <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='System Task Process' onclick="getSystemTaskProcess()"/>--%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="onlineUsersDiv" >
                            <c:if test="${!empty onlineUsers}">
                                <display:table name="${onlineUsers}" pagesize="50" id="onlineUsers" sort="list"
                                               class="displaytagStyleNew" style="width:100%" requestURI="/onlineUsers.do?">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"><font color="blue">{0}</font>
                                            Online Users displayed,For more Records click on page numbers.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">One {0} displayed. Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">{0} {1} displayed, Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner">No Records Found.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="top"/>
                                    <display:setProperty name="paging.banner.item_name" value="Online Users"/>
                                    <display:setProperty name="paging.banner.items_name" value="Online Users"/>
                                    <display:column title="Login ID" property="loginName" headerClass="sortable" sortable="true"/>
                                    <display:column title="First Name" property="firstName" headerClass="sortable" sortable="true"/>
                                    <display:column title="Last Name" property="lastName" headerClass="sortable" sortable="true"/>
                                    <display:column title="Terminal" property="term" headerClass="sortable" sortable="true"/>
                                    <display:column title="Phone" property="telephone" headerClass="sortable" sortable="true"/>
                                    <display:column title="Address" property="address1" headerClass="sortable" sortable="true"/>
                                    <display:column title="City" property="city" headerClass="sortable" sortable="true"/>
                                    <display:column title="State" property="state" headerClass="sortable" sortable="true"/>
                                    <display:column title="Country" property="country" headerClass="sortable" sortable="true"/>
                                    <display:column title="IP Address" property="userIpAddress" headerClass="sortable" sortable="true"/>
                                    <display:column title="Logged On" property="date" headerClass="sortable" sortable="true"/>
                                    <display:column title="Action" sortable="true" headerClass="sortable" style="color:red">
                                        <span class="hotspot"  style="cursor:pointer">
                                            <img src="${path}/img/Remove-User.png" height="20px" width="20px"alt="Remove" onclick="removeOnlineUsers('${onlineUsers.userId}')"/>
                                        </span>
                                     </display:column>
                                </display:table>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
                <html:hidden property="buttonValue"/>
                <html:hidden property="userId"/>
            </html:form>
    </body>
    <script type="text/javascript">
        function exportUsersToExcel()
        {
            document.onlineUsersForm.buttonValue.value = "exportToExcel";
            document.onlineUsersForm.submit();
        }
    </script>
</html>



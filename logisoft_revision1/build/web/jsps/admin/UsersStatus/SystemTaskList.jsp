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
        <title>System Task Process</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="<c:url value="/js/common.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/admin/usersList.js"/>"></script>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/onlineUsers.do" name="onlineUsersForm" styleId="onlineUsersForm"
                   type="com.logiware.form.OnlineUsersForm" scope="request">
            <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                    <td>System Task Process</td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='Show Online Users' onclick="getOnlineUsers()"/>
                    
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='System Task Process' onclick="getSystemTaskProcess()"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="onlineUsersDiv">
                            <c:if test="${!empty systemTaskList}">
                                <display:table name="${systemTaskList}" pagesize="100" id="systemTask"
                                               class="displaytagStyleNew" style="width:100%">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"><font color="blue">{0}</font>
                                            System Task List displayed,For more Records click on page numbers.</span>
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
                                    <display:setProperty name="paging.banner.placement" value="bottom"/>
                                    <display:setProperty name="paging.banner.item_name" value="Online Users"/>
                                    <display:setProperty name="paging.banner.items_name" value="Online Users"/>
                                    <display:column title="Process Name" property="imageName" headerClass="sortable" sortable="true"/>
                                    <display:column title="ProcessId" property="processId" headerClass="sortable" sortable="true"/>
                                    <display:column title="CpuMemoryUsage" property="cpuMemoryUsage" headerClass="sortable" sortable="true"/>
                                    <display:column title="SystemTaskUser" property="systemTaskUser" headerClass="sortable" sortable="true"/>
                                    <display:column title="Status" property="imageStatus" headerClass="sortable" sortable="true"/>
                                </display:table>
                            </c:if>
                        </div>
                    </td>
                </tr>
                 </table>
                <html:hidden property="buttonValue"/>
            </html:form>
    </body>
</html>



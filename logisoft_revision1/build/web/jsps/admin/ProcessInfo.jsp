<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
%>
<html> 
    <head>
        <title>JSP for ProcessInfoForm form</title>
        <script type="text/javascript">
            function delete1(val){
                document.getElementById("unlockButton").disabled = true;
                document.processInfoForm.index.value=val;
                document.processInfoForm.buttonValue.value="delete";
                document.processInfoForm.submit();
            }
            function refreshPage(){
               document.processInfoForm.buttonValue.value="";
               document.processInfoForm.submit();
            }
        </script>
        <%@include file="../includes/baseResources.jsp" %>

        <%@include file="../includes/resources.jsp" %>

    </head>
    <body>
        <html:form action="/processInfo" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" ><td>List of Locked Pages</td>
                <td align="right"> 
                <span class="hotspot" onmouseover="tooltip.show('<strong>Refresh</strong>',null,event);" onmouseout="tooltip.hide();">
                            <img src="<%=path%>/img/icons/Button-Refresh-icon.png" border="0"  onclick="refreshPage()" /></span>
                    
                 </td>
                </tr>
                <tr><td colspan="2">

                        <div id="divtablesty1" class="scrolldisplaytable">
                            <table  border="0" cellpadding="0" cellspacing="0" >

                                <display:table name="${lockedList}" pagesize="<%=pageSize%>" class="displaytagstyle" id="divtablesty1" sort="list"  style="width:100%">

                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> User details displayed,For more Users click on page numbers.
                                            <br>
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">
						One {0} displayed. Page Number
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">
						{0} {1} Displayed, Page Number

                                        </span>
                                    </display:setProperty>

                                    <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Process Info"/>
                                    <display:setProperty name="paging.banner.items_name" value="Process Infos"/>
                                    <display:column property="userName" title="User Name"/>
                                    <display:column property="role" title="Role"/>
                                    <display:column property="telephone" title="Phone"/>
                                    <display:column property="email" title="Email"/>
                                    <display:column property="processinfodate" title="Date"/>
                                    <display:column property="itemName" title="Program Name"/>
                                    <display:column property="moduleId" title="Module Id"/>
                                    <display:column property="recordid" title="Record Id"/>
                                    <display:column title="Actions">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>UnLock</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/lock.gif" border="0" id="unlockButton"  onclick="delete1(${divtablesty1.id})" /></span>
                                        </display:column>
                                    </display:table>

                                <%
                                %>


                            </table></div>
                    </td></tr>
            </table>
            <html:hidden property="buttonValue"/>
            <html:hidden property="index"/>
        </html:form>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


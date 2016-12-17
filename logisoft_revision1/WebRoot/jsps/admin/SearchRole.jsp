<%@ page language="java" pageEncoding="ISO-8859-1"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.SearchUserBean,com.gp.cong.logisoft.domain.Role"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="../includes/baseResourcesForJS.jsp" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link type="text/css" rel="stylesheet" media="screen" href="${contextPath}/jsps/LCL/css/lable-fields.css"/>
<script type="text/javascript" src="${contextPath}/js/jquery/jquery.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery/jquery-ext.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery/jquery.centreIt-1.1.5.js"></script>
<script type="text/javascript" src="${contextPath}/js/jquery/jquery-impromptu.js"></script>
<link type="text/css" rel="stylesheet" href="${contextPath}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${contextPath}/js/jquery/jquery.tooltip.js"></script>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            DBUtil dbUtil = new DBUtil();
            String buttonValue = "";
            SearchUserBean sBean = new SearchUserBean();
            List roleList = null;
            String msg = "";
            String link = "";
            String modify = null;
//test.getTreeStucture(request);

            Role role = new Role();

            role.setMatch("starts");
            String rolName = "";
            if (request.getParameter("name") != null) {
                rolName = (String) request.getParameter("name");
            }
            String message = "";
            if (request.getAttribute("msg") != null) {
                message = (String) request.getAttribute("msg");
            }

            if (request.getParameter("message") != null) {
                msg = (String) request.getParameter("message");
            }
            if (request.getAttribute("suBean") != null) {
                sBean = (SearchUserBean) request.getAttribute("suBean");
                role.setMatch(sBean.getMatch());
            }

            if (request.getAttribute("buttonValue") != null) {
                buttonValue = (String) request.getAttribute("buttonValue");

            }
            if (session.getAttribute("roleList") != null) {
                roleList = (List) session.getAttribute("roleList");
            }

            if (request.getParameter("programid") != null && session.getAttribute("processinfoforrole") == null) {

                buttonValue = "searchall";
            }
            if (buttonValue.equals("searchall")) {
                roleList = dbUtil.getAllRole();
                session.setAttribute("roleList", roleList);
                if (request.getParameter("programid") != null) {
                    session.setAttribute("processinfoforrole", request.getParameter("programid"));
                }

                session.setAttribute("roleListCaption", "RoleList {All Records}");
            }
            if (request.getParameter("modify") != null) {
                session.setAttribute("modifyforrole", request.getParameter("modify"));
            } else {
                modify = (String) session.getAttribute("modifyforrole");

            }

            request.setAttribute("role", role);
// Name:Rohith Date:12/01/2007(mm/dd/yy) setting the path to searchRole action
            String editPath = path + "/searchRole.do";




%>
<html> 
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <base href="<%=basePath%>">
        <title>Search Role</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script language="javascript" type="text/javascript">
  
            function searchform()
            {
                if(document.searchRole.txtCal.value!="")
                {
                    if(isValidDate(document.searchRole.txtCal.value)==false)
                    {

                        document.searchRole.txtCal.value="";
                        document.searchRole.txtCal.focus();
                        return;
                    }
                }
                if(document.searchRole.roleName.value=="")
                {
                    if(document.searchRole.txtCal.value=="")
                    {
                        alert("Please Enter either the Role Name or Role Created Date");
                        document.searchRole.txtCal.value="";
                        document.searchRole.txtCal.focus();
     
                        return;
                    }
                }
                if(document.searchRole.roleName.value.match(" "))
                {
                    alert("Space is not allowed for Role Name");
                    return;
                }
         
                document.searchRole.buttonValue.value="search";
                document.searchRole.submit();
            }
            function searchallform()
            {
                document.searchRole.buttonValue.value="searchall";
                document.searchRole.submit();
            }
            function confirmdelete(obj)
            {
    
                var rowindex=obj.parentNode.parentNode.rowIndex;
                //alert(rowindex);
                var x=document.getElementById('roletable').rows[rowindex].cells;
                document.searchRole.name.value=x[0].innerHTML;
                //alert(obj.name);
                document.searchRole.index.value=rowindex-1;
                document.searchRole.buttonValue.value="delete";
    
                //var result = confirm("Are you sure you want to delete this Role "+x[0].innerHTML);
                var result = confirm("Are you sure you want to delete this Role "+x[0].innerHTML);
                if(result)
                {
                    document.searchRole.submit();
                }
            }

            function viewForm(obj)
            {
 
                document.searchRole.index.value=obj.name;
                document.searchRole.buttonValue.value="view";
                document.searchRole.submit();
            }


            function editForm(obj)
            {
                var rowindex=obj.parentNode.parentNode.rowIndex;
                var x=document.getElementById('roletable').rows[rowindex].cells;
                document.searchRole.name.value=x[0].innerHTML;
                document.searchRole.index.value=obj.name;
                document.searchRole.buttonValue.value="edit";
                document.searchRole.submit();
            }


            function disabled(val)
            {

                if(val == 0)
                {
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++)
                    {
                        if(imgs[k].id != "showall" && imgs[k].id!="search")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                }
                setRoleStyle();
                //displaytagcolor();
                //initRowHighlighting();
   
                if(val=="popup"){
                    window.location.href="<%=path%>/jsps/admin/NewRole.jsp";
                }
            }
            function setRoleStyle()
            {
		
			
                if(document.searchRole.buttonValue.value="searchall")
                {
                    var x=document.getElementById('roletable').rows[1].cells;
		 		  
                    x[1].className="sortable sorted order1";
		     
                }
		  
                if(document.searchRole.buttonValue.value=="search")
                {
		  	
                    var input = document.getElementsByTagName("input");
		
                    if(!input[0].value=="")
                    {
                        var x=document.getElementById('roletable').rows[1].cells;
                        x[1].className="sortable sorted order1";
                    }
                    else if(!input[1].value=="")
                    {
                        var x=document.getElementById('roletable').rows[2].cells;
                        x[2].className="sortable sorted order1";
                    }
	  		   			
                }
            }
 
            function displaytagcolor()
            {
                var datatableobj = document.getElementById('roletable');
				
                for(i=0; i<datatableobj.rows.length; i++)
                {
                    var tablerowobj = datatableobj.rows[i];
                    if(i%2==0)
                    {
                        tablerowobj.bgColor='#FFFFFF';
                    }
                    else
                    {
                        tablerowobj.bgColor='#E6F2FF';
                    }
                }
            }
		 
            function initRowHighlighting()
            {
                if (!document.getElementById('roletable')){ return; }
                var tables = document.getElementById('roletable');
                attachRowMouseEvents(tables.rows);
            }

            function attachRowMouseEvents(rows)
            {
		
                for(var i =1; i < rows.length; i++)
                {
                    var row = rows[i];
                    row.onmouseover =	function()
                    {
                        this.className = 'rowin';
                    }
                    row.onmouseout =	function()
                    {
                        this.className = '';
                    }
                    row.onclick= function()
                    {
                    }
                }
		
            }
		
            var newwindow = '';
            function addform() {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/admin/roleCode.jsp";
                }
                else
                {
                    newwindow=window.open("<%=path%>/jsps/admin/roleCode.jsp","","width=400,height=100");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function getAddRole(){
                window.location.href="<%=path%>/jsps/admin/NewRole.jsp";
            }
        </script>
    </head>
    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>')" >
        <html:form action="/searchRole" name="searchRole" type="com.gp.cong.logisoft.struts.form.SearchRoleForm" scope="request">
            <font color="blue"><h4><%=msg%></h4></font>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Search Criteria</td></tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr><td colspan="8"></td></tr>
                            <tr>
                                <td  id="labelText">&nbsp;&nbsp;Role Name</td>
                                <td>
                                    <html:text property="roleName" styleClass="textlabelsBoldForTextBox width-145px"
                                    value="<%=sBean.getRoleName()%>" onkeyup="toUppercase(this)"/>
                                </td>
                                <td  id="labelText">Role Created On</td>
                                <td>
                                    <html:text property="txtCal" styleId="txtcal" value="<%=sBean.getTxtCal()%>"
                                               styleClass="textlabelsBoldForTextBox width-145px" ></html:text>
                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" style="vertical-align: middle;"
                                         id="cal" onmousedown="insertDateFromCalendar(this.id,0);" />
                                </td>
                                <td  id="labelText">Match Only</td>
                                <td ><html:radio  property="match" value="match" name="role" ></html:radio></td>
                                <td  id="labelText">Start list at</td>
                                <td><html:radio  property="match" value="starts" name="role"></html:radio></td>
                            </tr>
                            <tr><td colspan="8"></td></tr>
                            <tr><td colspan="8"></td></tr>
                            <tr>
                                <%--    <td align="right"><img src="<%=path%>/img/search.gif"  id="search" border="0" onclick="searchform()" --%>
                                <%--              	   style="cursor: pointer; cursor: hand;"/>--%>
                                <%--	<img src="<%=path%>/img/showall.gif"  border="0" onclick="searchallform()" id="showall" />--%>
                                <%--    <img src="<%=path%>/img/addnew.gif" border="0" onclick="addform()"/></td>--%>
                                <td colspan="3"></td>
                                <td colspan="2">
                                    <input type="button" onclick="searchform()" class="buttonStyleNew" name="search" value="Search"/>
                                    <input type="button" onclick="searchallform()" class="buttonStyleNew" name="showall" value="ShowAll"/>
                                    <%--	    <input type="button" value="AddNew" onclick="window.location.href = '<%=path%>/jsps/AccountsRecievable/Newbatch.jsp'" /> --%>
                                    <input type="button" onclick="return GB_show('Role', '<%=path%>/jsps/admin/roleCode.jsp?button='+'admin',150,600)"  class="buttonStyleNew"   name="AddNew" value="Add New">
                                    <%--                  onclick="return GB_show('Role', '<%=path%>/jsps/admin/roleCode.jsp?button='+'admin',150,600)"--%>
                                </td>
                                <td colspan="3">
                                </td>


                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <table>
                <tr><td>&nbsp;</td></tr>
            </table>

            <table width="100%" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Search Results</td></tr>
                <%--        <td  class="headerbluesmall">&nbsp;&nbsp;<%=session.getAttribute("roleListCaption") %>  </td>--%>

                <%


                            if (!buttonValue.equals("load") || roleList != null) {

                %>
                <tr>
                    <td>
                        <div id="divtablesty1">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <display:table name="<%=roleList%>" pagesize="<%=pageSize%>"
                                               class="dataTable" id="roletable" sort="list" style="width:100%">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Role Details Displayed,For more Role click on Page Numbers.
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
                                    <display:setProperty name="paging.banner.item_name" value="Role"/>
                                    <display:setProperty name="paging.banner.items_name" value="Roles"/>
                                    <display:column property="roleDesc" href="<%=editPath%>"
                                                    paramId="paramid" paramProperty="roleId" title="Role Name"  sortable="true" />
                                    <display:column property="roleCreatedDate" title="Role Created On" sortable="true" />
                                    <display:column property="updatedBy" title="Updated By" sortable="true" />
                                    <display:column property="updatedDate" title="Updated On" sortable="true" />
                                    <display:column  title="Actions">
                                        <img src="${contextPath}/img/icons/pubserv.gif" alt="moreInfoIcon" class="hotspot"
                                             title="MoreInfo" onclick="moreInfoPage('${roletable.roleId}')" />
                                        &nbsp;&nbsp;
                                        <img src="${contextPath}/img/icons/editrole.gif" alt="roleDutyIcon"
                                             onclick="editRoleDuty('${roletable.roleId}', '${roletable.roleDesc}')"
                                             title="Role Duty"/>
                                    </display:column>

                                </display:table>
                            </table></div></td>
                </tr>
                <%
                            }
                %>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
            <html:hidden property="index" styleId="index" />
            <html:hidden property="msg"/>
            <html:hidden property="name"/>
            <html:hidden property="rolName" value="<%=rolName%>"/>
            <script type="text/javascript">
                $(document).ready(function() {
                    jQuery("[title != '']").not("link").tooltip();
                });
                function editRoleDuty(roleId, roleName) {
                    document.searchRole.action = '${path}/editRoleDuties.do?action=edit&roleId='+roleId+'&roleName='+roleName;
                    document.searchRole.submit();

                }
                function moreInfoPage(val){
                    document.searchRole.action = '${path}/searchRole.do?param='+val;
                    document.searchRole.submit();
                }
            </script>
        </html:form>
    </body>
</html>

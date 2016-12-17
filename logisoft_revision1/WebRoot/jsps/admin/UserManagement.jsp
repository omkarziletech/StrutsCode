<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.SearchUserBean,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/baseResourcesForJS.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <!-- <base href="<%=basePath%>"> 

        <title>User Management</title> -->

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">

        <link rel="stylesheet" type="text/css" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua" />
        <link rel="alternate stylesheet" type="text/css" media="all"
              href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css">
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>

        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <%@include file="../../jsps/preloader.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $("[title != '']").not("link").tooltip();
            });
            function searchform(){
                if(document.userManagement.txtCal.value!=""){
                    if(isValidDate(document.userManagement.txtCal.value)==false){
                        document.userManagement.txtCal.value="";
                        document.userManagement.txtCal.focus();
                        return false;
                    }                  
                }
                if($('#firstName').val()=="" && $('#loginName').val()==""
                    && $('#lastName').val()=="" && $('#userStatus').val()==""
                    && $('#roleName').val()==""  && $('#txtcal').val()==""){
                    $.prompt("Please Enter Any Search Criteria");
                    return false;
                }
                submitForm();
            }
            function searchLimit(){
                submitForm();
            }
            function doSortAscDec(sortByValue) {
                var sortBy = $("#sortBy").val();
                var toggleValue = sortBy === "up" ? "down" : "up";
                $("." + sortByValue).removeClass(sortBy).addClass(toggleValue);
                $("#sortBy").val(toggleValue);
                $("#columnName").val(sortByValue);
                submitForm();
            }
            function submitForm(){
                showLoading();
                document.userManagement.buttonValue.value="search";
                document.userManagement.submit();
            }
            function resetAllFields(){
                $('#firstName').val('');
                $('#loginName').val('');
                $('#lastName').val('');
                $('#userStatus').val('');
                $('#roleName').val('');
                $('#txtcal').val('');
                $("#sortBy").val('');
                $("#columnName").val('');
                submitForm();
            }
            function searchallform()
            {
                document.userManagement.buttonValue.value="";
                document.userManagement.submit();
            }
            function confirmdelete(obj,val)
            {

                var rowindex=obj.parentNode.parentNode.rowIndex;
                var x=document.getElementById('usertable').rows[rowindex].cells;

                var y=document.userManagement.login.value;
                document.userManagement.name.value=x[0].innerHTML;
                document.userManagement.index.value=obj.name;
                document.userManagement.buttonValue.value="delete";
                alert(x[0].innerHTML);
                if(x[0].innerHTML==y)
                {
                    alert("This user cannot be deleted");
                    return;
                }
                var result = confirm("Are you sure you want to delete this user "+x[0].innerHTML);
                if(result)
                {
                    document.userManagement.submit();
                }

            }
            function editForm(obj)
            {
                var rowindex=obj.parentNode.parentNode.rowIndex;
                var x=document.getElementById('usertable').rows[rowindex].cells;
                document.userManagement.name.value=x[0].innerHTML;
                document.userManagement.index.value=obj.name;
                document.userManagement.buttonValue.value="edit";
                document.userManagement.submit();
            }

            function viewForm(obj)
            {

                document.userManagement.index.value=obj.name;
                document.userManagement.buttonValue.value="view";
                document.userManagement.submit();
            }

            var newwindow = '';
            function addform() {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/admin/userCode.jsp";
                }
                else
                {
                    newwindow=window.open("<%=path%>/jsps/admin/userCode.jsp","","width=400,height=100");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }

            function disabled(val)
            {
                if(val == 0)
                {
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++)
                    {
                        if(imgs[k].id != "cal" && imgs[k].id != "showall" && imgs[k].id!="search")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }

                }
                //displaytagcolor();
                //initRowHighlighting();
                setWarehouseStyle();
            }
            function setWarehouseStyle()
            {

                if(document.userManagement.buttonValue.value=="searchall")
                {
                    var x=document.getElementById('usertable').rows[0].cells;

                    x[0].className="sortable sorted order1";
                }
                if(document.userManagement.buttonValue.value=="search")
                {
                    var input = document.getElementsByTagName("input");
                    var select = document.getElementsByTagName("select");
                    if(!input[0].value=="")
                    {

                        var x=document.getElementById('usertable').rows[0].cells;
                        x[0].className="sortable sorted order1";
                    }
                    else if(!input[1].value=="")
                    {

                        var x=document.getElementById('usertable').rows[0].cells;
                        x[5].className="sortable sorted order1";
                    }


                    else if(!input[4].value=="0")
                    {
                        var x=document.getElementById('usertable').rows[0].cells;
                        x[1].className="sortable sorted order1";
                    }
                    else if(!input[5].value=="0")
                    {
                        var x=document.getElementById('usertable').rows[0].cells;
                        x[2].className="sortable sorted order1";
                    }
                }
            }
            function displaytagcolor()
            {
                var datatableobj = document.getElementById('usertable');
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
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function initRowHighlighting()
            {
                if (!document.getElementById('usertable')){ return; }
                var tables = document.getElementById('usertable');
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
            function searchallform()
            {
                document.userManagement.buttonValue.value="searchall";
                document.userManagement.submit();
            }
            function getUserPage(){
                window.location.href="<%=path%>/jsps/admin/NewUser.jsp";
            }
            function moreInfo(val){
                document.userManagement.buttonValue.value="moreinfo";
                document.userManagement.param.value = val;
                document.userManagement.submit();
            }
            function editUser(val){
                document.userManagement.buttonValue.value="edit";
                document.userManagement.param.value = val;
                document.userManagement.submit();
            }
        </script>

    </head>
    <body class="whitebackgrnd">
        <html:form action="/userManagement" name="userManagement"
                   type="com.gp.cong.logisoft.struts.form.UserManagementForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="4">Search Criteria</td></tr>
                <tr>
                    <td colspan="4">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr><td colspan="4"></td></tr>

                            <tr>
                                <td   id="labelText"><bean:message key="form.newUserForm.loginName" /></td>
                                <td  valign="middle">
                                    <html:text property="loginName" styleId="loginName" styleClass="textlabelsBoldForTextBox width-145px"
                                               value="${suBean.loginName}"/></td>
                                <td   id="labelText"><bean:message key="form.newUserForm.usercreated" /></td>
                                <td>
                                    <html:text property="txtCal" styleId="txtcal" styleClass="textlabelsBoldForTextBox width-145px"
                                               value="${suBean.txtCal}"/>
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal" width="16" height="16"
                                         id="cal" style="vertical-align: middle;"
                                         onmousedown="insertDateFromCalendar(this.id,0);"/>
                                </td>
                            </tr>
                            <tr>
                                <td  id="labelText"><bean:message key="form.newUserForm.firstName" /></td>
                                <td   valign="middle">
                                    <html:text styleId="firstName" styleClass="textlabelsBoldForTextBox width-145px"
                                               property="firstName" value="${suBean.firstName}" /></td>
                                <td   id="labelText">
                                    <bean:message key="form.newUserForm.lastName" /></td>
                                <td  valign="middle">
                                    <html:text property="lastName" styleId="lastName" styleClass="textlabelsBoldForTextBox width-145px"
                                               value="${suBean.lastName}" />
                                </td>

                            </tr>
                            <tr>
                                <td id="labelText">Status</td>
                                <td>
                                    <html:select property="userStatus" styleId="userStatus" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                                 value="${suBean.userStatus}"  onchange="submitForm()">
                                        <html:option value="">Select</html:option>
                                        <html:optionsCollection name="statuslist"/>
                                    </html:select>
                                </td>
                                <td  id="labelText"><bean:message key="form.newUserForm.roleName" /></td>
                                <td  valign="middle">

                                    <html:select property="roleName" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                                 styleId="roleName" value="${suBean.roleName}" onchange="submitForm()">
                                        <html:option value="">Select</html:option>
                                        <html:optionsCollection name="rolelist" styleClass="areahighlightyellow"/>
                                    </html:select>

                            </tr>
                            <tr>
                                <td colspan="4" align="center">
                                    <input type="button" class="buttonStyleNew" onclick="searchform()" name="search" value="Search"/>
                                    <input type="button" class="buttonStyleNew" value="Add New"
                                           onclick="return GB_show('User', '<%=path%>/jsps/admin/userCode.jsp?relay='+'add',150,600)" />
                                    <input type="button" class="buttonStyleNew" onclick="resetAllFields()" name="reset" value="Reset"/>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
            </table>
            <br>
            <table width="100%" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Search Results</td>
                    <td align="right" style="width:10%" class="blackBoldArempty">Limit
                        <html:select property="limit" styleId="limit" styleClass="dropdown"
                                     onchange="searchLimit()" value="${suBean.limit}">
                            <html:option value="100" >100</html:option>
                            <html:option value="250" >250</html:option>
                            <html:option value="500" >500</html:option>
                            <html:option value="1000" >1000</html:option>
                        </html:select>
                    </td>
                </tr>

                <tr><td colspan="2">
                        <table  border="0" cellpadding="0" cellspacing="0">
                            <table class="dataTable" border="0" id="userTable">
                                <thead>
                                    <tr>
                                        <th class="${suBean.columnName eq 'login_name' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('login_name');">
                                                <span style="color: black;">Login Name</span>
                                            </a>
                                        </th>
                                        <th class="${suBean.columnName eq 'first_name' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('first_name');">
                                                <span style="color: black;">First Name</span>
                                            </a>
                                        </th>
                                        <th class="${suBean.columnName eq 'last_name' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('last_name');">
                                                <span style="color: black;">Last Name</span>
                                            </a>
                                        </th>
                                        <th class="${suBean.columnName eq 'role' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('role');">
                                                <span style="color: black;">Role Name</span>
                                            </a>
                                        </th>
                                        <th>Telephone</th>
                                        <th>Email</th>
                                        <th>Created Date</th>
                                        <th class="${suBean.columnName eq 'last_login_date' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('last_login_date');">
                                                <span style="color: black;"> Last Login Date</span>
                                            </a>
                                        </th>
                                        <th class="${suBean.columnName eq 'status' ? suBean.sortBy : ''}">
                                            <a href="javascript:doSortAscDec('status');">
                                                <span style="color: black;">Status</span>
                                            </a>
                                        </th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${userList}">
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>
                                                <u>
                                                    <span style="height: 10px;cursor: pointer;color:blue " class="link"
                                                          onclick="editUser('${user.userId}','${user.loginName}')">
                                                        ${user.loginName}
                                                    </span>
                                                </u>
                                            </td>
                                            <td>${user.firstName}</td>
                                            <td>${user.lastName}</td>
                                            <td>${user.roleName}</td>
                                            <td>${user.telephone}</td>
                                            <td>${user.email ne 'null' ? user.email : ''}</td>
                                            <td>${user.createdBy}</td>
                                            <td>${user.lastLogin}</td>
                                            <td>${user.status}</td>
                                            <td>
                                                <img src="${path}/img/icons/pubserv.gif" title="MoreInfo" alt=""
                                                     border="0" onclick="moreInfo('${user.userId}');" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" value=""/>
            <html:hidden property="index" styleId="index" />
            <html:hidden property="login" value=""/>
            <html:hidden property="message" value=""/>
            <html:hidden property="name"/>
            <html:hidden property="logName" value=""/>
            <html:hidden property="param"/>
            <html:hidden property="sortBy" styleId="sortBy" value="${suBean.sortBy}"/>
            <html:hidden property="columnName" styleId="columnName" value="${suBean.columnName}"/>
        </html:form>
    </body>
</html>

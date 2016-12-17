<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
String search1="";
String path1="";
List search=new ArrayList();

if(request.getParameter("button")!=null)
{
if(session.getAttribute("userList")!=null)
{
session.removeAttribute("userList");
}
search1=(String)request.getParameter("button");
session.setAttribute("search1",search1);
}
if(session.getAttribute("userList")!=null)
{
search=(List)session.getAttribute("userList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>

		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
<%}
	

String editPath=path+"/userPopUp.do";

%>
<html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for City selectionform</title>
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	function search1()
	{
	if(document.userPopUpForm.loginName.value=="" && document.userPopUpForm.lastName.value=="")
	{
	alert("Please Enter the any Search Criteria");
	return;
	}
	
		document.userPopUpForm.buttonValue.value="search"
  	    	document.userPopUpForm.submit();
	}
	</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/userPopUp" name="userPopUpForm" type="com.gp.cong.logisoft.struts.form.UserPopUpForm" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="tableBorderNew">
    

     <tr class="tableHeadingNew">Search Criteria
  </tr>
  
	 
	<tr >
      <td class="textlabelsBold">First Name</td>
	  <td>
	   <html:text property="loginName" styleClass="textlabelsBoldForTextBox"  value="" size="8"/>
	  </td>
	  <td class="textlabelsBold">Last Name</td>
	  <td><html:text property="lastName" styleClass="textlabelsBoldForTextBox" value="" size="8"/>
	  <br></td>
	  <td>
	  <input type="button" class="buttonStyleNew" onclick="search1()" value="Go"><%--
	  <img src="<%=path%>/img/search1.gif" onclick="search1()" />
	  --%></td>
	  </tr>
	  </table>
<br>
	  

  <%
  int i=0;
if((search!=null)) 
{
%>
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260%;">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
     <tr class="tableHeadingNew"> List Of Managers </tr>
<tr>
<td>
<display:table name="<%=search%>" pagesize="<%=pageSize%>" class="displaytagstyle">
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Ports Details displayed,For more Ports click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Ports"/>
  			<display:setProperty name="paging.banner.items_name" value="Ports"/>
    				
  			<%
  			String loginname="";
  			if(search!=null && search.size()>0)
  			{
  			User user=(User)search.get(i);
  			loginname=user.getFirstName();
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="First Name"><a href="<%=tempPath%>"><%=loginname%></a></display:column>
		 <display:column title="Last Name" property="lastName"></display:column>
	<%i++; %>
</display:table>  
</td></tr>
  </table>
  </div>
  
<%}%>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
		
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


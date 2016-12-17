<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cvst.logisoft.domain.AccountDetails"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String search1="";
String path1="";
String itemNo="";
if(session.getAttribute("buttonValue")!=null)
{
session.removeAttribute("buttonValue");
}

if(request.getParameter("itemNo")!=null)
{
itemNo=(String)request.getParameter("itemNo");
session.setAttribute("itemNo",itemNo);
}
if(session.getAttribute("search")!=null)
{
session.removeAttribute("search");
}
List accountList=new ArrayList();


if(request.getParameter("linebutton")!=null)
{
if(session.getAttribute("accountList")!=null)
{
session.removeAttribute("accountList");
}
search1=(String)request.getParameter("linebutton");
session.setAttribute("search1",search1);
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
if(session.getAttribute("accountList")!=null)
{
accountList=(List)session.getAttribute("accountList");
}
String editPath=path+"/accountPopUp.do";
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>

		self.close();
		
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
 %>
<html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for City selectionform</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	<%@include file="../includes/baseResources.jsp" %>
	<script type="text/javascript">
	function search1()
	{
		document.accountPopUpForm.buttonValue.value="search"
  	    	document.accountPopUpForm.submit();
	}
	</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/accountPopUp" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  </tr>
  
	 
	<tr >
      <td class="textlabels">Account</td>
	  <td>
	   <html:text property="account" size="3"/>
	  </td>
	  <td class="textlabels">Account Description</td>
	  <td><html:text property="acctDesc" />
	  </td>
	  
	  <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  </tr>
	  </table>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
  

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  </tr>
</table>

  <%
  int i=0;
if(accountList!=null) 
{

%>
<div id="divtablesty1" class="scrolldisplaytable">
<display:table name="<%=accountList%>" pagesize="<%=pageSize%>" class="displaytagstyle">
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> AccountDetails Details displayed,For more AccountDetails click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="AccountDetails"/>
  			<display:setProperty name="paging.banner.items_name" value="AccountDetailss"/>
    		<%
    		String account="";
    		if(accountList!=null && accountList.size()>0)
    		{
    		AccountDetails acount=(AccountDetails)accountList.get(i);
    		if(acount!=null)
    		{
    		account=acount.getAccount();
    		}
    		}
    		String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
    		 %>
		 <display:column title="Account No"><a href="<%=tempPath%>"><%=account%></a></display:column>

	<display:column property="acctDesc" title="Account Description" ></display:column>
	
	<%i++; %>
</display:table>  
  </div>
  
<%}%>
	  
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


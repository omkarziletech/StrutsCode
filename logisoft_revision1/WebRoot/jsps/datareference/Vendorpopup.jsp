<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ page import="com.gp.cong.logisoft.beans.*,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerTemp" %>
<%@ page import="org.apache.struts.util.LabelValueBean" %>
<%@ page import="java.util.*" %>
<%
String search="";
String path1="";
List vendorList=new ArrayList();


if(request.getParameter("button")!=null)
{
if(session.getAttribute("customerList")!=null)
{
	session.removeAttribute("customerList");
}
search=(String)request.getParameter("button");
session.setAttribute("search1",search);
}
if(session.getAttribute("customerList")!=null)
{
vendorList=(List)session.getAttribute("customerList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
 String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("buttonValue")!=null  && request.getAttribute("buttonValue").equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
	</script>
	<%}

DBUtil dbUtil=new DBUtil();

  
  
String editPath=path+"/vendorpopup.do";
CitySelectionBean cityBean=null;
%>
 
<html:html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for VendorpopupForm form</title>
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	 
		
         function search1()
         { 
         if(document.vendorpopupForm.accountNo.value=="")
	{
	 alert("Please Enter Account No");
        return;
        }
                document.vendorpopupForm.buttonValue.value="search"
  	        	     document.vendorpopupForm.submit();
  	     }
  	         	    
	</script>
	
	</head>
	<body class="whitebackgrnd">
		<html:form action="/vendorpopup" name="vendorpopupForm" type="com.gp.cong.logisoft.struts.form.VendorpopupForm" scope="request">
		<html:hidden property="buttonValue"/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" height="46" class="tableBorderNew">
    

      <tr class="tableHeadingNew">Search Criteria
  </tr>
  
	<tr >
      <td class="textlabels">Account NO </td>
	  <td>
	   <html:text property="accountNo" styleClass="areahighlightwhite" value="" maxlength="20"/>
	  </td><td valign="middle"><img src="<%=path%>/img/search1.gif" onclick="search1()"></td>
	  <td class="textlabels">&nbsp;</td>
	   <td></td>
	  </tr>
	  </table>
	  
<br>

  <%
  int i=0;
if((vendorList!=null && vendorList.size()>0)) 
{
%>
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260px;">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
     <tr class="tableHeadingNew"> Vendor Lists </tr>
<tr>
<td>
<display:table name="<%=vendorList%>" pagesize="<%=pageSize%>" class="displaytagstyle">
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> City Details displayed,For more code click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/>
  			<%
  			String accountNo="";
  			if(vendorList!=null && vendorList.size()>0)
  			{
  			CustomerTemp c1=(CustomerTemp)vendorList.get(i);
  			accountNo=c1.getAccountNo();
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
                 

  			%>
		 <display:column   title="Account No"><a href="<%=tempPath%>"><%=accountNo%></a></display:column>

	
	<%i++; %>
</display:table>  
</td></tr></table>
  </div>
  
<%}%>
		
			
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>


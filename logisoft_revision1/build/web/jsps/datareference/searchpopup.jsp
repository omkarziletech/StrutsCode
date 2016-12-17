<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.UnLocation" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>

<%
String search="";
String path1="";
String warehcity="";
warehcity = request.getParameter("cityname");

if(request.getParameter("button")!=null)
{
List lList=(List)session.getAttribute("cityDetails");
if(session.getAttribute("cityDetails")!=null)
{
session.removeAttribute("cityDetails");
}
if(lList!=null &&lList.size()>0)
{
lList.clear();
}

search=(String)request.getParameter("button");
session.setAttribute("search1",search);
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
List cityDetails=new ArrayList();
if(session.getAttribute("cityDetails")!=null)
{
cityDetails=(List)session.getAttribute("cityDetails");
} 
  
String editPath=path+"/Searchcity.do";
UnLocation cityBean=null;
%>
<html:html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for City selectionform</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
	<%@include file="../includes/baseResources.jsp" %>
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	 
		function cityload()
		{  
		 if(document.newTerminalForm.city.value=="<%=warehcity%>")
		  {
		  //  alert(document.newTerminalForm.city.value);
		   document.newTerminalForm.buttonValue.value="search"
		     document.newTerminalForm.submit();
  	      
		  } 
		 
		}
		
         function search1()
         { 
         if(document.newTerminalForm.city.value=="")
         {
         alert("Please enter the city");
         return;
         }
                document.newTerminalForm.buttonValue.value="search"
  	        	     document.newTerminalForm.submit();
  	     }
  	         
	</script>
	
</head>

<body class="whitebackgrnd" onload="cityload()">
<html:form action="/Searchcity" name="newTerminalForm" type="com.gp.cong.logisoft.struts.form.NewTerminalForm" scope="request">
<html:hidden property="buttonValue"/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" height="46" class="tableBorderNew">
    

     <tr class="tableHeadingNew">Search Criteria
  </tr>
	<tr >
      <td class="textlabelsBold">City Name </td>
	  <td>
	   <html:text property="city" styleClass="textlabelsBoldForTextBox" value="<%=warehcity %>" maxlength="20"/>
	   <% %>
	  </td><td valign="middle">
	  <input type="button" class="buttonStyleNew" onclick="search1()" value="Go"><%--
	  <img src="<%=path%>/img/search1.gif" onclick="search1()"></td>
	  --%><td class="textlabelsBold">&nbsp;</td>
	   <td></td>
	  </tr>
	  </table>
	 <br>
  <%
  int i=0;
if((cityDetails!=null && cityDetails.size()>0)) 
{
%>
<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:260px;">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
     <tr class="tableHeadingNew"> List Of Cities </tr>
<tr>
<td>
<display:table name="<%=cityDetails%>" pagesize="<%=pageSize%>" class="displaytagstyleNew">
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
  			<%String iStr=String.valueOf(i);
  			String country="";
  			String state="";
  			String tempPath=editPath+"?index="+iStr;
                  cityBean=(UnLocation)cityDetails.get(i);
                  if(cityBean.getCountryId()!=null && cityBean.getCountryId().getCodedesc()!=null)
                  {
 			country=cityBean.getCountryId().getCodedesc();
 			}
 			if(cityBean.getStateId()!=null && cityBean.getStateId().getCode()!=null)
 			{
 			state=cityBean.getStateId().getCode();
 			}
 			
  			%>
		 <display:column   title="City"><a href="<%=tempPath%>" ><%=cityBean.getUnLocationName()%> </a></display:column>

	<display:column href="" title="Country" ><%=country%></display:column>
	<display:column title="State"  maxLength="15"><%=state%></display:column>
	<%i++; %>
</display:table>  
  </td></tr></table>
  </div>
  
<%}%>
 
 
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>

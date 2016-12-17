<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.PortsTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
List search=new ArrayList();
String search1="";
String path1="";
String percode="";   
String percodename="";

if(request.getParameter("percode")!=null){
percode=request.getParameter("percode");
}
if(request.getParameter("percodename")!=null){
percodename=request.getParameter("percodename");
}


if(session.getAttribute("pierList")!=null)
{
search=(List)session.getAttribute("pierList");
}

if(request.getParameter("button")!=null)
{
search1=(String)request.getParameter("button");
session.setAttribute("search1",search1);
}
String nameno="";
if(search!=null && search.size()>0&& search.size()==1)
  			{
  			PortsTemp p=(PortsTemp)search.get(0);
  			if(p.getShedulenumber()!=null)
  			{
  			nameno=p.getShedulenumber();
  			}
  			
  			
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
	
DBUtil dbUtil=new DBUtil();
String editPath=path+"/searchPierCode.do";

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
	 
		function portLoad(val1,val2,val3)
		{  
		 if(val1!=""){
		 if(document.searchPierCodeForm.pierCode.value=="<%=percode%>")
		  {
		   
		   
		   document.searchPierCodeForm.buttonValue.value="search"
  	    	document.searchPierCodeForm.submit();
  	      
		  } 
		 }
		 if(val3!=""){
		 if(document.searchPierCodeForm.portName.value=="<%=percodename%>")
		  {
		   
		   
		   document.searchPierCodeForm.buttonValue.value="search"
  	    	document.searchPierCodeForm.submit();
  	      
		  } 
		 }
		 if(val2!=""){
		
		   document.searchPierCodeForm.index.value="0";
  	    	document.searchPierCodeForm.submit();
  	      
		  } 
		 
		}
         function searchcriteria()
         { 
         if(document.searchPierCodeForm.pierCode.value=="" && document.searchPierCodeForm.portName.value=="")
         {
         alert("Please Enter any search Criteria");
         return;
         }
          	document.searchPierCodeForm.buttonValue.value="search"
  	    	document.searchPierCodeForm.submit();
  	     }
  	         	    
	</script>
	
</head>

<body class="whitebackgrnd" onload="portLoad('<%=percode%>','<%=nameno%>','<%=percodename%>')">
		<html:form action="/searchPierCode" name="searchPierCodeForm" type="com.gp.cong.logisoft.struts.form.SearchPierCodeForm" scope="request">
			<html:hidden property="buttonValue"/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    

     <tr class="tableHeadingNew">
     Search Criteria
  </tr>
  
	 
	<tr >
      <td class="textlabelsBold">POL</td>
	  <td>
	   <html:text property="pierCode" styleClass="textlabelsBoldForTextBox"  value="<%=percode%>" size="3"/>
	  </td>
	  <td class="textlabelsBold">POL Name</td>
	  <td><html:text property="portName" styleClass="textlabelsBoldForTextBox" value="<%=percodename %>"  />
	  </td>
	  
	  <td>
	  <input type="button" class="buttonStyleNew" value="Go" onclick="searchcriteria()"><%--
	  <img src="<%=path%>/img/search1.gif" onclick="searchcriteria()" />
	  --%></td>
	  </tr>
	  </table>
	 
  <br>

  <%
  int i=0;
if((search!=null)) 
{
  		 if( request.getParameter("percode") != "")
		 {
		
%>
<div id="divtablesty1" class="scrolldisplaytable">
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
  <tr class="tableHeadingNew">
     Lisy of POL
  </tr><tr><td>
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
  			String piercode="";
  			if(search!=null && search.size()>0)
  			{
  			PortsTemp p=(PortsTemp)search.get(i);
  			if(p.getShedulenumber()!=null)
  			{
  			piercode=p.getShedulenumber();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Schedule Code"><a href="<%=tempPath%>"><%=piercode%></a></display:column>

	<display:column property="portname" title="City Name" ></display:column>
	<display:column property="controlNo" title="Control Number"></display:column>
	<%i++; %>
</display:table>  
</td></tr></table>
  </div>
  
<%}
else
{ 
%>
<display:table>
  	<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
				</span>	
				</display:setProperty>
				</display:table>

<%} }%>

	<html:hidden property="index" />
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>


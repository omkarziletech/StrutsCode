<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<% 
List search=new ArrayList();
String searchCode="";
String path1="";
String comcode="";
String coName="";
if(request.getParameter("comcode")!=null){
comcode=request.getParameter("comcode");
}
if(request.getParameter("comcodename")!=null){
coName=request.getParameter("comcodename");

}

if(request.getParameter("button")!=null)
{
if(session.getAttribute("codeList")!=null)
{
	session.removeAttribute("codeList");
}
searchCode=(String)request.getParameter("button");

session.setAttribute("searchCode",searchCode);
}
if(session.getAttribute("codeList")!=null)
{
search=(List)session.getAttribute("codeList");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
	String code1="";
	if(search!=null && search.size()>0 && search.size()==1)
  			{
  			GenericCode g=(GenericCode)search.get(0);
  			if(g.getCode()!=null)
  			{
  			code1=g.getCode();
  			}
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
String editPath=path+"/searchComCode.do";

%>
 
<html> 
	<head>
		<title>JSP for SearchTerminalForm form</title>
		<base href="<%=basePath%>">
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script type="text/javascript">
		function comCode(val1,val2,val3)
		{  
		if(val1!=""){
		 if(document.searchComCodeForm.code.value=="<%=comcode%>")
		  {
		   
		  document.searchComCodeForm.buttonValue.value="search"
  	    	document.searchComCodeForm.submit();
  	      
		  } 
		 }
		 if(val3!=""){
		 if(document.searchComCodeForm.codeDescription.value=="<%=coName%>")
		  {
		   
		  document.searchComCodeForm.buttonValue.value="search"
  	    	document.searchComCodeForm.submit();
  	      
		  } 
		 }
		 if(val2!=""){
		  document.searchComCodeForm.index.value="0";
			document.searchComCodeForm.submit();
		 }
		}
	    function search1()
        { 
        if(document.searchComCodeForm.code.value=="" && document.searchComCodeForm.codeDescription.value=="")
        {
        alert("Please Enter any search Criteria");
        return;
        }
        	document.searchComCodeForm.buttonValue.value="search"
  	    	document.searchComCodeForm.submit();
  	    }
  	    </script>
	</head>
	<body class="whitebackgrnd" onload="comCode('<%=comcode%>','<%=code1%>','<%=coName%>')">
		<html:form action="/searchComCode" name="searchComCodeForm" type="com.gp.cong.logisoft.struts.ratemangement.form.SearchComCodeForm" scope="request">
			<html:hidden property="buttonValue"/>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    	<tr class="tableHeadingNew">Search Criteria
  </tr>
  		<tr >
      		<td class="textlabels">Code</td>
	  		<td><html:text property="code"  value="<%=comcode%>" size="3"/></td>
	  		<td class="textlabels">Code Description</td>
	  		<td><html:text property="codeDescription" value="<%=coName%>"/></td>
	  	    <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  	</tr>
	    </table>
	  	
		<br> 
		<%
  			int i=0;
			if((search!=null)) 
			{
		%>
		<div id="divtablesty1" class="scrolldisplaytable">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
     <tr class="tableHeadingNew"> List Of Com Code  </tr>
<tr>
<td>
		<display:table name="<%=search%>" pagesize="10" class="displaytagstyle">
		
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Code Details displayed,For more Ports click on page numbers.
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
  			String code="";
  			if(search!=null && search.size()>0)
  			{
  			GenericCode g=(GenericCode)search.get(i);
  			if(g.getCode()!=null)
  			{
  			code=g.getCode();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Code"><a href="<%=tempPath%>"><%=code%></a></display:column>
		
		 <display:column property="codedesc" title="Code Description"></display:column>
		 <%i++; %>
		</display:table>  
</td></tr></table>
  		</div>
  	<%}%>
  	  	<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.domain.*" pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
CustAddress p =null;;
List search=new ArrayList();
String search1="";
String path1="";
String parentpage="";
		String Acctname="";
  		String AcctNum=""; 
  		String address="";
String clientName = null;


String buttonvalue=(String)request.getAttribute("buttonValue");
if(request.getParameter("button")!=null)
{
if(session.getAttribute("acctNameList")!=null)
{
  session.removeAttribute("acctNameLis");
}
search1=(String)request.getParameter("button");
session.setAttribute("search",search1);
}
if(request.getParameter("clientName") != null){
 	clientName = request.getParameter("clientName");
}
if(session.getAttribute("acctNameList")!=null)
{
search=(List)session.getAttribute("acctNameList");
}
String customersearch="";
if(request.getParameter("customersearch")!=null)
{
customersearch = (String) request.getParameter("customersearch");

}

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
if(buttonvalue!=null && buttonvalue.equals("completed"))
{

%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
	

String editPath=path+"/Customersearch.do";

  
  
 

 
 
%>

<html:html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for Customer Search form</title>
		<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
</head>

<body class="whitebackgrnd" onload="load('<%=clientName%>')">
		<html:form action="/Customersearch" scope="request">
			<html:hidden property="buttonValue"/>
			 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			 <tr class="tableHeadingNew">Search Criteria</tr>
			 <td>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    
<%----%>
<%--     <tr>--%>
<%--    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> --%>
<%--  </tr>--%>
  
	 
	<tr >
      <td class="textlabels">Customer Name</td>
	  <td>
	   <html:text property="acctName"  value="<%=clientName%>"   onkeyup="toUppercase(this)"/>
	  </td>
	 	  <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  </tr>
	  </table>
	  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    
     <br/>

     <tr class="tableHeadingNew">Search Criteria</tr>
</table>

  <%
  int i=0;
if((search!=null)) 
{
%>
<div id="divtablesty1" class="scrolldisplaytable">
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
					 
				</span>	
				</display:setProperty>
    			
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
    				
  			<%
  			if(search!=null && search.size()>0)
  			{
  			 p=(CustAddress)search.get(i);
  			  if(p.getAcctName()!=null)
  			  {
  			   Acctname=p.getAcctName();
  			  } 
  			if(p.getAcctNo()!=null)
  			{
  			 AcctNum = p.getAcctNo();
  			}
  			if(p.getAddress1()!=null)
  			{
  			  address=p.getAddress1();
  			}  
  	 //+"&acctname="+Acctname;
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			
  			%>
		 <display:column title="Acct Name"><a href="<%=tempPath%>"> <%=Acctname %></a></display:column>

		<display:column  title="Acct Number"> <%=AcctNum%> </display:column>
		<display:column  title="Address"> <%=address%> </display:column>
	<%i++; %>
</display:table>  
  </div>
  
<%}%>
</td>
</table>
</html:form>
</body>
<script type="text/javascript">
	 function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		
         function search1()
         { 
    if(document.CustomerSearchForm.acctName.value=="")
    {
     alert("enter Customer Name");
     document.CustomerSearchForm.acctName.value="";
     document.CustomerSearchForm.acctName.focus();
     return;
    }
    
          document.CustomerSearchForm.buttonValue.value="search"
  	    document.CustomerSearchForm.submit();
  	     }
	 
function load(val)
{
 if(val!="null")
  {
      document.CustomerSearchForm.acctName.value = val;
      document.CustomerSearchForm.buttonValue.value="search"
      document.CustomerSearchForm.submit();
  }
}	 
  	         	    
	</script>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>

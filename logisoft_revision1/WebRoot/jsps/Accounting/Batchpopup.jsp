<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cvst.logisoft.util.DBUtil,java.util.*,com.gp.cvst.logisoft.domain.Batch"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
List search=new ArrayList();
String search1="";
String path1="";
String batchNo="";
if(request.getParameter("batchNo")!=null)
{
batchNo=(String)request.getParameter("batchNo");
}
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
String ab=(String)request.getAttribute("buttonValue");

if(request.getParameter("button")!=null)
{
if(session.getAttribute("batchLst")!=null)
{
session.removeAttribute("batchLst");
}
search1=(String)request.getParameter("button");
session.setAttribute("search",search1);
}
if(session.getAttribute("batchLst")!=null)
{
search=(List)session.getAttribute("batchLst");
}
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(ab!=null && ab.equals("completed"))
{
%>    
<script>
		self.close();
		opener.location.href="<%=path%>/<%=path1%>";
</script>
	<%}
String editPath=path+"/searchBatch.do";

%>
<html:html> 
	<head>
	   <base href="<%=basePath%>">
		<title>JSP for City selectionform</title>
		<%@include file="../includes/baseResources.jsp" %>


	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	 
		
         function search1()
         { 
         if(document.searchBatchForm.batchno.value!="")
         {
         if(IsNumeric(document.searchBatchForm.batchno.value.replace(/ /g,''))==false)
  			 {
  				  alert("BatchNo should be Numeric.");
  				  document.searchBatchForm.batchno.value="";
   				  document.searchBatchForm.batchno.focus();
   				  return;
   			 } 
   			 if(document.searchBatchForm.batchno.value.length>9)
   			 {
   			 alert("batch number should not be more than 9 numbers");
   			   document.searchBatchForm.batchno.value="";
   			   document.searchBatchForm.batchno.focus();
   			 return;
   			 }
         }
          document.searchBatchForm.buttonValue.value="search"
  	    document.searchBatchForm.submit();
  	     }
	 <%-- function accountHistory(obj,val)
	    {
	   var x=document.getElementById('row').rows[++val].cells;
	   //alert("hi3"+x[0].innerHTML);
	   //alert("hi3"+x[1].innerHTML);
	   
	   alert("value in the rowx"+x);
	   document.ChartOfAccountsForm.index.value=val;
	   document.forms[0].buttonValue.value="AcctHistory";
	   document.ChartOfAccountsForm.submit();
	   
	}--%>
  	       function getbatchno()
  	       {
  	        if(document.searchBatchForm.batchno.value=="<%=batchNo%>")
		  {
		 
		   document.searchBatchForm.buttonValue.value="search"
		     document.searchBatchForm.submit();
  	      
		  } 
  	       }  	    
	</script>
	
</head>

<body class="whitebackgrnd" onload="">
		<html:form action="/searchBatch" scope="request">
			<html:hidden property="buttonValue"/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    

     <tr>
    <td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> 
  </tr>
  
	 
	<tr >
      <td class="textlabels">Batch No</td>
	  <td>
	   <html:text property="batchno"  value="<%=batchNo%>" size="3"/>
	  </td>
	  <td class="textlabels">Batch Desc</td>
	  <td><html:text property="desc" value=""  />
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
if((search!=null)) 
{
%>
<div id="divtablesty1" class="scrolldisplaytable">
<display:table name="<%=search%>" pagesize="<%=pageSize%>" class="displaytagstyle">
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Batches displayed,For more Batches click on page numbers.
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
  			String batchno="";
  			if(search!=null && search.size()>0)
  			{
  			Batch p=(Batch)search.get(i);
  			if(p.getBatchId()!=null)
  			{
  			batchno=p.getBatchId();
  			}
  			}
  			String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
		 <display:column title="Batch Number"><a href="<%=tempPath%>"><%=batchno%></a></display:column>

		<display:column property="batchDesc" title="Description"></display:column>
	<%i++; %>
</display:table>  
  </div>
  
<%}%>

</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>


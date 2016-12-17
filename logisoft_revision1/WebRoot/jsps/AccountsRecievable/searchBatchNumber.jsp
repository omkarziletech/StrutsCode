<%@ page language="java" pageEncoding="ISO-8859-1" %>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="java.util.ArrayList"/>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.ArBatch"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
 String path= request.getContextPath();


 String batchNo = "";
 List batchList = new ArrayList(); 
 if(request.getParameter("batchNo") != null)
 {
   batchNo = request.getParameter("batchNo");
 }

if(session.getAttribute("batchlist") != null)
{
  batchList = (List)session.getAttribute("batchlist");
}

String editPath = path + "/searchBatchNumber.do";
if(request.getAttribute("checked") != null)
{

  %>
  
  <script type="text/javascript">
    self.close();
	opener.location.href="<%=path%>/jsps/AccountsRecievable/arBatch.jsp";
 </script>
 <%
 
 }
  %>
<html> 
	<head >
		<title>JSP for SearchBatchNumberForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
	</head>
	<body class="whitebackgrnd" onload="batchload('<%=batchNo%>')">
		<html:form action="/searchBatchNumber" scope="request">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
    	<tr class="tableHeadingNew">Search Criteria
<%--    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> --%>
  		</tr>
  		<tr >
      		<td width="1" class="textlabels">Batch Number</td>
	  		<td width="1"><html:text property="batchNo" value="<%=batchNo %>" size="5"/></td>
	  		<td width="1"><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
	  	</tr>
	    </table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
        <tr class="tableHeadingNew">Search Results
<%--    		<td height="15" class="headerbluesmall" colspan="11">&nbsp;&nbsp; Search Criteria</td> --%>
  		</tr>
		</table>
			<%
		int i=0;
		if(batchList != null )
		{
		%>
		
		 <div id="divtablesty1">
		 <display:table name="<%=batchList%>" pagesize="<%=pageSize%>" class="displaytagstyle">
		<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    					<font color="blue">{0}</font> Terminal Details displayed,For more Ports click on page numbers.
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
  			int bNo =0; 
  			
  		
  			if(batchList!=null && batchList.size()>0)
  			{
  			ArBatch c=(ArBatch)batchList.get(i);
  			if(c.getBatchId() != null)
  			{
  			bNo = c.getBatchId();
  			}
  			}
  		    String iStr=String.valueOf(i);
  			String tempPath=editPath+"?index="+iStr;
  			%>
  			<display:column title="Batch Number"><a href="<%=tempPath%>"><%=bNo%></a></display:column> 
  			 <%i++; %>
  			 </display:table>
  			 </div>
  			 <%
  			   }
  			  %>
  			 </td>
  			 </table>
  		<html:hidden property="buttonValue"/>
  	      <html:hidden property="index"/>
		</html:form>
	</body>
	<script type="text/javascript">
		function batchload(val)
		{
		 if(val != "")
		 {
		
		   document.searchBatchNumberForm.batchNo.value=val;
		   document.searchBatchNumberForm.submit();
		 }
		 
		}
		
		function search1()
        { 
        if(document.searchBatchNumberForm.batchNo.value=="" )
        {
        alert("Please Enter any search Criteria");
        return;
        }
      
        document.searchBatchNumberForm.buttonValue.value = "search";
        
        document.searchBatchNumberForm.submit();
  	    	    	
  	    }
		</script>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


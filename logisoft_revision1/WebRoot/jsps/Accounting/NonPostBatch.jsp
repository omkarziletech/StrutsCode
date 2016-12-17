<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cvst.logisoft.domain.JournalEntry"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<% 

String path = request.getContextPath();

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
	path="../..";
}
List postedBatchList=new ArrayList();
if(session.getAttribute("postedBatchList")!=null)
{
postedBatchList=(List)session.getAttribute("postedBatchList");
}

%>
<html> 
	<head>
	<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<title>BatchList</title>
		<%@include file="../includes/baseResources.jsp" %>

		<title>JSP for NonPostBatchForm form</title>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/nonPostBatch" scope="request">
		<font color="red" size="4"><%="This Batch cannot be posted"%></font>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td height="12" class="headerbluesmall">
						&nbsp;&nbsp;List of Error Journal Entries
					</td>
				</tr>
			</table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	 <% int i=0; %>
			   <display:table name="<%=postedBatchList%>" id="row" pagesize="<%=pageSize%>"
				class="displaytagstyle">
				<display:setProperty name="paging.banner.some_items_found">
					<span class="pagebanner"> <font color="blue">{0}</font> Batch
						details displayed,For more batches click on page numbers. </span>
				</display:setProperty>
				<display:setProperty name="paging.banner.one_item_found">
					<span class="pagebanner"> One {0} displayed. Page Number </span>
				</display:setProperty>
				<display:setProperty name="paging.banner.all_items_found">
					<span class="pagebanner"> {0} {1} Displayed, Page Number </span>
				</display:setProperty>

				<display:setProperty name="basic.msg.empty_list">
					<span class="pagebanner"> No Records Found. </span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="JournalEntry" />
				<display:setProperty name="paging.banner.items_name" value="JournalEntrys" />
				<%
				String period="";
				if(postedBatchList!=null && postedBatchList.size()>0)
				{
				JournalEntry j1=(JournalEntry)postedBatchList.get(i); 
				if(j1.getPeriod()!=null)
				{
				period=j1.getPeriod();
				}
				}%>
				<display:column property="batchId" title="Batch Number"/>
				<display:column property="journalEntryId" title="Journal Entry Id"></display:column>
				<display:column title="Period"><%=period%></display:column>
			<%i++; %>
			</display:table>
			
			</table>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


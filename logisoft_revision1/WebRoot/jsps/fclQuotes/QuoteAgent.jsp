<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.CustomerTemp,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
List agentList=new ArrayList();
if(session.getAttribute("agentList")!=null){
agentList=(List)session.getAttribute("agentList");
}
String link="";
String editPath=path+"/quoteAgent.do";
String button="";
if(request.getAttribute("buttonValue")!=null){
button=(String)request.getAttribute("buttonValue");
}
if(button!=null && button.equals("Completed")){
%>
<script type="text/javascript">
parent.parent.GB_hide()
parent.parent.getAgentInfo("${clientList[0]}","${clientList[1]}");
</script>
<%
}
%>
<html> 
	<head>
		<title>JSP for QuoteAgentForm form</title>
	</head>
	<%@include file="../includes/baseResources.jsp" %>
	<body class="whitebackgrnd">
		<html:form action="/quoteAgent" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew ">
  <tr class="tableHeadingNew"><td width="100%">
  <table class="tableBorderNew"><tr class="tableHeadingNew"><td width="100%">List of Agents</td></tr>
</table></td><td><table><tr>
</tr>
</table></td></tr>
<%
if(agentList!=null && agentList.size()>0)
 {
 int i=0; 
 
%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=agentList%>" class="displaytagstyleNew" pagesize="10"  style="width:100%" id="arInquiry" sort="list" >

<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Recievables displayed,For more Records click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
  			<%
  			String accountName="";
  			String accountNo="";
  			
  			CustomerTemp custAddress=(CustomerTemp)agentList.get(i);
  			if(custAddress.getAccountName()!=null){
  			accountName=custAddress.getAccountName();
  			accountNo=custAddress.getAccountNo();
  			}
  			link=editPath+"?paramId="+i+"&button="+button+"&accountNo="+accountNo;
  			 %>
  	<display:column title="CustomerName" ><a href="<%=link%>" ><%=accountName%></a></display:column>
  	<display:column property="accountNo" title="CustomerNo"></display:column>
<%i++;

 %>
</display:table>  
<%} %>
  </div>


</table>
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


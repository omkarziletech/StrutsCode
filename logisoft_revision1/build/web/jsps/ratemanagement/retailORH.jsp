<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,
com.gp.cong.logisoft.domain.RetailWeightRangesRatesHistory,
com.gp.cong.logisoft.domain.RetailStandardCharges,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailOceanDetailsRatesHistory"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List retailRatesHistoryList=new ArrayList();
RetailStandardCharges retailRatesObj1= new RetailStandardCharges();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
if(session.getAttribute("retailstandardCharges")!=null ){
    retailRatesObj1=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
}
retailRatesHistoryList=dbUtil.getRetailRatesHistory(retailRatesObj1.getId());
String defaultRate="";
if(session.getAttribute("retaildefaultRate")!=null){
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
%>
 
<html> 
	<head>
		<title>JSP for RetailORHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/retailORH" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
     <tr class="tableHeadingNew">Ocean Frieght Rates History</tr>	
			<tr><td>
          		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
          		
        <% 
        int i=0;
        
        %>
        <display:table name="<%=retailRatesHistoryList%>" pagesize="<%=pageSize%>" defaultorder="descending" defaultsort="1" class="displaytagstyle" id="itemtable" sort="list" > 
			<%
				String cheDate="";
				String firstOcean = "";
				String firstTt = "";
				String secondOcean = "";
				String secondTt = "";
				String thirdOcean = "";
				String thirdTt = "";
				String fourthOcean = "";
				String fourthTt = "";
				String effDate="",rateCBM ="",rate1000kg="",ratelbs="",ofMinAmt="",ratecft="",rateCFT="",englishMinAmt="";
				if(retailRatesHistoryList!=null && retailRatesHistoryList.size()>0)
  					{
  						RetailOceanDetailsRatesHistory retailRatesHistory=(RetailOceanDetailsRatesHistory)retailRatesHistoryList.get(i); 			
  						if(retailRatesHistory.getId()!=null)
  						{
  						
  							retailRatesHistory.getWhoChanged();
							
							if(retailRatesHistory.getChangedDate()!=null){
							cheDate=dateFormat.format(retailRatesHistory.getChangedDate());
  						}
  						if(retailRatesHistory.getEffectiveDate()!=null){
							effDate=dateFormat.format(retailRatesHistory.getEffectiveDate());
  						}
  						}
  						
  						if (retailRatesHistory.getMetricCmb() != null) {
									rateCBM = df.format(retailRatesHistory
									.getMetricCmb());
								} else {
									rateCBM = ("0.00");
								}
								if (retailRatesHistory.getMetric1000kg() != null) {
									rate1000kg = df.format(retailRatesHistory
									.getMetricCmb());
								} else {
									rate1000kg = ("0.00");
								}
								if (retailRatesHistory.getMetricMinAmt() != null) {
									ofMinAmt = df.format(retailRatesHistory
									.getMetricMinAmt());
								} else {
									ofMinAmt = ("0.00");
								}
								if (retailRatesHistory.getEnglish1000lb() != null) {
									ratelbs = df.format(retailRatesHistory
									.getEnglish1000lb());
								} else {
									ratelbs = ("0.00");
								}
								if (retailRatesHistory.getEnglishLbs() != null) {
									rateCFT = df.format(retailRatesHistory
									.getEnglishLbs());
								} else {
									rateCFT = ("0.00");
								}
								if (retailRatesHistory.getEnglishMinAmt() != null) {
									englishMinAmt = df.format(retailRatesHistory
									.getEnglishMinAmt());
								} else {
									englishMinAmt = ("0.00");
								}
								if (retailRatesHistory.getAOcean() != null) {
									firstOcean = df.format(retailRatesHistory
									.getAOcean());
								} else {
									firstOcean = ("0.00");
								}
								if (retailRatesHistory.getATt() != null) {
									firstTt = df.format(retailRatesHistory
									.getATt());
								} else {
									firstTt = ("0.00");
								}
								if (retailRatesHistory.getBOcean() != null) {
									secondOcean = df.format(retailRatesHistory
									.getBOcean());
								} else {
									secondOcean = ("0.00");
								}
								if (retailRatesHistory.getBTt() != null) {
									secondTt = df.format(retailRatesHistory
									.getBTt());
								} else {
									secondTt = ("0.00");
								}
								if (retailRatesHistory.getCOcean() != null) {
									thirdOcean = df.format(retailRatesHistory
									.getCOcean());
								} else {
									thirdOcean = ("0.00");
								}
								if (retailRatesHistory.getCTt() != null) {
									thirdTt = df.format(retailRatesHistory
									.getCTt());
								} else {
									thirdTt = ("0.00");
								}
								if (retailRatesHistory.getDOcean() != null) {
									fourthOcean = df.format(retailRatesHistory
									.getDOcean());
								} else {
									fourthOcean = ("0.00");
								}
								if (retailRatesHistory.getDTt() != null) {
									 fourthTt= df.format(retailRatesHistory
									.getDTt());
								} else {
									fourthTt = ("0.00");
								}
								}
			 %>
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Item Details Displayed,For more Items click on Page Numbers.
    				<br>
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
			<display:setProperty name="paging.banner.item_name" value="Item"/>
  			<display:setProperty name="paging.banner.items_name" value="Items"/>
  			<display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
			<%if(defaultRate.equals("M"))
			{ %>
			<display:column property="metric1000kg" title="Rate/1000kgs" ><%=rate1000kg %></display:column>
			<display:column property="metricCmb" title="Rate/CBM " > <%=rateCBM %> </display:column>
			<display:column property="metricMinAmt" title="OF Min Amt" > <%= ofMinAmt%> </display:column>
			<%}
			else if(defaultRate.equals("E"))
			{ %>
			<display:column property="english1000lb" title="Rate/100lb " > <%=ratelbs %> </display:column>
			<display:column property="englishLbs" title="Rate/CFT" > <%= ratecft%> </display:column>
			<display:column property="englishMinAmt" title="OF Min Amt " > <%=englishMinAmt %> </display:column>
			<%} %>
			<display:column property="AOcean" title="Size(A) Ocean" > <%=firstOcean %> </display:column>
			<display:column property="ATt" title="Size(A) Tt" > <%= firstTt%> </display:column>
			<display:column property="BOcean" title="Size(B) Ocean" > <%=secondOcean %> </display:column>
			<display:column property="BTt" title="Size(B) Tt" > <%=secondTt %> </display:column>
			<display:column property="COcean" title="Size(C) Ocean" > <%=thirdOcean %> </display:column>
			<display:column property="CTt" title="Size(C) Tt" > <%=thirdTt %> </display:column>
			<display:column property="DOcean" title="Size(D) Ocean" > <%=fourthOcean %> </display:column>
			<display:column property="DTt" title="Size(D) Tt" > <%=fourthTt %> </display:column>
			<display:column  title="Effective  Date" ><%=effDate%></display:column>
			<display:column  title="Changed Date" ><%=cheDate%></display:column>
			<display:column property="whoChanged" title="Who Changed" />
			

  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table>
    	</td> 
   </tr>  
</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


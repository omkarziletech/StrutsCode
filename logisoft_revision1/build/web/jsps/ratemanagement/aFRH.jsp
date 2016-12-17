<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.AirAccesorialRatesHistory,com.gp.cong.logisoft.domain.AirCommodityChargesHistory,com.gp.cong.logisoft.domain.StandardCharges,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List standardHistoryList=new ArrayList();
StandardCharges airRatesObj1= new StandardCharges();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String chargeCode = "";
  					String chargeDesc = "";
  					String chargeType="";
  					Date effective_Date=new Date();
	  				String effDate=null;	
					String type=null;
					String cheDate="";
if(session.getAttribute("standardCharges")!=null )
{
    airRatesObj1=(StandardCharges)session.getAttribute("standardCharges");
}


List csssListHistory = new ArrayList();
csssListHistory=dbUtil.getAllAirCommodityHistory(airRatesObj1.getId());

int k=0;
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	
	
	
		<html:form action="/aFRH" scope="request">

	<body class="whitebackgrnd">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-top:10px;">
<tr class="tableHeadingNew" height="90%">List of Commodity Specific Accessorial Rate History</tr> 
 	<td>
			
		<!-- 888888888888888888888888888888888888888888888888888888888888888888-->
<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr class="textlabels">
  				<td height="12" colspan="4"  >&nbsp;&nbsp; </td>
			</tr>
			</table>
		 <display:table name="<%=csssListHistory%>" pagesize="<%=pageSize%>" class="displaytagstyle"  sort="list" id="agsstable" > 
			
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
  			<%
  			    
				Date change_date=new Date();
				String insuranceRate = "";
				String insuranceAmt = "";
				String amtPerCft = "";
				String amtPer100lbs = "";
				String amtPerCbm = "";
				String amtPer1000kg = "";
				String minAmt = "";
				String amount = "";
				String perc = "";
				  if(csssListHistory != null && csssListHistory.size()>0)
			    {
					AirCommodityChargesHistory airCommodityChargesHistory=(AirCommodityChargesHistory)csssListHistory.get(k);
					if(airCommodityChargesHistory.getChargeType()!=null)
					{
					type=airCommodityChargesHistory.getChargeType().getCodedesc();
					}
					if(airCommodityChargesHistory.getChargeCode()!=null)
					{
					chargeCode=airCommodityChargesHistory.getChargeCode().getCode();
					}
			    	//----------------------------
			    	if(airCommodityChargesHistory!=null){
			    	if(airCommodityChargesHistory.getEffectiveDate()!=null){
			    	effDate=dateFormat.format(airCommodityChargesHistory.getEffectiveDate());
					
					}
				if(airCommodityChargesHistory.getChangedDate()!=null){
						cheDate=dateFormat.format(airCommodityChargesHistory.getChangedDate());
  					
					}
				}
				if(airCommodityChargesHistory.getInsuranceRate()!=null)
				{
				insuranceRate = df.format(airCommodityChargesHistory.getInsuranceRate());
				}
				if(airCommodityChargesHistory.getInsuranceAmt() != null)
				{
				insuranceAmt = df.format(airCommodityChargesHistory.getInsuranceAmt());
				}
				if(airCommodityChargesHistory.getAmtPerCft() !=null)
				{
				amtPerCft = df.format(airCommodityChargesHistory.getAmtPerCft());
				}
				if(airCommodityChargesHistory.getAmtPer100lbs() != null)
				{
				amtPer100lbs = df.format(airCommodityChargesHistory.getAmtPer100lbs());
				}
				if(airCommodityChargesHistory.getAmtPerCbm() != null)
				{
				amtPerCbm = df.format(airCommodityChargesHistory.getAmtPerCbm());
				}
				if(airCommodityChargesHistory.getAmtPer1000kg() != null)
				{
				amtPer1000kg = df.format(airCommodityChargesHistory.getAmtPer1000kg());
				}
				if(airCommodityChargesHistory.getMinAmt() != null)
				{
				minAmt = df.format(airCommodityChargesHistory.getMinAmt());
				}
				if(airCommodityChargesHistory.getAmount() != null)
				{
				amount = df.format(airCommodityChargesHistory.getAmount());
				}
				if(airCommodityChargesHistory.getPercentage() != null)
				{
				perc = df.format(airCommodityChargesHistory.getPercentage());
				}
				}
				
			 %>
  			<display:column  title="Chrg Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column  title="Insure Rate"><%=insuranceRate%></display:column>
			<display:column  title="Insure Amount"><%=insuranceAmt%></display:column>
			
			<display:column  title="Amt/Cft" ><%=amtPerCft%></display:column>
			<display:column  title="Amt/100lbs" ><%=amtPer100lbs%></display:column>
			<display:column  title="Amt/Cbm"><%=amtPerCbm%></display:column>
			<display:column  title="Amt/1000kg"><%=amtPer1000kg%></display:column>
			
			<display:column property="percentage" title="Perc" > <%=perc %> </display:column>
			
			<display:column  title="MinAmt"><%=minAmt%></display:column>
			
			<display:column  title="Amt"><%=amount%></display:column>
			
		<display:column title="Effec Date" ><%=effDate%></display:column>
			
		<display:column title="Chng Date" ><%=cheDate%></display:column>
  				<display:column property="whoChanged" title="Who Chnged" />
			
  			<% k++;
  		   	   
  			%>
		     	    		
			
  			   		
       		
		</display:table>
     </td>
     
     
        
    	</html:form>
  
		
		
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


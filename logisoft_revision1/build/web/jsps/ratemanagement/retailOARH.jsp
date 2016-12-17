
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RetailStandardCharges1History,com.gp.cong.logisoft.domain.RetailCommodityChargesHistory,com.gp.cong.logisoft.domain.RetailStandardCharges"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
List standardHistoryList=new ArrayList();
RetailStandardCharges retailRatesObj1= new RetailStandardCharges();
String chargeCode = "";
  					String chargeDesc = "";
  					String chargeType="0";
  					Date effective_Date=new Date();
	  				String effDate=null;	
					String type=null;
					String cheDate="";
if(session.getAttribute("retailstandardCharges")!=null )
{
    retailRatesObj1=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
}
List csssListHistory = new ArrayList();
csssListHistory=dbUtil.getAllRetailCommodityHistory(retailRatesObj1.getId());
String defaultRate="";

if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}

int k=0;
%>

 
<html> 
	<head>
		<title>JSP for RetailOARHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<html:form action="/retailOARH" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
	<tr class="tableHeadingNew" >List of Commodity Specific Acessorial Rates History </tr>
   
	
	
		<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr class="textlabels">
  				<td height="12" colspan="4" >&nbsp;&nbsp;</td>
			</tr>
			</table>
		 <display:table name="<%=csssListHistory%>" pagesize="<%=pageSize%>"  defaultorder="descending" defaultsort="1"  class="displaytagstyle"  sort="list" id="agsstable"> 
			
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
				String  insuranceAmt = "";
				String  amtPerCft = "";
				String  amtPer100lbs = "";
				String  amtPerCbm = "";
				String  amtPer1000kg = "";
				String  percentage = "";
				String  minAmt= "";
				String  amount= "";
				
				
				  if(csssListHistory != null && csssListHistory.size()>0)
			    {
					RetailCommodityChargesHistory retailCommodityChargesHistory=(RetailCommodityChargesHistory)csssListHistory.get(k);
					if(retailCommodityChargesHistory.getChargeType()!=null)
					{
						type=retailCommodityChargesHistory.getChargeType().getCodedesc();
				
					}
						chargeCode=retailCommodityChargesHistory.getChargeCode().getCode();
					
			    	
			    	if(retailCommodityChargesHistory!=null)
			    	{
			    		if(retailCommodityChargesHistory.getEffectiveDate()!=null)
			    		{
			    			effective_Date=retailCommodityChargesHistory.getEffectiveDate();
							int d=1900+effective_Date.getYear();
							effDate=effective_Date.getMonth()+"/"+effective_Date.getDate()+"/"+d;
						}
						if(retailCommodityChargesHistory.getChangedDate()!=null)
						{
							change_date=retailCommodityChargesHistory.getChangedDate();
  							int d1=1900+change_date.getYear();
							cheDate=change_date.getMonth()+"/"+change_date.getDate()+"/"+d1;
						}
						if(retailCommodityChargesHistory.getInsuranceRate() != null)
						{
						  insuranceRate = df.format(retailCommodityChargesHistory.getInsuranceRate());
						}
						if(retailCommodityChargesHistory.getInsuranceAmt() != null )
						{
						  insuranceAmt = df.format(retailCommodityChargesHistory.getInsuranceAmt());
						}
						if(retailCommodityChargesHistory.getAmtPerCft() != null )
						{
						  amtPerCft = df.format(retailCommodityChargesHistory.getAmtPerCft());
						}
						if(retailCommodityChargesHistory.getAmtPer100lbs() != null )
						{
						  amtPer100lbs = df.format(retailCommodityChargesHistory.getAmtPer100lbs());
						}
						if(retailCommodityChargesHistory.getAmtPerCbm() != null )
						{
						  amtPerCbm = df.format(retailCommodityChargesHistory.getAmtPerCbm());
						}
						if(retailCommodityChargesHistory.getAmtPer1000kg() != null )
						{
						  amtPer1000kg = df.format(retailCommodityChargesHistory.getAmtPer1000kg());
						}
						if(retailCommodityChargesHistory.getPercentage() != null )
						{
						  percentage = per.format(retailCommodityChargesHistory.getPercentage()) + "%";
						}
						if(retailCommodityChargesHistory.getMinAmt() != null )
						{
						  minAmt = df.format(retailCommodityChargesHistory.getMinAmt());
						}
						if(retailCommodityChargesHistory.getAmount() != null )
						{
						  amount = df.format(retailCommodityChargesHistory.getAmount());
						}
					}
				}			
			 %>
			  <display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
  			<display:column  title="Charge_Code"><%=chargeCode %></display:column>
			<display:column  title="Charge_Type"  ><%=type %></display:column>
			<display:column property="standard" title="Standard"></display:column>
			<display:column property="asFreightedCheckBox" title="As_Frg"></display:column>
			<display:column  title="&nbsp;Insu_Rate"> &nbsp;<%=insuranceRate %> </display:column>
			<display:column  title="&nbsp;Insu_Amount"> &nbsp;<%=insuranceAmt %> </display:column>
			<%
			if(defaultRate.equals("E"))
			{ %>
			<display:column  title="&nbsp;AmtPerCft" > &nbsp;<%=amtPerCft %> </display:column>
			<display:column  title="&nbsp;AmtPer100lbs" > &nbsp;<%=amtPer100lbs %> </display:column>
			<%}
			else if(defaultRate.equals("M"))
			{ 
		
			%>
			<display:column  title="&nbsp;AmtPerCbm" >&nbsp; <%=amtPerCbm %> </display:column>
			<display:column  title="&nbsp;AmtPer1000kg">&nbsp; <%=amtPer1000kg %> </display:column>
			<%}
			 %>
			<display:column  title="&nbsp;Percentage" >&nbsp; <%=percentage %> </display:column>
			<display:column  title="&nbsp;MinAmt" > &nbsp;<%=minAmt %> </display:column>
			<display:column  title="&nbsp;Amt">&nbsp; <%=amount %> </display:column>
			
		<display:column title="&nbsp;Effective_Date" >&nbsp;<%=effDate%></display:column>
			
		<display:column title="Changed_Date" ><%=cheDate%></display:column>
  				<display:column property="whoChanged" title="Who Changed" />
			
  			<% k++;
  		   	   
  			%>
		     
		</display:table>
		</td>
		</table>
			
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


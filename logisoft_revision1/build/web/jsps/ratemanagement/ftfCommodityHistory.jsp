<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FTFStandardChargesHistory,com.gp.cong.logisoft.domain.FTFCommodityChargesHistory,com.gp.cong.logisoft.domain.FTFMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List standardHistoryList=new ArrayList();
DecimalFormat df = new DecimalFormat("0.00");
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
String chargeCode = "";
String chargeDesc = "";
String chargeType="";
Date effective_Date=new Date();
String effDate=null;	
String type=null;
String cheDate="";
String insuranceRate= "";
String insuranceAmt= "";
String amtPerCft= "";
String amtPer100lbs= "";
String amtPerCbm= "";
String amtPer1000kg= "";
String minAmt= "";
String amount= "";
String Perc= "";

FTFMaster ftfMaster=new FTFMaster();
if(session.getAttribute("addftfMaster")!=null){
					ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
				}
//standardHistoryList=dbUtil.getAllCoStandardHistory(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort());

List csssListHistory = new ArrayList();
csssListHistory=dbUtil.getAllftfCommodityHistory(ftfMaster.getId());

int k=0;
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
<table width=100% border="0" cellpadding="0" cellspacing="0"class="tableBorderNew">
			<tr class="tableHeadingNew"> List of Commodity Rate History </tr>
			<tr>
			  <td>
			  
		 <display:table name="<%=csssListHistory%>" pagesize="<%=pageSize%>" defaultorder="descending" defaultsort="1" class="displaytagstyle"  sort="list" id="agsstable" > 
			
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
				
				  if(csssListHistory != null && csssListHistory.size()>0)
			    {
					FTFCommodityChargesHistory ftfCommodityChargesHistory=(FTFCommodityChargesHistory)csssListHistory.get(k);
					if(ftfCommodityChargesHistory.getChargeType()!=null)
					type=ftfCommodityChargesHistory.getChargeType().getCodedesc();
					chargeCode=ftfCommodityChargesHistory.getChargeCode().getCode();
			    
			    	
			    	if(ftfCommodityChargesHistory!=null){
			    	if(ftfCommodityChargesHistory.getEffectiveDate()!=null){
			    	effDate=dateFormat.format(ftfCommodityChargesHistory.getEffectiveDate());
					
					}
				if(ftfCommodityChargesHistory.getChangedDate()!=null){
						cheDate=dateFormat.format(ftfCommodityChargesHistory.getChangedDate());
  					
					}
				}
				if(ftfCommodityChargesHistory.getInsuranceRate() != null)
				{
				   insuranceRate = df.format(ftfCommodityChargesHistory.getInsuranceRate());
				}
				if(ftfCommodityChargesHistory.getInsuranceAmt() != null)
				{
				   insuranceAmt = df.format(ftfCommodityChargesHistory.getInsuranceAmt());
				}
				if(ftfCommodityChargesHistory.getAmtPerCft() != null)
				{
				   amtPerCft = df.format(ftfCommodityChargesHistory.getAmtPerCft());
				}
				if(ftfCommodityChargesHistory.getAmtPer100lbs() != null)
				{
				   amtPer100lbs = df.format(ftfCommodityChargesHistory.getAmtPer100lbs());
				}
				if(ftfCommodityChargesHistory.getAmtPerCbm() != null)
				{
				   amtPerCbm = df.format(ftfCommodityChargesHistory.getAmtPerCbm() );
				}
				if(ftfCommodityChargesHistory.getAmtPer1000kg() != null)
				{
				   amtPer1000kg = df.format(ftfCommodityChargesHistory.getAmtPer1000kg());
				}
				if(ftfCommodityChargesHistory.getPercentage() != null)
				{
				   Perc = df.format(ftfCommodityChargesHistory.getPercentage()) + "%";
				}
				if(ftfCommodityChargesHistory.getMinAmt() != null)
				{
				   minAmt = df.format(ftfCommodityChargesHistory.getMinAmt());
				}
				if(ftfCommodityChargesHistory.getAmount() != null)
				{
				   amount = df.format(ftfCommodityChargesHistory.getAmount());
				}
				if(ftfCommodityChargesHistory.getInsuranceRate() != null)
				{
				   insuranceRate = df.format(ftfCommodityChargesHistory.getInsuranceRate());
				}
				if(ftfCommodityChargesHistory.getInsuranceRate() != null)
				{
				   insuranceRate = df.format(ftfCommodityChargesHistory.getInsuranceRate());
				}
				}
				
			 %>
			 <display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
  			<display:column  title="Chrg Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column property="insuranceRate" title="Insure Rate"><%=insuranceRate%></display:column>
			<display:column property="insuranceAmt" title="Insure Amount"><%=insuranceAmt%></display:column>
			
			<display:column property="amtPerCft" title="Amt/Cft" ><%=amtPerCft%></display:column>
			<display:column property="amtPer100lbs" title="Amt/100lbs" ><%=amtPer100lbs%></display:column>
			<display:column property="amtPerCbm" title="Amt/Cbm" ><%=amtPerCbm%></display:column>
			<display:column property="amtPer1000kg" title="Amt/1000kg" ><%=amtPer1000kg%></display:column>>
			
			<display:column property="percentage" title="Perc" ><%=Perc%></display:column>
			
			<display:column property="minAmt" title="MinAmt" ><%=minAmt%></display:column>
			
			<display:column property="amount" title="Amt"><%=amount%></display:column>
			
		<display:column title="Effec Date" ><%=effDate%></display:column>
			
		<display:column title="Chng Date" ><%=cheDate%></display:column>
  				<display:column property="whoChanged" title="Who Chnged" />
			
  			<% k++;
  		   	   
  			%>
		     	    		
			
  			   		
       		
		</display:table>
        
        
    	</td>
			</tr>
			</table>
		
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


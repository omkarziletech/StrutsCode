<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.LCLColoadStandardChargesHistory,com.gp.cong.logisoft.domain.LCLColoadCommodityChargesHistory,com.gp.cong.logisoft.domain.LCLColoadMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
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

DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String chargeCode = "";
String chargeDesc = "";
String chargeType="";
String minAmt="";
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg= "";
Date effective_Date=new Date();
String effDate=null;	
String type=null;
String cheDate="";
LCLColoadMaster lCLColoadMaster=new LCLColoadMaster();
if(session.getAttribute("addlclColoadMaster")!=null){
					lCLColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
				}
//standardHistoryList=dbUtil.getAllCoStandardHistory(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort());

List csssListHistory = new ArrayList();
csssListHistory=dbUtil.getAllCoLoadCommodityHistory(lCLColoadMaster.getId());
String defaultRate="";
if(session.getAttribute("lcldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("lcldefaultRate");
}
int k=0;
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<!-- 888888888888888888888888888888888888888888888888888888888888888888-->
<%--<table width=100% border="0" cellpadding="0" cellspacing="0">--%>
<%--			<tr class="textlabels">--%>
<%--  				<td height="12" colspan="4"  class="headerbluesmall">&nbsp;&nbsp;List of Commodity Rate History </td>--%>
<%--			</tr>--%>
<%--			</table>--%>

        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" height="90%">List of Commodity Rate History</tr> 
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
					LCLColoadCommodityChargesHistory coCommodityChargesHistory=(LCLColoadCommodityChargesHistory)csssListHistory.get(k);
					if(coCommodityChargesHistory.getChargeType()!=null)
					type=coCommodityChargesHistory.getChargeType().getCodedesc();
					chargeCode=coCommodityChargesHistory.getChargeCode().getCode();
			    
			    	
			    	if(coCommodityChargesHistory!=null){
			    	if(coCommodityChargesHistory.getEffectiveDate()!=null){
			    	effDate=dateFormat.format(coCommodityChargesHistory.getEffectiveDate());
					
					}
				if(coCommodityChargesHistory.getChangedDate()!=null){
						cheDate=dateFormat.format(coCommodityChargesHistory.getChangedDate());
  					
					}
				}
				if(coCommodityChargesHistory.getPercentage()!=null)
					{
					percentage=coCommodityChargesHistory.getPercentage().toString()+"%";
					}
				if(coCommodityChargesHistory.getAmtPer1000kg()!=null)
				{
					amtkg=df.format(coCommodityChargesHistory.getAmtPer1000kg());
				}
				if(coCommodityChargesHistory.getInsuranceRate()!=null)
				{
				   insuranceRate=df.format(coCommodityChargesHistory.getInsuranceRate());
				}	
				
				if(coCommodityChargesHistory.getInsuranceAmt()!=null)
				{
					insuranceAmt=df.format(coCommodityChargesHistory.getInsuranceAmt());
				}	
				
				if(coCommodityChargesHistory.getAmount()!=null)
				{
					amount=df.format(coCommodityChargesHistory.getAmount());
				}	
				
				if(coCommodityChargesHistory.getMinAmt()!=null)
				{
					minAmt=df.format(coCommodityChargesHistory.getMinAmt());
				}	
				if(coCommodityChargesHistory.getAmtPerCft()!=null)
				{
					cft=df.format(coCommodityChargesHistory.getAmtPerCft());
				}	
				if(coCommodityChargesHistory.getAmtPer100lbs()!=null)
				{
					lbs=df.format(coCommodityChargesHistory.getAmtPer100lbs());
				}	
				if(coCommodityChargesHistory.getAmtPerCbm()!=null)
				{
					cbm=df.format(coCommodityChargesHistory.getAmtPerCbm());
				}	
				}
				
			 %>
			 <display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
  			<display:column  title="Chrg Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column  title="Insure Rate"><%=insuranceRate%></display:column>
			<display:column  title="Insure Amount"><%=insuranceAmt%></display:column>
			<%if(defaultRate.equals("E"))
			{ %>
			<display:column  title="Amt/Cft" ><%=cft%></display:column>
			<display:column  title="Amt/100lbs" ><%=lbs%></display:column>
			<%}
			else if(defaultRate.equals("M"))
			{ %>
			<display:column  title="Amt/Cbm" ><%=cbm%></display:column>
			<display:column  title="Amt/1000kg"><%=amtkg%></display:column>
			<%} %>
			<display:column  title="Perc" ><%=percentage%></display:column>
			
			<display:column  title="MinAmt" ><%=minAmt%></display:column>
			
			<display:column  title="Amt"><%=amount %></display:column>
			
		<display:column title="Effec Date" ><%=effDate%></display:column>
			
		<display:column title="Chng Date" ><%=cheDate%></display:column>
  				<display:column property="whoChanged" title="Who Chnged" />
			
  			<% k++;
  		   	   
  			%>
		     	    		
			
  			   		
       		
		</display:table>
        
        </td>
        </table>
        
    	
		
		
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


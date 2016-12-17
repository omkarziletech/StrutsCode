<%@ page language="java" import="com.gp.cong.logisoft.domain.FTFStandardCharges,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FTFStandardChargesHistory,com.gp.cong.logisoft.domain.LCLColoadCommodityChargesHistory,com.gp.cong.logisoft.domain.FTFMaster,java.text.DateFormat,java.text.SimpleDateFormat"%>
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

DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String chargeCode = "";
String chargeDesc = "";
String chargeType="";
Date effective_Date=new Date();
String effDate=null;	
String type=null;
String cheDate="";
int chargecode=0;
FTFMaster ftfMaster=new FTFMaster();
if(session.getAttribute("ftfStandardCharges")!=null )
{
   FTFStandardCharges  ftfStandardCharges=(FTFStandardCharges)session.getAttribute("ftfStandardCharges");
	if(ftfStandardCharges.getChargeCode()!=null)
	{
		chargecode=ftfStandardCharges.getChargeCode().getId();
		
	}
}
if(session.getAttribute("addftfMaster")!=null)
		{
					ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
				}


standardHistoryList=dbUtil.getAllftfStandardHistory(ftfMaster.getId(),chargecode);

//List csssListHistory = new ArrayList();
//csssListHistory=dbUtil.getAllCoLoadCommodityHistory(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort());

int k=0;
%>
<html> 
	<head>
		<title>JSP for AFRHForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	</head>
	<body class="whitebackgrnd">
		<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew">List of Accessorial General Standard Charges History </tr>
             <tr>
              <td>
              
			<table width="100%">
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;

          		%>
          	 	<display:table name="<%=standardHistoryList%>" pagesize="<%=pageSize%>"  class="displaytagstyle" id="airratestable" sort="list" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Item Displayed,For more Data click on Page Numbers.
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
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
						No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Relay"/>
  				<display:setProperty name="paging.banner.items_name" value="Relays"/>
  				<%
  					
					
				Date change_date=null;
  				String insuranceRate = "";
				String  insuranceAmt = "";
				String  amtPerCft = "";
				String  amtPer100lbs = "";
				String  amtPerCbm = "";
				String  amtPer1000kg = "";
				String  percentage = "";
				String  minAmt= "";
				String  amount= "";			
  					
  					if(standardHistoryList!=null && standardHistoryList.size()>0)
  					{
  						FTFStandardChargesHistory ftfStandardChargesHistory=(FTFStandardChargesHistory)standardHistoryList.get(i); 			
  						if(ftfStandardChargesHistory.getChargeCode()!=null)
  						{
  							chargeCode=ftfStandardChargesHistory.getChargeCode().getCode();
  							chargeDesc=ftfStandardChargesHistory.getChargeCode().getCodedesc();
  						}
  						if(ftfStandardChargesHistory.getChargeType()!=null)
  						{
  							chargeType=ftfStandardChargesHistory.getChargeType().getCodedesc();
  							
  						}
  						if(ftfStandardChargesHistory.getEffectiveDate()!=null){
  						effDate=dateFormat.format(ftfStandardChargesHistory.getEffectiveDate());
					
					//---------------------------------------------------
					}if(ftfStandardChargesHistory.getChangedDate()!=null){
					cheDate=dateFormat.format(ftfStandardChargesHistory.getChangedDate());
  					
					
  						}
  						
  						if(ftfStandardChargesHistory.getInsuranceRate() != null)
						{
						  insuranceRate = df.format(ftfStandardChargesHistory.getInsuranceRate());
						}
						if(ftfStandardChargesHistory.getInsuranceAmt() != null )
						{
						  insuranceAmt = df.format(ftfStandardChargesHistory.getInsuranceAmt());
						}
						if(ftfStandardChargesHistory.getAmtPerCft() != null )
						{
						  amtPerCft = df.format(ftfStandardChargesHistory.getAmtPerCft());
						}
						if(ftfStandardChargesHistory.getAmtPer100lbs() != null )
						{
						  amtPer100lbs = df.format(ftfStandardChargesHistory.getAmtPer100lbs());
						}
						if(ftfStandardChargesHistory.getAmtPerCbm() != null )
						{
						  amtPerCbm = df.format(ftfStandardChargesHistory.getAmtPerCbm());
						}
						if(ftfStandardChargesHistory.getAmtPer1000Kg() != null )
						{
						  amtPer1000kg = df.format(ftfStandardChargesHistory.getAmtPer1000Kg());
						}
						if(ftfStandardChargesHistory.getPercentage() != null )
						{
						  percentage = df.format(ftfStandardChargesHistory.getPercentage()) + "%";
						}
						if(ftfStandardChargesHistory.getMinAmt() != null )
						{
						  minAmt = df.format(ftfStandardChargesHistory.getMinAmt());
						}
						if(ftfStandardChargesHistory.getAmount() != null )
						{
						  amount = df.format(ftfStandardChargesHistory.getAmount());
						}
  					}
  					
  		 		%>
  		 		<display:column  title="Chrg Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg Type"  ><%=chargeType %></display:column>
			<display:column property="standard" title="Std"></display:column>
		
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column  title="Insurance Rate"> <%=insuranceRate %> </display:column>
			<display:column  title="Insurance Amount"> <%=insuranceAmt %> </display:column>
			
			<display:column  title="AmtPerCft" > <%=amtPerCft %> </display:column>
			<display:column  title="AmtPer100lbs" > <%=amtPer100lbs %> </display:column>
		<display:column  title="AmtPerCbm" > <%=amtPerCbm %> </display:column>
			<display:column  title="AmtPer1000kg"> <%=amtPer1000kg %> </display:column>
			
			<display:column  title="Percentage" > <%=percentage %> </display:column>
			
			
			<display:column  title="MinAmt" > <%=minAmt %> </display:column>
			
			<display:column  title="Amt"> <%=amount %> </display:column>
			<display:column title="Effec Date" ><%=effDate%></display:column>
			
				<display:column title="Chnged Date" ><%=cheDate%></display:column>
  				<display:column property="whoChanged" title="Who Chnged" />
				
			 	
			 	<%i++; %>
		 </display:table>
   	 </table>
   </div>
  </td>
 </tr>	
			</table>
			
	 			</td>
              </tr>  
           	</table>
		
		</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
		</html>
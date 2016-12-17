<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.AirAccesorialRatesHistory,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
AirStandardCharges airRatesObj1= new AirStandardCharges();
DBUtil dbUtil=new DBUtil();
List standardHistoryList=new ArrayList();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
int id=0;
if(session.getAttribute("airStandardCharges")!=null )
{
    airRatesObj1=(AirStandardCharges)session.getAttribute("airStandardCharges");
    if(airRatesObj1.getChargeCode()!=null)
        id=airRatesObj1.getChargeCode().getId();
}

if(airRatesObj1!=null)
{

standardHistoryList=dbUtil.getAllStandardHistory(airRatesObj1.getStandardId(),id);
}
%>
<html> 
	<head>
		<title>JSP for AirStandardHistoryForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		
	</head>
	<body class="whitebackgrnd">
		<html:form action="/airStandardHistory" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr class="textlabels">
  				 <td height="12" colspan="5" >&nbsp;  </td> 
			</tr>
			</table>
			<table width="100%" class="tableBorderNew">
			<tr class="tableHeadingNew">List of Accessorial General Standard Charges History</tr>
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
          		
          		<%
         			 int i=0;

          		%>
          	 	<display:table name="<%=standardHistoryList%>" pagesize="<%=pageSize%>"   class="displaytagstyle" id="airratestable" sort="list" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Relay Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Air Standard History"/>
  				<display:setProperty name="paging.banner.items_name" value="Air Standard History"/>
  				<%
  					String chargeCode="";
					String chargeDesc="";
					String chargeType="";
				Date change_date=null;
  					String effDate="";
  					String cheDate="";
  					String minAmt = "";
  					if(standardHistoryList!=null && standardHistoryList.size()>0)
  					{
  						AirAccesorialRatesHistory airAccesorialRatesHistory=(AirAccesorialRatesHistory)standardHistoryList.get(i); 			
  						if(airAccesorialRatesHistory.getChargeCode()!=null)
  						{
  							chargeCode=airAccesorialRatesHistory.getChargeCode().getCode();
  							chargeDesc=airAccesorialRatesHistory.getChargeCode().getCodedesc();
  						}
  						if(airAccesorialRatesHistory.getChargeType()!=null)
  						{
  							chargeType=airAccesorialRatesHistory.getChargeType().getCodedesc();
  						}
  						if(airAccesorialRatesHistory.getEffectiveDate()!=null){
  						effDate=dateFormat.format(airAccesorialRatesHistory.getEffectiveDate());
					
					//---------------------------------------------------
					}if(airAccesorialRatesHistory.getChangedDate()!=null){
					cheDate=dateFormat.format(airAccesorialRatesHistory.getChangedDate());
  					
					
  						}
  					
  					}
  					
  		 		%>
  		 		<display:column  title="Chrg Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg Type"  ><%=chargeType %></display:column>
			<display:column property="standard" title="Std"></display:column>
		
			<display:column property="asFrfgted" title="As Freighted"></display:column>
			<display:column property="insuranceRate" title="Insure Rate"></display:column>
			<display:column property="insuranceAmt" title="Insure Amount"></display:column>
			
			<display:column property="amtPerCft" title="Amt/Cft" />
			<display:column property="amtPer100lbs" title="Amt/100lbs" />
			<display:column property="amtPerCbm" title="Amt/Cbm" />
			<display:column property="amtPer1000kg" title="Amt/1000kg" />
			
			<display:column property="percentage" title="Perc" />
			
			
			<display:column property="minAmt" title="MinAmt" />
			
			<display:column property="amount" title="Amt"></display:column>
			
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
			
	
		
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


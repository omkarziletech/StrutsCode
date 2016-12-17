
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,
com.gp.cong.logisoft.domain.RetailStandardCharges1History,
com.gp.cong.logisoft.domain.RetailCommodityChargesHistory,com.gp.cong.logisoft.domain.RetailStandardCharges1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String defaultRate="";
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");
List standardHistoryList=new ArrayList();
RetailStandardCharges1 retailStandardCharges= new RetailStandardCharges1();
RetailStandardCharges1 retailStd=new RetailStandardCharges1();
String chargeCode = "";
  					String chargeDesc = "";
  					String chargeType="";
  					Date effective_Date=new Date();
	  				String effDate=null;	
					String type=null;
					String cheDate="";
					String amount = "";
					int id=0;
if(session.getAttribute("retailStandardCharges")!=null )
{
	
    retailStandardCharges=(RetailStandardCharges1)session.getAttribute("retailStandardCharges");
	if(retailStandardCharges.getChargeCode()!=null)
    id=retailStandardCharges.getChargeCode().getId();
  }


standardHistoryList=dbUtil.getAllRetailStandardHistory(retailStandardCharges.getStandardId(),id);

List csssListHistory = new ArrayList();
//csssListHistory=dbUtil.getAllRetailCommodityHistory(retailRatesObj1.getId());
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
<%--		<table width=100% border="0" cellpadding="0" cellspacing="0">--%>
<%--			<tr class="textlabels">--%>
<%--  				 <td height="12" colspan="5"  class="headerbluesmall">&nbsp;List of &nbsp;Ocean Accessorial Rate History  </td> --%>
<%--			</tr>--%>
<%--			</table>--%>
			<table width="100%" class="tableBorderNew">
			<tr class="tableHeadingNew">List of Ocean Accessorial Rate History</tr>
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;

          		%>
          	 	<display:table name="<%=standardHistoryList%>" pagesize="<%=pageSize%>" defaultorder="descending" defaultsort="1"  class="displaytagstyle" id="retailratestable" sort="list"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Accesorial General standards Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Retail"/>
  				<display:setProperty name="paging.banner.items_name" value="Retails"/>
  				<%
  					
					
				Date change_date=null;
  						
  					String percantage="";
  					if(standardHistoryList!=null && standardHistoryList.size()>0)
  					{
  						RetailStandardCharges1History retailStandardCharges1History=(RetailStandardCharges1History)standardHistoryList.get(i); 			
  						if(retailStandardCharges1History.getChargeCode()!=null)
  						{
  							chargeCode=retailStandardCharges1History.getChargeCode().getCode();  						
  							chargeDesc=retailStandardCharges1History.getChargeCode().getCodedesc();
  						}
  						if(retailStandardCharges1History.getChargeType()!=null)
  						{
  							chargeType=retailStandardCharges1History.getChargeType().getCodedesc();
  							
  						}
  						if(retailStandardCharges1History.getEffectiveDate()!=null){
  						effective_Date=retailStandardCharges1History.getEffectiveDate();
					 int d=1900+effective_Date.getYear();
					effDate=effective_Date.getMonth()+"/"+effective_Date.getDate()+"/"+d;
					//---------------------------------------------------
					}
					if(retailStandardCharges1History.getChangedDate()!=null){
					change_date=retailStandardCharges1History.getChangedDate();
  					 int d1=1900+change_date.getYear();
					cheDate=change_date.getMonth()+"/"+change_date.getDate()+"/"+d1;
					
  						}
  						
  						if(retailStandardCharges1History.getAmount() != null)
  						{
  						  amount = df.format(retailStandardCharges1History.getAmount() );
  						}
  						if(retailStandardCharges1History.getPercentage() != null)
  						{
  						  percantage =per.format(retailStandardCharges1History.getPercentage());
  						}  					
  					}
  					
  		 		%>
  		 		 <display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
  		 		<display:column  title="Charge_Code"><%=chargeCode %></display:column>
			<display:column  title="Charge_Type"  ><%=chargeType %></display:column>
			<display:column property="standard" title="Standard"></display:column>
			<display:column property="asFrfgted" title="&nbsp;As_Frg"></display:column>
			<display:column property="insuranceRate" title="&nbsp;Insu_ate"></display:column>
			<display:column property="insuranceAmt" title="&nbsp;Insu_Amount"></display:column>
<%
			if(!defaultRate.equals("") && defaultRate.equals("E"))
			{ 
%>	
			<display:column property="amtPerCft" title="&nbsp;AmtPerCft" />
			<display:column property="amtPer100lbs" title="&nbsp;AmtPer100lbs" />
<%
			} 
			if(!defaultRate.equals("") && defaultRate.equals("M")){ %>	
			<display:column property="amtPerCbm" title="&nbsp;AmtPerCbm" />
			<display:column property="amtPer1000kg" title="&nbsp;AmtPer1000kg" />
			<%} %>
			<display:column  title="&nbsp;Percentage" ><%=percantage%></display:column>
			<display:column property="minAmt" title="&nbsp;MinAmt" />
			<display:column  title="Amt"><%=amount%></display:column>
			<display:column title="Effective Date" ><%=effDate%></display:column>
			<display:column title="Changed Date" ><%=cheDate%></display:column>
  			<display:column property="whoChanged" title="Who Changed" />
				
			 	
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


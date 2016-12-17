<%@ page language="java" import= "com.gp.cong.logisoft.domain.UniversalMaster,java.text.DecimalFormat,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.util.DBUtil,java.text.DateFormat,java.text.SimpleDateFormat,java.util.*,com.gp.cong.logisoft.domain.UniverseCommodityChrgHistory,com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

List csssList = new ArrayList();
List applyGeneralStandardList=new ArrayList();
String chargecode=null;
String codeDesc=null;
DBUtil dbUtil=new DBUtil();
String chargeType="";
String rateType="";
String minAmt="";
User user=null;
String Port="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("########0.000");
if(session.getAttribute("universaldefaultRate")!=null)
	{
		Port=(String)session.getAttribute("universaldefaultRate");
		
		
	}
if(session.getAttribute("loginuser")!=null)
{
	user=(User)session.getAttribute("loginuser");
}
UniversalMaster universalMaster=new UniversalMaster();
if(session.getAttribute("addUniversalMaster")!=null)
{
universalMaster=(UniversalMaster)session.getAttribute("addUniversalMaster");
}
csssList=dbUtil.getAllUniversalCommHistory(universalMaster.getId());

String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
String effectiveDate="";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Commodity Specific Charges</title>
    
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
	   function disabled(val1)
   		{
   		
   		var tables1 = document.getElementById('agsstable');
		
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		// initRowHighlighting();
   		// setWarehouseStyle();
   		 }
	  	 		
	    }
	    function displaytagcolor()
		{
		 	 	var datatableobj1 = document.getElementById('agsstable');
		
				for(i=0; i<datatableobj1.rows.length; i++)
				{
					var tablerowobj = datatableobj1.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					
		 	 	}
		}
	function initRowHighlighting()
  	{
			if (!document.getElementById('agsstable')){ return; }
			 var tables = document.getElementById('agsstable');
			attachRowMouseEvents(tables.rows);
		
	}
	function attachRowMouseEvents(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
			var row = rows[i];
			row.onmouseover =	function() 
			{ 
				this.className = 'rowin';
			}
			row.onmouseout =	function() 
			{ 
				this.className = '';
			}
            row.onclick= function() 
			{ 
			}
			}
		
		}
  	    </script>

  </head>
  
  <body class="whitebackgrnd" onLoad="disabled()">
    	
	<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"> Universal Commodity History </tr>
			<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int k=0;
        
        %>
        <display:table name="<%=csssList%>" pagesize="<%=pageSize%>"  class="displaytagstyle"  sort="list" id="agsstable"  defaultorder="ascending" defaultsort="2"> 
			
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
  			<%Date effective_Date=null;
						String effDate=null;		
				String type=null;
				String chargeCode=null;
				String tempPath="";
				String perc="";
								String cheDate="";
			    if(csssList != null && csssList.size()>0)
			    {
					UniverseCommodityChrgHistory airCommodityChargesObj2=(UniverseCommodityChrgHistory)csssList.get(k);
					if(airCommodityChargesObj2.getChargeType()!=null)
					type=airCommodityChargesObj2.getChargeType().getCodedesc();
					if(airCommodityChargesObj2.getChargeCode()!=null)
					chargeCode=airCommodityChargesObj2.getChargeCode().getCode();
				
			
  					if(airCommodityChargesObj2.getEffectiveDate()!=null){
  					effective_Date=airCommodityChargesObj2.getEffectiveDate();
  					
  					
					effDate=dateFormat.format(effective_Date);
					}
					if(airCommodityChargesObj2.getPercentage()!=null)
					{
					perc=per.format(airCommodityChargesObj2.getPercentage()) + "%";
					}
					
				if(airCommodityChargesObj2.getAmtPer1000kg()!=null)
				{
					amtkg=df.format(airCommodityChargesObj2.getAmtPer1000kg());
				}
				if(airCommodityChargesObj2.getInsuranceRate()!=null)
				{
				   insuranceRate=df.format(airCommodityChargesObj2.getInsuranceRate());
				}	
				
				if(airCommodityChargesObj2.getInsuranceAmt()!=null)
				{
					insuranceAmt=df.format(airCommodityChargesObj2.getInsuranceAmt());
				}	
				
				if(airCommodityChargesObj2.getAmount()!=null)
				{
					amount=df.format(airCommodityChargesObj2.getAmount());
				}	
				
				if(airCommodityChargesObj2.getMinAmt()!=null)
				{
					minAmt=df.format(airCommodityChargesObj2.getMinAmt());
				}	
				if(airCommodityChargesObj2.getAmtPerCft()!=null)
				{
					cft=df.format(airCommodityChargesObj2.getAmtPerCft());
				}	
				if(airCommodityChargesObj2.getAmtPer100lbs()!=null)
				{
					lbs=df.format(airCommodityChargesObj2.getAmtPer100lbs());
				}	
				if(airCommodityChargesObj2.getAmtPerCbm()!=null)
				{
					cbm=df.format(airCommodityChargesObj2.getAmtPerCbm());
				}	
				  if(airCommodityChargesObj2.getChangedDate()!=null){
  					cheDate=dateFormat.format(airCommodityChargesObj2.getChangedDate());
  					}	
				}	
					String iStr=String.valueOf(k);
  					
			 %>
  			<display:column  title="Chrg_Code"><%=chargeCode %></display:column>
			<display:column  title="Chrg_Type"  ><%=type %></display:column>
			<display:column property="standard" title="Std"></display:column>
			
			<display:column property="asFrfgted" title="As_Frg"></display:column>
			<display:column  title="&nbsp;Insu_Rate">&nbsp;<%=insuranceRate%></display:column>
			<display:column  title="&nbsp;Insu_Amt"><%=insuranceAmt%></display:column>
		<%	if(Port!=null && Port.equals("E"))
		{%>
			<display:column  title="&nbsp;Amt/Cft" >&nbsp;<%=cft%></display:column>
			<display:column  title="&nbsp;Amt/100lbs">&nbsp;<%=lbs%></display:column>
		
		<%}else if(Port!=null && Port.equals("M")) { %>
		
			<display:column  title="&nbsp;Amt/Cbm" >&nbsp;<%=cbm%></display:column>
			<display:column  title="&nbsp;Amt/1000kg" >&nbsp;<%=amtkg%></display:column>
			<%} %>
			<display:column title="&nbsp;Perc" >&nbsp;<%=perc%></display:column>
			
			<display:column  title="&nbsp;MinAmt" >&nbsp;<%=minAmt%></display:column>
			
			<display:column  title="&nbsp;Amt">&nbsp;<%=amount%></display:column>
			
			
			<display:column title="Eff Date" ><%=effDate%></display:column>
  			<display:column property="whoChanged" title="Who Chnged" />
  			<display:column  title="Changed Date"><%=cheDate%></display:column>
  			
  			<% k++;
  		   	   
  			%>
		     	    		
			
  			   		
       		
		</display:table>
        
        </table></div>  
    	</td> 
   </tr>  
		</table>
		
  		
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

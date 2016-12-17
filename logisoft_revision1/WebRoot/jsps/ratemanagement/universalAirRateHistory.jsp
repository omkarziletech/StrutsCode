
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.*,java.util.ArrayList,com.gp.cong.logisoft.domain.UniverseAirFreightHistory,com.gp.cong.logisoft.domain.UniversalMaster,
com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,
java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import = "java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%

String path = request.getContextPath();
String amount="";
DBUtil dbUtil=new DBUtil();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
List ariFrightList=new ArrayList();
List startrange=new ArrayList();
String getObj=null;
getObj=(String)session.getAttribute("rangeObj");

UniversalMaster universalMaster=new UniversalMaster();
if(session.getAttribute("addUniversalMaster")!=null)
{
universalMaster=(UniversalMaster)session.getAttribute("addUniversalMaster");
}
ariFrightList=dbUtil.getAllUniversalAirHistory(universalMaster.getId());




	   
	   
String modify="";


	


%>
 
<html> 
	<head>
		<title>JSP for AGSSForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function disabled(val1)
   		{
   		
   		var tables1 = document.getElementById('uniairtable');
		
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		// setWarehouseStyle();
   		 }
	  	 		
	    }
	    function displaytagcolor()
		{
		 	 	var datatableobj1 = document.getElementById('uniairtable');
		
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
			if (!document.getElementById('uniairtable')){ return; }
			 var tables = document.getElementById('uniairtable');
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
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		
			<table width="100%" class="tableBorderNew">
			<tr class="tableHeadingNew"> Import Air History </tr>
	<tr height="5">
    </tr>  
	<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        
        %>
        <display:table name="<%=ariFrightList%>" pagesize="<%=pageSize%>" class="displaytagstyle"  sort="list" id="uniairtable" > 
			<%
				String tempPath="";
				String wightrange="";
				String ChangedDate="";
				String cheDate="";
//  					GenericCode genObj=new GenericCode();
  //					GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
  					if(ariFrightList!=null && ariFrightList.size()>0){
  					UniverseAirFreightHistory universeAirFreight=(UniverseAirFreightHistory)ariFrightList.get(i);
  					if(universeAirFreight.getWeightRange()!=null){
  					wightrange=universeAirFreight.getWeightRange().getCodedesc();
  					}
  					if(universeAirFreight.getAmount()!=null){
  					amount=df.format(universeAirFreight.getAmount());
  					}
  					if(universeAirFreight.getChangedDate()!=null){
  					cheDate=dateFormat.format(universeAirFreight.getChangedDate());
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
  			
  			
			<display:column  paramId="param" paramProperty="id"  title="Wight Range &nbsp; &nbsp;" ><%=wightrange%></display:column>
			<display:column  title="Amount &nbsp; &nbsp;"><%=amount%></display:column>
			
			<display:column property="whoChanged" title="Who Chnged &nbsp; &nbsp;" />
			<display:column  title="Changed Date &nbsp; &nbsp;"><%=cheDate%></display:column>
			
  			<% i++;
  		   	   
  			%>
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
   </tr>  
</table>
			
	
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>







 	
			

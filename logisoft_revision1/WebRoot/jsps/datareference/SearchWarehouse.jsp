
<jsp:directive.page import="com.gp.cong.logisoft.domain.Warehouse"/><%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.SearchWarehouseBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.WarehouseTemp"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
	 <%
response.addHeader("Pragma", "No-cache");
response.addHeader("Cache-Control", "no-cache");
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String buttonValue="";
String wareHouseCode = "";
String wareHouseName = "";
String city = "";

SearchWarehouseBean sBean=new SearchWarehouseBean();
List warehouseList=null;
String msg="";
String message="";
String modify=null;
WarehouseTemp whrs=new WarehouseTemp();
if(session.getAttribute("wareHouse") != null)
				{
				   session.removeAttribute("wareHouse");
				}
				
if(session.getAttribute("wareHouseObj") != null)
{
whrs = (WarehouseTemp)session.getAttribute("wareHouseObj");
wareHouseCode = whrs.getWarehouseNo();
wareHouseName = whrs.getWarehouseName();
city =whrs.getCity();
session.removeAttribute("wareHouseObj");
}

whrs.setMatch("starts");

if(session.getAttribute("warehouseList")!=null)
{
		warehouseList=(List)session.getAttribute("warehouseList");
		if(warehouseList.size() ==1)
		{
		whrs = (WarehouseTemp)warehouseList.get(0);
		wareHouseCode = whrs.getWarehouseNo();
		wareHouseName = whrs.getWarehouseName();
		city =whrs.getCity();
		}
		
} 
if(request.getAttribute("swBean")!=null)
	{
 		sBean=(SearchWarehouseBean)request.getAttribute("swBean");
 		whrs.setMatch(sBean.getMatch());
 		buttonValue=sBean.getButtonValue();
	} 
request.setAttribute("whrs",whrs);
if(request.getAttribute("msg")!=null)
{
		message=(String)request.getAttribute("msg");
}
if(request.getAttribute("message")!=null)
{
		msg=(String)request.getAttribute("message");
}

if(request.getParameter("modify")!= null)
{
		session.setAttribute("modifyforwarehouse",request.getParameter("modify"));
 		modify=(String)session.getAttribute("modifyforwarehouse");
}
else
{
 		modify=(String)session.getAttribute("modifyforwarehouse");
}
if(request.getAttribute("buttonValue")!=null)
{
   		 buttonValue=(String)request.getAttribute("buttonValue");
}


if(request.getParameter("programid")!= null && session.getAttribute("processinfoforwarehouse")==null)
{ 
	  buttonValue="searchall";
 	 
 	  session.setAttribute("caption", "List of Warehouses");
 }
if(buttonValue.equals("searchall"))
{

	warehouseList=dbUtil.getAllWarehouses();
	session.setAttribute("warehouseList",warehouseList);
	if(request.getParameter("programid")!=null)
{
 session.setAttribute("processinfoforwarehouse",request.getParameter("programid"));
 }
session.setAttribute("caption", "List of Warehouses");
}	

// Name:Rohith Date:12/04/2007  setting the path to Search Warehouse action
	String editPath=path+"/searchWarehouse.do";
	
	
%>
 
<html>
	<head>
		<title>SearchMenu/Action</title>
		<%@include file="../includes/baseResources.jsp" %>	
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
		</script>
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
		<script type="text/javascript">
		
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide.toggle();
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				
			});
		
           var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/WarehouseCode.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/WarehouseCode.jsp","","width=350,height=100");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
          
		   function disabled(val)
			{
				if(val=="20"){
				window.location.href="<%= path%>/jsps/datareference/addWarehouse.jsp";
				}
				if(val == 0)
				{		
      				    var imgs = document.getElementsByTagName('img');
   						for(var k=0; k<imgs.length; k++)
   						{
   							 if(imgs[k].id != "showall" && imgs[k].id!="search")
   							 {
   								 imgs[k].style.visibility = 'hidden';
   							 }
   						}
   				}
   				var datatableobj = document.getElementById('warehousetable');
   				if(datatableobj!=null)
   				{
   					//displaytagcolor();
   					//initRowHighlighting();
   					setWarehouseStyle();
   				}	
  			 }
  			function setWarehouseStyle()
		  {
		
		  	if(document.searchWarehouse.buttonValue.value=="searchall")
		  	{
		  	 var x=document.getElementById('warehousetable').rows[0].cells;	
		  
		     x[0].className="sortable sorted order1";
		  	}
		  	if(document.searchWarehouse.buttonValue.value=="search")
		  	{
		  	var input = document.getElementsByTagName("input");
		  	var select = document.getElementsByTagName("select");
	  		 			if(!input[0].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('warehousetable').rows[0].cells;	
	  		   				 x[0].className="sortable sorted order1";
	  		   			}
	  		   			else if(!input[1].value=="")
	  					{
	  					
	  		 				 var x=document.getElementById('warehousetable').rows[0].cells;	
	  		   				 x[1].className="sortable sorted order1";
	  		   			}
	  		   			
	  		   		
	  		   		else if(!input[2].value=="")
	  					{
	  		 				 var x=document.getElementById('warehousetable').rows[0].cells;	
	  		   				 x[2].className="sortable sorted order1";
	  		   			}
	  				}
		 }  
		  function displaytagcolor()
		  {
				var datatableobj = document.getElementById('warehousetable');
				for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					/*if(i==0)
					{
					for(j=0; i<tablerowobj.cells.length; j++)
					{
						alert(tablerowobj.cells[j].style.backgroundPosition);
					}
					}*/
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
		 function searchform(val){
			if(document.searchWarehouse.warehouseCode.value=="")
			{
				if(document.searchWarehouse.warehouseName.value=="")
				{
					if(document.searchWarehouse.city.value=="")
					{
                                            if(document.searchWarehouse.warehouseType.value=="")
					{
							alert("Please Enter Any Search Criteria");
							document.searchWarehouse.warehouseCode.focus();
							return;
                                        }
					}
				}
			}
			document.searchWarehouse.warehouseCode.value = val;
			document.searchWarehouse.buttonValue.value="search";
			document.searchWarehouse.submit();
		}
		function toUppercase(obj) 
	    {
			obj.value = obj.value.toUpperCase();
		}
		function searchallform()
		{
			document.searchWarehouse.buttonValue.value="searchall";
			document.searchWarehouse.submit();
		}
		function initRowHighlighting()
  		{
			if (!document.getElementById('warehousetable')){ return; }
			var tables = document.getElementById('warehousetable');
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
	
	 	  function popup1(mylink, windowname)
			{
			
				if (!window.focus)return true;
				var href;
				if (typeof(mylink) == 'string')
		   		href=mylink;
				else
		   		href=mylink.href;
				mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
				mywindow.moveTo(200,180);
				return false;
			}
			
			function searchbycode()
			{
			   if(event.keyCode==13)
			   {
			  window.open("<%= path%>/jsps/datareference/searchWareHouseCode.jsp?wareHouseCode="+ document.searchWarehouse.warehouseCode.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400" );
			   }
			}
			
		function searchbyname()
			{
			   if(event.keyCode==13)
			   {
			  window.open("<%= path%>/jsps/datareference/searchWareHouseCode.jsp?wareHouseName="+ document.searchWarehouse.warehouseName.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400" );
			   }
			}
			
			function getOpenPage(){
			window.location.href="<%= path%>/jsps/datareference/addWarehouse.jsp";
		
			}
			function getWareHouseDetails(ev){	
			    document.getElementById("warehouseName").value="";
   				document.getElementById("city").value="";
	        	if(event.keyCode==9 || event.keyCode==13){
						var params = new Array();
						params['requestFor'] = "wareHouseName";
						params['wareHouseCode'] = document.searchWarehouse.warehouseCode.value;
						var bindArgs = {
	  							url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  							error: function(type, data, evt){alert("error");},
	 							mimetype: "text/json",
	  							content: params
	     				};
	     			
						var req = dojo.io.bind(bindArgs);
	 					dojo.event.connect(req, "load", this, "populateWareHouseDetails");
		       }
			}
			function populateWareHouseDetails(type, data, evt) {
  				if(data){
   						document.getElementById("warehouseName").value=data.wareHouseName;
   						if(data.wareHouseCity){
   						document.getElementById("city").value=data.wareHouseCity;
   						}
   				}
			}
</script>
<%@include file="../includes/resources.jsp" %>


</head>
<body class="whitebackgrnd" marginheight="0" topmargin="0" >
<html:form action="/searchWarehouse" name="searchWarehouse" type="com.gp.cong.logisoft.struts.form.SearchWarehouseForm" scope="request">
 <font color="blue" size="4"><%=msg%></font>
   <br>
 <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew">
   	<tr class="tableHeadingNew">Search Criteria</tr>
	   <tr>
		<td class="style2">
		<span onmouseover="tooltip.show('<strong>Ware House Code</strong>',null,event);" onmouseout="tooltip.hide();">Whse Code</span></td>
		<td>
		   <input  name="warehouseCode" id="warehouseCode" value=""  onkeydown="getWareHouseDetails(this.value)" size="30" />
	        <dojo:autoComplete formId="searchWarehouse" textboxId="warehouseCode" action="<%=path%>/actions/getWareHouseName.jsp?tabName=WARE_HOUSE&from=0"/>
		</td>
<%--		<td width="15%"><html:text property="warehouseCode" value="<%=whrs.getId()%>" onkeyup="toUppercase(this)" onkeypress="searchbycode()" maxlength="3" ></html:text>&nbsp;<img border="0" src="<%=path%>/img/search1.gif" style="cursor: pointer; cursor: hand;" onclick="return popup1('<%= path%>/jsps/datareference/searchWareHouseCode.jsp?wareHouseCode=' + document.searchWarehouse.warehouseCode.value ,'windows')"/></td>--%>
		<td class="style2"><span onmouseover="tooltip.show('<strong>Ware House Name</strong>',null,event);" onmouseout="tooltip.hide();">Whse Name</span></td>
		<td><html:text property="warehouseName"  onkeyup="toUppercase(this)" onkeypress="searchbyname()" maxlength="25"  size="30"/></td>
	</tr>
	<tr>
	    <td class="style2">City</td>
		<td><html:text property="city"  size="30"/></td>
		<td  class="style2" >Warehouse Type</td>
                <td>
                    <html:select property="warehouseType" styleClass="selectboxstyle"  style="width:118px" >
                        <html:option value="">Select</html:option>
                        <html:option value="FCLE">FCLE</html:option>
                        <html:option value="LCLE">LCLE</html:option>
                        <html:option value="FCLI">FCLI</html:option>
                        <html:option value="LCLI">LCLI</html:option>
                        <html:option value="AIRE">AIRE</html:option>
                        <html:option value="AIRI">AIRI</html:option>
                    </html:select>
                </td>
	</tr>
	<tr>		
	<td colspan="6" align="center">
		<input type="button" class="buttonStyleNew" value="Search"  name="search" onclick="searchform(document.searchWarehouse.warehouseCode.value)"  />
		<input type="button" class="buttonStyleNew" value="Show All"   id="showall" onclick="searchallform()"  />
		<input type="button" class="buttonStyleNew" value="Add New" id="addnew"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/WarehouseCode.jsp?button='+'code',150,600)"/>
		
	 </td>
	</tr>
 </table>
    				
    				 
     		
     		 	
      <br>
    <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0" >		
 	<tr class="tableHeadingNew" ><td></td>
 	 <td align="right">
 	 
 	 <%--<a id="commonChargesToggle" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a>
 	 
 	  --%></td>
  	</tr>
  	
  	<tr >
        <td align="left" scope="row"><%--
        <div id="common_Charges_vertical_slide">
        --%><div id="divtablesty1" class="scrolldisplaytable">
          <table border="0" width="100%" cellpadding="0" cellspacing="0" >
          	<%
         		 int i=0;
          	%>
          	 <display:table name="<%=warehouseList%>" pagesize="<%=pageSize%>" 
          	 class="displaytagstyle" id="warehousetable" sort="list" style="width:100%">
			 	
			 	<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Warehouse Displayed,For more Data click on Page Numbers.
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
				<%
					String link=null;
					if(warehouseList!=null && warehouseList.size()>0){
						WarehouseTemp warehouse=(WarehouseTemp)warehouseList.get(i);
						link= editPath+"?param="+warehouse.getId();
					}
				 %>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Warehouse"/>
  				<display:setProperty name="paging.banner.items_name" value="Warehouses"/>

				<display:column  property="warehouseName" sortable="true" title="WAREHOUSE NAME"/>
				<display:column property="city" title="CITY" sortable="true"></display:column>
			    <display:column property="warehouseNo" title="WAREHOUSE CODE" href="<%=editPath%>" paramId="paramid" paramProperty="id" sortable="true"></display:column>
				<display:column  property="warehouseType" sortable="true" title="WAREHOUSE TYPE"/>
	  		     <display:column title="Actions">
	  		     	<span onmouseover="tooltip.show('<strong>MoreInfo<strong>',null,event)" onmouseout="tooltip.hide()">
	  		     	 <img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href='<%=link%>'"/>
	  		     	</span>
	  		     </display:column>
	  		     <%i++; %>
			
		 </display:table>
		
   	 </table>
   </div>
   <%--</div>
  --%></td>
 </tr>
 
</table>	
  
<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue %>"/>
<script>disabled('<%=modify%>')</script>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


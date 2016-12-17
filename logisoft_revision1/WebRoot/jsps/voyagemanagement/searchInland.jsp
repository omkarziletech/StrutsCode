<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.domain.VoyageInland,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.domain.FclBuyCost,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,java.text.DecimalFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>

<%
String path = request.getContextPath();
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setMatch("match");
String editPath="";

VoyageInland voyageInland=new VoyageInland();

String trmNum="";
String trmNam="";
String destAirPortNo="";
String destAirPortName="";
String message="";
String modify="";
String voyNo="";
String dateLoad=""; 
List records=new ArrayList();
DecimalFormat df = new DecimalFormat("0.00");

List searchinlandList=new ArrayList();

if(session.getAttribute("trade")!=null)
	{
	session.removeAttribute("trade");
	}
if(request.getAttribute("airRatesBean")!=null)
	{
	airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
	}
request.setAttribute("airRatesBean",airRatesBean);
if(request.getAttribute("warning")!=null)
	{
	message=(String)request.getAttribute("warning");
	}
	
if(session.getAttribute("searchinlandrecords")!=null)

	{
	
	voyageInland=(VoyageInland)session.getAttribute("searchinlandrecords");
	
	if(voyageInland.getOriginTerminal()!=null)
	{
		trmNum=voyageInland.getOriginTerminal().getTrmnum();
		trmNam=voyageInland.getOriginTerminal().getTerminalLocation();
		
		}
		if(voyageInland.getDestTerminal()!=null)
		{
		destAirPortNo=voyageInland.getDestTerminal().getShedulenumber();
		destAirPortName=voyageInland.getDestTerminal().getPortname();
		}
		
		
		
}

	if(session.getAttribute("fclmessage")!=null)
		{
		message=(String)session.getAttribute("fclmessage");
		}

	if(request.getParameter("programid")!= null)
		{
		 modify=(String)request.getParameter("programid");
		 session.setAttribute("processinfoforInland",modify);
		}
		/*if(session.getAttribute("addfclrecords")!=null){
		fclBuy=(FclBuy)session.getAttribute("addvoyagerecords");
		FclBuyCostDAO fclBuyDAO=new FclBuyCostDAO();
		fclBuyDAO.removeRecords(fclBuy);
		}*/
		
	if(session.getAttribute("searchFclcodelist")!=null)
		{
		records=(List)session.getAttribute("searchFclcodelist");
		
	}
	
	if(session.getAttribute("searchinlandList")!=null)
{
		searchinlandList=(List)session.getAttribute("searchinlandList");

} 

if(session.getAttribute("inlandmessage")!=null){
message=(String)session.getAttribute("inlandmessage");
}
if(request.getParameter("modify")!= null)
{

 modify=(String)request.getParameter("modify");
 session.setAttribute("modifyforInlandRates",modify);
}
else
{
 	modify=(String)session.getAttribute("modifyforInlandRates");
}
session.setAttribute("inlandrate", "edit");
	editPath=path+"/searchInland.do";	
	
	%> 
<html> 
	<head>
		<title>JSP for SearchInlandForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

			
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
		 var newwindow = '';  
		 
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
			//document.searchFCLForm.buttonValue.value="search";
			// document.searchInlandForm.submit();
			return false;
			}
			
		function searchform()
			{
			if(document.searchInlandForm.terminalNumber.value=="" || document.searchInlandForm.destSheduleNumber.value=="")
				{
					alert("Please select OriginTerminal  and Dest Port  ");
					return;
				}
				
				
			document.searchInlandForm.buttonValue.value="search";
			document.searchInlandForm.index.value="get";
			document.searchInlandForm.submit();
			}
			
		function searchallform()
			{
			document.searchInlandForm.buttonValue.value="showall";
			document.searchInlandForm.submit();
			}
			
		function addform() 
		{

          if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/voyagemanagement/inlandPopup.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/voyagemanagement/inlandPopup.jsp?inlandvyg="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
         } 
        
		
		  
	function disabled()
   	{
		
		/*if(val == 0)
		{		
        	var imgs = document.getElementsByTagName('img');
   			for(var k=0; k<imgs.length; k++)
   			{
   		 		if(imgs[k].id != "showall" && imgs[k].id!="search")
   		 		{
   		 			imgs[k].style.visibility = 'hidden';
   		 		}
   			}
  		 } */
  		  var tables1 = document.getElementById('nonftfratestable');
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
  	 	 displaytagcolorcom()
   		 initRowHighlightingcom();
   		 setWarehouseStylecom();
   		 }
  		 var tables = document.getElementById('lclcoloadratestable');
  		 if(tables!=null)
  		 {
  		 //alert("inside");
  	 	 displaytagcolor();
  	 	 //displaytagcolorcom()
   		 initRowHighlighting();
   		 setWarehouseStyle();
   		 }
   	}
   	
   	 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.searchInlandForm.terminalName.value="";
		document.searchInlandForm.buttonValue.value="popupsearch";
	    document.searchInlandForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getOriginTerminalName(ev)
	{document.getElementById("terminalName").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		   var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.searchInlandForm.terminalNumber.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNameDojo");
		}
	}
	 function populateTerminalNameDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalName").value=data.terminalName;
		   	}
	 }
	
	function getOriginTerminalNumber(ev)
	{document.getElementById("terminalNumber").value="";
		if(event.keyCode==9 || event.keyCode==13){
		
		 var params = new Array();
				 params['requestFor'] = "OrgTerminalNumber";
				 params['terminalName'] = document.searchInlandForm.terminalName.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNumberDojo");
		}
	}
	 function populateTerminalNumberDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalNumber").value=data.terminalNumber;
		   	}
	   }



	 function getDestinationName(ev)
	{document.getElementById("destAirportname").value="";
		if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DestAirportname";
				 params['scheduleNumber'] = document.searchInlandForm.destSheduleNumber.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 
				 dojo.event.connect(req, "load", this, "populateDestAirportname1");
			    }
	}
	  function populateDestAirportname1(type, data, evt) {
		   	if(data){
		   		document.getElementById("destAirportname").value=data.destAirportname;
		   	}
		   }
	function getDestinationNumber(ev)
	{document.getElementById("destSheduleNumber").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		    var params = new Array();
				 params['requestFor'] = "ScheduleNumber";
				 params['destAirportName'] = document.searchInlandForm.destAirportname.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateDestAirportname");
		 }
	}
	
	 function populateDestAirportname(type, data, evt) {
		   	if(data){
		   		document.getElementById("destSheduleNumber").value=data.destSheduleNumber;
		   	}
		   }
	function getInland()
	{
	
	window.location.href="<%=path%>/jsps/voyagemanagement/inlandVoyage.jsp";
	}
		</script>
		
		<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/searchInland" scope="request">
		 <font color="blue" size="4"><%=message%></font>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0">
				 <tr class="textlabels"><td align="left" class="headerbluelarge">Inland Voyage</td>
			     </tr>
				 <tr class="textlabels">
	    			<td align="left" class="headerbluelarge">&nbsp;</td>
	  			 </tr>
	  			
			</table> 
	<table border="0" width="100%" class="tableBorderNew">
	  <tr class="tableHeadingNew"> Search Criteria </tr>
	  <tr>
	     <td>
	    	
		    <table width="100%"  border="0">
  				<tr><td></td></tr>
			
  				<tr valign="top">
    			<td>
    			<table  border="0" ><tr class="style2"><td>Org Trm&nbsp;
	    			
	    			</td></tr></table></td>
	    			<td >
<%--	    			<html:text property="terminalNumber" size="2" onkeydown="titleLetter(this.value)" maxlength="2" styleId="terminalNumber" value="<%=trmNum%>"/>--%>
	    			
	    	   <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getOriginTerminalName(this.value)"   value="<%=trmNum %>" maxlength="2"/>    
    		   <dojo:autoComplete formId="searchInlandForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=INLAND_VOYAGE&from=0"/>                
					</td>
					<td>&nbsp;</td>
	    			<td><span class="textlabels">Org Trm Name</span></td>
	    			<td >
<%--	    			<html:text property="terminalName"  onkeydown="titleLetterName(this.value)" value="<%=trmNam%>"/>--%>
	    			<input name="terminalName" id="terminalName"  onkeydown="getOriginTerminalNumber(this.value)"  value="<%=trmNam %>" size="15" />    
    			   <dojo:autoComplete formId="searchInlandForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=INLAND_VOYAGE&from=0"/>
	    			</td>
	    	
					</tr>
		    		<tr valign="top">	
    				<td class="textlabels">
    					<table border="0" ><tr class="style2"><td>Dest port&nbsp;</td></tr></table> </td>
		    			<td >
<%--		    			<html:text property="destSheduleNumber"  size="5" onkeydown="getDestination(this.value)" maxlength="5" value="<%=destAirPortNo%>"></html:text><br>--%>
		    	<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getDestinationName(this.value)"  value="<%=destAirPortNo %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="searchInlandForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=0"/>
		    			</td>
		    			<td></td>
		    			<td><span class="textlabels">Dest Port Name</span></td>
		    			<td >
<%--		    			<html:text property="destAirportname" onkeydown="getDestinationName(this.value)" value="<%=destAirPortName%>"/><br>--%>
		    	<input name="destAirportname" id="destAirportname" size="15" onkeydown="getDestinationNumber(this.value)"  value="<%=destAirPortName %>" />    
    		    <dojo:autoComplete formId="searchInlandForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=1" />
		    			</td>
		    			<td></td>
		    			<td></td>
		    			<td></td>
		    			<td></td>
		    		</tr>
	    			<tr valign="top">	
    				<td>
    					<table  border="0"><tr class="style2"><td>Voyage #&nbsp;</tr></table> </td>
			    			<td ><html:text property="voyageNo" maxlength="6" size="15"  value="<%=voyNo%>"></html:text><br></td>
			    			<td>&nbsp;</td>
			    			<td><span class="textlabels">Date Loaded</span></td>
			    			<td colspan="0"><html:text property="dateLoaded" styleId="txtItemcted"  value="<%=dateLoad%>" size="15"></html:text>
            				<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top" id="itemcted" onmousedown="insertDateFromCalendar(this.id,0);" style="margin-top:3px" />
            			    </td>
								
						
						
			    			<td>&nbsp;</td>
			    		</tr>	
			    		<table border="0">
			    		<tr>
			    				<td class="style2">Match Only
								<html:radio property="match" value="match" name="airRatesBean"></html:radio>
									Start list at
								<html:radio property="match" value="starts" name="airRatesBean"></html:radio></td>
					
			    		
			    		
			    		</tr>
    		          </table>
    		             <tr>
    		                      	
							<td align="center"><input type="button" value="Search" onclick="searchform()" class="buttonStyleNew" />
						
							    <input type="button" value="AddNew" onclick="return GB_show('Inland Voyage','<%=path%>/jsps/voyagemanagement/inlandPopup.jsp?inlandvyg='+'add',150,600)" class="buttonStyleNew"/>

							</td>
    		             
    		             
    		             </tr>
    				
  			
  			</table>
  	    </td>
	  </tr>
	</table>	
	
	<br style="margin-top : 5px"/>		
	
<table class="tableBorderNew" border="0" width="100%">
 <tr class="tableHeadingNew"> <%=session.getAttribute("voyageRatescaption") %> </tr>
 <tr> 
   <td>
    
  	
    <table width="100%">
  			
		<tr height="5">
    			
  		</tr>
		<tr><td>
		<table width="100%">
  			<tr>
        		<td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
          		<table width="80%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;

          		%>
          	 	<display:table  name="<%=searchinlandList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="lclcoloadratestable" sort="list" style="width:100%"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Voyages Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Voyage"/>
				<display:setProperty name="paging.banner.items_name" value="Voyages"/>
  				
  				<%
  				 String unitycode="";
				   	String code="";
  					String codedesc="";
  					String codetype="";
  					String costcode="";
  					String origin="";
  					String link ="";
  					String terminal="";
					String destterminal="";
					String terminalDk="";
					String destination="";
					String inlandVoyageNo="";
					String dateloaded="";
					String datedepart="";
					String arrivaldate="";
					String vesselNo="";
					String ssvoyageNo="";
  					String ratAmount[]=new String[10];
  					List unittypelist=new ArrayList();
  					//List recordsList=new ArrayList();
  					Set set=new HashSet();
  					int k=0;
					
  					VoyageInland voyageInlands=new VoyageInland();

  					if (searchinlandList != null && searchinlandList.size() > 0)
  					{	
  						 voyageInlands=(VoyageInland)searchinlandList.get(i);
  						 if(voyageInlands.getId()!=null){
  						 
  						 
  						 if(voyageInlands.getOriginTerminal()!=null){
			terminal=voyageInlands.getOriginTerminal().getTrmnum().toString();
		}
		
		if(voyageInlands.getDestTerminal()!=null){
			destterminal=voyageInlands.getDestTerminal().getShedulenumber().toString();
		}
		if(voyageInlands.getScheduleDkOrigin()!=null){
			terminalDk=voyageInlands.getScheduleDkOrigin().getTrmnum().toString();
		}
		if(voyageInlands.getScheduleDkDestination()!=null){
			destination=voyageInlands.getScheduleDkDestination().getShedulenumber();
		}
		if(voyageInlands.getInlandVoyageNo()!=null){
			inlandVoyageNo=voyageInlands.getInlandVoyageNo().toString();
		}
		if(voyageInlands.getDateLoaded()!=null){
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			dateloaded=df1.format(voyageInlands.getDateLoaded());
		}
		if(voyageInlands.getDateOfDeparture()!=null){
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			datedepart=df1.format(voyageInlands.getDateOfDeparture());
		}
		if(voyageInlands.getDateTerminalArrival()!=null){
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			arrivaldate=df1.format(voyageInlands.getDateTerminalArrival());
		}
		if(voyageInlands.getVesselNo()!=null){
			vesselNo=voyageInlands.getVesselNo().getCode().toString();
		}
		if(voyageInlands.getVesselNo()!=null){
			vesselNo=voyageInlands.getVesselNo().getCode().toString();
		}
		if(voyageInlands.getSslVoyageNo()!=null){
			ssvoyageNo=voyageInlands.getSslVoyageNo();
		}
  						 }
  		link=editPath+"?param="+voyageInlands.getId();
  							}
  						
  		 		%>
  		 		
  		 		
				<display:column title="Origin Ter." href="<%=editPath%>"  paramId="paramid" sortable="true" paramProperty="id"><%=terminal%></display:column>
				
				
				<display:column sortable="true" title="Dest Ter.">&nbsp;&nbsp;<%=destterminal%></display:column>
				<display:column sortable="true" title="Inland Voyage#"><%=inlandVoyageNo%></display:column>
				<display:column sortable="true" title="Date Loaded"><%=dateloaded%></display:column>
				<display:column sortable="true" title="Date of Departure"><%=datedepart%></display:column>
				<display:column sortable="true" title="Arrival Date"><%=arrivaldate%></display:column>
				<display:column sortable="true" title="Vessel#"><%=vesselNo%></display:column>
				<display:column sortable="true" title="S.S. Voyage#"><%=ssvoyageNo%></display:column>
				<display:column>
				    <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong',null,event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href('<%=link%>')" /></span>
				</display:column>
				
				 
				
				<%
				i++; %>
		 		</display:table>
		 
   	 </table>
   	 <!-- This is for common commdity -->
  			
	

<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"  />
<script> disabled()</script>
   </div></td></tr></table></td></tr></table>
   
      </td>
   </tr>
</table>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


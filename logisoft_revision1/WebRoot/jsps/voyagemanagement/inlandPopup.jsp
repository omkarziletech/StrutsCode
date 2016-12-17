<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.util.ArrayList,com.gp.cong.logisoft.util.DBUtil,java.util.*,java.text.SimpleDateFormat,java.text.DateFormat,com.gp.cong.logisoft.domain.VoyageInland,java.util.List,com.gp.cong.logisoft.domain.FclBuy"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
VoyageInland voyageInland=new VoyageInland();
String trmNum="";
String trmName="";
String destAirPortNo="";
String destAirportname="";
String message="";
String modify="";
String voyNo="";
String dateLoad="";
String address =""; 
if(request.getParameter("inlandvyg")!=null){
	session.setAttribute("addinlandrecords",session.getAttribute("searchinlandrecords"));
	}

		    
	       if(session.getAttribute("yes")!=null)
			{

			session.removeAttribute("yes");
			}

			if(session.getAttribute("no")!=null)
			{
		      session.removeAttribute("no");
			}
	       
	   		  
if(session.getAttribute("addinlandrecords")!=null)
{

	voyageInland=(VoyageInland)session.getAttribute("addinlandrecords");
	if(voyageInland.getOriginTerminal()!=null)
	{
	trmNum=voyageInland.getOriginTerminal().getTrmnum();
	trmName=voyageInland.getOriginTerminal().getTerminalLocation();
	
	}
	if(voyageInland.getDestTerminal()!=null)
	{
	destAirPortNo=voyageInland.getDestTerminal().getShedulenumber();
	destAirportname=voyageInland.getDestTerminal().getPortname();
	}
	if(voyageInland.getInlandVoyageNo()!=null){
	voyNo=voyageInland.getInlandVoyageNo().toString();
}
if(voyageInland.getDateLoaded()!=null){
	try
	{	Date Dtld=voyageInland.getDateLoaded();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		dateLoad=df1.format(Dtld);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}
	
}
}
if(request.getAttribute("sendfclcontrol")!=null)
{
address=(String)request.getAttribute("sendfclcontrol");
}
if(request.getAttribute("message")!=null){
	message=(String)request.getAttribute("message");
		
}	
	
if(request.getAttribute("msg")!=null)
{
	//msg=(String)request.getAttribute("msg");
}	

if(request.getAttribute("update")!=null){
	message=(String)request.getAttribute("update");
}
if(!trmNum.equals("") && !destAirPortNo.equals("") )
{
if(message.equals("")){
if(address.equals("sendfclcontrol"))
{
%>
	<script>
	         parent.parent.getInland();
	        parent.parent.GB_hide();
	       
		//self.close();
		//opener.location.href="<%=path%>/jsps/voyagemanagement/inlandVoyage.jsp";
	</script>
<%
}}
}

%>
 
<html> 
	<head>
		<title>JSP for InlandPopupForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils','utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
		 function popup1(mylink, windowname)
		{	//alert("123");
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=500,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			// document.inlandPopUpForm.buttonValue.value="search";
			//document.inlandPopupForm.submit();
			return false;
		}
		function searchform()
		{
		if(document.inlandPopupForm.terminalNumber.value=="")
		{
		alert("Please select Terminal Number");
		return;
		}
		if(document.inlandPopupForm.destSheduleNumber.value=="")
		{
		alert("Please select Destination Airport Code");
		return;
		}
		if(document.inlandPopupForm.voyageNo.value=="")
		{
		alert("Please select Voyage#");
		return;
		}
		if(document.inlandPopupForm.dateLoaded.value=="")
		{
		alert("Please select Date Loaded");
		return;
		}
		
			document.inlandPopupForm.buttonValue.value="search";
			document.inlandPopupForm.submit();
		}
	
	
	
	
	
	
 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.inlandPopupForm.terminalName.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
	    document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		
		document.inlandPopupForm.terminalName.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
		document.inlandPopupForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.inlandPopupForm.terminalNumber.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
	    document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.inlandPopupForm.terminalNumber.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
		document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.inlandPopupForm.destSheduleNumber.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
	    document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.inlandPopupForm.destSheduleNumber.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
		document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	 }
	 function getDestination(ev)
	{
		if(event.keyCode==9){
		document.inlandPopupForm.destAirportname.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
	    document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.inlandPopupForm.destAirportname.value="";
		document.inlandPopupForm.buttonValue.value="popupsearch";
		document.inlandPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
		function getOriginTerminalName(ev)
	{document.getElementById("terminalName").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		   var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.inlandPopupForm.terminalNumber.value;
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
				 params['terminalName'] = document.inlandPopupForm.terminalName.value;
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
				 params['scheduleNumber'] = document.inlandPopupForm.destSheduleNumber.value;
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
				 params['destAirportName'] = document.inlandPopupForm.destAirportname.value;
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
	</script>
	
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/inlandPopup" scope="request">
		<font color="blue"><b><%=message%></b></font>
			
	<table class="tableBorderNew" border="0" width="100%">
	  <tr class="tableHeadingNew"> Add New </tr>
	  <tr>
	    <td> 
	       
	    		
			<table width=100%  border="0" cellpadding="0" cellspacing="0">
			<tr class="textlabels">
    			<td><table width="100%" border="0"><tr class="style2"><td>Org Trm &nbsp;</td></tr></table></td>
    			<td>
<%--    			<html:text property="terminalNumber" value="<%=trmNum%>" size="2" onkeydown="titleLetter(this.value)" maxlength="2" />--%>
    			 <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getOriginTerminalName(this.value)" value="<%=trmNum %>" maxlength="2"/>    
    		   <dojo:autoComplete formId="inlandPopupForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=INLAND_VOYAGE&from=0"/>                
    			</td>
    			
    			<td>Org Trm Name</td>
    			<td>
<%--    			<html:text property="terminalName" value="<%=trmName%>" onkeydown="titleLetterName(this.value)"/>--%>
    			<input name="terminalName" id="terminalName"  onkeydown="getOriginTerminalNumber(this.value)" size="15" value="<%=trmName %>" />    
    			   <dojo:autoComplete formId="inlandPopupForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=INLAND_VOYAGE&from=0"/>
    			</td>
    		</tr>
    		<tr class="textlabels">	
    			<td><table width="100%" border="0"><tr class="style2"><td>Dest port  &nbsp;</td></tr></table></td>
    			<td>
<%--    			<html:text property="destSheduleNumber" size="5" onkeydown="getDestination(this.value)" maxlength="5" value="<%=destAirPortNo%>"  />--%>
    			<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getDestinationName(this.value)" value="<%=destAirPortNo %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="inlandPopupForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=0"/>
    			</td>
    			
    			<td>Dest Port Name</td>
    			<td>
<%--    			<html:text property="destAirportname" value="<%=destAirportname%>" onkeydown="getDestinationName(this.value)"/>--%>
    			<input name="destAirportname" id="destAirportname"  onkeydown="getDestinationNumber(this.value)" value="<%=destAirportname %>" size="15" />    
    		    <dojo:autoComplete formId="inlandPopupForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=1" />
    			</td>
    			
    		</tr>
    		<tr class="textlabels">	
    			<td>Voyage #</td>
    			<td><html:text property="voyageNo" maxlength="6"  size="15"  value="<%=voyNo%>"  /></td>
    			
    			<td>Date Loaded</td>
    			<td><html:text property="dateLoaded" styleId="txtitemcreated"  value="<%=dateLoad%>" size="15"></html:text>
    			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
								id="itemcreated"
								onmousedown="insertDateFromCalendar(this.id,0);" /></td>
    		</tr>
    		
    		<tr>
    		<td>&nbsp;</td>
    		<td>&nbsp;</td>
    		<td>
    		    <input type="button" value="Submit" class="buttonStyleNew" onclick="searchform()"/>
    		    
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue" />
		
			</td>
	    </tr>
	</table>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


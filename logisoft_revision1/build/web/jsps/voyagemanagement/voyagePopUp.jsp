
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.util.ArrayList,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.VoyageExport,java.util.List,com.gp.cong.logisoft.domain.FclBuy"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
VoyageExport voyageExport=new VoyageExport();
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
String message="";
String msg="";
String modify="";
String sslineno="";
String sslinename="";
String contact="";

/*if(session.getAttribute("searchFclcodelist")!=null){

	List li=(List)session.getAttribute("searchFclcodelist");
	
	if(li.size()>0){
	session.removeAttribute("searchFclcodelist");
	
	}

}*/
if(request.getParameter("fclcorates")!=null){
	session.setAttribute("addvoyagerecords",session.getAttribute("searchvoyagerecords"));
	}
if(request.getAttribute("message")!=null){
	message=(String)request.getAttribute("message");
		
}
if(session.getAttribute("costcode")!=null){session.removeAttribute("costcode");}
if(session.getAttribute("costCodeList")!=null){session.removeAttribute("costCodeList");}

			  
if(session.getAttribute("addvoyagerecords")!=null)
{

voyageExport=(VoyageExport)session.getAttribute("addvoyagerecords");
if(voyageExport.getOriginTerminal()!=null)
{
terminalNumber=voyageExport.getOriginTerminal().getTrmnum();
terminalName=voyageExport.getOriginTerminal().getTerminalLocation();

}
if(voyageExport.getDestinationPort()!=null)
{
destSheduleNumber=voyageExport.getDestinationPort().getShedulenumber();
destAirportname=voyageExport.getDestinationPort().getPortname();
}
if(voyageExport.getComNum()!=null)
{
comCode=voyageExport.getComNum().getCode();
comDesc=voyageExport.getComNum().getCodedesc();
}
if(voyageExport.getLineNo()!=null){
sslineno=voyageExport.getLineNo().getCarriercode();
sslinename=voyageExport.getLineNo().getCarriername();
//contact=voyageExport.getSslineNo().getFclContactNumber();
}
}
List list=new ArrayList();
DBUtil dbutil=new DBUtil();
/*if(voyageExport!=null){
	list=dbutil.getFCLDetails(voyageExport.getOriginTerminal(),voyageExport.getDestinationPort(),voyageExport.getComNum(),voyageExport.getLineNo());
		if(session.getAttribute("exist")==null){
		if(list!=null&&list.size()>0){
		
			sslineno="";
			sslinename="";
			comCode="";
			comDesc="";
				}
				}
		}*/
String address="";
if(session.getAttribute("exist")!=null){
session.removeAttribute("exist");
}
if(request.getAttribute("sendfclcontrol")!=null)
{
address=(String)request.getAttribute("sendfclcontrol");
}
if(request.getAttribute("msg")!=null)
{
	msg=(String)request.getAttribute("msg");
}
/*if(request.getAttribute("update")!=null)
{
	message=(String)request.getAttribute("update");
	}*/
if(request.getAttribute("update")!=null){
	message=(String)request.getAttribute("update");
}
if(request.getParameter("modify")!= null)
{
	session.setAttribute("modifyforrelay",request.getParameter("modify"));
 	modify=(String)session.getAttribute("modifyforrelay");
}
else
{
 	modify=(String)session.getAttribute("modifyforrelay");
}
if(!terminalNumber.equals("") && !destSheduleNumber.equals("") && !sslineno.equals(""))
{
if(message.equals("")){

if(address.equals("sendfclcontrol"))
{
%>
	<script>
	       
	       
	       parent.parent.getVoyageExport();
	       parent.parent.close();
	       
			//self.close();
		//opener.location.href="<%=path%>/jsps/voyagemanagement/exportVoyage.jsp";
	</script>
<%
}}
}
%>


 
<html> 
	<head>
		<title>JSP for VoyagePopUpForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
		<script language="javascript" type="text/javascript">
		 function popup1(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=500,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			//document.voyagePopUpForm.buttonValue.value="search";
			//document.voyagePopUpForm.submit();
			return false;
		}
		function searchform()
		{
		//alert("inside");
		 if(document.voyagePopUpForm.terminalNumber.value=="")
		 {
		 alert("Please select Terminal Number");
		 return;
		 }
		
		 if(document.voyagePopUpForm.destSheduleNumber.value=="")
		 {
		 alert("Please select Destination Airport Code");
		 return;
		 }
		
		 if(document.voyagePopUpForm.sslinenumber.value=="")
		 {
		 alert("Please select SS Line Number Code");
		 return;
		 }
			document.voyagePopUpForm.buttonValue.value="search";
			document.voyagePopUpForm.submit();
		}
		
		
	 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.voyagePopUpForm.terminalName.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
	    document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		
		document.voyagePopUpForm.terminalName.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.voyagePopUpForm.terminalNumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
	    document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.voyagePopUpForm.terminalNumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.voyagePopUpForm.destSheduleNumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
	    document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.voyagePopUpForm.destSheduleNumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	 }
	 function getDestination(ev)
	{
		if(event.keyCode==9){
		document.voyagePopUpForm.destAirportname.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
	    document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.voyagePopUpForm.destAirportname.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getsslineno(ev)
	{
	  if(event.keyCode==9){
		document.voyagePopUpForm.sslinename.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	
	}
	}
	
	function getsslinenopress(ev)
	{
	  if(event.keyCode==13){
		document.voyagePopUpForm.sslinename.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	
	}
	}
	 
	 function getsslinename(ev)
	 {
	  if(event.keyCode==9){
		document.voyagePopUpForm.sslinenumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	
	}
	 }
	 function getsslinenamepress(ev)
	 {
	  if(event.keyCode==13){
		document.voyagePopUpForm.sslinenumber.value="";
		document.voyagePopUpForm.buttonValue.value="popupsearch";
		document.voyagePopUpForm.submit();
	
	}
	 }
	    function getOriginalTerminal(ev){  
	    document.getElementById("terminalName").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.voyagePopUpForm.terminalNumber.value;
			
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
		   
		   	       function getOriginalTerminalName(ev){
		   	       document.getElementById("terminalNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "OrgTerminalNumber";
				 params['terminalName'] = document.voyagePopUpForm.terminalName.value;
		
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
		   
		    function getPortName(ev){
		    document.getElementById("destAirportname").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DestAirportname";
				 params['scheduleNumber'] = document.voyagePopUpForm.destSheduleNumber.value;
		
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
		   		document.getElementById("destAirportname").value=data.destAirportname;
		   	}
		   }
		   
		   	         function getScheduleNumber(ev){
		   	         document.getElementById("destSheduleNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "ScheduleNumber";
				 params['destAirportName'] = document.voyagePopUpForm.destAirportname.value;
		
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			 
				 dojo.event.connect(req, "load", this, "populateScheduleNumber");
				
			    }
			 }
			 
		   function populateScheduleNumber(type, data, evt) {
		   	if(data){
		   		document.getElementById("destSheduleNumber").value=data.destSheduleNumber;
		   	}
		   }
		   
		   function getSslineName(ev){
		   document.getElementById("sslinename").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineName";
				 params['sslinenumber'] = document.voyagePopUpForm.sslinenumber.value;
		
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			 
				 dojo.event.connect(req, "load", this, "populateSslineName");
				
			    }
			 }
			 
		   function populateSslineName(type, data, evt) {
		   	if(data){
		   		document.getElementById("sslinename").value=data.sslinename;
		   	}
		   }
		   
		       function getSslineNumber(ev){
		       document.getElementById("sslinenumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineNumber";
				 params['sslinename'] = document.voyagePopUpForm.sslinename.value;
		
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			 
				 dojo.event.connect(req, "load", this, "populateSslineNumber");
				
			    }
			 }
			 
		   function populateSslineNumber(type, data, evt) {
		   	if(data){
		   		document.getElementById("sslinenumber").value=data.sslinenumber;
		   	}
		   }
		   
	
		
	</script>
		
	</head>
	<body class="whitebackgrnd">
		<html:form action="/voyagePopUp" scope="request">
			<font color="blue"><b><%=message%></b></font>
<table width="100%" class="tableBorderNew" border="0">
  <tr class="tableHeadingNew"> Add New</tr>
  <tr>
    <td>
       
    			
			<table width=100%  border="0" cellpadding="0" cellspacing="0">
			<tr>
    			<td><table width="100%" border="0"><tr class="style2"><td>Org Trm &nbsp;</td></tr></table></td>
    			<td>

    			  <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getOriginalTerminal(this.value)"   value="<%=terminalNumber %>" maxlength="2"/>    
    		   <dojo:autoComplete formId="voyagePopUpForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=EXPORT_VOYAGE&from=0"/>                
    			</td>
    			
    			<td><span class="textlabels">Org Trm Name</span></td>
    			<td>

    			<input name="terminalName" id="terminalName"  onkeydown="getOriginalTerminalName(this.value)"  value="<%=terminalName %>" size="15" />    
    			   <dojo:autoComplete formId="voyagePopUpForm" textboxId="terminalName" action="<%=path%>/actions/getTerminalName.jsp?tabName=EXPORT_VOYAGE&from=0"/>
    			</td>
    		</tr>
    		<tr>	
    			<td class="textlabels"><table width="100%" border="0"><tr class="style2"><td>POD No&nbsp;</td></tr></table> </td>
    			<td>

    			<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getPortName(this.value)"  value="<%=destSheduleNumber %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="voyagePopUpForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=0"/>
    			</td>
    			
    			<td><span class="textlabels">POD Name</span></td>
    			<td>

    			<input name="destAirportname" id="destAirportname" size="15" onkeydown="getScheduleNumber(this.value)"  value="<%=destAirportname %>" />    
    		    <dojo:autoComplete formId="voyagePopUpForm" textboxId="destAirportname" action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=1"/>
    			</td>
    			
    		</tr>
    		
    		<tr>	
    			<td><table width="100%" border="0"><tr class="style2"><td>SS Line Number&nbsp;</td></tr></table> </td>
    			<td>

    			<input name="sslinenumber" id="sslinenumber" size="15" maxlength="5" value="<%=sslineno%>" onkeydown="getSslineName(this.value)" />
    		    <dojo:autoComplete formId="voyagePopUpForm" textboxId="sslinenumber" action="<%=path%>/actions/getSsLineNo.jsp?tabName=EXPORT_VOYAGE&from=0"/>
    			</td>
    			
    			<td class="textlabels">SS Line Name </td>
    			<td>

    			<input name="sslinename" id="sslinename" size="15" value="<%=sslinename%>" onkeydown="getSslineNumber(this.value)" />
    			<dojo:autoComplete formId="voyagePopUpForm" textboxId="sslinename" action="<%=path%>/actions/getSsLineName.jsp?tabName=EXPORT_VOYAGE&from=0"/>
    			</td>
    		</tr>
    		
    		
    		<tr>
    		<td>&nbsp;</td>
    		<td>&nbsp;</td>
    		<td>
           		 <input type="button" value="Submit" onclick="searchform()" class="buttonStyleNew" />
           		
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    </td>
  </tr>
</table>		
    		<html:hidden property="buttonValue" styleId="buttonValue" />

		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


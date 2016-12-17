<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayInquiry,com.gp.cong.logisoft.util.DBUtil" />
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String buttonValue = "load";
String message="";
String poldesc = "";
	String polid = "";
	String poddesc = "";
	String podid = "";
	DBUtil dbUtil = new DBUtil();
	if(request.getParameter("relay")!=null && request.getParameter("relay").equals("add"))
	{
		if(session.getAttribute("relaycode")!=null)
		{
 			session.removeAttribute("relaycode");
		}
	}
if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String rlcode="";
if(request.getAttribute("rcode")!=null)
{
rlcode=(String)request.getAttribute("rcode");
}
RelayInquiry relay = new RelayInquiry();
if(session.getAttribute("relaycode")!=null)
{
 relay=(RelayInquiry)session.getAttribute("relaycode");
}
if (relay != null && relay.getPolCode() != null) {
			poldesc = relay.getPolCode().getPortname();
			
		if(relay.getPolCode()!=null)
		{
			polid = relay.getPolCode().getShedulenumber();
			
		}
			
		}
		if (relay != null && relay.getPodCode() != null) {
			poddesc = relay.getPodCode().getPortname();
			if(relay.getPodCode()!=null)
		{
			 podid= relay.getPodCode().getShedulenumber();
		}
				
		}
if(rlcode.equals("addrelay"))
{
%>
	<script>

	parent.parent.GB_hide();
		parent.parent.test();
		
		//self.close();
		//opener.location.href="<%=path%>/jsps/datareference/addRelay.jsp";
	</script>
<%
}
%>
 
<html> 
	<head>
		<title>JSP for RelayCodeForm form</title>
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

		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.relayCodeForm.pol.value=="")
		{
		alert("Please enter the POL Code");
		return;
		}
		if(document.relayCodeForm.pod.value=="")
		{
		alert("Please enter the POD Code");
		return;
		}
		
		document.relayCodeForm.buttonValue.value="search";
		document.relayCodeForm.submit();
		}
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
	function submit1()
    {
   
    	document.relayCodeForm.buttonValue.value="polselected";
  	 	document.relayCodeForm.submit();
    }	
	function submit2()
    {
     	document.relayCodeForm.buttonValue.value="podselected";
  	 	document.relayCodeForm.submit();
    }	
    function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
mywindow.moveTo(200,180);

          document.relayCodeForm.submit();
return false;
}
function getPolName(ev){ 
	document.getElementById("polText").value="";
			if(event.keyCode==9 || event.keyCode==13){
			   
				 var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.relayCodeForm.pol.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 
				 var req = dojo.io.bind(bindArgs);
			 
				 dojo.event.connect(req, "load", this, "populatePolName");
				
			    }
			 }
			 
		   function populatePolName(type, data, evt) {
		   	if(data){
		   		document.getElementById("polText").value=data.portname;
		   	
		   	}
		   }
		function getPortofDischarge(ev){ 
		    document.getElementById("podText").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.relayCodeForm.pod.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePodName");
				
			    }
			 }
			 
		   function populatePodName(type, data, evt) {
		   	if(data){
		   		document.getElementById("podText").value=data.portname;
		   	
		   	}
		   }
		   function getPolValue(ev){ 
		   document.getElementById("pol").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "DestinationPortNumberInCodes";
				 params['destinationPortName'] = document.relayCodeForm.polText.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePolValue");
				
			    }
			 }
			 
		   function populatePolValue(type, data, evt) {
		   	if(data){
		   		document.getElementById("pol").value=data.DestinationNumberInCodes;
		   	
		   	}
		   }
		   	   function getPodValue(ev){ 
			if(event.keyCode==9 || event.keyCode==13){
			     document.getElementById("pod").value="";
			     var params = new Array();
				 params['requestFor'] = "DestinationPortNumberInCodes";
				 params['destinationPortName'] = document.relayCodeForm.podText.value;
			     var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
			  
				 dojo.event.connect(req, "load", this, "populatePodValue");
				
			    }
			 }
			 
		   function populatePodValue(type, data, evt) {
		   	if(data){
		   		document.getElementById("pod").value=data.DestinationNumberInCodes;
		   	
		   	}
		   }
</script>
	<%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd">
<html:form action="/relayCode" scope="request">
<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr><td><font color="blue" size="2"><%=message%></font></td></tr>
<tr class="tableHeadingNew"> Add POL and POD</tr>
<tr>
<td>
<table width="100%"  border="0" cellpadding="3" cellspacing="0">
<tr>
    <td class="textlabels">POL</td>
	<td><input  name="pol" id="pol" value="<%=polid%>" onkeydown="getPolName(this.value)" style="width:215px"/>   
	    <dojo:autoComplete formId="relayCodeForm" textboxId="pol" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=0"/></td>	
	<td class="style2" >POL Name </td>
	<td><input  name="polText" id="polText" value="<%=poldesc%>" onkeydown="getPolValue(this.value)" style="width:215px"/>   
	    <dojo:autoComplete formId="relayCodeForm" textboxId="polText" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=1"/></td>
    </tr>
	<tr>
	<td class="textlabels">POD</td>
	<td><input  name="pod" id="pod" value="<%=podid%>" onkeydown="getPortofDischarge(this.value)" style="width:215px"/>   
	    <dojo:autoComplete formId="relayCodeForm" textboxId="pod" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=2"/></td>
	<td class="style2">POD Name </td>
	<td><input  name="podText" id="podText" value="<%=poddesc%>" onkeydown="getPodValue(this.value)" style="width:215px"/>   
	    <dojo:autoComplete formId="relayCodeForm" textboxId="podText" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_CODE&from=3"/></td>
	</tr>
	<tr>
 	<td colspan="4" align="center"><input type="button" class="buttonStyleNew" id="search" value="Submit" onclick="searchform()"/>
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




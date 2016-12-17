<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.Ports"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayDestination"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List portCodeList=new ArrayList();
List portRelayList=new ArrayList();
Ports portsobjs=null;
String buttonValue="load";
String destinationname="";
String destinationid="";
String ttFromPodToFd="";
String adddest="";
String msg="";
if(request.getAttribute("msg")!=null) {
msg=(String)request.getAttribute("msg");
}
if(request.getParameter("addDestination")!=null && request.getParameter("addDestination").equals("list")) {
adddest=(String)request.getParameter("addDestination");
}
if(request.getAttribute("buttonValue")!=null) {
   buttonValue=(String)request.getAttribute("buttonValue");
    
}
if (session.getAttribute("destinationList") != null) {
			portRelayList = (List)session.getAttribute("destinationList");
		}
if(buttonValue.equals("add") ||buttonValue.equals("update")|| buttonValue.equals("delete"))
{
%>
	<script>
		self.close();
		opener.location.href="<%=path%>/jsps/datareference/addRelay.jsp";
	</script>
<%}
RelayDestination relayDestination=new RelayDestination();
if(session.getAttribute("relayDestination")!=null) {
   			relayDestination=(RelayDestination)session.getAttribute("relayDestination");
   			if(relayDestination!=null && relayDestination.getDestinationId()!=null) {
   				destinationname=relayDestination.getDestinationId().getPortname();
   				if(relayDestination.getDestinationId()!=null) {
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				
   				}
   			}
   			if(relayDestination!=null  && relayDestination.getTtFromPodToFd()!=null) {
				 			ttFromPodToFd=relayDestination.getTtFromPodToFd().toString();
					}
   		}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%@include file="../includes/resources.jsp" %>
		<title>Air </title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
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
		function submit1() { 
 			if(IsNumeric(document.addDestination.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.addDestination.ttToPol.value="";
    				document.addDestination.ttToPol.focus();
   					return;
  				} 
 			document.addDestination.buttonValue.value="destinationselected";
  			document.addDestination.submit();
  			
   		}
   		function cancel() {
   			
   			document.addDestination.buttonValue.value="cancel";
  			document.addDestination.submit();
  			window.close();
   		}
   		function add(val1) {
   		if(val1=="") {
   			if(document.addDestination.destinationCode.value=="0") {
						alert("Please select Destination Code");
						document.addDestination.destinationCode.value="";
						document.addDestination.destinationCode.focus();
						return;
					}
   			 	
  				}
   		if(IsNumeric(document.addDestination.ttToPol.value)==false) {
   					alert("Transit Time to POL should be Numeric.");
  					document.addDestination.ttToPol.value="";
    				document.addDestination.ttToPol.focus();
   					return;
  				} 
   		if(val1=="") {
   			document.addDestination.buttonValue.value="add";
   		} else if(val1=="list") {
   		document.addDestination.buttonValue.value="update";
   		}
  			document.addDestination.submit();
   		}
   		function disabled(val) {
   		if(val=="list") {
   		var select =document.addDestination.destinationCode.value;
	 	select.disabled = true;
   		var y=document.addDestination.destinationName.value;
   		y.disabled=true;
   		}
  		}
  	function confirmdestdelete() {
		document.addDestination.buttonValue.value="delete";
        var result = confirm("Are you sure you want to delete this Destination ");
		if(result) {
   			document.addDestination.submit();
   		}	
   	}	
function popup1(mylink, windowname) {
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
document.addDestination.submit();
return false;
}
   		function getPortofDischarge(ev){ 
		    document.getElementById("destinationName").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.addDestination.destinationCode.value;
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
		   		document.getElementById("destinationName").value=data.portname;
		   	}
		   }	
		</script>
 </head>
<body class="whitebackgrnd">
<html:form action="/addDestination" name="addDestination" type="com.gp.cong.logisoft.struts.form.AddDestinationForm" scope="request">

<font color="blue" size="4"><%=msg%></font>
<table width="100%" height="57" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"><td>Destination Details</td>
	<td align="right">		
 			<%if(adddest.equals("list")) { %>
			<input type="button" class="buttonStyleNew" id="save" value="Save" onclick="add('<%=adddest%>');"/>
			<%} else { %>
             <input type="button" class="buttonStyleNew" id="save" value="Add" onclick="add('<%=adddest%>');"/>
			<%} %>
			<%if(adddest.equals("list")) { %>
			<input type="button" class="buttonStyleNew" id="delete" value="Delete" onclick="confirmdestdelete()"/>
			<%} %>
  		</td>
	</tr>
	<tr><td colspan="2"><table width="100%" border="0" cellpadding="3" cellspacing="0" >
  		<%if(relayDestination!=null && relayDestination.getDestinationId()!=null){
   				if(adddest.equals("list")){
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				}else{
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				}
   		} %>
   		
		<tr class="textlabels">
        <td>Destination Code*</td>
  		<%if(!adddest.equals("list") && !buttonValue.equals("update")) { %>
			<td><input  name="destinationCode" id="destinationCode" value="<%=destinationid%>"  onkeydown="getPortofDischarge(this.value)" />   
	     		<dojo:autoComplete formId="addDestination" textboxId="destinationCode" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_INQUIRY"/>
  			</td>
           	<%}
           	if(adddest.equals("list"))
           	{ %>
           	<td><html:text property="destinationCode" value="<%=destinationid%>" readonly="true" styleClass="varysizeareahighlightgrey"></html:text></td>
           	<%} %>
  			<td class="textlabels">Destination Name</td>
 			<td><html:text property="destinationName" value="<%=destinationname%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
		</tr>
		<tr >
  			<td class="textlabels">Transit Time to POL</td>
  			<td><html:text property="ttToPol" maxlength="2" value="<%=ttFromPodToFd%>"/></td>
  			<td class="textlabels"></td>
  			<td></td>
  		</tr>
</table>
</td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<script>disabled('<%=adddest%>')</script>
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

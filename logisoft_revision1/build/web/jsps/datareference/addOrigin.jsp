<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RelayOrigin"/>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String edit=(String)request.getAttribute("edit");

String msg="";
if(request.getAttribute("msg")!=null)
{														
msg=(String)request.getAttribute("msg");
}
String buttonValue="load";
String originname="";
String originid="";
String ttToPol="";
String cutOffDays="";
String cutOffTime="";

List portRelayList=new ArrayList();
String week="0";
String addorigin="";

if(request.getParameter("addorigin")!=null && request.getParameter("addorigin").equals("list"))
{
addorigin=(String)request.getParameter("addorigin");
}
if(request.getAttribute("buttonValue")!=null)
{
   buttonValue=(String)request.getAttribute("buttonValue");
}
if(buttonValue.equals("add") || buttonValue.equals("update") || buttonValue.equals("delete"))
{
%>
	<script>
		self.close();
		opener.location.href="<%=path%>/jsps/datareference/addRelay.jsp";
	</script>
<%
}

if (session.getAttribute("originList") != null) {
			portRelayList = (List)session.getAttribute("originList");
			request.setAttribute("portRelayList",portRelayList);
		}
		

RelayOrigin relayOrigin=null;

 
if(session.getAttribute("relayOrigin")!=null)
		{
   			relayOrigin=(RelayOrigin)session.getAttribute("relayOrigin");
   			
   			if(relayOrigin!=null && relayOrigin.getOriginId()!=null)
   			{
   				originname=relayOrigin.getOriginId().getPortname();
   				if(relayOrigin.getOriginId()!=null)
   				{
   					originid = relayOrigin.getOriginId().getShedulenumber();
   					
   				
   				}
   				if(relayOrigin.getCutOffDayOfWeek()!=null && relayOrigin.getCutOffDayOfWeek().getId()!=null)
   				{
   				week=relayOrigin.getCutOffDayOfWeek().getId().toString();
   				}
   			}
   			
				if(relayOrigin!=null  && relayOrigin.getTtToPol()!=null)
					{
				 			ttToPol=relayOrigin.getTtToPol().toString();
					}
			if(relayOrigin.getCutOffDayOfWeek()!=null)
					{
							cutOffDays=relayOrigin.getCutOffDayOfWeek().toString();
					}
			if(relayOrigin.getCutOffTime()!=null)
					{
							cutOffTime=relayOrigin.getCutOffTime().toString();
					}		
	
   		}
 request.setAttribute("weekList",dbUtil.getWeekList(new Integer(25),"yes","Select Week")); 			
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
		<title>Air </title>
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
		function submit1()
 		{ 
 				if(IsNumeric(document.addOrigin.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.addOrigin.ttToPol.value="";
    				document.addOrigin.ttToPol.focus();
   					return;
  				} 
  				if(IsNumeric(document.addOrigin.cutOffDayOfWeek.value)==false)
  				{
   					alert("Cut off days of week should be Numeric.");
  					document.addOrigin.cutOffDayOfWeek.value="";
    				document.addOrigin.cutOffDayOfWeek.focus();
   					return;
  				} 
  				if(IsNumeric(document.addOrigin.cutOffTime.value)==false)
  				{
   					alert("Cut off time should be Numeric.");
  					document.addOrigin.cutOffTime.value="";
    				document.addOrigin.cutOffTime.focus();
   					return;
  				} 
  				
 			document.addOrigin.buttonValue.value="originselected";
  			document.addOrigin.submit();
  			
   		}
   		
   		function cancel()
   		{
   			document.addOrigin.buttonValue.value="cancel";
  			document.addOrigin.submit();
  			window.close();
   		}
   		function add(val1)
   		{
   		
   		if(val1=="")
   		{
   				if(document.addOrigin.originCode.value=="0")
					{
						alert("Please select Origin Code");
						document.addOrigin.originCode.value="";
						document.addOrigin.originCode.focus();
						return;
					}
   		}
   		if(IsNumeric(document.addOrigin.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.addOrigin.ttToPol.value="";
    				document.addOrigin.ttToPol.focus();
   					return;
  				} 
  				if(IsNumeric(document.addOrigin.cutOffDayOfWeek.value)==false)
  				{
   					alert("Cut off days of week should be Numeric.");
  					document.addOrigin.cutOffDayOfWeek.value="";
    				document.addOrigin.cutOffDayOfWeek.focus();
   					return;
  				} 
  				if(IsNumeric(document.addOrigin.cutOffTime.value)==false)
  				{
   					alert("Cut off time should be Numeric.");
  					document.addOrigin.cutOffTime.value="";
    				document.addOrigin.cutOffTime.focus();
   					return;
  				} 
  				
   		if(val1=="")
   		{
   				
   			document.addOrigin.buttonValue.value="add";
   		}
   		else if(val1=="list")
   		{
   			document.addOrigin.buttonValue.value="update";
   		}
  			document.addOrigin.submit();
   		}
   		function disabled(val)
   		{
   		if(val=="list")
   		{
   		var select = document.addOrigin.originCode.value;
	 	select.disabled = true;
   		var y=document.addOrigin.originName.value;
   		y.disabled=true;
   		}
  		}
   		
   		function confirmdelete()
	{
	
		document.addOrigin.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this origin ");
		if(result)
		{
   			document.addOrigin.submit();
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
window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
document.addOrigin.submit();

return false;
}
 			function getPortofDischarge(ev){ 
		    document.getElementById("originName").value="";
			if(event.keyCode==9 || event.keyCode==13){
			     var params = new Array();
				 params['requestFor'] = "ScheduleCode";
				 params['scheduleCode'] = document.addOrigin.originCode.value;
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
		   		document.getElementById("originName").value=data.portname;
		   	
		   	}
		   }	
		</script>
		<%@include file="../includes/resources.jsp" %>
 </head>
<body class="whitebackgrnd" >
<html:form action="/addOrigin" name="addOrigin" type="com.gp.cong.logisoft.struts.form.AddOriginForm" scope="request">
<font color="blue" size="4"><%=msg%></font>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  		<tr class="tableHeadingNew"><td>Origin Details</td>
            <td align="right"><%if(addorigin.equals("list")) { %>
			<input type="button" class="buttonStyleNew" id="save" value="Save" onclick="add('<%=addorigin%>')"/>
			<%} else { %>
 			<input type="button" class="buttonStyleNew" id="save" value="Add" onclick="add('<%=addorigin%>');"/>
			<%} %>
			<% if(addorigin.equals("list")) { %>
 			<input type="button" class="buttonStyleNew" id="Delete" value="Delete" onclick="confirmdelete()"/>
			<%} %></td>
 			</tr>
 			<tr>
 			<td colspan="2">
 			<table width="100%" border="0" cellpadding="3" cellspacing="0" >
  		<%
  				if(relayOrigin!=null && relayOrigin.getOriginId()!=null)
   				{
   				if(addorigin.equals("list"))
   				{
   				originid = relayOrigin.getOriginId().getShedulenumber();
   				}
   				else
   				{
   				originid = relayOrigin.getOriginId().getShedulenumber();
   				}
   				}
   				
   				 %>
		<tr class="textlabels">
		<td>Origin Code*&nbsp;</td>
  			<%  if(!addorigin.equals("list") && !buttonValue.equals("update")) 	{ %>
           	<td><input  name="originCode" id="originCode" value="<%=originid%>" onkeydown="getPortofDischarge(this.value)" />   
	     	  <dojo:autoComplete formId="addOrigin" textboxId="originCode" action="<%=path%>/actions/getPorts.jsp?tabName=RELAY_INQUIRY"/>
  			</td>
           	<% } if(addorigin.equals("list")) 	{ %>
           	 <td><html:text property="originCode" value="<%=originid%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
           	 <%} %>
  			<td class="textlabels">Origin Name</td>
 			<td><html:text property="originName" value="<%=originname%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
		</tr>
		<tr >
  			<td class="textlabels">Transit Time to POL</td>
  			<td><html:text property="ttToPol" maxlength="2"   value="<%=ttToPol%>"   /></td>
  			<td class="textlabels">Cutoff Day of Week*</td>
  			<td><html:select property="cutOffDayOfWeek"  styleClass="selectboxstyle" value="<%=week%>">
           		<html:optionsCollection name="weekList"/>	
           	</html:select></td>
  		</tr>
		<tr>
 			 <td class="textlabels">Cutoff Time</td>
   			 <td><html:text property="cutOffTime" maxlength="2"  value="<%=cutOffTime%>" /></td>
   			 <td>&nbsp;</td>
    		 <td>&nbsp;</td>
  		</tr>
</table>
</td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<script>disabled('<%=addorigin%>')</script>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.PortsTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


List portList=new ArrayList();
PortsTemp portsobjs=null;
String portname="";
String shedulenumber="";
String eciportcode="";
String msg="";

if(request.getAttribute("msg")!=null)
{
msg=(String)request.getAttribute("msg");
}
if(session.getAttribute("portLst") != null)
{
	portList=(List)session.getAttribute("portLst");
}

if(session.getAttribute("portsobj") != null)
{
 portsobjs =(PortsTemp) session.getAttribute("portsobj");
 shedulenumber=portsobjs.getShedulenumber();
 portname=portsobjs.getPortname();	
 eciportcode=portsobjs.getEciportcode();
}
if(request.getAttribute("buttonValue") != null && request.getAttribute("buttonValue").equals("add") )
{
portname="";
shedulenumber="";
}
String modify = null;
if(session.getAttribute("airocean")!=null)
{
modify=(String)session.getAttribute("airocean");
}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
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
		
		function submit1()
 		{ 
 			document.airPortExceptionForm.buttonvalue.value="portselected";
  			document.airPortExceptionForm.submit();
   		}
   		
   		function addports()
   		{
   		if(document.airPortExceptionForm.portcode.value=="")
   		{
   		alert("Please select the port code");
   		return;
   		}
   		
   			document.airPortExceptionForm.buttonvalue.value="add";
  			document.airPortExceptionForm.submit();
   		}
   		
  		function confirmdelete(obj)
		{
    		
     		var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('portstable').rows[rowindex].cells;	
			document.airPortExceptionForm.index.value=obj.name;
    		document.airPortExceptionForm.buttonvalue.value="delete";
   		    var result = confirm("Are you sure you want to delete this port "+x[1].innerHTML);
			if(result)
			{
   				document.airPortExceptionForm.submit();
   			}	
		}
   		
   		 function disabled(val)
   {
    
	if( val!=null && val!='' && (val == 3 || val == 0) )
	{		
      
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		 if(imgs[k].id == "add" ||imgs[k].id=="delete")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue")
	 		
	  		{
	  		   input[i].disabled = true;
	  		}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled = true;
	 	}
	 	document.getElementById("add").style.visibility = 'hidden';
	 	document.getElementById("delete").style.visibility = 'hidden';
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


return false;
}
   	
function getPortName(ev)
{   	
    if(event.keyCode==9 || event.keyCode==13){
	var params = new Array();
	params['requestFor'] = "ScheduleCode";
	params['scheduleCode'] = document.airPortExceptionForm.portcode.value;
	var bindArgs = {
	  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  error: function(type, data, evt){alert("error");},
	  mimetype: "text/json",
	  content: params
	};
	var req = dojo.io.bind(bindArgs);
	 dojo.event.connect(req, "load", this, "populatePortName");
	}
}
 function populatePortName(type, data, evt) {
  	if(data){
   		document.getElementById("portname").value=data.portname;
   		if(data.eciportcode){
   		document.getElementById("shedulecode").value=data.eciportcode;
   		}else{
   		document.getElementById("shedulecode").value="";
   		}
   		
   	}
}  		
		</script>
 </head>
 <body class="whitebackgrnd" >
<html:form action="/airPortException" scope="request">
<font color="blue" size="4"><%=msg%></font>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	
	
	<tr class="tableHeadingNew">
  		AIR PORT EXCEPTION 
	</tr>
<TR>
	<td class="style2">Schedule Code*&nbsp;
	</td>
    <td width="100">
           <input  name="portcode" id="portcode" value="<%=shedulenumber%>"  onkeydown="getPortName(this.value)" />   
	        <dojo:autoComplete formId="airPortExceptionForm" textboxId="portcode" action="<%=path%>/actions/getPorts.jsp?tabName=CARRIER"/>
</td>
    

 <tr class="style2">  	
   	<td width="40%">Port Name</td>
   	<td width="100"><html:text property="portname" value="<%=portname%>" readonly="true"/></td>
 </tr>  
<tr class="style2">  	
   	<td width="100">ECI Port Code</td>
   	<td width="100"><html:text property="shedulecode" value="<%=eciportcode %>" readonly="true"/></td>
</tr>  
<tr><td colspan="3" align="center" >
<input type="button" class="buttonStyleNew" value="Add" name="add"  onclick="addports()"/></td></tr>
</table><br>
<%if(portList!=null && portList.size()>0)
{ %>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
   <tr class="tableHeadingNew">  List of AirPorts </tr>

   <tr>
		<td>
		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:300px;">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
         
        <display:table name="<%=portList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portstable">
        <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"></display:setProperty> 
        <display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="Printer"/>
  		<display:setProperty name="paging.banner.items_name" value="Printers"/>
  		
	    <display:column property="shedulenumber" title="Shedule Code" />
	    
		<display:column property="portname" title="Port Name" />
		
		<display:column property="eciportcode" title="ECI Port Code" />
		 
	    <display:column>
	    <input type="button" class="buttonStyleNew" value="Delete" name="<%=i%>"  id="delete" onclick="confirmdelete(this)"/>
<%--	    <img name="<%=i%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" id="delete" onclick="confirmdelete(this)"/></display:column>--%>
  		</display:column>
  		<% i++;%>
		</display:table>
        </table></div>   
    	</td> 
   </tr>  
 </table>
 <%} %>

<html:hidden property="buttonvalue" styleId="buttonvalue"/>
<html:hidden property="index" />
<script>disabled('<%=modify%>')</script>
</html:form>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


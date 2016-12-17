<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.PortsTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


List portcodeList=new ArrayList();
List portList=new ArrayList();
PortsTemp portsobjs=null;
String portname="";
String eciportcode="";
String shedulenumber="";
String modify="";
String msg="";
if(request.getAttribute("msg")!=null)
{
msg=(String)request.getAttribute("msg");
}
List portlst=new ArrayList();
if(session.getAttribute("portList1") != null)
{
	portList=(List)session.getAttribute("portList1");
}
if(session.getAttribute("portlst") != null)
{
portlst=(List)session.getAttribute("portlst");
}
if(portcodeList != null)
{
//portcodeList=dbUtil.getPortCodeList(portlst);
//request.setAttribute("portcodeList",portcodeList);
}
if(session.getAttribute("porttempobj") != null)
{
 portsobjs =(PortsTemp) session.getAttribute("porttempobj");
 shedulenumber=portsobjs.getShedulenumber();
 portname=portsobjs.getPortname();	
 eciportcode=portsobjs.getEciportcode();
}
if(request.getAttribute("buttonValue") != null && request.getAttribute("buttonValue").equals("add") )
{
portname="";
shedulenumber="";
eciportcode="";
}

modify = (String) session.getAttribute("modifyforports");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
		<title>Air </title>
<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script language="javascript" type="text/javascript">
		function submit1()
 		{ 
 			document.associatedPortForm.buttonvalue.value="portselected";
  			document.associatedPortForm.submit();
   		}
   		
   		function addports()
   		{
   		if(document.associatedPortForm.shedulecode.value=="")
   		{
   		alert("Please select the Schedule code");
   		return;
   		}
   			document.associatedPortForm.buttonvalue.value="add";
  			document.associatedPortForm.submit();
   		}
   		
  		function confirmdelete(obj)
		{
    		
     		var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('portstable').rows[rowindex].cells;	
			document.associatedPortForm.index.value=obj.name;
    		document.associatedPortForm.buttonvalue.value="delete";
   		    var result = confirm("Are you sure you want to delete this port "+x[1].innerHTML);
			if(result)
			{
   				document.associatedPortForm.submit();
   			}	
		}
   		
   	function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		if(imgs[k].id=="add")
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
		</script>
 </head>
 <body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/associatedPort" scope="request">
		<font color="blue" size="4"><%=msg%></font>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew">Associated Ports</tr>
<td>

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
<tr height="8"></tr>
<tr class="style2">
	    	 	<td><table width="100%" border="0">
	    	 	<tr class="style2">
<td>Schedule Code&nbsp;</td></tr></table></td>
    <td><html:text property="shedulecode" readonly="true" value="<%=shedulenumber%>"></html:text>
<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'associatedport','windows')">
    </td>
    <td>&nbsp;</td>
    <td>
    	<input type="button" class="buttonStyleNew" id="add" value="Submit"  onclick="addports()"/>  
   
 </tr>

 <tr class="style2">  	
   	<td>Port Name</td>
   	<td><html:text property="portname" value="<%=portname%>" readonly="true"/></td>
</tr>  
	
<tr class="style2">  	
   	<td>ECI Port Code</td>
   	<td><html:text property="portcode" value="<%=eciportcode%>" readonly="true"/></td>
</tr>  
<tr height="8"></tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">

   <tr class="tableHeadingNew">List of AirPorts
        
   </tr>
  
   <tr>
		<td>
		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
        %>
        <display:table name="<%=portList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portstable" style="width:100%">
        <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"></display:setProperty> 
        <display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="Ports"/>
  		<display:setProperty name="paging.banner.items_name" value="Ports"/>
	    <display:column property="shedulenumber" title="Shedule Code" />
	    <display:column/>
		<display:column property="portname" title="Port Name" />
		<display:column/>
		<display:column property="eciportcode" title="ECI Port Code" />
		<display:column/>
	    <display:column><img name="<%=i%>" src="<%=path%>/img/toolBar_delete_hover.gif" border="0" id="delete" onclick="confirmdelete(this)"/></display:column>
  		<% i++;%>
		</display:table>
        </table></div>   
    	</td> 
   </tr>  
 </table>
 </table>
<html:hidden property="buttonvalue" styleId="buttonvalue"/>
<html:hidden property="index" />
</html:form>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



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
if(request.getAttribute("msg")!=null)
{
msg=(String)request.getAttribute("msg");
}
if(request.getParameter("editdestination")!=null && request.getParameter("editdestination").equals("list"))
{
adddest=(String)request.getParameter("editdestination");
}
if(request.getAttribute("buttonValue")!=null)
{
   buttonValue=(String)request.getAttribute("buttonValue");
    
}
if (session.getAttribute("destinationList") != null) {
			portRelayList = (List)session.getAttribute("destinationList");
		}

	
if(buttonValue.equals("add")||buttonValue.equals("update")|| buttonValue.equals("delete"))
{
%>
	<script>
		self.close();
		opener.location.href="<%=path%>/jsps/datareference/editRelay.jsp";
	</script>
<%}
RelayDestination relayDestination=new RelayDestination();


if(session.getAttribute("relayDestination")!=null)
		{
   			relayDestination=(RelayDestination)session.getAttribute("relayDestination");
   			  
   			if(relayDestination!=null && relayDestination.getDestinationId()!=null)
   			{
   				destinationname=relayDestination.getDestinationId().getPortname();
   				if(relayDestination.getDestinationId()!=null)
   				{
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				}
   			}
   			if(relayDestination!=null  && relayDestination.getTtFromPodToFd()!=null)
					{
				 			ttFromPodToFd=relayDestination.getTtFromPodToFd().toString();
					}
   		}
  	 String modify="";
  	 if (session.getAttribute("view") != null)
	{
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
 			if(IsNumeric(document.editDestination.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.editDestination.ttToPol.value="";
    				document.editDestination.ttToPol.focus();
   					return;
  				} 
 			document.editDestination.buttonValue.value="destinationselected";
  			document.editDestination.submit();
  			
   		}
   		
   		function cancel1()
   		{
   			
   			document.editDestination.buttonValue.value="cancel";
  			document.editDestination.submit();
  			window.close();
   		}
   		function add1(val1)
   		{
   		if(val1=="")
   		{
   				if(document.editDestination.destinationCode.value=="0")
					{
						alert("Please select Destination Code");
						document.editDestination.destinationCode.value="";
						document.editDestination.destinationCode.focus();
						return;
					}
		}
   			 	if(IsNumeric(document.editDestination.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.editDestination.ttToPol.value="";
    				document.editDestination.ttToPol.focus();
   					return;
  				} 
  				
  		if(val1=="")
   		{		
   			document.editDestination.buttonValue.value="add";
   			
   		}
   		else if(val1=="list")
   		{	
   			document.editDestination.buttonValue.value="update";
   		}
  		document.editDestination.submit();
   		}
   		
  		function disabled(val,val2)
   		{
   			 if((val2 == 0 || val2== 3)&& val2!="")
			{
       			 var imgs = document.getElementsByTagName('img');
   				 document.getElementById("add").style.visibility = 'hidden';
   				 for(var k=0; k<imgs.length; k++)
   				 {
   		 			if(imgs[k].id != "cancel")
   		 			{
   		    			imgs[k].style.visibility = 'hidden';
   		 			}
   				 }
   				var input = document.getElementsByTagName("input");
   				for(i=0; i<input.length; i++)
	 			{
	 				if(input[i].id != "buttonValue")
	 				{
	  					input[i].readOnly=true;
						input[i].style.color="blue";

	  				}
  	 			}
   				var select = document.getElementsByTagName("select");
   				for(i=0; i<select.length; i++)
	 			{
	 				select[i].disabled=true;
			select[i].style.backgroundColor="blue";


  	 			}
  		    }
  		   else if(val=="list")
   			{
   				var select =document.editDestination.destinationCode.value;
	 			select.disabled = true;
   				var y=document.editDestination.destinationName.value;
   				y.disabled=true;
   			}
   			
  		}
  		
   		function confirmdestdelete()
		{
			document.editDestination.buttonValue.value="delete";
   			 var result = confirm("Are you sure you want to delete this Destination ");
			if(result)
			{
   				document.editDestination.submit();
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
document.editDestination.submit();

return false;
}		
   		
		</script>
 </head>
<body class="whitebackgrnd" onload="disabled('<%=adddest%>','<%=modify%>')" onkeydown="preventBack()">
<html:form action="/editDestination" name="editDestination" type="com.gp.cong.logisoft.struts.form.EditDestinationForm" scope="request">
<font color="blue" size="4"><%=msg%></font>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew"><td>Destination Details</td>
		<td colspan="3" align="right">	<%if(adddest.equals("list")) { %>
			<input type="button" class="buttonStyleNew" value="Save" onclick="add1('<%=adddest%>')"/>
			<%} else { %>
		   <input type="button" class="buttonStyleNew" value="Add" id="add" onclick="add1('<%=adddest%>')"/>
			<%} %>
			<%if(adddest.equals("list")) { %>
			<input type="button" class="buttonStyleNew" id="cancel" value="Delete" onclick="confirmdestdelete()"/>
			<%} %></td>
		</tr>
		<tr>
		<td colspan="2">
		<table width="100%"  border="0" cellpadding="3" cellspacing="0" >
  		<%
  				if(relayDestination!=null && relayDestination.getDestinationId()!=null)
   				{
   				if(adddest.equals("list"))
   				{
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				}
   				else
   				{
   				destinationid = relayDestination.getDestinationId().getShedulenumber();
   				}
   				}
   				
   				
   				 %>
		<tr >
  			<td class="textlabels">Destination Code<font color="red">*</font></td>
  			<%if(!adddest.equals("list") && !buttonValue.equals("update"))
  			{ %>
  			<td><html:text property="destinationCode" value="<%=destinationid%>" readonly="true">
           		
           	</html:text>
           	<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editdestination','windows')">
           	</td>
           	<%}
          	if(adddest.equals("list"))
           	{ %>
           	<td><html:text property="destinationCode" value="<%=destinationid%>" readonly="true" styleClass="varysizeareahighlightgrey"/></td>
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
</html:form>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


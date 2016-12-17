<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
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

String cutOffTime="";
String week="0";


String editorigin="";
if(request.getParameter("editOrigin")!=null && request.getParameter("editOrigin").equals("list"))
{
editorigin=(String)request.getParameter("editOrigin");
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
		opener.location.href="<%=path%>/jsps/datareference/editRelay.jsp";
	</script>
<%
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
   			}
   			if(relayOrigin.getCutOffDayOfWeek()!=null && relayOrigin.getCutOffDayOfWeek().getId()!=null)
   			{
   			week=relayOrigin.getCutOffDayOfWeek().getId().toString();
   			}
				if(relayOrigin!=null  && relayOrigin.getTtToPol()!=null)
					{
				 			ttToPol=relayOrigin.getTtToPol().toString();
					}
			
			if(relayOrigin.getCutOffTime()!=null)
					{
							cutOffTime=relayOrigin.getCutOffTime().toString();
					}		
	
   		}
  	 request.setAttribute("weekList",dbUtil.getWeekList(new Integer(25),"yes","Select Week")); 		
  	 String modify="";
 
  	 if (session.getAttribute("view") != null)
	{
		modify = (String) session.getAttribute("view");
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
		<script language="javascript" type="text/javascript">
		function submit1()
 		{ 
 				if(IsNumeric(document.editOrigin.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.editOrigin.ttToPol.value="";
    				document.editOrigin.ttToPol.focus();
   					return;
  				} 
  				if(IsNumeric(document.editOrigin.cutOffDayOfWeek.value)==false)
  				{
   					alert("Cut off days of week should be Numeric.");
  					document.editOrigin.cutOffDayOfWeek.value="";
    				document.editOrigin.cutOffDayOfWeek.focus();
   					return;
  				} 
  				if(IsNumeric(document.editOrigin.cutOffTime.value)==false)
  				{
   					alert("Cut off time should be Numeric.");
  					document.editOrigin.cutOffTime.value="";
    				document.editOrigin.cutOffTime.focus();
   					return;
  				} 
  				
 			document.editOrigin.buttonValue.value="originselected";
  			document.editOrigin.submit();
  			
   		}
   		
   		function cancel1()
   		{
   			document.editOrigin.buttonValue.value="cancel";
  			document.editOrigin.submit();
  			window.close();
   		}
   		function add1(val1)
   		{
   		
   		if(val1=="")
   		{
   				if(document.editOrigin.originCode.value=="0")
					{
						alert("Please select Origin Code");
						document.editOrigin.originCode.value="";
						document.editOrigin.originCode.focus();
						return;
					}
		}
   			 	if(IsNumeric(document.editOrigin.ttToPol.value)==false)
  				{
   					alert("Transit Time to POL should be Numeric.");
  					document.editOrigin.ttToPol.value="";
    				document.editOrigin.ttToPol.focus();
   					return;
  				} 
  				if(IsNumeric(document.editOrigin.cutOffDayOfWeek.value)==false)
  				{
   					alert("Cut off days of week should be Numeric.");
  					document.editOrigin.cutOffDayOfWeek.value="";
    				document.editOrigin.cutOffDayOfWeek.focus();
   					return;
  				} 
  				if(IsNumeric(document.editOrigin.cutOffTime.value)==false)
  				{
   					alert("Cut off time should be Numeric.");
  					document.editOrigin.cutOffTime.value="";
    				document.editOrigin.cutOffTime.focus();
   					return;
  				} 
  		if(val1=="")
   		{		
   			document.editOrigin.buttonValue.value="add";
   		}
   	else if(val1=="list")
   		{	
   		document.editOrigin.buttonValue.value="update";
   		}
  			document.editOrigin.submit();
  			//document.opener.reload();
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
  else	if(val=="list")
   		{
   		var select = document.editOrigin.originCode.value;
	 	select.disabled = true;
   		var y=document.editOrigin.originName.value;
   		y.disabled=true;
   		}
   		
  		}
   		
   		function confirmdelete()
	{
	
		document.editOrigin.buttonValue.value="delete";
    	var result = confirm("Are you sure you want to delete this origin ");
		if(result)
		{
   			document.editOrigin.submit();
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
document.editOrigin.submit();

return false;
}	
		</script>
 </head>
<body class="whitebackgrnd" onload="disabled('<%=editorigin%>','<%=modify%>')" onkeydown="preventBack()">
<html:form action="/editOrigin" name="editOrigin" type="com.gp.cong.logisoft.struts.form.EditOriginForm" scope="request">
<font color="blue" size="4"><%=msg%></font>


	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew"><td>Origin Details</td>
		<td align="right"> <%if(editorigin.equals("list")) { %>
			<input type="button" class="buttonStyleNew" value="Save" onclick="add1('<%=editorigin%>')"/>
			<%} else { %>
			<input type="button" class="buttonStyleNew" value="Add" id="add" onclick="add1('<%=editorigin%>')"/>
			<%} %>
			<%if(editorigin.equals("list")) { %>
			<input type="button" class="buttonStyleNew" value="Delete" onclick="confirmdelete()"/>
			<%} %></td>
		</tr>
		<tr>
		<td colspan="2">
		<table width="100%" border="0" cellpadding="3" cellspacing="0">
  		<%
  				if(relayOrigin!=null && relayOrigin.getOriginId()!=null)
   				{
   				if(editorigin.equals("list"))
   				{
   				originid = relayOrigin.getOriginId().getShedulenumber();
   				}
   				else
   				{
   				originid = relayOrigin.getOriginId().getShedulenumber();
   				}
   				}
   				
   				 %>
		<tr >
  			<td><table width="100%" border="0"><tr class="style2"><td>Origin Code<font color="red">*</font>&nbsp;</td></tr></table></td>
  			<%if(!editorigin.equals("list")&& !buttonValue.equals("update"))
  			{
  			 %>
  			<td><html:text property="originCode" value="<%=originid%>"  readonly="true">
           		
           	</html:text>
           	<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editorigin','windows')">
           	</td>
           	<%}
           	if(editorigin.equals("list"))
           	{ %>
           	<td><html:text property="originCode" value="<%=originid%>" readonly="true" styleClass="varysizeareahighlightgrey"></html:text></td>
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
   			 <td width="111">&nbsp;</td>
    		 <td width="132">&nbsp;</td>
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

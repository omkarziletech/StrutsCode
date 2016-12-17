<%@ page language="java" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String msg="";
	if(request.getAttribute("message")!=null)
	{
		msg=(String)request.getAttribute("message");
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for ConsigneeMainForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
 <script type="text/javascript">
 function save()
	{
		var netie=navigator.appName;
  
    if(netie=="Microsoft Internet Explorer")
  	 {
   		
	
	if(parent.mainFrame.document.acciframe.consigneeForm.consigNo.value=="")
	{
		alert("Please enter the Consignee No");
		parent.mainFrame.document.acciframe.consigneeForm.consigNo.value="";
		parent.mainFrame.document.acciframe.consigneeForm.consigNo.focus();
		return;
	}
	var val=parent.mainFrame.document.acciframe.consigneeForm.consigNo.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee no");
            	return;
           }
  if(parent.mainFrame.document.acciframe.consigneeForm.consigName.value=="")
	{
		alert("Please enter the  Consignee Name");
		parent.mainFrame.document.acciframe.consigneeForm.consigName.value="";
		parent.mainFrame.document.acciframe.consigneeForm.consigName.focus();
		return;
	}
	var val=parent.mainFrame.document.acciframe.consigneeForm.consigName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee Name");
            	return;
           }
	if(parent.mainFrame.document.acciframe.consigneeForm.coName.value=="")
	{
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.acciframe.consigneeForm.coName.coName.value="";
		parent.mainFrame.document.acciframe.consigneeForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.acciframe.consigneeForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
	
 if(parent.mainFrame.document.acciframe.consigneeForm.country.value=="0")
   {
		alert("Please enter the country");
		parent.mainFrame.document.acciframe.consigneeForm.country.value="";
		parent.mainFrame.document.acciframe.consigneeForm.country.focus();
		return;
	}   
	if(parent.mainFrame.document.acciframe.consigneeForm.city.value=="0")
	{
		alert("Please enter the city");
		parent.mainFrame.document.acciframe.consigneeForm.city.value="";
		parent.mainFrame.document.acciframe.consigneeForm.city.focus();
		return;
	}     
	if(IsNumeric(parent.mainFrame.document.acciframe.consigneeForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.acciframe.consigneeForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.consigneeForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.acciframe.consigneeForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(parent.mainFrame.document.acciframe.consigneeForm.phone.value!="" && parent.mainFrame.document.acciframe.consigneeForm.phone.value<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.acciframe.consigneeForm.phone.value="";
    	parent.mainFrame.document.acciframe.consigneeForm.phone.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.acciframe.consigneeForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.acciframe.consigneeForm.phone.value="";
    	parent.mainFrame.document.acciframe.consigneeForm.phone.focus();
   	    return;
   } 
    var value=parent.mainFrame.document.acciframe.consigneeForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Telephone No");
					return;
				}
			}
   if(parent.mainFrame.document.acciframe.consigneeForm.fax.value!="" && parent.mainFrame.document.acciframe.consigneeForm.fax.value<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.acciframe.consigneeForm.fax.value="";
    	parent.mainFrame.document.acciframe.consigneeForm.fax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.acciframe.consigneeForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.acciframe.consigneeForm.fax.value="";
    	parent.mainFrame.document.acciframe.consigneeForm.fax.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.acciframe.consigneeForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Fax");
					return;
				}
			}
	if(IsNumeric(parent.mainFrame.document.geniframe.consigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		parent.mainFrame.document.geniframe.consigneeInformationForm.taxExempt.value="";
    	parent.mainFrame.document.geniframe.consigneeInformationForm.taxExempt.focus();
   	  return;
   } 
   if(IsNumeric(parent.mainFrame.document.geniframe.consigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		parent.mainFrame.document.geniframe.consigneeInformationForm.maxDaysBetVisits.value="";
    	parent.mainFrame.document.geniframe.consigneeInformationForm.maxDaysBetVisits.focus();
   	  return;
   } 
 if(IsNumeric(parent.mainFrame.document.geniframe.consigneeInformationForm.zipCode.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.geniframe.consigneeInformationForm.zipCode.value="";
   	 	parent.mainFrame.document.geniframe.consigneeInformationForm.zipCode.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.geniframe.consigneeInformationForm.zipCode.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for ZipCode");
					return;
				}
			}
		}
		else if(netie=="Netscape")
  	 {
		
		if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigNo.value=="")
	{
		alert("Please enter the Consignee No");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigNo.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigNo.focus();
		return;
	}
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigNo.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee no");
            	return;
           }
  if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigName.value=="")
	{
		alert("Please enter the  Consignee Name");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigName.focus();
		return;
	}
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.consigName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee Name");
            	return;
           }
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.coName.value=="")
	{
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.coName.coName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
	
 if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.country.value=="0")
   {
		alert("Please enter the country");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.country.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.country.focus();
		return;
	}   
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.city.value=="0")
	{
		alert("Please enter the city");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.city.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.city.focus();
		return;
	}     
	if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value!="" && parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.focus();
   	  return;
   } 
    var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Telephone No");
					return;
				}
			}
			
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value!="" && parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Fax");
					return;
				}
			}
	if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.taxExempt.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.taxExempt.focus();
   	  return;
   } 
   if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.maxDaysBetVisits.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.maxDaysBetVisits.focus();
   	  return;
   } 
 if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.zipCode.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.zipCode.value="";
   	 	parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.zipCode.focus();
   	 	return;
   } 
   var value= parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.zipCode.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for ZipCode");
					return;
				}
			}
		}
		
		var netie=navigator.appName;
  
    if(netie=="Microsoft Internet Explorer")
  	 {
   		parent.mainFrame.document.acciframe.consigneeForm.submit();
  	parent.mainFrame.document.geniframe.consigneeInformationForm.submit();
  	
  	 }
   	else if(netie=="Netscape")
  	 {
   		 parent.mainFrame.document.getElementById('acciframe').contentDocument.consigneeForm.submit();
   		 parent.mainFrame.document.getElementById('geniframe').contentDocument.consigneeInformationForm.submit();
  	 }
  	 
   		 document.consigneeMainForm.buttonValue.value="add";
   		 document.consigneeMainForm.submit();
  }
  function cancel()
  {
    document.consigneeMainForm.buttonValue.value="cancel";
    document.consigneeMainForm.submit();
  }
    </script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/consigneeMain" scope="request">
	
			<table width="590" cellpadding="0" cellspacing="0" >
<tr class="textlabels">
<td width="589">
 <font color="blue"><h4><%=msg%></h4></font>	
</td>

<td width="82"><img src="<%=path%>/img/save.gif" border="0" onclick="save()"/></td>
<td><a>
<img src="<%=path%>/img/cancel.gif" onclick="cancel()"/></a></td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



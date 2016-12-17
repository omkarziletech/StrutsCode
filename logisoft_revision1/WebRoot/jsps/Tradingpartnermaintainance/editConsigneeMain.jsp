<%@ page language="java" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String msg = "";

	
	if (session.getAttribute("message") != null)
	{
		msg = (String) session.getAttribute("message");
	}
	String modify = null;
	if (session.getAttribute("modifyforconsignee") != null) {
		modify = (String) session.getAttribute("modifyforconsignee");

	}

	// Name:Rohith Date:12/10/2007 (mm/dd/yy)  ----> Is view only when page is locked

	if (session.getAttribute("view") != null)
	{
		modify = (String) session.getAttribute("view");
		
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for EditConsigneeMainForm form</title>
    <%@include file="../includes/baseResources.jsp" %>


	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
 <script type="text/javascript">
 function save()
	{
			var netie=navigator.appName;
  		if(netie=="Microsoft Internet Explorer")
  		 {
   			if(parent.mainFrame.document.acciframe.editConsigneeForm.consigNo.value=="")
				{
					alert("Please enter the Consignee No");
					parent.mainFrame.document.acciframe.editConsigneeForm.consigNo.value="";
					parent.mainFrame.document.acciframe.editConsigneeForm.consigNo.focus();
					return;
				}
				var val=parent.mainFrame.document.acciframe.editConsigneeForm.consigNo.value
        if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee no");
            	return;
           }
 			 if(parent.mainFrame.document.acciframe.editConsigneeForm.consigName.value=="")
				{
					alert("Please enter the  Consignee Name");
					parent.mainFrame.document.acciframe.editConsigneeForm.consigName.value="";
					parent.mainFrame.document.acciframe.editConsigneeForm.consigName.focus();
					return;
				}
				var val=parent.mainFrame.document.acciframe.editConsigneeForm.consigName.value
        if(val.match(" "))
        {
         	alert("WhiteSpace is not allowed for Consignee Name");
         	return;
        }
		if(parent.mainFrame.document.acciframe.editConsigneeForm.coName.value=="")
		{
			alert("Please enter the C/O Name");
	 		parent.mainFrame.document.acciframe.editConsigneeForm.coName.coName.value="";
			parent.mainFrame.document.acciframe.editConsigneeForm.coName.focus();
	 		return;
		}  
		var val=parent.mainFrame.document.acciframe.editConsigneeForm.coName.value
     if(val.match(" "))
       {
      		alert("WhiteSpace is not allowed for C/O Name");
       		return;
       }
	
 if(parent.mainFrame.document.acciframe.editConsigneeForm.country.value=="0")
   {
		alert("Please enter the country");
		parent.mainFrame.document.acciframe.editConsigneeForm.country.value="";
		parent.mainFrame.document.acciframe.editConsigneeForm.country.focus();
		return;
	}   
	if(parent.mainFrame.document.acciframe.editConsigneeForm.city.value=="0")
	{
		alert("Please enter the city");
		parent.mainFrame.document.acciframe.editConsigneeForm.city.value="";
		parent.mainFrame.document.acciframe.editConsigneeForm.city.focus();
		return;
	}     
	if(IsNumeric(parent.mainFrame.document.acciframe.editConsigneeForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.acciframe.editConsigneeForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.editConsigneeForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.acciframe.editConsigneeForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(parent.mainFrame.document.acciframe.editConsigneeForm.phone.value!="" && parent.mainFrame.document.acciframe.editConsigneeForm.phone.value<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.acciframe.editConsigneeForm.phone.value="";
    	parent.mainFrame.document.acciframe.editConsigneeForm.phone.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.acciframe.editConsigneeForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.acciframe.editConsigneeForm.phone.value="";
    	parent.mainFrame.document.acciframe.editConsigneeForm.phone.focus();
   	    return;
   } 
    var value=parent.mainFrame.document.acciframe.editConsigneeForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Telephone No");
					return;
				}
			}
   if(parent.mainFrame.document.acciframe.editConsigneeForm.fax.value!="" && parent.mainFrame.document.acciframe.editConsigneeForm.fax.value<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.acciframe.editConsigneeForm.fax.value="";
    	parent.mainFrame.document.acciframe.editConsigneeForm.fax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.acciframe.editConsigneeForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.acciframe.editConsigneeForm.fax.value="";
    	parent.mainFrame.document.acciframe.editConsigneeForm.fax.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.acciframe.editConsigneeForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Fax");
					return;
				}
			}
			
			if(IsNumeric(parent.mainFrame.document.geniframe.editConsigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		parent.mainFrame.document.geniframe.editConsigneeInformationForm.taxExempt.value="";
    	parent.mainFrame.document.geniframe.editConsigneeInformationForm.taxExempt.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.geniframe.editConsigneeInformationForm.taxExempt.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Tax Exempt");
					return;
				}
			}

	if(IsNumeric(parent.mainFrame.document.geniframe.editConsigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		parent.mainFrame.document.geniframe.editConsigneeInformationForm.maxDaysBetVisits.value="";
    	parent.mainFrame.document.geniframe.editConsigneeInformationForm.maxDaysBetVisits.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.geniframe.editConsigneeInformationForm.maxDaysBetVisits.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Max Days Between Visits");
					return;
				}
			}
	
		}
	else if(netie=="Netscape")
  {
						if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigNo.value=="")
	{
		alert("Please enter the Consignee No");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigNo.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigNo.focus();
		return;
	}
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigNo.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee no");
            	return;
           }
  if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigName.value=="")
	{
		alert("Please enter the  Consignee Name");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigName.focus();
		return;
	}
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.consigName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee Name");
            	return;
           }
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.coName.value=="")
	{
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.coName.coName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
	
 if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.country.value=="0")
   {
		alert("Please enter the country");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.country.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.country.focus();
		return;
	}   
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.city.value=="0")
	{
		alert("Please enter the city");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.city.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.city.focus();
		return;
	}     
	if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value!="" && parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.focus();
   	  return;
   } 
	 var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Telephone No");
					return;
				}
			}
			
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value!="" && parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Fax");
					return;
				}
			}
	
			if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.taxExempt.value.replace(/ /g,''))==false)
   {
    	alert("Tax Exempt should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.taxExempt.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.taxExempt.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.taxExempt.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Tax Exempt");
					return;
				}
			}

	if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.maxDaysBetVisits.value.replace(/ /g,''))==false)
   {
    	alert("Max Days Between Visits should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.maxDaysBetVisits.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.maxDaysBetVisits.focus();
   	    return;
   } 
   var value=parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.maxDaysBetVisits.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space for Max Days Between Visits");
					return;
				}
			}

	}
		
   
	
		
	
		var netie=navigator.appName;
  
    if(netie=="Microsoft Internet Explorer")
  	 {
   		 parent.mainFrame.document.acciframe.editConsigneeForm.submit();
   		 parent.mainFrame.document.geniframe.editConsigneeInformationForm.submit();
  		 
  	 }
   	else if(netie=="Netscape")
  	 {
   		 parent.mainFrame.document.getElementById('acciframe').contentDocument.editConsigneeForm.submit();
   		 parent.mainFrame.document.getElementById('geniframe').contentDocument.editConsigneeInformationForm.submit();
  		 
  	 }
  	 
    document.editConsigneeMainForm.buttonValue.value="add";
    document.editConsigneeMainForm.submit();
  }
    function confirmdelete()
    {
			document.editConsigneeMainForm.buttonValue.value="delete";
			var result = confirm("Are you sure you want to delete this Consignee");
			if(result)
			{
    			document.editConsigneeMainForm.submit();
   	    	}
    }
    
   	function cancelbtn(val)
  	{
  
   	 if(val==0 || val==3 )
		{
			document.editConsigneeMainForm.buttonValue.value="cancelview";
		}
	 else
		{
   			document.editConsigneeMainForm.buttonValue.value="cancel";
  		}
  		 document.editConsigneeMainForm.submit();
    }
   
 function disabled(val1,val2)
   {
  
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id != "cancel" &&  imgs[k].id!="note")
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
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 	if(val1==3)
		{
			alert(val2);
			
		}		
    }
    </script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=msg%>')">
		<html:form action="/editConsigneeMain" scope="request">
	
			<table width="590" cellpadding="0" cellspacing="0" >
<tr class="textlabels">
<td width="589">
 	
</td>
<td width="82"><img src="<%=path%>/img/save.gif" border="0" onclick="save()" /></td>
<td><img src="<%=path%>/img/delete.gif" id="delete"
										onclick="confirmdelete()" />
								</td>
<td><a>
<img src="<%=path%>/img/cancel.gif" id="cancel" onclick="cancelbtn(<%=modify%>)"/></a></td>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



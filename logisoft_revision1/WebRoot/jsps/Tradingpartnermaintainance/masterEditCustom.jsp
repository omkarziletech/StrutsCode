<%@ page language="java" pageEncoding="ISO-8859-1"  import="com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.TradingPartner"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String modify="";
String message="";
String type="";
String master="0";

TradingPartner tradingpartner=new TradingPartner();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
modify = (String) session.getAttribute("modifyformastercustomer");
String accountPrefix="";
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
	}
	if(session.getAttribute("msg")!=null)
{
 message=(String)session.getAttribute("msg");
 
} 
if(session.getAttribute("MasterTradingPartner")!=null)
{
tradingpartner=(TradingPartner)session.getAttribute("MasterTradingPartner");



if(tradingpartner.getMaster()!=null)
{
master=tradingpartner.getMaster();
}
}
DBUtil dbUtil=new DBUtil();


%>



 
<html> 
	<head>
	<base href="<%=basePath%>">
		<title>JSP for MasterEditCustomForm form</title>
		<%@include file="../includes/baseResources.jsp" %>


	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" src="<%=path%>/js/FormChek.js" ></script>
    <script type="text/javascript">
    
    
    
    
    function editsave()
	{
	  
    	var netie=navigator.appName;  
    	if(netie=="Microsoft Internet Explorer")
     {
				/*if(parent.mainFrame.document.acciframe.masterEditCustomerForm==undefined)
		   	    {
		   	        var flag="Account Details";
		   	    }
		   		if(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm==undefined)
		   	    {
		   	         var flag="General Information";
		   	    }
		   	    if(parent.mainFrame.document.aciframe.masterEditAccountingForm==undefined)
		   	    {
		   	         var flag="Accounting";
		   	    }
		   	    if(flag=="Account Details")
		   	    {
		   	    	alert("You are in notes page, Please come back to Account Details page");
		   	    }
		   	    if(flag=="General Information")
		   	    {
		   	    	alert("You are in notes page, Please come back to General Information page");
		   	    }
		   	    if(flag=="Accounting")
		   	    {
		   	    	alert("You are in notes page, Please come back to Accounting page");
		   	    }
  
 
 if(parent.mainFrame.document.acciframe.masterEditCustomerForm.coName.value!= null &&
   parent.mainFrame.document.acciframe.masterEditCustomerForm.coName.value=="")
	{	
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.acciframe.masterEditCustomerForm.coName.value="";
		parent.mainFrame.document.acciframe.masterEditCustomerForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.acciframe.masterEditCustomerForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
      
	if(parent.mainFrame.document.acciframe.masterEditCustomerForm.city.value!= null &&
	parent.mainFrame.document.acciframe.masterEditCustomerForm.city.value=="")
	{
	
		alert("Please enter the city");
		parent.mainFrame.document.acciframe.masterEditCustomerForm.city.value="";
		parent.mainFrame.document.acciframe.masterEditCustomerForm.city.focus();
		return;
	}   
	if(parent.mainFrame.document.acciframe.masterEditCustomerForm.address1.value!=null &&
	parent.mainFrame.document.acciframe.masterEditCustomerForm.address1.value=="")
	{
		alert("Please enter the Address1");
		parent.mainFrame.document.acciframe.masterEditCustomerForm.address1.value="";
		parent.mainFrame.document.acciframe.masterEditCustomerForm.address1.focus();
		return;
	}
	if(parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value!=null &&
	parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value!="")
	{
	var value=parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") !=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
			
	if(IsNumeric(parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.focus();
   	 	return;
   } 
   }
   
   if(parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value!= null &&
   parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value!="" &&
 parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.masterEditCustomerForm.zip.focus();
   	 	return;
   } 
   if(IsNumeric(parent.mainFrame.document.acciframe.masterEditCustomerForm.extension.value.replace(/ /g,''))==false)
  {
    alert("Extension should be Numeric.");
  	parent.mainFrame.document.acciframe.masterEditCustomerForm.extension.value="";
    parent.mainFrame.document.acciframe.masterEditCustomerForm.extension.focus();
    return;
   } 
   
   var value=parent.mainFrame.document.acciframe.masterEditCustomerForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ")!=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(parent.mainFrame.document.acciframe.masterEditCustomerForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.acciframe.masterEditCustomerForm.phone.value="";
    	parent.mainFrame.document.acciframe.masterEditCustomerForm.phone.focus();
   	    return;
   } 
    
    var value=parent.mainFrame.document.acciframe.masterEditCustomerForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") !=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(parent.mainFrame.document.acciframe.masterEditCustomerForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.acciframe.masterEditCustomerForm.fax.value="";
    	parent.mainFrame.document.acciframe.masterEditCustomerForm.fax.focus();
   	    return;
   } 
    var stremail = parent.mainFrame.document.acciframe.masterEditCustomerForm.email1.value;
    if(stremail!="")
    {
	if(stremail.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email1");
		return;
	}
	if(isEmailSpecial(stremail)==false)
	{
		alert("Special Characters not allowed in Email1.");
		parent.mainFrame.document.acciframe.masterEditCustomerForm.email1.focus();
		return;
	} 
	 if(isEmail(stremail)==false)
  	{
    	alert("Please enter proper email1.");
    	parent.mainFrame.document.acciframe.masterEditCustomerForm.email1.focus();
    	return;
   } 
   }
   var stremail1 = parent.mainFrame.document.acciframe.masterEditCustomerForm.email2.value;
   if(stremail1!="")
   {
	if(stremail1.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email2");
		return;
	}
	if(isEmailSpecial(stremail1)==false)
	{
		alert("Special Characters not allowed in Email2.");
		parent.mainFrame.document.acciframe.masterEditCustomerForm.email2.focus();
		return;
	} 
	 if(isEmail(stremail1)==false)
  	{
    	alert("Please enter proper email2.");
    	parent.mainFrame.document.acciframe.masterEditCustomerForm.email2.focus();
    	return;
   } 
   }
 
    if(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value!=null && 
    parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value!="" && 
parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value="";
    	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.focus();
   	    return;
   } 
   if(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.spclRemark.value!= null &&
   parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.spclRemark.value!="" && 
parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.spclRemark.value.length>200)
   {
   		alert("Special Remark Number should be below 200 digits.");
  		parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.spclRemark.value="";
    	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.spclRemark.focus();
   	    return;
   } 
   if(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.defaultRoute.value!="" && 
parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.defaultRoute.value.length>200)
   {
   		alert("Default Route Number should be below 200 digits.");
  		parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.defaultRoute.value="";
    	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.defaultRoute.focus();
   	    return;
   } 
   
    if(IsNumeric(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.maxDay.value.replace(/ /g,''))==false)
   {
    	alert("Max Day should be Numeric.");
  		parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.maxDay.value="";
    	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.maxDay.focus();
   	    return;
   } 
   if(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.value!="" && 
parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.value="";
    	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.focus();
   	    return;
   } 
   
   
   
   if(IsNumeric(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value.replace(/ /g,''))==false)
  {
    alert("Additional Phone Number should be Numeric.");
  	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.value="";
    parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.phone1.focus();
    return;
   } 
   if(IsNumeric(parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.value.replace(/ /g,''))==false)
  {
    alert("Additional Fax Number should be Numeric.");
  	parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.value="";
    parent.mainFrame.document.geniframe.masterEditGeneralInformationForm.fax1.focus();
    return;
   }
   
   
   
 if( parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.value!="" &&  
parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		 parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.value="";
    	 parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.focus();
   	    return;
   } 

  if(parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value!="")
   {
   if(IsNumeric(parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value.replace(/ /g,''))==false)
   {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.focus();
   	    return;
   }
   else if(parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value.length>5 && 
parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.zipCode.focus();
   	    return;
   }
   }
  
   if( parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.value!="" &&  parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		 parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.value="";
    	 parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.focus();
   	    return;
   } 
    if(IsNumeric(parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
   {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   } 
   
   if(IsNumeric(parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.value.replace(/ /g,''))==false)
   {
    	alert("A/R Fax should be Numeric.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.arFax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.value.replace(/ /g,''))==false)
   {
    	alert("A/R phone should be Numeric.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.arPhone.focus();
   	    return;
   } 
  
   if(parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value!="")
   {
   if(parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value.length<5)
   {
  		 alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   }
   else if(parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value.length>5 && 
parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   }
   }
    var stremail2 = parent.mainFrame.document.aciframe.masterEditAccountingForm.email.value;
    if(stremail2!="")
    {
	if(stremail2.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email");
		return;
	}
	if(isEmailSpecial(stremail2)==false)
	{
		alert("Special Characters not allowed in Emai.");
		parent.mainFrame.document.aciframe.masterEditAccountingForm.email.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email.");
    	parent.mainFrame.document.aciframe.masterEditAccountingForm.email.focus();
    	return;
   } 
   }*/
}
 else if(netie=="Netscape")
   {
   /*if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm==undefined)
	{
		 var flag="Account Details";
	}
	if(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm==undefined)
	{
		 var flag="General Information";
		   	    	
	}
	if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm==undefined)
	{
		 var flag="Accounting";
		   	    	
	 }
		   	    
		   	    if(flag=="Account Details")
		   	    {
		   	    	alert("You are in notes page, Please come back to Account Details page");
		   	    }
		   	    if(flag=="General Information")
		   	    {
		   	    	alert("You are in notes page, Please come back to General Information page");
		   	    }
		   	    if(flag=="Accounting")
		   	    {
		   	    	alert("You are in notes page, Please come back to Accounting page");
		   	    }
  
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.coName.value!= null &&
   parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.coName.value=="")
	{
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.coName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
  
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.city.value=="")
	{
		alert("Please enter the city");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.city.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.city.focus();
		return;
	}     
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.address1.value=="")
	{
		alert("Please enter the Address1");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.address1.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.address1.focus();
		return;
	}
	
	if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.focus();
   	 	return;
   } 
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value!="" && 
parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.focus();
   	 	return;
   } 
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value!="" && 
parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value.length>5 &&
 parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
				}
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value!="")
	{
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.phone.focus();
   	    return;
   } 
    var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value!="")
   {
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.fax.focus();
   	    return;
   } 
    var stremail = parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email1.value;
    if(stremail!="")
    {
	if(stremail.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email1");
		return;
	}
	if(isEmailSpecial(stremail)==false)
	{
		alert("Special Characters not allowed in Email1.");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email1.focus();
		return;
	} 
	 if(isEmail(stremail)==false)
  	{
    	alert("Please enter proper email1.");
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email1.focus();
    	return;
   } 
   }
   var stremail2 = parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email2.value;
   if(stremail2!="")
   {
	if(stremail2.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email2");
		return;
	}
	if(isEmailSpecial(stremail2)==false)
	{
		alert("Special Characters not allowed in Email2.");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email2.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email2.");
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.email2.focus();
    	return;
   } 
   }
     
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.phone1.value!= null && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.phone1.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.phone1.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.phone1.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.phone1.focus();
   	    return;
   } 
 
    if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.maxDay.value.replace(/ /g,''))==false)
   {
    	alert("Max Day should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.maxDay.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.maxDay.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.fax1.value!="" &&
    parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.fax1.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.fax1.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.fax1.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.spclRemark.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.spclRemark.value.length>200)
   {
   		alert("Special Remark Number should be below 200 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.spclRemark.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.spclRemark.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.defaultRoute.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.defaultRoute.value.length>200)
   {
   		alert("Default Route Number should be below 200 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.defaultRoute.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.masterEditGeneralInformationForm.defaultRoute.focus();
   	    return;
   }
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arFax.value!="" &&
    parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arFax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arFax.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arFax.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arPhone.value!="" &&
    parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arPhone.value.length<13)
   {
   		alert("Phone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arPhone.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.arPhone.focus();
   	    return;
   } 
    if(IsNumeric(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value.replace(/ /g,''))==false)
   {
    	alert("ZipCode should be Numeric.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value!="")
   {
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value.length<5)
   {
   		alert("ZipCode should be 5digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.focus();
   	    return;
   } 
   else if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value.length>5 
&& parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value.length<9)
   {
   		alert("ZipCode should be 9digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.zipCode.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
   {
    	alert("ZipCode should be Numeric.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value!="")
   {
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value.length<5)
   {
   		alert("ZipCode should be 5digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   } 
   else if(parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value.length>5 && 
parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value.length<9)
   {
   		alert("ZipCode should be 9digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.ffzipCode.focus();
   	    return;
   } 
   }
    var stremail2 = parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.email.value;
	if(stremail2!="")
	{
	if(stremail2.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email");
		return;
	}
	if(isEmailSpecial(stremail2)==false)
	{
		alert("Special Characters not allowed in Emai.");
		parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.email.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email.");
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.email.focus();
    	return;
   } 
   }*/

   }	
    	
    	if(netie=="Microsoft Internet Explorer")
    	{
    	   
   			//parent.mainFrame.document.acciframe1.masterEditCustomerForm.submit();
   			parent.mainFrame.document.acciframe.masterEditGeneralInformationForm.submit();
   			parent.mainFrame.document.aciframe.masterEditAccountingForm.submit();
   			parent.mainFrame.document.editvendiframe.mastereditvendorForm.submit();
   		}   
  		else if(netie=="Netscape")
   		{
   		  
   			//parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditCustomerForm.submit();
   			parent.mainFrame.document.getElementById('acciframe').contentDocument.masterEditGeneralInformationForm.submit();
   			parent.mainFrame.document.getElementById('aciframe').contentDocument.masterEditAccountingForm.submit();
   			if(parent.mainFrame.document.getElementById('editvendiframe').contentDocument.mastereditvendorForm!=undefined)
   			{
   			 parent.mainFrame.document.getElementById('editvendiframe').contentDocument.mastereditvendorForm.submit();
   			}
   		}
    	document.masterEditCustomForm.buttonValue.value="add";
   		document.masterEditCustomForm.submit();
    }
    
     function confirmdelete()
    {
			document.masterEditCustomForm.buttonValue.value="delete";
			var result = confirm("Are you sure you want to delete this Customer");
			if(result)
			{
    			document.masterEditCustomForm.submit();
   	    	}
    }
     
    
   function editcancel(val)
    {
	    if(val==0 || val==3)
	    {
	     document.masterEditCustomForm.buttonValue.value="cancel";
	    document.masterEditCustomForm.submit();
	    }
	    else
	    {
		       /* var result=confirm("Do you want to save this record?");
				   if(result)
				   {
				   editsave();
					   //if(val==0 || val==3)
					    {
					    document.masterEditCustomForm.buttonValue.value="cancelview";//
				  }
		    else
		    {*/
    document.masterEditCustomForm.buttonValue.value="cancel";
    document.masterEditCustomForm.submit();
    
    	}
   } 
    
    
    
    function disabled(val1,val2)
   	{
			if(val1 == 0 || val1 == 3)
			{
       			 var imgs = document.getElementsByTagName('img');
       			
   				 for(var k=0; k<imgs.length; k++)
   				 {
   				 	
   		 			if(input[i].id != "buttonValue" && input[i].id !="save" && input[i].id !="delete" && input[i].id !="cancel")
	 				{
   		    			imgs[k].style.visibility = 'hidden';
   		 			}
   				 }
   				var input = document.getElementsByTagName("input");
   				for(i=0; i<input.length; i++)
	 			{
	 				if(input[i].id != "buttonValue" && input[i].id !="save" && input[i].id !="delete" && input[i].id !="cancel")
	 				{
	  					input[i].disabled = true;
	  				}
  	 			}
  	 			document.getElementById("save").style.visibility = 'hidden';
  	 			document.getElementById("delete").style.visibility = 'hidden';
  	 		}
  	  		if(val1 == 1)
  	 		{
  	 	  		document.getElementById("delete").style.visibility = 'hidden';
  	 		}
  	 		if(val1 == 3 &&  val2!="")
			{
				alert(val2);
			}		
  	 }
  	 
  	 
  	 
  	 function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
    
    
    </script >
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form action="/masterEditCustom" scope="request">
		
		<table width="100%" cellpadding="0" cellspacing="0">
<tr class="textlabels">
 <td><bean:message key="form.customerForm.name" /></td>
 <td><html:text property="name" value="<%=tradingpartner.getAccountName()%>" onkeyup="toUppercase(this)"/></td>
 <td>Account No</td>
 <td><html:text property="accountno" value="<%=tradingpartner.getAccountno()%>" readonly="true" styleClass="areahighlightgreydefult"/></td>











<td >
  <input type="button" class="buttonStyleNew" value="Save" id="save" onclick="editsave()"  />
</td>
<td >
  <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="confirmdelete()"  />
</td>
<td>
  <input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="editcancel(<%=modify%>)"  />
</td>
<%--<img src="<%=path%>/img/cancel.gif" id="cancel" onclick="editcancel(<%=modify%>)" />--%>
</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		
			
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


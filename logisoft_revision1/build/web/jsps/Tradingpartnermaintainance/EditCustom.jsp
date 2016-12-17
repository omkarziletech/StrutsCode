<%@ page language="java" pageEncoding="ISO-8859-1" 
import="com.gp.cong.logisoft.util.DBUtil, com.gp.cong.logisoft.domain.TradingPartner"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String modify="";
String message="";
String type="";
String master="0";
//TradingPartner tradingpartner= new TradingPartner();
TradingPartner tradingpartner = new  TradingPartner();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
modify = (String) session.getAttribute("modifyforcustomer");
String accountPrefix="";
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
	}
	if(session.getAttribute("message")!=null)
{
 message=(String)session.getAttribute("message");
 
} 
if(session.getAttribute("tradingpartner")!=null)
{
tradingpartner=(TradingPartner)session.getAttribute("tradingpartner");

if(tradingpartner.getMaster()!=null)
{
 master=tradingpartner.getMaster();
}

}

DBUtil dbUtil=new DBUtil();
request.setAttribute("mastertypelist",dbUtil.getMasterCodeList());

%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for editCustomerForm form</title>
    
	<%@include file="../includes/baseResources.jsp" %>


	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" src="<%=path%>/js/FormChek.js" ></script>
    <script type="text/javascript">
    function editsave()
	{
	  //alert("hai");
    	var netie=navigator.appName;  
    	if(netie=="Microsoft Internet Explorer")
   {
				/*if(parent.mainFrame.document.acciframe.editCustomerForm==undefined)
		   	    {
		   	        var flag="Account Details";
		   	    	
		   	    }
		   		if(parent.mainFrame.document.geniframe1.editGeneralInformationForm==undefined)
		   	    {
		   	         var flag="General Information";
		   	    	
		   	    }
		   	    if(parent.mainFrame.document.aciframe.editAccountingForm==undefined)
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
  
	
	if(parent.mainFrame.document.acciframe.editCustomerForm.city.value=="")
	{
		alert("Please enter the city");
		parent.mainFrame.document.acciframe.editCustomerForm.city.value="";
		parent.mainFrame.document.acciframe.editCustomerForm.city.focus();
		return;
	}     
	if(parent.mainFrame.document.acciframe.editCustomerForm.address1.value=="")
	{
		alert("Please enter the Address1");
		parent.mainFrame.document.acciframe.editCustomerForm.address1.value="";
		parent.mainFrame.document.acciframe.editCustomerForm.address1.focus();
		return;
	}
	var value=parent.mainFrame.document.acciframe.editCustomerForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") !=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
	if(IsNumeric(parent.mainFrame.document.acciframe.editCustomerForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.acciframe.editCustomerForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.editCustomerForm.zip.focus();
   	 	return;
   } 
   if(parent.mainFrame.document.acciframe.editCustomerForm.zip.value!="" &&
 parent.mainFrame.document.acciframe.editCustomerForm.zip.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.acciframe.editCustomerForm.zip.value="";
   	 	parent.mainFrame.document.acciframe.editCustomerForm.zip.focus();
   	 	return;
   } 
   
   var value=parent.mainFrame.document.acciframe.editCustomerForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ")!=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(parent.mainFrame.document.acciframe.editCustomerForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.acciframe.editCustomerForm.phone.value="";
    	parent.mainFrame.document.acciframe.editCustomerForm.phone.focus();
   	    return;
   } 
    
    var value=parent.mainFrame.document.acciframe.editCustomerForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") !=-1)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(parent.mainFrame.document.acciframe.editCustomerForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.acciframe.editCustomerForm.fax.value="";
    	parent.mainFrame.document.acciframe.editCustomerForm.fax.focus();
   	    return;
   } 
    var stremail = parent.mainFrame.document.acciframe.editCustomerForm.email1.value;
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
		parent.mainFrame.document.acciframe.editCustomerForm.email1.focus();
		return;
	} 
	 if(isEmail(stremail)==false)
  	{
    	alert("Please enter proper email1.");
    	parent.mainFrame.document.acciframe.editCustomerForm.email1.focus();
    	return;
   } 
   }
   var stremail1 = parent.mainFrame.document.acciframe.editCustomerForm.email2.value;
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
		parent.mainFrame.document.acciframe.editCustomerForm.email2.focus();
		return;
	} 
	 if(isEmail(stremail1)==false)
  	{
    	alert("Please enter proper email2.");
    	parent.mainFrame.document.acciframe.editCustomerForm.email2.focus();
    	return;
   } 
   }
    if(IsNumeric(parent.mainFrame.document.acciframe.editCustomerForm.extension.value.replace(/ /g,''))==false)
  {
    alert("Extension should be Numeric.");
  	parent.mainFrame.document.acciframe.editCustomerForm.extension.value="";
    parent.mainFrame.document.acciframe.editCustomerForm.extension.focus();
    return;
   } 
  
  
   if(parent.mainFrame.document.geniframe1.editGeneralInformationForm.spclRemark.value!="" && 
parent.mainFrame.document.geniframe1.editGeneralInformationForm.spclRemark.value.length>200)
   {
   		alert("Special Remark Number should be below 200 digits.");
  		parent.mainFrame.document.geniframe1.editGeneralInformationForm.spclRemark.value="";
    	parent.mainFrame.document.geniframe1.editGeneralInformationForm.spclRemark.focus();
   	    return;
   } 
   if(parent.mainFrame.document.geniframe1.editGeneralInformationForm.defaultRoute.value!="" && 
parent.mainFrame.document.geniframe1.editGeneralInformationForm.defaultRoute.value.length>200)
   {
   		alert("Default Route Number should be below 200 digits.");
  		parent.mainFrame.document.geniframe1.editGeneralInformationForm.defaultRoute.value="";
    	parent.mainFrame.document.geniframe1.editGeneralInformationForm.defaultRoute.focus();
   	    return;
   } 
    if(IsNumeric(parent.mainFrame.document.geniframe1.editGeneralInformationForm.maxDay.value.replace(/ /g,''))==false)
   {
    	alert("Max Day should be Numeric.");
  		parent.mainFrame.document.geniframe1.editGeneralInformationForm.maxDay.value="";
    	parent.mainFrame.document.geniframe1.editGeneralInformationForm.maxDay.focus();
   	    return;
   } 
   if(parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.value!="" && 
parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.value="";
    	parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.focus();
   	    return;
   } 
   
   
   
    if(IsNumeric(parent.mainFrame.document.geniframe1.editGeneralInformationForm.phone1.value.replace(/ /g,''))==false)
  {
    alert("Additional Phone Number should be Numeric.");
  	parent.mainFrame.document.geniframe1.editGeneralInformationForm.phone1.value="";
    parent.mainFrame.document.geniframe1.editGeneralInformationForm.phone1.focus();
    return;
   } 
   if(IsNumeric(parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.value.replace(/ /g,''))==false)
  {
    alert("Additional Fax Number should be Numeric.");
  	parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.value="";
    parent.mainFrame.document.geniframe1.editGeneralInformationForm.fax1.focus();
    return;
   }
   if( parent.mainFrame.document.aciframe.editAccountingForm.arPhone.value!="" &&  
parent.mainFrame.document.aciframe.editAccountingForm.arPhone.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		 parent.mainFrame.document.aciframe.editAccountingForm.arPhone.value="";
    	 parent.mainFrame.document.aciframe.editAccountingForm.arPhone.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value.replace(/ /g,''))==false)
   {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.zipCode.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.aciframe.editAccountingForm.arFax.value.replace(/ /g,''))==false)
   {
    	alert("A/R Fax should be Numeric.");
  		parent.mainFrame.document.aciframe.editAccountingForm.arFax.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.arFax.focus();
   	    return;
   } 
   if(IsNumeric(parent.mainFrame.document.aciframe.editAccountingForm.arPhone.value.replace(/ /g,''))==false)
   {
    	alert("A/R phone should be Numeric.");
  		parent.mainFrame.document.aciframe.editAccountingForm.arPhone.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.arPhone.focus();
   	    return;
   } 
   if(parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value!="")
   {
   if(parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.zipCode.focus();
   	    return;
   }
   else if(parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value.length>5 && 
parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.aciframe.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.zipCode.focus();
   	    return;
   }
   }
  
   if( parent.mainFrame.document.aciframe.editAccountingForm.arFax.value!="" &&  parent.mainFrame.document.aciframe.editAccountingForm.arFax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		 parent.mainFrame.document.aciframe.editAccountingForm.arFax.value="";
    	 parent.mainFrame.document.aciframe.editAccountingForm.arFax.focus();
   	    return;
   } 
    if(IsNumeric(parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
   {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value!="")
   {
   if(parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value.length<5)
   {
  		 alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.focus();
   	    return;
   }
   else if(parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value.length>5 && 
parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.aciframe.editAccountingForm.ffzipCode.focus();
   	    return;
   }
   }
    var stremail2 = parent.mainFrame.document.aciframe.editAccountingForm.email.value;
    if(stremail2!="")
    {
	if(stremail2.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email");
		return;
	}
	if(isEmailSpecial(stremail2)==false)
	{
		alert("Special Characters not allowed in Email.");
		parent.mainFrame.document.aciframe.editAccountingForm.email.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email.");
    	parent.mainFrame.document.aciframe.editAccountingForm.email.focus();
    	return;
   } 
   }*/
}
 else if(netie=="Netscape")
   {
 	/*if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm==undefined)
	{
		 var flag="Account Details";
	}
	if(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm==undefined)
	{
		 var flag="General Information";
		   	    	
	}
	if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm==undefined)
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
  
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.coName.value=="")
	{
		alert("Please enter the C/O Name");
	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.coName.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.coName.focus();
	 	return;
	}  
	var val=parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
  
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.city.value=="")
	{
		alert("Please enter the city");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.city.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.city.focus();
		return;
	}     
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.address1.value=="")
	{
		alert("Please enter the Address1");
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.address1.value="";
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.address1.focus();
		return;
	}
	
	if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.focus();
   	 	return;
   } 
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value!="" && 
parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.focus();
   	 	return;
   } 
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value!="" && 
parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value.length>5 &&
 parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value.length<9)
   {
   		alert("Zipcode should be 9 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.value="";
   	 	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.zip.focus();
   	 	return;
   } 
   var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
				}
	if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value!="")
	{
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.phone.focus();
   	    return;
   } 
    var value=parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value!="")
   {
   if(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.value="";
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.fax.focus();
   	    return;
   } 
    var stremail = parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email1.value;
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
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email1.focus();
		return;
	} 
	 if(isEmail(stremail)==false)
  	{
    	alert("Please enter proper email1.");
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email1.focus();
    	return;
   } 
   }
   var stremail2 = parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email2.value;
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
		parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email2.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email2.");
    	parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.email2.focus();
    	return;
   } 
   }*/
     
   /*if(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.phone1.value!=null && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.phone1.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.phone1.value.length<13)
   {
   		alert("Telephone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.phone1.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.phone1.focus();
   	    return;
   } */
 
   /* if(IsNumeric(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.maxDay.value.replace(/ /g,''))==false)
   {
    	alert("Max Day should be Numeric.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.maxDay.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.maxDay.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.fax1.value!="" &&
    parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.fax1.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.fax1.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.fax1.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.spclRemark.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.spclRemark.value.length>200)
   {
   		alert("Special Remark Number should be below 200 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.spclRemark.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.spclRemark.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.defaultRoute.value!="" && 
   parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.defaultRoute.value.length>200)
   {
   		alert("Default Route Number should be below 200 digits.");
  		parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.defaultRoute.value="";
    	parent.mainFrame.document.getElementById('geniframe').contentDocument.editGeneralInformationForm.defaultRoute.focus();
   	    return;
   }
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arFax.value!="" &&
    parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arFax.value.length<13)
   {
   		alert("Fax Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arFax.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arFax.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arPhone.value!="" &&
    parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arPhone.value.length<13)
   {
   		alert("Phone Number should be 13 digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arPhone.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.arPhone.focus();
   	    return;
   } 
    if(IsNumeric(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value.replace(/ /g,''))==false)
   {
    	alert("ZipCode should be Numeric.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value!="")
   {
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value.length<5)
   {
   		alert("ZipCode should be 5digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.focus();
   	    return;
   } 
   else if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value.length>5 
&& parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value.length<9)
   {
   		alert("ZipCode should be 9digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.zipCode.focus();
   	    return;
   } 
   }
   if(IsNumeric(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
   {
    	alert("ZipCode should be Numeric.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.focus();
   	    return;
   } 
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value!="")
   {
   if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value.length<5)
   {
   		alert("ZipCode should be 5digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.focus();
   	    return;
   } 
   else if(parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value.length>5 && 
parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value.length<9)
   {
   		alert("ZipCode should be 9digits.");
  		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.value="";
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.ffzipCode.focus();
   	    return;
   } 
   }
    var stremail2 = parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.email.value;
	if(stremail2!="")
	{
	if(stremail2.indexOf(" ") != -1)
	{
		alert("Please dont use white space in email");
		return;
	}
	if(isEmailSpecial(stremail2)==false)
	{
		alert("Special Characters not allowed in Email");
		parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.email.focus();
		return;
	} 
	 if(isEmail(stremail2)==false)
  	{
    	alert("Please enter proper email.");
    	parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.email.focus();
    	return;
   } 
 }*/
   
   }	
    	if(netie=="Microsoft Internet Explorer")
    	{
 			//parent.mainFrame.document.acciframe.editCustomerForm.submit();
     		parent.mainFrame.document.acciframe.editGeneralInformationForm.submit();
   			parent.mainFrame.document.aciframe.editAccountingForm.submit();
   			parent.mainFrame.document.editvframe.editvendorForm.submit()
   		}   
  		else if(netie=="Netscape")
   		{
   		  //alert("i m in netscape");
   			//parent.mainFrame.document.getElementById('acciframe').contentDocument.editCustomerForm.submit();
   			parent.mainFrame.document.getElementById('acciframe').contentDocument.editGeneralInformationForm.submit();
   			parent.mainFrame.document.getElementById('aciframe').contentDocument.editAccountingForm.submit();
   			parent.mainFrame.document.getElementById('editvframe').contentDocument.editvendorForm.submit();
   		}
   		document.editCustomForm.buttonValue.value="add";
   		document.editCustomForm.submit();
    }
    
     function confirmdelete()
    {
			
			document.editCustomForm.buttonValue.value="delete";
			var result = confirm("Are you sure you want to delete this Customer");
			if(result)
			{
    			document.editCustomForm.submit();
   	    	}
    }
     function editcancel(val)
    {
	    if(val==0 || val==3)
	     {
	     document.editCustomForm.buttonValue.value="cancel";
		 document.editCustomForm.submit();
	     }
		 else
	     {
			/*var result=confirm("Do you want to save this record?");
			   if(result)
			   {
			   editsave();
			  if(val==0 || val==3 ){
			   document.editCustomForm.buttonValue.value="cancelview";
	           }
			  else 
			   {*/
			    document.editCustomForm.buttonValue.value="cancel";
			    document.editCustomForm.submit();
		 }
		   
   }
    		
    
    function disabled(val1,val2)
   	{
   	
			if(val1 == 0 || val1 == 3)
			{
       			 var imgs = document.getElementsByTagName('img');
   				 for(var k=0; k<imgs.length; k++)
   				 {
   		 			if(imgs[k].id!="cancel")
   		 			{
   		    			imgs[k].style.visibility = 'hidden';
   		 			}
   				 }
   				var input = document.getElementsByTagName("input");
   				for(i=0; i<input.length; i++)
	 			{
	 				if(input[i].id != "buttonValue" && input[i].id !="save" && input[i].id !="delete" && input[i].id !="cancel")
	 				{
	  					alert(input[i].name);
	  					input[i].disabled = true;
	  				}
  	 			}
  	 			var select = document.getElementsByTagName("select");
   				for(i=0; i<select.length; i++)
	 			{
	 				
	  					select[i].disabled = true;
	  				
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
    </script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form action="/editCustom" scope="request">
		
			<table width="100%" cellpadding="0" cellspacing="0">
<tr class="textlabels">
 <td><bean:message key="form.customerForm.name" /></td>
 <td><html:text property="name" value="<%=tradingpartner.getAccountName()%>" onkeyup="toUppercase(this)"/></td>
 <td>Account No</td>
 <td><html:text property="accountno" value="<%=tradingpartner.getAccountno()%>" readonly="true" styleClass="areahighlightgreydefult"/></td>

<td>Master</td>
<td><html:select property="master" styleClass="smalldropdownStyledefault" value="<%=master%>">
      		<html:optionsCollection name="mastertypelist"/>
            </html:select></td>
<td><input type="button" class="buttonStyleNew" value="Save" id="save" onclick="editsave()"/></td>
<td>  <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="confirmdelete()"  /></td>
<td>  <input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="editcancel(<%=modify%>)"  /></td>

</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

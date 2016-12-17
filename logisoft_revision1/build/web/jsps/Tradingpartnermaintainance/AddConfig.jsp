<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerContact"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for CustomerForm form</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" src="<%=path%>/js/FormChek.js" ></script>
    <script type="text/javascript">
    
    function toUppercase(obj) 
	 {
		obj.value = obj.value.toUpperCase();
	 }
     
    function goBack()
     {
    	 document.tradingPartnerForm.buttonValue.value="goBack";
    	 document.tradingPartnerForm.submit();
     }
    	
    function deleteContactInfo()
     {
	     document.tradingPartnerForm.buttonValue.value="deleteContactConfiguration";
		   var result = confirm("Are you sure you want to delete this Contact ");
			if(result)
			{
		   		document.tradingPartnerForm.submit();
		   	}
     }
    
   function saveContactInfo()
    {
   			alert("save");
		   <%-- if(document.tradingPartnerForm.firstName.value=="")
		   {
			   alert("Please enter the First Name");
			   document.tradingPartnerForm.firstName.value="";
			   document.tradingPartnerForm.firstName.focus();
			   return;
		   }
		   var val=document.tradingPartnerForm.firstName.value
		           if(val.match(" "))
		           {
		            	alert("WhiteSpace is not allowed for First Name");
		            	return;
		           }
		   if(isSpecial(document.tradingPartnerForm.firstName.value)==false)
		   {
			    alert("Special Characters not allowed for First Name.");
			  	document.tradingPartnerForm.firstName.value="";
			    document.tradingPartnerForm.firstName.focus();
			    return;
		   } 
		   alert("position")  
		  if(isSpecial(document.tradingPartnerForm.position.value)==false)
		  {
			    alert("Special Characters not allowed for Position.");
			  	document.tradingPartnerForm.position.value="";
			    document.tradingPartnerForm.position.focus();
			    return;
		  } 
		  alert("lastname")
		 if(isSpecial(document.tradingPartnerForm.lastName.value)==false)
		  {
			    alert("Special Characters not allowed for Last Name.");
			  	document.tradingPartnerForm.lastName.value="";
			    document.tradingPartnerForm.lastName.focus();
			    return;
		  }  
		  alert("phone");
		   var value=document.tradingPartnerForm.phone.value;
					
					for(var i=0;i< value.length;i++)
					{
						if(value.indexOf(" ") == 0)
						{
							alert("Please dont start with white space");
							return;
						}
					}
   
		   if(IsNumeric(document.tradingPartnerForm.phone.value.replace(/ /g,''))==false)
			{
			    alert("Telephone Number should be Numeric.");
			  	document.tradingPartnerForm.phone.value="";
			    document.tradingPartnerForm.phone.focus();
			    return;
		    } 
		    alert("extension");
		  if(IsNumeric(document.tradingPartnerForm.extension.value.replace(/ /g,''))==false)
		   {
			    alert("Extension should be Numeric.");
			  	document.tradingPartnerForm.extension.value="";
			    document.tradingPartnerForm.extension.focus();
			    return;
		   } 
		   if(document.tradingPartnerForm.phone.value!="" && document.tradingPartnerForm.phone.value.length<13)
		   {
			   alert("Phone Number should be 13 Digits");
			   document.tradingPartnerForm.phone.value="";
			   document.tradingPartnerForm.phone.focus();
			   return;
		   }  
		    alert("fax");
		   var value=document.tradingPartnerForm.fax.value;
					
					for(var i=0;i< value.length;i++)
					{
						if(value.indexOf(" ") == 0)
						{
							alert("Please dont start with white space");
							return;
						}
					}
   
		   if(IsNumeric(document.tradingPartnerForm.fax.value.replace(/ /g,''))==false)
		   {
			    alert("Fax Number should be Numeric.");
			  	document.tradingPartnerForm.fax.value="";
			    document.tradingPartnerForm.fax.focus();
			    return;
		   } 
		   if(document.tradingPartnerForm.fax.value!="" && document.tradingPartnerForm.fax.value.length<13)
		   {
			   alert("Fax Number should be 13 Digits");
			   document.tradingPartnerForm.fax.value="";
			   document.tradingPartnerForm.fax.focus();
			   return;
		   }  
		   alert("comment");
		   if(document.tradingPartnerForm.comment.value!="" && document.tradingPartnerForm.comment.value.length>100)
		   {
			   alert("Comment should be only 100 characters");
			   document.tradingPartnerForm.comment.value="";
			   document.tradingPartnerForm.comment.focus();
			   return;
		   } --%>
   		alert("lastline");
	    document.tradingPartnerForm.buttonValue.value="saveContactConfiguration";
	    document.tradingPartnerForm.submit();
	    alert("notvisible");
    }
     
     
    function saveAfterEdit()
    {
   		 if(document.tradingPartnerForm.firstName.value=="")
		   {
		   alert("Please enter the First Name");
		   document.tradingPartnerForm.firstName.value="";
		   document.tradingPartnerForm.firstName.focus();
		   return;
		   }
		   var val=document.tradingPartnerForm.firstName.value
		           if(val.match(" "))
		           {
		            	alert("WhiteSpace is not allowed for First Name");
		            	return;
		           }
		   if(isSpecial(document.tradingPartnerForm.firstName.value)==false)
		    {
			    alert("Special Characters not allowed for First Name.");
			  	document.tradingPartnerForm.firstName.value="";
			    document.tradingPartnerForm.firstName.focus();
			    return;
		    }   
          if(isSpecial(document.tradingPartnerForm.position.value)==false)
		   {
			    alert("Special Characters not allowed for Position.");
			  	document.tradingPartnerForm.position.value="";
			    document.tradingPartnerForm.position.focus();
			    return;
		   }   
           
		   if(isSpecial(document.tradingPartnerForm.lastName.value)==false)
		  	{
			    alert("Special Characters not allowed for Last Name.");
			  	document.tradingPartnerForm.lastName.value="";
			    document.tradingPartnerForm.lastName.focus();
			    return;
		   	}  
		   var value=document.tradingPartnerForm.phone.value;
					
					for(var i=0;i< value.length;i++)
					{
						if(value.indexOf(" ") == 0)
						{
							alert("Please dont start with white space");
							return;
						}
					}
   
		   if(IsNumeric(document.tradingPartnerForm.phone.value.replace(/ /g,''))==false)
		  	{
			    alert("Telephone Number should be Numeric.");
			  	document.tradingPartnerForm.phone.value="";
			    document.tradingPartnerForm.phone.focus();
			    return;
		   } 
  
		   //if(document.tradingPartnerForm.phone.value!="" && document.addConfigForm.phone.value.length<13)
		   //{
			  // alert("Phone Number should be 13 Digits");
			   //document.tradingPartnerForm.phone.value="";
			   //document.tradingPartnerForm.phone.focus();
			   //return;
		  // }  
		    var value=document.tradingPartnerForm.fax.value;
					
					for(var i=0;i< value.length;i++)
					{
						if(value.indexOf(" ") == 0)
						{
							alert("Please dont start with white space");
							return;
						}
					}
   
		   if(IsNumeric(document.tradingPartnerForm.fax.value.replace(/ /g,''))==false)
		    {
			    alert("Fax Number should be Numeric.");
			  	document.tradingPartnerForm.fax.value="";
			    document.tradingPartnerForm.fax.focus();
			    return;
		    } 
		   //if(document.tradingPartnerForm.fax.value!="" && document.addConfigForm.fax.value.length<13)
		    //{
			  // alert("Fax Number should be 13 Digits");
			  // document.tradingPartnerForm.fax.value="";
			  // document.tradingPartnerForm.fax.focus();
			  // return;
		    //} 
		  var stremail = document.tradingPartnerForm.email.value;
		  if(stremail!="")
		  {
				if(stremail.indexOf(" ") != -1)
				{
					alert("Please dont use white space in email");
					return;
				}
				if(isEmailSpecial(stremail)==false)
				{
					alert("Special Characters not allowed in Email.");
					document.tradingPartnerForm.email.focus();
					return;
				} 
				if(isEmail(stremail)==false)
			  	{
			    	alert("Please enter proper email.");
			    	document.tradingPartnerForm.email.focus();
			    	return;
			    }  
           }
		   if(document.tradingPartnerForm.comment.value!="" && document.tradingPartnerForm.comment.value.length>100)
		   {
			   alert("Comment should be only 100 characters");
			   document.tradingPartnerForm.comment.value="";
			   document.tradingPartnerForm.comment.focus();
			   return;
		   }
   
	    document.tradingPartnerForm.buttonValue.value="editContactConfiguration";
	    document.tradingPartnerForm.submit();
    }
    
   	function disabled(val1,val2)
   	{
		if(val1 == 0 || val1== 3)
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
	  	 	var textarea = document.getElementsByTagName("textarea");
	   		
	   		for(i=0; i<textarea.length; i++)
		 	{
		 		textarea[i].disabled = true;
	  	 	}
	   }
	  	 if(val1 == 1)
	  	 {
	  	 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 }
    }
   
</script>
  <%@include file="../includes/resources.jsp" %>
</head>
<body class="whitebackgrnd" >
<html:form action="/tradingPartner" scope="request">
<%
if (request.getAttribute("ADD_ADDRESS") != null) {
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew" colspan="2"><font style="font-weight: bold">Enter Contact Details</font></tr>
<tr>
 	
 	<td align="right">
 	<input type="button" class="buttonStyleNew" value="Go Back" id="cancel"  onclick="goBack()" />
 	<input type="button" class="buttonStyleNew" value="Save" onclick="saveContactInfo()" id="save"/></td>
 <%--<%if(session.getAttribute("custConfig")!=null)
 	 {
 	 %>
 	 <input type="button" class="buttonStyleNew" value="Save" id="save" onclick="saveAfterEdit()" />
 	 <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="deleteContactInfo()"/>
	<%
	}
	else
	{
	%>
	<input type="button" class="buttonStyleNew" value="Save" onclick="saveContactInfo()" id="save"/>
 	<%} %>
--%></tr>

<tr style="padding:top:50px;">
  <td> 
    <table width="100%" >
    <tr class="style2">
  		<td><bean:message key="form.customerForm.firstname" /></td>
  		<td><html:text property="firstName" value="" onkeyup="toUppercase(this)"/></td>
  		<td><bean:message key="form.customerForm.lastname" /></td>
 	    <td><html:text property="lastName" value="" onkeyup="toUppercase(this)"/></td>
  	</tr>
  	<tr class="style2">	
  		<td><bean:message key="form.customerForm.position" /></td>
        <td><html:text property="position" value="" onkeyup="toUppercase(this)"/></td>
        <td><bean:message key="form.customerForm.email" /></td>
        <td><html:text property="email" value="" /></td>
   </tr>
   <tr class="style2">
  	    <td><bean:message key="form.customerForm.phoneno" /></td>
        <td><html:text property="phone" value="" maxlength="13" onkeypress="getIt(this)"/></td>
  	    <td><bean:message key="form.customerForm.extension" /></td>
        <td><html:text property="extension" value="" maxlength="4" /></td>
   </tr>
   <tr class="style2">
      <td><bean:message key="form.customerForm.faxno" /></td>
      <td><html:text property="fax" value="" maxlength="13" onkeypress="getIt(this)"/></td>
      <td><bean:message key="form.customerForm.comments" /></td>
      <td><html:textarea property="comment" value="" styleClass="textareastyle" cols="25" onkeyup="toUppercase(this)"/></td>
  </tr>
 </table>
 </td>
</tr>

<tr>
      <td style="padding:top:10px;">&nbsp;</td>
</tr>


<tr style="padding:top:50px;">
  <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr class="tableHeadingNew" colspan="2"><font style="font-weight: bold">Auto Notification Configuration</font> </tr>
    <tr><td>
	<table width="100%" border="0">
   	   <tr class="style2">
            <td>CODE A</td>
            <td><html:select property="codea" styleClass="selectboxstyle" value=""> 
                   
                    </html:select></td>
  			<td align="right">CODE E</td>
 			<td><html:select property="codee" styleClass="selectboxstyle" value="">
                  
                    </html:select></td>
  	 </tr>
	 <tr class="style2">
  			<td>CODE B</td>
 			<td><html:select property="codeb" styleClass="selectboxstyle" value="">
                  
                    </html:select></td>
  			<td align="right">CODE F</td>
 			<td><html:select property="codef" styleClass="selectboxstyle" value="">
                 
                    </html:select></td>
	</tr>
	<tr class="style2">

  			<td>CODE C</td>
  			<td><html:select property="codec" styleClass="selectboxstyle" value="">
                  
                    </html:select></td>
  			<td align="right">CODE G</td>
  			<td><html:select property="codeg" styleClass="selectboxstyle" value="">
                   
                    </html:select></td>
	</tr>
	<tr class="style2">
 			<td>CODE D</td>
  			<td><html:select property="coded" styleClass="selectboxstyle" value="">
                  
                    </html:select></td>
  			<td align="right">CODE H</td>

 			 <td><html:select property="codeh" styleClass="selectboxstyle" value="">
                  
                    </html:select></td>
	</tr>
	<tr class="style2">
 			 <td>&nbsp;</td>
  			 <td>&nbsp;</td>
             <td align="right">CODE  I</td>
             <td><html:select property="codei" styleClass="selectboxstyle" value="">
                 
                    </html:select></td>
    </tr>
    <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
    </tr>
   </table>
   </td>
   </tr>
</table>
</td></tr>
      <html:hidden property="buttonValue" styleId="buttonValue"/>
</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


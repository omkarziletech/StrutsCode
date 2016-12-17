<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.ConsigneeConfig"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String modify="";
String codea="0";
String codeb="0";
String phone="";
String fax="";
ConsigneeConfig custConfig=new ConsigneeConfig();
if(session.getAttribute("custConfig")!=null)
{
custConfig=(ConsigneeConfig)session.getAttribute("custConfig");
phone=dbUtil.appendstring(custConfig.getPhone());
fax=dbUtil.appendstring(custConfig.getFax());
if(custConfig!=null && custConfig.getCodea()!=null && custConfig.getCodea().getId()!=null)
{
codea=custConfig.getCodea().getId().toString();
}
if(custConfig!=null && custConfig.getCodeb()!=null && custConfig.getCodeb().getId()!=null)
{
codeb=custConfig.getCodeb().getId().toString();
}
}
modify = (String) session.getAttribute("modifyforcustomer");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
	}
request.setAttribute("acodelist",dbUtil.getspecialcodelist(22,"yes","Select Special Code","A"));
request.setAttribute("bcodelist",dbUtil.getspecialcodelist(22,"yes","Select Special Code","B"));
String message="";
if(session.getAttribute("message")!=null)
{
 message=(String)session.getAttribute("message");
} 
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for ConsigneeConfigForm form</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
    function addsave1()
    {
    if(document.addConsigneeConfigForm.firstName.value=="")
   {
   alert("Please enter the First Name");
   document.addConsigneeConfigForm.firstName.value="";
   document.addConsigneeConfigForm.firstName.focus();
   return;
   }
   var val=document.addConsigneeConfigForm.firstName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for First Name");
            	return;
           }
   if(isSpecial(document.addConsigneeConfigForm.firstName.value)==false)
  {
    alert("Special Characters not allowed for First Name.");
  	document.addConsigneeConfigForm.firstName.value="";
    document.addConsigneeConfigForm.firstName.focus();
    return;
   }   
     if(isSpecial(document.addConsigneeConfigForm.position.value)==false)
  {
    alert("Special Characters not allowed for Position.");
  	document.addConsigneeConfigForm.position.value="";
    document.addConsigneeConfigForm.position.focus();
    return;
   }   
           
   if(isSpecial(document.addConsigneeConfigForm.lastName.value)==false)
  {
    alert("Special Characters not allowed for Last Name.");
  	document.addConsigneeConfigForm.lastName.value="";
    document.addConsigneeConfigForm.lastName.focus();
    return;
   }  
   var value=document.addConsigneeConfigForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.addConsigneeConfigForm.phone.value.replace(/ /g,''))==false)
  {
    alert("Telephone Number should be Numeric.");
  	document.addConsigneeConfigForm.phone.value="";
    document.addConsigneeConfigForm.phone.focus();
    return;
   } 
  
   if(document.addConsigneeConfigForm.phone.value!="" && document.addConsigneeConfigForm.phone.value.length<13)
   {
   alert("Phone Number should be 13 Digits");
   document.addConsigneeConfigForm.phone.value="";
   document.addConsigneeConfigForm.phone.focus();
   return;
   }  
    var value=document.addConsigneeConfigForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.addConsigneeConfigForm.fax.value.replace(/ /g,''))==false)
  {
    alert("Fax Number should be Numeric.");
  	document.addConsigneeConfigForm.fax.value="";
    document.addConsigneeConfigForm.fax.focus();
    return;
   } 
   if(document.addConsigneeConfigForm.fax.value!="" && document.addConsigneeConfigForm.fax.value.length<13)
   {
   alert("Fax Number should be 13 Digits");
   document.addConsigneeConfigForm.fax.value="";
   document.addConsigneeConfigForm.fax.focus();
   return;
   }  
   if(document.addConsigneeConfigForm.comment.value!="" && document.addConsigneeConfigForm.comment.value.length>100)
   {
   alert("Comment should be only 100 characters");
   document.addConsigneeConfigForm.comment.value="";
   document.addConsigneeConfigForm.comment.focus();
   return;
   }
   
    document.addConsigneeConfigForm.buttonValue.value="add";
    document.addConsigneeConfigForm.submit();
    }
     function editsave1()
    {
    if(document.addConsigneeConfigForm.firstName.value=="")
   {
   alert("Please enter the First Name");
   document.addConsigneeConfigForm.firstName.value="";
   document.addConsigneeConfigForm.firstName.focus();
   return;
   }
   var val=document.addConsigneeConfigForm.firstName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for First Name");
            	return;
           }
   if(isSpecial(document.addConsigneeConfigForm.firstName.value)==false)
  {
    alert("Special Characters not allowed for First Name.");
  	document.addConsigneeConfigForm.firstName.value="";
    document.addConsigneeConfigForm.firstName.focus();
    return;
   }   
     if(isSpecial(document.addConsigneeConfigForm.position.value)==false)
  {
    alert("Special Characters not allowed for Position.");
  	document.addConsigneeConfigForm.position.value="";
    document.addConsigneeConfigForm.position.focus();
    return;
   }   
           
   if(isSpecial(document.addConsigneeConfigForm.lastName.value)==false)
  {
    alert("Special Characters not allowed for Last Name.");
  	document.addConsigneeConfigForm.lastName.value="";
    document.addConsigneeConfigForm.lastName.focus();
    return;
   }  
   var value=document.addConsigneeConfigForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.addConsigneeConfigForm.phone.value.replace(/ /g,''))==false)
  {
    alert("Telephone Number should be Numeric.");
  	document.addConsigneeConfigForm.phone.value="";
    document.addConsigneeConfigForm.phone.focus();
    return;
   } 
  
   if(document.addConsigneeConfigForm.phone.value!="" && document.addConsigneeConfigForm.phone.value.length<13)
   {
   alert("Phone Number should be 13 Digits");
   document.addConsigneeConfigForm.phone.value="";
   document.addConsigneeConfigForm.phone.focus();
   return;
   }  
    var value=document.addConsigneeConfigForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.addConsigneeConfigForm.fax.value.replace(/ /g,''))==false)
  {
    alert("Fax Number should be Numeric.");
  	document.addConsigneeConfigForm.fax.value="";
    document.addConsigneeConfigForm.fax.focus();
    return;
   } 
   if(document.addConsigneeConfigForm.fax.value!="" && document.addConsigneeConfigForm.fax.value.length<13)
   {
   alert("Fax Number should be 13 Digits");
   document.addConsigneeConfigForm.fax.value="";
   document.addConsigneeConfigForm.fax.focus();
   return;
   }  
   if(document.addConsigneeConfigForm.comment.value!="" && document.addConsigneeConfigForm.comment.value.length>100)
   {
   alert("Comment should be only 100 characters");
   document.addConsigneeConfigForm.comment.value="";
   document.addConsigneeConfigForm.comment.focus();
   return;
   }
   
    document.addConsigneeConfigForm.buttonValue.value="edit";
    document.addConsigneeConfigForm.submit();
    }
    function cancel1()
    {
    document.addConsigneeConfigForm.buttonValue.value="cancel";
    document.addConsigneeConfigForm.submit();
    }
     function confirmnote()
	{
		document.addConsigneeConfigForm.buttonValue.value="note";
    	document.addConsigneeConfigForm.submit();
   	}
   	 function deleteform1()
    {
  
    document.addConsigneeConfigForm.buttonValue.value="deleteconfig";
    var result = confirm("Are you sure you want to delete this Contact ");
	if(result)
	{
   		document.addConsigneeConfigForm.submit();
   	}
    
    }
   	 function disabled(val1,val2)
   {
 
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id!="cancel" )
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
  	 		
    }
    </script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form action="/addConsigneeConfig" scope="request">
			<table width="711" border="0" cellpadding="0" cellspacing="0">

<tr class="style2">
  <td width="480" align="left" class="headerbluelarge"> <bean:message key="form.customerForm.entercontactdetail" /> </td>
  
 <td width="108"><img src="<%=path%>/img/previous.gif" onclick="cancel1()" id="cancel"/></td>
 <%if(session.getAttribute("custConfig")!=null)
 {
 %>
 <td width="95"><img src="<%=path%>/img/save.gif" border="0" onclick="editsave1()" id="save"/></td>

<td width="108"><img src="<%=path%>/img/delete.gif" id="delete" onclick="deleteform1()"/></td>
<%
}
else
{
%>
 <td width="95"><img src="<%=path%>/img/save.gif" border="0" onclick="addsave1()" id="save"/></td>
 <%} %>
</tr>


  <tr>
  <td colspan="3"></td></tr>
     <tr>
    <td height="12" colspan="25"  class="headerbluesmall">&nbsp;<bean:message key="form.customerForm.contactdetail" /> </td> 
  </tr>
</table>
<table>


<tr class="style2">
  <td width="139"><bean:message key="form.customerForm.firstname" />
  <td width="142"><bean:message key="form.customerForm.lastname" />
  <td colspan="4"><bean:message key="form.customerForm.position" /></td>
  <td colspan="4">&nbsp;</td>
  </tr>
<tr>

  <td><html:text property="firstName" value="<%=custConfig.getFirstName()%>"/></td>
  <td><html:text property="lastName" value="<%=custConfig.getLastName()%>"/></td>
  <td><html:text property="position" value="<%=custConfig.getPosition()%>"/></td>
  <td colspan="4">&nbsp;</td>
</tr>
<tr class="style2">
  <td><bean:message key="form.customerForm.phoneno" /></td>
  <td><bean:message key="form.customerForm.faxno" /> </td>
  <td><bean:message key="form.customerForm.email" /></td>

  <td colspan="4">&nbsp;</td>
</tr>
<tr class="style2">
  <td><html:text property="phone" value="<%=phone%>" maxlength="13" onkeypress="getIt(this)"/></td>
  <td><html:text property="fax" value="<%=fax%>" maxlength="13" onkeypress="getIt(this)"/></td>
  <td><html:text property="email" value="<%=custConfig.getEmail()%>"/></td>
  <td colspan="4">&nbsp;</td>
</tr>
<tr class="style2">
  <td><bean:message key="form.customerForm.comments" /></td>

  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td colspan="4">&nbsp;</td>
</tr>
<tr>
  <td colspan="3"><html:textarea property="comment" value="<%=custConfig.getComment()%>" styleClass="textareastyle" cols="50" /></td>
  <td width="188" colspan="4">&nbsp;</td>
</tr>

</table>
<table width="703" border="0" cellpadding="0" cellspacing="0">

<tr></tr>
  <tr>
  <td width="703"></td></tr>
     <tr>
    <td height="7" colspan="25" class="headerbluesmall">&nbsp;<bean:message key="form.customerForm.autonotification" /> </td> 
  </tr>
<tr height="5"></tr>
</table>
<table>

<tr class="style2">
  <td width="203"><bean:message key="form.customerForm.codea" /></td>
  <td width="137"><html:select property="codea" styleClass="selectboxstyle" value="<%=codea%>">
                   <html:optionsCollection name="acodelist"/>
                    </html:select></td>

  
  <td width="219"><bean:message key="form.customerForm.codee" /></td>
 <td colspan="4"><html:select property="codee" styleClass="selectboxstyle">
                  
                    </html:select></td>
  </tr>
<tr class="style2">
  <td><bean:message key="form.customerForm.codeb" /> </td>
 <td><html:select property="codeb" styleClass="selectboxstyle" value="<%=codeb%>">
                  <html:optionsCollection name="bcodelist"/>
                    </html:select></td>
  <td><bean:message key="form.customerForm.codef" /></td>
  <td colspan="4"><html:select property="codef" styleClass="selectboxstyle">
                 
                    </html:select></td>
</tr>
<tr class="style2">

  <td><bean:message key="form.customerForm.codec" /> </td>
  <td><html:select property="codec" styleClass="selectboxstyle">
                   
                    </html:select></td>
  <td><bean:message key="form.customerForm.codeg" /> </td>
  <td colspan="4"><html:select property="codeg" styleClass="selectboxstyle">
                   
                    </html:select></td>
</tr>
<tr class="style2">
  <td><bean:message key="form.customerForm.coded" /> </td>
  <td><html:select property="coded" styleClass="selectboxstyle">
                   
                    </html:select></td>
  <td><bean:message key="form.customerForm.codeh" /> </td>

  <td colspan="4"><html:select property="codeh" styleClass="selectboxstyle">
                  
                    </html:select></td>
</tr>
<tr class="style2">
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td><bean:message key="form.customerForm.codei" /> </td>
  <td colspan="4"><html:select property="codei" styleClass="selectboxstyle">
                  
                    </html:select></td>
</tr>
<tr>
  <td colspan="3">&nbsp;</td>
  <td width="131" colspan="4">&nbsp;</td>
</tr>
</table>

      <html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.Consignee,com.gp.cong.logisoft.domain.GenericCode,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
Consignee consignee =new Consignee();

request.setAttribute("countrylist",dbUtil.getGenericCodeList(11,"yes","Select Country"));
GenericCode genericCode = null;
String countryId="219";
String cityId="0";
String state="";
String message="";

String msg = "";

	
	if (request.getAttribute("message") != null)
	{
		msg = (String) request.getAttribute("message");
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

List cities=new ArrayList();
if(session.getAttribute("consignee")!=null)
{
consignee=(Consignee)session.getAttribute("consignee");
genericCode= consignee.getCountry();

if(consignee!=null && consignee.getCountry()!=null && consignee.getCountry().getId()!=null)
   {
   	 countryId=consignee.getCountry().getId().toString();
   }
 if(consignee!=null && consignee.getCity()!=null && consignee.getCity().getId()!=null)
   {
   	 cityId=consignee.getCity().getId().toString();
   }
   if(consignee!=null && consignee.getState()!=null )
   {
   	 state=consignee.getState();
   }	
}
request.setAttribute("consignee",consignee);
if(genericCode!=null && genericCode.getCodedesc()!=null)
{
cities=dbUtil.getCityList(genericCode);
}
request.setAttribute("citylist",cities);

%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for editConsigneeForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
    function save()
	{
	if(document.editConsigneeForm.consigNo.value=="")
	{
		alert("Please enter the Consignee No");
		document.editConsigneeForm.consigNo.value="";
		document.editConsigneeForm.consigNo.focus();
		return;
	}
	var val=document.editConsigneeForm.consigNo.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee no");
            	return;
           }
   
   if(document.editConsigneeForm.consigName.value=="")
	{
		alert("Please enter the  Consignee Name");
		document.editConsigneeForm.consigName.value="";
		document.editConsigneeForm.consigName.focus();
		return;
	}
	var val=document.editConsigneeForm.consigName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for Consignee Name");
            	return;
           }
   if(document.editConsigneeForm.coName.value=="0")
	{
		alert("Please enter the C/O Name");
	 	document.editConsigneeForm.coName.value="";
		document.editConsigneeForm.coName.focus();
	 	return;
	}  
	var val=document.editConsigneeForm.coName.value
           if(val.match(" "))
           {
            	alert("WhiteSpace is not allowed for C/O Name");
            	return;
           }
   if(document.editConsigneeForm.country.value=="0")
   {
		alert("Please enter the country");
		document.editConsigneeForm.country.value="";
		document.editConsigneeForm.country.focus();
		return;
	}   
	if(document.editConsigneeForm.city.value=="0")
	{
		alert("Please enter the city");
		document.editConsigneeForm.city.value="";
		document.editConsigneeForm.city.focus();
		return;
	}     
	if(document.editConsigneeForm.address1.value=="")
	{
		alert("Please enter the Address1");
		document.editConsigneeForm.address1.value="";
		document.editConsigneeForm.address1.focus();
		return;
	}
	
	if(IsNumeric(document.editConsigneeForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		document.editConsigneeForm.zip.value="";
   	 	document.editConsigneeForm.zip.focus();
   	 	return;
   } 
   var value=document.editConsigneeForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.editConsigneeForm.phone.value.replace(/ /g,''))==false)
   {
    	alert("Telephone Number should be Numeric.");
  		document.editConsigneeForm.phone.value="";
    	document.editConsigneeForm.phone.focus();
   	    return;
   } 
    var value=document.editConsigneeForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
   
   if(IsNumeric(document.editConsigneeForm.fax.value.replace(/ /g,''))==false)
   {
    	alert("Fax Number should be Numeric.");
  		document.editConsigneeForm.fax.value="";
    	document.editConsigneeForm.fax.focus();
   	    return;
   } 
   /*
  for(i = 0; i < parent.mainFrame.document.genigrame.document.all.length; i++){
   alert(parent.mainFrame.document.getElementById('geniframe').document.all(i).id);
}*/
//var geniframeobj=parent.mainFrame.document.geniframe.generalInformationForm;
//var aciframeobj=parent.mainFrame.document.aciframe.accountingForm;
//var custiframeobj=parent.mainframe.document.custiframe.contactConfigForm;
   //alert(parent.mainFrame.document.geniframe.generalInformationForm);
  // geniframeobj.submit();
 // aciframeobj.submit();
  //custiframeobj.submit();
   document.editConsigneeForm.buttonValue.value="add";
   document.editConsigneeForm.submit();
   }
    function selectcity()
    {
   
    if(document.editConsigneeForm.country.value=="0")
    {
    	alert("Please enter the country");
   	    return;
    }
    document.editConsigneeForm.buttonValue.value="selectcity";
    document.editConsigneeForm.submit();
    }
    function selectstate()
    {
    if(document.editConsigneeForm.city.value=="0")
    {
    	alert("Please enter the city");
    	return;
    }
     document.editConsigneeForm.buttonValue.value="selectstate";
    document.editConsigneeForm.submit();
    }
    function cancel()
    {
    document.editConsigneeForm.buttonValue.value="cancel";
    document.consigneeForm.submit();
    }
   function confirmnote()
		{
			document.editConsigneeForm.buttonValue.value="note";
   	 	document.editConsigneeForm.submit();
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
  	
    }
    </script>
	</head>
	 <body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form action="/editConsignee" scope="request">
		 <font color="blue"><h4><%=message%></h4></font>
		<table width="812" cellpadding="0" cellspacing="0">
<tr class="textlabels">
<td width="589">
  <p align="left" class="headerbluelarge"><bean:message key="form.consigneeForm.accountdetail" /></p>
</td>
<td width="78" align="left"><img src="<%=path%>/img/note.gif" id="note"
										onclick="confirmnote()" />
								</td>
</tr>
<tr height="30"></tr>
<tr>
<td height="12"  class="headerbluesmall" colspan="10">&nbsp;&nbsp;<bean:message key="form.consigneeForm.addressinfo" /></td>
</tr>
</table>
<table width="811" height="173">
  <tr class="textlabels">
    <td valign="top" height="20">&nbsp;</td>
    <td width="129" valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>

    <td width="131" valign="top">&nbsp;</td>
    <td valign="top">&nbsp;</td>
    <td width="124" valign="top">&nbsp;</td>
  </tr>
  <tr class="style2">
    <td valign="top" width="60" height="27"><bean:message key="form.consigneeForm.consno" /></td>
    <td valign="top"><html:text property="consigNo" styleClass="areahighlightyellow" value="<%=consignee.getConsigNo()%>" maxlength="5" readonly="true"/></td>
    <td valign="top" width="58"><bean:message key="form.consigneeForm.name" /></td>
		<td valign="top" width="131"><html:text property="consigName" value="<%=consignee.getConsigName()%>" maxlength="50"/></td>
    <td valign="top" width="82"><bean:message key="form.consigneeForm.coname" /></td>
    <td valign="top"><html:text property="coName" value="<%=consignee.getCoName()%>" maxlength="50"/></td>
    
    
  </tr>
  <tr class="style2">
  
    <td valign="top"height="26"><bean:message key="form.consigneeForm.address1" /></td>
    <td valign="baseline"><html:text property="address1" value="<%=consignee.getAddress1()%>" maxlength="100"/></td>
    <td valign="top"><bean:message key="form.consigneeForm.address2" /></td>
    <td valign="top"><html:text property="address2" value="<%=consignee.getAddress2()%>" maxlength="100"/></td>
  	<td valign="top"><bean:message key="form.consigneeForm.country" /></td>
  	<td valign="top"><html:select property="country" styleClass="selectboxstyle" onchange="selectcity()" value="<%=countryId%>">
            		<html:optionsCollection name="countrylist" />
                	</html:select></td>

    
  </tr>

  <tr class="style2">
   
    <td valign="top"width="60"><bean:message key="form.consigneeForm.city" /></td>

    <td valign="top"><html:select property="city" styleClass="selectboxstyle" onchange="selectstate()" value="<%=cityId%>">
                   <html:optionsCollection name="citylist" />
                	</html:select></td>
    <td valign="top" height="26"><bean:message key="form.consigneeForm.state" /></td>
    <td valign="top"><html:text property="state" value="<%=state%>" readonly="true"/></td>
    <td valign="top"><bean:message key="form.consigneeForm.phone" /> </td>
    <td valign="top"><html:text property="phone" value="<%=consignee.getPhone()%>" onkeypress="getIt(this)" maxlength="13"/></td>
  </tr>
  <tr class="style2">
  	<td  valign="top"><bean:message key="form.consigneeForm.zip" /></td>
    <td  valign="top"><html:text property="zip" value="<%=consignee.getZip()%>" maxlength="10"/></td>
    <td valign="top" height="26"><bean:message key="form.consigneeForm.fax" /></td>
    <td valign="top"><html:text property="fax" value="<%=consignee.getFax()%>" onkeypress="getIt(this)" maxlength="13"/></td>
    <td valign="top"><bean:message key="form.consigneeForm.email1" /></td>

    <td valign="top"><html:text property="email1" value="<%=consignee.getEmail1()%>" maxlength="40"/></td>
  </tr>
  <tr class="style2">
  	
    <td  valign="top"><bean:message key="form.consigneeForm.email2" /></td>
    <td  valign="top"><html:text property="email2" value="<%=consignee.getEmail2()%>" maxlength="40"/></td>
    <td  valign="top">&nbsp;</td>
    <td  valign="top">&nbsp;</td>
    <td  valign="top">&nbsp;</td>
    <td  valign="top">&nbsp;</td>
  </tr>
</table>
<html:hidden property="buttonValue"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
		
		
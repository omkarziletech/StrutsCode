<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
Customer customer=new Customer();
DBUtil dbUtil=new DBUtil();
String phone="";
String cityId="";
String state="";
String countryId="";
String fax="";
String editAddress="";
String path1="";
customer.setPrimary("off");
customerBean customerbean=new customerBean();
if(request.getAttribute("path1")!=null)
{
path1=(String)request.getAttribute("path1");
}
if(request.getParameter("editAddress")!=null)
{
editAddress=(String)request.getParameter("editAddress");

session.setAttribute("editAddress",editAddress);
}

if(session.getAttribute("customerbean")!=null)
{
customerbean=(customerBean)session.getAttribute("customerbean");
customer.setPrimary(customerbean.getPrimary());
}


if(session.getAttribute("adressCust")!=null)
{
customer=(Customer)session.getAttribute("adressCust");
customer.setPrimary(customer.getPrimary());
if(customer.getPhone()!=null)
{
phone=dbUtil.appendstring(customer.getPhone());
}
if(customer!=null && customer.getCity2()!=null )
{
   cityId=customer.getCity2();
}
if(customer!=null && customer.getState()!=null )
{
   state=customer.getState();
}
if(customer!=null && customer.getCuntry()!=null && customer.getCuntry().getCodedesc()!=null)
{
   countryId=customer.getCuntry().getCodedesc();
}
}
request.setAttribute("customerbean",customer);
if(request.getAttribute("editaddress")!=null)
{
if(request.getAttribute("editaddress").equals("editaddress"))
{
%>
<script type="text/javascript">
self.close();
opener.location.href="<%=path%>/<%=path1%>";
</script>
<%
}
}
String modify="";

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
	}
%>
<html> 
	<head>
		<title>JSP for EditAddressForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
	<script type="text/javascript">
    function add1()
	{
	   var value=document.editAddressForm.zip.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
        if(IsNumeric(document.editAddressForm.zip.value)==false)
  {
    	alert("Zipcode should be Numeric.");
  		document.editAddressForm.zip.value="";
   	 	document.editAddressForm.zip.focus();
   	 	return;
   }
    if(document.editAddressForm.zip.value!="" && document.editAddressForm.zip.value.length<5)
   {
   		alert("Zipcode should be 5 digits.");
  		document.editAddressForm.zip.value="";
   	 	document.editAddressForm.zip.focus();
   	 	return;
   }
   var value=document.editAddressForm.phone.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
        if(IsNumeric(document.editAddressForm.fax.value)==false)
  {
    	alert("Fax should be Numeric.");
  		document.editAddressForm.phone.value="";
   	 	document.editAddressForm.phone.focus();
   	 	return;
   }
   var value=document.editAddressForm.fax.value;
			
			for(var i=0;i< value.length;i++)
			{
				if(value.indexOf(" ") == 0)
				{
					alert("Please dont start with white space");
					return;
				}
			}
        if(IsNumeric(document.editAddressForm.phone.value)==false)
  {
    	alert("Phone should be Numeric.");
  		document.editAddressForm.phone.value="";
   	 	document.editAddressForm.phone.focus();
   	 	return;
   }
    
	document.editAddressForm.buttonValue.value="add";
    document.editAddressForm.submit();
   	}
   	function cancel1(val1)
   	{
   	
   	if(val1==3)
   	{
   	document.editAddressForm.buttonValue.value="cancelview";
   	}
   	else
   	{
   		document.editAddressForm.buttonValue.value="cancel";
   	}
    document.editAddressForm.submit();
   	}
   	function confirmdelete()
   	{
   	  	document.editAddressForm.buttonValue.value="delete";
    document.editAddressForm.submit();
   	}
   	function popup1(mylink, windowname)
{

if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
mywindow=window.open(href, windowname, 'width=600,height=250,scrollbars=yes');
mywindow.moveTo(200,180);
document.editAddressForm.buttonValue.value="popup";
 document.editAddressForm.submit();
return false;
}
   function phonevalid(obj)
{ 
if(document.editAddressForm.country.value=="" && document.editAddressForm.country.value!="UNITED STATES")
{
    
  		if((document.editAddressForm.phone.value.length>10) || IsNumeric(document.editAddressForm.phone.value.replace(/ /g,''))==false)
 		{  
  		 alert("please enter the only 10 digits and numerics only");
		  document.editAddressForm.phone.value="";
  			document.editAddressForm.phone.focus(); 
 		 }
}
 else{
 getIt(obj);
  
}
}
function faxvalid(obj)
{ 
if(document.editAddressForm.country.value=="" && document.editAddressForm.country.value!="UNITED STATES")
{
  if((document.editAddressForm.fax.value.length>10) || IsNumeric(document.editAddressForm.fax.value.replace(/ /g,''))==false)
 {  
   alert("please enter the only 10 digits and numerics only");
  document.editAddressForm.fax.value="";
  document.editAddressForm.fax.focus(); 
  }
	}
 
 else{
 getIt(obj);
  
}
}	
function zipcode(obj)
{
if(document.editAddressForm.country.value=="" && document.editAddressForm.country.value!="UNITED STATES")
{
  
  		if((document.editAddressForm.zip.value.length>5) || IsNumeric(document.editAddressForm.zip.value.replace(/ /g,''))==false)
 		{  
   alert("please enter the only 5 digits and numerics only");
  document.editAddressForm.zip.value="";
  document.editAddressForm.zip.focus(); 
  			}
			}
 else{
 getzip(obj);
  
}
}
 function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
	function limitText(limitField, limitCount, limitNum) {
	limitField.value = limitField.value.toUpperCase();
if (limitField.value.length > limitNum) {
limitField.value = limitField.value.substring(0, limitNum);
} else {
limitCount.value = limitNum - limitField.value.length;
 }
}	
function disabled(val1)
   {
	if(val1== 3)
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
  	
    }
     function titleLetter()
		   {
		       if(event.keyCode==9)
			    {
			      //window.open("<%=path%>/jsps/datareference/searchpopup.jsp?button=searchcustomercity");
			      document.editAddressForm.buttonValue.value ="editcitypopup" ;
		          document.editAddressForm.submit();
			    }
		   }
		  

			function titleLetterPress()
			{
			   if(event.keyCode==13)
				  {
					
					  document.editAddressForm.buttonValue.value ="editcitypopup" ;
					  document.editAddressForm.submit();
				  }
			  
			} 
    </script>
	</head>
	<%
	if(modify!=null && modify.equals("3"))
	{
	
	 %>
	
		<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
	<%}
	
	else{ %>
		<body class="whitebackgrnd">
	<%
	} 
	%>
	
	
<%--	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">--%>
<html:form action="/editAddress" scope="request">
<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew" height="90%">Edit Customer Address</tr>

 <tr class="style2">
   <td><table cellpadding="3">
        	<tr>
            	<td class="style2" align="right">Primary</td>
            	<td><html:checkbox property="primary" name="customerbean"></html:checkbox></td>
        	</tr>
        	<tr>
    			<td class="style2" align="right"><bean:message key="form.customerForm.coname" /></td>
    			<td ><html:text property="coName" value="<%=customer.getCoName()%>" styleClass="areahighlightyellow" size="25" maxlength="50" onkeyup="toUppercase(this)"/></td>
	    	<tr>
	    	<tr>
	    		<td valign="top" class="style2" align="right">Address</td>
    			<td ><html:textarea property="address1" value="<%=customer.getAddress1()%>" styleClass="textareastyle" cols="31" rows="4"  onkeyup="limitText(this.form.address1,this.form.countdown,100)"/></td>
	    	</tr>
	    	<tr>
	    		<td align="right" class="style2">City &nbsp;  <%--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button='+'editaddress','windows')">--%> </td>
    			<td>
					<%--    <html:text property="city" value="<%=cityId%>"  onkeyup="toUppercase(this)" maxlength="30"></html:text>--%>
    				<input name="city" id="city1" value="<%=cityId%>" onkeydown="titleLetter()" onkeypress="titleLetterPress()" size="25"/>
    				<dojo:autoComplete formId="editAddressForm" textboxId="city1" action="<%=path%>/actions/getCity.jsp"/> 
    			</td>
	    	</tr>
	        <tr>
	        	<td class="style2" align="right"><bean:message key="form.customerForm.state" /></td>
    			<td ><html:text property="state" size="25" value="<%=state%>" readonly="true" styleClass="areahighlightgrey"/></td>
    		</tr>
    		<tr>
    		    <td class="style2" align="right"><bean:message key="form.customerForm.zip" /></td>
    			<td><html:text property="zip" size="25" value="<%=customer.getZip()%>" onkeyup="zipcode(this)" maxlength="10"/></td>
    		</tr>
    		<tr>
    		     <td class="style2" align="right"><bean:message key="form.customerForm.country" /></td>
    			 <td><html:text property="country" size="25" value="<%=countryId%>" readonly="true" styleClass="areahighlightgrey"/>
	        </tr>
	        
 </table></td>      
 <td></td>
 <td valign="bottom"><table cellpadding="3">   
           <tr>
               	<td class="style2" align="right"><bean:message key="form.customerForm.contactname" /></td>
    			<td><html:text property="contactName" value="<%=customer.getContactName()%>" size="25" maxlength="18" onkeyup="toUppercase(this)"/></td>
    	   </tr>
    	   <tr>
    	   		 <td class="style2" align="right" ><bean:message key="form.customerForm.phone" /> </td>
    			 <td><table><tr>
    			 <td class="style2"><html:text property="phone" size="15" value="<%=phone%>" onkeyup="phonevalid(this)" maxlength="13" /></td> 
    	         <td class="style2" align="left">Ext</td>
    		   	 <td class="style2"><html:text property="extension" size="1"value="<%=customer.getExtension()%>"  maxlength="4"/></td>
    		   	 </tr></table></td>
    	   </tr>
    	   <tr>
    	         <td class="style2" align="right"><bean:message key="form.customerForm.fax" /></td>
    			 <td ><html:text property="fax"  value="<%=customer.getFax()%>" size="25" maxlength="13" onkeyup="faxvalid(this)" /></td>
    	   </tr>   
           <tr>
            	 <td class="style2" align="right"><bean:message key="form.customerForm.email1" /></td>
    			 <td><html:text property="email1" value="<%=customer.getEmail1()%>" size="25" maxlength="40"/></td>
          </tr>
          <tr>
          		<td class="style2" align="right"><bean:message key="form.customerForm.email2" /></td>
    			<td><html:text property="email2" value="<%=customer.getEmail2()%>" size="25" maxlength="40" /></td>	
          </tr>
           <tr>
            	<td colspan="5">&nbsp;</td>
        	</tr>
        	<tr>
            	<td colspan="5">&nbsp;</td>
        	</tr>
        	<tr>
            	<td colspan="5">&nbsp;</td>
        	</tr>
        	<tr>
            	<td colspan="5">&nbsp;</td>
        	</tr>
            <tr>
            	<td colspan="5">&nbsp;</td>
        	</tr>
	      <tr valign="bottom">&nbsp;&nbsp;</tr>
  </table></td>
 </tr>
 <tr>
 		<td align="right">
 		  <input type="button" class="buttonStyleNew" value="Update" id="add" onclick="add1()"/>
 		</td>
 		<td width="100" align="center">
 		  <input type="button" class="buttonStyleNew" value="Previous" id="cancel" onclick="cancel1('<%=modify%>')" /></td>
<%-- 		<img src="<%=path%>/img/cancel.gif" border="0" id="cancel" onclick="cancel1('<%=modify%>')"/>--%>
 		<td align="left">
 		  <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="confirmdelete()"/></td>
<%-- 		<img src="<%=path%>/img/delete.gif" onclick="confirmdelete()" id="delete"/>--%>
</tr>
  </table>
  <html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



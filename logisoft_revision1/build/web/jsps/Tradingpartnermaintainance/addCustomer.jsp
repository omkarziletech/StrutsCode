<%@ page language="java" pageEncoding="ISO-8859-1"
	import="com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.beans.customerBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
	String path = request.getContextPath();
	String countryId = "";
	String cityId = "";
	String state = "";
	String phone = "";
	String fax = "";
	String ScheduleNumber = "";
	String extension = "";
	Customer customer = new Customer();
	DBUtil dbUtil = new DBUtil();
	customer.setPrimary("off");
	customerBean customerbean = new customerBean();
	if (request.getParameter("addCustomer") != null) {
		session.setAttribute("addCustomer", request
		.getParameter("addCustomer"));
	}
	if (session.getAttribute("customerbean") != null) {
		customerbean = (customerBean) session
		.getAttribute("customerbean");
		customer.setPrimary(customerbean.getPrimary());
	}
	request.setAttribute("customerbean", customer);
%>
<c:if test="${add!=null}">
	<script type="text/javascript">
        parent.parent.GB_hide();
 </script>
</c:if>

<c:if test="${addMaster!=null}">
	<script type="text/javascript">
        parent.parent.GB_hide();
 </script>
</c:if>
<html>
<head>
	<title>JSP for AddCustomerForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js"></script>
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript">
	    dojo.hostenv.setModulePrefix('utils', 'utils');
	    dojo.widget.manager.registerWidgetPackage('utils');
	    dojo.require("utils.AutoComplete");
	    dojo.require("dojo.io.*");
		dojo.require("dojo.event.*");
		dojo.require("dojo.html.*");
	</script>
	<script type="text/javascript">

function add1(){  
	    if(document.tradingPartnerForm.coName.value== "" ){
			alert("please enter the Coname");
			document.tradingPartnerForm.coName.value="";
			document.tradingPartnerForm.coName.focus();
			return;
         }
       if(document.tradingPartnerForm.address1.value==""){
            alert("please enter the address");
            document.tradingPartnerForm.address1.value="";
			document.tradingPartnerForm.address1.focus();
			return;
         }
        if(document.tradingPartnerForm.city.value==""){
             alert("please enter the city");
            document.tradingPartnerForm.city.value="";
			document.tradingPartnerForm.city.focus(); 
			return;
         }
         var value=document.tradingPartnerForm.zip.value;
		for(var i=0;i< value.length;i++){
			if(value.indexOf(" ") == 0){
				alert("Please dont start with white space");
				return;
			}
		}

    if(document.tradingPartnerForm.zip.value!="" && document.tradingPartnerForm.zip.value.length<5){
   		alert("Zipcode should be 5 digits.");
  		document.tradingPartnerForm.zip.value="";
   	 	document.tradingPartnerForm.zip.focus();
   	 	return;
   }
   var value=document.tradingPartnerForm.phone.value;
   for(var i=0;i< value.length;i++){
	if(value.indexOf(" ") == 0){
		alert("Please dont start with white space");
		return;
	}
}
    var value=document.tradingPartnerForm.fax.value;
	for(var i=0;i< value.length;i++){
		if(value.indexOf(" ") == 0){
			alert("Please dont start with white space");
			return;
		}
    }
		  document.tradingPartnerForm.buttonValue.value="addCustAdrress";
	      document.tradingPartnerForm.submit();
	}
	     function phonevalid(obj) { 
			if(document.tradingPartnerForm.country.value=="" && document.tradingPartnerForm.country.value!="UNITED STATES"){
			  		if((document.tradingPartnerForm.phone.value.length>10) || IsNumeric(document.tradingPartnerForm.phone.value.replace(/ /g,''))==false){  
			  		 alert("please enter the only 10 digits and numerics only");
					  document.tradingPartnerForm.phone.value="";
			  			document.tradingPartnerForm.phone.focus(); 
			 		 }
			}else{alert(obj);
			 getIt(obj);
			}
			}
			function faxvalid(obj) { 
				if(document.tradingPartnerForm.country.value=="" && document.tradingPartnerForm.country.value!="UNITED STATES"){
				  if((document.tradingPartnerForm.fax.value.length>10) || IsNumeric(document.tradingPartnerForm.fax.value.replace(/ /g,''))==false){  
				    alert("please enter the only 10 digits and numerics only");
				    document.tradingPartnerForm.fax.value="";
				    document.tradingPartnerForm.fax.focus(); 
				}
			}else{alert(obj);
				 getIt(obj);
			}
		   }
		  function zipcode(obj){
				if(document.tradingPartnerForm.country.value=="" && document.tradingPartnerForm.country.value!="UNITED STATES"){
			  		if((document.tradingPartnerForm.zip.value.length>5) || IsNumeric(document.tradingPartnerForm.zip.value.replace(/ /g,''))==false){  
						  alert("please enter the only 5 digits and numerics only");
						  document.tradingPartnerForm.zip.value="";
						  document.tradingPartnerForm.zip.focus(); 
			  		}
				}else{alert(obj);
 					getzip(obj);
					}					
			}
  	     function getCountry(ev){ 
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "country";
				 params['city'] = document.tradingPartnerForm.city.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateCountryAndState");
			    }
			 }
		   function populateCountryAndState(type, data, evt) {
		   	if(data){
		   		document.getElementById("country").value=data.country;
		   		if(data.state){
		   		document.getElementById("state").value=data.state;
		   		}else{
		   		document.getElementById("state").value="";
		   		}
	   	}
}
function getUppercase(){
alert("ssss");
}
</script>
	<%@include file="../includes/resources.jsp"%>
</head>
<body class="whitebackgrnd" onunload="opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/MasterCustomer.jsp";">
<html:form action="/tradingPartner" scope="request">
<table width="100%" border="0" class="tableBorderNew">
  <tr class="tableHeadingNew" height="90%">Add Customer Address</tr>
	<tr class="textlabelsBold">
	<td>
	<table border="0" width="50%" cellpadding="3">
	 <tr class="textlabelsBold">
	 <td  align="right" valign="bottom">Primary</td>
	 <td>
	 	<html:checkbox property="primary" name="customerbean" />
	    Notify<html:checkbox property="notifyParty"/>
	 </td>
     </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"><bean:message key="form.customerForm.coname" /></td>
	 <td><html:text property="coName" value="<%=customer.getCoName()%>" styleClass="areahighlightyellow" 
	        size="25" maxlength="50" /></td>
	 </tr>
	 <tr>
	 <td valign="top" class="textlabelsBold" align="right">Address </td>
	 <td><html:textarea property="address1"	value="<%=customer.getAddress1()%>" styleClass="textareastyle"
		cols="31" rows="4" onkeyup="limitText(this.form.address1,this.form.countdown,100)" onkeydown="getUppercase()"/></td>
	 </tr>
	 <tr><td class="textlabelsBold" align="right">City &nbsp;</td>
	 <td><input name="city" id="city" value="<%=cityId%>" onkeydown="getCountry(this.value)" size="25" />
		<dojo:autoComplete formId="tradingPartnerForm" textboxId="city" action="<%=path%>/actions/getCity.jsp /></td>
	 </tr>
	 <tr><td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.state" /> </td>
	 <td><html:text size="25" property="state" styleId="state" value="<%=state%>" readonly="true" styleClass="areahighlightgrey" /></td>
	 </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.zip" /> </td>
	 <td> <html:text size="25" property="zip" value="<%=customer.getZip()%>" onkeyup="zipcode(this)" maxlength="10" /></td>
	 </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.country" /> </td>
	 <td> <html:text size="25" property="country" style="country" value="<%=countryId%>" readonly="true" styleClass="areahighlightgrey" /></td>
	 </tr>
     </table>
	 </td>
   	 <td valign="top">
	<table width="100%" border="0" cellpadding="3">
	 <tr>
	 <td class="textlabelsBold"> <bean:message key="form.customerForm.contactname" /> </td>
	 <td> <html:text property="contactName" value="<%=customer.getContactName()%>" size="25" maxlength="18" /></td>
	 <tr>
	 <tr>
	 <td align="right" class="textlabelsBold"> <bean:message key="form.customerForm.phone" /> </td>
	 <td>
	  <table>
		 <tr>
		 <td align="left"> <html:text property="phone" value="<%=customer.getPhone()%>" onkeyup="phonevalid(this)" size="15" maxlength="13" /></td>
		 <td align="left" class="textlabelsBold"> Ext </td>
		 <td align="left"> <html:text property="extension" value="<%=customer.getExtension()%>" size="1" maxlength="4" />
		 </td>
		 </tr>
	  </table>
	 </td>
	 </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.fax" /> </td>
	 <td align="left"> <html:text property="fax" value="<%=customer.getFax()%>" size="25" maxlength="13" onkeyup="faxvalid(this)" /></td>
	 </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.email1" /> </td>
	 <td> <html:text property="email1" value="<%=customer.getEmail1()%>" size="25" maxlength="40" /> </td>
	 </tr>
	 <tr>
	 <td class="textlabelsBold" align="right"> <bean:message key="form.customerForm.email2" /> </td>
	 <td> <html:text property="email2" value="<%=customer.getEmail2()%>"  size="25" maxlength="40" /></td>
	 </tr>
	 <tr>
	 <td colspan="5"> &nbsp; </td>
	 </tr>
	 <tr>
	 <td colspan="5"> &nbsp; </td>
	 </tr>
	 <tr>
	 <td colspan="5"> &nbsp; </td>
	 </tr>
	 <tr>
	 <td colspan="5"> &nbsp; </td>
	 </tr>
	 <tr valign="bottom"> &nbsp;&nbsp; </tr>
	 </table>
	 </td>
	 </tr>
     <tr>
	 <td align="center" colspan="2"  width="90%"> <input type="button" class="buttonStyleNew" value="Add Customer Address" id="add" style="width:150px"  onclick="add1()" /></td>
 </tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



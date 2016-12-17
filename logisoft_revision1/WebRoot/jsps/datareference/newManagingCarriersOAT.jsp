<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.CarrierAirline,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.CarriersOrLine"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="carair" class="com.gp.cong.logisoft.struts.form.NewManagingCarriersOATForm"></jsp:useBean>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List carriertypeList=new ArrayList();
String msg="";
CarriersOrLine carriers=null;
GenericCode genericobj=null;
String carriertype="0";
CarriersOrLine carrierOrline=new CarriersOrLine();
//carrierOrline.setEdiCarrier("I");
request.setAttribute("carrierOrline",carrierOrline);
if(session.getAttribute("carriers") != null)
{
   	carriers=(CarriersOrLine)session.getAttribute("carriers");
   	//from edit source
      if(carriers.getEdiCarrier()!=null && carriers.getEdiCarrier().equals("I"))
      {
       carair.setEdiCarrier("on");
       }else
       {
         carair.setEdiCarrier("off");
       }   	
   	genericobj=carriers.getCarriertype();
   	if(genericobj!=null)
   	{
		carriertype=genericobj.getId().toString();
	}
}
if(request.getAttribute("message")!=null)
{
msg=(String)request.getAttribute("message");
}
if(carriertypeList != null)
{
carriertypeList=dbUtil.getGenericCodeList(new Integer(17),"yes","Select Carrier Type");
request.setAttribute("carriertypeList",carriertypeList);
}
//from carriers
  CarrierAirline carrier = new  CarrierAirline();
  request.setAttribute("carrier",carrier);
carair.setAcomyn("N");
   
   if(carrier.getAcomyn()!=null && carrier.getAcomyn().equals("Y"))
	{
    carair.setAcomyn("on");
	}else
	{
carair.setAcomyn("off");
	}
 


%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
		<title>New Managing Carriers OAT</title>
		<%@include file="../includes/baseResources.jsp" %>
                <%@include file="../fragment/formSerialize.jspf"  %>
                <script language="javascript">
                    start = function(){
                        serializeForm();
                      }
                    window.onload = start;
                </script>

		<script language="javascript" src="<%=path%>/js/common.js" ></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
		<script language="javascript" type="text/javascript">
		
		function JasperReport()
        {
        alert("hi12312312312312");
       //  document.newManagingCarriersOATForm.buttonValue.value="report";
   		// alert("kkk"+ document.newManagingCarriersOATForm.buttonValue.value);
   		 // document.newManagingCarriersOATForm.submit();
        alert("hai");
        
        }
		function defaultValues()
		{
			 document.getElementById("divair").style.visibility = 'hidden';
			 document.getElementById("equiprates").style.visibility = 'hidden';
			 document.getElementById("abbr").style.visibility = 'hidden';
			 document.getElementById("fclcontactlabel").style.visibility = 'hidden';
			 document.getElementById("fclcontacttext").style.visibility = 'hidden';
		}
		//functions for the check boxes
		function chkall(){
    if(document.newManagingCarriersOATForm.acomyn.checked)
    {
     
    document.newManagingCarriersOATForm.acomyn.value="Y";
     
     document.newManagingCarriersOATForm.acomyn.focus();
    
    return false;
    }
        }
		function chkall1(){
    if(document.newManagingCarriersOATForm.ediCarrier.checked)
    {
     
    document.newManagingCarriersOATForm.ediCarrier.value="I";
     
     document.newManagingCarriersOATForm.ediCarrier.focus();
    
    return false;
    }
        }
        
        
		function save()
		{
		  
		  if(document.newManagingCarriersOATForm.carrierType.value=="0")
		  {
		  alert("Please select the carrier type");
		  return;
		  }
			var carriercode=document.newManagingCarriersOATForm.carriercode;
			if(carriercode.value==""&& !isNAN(carriercode.value))
 		 	{  
 		  	  	 alert("Please enter the Carrier Code ");
  			 	 carriercode.focus();
  				 return;
  		 	}  
  		 	if(carriercode.value.match(" "))
            {
            	alert("Space is not allowed for Carrier Code");
            	carriercode.focus();
            	return;
            }
            if(isSpecial(carriercode.value)==false)
  			{
    			alert("Special Characters not allowed for Carrier Code.");
  				carriercode.value="";
    			carriercode.focus();
    			return;
  			} 
  			var carriername=document.newManagingCarriersOATForm.carriername;
			if(carriername.value=="")
 		 	{  
 		  	  	 alert("Please enter the Carrier Name ");
  			 	 carriername.focus();
  				 return;
  		 	}  
  		 	
			
         var SCAC=document.newManagingCarriersOATForm.SCAC;
  		 	if(SCAC.value.match(" "))
            {
            	alert("Space is not allowed for SCAC");
            	SCAC.focus();
            	return;
            }
            if(isSpecial(SCAC.value)==false)
  			{
    			alert("Special Characters not allowed for SCAC");
  				SCAC.value="";
    			SCAC.focus();
    			return;
  			}
  			if(document.newManagingCarriersOATForm.carrierType.value=="4")
  			{
  				if(document.newManagingCarriersOATForm.airabbr.value=="")
  				{
  					alert("Please enter the AirLine ABBR");
  					document.newManagingCarriersOATForm.airabbr.focus();
  					return;
  				}
  				if(document.newManagingCarriersOATForm.aircod.value=="")
  				{
  					alert("Please enter the AirLine code");
  					document.newManagingCarriersOATForm.aircod.focus();
  					return;
  				}
  							
  				
  				if(document.newManagingCarriersOATForm.commisionpercentage.value.length>4)
				{
    				alert("Commission Percentage must not be more than 3 characters");
    				document.newManagingCarriersOATForm.commisionpercentage.value="";
     				document.newManagingCarriersOATForm.commisionpercentage.focus();
     				return;
    			}
    			

  			}
  			document.newManagingCarriersOATForm.buttonValue.value="save";
   			document.newManagingCarriersOATForm.submit();
			
		}
		function cancel()
		{
			document.newManagingCarriersOATForm.buttonValue.value="cancel";
			var result = confirm("Do you want to save the changes?");
	   			if(result)
	   			{
	   			   save();
	   			  document.newManagingCarriersOATForm.submit();
	   			}
   			document.newManagingCarriersOATForm.submit();
 		}		
		function displayHidden(obj)
		{
		if(document.newManagingCarriersOATForm.carrierType.value=="0")
		{
		alert("please select the carrier type");
		return;
		}
		  document.newManagingCarriersOATForm.buttonValue.value="onchange";
		document.newManagingCarriersOATForm.submit();
		  
		   if((obj.value=="4" || obj.value=="13") && obj.value!="")
		   {
		   document.getElementById("abbr").style.visibility = 'visible';
		   }
		   else
		   {
		   document.getElementById("abbr").style.visibility = 'hidden';
		   }
			if(obj.value=="4")
			{
				document.getElementById("divair").style.visibility = 'visible';
				
			}
			else
			{
				document.getElementById("divair").style.visibility = 'hidden';
				
			}
			if(obj.value=="13")
			{
				document.getElementById("equiprates").style.visibility = 'visible';
				
				document.getElementById("fclcontactlabel").style.visibility = 'visible';
				document.getElementById("fclcontacttext").style.visibility = 'visible';
			}
			else
			{
				document.getElementById("equiprates").style.visibility = 'hidden';
				document.getElementById("fclcontactlabel").style.visibility = 'hidden';
				document.getElementById("fclcontacttext").style.visibility = 'hidden';
			}
		}
	  function openPortExceptionList() 
      {
        	mywindow=window.open("<%=path%>/jsps/datareference/airPortException.jsp","","width=650,height=450");
           	mywindow.moveTo(200,180);
      }    
	function toUppercase(obj) 
	    {
			obj.value = obj.value.toUpperCase();
		}
		  var newwindow = '';
           function addform() {
           document.newManagingCarriersOATForm.submit();
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/Claim.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/Claim.jsp","","width=600,height=300");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
		</script>
		<%@include file="../includes/resources.jsp" %>
	</head>
  
  <body class="whitebackgrnd" >
  <html:form action="/newManagingCarriersOAT" scope="request">

  <table width="100%" border="0" cellspacing="0" cellpadding="0"  class="tableBorderNew">
  <tr class="tableHeadingNew"><td><bean:message key="form.managingCarriersOATForm.newcarrier"/></td>
   <td align="right"><input type="button" class="buttonStyleNew" value="Save"  onClick="save()" /> 
	 <input type="button" class="buttonStyleNew" value="Go Back"   onclick="cancel()"  /> </td>
   </tr>
   <tr>
   <td colspan="2">
   <table width="100%" border="0" cellspacing="3" cellpadding="0">
  <tr>
    <td width="30%" class="style2"><bean:message key="form.managingCarriersOATForm.carriercode"/>&nbsp;</td>
    <td ><html:text property="carriercode" value="<%=carriers.getCarriercode()%>"  maxlength="5" styleClass="varysizeareahighlightgrey" onkeypress="return checkIts(event)" style="width:215px" /></td>
  </tr>
  <tr>
    <td class="style2"><bean:message key="form.managingCarriersOATForm.carriertype"/>*</td>
    <td><html:select property="carrierType" styleClass="selectboxstyle" onchange="displayHidden(this)" style="width:215px">
      <html:optionsCollection name="carriertypeList"/> </html:select>
	 </td>
  </tr>
  <tr>
    <td  class="style2"><bean:message key="form.managingCarriersOATForm.carriername"/>* </td>
    <td ><html:text property="carriername" maxlength="25" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)" style="width:215px"/></td>
  </tr>
  <tr>
    <td class="style2"><bean:message key="form.managingCarriersOATForm.scac"/></td>
    <td ><html:text property="SCAC" maxlength="4"  onkeyup="toUppercase(this)" style="width:215px"/></td>
  </tr>
<%
 if(carriertype.equals("13")||carriertype.equals("4")){ 
%>
  <tr id="abbr">
    <td  class="style2"><bean:message key="form.managingCarriersOATForm.abbr"/></td>
    <td ><html:text property="abbreviation" maxlength="10"  onkeyup="toUppercase(this)" style="width:215px"/></td>
  </tr>
<%
}
	if(carriertype.equals("13")){
%>
  <tr>
    <td  class="style2" id="fclcontactlabel"><bean:message key="form.managingCarriersOATForm.fclContact"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td ><html:text property="fclContactNumber" maxlength="30" style="width:215px"/></td>
   </tr>
  <tr class="style2">
	<td><bean:message key="form.managingCarriersOATForm.edicarrier"/></td>
	<td>
		<table><tr class="style2">
		<td><html:checkbox property="ediCarrier"  name="carair" ></html:checkbox></td>
	    </tr>
	    </table>
  	</td>
  </tr>
  <tr>
   <td  colspan="4" align="center"><span class="style2">
   <input type="button" class="buttonStyleNew" value="Equip Rates"  name="equiprates" onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/oceanException.jsp?button='+'code',300,600)"/> 

  </tr>
  <% } %>
</table>
<%if(carriertype.equals("4")){
 %>
 <table width="100%" border="0" cellspacing="3" cellpadding="0">
  <tr class="tableHeadingNew">
  
  </tr>
    <tr class="textfieldstyle">
       <td class="style2" width="30%" ><bean:message key="form.managingCarriersOATForm.airlinecode"/></td>
      <td width="100"><html:text property="aircod" maxlength="3" onkeypress="return checkIts(event)" style="width:215px" /></td>
     
      <td width="100">&nbsp;</td>
      </tr>
    <tr class="textfieldstyle">
    <td class="style2" width="100"><bean:message key="form.managingCarriersOATForm.airlineabbr"/></td>
      <td width="100"><html:text property="airabbr" maxlength="2"  onkeyup="toUppercase(this)" style="width:215px" /></td>
    </tr>  
    <tr class="textfieldstyle">
      <td class="style2" width="100"><bean:message key="form.managingCarriersOATForm.airlineacct"/></td>
      <td width="100"><html:text property="alnact" maxlength="20"  onkeyup="toUppercase(this)" style="width:215px"/></td>
    </tr>
  <tr>
  <td>
	<tr class="textfieldstyle">
	 	<td width="15"  class="style2"><bean:message key="form.managingCarriersOATForm.commissiontype" /> </td>
      	<td width="75">
      				<table><tr>
      				<td>
					  <html:checkbox property="acomyn"   name="carair" onclick="chkall()">
      				</html:checkbox>
      				</td>
      				<td>
      				<html:select   property="commissiontype" styleClass="verysmalldropdownStyle" >
					  <html:option value="0">0</html:option>
					  <html:option value="1">1</html:option>
					  </html:select>
					  </td></tr></table>
	 	</td>
      	<td class="style2"><bean:message key="form.managingCarriersOATForm.0or1"/> </td>
	</tr>  
  	<tr class="textfieldstyle">
      <td class="style2"><bean:message key="form.managingCarriersOATForm.commission%"/></td>
      <td><html:text property="commisionpercentage" styleId="commper" maxlength="4" onkeypress="getDecimals(this,1,event)" style="width:215px"/></td>
      <td class="style2">&nbsp;</td>
      <td>&nbsp;</td>
      <td class="style2">&nbsp;</td>
    </tr>
  	
  <tr>
   		<td colspan="4" class="style2" align="center">
   		 <input type="button" class="buttonStyleNew" value="Port Exception" style="width:90px"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/airPortException.jsp?button='+'code',300,600)" /> 
   		  	 <input type="button" class="buttonStyleNew" value="AWB"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/Claim.jsp?button='+'code',300,600)" /> 
  </tr>
   </table>
   </td>
</tr>
</table>
  <% } %>
  <html:hidden property="buttonValue"/>
  </html:form> 
  </body>
  
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

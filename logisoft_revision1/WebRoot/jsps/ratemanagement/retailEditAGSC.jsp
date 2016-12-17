<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RetailStandardCharges1,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
DecimalFormat per = new DecimalFormat("0.000");

String chargecode="";
String codeDesc="";
List typeList=new ArrayList();
List agssList=null;
String chargeType="0";
RetailStandardCharges1 retailStandardCharges=new RetailStandardCharges1();

AirRatesBean airRatesBean=new AirRatesBean();
String effectiveDate="";
String minAmt="";
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="",result ="";
String chartypedesc="";
String std="";
String defaultRate="";
if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
RetailStandardCharges1 retailStd=new RetailStandardCharges1();
retailStd.setStandard("off");
retailStd.setExclude("off");
retailStd.setAsFrfgted("off");

if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
retailStd.setStandard(airRatesBean.getStandard());
retailStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airStd",retailStd);
if(session.getAttribute("retailStandardCharges")!=null )
{
	
    retailStandardCharges=(RetailStandardCharges1)session.getAttribute("retailStandardCharges");
    
	if(retailStandardCharges.getChargeCode()!=null)
	{
			chargecode=retailStandardCharges.getChargeCode().getCode();

			codeDesc=retailStandardCharges.getChargeCode().getCodedesc();
	}
	if(retailStandardCharges.getChargeType()!=null)
	{
		chartypedesc=retailStandardCharges.getChargeType().getCodedesc();
	}
	if(retailStandardCharges.getChargeType()!=null)
	{
	chargeType=retailStandardCharges.getChargeType().getId().toString();

	}
	if(retailStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(retailStandardCharges.getMinAmt());
	}
	if(retailStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=retailStandardCharges.getEffectiveDate().toString();
	}
	if(retailStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(retailStandardCharges.getInsuranceRate());
	}
	if(retailStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(retailStandardCharges.getInsuranceAmt());
	}
	if(retailStandardCharges.getPercentage()!=null)
	{
	
	double d=retailStandardCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	}
	if(retailStandardCharges.getAmount()!=null)
	{
	amount=df.format(retailStandardCharges.getAmount());
	}
	if(retailStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(retailStandardCharges.getAmtPerCft());
	}
	if(retailStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(retailStandardCharges.getAmtPer100lbs());
	}
	if(retailStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(retailStandardCharges.getAmtPerCbm());
	}
	if(retailStandardCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(retailStandardCharges.getAmtPer1000kg());
	}
	if(retailStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(retailStandardCharges.getEffectiveDate());
	}
	if(retailStandardCharges.getStandard()!=null && retailStandardCharges.getStandard().equals("Y"))
	{
	retailStd.setStandard("on");
	}
	else
	{
	retailStd.setStandard("off");
	}
	if(retailStandardCharges.getAsFrfgted()!=null && retailStandardCharges.getAsFrfgted().equals("X"))
	{
	retailStd.setAsFrfgted("on");
	}
	else
	{
	retailStd.setAsFrfgted("off");
	}
}

if(session.getAttribute("retailagssAdd") != null)
{
	agssList = (List) session.getAttribute("retailagssAdd");
	
	
}

String msg="";
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
}
String modify="";
modify = (String) session.getAttribute("modifyforretailRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}



%>

 
<html> 
	<head>
		<title>JSP for RetailEditAGSCForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addAGSS()
		{
		if(document.retailEditAGSCForm.chargeType.value=="0")
		{
		alert("Please select Charge Type");
		return;
		}
			document.retailEditAGSCForm.buttonValue.value="edit";
  			document.retailEditAGSCForm.submit();
		}
		
   		function submit()
		{
			document.retailEditAGSCForm.buttonValue.value="";
			document.retailEditAGSCForm.submit();
		}
   		
		function cancelbtn()
   		{
   		
   		document.retailEditAGSCForm.buttonValue.value="cancel";
		document.retailEditAGSCForm.submit();
   		}
   		
   		
   		function delete1(){
			
		 	document.retailEditAGSCForm.buttonValue.value="delete";
			document.retailEditAGSCForm.submit();
   		}
		function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
       
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   			if(imgs[k].id != "cancel")
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{	
	 		
	 			
	 			if(input[i].name!="delete")
	 			   {
	 			   alert("==="+input[i].name);
	 				if(input[i].name=="exclude" || input[i].name=="asFrfgted" || input[i].name=="standard"){
	 					input[i].disabled=true;
	 				}
	 				else{
	 				input[i].className="areahighlightgreysmall";
	  				input[i].readOnly=true;
	  				}
	  			}	
  	 	
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++)	{
	 			textarea[i].readOnly=true;
	 					
	  	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 }
  	 document.getElementById("delete").style.visibility = 'hidden';
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
	function disable(val)
   		{
   		if(val.checked == true){
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="exclude"&& input[i].name!="charge"&& input[i].name!="desc")
   			{
   		    	input[i].value="";
   		    	input[i].readOnly=true;
   		    	input[i].style.backgroundColor="blue";
   		    	
   		    }
	  	}
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].value="";
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
   		}else {
   		
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="exclude"&& input[i].name!="charge"&& input[i].name!="desc")
   			{	
   		    	input[i].readOnly=false;
   		    	input[i].style.backgroundColor="white";
   		    }
	  	}
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=false;
			select[i].style.backgroundColor="white";
	  		
  	 	}
   		}
   		}
			
	</script>
	
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		
		<html:form action="/retailEditAGSC" name="retailEditAGSCForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailEditAGSCForm" scope="request">
			
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
		<tr><td>
		<%if(session.getAttribute("removebutton")==null){ %>
			<table width=100% border="0" cellpadding="0" cellspacing="0" >
			  <tr class="tableHeadingNew"><td>Accesorial & Gen Std Chrgs</td>
			   <td colspan="2">
			        <input type="button" value="Go Back" id="cancel" onclick="addAGSS()" class="buttonStyleNew" >		  
			  			  
			   </td>
			</tr>
			<%}else{ %>
			
			<%} %>
			<tr>
			<td>
			<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
		   <tr class="textlabels">
				<td>Chrg Code</td>
				<td>Chrg Desc </td>
				<td>Exclude </td>
				<td>Type </td>
				<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        	<td align="center">Amt</td>
        	
            <%
         	}
         	if(chargeType.equals("11289"))
         	{
         	%>
         	<%if(defaultRate.equals("E"))
         	{ %>
        	<td>$/cft</td>
			<td>$/100lbs</td>
			<%}
			else if(defaultRate.equals("M"))
			{ %>
			<td>$/cbm</td>
			<td>$/1000kg</td>
			<%} %>
        	<td>MinAmt</td>
            <%
         	}
         	if(chargeType.equals("11288"))
         	{
         	%>
         	<td>Perc</td>
         	<td>MinAmt</td>
         	<%
         	}
         	if(chargeType.equals("11291"))
         	{
         	%>
         	<td align="center">MinAmt</td>
         	<%
         	}
         	if(chargeType.equals("11290"))
		 	{
         	%>
         	<td>As Freighted</td>
         	<td>Insurance Rate</td>
         	<td>Insurance Amt</td>
         	<td>MinAmt</td>
         	<%
         	}
         	%>
           
			<td>Eff.Date</td>
			
		 </tr>
		 <tr class="textlabels">
 		 <td ><html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/></td>
   		 <td ><html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/></td>
  		 <td><html:checkbox property="exclude" name="airStd" onclick="disable(this)"></html:checkbox></td>
  		 <td><html:text property="chargeType"  value="<%=chartypedesc%>" readonly="true"/> </td>
         <td><html:checkbox property="standard" name="airStd"  ></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11289"))
         {
         %>
         <%if(defaultRate.equals("E"))
         { %>
         <td><html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCft" value="<%=cft%>"/></td>
  		 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  		 <%}
  		 else if(defaultRate.equals("M"))
  		 { %>
  		 <td><html:text property="amtPerCbm" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCbm" value="<%=cbm%>"/></td>
  		 <td><html:text property="amtPer1000kg" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer1000kg" value="<%=amtkg%>"/></td>	
  		 <%} %>
  		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
         <%
         }
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage"  maxlength="3" size="3" onblur="checkdec(this)" styleId="percentage" value="<%=percentage%>"/></td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		 if(chargeType.equals("11291"))
		 {
		 %>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airStd"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" size="7" readonly="true" value="<%=effectiveDate%>"/></td>
  		<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
<td>  		<%--<input type="button" value="Delete" name="delete" id="delete" onclick="delete1()" class="buttonStyleNew" >	</td>
        --%></tr> 
     </table>
	</td>
	</tr>
  </table>
  </td></tr>
  </table>			
			
			
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


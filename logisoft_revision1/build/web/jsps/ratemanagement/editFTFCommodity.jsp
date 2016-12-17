<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.FTFCommodityCharges,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<jsp:directive.page import="java.text.DecimalFormat"/> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String chargeType="0";
String chargecode="";
String codeDesc="";
String amount="";
String cft="";
String modify="";
String message="";
String lbs="";
String cbm="";
String minAmt="";
String amtkg="";
String percentage="";
String insuranceRate="";
String insuranceAmt="";
String effectiveDate="";
String exclude="";
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setStandard("off");
airRatesBean.setAsFrfgted("off");

DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
FTFCommodityCharges ftfCommodityCharges=new FTFCommodityCharges();
if(session.getAttribute("addftfcommodity")!=null)
{
ftfCommodityCharges=(FTFCommodityCharges)session.getAttribute("addftfcommodity");
if(ftfCommodityCharges.getChargeCode()!=null)
{
chargecode=ftfCommodityCharges.getChargeCode().getCode();
codeDesc=ftfCommodityCharges.getChargeCode().getCodedesc();
}
if(ftfCommodityCharges.getChargeType()!=null)
{
chargeType=ftfCommodityCharges.getChargeType().getCodedesc();
}
if(ftfCommodityCharges.getAmount()!=null)
{
amount=df.format(ftfCommodityCharges.getAmount());
}
if(ftfCommodityCharges.getAmtPerCft()!=null)
{
cft=df.format(ftfCommodityCharges.getAmtPerCft());
}
if(ftfCommodityCharges.getAmtPer100lbs()!=null)
{
lbs=df.format(ftfCommodityCharges.getAmtPer100lbs());
}
if(ftfCommodityCharges.getAmtPerCbm()!=null)
{
cbm=df.format(ftfCommodityCharges.getAmtPerCbm());
}
if(ftfCommodityCharges.getMinAmt()!=null)
{
minAmt=df.format(ftfCommodityCharges.getMinAmt());
}
if(ftfCommodityCharges.getAmtPer1000kg()!=null)
{
amtkg=df.format(ftfCommodityCharges.getAmtPer1000kg());
}
if(ftfCommodityCharges.getPercentage()!=null)
{
 
    double d=ftfCommodityCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	
}
if(ftfCommodityCharges.getInsuranceAmt()!=null)
{
insuranceAmt=df.format(ftfCommodityCharges.getInsuranceAmt());
}
if(ftfCommodityCharges.getInsuranceRate()!=null)
{
insuranceRate=df.format(ftfCommodityCharges.getInsuranceRate());
}
if(ftfCommodityCharges.getEffectiveDate()!=null)
{
effectiveDate=dateFormat.format(ftfCommodityCharges.getEffectiveDate());
}
if(ftfCommodityCharges.getStandard()!=null && ftfCommodityCharges.getStandard().equals("Y"))
{
airRatesBean.setStandard("on");
}
if(ftfCommodityCharges.getAsFrfgted()!=null && ftfCommodityCharges.getAsFrfgted().equals("X"))
{
airRatesBean.setAsFrfgted("on");
}
if(ftfCommodityCharges.getExclude()!=null){
exclude=ftfCommodityCharges.getExclude();
}
}
request.setAttribute("airRatesBean",airRatesBean);
if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}
modify = (String) session.getAttribute("modifyforftfRates");
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}
String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}
%>
 
<html> 
	<head>
		<title>JSP for EditFTFCommodityForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function disabled(val1,val2,val3) {
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			
			  			if(input[i].name!="buttonValue")
	 			   {
	 				
	 				if(input[i].name=="exclude" || input[i].name=="asFrfgted" || input[i].name=="standard")
	 				{
	 					input[i].disabled=true;
	 				}
	 				else{
	 				//input[i].className="areahighlightgreysmall";
	  				input[i].readOnly=true;
	  				}
	  			}	
	  			
					 	
					 	}
		  	 	var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   			var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  		  	 	document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 		if(val3!=""&&val3!=null){ 
	  	 		
	  	 		var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id == "itemcreatedon" ||imgs[k].id == "save" )	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		  	 		document.editFTFCommodityForm.standard.disabled=true
	  	 		
	  	 		}
		  	 if(val1 == 1)	 {
	  	 		 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				alert(val2);
				}		
    		}
		function addAGSS()
		{
		document.editFTFCommodityForm.buttonValue.value="add";
		document.editFTFCommodityForm.submit();
		}
		function delete1()
		{
			document.editFTFCommodityForm.buttonValue.value="delete";
   			document.editFTFCommodityForm.submit();
   		}
   		function cancelbtn()
		{
			document.editFTFCommodityForm.buttonValue.value="cancel";
   			document.editFTFCommodityForm.submit();
		}
		 </script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>','<%=exclude%>')">
		<html:form action="/editFTFCommodity" scope="request">
		<div align="left">
			 <input type="button" value="Go Back" class="buttonStyleNew" onclick="addAGSS()" id="cancel"/><%--
			 <input type="button" value="Delete" class="buttonStyleNew" onclick="delete1()" id="delete"/>
		--%></div>	

<table width="100%" class="tableBorderNew" border="0">
  <tr class="tableHeadingNew"> Commodity Specfic Std Charges  </tr>
  <tr>
    <td>
   
   <table width="100%"  border="0"  cellpadding="0" cellspacing="0">
		
		<tr class="textlabels">
			<td>Chrg Code</td>
			<td>Charge Desc </td>
			<td>Type </td>
			<td>Std</td>
			<% 
            if(chargeType!=null&&chargeType.equals("Flat rate"))
        	{
        	%>
        	<td>Amt</td>
        	
            <%
         	}
         	if(chargeType!=null&&chargeType.equals("Weight or measure"))
         	{
         	        
         	if(defaultRate.equals("E"))
         	{ %>
        	<td>$/cft</td>
			<td>$/100lbs</td>
			<%}else if(defaultRate.equals("M"))
			{ %>
			<td>$/cbm</td>
			<td>$/1000kg</td>
			<%} %>
        	<td>MinAmt</td>
            
         	<%}
         	if(chargeType!=null&&chargeType.equals("Charge percent"))
         	{
         	%>
         	<td>Percent</td>
         	<td>MinAmt</td>
         	<%
         	}
         	if(chargeType!=null&&chargeType.equals("Air rates in Dollars per pound"))
         	{
         	%>
         	<td>MinAmt</td>
         	<%
         	}
         	if(chargeType!=null&&chargeType.equals("As Freighted"))
		 	{
         	%>
         	<td>As Freighted</td>
         	<td>Insurance Rate</td>
         	<td>Insurance Amount</td>
         	<td>MinAmt</td>
         	<%
         	}
         	%>
           
			<td>Eff.Date</td>
			
		 </tr>
		 <tr class="textlabels">
 		 <td ><html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/></td>
   		 <td ><html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/></td>
   	
  		 <td ><html:text property="chargeType" value="<%=chargeType%>" readonly="true" styleClass="varysizeareahighlightgrey">
      								    
                </html:text></td>
         <td><html:checkbox property="standard" name="airRatesBean"></html:checkbox></td>
         <% 
         if(chargeType!=null&&chargeType.equals("Flat rate"))
         {
         %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType!=null&&chargeType.equals("Weight or measure"))
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
         if(chargeType!=null&&chargeType.equals("Charge percent"))
         {
         %>
         <td><html:text property="percentage"  maxlength="3" size="3"  onblur="checkdec(this)" value="<%=percentage%>"/></td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		 if(chargeType!=null&&chargeType.equals("Air rates in Dollars per pound"))
		 {
		 %>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 if(chargeType!=null&&chargeType.equals("As Freighted"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airRatesBean"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true"/></td>
  				
  		<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  		
        </tr> 
        </table>
        
      </td>
  </tr>
</table>   
        <html:hidden property="buttonValue" styleId="buttonValue"/>

		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


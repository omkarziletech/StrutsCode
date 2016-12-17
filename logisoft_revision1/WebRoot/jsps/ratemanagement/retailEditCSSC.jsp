<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.RetailCommodityCharges,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String chargecode=null;
String codeDesc=null;
String chargeType="0";
String minAmt="";
String defaultRate="";
if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
String effectiveDate="";
RetailCommodityCharges retailCommodityCharges=new RetailCommodityCharges();
RetailCommodityCharges retailComm=new RetailCommodityCharges();

String chartypedesc="";
String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");

if(session.getAttribute("retailCommodityCharges")!=null )
{
    retailCommodityCharges=(RetailCommodityCharges)session.getAttribute("retailCommodityCharges");
	if(retailCommodityCharges.getChargeCode()!=null)
	{
		chargecode=retailCommodityCharges.getChargeCode().getCode();
		codeDesc=retailCommodityCharges.getChargeCode().getCodedesc();
	}
	
	if(retailCommodityCharges.getChargeType()!=null)
	{
		chargeType=retailCommodityCharges.getChargeType().getId().toString();
		chartypedesc=retailCommodityCharges.getChargeType().getCodedesc();
	}
	if(retailCommodityCharges.getMinAmt()!=null)
	{
	minAmt=df.format(retailCommodityCharges.getMinAmt());
	}
	if(retailCommodityCharges.getAmount()!=null)
	{
	amount=df.format(retailCommodityCharges.getAmount());
	}
	if(retailCommodityCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(retailCommodityCharges.getInsuranceRate());
	}
	if(retailCommodityCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(retailCommodityCharges.getInsuranceAmt());
	}
	if(retailCommodityCharges.getPercentage()!=null)
	{
	double d=retailCommodityCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	}
	if(retailCommodityCharges.getAmtPerCft()!=null)
	{
	cft=df.format(retailCommodityCharges.getAmtPerCft());
	}
	if(retailCommodityCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(retailCommodityCharges.getAmtPerCbm());
	}
	if(retailCommodityCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(retailCommodityCharges.getAmtPer100lbs());
	}
	if(retailCommodityCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(retailCommodityCharges.getAmtPer1000kg());
	}
	if(retailCommodityCharges.getStandard()!=null && retailCommodityCharges.getStandard().equals("Y"))
	{
	retailComm.setStandard("on");
	}
	else
	{
	retailComm.setStandard("off");
	}
	if(retailCommodityCharges.getAsFreightedCheckBox()!=null && retailCommodityCharges.getAsFreightedCheckBox().equals("Y"))
	{
	retailComm.setAsFreightedCheckBox("on");
	}
	else
	{
	retailComm.setAsFreightedCheckBox("off");
	}
	if(retailCommodityCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(retailCommodityCharges.getEffectiveDate());
	}
}
AirRatesBean airRatesBean=new AirRatesBean();
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
retailComm.setStandard(airRatesBean.getStandard());
retailComm.setExclude(airRatesBean.getExclude());
retailComm.setAsFreightedCheckBox(airRatesBean.getAsFrfgted());
}
request.setAttribute("airComm",retailComm);

if(session.getAttribute("retailcssList") != null)
{
	List csssList = (List) session.getAttribute("retailcssList");
}
/*
if(typeList != null)
{
	typeList=dbUtil.getTypeListForCssc1(new Integer(34),"yes","Select Rate type");
	request.setAttribute("typeList",typeList);
}
*/
String modify="";
	
modify = (String) session.getAttribute("modifyforretailRates");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
		
		if(session.getAttribute("retailCommodityCharges")!=null)
	    {
	    	retailCommodityCharges=(RetailCommodityCharges)session.getAttribute("retailCommodityCharges");
	    }
	    String exclude="";
	    exclude=retailCommodityCharges.getExclude();	   

String editPath=path+"/retailEditCSSC.do";
%>
 
<html> 
	<head>
	<base href="<%=basePath%>">
		<title>JSP for RetailEditCSSCForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script type="text/javascript">
	function searchform()
		{
			document.retailEditCSSCForm.buttonValue.value="search";
			document.retailEditCSSCForm.submit();
		}
		function addAGSS()
		{
			document.retailEditCSSCForm.buttonValue.value="add";
  			document.retailEditCSSCForm.submit();
		}
		function delete1()
		{
			document.retailEditCSSCForm.buttonValue.value="delete";
   			document.retailEditCSSCForm.submit();
   		}
   		function popup1(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.retailEditCSSCForm.submit();
			return false;
		}
		function disable(val)
   		{
   		//alert();
   		/*if(val.checked == true)
   		{
   		var input = document.getElementsByTagName("input");
   		alert();
   		for(i=0; i<input.length; i++)
	 	{
	  			
	  			alert(input[i]);
	  			if(input[i].name!="buttonValue")
	 			   {
	 				if(input[i].name=="exclude" || input[i].name=="asFrfgted" || input[i].name=="standard")
	 				{
	 					input[i].disabled=true;
	 				}
	 				else{
	 				input[i].className="areahighlightgreysmall";
	  				input[i].readOnly=true;
	  				}
	  			}	
	  			
	  	}
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].value="";
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
   		}
   		else {
   		*/
   		var input = document.getElementsByTagName("input");
   		//alert();
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="buttonValue")
	 			   {
	 				//alert();
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
  	 	var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=false;
			select[i].style.backgroundColor="white";
	  		
  	 	}
   		//}
   		}
		
		function submit()
		{
		document.retailEditCSSCForm.buttonValue.value="";
		document.retailEditCSSCForm.submit();
		}
		function cancelbtn()
		{
			document.retailEditCSSCForm.buttonValue.value="cancel";
   			document.retailEditCSSCForm.submit();
		}
		
		function disabled(val1,val)
    {
  
	if(val1!="" && (val1 == 0 || val1== 3) )
	{
        var imgs = document.getElementsByTagName('img');
        document.getElementById("delete").style.visibility = 'hidden';
   		for(var k=0; k<imgs.length; k++){
   		if(imgs[k].id != "cancel" )
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="buttonValue" && input[i].name!="cancel")
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
  	 	for(i=0; i<textarea.length; i++)
	 	{
	 			textarea[i].readOnly=true;
	  			
	 					
	  	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 }
  	  if(val!=""&&val!=null && val!='N'){ 
	  	 		
	  	 		var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id == "itemcreatedon" || imgs[k].id=="add")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
	  	 		
	  	 		document.retailEditCSSCForm.standard.disabled=true
	  	 		}
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
		
		</script>
		
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=exclude%>')">
		<html:form action="/retailEditCSSC" scope="request">
			
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
<tr class="tableHeadingNew" ><td>Fcl History records<td><td align="right"><input type="button" value="Save" class="buttonStyleNew" id="cancel" onclick="addAGSS()" /></td></tr>
<td>			
			
	    <table width="100%">
		<tr>
              <td valign="top" colspan="8" align="left" style="padding-top:10px;">
               </td>      
        </tr>
		</table>
		 <table width=100% border="0" cellpadding="0" cellspacing="0">
     <tr>
     	<td height="12" colspan="4"  >&nbsp;&nbsp; </td> 
  	 </tr>
  	 </table>
		<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
		
		<tr class="textlabels">
			<td>Chrg Code</td>
			<td>Chrg Desc </td>
		
			<td>Type </td>
			<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        	<td>Amt</td>
        	
            <%
         	}
         	if(chargeType.equals("11289"))
         	{
         	%>
         	<%if(defaultRate.equals("E"))
         	{%>
         	
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
         	<td>MinAmt</td>
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
 		 <td ><html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="areahighlightgreysmall"  size="3"/></td>
   		 <td ><html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/></td>
   	    
  		
      	 <td ><html:text property="chargeType"  value="<%=chartypedesc%>" readonly="true"/></td>						   
         <td><html:checkbox property="standard" name="airComm"></html:checkbox></td>
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
         <td><html:text property="percentage"  maxlength="3" size="3" onblur="checkdec(this)" value="<%=percentage%>"/></td>
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
		  <td><html:checkbox property="asFreightedCheckBox" name="airComm"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" readonly="true" size="7" /></td>
  		<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
  		
        </tr> 
        </table>
        </td>
        </table>
        <html:hidden property="buttonValue" styleId="buttonValue"/>
  		<html:hidden property="index" />
	
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.AirCommodityCharges,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

List csssList = new ArrayList();
List applyGeneralStandardList=new ArrayList();
String chargecode=null;
String codeDesc=null;
DBUtil dbUtil=new DBUtil();
String chargeType="";
String rateType="";
String minAmt="";
String buttonValue="";

String effectiveDate="";
List typeList=new ArrayList();
AirCommodityCharges airCommodityCharges=new AirCommodityCharges();
AirCommodityCharges airComm=new AirCommodityCharges();


String amount="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
String chartypedesc="";
String exclude="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
if(request.getParameter("buttonValue")!=null)
{
buttonValue=(String)request.getParameter("buttonValue");
}
if(session.getAttribute("airCommodityCharges")!=null )
{
    airCommodityCharges=(AirCommodityCharges)session.getAttribute("airCommodityCharges");
	if(airCommodityCharges.getChargeCode()!=null)
	{
		chargecode=airCommodityCharges.getChargeCode().getCode();
		codeDesc=airCommodityCharges.getChargeCode().getCodedesc();
	}
	
	if(airCommodityCharges.getChargeType()!=null)
	{
		chargeType=airCommodityCharges.getChargeType().getId().toString();
		chartypedesc=airCommodityCharges.getChargeType().getCodedesc();
	}
	if(airCommodityCharges.getMinAmt()!=null)
	{
	minAmt=df.format(airCommodityCharges.getMinAmt());
	}
	if(airCommodityCharges.getAmount()!=null)
	{
	amount=df.format(airCommodityCharges.getAmount());
	}
	if(airCommodityCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(airCommodityCharges.getInsuranceRate());
	}
	if(airCommodityCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(airCommodityCharges.getInsuranceAmt());
	}
	if(airCommodityCharges.getPercentage()!=null)
	{
	double d=airCommodityCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	
	}
	if(airCommodityCharges.getAmtPerCft()!=null)
	{
	cft=df.format(airCommodityCharges.getAmtPerCft());
	}
	if(airCommodityCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(airCommodityCharges.getAmtPerCbm());
	}
	if(airCommodityCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(airCommodityCharges.getAmtPer100lbs());
	}
	if(airCommodityCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(airCommodityCharges.getAmtPer1000kg());
	}
	if(airCommodityCharges.getStandard()!=null && airCommodityCharges.getStandard().equals("Y"))
	{
	airComm.setStandard("on");
	}
	else
	{
	airComm.setStandard("off");
	}
	if(airCommodityCharges.getAsFrfgted()!=null && airCommodityCharges.getAsFrfgted().equals("X"))
	{
	airComm.setAsFrfgted("on");
	}
	else
	{
	airComm.setAsFrfgted("off");
	}
	if(airCommodityCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(airCommodityCharges.getEffectiveDate());
	}
	if(airCommodityCharges.getExclude()!=null){
exclude=airCommodityCharges.getExclude();
}
}
AirRatesBean airRatesBean=new AirRatesBean();
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airComm.setStandard(airRatesBean.getStandard());
airComm.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airComm",airComm);

if(session.getAttribute("cssList") != null)
{
	csssList = (List) session.getAttribute("cssList");
}

if(typeList != null)
{
	typeList=dbUtil.getTypeListForCsssedit(new Integer(34),"yes","Select Rate type",csssList,chargeType);
	request.setAttribute("typeList",typeList);
}


if(session.getAttribute("applyGeneralStandardList")!=null)
{
	applyGeneralStandardList=(List)session.getAttribute("applyGeneralStandardList");
}	

String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
		
		
		
String editPath=path+"/cSSS.do";
%>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Commodity Specific Charges</title>
    
		<%@include file="../includes/baseResources.jsp" %>

		<script type="text/javascript">
	function searchform()
		{
			document.editCSSSForm.buttonValue.value="search";
			document.editCSSSForm.submit();
		}
		function addAGSS()
		{
			document.editCSSSForm.buttonValue.value="add";
  			document.editCSSSForm.submit();
		}
		function delete1()
		{
			document.editCSSSForm.buttonValue.value="delete";
   			document.editCSSSForm.submit();
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
			document.editCSSSForm.submit();
			return false;
		}
		
		function submit()
		{
		document.editCSSSForm.buttonValue.value="";
		document.editCSSSForm.submit();
		}
		function cancelbtn()
		{
			document.editCSSSForm.buttonValue.value="cancel";
   			document.editCSSSForm.submit();
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
	  			if(input[i].name!="buttonValue")
	 			   {
	 				if(input[i].name=="exclude" || input[i].name=="asFrfgted" || input[i].name=="standard"){
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
  	
  	 if(val2!=""&&val2!=null){ 
	  	 		
	  	 		var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id == "itemcreatedon" || imgs[k].id=="add")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
	  	 		
	  	 		document.editCSSSForm.standard.disabled=true
	  	 		}
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
		</script>
	</head>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=exclude%>')">
		<html:form action="/editCSSS" scope="request">
		
    <div align="right">
     <input type="button" class="buttonStyleNew" value="Go Back" onclick="addAGSS()" id="cancel">
              <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="delete1()">
    </div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew" height="90%">Excluded General std Charges</tr> 
 
		 <table width=100% border="0" cellpadding="0" cellspacing="0">
     <tr>
     	<td height="12" colspan="4"  >&nbsp;&nbsp;</td> 
  	 </tr>
  	 </table>
		<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
		
		<tr class="textlabels">
			<td>Chrg Code</td>
			<td>Charge Desc </td>
			<td>Type </td>
			<td>Std</td>
			<% 
            if(chargeType.equals("11287"))
        	{
        	%>
        	<td>Amt</td>
        	
            <%
         	}
         	if(chargeType.equals("11291")){
         %>
					<td>
						$/100lbs
					</td>
					
				 <td>MinAmt</td>
					<%}
         	if(chargeType.equals("11288"))
         	{
         	%>
         	<td>Percent</td>
         	<td>MinAmt</td>
         	<%
         	}
         	
         	if(chargeType.equals("11290"))
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
   	
  		 <td ><html:text property="chargeType" value="<%=chartypedesc%>" readonly="true"/>
</td>
         <td><html:checkbox property="standard" name="airComm"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
        
         if(chargeType.equals("11288"))
         {
         %>
         <td><html:text property="percentage"  onblur="checkdec(this)" maxlength="3" size="3"  value="<%=percentage%>"/></td>
		 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 
		 <%
		 }
		 if(chargeType.equals("11291"))
         {%>
        		 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  			 	<td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=minAmt%>"/></td>
        	 <%
         }
		 
		 if(chargeType.equals("11290"))
		 {
		 %>
		  <td><html:checkbox property="asFrfgted" name="airComm"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" /></td>
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


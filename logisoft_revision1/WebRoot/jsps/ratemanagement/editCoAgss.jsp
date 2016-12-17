<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.LCLColoadStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
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
String chargecode=null;
String codeDesc=null;
List typeList=new ArrayList();
List agssList=null;
LCLColoadStandardCharges lCLColoadStandardCharges=new LCLColoadStandardCharges();
lCLColoadStandardCharges.setStandard("off");
lCLColoadStandardCharges.setAsFrfgted("off");
String minAmt="";
String rateType="";
String chargeType="0";
String effectiveDate="";
String insuranceRate="";
String insuranceAmt="";
String percentage="";
String amount="";
String cft="";
String lbs="";
String cbm="";
String amtkg="";
String buttonValue="";
String chartypedesc="";
AirRatesBean airRatesBean=new AirRatesBean();
LCLColoadStandardCharges airStd=new LCLColoadStandardCharges();
if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("coStandardCharges")!=null )
{
    lCLColoadStandardCharges=(LCLColoadStandardCharges)session.getAttribute("coStandardCharges");
	if(lCLColoadStandardCharges.getChargeCode()!=null)
	{
		chargecode=lCLColoadStandardCharges.getChargeCode().getCode();
		codeDesc=lCLColoadStandardCharges.getChargeCode().getCodedesc();
		chartypedesc=lCLColoadStandardCharges.getChargeType().getCodedesc();
	}
	
	if(lCLColoadStandardCharges.getChargeType()!=null)
	{
	chargeType=lCLColoadStandardCharges.getChargeType().getId().toString();
	}
	if(lCLColoadStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(lCLColoadStandardCharges.getMinAmt());
	}
	if(lCLColoadStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=lCLColoadStandardCharges.getEffectiveDate().toString();
	}
	if(lCLColoadStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(lCLColoadStandardCharges.getInsuranceRate());
	}
	if(lCLColoadStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(lCLColoadStandardCharges.getInsuranceAmt());
	}
	if(lCLColoadStandardCharges.getPercentage()!=null)
	{
	double d=lCLColoadStandardCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	}
	if(lCLColoadStandardCharges.getAmount()!=null)
	{
	amount=df.format(lCLColoadStandardCharges.getAmount());
	}
	if(lCLColoadStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(lCLColoadStandardCharges.getAmtPerCft());
	}
	if(lCLColoadStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(lCLColoadStandardCharges.getAmtPer100lbs());
	}
	if(lCLColoadStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(lCLColoadStandardCharges.getAmtPerCbm());
	}
	if(lCLColoadStandardCharges.getAmtPer1000Kg()!=null)
	{
	amtkg=df.format(lCLColoadStandardCharges.getAmtPer1000Kg());
	}
	if(lCLColoadStandardCharges.getStandard()!=null && lCLColoadStandardCharges.getStandard().equals("Y"))
	{
	airStd.setStandard("on");
	}
	else
	{
	airStd.setStandard("off");
	}
	if(lCLColoadStandardCharges.getAsFrfgted()!=null && lCLColoadStandardCharges.getAsFrfgted().equals("X"))
	{
	airStd.setAsFrfgted("on");
	}
	else
	{
	airStd.setAsFrfgted("off");
	}
	if(lCLColoadStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(lCLColoadStandardCharges.getEffectiveDate());
	}
}
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airStd.setStandard(airRatesBean.getStandard());
airStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airStd",airStd);

if(session.getAttribute("coagssAdd") != null)
{
	agssList = (List) session.getAttribute("coagssAdd");
}

if(typeList != null)
{
	
	
	typeList=dbUtil.getTypeListforcoagss(new Integer(34),"yes","Select Rate type",agssList,chargeType);
	request.setAttribute("typeList",typeList);
	
}
else
{
 	
}
String modify="";
modify = (String) session.getAttribute("modifyforlclcoloadRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/editAddLclColoadStandard.do";
String defaultRate="";
if(session.getAttribute("lcldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("lcldefaultRate");
}
%>
 
 
<html> 
	<head>
		<title>JSP for AGSSEditForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		function cancelbtn()
   		{
   		document.aGSSEditForm.buttonValue.value="cancel";
		document.aGSSEditForm.submit();
   		}
		function addAGSS()
		{
		
			document.aGSSEditForm.buttonValue.value="edit";
  			document.aGSSEditForm.submit();
		}
		
		function delete1()
		{
			
		 	document.aGSSEditForm.buttonValue.value="delete";
			document.aGSSEditForm.submit();
   		}
   		function submit()
   		{
   		document.aGSSEditForm.buttonValue.value="";
		document.aGSSEditForm.submit();
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
			document.aGSSEditForm.submit();
			return false;
		}
   		function disabled(val1)
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
  	 	document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
	</script>
	</head>
	<body   class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editAddLclColoadStandard" name="aGSSEditForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditAddLclColoadStandardForm" scope="request">
		
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <td>
    	
			<%if(session.getAttribute("getEdit")==null){ %>
			<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr class="tableHeadingNew"><td>Accessorial &amp; Gen Std Chrgs</td>
				 <td align="right">
					 <input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="addAGSS()"/>
			     </td>
				 <td align="right">
					<%--<input type="button" value="Delete" class="buttonStyleNew" id="delete" onclick="delete1()"/>
				 --%></td>
			 </tr>	
			<%}%>
			
			<tr>
			<td>
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
         	if(chargeType.equals("11289"))
         	{
         	%>
         	<%if(defaultRate.equals("E"))
         	{ %>
        	<td>$/cft</td>
			<td>$/100lbs</td>
			<%}else if(defaultRate.equals("M"))
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
         	<td>Percent</td>
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
  			<td ><html:text property="chargeType" value="<%=chartypedesc%>" readonly="true"/></td>
            <td><html:checkbox property="standard" name="airStd"></html:checkbox></td>
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
       	 	 <td><html:text property="percentage" maxlength="3" size="3" onblur="checkdec(this)" styleId="percentage" value="<%=percentage%>"/></td>
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
  			<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true" /></td>
  			<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
		<td> 	 <input type="button" value="Delete" class="buttonStyleNew" id="delete" onclick="delete1()"/></td>
 	 </tr> 
        </table>
	</td></tr></table>
	</td>
	</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


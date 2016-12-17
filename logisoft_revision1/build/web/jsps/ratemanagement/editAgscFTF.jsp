<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.FTFStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
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
String chargecode=null;
String codeDesc=null;
List typeList=new ArrayList();
List agssList=null;
FTFStandardCharges ftfStandardCharges=new FTFStandardCharges();
ftfStandardCharges.setStandard("off");
ftfStandardCharges.setAsFrfgted("off");
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
FTFStandardCharges ftfStd=new FTFStandardCharges();
if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("ftfStandardCharges")!=null )
{
    session.setAttribute("visited","visited");
    ftfStandardCharges=(FTFStandardCharges)session.getAttribute("ftfStandardCharges");
	if(ftfStandardCharges.getChargeCode()!=null)
	{
		chargecode=ftfStandardCharges.getChargeCode().getCode();
		codeDesc=ftfStandardCharges.getChargeCode().getCodedesc();
		chartypedesc=ftfStandardCharges.getChargeType().getCodedesc();
	}
	
	if(ftfStandardCharges.getChargeType()!=null)
	{
	chargeType=ftfStandardCharges.getChargeType().getId().toString();
	}
	if(ftfStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(ftfStandardCharges.getMinAmt());
	}
	if(ftfStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=ftfStandardCharges.getEffectiveDate().toString();
	}
	if(ftfStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(ftfStandardCharges.getInsuranceRate());
	}
	if(ftfStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(ftfStandardCharges.getInsuranceAmt());
	}
	if(ftfStandardCharges.getPercentage()!=null)
	{
	double d=ftfStandardCharges.getPercentage();
	int i=(int)(d*1000);
	percentage=i+"";
	}

	if(ftfStandardCharges.getAmount()!=null)
	{
	amount=df.format(ftfStandardCharges.getAmount());
	}
	if(ftfStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(ftfStandardCharges.getAmtPerCft());
	}
	if(ftfStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(ftfStandardCharges.getAmtPer100lbs());
	}
	if(ftfStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(ftfStandardCharges.getAmtPerCbm());
	}
	if(ftfStandardCharges.getAmtPer1000Kg()!=null)
	{
	amtkg=df.format(ftfStandardCharges.getAmtPer1000Kg());
	}
	if(ftfStandardCharges.getStandard()!=null && ftfStandardCharges.getStandard().equals("Y"))
	{
	ftfStd.setStandard("on");
	}
	else
	{
	ftfStd.setStandard("off");
	}
	if(ftfStandardCharges.getAsFrfgted()!=null && ftfStandardCharges.getAsFrfgted().equals("X"))
	{
	ftfStd.setAsFrfgted("on");
	}
	else
	{
	ftfStd.setAsFrfgted("off");
	}
	if(ftfStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(ftfStandardCharges.getEffectiveDate());
	}
}
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
ftfStd.setStandard(airRatesBean.getStandard());
ftfStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("ftfStd",ftfStd);

if(session.getAttribute("ftfagssAdd") != null)
{
	agssList = (List) session.getAttribute("ftfagssAdd");
}

if(typeList != null)
{
	
	
	typeList=dbUtil.getTypeListforftfagss(new Integer(34),"yes","Select Rate type",agssList,chargeType);
	request.setAttribute("typeList",typeList);
	
}
else
{
 	
}
String modify="";
modify = (String) session.getAttribute("modifyforftfRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/editAgscFTF.do";
String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}

if(session.getAttribute("serachftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("serachftfdefaultRate");
}
%>
 
<html> 
	<head>
		<title>JSP for EditAgscFTFForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		function cancelbtn()
   		{
   		document.editAgscFTFForm.buttonValue.value="cancel";
		document.editAgscFTFForm.submit();
   		}
		function addAGSS()
		{
		
			document.editAgscFTFForm.buttonValue.value="edit";
  			document.editAgscFTFForm.submit();
		}
		
		function delete1()
		{
			
		 	document.editAgscFTFForm.buttonValue.value="delete";
			document.editAgscFTFForm.submit();
   		}
   		function submit()
   		{
   		document.editAgscFTFForm.buttonValue.value="";
		document.editAgscFTFForm.submit();
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
			document.editAgscFTFForm.submit();
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
  	 	var chkbox = document.getElementByTagName("checkbox");
  	 	alert("HIIIIIIIIIIIIIIIIii");
  	 	for(i=0;i<chkbox.length;i++)
  	 	{
  	 	   chkbox[i].readonly=true;
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
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
	</script>
		
	</head>
	<body  class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/editAgscFTF" name="editAgscFTFForm" type="com.gp.cong.logisoft.struts.ratemangement.form.EditAgscFTFForm" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td>
			<table width="100%">
			<tr>
			<td width="80%"></td>
			<td align="right">
			<% if(session.getAttribute("getFtfEdit")!=null)
   { %>
   					
   
			
<%}else{ %>
					
					<img src="<%=path%>/img/cancel.gif" border="0" id="cancel" onclick="addAGSS()" />
								</td>
								
								
								<%--<td align="right">
									<img src="<%=path%>/img/delete.gif" border="0" id="delete"
										onclick="delete1()" />
								</td>
								
	--%><%} %>
			</tr></table>
			<table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
  				<tr>
  					<td ></td></tr>
     			<tr class="tableHeadingNew"> Accessorial &amp; Gen Std Chrgs </tr>
     			<tr>
     			 <td>
     			 
			</td>
			</tr>
			
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
         
        	if(defaultRate.equals("E"))
					{ %>
					<td>
						$/cft
					</td>
					<td>
						$/100lbs
					</td>
					<%
					}
					else if(defaultRate.equals("M"))
					{ %>
					<td>
						$/cbm
					</td>
					<td>
						$/1000kg
					</td>
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
         <td><html:checkbox property="standard" name="ftfStd"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         <td><html:text property="amount" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11289"))
         {	
         if(defaultRate.equals("E")){
         %>
         <td><html:text property="amtPerCft" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPerCft" value="<%=cft%>"/></td>
  		 <td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
<%}	if(defaultRate.equals("M")){ %>
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
		  <td><html:checkbox property="asFrfgted" name="ftfStd"></html:checkbox></td>
		  <td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  <td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true" /></td>
  		<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
 
        </tr> 
        </table>
	</td></tr></table>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
       </td>
     			  </tr>
			</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


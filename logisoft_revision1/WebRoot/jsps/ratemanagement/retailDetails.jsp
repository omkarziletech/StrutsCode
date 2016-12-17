<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.beans.RetailRatesBean,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.RetailWeightRangesRates,com.gp.cong.logisoft.domain.GenericCode,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.RetailStandardCharges"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailOceanDetailsRates"/>
 <%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
DecimalFormat df = new DecimalFormat("0.00");
RetailRatesBean retailRatesBean=new RetailRatesBean();
retailRatesBean.setMetric("M");
String metric1000KG="",metricMinAmt="",metricCBM="",english1000lb="",englishLbs="",englishMinAmt;
String expressAmt="";
String defferedRate="";
String defferedAmt="";
String firstOcean="";
String firstTt="";
String secondOcean="";
String secondTt="";
String thirdOcean="";
String thirdTt="";
String fourthOcean="";
String fourthTt="";
String measureType="";
String userdisplay="";
String effectiveDate="";
List retailDetailsList = null;
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
User user=null;
RetailWeightRangesRates retailweightrangesrate=new RetailWeightRangesRates();

if(session.getAttribute("loginuser")!=null)
{
	user=(User)session.getAttribute("loginuser");
}
if(userdisplay != null)
{
	userdisplay=user.getFirstName();
}
String defaultRate="";
if(session.getAttribute("retaildefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("retaildefaultRate");
}
RetailOceanDetailsRates retailWeightRangeObj=new RetailOceanDetailsRates();
effectiveDate=dateFormat.format(new Date());
if(session.getAttribute("retailDetails")!=null)
{

	retailWeightRangeObj=(RetailOceanDetailsRates)session.getAttribute("retailDetails");
	if(retailWeightRangeObj.getMetric1000kg()!=null)
	{
	metric1000KG=df.format(retailWeightRangeObj.getMetric1000kg());
	}
	if(retailWeightRangeObj.getMetricMinAmt()!=null)
	{
	metricMinAmt=df.format(retailWeightRangeObj.getMetricMinAmt());
	}
	if(retailWeightRangeObj.getMetricCmb()!=null)
	{
	metricCBM=df.format(retailWeightRangeObj.getMetricCmb());
	}
	if(retailWeightRangeObj.getEnglish1000lb()!=null)
	{
	english1000lb=df.format(retailWeightRangeObj.getEnglish1000lb());
	}
	if(retailWeightRangeObj.getEnglishLbs()!=null)
	{
	englishLbs=df.format(retailWeightRangeObj.getEnglishLbs());
	}
	if(retailWeightRangeObj.getEnglishMinAmt()!=null)
	{
	englishMinAmt=df.format(retailWeightRangeObj.getEnglishMinAmt());
	}
	if(retailWeightRangeObj.getAOcean()!=null)
	{
	firstOcean=df.format(retailWeightRangeObj.getAOcean());
	}
	if(retailWeightRangeObj.getATt()!=null)
	{
	firstTt=df.format(retailWeightRangeObj.getATt());
	}
	if(retailWeightRangeObj.getBOcean()!=null)
	{
	secondOcean=df.format(retailWeightRangeObj.getBOcean());
	}
	if(retailWeightRangeObj.getBTt()!=null)
	{
	secondTt=df.format(retailWeightRangeObj.getBTt());
	}
	if(retailWeightRangeObj.getCOcean()!=null)
	{
	thirdOcean=df.format(retailWeightRangeObj.getCOcean());
	}
	if(retailWeightRangeObj.getCTt()!=null)
	{
	thirdTt=df.format(retailWeightRangeObj.getCTt());
	}
	if(retailWeightRangeObj.getDOcean()!=null)
	{
	fourthOcean=df.format(retailWeightRangeObj.getDOcean());
	}
	if(retailWeightRangeObj.getDTt()!=null)
	{
	fourthTt=df.format(retailWeightRangeObj.getDTt());
	}
	if(retailWeightRangeObj.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(retailWeightRangeObj.getEffectiveDate());

	}
	if(retailWeightRangeObj.getMeasureType()!=null){
measureType=retailWeightRangeObj.getMeasureType();
retailRatesBean.setMetric(measureType);
}
	
	
}


if(request.getAttribute("retailRatesBean")!=null)
{
retailRatesBean=(RetailRatesBean)request.getAttribute("retailRatesBean");
retailRatesBean.setMetric(retailRatesBean.getMetric());
}
request.setAttribute("retailRatesBean",retailRatesBean);
String editPath=path+"/retailDetails.do";




//retailweightrangesrate.setType("M");
//session.setAttribute("retailweightrangesrate",retailweightrangesrate);
String modify="";
modify = (String) session.getAttribute("modifyforretailRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}

%>
 
<html> 
	<head>
		<title>JSP for RetailDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		/*function type1(obj)
		{
		//alert(obj.value);
		if(obj.value=="M")
		{
		//alert(document.getElementById("met"));
		document.getElementById("met").style.visibility = 'visible';
		document.getElementById("eng").style.visibility = 'hidden';
		document.getElementById("met1").style.visibility = 'visible';
		document.getElementById("eng1").style.visibility = 'hidden';
		document.getElementById("met2").style.visibility = 'visible';
		document.getElementById("eng2").style.visibility = 'hidden';
		}
		
		}
		function type2(obj)
		{
		//alert(obj.value);
		if(obj.value=="E")
		{
		 document.getElementById("eng").style.visibility = 'visible';
		 document.getElementById("met").style.visibility = 'hidden';
		 document.getElementById("eng1").style.visibility = 'visible';
		 document.getElementById("met1").style.visibility = 'hidden';
		  document.getElementById("eng2").style.visibility = 'visible';
		 document.getElementById("met2").style.visibility = 'hidden';
		
		}
		
		}
		function retailDetails()
		{
			document.retailDetailsForm.buttonValue.value="add";
  			document.retailDetailsForm.submit();
		}*/
		/*function confirmdelete(obj)
		{
		 	var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('airweightratestable').rows[rowindex].cells;	
	   		document.retailDetailsForm.index.value=rowindex-1;
			document.retailDetailsForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this Weight Range");
			if(result)
			{
   				document.retailDetailsForm.submit();
   			}	
   		}*/
   		function changemetric()
		{
		document.retailDetailsForm.buttonValue.value="changemetric";
    	document.retailDetailsForm.submit();
		}
   		
   		function disabled(val1)
   {
  
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		   // imgs[k].style.visibility = 'hidden';
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
   		{
   			if(input[i].id != "buttonValue")
	 			{
					//alert(input[i].className);
					
					//input[i].className="areahighlightgreysmall";
	  				input[i].readOnly=true;
  	 			}
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++)
	 	{
	 			textarea[i].disabled=true;
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
	
   		function saveOFR()
   		{
   		 alert("HIIIIIIIIIII");
   		 document.retailDetailsForm.submit();
   		}
   		
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/retailDetails" name="retailDetailsForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailDetailsForm" scope="request">
		
		
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px">
<tr class="tableHeadingNew" >Ocean Freight Rate Details</tr>
 <tr><td>
 <table border="0" cellpadding="0" cellspacing="0" >
 <tr class="style2">
  
  <%//if(defaultRate!=null && defaultRate.equals("M"))
 // {
 // retailRatesBean.setMetric("M");*/
  %>
  <td>Port</td>
  <td>Metric<html:radio property="metric" value="M" name="retailRatesBean"></html:radio></td>
  <%//}
 // else if(defaultRate!=null && defaultRate.equals("E"))
 // { 
  //retailRatesBean.setMetric("E");
  %>
  <td>Port</td>
  <td>English<html:radio property="metric" value="E" name="retailRatesBean"></html:radio></td>
<%//} %>
  	</tr>
  	<tr class="style2">
  		<%// if(defaultRate.equals("M"))
  //	{%>
  	<td>Rate/CBM</td>
  	<td><html:text property="rateCbm" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=metricCBM%>"></html:text></td>
  	<%//} else if(defaultRate.equals("E"))
  	//{ %>
  		
  	<td>Rate/CFT</td>
  		<td><html:text property="rateCft" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=englishLbs %>"></html:text></td>
  		<%//}
  		//else
  		//{ %>
  		<td></td>
  		<td></td>
  		<%//} %>
  		</tr>
<tr class="style2">
  	<% //if(defaultRate.equals("M"))
  	//{%>
  	
  	<td>Rate/1000kGS</td>
  	<td><html:text property="rateKgs" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=metric1000KG%>"></html:text></td>
 <%//} else if(defaultRate.equals("E"))
  	//{ %>
  	<td>Rate/100LB</td>
  	<td><html:text property="rateLb" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10" value="<%=english1000lb%>"></html:text></td>
  		<%//}
  		//else
  		//{ %>
  		<td></td>
  		<td></td>
  		<%//} %>
 
  	
  	<tr class="style2">
  		<%// if(defaultRate.equals("M"))
  	//{%>
  	<td>Min Amt</td>
  	<td><html:text property="metricMinamt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=metricMinAmt%>"></html:text></td>
  		<%//} else if(defaultRate.equals("E"))
  	//{ %>
  		
  	<td>Min Amt</td>
  	<td><html:text property="englishMinamt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=english1000lb %>"></html:text></td>
  		<%//} 
  		//else
  		//{%>
  		<td></td>
  		<td></td>
  		<%//} %>
  	</tr>
  	<tr class="style2"><td>
  	Effective Date</td>
  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true"/>
  			<img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
  				
 
  	</tr>
  	</table>
  	</td>
  
  <td>
  	<table cellpadding="0" cellspacing="0">
  		<tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  		
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  			<tr class="tableHeadingNew" ><td>&nbsp;</td></tr>
  	</table>
  </td>
  
  <td>
  
  <table  border="0" cellpadding="0" cellspacing="0">
 <tr class="tableHeadingNew">Rate per Barrel</tr>
 	<tr class="style2">
 	<td>
 	</td>
 	<td>Ocean Freight</td>
  	<td>T&T</td>
 	</tr ><tr class="style2">
	 	 <td class="style2">Size(A)</td>
	  	<td><html:text property="firstOcean" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=firstOcean%>"></html:text></td>
	  	<td><html:text property="firstTt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=firstTt%>"></html:text></td>
	 </tr>
	 <tr class="style2">
		 <td>Size(B)</td>
	  	<td><html:text property="secondOcean" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=secondOcean%>"></html:text></td>
	  	<td><html:text property="secondTt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=secondTt%>"></html:text></td>
	</tr>  
	<tr class="style2">
		<td>Size(C)</td>
	  	<td><html:text property="thirdOcean" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=thirdOcean%>"></html:text></td>
	  	<td><html:text property="thirdTt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=thirdTt%>"></html:text></td>
	</tr>
	<tr class="style2">
		<td>Size(D)</td>
	  	<td><html:text property="fourthOcean" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=fourthOcean%>"></html:text></td>
	  	<td><html:text property="fourthTt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="10"  value="<%=fourthTt%>"></html:text></td>
	  
	</tr>
  
 	
</table>
  </td>
  </tr>	

</table>

	


  	  	<html:hidden property="buttonValue" styleId="buttonValue" />
		<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
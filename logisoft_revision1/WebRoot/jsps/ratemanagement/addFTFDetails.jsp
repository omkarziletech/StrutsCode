<%@ page language="java" import="com.gp.cong.logisoft.beans.AirRatesBean,java.util.*,java.text.DecimalFormat,com.gp.cong.logisoft.domain.FTFDetails"%>

<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="java.text.DateFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

	String path = request.getContextPath();
	AirRatesBean airRatesBean=new AirRatesBean();
	airRatesBean.setMetric("M");
	DecimalFormat df = new DecimalFormat("0.00");
	FTFDetails ftfDetails=new FTFDetails();
	String rate1000kgs1="";
	String rateCbm="";
	String ofMinAmtMetric="";
	String rate100lb1="";
	String rateCft="";
	String ofMinAmtEnglish="";
	String oceanFreightA="";
	String ttA="";
	String oceanFreightB="";
	String ttB="";
	String oceanFreightC="";
	String ttC="";
	String oceanFreightD="";
	String ttD="";
	String measureType="";
	String effDate="";
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
if(request.getAttribute("airRatesBean")!=null){
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airRatesBean.setMetric(airRatesBean.getMetric());
}
effDate=dateFormat.format(new Date());
String defaultRate="";
if(session.getAttribute("ftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("ftfdefaultRate");
}
if(session.getAttribute("ftfdetails")!=null)
{
ftfDetails=(FTFDetails)session.getAttribute("ftfdetails");

if(ftfDetails.getMetric1000kg()!=null)
{
rate1000kgs1=df.format(ftfDetails.getMetric1000kg());

}
if(ftfDetails.getMetricCbm()!=null)
{
rateCbm=df.format(ftfDetails.getMetricCbm());

}
if(ftfDetails.getMetricOfMinamt()!=null)
{
ofMinAmtMetric=df.format(ftfDetails.getMetricOfMinamt());
}
if(ftfDetails.getEnglish100lb()!=null)
{
rate100lb1=df.format(ftfDetails.getEnglish100lb());
}
if(ftfDetails.getEnglishCft()!=null)
{
rateCft=df.format(ftfDetails.getEnglishCft());
}
if(ftfDetails.getEnglishOfMinamt()!=null)
{
ofMinAmtEnglish=df.format(ftfDetails.getEnglishOfMinamt());
}
if(ftfDetails.getSizeAOf()!=null)
{
oceanFreightA=df.format(ftfDetails.getSizeAOf());
}
if(ftfDetails.getSizeATt()!=null)
{
ttA=df.format(ftfDetails.getSizeATt());
}
if(ftfDetails.getSizeBOf()!=null)
{
oceanFreightB=df.format(ftfDetails.getSizeBOf());
}
if(ftfDetails.getSizeBTt()!=null)
{
ttB=df.format(ftfDetails.getSizeBTt());
}
if(ftfDetails.getSizeCOf()!=null)
{
oceanFreightC=df.format(ftfDetails.getSizeCOf());
}
if(ftfDetails.getSizeCTt()!=null)
{
ttC=df.format(ftfDetails.getSizeCTt());
}
if(ftfDetails.getSizeDOf()!=null)
{
oceanFreightD=df.format(ftfDetails.getSizeDOf());
}
if(ftfDetails.getSizeDTt()!=null)
{
ttD=df.format(ftfDetails.getSizeDTt());
}
if(ftfDetails.getEffectiveDate()!=null)
	{
	effDate=dateFormat.format(ftfDetails.getEffectiveDate());

	}
if(ftfDetails.getMeasureType()!=null){
measureType=ftfDetails.getMeasureType();
airRatesBean.setMetric(measureType);
}
}


request.setAttribute("airRatesBean",airRatesBean);
String editPath=path+"/addFTFDetails.do";
String modify=null;
if(session.getAttribute("message")!=null){

modify=(String)session.getAttribute("view");
}
modify = (String) session.getAttribute("modifyforftfRates");
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}

%>

 
<html> 
	<head>
		<title>JSP for AddFTFDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function changemetric()
		{
		document.addFTFDetailsForm.buttonValue.value="changemetric";
    	document.addFTFDetailsForm.submit();
		}
		
		
	function disabled(val1)
   {
  
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
   		{
   			if(input[i].id != "buttonValue")
	 			{
					//alert(input[i].className);
					
				//	input[i].className="areahighlightgrey";
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
		</script>
		
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/addFTFDetails" name="addFTFDetailsForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddFTFDetailsForm" scope="request">
   <table width=100% border="0" cellpadding="0" cellspacing="3"  class="tableBorderNew">
     <tr class="tableHeadingNew"> LCL Co Load Details </tr>
	<tr>
	<td>
	  <table border="0">
	  <tr>
	  <td>
		<table class="style2" border="0">
		<tr>
		<td>Port</td>
			<td>Metric
 		 <html:radio property="metric" value="M" name="airRatesBean"></html:radio></td>
		</tr>
		<tr>
			<td>Rate/CBM</td>
  			<td><html:text property="rateCbm" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=rateCbm%>"></html:text></td>
		</tr>
		<tr>
			<td>Rate/1000kGS</td>
  			<td><html:text property="rate1000kgs" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"   value="<%=rate1000kgs1%>"></html:text></td>
		</tr>
		<tr>
			<td>OF Min Amt</td>
  			<td><html:text property="ofMinAmtMetric" onkeypress="check(this,4)" onblur="checkdec(this)"maxlength="7"   size="10"  value="<%=ofMinAmtMetric%>"></html:text></td>
		</tr>
		
		</table>
		</td>
		<td>
			<table class="style2" border="0">
				<tr>
					<td>Port</td>
  					<td>English<html:radio property="metric" value="E" name="airRatesBean"></html:radio></td>
				</tr>
				<tr>
					<td>Rate/CFT</td>
				 	<td><html:text property="rateCft" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=rateCft%>"></html:text></td>
				</tr>	
				<tr>
					<td>Rate/100LB</td>
					<td><html:text property="rate100lb" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=rate100lb1%>"></html:text></td>
				</tr>
				<tr>
					<td>OF Min Amt</td>
				 	<td><html:text property="ofMinAmtEnglish" onkeypress="check(this,4)" onblur="checkdec(this)"maxlength="7"  size="10"   value="<%=ofMinAmtEnglish%>"></html:text></td>
				</tr>	
			</table>
		</td>
		</tr>
		<tr> <td colspan="2">
		 		<table class="style2">
		 			<tr>
		 				<td>
			  		Effective Date</td>
  					<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effDate%>" size="7" readonly="true"/>
  					<img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
  				
		 			</tr>
		 		</table>
		</td></tr>
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
			<table  cellpadding="0" cellspacing="0">
				<tr class="tableHeadingNew"> Rate per Barrel</tr>
				<tr class="style2">
				<td></td>
					<td>Ocean Freight</td>
				  	<td align="center">T&T</td>
				</tr>
				<tr  class="style2">
					 <td>Size(A)</td>
				  	<td><html:text property="oceanFreightA" onkeypress="check(this,4)" onblur="checkdec(this)"maxlength="7"  size="10"  value="<%=oceanFreightA%>"></html:text></td>
				  	<td><html:text property="ttA" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=ttA%>"></html:text></td>
				</tr>
				<tr  class="style2">
					<td>Size(B)</td>
		  			<td><html:text property="oceanFreightB" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=oceanFreightB%>"></html:text></td>
  					<td><html:text property="ttB" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=ttB%>"></html:text></td>
				</tr>
				<tr  class="style2">
					<td>Size(C)</td>
				  	<td><html:text property="oceanFreightC" onkeypress="check(this,4)" onblur="checkdec(this)"maxlength="7"   size="10"  value="<%=oceanFreightC%>"></html:text></td>
				  	<td><html:text property="ttC" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=ttC%>"></html:text></td>
				</tr>
				<tr  class="style2">
					<td>Size(D)</td>
				  	<td><html:text property="oceanFreightD" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10"  value="<%=oceanFreightD%>"></html:text></td>
				  	<td><html:text property="ttD" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"   size="10" value="<%=ttD%>"></html:text></td>
				</tr>
			</table>
		</td>	
		</tr>
  	</table>
  	<html:hidden property="buttonValue" styleId="buttonValue" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


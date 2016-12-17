<%@ page language="java" import="com.gp.cong.logisoft.beans.AirRatesBean,java.util.*,java.text.DecimalFormat,com.gp.cong.logisoft.domain.LCLColoadDetails"%>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="java.text.DateFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setMetric("M");
List lclColoadList=new ArrayList();
DecimalFormat df = new DecimalFormat("0.00");
LCLColoadDetails lclColoadDetails=new LCLColoadDetails();
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
String MType="";
String effectiveDate="";
String EType="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
effectiveDate=dateFormat.format(new Date());
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airRatesBean.setMetric(airRatesBean.getMetric());
}
String defaultRate="";

if(session.getAttribute("lcldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("lcldefaultRate");

}
if(session.getAttribute("lclcoloaddetails")!=null)
{
lclColoadDetails=(LCLColoadDetails)session.getAttribute("lclcoloaddetails");

if(lclColoadDetails.getMetric1000kg()!=null)
{
rate1000kgs1=df.format(lclColoadDetails.getMetric1000kg());
}
if(lclColoadDetails.getMetricCbm()!=null)
{
rateCbm=df.format(lclColoadDetails.getMetricCbm());
}
if(lclColoadDetails.getMetricOfMinamt()!=null)
{
ofMinAmtMetric=df.format(lclColoadDetails.getMetricOfMinamt());
}
if(lclColoadDetails.getEnglish100lb()!=null)
{
rate100lb1=df.format(lclColoadDetails.getEnglish100lb());
}
if(lclColoadDetails.getEnglishCft()!=null)
{
rateCft=df.format(lclColoadDetails.getEnglishCft());
}
if(lclColoadDetails.getEnglishOfMinamt()!=null)
{
ofMinAmtEnglish=df.format(lclColoadDetails.getEnglishOfMinamt());
}
if(lclColoadDetails.getSizeAOf()!=null)
{
oceanFreightA=df.format(lclColoadDetails.getSizeAOf());
}
if(lclColoadDetails.getSizeATt()!=null)
{
ttA=df.format(lclColoadDetails.getSizeATt());
}
if(lclColoadDetails.getSizeBOf()!=null)
{
oceanFreightB=df.format(lclColoadDetails.getSizeBOf());
}
if(lclColoadDetails.getSizeBTt()!=null)
{
ttB=df.format(lclColoadDetails.getSizeBTt());
}
if(lclColoadDetails.getSizeCOf()!=null)
{
oceanFreightC=df.format(lclColoadDetails.getSizeCOf());
}
if(lclColoadDetails.getSizeCTt()!=null)
{
ttC=df.format(lclColoadDetails.getSizeCTt());
}
if(lclColoadDetails.getSizeDOf()!=null)
{
oceanFreightD=df.format(lclColoadDetails.getSizeDOf());
}
if(lclColoadDetails.getSizeDTt()!=null)
{
ttD=df.format(lclColoadDetails.getSizeDTt());
}
if(lclColoadDetails.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(lclColoadDetails.getEffectiveDate());

	}
if(lclColoadDetails.getMeasureType()!=null){
measureType=lclColoadDetails.getMeasureType();
airRatesBean.setMetric(measureType);
}
}


request.setAttribute("airRatesBean",airRatesBean);
String editPath=path+"/addLclColoadDetails.do";
String modify=null;
if(session.getAttribute("message")!=null){

modify=(String)session.getAttribute("view");
}
modify = (String) session.getAttribute("modifyforlclcoloadRates");
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}

%>
<html> 
	<head>
		<title>JSP for AddLclColaodDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function changemetric()
		{
		document.addLclColaodDetailsForm.buttonValue.value="changemetric";
    	document.addLclColaodDetailsForm.submit();
		}
		function add()
		{
		document.addLclColaodDetailsForm.buttonValue.value="save";
    	document.addLclColaodDetailsForm.submit();
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
					
					//input[i].className="areahighlightgrey";
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
	
		<html:form action="/addLclColaodDetails" name="addLclColaodDetailsForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddLclColaodDetailsForm" scope="request">
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px;padding:top-10px;">
    <tr class="tableHeadingNew" >LCL Co Load Details</tr>
    <tr></tr>
      <tr>
      	<td>	
      		<table><tr><td>
      		<table class="style2" cellpadding="0" cellspacing="0">
      			<tr></tr>
      			<tr>
   					<td>Port</td>
 					<td>Metric<html:radio property="metric" value="M" name="airRatesBean"></html:radio></td>
      			</tr>
      			<tr>
      				<td>Rate/CBM</td>
				  	<td><html:text property="rateCbm" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=rateCbm%>"></html:text></td>
      			</tr>
      			<tr>
      				<td>Rate/1000kGS</td>
				  	<td><html:text property="rate1000kgs" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=rate1000kgs1%>"></html:text></td>
      			</tr>
      			<tr>
      				<td>OF Min Amt</td>
				  	<td><html:text property="ofMinAmtMetric" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ofMinAmtMetric%>"></html:text></td>
      			</tr>
      			
      		</table>
      	</td>
      	<td>
      		<table class="style2" cellpadding="0" cellspacing="0">
      		<tr></tr>
      			<tr>
      				<td>Port</td>
					<td>English<html:radio property="metric" value="E" name="airRatesBean"></html:radio></td>
      			</tr>
      			<tr>
      				<td>Rate/CFT</td>
					 <td><html:text property="rateCft" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=rateCft%>"></html:text></td>
      			</tr>
      			<tr>
      				<td>Rate/100LB</td>
					<td><html:text property="rate100lb" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=rate100lb1%>"></html:text></td>
      			</tr>
      			<tr>
      				<td>OF Min Amt</td>
			 		<td><html:text property="ofMinAmtEnglish" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ofMinAmtEnglish%>"></html:text></td>
      			</tr>
      			
      		</table>
      	</td>
      	</tr>
      	<tr>
      	<td colspan="2">
      		<table class="style2">
      			<tr>
      				<td>Effective Date</td>
			  		<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>"  size="7" readonly="true"/>
  					<img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
  				
      			</tr>
      		</table>
      	</td>
      	</tr>
      	</table>
      	</td>
      	<td>
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
      		<table   cellpadding="0" cellspacing="0">
      			<tr class="tableHeadingNew">Rate per Barrel</tr>
      			<tr class="style2">
      				<td></td>
      				<td>Ocean Freight</td>
  					<td align="center">T&T</td>
      			</tr>
      			<tr class="style2">
      				 <td>Size(A)</td>
  					 <td><html:text property="oceanFreightA" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=oceanFreightA%>"></html:text></td>
  					 <td><html:text property="ttA" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ttA%>"></html:text></td>
  
      			</tr>
      			<tr class="style2">
      				<td>Size(B)</td>
				  	<td><html:text property="oceanFreightB" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=oceanFreightB%>"></html:text></td>
				  	<td><html:text property="ttB" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ttB%>"></html:text></td>
      			</tr>
      			<tr  class="style2">
      				<td>Size(C)</td>
  					<td><html:text property="oceanFreightC" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=oceanFreightC%>"></html:text></td>
  					<td><html:text property="ttC" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ttC%>"></html:text></td>
  
      			</tr>
      			<tr class="style2">
      				<td>Size(D)</td>
  					<td><html:text property="oceanFreightD" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=oceanFreightD%>"></html:text></td>
  					<td><html:text property="ttD" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7"  size="10"  value="<%=ttD%>"></html:text></td>
  
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


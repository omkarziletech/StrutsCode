<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.AirStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat"%>
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
AirStandardCharges airStandardCharges=new AirStandardCharges();
airStandardCharges.setStandard("off");
airStandardCharges.setAsFrfgted("off");
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
AirStandardCharges airStd=new AirStandardCharges();
if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("airStandardCharges")!=null )
{
    airStandardCharges=(AirStandardCharges)session.getAttribute("airStandardCharges");
	if(airStandardCharges.getChargeCode()!=null)
	{
		chargecode=airStandardCharges.getChargeCode().getCode();
		codeDesc=airStandardCharges.getChargeCode().getCodedesc();
		chartypedesc=airStandardCharges.getChargeType().getCodedesc();
	}
	
	if(airStandardCharges.getChargeType()!=null)
	{
	chargeType=airStandardCharges.getChargeType().getId().toString();
	}
	if(airStandardCharges.getMinAmt()!=null)
	{
		minAmt=df.format(airStandardCharges.getMinAmt());
	}
	if(airStandardCharges.getEffectiveDate()!=null)
	{
		effectiveDate=airStandardCharges.getEffectiveDate().toString();
	}
	if(airStandardCharges.getInsuranceRate()!=null)
	{
	insuranceRate=df.format(airStandardCharges.getInsuranceRate());
	}
	if(airStandardCharges.getInsuranceAmt()!=null)
	{
	insuranceAmt=df.format(airStandardCharges.getInsuranceAmt());
	}
	if(airStandardCharges.getPercentage()!=null)
	{
		double d=airStandardCharges.getPercentage();
	   int i=(int)(d*1000);
	   percentage=i+"";
	
	}
	if(airStandardCharges.getAmount()!=null)
	{
	amount=df.format(airStandardCharges.getAmount());
	}
	if(airStandardCharges.getAmtPerCft()!=null)
	{
	cft=df.format(airStandardCharges.getAmtPerCft());
	}
	if(airStandardCharges.getAmtPer100lbs()!=null)
	{
	lbs=df.format(airStandardCharges.getAmtPer100lbs());
	}
	if(airStandardCharges.getAmtPerCbm()!=null)
	{
	cbm=df.format(airStandardCharges.getAmtPerCbm());
	}
	if(airStandardCharges.getAmtPer1000kg()!=null)
	{
	amtkg=df.format(airStandardCharges.getAmtPer1000kg());
	}
	if(airStandardCharges.getStandard()!=null && airStandardCharges.getStandard().equals("Y"))
	{
	airStd.setStandard("on");
	}
	else
	{
	airStd.setStandard("off");
	}
	if(airStandardCharges.getAsFrfgted()!=null && airStandardCharges.getAsFrfgted().equals("X"))
	{
	airStd.setAsFrfgted("on");
	}
	else
	{
	airStd.setAsFrfgted("off");
	}
	if(airStandardCharges.getEffectiveDate()!=null)
	{
	effectiveDate=dateFormat.format(airStandardCharges.getEffectiveDate());
	}
}
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airStd.setStandard(airRatesBean.getStandard());
airStd.setAsFrfgted(airRatesBean.getAsFrfgted());
}
request.setAttribute("airStd",airStd);

if(session.getAttribute("agssAdd") != null)
{
	agssList = (List) session.getAttribute("agssAdd");
}

if(typeList != null)
{
	
	
	typeList=dbUtil.getTypeListforagss(new Integer(34),"yes","Select Rate type",agssList,chargeType);
	request.setAttribute("typeList",typeList);
	
}
else
{
 	
}
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/aGSSEdit.do";


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
		if(document.aGSSEditForm.chargeType.value=="0")
		{
		alert("Please select Charge Type");
		return;
		}
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
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')" bgcolor="gray"><br>
	<html:form action="/aGSSEdit" scope="request">
   <table width="100%" border="0" >
    <tr><td>
      <%if(session.getAttribute("getAirEdit")==null){ %>
      <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew">
      <tr class="tableHeadingNew"><td>Accessorial Gen Std Chrgs</td>
      	<td align="right">
         	 <input type="button" value="Go Back" onclick="addAGSS()" class="buttonStyleNew" id="cancel"/>
           	 <input type="button" value="Delete" onclick="delete1()" class="buttonStyleNew" id="delete"/>
      	 </td>
      </tr>
	 <%}%>
	 <tr><td>
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
         	if(chargeType.equals("11291"))
         	{
         	%>	<td>
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
 		 	<td><html:text property="charge" value="<%=chargecode%>" readonly="true" styleClass="varysizeareahighlightgrey" size="3"/></td>
   		 	<td><html:text property="desc" value="<%=codeDesc%>" readonly="true" styleClass="varysizeareahighlightgrey" size="12"/></td>
  		 	<td><html:text property="chargeType" value="<%=chartypedesc%>" readonly="true"/></td>
         	<td><html:checkbox property="standard" name="airStd"></html:checkbox></td>
         <% 
         if(chargeType.equals("11287"))
         {
         %>
         	<td><html:text property="amount" maxlength="8" size="8"  styleId="amount" value="<%=amount%>"/></td>
         <% 
         }
         if(chargeType.equals("11291"))
         {
        %> 
         	<td><html:text property="amtPer100lbs" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="amtPer100lbs" value="<%=lbs%>"/></td>
  	    	<td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
        	 <%
        }
        
         if(chargeType.equals("11288"))
         {
         %>
        	 <td><html:text property="percentage"  onblur="checkdec(this)" maxlength="3" size="3"  styleId="percentage" value="<%=percentage%>"/></td>
			 <td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"  styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 if(chargeType.equals("11290"))
		 {
		 %>
		  	<td><html:checkbox property="asFrfgted" name="airStd"></html:checkbox></td>
		  	<td><html:text property="insuranceRate" onkeypress="check(this,3)" onblur="checkdec(this)" maxlength="6" size="6"  styleId="minAmt" value="<%=insuranceRate%>"/></td>
		  	<td><html:text property="insuranceAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=insuranceAmt%>"/></td>
		  	<td><html:text property="minAmt" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8" styleId="minAmt" value="<%=minAmt%>"/></td>
		 <%
		 }
		 %>
  			<td><html:text property="txtItemcreatedon" styleId="txtitemcreatedon" value="<%=effectiveDate%>" size="7" readonly="true" /></td>
  			<td><div><img src="<%=path%>/img/CalendarIco.gif" alt="cal"   align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" /></div></td>
     </tr> 
    </table>
    </td></tr>
  </table>
  </td></tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.beans.PortsBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="portsair" class="com.gp.cong.logisoft.struts.form.PortDetailsForm" scope="request"></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c"%>
<%
String path = request.getContextPath();

DBUtil dbUtil=new DBUtil();
String cityName="";
String unCityCode="";
List countryList=new ArrayList();
List regionCodeList=new ArrayList();
Ports portobj=new Ports();
Ports portobject=new Ports();
String regionId="0";
String pierId="";
String portNameAbbr="";
String country="";
String type="";
List pierCode=new ArrayList();

if(session.getAttribute("portobject")!=null)
{
portobject=(Ports)session.getAttribute("portobject");

if(portobject.getShedulenumber()!=null)
{
pierId=portobject.getShedulenumber();
}
}
if(session.getAttribute("portobj")!=null)
{
portobj=(Ports)session.getAttribute("portobj");
if(portobj.getPortname()!=null)
{
cityName=portobj.getPortname();
}
if(portobj.getUncitycode()!=null && portobj.getUncitycode().getId()!=null)
{
unCityCode=portobj.getUncitycode().getUnLocationCode();
}
if(portobj.getCountryName()!=null)
{
country=portobj.getCountryName();
}
if(portobj.getRegioncode()!=null)
{
regionId=portobj.getRegioncode().getId().toString();
}
type = portobj.getType();
	
	if(type!=null && type.equals("K"))
	{
	portsair.setType("on");
	}
	else
	{
	portsair.setType("off");
	}
	

if(portobj.getDrABBR()!=null)
{
portNameAbbr=portobj.getDrABBR().toString();
}
}
 	if(countryList != null)
	{
		//countryList=dbUtil.getGenericCodeList(new Integer(11),"yes","Select Country");
		//request.setAttribute("countryList",countryList);
	}	

	if(regionCodeList != null)
	{
		regionCodeList=dbUtil.getGenericCodeList(new Integer(19),"yes","Select Region Code");
		request.setAttribute("regionCodeList",regionCodeList);
	}	
	String msg="";
	if(request.getAttribute("message")!=null)
	{
		msg=(String)request.getAttribute("message");
	}	
	if(pierCode != null)
	{
		//pierCode=dbUtil.getAllpiercode();
		//request.setAttribute("pierCode",pierCode);
	}
	Ports ports=new Ports();
	PortsBean portBean=new PortsBean();
	if(session.getAttribute("portBean")!=null)
	{
	portBean=(PortsBean)session.getAttribute("portBean");
	}
	 request.setAttribute("ports",ports);
	
%>

 
<html> 
	<head>
		<title>Port Details</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
			
			function submit1()
 			{ 
 				
 				document.portDetails.buttonValue.value="unCityCodeSelected";
 				document.portDetails.submit();
   			}
   			 function toUppercase(obj) 
	    {
			obj.value = obj.value.toUpperCase();
		}
		function ratespiercode()
		{
		document.portDetails.buttonValue.value="pierselected";
 				document.portDetails.submit();
   			}
			
			function cancel()
			{
				document.portDetails.buttonValue.value="cancel";
   				document.portDetails.submit();
 			}
 			
 			 
 			function enableISO(obj)
 			{
 				 
 				 document.getElementById("countryiso").value="";	 
 				 if(document.portDetails.type.checked==false)
 				 {
 				 	
 				 	var disD=document.getElementById("countryiso");
 				 	disD.disabled=true;
 				 	disD.className="areahighlightgrey";
 				 }
 				 else if(document.portDetails.type.checked)
 				 {
 				 	
 				 	var disK=document.getElementById("countryiso");
 				 	disK.disabled=false;
 				 	disK.className="areahighlightwhite";
 				 }
 				 
 		         document.portDetails.type.focus();
 			}
 			function enableISO1(obj)
 			{
 				
 				 if(obj=="null" || obj=="D")
 				 {
 				 	
 				 	var disD=document.getElementById("countryiso");
 				 	disD.disabled=true;
 				 	disD.className="areahighlightgrey";
 				 }
 				 else if(obj=="K")
 				 {
 				 	var disK=document.getElementById("countryiso");
 				 	
 				 	disK.disabled=false;
 				 	disK.className="areahighlightwhite";
 				 }
 			}
 function popup1(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
document.portDetails.submit();
return false;
}
 	function searchcity(){
 	document.portDetails.buttonValue.value="searchcity";
 	document.portDetails.submit();
 }
 			

		</script>
</head>

<body class="whitebackgrnd" onload="enableISO1('<%=type%>')">
<html:form action="/portDetails"  name="portDetails" type="com.gp.cong.logisoft.struts.form.PortDetailsForm" scope="request">
<font color="blue" size="4"><%=msg%></font>

<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew">Port Details</tr>
<tr>
  <td>
     <table width="100%" border="0">	
  	 <tr class="textlabelsBold">
    	<td>City Name &nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button='+'searchportcity&cityname='+document.portDetails.portName.value,'windows')"></td>
    	<td><bean:message key="form.ManagePortsForm.UNCityCode"/></td>
    	<td><bean:message key="form.ManagePortsForm.Country"/></td>
    	<td>Pier Abbreviation</td>
    </tr>
	<tr class="textlabelsBold" >
		<td><html:text property="portName" value="<%=cityName%>" maxlength="20" styleClass="textfieldstyle" onkeyup="searchcity()"  style="width:120px"/></td>
		<td><html:text property="unCityCode"  value="<%=unCityCode%>" readonly="true" styleClass="areahighlightgrey"  style="width:120px"/>
      				</td>
		<td ><html:text property="country"  value="<%=country%>"  readonly="true" styleClass="areahighlightgrey" style="width:120px"/>	       	
		<td ><html:text property="pierCode" value="<%=portobj.getPiercode()%>" styleClass="textfieldstyle"  maxlength="3" style="width:120px" onkeyup="toUppercase(this)"/></td>
                <td>Port City</td>
              <td>
                  <c:choose>
                      <c:when test="${portobj.portCity == 'Y'}">
                          <input type="radio" name="portCity" value="Y" checked>Y&nbsp;
                          <input type="radio" name="portCity" value="N">N
                      </c:when>
                      <c:otherwise>
                           <input type="radio" name="portCity" value="Y">Y&nbsp;
                          <input type="radio" name="portCity" value="N" checked>N
                      </c:otherwise>
                  </c:choose>
              </td>
	
   </tr>
   <tr class="textlabelsBold">
  	    <td><bean:message key="form.ManagePortsForm.ECIPortCode"/></td>
    	<td><table><tr class="style2">
    	<td align="left"><bean:message key="form.ManagePortsForm.DK"/></td>
    	<td></td>
    	<td></td>
    	<td align="left"></td>
    	</tr></table></td>
       	<td><bean:message key="form.ManagePortsForm.CountryISO"/></td>
        <td><bean:message key="form.ManagePortsForm.RegionCodes"/></td>
  </tr>
  <tr class="textlabelsBold" >
		<td><html:text property="eciPortCode" styleClass="textfieldstyle" value="<%=portobj.getEciportcode()%>"  maxlength="3" style="width:120px" onkeyup="toUppercase(this)"/></td>
		<td><table><tr class="style2">
    	<td><html:checkbox property="type"  name="portsair"  onclick="enableISO(this)" ></html:checkbox></td>
    	
    	<td align="left"></td>
  		<td></td>
 		</tr></table></td> 
 	   <td ><html:text property="countryISO" styleId="countryiso" value="<%=portobj.getIsocode()%>" styleClass="areahighlightgrey" onkeyup="toUppercase(this)" maxlength="2" style="width:120px"/></td>
  	   <td><html:select property="regionCodes" styleClass="dropdownboxStyle" value="<%=regionId%>" style="width:120px">
      			<html:optionsCollection name="regionCodeList"/>          
        	       	</html:select></td>
           <td>Omit 2 Letter Country Code</td>
    <td>
        <c:choose>
              <c:when test="${portobj.omit2LetterCountryCode == 'Y'}">
                  <input type="radio" name="omit2LetterCountryCode" value="Y" checked>Y&nbsp;
                  <input type="radio" name="omit2LetterCountryCode" value="N">N
              </c:when>
              <c:otherwise>
                   <input type="radio" name="omit2LetterCountryCode" value="Y">Y&nbsp;
                  <input type="radio" name="omit2LetterCountryCode" value="N" checked>N
              </c:otherwise>
          </c:choose>
    </td>
    	
 </tr>
 <tr class="textlabelsBold" >
			<td>Port Name Abbreviation</td>
    	 	<td><table width="100%" border="0"><tr class="style2"><td>Rates From Pier Code&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'searchportcity','windows')"></td></tr></table></td>
  			<td class="style2">Rates From Control Number</td>
  			<td class="style2">Rates from Port Name</td>
 </tr>
 <tr class="textlabelsBold" >
    		<td><html:text property="portNameAbbrevation" styleClass="textfieldstyle" value="<%=portNameAbbr%>" maxlength="4" onkeyup="toUppercase(this)" style="width:120px"/></td>
    		<td><html:text property="ratesFromPierCode" value="<%=pierId%>" maxlength="5" readonly="true" styleClass="areahighlightgrey" style="width:120px"/>
        	</td>
    		<td><html:text property="rateControl"  value="<%=portobject.getControlNo()%>" readonly="true" maxlength="5" styleClass="areahighlightgrey" style="width:120px"/></td>
  			<td><html:text property="ratePortName"   value="<%=portobject.getPortname()%>" styleClass="areahighlightgrey" readonly="true" style="width:120px"/></td>
 </tr>
</table>  	
</table>

		<html:hidden property="buttonValue" styleId="buttonValue"/>		
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


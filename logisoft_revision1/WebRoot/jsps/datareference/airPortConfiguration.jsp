<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.AirPortConfiguration,com.gp.cong.logisoft.domain.RefTerminal,
com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.domain.TradingPartner,
com.gp.cong.logisoft.beans.PortsBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<jsp:useBean id="airport" class="com.gp.cong.logisoft.struts.form.AirPortConfigurationForm"></jsp:useBean>
<%
String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();
GenericCode genericObj=null;
String terminalName="";
String terminalNo="";
String userId="";
String flight="0";
String airportCityName="";
String airportCode="";
List unCityCodeList=new ArrayList();
List flightList=new ArrayList();
List agencyInfoListForAir = null;
AgencyInfo agencyDefaultObj=new AgencyInfo();
agencyDefaultObj.setDefaultValue("Y");
request.setAttribute("agencyDefaultObj",agencyDefaultObj);
AirPortConfiguration airPortObj=new AirPortConfiguration();
PortsBean portBean=new PortsBean();
String airCode="0";
String airPortName="";
// changed source code
request.setAttribute("airServiceList", dbUtil.getSelectBoxList(new Integer(72), "Select Port Service"));
String printOnAirFitSch="";
if(session.getAttribute("airPortObj")!=null)
{
	airPortObj=(AirPortConfiguration)session.getAttribute("airPortObj");
	if(airPortObj.getTrmnum()!=null)
	{
		terminalNo=airPortObj.getTrmnum().getTrmnum();
		terminalName=airPortObj.getTrmnum().getTerminalLocation();
	}
	if(airPortObj.getFlightScheduleRegion()!=null)
	{
		flight=airPortObj.getFlightScheduleRegion().getId().toString();
	}
        if(airPortObj.getLineManager() !=null){
                userId=airPortObj.getLineManager();
        }
	/*if(airPortObj.getLineManager()!=null)
	{
		userId=airPortObj.getLineManager().getFirstName()+" "+airPortObj.getLineManager().getLastName();
	}*/
  // code changed by chandu
 
	
	if(airPortObj.getAirPortCityId()!=null)
	{
		//airportCityName=airPortObj.getAirPortCityId().getId().toString();
		airportCityName=airPortObj.getAirPortCityId();

	}
	if(airPortObj.getAirPortId()!=null)
	{
		airCode=airPortObj.getAirPortId().getId().toString();
		if(airPortObj.getAirPortId().getCodedesc()!=null)
		{
			airPortName=airPortObj.getAirPortId().getCodedesc();
		}
	}
}
request.setAttribute("flightList",dbUtil.getGenericCodeList(new Integer(19),"yes","Select RegionCode"));
request.setAttribute("airPortCodeList",dbUtil.getGenericCodeList(new Integer(1),"No","Select AirPortCode"));
if(unCityCodeList != null)
{
	//unCityCodeList=dbUtil.getUnCityCodeForAirport();
	//request.setAttribute("unCityCodeList",unCityCodeList);
}

if(session.getAttribute("agencyInfoListForAir")!=null)
{
	agencyInfoListForAir = (List)session.getAttribute("agencyInfoListForAir");
	session.setAttribute("agencyInfoListForAirAdd",agencyInfoListForAir);
}	
AirPortConfiguration airPortConfigPrint = new AirPortConfiguration();
// changed from hyd.....
printOnAirFitSch= airPortConfigPrint.getPrintOnAirFitSch();
	if(printOnAirFitSch!=null && printOnAirFitSch.equals("Y"))
	{
	airport.setPrintOnAirFitSch("on");
	}
	else
	{
	airport.setPrintOnAirFitSch("off");
	}

String lclAirBlgoCollect= airPortObj.getLclAirBlgoCollect();
	if(lclAirBlgoCollect!=null && lclAirBlgoCollect.equals("Y"))
	{
	airport.setLclAirBlgoCollect("on");
	}
	else
	{
	airport.setLclAirBlgoCollect("off");
	}
//airPortConfigPrint.setPrintOnAirFitSch("Y");
//airPortConfigPrint.setLclAirBlgoCollect("Y");
airPortConfigPrint.setServiceAir("Y");
if(request.getAttribute("portBean")!=null)
{
	portBean=(PortsBean)request.getAttribute("portBean");
 	airPortConfigPrint.setPrintOnAirFitSch(portBean.getPrintAir());
 	airPortConfigPrint.setServiceAir(portBean.getServiceAir());
 	airPortConfigPrint.setLclAirBlgoCollect(portBean.getLclairbl());
}
request.setAttribute("airPortConfigPrint",airPortConfigPrint);	
session.setAttribute("agencyair","add");
%>	
<html> 
<head>
	<title>Airport Configuration</title>
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" type="text/javascript">
	function submit1()
 	{ 
 		document.AirPortConfigForm.buttonValue.value="terminalSelected";
 		document.AirPortConfigForm.submit();
 	}
 	function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
 	function submit2()
 	{ 
 		document.AirPortConfigForm.buttonValue.value="airportCodesSelected";
 		document.AirPortConfigForm.submit();
 	}
	function save()
	{
		document.AirPortConfigForm.submit();
	}
   	var newwindow = '';
    function openAgencyInfo()
    {
        if (!newwindow.closed && newwindow.location)
		{
            newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForAir.jsp";
        }
        else 
        {
         	newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForAir.jsp","","width=650,height=450");
         	if (!newwindow.opener) newwindow.opener = self;
        }
        if (window.focus) {newwindow.focus()}
        document.AirPortConfigForm.submit();
        return false;
      } 
     function limitText(limitField, limitCount, limitNum)
     {
		limitField.value = limitField.value.toUpperCase();
		if (limitField.value.length > limitNum)
		{
			limitField.value = limitField.value.substring(0, limitNum);
		}
		else 
		{
			limitCount.value = limitNum - limitField.value.length;
 		}
 	 }		
 	 // for the check box functions
function airchkall(){
    if(document.AirPortConfigForm.printOnAirFitSch.checked)
    {
     
    document.AirPortConfigForm.printOnAirFitSch.value="Y";
     
     document.AirPortConfigForm.printOnAirFitSch.focus();
    
    return false;
    }	  
    }
    // for the check box functions
    
    function lclchkall(){
    if(document.AirPortConfigForm.lclAirBlgoCollect.checked)
    {
     
    document.AirPortConfigForm.lclAirBlgoCollect.value="Y";
     
     document.AirPortConfigForm.lclAirBlgoCollect.focus();
    
    return false;
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
mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
mywindow.moveTo(200,180);

           document.AirPortConfigForm.submit();
return false;
}
	function getAirPortsConfig(){
	alert("Main Page");
		window.location.href="<%=path%>/jsps/datareference/airPortConfiguration.jsp";
	}
	
	</script>
	<%@include file="../includes/resources.jsp" %>	
</head>
<body  class="whitebackgrnd">
<html:form action="/airPortConfiguration" name="AirPortConfigForm" type="com.gp.cong.logisoft.struts.form.AirPortConfigurationForm" scope="request">
<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">AirPort Configuration</tr>
<tr>
   <td>
   <table width="100%" border="0">
	<tr>
  		<td><table width="100%" border="0"><tr class="style2"><td>Terminal No&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'air','windows')"></td></tr></table></td>
  		
  		<td class="style2">Terminal Location</td>
  			<td>Line Manager&nbsp;
                            <%--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'air','windows')">--%>
                        </td>
  		<td><span class="style2">LCL air B/L go collect</span></td>
  	</tr>
	<tr>
  		<td><html:text property="terminalNo" value="<%=terminalNo%>" readonly="true" style="width:120px" /></td>
  		<td class="style2"><html:text property="terminalName" value="<%=terminalName%>" maxlength="100" styleClass="areahighlightgrey" readonly="true" style="width:120px" /></td>
  		<td><html:text property="lineManager" value="<%=userId%>"  style="width:120px" /></td>
  		<td>
  			<table>
  				<tr class="style2">
  					<td><html:checkbox property="lclAirBlgoCollect"  name="airport" onclick="lclchkall()"></html:checkbox></td>
    				<td> </td>
    		    </tr>
    		</table>
    	</td>
  	</tr>
	<tr class="style2">
    	<td><bean:message key="form.AirPortConfigForm.AirPortCode" /></td>
    	<td>AirPort Name</td>
    	<td><bean:message key="form.AirPortConfigForm.AirPortCityName"/> &nbsp; <img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button='+'newairportcity','windows')"> </td>
    	<td>Flight Schedule Region</td>
    </tr>
    <tr>
    	<td height="14"><html:select property="airPortCode" value="<%=airCode%>" onchange="submit2()" style="width:120px"><html:optionsCollection name="airPortCodeList"/></html:select></td>
  		<td><html:text property="airPortName" value="<%=airPortName%>" readonly="true" styleClass="areahighlightgrey" style="width:120px"/></td>
    	 <td><html:text property="airPortCityName"   value="<%=airportCityName%>" style="width:120px"/>
        <td><html:select property="flightScheduleRegion" value="<%=flight%>" style="width:120px">
      				<html:optionsCollection name="flightList" />          
        	       	</html:select></td>       	
    </tr>
</table>
</td>
</tr>

<tr>
   <td>
      <table>
		<tr>        	       	
    		<td colspan="4">
    		   <input type="button" class="buttonStyleNew" id="search" value="Agency Info"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForAir.jsp?relay='+'add',400,900)"/>          			
    		
       	</tr>
       	<tr>
      	 	<td>	
       		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
        	<% 
        		int i=0;
        		int k=0;
        	%>
        	<display:table name="<%=agencyInfoListForAir%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
			<display:setProperty name="paging.banner.some_items_found">
			<span class="pagebanner"> <font color="blue">{0}</font>
			Port Exceptions Displayed,For more Data click on Page Numbers. <br>
			</span>
			</display:setProperty>
			<display:setProperty name="paging.banner.one_item_found">
			<span class="pagebanner">
			One {0} displayed. Page Number
			</span>
			</display:setProperty>
			<display:setProperty name="paging.banner.all_items_found">
			<span class="pagebanner">
			{0} {1} Displayed, Page	Number
			</span>
			</display:setProperty>
			<display:setProperty name="basic.msg.empty_list">
			<span class="pagebanner"> No Records Found. </span>
			</display:setProperty>
			<display:setProperty name="paging.banner.placement"
										value="bottom" />
			<display:setProperty name="paging.banner.item_name"
										value="Agency Info" />
			<display:setProperty name="paging.banner.items_name"
										value="Agency Info" />
  			<%
        		TradingPartner customerObj = null;
            	TradingPartner consigneeObj=null;
            	String agentAcountNo="";
				String agntName="";
				String conAcctNo="";
				String conName="";
            	if(agencyInfoListForAir!=null && agencyInfoListForAir.size()>0)
            	{
            		AgencyInfo agencyInfoObj=(AgencyInfo)agencyInfoListForAir.get(i);
                	if(agencyInfoObj!=null)
                	{
                		customerObj= agencyInfoObj.getAgentId();
                    	consigneeObj=agencyInfoObj.getConsigneeId();
                    	if(customerObj!=null)
                    	{
                    		agentAcountNo = customerObj.getAccountno();
                        	agntName = customerObj.getAccountName();
                   	 	}
                    	if(consigneeObj!=null)
                    	{
                    		conAcctNo = consigneeObj.getAccountno();
                        	conName = consigneeObj.getAccountName();
                   		}
               		}
             	}
        	%>
	    	<display:column title="Agent Acct No"><%=agentAcountNo%></display:column>
			<display:column></display:column>
			<display:column></display:column>
			<display:column title="Agent Name" ><%=agntName%></display:column>
			<display:column></display:column>
			<display:column></display:column>
			<display:column title="Consignee AcctNo"><%=conAcctNo%></display:column>
			<display:column></display:column>
			<display:column></display:column>
			<display:column title="Consignee Name" ><%=conName%></display:column>
			<display:column></display:column>
			<display:column></display:column>
			<display:column title="Default" property="defaultValue"/>
			<display:column></display:column>
			<display:column></display:column>
			<% i++;
  		   	   k++;	
  			%>
			</display:table>
    	   	</table></div>
    	   	</td>
  	</tr>
<tr>
   <td>
	  <table>
	   <tr class="style2">
			<td><div align="right"></div></td>
			<td><div align="right"></div></td>
			<td><div align="left"></div></td>
	   </tr>  	
  		<tr class="style2">
    		<td style="width: 130px"><bean:message key="form.AirPortConfigForm.PrintonAirFitSch"/>(Y/N)</td>
                <td><div align="left"><html:checkbox property="printOnAirFitSch"  name="airport" onclick="airchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
        	<td><bean:message key="form.AirPortConfigForm.ServiceAir"/></td>
    		<td style="width: 70px">
                    <html:select property="serviceAir" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${airPortObj.serviceAir}">
                          <html:optionsCollection name="airServiceList"/>
                    </html:select>
                </td>
    	</tr>
	</table>
</td>
</tr>

<tr>
   <td>
	<table>  	
   	 <tr class="style2">
    	<td><bean:message key="form.AirPortConfigForm.AirSplRemarksEnglish"/></td>
    	<td><bean:message key="form.AirPortConfigForm.AirSplRemarksSpanish"/> </td>
   	 </tr>
  	<tr class="style2">
    	<td ><html:textarea property="airPortSplRemarksinEnglish" cols="58" rows="3" value="<%=airPortObj.getAirSplRemarksEnglish()%>" onkeyup="limitText(this.form.airPortSplRemarksinEnglish,this.form.countdown,300)" styleClass="textareastyle"/></td>
  		<td><html:textarea property="airPortSplRemarksinSpanish" cols="58" rows="3" value="<%=airPortObj.getAirSplRemarksSpanish()%>" onkeyup="limitText(this.form.airPortSplRemarksinSpanish,this.form.countdown,300)" styleClass="textareastyle"/></td>
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


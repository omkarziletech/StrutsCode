<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,com.gp.cong.logisoft.beans.PortsBean,com.gp.cong.logisoft.domain.User,java.util.Set,java.util.Iterator,com.gp.cong.logisoft.domain.AirPortConfiguration,com.gp.cong.logisoft.domain.RefTerminalTemp,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.Consignee"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="airport" class="com.gp.cong.logisoft.struts.form.EditAirportConfigForm"></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();

RefTerminalTemp terminalobj=null;
GenericCode genericObj=null;

String terminalName="";
String terminalNo="";
String airportCityName="";

String flight="0";

List agencyInfoListForAir = null;
AgencyInfo agencyDefaultObj=new AgencyInfo();
agencyDefaultObj.setDefaultValue("N");
request.setAttribute("agencyDefaultObj",agencyDefaultObj);

AirPortConfiguration airPortObj = null;
UnLocation unLocationObj = null;
String airPortCityName="";


// source changed from hyd
String printOnAirFitSch="";

String airCode="0";
String modify="";
AirPortConfiguration airPortConfigPrint = new AirPortConfiguration();
//airPortConfigPrint.setPrintOnAirFitSch("Y");
airPortConfigPrint.setLclAirBlgoCollect("Y");
airPortConfigPrint.setServiceAir("Y");

String splRemarksEnglish="";
String splRemarksSpanish="";
User userObj=null;
String airPortName="";
PortsBean portBean=new PortsBean();
String linemanager="";
request.setAttribute("airServiceList", dbUtil.getSelectBoxList(new Integer(72), "Select Port Service"));
if(session.getAttribute("airPortObjConfigConfiguration") != null)
	{
			airPortObj=(AirPortConfiguration)session.getAttribute("airPortObjConfigConfiguration");
			terminalobj = airPortObj.getTrmnum();
			if(terminalobj != null)
			{
				terminalNo = terminalobj.getTrmnum();
				terminalName = terminalobj.getTerminalLocation();
			}
			/*userObj=airPortObj.getLineManager();
			if(userObj != null)
			{
				linemanager=userObj.getFirstName()+" "+userObj.getLastName();
			}*/
			airPortCityName=airPortObj.getAirPortCityId();
			if(airPortObj.getAirPortId()!=null)
			{
				airCode=airPortObj.getAirPortId().getId().toString();
				airPortName=airPortObj.getAirPortId().getCodedesc();
			}
			// to edit the city
		
			
			if(airPortObj.getAirSplRemarksEnglish()!= null)
 			{
 				splRemarksEnglish=airPortObj.getAirSplRemarksEnglish();
 			}
                        if(airPortObj.getLineManager() !=null){
				linemanager=airPortObj.getLineManager();
			}
 			if(airPortObj.getAirSplRemarksSpanish() != null)
 			{	
 				splRemarksSpanish=airPortObj.getAirSplRemarksSpanish();
			}
			if(airPortObj.getFlightScheduleRegion()!=null)
			{
				flight=airPortObj.getFlightScheduleRegion().getId().toString();
			}
			printOnAirFitSch= airPortObj.getPrintOnAirFitSch();
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
			// check from here
			airPortConfigPrint.setPrintOnAirFitSch(printOnAirFitSch);
 			airPortConfigPrint.setServiceAir(airPortObj.getServiceAir());
 			airPortConfigPrint.setLclAirBlgoCollect(airPortObj.getLclAirBlgoCollect());
		}
		

	request.setAttribute("airPortConfigPrint",airPortConfigPrint);	


request.setAttribute("flightList",dbUtil.getGenericCodeList(new Integer(19),"yes","Select RegionCode"));
request.setAttribute("airCodeList",dbUtil.getGenericCodeList(new Integer(1),"No","Select AirPortCode"));

	
	
 	if(session.getAttribute("agencyInfoListForAir")!=null)
	{
		agencyInfoListForAir = (List)session.getAttribute("agencyInfoListForAir");
		session.setAttribute("agencyInfoListForAirAdd",agencyInfoListForAir);
	}	
 	if(request.getAttribute("portBean")!=null)
 	{
 		portBean=(PortsBean)request.getAttribute("portBean");
 		airPortConfigPrint.setPrintOnAirFitSch(portBean.getPrintAir());
 		airPortConfigPrint.setServiceAir(portBean.getServiceAir());
 		airPortConfigPrint.setLclAirBlgoCollect(portBean.getLclcollect());
 	}
 modify = (String) session.getAttribute("modifyforports");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}	
	session.setAttribute("agencyair","edit");
%>


	
<html> 
<head>
	<title>Airport Configuration</title>
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" type="text/javascript">
	function submit1()
 	{ 
 		document.EditAirPortConfigForm.buttonValue.value="terminalSelected";
 		document.EditAirPortConfigForm.submit();
 	}
 	function submit2()
 	{ 
 		document.EditAirPortConfigForm.buttonValue.value="airportCodesSelected";
 		document.EditAirPortConfigForm.submit();
 	}
	function save()
	{
		document.EditAirPortConfigForm.submit();
	}
	
   	 function toUppercase(obj) 
	    {
			obj.value = obj.value.toUpperCase();
		}
		 var newwindow = '';
           function openAgencyInfo() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForAir.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForAir.jsp","","width=950,height=450");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
          
           return false;
           } 
           function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 if(imgs[k].id!="note")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" && input[i].name!="terminalName" && input[i].name!="airportName" && input[i].name!="airPortCityName")
	 		{
	  			input[i].readOnly=true;
	  			input[i].style.color="blue";
	  		}
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
   		for(i=0; i<textarea.length; i++)
	 	{
	 		
	  			textarea[i].readOnly=true;
	  			textarea[i].style.color="blue";
	  		
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 	}
  	 }
  	  function confirmnote()
	{
		document.EditAirPortConfigForm.buttonValue.value="note";
    	document.EditAirPortConfigForm.submit();
   	}	
   	 function limitText(limitField, limitCount, limitNum) {
	limitField.value = limitField.value.toUpperCase();
if (limitField.value.length > limitNum) {
limitField.value = limitField.value.substring(0, limitNum);
} else {
limitCount.value = limitNum - limitField.value.length;
 }
 
}	

// updated from the edit
function airchkall(){
    if(document.EditAirPortConfigForm.printOnAirFitSch.checked)
    {
     
    document.EditAirPortConfigForm.printOnAirFitSch.value="Y";
     
     document.EditAirPortConfigForm.printOnAirFitSch.focus();
    
    return false;
    }	  
    }// updated from the edit
    function lclchkall(){
    if(document.EditAirPortConfigForm.lclAirBlgoCollect.checked)
    {
     
    document.EditAirPortConfigForm.lclAirBlgoCollect.value="Y";
     
     document.EditAirPortConfigForm.lclAirBlgoCollect.focus();
    
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

           document.EditAirPortConfigForm.submit();
return false;
}	
	function editAirPortsConfig(){
		window.location.href="<%=path%>/jsps/datareference/editAirportConfig.jsp";
	}
	</script>
	<%@include file="../includes/resources.jsp" %>	
</head>
<body  class="whitebackgrnd" onLoad="disabled('<%=modify%>')" onkeydown="preventBack()">
<html:form action="/editAirportConfig" name="EditAirPortConfigForm" styleId="editAirPortConfigForm" type="com.gp.cong.logisoft.struts.form.EditAirportConfigForm" scope="request">
<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew"><td>AirPort Configuration</td>
<td align="right"> <input type="button" class="buttonStyleNew" id="note" value="Note"  onclick="confirmnote()" disabled="true"/></td>
</tr>
<tr>
   <td colspan="2">
    <table width="100%" cellpadding="3" cellspacing="0" border="0">
	<tr>
  		<td class="textlabelsBold">Terminal No</td>
  		<td class="textlabelsBold">Terminal Location</td>
        <td class="textlabelsBold">Line Manager</td>
  		<td class="textlabelsBold">LCL air B/L go collect</td>
  	</tr>
	<tr>
            <td><html:text property="terminalNo" styleId="terminalNumber" styleClass="textlabelsBoldForTextBox"  value="<%=terminalNo%>" readonly="true" style="width:120px"/>
  		<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'editair','windows')">
  		</td>
    	<td class="textlabelsBold"><html:text   property="terminalName" value="<%=terminalName%>" maxlength="100" readonly="true" styleClass="areahighlightgrey" style="width:120px"/></td>
  		<td><html:text  styleClass="textlabelsBoldForTextBox"  property="lineManager" value="<%=linemanager%>"  style="width:120px"/>
  		<%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'editair','windows')">--%>
  		</td>
  	    <td><html:checkbox property="lclAirBlgoCollect"  name="airport" onclick="lclchkall()"></html:checkbox> </td>
   	</tr>
    <tr class="textlabelsBold">
    	<td><bean:message key="form.AirPortConfigForm.AirPortCode" /></td>
    	<td>AirPort Name</td>
    	<td><bean:message key="form.AirPortConfigForm.AirPortCityName"/> </td>
    	<td>Flight Schedule Region</td>
    </tr>
    <tr>
    	<td height="14"><html:select property="airPortCode" styleClass="verysmalldropdownStyleForText" value="<%=airCode%>" onchange="submit2()" style="width:120px">
    	<html:optionsCollection name="airCodeList"/> </html:select></td>
    	<td><html:text   property="airportName" value="<%=airPortName%>" readonly="true" styleClass="areahighlightgrey" style="width:120px"/></td>
    	<td><html:text styleClass="textlabelsBoldForTextBox"  property="airPortCityName"   value="<%=airPortCityName%>"   style="width:120px"/>
    	<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button='+'editairportcity','windows')">
    	</td>
        <td><html:select property="flightScheduleRegion" styleClass="verysmalldropdownStyleForText" value="<%=flight%>" style="width:120px">
      				<html:optionsCollection name="flightList" /></html:select></td>
   </tr>
</table>
</td>
</tr>
<tr>
<td colspan="2">  
<table width="100%" cellpadding="3" cellspacing="0" border="0" class="tableBorderNew">
<tr class="tableHeadingNew">Agency Info
<span style="padding-left: 663px;"><input type="button" class="buttonStyleNew" id="agencyInfo" value="Agency Info"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForAir.jsp?relay='+'add',310,900)" /></span>
</tr>
<tr>        	       	
	<td>
    		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0" border="1">
        	<% 
        		int i=0;
        		int k=0;
        	%>
        	<display:table name="<%=agencyInfoListForAir%>" pagesize="<%=pageSize%>" class="displaytagstyleNew" id="portexceptiontable" style="width:100%">
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
			<display:column title="Default" property="defaultValue">
			<bean:message key="form.LCLPortsConfiguration.RadioDisplayTagY" />	
			</display:column>
			<display:column></display:column>
			<display:column></display:column>
			<% i++;
  		   	   k++;	
  			%>
			</display:table>
    	   	</table></div> 
 
</td>
</tr>
</table>
<table>
<tr>
<td colspan="2">
<table width="100%" cellpadding="3" cellspacing="0" border="0">
 	
  	<tr class="textlabelsBold">
            <td style="width: 130px"><bean:message key="form.AirPortConfigForm.PrintonAirFitSch"/>(Y/N)</td>
    	<td ><div align="left"><html:checkbox property="printOnAirFitSch"  name="airport" onclick="airchkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></div></td>
    	<td> </td>
    	<td>&nbsp;</td>
    	<td style="width: 70px"><bean:message key="form.AirPortConfigForm.ServiceAir"/></td>
    	<td>
            <html:select property="serviceAir" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${airPortObjConfigConfiguration.serviceAir}">
                  <html:optionsCollection name="airServiceList"/>
            </html:select>
        </td>
    </tr>
</table>
</td>
</tr>

<tr>
<td colspan="2">
	<table  width="100%" cellpadding="3" cellspacing="0" border="0">  	
    <tr class="textlabelsBold">
    	<td><bean:message key="form.AirPortConfigForm.AirSplRemarksEnglish"/></td>
    	<td><bean:message key="form.AirPortConfigForm.AirSplRemarksSpanish"/> </td>
    </tr>
  	<tr class="textlabelsBold">
            <td><html:textarea property="airPortSplRemarksinEnglish" styleId="airSplRemarksinEnglish" cols="58" rows="3" value="<%=splRemarksEnglish%>"  onkeyup="limitText(this.form.airPortSplRemarksinEnglish,this.form.countdown,300)" styleClass="textareastyle"/></td>
            <td><html:textarea property="airPortSplRemarksinSpanish" styleId="airSplRemarksinSpanish" cols="58" rows="3" value="<%=splRemarksSpanish%>"   onkeyup="limitText(this.form.airPortSplRemarksinSpanish,this.form.countdown,300)" styleClass="textareastyle"/></td>
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


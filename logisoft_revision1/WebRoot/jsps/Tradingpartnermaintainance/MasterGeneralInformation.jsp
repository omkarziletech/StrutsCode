<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.GeneralInformation,com.gp.cong.logisoft.beans.customerBean,java.util.*,java.text.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
GeneralInformation generalInformation=new GeneralInformation();
generalInformation.setIdType("E");
generalInformation.setGoalAcct("off");
generalInformation.setPoa("off");
generalInformation.setImportTrackingScreen("off");
generalInformation.setActivatePwdQuotes("off");
generalInformation.setAllowLclQuotes("off");
generalInformation.setAllowFclQuotes("off");
generalInformation.setFaxSailingSchedule("off");
generalInformation.setFclMailingList("off");
generalInformation.setChristmasCard("off");
generalInformation.setImportsCfs("U");
generalInformation.setPbaSurchrge("off");
generalInformation.setInsure("off");
generalInformation.setActive("off");
String maxday="";

String commodity="";
String impcommodity="";

String commodityDesc="";
String impcommodityDesc="";
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
String ldpwdactivate="";


String salescode="0";
String arcode="0";
String phone="";
String fax="";
String idText="";
customerBean customerbean=new customerBean();

if(session.getAttribute("customerbean")!=null)
{
customerbean=(customerBean)session.getAttribute("customerbean");
generalInformation.setIdType(customerbean.getIdType());
generalInformation.setGoalAcct(customerbean.getGoalAcct());
generalInformation.setPoa(customerbean.getPoa());
generalInformation.setImportTrackingScreen(customerbean.getImportTrack());
generalInformation.setActivatePwdQuotes(customerbean.getWebQuotes());
generalInformation.setAllowFclQuotes(customerbean.getWebFcl());
generalInformation.setAllowLclQuotes(customerbean.getWebLcl());
generalInformation.setReservedForFuture(customerbean.getReserved());
generalInformation.setFaxSailingSchedule(customerbean.getFaxSail());
generalInformation.setFclMailingList(customerbean.getFclMail());
generalInformation.setChristmasCard(customerbean.getChristmas());
generalInformation.setImportsCfs(customerbean.getImportCfs());
generalInformation.setPbaSurchrge(customerbean.getPba());
generalInformation.setInsure(customerbean.getInsure());
generalInformation.setEinMaster(customerbean.getEinmaster());
generalInformation.setCommodityMaster(customerbean.getCommoditymaster());
generalInformation.setImportMaster(customerbean.getImportmaster());
generalInformation.setActive(customerbean.getActive());
}
request.setAttribute("customerbean",customerbean);
request.setAttribute("generalInformation",generalInformation);

if(session.getAttribute("mastergeneralInformation")!=null)
{
generalInformation=(GeneralInformation)session.getAttribute("mastergeneralInformation");

if(generalInformation.getAd1Phones()!=null)
{
phone=dbUtil.appendstring(generalInformation.getAd1Phones());
}
if(generalInformation.getAd1Faxs()!=null)
{
fax=dbUtil.appendstring(generalInformation.getAd1Faxs());
}

if(generalInformation.getLdPwdActivated()!=null)
{
ldpwdactivate=dateFormat.format(generalInformation.getLdPwdActivated());

}

}


if(generalInformation!=null && generalInformation.getGenericCode()!=null && generalInformation.getGenericCode().getCode()!=null)
{
commodity=generalInformation.getGenericCode().getCode();

commodityDesc=generalInformation.getGenericCode().getCodedesc();
}

if(generalInformation.getMaxDaysBetVisits()!=null)
{
maxday=generalInformation.getMaxDaysBetVisits().toString();
}

if(generalInformation!=null && generalInformation.getImpCommodity()!=null && generalInformation.getImpCommodity().getCode()!=null)
{
impcommodity=generalInformation.getImpCommodity().getCode();
impcommodityDesc=generalInformation.getImpCommodity().getCodedesc();
}


if(generalInformation!=null && generalInformation.getSalescode()!=null && generalInformation.getSalescode().getId()!=null)
{
salescode=generalInformation.getSalescode().getId().toString();
}


request.setAttribute("commoditylist",dbUtil.getGenericCodeList(4,"no","Select Commodity Code"));
request.setAttribute("salescodelist",dbUtil.getGenericCodeList(23,"no","Select Sales Code"));

%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
   <title>JSP for GeneralInformationForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
	<script type="text/javascript">
	function selectidtype()
	{
	document.masterGeneralInformationForm.buttonValue.value="selectid";
    document.masterGeneralInformationForm.submit();
    }
   function selectcommodity()
   {
   document.masterGeneralInformationForm.buttonValue.value="selectcommodity";
   document.masterGeneralInformationForm.submit();
    }
     function selectimpcommodity()
   {
   document.masterGeneralInformationForm.buttonValue.value="selectimpcommodity";
   document.masterGeneralInformationForm.submit();
    }
    function selectsalescode()
    {
     document.masterGeneralInformationForm.buttonValue.value="selectsalescode";
   document.masterGeneralInformationForm.submit();
    }
    function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
	function limitText(limitField, limitCount, limitNum) {
	limitField.value = limitField.value.toUpperCase();
if (limitField.value.length > limitNum) {
limitField.value = limitField.value.substring(0, limitNum);
} else {
limitCount.value = limitNum - limitField.value.length;
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
         
        				   
          document.masterGeneralInformationForm.submit();
return false;
}	
	
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.masterGeneralInformationForm.commDesc.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
	    document.masterGeneralInformationForm.submit();
	
	}
	}
	
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.masterGeneralInformationForm.commDesc.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
		document.masterGeneralInformationForm.submit();
	
	}
	}
	
		function getComCodeName(ev)
	{
		if(event.keyCode==9){
		document.masterGeneralInformationForm.commodity.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
	    document.masterGeneralInformationForm.submit();
	
	}
	}
	
	function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.masterGeneralInformationForm.commodity.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
		document.masterGeneralInformationForm.submit();
	
	}
	}
		
		
		function getImpComCode(ev)
	{
		if(event.keyCode==9){
		document.masterGeneralInformationForm.impCommodityDesc.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
	    document.masterGeneralInformationForm.submit();
	
	}
	}
	
	function getImpComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.masterGeneralInformationForm.impCommodityDesc.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
		document.masterGeneralInformationForm.submit();
	
	}
	}
	
		function getImpComCodeName(ev)
	{
		if(event.keyCode==9){
		document.masterGeneralInformationForm.impCommodity.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
	    document.masterGeneralInformationForm.submit();
	
	}
	}
	
	function getImpComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.masterGeneralInformationForm.impCommodity.value="";
		document.masterGeneralInformationForm.buttonValue.value="popupsearch";
		document.masterGeneralInformationForm.submit();
	
	}
	}
	
	</script>
	</head>
	<body class="whitebackgrnd" >
	<html:form action="/masterGeneralInformation" name="masterGeneralInformationForm" 
         type="com.gp.cong.logisoft.struts.form.MasterGeneralInformationForm" scope="request">
         
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">General Information</tr>
<tr class="style2">
   <td>
       <table width="87%" border="0">
            <tr class="style2">
			    <td width="15%" >Account Type</td>
			    <td width="27%"align="right">&nbsp;&nbsp;S</td>
			    <td><html:checkbox property="accountType1" name="customerbean"></html:checkbox></td>
			    <td >F</td>
			    <td><html:checkbox property="accountType2" name="customerbean"/></td>
			    <td >N</td>
			    <td><html:checkbox property="accountType3" name="customerbean"/></td>
			    <td>C</td>
			    <td><html:checkbox property="accountType4" name="customerbean"/></td>
			    <td>SS</td>
			    <td><html:checkbox property="accountType5" name="customerbean"/></td>
			    <td>T</td>
			    <td><html:checkbox property="accountType6" name="customerbean"/></td>
			    <td>A</td>
			    <td><html:checkbox property="accountType7" name="customerbean"/></td>
			    <td>I</td>
			    <td><html:checkbox property="accountType8" name="customerbean"/></td>
			    <td>E</td>
			    <td><html:checkbox property="accountType9" name="customerbean"/></td>
			    <td>V</td>
			    <td><html:checkbox property="accountType10" name="customerbean"/></td>
			    <td>O</td>
			    <td><html:checkbox property="accountType11" name="customerbean"/></td>
			    <td width="95%" align="right">&nbsp;</td>
			    <td width="95%" align="right">&nbsp;</td>
			    <td width="95%" align="right">&nbsp;</td>
			    <td width="95%" align="right">&nbsp;</td>
			    <td width="95%" align="right">&nbsp;</td>
			    <td width="95%" align="right"><html:checkbox property="active" name="generalInformation"/></td>
			    <td>Active</td>
			   
            </tr>
     </table>
   </td></tr>
   
<tr>
    <td>  
        <table width="100%" border="0" cellspacing="6" cellpadding="0" >     
          <tr class="style2">
             <td>
       			 <table>
        			<tr class="style2">
        				<td width="41%"></td>
        				<td width="25%"><bean:message key="form.customerForm.e" /></td>
        				<td width="25%"><bean:message key="form.customerForm.d" /></td>
						<td><bean:message key="form.customerForm.s" /></td>
        			</tr>
        		</table>
        	</td>
            <td>EIN/SSN</td>
            <td>DUNS</td> 
            <td>Import Tracking Screen2</td>
         </tr>
		 <tr class="style2">
					<td>
						<table>
							<tr class="style2" >
								<td><bean:message key="form.customerForm.idtype" /></td>
								<td><html:radio property="idType" value="E" name="generalInformation" ></html:radio></td>
								<td><html:radio property="idType" value="D" name="generalInformation" ></html:radio>
								</td>
								<td><html:radio property="idType" value="S" name="generalInformation" ></html:radio>
								</td>
							</tr>
						</table>
					</td>
					<td><html:text property="idtext" value="<%=generalInformation.getIdText()%>"/></td>
					<td><html:text property="dunsNo" value="<%=generalInformation.getDunsNo()%>"/></td>
                    <td>&nbsp;&nbsp;<html:checkbox property="importTrackingScreen" name="generalInformation"></html:checkbox></td>
        </tr>
        <tr class="style2">
        <tr class="style2"><td>Commodity#  </td>
                    <td><bean:message key="form.customerForm.commoditydesc" /> </td>
                    <td><bean:message key="form.customerForm.activatepassword" /></td>
        		    <td>Allow Web FCL Quotes</td>
      
      	</tr>
      	<tr class="style2">
            <td>
	<%--        <html:text property="commodity" styleClass="verysmalldropdownStyle" value="<%=commodity%>" readonly="true" ></html:text>--%>
	            <input name="commodity" id="commodity" size="20" value="<%=commodity%>"  onkeydown="getComCode(this.value)"  onkeypress="getComCodePress(this.value)" maxlength="6" size="6"/>
	           <dojo:autoComplete formId="masterGeneralInformationForm" textboxId="commodity" action="<%=path%>/actions/getChargeCode.jsp?tabName=MASTER_GENERAL_INFORMATION"/>
           </td>
           <td>
	<%--        <html:text property="commDesc" value="<%=commodityDesc%>" readonly="true"/>--%>
	            <input name="commDesc" id="commDesc"  onkeydown="getComCodeName(this.value)" onkeypress="getComCodeNamePress(this.value)" value="<%=commodityDesc %>" />    
	            <dojo:autoComplete formId="masterGeneralInformationForm" textboxId="commDesc" action="<%=path%>/actions/getChargeCodeDesc?tabName=MASTER_GENERAL_INFORMATION&from=0"/>
           </td>
           <td><html:checkbox property="activatePwdQuotes" name="generalInformation"></html:checkbox></td>
           <td>&nbsp;&nbsp;<html:checkbox property="allowFclQuotes" name="generalInformation"></html:checkbox></td>
       </tr>
      <tr class="style2"><td>Import Commodity # </td>
         <td><bean:message key="form.customerForm.importcommoditydesc" /></td>
         <td><bean:message key="form.customerForm.weblcl" /></td>
         <td><bean:message key="form.customerForm.faxsail" /></td>
      </tr>
      <tr class="style2">
	        <td>
	<%--        <html:text property="impCommodity" styleClass="verysmalldropdownStyle" value="<%=impcommodity%>" readonly="true" ></html:text>--%>
	            <input name="impCommodity" id="impCommodity"  size="20"  value="<%=impcommodity%>"  onkeydown="getImpComCode(this.value)"  onkeypress="getImpComCodePress(this.value)" maxlength="6" size="6"/>
	            <dojo:autoComplete formId="masterGeneralInformationForm" textboxId="impCommodity" action="<%=path%>/actions/getImportComCode.jsp"/> 
	        </td>
	        <td>
	<%--        <html:text property="impCommodityDesc" value="<%=impcommodityDesc%>" readonly="true"/>--%>
	            <input name="impCommodityDesc" id="impCommodityDesc"  onkeydown="getImpComCodeName(this.value)" onkeypress="getImpComCodeNamePress(this.value)" value="<%=impcommodityDesc %>" />    
	            <dojo:autoComplete formId="masterGeneralInformationForm" textboxId="impCommodityDesc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=MASTER_GENERAL_INFORMATION&from=1"/>
	        </td>
	        <td><html:checkbox property="allowLclQuotes" name="generalInformation"></html:checkbox></td>
	        <td>&nbsp;&nbsp;<html:checkbox property="faxSailingSchedule" name="generalInformation"></html:checkbox></td> 
      </tr>
     <tr class="style2">
        <td><bean:message key="form.customerForm.salescode" /></td>
      	<td><bean:message key="form.customerForm.salescodename" /></td>
        <td><bean:message key="form.customerForm.addphone" /> </td>
  		<td><bean:message key="form.customerForm.fclmail" /></td> 
     </tr>
    <tr class="style2">
         <td><html:select property="salesCode" styleClass="dropdownboxStyle" onchange="selectsalescode()" value="<%=salescode%>">
                   <html:optionsCollection name="salescodelist"/>
                	</html:select></td>
         <td><html:text property="salesCodeName" value="<%=generalInformation.getSalesCodeName()%>" readonly="true"/></td>    
         <td><html:text property="phone1" onkeypress="getIt(this)" maxlength="13" value="<%=phone%>"/></td>
    	 <td>&nbsp;&nbsp;<html:checkbox property="fclMailingList" name="generalInformation"></html:checkbox></td>
    </tr>
    <tr class="style2">
           <td><bean:message key="form.customerForm.addfax" /></td>
      	   <td><bean:message key="form.customerForm.inventorycode" /> </td>
      	   <td><span class="style2"><bean:message key="form.customerForm.maxday" /></span></td>
           <td><bean:message key="form.customerForm.christmascard" /></td>
    </tr>
    <tr class="style2">
            <td><html:text property="fax1" onkeypress="getIt(this)" maxlength="13" value="<%=fax%>"/></td>
     	    <td   class="style2"><html:text property="inventoryCode" value="<%=generalInformation.getInventoryCode()%>" maxlength="20" onkeyup="toUppercase(this)"/></td>
       	  	<td><html:text property="maxDay" maxlength="5" value="<%=maxday%>" onkeypress="return checkIts(event)" /></td>
       	    <td>&nbsp;&nbsp;<html:checkbox property="christmasCard" name="generalInformation"></html:checkbox></td>
    </tr>
	<tr class="style2">
 			<td><bean:message key="form.customerForm.frieghtfmc" /></td>
        	<td><bean:message key="form.customerForm.frieghtchb" /></td>
        	<td>&nbsp;Insure</td>
       		<td ><bean:message key="form.customerForm.goalacct" /></td>
	</tr>
    <tr class="style2">
      		<td><html:text property="frieghtFmc" value="<%=generalInformation.getFwFmcNo() %>" maxlength="25" onkeyup="toUppercase(this)"/></td>
       		<td><html:text property="frieghtChb" value="<%=generalInformation.getFwChbNo()%>"/></td>
       	    <td>&nbsp;&nbsp;<html:checkbox property="insure" name="generalInformation"></html:checkbox></td>
            <td >&nbsp;&nbsp;<html:checkbox property="goalAcct" name="generalInformation"></html:checkbox></td>
   </tr>   
   <tr class="style2">
            <td><span class="style2"><bean:message key="form.customerForm.spclremark" /> </span></td>
  			<td><span class="style2"><bean:message key="form.customerForm.defaultroute" /></span></td>
   			<td>Insurance Comments</td>
   			<td class="style2">&nbsp;&nbsp;<bean:message key="form.customerForm.poa" /></td>
   </tr>  
   <tr class="style2">
         <td><html:textarea property="spclRemark" styleClass="textareastyle" cols="25" value="<%=generalInformation.getSpecialRemarks() %>" onkeyup="limitText(this.form.spclRemark,this.form.countdown,200)"/></td>
    	 <td><html:textarea property="defaultRoute" styleClass="textareastyle" cols="25" value="<%=generalInformation.getRoutingInstruction() %>" onkeyup="limitText(this.form.defaultRoute,this.form.countdown,200)"></html:textarea></td>

		 <td align="left"><html:text property="insuranceComment" onkeyup="toUppercase(this)" value="<%=generalInformation.getInsuranceComment()%>" maxlength="60"></html:text></td>	
  		 <td>&nbsp;&nbsp;<html:checkbox property="poa" name="generalInformation"></html:checkbox></td>
  </tr>
  <tr class="style2">
        <td>Exempt From PBA surchrge</td>
        <td></td>
        <td><table><tr class="style2">
        <td width="30%" align="center"><bean:message key="form.customerForm.u" /></td>
        <td width="10%"><bean:message key="form.customerForm.f" /></td>
        <td><bean:message key="form.customerForm.blank" /></td>
     </tr></table></td>
  </tr>
  <tr class="style2">
        <td><html:checkbox property="pbaSurchrge" name="generalInformation"></html:checkbox></td>
  		<td><bean:message key="form.customerForm.importcfs" /></td>
  		<td><table><tr>
 		<td><html:radio property="importsCfs" value="U" name="generalInformation"></html:radio></td>
		<td><html:radio property="importsCfs" value="F" name="generalInformation"></html:radio>
		</td>
		<td><html:radio property="importsCfs" value="B" name="generalInformation"></html:radio>
		</td></tr></table></td>
  </tr>
</table>
</td>
</tr>
</table>

<table><tr><td>&nbsp;</td></tr></table>

<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Web Inventory Tracking</tr>
 <tr>
    <td> 
		<table width="100%">
 			 <tr class="style2">
  				<td><bean:message key="form.customerForm.username"/></td>
  			    <td><html:text property="userName" value="<%=generalInformation.getUserName()%>" maxlength="50" onkeyup="toUppercase(this)"/></td>
       	        <td><bean:message key="form.customerForm.password" /></td>
                <td><html:password property="password" value="<%=generalInformation.getPassword()%>" maxlength="50"/></td>
       	        <td><span class="style2"><bean:message key="form.customerForm.lastdate" /></span></td>
       	        <td><html:text property="lastDate" value="<%=ldpwdactivate%>" readonly="true"/></td>
       	    </tr>
        </table>
    </td>
 </tr>
</table>
<html:hidden property="buttonValue"/>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


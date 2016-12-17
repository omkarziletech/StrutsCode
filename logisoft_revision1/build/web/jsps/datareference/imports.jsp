<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.Consignee,com.gp.cong.logisoft.domain.ImportPortConfiguration"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();

String terminalName="";
String terminalNo="";
String importsService="";
String importAgentNumber="";
String importAgentWarehouse="";

List agencyInfoListForImp = null;
String userId="";
String chargeCode="";
ImportPortConfiguration importObj=new ImportPortConfiguration();
if(session.getAttribute("importPortObj")!=null)
{
	importObj=(ImportPortConfiguration)session.getAttribute("importPortObj");
	if(importObj.getTrmnum()!=null)
	{
		terminalNo=importObj.getTrmnum().getTrmnum();
		terminalName=importObj.getTrmnum().getTerminalLocation();
	}
	/*if(importObj.getLineManager()!=null)
	{
		userId=importObj.getLineManager().getFirstName()+" "+importObj.getLineManager().getLastName();
	}*/
	if(importObj.getOfChargeCode()!=null)
	{
		chargeCode=importObj.getOfChargeCode().getCodedesc();
	}
        if(importObj.getLineManager() !=null){
                userId=importObj.getLineManager();
        }
        if(importObj.getImportsService()!=null)
	{
			importsService=importObj.getImportsService();
	}
        if(importObj.getImportAgentNumber()!=null)
	{
			importAgentNumber=importObj.getImportAgentNumber();
	}
        if(importObj.getImportAgentWarehouse()!=null)
	{
			importAgentWarehouse=importObj.getImportAgentWarehouse();
	}
	
}
 	if(session.getAttribute("agencyInfoListForImpAdd") != null)
	{
		agencyInfoListForImp = (List)session.getAttribute("agencyInfoListForImpAdd");
		session.setAttribute("agencyInfoListForImp",agencyInfoListForImp);
	}	
	AgencyInfo agencyDefaultObj=new AgencyInfo();
	agencyDefaultObj.setDefaultValue("Y");
	request.setAttribute("agencyDefaultObj",agencyDefaultObj);
	session.setAttribute("agencyimport","add");
%>


	
<html> 
<head>
	<title>Airport Configuration</title>
	<%@include file="../includes/baseResources.jsp" %>
        <link rel="stylesheet" type="text/css" href="<%=path%>/css/autocomplete.css"/>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script language="javascript" src="<%=path%>/js/caljs/calendar.js" ></script>
        <script language="javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
	<script language="javascript" type="text/javascript">
	function submit1()
 	{ 
 		
 		document.importsForm.buttonValue.value="terminalSelected";
 		document.importsForm.submit();
 	}
 	
	function save()
	{
		document.importsForm.submit();
	}
	 
    	var newwindow = '';
           function openAgencyInfo() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForImp.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForImp.jsp","","width=650,height=450");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           document.importsForm.submit();
           return false;
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

            document.importsForm.submit();
return false;
}
	function getImplPortsConfig(){
		window.location.href="<%=path%>/jsps/datareference/imports.jsp";
	}
	</script>
	<%@include file="../includes/resources.jsp" %>	
</head>
<body class="whitebackgrnd">
<html:form action="/imports" name="importsForm" type="com.gp.cong.logisoft.struts.form.ImportsForm" scope="request">
<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew" >
<tr class="tableHeadingNew" colspan="2">Imports Port Configuration</tr>
<tr>
  <td>
	<table width="100%">
		<tr>
  			<td><table width="100%" border="0"><tr class="style2"><td>Terminal No&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'import','windows')"></td></tr></table></td>
  			<td class="style2">Terminal Location</td>
  			<td><table width="100%" border="0"><tr class="style2">
                                    <td>Line Manager&nbsp;
                                        <%--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'import','windows')">--%>
                                    </td>
                                </tr></table></td>
			<td><table width="100%" border="0"><tr class="style2"><td>OF Charge Code&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchChargeCode.jsp?button='+'import','windows')"></td></tr></table></td>
                        <td class="style2">Imports Service </td>
                        <td class="style2">Import Agent Number </td>
                        <td class="style2">Import Agent Warehouse</td>
  		</tr>
		<tr>
  			<td height="26" ><html:text property="terminalNo" value="<%=terminalNo%>" readonly="true">
      					</html:text>
    		</td>
  			<td class="style2"><html:text property="terminalName" value="<%=terminalName%>" maxlength="100" styleClass="areahighlightgrey" readonly="true"/></td>
  		  	<td><html:text property="lineManager" value="<%=userId%>" >
      			</html:text>
  			<td><html:text property="ofChargeCode" value="<%=chargeCode%>" readonly="true">
                            </html:text></td>
                        <td class="style2">
                             
                            <input type="radio" name="importsService" id="importsService" value="Y" >Y&nbsp;
                            <input type="radio" name="importsService" id="importsService" value="N">N
                        </td>
                        <td><html:text property="importAgentNumber" styleClass="textlabelsBoldForTextBox" styleId="importAgentNumber"
  			      value="<%=importAgentNumber%>" maxlength="10" />
                            <input id="importAgentNumber_check" type="hidden" value="<%=importAgentNumber%>"/>
                            <div id="importAgentNumber_choices"  style="width: 5px;"  align="right"  class="autocomplete"></div>
                            <script type="text/javascript">
				AjaxAutocompleter("importAgentNumber","importAgentNumber_choices","","importAgentNumber_check",
				"<%=path%>/actions/getTradingPartnerAccountName.jsp?tabName=IMPORT_AGENT_NUMBER&isDojo=false","","");
                            </script>
                        </td>
                        <td><html:text property="importAgentWarehouse" styleClass="textlabelsBoldForTextBox" styleId="importAgentWarehouse"
  			      value="<%=importAgentWarehouse%>" maxlength="3"/>
                            <input id="importAgentWarehouse_check" type="hidden" value="<%=importAgentWarehouse%>"/>
                            <div id="importAgentWarehouse_choices"  style="width: 5px;"  align="right"  class="autocomplete"></div>
                            <script type="text/javascript">
				AjaxAutocompleter("importAgentWarehouse","importAgentWarehouse_choices","","importAgentWarehouse_check",
				"<%=path%>/actions/getWareHouseDetails.jsp?tabName=IMPORT_AGENT_WAREHOUSE","","");
                            </script>
                        </td>
		</tr>	
	</table>
</td>
</tr>

<tr>
  <td colspan="4">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>    	
    		<td width="15%">
    		 <input type="button" class="buttonStyleNew" id="search" value="Agency Info"  onclick="return GB_show('Import', '<%=path%>/jsps/datareference/agencyInfoForImp.jsp?relay='+'add',350,700)"/>          			
                </td>
    		<td>
    		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        	<table width="100%" border="0" cellpadding="0" cellspacing="0">
        	<% 
        		int i=0;
        		int k=0;
        	%>
        	<display:table name="<%=agencyInfoListForImp%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
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
            	if(agencyInfoListForImp!=null && agencyInfoListForImp.size()>0)
            	{
            		AgencyInfo agencyInfoObj=(AgencyInfo)agencyInfoListForImp.get(i);
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
</table>
</td>
</tr>
</table>   
<html:hidden property="buttonValue" styleId="buttonValue"/>	
</html:form>
</body>
</html>


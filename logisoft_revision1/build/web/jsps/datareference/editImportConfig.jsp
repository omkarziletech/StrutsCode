<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="java.util.List,com.gp.cong.logisoft.domain.ImportPortConfiguration,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.AgencyInfo"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c"%>
<%

String path = request.getContextPath();
String terminalName="";
String terminalNo="";
List agencyInfoListForImp = null;

ImportPortConfiguration importsObj=null;

String linemanager="";
String ofChargeCode="";
String importsService="";
String importAgentNumber="";
String importAgentWarehouse="";

String modify="";

AgencyInfo agencyDefaultObj=new AgencyInfo();
		agencyDefaultObj.setDefaultValue("N");
	if(session.getAttribute("impPortObjConfiguration")!=null)
	{
		importsObj=(ImportPortConfiguration)session.getAttribute("impPortObjConfiguration");
		if(importsObj.getTrmnum()!=null)
		{
			terminalNo=importsObj.getTrmnum().getTrmnum();
			terminalName=importsObj.getTrmnum().getTerminalLocation();
		}
		/*if(importsObj.getLineManager()!=null)
		{
			linemanager=importsObj.getLineManager().getFirstName()+" "+importsObj.getLineManager().getLastName();
		}*/
		if(importsObj.getOfChargeCode()!=null)
		{
			ofChargeCode=importsObj.getOfChargeCode().getCodedesc();
		}
                if(importsObj.getLineManager() !=null){
				linemanager=importsObj.getLineManager();
			}
                if(importsObj.getImportsService()!=null)
		{
			importsService=importsObj.getImportsService();
		}
                if(importsObj.getImportAgentNumber()!=null)
		{
			importAgentNumber=importsObj.getImportAgentNumber();
		}
                if(importsObj.getImportAgentWarehouse()!=null)
		{
			importAgentWarehouse=importsObj.getImportAgentWarehouse();
		}

	}
 		if(session.getAttribute("agencyInfoListForImpAdd") != null)
		{
			agencyInfoListForImp = (List)session.getAttribute("agencyInfoListForImpAdd");
			session.setAttribute("agencyInfoListForImp",agencyInfoListForImp);
		}

		request.setAttribute("agencyDefaultObj",agencyDefaultObj);
		modify = (String) session.getAttribute("modifyforports");
                if (session.getAttribute("view") != null) {
                        modify = (String) session.getAttribute("view");

                }
                session.setAttribute("agencyimport","edit");
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

function submit1(){
  document.EditImportConfigForm.buttonValue.value="terminalSelected";
  document.EditImportConfigForm.submit();
}
function save(){
  document.EditImportConfigForm.submit();
}
var newwindow = '';
function openAgencyInfo(){
   if (!newwindow.closed && newwindow.location){
       newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForImp.jsp";
   }else {
       newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForImp.jsp","","width=950,height=450");
       if (!newwindow.opener) newwindow.opener = self;
   }
   if (window.focus) {
      newwindow.focus()
   }
   document.EditImportConfigForm.submit();
   return false;
}
function disabled(val1){
	if(val1 == 0 || val1== 3){
        var imgs = document.getElementsByTagName('img');
   		for(var k=0; k<imgs.length; k++){
   		  if(imgs[k].id!="note") {
   		    imgs[k].style.visibility = 'hidden';
   		  }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++){
	 		if(input[i].id != "buttonValue" && input[i].name!="terminalName"){
	  			input[i].readOnly=true;
			    input[i].style.color="blue";
	  		}
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
   		for(i=0; i<textarea.length; i++){
	  		textarea[i].readOnly=true;
			textarea[i].style.color="blue";
  	 	}
   		var select = document.getElementsByTagName("select");
   		for(i=0; i<select.length; i++){
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
        }
   }
}
function confirmnote(){
	document.EditImportConfigForm.buttonValue.value="note";
    document.EditImportConfigForm.submit();
}
function popup1(mylink, windowname){
	if (!window.focus)return true;
	var href;
	if (typeof(mylink) == 'string')
  		 href=mylink;
	else
   		href=mylink.href;
        mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
        mywindow.moveTo(200,180);

    document.EditImportConfigForm.submit();
    return false;
}
function editImplPortsConfig(){
  window.location.href="<%=path%>/jsps/datareference/editImportConfig.jsp";
}

	</script>
	<%@include file="../includes/resources.jsp" %>
</head>

<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')" onkeydown="preventBack()">
<html:form action="/editImportConfig" name="EditImportConfigForm" styleId="editImportConfigForm" type="com.gp.cong.logisoft.struts.form.EditImportConfigForm" scope="request">

<table width="100%" cellpadding="2" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"><td>Imports Port Configuration</td>
		<td align="right">
		   <input type="button" class="buttonStyleNew" id="note" value="Note"  onclick="confirmnote()"
		     disabled="true"/></td>
	</tr>
	<tr>
   	  <td colspan="2">
		<table width="100%" cellpadding="0" cellspacing="0">
		  <tr class="textlabelsBold">
  			 <td>Terminal No&nbsp;<img border="0" src="<%=path%>/img/icons/display.gif" alt="search"
  			      onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'editimport','windows')"></td>
  			 <td>Terminal Name</td>
  			 <td>Line Manager&nbsp;
                             <%--<img border="0" src="<%=path%>/img/icons/display.gif" alt="search"
  			      onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'editimport','windows')">--%>
                         </td>
  			 <td>OF Charge Code&nbsp;<img border="0" src="<%=path%>/img/icons/display.gif" alt="search"
  			      onclick="return popup1('<%=path%>/jsps/ratemanagement/searchChargeCode.jsp?button='+'editimport','windows')"></td>
                         <td>Imports Service </td>
                         <td>Import Agent Number </td>
                         <td>Import Agent Warehouse</td>
  		  </tr>
		  <tr class="textlabelsBold">
  			 <td><html:text property="terminalNo" styleClass="textlabelsBoldForTextBox" value="<%=terminalNo%>"
  			      readonly="true" onfocus="this.blur();"/></td>
    		 <td><html:text property="terminalName" value="<%=terminalName%>" maxlength="100"
    		      readonly="true" onfocus="this.blur();" styleClass="BackgrndColorForTextBox"/></td>
  			 <td><html:text property="lineManager" styleClass="textlabelsBoldForTextBox"
  			      value="${impPortObjConfiguration.lineManager}" /></td>
  			 <td><html:text property="ofChargeCode" styleClass="textlabelsBoldForTextBox"
  			      value="<%=ofChargeCode%>" readonly="true"/></td>
                         <td>
                             <c:choose>
                                 <c:when test="${impPortObjConfiguration.importsService == 'Y'}">
                                     <input type="radio" name="importsService" value="Y" checked>Y&nbsp;
                                    <input type="radio" name="importsService" value="N">N
                                </c:when>
                                 <c:when test="${impPortObjConfiguration.importsService == 'N'}">
                                     <input type="radio" name="importsService" value="Y" >Y&nbsp;
                                    <input type="radio" name="importsService" value="N" checked>N
                                </c:when>
                                <c:otherwise>
                                     <input type="radio" name="importsService" value="Y">Y&nbsp;
                                    <input type="radio" name="importsService" value="N">N
                                </c:otherwise>
                             </c:choose>

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
           <tr class="textlabelsBold">
                    <td>Over Weight Limit 20</td>
                    <td>Over Weight Limit 40</td>
                </tr>
                <tr class="textlabelsBold">
                    <td><html:text property="overWeightLimit20" value="${impPortObjConfiguration.overWeightLimit20}" styleClass="textlabelsBoldForTextBox" maxlength="6" onkeyup="checkForNumberAndDecimal(this)"></html:text></td>
                    <td><html:text property="overWeightLimit40" value="${impPortObjConfiguration.overWeightLimit40}" styleClass="textlabelsBoldForTextBox" maxlength="6" onkeyup="checkForNumberAndDecimal(this)"></html:text></td>
                </tr>
		</table>
	 </td>
	</tr>

    <tr>
	 <td colspan="2">
	   <table width="100%" cellpadding="0" cellspacing="0" border="0">
		 <tr>
    		 <td width="20%">
    		   <input type="button" class="buttonStyleNew" id="agencyInfo" value="Agency Info"
    		     onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForImp.jsp?relay='+'add',310,900)"/>
    		 </td>
    	     <td>
    		 <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
        	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	        	<%
	        		int i=0;
	        		int k=0;
	        	%>
        	<display:table name="<%=agencyInfoListForImp%>" pagesize="<%=pageSize%>" class="displaytagstyle"
        	  id="portexceptiontable" style="width:100%">
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
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Agency Info" />
				<display:setProperty name="paging.banner.items_name" value="Agency Info" />
  			<%
        		TradingPartner customerObj = null;
            	TradingPartner consigneeObj=null;
            	String agentAcountNo="";
				String agntName="";
				String conAcctNo="";
				String conName="";
            	if(agencyInfoListForImp!=null && agencyInfoListForImp.size()>0){
            		AgencyInfo agencyInfoObj=(AgencyInfo)agencyInfoListForImp.get(i);
                	if(agencyInfoObj!=null){
                		customerObj= agencyInfoObj.getAgentId();
                    	consigneeObj=agencyInfoObj.getConsigneeId();
                    	if(customerObj!=null){
                    		agentAcountNo = customerObj.getAccountno();
                        	agntName = customerObj.getAccountName();
                   	 	}
                    	if(consigneeObj!=null){
                    		conAcctNo = consigneeObj.getAccountno();
                        	conName = consigneeObj.getAccountName();
                   		}
               		}
             	}
        	%>
		    	<display:column title="Agent Acct No"><%=agentAcountNo%></display:column>
				<display:column title="Agent Name" ><%=agntName%></display:column>
				<display:column title="Consignee AcctNo"><%=conAcctNo%></display:column>
				<display:column title="Consignee Name" ><%=conName%></display:column>
				<display:column title="Default" property="defaultValue">
			       <bean:message key="form.LCLPortsConfiguration.RadioDisplayTagY" />
			    </display:column>

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


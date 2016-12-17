<%@ page language="java"
	import="java.util.*,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.struts.form.*"
	pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	DBUtil util = new DBUtil();
	List ruleList = new ArrayList();
	if (ruleList != null) {
		ruleList = util.getGenericCodeList(new Integer(20), "yes",
		"Select Rule");
		request.setAttribute("ruleList", ruleList);
	}
%>
<script language="javascript" type="text/javascript">

function enabletieramount1(obj){
	if(obj.value=="316" || obj.value=="319"){
		var disD=document.getElementById("tieramount1");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount1");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
}
function enabletieramount2(obj){
	if(obj.value=="316" || obj.value=="319") {
		var disD=document.getElementById("tieramount2");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount2");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
}
function enabletieramount3(obj){
	if(obj.value=="316" || obj.value=="319"){
 		var disD=document.getElementById("tieramount3");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount3");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
}
function enabletieramount4(obj){
	if(obj.value=="316" || obj.value=="319"){
 		var disD=document.getElementById("tieramount4");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount4");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
}
function check(){
	if(document.getElementById('ruleRouteByAgentAdmin').value == '316'){
		var disD=document.getElementById("tieramount1");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount1");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
 	if(document.getElementById('ruleRouteByAgentCommn').value=="316"){
 		var disD=document.getElementById("tieramount2");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount32");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
 	
 	if(document.getElementById('ruleRouteNotAgentAdmin').value=="316"){
 		var disD=document.getElementById("tieramount3");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount3");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
 	if(document.getElementById('ruleRouteNotAgentCommn').value=="316"){
 		var disD=document.getElementById("tieramount4");
 		disD.disabled=false;
 		disD.className="textlabelsBoldForTextBox";
 	}else{
 		var disK=document.getElementById("tieramount4");
 		disK.disabled=true;
 		disK.className="areahighlightgrey";
 	}
}
function isDecimal(obj){
          if(/[^0-9.]+|\d{1,2}$/.test(obj.value)){
              obj.value=obj.value.replace(/[^0-9.]+/g,'');
               if(obj.value.indexOf(".")>=0){
                    var values = obj.value.split(".");
                    if(values[1].length>=2){
                         obj.value=values[0]+"."+values[1].substr(0,2)
                    }
               }
          }
          else{
               obj.value=obj.value.replace(/[^0-9.]+/g,'');
          }
     }
function save(){
	document.agencyRulesForm.buttonValue.value = "save";
	document.agencyRulesForm.submit();
}
</script>
<%@include file="../includes/baseResources.jsp"%>
<html>
	<head>
		<base href="<%=basePath%>">
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/agencyRules" 
			type="com.gp.cong.logisoft.struts.action.AgencyRulesAction" scope="request">
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
				<tr class="tableHeadingNew"><td>Agency Rules</td>
				<tr>
					<td colspan="2">
						<table width="100%" cellpadding="2" cellspacing="0" border="0">
							<tr>
								<td height="24" scope="col">
									&nbsp;
								</td>
								<td class="textlabelsBold" scope="col">
									<div align="left">
										<bean:message key="form.FCLPortsConfiguration.Rule" />
									</div>
								</td>
								<td class="textlabelsBold" scope="col">
									<div align="left">
										<bean:message key="form.FCLPortsConfiguration.Amount(1)" />
									</div>
								</td>
								<td class="textlabelsBold" scope="col">
									<div align="left">
										<bean:message key="form.FCLPortsConfiguration.TierAmount" />
									</div>
								</td>
							</tr>
							<tr>
								<td height="26" class="textlabelsBold">
									<bean:message
										key="form.FCLPortsConfiguration.RouteByAgentAdmin" />
								</td>
								<td>
									<html:select property="ruleRouteByAgentAdmin" styleId="ruleRouteByAgentAdmin"
										styleClass="verysmalldropdownStyle" value="${AgencyRules.routeAgtAdminRule}"
										onchange="enabletieramount1(this)">
										<html:optionsCollection name="ruleList" />
									</html:select>
								</td>
								<td>
									<html:text property="amountRouteByAgentAdmin" maxlength="8" styleClass="textlabelsBoldForTextBox"
										size="8" value="${AgencyRules.routeAgtAdminAmt}"  onkeyup="isDecimal(this)" />
								</td>
								<td>
									<html:text property="tierAmountRouteByAgentAdmin"
										styleId="tieramount1" maxlength="8" value="${AgencyRules.routeAgtAdminTieramt}" disabled="true"
										styleClass="areahighlightgrey"
										onkeypress="getDecimal(this,5,event)" />
								</td>
							</tr>
							<tr>
								<td height="26" class="textlabelsBold">
									<bean:message
										key="form.FCLPortsConfiguration.RouteByAgentCommn" />
								</td>
								<td>
									<html:select property="ruleRouteByAgentCommn" styleId="ruleRouteByAgentCommn"
										styleClass="verysmalldropdownStyle" value="${AgencyRules.routeAgtCommnRule}"
										onchange="enabletieramount2(this)">
										<html:optionsCollection name="ruleList" />
									</html:select>
								</td>
								<td>
								
									<html:text property="amountRouteByAgentCommn" maxlength="8"  styleClass="textlabelsBoldForTextBox"
										size="8" value="${AgencyRules.routeAgtCommnAmt}"  onkeyup="isDecimal(this)" />
								</td>
								<td>
									<html:text property="tierAmountRouteByAgentCommn" maxlength="8"
										styleId="tieramount2" value="${AgencyRules.routeAgtCommnTieramt}" disabled="true"
										styleClass="areahighlightgrey"
										onkeypress="getDecimal(this,5,event)" />
								</td>
							</tr>
							<tr>
								<td height="26" class="textlabelsBold">
									Not Routed By Agent Admin
								</td>
								<td>
									<html:select property="ruleRouteNotAgentAdmin" styleId="ruleRouteNotAgentAdmin"
										styleClass="verysmalldropdownStyle" value="${AgencyRules.notRouteAgtAdminRule}"
										onchange="enabletieramount3(this)">
										<html:optionsCollection name="ruleList" />
									</html:select>
								</td>
								<td>
									<html:text styleClass="textlabelsBoldForTextBox" property="amountRouteNotAgentAdmin" maxlength="8"
										size="8" value="${AgencyRules.notRouteAgtAdminAmt}"  onkeyup="isDecimal(this)" />
								</td>
								<td>
									<html:text property="tierAmountRouteNotAgentAdmin" 
										maxlength="8" value="${AgencyRules.notRouteAgtAdminTieramt}" disabled="true" styleId="tieramount3"
										styleClass="areahighlightgrey" 
										onkeypress="getDecimal(this,5,event)" />
								</td>
							</tr>
							<tr>
								<td height="24" class="textlabelsBold">
									Not Routed BY Agent Commn
								</td>
								<td>
									<html:select property="ruleRouteNotAgentCommn" styleId="ruleRouteNotAgentCommn"
										styleClass="verysmalldropdownStyle" value="${AgencyRules.notRouteAgtCommnRule}"
										onchange="enabletieramount4(this)">
										<html:optionsCollection name="ruleList" />
									</html:select>
								</td>
								<td>
									<html:text property="amountRouteNotAgentCommn" maxlength="8"  styleClass="textlabelsBoldForTextBox"
										size="8" value="${AgencyRules.notRouteAgtCommnAmt}"  onkeyup="isDecimal(this)" />
								</td>
								<td>
									<html:text property="tierAmountRouteNotAgentCommn"
										maxlength="8" value="${AgencyRules.notRouteAgtCommnTieramt}" disabled="true" styleId="tieramount4"
										styleClass="areahighlightgrey" 
										onkeypress="getDecimal(this,5,event)" />
								</td>
							</tr>

						</table>
					</td>
				</tr>
				<tr>
					<td align="center" style="padding-top:10px;padding-bottom: 10px;"><input type="button" class="buttonStyleNew" style="width: 50px;" value="Save" name="Save" onclick="save()"></td>
				</tr>
			</table>
			<html:hidden property="index" value="${index}"/>
			<html:hidden property="buttonValue"/>
			<html:hidden property="agencyRulesId" value="${AgencyRules.id}"/>
		</html:form>
	</body>
	<script>check();</script>
	<c:if test="${close eq 'close'}">
		<script>parent.parent.GB_hide();</script>
	</c:if>
	<%@include file="../includes/baseResourcesForJS.jsp"%>
</html>

<%-- 
    Document   : consgineeInfo
    Created on : Dec 17, 2010, 12:39:23 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/resources.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@include file="../includes/baseResourcesForJS.jsp"%>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@include file="../fragment/formSerialize.jspf" %>
<html>
    <head>
		<title>Consignee Info</title>
	</head>
	<body>
		<html:form action="/tradingPartner" name="tradingPartnerForm" styleId="consigneeInfo"
				   type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" method="post" scope="request">
			<table width="100%" border="0" class="tableBorderNew" cellpadding="3" cellspacing="0">
				<tr class="tableHeadingNew"><td colspan="6">Consignee Information</td></tr>
				<tr class="textlabelsBold">
					<fmt:formatNumber var="portNumber" value="${TRADINGPARTNER.portNumber}" pattern="000"/>
					<td>Port</td>
					<td><html:text property="portNumber" styleId="portNumber" maxlength="3"
							   styleClass="textlabelsBoldForTextBox" value="${portNumber}" onchange="validatePort(this)"/></td>
					<td>Tax Exemption</td>
					<td><html:text property="taxExempt" styleId="taxExempt" 
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.taxExempt}" maxlength="11"/></td>
					<td>Federal Id</td>
					<td><html:text property="federalId" styleId="federalId" 
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.federalId}" maxlength="14"/></td>
				</tr>
				<tr class="tableHeadingNew"><td colspan="6">Notify Party Details</td></tr>
				<tr class="textlabelsBold">
					<td>Notify Party</td>
					<td><html:text property="consigneeNotifyParty" styleId="consigneeNotifyParty" 
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.notifyParty}" maxlength="40"/></td>
					<td>Address </td>
					<td><html:textarea property="notifyPartyAddress" styleId="notifyPartyAddress" onkeydown="maxLength(this,150)" 
                                                style="text-transform:uppercase" styleClass="textlabelsBoldForTextBox" cols="50" rows="3" value="${TRADINGPARTNER.notifyPartyAddress}"/>
                                            
                                        </td>
					<td>Postal Code</td>
					<td><html:text property="notifyPartyPostalCode" styleId="notifyPartyPostalCode"  maxlength="20"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.notifyPartyPostalCode}"/></td>
				</tr>
				<tr class="textlabelsBold">
					<td>City</td>
					<td><html:text property="notifyPartyCity" styleId="notifyPartyCity"  maxlength="30"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.notifyPartyCity}"/></td>
					<td>State</td>
					<td><html:text property="notifyPartyState" styleId="notifyPartyState"  maxlength="2"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.notifyPartyState}"/></td>
					<td>Country</td>
					<td><html:text property="notifyPartyCountry" styleId="notifyPartyCountry"  maxlength="30"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.notifyPartyCountry}"/></td>
				</tr>
				<tr>
					<td colspan="6" align="center"><input type="button" onclick="saveConsigneeInfo()" value="Save" class="buttonStyleNew"/></td>
				</tr>
			</table>
			<html:hidden property="tradingPartnerId" value="${accountNo}"/>
			<html:hidden property="buttonValue"/>
		</html:form>
		<script type="text/javascript">
			function saveConsigneeInfo(){
				document.tradingPartnerForm.buttonValue.value="saveConsigneeInfo";
				document.tradingPartnerForm.submit();
			}

			function validatePort(obj){
				if(isNaN(obj.value)){
					alert("Please enter the port in number only")
				}
			}
			function validateLimit(obj){
				if(obj.value.length>150){
					obj.value = obj.value.substr(0, 150);
					return false;
				}
			}
                         function maxLength(field,maxChars){
                            if(event.keyCode != 8){
                                if(field.value.length >= maxChars) {
                                    event.returnValue=false;
                                    return false;
                                }
                            }
                        }
		</script>
            <c:if test="${view == '3' || not empty disableTabBasedOnRole}">
                <script type="text/javascript">
                        disablePage(document.getElementById("consigneeInfo"));
                </script>
            </c:if>
	</body>
</html>

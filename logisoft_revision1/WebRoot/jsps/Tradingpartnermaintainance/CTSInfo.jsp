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
				<tr class="tableHeadingNew"><td colspan="6">CTS Information</td></tr>
				<tr class="textlabelsBold">
					<td>CTS UID</td>
					<td><html:text property="ctsUID" styleId="ctsUID" maxlength="3"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.ctsUID}" /></td>
					<td>Line MarkUp</td>
					<td><html:text property="lineMarkUp" styleId="lineMarkUp" onkeyup="allowOnlyWholeNumbers(this);"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.lineMarkUp}" maxlength="10"/></td>
					<td>Fuel MarkUp</td>
					<td><html:text property="fuelMarkUp" styleId="fuelMarkUp" onkeyup="allowOnlyWholeNumbers(this);"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.fuelMarkUp}" maxlength="14"/></td>
				</tr>
				<tr class="textlabelsBold">
					<td>Flat Fee</td>
					<td><html:text property="flatFee" styleId="flatFee"  maxlength="20" onkeyup="allowOnlyWholeNumbers(this);"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.flatFee}"/></td>
					<td>Minimum Amount</td>
					<td><html:text property="minAmount" styleId="minAmount"  maxlength="20" onkeyup="allowOnlyWholeNumbers(this);"
							   styleClass="textlabelsBoldForTextBox" value="${TRADINGPARTNER.minAmount}"/></td>
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
				document.tradingPartnerForm.buttonValue.value="saveCtsInfo";
				document.tradingPartnerForm.submit();
			}

			function allowOnlyWholeNumbers(obj){
                            if(!/^\d*(\.\d{0,2})?$/.test(obj.value)){
                                obj.value= "";
                                return false;
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

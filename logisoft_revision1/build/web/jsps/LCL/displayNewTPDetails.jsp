<%-- 
    Document   : displayNewTPDetails
    Created on : 13 Aug, 2013, 1:06:29 PM
    Author     : LogiwareInc
--%>

<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<%@include file="/jsps/includes/baseResources.jsp" %>
<script type="text/javascript">
    function submitButton(){
        var moduleName=$('#buttonValue').val();
         if(moduleName==="LCL_IMPORT_SHIPPER" || moduleName==="LCL_IMPORT_CONSIGNEE" || moduleName==="LCL_IMPORT_NOTIFY" ||  moduleName==="LCL_IMPORT_NOTIFY2"){
            parent.setNewShipperInfoDetails($('#accountName').val(),$('#acctNo').val(),moduleName);
        }
        else if(moduleName==="LCL_IMP_QUOTE_SHIPPER" || moduleName==="LCL_IMP_QUOTE_CONSIGNEE" || moduleName==="LCL_IMP_QUOTE_NOTIFY" ||  moduleName==="LCL_IMP_QUOTE_NOTIFY2"){
             parent.setNewTPInfoDetails($('#accountName').val(),$('#acctNo').val(),moduleName);
        }
        parent.$.colorbox.close();
    }
</script>
<body style="background:#ffffff">
    <cong:form id="tradingPartnerForm" name="tradingPartnerForm" action="/lclNewTPDetails">
        <table style="width:98%;border: 1px solid #dcdcdc;" border="0" >
            <input type="hidden" id="accountName" name="accountName" value="${tradingPartner.accountName}"/>
            <input type="hidden" id="buttonValue" name="buttonValue" value="${tradingPartnerForm.buttonValue}"/>
            <input type="hidden" id="acctNo" name="acctNo" value="${tradingPartner.accountno}"/>
            <tr class="tableHeadingNew">
                <td colspan="2">Trading Partner</td>
            </tr>
            <tr><td style="font-weight: bold;font-size: 16px;color:green">${tradingPartner.accountno}</td>
                <td style="font-weight: bold;font-size: 16px;color:green">${tradingPartner.accountName}</td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td></td>
                <td><div class="button-style1"  onclick="submitButton()">Ok</div></td></tr>
        </table>
    </cong:form></body>


<%-- 
    Document   : fclDoorDeliveryInfo
    Created on : Jan 11, 2016, 4:51:36 PM
    Author     : User
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/resources.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <html>
        <head>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <link rel="alternate stylesheet" type="text/css" media="all" href="${path}/css/cal/calendar-win2k-cold-2.css"  title="win2k-cold-2" />
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/fclDoorDelivery.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <%@include file="../../jsps/preloader.jsp"%>
    </head>
    <body class="whitebackgrnd">
        <%@include file="../../../../jsps/preloader.jsp"%>
        <html:form action="fclDoorDeliveryInfo.do" name="fclDoorDeliveryInfoform" type="com.gp.cvst.logisoft.struts.form.FclDoorDeliveryInfoForm" >
            <input type="hidden" id="saveFlag" name="saveFlag" value="${saveFlag}"/>
            <html:hidden property="methodName" styleId="methodName"/>
            <html:hidden property="fileNumber" styleId="fileNumber" value="${fclDoorDeliveryInfoform.fileNumber}"/>
            <html:hidden property="bolId" styleId="bolId" value="${fclDoorDeliveryInfoform.bolId}"/>
            <html:hidden property="zip" styleId="zip" value="${fclDoorDeliveryInfoform.zip}"/>

            <table width="100%" border="0" cellpadding="1" cellspacing="8" id="records" class="tableBorderNew">
                <tr>
                    <td class="textlabelsBold" align="right" valign="middle">Delivery Date&nbsp;&nbsp;</td>
                    <td>
                <fmt:formatDate pattern="MM/dd/yyyy HH:mm a" var="deliveryDate" value="${fclDoorDeliveryInfoform.deliveryDate}" />
                <html:text styleClass="textlabelsBoldForTextBox" property="deliveryDate"
                           value="${fclDoorDeliveryInfoform.deliveryDate}" styleId="txtdoorDeliveryCal" onchange="validateDateTime('true',this)"
                           size="20" />
                <img src="${path}/img/CalendarIco.gif" alt="cal" id="doorDeliveryCal" name="cal1"
                     onmousedown="insertDateFromCalendar(this.id, 9);"/>
            </td>
            <td class="textlabelsBold" align="right" valign="middle">Free Date&nbsp;&nbsp;</td>
            <td>
            <fmt:formatDate pattern="MM/dd/yyyy" var="freeDate" value="${fclDoorDeliveryInfoform.freeDate}" />
            <html:text styleClass="textlabelsBoldForTextBox " property="freeDate" style="vertical-align: middle"
                       value="${fclDoorDeliveryInfoform.freeDate}" styleId="txtfreeDateCal" onchange="validateDate('true',this)"
                       size="20" />
            <input type="hidden" id="doorDelivery" value=""/>
            <img src="${path}/img/CalendarIco.gif" alt="cal" id="freeDateCal"
                 onmousedown="insertDateFromCalendar(this.id, 9);"  />
        </td>   
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">Delivery To &nbsp;&nbsp;</td>
        <td>
            <table border="0">
                <tr class="textlabelsBold">
                    <td id="existingDoorDeliveryTo" align="left">
                        <input  class="textlabelsBoldForTextBox" name="deliveryTo" id="deliveryTo" maxlength="50"
                                value="${fclDoorDeliveryInfoform.deliveryTo}"  size="20"  style="text-transform: uppercase"/>
                        <input id="deliveryTo_check" type="hidden" value="${fclDoorDeliveryInfoform.deliveryTo}" />
                        <div id="deliveryTo_choices"  style="display:none;" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocompleteWithFormClear("deliveryTo", "deliveryTo_choices", "deliveryToNumber", "deliveryTo_check",
                                    "${path}/actions/tradingPartner.jsp?tabName=doorDelivery&from=1", "checkDisableAcctForDelivery()", "onDeliveryToBlur();");
                        </script>
                        <html:text property="deliveryToAcctNo"  maxlength="10" readonly="true"
                                   styleId="deliveryToNumber"  size="22" styleClass="BackgrndColorForTextBox" 
                                   value="${fclDoorDeliveryInfoform.deliveryToAcctNo}" />
                    </td>
                    <td id="newDeliveryTo" align="left">
                        <html:text property="deliveryToDup" styleId="deliveryToDup"  size="22"
                                   value="${fclDoorDeliveryInfoform.deliveryToDup}" styleClass="textlabelsBoldForTextBox"
                                   style="text-transform: uppercase"  maxlength="50" />

                    </td> 
                    <td align="left">
                        <html:checkbox property="manualDeliveryTo" styleId="manualDeliveryTo" name="fclDoorDeliveryInfoform" 
                                       onclick="disableDeliveryToDojo()" title="TP Not Listed"/>
                    </td>
                </tr>               
            </table>
        </td>
        <td class="textlabelsBold" align="right">Email</td>
        <td>
            <html:text property="deliveryEmail" 
                       styleId="deliveryEmail"  size="22" styleClass="textlabelsBoldForTextBox"
                       value="${fclDoorDeliveryInfoform.deliveryEmail}" onblur="validateEmail(this);"/>
        </td>
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">Address</td>
        <td>
            <html:textarea property="deliveryAddress" styleId="deliveryAddress" cols="30" rows="3" value="${fclDoorDeliveryInfoform.deliveryAddress}" style="text-transform: uppercase"/>
        </td>
        <td class="textlabelsBold" align="right">Contact Name</td>
        <td>
            <html:text property="deliveryContactName" styleId="deliveryContactName" style="text-transform: uppercase"
                       value="${fclDoorDeliveryInfoform.deliveryContactName}" styleClass="textlabelsBoldForTextBox"/>
        </td>
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">City</td>
        <td>
            <html:text property="deliveryCity" styleId="deliveryCity" value="${fclDoorDeliveryInfoform.deliveryCity}" style="width:100px;text-transform: uppercase" styleClass="textlabelsBoldForTextBox"/>
            <span class="textlabelsBold">&nbsp;&nbsp;State</span>
            <html:text property="deliveryState" styleId="deliveryState" value="${fclDoorDeliveryInfoform.deliveryState}" style="width:100px;text-transform: uppercase" styleClass="textlabelsBoldForTextBox"/>
        </td>
        <td class="textlabelsBold" align="right">Phone</td>
        <td>
            <html:text property="deliveryPhone" styleId="deliveryPhone" value="${fclDoorDeliveryInfoform.deliveryPhone}" styleClass="textlabelsBoldForTextBox" onchange="validateNumeric(this,'deliveryPhone');"/>
        </td>
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">Zip</td>
        <td>
            <html:text property="deliveryZip" styleId="deliveryZip" value="${fclDoorDeliveryInfoform.deliveryZip}" style="width:100px" styleClass="textlabelsBoldForTextBox" onchange="validateNumeric(this,'deliveryZip');"/>  
        </td>
       <td class="textlabelsBold" align="right">Fax</td>
        <td>
            <html:text property="deliveryFax" styleId="deliveryFax" value="${fclDoorDeliveryInfoform.deliveryFax}" styleClass="textlabelsBoldForTextBox" onchange="validateNumeric(this,'deliveryFax');"/>
        </td>
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">Local Delivery or Transfer By&nbsp;&nbsp;</td>
        <td>
            <html:text property="localDeliveryOrTransferBy" styleClass="textlabelsBoldForTextBox" styleId="localDeliveryOrTransferBy" maxlength="20" style="text-transform: uppercase"
                       size="22" value="${fclDoorDeliveryInfoform.localDeliveryOrTransferBy}" ></html:text>
            </td>

            <td align="right" class="textlabelsBold">Billing &nbsp;&nbsp;</td>
            <td class="textlabelsBold">
            <html:radio  property="billing" value="P" styleId="billingPrepaid" name="fclDoorDeliveryInfoform"/>P
            <html:radio  property="billing" value="C" styleId="billingCollector" name="fclDoorDeliveryInfoform"/>C
            <html:radio  property="billing" value="T" styleId="billingThirdParty" name="fclDoorDeliveryInfoform"/>T
        </td>  
    </tr>
    <tr>
        <td class="textlabelsBold" align="right">PO#</td>
        <td>
            <html:text property="po" styleId="po" value="${fclDoorDeliveryInfoform.po}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase"/>  
        </td>
       <td class="textlabelsBold" align="right">Reference Numbers</td>
        <td>
            <html:text property="referenceNumbers" styleId="referenceNumbers" value="${fclDoorDeliveryInfoform.referenceNumbers}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <input type="button" value="Save" id="save"
                   class="buttonStyleNew" onclick="submitForm();"/>
        </td>
    </tr>
</table>
</html:form>
</body>
</html>

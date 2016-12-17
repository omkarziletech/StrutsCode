<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="/WEB-INF/c-1_0-rt.tld" %>
<%@include file="/taglib.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:table>
    <cong:tr>
        <cong:td width="35%" styleClass="textlabelsBoldforlcl">SCAC Code</cong:td>
        <cong:td width="25%"><cong:text name="scacCode" id="scacCode" styleClass="textlabelsBoldForTextBox" value="${scac}" maxlength="4" style="text-transform:uppercase"/>
            <c:if test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                <input type="checkbox" id="newScacCode" title="SCAC not listed" onclick="setMaxLenght()"/>   
            </c:if>
            <img alt="remarks"  align="middle" id="scacCodeList"  src="/logisoft/img/icons/display.gif" title="Lookup By Company Name" onclick="getScacCodeList('${path}')"/>
        </cong:td>
    </cong:tr>
    <c:if test="${lclPickupInfoForm.moduleName eq 'Exports'}">
        <cong:tr>
            <cong:td width="35%" styleClass="textlabelsBoldforlcl">Vendor</cong:td>
            <cong:td width="25%">

                <cong:autocompletor name="costVendorAcct" id="costVendorAcct" template="tradingPartner" 
                                    query="VENDOR" styleClass="textlabelsBoldForTextBox textLCLuppercase"
                                    fields="costVendorNo,acctType,NULL,NULL,NULL,NULL,NULL,costDisabled,NULL,NULL,NULL,NULL,NULL" 
                                    width="500" scrollHeight="200" container="NULL" position="left"  shouldMatch="true" callback="acctTypeCheck()" value="${lclPickupInfoForm.costVendorAcct}"/>
            </cong:td><cong:td> 
                <input type="text" name="costVendorNo"  id="costVendorNo" style="width:80px;" value="${lclPickupInfoForm.costVendorNo}"class="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true"/>
                <input type="hidden" name="acctType" id="acctType"/>
                <input type="hidden" name="costDisabled" id="costDisabled"/>
            </cong:td>
        </cong:tr>
    </c:if>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Pickup Charge/Sell</cong:td>
        <cong:td width="25%">
            <c:choose><c:when test="${empty pickupCharge}">
                    <c:set var="charge" value="textlabelsBoldForTextBox"></c:set>
                    <c:set var="chargeDisable" value="false"></c:set>
                </c:when>
                <c:otherwise>
                    <c:set var="charge" value="textlabelsBoldForTextBoxDisabledLook"></c:set>
                    <c:set var="chargeDisable" value="true"></c:set>
                </c:otherwise>
            </c:choose>
            <cong:text name="chargeAmount" id="chargeAmount" readOnly="${chargeDisable}" value="${pickupCharge}" styleClass="${charge}" style="text-transform: uppercase" onkeyup="checkForNumberAndDecimal(this)" container="NULL"/>
        </cong:td><cong:hidden name="duplicateChargeAmount" id="duplicateChargeAmount" value="${pickupCharge}"/>
        <cong:td width="34%">
            <c:choose>                                                                               
                <c:when test="${lclPickupInfoForm.moduleName eq 'Imports' && empty closedBy  && not empty pickupCharge && (billToParty eq 'A' && empty invoiceStatus)}">
                    <span valign="middle" id="chargeImage" title="Change Pickup Charge">
                        <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearCharge" onclick="editCharge('Are You sure you want to change the PickUp charge?');"  />
                    </span>
                </c:when>
                <c:when test="${lclPickupInfoForm.moduleName eq 'Imports' && empty closedBy  && not empty pickupCharge && (billToParty ne 'A' && fileNumberStatus ne 'M')}">
                    <span valign="middle" id="chargeImage" title="Change Pickup Charge">
                        <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearCharge" onclick="editCharge('Are You sure you want to change the PickUp charge?');"  />
                    </span>
                </c:when>
                <c:when test="${lclPickupInfoForm.moduleName eq 'Exports' && not empty pickupCharge && fileNumberStatus ne 'M'}">
                    <span valign="middle" id="chargeImage" title="Change Pickup Charge">
                        <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearCharge" onclick="editCharge('Are You sure you want to change the PickUp charge?');"  />
                    </span>
                </c:when>
                <c:otherwise></c:otherwise>
            </c:choose>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Pickup Cost</cong:td>
        <cong:td>
            <c:choose>
                <c:when test="${lclPickupInfoForm.moduleName eq 'Exports'}">
                    <c:set var="charge" value="textlabelsBoldForTextBox"></c:set>
                    <c:set var="chargeDisable" value="false"></c:set>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${empty closedBy}">
                            <c:choose>
                                <c:when test="${not empty pickupCost && (transType eq 'AP' || transType eq 'IP')}">
                                    <c:set var="cost" value="textlabelsBoldForTextBoxDisabledLook"></c:set>
                                    <c:set var="costDisable" value="true"></c:set>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="cost" value="textlabelsBoldForTextBox"></c:set>
                                    <c:set var="costDisable" value="false"></c:set>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:set var="cost" value="textlabelsBoldForTextBoxDisabledLook"></c:set>
                            <c:set var="costDisable" value="true"></c:set>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
            <cong:text name="pickupCost" id="pickupCost" value="${pickupCost}" styleClass="${cost}" onkeyup="checkForNumberAndDecimal(this)" container="NULL" onchange="enableVendor();"
                       readOnly="${costDisable}" />
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Fax</cong:td>
        <cong:td>
            <cong:text name="fax1" id="fax1" styleClass="textlabelsBoldForTextBox" value="${fax1}" style="text-transform: uppercase" container="NULL" onkeyup="checkForNumber(this);" maxlength="10"/>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Contact Email</cong:td>
        <cong:td>
            <cong:text name="email1" id="email1" styleClass="textlabelsBoldForTextBox" value="${email1}" container="NULL" maxlength="100"/>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Contact Name</cong:td>
        <cong:td>
            <cong:text name="contactName" id="contactName" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${contactName}" container="NULL" maxlength="150"/>
        </cong:td>
    </cong:tr>

    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
        <cong:td>
            <cong:text name="phone1" id="phone1" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${phone1}" container="NULL" onkeyup="checkForNumber(this);" maxlength="10"/>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <c:choose><c:when test="${lclPickupInfoForm.moduleName ne 'Imports'}">
                <cong:td width="8%" styleClass="textlabelsBoldforlcl">Shipper Hours</cong:td>
            </c:when>
            <c:otherwise><cong:td width="8%" styleClass="textlabelsBoldforlcl">Receiving Hours</cong:td></c:otherwise>
        </c:choose>
        <cong:td>
            <cong:text name="pickupHours" id="pickupHours" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${pickupHours}" maxlength="50" container="NULL"/>
        </cong:td>
    </cong:tr>
    <c:if test="${lclPickupInfoForm.moduleName ne 'Imports'}">
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">CutOff Note</cong:td>
            <cong:td>
                <cong:text name="pickupReadyNote" id="pickupReadyNote" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${pickupReadyNote}" maxlength="150" container="NULL"/>
            </cong:td>
        </cong:tr>
    </c:if>
</cong:table>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<style>
    .val{
        font-weight: bold;
    }
</style>
<c:set var="fileNumberId" value="${fileNumberId}"/>
<div style="width:100%; float:left;">
    <cong:table styleClass="tableHeadingNew" style="width:100%" align="center">
        <cong:tr>
            <cong:td align="center">
                Truck Pickup List
            </cong:td>
            <c:choose>
                <c:when test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                    <c:if test="${(fileNumberStatus ne 'M' && empty invoiceStatus && transType ne 'AP') or 
                                  (fileNumberStatus eq 'M' && empty invoiceStatus && transType ne 'AP')}">
                        <cong:td styleClass="floatLeft" width="8%"><span class="button-style1" onclick="savePickupCharge();">Save</span></cong:td>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <cong:td styleClass="floatLeft" width="8%"><span class="button-style1" onclick="savePickupCharge();">Save</span></cong:td>   
                </c:otherwise>
            </c:choose>
        </cong:tr>
    </cong:table>
    <cong:table styleClass="floatLeft" width="25%">
        <cong:tr>
            <cong:td styleClass="val">Origin:</cong:td>
            <cong:td>${lclSession.xmlObjMap.origin.city},${lclSession.xmlObjMap.origin.state},${lclSession.xmlObjMap.origin.zipCode}</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="val">Destination:</cong:td>
            <cong:td>${lclSession.xmlObjMap.destination.city},${lclSession.xmlObjMap.destination.state},${lclSession.xmlObjMap.destination.zipCode}</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="val">Distance:</cong:td>
            <cong:td>${lclSession.xmlObjMap.rates.miles}</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="val">Class:</cong:td>
            <cong:td>${lclSession.xmlObjMap.classes.class1}</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="val">Weight:</cong:td>
            <cong:td>${lclSession.xmlObjMap.weights.weight}</cong:td>
        </cong:tr>
    </cong:table>

    <table width="100%" class="dataTable">
        <thead>
            <tr>
                <th>Action</th>
                <th>Carrier Name</th>
                <th>SCAC Code</th>
                <th>Direct/InterLine</th>
                <th>Days</th>
                <th>Type</th>
                <th>Net Charge</th>
                <th>Fuel Charge</th>
                <th>Extra Charge</th>
                <th>Final Charge</th>
            </tr>
        </thead>
         <c:if test="${empty lclSession.carrierList && lclPickupInfoForm.moduleName eq 'Exports'}">
             <tbody><tr><td></td>
                        <td></td>
                     <td colspan="2">
             <a style="color: red">Vendor Error. </a>
         </td>
         <td></td>
         <td></td>
         <td></td>
         <td></td>
         <td></td>
         <td></td>
         <td></td>
                 </tr></tbody>
                    </c:if>
        <c:forEach items="${lclSession.carrierList}" var="carrier">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tbody>
                <tr class="${zebra}">
                    <td>
                        <c:choose>
                            <c:when test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                                <c:if test="${(fileNumberStatus ne 'M' && empty invoiceStatus && transType ne 'AP') or 
                                              (fileNumberStatus eq 'M' && empty invoiceStatus && transType ne 'AP')}">
                                      <input type="${doorDellStatus eq 'true' ? 'hidden' : 'radio'}" name="carrierRadio" id="carrierRadio"  value="${carrier.scac}==${carrier.finalcharge}==${carrier.initialcharges}">
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.scac}==${carrier.finalcharge}==${carrier.initialcharges}">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${carrier.name}</td>
                   
                    <td>${carrier.scac}</td>
                    <td>${carrier.relation}</td>
                    <td>${carrier.days}</td>
                    <td>${carrier.service.name}</td>
                    <td>${carrier.initialcharges}</td>
                    <td>${carrier.fuelsurcharge}</td>
                    <td>${carrier.additionalsc.total}</td>
                    <td>${carrier.finalcharge}</td>
                </tr>
            </tbody>
        </c:forEach>
    </table>
</div>
<script type="text/javascript">
    function savePickupCharge() {
        var fileNumberId = '${fileNumberId}';
        if ($("input:radio[name='carrierRadio']:checked").is(':checked')) {
            var val = $("input:radio[name='carrierRadio']:checked").val();
            var fax = parent.$('#fax1').val();
            var spAcct = parent.$('#spAcct').val();
            var email1 = parent.$('#email1').val();
            var contactName = parent.$('#contactName').val();
            var phone1 = parent.$('#phone1').val();
            var pickupHours = parent.$('#pickupHours').val();
            var pickupReadyNote = parent.$('#pickupReadyNote').val();
            var pickupReadyDate = parent.$('#pickupReadyDate').val();
            var fromZip = parent.$('#cityStateZip').val();
            var toZip = parent.$('#whseZip').val();
            var scac = val.split("==")[0];
            var pickupCharge = val.split("==")[1];
            var moduleName = parent.$('#moduleName').val();
            var chargeCode = moduleName === 'Exports' ? "INLAND" : "DOORDEL";
        if (parent.$('#apGlMappingFlagId').val() === "N") {
            $.prompt('No Gl Mapping found for CostCode ---> ' + chargeCode);
            return false;
        } else if (parent.$('#arGlMappingFlagId').val() === "N") {
            $.prompt('No Gl Mapping found for ChargeCode --->' + chargeCode);
            return false;
        }
          showLoading();
            $.ajax({
                url: "lclPickupInfo.do?methodName=addPickupChargeToAcct&fileNumberId=" + fileNumberId + "&scac=" + scac + "&pickupCharge=" + pickupCharge + "&fax=" + fax + "&contactName=" + contactName + "&phone1=" + phone1 + "&pickupHours=" + pickupHours + "&pickupReadyNote=" + pickupReadyNote + "&email1=" + email1 + "&spAcct=" + spAcct + "&toZip=" + toZip + "&pickupReadyDate=" + pickupReadyDate + "&fromZip=" + fromZip,
                success: function (data) {
                   $('#pickupCharge').html(data);
                 $('#pickupCharge', window.parent.document).html(data);
                    parent.$('#chargeAmount').addClass("textlabelsBoldForTextBoxDisabledLook");
                    parent.$('#chargeAmount').attr("readonly", true);
                    parent.$('#costVendorAcct').removeClass('textlabelsBoldForTextBoxDisabledLook');
                    parent.$('#costVendorAcct').removeAttr("readonly");
                    hideProgressBar();
                    parent.$.colorbox.close();
                }
            });
        } else {
            $.prompt('Please select a carrier');
        }
    }
</script>
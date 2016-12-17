<%@include file="init.jsp" %>
<%@include file="/taglib.jsp"%>
<style>
    .val{
        font-weight: bold;
    }
</style>
<c:set var="fileNumberId" value="${fileNumberId}"/>
<cong:div style="width:100%; float:left;">
    <cong:table styleClass="tableHeadingNew" style="width:100%" align="center">
        <cong:tr>
            <cong:td align="center">
                Truck Pickup List
            </cong:td>
            <cong:td styleClass="floatRight" width="15%"><span class="button-style1" onclick="savePickupCharge();">Save</span></cong:td>
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

    <cong:table width="100%" border="1" styleClass="tableHeading2-small" style="border-collapse: collapse; border: 1px solid #dcdcdc">
        <cong:tr>
            <cong:th>Action</cong:th>
            <cong:th>Carrier Name</cong:th>
            <cong:th>SCAC Code</cong:th>
            <cong:th>Direct/InterLine</cong:th>
            <cong:th>Days</cong:th>
            <cong:th>Type</cong:th>
            <cong:th>Net Charge</cong:th>
            <cong:th>Fuel Charge</cong:th>
            <cong:th>Extra Charge</cong:th>
            <cong:th>Final Charge</cong:th>
        </cong:tr>
        <c:forEach items="${lclSession.carrierList}" var="carrier">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <cong:tr styleClass="${zebra}">
                <cong:td>
                    <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.scac}==${carrier.finalcharge}">
                </cong:td>
                <cong:td>${carrier.name}</cong:td>
                <cong:td>${carrier.scac}</cong:td>
                <cong:td>${carrier.relation}</cong:td>
                <cong:td>${carrier.days}</cong:td>
                <cong:td>${carrier.service.name}</cong:td>
                <cong:td>${carrier.initialcharges}</cong:td>
                <cong:td>${carrier.fuelsurcharge}</cong:td>
                <cong:td>${carrier.additionalsc.total}</cong:td>
                <cong:td>${carrier.finalcharge}</cong:td>
            </cong:tr>
        </c:forEach>
    </cong:table>
</cong:div>
<script type="text/javascript">
    function savePickupCharge(){
        var fileNumberId='${fileNumberId}'
        if($("input:radio[name='carrierRadio']:checked").is(':checked')){
            var val=$("input:radio[name='carrierRadio']:checked").val();
            var scac=val.split("==")[0];
            var pickupCharge=val.split("==")[1];
            showProgressBar();
            $.ajax({
                url: "lclBlPickupInfo.do?methodName=addPickupChargeToAcct&fileNumberId="+fileNumberId+"&scac="+scac+"&pickupCharge="+pickupCharge,
                success: function(data) {
                    $('#pickupCharge').html(data);
                    $('#pickupCharge',window.parent.document).html(data);
                    hideProgressBar();
                    parent.$.colorbox.close();
                }
            });
        }else{
            congAlert('Please select a carrier');
        }
    }
    function congAlert(txt){
        $.prompt(txt);
    }
</script>
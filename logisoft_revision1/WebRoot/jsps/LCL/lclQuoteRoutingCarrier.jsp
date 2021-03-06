<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp"%>
<c:set var="fileNumberId" value="${fileNumberId}"/>
<cong:div style="width:100%; ">
    <cong:table styleClass="tableHeadingNew" style="width:100%" align="center">
        <cong:tr>
            <cong:td align="center">
                Truck Pickup List
            </cong:td>
            <c:if test="${lclQuoteMultiRateForm.moduleId=='R'}">
                <cong:td width="7%">
                    <span class="button-style1" onclick="savePickupCharge('${index}');">Save</span>
                </cong:td>
            </c:if>
            <c:if test="${lclQuoteMultiRateForm.moduleId=='DS'}">
                <cong:td width="7%"><span class="button-style1" onclick="savePickupCharge('${index}');">Save</span>
                </cong:td>
            </c:if>
        </cong:tr>
    </cong:table>
    <table width="25%" class="textBoldforlcl">
        <cong:hidden name="moduleId" id="moduleId" value="${moduleId}"/>
        <tr>
            <td>Origin:</td>
            <td>${lclSession.xmlObjMap.origin.city},${lclSession.xmlObjMap.origin.state},${lclSession.xmlObjMap.origin.zipCode}</td>
        </tr>
        <tr>
            <td>Destination:</td>
            <td>${lclSession.xmlObjMap.destination.city},${lclSession.xmlObjMap.destination.state},${lclSession.xmlObjMap.destination.zipCode}</td>
        </tr>
        <tr>
            <td>Distance:</td>
            <td>${lclSession.xmlObjMap.rates.miles}</td>
        </tr>
        <tr>
            <td>Class:</td>
            <td>${lclSession.xmlObjMap.classes.class1}</td>
        </tr>
        <tr>
            <td>Weight:</td>
            <td>${lclSession.xmlObjMap.weights.weight}</td>
        </tr>
    </table>

    <table class="dataTable" style="border: 1px solid #dcdcdc">
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
        <tbody>
            <c:forEach items="${lclSession.carrierList}" var="carrier">
                <c:choose>
                    <c:when test="${zebra=='odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${zebra}">
                    <td>
                        <input type="radio" name="carrierRadio" id="carrierRadio" value="${carrier.scac}==${carrier.finalcharge}==${carrier.initialcharges}==${carrier.days}">
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
            </c:forEach>
        </tbody>
    </table>
</cong:div>
<script type="text/javascript">
    //    function savePickupCharge(index){
    //        var totalobj = "parent.document.lclQuoteMultiRateForm.hiddenTotalAmount"+index;
    //        var hiddenTotalValue = eval(totalobj).value;
    //        if($("input:radio[name='carrierRadio']:checked").is(':checked')){
    //            var moduleId=$('#moduleId').val();
    //            if(moduleId=='R'){
    //
    //                var val=$("input:radio[name='carrierRadio']:checked").val();
    //                var scac=val.split("==")[0];
    //                var pickupCharge=val.split("==")[1];
    //                var pickupCost=val.split("==")[2];
    //                showProgressBar();
    //                $.ajax({
    //                    url: "lclQuotemultiRate.do?methodName=setCarrier&index="+index+"&pickupCharge="+pickupCharge+"&scac="+scac+"&pickupCost="+pickupCost,
    //                    success: function(data) {
    //                        $('#pickupCharge').html(data);
    //                        $('#pickupCost').html(data);
    //                        $('#pickupCharge',window.parent.document).html(data);
    //                        hideProgressBar();
    //                        var row = parent.document.getElementById("routingtable").rows.item(parseInt(index)+1);
    //                        var col = row.cells.item(6);
    //                       var prevstr = 0.00;
    //                        if(col.innerHTML!="")
    //                        {
    //                            var prepickupstr = col.innerHTML;
    //                            var beginIndex = prepickupstr.indexOf('$')+1;
    //                            var endIndex = prepickupstr.indexOf('(');
    //                            prevstr = Number(prepickupstr.substring(beginIndex, endIndex));
    //                        }
    //                        col.innerHTML = "$"+pickupCharge+"("+scac+")";
    //                        col = row.cells.item(8);
    //                        var total = Number(hiddenTotalValue)+Number(pickupCharge);
    //                        total = total - prevstr;
    //                        col.innerHTML = total.toFixed(2);
    //                        eval(totalobj).value = total.toFixed(2);
    //                        setColorForCheapestTotal();
    //                        parent.$.colorbox.close();
    //                    }
    //                });}else if(moduleId='DS'){
    //                var val=$("input:radio[name='carrierRadio']:checked").val();
    //                var scac=val.split("==")[0];
    //                var pickupCharge=val.split("==")[1];
    //                var row = parent.document.getElementById("directsailingtable").rows.item(parseInt(index)+1);
    //                var col = row.cells.item(4);
    //                col.innerHTML = "$"+pickupCharge+"("+scac+")";
    //                parent.$.colorbox.close();
    //            }
    //        }else{
    //            congAlert('Please select a carrier');
    //        }
    //    }

    function savePickupCharge(index){
        var totalobj = "parent.document.lclQuoteMultiRateForm.hiddenTotalAmount"+index;
        var hiddenTotalValue = eval(totalobj).value;
        if($("input:radio[name='carrierRadio']:checked").is(':checked')){
            var val=$("input:radio[name='carrierRadio']:checked").val();
            var scac=val.split("==")[0];
            var pickupCharge=val.split("==")[1];
            var pickupCost=val.split("==")[2];
            var days = val.split("==")[3];
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "setCarrier",
                    param1: index,
                    param2: pickupCharge,
                    param3: scac,
                    param4: pickupCost,
                    dataType: "json",
                    request:"true"
                },
                async: false,
                preloading: true,
                success: function(data) {
                    if(data)  {
                        var row = parent.document.getElementById("routingtable").rows.item(parseInt(index)+1);
                        var col = row.cells.item(7);
                        var prevstr = 0.00;
                        if(col.innerHTML!="")
                        {
                            var prepickupstr = col.innerHTML;
                            var beginIndex = prepickupstr.indexOf('$')+1;
                            var endIndex = prepickupstr.indexOf('(');
                            prevstr = Number(prepickupstr.substring(beginIndex, endIndex));
                        }
                        col.innerHTML = "$"+pickupCharge+"("+scac+")";
                        col = row.cells.item(9);
                        var total = Number(hiddenTotalValue)+Number(pickupCharge);
                        total = total - prevstr;
                        col.innerHTML = total.toFixed(2);
                        eval(totalobj).value = total.toFixed(2);
                        setColorForCheapestTotal();
                        col = row.cells.item(4);
                        col.innerHTML = days;
                        parent.$.colorbox.close();
                        
                    }
                }
            });
        }else{
            $.prompt('Please select a carrier');
        }
    }
    function setColorForCheapestTotal()
    {
        var routingtableobj = parent.document.getElementById("routingtable");
        var row = routingtableobj.rows.item(0);
        var cheapestTotalCharges = 0.00;
        for (var i = 1; i<=routingtableobj.rows.length-1; i++ ) {
            for (var j = 0; j<row.cells.length; j++ ) {
                var col = row.cells.item(j);
                if(col.innerHTML.toString().trim().toUpperCase().indexOf("TOTAL CHARGES")!=-1)
                {
                    if(routingtableobj.rows[i].cells[j].innerHTML!=null && routingtableobj.rows[i].cells[j].innerHTML.trim()!="")
                    {
                        routingtableobj.rows[i].cells[j].className = "";
                        if(i==1)
                        {
                            cheapestTotalCharges = Number(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }
                        else if(cheapestTotalCharges>Number(routingtableobj.rows[i].cells[j].innerHTML.trim()))
                        {
                            cheapestTotalCharges = Number(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }
                    }
                }
            }//end of j loop
        }//end of i loop
        for (var i = 1; i<=routingtableobj.rows.length-1; i++ ) {
            for (var j = 0; j<row.cells.length; j++ ) {
                var col = row.cells.item(j);
                if(col.innerHTML.toString().trim().toUpperCase().indexOf("TOTAL CHARGES")!=-1 && cheapestTotalCharges>0.00)
                {
                    if(routingtableobj.rows[i].cells[j].innerHTML!=null && routingtableobj.rows[i].cells[j].innerHTML.trim()!="")
                    {
                        if(cheapestTotalCharges==Number(routingtableobj.rows[i].cells[j].innerHTML.trim()))
                        {
                            routingtableobj.rows[i].cells[j].className = "routingTableRed";
                        }
                    }
                }

            }
        }
    }
</script>
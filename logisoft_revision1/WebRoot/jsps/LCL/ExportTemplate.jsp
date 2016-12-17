<c:forEach items="${orderedTemplateList}" var="order">
    <span class="orderKey" style="display: none;">${order.templateKey}</span>
    <c:set var="QUOTE" value="${order.templateKey eq 'QUOTE'  ? true : QUOTE}"/>
    <c:set var="BOOKING" value="${order.templateKey eq 'BOOKING' ? true : BOOKING}"/>
    <c:set var="BL" value="${order.templateKey eq 'BL' ? true : BL}"/>
    <c:set var="HAZ" value="${order.templateKey eq 'HAZ' ? true : HAZ}"/>
    <c:set var="PN" value="${order.templateKey eq 'PN' ? true : PN}"/>
    <c:set var="EDI" value="${order.templateKey eq 'EDI' ? true : EDI}"/>
    <c:set var="FILE_NUMBER" value="${order.templateKey eq 'FILE_NUMBER' ? true : FILE_NUMBER}"/>
    <c:set var="TR" value="${order.templateKey eq 'TR' ? true : TR}"/>
    <c:set var="STATUS" value="${order.templateKey eq 'STATUS' ? true : STATUS}"/>
    <c:set var="OSD" value="${order.templateKey eq 'OSD' ? true : OSD}"/>
    <c:set var="ERT" value="${order.templateKey eq 'ERT' ? true : ERT}"/>
    <c:set var="DOC" value="${order.templateKey eq 'DOC' ? true : DOC}"/>
    <c:set var="DATE_RECEIVED" value="${order.templateKey eq 'DATE_RECEIVED' ? true : DATE_RECEIVED}"/>
    <c:set var="PCS" value="${order.templateKey eq 'PCS' ? true : PCS}"/>
    <c:set var="CUBE" value="${order.templateKey eq 'CUBE' ? true : CUBE}"/>
    <c:set var="LOAD_LRD" value="${order.templateKey eq 'LOAD_LRD' ? true : LOAD_LRD}"/>
    <c:set var="WEIGHT" value="${order.templateKey eq 'WEIGHT' ? true : WEIGHT}"/>
    <c:set var="ORIGIN" value="${order.templateKey eq 'ORIGIN' ? true : ORIGIN}"/>
    <c:set var="POL" value="${order.templateKey eq 'POL' ? true : POL}"/>
    <c:set var="POD" value="${order.templateKey eq 'POD' ? true : POD}"/>
    <c:set var="INLAND_ETA" value="${order.templateKey eq 'INLAND_ETA' ? true : INLAND_ETA}"/>
    <c:set var="DESTINATION" value="${order.templateKey eq 'DESTINATION' ? true : DESTINATION}"/>
    <c:set var="SHIPPER" value="${order.templateKey eq 'SHIPPER' ? true : SHIPPER}"/>
    <c:set var="FORWARDER" value="${order.templateKey eq 'FORWARDER' ? true : FORWARDER}"/>
    <c:set var="CONSIGNEE" value="${order.templateKey eq 'CONSIGNEE' ? true : CONSIGNEE}"/>
    <c:set var="ACTION" value="${order.templateKey eq 'ACTION' ? true : ACTION}"/>
    <c:set var="BILL_TERMINAL" value="${order.templateKey eq 'BILL_TERMINAL' ? true : BILL_TERMINAL}"/>
    <c:set var="AES_BY" value="${order.templateKey eq 'AES_BY' ? true : AES_BY}"/>
    <c:set var="QUOTE_BY" value="${order.templateKey eq 'QUOTE_BY' ? true : QUOTE_BY}"/>
    <c:set var="BOOKED_BY" value="${order.templateKey eq 'BOOKED_BY' ? true : BOOKED_BY}"/>
    <c:set var="CONSOLIDATE" value="${order.templateKey eq 'CONSOLIDATE' ? true : CONSOLIDATE}"/>
    <c:set var="BOOKED_SAILDATE" value="${order.templateKey eq 'BOOKED_SAILDATE' ? true : BOOKED_SAILDATE}"/>
    <c:set var="RELAY_OVERRIDE" value="${order.templateKey eq 'RELAY_OVERRIDE' ? true : RELAY_OVERRIDE}"/>
    <c:set var="HOTCODE" value="${order.templateKey eq 'HOTCODE' ? true : HOTCODE}"/>
    <c:set var="LOAD_REMARKS" value="${order.templateKey eq 'LOAD_REMARKS' ? true : LOAD_REMARKS}"/>
    <c:set var="ORIGIN_LRD" value="${order.templateKey eq 'ORIGIN_LRD' ? true : ORIGIN_LRD}"/>
    <c:set var="CURRENT_LOCATION" value="${order.templateKey eq 'CURRENT_LOCATION' ? true : CURRENT_LOCATION}"/>
    <c:set var="LINE_LOCATION" value="${order.templateKey eq 'LINE_LOCATION' ? true : LINE_LOCATION}"/>
    <c:set var="BOOKED_VOYAGE" value="${order.templateKey eq 'BOOKED_VOYAGE' ? true : BOOKED_VOYAGE}"/>
    <c:set var="DISPOSITION" value="${order.templateKey eq 'DISPOSITION' ? true : LINE_LOCATION}"/>
    <c:set var="SOURCE" value="${order.templateKey eq 'SOURCE' ? true : SOURCE}"/>
    <c:set var="BRAND" value="${order.templateKey eq 'BRAND' ? true : BRAND}"/>
</c:forEach>               
<table width="auto" border="0" id="listofTemplate" style="margin-left:50px;">
    <tr><td colspan="8"><br></td></tr>
    <tr>
        <td>QUOTE:</td>
        <td><input type="checkbox" name="qu" id="QUOTE" class="checkbox" ${QUOTE ? 'checked':''} container="NULL"/></td>
        <td>BKG:</td>
        <td><input type="checkbox" name="bk" id="BOOKING" class="checkbox" ${BOOKING ? 'checked':''} container="NULL"/></td>
        <td>BL:</td>
        <td><input type="checkbox" name="bl" id="BL" class="checkbox" ${BL ? 'checked':''} container="NULL"/></td>
        <td>HZ:</td>
        <td><input type="checkbox" name="hz" id="HAZ" class="checkbox" ${HAZ ? 'checked':''} container="NULL"/></td>
        <td>PN:</td>
        <td><input type="checkbox" name="PN" id="PN" class="checkbox" ${PN ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>EDI:</td>
        <td><input type="checkbox" name="edi" id="EDI" class="checkbox" ${EDI ? 'checked':''} container="NULL"/></td>
        <td>FILE NO:</td>
        <td><input type="checkbox" name="fileNo" id="FILE_NUMBER" class="checkbox" ${FILE_NUMBER ? 'checked':''} container="NULL"/></td>
        <td>TR:</td>
        <td><input type="checkbox" name="tr" id="TR" class="checkbox" ${TR ? 'checked':''} container="NULL"/></td>
        <td>STATUS:</td>
        <td><input type="checkbox" name="status" id="STATUS" class="checkbox" ${STATUS ? 'checked':''} container="NULL"/></td>
        <td>OSD:</td>
        <td><input type="checkbox" name="OSD" id="OSD" class="checkbox"  ${OSD ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>DOC:</td>
        <td><input type="checkbox" name="doc" id="DOC" class="checkbox" ${DOC ? 'checked':''} container="NULL"/></td>
        <td>DATE RECEIVED:</td>
        <td><input type="checkbox" name="dateReceived" id="DATE_RECEIVED" ${DATE_RECEIVED ? 'checked':''} 
                   class="checkbox" container="NULL"/></td>
        <td>PCS:</td>
        <td><input type="checkbox" name="pcs" id="PCS" class="checkbox" ${PCS ? 'checked':''} container="NULL"/></td>
        <td>CUBE:</td>
        <td><input type="checkbox" name="cube" id="CUBE" class="checkbox" ${CUBE ? 'checked':''} container="NULL"/></td>
        <td>ERT:</td>
        <td><input type="checkbox" name="ERT" id="ERT" class="checkbox"  ${ERT ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>WEIGHT:</td>
        <td><input type="checkbox" name="weight" id="WEIGHT" class="checkbox" ${WEIGHT ? 'checked':''} container="NULL"/></td>
        <td>ORIGIN:</td>
        <td><input type="checkbox" name="origin" id="ORIGIN" class="checkbox" ${ORIGIN ? 'checked':''} container="NULL"/></td>
        <td>POL:</td>
        <td><input type="checkbox" name="pol" id="POL" class="checkbox" ${POL ? 'checked':''} container="NULL"/></td>
        <td>POD:</td>
        <td><input type="checkbox" name="pod" id="POD" class="checkbox" ${POD ? 'checked':''} container="NULL"/></td>
        <td>LOAD LRD:</td>
        <td><input type="checkbox" name="LOAD_LRD" id="LOAD_LRD" class="checkbox" ${LOAD_LRD ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>DESTINATION:</td>
        <td><input type="checkbox" name="destination" id="DESTINATION" ${DESTINATION ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>SHIPPER:</td>
        <td><input type="checkbox" name="shipper" id="SHIPPER" class="checkbox" ${SHIPPER ? 'checked':''} container="NULL"/></td>
        <td>FWD:</td>
        <td><input type="checkbox" name="fwd" id="FORWARDER" class="checkbox" ${FORWARDER ? 'checked':''} container="NULL"/></td>
        <td>CONSIGNEE:</td>
        <td><input type="checkbox" name="consignee" id="CONSIGNEE" ${CONSIGNEE ? 'checked':''} 
                   class="checkbox" container="NULL"/></td>
        <td>INLAND ETA:</td>
        <td><input type="checkbox" name="INLAND_ETA" id="INLAND_ETA" class="checkbox" ${INLAND_ETA ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>BILL TM:</td>
        <td><input type="checkbox" name="billTm" id="BILL_TERMINAL" ${BILL_TERMINAL ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>AES BY:</td>
        <td><input type="checkbox" name="aesBy" id="AES_BY" ${AES_BY ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>QUOTE BY:</td>
        <td><input type="checkbox" name="quoteBy" id="QUOTE_BY" ${QUOTE_BY ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>BOOKED BY:</td>
        <td><input type="checkbox" name="bookedBy" id="BOOKED_BY" ${BOOKED_BY ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>ACTION:</td>
        <td><input type="checkbox" name="ACTION" id="ACTION" class="checkbox" ${ACTION ? 'checked':''} container="NULL"/></td>
    </tr>
    <tr>
        <td>CONS:</td>
        <td><input type="checkbox" name="cons" id="CONSOLIDATE" class="checkbox" ${CONSOLIDATE ? 'checked':''} container="NULL"/></td>
        <td>BOOKED FOR SAIL DATE:</td>
        <td><input type="checkbox" name="bookedSaildate" id="BOOKED_SAILDATE" ${BOOKED_SAILDATE ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>RELAY OVERRIDE:</td>
        <td><input type="checkbox" name="relayOverride" id="RELAY_OVERRIDE" ${RELAY_OVERRIDE ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>HOT CODES:</td>
        <td><input type="checkbox" name="hotCodes" id="HOTCODE" ${HOTCODE ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>BOOKED VOYAGE:</td>
        <td><input type="checkbox" name="bookedVoyage" id="BOOKED_VOYAGE" class="checkbox" ${BOOKED_VOYAGE ? 'checked':''} container="NULL"/><td>
    </tr>
    <tr>
        <td>LOADING RMKS:</td>
        <td><input type="checkbox" name="loadingRemarks" id="LOAD_REMARKS" ${LOAD_REMARKS ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>ORIGIN LRD:</td>
        <td><input type="checkbox" name="originLrd" id="ORIGIN_LRD" ${ORIGIN_LRD ? 'checked':''} class="checkbox" container="NULL"/></td>
        <td>CURRENT LOCATION:</td>
        <td><input type="checkbox" name="currentLocation" id="CURRENT_LOCATION" class="checkbox" ${CURRENT_LOCATION ? 'checked':''}  container="NULL"/></td>
        <td>LINE LOCATION:</td>
        <td><input type="checkbox" name="lineLocation" id="LINE_LOCATION" class="checkbox" ${LINE_LOCATION ? 'checked':''}  container="NULL"/></td>
        <td>DISPOSITION:</td>
        <td><input type="checkbox" name="disposition" id="DISPOSITION" class="checkbox" ${DISPOSITION ? 'checked':''}  container="NULL"/></td>
    </tr> 
    <tr>
        <td>SOURCE:</td>
        <td><input type="checkbox" name="source" id="SOURCE" class="checkbox" ${SOURCE ? 'checked':''} container="NULL"/></td>
        <td>BRAND:</td>
        <td><input type="checkbox" name="brand" id="BRAND" class="checkbox" ${BRAND ? 'checked':''} container="NULL"/></td>
    </tr>
</table>
<div style="margin-top: 20px;margin-left:70px;height:auto;width:240px;
     overflow-y:auto;border-collapse: collapse; border: 2px solid #F8F8FF">
    <table class="dataTable">
        <thead>
            <tr>
                <th>
                    <b style="font-weight:bold">Order Of Items</b>
                </th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td id="templateOrderList"></td>
                <td>
                    <img src="${path}/images/icons/arrow/green_Up.png" width="20" height="20" align="middle" 
                         class="arrowAction" title="move up"  onclick="moveUpDown('up')"/>
                    <br/><br/>
                    <img src="${path}/images/icons/arrow/green_down.png" width="20" height="20" align="middle" 
                         class="arrowAction" title="move down" onclick="moveUpDown('down')"/>
                </td>
            </tr>
        </tbody>
    </table>
</div>
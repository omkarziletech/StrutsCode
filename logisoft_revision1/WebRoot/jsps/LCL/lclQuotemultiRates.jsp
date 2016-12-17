<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<script src="${path}/jsps/LCL/js/scriptjs.js" type="text/javascript" ></script>
<script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>
<script type="text/javascript">
    $(document).ready(function () {
        window.parent.parent.closePreloading();
        JToolTip('#plus', '${commodityCodeMany}', 400, 150);
        setColorForCheapest();
        checkNoRatesCommodity('${noRatesCommities}');
    });
    function checkNoRatesCommodity(commodities) {
        if (commodities !== '') {
            commodities = commodities.substring(0, commodities.length - 1);
            $.prompt("No Rates Available For Commodity# <b style='color:red;'>" + commodities + "</b>");
        }
    }

    function setColorForCheapest()
    {
        var routingtableobj = document.getElementById("routingtable");
        var row = routingtableobj.rows.item(0);
        var shortestTTDays = 0;
        var cheapestTotalCharges = 0.00;
        for (var i = 1; i <= routingtableobj.rows.length - 1; i++) {
            for (var j = 0; j < row.cells.length; j++) {
                var col = row.cells.item(j);
                if (col.innerHTML.toString().trim().toUpperCase().indexOf("TT(CFS TO FD)") != -1)
                {
                    if (routingtableobj.rows[i].cells[j].innerHTML != null && routingtableobj.rows[i].cells[j].innerHTML.trim() != "")
                    {
                        if (i == 1)
                        {
                            shortestTTDays = parseInt(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }

                        else if (shortestTTDays > parseInt(routingtableobj.rows[i].cells[j].innerHTML.trim()) ||
                                (shortestTTDays == 0 && parseInt(routingtableobj.rows[i].cells[j].innerHTML.trim() != 0)))
                        {
                            shortestTTDays = parseInt(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }
                    }
                }
                else if (col.innerHTML.toString().trim().toUpperCase().indexOf("TOTAL CHARGES") != -1)
                {
                    if (routingtableobj.rows[i].cells[j].innerHTML != null && routingtableobj.rows[i].cells[j].innerHTML.trim() != "")
                    {
                        if (i == 1)
                        {
                            cheapestTotalCharges = Number(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }
                        else if (cheapestTotalCharges > Number(routingtableobj.rows[i].cells[j].innerHTML.trim()) ||
                                (cheapestTotalCharges == 0 && Number(routingtableobj.rows[i].cells[j].innerHTML.trim() != 0)))
                        {
                            cheapestTotalCharges = Number(routingtableobj.rows[i].cells[j].innerHTML.trim());
                        }
                    }
                }
            }//end of j loop
        }//end of i loop
        for (var i = 1; i <= routingtableobj.rows.length - 1; i++) {
            for (var j = 0; j < row.cells.length; j++) {
                var col = row.cells.item(j);
                if (col.innerHTML.toString().trim().toUpperCase().indexOf("TT(CFS TO FD)") != -1 && shortestTTDays > 0)
                {
                    if (routingtableobj.rows[i].cells[j].innerHTML != null && routingtableobj.rows[i].cells[j].innerHTML.trim() != "")
                    {
                        if (shortestTTDays == parseInt(routingtableobj.rows[i].cells[j].innerHTML.trim()))
                        {
                            routingtableobj.rows[i].cells[j].className = "routingTableRed";
                        }
                    }
                }
                else if (col.innerHTML.toString().trim().toUpperCase().indexOf("TOTAL CHARGES") != -1 && cheapestTotalCharges > 0.00)
                {
                    if (routingtableobj.rows[i].cells[j].innerHTML != null && routingtableobj.rows[i].cells[j].innerHTML.trim() != "")
                    {
                        if (cheapestTotalCharges == Number(routingtableobj.rows[i].cells[j].innerHTML.trim()))
                        {
                            routingtableobj.rows[i].cells[j].className = "routingTableRed";
                        }
                    }
                }

            }
        }
    }
    function callCTSMultiRates(fileNumberId, index, moduleId, ctsAmount) {
        var isCommodityFlag = $("#isCommodityFlagId").val();
        if (isCommodityFlag === "" || isCommodityFlag === null) {
            $.prompt("Please add Weight and Measure from Commodity");
        }
        else if (ctsAmount == "") {
            $.prompt("No Rates Available.Please verify with vendor directly.");
            return false;
        }
        else {
            var href = "lclQuotemultiRate.do?methodName=carrier&index=" + index + "&fileNumberId=" + fileNumberId + "&moduleId=" + moduleId;
            $.colorbox({
                iframe: true,
                width: "70%",
                height: "90%",
                href: href,
                title: "Carrier"
            });
        }
    }

    function saveCTSMultiRates() {
        if ($("input:radio[name='routingRadio']:checked").is(':checked')) {
            var val = $("input:radio[name='routingRadio']:checked").val();
            showLoading();
            parent.$("#methodName").val('saveRates');
            parent.$("#index").val(val);
            parent.$("#lclQuoteForm").submit();
            document.opener.document.location.reload();
            parent.$.fn.colorbox.close();
        } else {
            $.prompt('Please select an origin');
        }
    }
    function showRatesOnMouseOver(index) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "showRates",
                param1: index,
                request: true
            },
            async: false,
            success: function (data) {
                $("#ratesToolTip").val(data);
            }
        });
    }

</script>
<body>
    <form id="lclQuoteMultiRateForm" name="lclQuoteMultiRateForm" action="/lclQuotemultiRate.do">
        <input type="hidden" id="isCommodityFlagId" name="isCommodityFlag" value="${isCommodityFlag}"/>
        <table width="100%" border="0">
            <tr class="tableHeadingNew">
                <td width="5%">
                    Routing Options
                </td>
                <td width="10%"></td>
                <td width="30%">
                    Destination: <span style="color:red">${destination}</span>
                </td>
                <td width="10%" style="float: right;">
                    <input type="button" class="button" value="Submit" onclick="saveCTSMultiRates()"/>
                    <input type="button" class="button" value="Cancel" onclick="parent.$.fn.colorbox.close();"/>
                </td>
                <td width="4%">

            </tr>
        </table>
        <table width="100%" border="0" class="textBoldforlcl" style="font-size: 12px;">
            <tr>
                <td width="5%"></td>
                <td width="30%">Commodity: <b>${commodityCodeOne}</b>
                    <c:if test="${not empty showPlus && showPlus=='Y'}">
                        <img id="plus" src="${path}/img/icons/plus.png" alt="Commodity" style="vertical-align: middle"/>
                    </c:if>
                </td>
                <td width="20%">CFT/CBM: <b>${commodityCftCbm}</b></td>
                <td width="20%">LBS/KGS: <b>${commodityLbsKgs}</b></td>
                <td width="10%">
                    <c:if test="${hazmat}">
                        <span style="color: red;font-size: 14px;vertical-align: top">(Includes Hazardous)</span>
                    </c:if>

                </td>
                <td width="10%"></td>
            </tr>
            <tr>
                <td></td>
                <td>Rate Type:<span style="color: green;font-size: 12px;vertical-align: top">${rateType}</span> </td>
                <td>Door Origin:<span style="color: green;font-size: 12px;vertical-align: top">${doorOrigin}</span></td>
                <td>Zip:<span style="color: green;font-size: 12px;vertical-align: top">${zip}</span></td>
                <td></td>
                <td></td>
            </tr>
        </table>
        <table width="100%" border="0"  cellpadding="0" cellspacing="0" >
            <tr>
                <td>

                    <cong:div style="width:100%; ">
                        <table class="dataTable" border="0" style="margin:1px 0; border: 1px solid #dcdcdc" id="routingtable">
                            <thead>
                                <tr>
                                    <th>Select</th>
                                    <th>Closest CFS</th>
                                    <th>Relay</th>
                                    <th>Next LRD</th>
                                    <th>TT(Door to CFS)</th>
                                    <th>TT(CFS to FD)</th>
                                    <th>O/F Rates/Charge</th>
                                        <c:if test="${pickupDisable eq 'Y'}">
                                        <th>Inland Rate (CTS)</th>
                                        <th></th>
                                        </c:if>
                                    <th>Total Charges</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty lclSession.routingOptionsList}">
                                    <c:set var="index" value='0'/>
                                    <c:forEach items="${lclSession.routingOptionsList}" var="routeObj">
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>
                                                <input type="radio" name="routingRadio" id="routingRadio" value="${index}">
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${routeObj.relayType eq 'D' || (routeObj.pooRelay eq routeObj.polRelay) && (routeObj.podRelay eq routeObj.fdRelay)}">
                                                        <%--  <c:set var="origin" value="${fn:substring(routeObj.origin,0,fn:indexOf(routeObj.origin,')')+1)}"/>
                                                        <c:set var="count" value="${fn:length(origin)}"/>
                                                        <c:set var="totalCount" value="${fn:length(routeObj.origin)}"/>
                                                        <c:set var="miles" value="${fn:substring(routeObj.origin,count+1,totalCount)}"/>--%>
                                                        <span style="color:blue;font-weight: bold">${routeObj.origin}</span>
                                                        <c:set var="styleMiles" value='${index >2 ? "green":"blue"}'/>
                                                        <span style="color:${styleMiles};font-weight: bold">${routeObj.miles}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <b>
                                                            <%--  <c:set var="miles" value="${fn:substring(routeObj.origin,fn:indexOf(routeObj.origin,'('),fn:indexOf(routeObj.origin,')')+1)}"/>
                                                            <c:set var="origin" value="${fn:substring(routeObj.origin,0,fn:indexOf(routeObj.origin,'(')-1)}"/>--%>
                                                            <span style="color:brown">${routeObj.origin}</span>
                                                            <span style="color:green">${miles}</span>
                                                        </b>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${routeObj.relayType eq 'D'}">
                                                        <span style="color:brown;font-weight:bold">${routeObj.routingType}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${(routeObj.pooRelay eq routeObj.polRelay) && (routeObj.podRelay eq routeObj.fdRelay)}">
                                                                <span style="color:brown;font-weight:bold"> DIRECT</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span title="${routeObj.pooRelayName}">${routeObj.pooRelay},</span>
                                                                <span title="${routeObj.polRelayName}">${routeObj.polRelay},</span>
                                                                <span title="${routeObj.podRelayName}">${routeObj.podRelay},</span>
                                                                <span title="${routeObj.fdRelayName}">${routeObj.fdRelay}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${routeObj.nextLrd}</td>
                                             <td width="3%">${routeObj.days}</td>
                                             <td width="3%">${routeObj.transitTime}</td>
                                            <td>
                                                ${routeObj.ofrateAmount}
                                                <span class="cons" onmouseover="showRatesOnMouseOver('${index}');
                                                        tooltip.showSmall($('#ratesToolTip').val());" onmouseout="tooltip.hide()">
                                                    <img src="${path}/img/icons/copy.gif" alt="rates" height="16" title="${ratesDetails}" width="16" />
                                                </span>
                                            </td>
                                            <c:if test="${pickupDisable eq 'Y'}">
                                                <td>${routeObj.ctsAmount}${routeObj.scac}
                                                <c:if test="${empty routeObj.ctsAmount}">
                                                    Vendor Error
                                                </c:if>
                                                    </td>
                                                <td>
                                                    <input type="button" class="button-style1 routingcarrier" value="ClickhereCTS" onclick="callCTSMultiRates('${fileNumberId}', '${index}', 'R', '${routeObj.ctsAmount}')"/>
                                                </td>
                                            </c:if>
                                            <td>${routeObj.totalAmount}</td>
                                        </tr>
                                    <input type="hidden" name="hiddenTotalAmount${index}" value="${routeObj.hiddenTotalAmount}"/>
                                    <c:set var="index" value="${index+1}"></c:set>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </cong:div>
                </td>
            </tr>
        </table>
        <input type="hidden" name="methodName" id="methodName"/>
        <input type="hidden" id="ratesToolTip" name="ratesToolTip"/>
    </form>
</body>
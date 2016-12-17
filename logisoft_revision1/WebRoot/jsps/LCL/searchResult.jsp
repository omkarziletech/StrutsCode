<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LCL Search</title>
        <%@include file="init.jsp"%>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@page import="com.gp.cong.common.Application"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <cong:javascript  src="${path}/jsps/LCL/js/checkLock.js"/>
        <script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>
        <% pageContext.setAttribute("singleQuotes", "'");%>
    </head>
    <body>
        <form id="lclSearchForm" name="lclSearchForm" action="lclSearch.do">
            <input type="hidden" id="highlightFileNo" name="highlightFileNo" value="${highlightFileNo}"/>
            <input type="hidden" id="uom" name="uom" value="${lclSearchForm.commodity}"/>
            <input type="hidden" id="userId" name="userId" value="${loginuser.userId}"/>
            <input type="hidden" id="isSingleFile" value="${isSingleFile}"/>
            <cong:hidden name="filenumber" id="filenumber" value="${fileNumber}"/>
            <cong:hidden name="searchFileNo" id="searchFileNo"/>
            <cong:hidden name="filterBy" id="filterBy" value="${lclSearchForm.filterBy}"/>
            <cong:hidden name="moduleName" id="moduleName" value="${lclSearchForm.moduleName}"/>
            <c:set var="moduleName" value="${lclSearchForm.moduleName}"/>
            <div id="button-group1" style="width:100%; margin: 3px 0">
                <input type="hidden" name="methodName" id="methodName" value="search"/>
                <script type="text/javascript">
                    function submitForm(methodName) {
                        $("#methodName").val(methodName);
                        $('#filterBy').val("All");
                        $("#searchFileNo").val("Y");
                        $("#lclSearchForm").submit();
                    }

                    function submitAesHistory(path, filenumber) {
                        var href = path + '/aesHistory.do?fileNumber=' + filenumber;
                        $(".linkSpan").attr("href", href);
                        $(".linkSpan").colorbox({
                            iframe: true,
                            width: "75%",
                            height: "90%",
                            title: "AES Tracking"
                        });
                    }

                    function submitBookingForm(methodName, fileNumber) {
                        window.location = 'lclBooking.do?methodName=' + methodName + '&fileNumber=' + fileNumber;
                        return false;
                    }

                    function submitSearchForm(methodName, fileId, obj, index) {
                        var rowslen = document.getElementById("file").rows.length - 1;
                        var rowlength = parseInt(index) + 1;
                        var rowCount = rowlength % parseInt(rowslen);
                        var releaseTool = "<span style='color:red' onmouseover=\"tooltip.showSmall('<strong>Released for Export</strong>')\" onmouseout='tooltip.hide();'>" + "R" + "</span>";
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "cargoReleaseStatus",
                                param1: fileId,
                                request: true,
                                dataType: "json"
                            },
                            async: false,
                            success: function (data) {
                                if (data) {
                                    var classValue = $(".onHoldClass" + index).text();
                                    if (classValue == "OH") {
                                        $(".statusClass" + index).html(releaseTool + "<span  style='color:red;font-weight: bold' title='On Hold' >," + "  OH  " + "</span>");
                                    } else {
                                        $(".statusClass" + index).html(releaseTool);
                                    }
                                }
                            }
                        });
                        obj.style.visibility = "hidden";
                        return false;
                    }

                    function totalCftLbs() {
                        var rowslen = document.getElementById("file").rows.length - 1;
                        var row = document.getElementById("file").rows.item(0);
                        var totalcft = 0.000;
                        var totallbs = 0.000;
                        var totalReleasedlbs = 0.000;
                        var totalReleasedcft = 0.000;
                        for (var i = 1; i <= rowslen; i++) {
                            for (var j = 0; j < row.cells.length; j++) {
                                var col = row.cells.item(j);
                                if (col.innerHTML.toString().toUpperCase() == 'CFT' || col.innerHTML.toString().toUpperCase() == 'CBM') {
                                    var cube = document.getElementById("file").rows[i].cells[j].innerHTML;
                                    var c = cube.substring(0, cube.indexOf('<'))
                                    if (c != "") {
                                        cube = c;
                                    } else {
                                        cube = cube;
                                    }
                                    if (cube != "") {
                                        totalcft = Number(totalcft) + Number(cube);
                                        totalcft = totalcft.toFixed(2);
                                    }
                                }
                            }
                        }
                        for (var k = 1; k <= rowslen; k++) {
                            for (var l = 0; l < row.cells.length; l++) {
                                var col = row.cells.item(l);
                                if (col.innerHTML.toString().toUpperCase() == 'LBS' || col.innerHTML.toString().toUpperCase() == 'KGS') {
                                    var weight = document.getElementById("file").rows[k].cells[l].innerHTML;
                                    var w = weight.substring(0, weight.indexOf('<'));
                                    if (w != "") {
                                        weight = w;
                                    } else {
                                        weight = weight;
                                    }
                                    if (weight != "") {
                                        totallbs = Number(totallbs) + Number(weight);
                                        totallbs = totallbs.toFixed(2);
                                    }
                                }
                            }
                        }
                        $('.statusvalues').each(function () {
                            var stvalues = $(this).text().trim().trim();
                            if (stvalues === 'R') {
                                var cftValues = $(this).next().next().next().next().text().replace(/[&\/\\#,+()$~%'":*?<>{}]/g, '').trim().trim();
                                var lbsValues = $(this).next().next().next().next().next().text().replace(/[&\/\\#,+()$~%'":*?<>{}]/g, '').trim().trim();
                                totalReleasedcft = Number(totalReleasedcft) + Number(cftValues);
                                totalReleasedcft = totalReleasedcft.toFixed(2);
                                totalReleasedlbs = Number(totalReleasedlbs) + Number(lbsValues);
                                totalReleasedlbs = totalReleasedlbs.toFixed(2);
                            }
                        });

                        //                        for (var a = 1; a <= rowslen; a++) {
                        //                            for (var b = 0; b < row.cells.length; b++) {
                        //                                var col = row.cells.item(b);
                        //                                if (col.innerHTML.toString().toUpperCase() == 'STATUS') {
                        //                                    var statusValue = document.getElementById("file").rows[a].cells[b].innerHTML;
                        //                                    if (statusValue.indexOf("Released") != -1) {
                        //                                        var Releasedweight = document.getElementById("file").rows[a].cells[b + 4].innerHTML;
                        //                                        var rw = Releasedweight.substring(0, Releasedweight.indexOf('<'))
                        //                                        if (rw != "") {
                        //                                            Releasedweight = rw;
                        //                                        } else {
                        //                                            Releasedweight = Releasedweight;
                        //                                        }
                        //                                        totalReleasedlbs = Number(totalReleasedlbs) + Number(Releasedweight);
                        //                                        totalReleasedlbs = totalReleasedlbs.toFixed(2);
                        //                                    }
                        //                                }
                        //                            }
                        //                        }
                        //                        for (var c = 1; c <= rowslen; c++) {
                        //                            for (var d = 0; d < row.cells.length; d++) {
                        //                                var col = row.cells.item(d);
                        //                                if (col.innerHTML.toString().toUpperCase() == 'STATUS') {
                        //                                    var statusValue = document.getElementById("file").rows[c].cells[d].innerHTML;
                        //                                    if (statusValue.indexOf("Released") != -1) {
                        //                                        var Releasedcube = document.getElementById("file").rows[c].cells[d + 3].innerHTML;
                        //                                        var rc = Releasedcube.substring(0, Releasedcube.indexOf('<'))
                        //                                        if (rc != "") {
                        //                                            Releasedcube = rc;
                        //                                        } else {
                        //                                            Releasedcube = Releasedcube;
                        //                                        }
                        //                                        totalReleasedcft = Number(totalReleasedcft) + Number(Releasedcube);
                        //                                        totalReleasedcft = totalReleasedcft.toFixed(2);
                        //                                    }
                        //                                }
                        //                            }
                        //                        }
                        var uom = $('#uom').val();
                        var filter = $('#filterBy').val();
                        if (filter == 'IWB' || filter == 'IPO') {
                            if (uom == 'I') {
                                document.getElementById('cftlbs').innerHTML = "<span style='color:blue;font-weight: bold'>" + totalcft + "</span>" + " CFT /" + "<span style='color:blue;font-weight: bold'>" + totallbs + "</span>" + " LBS";
                                document.getElementById('cftlbsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>" + totalReleasedcft + "</span>" + " CFT /" + "<span style='color:#800080;font-weight: bold'>" + totalReleasedlbs + "</span>" + " LBS";
                            }
                            if (uom == 'M') {
                                document.getElementById('cbmkgs').innerHTML = "<span style='color:blue;font-weight: bold'>" + totalcft + "</span>" + " CBM /" + "<span style='color:blue;font-weight: bold'>" + totallbs + "</span>" + " KGS";
                                document.getElementById('cbmkgsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>" + totalReleasedcft + "</span>" + " CBM /" + "<span style='color:#800080;font-weight: bold'>" + totalReleasedlbs + "</span>" + " KGS";
                            }
                        }
                    }

                    function openTrackingPopUp(path, methodName, filenumber, filenumberId) {
                        var href = path + '/lclTracking.do?methodName=' + methodName + '&fileNumber=' + filenumber + '&fileId=' + filenumberId;
                        $(".track").attr("href", href);
                        $(".track").colorbox({
                            iframe: true,
                            width: "65%",
                            height: "65%",
                            title: "Tracking"
                        });
                    }
                    function showConsolidate(path, methodName, filenumber, filenumberId) {
                        var url = path + '/lclSearch.do?methodName=' + methodName + '&fileNumber=' + filenumber + '&fileNumberA=' + filenumberId;
                        window.location = url;
                    }
                    function openQuickQuote(path) {
                        var href = path + "/lclSearch.do?methodName=openQuickQuote";
                        $('#quickQuote').attr("href", href);
                        $('#quickQuote').colorbox({
                            iframe: true,
                            width: "63%",
                            height: "80%",
                            title: "Quick Quote"
                        });
                    }
                    function searchResultBack(path, fileNumberId, moduleId, screenName) {
                        if (moduleId === 'mainScreen') {
                            $.ajaxx({
                                data: {
                                    className: "com.gp.cong.lcl.dwr.LclDwr",
                                    methodName: "searchFiles",
                                    request: "true"
                                },
                                async: false,
                                success: function (data) {
                                    if (data === 'true') {
                                        window.parent.goBack(path, fileNumberId, moduleId, screenName);
                                    }
                                }
                            });
                        } else {
                            window.parent.goBack(path, fileNumberId, moduleId, screenName);
                        }
                    }
                    function searchBack()
                    {
                        $("#methodName").val("goBackScreen");
                        $("#lclSearchForm").submit();
                    }


                    function openLoadUnits(path) {
                        var href = path + "/lclSearch.do?methodName=openVoyage";
                        $("#unitVoyage").attr("href", href);
                        $("#unitVoyage").colorbox({
                            iframe: true,
                            width: "40%",
                            height: "30%",
                            title: "Load Units"
                        });
                    }


                    function setResultHeight() {
                        if ($(".scrollable-table").length > 0) {
                            var windowHeight = window.parent.getFrameHeight();
                            var height = windowHeight;
                            height -= 50;
                            $(".scrollable-table").height(height);
                            $("body").css("overflow-y", "hidden");
                        }
                    }

                    jQuery(document).ready(function () {
                        totalCftLbs();
                        $(document).keydown(function (e) {
                            if (e.keyCode === 13) {
                                submitForm('search');
                                return false;
                            }
                        });
                        if ($("#isSingleFile").val() === "true") {
                            $('.file-no').trigger('click');
                        }
                        setResultHeight();
                        $(window).resize(function () {
                            window.parent.changeHeight();
                            setResultHeight();
                        });
                    });
                </script>
                <style>
                    li {
                        width: 70px;
                        background-color: #F0F0F6;
                    }
                </style>
            </div>

            <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td class="fileno-text1" align="left">
                        <table width="100%">
                            <tr>
                                <td width="33%">
                                    <c:if test="${not empty lclSearchForm.filterBy}">
                                        <c:if test="${lclSearchForm.filterBy=='Q'}">
                                            <b>Filter By-><c:out value="Quote"></c:out></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='B'}">
                                            <b>Filter By-><c:out value="Booking"></c:out></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='RF'}">
                                            <b>Filter By-><c:out value="Refused"></c:out></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='X'}">
                                            <b>Filter By-><c:out value="Terminated"></c:out></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='IWB'}">
                                            <b>Filter By-><span style="color:blue;font-weight: bold">Inventory All</span></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='BP'}">
                                            <b>Filter By-><span style="color:blue;font-weight: bold">BL Pool</span></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='IPO'}">
                                            <b>Filter By-><span style="color:blue;font-weight: bold">Inventory Pickups Only</span></b>,
                                        </c:if>
                                        <c:if test="${lclSearchForm.filterBy=='BL'}">
                                            <b>Filter By-><span style="color:blue;font-weight: bold">Loaded with no BL</span></b>,
                                        </c:if>
                                    </c:if>
                                    <c:if test="${not empty lclSearchForm.fileNumber}">
                                        <b>File Number-><span style="color:blue;font-weight: bold"><c:out value="${fn:toUpperCase(lclSearchForm.fileNumber)}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.inbondNo}">
                                        <b>Inbond No-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.inbondNo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.ssl}">
                                        <b>SSL-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.ssl}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.origin}">
                                        <b>Origin-><c:choose>
                                                <c:when test="${lclSearchForm.filterBy == 'IWB' || lclSearchForm.filterBy == 'BP'
                                                                || lclSearchForm.filterBy == 'IPO' || lclSearchForm.filterBy == 'BL'}">
                                                        <span style="color:green;font-weight: bold"><c:out value="${lclSearchForm.origin}"></c:out></span>

                                                </c:when>
                                                <c:otherwise><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.origin}"></c:out></span></c:otherwise>
                                            </c:choose>,</b>
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.pol}">
                                        <b>POL-><c:choose>
                                                <c:when test="${lclSearchForm.filterBy == 'IWB' || lclSearchForm.filterBy == 'IPO' ||
                                                                lclSearchForm.filterBy == 'BP' || lclSearchForm.filterBy == 'BL'}">
                                                        <span style="color:green;font-weight: bold"><c:out value="${lclSearchForm.pol}"></c:out></span>
                                                </c:when>
                                                <c:otherwise><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.pol}"></c:out></span></c:otherwise>
                                            </c:choose>,</b>
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.pod}">
                                        <b>POD-><c:choose>
                                                <c:when test="${lclSearchForm.filterBy == 'IWB' || lclSearchForm.filterBy == 'BP'
                                                                || lclSearchForm.filterBy == 'IPO' || lclSearchForm.filterBy == 'BL'}">
                                                        <span style="color:green;font-weight: bold"><c:out value="${lclSearchForm.pod}"></c:out></span>
                                                </c:when>
                                                <c:otherwise><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.pod}"></c:out></span></c:otherwise>
                                            </c:choose>,</b>
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.destination}">
                                        <b>Destination-><c:choose>
                                                <c:when test="${lclSearchForm.filterBy == 'IWB' || lclSearchForm.filterBy == 'BP'
                                                                || lclSearchForm.filterBy == 'IPO' || lclSearchForm.filterBy == 'BL'}">
                                                        <span style="color:green;font-weight: bold"><c:out value="${lclSearchForm.destination}"></c:out></span>
                                                </c:when>
                                                <c:otherwise><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.destination}"></c:out></span></c:otherwise>
                                            </c:choose>,</b>
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.shipperName}">
                                        <b>Shipper-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.shipperName}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.forwarder}">
                                        <b>Forwarder-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.forwarder}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.consignee}">
                                        <b>Consignee-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.consignee}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.masterBl}">
                                        <b>Master BL-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.masterBl}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.issuingTerminal}">
                                        <b>Issuing Terminal->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.issuingTerminal}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.client}">
                                        <b>Client-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.client}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.containerNo}">
                                        <b>Container No-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.containerNo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.createdBy}">
                                        <b>Quote By-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.createdBy}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.bookedBy}">
                                        <b>Booked By-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.bookedBy}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${empty lclSearchForm.fileNumber && lclSearchForm.filterBy != 'IPO' && lclSearchForm.filterBy != 'IWB'
                                                      && lclSearchForm.filterBy != 'BP' && lclSearchForm.filterBy != 'BL' && empty lclSearchForm.containerNo}">
                                        <b> Start Date->
                                            <span style="color:blue;font-weight: bold">
                                                <c:out value="${lclSearchForm.startDate}"></c:out></span></b>,
                                            </c:if>
                                            <c:if test="${empty lclSearchForm.fileNumber && lclSearchForm.filterBy != 'IPO'
                                                          && lclSearchForm.filterBy != 'IWB' && lclSearchForm.filterBy != 'BP' && lclSearchForm.filterBy != 'BL'  && empty lclSearchForm.containerNo}">
                                        <b> To Date->
                                            <span style="color:blue;font-weight: bold">
                                                <c:out value="${lclSearchForm.endDate}"></c:out></span></b>,
                                            </c:if>
                                            <c:if test="${not empty lclSearchForm.originRegion && lclSearchForm.originRegion!='0'}">
                                        <b>Origin Region->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.originRegion}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.destinationRegion && lclSearchForm.destinationRegion!='0'}">
                                        <b>Destination Region->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.destinationRegion}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.sslBookingNo && lclSearchForm.sslBookingNo!='0'}">
                                        <b>SSL Booking #->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.sslBookingNo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.bookedForVoyage && lclSearchForm.bookedForVoyage!='0'}">
                                        <b>Booked for Voyage->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.bookedForVoyage}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.customerPo && lclSearchForm.customerPo!='0'}">
                                        <b>Customer Po #->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.customerPo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.trackingNo && lclSearchForm.trackingNo!='0'}">
                                        <b>Tracking #->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.trackingNo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.warehouseDocNo && lclSearchForm.warehouseDocNo!='0'}">
                                        <b>Warehouse Doc #->
                                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.warehouseDocNo}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.limit && lclSearchForm.limit!='' && empty lclSearchForm.fileNumber && lclSearchForm.filterBy != 'IWB'
                                                      && lclSearchForm.filterBy != 'BP' && lclSearchForm.filterBy != 'IPO' && lclSearchForm.filterBy != 'BL'}">
                                        <b>Limit-><span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.limit}"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.sortBy && fn:contains(lclSearchForm.sortBy, 'C')}">
                                        <b>Sort By-><span style="color:blue;font-weight: bold"><c:out value="Container Cut off"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.sortBy && fn:contains(lclSearchForm.sortBy, 'D')}">
                                        <b>Sort By-><span style="color:blue;font-weight: bold"><c:out value="Doc Cut Off"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.sortBy && fn:contains(lclSearchForm.sortBy, 'DNR')}">
                                        <b>Sort By-><span style="color:blue;font-weight: bold"><c:out value="Doc's Not Received"></c:out></span></b>,
                                        </c:if>
                                        <c:if test="${not empty lclSearchForm.sortBy && fn:contains(lclSearchForm.sortBy, 'E')}">
                                        <b>Sort By-><span style="color:blue;font-weight: bold"><c:out value="ETD"></c:out></span></b>
                                        </c:if>
                                        <c:if test="${consolidate}">
                                        <b><span style="color:red;"><c:out value="Consolidation"></span></c:out></b>
                                        </c:if>
                                </td>
                                <td width="24%" align="center">
<!--                                    <input type="button" class="button-style1" onclick="searchResultBack('${path}', '', 'mainScreen', 'searchResult');" value="Back"/>-->
                                    <input type="button" class="button-style1" onclick="searchBack();" value="Back"/>
                                    <input type="button" value="New Quote" class="button-style1" onclick="checkFileNumber('${path}', '', 'Quotes', '${lclSearchForm.moduleName}');"/>
                                    <c:choose>
                                        <c:when test="${moduleName eq 'Imports'}"><input type="button" class="button-style1" onclick="checkFileNumber('${path}', '', 'Bookings', '${lclSearchForm.moduleName}');" value="New DR"/>
                                        </c:when><c:otherwise>
                                            <input type="button" class="button-style1" onclick="checkFileNumber('${path}', '', 'Bookings', '${lclSearchForm.moduleName}');" value="New Bkg"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${lclSearchForm.filterBy == 'IWB' && lclSearchForm.pol!='' && lclSearchForm.pod!=''}">
                                        <input type="button" id="unitVoyage" class="button" onclick="openLoadUnits('${path}')" value="Load"/>
                                    </c:if>
                                </td>
                                <c:if test="${lclSearchForm.filterBy == 'IPO' || lclSearchForm.filterBy == 'IWB'
                                              || lclSearchForm.filterBy == 'BP' || lclSearchForm.filterBy == 'BL'}">
                                    <c:choose>
                                        <c:when test="${lclSearchForm.commodity eq 'I'}">
                                            <td style="font-weight: bolder;" width="5%">Totals:</td>
                                            <td style="font-weight: bolder;" width="5%"> <cong:label id="cftlbs" text=""></cong:label>
                                                </td>
                                                <td style="font-weight: bolder;" width="5%">&nbsp;&nbsp;&nbsp;Released:</td>
                                                <td style="font-weight: bolder;" width="5%"> <cong:label id="cftlbsForR" text=""></cong:label>,
                                                </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="font-weight: bolder;" width="5%">Totals:</td>
                                            <td style="font-weight: bolder;" width="5%"> <cong:label id="cbmkgs" text=""></cong:label>
                                                </td>
                                                <td style="font-weight: bolder;" width="5%">&nbsp;&nbsp;&nbsp;Released:</td>
                                                <td style="font-weight: bolder;" width="5%"> <cong:label id="cbmkgsForR" text=""></cong:label>,
                                                </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <td align="right" width="20%">
                                    <c:if test="${moduleName ne 'Imports'}">
                                        <c:if test="${lclSearchForm.cfcl=='0'}">
                                            <b>CFCL-><span style="color:blue;font-weight: bold"><c:out value="Exclude"></c:out></span></b>
                                            </c:if>
                                            <c:if test="${lclSearchForm.cfcl=='1'}">
                                            <b>CFCL-><span style="color:blue;font-weight: bold"><c:out value="Only"></c:out></span></b>
                                            </c:if>
                                            <c:if test="${empty lclSearchForm.cfcl}">
                                            <b>CFCL-><span style="color:blue;font-weight: bold"><c:out value="Include"></c:out></span></b>
                                            </c:if>
                                            <c:if test="${not empty lclSearchForm.cfclAcct}">
                                            ,<b>CFCL Account#->
                                                <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.cfclAcct}"></c:out></span></b>
                                            </c:if>
                                        </c:if>
                                </td>
                                <td width="2%" align="right">
                                    <b><span style="color:black;font-weight: bold">
                                            &nbsp;&nbsp;&nbsp;&nbsp;File Search:&nbsp;</span></b></td><td width="2%" align="left">
                                    <input type="text" id="fileNumber" name="fileNumber" size="8" class="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
                                    <input type="hidden" id="limit" name="limit" size="8" class="textlabelsBoldForTextBox" value="100"/>
                                    <img src="${path}/img/icons/magnifier.png" border="0" alt="S" onclick="submitForm('search');"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:if test="${not empty fileSearchList}">
                            <div id="result-header" class="table-banner green">
                                <div class="float-left">
                                    <c:choose>
                                        <c:when test="${fn:length(fileSearchList)>1}">
                                            ${fn:length(fileSearchList)} files found.
                                        </c:when>
                                        <c:otherwise>1 file found.</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="scrollable-table">
                                <div>
                                    <div>
                                        <table>
                                            <thead>
                                                <tr>
                                                    <c:if test="${template eq null || template.qu}"><th><div label="QU"/></th></c:if>
                                                    <c:if test="${template eq null || template.bk}"><th><div label="BK"/></th></c:if>
                                                    <c:if test="${template eq null || template.bl}"><th><div label="BL"/></th></c:if>
                                                    <c:if test="${template eq null || template.hz}"><th><div label="HZ"/></th></c:if>
                                                    <c:if test="${template eq null || template.edi}"><th><div label="EDI"/></th></c:if>
                                                        <th><div label="S"/></th>
                                                        <th><div label="File No"/></th>
                                                    <c:if test="${template eq null || template.tr}"><th><div label="TR"/></th></c:if>
                                                        <c:if test="${template eq null || template.status}">
                                                        <th>
                                                            <div label="Status" onclick="doSortAscDesc('status')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.doc}"><th><div label="Doc"/></th></c:if>
                                                        <th><div label="Dispo"/></th>
                                                    <c:if test="${template eq null || template.currentLocation}"><th><div label="CurLoc"/></th></c:if>
                                                    <c:if test="${template eq null || template.pcs}"><th><div label="PCS"/></th></c:if>
                                                    <c:if test="${template eq null || template.cube}"><th><div label="${lclSearchForm.commodity eq 'M' ? 'CBM' : 'CFT'}"/></th></c:if>
                                                    <c:if test="${template eq null || template.cube}"><th><div label="${lclSearchForm.commodity eq 'M' ? 'KGS' : 'LBS'}"/></th></c:if>
                                                        <c:if test="${template eq null || template.origin}">              
                                                        <th>
                                                            <div label="Origin" onclick="doSortAscDesc('originUncode')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.pol}">
                                                        <th>
                                                            <div label="Pol" onclick="doSortAscDesc('polUncode')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.pod}">
                                                        <th>
                                                            <div label="Pod" onclick="doSortAscDesc('podUncode')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.destination}">
                                                        <th>
                                                            <div label="Destn" onclick="doSortAscDesc('destinationUncode')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <th><div label="BookedVoy"/></th>
                                                    <c:if test="${template eq null || template.dateReceived}"><th><div label="Date Rec"/></th></c:if>
                                                    <c:if test="${template eq null || template.originLrd}"><th><div label="Origin LRD"/></th></c:if>
                                                    <c:if test="${template eq null || template.loadLrd}"><th><div label="Load LRD"/></th></c:if>
                                                    <c:if test="${template eq null || template.bookedSaildate}"><th><div label="SailingDate"/></th></c:if>
                                                        <c:if test="${template eq null || template.shipper}">
                                                        <th>
                                                            <div label="Shipper" onclick="doSortAscDesc('shipName')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.fwd}">
                                                        <th>
                                                            <div label="FWD" onclick="doSortAscDesc('fwdName')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.consignee}">                
                                                        <th>
                                                            <div label="Consig" onclick="doSortAscDesc('consName')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.billTm}">
                                                        <th>
                                                            <div label="Bill TM" onclick="doSortAscDesc('billingTerminal')" class="underline"/>
                                                        </th>
                                                    </c:if>
                                                    <c:if test="${template eq null || template.aesBy}"><th><div label="AES"/></th></c:if>
                                                    <c:if test="${template eq null || template.quoteBy}"><th><div label="Quote By"/></th></c:if>
                                                    <c:if test="${template eq null || template.bookedBy}"><th><div label="Booked By"/></th></c:if>
                                                    <c:if test="${template eq null || template.cons}"><th><div label="Cons"/></th></c:if>
                                                    <c:if test="${template eq null || template.hotCodes}"><th><div label="HotCodes"/></th></c:if>
                                                        <th><div label="Action"/></th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                <c:set var="index" value='0'/>
                                                <c:forEach var="file" items="${fileSearchList}">
                                                    <tr>
                                                        <c:if test="${template==null || template.qu}">
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty file.bookedBy && not empty file.quoteBy}">
                                                                        <span title="Quote"><img src="${path}/img/icons/orange_dot.png" alt="orange disc"/></span>
                                                                        </c:when>
                                                                        <c:when test="${file.state=='Q' && empty file.bookedBy && not empty file.quoteBy}">
                                                                            <c:choose>
                                                                                <c:when test="${file.quoteComplete}">
                                                                                <span title="Quote"><img src="${path}/img/icons/darkGreenDot.gif"  alt="green disc"/></span>
                                                                                </c:when><c:otherwise>
                                                                                <span title="Quote"><img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"/></span>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:when>
                                                                    </c:choose>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${template==null || template.bk}">
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${file.state eq 'B' && (file.status eq 'M' || file.state eq 'B' && moduleName eq 'Exports')}">
                                                                        <span title="${mouseover}"><img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/></span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <c:when test="${file.state eq 'B' && moduleName eq 'Imports' && file.pieceUnit ne null}">
                                                                                <span title="Dock Receipt"><img src="${path}/img/icons/lightBlue2.gif" alt="green disc"/></span>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <c:if test="${moduleName eq 'Imports' && file.state eq 'B'}">
                                                                                    <span title="Dock Receipt"><img src="${path}/img/icons/reddot1.gif" alt="green disc"/></span></c:if>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${template==null || template.bl}}">
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${file.state eq 'BL' && file.status eq 'M'}">
                                                                        <span title="Manifested"><img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/></span>
                                                                        </c:when>
                                                                        <c:when test="${file.postedByUserId!='' && file.postedByUserId!=null}">
                                                                        <span title="Posted"><img src="${path}/img/icons/yellow.gif" alt="yellow disc"/></span>
                                                                        </c:when>
                                                                        <c:when test="${file.state eq 'BL' && (file.status eq 'B' || file.status eq 'R')}">
                                                                        <span title="Pool"><img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"/></span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:if test="${file.state eq 'BL'}">
                                                                            <span><img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"/></span></c:if>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${template==null || template.hz}">
                                                            <td>
                                                                <c:if test="${file.hazmat!=null && file.hazmat==true}">
                                                                    <img src="${path}/img/icons/danger..png"  style="cursor:pointer" width="12" height="12" alt="Haz" title="Hazardous Information"/>
                                                                </c:if>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${template==null || template.edi==true}">
                                                            <td>&nbsp;</td>
                                                        </c:if>
                                                        <td>
                                                            <span style="vertical-align:middle">${file.datasource}</span>
                                                        </td>
                                                        <td>
                                                            <u>
                                                                <c:choose>
                                                                    <c:when test="${file.bookingType eq 'I' || file.bookingType eq 'T'}">
                                                                        <c:set var="fileNo" value="IMP-${file.fileNumber}"/>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:choose>
                                                                            <c:when test="${file.shortShip}">
                                                                                <c:set var="fileNo" value="${file.fileNumber}"/>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <c:set var="fileNo" value="${fn:substring(file.originUnLocCode,2,5)}-${file.fileNumber}"/>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <c:choose>
                                                                    <c:when test="${file.fileNumber eq fileNumber}">
                                                                        <span class="highlight-saffron link" onclick=" checkLock('${path}', '${file.fileNumber}', '${file.id}', '${file.state}', '${lclSearchForm.moduleName}', '${file.importTranshipment}', 'LCLIMPTRANS');">
                                                                            ${fileNo}
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="link" onclick=" checkLock('${path}', '${file.fileNumber}', '${file.id}', '${file.state}', '${lclSearchForm.moduleName}', '${file.importTranshipment}', 'LCLIMPTRANS');">
                                                                            ${fileNo}
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </u>
                                                        </td>
                                                        <c:if test="${template==null || template.tr}">
                                                            <display:column title="TR" style="cursor:pointer">
                                                        <span class="track" onclick="openTrackingPopUp('${path}', 'display', '${file.fileNumber}', '${file.id}');">
                                                            <img src="${path}/img/icons/transaction.gif" align="middle" height="10" width="10" alt="TR"/></span>
                                                        </display:column>
                                                    </c:if>
                                                    <c:if test="${template==null || template.status}">
                                                    <td>
                                                        <label id="Release" class="statusClass${index}">
                                                            <c:choose>
                                                                <c:when test="${file.state eq 'Q'}">
                                                                    <span style="vertical-align: top;height: 10px" id="statusQ" title="Quote">${file.state}</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${file.state eq 'B' || file.state eq 'BL'}">
                                                                        <c:if test="${file.status eq 'B'}">
                                                                            <span style="height: 10px" title="Booking">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'X'}">
                                                                            <span style="height: 10px" title="Cargo Terminated">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'RF' }">
                                                                            <span style="height: 10px" title="Cargo Refused">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'WU'}">
                                                                            <span style="height: 10px" title="Warehouse Un-Verified">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'WV'}">
                                                                            <span style="height: 10px" title="Warehouse Verified">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'R'}">
                                                                            <span style="height: 10px" id="statusR" title="Released for Export">
                                                                                <font color="red">${file.status}</font></span>
                                                                       </c:if>
                                                                       <c:if test="${file.status eq 'PR'}">
                                                                            <span style="height: 10px" id="statusR" title="PreReleased for Export">
                                                                                ${file.status}</span>
                                                                       </c:if>
                                                                       <c:if test="${file.status eq 'L'}">
                                                                            <span style="height: 10px" title="Load Completed">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'M'}">
                                                                            <span style="height: 10px" title="Manifested">${file.status}</span>
                                                                        </c:if>
                                                                        <c:if test="${file.status eq 'PU'}">
                                                                            <span style="height: 10px" title="Picked Up">${file.status}</span>
                                                                        </c:if>
                                                                    </c:if>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <c:if test="${(file.billcode=='S' && file.credit_s=='11343') || (file.billcode=='T' && file.credit_t=='11343') || (file.billcode=='F' && file.credit_f=='11343')}">
                                                                <c:out value=","/>
                                                                <label class="onHoldClass${index}"><span  style="color:red;font-weight: bold" title="On Hold" >OH</span></label>
                                                            </c:if>
                                                        </label>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.doc}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${file.clientPwk=='1'}">
                                                                <span style="vertical-align:middle">Y</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span  style="vertical-align:middle"></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <span style="cursor:pointer" title="${file.dispoDesc}"><c:out value="${file.dispoCode}"></c:out></span>
                                                    </td>
                                                <c:if test="${template==null || template.currentLocation}">
                                                    <td>
                                                        <span style="cursor:pointer" title="${file.currentLocationName}/${file.currentLocationStatecode}/${file.currentLocation}">${file.currentLocation}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.pcs}">
                                                    <td>
                                                        <c:out value="${file.totalPiece}"/>
                                                        <c:if test="${not empty file.totalActualPiece && file.totalActualPiece!=0}">
                                                            <c:if test="${file.totalActualPiece!=file.totalBookedPiece}">
                                                                <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${file.totalBookedPiece}">*</span>
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${lclSearchForm.commodity=='I'}">
                                                        <c:if test="${template==null || template.cube}">
                                                            <td>
                                                                <c:out value="${file.totalVolumeImperial}"/>
                                                                <c:if test="${not empty file.totalActualMeasure && file.totalActualMeasure!='0.000'}">
                                                                    <c:choose>
                                                                        <c:when test="${file.totalMeasureFlag}">
                                                                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${file.totalBookedMeasure}">*</span>
                                                                        </c:when>
                                                                        <c:otherwise></c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${template==null || template.weight}">
                                                            <td>
                                                                <c:out value="${file.totalWeightImperial}"/>
                                                                <c:if test="${not empty file.totalActualWeight && file.totalActualWeight!='0.000'}">
                                                                    <c:choose>
                                                                        <c:when test="${file.totalWeightFlag}">
                                                                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${file.totalBookedWeight}">*</span>
                                                                        </c:when>
                                                                        <c:otherwise></c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                            </td>
                                                        </c:if>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>
                                                            <c:out value="${file.totalVolumeMetric}"/>
                                                            <c:if test="${not empty file.totalActualVolumeMetric && file.totalActualVolumeMetric!='0.000'}">
                                                                <c:choose>
                                                                    <c:when test="${file.totalVolumeMetricFlag}">
                                                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${file.totalBookedVolumeMetric}">*</span>
                                                                    </c:when>
                                                                    <c:otherwise></c:otherwise>
                                                                </c:choose>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:out value="${file.totalBookedWeightMetric}"/>
                                                            <c:if test="${not empty file.totalActualWeightMetric && file.totalActualWeightMetric!='0.000'}">
                                                                <c:choose>
                                                                    <c:when test="${file.totalWeightMetricFlag}">
                                                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${file.totalBookedWeightMetric}">*</span>
                                                                    </c:when>
                                                                    <c:otherwise></c:otherwise>
                                                                </c:choose>
                                                            </c:if>
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:if test="${template==null || template.origin}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${file.pooPickup && not empty file.pickupCity  && moduleName ne 'Imports'}">
                                                                <c:choose>
                                                                    <c:when test="${file.bookingType ne 'T'}">
                                                                        <span  style="color:red;" title="Origin=${file.originUnLocCode}/${file.originName}/${file.originState} <br>Door Origin=${fn:substring(file.pickupCity,fn:indexOf(file.pickupCity,'-')+1,fn:indexOf(file.pickupCity,'length-1'))}">${file.originUnLocCode}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span  style="color:red;" title="Origin=${file.originUnLocCode}/${file.originName}/${file.originState}
                                                                               <br>Cargo Originated=${file.transPolCode}/${file.transPolName}/${file.polCountry}
                                                                               <br>Door Origin=${fn:substring(file.pickupCity,fn:indexOf(file.pickupCity,'-')+1,fn:indexOf(file.pickupCity,'length-1'))}">${file.originUnLocCode}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:choose>
                                                                    <c:when test="${file.bookingType eq 'T' && moduleName ne 'Imports'}">
                                                                        <span  style="color:brown;" title="Origin=${file.originUnLocCode}/${file.originName}/${file.originState}
                                                                               <br>Cargo Originated=${file.transPolCode}/${file.transPolName}/${file.polCountry}">${file.originUnLocCode}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span title="${file.originUnLocCode}/${file.originName}/${file.originState}">${file.originUnLocCode}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.pol}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${file.relayOveride==true}">
                                                                <span title="${file.polUnLocCode}/${file.polName}/${file.polState}" style="color:#800080"><b>${file.polUnLocCode}</b></span>
                                                                    </c:when><c:otherwise>
                                                                        <c:if test="${file.fdUnLocCode!='007UN'}">
                                                                    <span title="${file.polUnLocCode}/${file.polName}/${file.polState}">${file.polUnLocCode}</span>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.pod}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${file.relayOveride==true}">
                                                                <span title="${file.podUnLocCode}/${file.podName}/${file.podCountry}" style="color:#800080"><b>${file.podUnLocCode}</b></span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:if test="${file.fdUnLocCode!='007UN'}">
                                                                    <span title="${file.podUnLocCode}/${file.podName}/${file.podCountry}">${file.podUnLocCode}</span>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.destination}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${file.pooPickup && not empty file.pickupCity && moduleName eq 'Imports'}">
                                                                <span  style="color:red;" title="Destination=${file.fdUnLocCode}/${file.fdName}/${file.fdCountry} <br>Door Delivery=${fn:substring(file.pickupCity,fn:indexOf(file.pickupCity,'-')+1,fn:indexOf(file.pickupCity,'length-1'))}">${file.fdUnLocCode}</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span title="${file.fdUnLocCode}/${file.fdName}/${file.fdCountry}">${file.fdUnLocCode}</span>
                                                            </c:otherwise>  </c:choose>
                                                        </td>
                                                </c:if>
                                                <td>${file.bookedVoyage}</td>
                                                <c:if test="${template==null || template.dateReceived}">
                                                    <td><fmt:formatDate value="${file.cargoReceivedDate}" pattern="dd-MMM-yyyy"/></td>
                                                </c:if>
                                                <c:if test="${template==null || template.originLrd}">
                                                    <td><fmt:formatDate value="${file.originLrd}" pattern="dd-MMM-yyyy"/></td>
                                                </c:if>
                                                <c:if test="${template==null || template.loadLrd}">
                                                    <td><fmt:formatDate value="${file.loadLrd}" pattern="dd-MMM-yyyy"/></td>
                                                </c:if>
                                                <c:if test="${template==null || template.bookedSaildate}">
                                                    <td><fmt:formatDate value="${file.sailDate}" pattern="dd-MMM-yyyy"/></td>
                                                </c:if>
                                                <c:if test="${template==null || template.shipper}">
                                                    <td>
                                                        <span class="hotspot" title="${file.shipName}/${file.shipAcct}/${file.shipAdd}
                                                              /${file.shipCity},${file.shipState},${file.shipZip}">${fn:substring(file.shipName,0,5)}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.fwd}">
                                                    <td>
                                                        <c:set var="fwd" value="${fn:replace(file.fwdName,singleQuotes,'')}"/>
                                                        <span title="${fwd}/${file.fwdAcct}/${file.fwdAdd}
                                                              /${file.fwdCity},${file.fwdState},${file.fwdZip}">${fn:substring(file.fwdName,0,5)}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.consignee}">
                                                    <td>
                                                        <span title="${file.consName}/${file.consAcct}/${file.consAdd}
                                                              /${file.consCity},${file.consState},${file.consZip}">${fn:substring(file.consName,0,5)}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.billTm}">
                                                    <td>
                                                        <c:choose><c:when test="${moduleName eq 'Exports'}">
                                                                <span title="${file.billTm}-${file.billTermNo}">${fn:substring(file.billTm,0,5)}</span>
                                                            </c:when> <c:otherwise>
                                                                <span title="${file.billTm}-${file.billTermNo}">${file.billTermNo}-${file.billTm}</span>
                                                            </c:otherwise> </c:choose>
                                                        </td>
                                                </c:if>
                                                <c:if test="${template==null || template.aesBy}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty file.aesStatus}">
                                                                <c:set var="aesStatus" value="${fn:toUpperCase(file.aesStatus)}"/>
                                                                <c:choose>
                                                                    <c:when test="${aesStatus eq 'SHIPMENT ADDED' || aesStatus eq 'SHIPMENT REPLACED'}">
                                                                        <span class="linkSpan" id="link"  style="font-weight: bold;background:#00FF00" title="${file.aesStatus}" onclick="submitAesHistory('${path}', '${file.fileNumber}');">AES</span>
                                                                    </c:when>
                                                                    <c:when test="${aesStatus eq 'VERIFY'}">
                                                                        <span class="linkSpan"  style="font-weight: bold;background:#00FFFF" title="${file.aesStatus}" onclick="submitAesHistory('${path}', '${file.fileNumber}');">AES</span>
                                                                    </c:when>
                                                                    <c:when test="${aesStatus eq 'SHIPMENT REJECTED' || aesStatus eq 'SUCCESSFULLY PROCESSED'}">
                                                                        <span class="linkSpan"  style="font-weight: bold;background:#FF0000" title="${file.aesStatus}" onclick="submitAesHistory('${path}', '${file.fileNumber}');">AES</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="linkSpan"  style="font-weight: bold;background:yellow" title="${file.aesStatus}" onclick="submitAesHistory('${path}', '${file.fileNumber}');">AES</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:when test="${not empty file.sedCount && file.sedCount!='0'}">
                                                                <span style="font-weight: bold;background:yellow;cursor: pointer" title="Aes Sent" >AES</span>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.quoteBy}">
                                                    <td>
                                                        <span title="${fn:toUpperCase(file.quoteBy)}">${fn:toUpperCase(fn:substring(file.quoteBy,0,6))}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.bookedBy}">
                                                    <td>
                                                        <span title="${fn:toUpperCase(file.bookedBy)}">${fn:toUpperCase(fn:substring(file.bookedBy,0,6))}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${moduleName ne 'Imports' && (template==null || template.cons)}">
                                                    <td>
                                                        <c:if test="${not empty file.consolidatedFiles && consIconSearchScreen!=false}">
                                                            <span class="cons" onmouseover="showConsolidateOnMouseOver('${file.id}', '${index}');" onclick="showConsolidate('${path}', 'consolidatedFiles', '${file.fileNumber}', '${file.id}')" onmouseout="tooltip.hide()">
                                                                <img src="${path}/jsps/LCL/images/consolidate.png" alt="cons" height="10" width="10" />
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                </c:if>
                                                <c:if test="${template==null || template.hotCodes}">
                                                    <td>
                                                        <span id="hotCodeId" title="${file.hotCodes}">${fn:substring(file.hotCodeKey,0,5)}</span>
                                                    </td>
                                                </c:if>
                                                <c:if test="${moduleName ne 'Imports' && (template==null || template.relayOverride)}">
                                                    <td>
                                                        <c:if test="${file ne null && file.status eq 'WV'}">
                                                            <span style="cursor:pointer" title="Release">
                                                                <img src="${path}/img/icons/releaseicon.png" width="10" height="10" align="middle" id="Release"
                                                                     class="buttonStyleNew" onclick="submitSearchForm('Release', '${file.id}', this, '${index}')"></span>
                                                            </c:if>
                                                    </td>
                                                </c:if>
                                                <c:set var="index" value="${index+1}"></c:set>
                                                    </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileIdA" value="${param.fileIdA}"/>
<div style="width:100%; float:left;">
    <table width="100%" class="dataTable" id="conosolidateId">
        <thead>
            <tr>
                <th>File No</th>
                <th>Origin</th>
                <th>Client</th>
                <th>Status</th>
                <th>Sail Date</th>
                <th>Piece</th>
                <th>Weight (LBS)</th>
                <th>Weight (KGS)</th>
                <th>Measure (CFT)</th>
                <th>Measure (CBM)</th>

                <th>Action</th>
            </tr>
        </thead>
        <tbody id="mytbody">
            <c:if test="${not empty consolidateList}">
                <c:forEach items="${consolidateList}" var="consolidate">
                    <c:choose>
                        <c:when test="${zebra eq 'odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${zebra}">
                        <td>
                            <span class="link ${consolidate.fileId eq lclBookingForm.consolidatedId ? 'highlight-saffron' : ''}"
                                  style="color:blue;" onclick ="goToBooking('${path}', '${consolidate.fileId}', 'Exports', 'Booking', 'ConsolidateDesc');">
                                ${consolidate.fileNo}  <%-- ---- > ${consolidate.fileState} --%>
                            </span>
                            <span class="greenBold">
                                &nbsp;&nbsp;
                                <c:out value="${consolidate.fileId eq consolidate.consolidateFile ? '(Parent)': ''}"/>
                            </span>
                        </td>
                        <td>${consolidate.pooName}</td>
                        <td title="${consolidate.clientName}/${consolidate.clientAcctNo}">
                            ${consolidate.clientName}
                        </td>
                        <td>
                            <c:if test="${consolidate.fileStatus eq 'L' || consolidate.fileStatus eq 'M'}">
                                <c:out value="Loaded"/>
                            </c:if>
                            <c:if test="${consolidate.fileStatus eq 'RF'}">
                                <c:out value="Refused"/>
                            </c:if>                                            
                            <c:if test="${consolidate.fileStatus eq 'B'}">
                                <c:out value="Booking"/>
                            </c:if>
                            <c:if test="${consolidate.fileStatus eq 'R'}">
                                <c:out value="Released"/>
                            </c:if>
                            <c:if test="${consolidate.fileStatus eq 'W'}">
                                ${consolidate.dispoStatus eq 'RCVD' ? "WAREHOUSE(Verified)" :"WAREHOUSE(Un-verified)"}
                            </c:if>
                            <c:if test="${consolidate.fileStatus eq 'PR'}">
                                <c:out value="WAREHOUSE(Pre Release)"/>
                            </c:if>
                        </td>
                        <td>${consolidate.bkgSailDate}</td>
                        <td>${consolidate.piece}</td>
                        <td>${consolidate.weightImpLbs}</td>
                        <td>${consolidate.weightMetricKgs}</td>
                        <td>${consolidate.volumeImpcft}</td>
                        <td>${consolidate.volumeMetricCbm}</td>
                        <td>
                            <c:if test="${consolidate.fileId ne consolidate.consolidateFile}">
                                <img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                     onclick="deConsolidated('${consolidate.fileId}', '${consolidate.fileNo}');"
                                     title="Delete consolidate"/>
                                <c:if test="${consolidate.fileState ne 'BL'}">
                                    <img src="${path}/images/parentDrLogo.jpg" style="cursor:pointer" id="${consolidate.fileNo}" width="16" height="16" alt="delete" 
                                         onclick="makeParent(this, '${consolidate.fileId}');"  title="Make Parent"/>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </tbody>
    </table>
</div>
<script>
    var path = "/" + window.location.pathname.split('/')[1];
    jQuery(document).ready(function () {
        var count = $('#mytbody tr').length;
        if (count >= 1) {
            checkConsolidate();
        }
        if ($("#methodName").val() === "deleteConsolidate" && count === 0) {
            emptyConsolidate();
        }
    });
    function checkConsolidate() {
        if ($("#roleDutyForBatchCode").val() === 'true') {
            $("#consolidateHsCodes").show();
        }
        if($("#roleDutyForHotCode").val() === 'true'){
            $("#consolidateHotCodes").show();
        }
        $(".tab-box a.isconsolidate ").css({
            "background": "url(" + path + "/jsps/LCL/images/tab-bg-green.gif) no-repeat right top ",
            "color": "white"
        });
        $(".tab-box a.isconsolidate  span").css({
            "background": "url(" + path + "/jsps/LCL/images/tab-left-bg-green.gif) no-repeat left top "
        });
    }
    function emptyConsolidate() {
        $("#consolidateHsCodes").hide();
        $("#consolidateHotCodes").hide();
        $(".tab-box a.isconsolidate ").css({
            "background": "",
            "color": "blue",
            "addClass": "activeLink"
        });
        $(".tab-box a.isconsolidate span").css({
            "background": "",
            "addClass": "activeLink"
        });
    }

    function goToBooking(path, fileId, moduleName, toScreen, fromScreen) {
        var fromFileId = $("#fileNumberId").val();
        var fileState = getfileNumberState(fileId);
        window.parent.goToConsolidatePage(path, fileId, fileState, moduleName, toScreen, fromScreen, fromFileId);
    }
    function deConsolidated(id, fileNo) {
        if ($("#fileType").val() !== "BL") {
            deleteConsolidate(id, fileNo, "", "");
        } else {
            var txt = "This change will not impact the B/L cube/weight/charges since it is already COB";
            var current_file_Id = $("#fileNumberId").val();
            var cob = $("#cob").val();
            if (cob === 'true') {
                cobYes(txt, id, fileNo, current_file_Id);
            } else {
                deleteConsolidate(id, fileNo, "", current_file_Id);
            }
        }
    }

    function cobYes(txt, id, fileNo, current_file_Id) {
        var remarks = '';
        $.prompt(txt, {
            buttons: {Confirm: 1, Cancel: 2},
            submit: function (v) {
                if (v === 1) {
                    remarks = "Accepted rates warning after COB.";
                    submitConsolidation(id, fileNo, remarks, "");
                } else if (v === 2) {
                    remarks = "Rates warning after COB not accepted.";
                }
                ratesNotAccepted(current_file_Id, remarks);
            }
        });
    }

    function makeParent(ele, fileA) {
        var currentFileId = $("#fileNumberId").val();
        var txt = "Are you sure you want to make <span style='color:red;'>DR " + ele.id + " </span> the Parent for this consolidation?";
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    $("#methodName").val("makeDRasParentDR");
                    var params = $("#lclBookingForm").serialize();
                    params += "&fileA=" + fileA + "&currentFileId=" + currentFileId;
                    $.post($("#lclBookingForm").attr("action"), params, function (data) {
                        if (data !== null) {
                            $("#consolidateDesc").html(data);
                            $("#consolidateDesc", window.parent.document).html(data);
                            hideProgressBar();
                        }
                    });
                } else {
                    $.prompt.close();
                }
            }
        });
    }
</script>

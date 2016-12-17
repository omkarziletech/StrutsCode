<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/taglib.jsp" %>
<%@include file="init.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <input type="hidden" id="fileNumber" value="${lclAddVoyageForm.fileNumber}"/>
        <c:if test="${not empty consolidateList}">
            <c:set value="This Dr is consolidated with the following DRs:" var="label1"/>
            <c:set value="Would you like to pick these in the same unit?" var="label2"/>
        </c:if>
        <table  width="100%">
            <tr><td  width="100%" class="tableHeadingNew">Consolidated List For File# ${lclAddVoyageForm.fileNumber}</td></tr>
        </table>
        <span class="red">${label1}</span>
        <table class="dataTable" style="padding-top: 0.5em;">
            <thead>
            <th>File#</th>
            <th>Status</th>
            <th>Pieces</th>
            <th>Cutft</th>
            <th>Pounds</th>
            <th>Curr loc</th>
            <th>Pol</th>
            <th>Dispo</th>
            <th>Remarks</th>
            <th>Hot code</th>
            <th>Whsloc</th>
        </thead>
        <c:forEach items="${consolidateList}" var="consolidate">
            <c:set var="zebra" value="${zebra=='odd' ? 'even' : 'odd'}"/>
            <input type="hidden" value="${consolidate.fileId}" class="consolidateFile"/>
            <tbody>
                <tr class="${zebra}">
                    <td>${consolidate.fileNo}</td>
                    <td> 
                        <c:choose>
                            <c:when test ="${consolidate.haz}">
                                <img src="${path}/img/icons/danger..png"  style="cursor:pointer" width="12" 
                                     height="12" alt="Haz" title="Hazardous"/>${consolidate.status}
                            </c:when>
                            <c:otherwise>${consolidate.status}</c:otherwise>
                        </c:choose> 
                    </td>
                    <td>${consolidate.totalPieceCount}</td>
                    <td>${consolidate.totalVolumeImperial}</td>
                    <td>${consolidate.totalWeightImperial}</td>
                    <td>${consolidate.curLoc}</td>
                    <td>${consolidate.pol}</td>
                    <td>${consolidate.dispo}</td>
                    <td>${consolidate.remarks}</td>
                    <td>${consolidate.hotCodes}</td>
                    <td>${consolidate.warehouseLine}</td>
                </tr>
            </tbody>
        </c:forEach>
        <tr><td></br></td></tr>
        <tr>
            <td colspan="11">
                <span class="redBold11px">${label2}</span>
            </td>
        </tr>
    </table>
    <table>
        <tr>
            <td width="70%" style="padding-top:2em;"></td>
            <td>
                <input type="button" class="button-style1" value="Yes" onclick="selectToConsolidateDr();"/>
            </td>
            <td><input type="button" class="button-style1" value="No" onclick="closePopUp();"/></td>
            <td><input type="button" class="button-style1" value="Cancel" onclick="cancelProcess('${lclAddVoyageForm.fileNumberId}');"/></td>
        </tr>
    </table>

    <table>
        <tr>
            <td style="padding-top:1.5em;"><span class="redBold11px">NOTE : </span></td>
            <td style="padding-top:1.5em;">
                <span class="bold">The following DRs are also part of the consolidation but are NOT released: DR#</span>
                <span class="red" style="font-size:13px;">${nonReleaseDrList}</span>
            </td>

        </tr>
        <tr>
            <td style="padding-top:.5em;"></td>
            <td style="padding-top:.5em;">
                <span class="bold">The following DRs are also part of the consolidation but are booked for another voyage: DR#</span>
                <span class="red" style="font-size:13px;">${bookedAnotherVoyageList}</span>
            </td>
        </tr>
    </table>
</body>
</html>
<script type="text/javascript">
    function selectToConsolidateDr() {
        $(".consolidateFile").each(function () {
            var fileId = $(this).val();
            parent.$("#" + fileId).attr("checked", true);
            parent.$("#" + fileId).attr("disabled", true);
            parent.$("#consolidateIcon" + fileId).hide();
        });
        closePopUp();
        parent.showCalcultedCftLbsPopUp();
    }

    function cancelProcess(fileId) {
        parent.$("#" + fileId).attr("checked", false);
        parent.showCalcultedCftLbsPopUp();
        closePopUp();
    }

    function closePopUp() {
        parent.$.fn.colorbox.close();
    }
</script>
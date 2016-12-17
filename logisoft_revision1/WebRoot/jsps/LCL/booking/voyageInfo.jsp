<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table border = "0">
    <c:if test="${not empty voyagInfoList}">
        <c:set var="index" value="-1"/>
        <c:forEach var="voyageInfo" items="${voyagInfoList}" varStatus ="count">
            <input type="hidden" id="scheduleNo${count.index}" value="${voyageInfo.scheduleNo}"/>
            <input type="hidden" id="unitNo${count.index}" value="${voyageInfo.unitNo}"/>
            <input type="hidden" id="origin${count.index}" value="${voyageInfo.origin}"/>
            <input type="hidden" id="destination${count.index}" value="${voyageInfo.destination}"/>
            <c:if test="${count.index == 0}">
                <input type="hidden" id="currentIndex" value="${count.index}"/>
                <c:set var="scheduleNo" value="${voyageInfo.scheduleNo}"/>
                <c:set var="unitNo" value="${voyageInfo.unitNo}"/>
                <c:set var="origin" value="${voyageInfo.origin}"/>
                <c:set var="destination" value="${voyageInfo.destination}"/>
            </c:if>
            <c:set var="index" value="${index+1}"/>
        </c:forEach>
        <input type="hidden" id="totalIndex" value="${index}"/>
    </c:if>
                <tr><td class="textlabelsBoldforlcl">Voyage#:</td><td class="greenBold"><label class="scheduleNo">${scheduleNo}</label>
            <c:if test="${fn:length(voyagInfoList) > 1}">
                <span title="Next Picked Voyage Details"><img src='${path}/img/icons/astar.gif' width='12' height='12' onclick='getNextVoyage()'/></span>
            </c:if>
        </td></tr>
    <tr><td class="textlabelsBoldforlcl">Unit#:</td><td class="greenBold unitNo">${unitNo}</td></tr>
    <tr><td class="textlabelsBoldforlcl">Origin:</td><td class="greenBold origin">${origin}</td></tr>
    <tr><td class="textlabelsBoldforlcl">Destination:</td><td class="greenBold destination">${destination}</td></tr>
</table>

<script>
    function getNextVoyage() {
        var index = parseInt($("#currentIndex").val()) + 1;
        var totalindex = parseInt($("#totalIndex").val());
        $("#currentIndex").val(index);
        if (index > totalindex) {
            $("#currentIndex").val(0);
            index = 0;
        }
        $('.scheduleNo').text($('#scheduleNo' + index).val());
        $('.unitNo').text($('#unitNo' + index).val());
        $('.origin').text($('#origin' + index).val());
        $('.destination').text($('#destination' + index).val());
    }
</script>
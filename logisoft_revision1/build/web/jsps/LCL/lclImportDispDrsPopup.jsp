<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="unitId" name="unitId"  />
    <cong:hidden id="postFlag" name="postFlag"  />
    <cong:hidden id="unitssId" name="unitssId"  />
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="15%">
                View DRs
            </td>
            <td width="5%"><span class="blackBold"> UNIT# </span></td>
            <td width="25%"><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span></td>
            <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
            <td width="10%"><span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span></td>
                <cong:td width="40%" align="left">
            <input type="button" value="Confirm and Send" align="center" class="button-style1"
                   onclick="manifest('CS','${noAlertFlag}');"/>
            <input type="button" value="Confirm and No Send" align="center" class="button-style1" disabled="true"
                   onclick="manifest('CNS');"/>
            <input type="button" value="Abort" id="closePopup" align="center" class="button-style1" onclick="abortDispDrsPopup();"/>
        </cong:td>
    </tr>
</table>
<cong:table width="100%" border="0">
    <cong:tr>
        <cong:td width="30%"></cong:td>

    </cong:tr>

</cong:table>
<div style="width:100%;overflow:auto;height:280px" align="center" id="drDiv">
    <table border="1" id="manifestDr" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
        <tr class="tableHeading2">
            <td width="4%">File#</td>
            <td width="5%">Tot Collect chgs</td>
            <td width="5%">Tot IPI</td>
            <td width="8%">Consignee</td>
            <td width="8%">Notify</td>
            <td width="4%">Bill To Party</td>
            <td width="5%">Destination</td>
            <td width="2%">T/S</td>
            <td width="31%">Arrival Notice EMail</td>
            <td width="26%">Fax</td>
            <td width="2%">View A/N</td>
        </tr>
        <c:forEach items="${drList}" var="importsManifestBean">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tr>
                <td
                    <c:if test="${importsManifestBean.transShipment=='Yes'}">
                        class="redBold11px"
                    </c:if>
                    >${importsManifestBean.fileNo}</td>
                <td align="left">${importsManifestBean.totalCharges}</td>
                <td align="left" class="greenBold">${importsManifestBean.totalIPI}</td>
                <td>
                    <span title="<strong>${importsManifestBean.consigneeName}<br/>${importsManifestBean.consAcct}</strong>">
                        ${fn:substring(importsManifestBean.consigneeName,0,11)}
                    </span>
                <td>
                    <span title="<strong>${importsManifestBean.notifyName}<br/>${importsManifestBean.notifyAcct}</strong>">
                        ${fn:substring(importsManifestBean.notifyName,0,11)}
                    </span>
                </td>
                <td>${importsManifestBean.billToParty}</td>
                <td>
                    <span title="<strong>${importsManifestBean.destinationName}/${importsManifestBean.destinationCountry}/${importsManifestBean.destination}</strong>">
                        ${importsManifestBean.destination}
                    </span>
                </td>
                <td>${importsManifestBean.transShipment}</td>
                <td>
                    <span title="${fn:replace(importsManifestBean.consigneeEmail,',','<br/>')} <br/> ${fn:replace(importsManifestBean.notifyEmail,',','<br/>')}">
                        <c:set var="emails" value="${importsManifestBean.consigneeEmail} ${importsManifestBean.notifyEmail}" />
                        ${fn:substring(emails,0,40)}

                    </span>
                </td>
                <td>
                    <span title="${fn:replace(importsManifestBean.consigneeFax,',','<br/>')}<br/>${fn:replace(importsManifestBean.notifyFax,',','<br/>')}">
                        <c:set var="fax" value="${importsManifestBean.consigneeFax} ${importsManifestBean.notifyFax}" />
                        ${fn:substring(fax,0,40)}
                    </span>
                </td>
                <td><c:if test="${importsManifestBean.transShipment=='No'}">
                        <span class="hotspot" title="<strong>Preview</strong>">
                            <img id="report" src="${path}/img/icons/search_over.gif" border="0" height="17"
                                 onclick="viewDispPdf('${importsManifestBean.fileId}', '${importsManifestBean.fileNo}', '${path}');"/>
                        </span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<br>
</cong:form>
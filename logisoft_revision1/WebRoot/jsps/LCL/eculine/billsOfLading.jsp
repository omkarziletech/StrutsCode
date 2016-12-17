<%-- 
    Document   : billsOfLading
    Created on : Jul 31, 2013, 12:24:15 PM
    Author     : Rajesh
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<display:table name="${blInfoList}" id="bl" class="display-table" requestURI="/lclEculineEdi.do">
    <c:set var="highlight" value=""/>
    <display:column title="House B/L">
        <c:choose>
            <c:when test="${empty invoiceReq && hblNo eq bl.blNo}">
                <span class="link highlight-saffron" style="cursor: pointer" onclick="openBol('${path}', '${bl.id}', '${bl.blNo}', '${ecu.id}', '${bl.adjudicated}');">
                    ${bl.blNo}
                </span>
            </c:when>
            <c:when test="${invoiceReq eq 'yes' && hblNo eq bl.blNo}">
                <span class="link" style="cursor: pointer" onclick="openBol('${path}', '${bl.id}', '${bl.blNo}', '${ecu.id}', '${bl.adjudicated}');">
                    ${bl.blNo}
                </span>
                <c:set var="highlight" value="highlight-saffron"/>
            </c:when>
            <c:otherwise>
                <span class="link" style="cursor: pointer;"
                      onclick="openBol('${path}', '${bl.id}', '${bl.blNo}', '${ecu.id}', '${bl.adjudicated}');">
                    ${bl.blNo}
                </span>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="AMS SCAC">
        <span title="${bl.amsScac}">${bl.amsScac}</span>
    </display:column>
    <display:column title="ECI Bkg #">
        <c:choose>
            <c:when test="${not empty bl.fileId }">
                <a href="javascript:void(0);" onclick="openFile('${path}', '${bl.fileId}', '${bl.fileNo}');"
                   class="${bl.fileId eq fileId ? 'highlight-saffron' : ''}">
                    ${bl.fileNo}
                </a>
                <c:if test="${ecu.approved ne true}">
                    <input type="button" value="Un" class="button-style1" onclick="unLinkDR('${path}', '${bl.id}', '${ecu.voyNo}', '${ecu.containerNo}');"/>
                </c:if>
            </c:when>
            <c:otherwise>
                <input type="button" value="Link DR" class="button-style1" onclick="linkDR('${path}', '${bl.id}', '${bl.blNo}', '${ecu.polUncode}', '${ecu.podUncode}', '${ecu.voyNo}', '${ecu.containerNo}');"/>
            </c:otherwise>
        </c:choose>
    </display:column>
    <c:choose>
        <c:when test="${ bl.invoiceStatus eq '1' }">
            <c:set var="statusColor" value="green" />
        </c:when>
        <c:when test="${ bl.chargesPostedStatus eq '1'}">
            <c:set var="statusColor" value="orange" />
        </c:when>
        <c:otherwise>
            <c:set var="statusColor" value="red" />
        </c:otherwise>
    </c:choose>
    <display:column title="Invoice #">
        <span class="link ${highlight}"  style="cursor: pointer; color: ${statusColor}" onclick="openInvoice('${path}', '${ecu.id}', '${bl.blNo}', '${bl.fileId}', '${bl.cntrId}');">
            ${bl.invoiceNo}
        </span>
    </display:column>
    <display:column title="Shipper">
        <c:choose>
            <c:when test="${not empty bl.shipperNo}">
                <span class="color-green" title="${bl.shipperNo}<br/>${bl.shipper}">${bl.shipperNo}</span>
            </c:when>
            <c:otherwise>
                <span class="color-red" title="Not mapped with trading partner">${fn:substring(bl.shipper,0,15)}</span>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="Consignee">
        <c:choose>
            <c:when test="${not empty bl.consigneeNo}">
                <span class="color-green" title="Mapped successfully">${bl.consigneeNo}</span>
            </c:when>
            <c:otherwise>
                <span class="color-red" title="Not mapped with trading partner">${fn:substring(bl.consignee,0,15)}</span>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="Notify Party">
        <c:choose>
            <c:when test="${not empty bl.notifyNo}">
                <span class="color-green" title="Mapped successfully">${bl.notifyNo}</span>
            </c:when>
            <c:otherwise>
                <span class="color-red" title="Not mapped with trading partner">${fn:substring(bl.notify,0,15)}</span>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="PLOR" property="por"></display:column>
    <display:column title="POL">
        <span title="${bl.origin}">${bl.pol}</span>
    </display:column>
    <display:column title="POD">
        <span title="${bl.destn}">${bl.pod}</span>
    </display:column>
    <display:column title="Delivery" property="delivery"></display:column>
    <display:column title="Pre Carriage" property="preCarriageBy"></display:column>
    <display:column title="Ship terms" property="shipTerms"></display:column>
    <display:column title="Exp">
        <c:choose>
            <c:when test="${not empty bl.expressRelease && bl.expressRelease ne '0'}">Y</c:when>
            <c:otherwise>N</c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="Piece(s)" property="pieces"></display:column>
    <display:column title="Package" property="packType"></display:column>
    <display:column title="Weight">
        <span title="${bl.weight} ${bl.weightUnit}">${bl.weight}</span>
    </display:column>
    <display:column title="Volume">
        <span title="${bl.volume} ${bl.volumeUnit}">
            ${bl.volume}
            <%-- Hidden fields --%>
            <input type="hidden" id="ecuId" value="${ecu.id}" />
            <input type="hidden" id="ecuApproved" value="${ecu.approved}" />
            <input type="hidden" id="packageId${bl_rowNum}" value="${bl.packageId}" />
            <input type="hidden" id="pod${bl_rowNum}" value="${bl.pod}" />
            <input type="hidden" id="pol${bl_rowNum}" value="${bl.pol}" />
            <input type="hidden" id="delivery${bl_rowNum}" value="${bl.delivery}" />
            <input type="hidden" id="blId${bl_rowNum}" value="${bl.id}" />
            <input type="hidden" id="adjudicated${bl_rowNum}" value="${bl.adjudicated}" />
            <input type="hidden" id="unPolCount${bl_rowNum}" value="${bl.unPolCount}" />
            <input type="hidden" id="unPodCount${bl_rowNum}" value="${bl.unPodCount}" />
            <input type="hidden" id="unDelCount${bl_rowNum}" value="${bl.unDelCount}" />
        </span>
    </display:column>
    <display:column title="Notes">
        <img alt="notes" title="Notes" src="${path}/images/icons/notes.png"
             class="notes" onclick="openNotes('${path}', '', '${bl.blNo}', 'bl');"/>
    </display:column>
    <display:column title="Action">
        <c:choose>
            <c:when test="${ecu.approved && bl.approved}">
                <img alt="Approved" title="Approved" src="${path}/images/icons/check-green.png" class="approvedBkg"/>
            </c:when>
            <c:when test="${bl.adjudicated}">
                <img alt="Approved" title="Ready to Approve" src="${path}/images/icons/check-yellow.png" class="approvedBkg"
                     onclick="readyToApproveBl();"/>
            </c:when>
            <c:otherwise>
                <img alt="Approve" title="Not Ready To Approve" src="${path}/images/icons/check-gray.png" class="approveBkg"
                     onclick="approveBkg('${path}', '${bl.id}', '${ecu.voyNo}', '${bl_rowNum}');"/>
            </c:otherwise>
        </c:choose>
    </display:column>
</display:table>

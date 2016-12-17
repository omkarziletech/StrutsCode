<%-- 
    Document   : voyageInfoResults
    Created on : Sep 26, 2015, 12:19:17 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<input type="hidden" id="terminalNo" value="${lclssheader.billingTerminal.trmnum}"/>
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="closeddatetime" value="${lclssheader.closedDatetime}"></fmt:formatDate>
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="reopeneddatetime" value="${lclssheader.reopenedDatetime}"></fmt:formatDate>
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="auditeddatetime" value="${lclssheader.auditedDatetime}"></fmt:formatDate>
<c:choose>
    <c:when test="${not empty lclssheader and not empty lclssheader.closedBy and not empty lclssheader.closedBy.loginName}">
        <c:set var="closed" value="Reopen"/>
        <c:set var="closebutton" value="green-background"/>
    </c:when>
    <c:when test="${not empty lclssheader and not empty lclssheader.reopenedBy and not empty lclssheader.reopenedBy.loginName}">
        <c:set var="closed" value="Close"/>
        <c:set var="closebutton" value="green-background"/>
    </c:when>
    <c:otherwise>
        <c:set var="closed" value=""/>
        <c:set var="closebutton" value="button-style1"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${not empty lclssheader and not empty lclssheader.auditedBy and not empty lclssheader.auditedBy.loginName and lclssheader.auditedDatetime ne null}">
        <c:set var="audited" value="Audited"/>
        <c:set var="auditbutton" value="green-background"/>
        <c:set var="auditremarks" value="${lclssheader.auditedRemarks}"/>
    </c:when>
     <c:when test="${not empty lclssheader and not empty lclssheader.auditedBy and not empty lclssheader.auditedBy.loginName and lclssheader.auditedDatetime eq null}">
       <c:set var="audited" value="Audited"/>
        <c:set var="auditbutton" value="yellow-background"/>
        <c:set var="auditremarks" value="${lclssheader.auditedRemarks}"/>
    </c:when>   
    <c:otherwise>
        <c:set var="audited" value="Audit"/>
        <c:set var="auditbutton" value="button-style1"/>
    </c:otherwise>
</c:choose>
<table align="center" id="ssheaderTable" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
    <tr class="tableHeadingNew">
        <td width="100%">Voyage Information</td>
    </tr>
    <tr>
        <td>
            <div id="divtablesty1" style="width: 100%;">
                <display:table name="${lclssheader}" class="dataTable" id="lclssheader" sort="list" >
                    <display:column title="Bill"  headerClass="sortable">
                        <c:if test="${not empty lclssheader.billingTerminal.trmnum}">
                            ${lclssheader.billingTerminal.trmnum}-${lclssheader.billingTerminal.terminalLocation}</c:if></display:column>
                    <display:column title="POL" headerClass="sortable" class="boldGreen"><span>${originValue}</span></display:column>
                    <display:column title="POD" headerClass="sortable" class="boldGreen"><span>${destinationValue}</span></display:column>
                    <display:column title="File#" property="scheduleNo" headerClass="sortable" class="boldBlue"/>
                    <display:column title="Action">
                        <c:choose>
                            <c:when test="${roleDuty.lclImportVoyageReopen && closed eq 'Reopen'}">
                                <input type="button" align="right" class="${closebutton}" id="closevoyagebutton"
                                       title="Closed By: ${lclssheader.closedBy.loginName}<br/> On: ${closeddatetime}<br/>Remarks: ${lclssheader.closedRemarks}"
                                       value="Reopen" onclick="closeVoyage('${path}');"/>
                            </c:when>
                            <c:when test="${!roleDuty.lclImportVoyageReopen && closed eq 'Reopen'}">
                                <input type="button" align="right" class="gray-background" id="closevoyagebutton"
                                       title="Closed By: ${lclssheader.closedBy.loginName}<br/>On: ${closeddatetime}<br/>Remarks: ${lclssheader.closedRemarks}"
                                       value="Reopen"/>
                            </c:when>
                            <c:when test="${roleDuty.lclImportVoyageClose && closed eq 'Close'}">
                                <input type="button" align="right" class="button-style1yellow" id="closevoyagebutton" onclick="closeVoyage('${path}');"
                                       title="Reopened By: ${lclssheader.reopenedBy.loginName}<br/>On: ${reopeneddatetime}<br/>Remarks: ${lclssheader.reopenedRemarks}"
                                       value="Close"/>
                            </c:when>
                            <c:when test="${!roleDuty.lclImportVoyageClose && closed eq 'Close'}">
                                <input type="button" align="right" class="gray-background" id="closevoyagebutton"
                                       title="Reopened By: ${lclssheader.reopenedBy.loginName}<br/>On: ${reopeneddatetime}<br/>Remarks: ${lclssheader.reopenedRemarks}"
                                       value="Close"/>
                            </c:when>
                            <c:when test="${!roleDuty.lclImportVoyageClose}">
                                <input type="button" align="right" class="${closebutton}" id="closevoyagebutton" value="Close" disabled/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" align="right" class="${closebutton}" id="closevoyagebutton" value="Close" onclick="closeVoyage('${path}');"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${roleDuty.lclImportVoyageAudit}">
                                <input type="button" align="right" class="${auditbutton}" id="auditvoyagebutton" onclick="auditVoyage('${path}');"
                                       title="Audited By: ${lclssheader.auditedBy.loginName}<br/>On: ${auditeddatetime}<br/>Remarks: ${auditremarks}"
                                       value="${audited}"/></c:when>
                            <c:otherwise>
                                <input type="button" align="right" class="gray-background" id="auditvoyagebutton"
                                       title="Audited By: ${lclssheader.auditedBy.loginName}<br/>On: ${auditeddatetime}<br/>Remarks: ${lclssheader.auditedRemarks}"
                                       value="${audited}"/></c:otherwise></c:choose>
                        <img align="left" src="${path}/images/edit.png" title="Edit Voyage" id="imgeditheader" width="16" height="16" onclick="editVoyage('${path}', '${lclssheader.id}');"/>
                    </display:column>
                </display:table>
            </div>
        </td>
    </tr>
</table>

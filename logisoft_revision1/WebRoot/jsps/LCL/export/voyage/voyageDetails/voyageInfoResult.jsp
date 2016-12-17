<%-- 
    Document   : voyageInfoResult
    Created on : Jun 19, 2015, 5:22:22 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="closeddatetime" value="${lclssheader.closedDatetime}"></fmt:formatDate>
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="reopeneddatetime" value="${lclssheader.reopenedDatetime}"></fmt:formatDate>
<fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="auditeddatetime" value="${lclssheader.auditedDatetime}"></fmt:formatDate>
<c:choose>
    <c:when test="${not empty lclssheader and not empty lclssheader.closedBy and not empty lclssheader.closedBy.loginName}">
        <c:set var="closed" value="Closed"/>
        <c:set var="closebutton" value="green-background"/>
    </c:when>
    <c:when test="${not empty lclssheader and not empty lclssheader.reopenedBy and not empty lclssheader.reopenedBy.loginName}">
        <c:set var="closed" value="Close"/>
        <c:set var="closebutton" value="green-background"/>
    </c:when>
    <c:otherwise>
        <c:set var="closed" value="Close"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${not empty lclssheader and not empty lclssheader.auditedBy and not empty lclssheader.auditedBy.loginName}">
        <c:set var="audited" value="Audited"/>
        <c:set var="auditbutton" value="green-background"/>
    </c:when>
    <c:otherwise>
        <c:set var="audited" value="Audit"/>
        <c:set var="auditbutton" value="button-style1"/>
    </c:otherwise>
</c:choose>
<c:if test="${not empty lclssheader}">
    <cong:table align="center" id="ssheaderTable" cellpadding="0" cellspacing="0" width="100%" border="0" style="border:1px solid #dcdcdc;">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td width="100%" colspan="8">Voyage Information</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td colspan="8">
                <div id="divtablesty1" style="width: 100%;">
                    <display:table name="${lclssheader}" class="dataTable" id="lclssheader" sort="list" >
                        <display:column title="Voyage#" property="scheduleNo" headerClass="sortable" class="boldBlue"/>
                        <c:choose>
                            <c:when test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                                <display:column title="POL" headerClass="sortable" class="boldGreen"><span>${originValue}</span></display:column>
                                <display:column title="POD" headerClass="sortable" class="boldGreen"><span>${destinationValue}</span></display:column>
                            </c:when>
                            <c:otherwise>
                                <display:column title="Origin Terminal" headerClass="sortable" class="boldGreen"><span>${originValue}</span></display:column>
                                <display:column title="Destination Terminal" headerClass="sortable" class="boldGreen"><span>${destinationValue}</span></display:column>
                            </c:otherwise>
                        </c:choose>
                        <display:column title="Cut off" property="cutOffDays"  headerClass="sortable"/>
                        <c:choose>
                            <c:when test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
                                <display:column title="TT(POL-POD)" property="totalTransitTime" headerClass="sortable"/>
                                <display:column title="Elite/BS Voy#"  headerClass="sortable">
                                    <span>
                                        ${eliteVoyNum}
                                    </span>
                                </display:column>
                            </c:when>
                            <c:otherwise>
                                <display:column title="TT(Origin-Destination)" property="totalTransitTime" headerClass="sortable"/>
                            </c:otherwise>
                        </c:choose>
                        <display:column title="Action">
                            <img align="left" src="${path}/images/edit.png" id="imgeditheader" width="16" height="16" onclick="editVoyage('${path}','${lclssheader.id}')"/>

                            <c:choose>
                                <c:when test="${closed eq 'Closed'}">
                                    <input type="button" align="right" class="${closebutton}" id="closevoyagebutton"
                                           title="Closed By: ${lclssheader.closedBy.loginName}<br/> On: ${closeddatetime}<br/>Remarks: ${lclssheader.closedRemarks}"
                                           value="Closed"
                                           <c:if test="${roleDuty.voyageCloseAuditUndo}">
                                        onclick="closeVoyage('${path}'); disabled
                                    </c:if>
                                    "/>
                                </c:when>
                                <c:when test="${closed eq 'Close'}">
                                    <input type="button" align="right" class="button-style1" id="closevoyagebutton" onclick="closeVoyage('${path}');"
                                           <c:if test="${not empty lclssheader and not empty lclssheader.reopenedBy and not empty lclssheader.reopenedBy.loginName}">
                                            title="Reopened By: ${lclssheader.reopenedBy.loginName}<br/>On: ${reopeneddatetime}<br/>Remarks: ${lclssheader.reopenedRemarks}"
                                           </c:if>
                                    value="Close"/>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                            <input type="button" align="right" class="${auditbutton}" id="auditvoyagebutton"
                                    <c:if test="${not empty lclssheader and not empty lclssheader.auditedBy and not empty lclssheader.auditedBy.loginName}">
                                   title="Audited By: ${lclssheader.auditedBy.loginName}<br/>On: ${auditeddatetime}<br/>Remarks: ${lclssheader.auditedRemarks}"
                                    </c:if>
                                   value="${audited}"
                                   <c:if test="${roleDuty.voyageCloseAuditUndo || audited=='Audit'}">
                                onclick="auditVoyage('${path}');"
                            </c:if>                         
                              />
                            <div id="arInvoice" align="left" class="button-style1 invoice" onclick="openLclVoyageArInvoice('${path}','${lclssheader.id}','${lclssheader.scheduleNo}',true)">
                                AR Invoice
                            </div>
                            <input type="button" class="button-style1" id="notes"
                                   value="Notes"  onclick="openHeaderNotes('${path}','${lclssheader.id}','${lclssheader.scheduleNo}')"/>
                           <div  class="button-style1" id="printEmailVoyage" onclick="printEmailVoyage('${path}','${lclssheader.id}','${lclssheader.scheduleNo}')">
                             House B/Ls Batch
                            </div>
                          </display:column>      
                    </display:table>
                </div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <br/>
</c:if>
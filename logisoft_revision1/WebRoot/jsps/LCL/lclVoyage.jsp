<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:if test="${not empty voyageList}">
    <table class="dataTable" border="0">
        <thead>
            <tr>
                <c:if test="${!voyageAction}">
                    <th></th>
                </c:if>
                <th>Vessel</th>
                <th>Line</th>
                <th>ECI Voy</th>
                <th>SSL Voy</th>
                <th>Origin LRD</th>
                <th>Load LRD</th>
                <th>ETD at POL</th>
                <th>Pier</th>
                <th>ETA at POD</th>
                <th>ETA FD</th>
                <th>TT(POL-POD)</th>
                <th>TT(POO-FD)</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="voyage" items="${voyageList}">
                <c:choose>
                    <c:when test="${rowStyle eq 'oddStyle'}">
                        <c:set var="rowStyle" value="evenStyle"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="rowStyle" value="oddStyle"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${rowStyle}">
                    <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="originLrd" value="${voyage.originLrd}"/>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="loadLrd" value="${voyage.loadLrd}"/>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${voyage.sailingDate}"/>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="podAtEta" value="${voyage.podAtEta}"/>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="fdEta" value="${voyage.fdEta}"/>
                    <fmt:formatDate pattern="MMM dd yyyy hh:mm:ss" var="fdEta1" value="${voyage.fdEta}"/>
                    <c:if test="${!voyageAction}">
                        <td>
                            <input type="radio" name="voyageRadio" id="voyageRadio"
                                   value="${voyage.ssHeaderId}==${voyage.voyageNo}==${sailDate}==${voyage.carrierName}==${voyage.vesselName}==${voyage.ssVoyage}==${fdEta}==${originLrd}==${loadLrd}==${voyage.departPier}${voyage.scheduleK}==${voyage.departPier}==${voyage.departSched}==${fdEta1}" onclick="setBkgVoyageDetails();">
                        </td>
                    </c:if>
                    <td>${voyage.vesselName}</td>
                    <td>${voyage.carrierName}</td>
                    <td>${voyage.voyageNo}</td>
                    <td>${voyage.ssVoyage}</td>
                    <td>${originLrd}</td>
                    <td>${loadLrd}</td>
                    <td>${sailDate}</td>
                    <td>${voyage.departPier}&nbsp;${voyage.scheduleK}</td>
                    <td>${podAtEta}</td>
                    <td>${fdEta}</td>
                    <td>${voyage.ttPolPod}</td>
                    <td>${voyage.ttPooFd}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>
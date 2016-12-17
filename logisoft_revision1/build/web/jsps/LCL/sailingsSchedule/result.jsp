<%-- 
    Document   : result
    Created on : Aug 26, 2015, 6:15:35 PM
    Author     : Mei
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
    <tr class="tableHeadingNew">
        <td width="30%">Search Results</td>
        <td width="2%">POL:</td>
        <td width="20%" style="color: green;"><span id="polName">${sailingScheduleForm.polName}</span></td>
        <td width="2%">POD:</td>
        <td style="color: green;"><span id="podName">${sailingScheduleForm.podName}</span></td>
    </tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0" style="border:1px solid #dcdcdc"
       class="tableBorderNew" align="center" id="sailingScheduleTable">
    <tr><td>
            <div id="result-header" class="table-banner green">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${fn:length(sailingScheduleForm.sailingScheduleList)>=1}">
                            ${fn:length(sailingScheduleForm.sailingScheduleList)} files found.
                        </c:when>
                        <c:otherwise>No file found.</c:otherwise>
                    </c:choose>
                </div>
            </div>
            <table class="dataTable">
                <thead  >
                    <tr>
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
                <c:if test="${not empty sailingScheduleForm.sailingScheduleList}">
                    <c:forEach var="voyage" items="${sailingScheduleForm.sailingScheduleList}">
                        <c:choose>
                            <c:when test="${rowStyle eq 'oddStyle'}">
                                <c:set var="rowStyle" value="evenStyle"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rowStyle" value="oddStyle"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${rowStyle}">
                        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="pooLrdt" value="${voyage.originLrd}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="polLrdt" value="${voyage.loadLrd}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${voyage.sailingDate}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="podEta" value="${voyage.podAtEta}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="fdEta" value="${voyage.fdEta}"/>
                        <td>${voyage.vesselName}</td>
                        <td>${voyage.carrierName}</td>
                        <td>${voyage.voyageNo}</td>
                        <td>${voyage.ssVoyage}</td>
                        <td>${pooLrdt}</td>
                        <td>${polLrdt}</td>
                        <td>${polEtd}</td>
                        <td>${voyage.departPier}</td>
                        <td>${podEta}</td>
                        <td>${fdEta}</td>
                        <td>${voyage.ttPolPod}</td>
                        <td>${voyage.ttPooFd}</td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </td></tr>
</table>

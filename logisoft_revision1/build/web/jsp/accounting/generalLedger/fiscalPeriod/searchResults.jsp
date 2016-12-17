<%-- 
    Document   : searchResults
    Created on : Feb 27, 2014, 7:54:21 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/dao" prefix="dao"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="periodMap" scope="request" class="java.util.HashMap"/>
<c:set target="${periodMap}" property="01" value="JANUARY"/>
<c:set target="${periodMap}" property="02" value="FEBRUARY"/>
<c:set target="${periodMap}" property="03" value="MARCH"/>
<c:set target="${periodMap}" property="04" value="APRIL"/>
<c:set target="${periodMap}" property="05" value="MAY"/>
<c:set target="${periodMap}" property="06" value="JUNE"/>
<c:set target="${periodMap}" property="07" value="JULY"/>
<c:set target="${periodMap}" property="08" value="AUGUST"/>
<c:set target="${periodMap}" property="09" value="SEPTEMBER"/>
<c:set target="${periodMap}" property="10" value="OCTOBER"/>
<c:set target="${periodMap}" property="11" value="NOVEMBER"/>
<c:set target="${periodMap}" property="12" value="DECEMBER"/>
<c:set target="${periodMap}" property="AD" value="ADJUSTING"/>
<c:set target="${periodMap}" property="CL" value="CLOSING"/>
<div class="result-container">
    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" style="table-layout: fixed;">
        <colgroup>
            <col span="1" class="width-40px">
            <col span="1" class="width-125px">
            <col span="1" class="width-125px">
            <col span="1" class="width-125px">
        </colgroup>
        <thead>
            <tr>
                <th>Period</th>
                <th>Description</th>
                <th>Starting Date</th>
                <th>Ending Date</th>
                <th>Status</th>
                <th>Notes</th>
            </tr>
        </thead>
        <tbody>
            <c:set var="queryString" value="select"/>
            <c:set var="queryString" value="${queryString}  period as keyName,"/>
            <c:set var="queryString" value="${queryString}  if(count(n.id) > 0, 'Y', 'N') as valueName "/>
            <c:set var="queryString" value="${queryString}from"/>
            <c:set var="queryString" value="${queryString}  fiscal_period f"/>
            <c:set var="queryString" value="${queryString}    left join notes n"/>
            <c:set var="queryString" value="${queryString}      on ("/>
            <c:set var="queryString" value="${queryString}        n.module_id = 'FISCAL_PERIOD'"/>
            <c:set var="queryString" value="${queryString}        and n.module_ref_id = f.period_dis"/>
            <c:set var="queryString" value="${queryString}        and n.note_type = 'manual'"/>
            <c:set var="queryString" value="${queryString}      )"/>
            <c:set var="queryString" value="${queryString}  where f.year = ${fiscalPeriodForm.fiscalYear.year} "/>
            <c:set var="queryString" value="${queryString}group by f.period_dis"/>
            <c:set var="noteMap" value="${dao:getMap(queryString)}"/>
            <c:set var="zebra" value="odd"/>
            <c:forEach var="fiscalPeriod" items="${fiscalPeriodForm.fiscalPeriods}">
                <tr class="${zebra} align-center">
                    <td>${fiscalPeriod.period}</td>
                    <td>${periodMap[fiscalPeriod.period]}</td>
                    <td><fmt:formatDate value="${fiscalPeriod.startDate}" pattern="MM/dd/yyyy"/></td>
                    <td><fmt:formatDate value="${fiscalPeriod.endDate}" pattern="MM/dd/yyyy"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${fn:toUpperCase(fiscalPeriod.status) eq 'OPEN'}">
                                <input type="checkbox" class="switch display-hide" checked value="${fiscalPeriod.periodDis}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" class="switch display-hide" value="${fiscalPeriod.periodDis}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${noteMap[fiscalPeriod.period] eq 'Y'}">
                                <img src="${path}/images/icons/notepad_green.png" title="Notes" onclick="showNotes('${fiscalPeriod.periodDis}');"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${path}/images/icons/notepad_yellow.png" title="Notes" onclick="showNotes('${fiscalPeriod.periodDis}');"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${zebra eq 'odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tbody>
    </table>
</div>
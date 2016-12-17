<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">More Information</td>
            <td>
                <a id="lightBoxClose" href="javascript: closePopUpDiv()">
                    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable">
    <c:choose>
        <c:when test="${not empty moreInfoList}">
            <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" style="border: 1px solid white">
                <thead>
                    <tr>
                        <th>Posted Date</th>
                        <th>GL Period</th>
                        <th style="text-align: right">Invoice Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="moreInfo" items="${moreInfoList}">
                        <tr class="${zebra}">
                            <td><fmt:formatDate pattern="MM/dd/yyyy" value="${moreInfo.postedDate}"/></td>
                            <td>${moreInfo.glPeriod}</td>
                            <c:choose>
                                <c:when test="${fn:contains(moreInfo.amount, '-')}">
                                    <c:set var="amount" value="(${fn:replace(moreInfo.amount,'-','')})"/>
                                    <c:set var="color" value="red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="amount" value="${moreInfo.amount}"/>
                                    <c:set var="color" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <td class="${color}" style="text-align:right;">${amount}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <span class="pagebanner">
                No More Information found.
            </span>
        </c:otherwise>
    </c:choose>
</div>

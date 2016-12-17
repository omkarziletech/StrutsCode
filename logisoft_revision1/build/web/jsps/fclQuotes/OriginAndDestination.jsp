<%-- 
    Document   : OriginAndDestination.jsp
    Created on : Oct 8, 2010, 4:25:08 PM
    Author     : Logiware
--%>

<%@page import="javax.swing.JFileChooser"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty originDestinationList}">
        <table width="100%"
               style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
            <tbody>
                <tr>
                    <td class="lightBoxHeader">
                        ${listHeading}
                    </td>
                    <td>
                        <div style="vertical-align: top">
                            <a id="lightBoxClose" href="javascript: closeOriginDestinationList();">
                                <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                            </a>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <span id="doorOriginDisplay" style="display:none"></span>
        <table cellpadding="2" cellspacing="2" style="width: 98%">
            <tr class="textlabelsBold" style="background:#DCDCDC">
                <td align="left">&nbsp;<input type="checkbox" name="checkAll" id="checkAll" onclick="checkAndUnceckAll('checkAll', 'originDestination');">ALL
                    <c:if test="${ratesFrom != 'quickRates'}">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span id="top10Span"><input type="checkbox" name="top10" id="top10" onclick="selectTopTen(this);">Top 5</span>
                        </c:if>
                        <c:if test="${not empty zip && (enableIms == 'Y' ||  enableIms == 'y')}">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span id="top10Span"><input type="checkbox" name="enableIMS" id="enableIMS">Enable Inland</span>
                        </c:if>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="buttonStyleNew" name="getRates" value="GetRates" onclick="showRateGrid('${route}', '${path}')">
                </td>
            </tr>
        </table>
        <div id="originAndDestinations" style="width: 98%;height:305px;overflow:scroll">
                ${originDestinationList}
                <br><br>
                <tr class="textlabelsBold">
                    <td id="originDestn" colspan="8">
                        ${cyYardMessage}
                    </td>
                </tr>
        </div>
    </c:when>
    <c:otherwise>${destination}</c:otherwise>
</c:choose>

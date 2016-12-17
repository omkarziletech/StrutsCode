<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<div style="width:100%;height:155px;overflow-y:auto;overflow-x:hidden;border-collapse: collapse; border: 1px solid #F8F8FF">
    <c:if test="${not empty lclQuoteHsCodeList}">
        <c:set var="count" value="0"/>
        <c:forEach items="${lclQuoteHsCodeList}" var="hs">
            <c:if test="${not empty hs.noPieces}">
                <c:set var="count" value="1"></c:set>
            </c:if>
        </c:forEach>
        <c:choose>
            <c:when test="${count==1}">
                <display:table  name="${lclQuoteHsCodeList}" id="hsCode" class="dataTable" sort="list" requestURI="/lclQuote.do" >
                    <display:column title="Code">
                        <span style=" cursor: pointer" title="${fn:toUpperCase(hsCode.codes)}">${fn:toUpperCase(fn:substring(hsCode.codes,0,25))}
                        </span>
                    </display:column>
                    <display:column title="Pcs" >
                        <span style="font-size: 10px">${hsCode.noPieces}</span>
                    </display:column>
                    <display:column title="Wgt(KGS)">
                        <span style="font-size: 10px">${hsCode.weightMetric}</span>
                    </display:column>
                    <display:column title="PkgType">
                        <span style="font-size: 10px"> ${hsCode.packageType.description}</span>
                    </display:column>
                    <display:column title="Action">
                        <span style="font-size: 10px">
                            <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                                 onclick="deleteHsCode('Are you sure you want to delete?','${hsCode.id}');"/></span>
                        </display:column>
                    </display:table>
                </c:when>
                <c:otherwise>
                <table class="dataTable">
                    <c:forEach items="${lclQuoteHsCodeList}" var="hsCode">
                        <c:choose>
                            <c:when test="${zebra=='odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:tr styleClass="${zebra}" style="text-transform: uppercase">
                            <cong:td style="width:5%"> <a style=" cursor: pointer" title="${fn:toUpperCase(hsCode.codes)}">
                                    <c:set value="${fn:toUpperCase(hsCode.codes)}" var="code"/>
                                    ${fn:substring(code,0,25)}</a></cong:td>         
                                <cong:td styleClass="floatLeft">
                                <cong:td style="width:10% !important;">
                                <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12"
                                     onclick="deleteHsCode('Are you sure you want to delete?','${hsCode.id}')"/>      
                            </cong:td>
                            </cong:td>
                        </cong:tr>
                    </c:forEach>
                    <cong:td>${hsCode.noPieces} </cong:td>
                    <cong:td>${hsCode.weightMetric} </cong:td>
                    <cong:td> ${hsCode.packageType.description}
                    </cong:td>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>
    <cong:hidden name="custId" id="custId" />
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
</div>

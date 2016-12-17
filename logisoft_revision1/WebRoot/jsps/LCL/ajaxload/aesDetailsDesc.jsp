<%@include file="/taglib.jsp" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<% pageContext.setAttribute("singleQuotes", "'");%>
<% pageContext.setAttribute("doubleQuotes", "\"");%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"></c:set>
<c:set var="fileNumber" value="${param.fileNumber}"></c:set>
<c:if test="${not empty aesDetailsList}">
    <body style="background:#ffffff">
        <cong:div style="width:100%; float:left;">
            <c:set var="index" value='0'/>
            <display:table name="${aesDetailsList}" id="aes" class="dataTable" sort="list" requestURI="/lclBooking.do"  pagesize="50">

                <display:setProperty name="paging.banner.some_items_found">
                    <span class="pagebanner">
                        <font color="blue">{0}</font> Search File details displayed,For more code click on page numbers.
                    </span>
                </display:setProperty>
                <display:setProperty name="paging.banner.one_item_found">
                    <span class="pagebanner">One {0} displayed. Page Number</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.all_items_found"><span class="pagebanner">{0} {1} Displayed, Page Number</span>
                </display:setProperty>
                <display:setProperty name="basic.msg.empty_list">
                </display:setProperty>
                <display:setProperty name="paging.banner.placement" value="bottom" />
                <display:setProperty name="paging.banner.item_name" value="aes"/>
                <display:setProperty name="paging.banner.items_name" value="aes"/>
                <display:column title=""></display:column>
                <c:set var="index" value="${index+1}"></c:set>
                <display:column title="Shipper">${fn:toUpperCase(aes.expnam)}</display:column>
                <display:column title="Consignee">${fn:toUpperCase(aes.connam)}</display:column>
                <display:column title="Transaction Ref#">${aes.trnref}</display:column>
                <display:column title="Unit No">${aes.unitno}</display:column>
                <display:column title="ITN">${aes.itn}</display:column>
                <display:column title="Status">
                    <c:choose>
                        <c:when test="${! empty aes.itn}">
                            <c:out value="Completed"/>
                        </c:when>
                        <c:when test="${aes.status == 'N'}">
                            <c:out value="New"/>
                        </c:when>
                        <c:when test="${aes.status == 'S'}">
                            <c:out value="Sent"/>
                        </c:when>
                        <c:when test="${aes.status == 'C'}">
                            <c:out value="Completed"/>
                        </c:when>
                        <c:when test="${aes.status == 'P'}">
                            <c:out value="Pending"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="Error"/>
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="History">
                    <c:if test="${not empty aes.itnStatus}">
                        <c:set var="aesStatus" value="${fn:toUpperCase(aes.itnStatus)}"/>
                        <c:choose>
                            <c:when test="${fn:contains(aesStatus,'SHIPMENT ADDED') || fn:contains(aesStatus,'SHIPMENT REPLACED')}">
                                <span class="linkSpan" id="link"  style="font-weight: bold;background:#00FF00" title="${aesStatus}"
                                      onclick="submitAesHistory('${path}','${aes.shpdr}');">AES</span>
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'VERIFY')}">
                                <span class="linkSpan"  style="font-weight: bold;background:#00FFFF" title="${aesStatus}"
                                      onclick="submitAesHistory('${path}','${aes.shpdr}');">AES</span>
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'SHIPMENT REJECTED')}">
                                <span class="linkSpan"  style="font-weight: bold;background:#FF0000" title="${aesStatus}"
                                      onclick="submitAesHistory('${path}','${aes.shpdr}');">AES</span>
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'SUCCESSFULLY PROCESSED')}">
                                <span class="linkSpan"  style="font-weight: bold;background:yellow" title="${aesStatus}"
                                      onclick="submitAesHistory('${path}','${aes.shpdr}');">AES</span>
                            </c:when>
                            <c:otherwise>
                                <span class="linkSpan"  style="font-weight: bold;background:#FF0000" title="${aesStatus}"
                                      onclick="submitAesHistory('${path}','${aes.shpdr}');" >AES</span>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </display:column>
                <display:column>
                    <c:choose>
                        <c:when test="${aes.sched}">
                            <input type="button" class="buttonStyleNew schedB" id="schedB" style="background-color: green" onclick="displaySchedBDetails('${path}','${aes.shpdr}','${aes.trnref}')" value="SchedB"/>
                        </c:when>
                        <c:otherwise>
                            <input type="button" class="buttonStyleNew schedB" id="schedB" onclick="displaySchedBDetails('${path}','${aes.shpdr}','${aes.trnref}')" value="SchedB"/>
                        </c:otherwise>
                    </c:choose>
                    <c:set var="aesStatus" value="${fn:toUpperCase(aes.itnStatus)}"/>
                    <c:choose>
                        <c:when test="${roleDuty.resendAes}">
                            <input type="button" class="buttonStyleNew" value="Send AES" onclick="fileSed('${roleDuty.resendAes}','${aes.trnref}','${index}',this,'${aes.shpdr}')" id="aesFileId${index}"
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="buttonStyleNew" value="Send AES" onclick="fileSed('${roleDuty.resendAes}','${aes.trnref}','${index}',this,'${aes.shpdr}')" id="aesFileId${index}"
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:contains(aesStatus,'SHIPMENT ADDED') ||
                                            fn:contains(aesStatus,'SHIPMENT REPLACED')}">
                                    style="background:#00FF00"
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'VERIFY')}">
                                style="background-color:#00FFFF;"
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'SHIPMENT REJECTED')}">
                                style="background-color: #FF0000;"
                            </c:when>
                            <c:when test="${fn:contains(aesStatus,'SUCCESSFULLY PROCESSED')}">
                                style="background-color: yellow"
                            </c:when>
                            <c:when test="${! empty aesStatus}">
                                style="background-color: #FF0000"
                            </c:when>
                            <c:when test="${aes.status == 'N'}">
                                style=""
                            </c:when>
                            <c:when test="${aes.status == 'S' || aes.status == 'P'}">
                                style="background-color: yellow;"
                            </c:when>
                            <c:when test="${aes.status == 'C'}">
                                style="background-color: #008000;"
                            </c:when>
                            <c:otherwise>
                                style=""
                            </c:otherwise>
                        </c:choose>
                        title="Send AES"/>
                </display:column>
                <display:column title="ACTION">
                    <img src="${path}/images/edit.png"  style="cursor:pointer" class="aes" width="16" height="16" alt="edit"
                         onclick="editAes('${path}',${aes.id},'${aes.trnref}','${fileNumber}');"  title="edit AesDetails"/>
                    <c:choose>
                        <c:when test="${aes.status != 'S'}">
                            <img id="deleteAesDetails" src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                 onclick="deleteAesDesc('Are you sure you want to delete?','${aes.id}','${aes.shpdr}');" title="delete AesDetails"/>
                        </c:when>
                    </c:choose>
                </display:column>
                <cong:hidden name="trn" id="trn" />
                <cong:hidden name="shpDr" id="shpDr" value="${aes.shpdr}" />
            </display:table>
        </cong:div>
    </body>
</c:if>

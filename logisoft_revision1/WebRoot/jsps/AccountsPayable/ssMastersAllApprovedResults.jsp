<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${ssMastersApprovedForm.noOfPages > 0}">
            <div style="float:left">
                <c:choose>
                    <c:when test="${ssMastersApprovedForm.totalPageSize>ssMastersApprovedForm.noOfRecords}">
                        ${ssMastersApprovedForm.noOfRecords} out of ${ssMastersApprovedForm.totalPageSize} approved ss masters displayed.
                    </c:when>
                    <c:when test="${ssMastersApprovedForm.noOfRecords>1}">
                        ${ssMastersApprovedForm.noOfRecords} approved ss masters displayed.
                    </c:when>
                    <c:otherwise>1 approved ss master displayed.</c:otherwise>
                </c:choose>
            </div>
            <c:if test="${ssMastersApprovedForm.noOfPages>1 && ssMastersApprovedForm.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png" border="0"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${ssMastersApprovedForm.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                </a>
            </c:if>
            <c:if test="${ssMastersApprovedForm.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${ssMastersApprovedForm.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${ssMastersApprovedForm.pageNo!=pageNo}">
                                <option value="${pageNo}">${pageNo}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${pageNo}" selected>${pageNo}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <a href="javascript: gotoSelectedPage()">
                    <img alt="Go" src="${path}/images/go.jpg" border="0"/>
                </a>
            </c:if>
            <c:if test="${ssMastersApprovedForm.noOfPages>ssMastersApprovedForm.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${ssMastersApprovedForm.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png" border="0"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${ssMastersApprovedForm.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                </a>
            </c:if>
        </c:if>

    </div>
</div>
<div style="width: 100%;float:left">
    <c:choose>
        <c:when test="${not empty ssMastersApprovedForm.ssMastersApprovedList}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="ssMasters" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${ssMastersApprovedForm.orderBy=='desc'}">
                                <c:set var="orderBy" value="asc"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="orderBy" value="desc"/>
                            </c:otherwise>
                        </c:choose>
                        <tr>
                            <th><a href="javascript:doSort('module_name','${orderBy}')"> Module Name </a></th>
                            <th><a href="javascript:doSort('file_no','${orderBy}')"> File/Voyage # </a></th>
                            <th><a href="javascript:doSort('ssline_name','${orderBy}')"> SSL Name </a></th>
                            <th><a href="javascript:doSort('ssline_no','${orderBy}')"> SSL No </a></th>
                            <th><a href="javascript:doSort('agent_name','${orderBy}')"> Destination Agent Name </a></th>
                            <th><a href="javascript:doSort('agent_no','${orderBy}')"> Destination Agent No </a></th>
                            <th><a href="javascript:doSort('etd','${orderBy}')"> ETD </a></th>
                            <th><a href="javascript:doSort('eta','${orderBy}')"> ETA </a></th>
                            <th><a href="javascript:doSort('prepaid_or_collect','${orderBy}')"> Prepaid/Collect </a></th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="ssMasters" items="${ssMastersApprovedForm.ssMastersApprovedList}">
                            <tr class="${zebra}">
                                <td class="uppercase">${ssMasters.moduleName}</td>
                                <td>${ssMasters.fileNo}</td>
                                <td class="uppercase">${ssMasters.sslineName}</td>
                                <td class="uppercase">${ssMasters.sslineNo}</td>
                                <c:choose>
                                    <c:when test="${ssMasters.prepaidOrCollect=='C-Collect'}">
                                        <td class="uppercase">
                                            ${ssMasters.agentName}
                                        </td>
                                        <td class="uppercase">
                                            ${ssMasters.agentNo}
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                                <td>${ssMasters.etd}</td>
                                <td>${ssMasters.eta}</td>
                                <td>${ssMasters.prepaidOrCollect}</td>
                                <td>
                                     <c:choose>
                                        <c:when test="${ssMasters.moduleName == 'LCLE'}">
                                            <img alt="Convert to AP" title="Convert to AP"
                                                src="${path}/images/icons/forward.png" onclick="convertLclSsMasterToAp('${ssMasters.headerId}','${ssMasters.bookingNumber}','${ssMasters.sslineNo}','${ssMasters.fileNo}','${ssMasters.etd}','SSMastersAll','${ssMasters.ssMasterId}','${ssMasters.ssMasterBl}')"/>
                                           <img alt="Notes" title="Notes" src="${path}/images/notepad_yellow.png"
                                                onclick="openHeaderNotes('${path}','${ssMasters.headerId}','${ssMasters.fileNo}')"/>
                                           <img alt="Remove" title="Remove" src="${path}/images/trash.png" onclick="removeLclSsFile('${ssMasters.headerId}','${ssMasters.bookingNumber}',this)"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img alt="Convert to AP" title="Convert to AP"
                                                 src="${path}/images/icons/forward.png" onclick="convertToAp('${ssMasters.fileNo}','SSMastersAll')"/>
                                            <img alt="Notes" title="Notes" src="${path}/images/notepad_yellow.png"
                                                 onclick="showNotes('${notesConstants.FILE}','${ssMasters.fileNo}')"/>
                                            <img alt="Remove" title="Remove" src="${path}/images/trash.png" onclick="removeFile('${ssMasters.fileNo}',this)"/>
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
        </c:when>
        <c:otherwise>
            No approved ss master Found
        </c:otherwise>
    </c:choose>
</div>
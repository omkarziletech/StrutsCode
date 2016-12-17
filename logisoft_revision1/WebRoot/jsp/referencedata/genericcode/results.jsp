<%-- 
    Document   : results
    Created on : Apr 22, 2014, 4:30:38 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty genericCodeForm.genericCodes}">
        <div id="result-header" class="table-banner green">
            <div class="float-right">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${genericCodeForm.totalRows gt genericCodeForm.selectedRows}">
                            ${genericCodeForm.selectedRows} records displayed. ${genericCodeForm.totalRows} records found.
                        </c:when>
                        <c:when test="${genericCodeForm.selectedRows gt 1}">${genericCodeForm.selectedRows} records found.</c:when>
                        <c:otherwise>1 record found.</c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${genericCodeForm.totalPages gt 1 and genericCodeForm.selectedPage gt 1}">
                    <a href="javascript: void(0)" onclick="paging('1');">
                        <img title="First page" src="${path}/images/first.png"/>
                    </a>
                    <a href="javascript: void(0)" onclick="paging('${genericCodeForm.selectedPage-1}');">
                        <img title="Previous page" src="${path}/images/prev.png"/>
                    </a>
                </c:if>
                <c:if test="${genericCodeForm.totalPages gt 1}">
                    <select id="selectedPageNo" class="dropdown float-left">
                        <c:forEach begin="1" end="${genericCodeForm.totalPages}" var="selectedPage">
                            <c:choose>
                                <c:when test="${genericCodeForm.selectedPage eq selectedPage}">
                                    <option value="${selectedPage}" selected>${selectedPage}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${selectedPage}">${selectedPage}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <a href="javascript: void(0)" onclick="paging('');">
                        <img title="Goto Page" src="${path}/images/go.jpg"/>
                    </a>
                </c:if>
                <c:if test="${genericCodeForm.totalPages gt genericCodeForm.selectedPage}">
                    <a href="javascript: void(0)" onclick="paging('${genericCodeForm.selectedPage + 1}');">
                        <img title="Next page" src="${path}/images/next.png"/>
                    </a>
                    <a href="javascript: void(0)" onclick="paging('${genericCodeForm.totalPages}');">
                        <img title="Last page" src="${path}/images/last.png"/>
                    </a>
                </c:if>
            </div>
        </div>
        <div class="result-container" style="width: 100%">
            <table cellpadding="0" cellspacing="1" class="display-table">
                <thead>
                    <tr>
                        <c:forEach var="gcl" items="${genericCodeForm.genericCodeLabels}" varStatus="status">
                            <th>
                                <c:choose>
                                    <c:when test="${status.count gt 2}">
                                        <a href="javascript: void(0)" onclick="sorting('field${status.count - 2}');">${gcl.label}</a>
                                    </c:when>
                                    <c:when test="${status.count eq 2}">
                                        <a href="javascript: void(0)" onclick="sorting('codedesc');">${gcl.label}</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="javascript: void(0)" onclick="sorting('code');">${gcl.label}</a>
                                    </c:otherwise>
                                </c:choose>
                            </th>
                        </c:forEach>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="genericCode" items="${genericCodeForm.genericCodes}">
                        <tr class="${zebra}">
                            <c:forEach var="gcl" items="${genericCodeForm.genericCodeLabels}" varStatus="status">
                                <td>
                                    <c:choose>
                                        <c:when test="${gcl.label eq 'Code' and genericCodeForm.codeType.editable}">
                                            <a href="javascript: void(0)" onclick="edit('${genericCode.id}')">${genericCode.code}</a>
                                        </c:when>
                                        <c:when test="${gcl.fieldType eq 'textarea'}">
                                            <div class="textarea-div text-case-none width-320px">${genericCode[gcl.fieldName]}</div>
                                        </c:when>
                                        <c:otherwise>
                                            ${genericCode[gcl.fieldName]}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                            <td>
                                <img src="${path}/images/icons/preview.png" title="View" onclick="view('${genericCode.id}')"/>
                                <c:if test="${genericCodeForm.codeType.description eq 'Print Comments' 
                                              or genericCodeForm.codeType.description eq 'AES Commodity Code'
                                              or genericCodeForm.codeType.description eq 'Pre-Defined Remarks FCLE'
                                              or genericCodeForm.codeType.description eq 'Pre-Defined Remarks FCLI' 
                                              or genericCodeForm.codeType.description eq 'Pre-Defined Remarks LCLE' 
                                              or genericCodeForm.codeType.description eq 'Pre-Defined Remarks LCLI'}">
                                      <img src="${path}/images/trash.png" title="Delete" onclick="deleteCode('${genericCode.id}', '${genericCode.code}')"/>
                                </c:if>
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
        <div class="table-banner green">No records found</div>
    </c:otherwise>
</c:choose>
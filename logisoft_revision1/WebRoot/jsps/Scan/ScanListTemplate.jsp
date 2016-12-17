<%@page import="javax.swing.JFileChooser"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:set var="rowIndex" value="0"/>
<c:choose>
    <c:when test="${not empty scanSubList1 || not empty scanSubList2}">
        <table width="100%" border="0" cellpadding="0"  cellspacing="0">
            <tr>
                <c:if test="${not empty scanSubList1}">
                    <td valign="top">
                        <display:table name="${scanSubList1}" class="displaytagstyleNew" id="scanSubList1" style="width:100%;">
                            <display:setProperty name="paging.banner.placement" value="none"/>
                            <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"/>
                            <c:if test="${empty scanForm.fileNumber && !empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                <display:column title="Screen Name" property="screenName" sortable="true" headerClass="sortable"/>
                            </c:if>
                            <display:column title="Action">
                                <c:choose>
                                    <c:when test="${!empty scanForm.fileNumber}">
                                        <img alt="Scan" src="${path}/img/icons/printer.gif" border="0" onclick="scan('${rowIndex}');"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Scan</strong>',null,event);"/>&nbsp;&nbsp;
                                        <img alt="Attach" src="${path}/img/icons/attach.gif" border="0" onclick="attachFiles('${rowIndex}');"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Attach</strong>',null,event);"/>&nbsp;&nbsp;
                                        <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>',null,event);"/>
                                        <c:if test="${scanSubList1.totalScan+scanSubList1.totalAttach !='0'}">
                                            <span class="hotspot">
                                                <c:out value="(${scanSubList1.totalScan+scanSubList1.totalAttach})"/>
                                            </span>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${!empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                            <img alt="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="edit('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);"/>&nbsp;&nbsp;
                                            <img alt="Delete" src="${path}/img/icons/delete.gif" border="0" onclick="deleteScan('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="docType${rowIndex}" id="docType${rowIndex}" value="${scanSubList1.documentType}"/>
                                <input type="hidden" name="docName${rowIndex}" id="docName${rowIndex}" value="${scanSubList1.documentName}"/>
                                <input type="hidden" name="docId${rowIndex}" id="docId${rowIndex}" value="${scanSubList1.id}"/>
                                <input type="hidden" name="listScreenName${rowIndex}" id="listScreenName${rowIndex}" value="${scanSubList1.screenName}"/>
                            </display:column>
                            <c:set var="rowIndex" value="${rowIndex+1}"/>
                        </display:table>
                    </td>
                </c:if>
                <c:if test="${not empty scanSubList2}">
                    <td valign="top">
                        <display:table name="${scanSubList2}" class="displaytagstyleNew" id="scanSubList2" style="width:100%;">
                            <display:setProperty name="paging.banner.placement" value="none"/>
                            <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"/>
                            <c:if test="${empty scanForm.fileNumber && !empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                <display:column title="Screen Name" property="screenName" sortable="true" headerClass="sortable"/>
                            </c:if>
                            <display:column title="Action">
                                <c:choose>
                                    <c:when test="${!empty scanForm.fileNumber}">
                                        <img alt="Scan" src="${path}/img/icons/printer.gif" border="0" onclick="scan('${rowIndex}');"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Scan</strong>',null,event);"/>&nbsp;&nbsp;
                                        <img alt="Attach" src="${path}/img/icons/attach.gif" border="0" onclick="attachFiles('${rowIndex}');"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Attach</strong>',null,event);"/>&nbsp;&nbsp;
                                        <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>',null,event);"/>
                                        <c:if test="${scanSubList2.totalScan+scanSubList2.totalAttach !='0'}">
                                            <span class="hotspot">
                                                <c:out value="(${scanSubList2.totalScan+scanSubList2.totalAttach})"/>
                                            </span>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${!empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                            <img alt="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="edit('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);"/>&nbsp;&nbsp;
                                            <img alt="Delete" src="${path}/img/icons/delete.gif" border="0" onclick="deleteScan('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="docType${rowIndex}" id="docType${rowIndex}" value="${scanSubList2.documentType}"/>
                                <input type="hidden" name="docName${rowIndex}" id="docName${rowIndex}" value="${scanSubList2.documentName}"/>
                                <input type="hidden" name="docId${rowIndex}" id="docId${rowIndex}" value="${scanSubList2.id}"/>
                                <input type="hidden" name="listScreenName${rowIndex}" id="listScreenName${rowIndex}" value="${scanSubList2.screenName}"/>
                            </display:column>
                            <c:set var="rowIndex" value="${rowIndex+1}"/>
                        </display:table>
                    </td>
                </c:if>
            </tr>
        </table>
    </c:when>
    <c:otherwise>
        No Documents Found
    </c:otherwise>
</c:choose>
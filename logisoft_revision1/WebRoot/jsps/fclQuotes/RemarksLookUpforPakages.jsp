<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <title>JSP for RemarksLookUpForm form</title>
    </head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <%@include file="../includes/baseResources.jsp"%>
    <script language="javascript" src="${path}/js/common.js"></script>
    <body class="whitebackgrnd">
        <html:form action="/remarksLookUp" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Search</td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" value="Close" onclick="getSearch()">
                        <input type="button" class="buttonStyleNew" value="Add" onclick="addRemarks()">
                    </td>
                    <%--<td align="right"><input type="button" class="buttonStyleNew" value="Add" onclick="add()"></td>--%>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="3" >Remarks
                        <html:text property="remarks" value="" size="40" maxlength="250"></html:text>
                        <input type="button" class="buttonStyleNew" value="Search" onclick="searchRemarks()">

                    </td>
                </tr>
                <tr style="padding-top:10px;">
                    <td colspan="2">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class="tableHeadingNew">
                                <td >
                                    <input type="button" class="buttonStyleNew" value="Submit" onclick="submitRemarks()" style="width:40px;">
			    			  List of Remarks
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="pager" class="pagebanner" align="left">
                                        <c:if test="${!empty totalPages}">
                                            <div style="float:left;">
                                                <c:choose>
                                                    <c:when test="${totalSize>currentPageSize}">
                                                        <c:out value="${totalSize}"/> Remarks Details, Displayed,For more data click on Page Numbers.
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${totalSize==1}">
                                                            <c:out value="${totalSize}"/> Remarks Detail Displayed
                                                        </c:if>
                                                        <c:if test="${totalSize>1}">
                                                            <c:out value="${totalSize}"/> Remarks Details Displayed
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div style="float:right;">
                                                <c:if test="${totalPages>1 && currentPageNo>1}">
                                                    <a href="javascript:gotoRemarksPage('1')">
                                                        <c:out value="First"/>
                                                    </a>
                                                    <a href="javascript:gotoRemarksPage('${currentPageNo-1}')">
                                                        <c:out value="Prev"/>
                                                    </a>
                                                </c:if>
                                                <c:if test="${totalPages>1}">
                                                    <c:forEach begin="1" end="${totalPages}" var="pageNo">
                                                        <c:choose>
                                                            <c:when test="${pageNo!=totalPages}">
                                                                <c:if test="${currentPageNo!=pageNo}">
                                                                    <a href="javascript:gotoRemarksPage('${pageNo}')"><c:out value="${pageNo}"/></a>
                                                                </c:if>
                                                                <span style="float:left;">
                                                                    <c:if test="${currentPageNo==pageNo}">
                                                                        <c:out value="${pageNo}"/>
                                                                    </c:if>
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${currentPageNo!=pageNo}">
                                                                    <a href="javascript:gotoRemarksPage('${pageNo}')"><c:out value="${pageNo}"/></a>
                                                                </c:if>
                                                                <c:if test="${currentPageNo==pageNo}">
                                                                    <c:out value="${pageNo}"/>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </c:if>
                                                <c:if test="${totalPages>currentPageNo}">
                                                    <a href="javascript:gotoRemarksPage('${currentPageNo+1}')">
                                                        <c:out value="Next"/>
                                                    </a>
                                                    <a href="javascript:gotoRemarksPage('${totalPages}')">
                                                        <c:out value="End"/>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="divtablesty1" style="height:80%;">
                                            <c:choose>
                                                <c:when test="${!empty remarksList}">
                                                    <c:set var="index" value="0"/>
                                                    <display:table name="${remarksList}"  class="displaytagstyle"
                                                                   id="remarks" sort="list"   style="width:100%" pagesize="${currentPageSize}" >

                                                        <display:setProperty name="paging.banner.placement" value="none" />
                                                        <display:column title="" style="width:20px;">
                                                            <html:checkbox property="rcheck" styleId="remarksCheck${index}"></html:checkbox>
                                                        </display:column>
                                                        <display:column title="Remarks" maxLength="100">${remarks.codedesc}</display:column>
                                                        <display:column style="display: none;">
                                                            <input type="hidden" id="preRemark${index}" value="${remarks.codedesc}"/>
                                                        </display:column>
                                                        <c:set var="index" value="${index+1}"></c:set>
                                                    </display:table>
                                                </c:when>
                                                <c:otherwise>
                                                    <div>No Records Found</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                </td></tr>
                        </table>
                    </td></tr>
            </table>
            <input type="hidden" id="remarksSize" value="${index}"/>
        </html:form>
    </body>
</html>


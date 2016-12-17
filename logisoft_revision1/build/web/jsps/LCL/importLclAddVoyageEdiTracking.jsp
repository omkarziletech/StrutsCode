<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <%@include file="../includes/baseResources.jsp" %>
        <title>Edi Tracking</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>

    </head>
    <body class="whitebackgrnd" >
        <table width="100%" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">

                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew" >
                        <tr class="tableHeadingNew">
                            <td>Edi Tracking <td>
                        </tr>
                        <tr>
                            <td>
                                <c:if test="${!empty logFileEdiList}">
                                    <display:table name="${logFileEdiList}" class="displaytagstyleNew" pagesize="15"
                                                   id="ediTracking" sort="list" requestURI="/ediTracking.do?">
                                           <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font>Filenames Displayed,For more Data click on Page Numbers.
                                                    <br>
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_items_found">
                                                <span class="pagebanner">
							     			One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
							     			{0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner">
										    No Records Found.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Filename"/>
                                            <display:setProperty name="paging.banner.items_name" value="Filenames"/>
                                        <display:column  property="fileNo" sortable="true" title="FILE NO"></display:column>
                                        <display:column  property="messageType" sortable="true" title="MSG TYPE"></display:column>
                                        <display:column  sortable="true" title="COMPANY">
                                            ${ediCompany}
                                        </display:column>
                                        <display:column  property="filename" sortable="true" title="FILE NAME" maxLength="35">
                                        </display:column>
                                        <display:column  property="bookingNumber" sortable="true" title="BOOKING #"></display:column>
                                        <display:column property="processedDate" title="PROCESSED DATE" sortable="true">
                                        </display:column>
                                        <display:column property="status" title="SI STATUS" sortable="true" ></display:column>
                                        <display:column property="ackReceivedDate" sortable="true" title="ACK RCVD DATE"></display:column>
                                        <display:column property="ackStatus" title="ACK STATUS" sortable="true"></display:column>
                                    </display:table>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>

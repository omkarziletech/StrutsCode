<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
    String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>


        <link href="<%=path%>/css/style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
        <link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
        <link href="<%=path%>/css/style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
        <script language="JavaScript" type="text/javascript" src="<%=path%>/js/cbrte/html2xhtml.js"></script>
        <script language="JavaScript" type="text/javascript" src="<%=path%>/js/cbrte/richtext_compressed.js"></script>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd" >

        <form name="mailForm" action="/emailScheduler"> 

            <script type="text/javascript">
                function cancelDetails() {
                    self.close();
                }
                function showAttachment(file) {
                    window.open('<%=path%>/servlet/FileViewerServlet?fileName=' + file, '_blank', 'width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
                }

                initRTE("<%=path%>/js/cbrte/images/", "<%=path%>/js/cbrte/", "", true);
            </script>

            <table width="100%" class="tableBorderNew" border="0">
                <tr class="tableHeadingNew">Compose Mail<br></tr>
                <tr class="textlabels">
                    <td width="10%"> From </td>
                    <td><c:out value="${emailSchedulerVO.fromAddress}"></c:out> </td>
                    </tr>
                    <tr class="textlabels">
                        <td width="10%"> To </td>
                        <td><c:out value="${emailSchedulerVO.toAddress}"></c:out> </td>
                    </tr>
                    <tr  class="textlabels">
                        <td> CC </td>
                        <td><c:out value="${emailSchedulerVO.ccAddress}"></c:out> </td>
                    </tr>
                    <tr  class="textlabels">
                        <td > BCC </td>
                        <td><c:out value="${emailSchedulerVO.bccAddress}"></c:out> </td>
                    </tr>
                    <tr  class="textlabels">
                        <td > Sent </td>
                        <td><c:out value="${emailSchedulerVO.emailDate}"></c:out> </td>
                    </tr>
                    <tr  class="textlabels">
                        <td>Subject</td>
                        <td><c:out value="${emailSchedulerVO.subject}"></c:out> </td>
                    </tr>
                    <tr  class="textlabels">
                        <td  align="right"> <img src="<%=path%>/img/icons/attachment.png"  width="20" height="20" align="top" style="margin-top:3px;"/></td>
                    <td>
                        <c:choose >
                            <c:when test="${multiFileLocation ne null}" >
                                <c:forEach  var="fileLocation" items="${multiFileLocation}">
                                    <a href="javascript:showAttachment('${fileLocation}');"><c:out value="${fileLocation}"></c:out></a><br>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:forEach  var="file" items="${emailSchedulerVO.fileLocations}">
                                    <a href="javascript:showAttachment('${file}');"><c:out value="${file}"></c:out></a>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr  class="textlabels">
                    <td class="textlabels" valign="top">Message</td>
                    <td>${emailSchedulerVO.htmlMessage}
                    </td>
                </tr>

            </table>
        </form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
<%--
    Document   : voyages
    Created on : Sep 17, 2013, 5:18:47 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Scan Details</title>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@ taglib uri="/WEB-INF/tlds/string.tld" prefix="string"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    </head>
    <body>
        <html:form action="lclScanViewDetails.do" name="lclImportPaymentForm"
                   styleId="lclImportPaymentForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclImportPaymentForm" scope="request" method="post">
            <div class="head-tag" style="position: fixed; width: 100%">
                File Number :
                <span class="green">${lclImportPaymentForm.fileNumber}</span>
            </div>
            <br/>
            <br/>
            <div class="scrolldisplaytable" style="width: 100%;height: 91%;overflow: auto">
                <display:table name="${documentList}"
                               defaultorder="descending" class="dataTable"
                               id="documentListItem" style="width:100%" sort="list" requestURI="/lclScanViewDetails.do" >
                    <display:setProperty name="paging.banner.some_items_found">
                        <span class="pagebanner"> <font color="blue">{0}</font> Scanned/Attached
				Files Displayed,For more Scanned/Attached Files click on Page Numbers. <br>
                        </span>
                    </display:setProperty>
                    <display:setProperty name="paging.banner.one_item_found">
                        <span class="pagebanner"> One {0} Displayed. Page Number </span>
                    </display:setProperty>
                    <display:setProperty name="paging.banner.all_items_found">
                        <span class="pagebanner"> {0} {1} Displayed, Page Number </span>
                    </display:setProperty>
                    <display:setProperty name="basic.msg.empty_list">
                        <span class="pagebanner"> No Records Found. </span>
                    </display:setProperty>
                    <display:setProperty name="paging.banner.placement" value="bottom" />
                    <display:setProperty name="paging.banner.item_name" value="Scanned/Attached Files" />
                    <display:setProperty name="paging.banner.items_name" value="Scanned/Attached Files" />
                    <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"></display:column>
                    <display:column title="File Name" property="fileName" sortable="true" headerClass="sortable" ></display:column>
                    <display:column title="File Size" property="fileSize" sortable="true" headerClass="sortable" ></display:column>
                    <display:column title="Operation" property="operation" sortable="true" headerClass="sortable"></display:column>
                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="operationDate" value="${documentListItem.dateOprDone}"/>
                    <display:column title="Operation Date" sortable="true" headerClass="sortable">
                        <c:out value="${operationDate}"></c:out>
                    </display:column>
                    <display:column title="Status" property="status" sortable="true" headerClass="sortable" >
                        ${documentListItem.documentName =='SS LINE MASTER BL' ? documentListItem.status : ''}
                    </display:column>
                    <display:column title="Comments" sortable="true" headerClass="sortable"><span title="${fn:toUpperCase(documentListItem.comments)}">${fn:toUpperCase(string:splitter(documentListItem.comments,75,'<br>'))}</span></display:column>
                    <display:column title="View">
                        <span class="hotspot" onclick="viewFile('${documentListItem.fileLocation}/${documentListItem.fileName}')" title="Click to View">
                            <img align="middle" src="${path}/img/icons/search_over.gif" border="0" alt="View"/> </span>
                        </display:column>
                    </display:table>
            </div>
        </html:form>
    </body>
</html><script type="text/javascript">
    function viewFile(fileName) {
        window.open ("${path}/servlet/FileViewerServlet?fileName="+fileName, "","resizable=1,location=1,status=1,scrollbars=1, width=1200,height=600,location=no,linemenubar=no,toolbar=no,status=no");
    }
</script>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/tlds/string.tld" prefix="string"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<div class="message"></div>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">
				Document List
            </td>
            <td>
                <div style="vertical-align: top">
                    <a id="lightBoxClose" href="javascript: closeDocList();">
                        <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable" style="width: 100%;height: 91%;overflow: auto">
    <display:table name="${documentList}"
                   defaultorder="descending" class="displaytagstyleNew"
                   id="documentListItem" style="width:100%" sort="list"  >
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
        <display:column title="Document<br/>Name" property="documentName" sortable="true" headerClass="sortable"></display:column>
        <display:column title="<br/>File Name" property="fileName" sortable="true" headerClass="sortable" ></display:column>
        <display:column title="<br/>File Size" property="fileSize" sortable="true" headerClass="sortable" ></display:column>
        <display:column title="<br/>Operation" property="operation" sortable="true" headerClass="sortable"></display:column>
        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="operationDate" value="${documentListItem.dateOprDone}"/>
        <display:column title="Operation</br>Date" sortable="true" headerClass="sortable">
            <c:out value="${operationDate}"></c:out>
        </display:column>
        <display:column title="<br/>Status" property="status" sortable="true" headerClass="sortable" >
            ${documentListItem.documentName =='SS LINE MASTER BL' ? documentListItem.status : ''}
        </display:column>
        <display:column title="<br/>Comments" sortable="true" headerClass="sortable">${string:splitter(documentListItem.comments,75,'<br>')}</display:column>
        <display:column title="<br/>Actions">
            <img align="middle" src="${path}/img/icons/search_over.gif" title="Click to View" onclick="viewFile('${documentListItem.fileLocation}/${documentListItem.fileName}')" >
            <c:if test="${roleDuty.deleteAttachedDocuments}">
                <img alt=""  align ="middle" src="${path}/images/trash.png" title="Click to Delete" onclick="deleteAttachment(this, '${documentListItem.id}', '${documentListItem.fileName}')">
            </c:if>
        </display:column>
    </display:table>
</div>



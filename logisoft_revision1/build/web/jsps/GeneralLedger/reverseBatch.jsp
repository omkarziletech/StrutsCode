<%--
    Document   : reverseBatch
    Created on : Aug 30, 2011, 4:06:00 AM
    Author     : Lakshmi Narayanan
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Reverse Batch - ${batchId}</td>
            <td>
                <a id="lightBoxClose" href="javascript: closePopUpDiv();">
                    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div>
    <table width="100%" cellpadding="3" cellspacing="3" border="0">
        <tr class="textLabelsBold">
            <td>Open Batches</td>
            <td>
                <select name="openBatchId" id="openBatchId" class="dropdown_accounting" style="width: 125px">
                    <c:forEach var="openBatchId" items="${openBatchIds}">
                        <option value="${openBatchId}">${openBatchId}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="button" value="New Batch" class="buttonStyleNew" onclick="reverseToNewBatch()"/>
                <input type="button" value="Old Batch" class="buttonStyleNew" onclick="reverseToOldBatch()"/>
            </td>
        </tr>
    </table>
</div>

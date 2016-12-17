<%-- 
    Document   : viewVoidCorrection
    Created on : Jan 11, 2016, 11:37:18 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Void Correction</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="selectedMenu" name="selectedMenu"/>
            <cong:hidden id="correctionId" name="correctionId"/>
            <cong:hidden id="fileId" name="fileId" />
            <cong:hidden id="fileNo" name="fileNo" />
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="80%">
                        VOIDED Correction List FileNo:- ${lclCorrectionForm.fileNo}
                    </td>
                    <td width="20%">
                    </td>
                </tr>
                <tr>
                    <td width="80%" colspan="2">
                        <div  style="height:80%;">
                            <table class="dataTable">
                                <thead>
                                    <tr>
                                        <th>CN#</th>
                                        <th>User</th>
                                        <th>P</th>
                                        <th>Sail Date</th>
                                        <th>Notice Date</th>
                                        <th>Current Profit</th>
                                        <th>Profit After C/N</th>
                                        <th>C/N Code</th>
                                        <th>Approval</th>
                                        <th>F</th>
                                        <th>P</th>
                                        <th>C-Type</th>
                                        <th>Who Voided</th>
                                        <th>Voided Date</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody style="text-transform: uppercase">
                                    <c:forEach var="correction" items="${correctionList}">
                                        <c:choose>
                                            <c:when test="${zebra eq 'odd'}">
                                                <c:set var="zebra" value="even"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="zebra" value="odd"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${zebra}">
                                            <td>${correction.correctionNo}</td>
                                            <td>${correction.enteredBy.loginName}</td>
                                            <td></td>
                                            <td>${pickedVoyage.etaSailDate}</td>
                                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="createdDate" value="${correction.enteredDatetime}"/>
                                            <td>${createdDate}</td>

                                            <td>${correction.currentProfit}</td>
                                            <td>${correction.profitAfterCN}</td>
                                            <td title="${correction.code.codedesc}">${correction.code.code}</td>

                                            <td style="color: green;font-weight: bold;">${correction.approvedBy.loginName}</td>
                                            <td></td>
                                            <td></td>
                                            <td title="${correction.type.codedesc}">${correction.type.code}</td>

                                            <td>${correction.voidedBy.loginName}</td>
                                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="voidedDate" value="${correction.voidedDate}"/>
                                            <td>${voidedDate}</td>
                                            <td>
                                                <img src="${path}/img/icons/container_obj.gif" border="0" title="View Correction" alt="view"
                                                     onclick="viewVoidCorrection('${correction.id}');"/>
                                                <img src="${path}/img/icons/send.gif" border="0" title="Print/Fax/Email" alt="Print"
                                                     onclick="PrintReportsOpenSeperately('${correction.id}','${correction.correctionNo}','','V')"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </td></tr>
            </table>
            <br/>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    function viewVoidCorrection(correctionId){
        parent.$.prompt.close();
        parent.$("#methodName").val('viewCorrections');
        parent.$('#correctionId').val(correctionId);
        parent.$("#lclCorrectionForm").submit();
    }
</script>

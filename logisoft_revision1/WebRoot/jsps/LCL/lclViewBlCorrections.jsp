<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/tooltip/tooltip.js" ></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="fileId" name="fileId" />
    <cong:hidden id="fileNo" name="fileNo" />
    <cong:hidden id="blNo" name="blNo" />
    <cong:hidden id="noticeNo" name="noticeNo" />
    <cong:hidden id="lastCorrectionNo" name="lastCorrectionNo"/>
    <cong:hidden id="lastApprovedCorrectionNo" name="lastApprovedCorrectionNo"/>
    <cong:hidden id="correctionCount" name="correctionCount"/>
    <cong:hidden id="selectedMenu" name="selectedMenu"/>
    <cong:hidden id="buttonValue" name="buttonValue"/>
    <cong:hidden id="sendDebitCreditNotes" name="sendDebitCreditNotes"/>
    <cong:hidden id="customerAcctNo" name="customerAcctNo"/>
    <cong:hidden id="screenName"  name="screenName"/>
    <cong:hidden id="cfsDevAcctNo" name="cfsDevAcctNo"/>
    <cong:hidden id="cfsDevAcctName" name="cfsDevAcctName"/>
    <cong:hidden id="headerId" name="headerId" value="${auditOrClosedBy.id}"/>
    <cong:hidden id="costAmount" name="costAmount" />
    <input type="hidden" id="consigneeCode"  name="consigneeCode" value="${lclCorrectionForm.constAcctNo}"/>
    <input type="hidden" id="consigneeName"  name="consigneeName" value="${lclCorrectionForm.constAcctName}"/>
    <input type="hidden" id="notifyCode"  name="notifyCode" value="${lclCorrectionForm.notyAcctNo}"/>
    <input type="hidden" id="notifyName"  name="notifyName" value="${lclCorrectionForm.notyAcctName}"/>
    <input type="hidden" id="thirdpartyaccountNo"  name="thirdpartyaccountNo" value="${lclCorrectionForm.thirdPartyAcctNo}"/>
    <input type="hidden" id="thirdPartyname"  name="thirdPartyname" value="${lclCorrectionForm.thirdPartyName}"/>
    <input type="hidden" name="path" value="${path}"/>
    <input type="hidden" name="message" value="${message}"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="75%">
                Add New
            </td>
            <td width="25%">
                <c:choose>
                    <c:when test="${lclCorrectionForm.selectedMenu eq 'Imports' && lclCorrectionForm.buttonValue eq 'quickcn'}">
                        <input type="button" value="Quick CN" align="center" class="button-style1" onclick="addLclBlCorrection();"/>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${empty auditOrClosedBy.auditedBy && empty auditOrClosedBy.closedBy}">
                            <input type="button" value="Add New" align="center" class="button-style1" onclick="addLclBlCorrection();"/>
                        </c:if>
                    </c:otherwise>
                </c:choose>
                <input type="button" value="View Void" align="center" class="${lclCorrectionForm.voidedClassName}"
                       onclick="viewLclBlVoidCorrection('${path}', '${lclCorrectionForm.selectedMenu}');"/>
                <input type="button" value="Notes" align="center" class="button-style1" onclick="displayCorrectionNotesPopUp()"/>
                <input type="button" value="Close" align="center" class="button-style1" onclick="closeCorrection($('#thirdPartyname').val(), $('#thirdpartyaccountNo').val())"/>
            </td>
        </tr>
        <tr>
            <td width="80%" colspan="9">
                <div  style="height:80%;">
                    <div class="table-banner green">
                        <table>
                            <tr><td>
                                    <c:choose>
                                        <c:when test="${fn:length(lclBlCorrectionList)>=1}">
                                            ${fn:length(lclBlCorrectionList)} Corrections found.
                                        </c:when>
                                        <c:otherwise>No Corrections found.</c:otherwise>
                                    </c:choose>
                                </td></tr>
                        </table>
                    </div>
                    <table class="dataTable" id="correctionDisplayTable">
                        <thead>
                            <tr>
                                <th>CN#</th>
                                <th>User</th>
                                <th>P</th>
                                <th>Sail Date</th>
                                <th>Notice Date</th>
                                <c:if test="${lclCorrectionForm.selectedMenu ne 'Imports'}">
                                    <th>Current Profit</th>
                                    <th>Profit After C/N</th>
                                </c:if>
                                <th>C/N Code</th>
                                <th>Approval</th>
                                <th>F</th>
                                <th>P</th>
                                <th>C-Type</th>

                                <th>Who Posted</th>
                                <th>Posted Date</th>
                                <th>Posted</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody style="text-transform: uppercase">
                            <c:forEach var="correction" items="${lclBlCorrectionList}">
                                <c:choose>
                                    <c:when test="${zebra eq 'odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${zebra}" style="border-color:white;">
                                    <td>${correction.noticeNo}</td>
                                    <td>${correction.userName}</td>
                                    <td>${correction.prepaidCollect}</td>
                                    <td>
                                        <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${correction.sailDate}"/>
                                        ${sailDate}
                                    </td>
                                    <td>${correction.noticeDate}</td>
                                    <c:if test="${lclCorrectionForm.selectedMenu ne 'Imports'}">
                                        <td>${correction.currentProfit}</td>
                                        <td>${correction.profitAfterCN}</td>
                                    </c:if>
                                    <td>${correction.correctionCode}</td>
                                    <td style="color: green;font-weight: bold;">
                                        ${correction.approval}</td>
                                    <td></td>
                                    <td></td>
                                    <td>${correction.correctionType}</td>
                                    <td>${correction.whoPosted}</td>
                                    <td>${correction.postedDate}</td>
                                    <td style="color: ${correction.posted eq 'Yes' ? 'green':'red'};
                                        font-weight: bold;">
                                        ${correction.posted}
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${correction.status eq 'Q' || correction.status eq 'A'}">
                                                <img src="${path}/img/icons/container_obj.gif" border="0" title="View"
                                                     onclick="viewLclBlCorrection('${correction.correctionId}', '${auditOrClosedBy.id}');"/>
                                                <c:if test="${not empty correction.postedDate}">
                                                    <c:choose>
                                                        <c:when test="${correction.creditDebit eq 'Y'}">
                                                            <img src="${path}/img/icons/dbcr.jpg" border="0" title="Credit/Debit Note" 
                                                                 onclick="PrintReportsOpenSeperately('${correction.correctionId}', '${correction.noticeNo}', '')"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img title="Corrected Freight Invoices" src="${path}/images/icons/preview.png"
                                                                 onclick="PrintReportsOpenSeperately('${correction.correctionId}', '${correction.noticeNo}', '')"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit" title="Edit"
                                                     onclick="editLclBlCorrection('${correction.correctionId}');"/>
                                                <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"  title="Delete"
                                                     onclick="deleteLclBlCorrection('${correction.correctionId}', '${correction.noticeNo}', '${correction.blNo}');"/>
                                            </c:otherwise>
                                        </c:choose>
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
<script type="text/javascript">
    jQuery(document).ready(function() {
        if (document.lclCorrectionForm.message.value != null && document.lclCorrectionForm.message.value != "") {
            $.prompt(document.lclCorrectionForm.message.value);
            if (document.lclCorrectionForm.message.value.indexOf("Approved") != -1) {
                parent.$('#consigneeCode').val($('#consigneeCode').val());
                parent.$('#consigneeName').val($('#consigneeName').val());
                parent.$('#notifyCode').val($('#notifyCode').val());
                parent.$('#notifyName').val($('#notifyName').val());
                parent.$('#thirdpartyaccountNo').val($('#thirdpartyaccountNo').val());
                parent.$('#thirdPartyname').val($('#thirdPartyname').val());
                parent.$('#methodName').val('editBooking');
                parent.$('#lclBookingForm').submit();
            }
            document.lclCorrectionForm.message.value = "";
        }
        if (document.getElementById('correctionCount').value > 0) {
            parent.document.getElementById('correctionNotice').className = "green-background";
            parent.document.getElementById('correctionNoticeBottom').className = "green-background";
        } else {
            parent.document.getElementById('correctionNotice').className = "button-style1";
            parent.document.getElementById('correctionNoticeBottom').className = "button-style1";
        }
    });
</script>
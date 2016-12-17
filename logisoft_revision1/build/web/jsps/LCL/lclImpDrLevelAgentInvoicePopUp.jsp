<%-- 
    Document   : lclImportDrLevelAgentInvoicePopUp
    Created on : Sep 24, 2014, 5:46:38 PM
    Author     : aravindhan.v
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclVoyageArInvoice.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!--content Start Here-->
<body style="background:#ffffff">
    <form name="lclVoyageArInvoiceForm" id="lclVoyageArInvoiceForm" action="lclVoyageArInvoice.do">
        <input type="hidden" id="methodName" name="methodName">
        <cong:div style="width:100%; float:left;">
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                List Of DR Agent Charges
            </cong:div> </cong:div>

            <table class="display-table" border="0" id="file">
                <thead>
                    <tr>
                        <th></th>
                        <th>File No</th>
                        <th>Agent #</th>
                        <th>Agent Name</th>
                        <th>charge code</th>
                        <th>Agent_Rel_Inv</th>
                    </tr>
                </thead>
                <tbody>
              <c:if test="${not empty arDrLevetChargeList}">
                <c:forEach var="agentChargeList" items="${arDrLevetChargeList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                <tbody>
                    <tr class="${rowStyle}">
                        <td>
                                <input type="checkbox" name="checkAgentCharge" class="checkAgentCharge" value="${agentChargeList.fileId}">
                        </td>
                        <td>${agentChargeList.fileNo}</td>
                        <td>${agentChargeList.agentNo}</td>
                        <td>${agentChargeList.agentName}</td>
                        <td>${agentChargeList.chargeCode}</td>
                        <td>${agentChargeList.agentrelInv}</td>
                    </tr>
                </tbody>
            </c:forEach>
                </c:if>
            </tbody>
        </table>
        <div style="margin-left:30%;">
            <input type="button" class="button-style1" value="Add Released Charge" id="addReleasedCharge"
                   onclick="addReleasedInvoice('save');"/>
            <input type="button" class="button-style1" value="Cancel" id="addReleasedCharge"
                   onclick="canelReleasedInvoice();">
        </div>
    </form>
</body>
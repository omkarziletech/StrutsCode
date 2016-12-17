<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="moduleName" value='${lclVoyageArInvoiceForm.selectedMenu}' />
<c:set var="count" value="0"/>
<c:choose><c:when test="${moduleName eq 'Imports'}">
        <c:set var="shipmentType" value="LCLI"/>
    </c:when>
    <c:otherwise> <c:set var="shipmentType" value="LCLE"/></c:otherwise>
</c:choose>
<body style="background:#ffffff">
    <cong:form name="lclVoyageArInvoiceForm" id="lclVoyageArInvoiceForm" action="lclVoyageArInvoice.do">
        <input type="hidden" id="methodName" name="methodName"/>
        <cong:hidden id="fileNumberId" name="fileNumberId" value="${lclVoyageArInvoiceForm.fileNumberId}"/>
        <cong:hidden id="arRedInvoiceId" name="arRedInvoiceId" value="${lclVoyageArInvoiceForm.arRedInvoiceId}"/>
        <cong:hidden id="agentFlag" name="agentFlag" value="${lclVoyageArInvoiceForm.agentFlag}"/>
        <jsp:useBean id="dbUtil" scope="request" class="com.gp.cong.logisoft.util.DBUtil"/>
        <c:set var="terminalCodeList" value="${dbUtil.terminalCodeList}" scope="request"/>
        <c:if test="${not empty drAgentCharge}">
            <display:table id="agentCharge" class="display-table" name="${drAgentCharge}">
                <c:set var="totAutoAmt" value="0"/>
                <c:set var="autoAgentNo" value=""/>
                <c:set var="autoAgentName" value=""/>
                <c:forEach  var="autochargeAmt" items="${autoAgentChargeList}">
                    <c:set var="totAutoAmt" value="${totAutoAmt+autochargeAmt.totalCharges}"/>
                    <c:set var="autoAgentNo" value="${autochargeAmt.agentNo}"/>
                    <c:set var="autoAgentName" value="${autochargeAmt.agentName}"/>
                </c:forEach>
                <display:column title="Agent#" style="width:100Px;" >
                    <c:choose>
                        <c:when test="${not empty agentCharge.agentNo}">
                            ${agentCharge.agentNo}
                        </c:when>
                        <c:otherwise>
                            ${autoAgentNo}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Agent Name" style="width:200Px;">
                    <c:choose>
                        <c:when test="${not empty agentCharge.agentName}">
                            ${agentCharge.agentName}
                        </c:when>
                        <c:otherwise>
                            ${autoAgentName}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Total Agent Amt(Rels to Inv)" style="width:100Px;" >
                    <c:choose>
                        <c:when test="${not empty agentCharge.agentrelInv}">
                            <input type="hidden" id="totalRelsAmt" value="${totAutoAmt+agentCharge.agentrelInv}"/>${totAutoAmt+agentCharge.agentrelInv}
                        </c:when><c:otherwise>
                            <input type="hidden" id="totalRelsAmt" value=" ${totAutoAmt}"/> ${totAutoAmt}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Total Agent Amt(Not Rels to Inv)" style="width:100Px;" >
                    <c:if  test="${not empty agentCharge.agentrelnotInv}">
                        ${agentCharge.agentrelnotInv}
                    </c:if>
                </display:column>
            </display:table>
        </c:if>
        <cong:table align="center" width="100%" border="0">
            <cong:tr>
                <cong:td width="42%" > </cong:td>
                <cong:td width="50%" >
                    <c:choose>
                        <c:when test="${lclVoyageArInvoiceForm.arRedInvoiceId ne null}">
                            <input type="button" class="button-style1" value="Add Released Charge" id="addReleasedCharge" onclick="createAgentArInvoice('${path}', '${lclVoyageArInvoiceForm.unitNo}', '${lclVoyageArInvoiceForm.agentFlag}','');" />
                        </c:when>
                        <c:otherwise>
                            <input type="button" class="button-style1" value="Create Invoice" id="createInvoice"
                                   onclick="createAgentInvoice('${path}', '${lclVoyageArInvoiceForm.unitNo}', '${lclVoyageArInvoiceForm.agentFlag}');" />
                        </c:otherwise>
                    </c:choose>
                    <input type="button" class="button-style1" value="Abort" id="abort" onclick="abortCurrentPopup();" />
                   <c:if test="${totAutoAmt+agentCharge.agentrelInv > 0 }"> <img src="${path}/jsps/LCL/images/search_over.gif" alt="preview"
                        style="cursor: pointer" onclick="previewArInvoice('${path}', '${lclVoyageArInvoiceForm.agentFlag}');"/></c:if>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:form></body>

<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp"%>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="i" value="0"></c:set>
<display:table name="${listOfcustomers}" class="displaytagstyleNew" pagesize="<%=pageSize%>"  style="width:100%" id="aPPayment" sort="list" >
    <display:setProperty name="paging.banner.some_items_found">
        <span class="pagebanner"><font color="blue">{0}</font> Payables displayed,For more Records click on page numbers.</span>
    </display:setProperty>
    <display:setProperty name="paging.banner.one_item_found">
        <span class="pagebanner">One {0} displayed. Page Number</span>
    </display:setProperty>
    <display:setProperty name="paging.banner.all_items_found">
        <span class="pagebanner">{0} {1} Displayed, Page Number</span>
    </display:setProperty>
    <display:setProperty name="basic.msg.empty_list">
        <span class="pagebanner">No Records Found.</span>
    </display:setProperty>
    <display:setProperty name="paging.banner.placement" value="bottom" />
    <display:setProperty name="paging.banner.item_name" value="Payable"/>
    <display:setProperty name="paging.banner.items_name" value="Payables"/>
    <c:choose>
        <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL && !loginuser.achApprover}">
            <display:column title="<br/>Vendor" sortable="true" property="customer" headerClass="sortable" style="background-color: gray;"/>
            <display:column title="Vendor<br/>Number" sortable="true" property="customerNo" headerClass="sortable" style="background-color: gray;"/>
            <display:column title="Credit<br/>Hold" sortable="true" property="hold" headerClass="sortable" style="background-color: gray;"/>
            <display:column title="Payment<br/>Amount" sortable="true" headerClass="sortable" style="background-color: gray;">
                <c:choose>
                    <c:when test="${fn:contains(aPPayment.balance, '-')}">
                        <span style="color: red">
                            <c:out  value="(${fn:substring(aPPayment.balance,1,fn:length(aPPayment.balance))})"/>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${aPPayment.balance}"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Payment<br/>Method" style="background-color: gray;">
                <select name="paymethod" id="paymethod" style="width: 70px"  class="textlabelsBoldForTextBox">
		    <c:forEach var="labelValueBean" items="${aPPayment.paymentMethods}">
			<option value="${labelValueBean.value}">${labelValueBean.label}</option>
		    </c:forEach>
		</select>
                <input type="hidden" name="paymentMethodArray" id="paymentMethodArray${i}"/>
            </display:column>
            <display:column title="Payment<br/>Date" style="background-color: gray;">
                <input type="text" name="paymentDate" id="txtPaymentDate${i}" style="text-align: center;background-color: gray;"
                       value="${aPPayment.transDate}" size="10" readonly="readonly"/>
            </display:column>
        </c:when>
        <c:otherwise>
            <display:column title="<br/>Vendor" sortable="true" property="customer" headerClass="sortable"/>
            <display:column title="Vendor<br/>Number" sortable="true" property="customerNo" headerClass="sortable"/>
            <display:column title="Credit<br/>Hold" sortable="true" property="hold" headerClass="sortable"/>
            <display:column title="Payment<br/>Amount" sortable="true" headerClass="sortable">
                <c:choose>
                    <c:when test="${fn:contains(aPPayment.balance, '-')}">
                        <span style="color: red">
                            <c:out  value="(${fn:substring(aPPayment.balance,1,fn:length(aPPayment.balance))})"/>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${aPPayment.balance}"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Payment<br/>Method">
		<select name="paymethod" id="paymethod" style="width: 70px"  class="textlabelsBoldForTextBox">
		    <c:forEach var="labelValueBean" items="${aPPayment.paymentMethods}">
			<option value="${labelValueBean.value}">${labelValueBean.label}</option>
		    </c:forEach>
		</select>
                <input type="hidden" name="paymentMethodArray" id="paymentMethodArray${i}"/>
            </display:column>
            <display:column title="Payment<br/>Date">
                <input type="text" name="paymentDate" id="txtPaymentDate${i}"
                       style="text-align: center" value="${aPPayment.transDate}" size="10" readonly="readonly" class="textlabelsBoldForTextBox"/>
            </display:column>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${!loginuser.achApprover}">
            <c:choose>
                <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                    <display:column title="Check<br/>Pay" style="background-color: gray;">
                        <input type="checkbox" name="payCheckBox" checked disabled value="${aPPayment.transactionId}"/>
                    </display:column>
                </c:when>
                <c:otherwise>
                    <display:column title="Check<br/>Pay">
                        <input type="checkbox" name="payCheckBox" value="${aPPayment.transactionId}"/>
                    </display:column>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <display:column title="Check<br/>Pay">
                <c:choose>
                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                        <input type="checkbox" name="payCheckBox" checked value="${aPPayment.transactionId}" onclick="payOrUnPay(this)"/>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="payCheckBox" value="${aPPayment.transactionId}" onclick="payOrUnPay(this)"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${!loginuser.achApprover}">
            <c:choose>
                <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                    <display:column title="Approve<br/>Pay" style="background-color: gray;">
                        <input type="checkbox" name="approveCheckBox" disabled/>
                    </display:column>
                </c:when>
                <c:otherwise>
                    <display:column title="Approve<br/>Pay">
                    </display:column>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <display:column title="Approve<br/>Pay">
                <c:choose>
                    <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL}">
                        <div id="approveCheckBoxdiv${i}">
                            <input type="checkbox" name="approveCheckBox" value="${aPPayment.transactionId}" onclick="approveOrUnApprove(this)"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div id="approveCheckBoxdiv${i}" style="display: none">
                            <input type="checkbox" name="approveCheckBox" value="${aPPayment.transactionId}" onclick="approveOrUnApprove(this)"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </display:column>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${aPPayment.status==commonConstants.STATUS_WAITING_FOR_APPROVAL && !loginuser.achApprover}">
            <display:column title="<br/>Action" style="background-color: gray;">
                <span class="hotspot" onmouseover="tooltip.show('<strong>ShowAll</strong>',null,event);"
                      onmouseout="tooltip.hide();">
                    <img src="${path}/img/icons/showall.gif" border="0" onclick="showDetails('${aPPayment.transactionId}')" />
                </span>
				<img alt="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.AP_PAYMENT}&moduleRefId=${aPPayment.apBatchId}',300,900);"/>
                <input type="hidden" name="customersName" id="customersName" value="${aPPayment.customer}"/>
            </display:column>
        </c:when>
        <c:otherwise>
            <display:column title="<br/>Action">
                <span class="hotspot" onmouseover="tooltip.show('<strong>ShowAll</strong>',null,event);" onmouseout="tooltip.hide();" >
                    <img src="${path}/img/icons/showall.gif" border="0" onclick="showDetails('${aPPayment.transactionId}')" />
                </span>
                <input type="hidden" name="customersName" id="customersName" value="${aPPayment.customer}"/>
            </display:column>
        </c:otherwise>
    </c:choose>
    <c:set var="i" value="${i+1}" />
</display:table>  

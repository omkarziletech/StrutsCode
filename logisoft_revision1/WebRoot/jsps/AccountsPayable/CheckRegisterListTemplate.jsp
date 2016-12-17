<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<c:if test="${!empty checkRegisterList && fn:length(checkRegisterList)>0}">
    <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%">
        <tr class="tableHeadingNew">
            <td>List of Transaction</td>
            <td align="right">
                <c:if test="${canEdit && roleDuty.checkRegisterController}">
                    <input type="button" name='Save' class="buttonStyleNew" id="saveButton" style="width: 60px;" value='Save' onclick="save()"/>
                </c:if>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div align="right" id="pager" class="pagebanner" style="padding: 10px 10px 0 0">
                    <div style="float:right">
                        <c:if test="${!empty totalPages}">
                            <input type="hidden" id="pageNo" value="${currentPageNo}"/>
                            <div style="float:left">
                                <c:choose>
                                    <c:when test="${totalPageSize>currentPageSize}">
                                        ${currentPageSize} out of ${totalPageSize} transactions displayed.
                                    </c:when>
                                    <c:when test="${totalPageSize>1}">
                                        ${totalPageSize} transactions displayed.
                                    </c:when>
                                    <c:otherwise>
                                        1 transaction displayed.
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${totalPages>1 && currentPageNo>1}">
                                <a title="First page" href="javascript: gotoPage('1')">
                                    <img alt="First" src="${path}/images/first.png" border="0"/>
                                </a>
                                <a title="Previous page" href="javascript: gotoPage('${currentPageNo-1}')">
                                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                                </a>
                            </c:if>
                            <c:if test="${totalPages>1}">
                                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                                    <c:forEach begin="1" end="${totalPages}" var="pageNo">
                                        <c:choose>
                                            <c:when test="${currentPageNo!=pageNo}">
                                                <option value="${pageNo}">${pageNo}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${pageNo}" selected>${pageNo}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                                <a href="javascript: gotoPage(document.getElementById('selectedPageNo').value)">
                                    <img alt="Go" src="${path}/images/go.jpg" border="0"/>
                                </a>
                            </c:if>
                            <c:if test="${totalPages>currentPageNo}">
                                <a title="Next page" href="javascript: gotoPage('${currentPageNo+1}')">
                                    <img alt="First" src="${path}/images/next.png" border="0"/>
                                </a>
                                <a title="Last page" href="javascript: gotoPage('${totalPages}')">
                                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                                </a>
                            </c:if>
                        </c:if>
                    </div>
                </div>
                <div id="displayTableDiv" class="scrolldisplaytable" style="float:left">
                    <c:set var="i" value="0"></c:set>
                    <display:table name="${checkRegisterList}" class="displaytagstyleNew" pagesize="${currentPageSize}" style="width:100%;overflow:auto;" id="checkRegisters" sort="external">
                        <display:setProperty name="paging.banner.placement" value="none"/>
                        <display:column property="apBatchId" title="Batch<br>Number" sortable="true" headerClass="sortable" sortName="Ap_Batch_Id"/>
                        <display:column property="chequenumber" title="Check<br>Number" sortable="true" headerClass="sortable" sortName="cast(Cheque_number as signed integer)"/>
                        <display:column property="customer" title="Vendor<br>Name" maxLength="20" sortable="true" headerClass="sortable" 
                                        sortName="Cust_name" style="text-transform:uppercase;"/>
                        <display:column property="customerNo" title="Vendor<br>Number" sortable="true" headerClass="sortable" 
                                        sortName="Cust_no" style="text-transform:uppercase;"/>
                        <display:column property="paymentMethod" title="Payment<br>Method" sortable="true" headerClass="sortable" 
                                        sortName="pay_method" style="text-transform:uppercase;"/>
                        <c:choose>
                            <c:when test="${fn:contains(checkRegisters.amount, '-')}">
                                <display:column  title="Payment<br>Amount" sortable="true" headerClass="sortable" sortName="sum(Transaction_amt)"  style="color:red">
                                    <c:out  value="(${fn:substring(checkRegisters.amount,1,fn:length(checkRegisters.amount))})"/>
                                </display:column>
                            </c:when>
                            <c:otherwise>
                                <display:column  title="Payment<br>Amount" sortable="true" headerClass="sortable" sortName="sum(Transaction_amt)" style="color:black">
                                    <c:out value="${checkRegisters.amount}"/>
                                </display:column>
                            </c:otherwise>
                        </c:choose>
                        <display:column property="transDate" title="Payment<br>Date" sortable="true" headerClass="sortable" sortName="Transaction_date"/>
                        <display:column property="glAcctNo" title="GL<br>Account" sortable="true" headerClass="sortable" sortName="GL_account_number"/>
                        <display:column property="bankAccountNumber" title="Bank<br>Account" sortable="true" headerClass="sortable" sortName="bank_account_no"/>
                        <display:column title="Cleared">
                            <c:choose>
                                <c:when test="${checkRegisters.cleared=='Y'}">
                                    <input type="checkbox" id="cleared${i}"  checked='checked' value="C" disabled="disabled" onclick="checkSelected('cleared',this,'${checkRegisters.transactionId}');"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" id="cleared${i}" value="C" disabled="disabled" onclick="checkSelected('cleared',this,'${checkRegisters.transactionId}');"/>
                                </c:otherwise>
                            </c:choose>
                        </display:column>
                        <display:column property="clearedDate" title="Cleared<br>Date" sortable="true" headerClass="sortable" sortName="cleared_date"/>
                        <display:column property="confirmationNumber" title="Confirmation<br>Number" sortable="true" headerClass="sortable" 
                                        sortName="confirmation_number" style="text-transform:uppercase;">
                        </display:column>
                        <c:choose>
                            <c:when test="${canEdit && roleDuty.checkRegisterController}">
                                <display:column title="Void">
                                    <c:choose>
                                        <c:when test="${checkRegisters.voidTransaction == 'Y'}">
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}" checked="checked" disabled="disabled"/>
                                        </c:when>
                                        <c:when test="${checkRegisters.cleared=='Y' && checkRegisters.paymentMethod!='ACH'}">
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}" disabled="disabled" />
                                        </c:when>
                                        <c:when test="${checkRegisters.status=='tempVoid'}">
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}" checked="checked"
                                                   onclick="checkSelected('void',this,'${checkRegisters.transactionId}');"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}"
                                                   onclick="checkSelected('void',this,'${checkRegisters.transactionId}');">
                                        </c:otherwise>
                                    </c:choose>
                                </display:column>
                                <display:column title="Reprint">
                                    <c:choose>
                                        <c:when test="${checkRegisters.cleared=='Y' || checkRegisters.voidTransaction == 'Y'}">
                                            <input type="checkbox" name="reprint" id="reprint" value="${checkRegisters.transactionId}" disabled="disabled"/>
                                        </c:when>
                                        <c:when test="${checkRegisters.status=='tempReprint'}">
                                            <input type="checkbox" name="reprint" id="reprint" value="${checkRegisters.transactionId}" checked="checked" onclick="checkSelected('reprint',this,'${checkRegisters.transactionId}');"/>
                                        </c:when>
                                        <c:when test="${checkRegisters.status==commonConstants.STATUS_SENT || checkRegisters.status==commonConstants.STATUS_READY_TO_SEND}">
                                            <input type="checkbox" name="reprint" id="reprint" value="${checkRegisters.transactionId}" disabled="disabled"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="reprint" id="reprint" value="${checkRegisters.transactionId}" onclick="checkSelected('reprint',this,'${checkRegisters.transactionId}');"/>
                                        </c:otherwise>
                                    </c:choose>
                                </display:column>
                            </c:when>
                            <c:otherwise>
                                <display:column title="Void">
                                    <c:choose>
                                        <c:when test="${checkRegisters.voidTransaction == 'Y'}">
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}" checked="checked" disabled="disabled"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="void" id="void" value="${checkRegisters.transactionId}" disabled="disabled" />
                                        </c:otherwise>
                                    </c:choose>
                                </display:column>
                                <display:column title="Reprint">
                                    <input type="checkbox" name="reprint" id="reprint" value="${checkRegisters.transactionId}" disabled="disabled"/>
                                </display:column>
                            </c:otherwise>
                        </c:choose>
                        <display:column title="Action">
                            <img alt="" title="Show Details" src="${path}/images/more_details.png" border="0" onclick="showDetails('${checkRegisters.transactionId}')"/>
                            <c:if test="${canEdit}">
                                <img alt="" title="Print" src="${path}/img/icons/printer.gif" border="0" onclick="print('${checkRegisters.chequenumber}','${checkRegisters.customerNo}','${checkRegisters.transDate}','${checkRegisters.paymentMethod}','${checkRegisters.transactionId}');"/>
                                <img alt="" title="Export to Excel" src="${path}/images/excel.gif" border="0" onclick="exportToExcel('${checkRegisters.chequenumber}','${checkRegisters.customerNo}','${checkRegisters.transDate}','${checkRegisters.paymentMethod}','${checkRegisters.transactionId}');"/>
                                <c:choose>
                                    <c:when test="${fn:toUpperCase(checkRegisters.paymentMethod)==fn:toUpperCase(commonConstants.PAYMENT_METHOD_CHECK)}">
                                        <img alt="" title="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.AP_PAYMENT}&moduleRefId=${checkRegisters.chequenumber}',300,900);"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img alt="" title="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.AP_PAYMENT}&moduleRefId=${checkRegisters.customerNo}-${checkRegisters.apBatchId}',300,900);"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </display:column>
                        <display:column style="display:none">
                            <input type="hidden" name="batchNo" id="batchNo${i}" value="${checkRegisters.apBatchId}"/>
                            <input type="hidden" name="transactionId" id="transactionId${i}" value="${checkRegisters.transactionId}"/>
                            <input type="hidden" name="checkNumber" id="checkNumber${i}" value="${checkRegisters.chequenumber}"/>
                        </display:column>
                        <c:set var="i" value="${i+1}"></c:set>
                    </display:table>
                </div>
            </td>
        </tr>
    </table>
</c:if>

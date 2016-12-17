<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<html> 
    <head>
        <title>JSP for InbondDetailsForm form</title>
        <%@include file="../includes/baseResources.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script language="javascript" src="${path}/js/paymentRelease.js"></script>
        <script type="text/javascript" language="javascript">

        </script>
    </head>
    <body class="whitebackgrnd" >
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()">
            </form>
        </div>
        <html:form action="/paymentRelease" scope="request">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="id" styleId="id"/>
            <html:hidden property="bolId" styleId="bolId"/>
            <html:hidden property="fileNumber" styleId="fileNumber"/>
            <html:hidden property="overPaid" styleId="overPaid"/>
            <table class="tableBorderNew" cellpadding="2" cellspacing="0"  border="0"  width="100%">
                <tr class="tableHeadingNew">
                    <td width="150px">Import Release </td>
                    <td align="right" colspan="5">
                        <input type="button" value="Save"  class="buttonStyleNew"
                               onclick="savePaymentRelease()" />
                        <input type="button" value="Close"  class="buttonStyleNew"
                               onclick="closePaymentRelease()" />
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Original Received</td>
                    <c:choose>
                        <c:when test="${originlaBlrequiredForRelease == 'No'}">
                            <td> Y<html:radio property="importRelease" name="paymentReleaseForm" value="Y" 
                                        disabled="true" styleId="importReleaseYes" onclick="checkImportRelease(this)"/>&nbsp;&nbsp;
                                N<html:radio property="importRelease" name="paymentReleaseForm" value="N"
                                            disabled="true" styleId="importReleaseNo" onclick="checkImportRelease(this)"/>
                            </td>
                            <td>
                                Released On
                            </td>
                            <td>
                                <html:text styleClass="BackgrndColorForTextBox"  property="importReleaseOn" 
                                           size="18" readonly="true"  tabindex="-1" styleId="importReleaseOn"/>
                            </td>
                            <td>
                                Comment
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${paymentReleaseForm.importRelease == 'Y'}">
                                        <html:textarea property="importReleaseComments"  styleClass="BackgrndColorForTextBox"
                                                       styleId="importCommentsId"  rows="2" cols="31"
                                                       style="text-transform: uppercase">
                                        </html:textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <html:textarea property="importReleaseComments"  styleClass="BackgrndColorForTextBox"
                                                       styleId="importCommentsId"  rows="2" cols="31"
                                                       style="text-transform: uppercase" readonly="true" tabindex="-1">
                                        </html:textarea>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td> Y<html:radio property="importRelease" name="paymentReleaseForm" value="Y"
                                        styleId="importReleaseYes" onclick="checkImportRelease(this)"/>&nbsp;&nbsp;
                                N<html:radio property="importRelease" name="paymentReleaseForm" value="N"
                                            styleId="importReleaseNo" onclick="checkImportRelease(this)"/>
                            </td>
                            <td>
                                Released On
                            </td>
                            <td>
                                <html:text styleClass="BackgrndColorForTextBox"  property="importReleaseOn" 
                                           size="18" readonly="true"  tabindex="-1" styleId="importReleaseOn"/>
                            </td>
                            <td>
                                Comment
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${paymentReleaseForm.importRelease == 'Y'}">
                                        <html:textarea property="importReleaseComments"  styleClass="textlabelsBoldForTextBox"
                                                       styleId="importCommentsId"  rows="2" cols="31"
                                                       style="text-transform: uppercase">
                                        </html:textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <html:textarea property="importReleaseComments"  styleClass="BackgrndColorForTextBox"
                                                       styleId="importCommentsId"  rows="2" cols="31"
                                                       style="text-transform: uppercase" readonly="true" tabindex="-1">
                                        </html:textarea>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr class="textlabelsBold">
                    <c:choose>
                        <c:when test ="${originlaBlrequiredForRelease == 'Yes'}">
                            <td>Express release</td>
                            <td>
                                Y<html:radio property="expressRelease" value="Y" name="paymentReleaseForm" disabled="true" styleId="expressReleaseYes" styleClass="BackgrndColorForTextBox" onclick="checkExpressRelease(this)"/>&nbsp;&nbsp;
                                N<html:radio property="expressRelease" value="N" name="paymentReleaseForm" disabled="true" styleId="expressReleaseNo" styleClass="BackgrndColorForTextBox" onclick="checkExpressRelease(this)"/>
                            </td>
                            <td>Released On</td>
                            <td style="padding-bottom: 2px;">
                                <html:text property="expressReleasedOn" readonly="true"  tabindex="-1"
                                           styleClass="BackgrndColorForTextBox" styleId="expressReleasedOn" size="18" />
                            </td>
                            <td>Comment</td>
                            <td style="padding-left: 1px;">
                                <c:choose>
                                    <c:when test="${paymentReleaseForm.expressRelease == 'Y'}">
                                        <html:textarea property="expressReleaseComment" styleClass="BackgrndColorForTextBox"
                                                       styleId="expressReleaseComment" rows="2" cols="31"
                                                       style="text-transform: uppercase">

                                        </html:textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <html:textarea property="expressReleaseComment" styleClass="BackgrndColorForTextBox"
                                                       styleId="expressReleaseComment" rows="2" cols="31"
                                                       style="text-transform: uppercase" readonly="true" tabindex="-1"></html:textarea>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td>Express release</td>
                            <td>
                                Y<html:radio property="expressRelease" value="Y" name="paymentReleaseForm" styleId="expressReleaseYes" onclick="checkExpressRelease(this)"/>&nbsp;&nbsp;
                                N<html:radio property="expressRelease" value="N" name="paymentReleaseForm" styleId="expressReleaseNo" onclick="checkExpressRelease(this)"/>
                            </td>
                            <td>Released On</td>
                            <td style="padding-bottom: 2px;">
                                <html:text property="expressReleasedOn" readonly="true"  tabindex="-1"
                                           styleClass="BackgrndColorForTextBox" styleId="expressReleasedOn" size="18" />
                            </td>
                            <td>Comment</td>
                            <td style="padding-left: 1px;">
                                <c:choose>
                                    <c:when test="${paymentReleaseForm.expressRelease == 'Y'}">
                                        <html:textarea property="expressReleaseComment" styleClass="textlabelsBoldForTextBox"
                                                       styleId="expressReleaseComment" rows="2" cols="31"
                                                       style="text-transform: uppercase">

                                        </html:textarea>
                                    </c:when>
                                    <c:otherwise>
                                        <html:textarea property="expressReleaseComment" styleClass="BackgrndColorForTextBox"
                                                       styleId="expressReleaseComment" rows="2" cols="31"
                                                       style="text-transform: uppercase" readonly="true" tabindex="-1"></html:textarea>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:otherwise>
                    </c:choose>

                </tr> 
                <tr class="textlabelsBold">
                    <td>Delivery Order</td>
                    <td>
                        Y<html:radio property="deliveryOrder" value="Y" name="paymentReleaseForm" styleId="deliveryOrderYes" onclick="checkdeliveryOrder(this)"/>&nbsp;&nbsp;
                        N<html:radio property="deliveryOrder" value="N" name="paymentReleaseForm" styleId="deliveryOrderNo" onclick="checkdeliveryOrder(this)"/>
                    </td>
                    <td>Released On</td>
                    <td style="padding-bottom: 2px;">
                        <html:text property="deliveryOrderOn" readonly="true"  tabindex="-1"
                                   styleClass="BackgrndColorForTextBox" styleId="deliveryOrderOn" size="18" />
                    </td>
                    <td>Comment</td>
                    <td style="padding-left: 1px;"> 
                        <c:choose>
                            <c:when test="${paymentReleaseForm.deliveryOrder == 'Y'}">
                                <html:textarea property="deliveryOrderComment" styleClass="textlabelsBoldForTextBox"
                                               styleId="deliveryOrderComment" rows="2" cols="31"
                                               style="text-transform: uppercase">

                                </html:textarea>
                            </c:when>
                            <c:otherwise>
                                <html:textarea property="deliveryOrderComment" styleClass="BackgrndColorForTextBox"
                                               styleId="deliveryOrderComment" rows="2" cols="31"
                                               style="text-transform: uppercase" readonly="true" tabindex="-1"></html:textarea>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Customs Clearance</td>
                    <td>
                        Y<html:radio property="customsClearance" value="Y" name="paymentReleaseForm" styleId="customsClearanceYes" onclick="checkcustomsClearance(this)"/>&nbsp;&nbsp;
                        N<html:radio property="customsClearance" value="N" name="paymentReleaseForm" styleId="customsClearanceNo" onclick="checkcustomsClearance(this)"/>
                    </td>

                    <td>Released On</td>
                    <td style="padding-bottom: 2px;">
                        <html:text property="customsClearanceOn" readonly="true"  tabindex="-1"
                                   styleClass="BackgrndColorForTextBox" styleId="customsClearanceOn" size="18" />
                    </td>
                    <td>Comment</td>
                    <td style="padding-left: 1px;"> 
                        <c:choose>
                            <c:when test="${paymentReleaseForm.customsClearance == 'Y'}">
                                <html:textarea property="customsClearanceComment" styleClass="textlabelsBoldForTextBox"
                                               styleId="customsClearanceComment" rows="2" cols="31"
                                               style="text-transform: uppercase">

                                </html:textarea>
                            </c:when>
                            <c:otherwise>
                                <html:textarea property="customsClearanceComment" styleClass="BackgrndColorForTextBox"
                                               styleId="customsClearanceComment" rows="2" cols="31"
                                               style="text-transform: uppercase" readonly="true" tabindex="-1"></html:textarea>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>


            </table>
            <table class="tableBorderNew" cellpadding="2" cellspacing="0"  border="0"  width="100%">
                <tr class="tableHeadingNew">
                    <td width="150px">Payment Release</td>
                    <td align="right" colspan="5">
                        <html:hidden property="collectAmount"/>
                        <c:if test="${paymentReleaseForm.collectAmount != null}">
                            <fmt:formatNumber pattern="##,###,##0.00" value="${paymentReleaseForm.collectAmount}" var="totalCollectAmount"></fmt:formatNumber>
                            A/R Collect Amount:  ${totalCollectAmount} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        </c:if>
                        <input type="button" value="Add Payment" id="addPayment" class="buttonStyleNew"
                               onclick="openPaymentRelease()" style="width:80px"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Payment Release</td>
                    <td >
                        Y<html:radio property="paymentRelease" value="Y" name="paymentReleaseForm" styleId="paymentReleaseYes" onclick="checkPaymentRelease(this)"/>&nbsp;&nbsp;
                        N<html:radio property="paymentRelease" value="N" name="paymentReleaseForm" styleId="paymentReleaseNo" onclick="checkPaymentRelease(this)"/>

                    </td>
                    <td >Released On</td>
                    <td style="padding-bottom:2px;">
                        <html:text property="releasedOn" readonly="true"  tabindex="-1"
                                   styleClass="BackgrndColorForTextBox" styleId="releasedOn" size="18" />
                    </td>
                    <td >Comment</td>
                    <td style="padding-left:1px;">
                        <c:choose>
                            <c:when test="${paymentReleaseForm.paymentRelease == 'Y'}">
                                <html:textarea property="comment"  styleClass="textlabelsBoldForTextBox"
                                               styleId="paymentReleaseComments"  rows="2" cols="31"
                                               style="text-transform: uppercase">
                                </html:textarea>
                            </c:when>
                            <c:otherwise>
                                <html:textarea property="comment"  styleClass="BackgrndColorForTextBox"
                                               styleId="paymentReleaseComments"  rows="2" cols="31"
                                               style="text-transform: uppercase" readonly="true" tabindex="-1">
                                </html:textarea>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>

            <div id="paymentReleaseDiv" style="display: none">
                <table width="100%" border="0" cellpadding="3" cellspacing="0" id="records" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td colspan="8">Add Payment </td>
                    </tr>
                    <tr  class="textlabelsBold">
                        <td>Check Number</td>
                        <td><html:text  property="checkNumber" styleId="checkNumber" value="" styleClass="textlabelsBoldForTextBox" maxlength="20"/></td>
                        <td>Amount</td>
                        <td>
                            <html:text property="amount" styleId="paymentAmount"  styleClass="textlabelsBoldForTextBox" maxlength="12" onchange="formatNumber(this)"/>
                        </td>
                        <td>Paid By</td>
                        <td><html:text property="paidBy" styleId="paidBy" value="" styleClass="textlabelsBoldForTextBox" size="18" maxlength="100" /></td>
                        <td>Paid Date</td>
                        <td>
                            <img src="${path}/img/CalendarIco.gif" alt="cal" id="paidDateCal"
                                 onmousedown="insertDateFromCalendar(this.id, 3);"  />
                            <html:text property="paidDate" value=""
                                       styleClass="textlabelsBoldForTextBox" styleId="txtpaidDateCal" size="16"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="8">
                            <input type="button" value="Save" id="savePayment" class="buttonStyleNew"
                                   onclick="addPaymentRelease(this)" />
                            <input type="button" value="Cancel"  class="buttonStyleNew"
                                   onclick="cancelPaymentRelease()" />
                        </td>
                    </tr>
                </table>
            </div>
            <br>
            <table width="100%" class="tableBorderNew">
                <tr>
                    <td>
                        <div id="inbondDisplayTableId">
                            <table border="0" cellpadding="0" cellspacing="0" id="paymentReleasetable" width="100%">
                                <tr>
                                    <td>
                                        <c:set var="index" value="0"></c:set>
                                        <display:table name="${paymentReleaseList}" id="paymentRelease"
                                                       class="displaytagstyleNew"   pagesize="50" sort="list">
                                            <display:setProperty name="paging.banner.some_items_found"> <span class="pagebanner"> <font color="blue">{0}</font> Payments displayed,For more Records click on page numbers. </span> </display:setProperty>
                                            <display:setProperty name="paging.banner.one_item_found"> <span class="pagebanner"> One {0} displayed. Page Number </span> </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found"> <span class="pagebanner"> {0} {1} Displayed, Page Number </span> </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list"> <span class="pagebanner"> No Records Found. </span> </display:setProperty>
                                            <display:column title="Check Number" property="checkNumber"></display:column>
                                            <%--<fmt:formatDate pattern="dd-MMM-yyyy" var="release" value="${paymentRelease.releasedOn}"/>
                                            <display:column title="Released On">
                                                ${release}
                                            </display:column>--%>
                                            <display:column title="Paid By" property="paidBy">
                                            </display:column>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="date" value="${paymentRelease.paidDate}"/>
                                            <display:column title="Paid Date">
                                                ${date}
                                            </display:column>
                                            <display:column title="Amount">
                                                <fmt:formatNumber var="amount"  pattern="###,###,##0.00" value="${paymentRelease.amount}"/>
                                                ${amount}
                                            </display:column>
                                            <display:column title="Action" >
                                                <img src="${path}/img/icons/delete.gif" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"
                                                     onclick="deletePaymentRelease('${paymentRelease.id}')" id="deleteimg" />
                                                <img src="${path}/img/icons/edit.gif" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();"
                                                     onclick="editPaymentRelease('${paymentRelease.id}', '${paymentRelease.checkNumber}', '${amount}', '${date}', '${paymentRelease.paidBy}')" id="editimg" />
                                            </display:column>
                                            <c:set var="index" value="${index+1}"></c:set>
                                        </display:table>
                                    </td>
                                </tr>
                                <tr>
                                    <c:if test="${totalPaidAmount!=null}">
                                        <td style="padding-right:70px;text-align:right;" colspan="2">
                                            <b>Total Paid Amount: </b>
                                            <fmt:formatNumber pattern="##,###,##0.00" value="${totalPaidAmount}" var="paidAmount"></fmt:formatNumber>
                                            ${paidAmount}
                                        </td>
                                    </c:if>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <input type="hidden" value="${hasPayment}" id="hasPayment"/>
        </html:form>
    </body>
</html>


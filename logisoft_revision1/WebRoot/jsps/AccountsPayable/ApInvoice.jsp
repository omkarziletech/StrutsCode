<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <base href="${basePath}">
        <title>AP Invoice Generator</title>
        <%@include file="../includes/baseResources.jsp"%>
        <c:if test="${param.accessMode==0 && apInvoiceForm.buttonValue!='viewInvoice'}">
            <c:redirect url="/jsps/AccountsPayable/SearchApInvoice.jsp?accessMode=${param.accessMode}"/>
        </c:if>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/dwr/util.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/dwr/interface/DwrUtil.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/dwr/interface/AccrualsBC.js"/>"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
	<c:set var="accessMode" value="1"/>
	<c:set var="canEdit" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="canEdit" value="false"/>
	</c:if>
    </head>
    <body class="whitebackgrnd" onmousedown="cancelBlur();">
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form action="/apinvoice?accessMode=${accessMode}" name="apInvoiceForm"
                   type="com.gp.cvst.logisoft.struts.form.APInvoiceForm" scope="request">
            <html:hidden property="buttonValue"/>
            <html:hidden property="accrualsId"/>
            <html:hidden property="apInvoiceId" value="${apInvoice.id}"/>
            <html:hidden property="totalAmount"  value="${apInvoice.invoiceAmount}"/>
            <c:if test="${apInvoice.status=='AP'}">
                <span style="font-size:medium;color:red">
                    This invoice <c:out value="${apInvoice.invoiceNumber}"/> is posted to AP
                </span>
            </c:if>
            <c:if test="${!empty feedbackMessage}">
                <span style="font-size:medium;color:blue">
                    <c:out value="${feedbackMessage}"/>
                </span>
            </c:if>
            <c:if test="${!empty errorMessage}">
                <span style="font-size:medium;color:red">
                    <c:out value="${errorMessage}"/>
                </span> </c:if>
            <c:if test="${!empty editAPInvoice}">
                <html:hidden property="editApInvoice" value="${editAPInvoice}"/>
            </c:if>
            <table border="0" cellpadding="3" cellspacing="3" class="tableBorderNew" width="100%">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="3" cellspacing="0">
                            <tr class="tableHeadingNew">
                                <td colspan="5">AP Invoice</td>
                                <td align="right">
                                    <input type="button" class="buttonStyleNew" style="width: 75px" value="Goto Search"
                                           onclick="window.location='${path}/jsps/AccountsPayable/SearchApInvoice.jsp?accessMode=${accessMode}'">
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Vendor Name</td>
                                <td>
                                    <input name="cusName" id="cusName" value="${apInvoiceform.cusName}"
					    style="text-transform:uppercase;" class="textlabelsBoldForTextBox"/>
                                    <input name="custname_check" id="custname_check"  type="hidden" value="${apInvoiceform.cusName}">
                                    <div id="custname_choices" style="display: none" class="autocomplete"></div>
				    <c:if test="${canEdit}">
					<input type="button" class="buttonStyleNew"
					       value="Trading Partner" style="width: 100px" onclick="gotoTradingPartner()"/>
				    </c:if>
                                </td>
                                <td>Vendor Number</td>
                                <td><html:text property="accountNumber" styleId="accountNumber" tabindex="-1" readonly="true" 
                                           value="${apInvoiceform.accountNumber}" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                           style="text-transform:uppercase;"/>
                                </td>
                                <td>Invoice Number</td>
                                <td><html:text property="invoiceNumber" styleId="invoiceNumber" value="${apInvoice.invoiceNumber}" 
                                           styleClass="textlabelsBoldForTextBox" onblur="validateInvoice(this)" style="text-transform:uppercase;"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Vendor Type</td>
                                <td>
                                    <html:text property="arCustomertype" styleId="arCustomertype" tabindex="-1" value="${apInvoice.customerType}" 
                                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td>Contact Name</td>
                                <td>
                                    <html:text property="contactName" styleId="contactName" tabindex="-1" value="${apInvoice.contactName}" 
                                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td>Date</td>
                                <td>
                                    <div class="float-left">
                                        <fmt:formatDate pattern="MM/dd/yyyy HH:mm" var="date" value="${apInvoice.date}"/>
                                        <html:text property="date" value="${date}"  styleClass="textlabelsBoldForTextBox"
                                                   styleId="txtcal1" onchange="validateInvoiceDate(this)" onkeyup="triggerTab('notes')"/>
                                    </div>
                                    <div class="calendar-img">
                                        <img src="${path}/img/CalendarIco.gif" id="cal1" height="14px"  alt="cal" name="cal1"
                                             onmousedown="insertDateFromCalendar(this.id,2);" style="cursor: pointer"/>
                                    </div>
                                </td>


                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">Address</td>
                                <td>
                                    <html:textarea property="address" styleId="address" tabindex="-1" style="text-transform:uppercase;"
						   rows="2" cols="21" value="${apInvoice.address}" styleClass="textlabelsBoldForTextBox"/>
                                </td>
                                <td>Phone</td>
                                <td>
                                    <html:text property="phoneNumber" styleId="phoneNumber" tabindex="-1"
					       value="${apInvoice.phoneNumber}" maxlength="20" styleClass="textlabelsBoldForTextBox"/>
                                </td>
                                <td>Terms</td>
                                <td>
                                    <html:text property="termDesc" styleId="termDesc" tabindex="-1" style="text-transform:uppercase;"
					       onchange="calculateDueDate(this)" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                    <html:hidden property="term" styleId="term"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">For</td>
                                <td valign="top" align="left" colspan="3">
                                    <html:textarea property="notes" styleId="notes" rows="3" cols="84" onblur="changeFocus('glAccount')"
                                                   value="${apInvoice.notes}" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td>Due Date</td>
                                <td>
                                    <div class="float-left">
                                    <fmt:formatDate pattern="MM/dd/yyyy HH:mm" var="dueDate" value="${apInvoice.dueDate}"/>
                                    <html:text property="dueDate" value="${dueDate}" styleClass="textlabelsBoldForTextBox"
                                               styleId="txtcal2" onchange="validateDueDate(this)" tabindex="-1"/>
                                    </div>
                                    <div class="calendar-img">
                                        <img src="${path}/img/CalendarIco.gif" alt="cal" height="14px" name="cal2" align="top"
                                         id="cal2" onMouseDown="insertDateFromCalendar(this.id,2);" style="cursor: pointer"/>
                                    </div>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">Reject Invoice</td>
                                <td>
                                    <c:if test="${apInvoice.status!='AP' && apInvoice.status!='R'}">
                                        <html:checkbox property="rejectinvoice" value="${commonConstants.YES}" onclick="showPost(this)" tabindex="-1"/>
                                    </c:if>
                                    <c:if test="${apInvoice.status=='R'}">
                                        <html:checkbox property="rejectinvoice" value="${commonConstants.YES}" onclick="showPost(this)" tabindex="-1"/>
                                    </c:if>
                                    <c:if test="${apInvoice.status=='AP'}">
                                        <html:checkbox property="rejectinvoice" value="${commonConstants.NO}" disabled="true" tabindex="-1"/>
                                    </c:if>
                                    Recurring
                                    <c:choose>
                                        <c:when test="${!empty apInvoice.id}">
                                            <html:checkbox property="recurring" value="y" disabled="true" tabindex="-1"/>
                                        </c:when>
                                        <c:otherwise>
                                            <html:checkbox property="recurring" value="y" tabindex="-1"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td colspan="2"></td>
                                <td colspan="2">
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>Starting<br>Period</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${apInvoice.startPeriod==null}">
                                                        <select name="startingPeriod" class="dropdown_accounting" tabindex="-1">
                                                            <c:forEach var="item" items="${fiscalPeriods}">
                                                                <option><c:out value="${item.periodDis}"/></option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:text  property="startingPeriod" value="${apInvoice.startPeriod}"
								    size="5" readonly="true" styleClass="textlabelsBoldForTextBox" tabindex="-1"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>Ending<br>Period</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${apInvoice.endPeriod==null}">
                                                        <select name="endingPeriod" class="dropdown_accounting" tabindex="-1">
                                                            <c:forEach var="item" items="${fiscalPeriods}">
                                                                <option><c:out value="${item.periodDis}"/></option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <html:text  property="endingPeriod" value="${apInvoice.endPeriod}"
								    size="5" readonly="true" styleClass="textlabelsBoldForTextBox" tabindex="-1"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
			    <c:if test="${canEdit}">
				<tr>
				    <td colspan="6" align="center">
					<c:if test="${apInvoice.status!='AP'}">
					    <input type="button" class="buttonStyleNew" name="update" value="Save" onClick="updateAPInvoice()"/>
					    <input type="button" class="buttonStyleNew" name="delete" value="Delete" onClick="deleteAPInvoice()"/>
					    <input type="button" class="buttonStyleNew" name="postbutton" id="postbutton" value="Post" onClick="postInvoice()">
					</c:if>
                                            <input type="button" class="buttonStyleNew" name="upload" value="Scan/Attach" onclick="scanOrAttach()" id="scanAttach"/>
					<input type="button" class="buttonStyleNew"
					       style="width: 60px" value='Notes' onclick="showNotes('${notesConstants.AP_INVOICE}')"/>
				    </td>
				</tr>
			    </c:if>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:if test="${apInvoice.status!='AP'}">
                            <table width="100%" border="0" cellpadding="3" cellspacing="0">
                                <tr class="tableHeadingNew">
                                    <td colspan="7">Add Line Items</td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>GL Account</td>
                                    <td>
                                        <html:text  property="glAccount" styleId="glAccount"  value="" styleClass="textlabelsBoldForTextBox"/>
                                        <input type="hidden" name="glAccountValid" id="glAccountValid">
                                        <div class="newAutoComplete" id="glAccountDiv"></div>
				    </td>
                                    <td>Description</td>
                                    <td>
                                        <html:textarea property="description" tabindex="-1" style="text-transform:uppercase;"
						       rows="2" cols="20" value=""  styleClass="textlabelsBoldForTextBox"/>
                                    </td>
                                    <td>Amount</td>
                                    <td>
                                        <html:text property="amount" styleId="amount"
						   onchange="formatAmount()" value="0.00" styleClass="textlabelsBoldForTextBox"/>
                                    </td>
                                    <td align="center">
                                        <input type="button" class="buttonStyleNew" value="Add" name="save" onClick="addLineItem()"/>
                                    </td>
                                </tr>
                            </table>
                        </c:if> 
                    </td>
                </tr>
		<tr>
                    <td>
                        <table width="100%" border="0" cellpadding="3" cellspacing="0">
                            <tr class="tableHeadingNew">
                                <td> List of Line Items </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="listDiv" class="scrolldisplaytable" style="height:100px;overflow: auto;">
                                        <display:table name="${lineItems}" pagesize="${pageSize}" defaultorder="descending"
                                                       defaultsort="6" class="displaytagstyleNew" id="lineItems"sort="list">
                                            <display:setProperty name="paging.banner.some_items_found"> <span class="pagebanner"> 
						    <font color="blue">{0}</font> Line Items displayed,For more Records click on page numbers. </span>
						</display:setProperty>
						<display:setProperty name="paging.banner.one_item_found">
						<span class="pagebanner"> One {0} displayed. Page Number </span>
					    </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found"> 
						<span class="pagebanner"> {0} {1} Displayed, Page Number </span>
					    </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list"> <span class="pagebanner"> No Records Found. </span>
					    </display:setProperty>
                                            <display:column property="glAcctNo" title="GL Account" sortable="true" headerClass="sortable"/>
                                            <display:column property="description"  title="Description" sortable="true" headerClass="sortable" 
                                                            style="text-transform:uppercase;"/>
                                            <display:column property="amount" title="Amount" sortable="true" headerClass="sortable"></display:column>
                                            <c:if test="${!empty lineItems && lineItems.status!='AS'}">
                                                <display:column title="Action">
						    <img alt="" src="${path}/img/icons/delete.gif" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"
								 onmouseout="tooltip.hide();" onclick="deleteAccruals(${lineItems.transactionId})"/>
                                                    <input type="hidden" name="transactionId" id="transactionId" value="${lineItems.transactionId}">
                                                    <input type="hidden" name="invoiceAmount" id="invoiceAmount" value="${lineItems.amount}">
                                                </display:column>
                                            </c:if>
                                        </display:table>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <c:if test="${apInvoice.invoiceAmount!=null}">
                                    <td style="padding-right:180px;text-align:right;">
                                        <b>Total Amount: </b>
                                        <fmt:formatNumber pattern="##,###,##0.00" value="${apInvoice.invoiceAmount}"></fmt:formatNumber>
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
        <input type="hidden" id="viewAccountingScanAttach" value="${roleDuty.viewAccountingScanAttach}">
    </body>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApInvoice.js"></script>
    <%@include file="../includes/baseResourcesForJS.jsp"%>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery("#lineItems").tablesorter({widgets: ['zebra']});
    </script>
</html>

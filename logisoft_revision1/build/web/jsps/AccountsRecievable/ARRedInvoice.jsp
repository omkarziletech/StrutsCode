<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
            <base href="${basePath}">
        <title>AR Invoice Generator</title>
        <%@include file="../includes/baseResources.jsp"%>

        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <style type="text/css">
        #contactConfigDetails{
            position:fixed;
            _position:absolute;
            border-style: solid solid solid solid;
            background-color: white;
            z-index:99;
            left:20%;
            top:30%;
            bottom:5%;
            right:20%;
            _height:expression(document.body.offset+"px");

            width:auto;
            height: auto;
            float: left;
        }
    </style>
    <body class="whitebackgrnd">
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 99%;padding-left: 1%;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 99%;padding-left: 1%;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel"
                       onclick="confirmNo()">
            </form>
        </div>
        <html:form action="/arRedInvoice" name="arRedInvoiceForm"
                   type="com.logiware.form.ARRedInvoiceForm" scope="request">
            <html:hidden property="buttonValue"/>
            <html:hidden property="accrualsId"/>
            <html:hidden property="arRedInvoiceId" value="${arRedInvoice.id}"/>
            <c:if test="${arRedInvoice.status=='AR'}">
                <span style="font-size:medium;color:red">
                    This invoice <c:out value="${arRedInvoice.invoiceNumber}"/> is posted to AR
                </span>
            </c:if>
            <c:if test="${!empty feedbackMessage}">
                <span style="font-size:medium;color:blue">
                    <c:out value="${feedbackMessage}"/>
                </span>
            </c:if>
            <c:if test="${!empty editArRedInvoice}">
                <html:hidden property="editArRedInvoice" value="${editArRedInvoice}"/>
            </c:if>
            <table border="0" cellpadding="3" cellspacing="3" class="tableBorderNew" width="100%">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="1" cellspacing="0">
                            <tr class="tableHeadingNew">
                                <c:if test="${empty fclBl}">
                                    <td colspan="5">AR Invoice</td>
                                    <td align="right">
                                        <input type="button" class="buttonStyleNew" style="width: 75px"
                                               value="Goto Search" onclick="window.location = '${path}/jsps/AccountsRecievable/SearchArRedInvoice.jsp?accessMode=${accessMode}'">
                                    </td>
                                </c:if>
                                <c:if test="${!empty fclBl}">
                                    <td colspan="6">AR Invoice</td>
                                    <td align="right">
                                        <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addArRedInvoice('${fclBl.fileNo}')"/>
                                        <input type="button" value="Search"  name="Search" class="buttonStyleNew" onclick="listArInvoice('${fclBl.fileNo}')"/>
                                        <input type="button" value="Close"  name="close" class="buttonStyleNew" onclick="closeArRedInvoice('${fclBl.fileNo}')"/>
                                    </td>
                                </c:if>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Customer Name</td>
                                <td>
                                    <input name="cusName" id="cusName" value="${arRedInvoiceform.cusName}"
                                           style="text-transform:uppercase;" class="textlabelsBoldForTextBox"/>
                                    <input name="custname_check" id="custname_check"  type="hidden" value="${arRedInvoiceform.cusName}">
                                    <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                    <c:choose>
                                        <c:when test="${importFlag eq true}">
                                            <script type="text/javascript">
                                                AjaxAutocompleter("cusName", "custname_choices", "accountNumber", "custname_check",
                                                        "${path}/servlet/AutoCompleterServlet?action=Customer&textFieldId=cusName&importFlag=true", "getCustomerDetails(true)", "");
                                            </script>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButtonForFF" onclick="openArContact()"/>
                                        </c:when>
                                        <c:otherwise>
                                            <script type="text/javascript">
                                                AjaxAutocompleter("cusName", "custname_choices", "accountNumber", "custname_check",
                                                        "${path}/servlet/AutoCompleterServlet?action=Customer&textFieldId=cusName&actType=NoConsignee&importFlag=false", "getCustomerDetails(false)", "");
                                            </script>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openArContact()"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>Customer Number</td>
                                <td><html:text property="accountNumber" styleId="accountNumber" value="${arRedInvoiceform.accountNumber}" 
                                           styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td>Invoice #</td>
                                <td><html:text property="invoiceNumber" value="${arRedInvoice.invoiceNumber}" styleClass="BackgrndColorForTextBox" 
                                           readonly="true" tabindex="-1" style="text-transform:uppercase;"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">Customer Type</td>
                                <td valign="top">
                                    <html:text property="arCustomertype" styleId="arCustomertype"  value="${arRedInvoice.customerType}"
                                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td valign="top">Contact Name</td>
                                <td valign="top">
                                    <html:text property="contactName" styleId="contactName"  styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td valign="top">Date</td>
                                <td valign="top">
                                    <fmt:formatDate pattern="MM/dd/yyyy HH:mm" var="date" value="${arRedInvoice.date}"/>
                                    <html:text property="date" value="${date}"  styleClass="textlabelsBoldForTextBox"
                                               styleId="txtcal1" onchange="validateInvoiceDate(this)"/>
                                    <img src="${path}/img/CalendarIco.gif" id="cal1" alt="cal" name="cal1" height="13px"
                                         onmousedown="insertDateFromCalendar(this.id, 2);" style="cursor: pointer"/> </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">Address</td>
                                <td valign="top">
                                    <html:textarea property="address" styleId="address" rows="2" cols="21" value="${arRedInvoice.address}" 
                                                   styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                                </td>
                                <td valign="top">Phone</td>
                                <td valign="top">
                                    <html:text property="phoneNumber" styleId="phoneNumber" value="${arRedInvoice.phoneNumber}" maxlength="20" styleClass="textlabelsBoldForTextBox"/>
                                </td>
                                <td valign="top">Terms</td>
                                <td valign="top">
                                    <html:text property="termDesc" styleId="termDesc" onchange="calculateDueDate(this)" readonly="true" 
                                               styleClass="textlabelsBoldForTextBox"/>
                                    <html:hidden property="term" styleId="term"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">For</td>
                                <td valign="top" align="left" colspan="3">
                                    <html:textarea property="notes" rows="3" cols="80"
                                                   value="${arRedInvoice.notes}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 499)"/>
                                </td>
                                <td valign="top">Due Date</td>
                                <td valign="top">
                                    <fmt:formatDate pattern="MM/dd/yyyy HH:mm" var="dueDate" value="${arRedInvoice.dueDate}"/>
                                    <html:text property="dueDate" value="${dueDate}" styleClass="textlabelsBoldForTextBox"
                                               styleId="txtcal2" onchange="validateDueDate(this)"/>
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal2" align="top" height="13px"
                                         id="cal2" onMouseDown="insertDateFromCalendar(this.id, 2);" style="cursor: pointer"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" align="center">
                                    <c:if test="${arRedInvoice.status!='AR'}">
                                        <input type="button" class="buttonStyleNew" name="update" value="Save" onClick="updateArRedInvoice()"/>
                                        <input type="button" class="buttonStyleNew" name="delete" value="Delete" onClick="deleteArRedInvoice()"/>
                                        <c:if test="${!empty lineItems}">
                                            <input type="button" class="buttonStyleNew" name="postbutton" id="postbutton" value="Post" onClick="contactNotification('${fclBl.importFlag}')">
                                        </c:if>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:if test="${! empty arRedInvoice.id}">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr class="tableHeadingNew">
                                    <td> List of Line Items </td>
                                    <c:if test="${arRedInvoice.status!='AR'}">
                                        <td align="right">
                                            <input type="button" class="buttonStyleNew" value="Add" name="save" onClick="openLineItem()" />
                                        </td>
                                    </c:if>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <c:set var="totalAmount" value="0"/>
                                        <c:set var="item" value="0"></c:set>
                                            <div id="listDiv" class="scrolldisplaytable" style="height:100px; float: left; overflow-y:scroll ">
                                            <display:table name="${lineItems}" pagesize="${pageSize}" defaultorder="descending"
                                                           defaultsort="6" class="displaytagstyleNew" id="lineItem" sort="list">
                                                <display:setProperty name="paging.banner.some_items_found"> <span class="pagebanner"> <font color="blue">{0}</font> Line Items displayed,For more Records click on page numbers. </span> </display:setProperty>
                                                <display:setProperty name="paging.banner.one_item_found"> <span class="pagebanner"> One {0} displayed. Page Number </span> </display:setProperty>
                                                <display:setProperty name="paging.banner.all_items_found"> <span class="pagebanner"> {0} {1} Displayed, Page Number </span> </display:setProperty>
                                                <display:setProperty name="basic.msg.empty_list"> <span class="pagebanner"> No Records Found. </span> </display:setProperty>
                                                <display:column property="blDrNumber" title="BL/DR Number" sortable="true" headerClass="sortable"></display:column>
                                                <display:column property="glAccount" title="GL Account" sortable="true" headerClass="sortable"></display:column>
                                                <display:column property="chargeCode" title="Charge Code" sortable="true" headerClass="sortable"></display:column>
                                                <display:column property="shipmentType" title="Shipment Type" sortable="true" headerClass="sortable"></display:column>
                                                <display:column   title="Description" sortable="true" headerClass="sortable">
                                                    <html:textarea property="tempDescription" styleClass="textlabelsBoldForTextBox"
                                                                   readonly="true" rows="2" cols="60" value="${lineItem.description}" />
                                                </display:column>
                                                <display:column  title="Amount" sortable="true" headerClass="sortable">
                                                    <fmt:formatNumber var="amount" value="${lineItem.amount}" pattern="###,###,##0.00"/>
                                                    ${amount}
                                                </display:column>
                                                <display:column title="Action"> 
                                                    <c:if test="${arRedInvoice.status!='AR'}">
                                                        <img src="${path}/img/icons/delete.gif"  onmouseover="tooltip.show('<strong>Delete</strong>', null, event);"
                                                             onmouseout="tooltip.hide();" onclick="deleteAccruals(${lineItem.id})"/>
                                                    </c:if>
                                                </display:column>
                                                <c:set var="item" value="${item+1}"></c:set>
                                                <c:set var="totalAmount" value="${totalAmount + lineItem.amount}"/>
                                            </display:table>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <c:if test="${totalAmount!=null}">
                                        <td style="padding-right:70px;text-align:right;" colspan="2">
                                            <fmt:formatNumber var="invoiceAmount" value="${totalAmount}" pattern="###,###,##0.00"/>
                                            <b>Total Amount: </b>${invoiceAmount}
                                            <input type="hidden" name="totalInvoiceAmount" id="invoiceAmount" value="${invoiceAmount}">
                                        </td>
                                    </c:if>
                                </tr>
                            </table>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="addLineItems" style="display:none;" align="center">
                            <c:if test="${arRedInvoice.status!='AR' && ! empty arRedInvoice.id}">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <tr class="tableHeadingNew">
                                        <td colspan="12">Add Line Items</td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>BL/DR Number</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty fclBl}">
                                                    <html:text property="bl_drNumber" styleId="bl_drNumber" styleClass="BackgrndColorForTextBox" size="25%" value="04-${fclBl.fileNo}" readonly="true" tabindex="-1"></html:text>
                                                </c:when>
                                                <c:when test="${not empty arRedInvoice.screenName and not empty arRedInvoice.fileNo}">
                                                    <html:text property="bl_drNumber" styleId="bl_drNumber" styleClass="BackgrndColorForTextBox" size="25%" value="04-${arRedInvoice.fileNo}" readonly="true" tabindex="-1"></html:text>
                                                </c:when>
                                                <c:when test="${not empty arRedInvoice.fileNo}">
                                                    <html:text property="bl_drNumber" styleId="bl_drNumber" styleClass="BackgrndColorForTextBox" size="25%" value="${arRedInvoice.fileNo}" readonly="true" tabindex="-1"></html:text>
                                                </c:when>
                                                <c:otherwise>
                                                    <input name="bl_drNumber" id="bl_drNumber" style="text-transform:uppercase;" class="textlabelsBoldForTextBox" onfocus="disableDojo()" size="25%" maxlength="20"/>
                                                    <input name="bl_drNumber_check" id="bl_drNumber_check"  type="hidden">
                                                    <div id="bl_drNumber_choices" style="display: none" class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocompleteWithFormClear("bl_drNumber", "bl_drNumber_choices", "", "bl_drNumber_check",
                                                                "${path}/actions/getFileNumber.jsp", "", "");
                                                    </script>
                                                    <input type="checkbox" id="drNumberCheck" onclick="checkUncheckDrNumber(this)"
                                                           onmouseover="tooltip.show('<strong>BL/BR Number Not Listed</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>Shipment Type</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty fclBl && fclBl.importFlag == 'I'}">
                                                    <html:text property="shipmentType" styleId="shipmentType" styleClass="BackgrndColorForTextBox" size="25%" value="FCLI" readonly="true" tabindex="-1"></html:text>
                                                </c:when>
                                                <c:when test="${not empty fclBl}">
                                                    <html:text property="shipmentType" styleId="shipmentType" styleClass="BackgrndColorForTextBox" size="25%" value="FCLE" readonly="true" tabindex="-1"></html:text>
                                                </c:when>
                                                <c:otherwise>
                                                    <html:select property="shipmentType" styleClass="textlabelsBoldForTextBox" value=""  styleId="shipmentType" onchange="getChargeCode(this)" style="width:80px;">
                                                        <html:option value="">Select</html:option>
                                                        <html:option value="FCLE">FCLE</html:option>
                                                        <html:option value="FCLI">FCLI</html:option>
                                                        <html:option value="LCLE">LCLE</html:option>
                                                        <html:option value="LCLI">LCLI</html:option>
                                                    </html:select>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td>Charge Code</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty fclBl && not empty chargeCodeList}">
                                                    <html:select property="chargeCode"  value="" style="width:150px;" styleClass="dropdown_accounting" styleId="chargeCode">
                                                        <html:optionsCollection name="chargeCodeList"/>
                                                    </html:select>
                                                </c:when>
                                                <c:otherwise>
                                                    <html:select property="chargeCode"  value="" style="width:150px;  border:1px solid #c4c5c4" styleClass="dropdown_accounting" styleId="chargeCode">
                                                        <html:option value="">Select</html:option>
                                                    </html:select>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Terminal Code</td>
                                        <td>
                                            <html:select property="terminalCode"  value="${issuingTerminal}" style="width:150px;" styleClass="dropdown_accounting" styleId="terminalCode">
                                                <html:optionsCollection name="terminalCodeList"/></html:select>
                                            </td>
                                            <td>Amount</td>
                                            <td>
                                            <html:text property="amount" maxlength="8" onchange="checkForNumberAndDecimals(this)" value="0.00" styleClass="textlabelsBoldForTextBox mandatory" size="10%"/>
                                        </td>
                                        <td>Description</td>
                                        <td>
                                            <html:textarea property="description" rows="4" cols="30" value=""  styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase"></html:textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="6" align="center">
                                                <input type="button" class="buttonStyleNew" value="Save" name="save" id="saveAddLineitem" onClick="addLineItem()" />
                                                <input type="button" class="buttonStyleNew" value="Cancel" name="cancel" onClick="closeLineItem()" />
                                            </td>
                                        </tr>
                                    </table>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
                 <div id="contactConfigDetails" style="display:none;width:900px;" align="center" >
                <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
                    <tr><td style="color: red">${invoiceMessage}</td></tr>
                        <c:if test="${not empty ContactConfigE1andE2}">
                        <tr class="tableHeadingNew">Following contacts will automaticlally recieve copy of the invoice</tr>
                        <tr><td>
                                <display:table name="${ContactConfigE1andE2}" id="configTableId" class="displaytagstyle" pagesize="50">
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span style="display:none;" class="pagebanner" />
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" >
                                        <span style="display:none;"></span>
                                    </display:setProperty>

                                    <c:choose>
                                        <c:when test="${not empty configTableId.accountNo}">
                                            <display:column title="AccountName" property="accountName"></display:column>
                                            <display:column title="AccountNo" property="accountNo"></display:column>
                                            <display:column title="AccountType">${configTableId.accountType}
                                                <c:if test="${not empty configTableId.subType}">
                                                    (${configTableId.subType})
                                                </c:if>
                                            </display:column>
                                            <display:column title="FirstName" property="firstName"></display:column>
                                            <display:column title="LastName" property="lastName"></display:column>
                                            <display:column title="Email" property="email"></display:column>
                                            <display:column title="Fax" property="fax"></display:column>
                                            <display:column title="CodeK" property="codek.code"></display:column>
                                        </c:when>
                                        <c:otherwise>
                                            <display:column title="AccountName" style="font-weight:bolder;background:pink" property="accountName"></display:column>
                                            <display:column title="AccountNo" style="font-weight:bolder;background:pink" property="accountNo"></display:column>
                                            <display:column title="AccountType" style="font-weight:bolder;background:pink">
                                                ${configTableId.accountType}${configTableId.subType}
                                                <c:if test="${not empty configTableId.subType}">
                                                    (${configTableId.subType})
                                                </c:if>
                                            </display:column>
                                            <display:column title="FirstName" style="font-weight:bolder;background:pink" property="firstName"></display:column>
                                            <display:column title="LastName" style="font-weight:bolder;background:pink" property="lastName"></display:column>
                                            <display:column title="Email" style="font-weight:bolder;background:pink" property="email"></display:column>
                                            <display:column title="Fax" style="font-weight:bolder;background:pink" property="fax"></display:column>
                                            <display:column title="CodeK" style="font-weight:bolder;background:pink" property="codek"></display:column>
                                        </c:otherwise>
                                    </c:choose>
                                </display:table>
                            </td></tr>
                        <tr><td>&nbsp;</td>
                        </tr></c:if>
                        <tr>
                            <td align="center">
                                <input class="buttonStyleNew" type="button" value="Send" onclick="postArRedInvoice(this)">
                                <input class="buttonStyleNew" type="button" value="Do Not Send" onclick="postArRedInvoiceWithoutMail(this)">
                            </td>
                        </tr>
                    </table>
                </div>
                <input type="hidden" id="index" name="index" value="${item}"/>
            <html:hidden property="action"/>
            <html:hidden property="fileNo"/>
            <html:hidden property="screenName"/>
            <html:hidden property="notification"/>
            <html:hidden property="fileType"/>
            <html:hidden property="totalAmount"  value="${totalAmount}"/>
        </html:form>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp"%>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/ARRedInvoice.js"></script>

    <c:if test="${not empty ContactConfigE1andE2}">
        <script language="javascript">
                                    redInvoiceMailNotification();
        </script>
    </c:if>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery("#lineItems").tablesorter({widgets: ['zebra']});
    </script>
</html>

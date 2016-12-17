<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %><%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Search AR Invoice</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
	<c:set var="accessMode" value="1"/>
	<c:set var="canEdit" value="true"/>
	<c:if test="${param.accessMode==0}">
	    <c:set var="accessMode" value="0"/>
	    <c:set var="canEdit" value="false"/>
	</c:if>
    </head>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form action="/arRedInvoice?accessMode=${accessMode}" name="arRedInvoiceForm" type="com.logiware.form.ARRedInvoiceForm" scope="request">
            <html:hidden property="buttonValue" value="" />
            <html:hidden property="apInvoiceChargesId"/>
            <html:hidden property="arRedInvoiceId"/>
            <c:if test="${!empty displayMessage}">
                <span style="font-size:medium;color:red;">
                    <c:out value="${displayMessage}"/>
                </span>
            </c:if>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <c:if test="${empty fileNo && empty displayMessage}">

                    <tr>
                        <td>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                                <tr class="tableHeadingNew">
                                    <td colspan="6"> Search AR Invoice</td>
                                </tr>
                                <tr></tr>
                                <tr class="textlabelsBold">
                                    <td>Customer</td>
                                    <td>
                                        <input name="cusName" id="cusName" value="${arRedInvoiceForm.cusName}" class="textlabelsBoldForTextBox" 
                                               style="text-transform: uppercase;"/>
                                        <input name="custname_check" id="custname_check" value="${arRedInvoiceForm.cusName}" type="hidden"/>
                                        <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                        <script type="text/javascript">
                                             AjaxAutocompleter("cusName", "custname_choices","accountNumber", "custname_check",
                                        "${path}/servlet/AutoCompleterServlet?action=Customer&textFieldId=cusName","SearchArRedInvoice()","");
                                      </script>
                                    </td>
                                    <td>Account Number</td>
                                    <td><html:text property="accountNumber" value="${arRedInvoiceForm.accountNumber}" 
                                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"></html:text></td>
                                    <td>Invoice Number</td>
                                    <td><html:text property="invoiceNumber" value="${arRedInvoiceForm.invoiceNumber}" 
                                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"></html:text></td>
                                </tr>
                                <tr>
                                    <td colspan="6" align="center"><input type="button" value="Search" name="search" class="buttonStyleNew" onclick="SearchArRedInvoice()"/>
                                        <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addArRedInvoice()"/>
                                        <c:if test="${not empty fileNo}">
                                            <input type="button" value="Close"  name="close" class="buttonStyleNew" onclick="closeArRedInvoice()"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table >
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>
                        <br/>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>List of AR Invoices</td>
                                <c:choose>
                                <c:when test="${not empty fileNo}">
                                    <td align="right">
                                        <input type="button" value="Search" name="search" class="buttonStyleNew" onclick="SearchArRedInvoice('${fileNo}')"/>
                                        <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addArRedInvoice('${fileNo}')"/>
                                        <input type="button" value="Close"  name="close" class="buttonStyleNew" onclick="closeArRedInvoice('${fileNo}')"/>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td align="right">
                                    <input type="button" value="Search" name="search" class="buttonStyleNew" onclick="SearchArRedInvoice()"/>
                                    <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addArRedInvoice()"/>
<!--                                    <input type="button" value="Close"  name="close" class="buttonStyleNew" onclick="closeArRedInvoice()"/>-->
                                    </td>
                                </c:otherwise>
                                </c:choose>
                            </tr>
                            <tr>
                                <td align="left" colspan="2">
                                    <div  style="height: 270px; overflow: auto" class="scrolldisplaytable">
                                        <display:table name="${arRedInvoiceList}" class="displaytagstyleNew" id="arRedInvoice" sort="list" requestURI="/arRedInvoice.do">
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font> Invoices Displayed,For more Data click on Page Numbers.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_item_found">
                                                <span class="pagebanner">
                                                    One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
                                                    {0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner"> No Records Found. </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Invoice"/>
                                            <display:setProperty name="paging.banner.items_name" value="Invoices "/>
                                            <display:column property="customerName"  title="CustomerName" sortable="true"  style="text-transform:uppercase;"></display:column>
                                            <display:column property="customerNumber"  title="Account #" sortable="true"  style="text-transform:uppercase;"></display:column>
                                            <display:column property="customerType"  title="Customer Type" sortable="true" ></display:column>
                                            <display:column property="invoiceNumber"  title="Invoice #" sortable="true" style="text-transform:uppercase;"></display:column>
                                            <display:column  title="Invoice Amount" sortable="true" >
                                                <fmt:formatNumber var="amount" value="${arRedInvoice.invoiceAmount}" pattern="###,###,##0.00"/>
                                                ${amount}
                                            </display:column>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="invoiceDate" value="${arRedInvoice.date}"/>
                                            <display:column title="Invoice Date" sortable="true">${invoiceDate}</display:column>
                                            <display:column title="Status" sortable="true" >
                                                <c:choose>
                                                    <c:when test="${arRedInvoice.status =='AR'}">
                                                        <c:out value="Posted"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="In Progress"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                             <fmt:formatDate pattern="dd-MMM-yyyy" var="postedDate" value="${arRedInvoice.postedDate}"/>
                                            <display:column title="Posted Date" sortable="true">${postedDate}</display:column>
                                            <display:column title="Action">
                                                    <img src="${path}/img/icons/edit.gif" onclick="editArRedInvoice(${arRedInvoice.id},'${fileNo}')" onmouseover="tooltip.show('<strong>Edit Invoice</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                                    <img id="report" onmouseover="tooltip.show('<strong>Preview</strong>',null,event);" onmouseout="tooltip.hide();"
                                                       src="${path}/img/icons/search_over.gif" border="0"
                                                       onClick="previewArInvoice('${arRedInvoice.id}')"/>
                                                    <img src="${path}/img/icons/send.gif" border="0"
                                                              onclick="PrintReports('${arRedInvoice.invoiceNumber}', '${arRedInvoice.id}')" onmouseover="tooltip.show('Print/Fax/Email',null,event);" 
                                                        onmouseout="tooltip.hide();"/>
                                                    <c:if test="${arRedInvoice.status =='AR' && roleDuty.reversePostedInvoices}">
                                                        <img src="${path}/img/icons/unpost1.png" border="0"
                                                                  onclick="reverseToPost('${arRedInvoice.id}')" onmouseover="tooltip.show('Reverse Post',null,event);"
                                                            onmouseout="tooltip.hide();"/>
                                                    </c:if>

                                            </display:column>
                                        </display:table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
             <html:hidden property="action"/>
             <html:hidden property="fileNo"/>
             <html:hidden property="screenName"/>
             <html:hidden property="fileType"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/ARRedInvoice.js"></script>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery("#apInvoice").tablesorter({widgets: ['zebra']});
    </script>
</html>
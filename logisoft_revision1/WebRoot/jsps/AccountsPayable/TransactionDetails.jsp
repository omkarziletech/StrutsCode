<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@include file="../includes/baseResources.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Invoice Details</title>
    </head>

    <body class="whitebackgrnd">
        <html:form action="/apInquiry" name="apInquiryForm" type="com.gp.cvst.logisoft.struts.form.ApInquiryForm" scope="request">
            <table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Invoice Details</td></tr>
                <tr>
                    <td>
                        <div class="scrolldisplaytable" style="height: 200px">
                            <display:table name="${apInquiryForm.invoiceOrBlDetails}" id="invoiceOrBlDetails" class="displaytagstyleNew" style="width:100%">
                                <display:setProperty name="paging.banner.placement" value="none"/>
                                <display:column title="BL Number" property="billofLadding"/>
                                <display:column title="Dock Receipt" property="docReceipt"/>
                                <display:column title="Voyage" property="voyagenumber"/>
                                <display:column title="Container" property="containerNo" maxLength="20"/>
                                <display:column title="Date" property="transactionDate" format="{0,date,MM/dd/yyyy}"/>
                                <display:column title="Posted Date" property="postedDate" format="{0,date,MM/dd/yyyy}"/>
                                <display:column title="Amount" property="transactionAmount" format="{0,number,###,###,##0.00}"/>
                                <display:column title="Charge Code" property="chargeCode"/>
                                <display:column title="GL Account" property="glAcctNo"/>
                            </display:table>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>

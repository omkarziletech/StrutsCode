<%-- 
    Document   : header
    Created on : Sep 19, 2013, 3:16:45 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 0; border-spacing: 0; border: none;">
    <tr>
	<td style="width: 650px;">
	    <label class="label">Customer Name</label>
	    <html:text property="customerName" styleId="customerName" styleClass="textbox"/>
	    <html:text property="customerNumber" styleId="customerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
	    <input type="hidden" id="customerNameCheck" value="${arInquiryForm.customerName}"/>
	    <c:if test="${writeMode}">
		<img src="${path}/images/icons/trading_partner.png" title="Go to Trading Partner" class="trading-partner" onclick="gotoTradingPartner();"/>
	    </c:if>
	    <html:checkbox title="Show Disabled" property="disabled" styleId="disabled" value="true" styleClass="check-box"/>
	    <html:checkbox title="Show All Accounts" property="showAllAccounts" styleId="showAllAccounts" value="true" styleClass="check-box"/>
	    <input type="button" class="button" value="Search" onclick="search();" tabindex="-1"/>
	    <input type="button" class="button" value="Clear" onclick="clearAll();" tabindex="-1"/>
	    <c:if test="${loginuser.role.roleDesc == 'APManager'
				      || loginuser.role.roleDesc ==  'Credit_Manager' || loginuser.role.roleDesc ==  'Admin'}">
		<input type="button" class="button" value="Upload AR Invoices" onclick="uploadArInvoices();"/>
	    </c:if>
	</td>
	<td style="width: 150px;">
	    <img src="${path}/images/icons/notes-32x32.png" title="Account Notes" class="img-20px margin-2-0-0-10" onclick="showAccountNotes();"/>
	    <img src="${path}/images/icons/e-statement.png" title="E-Statement" class="img-20px margin-2-0-0-10" onclick="sendStatement();"/>
	    <img src="${path}/images/icons/email.png" title="Send Email" class="img-20px margin-2-0-0-10" onclick="sendEmail();"/>
	    <img src="${path}/images/icons/mass-adjustment.png" title="Mass Adjustment" class="img-20px margin-2-0-0-10" onclick="doMassAdjustment();"/>
            <img src="${path}/images/icons/customer_reference.png" title="Mass Customer Reference Update" class="margin-2-0-0-10" style="height: 24px;width: 24px;" onclick="massCustRefUpdate();"/>
	</td>
	<td>
	    <div class="message align-left italic margin-2-0-0-10">${message}</div>
	</td>
    </tr>
</table>
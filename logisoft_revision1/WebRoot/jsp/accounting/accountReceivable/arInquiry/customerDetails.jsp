<%-- 
    Document  : customerDetails
    Created on: Sep 23, 2013, 4:49:24 PM
    Author    : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="customer-container">
    <table class="table" style="margin: 0; border-spacing: 0; border: none;">
	<tr>
	    <td width="20%" style="vertical-align: top">
		<table class="label" cellspacing="2">
		    <tr>
			<td class="align-right">Customer Name:</td>
			<td class="normal">${arInquiryForm.arSummary.customerName}</td>
		    </tr>
		    <tr>
			<td class="align-right">Address:</td>
			<td class="normal">${arInquiryForm.arSummary.address}</td>
		    </tr>
		    <tr>
			<td class="align-right">Contact:</td>
			<td class="normal">${arInquiryForm.arSummary.contact}</td>
		    </tr>
		    <tr>
			<td class="align-right">Phone:</td>
			<td class="normal">${arInquiryForm.arSummary.phone}</td>
		    </tr>
		    <tr>
			<td class="align-right">Fax:</td>
			<td class="normal">${arInquiryForm.arSummary.fax}</td>
		    </tr>
		    <tr>
			<td class="align-right">Email:</td>
			<td class="normal">${arInquiryForm.arSummary.email}</td>
		    </tr>
		</table>
	    </td>
	    <td width="20%" style="vertical-align: top">
		<table class="label" cellspacing="2">
		    <tr>
			<td class="align-right">Customer Number:</td>
			<td class="normal">${arInquiryForm.arSummary.customerNumber}</td>
		    </tr>
		    <tr>
			<td class="align-right">Type:</td>
			<td class="normal">${arInquiryForm.arSummary.type}</td>
		    </tr>
		    <tr>
			<td class="align-right">ECU Designation:</td>
			<td class="normal">${arInquiryForm.arSummary.ecuDesignation}</td>
		    </tr>
		    <tr>
			<td class="align-right">Shipper:</td>
			<td class="normal">${arInquiryForm.arSummary.shipper}</td>
		    </tr>
		    <tr>
			<td class="align-right">Vendor:</td>
			<td>${arInquiryForm.arSummary.vendor}</td>
		    </tr>
		    <tr>
			<td class="align-right">Consignee:</td>
			<td class="normal">${arInquiryForm.arSummary.consignee}</td>
		    </tr>
		    <tr>
			<td class="align-right">Master:</td>
			<td class="normal">${arInquiryForm.arSummary.master}</td>
		    </tr>
		</table>
	    </td>
	    <td width="15%" style="vertical-align: top">
		<table class="label" cellspacing="2">
		    <tr>
			<td class="align-right">Credit Limit:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.creditLimit,'-')}">
				<td class="normal red">(${fn:replace(arInquiryForm.arSummary.creditLimit,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal black">${arInquiryForm.arSummary.creditLimit}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
                    <tr>
                        <td class="align-right">Credit Status:</td>
                        <td class="normal">${arInquiryForm.arSummary.creditStatus}
                            <c:if test="${arInquiryForm.arSummary.importCredit eq 'Y'}">
                                <br>Import Credit
                            </c:if>
                            <c:if test="${arInquiryForm.arSummary.exemptCreditProcess eq 'Y'}">
                                <br>Exempt from Auto Hold
                            </c:if>
                            <c:if test="${arInquiryForm.arSummary.hhgPeAutosCredit eq 'Y'}">
                                <br>HHG/PE/AUTOS CREDIT
                            </c:if>
                        </td>
                    </tr>
		    <tr>
			<td class="align-right">Credit Term:</td>
                        <td class="normal">${arInquiryForm.arSummary.creditTerm}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Collector:</td>
                        <td class="normal">${arInquiryForm.arSummary.collector}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Sales Person :</td>
                        <td class="normal">${arInquiryForm.arSummary.salesPerson}</td>
                    </tr>
                    <tr>
                        <td class="align-right">Last Statement :</td>
                        <td class="normal">${arInquiryForm.arSummary.lastStatement}</td>
                    </tr>
                </table>
            </td>
            <c:if test="${not empty arInquiryForm.acSummary}">
		<td width="15%" style="vertical-align: top">
		    <table class="label" cellspacing="2">
			<tr>
			    <td class="align-right">AC SUMMARY</td>
			    <td class="align-right">AMOUNT</td>
			</tr>
			<tr>
			    <td class="align-right">Current:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.age30Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.age30Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.age30Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">31-60 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.age60Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.age60Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.age60Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">61-90 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.age90Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.age90Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.age90Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">&gt;90 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.age91Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.age91Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.age91Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">Total:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.total,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.total,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.total}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">Net Amt(AR-AC):</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.acSummary.netAmount1,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.acSummary.netAmount1,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.acSummary.netAmount1}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
		    </table>
		</td>
	    </c:if>
	    <c:if test="${not empty arInquiryForm.apSummary}">
		<td width="15%" style="vertical-align: top">
		    <table class="label" cellspacing="2">
			<tr>
			    <td class="align-right">AP SUMMARY</td>
			    <td class="align-right">AMOUNT</td>
			</tr>
			<tr>
			    <td class="align-right">Current:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.age30Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.age30Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.age30Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">31-60 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.age60Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.age60Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.age60Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">61-90 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.age90Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.age90Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.age90Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">&gt;90 Days:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.age91Amount,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.age91Amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.age91Amount}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">Total:</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.total,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.total,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.total}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
			<tr>
			    <td class="align-right">Net Amt(AR-AP):</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.netAmount1,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.netAmount1,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.netAmount1}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
		    </table>
		</td>
	    </c:if>
	    <td width="15%" style="vertical-align: top">
		<table class="label" cellspacing="2">
		    <tr>
			<td class="align-right">AR SUMMARY</td>
			<td class="align-right">AMOUNT</td>
		    </tr>
		    <tr>
			<td class="align-right">Current:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.age30Amount,'-')}">
				<td class="normal align-right red">(${fn:replace(arInquiryForm.arSummary.age30Amount,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal align-right black">${arInquiryForm.arSummary.age30Amount}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		    <tr>
			<td class="align-right">31-60 Days:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.age60Amount,'-')}">
				<td class="normal align-right red">(${fn:replace(arInquiryForm.arSummary.age60Amount,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal align-right black">${arInquiryForm.arSummary.age60Amount}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		    <tr>
			<td class="align-right">61-90 Days:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.age90Amount,'-')}">
				<td class="normal align-right red">(${fn:replace(arInquiryForm.arSummary.age90Amount,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal align-right black">${arInquiryForm.arSummary.age90Amount}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		    <tr>
			<td class="align-right">&gt;90 Days:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.age91Amount,'-')}">
				<td class="normal align-right red">(${fn:replace(arInquiryForm.arSummary.age91Amount,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal align-right black">${arInquiryForm.arSummary.age91Amount}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		    <tr>
			<td class="align-right">Total:</td>
			<c:choose>
			    <c:when test="${fn:contains(arInquiryForm.arSummary.total,'-')}">
				<td class="normal align-right red">(${fn:replace(arInquiryForm.arSummary.total,'-','')})</td>
			    </c:when>
			    <c:otherwise>
				<td class="normal align-right black">${arInquiryForm.arSummary.total}</td>
			    </c:otherwise>
			</c:choose>
		    </tr>
		    <c:if test="${not empty arInquiryForm.apSummary}">
			<tr>
			    <td class="align-right">Net Amt(AR-AP-AC):</td>
			    <c:choose>
				<c:when test="${fn:contains(arInquiryForm.apSummary.netAmount2,'-')}">
				    <td class="normal align-right red">(${fn:replace(arInquiryForm.apSummary.netAmount2,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="normal align-right black">${arInquiryForm.apSummary.netAmount2}</td>
				</c:otherwise>
			    </c:choose>
			</tr>
		    </c:if>
		</table>
	    </td>
	    <c:if test="${not empty arInquiryForm.arSummary.paymentDate}">
		<td width="5%" style="vertical-align: top">
		    <table class="label" cellspacing="2">
			<tr>
			    <td class="align-center" colspan="2">LAST PAYMENT</td>
			</tr>
			<tr>
			    <td class="align-right">Date:</td>
			    <td class="normal">${arInquiryForm.arSummary.paymentDate}</td>
			</tr>
			<tr>
			    <td class="align-right">Amount:</td>
			    <td class="normal">${arInquiryForm.arSummary.paymentAmount}</td>
			</tr>
			<tr>
			    <td class="align-right">Reference:</td>
			    <td class="normal">${arInquiryForm.arSummary.paymentReference}</td>
			</tr>
		    </table>
		</td>
	    </c:if>
	</tr>
    </table>
</div>
<%-- 
    Document   : tradingPartnerExistsCustomerList
    Created on : Nov 14, 2016, 2:45:41 PM
    Author     : Stefy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/css/common.css" />
<input type="hidden" name="tpFlag" id="tpFlag"/>
<input type="hidden" name="tpacctNo" id="tpacctNo"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">
                Exists Customer List
            </td>
            <td>
                <div style="vertical-align: top">
                    <a id="lightBoxClose" href="javascript: cancelAdd();">
                        <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
        <tr>

        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable" style="width: 100%;height: 90%;overflow: auto;border-color: #000000">
    <table class="displaytagstyleNew"
           id="documentListItem" style="width:100%" >
        <thead>
            <tr>
                <th></th>
                <th>Customer Name</th>
                <th>Account No</th>
                <th>Account Type</th>
                <th>UnLocation Code</th>
                <th>Address</th>
            </tr>
        </thead>
        <c:if test="${not empty customerList}">
            <tbody>
                <c:forEach var="customer" items="${customerList}">
                    <c:set var="zebra" value="${zebra eq 'odd' ? 'even' : 'odd'}"/>
                    <tr class="${zebra}">
                        <td>
                            <input type="radio" id="existingaccount" name="existingaccount"
                                   value="${customer.accountName}==${customer.accountno}==${customer.customerLocation.unLocationCode}==${customer.custAddr.address1}==${customer.custAddr.city2}
                                   ==${customer.custAddr.state}==${customer.custAddr.zip}==${customer.custAddr.country}==${customer.custAddr.phone}==${customer.custAddr.email1}==${customer.custAddr.fax}"/>
                        </td>
                        <td>${customer.accountName}</td>
                        <td>${customer.accountno}</td>
                        <td>${customer.acctType}</td>
                        <td>${customer.customerLocation.unLocationCode}</td>
                        <td>${customer.custAddr.address1}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </c:if>
    </table>
    <form style="text-align:center;padding-right:4px;padding-bottom: 4px;background:#DFE0DF">
        <input type="button" style="height: 20px; width: 90px"  class="buttonStyleNew" value="Proceed"  onclick="createNewTp('${path}');" />
        <input type="button" style="height: 20px; width: 90px" class="buttonStyleNew" value="Cancel"  onclick="cancelAdd();" />
        <input type="button" style="height: 20px; width: 140px" class="buttonStyleNew" value="Use Selected Account"  onclick="useSelectedAccount();" />
    </form>
</div>

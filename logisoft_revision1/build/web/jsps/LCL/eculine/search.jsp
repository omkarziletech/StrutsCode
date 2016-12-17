<%-- 
    Document   : search
    Created on : Jun 12, 2013, 3:31:02 PM
    Author     : Rajesh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Manifest</title>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/LCL/colorBox.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineEdi.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                setApprovedMsg();
            });
        </script>
    </head>
    <body>
        <html:form action="/lclEculineEdi.do" name="lclEculineEdiForm"
                   styleId="lclEculineEdiForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiForm" scope="request" method="post">
            <div id="approved-msg" style="color:green; margin-right: 90px; z-index: -1"  class="arial font-12px bold">
                <img alt="Approved" id="approved-img" title="Approved" src="${path}/jsps/LCL/images/approve.png" class="approve"/>
                ${approved}
            </div>
            <table class="table">
                <tr>
                    <th colspan="16">Search Criteria</th>
                </tr>
                <tr>
                    <td class="label">Unit #</td>
                    <td>
                        <cong:autocompletor name="containerNo" template="one" id="containerNo" query="ECULINE_UNIT"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${contNo}"
                                            width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="search('byUnitNo')"/>
                    </td>
                    <td class="label">POL</td>
                    <td>
                        <cong:autocompletor name="polUncode" template="one" id="polUncode" query="ORIGIN_UNLOC"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${pol}"
                                            width="250" container="NULL" shouldMatch="true" scrollHeight="200px"
                                            callback="getUnloc('#polUncode', ''); search('byUnitNo')"/>
                    </td>
                    <td class="label">POD</td>
                    <td>
                        <cong:autocompletor name="podUncode" template="one" id="podUncode" query="PORT"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${pod}" width="250"
                                            container="NULL" shouldMatch="true" scrollHeight="200px"
                                            callback="getUnloc('#podUncode', ''); search('byUnitNo')"/>
                    </td>
                    <td class="label">Reference #</td>
                    <td>
                        <cong:autocompletor name="referenceNo" template="one" id="referenceNo" query="ECULINE_REFNO"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${refNo}"
                                            width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="search('byUnitNo')"/>
                    </td>
                    <td class="label">Invoice #</td>
                    <td>
                        <cong:autocompletor name="invoiceNumber" template="one" id="invoiceNumber" query="ECULINE_INVOICE_NO"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="${invoiceNumber}"
                                            width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="search('byUnitNo')"/>
                    </td>
                    <td>
                        <cong:checkbox  name="approved" id="approved" value="${containerApproved}"/>
                        <label class="label" for="approved">Approved</label>
                    </td>
                    <td>
                        <cong:checkbox name="unapproved" id="unapproved"  value="${containerUnApproved}"/>
                        <label class="label" for="unapproved">Un Approved</label>
                    </td>
                    <td>
                        <cong:checkbox name="readyToApproved" id="readyToApproved"  value="${containerreadyToApproved}"/>
                        <label class="label" for="readyToApproved">Ready To Approve</label>
                    </td>
                </tr>
                <tr>
                    <td colspan="16" class="align-center">
                        <html:hidden property="id" styleId="id"/>
                        <html:hidden property="voyNo" styleId="voyNo"/>
                        <html:hidden property="methodName" styleId="methodName"/>
                        <html:hidden property="containerNo" styleId="containerNo" value="${contNo}"/>
                        <input type="button" value="Search" class="button" onclick="search();"/>
                        <input type="button" value="Reset" class="button" onclick="resetAll();"/>
                        <label class="label">Limit</label>
                        <select name="limit" id="limit" class="dropdown" style="max-width: 50px">
                            <option value="25" ${lclEculineEdiForm.limit eq 25 ? 'selected' : ''}>25</option>
                            <option value="50" ${lclEculineEdiForm.limit eq 50 ? 'selected' : ''}>50</option>
                            <option value="100" ${lclEculineEdiForm.limit eq 100 ? 'selected' : ''}>100</option>
                            <option value="250" ${lclEculineEdiForm.limit eq 250 ? 'selected' : ''}>250</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th colspan="16">Search Results</th>
                </tr>
                <tr>
                    <td colspan="16">
                        <c:import url="/jsps/LCL/eculine/searchResults.jsp"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>

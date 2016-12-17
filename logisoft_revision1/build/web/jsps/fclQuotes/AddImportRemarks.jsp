<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <title>JSP for RemarksLookUpForm form</title>
    </head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <%@include file="../includes/baseResources.jsp"%>
    <body class="whitebackgrnd">
         <html:form action="/remarksLookUp" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="2">Search</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Add Remarks</td>
                    <td>
                        <textarea onkeypress="return checkTextAreaLimit(this, 250)"  style="text-transform: uppercase"  cols="60" rows="5"
                                  name="addRemarks" class="textlabelsBoldForTextBox" id="addRemarks"></textarea>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td align="center" colspan="2">
                        <input type="button" class="buttonStyleNew" value="Close" onclick="closeAddRemarks()">
                        <input type="button" class="buttonStyleNew" value="save" onclick="saveRemarks()">
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>


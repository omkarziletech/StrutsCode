<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>
<%@include file="/jsps/includes/resources.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>


<html>
    <head>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1" />

        <title>UnLocation Remarks Page</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>

    </head>
    <body class="whitebackgrnd">
        <html:form action="/unlocation" name="searchUnLocationForm" styleId="searchUnLocationForm"
                   type="com.logiware.form.SearchUnLocationForm" scope="request">
            <html:hidden property="buttonValue" />
            <table class="tableBorderNew" width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="textlabels">
                        Remarks
                    </td>
                    <td align="center" style="padding-top: 20px;width: 100%;">
                        <textarea  class="textlabelsBoldForTextBox" rows="4" cols="100" id="remark"
                                   name="remarks" ><c:out value="${remarks}"/></textarea>
                    </td>
                </tr>
                <tr  align="center">
                    <td>
                        <input type="button" class="buttonStyleNew" value="Save" onClick="saveRemarks()"/>
                        <input type="button" class="buttonStyleNew" value="Cancel" onClick="cancelRemarks()"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    
    <script type="text/javascript">
        function saveRemarks() {
            parent.parent.saveRemarks(document.searchUnLocationForm.remarks.value);
        }
        function cancelRemarks() {
            parent.parent.cancelRemarks();
        }
    </script>
</html>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
    <tbody>
        <tr>
            <td class="lightBoxHeader">
				User Details
            </td>
            <td>
                <div>
                    <a id="lightBoxClose"  href="javascript:closeAddUserName();">
                        <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<br/>
<table class="textLabels" align="center">
    <tr>
        <td>User Name</td>
        <td>
            <input name="user" id="user" style="text-transform: uppercase;"/>
            <input type="hidden" name="userValid" id="userValid">
            <div id="userNameDiv" class="newAutoComplete"></div>
        </td>
    </tr>
</table>




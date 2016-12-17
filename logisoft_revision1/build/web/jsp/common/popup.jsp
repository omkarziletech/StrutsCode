<%-- 
    Document   : popup
    Created on : Apr 24, 2013, 10:17:34 PM
    Author     : Rajesh
--%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<link type="text/css" rel="stylesheet" href="${path}/css/popupstyle.css"/>

<%-- Generic popup --%>
<div id="remarks-div" class="remarks-popup popup-div">
    <table class="popup-table" border="0" cellspacing="0">
        <tr class="tableHeadingNew" >
            <th id="remarks-msg" align="left" class="trebuchet-ms font-14px">
                <div id="remarks-title" class="trebuchet-ms font-14px"></div>
            </th>
            <th align="right">
                <img alt="Close" src="${path}/jsps/LCL/images/close1.png" onclick="cancelRemarks();" title="Close" id="close-img" class="remark"/>
            </th>
        </tr>
        <tr>
            <td class="textlabelsBold" colspan="2">
                <html:textarea rows="5" cols="78" styleId="override-remarks" property="overrideRemarks" styleClass="calibri font-12px uppercase"
                               onkeypress="return checkTextAreaLimit(this, 500);"></html:textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input id="submit-remarks" type="button" value="Submit" class="buttonStyleNew" onclick="saveRemarks();">
                <input id="cancel-remarks" type="button" value="Cancel" class="buttonStyleNew" onclick="cancelRemarks();"/>
            </td>
        </tr>
    </table>
</div>
<%-- popup ends --%>  

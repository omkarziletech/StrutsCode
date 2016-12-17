<%-- 
    Document   : addOrEdit
    Created on : 18 Jan, 2015, 4:26:01 PM
    Author     : Lucky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
    <table class="table margin-none border-none">
        <tr>
            <td class="label">
                Screen Name <span class="red">*</span>
            </td>
            <td>
                <select id="addOrEditScreenName" class="dropdown">
                    <c:forEach var="screenName" items="${scanForm.screenNames}">
                        <option value="${screenName.codedesc}" ${scanForm.scanConfig.screenName eq screenName.codedesc ? 'selected' : ''}>
                            ${screenName.codedesc}
                        </option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td class="label">
                Document Name <span class="red">*</span>
            </td>
            <td>
                <input type="text" id="addOrEditDocumentName" class="textbox" value="${scanForm.scanConfig.documentName}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="align-center">
                <input type="hidden" id="addOrEditId" value="${scanForm.scanConfig.id}"/>
                <input type="button" class="button" value="Save" onclick="saveDocument();"/>
                <input type="button" class="button" value="Cancel" onclick="Lightbox.close();"/>
            </td>
        </tr>
    </table>
</div>
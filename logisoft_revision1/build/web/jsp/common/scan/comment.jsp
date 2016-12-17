<%-- 
    Document   : comment
    Created on : 17 Jan, 2015, 2:35:50 AM
    Author     : Lucky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<div>
    <table class="table margin-none border-none">
        <tr>
            <td class="label">
                Comment
                <c:if test="${param.screenName ne 'INVOICE' && param.screenName ne 'AR BATCH' && param.screenName ne 'JOURNAL ENTRY'}">
                    <span class="red">*</span>
                </c:if>
            </td>
            <td>
                <textarea name="comment" id="comment" class="textarea" rows="2" cols="30"></textarea>
            </td>
        </tr>
        <c:if test="${param.documentName eq 'SS LINE MASTER BL'}">
            <tr>
                <td class="label valign-middle">Status</td>
                <td class="label">
                    <input type="radio" name="status" id="statusApproved" value="Approved" checked/>
                    <label for="statusApproved">Approved</label>
                    &nbsp;&nbsp;
                    <input type="radio" name="status" id="statusDisputed" value="Disputed"/>
                    <label for="statusDisputed">Disputed</label>
                </td>
            </tr>
        </c:if>
        <c:if test="${param.action eq 'Attach'}">
            <tr>
                <td class="label valign-middle">Select File</td>
                <td>
                    <div class="label">
                        <input type="file" name="document" id="document" tabindex="-1"/>
                    </div>
                </td>
            </tr>
        </c:if>    
        <tr>
            <td colspan="2" class="align-center">
                <c:choose>
                    <c:when test="${param.action eq 'Scan'}">
                        <input type="button" class="button" value="Scan" onclick="scan('${param.documentName}');"/>
                    </c:when>
                    <c:otherwise>
                        <input type="button" class="button" value="Attach" onclick="attach('${param.documentName}');"/>
                        <input type="button" class="button" value="Drag & Drop" onclick="dragAndDrop('${param.documentName}');"/>
                    </c:otherwise>
                </c:choose>
                <input type="button" class="button" value="Cancel" onclick="Lightbox.close();"/>
            </td>
        </tr>
    </table>
</div>
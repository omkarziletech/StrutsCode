<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="title"%>
<%@attribute name="id"%>
<%@attribute name="styleClass"%>

<table class="dialog ${styleClass}">
    <thead>
        <tr>
            <th class="title">
                ${title}
                <img alt="close" src="${path}/images/common/icons/close.png" title="close" class="close" onclick="this.parentNode.parentNode.parentNode.parentNode.style.display = 'none';"/>
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="content" id="content_${id}">
                <jsp:doBody/>
            </td>
        </tr>
    </tbody>
</table>

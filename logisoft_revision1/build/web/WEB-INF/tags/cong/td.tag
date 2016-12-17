<%@attribute name="width"%>
<%@attribute name="align"%>
<%@attribute name="colspan"%>
<%@attribute name="rowspan"%>
<%@attribute name="styleClass"%>
<%@attribute name="valign"%>
<%@attribute name="onclick"%>
<%@attribute name="style"%>

<%@attribute name="id"%>
<td onclick="${onclick}" style="${style}" width="${width}" align="${align}" colspan="${colspan}" id="${id}" class="${styleClass}" rowspan="${rowspan}" valign="${valign}">
    <jsp:doBody/>
</td>

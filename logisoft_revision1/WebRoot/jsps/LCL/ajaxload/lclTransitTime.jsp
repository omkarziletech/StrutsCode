<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<cong:tr>
    <cong:td styleClass="textlabelsBoldforlcl" width="3.75%">Transit Time</cong:td>
<cong:td width="6.25%">
    <input type="text" class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="transitTime" name="transitTime" value="${transitTime eq 0 ? " " : transitTime}"/>
</cong:td>
</cong:tr>
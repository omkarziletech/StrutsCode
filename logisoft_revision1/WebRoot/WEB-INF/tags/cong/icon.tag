<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="type" required="true"%>
<%@attribute name="title" %>
<%@attribute name="onclick"%>
<label class="${type} icon" onclick="${onclick}" title="${title}"/>

<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@attribute name="name" required="true" %>
<%@attribute name="styleClass"%>
<html:file property="${name}" styleId="${name}" styleClass="${styleClass}"/>

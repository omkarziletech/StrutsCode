<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@attribute name="key" required="true"%>
<%@attribute name="arg0"%>
<%@attribute name="arg1"%>
<%@attribute name="arg2"%>
<%@attribute name="arg3"%>

<bean:message key='${key}' arg0="${arg0}" arg1="${arg1}" arg2="${arg2}"  arg3="${arg3}" />

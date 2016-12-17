<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<jsp:useBean id="lclSearchForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclSearchForm"/>
<html:select name="lclSearchForm" property="sortBy" styleId="sortBy" styleClass="smallDropDown textlabelsBoldforlcl" style="text-transform:uppercase">
    <html:option value="">SELECT</html:option>
    <html:optionsCollection name="lclSearchForm" property="templateList" value="id" label="templateName"/>
</html:select>

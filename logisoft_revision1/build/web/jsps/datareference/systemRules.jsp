<%-- 
    Document   : systemRules
    Created on : Mar 9, 2011, 4:00:48 PM
    Author     : vellaisamy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/baseResourcesForJS.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/systemRules" name="systemRulesForm" type="com.logiware.form.SystemRulesForm" scope="request">
            <c:choose>
                <c:when test="${not empty systemRulesSublist1 || not empty systemRulesSublist2}">
                     <table width="100%" border="0" cellpadding="0"  cellspacing="0">
                         <tr class="tableHeadingNew">
                         <span style="float: left">Company Info</span><input type="submit" class="buttonStyleNew" value="Save" style="width: 50px;float: right"/>
                         </tr>
                         <tr>
                             <c:if test="${not empty systemRulesSublist1}">
                                 <td valign="top">
                                     <display:table pagesize="50" name="${systemRulesSublist1}" class="displaytagstyleNew" id="systemRulesSublist1">
                                     <display:setProperty name="paging.banner.placement" value="none"/>
                                     <display:column property="ruleCode" title="Label">
                                     </display:column>
                                     <display:column title="Value">
                                         <c:choose>
                                             <c:when test="${systemRulesSublist1.fieldType == 'TEXTAREA'}">
                                                 <html:textarea property="ruleName" styleId="ruleName" rows="3" cols="30" styleClass="textlabelsBoldForTextBox" value="${systemRulesSublist1.ruleName}"></html:textarea>
                                             </c:when>
                                             <c:otherwise>
                                                 <html:text property="ruleName" styleId="ruleName" size="30" styleClass="textlabelsBoldForTextBox" value="${systemRulesSublist1.ruleName}"></html:text>
                                             </c:otherwise>

                                         </c:choose>
                                         <html:hidden property="ruleId" value="${systemRulesSublist1.id}"/>
                                     </display:column>
                                    </display:table>
                                 </td>
                             </c:if>
                              <c:if test="${not empty systemRulesSubList2}">
                                  <td valign="top">
                                     <display:table pagesize="50" name="${systemRulesSubList2}" class="displaytagstyleNew" id="systemRulesSubList2">
                                         <display:setProperty name="paging.banner.placement" value="none"/>
                                         <display:column property="ruleCode" title="Label">
                                         </display:column>
                                         <display:column title="Value">
                                             <c:choose>
                                                 <c:when test="${systemRulesSubList2.fieldType == 'TEXTAREA'}">
                                                     <html:textarea property="ruleName" styleId="ruleName" rows="3" cols="30" styleClass="textlabelsBoldForTextBox" value="${systemRulesSubList2.ruleName}"></html:textarea>
                                                 </c:when>
                                                 <c:otherwise>
                                                     <html:text property="ruleName" styleId="ruleName" size="30" styleClass="textlabelsBoldForTextBox" value="${systemRulesSubList2.ruleName}"></html:text>
                                                 </c:otherwise>

                                             </c:choose>
                                             <html:hidden property="ruleId" value="${systemRulesSubList2.id}"/>
                                          </display:column>
                                     </display:table>
                                  </td>
                             </c:if>
                         </tr>
                     </table>
                </c:when>
                <c:otherwise>
                    No Documents Found
                </c:otherwise>
            </c:choose>
           
            <input type="hidden" name="buttonValue" id="buttonValue" value="update"/>
        </html:form>
        
    </body>
</html>

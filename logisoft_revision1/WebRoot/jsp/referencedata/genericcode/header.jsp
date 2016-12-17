<%-- 
    Document   : header
    Created on : Apr 22, 2014, 3:17:54 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<table class="table no-margin-border">
    <tr>
        <td>
            <span class="label">Code Type</span>
            <span>
                <html:select property="genericCode.codetypeid" styleId="codetypeid" styleClass="dropdown">
                    <html:option value="">Select Codes</html:option>
                    <html:optionsCollection property="codeTypes" label="description" value="codetypeid"/>
                </html:select>
            </span>
            <span class="label margin-0-0-0-10">Code Description</span>
            <span>
                <html:text property="genericCode.codedesc" styleId="codedesc" styleClass="textbox"/>
            </span>
            <span class="label margin-0-0-0-10">Code</span>
            <span>
                <html:text property="genericCode.code" styleId="code" styleClass="textbox"/>
            </span>
            <span class="margin-0-0-0-10">
                <input type="button" class="button" value="Search" onclick="search();"/>
                <input type="button" class="button" value="Clear" onclick="clearAll();"/>
                <input type="button" class="button" value="Add New" onclick="add();"/>
            </span>
            <c:if test="${genericCodeForm.codeType.loadFromBluescreen eq 'Y'}">
                <span class="blue bold italic margin-0-0-0-10">These records are loaded from blue screen</span>
            </c:if>
            <c:if test="${not empty error}">
                <span class="error italic margin-0-0-0-10">${error}</span>
            </c:if>
            <c:if test="${not empty message}">
                <span class="message italic margin-0-0-0-10">${message}</span>
            </c:if>
        </td>
    </tr>
</table>
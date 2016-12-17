<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.util.DBUtil" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%
            DBUtil dbUtil = new DBUtil();
            request.setAttribute("budgetSet", dbUtil.getbudgetsetForFiscalPeriod());
            request.setAttribute("years", dbUtil.getFiscalYearForStatus(null));
            request.setAttribute("periods", dbUtil.getmonthList1());
            String toPeriod = request.getParameter("peridTo");
            String year = request.getParameter("year");
            if (null != toPeriod && toPeriod.length() == 1) {
                toPeriod = "0" + toPeriod;
            }
            request.setAttribute("toPeriod", toPeriod);
            request.setAttribute("year", year);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Income Statement Report</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/fiscalperiod" name="fiscalPeriodForm" type="com.gp.cvst.logisoft.struts.form.FiscalPeriodForm" scope="request" >
            <table width="100%" border="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Generate - Income Statement Report</td>
                </tr>
                <tr>
                    <td>
                        <table width="40%" border="0">
                            <tr class="textlabels">
                                <td>Fiscal Year</td>
                                <td>
                                    <html:select property="year" styleClass="textlabelsBoldForTextBox" value="${year}">
                                        <html:optionsCollection name="years" styleClass="unfixedtextfiledstyle"/>
                                    </html:select>
                                </td>
                                <td>From Period</td>
                                <td>
                                    <html:select property="fromPeriod" styleClass="textlabelsBoldForTextBox">
                                        <c:forEach var="period" items="${periods}">
                                            <html:option value="${period.value}" key="${period.label}"/>
                                        </c:forEach>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="textlabels">
                                <td>Budget Set</td>
                                <td>
                                    <html:select property="copybudgetset" styleClass="textlabelsBoldForTextBox">
                                        <html:optionsCollection name="budgetSet" styleClass="unfixedtextfiledstyle"/>
                                    </html:select>
                                </td>
                                <td>To Period</td>
                                <td>
                                    <html:select property="toPeriod" styleClass="textlabelsBoldForTextBox" value="${toPeriod}">
                                        <c:forEach var="period" items="${periods}">
                                            <html:option value="${period.value}" key="${period.label}"/>
                                        </c:forEach>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="4" align="center">
                                    <input type="button" value="Generate Report" class="buttonStyleNew" style="width:120px" onclick="report()"/>
                                    <input type="button" value="Go Back" class="buttonStyleNew" onclick="goBack()"/>
                                    <input type="button" name="exporttoexcel" style="width:90px" value="ExportToExcel" onclick="exportExcel()" class="buttonStyleNew"/>
                                    <input type="button" name="email" value="Email" class="buttonStyleNew" onclick="openMailPopup()"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
        </html:form>
        <script type="text/javascript">
            function report(){
                document.fiscalPeriodForm.buttonValue.value = "IncomeStatementReport";
                document.fiscalPeriodForm.submit();
            }
            function goBack(){
                window.location.href="${path}/jsps/Accounting/SearchFiscalCalender.jsp";
            }
            function exportExcel(){
                document.fiscalPeriodForm.buttonValue.value = "exportToExcel";
                document.fiscalPeriodForm.submit();
            }
            function openMailPopup(){
                GB_show('Email','${path}/sendEmail.do?id='+document.fiscalPeriodForm.year.value+'&fromPeriod='+document.fiscalPeriodForm.fromPeriod.value+'&toPeriod='+
                    document.fiscalPeriodForm.toPeriod.value+'&reportTitle='+'IncomeStatementReport'+'&moduleName=FiscalPeriod',455,650);
            }
        </script>
        <c:if test="${not empty fileName}">
            <script type="text/javascript">
                window.parent.showGreyBox("Income Statement Report","${path}/servlet/FileViewerServlet?fileName=${fileName}");
            </script>
        </c:if>
    </body>
</html>

<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesConstants"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <%@include file="../includes/jspVariables.jsp"%>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>FCL Quotation Rates</title>
         <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>
        <script  language="javascript" type="text/javascript">
        </script>
        <link rel="stylesheet" href="${path}/css/default/style.css"
              type="text/css" media="print, projection, screen" />
        <link rel="alternate stylesheet" type="text/css" media="all"
              href="${path}/css/cal/calendar-win2k-cold-2.css"
              title="win2k-cold-2" />
        <link rel="stylesheet" type="text/css"
              href="${path}/css/cal/skins/aqua/theme.css" title="Aqua" />
        <link rel="stylesheet" type="text/css" href="${path}/css/sweetTitles.css" />
    </head>
    <body class="whitebackgrnd">
        <html:form action="/fclQuotes" name="fclQuotesPopupForm" type="com.gp.cvst.logisoft.struts.form.fclQuotesPopupForm" scope="request">
            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>
                        Rates Quick Review
                    </td>
                    <td>
                        <div style="vertical-align: top">
                            <a id="lightBoxClose" href="javascript: enableQuickRates();closeDocList();">
                                <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                            </a>
                        </div>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        <table border="0" width="100%" cellpadding="3">
                            <tr class="textlabelsBold">
                                <td>Destination</td>
                                <td>
                                    <input Class="textlabelsBoldForTextBox" name="portofDischarge" id="portofDischarge" size="22"
                                           onkeydown="getTemp()"/>
                                     <input type="hidden" id="portofDischarge_check" value=""/>
                                    <div id="destination_port_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                    <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                        <input type="checkbox" id="destinationCity" checked="checked"/></span>
                                    <input type="checkbox" id="showAllCity" onclick="checkShowAllCity();" onmouseover="tooltip.show('<strong>Show Rates for Entire Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                </td>
                                <td>Commodity</td>
                                <td>
                                    <input Class="textlabelsBoldForTextBox" name="commcode"
                                           id="commcode" maxlength="7" size="22" value="006100" />
                                    <input id="commcode_check" type="hidden" value=""/>
					<div id="commcode_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Origin</td>
                                <td id="isTerminal1">
                                    <input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox"
                                           size="22" onkeydown="getTemp1()"/>
                                    <input type="hidden" id="isTerminal_check" value=""/>
                                    <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                        <input type="checkbox" checked="checked" id="originCountry"/>   </span>
                                    <div id="isTerminal_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                </td>
                                <td>Hazmat</td>
                                <td>
                                    <html:radio property="hazmat" value="Y" />Y
                                    <html:radio property="hazmat" value="N" />N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td colspan="4" align="center">
                                    <input type="button" id="getRates" onClick="getQuickRates()" Value="Rates" style="width: 52px;" class="buttonStyleNew" />
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <%@include file="../includes/resources.jsp"%>
</html>

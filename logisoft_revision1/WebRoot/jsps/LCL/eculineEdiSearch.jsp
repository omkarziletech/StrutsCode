<%-- 
    Document   : eculineEdiSearch
    Created on : Jun 12, 2013, 3:31:02 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Manifest</title>
        <%@include file="init.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineEdi.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                //approved message
                if($('#approved-msg').text().replace(/\s+/g, '') != '') {
                    setTimeout(function() { $("#approved-msg").fadeOut('slow', 'swing'); }, 1000);
                    setTimeout(function() { $("#approved-msg").fadeIn('slow', 'swing'); }, 1100);
                } else {
                    $('#approved-msg').hide();
                    $('#approved-img').hide();
                }
                //tooltip
                $("[title != '']").not("link").tooltip();
            });
        </script>
    </head>
    <body>
        <br/>
        <html:form action="/lclEculineEdi.do" name="lclEculineEdiForm"
                   styleId="lclEculineEdiForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiForm" scope="request" method="post">
            <table class="width-50pc">
                <tr>
                    <td class="label">Unit #</td>
                    <td>
                        <html:text styleId="containerNo" property="containerNo" styleClass="textbox" />
                    </td>
                    <td class="label">POL</td>
                    <td>
                        <cong:autocompletor name="polUncode" template="tradingPartner" id="polUncode" query="ORIGIN_UNLOC"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value=""
                                            width="250" container="NULL" shouldMatch="true" scrollHeight="200px"
                                            callback="getUnloc('#polUncode', '');"/>
                    </td>
                    <td class="label">POD</td>
                    <td>
                        <cong:autocompletor name="podUncode" template="tradingPartner" id="podUncode" query="PORT"
                                            fields="" styleClass="textlabelsBoldForTextBox textCap" value="" width="250"
                                            container="NULL" shouldMatch="true" scrollHeight="200px"
                                            callback="getUnloc('#podUncode', '');"/>
                    </td>
                    <td>
                        <input type="hidden" id="methodName" name="methodName"/>
                        <input type="hidden" id="voyNo" name="voyNo"/>
                    </td>
                    <td colspan="3"></td>
                    <td>
                        <input type="button" value="Search" class="button" onclick="search('byUnitNo');"/>
                        <input type="button" value="Search All" class="button" onclick="search('all');"/>
                    </td>
                    <td>
                        <div id="approved-msg" style="color:green; margin-right: 90px; z-index: -1" class="arial font-12px bold">
                            <img alt="Approved" id="approved-img" title="Approved" src="${path}/jsps/LCL/images/approve.png" class="approve"/>
                            ${approved}
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
        <br/>
        <display:table name="${eculineEdiList}" id="eculineEdi" class="display-table" requestURI="/lclEculineEdi.do">
            <display:column title="Container #" property="containerNo" sortable="true"></display:column>
            <display:column title="Sail Date" property="sailDate" sortable="true"></display:column>
            <display:column title="Arvl Date" property="arvlDate" sortable="true"></display:column>
            <display:column title="Voy" property="voyNo" sortable="true"></display:column>
            <display:column title="POL" sortable="true">
                <span title="${eculineEdi.origin}">${eculineEdi.polUncode}</span>
            </display:column>
            <display:column title="POD" sortable="true">
                <span title="${eculineEdi.destination}">${eculineEdi.podUncode}</span>
            </display:column>
            <display:column title="Billing Terminal" property="billingTerminal" sortable="true"></display:column>
            <display:column title="Vessel" sortable="true">
                <c:choose>
                    <c:when test="${not empty eculineEdi.vesselCode}">
                        <span id="vessel" class="greenBold">${eculineEdi.vesselName}</span>
                    </c:when>
                    <c:otherwise>
                        <span id="vessel" class="redBold11px" title="vessel not mapped">${eculineEdi.vesselName}</span>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Size" property="contSize" sortable="true"></display:column>
            <display:column title="Pieces" property="pieces" sortable="true"></display:column>
            <display:column title="Cube" property="cube" sortable="true"></display:column>
            <display:column title="Weight" property="weight" sortable="true"></display:column>
            <display:column title="Ref #" property="refNo" sortable="true"></display:column>
            <display:column title="File">
                <img alt="View" title="Click to Open XML file" src="${path}/images/xml.gif"
                     class="xmlfile" onclick="viewXmlFile('${path}','${eculineEdi.id}');"/>
            </display:column>
            <display:column title="Action">
                <img alt="View" title="Click to view BL List for voy # ${eculineEdi.voyNo}" src="${path}/img/icons/view.gif"
                     class="view" onclick="viewBl('${path}','${eculineEdi.voyNo}');"/>
                <c:choose>
                    <c:when test="${eculineEdi.approved == true}">
                        <img alt="Approved" title="Approved voy # ${eculineEdi.voyNo}" src="${path}/jsps/LCL/images/approve.png" />
                    </c:when>
                    <c:when test="${not empty eculineEdi.billingTerminal && not empty eculineEdi.sslineNo && not eculineEdi.havingError}">
                        <img alt="Process" title="Click to approve voy # ${eculineEdi.voyNo}" src="${path}/jsps/LCL/images/tick.png"
                             class="edit"onclick="approveVoy('${eculineEdi.voyNo}');"/>
                    </c:when>
                    <c:otherwise>
                        <img alt="Error" title="Attention needed voy # ${eculineEdi.voyNo}" src="${path}/jsps/LCL/images/close1.png"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
        </display:table>
    </body>
</html>

<%--
    Document   : lclSegregationDr
    Created on : May 11, 2015, 12:34:12 PM
    Author     : Mohanapriya
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <%@include file="init.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="colorBox.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
        <cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
        <cong:javascript src="${path}/jsps/LCL/js/lclSegregation.js"/>
    </head>
    <body>
        <cong:div style="width:100%; float:left;">
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                Original File No: <span class="fileNo">${fileNumber}</span>
            </cong:div><br><br>
            <cong:form name="lclSegregationForm" id="lclSegregationForm" action="lclSegregation.do" method="post">
                <table width="100%" border="0">
                    <cong:tr>
                        <td class="textlabelsBoldforlcl">Final Destination</td>
                        <td>
                            <cong:autocompletor name="segDest" id="segDest" template="two" query="DEST_UNLOC" 
                                                fields="NULL,NULL,NULL,segDestId"
                                                shouldMatch="true" scrollHeight="150" width="320" value="${segDestination}"
                                                container="NULL" styleClass="mandatory textlabelsBoldForTextBoxWidth"/>
                        </td>
                    </cong:tr>
                    <cong:tr>
                        <td class="textlabelsBoldforlcl">AMS HBL</td>
                        <td> <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:76px" 
                                   name="amsHbl" id="amsHbl" value="${amsHbl}" readOnly="true"/></td>
                        </cong:tr>
                        <cong:tr>
                        <td class="textlabelsBoldforlcl">Pieces</td>
                        <td> <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:76px" 
                                   name="pieces" id="pieces" readOnly="true" value="${pcs}"/></td>
                        </cong:tr>
                        <cong:tr>
                        <td class="textlabelsBoldforlcl">CBM</td>
                        <td>
                            <cong:text styleClass="textlabelsBoldForTextBox weight mandatory" style="width:76px" onchange="correctBookedVolumeMetric(this)"
                                       name="cbm" id="cbm" onkeyup="checkForNumberAndDecimal(this);"/>
                        </td>
                    </cong:tr>
                    <cong:tr>
                        <td class="textlabelsBoldforlcl">KGS</td>
                        <td>
                            <cong:text styleClass="textlabelsBoldForTextBox weight mandatory" style="width:76px" onchange="correctBookedWeightMetric(this)"
                                       name="kgs" id="kgs" onkeyup="checkForNumberAndDecimal(this);"/>
                        </td></cong:tr>
                    <cong:tr>
                        <td class="textlabelsBoldforlcl">Transshipment</td>
                        <cong:td>
                            <input type="radio" name="transshipment" value="Y" id="transshipmentY"  container="NULL" onclick="changeFdDojo();"/>Yes
                            <input type="radio" name="transshipment" value="N" id="transshipmentN" checked="yes" container="NULL" onclick="changeFdDojo();"/>No
                        </cong:td>
                    </cong:tr>
                </table><table align="center">
                    <cong:tr>
                        <cong:td>
                            <input type="button" class="button-style1" value="Create DR" onclick="createSegregationDr()"/>
                            <input type="button" class="button-style1" value="Abort" onclick="abortSegregation()"/>
                        </cong:td>
                    </cong:tr>
                </table>
                <input type="hidden" name="methodName" id="methodName"/>
                <input type="hidden" name="amsHblId" id="amsHblId" value="${amsHblId}"/>
                <input type="hidden" name="fileId" id="fileId" value="${fileNumberId}"/>
                <input type="hidden" name="lbs" id="lbs"/>
                <input type="hidden" name="cft" id="cft"/>
                <input type="hidden" name="segDestId" id="segDestId"/>
            </cong:form>
        </cong:div>
    </body>
</html>

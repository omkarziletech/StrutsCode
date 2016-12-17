<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="init.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="colorBox.jsp" %>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <%@include file="../fragment/lclFormSerialize.jspf"  %>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <cong:javascript src="${path}/jsps/LCL/js/common.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/lclQuote.js"/>
    <script type="text/javascript" src="${path}/jsps/LCL/js/lclImpQuote.js"></script>
    <%@include file="/taglib.jsp" %>
      <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 20%;
                right: 0;
                top: 20%;
            }
        </style>
    <body style="overflow:auto">
        <div id="pane">
            <c:if test="${lockMessage!='' && lockMessage!=null }">
                <center><font color="#FF4A4A" size="2"><b style="margin-left:150px;color: #000080;font-size: 15px;" >${lockMessage}</b></font></center>
            </c:if>
            <cong:form name="lclQuoteForm" id="lclQuoteForm" action="lclQuote.do">
                 <div id="cover"></div> 
                <cong:hidden name="quoteType" id="quoteType" value="I"/>
                <cong:hidden name="homeScreenQtFileFlag" id="homeScreenQtFileFlag" value="${lclQuoteForm.homeScreenQtFileFlag}"/>
                <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="<bean:message key="application.spotRate.commodityCode"/>"/>
                <cong:hidden name="index" id="index" />
                <%@include file="quote/status.jsp" %>
                <%@include file="quote/button.jsp" %>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <cong:table>
                                    <cong:tr>
                                        <cong:td styleClass="black-border">
                                            <%@include file="quote/importClient.jsp" %>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- First Row ends here -->
                        <!--for Imports only-->

                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="/jsps/LCL/tradingPartnerQuote.jsp" %>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="quote/importTradeRoute.jsp" %>
                            </cong:td>
                        </cong:tr><!-- Second Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <cong:table>
                                    <cong:tr styleClass="caption">
                                        <cong:td>
                                            <cong:table>
                                                <cong:tr>
                                                    <cong:td style="text-align:right; width:50%">
                                                        Commodity
                                                    </cong:td>
                                                    <cong:td style="text-align:left;width:50%">
                                                        <cong:div>
                                                            <span class="button-style1 floatLeft addCommodity" id="Qcommodity1" onclick="quoteCommodity('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', 'false', '${lclQuoteForm.moduleName}')"> Add New</span>
                                                        </cong:div>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td>
                                            <cong:table  cellpadding="0" cellspacing="0" style="border-collapse: collapse" width="100%">

                                                <cong:tr>
                                                    <cong:td id="commodityDesc">
                                                        <!-- add the commodity descriptin list -->
                                                        <c:import url="/jsps/LCL/commodityQuoteDesc.jsp">
                                                            <c:param name="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
                                                            <c:param name="fileNumber" value="${lclQuote.lclFileNumber.fileNumber}"/>
                                                            <c:param name="copyFnVal" value="${lclQuoteForm.copyFnVal}"/>
                                                        </c:import>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- third Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td  width="100%" valign="top" styleClass="black-border"  id="upcomingImpSection">
                                <cong:table border="0">
                                    <tr class="caption" onclick="toggleUpcomingSailings()" style="cursor: pointer">
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">&nbsp;&nbsp;Upcoming Sailings&nbsp;&nbsp;</cong:td>
                                        <cong:td valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >&nbsp;&nbsp;Origin:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label id="originSailing"  styleClass="greenBold" style="width:100px"></cong:label>
                                        </cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >POL:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label styleClass="greenBold" style="width:190px" id="polSailing"/></cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">POD:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label text="" styleClass="greenBold" style="width:290px" id="podSailing"/></cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">Destination:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label text="" styleClass="greenBold" style="width:290px" id="destinationSailing"/></cong:td>
                                        <td align="right" width="10%">
                                            <cong:label id="col">Click to Expand</cong:label>
                                            <cong:label id="exp">Click to Hide</cong:label>
                                            <a href="javascript: void()">
                                                <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
                                            </a>
                                        </td>
                                    </tr>
                                </cong:table>
                                <cong:table>
                                    <cong:tr>
                                        <cong:td>
                                            <cong:div style="height:100px; overflow-y:auto;" id="upcomingSailing">
                                                <jsp:include page="/jsps/LCL/lclVoyage.jsp"/>
                                            </cong:div>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border" id="portRemark">
                                <table width="100%">
                                    <tr>
                                        <td  width="50%">
                                            <table id="portSpecialRemarks" width="100%">
                                                <tr><td class="caption" align="center">Port Special Remarks</td></tr>
                                                <tr><td>
                                                        <cong:textarea cols="78" rows="8" id="specialRemarks" styleClass="text-readonlytext8" readOnly="true"
                                                                       name="specialRemarks"  value="${lclQuoteForm.specialRemarks}">
                                                        </cong:textarea>
                                                    </td></tr>
                                            </table>
                                        </td>
                                        <td  width="50%">
                                            <table id="portInternalRemarks" width="100%">
                                                <tr><td class="caption" align="center">Port Internal Remarks</td></tr>
                                                <tr><td>
                                                        <cong:textarea cols="78" rows="8" id="internalRemarks" styleClass="text-readonlytext8"
                                                                       readOnly="true" name="internalRemarks" value="${lclQuoteForm.internalRemarks}">
                                                        </cong:textarea>
                                                    </td></tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border" id="regionalRemark">
                                <table width='50%' id="griRemarks">
                                    <tr><td class="caption" align="center">GRI and Regional Remarks</td></tr>
                                    <tr>
                                        <td>
                                            <cong:textarea cols="90" rows="8" id="portGriRemarks" styleClass="text-readonlytext8" readOnly="true"
                                                           name="portGriRemarks" value="${lclQuoteForm.portGriRemarks}">
                                            </cong:textarea>
                                        </td>
                                    </tr>
                                </table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="quote/importQuoteGeneralInformation.jsp" %>
                            </cong:td>

                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="black-border">
                                <%@include file="/jsps/LCL/lclQuoteImport.jsp" %>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </div>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <div class="tab-block">
                                    <div class="tab-box">
                                        <a href="javascript:;" class="tabLink activeLink" id="cont-2"><span>Cost and Charges</span></a>
                                    </div>
                                    <div class="tabcontent" id="cont-2-1">
                                        <cong:div id="chargeDesc">
                                            <c:import url="/jsps/LCL/ajaxload/quoteChargeDesc.jsp">
                                                <c:param name="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclQuote.lclFileNumber.fileNumber}"/>
                                            </c:import>
                                        </cong:div>
                                    </div>
                                </div>
                            </cong:td>
                        </cong:tr><!-- tab row ends here -->
                    </cong:table>
                </div>
                <cong:table>
                    <cong:tr styleClass="textBoldforlcl">
                        <td>File No :
                            <c:if test="${not empty lclQuote.lclFileNumber.fileNumber}">
                                <span class="fileNo">IMP-${lclQuote.lclFileNumber.fileNumber}</span>
                            </c:if>
                        </td>
                        <cong:td style="float: right" styleClass="textlabelsBoldforlcl" valign="middle" id="quoteComplte">Quote Complete
                            <c:choose>
                                <c:when test="${lclQuote.quoteComplete==true}">
                                    <input type="radio" name="quoteCompleted" id="quoteCompleteYY" value="Y" checked="yes" onclick="checkQuoteCompletion()"/>yes
                                    <input type="radio" name="quoteCompleted" id="quoteCompleteNN" value="N" />No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="quoteCompleted" id="quoteCompleteYY" value="Y" onclick="checkQuoteCompletion()"/>yes
                                    <input type="radio" name="quoteCompleted" id="quoteCompleteNN" value="N" checked="yes" />No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                </cong:table>
                <%@include file="quote/buttonBottom.jsp" %>
                <input type="hidden" name="clientToolTip" id="clientToolTip"/>
                <input type="hidden" name="methodName" id="methodName"/>
            </cong:form>
        </div>
    </body>

</html>

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
    <cong:javascript src="${path}/jsps/LCL/js/exportQuote.js"/>
    <%@include file="/taglib.jsp" %>
    <body style="overflow:auto">
        <div id="pane">
            <c:if test="${lockMessage!='' && lockMessage!=null }">
                <center><font color="#FF4A4A" size="2"><b style="margin-left:150px;color: #000080;font-size: 15px;" >${lockMessage}</b></font></center>
                    </c:if>
                    <cong:form name="lclQuoteForm" id="lclQuoteForm" action="lclQuote.do">
                        <cong:hidden name="quoteType" id="quoteType" value="E"/>
                <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="<bean:message key="application.spotRate.commodityCode"/>"/>
                <input type="hidden" name="quoteOriginDestinationPrefix" id="quoteOriginDestinationPrefix"
                       value="${fn:substring(lclQuote.portOfOrigin.unLocationCode,2,5)}-${fn:substring(lclQuote.finalDestination.unLocationCode,2,5)}"/>
                <input type="hidden" name="moduleName" id="moduleName" value="${lclQuoteForm.moduleName}"/>
                <cong:hidden name="index" id="index" />
                <cong:hidden name="pooTrmNum" id="pooTrmNum" value="${lclQuoteForm.pooTrmNum}"/>
                <cong:hidden name="polTrmNum" id="polTrmNum" value="${lclQuoteForm.polTrmNum}"/>
                <cong:hidden name="podEciPortCode" id="podEciPortCode" value="${lclQuoteForm.podEciPortCode}"/>
                <cong:hidden name="fdEciPortCode" id="fdEciPortCode"  value="${lclQuoteForm.fdEciPortCode}"/>
                <cong:hidden name="fdEngmet" id="fdEngmet"  value="${lclQuoteForm.fdEngmet}"/>
                <cong:hidden name="eculineCommodity" id="eculineCommodity"/>
                <cong:hidden name="homeScreenQtFileFlag" id="homeScreenQtFileFlag" value="${lclQuoteForm.homeScreenQtFileFlag}"/>
                <cong:hidden name="previousSailing" id="previousSailing"/>
                <cong:hidden name="ups" id="ups"/>
                <input type="hidden" name="isMeasureImpChanged" id="isMeasureImpChanged" value="false"/>
                <cong:hidden name="smallParcelRemarks" id="smallParcelRemarks"  />
                <cong:hidden name="saveRemarks" id="saveRemarks" value="false"  />
                <input type="hidden" id="insertInbondFlag" />
                <input type="hidden" name="fileState" id="fileState" value="${lclQuote.lclFileNumber.state}">
                <input type="hidden" name="loginUser" id="loginUser" value="${loginuser.loginName}"/>
                <input type="hidden" name="loginUserId" id="loginUserId" value="${loginuser.userId}"/>
                <%@include file="quote/status.jsp" %>
                <%@include file="quote/button.jsp" %>   
                
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <cong:table>
                                    <cong:tr>
                                        <cong:td styleClass="black-border">
                                            <%@include file="quote/client.jsp" %>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- First Row ends here -->
                        <!--for Imports only-->
                        <cong:tr>
                            <cong:td>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="quote/tradeRoute.jsp" %>
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
                            <cong:td  width="100%" id="upcomingSection" valign="top" styleClass="black-border">
                                <cong:table border="0">
                                    <tr class="caption" style="cursor: pointer">
                                        <cong:td onclick="toggleVoyageInformation()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">&nbsp;&nbsp;Upcoming Sailings&nbsp;&nbsp;</cong:td>
                                    <cong:td valign="middle"><input type="checkbox" name="showOlder" id="showOlder" title="Show Older" style="vertical-align: middle;" onclick="setRelayDetailsPrevious();"/>Show Older</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >&nbsp;&nbsp;Origin:</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;border: 0px solid #DDDDDD;width:190px" id="pooSailing" /></cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" valign="middle">&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >POL:</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;border: 0px solid #DDDDDD;width:190px" id="polSailing" /></cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" valign="middle">&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">POD:</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;border:0px;color: green;font-size: 12px; text-transform: uppercase;border: 0px solid #DDDDDD;width:290px"  id="podSailing" /></cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" valign="middle">&nbsp;&nbsp;&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">Destination:</cong:td>
                                    <cong:td onclick="toggleVoyageInformation()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;border:0px;color: green;font-size: 12px; text-transform: uppercase;border: 0px solid #DDDDDD;width:290px" id="fdSailing"/></cong:td>
                                        <td onclick="toggleVoyageInformation()" align="right" width="10%">
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
                                        <cong:td styleClass="text-readonly">
                                            <cong:div style="height:100px; overflow-y:auto;" id="upcomingSailing">
                                                <jsp:include page="/jsps/LCL/lclVoyage.jsp"/>
                                            </cong:div>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- fourth Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>

                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <cong:table>
                                    <cong:td></cong:td>
                                    <cong:tr>
                                        <cong:td  width="50%" valign="top">
                                            <table id="portSpecialRemarks" width="100%">
                                                <tr><td class="caption" align="center">Port Special Remarks</td></tr>
                                                <tr><td>
                                                        <div  class="text-readonlytext8" style="width:auto; white-space: normal">
                                                            <span class="splRemarks">
                                                                <c:if test="${lclQuoteForm.specialRemarksPod ne null && lclQuoteForm.specialRemarksPod ne ''}">
                                                                    <span style="color:#800080;" >POD :</span>
                                                                    <span id="specialFd" style="color:red">${lclQuoteForm.specialRemarksPod}</span><br/>
                                                                </c:if>
                                                                <c:if test="${lclQuoteForm.specialRemarks ne null && lclQuoteForm.specialRemarks ne ''}">
                                                                    <span style="color:#800080">FD :</span>
                                                                    <span  id="specialPod" style="color:red">${lclQuoteForm.specialRemarks}</span>
                                                                </c:if>
                                                            </span>
                                                            <%-- Access only load from Script --%>
                                                            <span  class="splRemarks" id="specialPod" style="color:red"></span>
                                                            <span  class="splRemarks" id="specialFd" style="color:red"></span>
                                                        </div>
                                                        <div style="display:none;">
                                                            <cong:textarea id="specialRemarks" name="specialRemarks" value="${lclQuoteForm.specialRemarks}"/>
                                                            <cong:textarea id="specialRemarksPod" name="specialRemarksPod" value="${lclQuoteForm.specialRemarksPod}"/>
                                                        </div>
                                                    </td></tr>
                                            </table>
                                        </cong:td>
                                        <cong:td width="50%">

                                            <table id="portInternalRemarks" width="100%">
                                                <tr><td class="caption" align="center">Port Internal Remarks</td></tr>
                                                <tr><td>
                                                        <div  class="text-readonlytext8"  style="width:auto; white-space: normal">
                                                            <span class="internRemarks">
                                                                <c:if test="${lclQuoteForm.internalRemarksPod ne null && lclQuoteForm.internalRemarksPod ne '' }">
                                                                    <span style="color:#800080" >POD :</span>
                                                                    <span id="internalFd" style="color:red">${lclQuoteForm.internalRemarksPod}</span><br/>
                                                                </c:if>
                                                                <c:if test="${lclQuoteForm.internalRemarks ne null && lclQuoteForm.internalRemarks ne '' }">
                                                                    <span style="color:#800080">FD :</span>
                                                                    <span style="color:red;" id="internalPod">${lclQuoteForm.internalRemarks}</span>
                                                                </c:if>
                                                            </span>
                                                            <%-- Access only load from Script --%>
                                                            <span  class="internRemarks" id="internalPod" style="color:red"></span>
                                                            <span  class="internRemarks" id="internalFd" style="color:red"></span>
                                                        </div>
                                                        <div style="display:none;">
                                                            <cong:textarea id="internalRemarks" name="internalRemarks" value="${lclQuoteForm.internalRemarks}"/>
                                                            <cong:textarea id="internalRemarksPod" name="internalRemarksPod" value="${lclQuoteForm.internalRemarksPod}"/>
                                                        </div>
                                                    </td></tr>
                                            </table>

                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>

                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <table id="griRemarks" width="50%">
                                    <tr><td class="caption" align="center">Port GRI Remarks</td></tr>
                                    <tr><td>
                                            <div  class="text-readonlytext8" id="griRemarksDiv" style="width:auto; white-space: normal">
                                                <span class="griRemarks">
                                                    <c:if test="${lclQuoteForm.portGriRemarksPod ne null && lclQuoteForm.portGriRemarksPod ne '' }">
                                                        <span style="color:#800080" >POD :</span>
                                                        <span style="color:red" id="griRemarksFd">${lclQuoteForm.portGriRemarksPod}</span><br/>
                                                    </c:if>
                                                    <c:if test="${lclQuoteForm.portGriRemarks ne null && lclQuoteForm.portGriRemarks ne '' }">
                                                        <span style="color:#800080">FD :</span>
                                                        <span style="color:red" id="griRemarksPod">${lclQuoteForm.portGriRemarks}</span>
                                                    </c:if>
                                                </span>
                                                <%-- Access only load from Script --%>
                                                <span class="griRemarks" style="color:red" id="griRemarksPod"></span>
                                                <span class="griRemarks" style="color:red" id="griRemarksFd"></span>
                                            </div>
                                            <div style="display:none;">
                                                <cong:textarea id="portGriRemarks" name="portGriRemarks" value="${lclQuoteForm.portGriRemarks}"/>
                                                <cong:textarea id="portGriRemarksPod" name="portGriRemarksPod" value="${lclQuoteForm.portGriRemarksPod}"/>
                                            </div>
                                        </td></tr>
                                </table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="quote/generalInformation.jsp" %>
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
                                        <a href="javascript:;" class="tabLink " id="cont-3"><span>Parties</span></a>
                                    </div>
                                    <div class="tabcontent" id="cont-2-1">
                                        <cong:div id="chargeDesc">
                                            <c:import url="/jsps/LCL/ajaxload/quoteChargeDesc.jsp">
                                                <c:param name="fileNumberId" value="${lclQuote.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclQuote.lclFileNumber.fileNumber}"/>
                                            </c:import>
                                        </cong:div>
                                    </div>
                                    <div class="tabcontent hide" id="cont-3-1">
                                        <%@include file="/jsps/LCL/tradingPartnerQuote.jsp" %>
                                    </div>
                                </div>
                            </cong:td>
                        </cong:tr><!-- tab row ends here -->
                    </cong:table>
                </div>
                <table border="0" width="100%">
                    <tr>
                        <td class="textBoldforlcl">File No :
                            <c:if test="${not empty lclQuote.lclFileNumber.fileNumber}">
                                <c:choose>
                                    <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                                        <span class="fileNo" id="fileNumberQuote">${originUnCode}-${lclQuote.lclFileNumber.fileNumber}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="fileNo">IMP-${lclQuote.lclFileNumber.fileNumber}</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:if></td>
                        <td></td> <td></td> <td></td>
                        <td style="float: right" class="textlabelsBoldforlcl" id="quoteComplte">Quote Complete
                            <input type="radio" name="quoteCompleted" id="quoteCompleteYY" value="Y" ${qtComplete ? "checked":""} onclick="checkQuoteCompletion()"/>Yes
                            <input type="radio" name="quoteCompleted" id="quoteCompleteNN"  value="N" ${!qtComplete ? "checked":""}/>No
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5">
                            <%@include file="quote/buttonBottom.jsp" %>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="clientToolTip" id="clientToolTip"/>
                <input type="hidden" name="methodName" id="methodName"/>
            </cong:form>
        </div>
    </body>
</html>
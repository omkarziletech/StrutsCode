<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<%@ page import="com.gp.cvst.logisoft.domain.BookingFcl"%>
<%@ page import="com.gp.cvst.logisoft.domain.BookingfclUnits"%>
<%@ page import="com.gp.cong.logisoft.domain.User"%>
<%@ page import="com.gp.cong.logisoft.domain.RoleDuty"%>
<%@ page import="com.gp.cong.logisoft.bc.fcl.BookingFclBC"%>
<%@ page import="com.gp.cvst.logisoft.beans.TransactionBean"%>
<%@ page import="com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%
    User userid = (User) session.getAttribute("loginuser");
    RoleDuty roleDuty = (RoleDuty) session.getAttribute("roleDuty");
    boolean hasUserLevelAccess = roleDuty.isShowDetailedCharges();
    TransactionBean transactionBean = new TransactionBean();
    NumberFormat numformat = new DecimalFormat("##,###,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String oceanFrightForCollapse = "";
    String ocenFrightRollUpAmount = "";
%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div id="commentsPopupDiv" style="display: none; height:135px;width:510px;left:30%;top:40%">
    <div align="right"></div>
    <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr class="tableHeadingNew" >
            <th align="left">Comments</th>
            <th align="right"><div id="commentsTitleMessage" ></div><input type="hidden"  id="hiddenIdValue" /><input type="hidden"  id="hiddenAdjustmentAmtValue" /></th>

        </tr>
        <tr>
            <td colspan="2" align="center">
                <textarea rows="5" cols="60" id="commentsAboutAdjustment" onkeyup="getUpperCaseValue(this)" onkeypress="return checkTextAreaLimit(this, 500)"></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="checkbox" name="applytoallcontainerchkbox" id="applytoallcontainerchkbox"/>
                <label for="applytoallcontainerchkbox" class="textlabelsBold">Apply to ALL Containers</label>
                <input type="button" value="submit"  class="buttonStyleNew" onclick="submitCommentsById(getElelmentValueById('hiddenIdValue'), getElelmentValueById('hiddenAdjustmentAmtValue'), '${bookingValues.bookingId}')" />
                <input name="cancelCommentsButton" type="button" value="cancel"  class="buttonStyleNew" onclick="closeCommentsPopupByElementId('commentsPopupDiv', getElelmentValueById('hiddenIdValue'))"/>

            </td>
        </tr>
    </table>
</div>
<%@include file="/jsp/common/popup.jsp" %>
<div id="bookingContainer" style="width:99%;padding-left:5px;padding-top:10px;">
    <ul class="newtab">
        <li><a href="#tradeRoute" onclick='setTabName("tradeRoute");'><span class="newtabRight">Trade Route<b style="color:red;">*</b></span></a></li>
        <li><a href="#shipperForwarder" onclick='setTabName("shipperForwarder");'><span class="newtabRight">Shipper Forwarder Consignee<b style="color:red;">*</b></span></a></li>
        <li><a href="#equipment" onclick='setTabName("equipment");'><span class="newtabRight">Equipment Pickup and Return</span></a></li>
        <li><a href="#specialProvisions" onclick='setTabName("specialProvisions");'><span class="newtabRight">Special Provisions and Goods Description</span></a></li>
        <li><a href="#rates" onclick='setTabName("rates");'><span class="newtabRight">Cost & Charges<b style="color:red;">*</b></span></a></li>
    </ul>
    <div id="tradeRoute"  class="whitebackgrnd">
        <table width="100%"  border="0" class="tableBorderNew">
            <tr class="tableHeadingNew"><td>Trade Route</td></tr>
            <tr><td>
                    <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0"
                           style="border-bottom-width: 1px;border-bottom-color:grey;border-top-width: 0px;
                           border-left-width: 0px;border-right-width: 0px;">
                        <tr>
                            <c:choose>
                                <c:when test="${importFlag}">
                                    <%@include file="../fragment/desOrgForimport.jsp" %>
                                </c:when>
                                <c:otherwise>
                                    <%@include file="../fragment/desOrgForExport.jsp" %>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <table width="100%" class="tableBorderNew" cellpadding="3"
                                       style="border-left-width:1px;border-left-color:grey;border-top-width:0px;
                                       border-bottom-width:0px;border-right-width: 0px;">
                                    <tr class="textlabelsBold">
                                        <td align="right">Default Agent</td>
                                        <td>
                                            <html:radio property="alternateAgent" onclick="fillDefaultAgent('${importFlag}');" styleId="alternateAgentY"
                                                        value="Y" name="transactionBean"/>Yes
                                            <html:radio property="alternateAgent" onclick="clearValues();" styleId="alternateAgentN"
                                                        value="N" name="transactionBean"/>No<br />
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right">Direct Consignment&nbsp;</td>
                                        <td>
                                            <html:radio property="directConsignmntCheck" styleId="directConsignmentY" onclick="directConsignmnt('${importFlag}');"
                                                        value="on" name="transactionBean" tabindex="-1"/>Yes
                                            <html:radio property="directConsignmntCheck" styleId="directConsignmentN" onclick="directConsignmntNo('${importFlag}');"
                                                        value="off"  name="transactionBean" tabindex="-1"/>No
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right">Agent Name</td>
                                        <td class="textlabelsBold" >
                                           <c:choose>
                                                <c:when test="${bookingValues.directConsignmntCheck == 'on' && bookingValues.alternateAgent == 'N'}">
                                                    <html:text property="agent" styleId="agent" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <html:text property="agent" styleId="agent" styleClass="textlabelsBoldForTextBox"
                                                               value="${bookingValues.agent}" size="20" maxlength="80" onkeyup="checkPrepaid(this)" onkeydown="setDojoAction()"  />
                                                </c:otherwise>
                                            </c:choose>
                                            <input class="textlabelsBoldForTextBox"  name="agent_check" id="agent_check" type="hidden" value="${bookingValues.agent}"/>
                                            <div id="agent_choices" style="display: none" class="autocomplete"></div>
                                            <script type="text/javascript">
                    initAutocompleteWithFormClear("agent", "agent_choices", "agentNo", "agent_check",
                            "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=2&importFlag=${importFlag}", "checkForDisableAgent('routedByAgent')", "");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right">Agent Number</td>
                                        <td class="textlabelsBold">
                                            <html:text property="agentNo" styleId="agentNo" styleClass="textlabelsBoldForTextBoxDisabledLook"
                                                       readonly="true" value="${bookingValues.agentNo}" size="20" maxlength="10"  tabindex="-1"/>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right">ERT</td>
                                        <td >
                                            <div
                                                <c:choose>
                                                    <c:when test="${importFlag eq true}">
                                                        class="textlabelsBold"
                                                    </c:when>
                                                    <c:otherwise>
                                                        class="mandatory"
                                                    </c:otherwise>
                                                </c:choose>
                                                style="float: left">
                                                <c:choose>
                                                    <c:when test="${bookingValues.directConsignmntCheck == 'on' && bookingValues.alternateAgent == 'N'}">
                                                        <html:select property="routedAgentCheck" name="transactionBean"  style="width:120px;" styleClass="dropdown_accountingDisabled"
                                                                     styleId="routedAgentCheck" value="" disabled="true" >
                                                            <html:option value="">Select</html:option>
                                                            <html:option value="yes">Yes</html:option>
                                                            <html:option value="no">No</html:option>
                                                        </html:select>
                                                    </c:when>
                                                    <c:when test="${importFlag eq true && empty bookingValues.bookingId}">
                                                      <html:select property="routedAgentCheck" name="transactionBean"  style="width:120px;" styleClass="dropdown_accounting"
                                                                     onchange="setDefaultRouteAgent('${importFlag}')" styleId="routedAgentCheck" value="no" disabled="false">
                                                            <html:option value="">Select</html:option>
                                                            <html:option value="yes">Yes</html:option>
                                                            <html:option value="no">No</html:option>
                                                        </html:select>
                                                    </c:when>
                                                    <c:otherwise>
                                                      <html:select property="routedAgentCheck" name="transactionBean"  style="width:120px;" styleClass="dropdown_accounting"
                                                                     onchange="setDefaultRouteAgent('${importFlag}')" styleId="routedAgentCheck"  disabled="false" >
                                                            <html:option value="">Select</html:option>
                                                            <html:option value="yes">Yes</html:option>
                                                            <html:option value="no">No</html:option>
                                                        </html:select>
                                                    </c:otherwise>
                                                </c:choose>

                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right" align="right">Routed By Agent</td>
                                        <td class="textlabelsBold" valign="top">
                                            <c:choose>
                                                <c:when test="${bookingValues.directConsignmntCheck == 'on' && bookingValues.alternateAgent == 'N'}">
                                                    <input type="text" name="routedByAgent" id="routedByAgent" class="textlabelsBoldForTextBoxDisabledLook"   />
                                                </c:when>
                                               <c:otherwise>
                                                    <input type="text" name="routedByAgent" id="routedByAgent" value="${bookingValues.routedByAgent}"
                                                           size="20" class="textlabelsBoldForTextBox"    maxlength="80"  />
                                                </c:otherwise>
                                            </c:choose>

                                            <input Class="textlabelsBoldForTextBox"  name="routedByAgent_check"
                                                   id="routedByAgent_check" type="hidden" value="${bookingValues.routedByAgent}"/>
                                            <div id="routedByAgent_choices" style="display: none" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initAutocomplete("routedByAgent", "routedByAgent_choices", "RoutedDup", "routedByAgent_check",
                                                        "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=1", "checkForDisableRouted()");
                                            </script>
                                            <input name="RoutedDup" id="RoutedDup" type="text" style="display: none;"/>
                                            <html:checkbox property="routedAgentCheck" name="transactionBean"
                                                           onclick="checkRoutedAgent()" styleClass="textlabelsBoldForTextBox"/>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                </table>
                            </td>

                        </tr>
                    </table>
                </td>
            </tr><!--1st row ends -->

            <tr><!-- 2nd row -->
                <td colspan="8">
                    <table width="100%" height="100%" class="tableBorderNew"
                           style="border-top-width:0px;border-left-width: 0px;
                           border-bottom-width:1px;border-bottom-color:grey;border-right-width: 0px;">

                        <tr class="textlabelsBold">
                            <td>Region Remarks :</td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>&nbsp;</td>
                            <td width="100%" valign="baseline">
                                <c:if test="${not empty regionRemarks}">
                                    <ul style='margin-left: 0px;'>
                                        <li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'> ${str:splitter(regionRemarks,120,'<br>')} </li>
                                    </ul>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr valign="top"><!-- 3rd row -->
                <td>
                    <table width="100%"  border="0">
                        <tr class="textlabelsBold">
                            <td valign="top">&nbsp;&nbsp;Port Remarks:</td>
                            <td width="100%">
                                <%
                                    if (request.getAttribute("bookingValues") != null) {
                                        BookingFcl bookingFcl = (BookingFcl) request.getAttribute("bookingValues");
                                        String remarks = bookingFcl.getDestRemarks() != null ? bookingFcl.getDestRemarks() : "";
                                        String[] remarksDup = remarks.split("\\n");
                                        out.println("<ul style='margin-left: 0px;'>");
                                        for (int i = 0; i < remarksDup.length; i++) {
                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[i] + "</li>");
                                        }
                                        out.println("</ul>");
                                    }

                                %>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top">&nbsp;</td>
                            <td>
                                <%
                                    if (request.getAttribute("bookingValues") != null) {
                                        BookingFcl bookingFcl = (BookingFcl) request.getAttribute("bookingValues");
                                        String rateRemarks = bookingFcl.getRatesRemarks() != null ? bookingFcl.getRatesRemarks() : "";
                                        String[] remarksDup = rateRemarks.split("\\n");
                                        out.println("<ul style='margin-left: 0px;'>");
                                        for (int i = 0; i < remarksDup.length; i++) {
                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[i] + "</li>");
                                        }
                                        out.println("</ul>");
                                    }

                                %>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top">&nbsp;</td>
                            <td>
                                <%
                                    if (request.getAttribute("bookingValues") != null) {
                                        BookingFcl bookingFcl = (BookingFcl) request.getAttribute("bookingValues");
                                        String griRemarks = new BookingFclBC().getGRIRemarks(bookingFcl);
                                        String[] remarksDup = griRemarks.split("\\n");
                                        out.println("<ul style='margin-left: 0px;'>");
                                        for (int i = 0; i < remarksDup.length; i++) {
                                            out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>" + remarksDup[i] + "</li>");
                                        }
                                        out.println("</ul>");
                                    }
                                %>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>&nbsp;</td>
                            <td width="100%" valign="baseline" style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>
                                ${remarks}
                            </td>
                        </tr>
                    </table>
                </td></tr>
        </table>
    </div>

    <div id="shipperForwarder" class="whitebackgrnd">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
            <tr>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" >
                        <tr class="tableHeadingNew"><td colspan="2">Shipper</td></tr>
                        <tr  class="textlabelsBold">
                            <td>Name</td>
                            <td>
                                <input type="text" Class="textlabelsBoldForTextBox" name="shipperName" id="shipperName"
                                       style="text-transform: uppercase" size="30" maxlength="50" value="${bookingValues.shipper}"
                                       onfocus="disableDojo(this)"  onkeyup="copyNotListedTp(this, 'shipperTpCopy')"/>
                                <input id="shipperName_check" type="hidden" value="${bookingValues.shipper}" />
                                <div id="shipper_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                                initAutocompleteWithFormClear("shipperName", "shipper_choices", "shipper", "shipperName_check",
                                                        "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=3&acctTyp=S", "getShipperInfo()", "onShipperBlur();");
                                </script>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                      onmouseout="tooltip.hide();">
                                    <input type="hidden" id="shipperTpCopy"/>
                                    <html:checkbox property="shipperTpCheck" styleId="shipperTpCheck" name="transactionBean"
                                                   onclick="disableAutoComplete(this)"/>
                                </span>
                                <img src="${path}/img/icons/display.gif" id="toggle" onclick="getShipper(this.value);"/>
                                <img src="${path}/img/icons/add2.gif"  onclick="openTradingPartner('shipperName');"/>
                                <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('S');"/>
                                <c:choose>
                                    <c:when test="${isShipperBlueNotes}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="shipperIcon" onclick="openCustomerNotesInfo(jQuery('#shipper').val(), jQuery('#shipperName').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="shipperIcon" onclick="openCustomerNotesInfo(jQuery('#shipper').val(), jQuery('#shipperName').val());"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td width="10%">Code</td>
                            <td>
                                <input name="shipper" Class="BackgrndColorForTextBox" id="shipper"
                                       value="${bookingValues.shipNo }" maxlength="15" size="30"
                                       style="text-transform: uppercase" readonly="true" tabindex="-1" />
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top">Address</td>
                            <td>
                                <html:textarea property="shipperAddress" styleClass="textlabelsBoldForTextBox" styleId="shipperAddress"
                                               cols="31" rows="7" style="text-transform: uppercase"
                                               value="${bookingValues.addressforShipper}" ></html:textarea>
                                <img src="${path}/img/icons/display.gif" id="shipperContactButton"  onclick="getShipperAddress(this.value)"/>
                            </td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td>City</td>
                            <td>
                                <html:text property="shipperCity" styleClass="textlabelsBoldForTextBox" size="13" maxlength="50"
                                           style="text-transform: uppercase"    value="${bookingValues.shipperCity}" styleId="shipperCity"  />State
                                <html:text size="6" styleClass="textlabelsBoldForTextBox" property="shipperState" maxlength="50"
                                           value="${bookingValues.shipperState}"  style="text-transform: uppercase" styleId="shipperState"  />
                            </td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td>Zip</td>
                            <td><html:text property="shipperZip" styleClass="textlabelsBoldForTextBox" size="30" styleId="shipperZip" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase" value="${bookingValues.shipperZip}" onkeypress="getzip(this)" maxlength="5"  /></td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td>Country</td>
                            <td><html:text property="shipperCountry" styleClass="textlabelsBoldForTextBox"  size="30" maxlength="50"
                                       style="text-transform: uppercase"  value="${bookingValues.shipperCountry}" styleId="shipperCountry" /></td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td>Phone</td>
                            <td>
                                <html:text property="shipPho" maxlength="30"  styleClass="textlabelsBoldForTextBox" size="30" onblur="checkForNumberAndDecimal(this)"
                                           style="text-transform: uppercase" value="${bookingValues.shipperPhone}" styleId="shipPho" onkeyup="getIt(this)" /></td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td> Fax</td>
                            <td><html:text property="shipperFax" maxlength="30" styleClass="textlabelsBoldForTextBox" size="30" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase" value="${bookingValues.shipperFax}" styleId="shipperFax"  onkeyup="getIt(this)"/></td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td> Email</td>
                            <td>
                                <html:text property="shipEmai" maxlength="100"  styleClass="textlabelsBoldForTextBox" styleId="shipperEmail"
                                           style="text-transform: uppercase"  size="30" value="${bookingValues.shipperEmail}"  />
                            </td>
                        </tr>
                        <tr  class="textlabelsBold">
                            <td>Client Ref</td>
                            <td><html:text property="shipperClientReference" styleId="shipperClientReference" styleClass="textlabelsBoldForTextBox" maxlength="20"
                                       style="text-transform: uppercase"  size="30" value="${bookingValues.shipperClientReference}"  /></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Client</td>
                            <td><html:checkbox property="shippercheck" styleId="shippercheck" name="transactionBean" onclick="CheckedShip()"></html:checkbox></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>POA</td>
                                <td id="shipperPoaid">
                                ${bookingValues.shipperPoa}
                            </td>
                            <html:hidden property="shipperPoa" styleId ="shipperPoa" value="${bookingValues.shipperPoa}"/>
                        </tr>
                    </table>
                    <br /></td>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1">
                        <tr class="tableHeadingNew"><td colspan="2">Forwarder</td></tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Name</td>
                            <td class="textlabelsBold">
                                <input type="text" style="text-transform: uppercase" Class="textlabelsBoldForTextBox mandatory" name="fowardername" id="fowardername"
                                       size="30" maxlength="50"  value="${bookingValues.forward}"  />
                                <input id="fowardername_check" type="hidden" value="${bookingValues.forward }" />
                                <div id="forward_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                                initAutocompleteWithFormClear("fowardername", "forward_choices", "forwarder", "fowardername_check",
                                                        "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=4&acctTyp=F", "getForwarderInfo()", "onForwarderBlur();");
                                </script>

                                <img src="${path}/img/icons/display.gif" id="toggle"  onclick="getForwarder()" />
                                <img src="${path}/img/icons/add2.gif"  onclick="openTradingPartner('fowardername')"/>
                                <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('F')"/>
                                <c:choose>
                                    <c:when test="${isForwarderBlueNotes}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="forwarderIcon" onclick="openCustomerNotesInfo(jQuery('#forwarder').val(), jQuery('#forwardername').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="forwarderIcon" onclick="openCustomerNotesInfo(jQuery('#forwarder').val(), jQuery('#forwardername').val());"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" width="10%" align="right">Code</td>
                            <td class="textlabelsBold">
                                <input  name="forwarder" Class="BackgrndColorForTextBox" id="forwarder"
                                        value="${bookingValues.forwNo }" maxlength="15" size="30" readonly ="true" tabindex ="-1"
                                        style="text-transform: uppercase"  />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right" valign="top"> Address</td>
                            <td>
                                <html:textarea property="forwarderAddress" styleClass="textlabelsBoldForTextBox"
                                               cols="31" rows="7" style="text-transform: uppercase"
                                               value="${bookingValues.addressforForwarder}" styleId="forwarderAddress"></html:textarea>
                                <img src="${path}/img/icons/display.gif" id="forwarderContactButton" onclick="getForwarderAddress(this.value)" />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> City</td>
                            <td class="textlabelsBold">
                                <html:text property="forwarderCity" styleClass="textlabelsBoldForTextBox" size="13" maxlength="50"
                                           style="text-transform: uppercase"  value="${bookingValues.forwarderCity}"  styleId="forwarderCity"/>State
                                <html:text property="forwarderState" styleClass="textlabelsBoldForTextBox" size="6" maxlength="50"
                                           style="text-transform: uppercase"  value="${bookingValues.forwarderState}"  styleId="forwarderState"/></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Zip<br /></td>
                            <td><html:text property="forwarderZip" styleClass="textlabelsBoldForTextBox" size="30" styleId="forwarderZip" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase" value="${bookingValues.forwarderZip}" onkeypress="getzip(this)" maxlength="5"  /><br /></td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold" align="right"> Country</td>
                            <td>
                                <html:text property="forwarderCountry" styleClass="textlabelsBoldForTextBox" size="30" maxlength="50"
                                           style="text-transform: uppercase" value="${bookingValues.forwarderCountry}"  styleId="forwarderCountry"/>
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Phone</td>
                            <td  class="textlabelsBold">
                                <html:text property="forwarderPhone" maxlength="30" styleClass="textlabelsBoldForTextBox" size="30" onblur="checkForNumberAndDecimal(this)"
                                           style="text-transform: uppercase" value="${bookingValues.forwarderPhone}"  styleId="forwarderPhone" onkeyup="getIt(this)"/>
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Fax</td>
                            <td><html:text property="forwarderFax" maxlength="30" style="text-transform: uppercase"
                                       styleClass="textlabelsBoldForTextBox" value="${bookingValues.forwarderFax}" size="30"  onblur="checkForNumberAndDecimal(this)" styleId="forwarderFax" onkeyup="getIt(this)"/></td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold" align="right"> Email</td>
                            <td  class="textlabelsBold">
                                <html:text property="forwarderEmail" maxlength="100"
                                           styleClass="textlabelsBoldForTextBox" styleId="forwarderEmail"
                                           style="text-transform: uppercase"
                                           size="30" value="${bookingValues.forwarderEmail}"  /></td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold">Client Ref</td>
                            <td><html:text property="forwarderClientReference" styleClass="textlabelsBoldForTextBox" maxlength="20"
                                       style="text-transform: uppercase"  size="30" value="${bookingValues.forwarderClientReference}" styleId="forwarderClientReference" /> </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Client</td>
                            <td><html:checkbox  property="forwardercheck" styleId="forwardercheck" name="transactionBean" onclick="CheckedForwarder()"></html:checkbox></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">POA</td>
                                <td id="forwarderPoaid">
                                ${bookingValues.forwarderPoa}
                            </td>
                            <html:hidden property="forwarderPoa" styleId="forwarderPoa" value="${bookingValues.forwarderPoa}"/>
                        </tr>
                    </table>
                    <br /></td>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1">
                        <tr class="tableHeadingNew"><td colspan="2">Consignee</td></tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Name</td>
                            <td class="textlabelsBold">
                                <input type="text" Class="textlabelsBoldForTextBox" name="consigneename" id="consigneename"
                                       style="text-transform: uppercase"  size="30" maxlength="50"  value="${bookingValues.consignee }"  onfocus="disableDojo(this)"  onkeyup="copyNotListedTp(this, 'consigneeTpCopy')"/>
                                <input id="consigneename_check" type="hidden" value="${bookingValues.consignee}"/>
                                <div id="consigneename_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                                initAutocompleteWithFormClear("consigneename", "consigneename_choices", "consignee", "consigneename_check",
                                                        "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=5&acctTyp=C", "getConsigneeInfo()", "onConsigneeBlur();");
                                </script>
                                <input type="hidden" id="consigneeTpCopy"/>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                      onmouseout="tooltip.hide();">
                                    <html:checkbox property="consigneeTpCheck" styleId="consigneeTpCheck" name="transactionBean"
                                                   onclick="disableAutoComplete(this)"/>
                                </span>
                                <img src="${path}/img/icons/display.gif" id="toggle"  onclick="getConsignee(this.value)" />
                                <img src="${path}/img/icons/add2.gif"  onclick="openTradingPartner('consigneename')"/>
                                <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('C')"/>
                                <c:choose>
                                    <c:when test="${isConsigneeBlueNotes}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="consigneeIcon" onclick="openCustomerNotesInfo(jQuery('#consignee').val(), jQuery('#consigneename').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="consigneeIcon" onclick="openCustomerNotesInfo(jQuery('#consignee').val(), jQuery('#consigneename').val());"/>
                                    </c:otherwise>
                                </c:choose>

                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold" width="10%" align="right">Code</td>
                            <td class="textlabelsBold">
                                <input  name="consignee" Class="BackgrndColorForTextBox" id="consignee"
                                        value="${bookingValues.consNo}" maxlength="15" size="30" readonly="true"
                                        style="text-transform: uppercase" tabindex="-1" />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" valign="top" align="right"> Address</td>
                            <td><html:textarea property="consigneeAddress" styleClass="textlabelsBoldForTextBox" cols="31" rows="7"
                                           style="text-transform: uppercase"   value="${bookingValues.addressforConsingee}" styleId="consigneeAddress" ></html:textarea>
                                <img src="${path}/img/icons/display.gif" id="congineeContactButton"  onclick="getConsigneeAddress(this.value)" />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> City</td>
                            <td class="textlabelsBold">
                                <html:text property="consigneeCity" styleClass="textlabelsBoldForTextBox"  size="13" maxlength="50"
                                           style="text-transform: uppercase"   value="${bookingValues.consigneeCity}" styleId="consigneeCity"  />State
                                <html:text property="consigneeState" styleClass="textlabelsBoldForTextBox" size="6" maxlength="50"
                                           style="text-transform: uppercase"   value="${bookingValues.consigneeState}" styleId="consigneeState"  /></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Zip</td>
                            <td><html:text property="consigneeZip" styleClass="textlabelsBoldForTextBox" maxlength="5" styleId="consigneeZip" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase" value="${bookingValues.consigneeZip}" size="30"  onkeyup="getzip(this)"  /></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Country</td>
                            <td><html:text property="consigneeCountry" styleClass="textlabelsBoldForTextBox" size="30" maxlength="50"
                                       style="text-transform: uppercase"  value="${bookingValues.consigneeCountry}" styleId="consigneeCountry"  /></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Phone</td>
                            <td class="textlabelsBold">
                                <html:text property="consigneePhone" maxlength="30" styleClass="textlabelsBoldForTextBox" onblur="checkForNumberAndDecimal(this)"
                                           style="text-transform: uppercase" value="${bookingValues.consingeePhone}" size="30" styleId="consigneePhone" onkeyup="getIt(this)" /></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Fax</td>
                            <td><html:text property="consigneeFax" maxlength="30" styleClass="textlabelsBoldForTextBox" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase" value="${bookingValues.consigneeFax}" size="30" styleId="consigneeFax"  onkeyup="getIt(this)"/></td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right"> Email</td>
                            <td class="textlabelsBold">
                                <html:text property="consigneeEmail" maxlength="100" styleClass="textlabelsBoldForTextBox"
                                           styleId="consigneeEmail"  style="text-transform: uppercase"
                                           size="30"   value="${bookingValues.consingeeEmail}"  /></td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold">Client Ref</td>
                            <td>
                                <html:text property="consigneeClientReference" styleId="consigneeClientReference" styleClass="textlabelsBoldForTextBox" maxlength="20"
                                           style="text-transform: uppercase"   size="30" value="${bookingValues.consigneeClientReference}"  /></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Client</td>
                            <td><html:checkbox property="consigneecheck" styleId="consigneecheck" name="transactionBean" onclick="CheckedConsignee()"></html:checkbox></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">POA</td>
                                <td id="ConsigneePoaid">
                                ${bookingValues.consigneePoa}
                            </td>
                            <html:hidden property="consigneePoa" styleId="consigneePoa" value="${bookingValues.consigneePoa}"/>

                        </tr>
                    </table>
                    <br /></td>

            </tr>
        </table>
    </div>

    <div id="equipment" class="whitebackgrnd">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
            <tr>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" >
                        <tr class="tableHeadingNew"><td colspan="2">Equipment Pickup</td></tr>
                        <tr>
                            <td class="textlabelsBold" style="padding-left:10px;" >Earliest Date</td>
                            <td><fmt:formatDate pattern="MM/dd/yyyy" var="earliestPickUpDate" value="${bookingValues.earliestPickUpDate}"/>
                                <html:text property="earlierPickUpDate"  styleClass="textlabelsBoldForTextBox" size="23"
                                           value="${earliestPickUpDate}" styleId="txtcal99" onchange="compareWithTodaysDate('${importFlag}',this)"  />
                                <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal10" width="16" height="16"
                                     id="cal99" onmousedown="insertDateFromCalendar(this.id, 0)" />
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" class="textlabelsBold" style="padding-left:10px;">Location/Name</td>
                            <td>
                                <input name="exportPositioning" Class="textlabelsBoldForTextBox" id="exportPositioning"
                                       size="23" maxlength="50"  style="text-transform: uppercase"
                                       value="${bookingValues.exportPositoningPickup}"/>
                                <input type="hidden" Class="textlabelsBoldForTextBox" name="wareHouse_check"
                                       id="wareHouse_check"    value="${bookingValues.exportPositoningPickup}"/>
                                <%--<input type="hidden" Class="textlabelsBoldForTextBox" name="wareHouseTemp"
                                        id="wareHouseTemp"    />--%>
                                <div id="wareHouse_choices" style="display: none" class="autocomplete"></div>
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <script type="text/javascript">
                                                initAutocompleteWithFormClear("exportPositioning", "wareHouse_choices", "wareHouseId", "wareHouse_check",
                                                        "${path}/actions/getWareHouseDetails.jsp?tabName=BOOKING&from=FCLI", "getWareHouseAdd()", "clearWareHouseNameAndAddress()");
                                        </script>
                                    </c:when>
                                    <c:otherwise>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("exportPositioning", "wareHouse_choices", "wareHouseId", "wareHouse_check",
                                                    "${path}/actions/getWareHouseDetails.jsp?tabName=BOOKING&from=FCLE", "getWareHouseAdd()", "clearWareHouseNameAndAddress()");
                                        </script>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" Class="textlabelsBoldForTextBox" name="wareHouseId" id="wareHouseId"/>
                                <input type="text" Class="BackgrndColorForTextBox" name="wareHouseTemp"
                                       id="wareHouseTemp" value="${bookingValues.wareHouseCode}" size="4" readonly="true"  tabindex="-1"/>
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <img src="${path}/img/icons/add2.gif" id ="addWareHouse" onclick="return GB_show('Add WareHouse', '${path}/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=FCLI&field=pickUp', 325, 730);"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/add2.gif" id ="addWareHouse" onclick="return GB_show('Add WareHouse', '${path}/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=FCLE&field=pickUp', 325, 730);"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" class="textlabelsBold" style="padding-left:10px;">Name/Address</td>
                            <td>
                                <html:textarea property="emptypickupaddress" styleClass="textlabelsBoldForTextBox"
                                               styleId="emptypickupaddress" cols="28" rows="3"
                                               value="${bookingValues.emptypickupaddress}"
                                               style="text-transform: uppercase" onchange="limitTextchar(this,200)"
                                               onkeypress="limitTextchar(this,200)" >
                                </html:textarea>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top">&nbsp;&nbsp;Remarks</td>
                            <td>
                                <html:textarea property="pickUpRemarks" styleClass="textlabelsBoldForTextBox"
                                               styleId="pickUpRemarks" cols="28" rows="3" style="text-transform: uppercase"
                                               onchange="return limitTextchar(this,100)"
                                               value="${bookingValues.pickUpRemarks}"
                                               onkeypress=" return limitTextchar(this,100)" ></html:textarea></td>
                            </tr>
                        </table>
                    </td>
                    <td width="25%" valign="top">
                        <table width="100%" border="0" cellpadding="1" cellspacing="1">
                            <tr class="tableHeadingNew"><td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;Spotting Address</td></tr>
                            <tr class="textlabelsBold">
                                <td align="right">Date</td>
                                <td>
                                <c:choose>
                                    <c:when test="${bookingValues.timeCheckBox=='on'}">
                                        <fmt:formatDate  pattern="MM/dd/yyyy" var="positioningDate" value="${bookingValues.positioningDate}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatDate  pattern="MM/dd/yyyy HH:mm a" var="positioningDate" value="${bookingValues.positioningDate}"/>
                                    </c:otherwise>
                                </c:choose>
                                <html:text  property="postioningDate"  styleClass="textlabelsBoldForTextBox" size="45" style="text-transform: uppercase"
                                            value="${positioningDate}" styleId="txtcal53" onchange="compareWithTodaysDate('${importFlag}',this)"  />
                                <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16"
                                     id="cal53" onmousedown="displayDateOnly(this);" />
                                <span class="hotspot" onmouseover="tooltip.show('<strong>Time Unknown</strong>', null, event);"
                                      onmouseout="tooltip.hide();" style="color:black;">
                                    <html:checkbox property="timeCheckBox" styleId="timeCheckBox" name="bookingValues"
                                                   onclick="checkDateTime()"/>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold" align="right">Contact</td>
                            <td>
                                <html:text property="loadcontact" styleClass="textlabelsBoldForTextBox" size="49" styleId="loadcontact"
                                           style="text-transform: uppercase"  value="${bookingValues.loadcontact}" maxlength="50" ></html:text></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" align="right">Phone</td>
                                <td>
                                <html:text property="loadphone" styleClass="textlabelsBoldForTextBox" size="49" styleId="loadphone" onblur="checkForNumberAndDecimal(this)"
                                           style="text-transform: uppercase" value="${bookingValues.loadphone}" maxlength="30" ></html:text></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Same as Shipper</strong>', null, event);"
                                          onmouseout="tooltip.hide();" style="color:black;">
                                        <input type="checkbox" name="spottingCheckBox" id="spottingCheckBox" onclick="insertShipperDetails()">
                                    </span></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" align="right">AccountName</td>
                                <td>
                                    <input type="text" Class="textlabelsBoldForTextBox" name="spottingAccountName" id="spottingAccountName"
                                           style="text-transform: uppercase"  size="49" maxlength="50"  value="${bookingValues.spottingAccountName}"
                                    onfocus="disableDojo(this)"  onkeyup="copyNotListedTp(this, 'spottAddrTpCopy')"/>
                                <input Class="textlabelsBoldForTextBox"  name="spottingname_check" id="spottingname_check"
                                       type="hidden" value="${bookingValues.spottingAccountName}" />
                                <div id="spottingname_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                            initAutocompleteWithFormClear("spottingAccountName", "spottingname_choices", "spottingAccountNo", "spottingname_check",
                                                    "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=6", "getSpottingInfo()", "onSpottAddrrBlur()");
                                </script>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                      onmouseout="tooltip.hide();">
                                    <input type="hidden" id="spottAddrTpCopy"/>
                                    <html:checkbox property="spottAddrTpCheck" styleId="spottAddrTpCheck" name="transactionBean"
                                                   onclick="disableAutoComplete(this)"/>
                                </span>
                                <img src="${path}/img/icons/add2.gif" id ="addAccoName" onclick="openTradingPartner('spottingAccountName')"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold" align="right">AccountNo</td>
                            <td>
                                <html:text property="spottingAccountNo" styleClass="BackgrndColorForTextBox" size="49" styleId="spottingAccountNo" readonly="true" tabindex="-1"
                                           value="${bookingValues.spottingAccountNo}"  style="text-transform: uppercase" maxlength="10" ></html:text></td>
                            </tr>
                            <tr>
                            </tr>
                            <tr>
                                <td valign="top" align="right"><span class="textlabelsBold">Address</span></td>
                                <td>
                                <html:textarea property="addressofExpPosition" styleClass="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase"
                                               styleId="addressofExpPosition" onkeyup ="limitTextarea(this,6,30)" onkeypress="return checkTextAreaLimit(this, 200)"
                                               cols="50" rows="3" value="${bookingValues.addressForExpPositioning}" />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right">City</td>
                            <td class="textlabelsBold">
                                <html:text property="spotAddrCity" styleId="spotAddrCity"
                                           styleClass="textlabelsBoldForTextBox"  size="33"  style="text-transform: uppercase"
                                           value="${bookingValues.spotAddrCity}" maxlength="50"  />State
                                <html:text property="spotAddrState" styleId="spotAddrState"
                                           styleClass="textlabelsBoldForTextBox" size="4"  style="text-transform: uppercase"
                                           value="${bookingValues.spotAddrState}" maxlength="50"  />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right">Zip</td>
                            <td><html:text property="spotAddrZip" styleId="spotAddrZip" styleClass="textlabelsBoldForTextBox" maxlength="10" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase"  value="${bookingValues.spotAddrZip}" size="49"  onkeypress="getzip(this)" />
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top"  align="right">Remarks</td>
                            <td >
                                <html:textarea property="loadRemarks" styleClass="textlabelsBoldForTextBox" styleId="loadRemarks"
                                               cols="50" rows="3" value="${bookingValues.loadRemarks}" style="text-transform: uppercase"
                                               onchange="return limitTextchar(this, 100)"
                                               onkeypress="return limitTextchar(this, 100);" >
                                </html:textarea>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1">
                        <tr class="tableHeadingNew"><td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;Equipment Return</td></tr>
                        <tr>
                            <td class="textlabelsBold">Location/Name</td>
                            <td>
                                <input name="equipmentReturnName" Class="textlabelsBoldForTextBox" id="equipmentReturnName"
                                       size="23" maxlength="50"  style="text-transform: uppercase"
                                       value="${bookingValues.equipmentReturnName}"/>
                                <input type="hidden" Class="textlabelsBoldForTextBox" name="equipmentReturn_check"
                                       id="equipmentReturn_check"    value="${bookingValues.equipmentReturnName}"/>
                                <div id="equipmentReturn_choices" style="display: none" class="autocomplete"></div>
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("equipmentReturnName", "equipmentReturn_choices", "equipmentReturnId", "equipmentReturn_check",
                                                    "${path}/actions/getWareHouseDetails.jsp?tabName=BOOKING&from=FCLI", "addEquipmentReturn()", "clearEquipmentReturnNameAndAddress()");
                                        </script>
                                    </c:when>
                                    <c:otherwise>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("equipmentReturnName", "equipmentReturn_choices", "equipmentReturnId", "equipmentReturn_check",
                                                    "${path}/actions/getWareHouseDetails.jsp?tabName=BOOKING&from=FCLE", "addEquipmentReturn()", "clearEquipmentReturnNameAndAddress()");
                                        </script>
                                    </c:otherwise>
                                </c:choose>

                                <input type="hidden" Class="textlabelsBoldForTextBox" name="equipmentReturnId" id="equipmentReturnId"/>
                                <input type="text" Class="BackgrndColorForTextBox" name="equipmentReturnTemp"
                                       id="equipmentReturnTemp" value="${bookingValues.equipmentReturnCode}" size="4" readonly="true"  tabindex="-1"/>
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <img src="${path}/img/icons/add2.gif" id ="addEquipmentReturn" onclick="return GB_show('Add WareHouse', '${path}/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=FCLI&screenName=Booking&field=return', 325, 730);"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/add2.gif" id ="addEquipmentReturn" onclick="return GB_show('Add WareHouse', '${path}/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=FCLE&screenName=Booking&field=return', 325, 730);"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" align="right"><span class="textlabelsBold">Name/Address<br>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Check if same as Equipment Pick-up</strong>', null, event);"
                                          onmouseout="tooltip.hide();" style="color:black;">
                                        <input type="checkbox" id="autoAddressFill" onclick="fillReturnAddress()"
                                               name="autoAddressFill" />
                                    </span>
                                    <td>
                                        <html:textarea property="addressofDelivery" styleClass="textlabelsBoldForTextBox"  styleId="addressofDelivery"
                                                       cols="28" rows="3" value="${bookingValues.addessForExpDelivery}"
                                                       style="text-transform: uppercase"
                                                       onkeypress="return checkTextAreaLimit(this, 200)"  /></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td valign="top" align="right">Remarks</td>
                            <td>
                                <html:textarea property="returnRemarks"
                                               styleClass="textlabelsBoldForTextBox" styleId="returnRemarks"
                                               cols="28" rows="3" value="${bookingValues.returnRemarks}"
                                               style="text-transform: uppercase" onchange="return limitTextchar(this,100)"
                                               onkeypress="return limitTextchar(this,100)" >
                                </html:textarea>
                            </td>
                        </tr>
                    </table>
                    <table width="100%" border="0" cellpadding="1" cellspacing="1">
                        <tr class="tableHeadingNew"><td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;Equipment Control Dates</td></tr>
                        <tr>
                            <td class="textlabelsBold" width="5%" align="right">Date out of Yard</td>
                            <td>
                                <fmt:formatDate pattern="MM/dd/yyyy" var="dateOutOfYard" value="${bookingValues.dateoutYard}"/>
                                <html:text property="dateOutYard"  styleClass="textlabelsBoldForTextBox" size="23"  styleId="txtcal13"
                                           value="${dateOutOfYard}" onblur="dateOutOfYardValidation(this);" onchange="dateOutOfYardIsLesser(this)" />
                                <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16"
                                     id="cal13" onmousedown="insertDateFromCalendar(this.id, 0);" />
                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBold">Date Back into Yard</td>
                            <td class="textlabelsBold">
                                <fmt:formatDate pattern="MM/dd/yyyy" var="dateIntoYard" value="${bookingValues.dateInYard}"/>
                                <html:text property="dateInYard"  styleClass="textlabelsBoldForTextBox" size="23"  styleId="txtcal14"
                                           value="${dateIntoYard}" onblur="dateInYardValidation(this)" onchange="dateInYardIsGreater(this)"  />
                                <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16"
                                     id="cal14" onmousedown="insertDateFromCalendar(this.id, 0);" />
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="25%" valign="top">
                    <table width="100%" border="0" cellpadding="1" cellspacing="1">
                        <tr class="tableHeadingNew"><td colspan="2">Trucker</td></tr>
                        <tr class="textlabelsBold">
                            <td align="right">
                                <span class="hotspot" onmouseover="tooltip.show('<strong>Same as SSLine</strong>', null, event);"
                                      onmouseout="tooltip.hide();" style="color:black;">
                                    <html:checkbox property="truckerCheckbox" name="transactionBean" styleId="truckerCheckbox" onclick ="getTruckerFromSSline()"/>
                                </span>
                                Name</td>
                            <td class="textlabelsBold">
                                <input type="text" Class="textlabelsBoldForTextBox" name="truckerName" id="truckerName"
                                       style="text-transform: uppercase"   size="23" maxlength="200"  value="${bookingValues.name}"
                                       onfocus="disableDojo(this)"  onkeyup="copyNotListedTp(this, 'truckerTpCopy')"/>
                                <input id="trucker_check" type="hidden"  value="${bookingValues.name }" />
                                <div id="truckerLeft_choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                            initAutocompleteWithFormClear("truckerName", "truckerLeft_choices", "truckerCode", "trucker_check",
                                                    "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=7", "getTruckerInfo()", "onTruckerBlur();");
                                </script>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);" onmouseout="tooltip.hide();">
                                    <input type="hidden" id="truckerTpCopy"/>
                                    <html:checkbox property="truckerTpCheck" styleId="truckerTpCheck" name="transactionBean"
                                                   onclick="disableAutoComplete(this)"/>
                                </span>
                                <img src="${path}/img/icons/display.gif" id="toggle02"  onclick="getTrukerName(this.value)" />
                                <img src="${path}/img/icons/add2.gif"   id="addName" onclick="openTradingPartner('truckerName')"/>
                                <c:if test="${not empty bookingValues.truckerCode}">
                                    <img src="${path}/img/icons/comparison.gif" alt="Add Contact" id="addContact" onclick="openContactInfo('trucker')"/>
                                </c:if>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Code</td>
                            <td class="textlabelsBold">
                                <input name="truckerCode" Class="BackgrndColorForTextBox" id="truckerCode"
                                       value="${bookingValues.truckerCode}" maxlength="10" size="23"
                                       style="text-transform: uppercase" readonly="true" tabindex="-1" />
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right" valign="top">Address</td>
                            <td>
                                <html:textarea property="addressoftrucker" styleClass="textlabelsBoldForTextBox" styleId ="addressoftrucker"
                                               cols="24" rows="3" style="text-transform: uppercase"
                                               onkeypress="return checkTextAreaLimit(this, 200)"
                                               value="${bookingValues.address}"  />
                                <img src="${path}/img/icons/display.gif" id="truckerContactButton"  onclick="getTruckerAddress(this.value)" />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right">City</td>
                            <td class="textlabelsBold">
                                <html:text property="truckerCity" styleId="truckerCity"
                                           styleClass="textlabelsBoldForTextBox"  size="8"  style="text-transform: uppercase"
                                           value="${bookingValues.truckerCity}" maxlength="50"  />State
                                <html:text property="truckerState" styleId="truckerState"
                                           styleClass="textlabelsBoldForTextBox" size="4"  style="text-transform: uppercase"
                                           value="${bookingValues.truckerState}" maxlength="50"  />
                            </td>
                        </tr>
                        <tr>
                            <td  class="textlabelsBold" align="right">Zip</td>
                            <td><html:text property="truckerZip" styleId="truckerZip" styleClass="textlabelsBoldForTextBox" maxlength="10" onblur="checkForNumberAndDecimal(this)"
                                       style="text-transform: uppercase"  value="${bookingValues.truckerZip}" size="23" onkeypress="getzip(this)"  />
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Phone</td>
                            <td>
                                <html:text property="truckerPhone" styleClass="textlabelsBoldForTextBox" size="23" maxlength="30" onblur="checkForNumberAndDecimal(this)" styleId="truckerPhone"
                                           style="text-transform: uppercase"  value="${bookingValues.truckerPhone}" ></html:text></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td align="right">Email<br /></td>
                                <td>
                                <html:text property="truckerEmail" styleClass="textlabelsBoldForTextBox"  size="23" styleId ="truckerEmail" onblur="emailValidate(this)"
                                           maxlength="50"  style="text-transform: uppercase"
                                           value="${bookingValues.truckerEmail}" ></html:text><br /></td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">Client Ref</td>
                                <td>
                                <html:text property="truckerClientReference" styleClass="textlabelsBoldForTextBox" styleId ="truckerClientReference"
                                           style="text-transform: uppercase"    maxlength="20" size="23" value="${bookingValues.truckerClientReference}"  /> </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>

    <div id="specialProvisions" class="whitebackgrnd">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew"><td colspan="3">Special Provisions</td></tr>
                        <tr>
                            <td height="100%">
                                <table width="100%" height="100%" class="tableBorderNew"
                                       style="border-right-width: 1px;border-right-color:grey;border-left-width: 0px;
                                       border-bottom-width:0px;border-top-width: 0px;">
                                    <tr class="textlabelsBold">
                                        <td>Special Equipment</td>
                                        <td>
                                            <html:radio property="specialequipment"  value="Y" styleId="y1"
                                                        name="transactionBean"  onclick="disableSpecialEqpmt()" />Yes
                                            <html:radio property="specialequipment" value="N" styleId="n1"
                                                        name="transactionBean" onclick="disableSpecialEqpmt()"/>No
                                        </td>
                                        <td>
                                            <html:select property="specialEqpmtSelectBox" styleClass="dropdown_accounting"
                                                         styleId="specialEqpmtSelectBox" value="" >
                                                <html:optionsCollection name="specialEquipmentList"/></html:select>
                                                &nbsp;&nbsp;
                                            <html:select  styleClass="dropdown_accounting"  property="specialEqpmtUnit" styleId="specialEqpmtUnit" value="">
                                                <html:option value="">Select Unit</html:option>
                                                <c:if test="${! empty specialEquipmentUnitList}">
                                                    <html:optionsCollection name="specialEquipmentUnitList"/>
                                                </c:if>
                                            </html:select>
                                            &nbsp;&nbsp
                                            <input type="button" value="Add to Booking" id="addEquipment" onclick="addOrUpdateSpecialEquipment('${bookingValues.bookingId}', 'add')" class="buttonStyleNew" style="width:80px"/>
                                            &nbsp;&nbsp;
                                            <input type="button" value="Update Standard" id="updateEquipment" onclick="addOrUpdateSpecialEquipment('${bookingValues.bookingId}', 'update')" class="buttonStyleNew" style="width:90px"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td height="100%">
                                <table width="100%" height="100%" class="tableBorderNew"
                                       style="border-right-width: 1px;border-right-color:grey;border-left-width: 0px;
                                       border-bottom-width: 0px;border-top-width: 0px;">
                                    <tr >
                                        <td class="textlabelsBold"><div id="inlandVal" ></div></td>
                                        <td class="textlabelsBold">
                                            <html:radio property="inland" value="Y" styleId="y7" onclick="getInland('${bookingValues.ratesNonRates}')" name="transactionBean"/>Yes
                                            <html:radio property="inland" value="N" styleId="n7" onclick="deleteInland()" name="transactionBean"/>No
                                        </td>
                                    </tr>
                                    <tr >
                                        <td class="textlabelsBold">Document Charge</td>
                                        <td class="textlabelsBold">
                                            <html:radio property="docCharge" value="Y" styleId="docChargeY" onclick="getDocCharge()" name="transactionBean"/>Yes
                                            <html:radio property="docCharge" value="N" styleId="docChargeN" onclick="deleteDocCharge()" name="transactionBean"/>No
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td width="100px;">
                                <table  height="100%" style="margin-left: 0px;">
                                    <tr><td height="100%">
                                            <table width="100%" height="100%" class="tableBorderNew"
                                                   style="border-right-width: 0px;border-bottom-color:grey;border-left-width: 0px;
                                                   border-bottom-width: 1px;border-top-width: 0px;">
                                                    <tr  class="textlabelsBold">
                                                        <c:if test="${importFlag eq false}">
                                                            <td>Auto Deduct FF Commission 
                                                                <html:radio property="deductFFcomm" value="Y" styleId="y5"  name="transactionBean" onclick="getFFCommission()" />Yes
                                                                <html:radio property="deductFFcomm" value="N" styleId="n5" name="transactionBean"  onclick="deleteFFCommission()"/>No
                                                            </td>
                                                        </c:if>
                                                 
                                                            <c:if test="${importFlag eq false}">
                                                            <td class="textlabelsBold"> Chassis 
                                                                <html:radio property="chassisCharge" value="Y" tabindex="-1" styleId="chassisChargeY" name="transactionBean" onclick="getChasisCharge('${bookingValues.bookingId}')"/>Yes
                                                                <html:radio property="chassisCharge" value="N" tabindex="-1" styleId="chassisChargeN" name="transactionBean" onclick="deleteChassis()"/>No
                                                            </td>
                                                            </c:if>
                                                       
                                                </tr>
                                            </table>
                                        </td></tr>
                                    <tr><td><table>
                                                <tr class="textlabelsBold">
                                                    <td>Insurance</td>
                                                    <td>
                                                        <html:radio property="insurance" value="Y" styleId="y8" onclick="getinsurance()" name="transactionBean">Yes</html:radio>
                                                        <html:radio property="insurance"  value="N" styleId="n8" onclick="getinsurance1()" name="transactionBean">No</html:radio>
                                                        </td>
                                                        <td valign="bottom" style="padding-left:10px;">Cost of Goods
                                                        <fmt:formatNumber pattern="##,###,##0" var="costofgoods" value="${bookingValues.costofgoods}"/>
                                                        <input name="costofgoods" Class="BackgrndColorForTextBox" id="costofgoods"
                                                               value="${costofgoods}" size="5" readonly="true" onkeydown="return false;" onkeypress="return false;" tabindex="-1"/>
                                                    </td>
                                                  <td valign="bottom" style="padding-left:20px;">Pier Pass:
                                                        <html:radio property="pierPass" tabindex="-1" styleId="pierPassY" value="Y" name="transactionBean"  disabled="true" onclick="calculatePierPassCharge()"/>Yes
                                                        <html:radio property="pierPass" tabindex="-1" styleId="pierPassN" value="N" name="transactionBean"  disabled="true" onclick="deletePierPassCharge()"/>No
                                                    </td> 
                                                    <td  valign="bottom" style="visibility:hidden">Insurance Amt
                                                        <fmt:formatNumber pattern="##,###,##0.00" var="insurancamt" value="${bookingValues.insurancamt}"/>
                                                        <html:text property="insurancamt" styleClass="textlabelsBoldForTextBox" value="${insurancamt}" size="5" readonly="true" tabindex="-1"/>
                                                    </td>
                                                </tr>
                                            </table></td></tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr><!-- 1st row ends -->
            <tr><!-- 2nd row -->
                <td><br>
                    <table border="0" width="100%"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">

                        <tr>
                            <td width="50%" valign="top">
                                <table class="tableBorderNew" height="100%"  border="0"
                                       style="border-left-width: 0px;border-right-width: 0px;border-bottom-width: 0px;"
                                       cellpadding="0" cellspacing="0" width="100%">
                                    <tr class="tableHeadingNew"><td>Goods Description</td></tr>
                                    <tr>
                                        <td>
                                            <html:textarea property="goodsDescription" styleClass="textlabelsBoldForTextBox"
                                                           value="${bookingValues.goodsDescription}" cols="90" rows="6"
                                                           style="text-transform: uppercase; width:100%"
                                                           onkeypress="return checkTextAreaLimit(this, 500)"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td width="50%" valign="top">
                                <table class="tableBorderNew" style="border-bottom-width: 0px;border-right-width: 0px;"
                                       border="0" width="100%" cellpadding="0" cellspacing="0">
                                    <tr class="tableHeadingNew">
                                        <td>&nbsp;&nbsp;Remarks
                                            <span style="padding-left:230px;">Predefined Remarks
                                                <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp('${importFlag}')"/>
                                            </span></td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:textarea styleId="commentRemark" property="remarks"
                                                           styleClass="textlabelsBoldForTextBox"
                                                           value="${bookingValues.remarks}" cols="90"  rows="6"
                                                           style="text-transform: uppercase; width:100%"
                                                           onkeypress="return checkTextAreaLimit(this, 500)"  />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                    </table>
                </td></tr>
        </table>
    </div>

    <div id="rates" class="whitebackgrnd">
        <table  class="tableBorderNew" border="0" bordercolor="green"  cellpadding="0" cellspacing="0"><%--rates--%>
            <tr><td>
                    <table  border="0"><%--sub1--%>

                        <%
                            transactionBean = new TransactionBean();
                            List fcllist = new ArrayList();
                            int l = 0;
                            String unittemp = "", className = "", amt = "", markUp1 = "", unit = "", chargecode = "", costType = "", num = "",
                                    currency = "", chargecodedesc = "", sellRate = "", accoutnNo = "", accountName = "", newRate = "",
                                    effectiveDate = "", newFlag = "", id = "", Charges = "", temp = "", comment = "", adjustment = "", tempComment = "", chargeBookingNumber = "";
                            String diffMarkUp = "", unitValue = "", previousUnit = "";
                            Double totalAmount = 0.00;
                            Double totalRatesAmount = 0.00;
                            String outOfGaugeComment = "";
                            String spclEquipUnit = "";
                            String prevSpclEquipUnit = "";
                            boolean hasEquipment = true;
                            String adjustmentChargeComments = "";
                            String manualCharges = "";
                            String costRate = "";

                        %>
                        <tr><%--11--%>
                            <td id="expandRates" style="display:none;">
                                <div id="divtablesty1" style="border:thin;" >
                                    <%fcllist = (List) request.getAttribute("fclRates");
                                        if (fcllist != null && fcllist.size() > 0) {%>
                                    <table  border="0" cellpadding="3" cellspacing="0" class="displaytagstyleNew" id="expandRatesTable" style="width:75%">
                                        <thead><tr>
                                                <%
                                                    if (userid != null && hasUserLevelAccess) {%>
                                                <td><img src="${path}/img/icons/up.gif" border="0" onclick="getExpandRates()" id="triangleicon"/></td>
                                                    <%}%>
                                                <td>Select</td>
                                                <td>Unit</td>
                                                <td>Number &nbsp; Out of Gauge</td>
                                                <td>ChargeCode</td>
                                                <td>Cost</td>
                                                <td>Markup</td>
                                                <td>Sell</td>
                                                <td>Adjustment</td>
                                                <td>Comments</td>
                                                <c:if test="${bookingValues.spotRate eq 'Y'}">
                                                <td>Spot Cost</td>
                                                 </c:if>
                                                <td>&nbsp;&nbsp;Bundle into OFR</td>
                                                <td style="padding-left:10px;">Vendor Name</td>
                                                <td>Vendor No</td>
                                                <td>Comment</td>
                                                <td style="padding-left:10px;">Actions</td>
                                                </tr>
                                        </thead>
                                        <%
                                            className = "odd";
                                            while (l < fcllist.size()) {

                                                BookingfclUnits book = new BookingfclUnits();
                                                book = (BookingfclUnits) fcllist.get(l);
                                                transactionBean.setExpandCheck(book.getApproveBl());
                                                transactionBean.setCheckSplEqpUnits(book.getApproveBl());
                                                transactionBean.setCheckStandardCharge(book.getApproveBl());
                                                if (book.getPrint() != null) {
                                                    transactionBean.setPrint(book.getPrint());
                                                } else {
                                                    transactionBean.setPrint("off");
                                                }
                                                request.setAttribute("transactionBean", transactionBean);
                                                id = String.valueOf(book.getId());
                                                request.setAttribute("bookId", id);
                                                chargeBookingNumber = book.getBookingNumber();
                                                if (book.getSpecialEquipmentUnit() == null) {
                                                    book.setSpecialEquipmentUnit("");
                                                }
                                                if (book.getSpecialEquipment() != null) {
                                                    spclEquipUnit = book.getSpecialEquipment();
                                                } else {
                                                    spclEquipUnit = "";
                                                }
                                                if (book.getStandardCharge() == null) {
                                                    book.setStandardCharge("");
                                                }
                                                if (book.getOutOfGaugeComment() != null) {
                                                    outOfGaugeComment = book.getOutOfGaugeComment().replace("\r\n", "<br>").replace("\n", "<br>");
                                                } else {
                                                    outOfGaugeComment = "";
                                                }
                                                if (book.getUnitType() != null) {
                                                    unitValue = book.getUnitType().getCodedesc().toString();
                                                    unit = unitValue + "-" + book.getSpecialEquipmentUnit() + "-" + book.getStandardCharge();
                                                }
                                                request.setAttribute("splEquipmentUnits", book.getSpecialEquipmentUnit());
                                                request.setAttribute("splEquipment", book.getSpecialEquipment());
                                                request.setAttribute("bookingFclUnits", book);
                                                request.setAttribute("UnitName", unit);
                                                request.setAttribute("unitValue", unitValue);
                                                chargecode = book.getChgCode();
                                                costType = book.getCostType();
                                                if (book.getAmount() != null && !book.getAmount().equals("")) {
                                                    amt = numformat.format(book.getAmount());
                                                }
                                                if (book.getSpotRateMarkUp() != null) {
                                                    markUp1 = numformat.format(book.getSpotRateMarkUp());
                                                }else if (book.getMarkUp() != null) {
                                                    markUp1 = numformat.format(book.getMarkUp());
                                                }

                                                if (book.getNewFlag() != null) {
                                                    newFlag = book.getNewFlag();
                                                } else {
                                                    newFlag = "";
                                                }
                                                currency = book.getCurrency();
                                                num = book.getNumbers();
                                                chargecodedesc = book.getChargeCodeDesc();
                                                Charges = chargecodedesc + "/" + chargecode;
                                                request.setAttribute("Chargecode", chargecode);
                                               if (book.getSellRate() != null) {
                                                   if(null!=book.getSpotRateAmt()){
                                                         sellRate = numformat.format(book.getSpotRateAmt() + book.getSpotRateMarkUp());
                                                   }else{
                                                        sellRate = numformat.format(book.getAmount() + book.getMarkUp());
                                                   }
                                                }

                                                if (book.getAccountNo() != null) {
                                                    accoutnNo = book.getAccountNo();
                                                } else {
                                                    accoutnNo = "";
                                                }
                                                if (book.getAccountName() != null) {
                                                    accountName = book.getAccountName();
                                                } else {
                                                    accountName = "";
                                                }
                                                if (book.getFutureRate() != null) {
                                                    newRate = numformat.format(book.getFutureRate());
                                                } else {
                                                    newRate = "";
                                                }
                                                if (book.getEfectiveDate() != null) {
                                                    effectiveDate = sdf.format(book.getEfectiveDate());
                                                } else {
                                                    effectiveDate = "";
                                                }
                                                if (book.getComment() != null) {
                                                    tempComment = book.getComment();
                                                    int ind = 0;
                                                    if (tempComment != null && !tempComment.equals("")) {
                                                        ind = tempComment.indexOf("|");
                                                        if (ind != -1) {
                                                            comment = tempComment.substring(0, ind);
                                                            tempComment = tempComment.substring(ind + 1);
                                                        } else {
                                                            comment = tempComment;
                                                            tempComment = "";
                                                        }
                                                    }
                                                } else {
                                                    comment = "";
                                                }
                                                if (book.getAdjustment() != null) {
                                                    adjustment = numformat.format(book.getAdjustment());
                                                } else {
                                                    adjustment = "0.00";
                                                }
                                                if (book.getAdjustmentChargeComments() != null) {
                                                    adjustmentChargeComments = book.getAdjustmentChargeComments();
                                                } else {
                                                    adjustmentChargeComments = "";
                                                }
                                                request.setAttribute("adjRemarks", adjustmentChargeComments);
                                                StringBuilder s = new StringBuilder();
                                                if (comment != null) {
                                                    int index = 0;
                                                    char[] commentArray = comment.toCharArray();
                                                    for (int i = 0; i < commentArray.length; i++) {
                                                        if (commentArray[i] == '\n') {
                                                            s.append(comment.substring(index, i).trim());
                                                            s.append(" ");
                                                            index = i + 1;
                                                        }
                                                    }
                                                    s.append(comment.substring(index, comment.length()).trim());

                                                }
                                                s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                                s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                                request.setAttribute("l", l);
                                        %>
                                        <tbody>
                                            <%
                                                if (!previousUnit.equals(unit) && !previousUnit.equals("")) {
                                            %>
                                            <%if (hasEquipment) {
                                                    hasEquipment = false;
                                                    if (null != prevSpclEquipUnit && !prevSpclEquipUnit.equals("")) {%>
                                            <tr>
                                                <td></td>
                                                <td class="whitebackgrnd" class="textlabelsBold">
                                                    <font style="color: #D80000;"><%=prevSpclEquipUnit%></font>
                                                </td>
                                            </tr>
                                            <%}
                                                }%>
                                            <tr><td></td>
                                                <td><b  class="blackBG">Total:for</b></td>
                                                <td><b  class="blackBG"> <%=unittemp%></b></td>
                                                <td><b  class="blackBG">--------</b></td>
                                                <td><b  class="blackBG">-----------</b></td>
                                                <td><b  class="blackBG">--------</b></td>

                                                <td><b  class="blackBG">------------</b></td>

                                                <td><b  class="blackBG">--------></b></td>
                                                <td><b  class="blackBG"><%=numformat.format(totalAmount)%></b></td>
                                            </tr>
                                            <%
                                                    totalAmount = 0.00;
                                                }
                                            %>
                                            <tr class="<%=className%>">

                                                <td class="whitebackgrnd">
                                                    <input type="hidden" class="expandRatesTableChargeCode" value="${bookingFclUnits.chargeCodeDesc}"/>
                                                </td>
                                                <%if (!previousUnit.equals(unit)) {%>
                                                <td>
                                                    <html:checkbox property="expandCheck" value="<%=id%>" onclick="getNumbersChanged();checkAndUnCheckContainer(this);" name="transactionBean"/>
                                                    <c:if test="${! empty splEquipmentUnits}">
                                                        <img src="${path}/img/icons/delete.gif" onclick="deleteSpecialEquipmentUnit('${splEquipmentUnits}', '${bookingFclUnits.standardCharge}')" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    </c:if>
                                                    <html:checkbox property="checkSplEqpUnits"  value="${bookingFclUnits.specialEquipmentUnit}" style="visibility:hidden" name="transactionBean"/>
                                                    <html:checkbox property="checkStandardCharge"  value="${bookingFclUnits.standardCharge}" style="visibility:hidden" name="transactionBean"/>
                                                </td>
                                                <td><font style="background-color:#CCEBFF;"><c:out value="${unitValue}"/></font></td>
                                                <td><input name="numbers" id="numbers<%=l%>"   Class="textlabelsBoldForTextBox" value="<%=num%>" size="3" 
                                                           maxlength="3" onchange="getNumbersChanged();
                                                validateQuantityForExpand(this.value, '<%=l%>');"/>
                                                    &nbsp; &nbsp;<c:choose>
                                                        <c:when test="${bookingFclUnits.outOfGauge == 'Y'}">
                                                            <input type="radio"  name="outOfGage1${bookingFclUnits.chargeCodeDesc}${UnitName}" value="Y" checked onclick="checkGaugeComment('E', '${UnitName}', this)"/>Y
                                                            <input type="radio"  name="outOfGage1${bookingFclUnits.chargeCodeDesc}${UnitName}" value="N" onclick="checkGaugeComment('E', '${UnitName}', this)"/>N
                                                            <img src="${path}/img/icons/edit.gif" onclick="openOutOfGuageComments('${bookingFclUnits.unitType}', '${bookingFclUnits.standardCharge}', '<%=outOfGaugeComment%>')"
                                                                 onmouseover="tooltip.show('<strong>Add/Edit Comment</strong>', null, event);" onmouseout="tooltip.hide();" id="E${UnitName}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="radio"  name="outOfGage1${bookingFclUnits.chargeCodeDesc}${UnitName}" value="Y" onclick="checkGaugeComment('E', '${UnitName}', this)"/>Y
                                                            <input type="radio"  name="outOfGage1${bookingFclUnits.chargeCodeDesc}${UnitName}" value="N" checked onclick="checkGaugeComment('E', '${UnitName}', this)"/>N
                                                            <img src="${path}/img/icons/edit.gif" onclick="openOutOfGuageComments('${bookingFclUnits.unitType}', '${bookingFclUnits.standardCharge}', '')"
                                                                 style="visibility: hidden" id="E${UnitName}"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <%hasEquipment = true;
                                                    prevSpclEquipUnit = spclEquipUnit;
                                                } else {%>
                                                <%if (hasEquipment) {%>
                                                <td colspan="2">
                                                    <c:if test="${! empty splEquipmentUnits}">
                                                        <font style="color: #D80000;">${splEquipment}</font>
                                                    </c:if>
                                                </td>
                                                <%hasEquipment = false;
                                                } else {%>
                                                <td></td>
                                                <td></td>
                                                <%}%>
                                                <td><input name="numbers" id="numbers<%=l%>" Class="textlabelsBoldForTextBox" value="<%=num%>" size="3"
                                                           maxlength="3" style="visibility:hidden" onchange="getNumbersChanged(this);
                                                validateQuantityForExpand(this.value, '<%=l%>');"/></td>
                                                    <%}
                                                        unittemp = unitValue;
                                                        previousUnit = unit;%>

                                                <%if (newFlag.equals("new") || newFlag.equals("FF") || newFlag.equals("IN") || newFlag.equals("D") || newFlag.equals("PP")) {%>
                                                <td><font class="starColor" >*</font>
                                                    <span style="font-style: italic"><c:out value="${Chargecode}"/></span></td>
                                                    <%} else {%>
                                                <td><c:out value="${Chargecode}"/></td>
                                                <%}%>
                                                <%if (newFlag.equals("IN")) {%>
                                                <td><input name="chargeAmount" id="chargeAmount" Class="BackgrndColorForTextBox" value="0.00" readonly="true" size="6" tabindex="-1"/> </td>
                                                    <%} else if (newFlag.equals("FF") || newFlag.equals("D")) {%>
                                                <td><input name="chargeAmount" id="chargeAmount" Class="BackgrndColorForTextBox" value="<%=markUp1%>" readonly="true" size="6" tabindex="-1"/> </td>
                                                    <%} else {%>
                                                <td><input name="chargeAmount" id="chargeAmount" Class="BackgrndColorForTextBox" value="<%=amt%>" readonly="true" size="6" tabindex="-1"/> </td>
                                                    <%}
                                                        if (newFlag.equals("new")) {

                                                            diffMarkUp = numformat.format(book.getMarkUp() - book.getAmount());

                                                    %>
                                                <td><html:text property="chargeMarkUp" styleClass="BackgrndColorForTextBox" value="<%=diffMarkUp%>" size="6" onkeypress="getNumbersChanged()" readonly="true" tabindex="-1"/></td>
                                                <td><input name="sellRate" id="sellRate" Class="BackgrndColorForTextBox" maxlength="15" size="6" value="<%=markUp1%>" readonly="true" tabindex="-1"/></td>
                                                    <%} else {
                                                        if (newFlag.equals("IN")) {%>
                                                <td><html:text property="chargeMarkUp" styleClass="BackgrndColorForTextBox" value="0.00" size="6" onkeypress="getNumbersChanged()" readonly="true" tabindex="-1"/></td>
                                                <%} else if (newFlag.equals("FF") || newFlag.equals("D")) {
                                                %>
                                                <td><html:text property="chargeMarkUp" styleClass="BackgrndColorForTextBox" value="<%=amt%>" size="6" onkeypress="getNumbersChanged()" readonly="true" tabindex="-1"/></td>
                                                <% } else {%>
                                                <td><html:text property="chargeMarkUp" styleClass="BackgrndColorForTextBox" value="<%=markUp1%>" size="6" onkeypress="getNumbersChanged()" readonly="true" tabindex="-1"/></td>
                                                <%}%>
                                                <td><input name="sellRate" id="sellRate" Class="BackgrndColorForTextBox" maxlength="15" size="6" value="<%=sellRate%>" readonly="true" tabindex="-1"/></td>
                                                    <%}%>

                                                <%if (newFlag.equals("new") || newFlag.equals("FF") || newFlag.equals("IN") || newFlag.equals("D") ||  newFlag.equals("PP")) {%>
                                                <td><html:text property="adjustment" styleClass="textlabelsBoldForTextBox" maxlength="8" value="0.00" size="6"
                                                           style="display:none;" /></td>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                <%} else {%>
                                                <td><html:text property="adjustment" styleClass="textlabelsBoldForTextBox" value="<%=adjustment%>" onchange="allowOnlyWholeNumbers(this);getAccountNameById(this.value,${bookId})"
                                                           maxlength="8" size="6" onkeypress="getNumbersChanged()" styleId="adjustment${bookId}"/></td>

                                                <td>
                                                    <c:if test="${adjRemarks ne null and adjRemarks ne ''}">
                                                    <div id="expand-adj">
                                                        <img class="adjustmentRemarks" alt="Adjustment Remarks" src="${path}/img/icons/view.gif"
                                                             onmouseover="showToolTip('${adjRemarks}', 100, event)"
                                                             onmouseout="tooltip.hideComments();"/>
                                                    </div>
                                                    </c:if>
                                                </td>
                                                <%}%>
                                                <c:if test="${bookingValues.spotRate eq 'Y'}">
                                                    <td width="80%">
                                                        <fmt:formatNumber pattern="##,###,##0.00" var="spotAmt" value="${bookingFclUnits.spotRateAmt}"/>
                                                        <html:text property="spotRateAmt" styleClass="BackgrndColorForTextBox"
                                                                   readonly="true" value="${spotAmt}" size="5" tabindex="-1" ></html:text>
                                                    </td>
                                                </c:if>
                                                <%if ("OCNFRT".equals(chargecodedesc) || "OFIMP".equals(chargecodedesc)) {%>
                                                <td align="center">
                                                    <html:checkbox property="print" onclick="saveExpandedBundleOFR()" name="transactionBean"
                                                                   style="visibility:hidden" /></td>
                                                    <%} else {%>
                                                <td align="center">
                                                    <%
                                                        if (chargecodedesc.equals("005")) {%>
                                                    <html:checkbox property="print" onclick="getCheckFFCommssionExapnd('${l}')" name="transactionBean" />
                                                    <%} else {%>
                                                    <html:checkbox property="print" onclick="saveExpandedBundleOFR()" name="transactionBean" />
                                                    <%}%>
                                                </td>
                                                <%}%>

                                                <%if (newFlag.equals("new")) {%>
                                                <td style="width:200px;padding-left:10px;" >
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="30" name="accountname<%=l%>" id="accountname<%=l%>"
                                                           onkeydown="getAccountName(this.value)" value="<%=accountName%>" style="font-style: italic;background-color: CCEBFF"
                                                           readonly="readonly" tabindex="-1"/>
                                                    <div id="accountname<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                            initAutocomplete("accountname<%=l%>", "accountname<%=l%>_choices", "", "",
                                                    "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <td>
                                                    <input type="text" Class="textlabelsBoldForTextBox"  size="11" name="accountno<%=l%>" id="accountno<%=l%>" value="<%=accoutnNo%>"
                                                           onkeydown="getAccountName(this.value)" style="font-style: italic;background-color: CCEBFF"
                                                           readonly="readonly" tabindex="-1"/>
                                                    <div id="accountno<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                            initAutocomplete("accountno<%=l%>", "accountno<%=l%>_choices", "", "",
                                                    "${path}/actions/getCustomer.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <%} else {%>
                                                <td style="width:200px;padding-left:10px;" >
                                                    <input type="text" Class="textlabelsBoldForTextBox"  size="30" name="accountname<%=l%>" id="accountname<%=l%>" tabindex="-1"
                                                           onkeydown="getAccountName(this.value)" value="<%=accountName%>" style="font-style: italic;background-color: CCEBFF" readonly="readonly" />

                                                    <div id="accountname<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                            initAutocomplete("accountname<%=l%>", "accountname<%=l%>_choices", "", "",
                                                    "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>

                                                </td>
                                                <td>
                                                    <input type="text" Class="textlabelsBoldForTextBox"  size="11" name="accountno<%=l%>" id="accountno<%=l%>" tabindex="-1"
                                                           value="<%=accoutnNo%>" onkeydown="getAccountName(this.value)" style="font-style: italic;background-color: CCEBFF" readonly="readonly" />
                                                    <div id="accountno<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                            initAutocomplete("accountno<%=l%>", "accountno<%=l%>_choices", "", "",
                                                    "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <%}%>


                                                <%if (chargecodedesc.equals("DRAY") || chargecodedesc.equals("INTMDL") || chargecodedesc.equals("INSURE") || chargecodedesc.equals("005")) {%>
                                                <%if ((s != null && s.toString().trim().length() == 0) || s.toString().trim().equalsIgnoreCase("-->")) {%>
                                                <td></td>
                                                <%} else {
                                                %>
                                                <td align="center">
                                                    <img id="viewgif"  src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=s.toString()%></strong>', 100, event);"
                                                         onmouseout="tooltip.hideComments();"/>
                                                </td>
                                                <%}%>
                                                <td style="padding-left:10px;">
                                                    <img src="${path}/img/icons/edit.gif" onclick="editArCharges('<%=id%>', '${bookingValues.fileNo}')" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    <%if (!chargecodedesc.equals("INSURE") && !chargecodedesc.equals("CHASFEE")) {%>
                                                    <img src="${path}/img/icons/copy.gif" onclick="changeToPerBl('<%=chargecode%>', '<%=chargeBookingNumber%>', '<%=id%>')" onmouseover="tooltip.show('<strong>Change Cost Type To PER BL CHARGES</strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                                    <%}%>
                                                </td>
                                                <%} else {%>
                                                <%if ((s != null && s.toString().trim().length() == 0) || s.toString().trim().equalsIgnoreCase("-->")) {%>
                                                <td></td>
                                                <%} else {%>
                                                <td align="center">
                                                    <img id="viewgif" src="${path}/img/icons/view.gif"  onmouseover="tooltip.showComments('<strong><%=s.toString()%></strong>', 100, event);"
                                                         onmouseout="tooltip.hideComments();"/>
                                                </td>
                                                <%}%>
                                        <c:choose>
                                        <c:when test="${roleDuty.editDeferralCharge and bookingFclUnits.chargeCodeDesc eq 'DEFER'}">
                                            <c:set var="isEdit" value='true'></c:set>
                                        </c:when>
                                        <c:when test="${!roleDuty.editDeferralCharge and bookingFclUnits.chargeCodeDesc eq 'DEFER'}">
                                            <c:set var="isEdit" value='false'></c:set>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="isEdit" value='true'></c:set>
                                        </c:otherwise>
                                        </c:choose>
                                            <td style="padding-left:10px;">
                                        <c:if test="${isEdit eq 'true'}">
                                                <img src="${path}/img/icons/edit.gif" onclick="editArCharges('<%=id%>', '${bookingValues.fileNo}')" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                        </c:if>
                                        <%if (!newFlag.equals("") && !chargecodedesc.equals("INSURE") && !chargecodedesc.equals("CHASFEE")) {%>
                                                <img src="${path}/img/icons/copy.gif" onclick="changeToPerBl('<%=chargecode%>', '<%=chargeBookingNumber%>', '<%=id%>')" onmouseover="tooltip.show('<strong>Change Cost Type To PER BL CHARGES </strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                                <%}%>
                                                <%if (!newFlag.equals("") && !chargecodedesc.equals("INLAND") && !chargecodedesc.equals("DOCUM") && !chargecodedesc.equals("CHASFEE") ) {%>
                                                <img src="${path}/img/icons/delete.gif" onclick="deleteCharges('<%=id%>')" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                <%}%>
                                            </td>
                                                <%}%>

                                                <td  style="display:none;">
                                                    <%
                                                        if (newFlag.equals("new")) {
                                                            totalAmount += book.getMarkUp();

                                                        } else {
                                                            if (null != book.getSpotRateAmt()) {
                                                                    totalAmount += book.getSpotRateAmt() + book.getSpotRateMarkUp();
                                                                } else {
                                                                totalAmount += book.getAmount() + book.getMarkUp();
                                                            }
                                                            if (book.getAdjustment() != null) {
                                                                totalAmount += book.getAdjustment();
                                                            }

                                                        }
                                                    %>
                                                    <html:text property="unitType" value="<%=unitValue%>" style="visibility:hidden" size="3"/>
                                                    <html:text property="costType"  maxlength="30" size="7" value="<%=costType%>" readonly ="true" tabindex="-1"/>
                                                    <html:text property="chargeCddesc" value="<%=chargecodedesc%>" readonly ="true" size="3" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="chargeCodes" value="<%=chargecode%>" readonly ="true" size="8" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="currency"  maxlength="30" size="3" value="<%=currency%>" readonly ="true" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="splEqpUnits" style="font-style:italic;visibility:hidden" value="${bookingFclUnits.specialEquipmentUnit}"></html:text>
                                                    <html:text property="standardCharge" style="font-style:italic;visibility:hidden" value="${bookingFclUnits.standardCharge}"></html:text>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        <%
                                                unittemp = unitValue;
                                                previousUnit = unit;
                                                if (className == "even") {
                                                    className = "odd";
                                                } else {
                                                    className = "even";
                                                }
                                                l++;
                                            }
                                        %>
                                        <%if (hasEquipment) {
                                                hasEquipment = false;
                                                if (null != prevSpclEquipUnit && !prevSpclEquipUnit.equals("")) {%>
                                        <tr>
                                            <td></td>
                                            <td class="whitebackgrnd" class="textlabelsBold">
                                                <font style="color: #D80000;"><%=prevSpclEquipUnit%></font>
                                            </td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                        </tr>
                                        <%}
                                            }%>
                                        <tr>
                                            <td></td>
                                            <td><b  class="blackBG">Total:for</b></td>
                                            <td><b  class="blackBG"> <%=unittemp%></b></td>
                                            <td><b  class="blackBG">--------</b></td>
                                            <td><b  class="blackBG">-------------</b></td>
                                            <td><b  class="blackBG">--------</b></td>

                                            <td><b  class="blackBG">-------------</b></td>

                                            <td><b  class="blackBG">-------------></b></td>
                                            <td><b  class="blackBG"><%=numformat.format(totalAmount)%></b></td>
                                        </tr>
                                    </table>

                                    <%}%>
                                </div>
                                <br /></td>
                        </tr>

                        <tr><%--11--%>
                            <td id="collapseRates">
                                <div id="divtablesty1" style="border:thin;">
                                    <%
                                        temp = "";
                                        l = 0;
                                        totalAmount = 0.00;
                                        totalRatesAmount = 0.00;
                                        unittemp = "";
                                        unit = "";
                                        unitValue = "";
                                        previousUnit = "";
                                        String commentTemp = "";
                                        Double sell = 0.00;
                                        Double cost = 0.00;
                                        fcllist = (List) request.getAttribute("consolidaorList");
                                        if (fcllist != null && fcllist.size() > 0) {%>
                                    <table border="0" cellpadding="3" cellspacing="0" id="collapseRatesTable" class="displaytagstyleNew" style="width:60%">
                                        <thead><tr>
                                                <%
                                                    if (userid != null && hasUserLevelAccess) {%>
                                                <td><img src="${path}/img/icons/up.gif" border="0" onclick="getExpandRates()" id="collpaseicon"/></td>
                                                    <%} else {%>
                                                <%}%>
                                                <td>Select</td>
                                                <td>Unit</td>
                                                <td>Number &nbsp; Out of Gauge</td>
                                                <td>ChargeCode</td>
                                                <td>Sell</td>
                                                <td>Adjustment</td>
                                                <td>Comments</td>
                                                <c:if test="${bookingValues.spotRate eq 'Y'}">
                                                <td>Spot Cost</td>
                                                 </c:if>
                                                <td>&nbsp;&nbsp;Bundle into OFR</td>
                                                <td style="padding-left:10px;">Vendor Name</td>
                                                <td>Vendor No</td>
                                                <td>Comment</td>
                                                <td style="padding-left:10px;">Actions</td>
                                                </tr>

                                        </thead>
                                        <%
                                            className = "odd";
                                            while (l < fcllist.size()) {
                                                sell = 0.00;
                                                cost = 0.00;
                                                manualCharges = "";
                                                BookingfclUnits book = new BookingfclUnits();
                                                book = (BookingfclUnits) fcllist.get(l);
                                                transactionBean.setCollapseCheck(book.getApproveBl());
                                                transactionBean.setCheckSplEqpUnitsCollapse(book.getApproveBl());
                                                transactionBean.setCheckStandardChargeCollapse(book.getApproveBl());
                                                if (book.getPrint() != null) {
                                                    transactionBean.setCollapseprint(book.getPrint());
                                                } else {
                                                    transactionBean.setCollapseprint("off");
                                                }
                                                request.setAttribute("transactionBean", transactionBean);
                                                id = String.valueOf(book.getId());
                                                request.setAttribute("bookId", id);
                                                chargeBookingNumber = book.getBookingNumber();
                                                if (book.getSpecialEquipmentUnit() == null) {
                                                    book.setSpecialEquipmentUnit("");
                                                }
                                                if (null != book.getManualCharges()) {
                                                    manualCharges = book.getManualCharges();
                                                }
                                                request.setAttribute("manualCharges", manualCharges);
                                                if (book.getStandardCharge() == null) {
                                                    book.setStandardCharge("");
                                                }
                                                if (book.getUnitType() != null) {
                                                    unitValue = book.getUnitType().getCodedesc().toString();
                                                    unit = unitValue + "-" + book.getSpecialEquipmentUnit() + "-" + book.getStandardCharge();
                                                }
                                                if (book.getOutOfGaugeComment() != null) {
                                                    outOfGaugeComment = book.getOutOfGaugeComment().replace("\r\n", "<br>").replace("\n", "<br>");
                                                } else {
                                                    outOfGaugeComment = "";
                                                }
                                                if (book.getSpecialEquipment() != null) {
                                                    spclEquipUnit = book.getSpecialEquipment();
                                                } else {
                                                    spclEquipUnit = "";
                                                }
                                                request.setAttribute("splEquipmentUnits", book.getSpecialEquipmentUnit());
                                                request.setAttribute("splEquipment", book.getSpecialEquipment());
                                                request.setAttribute("bookingFclUnits", book);
                                                request.setAttribute("UnitName", unit);
                                                request.setAttribute("unitValue", unitValue);
                                                chargecode = book.getChgCode();
                                                costType = book.getCostType();
                                               if (book.getAmount() != null && !book.getAmount().equals("")) {
                                                    amt = numformat.format(book.getAmount());
                                                    sell += book.getAmount();
                                                }

                                                cost = sell;
                                                if (book.getSpotRateMarkUp() != null) {
                                                    markUp1 = numformat.format(book.getSpotRateMarkUp());
                                                    sell += book.getSpotRateMarkUp();
                                                }else if (book.getMarkUp() != null) {
                                                    markUp1 = numformat.format(book.getMarkUp());
                                                    sell += book.getMarkUp();
                                                }

                                                if (book.getNewFlag() != null) {
                                                    newFlag = book.getNewFlag();
                                                } else {
                                                    newFlag = "";
                                                }
                                                currency = book.getCurrency();
                                                num = book.getNumbers();
                                                chargecodedesc = book.getChargeCodeDesc();
                                                if (chargecodedesc.equals("INTFS") || chargecodedesc.equals("INTRAMP")) {
                                                    Charges = "INTMDL";
                                                } else {
                                                    Charges = chargecodedesc;
                                                }
                                                if (chargecode.equalsIgnoreCase("Intermodal F/S") || chargecode.equalsIgnoreCase("Intermodal Ramp")) {
                                                    Charges += "/" + "INTERMODAL";
                                                    chargecode = "INTERMODAL RAMP";
                                                } else {
                                                    Charges += "/" + chargecode;
                                                }
                                                request.setAttribute("Chargecode", chargecode);

                                                sellRate = numformat.format(sell);
                                                costRate = numformat.format(cost);
                                                request.setAttribute("costRate", costRate);
                                                if (null != chargecodedesc && ("OCNFRT".equals(chargecodedesc) || "OFIMP".equals(chargecodedesc))) {
                                                    oceanFrightForCollapse += unittemp + "--" + sellRate.toString() + ")";
                                                    request.setAttribute("oceanFrightForCollapse", oceanFrightForCollapse);
                                                }
                                                if (book.getAccountNo() != null) {
                                                    accoutnNo = book.getAccountNo();
                                                } else {
                                                    accoutnNo = "";
                                                }
                                                if (book.getAccountName() != null) {
                                                    accountName = book.getAccountName();
                                                } else {
                                                    accountName = "";
                                                }
                                                if (book.getFutureRate() != null) {
                                                    newRate = numformat.format(book.getFutureRate());
                                                } else {
                                                    newRate = "";
                                                }
                                                if (book.getEfectiveDate() != null) {
                                                    effectiveDate = sdf.format(book.getEfectiveDate());
                                                } else {
                                                    effectiveDate = "";
                                                }
                                                if (book.getComment() != null) {
                                                    commentTemp = book.getComment();
                                                    int k = 0;
                                                    if (commentTemp != null && !commentTemp.equals("")) {
                                                        k = commentTemp.indexOf("|");
                                                        if (k != -1) {
                                                            comment = commentTemp.substring(0, k);
                                                            commentTemp = commentTemp.substring(k + 1);
                                                        } else {
                                                            comment = commentTemp;
                                                            commentTemp = "";
                                                        }
                                                    }
                                                } else {
                                                    comment = "";
                                                }
                                                if (book.getAdjustment() != null) {
                                                    adjustment = numformat.format(book.getAdjustment());
                                                } else {
                                                    adjustment = "0.00";
                                                }
                                                if (book.getAdjustmentChargeComments() != null) {
                                                    adjustmentChargeComments = book.getAdjustmentChargeComments();
                                                } else {
                                                    adjustmentChargeComments = "";
                                                }
                                                request.setAttribute("adjRemarks", adjustmentChargeComments);
                                                StringBuilder s = new StringBuilder();
                                                if (comment != null) {
                                                    int index = 0;
                                                    char[] commentArray = comment.toCharArray();
                                                    for (int i = 0; i < commentArray.length; i++) {
                                                        if (commentArray[i] == '\n') {
                                                            s.append(comment.substring(index, i).trim());
                                                            s.append(" ");
                                                            index = i + 1;
                                                        }
                                                    }
                                                    s.append(comment.substring(index, comment.length()).trim());

                                                }
                                                s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                                s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                                request.setAttribute("EffectiveDate", effectiveDate);
                                                request.setAttribute("l", l);
                                        %>

                                        <tbody>
                                            <%
                                                if (!previousUnit.equals(unit) && !previousUnit.equals("")) {
                                                    ocenFrightRollUpAmount += unittemp + "--" + totalRatesAmount.toString() + ")";
                                                    request.setAttribute("ocenFrightRollUpAmount", ocenFrightRollUpAmount);
                                            %>
                                            <%if (hasEquipment) {
                                                    hasEquipment = false;
                                                    if (null != prevSpclEquipUnit && !prevSpclEquipUnit.equals("")) {%>
                                            <tr>
                                                <td></td>
                                                <td class="whitebackgrnd" class="textlabelsBold">
                                                    <font style="color: #D80000;"><%=prevSpclEquipUnit%></font>
                                                </td>
                                            </tr>
                                            <%}
                                                }%>
                                            <tr>
                                                <td></td>
                                                <td><b  class="blackBG">Total:for</b></td>
                                                <td><b  class="blackBG"> <%=unittemp%></b></td>
                                                <td><b  class="blackBG">-------------------------</b></td>
                                                <td><b  class="blackBG">-----------------------------------------------</b></td>
                                                <td><b  class="blackBG">--------------></b></td>
                                                <td><b  class="blackBG"><%=numformat.format(totalAmount)%></b></td>

                                            </tr>
                                            <tr>
                                                <td colspan="15" style="border-top:1px solid #c5c4c5">&nbsp</td>
                                            </tr>

                                            <%
                                                    totalAmount = 0.00;
                                                    totalRatesAmount = 0.00;
                                                }
                                            %>
                                            <tr class="<%=className%>">

                                                <td class="whitebackgrnd"></td>
                                                <%if (!previousUnit.equals(unit)) {%>
                                                <td>
                                                    <html:checkbox property="collapseCheck" value="<%=id%>" onclick="getNumbersChangedCollapse();checkAndUnCheckContainer(this);" name="transactionBean"/>
                                                    <c:if test="${! empty splEquipmentUnits}">
                                                        <img src="${path}/img/icons/delete.gif" onclick="deleteSpecialEquipmentUnit('${splEquipmentUnits}', '${bookingFclUnits.standardCharge}')" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    </c:if>
                                                    <html:checkbox property="checkSplEqpUnitsCollapse"  value="${bookingFclUnits.specialEquipmentUnit}" style="visibility:hidden" name="transactionBean"/>
                                                    <html:checkbox property="checkStandardChargeCollapse"  value="${bookingFclUnits.standardCharge}" style="visibility:hidden" name="transactionBean"/>
                                                </td>
                                                <td><font style="background-color:#CCEBFF;"><c:out value="${unitValue}"/></font></td>
                                                <td><input name="hiddennumbers" id="hiddennumbers<%=l%>" Class="textlabelsBoldForTextBox" value="<%=num%>" size="3" onkeydown="setNumberval(event);"
                                                           maxlength="3"  onchange="getNumbersChangedCollapse();
                                                validateQuantity(this.value, '<%=l%>')"  />
                                                    &nbsp; <c:choose>
                                                        <c:when test="${bookingFclUnits.outOfGauge == 'Y'}">
                                                            <input type="radio"  name="outOfGage${bookingFclUnits.chargeCodeDesc}${UnitName}" value="Y" checked onclick="checkGaugeComment('C', '${UnitName}', this)"/>Y
                                                            <input type="radio"  name="outOfGage${bookingFclUnits.chargeCodeDesc}${UnitName}" value="N" onclick="checkGaugeComment('C', '${UnitName}', this)"/>N
                                                            <img src="${path}/img/icons/edit.gif" onclick="openOutOfGuageComments('${bookingFclUnits.unitType.id}', '${bookingFclUnits.standardCharge}', '<%=outOfGaugeComment%>')"
                                                                 onmouseover="tooltip.show('<strong>Add/Edit Comment</strong>', null, event);" onmouseout="tooltip.hide();" id="C${UnitName}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="radio"  name="outOfGage${bookingFclUnits.chargeCodeDesc}${UnitName}" value="Y" onclick="checkGaugeComment('C', '${UnitName}', this)"/>Y
                                                            <input type="radio"  name="outOfGage${bookingFclUnits.chargeCodeDesc}${UnitName}" value="N" checked onclick="checkGaugeComment('C', '${UnitName}', this)"/>N
                                                            <img src="${path}/img/icons/edit.gif" onclick="openOutOfGuageComments('${bookingFclUnits.unitType.id}', '${bookingFclUnits.standardCharge}', '')"
                                                                 style="visibility: hidden" id="C${UnitName}"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <%hasEquipment = true;
                                                    prevSpclEquipUnit = spclEquipUnit;
                                                } else {%>
                                                <%if (hasEquipment) {
                                                        hasEquipment = false;%>
                                                <td colspan="2">
                                                    <c:if test="${! empty splEquipmentUnits}">
                                                        <font style="color: #D80000;">${splEquipment}</font>
                                                    </c:if>
                                                </td>
                                                <%} else {%>
                                                <td></td>
                                                <td></td>
                                                <%}%>
                                                <td><input name="hiddennumbers" id="hiddennumbers<%=l%>" Class="textlabelsBoldForTextBox" value="<%=num%>"
                                                           size="3" maxlength="3" onchange="validateQuantity(this.value, '<%=l%>')" style="visibility:hidden"  /></td>
                                                    <%}
                                                        unittemp = unit;
                                                        previousUnit = unit;%>

                                                <%if (newFlag.equals("new") || newFlag.equals("FF") || newFlag.equals("IN") || newFlag.equals("D") || newFlag.equals("PP")) {%>
                                                <td><font class="starColor" >*</font>
                                                    <span style="font-style: italic"><c:out value="${Chargecode}"/></span></td>
                                                    <%} else {%>
                                                <td><c:out value="${Chargecode}"/></td>
                                                <%}%>
                                                <%if (newFlag.equals("new")) {%>
                                                <td><input name="sellRate" id="sellRate" Class="BackgrndColorForTextBox" readonly="true"  maxlength="15" size="6" value="<%=markUp1%>"  tabindex="-1"/></td>
                                                    <%} else {%>
                                                <td><input name="sellRate" id="sellRate" Class="BackgrndColorForTextBox" readonly="true"  maxlength="15" size="6" value="<%=sellRate%>"  tabindex="-1"/></td>
                                                    <%}%>


                                                <%if (newFlag.equals("new") || newFlag.equals("FF") || newFlag.equals("IN") || newFlag.equals("D") ||  newFlag.equals("PP")) {%>
                                                <td><html:text property="hiddenAdjustment" styleClass="textlabelsBoldForTextBox"  value="0.00" size="6" onchange="allowOnlyWholeNumbers(this)"
                                                            maxlength="8" style="display:none;"   /></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                <%} else {%>
                                                <td><html:text property="hiddenAdjustment" styleClass="textlabelsBoldForTextBox" onchange="allowOnlyWholeNumbers(this);getAccountNameById(this.value,${bookId})"
                                                    value="<%=adjustment%>" size="6" maxlength="8" onkeypress="getNumbersChangedCollapse()"
                                                               styleId="hiddenAdjustment${bookId}"/></td>
                                                <td>
                                                    <c:if test="${adjRemarks ne null and adjRemarks ne ''}">
                                                    <div id="consolidate-adj">
                                                        <img class="adjustmentRemarks" alt="Adjustment Remarks" src="${path}/img/icons/view.gif"
                                                             onmouseover="showToolTip('${adjRemarks}', 100, event)"
                                                             onmouseout="tooltip.hideComments();"/>
                                                    </div>
                                                    </c:if>
                                                </td>
                                                <%}%>
                                                  <c:if test="${bookingValues.spotRate eq 'Y'}">
                                                    <td width="80%">
                                                        <fmt:formatNumber pattern="##,###,##0.00" var="spotAmt" value="${bookingFclUnits.spotRateAmt}"/>
                                                        <html:text property="spotRateAmt" styleClass="BackgrndColorForTextBox"
                                                                   readonly="true" value="${spotAmt}" size="5" tabindex="-1" ></html:text>
                                                        </td>
                                                    </c:if>

                                                <%if ("OCNFRT".equals(chargecodedesc) || "OFIMP".equals(chargecodedesc)) {%>
                                                <td align="center">
                                                    <html:checkbox property="collapseprint" name="transactionBean" onclick="saveCollapsedBundleOFR()"
                                                                   style="visibility:hidden" /></td>
                                                    <%} else {%>
                                                <td align="center">
                                                    <%
                                                        if (chargecodedesc.equals("005")) {%>
                                                    <html:checkbox property="collapseprint" name="transactionBean" onclick="getCheckFFCommssionCollapse('${l}')" />
                                                    <%} else {%>
                                                    <html:checkbox property="collapseprint" name="transactionBean" onclick="saveCollapsedBundleOFR()" />
                                                    <%}%>
                                                </td>
                                                <%}%>


                                                <%if (newFlag.equals("new")) {%>
                                                <td style="width:200px;padding-left:10px;">
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="30" name="accountname<%=l%>" id="accountname<%=l%>"
                                                           value="<%=accountName%>" style="font-style: italic;background-color: CCEBFF"
                                                           readonly="readonly"  tabindex="-1"/>
                                                    <div id="accountname<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                            initAutocomplete("accountname<%=l%>", "accountname<%=l%>_choices", "", "",
                                                    "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <td>
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="11" name="accountno<%=l%>" id="accountno<%=l%>" value="<%=accoutnNo%>"
                                                           style="font-style: italic;background-color: CCEBFF" readonly="readonly"  tabindex="-1"/>
                                                    <div id="accountno<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocomplete("accountno<%=l%>", "accountno<%=l%>_choices", "", "",
                                                                "${path}/actions/getCustomer.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <%} else {%>
                                                <td style="width:200px;padding-left:10px;" >
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="30" name="accountname<%=l%>" id="accountname<%=l%>"
                                                           value="<%=accountName%>" style="font-style: italic;background-color: CCEBFF"
                                                           readonly="readonly"  tabindex="-1"/>
                                                    <div id="accountname<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocomplete("accountname<%=l%>", "accountname<%=l%>_choices", "", "",
                                                                "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <td>
                                                    <input type="text" Class="textlabelsBoldForTextBox"  size="11" name="accountno<%=l%>" id="accountno<%=l%>" value="<%=accoutnNo%>"
                                                           style="font-style: italic;background-color: CCEBFF" readonly="readonly"  tabindex="-1"/>

                                                    <div id="accountno<%=l%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocomplete("accountno<%=l%>", "accountno<%=l%>_choices", "", "",
                                                                "${path}/actions/getCustomer.jsp?tabName=BOOKING&from=0&index=<%=l%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <%}%>


                                                <%if (chargecodedesc.equals("DRAY") || chargecodedesc.equals("INTMDL") || chargecodedesc.equals("INSURE") || chargecodedesc.equals("005")) {%>
                                                <%if ((s != null && s.toString().trim().length() == 0) || s.toString().trim().equalsIgnoreCase("-->")) {%>
                                                <td>
                                                </td>
                                                <%} else {%>
                                                <td align="center">
                                                    <img id="viewgif1"  src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=s.toString()%></strong>', 100, event);"
                                                         onmouseout="tooltip.hideComments();"/>
                                                </td>
                                                <%}%>
                                                <td style="padding-left:10px;">
                                                    <img src="${path}/img/icons/edit.gif" onclick="editArCharges('<%=id%>', '${bookingValues.fileNo}')" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    <%if (!chargecodedesc.equals("INSURE") && !chargecodedesc.equals("CHASFEE")) {%>
                                                    <img src="${path}/img/icons/copy.gif" onclick="changeToPerBl('<%=chargecode%>', '<%=chargeBookingNumber%>', '<%=id%>')" onmouseover="tooltip.show('<strong>Change Cost Type To PER BL CHARGES</strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                                    <%}%>
                                                </td>
                                                <%} else {%>
                                                <%if ((s != null && s.toString().trim().length() == 0) || s.toString().trim().equalsIgnoreCase("-->")) {%>
                                                <td>
                                                </td>
                                                <%} else {%>
                                                <td align="center">
                                                    <img  id="viewgif1"  src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=s.toString()%></strong>', 100, event);"
                                                          onmouseout="tooltip.hideComments();"/>
                                                </td>
                                                <%}%>
                                                <td style="padding-left:10px;">
                                                    <img src="${path}/img/icons/edit.gif" onclick="editCollapseCharges('<%=id%>', '${bookingValues.fileNo}', '${bookingValues.bookingId}', '<%=sellRate%>', '${costRate}', '${manualCharges}')" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                                    <%if (!newFlag.equals("") && !chargecodedesc.equals("INSURE") && !chargecodedesc.equals("CHASFEE")) {%>
                                                    <img src="${path}/img/icons/copy.gif" onclick="changeToPerBl('<%=chargecode%>', '<%=chargeBookingNumber%>', '<%=id%>')" onmouseover="tooltip.show('<strong>Change Cost Type To PER BL CHARGES</strong>', null, event);"	onmouseout="tooltip.hide();"/>
                                                    <%}%>
                                                    <%if (!newFlag.equals("") && !chargecodedesc.equals("INLAND") && !chargecodedesc.equals("DOCUM") && !chargecodedesc.equals("CHASFEE")) {%>
                                                    <img src="${path}/img/icons/delete.gif" onclick="deleteCharges('<%=id%>')" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    <%}%>
                                                </td>
                                                <%}%>

                                                <td  style="display: none;">
                                                    <html:text property="chargeAmount" value="<%=amt%>" style="visibility:hidden" size="1"/>
                                                    <html:text property="chargeMarkUp" value="<%=markUp1%>" style="visibility:hidden" size="1"/>
                                                    <%

                                                        if (newFlag.equals("new")) {
                                                            totalAmount += book.getMarkUp();
                                                        } else {
                                                            totalAmount += sell;
                                                            totalRatesAmount += sell;
                                                            if (book.getAdjustment() != null) {
                                                                totalAmount += book.getAdjustment();
                                                                totalRatesAmount += book.getAdjustment();
                                                            }
                                                        }
                                                    %>
                                                    <html:text property="hiddenunitType" value="<%=unitValue%>" style="visibility:hidden" size="3"/>
                                                    <html:text property="costType"  maxlength="30" size="7" value="<%=costType%>" readonly ="true" tabindex="-1"/>
                                                    <html:text property="hiddenchargeCddesc" value="<%=chargecodedesc%>" readonly ="true" size="3" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="chargeCodes" value="<%=chargecode%>" readonly ="true" size="8" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="currency"  maxlength="30" size="3" value="<%=currency%>" readonly ="true" style="visibility:hidden" tabindex="-1"/>
                                                    <html:text property="splEqpUnitsCollapse" style="font-style:italic;visibility:hidden" value="${bookingFclUnits.specialEquipmentUnit}"></html:text>
                                                    <html:text property="standardChargeCollapse" style="font-style:italic;visibility:hidden" value="${bookingFclUnits.standardCharge}"></html:text>
                                                    </td>
                                                </tr>

                                            </tbody>
                                        <%
                                                unittemp = unitValue;
                                                previousUnit = unit;
                                                if (className == "even") {
                                                    className = "odd";
                                                } else {
                                                    className = "even";
                                                }
                                                l++;
                                            }
                                            ocenFrightRollUpAmount += unittemp + "--" + totalRatesAmount.toString() + ")";
                                        %>
                                        <%if (hasEquipment) {
                                                hasEquipment = false;
                                                if (null != prevSpclEquipUnit && !prevSpclEquipUnit.equals("")) {%>
                                        <tr>
                                            <td></td>
                                            <td class="whitebackgrnd" class="textlabelsBold">
                                                <font style="color: #D80000;"><%=prevSpclEquipUnit%></font>
                                            </td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                            <td><b class="blackBG"></b></td>
                                        </tr>
                                        <%}
                                            }%>
                                        <tr>
                                            <td></td>
                                            <td><b  class="blackBG">Total:for</b></td>
                                            <td><b  class="blackBG"> <%=unittemp%></b></td>
                                            <td><b  class="blackBG">------------------------</b></td>
                                            <td><b  class="blackBG">-----------------------------</b></td>
                                            <td><b  class="blackBG">-------------></b></td>
                                            <td><b  class="blackBG"><%=numformat.format(totalAmount)%></b></td>
                                        </tr>
                                    </table>
                                    <table>
                                    </table>
                                    <%}%>
                                </div>
                                <br /></td>
                        </tr>

                        <tr><%--33--%>
                            <%
                                List otherChargesList = (List) request.getAttribute("otherChargesList");
                                NumberFormat numformat1 = new DecimalFormat("##,###,##0.00");
                                //String amt[]=new String[50];
                                String chargeCd = "";
                                String chargecode2 = "";
                                String costType2 = "";
                                String retail = "0.00";
                                String currecny = "";
                                String otherEffectiveDate = "";
                                String othermarkUp = "";
                                String otherSellRate = "";
                                String otherAccountNo = "";
                                String otherAccountName = "";
                                String newFlag2 = "";
                                String comment1 = "";
                                String commentTempOther = "";
                                id = "";
                                Double total = 0.00;
                                TransactionBean transBean = new TransactionBean();
                                transBean.setOtherprint("off");
                                if ((otherChargesList != null && otherChargesList.size() > 0)) {
                                    int i = 0;
                            %>
                            <td align="left">
                                <div id="divtablesty1" style="border:thin;">
                                    <table  border="0"  cellpadding="1.5" cellspacing="0" class="displaytagstyleNew">
                                        <tr style="background-color: #8DB7D6;font-size:12px">
                                            <td colspan="13">for Other Applicable Charges</td>
                                        </tr>
                                        <thead style="display: block;visibility: hidden;"><tr>
                                                <td class="whitebackgrnd">&nbsp;</td>
                                                <td><b  class="blackBG">Total:for</b></td>
                                                <td><b  class="blackBG"> Total:for</b></td>
                                                <td><b  class="blackBG">---------</b></td>
                                                <td><b  class="blackBG">----------------------</b></td>
                                                <td><b  class="blackBG">-------------></b></td>
                                                <td>Adjustment</td>
                                                <td>Bundle into OFR</td>
                                                <%-- <td style="padding-left:10px;">Effective Date</td>--%>
                                                <td>Vendor Name</td>
                                                <td>Vendor No</td>
                                                <td>Comment</td>
                                                <td style="padding-left:10px;">Actions</td>
                                                <td width="1%" class="whitebackgrnd"></td></tr>
                                        </thead>
                                        <%
                                            className = "odd";
                                            String Charges2 = "";
                                            while (i < otherChargesList.size()) {
                                                BookingfclUnits b = new BookingfclUnits();
                                                b = (BookingfclUnits) otherChargesList.get(i);
                                                id = String.valueOf(b.getId());
                                                chargeCd = b.getChargeCodeDesc();
                                                chargecode2 = b.getChgCode();
                                                Charges2 = chargeCd + "/" + chargecode2;
                                                request.setAttribute("chargecode2", chargecode2);
                                                costType2 = b.getCostType();
                                                if (b.getNewFlag() != null) {
                                                    newFlag2 = b.getNewFlag();
                                                } else {
                                                    newFlag2 = "";
                                                }
                                                if (b.getMarkUp() != null) {
                                                    othermarkUp = numformat1.format(b.getMarkUp());
                                                    total += b.getMarkUp();
                                                }
                                                if (b.getSellRate() != null) {
                                                    otherSellRate = numformat1.format(b.getSellRate());

                                                }
                                                if (b.getAccountNo() != null) {
                                                    otherAccountNo = b.getAccountNo();
                                                } else {
                                                    otherAccountNo = "";
                                                }
                                                if (b.getAccountName() != null) {
                                                    otherAccountName = b.getAccountName();
                                                } else {
                                                    otherAccountName = "";
                                                }
                                                if (b.getEfectiveDate() != null) {
                                                    otherEffectiveDate = sdf.format(b.getEfectiveDate());
                                                } else {
                                                    otherEffectiveDate = "";
                                                }
                                                if (b.getComment() != null) {
                                                    commentTempOther = b.getComment();
                                                    int p = 0;
                                                    if (!commentTempOther.equals("")) {
                                                        p = commentTempOther.indexOf("|");
                                                        if (p != -1) {
                                                            comment1 = commentTempOther.substring(0, p);
                                                            commentTempOther = commentTempOther.substring(p + 1);
                                                        } else {
                                                            comment1 = commentTempOther;
                                                            commentTempOther = "";
                                                        }
                                                    }
                                                } else {
                                                    comment1 = "";
                                                }
                                                StringBuilder s = new StringBuilder();
                                                if (comment1 != null) {

                                                    int index = 0;
                                                    char[] commentArray = comment1.toCharArray();
                                                    for (int j = 0; j < commentArray.length; j++) {
                                                        if (commentArray[j] == '\n') {
                                                            s.append(comment1.substring(index, j).trim());
                                                            s.append(" ");
                                                            index = j + 1;
                                                        }
                                                    }
                                                    s.append(comment1.substring(index, comment1.length()).trim());

                                                }
                                                s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                                s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                                if (b.getPrint() != null) {
                                                    transBean.setOtherprint(b.getPrint());
                                                } else {
                                                    transBean.setOtherprint("off");
                                                }
                                                request.setAttribute("transactionbean", transBean);
                                                request.setAttribute("OtherEffectiveDate", otherEffectiveDate);

                                        %>
                                        <tbody>
                                            <tr class="<%=className%>">
                                                <td class="whitebackgrnd" >&nbsp;</td>
                                                <%if (newFlag2.equals("new") || newFlag2.equals("D") || newFlag2.equals("IN")) {%>
                                                <td><font class="starColor" >*</font>
                                                    <span style="font-style: italic"><c:out value="${chargecode2}"></c:out></span></td>
                                                    <%} else {%>
                                                <td><c:out value="${chargecode2}"></c:out></td>
                                                <%}%>
                                                <%if (newFlag2.equals("new") || newFlag2.equals("D") || newFlag2.equals("IN") || b.getManualCharges().equalsIgnoreCase("D")) {%>
                                                <td>
                                                    <html:text property="otherSellRate" styleClass="BackgrndColorForTextBox" readonly="true"
                                                    maxlength="30" size="6" value="<%=othermarkUp%>" tabindex="-1" />
                                                </td>
                                                <%} else {%>
                                                <td>
                                                    <html:text property="otherSellRate" styleClass="BackgrndColorForTextBox" readonly="true"
                                                    maxlength="30" size="6" value="<%=retail%>" tabindex="-1" />
                                                </td>
                                                <%}%>
                                                <td>
                                                    <html:text property="othermarkUp" styleClass="textlabelsBoldForTextBox"
                                                    maxlength="30" size="6" value="<%=othermarkUp%>" style="visibility:hidden;" />
                                                </td>

                                                <td align="center">
                                                    <html:checkbox property="otherprint" name="transactionbean" onclick="saveOtherBundleOFR()"/></td>

                                                <td style="padding-left: 14px;">
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="30" name="otheraccountname<%=i%>" tabindex="-1"
                                                           id="otheraccountname<%=i%>"  value="<%=otherAccountName%>" style="background-color: CCEBFF" readonly="readonly"  />

                                                    <div id="otheraccountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocomplete("otheraccountname<%=i%>", "otheraccountname<%=i%>_choices", "", "",
                                                                "${path}/actions/getCustomerName.jsp?tabName=BOOKING&from=5&index=<%=i%>&isDojo=false", "");
                                                    </script>
                                                </td>
                                                <td>
                                                    <input type="text" Class="textlabelsBoldForTextBox" size="15" name="otheraccountno<%=i%>"
                                                           id="otheraccountno<%=i%>" value="<%=otherAccountNo%>"  style="background-color: CCEBFF"
                                                           readonly="readonly"  tabindex="-1"/>
                                                    <div id="otheraccountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocomplete("otheraccountno<%=i%>", "otheraccountno<%=i%>_choices", "", "",
                                                                "${path}/actions/getCustomer.jsp?tabName=BOOKING&from=5&index=<%=i%>&isDojo=false", "");
                                                    </script>

                                                </td>
                                                <%if ((s != null && s.toString().trim().length() == 0) || s.toString().trim().equalsIgnoreCase("-->")) {%>
                                                <td>
                                                </td>
                                                <%} else {%>
                                                <td align="center">
                                                    <img id="viewgif2"   src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=s.toString()%></strong>', 100, event);"
                                                         onmouseout="tooltip.hideComments();"/>
                                                </td>
                                                <%}%>
                                                <td>
                                                    <img src="${path}/img/icons/edit.gif" onclick="editArCharges('<%=id%>', '${bookingValues.fileNo}')" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    <% if (null != b.getManualCharges() && !b.getManualCharges().equalsIgnoreCase("D")) {%>
                                                    <img src="${path}/img/icons/delete.gif" onclick="deleteCharges1('<%=id%>')" id="deleteimg" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                                    <% }%>
                                                </td>

                                                <html:hidden property="retail"  value="<%=retail%>" />
                                                <html:hidden property="othermarkUp"  value="<%=othermarkUp%>" />
                                                <html:hidden property="costType1"   value="<%=costType2%>"/>
                                                <html:hidden property="othercurrecny"   value="<%=currecny%>" />
                                            </tr>
                                        </tbody>
                                        <%
                                                if (className == "even") {
                                                    className = "odd";
                                                } else {
                                                    className = "even";
                                                }
                                                i++;
                                            }
                                        %>


                                    </table>
                                    <table>
                                        <tr>
                                            <td></td>
                                            <td><b  class="blackBG">Total:</b></td>
                                            <td><b style="font-size:15;color: black;">for Other Applicable</b></td>
                                            <td><b  class="blackBG">Charges------------</b></td>
                                            <td><b  class="blackBG">--------------></b></td>
                                            <td><b  class="blackBG"><%=numformat1.format(total)%></b></td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                            <%}%>
                        </tr>


                        <tr style="padding-top:10px;"><td><%--44--%>
                                <table><%--last--%>
                                    <tr>
                                        <td align="right">
                                            <c:choose>
                                                <c:when test="${bookingValues.bookingComplete=='Y'}">
                                                    <input type="button"  class="buttonStyleNew" id="inputRatesManually"  value="Input Rates Manually" style="width: 120px;display: none;"
                                                           onclick="goToBookingCharges('${bookingValues.ratesNonRates}')"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="button"  class="buttonStyleNew" id="inputRatesManually"  value="Input Rates Manually" style="width: 120px;"
                                                           onclick="goToBookingCharges('${bookingValues.ratesNonRates}')"/>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>

                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${empty fclBlCostCodesList}">
                                                    <input type="button"  class="buttonStyleNew"  value="Preview Accruals" style="width: 108px;display: none;"
                                                           id="previewBookingAccruals" onclick="showBookingAccruals();"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="button"  class="buttonStyleNew"  value="Preview Accruals"
                                                           id="previewBookingAccruals" onclick="showBookingAccruals()"/>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${bookingValues.bookingComplete=='Y' || empty bookingValues.bookingId}">
                                                    <input type="button"  class="buttonStyleNew" id="addBookingAccrual"  value="Add Accrual" style="width: 108px;display: none;"
                                                           onclick="addingAccrualsInBooking('${importFlag}')"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="button"  class="buttonStyleNew" id="addBookingAccrual"  value="Add Accrual" style="width: 108px;"
                                                           onclick="addingAccrualsInBooking('${importFlag}')"/>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                    </tr>

                                </table><%--last ends--%>
                                <div id="showBookingAccrual" style="display: none;width: 100%;float:left">
                                    <c:import url="/jsp/fcl/accrualsResult.jsp"/>
                                </div>
                            </td></tr><%--44--%>
                    </table>
                </td></tr></table><%--rates ends--%>
    </div>
</div>
<table width="100%" cellspacing="0" border="0"  cellpadding="0">
    <tr class="textlabelsBold"></tr>
    <tr>
        <td>
            <table>
                <tr class="textlabelsBold">
                    <td id="scroll">File No :<font color="#FF4A4A" size="4">${fileNo}</font></td>
                        <td id="spotRateMsgDiv2" style="display:none">
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <div id="spotRateMsgStatus2" class="red bold"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="left">
            <input type="button" value="Go Back" id="cancel1" class="buttonStyleNew"
                   onclick="compareWithOldArray()"/>
            <input type="button" value="Save" id="save1" class="buttonStyleNew" onclick="saveOrUpdate()" style="width:50px;"
                   onmouseover="tooltip.showTopText('<strong>${mandatoryFieldForBooking}</strong>', null, event);" onmouseout="tooltip.hide();"/>
            <c:if test="${bookingValues.blFlag!='on'}">
                <input type="button"  class="buttonStyleNew" id="charge" value="ConvertToBL"
                       onclick="converttobl()" style="width:80px"/>
            </c:if>
                    <input type="button"  class="buttonStyleNew" id="copyBooking" value="Copy"
                           onclick="copyBookings()" />
            <input type="button" id="bConf1" value="Print/Fax/Email" class="buttonStyleNew"
                   onclick="PrintReports('${blFlag}')" style="width:100px"/>
            <c:set var="manualNotesCount" value="buttonStyleNew"/>
            <c:if test="${ManualNotes}">
                <c:set var="manualNotesCount" value="buttonColor"/>
            </c:if>
            <input type="button" class="${manualNotesCount}" id="noteButtonDown" name="search" value="Note" style="width:50px;"
                   onclick="return GB_show('Notes', '${path}/notes.do?moduleId=' + '<%=NotesConstants.FILE%>&moduleRefId=' + '${bookingValues.fileNo}', 300, 700);" />
            <c:choose>
                <c:when test="${null!=TotalScan && TotalScan!='0'}">
                    <input id="scanButtonDown" class="buttonColor" type="button"
                           value="Scan/Attach" onClick="scan('${bookingValues.fileNo}', '${importFlag}')"/>
                </c:when>
                <c:otherwise>
                    <input id="scanButtonDown" class="buttonStyleNew" type="button"
                           value="Scan/Attach" onClick="scan('${bookingValues.fileNo}', '${importFlag}')"/>
                </c:otherwise>
            </c:choose>
            <c:if test="${newBooking == 'edit'}">
                <input type="button" class="buttonStyleNew" id="ReversetoQuote1" value="Reverse to Quote"
                       onclick="convertToQuote('${bookingValues.fileNo}')" />
            </c:if>
            <input type="button" class="buttonStyleNew" value="Hazmat"  id="hazmatButtonDown" onclick="getPopHazmat()"/>
            <input id="inbondButtonDown" type="button" value="Inbond" class="buttonStyleNew"
                   onclick="gotoInbond('${bookingId}', '${fileNo}')" style="width:60px;" />
            <input type="button" id="arRedInvoiceDown" value="AR Invoice" class="buttonStyleNew"
                   onclick="return GB_show('AR Invoice', '${path}/arRedInvoice.do?action=listArInvoice&fileNo=${bookingValues.fileNo}&screenName=BOOKING&fileType=${bookingValues.importFlag}', 550, 1100)"/>
            <c:if test="${loginuser.role.roleDesc == 'Admin'}">
                <input type="button" value="ResendToBlue" id="resend"  class="buttonStyleNew" onclick="resendToBlueScreen('${bookingValues.fileNo}')"/>
            </c:if>
            <input type="button" value="Send Bkg EDI" id="sendEdi1" style="visibility:hidden" class="buttonStyleNew" onclick="generate300Xml()"/>
            <input type="button" value="Cancel Bkg EDI" id="cancelEdi1" style="visibility:hidden" class="buttonStyleNew" onclick="cancel300Xml()"/>
        </td>
    </tr>
</table>
<div id="outOfGaugeCommentDiv" style="display: none; height:150px;width:300px; position: absolute; top:30%; left:40%">
    <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr class="tableHeadingNew" >
            <td align="left">OUT OF GAUGE COMMENT
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td class="textlabelsBold" colspan="2">
                <textarea rows="2" cols="34" id="outOfGaugeComment" style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 60)"
                          onkeyup="limitTextarea(this, 2, 30)" onfocus="focus_watch = setInterval('watchTextarea(outOfGaugeComment)', 250)" onblur="clearInterval(focus_watch)"></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="button" value="submit"  class="buttonStyleNew"  onclick="submitOutOfGuageComments('${QuoteValues.quoteId}')">
                <input type="button" value="cancel"  class="buttonStyleNew"  onclick="closeCommentDiv()">
            </td>
        </tr>
    </table>
               <script>
        </script>
</div>

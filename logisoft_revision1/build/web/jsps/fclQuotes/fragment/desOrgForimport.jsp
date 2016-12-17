<%
String quoteId = (String)request.getAttribute("quoteId");
%>
<td>
    <table  width="100%" cellpadding="3" border="0">
        <tr class="textlabelsBold"><td align="right">Origin</td></tr>
        <tr class="textlabelsBold"><td align="right">Destination</td></tr>
        <tr class="textlabelsBold"><td align="right">ComCode</td></tr>
        <tr class="textlabelsBold">
        <c:choose>
            <c:when test="${bookingValues.comcode!='006100'}">
                <td align="right">Description :&nbsp;</td>
            </c:when>
            <c:otherwise>
                <td>&nbsp;</td>
            </c:otherwise>
        </c:choose>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
</td>
<td>
    <table width="100%" cellpadding="1" border="0">
        <tr class="textlabelsBold"> <td>

                <input name="originTerminal" Class="textlabelsBoldForTextBox mandatory" id="originTerminal" maxlength="80"
                       value="${bookingValues.originTerminal}"  size="20" />
                <div id="originTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="originTerminal_check" type="hidden" value="${bookingValues.originTerminal}" size="22" />
                <script type="text/javascript">
                    initAutocomplete("originTerminal","originTerminal_choices","","originTerminal_check",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&importFlag=true&from=2&isDojo=false","setFocusFromDojo('portOfDischarge')");
                </script></td>
        </tr>
        <tr class="textlabelsBold">
            <td>
                <input name="portOfDischarge" Class="textlabelsBoldForTextBox mandatory" id="portOfDischarge" maxlength="80"
                       value="${bookingValues.portofDischarge}"  size="20"  />
                <input id="portofDischarge_check" type="hidden" value="${bookingValues.portofDischarge}" size="22" />
                <div id="portOfDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("portOfDischarge","portOfDischarge_choices","","portofDischarge_check",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=1&isDojo=false","defaultAgentforcons();");
                </script>
            </td>
        </tr>

        <tr>
            <%if (quoteId != null && quoteId.equals("")) {%>
            <td><span class="textlabelsBold">
                    <%--<html:text  property ="commcode"  maxlength= "7" size="12" value="<%=comcode%>" readonly="true"/>&nbsp;&nbsp;&nbsp;--%>
                    <input name="commcode" Class="BackgrndColorForTextBox" readonly="readonly" id="commcode"  size="20" maxlength="20"
                           value="${bookingValues.comcode}" onkeydown="getComCodeDesc(this.value)"  tabindex="-1" />
                </span>
                <div id="commcode_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("commcode","commcode_choices","","",
                    "${path}/actions/getChargeCode.jsp?tabName=BOOKING&isDojo=false","");
                </script>
            </td>
            <%} else {%>
            <td><span class="textlabelsBold">
                    <input name="commcode" Class="BackgrndColorForTextBox" id="commcode" size="20" maxlength="20"
                           value="${bookingValues.comcode}" onkeydown="getComCodeDesc(this.value)"
                           readonly="readonly" tabindex="-1" />

                    <div id="commcode_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                    <script type="text/javascript">
                        initAutocomplete("commcode","commcode_choices","","",
                        "${path}/actions/getChargeCode.jsp?tabName=BOOKING&isDojo=false","disableAutoFF()");
                    </script>

                </span>
            </td>
            <%}%>
        </tr>
        <tr>
           <c:choose>
            <c:when test="${bookingValues.comcode!='006100'}">
                 <c:choose>
                        <c:when test="${!empty bookingValues.comdesc}">
                            <c:set var="comdesccode" value="${bookingValues.comdesc}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="comdesccode" value="${comdesccode}"/>
                        </c:otherwise>
                    </c:choose>
                <td>
                    <input name="comdesc" Class="BackgrndColorForTextBox" id="comdesc" readonly="readonly"
                           value="${comdesccode}" size="25" onkeydown="getComCode(this.value)" tabindex="-1" />
                    <div id="comdesc_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                    <script type="text/javascript">
                        initAutocomplete("comdesc","comdesc_choices","","",
                        "${path}/actions/getChargeCodeDesc.jsp?tabName=BOOKING&isDojo=false","");
                    </script>
                </td>
            </c:when>
            <c:otherwise>
                <td>&nbsp;</td>
            </c:otherwise>
        </c:choose>
    </tr>
    <tr><td>&nbsp;</td></tr>
</table>
</td>
<td>
    <table width="100%" border="0" cellpadding="3">
        <tr class="textlabelsBold"><td align="right">POL</td></tr>
        <tr class="textlabelsBold"><td align="right">POD</td></tr>
        <tr class="textlabelsBold"><td align="right">Transit Days</td></tr>
        <tr class="textlabelsBold"><td align="right">Issuing TM</td></tr>
        <tr><td>&nbsp;</td></tr>
    </table>
</td>
<td>
    <table  width="100%" border="0" cellpadding="1">
        <tr>
            <td><input name="portOfOrigin" Class="textlabelsBoldForTextBox" id="portOfOrigin" 
                       value="${bookingValues.portofOrgin}"  size="20" maxlength="80"  />
                <div id="portOfOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="portofOrgin_check" type="hidden" value="${bookingValues.portofOrgin}" size="22" />
                <script type="text/javascript">
                    initAutocomplete("portOfOrigin","portOfOrigin_choices","","portofOrgin_check",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=4&isDojo=false","setFocusFromDojo('destination')");
                </script>

            </td>
        </tr>
        <tr>
            <td><input name="destination"  id="destination" value="${bookingValues.destination}"  size="20"
                       onkeyup="getPod()" onkeydown="getAgentforPod(this.value,true)" Class="textlabelsBoldForTextBox"  />
                <input id="destination_check" type="hidden" value="${bookingValues.destination}" size="22" />
                <div id="destination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("destination","destination_choices","","destination_check",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=3&isDojo=false","setFocusFromDojo('noOfDays')");
                </script>

            </td>
        </tr>
        <tr>
            <td>
                <input name="noOfDays" id="noOfDays" Class="textlabelsBoldForTextBox" onchange="numericTextbox(this)" value="${bookingValues.noOfDays}"
                       size="20" />
            </td>
        </tr>
        <tr>
            <td>

                <span class="textlabelsBold">
                    <c:choose>
                        <c:when test="${! empty bookingValues.issuingTerminal}">
                            <c:set var="terminalCode" value="${bookingValues.issuingTerminal}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="terminalCode" value="${userIssuingTerminal}"/>
                        </c:otherwise>
                    </c:choose>
                    <input type="text"  name="issuingTerminal" Class="textlabelsBoldForTextBox mandatory"
                           value="${terminalCode}" id="issuingTerminal" size="20" />
                    <input type="hidden" value="${terminalCode}" id="issuingTerminal_check"/>
                    <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                    <script type="text/javascript">
                        initAutocomplete("issuingTerminal","issuingTerminal_choices","","issuingTerminal_check",
                        "${path}/actions/getTerminalName.jsp?tabName=BOOKING&isDojo=false&importFlag=true","setFocusFromDojo('doorOrigin')");
                    </script>
            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
    </table>
</td>
<td>
    <table width="100%" border="0" cellpadding="3">
        <tr class="textlabelsBold"><td align="right">Door Org</td></tr>
        <tr class="textlabelsBold"><td align="right">Org Zip </td></tr>
        <tr class="textlabelsBold"><td align="right">Door at Dest</td></tr>
        <tr class="textlabelsBold"><td align="right">NVO Move</td></tr>
        <tr class="textlabelsBold"><td align="right">Line Move</td></tr>
    </table>
</td>
<td>
    <table width="100%" border="0" cellpadding="1" cellspacing="3">
        <tr class="textlabelsBold">
              <td>
                <input name="doorDestination"  id="doorDestination"  Class="textlabelsBoldForTextBox"
                       value="${bookingValues.doorDestination}" size="20"  />
                <div id="doorDestination_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("doorDestination","doorDestination_choices","","",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=6&isDojo=false","setFocusFromDojo('moveType')");
                </script>
            </td>
        </tr>
        <tr class="textlabelsBold"><td>
                <input name="zip" id="zip" Class="textlabelsBoldForTextBox"  maxlength="20"
                       value="${bookingValues.zip}" onkeyup="getzip(this)" size="20"  />
            </td>
        </tr>
        <tr>
          <td>
                <input name="doorOrigin"id="doorOrigin"  Class="textlabelsBoldForTextBox"
                       value="${bookingValues.doorOrigin}" size="20"  />
                <div id="doorOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("doorOrigin","doorOrigin_choices","","",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=5&isDojo=false","setFocusFromDojo('zip')");
                </script>
          <c:choose>
              <c:when test="${bookingValues.onCarriage == 'on'}">
                  <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" checked onclick="chkDoorDest();"/>
              </c:when>
              <c:otherwise>
                  <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" onclick="chkDoorDest();"/>
              </c:otherwise>
          </c:choose>
          <c:if test="${not empty bookingValues.onCarriageRemarks}">
              <span class="textlabelsBold" onmouseover="showToolTip('${bookingValues.onCarriageRemarks}',100,event)"  onmouseout="tooltip.hideComments();">
                  <img src="${path}/img/icons/view.gif" id="oncarriage-img" class="remark"/>
              </span>
          </c:if>
            </td>
        </tr>
        <tr>
            <td>
        <html:select property="moveType" styleClass="dropdown_accounting"
                     value="${bookingValues.moveType}" styleId="moveType"  style="width:126px;">
            <html:optionsCollection name="typeOfMoveList" /></html:select>
        <span class="hotspot"
              onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking','60',event);"
              onmouseout="tooltip.hide()" >
            <img src="${path}/img/icons/help-icon.gif" />
        </span>
        <input value="off" type="checkbox" name="rampCheck"  id="rampCheck" style="display: none"/>
</td>
</tr>
<tr>
    <td>
        <div class="mandatory" id="lineMoveDiv" style="float:left">
            <html:select property="lineMove" onkeydown="if(event.keyCode==9){document.getElementById('cancel1').focus()}" styleClass="dropdown_accounting" styleId="lineMove"
                         value="${bookingValues.lineMove}" onchange="greyOutTruckerCheckBox()" style="width:126px;">
                <html:optionsCollection name="typeOfMoveList" /></html:select></div>
        <span class="hotspot"
              onmouseover="tooltip.show('If Line move is NOT from \'DOOR\' then Equipment pickup and return addresses are required.','60',event);"
              onmouseout="tooltip.hide()" >
            <img src="${path}/img/icons/help-icon.gif" />
        </span>
    </td>
</tr>
</table>
</td>

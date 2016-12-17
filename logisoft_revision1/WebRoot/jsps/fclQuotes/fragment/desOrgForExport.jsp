<%
String quoteId = (String)request.getAttribute("quoteId");
%>
<td valign="top">
    <table  width="100%" cellpadding="3" border="0">
        <tr class="textlabelsBold">
            <td align="right">Destination</td>
            <%if (quoteId != null && quoteId.equals("")) {%>
            <td><input  name="portOfDischarge" Class="textlabelsBoldForTextBox mandatory" id="portOfDischarge" readonly="readonly"
                        value="${bookingValues.portofDischarge}"  size="20" maxlength="80"
                        onkeyup="getDestination()" onkeydown="getAgentforDestination(this.value,false)" />
            </td>
            <%} else {%>
            <td>
                <input name="portOfDischarge" Class="BackgrndColorForTextBox mandatory" id="portOfDischarge" maxlength="80"
                       value="${bookingValues.portofDischarge}"  size="20" readonly="readonly" onkeyup="getDestination()" tabindex="-1" />
                <div id="portOfDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("portOfDischarge","portOfDischarge_choices","","",
                    "${path}/actions/getUnlocationCode.jsp?tabName=BOOKING&from=2&isDojo=false","defaultAgentforcons()");
                </script>
            </td>
            <%}%>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Origin</td>
            <%if (quoteId != null && quoteId.equals("")) {%>
            <td><input  name="originTerminal" Class="textlabelsBoldForTextBox mandatory" id="originTerminal" maxlength="80"
                        value="${bookingValues.originTerminal}" readonly="readonly" size="20" />
            </td>
            <%} else {%>
            <td>
                <input name="originTerminal" Class="BackgrndColorForTextBox mandatory" id="originTerminal" maxlength="80"
                       value="${bookingValues.originTerminal}"  size="20" readonly="readonly" tabindex="-1" />
            </td>
            <%}%>

        </tr>
        <tr class="textlabelsBold">
            <td align="right">ComCode</td>
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
        <tr class="textlabelsBold">
        <c:choose>
            <c:when test="${bookingValues.comcode!='006100'}">
                <td align="right" >Description</td>
            </c:when>
            <c:otherwise>
                <td>&nbsp;</td>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${bookingValues.comcode!='006100'}">
                <td>
                    <input name="comdesc" Class="BackgrndColorForTextBox" id="comdesc" readonly="readonly"
                           value="${bookingValues.comdesc}" size="25" onkeydown="getComCode(this.value)" tabindex="-1" />
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

</table>
</td>



<td valign="top">
    <table width="100%" border="0" cellpadding="3">
        <tr class="textlabelsBold">
            <td align="right">POL</td>
            <td><input name="portOfOrigin" Class="textlabelsBoldForTextBox" id="portOfOrigin"
                       value="${bookingValues.portofOrgin}"  size="20" maxlength="80"/>
                <input id="portOfOrigin_check" type="hidden" value="${bookingValues.portofOrgin}" size="22" />
                <div id="portOfOrigin_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("portOfOrigin","portOfOrigin_choices","","portOfOrigin_check",
                    "${path}/actions/getUnlocationCode.jsp?tabName=BOOKING&from=3&isDojo=false","setFocusFromDojo('lineMove'),polChangeBooking()");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">POD</td>
            <td><input name="destination" Class="textlabelsBoldForTextBox" id="destination" value="${bookingValues.destination}"  size="20"
                       onkeyup="getPod()" onkeydown="getAgentforPod(this.value,false)" />
                <input id="destination_check" type="hidden" value="${bookingValues.destination}" size="22" />
                <div id="destination_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocomplete("destination","destination_choices","","destination_check",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=3&isDojo=false","");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Transit Days</td>
            <td>
                <input name="noOfDays" id="noOfDays" Class="BackgrndColorForTextBox" value="${bookingValues.noOfDays}"
                       size="20" tabindex="-1" readonly="readonly" />
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Issuing TM</td>
            <td>

                <span class="textlabelsBold">
                    <input type="text"  name="issuingTerminal" Class="textlabelsBoldForTextBox mandatory"
                           value="${bookingValues.issuingTerminal}" id="issuingTerminal" size="20" />
                    <div id="issuingTerminal_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                    <input type="hidden" value="${bookingValues.issuingTerminal}" id="issuingTerminal_check"/>
                    <script type="text/javascript">
                        initAutocomplete("issuingTerminal","issuingTerminal_choices","","issuingTerminal_check",
                        "${path}/actions/getTerminalName.jsp?tabName=BOOKING&isDojo=false","setFocusFromDojo('doorOrigin')");
                    </script>

            </td>

        </tr>

    </table>
</td>

<td valign="top">
    <table width="100%" border="0" cellpadding="3" >
        <tr class="textlabelsBold">
            <td align="right">Door Org</td>
            <td>
                <input name="doorOrigin" Class="BackgrndColorForTextBox" id="doorOrigin"
                       value="${bookingValues.doorOrigin}" size="20" onchange="selectBookingNVOmove();" readonly="readonly" tabindex="-1" />&nbsp;&nbsp;&nbsp;&nbsp;
                <c:if test="${not empty bookingValues.doorOrigin && (enableIms == 'Y' ||  enableIms == 'y')}">
                    <input type="button" id="imsQuoteImg"  onclick="getEmptyPickUp('${bookingValues.bookingId}','${bookingValues.hazmat}','${bookingValues.fileNo}','${bookingValues.originTerminal}')" class="buttonStyleNew" value="IMS"/>
                </c:if>
            </td>
        </tr>

        
            <c:if test="${(not empty bookingValues.doorOrigin) || (empty bookingValues.doorOrigin)}">
                <tr class="textlabelsBold">
                    <td align="right">Org Zip</td>
                     <td  class="textlabelsBold"  id="originZip">
                <html:text property="zip" onmousedown="if(event.keyCode==9){tabMovErt()}" styleId="zip"  styleClass="textlabelsBoldForTextBox"
                           onkeydown="selectNVOmove()" value="${bookingValues.zip}" size="22" /><!---->
                <input type="hidden" id="zip_check" value="${bookingValues.zip}"/>
                <div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocompleteWithFormClear("zip", "zip_choices", "doorOrigin", "zip_check",
                    "${path}/actions/getZipCode.jsp?tabName=QUOTE&from=1", "updateDoorOrigin('doorOrigin')", "hideImsQuotes('${bookingValues.bookingId}')");
                </script>
                <img src="${path}/img/map.png" id="zipLookUp" align="middle" onmouseover="tooltip.show('<strong>Google Map Search</strong>', null, event);" onmouseout="tooltip.hide();"
                     onclick="getGoogleMap()" />
                    </td>
                </tr>
            </c:if>

       

        <tr class="textlabelsBold">
            <td align="right">Door at Dest</td>
            <td>
                <input name="doorDestination"  id="doorDestination"  Class="textlabelsBoldForTextBox"
                       value="${bookingValues.doorDestination}" onchange="changeBookingNVOMove();" size="20"  />
                <input type="hidden" id="doorDestination_check" value="${bookingValues.doorDestination}"/>
                <div id="doorDestination_choices"   style="display: none;width: 5px;"  class="autocomplete"></div>
                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("doorDestination","doorDestination_choices","","doorDestination_check",
                                                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=6&isDojo=false","","");
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
        <tr class="textlabelsBold">
            <td align="right">NVO Move</td>
            <td>
                <div style="float:left">
                    <html:select property="moveType" styleClass="dropdown_accounting" onchange="setBookingNvoMove();"
                             value="${bookingValues.moveType}" styleId="moveType"  style="width:126px;" >
                    <html:optionsCollection name="typeOfMoveList" /></html:select></div>
                <span class="hotspot"
                      onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking','60',event);"
                      onmouseout="tooltip.hide()" >
                    <img src="${path}/img/icons/help-icon.gif" />
                </span>
	<c:choose>
	    <c:when test="${not empty bookingValues.doorOrigin}">
		    <c:choose>
			<c:when test="${not empty bookingValues.rampCheck && bookingValues.rampCheck=='on'}">
			    <input value="${bookingValues.rampCheck}" type="checkbox" checked name="rampCheck"  id="rampCheck"
				   onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
				   onclick="getTypeofMoveList('${bookingValues.bookingId}');"/>
			</c:when>
			<c:otherwise>
			    <input value="${bookingValues.rampCheck}" type="checkbox" name="rampCheck"  id="rampCheck"
				   onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
				   onclick="getTypeofMoveList('${bookingValues.bookingId}');"/>
			</c:otherwise>
		    </c:choose>
	    </c:when>
	    <c:otherwise>
		<input value="off" type="checkbox" name="rampCheck"  id="rampCheck" style="display: none"/>
	    </c:otherwise>
	</c:choose>
		<c:if test="${not empty bookingValues.doorOrigin}">
		</c:if>
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Line Move</td>
            <td>
                <div class="mandatory" id="lineMoveDiv" style="float:left">
                    <html:select property="lineMove" styleId="lineMove"  styleClass="dropdown_accounting"
                                 value="${bookingValues.lineMove}" onkeydown="if(event.keyCode==9){document.getElementById('cancel1').focus()}" onchange="greyOutTruckerCheckBox(document.getElementById('editbook'));"  style="width:126px;">
                        <html:optionsCollection name="lineMoveList" /></html:select></div>
                <span class="hotspot"
                      onmouseover="tooltip.show('If Line move is NOT from \'DOOR\' then Equipment pickup and return addresses are required.','60',event);"
                      onmouseout="tooltip.hide()" >
                    <img src="${path}/img/icons/help-icon.gif" />
                </span>
            </td>
        </tr>
    </table>
</td>

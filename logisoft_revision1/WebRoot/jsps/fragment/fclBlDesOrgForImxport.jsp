
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr class="textlabelsBold"></tr>
        <tr class="textlabelsBold">
            <td align="right">Origin</td>
            <td>
                <input class="textlabelsBoldForTextBox mandatory" name="terminalName" value="${fclBl.terminal}"  id="terminalName" />
                <div id="terminalName_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="origin_check" type="hidden" value="${fclBl.terminal}"/>
                <script type="text/javascript">
                    initAutocomplete("terminalName","terminalName_choices","","origin_check",
                    "<%=path%>/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=0&importFlag=true&isDojo=false","fillPol()");
                </script>
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Destination</td>
            <td>
                <input class="textlabelsBoldForTextBox mandatory" name="finalDestination"  value="${fclBl.finalDestination}"  id="finalDestination"  />
                <div id="finalDestination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="destination_check" type="hidden" value="${fclBl.finalDestination}"/>
                <script type="text/javascript">
                    initAutocomplete("finalDestination","finalDestination_choices","","destination_check",
                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","fillPod()");
                </script>
            </td>
        </tr>

        <tr  class="textlabelsBold">
            <td align="right" >ComCode</td>
            <td>
                <input name="commodityCode" Class="BackgrndColorForTextBox" id="commodityCode" size="20"
                       value="${fclBl.commodityCode}"
                       readonly="readonly" tabindex="-1" /></td>
        <div id="commodityCode_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
        <input id="commodity_check" type="hidden" value="${fclBl.commodityCode}"/>
        <script type="text/javascript">
            initAutocomplete("commodityCode","commodityCode_choices","","commodity_check",
            "<%=path%>/actions/getChargeCode.jsp?tabName=FCLBL&isDojo=false","setComCode()");
        </script>
    </tr>
    <tr class="textlabelsBold">
    <c:choose>
        <c:when test="${fclBl.commodityCode!='006100'}">
            <td align="right">Description &nbsp;</td>
            <td>
                <input name="commodityDesc" Class="BackgrndColorForTextBox" id="commodityDesc"  value="${fclBl.commodityDesc}" size="20" readonly="readonly" tabindex="-1" />
            </td>
        </c:when>
        <c:otherwise>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </c:otherwise>
    </c:choose>
</tr>
<tr class="textlabelsBold">
    <td align="right" >
        Point and Country of Origin</td>
    <td>
<html:text property="country" styleClass="textlabelsBoldForTextBox"
           style="text-transform: uppercase"  styleId="pointAndCountryOfOrigin" maxlength="20"
           value="${fclBl.routedByAgentCountry}"></html:text>
</td>
</tr>
</table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="5">
        <tr class="textlabelsBold"><td align="right">POL</td></tr>
        <tr class="textlabelsBold"><td align="right">POD</td></tr>
        <tr class="textlabelsBold"><td align="right">Transit Days</td></tr>
        <tr class="textlabelsBold"><td align="right">Issuing TM</td></tr>
        <tr><td>&nbsp;</td></tr>
    </table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr class="textlabelsBold">
            <td>
                <input  class="textlabelsBoldForTextBox" name="portofladding" value="${fclBl.portOfLoading}"
                        id="portofladding"  />
                <div id="portofladding_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="portOfLoading_check" type="hidden" value="${fclBl.portOfLoading}"/>
                <script type="text/javascript">
                    initAutocomplete("portofladding","portofladding_choices","","portOfLoading_check",
                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","setFocusFromDojo('portofdischarge')");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td>
                <input  class="textlabelsBoldForTextBox" name="portofdischarge" onkeydown="fillClause()"
                        value="${fclBl.portofDischarge}" id="portofdischarge"  />
                <div id="portofdischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                <input id="portofDischarge_check" type="hidden" value="${fclBl.portofDischarge}"/>
                <script type="text/javascript">
                    initAutocomplete("portofdischarge","portofdischarge_choices","","portofDischarge_check",
                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","setFocusFromDojo('noOfDays')");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td>
                <input name="noOfDays" id="noOfDays" Class="textlabelsBoldForTextBox" onchange="numericTextbox(this)" value="${fclBl.noOfDays}" size="2" />
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td>
        <c:choose>
            <c:when test="${! empty fclBl.billingTerminal}">
                <c:set var="terminalCode" value="${fclBl.billingTerminal}"/>
            </c:when>
            <c:otherwise>
                <c:set var="terminalCode" value="${userIssuingTerminal}"/>
            </c:otherwise>
        </c:choose>
        <input  class="textlabelsBoldForTextBox mandatory" name="billingTerminal" value="${terminalCode}"
                id="billingTerminal" />
        <input name="billing_Terminal_check" id="billing_Terminal_check"  type="hidden" value="${terminalCode}">
        <div id="billingTerminal_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
        <script type="text/javascript">
            initAutocompleteWithFormClear("billingTerminal","billingTerminal_choices","","billing_Terminal_check",
            "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false&importFlag=true","getTerminalAddress()","");
        </script>
</td> 
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="4">
        <tr class="textlabelsBold"><td align="right">Door Org</td></tr>
        <tr class="textlabelsBold">
            <td align="right">Door Dest Zip/City </td>
            <%--<%if (!doorOfOrigin.equals("")) {%>
            <td align="right">Org Zip </td>
            <%} else {%>
            <td align="right">&nbsp;</td>
            <%}%>
            --%></tr>
        <tr class="textlabelsBold"><td align="right">Door at Dest</td></tr>
        <tr class="textlabelsBold"><td align="right">NVO Move</td></tr>
        <tr class="textlabelsBold">
            <td align="right" valign="top">Line Move</td>
        </tr>
    </table>
</td>
<td valign="top">
    <table border="0" class="tableBorderNew" width="100%" cellspacing="0" cellpadding="2"
           style="border-right-width:1px;border-right-color:grey;border-top-width:0px;
           border-bottom-width:0px;border-left-width: 0px;">
        <tr class="textlabelsBold">
            <td style="padding-right: 5px;">
        <html:text property="doorOfDestination"  styleClass="textlabelsBoldForTextBox" value="<%=doorOfDestination%>" styleId="doorOfDestination"/>
        <div id="doorOfDestination_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
        <script type="text/javascript">
            initAutocomplete("doorOfDestination","doorOfDestination_choices","","",
            "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=6&isDojo=false","setFocusFromDojo('moveType')");
        </script>
</td>
</tr>

<tr class="textlabelsBold">
    <td id="originZip">
<html:text property="zip" styleClass="textlabelsBoldForTextBox"  value="${fclBl.zip}" styleId="zip"
           onkeyup="getzip(this)" style="width:125px" />
<input type="hidden" id="zip_check" value="${fclBl.zip}"/>
<div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
<script type="text/javascript">
    initAutocompleteWithFormClear("zip", "zip_choices", "doorOfOrigin", "zip_check",
    "${path}/actions/getZipCode.jsp?tabName=FCL_BL&from=1", "updateDoorOrigin('doorOfOrigin')", "");
</script>
</td>
</tr>


<tr class="textlabelsBold">
    <td>
<html:text property="doorOfOrigin"   value="<%=doorOfOrigin%>"
           styleClass="textlabelsBoldForTextBox" styleId="doorOfOrigin"/>
<input type="hidden" id="doorOrigin_check" value="<%=doorOfOrigin%>"/>
<div id="doorOfOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
<script type="text/javascript">
            initAutocomplete("doorOfOrigin","doorOfOrigin_choices","","doorOrigin_check",
            "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=5&isDojo=false","setFocusFromDojo('zip')", "hideOrgZip();");
</script>
<input type="button" class="buttonStyleNew" style="float: right;" id="doorDelivery" value="Door Delivery"
                         onclick="showDoorDeliveryInfo('${path}', '${fclBl.bol}', '${fclBl.fileNo}', 'Door Delivery Information');"/>
</td>
</tr>
<tr class="textlabelsBold">
    <td>
<html:select property="moveType"  styleId="moveType"  value="${fclBl.moveType}"
             style="width:125px;" styleClass="dropdown_accounting">
    <html:optionsCollection name="typeOfMoveList" /></html:select>
</td>
</tr>
<tr class="textlabelsBold">
    <td>
<html:select property="lineMove"  styleId="lineMove" value="${fclBl.lineMove}"
             style="width:125px;" styleClass="dropdown_accounting">
    <html:optionsCollection name="typeOfMoveList" /></html:select>
</td>
</tr>
</table>
</td>
<td valign="top">
    <table   border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr class="textlabelsBold">
            <td align="right" >Default Agent</td>
            <td>
        <html:radio property="defaultAgent" onclick="fillDefaultAgent();" styleId="defaultAgentY"
                    value="Y" name="fclBillLaddingform"/>Yes
        <html:radio property="defaultAgent" onclick="clearValues();" styleId="defaultAgentN"
                    value="N"  name="fclBillLaddingform"/>No
</td>
</tr>
<tr class="textlabelsBold">
    <td align="right">Direct Consignment&nbsp;</td>
    <td>
<html:radio property="directConsignment" styleId="directConsignmentY" onclick="directConsignCheck();"
            value="on" name="fclBillLaddingform" tabindex="-1"/>Yes
<html:radio property="directConsignment" styleId="directConsignmentN" onclick="directConsignCheckNo();"
            value="off"  name="fclBillLaddingform" tabindex="-1"/>No
</td>
</tr>
<tr class="textlabelsBold">
    <td align="right">Agent Name</td>
    <td>
<html:text property="agent" styleClass="textlabelsBoldForTextBox" value="${fclBl.agent}"
           styleId="agent" onkeydown="setDojoAction()"/>
<input Class="textlabelsBoldForTextBox"  name="agent_check" id="agent_check" value="${fclBl.agent}" type="hidden" />
<div id="agent_choices" style="display: none" class="autocomplete"></div>
<script type="text/javascript">
    initAutocompleteWithFormClear("agent","agent_choices","agentNo","agent_check",
    "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=1&importFlag=true","checkDisabledAgent()");
</script>

</td>
</tr>
<tr class="textlabelsBold">
    <td align="right">Agent Number</td>

    <td>
<html:text property="agentNo" styleId="agentNo" styleClass="textlabelsBoldForTextBox" readonly="true"
           value="${fclBl.agentNo}" ></html:text>
</td>
</tr>
<tr class="textlabelsBold">
    <td align="right" >ERT</td>

    <td>
        <div class="textlabelsBold" style="float: left">
            <c:choose>
                <c:when test="${ empty fclBl.bol}">
                    <html:select property="routedAgentCheck" styleId="routedAgentCheck" value="no" style="width:130px;" styleClass="dropdown_accounting" onchange="setDefaultRouteAgent()">
                        <html:option value="">Select</html:option>
                        <html:option value="yes">Yes</html:option>
                        <html:option value="no">No</html:option>
                    </html:select>
                </c:when>
                <c:otherwise>
                    <html:select property="routedAgentCheck" styleId="routedAgentCheck" value="${fclBl.routedAgentCheck}" style="width:130px;" styleClass="dropdown_accounting" onchange="setDefaultRouteAgent()">
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
    <td align="right" >Routed By Agent </td>
    <td valign="top">
<c:choose>
    <c:when test="${fclBl.readyToPost=='M' && roleDuty.allowRoutedAgent}">
        <input type="text" name="routedByAgent" id="routedByAgent" value="${fclBl.routedByAgent}"
               size="20" class="textlabelsBoldForTextBox" maxlength="50"/>
    </c:when>
    <c:otherwise>
        <input type="text" name="routedByAgent" id="routedByAgent" value="${fclBl.routedByAgent}"
               size="20" class="textlabelsBoldForTextBox" maxlength="50"   tabindex="-1"/>
    </c:otherwise>
</c:choose>
<input Class="textlabelsBoldForTextBox"  name="routedByAgent_action"
       id="routedByAgent_action" type="hidden" value="${fclBl.routedByAgent}" />
<input name="RoutedDup" id="RoutedDup" type="text" style="display: none;"/>
<div id="routedByAgent_choices" style="display: none" class="autocomplete"></div>
<script type="text/javascript">
    initAutocompleteWithFormClear("routedByAgent","routedByAgent_choices","RoutedDup","routedByAgent_action",
    "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=2","checkDisabledRouted()");
</script>
</td>
</tr>
</table>
</td>


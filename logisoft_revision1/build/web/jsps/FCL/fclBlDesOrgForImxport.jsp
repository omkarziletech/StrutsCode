
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr class="textlabelsBold"></tr>
        <tr class="textlabelsBold">
            <td align="right">Origin</td>
            <td>
                <html:text styleClass="textlabelsBoldForTextBox mandatory" property="fclBl.terminal"   styleId="terminalName" />
                <div id="terminalName_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                <script type="text/javascript">
                    initOPSAutocomplete("terminalName","terminalName_choices","","",
                    "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","fillPol()");
                </script>
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td align="right">Destination</td>
            <td>
                <html:text styleClass="textlabelsBoldForTextBox mandatory" property="fclBl.port"   styleId="finalDestination"  />
                <div id="finalDestination_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                <script type="text/javascript">
                    initOPSAutocomplete("finalDestination","finalDestination_choices","","",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","fillPod()");
                </script>
            </td>
        </tr>

        <tr  class="textlabelsBold">
            <td align="right" >ComCode</td>
            <td>
                <html:text property="fclBl.commodityCode" styleClass="textlabelsBoldForTextBox" styleId="commodityCode" size="20"
                        /></td>
        <div id="commodityCode_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
        <script type="text/javascript">
            initAutocomplete("commodityCode","commodityCode_choices","commodityDesc","",
            "${path}/actions/getChargeCode.jsp?tabName=FCLBL&isDojo=false","setComCode()");
        </script>
    </tr>
    <tr class="textlabelsBold">
    <c:choose>
        <c:when test="${fclBl.commodityCode!='006100'}">
            <td align="right">Description &nbsp;</td>
            <td>
                <input name="fclBl.commodityDesc" Class="textlabelsBoldForTextBox" id="commodityDesc"  size="20" readonly="readonly" tabindex="-1" />
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
<html:text property="fclBl.routedbyAgentsCountry" styleClass="textlabelsBoldForTextBox"
           style="text-transform: uppercase"  styleId="pointAndCountryOfOrigin" maxlength="20"></html:text>
</td>
</tr>
</table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="3">
        <tr class="textlabelsBold"><td align="right">POL</td></tr>
        <tr class="textlabelsBold"><td align="right">POD</td></tr>
        <tr class="textlabelsBold"><td align="right">Transit Days</td></tr>
        <tr class="textlabelsBold"><td align="right">Issuing </td></tr>
        <tr><td>&nbsp;</td></tr>
    </table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="2">
        <tr class="textlabelsBold">
            <td>
                <html:text  styleClass="textlabelsBoldForTextBox" property="fclBl.portofLoading"
                        styleId="portofladding"  />
                <div id="portofladding_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                <script type="text/javascript">
                    initOPSAutocomplete("portofladding","portofladding_choices","","",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","setFocusFromDojo('portofdischarge')");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td>
                <html:text  styleClass="textlabelsBoldForTextBox" property="fclBl.portofDischarge" onkeydown="fillClause()"
                         styleId="portofdischarge"  />
                <div id="portofdischarge_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                <script type="text/javascript">
                    initOPSAutocomplete("portofdischarge","portofdischarge_choices","","",
                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","setFocusFromDojo('noOfDays')");
                </script>

            </td>
        </tr>
        <tr class="textlabelsBold">
            <td>
        <html:text property="fclBl.noDays"   styleClass="textlabelsBoldForTextBox"   size="2" styleId="noOfDays"/>
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
        <html:text  styleClass="textlabelsBoldForTextBox mandatory" property="fclBl.billingTerminal"
                styleId="billingTerminal" />
        <input name="billing_Terminal_check" id="billing_Terminal_check"  type="hidden" value="${terminalCode}">
        <div id="billingTerminal_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
        <script type="text/javascript">
            initAutocompleteWithFormClear("billingTerminal","billingTerminal_choices","","billing_Terminal_check",
            "${path}/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false&importFlag=true","getTerminalAddress()","");
                                            </script>
                                        </td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</td>
<td valign="top">
    <table  border="0" width="100%" cellspacing="0" cellpadding="3">
        <tr class="textlabelsBold"><td align="right">Door Org</td></tr>
        <tr class="textlabelsBold">
            <td align="right">Org Zip </td>
            </tr>
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
            <td>
        <html:text property="fclBl.doorOfOrigin"
                   styleClass="textlabelsBoldForTextBox" styleId="doorOfOrigin"/>
        <div id="doorOfOrigin_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
        <script type="text/javascript">
                   initOPSAutocomplete("doorOfOrigin","doorOfOrigin_choices","","",
                   "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=5&isDojo=false","setFocusFromDojo('zip')");
        </script>
</td>
</tr>
<tr class="textlabelsBold">
    <td id="originZip">
<html:text property="fclBl.zip" styleClass="textlabelsBoldForTextBox"   styleId="zip"
           onkeyup="getzip(this)" style="width:125px" />
</td>
</tr>
<tr class="textlabelsBold">
    <td style="padding-right: 5px;">
<html:text property="fclBl.doorOfDestination"  styleClass="textlabelsBoldForTextBox"/>
<div id="doorOfDestination_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
<script type="text/javascript">
        initOPSAutocomplete("doorOfDestination","doorOfDestination_choices","","",
        "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=6&isDojo=false","setFocusFromDojo('moveType')");
</script>
</td>
</tr>
<tr class="textlabelsBold">
    <td>
<html:select property="fclBl.moveType"  styleId="moveType"
             style="width:125px;" styleClass="dropdown_accounting">
    <html:option value="0">Select One</html:option>
</html:select>
</td>
</tr>
<tr class="textlabelsBold">
    <td>
<html:select property="fclBl.lineMove"  styleId="lineMove"
             style="width:125px;" styleClass="dropdown_accounting">
    <html:option value="0">Select One</html:option>
</html:select>
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
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentY" onclick="directConsignCheck()"
                                            value="on" name="fclBillLaddingform" tabindex="-1"/>Yes
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentN" onclick="directConsignCheckNo()"
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
        "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=1","checkDisabledAgent()");
    </script>
 
    </td>
    </tr>
    <tr class="textlabelsBold">
        <td align="right">Agent Numbe</td>
          <td class="textlabelsBold">

             <html:text property="agentNo" styleId="agentNo" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"
               value="${fclBl.agentNo}" tabindex="-1"></html:text>
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
               size="20" class="textlabelsBoldForTextBoxDisabledLook" maxlength="50" readonly="readonly"  tabindex="-1"/>
        </c:otherwise>
     </c:choose>
        <input Class="textlabelsBoldForTextBox"  name="routedByAgent_action"
               id="routedByAgent_action" type="hidden" value="${fclBl.routedByAgent}" />
        <input type="text" style="display: none;" name="RoutedDup">
        <div id="routedByAgent_choices" style="display: none" class="autocomplete"></div>
        <script type="text/javascript">
            initAutocompleteWithFormClear("routedByAgent","routedByAgent_choices","RoutedDup","routedByAgent_action",
            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=2","checkDisabledRouted()");
        </script>
        <input name="RoutedDup" id="RoutedDup" type="text" style="display: none;"/>
    </td>
 </tr>
  </table>
</td>

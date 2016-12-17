<td valign="top">
                                <table  border="0" width="100%" cellspacing="0" cellpadding="3">
                                    <tr class="textlabelsBold"><td align="right">Destination</td></tr>
                                    <tr class="textlabelsBold"><td align="right">Origin</td></tr>
                                    <tr class="textlabelsBold"><td align="right" >ComCode</td></tr>
                                    <tr class="textlabelsBold">
                                        <c:choose>
                                            <c:when test="${fclBl.commodityCode!='006100'}">
                                                <td align="right" >Description &nbsp;</td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>&nbsp;</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr class="textlabelsBold"><td align="right" >
                                    Point and Country of Origin</td></tr>
                                </table>
                            </td>
                            <td valign="top">
                                <table  border="0" width="100%" cellspacing="0" cellpadding="2">
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text styleClass="BackgrndColorForTextBox" property="fclBl.port" styleId="finalDestination" readonly="true" tabindex="-1"/>
                                            <div id="finalDestination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                  initOPSAutocomplete("finalDestination","finalDestination_choices","","",
                                                "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text styleClass="BackgrndColorForTextBox" property="fclBl.terminal" styleId="terminalName" readonly="true" tabindex="-1"/>
                                            <div id="terminalName_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initOPSAutocomplete("terminalName","terminalName_choices","","",
                                                "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr  class="textlabelsBold">
                                        <td>
                                            <html:text styleClass="BackgrndColorForTextBox" property="fclBl.commodityCode" styleId="commodityCode" size="20" readonly="true" tabindex="-1"/>
                                    </tr>
                                    <tr>
                                        <c:choose>
                                            <c:when test="${fclBl.commodityCode!='006100'}">
                                                <td>
                                                    <html:text styleClass="BackgrndColorForTextBox" property="fclBl.commodityDesc" styleId="commodityDesc" size="25" readonly="true" tabindex="-1"/>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>&nbsp;</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text property="fclBl.routedbyAgentsCountry" styleClass="textlabelsBoldForTextBox"
                                             style="text-transform: uppercase"  styleId="pointAndCountryOfOrigin" maxlength="20"
                                                       ></html:text>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td valign="top">
                                <table  border="0" width="100%" cellspacing="0" cellpadding="3">
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
                                            <html:text styleClass="textlabelsBoldForTextBox" property="fclBl.portofLoading" styleId="portofladding"/>
                                            <input id="portOfLoading_check" type="hidden" value="${fclBlForm.fclBl.portofLoading}"/>
                                            <div id="portofladding_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                    initAutocompleteWithFormClear("portofladding","portofladding_choices","","portOfLoading_check",
                                                    "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","","");
                                            </script>

                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text styleClass="textlabelsBoldForTextBox" property="fclBl.portofDischarge" styleId="portofdischarge"/>
                                            <input id="portofDischarge_check" type="hidden" value="${fclBlForm.fclBl.portofDischarge}"/>
                                            <div id="portofdischarge_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                            <script type="text/javascript">
                            					initAutocompleteWithFormClear("portofdischarge","portofdischarge_choices","","portofDischarge_check",
                            					"${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","","");
                                            </script>

                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text property="fclBl.noDays"   styleClass="BackgrndColorForTextBox" size="2" styleId="noOfDays" readonly="true"/>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text styleClass="textlabelsBoldForTextBox" property="fclBl.billingTerminal" styleId="billingTerminal" maxlength="20"/>
                                            <input name="billing_Terminal_check" id="billing_Terminal_check"  type="hidden" value="${fclBlForm.fclBl.billingTerminal}">
                                            <div id="billingTerminal_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                            <script type="text/javascript">
                            					initAutocompleteWithFormClear("billingTerminal","billingTerminal_choices","","billing_Terminal_check",
                            					"${path}/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false","fillDomesticRouting()","");
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
                                        <td align="right">
                                            <c:choose>
                                                <c:when test="${not empty fclBlForm.fclBl.doorOfOrigin}">
                                                    Org Zip
                                                </c:when>
                                                <c:otherwise>
                                                    &nbsp;
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold"><td align="right">Door at Dest</td></tr>
                                    <tr class="textlabelsBold"><td align="right">NVO Move</td></tr>
                                    <tr class="textlabelsBold"><td align="right" valign="top">Line Move</td></tr>
                                </table>
                            </td>
                            <td valign="top">
                                <table border="0" class="tableBorderNew" width="100%" cellspacing="0" cellpadding="2"
                                       style="border-right-width:1px;border-right-color:grey;border-top-width:0px;
                                       border-bottom-width:0px;border-left-width: 0px;">
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text property="fclBl.doorOfOrigin"
                                                       styleClass="BackgrndColorForTextBox" readonly="true" tabindex="-1"/>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td id="originZip">
                                            <c:choose>
                                                <c:when test="${not empty fclBlForm.fclBl.doorOfOrigin}">
                                                    <html:text property="fclBl.zip" styleClass="BackgrndColorForTextBox"
                                                               onkeyup="getzip(this)" style="width:125px" readonly="true" tabindex="-1"/>
                                                </c:when>
                                                <c:otherwise>
                                                    &nbsp;
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td style="padding-right: 5px;">
                                            <html:text property="fclBl.doorOfDestination" styleId="doorOfDestination" styleClass="textlabelsBoldForTextBox"
                                                    size="20"/>
                                            <div id="doorOfDestination_choices" style="display: none;width: 5px;" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initOPSAutocomplete("doorOfDestination","doorOfDestination_choices","","",
							"${path}/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=6&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:select property="fclBl.moveType" styleId="moveType" disabled="true"
                                                         style="width:125px;" styleClass="textlabelsBoldForTextBox dropdown_accounting" tabindex="-1">
                                                         <html:optionsCollection name="fclBlForm" property="moveTypeList" />
                                            </html:select>
                                    <c:if test="${fclBlForm.fclBl.rampCheck=='on'}">
                                        <html:checkbox property="fclBl.rampCheck" value="${fclBlForm.fclBl.rampCheck}" styleId="rampCheck"  disabled="true"/>
                                    </c:if>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:select property="fclBl.lineMove"   styleId="lineMove"
                                                         style="width:125px;" styleClass="textlabelsBoldForTextBox dropdown_accounting">
                                                <html:optionsCollection name="fclBlForm" property="lineMoveList"  />
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
                                            <html:radio property="fclBl.defaultAgent" name="fclBlForm" value="Y" styleId="defaultAgentY" onclick="autoFillDefaultAgent()"/>Yes
                                            <html:radio property="fclBl.defaultAgent" name="fclBlForm" value="N" styleId="defaultAgentN" onclick="clearAgentValues()"/>No
                                        </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td align="right">Direct Consignment&nbsp;</td>
                                                <td>
                                            <html:radio property="fclBl.directconsignCheck" styleId="directConsignmentY" onclick="directConsign();"
                                                        value="on" name="fclBlForm" tabindex="-1"/>Yes
                                            <html:radio property="fclBl.directconsignCheck" styleId="directConsignmentN" onclick="directConsignNo();"
                                                        value="off"  name="fclBlForm" tabindex="-1"/>No
                                            </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td align="right">Agent Name</td>
                                                <td>
    <c:choose>
        <c:when test="${fclBlForm.fclBl.defaultAgent == 'Y' || fclBlForm.fclBl.directconsignCheck == 'on'}">
            <html:text property="fclBl.agent" styleClass="BackgrndColorForTextBox"
                       styleId="agent" readonly="true" tabindex="-1" onkeydown="setAgentDojoAction()"/>
        </c:when>
        <c:otherwise>
            <html:text property="fclBl.agent" styleClass="textlabelsBoldForTextBox"
                       styleId="agent" onkeydown="setAgentDojoAction()"/>
        </c:otherwise>
    </c:choose>
    <input Class="textlabelsBoldForTextBox"  name="agent_check" id="agent_check" type="hidden" value="${fclBlForm.fclBl.agent}"/>
    <div id="agent_choices" style="display: none" class="autocomplete"></div>
    <script type="text/javascript">
            initAutocompleteWithFormClear("agent","agent_choices","agentNo","agent_check",
            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=1&importFlag=false","checkDisableAgent()");
    </script>
   
</td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right">Agent Number</td>
                                        <td>
                                    <html:text property="fclBl.agentNo" styleId="agentNo" styleClass="BackgrndColorForTextBox" readonly="true" tabindex="-1"></html:text>
                                            </td>
                                            </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right" >ERT</td>
                                        <td>
        <div class="mandatory" id="routedAgentCheckDiv" style="float: left">
            <c:choose>
                <c:when test="${fclBlForm.fclBl.directconsignCheck == 'on' && fclBlForm.fclBl.defaultAgent == 'N'}">
                    <html:select property="fclBl.routedAgentcheck" styleId="routedAgentCheck"  style="width:130px;" styleClass="dropdown_accountingDisabled" onchange="setDefaultRouteAgent()" disabled="true">
                        <html:option value="">Select</html:option>
                        <html:option value="yes">Yes</html:option>
                        <html:option value="no">No</html:option>
                    </html:select>
                </c:when>
                <c:otherwise>
                    <html:select property="fclBl.routedAgentcheck" styleId="routedAgentCheck"  style="width:130px;" styleClass="dropdown_accounting" onchange="setDefaultRouteAgent()">
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
                                        <td >
                <c:choose>
                    <c:when test="${fclBlForm.fclBl.readyToPost=='M' && roleDuty.allowRoutedAgent}">
                        <html:text property="fclBl.routedByAgent" styleId="routedByAgent"
                                   size="20" styleClass="textlabelsBoldForTextBox" maxlength="50"/>
                    </c:when>
                    <c:when test="${fclBlForm.fclBl.routedAgentcheck == 'no' && fclBlForm.fclBl.readyToPost != 'M'}">
                        <html:text property="fclBl.routedByAgent" styleId="routedByAgent"
                                   size="20" styleClass="textlabelsBoldForTextBox" maxlength="50"/>
                    </c:when>
                    <c:when test="${fclBlForm.fclBl.readyToPost=='M' || fclBlForm.fclBl.directconsignCheck == 'on'}">
                        <html:text property="fclBl.routedByAgent" styleId="routedByAgent"
                                   size="20" styleClass="BackgrndColorForTextBox" maxlength="50" readonly="true"  tabindex="-1"/>
                    </c:when>
                    <c:otherwise>
                        <html:text property="fclBl.routedByAgent" styleId="routedByAgent"
                                   size="20" styleClass="textlabelsBoldForTextBox" maxlength="50"/>
                    </c:otherwise>
                </c:choose>
                <input Class="textlabelsBoldForTextBox"  name="routedByAgent_action"
                       id="routedByAgent_action" type="hidden" value="${fclBlForm.fclBl.routedByAgent}"/>
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

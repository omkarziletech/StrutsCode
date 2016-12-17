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
                                            <input class="BackgrndColorForTextBox" name="finalDestination"
                                                   value="${fclBl.finalDestination}"  id="finalDestination"   readonly="readonly" tabindex="-1" />
                                            <div id="finalDestination_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                  initOPSAutocomplete("finalDestination","finalDestination_choices","","",
                                                "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <input class="BackgrndColorForTextBox" name="terminalName" value="${fclBl.terminal}"
                                                   id="terminalName"  readonly="readonly" tabindex="-1" />
                                            <div id="terminalName_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initOPSAutocomplete("terminalName","terminalName_choices","","",
                                                "<%=path%>/actions/getUnlocationCode.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr  class="textlabelsBold">
                                        <td>
                                            <input name="commodityCode" Class="BackgrndColorForTextBox" id="commodityCode" size="20"
                                               value="${fclBl.commodityCode}"  readonly="readonly" tabindex="-1" /></td>
                                    </tr>
                                    <tr>
                                        <c:choose>
                                            <c:when test="${fclBl.commodityCode!='006100'}">
                                                <td>
                                                    <input name="commodityDesc" Class="BackgrndColorForTextBox" 
                                                           id="commodityDesc" readonly="readonly"
                                                           value="${fclBl.commodityDesc}" size="25" />
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>&nbsp;</td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text property="country" styleClass="textlabelsBoldForTextBox" 
                                             style="text-transform: uppercase"  styleId="pointAndCountryOfOrigin" maxlength="20"
                                                       value="${fclBl.routedByAgentCountry}"></html:text>
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
                                            <input  class="textlabelsBoldForTextBox" name="portofladding" value="${fclBl.portOfLoading}"
                                                    id="portofladding"/>
                                            <input id="portOfLoading_check" type="hidden" value="${fclBl.portOfLoading}" />
                                            <div id="portofladding_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                            					initAutocompleteWithFormClear("portofladding","portofladding_choices","","portOfLoading_check",
                            					"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false","","");
                                            </script>

                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <input  class="textlabelsBoldForTextBox" name="portofdischarge" onkeydown="fillClause()"
                                                    value="${fclBl.portofDischarge}" id="portofdischarge"  />
                                            <div id="portofdischarge_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                            					initOPSAutocomplete("portofdischarge","portofdischarge_choices","","",
                            					"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","");
                                            </script>

                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:text property="noOfDays" readonly="true"  styleClass="BackgrndColorForTextBox"
                                                value="${fclBl.noOfDays}" size="2" tabindex="-1" />
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <input  class="textlabelsBoldForTextBox" name="billingTerminal" value="${fclBl.billingTerminal}" maxlength="20"
                                                    id="billingTerminal" />
                                            <input name="billing_Terminal_check" id="billing_Terminal_check"  type="hidden">
                                            <div id="billingTerminal_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                            <script type="text/javascript">
                            					initAutocompleteWithFormClear("billingTerminal","billingTerminal_choices","","",
                            					"<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false","updateDomesticRouting()","");
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
                                        <%if (!doorOfOrigin.equals("")) {%>
                                        <td align="right">Org Zip </td>
                                        <%} else {%>
                                        <td align="right">&nbsp;</td>
                                        <%}%>
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
                                            <html:text property="doorOfOrigin" readonly="true"  value="<%=doorOfOrigin %>"
                                                       styleClass="BackgrndColorForTextBox" tabindex="-1" />
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td id="originZip">
                                            <%if (!doorOfOrigin.equals("")) {%>
                                            <html:text property="zip" styleClass="BackgrndColorForTextBox"  value="${fclBl.zip}"
                                                       onkeyup="getzip(this)" style="width:125px" readonly="true" tabindex="-1" />
                                            <%}else{%>
                                            &nbsp;
                                            <%}%>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td style="padding-right: 5px;">
                                            <input name="doorOfDestination" id="doorOfDestination" class="textlabelsBoldForTextBox"
                                                   value="${fclBl.doorOfDestination}" size="20"/>
                                            <div id="doorOfDestination_choices" style="display: none;width: 5px;" align="right" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                initOPSAutocomplete("doorOfDestination","doorOfDestination_choices","","",
							"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=6&isDojo=false","");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:select property="moveType" disabled="true" styleId="moveType"  value="${fclBl.moveType}"
                                                         style="width:125px;" styleClass="BackgrndColorForTextBox dropdown_accounting" tabindex="-1">
                                            <html:optionsCollection name="typeOfMoveList" /></html:select>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>
                                            <html:select property="lineMove"   styleId="lineMove" value="${fclBl.lineMove}"
                                                         style="width:125px;" styleClass="textlabelsBoldForTextBox dropdown_accounting">
                                            <html:optionsCollection name="typeOfMoveList" /></html:select>
                                        </td>
                                    </tr>
                                </table>
                            </td>

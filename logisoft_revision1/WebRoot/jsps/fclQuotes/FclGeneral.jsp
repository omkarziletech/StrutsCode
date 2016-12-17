<table border="0" cellpadding="0" cellspacing="0" width="100%" >
                        <tr>
                            <td valign="top">
                                <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                                    <tr class="tableHeadingNew">
                                        <td colspan="2">Release Clause</td>
                                        <td>Special Provisions</td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold">Release Clause</td>
                                        <td>
                                            <input  class="textlabelsBoldForTextBox" name="blClause" id="blClause"
                                                value="${fclBl.fclBLClause}" size="5"/>
                                            <div id="blClause_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                        </td>
                                        <td class="textlabelsBold">
                                          <c:if test="${! empty addchargeslist}">
                                            Insurance &nbsp;
                                            <html:radio property="insurance" value="Y" name="transactionBean" styleId="insuranceYes" onclick="addInsurance()"></html:radio> Yes
                                            <html:radio property="insurance" value="N" name="transactionBean" styleId="insuranceNo" onclick="deleteInsurance()"></html:radio> No &nbsp;&nbsp;
                                          </c:if>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td valign="top">Description</td>
                                        <td>
                                    <html:textarea styleId="clauseDescription" styleClass="BackgrndColorForTextBox" readonly="true" tabindex="-1"
                                                   cols="55" rows="3" value="${fclBl.clauseDescription}" onkeypress="return checkTextAreaLimit(this, 500)" property="clauseDescription"  ></html:textarea>
                                    <script type="text/javascript">
                                              initOPSAutocomplete("blClause","blClause_choices","clauseDescription","",
                                              "${path}/actions/getChargeCode.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","");
                                    </script>
                                </td>
                                <td class="textlabelsBold">
                                <c:if test="${! empty addchargeslist}">Cost of Goods 
                                    <fmt:formatNumber pattern="##,###,##0" var="costOfGoods" value="${fclBl.costOfGoods}" />
                                    <input type="text" value="${costOfGoods}" class="BackgrndColorForTextBox" readonly="true" tabindex="-1" size="7"/>
                                </c:if>
                                </td>
                                </tr>
                                </table>

                                <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
                                <tr valign="top">
                                <td >
                                	<table width="100%" cellpadding="1" cellspacing="0" border="0">
                                            <tr class="tableHeadingNew">
                                                <td colspan="2">Imports</td>
                                            </tr>
                                    <tr>
                                        <td width="80%">
                                            <table width="100%" cellpadding="1" cellspacing="0">
                                                  <tr class="textlabelsBold">
                                                        <td>Import AMS House BL# </td>
                                                        <td align="left">
                                                            <html:text property="importAMSHosueBlNumber"  value="${fclBl.importAMSHouseBl}" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox mandatory"
                                                               styleId="importAMSHosueBlNumber" maxlength="30" size="26" />
                                                        </td>
                                                     </tr>
                                                    <tr class="textlabelsBold">
                                                        <td >Sub-House BL# </td>
                                                        <td align="left">
                                                            <html:text property="imporigBL" value="${fclBl.importOrginBlno}" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                                                               styleId="importOriginalBL" maxlength="30" size="26" />
                                                        </td>
                                                    </tr>

                                                <tr class="textlabelsBold">
                                                    <td>Original House BL Required </td>
                                                    <td style="padding-bottom:14px;">
                                                        <html:radio property="originalBlRequired" value="Yes" name="transactionBean" styleId="originalBlYes"></html:radio> Yes
                                                        <html:radio property="originalBlRequired" value="No" name="transactionBean" styleId="originalBlNo"></html:radio> No
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    </table>

                                </td>
                                    <td  valign="top">
                                    <!-- this date of yard -->
                                    </td>
                                    </tr>
                                </table>
                            </td>
                            <td valign="top">
                                <table border="0" width="100%"  cellpadding="3" cellspacing="0" class="tableBorderNew">
                                    <tr class="tableHeadingNew"><td colspan="2">
                                               <c:choose>
                                                    <c:when test="${importFlag}">
                                                          Import References
                                                    </c:when>
                                                    <c:otherwise>
                                                        Export Reference
                                                    </c:otherwise>
                                               </c:choose>
                                        </td></tr>
                                    <tr>
                                        <td>
                                            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                                <tr>
                                                   <td class="textlabelsBold">
                                                       <c:choose>
                                                        <c:when test="${importFlag}">
                                                               Import References/Predefined Remarks
                                                                <span style="padding-left:10px;">
                                                                <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp('${importFlag}')"/></span>

                                                        </c:when>
                                                        <c:otherwise>
                                                            Export Reference
                                                        </c:otherwise>
                                                       </c:choose>
                                                   </td>
                                                </tr>
                                                <tr>
                                                 <td><html:textarea styleClass="textlabelsBoldForTextBox" styleId="exportReference" tabindex="-1"
                                                         property="exportReference" onkeypress="return checkTextAreaLimit(this, 250)" rows="3" cols="80"
                                                         value="${fclBl.exportReference}" style="text-transform: uppercase"/>
                                                </td>
                                                </tr>
                                                <tr>
                                                  <td class="textlabelsBold">Domestic Routing/Predefined Remarks</td>
                                                </tr>
                                                <tr>
                                                    <td valign="top">
                                                        <html:textarea styleClass="textlabelsBoldForTextBox"  property="domesticRouting"
                                                          rows="3" cols="80" value="${fclBl.domesticRouting}" onkeyup="limitTextarea(this,3,42)"
                                                          styleId="domesticRouting" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">
                                                       Onward Inland Routing/Cargo Location &nbsp;&nbsp;&nbsp;
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold" valign="top">
                                                        <span class="textlabelsBold">
                                                            <html:textarea property="onwardInlandRouting" styleClass="textlabelsBoldForTextBox" styleId="onwardInlandRouting"
                                                               value="${! empty fclBl.onwardInlandRouting ? fclBl.onwardInlandRouting: 'TBA' }" onkeypress="return checkTextAreaLimit(this, 100)" rows="3" cols="80"
                                                               style="text-transform: uppercase" ></html:textarea>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
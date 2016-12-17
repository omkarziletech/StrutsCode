<%-- 
    Document   : multiQuoteRatesDiv
    Created on : Mar 29, 2016, 4:56:20 PM
    Author     : Nambu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="addRatesDivId" class="static-popup" style="display: none;width: 600px;height: 104px;">
        <table class="table" style="margin: 1px;width: 598px;">
            <tr>
                <th colspan="4">
            <div class="float-left">Rates Quick Review</div>
            <div class="float-right">
                <a href="javascript: closeDivs()">
                    <img alt="Close Quick Rates" src="${path}/images/icons/close.png"/>
                </a>
            </div>
            </th>
            </tr>
            <c:choose>
                <c:when test="${importFlag}">
                    <tr  class="textlabelsBold">
                        <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                        <td id="isTerminal1">
                            <input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox mandatory" value="${quotationForm.isTerminal}"
                                   size="22" onblur="copyValPol();" onkeydown="getOriginUrl()"/>
                            <input type="hidden" id="isTerminal_check" value="${quotationForm.isTerminal}"/>
                            <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                <input type="checkbox" checked="checked" id="originCountry">   </span>

                            <div id="isTerminal_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                            <script type="text/javascript">
                                    initAutocomplete("isTerminal","isTerminal_choices","","isTerminal_check",
                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&importFlag=true&typeOfmove=&isDojo=false","setFocusFromDojo('portofDischarge')");
                            </script>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
                        <td>
                            <input Class="textlabelsBoldForTextBox mandatory" name="portofDischarge" onblur="copyValPod();" id="portofDischarge" value="${quotationForm.portofDischarge}" size="22"
                                   onkeyup="getDestination()"
                                   onkeydown="setDojoAction()"/>
                            <input type="hidden" id="portofDischarge_check" value="${quotationForm.portofDischarge}"/>
                            <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                            <script type="text/javascript">
                                               initAutocomplete("portofDischarge","destination_port_choices","","portofDischarge_check",
                                                               "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7");
                            </script>
                            <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                <input type="checkbox" id="destinationCity" checked="checked" tabindex="-1"></span>

                            <div id="portofDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                            <script type="text/javascript">
                               initAutocomplete("portofDischarge","portofDischarge_choices","","portofDischarge_check",
                               "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=76&radio=city&isDojo=false","");
                            </script>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr class="textlabelsBold">
                        <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
                        <td>
                            <input Class="textlabelsBoldForTextBox mandatory" name="portofDischarge" id="portofDischarge" value="${quotationForm.portofDischarge}" size="22"
                                   onkeyup="getDestination()"
                                   onkeydown="setDojoAction()"/>
                            <input id="portofDischarge_check" type="hidden" value="${quotationForm.portofDischarge}"/>
                            <div id="destination_port_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                            <script type="text/javascript">
                                               initAutocomplete("portofDischarge","destination_port_choices","","portofDischarge_check",
                                                               "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7");
                            </script>
                            <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                <input type="checkbox" id="destinationCity" checked="checked" ></span>
                            <span onmouseover="tooltip.show('<strong>Show Rates for Entire Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                <input type="checkbox" id="showAllCity" onclick="checkShowAllCity();" ></span>

                            <div id="portofDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                            <script type="text/javascript">
                               initAutocomplete("portofDischarge","portofDischarge_choices","","portofDischarge_check",
                               "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=76&radio=city&isDojo=false");
                            </script>
                        </td>
                        <td class="label">Commodity</td>
	<td>
	    <input name="commcode" class="textlabelsBoldForTextBox mandatory"  id="commcode" value="${quotationForm.commcode}"
	         		          maxlength="6" size="22" onkeydown="bulletRatesStauts();"/>
                                       <input id="commcode_check" type="hidden" value="${quotationForm.commcode}"/>
					<div id="commcode_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                    initAutocomplete("commcode","commcode_choices","","commcode_check",
									"${path}/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false");
                                            </script>
            <span onmouseover="tooltip.show('<strong>Bullet Rates<strong>',null,event);" onmouseout="tooltip.hide();">
            <input type="checkbox" id="bulletRates"  name="bulletRatesCheck" onclick="bulletRatesClick()">
            </span>
	</td>
                    </tr>
                    <tr  class="textlabelsBold">
                        <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                        <td id="isTerminal1">
                            <input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox mandatory" value="${quotationForm.isTerminal}"
                                   size="22" onkeydown="getOriginUrl()"/>
                            <input id="isTerminal_check" type="hidden" value="${quotationForm.isTerminal}"/>
                            <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                <input type="checkbox" checked="checked" id="originCountry" >   </span>

                            <div id="isTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                            <script type="text/javascript">
                                    initAutocomplete("isTerminal","isTerminal_choices","","isTerminal_check",
                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&typeOfmove=&isDojo=false","setFocusFromDojo('getRates')");
                            </script>
                        </td>
                    <td class="label">Hazmat</td>
	<td class="label">
	    <input type="radio" name="hazmat" id="hazmat" value="Y"/>Y
	    <input type="radio" name="hazmat" id="hazmat" value="N" checked/>N
	</td>
                    </tr>
                </c:otherwise>
            </c:choose>

            <tr>
                <td colspan="4" align="center">
                    <input type="button" id="getRates" value="Rates" class="button" onclick="getMultiRates('${importFlag}', '${quotationNo}');" />
                </td>
            </tr>
        </table>
    </div>

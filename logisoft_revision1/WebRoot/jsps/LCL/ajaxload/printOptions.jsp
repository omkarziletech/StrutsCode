<%--
    Document   : printOptions
    Created on : Oct 17, 2012, 6:30:08 PM
    Author     : Mohanapriya
--%>
<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"/>
<body>
    <input type="hidden" name="path" id="path" value="${path}"/>
    <input type="hidden" name="fileId" id="fileId" value="${lclBl.lclFileNumber.id}"/>
    <cong:div style="width:100%; float:left;">
        <cong:table width="100%"  border="0" style="border:1px solid #c4c5c4">
            <cong:tr>
                <cong:td  styleClass="tableHeadingNew" colspan="2">Print Options</cong:td>
            </cong:tr>
            <c:forEach items="${lclPrintOptionsList}" var="options">
                <c:if test="${options.optionValue eq 'AES'}">
                    <c:set var="aesValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'NCM'}">
                    <c:set var="ncmValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue=='HS'}">
                    <c:set var="hsValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue=='FP' && options.optionKey=='N'}">
                    <c:set var="optionKey4" value="N"/>
                </c:if>
                <c:if test="${options.optionValue eq 'IMP'}">
                    <c:set var="printImp" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue=='ALERT'}">
                    <c:set var="preAlertValues" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue=='PROOF'}">
                    <c:set var="proofValues" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'MET'}">
                    <c:set var="printMetricValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'PORT'}">
                    <c:set var="altPortLowerValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'RECEIVE'}">
                    <c:set var="paymentReceiveValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'MINI'}">
                    <c:set var="printMiniManifestValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'PIER'}">
                    <c:set var="printPierPodValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'ARRIVALDATE'}">
                    <c:set var="printArrivalDateValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'HBLPIER'}">
                    <c:set var="hblPierOverrValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'HBLPOL'}">
                    <c:set var="hblPolOverValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'DELIVERY'}">
                    <c:set var="fdOverrValue" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'LADENSAILDATE'}">
                    <c:set var="ladenSailDateVal" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'PRINTTERMSTYPE'}">
                    <c:set var="printTermsType1" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'CORRECTEDBL'}">
                    <c:choose>
                        <c:when test="${correctionClassName eq 'green-background' && lclBlForm.blUnitCob eq true}">
                            <input type="hidden" name="greenClassId" id="greenClassId" value="true"/>
                            <c:set var="printCorrectedBl" value="Y"/>
                        </c:when> 
                        <c:otherwise>
                            <c:set var="printCorrectedBl" value="${options.optionKey}"/>
                        </c:otherwise>   
                    </c:choose>
                </c:if>
                <c:if test="${options.optionValue eq 'PRINTHAZBEFORE'}">
                    <c:set var="printHazBeforeKey" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'PRINTPPDBLBOTH'}">
                    <c:set var="printPpdBlBoth" value="${options.optionKey}"/>
                </c:if>
                <c:if test="${options.optionValue eq 'INSURANCE'}">
                    <c:set var="insuranceValue" value="${empty options.optionKey ? 'N' : options.optionKey}"/>
                </c:if>
            </c:forEach>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print AES on body</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="aesCodes" class ="aftercobenable" id="aesCodesY"
                           ${aesValue eq 'Y' ? "checked" :''} onclick="updateAesValues();"/> Yes
                    <input type="radio" value="N" name="aesCodes" class ="aftercobenable" id="aesCodesN"
                           ${aesValue eq 'N' ? "checked" :''} onclick="updateAesValues();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print HS codes on body</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="hsCodes" id="hsCodesY"
                           ${hsValue eq 'Y' ? "checked" :''} onclick="updateHsValues();"/> Yes
                    <input type="radio" value="N" name="hsCodes" id="hsCodesN"
                           ${hsValue eq 'N' ? "checked" :''} onclick="updateHsValues();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print NCM codes on body</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="ncmCodes" id="ncmCodesY"
                           ${ncmValue eq 'Y' ? "checked" :''} onclick="updateNcmValues();"/> Yes
                    <input type="radio" value="N" name="ncmCodes" id="ncmCodesN"
                           ${ncmValue eq 'N' ? "checked" :''} onclick="updateNcmValues();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Use Alt Freight Pickup account</cong:td>
                <cong:td  styleClass="textBoldforlcl"> <c:choose>
                        <c:when test="${optionKey4=='N'}">
                            <input type="radio" value="Y" name="freightPickup" id="freightPickupY" onclick="clickFreightPickup();"/> Yes
                            <input type="radio" value="N" name="freightPickup" id="freightPickupN" checked="true" onclick="clickFreightPickup();"/> No
                        </c:when>
                        <c:otherwise>
                            <input type="radio" value="Y" name="freightPickup" id="freightPickupY" checked="true" onclick="clickFreightPickup();"/> Yes
                            <input type="radio" value="N" name="freightPickup" id="freightPickupN" onclick="clickFreightPickup();"/> No
                        </c:otherwise>
                    </c:choose>
                    <cong:autocompletor name="freightPickupText" template="tradingPartner" id="freightPickupText" callback="freightPickupField();"
                                        query="CLIENT_NO_CONSIGNEE" value="${frieghtPickupValue}" width="500" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox textLCLuppercase" scrollHeight="200px"  fields="freightPickupAccountNo,customerType,NULL,NULL,NULL,NULL,NULL,clientDisabled,address,NULL,NULL,NULL,NULL,contactName,phoneNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,clientDisableAcct" />
                    <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${frieghtPickupNo}"  name="freightPickupAccountNo" id="freightPickupAccountNo"/>
                    <input type="hidden" name="clientDisabled" id="clientDisabled"/>
                    <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
                </cong:td></cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print decimal on Imperial values</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="imperial" id="imperialY"
                           ${printImp eq 'Y' ? "checked" :''}  onclick="updateImpValue();"/> Yes
                    <input type="radio" value="N" name="imperial" id="imperialN"
                           ${printImp eq 'N' ? "checked" :''} onclick="updateImpValue();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Pre Alert</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="alert" id="alertY"
                           ${preAlertValues eq 'Y' ? "checked" :''}  onclick="clickPreAlert();"/> Yes
                    <input type="radio" value="N" name="alert" id="alertN"
                           ${preAlertValues eq 'N' ? "checked" :''}  onclick="clickPreAlert();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Proof Copy</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="proof" id="proofY"
                           ${proofValues eq 'Y' ? "checked" :''} onclick="clickProofCopy()"/> Yes
                    <input type="radio" value="N" name="proof" id="proofN"
                           ${proofValues eq 'N' ? "checked" :''} onclick="clickProofCopy()"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print Imperial values on Metric ports</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="metric" id="metricY"
                           ${printMetricValue eq 'Y' ? "checked" :''} onclick="clickMetricPort();"/> Yes
                    <input type="radio" value="N" name="metric" id="metricN"
                           ${printMetricValue eq 'N' ? "checked" :''}  onclick="clickMetricPort();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr><cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print Alt Port lower right</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" value="Y" name="port" id="portY"
                           ${altPortLowerValue eq 'Y' ? "checked" :''} onclick="clickAltPort();"/> Yes
                    <input type="radio" value="N" name="port" id="portN"
                           ${altPortLowerValue eq 'N' ? "checked" :''} onclick="clickAltPort();"/> No
                    <cong:autocompletor name="portText" template="client" id="portText" callback="portField();"
                                        query="UNLOCATIONCODE" value="${altPortValue}" width="100" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox textLCLuppercase" scrollHeight="200px"/>
                </cong:td></cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Payment must be received</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="paymentReceive" value="${empty paymentReceiveValue ? 'N' : paymentReceiveValue}"/>
                    <input type="radio" value="Y" name="payment" id="paymentY"
                           ${paymentReceive eq 'Y' ? "checked" :''} onclick="clickPayment();"/> Yes
                    <input type="radio" value="N" name="payment" id="paymentN"
                           ${paymentReceive eq 'N' ? "checked" :''} onclick="clickPayment();"/> No
                    <img title="Please note that payment must be received prior to issuing express release / original HBL"
                         style="vertical-align: middle" src="${path}/img/icons/help-icon.gif" width="15" height="15" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print No.of Packages in Mini-Manifest format Yes/No</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="printMiniManifest" value="${empty printMiniManifestValue ? 'N' : printMiniManifestValue}"/>
                    <input type="radio" value="Y" name="mini" id="miniY"
                           ${printMiniManifest eq 'Y' ? "checked" :''} onclick="updateMiniManifest();"/> Yes
                    <input type="radio" value="N" name="mini" id="miniN"
                           ${printMiniManifest eq 'N' ? "checked" :''} onclick="updateMiniManifest();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print Pier in Port of Loading Box</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="printPierPod" value="${empty printPierPodValue ? 'Y' : printPierPodValue}"/>
                    <input type="radio" value="Y" name="pier" id="pierY"
                           ${printPierPod eq 'Y' ? "checked" :''}  onclick="updatePierPod();"/> Yes
                    <input type="radio" value="N" name="pier" id="pierN"
                           ${printPierPod eq 'N' ? "checked" :''}  onclick="updatePierPod();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Print Arrival Date - lower right corner,after Unit at POD</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="printArrivalDate" value="${empty printArrivalDate ? 'Y' : printArrivalDateValue}"/>
                    <input type="radio" value="Y" name="arrival" id="arrivalDateY"
                           ${printArrivalDate eq 'Y' ? "checked" :''} onclick="updateArrivalDate();"/> Yes
                    <input type="radio" value="N" name="arrival" id="arrivalDateN"
                           ${printArrivalDate eq 'N' ? "checked" :''}   onclick="updateArrivalDate();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">HBL Pier Override</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="hblPierOver" value="${empty hblPierOverrValue ? 'N' : hblPierOverrValue}"/>
                    <input type="radio" value="Y" name="hblPier" id="hblPierY"
                           ${hblPierOver ne 'N' ? "checked" :''} onclick="clickHblPier();"/> Yes
                    <input type="radio" value="N" name="hblPier" id="hblPierN"
                           ${hblPierOver eq 'N' ? "checked" :''} onclick="clickHblPier();"/> No
                    <cong:text styleClass="textlabelsBoldForTextBox textLCLuppercase"
                               value="${hblPierOver ne 'Y' && hblPierOver ne 'N' ? hblPierOver :''}"
                               name="hblPierText" id="hblPierText" onchange="clickHblPier();" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">HBL Pol Override</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <c:set var="hblPolOver" value="${empty hblPolOverValue ? 'N' : hblPolOverValue}"/>
                    <input type="radio" value="Y" name="hblPol" id="hblPolY"
                           ${hblPolOver ne 'N' ? "checked" :''} onclick="clickHblPol();"/> Yes
                    <input type="radio" value="N" name="hblPol" id="hblPolN"
                           ${hblPolOver eq 'N' ? "checked" :''} onclick="clickHblPol();"/> No
                    <cong:text styleClass="textlabelsBoldForTextBox textLCLuppercase" container="NULL"
                               value="${(hblPolOver eq 'Y' || hblPolOver eq 'N') ? '' : hblPolOver}" name="hblPolText" id="hblPolText" onchange="clickHblPol();" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Final Delivery To override</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <c:set var="hblFinalOver" value="${empty fdOverrValue ? 'N' : fdOverrValue}"/>
                    <input type="radio" value="Y" name="deliveryOverride" id="deliveryOverrideY"
                           ${hblFinalOver ne 'N'? "checked":""} onclick="clickdeliveryOverride();"/> Yes
                    <input type="radio" value="N" name="deliveryOverride" id="deliveryOverrideN"
                           ${hblFinalOver eq 'N' ? "checked" :''} onclick="clickdeliveryOverride();"/> No
                    <cong:text styleClass="textlabelsBoldForTextBox textLCLuppercase" maxlength="30"
                               value="${hblFinalOver ne 'Y' && hblFinalOver ne 'N' ? hblFinalOver :''}"
                               name="deliveryText" id="deliveryText" onchange="clickdeliveryOverride();" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Print Laden on board + sailing date</cong:td>
                <cong:td styleClass="textBoldforlcl" >
                    <input type="radio" value="N" name="ladenSailDate" id="ladenSailDate"
                           ${ladenSailDateVal eq 'N'? "checked":""}   onclick="clickRouting();" /> No &nbsp;
                    <input type="radio" value="ROUTING" name="ladenSailDate" id="routing"
                           ${ladenSailDateVal eq 'ROUTING'? "checked":""}   onclick="clickRouting();"/> Routing Instructions
                    <input type="radio" value="EXPORT" name="ladenSailDate" id="export"
                           ${ladenSailDateVal eq 'EXPORT'? "checked":""}  onclick="clickRouting();"/> Export References
                    <input type="radio" value="BODYBL" name="ladenSailDate" id="export"
                           ${ladenSailDateVal eq 'BODYBL'? "checked":""}  onclick="clickRouting();"/> Body Of BL


                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Where to print Terms Type 1</cong:td>
                <cong:td styleClass="textBoldforlcl" >
                    <c:set var="printTermsType" value="${empty printTermsType1 ? 'ROUTING' : printTermsType1}"/>
                    <input type="radio" value="ROUTING" name="printTermsType" id="routing"
                           ${printTermsType eq 'ROUTING' ? "checked" :""}   onclick="clickTerms();"/> Routing Instructions
                    <input type="radio" value="EXPORT" name="printTermsType" id="export"
                           ${printTermsType eq 'EXPORT' ? "checked" :""}  onclick="clickTerms();"/> Export References
                    <input type="radio" value="BODYBL" name="printTermsType" id="bodyBl"
                           ${printTermsType eq 'BODYBL' ? "checked" :""}  onclick="clickTerms();"/> Body Of BL
                    <input type="radio" value="MARKS" name="printTermsType" id="marks"
                           ${printTermsType eq 'MARKS' ? "checked" :""}   onclick="clickTerms();" /> Marks
                    <input type="radio" value="NOTPRINT" name="printTermsType" id="marks"
                           ${printTermsType eq 'NOTPRINT' ? "checked" :""}   onclick="clickTerms();" /> Do Not Print

                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Print words Corrected BL when CN is present</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <c:set var="corrected" value="${empty printCorrectedBl ? 'N' : printCorrectedBl}"/>
                    <input type="radio" value="Y" name="correctedBl" id="correctedBl"
                           ${corrected eq 'Y'? "checked":''} onclick="clickCorrectedBl();"/> Yes
                    <input type="radio" value="N" name="correctedBl" id="correctedBl"
                           ${corrected eq 'N' ? "checked" :''} onclick="clickCorrectedBl();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Print Haz info first before Descriptions</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <c:set var="printHazBefore" value="${empty printHazBeforeKey ? 'N' : printHazBeforeKey}"/>
                    <input type="radio" value="Y" name="printHazBefore" id="printHazBefore"
                           ${printHazBefore eq 'Y'? "checked":''} onclick="updatePrintHazBefore();"/> Yes
                    <input type="radio" value="N" name="printHazBefore" id="printHazBefore"
                           ${printHazBefore eq 'N' ? "checked" :''} onclick="updatePrintHazBefore();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5" styleClass="textlabelsBoldforlcl" valign="middle">Print Freight Prepaid on a Both BL</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <c:set var="printPpdBlBoth" value="${empty printPpdBlBoth ? 'N' : printPpdBlBoth}"/>
                    <input type="hidden" id="checkprintPpdBlBoth" name="checkprintPpdBlBoth" value="${printPpdBlBoth}"/>
                    <input type="radio" value="Y" name="printPpdBlBoth" id="printPpdBlBothY"
                           ${printPpdBlBoth eq 'Y'? "checked":''} onclick="updateprintPpdBlBoth();"/> Yes
                    <input type="radio" value="N" name="printPpdBlBoth" id="printPpdBlBothN"
                           ${printPpdBlBoth eq 'N' ? "checked" :''} onclick="updateprintPpdBlBoth();"/> No
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="5%" styleClass="textlabelsBoldforlcl" valign="middle">Insurance - Print Goods Shipped Hereunder are insured</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <c:set var="insuranceValue" value="${empty insuranceValue ? 'Y': insuranceValue}"/>
                    <input type="radio" value="Y" name="printInsurance" id="printInsuranceY" 
                           <c:if test="${!checkBlInsurance}">disabled='disabled' </c:if> 
                           ${insuranceValue eq 'Y' ? "checked" :''} onclick="updatePrintInsurance();"/> Yes
                    <input type="radio" value="N" name="printInsurance" id="printInsuranceN"
                           <c:if test="${!checkBlInsurance}">disabled='disabled' </c:if> 
                           ${insuranceValue eq 'N' ? "checked" :''} onclick="updatePrintInsurance();"/> No
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:div>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            //            clickFreightPickup();
            //            clickAltPort();
            //            pierPort();
            //            arrivalDate();
            if ($("#blUnitCob").val() && $("#greenClassId").val()) {
                //set default to 'Y' When Correction Did 
                updateOption("CORRECTEDBL", "Y")
            }
            clickTerms();
            var altPortKey = $('input:radio[name=port]:checked').val();
            if (altPortKey === 'N') {
                $('#portText').hide();
            }
            var freightPickup = $('input:radio[name=freightPickup]:checked').val();
            if (freightPickup == "N") {
                $('#freightPickupText').hide();
                $('#freightPickupText').val('');
                $('#freightPickupText').hide();
                $('#freightPickupAccountNo').val('');
                $('#freightPickupAccountNo').hide();
            }
            var hblPier = $('input:radio[name=hblPier]:checked').val();
            if (hblPier == "N") {
                $('#hblPierText').hide();
            }
            var hblPol = $('input:radio[name=hblPol]:checked').val();
            if (hblPol == "N") {
                $('#hblPolText').hide();
            }
            var delivery = $('input:radio[name=deliveryOverride]:checked').val();
            if (delivery == "N") {
                $('#deliveryText').hide();
            }

        });
        function updateAesValues() {
            var optionValue = $('input:radio[name=aesCodes]:checked').val();
            updateOption("AES", optionValue);
        }
        function updateHsValues() {
            var optionValue = $('input:radio[name=hsCodes]:checked').val();
            updateOption("HS", optionValue);
        }
        function updateNcmValues() {
            var optionValue = $('input:radio[name=ncmCodes]:checked').val();
            updateOption("NCM", optionValue);
        }
        function updatePrintInsurance() {
            var optionValue = $('input:radio[name=printInsurance]:checked').val();
            updateOption("INSURANCE", optionValue);
        }
        function freightPickupField() {
            var fileNumberId = $('#fileNumberId').val();
            var freightText = $('#freightPickupAccountNo').val();
            if ($('#clientDisabled').val() === 'Y') {
                $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#clientDisableAcct').val() + "</span>");
                $('#freightPickupAccountNo').val('');
                $('#freightPickupText').val('');
            }
            if (freightText !== '') {
                $('#freightPickupAccountNo').show();
                $('#freightPickupAccountNo').attr("readonly", true);
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateLclOptions",
                        param1: fileNumberId,
                        param2: freightText,
                        param3: "FPFEILD",
                        request: true
                    }
                });
            }
        }
        function portField() {
            var fileNumberId = $('#fileNumberId').val();
            var portText = $('#portText').val();
            if (portText !== '') {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateLclOptions",
                        param1: fileNumberId,
                        param2: portText,
                        param3: "PORTFIELD",
                        request: true
                    }
                });
            }
        }
        function clickFreightPickup() {
            var fileNumberId = $('#fileNumberId').val();
            var freightKey = $('input:radio[name=freightPickup]:checked').val();
            if (freightKey === 'Y') {
                $('#freightPickupText').show();
                $('#freightPickupText').attr('readonly', false);
            } else {
                $('#freightPickupText').val('');
                $('#freightPickupText').hide();
                $('#freightPickupAccountNo').val('');
                $('#freightPickupAccountNo').hide();
            }
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "updateLclOptions",
                    param1: fileNumberId,
                    param2: freightKey,
                    param3: "FP",
                    request: true
                }
            });
        }
        // --------------  saving HBL PIER Field in print options--------------------------//
        function clickHblPier() {
            var fileNumberId = $('#fileNumberId').val();
            var textValue = $('#hblPierText').val();
            var hblPier = $('input:radio[name=hblPier]:checked').val();
            if (hblPier == "Y") {
                $('#hblPierText').show();
                $('#hblPierText').attr('readonly', false);
                textValue = $('#hblPierText').val() !== "" ? $('#hblPierText').val() : "Y";
            } else {
                $('#hblPierText').hide();
                $('#hblPierText').val('');
                textValue = "N";
            }
            if (textValue != '') {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateLclOptions",
                        param1: fileNumberId,
                        param2: textValue.toUpperCase(),
                        param3: "HBLPIER",
                        request: true
                    }
                });
            }
        }
        //----------------------saving HBL POL Field in print options---------------------//
        function clickHblPol() {
            var hbloptYValue = $('input:radio[name=hblPol]:checked').val();
            var hblPolOverrider = $('#hblPolText').val();
            if (hbloptYValue == 'Y') {
                $('#hblPolText').show();
                $('#hblPolText').attr('readonly', false);
                hblPolOverrider = '' !== hblPolOverrider ? hblPolOverrider : "Y";
            } else {
                $('#hblPolText').hide();
                $('#hblPolText').val('');
                hblPolOverrider = "N";
            }
            if (hbloptYValue != '') {
                var fileNumberId = $('#fileNumberId').val();
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateLclOptions",
                        param1: fileNumberId,
                        param2: hblPolOverrider.toUpperCase(),
                        param3: "HBLPOL",
                        request: true
                    }
                });
            }

        }

        //-----------------------------saving FINAL DELIVERY TO OVERRIDE Field in print options----------------------//

        function clickdeliveryOverride() {
            var deliveryValueText = $('deliveryText').val();
            var deliveryYValues = $('input:radio[name=deliveryOverride]:checked').val();
            if (deliveryYValues == 'Y') {
                $('#deliveryText').show();
                $('#deliveryText').attr('readonly', false);
                deliveryValueText = $('#deliveryText').val() !== "" ? $('#deliveryText').val() : "Y";
            }
            else {
                $('#deliveryText').hide();
                $('#deliveryText').val('');
                deliveryValueText = "N";
            }
            if (deliveryValueText != '') {
                var fileNumberId = $('#fileNumberId').val();
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateLclOptions",
                        param1: fileNumberId,
                        param2: deliveryValueText.toUpperCase(),
                        param3: "DELIVERY",
                        request: true
                    },
                    success: function (data) {
                        $('#finalDestinationf').val($('#deliveryText').val());
                    }

                });
            }
        }



        function clickAltPort() {
            var fileNumberId = $('#fileNumberId').val();
            var portKey = $('input:radio[name=port]:checked').val();
            if (portKey === 'Y') {
                $('#portText').show();
                $('#portText').attr('readonly', false);
            } else {
                $('#portText').val('');
                $('#portText').hide();

            }
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "updateLclOptions",
                    param1: fileNumberId,
                    param2: portKey,
                    param3: "PORT",
                    request: true
                }
            });
        }
        function clickPreAlert() {
            var optionValue = $('input:radio[name=alert]:checked').val();
            updateOption("ALERT", optionValue);
        }
        function clickProofCopy() {
            var optionValue = $('input:radio[name=proof]:checked').val();
            updateOption("PROOF", optionValue);
        }
        function clickMetricPort() {
            var optionValue = $('input:radio[name=metric]:checked').val();
            updateOption("MET", optionValue);
        }
        function updateImpValue() {
            var optionValue = $('input:radio[name=imperial]:checked').val();
            updateOption("IMP", optionValue);
        }
        function clickPayment() {
            var optionValue = $('input:radio[name=payment]:checked').val();
            updateOption("RECEIVE", optionValue);
        }
        function updateMiniManifest() {
            var optionValue = $('input:radio[name=mini]:checked').val();
            updateOption("MINI", optionValue);
        }
        function updatePierPod() {
            var optionValue = $('input:radio[name=pier]:checked').val();
            updateOption("PIER", optionValue);
        }
        function updateArrivalDate() {
            var optionValue = $('input:radio[name=arrival]:checked').val();
            updateOption("ARRIVALDATE", optionValue);
        }
        function clickRouting() {
            var optionValue = $('input:radio[name=ladenSailDate]:checked').val();
            updateOption("LADENSAILDATE", optionValue);
        }
        function clickTerms() {
            var optionValue = $('input:radio[name=printTermsType]:checked').val();
            updateOption("PRINTTERMSTYPE", optionValue);
        }
        function clickCorrectedBl() {
            var optionValue = $('input:radio[name=correctedBl]:checked').val();
            updateOption("CORRECTEDBL", optionValue);
        }
        function updatePrintHazBefore() {
            var optionValue = $('input:radio[name=printHazBefore]:checked').val();
            updateOption("PRINTHAZBEFORE", optionValue);
        }
        function updateprintPpdBlBoth() {
            var pcBoth = $("input:radio[name='pcBoth']:checked").val();
            var optionValue = $('input:radio[name=printPpdBlBoth]:checked').val();
            if (pcBoth === 'B' && (optionValue === 'Y' || optionValue === 'N')) {
                updateOption("PRINTPPDBLBOTH", optionValue);
            } else {
                $('#printPpdBlBothY').attr('checked', false);
                $('#printPpdBlBothN').attr('checked', true);
            }
        }
        function updateOption(optionKey, optionValue) {
            var fileNumberId = $('#fileNumberId').val();
            $.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "updateLclOptions",
                    param1: fileNumberId,
                    param2: optionValue,
                    param3: optionKey,
                    request: true
                }
            });
        }
    </script>
</body>
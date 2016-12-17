<%@include file="init.jsp" %>
<%@include file="/taglib.jsp" %> 
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/jspVariables.jsp"%>
<style>
    .check {
        float:right
    }
</style>
<body>
    <cong:div style="width:99%; float:left;">
        <cong:form name="lclAesDetailsForm" id="lclAesDetailsForm" action="/lclAesDetails.do">
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="id" id="id"/>
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                AES for File No: <span class="fileNo">${lclAesDetailsForm.fileNumber}</span> 
            </cong:div>
            <cong:table width="100%">
            <tbody>
                <cong:tr>
                    <cong:td>
                        <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew" style="border:1px solid #dcdcdc">
                        <tbody>
                            <cong:tr styleClass="tableHeadingNew">
                                <cong:td>USPPI</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td><cong:table width="100%" cellspacing="0" cellpadding="2" border="0">
                                    <tbody>
                                        <cong:tr>
                                            <cong:td styleClass="textlabelsBoldforlcl">Name</cong:td>
                                            <cong:td><c:choose>
                                                    <c:when test="${sedFilings.shipperCheck=='on'}">
                                                        <cong:div id="shipper"  style="display: none">
                                                            <cong:text name="expnam" id="expname" styleClass="mandatory textLclWidth" style="text-transform: uppercase" value="${sedFilings.expnam}"/></cong:div>
                                                        <cong:div id="dojoShipper">
                                                            <cong:autocompletor id="shipnam" name="expnam" template="tradingPartner"
                                                                                fields="expnum,shipper_acct_type,NULL,NULL,NULL,NULL,NULL,NULL,expadd,expcty,expsta,NULL,expzip,NULL,expcpn,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,expirs,expctry,idtype,duns"
                                                                                query="CLIENT_NO_CONSIGNEE"  width="600" container="NULL" value="${sedFilings.expnam}" styleClass="mandatory textLclWidth textuppercaseLetter"
                                                                                shouldMatch="true" scrollHeight="300px" callback="copyShipper()"/></cong:div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <cong:div id="shipper">
                                                            <cong:text name="expnam" id="expname" styleClass="mandatory textLclWidth" style="text-transform: uppercase" value="${sedFilings.expnam}"/></cong:div>
                                                        <cong:div id="dojoShipper" style="display: none">
                                                            <cong:autocompletor id="shipnam" name="expnam" template="tradingPartner"
                                                                                fields="expnum,shipper_acct_type,NULL,NULL,NULL,NULL,NULL,NULL,expadd,expcty,expsta,NULL,expzip,NULL,expcpn,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,expirs,expctry,idtype,duns"
                                                                                query="CLIENT_NO_CONSIGNEE"  width="600" container="NULL" value="${sedFilings.expnam}" styleClass="mandatory textLclWidth textuppercaseLetter"
                                                                                shouldMatch="true" scrollHeight="300px" callback="copyShipper()"/></cong:div>
                                                    </c:otherwise></c:choose>
                                                <input type="hidden" id="shipper_acct_type"/>
                                                <input type="hidden" id="duns"/>
                                                <input type="hidden" id="idtype"/>
                                                <input type="hidden" name="checkboxShip" id="checkboxShip" value="${sedFilings.shipperCheck}"/>
                                            <input type="hidden" name="expctry" id="expctry" value="${sedFilings.expctry}"/>
                                            <cong:checkbox name="shipperCheck" id="shipperCheck" onmouseover="tooltip.showSmall('Use TP dojo')" value="${sedFilings.shipperCheck}" onmouseout="tooltip.hide()" onclick="shipperDojo();"/></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">Number</cong:td>
                                        <cong:td><input type="text" name="expnum" id="expnum" class="text text-readonly" readonly="true" value="${sedFilings.expnum}"/></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">Cargo Origin <br/>Address</cong:td>
                                        <cong:td><cong:textarea cols="30" rows="3" name="expadd" id="expadd" style="text-transform: uppercase" value="${sedFilings.expadd}"></cong:textarea></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                                        <cong:td><cong:text name="expcty" id="expcty" styleClass="text mandatory" style="text-transform: uppercase" value="${sedFilings.expcty}" maxlength="50"/></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">State</cong:td>
                                        <cong:td><cong:text name="expsta" id="expsta" styleClass="text mandatory" style="text-transform: uppercase" value="${sedFilings.expsta}" maxlength="2"/></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                                        <cong:td><cong:text name="expzip" id="expzip" styleClass="text mandatory" style="text-transform: uppercase" value="${sedFilings.expzip}" maxlength="5"/></cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">IRS #</cong:td>
                                        <cong:td>
                                            <cong:text name="expirs" id="expirs" styleClass="text mandatory"
                                                       style="text-transform: uppercase"  onchange="validateIrs(this)" value="${sedFilings.expirs}"/>
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">Type</cong:td>
                                        <cong:td>
                                            <html:select styleId="expicd" styleClass="smallDropDown mandatory" property="expicd" name="expicd" value="${sedFilings.expicd}">
                                                <html:option value="">Select</html:option>
                                                <html:option value="E">EIN</html:option>
                                                <html:option value="D">DUNS</html:option>
                                                <html:option value="T">PASSPORT NUMBER</html:option></html:select>
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td styleClass="textlabelsBoldforlcl">POA</cong:td>
                                        <cong:td styleClass="textBoldforlcl">
                                            <c:choose>
                                                <c:when test="${sedFilings.exppoa=='Y'}">
                                                    <input type="radio" id="exppoa1" checked="checked" value="Y" name="exppoa"/>Yes
                                                    <input type="radio" id="exppoa2" value="N" name="exppoa"/>No
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" id="exppoa1" value="Y" name="exppoa"/>Yes
                                                    <input type="radio" id="exppoa2" checked="checked" value="N" name="exppoa"/>No
                                                </c:otherwise>
                                            </c:choose>
                                        </cong:td>
                                    </cong:tr>
                                </tbody>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </tbody>
            </cong:table>  
            <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew" style="border:1px solid #dcdcdc" >
                <tbody><cong:tr styleClass="tableHeadingNew">
                        <cong:td>ULTIMATE CONSIGNEE</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td>
                            <cong:table width="100%" cellspacing="0" cellpadding="2" border="0">
                            <tbody><cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Name</cong:td>
                                    <cong:td>
                                        <c:choose>
                                            <c:when test="${sedFilings.consigneeCheck=='on'}">
                                            <div id="consigne"  style="display: none">
                                                <cong:text name="connam" id="connam" styleClass="mandatory textLclWidth" style="text-transform: uppercase" value="${sedFilings.connam}"/></div>
                                            <div id="dojconsignee">
                                                <cong:autocompletor name="connam" template="tradingPartner" id="consName" fields="connum,NULL,NULL,NULL,NULL,NULL,NULL,NULL,conadd,concty,conpst,NULL,consZip,NULL,concpn"
                                                                    styleClass="mandatory textLclWidth textuppercaseLetter" query="CLIENT_NO_CONSIGNEE"  width="600" container="NULL" value="${sedFilings.connam}" scrollHeight="300px"
                                                                    shouldMatch="true" callback="copyConsignee();"/></div>
                                            </c:when>
                                            <c:otherwise>
                                            <div id="consigne">
                                                <cong:text name="connam" id="connam" styleClass="mandatory textLclWidth" style="text-transform: uppercase" value="${sedFilings.connam}"/></div>
                                            <div id="dojconsignee" style="display: none">
                                                <cong:autocompletor name="connam" template="tradingPartner" id="consName" fields="connum,NULL,NULL,NULL,NULL,NULL,NULL,NULL,conadd,concty,conpst,NULL,consZip,NULL,concpn"
                                                                    styleClass="mandatory textLclWidth textuppercaseLetter" query="CLIENT_NO_CONSIGNEE"  width="600" container="NULL" value="${sedFilings.connam}" scrollHeight="300px"
                                                                    shouldMatch="true" callback="copyConsignee();"/></div>
                                            </c:otherwise></c:choose>
                                    <input type="hidden" name="checkboxCons" id="checkboxCons" value="${sedFilings.consigneeCheck}"/>
                                    <cong:checkbox name="consigneeCheck" id="consigneeCheck" onmouseover="tooltip.showSmall('Use TP dojo')" value="${sedFilings.consigneeCheck}" onmouseout="tooltip.hide()" onclick="consigneeDojo()"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Number</cong:td>
                                <cong:td> <input type="text" name="connum" id="connum" class="text text-readonly" style="text-transform: uppercase" readonly="true" value="${sedFilings.connum}"/> </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                                <cong:td><cong:textarea name="conadd" cols="30" rows="3" id="conadd" style="text-transform: uppercase" styleClass="mandatory" value="${sedFilings.conadd}"></cong:textarea></cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                                <cong:td><cong:text name="concty" id="concty" styleClass="text mandatory" style="text-transform: uppercase" value="${sedFilings.concty}" maxlength="50"/></cong:td>
                            </cong:tr>
                            <cong:tr> 
                                <cong:td styleClass="textlabelsBoldforlcl">Country</cong:td>
                                <cong:td><cong:text name="consCountry" id="conpst" maxlength="50" style="text-transform: uppercase" value="${sedFilings.conctry}"/></cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                                <cong:td><cong:text id="consZip" name="conpst" maxlength="5" style="text-transform: uppercase" value="${sedFilings.conpst}"/></cong:td>
                            </cong:tr>
                            <cong:tr>
                                <td class="textlabelsBoldforlcl">POA</td>
                                <cong:td styleClass="textBoldforlcl">
                                    <c:choose>
                                        <c:when test="${sedFilings.conpoa=='Y'}">
                                            <input type="radio" id="conpoa1" checked="checked" value="Y" name="conpoa"/>Y
                                            <input type="radio" id="conpoa2" value="N" name="conpoa"/>No
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" id="conpoa1" value="Y" name="conpoa"/>Y
                                            <input type="radio" id="conpoa2" checked="checked" value="N" name="conpoa"/>No
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                            <tr>
                                <td class="textlabelsBoldforlcl">CONTYP</td>
                                <td>
                                    <html:select property="CONTYP" styleId="CONTYP" styleClass="dropdown_accounting mandatory"
                                                 name="lclAesDetailsForm">
                                        <html:option value="">Select</html:option>
                                        <html:option value="O">O=Other/Unknown</html:option>
                                        <html:option value="D">D=DirectConsumer</html:option>
                                        <html:option value="G">G=GovernmentEntity</html:option>
                                        <html:option value="R">R=ResaleDealer</html:option>
                                    </html:select>
                                </td>
                            </tr>
                        </tbody>
                    </cong:table>
                </cong:td>
            </cong:tr>
        </tbody>
    </cong:table>
</cong:td>                   
<cong:td>
    <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td>General Information</cong:td>
            <cong:td align="right">
                <input type="button"  value="Save" class="button-style3 floatRight" onclick="validateAes()"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td colspan="2">
                <cong:table width="100%" cellspacing="0" cellpadding="2" border="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">ITN#</cong:td>
                        <cong:td><input type="text" name="itn" class="text text-readonly" readonly="true" value="${sedFilings.itn}"/></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Shipment/DR#</cong:td>
                        <cong:td><input type="text" name="shpdr" id="shpdr" class="text text-readonly" readonly="true" value="${sedFilings.shpdr}"/></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Transaction Ref</cong:td>
                        <cong:td><input type="text" name="trnref" id="trnref" class="text text-readonly" readonly="true" value="${sedFilings.trnref}"/></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">BL NUM#</cong:td>
                        <cong:td><input type="text" name="blnum" class="text text-readonly" readonly="true" value="${sedFilings.blnum}"/></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">SSL BKG#</cong:td>
                        <cong:td><input type="text" name="sslBkg" id="sslBkg" class="text text-readonly" readonly="true" value="${sedFilings.trnref}"/></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td><input type="text" name="email" id="email" class="text mandatory" value="${sedFilings.email}"/></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Status Of SED</cong:td>
                        <cong:td>
                            <input type="text" name="status" class="text text-readonly" readonly="true" value="${sedFilings.status}"/>
                        </cong:td>
                        <cong:td></cong:td>
                        <cong:td></cong:td>

                    </cong:tr>
                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew">
        <cong:tr styleClass="tableHeadingNew">
            <cong:td>Trade Route</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td>
                <cong:table width="100%" cellspacing="0" cellpadding="2" border="0">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Origin State</cong:td>
                        <cong:td>
                            <cong:autocompletor id="origin" name="origin" template="concatOrigin" fields="NULL,originState,NULL,NULL" query="RELAYNAME" styleClass="mandatory textLCLuppercase" width="500" container="NULL" value="${sedFilings.origin}" shouldMatch="true"/>
                            <input type="text" name="originState" id="originState" class="text text-readonly" readonly="true" value="${sedFilings.orgsta}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle"><span class="mandatory">Routed Transaction</span></cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${sedFilings.routed=='Y'}">
                                    <input type="radio" id="routed1" value="Y" name="routed" checked="checked"/>Yes
                                    <input type="radio" id="routed"  value="N" name="routed"/>No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" id="routed1" value="Y" name="routed"/>Yes
                                    <input type="radio" id="routed" checked="checked" value="N" name="routed"/>No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Destination Country</cong:td>
                        <cong:td>
                            <cong:autocompletor id="destn" name="destn" template="concatOrigin" fields="NULL,NULL,NULL,destnCty"
                                                query="RELAYNAME_FD" styleClass="mandatory textLCLuppercase"  width="500" container="NULL" value="${sedFilings.destn}" shouldMatch="true"/>
                            <input type="text" name="cntdes" id="destnCty" class="text text-readonly" readonly="true" value="${sedFilings.cntdes}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle"><span>Related Companies</span></cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${sedFilings.relate=='Y'}">
                                    <input type="radio" id="relate" value="Y" name="relate" checked="checked"/>Yes
                                    <input type="radio" id="relate"  value="N" name="relate"/>No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" id="relate" value="Y" name="relate"/>Yes
                                    <input type="radio" id="relate" checked="checked" value="N" name="relate"/>No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POL</cong:td>
                        <cong:td>
                            <cong:autocompletor name="pol" id="pol" styleClass="mandatory textLCLuppercase" template="origin"
                                                fields="NULL,NULL,polId,exppnm" query="PORT_NAME" width="500" labeltitle="Port Of Loading" container="NULL"
                                                value="${sedFilings.pol}" shouldMatch="true"/>
                            <input type="text" name="exppnm" value="${sedFilings.exppnm}" id="exppnm" class="text text-readonly" readonly="true"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">
                            Waiver Notice
                        </cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${sedFilings.waiver=='Y'}">
                                    <input type="radio" id="waiver" value="Y" name="waiver" checked="checked"/>Yes
                                    <input type="radio" id="waiver"   value="N" name="waiver"/>No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" id="waiver" value="Y" name="waiver"/>Yes
                                    <input type="radio" id="waiver" checked="checked" value="N" name="waiver"/>No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POD</cong:td>
                        <cong:td>
                            <cong:autocompletor name="pod" id="pod" styleClass="text mandatory textLCLuppercase" query="PORT_NAME"
                                                fields="NULL,NULL,podId,upptna" template="origin" width="500" container="NULL"
                                                value="${sedFilings.pod}" shouldMatch="true"/>
                            <input type="text" name="upptna" value="${sedFilings.upptna}" id="upptna" class="text text-readonly" readonly="true" />
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">
                            <span class="mandatory">Hazardous</span>
                        </cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${sedFilings.hazard=='Y'}">
                                    <input type="radio" id="hazard" value="Y" name="hazard" checked="checked"/>Yes
                                    <input type="radio" id="hazard" value="N" name="hazard"/>No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" id="hazard" value="Y" name="hazard"/>Yes
                                    <input type="radio" id="hazard" checked="checked" value="N" name="hazard"/>No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Departure Date
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="depDat" value="${sedFilings.depdat}"/>
                            <cong:calendar id="depDate" name="depDate" styleClass="text2 mandatory" value="${depDat}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Mode Of Transportation</cong:td>
                        <cong:td>
                            <html:select styleId="modtrn" styleClass="dropdown_accounting" property="modtrn"
                                         name="modtrn" value="${sedFilings.modtrn}">
                                <html:option value="10">Vessel</html:option>
                                <html:option value="40">Air</html:option>
                                <html:option value="20">Rail</html:option>
                            </html:select>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew">
        <cong:tr>
            <cong:td>
                <cong:table width="100%" cellspacing="0" cellpadding="1" border="0">
                    <tr class="tableHeadingNew">
                        <td colspan="2">Carrier/Vessel/Voyage</td>
                        <td colspan="2">Inbond</td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Conveyance</cong:td>
                        <cong:td>
                            <cong:text id="vesnam" name="vesnam" style="text-transform: uppercase"
                                       styleClass="text mandatory" value="${sedFilings.vesnam}"  maxlength="30"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Inbond Number</cong:td>
                        <td>
                            <cong:text id="inbnd" name="inbnd" style="text-transform: uppercase" styleClass="text"
                                       onchange="enableFtZone()" maxlength="15" value="${sedFilings.inbnd}" container="NULL"/>
                        </td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Vessel Number</cong:td>
                        <cong:td>
                            <input type="text" id="vesnum" class="text text-readonly" readOnly="true" tabindex="-1"
                                   maxlength="6" name="vesnum" value="${sedFilings.vesnum}"/></cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Entry#</cong:td>
                        <cong:td>
                            <cong:text id="inbent" styleClass="text" style="text-transform: uppercase" onkeyup="checkInbondNo(this)"
                                       container="NULL" maxlength="12" name="inbent" value="${sedFilings.inbent}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Voyage Number</cong:td>
                        <cong:td>
                            <cong:text id="voyvoy" styleClass="text" style="text-transform: uppercase"
                                       maxlength="30" name="voyvoy" value="${sedFilings.voyvoy}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Foreign Trade Zone</cong:td>
                        <cong:td>
                            <cong:text id="ftzone" styleClass="text" style="text-transform: uppercase" onkeyup="checkInbondNo(this)"
                                       container="NULL"  maxlength="7" name="ftzone" value="${sedFilings.ftzone}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Carrier SCAC</cong:td>
                        <cong:td>
                            <cong:text id="scac" styleClass="text mandatory" style="text-transform: uppercase"
                                       maxlength="4" name="scac" value="${sedFilings.scac}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Inbond Type
                        </cong:td>
                        <cong:td>
                            <html:select styleId="inbtyp" styleClass="smallDropDown" property="inbtyp" value="${sedFilings.inbtyp}">
                                <html:option value="">Please Select</html:option>
                                <html:option value="3">FTZ-TE</html:option>
                                <html:option value="4">FTZ-IE</html:option>
                                <html:option value="5">WW-TE</html:option>
                                <html:option value="6">WW-IE</html:option>
                            </html:select>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Unit Number</cong:td>
                        <cong:td>
                            <cong:text id="unitno" styleClass="text" style="text-transform: uppercase"
                                       maxlength="13" name="unitno" value="${sedFilings.unitno}"/>
                        </cong:td>
                        <cong:td styleClass="textlabelsBoldforlcl">Inbond Indicator</cong:td>
                        <cong:td styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${sedFilings.inbind=='Y'}">
                                    <input type="radio" id="inbind1" value="Y" name="inbind" checked="checked"/>Yes
                                    <input type="radio" id="inbind2" value="N" name="inbind"/>No
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" id="inbind1" value="Y" name="inbind"/>Yes
                                    <input type="radio" id="inbind2" checked="checked" value="N" name="inbind"/>No
                                </c:otherwise>
                            </c:choose>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="100%" cellspacing="0" cellpadding="3" border="0" styleClass="tableBorderNew">
        <tbody><cong:tr styleClass="tableHeadingNew">
                <cong:td>Contact Information</cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td>
                    <cong:table width="100%" cellspacing="0" cellpadding="2" border="0">
                    <tbody><cong:tr>
                            <cong:td colspan="6" styleClass="lab-sty textBoldforlcl">Shipper Contact</cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">First Name</cong:td>
                            <cong:td><cong:text id="expcfn" styleClass="text mandatory" style="text-transform: uppercase"  maxlength="20" name="expcfn" value="${sedFilings.expcfn}"/></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">Last Name</cong:td>
                            <cong:td><cong:text id="expcln" styleClass="text" maxlength="20" style="text-transform: uppercase" name="expcln" value="${sedFilings.expcln}"/></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td><cong:text id="expcpn" styleClass="text mandatory" style="text-transform: uppercase"   value="${sedFilings.expcpn}" maxlength="10" name="expcpn" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td colspan="6" styleClass="lab-sty textBoldforlcl">Consignee Contact</cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">First Name</cong:td>
                            <cong:td><cong:text id="concfn" styleClass="text" style="text-transform: uppercase" maxlength="20" name="concfn" value="${sedFilings.concfn}"/></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">Last Name</cong:td>
                            <cong:td><cong:text id="concln" styleClass="text" style="text-transform: uppercase" maxlength="20" name="concln" value="${sedFilings.concln}"/></cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td><cong:text id="concpn" styleClass="text" style="text-transform: uppercase" value="${sedFilings.concpn}" maxlength="10" name="concpn" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                        </cong:tr>
                    </tbody></cong:table>
            </cong:td>
        </cong:tr>
    </tbody></cong:table>
</cong:td>
</cong:tr>
</tbody></cong:table>
</cong:form>
</cong:div>
<script>
    jQuery(document).ready(function(){
        copyConsignee();
        copyShipper();
        verifyCheckBoxValue();
    });
    function submitAjaxAes(methodName,formName,selector,id){
        showProgressBar();
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        var shpdr=$('#shpdr').val();
        params+="&id="+id+"&shpdr="+shpdr;
        $.post($(formName).attr("action"),params,
        function(data) {
            $(selector).html(data);
            $(selector,window.parent.document).html(data);
            hideProgressBar();
            parent.$.colorbox.close();
        });
    }

    function congAlert(txt){
        $.prompt(txt);
    }
    
    function shipperDojo(){
        if($('#shipperCheck').is(":checked")){
            $('#shipper').hide();
            $('#dojoShipper').show();
            $('#expnum').val('');
            $('#expadd').val('');
            $('#expcty').val('');
            $('#expzip').val('');
            $('#expsta').val('');
            $('#expnam').val('');
            $('#expcpn').val('');
            $('#expname').val('');
            $('#shipnam').val('');
            $('#expirs').val('');
            $('#expicd').val('');
        }else{
            $('#shipper').show();
            $('#dojoShipper').hide();
            $('#expnum').val('');
            $('#expadd').val('');
            $('#expirs').val('');
            $('#expicd').val('');
            $('#expcty').val('');
            $('#expzip').val('');
            $('#expsta').val('');
            $('#expcpn').val('');
            $('#expnam').val('');
            $('#expname').val('');
            $('#shipnam').val('');
        }
    }
    function consigneeDojo(){
        if($('#consigneeCheck').is(":checked")){
            $('#consigne').hide();
            $('#dojconsignee').show();
            $('#connam').val('');
            $('#concpn').val('');
            $('#connum').val('');
            $('#conadd').val('');
            $('#concty').val('');
            $('#conpst').val('');
            $('#consZip').val('');
            $('#consName').val('');
        }else{
            $('#consigne').show();
            $('#dojconsignee').hide();
            $('#connam').val('');
            $('#connum').val('');
            $('#conadd').val('');
            $('#concpn').val('');
            $('#concty').val('');
            $('#conpst').val('');
            $('#consZip').val('');
            $('#consName').val('');
        }
    }
    
    
    function copyConsignee(){
        var consigne= $('#consName').val();
        $('#connam').val(consigne);
    }
    function copyShipper(){
        var consigne= $('#shipnam').val();
        $('#expname').val(consigne);
        if($('#idtype').val()=='E'){
            $('#expicd').val('E');
        }else if($('#idtype').val()=='S'){
            $('#expicd').val('T');
        }else{
            if($('#duns').val()!=null && $('#duns').val()!=""){
                $('#expirs').val($('#duns').val());
                $('#expicd').val('D');
            }else{
                $('#expirs').val($('#expirs').val());
            }
        }
    }

    //    function shipper_AccttypeCheck(){
    //        target=jQuery("#shipper_acct_type").val();
    //        if(target!=""){
    //            if(((target!='S' && target!='E' && target!='I' && target=='V') || (target=='C')) && subtype!='Forwarder'){
    //                congAlert("Please select the customers with account type S,E,I and V with subtype forwarder");
    //                $('#expnum').val('');
    //                $('#expadd').val('');
    //                $('#expcty').val('');
    //                $('#expzip').val('');
    //                $('#expsta').val('');
    //                $('#expname').val('');
    //                $('#shipnam').val('');
    //            }
    //        }
    //    }

    function validateAes(){
        var shipPoa=$("input:radio[name='exppoa']:checked").val();
        var conPoa=$("input:radio[name='conpoa']:checked").val();
        var mandatory = "";
        if(trim(document.getElementById("expname").value)==''&&trim(document.getElementById("shipnam").value)==''){
            mandatory = "-->Shipper Name is required<br>";
        }
        if(trim( document.getElementById('expadd').value)==''){
            mandatory =mandatory+ "-->Shipper Address is required<br>";
        }
        if(trim(document.getElementById('expcty').value)=='' ){
            mandatory =mandatory+ "-->Shipper City is required<br>";
        }
        if(trim(document.getElementById('expsta').value)==null || trim(document.getElementById('expsta').value)=="" ){
            mandatory =mandatory+ "-->Shipper state is required<br>";
        }
        if(trim(document.getElementById('expzip').value)==null || trim(document.getElementById('expzip').value)=="" ){
            mandatory =mandatory+ "-->Shipper zip is required<br>";
        }
        if(trim(document.getElementById('expicd').value)==null || trim(document.getElementById('expicd').value)==""){
            mandatory=mandatory+ "-->Shipper type field is required<br>";
        }
        if( trim(document.getElementById("connam").value)=="" && trim(document.getElementById("consName").value)=="" ){
            mandatory =mandatory+ "-->Consignee Name is required<br>";
        }
        if(trim(document.getElementById("conadd").value)==null || trim(document.getElementById("conadd").value)==""){
            mandatory =mandatory+ "-->Consignee address is required<br>";
        }
        if(trim(document.getElementById("concty").value)==null || trim(document.getElementById("concty").value)==""){
            mandatory =mandatory+ "-->Consignee City is Required<br>";
        }
        if(trim(document.getElementById('email').value)=='' || trim(document.getElementById('email').value)==null){
            mandatory =mandatory+ "-->Email is required<br>";
        }
        if(trim(document.getElementById('origin').value)=='' || trim(document.getElementById('origin').value)==null){
            mandatory =mandatory+ "-->Origin is required<br>";

        }
        if(trim(document.getElementById("originState").value)==null || trim(document.getElementById("originState").value)==""){
            mandatory =mandatory+ "-->Origin State is Required<br>";
        }
        if(trim(document.getElementById("destn").value)==null || trim(document.getElementById("destn").value)==""){
            mandatory =mandatory+ "-->Destination is Required<br>";
        }
        if(trim(document.getElementById("pol").value)==null || trim(document.getElementById("pol").value)==""){
            mandatory =mandatory+ "-->Pol is Required<br>";
        }
        if(trim(document.getElementById("pod").value)==null || trim(document.getElementById("pod").value)==""){
            mandatory =mandatory+ "-->Pod is Required<br>";
        }
        if(trim(document.getElementById("vesnam").value)==null || trim(document.getElementById("vesnam").value)==""){
            mandatory =mandatory+ "-->Conveyance is Required<br>";
        }
        if(trim(document.getElementById("scac").value)==null ||trim(document.getElementById("scac").value)==""){
            mandatory =mandatory+ "-->Scac is Required<br>";
        }
        if(trim(document.getElementById("expcpn").value)==null || trim(document.getElementById("expcpn").value)==""){
            mandatory =mandatory+ "-->Shipper Phone is Required<br>";
        }
        if(trim(document.getElementById("expcfn").value)==null ||trim(document.getElementById("expcfn").value)==""){
            mandatory =mandatory+ "-->Shipper First Name is Required<br>";
        }
        if(trim(document.getElementById("depDate").value)==null || trim(document.getElementById("depDate").value)==""){
            mandatory =mandatory+ "-->Departure Date is Required<br>";
        }
        if(trim(shipPoa)=='N' && trim(conPoa)=='N'){
            mandatory =mandatory+ "-->Please Select Either Shipper or Consignee POA<br>";
        }
        if(trim(document.getElementById("expirs").value)==null || trim(document.getElementById("expirs").value)==""){
            mandatory =mandatory+ "-->IRS # is Required<br>";
        }
        else if(document.getElementById("expirs").value.replace("-","").trim().length != '9' && document.getElementById("expirs").value.replace("-","").trim().length!= '11' && document.getElementById("expirs").value.replace("-","").trim().length!= '13'){
            mandatory =mandatory+ "-->IRS# Length Should be 9 or 11 or 13<br>";
        }
        if(( trim(document.getElementById("inbnd").value)!="")&& ( trim(document.getElementById("inbent").value)=="" && trim(document.getElementById("ftzone").value)=="")){
            mandatory =mandatory+ "-->Please Enter either Foreign Trade Zone or Inbond Entry#<br>";
        }
        if($('#CONTYP').val() === ""){
            mandatory =mandatory+"--->Select CONTYP in ULTIMATE CONSIGNEE";
        }
        if(mandatory != ''){
            $.prompt(mandatory);
        }
        else{
            submitAjaxAes('save','#lclAesDetailsForm','#aesDesc','');
        }
    }
        
          

    function verifyCheckBoxValue(){
        var shipId = $('#checkboxShip').val();
        var consId = $('#checkboxCons').val();
        if(shipId=='on'){
            $('#shipperCheck').attr('checked',true);
        }   else{
            $('#shipperCheck').attr('checked',false);
        }
        if(consId=='on') {
            $('#consigneeCheck').attr('checked',true);
        }   else{
            $('#consigneeCheck').attr('checked',false);
        }
    }

    function checkForNumberAndDecimal(obj){
        var result;
        if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
            obj.value="";
            $.prompt("This field should be Numeric");
        }
    }
    function checkInbondNo(obj) {
        if (event.keyCode != 9) {
            if (trim($("#inbnd").val()) == "") {
                $.prompt('Please Enter Inbond Number');
                obj.value = "";
            }
        }
    }
    function enableFtZone() {
        if (trim($("#inbnd").val()) == "") {
            $("#inbent").val('');
            $("#ftzone").val('');
        }
    }
    function validateIrs(obj) {
        var irsNo = obj.value.replace("-", "");
        if (irsNo.length != '9' && irsNo.length != '11' && irsNo.length != '13') {
            $.prompt("IRS# Length Should be 9 or 11 or 13");
        }
    }
    $(document).ready(function(){
        $(document).keydown(function(e) {
            if($(e.target).attr("readonly")){
                if (e.keyCode === 8) {
                    return false;
                }
            }
        });
    });
</script>
</body>


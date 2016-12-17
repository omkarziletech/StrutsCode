<html>
    <head>
        <%@include file="../init.jsp"%>
        <%@include file="/taglib.jsp"%>
        <%@include file="../colorBox.jsp" %>
        <%@include file="../../includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript">
            var path = "${path}";
        </script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <cong:form  action="/domesticBooking" name="bookingForm" id="bookingForm">
            <c:if test="${empty order}">
                <cong:table width="100%" style="margin:5px 0; float:left">
                    <cong:tr>
                        <cong:td styleClass="tableHeadingNew" colspan="4">
                            <cong:div styleClass="floatLeft">Edit Booking&nbsp; &nbsp;</cong:div>
                            <cong:div styleClass="floatRight">
                                <c:choose>
                                <c:when test="${empty booking.bolStatus}">
                                    <html:button value="Submil Shipment" onclick="createXml(${booking.id})" property="shipment" styleClass="button" />
                                </c:when>
                                <c:when test="${booking.bolStatus == 'E'}">
                                    <html:button value="Submil Shipment" onclick="createXml(${booking.id})" property="shipment" styleClass="button" style="background-color: #FF0000;"/>
                                </c:when>
                                <c:otherwise>
                                    <html:button value="Submil Shipment" onclick="createXml(${booking.id})" property="shipment" styleClass="button"/>
                                </c:otherwise>
                            </c:choose>
                                <html:button value="View/Print" onclick="createPreview('${booking.id}')" property="shipment" styleClass="button"/>
                                <html:button value="Save" onclick="updateBooking()" property="update" styleClass="button"/>
                                &nbsp; &nbsp;&nbsp; &nbsp;
                            </cong:div>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </c:if>
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="bookingId" name="bookingId" value="${booking.id}"/>
            <cong:hidden id="purchaseOrderId" name="purchaseOrderId" value="${order.id}"/>
            <table class="tableBorderNew" border="0" width="100%" cellspacing="3" cellpadding="0">
                <tr>
                    <td>
                        <cong:table width="100%"  cellpadding="0" cellspacing="0" border="0" styleClass="tableBorderNew">
                            <cong:tr>
                                <cong:td colspan="2" styleClass="report-header">Shipper</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                                <cong:td>
                                    <cong:autocompletor id="shipperName" name="shipperName" template="tradingPartner"  styleClass="textlabelsBoldForTextBox textCap"
                                                        fields="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,shipperAddress,shipperCity,shipperState,NULL,shipperZip,shipperContactName,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,NULL"
                                                        query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${booking.shipperName}"
                                                        shouldMatch="true"  scrollHeight="300px"/>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','SH')"/>

                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                <cong:td>
                                    <cong:textarea cols="25" rows="4" id="shipperAddress" name="shipperAddress"  styleClass="textlabelsBoldForTextBox textup" value="${booking.shipperAddress1}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                                <cong:td>

                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperCity" name="shipperCity"  maxlength="50" value="${booking.shipperCity}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">State </cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperState" name="shipperState"  maxlength="50" value="${booking.shipperState}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperZip" name="shipperZip"  maxlength="50" value="${booking.shipperZipcode}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Shipper Ref</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperReference" name="shipperReference"  maxlength="50" value="${booking.shipperReference}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Contact Name</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperContactName" name="shipperContactName"  maxlength="50" value="${booking.shipperContactName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperPhone" name="shipperPhone"  maxlength="50" value="${booking.shipperContactPhone}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperFax" name="shipperFax"  maxlength="50" value="${booking.shipperContactFax}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperEmail" name="shipperEmail" maxlength="100" value="${booking.shipperContactEmail}"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>

                    </td>
                    <td>
                        <cong:table width="100%"  cellpadding="0" cellspacing="0" border="0" styleClass="tableBorderNew">
                            <cong:tr>
                                <cong:td colspan="2" styleClass="report-header">Consignee</cong:td>
                            </cong:tr>

                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                                <cong:td>
                                    <cong:autocompletor id="consigneeName" name="consigneeName" template="tradingPartner"  styleClass="textlabelsBoldForTextBox textCap"
                                                        fields="NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,consigneeAddress,consigneeCity,consigneeState,NULL,consigneeZip,consigneeContactName,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,NULL,NULL"
                                                        query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${booking.consigneeName}"
                                                        shouldMatch="true"  scrollHeight="300px"/>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','CO')"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                <cong:td>
                                    <cong:textarea cols="25" rows="4" id="consigneeAddress" name="consigneeAddress"  styleClass="textlabelsBoldForTextBox textup" value="${booking.consigneeAddress1}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeCity" name="consigneeCity"  maxlength="50" value="${booking.consigneeCity}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">State </cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="consigneeState" name="consigneeState"  maxlength="50" value="${booking.consigneeState}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="consigneeZip" name="consigneeZip"  maxlength="50" value="${booking.consigneeZipcode}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Consignee Ref</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="consigneeReference" name="consigneeReference"  maxlength="50" value="${booking.consigneeReference}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Contact Name</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="consigneeContactName" name="consigneeContactName"  maxlength="50" value="${booking.consigneeContactName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneePhone" name="consigneePhone"  maxlength="50" value="${booking.consigneeContactPhone}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeFax" name="consigneeFax"  maxlength="50" value="${booking.consigneeContactFax}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeEmail" name="consigneeEmail" maxlength="100" value="${booking.consigneeContactEmail}"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </td>
                    <td valign="top">
                        <cong:table width="100%"  cellpadding="0" cellspacing="0" border="0" styleClass="tableBorderNew">
                            <cong:tr>
                                <cong:td colspan="2" styleClass="report-header">Carrier Details</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <cong:table>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Carrier Name</cong:td>
                                            <cong:td>
                                                <cong:text  id="carrierName" name="carrierName" styleClass="textlabelsBoldForTextBox textCap" value="${booking.carrierName}"/>
                                            </cong:td>
                                        </cong:tr>
                                         <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Pro Number</cong:td>
                                            <cong:td>
                                                <cong:text  id="proNumber" name="proNumber" styleClass="textlabelsBoldForTextBox textCap" value="${booking.proNumber}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin Terminal</cong:td>
                                            <cong:td>
                                                <cong:text  id="originName" name="originName" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originName}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin Code</cong:td>
                                            <cong:td>
                                                <cong:text  id="originCode" name="originCode" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originCode}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                            <cong:td>
                                                <cong:textarea cols="25" rows="4" id="originAddress" name="originAddress"  styleClass="textlabelsBoldForTextBox textup" value="${booking.originAddress}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin City</cong:td>
                                            <cong:td>
                                                <cong:text  id="originCity" name="originCity" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originCity}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin State</cong:td>
                                            <cong:td>
                                                <cong:text  id="originState" name="originState" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originState}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin Zip</cong:td>
                                            <cong:td>
                                                <cong:text  id="originZip" name="originZip" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originZip}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin Phone</cong:td>
                                            <cong:td>
                                                <cong:text  id="originPhone" name="originPhone" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originPhone}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Origin Fax</cong:td>
                                            <cong:td>
                                                <cong:text  id="originFax" name="originFax" styleClass="textlabelsBoldForTextBox textCap" value="${booking.originFax}"/>
                                            </cong:td>
                                        </cong:tr>
                                    </cong:table>
                                </cong:td>
                                <cong:td>
                                    <cong:table>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Carrier Scac</cong:td>
                                            <cong:td>
                                                <cong:text  id="scac" name="scac" styleClass="textlabelsBoldForTextBox textCap" value="${booking.scac}"/>
                                            </cong:td>
                                        </cong:tr>
                                          <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Carrier Nmemonic</cong:td>
                                            <cong:td>
                                                <cong:text  id="carrierNemonic" name="carrierNemonic" styleClass="textlabelsBoldForTextBox textCap" value="${booking.carrierNemonic}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination Terminal</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationName" name="destinationName" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationName}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination Code</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationCode" name="destinationCode" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationCode}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                            <cong:td>
                                                <cong:textarea cols="25" rows="4" id="destinationAddress" name="destinationAddress"  styleClass="textlabelsBoldForTextBox textup" value="${booking.destinationAddress}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination City</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationCity" name="destinationCity" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationCity}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">destination State</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationState" name="destinationState" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationState}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination Zip</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationZip" name="destinationZip" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationZip}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination Phone</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationPhone" name="destinationPhone" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationPhone}"/>
                                            </cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td  styleClass="td textlabelsBoldforlcl">Destination Fax</cong:td>
                                            <cong:td>
                                                <cong:text  id="destinationFax" name="destinationFax" styleClass="textlabelsBoldForTextBox textCap" value="${booking.destinationFax}"/>
                                            </cong:td>
                                        </cong:tr>
                                    </cong:table>
                                </cong:td>
                            </cong:tr>

                        </cong:table>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <cong:table width="100%"  cellpadding="0" cellspacing="0" border="0" styleClass="tableBorderNew">
                            <cong:tr>
                                <cong:td colspan="10" styleClass="report-header">Bill TO</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="billtoName" name="billtoName"  maxlength="50" value="${booking.billtoName}"/>
                                </cong:td>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                <cong:td>
                                    <cong:textarea cols="40" rows="3" id="billtoAddress" name="billtoAddress"  styleClass="textlabelsBoldForTextBox textup" value="${booking.billtoAddress1}"/>
                                </cong:td>
                                <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="billtoCity" name="billtoCity"  maxlength="50" value="${booking.billtoCity}"/>
                                </cong:td>
                                <cong:td  styleClass="td textlabelsBoldforlcl">State </cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="billtoState" name="billtoState"  maxlength="50" value="${booking.billtoState}"/>
                                </cong:td>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="billtoZip" name="billtoZip"  maxlength="50" value="${booking.billtoZipcode}"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <div id="results">
                            <c:import url="/jsps/LCL/Domestic/purchaseOrder.jsp"/>
                        </div>
                    </td>
                </tr>
                <c:if test="${not empty order}">
                    <tr>
                        <td colspan="3">
                            <table border="0" width="100%" cellspacing="3" cellpadding="0">
                                <tr>
                                    <td>
                                        <cong:table width="100%" cellspacing="6" cellpadding="3">
                                            <cong:tr styleClass="report-header">
                                                <cong:td colspan="9" >Line Item Details</cong:td>
                                                <cong:td align="right" style="float:right">
                                                    <html:button value="Save" onclick="updatePurchaseOrder()" property="update" styleClass="button"/>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">P.O Number</cong:td>
                                                <cong:td>
                                                    <cong:text  id="purchaseOrderNo" name="purchaseOrderNo" styleClass="textlabelsBoldForTextBox textCap" value="${order.purchaseOrderNo}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Package Type</cong:td>
                                                <cong:td>
                                                    <html:select property="packageType" styleId="packageType"  styleClass="smallDropDown" style="width:100%" value="${order.packageType}">
                                                        <html:option value="">Select</html:option>
                                                        <html:option value="BAG">Bag</html:option>
                                                        <html:option value="BDL">Bundle</html:option>
                                                        <html:option value="BOX">Box</html:option>
                                                        <html:option value="BRL">Barrel</html:option>
                                                        <html:option value="CRT">Crate</html:option>
                                                        <html:option value="CTN">Carton</html:option>
                                                        <html:option value="DRM">Drum</html:option>
                                                        <html:option value="PAL">Pail</html:option>
                                                        <html:option value="PCS">Pieces</html:option>
                                                        <html:option value="REL">REEL</html:option>
                                                        <html:option value="OTH">Other</html:option>
                                                    </html:select>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Package Quantity</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="packageQuantity" name="packageQuantity"  maxlength="50" value="${order.packageQuantity}" onkeyup="allowOnlyWholeNumbers(this);"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Weight</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="weight" name="weight"  maxlength="50" value="${order.weight}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Extra Info</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="extraInfo" name="extraInfo"  maxlength="50" value="${order.extraInfo}"/>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Product Name</cong:td>
                                                <cong:td>
                                                    <cong:text  id="productName" name="productName" styleClass="textlabelsBoldForTextBox textCap" value="${order.productName}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Description</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="description" name="description"  maxlength="50" value="${order.description}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Hazmat Number</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="hazmatNumber" name="hazmatNumber"  maxlength="50" value="${order.hazmatNumber}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">NMFC</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="nmfc" name="nmfc"  maxlength="50" value="${order.nmfc}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Class</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="classes" name="classes"  maxlength="50" value="${order.classes}"/>
                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Handling UnitType</cong:td>
                                                <cong:td>
                                                    <html:select property="handlingUnitType" styleId="handlingUnitType"  styleClass="smallDropDown" style="width:100%" value="${order.handlingUnitType}">
                                                        <html:option value="">Select</html:option>
                                                        <html:option value="PAL">Pallet</html:option>
                                                        <html:option value="SKD">Skid</html:option>
                                                        <html:option value="LSE">Loose</html:option>
                                                        <html:option value="OTH">Other</html:option>
                                                    </html:select>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Handling UnitQty</cong:td>
                                                <cong:td>
                                                    <cong:text  id="handlingUnitQuantity" name="handlingUnitQuantity" styleClass="textlabelsBoldForTextBox textCap" onkeyup="allowOnlyWholeNumbers(this);" value="${order.handlingUnitQuantity}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Length</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="length" name="length" onkeyup="allowOnlyWholeNumbers(this);"  maxlength="50" value="${order.length}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Width</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="width" name="width" onkeyup="allowOnlyWholeNumbers(this);" maxlength="50" value="${order.width}"/>
                                                </cong:td>
                                                <cong:td  styleClass="td textlabelsBoldforlcl">Height</cong:td>
                                                <cong:td>
                                                    <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="height" name="height" onkeyup="allowOnlyWholeNumbers(this);" maxlength="50" value="${order.height}"/>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </c:if>
            </table>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    function createXml(id){
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "createBolXML",
                param1: id,
                request: true,
                dataType: "json"
            },
            success: function (data) {
                if(null != data){
                    if(undefined != data[1] && null != data[1] && data[1] != ''){
                        parent.document.getElementById("bookingId").value = id;
                        parent.search();
                    }else{
                        sampleAlert(data[0]);
                    }
                }
            }
        });
    }
    function closePopupBox(){
        parent.$.fn.colorbox.close();
    }
    function updateBooking(){
        $('#methodName').val("updateBooking");
        $('#bookingForm').submit();
    }
    function editPurchaseOrder(id){
        $('#methodName').val("editPurchaseOrder");
        $('#purchaseOrderId').val(id);
        $('#bookingForm').submit();
    }
    function updatePurchaseOrder(){
        $('#methodName').val("updatePurchaseOrder");
        $('#bookingForm').submit();
    }
    function allowOnlyWholeNumbers(obj){
        var result;
        if(!/^[1-9]\d*$/.test(obj.value)){
            result=obj.value.replace(/[^0-9]+/g,'');
            obj.value=result;
            return false;
        }
        return true;
    }
    function openTradingPartner(path, type) {
        var url = path + '/tradingPartner.do?buttonValue=removesession';
        $(".tp").attr("href", url);
        $(".tp").colorbox({
            iframe: true,
            width: "100%",
            height: "100 %",
            title: "Trading Partner"
        });
    }
    function showPreview(fileName){
        var title = "Quote Rate";
        var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
        window.parent.parent.showLightBox(title,url);
    }
    function createPreview(id){
        var url = $("#bookingForm").attr("action");
        var params = "methodName=preview";
        params += "&bookingId=" + id;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showPreview",
            async: false
        });
    }
</script>

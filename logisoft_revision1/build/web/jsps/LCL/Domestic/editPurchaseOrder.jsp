<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Edit Booking&nbsp; &nbsp;</cong:div>
                <cong:div styleClass="floatRight">
                    <html:button value="Save" onclick="updatePurchaseOrder()" property="update" styleClass="button"/>
                    &nbsp; &nbsp;
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <table class="tableBorderNew" border="0" width="100%" cellspacing="3" cellpadding="0">
        <tr>
            <td>
                <cong:table width="100%"  cellpadding="0" cellspacing="0" border="0" styleClass="tableBorderNew">
                    <cong:tr>
                        <cong:td colspan="10" styleClass="report-header">Purchase Order</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Number</cong:td>
                        <cong:td>
                            <cong:text  id="purchaseOrderNo" name="purchaseOrderNo" styleClass="textlabelsBoldForTextBox textCap" value="${order.purchaseOrderNo}"/>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Package Type</cong:td>
                            <html:select property="packageType" styleId="packageType" value="70" styleClass="smallDropDown" style="width:100%" value="${order.packageType}">
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
                        <cong:td>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Package Quantity</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="packageQuantity" name="packageQuantity"  maxlength="50" value="${order.packageQuantity}"/>
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
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="description" name="description"  maxlength="50" value="${order.description}"/>
                        <cong:td>
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
                            <cong:text  id="handlingUnitQuantity" name="handlingUnitQuantity" styleClass="textlabelsBoldForTextBox textCap" value="${order.handlingUnitQuantity}"/>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Handling UnitQty</cong:td>
                        <html:select property="handlingUnitType" styleId="handlingUnitType" value="70" styleClass="smallDropDown" style="width:100%" value="${order.handlingUnitType}">
                                    <html:option value="">Select</html:option>
                                    <html:option value="PAL">Pallet</html:option>
                                    <html:option value="SKD">Skid</html:option>
                                    <html:option value="LSE">Loose</html:option>
                                    <html:option value="OTH">Other</html:option>
                            </html:select>
                        <cong:td>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Length</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="length" name="length"  maxlength="50" value="${order.length}"/>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Width</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="width" name="width"  maxlength="50" value="${order.width}"/>
                        </cong:td>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Height</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="height" name="height"  maxlength="50" value="${order.height}"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </td>
        </tr>
    </table>

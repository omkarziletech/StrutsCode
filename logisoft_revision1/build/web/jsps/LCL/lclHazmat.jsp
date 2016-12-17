<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclHazmat.js"/>
<cong:javascript src="${path}/jsps/LCL/js/common/lclHazmat.js"/>
<%-- <cong:javascript src="${path}/jsps/LCL/js/common.js"/> --%>
<jsp:useBean id="LclHazmatDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO"/>
<c:set var="pkgCompos" value="${LclHazmatDAO.allPkgComposition}" scope="request"/>
<c:set var="outerPkgType" value="${LclHazmatDAO.outerPkgType}"  scope="request"/>
<c:set var="innerPkgType" value="${LclHazmatDAO.innerPkgType}"  scope="request"/>

<body>
    <cong:form  id="lclHazmatForm"  name="lclHazmatForm" action="/lclHazmat.do" >
        <cong:hidden name="buttonFlag" id="buttonFlag" value="${lclHazmatForm.buttonFlag}"/>
        <cong:hidden name="freeFormValues" id="freeFormValues" value="${lclBookingHazmat.hazmatDeclarations}"/>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="moduleName" id="moduleName"  value="${lclHazmatForm.moduleName}"/>
        <cong:hidden name="fileNumberId" value="${lclHazmatForm.fileNumberId}" id="fileNumberId"/>
        <cong:hidden name="fileNumber" value="${lclHazmatForm.fileNumber}"/>
        <cong:hidden name="bookingPieceId" id="bookingPieceId" value="${lclHazmatForm.bookingPieceId}"/>
        <cong:hidden name="commodityCode" id="commodityCode" value="${lclHazmatForm.commodityCode}"/>
        <cong:hidden name="commodityDesc" id="commodityDesc" value="${lclHazmatForm.commodityDesc}"/>
        <cong:hidden name="hazClassDesc" id="hazClassDesc" value="${lclHazmatForm.hazClassDesc}"/>
        <cong:hidden name="hazmatId" id="hazmatId" value="${lclHazmatForm.hazmatId}"/>
        <cong:div style="width:99%;">
            <cong:table  style="width:100%">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td  width="10%">Hazmat for File No: <span style="color: red">${lclHazmatForm.fileNumber}</span></cong:td>
                    <cong:td  width="10%">Tariff No: <span style="color: red" >${lclHazmatForm.commodityCode}</span></cong:td>
                    <cong:td  width="10%">Tariff Name: <span style="color: red" >${lclHazmatForm.commodityDesc}</span></cong:td>
                </cong:tr>
            </cong:table>

            <c:if test="${not empty lclHazmatForm.hazmatList}">
                <cong:div id="hazmatList">
                    <display:table name="${lclHazmatForm.hazmatList}" class="dataTable" style="width:100%"
                                   id="lclHazmat" requestURI="/lclHazmat.do" pagesize='10'>
                        <display:setProperty name="paging.banner.some_items_found">
                            <span class="pagebanner">
                                <font color="blue">{0}</font> Search File details displayed,For more code click on page numbers.
                            </span>
                        </display:setProperty>
                        <display:setProperty name="paging.banner.one_item_found">
                            <span class="pagebanner" style="font-size: 10px">One {0} displayed. Page Number</span>
                        </display:setProperty>
                        <display:setProperty name="paging.banner.all_items_found"><span class="pagebanner">{0} {1} Displayed, Page Number</span>
                        </display:setProperty>
                        <display:setProperty name="basic.msg.empty_list">
                        </display:setProperty>
                        <display:setProperty name="paging.banner.placement" value="bottom" />
                        <display:setProperty name="paging.banner.item_name" value="Hazmat"/>
                        <display:setProperty name="paging.banner.items_name" value="Hazmat"/>
                        <display:column title="Action">
                            <img src="${path}/images/edit.png" class="addCommodity" style="cursor:pointer" width="12" height="12" alt="edit" id="editHaz"
                                 onclick="editBkgHazmat('${lclHazmat.id}');" title="edit hazmat"/>
                            <img src="${path}/images/error.png" style="cursor:pointer" width="12" height="12" alt="delete"
                                 onclick="deleteBkgHazmat('${lclHazmat.id}');"
                                 title="delete hazmat"/>
                        </display:column>
                        <display:column title="UN Number" property="unHazmatNo"></display:column>
                        <display:column  title="Proper Shipping Name">
                            <span style="text-transform: uppercase">${lclHazmat.properShippingName}</span>
                        </display:column>
                        <display:column title="Technical Name">
                            <span style="text-transform: uppercase">${lclHazmat.technicalName}</span>
                        </display:column>
                        <display:column title="IMO Class Code" property="imoPriClassCode"></display:column>
                    </display:table>
                </cong:div>
            </c:if>

            <cong:table>
                <cong:tr styleClass="tableHeadingNew" style="width:95%">
                    <cong:td styleClass="tableHeadingNew" width="90%">Hazmat</cong:td>
                    <cong:td align="right" width="5%">
                        <cong:div styleClass="button-style1" style="float:right" id="addHazmat" onclick="addBkgHazmat();">Add</cong:div>
                        <cong:div styleClass="button-style1" style="float:right" id="saveHazmat" >Save</cong:div>
                    </cong:td>
                    <cong:td align="right" width="5%">
                        <cong:div styleClass="button-style1" style="float:left" id="closeHazmat" onclick="closeHazmat('${lclHazmat.id}','${lclHazmat.lclBookingPiece.id}')">Close</cong:div>
                    </cong:td>
                </cong:tr>
            </cong:table>

            <cong:div id="hazmatsection">
                <cong:table width="100%" style="margin:5px 0; float:left" border="0">
                    <cong:tr>
                        <cong:td width="850px">
                            <cong:table cellpadding="2" cellspacing="0" border="0" width="850px" styleClass="borderL borderT haz-table ">
                                <cong:tr>
                                    <cong:td width="12%" styleClass="textlabelsBoldforlcl">UN#:</cong:td>
                                    <cong:td width="15%">
                                        <cong:text name="unHazmatNo" id="unHazmatNo" styleClass="textlabelsBoldForTextBox mandatory" maxlength="4"
                                                   value="${lclBookingHazmat.unHazmatNo}" onkeyup="validateNumbersOnly(this);" onchange="validateFourDigit(this);"/>
                                    </cong:td>
                                    <cong:td width="20%">&nbsp;</cong:td>
                                    <cong:td width="15%">&nbsp;</cong:td>
                                    <cong:td width="20%">&nbsp;</cong:td>
                                    <cong:td width="15%" styleClass="borderR">&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Proper Shipping Name:</cong:td>
                                    <cong:td width="20%">
                                        <cong:text name="properShippingName" id="properShippingName" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox mandatory" value="${lclBookingHazmat.properShippingName}" maxlength="50"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Tech. Chemical Name:</cong:td>
                                    <cong:td>
                                        <cong:text name="technicalName" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" id="technicalName" value="${lclBookingHazmat.technicalName}" maxlength="100"/>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td styleClass="borderR">&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td width="25%" styleClass="textlabelsBoldforlcl">IMO Class: </cong:td>
                                    <cong:td>
                                        <html:select styleClass="textlabelsBoldForTextBox smallDropDown mandatory" style="width: 100px" property="imoPriClassCode" styleId="hazClass" value="${lclBookingHazmat.imoPriClassCode}">
                                            <html:optionsCollection property="hazmatClass"/>
                                        </html:select>
                                        <img src="${path}/img/icons/iicon.png" width="12" height="12"
                                             title="${lclHazmatForm.hazClassDesc}" style="vertical-align: middle"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">IMO Subsidary Class:</cong:td>
                                    <cong:td>
                                        <cong:text name="imoPriSubClassCode" id="imoPriSubClassCode" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclBookingHazmat.imoPriSubClassCode}" maxlength="3" />
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">IMO Secondary Class:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="imoSecSubClassCode" id="imoSecSubClassCode" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclBookingHazmat.imoSecSubClassCode}" maxlength="3" />
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Packaging Group Code: </cong:td>
                                    <cong:td>
                                        <html:select styleClass="smallDropDown textlabelsBoldForTextBox" style="width:130px;" property="packingGroupCode" styleId="packingGroupCode" value="${lclBookingHazmat.packingGroupCode}">
                                            <html:optionsCollection property="pkgGroupCode"/>
                                        </html:select>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td styleClass="borderR">&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="borderB textlabelsBoldforlcl">Flash point: </cong:td>
                                    <cong:td styleClass="borderB" colspan="2">
                                        <cong:text name="flashPoint" id="flashPoint" styleClass="textSmall textlabelsBoldForTextBox" value="${lclBookingHazmat.flashPoint}" onkeyup="allowNegativeNumbers(this)" maxlength="7"/>
                                        <cong:label  styleClass="textBoldforlcl" text="(Celsius)"></cong:label></cong:td>
                                    <cong:td styleClass="borderB">&nbsp;</cong:td>
                                    <cong:td styleClass="borderB">&nbsp;</cong:td>
                                    <cong:td styleClass="borderR borderB">&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Outer Packaging Pieces: </cong:td>
                                    <cong:td>
                                        <cong:text name="outerPkgNoPieces" id="outerPkgNoPieces" styleClass="textlabelsBoldForTextBox number mandatory" value="${lclBookingHazmat.outerPkgNoPieces}" onkeyup="checkForNumberAndDecimal(this)" maxlength="5"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Inner Packing Pieces: </cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="innerPkgNoPieces" id="innerPkgNoPieces" styleClass="textlabelsBoldForTextBox number" value="${lclBookingHazmat.innerPkgNoPieces}" onkeyup="checkForNumberAndDecimal(this)" maxlength="5"/>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Outer Pkg Composition: </cong:td>
                                    <cong:td>
                                        <html:select styleClass="textlabelsBoldForTextBox smallDropDown mandatory" style="width:130px;" property="outerPkgComposition" styleId="outerPkgComposition" value="${lclBookingHazmat.outerPkgComposition}">
                                            <html:option value="" styleClass="tdLeft">Select Package Type</html:option>
                                            <html:optionsCollection name="pkgCompos" />
                                        </html:select>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Inner Pkg Composition: </cong:td>
                                    <cong:td styleClass="borderR">
                                        <html:select styleClass="textlabelsBoldForTextBox smallDropDown" style="width:130px;" property="innerPkgComposition" styleId="innerPkgComposition" value="${lclBookingHazmat.innerPkgComposition}">
                                            <html:option value="" styleClass="tdLeft">Select Package Type</html:option>
                                            <html:optionsCollection name="pkgCompos" />
                                        </html:select>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Outer Packaging Type: </cong:td>
                                    <cong:td>
                                        <html:select styleClass="textlabelsBoldForTextBox smallDropDown mandatory" style="width:130px;" property="outerPkgType" styleId="outerPkgType" value="${lclBookingHazmat.outerPkgType}">
                                            <html:option value="" styleClass="tdLeft">Select Package Type</html:option>
                                            <html:optionsCollection name="outerPkgType" />
                                        </html:select>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Inner Packaging Type: </cong:td>
                                    <cong:td styleClass="borderR">
                                        <html:select styleClass="textlabelsBoldForTextBox smallDropDown" style="width:130px;" property="innerPkgType" styleId="innerPkgType" value="${lclBookingHazmat.innerPkgType}">
                                            <html:option value="" styleClass="tdLeft">Select Package Type</html:option>
                                            <html:optionsCollection name="innerPkgType" />
                                        </html:select>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>                           
                                <cong:tr>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Inner Pkg UOM:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <html:select property="innerPkgUom" styleId="innerPkgUom" style="width:130px;" styleClass="textlabelsBoldForTextBox smallDropDown" value="${lclBookingHazmat.innerPkgUom}">
                                            <html:option value="" styleClass="tdLeft">Select</html:option>
                                            <html:option value="KGS">Kilograms</html:option>
                                            <html:option value="LTR">Liters</html:option>
                                            <html:option value="ML">Milliliters</html:option>
                                            <html:option value="GAL">Gallons</html:option>
                                            <html:option value="OZ">Ounces</html:option>
                                        </html:select>
                                        <span  id="innerPkgUom" title="K=KGS<BR>L=LITERS<BR>M=ML<BR>G=GAL<BR>O=OZ">
                                            <cong:img src="${path}/img/icons/iicon.png" width="12" height="12"/>
                                        </span>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>

                                <cong:tr>
                                    <cong:td styleClass="borderB" >&nbsp;</cong:td>
                                    <cong:td styleClass="borderB">&nbsp;</cong:td>
                                    <cong:td styleClass="borderB textlabelsBoldforlcl">Inner Pkg Wt/Vol Per Piece: </cong:td>
                                    <cong:td styleClass="borderR borderB tdLeft">
                                        <cong:text name="innerPkgNwtPiece" id="innerPkgNwtPiece" styleClass="textlabelsBoldForTextBox" value="${lclBookingHazmat.innerPkgNwtPiece}" onkeyup="checkForNumberAndDecimal(this)"  maxlength="10"/>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Total Net Weight/Kgs:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="totalNetWeight" id="totalNetWeight" styleClass="textlabelsBoldForTextBox truncateTwoDigit" onkeyup="checkForNumberAndDecimal(this); " value="${lclBookingHazmat.totalNetWeight}" maxlength="10"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Emergency Contact:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="emergencyContact" id="emergencyContact" styleClass="textlabelsBoldForTextBox" value="CHEMTREC" style="text-transform: uppercase" maxlength="40"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Total Gross Weight/Kgs:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="totalGrossWeight" id="totalGrossWeight" styleClass="textlabelsBoldForTextBox mandatory truncateTwoDigit" onkeyup="checkForNumberAndDecimal(this)" value="${lclBookingHazmat.totalGrossWeight}" maxlength="10"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Emergency Phone No:</cong:td>
                                    <cong:td styleClass="borderR">
                                        <cong:text name="emergencyPhone" styleClass="textlabelsBoldForTextBox" id="emergencyPhone" value="703-527-3887" onkeyup="checkForNumberAndDecimal(this)" maxlength="12"/>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>
                                    
                                <cong:tr>
                                    <c:choose>
                                        <c:when test="${lclHazmatForm.moduleName ne 'Imports'}">
                                            <cong:td styleClass="borderB textlabelsBoldforlcl">Total Volume(Liter/Gals):</cong:td>
                                            <cong:td styleClass="borderB borderR">
                                                <cong:text name="liquidVolume" id="liquidVolume" styleClass="textlabelsBoldForTextBox truncateTwoDigit" value="${lclBookingHazmat.liquidVolume}" onkeyup="checkForNumberAndDecimal(this)" maxlength="10"/>

                                                <html:select styleClass="textlabelsBoldForTextBox smallDropDown" style="width:130px;" property="liquidVolumeLitreorGals" styleId="liquidVolumeLitreorGals" value="${lclBookingHazmat.liquidVolumeLitreorGals}">
                                                    <html:option value="" styleClass="tdLeft">Select</html:option>
                                                    <html:option value="Liters">LITERS</html:option>
                                                    <html:option value="Gallons">GALLONS</html:option>
                                                </html:select>
                                            </cong:td>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:td styleClass="borderB textlabelsBoldforlcl">Total Volume:</cong:td>
                                            <cong:td styleClass="borderB borderR">
                                                <cong:text name="liquidVolume" id="liquidVolume" styleClass="textlabelsBoldForTextBox" value="${lclBookingHazmat.liquidVolume}" onkeyup="checkForNumberAndDecimal(this)" maxlength="10"/>
                                            </cong:td>
                                        </c:otherwise>     
                                    </c:choose>
                                    <cong:td styleClass="borderB textlabelsBoldforlcl">EMS code:</cong:td>
                                    <cong:td styleClass="borderB borderR">
                                        <cong:text name="emsCode" id="emsCode" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclBookingHazmat.emsCode}" maxlength="20"/>
                                    </cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td>&nbsp;</cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                        <cong:td valign="top" width="250px">
                            <cong:table styleClass="textBoldforlcl">
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Reportable Quantity:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="reportableQuantity" id="reportableQuantityY" value="Y" 
                                               ${lclBookingHazmat.reportableQuantity ? 'checked':''}/>Y
                                        <input type="radio" name="reportableQuantity" id="reportableQuantityN" value="N"
                                               ${!lclBookingHazmat.reportableQuantity ? 'checked':''}/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Marine pollutant:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="marinePollutant" id="marinePollutantY" value="Y"  
                                               ${lclBookingHazmat.marinePollutant ? 'checked':''}/>Y
                                        <input type="radio" name="marinePollutant" id="marinePollutantN" value="N"
                                               ${!lclBookingHazmat.marinePollutant ? 'checked':''}/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Excepted Quantity:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="exceptedQuantity" id="exceptedQuantityY" value="Y" 
                                               ${lclBookingHazmat.exceptedQuantity ? 'checked':''}/>Y
                                        <input type="radio" name="exceptedQuantity" id="exceptedQuantityN" value="N"
                                               ${!lclBookingHazmat.exceptedQuantity ? 'checked':''}/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Limited Quantity:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="limitedQuantity" id="limitedQuantityY" value="Y" 
                                               ${lclBookingHazmat.limitedQuantity ? 'checked':''}/>Y
                                        <input type="radio" name="limitedQuantity" id="limitedQuantityN" value="N" 
                                               ${!lclBookingHazmat.limitedQuantity ? 'checked':''}/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Residue:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="residue" id="residueY" value="Y" ${lclBookingHazmat.residue ? 'checked':''}/>Y
                                        <input type="radio" name="residue" id="residueN" value="N" ${!lclBookingHazmat.residue ? 'checked':''}/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Inhalation Hazard:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="inhalationHazard" 
                                               id="inhalationHazardY" ${lclBookingHazmat.inhalationHazard ?'checked':''} value="Y"/>Y
                                        <input type="radio" name="inhalationHazard" 
                                               id="inhalationHazardN" ${!lclBookingHazmat.inhalationHazard ?'checked':''} value="N"/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td>
                                        <input type="button"  value="Free Form" onclick="freeFormPopUp('${path}', 'LCLBKG')"
                                               class="${empty lclBookingHazmat.hazmatDeclarations ? 'button-style1' : 'green-background'} freeFormat" id="freeForm"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Print on House B/L:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="printHouseBL" id="printHouseBLY" 
                                               ${lclBookingHazmat.printOnHouseBl ? 'checked':''} value="Y"/>Y
                                        <input type="radio" name="printHouseBL" id="printHouseBLN" 
                                               ${!lclBookingHazmat.printOnHouseBl ? 'checked':''} value="N"/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Print on SS Master B/L:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="printMasterBL" id="printMasterBLY" 
                                               ${lclBookingHazmat.printOnSsMasterBl ? 'checked':''} value="Y"/>Y
                                        <input type="radio" name="printMasterBL" id="printMasterBLN" 
                                               ${!lclBookingHazmat.printOnSsMasterBl ? 'checked':''} value="N"/>N
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Send with EDI master:</cong:td>
                                    <cong:td>
                                        <input type="radio" name="sendEdiMaster" id="sendEdiMasterY" 
                                               ${lclBookingHazmat.sendWithEdiMaster ? 'checked':''} value="Y"/>Y
                                        <input type="radio" name="sendEdiMaster" id="sendEdiMasterN" 
                                               ${!lclBookingHazmat.sendWithEdiMaster ? 'checked':''} value="N"/>N
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:div>
        </cong:div>
    </cong:form>
</body>

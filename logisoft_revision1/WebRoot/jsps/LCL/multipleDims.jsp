<%-- 
    Document   : multipleDims
    Created on : Oct 17, 2016, 2:46:48 PM
    Author     : NambuRajasekar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/currencyConverter.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/jsps/LCL/css/layout.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/jsps/LCL/css/lable-fields.css"/>
        <title>Multiple Dims</title>
    </head>

    <body>
    <html:form  action="/commodityDetails" name="commodityDetailsForm" styleId="commodityDetailsForm"
                type="com.gp.cvst.logisoft.struts.form.lcl.CommodityDetailsForm" scope="request"  method="post">

        <table width="97%">
            <thead>
            <html:hidden property="fileNumberId" styleId="fileNumberId" value="${commodityDetailsForm.fileNumberId}"/>
            <html:hidden property="bookingPieceId" styleId="bookingPieceId" value="${commodityDetailsForm.bookingPieceId}"/>
            <input type="hidden" name="uom" id="uom" value="${commodityDetailsForm.uom}"/>
            <input type="hidden" name="parentRowLen" id="parentRowLen" value="${parentRowLen}"/>
            <tr class="tableHeadingNew">
                <td width="60%">Commodity Details <span style="color:red">${commodityDetailsForm.fileNumber}</span></td>
                <td width="26%">
                    UOM
                    <input type="radio" name="actualUom" value="M" id="actualUomM" onclick="showHideMeasureLabel(this);
                            showValue();" />M
                    <input type="radio" name="actualUom" value="I" id="actualUomI" onclick="showHideMeasureLabel(this);
                            showValue();" />I
                    <input type="button" id="saveMul" value="Save" class="button-style1" onclick="saveMultiple();"/>
                </td>
            </tr>
            </thead>
        </table>

        <table width="100%" >
            <tr id="actualI" class="textBoldforlcl">
                <td width="17%">Length(IN)</td>
                <td width="17%">Width(IN)</td>
                <td width="17%">Height(IN)</td>
                <td width="14%">Pieces</td>
                <td width="15%">Weight/PC(LBS)</td>
                <td width="16%">WarehouseLine</td>
                <td>&nbsp;</td>
            </tr>
            <tr id="actualM" style="display:none" class="textBoldforlcl">
                <td width="17%">Length(CMS)</td>
                <td width="17%">Width(CMS)</td>
                <td width="17%">Height(CMS)</td>
                <td width="14%">Pieces</td>
                <td width="15%">Weight/PC(KGS)</td>
                <td width="16%">WarehouseLine</td>
                <td>&nbsp;</td>
            </tr>
        </table>
        <div id="nextInbox" >   
            <table width="100%" id="inboxTable">
                <tr class="defaultPP" style="display:none">
                    <td><input type="text" class="textsmall mandatory weight acLength" name="actualLength" id="actualLength" onkeyup="checkForNumberAndDecimal(this)"/></td>
                    <td><input type="text" class="textsmall mandatory weight acWidth" name="actualWidth" id="actualWidth" onkeyup="checkForNumberAndDecimal(this)"/></td>
                    <td><input type="text" class="textsmall mandatory weight acHeight" name="actualHeight" id="actualHeight" onkeyup="checkForNumberAndDecimal(this)"/></td>
                    <td><input type="text" class="textsmall mandatory pieces" name="pieceCount" id="pieceCount"  onkeyup="checkForNumber(this)"/></td>
                    <td><input type="text" class="textsmall weight acWeight" name="actualWeight" id="actualWeight" onkeyup="checkForNumberAndDecimal(this)"/></td>
                    <td><input type="text" class="textsmall warehouseTab" style="text-transform: uppercase" name="warehouse" id="warehouse" /></td>
                    <td><img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removePP"/></td>
                </tr>  
            </table>
        </div> 
    </html:form> 
</body>
<script type="text/javascript" src="${path}/jsps/LCL/js/multipleDimsQtBkgsCommon.js"/></script> 
</html>
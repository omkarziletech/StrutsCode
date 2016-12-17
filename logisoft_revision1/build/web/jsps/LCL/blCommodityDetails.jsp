<%-- 
    Document   : commodityDetails
    Created on : Jan 29, 2012, 1:19:34 PM
    Author     : lakshh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>

<html>
    <style type="text/css">
        .text-border{
            border: none;
        }
        .measure-met{
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function(){
            $('#actualLength').focus().val();
            $('#saveDetail').click(function(){
                var error = true;
                $(".mandatory").each(function(){
                    if($(this).val().length == 0){
                        $.prompt('This field is required');
                        error = false;
                        $(this).css("border-color","red");
                        $(this).focus();
                        return false;
                    }
                });
                if(error){
                    showLoading();
                    $('#methodName').val('addDetails');
                    $('#blCommodityDetailsForm').submit();
                }
            });
            if($("#actualUomI").is(":checked")){
                var rowslen = document.getElementById("dimsTable").rows.length;
                if(rowslen>1){
                    //$('#actualUomM').attr('disabled', true);
                    changeUom();
                }
            }
            if($("#actualUomM").is(":checked")){
                var rowlen = document.getElementById("dimTable").rows.length;
                if(rowlen>1){
                    changeUom();
                }
            }
            var uom = $('#uom').val();
            if(uom=='M'){
                var rowlen = document.getElementById("dimTable").rows.length;
                if(rowlen>1){
                    $('.measure-imp').hide();
                    $('.measure-met').show();
                    $('#impDetailList').hide();
                    $('#metDetailList').show();
                    $('#actualM').show();
                    $('#actualI').hide();
                }
            }if(uom=='I'){
                $('.measure-imp').show();
                $('.measure-met').hide();
                $('#impDetailList').show();
                $('#metDetailList').hide();
                $('#actualM').hide();
                $('#actualI').show();
            }

        });
    </script>
    <body>
        <cong:form name="blCommodityDetailsForm" id="blCommodityDetailsForm" action="/blCommodityDetails.do">
            <%--<input type="hidden" name="id" id="id" value="${id}"/>--%>
            <cong:hidden name="blPieceId" id="blPieceId" value="${blCommodityDetailsForm.blPieceId}"/>
            <cong:hidden name="pieceDetailId" id="pieceDetailId" value="${blCommodityDetailsForm.pieceDetailId}"/>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${blCommodityDetailsForm.fileNumber}"/>
            <cong:hidden name="dimFlag" value="${blCommodityDetailsForm.dimFlag}"/>
            <cong:div styleClass="tableHeadingNew" style="width:97%">Commodity Details <span style="color:red">${blCommodityDetailsForm.fileNumber}</span>
                <cong:div styleClass="floatRight" style="width:20%">UOM
                    <cong:radio name="actualUom" value="M" id="actualUomM" onclick="showHideMeasureLabel(this);showValue();"/>M
                    <cong:radio name="actualUom" value="I" id="actualUomI" onclick="showHideMeasureLabel(this);showValue();"/>I
                    <cong:hidden name="uom" id="uom" value="${blCommodityDetailsForm.uom}"/>
                </cong:div>
            </cong:div>
            <cong:table width="100%" style="margin:5px 0; float:left">
                <cong:tr id="actualI" styleClass="textBoldforlcl">
                    <cong:td width="12%">Length(IN)</cong:td>
                    <cong:td width="12%">Width(IN)</cong:td>
                    <cong:td width="12%">Height(IN)</cong:td>
                    <cong:td width="12%">Pieces</cong:td>
                    <cong:td width="12%">Weight/PC(LBS)</cong:td>
                    <cong:td  width="12%">Warehouse Line</cong:td>
                    <cong:td width="8%"></cong:td>
                </cong:tr>
                <cong:tr id="actualM" style="display:none" styleClass="textBoldforlcl">
                    <cong:td width="12%">Length(CMS)</cong:td>
                    <cong:td width="12%">Width(CMS)</cong:td>
                    <cong:td width="12%">Height(CMS)</cong:td>
                    <cong:td width="12%">Pieces</cong:td>
                    <cong:td width="12%">Weight/PC(KGS)</cong:td>
                    <cong:td width="12%">Warehouse Line</cong:td>
                    <cong:td width="8%"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualLength" id="actualLength" value="${blPieceDetail.actualLength}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualWidth" id="actualWidth" value="${blPieceDetail.actualWidth}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualHeight" id="actualHeight" value="${blPieceDetail.actualHeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory" name="pieceCount" id="pieceCount" value="${blPieceDetail.pieceCount}" container="NULL" onkeyup="checkForNumber(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall weight" name="actualWeight" id="actualWeight" value="${blPieceDetail.actualWeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall" style="text-transform: uppercase" name="warehouse" id="warehouse" value="${blPieceDetail.stowedLocation}" container="NULL"/></cong:td>
                    <cong:td width="5%"><input type="button"  value="Add" class="button-style1" id="saveDetail"/>  </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="7">
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="measure-imp"  width="17%">Total Measure(I):</cong:td>
                                <cong:td styleClass="measure-imp"  width="22%">
                                    <cong:text name="totalMeasureImp" styleClass="textsmall text-border" readOnly="true"
                                               container="NULL"  id="totalMeasureImp"
                                               value="${blCommodityDetailsForm.totalMeasureImp}"/>(CFT)
                                </cong:td>
                                <cong:td styleClass="measure-met"  width="17%">Total Measure(M):</cong:td>
                                <cong:td styleClass="measure-met"  width="22%">
                                    <cong:text name="totalMeasureMet" styleClass="textsmall text-border"
                                               readOnly="true" container="NULL"  id="totalMeasureMet"
                                               value="${blCommodityDetailsForm.totalMeasureMet}"/>(CBM)
                                    <!--                                    Measure(M)=Measure(I)/35.3146-->
                                </cong:td>
                                <cong:td styleClass="measure-imp"  width="16%">Total Weight(I):</cong:td>
                                <cong:td styleClass="measure-imp"  width="22%">
                                    <cong:text name="totalWeightImp" styleClass="textsmall text-border"
                                               readOnly="true" container="NULL"  id="totalWeightImp"
                                               value="${blCommodityDetailsForm.totalWeightImp}"/>(LBS)
                                </cong:td>
                                <cong:td styleClass="measure-met" width="38%">Total Weight(M):
                                    <cong:text name="totalWeightMet" styleClass="textsmall text-border"
                                               readOnly="true" container="NULL"  id="totalWeightMet"
                                               value="${blCommodityDetailsForm.totalWeightMet}"/>(KGS)
                                </cong:td>
                                <cong:td width="13%">Total Pieces:</cong:td>
                                <cong:td>
                                    <cong:text name="totalPieces" styleClass="textsmall text-border" 
                                               readOnly="true" container="NULL"  id="totalPieces"
                                               value="${blCommodityDetailsForm.totalPieces}"/>
                                </cong:td>
                                <cong:td width="10%">
                                    <c:choose>
                                        <c:when test="${blCommodityDetailsForm.dimFlag eq 'booked'}">
                                            <cong:div id="bookedAction">
                                                <input type="button"  class="button-style1" value="Save" onclick="saveDetails('updateBookedComm','#blCommodityDetailsForm','#bookedField')"/>
                                            </cong:div>
                                        </c:when>
                                        <c:when test="${blCommodityDetailsForm.dimFlag eq 'actual'}">
                                            <cong:div id="actualAction">
                                                <input type="button"  class="button-style1" value="Save" onclick="saveDetails('updateActualComm','#blCommodityDetailsForm','#actualField')"/>
                                            </cong:div>
                                        </c:when>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="7">
                        <cong:div id="impDetailList"  style="width:100%">
                            <table width="100%" border="0" id="dimsTable"  class="dataTable">
                                <thead>
                                    <tr>
                                        <th>Length(IN)</th>
                                        <th>Width(IN)</th>
                                        <th>Height(IN)</th>
                                        <th>Weight Per Piece(LBS)</th>
                                        <th>Piece</th>
                                        <th>Warehouse Line</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="count" value="0"/>
                                    <c:forEach items="${detailListImp}" var="details">
                                        <c:set var="count" value="${count+1}"/>
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>${details.actualLength}</td>
                                            <td>${details.actualWidth}</td>
                                            <td>${details.actualHeight}</td>
                                            <td>${details.actualWeight}</td>
                                            <td>${details.pieceCount}</td>
                                            <td style="text-transform: uppercase">${details.stowedLocation}</td>
                                            <td>
                                                <cong:img src="${path}/images/edit.png" styleClass="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit"
                                                          onclick="editDetails('${path}','${details.id}','${details.lclBlPiece.id}','${blCommodityDetailsForm.dimFlag}','${details.actualUom}');"/>
                                                <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                          onclick="deleteDetail('Are you sure you want to delete?','${details.id}');"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </cong:div>
                        <cong:div id="metDetailList" style="display:none;width:100%;">
                            <table width="100%" border="0"  class="dataTable" id="dimTable">
                                <thead>
                                    <tr>
                                        <th>Length(CM)</th>
                                        <th>Width(CM)</th>
                                        <th>Height(CM)</th>
                                        <th>Weight Per Piece(KGS)</th>
                                        <th>Piece</th>
                                        <th>Warehouse Line</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <c:set var="count" value="0"/>
                                <tbody>
                                    <c:forEach items="${detailListMet}" var="details">
                                        <c:set var="count" value="${count+1}"/>
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>${details.actualLength}</td>
                                            <td>${details.actualWidth}</td>
                                            <td>${details.actualHeight}</td>
                                            <td>${details.actualWeight}</td>
                                            <td>${details.pieceCount}</td>
                                            <td style="text-transform: uppercase">${details.stowedLocation}</td>
                                            <td>
                                                <cong:img src="${path}/images/edit.png" styleClass="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit"
                                                          onclick="editDetails('${path}','${details.id}','${details.lclBlPiece.id}','${blCommodityDetailsForm.dimFlag}','${details.actualUom}');"/>
                                                <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                          onclick="deleteDetail('Are you sure you want to delete?','${details.id}');"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </cong:div>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <input type="hidden" name="actualWeightMetric" id="actualWeightMetric"/>
            <input type="hidden" name="actualVolumeMetric" id="actualVolumeMetric"/>
            <input type="hidden" name="actualWeightVolume" id="actualWeightImperial"/>
            <input type="hidden" name="actualVolumeImperial" id="actualVolumeImperial"/>
        </cong:form>
    </body>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#actualWeightMetric').val( parent.$('#actualWeightMetric').val())
            $('#actualVolumeMetric').val(parent.$('#actualVolumeMetric').val())
            $('#actualWeightVolume').val(parent.$('#actualWeightImperial').val())
            $('#actualVolumeImperial').val( parent.$('#actualVolumeImperial').val())
        });

        function saveDetails(methodName,formName,selector){
               
            var actualUom = $('input:radio[name=actualUom]:checked').val();
            var rowlen = "";
            if (actualUom == 'I') {
                rowlen = document.getElementById("dimsTable").rows.length;
            } else {
                rowlen = document.getElementById("dimTable").rows.length;
            }
            if (rowlen != "" && rowlen > 1) {
                var bkgWeightMetric=$("#actualWeightMetric").val();
                var bkgVolumeMetric=$("#actualVolumeMetric").val();
                var bkgWeightImperial=$("#actualWeightImperial").val();
                var bkgVolumeImperial=$("#actualVolumeImperial").val();
                if((bkgWeightMetric==null || bkgWeightMetric=="") && (bkgVolumeMetric==null || bkgVolumeMetric=="")
                    && (bkgWeightImperial==null || bkgWeightImperial=="") && (bkgVolumeImperial==null || bkgVolumeImperial=="")){
                    saveDetailInAjax(methodName,formName,selector)
                }else{
                    $.prompt('Do you want to Override Existing Values?',{
                        buttons:{
                            Yes:1,
                            No:2
                        },
                        submit:function(v){
                            if(v==1){
                                saveDetailInAjax(methodName,formName,selector);
                                $.prompt.close();
                            }
                            else if(v==2){
                                $('#actualLength').focus().val();
                                $.prompt.close();
                            }
                        }
                    });
                }
            }else{
                $.prompt('Please add atleast one commodity Details');
            }
        }
        function saveDetailInAjax(methodName,formName,selector){
            showProgressBar();
            parent.showBlDimsDetails();
            parent.$('#actualPieceCount').val($('#totalPieces').val());
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            $.post($(formName).attr("action"),params,
            function(data) {
                $(selector).html(data);
                $(selector,window.parent.document).html(data);
                hideProgressBar();
                parent.$.fn.colorbox.close();
            });
        }
        function submitForm(methodName,formName){
            $('#methodName').val(methodName);
            $(formName).submit();
        }
        function editDetails(path,pieceDetailId,blPieceId,dimFlag,uom){
            showLoading();
            var fileNumber=$('#fileNumber').val();
            var url=path+"/blCommodityDetails.do?methodName=editDetails&pieceDetailId="+pieceDetailId+"&blPieceId="+blPieceId+"&dimFlag="+dimFlag+"&uom="+uom+"&fileNumber="+fileNumber;
            window.location=url;
            changeUom();
        }
        function deleteDetails(id){
            $('#id').val(id);
        }
        function deleteDetail(txt,pieceDetailId){
            $.prompt(txt,{
                buttons:{
                    Yes:1,
                    No:2
                },
                submit:function(v){
                    if(v==1){
                        showLoading();
                        $('#pieceDetailId').val(pieceDetailId)
                        $('#methodName').val('deleteDetails');
                        $('#blCommodityDetailsForm').submit();
                        $.prompt.close();
                    }
                    else if(v==2){
                        $.prompt.close();
                    }
                }
            });
        }
        function showHideMeasureLabel(obj){
            var val=obj.value;
            if(val=='M'){
                $('.measure-imp').hide();
                $('.measure-met').show();
                $('#impDetailList').hide();
                $('#metDetailList').show();
                $('#uom').val(val);
            }
            if(val=='I'){
                $('.measure-imp').show();
                $('.measure-met').hide();
                $('#impDetailList').show();
                $('#metDetailList').hide();
                $('#uom').val(val);
            }
        }
        function sampleAlert(txt){
            $.prompt(txt);
        }
        function checkForNumberAndDecimal(obj){
            var result;
            if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
                obj.value="";
                sampleAlert("This field should be Numeric");
                             
            }
        }function checkForNumber(obj){
            var result;
            if(!/^\+?[0-9]*\.?[0-9]+$/.test(obj.value)){
                obj.value="";
                sampleAlert("This field should be Numeric");
                             
            }
        }
        function showValue(){
            if($("#actualUomM").is(":checked")){
                $('#actualM').show();
                $('#actualI').hide();
            }
            if($("#actualUomI").is(":checked")){
                $('#actualI').show();
                $('#actualM').hide();
            }
        }
            
        function changeUom(){
            var uom=$('#uom').val();
            if(uom == 'M'){
                $('#actualUomM').attr('checked',true);
                $('#actualUomI').attr('disabled', true);
            }
            if(uom == 'I'){
                $('#actualUomI').attr('checked',true);
                $('#actualUomM').attr('disabled', true);
            }
        }
        
    </script>
</html>




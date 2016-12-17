<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<body style="background:#ffffff">
    <cong:div style="width:99%; float:left;">
        <cong:form name="lclBarrelForm" id="lclBarrelForm" action="/lclBarrel.do">
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:table style="width:100%">
                <cong:tr>
                    <cong:td styleClass="tableHeadingNew" >
                        <cong:div styleClass="floatLeft">Barrel for File No: <span class="fileNo">${lclBarrelForm.fileNumber}</span>  </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <cong:table width="70%" style="margin:5px 0; float:left">
                            <cong:tr>
                                <cong:td>Piece</cong:td>
                                <cong:td>Weight</cong:td>
                                <cong:td>Measure</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td><cong:text styleClass="text mandatory" name="bookedPieceCount" id="bookedPieceCount" value="${lclBookingPiece.bookedPieceCount}" container="NULL"/></cong:td>
                                <cong:td><cong:text styleClass="text mandatory weight" name="bookedWeightImperial" id="bookedWeightImperial" value="${bookingPieceDetail.actualLength}" container="NULL"/></cong:td>
                                <cong:td><cong:text styleClass="text mandatory weight" name="bookedVolumeImperial" id="bookedVolumeImperial" value="${bookingPieceDetail.actualLength}" container="NULL"/></cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>

            <cong:table style="width:100%">
                <cong:tr>
                    <cong:td styleClass="tableHeadingNew" >
                        <cong:div styleClass="floatLeft">Non-Barrel for File No: <span class="fileNo">${lclBarrelForm.fileNumber}</span>  </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <cong:table width="70%" style="margin:5px 0; float:left">
                            <cong:tr>
                                <cong:td>Piece</cong:td>
                                <cong:td>Weight</cong:td>
                                <cong:td>Measure</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td><cong:text styleClass="text mandatory" name="actualPieceCount" id="actualPieceCount" value="${bookingPieceDetail.actualLength}" container="NULL"/></cong:td>
                                <cong:td><cong:text styleClass="text mandatory weight" name="actualWeightImperial" id="actualWeightImperial" value="${bookingPieceDetail.actualLength}" container="NULL"/></cong:td>
                                <cong:td><cong:text styleClass="text mandatory weight" name="actualVolumeImperial" id="actualVolumeImperial" value="${bookingPieceDetail.actualLength}" container="NULL"/></cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <input type="button" value="Save" class="button-style1" onclick="submitForm('save','#lclBarrelForm')"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitForm(methodName,formName){
            $("#methodName").val(methodName);
            $(formName).submit();
            parent.$.fn.colorbox.close();
        }
    </script>
</body>

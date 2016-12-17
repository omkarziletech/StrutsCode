<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  id="lclTerminatePopupForm"  name="lclTerminatePopupForm" action="lclTerminatePopup.do" >
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Terminate for File No:<span style="color:red">${lclTerminatePopupForm.fileNumber}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr><cong:td width="50%">
                <cong:table style="font-size: 14px" width="80%" border="0" align="center">
                    <cong:hidden id="fileNumber" name="fileNumber"/>
                    <cong:hidden id="fileId" name="fileId"/>
                    <cong:hidden id="methodName" name="methodName"/>
                    <cong:hidden id="originCityName" name="originCityName"/>
                    <input type="hidden" name="invoiceFlag" id="invoiceFlag" value="${arInvoiceFlag}"/>
                    <input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"><%-- Login User Id--%>
                    <input type="hidden" id="moduleName" name="moduleName" value="${lclTerminatePopupForm.moduleName}">
                    <cong:tr>
                        <cong:td align="center" styleClass="textlabelsBoldforlcl" >Would you like to Terminate this Cargo?
                        </cong:td>
                    </cong:tr>
                </cong:table>
                        <cong:table>
                            <cong:tr>
                                <cong:td width="35%"></cong:td>
                                <cong:td>
                                    <input type="radio" value="Cancelled by Customer" name="terminate"/>Cancelled by Customer
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td></cong:td><cong:td><input type="radio" name="terminate" value="Cargo Picked Up" onclick="terminateCargo()"/>Cargo Picked Up</cong:td>
                            </cong:tr>
                            <cong:tr><cong:td></cong:td><cong:td><input type="radio" name="terminate" value="New Booking Issued"/>New Booking Issued</cong:td></cong:tr>
                            <c:choose>
                                <c:when test="${lclTerminatePopupForm.moduleName=='Exports'}">
                                    <cong:tr>
                                        <cong:td>
                                        </cong:td>
                                        <cong:td><input type="radio" name="terminate" value="Consolidated"/>Consolidated
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td>
                                        </cong:td>
                                        <cong:td><input type="radio" name="terminate" value="Duplicated"/>Duplicated
                                        </cong:td>
                                    </cong:tr>
                                        <cong:tr>
                                        <cong:td>
                                        </cong:td>
                                        <cong:td><input type="radio" name="terminate" value="Converted To Air"/>Converted To Air
                                        </cong:td>
                                    </cong:tr>
                                        <cong:tr>
                                        <cong:td>
                                        </cong:td>
                                        <cong:td><input type="radio" name="terminate" value="Salvage"/>Salvage
                                        </cong:td>
                                    </cong:tr>
                                        <cong:tr>
                                        <cong:td>
                                        </cong:td>
                                        <cong:td><input type="radio" name="terminate" value="Other"/>Other
                                        </cong:td>
                                    </cong:tr>
                                    <c:choose> 
                                     <c:when test="${isConsolidate}">
                                            <cong:tr><cong:td colspan="2">&nbsp;</cong:td></cong:tr><cong:tr><cong:td></cong:td><cong:td>                               &nbsp;&nbsp;Do you want to terminate ALL bookings in this consolidation
                                                </cong:td> 
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td></cong:td>
                                                <cong:td> 
                                                    <input type="radio" id ="consoTerminate" name="consoTerminate"  value="Y"  /> Yes
                                                    <input type="radio" id ="consoTerminate" name="consoTerminate"  value="N" checked /> No
                                                </cong:td>
                                            </cong:tr> 
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="consoTerminate" id="consoTerminate" value=""  />   
                                        </c:otherwise>
                                    </c:choose>
                                            
                                </c:when>

                            </c:choose>
                        </cong:table>
            </cong:td>
            <cong:td width="50%">
                <cong:table > <cong:tr> <cong:td>Comment<span class="red bold" style="font-size: medium">*</span>
                        </cong:td></cong:tr>
                    <cong:tr>
                        <cong:td>   <cong:textarea rows="6" cols="30" styleClass="textLCLuppercase" style="width:300px;resize:none;" name="termComment" id="termComment" value=""></cong:textarea></cong:td>
                    </cong:tr></cong:table> </cong:td>
        </cong:tr>
    </cong:table>
    <br/>
    <cong:table>
        <cong:tr><cong:td>
                <cong:div styleClass="button-style1" onclick="lclDomTerminationStatus('${path}')"> Terminate </cong:div>
                <cong:div styleClass="button-style1" onclick="closepopUp()"> Cancel </cong:div>
            </cong:td></cong:tr>
    </cong:table>
</cong:form>
<script type="text/javascript">
    function lclDomTerminationStatus(path){
        var terminateOption=$('input:radio[name=terminate]:checked').val();
        var consoTerminate=$('input:radio[name=consoTerminate]:checked').val();
        consoTerminate=(consoTerminate!==undefined)?consoTerminate:"";
        var fileId=$('#fileId').val();
        var fileNo=$('#fileNumber').val();
        var comment=$('#termComment').val();
        var invoiceFlag=$("#invoiceFlag").val();
        if(comment===null || comment==="" || comment===undefined){
            $.prompt("Comment Is Required");
        }
        else if(terminateOption===null || terminateOption==="" || terminateOption===undefined){
            $.prompt("Please Select atleast one Terminate Option");
        }else{

            if(invoiceFlag==="true" && terminateOption==="Cancelled by Customer"){
                var href=path+"/lclArInvoice.do?methodName=displayManualRates&fileNumberId="+fileId+"&fileNumber="+fileNo+"&terminate=Terminate&comment="+comment+"&consoTerminate="+consoTerminate;
                parent.$.colorbox({
                    iframe:true,
                    href:href,
                    width:"85%",
                    height:"85%",
                    title:"AR Invoice"
                });
            }else{
                var userId=$("#loginUserId").val();
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "lclDomTermination",
                        param1: fileId,
                        param2: "X",
                        param3: terminateOption,
                        param4: comment,
                        param5: userId,
                        param6: consoTerminate
                    },
                    async:false,
                    success: function(data) {
                        if(data==="true"){
                            parent.jQuery("#fileStatus").val('T');
                            parent.jQuery("#statuslabel").text("Terminated");
                            parent.$("#lclDomTermination").hide();
                            parent.$("#lclDomTermination1").hide();
                            parent.$("#lclUnTermination").show();
                            parent.$("#lclUnTermination1").show();
                            window.parent.showLoading();
                            parent.$("#methodName").val('saveBooking');
                            parent.$("#lclBookingForm").submit();
                            parent.$.colorbox.close();
                        }
                    }
                });
            }
        }
    }
    function closepopUp(){
        parent.$.colorbox.close();
    }
    function terminateCargo(){
        $.prompt("Make sure you bill customer for handling!");
    }
</script>

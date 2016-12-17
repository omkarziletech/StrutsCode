<%-- 
    Document   : lclDispositionPopUp
    Created on : Oct 11, 2016, 7:00:22 PM
    Author     : rathnapandian_G
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <%@include file="init.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <body style="overflow:auto">
        <cong:form  action="lclBooking.do" name="lclBookingForm" id="lclBookingForm" >
            <cong:hidden id="fileNumberId" name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
            <input type="hidden" id="userId" name="userId" value="${loginuser.userId}"/>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">
                        File No:# <span style="color: red;">${lclBookingForm.fileNumber}</span>
                    </td>
                </tr>
            </table>
            <br/>
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr styleClass="textBoldforlcl">
                    <cong:td>Disposition</cong:td>
                    <cong:td>City Location</cong:td>
                    <cong:td>WareHouse Code</cong:td>
                    <cong:td>Date&Time</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <cong:autocompletor name="dispoPopUpId" id="dispoPopUpId" template="two" width="250" container="NULL"
                                            query="DISPOSITION" fields="NULL,dispositionId"  shouldMatch="true" scrollHeight="150px" 
                                            />
                        <input type="hidden" id="dispositionId" name="dispositionId" />
                    </cong:td>
                    <cong:td>
                        <cong:autocompletor name="dispoCityCode" id="dispoCityCode" template="two" width="250" container="NULL"
                                            query="PORT"  
                                            fields="NULL,NULL,NULL,currentLocation"
                                            scrollHeight="150px" />  
                        <input type="hidden" id="currentLocation" name="currentLocation" /> 

                    </cong:td>
                    <cong:td >
                        <cong:autocompletor id="dispoWareHouse" name="dispoWareHouse" width="250" container="NULL"
                                            query="EXPORT_DELIVER_CARGO" fields="NULL,NULL,NULL,NULL,NULL,NULL,NULL,warhseId" scrollHeight="150px" />
                        <input type="hidden" id="warhseId" name="warhseId"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl">
                        <cong:calendarNew styleClass="textlabelsBoldForTextBoxsizeWidth152" id="dispoDateTime" is12HrFormat="true" showTime="true"  
                                          name="dispoDateTime"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:table width="100%" border="0" align="center">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td width="44%"></cong:td>
                    <cong:td align="center">
                        <input type="button" class="button-style1" align="center" value="Save" id="saveDispo" onclick="saveDispoPopUp();"
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:form>

        <script type='text/javascript'>
            jQuery(document).ready(function () {
                document.getElementById('dispoDateTime').value = Date();
            }
            )
            function saveDispoPopUp() {
                var fileId = $("#fileNumberId").val();
                var dispositionPopUp = $("#dispositionId").val();
                var dispoCityCode = $("#currentLocation").val();
                var dispoWhseId = $("#warhseId").val();
                var userId = $("#userId").val();
                if (dispositionPopUp === '') {
                    $.prompt("Please enter Disposition");
                    return false;
                }
                if (dispoCityCode === "" && $("#dispoPopUpId").val() !== 'OBKG') {
                    $.prompt("Please enter City Location");
                    return false;

                }

                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.struts.action.lcl.LclBookingAction",
                        methodName: "saveDispositionPopUp",
                        param1: fileId,
                        param2: dispositionPopUp,
                        param3: dispoCityCode,
                        param4: dispoWhseId,
                        param5: userId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        window.parent.showLoading();
                        parent.$("#methodName").val("editBooking");
                        parent.$("#lclBookingForm").submit();
                    }

                });
            }
        </script>
    </body>
</html>
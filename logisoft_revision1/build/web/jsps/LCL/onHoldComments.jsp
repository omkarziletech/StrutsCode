<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="colorBox.jsp" %>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            File No: <span class="fileNo">${lclRemarksForm.fileNumber} </span><span id="refusedValidate"/>
        </cong:div>
        <br><br>
        <cong:form name="lclRemarksForm" id="lclRemarksForm" action="lclRemarks.do">
            <c:set var="holdFlag" value="${lclRemarksForm.hold}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${lclRemarksForm.fileNumber}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclRemarksForm.fileNumberId}"/>
            <input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}">
            <input type="hidden" id="previousValue" name="previousValue" value="${holdFlag ? 'Y':'N'}">
            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Hold</cong:td>
                    <cong:td>
                        
                        <input type="radio" name="hold" id="holdY" value="Y" ${holdFlag ? 'checked':''} onclick="hold();"/> Yes
                        <input type="radio" name="hold" id="holdN" value="N" ${!holdFlag ? 'checked':''} onclick="unhold();"/> No
                        <input type="hidden" name="previousValue" id="previousValue" value="${lclRemarksForm.hold}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Comments</cong:td>
                    <cong:td>
                        <textarea rows="4" cols="50" id="noteDesc" style="text-transform: uppercase" onblur="validateMaxLength(this, 500)"></textarea>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td>
                        <input type="button" class="button-style1" value="Save" onclick="submitOnHold()"/>
                        <input type="button" class="button-style1" value="Cancel"  onclick="closeOnHold();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitOnHold() {
            var previousValue = $("#previousValue").val();
            var hold = $('input:radio[name=hold]:checked').val();
            if(previousValue === hold){
                  parent.$.colorbox.close();
            }else{ 
                var comments = $("#noteDesc").val();
                if(comments === ''){
                    $.prompt("Please Enter Comments");
                    return;
                }else{
                    var fileId = $("#fileNumberId").val();
                    var userId = $("#loginUserId").val();
                    $.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "saveOnHoldNotes",
                            param1: comments,
                            param2: hold,
                            param3: fileId,
                            param4: userId,
                            dataType: "json"
                        },
                        async: false,
                        success: function (data) {
                            if(data === 'Y'){
                                parent.$( "#holdButton1" ).removeClass("button-style1").addClass("red-background");
                                parent.$( "#holdButton2" ).removeClass("button-style1").addClass("red-background");
                                parent.$( "#holdButton1" ).text("UnHold");
                                parent.$( "#holdButton2" ).text("UnHold");
                                parent.$( "#hold" ).val(data);
                            }else if(data === 'N'){
                                parent.$( "#holdButton1" ).removeClass("red-background").addClass("green-background");
                                parent.$( "#holdButton2" ).removeClass("red-background").addClass("green-background");
                                parent.$( "#holdButton1" ).text("Hold");
                                parent.$( "#holdButton2" ).text("Hold");
                                parent.$( "#hold" ).val(data);
                            }
                            parent.$.colorbox.close();
                        }
                    });
                }
            }
        }
        
        
        function closeOnHold() {
            parent.$.colorbox.close();
        }
    </script>
</body>


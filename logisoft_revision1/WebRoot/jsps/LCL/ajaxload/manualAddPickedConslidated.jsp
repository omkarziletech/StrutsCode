<%@include file="../init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <body>
        <input type="hidden" name="pod" id="pod" value="${destination}"/>
        <input type="hidden" name="fd" id="fd" value="${finalDest}"/>
        <input type="hidden" name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
        <cong:div id="displayPickedConsolidateDr">
            <cong:table width="100%" border="0" cellpadding="2" cellspacing="0" styleClass="tableBorderNew">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td>File No:<span class="fileNo">${fileNumber}</span></cong:td>  
                </cong:tr>
            </cong:table> <br/>
            <cong:table width="100%" border="0" cellpadding="2" cellspacing="0">
                <cong:tr>
                    <cong:td colspan="2" >
                        <cong:table > 
                            <c:forEach var="i" begin="1" end="5">
                                <cong:tr>
                                    <c:forEach var="j" begin="1" end="8">
                                        <cong:td>
                                            <input type="text" name="drNumber"  class="textlabelsBoldForTextBox" style="width:85px; height: 22px;" maxlength="6"/>
                                        </cong:td>
                                    </c:forEach>
                                </cong:tr>
                            </c:forEach>
                        </cong:table> 
                    </cong:td>
                    <cong:td>
                        <input type="button" class="button-style1" value="save" onclick="savePickedDrToConsolidate()" id="saveCode" />
                    </cong:td>
                </cong:tr>
            </cong:table></cong:div>
    </body>
</html>
<script type="text/javascript">
//    function addPickedDrToConsolidate() {
//        if ($("#pickedConsolidateDr").val() === "") {
//            $.prompt("Please select atleast one record for consolidation");
//            return false;
//        } else if ($("#state").val() === "BL") {
//            $.prompt("DR has a BL, you cannot add to consolidation");
//            $("#pickedConsolidateDr").val("");
//            return false;
//        } else {
//            displayConsolidateFile();
//        }
//    }

    function savePickedDrToConsolidate() {
        var fileNumberId = $("#fileNumberId").val();
        var values = [];
        $("input[name='drNumber']").each(function () {
            if ($(this).val() !== '') {
                values.push($(this).val().toUpperCase());
            }
        });
        var fileNumbers = values.toString();
        if (fileNumbers === "") {
            $.prompt("Please select atleast one record for consolidation");
            return false;
        }
        var results = validateFilesToConsolidate(fileNumberId, fileNumbers);
        if (results.fileNumbers !== "") {
            var txt = "The following DRs will not be consolidated :" + "</br>" + results.errorMsg;
            $.prompt(txt, {
                buttons: {
                    Ok: 1,
                    Cancel: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        ignoreConsolidateFile(results.fileNumbers, fileNumbers);
                    } 
                }
            });
        } else {
            convertFileNumberId(fileNumbers);
        }
    }

    function ignoreConsolidateFile(errorFileNo, fileNumbers) {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "ignoreConsolidateFile",
                param1: errorFileNo,
                param2: fileNumbers,
                dataType: "json"
            },
            preloading: true,
            async: false,
            success: function (data) {
                if(data !== "") {
                parent.$("#pickedConsolidate").val(data);
                parent.$.fn.colorbox.close();
                parent.addConsolidate('saveConsolidate', '#lclConsolidateForm', '#consolidateDesc');
                } else {
                parent.$.fn.colorbox.close();    
                parent.sampleAlert("Please select atleast one record for consolidation");
                }
            }
        });
    }
    
    function convertFileNumberId(fileNumbers) {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "convertFileNumberId",
                param1: fileNumbers,
                dataType: "json"
            },
            preloading: true,
            async: false,
            success: function (data) {
                if(data !== "") {
                parent.$("#pickedConsolidate").val(data);
                parent.$.fn.colorbox.close();
                parent.addConsolidate('saveConsolidate', '#lclConsolidateForm', '#consolidateDesc');
                } else {
                parent.$.fn.colorbox.close();    
                parent.sampleAlert("Please select atleast one record for consolidation");    
                }
            }
        });
    }

    function validateFilesToConsolidate(fileNumberId, fileNumbers) {
        var results = "";
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "validateFilesToConsolidate",
                param1: fileNumberId,
                param2: fileNumbers,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                results = data;
            }
        });
        return results;
    }

    function displayConsolidateFile() {
        parent.$("#methodName").val("displayPickedConsolidateDr");
        parent.$("#pickedConsolidateDr").val($("#pickedConsolidateDr").val());
        var params = parent.$("#lclConsolidateForm").serialize();
        $.post(parent.$("#lclConsolidateForm").attr("action"), params,
                function (data) {
                    $("#pickedConsolidateDr").val("");
                    $("#displayPickedConsolidateDr").html(data);
                    if ($("#duplicateFileNo").val() !== "") {
                        $.prompt("This DR <span style=color:red>" + $('#duplicateFileNo').val() + "</span>" + " Already Exit.");
                    }
                });
    }
    function removeConsolidateFile(fileNo) {
        parent.$("#methodName").val("removePickedConsolidateDr");
        parent.$("#pickedConsolidateDr").val(fileNo);
        var params = parent.$("#lclConsolidateForm").serialize();
        $.post(parent.$("#lclConsolidateForm").attr("action"), params,
                function (data) {
                    $("#displayPickedConsolidateDr").html(data);
                });
    }
</script>

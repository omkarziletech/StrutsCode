<%@include file="init.jsp" %>
<%@include file="/taglib.jsp" %>
<style>
    .text{
        border: 1px solid #BCBCBC;
        color: #353535;
        font: 11px Arial,Tahoma,Helvetica,FreeSans,sans-serif;
        width: 330px;
    }
</style>
<body>
    <cong:form name="lclBookingHazmatFreeForm" id="lclBookingHazmatFreeForm" action="hazmatFreeForm.do">
        <cong:hidden id="freeFormatValue" name="freeFormatValue" value="${freeFormValue}"/>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="selectedSection" id="selectedSection"/>
        <cong:hidden name="hazmatId" id="hazmatId"/>
        <cong:div>
            <cong:table>
                <cong:tr styleClass="textlabelsBold">
                    <cong:td styleClass="textlabelsBoldforlcl">Line1:</cong:td>
                    <cong:td>
                        <cong:text name="line1" id="line1" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line2:</cong:td>
                    <cong:td>
                        <cong:text name="line2" id="line2" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line3:</cong:td>
                    <cong:td>
                        <cong:text name="line3" id="line3" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line4:</cong:td>
                    <cong:td>
                        <cong:text name="line4" id="line4" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line5:</cong:td>
                    <cong:td>
                        <cong:text name="line5" id="line5" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line6:</cong:td>
                    <cong:td>
                        <cong:text name="line6" id="line6" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Line7:</cong:td>
                    <cong:td>
                        <cong:text name="line7" id="line7" styleClass="text" maxlength="50"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <br>
            <cong:table align="center">
                <cong:tr>
                    <cong:td/><cong:td/><cong:td/>
                    <cong:td><input type="button" class="button-style1" value="Save" id="save" onclick="saveFreeFormatValue('save');">
                        <input type="button" class="button-style1" value="Close" onclick="hideFreeForm();"></cong:td>
                    <cong:td/><cong:td/><cong:td/>
                </cong:tr>
            </cong:table>
        </cong:div>
    </cong:form>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        setValueInLine($("#freeFormatValue").val());
    });

    function setValueInLine(freeFormat) {
        if (freeFormat != "") {
            var lines = wordwrap(freeFormat, 50).split("<br/>");
            for (var i = 0; i < 7; i++) {
                var j = i + 1;
                if (undefined != lines[i] && null != lines[i]) {
                    $("#line" + j).val(lines[i]);
                }
            }
        }
    }
    function wordwrap(str, width, brk, cut) {
        brk = brk || '<br/>\n';
        width = width || 40;
        cut = cut || false;
        if (!str) {
            return str;
        }
        var regex = '.{1,' + width + '}(\\s|$)' + (cut ? '|.{' + width + '}|.+$' : '|\\S+?(\\s|$)');
        return str.match(RegExp(regex, 'g')).join(brk);
    }

    function hideFreeForm() {
        parent.$.colorbox.close();
    }

    function saveFreeFormatValue(methodName) {
        showProgressBar();
        var freeFormValue = "";
        var isValue = "";
        for (var i = 1; i <= 7; i++) {
            isValue += $("#line" + i).val();
            freeFormValue += $("#line" + i).val() + " " + "\n";
        }
        if (isValue.trim().trim() === "") {
            freeFormValue = "";
            parent.$("#freeForm").addClass("button-style1");
            parent.$("#freeForm").removeClass("green-background");
        }else{
            parent.$("#freeForm").removeClass("button-style1");
            parent.$("#freeForm").addClass("green-background");
        }
        parent.$('#freeFormValues').val(freeFormValue);
        $("#freeFormatValue").val(freeFormValue);
        $("#selectedSection").val($("#selectedSection").val());
        $("#methodName").val(methodName);
        $("#lclBookingHazmatFreeForm").submit();
        hideProgressBar();
        parent.$.colorbox.close();
    }


</script>

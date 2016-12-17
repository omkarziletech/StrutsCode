<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <body>
        <html:form action="/lclUnitType.do" name="lclUnitTypeForm" styleId="lclUnitTypeForm" type="com.gp.cong.logisoft.struts.form.lclDataReference.LclUnitTypeForm" scope="request">
            <input type="hidden" name="methodName" id="methodName" value="${lclUnitTypeForm.methodName}"/>
            <br/>
            <table width="99.5%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td width="100%" colspan="7">Add Unit Type</td><td><font  color="blue">${message}</font></td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" value="Save" name="save" onclick="saveUnitType()" />
                        <input type="button" class="buttonStyleNew" value="Go Back" name="cancel" onclick="backToUnitTypeList()" /></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Unit Description</td>
                    <td>
                        <html:text styleClass="mandatory text" property="description" styleId="description" onchange="checkDesc();"/>
                    </td>
                    <td  class="textlabelsBoldforlcl">Refrigeration</td>
                    <td>
                        <html:radio  value="true" property="refrigerated" /> Yes
                        <html:radio value="false" property="refrigerated"  /> No
                    </td>
                    <td class="textlabelsBoldforlcl">Elite Type</td>
                    <td><html:text styleClass="text" property="eliteType" styleId="eliteType" maxlength="5" style="text-transform: uppercase;" /></td>
                    <td class="textlabelsBoldforlcl">Interior Length Imperial</td>
                    <td><html:text styleClass="text" property="interiorLengthImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Interior Length Metric</td>
                    <td><html:text styleClass="text" property="interiorLengthMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Interior Width Imperial</td>
                    <td><html:text styleClass="text" property="interiorWidthImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Interior Width Metric</td>
                    <td><html:text styleClass="text" property="interiorWidthMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td class="textlabelsBoldforlcl">Interior Height Imperial</td>
                    <td><html:text styleClass="text" property="interiorHeightImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Interior Height Metric</td>
                    <td><html:text styleClass="text" property="interiorHeightMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Door Height Imperial</td>
                    <td><html:text styleClass="text" property="doorHeightImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Door Height Metric</td>
                    <td><html:text styleClass="text" property="doorHeightMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Door Width Imperial</td>
                    <td><html:text styleClass="text" property="doorWidthImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Door Width Metric</td>
                    <td><html:text styleClass="text" property="doorWidthMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Gross Weight Imperial</td>
                    <td><html:text styleClass="text" property="grossWeightImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Gross Weight Metric</td>
                    <td><html:text styleClass="text" property="grossWeightMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Tare Weight Imperial</td>
                    <td><html:text styleClass="text" property="tareWeightImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Tare Weight Metric</td>
                    <td><html:text styleClass="text" property="tareWeightMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Volume Imperial</td>
                    <td><html:text styleClass="text" property="volumeImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Volume Metric</td>
                    <td><html:text styleClass="text" property="volumeMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">Target Volume Imperial</td>
                    <td><html:text styleClass="text" property="targetVolumeImperial" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Target Volume Metric</td>
                    <td><html:text styleClass="text" property="targetVolumeMetric" onkeyup="checkForNumberAndDecimal(this);" /></td>
                    <td  class="textlabelsBoldforlcl">LCL Air</td>
                    <td>
                        <html:radio value="true" property="enabledLclAir" name="lclUnitTypeForm"/>Yes
                        <html:radio value="false" property="enabledLclAir" name="lclUnitTypeForm"/>No
                    </td>
                    <td  class="textlabelsBoldforlcl">LCL Exports</td>
                    <td>
                        <html:radio value="true" property="enabledLclExports" name="lclUnitTypeForm"/>Yes
                        <html:radio value="false" property="enabledLclExports" name="lclUnitTypeForm"/>No</td>
                    <td  class="textlabelsBoldforlcl">LCL Imports</td>
                    <td>
                        <html:radio value="true" property="enabledLclImports" name="lclUnitTypeForm"/>Yes
                        <html:radio value="false" property="enabledLclImports" name="lclUnitTypeForm"/>No
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Short Desc</td>
                    <td><html:text styleClass="text" property="shortDesc" styleId="shortDesc" maxlength="5" style="text-transform: uppercase;" /></td>
                    <td class="textlabelsBoldforlcl">Remarks</td>
                    <td colspan="2"><html:textarea  property="remarks" styleId="remarks" styleClass="textlabelsBoldForTextBox" rows="5" cols="50" style="text-transform: uppercase;" /></td>
                    <td colspan="4"></td>
                </tr>
            </table>
            <html:hidden property="methodName"/>
            <html:hidden property="id"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function backToUnitTypeList() {
            $('#methodName').val('display');
            $('#lclUnitTypeForm').submit();
        }
        function saveUnitType() {
            var descrip = $('#description').val();
            var elite = $('#eliteType').val();
            if (descrip == "" && descrip == '') {
                congAlert("Please Enter Description");
                $('#description').focus();
            } else if (elite == "" && elite == '') {
                congAlert("Please Enter Elite Type");
                $('#eliteType').focus();
            } else {
                $('#methodName').val('saveUnitType');
                $('#lclUnitTypeForm').submit();
            }
        }
        function checkDesc() {
            var descrip = $('#description').val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.dwr.LclDwr",
                    methodName: "checkDescription",
                    param1: descrip,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data == true) {
                        congAlert("Description is already exsist, Please enter a different description");
                        $('#description').val('');
                        $('#description').focus();
                    }
                }
            });
        }
        function congAlert(txt) {
            $.prompt(txt);
        }
    </script>
</html>

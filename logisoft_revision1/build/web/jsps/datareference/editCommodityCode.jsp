<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <title>Edit Commodity Code</title>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/commodityCode.do" name="commodityCodeForm" styleId="commodityCodeForm"  type="com.gp.cvst.logisoft.struts.form.lcl.CommodityCodeForm" scope="request">
            <input type="hidden" id="methodName" name="methodName">

            <br/>
            <table width="99.5%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Commodity details</td><td><font color="blue">${message}</font></td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" value="Save" name="save" onclick="saveCommodityType()" />
                        <input type="button" class="buttonStyleNew" value="Go Back" name="cancel" onclick="backToCommList()" /></td>
                </tr>
                <tr><td colspan="3">
                        <table width="100%" border="0">
                            <tr>
                                <td class="textlabelsBoldforlcl">Code</td>
                                <td><html:text styleId="code" property="code"  maxlength="25" styleClass="areahighlightyellow1" readonly="true"/></td>
                                <td class="textlabelsBoldforlcl">Active</td>
                                <td class="style2">
                                    <html:radio property="active" name="commodityCodeForm" value="true"/>Yes
                                    <html:radio property="active" name="commodityCodeForm" value="false"/>No</td>
                            </tr>
                            <tr>
                                <td class="textlabelsBoldforlcl">Description</td>
                                <td><html:text styleId="descEn" property="descEn"  maxlength="25" styleClass="areahighlightyellow1" readonly="true" /></td>
                                <td class="textlabelsBoldforlcl">Hazmat</td>
                                <td class="style2">
                                    <html:radio property="hazmat" name="commodityCodeForm" value="true" />Yes
                                    <html:radio property="hazmat" name="commodityCodeForm" value="false" />No</td>
                            </tr>
                            <tr>
                                <td class="textlabelsBoldforlcl">High Volume Discount</td>
                                <td class="style2">
                                    <html:radio property="highVolumeDiscount" name="commodityCodeForm" value="true"/>Yes
                                    <html:radio property="highVolumeDiscount" name="commodityCodeForm" value="false"/>No</td>
                                <td class="textlabelsBoldforlcl">Refrigeration Required</td>
                                <td class="style2">
                                    <html:radio property="refrigerationRequired" name="commodityCodeForm" value="true"/>Yes
                                    <html:radio property="refrigerationRequired" name="commodityCodeForm" value="false"/>No</td>
                            </tr>
                            <tr>
                                <td class="textlabelsBoldforlcl" valign="top">Default ERT</td>
                                <td class="style2" valign="top">
                                    <html:radio property="defaultErt" name="commodityCodeForm" value="true"/>Yes
                                    <html:radio property="defaultErt" name="commodityCodeForm" value="false"/>No</td>
                                <td class="textlabelsBoldforlcl"  valign="top">Remarks</td>
                                <td><html:textarea  property="remarks" styleId="remarks" styleClass="textlabelsBoldForTextBox" rows="5" cols="50"/></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <script type="text/javascript" >
        function backToCommList(){
            $('#methodName').val('display');
            $('#commodityCodeForm').submit();
        }
        function saveCommodityType(){
            $('#methodName').val('saveCommodity');
            $('#commodityCodeForm').submit();
        }
    </script>
</html>

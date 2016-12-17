<%-- 
    Document   : lclSearchVoyageUnits
    Created on : Apr 4, 2013, 9:08:03 PM
    Author     : VinothS
--%>

<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<body>
    <div style="width:99%; float:left;">
        <cong:form id="lclSearchForm" name="lclSearchForm" action="/lclSearch.do">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">
                        Load
                    </td>
                </tr>
            </table>
            <table width="100%" border="0">
                <tr><td><br/></td></tr>
                <tr>
                    <td styleClass="textlabelsBoldforlcl" width="15%">BookedVoyage/POL/POD/SailingDate</td>
                    <td>
                        <html:select property="voyageUnit" styleId="voyageUnit"  style="width:275px" styleClass="smallDropDown textlabelsBoldforlcl" >
                            <html:option value="">Select One</html:option>
                            <html:optionsCollection styleClass="voyagelist" name="voyageUnitList"/>
                        </html:select>
                    </td>
                </tr>
                <tr>
                    <td  colspan="2" align="center">
                        <input type="button" class="button-style1" id="go" name="go" value="Go" onclick="gotoUnits('${path}')"/>
                    </td>
                </tr>
            </table>
        </cong:form>
    </div>
    <script>
        function gotoUnits(path) {
            var optionSelect = $("#voyageUnit option:selected").text();
            var headerId = $("#voyageUnit").val();
            if (headerId !== '' && headerId !== null && headerId !== undefined) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO",
                        methodName: "getVoyageDetails",
                        param1: headerId,
                        dataType: "json"
                    },
                    success: function (data) {
                        var value = checkUnitAvailable(optionSelect.substring(0, optionSelect.indexOf("/")));
                        if (value === 'unlocked') {
                            goToUnitLoadScreenFromBooking(path, headerId, data[0], data[1]);
                        } else {
                            $.prompt(value);    
                        }
                    }
                });
            }
        }

        function goToUnitLoadScreenFromBooking(path, headerId, unitSsId, detailId) {
            var fromScreen = "BookingToUnitLoadScreen";
            var filter = 'lclExport';
            window.parent.parent.goToBookingFromVoyage(path, '', filter, headerId, detailId, unitSsId, fromScreen, "EXP VOYAGE", "");
        }

        function  checkUnitAvailable(scheduleNo) {
            var flag = "";
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "unitStuffingAvailable",
                    param1: scheduleNo,
                    param2: 'LCLEXPORT-VOYAGE',
                    param3: 'UNIT-STUFFING',
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    flag = data;
                }
            });
            return flag;
        }
    </script>

</body>


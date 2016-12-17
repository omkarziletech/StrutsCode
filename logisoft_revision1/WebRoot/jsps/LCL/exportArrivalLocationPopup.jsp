<%-- 
    Document   : exportArrivalLocationPopup
    Created on : May 6, 2015, 7:04:48 PM
    Author     : aravindhan.v
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="init.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form  action="lclAddVoyage.do" name="lclAddVoyageForm" id="lclAddVoyageForm">
            <cong:hidden id="headerId" name="headerId" value="${lclAddVoyageForm.headerId}"/>
            <cong:hidden id="unitId" name="unitId" value="${lclAddVoyageForm.unitId}"/>
            <cong:hidden id="unitssId" name="unitssId" value="${lclAddVoyageForm.unitssId}"/>
            <cong:hidden id="originId" name="originId" value="${lclAddVoyageForm.originId}"/>
            <cong:hidden id="finalDestinationId" name="finalDestinationId" value="${lclAddVoyageForm.finalDestinationId}"/>
            <cong:hidden id="unitssId" name="unitssId" value="${lclAddVoyageForm.headerId}"/>
            <cong:hidden id="index" name="index" value="${lclAddVoyageForm.index}"/>
            <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}"/>
            <cong:hidden id="unitVoyageSearch" name="unitVoyageSearch" value="${lclAddVoyageForm.unitVoyageSearch}"/>
            <cong:hidden id="pol" name="pol" value="${lclAddVoyageForm.pol}"/>
            <cong:hidden id="pod" name="pod" value="${lclAddVoyageForm.pod}"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">
                        <span>Arrival Location</span>
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center" border="0">
                <tr align="center">
                    <td class="textlabelsBoldforlcl">City</td>
                    <td>
                        <html:select property="arrivallocation" styleId="arrivallocation" value="" style="width:120px;"
                                     styleClass="textlabelsBoldForTextBox verysmalldropdownStyleForText mandatory">
                            <html:option value="">Select One</html:option>
                            <html:optionsCollection name="arrivalCityList"/>
                        </html:select>
                    </td>
                </tr>
            </table>
            <br/>
            <table width="100%" border="0" align="center">
                <tr>
                    <td width="45%"></td>
                    <td align="center">
                        <input type="button" value="submit" align="center" class="button-style1"   onclick="saveArrivalLocation('${path}');"/>
                    </td>
                </tr>
                <tr><td colspan="2"></td></tr>
            </table>
            <cong:hidden id="methodName" name="methodName"/>
        </form>
    </body>

    <script type="text/javascript">
        function saveArrivalLocation(){
            var headerId = $("#headerId").val();
            var unitId = $("#unitId").val();
            var unitSsId = $("#unitSsId").val();
            var index = $("#index").val();
            var originId = $("#originId").val();
            var destnId = $("#finalDestinationId").val();
            var unitVoyageSearch = $("#unitVoyageSearch").val();
            var filterByChanges = $("#filterByChanges").val();
            var pol=$("#pol").val()
            var pod=$("#pod").val()
            var arrivalLocation=$("#arrivallocation").val();
            if(arrivalLocation===null || arrivalLocation===""){
                $("#arrivallocation").css("border-color", "red");
                $("#warning").show();
            }else{
                showProgressBar();
                $("#methodName").val("loadComplete");
                var params = $("#lclAddVoyageForm").serialize();
                params += "&unitId="+unitId+"&originId="+originId+"&finalDestinationId="+destnId+"&unitssId="
                    +unitSsId+"&index="+index+"&headerId="+headerId+"&filterByChanges="
                    +filterByChanges+"&originalOriginName="+pol+"&originalDestinationName="+pod+"&arrivallocation="+arrivalLocation;
                if(unitVoyageSearch=='Y'){
                    params+="&unitVoyageSearch="+unitVoyageSearch;
                }
                $.post($("#lclAddVoyageForm").attr("action"), params,
                function(data) {
                    if(data!=null){
                        parent.$.fn.colorbox.close();
                        parent.$("#methodName").val("editVoyage");
                        parent.$("#lclAddVoyageForm").submit();
                    }
                });
            }
        }
    </script>
</html>

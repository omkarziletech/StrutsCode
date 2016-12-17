<%-- 
    Document   : currentLocatioPopUp
    Created on : Nov 7, 2014, 12:08:27 AM
    Author     : meiyazhakan.r
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  action="lclBooking.do" name="lclBookingForm" id="lclBookingForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="fileNumberId" name="fileNumberId"/>
    <input type="hidden" id="oldCurrentLocation" name="oldCurrentLocation" value="${oldCurrentLocation}"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                DR# <span style="color: red;">${lclBookingForm.fileNumber}</span>
            </td>
        </tr>
    </table>
    <br/>
    <cong:table width="100%" border="0">
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Current Location</cong:td>
            <cong:td>
                <cong:autocompletor name="currentLocationDojo" id="currentLocationDojo" template="one" width="250" container="NULL"
                                    query="PORT" fields="NULL,NULL,NULL,unLocationId" styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300px"/>
                <input type="hidden" name="unLocationId" id="unLocationId"/>
            </cong:td>
        </cong:tr>
    </cong:table>
    <br/>
    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td align="center">
                <input type="button" value="submit" align="center" class="button-style1" onclick="saveCurrentLocation();"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="2"></cong:td></cong:tr>
    </cong:table>
</cong:form>
<script type="text/javascript">
    function saveCurrentLocation(){
        var oldUnlocationCode=getUnlocationCode("oldCurrentLocation");
        var newUnlocationCode=getUnlocationCode("currentLocationDojo");
        if($("#currentLocationDojo").val()===null || $("#currentLocationDojo").val()===""){
            $("#currentLocationDojo").css("border-color", "red");
            $("#warning").show();
        }else if(oldUnlocationCode===newUnlocationCode){
        
        }else{
            showProgressBar();
            var oldLocationName=getUnlocationName("oldCurrentLocation");
            $("#methodName").val("saveCurrentLocation");
            var params = $("#lclBookingForm").serialize();
            params += "&fileNumberId=" + $("#fileNumberId").val()+"&unLocationId="+$("#unLocationId").val()+"&oldLocationName="+oldLocationName;
            $.post($("#lclBookingForm").attr("action"), params,
            function(data) {
                if(data==="true"){
                    parent.jQuery("#currentLocation").text($("#currentLocationDojo").val());
                    parent.jQuery("#oldCurrentLocationName").val($("#currentLocationDojo").val());
                    parent.$.fn.colorbox.close();
                    hideProgressBar();
                }
            });
        }
    }
    function getUnlocationCode(id) {
        var  unlocationCode = $('#'+id).val();
        if (unlocationCode.lastIndexOf("(") > -1 && unlocationCode.lastIndexOf(")") > -1) {
            return unlocationCode.substring(unlocationCode.lastIndexOf("(") + 1, unlocationCode.lastIndexOf(")"));
        }
        return "";
    }
    function getUnlocationName(id) {
        var  unlocationCode = $('#'+id).val();
        return unlocationCode.substring(0,unlocationCode.lastIndexOf("/"));
        return "";
    }
</script>
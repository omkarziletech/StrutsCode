<%-- 
    Document   : fclRatesReport
    Created on : May 18, 2015, 5:00:15 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>FCL Rates Report</title>
        <%@include file="../LCL/init.jsp" %>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body>
        <html:form  action="/fclRatesReport.do" styleId="searchFCLForm" name="searchFCLForm" type="com.gp.cong.logisoft.struts.ratemangement.action.FclRatesReportAction" scope="request">
            <html:hidden property="methodName" styleId="methodName"/>
            <html:hidden property="orgRegion" styleId="orgRegion"/>
            <html:hidden property="destRegion" styleId="destRegion"/>
            <html:hidden property="originName" styleId="hiddenOriginName"/>
            <html:hidden property="destinationName" styleId="hiddenDestinationName"/>

            <table width="100%" style="border:1px solid #dcdcdc">
                <thead>
                    <tr id="tableBanner">
                        <td colspan="4">FCL Rates Search</td>
                    </tr>
                </thead>
                <tbody>
                    <tr><td colspan="4"></td></tr>
                    <tr>
                        <td id="labelText">Origin</td>
                        <td>
                            <cong:autocompletor  name="origin" styleClass="textWidth"  position="right" id="originId"
                                                 fields="originSchnumId"
                                                 shouldMatch="true" width="200"  query="FCL_RATE_ORIGIN"
                                                 template="one" container="null" scrollHeight="300"/>
                            <html:text property="originSchnum" styleId="originSchnumId" styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:70px"/>
                        </td>
                        <td id="labelText">Destination</td>
                        <td>
                            <cong:autocompletor  name="destination" styleClass="textWidth"  position="right" id="destinationId"
                                                 fields="destinationSchnumId"
                                                 shouldMatch="true" width="350"  query="FCL_RATE_DESTINATION"
                                                 template="one" container="null" scrollHeight="300"/>
                            <html:text property="destinationSchnum" styleId="destinationSchnumId" styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:70px"/>
                        </td>
                    </tr>
                    <tr><td colspan="4"></td></tr>
                    <tr> 
                        <td id="labelText">Origin Region</td>
                        <td>
                            <html:select property="originName" styleId="originName" styleClass="dropdownMultiSelect" style="text-transform:none;" multiple="multiple">
                                <html:optionsCollection property="regions" label="label" value="value"/>
                            </html:select>
                        </td>
                        <td id="labelText">Destination Region</td>
                        <td>
                            <html:select property="destinationName" styleId="destinationName" styleClass="dropdownMultiSelect" style="text-transform:none"  multiple="multiple">
                                <html:optionsCollection property="regions" label="label" value="value"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr><td colspan="4"></td></tr>
                    <tr><td colspan="4"></td></tr>
                    <tr>
                        <td id="labelText">Commodity</td>
                        <td>
                            <cong:autocompletor  name="commodityName" styleClass="textWidth"  position="right" id="commodityNameId"
                                                 fields="commodityNumber" value="${searchFCLForm.commodityName}"
                                                 shouldMatch="true" width="600"  query="FCL_RATE_COMMODITY" template="one" container="null" scrollHeight="300"/>
                            <html:text styleClass="textlabelsBoldForTextBoxDisabledLook"  styleId="commodityNumber" property="commodityNumber" style="width:70px" readonly="true"/>
                        </td>
                        <td id="labelText">SS Line</td>
                        <td>
                            <cong:autocompletor  name="sslinename" styleClass="textWidth"  position="right" id="sslineNameId"
                                                 fields="NULL,NULL,NULL,NULL,NULL,sslinenumber"
                                                 shouldMatch="true" width="600"  query="FCL_RATE_SSLINE" template="tradingPartner" container="null" scrollHeight="300"/>
                            <html:text styleClass="textlabelsBoldForTextBoxDisabledLook" styleId="sslinenumber" property="sslinenumber" style="width:70px" readonly="true"/>
                        </td>
                    </tr>
                    <tr><td colspan="4" align="center">
                            <input type="button" value="Download Xls" class="button" onclick="downloadReport();"/>
                            <input type="button" value="Reset" class="button" onclick="resetForm();"/>
                        </td></tr>
                </tbody>
                <tr><td colspan="4"></td></tr>
                <tr><td colspan="4"></td></tr>
            </table>
        </html:form>
    </body>
    <script type="text/javascript">
        function resetForm(){
            $('#originSchnumId').val('');
            $('#originId').val('');
            $('#destinationSchnumId').val('');
            $('#destinationId').val('');
            $('#commodityNameId').val('');
            $('#commodityNumber').val('');
            $('#sslineNameId').val('');
            $('#sslinenumber').val('');
            $('#orgRegion').val('');
            $('#hiddenDestinationName').val('');
            $('#destRegion').val('');
            $('#originName option:selected').each(function(i, e){
                e.selected = false;
            });
            $('#destinationName option:selected').each(function(i, e){
                e.selected = false;
            });
        }
        function downloadReport(){
            var originName = [];
            var originId = [];
            var destinationName = [];
            var destinationId = [];
            $('#originName option:selected').each(function(i, selected){
                originName[i] =$(selected).val()+'-'+ $(selected).text();
                originId[i] = $(selected).val();
            });
            $('#destinationName option:selected').each(function(i, selected){
                destinationName[i] =$(selected).val()+'-'+ $(selected).text();
                destinationId[i] = $(selected).val();
            });
            if(originId.length==0 && $("#originSchnumId").val()===""){
                //  $("#originName").css("border-color", "red");
                $.prompt('Please Enter either Origin or Origin Region is Required')
                return false;
            }
            if(destinationId.length==0 && $("#destinationSchnumId").val()===""){
                $.prompt('Please Enter either Destination or Destination Region is Required')
                //  $("#destinationName").css("border-color", "red");
                return false;
            }
            if($('#commodityNumber').val()==''){
                $.prompt('Commodity is Required')
                $("#commodityNameId").css("border-color", "red");
                return false;
            }
            if($('#sslinenumber').val()==''){
                $.prompt('SSLine is Required')
                $("#sslineNameId").css("border-color", "red");
                return false;
            }
            $('#hiddenOriginName').val(originName)
            $('#orgRegion').val(originId)
            $('#hiddenDestinationName').val(destinationName)
            $('#destRegion').val(destinationId)
            document.searchFCLForm.methodName.value="downloadXlsReport";
            document.searchFCLForm.submit();
        }
    </script>
</html>

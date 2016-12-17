<%-- 
    Document   : BlueToLogi
    Created on : 13 Apr, 2015, 6:13:58 PM
    Author     : Admin
--%>

<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://cong.logiwareinc.com/tagutils" prefix="tagutils"%>
<html>
    <head>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1"/>
        <title>Show Online Users</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link rel="stylesheet" type="text/css" href="${path}/css/default/style.css" title="default"/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/admin/unLocation.js"></script>
    </head>
    <body>

        <cong:form name="expBlueToLogiForm" id="expBlueToLogiForm" action="blueToLogi.do">
            <table  cellspacing="1" class="tableBorderNew" width="100%" >
                &nbsp;
                <tr class="tableHeadingNew"><td colspan="4" width="100%">DR Details</td></tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td width="30%"></td>
                    <td class="textlabelsBold" width="15%">
					Destination
                        <cong:autocompletor id="code" name="code" template="two" query="CONCAT_RELAY_NAME_FD"  container="NULL" value="${code}"
                                            styleClass="textlabelsBoldForTextBox" shouldMatch="true" fields="NULL,NULL,NULL,destId" width="400" scrollHeight="200px"/>
                    </td>
                    <td width="5%">
                        <input type="button" class="buttonStyleNew"  style="width: 100px;" value='Load' onclick="loadData('${path}')"/>
                    </td>
                    <td width="50%">
                        <input type="button" class="buttonStyleNew"  style="width: 100px;" value='Delete' onclick="DeleteBlueRecords('${path}')"/>
                    </td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>

            </table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="destId" id="destId" />
            <input type="hidden" name="deleteMsg" id="deleteMsg" value="${deleteMsg}"/>
            <input type="hidden" name="loadMsg" id="loadMsg" value="${loadMsg}"/>
            <script type="text/javascript">
                jQuery(document).ready(function () {
                    if($("#deleteMsg").val()!=="" && $("#deleteMsg").val()!==null){
                        $.prompt($("#deleteMsg").val());
                    }
                    if($("#loadMsg").val()!=="" && $("#loadMsg").val()!==null){
                        $.prompt($("#loadMsg").val());
                    }
                });
                function DeleteBlueRecords(path){
                    var code=$("#code").val();
                    var destinationId=$("#destId").val();
                    if(code!=""){
                        showLoading();
                        var url = path + "/blueToLogi.do?methodName=deleteLogiRecord&destId="+destinationId;
                        window.location=url;
                       
                    }else{
                        $.prompt("Please enter Destination");
                    }

                }
    
                function loadData(path){
                    var code=$("#code").val();
                    var destinationId=$("#destId").val();
                    if(code!=""){
                        showLoading();
                        var url = path + "/blueToLogi.do?methodName=uploadLogiRecord&destId="+destinationId;
                        window.location=url;
                        
                    }else{
                        $.prompt("Please enter Destination");

                    }
            
                }

               

            </script>
        </cong:form>
    </body>

</html>

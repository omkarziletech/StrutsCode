<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="init.jsp" %>
        <%@include file="colorBox.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@include file="../fragment/lclFormSerialize.jspf"  %>
        <cong:javascript src="${path}/jsps/LCL/js/common.js"/>
        <%@include file="/taglib.jsp" %>
        <script type="text/javascript">
            function save1(path){
                $("#methodName").val('save');
                $("#lclUserDefaultsForm").submit();
            }
            function clear1(path){
                var href=path+"/lclUserDefaults.do?methodName=clear";
                document.location.href=href;
            }
        </script>
    </head>
    <body class="whitebackgrnd">

        <cong:form name="lclUserDefaultsForm" id="lclUserDefaultsForm" action="lclUserDefaults.do" >
            <div align="right">
                <input type="hidden"  id="methodName" name="methodName"/>
                <input type="button" class="buttonStyleNew" value="Save" id="save"  onclick="save1('${path}')">
                <input type="button" class="buttonStyleNew" value="Clear" id="clear"  onclick="clear1('${path}')">
                <input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="parent.parent.GB_hide();"/>
            </div>
            <cong:table  caption="&nbsp; " cellspacing="2" border="0">
                <cong:tr><cong:td colspan="2">&nbsp;</cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left"> Current Location </cong:td>
                    <cong:td>
                        <cong:autocompletor id="currentLocationR" name="currentLocation" template="one" fields="unlocationName,NULL,unlocationCode,currentLocationId"
                                            query="PORT" value="${currentLocation}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"         width="500" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="currentLocationId" id="currentLocationId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">Origin </cong:td>
                    <cong:td>
                        <cong:autocompletor id="portOfOriginR" name="portOfOrigin" template="one" fields="unlocationName,NULL,unlocationCode,portOfOriginId"
                                            query="PORT" value="${portOfOrigin}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="500" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfOriginId" id="portOfOriginId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">POL </cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${lclSession.selectedMenu=='Imports'}">
                                <c:set var="polQuery" value="ORIGIN_UNLOC"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="polQuery" value="PORT"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:autocompletor id="portOfLoadingR" name="portOfLoading" template="one" fields="unlocationName,NULL,unlocationCode,portOfLoadingId"
                                            query="${polQuery}" value="${portOfLoading}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="500" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">POD</cong:td>
                    <cong:td>
                        <cong:autocompletor id="portOfDestinationR" name="portOfDestination" template="one" fields="unlocationName,NULL,unlocationCode,portOfDestinationId"
                                            query="CONCAT_RELAY_NAME_FD" value="${portOfDestination}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="500" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" align="left">Destination</cong:td>
                    <cong:td>
                        <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                            query="CONCAT_RELAY_NAME_FD" value="${finalDestination}"
                                            styleClass="textlabelsLclBoldForMainScreenTextBox"  width="500" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="finalDestinationId" id="finalDestinationId" value=""/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:form>
    </body>
</html>
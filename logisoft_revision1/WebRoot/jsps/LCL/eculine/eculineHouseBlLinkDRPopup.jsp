<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%--By Vijay Gupta--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Container Details</title>
        <%@include file="../init.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript">
            function linkDrStatus(){
                if(document.getElementById("fileNumber").value==""){
                    sampleAlert("File Number is required");
                    $("#fileNumber").css("border-color","red");
                    $("#warning").show();
                }else{
                    if(isSpecial(document.getElementById("fileNumber").value)){
                        $("#methodName").val('savelinkDR');
                        $("#lclEculineEdiBlInfoForm").submit();
                        $("#submithide").hide();
                    }else{
                        sampleAlert("Special Characters Not Allowed in D/R#");
                        $("#fileNumber").css("border-color","red");
                        $("#warning").show();
                    }
                }
            }
            function linkDrOk(){
                parent.$("#methodName").val('refreshContainerPage');
                parent.$("#lclEculineEdiForm").submit();
            }
        </script>
    </head>
    <body>
        <cong:form  action="/lclEculineEdiBlInfo" name="lclEculineEdiBlInfoForm" id="lclEculineEdiBlInfoForm" >
            <cong:hidden id="houseBlNo" name="houseBlNo"  />
            <cong:hidden id="containerNo" name="containerNo" value="${lclAddVoyageForm.containerNo}"  />
            <cong:hidden id="voyNo" name="voyNo" value="${lclAddVoyageForm.voyNo}"  />
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="id" name="id"  />
            <cong:hidden id="houseBlNo" name="houseBlNo"  />
            <cong:hidden id="polUncode" name="polUncode" />
            <cong:hidden id="podUncode" name="podUncode" />
            <cong:hidden id="voyNo" name="voyNo" />
            <cong:hidden id="containerNo" name="containerNo" />
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">
                        Link DR: <span style="color: red;">${lclEculineEdiBlInfoForm.houseBlNo}</span>
                    </td>
                </tr>
            </table>
            <br/>
            <cong:table width="100%" border="0">
                <c:if test="${not empty lclEculineEdiBlInfoForm.message}">
                    <cong:tr>
                        <cong:td colspan="3" styleClass="greenFontBold" align="center" width="50%">${lclEculineEdiBlInfoForm.message}</cong:td>
                    </cong:tr>
                </c:if>
                <cong:tr>
                    <cong:td width="10%"></cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Enter D/R#</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${lclEculineEdiBlInfoForm.message eq 'D/R# Successfully Linked !'}">
                                <c:set var="cssStyle" value="textlabelsBoldForTextBoxDisabledLook"/>
                            </c:when>
                            <c:otherwise><c:set var="cssStyle" value="mandatory textuppercaseLetter"/></c:otherwise></c:choose>
                        <cong:autocompletor id="fileNumber" name="fileNumber" template="one" query="DOCK_RECEIPT" styleClass="${cssStyle}" shouldMatch="true"
                                            width="130" container="NULL" params="${lclEculineEdiBlInfoForm.polUncode},${lclEculineEdiBlInfoForm.podUncode}" scrollHeight="150"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <br/>
            <cong:table width="100%" border="0" align="center">
                <cong:tr>
                    <cong:td width="45%"></cong:td>
                    <cong:td align="center">
                        <c:choose>
                            <c:when test="${lclEculineEdiBlInfoForm.message eq 'D/R# Successfully Linked !'}">
                                <input type="button"  value="ok" align="center" class="button-style1" onclick="linkDrOk();"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" id="submithide" value="Submit" align="center" class="button-style1" onclick="linkDrStatus();"/>
                            </c:otherwise>
                        </c:choose>
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="2"></cong:td></cong:tr>
            </cong:table>
        </cong:form>
    </body>
</html>

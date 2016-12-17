<%-- 
    Document   : lclOSDPopup
        Created on : Mar 13, 2014, 12:35:06 PM
    Author     : vijaygupta.m
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd"> 
<%@include file="init.jsp"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
    <body style="background:#ffffff">
    <cong:form action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
        <input type="hidden" name="osdvalues" id="osdvalues" name="${lclUnitWhse.osdDatetime}"/>
        <table width="100%" style="width:99%;border: 1px solid #dcdcdc;" border="0">
            <tr class="tableHeadingNew">
                <td width="50%">OSD Details</td>
                <td width="50%" align="center">
                    Unit # :<span class="fileNo">${lclAddVoyageForm.unitNo}</span>
                </td>
            </tr>
            <tr>
                <td align="left" colspan="2">
                    <span class="blackBold">OSD Date :</span> 
                    <span  class="greenBold15px">
                        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss a" var="osd" value="${lclUnitWhse.osdDatetime}"></fmt:formatDate>
                        ${osd}
                    </span>
                </td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td  align="left" colspan="2">
                    <span class="blackBold">OSD User :</span>
                    <span  class="greenBold15px">${lclUnitWhse.osdUser.loginName}</span>
                </td>
                <td colspan="2"></td>
            </tr>
            <tr>
                <td align="center" colspan="2">
                    <c:if test="${empty lclUnitWhse.osdDatetime &&  empty lclUnitWhse.osdUser.loginName}">
                        <input type="button" class="buttonStyleNew" value="Yes"
                               onclick="saveOSD();"/>
                        &nbsp;&nbsp;
                    </c:if>
                    <c:if test="${not empty lclUnitWhse.osdDatetime && roleDuty.lclUnitOSD}">
                        <input type="button" class="buttonStyleNew" value="Reset"
                               onclick="resetosd();"/>
                    </c:if>
                </td>
            </tr>
        </table>
    </cong:form>
    <script type="text/javascript">
                                   function closePopupBox() {
                                       parent.$.fn.colorbox.close();
                                   }
                                   function resetosd() {
                                       parent.$("#methodName").val('clearOSDDetails');
                                       parent.$("#lclAddVoyageForm").submit();
                                   }
                                   function saveOSD() {
                                       var osdvalues = $('#osdvalues').val();
                                       if (osdvalues === '') {
                                           $.prompt("Confirm OSD?", {
                                               buttons: {
                                                   Yes: 1,
                                                   No: 2
                                               },
                                               submit: function(v) {
                                                   if (v === 1) {
                                                       osdDetails();
                                                       $.prompt.close();
                                                   }
                                                   else if (v === 2) {
                                                       $.prompt.close();
                                                       parent.$.fn.colorbox.close();
                                                   }
                                               }
                                           });
                                       } else {
                                           osdDetails();
                                       }

                                   }
                                   function osdDetails() {
                                       parent.$("#methodName").val('saveOSDDetails');
                                       parent.$("#lclAddVoyageForm").submit();
                                   }
    </script>
</body>

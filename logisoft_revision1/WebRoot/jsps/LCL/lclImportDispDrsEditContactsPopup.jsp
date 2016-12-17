<%-- 
    Document   : lclImportDispDrsEditContactsPopup
    Created on : Feb 13, 2014, 11:46:34 AM
    Author     : vijaygupta.m
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
    <body style="background:#ffffff">
    <cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
        <cong:hidden id="methodName" name="methodName"/>
        <cong:hidden id="unitId" name="unitId"  />
        <cong:hidden id="postFlag" name="postFlag"  />
        <cong:hidden id="unitssId" name="unitssId"  />
        <input type="hidden" id="dispContactScreenFlag" name="dispContactScreenFlag" value="true" />
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew">
                <td width="15%">
                    View DRs
                </td>
                <td width="5%"><span class="blackBold"> UNIT# </span></td>
                <td width="25%"><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span></td>
                <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
                <td width="10%"><span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span></td>
                <td width="10%">  
                    <input type="button" style="" align="middle" class="button-style1" 
                           value="OK" id="saveCode" onclick="addNewDispositionFromEditContactScreen('${path}', '${noAlertFlag}');"/></td>
            </tr>
        </table>
        <div style="width:100%;overflow:auto;height:280px" align="center" id="drDiv">
            <table border="1" id="manifestDr" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
                <tr class="tableHeading2">
                    <td width="4%">File#</td>
                    <td width="8%">Consignee</td>
                    <td width="8%">Notify</td>
                </tr>
                <c:set var="index" value="0"></c:set>
                <c:forEach items="${drList}" var="importsManifestBean">
                    <tr>
                        <td>${importsManifestBean.fileNo}</td>
                        <td>
                            <c:if test="${ empty importsManifestBean.consEmail }">
                                <span title="<strong>${importsManifestBean.consigneeName}<br/>${importsManifestBean.consAcct}</strong>">
                                    ${fn:substring(importsManifestBean.consigneeName,0,11)}
                                </span>
                                <input type="hidden" name="consigneeName" id="consigneeName${index}" value="${importsManifestBean.consigneeName}"/>
                                <input type="hidden" name="consigneeCode" id="consigneeCode${index}" value="${importsManifestBean.consAcct}"/>
                                <c:if test="${not empty importsManifestBean.consigneeName }">
                                    <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Consignee',${index})"/>
                                </c:if>
                            </c:if>
                        <td>
                            <c:if test="${ empty importsManifestBean.notifyEmail }">
                                <span title="<strong>${importsManifestBean.notifyName}<br/>${importsManifestBean.notifyAcct}</strong>">
                                    ${fn:substring(importsManifestBean.notifyName,0,11)}
                                </span>
                                <input type="hidden" name="notifyName" id="notifyName${index}" value="${importsManifestBean.notifyName}"/>
                                <input type="hidden" name="notifyCode" id="notifyCode${index}" value="${importsManifestBean.notifyAcct}"/>
                                <c:if test="${not empty importsManifestBean.notifyName }">
                                    <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Notify',${index} )"/>
                                </c:if>
                            </c:if>
                        </td>
                    </tr>
                    <c:set var="index" value="${index+1}"></c:set>
                </c:forEach>
            </table>

        </div>
        <br>
    </cong:form>

    <script type="text/javascript">
                               function openLclContactInfo(path, party, index) {
                                   var vendorNo = "";
                                   var vendorName = "";
                                   var subtype = "";
                                   if (party === 'Consignee') {
                                       vendorNo = jQuery("#consigneeCode" + index).val();
                                       vendorName = jQuery("#consigneeName" + index).val();
                                       subtype = 'LCL_IMPORT_CONSIGNEE';
                                   } else if (party === 'Notify') {
                                       vendorNo = jQuery("#notifyCode" + index).val();
                                       vendorName = jQuery("#notifyName" + index).val();
                                       subtype = 'LCL_IMPORT_NOTIFY';
                                   }
                                   vendorName = vendorName.replace("&", "amp;");
                                   var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + vendorName + "&vendorNo=" + vendorNo + "&vendorType=" + subtype;
                                   $(".contactR").attr("href", href);
                                   $(".contactR").colorbox({
                                       iframe: true,
                                       width: "98%",
                                       height: "98%",
                                       title: "Contact"
                                   });
                               }
    </script>
</body>

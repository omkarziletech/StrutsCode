<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<% pageContext.setAttribute("singleQuotes", "'");
            pageContext.setAttribute("newLine", "\r\n");
            pageContext.setAttribute("doubleQuotes", "\"");%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
<html>
    <body>
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="exceptionFileNumbers" name="exceptionFileNumbers" value="${lclAddVoyageForm.exceptionFileNumbers}"/>
            <cong:hidden id="unitNo" name="unitNo" value="${lclAddVoyageForm.unitNo}"/>
            <cong:hidden id="scheduleNo" name="scheduleNo" value="${lclAddVoyageForm.scheduleNo}"/>
            <cong:hidden id="unitssId" name="unitssId" value="${lclAddVoyageForm.unitssId}"/>
            <cong:hidden id="unitId" name="unitId" value="${lclAddVoyageForm.unitId}"/>
            <cong:hidden id="headerId" name="headerId" value="${lclAddVoyageForm.headerId}"/>
            <c:set var="saveOrUpdate" value="Submit"/>
            <c:set var="display" value="none"/>
            <c:if test="${not empty lclRemarks.remarks}">
                <c:set var="saveOrUpdate" value="Update"/>
                <c:set var="display" value="block"/>
            </c:if>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="25%">
                        Add Exception
                    </td>
                    <td width="5%"><span class="blackBold"> UNIT# </span></td>
                    <td width="25%"><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span></td>
                    <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
                    <td width="23%"><span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span></td>
                    <c:choose>
                        <c:when test="${not empty addExcepPoppUpFlag}">
                            <td style="float:left">
                                <c:choose>
                                    <c:when test="${not empty showAllButtonFlag}">
                                        <cong:div styleClass="button-style1" style="float:left"
                                                  onclick="showWholeBooking('${path}');" id="showWholeBooking">Show All </cong:div>
                                    </c:when><c:otherwise></c:otherwise>
                                </c:choose>
                            </td>
                        </c:when><c:otherwise>
                            <td style="float:left">
                                <cong:div styleClass="button-style1" style="float:left"
                                          onclick="addExceptionToDrs('${path}');" id="addExceptionId">Add Exception</cong:div>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td colspan="6">
                        <table id="exceptionTable" width="100%"  style="display: ${display}; border-top:0 ">
                            <tr class="blackBold">
                                <td>Exception</td>
                                <td><html:textarea property="unitException" styleClass="textCapsLetter textlabelsBoldForTextBox" styleId="unitExceptionId" cols="60" rows="6" value="${lclRemarks.remarks}"/></td>
                                <td><html:button styleClass="button-style1" style="float:left" property="saveOrUpdate"
                                             onclick="saveExceptionToDrs(this);"  value="${saveOrUpdate}" styleId="saveOrUpdateId"/></td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <br/>
            <table border="1" id="manifestDr" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
                <tr class="tableHeading2">
                    <td>File#</td>
                    <td>BL#</td>
                    <td>Exception</td>
                    <td>BL Invoice#</td>
                    <td width="5%">Cuft</td>
                    <td width="5%">Pounds</td>
                    <td width="5%">Action</td>
                </tr>
                <c:set var="index" value="0"/>
                <c:forEach items="${drList}" var="manifestBean">
                    <tr>
                        <td class="textlabelsBoldForTextBox">
                            ${manifestBean.fileNo}
                            <c:if test="${ empty manifestBean.unitException}">
                                <input type="checkbox" id="drException" name="drException" value="${manifestBean.fileNumberId}" onclick="checkUncheckExceptionToDrs()"/>
                            </c:if>
                            <c:if test="${not empty manifestBean.unitException}">
                                <c:set var="index" value="${index+1}"/>
                            </c:if>
                        </td>
                        <td class="textlabelsBoldForTextBox">${manifestBean.blNo}</td>
                        <td class="textlabelsBoldForTextBox">
                            <span  title="${fn:toUpperCase(manifestBean.unitException)}">${fn:toUpperCase(fn:substring(manifestBean.unitException,0,72))}</span></td>
                        <td class="textlabelsBoldForTextBox">${manifestBean.blInvoiceNo}</td>
                        <td class="textlabelsBoldForTextBox">${manifestBean.totalVolumeImperial}</td>
                        <td class="textlabelsBoldForTextBox">${manifestBean.totalWeightImperial}</td>
                        <td>
                            <c:if test="${ not empty manifestBean.unitException}">
                                <%--  <img src="${path}/img/icons/view.gif" width="16" height="16" onmouseover="tooltip.show('${fn:replace(fn:replace(fn:replace(manifestBean.unitException,doubleQuotes,''),singleQuotes,''),newLine,' ')}')"
                                       onmouseout="tooltip.hide();"/>--%>
                                <span title="<font size='2' color=#008000><b>User Details</b></font></br><font color=red>User :</font> ${manifestBean.modifiedBy}</br><font color=red>Created Date :</font> ${manifestBean.enteredDatetime}"> <img src="${path}/img/icons/view.gif" width="16" height="16" onmouseover=""
                                                                                                                                                                                                                                                   onmouseout="tooltip.hide();"/> </span>
                                <img src="${path}/images/icons/edit.gif" width="16" height="16" onclick="editExceptionToDrs(${manifestBean.fileNumberId})"/>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <cong:hidden id="exceptionCount" name="exceptionCount" value="${index}"/>
        </cong:form>
        <script type="text/javascript">
            $(document).ready(function() {
                if(!document.getElementById("drException")){
                    $("#addExceptionId").hide();
                }
                var exceptionCount=$("#exceptionCount").val();
                var unitssId=$("#unitssId").val();
                if(exceptionCount>0){
                    parent.$("#addException"+unitssId).addClass('green-background');
                }
            });
        </script>
    </body>
</html>

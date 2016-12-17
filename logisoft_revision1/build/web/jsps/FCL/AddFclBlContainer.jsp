<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>

<c:if test="${not empty closeFclContainer}">
    <script type="text/javascript">
        parent.parent.getUpdatedContainerDetails('${closeFclContainer.bol}');
    </script>
</c:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/fcl/fclBlContainer.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    </head>    
    <body class="whitebackgrnd" />
    <!--DESIGN FOR NEW ALERT BOX ---->
    <div id="AlertBox" class="alert">
        <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
        <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

        </p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="OK"
                   onclick="document.getElementById('AlertBox').style.display = 'none';
            grayOut(false, '');">
        </form>
    </div>
    <html:form action="/fclBlContainerDetls" styleId="fclBlContainer" scope="request">
        <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0">
            <tr class="tableHeadingNew"><font style="font-weight: bold">Container Details</font></tr>
        <tr class="textlabelsBold">
            <td>
                <table width="100%">
                    <tr class="textlabelsBold">
                        <td>Unit No </td>
                        <td>
                            <c:choose>
                                <c:when test="${fclBlContainerForm.readyToPost == 'M' || fclBlContainerForm.fclBlContainerDtls.trailerNo == 'BBLK-999999-9'}">
                                    <html:text property="fclBlContainerDtls.trailerNo" styleId="trailerNo" size="15"
                                               styleClass="BackgrndColorForTextBox mandatory" readonly="true"/>
                                </c:when>
                                <c:otherwise>
                                    <html:text property="fclBlContainerDtls.trailerNo" styleId="trailerNo"  size="15" maxlength="13"  onchange="validateUnitNumber(this)"
                                               styleClass="textlabelsBoldForTextBox mandatory"  style="text-transform: uppercase"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>Seal no #</td>
                        <td><html:text property="fclBlContainerDtls.sealNo" styleId="sealNo" style="text-transform: uppercase" maxlength="30"
                                   size="15" onkeydown="setToCommetFoucus()" styleClass="textlabelsBoldForTextBox mandatory"/></td>
                        <td></td>
                        <td>
                        </td>
                        <td>Size Legend</td>
                        <td>
                            <html:select property="sizeLegend" onchange="parent.parent.unitUpdate();" styleId="sizeLegend" value="${fclBlContainerForm.fclBlContainerDtls.sizeLegend.id}"
                                         style="width:120px; font-weight:bold" styleClass="dropdown_accounting">
                                <html:optionsCollection name="fclBlContainerForm" property="unitTypeList"/></html:select></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Marks and Numbers
                                <img src="${path}/img/icons/help-icon.gif"
                                 onmouseover="tooltip.show('<strong>Leave Marks and Numbers blank if you want the container# and seal to automatically print on the house and master</strong>', null, event);" onmouseout="tooltip.hide();"/></td>
                        <td><html:textarea property="fclBlContainerDtls.marksNo" styleId="marksNo"  styleClass="textlabelsBoldForTextBox" onkeyup="limitTextarea(this,40,15)"
                                       cols="23" rows="3" style="text-transform: uppercase"
                                       onfocus="focus_watch=setInterval('watchTextarea()',250)" onblur="clearInterval(focus_watch)"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="center">
                            <c:choose>
                                <c:when test="${fclBlContainerForm.methodName=='editContainer'}">
                                    <input type="button" value="Update" id="update" class="buttonStyleNew"
                                           onclick="updateContainerDetails()" />
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="Submit" id="add" class="buttonStyleNew"
                                           onclick="addContainerDetails()" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <html:hidden property="fclBlContainerDtls.trailerNoId" styleId="trailerNoId"/>
    <html:hidden property="trailerNoId"/>
    <input type="hidden" id="checkContainer" value="${fclBlContainerForm.fclBlContainerDtls.trailerNo}"/>
    <html:hidden property="methodName" styleId="methodName"/>
    <html:hidden property="bol" styleId="bol"/>
    <html:hidden property="breakBulk" styleId="breakBulk"/>
    <html:hidden property="readyToPost" styleId="readyToPost"/>
    <html:hidden property="fclBlContainerDtls.trailerNoOld" styleId="trailerNoOld"/>
    <c:set var="query" value="select if(count(*) ='0', 'false', 'true') as result from fcl_bl_costcodes where bolid=${fclBlContainerForm.bol}"/>
    <c:set var="query" value="${query} and transaction_type not in ('AC','') "/>
    <c:set var="query" value="${query}and Transaction_Type is not null"/>
    <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
</html:form>
</body>
<script type="text/javascript">
    load();
    changeSelectBoxOnViewMode();
    if (${fclBlContainerForm.methodName eq 'editContainer'} && parent.parent.document.getElementById("ratesNonRates").value !== 'N'
            && (parent.parent.document.getElementById('houseBL').value === 'B' || ${result eq 'true' or fclBlContainerForm.readyToPost eq 'M'})) {
        document.getElementById("sizeLegend").tabIndex = -1;
        document.getElementById("sizeLegend").disabled = true;
        document.getElementById("sizeLegend").style.border = 0;
        document.getElementById("sizeLegend").style.backgroundColor = "#CCEBFF";
    }
</script>
</html>

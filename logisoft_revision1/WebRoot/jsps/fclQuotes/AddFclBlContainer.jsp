<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<%@include file="../includes/jspVariables.jsp" %>

<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    com.gp.cong.logisoft.util.DBUtil unitDB = new com.gp.cong.logisoft.util.DBUtil();
    String userName = "";
    if (session.getAttribute("loginuser") != null) {
        User user = (User) session.getAttribute("loginuser");
        userName = user.getLoginName();
    }
    String bol = null, id = null, index = null, button = null, comments = null;
    if (request.getParameter("bol") != null) {
        bol = (String) request.getParameter("bol");
    }
    request.setAttribute("bol", bol);
    List unittypelist = new ArrayList();
    if (unittypelist != null) {
        unittypelist = unitDB.getUnitFCLUnitypeTest(new Integer(38), "yes", "Select Unit code", null, bol);
        request.setAttribute("unittypelist", unittypelist);
    }
    if (request.getParameter("button") != null) {
        button = (String) request.getParameter("button");
    }
    if (null != request.getParameter("id")) {
        id = (String) request.getParameter("id");
    }
    if (null != request.getParameter("index")) {
        index = (String) request.getParameter("index");
    }
%>

<c:if test="${not empty fclBillLaddingformForContainer}">
    <script type="text/javascript">
        parent.parent.getUpdatedContainerDetails('${fclBillLaddingformForContainer.bol}', '${updateContainerWithCharges}');
    </script>
</c:if>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script language="javascript" type="text/javascript">
        var containerChanged = false;
        function isContainerChanged() {
            containerChanged = true;
        }
        function updateContainerDetails(bol) {
            var unitNo = document.fclBillLaddingform.trailerNo.value;
            var breakBulk = jQuery('#breakBulk').val();
            var checkUnitNo = document.getElementById("containerCheck").value;
            if (unitNo !== '' && (unitNo.length < 13 || unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4)) {
                alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
                return;
            }
            if (breakBulk !== "Y") {
                if (unitNo === "") {
                    alertNew('Please Enter Unit Number');
                    return;
                }
                if (document.fclBillLaddingform.sealNo.value === "") {
                    alertNew('Please Enter Seal Number');
                    return;
                }
                if (document.fclBillLaddingform.sizeLegend.value === '0') {
                    alertNew('Please Select Container Size');
                    return;
                }
            }
            if (unitNo !== '' && checkUnitNo.toUpperCase() !== unitNo.toUpperCase()) {
                if (containerChanged) {
                    checkAvailabilty(unitNo, bol, "updateContainerWithCharges");
                } else {
                    checkAvailabilty(unitNo, bol, "updateContainer");
                }
            } else {
                document.getElementById("sizeLegend").disabled = false;
                if (containerChanged) {
                    document.fclBillLaddingform.buttonValue.value = "updateContainerWithCharges";
                } else {
                    document.fclBillLaddingform.buttonValue.value = "updateContainer";
                }
                document.fclBillLaddingform.bol.value = bol;
                document.fclBillLaddingform.submit();
            }
            //parent.parent.GB_hide();
        }
        function validateUnitNumber(obj) {
            var unitNo = obj.value;
            unitNo = unitNo.replace(/-/g, '');
            if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
                alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
                return;
            } else {
                if (unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4) {
                    obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
                }
            }

        }
        function addContainerDetails(bol, breakBulk) {
            var unitNo = document.fclBillLaddingform.trailerNo.value;
            if (unitNo !== '' && (unitNo.length < 13 || unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4)) {
                alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
                return false;
            }
            if (null !== breakBulk && breakBulk === 'Y') {
                document.fclBillLaddingform.buttonValue.value = "addContainer";
                document.fclBillLaddingform.bol.value = bol;
                document.fclBillLaddingform.submit();
            } else {
                if (document.fclBillLaddingform.sizeLegend.value === '0') {
                    alertNew('Please select container Size');
                    return false;
                }
                checkAvailabilty(unitNo, bol, "addContainer");
            }

        }
        function checkAvailabilty(unitNo, bol, action) {
            var unitNo = unitNo;
            if (unitNo !== '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "checkUnitNoAvailabilty",
                        param1: unitNo,
                        param2: bol,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (data) {
                            document.fclBillLaddingform.trailerNo.value = "";
                            document.fclBillLaddingform.trailerNo.focus();
                            alertNew("Unit number is already exist.Please enter different Unit number");
                            return;
                        } else {
                            document.getElementById("sizeLegend").disabled = false;
                            document.fclBillLaddingform.buttonValue.value = action;
                            document.fclBillLaddingform.bol.value = bol;
                            document.fclBillLaddingform.submit();
                        }
                    }
                });
            } else {
                document.fclBillLaddingform.buttonValue.value = action;
                document.fclBillLaddingform.bol.value = bol;
                document.fclBillLaddingform.submit();
            }
        }
        function setToCommetFoucus() {
            if (event.keyCode === 9 || event.keyCode === 13) {
                //document.fclBillLaddingform.marksNo.select();
                setTimeout("set()", 0);
                return false;
            }
        }
        function set() {
            if ('${importFlag}' === 'true' && document.fclBillLaddingform.sizeLegend) {
                document.fclBillLaddingform.sizeLegend.focus();
            } else {
                document.getElementById('marksNo').focus();
                document.getElementById('marksNo').select();
            }
        }
        function allowFreeFormat(val) {
            val.value = val.value.replace(/-/g, '');
        }

        function formatUnitNo(val) {
            if (event.keyCode !== 8 && event.keyCode !== 46) {
            <%--val.value = val.value.replace(/\s/g,'');--%>
                var inputValue = val.value;
                if (inputValue.length < 5 && isNotAlphabetic(inputValue)) {
                    val.value = inputValue.substring(0, inputValue.length - 1);
                    alertNew("Please enter alphabetic value");
                    return;
                }

                if (inputValue.length > 4) {
                    if (inputValue.length === 5) {
                        if (!isInteger(inputValue.substring(4))) {
                            val.value = inputValue.substring(0, inputValue.length - 1);
                            alertNew("Please enter numeric value");
                            return;
                        } else {
                            if (inputValue.length === 5) {
                                val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                            }
                        }
                    } else if (!isInteger(inputValue.substring(5, 11))) {
                        val.value = inputValue.substring(0, inputValue.length - 1);
                        alertNew("Please enter numeric value");
                        return;
                    } else {
                        if (inputValue.length === 12) {
                            if (!isInteger(inputValue.substring(11))) {
                                val.value = inputValue.substring(0, inputValue.length - 1);
                                alertNew("Please enter numeric value");
                                return;
                            } else {
                                val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                            }
                        }
                        if (inputValue.length === 13) {
                            if (!isInteger(inputValue.substring(12))) {
                                val.value = inputValue.substring(0, inputValue.length - 1);
                                alertNew("Please enter numeric value");
                                return;
                            }
                        }
                    }
                }
            }
        }
        function limitTextarea(textarea, maxLines, maxChar) {
            var lines = textarea.value.replace(/\r/g, '').split('\n'),
                    lines_removed,
                    char_removed,
                    i;
            if (maxLines && lines.length > maxLines) {
                alertNew('You can not enter\nmore than ' + maxLines + ' lines');
                lines = lines.slice(0, maxLines);
                lines_removed = 1;
            }
            if (maxChar) {
                i = lines.length;
                while (i-- > 0)
                    if (lines[i].length > maxChar) {
                        lines[i] = lines[i].slice(0, maxChar);
                        char_removed = 1;
                    }
                if (char_removed)
                    alertNew('You can not enter more\nthan ' + maxChar + ' characters per line');
            }
            if (char_removed || lines_removed)
                textarea.value = lines.join('\n');
        }

        function watchTextarea() {
            document.getElementById('marksNo').onkeyup();
        }

        function load(val, importflag) {
            setTimeout("focusTrailerNo()", 100);
            if (importflag === 'true') {
                document.fclBillLaddingform.sizeLegend.disabled = false;
                document.fclBillLaddingform.sizeLegend.className = "textlabelsBoldForTextBox";
            } else if ('${ManifestedBl}') {
                document.fclBillLaddingform.sizeLegend.disabled = true;
                document.fclBillLaddingform.sizeLegend.className = "BackgrndColorForTextBox";
            } else if (val !== "") {
                document.fclBillLaddingform.sizeLegend.disabled = false;
                document.fclBillLaddingform.sizeLegend.className = "textlabelsBoldForTextBox";
            }
        }
        function focusTrailerNo() {
            var trailerNo = document.fclBillLaddingform['trailerNo'].readOnly;
            if (!trailerNo) {
                document.getElementById('trailerNo').focus();
                document.getElementById('trailerNo').select();
            }
        }

        </script>
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
    <html:form action="/fclBillLadding"  name="fclBillLaddingform" type="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm" scope="request">

        <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0">
            <tr class="tableHeadingNew"><font style="font-weight: bold">Container Details</font></tr>
        <tr class="textlabelsBold">
            <td>
                <table width="100%">
                    <tr class="textlabelsBold">
                        <td>Unit No </td>
                        <td>
                            <c:choose>
                                <c:when test="${ManifestedBl!=null || FclBlContainer.trailerNo == 'BBLK-999999-9'}">
                                    <input name="trailerNo" id="trailerNo" value="${FclBlContainer.trailerNo}" size="15"
                                           class="BackgrndColorForTextBox mandatory" readonly="true">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="trailerNo" id="trailerNo" value="${FclBlContainer.trailerNo}" size="15" maxlength="13"  onchange="validateUnitNumber(this)"
                                           class="textlabelsBoldForTextBox mandatory"  style="text-transform: uppercase">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>Seal no #</td>
                        <td><html:text property="sealNo" name="sealNo" style="text-transform: uppercase" maxlength="30"
                                   value="${FclBlContainer.sealNo}" size="15" onkeydown="setToCommetFoucus()" styleClass="textlabelsBoldForTextBox mandatory"/></td>
                        <td></td>
                        <td>
                            <fmt:formatDate pattern="MM/dd/yyyy" var="lastdate" value="${FclBlContainer.lastUpdate}"/>
                            <input id="lastUpdate" name="lastUpdate" value="${lastdate}" readonly="readonly"
                                   class="BackgrndColorForTextBox" size="7" type="hidden"/></td>
                        <td>Size Legend</td>
                        <td>
                            <html:select property="sizeLegend" name="sizeLegend" onchange="isContainerChanged()" styleId="sizeLegend" value="${sizeLegend}"
                                         style="width:120px; font-weight:bold" styleClass="dropdown_accounting">
                                <html:optionsCollection name="unittypelist"/></html:select></td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td>Marks and Numbers
                            <img src="<%=path%>/img/icons/help-icon.gif"
                                 onmouseover="tooltip.show('<strong>Leave Marks and Numbers blank if you want the container# and seal to automatically print on the house and master</strong>', null, event);" onmouseout="tooltip.hide();"/></td>
                        <td><html:textarea property="marksNo" styleId="marksNo" name="marksNo" styleClass="textlabelsBoldForTextBox" onkeyup="limitTextarea(this,40,15)"
                                       value="${FclBlContainer.marks}" cols="23" rows="3" style="text-transform: uppercase"
                                       onfocus="focus_watch=setInterval('watchTextarea()',250)" onblur="clearInterval(focus_watch)"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="center">
                            <c:choose>
                                <c:when test="${param.button=='editContainer'}">
                                    <input type="button" value="Update" id="update" class="buttonStyleNew"
                                           onclick="updateContainerDetails('<%=bol%>')" />
                                </c:when>
                                <c:otherwise>
                                    <input type="button" value="Submit" id="add" class="buttonStyleNew"
                                           onclick="addContainerDetails('<%=bol%>', '${breakBulk}')" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    <html:hidden property="buttonValue"/>
    <html:hidden property="bol"/>
    <html:hidden property="trailerNoOld" styleId="trailerNoOld"/>
    <input type="hidden" id="containerCheck" value="${FclBlContainer.trailerNo}"/>
    <input type="hidden" name="id<%=index%>" value="<%=id%>"/>
    <input type="hidden" name="index" value="<%=index%>"/>
    <c:set var="query" value="select if(count(*) ='0', 'false', 'true') as result from fcl_bl_costcodes where bolid=${bol}"/>
    <c:set var="query" value="${query} and transaction_type not in ('AC','') "/>
    <c:set var="query" value="${query}and Transaction_Type is not null"/>
    <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
</html:form>
</body>
<script type="text/javascript">
    load('${sizeLegend}', '${importFlag}');
    changeSelectBoxOnViewMode();
    if (${param.button eq 'editContainer'} && parent.parent.document.getElementById("ratesNonRates").value !== 'N'
            && (parent.parent.document.fclBillLaddingform.houseBL[2].checked || ${result eq 'true' or ManifestedBl!=null})) {
        document.getElementById("sizeLegend").tabIndex = -1;
        document.getElementById("sizeLegend").disabled = true;
        document.getElementById("sizeLegend").style.border = 0;
        document.getElementById("sizeLegend").style.backgroundColor = "#CCEBFF";
    }
</script>
</html>

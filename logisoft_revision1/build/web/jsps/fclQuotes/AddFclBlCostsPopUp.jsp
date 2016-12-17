<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.domain.User,com.gp.cvst.logisoft.util.DBUtil"%>
<jsp:directive.page import="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<%    boolean importFlag = (null != request.getParameter("importFlag") && request.getParameter("importFlag").equalsIgnoreCase("true") ? true : false);
    request.setAttribute("importFlag", importFlag);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    DBUtil dbUtil = new DBUtil();
    request.setAttribute("defaultcurrency", dbUtil.getGenericFCL1(new Integer(32)));
    String bolId = request.getParameter("bolId");
    String fileNo = request.getParameter("fileNo");
    String codeId = request.getParameter("costId");
    String userName = "";
    Date date = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
    String todaysDate = format.format(date);
    User user = new User();
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
        userName = user.getLoginName();
    }
    String comment = "";
    int length;
    if (request.getAttribute("fclBillLaddingform") != null) {
        FclBillLaddingForm fclBillLaddingForm = (FclBillLaddingForm) request.getAttribute("fclBillLaddingform");
        if (CommonFunctions.isNotNull(fclBillLaddingForm.getCostComments())) {
            comment = fclBillLaddingForm.getCostComments();
            length = comment.length();
            request.setAttribute("length", length);
        }

    }

%>
<c:if test="${not empty fclBillLaddingformForContainer}">
    <script language="javascript">
        parent.parent.getUpdatedCompleteBL();
    </script>
</c:if>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script language="javascript" src="<%=path%>/js/fclBillLanding.js"></script>
        <script language="javascript" src="<%=path%>/js/fclBl.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script language="javascript" type="text/javascript">
        function addCostDetails(ev1, ev2, fileNo) {
            if (document.fclBillLaddingform.costCode.value === "") {
                alertNew("Please enter the cost code");
                return;
            }
            var chargeCode = document.fclBillLaddingform.costCode.value;
            if (chargeCode.trim() === "ADVFF" || chargeCode.trim() === "PBA") {
                if (parent.parent.document.fclBillLaddingform.forwardingAgentName.value === "") {
                    alertNew("Please Select Forwarder to Add this cost Code");
                    return;
                }
            }
            if (chargeCode.trim() === "ADVSHP") {
                if ('${importFlag}' === 'true' && parent.parent.document.fclBillLaddingform.houseShipper.value === "") {
                    alertNew("Please Select Master BL Shipper before adding ADVSHP charge");
                    return false;
                } else if (parent.parent.document.fclBillLaddingform.shipper.value === "") {
                    alertNew("Please Select House BL Shipper before adding ADVSHP charge");
                    return false;
                }
            }
            if (document.fclBillLaddingform.costAmount.value === "" || document.fclBillLaddingform.costAmount.value === "0" ||
                    document.fclBillLaddingform.costAmount.value === "0.0") {
                alertNew("Amount Cannot be Empty or Zero");
                return;
            }
            if (document.fclBillLaddingform.accountName.value === "" && chargeCode.trim() !== "ADVFF" && chargeCode.trim() !== "ADVSHP") {
                alertNew("Please enter the VendorName and Vendor No");
                return;
            }
            appendUserInfoForComments(document.getElementById('costComments'), ev1, ev2);
            var commentValue = document.getElementById('costComments').value.trim();
            commentValue = document.getElementById('previousComments').value + commentValue;//"\r"
            document.fclBillLaddingform.costComments.value = commentValue;
            if(checkAddCostMappingWithGL(chargeCode, fileNo)) {
            document.fclBillLaddingform.buttonValue.value = "addCostDetails";
            document.getElementById("add").style.display = "none";
            document.fclBillLaddingform.submit();
            }
        }
        function updateCostDetails(ev1, ev2) {
            if (document.fclBillLaddingform.costCode.value === "") {
                alertNew("Please enter the cost code");
                return;
            }
            if (document.fclBillLaddingform.accountName.value === "") {
                alertNew("Please enter the VendorName and Vendor No");
                return;
            }
            if (document.fclBillLaddingform.costAmount.value === "" || document.fclBillLaddingform.costAmount.value === "0" ||
                    document.fclBillLaddingform.costAmount.value === "0.0") {
                alertNew("Amount Cannot be Empty or Zero");
                return;
            }
            appendUserInfoForComments(document.getElementById('costComments'), ev1, ev2);
            var commentValue = document.getElementById('costComments').value.trim();
            commentValue = document.getElementById('previousComments').value + commentValue;
            document.fclBillLaddingform.costComments.value = commentValue;
            document.fclBillLaddingform.buttonValue.value = "updateCostDetails";
            document.fclBillLaddingform.submit();
        }
        function fillInvoiceNumber() {
            var costcode = document.fclBillLaddingform.costCode.value;
            if (null !== costcode && costcode === 'DEFER') {
                document.fclBillLaddingform.invoiceNumber.value = '${param.masterBl}';
            } else {
                document.fclBillLaddingform.invoiceNumber.value = '${fclBillLaddingform.invoiceNumber}';
            }
        }
        function checkDisabledPopUP() {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("accountNo").value
                },
                success: function(data) {
                    if (data !== "") {
                        alertNew(data);
                        document.getElementById("accountNo").value = "";
                        document.getElementById("accountName").value = "";
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                                methodName: "getCustInfoForCustNo",
                                param1: document.getElementById("accountNo").value,
                                dataType: "json"
                            },
                            success: function(data) {
                                fillVendorData(data);
                            }
                        });
                    }
                }
            });
        }
        function allowOnlyWholeNumbers(obj) {
            var result;
            if (!/^[1-9 . ]\d*$/.test(obj.value)) {
                result = obj.value.replace(/[^0-9 . ]+/g, '');
                obj.value = result;
                return false;
            }
            return true;
        }
        function checkForNumberAndDecimal(obj) {
            if (!/^-?([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
                obj.value = "";
                alertNew("The amount you entered is not a valid");
                return;
            }
        }
        function  fillVendorData(data) {
            var type;
            var array1 = new Array();
            if (data.acctType !== null) {
                type = data.acctType;
                array1 = type.split(",");
                if (null !== data.acctType && array1.length > 0 && array1.contains("V")) {
                    document.getElementById("accountNo").value = data.acctNo;
                } else {
                    alertNew("Select the customers with Account Type V");
                    document.getElementById("accountNo").value = "";
                    document.getElementById("accountName").value = "";
                }
            }
        }
        
        function checkAddCostMappingWithGL(costCode,fileNo) {
        var flag = true;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkChargeAndCostMappingWithGL",
                param1: costCode,
                param2: fileNo,
                param3: 'AC',
                param4: 'BL'
            },
            async: false,
            success: function (data) {
                if (data !== "") {
                    alertNew("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                    flag = false;
                }
            }
        });
        return flag;
    }

        </script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
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
    <!-- ALERT BOX DESIGN ENDS---->

    <html:form action="/fclBillLadding"  name="fclBillLaddingform" type="com.gp.cvst.logisoft.struts.form.FclBillLaddingForm" scope="request">
        <font color="blue"><b>${message}</b></font>
        <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0">
            <input type="hidden" value="<%=comment.replace("\"", "&#39;")%>" id="previousComments"/>
            <tr class="tableHeadingNew">Add Accruals</tr>
            <tr class="textlabelsBold">
                <td>
                    <table width="100%">
                        <tr class="textlabelsBold">
                            <td>Cost Code Desc</td>
                            <td>
                                <c:choose>
                                    <c:when test="${! empty fclBillLaddingform.costCode && fclBillLaddingform.costCode == 'FAECOMM'}">
                                        <input type="text" name="costCodeDesc" id="costCodeDesc" value="${fclBillLaddingform.costCodeDesc}"
                                               class="BackgrndColorForTextBox"  size="25" maxlength="60"  readonly="readonly" tabindex="-1" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" name="costCodeDesc" id="costCodeDesc" value="${fclBillLaddingform.costCodeDesc}"
                                               class="textlabelsBoldForTextBox"  size="25" maxlength="60"   />
                                    </c:otherwise>
                                </c:choose>
                                <input name="costCodeCheck" id="costCodeCheck" type="hidden" value="${fclBillLaddingform.costCodeDesc}"/>
                                <div id="costCodeChoices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                    initAutocompleteWithFormClear("costCodeDesc", "costCodeChoices", "costCode", "costCodeCheck",
                                            "<%=path%>/actions/autoCompleterForChargeCode.jsp?tabName=FCL_BL_CHARGES&from=7&import=${importFlag}", "fillInvoiceNumber()", "");
                                </script>
                            </td>
                            <td>Cost Code</td>
                            <td>
                                <input type="text" name="costCode" id="costCode" value="${fclBillLaddingform.costCode}"  
                                       class="BackgrndColorForTextBox" size="15" maxlength="10" readonly="readonly" tabindex="-1" />
                            </td>
                            <td align="right">Amount</td>
                            <td>
                                <html:text  property="costAmount" value="${fclBillLaddingform.costAmount}"
                                            styleClass="textlabelsBoldForTextBox" size="8" maxlength="8" onchange="checkForNumberAndDecimal(this);"/></td>
                            <td align="right">Currency</td>
                            <td>
                                <html:text property="costCurrency" value="USD" styleClass="BackgrndColorForTextBox" size="3" readonly="true" tabindex="-1"></html:text>
                                <!--		            <html:select property="costCurrency" value="${fclBillLaddingform.costCurrency}" styleClass="textlabelsBoldForTextBox">-->
                                    <!--         			  <html:optionsCollection name="defaultcurrency"/>-->
                                    <!--         			</html:select>-->

                                </td>
                            </tr>
                            <tr class="textlabelsBold"> 
                                <td valign="top">Vendor Name</td>
                                <td valign="top">
                                <c:choose>
                                    <c:when test="${! empty fclBillLaddingform.costCode && fclBillLaddingform.costCode == 'FAECOMM'}">
                                        <input type="text" name="accountName" id="accountName" value="${fclBillLaddingform.accountName}"
                                               class="BackgrndColorForTextBox"  size="25" maxlength="60"  readonly="readonly" tabindex="-1" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" name="accountName" id="accountName" value="${fclBillLaddingform.accountName}"
                                               class="textlabelsBoldForTextBox" size="25" maxlength="60"/>
                                    </c:otherwise>
                                </c:choose>

                                <input name="accountNameCheck" id="accountNameCheck" type="hidden" value="${fclBillLaddingform.accountName}"/>
                                <div id="accountNameChoices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                    initAutocompleteWithFormClear("accountName", "accountNameChoices", "accountNo", "accountNameCheck",
                                            "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=11&acctTyp=V", "checkDisabledPopUP()", "");
                                </script>
                            </td> 
                            <td valign="top">Vendor Account No</td>
                            <td valign="top">
                                <input type="text" name="accountNo" id="accountNo" value="${fclBillLaddingform.accountNo}" 
                                       class="BackgrndColorForTextBox" size="10" maxlength="20" readonly="readonly" tabindex="-1"/></td>
                            <td align="right">Invoice Number</td>
                            <td colspan="3">
                                <html:text  property="invoiceNumber" value="${fclBillLaddingform.invoiceNumber}"
                                            styleClass="textlabelsBoldForTextBox"  size="29" maxlength="30"/>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">

                            <td>Comments</td>
                            <td valign="top" >
                                <textarea rows="4" cols="32" name="costComments" id="costComments"  style="text-transform: uppercase;"   
                                          onkeypress="return testCommentsLength('<%=comment.replaceAll("(\r\n|\r|\n|\n\r)", "\t")%>', this, 460)"   class="textlabelsBoldForTextBox"></textarea></td>

                            <%
                                if (CommonFunctions.isNotNull(comment) && comment.contains(").")) {%>
                            <td align="right"  >

                                Previous Comments</td>
                            <td  colspan="6" align="right" valign="top">
                                <div class="commentScrollForDiv">
                                    <table border="0" >
                                        <%
                                            String[] arrys = comment.split("\\).");
                                            int size = (null != arrys) ? arrys.length - 1 : 0;
                                            int label = 1;
                                            com.gp.cong.logisoft.util.DBUtil dUtil = new com.gp.cong.logisoft.util.DBUtil();
                                            for (int i = size; i >= 0; i--, label++) {
                                                String resultData = arrys[i];
                                                resultData = dUtil.getData(resultData, 65);
                                        %>
                                        <tr class="textlabelsSmallBold">
                                            <td><%=resultData + ")."%></td>
                                        </tr>
                                        <%
                                            } %>

                                    </table></div>
                            </td>
                            <%}%>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td align="center">
                                <c:choose>
                                    <c:when test="${param.button=='editCostDetails'}">
                                        <input type="button" value="Update" id="update" class="buttonStyleNew" 
                                               onclick="updateCostDetails('<%=userName%>', '<%=todaysDate%>')" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="button" value="Add" id="add" class="buttonStyleNew" 
                                               onclick="addCostDetails('<%=userName%>', '<%=todaysDate%>','<%=fileNo%>')" />
                                    </c:otherwise> 
                                </c:choose>  
                            </td> 
                        </tr>
                    </table>
                </td>
            </tr>	    
        </table>
        <html:hidden property="buttonValue"/>
        <html:hidden property="bol" value="<%=bolId%>"/>
        <html:hidden property="costCodeId"/>
        <html:hidden property="rollUpAmount"/>
    </html:form>		
</body>
</html>
<%@ page import="com.gp.cong.logisoft.domain.User,com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ page language="java" import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <title>Newbatch</title>
        <%

                    String path = request.getContextPath();
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    if (path == null) {
                        path = "../..";
                    }
                    String glaccount = "";
                    Date dateCreated = new Date();
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
                    String batchdate = sdf.format(dateCreated);
                    String userId = "";
                    if (request.getAttribute("GlAccount") != null) {
                        glaccount = (String) request.getAttribute("GlAccount");
                    }
                    DBUtil dbutil = new DBUtil();
                    User user = null;
                    Integer userUniqueId = 0;

                    if (session.getAttribute("loginuser") != null) {
                        user = (User) session.getAttribute("loginuser");
                        userId = user.getLoginName();
                        userUniqueId = user.getUserId();
                    }

                    request.setAttribute("bankAcctList", dbutil.getBankAccountList(userUniqueId, AccountingConstants.NEWARBATCH));
                    String msg = "New Batch";
                    if (request.getAttribute("arNewBatchForm") != null) {
                        msg = "Edit Batch";
                    }

        %>

        <%@include file="../includes/baseResources.jsp" %>

        <script type="text/javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
            dojo.require("dojo.io.*");
            dojo.require("dojo.event.*");
            dojo.require("dojo.html.*");
        </script>
        <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/BankDetailsDAO.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/AccountDetailsDAO.js'></script>
    </head>

    <body class="whitebackgrnd" onload="glaccount();displaydate('${arNewBatchForm.batchNo}','${arNewBatchForm.date}');disableFields();disableFields();">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/arNewbatch"name="arNewBatchForm" type="com.gp.cvst.logisoft.struts.form.ArNewBatchForm" scope="request">
            <html:hidden property="buttonValue"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr class="textlabels">
                    <td colspan="4" align="left" class="headerbluelarge">&nbsp;</td>
                </tr>
                <tr class="textlabels">
                    <td width="100%" align="left" class="headerbluelarge">
                        <c:out value="<%=msg%>"></c:out>

                        <input type="text" name="batchNo" size="26"  value="${arNewBatchForm.batchNo}" readonly="readonly" style="border:0px;font-size:20px;color:blue;" class="bodybackgrnd"/>
                    </td>
                    <td width="100%" align="right" class="headerbluelarge">
                        <!-- <input type="button" class="buttonStyleNew" value="Go Back" onclick="window.location.href = '<%=path%>/jsps/AccountsRecievable/arBatch.jsp'"/> -->
                        <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()"/>

                    </td>

                </tr>
                <tr class="textlabels">
                    <td colspan="4" align="left" class="headerbluelarge">&nbsp;</td>
                </tr>
                <tr class="textlabels">
                    <td colspan="4"align="left" class="headerbluelarge">

                    </td>
                </tr>
            </table>



            <table width="100%" class="tableBorderNew" border="0">
                <tr class="tableHeadingNew"> Batch Details </tr>
                <tr class="textlabels">
                    <td>User ID</td>
                    <td>
                        <c:choose>
                            <c:when test="${arNewBatchForm.userId!=null && arNewBatchForm.userId!=''}">
                                <html:text property="userId" size="26" value="${arNewBatchForm.userId}" readonly="true" style="border:0px;font-size:16px;" styleClass="textlabelsBoldForTextBoxDisabledLook" ></html:text>
                            </c:when>
                            <c:otherwise>
                                <html:text property="userId" size="26" value="<%=userId%>"  readonly="true" style="border:0px;font-size:16px;" styleClass="textlabelsBoldForTextBoxDisabledLook" ></html:text>
                            </c:otherwise>
                        </c:choose>

                    </td>
                    <td align="right">Date :</td>
                    <td >
                        <input name="date" type="text" readonly="readonly" size="26" value="${arNewBatchForm.date}" style="border:0px;font-size:16px;" class="textlabelsBoldForTextBoxDisabledLook"/>
                    </td>
                </tr>
                <tr class="textlabels">
                    <c:choose>
                        <c:when test="${!empty sessionScope.loginuser
                                        && (sessionScope.loginuser.role.roleDesc ==commonConstants.ROLE_NAME_ARCLERK)}">
                                <td colspan="2"></td>
                        </c:when>
                        <c:otherwise>
                            <td > Net Settlement</td>
                            <td>
                                <c:choose>
                                    <c:when test="${!empty arNewBatchForm.netSettleCheck && arNewBatchForm.netSettleCheck=='S'}">
                                        <input type="checkbox" name="netSettleCheck" value="S" disabled="disabled" checked="checked"/>
                                    </c:when>
                                    <c:when test="${!empty arNewBatchForm.netSettleCheck && arNewBatchForm.netSettleCheck=='N'}">
                                        <input type="checkbox" name="netSettleCheck" value="N" disabled="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="checkbox" name="netSettleCheck" value="S" onclick="disableAmtFields()"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:otherwise>
                    </c:choose>
                    <td align="right">Deposit Date </td>
                    <td >
                        <html:text property="depositDate" styleId="txtcal3" styleClass="textlabelsBoldForTextBox"
                                   size="26" style=" width:157px;background-color:#DFDFDF; " readonly="true"value="${arNewBatchForm.depositDate}"></html:text>
                        <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16"
                             height="16" align="top" id="cal3" onmousedown="insertDateFromCalendar(this.id,0);"
                             style="margin-top: 3px" />
                    </td>
                </tr>
                <tr class="textlabels">
                    <td>Total Amount</td>
                    <td>
                        <c:choose>
                            <c:when test="${arNewBatchForm.totalAmount!=null && arNewBatchForm.totalAmount!=''}">
                                <html:text property="totalAmount" size="26" styleId="totalAmount" styleClass="textlabelsBoldForTextBox"
                                           value="${arNewBatchForm.totalAmount}" onchange="TotalAmount();"/>
                            </c:when>
                            <c:otherwise>
                                <html:text property="totalAmount" size="26" styleId="totalAmount" 
										   value="${arNewBatchForm.totalAmount}" onchange="TotalAmount()" styleClass="textlabelsBoldForTextBox"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td align="right">Balance Amount&nbsp; </td>
                    <td colspan="2">
                        <html:text property="balAmount" size="26" styleId="balAmount" styleClass="textlabelsBoldForTextBox"
								   value="${arNewBatchForm.balAmount}"  readonly="true" onchange="TotalAmount()"/>
                        &nbsp;
                    </td>
                </tr>
                <tr class="textlabels">
                    <td>Status</td>
                    <td>
                        <input name="status" readonly type="text" size="26" value="Open" value="${arNewBatchForm.status}" class="textlabelsBoldForTextBox"/>
                    </td>
                    <c:if test="${!empty sessionScope.loginuser
                                  && sessionScope.loginuser.role.roleDesc !=commonConstants.ROLE_NAME_ARCLERK}">
                          <td align="right">Direct GL Account </td>
                          <td colspan="2">
                              <input type="checkbox" name="glAccountCheck" onclick="ableGLAccount()"/>
                          </td>
                    </c:if>
                </tr>

                <tr class="textlabels">
                    <td>Bank Account</td>
                    <td>
                        <html:select property="bankAcct" value="${arNewBatchForm.bankAcct}"
									 styleId="bankAcct" onchange="glaccount()" style="width:155px" styleClass="textlabelsBoldForTextBox">
                            <html:optionsCollection name="bankAcctList" styleClass="textfieldstyle"/>
                        </html:select>
                    </td>
                    <td align="right" valign="top">GL Account</td>
                    <td><input type="text" name="GLAccount" id="GLAccount" size="26" value="${arNewBatchForm.GLAccount}" readonly="true" class="textlabelsBoldForTextBox"/>
					<dojo:autoComplete formId="arNewBatchForm" textboxId="GLAccount" action="<%=path%>/actions/accountNo.jsp?tabName=AR_NEW_BATCH"/>
            </td>

        </tr>

        <tr class="textlabels">
            <td >BankAcct Description</td>
            <td  align="left"><html:textarea property="bankAcctDesc" styleId="bankAcctDesc" style="width:155px" styleClass="textlabelsBoldForTextBox" value="${arNewBatchForm.bankAcctDesc}"></html:textarea></td>

            <td align="right">Notes</td>
            <td  colspan="2"><html:textarea property="notes" value="${arNewBatchForm.notes}" style="width:155px" styleClass="textlabelsBoldForTextBox"></html:textarea>
            </td>
        </tr>

        <tr class="textlabels">
            <td ><br></td>
            <td >

            </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td  align="center" class="headerbluelarge" colspan="4">
                <c:choose>
                    <c:when test="${arNewBatchForm.batchNo!=null && arNewBatchForm.batchNo!=''}">
                        <input type="button" class="buttonStyleNew"   value="Update Batch" style="width:90px" onclick="updateBatch()"/>
                        <input type="button" class="buttonStyleNew" value="UpdateBatch & ApplyPayments" onclick="SavebatchApplypayments()" style="width:180px"/>
                    </c:when>
                    <c:otherwise>
                        <input type="button" class="buttonStyleNew"   value="CreateBatch" style="width:90px" onclick="createBatch()"/>
                        <input type="button" class="buttonStyleNew" value="CreateBatch & ApplyPayments" onclick="SavebatchApplypayments()" style="width:180px"/>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>

    </table>
    <html:hidden property="tempBatchNo" value="${arNewBatchForm.batchNo}"/>
    <html:hidden property="directGlAccount" value="${arNewBatchForm.directGlAccount}"/>
</html:form>
</body>

<script type="text/javascript">
    dwr.engine.setTextHtmlHandler(dwrSessionError);
    function displaydate(val1,val2)
    {

        if(val2!=""){
            document.arNewBatchForm.date.value=val2;
        }else{
            document.arNewBatchForm.date.value="<%=batchdate%>";
            document.arNewBatchForm.depositDate.value="<%=batchdate%>";
        }
        if(val1!=""){
            document.arNewBatchForm.batchNo.value=val1;
        }else{
            //document.arNewBatchForm.batchNo.value=<%=dbutil.getArBatchNumber()%>;


        }

    }
    function createBatch() {
        if(validateFormFields()){
            var depositDate=document.getElementById('txtcal3').value;
            BankDetailsDAO.checkForValidDepositDate(depositDate, function(data){
                if(data=='false'){
                    alert("Please select another period, this period is not yet open or closed");
                    document.getElementById('txtcal3').value="";
                    return false;
                }else{
                    AccountDetailsDAO.validateAccount(document.arNewBatchForm.GLAccount.value,function(valid){
                       if(valid){
                            document.arNewBatchForm.buttonValue.value="createBatch";
                            document.arNewBatchForm.submit();
                       }else{
                           alert("Please enter valid gl account");
                           document.arNewBatchForm.GLAccount.value="";
                           document.arNewBatchForm.GLAccount.focus();
                           return;
                       }
                    });
                }
            });
        }
    }
    function glaccount(){
        var bankGlAcct = document.getElementById('bankAcct');
		if(!document.arNewBatchForm.netSettleCheck.checked){
			grayOutForZeroBatch();
			if(bankGlAcct.value=='0'){
				bankGlAcct.value='';
				document.getElementById('GLAccount').value='';
			}else{
				BankDetailsDAO.getGlAccountNo(bankGlAcct.value,loadGLAcct);
			}
		}
    }

	function grayOutForZeroBatch(){
		var bankAcct = document.getElementById('bankAcct');
		var selected = bankAcct.options[bankAcct.selectedIndex].text;
		var totalAmount = document.arNewBatchForm.totalAmount;
		if(selected.indexOf("Zero batch")>-1
			|| bankAcct.value.indexOf("xxxxxxx")>-1){
			totalAmount.value="0.00";
			totalAmount.readOnly=true;
			totalAmount.style.backgroundColor="#CCEBFF";
		}else{
			totalAmount.readOnly=false;
			totalAmount.style.backgroundColor="#fff";
		}
		TotalAmount()
	}

    function loadGLAcct(data){
        document.getElementById('GLAccount').value=data;
    }
    function TotalAmount(){
        var totalAmount="${arNewBatchForm.totalAmount}";
        var balanceAmount="${arNewBatchForm.balAmount}";
        var totAmt=document.arNewBatchForm.totalAmount.value;
        totAmt=totAmt.replace(/,/g,'');
        var presentTotalAmount=Number(totAmt);
        if(totalAmount!=null || totalAmount!=""){
            var balanceTobeAdded=Number(presentTotalAmount)-Number(totalAmount);
            document.arNewBatchForm.totalAmount.value=presentTotalAmount.toFixed(2);
            balanceTobeAdded=Number(balanceAmount)+Number(balanceTobeAdded);
            document.arNewBatchForm.balAmount.value=balanceTobeAdded.toFixed(2);
        }else{
            document.arNewBatchForm.totalAmount.value=Number(totAmt).toFixed(2);
            document.arNewBatchForm.balAmount.value=Number(totAmt).toFixed(2);
        }
    }
    function SavebatchApplypayments(){
        if(validateFormFields()){
            var depositDate=document.getElementById('txtcal3').value;
            BankDetailsDAO.checkForValidDepositDate(depositDate, function(data){
                if(data=='false'){
                    alert("Please select another period, this period is not yet open or closed");
                    document.getElementById('txtcal3').value="";
                    return false;
                }else{
                    AccountDetailsDAO.validateAccount(document.arNewBatchForm.GLAccount.value,function(valid){
                       if(valid){
                            document.arNewBatchForm.buttonValue.value="createapplyPayments";
                            document.arNewBatchForm.submit();
                       }else{
                           alert("Please enter valid gl account");
                           document.arNewBatchForm.GLAccount.value="";
                           document.arNewBatchForm.GLAccount.focus();
                           return;
                       }
                    });
                }
            });
        }
    }
    function validateFormFields(){

        if(document.arNewBatchForm.depositDate.value=="")
        {
            alert("Please Select Deposit Date");
            document.arNewBatchForm.depositDate.focus();
            return false;
        }
        if(document.arNewBatchForm.netSettleCheck.checked){

        }else{
            if(document.arNewBatchForm.totalAmount.value=="")
            {
                alert("Please Enter TotalAmount");
                document.arNewBatchForm.totalAmount.focus();
                return false;
            }
        }
        if(document.arNewBatchForm.glAccountCheck.checked){
            if(document.arNewBatchForm.GLAccount.value==""){
                alert("Please Enter GLAccount");
                document.arNewBatchForm.GLAccount.focus();
                return false;
            }
        }else{
            if(document.arNewBatchForm.GLAccount.value=="" && document.arNewBatchForm.bankAcct.value=="0")
            {
                alert("Please Select BankAccount or Enter GLAccount");
                document.arNewBatchForm.bankAcct.focus();
                return false;
            }
        }
        if(document.arNewBatchForm.bankAcctDesc.value.length>200){
            alert("BankAccount Description is More than 200 Characters");
            document.arNewBatchForm.bankAcctDesc.focus();
            return false;
        }
        if(document.arNewBatchForm.notes.value.length>200){
            alert("Notes is More than 200 Characters");
            document.arNewBatchForm.notes.focus();
            return false;
        }
        return true;
    }
    function goBack(){
        document.arNewBatchForm.buttonValue.value="goBack";
        document.arNewBatchForm.submit();
    }
    function ableGLAccount(){
        if(document.arNewBatchForm.glAccountCheck.checked){
            document.getElementById("GLAccount").readOnly=false;
            document.getElementById("bankAcct").disabled=true;
            document.getElementById("directGlAccount").value="true";
		} else{
            document.getElementById("GLAccount").readOnly=true;
            document.getElementById("bankAcct").disabled=false;
            document.getElementById("directGlAccount").value="false";
		}
    }
    function disableAmtFields(){
		document.getElementById("directGlAccount").value="false";
        if(document.arNewBatchForm.netSettleCheck.checked){
            document.arNewBatchForm.totalAmount.value="";
            document.arNewBatchForm.balAmount.value="";
            document.arNewBatchForm.totalAmount.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.balAmount.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.bankAcctDesc.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.bankAcct.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.GLAccount.style.backgroundColor = "#DFDFDF";
            document.getElementById("totalAmount").readOnly=true;
            document.getElementById("totalAmount").value="";
            document.getElementById("balAmount").readOnly=true;
            document.getElementById("balAmount").value="";
            document.getElementById("bankAcctDesc").readOnly=true;
            document.getElementById("bankAcctDesc").value="";
            document.getElementById("bankAcct").disabled=true;
            document.getElementById("bankAcct").value="0";
            document.getElementById("GLAccount").readOnly=true;
            getNetSettlementGLAccount();
        }else{
            document.arNewBatchForm.totalAmount.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.balAmount.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.bankAcctDesc.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.bankAcct.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.GLAccount.style.backgroundColor = "#FFFFFF";
            document.getElementById("totalAmount").readOnly=false;
            document.getElementById("balAmount").readOnly=false;
            document.getElementById("bankAcctDesc").readOnly=false;
            document.getElementById("bankAcct").disabled=false;
            document.getElementById("GLAccount").readOnly=true;
            document.getElementById('GLAccount').value="";
        }
    }

    function disableFields(){
        if(document.arNewBatchForm.netSettleCheck.checked){
            document.arNewBatchForm.totalAmount.value="";
            document.arNewBatchForm.balAmount.value="";
            document.arNewBatchForm.totalAmount.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.balAmount.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.bankAcctDesc.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.bankAcct.style.backgroundColor = "#DFDFDF";
            document.arNewBatchForm.GLAccount.style.backgroundColor = "#DFDFDF";
            document.getElementById("totalAmount").readOnly=true;
            document.getElementById("balAmount").readOnly=true;
            document.getElementById("bankAcctDesc").readOnly=true;
            document.getElementById("bankAcct").disabled=true;
            document.getElementById("GLAccount").readOnly=true;
        }else{
            document.arNewBatchForm.totalAmount.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.balAmount.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.bankAcctDesc.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.bankAcct.style.backgroundColor = "#FFFFFF";
            document.arNewBatchForm.GLAccount.style.backgroundColor = "#FFFFFF";
            document.getElementById("totalAmount").readOnly=false;
            document.getElementById("balAmount").readOnly=false;
            document.getElementById("bankAcctDesc").readOnly=false;
            document.getElementById("bankAcct").disabled=false;
            document.getElementById("GLAccount").readOnly=true;
        }
    }


    function  getNetSettlementGLAccount(){
        BankDetailsDAO.getNetSettlementGLAccountNumber(loadGLAcct);
    }
    function updateBatch(){
        if(validateFormFields()){
            var depositDate=document.getElementById('txtcal3').value;
            BankDetailsDAO.checkForValidDepositDate(depositDate, function(data){
                if(data=='false'){
                    alert("Please select another period, this period is not yet open");
                    document.getElementById('txtcal3').value="";
                    return false;
                }else{
                    AccountDetailsDAO.validateAccount(document.arNewBatchForm.GLAccount.value,function(valid){
                       if(valid){
                            document.arNewBatchForm.buttonValue.value="update";
                            document.arNewBatchForm.submit();
                       }else{
                           alert("Please enter valid gl account");
                           document.arNewBatchForm.GLAccount.value="";
                           document.arNewBatchForm.GLAccount.focus();
                           return;
                       }
                    });
                }
            });
        }
    }
</script>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

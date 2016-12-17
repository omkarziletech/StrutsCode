<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@include file="../includes/jspVariables.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
<html>
    <head>
	<base href="${basePath}">
        <title>'bankAccount.jsp'</title>
        <%@include file="../includes/baseResources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <style type="text/css">
            #userLookUpDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                left: 35%;
                top: 35%;
                border-style:solid solid solid solid;
                background-color: #fff;
            }
            .userdiv{
                border: 1px solid #dddddd;
                background-color: white;
                height: 60px;
                width: 147px;
                overflow: auto;
                font: 12px sans-serif,Arial;
                float: left;
            }
            .textlabelsBoldForTextBox{
                width: 147px;
            }
        </style>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body class="whitebackgrnd">
        <div id="cover" style="width: 906px ;height: 1000px;"></div>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form action="/bankAccount" scope="request" name="bankAccountForm" type="com.gp.cvst.logisoft.struts.form.BankAccountForm">
            <c:choose>
                <c:when test="${!empty addBankDetails}">
                    <table width="100%" border="0" cellpadding="2" cellspacing="0"class="tableBorderNew">
                        <tr class="tableHeadingNew">
			    <td colspan="5" align="center">Bank Section</td>
			    <td align="right">
				<input type="button" class="buttonStyleNew" onclick="searchBank()"  value='Search'/>
			    </td>
			</tr>
			<tr class="textlabelsBold">
			    <td>Bank Routing No</td>
			    <td><html:text property="bankRoutingNumber" styleClass="textlabelsBoldForTextBox" maxlength="9" style="text-transform:uppercase;"/></td>
			    <td>Bank Name</td>
			    <td><html:text property="bankName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/></td>
			    <td>Bank Address</td>
			    <td><html:textarea property="bankAddress" rows="2" cols="16" style="text-transform:uppercase"/></td>
			</tr>
			<tr class="tableHeadingNew">
			    <td colspan="6" align="center">Bank Account Section</td>
			</tr>
			<tr class="textlabelsBold" style="padding-top:10px">
			    <td>Bank Account No</td>
			    <td><html:text property="bankAccountNumber" styleId="bankAccountNumber" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/></td>
			    <td>GL Account No</td>
			    <td>
				<html:text property="glAccountNumber" styleId="glAccountNumber" styleClass="textlabelsBoldForTextBox"/>
				<input type="hidden" id="glAccountNumberValid" name="glAccountNumberValid" value="${bankAccountForm.glAccountNumber}"/>
				<div class="newAutoComplete" id="glAccountNumberDiv"></div>
			    </td>
			    <td>Starting Number</td>
			    <td><html:text property="startingNumber" styleId="startingNumber" styleClass="textlabelsBoldForTextBox"/></td>
			</tr>
			<tr class="textlabelsBold">
			    <td>Account Name</td>
			    <td><html:text property="accountName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/></td>
			    <td>Select Check Printer</td>
			    <td>
				<html:select property="checkPrinter" styleId="checkPrinter" style="width:146px" onchange="validatePrinter('CheckPrinter',this)" styleClass="textlabelsBoldForTextBox">
				    <html:optionsCollection name="printersList"/>
				</html:select>
			    </td>
			    <td>Select OverFlow Printer</td>
			    <td>
				<html:select property="overflowPrinter" styleId="overflowPrinter" style="width:146px" onchange="validatePrinter('OverFlowPrinter',this)" styleClass="textlabelsBoldForTextBox">
				    <html:optionsCollection name="printersList"/>
				</html:select>
			    </td>
			</tr>
			<tr class="textlabelsBold">
			    <td>User Name</td>
			    <td>
                                <html:hidden property="userName" styleId="userName" value="${bankAccountForm.userName}"/>
				<div id="userdiv" class="userdiv">
				    <c:forEach var="userName" items="${userNameArray}">
					<c:out value="${userName} "></c:out>
					<img alt="remove user" src='${path}/img/icons/remove_user.gif' id="${userName}"
					     style="cursor: pointer;" onclick="deleteUser('${userName}')"/>
					<br/>
				    </c:forEach>
				</div>
				<div style="float:left;vertical-align: bottom">
				    <img alt="Assign User" style="cursor: pointer;vertical-align: bottom" src="${path}/img/icons/add-user.png" onclick="addUserName()"
					 onmouseover="tooltip.show('<strong>Assign User</strong>', null, event);" onmouseout="tooltip.hide();"/>
				</div>
			    </td>
			    <td>Bank Email</td>
                            <td><html:text property="bankEmail" styleId="bankEmail" styleClass="textlabelsBoldForTextBox"/></td>
			    <td colspan="2">
				<c:set var="buttonValue" value="Save"/>
				<c:if test="${!empty bankAccountForm.bankAccountId}">
				    <c:set var="buttonValue" value="Update"/>
				</c:if>
				<input type="button" class="buttonStyleNew" value="${buttonValue}"
				       style='width: 60px' onclick="saveBackAccount('${bankAccountForm.bankAccountId}')">
			    </td>
			</tr>
		    </table>
		</c:when>
		<c:otherwise>
		    <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew">
			    <td colspan="2">
				Search Bank Details
			    </td>
			    <td align="right">
				<c:if test="${roleDuty.bankAccountCreateNew}">
				    <input type="button" class="buttonStyleNew" onclick="addBankDeatils()" value='Add Bank Details' style='width:90px'/>
				</c:if>
			    </td>
			</tr>
			<tr>
			    <td class="textlabelsBold" colspan="3">Bank Name
				<html:text property="bankName" styleId="bankName" value="${bankAccountDetails.bankName}" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
				<input name="bankAccountNumber" id="bankAccountNumber"  type="hidden" value="${bankAccountDetails.bankAccountNumber}"/>
				<input name="bankNameCheck" id="bankNameCheck"  type="hidden" value="${bankAccountDetails.bankName}"/>
				<div id="bankNameChoices" style="display: none" class="autocomplete"></div>
				&nbsp;&nbsp;
				<input type="button" class="buttonStyleNew" value='Go' onclick="searchBankList()"/>
				<input type="button" class="buttonStyleNew" value='Clear' onclick="clearBankList()"/>
			    </td>
			</tr>
			<tr class="tableHeadingNew">
			    <td colspan="3">
				List of Bank Details
			    </td>
			</tr>
			<tr>
			    <td colspan="3">
				<div id="divtablesty1" class="scrolldisplaytable">
				    <display:table name="${bankAccountList}" pagesize="<%=pageSize%>"
						   style="width:100%"
						   class="displaytagstyleNew" id="bankDetails" sort="list">
					<display:setProperty name="paging.banner.some_items_found">
					    <span class="pagebanner"> <font color="blue">{0}</font>
						Bank	Account Details displayed,For more Records click on page numbers. </span>
					    </display:setProperty>
					    <display:setProperty name="paging.banner.one_item_found">
					    <span class="pagebanner"> One {0} displayed. Page Number
					    </span>
					</display:setProperty>
					<display:setProperty name="paging.banner.all_items_found">
					    <span class="pagebanner"> {0} {1} Displayed, Page Number
					    </span>
					</display:setProperty>
					<display:setProperty name="basic.msg.empty_list">
					    <span class="pagebanner"> No Records Found. </span>
					</display:setProperty>
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:setProperty name="paging.banner.item_name" value="Bank Detail"/>
					<display:setProperty name="paging.banner.items_name" value="Bank Details"/>
					<display:column title="Bank<br/>Name" maxLength="20" property="bankName" sortable="true" 
							headerClass="sortable" style="text-transform:uppercase;"></display:column>
					<display:column title="Bank<br/>Account No" maxLength="10" property="bankAcctNo" sortable="true" 
							headerClass="sortable" style="text-transform:uppercase;"></display:column>
					<display:column title="Account<br/>Name" property="acctName"  maxLength="10" sortable="true" 
							headerClass="sortable" style="text-transform:uppercase;"></display:column>
					<display:column title="GL Account<br/>Number" maxLength="10" property="glAccountno" sortable="true" headerClass="sortable"></display:column>
					<display:column title="Bank Routing<br/>Number" maxLength="10"  property="bankRoutingNumber" sortable="true" headerClass="sortable"></display:column>
					<display:column title="Starting<br/>Number" maxLength="10" property="startingSerialNo" sortable="true" headerClass="sortable"></display:column>
					<display:column title="Check<br/>Printer" maxLength="18" property="checkPrinter" sortable="true" headerClass="sortable"></display:column>
					<display:column title="Overflow<br/>Printer" maxLength="18" property="overflowPrinter" sortable="true" headerClass="sortable"></display:column>
					<display:column title="Login<br/>Name" property="loginName" maxLength="10" sortable="true" 
							headerClass="sortable" style="text-transform:uppercase;"></display:column>
					<c:if test="${roleDuty.bankAccountCreateNew}">
					    <display:column title="<br/>Actions" >
                                                <img alt="" title="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="editBankDetails('${bankDetails.id}')"/>
                                                <img alt="" title="Ach SetUp" src="${path}/img/icons/ach_setup2.png" border="0" onclick="showAchSetUp('${bankDetails.id}')" />
                                                <img alt="" title="Delete" src="${path}/img/icons/delete.gif" onclick="deleteBankDetails('${bankDetails.id}')" />
                                                <img alt="" title="Notes" src="${path}/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '${path}/notes.do?moduleId=' + '${notesConstants.BANK_ACCOUNT}&moduleRefId=${bankDetails.bankName}-${bankDetails.bankAcctNo}', 300, 900);"/>
					    </display:column>
					</c:if>
				    </display:table>
				</div>
			    </td>
			</tr>
		    </table>
		</c:otherwise>
	    </c:choose>
	    <html:hidden property="button" styleId="button"/>
	    <html:hidden property="bankAccountId" styleId="bankAccountId"/>
	</html:form>
    </body>
    <script type="text/javascript">
	function saveBackAccount(bankAccountNo) {
	    if (document.bankAccountForm.bankAccountNumber.value == "") {
		alert("Please select Bank Account Number ");
		return;
	    }
	    if (document.bankAccountForm.userName.value == "") {
		alert("Please select User Name ");
		return;
	    }
	    if (document.bankAccountForm.glAccountNumber.value == "") {
		alert("Please select GL Account Number ");
		return;
	    }
	    if (document.bankAccountForm.bankName.value == "") {
		//alert("Please select Bank Account Name ");
		//return;
	    }
	    if (document.bankAccountForm.bankAddress.value == "") {
		//alert("Please select Bank Account Address ");
		//return;
	    }
	    if (bankAccountNo != "") {
		document.bankAccountForm.button.value = "updateBankAccount";
	    } else {
		document.bankAccountForm.button.value = "saveBankAccount";
	    }
	    document.bankAccountForm.submit();
	}

	function addBankDeatils() {
	    document.bankAccountForm.button.value = "addBankDetails";
	    document.bankAccountForm.submit();
	}

	function searchResults() {
	    if (event.keyCode == 13 || (event.keyCode == 9) || (event.button == 0)) {
		searchBankList();
	    }
	}
	function searchBank() {
	    document.bankAccountForm.button.value = "searchBank";
	    document.bankAccountForm.submit();
	}
	function searchBankList() {
	    document.bankAccountForm.button.value = "getBankDetails";
	    document.bankAccountForm.submit();
	}
	function clearBankList() {
	    document.bankAccountForm.button.value = "clearBankDetails";
	    document.bankAccountForm.submit();
	}
	function editBankDetails(val1) {
	    document.bankAccountForm.bankAccountId.value = val1
	    document.bankAccountForm.button.value = "editBankDetails";
	    document.bankAccountForm.submit();
	}
	function deleteBankDetails(val1) {
	    document.bankAccountForm.bankAccountId.value = val1
	    document.bankAccountForm.button.value = "deleteBankDetails";
	    document.bankAccountForm.submit();
	}

	function addMoreParams(element, entry) {
	    return entry;
	}

	function addUserName() {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "addUserNameForBankAccount",
                    forward: "/jsps/AccountsPayable/BankAccountUsersTemplate.jsp",
                    request: true
                },
                success: function (data) {
                    if (data) {
                        showPopUp();
                        var userLookUpDiv = createHTMLElement("div", "userLookUpDiv", "45%", "20%", document.body);
                        jQuery("#userLookUpDiv").html(data);
                        floatDiv("userLookUpDiv", document.body.offsetWidth / 4, document.body.offsetHeight / 4).floatIt();
                        document.getElementById('user').focus();
                        AjaxAutocompleter("user", "userNameDiv", "", "userValid", "${path}/servlet/AutoCompleterServlet?action=User&textFieldId=user", "addUser()", "");
                    }
                }
            });
	}
	function closeAddUserName() {
	    document.body.removeChild(document.getElementById("userLookUpDiv"));
	    closePopUp();
	}
	function addUser() {
	    if ((event.keyCode == 13) || (event.keyCode == 9) || (event.button == 0)) {
		if (jQuery("#userName").val() != '') {
		    var result = validateUserName();
		    if (result) {
			closeAddUserName();
		    }
		} else {
		    jQuery("#userName").val(jQuery("#user").val() + ",");
		    var user = jQuery("#user").val();
		    var closeImage = "<img src='${path}/img/icons/remove_user.gif' style=\"cursor: pointer;\" id='" + user + "'  onclick=\"deleteUser('" + user + "')\"/>";
                    jQuery("#userdiv").html(user+" "+closeImage+"<br/>");
		    closeAddUserName();
		}
	    }
	}

	function deleteUser(user) {
	    var image = document.getElementById(user);
	    image.parentNode.removeChild(image);
	    var userdiv = document.getElementById('userdiv').innerHTML;
	    var userName = jQuery("#userName").val();
	    var index = userName.indexOf(",");
	    if (index >= 0) {
		var userArray = userName.split(",");
		if (userArray.length == 1) {
		    userName = "";
		    userdiv = "";
		} else {
		    userName = "";
		    userdiv = "";
		    var j = 0;
		    for (var i = 0; i < userArray.length; i++) {
			if (user != userArray[i] && trim(userArray[i]) != "" && user != "") {
			    var closeImage = "<img src='${path}/img/icons/remove_user.gif' style=\"cursor: pointer;\" id='" + userArray[i] + "' onclick=\"deleteUser('" + userArray[i] + "')\"/>";
			    if (j == 0) {
				userName = userArray[i];
				userdiv = userArray[i] + " " + closeImage + "<br>";
			    } else {
				var addUser = userArray[i] + "  ";
				userdiv = userdiv.substring(0, userdiv.length - 4);
				var userdivArray = userdiv.split("<br>");
				if (userdivArray[userdivArray.length - 1].length > 160) {
				    userdiv = userdiv + "<br>";
				} else if ((userdivArray[userdivArray.length - 1].length + addUser.length) > 160) {
				    userdiv = userdiv + "<br>";
				}
				userdiv = userdiv + userArray[i] + " " + closeImage + "<br>";
				userName = userName + "," + userArray[i];
			    }
			    j++;
			}
		    }
		}
	    } else {
		userName = "";
		userdiv = "";
	    }
            jQuery("#userName").val(userName);
            jQuery("#userdiv").html(userdiv);
	}

	function validateUserName() {
	    var userArray = jQuery("#userName").val().split(",");
	    var i = 0;
	    for (i = 0; i < userArray.length; i++) {
		if (userArray[i] == jQuery("#user").val()) {
		    alert("User already added...Please select another user...");
                    jQuery("#user").val("");
		    return false;
		}
	    }
	    if (i == userArray.length) {
		var userdiv = document.getElementById('userdiv');
		var user = jQuery("#user").val();
		var closeImage = "<img src='${path}/img/icons/remove_user.gif' style=\"cursor: pointer;\" id='" + user + "' onclick=\"deleteUser('" + user + "')\"/>";
		var addUser = user + "  ";
		userdiv.innerHTML = userdiv.innerHTML.substring(0, userdiv.innerHTML.length - 4);
		var userArray = userdiv.innerHTML.split("<br>");
		if (userArray[userArray.length - 1].length > 160) {
		    userdiv.innerHTML = userdiv.innerHTML + "<br>";
		} else if ((userArray[userArray.length - 1].length + addUser.length) > 160) {
		    userdiv.innerHTML = userdiv.innerHTML + "<br>";
		}
		userdiv.innerHTML = userdiv.innerHTML + jQuery("#user").val() + " " + closeImage + "<br>";
                jQuery("#userName").val(jQuery("#userName").val()+","+jQuery("#user").val());
		return true;
	    }
	}
	function validatePrinter(printerType, printerObject) {
	    if (printerObject.value != "0") {
		if (printerType == "CheckPrinter") {
		    var overFlowPrinter = jQuery("#overflowPrinter").val();
		    var checkPrinter = printerObject.value;
		    if (overFlowPrinter == checkPrinter && overFlowPrinter != "0") {
			alert("Check Printer and Overflow Printer should not be same. Please Select another one.");
			printerObject.focus();
			printerObject.value = "0";
			return false;
		    }
		} else {
		    var overFlowPrinter = printerObject.value;
		    var checkPrinter = jQuery("#checkPrinter").val();
		    if (overFlowPrinter == checkPrinter && checkPrinter != "0") {
			alert("Check Printer and Overflow Printer should not be same. Please Select another one.");
			printerObject.focus();
			printerObject.value = "0";
			return false;
		    }
		}
	    }
	}
	function addMoreParams(element, entry) {
	    return entry;
	}
	if (document.getElementById("glAccountNumber")) {
	    AjaxAutocompleter("glAccountNumber", "glAccountNumberDiv", "", "glAccountNumberValid", "${path}/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=glAccountNumber&tabName=BANKACCOUNT", "", "");
	}
	if (document.getElementById("bankNameChoices")) {
	    AjaxAutocompleter("bankName", "bankNameChoices", "bankAccountNumber", "bankNameCheck", "${path}/servlet/AutoCompleterServlet?action=Bank&textFieldId=bankName&tabName=BANKACCOUNT", "searchResults()");
	}
	function showAchSetUp(bankId) {
	    GB_show("Ach Set Up", "${path}/achSetUp.do?buttonAction=showAchSetUp&bankId=" + bankId, 375, 800);
	}
    </script>
</html>

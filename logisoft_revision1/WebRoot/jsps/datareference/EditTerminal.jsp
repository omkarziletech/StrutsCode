<%@page import="com.gp.cong.logisoft.struts.form.EditTerminalForm"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"
         import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.TerminalBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<jsp:useBean id="termForm" class="com.gp.cong.logisoft.struts.form.EditTerminalForm" scope="request"></jsp:useBean>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    DBUtil dbUtil = new DBUtil();
    request.setAttribute("terminaltypelist", dbUtil.getGenericCodeList(
            18, "yes", "Select Terminal Type"));
    //request.setAttribute("countrylist", dbUtil.getGenericCodeList(11,"yes", "Select Country"));
    request.setAttribute("acflist", dbUtil.getacfList());
    RefTerminal terminal = new RefTerminal();
    GenericCode genericCode = null;
    String countryId = "";
    String cityId = "";
    String unLoc = "";
    String msg = "";
    String state = "";
    String terminalType = "0";
    String chargeCode = "";
    String brlChgCode = "";
    String over10kchgcode = "";
    String over20kchgcode = "";
    String docChgCode = "";
    String modify = "";
    String phone1 = "";
    String phone2 = "";
    String phone3 = "";
    String extension1 = "";
    String extension2 = "";
    String extension3 = "";
    String fax1 = "";
    String fax2 = "";
    String fax3 = "";
    String fax4 = "";
    String fax5 = "";
    String unLocationCode1 = "";
    String acctno = "";
    String docDeptEmail = "";
    String zaccount = "";
    String exportsBillingTerminalEmail = "";
    String docDeptName = "";
    String customerServiceName = "";
    String customerServiceEmail = "";
    String fclExportIssuingTerminal = "";
    String intraBookerId = "";
    String importsDoorDeliveryEmail = "";
    String lclDocDeptName = "";
    String lclDocDeptEmail = "";
    String lclCustomerServiceName = "";
    String lclCustomerServiceEmail = "";

    List cities = new ArrayList();
    TerminalBean terminalBean = new TerminalBean();
    if (request.getAttribute("terminalBean") != null) {
        terminalBean = (TerminalBean) request.getAttribute("terminalBean");
    }
    if (request.getAttribute("message") != null) {
        msg = (String) request.getAttribute("message");
    }
    if (session.getAttribute("terminal") != null) {
        terminal = (RefTerminal) session.getAttribute("terminal");
        if (terminal.getAirsrvc() != null && terminal.getAirsrvc().equals("Y")) {
            termForm.setAirsrvc("on");
        } else {
            termForm.setAirsrvc("off");
        }
        if (terminal.getPhnnum1() != null) {
            phone1 = terminal.getPhnnum1();
        }
        if (terminal.getPhnnum2() != null) {
            phone2 = terminal.getPhnnum2();
        }
        if (terminal.getPhnnum3() != null) {
            phone3 = terminal.getPhnnum3();
        }
        if (terminal.getFaxnum1() != null) {
            fax1 = terminal.getFaxnum1();
        }
        if (terminal.getFaxnum2() != null) {
            fax2 = terminal.getFaxnum2();
        }
        if (terminal.getFaxnum3() != null) {
            fax3 = terminal.getFaxnum3();
        }
        if (terminal.getFaxnum4() != null) {
            fax4 = terminal.getFaxnum4();
        }
        if (terminal.getFaxnum5() != null) {
            fax5 = terminal.getFaxnum5();
        }
        if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getCountryId() != null) {
            countryId = ((GenericCode) terminal.getUnLocation().getCountryId()).getCodedesc();
        }
        if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getUnLocationName() != null) {
            cityId = terminal.getUnLocation().getUnLocationName();
        }
        if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getUnLocationCode() != null) {
            unLoc = terminal.getUnLocation().getUnLocationCode();
        }
        if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getStateId() != null && terminal.getUnLocation().getStateId().getCode() != null) {
            state = ((GenericCode) terminal.getUnLocation().getStateId()).getCode();
        }
        if (terminal != null && terminal.getGenericCode() != null
                && terminal.getGenericCode().getCodedesc() != null) {
            terminalType = terminal.getGenericCode().getId().toString();
        }

        if (terminal != null && terminal.getGenericCode1() != null
                && terminal.getGenericCode1().getCode() != null) {
            chargeCode = terminal.getGenericCode1().getCode();
        }
        if (terminal != null && terminal.getGenericCode2() != null
                && terminal.getGenericCode2().getCode() != null) {
            brlChgCode = terminal.getGenericCode2().getCode().toString();
        }
        if (terminal != null && terminal.getGenericCode3() != null
                && terminal.getGenericCode3().getCode() != null) {
            over10kchgcode = terminal.getGenericCode3().getCode();
        }

        if (terminal != null && terminal.getGenericCode4() != null
                && terminal.getGenericCode4().getCode() != null) {
            over20kchgcode = terminal.getGenericCode4().getCode();
        }
        if (terminal != null && terminal.getGenericCode5() != null
                && terminal.getGenericCode5().getCode() != null) {
            docChgCode = terminal.getGenericCode5().getCode();
        }
        if (terminal.getUnLocCode() != null) {
            unLoc = terminal.getUnLocCode();
        }
        if (terminal.getUnLocationCode1() != null) {
            unLocationCode1 = terminal.getUnLocationCode1();
        }
        if (terminal.getTpacctno() != null) {
            acctno = terminal.getTpacctno();
        }
        if (terminal.getDocDeptEmail() != null) {
            docDeptEmail = terminal.getDocDeptEmail();
        }
        if (terminal.getZaccount() != null) {
            zaccount = terminal.getZaccount();
        }
        if (terminal.getExportsBillingTerminalEmail() != null) {
            exportsBillingTerminalEmail = terminal.getExportsBillingTerminalEmail().toLowerCase();
        }
        if (terminal.getDocDeptName() != null) {
            docDeptName = terminal.getDocDeptName();
        }
        if (terminal.getCustomerServiceName() != null) {
            customerServiceName = terminal.getCustomerServiceName();
        }
        if (terminal.getCustomerServiceEmail() != null) {
            customerServiceEmail = terminal.getCustomerServiceEmail();
        }
        if (terminal.getFclExportIssuingTerminal() != null) {
            fclExportIssuingTerminal = terminal.getFclExportIssuingTerminal();
        }
        if (terminal.getIntraBookerId() != null) {
            intraBookerId = terminal.getIntraBookerId();
        }
        if (terminal.getImportsDoorDeliveryEmail() != null) {
            importsDoorDeliveryEmail = terminal.getImportsDoorDeliveryEmail();
        }
        if (terminal.getLclDocDeptName() != null) {
            lclDocDeptName = terminal.getLclDocDeptName();
        }
        if (terminal.getLclDocDeptEmail() != null) {
            lclDocDeptEmail = terminal.getLclDocDeptEmail();
        }
        if (terminal.getLclCustomerServiceName() != null) {
            lclCustomerServiceName = terminal.getLclCustomerServiceName();
        }
        if (terminal.getLclCustomerServiceEmail() != null) {
            lclCustomerServiceEmail = terminal.getLclCustomerServiceEmail();
        }
    }
    if (genericCode != null) {
        cities = dbUtil.getCityList(genericCode);
    }
    request.setAttribute("citylist", cities);
    request.setAttribute("term", terminal);
    modify = (String) session.getAttribute("modifyforterminal");
    session.setAttribute("contactmodify", modify);
    session.setAttribute("notesmodify", modify);

    if (session.getAttribute("view") != null) {
        modify = (String) session.getAttribute("view");
        session.setAttribute("contactmodify", modify);
        session.setAttribute("notesmodify", modify);
    }
%>
<html:html>
    <head>
        <base href="<%=basePath%>">
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <title>JSP for TerminalManagementForm form</title>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js"></script>
        <script language="javascript">
            start = function () {
                serializeForm();
                checkOnload();
            }
            window.onload = start;
        </script>
        <script>
            function checkOnload() {
                if (document.getElementById("radioId").value == "Y") {
                    document.getElementById("fclExportIssuingTerminalY").checked = true;
                } else {
                    document.getElementById("fclExportIssuingTerminalN").checked = true;
                }
            }
            function popup1(mylink, windowname) {
                if (!window.focus) {
                    return true;
                }
                var href;
                if (typeof (mylink) == "string") {
                    href = mylink;
                } else {
                    href = mylink.href;
                }
                mywindow = window.open(href, windowname, "width=400,height=250,scrollbars=yes");
                mywindow.moveTo(200, 180);
                document.editTerminalForm.submit();
                return false;
            }
            function addTerminalManagers(turmNo) {
                var url = "${path}" + "/terminalManagement.do?buttonValue=addTerminalUser&turmNo=" + turmNo;
                GB_show("TerminalManager", url + "&", 400, 800);
            }
            function savecontact() {
                if (isAlpha(document.editTerminalForm.termNo.value) == false) {
                    alert("Special Characters not allowed for Term No.");
                    document.editTerminalForm.termNo.value = "";
                    document.editTerminalForm.termNo.focus();
                    return;
                }
                if (isAlpha(document.editTerminalForm.govSchCode.value) == false) {
                    alert("Special Characters not allowed for GovSchCode.");
                    document.editTerminalForm.govSchCode.value = "";
                    document.editTerminalForm.govSchCode.focus();
                    return;
                }
                if (document.editTerminalForm.termNo.value == "") {
                    alert("Please enter the term no");
                    document.editTerminalForm.termNo.value = "";
                    document.editTerminalForm.termNo.focus();
                    return;
                }
                var val = document.editTerminalForm.termNo.value;
                if (val.match(" ")) {
                    alert("WhiteSpace is not allowed for term no");
                    return;
                }
                /*if (document.editTerminalForm.city.value == "") {
                 alert("Please enter the city");
                 document.editTerminalForm.city.value = "";
                 document.editTerminalForm.city.focus();
                 return;
                 }
                 if (document.editTerminalForm.addressLine1.value == "") {
                 alert("Please enter the Address1");
                 document.editTerminalForm.addressLine1.value = "";
                 document.editTerminalForm.addressLine1.focus();
                 return;
                 }*/
                if (document.editTerminalForm.addressLine1.value.length > 50) {
                    alert("Please enter the Address1");
                    document.editTerminalForm.addressLine1.value = "";
                    document.editTerminalForm.addressLine1.focus();
                    return;
                }
                if (document.editTerminalForm.addressLine2.value != "" && document.editTerminalForm.addressLine2.value.length > 26) {
                    alert("Please enter the Address2");
                    document.editTerminalForm.addressLine2.value = "";
                    document.editTerminalForm.addressLine2.focus();
                    return;
                }
                if (IsNumeric(document.editTerminalForm.zip.value) == false) {
                    alert("Zipcode should be Numeric.");
                    document.editTerminalForm.zip.value = "";
                    document.editTerminalForm.zip.focus();
                    return;
                }
                //			var value = document.editTerminalForm.phoneNo1.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Phone number1 dont start with white space");
                //					return;
                //				}
                //			}
                if (IsNumeric(document.editTerminalForm.phoneNo1.value.replace(/ /g, "")) == false) {
                    alert("Telephone Number1 should be Numeric.");
                    document.editTerminalForm.phoneNo1.value = "";
                    document.editTerminalForm.phoneNo1.focus();
                    return;
                }
                //			if (document.editTerminalForm.phoneNo1.value != "" && document.editTerminalForm.phoneNo1.value.length < 10) {
                //				alert("Phone Number should be 10 Digits");
                //				document.editTerminalForm.phoneNo1.value = "";
                //				document.editTerminalForm.phoneNo1.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.phoneNo2.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Phone number2 dont start with white space");
                //					return;
                //				}
                //			}
                if (IsNumeric(document.editTerminalForm.phoneNo2.value.replace(/ /g, "")) == false) {
                    alert("Telephone Number2 should be Numeric.");
                    document.editTerminalForm.phoneNo2.value = "";
                    document.editTerminalForm.phoneNo2.focus();
                    return;
                }
                //			if (document.editTerminalForm.phoneNo2.value != "" && document.editTerminalForm.phoneNo2.value.length < 10) {
                //				alert("Phone Number should be 10 Digits");
                //				document.editTerminalForm.phoneNo2.value = "";
                //				document.editTerminalForm.phoneNo2.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.phoneNo3.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Phone number3 dont start with white space");
                //					return;
                //				}
                //			}
                if (IsNumeric(document.editTerminalForm.phoneNo3.value.replace(/ /g, "")) == false) {
                    alert("Telephone Number3 should be Numeric.");
                    document.editTerminalForm.phoneNo3.value = "";
                    document.editTerminalForm.phoneNo3.focus();
                    return;
                }
                //			if (document.editTerminalForm.phoneNo3.value != "" && document.editTerminalForm.phoneNo3.value.length < 10) {
                //				alert("Phone Number should be 10 Digits");
                //				document.editTerminalForm.phoneNo3.value = "";
                //				document.editTerminalForm.phoneNo3.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.faxNo1.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Fax number1 dont start with white space");
                //					return;
                //				}
                //			}
                //			if (document.editTerminalForm.faxNo1.value != "" && document.editTerminalForm.faxNo1.value.length < 10) {
                //				alert("Fax Number should be 10 Digits");
                //				document.editTerminalForm.faxNo1.value = "";
                //				document.editTerminalForm.faxNo1.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.faxNo2.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Fax number2  dont start with white space");
                //					return;
                //				}
                //			}
                //			if (document.editTerminalForm.faxNo2.value != "" && document.editTerminalForm.faxNo2.value.length < 10) {
                //				alert("Fax Number should be 10 Digits");
                //				document.editTerminalForm.faxNo2.value = "";
                //				document.editTerminalForm.faxNo2.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.faxNo3.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Fax number3  dont start with white space");
                //					return;
                //				}
                //			}
                //			if (document.editTerminalForm.faxNo3.value != "" && document.editTerminalForm.faxNo3.value.length < 10) {
                //				alert("Fax Number should be 10 Digits");
                //				document.editTerminalForm.faxNo3.value = "";
                //				document.editTerminalForm.faxNo3.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.faxNo4.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Fax number4  dont start with white space");
                //					return;
                //				}
                //			}
                //			if (document.editTerminalForm.faxNo4.value != "" && document.editTerminalForm.faxNo4.value.length < 10) {
                //				alert("Fax Number should be 10 Digits");
                //				document.editTerminalForm.faxNo4.value = "";
                //				document.editTerminalForm.faxNo4.focus();
                //				return;
                //			}
                //			var value = document.editTerminalForm.faxNo5.value;
                //			for (var i = 0; i < value.length; i++) {
                //				if (value.indexOf(" ") != -1) {
                //					alert("Please Fax number5  dont start with white space");
                //					return;
                //				}
                //			}
                //			if (document.editTerminalForm.faxNo5.value != "" && document.editTerminalForm.faxNo5.value.length < 10) {
                //				alert("Fax Number should be 10 Digits");
                //				document.editTerminalForm.faxNo5.value = "";
                //				document.editTerminalForm.faxNo5.focus();
                //				return;
                //			}
                if (document.editTerminalForm.notes.value != "" && document.editTerminalForm.notes.value.length > 140) {
                    alert("Notes should be 140 letters");
                    document.editTerminalForm.notes.value = "";
                    document.editTerminalForm.notes.focus();
                    return;
                }
                document.editTerminalForm.buttonValue.value = "update";
                document.editTerminalForm.submit();
            }
            function terminalcontacts() {
                document.editTerminalForm.buttonValue.value = "terminalcontact";
                document.editTerminalForm.submit();
            }
            function confirmdelete() {
                document.editTerminalForm.buttonValue.value = "delete";
                var result = confirm("Are you sure you want to delete this terminal");
                if (result) {
                    document.editTerminalForm.submit();
                }
            }
            function chkall() {
                if (document.editTerminalForm.airsrvc.checked) {
                    alert("chkall");
                    document.editTerminalForm.airsrvc.value = "Y";
                    document.editTerminalForm.airsrvc.focus();
                    return false;
                }
            }
            function disabled(val1, val2) {
                if (val1 == 0 || val1 == 3) {
                    var imgs = document.getElementsByTagName("img");
                    for (var k = 0; k < imgs.length; k++) {
                        if (imgs[k].id != "cancel" && imgs[k].id != "terminalcontact" && imgs[k].id != "note") {
                            imgs[k].style.visibility = "hidden";
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for (i = 0; i < input.length; i++) {
                        if (input[i].id != "buttonValue" && input[i].name != "govSchCode" && input[i].name != "termNo" && input[i].name != "scheduleSuffix" && input[i].name != "state" && input[i].name != "country") {
                            input[i].readOnly = true;
                            input[i].style.color = "blue";
                        }
                    }
                    var textarea = document.getElementsByTagName("textarea");
                    for (i = 0; i < textarea.length; i++) {
                        textarea[i].readOnly = true;
                        textarea[i].style.color = "blue";
                    }
                    var select = document.getElementsByTagName("select");
                    for (i = 0; i < select.length; i++) {
                        select[i].disabled = true;
                        select[i].style.backgroundColor = "blue";
                    }
                    document.getElementById("save").style.visibility = "hidden";
                    document.getElementById("delete").style.visibility = "hidden";
                }
                if (val1 == 1) {
                    document.getElementById("delete").style.visibility = "hidden";
                }
                if (val1 == 3 && val2 != "") {
                    alert(val2);
                }
            }
            function confirmnote() {
                document.editTerminalForm.buttonValue.value = "note";
                document.editTerminalForm.submit();
            }
            function cancelbtn() {
                // var val= document.editTerminalForm.hiddenmodify.value;
                document.editTerminalForm.buttonValue.value = "cancel";
                var result = confirm("Would you like to save the changes?");
                if (result) {
                    savecontact();
                }
                document.editTerminalForm.submit();
            }
            function cancelview() {
                document.editTerminalForm.buttonValue.value = "cancelview";
                document.editTerminalForm.submit();
            }
            function toUppercase(obj) {
                obj.value = obj.value.toUpperCase();
            }
            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                } else {
                    limitCount.value = limitNum - limitField.value.length;
                }
            }
            function searchcity() {
                document.editTerminalForm.buttonValue.value = "searchcity";
                document.editTerminalForm.submit();
            }
            /*	function phonevalid(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.phoneNo1.value.length > 10) || IsNumeric(document.editTerminalForm.phoneNo1.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.phoneNo1.value = "";
             document.editTerminalForm.phoneNo1.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function phonevalid1(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.phoneNo2.value.length > 10) || IsNumeric(document.editTerminalForm.phoneNo2.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.phoneNo2.value = "";
             document.editTerminalForm.phoneNo2.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function phonevalid2(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.phoneNo3.value.length > 10) || IsNumeric(document.editTerminalForm.phoneNo3.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.phoneNo3.value = "";
             document.editTerminalForm.phoneNo3.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function faxvalid(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.faxNo1.value.length > 10) || IsNumeric(document.editTerminalForm.faxNo1.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.faxNo1.value = "";
             document.editTerminalForm.faxNo1.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function faxvalid1(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.faxNo2.value.length > 10) || IsNumeric(document.editTerminalForm.faxNo2.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.faxNo2.value = "";
             document.editTerminalForm.faxNo2.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function faxvalid2(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.faxNo3.value.length > 10) || IsNumeric(document.editTerminalForm.faxNo3.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.faxNo3.value = "";
             document.editTerminalForm.faxNo3.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function faxvalid3(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.faxNo4.value.length > 10) || IsNumeric(document.editTerminalForm.faxNo4.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.faxNo4.value = "";
             document.editTerminalForm.faxNo4.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }
             function faxvalid4(obj) {
             if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
             if ((document.editTerminalForm.faxNo5.value.length > 10) || IsNumeric(document.editTerminalForm.faxNo5.value.replace(/ /g, "")) == false) {
             alert("please enter the only 10 digits and numerics only");
             document.editTerminalForm.faxNo5.value = "";
             document.editTerminalForm.faxNo5.focus();
             }//document.addWarehouse.phone.value.length=6;
             }
             //                        else {
             //				getIt(obj);
             //			}
             }*/
            function zipcode1(obj) {
                if (document.editTerminalForm.country.value == "" && document.editTerminalForm.country.value != "UNITED STATES") {
                    if ((document.editTerminalForm.zip.value.length > 5) || IsNumeric(document.editTerminalForm.zip.value.replace(/ /g, "")) == false) {
                        alert("please enter the only 5 digits and numerics only");
                        document.editTerminalForm.zip.value = "";
                        document.editTerminalForm.zip.focus();
                    }//document.addWarehouse.phone.value.length=6;
                } else {
                    getzip(obj);
                }
            }
            function getPortName(ev)
            {
                if (event.keyCode == 9 || event.keyCode == 13) {
                    var params = new Array();
                    params['requestFor'] = "ScheduleCode";
                    params['scheduleCode'] = document.editTerminalForm.govSchCode.value;
                    var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function (type, data, evt) {
                            alert("error");
                        },
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populatePortName");
                }
            }
            function populatePortName(type, data, evt) {
                if (data) {
                    document.getElementById("terminalLocation").value = data.portname;
                    if (data.scheduleSuffix) {
                        document.getElementById("scheduleSuffix").value = data.scheduleSuffix;
                    } else {
                        document.getElementById("scheduleSuffix").value = "";
                    }

                }
            }
            function getCountry(ev) {
                if (event.keyCode == 9 || event.keyCode == 13) {

                    var params = new Array();
                    params['requestFor'] = "country";
                    params['city'] = document.editTerminalForm.city.value;

                    var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function (type, data, evt) {
                            alert("error");
                        },
                        mimetype: "text/json",
                        content: params
                    };

                    var req = dojo.io.bind(bindArgs);

                    dojo.event.connect(req, "load", this, "populateCountryAndState");

                }
            }

            function populateCountryAndState(type, data, evt) {
                if (data) {
                    document.getElementById("country").value = data.country;
                    if (data.state) {
                        document.getElementById("state").value = data.state;
                    } else {
                        document.getElementById("state").value = "";
                    }
                }
            }
            function fillCountryAndState() {
                var array = new Array();
                var city = document.editTerminalForm.city.value;
                array = city.split('/');
                document.editTerminalForm.city.value = array[0];
                document.editTerminalForm.country.value = array[1];
                if (undefined != array[2] && null != array[2]) {
                    document.editTerminalForm.unLocCode.value = array[2];
                } else {
                    document.editTerminalForm.state.value = array[2];
                }
                if (undefined != array[3] && null != array[3]) {
                    document.editTerminalForm.state.value = array[3];
                }

            }
            function check() {
                var first = new Array();
                var second;
                var third = new Array();
                var acctno = document.editTerminalForm.acctno.value;
                first = acctno.split('>');
                second = first[1];
                third = second.split(',');
                document.editTerminalForm.acctno.value = third[0];
            }
        </script>
    </head>
    <body class="whitebackgrnd" onkeydown="preventBack()">
        <html:form action="/editTerminal" name="editTerminalForm"	type="com.gp.cong.logisoft.struts.form.EditTerminalForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Terminal Details</td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" id="managers" value="TerminalManagers" onclick="addTerminalManagers('<%=terminal.getTrmnum()%>')">
                        <input type="button" class="buttonStyleNew" id="save" value="Save" onclick="savecontact()"/>
                        <input type="button" class="buttonStyleNew" id="terminalcontact" value="Terminal Contact" style="width:90px" onclick="terminalcontacts()"/>
                        <%
                            if (modify.equals("0") || modify.equals("3")) {
                        %>
                        <input type="button" class="buttonStyleNew" id="cancel" value="Go Back" onclick="cancelview()"/>
                        <%                                                    } else {
                        %>
                        <input type="button" class="buttonStyleNew" id="cancel" value="Go Back" onclick="cancelbtn()"/>
                        <%                                        }
                        %>
                        <input type="button" class="buttonStyleNew" id="delete" value="Delete" onclick="confirmdelete()"/>
                        <input type="button" class="buttonStyleNew" id="note" value="Note" onclick="confirmnote()" disabled="true"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellpadding="3" cellspacing="0">
                            <tr class="textlabels">
                                <td class="textlabels">Terminal No<br></td>
                                <td><html:text property="termNo" value="<%=terminal.getTrmnum()%>" styleClass="varysizeareahighlightgrey" readonly="true" style="width:118px"
                                           onkeyup="toUppercase(this)" maxlength="5"/></td>
                                <td class="textlabels">Account Number<br></td>
                                <td><input name="acctno" id="acctno" value="<%=acctno%>" style="width:100px"/>
                                    <input type="hidden" id="acctno_check" value="<%=acctno%>"/>
                                    <div id="acctno_choices"  style="display: none;width:200px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("acctno", "acctno_choices", "", "",
                                                "<%=path%>/actions/getTradingPartnerAccountNo.jsp?tabName=TERMINAL&from=0", "check()", "");
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <%      String govsecCode = "";
                                    if (terminal.getGovSchCode() != null) {
                                        govsecCode = terminal.getGovSchCode();
                                    }
                                %>
                                <td class="textlabels">Gov Sch code</td>
                                <td><input name="govSchCode" id="govSchCode" value="<%=govsecCode%>"  style="width:118px" maxlength="5"/>
                                    <%--		<dojo:autoComplete formId="editTerminalForm" textboxId="govSchCode" action="<%=path%>/actions/getPorts.jsp?tabName=EDIT_TERMINAL" />--%>
                                </td>
                                <td class="textlabels">Terminal Location </td>
                                <td width="100px">
                                    <table>
                                        <tr>
                                            <td>
                                                <html:text property="terminalLocation"  value="<%=terminal.getTerminalLocation()%>"  maxlength="50" style="width:118px"/>
                                            </td>
                                            <td class="textlabels">
                                                UNLOC
                                            </td>
                                            <td><input name="unLocationCode1" id="unLocationCode1" value="<%=unLocationCode1%>"  style="width:50px"/>
                                                <input type="hidden" id="unLocationCode1_check" value="<%=unLocationCode1%>"/>
                                                <div id="unLocationCode1_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("unLocationCode1", "unLocationCode1_choices", "", "unLocationCode1_check",
                                                            "<%=path%>/actions/getPorts.jsp?tabName=EDIT_TERMINAL&from=0&isDojo=false", "");
                                                </script>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top" class="textlabels">Terminal Type</td>
                                <td><html:select property="terminalType" styleClass="selectboxstyle" value="<%=terminalType%>" style="width:118px">
                                        <html:optionsCollection name="terminaltypelist"/></html:select></td>
                                </tr>
                                <tr>
                                    <td class="textlabels">Schedule Suffix </td>
                                    <td> <html:text property="scheduleSuffix" value="<%=terminal.getScheduleSuffix()%>" readonly="true" styleClass="varysizeareahighlightgrey" style="width:118px"/></td>
                                <td class="textlabels" class="textlabels">Phone Number</td>
                                <td><html:text property="phoneNo1" value="<%=phone1%>"  maxlength="20" styleClass="areahighlightwhite" size="8"/>
                                    <html:text property="extension1" value="<%=terminal.getExtension1()%>" maxlength="4" size="2"/></td>
                                <td class="textlabels">Care Of </td>
                                <td ><html:text property="careof" value="<%=terminal.getCareof()%>"  onkeyup="toUppercase(this)" maxlength="35" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Name</td>
                                <td><html:text property="name" value="<%= terminal.getTrmnam()%>" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)" maxlength="30" style="width:118px"/></td>
                                <td class="textlabels">Address Line1</td>
                                <td><html:text property="addressLine1" value="<%=terminal.getAddres1()%>" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)" maxlength="25" style="width:118px"/></td>
                                <td class="textlabels">Address Line2 </td>
                                <TD><html:text property="addressLine2"  styleClass="areahighlightyellow1" value="<%=terminal.getAddres2()%>" onkeyup="toUppercase(this)"
                                           maxlength="25" style="width:118px"/></TD>
                            </tr>
                            <tr>
                                <td class="textlabels">City </td>
                                <td width="100px">
                                    <table>
                                        <tr>
                                            <td>
                                                <input  name="city" id="city" value="<%=cityId%>"  style="width:118px"/>
                                                <%--                                  <dojo:autoComplete formId="editTerminalForm" textboxId="city" action="<%=path%>/actions/getCity.jsp?tabName=EDIT_TERMINAL&from=0"/> --%>
                                                <%--<input type="hidden" id="city_check" value="<%=cityId %>"/>--%>
                                                <div id="city_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("city", "city_choices", "", "",
                                                            "<%=path%>/actions/getCity.jsp?tabName=EDIT_TERMINAL&from=0", "fillCountryAndState()", "");
                                                </script>
                                            </td>

                                            <td class="textlabels">
                                                UNLOC
                                            </td>
                                            <td><input name="unLocCode" id="unLocCode" value="<%=unLoc%>"  style="width:50px"/>
                                                <input type="hidden" id="unLocCode_check" value="<%=unLoc%>"/>
                                                <div id="unLocCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("unLocCode", "unLocCode_choices", "", "unLocCode_check",
                                                            "<%=path%>/actions/getPorts.jsp?tabName=EDIT_TERMINAL&from=0&isDojo=false", "");
                                                </script>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td align="left" class="textlabels">Country</td>
                                <td><html:text property="country" value="<%=countryId%>" styleId="country" styleClass="varysizeareahighlightgrey" readonly="true" style="width:118px"></html:text></td>
                                    <td class="textlabels">Zip</td>
                                    <td><html:text property="zip" value="<%=terminal.getZipcde()%>"  maxlength="10" onkeyup="zipcode1(this)" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">State</td>
                                <td><html:text property="state" value="<%=state%>" styleId="state" styleClass="varysizeareahighlightgrey" readonly="true" style="width:118px"/></td>
                                <td class="textlabels">ImportsCont Email </td>
                                <td><html:text property="importsContacts"  value="<%=terminal.getImportsContactEmail()%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">General Ledger#</td>
                                <td><html:text property="generalLedger"  value="<%=terminal.getLedgerNo()%>" onkeypress="return checkIts(event)"
                                           maxlength="2" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Import Doc</td>
                                <td><html:text property="phoneNo2" value="<%=phone2%>"  maxlength="20" styleClass="areahighlightwhite" size="8"/>
                                    <html:text property="extension2" value="<%=terminal.getExtension2()%>" maxlength="4" size="2"/>
                                <td class="textlabels">Fax Number</td>
                                <td> <html:text property="faxNo1" value="<%=fax1%>" maxlength="20" style="width:118px"/></td>
                                <td class="textlabels">Import Doc</td>
                                <td><html:text property="faxNo4" value="<%=fax4%>"  maxlength="20" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><html:text property="phoneNo3" value="<%=phone3%>"  maxlength="20" styleClass="areahighlightwhite" size="8"/>
                                    <html:text property="extension3" value="<%=terminal.getExtension3()%>" maxlength="4" size="2"/></td>
                                <td>&nbsp;</td>
                                <td><html:text property="faxNo2" value="<%=fax2%>"  maxlength="20" style="width:118px"/> </td>
                                <td>&nbsp;</td>
                                <td><html:text property="faxNo5" value="<%=fax5%>"  maxlength="20" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Charge Code</td>
                                <td><input  name="chargeCode" id="chargeCode" value="<%=chargeCode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="editTerminalForm" textboxId="chargeCode" action="<%=path%>/actions/getChargeCode.jsp?from=1&tabName=EDIT_TERMINAL"/></td>--%>
                                    <input type="hidden" id="chargeCode_check" value="<%=chargeCode%>"/>
                                    <div id="chargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("chargeCode", "chargeCode_choices", "", "chargeCode_check",
                                                "<%=path%>/actions/getChargeCode.jsp?from=1&tabName=EDIT_TERMINAL&isDojo=false", "");
                                    </script>
                                </td>
                                <td>&nbsp;</td>
                                <td> <html:text property="faxNo3" value="<%=fax3%>"  maxlength="20" style="width:118px"/></td>
                                <td class="textlabels">Brl Chg Code</td>
                                <td><input  name="brlChargeCode" id="brlChargeCode" value="<%=brlChgCode%>" style="width:118px"/>
                                    <%--            <dojo:autoComplete formId="editTerminalForm" textboxId="brlChargeCode" action="<%=path%>/actions/getChargeCode.jsp?from=2&tabName=EDIT_TERMINAL"/></td>--%>
                                    <input type="hidden" id="brlChargeCode_check" value="<%=brlChgCode%>"/>
                                    <div id="brlChargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("brlChargeCode", "brlChargeCode_choices", "", "brlChargeCode_check",
                                                "<%=path%>/actions/getChargeCode.jsp?from=2&tabName=EDIT_TERMINAL&isDojo=false", "");
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabels">Doc Chg Code</td>
                                <td><input  name="docChargeCode" id="docChargeCode" value="<%=docChgCode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="editTerminalForm" textboxId="docChargeCode" action="<%=path%>/actions/getChargeCode.jsp?from=5&tabName=EDIT_TERMINAL"/></td>--%>
                                    <input type="hidden" id="docChargeCode_check" value="<%=docChgCode%>"/>
                                    <div id="docChargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("docChargeCode", "docChargeCode_choices", "", "docChargeCode_check",
                                                "<%=path%>/actions/getChargeCode.jsp?from=5&tabName=EDIT_TERMINAL&isDojo=false", "");
                                    </script>
                                </td>
                                <td class="textlabels">Ovr 10K Chg Code</td>
                                <td><input  name="ovr10kChgCode" id="ovr10kChgCode" value="<%=over10kchgcode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="editTerminalForm" textboxId="ovr10kChgCode" action="<%=path%>/actions/getChargeCode.jsp?from=3&tabName=EDIT_TERMINAL"/></td> --%>
                                    <input type="hidden" id="ovr10kChgCode_check" value="<%=over10kchgcode%>"/>
                                    <div id="ovr10kChgCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("ovr10kChgCode", "ovr10kChgCode_choices", "", "ovr10kChgCode_check",
                                                "<%=path%>/actions/getChargeCode.jsp?from=3&tabName=EDIT_TERMINAL&isDojo=false", "");
                                    </script>
                                </td>
                                <td class="textlabels">Ovr 20K Chg Code</td>
                                <td><input  name="ovr20kChgCode" id="ovr20kChgCode" value="<%=over20kchgcode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="editTerminalForm" textboxId="ovr20kChgCode" action="<%=path%>/actions/getChargeCode.jsp?from=4&tabName=EDIT_TERMINAL"/></td> --%>
                                    <input type="hidden" id="ovr20kChgCode_check" value="<%=over20kchgcode%>"/>
                                    <div id="ovr20kChgCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("ovr20kChgCode", "ovr20kChgCode_choices", "", "ovr20kChgCode_check",
                                                "<%=path%>/actions/getChargeCode.jsp?from=4&tabName=EDIT_TERMINAL&isDojo=false", "");
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabels">Printer Model </td>
                                <td><html:text property="printerModel" value="<%=terminal.getPrintermodel()%>" onkeyup="toUppercase(this)" maxlength="20" style="width:118px"/></td>
                                <td class="textlabels"> Notes</td>
                                <td><html:textarea property="notes" styleClass="textareastyle" cols="25" value="<%=terminal.getNotes()%>" onkeyup="limitText(this.form.notes,this.form.countdown,140)" style="width:118px"></html:textarea><br></td>
                                <td colspan="2" class="textlabels">Active/CTC/FTF <html:select property="acf" styleClass="smallesdropdownStyle" value="<%=terminal.getActyon()%>" style="width:60px">
                                        <html:optionsCollection name="acflist" /> </html:select>
                                    <html:checkbox property="airsrvc"  name="termForm" onclick="chkall()" />Air SVC</td>
                            </tr>
                            <tr>
                                <td class="textlabels">FCL Exports Issuing Terminal</td>
                                <td class="textlabels">
                                    <input type="radio" name="fclExportIssuingTerminal" value="Y" Id="fclExportIssuingTerminalY" />Y
                                    <input type="radio" name="fclExportIssuingTerminal" value="N" Id="fclExportIssuingTerminalN"/>N
                                </td>
                                <td class="textlabels">Imports Door Delivery Email</td>
                                <td><html:text property="importsDoorDeliveryEmail" styleId="importsDoorDeliveryEmail" value="<%=importsDoorDeliveryEmail%>" maxlength="100" style="width:118px"/></td>
                                <td width="17%" class="textlabels">Exports Billing Terminal Email</td>
                                <td><html:text property="exportsBillingTerminalEmail" styleId="exportsBillingTerminalEmail" value="<%=exportsBillingTerminalEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">FCL Document Dept Name</td>
                                <td><html:text property="docDeptName" styleId="docDeptName" value="<%=docDeptName%>"  onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">FCL Document Dept Email</td>
                                <td><html:text property="docDeptEmail" styleId="docDeptEmail" value="<%=docDeptEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">FCL Export Master Shipper</td>
                                <td><html:text property="zaccount" styleId="zaccount" value="<%=zaccount%>" style="width:118px"/>
                                </td>
                            <div id="zaccount_choices"  style="display: none;width:200px;"  align="right"  class="newAutocomplete"></div>
                            <script type="text/javascript">
                                initAutocompleteWithFormClear("zaccount", "zaccount_choices", "", "",
                                        "${path}/actions/tradingPartner.jsp?tabName=TERMINAL&from=0&acctTyp=Z", "", "");
                            </script>
                            <tr>
                                <td class="textlabels">FCL Customer Service Dept Name</td>
                                <td><html:text property="customerServiceName" styleId="customerServiceName" value="<%=customerServiceName%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">FCL Customer Service Dept Email</td>
                                <td><html:text property="customerServiceEmail" styleId="customerServiceEmail" value="<%=customerServiceEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>   
                                <td class="textlabels">INTTRA Booker ID</td>
                                <td><html:text property="intraBookerId" styleId="intraBookerId" value="<%=intraBookerId%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>   
                            </tr>
                            <tr>
                                <td class="textlabels">LCL Document Dept Name</td>
                                <td><html:text property="lclDocDeptName" styleId="lclDocDeptName" value="<%=lclDocDeptName%>"  onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">LCL Document Dept Email</td>
                                <td><html:text property="lclDocDeptEmail" styleId="lclDocDeptEmail" value="<%=lclDocDeptEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">LCL Customer Service Dept Name</td>
                                <td><html:text property="lclCustomerServiceName" styleId="lclCustomerServiceName" value="<%=lclCustomerServiceName%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">LCL Customer Service Dept Email</td>
                                <td><html:text property="lclCustomerServiceEmail" styleId="lclCustomerServiceEmail" value="<%=lclCustomerServiceEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>   
                            </tr>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue" />
<input type="hidden" name="hiddenmodify" value="<%=modify%>" />
<input type="hidden" name="fclExportIssuingTerminal" id="radioId" value="<%=fclExportIssuingTerminal%>">
<script>disabled('<%=modify%>', '<%=msg%>')</script>
</html:form>
</body>

</html:html>




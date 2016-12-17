<%-- 
    Document   : multiQuote
    Created on : Jan 31, 2016, 11:01:43 PM
    Author     : NambuRajasekar
--%>

<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp"  %>
<c:set var="path" value="${pageContext.request.contextPath}"/>


<!DOCTYPE html>
<html>
    <head>
        <%@include file="../includes/resources.jsp" %>
        <title>Quotation</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script language="javascript" src="${path}/js/multiQuote.js"></script>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/rates.js"></script>
        <script type="text/javascript">
            start = function () {
                serializeForm();
                getCreditStatus();
                quoteCompleteOnLoad();
                if (document.getElementById('newClient').checked) {
                    document.getElementById('newerClient').style.display = "block";
                    document.getElementById('existingClient').style.display = "none";
                    onCustomerBlur("newClient");
                }
            }
            window.onload = start;
        </script>
    </head>

    <body class="whitebackgrnd"  topmargin="0">
        <%@include file="../preloader.jsp"%>
        <div id="commentDiv"   class="comments">
            <table border="1" id="commentTableInfo">
                <tbody border="0"></tbody>
            </table>
        </div>
        <div id="cover" style=" width: 100%  ;height: 1000px;"></div>

        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left:45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>

        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes" class="buttonStyleForAlert" value="OK"
                       onclick="yes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="Cancel"
                       onclick="No()">
            </form>
        </div>
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()">
            </form>
        </div>
        <div id="AlertBoxDefaultValues" class="alert">
            <p class="alertHeader"><b>Important Notes</b></p>
            <p id="innerText7" class="containerForAlert"></p>
            <div style="text-align:right;padding-right:4px;padding-bottom:4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="onclickAlertOk()">
            </div>
        </div>

        <div id="ConfirmYesNoCancelDiv" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="confirmMessagePara" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmOptionYes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="No"
                       onclick="confirmOptionNo()">
                <input type="button" id="confirmCancel"  class="buttonStyleForAlert" value="Cancel"
                       onclick="confirmOptionCancel()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->
        <html:form action ="/multiQuote"  name="multiQuotesForm" styleId="multiQuotesForm" type="com.gp.cvst.logisoft.struts.form.multiQuotesForm" scope="request">&nbsp;
            <%@include file="../fclQuotes/fragment/clientSearchOptions.jsp"%>
            <%@include file="../fclQuotes/fragment/multiQuoteRatesDiv.jsp"%>

            <table width="100%" border="0" cellpadding="2" cellspacing="0" >
                <%--<c:if test="${quotationForm.chargeFlag eq true}">--%>
                <tr class="textlabelsBold">
                    <td align="right" style="padding-right: 15px;font-size: 13;">MultiQuote Complete
                        <input type="radio" name="quoteComplete" id="quoteCompleteY" value="Y" onclick="quoteCompleteM();"/>Y
                        <input type="radio" name="quoteComplete" id="quoteCompleteN" value="N" checked />N    
                    </td>
                </tr>
                <%--</c:if>--%>
                <tr>
                    <td class="textlabelsBold">
                        <table   border="0">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td id="scroll">File No :<span class="fileNo"> ${quotationNo}</span></td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class="tableBorderNew">
                                        <tr class="textlabelsBold">
                                            <td>Quote By :<b class="headerlabel" style="color:blue;">
                                                    <c:out value="${quoteBy}"></c:out></b></td>
                                                    <%--<fmt:parseDate value="${quoteDate}" pattern="yyyy-MM-dd HH:mm" var="myDate"/>
                                                  <fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="quoteDate1" value="${quoteDate}"/>--%>
                                            <td style="padding-left:5px;">On :
                                                <b class="headerlabel" style="color:blue;"><c:out value="${quoteDate}" ></c:out></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td id="creditStatusCol" style="display:none">
                                        <table class="tableBorderNew">
                                            <tr class="textlabelsBold">
                                                <td>
                                                    <div id="creditStatus" class="red bold"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                            </table>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            <input type="button" id="cancel" value="Go Back" onclick="goBackMain()" class="buttonStyleNew" />
                            <input type="button" id="save" value="Save" onclick="saveMethod()"   class="buttonStyleNew"/>
                            <input type="button" id="Options" value="Add" onclick="addRatesDiv('${importFlag}')" class="buttonStyleNew" />

                        <c:if test="${quotationForm.convertButtonFlag eq true}">
                            <input type="button" id="convertQuote" value="ConvertToQuote" onclick="ConvertToQuote()" class="buttonStyleNew" />
                            <input type="button" id="printFax" value="Print/Fax/Email" onclick="printFaxEmail()" class="buttonStyleNew" />
                        </c:if>

                        <c:if test="${importFlag}">
                            <input  value="IMPORT" class="BackgrndColorForTextBox" size="4" readonly="true" tabindex="-1" />
                            <input type="text" name="fileType" value="I" id="fileType"/>
                        </c:if>
                    </td>
                    <%-- brand tab --%>

                </tr>
            </table>
            <table  border="0"  width="100%" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                <tr><td>
                        <table width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td colspan="8">Client</td></tr>
                            <tr>
                                <td valign="top">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-top: 4px;">Client&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-top: 4px;">Contact Name&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <%--onchange="tabMoveClient('${importFlag}');"--%>
                                    <table border="0">
                                        <tr class="textlabelsBold">
                                            <td id="existingClient" align="left">
                                                <input type="text" name="customerName" id="customerName"  size="22"  
                                                       value="${quotationForm.customerName}"  maxlength="50" style="text-transform: uppercase"

                                                       class=" textlabelsBoldForTextBox mandatory" onfocus="checkClientConsignee()"/>
                                                <input id="custname_check" type="hidden" value="${quotationForm.customerName}"/>
                                                <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                            </td>
                                            <td id="newerClient" align="left">
                                                <input type="text" name="customerName1" id="customerName1"  size="22"
                                                       value="${quotationForm.customerName1}" Class="textlabelsBoldForTextBox mandatory"
                                                       onkeydown="focusSetting(false);" onchange="getEmptyClient()"
                                                       style="text-transform: uppercase"  maxlength="50" />
                                                <input name="dup" type="hidden" Class="textlabelsBoldForTextBox"/>
                                            </td>
                                            <td>
                                                <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit"
                                                     onmouseover="tooltip.show('<strong>Click here to edit Client Search options</strong>', null, event);" onmouseout="tooltip.hide();" onclick="showClientSearchOption()"/>
                                                <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Client Notes"
                                                     id="clientIcon" onclick="openBlueScreenNotesInfo(jQuery('#clientNumber').val(), jQuery('#customerName').val());"/>
                                                <html:checkbox property="clientConsigneeCheck" styleId="clientConsigneeCheck"  name="quotationForm"
                                                               onmouseover="tooltip.show('<strong>Checked=Consignee Listed <p> UnChecked=Consignee Not Listed</strong>',null,event);" onmouseout="tooltip.hide();"
                                                               onclick="clearNewClient()"></html:checkbox>
                                                <html:checkbox property="newClient" styleId="newClient" onclick="newClientEQ();" name="quotationForm" />New
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <html:text property="contactName" styleId="contactName" style="text-transform: uppercase"
                                                           value="${quotationForm.contactName}" size="22" maxlength="200" styleClass="textlabelsBoldForTextBox"/>
                                                <div id="contactName_choices" style="display: none" class="autocomplete"></div>
                                                <img src="${path}/img/icons/display.gif" alt="Look Up" align="middle" id="contactButton" onclick="getContactInfo()"/>
                                            </td>
                                        </tr>
                                    </table></td>
                                <td width="8%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right" style="padding-bottom: 4px;">Acct No&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Email&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="16%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td class="textBoxBorder">
                                                <input type="text" name="clientNumber" id="clientNumber" value="${quotationForm.clientNumber}" tabindex="-1"
                                                       maxlength="15" readonly="true" class="textlabelsBoldForTextBoxDisabledLook" size="22" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <html:text property="email" styleId="email" value="${quotationForm.email}" maxlength="500" onblur="emailValidate(this)" size="22" styleClass="textlabelsBoldForTextBox" tabindex="-1"/>
                                                <div id="email_choices" style="display: none" class="autocomplete"></div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="10.5%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right">Phone&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td align="right">Fax&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="12%">
                                    <table cellpadding="3"width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td class="textlabelsBold">
                                                <html:text property="phone" maxlength="30" styleId="phone" onblur="checkForNumberAndDecimal(this)" value="${quotationForm.phone}"  styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
                                                <input type="checkbox"  style="visibility: hidden;" />
                                                <font class="textlabelsBold" style="visibility: hidden;">New</font>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <html:text property="fax" maxlength="30" styleId="fax" onblur="checkForNumberAndDecimal(this)" value="${quotationForm.fax}" styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="12%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="right">Client Type:&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="18%">
                                    <table width="100%" border="0">
                                        <tr class="textlabelsBold">
                                            <td align="center">
                                                <html:text property="clienttype" styleId="clienttype" styleClass="whitebackgrnd" value="${quotationForm.clienttype}"
                                                           style="color:black;font:10;width:100%;font-weight:bold;border:0px;" readonly="true" tabindex="-1"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <c:if test="${quotationForm.chargeFlag eq true}">
                                <div id="ChargeList">
                                    <c:import url="/jsps/fclQuotes/multiQuoteCharges.jsp">
                                    </c:import>
                                </div>
                            </c:if>
                        </table>
                    </td></tr>

                <html:hidden property="clientNumber" styleId="clientNumberHidden" value="${quotationForm.clientNumber}"/>
                <input type="hidden" id="importFlag" value="${importFlag}"/>
                <html:hidden property="ssLine" styleId="ssLine" />
                <html:hidden property="selectionInsert" styleId="selectionInsert"/>
                <html:hidden property="unitTypes" styleId="unitTypes" />
                <html:hidden property="focusValue" />
                <html:hidden property="quoteBy" styleId="quoteBy" value="${quoteBy}"/>
                <html:hidden property="quoteId" styleId="quoteId" value="${quotationForm.quoteId}"/>
                <html:hidden property="fileNo" styleId="fileNo" value="${quotationNo}"/>
                <html:hidden property="quoteDate" styleId="quoteDate"  value="${quoteDate}"/>
                <html:hidden property="convertButtonFlag" styleId="convertButtonFlag" value="${quotationForm.convertButtonFlag}" />
                <html:hidden property="methodName" styleId="methodName"/>
             <script type="text/javascript">
                    load();
                    makeClientTypeBorderLess();
                    setFocus('${focusValue}', '${buttonValue}');
                    //    tabMoveAfterDeleteRates('${importFlag}');
                </script>
            </html:form>
   </body>
   <style>
        #originAndDestinationDiv {
            position: fixed;
            _position: absolute;
            z-index: 99;
            border-style:solid;
            border-width:2px;
            border-color:#808080;
            padding:0px 0px 0px 0px;
            background-color: #FFFFFF;
            left:10px;
            right:5px;
            top:0;
            margin:0 auto;
        }

    </style>

    <script type="text/javascript" src="${path}/js/fcl/clientSearch.js"></script>
</html>
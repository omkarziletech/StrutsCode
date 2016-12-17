<%@page import="com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ page language="java"  import="com.gp.cong.logisoft.hibernate.dao.CorporateAcctTypeDAO"%>
<html>
    <%
                DBUtil db = new DBUtil();
                CorporateAcctTypeDAO corporateAcct = new CorporateAcctTypeDAO();
                request.setAttribute("typeOfMoveList", db.getGenericFCLforTypeOfMove(new Integer(48), "yes", "yes"));
                request.setAttribute("corporateAcctTypeList", corporateAcct.getCorporateAcctType());
                String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
                request.setAttribute("companyCode", companyCode);
    %>
    <head>
        <%@include file="../includes/jspVariables.jsp"%>
        <%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/js/isValidEmail.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/fcl/fclDojo.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <%@include file="../preloader.jsp"%>
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
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
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
        <html:form action="/tradingPartner" name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" styleId="generalInfo" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <div id="GeneralInfoContainer" style="width:99%;padding-left:5px;padding-top:10px;">
                            <ul class="htabs">
                                <li id="li1"><a href="javascript: void(0)" tabindex="1">Licences/Identification</a></li>
                                <li id="li2"><a href="javascript: void(0)" tabindex="2">Sales</a></li>
                                <li id="li3"><a href="javascript: void(0)" tabindex="3">Operations</a></li>
                                <li id="li4"><a href="javascript: void(0)" tabindex="4">Website Ship/FF#</a></li>
                                <li id="li5"><a href="javascript: void(0)" tabindex="5">Website Cnee</a></li>
                                <li id="li6"><a href="javascript: void(0)" tabindex="6">Misc</a></li>
                                <c:if test="${roleDuty.displayDefaults}">
                                    <li id="li7"><a href="javascript: void(0)" tabindex="7">Default</a></li>
                                </c:if>
                            </ul>
                            <div style="float:left; width: 100%; margin: 5px 0;">
                                <input type="button" class="buttonStyleNew" value="Save"  style="width:60px;" onclick="saveInfo()" />
                                <c:if test="${tradingPartnerForm.index!=null}">
                                    <!--                                    <input  type="button" class="buttonStyleNew" style="width: 60px" value='Notes'
                                                                                onclick="return GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.GENERALINFORMATION}&moduleRefId=${tradingPartnerForm.index}',375,700);"/>-->
                                </c:if>
                            </div>
                            <div class='pane' id="tab1" style="width: 100%;display: none">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="border:0px;">
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.idtype" /></td>
                                        <td class="textlabelsBold">
                                            <html:radio property="idType"  value="E" name="tradingPartnerForm"/><bean:message key="form.customerForm.e" />
                                            <html:radio property="idType" value="S" name="tradingPartnerForm"/><bean:message key="form.customerForm.s" />
                                            <html:radio property="idType" value="N" name="tradingPartnerForm"/>None
                                        </td>
                                        <td class="textlabelsBold">EIN/SSN Number</td>
                                        <td class="textlabelsBold" >
                                            <html:text styleClass="textlabelsBoldForTextBox" property="idtext" onkeyup="toUppercase(this)"   value="${tradingPartnerForm.idtext}" size="22"/>
                                        </td>
                                        <td class="textlabelsBold">DUNS</td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="dunsNo" value="${tradingPartnerForm.dunsNo}" size="22"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.frieghtfmc" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" property="frieghtFmc"  maxlength="25" value="${tradingPartnerForm.frieghtFmc}" size="16"/>
                                        </td>
                                        <td class="textlabelsBold" ><bean:message key="form.customerForm.frieghtchb" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" property="frieghtChb" value="${tradingPartnerForm.frieghtChb}" size="22"/>
                                        </td>
                                        <td class="textlabelsBold">NVO/OTI License#</td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="nvoOtiLicenseNo" value="${tradingPartnerForm.nvoOtiLicenseNo}" size="22"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.poa" /></td>
                                        <td align="left"><html:checkbox property="poa" name="tradingPartnerForm" />
                                        </td>
                                        <td class="textlabelsBold">EDI Portal</td>
                                        <td class="textlabelsBold">
                                            <html:radio property="shippingCode" value="G" name="tradingPartnerForm"></html:radio>&nbsp;&nbsp;GTNEXUS
                                            <html:radio property="shippingCode" value="I" name="tradingPartnerForm"></html:radio>&nbsp;&nbsp;INTRA
                                            <html:radio property="shippingCode" value="N" name="tradingPartnerForm"></html:radio>None
                                        </td>
                                        <td class="textlabelsBold">Firms Code</td>
                                        <td>
                                            <html:text property="firmsCode" styleId="firmsCode" styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" maxlength="4" size="4"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="pane" id="tab2" style="width: 100%;display: none">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="border:0px;">
                                    <tr>
                                        <td style="width: 70%">
                                            <table border="0" cellpadding="0" cellspacing="1">
                                                <tr>
                                                    <td class="textlabelsBold">Ship/FF Coload Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="commodity" id="commodity" class="textlabelsBoldForTextBox" value="${tradingPartnerForm.commodity}" size="5" maxlength="6"/>
                                                        <input type="hidden" name ="commodityCheck" id="commodityCheck" value="${tradingPartnerForm.commodity}"/>
                                                        <div id="commodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("commodity", "commodity_choices", "commDesc", "commodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=commodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="commDesc" id="commDesc"
                                                               value="${tradingPartnerForm.commDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search Ship/FF Coload Commodity#</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getCommodityCodeLookUp(this.value)" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Ship/FF Retail Commodity# </td>
                                                    <td class="textlabelsBold">
                                                        <input name="retailCommodity" id="retailCommodity" class="textlabelsBoldForTextBox" maxlength="6" value="${tradingPartnerForm.retailCommodity}"  size="5" />
                                                        <input type="hidden" name ="retailCommodityCheck" id="retailCommodityCheck" value="${tradingPartnerForm.retailCommodity}"/>
                                                        <div id="retailCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("retailCommodity", "retailCommodity_choices", "retailCommodityDesc", "retailCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=retailCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="retailCommodityDesc" id="retailCommodityDesc"
                                                               value="${tradingPartnerForm.retailCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search Ship/FF Retail Commodity #</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getRetailCommodityCodeLookUp(this.value)" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Ship/FF FCL Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="fclCommodity" id="fclCommodity" class="textlabelsBoldForTextBox" maxlength="6" value="${tradingPartnerForm.fclCommodity}"  size="5" />
                                                        <input type="hidden" name ="fclCommodityCheck" id="fclCommodityCheck" value="${tradingPartnerForm.fclCommodity}"/>
                                                        <div id="fclCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("fclCommodity", "fclCommodity_choices", "fclCommodityDesc", "fclCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=fclCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="fclCommodityDesc" id="fclCommodityDesc"
                                                               value="${tradingPartnerForm.fclCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td style="padding-left: 2px;">
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search Ship/FF FCL Commodity #</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getFclCommodityCodeLookUp()" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Consignee Coload Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="consColoadCommodity" id="consColoadCommodity" class="textlabelsBoldForTextBox"  value="${tradingPartnerForm.consColoadCommodity}" size="5" maxlength="6"/>
                                                        <input type="hidden" name ="consColoadCommodityCheck" id="consColoadCommodityCheck" value="${tradingPartnerForm.consColoadCommodity}"/>
                                                        <div id="consColoadCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("consColoadCommodity", "consColoadCommodity_choices", "consColoadCommodityDesc", "consColoadCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=consColoadCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="consColoadCommodityDesc" id="consColoadCommodityDesc"
                                                               value="${tradingPartnerForm.consColoadCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search Consignee Coload Commodity#</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getConsColoadCommodityCodeLookUp(this.value)" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Consignee Retail Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="consRetailCommodity" id="consRetailCommodity" class="textlabelsBoldForTextBox"  value="${tradingPartnerForm.consRetailCommodity}" size="5" maxlength="6"/>
                                                        <input type="hidden" name ="consRetailCommodityCheck" id="consRetailCommodityCheck" value="${tradingPartnerForm.consRetailCommodity}"/>
                                                        <div id="consRetailCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("consRetailCommodity", "consRetailCommodity_choices", "consRetailCommodityDesc", "consRetailCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=consRetailCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="consRetailCommodityDesc" id="consRetailCommodityDesc"
                                                               value="${tradingPartnerForm.consRetailCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search Consignee Retail Commodity #</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getConsRetailCommodityCodeLookUp(this.value)" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Consignee FCL Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="consFclCommodity" id="consFclCommodity" class="textlabelsBoldForTextBox" maxlength="6" value="${tradingPartnerForm.consFclCommodity}"  size="5" />
                                                        <input type="hidden" name ="consFclCommodityCheck" id="consFclCommodityCheck" value="${tradingPartnerForm.consFclCommodity}"/>
                                                        <div id="consFclCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("consFclCommodity", "consFclCommodity_choices", "consFclCommodityDesc", "consFclCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=consFclCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="consFclCommodityDesc" id="consFclCommodityDesc"
                                                               value="${tradingPartnerForm.consFclCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Search FCL Commodity #</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getConsFclCommodityCodeLookUp()" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">Origin Agent Import Commodity#</td>
                                                    <td class="textlabelsBold">
                                                        <input name="impCommodity" id="impCommodity" class="textlabelsBoldForTextBox" maxlength="6" value="${tradingPartnerForm.impCommodity}"  size="5" />
                                                        <input type="hidden" name ="impCommodityCheck" id="impCommodityCheck" value="${tradingPartnerForm.impCommodity}"/>
                                                        <div id="impCommodity_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            AjaxAutocompleter("impCommodity", "impCommodity_choices", "impCommodityDesc", "impCommodityCheck",
                                                            "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=impCommodity");
                                                        </script>
                                                    </td>
                                                    <td>
                                                        <input name="impCommodityDesc" id="impCommodityDesc"
                                                               value="${tradingPartnerForm.impCommodityDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                    </td>
                                                    <td>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Customer Import Commodity #</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                            <img src="${path}/img/icons/display.gif" onclick="getImportCommodityCodeLookUp(this.value)" />
                                                        </span>
                                                    </td>
                                                </tr>
                                                <c:choose>
                                                    <c:when test="${roleDuty.changeSalesCode && not empty tradingPartnerForm.eciAccountNo}">
                                                        <tr>
                                                            <td class="textlabelsBold">Shpr/FF Sales Person Code</td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBox" name="salesCode" id="salesCode" onkeyup="toUppercase(this)" onkeyup="toUppercase(this)" value="${tradingPartnerForm.salesCode}" size="5"/>
                                                                <input type="hidden" name ="salesCodeCheck" id="salesCodeCheck" value="${tradingPartnerForm.salesCode}"/>
                                                                <div id="salesCode_choices" style="display: none" class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    AjaxAutocompleter("salesCode", "salesCode_choices", "salesCodeName", "salesCodeCheck",
                                                                    "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=salesCode");
                                                                </script>
                                                            </td>
                                                            <td class="textlabelsBold">
                                                                <input name="salesCodeName" id="salesCodeName"
                                                                       value="${tradingPartnerForm.salesCodeName}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                            </td>
                                                            <td>
                                                                <span class="hotspot" onmouseover="tooltip.show('<strong>Search Shpr/FF Sales Person Code</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                                    <img src="${path}/img/icons/display.gif" onclick="getSalesCodeLookUp(this.value)" />
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td class="textlabelsBold">Shpr/FF Sales Person Code</td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBoxDisabledLook" name="salesCode" id="salesCode" onkeyup="toUppercase(this)" onkeydown="getSalesCode(this)" onkeyup="toUppercase(this)" value="${tradingPartnerForm.salesCode}" size="5" readonly/>
                                                            </td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBoxDisabledLook" name="salesCodeName" id="salesCodeName" onkeyup="toUppercase(this)" onkeydown="getSalesCode(this)" value="${tradingPartnerForm.salesCodeName}" size="45" readonly/>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${roleDuty.changeSalesCode && not empty tradingPartnerForm.eciAccountNo2}">
                                                        <tr>
                                                            <td class="textlabelsBold">Consignee Sales Person Code</td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBox" name="consSalesCode" id="consSalesCode" onkeyup="toUppercase(this)" onkeyup="toUppercase(this)" value="${tradingPartnerForm.consSalesCode}" size="5"/>
                                                                <input type="hidden" name ="consSalesCodeCheck" id="consSalesCodeCheck" value="${tradingPartnerForm.consSalesCode}"/>
                                                                <div id="consSalesCode_choices" style="display: none" class="autocomplete"></div>
                                                                <script type="text/javascript">
                                                                    AjaxAutocompleter("consSalesCode", "consSalesCode_choices", "consSalesCodeName", "consSalesCodeCheck",
                                                                    "${path}/actions/getChargeCode.jsp?tabName=GENERAL_INFORMATION&isDojo=false&fieldName=consSalesCode");
                                                                </script>
                                                            </td>
                                                            <td class="textlabelsBold">
                                                                <input name="consSalesCodeDesc" id="consSalesCodeDesc"
                                                                       value="${tradingPartnerForm.consSalesCodeDesc}" readonly="readonly" style="border: 0;" class="BackgrndColorForTextBox" size="45"/>
                                                            </td>
                                                            <td>
                                                                <span class="hotspot" onmouseover="tooltip.show('<strong>Search Consignee Sales Person Code</strong>', null, event);" id="toggle"  onmouseout="tooltip.hide();">
                                                                    <img src="${path}/img/icons/display.gif" onclick="getConsSalesCodeLookUp(this.value)" />
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td class="textlabelsBold">Consignee Sales Person Code</td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBoxDisabledLook" name="consSalesCode" id="consSalesCode" onkeyup="toUppercase(this)" onkeydown="getSalesCode(this)" onkeyup="toUppercase(this)" value="${tradingPartnerForm.consSalesCode}" size="5" readonly/>
                                                            </td>
                                                            <td class="textlabelsBold">
                                                                <input type="text" class="textlabelsBoldForTextBoxDisabledLook" name="consSalesCodeDesc" id="consSalesCodeDesc" onkeyup="toUppercase(this)" onkeydown="getSalesCode(this)" value="${tradingPartnerForm.consSalesCode}" size="45" readonly/>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </table>
                                        </td>
                                        <td style="vertical-align: top">
                                            <table border="0" cellpadding="0" cellspacing="1">
                                                <tr>
                                                    <td class="textlabelsBold">Max Days Between Visits
                                                        <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="maxDay" maxlength="5"  value="${tradingPartnerForm.maxDay}" onkeypress="return checkIts(event)" size="3"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">
                                                        <html:checkbox property="fclMailingList" name="tradingPartnerForm"/><bean:message key="form.customerForm.fclmail" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">
                                                        <html:checkbox  property="goalAcct" name="tradingPartnerForm" /><bean:message key="form.customerForm.goalacct" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">
                                                        <html:checkbox property="christmasCard" name="tradingPartnerForm" /><bean:message key="form.customerForm.christmascard" />
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class="textlabelsBold">
                                                        <html:checkbox property="faxSailingSchedule" name="tradingPartnerForm" /><bean:message key="form.customerForm.faxsail" />
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="pane" id="tab3" style="width: 100%;display: none">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="border:0px;">
                                    <tr class="textlabelsBold">
                                        <td align="right" style="padding-right: 10px;"><html:checkbox property="insure" name="tradingPartnerForm"/> </td>
                                        <td>Insure</td>

                                        <td><bean:message key="form.customerForm.importcfs" /></td>
                                        <td> <html:radio property="importsCfs" value="U" name="tradingPartnerForm" /><bean:message key="form.customerForm.u" />
                                            <html:radio property="importsCfs" value="F" name="tradingPartnerForm" /><bean:message key="form.customerForm.f" />
                                            <html:radio property="importsCfs" value="B" name="tradingPartnerForm" /><bean:message key="form.customerForm.blank" />
                                        </td>

                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right" style="padding-right: 10px;"><html:checkbox property="pbaSurchrge" name="tradingPartnerForm" /></td>
                                        <td>Exempt From PBA surcharge</td>
                                        <td>Allways Bill Coload</td>
                                        <td>  <html:radio property="allwaysBillCoload" value="Y" name="tradingPartnerForm" />Yes
                                            <html:radio property="allwaysBillCoload" value="N" name="tradingPartnerForm" />No</td>
                                    </tr>
                                    <tr class="textlabelsBold"><td align="right" style="padding-right: 10px;"><html:checkbox property="CFCL" name="tradingPartnerForm"/> </td>
                                        <td>CFCL Account</td></tr>

                                    <tr class="textlabelsBold">
                                        <td>CFCL Port Code</td>
                                        <td><html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="cfclPortCode"  maxlength="5" value="${tradingPartnerForm.cfclPortCode}" size="19"/></td>
                                        <td >Hot Codes</td>
                                        <td align="left"><table><tr align="left"> <td>
                                                        <input  type="text" name="hotCodes" id="hotCodes" size="7" class="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" value="${tradingPartnerForm.hotCodes}"/>
                                                <dojo:autoComplete formId="tradingPartnerForm" textboxId="hotCodes"
                                                                   action="${path}/actions/getChargeCodeDesc.jsp?tabName=OPERATION&from=1"/></td>
                                        <td><input  type="text" name="hotCodes1" size="7"  id="hotCodes1" class="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" value="${tradingPartnerForm.hotCodes1}"/>
                                    <dojo:autoComplete formId="tradingPartnerForm" textboxId="hotCodes1"
                                                       action="${path}/actions/getChargeCodeDesc.jsp?tabName=OPERATION&from=2"/></td>
                                    </tr></table></td>

                                </tr>
                                <tr class="textlabelsBold">
                                    <td valign="top">Default Routing Instructions</td>
                                    <td><html:textarea styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" property="defaultRoute"  rows="6" cols="45" value="${tradingPartnerForm.defaultRoute}"
                                                   onkeydown="maxLength(this,200)"/></td>
                                    <td valign="top">Insurance Comments</td>
                                    <td ><html:textarea styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="insuranceComment" onkeydown="maxLength(this,200)" value="${tradingPartnerForm.insuranceComment}"
                                                   rows="6" cols="45"></html:textarea></td>

                                </tr>
                                <tr class="textlabelsBold">
                                    <td valign="top">Known Shipper ID Air</td>
                                    <td><input type="text" class="textlabelsBoldForTextBox" name="knownShipIdAir" maxlength="80" onkeyup="toUppercase(this)" value="${tradingPartnerForm.knownShipIdAir}"></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                </table>
                            </div>

                            <div class="pane" id="tab4" style="width: 100%;display: none">
                                <table  border="0" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="border:0px;">
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.username" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)"  property="userName" styleId="userName" value="${tradingPartnerForm.userName}" maxlength="50"/>
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="allowLclQuotes" name="tradingPartnerForm" />
                                            <bean:message key="form.customerForm.weblcl" /></td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="importTrackingScreen" name="tradingPartnerForm"/>&nbsp;Imports Tracking Screen</td>

                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.password" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox"
                                                       property="password" value="${tradingPartnerForm.password}"
                                                       onkeyup="toUppercase(this)" maxlength="50" />
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="allowFclQuotes" name="tradingPartnerForm" />&nbsp;FCL Web Quotes</td>
                                        <td class="textlabelsBold">
                                            
                                            <html:checkbox property="activatePwdQuotes" styleId="activatePwdQuotes" name="tradingPartnerForm"/>
                                            Allow Webtools Login
                                            <c:if test="${(tradingPartnerForm.disabled == 'Y')}">
                                             <c:if test="${(tradingPartnerForm.activatePwdQuotes == 'on')}">
                                                 <script type="text/javascript">
                                        document.getElementById("activatePwdQuotes").checked = false;
                                        </script>
                                        </c:if>
                                            </c:if>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.lastdate" />
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="BackgrndColorForTextBox" onkeyup="toUppercase(this)"
                                                       property="lastDate" value="${tradingPartnerForm.lastDate}" readonly="true" /></td>
                                        <td class="textlabelsBold">
                                            Fcl Web Quote Use Commodity
                                            <html:radio property="fclWebquoteUseCommodity" value="Y" styleId="fclWebquoteUseCommodityY"
                                                        name="tradingPartnerForm"/>Y
                                            <html:radio property="fclWebquoteUseCommodity" value="N" styleId="fclWebquoteUseCommodityN"
                                                        name="tradingPartnerForm"/>N
                                            <html:radio property="fclWebquoteUseCommodity" value="B" styleId="fclWebquoteUseCommodityB"
                                                        name="tradingPartnerForm"/>B
                                        </td>
                                        <td class="textlabelsBold">
                                            Allow Customer Controlled Login
                                            <html:radio property="shipffCustControlLogin" value="Y" styleId="shipffCustControlLoginY"
                                                        name="tradingPartnerForm"/>Y
                                            <html:radio property="shipffCustControlLogin" value="N" styleId="shipffCustControlLoginN"
                                                        name="tradingPartnerForm"/>N
                                            <html:radio property="shipffCustControlLogin" value="A" styleId="shipffCustControlLoginA"
                                                        name="tradingPartnerForm"/>A
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold" style="padding-left: 3px;">
                                        <td>Allow Imports Web DAP/DDP</td>
                                        <td>
                                            <html:radio property="importWebDapDdp" value="Y" styleId="importWebDapDdpY"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="importWebDapDdp" value="N" styleId="importWebDapDdpN"
                                                        name="tradingPartnerForm"/>No
                                        </td>
                                        <td class="textlabelsBold" style="padding-left: 3px;">
                                            LCL Rate Sheet &nbsp;
                                            <html:radio property="lclRateSheet" value="C" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Co-Load</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">C</span>
                                            <html:radio property="lclRateSheet" value="R" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Retail</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">R</span>
                                            <html:radio property="lclRateSheet" value="N" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>No</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">N</span>
                                        </td>
                                        <td class="textlabelsBold">Import Freight Release
                                            <html:radio property="shipffImportFreightRelease" value="N" name="tradingPartnerForm">N</html:radio>
                                            <html:radio property="shipffImportFreightRelease" value="Y" name="tradingPartnerForm">Y</html:radio>
                                            <html:radio property="shipffImportFreightRelease" value="D" name="tradingPartnerForm">D</html:radio>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold" style="padding-left: 3px;">
                                        <td>Sales Agency/Brokerage Agreement</td>
                                        <td>
                                            <html:radio property="shipffSalesAgencyBrokerageAgreement" value="Y" styleId="shipffSalesAgencyBrokerageAgreement"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="shipffSalesAgencyBrokerageAgreement" value="N" styleId="shipffSalesAgencyBrokerageAgreement"
                                                        name="tradingPartnerForm"/>No
                                        </td>
                                        <td>Receive LCL Exports 315 Status Update
                                            <html:radio property="shipffReceiveLclExports315Status" value="Y" styleId="shipffReceiveLclExports315Status"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="shipffReceiveLclExports315Status" value="N" styleId="shipffReceiveLclExports315Status"
                                                        name="tradingPartnerForm"/>No  
                                        </td>
                                        <td>Inttra Account Number For 315 Messages
                                            <html:text styleClass="textlabelsBoldForTextBox" styleId="shipffInttraAccountNumber" onkeyup="toUppercase(this)" property="shipffInttraAccountNumber" 
                                                       value="${tradingPartnerForm.shipffInttraAccountNumber}" maxlength="15"/>
                                        </td>
                                    <tr class="textlabelsBold" style="padding-left: 3px;">
                                        <td>Send LCL Docs To Website</td>
                                        <td>
                                            <html:radio property="shipffSendLclDocsToWebsite" value="Y" styleId="shipffSendLclDocsToWebsite"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="shipffSendLclDocsToWebsite" value="N" styleId="shipffSendLclDocsToWebsite"
                                                        name="tradingPartnerForm"/>No     
                                        </td>
                                        <td>Allow CFCL Web Booking
                                            <html:radio property="shipffAllowCFCLWebBooking" value="Y" styleId="shipffAllowCFCLWebBooking"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="shipffAllowCFCLWebBooking" value="N" styleId="shipffAllowCFCLWebBooking"
                                                        name="tradingPartnerForm"/>No       
                                        </td>
                                    </tr>
                                    </tr>
                                </table>
                            </div>
                            <div class="pane" id="tab5" style="width: 100%;display: none">
                                <table  border="0" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew"  style="border:0px;">
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.username" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" styleId="consUserName" property="consUserName" 
                                                       value="${tradingPartnerForm.consUserName}" maxlength="50"/>
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="consAllowLclWebQuotes" styleId="consAllowLclWebQuotes" name="tradingPartnerForm" />
                                            <bean:message key="form.customerForm.weblcl" /></td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="consImportTrackingScreen" styleId="consImportTrackingScreen" name="tradingPartnerForm"/>&nbsp;Imports Tracking Screen
                                        </td>

                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.password" /></td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="textlabelsBoldForTextBox"
                                                       property="consPassword" styleId="consPassword" value="${tradingPartnerForm.consPassword}"
                                                       onkeyup="toUppercase(this)" maxlength="50" />
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="consAllowFclWebQuotes" styleId="consAllowFclWebQuotes" name="tradingPartnerForm" />&nbsp;FCL Web Quotes</td>
                                        <td class="textlabelsBold">
                                            <html:checkbox property="consActivatePwdQuotes" styleId="consActivatePwdQuotes" name="tradingPartnerForm" />
                                            Allow Webtools Login
                                         <c:if test="${(tradingPartnerForm.disabled == 'Y')}">
                                             <c:if test="${(tradingPartnerForm.consActivatePwdQuotes == 'true')}">
                                                 <script type="text/javascript">
                                        document.getElementById("consActivatePwdQuotes").checked = false;
                                        </script>
                                        </c:if>
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold"><bean:message key="form.customerForm.lastdate" />
                                        </td>
                                        <td class="textlabelsBold">
                                            <html:text styleClass="BackgrndColorForTextBox" onkeyup="toUppercase(this)"
                                                       property="consLastPwdActivatedDate" styleId="consLastPwdActivatedDate" value="${tradingPartnerForm.consLastPwdActivatedDate}" readonly="true" /></td>
                                        <td class="textlabelsBold">
                                            Fcl Web Quote Use Commodity
                                            <html:radio property="consFclWebQuoteUseCommodity" value="Y" styleId="consFclWebQuoteUseCommodityY"
                                                        name="tradingPartnerForm"/>Y
                                            <html:radio property="consFclWebQuoteUseCommodity" value="N" styleId="consFclWebQuoteUseCommodityN"
                                                        name="tradingPartnerForm"/>N
                                            <html:radio property="consFclWebQuoteUseCommodity" value="B" styleId="consFclWebQuoteUseCommodityB"
                                                        name="tradingPartnerForm"/>B
                                        </td>
                                        <td class="textlabelsBold">
                                            Allow Customer Controlled Login
                                            <html:radio property="consCustControlLogin" value="Y" styleId="consCustControlLoginY"
                                                        name="tradingPartnerForm"/>Y
                                            <html:radio property="consCustControlLogin" value="N" styleId="consCustControlLoginN"
                                                        name="tradingPartnerForm"/>N
                                            <html:radio property="consCustControlLogin" value="A" styleId="consCustControlLoginA"
                                                        name="tradingPartnerForm"/>A
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Allow Imports Web DAP/DDP</td>
                                        <td>
                                            <html:radio property="consImportWebDapDdp" value="Y" styleId="consImportWebDapDdpY"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="consImportWebDapDdp" value="N" styleId="consImportWebDapDdpN"
                                                        name="tradingPartnerForm"/>No
                                        </td>
                                        <td class="textlabelsBold">Imports Rating Coload/Retail
                                            <html:radio property="importQuoteColoadRetail" value="N" styleId="No"    name="tradingPartnerForm"/>N
                                            <html:radio property="importQuoteColoadRetail" value="R" styleId="Retail" name="tradingPartnerForm" />R
                                            <html:radio property="importQuoteColoadRetail" value="C" styleId="Coload" name="tradingPartnerForm"/>C
                                        </td>
                                        <td class="textlabelsBold">LCL Rate Sheet
                                            <html:radio property="consLclRateSheet" value="C" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Co-Load</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">C</span>
                                            <html:radio property="consLclRateSheet" value="R" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Retail</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">R</span>
                                            <html:radio property="consLclRateSheet" value="N" name="tradingPartnerForm"></html:radio>
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>No</strong>', null, event)" onmouseout="tooltip.hide()"
                                                  style="color:black;">N</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold">LCL Imports Quoting</td>
                                        <td class="textlabelsBold">
                                            <html:radio property="consLclImportQuoting" styleId="consLclImportQuotingN" value="N" name="tradingPartnerForm" title="No Access" styleClass="valign-bottom"/>
                                            <label for="consLclImportQuotingN" title="No Access" class="valign-bottom">N</label>
                                            <html:radio property="consLclImportQuoting" styleId="consLclImportQuotingB" value="B" name="tradingPartnerForm" title="Both DAP/DDP" styleClass="valign-bottom"/>
                                            <label for="consLclImportQuotingB" title="Both DAP/DDP" class="valign-bottom">B</label>
                                            <html:radio property="consLclImportQuoting" styleId="consLclImportQuotingD" value="D" name="tradingPartnerForm" title="DDP only" styleClass="valign-bottom"/>
                                            <label for="consLclImportQuotingD" title="DDP only" class="valign-bottom">D</label>
                                            <html:radio property="consLclImportQuoting" styleId="consLclImportQuotingP" value="P" name="tradingPartnerForm" title="DAP only" styleClass="valign-bottom"/>
                                            <label for="consLclImportQuotingP" title="DAP only" class="valign-bottom">P</label>
                                        </td>
                                        <td class="textlabelsBold">Import Freight Release
                                            <html:radio property="consImportFreightRelease" value="N" name="tradingPartnerForm">N</html:radio>
                                            <html:radio property="consImportFreightRelease" value="Y" name="tradingPartnerForm">Y</html:radio>
                                            <html:radio property="consImportFreightRelease" value="D" name="tradingPartnerForm">D</html:radio>
                                        </td>
                                        <td class="textlabelsBold">Sales Agency/Brokerage Agreement
                                            <html:radio property="consSalesAgencyBrokerageAgreement" value="Y" styleId="consSalesAgencyBrokerageAgreement"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="consSalesAgencyBrokerageAgreement" value="N" styleId="consSalesAgencyBrokerageAgreement"
                                                        name="tradingPartnerForm"/>No     
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold">Receive LCL Exports 315 Status Update</td>
                                        <td class="textlabelsBold">
                                            <html:radio property="consReceiveLclExports315Status" value="Y" styleId="consReceiveLclExports315Status"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="consReceiveLclExports315Status" value="N" styleId="consReceiveLclExports315Status"
                                                        name="tradingPartnerForm"/>No         
                                        </td>
                                        <td class="textlabelsBold">Inttra Account Number For 315 Messages
                                            <html:text styleClass="textlabelsBoldForTextBox" styleId="consInttraAccountNumber" onkeyup="toUppercase(this)" property="consInttraAccountNumber" 
                                                       value="${tradingPartnerForm.consInttraAccountNumber}" maxlength="15"/>   
                                        </td>
                                        <td class="textlabelsBold">Send LCL Docs To Website
                                            <html:radio property="consSendLclDocsToWebsite" value="Y" styleId="consSendLclDocsToWebsite"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="consSendLclDocsToWebsite" value="N" styleId="consSendLclDocsToWebsite"
                                                        name="tradingPartnerForm"/>No         
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="textlabelsBold">Allow CFCL Web Booking</td>
                                        <td class="textlabelsBold">
                                            <html:radio property="consAllowCFCLWebBooking" value="Y" styleId="consAllowCFCLWebBooking"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="consAllowCFCLWebBooking" value="N" styleId="consAllowCFCLWebBooking"
                                                        name="tradingPartnerForm"/>No         
                                        </td>

                                         <c:choose>
                                         <c:when test="${roleDuty.changeSalesCode}">
                                                     
                                        <td class="textlabelsBold">Apply Customer Commodity Rates When Notify Party
                                            <html:radio property="applyCustomerCommodityRates" value="Y" styleId="applyCustomerCommodityRates"
                                                        name="tradingPartnerForm"/>Yes
                                            <html:radio property="applyCustomerCommodityRates" value="N" styleId="applyCustomerCommodityRates"
                                                        name="tradingPartnerForm"/>No
                                             </td>
                                                </c:when>
                                               <c:otherwise>
                                               
                                            <td class="textlabelsBold">Apply Customer Commodity Rates When Notify Party
                                            <html:radio property="applyCustomerCommodityRates" value="Y" styleId="applyCustomerCommodityRates"
                                                        name="tradingPartnerForm" disabled="true"/>Yes
                                            <html:radio property="applyCustomerCommodityRates" value="N" styleId="applyCustomerCommodityRates"
                                                        name="tradingPartnerForm" disabled="true"/>No
                                             </td>
                                                 
                                                 </c:otherwise>
                                            </c:choose>
                                   </tr>
                                </table>
                            </div>
                            <div class="pane" id="tab6" style="width: 100%;display: none">
                                <table width="100%" border="0" cellpadding="3" cellspacing="2" class="tableBorderNew"  style="border:0px;">
                                    <tr class="textlabelsBold">
                                        <c:if test="${not master}">
                                            <td>Master</td>
                                            <c:choose>
                                                <c:when test="${roleDuty.changeMaster}">
                                                    <td>
                                                        <html:select property="master" styleClass="dropdown_accounting" value="${tradingPartnerForm.master}" onchange = "setMaster();">
                                                            <html:optionsCollection name="mastertypelist"/>
                                                        </html:select>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>
                                                        <html:select property="master" styleClass="textlabelsBoldForTextBoxDisabledLook" value="${tradingPartnerForm.master}" disabled="true">
                                                            <html:optionsCollection name="mastertypelist"/>
                                                        </html:select>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <td>Additional Phone#</td>
                                        <td>
                                            <html:text styleClass="textlabelsBoldForTextBox"  property="phone1" maxlength="13" value="${tradingPartnerForm.phone1}"  size="15"/>
                                        </td>
                                        <td>Additional Fax#</td>
                                        <td>
                                            <html:text styleClass="textlabelsBoldForTextBox" property="fax1" maxlength="13"  value="${tradingPartnerForm.fax1}" size="15"/>
                                        </td>
                                        <td>Merge Note info</td>
                                        <td>
                                            <html:textarea  property="mergeNoteInfo" onkeyup="toUppercase(this)" styleClass="textlabelsBoldForTextBox" onkeydown="TextAreaLimit(this,100)" value="${tradingPartnerForm.mergeNoteInfo}" cols="40" rows="3" />
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <c:choose>
                                            <c:when test="${roleDuty.editEciAcct}">
                                                <td>ECI Shpr/FF#</td>
                                                <td><html:text styleId="eciAccountNo" property="eciAccountNo" value="${tradingPartnerForm.eciAccountNo}" 
                                                           size="5" styleClass="textlabelsBoldForTextBox" onkeypress="return blockSpecialChar(event)" onblur="validateShipper()" onchange="checkShipper()"/></td>
                                                <td>ECI Consignee</td>
                                                <td><html:text styleId="eciAccountNo2" property="eciAccountNo2" value="${tradingPartnerForm.eciAccountNo2}" 
                                                           size="5" styleClass="textlabelsBoldForTextBox" onkeypress="return blockSpecialChar(event)" onblur="validateConsignee()" onchange="checkConsignee()"/></td>
                                                <td>ECI Vendor</td>
                                                <td><html:text  styleId="eciAccountNo3" property="eciAccountNo3" value="${tradingPartnerForm.eciAccountNo3}" 
                                                            size="5" styleClass="textlabelsBoldForTextBox" onkeypress="return blockSpecialChar(event)" onblur="validateVendor()" onchange="checkVendor()"/></td>
                                                </c:when>
                                                <c:otherwise>
                                                <td>ECI Shpr/FF#</td>
                                                <td><html:text styleId="eciAccountNo" property="eciAccountNo" value="${tradingPartnerForm.eciAccountNo}" styleClass="textlabelsBoldForTextBoxDisabledLook" disabled="true"/></td>
                                                <td>ECI Consignee</td>
                                                <td><html:text styleId="eciAccountNo2" property="eciAccountNo2" value="${tradingPartnerForm.eciAccountNo2}" styleClass="textlabelsBoldForTextBoxDisabledLook" disabled="true"/></td>
                                                <td>ECI Vendor</td>
                                                <td><html:text styleId="eciAccountNo3" property="eciAccountNo3" value="${tradingPartnerForm.eciAccountNo3}" styleClass="textlabelsBoldForTextBoxDisabledLook" disabled="true"/></td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td id="ssLineLabel">
                                            SSLine Number
                                        </td>
                                        <c:choose>
                                            <c:when test="${roleDuty.changeTpType}">
                                                <td id="ssLineValue">
                                                    <html:text property="sslineNumber" styleId="sslineNumber" style="text-transform: uppercase;" maxlength="5"  styleClass="textlabelsBoldForTextBox" onkeypress="return blockSpecialChar(event)" value="${tradingPartnerForm.sslineNumber}" size="5"/>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td id="ssLineValue">
                                                    <html:text property="sslineNumber" styleId="sslineNumber"  styleClass="textlabelsBoldForTextBoxDisabledLook" value="${tradingPartnerForm.sslineNumber}" disabled="true"/>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <c:choose>
                                            <c:when test="${roleDuty.changeTpType}">
                                                <td colspan="2">Account Type:&nbsp;
                                                    <html:checkbox property="accountType1" name="tradingPartnerForm" styleId="accountType1" onmouseover="tooltip.show('Shipper',null,event);" 
                                                                   onmouseout="tooltip.hide();" onclick="checkShipperOrForwarder()" />S&nbsp;
                                                    <html:checkbox property="accountType3" name="tradingPartnerForm" onmouseover="tooltip.show('NVOCC',null,event)" onmouseout="tooltip.hide();" disabled="true"/>N&nbsp;
                                                    <html:checkbox property="accountType4" name="tradingPartnerForm" styleId="accountType4" onmouseover="tooltip.show('Consignee',null,event)" 
                                                                   onmouseout="tooltip.hide();" onclick="getNotify();checkConsigneeAccount()" />C&nbsp;
                                                    <html:checkbox property="accountType8" name="tradingPartnerForm" onmouseover="tooltip.show('Import Agent',null,event)" onmouseout="tooltip.hide();"/>AI&nbsp;
                                                    <html:checkbox property="accountType9" name="tradingPartnerForm" onmouseover="tooltip.show('Export Agent',null,event)" onmouseout="tooltip.hide();"/>AE&nbsp;
                                                    <html:checkbox property="accountType10" name="tradingPartnerForm" styleId="accountType10" onmouseover="tooltip.show('Vendor',null,event)" 
                                                                   onmouseout="tooltip.hide();" onclick="checkForVendor();checkVendorAccount()"/>V&nbsp;
                                                    <html:checkbox property="accountType11" name="tradingPartnerForm" onmouseover="tooltip.show('Others',null,event)" onmouseout="tooltip.hide();" disabled="true"/>O&nbsp;
                                                    <c:if test="${not empty loginuser && loginuser.role.roleDesc == commonConstants.ROLE_NAME_ADMIN}">
                                                        <html:checkbox property="accountType13" name="tradingPartnerForm" onmouseover="tooltip.show('Company',null,event)" onmouseout="tooltip.hide();"/>Z&nbsp;
                                                    </c:if>
                                                </td>
                                                <td id="subTypeLabel">Sub Type</td>
                                                <td id="subTypeValue">
                                                    <html:select property="subType" styleId="subType" value="${tradingPartnerForm.subType}"
                                                                 styleClass="dropdown_accounting" onchange="onChangeSubType(${roleDuty.allowImportCfsVendor});checkSubType()" style="text-transform: uppercase;">
                                                        <html:optionsCollection name="subTypeList" styleClass="unfixedtextfiledstyle"/>
                                                    </html:select>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td colspan="2">Account Type:&nbsp;
                                                    <html:checkbox property="accountType1" name="tradingPartnerForm" onmouseover="tooltip.show('Shipper',null,event);" onmouseout="tooltip.hide();" disabled="true" />S&nbsp;
                                                    <html:checkbox property="accountType3" name="tradingPartnerForm" onmouseover="tooltip.show('NVOCC',null,event)" onmouseout="tooltip.hide();" disabled="true"/>N&nbsp;
                                                    <html:checkbox property="accountType4" name="tradingPartnerForm" onmouseover="tooltip.show('Consignee',null,event)" onmouseout="tooltip.hide();" disabled="true" />C&nbsp;
                                                    <html:checkbox property="accountType8" name="tradingPartnerForm" onmouseover="tooltip.show('Import Agent',null,event)" onmouseout="tooltip.hide();" disabled="true"/>AI&nbsp;
                                                    <html:checkbox property="accountType9" name="tradingPartnerForm" onmouseover="tooltip.show('Export Agent',null,event)" onmouseout="tooltip.hide();" disabled="true"/>AE&nbsp;
                                                    <html:checkbox property="accountType10" name="tradingPartnerForm" onmouseover="tooltip.show('Vendor',null,event)" onmouseout="tooltip.hide();" disabled="true" />V&nbsp;
                                                    <html:checkbox property="accountType11" name="tradingPartnerForm" onmouseover="tooltip.show('Others',null,event)" onmouseout="tooltip.hide();" disabled="true"/>O&nbsp;
                                                    <c:if test="${not empty loginuser && loginuser.role.roleDesc == commonConstants.ROLE_NAME_ADMIN}">
                                                        <html:checkbox property="accountType13" name="tradingPartnerForm" onmouseover="tooltip.show('Company',null,event)" onmouseout="tooltip.hide();" disabled="true"/>Z&nbsp;
                                                    </c:if>
                                                </td>
                                                <td id="subTypeLabel">Sub Type</td>
                                                <td id="subTypeValue">
                                                    <html:text property="subType" styleId="subType" value="${tradingPartnerForm.subType}"
                                                               readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>Forward Account</td>
                                        <td>
                                            <c:set var="readOnlyForwardAccount" value="false"/>
                                            <c:set var="readOnlyClass" value="textlabelsBoldForTextBox"/>
                                            <c:if test="${tradingPartnerForm.disabled == 'Y'}">
                                                <c:set var="readOnlyForwardAccount" value="true"/>
                                                <c:set var="readOnlyClass" value="BackgrndColorForTextBox"/>
                                            </c:if>
                                            <html:text property="forwardAccount" styleId="forwardAccount" styleClass="${readOnlyClass} uppercase"
                                                       value="${tradingPartnerForm.forwardAccount}" readonly="${readOnlyForwardAccount}" style="float:left"/>

                                            <input type="hidden" name="forwardAccountName" id="forwardAccountName" value="${tradingPartnerForm.forwardAccountName}"/>
                                            <input type="hidden" name="forwardAccountCheck" id="forwardAccountCheck" value="${tradingPartnerForm.forwardAccount}"/>
                                            <div id="forwardAccountChoices" style="display: none;float:left" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                AjaxAutocompleter("forwardAccount", "forwardAccountChoices", "forwardAccountName", "forwardAccountCheck",
                                                "${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=forwardAccount&excludeAccountNo=<%=accountNo%>", "");
                                            </script>
                                            <c:if test="${roleDuty.disableOrEnableTp}">
                                                <c:set var="enableImg" value="none"/>
                                                <c:set var="disableImg" value="block"/>
                                                <c:if test="${tradingPartnerForm.disabled == 'Y'}">
                                                    <c:set var="enableImg" value="block"/>
                                                    <c:set var="disableImg" value="none"/>
                                                </c:if>
                                                <img id="EnableImg" title="Enable" alt="Enable"
                                                     src="${path}/images/Unlock.png" onclick="enableTradingPartner('<%=accountNo%>')" style="display: ${enableImg};float:left"/>
                                                <img id="DisableImg" title="Disable" alt="Disable"
                                                     src="${path}/images/Lock.png" onclick="disableTradingPartner('<%=accountNo%>')" style="display: ${disableImg};float:left"/>
                                            </c:if>
                                        </td>
                                        <td>Account Name</td>
                                        <td>
                                            <html:text property="accountName" styleId="accountName" styleClass="textlabelsBoldForTextBox uppercase" 
                                                       value="${tradingPartnerForm.accountName}" size="35"/>
                                        </td>       
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>ECU Designation</td>
                                        <td>
                                            <html:select property="ecuDesignation" styleId="ecuDesignation" styleClass="dropdown_accounting unfixedtextfiledstyle" 
                                                         value="${tradingPartnerForm.ecuDesignation}" disabled="${not roleDuty.ecuDesignation}" onchange="onChangeCreditStatus(this)">
                                                <html:option value="LO">LOCAL(LO)</html:option>
                                                <html:option value="IC">INTERCOMPANY(IC)</html:option>
                                                <html:option value="AG">AGENTS(AG)</html:option>
                                                <html:option value="AA">AFFILIATED AGENTS(AA)</html:option>
                                            </html:select>
                                        </td>
                                        <td>ECU Reporting Type</td>
                                        <td>
                                            <html:select property="ecuReportingType" styleId="ecuReportingType" onchange="setCorporateAcctOption()"
                                                         styleClass="dropdown_accounting unfixedtextfiledstyle"
                                                         value="${tradingPartnerForm.ecuReportingType}" >
                                                <html:optionsCollection name="corporateAcctTypeList" />
                                            </html:select>
                                        </td>
                                        <td>Main Account Name</td>
                                        <td class="textlabelsBold">
                                            <html:text property="corporateAcctName" styleId="corporateAcctName" readonly="readonly" styleClass="BackgrndColorForTextBox" onkeyup="clearData()"/>
                                            <html:hidden property="corporateAcctId" styleId="corporateAcctId" styleClass="textbox"/>
                                        </td>
                                        <td>ECU Logo</td>
                                        <td>
                                            <html:radio property="ecuLogo" value="Y" name="tradingPartnerForm" disabled="${roleDuty.changeLogoPreference eq 'true' ? 'false' :'true'}"/>Yes
                                            <html:radio property="ecuLogo" value="N" name="tradingPartnerForm" disabled="${roleDuty.changeLogoPreference eq 'true' ? 'false' :'true'}"/>No
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td></td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Brand Preference</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${companyCode == '02'}">
                                                    <html:select property="brandPreference" styleId="brandPreference" styleClass="dropdown_accounting unfixedtextfiledstyle" 
                                                                 value="${tradingPartnerForm.brandPreference}">
                                                        <html:option value="None">None</html:option>
                                                        <html:option value="OTI">OTI</html:option>
                                                        <html:option value="Ecu Worldwide">ECU Worldwide</html:option>
                                                    </html:select> 
                                                </c:when>
                                                <c:otherwise>
                                                    <html:select property="brandPreference" styleId="brandPreference" styleClass="dropdown_accounting unfixedtextfiledstyle" 
                                                                 value="${tradingPartnerForm.brandPreference}">
                                                        <html:option value="None">None</html:option>
                                                        <html:option value="Econo">Econo</html:option>
                                                        <html:option value="Ecu Worldwide">ECU Worldwide</html:option>
                                                    </html:select> 
                                                </c:otherwise>
                                            </c:choose>   
                                        </td>
                                   
                                    </tr>
                                    <c:if test="${master && roleDuty.changeMaster}">
                                        <tr>
                                            <th colspan="4" class="table-head-background">
                                                Subsidiary Accounts
                                            </th>
                                        </tr>
                                        <tr>
                                            <td colspan="4">
                                                <div class="result-container" id="subsidiaryAccountsDiv" style="height: 300px"></div>
                                            </td>
                                        </tr>
                                    </c:if>
                                </table>
                            </div>
                            <div class="pane" id="tab7" style="width: 100%;display: none">
                                <table width="100%" border="0"  class="tableBorderNew">
                                    <tr>
                                        <td>
                                            <table cellpadding="3" cellspacing="2" border="0" class="tableBorderNew"  width="100%">
                                                <tr class="tableHeadingNew">
                                                    <td colspan="3">Quotation</td>
                                                    <td align="right"><input type="button" onclick="openRatesGrid('<%=accountNo%>')" value="Input Rates Manually" style="width: 115px;" class="buttonStyleNew"/></td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Use Default Values</td>
                                                    <td>
                                                        <html:radio property="applyDefaultValues" value="Y" styleId="applyDefaultValuesY"
                                                                    name="tradingPartnerForm"/>Yes
                                                        <html:radio property="applyDefaultValues" value="N" styleId="applyDefaultValuesN"
                                                                    name="tradingPartnerForm"/>No
                                                    </td>
                                                    <td align="right"></td>
                                                    <td></td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Destination</td>
                                                    <td>
                                                        <input Class="textlabelsBoldForTextBox" name="destination" id="finalDestination" value="${tradingPartnerForm.destination}" size="22" onchange="disableAutoFF()"/>
                                                        <input type="hidden" id="portofDischarge_check" value="${tradingPartnerForm.destination}"/>
                                                        <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocomplete("finalDestination", "destination_port_choices", "", "portofDischarge_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=2&isDojo=false", "disableAutoFF()");
                                                        </script>
                                                    </td>
                                                    <td align="right">POL</td>
                                                    <td>
                                                        <input Class="textlabelsBoldForTextBox" name="pol" id="portofladding" value="${tradingPartnerForm.pol}" size="22"/>
                                                        <input type="hidden" id="pol_check" value="${tradingPartnerForm.pol}"/>
                                                        <div id="pol_port_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocomplete("portofladding", "pol_port_choices", "", "pol_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=0&isDojo=false", "");
                                                        </script>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Origin</td>
                                                    <td>
                                                        <input Class="textlabelsBoldForTextBox" name="origin" id="terminal" value="${tradingPartnerForm.origin}" size="22"/>
                                                        <input type="hidden" id="origin_check" value="${tradingPartnerForm.origin}"/>
                                                        <div id="origin_port_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocomplete("terminal", "origin_port_choices", "", "origin_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=1&isDojo=false", "");
                                                        </script>
                                                    </td>
                                                    <td align="right">POD</td>
                                                    <td>
                                                        <input Class="textlabelsBoldForTextBox" name="pod" id="portofdischarge" value="${tradingPartnerForm.pod}" size="22"/>
                                                        <input type="hidden" id="pod_check" value="${tradingPartnerForm.pod}"/>
                                                        <div id="pod_port_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocomplete("portofdischarge", "pod_port_choices", "", "pod_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=3&isDojo=false", "");
                                                        </script>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">CommCode</td>
                                                    <td><input name="commodityCode" class="textlabelsBoldForTextBox" id="commcode" value="${tradingPartnerForm.commodityCode}"
                                                               maxlength="6" size="22" />
                                                        <input id="commcode_check" type="hidden" value="${tradingPartnerForm.commodityCode}"/>
                                                        <div id="commcode_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocomplete("commcode", "commcode_choices", "", "commcode_check",
                                                            "${path}/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false", "");
                                                        </script>
                                                    </td>
                                                    <td align="right">NVO Move</td>
                                                    <td>
                                                        <html:select property="nvoMove" styleId="typeofMoveSelect"  style="width:130px;" onchange="checkDoorMove()"
                                                                     styleClass="dropdown_accounting"  value="${tradingPartnerForm.nvoMove}" >
                                                            <html:optionsCollection name="typeOfMoveList" />
                                                        </html:select>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Issuing TM</td>
                                                    <td>
                                                        <input type="text" Class="textlabelsBoldForTextBox"  name="issuingTerminal"
                                                               value="${tradingPartnerForm.issuingTerminal}" id="issuingTerminal" size="22"  />
                                                        <input type="hidden" value="${tradingPartnerForm.issuingTerminal}" id="issuingTerminal_check" />
                                                        <div id="issuingTerminal_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>

                                                        <script type="text/javascript">
                                                            initAutocomplete("issuingTerminal", "issuingTerminal_choices", "", "issuingTerminal_check",
                                                            "${path}/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false", "");
                                                        </script>
                                                    </td>
                                                    <td align="right">ERT</td>
                                                    <td>
                                                        <html:select property="ert"  name="tradingPartnerForm" style="width:130px;"
                                                                     styleClass="dropdown_accounting"
                                                                     styleId="routedAgentCheck" onchange="validateDefaultAgent(this)">
                                                            <html:option value="">Select</html:option>
                                                            <html:option value="yes">Yes</html:option>
                                                            <html:option value="no">No</html:option>
                                                        </html:select>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Origin Zip/City</td>
                                                    <td>
                                                        <html:text property="originZip"  styleId="zip"  styleClass="textlabelsBoldForTextBox"
                                                                   value="${tradingPartnerForm.originZip}" size="22"  onkeydown="setNVOmove()" />
                                                        <input type="hidden" id="zip_check" value="${tradingPartnerForm.originZip}"/>
                                                        <div id="zip_choices"  style="display: none;width: 5px;"    class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("zip", "zip_choices", "doorOrigin", "zip_check",
                                                            "${path}/actions/getZipCode.jsp?tabName=QUOTE&from=1", "", "");
                                                        </script>
                                                    </td>
                                                    <td align="right">Door Origin</td>
                                                    <td>
                                                        <html:text property="doorOrigin"  styleId="doorOrigin"  styleClass="BackgrndColorForTextBox"
                                                                   value="${tradingPartnerForm.doorOrigin}" size="22" readonly="true" tabindex="-1"/>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Auto Deduct FF Commission</td>
                                                    <td>
                                                        <html:radio property="ffComm" value="Y" styleId="ffCommY"
                                                                    name="tradingPartnerForm"/>Yes
                                                        <html:radio property="ffComm" value="N" styleId="ffCommN"
                                                                    name="tradingPartnerForm"/>No
                                                    </td>
                                                    <td align="right">Document Charge</td>
                                                    <td>
                                                        <html:radio property="documentCharge" value="Y" styleId="docChargeY"
                                                                    name="tradingPartnerForm" onclick="hideUnhideDocCharge()"/>Yes
                                                        <html:radio property="documentCharge" value="N" styleId="docChargeN"
                                                                    name="tradingPartnerForm" onclick="hideUnhideDocCharge()"/>No
                                                        <c:choose>
                                                            <c:when test="${tradingPartnerForm.documentCharge == 'Y'}">
                                                                <html:text property="documentChargeAmount" styleClass="textlabelsBoldForTextBox" size="10" styleId="documentChargeAmount" value="${tradingPartnerForm.documentChargeAmount}" onchange="checkForNumberAndDecimal(this)" maxlength="8"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <html:text property="documentChargeAmount" styleClass="textlabelsBoldForTextBox" size="10" styleId="documentChargeAmount" value="0.00" style="visibility:hidden" maxlength="8"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold" >
                                                    <td colspan="2" align="left">Goods Description: </td>
                                                    <td colspan="2" align="left">Important Notes(Pop-Up): </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td colspan="2">
                                                        <html:textarea  property="goodsDesc" styleClass="textlabelsBoldForTextBox"
                                                                        value="${tradingPartnerForm.goodsDesc}" cols="55"
                                                                        style="text-transform: uppercase;" rows="6"
                                                                        onblur="return limitTextchars(this, 499)" />

                                                    </td>
                                                    <td colspan="2">
                                                        <html:textarea  property="importantNotes" styleClass="textlabelsBoldForTextBox"
                                                                        value="${tradingPartnerForm.importantNotes}" cols="55"
                                                                        style="text-transform: uppercase;" rows="6"
                                                                        onblur="return limitTextchars(this, 499)"/>

                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                        <td valign="top">
                                            <table cellpadding="3" cellspacing="2" border="0"  class="tableBorderNew" width="100%">
                                                <tr class="tableHeadingNew"><td colspan="4">Booking</td></tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Shipper name </td>
                                                    <td>
                                                        <input type="text" Class="textlabelsBoldForTextBox" name="shipperName" id="shipperName"
                                                               style="text-transform: uppercase" size="30" maxlength="200" value="${tradingPartnerForm.shipperName}"
                                                               onfocus="disableDojo(this)" onkeyup="copyNotListedTp(this, 'shipperCopy')"
                                                               />
                                                        <input id="shipperName_check" type="hidden" value="${tradingPartnerForm.shipperName}" />
                                                        <div id="shipper_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("shipperName", "shipper_choices", "shipperNo", "shipperName_check",
                                                            "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=3", "getShipperInfo()", "onShipperBlur();");
                                                        </script>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                                              onmouseout="tooltip.hide();">
                                                            <input type="hidden" id="shipperCopy"/>
                                                            <html:checkbox property="shipperCheck" styleId="shipperCheck" name="tradingPartnerForm"
                                                                           onclick="disableAutoCompleter(this)"/>
                                                        </span>

                                                    </td>
                                                    <td align="right">Shipper Number </td>
                                                    <td>
                                                        <input name="shipperNo" Class="BackgrndColorForTextBox" id="shipperNo"
                                                               value="${tradingPartnerForm.shipperNo }" maxlength="15" size="20"
                                                               style="text-transform: uppercase" readonly="true" tabindex="-1" />
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Consignee Name </td>
                                                    <td>
                                                        <html:text styleClass="textlabelsBoldForTextBox" property="consigneename" styleId="consigneename"
                                                                   style="text-transform: uppercase"  size="30" maxlength="200"  value="${tradingPartnerForm.consigneename}"
                                                                   onfocus="disableDojo(this)" onkeyup="copyNotListedTp(this,'consigneeCopy')"/>
                                                        <input id="consigneename_check" type="hidden" value="${tradingPartnerForm.consigneename}"/>
                                                        <div id="consigneename_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("consigneename", "consigneename_choices", "consigneeNo", "consigneename_check",
                                                            "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=5", "getConsigneeInfo()", "onConsigneeBlur();");
                                                        </script>
                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                                              onmouseout="tooltip.hide();">
                                                            <input type="hidden" id="consigneeCopy"/>
                                                            <html:checkbox property="consigneeCheck" styleId="consigneeCheck" name="tradingPartnerForm"
                                                                           onclick="disableAutoCompleter(this)"/>
                                                        </span>
                                                    </td>
                                                    <td align="right">Consignee Number </td>
                                                    <td>
                                                        <input  name="consigneeNo" Class="BackgrndColorForTextBox" id="consigneeNo"
                                                                value="${tradingPartnerForm.consigneeNo}" maxlength="15" size="20" readonly="true"
                                                                style="text-transform: uppercase" tabindex="-1" />
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">
                                                    <td align="right">Forwarder Name </td>
                                                    <td>
                                                        <html:text style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" property="fowardername" styleId="fowardername"
                                                                   size="30" maxlength="200"  value="${tradingPartnerForm.fowardername}"  />
                                                        <input id="fowardername_check" type="hidden" value="${tradingPartnerForm.fowardername}" />
                                                        <div id="forward_choices" style="display: none" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("fowardername", "forward_choices", "forwarderNo", "fowardername_check",
                                                            "${path}/actions/tradingPartner.jsp?tabName=BOOKING&from=4", "getForwarderInfo()", "");
                                                        </script>
                                                    </td>
                                                    <td align="right">Forwarder Number </td>
                                                    <td>
                                                        <input  name="forwarderNo" Class="BackgrndColorForTextBox" id="forwarderNo"
                                                                value="${tradingPartnerForm.forwarderNo }" maxlength="15" size="20" readonly ="true" tabindex ="-1"
                                                                style="text-transform: uppercase"  />
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">

                                                    <td align="right">Prepaid/Collect</td>
                                                    <td>
                                                        <html:radio property="prepaidOrCollect" value="P" styleId="prepaid" name="tradingPartnerForm" onclick="changeBillTo()"/>P
                                                        <html:radio property="prepaidOrCollect" styleId="collect" value="C" name="tradingPartnerForm" onclick="changeBillTo()"/>C
                                                    </td>
                                                    <c:choose>
                                                        <c:when test="${tradingPartnerForm.prepaidOrCollect == 'P'}">
                                                            <c:set var="disableP" value="false"/>
                                                            <c:set var="disableC" value="true"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="disableP" value="true"/>
                                                            <c:set var="disableC" value="false"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td align="right">Bill To Code </td>
                                                    <td>
                                                        <html:radio property="billTo" value="F"  name="tradingPartnerForm" disabled="${disableP}" onclick="validateBillTo()"/>F
                                                        <html:radio property="billTo" value="S"  name="tradingPartnerForm" disabled="${disableP}"/>S
                                                        <html:radio property="billTo" value="T" name="tradingPartnerForm" disabled="${disableP}"/>T
                                                        <html:radio property="billTo" value="C" name="tradingPartnerForm" disabled="${disableC}"/>A
                                                    </td>
                                                </tr>
                                                <tr class="textlabelsBold">

                                                    <td align="right">Line Move</td>
                                                    <td>
                                                        <html:select property="lineMove" styleId="lineMove"  style="width:130px;"
                                                                     styleClass="dropdown_accounting"  value="${tradingPartnerForm.lineMove}" >
                                                            <html:optionsCollection name="typeOfMoveList" />
                                                        </html:select>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="accountType" styleId="accountType" value="${tradingPartnerForm.accountType}"/>
            <html:hidden property="tradingPartnerId" value="<%=accountNo%>" />
            <html:hidden property="accountNo" styleId="accountNo" value="<%=accountNo%>"/>
            <html:hidden property="exportAgent" styleId="exportAgent" name="tradingPartnerForm"/>
            <html:hidden property="importAgent" styleId="importAgent" name="tradingPartnerForm"/>
            <html:hidden property="buttonValue"/>
            <c:if test="${empty selectedTab || selectedTab==0}">
                <c:set var="selectedTab" value="1"/>
            </c:if>
            <html:hidden property="selectedTab" styleId="selectedTab" value="${selectedTab}"/>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/TradingPartner/generalInformation.js"></script>
    <!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE -->
    <c:if test="${view=='3' || not empty disableTabBasedOnRole}">
        <script type="text/javascript">
            disablePage(document.getElementById("generalInfo"));
        </script>
    </c:if>
</html>

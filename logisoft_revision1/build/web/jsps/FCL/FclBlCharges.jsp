<%@page import="com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.bc.fcl.FclBlConstants,
         java.text.NumberFormat,java.text.DecimalFormat,com.gp.cvst.logisoft.domain.FclBl,com.gp.cong.logisoft.bc.fcl.FclBlBC,
         com.gp.cvst.logisoft.domain.FclBlCharges,com.gp.cvst.logisoft.domain.FclBlCostCodes,com.gp.cvst.logisoft.domain.CustAddress,
         java.text.SimpleDateFormat,com.gp.cvst.logisoft.beans.TransactionBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="../includes/jspVariables.jsp" %>
  <script type="text/javascript" src="/logisoft/js/jquery/jquery.tooltip.js"></script>
  <script type="text/javascript" src="/logisoft/js/jquery/jquery.qtip-1.0.0-rc3.min.js"></script>
  <link type="text/css" rel="stylesheet" media="screen" href="/logisoft/css/jquery/jquery.tooltip.css" />
<%
    String path = request.getContextPath();
    boolean hasUserLevelAccess = roleDuty.isShowDetailedCharges();
    boolean reSendAccruals = roleDuty.isResendAccruals();
    String adminCostCode = "";
    String fFChargeCode = "";
    String fAEChargeCode = "";
    String closedBy = "";
    String userid = "";
    String fileNo = "";
    String charges = "";
    DBUtil dbUtil = new DBUtil();
    NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    
    List chargelist = null;
    List costList = null;
    String totalCost = "0.00";
    double totCost = 0.0;
    double totalCostOfCollapse = 0.0;
    double totCostForCharges = 0.0;
    double totCostForChargesDup = 0.0;
    double profit = 0.0;
    String total = "0.00";
    String billThirdParty = "";
    String port = "";
    String issuingTerminal = "";
    Double shipperTotal = 0.00;
    Double notifyTotal = 0.00;
    String streamShipLine = "";
    String billThirdPartyAddress = "";
    Double forwarderTotal = 0.00;
    Double agentTotal = 0.00;
    Double consigneeTotal = 0.00;
    String billThirdPartyName = "";
    String readyToPOst = "";
    String thirdParty = "";
    String readyToPost = "";
    Double thirdPartyTotal = 0.00;
    String forignAgent = "";
    String agent = null;
    String faeOrIncent=request.getAttribute("faeCalculation") != null?(String)request.getAttribute("faeCalculation"):"";
    List addchargeslist = null;
    if (request.getAttribute("addchargeslist") != null) {
        addchargeslist = (List) request.getAttribute("addchargeslist");
    }
    List collapseList = new ArrayList();
    if (request.getAttribute("consolidatorList") != null) {
        collapseList = (List) request.getAttribute("consolidatorList");
    }
    List addcostcodeslist = null;
    if (request.getAttribute(FclBlConstants.FCLBL_COSTS_LIST) != null) {
        addcostcodeslist = (List) request.getAttribute(FclBlConstants.FCLBL_COSTS_LIST);
        if(!addcostcodeslist.isEmpty()){
        FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) addcostcodeslist.get(0);
            request.setAttribute("hasTransactionType", new FclBlCostCodesDAO().hasTransactionType(fclBlCostCodes.getBolId().toString()));
        }else{
            request.setAttribute("hasTransactionType",true);
        }
    }else{
        request.setAttribute("hasTransactionType",true);
    }
    request.setAttribute("toggleCostTable", null!=addcostcodeslist?addcostcodeslist.isEmpty():true);
    List collapseCostList = new ArrayList();
    if (request.getAttribute("consolidatorCostList") != null) {
        collapseCostList = (List) request.getAttribute("consolidatorCostList");
    }
    request.setAttribute("faeOrIncent", faeOrIncent);
%>
</head>
<script language="javascript" type="text/javascript">
    jQuery(document).ready(function(){
        var valueFori=document.getElementById('iIconBillToolTip').value;
       ImportsJToolTip('#vendorDetailsForicon',valueFori, 800,'topLeft');
    });
    var bool = true;
    var hasTransactionType=${hasTransactionType};
    var toggleCostTable=${toggleCostTable};
    function showHideCharges() {
        if(document.getElementById("collapsetable")||document.getElementById("chargestable")){
           if (bool == true) {
            document.getElementById("collapsetable").style.display = "block";
            document.getElementById("chargestable").style.display = "none";
            bool = false;
           } else {
            document.getElementById("collapsetable").style.display = "none";
            document.getElementById("chargestable").style.display = "block";
            bool = true;
          }
       }
    }

</script>
<body>

    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <table width="100%" border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" >
            <tr class="tableHeadingNew" >
                <td>
                    <img src="${path}/img/icons/up.gif" border="0" onclick="showHideCharges()" id="chargesExpandImage"/>
                Charges </td>

            <td align="right" class="textlabelsBold">
                <c:if test="${fclBlForm.fclBl.readyToPost != 'M'}">
                    <input type="button" value="Add" class="buttonStyleNew" id="add" onclick="addNewBlCharges('${not empty consolidatorList ? fn:length(consolidatorList) : 0}')"/>
                </c:if>
            </td>
        </tr>
        <tr >
            <td colspan="2">
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <%
                        int i = 0;
                    %>
                    <display:table  name="<%=addchargeslist%>"  id="chargestable" class="displaytagstyle" pagesize="<%=pageSize%>" style="display:none">
                        <display:setProperty name="basic.msg.empty_list">
                            <span style="display:none;" class="pagebanner" />
                        </display:setProperty>
                        <display:setProperty name="paging.banner.placement" value="false">
                            <span style="display:none;"></span>
                        </display:setProperty>
                        <%
                            String charge = "", amount = "0.00", billTo = "", adjustment = "0.00", printBill = "", chargeCode = "", currency = "USD", readyToPost1 = "", prepaid = "", collect = "";
                            String id = "", readOnlyFlag = "", chargesRemarks = "", bookingFlag = "", oldAmount = "0.00", totalAmount = "0.00",adjustmentChargesRemarks="";

                            if (addchargeslist != null && addchargeslist.size() > 0) {
                                FclBlCharges fclblCharges = (FclBlCharges) addchargeslist.get(i);
                                if (fclblCharges.getChargesId() != null) {
                                    id = fclblCharges.getChargesId().toString();
                                } else {
                                    id = null;
                                }
                                if (fclblCharges.getCharges() != null) {
                                    charge = fclblCharges.getCharges();
                                }
                                if (fclblCharges.getChargeCode() != null) {
                                    chargeCode = fclblCharges.getChargeCode();
                                }
                                if (fclblCharges.getReadyToPost() != null && (fclblCharges.getReadyToPost().equals("M")
                                        || fclblCharges.getReadyToPost().equals("on"))) {
                                    readyToPost = fclblCharges.getReadyToPost();
                                    readyToPost1 = fclblCharges.getReadyToPost();
                                }

                                if (null != fclblCharges.getReadOnlyFlag()) {
                                    readOnlyFlag = fclblCharges.getReadOnlyFlag();
                                }
                                if (null != fclblCharges.getBookingFlag()) {
                                    bookingFlag = fclblCharges.getBookingFlag();
                                } else {
                                    bookingFlag = "";
                                }
                                if (!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")) {
                                    amount = numberFormat.format(fclblCharges.getAmount());
                                    oldAmount = numberFormat.format(fclblCharges.getOldAmount());
                                    if (null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d) {
                                        adjustment = numberFormat.format(fclblCharges.getAdjustment());
                                        totalAmount = numberFormat.format(fclblCharges.getAmount() + fclblCharges.getAdjustment());
                                    }
                                } else if (fclblCharges.getOldAmount() != null) {
                                    amount = numberFormat.format(fclblCharges.getOldAmount());
                                    if (null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d) {
                                        adjustment = numberFormat.format(fclblCharges.getAdjustment());
                                        totalAmount = numberFormat.format(fclblCharges.getOldAmount() + fclblCharges.getAdjustment());
                                    }
                                }
                                totCostForCharges = totCostForCharges + fclblCharges.getAmount();
                                if (fclblCharges.getBillTo() != null) {
                                    billTo = fclblCharges.getBillTo();
                                }
                                if (fclblCharges.getPrintOnBl() != null) {
                                    printBill = fclblCharges.getPrintOnBl();
                                }
                                if (fclblCharges.getCurrencyCode() != null) {
                                    currency = fclblCharges.getCurrencyCode();
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("ThirdParty")) {
                                    thirdParty = fclblCharges.getBillTo();
                                    thirdPartyTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("Shipper")) {
                                    shipperTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("NotifyParty")) {
                                    notifyTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("Agent")) {
                                    agentTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase(FclBlConstants.CONSIGNEE)) {
                                    consigneeTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }

                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("Forwarder")) {
                                    forwarderTotal += (null != fclblCharges.getAmount()) ? fclblCharges.getAmount() : 0.00;
                                }

                                if (null != fclblCharges.getChargesRemarks()) {
                                    chargesRemarks = fclblCharges.getChargesRemarks();
                                }

                                StringBuilder s = new StringBuilder();
                                if (chargesRemarks != null) {
                                    int index = 0;
                                    char[] commentArray = chargesRemarks.toCharArray();
                                    for (int j = 0; j < commentArray.length; j++) {
                                        if (commentArray[j] == '\n') {
                                            s.append(chargesRemarks.substring(index, j).trim());
                                            s.append(" ");
                                            index = j + 1;
                                        }
                                    }
                                    s.append(chargesRemarks.substring(index, chargesRemarks.length()).trim());
                                }
                                s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                chargesRemarks = s.toString();
                                if(null!=fclblCharges.getAdjustmentChargesRemarks()){
                                    adjustmentChargesRemarks=fclblCharges.getAdjustmentChargesRemarks().toUpperCase();
                                } 

                            }
                            request.setAttribute("totalAmountExpand", totalAmount);
                        %>
                        <%if (bookingFlag.equalsIgnoreCase("new") || bookingFlag.equalsIgnoreCase("PP")) {%>
                        <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
                        <%} else if (readOnlyFlag.equalsIgnoreCase("on")) {%>
                        <display:column style="visibility:hidden;width:8px;" title=""></display:column>
                        <%} else {%>
                        <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
                        <%}%>
                        <display:column title="Chg Code" style="width:150px">
                            <input type="text" name="chargeCode<%=i%>" class="BackgrndColorForTextBox" id="chargeCode" readonly="true"
                                   value="<%=chargeCode%>"  onkeydown="getComCodeForCharges(this.value,<%=i%>)" size="4" />
                            <dojo:autoComplete formId="fclBillLaddingform"  textboxId="chargeCode<%=i%>"
                                               action="${path}/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=i%>"/>
                        </display:column>
                        <display:column title="Chg Code Desc" style="width:350px">
                            <input type="text" name="charge<%=i%>" class="BackgrndColorForTextBox"  id="charge" readonly="true"
                                   value="<%=charge%>" onkeydown="getChargeCodeDescription(this.value,<%=i%>)" />
                            <dojo:autoComplete formId="fclBillLaddingform" textboxId="charge<%=i%>"
                                               action="${path}/actions/getChargeCodeDesc.jsp?tabName=FCL_BL_CHARGES&index=<%=i%>"/>
                        </display:column>
                        <display:column title="Amount" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="amount" readonly="true"
                                   value="<%=amount%>" size="4" />
                        </display:column>
                        <display:column title="Adjustment" style="width:100px">
                            <input type="text" name="adjustment" maxlength="8"  value="<%=adjustment%>"
                                   class="BackgrndColorForTextBox" readonly="true"  size="4"  />
                            <c:if test="${not empty totalAmountExpand && totalAmountExpand != '0.00'}">
                                &nbsp;&nbsp;(Tot $ ${totalAmountExpand})
                            </c:if>
                        </display:column>
                        <display:column title="Currency" style="width:100px">
                            <input type=text" name="currency" value="<%=currency%>"
                                   class="BackgrndColorForTextBox" readonly="true" size="4"/>
                        </display:column>
                        <display:column title="Bill To Party" style="width:100px">
                            <html:text property="billTo" value="<%=billTo%>" name="billTo"
                                       styleClass="BackgrndColorForTextBox" readonly="true" size="8"/>
                        </display:column>
                        <display:column title="Print On BL" style="width:100px">
                            <input type=text" name="printBl" value="<%=printBill%>"
                                   class="BackgrndColorForTextBox" readonly="true" size="4"/>
                        </display:column>
                        <display:column title="Action" style="width:100px">

                            <%if (null != chargesRemarks && !chargesRemarks.trim().equals("")) {%>
                            <span class="hotspot" >
                                <img id="viewgif1"  src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=chargesRemarks%></strong>',100,event);"
                                     onmouseout="tooltip.hideComments();" style="color:black;"/></span>
                                     <%}if(null != adjustmentChargesRemarks && !adjustmentChargesRemarks.trim().equals("")){%>
                <span class="hotspot" onmouseover="tooltip.showComments('<strong><%=adjustmentChargesRemarks%></strong>',100,event);"
				onmouseout="tooltip.hideComments();" style="color:black;">
			   <img id="adjustmentChargeCommentViewGif"  src="<%=path%>/img/icons/view.gif"/></span>
                                <%}
                if (!chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)) {%>
                                <%if (!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")) {%>
                            <span class="hotspot">
                                <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment%>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                                     id="chargesexpand"  onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                <%} else {%>
                            <span class="hotspot" >
                                <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment%>')" name="<%=i%>"
                                     id="chargesexpand" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                <%}%>
                <%}%>
                        <c:if test="${fclBlForm.fclBl.readyToPost ne 'M'&& roleDuty.deleteCostandCharges}">
                            <span class="hotspot" >
                                <img src="${path}/img/icons/delete.gif" border="0" onclick="chargesdelete('<%=id%>')" name="<%=i%>"
                                     id="chargesDeleteExpand" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/> </span>
                        </c:if>
                        </display:column>
                        <%i++;%>
                    </display:table>
                </table></td>
        </tr>

        <tr>
            <td colspan="2">
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <%
                        i = 0;
                    %>
                    <display:table  name="<%=collapseList%>"   id="collapsetable" class="displaytagstyle" pagesize="<%=pageSize%>" style="display:block">
                        <display:setProperty name="basic.msg.empty_list">
                            <span style="display:none;" class="pagebanner" />
                        </display:setProperty>
                        <display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
                        <%
                            String charge = "", amount = "0.00", adjustment = "0.00", billTo = "", printBill = "", chargeCode = "", currency = "USD", readyToPost1 = "", prepaid = "", collect = "";
                            String id = "", readOnlyFlag = "", chargesRemarks = "", bookingFlag = "", oldAmount = "0.00", totalAmount = "0.00",adjustmentChargesRemarks="";


                            if (collapseList != null && collapseList.size() > 0) {
                                FclBlCharges fclblCharges = (FclBlCharges) collapseList.get(i);
                                if (fclblCharges.getChargesId() != null) {
                                    id = fclblCharges.getChargesId().toString();
                                } else {
                                    id = null;
                                }

                                if (fclblCharges.getReadyToPost() != null && (fclblCharges.getReadyToPost().equals("M")
                                        || fclblCharges.getReadyToPost().equals("on"))) {
                                    readyToPost = fclblCharges.getReadyToPost();
                                    readyToPost1 = fclblCharges.getReadyToPost();
                                }

                                charge = fclblCharges.getCharges();
                                chargeCode = fclblCharges.getChargeCode();
                                if (null != fclblCharges.getReadOnlyFlag()) {
                                    readOnlyFlag = fclblCharges.getReadOnlyFlag();
                                }
                                if (null != fclblCharges.getBookingFlag()) {
                                    bookingFlag = fclblCharges.getBookingFlag();
                                } else {
                                    bookingFlag = "";
                                }
                                if (!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")) {
                                    amount = numberFormat.format(fclblCharges.getAmount());
                                    oldAmount = numberFormat.format(fclblCharges.getOldAmount());
                                    if (null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d) {
                                        adjustment = numberFormat.format(fclblCharges.getAdjustment());
                                        totalAmount = numberFormat.format(fclblCharges.getAmount() + fclblCharges.getAdjustment());
                                    }
                                } else if (fclblCharges.getOldAmount() != null) {
                                    amount = numberFormat.format(fclblCharges.getOldAmount());
                                    if (null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d) {
                                        adjustment = numberFormat.format(fclblCharges.getAdjustment());
                                        totalAmount = numberFormat.format(fclblCharges.getOldAmount() + fclblCharges.getAdjustment());
                                    }
                                }
                                if (fclblCharges.getBillTo() != null) {
                                    billTo = fclblCharges.getBillTo();
                                }
                                if (fclblCharges.getPrintOnBl() != null) {
                                    printBill = fclblCharges.getPrintOnBl();
                                }
                                if (fclblCharges.getCurrencyCode() != null) {
                                    currency = fclblCharges.getCurrencyCode();
                                }
                                if (fclblCharges.getBillTo() != null && fclblCharges.getBillTo().equalsIgnoreCase("ThirdParty")) {
                                    thirdParty = fclblCharges.getBillTo();
                                    //thirdPartyTotal+=fclblCharges.getAmount();
                                }
                                if (null != fclblCharges.getChargesRemarks()) {
                                    chargesRemarks = fclblCharges.getChargesRemarks();

                                }
                                StringBuilder s = new StringBuilder();
                                if (chargesRemarks != null) {
                                    int index = 0;
                                    char[] commentArray = chargesRemarks.toCharArray();
                                    for (int j = 0; j < commentArray.length; j++) {
                                        if (commentArray[j] == '\n') {
                                            s.append(chargesRemarks.substring(index, j).trim());
                                            s.append(" ");
                                            index = j + 1;
                                        }
                                    }
                                    s.append(chargesRemarks.substring(index, chargesRemarks.length()).trim());

                                }
                                s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                chargesRemarks = s.toString();
                                if(null!=fclblCharges.getAdjustmentChargesRemarks()){
                                    adjustmentChargesRemarks=(fclblCharges.getAdjustmentChargesRemarks()).toUpperCase();
                                } 
                            }
                            request.setAttribute("totalAmountCollapse", totalAmount);
                        %>

                        <%if (bookingFlag.equalsIgnoreCase("new") || bookingFlag.equalsIgnoreCase("PP")) {%>
                        <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
                        <%} else if (readOnlyFlag.equalsIgnoreCase("on")) {%>
                        <display:column style="visibility:hidden;width:8px;" title=""></display:column>
                        <%} else {%>
                        <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:14px;width:10px;">**</display:column>
                        <%}%>
                        <display:column title="Chg Code" style="width:150px">
                            <input type="text" name="chargeCode<%=i%>" class="BackgrndColorForTextBox" id="chargeCode" readonly="true"
                                   value="<%=chargeCode%>"  onkeydown="getComCodeForCharges(this.value,<%=i%>)" size="4" tabindex="-1" />
                            <dojo:autoComplete formId="fclBillLaddingform"  textboxId="chargeCode<%=i%>"
                                               action="${path}/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=i%>"/>
                        </display:column>
                        <display:column title="Chg Code Desc" style="width:200px">
                            <input type="text" name="charge<%=i%>" class="BackgrndColorForTextBox"  id="charge" readonly="true"
                                   value="<%=charge%>" onkeydown="getChargeCodeDescription(this.value,<%=i%>)" tabindex="-1" />
                            <dojo:autoComplete formId="fclBillLaddingform" textboxId="charge<%=i%>"
                                               action="${path}/actions/getChargeCodeDesc.jsp?tabName=FCL_BL_CHARGES&index=<%=i%>"/>
                        </display:column>
                        <display:column title="Amount" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="amount" readonly="true"
                                   value="<%=amount%>" size="4" tabindex="-1"/>
                        </display:column>
                        <display:column title="Adjustment" style="width:200px">
                            <fmt:formatNumber type="number"  var="adjustment"  pattern="########0.00" />
                            <input type="text" name="adjustment" maxlength="8"  value="<%=adjustment%>"
                                   class="BackgrndColorForTextBox" readonly="true"  size="4" tabindex="-1" />
                            <c:if test="${not empty totalAmountCollapse && totalAmountCollapse != '0.00'}">
                                &nbsp;&nbsp;(Tot $ ${totalAmountCollapse})
                            </c:if>
                        </display:column>
                        <display:column title="Currency" style="width:100px">
                            <input type=text" name="currency" value="<%=currency%>"
                                   class="BackgrndColorForTextBox" readonly="true" size="4" tabindex="-1" />
                        </display:column>
                        <display:column title="Bill To Party" style="width:100px">
                            <html:text property="billTo" value="<%=billTo%>" name="billTo"
                                       styleClass="BackgrndColorForTextBox" readonly="true" size="8" tabindex="-1" />
                        </display:column>
                        <display:column title="Print On BL" style="width:100px">
                            <input type=text" name="printBl" value="<%=printBill%>"
                                   class="BackgrndColorForTextBox" readonly="true" size="4" maxlength="4" tabindex="-1" />
                        </display:column>
                        <display:column title="Action" style="width:100px">
                            <%if (null != chargesRemarks && !chargesRemarks.trim().equals("")) {%>
                            <span class="hotspot"  style="color:black;">
                                <img id="viewgif2"  src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong><%=chargesRemarks%></strong>',100,event);"
                                     onmouseout="tooltip.hideComments();"/></span>
                            <%}if(null != adjustmentChargesRemarks && !adjustmentChargesRemarks.trim().equals("")){%>
                <span class="hotspot" onmouseover="tooltip.showComments('<strong><%=adjustmentChargesRemarks%></strong>',100,event);"
				onmouseout="tooltip.hideComments();" style="color:black;">
			   <img id="adjustmentChargeCommentViewGif"  src="<%=path%>/img/icons/view.gif"/></span>
                                <%}
                    if (!chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)) {%>
                                <c:choose>
                                    <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
                                        <%if (!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")) {%>
                                    <span class="hotspot">
                                        <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','collapse','<%=amount%>','<%=adjustment%>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                                             id="chargesexpandImport" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                        <%} else {%>
                                    <span class="hotspot">
                                        <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment%>')" name="<%=i%>"
                                             id="chargesexpandImport" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                        <%}%>
                                    </c:when>
                                    <c:otherwise>
                                        <%if (!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")) {%>
                                    <span class="hotspot">
                                        <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','collapse','<%=amount%>','<%=adjustment%>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                                             id="chargesexpand" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                        <%} else {%>
                                    <span class="hotspot" >
                                        <img src="${path}/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment%>')" name="<%=i%>"
                                             id="chargesexpand" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                                        <%}%>
                                    </c:otherwise>
                                </c:choose>
                                <%}%>
                                <c:if test="${fclBlForm.fclBl.readyToPost ne 'M'&& roleDuty.deleteCostandCharges}">
                        <img src="${path}/img/icons/delete.gif" border="0" onclick="chargesdelete('<%=id%>')" name="<%=i%>"
                                 id="chargesDeleteCollapse" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                </c:if>
                        </display:column>

                        <%i++;%>
                    </display:table>
                </table></td>
        </tr>
    </table>

    <table width="100%" border="0" id="fclblChargesTable" class="fontsize13">
        <tr class="textlabelsBold">
            <td style="color:green;">Agent :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.agent}" size="20" readonly="readonly" id="disAgent"
                       class="displayWithBackGroundColorWhite"
                       <c:if test="${not empty fclBlForm.fclBl.agent}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.agent}</strong>',null,event);" onmouseout="tooltip.hide();"
                       </c:if>
                       />
                <input type="text" value="${fclBlForm.fclBl.agentNo}" size="10" readonly="readonly" id="disAgentNo"
                       class="displayWithBackGroundColorWhite"/></td>
            <td class="displayWithBackGroundColorRED" align="right">Routed By Agent :</td>
            <c:choose>
                <c:when test="${empty fclBlForm.fclBl.routedByAgent}">
                    <td><span class="displayWithBackGroundColorRED">NO</span></td>
                </c:when>
                <c:otherwise>
                    <td colspan="2">
                        <input type="text" value="${fclBlForm.fclBl.routedByAgent}" size="20" readonly="readonly" id="disRoutedAgent"
                               class="displayWithBackGroundColorRED"
                               <c:if test="${not empty fclBlForm.fclBl.routedByAgent}">
                                   onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.routedByAgent}</strong>',null,event);" onmouseout="tooltip.hide();"
                               </c:if>
                               />
                        <span style="padding-left:20px;">
                            <input type="text" value="${fclBlForm.fclBl.agentNo}" size="10" readonly="readonly"  id="disRoutedAgentNo"
                                   class="displayWithBackGroundColorRED"/>
                        </span></td>
                    </c:otherwise>
                </c:choose>
        </tr>
        <tr class="textlabelsBold">
            <td style="color:green;">Origin :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.terminal}" size="20" readonly="readonly" id="disOrigin"
                       class="displayWithBackGroundColorWhite"
                       <c:if test="${not empty fclBlForm.fclBl.terminal}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.terminal}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:green;" align="right">Destination :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.port}" size="20" readonly="readonly" id="disDestination"
                       class="displayWithBackGroundColorWhite"
                       <c:if test="${not empty fclBlForm.fclBl.port}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.port}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td align="left" style="color:green;">Commodity :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.commodityCode}" size="10" readonly="readonly" id="disCommodityCode"
                       class="displayWithBackGroundColorWhite"/></td>
        </tr>
        <tr class="textlabelsBold">
            <td style="color:green;padding-right:10px;">Carrier :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.sslineName}//${fclBlForm.fclBl.sslineNo}" size="30"
                       readonly="readonly"  class="displayWithBackGroundColorWhite" id="disCarrier"
                       <c:if test="${not empty fclBlForm.fclBl.sslineName}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.sslineName}//${fclBlForm.fclBl.sslineNo}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:green;" align="right">IS TM :</td>
            <td>
                <input type="text" value="${fclBlForm.fclBl.terminal}" size="20" readonly="readonly" id="disIssuingTerminal"
                       class="displayWithBackGroundColorWhite"
                       <c:if test="${not empty fclBlForm.fclBl.terminal}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.terminal}</strong>',null,event);" onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td colspan="2">Auto Deduct FF Commission :
                <c:choose>
                    <c:when test="${fclBlForm.fclBl.autoDeductFfcomm == 'Y'}">
                        <font color="red">YES</font>
                    </c:when>
                    <c:otherwise>
                        <font color="red">NO</font>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
        <c:set var="costOfGoods" value="Y" />
        <%if (shipperTotal != 0.0) {%>
        <tr class="textlabelsBold" id="shipperRow" >
            <td  style="color:green;">Bill To(Shipper):</td>
            <td  style="color:#0000FF;" >
                <input type="text" value="${fclBlForm.fclBl.houseShipperName}" size="35" readonly="readonly" id="disShipper"
                       class="displayBlueWithBackGroundColorWhite"
                       <c:if test="${not empty fclBlForm.fclBl.houseShipperName}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.houseShipperName}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:#0000FF;" align="right">
                <c:out value="${fclBlForm.fclBl.houseShipperNo}"></c:out></td>
                <td  style="color:red;" align="right" id="shippertotal">
                <c:out value="<%=numberFormat.format(shipperTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                        <c:set var="costOfGoods" value="N" />
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>
        </tr>
        <%}%>
        <%if (forwarderTotal != 0.0) {%>
        <tr class="textlabelsBold" id="forwarderRow" >
            <td width="10%" style="color:green;">Bill To(Forwarder):</td>
            <td width="20%" style="color:#0000FF;">
                <input type="text" value="${fclBlForm.fclBl.forwardingAgentName}" size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="disForwarder"
                       <c:if test="${not empty fclBlForm.fclBl.forwardingAgentName}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.forwardingAgentName}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:#0000FF;"  align="right">
                <c:out value="${fclBlForm.fclBl.forwardagentNo}"></c:out></td>
                <td style="color:red;" align="right" id="forwardertotal">
                <c:out value="<%=numberFormat.format(forwarderTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                        <c:set var="costOfGoods" value="N" />
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>
        </tr>
        <%}%>
        <%if (thirdPartyTotal != 0.0) {%>
        <tr class="textlabelsBold" id="thirdPartyRow" >
            <td width="10%" style="color:green;">Bill To(Third Party):</td>
            <td width="20%" style="color:#0000FF;">
                <input type="text" value="${fclBlForm.fclBl.thirdPartyName}" size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="disThirdParty"
                       <c:if test="${not empty fclBlForm.fclBl.thirdPartyName}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.thirdPartyName}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td  style="color:#0000FF;"  align="right">
                <c:out value="${fclBlForm.fclBl.billTrdPrty}"></c:out></td>
                <td style="color:red;" align="right">
                <c:out value="<%=numberFormat.format(thirdPartyTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>
        </tr>
        <%}%>
        <%if (agentTotal != 0.0) {%>
        <tr class="textlabelsBold" id="agentRow" >
            <td width="10%" style="color:green;">Bill To(Agent):</td>
            <td width="20%" style="color:#0000FF;">
                <input type="text" value="${fclBlForm.fclBl.agent}" size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                       <c:if test="${not empty fclBlForm.fclBl.agent}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.agent}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:#0000FF;" align="right"><c:out value="${fclBlForm.fclBl.agentNo}"></c:out></td>
                <td style="color:red;;" align="right" id="agenttotal">
                <c:out value="<%=numberFormat.format(agentTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                        <c:set var="costOfGoods" value="N" />
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>
        </tr>
        <%}%>
        <%if (consigneeTotal != 0.0) {%>
        <tr class="textlabelsBold" id="consigneeRow" >
            <td width="10%" style="color:green;">Bill To(Consignee):</td>
            <td width="20%" style="color:#0000FF;">
                <input type="text" value="${fclBlForm.fclBl.houseConsigneeName}" size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                       <c:if test="${not empty fclBlForm.fclBl.houseConsigneeName}">
                           onmouseover="tooltip.show('<strong>${fclBlForm.fclBl.houseConsigneeName}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:#0000FF;" align="right"><c:out value="${fclBlForm.fclBl.houseConsignee}"></c:out></td>
                <td style="color:red;;" align="right" id="consigneeTotal">
                <c:out value="<%=numberFormat.format(consigneeTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                        <c:set var="costOfGoods" value="N" />
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>

        </tr>
        <%}%>
        <%if (notifyTotal != 0.0) {%>
        <tr class="textlabelsBold" id="notifyPartyRow" >
            <td width="10%" style="color:green;">Bill To(NotifyParty):</td>
            <td width="20%" style="color:#0000FF;">
                <input type="text" value="${fclBl.notifyPartyName}" size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                       <c:if test="${not empty fclBl.notifyPartyName}">
                           onmouseover="tooltip.show('<strong>${fclBl.notifyPartyName}</strong>',null,event);"onmouseout="tooltip.hide();"
                       </c:if>
                       /></td>
            <td style="color:#0000FF;" align="right"><c:out value="${fclBl.notifyParty}"></c:out></td>
                <td style="color:red;;" align="right" id="notifyTotal">
                <c:out value="<%=numberFormat.format(notifyTotal)%>"></c:out></td>
                <c:if test="${costOfGoods == 'Y'}">
                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                        <c:set var="costOfGoods" value="N" />
                    <td align="right" style="color:green;;"> Value Of Goods:</td>
                    <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                </c:if>
            </c:if>

        </tr>
        <%}%>

        <tr class="textlabelsBold" id="newRow" style="display:none">
            <td width="10%" style="color:green;" id="partyLabel"></td>
            <td width="20%" style="color:#0000FF;">
                <input type="text"  size="35" readonly="readonly"
                       class="displayBlueWithBackGroundColorWhite" id="partyName"/></td>
            <td style="color:#0000FF;" id="partyNumber" align="right"></td>
            <td style="color:red;;" id="partyTotalCharges" align="right"></td>
            <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                <td align="right" style="color:green;;"> Value Of Goods:</td>
                <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
            </c:if>

        </tr>
        <tr class="textlabelsBold" >
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td style="color:blue;;" align="right">----------------</td>
        </tr>
        <tr class="textlabelsBold" >
            <td >&nbsp;</td>
            <td >&nbsp;</td>
            <td style="color:green;" align="right">
                Total
                <c:if test="${! empty multipleBl}">
                    Revenue(Multi)
                </c:if>
                :
            </td>
            <td style="color:red;;" align="right" id="totalOfCharge">
                <c:if test="${! empty invoiceAmount}">
                    (Invoice : ${invoiceAmount})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </c:if>
                <c:out value="${currentSumOfCharges}"></c:out>
                </td>
            <input type="hidden" value="${currentSumOfCharges}" id="totCostForCharges"/>
        <td style="color:red;" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <c:if test="${! empty correctedAmount}">
                (Corrected)
            </c:if>
        </td>
    </tr>
    <tr class="textlabelsBold" >
        <td >&nbsp;</td>
   <td >&nbsp;</td>
      <td style="color:green;" align="right">
            <span  class="hotspot">
              <img src="/logisoft/img/icons/iicon.png" width="15px" alt="vendorDetailsForicon" id="vendorDetailsForicon"/>  
            </span>  
            Cost Total : </td>
        <td style="color:red;" align="right" id="displayCostTotal">${totalCost}</td>
        <c:if test="${! empty latestCorrection && ! empty latestCorrection.accountName}">
            <td style="color:green;" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                Billing Now:
                <c:choose>
                    <c:when test="${latestCorrection.correctionType.code == 'B'}">
                        Collect
                    </c:when>
                    <c:otherwise>
                        Prepaid
                    </c:otherwise>
                </c:choose>
            </td>
        </c:if>
    </tr>
    <tr class="textlabelsBold" >
        <td >&nbsp;</td>
        <td >&nbsp;</td>
        <td style="color:green;" align="right" >Profit :</td>
        <td style="color:red;padding-left:11px;" align="right" id="displayProfit">${profit}</td>
        <c:if test="${! empty latestCorrection && ! empty latestCorrection.accountName}">
            <c:choose>
                <c:when test="${latestCorrection.correctionType.code == 'T'}" >
                    <td style="color:green;" align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill To : ${latestCorrection.shipper}</td>
                </c:when>
                <c:when test="${latestCorrection.correctionType.code == 'U'}" >
                    <td style="color:green;" align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill To : ${latestCorrection.forwarder}</td>
                </c:when>
                <c:otherwise>
                    <td style="color:green;" align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill To : ${latestCorrection.accountName}</td>
                </c:otherwise>
            </c:choose>
            
        </c:if>
    </tr>
</table>

<%--//:-------------------------------  sdfsd --%>

<%--****** TABLE FOR COSTS SHOWING 'AC' TYPE OF COSTS ******* --%>
<br>
<table  border="0">
    <c:if test="${fclBlForm.fclBl ne 'I'}">
            <tr>
                <td></td><td></td>
            <td align="right">
                <div id="totalCostFor" style="display: block">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style="color:green;font-size: 13px; font-weight: bold">Total costs for  &nbsp ${sslBlPrepaidCollectName} :</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style="color:red;font-size: 13px; font-weight: bold">${totalCostForSSL}</span>
                </div>
            </td>
            </tr>
        </c:if>
    <tr>
        <c:if test="${!fn:containsIgnoreCase(fclBlForm.fclBl.fileNo,'-')}">
            <c:if test="${hasTransactionType eq false}">
            <td><input type="button" value="Preview Accruals"  class="buttonStyleNew" id="previewAccruals" onclick="showAccruals()"/></td>
            </c:if>
        <td>
            <div style="display:block" id="buttonsForcost">
                <c:if test="${empty fclBlForm.fclBl.closedBy}">
                    <input type="button" value="Add Accrual"  class="buttonStyleNew" id="costadd" onclick="addNewCosts()"/>
                    <c:if test="${fclBlForm.fclBl.readyToPost ne 'M' && toggleCostTable eq false}">
                        <input type="button" value="Post Accrual Before Manifesting"  style="width:170px" class="buttonStyleNew" id="postCost" onclick="postBeforeManifest()"/>
                    </c:if>
                    <c:if test="${not empty fclBlForm.fclBl.agentNo and fclBlForm.fclBl.importFlag ne 'I'}">
                            <input type="button" value="Add FAE Accruals"  class="buttonStyleNew" style="width:120px"
                                   id="faeAdd" onclick="FEACalculation()"/>
                    </c:if>
                </c:if>
        </div>
        </td>
        </c:if>
        <c:if test="${fclBlForm.fclBl.readyToPost eq 'M'}">
            <td class="textlabelsBold">Resend Cost To Blue
                <html:radio property="fclBl.resendCostToBlue" value="Yes" styleId="resendCostToBlueYes" name="fclBlForm"/>Yes
                <html:radio property="fclBl.resendCostToBlue" value="No" styleId="resendCostToBlueNo" name="fclBlForm"/>No
            </td>
        </c:if>
    </tr>
</table>
<c:if test="${toggleCostTable eq false}">
<div style="display:block" id="accrualsTables">
    <table  border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" width="100%">
        <tr class="tableHeadingNew" >
            <td>
                <%
              if (userid != null && hasUserLevelAccess) {%>
                <img src="${path}/img/icons/up.gif" border="0" onclick="showCostCodes()" id="costExpandImage"/>
                <%}%>
                <span style="padding-left:10px;">FCL Costs/Accruals</span>
            </td>
        </tr>
        <tr><%-- ******  EXPAND TABLE FOR COSTS******* --%>
            <td colspan="2">
                <table border="0" cellpadding="0" cellspacing="0" >
                    <% int j = 0;%>
                    <display:table  name="<%=addcostcodeslist%>"
                    class="displaytagstyle" pagesize="<%=pageSize%>" id="expandcosttable"  style="display:none">
                        <display:setProperty name="basic.msg.empty_list">
                            <span style="display:none;" class="pagebanner" />
                        </display:setProperty>
                        <display:setProperty name="paging.banner.placement"><span style="display:none;"></span></display:setProperty>

                        <%
                            String costCodeDesc = "";
                            String amount = "0.00";
                            String account = "";
                            String accountName = "";
                            String accountSubName = "";
                            String costCode = "";
                            String datePaid = "";
                            String comments = "";
                            String costComments = "";
                            String codeid = "";
                            String costcurrency = "USD";
                            String bookingFlag = "";
                            String readOnlyFlag = "";
                            String manifestFlag = "";
                            String transactionType = "";
                            String invoiceNo = "";

                            if (addcostcodeslist != null && addcostcodeslist.size() > 0) {
                                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) addcostcodeslist.get(j);
                                if (fclBlCostCodes.getCodeId() != null) {
                                    codeid = fclBlCostCodes.getCodeId().toString();
                                } else {
                                    codeid = "";
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostCode() != null) {
                                    costCode = fclBlCostCodes.getCostCode();
                                    if (costCode != null && costCode.trim().equals(FclBlConstants.FAECODE)) {
                                        if (fclBlCostCodes.getAmount() != null) {
                                            // chnaged from adming cost to FAE  now adminCostCode=FAE Cost
                                            adminCostCode = fclBlCostCodes.getAmount().toString();
                                        }
                                    }

                                    if (costCode != null && costCode.trim().equals(FclBlConstants.FAECODE)) {
                                        fAEChargeCode = costCode;
                                    }
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostCodeDesc() != null) {
                                    costCodeDesc = fclBlCostCodes.getCostCodeDesc();
                                    if (costCodeDesc != null && costCodeDesc.equalsIgnoreCase(FclBlConstants.FFCODEDESC)) {
                                        fFChargeCode = costCodeDesc;
                                    }
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAmount() != null) {
                                    amount = numberFormat.format(fclBlCostCodes.getAmount());
                                    //totCost=totCost+fclBlCostCodes.getAmount();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAccNo() != null) {
                                    account = fclBlCostCodes.getAccNo();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAccName() != null) {
                                    accountName = fclBlCostCodes.getAccName();
                                    accountSubName = (accountName.length() > 5) ? accountName.substring(0, 5) : accountName;
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getDatePaid() != null) {
                                    datePaid = sdf.format(fclBlCostCodes.getDatePaid());
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getInvoiceNumber() != null) {
                                    invoiceNo = fclBlCostCodes.getInvoiceNumber();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getComments() != null) {
                                    comments = fclBlCostCodes.getComments();
                                    StringBuilder s = new StringBuilder();
                                    if (comments != null) {
                                        int index = 0;
                                        char[] commentArray = comments.toCharArray();
                                        for (int k = 0; k < commentArray.length; k++) {
                                            if (commentArray[k] == '\n') {
                                                s.append(comments.substring(index, k).trim());
                                                s.append(" ");
                                                index = k + 1;
                                            }
                                        }
                                        s.append(comments.substring(index, comments.length()).trim());

                                    }
                                    s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                    s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                    comments = s.toString();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostComments() != null) {
                                    costComments = fclBlCostCodes.getCostComments();
                                    StringBuilder s = new StringBuilder();
                                    if (costComments != null) {
                                        int index = 0;
                                        char[] commentArray = costComments.toCharArray();
                                        for (int k = 0; k < commentArray.length; k++) {
                                            if (commentArray[k] == '\n') {
                                                s.append(costComments.substring(index, k).trim());
                                                s.append(" ");
                                                index = k + 1;
                                            }
                                        }
                                        s.append(costComments.substring(index, costComments.length()).trim());

                                    }
                                    s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                    s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                    costComments = s.toString();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCurrencyCode() != null) {
                                    costcurrency = fclBlCostCodes.getCurrencyCode();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getReadyToPost() != null) {
                                    manifestFlag = fclBlCostCodes.getReadyToPost();
                                }

                                if (null != fclBlCostCodes && null != fclBlCostCodes.getReadOnlyFlag()) {
                                    readOnlyFlag = fclBlCostCodes.getReadOnlyFlag();
                                }
                                if (null != fclBlCostCodes && null != fclBlCostCodes.getTransactionType()) {
                                    transactionType = fclBlCostCodes.getTransactionType();
                                }
                                if (null != fclBlCostCodes && null != fclBlCostCodes.getBookingFlag()) {
                                    bookingFlag = fclBlCostCodes.getBookingFlag();
                                } else {
                                    bookingFlag = "";
                                }
                            }
                        %>
                        <%if (bookingFlag.equalsIgnoreCase("new") || bookingFlag.equalsIgnoreCase("PP")) {%>
                        <display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
                        <%} else if (readOnlyFlag.equalsIgnoreCase("on")) {%>
                        <display:column style="visibility:hidden;width:8px;"></display:column>
                        <%} else {%>
                        <display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
                        <%}%>
                        <display:column title="Cost Code" style="width:150px">
                            <input type="text" class="BackgrndColorForTextBox" name="costCode<%=j%>"
                                   id="costCode<%=j%>" value="<%=costCode%>" onkeydown="getCostCode(this.value,<%=j%>)" size="4" readonly="readonly"/>
                        </display:column>
                        <display:column title="" >
                            <input size="2" class="BackgrndColorForTextBox" name="<%="accrualsExpand" + codeid%>" 
                                   readonly="true" value="<%=transactionType%>" />
                        </display:column>
                        <display:column title="Cost Code Desc" style="width:350px">
                            <input type="text" class="BackgrndColorForTextBox" name="costCodeDesc<%=j%>" readonly="true"
                                   id="costCodeDesc<%=j%>" value="<%=costCodeDesc%>"
                                   onkeydown="getCostCodeDesc(this.value,<%=j%>)"  size="15"/>

                        </display:column>
                        <display:column title="Vendor Name" style="width:150px">
                            <span onmouseover="tooltip.show('<strong><%=accountName%></strong>',null,event);"
                                  onmouseout="tooltip.hide();">
                                <input type="text" class="BackgrndColorForTextBox" name="accountName<%=j%>" id="accountname<%=j%>"
                                       onkeydown="getVesselName(this.value)"  value="<%=accountSubName%>"
                                       size="5" readonly="true"/>
                            </span>
                        </display:column>
                        <display:column title="Vendor Account" style="width:130px">
                            <input type="text" class="BackgrndColorForTextBox" name="accountNo<%=j%>" id="accountno<%=j%>"
                                   value="<%=account%>"  onkeydown="getVesselName(this.value)" size="10" readonly="true"/>
                        </display:column>
                        <display:column title="Invoice Number" style="width:100px">
                            <input type="text" class="BackgrndColorForTextBox" name="invoiceNumber<%=j%>"
                                   value="<%=invoiceNo%>"  readonly="true" tabindex="-1" />
                        </display:column>
	  <display:column title="Amount" style="width:250px">
                            <input type=text" class="BackgrndColorForTextBox" name="costAmount"  value="<%=amount%>" size="4"
                                   readonly="true" />
                        </display:column>
                        <display:column title="Currency" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="costCurrency" value="<%=costcurrency%>"
                                   readonly="true" size="5" >
                        </display:column>
                        <display:column title="Date Paid" style="width:100px">
                            <input type="text" class="BackgrndColorForTextBox"  name="datePaid"  readonly="true"
                                   id="txtItemcreatedon1<%=j%>" size="10"  value="<%=datePaid%>" />
                        </display:column>
                        <display:column title="Cheque No" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="chequeNumber" value="" disabled="true"
                                   size="5">
                        </display:column>
                        <display:column style="visibility:hidden;">
                            <input type="hidden" name="codeid<%=j%>" value="<%=codeid%>">
                        </display:column>
                        <%j++;%>
                    </display:table>
                </table></td>
        </tr>

        <tr><%-- ******  COLLAPSE TABLE FOR COSTS******* --%>
            <td colspan="2">
                <table border="0" cellpadding="0" cellspacing="0"  >
                    <% j = 0;%>

                    <display:table  name="<%=collapseCostList%>" class="displaytagstyle" pagesize="<%=pageSize%>"
                                    id="collapseCosttable" style="display:block">
                        <display:setProperty name="basic.msg.empty_list">
                            <span style="display:none;" class="pagebanner" />
                        </display:setProperty>
                        <display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
                        <%
                            String costCodeDesc = "";
                            String amount = "0.00";
                            String account = "";
                            String accountName = "";
                            String accountSubName = "";
                            String costCode = "";
                            String datePaid = "";
                            String comments = "";
                            String costComments = "";
                            String codeid = "";
                            String costcurrency = "USD";
                            String bookingFlag = "";
                            String readOnlyFlag = "";
                            String manifestFlag = "";
                            String invoiceNo = "";
                            String transactionType = "";
                            String chargCodeDis = "";

                            if (collapseCostList != null && collapseCostList.size() > 0) {
                                FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) collapseCostList.get(j);
                                if (fclBlCostCodes.getCodeId() != null) {
                                    codeid = fclBlCostCodes.getCodeId().toString();
                                } else {
                                    codeid = "";
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostCode() != null) {
                                    costCode = fclBlCostCodes.getCostCode();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostCodeDesc() != null) {
                                    costCodeDesc = fclBlCostCodes.getCostCodeDesc();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAmount() != null) {
                                    amount = numberFormat.format(fclBlCostCodes.getAmount());
                                    totalCostOfCollapse = totalCostOfCollapse + fclBlCostCodes.getAmount();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAccNo() != null) {
                                    account = fclBlCostCodes.getAccNo();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getAccName() != null) {
                                    accountName = fclBlCostCodes.getAccName();
                                    accountSubName = (accountName.length() > 5) ? accountName.substring(0, 5) : accountName;
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getDatePaid() != null) {
                                    datePaid = sdf.format(fclBlCostCodes.getDatePaid());
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getInvoiceNumber() != null) {
                                    invoiceNo = fclBlCostCodes.getInvoiceNumber();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getComments() != null) {
                                    comments = fclBlCostCodes.getComments();
                                    StringBuilder s = new StringBuilder();
                                    if (comments != null) {
                                        int index = 0;
                                        char[] commentArray = comments.toCharArray();
                                        for (int k = 0; k < commentArray.length; k++) {
                                            if (commentArray[k] == '\n') {
                                                s.append(comments.substring(index, k).trim());
                                                s.append(" ");
                                                index = k + 1;
                                            }
                                        }
                                        s.append(comments.substring(index, comments.length()).trim());
                                    }
                                    s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                    s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                    comments = s.toString();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCostComments() != null) {
                                    costComments = fclBlCostCodes.getCostComments();
                                    StringBuilder s = new StringBuilder();
                                    if (costComments != null) {
                                        int index = 0;
                                        char[] commentArray = costComments.toCharArray();
                                        for (int k = 0; k < commentArray.length; k++) {
                                            if (commentArray[k] == '\n') {
                                                s.append(costComments.substring(index, k).trim());
                                                s.append(" ");
                                                index = k + 1;
                                            }
                                        }
                                        s.append(costComments.substring(index, costComments.length()).trim());
                                    }
                                    s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
                                    s = new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
                                    costComments = s.toString();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getCurrencyCode() != null) {
                                    costcurrency = fclBlCostCodes.getCurrencyCode();
                                }
                                if (fclBlCostCodes != null && fclBlCostCodes.getReadyToPost() != null) {
                                    manifestFlag = fclBlCostCodes.getReadyToPost();
                                }
                                if (null != fclBlCostCodes && null != fclBlCostCodes.getReadOnlyFlag()) {
                                    readOnlyFlag = fclBlCostCodes.getReadOnlyFlag();
                                }
                                if (null != fclBlCostCodes && null != fclBlCostCodes.getTransactionType()) {
                                    transactionType = fclBlCostCodes.getTransactionType();
                                }
                                if (null != fclBlCostCodes && null != fclBlCostCodes.getBookingFlag()) {
                                    bookingFlag = fclBlCostCodes.getBookingFlag();
                                } else {
                                    bookingFlag = "";
                                }
                                }
                        %>
                        <%--<%if(costReadyToPost.equals("M")|| readOnlyFlag.equalsIgnoreCase("on")){ %>--%>
                        <%if (bookingFlag.equalsIgnoreCase("new") || bookingFlag.equalsIgnoreCase("PP")) {%>
                        <display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
                        <%} else if (readOnlyFlag.equalsIgnoreCase("on")) {%>
                        <display:column style="visibility:hidden;width:8px;"></display:column>
                        <%} else {%>
                        <display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
                        <%}%>
                        <display:column title="Cost Code" style="width:150px">
                            <input type="text" class="BackgrndColorForTextBox" name="costCode<%=j%>"
                                   id="costCode<%=j%>" value="<%=costCode%>" onkeydown="getCostCode(this.value,<%=j%>)" size="4" readonly="readonly" tabindex="-1" />
                            <dojo:autoComplete formId="fclBillLaddingform" textboxId="costCode<%=j%>"
                                               action="${path}/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=3&index=<%=j%>"/>
                        </display:column>

                        <display:column title="" >
                            <input size="2" class="BackgrndColorForTextBox"  name="<%="accrualsCollaps" + codeid%>" 
                                   readonly="true" value="<%=transactionType%>"/>
                        </display:column>
                        <display:column title="Cost Code Desc" style="width:350px">
                            <input type="text" class="BackgrndColorForTextBox" name="costCodeDesc<%=j%>" readonly="true"
                                   id="costCodeDesc<%=j%>" value="<%=costCodeDesc%>"
                                   onkeydown="getCostCodeDesc(this.value,<%=j%>)"  size="15" tabindex="-1" />
                            <dojo:autoComplete formId="fclBillLaddingform" textboxId="costCodeDesc<%=j%>"
                                               action="${path}/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=4&index=<%=j%>" />
                        </display:column>
                        <display:column title="Vendor Name" style="width:150px">
                            <span onmouseover="tooltip.show('<strong><%=accountName%></strong>',null,event);"
                                  onmouseout="tooltip.hide();">
                                <input type="text" class="BackgrndColorForTextBox" name="accountName<%=j%>" id="accountname<%=j%>"
                                       onkeydown="getVesselName(this.value)"
                                       size="30"  value="<%=accountName%>" readonly="true" tabindex="-1" />
                                <dojo:autoComplete formId="fclBillLaddingform"  textboxId="accountname<%=j%>"
                                                   action="${path}/actions/getCustomerName.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=j%>"/>
                            </span>
                        </display:column>
                        <display:column title="Vendor Account" style="width:130px">
                            <input type="text" class="BackgrndColorForTextBox" name="accountNo<%=j%>" id="accountno<%=j%>"
                                   value="<%=account%>"  onkeydown="getVesselName(this.value)" size="10" readonly="true" tabindex="-1" />
                            <dojo:autoComplete formId="fclBillLaddingform" textboxId="accountno<%=j%>"
                                               action="${path}/actions/getCustomer.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=j%>"/>
                        </display:column>
                        <display:column title="Invoice Number" style="width:100px">
                            <input type="text" class="BackgrndColorForTextBox" name="invoiceNumber<%=j%>"
                                   value="<%=invoiceNo%>"  readonly="true" tabindex="-1" />
                        </display:column>
                        <display:column title="Amount" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="costAmount"  value="<%=amount%>" size="4"
                                   readonly="true" tabindex="-1">
                        </display:column>
                        <display:column title="Currency" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="costCurrency" value="<%=costcurrency%>"
                                   readonly="true" size="5" tabindex="-1">
                        </display:column>
                        <display:column title="Date Paid" style="width:100px">
                            <input type="text" class="BackgrndColorForTextBox"  name="datePaid"  readonly="true"
                                   id="txtItemcreatedon1<%=j%>" size="10"  value="<%=datePaid%>" tabindex="-1" />
                        </display:column>
                        <display:column title="Cheque No" style="width:100px">
                            <input type=text" class="BackgrndColorForTextBox" name="chequeNumber" value="" disabled="true"
                                   size="5" tabindex="-1" >
                        </display:column>
                        <display:column title="Actions" style="width:100px">
                            <%if (!costCode.equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
                      && !costCode.equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE) && !"AP".equals(transactionType)
                      && !"IP".equals(transactionType) && !"PN".equalsIgnoreCase(transactionType)) {%>
                            <span class="hotspot">
                                <img src="${path}/img/icons/edit.gif" border="0" onclick="editCosts('<%=codeid%>','<%=amount%>','<%=bookingFlag%>')"
                                     id="<%="editCollaps" + codeid%>" name="<%=j%>"  onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/></span>

                            <%}
                     if (null != costComments && !costComments.trim().equals("")) {%>
                            <span class="hotspot" style="color:black;">
                                <img id="viewgif4"  src="${path}/img/icons/view.gif"  onmouseover="tooltip.showComments('<strong><%=costComments%></strong>',100,event);"
                                     onmouseout="tooltip.hideComments();"/></span>

                            <%}
                                if (!"AP".equals(transactionType)
                                        && !"IP".equals(transactionType) && !"DS".equals(transactionType) && !"PN".equalsIgnoreCase(transactionType)) {
                            %>
                            <c:if test="${roleDuty.deleteCostandCharges}">
                            <span  class="hotspot">
                                <img src="${path}/img/icons/delete.gif" border="0" onclick="deleteCostDetails('<%=codeid%>')"
                                     id="<%="deleteCollaps" + codeid%>"  name="<%=j%>"  onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                            </c:if>

                            <%}%>
                            <c:if test="${fclBlForm.fclBl.readyToPost=='M'}">
                                <span class="hotspot"><img src="${path}/img/icons/refresh.gif" border="0" onclick="resendcost('<%=codeid%>')"
                            id="<%="resendManifestCollaps" + codeid%>"  name="<%=j%>"  onmouseover="tooltip.show('<strong>Resend</strong>',null,event);" onmouseout="tooltip.hide();"/></span>
                            </c:if>
                        </display:column>
                        <display:column style="visibility:hidden;">
                            <input type="hidden" name="codeid<%=j%>" value="<%=codeid%>">
                        </display:column>
                        <%j++;%>
                    </display:table>

                </table></td>
        </tr>
    </table>
</div>
</c:if>
<c:if test="${fclBlForm.fclBl.importFlag ne 'I'}">
    <div id="totalCostForDown" style="float: left;padding-left: 500px;display: none">
        <td>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color:green; font-size: 13px; font-weight: bold">Total costs for  &nbsp ${sslBlPrepaidCollectName} :</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="color:red; font-size: 13px; font-weight: bold">${totalCostForSSL}</span>
        </td>
    </div>
</c:if>
<input type="hidden" name="differenceOfCostAndCharges" />

<input type="hidden" name="totalOfCost" id="totalOfCost" value="<%=numberFormat.format(totCost)%>">
<input type="hidden" name="profitGained" id="profitGained" value="<%=numberFormat.format(profit)%>">
<input type="hidden" id="faeOrIncent" value="${faeOrIncent}">

 
 
</body>
<script type="text/javascript">
    showHideCharges();
    if(toggleCostTable=='false'){
        togglePreviewAccruals();
    }


  function ImportsJToolTip(selector, message, width, currentPosition) {
        jQuery(selector).qtip({
                content: message,
                style: {
                    width: width,
                    tip: {
                        corner: currentPosition,
                        color: '#8DB7D6'
                    },
                    border: {
                        width: 1,
                        radius: 3,
                        color: '#8DB7D6'
                    }
                },
                position: {
                    corner: {
                        target: 'bottomMiddle',
                        tooltip: currentPosition
                    }
                },
                show: 'mouseover',
                hide: 'mouseout'
            });
        }
</script>
</html>


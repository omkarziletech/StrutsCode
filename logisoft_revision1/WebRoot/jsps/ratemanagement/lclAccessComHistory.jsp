<%@ page language="java" import="com.gp.cong.logisoft.domain.LCLColoadStandardCharges,
         com.gp.cong.logisoft.util.DBUtil,java.util.*,
         com.gp.cong.logisoft.domain.LCLColoadStandardChargesHistory,
         com.gp.cong.logisoft.domain.LCLColoadCommodityChargesHistory,
         com.gp.cong.logisoft.domain.LCLColoadMaster,
         java.text.DateFormat,java.text.SimpleDateFormat, com.gp.cong.logisoft.hibernate.dao.LCLColoadStandardChargesHistoryDAO"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        DBUtil dbUtil = new DBUtil();
        List standardHistoryList = new ArrayList();
        LCLColoadMaster lCLColoadMaster = new LCLColoadMaster();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DecimalFormat df = new DecimalFormat("0.00");
        String chargeCode = "";
        String chargeDesc = "";
        String chargeType = "";
        Date effective_Date = new Date();
        String effDate = null;
        String type = null;
        String cheDate = "";
        int chargecode = 0;

        if (session.getAttribute("addftfMaster") != null) {

            lCLColoadMaster = (LCLColoadMaster) session.getAttribute("addftfMaster");


        }
        LCLColoadStandardChargesHistoryDAO lCLColoadStandardChargesHistoryDAO = new LCLColoadStandardChargesHistoryDAO();
        if (session.getAttribute("coStandardCharges") != null) {
            LCLColoadStandardCharges lCLColoadStandardCharges = (LCLColoadStandardCharges) session.getAttribute("coStandardCharges");
            if (lCLColoadStandardCharges.getChargeCode() != null) {
                chargecode = lCLColoadStandardCharges.getChargeCode().getId();

            }

            standardHistoryList = dbUtil.getAllCoStandardHistory(lCLColoadStandardCharges.getLclCoLoadId(), chargecode);

        }
//List csssListHistory = new ArrayList();
//csssListHistory=dbUtil.getAllCoLoadCommodityHistory(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort());
        String defaultRate = "";
        if (session.getAttribute("lcldefaultRate") != null) {
            defaultRate = (String) session.getAttribute("lcldefaultRate");
        }

        int k = 0;
%>
<html> 
    <head>
        <title>JSP for AFRHForm form</title>
        <%@include file="../includes/baseResources.jsp" %>

    </head>
    <body class="whitebackgrnd">
        <%--		<table width=100% border="0" cellpadding="0" cellspacing="0">--%>
<%--			<tr class="textlabels">--%>
<%--  				 <td height="12" colspan="5" width=20 class="headerbluesmall">&nbsp;List of &nbsp;Accessorial General Standard Charges History </td> --%>
<%--			</tr>--%>
        <%--			</table>--%>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew" height="90%">List of &nbsp;Accessorial General Standard Charges History</tr>
            <td>
                <table width="100%">
                    <tr>
                        <td align="left" ><div id="divtablesty1" class="scrolldisplaytable">
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                    <%
        int i = 0;

                                    %>
                                    <display:table name="<%=standardHistoryList%>" pagesize="<%=pageSize%>"   defaultorder="descending" defaultsort="1" class="displaytagstyle" id="airratestable" sort="list" >
                                        <display:setProperty name="paging.banner.some_items_found">
                                            <span class="pagebanner">
                                                <font color="blue">{0}</font> Item Displayed,For more Data click on Page Numbers.
                                                <br>
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                            <span class="pagebanner">
                                                One {0} displayed. Page Number
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.all_items_found">
                                            <span class="pagebanner">
                                                {0} {1} Displayed, Page Number
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="basic.msg.empty_list">
                                            <span class="pagebanner">
                                                No Records Found.
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.placement" value="bottom" />
                                        <display:setProperty name="paging.banner.item_name" value="Relay"/>
                                        <display:setProperty name="paging.banner.items_name" value="Relays"/>
                                        <%


        Date change_date = null;
        String insuranceRate = "";
        String insuranceAmt = "";
        String amtPerCft = "";
        String amtPer100lbs = "";
        String amtPerCbm = "";
        String amtPer1000kg = "";
        String percentage = "";
        String minAmt = "";
        String amount = "";

        if (standardHistoryList != null && standardHistoryList.size() > 0) {
            LCLColoadStandardChargesHistory lCLColoadStandardChargesHistory = (LCLColoadStandardChargesHistory) standardHistoryList.get(i);
            if (lCLColoadStandardChargesHistory.getChargeCode() != null) {
                chargeCode = lCLColoadStandardChargesHistory.getChargeCode().getCode();
                chargeDesc = lCLColoadStandardChargesHistory.getChargeCode().getCodedesc();
            }

            if (lCLColoadStandardChargesHistory.getChargeType() != null) {
                chargeType = lCLColoadStandardChargesHistory.getChargeType().getCodedesc();

            }
            if (lCLColoadStandardChargesHistory.getEffectiveDate() != null) {
                effDate = dateFormat.format(lCLColoadStandardChargesHistory.getEffectiveDate());

            //---------------------------------------------------
            }
            if (lCLColoadStandardChargesHistory.getChangedDate() != null) {
                cheDate = dateFormat.format(lCLColoadStandardChargesHistory.getChangedDate());


            }

            if (lCLColoadStandardChargesHistory.getInsuranceRate() != null) {
                insuranceRate = df.format(lCLColoadStandardChargesHistory.getInsuranceRate());
            }
            if (lCLColoadStandardChargesHistory.getInsuranceAmt() != null) {
                insuranceAmt = df.format(lCLColoadStandardChargesHistory.getInsuranceAmt());
            }
            if (lCLColoadStandardChargesHistory.getAmtPerCft() != null) {
                amtPerCft = df.format(lCLColoadStandardChargesHistory.getAmtPerCft());
            }
            if (lCLColoadStandardChargesHistory.getAmtPer100lbs() != null) {
                amtPer100lbs = df.format(lCLColoadStandardChargesHistory.getAmtPer100lbs());
            }
            if (lCLColoadStandardChargesHistory.getAmtPerCbm() != null) {
                amtPerCbm = df.format(lCLColoadStandardChargesHistory.getAmtPerCbm());
            }
            if (lCLColoadStandardChargesHistory.getAmtPer1000Kg() != null) {
                amtPer1000kg = df.format(lCLColoadStandardChargesHistory.getAmtPer1000Kg());
            }
            if (lCLColoadStandardChargesHistory.getPercentage() != null) {
                percentage = df.format(lCLColoadStandardChargesHistory.getPercentage()) + "%";
            }
            if (lCLColoadStandardChargesHistory.getMinAmt() != null) {
                minAmt = df.format(lCLColoadStandardChargesHistory.getMinAmt());
            }
            if (lCLColoadStandardChargesHistory.getAmount() != null) {
                amount = df.format(lCLColoadStandardChargesHistory.getAmount());
            }
        }

                                        %>
                                        <display:column property="id" title="" style="width:2%;visibility:hidden;color:red;"/>
                                        <display:column  title="&nbsp; Chrg_Code"><%=chargeCode%></display:column>
                                        <display:column  title="&nbsp; Chrg_Type"  ><%=chargeType%></display:column>
                                        <display:column property="standard" title="&nbsp; Std"></display:column>

                                        <display:column property="asFrfgted" title="&nbsp; As_Freighted"></display:column>
                                        <display:column  title="&nbsp; Insu_Rate"> <%=insuranceRate%> </display:column>
                                        <display:column  title="&nbsp; Insu_Amount"> <%=insuranceAmt%> </display:column>
                                        <%if (defaultRate.equals("E")) {%>
                                        <display:column  title=" &nbsp; AmtPerCft" > <%=amtPerCft%> </display:column>
                                        <display:column  title="&nbsp; AmtPer100lbs" > <%=amtPer100lbs%> </display:column>
                                        <%} else if (defaultRate.equals("M")) {%>
                                        <display:column  title="&nbsp; AmtPerCbm" > <%=amtPerCbm%> </display:column>
                                        <display:column  title="&nbsp; AmtPer1000kg"> <%=amtPer1000kg%> </display:column>
                                        <%}%>
                                        <display:column  title="&nbsp; Percentage" > <%=percentage%> </display:column>
                                        <display:column  title="&nbsp; Min_Amt" > <%=minAmt%> </display:column>


                                        <display:column  title="&nbsp; Amt"> <%=amount%> </display:column>

                                        <display:column title="&nbsp; Effec_Date" ><%=effDate%></display:column>

                                        <display:column title="Chnged Date" ><%=cheDate%></display:column>
                                        <display:column property="whoChanged" title="Who Chnged" />


                                        <%i++;%>
                                    </display:table>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>

            </td>
        </table>

    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
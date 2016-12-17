
<%@ page language="java"  import="java.util.*,com.gp.cong.logisoft.domain.FclContainertypeCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<jsp:useBean id="fclrecordform" class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm" scope="request"/>   
<%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        FclBuy fclBuy = new FclBuy();
        FclBuyCost fclBuycost = new FclBuyCost();
        String terminalNumber = "";
        String terminalName = "";
        String destSheduleNumber = "";
        String destAirportname = "";
        String comCode = "";
        String comDesc = "";
        String message = "";
        String enddate = "";
        String modify = "";
        String sslineno = "";
        String contact = "";
        String costdesc = "";
        String costtype = "";
        String code = "";
        String startdate = "";
        if (request.getAttribute("start") != null) {
            startdate = (String) request.getAttribute("start");

        }
        if (request.getAttribute("end") != null) {
            enddate = (String) request.getAttribute("end");

        }
        if (request.getAttribute("message") != null) {
            message = (String) request.getAttribute("message");

        }
        List weightRangeList = new ArrayList();        
        String sslinename = "";
        List recordsFrightList = new ArrayList();        
        List costcodelist = new ArrayList();        
        List costtypelist = new ArrayList();        
        List recordsList = new ArrayList();
        DBUtil dbUtil = new DBUtil();

        String editPath = path + "/addFCLRecords.do";
        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
        fclrecordform.setStandard("off");
        if (session.getAttribute("addfclrecords") != null) {
            fclBuy = (FclBuy) session.getAttribute("addfclrecords");
            if (fclBuy.getOriginTerminal() != null) {
                terminalNumber = fclBuy.getOriginTerminal().getUnLocationCode();
                terminalName = fclBuy.getOriginTerminal().getUnLocationName();
            }
            if (fclBuy.getDestinationPort() != null) {
                destSheduleNumber = fclBuy.getDestinationPort().getUnLocationCode();
                destAirportname = fclBuy.getDestinationPort().getUnLocationName();
            }
            if (fclBuy.getComNum() != null) {
                comCode = fclBuy.getComNum().getCode();
                comDesc = fclBuy.getComNum().getCodedesc();
            }
            if (fclBuy.getSslineNo() != null) {
                sslineno = fclBuy.getSslineNo().getAccountNo();
                sslinename = fclBuy.getSslineNo().getAccountName();
            }
        }
        if (fclBuy.getEndDate() != null) {
        }
        List unitList = new ArrayList();
        FclBuyCost fclBuyCost = new FclBuyCost();
        if (session.getAttribute("costCodeList") != null) {
            recordsList = (List) session.getAttribute("costCodeList");
            for (int i = 0; i < recordsList.size(); i++) {
                fclBuyCost = (FclBuyCost) recordsList.get(i);

                if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                    Iterator iter = fclBuyCost.getFclBuyUnitTypesSet().iterator();

                    while (iter.hasNext()) {
                        fclBuyCostTypeRates = (FclBuyCostTypeRates) iter.next();
                        unitList.add(fclBuyCostTypeRates);
                    }
                }
                if (fclBuyCost.getFclBuyAirFreightSet() != null) {
                    Iterator iter = fclBuyCost.getFclBuyAirFreightSet().iterator();
                    while (iter.hasNext()) {
                        FclBuyAirFreightCharges fclBuyAirFreightCharges = (FclBuyAirFreightCharges) iter.next();
                        recordsFrightList.add(fclBuyAirFreightCharges);
                    }
                }
            }
        }

        if (costcodelist != null) {
            costcodelist = dbUtil.getGenericCodeCostList(new Integer(36), "no", "Select Cost Code");
            request.setAttribute("costcodelist", costcodelist);

        }
        if (costtypelist != null) {
            costtypelist = dbUtil.getGenericCodetypeList(new Integer(37), "yes", "Select Cost type");
            request.setAttribute("costtypelist", costtypelist);

        }
        String selectcode = "0";

        if (session.getAttribute("costcode") != null) {

            fclBuycost = (FclBuyCost) session.getAttribute("costcode");


            if (fclBuycost != null && fclBuycost.getCostId() != null) {
                costdesc = fclBuycost.getCostId().getCodedesc();

                code = fclBuycost.getCostId().getCode().toString();
                if (fclBuycost.getContType() != null) {
                    selectcode = fclBuycost.getContType().getId().toString();

                }
            }
            if (fclBuycost.getContType() != null) {
                costtype = fclBuycost.getContType().getId().toString();

            }
        }


        if (request.getParameter("modify") != null) {

            modify = (String) request.getParameter("modify");

            session.setAttribute("modifyforfclbuyrates", modify);
        } else {
            modify = (String) session.getAttribute("modifyforfclbuyrates");
        }
        if (session.getAttribute("message") != null) {
        }
        if (request.getAttribute("editflaterate") != null && !request.getAttribute("editflaterate").equals("")) {
            int ii = Integer.parseInt((String) request.getAttribute("editflaterate"));

%>
<script type="text/javascript">
    mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+<%=ii%>,"","width=800,height=200");
    mywindow.moveTo(200,180);
</script>
<%
        }
%>

<html>
    <head>
        <title>JSP for AddLclColoadCommodityForm form</title>

        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" type="text/javascript">

            function submit1()
            {

                document.addFCLRecordsForm.buttonValue.value="costcode";
                document.addFCLRecordsForm.submit();
            }
            function costtypechange()
            {
                if(document.addFCLRecordsForm.costcode.value=="0"){
                    alert("Please select cost code !");
                }
                document.addFCLRecordsForm.buttonValue.value="costType";
                document.addFCLRecordsForm.submit();
            }

            function addForm()
            {

                if(document.addFCLRecordsForm.costcode.value=="0"){
                    alert("Please select cost code !");
                }

                else if(document.addFCLRecordsForm.costtype.value=="0"){
                    alert("Please select cost code !");
                }
                else{
                    document.addFCLRecordsForm.buttonValue.value="add";
                    document.addFCLRecordsForm.submit();
                }
            }
            function saveForm(val){            
                document.addFCLRecordsForm.buttonValue.value="save";
                document.addFCLRecordsForm.submit();
            }
            function cancelForm(){
                document.addFCLRecordsForm.buttonValue.value="cancel";
                document.addFCLRecordsForm.submit();
            }
        </script>
    </head>

    <body class="whitebackgrnd">
        <html:form   action="/addFCLRecords" scope="request">
            <font color="blue" size="4"><%=message%></font>
            <table width="815" border="0" cellpadding="0" cellspacing="0" id="records">

                <tr class="textlabels">
                    <td width="540" align="left" class="headerbluelarge">Buy Rates by Container <a href="AirRates.html" target="_parent" ></a></td>
                    <td width="78" align="left" class="headerbluelarge"><img src="<%=path%>/img/save.gif" onclick="saveForm()"/></td>
                    <td  align="left" class="headerbluelarge"><img src="<%=path%>/img/cancel.gif"" border="0
                                                                   " onclick="cancelForm()"/></td>
                    <td><img src="<%=path%>/img/delete.gif" border="0" onclick="delete1()" id="delete"/></td>
                    <td align="right"><img src="<%=path%>/img/note.gif" id="note"	onclick="confirmnote()" />	</td>
                </tr>
                <tr styleClass="textlabels">

                    <td colspan="3" align="left" class="headerbluelarge">&nbsp;</td>
                </tr>
                <tr>
                <td colspan="3"></td></tr>
                <tr>
                    <td height="12" colspan="3"  class="headerbluesmall">&nbsp;&nbsp;Buy Rates </td>
                </tr>
            </table>
            <table width="814"  border="0" cellpadding="0" cellspacing="0">

                <tr class="textlabels">
                    <td height="15" valign="bottom">&nbsp;</td>
                    <td colspan="2" valign="bottom">&nbsp;</td>
                    <td colspan="2" valign="bottom">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td valign="bottom">&nbsp;</td>
                    <td valign="bottom">

                        <div align="right"></div>
                    </td>

                    <td width="67" valign="bottom">&nbsp;</td>
                    <td width="30" valign="bottom">&nbsp;</td>
                    <td width="42" valign="bottom">&nbsp;</td>
                    <td width="42" valign="bottom">&nbsp;</td>
                </tr >
                <tr class="textlabels">
                    <td  valign="bottom">OriginTerminal</td>
                    <td  valign="bottom"> OriginTerminal Name </td>

                    <td valign="bottom">Dest Port</td>
                    <td>&nbsp;&nbsp;&nbsp;Dest Port Name </td>
                    <td valign="bottom">Com Code </td>
                    <td valign="bottom">Com Description</td>
                    <td valign="bottom">SS Line No</td>
                    <td valign="bottom">SS Line Name</td>
                    <td  valign="bottom">Contract NO</td>

                </tr >
                <tr class="textlabels">
                    <td ><html:text property="orgTerminal" styleClass="shortstylegrey" disabled="disabled" value="<%= terminalNumber%>"></html:text>
                    </td>
                    <td ><html:text property="orgName"  styleClass="bigstylegrey"  readonly="" value="<%=terminalName %>"/></td>
                    <td ><html:text property="destnum" styleClass="mediumstylegrey"  disabled="disabled" value="<%= destSheduleNumber%>"/>
                    </td>
                    <td>&nbsp;&nbsp;&nbsp;<html:text property="destname" styleClass="bigstylegrey"  readonly="" value="<%= destAirportname%>"/></td>
                    <td>&nbsp;&nbsp;<html:text property="comcode" styleClass="mediumstylegrey" value="<%= comCode%>"/>
                    </td>
                    <td><html:text property="comdesc"  styleClass="bigstylegrey"  value="<%=comDesc %>"/></td>
                    <td valign="bottom">&nbsp;&nbsp;<html:text property="sslineno" styleClass="mediumstylegrey" value="<%= sslineno%>"/>
                    </td>
                    <td valign="bottom"><html:text property="sslinename" styleClass="bigstylegrey" value="<%=sslinename %>"/></td>
                    <td><html:text property="contact" styleClass="bigstylegrey" value="<%=contact %>"/></td>
                </tr >
                <tr class="textlabels">
                    <td height="15" valign="bottom">&nbsp;</td>
                    <td colspan="2" valign="bottom">&nbsp;</td>
                    <td colspan="2" valign="bottom">&nbsp;</td>
                    <td>&nbsp;</td>
                    <td valign="bottom">&nbsp;</td>
                    <td valign="bottom">

                        <div align="right"></div>
                    </td>

                    <td width="67" valign="bottom">&nbsp;</td>
                    <td width="30" valign="bottom">&nbsp;</td>
                    <td width="42" valign="bottom">&nbsp;</td>
                    <td width="42" valign="bottom">&nbsp;</td>
                </tr >
            </table><table>
                <tr class="textlabels">
                    <td width="30" valign="bottom">Start Date</td>
                    <td height="15" valign="bottom">&nbsp;</td>
                    <td width="42" valign="bottom">End Date</td>
                    <td height="15" valign="bottom">&nbsp;</td>

                </tr >

                <tr class="textlabels">

                    <td>
                        <html:text property="startDate" styleClass="verysmalldropdownStyle" styleId="txtitemcreatedon"
                 value="<%=""%>" size="7" readonly="true" value="<%=startdate%>"/>
                    </td>
                    <td>
                        <div>
                            <img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
                                 id="itemcreatedon"
                                 onmousedown="insertDateFromCalendar(this.id,0);" />
                        </div>
                    </td>
                    <td>
                        <html:text property="endDate" styleId="txtEitemcreatedon" styleClass="verysmalldropdownStyle"
                 value="<%=""%>" size="7" readonly="true" value="<%=enddate%>"/>
                    </td>
                    <td>
                        <div>
                            <img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
                                 id="Eitemcreatedon"
                                 onmousedown="insertDateFromCalendar(this.id,0);" />
                        </div>
                    </td>
                </tr >
            </table>

            <table border="0">
                <tr class="textlabels">


                <td  valign="bottom">Cost Code</td>
                <td  valign="bottom">Cost description</td>
                <td  valign="bottom">Cost Type </td>
                <%
        if (costtype != null && costtype.equals("11300")) {
                %>
                <td width="94">Unit Type</td>
                <td width="82">Amount</td>
                <td width="82">Markup</td>
                <td >Standard</td>


                <td valign="bottom">&nbsp;</td>
                <td valign="bottom">&nbsp;</td>

                <%} else if (costtype != null && costtype.equals("11301") || costtype.equals("11302") || costtype.equals("11303") || costtype.equals("11304") || costtype.equals("11314")) {
                %>
                <td width="94">Retail</td>

                <td width="96">CTC</td>
                <td width="94">FTF</td>
                <td width="99">Minimun</td>
                <td colspan="3">&nbsp;</td>

                <%} else if (costtype != null && costtype.equals("11305")) {%>
                <td width="94">Retail</td>

                <%} else if (costtype != null && costtype.equals("11306")) {%>

                <td width="68"><span class="textlabels">Range </span></td>
                <td width="121"><span class="textlabels">Air Freight Amount </span></td>

                <%}%>
                <tr class="textlabels">
                    <% %>
                    <td ><html:select property="costcode" styleClass="verysmalldropdownStyle" onchange="submit1()">
                            <html:optionsCollection name="costcodelist"/>
                    </html:select></td>
                    <td ><html:text property="costdesc"  styleClass="verysmalldropdownStyle"  value="<%=costdesc %>"/></td>
                    <td><html:select property="costtype" styleClass="verysmalldropdownStyle"  onchange="costtypechange()" value="<%=selectcode%>">
                            <html:optionsCollection name="costtypelist"/>
                    </html:select></td>
                    <%
        if (costtype != null && costtype.equals("11300")) {
            if (code != null && code.equals("1")) {

                    %>
                    <td width="60" ><html:select property="unittype" styleClass="shortselectstyle1">
                    <html:optionsCollection name="unittypelist"/>  </html:select> </td>
                    <%} else {

                    %>
                    <td width="60" ><html:select property="unittype" styleClass="shortselectstyle1">
                    <html:optionsCollection name="secondunittypelist"/>  </html:select> </td>
                    <% }%>

                    <td ><html:text property="amount"  onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>

                    <td ><html:text property="markup"  onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"    value=""/></td>
                    <td ><html:checkbox property="standard" name="fclrecordform" ></html:checkbox></td>


                    <%
                    } else if (costtype != null && costtype.equals("11301") || costtype.equals("11302") || costtype.equals("11303") || costtype.equals("11304") || costtype.equals("11314")) {
                    %>
                    <td><html:text property="pcretail"  styleClass="verysmalldropdownStyle" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>
                    <td><html:text property="pcctc"  styleClass="verysmalldropdownStyle" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value="" /></td>
                    <td><html:text property="pcftf" styleClass="verysmalldropdownStyle" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>
                    <td><html:text property="pcminimun"  styleClass="verysmalldropdownStyle" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>
                    <td colspan="3">&nbsp;</td>

                    <%                    } else if (costtype != null && costtype.equals("11305")) {%>


                    <td><html:text property="pdretail"  styleClass="verysmalldropdownStyle" onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>

                    <%                    } else if (costtype != null && costtype.equals("11306")) {
                        if (code != null && code.equals("1")) {

                    %>
                    <td><html:select property="range" styleClass="verysmalldropdownStyle"  onchange="costtypechange()">
                            <html:optionsCollection name="ocenwightlist"/>
                    </html:select></td>
                    <%} else {

                    %>
                    <td><html:select property="range" styleClass="verysmalldropdownStyle"  onchange="costtypechange()">
                            <html:optionsCollection name="bunkerwightlist"/>
                    </html:select></td>
                    <% }
                    %>


                    <td width="105"><html:text property="afamount"  styleClass="verysmalldropdownStyle"  onkeypress="check(this,5)" onblur="checkdec(this)" maxlength="8" size="8"   value=""/></td>
                    <%}%>
                    <td><img src="<%=path%>/img/toolBar_add_hover.gif" alt="add" onclick="addForm()"/></td>
                </tr>

            </table>





            <table width="100%">
                <tr>
                    <td>
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <%
        int i = 0;

                                %>
                                <display:table name="<%=recordsList%>" pagesize="20" class="displaytagstyle"  sort="list" id="includedtable">
                                    <% String tempPath1 = "";
        String unitycode = "";
        String ComCode = "";
        String costcode = "";
        String costType = "";
        String ratAmount = "";
        String ctcAmt = "";
        String ftfAmt = "";
        String minimumAmt = "";
        String activeAmt = "";
        String markUp = "";
        String standard = "";

        String wightRange = "";
        String costCode = "";
        //String costType="";
        String rateAmount = "";
        String iStr = String.valueOf(i);

        List unitlist1 = new ArrayList();
        List airFrieghtList = new ArrayList();
        fclBuyCost = new FclBuyCost();

        if (recordsList != null && recordsList.size() > 0) {

            fclBuyCost = (FclBuyCost) recordsList.get(i);
            if (fclBuyCost.getCostId() != null) {

                code = fclBuyCost.getCostId().getCode();
            }
            if (fclBuyCost.getContType() != null) {
                costType = fclBuyCost.getContType().getCodedesc();
                costtype = fclBuyCost.getContType().getId().toString();
            }
            if (fclBuyCost.getFclBuyUnitTypesSet() != null) {
                FclBuyCostTypeRates fclBuyCostTypeRates1 = null;
                Iterator iter = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                while (iter.hasNext()) {
                    fclBuyCostTypeRates1 = (FclBuyCostTypeRates) iter.next();
                    unitlist1.add(fclBuyCostTypeRates1);
                }
                tempPath1 = editPath + "?index=" + iStr;

            } else if (fclBuyCost.getFclBuyAirFreightSet() != null) {
                FclBuyAirFreightCharges fclBuyAirFreightCharges = null;
                Iterator iter = fclBuyCost.getFclBuyAirFreightSet().iterator();
                while (iter.hasNext()) {
                    fclBuyAirFreightCharges = (FclBuyAirFreightCharges) iter.next();
                    airFrieghtList.add(fclBuyAirFreightCharges);
                }
                tempPath1 = editPath + "?index=" + iStr;


            }
            if (unitlist1 != null && unitlist1.size() > 0) {
                FclBuyCostTypeRates FclBuyCostType = (FclBuyCostTypeRates) unitlist1.get(i);
                if (FclBuyCostType.getUnitType() != null) {
                    unitycode = FclBuyCostType.getUnitType().getCodedesc();
                }
                if (FclBuyCostType.getRatAmount() != null) {
                    ratAmount = FclBuyCostType.getRatAmount().toString();
                }
                if (FclBuyCostType.getFtfAmt() != null) {
                    ftfAmt = FclBuyCostType.getFtfAmt().toString();
                }
                if (FclBuyCostType.getCtcAmt() != null) {
                    ctcAmt = FclBuyCostType.getCtcAmt().toString();
                }
                if (FclBuyCostType.getMinimumAmt() != null) {
                    minimumAmt = FclBuyCostType.getMinimumAmt().toString();
                }
                if (FclBuyCostType.getActiveAmt() != null) {
                    activeAmt = FclBuyCostType.getActiveAmt().toString();
                }
                if (FclBuyCostType.getMarkup() != null) {
                    markUp = FclBuyCostType.getMarkup().toString();
                }
                if (FclBuyCostType.getStandard() != null) {
                    standard = FclBuyCostType.getStandard();
                }
            }
            if (airFrieghtList != null && airFrieghtList.size() > 0) {
                FclBuyAirFreightCharges FclBuyAirFreight = (FclBuyAirFreightCharges) airFrieghtList.get(i);
                if (FclBuyAirFreight.getWieghtRange() != null) {
                    wightRange = FclBuyAirFreight.getWieghtRange().getCodedesc();
                }
                if (FclBuyAirFreight.getRatAmount() != null) {
                    rateAmount = FclBuyAirFreight.getRatAmount().toString();
                }
            }
        }

                                    %>
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Item Details Displayed,For more Items click on Page Numbers.
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
                                    <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
                                            No Records Found.
                                        </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Item"/>
                                    <display:setProperty name="paging.banner.items_name" value="Items"/>
                                    <display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Code" >
                                        <a href="<%=tempPath1%>">
                                    <%=costcode%></a></display:column>

                                    <display:column  title="Cost type" ><%=costType%></display:column>
                                    <display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wieght Range ">

                                        <%=wightRange%>
                                    </display:column>
                                    <display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Air Fright Amount" ><%=rateAmount%></display:column>

                                    <display:column title="Retail"><%=ratAmount%></display:column>
                                    <display:column title="FTFAmt"><%=ftfAmt%></display:column>
                                    <display:column title="CTCAmt" ><%=ctcAmt%></display:column>
                                    <display:column title="MinimumAmt" ><%=minimumAmt%></display:column>


                                    <display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Unit Type">
                                        <%=unitycode%>

                                    </display:column>
                                    <display:column title="Amount"><%=activeAmt%></display:column>
                                    <display:column title="Markup" ><%=markUp%></display:column>
                                    <display:column title="Standard" ><%=standard%></display:column>

                                    <%
        i++;%>




                                </display:table>

                        </table></div>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <html:hidden property="index" />

        </html:form>

    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


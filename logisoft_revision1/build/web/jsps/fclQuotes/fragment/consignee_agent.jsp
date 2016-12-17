<%
            if (importFlag) {
%>
<tr class="textlabelsBold">
    <td><input class="textlabelsBoldForTextBox"  name="consigneeName" id="consigneeName"
               value="${fclBl.consigneeName}"  size="38" style="text-transform: uppercase" onfocus="disableDojo(this)"/>
        <input name="shipperMaster_action" id="shipperMaster_action" type="hidden" Class="textlabelsBoldForTextBox"
               value="${fclBl.consigneeName}" />
        <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
        <script type="text/javascript">
            initOPSAutocomplete("consigneeName","shipperMaster_choices","consignee","shipperMaster_action",
            "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=7","getHouseConsigneeInfo()");
        </script>
<html:checkbox property="consigneeCheck" styleId="consigneeCheck" name="transactionBean"
               onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>',null,event);"
               onmouseout="tooltip.hide();"/>
<img src="<%=path%>/img/icons/display.gif" id="toggle" onclick="getConsignee(this.value)"/>
<img src="<%=path%>/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('consigneeNameBL',null,event)"/>
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="textlabelsBoldForTextBox" name="consignee" value="${fclBl.consigneeNo}"
                id="consignee" onkeydown="getHouseConsigneeAcctNo(this.value)" size="38" style="text-transform: uppercase" />
        <div id="consignee_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
        <script type="text/javascript">
            initOPSAutocomplete("consignee","consignee_choices","","",
            "<%=path%>/actions/getCustomer.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","");
        </script>
    </td>
</tr>
<%
    } else {
%>
<tr class="textlabelsBold">
    <td><input class="textlabelsBoldForTextBox"  name="consigneeName" id="consigneeName"
               value="${fclBl.consigneeName}"  size="38" style="text-transform: uppercase" onfocus="disableDojo(this)"/>
        <input name="shipperMaster_action" id="shipperMaster_action" type="hidden" Class="textlabelsBoldForTextBox"
               value="${fclBl.consigneeName}" />
        <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
        <script type="text/javascript">
                                                                          initOPSAutocomplete("consigneeName","shipperMaster_choices","consignee","shipperMaster_action",
                                                                          "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=7","getHouseConsigneeInfo()");
        </script>
<html:checkbox property="consigneeCheck" styleId="consigneeCheck" name="transactionBean"
               onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>',null,event);"
               onmouseout="tooltip.hide();"/>
<img src="<%=path%>/img/icons/display.gif" id="toggle" onclick="getConsignee(this.value)"/>
<img src="<%=path%>/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('consigneeNameBL')"/>
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="textlabelsBoldForTextBox" name="consignee" value="${fclBl.consigneeNo}"
                id="consignee" onkeydown="getHouseConsigneeAcctNo(this.value)" size="38" style="text-transform: uppercase" />
        <div id="consignee_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
        <script type="text/javascript">
            initOPSAutocomplete("consignee","consignee_choices","","",
            "<%=path%>/actions/getCustomer.jsp?tabName=FCL_BILL_LADDING&from=2&isDojo=false","");
        </script>
    </td>
</tr>
}%>
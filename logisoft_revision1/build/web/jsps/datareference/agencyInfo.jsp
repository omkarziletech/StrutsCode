<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="java.util.List,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.AgencyInfo"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

            String companyCodeValue = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            request.setAttribute("companyCodeValue", companyCodeValue);




            String agentAccNo = "";
            String agentName = "";
            String consigneeAcctNo = "";
            String consigneeName = "";
            StringBuilder portOfDischarge =  new StringBuilder();
            String finalDeliveryTo = "";
            AgencyInfo agencyObj = new AgencyInfo();
            List agencyInfoListForLCL = null;
            String buttonValue = "";
            String agency = "";
            AgencyInfo agencyDefaultObj = new AgencyInfo();
            agencyDefaultObj.setDefaultValue("Y");
            request.setAttribute("agencyDefaultObj", agencyDefaultObj);

            if (session.getAttribute("agency") != null) {
                agency = (String) session.getAttribute("agency");
            }


            if (session.getAttribute("agencyObjlcl") != null) {
                agencyObj = (AgencyInfo) session.getAttribute("agencyObjlcl");
            }
            if (agencyObj != null && agencyObj.getAgentId() != null) {
                agentAccNo = agencyObj.getAgentId().getAccountno();
                agentName = agencyObj.getAgentId().getAccountName();
            }
            if (agencyObj != null && agencyObj.getConsigneeId() != null) {
                consigneeAcctNo = agencyObj.getConsigneeId().getAccountno();
                consigneeName = agencyObj.getConsigneeId().getAccountName();
            }
            
            if (agencyObj != null && agencyObj.getPortOfDischarge()!= null) {
                    if (CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationName()) && null != agencyObj.getPortOfDischarge().getStateId()
                            && CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getStateId().getCode()) && CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationCode())) {
                        portOfDischarge.append(agencyObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyObj.getPortOfDischarge().getStateId().getCode()).append('(').append(agencyObj.getPortOfDischarge().getUnLocationCode()).append(')');
                    } else if (CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationName()) && agencyObj.getPortOfDischarge().getCountryId() != null
                            && CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationCode())) {
                        portOfDischarge.append(agencyObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyObj.getPortOfDischarge().getCountryId().getCodedesc()).append('(').append(agencyObj.getPortOfDischarge().getUnLocationCode()).append(')');
                    } else if (CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationCode()) && CommonUtils.isNotEmpty(agencyObj.getPortOfDischarge().getUnLocationCode())) {
                        portOfDischarge.append(agencyObj.getPortOfDischarge().getUnLocationName()).append('(').append(agencyObj.getPortOfDischarge().getUnLocationCode()).append(')');
                    }
                    portOfDischarge.toString();
                }
                finalDeliveryTo = agencyObj.getFinalDeliveryTo() != null ? agencyObj.getFinalDeliveryTo() : "";
            
            if (session.getAttribute("agencyInfoList") != null) {
                agencyInfoListForLCL = (List) session.getAttribute("agencyInfoList");
            }

            if (request.getAttribute("buttonValue") != null) {
                buttonValue = (String) request.getAttribute("buttonValue");
            }


            if (buttonValue.equals("save")) {
                if (agency.equals("add")) {
%>
<script>
    parent.parent.getLclPortsConfig();
    parent.parent.GB_hide();
</script>
<%        } else if (agency.equals("edit")) {
%>
<script>
    parent.parent.editLclPortsConfiguration();
    parent.parent.GB_hide();
</script>
<%                }
            }
%>


<%
            if (buttonValue.equals("cancel")) {
                if (agency.equals("add")) {
%>
<script>
    parent.parent.getLclPortsConfig();
    parent.parent.GB_hide();
</script>
<%} else if (agency.equals("edit")) {
%>
<script>
    parent.parent.editLclPortsConfiguration();
    parent.parent.GB_hide();
    self.close();
    opener.location.href="<%=path%>/jsps/datareference/editLclPortsConfig.jsp";
</script>
<%
                }
            }
            String modify = null, message = "";
            if (session.getAttribute("view") != null) {
                modify = (String) session.getAttribute("view");
            }
            if (session.getAttribute("message") != null) {
                message = (String) session.getAttribute("message");
            }
%>


<html> 
    <head>
        <title>Agency Info </title>
        <%@include file="../includes/baseResources.jsp" %>

        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        
        <script language="javascript" type="text/javascript">
            function save1()
            {
                if(document.AgencyInfoForm.defaultValue){
                    for(var i=0;i<document.AgencyInfoForm.defaultValue.length;i++){
                        if(document.AgencyInfoForm.defaultValue[i].checked){
                            document.AgencyInfoForm.index.value=i;
                        }
                    }
                }
                document.AgencyInfoForm.buttonValue.value="save";
                document.AgencyInfoForm.submit();
            }
            function submit1()
            {
                document.AgencyInfoForm.buttonValue.value="agentSelected";
                document.AgencyInfoForm.submit();
            }
            function submit2()
            {
                document.AgencyInfoForm.buttonValue.value="consigneeSelected";
                document.AgencyInfoForm.submit();
            }
            function addAgencyInfo()  {
                if(document.AgencyInfoForm.agentAcountNo.value==""){
                    alert("Please enter the agent Account No");
                    document.AgencyInfoForm.agentAcountNo.focus();
                    return;
                } 
                var nodeList = document.getElementsByName("lclAgentLevelBrand");
                if (!nodeList[0].checked && !nodeList[1].checked){
                    alert("Please Select Brand");
                    return;
                }
                document.AgencyInfoForm.buttonValue.value="add";
                document.AgencyInfoForm.submit();
            }
   			
            function confirmdelete(obj) {
                var rowindex=obj.parentNode.parentNode.rowIndex;
//                var x=document.getElementById('portexceptiontable').rows[rowindex].cells;
                document.AgencyInfoForm.index.value=rowindex-1;
                document.AgencyInfoForm.buttonValue.value="delete";
                var result = confirm("Are you sure you want to delete this Agent");
                if(result) {
                    document.AgencyInfoForm.submit();
                }
            }
            function confirmEdit(obj, agentNo, agentName, pickupNo, pickupName, brand,pod,fd) {
//                 document.AgencyInfoForm.agencyInfoId.value = parseInt(agencyInfoId); 
                 document.AgencyInfoForm.editUpdate.value = true;
                 document.getElementById('agentAcountNo').value =  agentNo;
                 document.getElementById('agentName').value = agentName;
                 document.getElementById('consigneeAcctNo').value = pickupNo;
                 document.getElementById('consigneeName').value = pickupName;
                 document.getElementById('podAgent').value = pod;
                 document.getElementById('finalDeliveryTo').value = fd;
                 var rowindex = obj.parentNode.parentNode.rowIndex;
                 document.AgencyInfoForm.index.value =rowindex-1;
                 
                 if(brand === "Econo" || brand === "OTI"){
                  document.getElementsByName("lclAgentLevelBrand")[0].checked = true; 
//                  document.getElementById("brandEcono").checked = true; same code
                 }else if(brand === "ECU WW") {
                     document.getElementsByName("lclAgentLevelBrand")[1].checked = true;
                 } else {
                     document.getElementsByName("lclAgentLevelBrand")[0].checked = false;
                     document.getElementsByName("lclAgentLevelBrand")[1].checked = false;
                 }
            }
            
            function validateLength(obj){
                if(obj.value.length > 25){
                    alert("Maxium Length 25 Characters Only");
                    document.getElementById("finalDeliveryTo").value= "";
                    return false;
                }
            }
            function cancel1()
            {
                document.AgencyInfoForm.buttonValue.value="cancel";
                document.AgencyInfoForm.submit();
            }
            function popup1(mylink, windowname)
            {
                if (!window.focus)return true;
                var href;
                if (typeof(mylink) == 'string')
                    href=mylink;
                else
                    href=mylink.href;
                mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                mywindow.moveTo(200,180);
                document.AgencyInfoForm.submit();
                return false;
            }
            function disabled(val1,val2)
            {
   
   	
                if(val1 == 0 || val1 == 3)
                {
                    var imgs = document.getElementsByTagName('img');
       			 
                    document.getElementById("save").style.visibility = 'hidden';
                    document.getElementById("add").style.visibility = 'hidden';
                    document.getElementById("cancel").style.visibility = 'hidden';
       			 
                    for(var k=0; k<imgs.length; k++)
                    {
                        if(imgs[k].id!="cancel")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for(i=0; i<input.length; i++)
                    {
                        if(input[i].id != "buttonValue")
                        {
                            input[i].readOnly=true;
                            input[i].style.color="blue";

                        }
                    }
                }
                if(val1 == 1)
                {
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if(val1 == 3 &&  val2!="")
                {
                    alert(val2);
                }
            }
            function fillAccountNo(){
               
                var val = document.getElementById('agentAcountNo').value;
                var accountNo = val.substring(0,val.indexOf(":-"));
                var accountName = val.substring(val.indexOf("-")+1,val.lastIndexOf("-"));
                document.getElementById('agentAcountNo').value = accountNo;
                document.getElementById('agentName').value= accountName;
              
                if(val.endsWith("DISABLED")){
                    var data = val.substring(0,val.indexOf(":-"));
                    disableAgent(data);
                }
            }         
           
            function fillAccountNo1(){
               
                var val = document.getElementById('agentName').value;
                var accountNo = val.substring(0,val.indexOf(":-"));
                var accountName = val.substring(val.indexOf("-")+1,val.lastIndexOf("-"));
                document.getElementById('agentAcountNo').value = accountNo;
                document.getElementById('agentName').value= accountName;
               
                if(val.endsWith("DISABLED")){
                    var data = val.substring(0,val.indexOf(":-"));
                    disableAgent(data);
                }
            }
            function fillAccountNo2(){
             
                var val = document.getElementById('consigneeAcctNo').value;
                var accountNo = val.substring(0,val.indexOf(":-"));
                var accountName = val.substring(val.indexOf("-")+1,val.lastIndexOf("-"));
                document.getElementById('consigneeAcctNo').value = accountNo;
                document.getElementById('consigneeName').value= accountName;
              
                if(val.endsWith("DISABLED")){
                    var data = val.substring(0,val.indexOf(":-"));
                    disableAgent(data);
                }
            }
            function fillAccountNo3(){
              
                var val = document.getElementById('consigneeName').value;
                var accountNo = val.substring(0,val.indexOf(":-"));
                var accountName = val.substring(val.indexOf("-")+1,val.lastIndexOf("-"));
                document.getElementById('consigneeAcctNo').value = accountNo;
                document.getElementById('consigneeName').value= accountName;
              
                if(val.endsWith("DISABLED")){
                    var data = val.substring(0,val.indexOf(":-"));
                    disableAgent(data);
                }
            }
            <%-- Alert For Disable Account--%>
                function disableAgent(data1){
                    if(data1 !== null && data1 !== "" ){
                        jQuery.ajaxx({
                            data: {
                                className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName : "checkForDisable",
                                param1 : data1
                            },
                            success: function(data) {
                                if(jQuery.trim(data) !== ""){
                                    alertNew(data);
                                    document.getElementById("agentAcountNo").value ="";
                                    document.getElementById("agentName").value ="";
                                    document.getElementById('consigneeAcctNo').value="";
                                    document.getElementById('consigneeName').value="";
                                }else{
                                    document.getElementById("agentAcountNo").value =data1;
                                }
                            }
                        });
                    }
                }

        </script>
        <%@include file="../includes/resources.jsp" %>

        <style>
            div.autocomplete ul{
                width: 400px;
            }
        </style>
    </head>
    <!--DESIGN FOR NEW ALERT BOX ---->
    <body class="whitebackgrnd"  topmargin="0">
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                           grayOut(false,'');">
            </form></div>
        <!--// ALERT BOX DESIGN ENDS -->
    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')" >
        <html:form action="/agencyInfo" name="AgencyInfoForm" type="com.gp.cong.logisoft.struts.form.AgencyInfoForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td>Agent Information</td>
                                <td  align="right">
                                    <input type="button" class="buttonStyleNew"  id="save" value="Save" onclick="save1()"/>
                                    <input type="button" class="buttonStyleNew" id="search" value="Go Back" onclick="cancel1()"/></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="80%" border="0" cellpadding="3" cellspacing="0" >
                                        <tr class="textlabelsBold">
                                            <td>Agent Acct No</td>
                                            <td >
                                                <input type="text" name="agentAcountNo" value="<%=agentAccNo%>" class="textlabelsBoldForTextBox"  id="agentAcountNo"/>
                                                <div id="agentAcountNochoices"  style="display: none;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("agentAcountNo","agentAcountNochoices","","",
                                                    "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=0&isDojo=false","fillAccountNo()");
                                                </script>
                                                <img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/AgentPopUp.jsp?button='+'lcl','windows')">
                                            </td>
                                            <td align="right" ><bean:message key="form.agencyInfoForm.AgentName" /></td>
                                            <td>
                                                <input type="text" name="agentName" class="textlabelsBoldForTextBox"  value="<%=agentName%>" id="agentName"/>
                                                <div id="agentNamechoices"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("agentName","agentNamechoices","","",
                                                    "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=1&isDojo=false","fillAccountNo1()");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>To Pickup Freight Acct&nbsp;</td>
                                            <td height="15">
                                                <input type="text" name="consigneeAcctNo" class="textlabelsBoldForTextBox" id="consigneeAcctNo" value="<%=consigneeAcctNo%>"/>
                                                <div id="consigneeAcctNochoices"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("consigneeAcctNo","consigneeAcctNochoices","","","<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=4&isDojo=false","fillAccountNo2()");
                                                </script>
                                                <img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/ConsigneePopUp.jsp?button='+'lcl','windows')"></td>
                                            <td align="right">To Pickup Freight name</td>
                                            <td>
                                                <input type="text" name="consigneeName" class="textlabelsBoldForTextBox"  id="consigneeName" value="<%=consigneeName%>"  />
                                                <div id="consigneeNamechoices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("consigneeName","consigneeNamechoices","","","<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=5&isDojo=false","fillAccountNo3()");
                                                </script>
                                            <td>&nbsp;Brand</td>
                                            <td height="15">
                                                <input type="hidden" id="companyCode" value="${companyCodeValue}"/>
                                                <c:choose>
                                                    <c:when test="${companyCodeValue == '03'}">
                                                        <input type="radio" name="lclAgentLevelBrand" value="Econo">Econo &nbsp;
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" name="lclAgentLevelBrand" value="OTI">OTI &nbsp;
                                                    </c:otherwise>
                                                </c:choose>                                   
                                                <input type="radio" name="lclAgentLevelBrand" value="ECU WW">ECU WW
                                            </td>
                                        </tr>
                                        
                                        <tr class="textlabelsBold">
                                            <td>Default port of Discharge&nbsp;</td>
                                            <td >
                                                <input type="text" name="portOfDischarge" class="textlabelsBoldForTextBox" id="podAgent" value="<%=portOfDischarge%>" />
                                                <div id="defaultPortOfDischargechoices"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                   initAutocomplete("podAgent", "defaultPortOfDischargechoices", "", "","<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=PORTSAGENT&from=1&isDojo=false", "");
                                                </script>
                                            </td>
                                            <td align="right">Final Delivery To</td>
                                            <td>
                                                <input type="text" name="finalDeliveryTo" class="textlabelsBoldForTextBox" 
                                                       id="finalDeliveryTo"  onkeyup="validateLength(this)"  value="<%=finalDeliveryTo%>" />
                                             </td>   
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td colspan="4" align="center"><input type="button" class="buttonStyleNew" id="add" value="Add" onclick="addAgencyInfo()"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Results</tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td>
                                                <div id="divtablesty1" style="border:thin;overflow:scroll;width:1000;height:220;">
                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                        <%
                                                                    int i = 0;
                                                                    int k = 0;
                                                        %>
                                                        <display:table name="<%=agencyInfoListForLCL%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable">
                                                            <display:setProperty name="paging.banner.some_items_found">
                                                                <span class="pagebanner"> <font color="blue">{0}</font>
			Port Exceptions Displayed,For more Data click on Page Numbers. <br>
                                                                </span>
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.one_item_found">
                                                                <span class="pagebanner">
			One {0} displayed. Page Number
                                                                </span>
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.all_items_found">
                                                                <span class="pagebanner">
			{0} {1} Displayed, Page	Number
                                                                </span>
                                                            </display:setProperty>
                                                            <display:setProperty name="basic.msg.empty_list">
                                                                <span class="pagebanner"> No Records Found. </span>
                                                            </display:setProperty>
                                                            <display:setProperty name="paging.banner.placement"
                                                                                 value="bottom" />
                                                            <display:setProperty name="paging.banner.item_name"
                                                                                 value="Agency Info" />
                                                            <display:setProperty name="paging.banner.items_name"
                                                                                 value="Agency Info" />
                                                            <%
                                                                TradingPartner customerObj = null;
                                                                TradingPartner consigneeObj = null;
                                                                String agentAcountNo = "";
                                                                String agntName = "";
                                                                String conAcctNo = "";
                                                                String conName = "";
                                                                String lclAgentLevelBrand = "";
                                                                StringBuilder pod = new StringBuilder();
                                                                String fd = "";
                                                                if (agencyInfoListForLCL != null && agencyInfoListForLCL.size() > 0) {
                                                                    AgencyInfo agencyInfoObj = (AgencyInfo) agencyInfoListForLCL.get(i);
                                                                    if (agencyInfoObj != null) {
                                                                        customerObj = agencyInfoObj.getAgentId();
                                                                        consigneeObj = agencyInfoObj.getConsigneeId();
                                                                        lclAgentLevelBrand = agencyInfoObj.getLclAgentLevelBrand();
                                                                        if (customerObj != null) {
                                                                            agentAcountNo = customerObj.getAccountno();
                                                                            agntName = customerObj.getAccountName();
                                                                        }
                                                                        if (consigneeObj != null) {
                                                                            conAcctNo = consigneeObj.getAccountno();
                                                                            conName = consigneeObj.getAccountName();
                                                                        }
                                                                        if (agencyInfoObj != null && agencyInfoObj.getPortOfDischarge() != null) {
                                                                            if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationName()) && null != agencyInfoObj.getPortOfDischarge().getStateId()
                                                                                    && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getStateId().getCode()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                                pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyInfoObj.getPortOfDischarge().getStateId().getCode()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                            } else if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationName()) && agencyInfoObj.getPortOfDischarge().getCountryId() != null
                                                                                    && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                                pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append("/").append(agencyInfoObj.getPortOfDischarge().getCountryId().getCodedesc()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                            } else if (CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode()) && CommonUtils.isNotEmpty(agencyInfoObj.getPortOfDischarge().getUnLocationCode())) {
                                                                                pod.append(agencyInfoObj.getPortOfDischarge().getUnLocationName()).append('(').append(agencyInfoObj.getPortOfDischarge().getUnLocationCode()).append(')');
                                                                            }
                                                                            pod.toString();
                                                                        }
                                                                        fd = agencyInfoObj.getFinalDeliveryTo() != null ? agencyInfoObj.getFinalDeliveryTo() : "";
                                                                    }
                                                                }
                                                            %>
                                                            <display:column title="Agent Acct No"><%=agentAcountNo%></display:column>

                                                            <display:column title="Agent Name" ><%=agntName%></display:column>

                                                            <display:column title="To Pickup Freight Acct"><%=conAcctNo%></display:column>

                                                            <display:column title="To Pickup Freight name" ><%=conName%></display:column>

                                                            <display:column title="Default" >
                                                                <c:choose>
                                                                    <c:when test="${portexceptiontable.defaultValue == 'Y'}">
                                                                        <html:radio property="defaultValue" value="Y" name="agencyDefaultObj" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <html:radio property="defaultValue" value="N" name="agencyDefaultObj" />
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </display:column>
                                                            <display:column title="Brand" >
                                                                <c:set var="lclAgentBrand" value="<%=lclAgentLevelBrand%>"/>
                                                                <c:choose>
                                                                    <c:when test="${lclAgentBrand eq 'Econo'}">
                                                                        Econo
                                                                    </c:when>
                                                                    <c:when test="${lclAgentBrand eq 'OTI'}">
                                                                        OTI
                                                                    </c:when>
                                                                    <c:when test="${lclAgentBrand eq 'ECU WW'}">
                                                                        ECU WW
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </display:column>
                                                            <display:column  title="PortOfDischarge">
                                                                <%=pod%>
                                                            </display:column >  
                                                            <display:column  title="FD">
                                                                <%=fd%>
                                                            </display:column >  
                                                            <display:column title="Action">
                                                                
                           <img name="<%=i%>" src="<%=path%>/img/icons/delete.gif" border="0" id="delete" onclick="confirmdelete(this)"/>
                           <img name="<%=i%>" src="<%=path%>/img/icons/edit.gif" border="0" id="editResult" 
                                onclick="confirmEdit(this,'<%=agentAcountNo%>','<%=agntName%>',
                                            '<%=conAcctNo%>', '<%=conName%>', '<%=lclAgentLevelBrand%>','<%=pod%>','<%=fd%>')"/>
                                                            </display:column>
                                                            <% i++;
                                                                        k++;
                                                            %>
                                                        </display:table>
                                                    </table></div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <html:hidden property="index" />
            <html:hidden property="editUpdate" />
        </html:form>
    </body>
</html>


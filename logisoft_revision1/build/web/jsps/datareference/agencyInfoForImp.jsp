<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="java.util.List,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.AgencyInfo"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";



String agentAccNo="";
String agentName="";
String consigneeAcctNo="";
String consigneeName="";
AgencyInfo agencyObj = new AgencyInfo() ;
List agencyInfoListForImp = null;
String buttonValue="";
String agency="";
if(session.getAttribute("agencyimport")!=null)
{
agency=(String)session.getAttribute("agencyimport");
}
        AgencyInfo agencyDefaultObj=new AgencyInfo();
        agencyDefaultObj.setDefaultValue("Y");
        request.setAttribute("agencyDefaultObj",agencyDefaultObj);
	
	
	
        if(session.getAttribute("agencyObjimport") != null)
        {
                agencyObj = (AgencyInfo) session.getAttribute("agencyObjimport");
        }
        if(agencyObj != null && agencyObj.getAgentId() != null)
        {
                agentAccNo = agencyObj.getAgentId().getAccountno();
                agentName = agencyObj.getAgentId().getAccountName();
        }
        if(agencyObj != null  && agencyObj.getConsigneeId() != null)
        {
                consigneeAcctNo = agencyObj.getConsigneeId().getAccountno();
                consigneeName = agencyObj.getConsigneeId().getAccountName();
        }
        if (session.getAttribute("agencyInfoListForImp") != null)
        {
                agencyInfoListForImp = (List) session.getAttribute("agencyInfoListForImp");
                session.setAttribute("agencyInfoListForImp",agencyInfoListForImp);
        }
        if(request.getAttribute("buttonValue")!=null)
        {
                buttonValue=(String)request.getAttribute("buttonValue");
        }
        if(buttonValue.equals("save")|| buttonValue.equals("cancel"))
        {
        if(agency.equals("add"))
        {
%>
<script>
    parent.parent.getImplPortsConfig();
    parent.parent.GB_hide();
</script>
<%
}
else if(agency.equals("edit"))
{
%>
<script>
    parent.parent.editImplPortsConfig();
    parent.parent.GB_hide();
</script>
<%
}
}
String modify=null,message="";
if (session.getAttribute("view") != null) {
        modify = (String) session.getAttribute("view");
}
if(session.getAttribute("message")!=null)
{
message=(String)session.getAttribute("message");
}
%>
<html> 
    <head>
        <%@include file="../includes/resources.jsp" %>

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
		
          function save1(){
              for(var i=0;i<document.agencyInfoForImp.defaultValue.length;i++){
                    if(document.agencyInfoForImp.defaultValue[i].checked){
                        document.agencyInfoForImp.index.value=i;
                    }
                }
                document.agencyInfoForImp.buttonValue.value="save";
                document.agencyInfoForImp.submit();
            }
            function cancel1(){
                document.agencyInfoForImp.buttonValue.value="cancel";
                document.agencyInfoForImp.submit();
            }
            function submit1(){
                document.agencyInfoForImp.buttonValue.value="agentSelected";
                document.agencyInfoForImp.submit();
            }
            function submit2(){
                document.agencyInfoForImp.buttonValue.value="consigneeSelected";
                document.agencyInfoForImp.submit();
            }
            function addAgencyInfo(){
                if(document.agencyInfoForImp.agentAcountNo.value==""){
                    alert("Please enter the Agent Account No");
                    document.agencyInfoForImp.agentAcountNo.focus();
                    return;
                }
                if(document.agencyInfoForImp.consigneeAcctNo.value=="0"){
                    alert("Please enter the consignee Account No");
                    document.agencyInfoForImp.consigneeAcctNo.focus();
                    return;
                }
                document.agencyInfoForImp.buttonValue.value="add";
                document.agencyInfoForImp.submit();
            }
            function confirmdelete(obj){
                var rowindex=obj.parentNode.parentNode.rowIndex;
                var x=document.getElementById('portexceptiontable').rows[rowindex].cells;
	
                document.agencyInfoForImp.index.value=rowindex-1;
                document.agencyInfoForImp.buttonValue.value="delete";
                var result = confirm("Are you sure you want to delete this Agent");
                if(result){
                    document.agencyInfoForImp.submit();
                }
            }
            function popup1(mylink, windowname){
                if(!window.focus)return true;
                var href;
                if (typeof(mylink) == 'string')
                    href=mylink;
                else
                    href=mylink.href;
                mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                mywindow.moveTo(200,180);
                document.agencyInfoForImp.submit();
                return false;
            }
            function disabled(val1,val2){
                if(val1 == 0 || val1 == 3){
                    var imgs = document.getElementsByTagName('img');
                    document.getElementById("save").style.visibility = 'hidden';
                    document.getElementById("add").style.visibility = 'hidden';
                    document.getElementById("cancel").style.visibility = 'hidden';
                    for(var k=0; k<imgs.length; k++){
                        if(imgs[k].id!="cancel"){
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for(i=0; i<input.length; i++){
                        if(input[i].id != "buttonValue"){
                            input[i].readOnly=true;
                            input[i].style.color="blue";
                        }
                    }
                }
                if(val1 == 1){
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if(val1 == 3 &&  val2!=""){
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
       
              <style>
            div.autocomplete ul{
                width: 365px;
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
    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
        <html:form action="/agencyInfoForImp" name="agencyInfoForImp" type="com.gp.cong.logisoft.struts.form.AgencyInfoForImpForm" scope="request">

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td>Agent Information</td>
                                <td align="right">
                                    <input type="button" class="buttonStyleNew" id="save" value="Save" onClick="save1()"/>
                                    <input type="button" class="buttonStyleNew" id="cancel" value="Go Back"  onclick="cancel1()"/></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="80%" border="0" cellpadding="3" cellspacing="0" >
                                        <tr class="textlabelsBold">
                                            <td>Agent Acct No</td>
                                            <td>
                                                <%--<html:text property="agentAcountNo" value="<%=agentAccNo%>"  onfocus="this.blur();"  styleClass="textlabelsBoldForTextBox" />--%>
                                                <input type="text" name="agentAcountNo" value="<%=agentAccNo%>" class="textlabelsBoldForTextBox"  id="agentAcountNo"/>
                                                <div id="agentAcountNochoices"  style="display: none; width: 2px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("agentAcountNo","agentAcountNochoices","","",
                                                    "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=0&isDojo=false","fillAccountNo()");
                                                </script>
                                                <img border="0" src="<%=path%>/img/icons/display.gif" alt="search"
                                                     onclick="return popup1('<%=path%>/jsps/datareference/AgentPopUp.jsp?button=import','windows')"></td>
                                            <td>Agent Name</td>
                                            <td>
                                                <%-- <html:text property="agentName" value="<%=agentName%>" maxlength="100" readonly="true" onfocus="this.blur();"
                                                        styleClass="textlabelsBoldForTextBox" />--%>
                                                <input type="text" name="agentName" class="textlabelsBoldForTextBox"  value="<%=agentName%>" id="agentName"/>
                                                <div id="agentNamechoices"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("agentName","agentNamechoices","","",
                                                    "<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=1&isDojo=false","fillAccountNo1()");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Consignee Acct No</td>
                                            <td>
                                                <%--<html:text property="consigneeAcctNo" value="<%=consigneeAcctNo%>"  onfocus="this.blur();"
                                                       styleClass="textlabelsBoldForTextBox"/>--%>
                                                <input type="text" name="consigneeAcctNo" class="textlabelsBoldForTextBox" id="consigneeAcctNo" value="<%=consigneeAcctNo%>"/>
                                                <div id="consigneeAcctNochoices"   class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("consigneeAcctNo","consigneeAcctNochoices","","","<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=2&isDojo=false","fillAccountNo2()");
                                                </script>
                                                <img border="0" src="<%=path%>/img/icons/display.gif" alt="search"
                                                     onclick="return popup1('<%=path%>/jsps/datareference/ConsigneePopUp.jsp?button=import','windows')"></td>
                                            <td>Consignee Name</td>
                                            <td>
                                                <%--<html:text property="consigneeName" value="<%=consigneeName%>" maxlength="100" readonly="true"
                                                       onfocus="this.blur();" styleClass="textlabelsBoldForTextBox"/>--%> 
                                                <input type="text" name="consigneeName" class="textlabelsBoldForTextBox"  id="consigneeName" value="<%=consigneeName%>"  />
                                                <div id="consigneeNamechoices"   class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("consigneeName","consigneeNamechoices","","","<%=path%>/actions/getCustomer.jsp?tabName=PORTS&from=3&isDojo=false","fillAccountNo3()");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td colspan="4" align="center">
                                                <input type="button" class="buttonStyleNew" id="add" value="Add" onClick="addAgencyInfo()"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr><td>&nbsp;</td></tr>

                <tr><td colspan="2">
                        <table border="0" width="100%" class="tableBorderNew">
                            <tr class="tableHeadingNew">Results</tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td>
                                                <div id="divtablesty1" style="border:thin;overflow:scroll;width:850;height:220;">
                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                        <%
                                                                int i=0;
                                                                int k=0;
                                                        %>
                                                        <display:table name="<%=agencyInfoListForImp%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable">
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
                                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                                            <display:setProperty name="paging.banner.item_name" value="Agency Info" />
                                                            <display:setProperty name="paging.banner.items_name" value="Agency Info" />
                                                            <%
                                                            TradingPartner customerObj = null;
                                                        TradingPartner consigneeObj=null;
                                                        String agentAcountNo="";
                                                                    String agntName="";
                                                                    String conAcctNo="";
                                                                    String conName="";
                                                        if(agencyInfoListForImp!=null && agencyInfoListForImp.size()>0) {
                                                            AgencyInfo agencyInfoObj=(AgencyInfo)agencyInfoListForImp.get(i);
                                                            if(agencyInfoObj!=null) {
                                                                    customerObj= agencyInfoObj.getAgentId();
                                                                consigneeObj=agencyInfoObj.getConsigneeId();
                                                                if(customerObj!=null) {
                                                                    agentAcountNo = customerObj.getAccountno();
                                                                    agntName = customerObj.getAccountName();
                                                                }
                                                                if(consigneeObj!=null){
                                                                    conAcctNo = consigneeObj.getAccountno();
                                                                    conName = consigneeObj.getAccountName();
                                                                }
                                                            }
                                                         }
                                                            %>
                                                            <display:column title="Agent Acct No"><%=agentAcountNo%></display:column>
                                                            <display:column title="Agent Name"><%=agntName%></display:column>
                                                            <display:column title="Consignee AcctNo"><%=conAcctNo%></display:column>
                                                            <display:column title="Consignee Name" ><%=conName%></display:column>
                                                            <display:column title="Default">
                                                                <html:radio property="defaultValue" value="Y" name="agencyDefaultObj" ></html:radio>
                                                            </display:column>
                                                            <display:column><img name="<%=i%>" src="<%=path%>/img/icons/delete.gif"  alt="delete"
                                                                 id="delete" onclick="confirmdelete(this)"/></display:column>
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
            <html:hidden property="defaultValue"/>
            <html:hidden property="index" />
        </html:form>
    </body>
</html>


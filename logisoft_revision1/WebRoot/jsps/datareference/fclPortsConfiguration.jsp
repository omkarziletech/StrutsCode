<%@page import="com.gp.cong.logisoft.domain.TradingPartner"%>
<%@page import="com.gp.cong.logisoft.domain.AgencyInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.gp.cong.logisoft.domain.FCLPortConfiguration"%>
<%@page import="com.gp.cong.logisoft.beans.PortsBean"%>
<%@page import="java.util.List"%>
<%@page import="com.gp.cong.logisoft.util.DBUtil"%>
<%@page import="com.gp.cong.logisoft.domain.RefTerminal"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
         java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.FCLPortConfiguration,com.gp.cong.logisoft.domain.AgencyInfo,com.gp.cong.logisoft.beans.PortsBean,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<jsp:useBean id="cubeFclObj" class="com.gp.cong.logisoft.struts.form.FclPortsConfigurationForm" scope="request"></jsp:useBean>
<%
String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();
List terminalList=new ArrayList();
List lineManagerList=new ArrayList();
List ruleList=new ArrayList();
List clauseList=new ArrayList();
String buttonValue="";

RefTerminal terminalobj=null;
String terminalName="";
String terminalNo="";
List agencyInfoListForFCL = null;
String userId="";
String blclause="0";
String blclausedesc="";
String rule1="0";
String rule2="0";
String rule3="0";
String temporaryText="";
String originRemarks = "";
String txtCal="";
String rule4="0";
String amount1="";
String amount2="";
String amount3="";
String amount4="";
String tierAmount1="";
String tierAmount2="";
String tierAmount3="";
String tierAmount4="";
String currentAdjFactor="";
String transhipment="";
String txtCal1="";
// changes from hyd
String srvcFcl="";
String cubeWtMandatoryFcl="";
String fclSsBlGoCollect="";
String fclHouseBlGoCollect="";
String defaultDischarge="";
String specialRemarks="";
String insuranceAllowed="";
String brandField="";
PortsBean portBean=new PortsBean();
List portcodeList=new ArrayList();
if(portcodeList != null)
{
//portcodeList=dbUtil.getlclCodeList();
//request.setAttribute("transhipmentList",portcodeList);
}
        if(terminalList != null)
        {
                //terminalList=dbUtil.getTerminalListForLCLPortsConfig();
                //request.setAttribute("terminalList",terminalList);
        }
        if(request.getAttribute("terminalObj") != null)
        {
                terminalobj =(RefTerminal) request.getAttribute("terminalObj");
                terminalName=terminalobj.getTerminalLocation();
                terminalNo = terminalobj.getTrmnum();
        }
        FCLPortConfiguration fclPortobj=new FCLPortConfiguration();
        if(session.getAttribute("fclPortobj")!=null)
        {
        fclPortobj=(FCLPortConfiguration)session.getAttribute("fclPortobj");
        if(fclPortobj.getTrmnum()!=null)
        {
        terminalNo=fclPortobj.getTrmnum().getTrmnum();
        terminalName=fclPortobj.getTrmnum().getTrmnam();
        }
        if(fclPortobj.getLineManager() !=null){
                userId=fclPortobj.getLineManager();
        }
        if(fclPortobj.getTranshipment() !=null){
                transhipment=fclPortobj.getTranshipment();
        }
        if(fclPortobj.getDefaultPortOfDischarge() !=null){
                defaultDischarge=fclPortobj.getDefaultPortOfDischarge();
        }
        if(fclPortobj.getExpirationDate()!=null)
        {
                SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");

                txtCal1=sdf.format(fclPortobj.getExpirationDate());
        }
        /*if(fclPortobj.getTranshipment()!=null )
        {
         transhipment=fclPortobj.getTranshipment().getShedulenumber()+"-"+ fclPortobj.getTranshipment().getPortname();
        }
        if(fclPortobj.getDefaultPortOfDischarge()!=null )
        {
         defaultDischarge=fclPortobj.getDefaultPortOfDischarge().getShedulenumber()+"-"+ fclPortobj.getDefaultPortOfDischarge().getPortname();
        }
        if(fclPortobj.getLineManager()!=null)
        {
        userId=fclPortobj.getLineManager().getFirstName() +" " +fclPortobj.getLineManager().getLastName();
        }*/
        if(fclPortobj.getBlClauseId()!=null)
        {
        if(fclPortobj.getBlClauseId().getId()!=null)
        {
        blclause=fclPortobj.getBlClauseId().getId().toString();
        }
        if(fclPortobj.getBlClauseId().getCodedesc()!=null)
        {
        blclausedesc=fclPortobj.getBlClauseId().getCodedesc();
        }
        }
        if(fclPortobj.getRadmRule()!=null)
        {
        rule1=fclPortobj.getRadmRule().getId().toString();
        }
 
        if(fclPortobj.getRcomRule()!=null)
        {
        rule2=fclPortobj.getRcomRule().getId().toString();
 	
        }
        if(fclPortobj.getNadmRule()!=null)
        {
        rule3=fclPortobj.getNadmRule().getId().toString();
        }
        if(fclPortobj.getNcomRule()!=null)
        {
        rule4=fclPortobj.getNcomRule().getId().toString();
        }
        if(fclPortobj.getRadmAm() != null)
        {
                amount1=fclPortobj.getRadmAm().toString();
        }
        if(fclPortobj.getRadmTierAmt() != null)
        {
                tierAmount1=fclPortobj.getRadmTierAmt().toString();
        }
        if(fclPortobj.getRcomAm() != null)
        {
                amount2=fclPortobj.getRcomAm().toString();
        }
        if(fclPortobj.getRcomTierAmt() != null)
        {
                tierAmount2=fclPortobj.getRcomTierAmt().toString();
        }
        if(fclPortobj.getNadmAm() != null)
        {
                amount3=fclPortobj.getNadmAm().toString();
        }
        if(fclPortobj.getNadmTierAmt() != null)
        {
        tierAmount3=fclPortobj.getNadmTierAmt().toString();
        }
        if(fclPortobj.getNcomAm() != null)
        {
        amount4=fclPortobj.getNcomAm().toString();
        }
        //updated by hyd.
 //updated by hyd.
        srvcFcl=fclPortobj.getSrvcFcl();
    if(srvcFcl!=null && srvcFcl.equals("Y"))
    {
    cubeFclObj.setSrvcFcl("on");
    }
    else
    {
    cubeFclObj.setSrvcFcl("off");
    }
    cubeWtMandatoryFcl= fclPortobj.getSrvcFcl();
    if(cubeWtMandatoryFcl!=null && cubeWtMandatoryFcl.equals("Y"))
    {
    cubeFclObj.setCubeWtMandatoryFcl("on");
    }
    else{
    cubeFclObj.setCubeWtMandatoryFcl("off");
    }
    
    
    fclSsBlGoCollect=fclPortobj.getFclSsBlGoCollect();
    if(fclSsBlGoCollect!=null && fclSsBlGoCollect.equals("Y"))
    {
    cubeFclObj.setFclSsBlGoCollect("on");
    
    }
    else
    {
    cubeFclObj.setFclSsBlGoCollect("off");
    }
         fclHouseBlGoCollect=fclPortobj.getFclHouseBlGoCollect();
         if(fclHouseBlGoCollect!=null && fclHouseBlGoCollect.equals("Y"))
         {
          cubeFclObj.setFclHouseBlGoCollect("on");
         }
         else
         {
         cubeFclObj.setFclHouseBlGoCollect("off");
         }
         insuranceAllowed=fclPortobj.getInsuranceAllowed();
         if(insuranceAllowed.equals("Y")) {
             cubeFclObj.setInsuranceAllowed("Y");
         } else {
             cubeFclObj.setInsuranceAllowed("N");
          }
         brandField=fclPortobj.getBrandField();
         if(brandField.equals("Econocaribe")){
            cubeFclObj.setBrandField("Econocaribe");
         }else{
            cubeFclObj.setBrandField("Ecu Worldwide"); 
         }
         specialRemarks=fclPortobj.getSpecialRemarks();
 	 
         if(specialRemarks!=null && specialRemarks.equals("Y"))
         {
          cubeFclObj.setSpecialRemarks("on");
         }
         else
         {
        cubeFclObj.setSpecialRemarks("off");
         }
 	
        if(fclPortobj.getNcomTierAmt()!= null)
        {
                tierAmount4=fclPortobj.getNcomTierAmt().toString();
        }
        if(fclPortobj.getCurrentAdjFactor() != null)
        {
         currentAdjFactor=fclPortobj.getCurrentAdjFactor().toString();
        }
        if(fclPortobj.getTemporaryText()!=null)
        {
        temporaryText=fclPortobj.getTemporaryText();
        }
        if(fclPortobj.getOriginRemarks()!=null)
        {
        originRemarks=fclPortobj.getOriginRemarks();
        }
        }
 	
        if(lineManagerList != null)
        {
                //lineManagerList=dbUtil.getAllUserForLinemanagers();
                //request.setAttribute("lineManagerList",lineManagerList);
        }
        if(ruleList != null)
        {
                ruleList=dbUtil.getGenericCodeList(new Integer(20),"yes","Select Rule");
                request.setAttribute("ruleList",ruleList);
        }
        if(clauseList != null)
        {
                clauseList=dbUtil.getGenericCodeList(new Integer(7),"No","Select BL Clauses");
                request.setAttribute("clauseList",clauseList);
        }
         request.setAttribute("SSBLGOCollectList", new DBUtil().getSelectBoxList(new Integer(74), "Select"));
        if(session.getAttribute("agencyInfoListForFCL") != null)
        {
                agencyInfoListForFCL = (List)session.getAttribute("agencyInfoListForFCL");
                AgencyInfo agencyInfo=(AgencyInfo)agencyInfoListForFCL.get(0);
                session.setAttribute("agencyInfoListForFCLAdd",agencyInfoListForFCL);
        }
	
	
        FCLPortConfiguration cubeWtMandatoryFclObj=new FCLPortConfiguration();
        //cubeWtMandatoryFclObj.setCubeWtMandatoryFcl("Y");
        //cubeWtMandatoryFclObj.setSrvcFcl("Y");
        //cubeWtMandatoryFclObj.setFclSsBlGoCollect("Y");
        //cubeWtMandatoryFclObj.setFclHouseBlGoCollect("Y");
        //cubeWtMandatoryFclObj.setSpecialRemarks("Y");
        //cubeWtMandatoryFclObj.setQuoteClause("standard");
        if(request.getAttribute("portBean")!=null)
        {
        portBean=(PortsBean)request.getAttribute("portBean");
        // objectname changed
        cubeWtMandatoryFclObj.setSrvcFcl(srvcFcl);
        cubeWtMandatoryFclObj.setCubeWtMandatoryFcl(cubeWtMandatoryFcl);
        cubeWtMandatoryFclObj.setFclSsBlGoCollect(fclSsBlGoCollect);

        cubeWtMandatoryFclObj.setFclHouseBlGoCollect(fclHouseBlGoCollect);
        cubeWtMandatoryFclObj.setSpecialRemarks(specialRemarks);
	
        cubeWtMandatoryFclObj.setQuoteClause(portBean.getQuoteClause());
        txtCal=portBean.getTxtCal();
        }
        request.setAttribute("cubeWtMandatoryFclObj",cubeWtMandatoryFclObj);
        AgencyInfo agencyDefaultObj=new AgencyInfo();
        //agencyDefaultObj.setDefaultValue("Y");
        request.setAttribute("agencyDefaultObj",agencyDefaultObj);
        session.setAttribute("agencyfcl","add");
%>

<head>
    <title>FCL Ports Configuration</title>
    <%@include file="../includes/baseResources.jsp" %>

    <script language="javascript" type="text/javascript">
        function submit1()
        {
 				
            document.FclPortsConfigForm.buttonValue.value="terminalSelected";
            document.FclPortsConfigForm.submit();
 				
        }
        var newwindow = '';
        function openAgencyInfo1() {
          
            if (!newwindow.closed && newwindow.location)
            {
                newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForFcl.jsp";
            }
            else
            {
                newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForFcl.jsp","","width=650,height=450");
                if (!newwindow.opener) newwindow.opener = self;
            }
            if (window.focus) {newwindow.focus()}
            document.FclPortsConfigForm.submit();
            return false;
        }
        function getclause()
        {
            document.FclPortsConfigForm.buttonValue.value="clauseSelected";
            document.FclPortsConfigForm.submit();
 				
        }
        /*function chkall(){
if(document.newTerminalForm.airsrvc.checked)
{
     
document.newTerminalForm.airsrvc.value="Y";
     
document.newTerminalForm.airsrvc.focus();
    
return false;
}*/
        function enabletieramount1(obj)
        {
            if(obj.value=="316" || obj.value=="319")
            {
                var disD=document.getElementById("tieramount1");
                disD.disabled=false;
                disD.className="areahighlightwhite";
            }
            else
            {
                var disK=document.getElementById("tieramount1");
 				 	
                disK.disabled=true;
                disK.className="areahighlightgrey";
            }
        }
        function enabletieramount2(obj)
        {
            if(obj.value=="316" || obj.value=="319")
            {
                var disD=document.getElementById("tieramount2");
 				 	
                disD.disabled=false;
                disD.className="areahighlightwhite";
            }
            else
            {
                var disK=document.getElementById("tieramount2");
 				 	
                disK.disabled=true;
                disK.className="areahighlightgrey";
            }
        }
        function enabletieramount3(obj)
        {
            if(obj.value=="316" || obj.value=="319")
            {
                var disD=document.getElementById("tieramount3");
 				 	
                disD.disabled=false;
                disD.className="areahighlightwhite";
            }
            else
            {
                var disK=document.getElementById("tieramount3");
 				 	
                disK.disabled=true;
                disK.className="areahighlightgrey";
            }
        }
        function enabletieramount4(obj)
        {
            if(obj.value=="316" || obj.value=="319")
            {
                var disD=document.getElementById("tieramount4");
 				 	
                disD.disabled=false;
                disD.className="areahighlightwhite";
            }
            else
            {
                var disK=document.getElementById("tieramount4");
 				 	
                disK.disabled=true;
                disK.className="areahighlightgrey";
            }
        }
        function getradio(obj)
        {
            if(obj.value=="S")
            {
                document.getElementById("text").style.visibility='hidden';
                document.getElementById("date").style.visibility='visible';

            }
            else if(obj.value=="T")
            {
                document.getElementById("text").style.visibility='visible';

                document.getElementById("date").style.visibility='visible';

            }
            else if(obj.value=="N")
            {
                document.getElementById("text").style.visibility='hidden';

                document.getElementById("date").style.visibility='hidden';

            }
        }
        function toUppercase(obj)
        {
            obj.value = obj.value.toUpperCase();
        }
        function limitText(limitField, limitNum) {
            limitField.value = limitField.value.toUpperCase();
            if (limitField.value.length > limitNum) {
                limitField.value = limitField.value.substring(0, limitNum);
            }
 
        }
        function disabled()
        {
            var disK=document.getElementById("text");
            disK.style.visibility='hidden';


            document.getElementById("date").style.visibility='visible';
        }
        // functions for check boxes

        function chkall(){
            if(document.FclPortsConfigForm.srvcFcl.checked)
            {
     
                document.FclPortsConfigForm.srvcFcl.value="Y";
     
                document.FclPortsConfigForm.srvcFcl.focus();
    
                return false;
            }
        } //2 check cubeWtMandatoryFcl
        function cubechkall(){
            if(document.FclPortsConfigForm.cubeWtMandatoryFcl.checked)
            {
     
                document.FclPortsConfigForm.cubeWtMandatoryFcl.value="Y";
     
                document.FclPortsConfigForm.cubeWtMandatoryFcl.focus();
    
                return false;
            }
        } //3 check fclSsBlGoCollect
        
        function fclschkall(){
            if(document.FclPortsConfigForm.fclSsBlGoCollect.checked)
            {
     
                document.FclPortsConfigForm.fclSsBlGoCollect.value="Y";
     
                document.FclPortsConfigForm.fclSsBlGoCollect.focus();
    
                return false;
            }
        }
        function housechkall(){
            if(document.FclPortsConfigForm.fclHouseBlGoCollect.checked)
            {
     
                document.FclPortsConfigForm.fclHouseBlGoCollect.value="Y";
     
                document.FclPortsConfigForm.fclHouseBlGoCollect.focus();
    
                return false;
            }
        }
        function spchkall(){
            if(document.FclPortsConfigForm.specialRemarks.checked)
            {
     
                document.FclPortsConfigForm.specialRemarks.value="Y";
     
                document.FclPortsConfigForm.specialRemarks.focus();
    
                return false;
            }
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

            document.FclPortsConfigForm.submit();
            return false;
        }
        function getFclPortsConfig(){
                        //alert("I am MAin Method");
            window.location.href="<%=path%>/jsps/datareference/fclPortsConfiguration.jsp";
        }

        function limitTextarea(textarea,maxLines,maxChar){
            var lines=textarea.value.replace(/\r/g,'').split('\n'),
            lines_removed,
            char_removed,
            i;
            if(maxLines&&lines.length>maxLines){
                alertNew('You can not enter\nmore than '+maxLines+' lines');
                lines=lines.slice(0,maxLines);
                lines_removed=1
            }
            if(maxChar){
                i=lines.length;
                while(i-->0)
                    if(lines[i].length>maxChar){
                        lines[i]=lines[i].slice(0,maxChar);
                        char_removed=1;
                    }
                if(char_removed)alertNew('You can not enter more\nthan '+maxChar+' characters per line');
            }
            if(char_removed||lines_removed)textarea.value=lines.join('\n');
        }
    </script>
    <%@include file="../includes/resources.jsp" %>
</head>
<html>
    <body  class="whitebackgrnd" onload="disabled()">
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                      grayOut(false,'');">
            </form>
        </div>
        <html:form action="/fclPortsConfiguration" name="FclPortsConfigForm" type="com.gp.cong.logisoft.struts.form.FclPortsConfigurationForm" scope="request">

            <table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr>
                    <td colspan="2" style="padding-top: 5px;padding-bottom: 5px;">
                        <table  width="100%" cellpadding="2" cellspacing="2" border="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td>Agency Info
                                    <span style="padding-left: 810px;"><input type="button" class="buttonStyleNew" id="search" value="Agency Info"  onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForFcl.jsp?relay='+'add',400,900)"/>
                                    </span></td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">

                                        <%
                                                int i=0;
                                                int k=0;
                                        %>
                                        <display:table name="<%=agencyInfoListForFCL%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner"> <font color="blue">{0}</font>
			Agency Info Displayed,For more Agency Info click on Page Numbers. <br>
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
                                    TradingPartner consigneeObj=null;
                                    String agentAcountNo="";
                                                    String agntName="";
                                                    String conAcctNo="";
                                                    String conName="";
                                    if(agencyInfoListForFCL!=null && agencyInfoListForFCL.size()>0)
                                    {
                                            AgencyInfo agencyInfoObj=(AgencyInfo)agencyInfoListForFCL.get(i);
                                            if(agencyInfoObj!=null)
                                            {
                                                    customerObj= agencyInfoObj.getAgentId();
                                            consigneeObj=agencyInfoObj.getConsigneeId();
                                            if(customerObj!=null)
                                            {
                                                    agentAcountNo = customerObj.getAccountno();
                                                    agntName = customerObj.getAccountName();
                                                    }
                                            if(consigneeObj!=null)
                                            {
                                                    conAcctNo = consigneeObj.getAccountno();
                                                    conName = consigneeObj.getAccountName();
                                                    }
                                            }
                                    }
                                            %>
                                            <display:column title="Agent Acct No"><%=agentAcountNo%></display:column>
                                            <display:column></display:column>
                                            <display:column></display:column>
                                            <display:column title="Agent Name" ><%=agntName%></display:column>
                                            <display:column></display:column>
                                            <display:column></display:column>
                                            <display:column title="Consignee AcctNo"><%=conAcctNo%></display:column>
                                            <display:column></display:column>
                                            <display:column></display:column>
                                            <display:column title="Consignee Name" ><%=conName%></display:column>
                                            <display:column></display:column>
                                            <display:column></display:column>
                                            <display:column title="Default" property="defaultValue"/>

                                            <display:column></display:column>
                                            <display:column></display:column>
                                            <% i++;
                                               k++;
                                            %>
                                        </display:table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class="tableHeadingNew"><td>General Information</td></tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" cellpadding="2" cellspacing="0" border="0">
                            <tr>
                                <td class="textlabelsBold">Terminal No</td>
                                <td class="textlabelsBold">Terminal Location</td>
                                <td class="textlabelsBold">Line Manager</td>
                                <td class="textlabelsBold">Transhipment</td>
                                <td class="textlabelsBold">Default Port Of Discharge</td>  			
                            </tr>
                            <tr>
                                <td><html:text styleClass="textlabelsBoldForTextBox"   property="terminalNo" value="<%=terminalNo%>" readonly="true" style="width:110px" />
                                    &nbsp;<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'fcl','windows')">
                                </td>
                                <td><html:text  property="terminalName" value="<%=terminalName%>" maxlength="100" styleClass="areahighlightgrey" readonly="true" style="width:120px"/>

                                </td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="lineManager" value="<%=userId%>" style="width:120px" />
                                    &nbsp;&nbsp;
                                    <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/UserPopUp.jsp?button='+'editfcl','windows')">--%>
                                </td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="transhipment" value="<%=transhipment%>" style="width:120px" />
                                    &nbsp;&nbsp;
                                    <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editfcltrashipment','windows')">--%>
                                </td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="defaultPortOfDischarge" value="<%=defaultDischarge%>" style="width:140px" />
                                    &nbsp;&nbsp;
                                    <%--<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editfcldefaultport','windows')">--%>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
                <tr><td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr class="textlabelsBold">
                                <td valign="top">BL Release Clause </td>
                                <td valign="top"><html:select property="blClauses" style="width:120px;" styleClass="verysmalldropdownStyleForText" onchange="getclause()" value="<%=blclause%>">
                                        <html:optionsCollection name="clauseList"/>
                                    </html:select></td>
                                <td valign="top"><table><tr class="textlabelsBold">
                                            <td><html:checkbox property="cubeWtMandatoryFcl"   name="cubeFclObj" onclick="cubechkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></td>
                                            <td ><bean:message key="form.FCLPortsConfiguration.CubeandWtMandatoryonFCLB/LS" />(Y/N)</td>
                                            <td style="width: 60px">
                                            </td>
                                            <%--<td ><html:checkbox property="fclSsBlGoCollect"  name="cubeFclObj" onclick="fclschkall()" ></html:checkbox></td>--%>
                                            <td align="right"><bean:message key="form.FCLPortsConfiguration.FCLSSBLGOCollect" /></td>
                                            <td align="right">
                                                <html:select property="fclSsBlGoCollect" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${fclPortobj.fclSsBlGoCollect}" name="cubeFclObj">
                                                    <html:optionsCollection name="SSBLGOCollectList"/>
                                                </html:select>
                                            </td>
                                        </tr></table></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top"><bean:message key="form.FCLPortsConfiguration.ClauseDescription" /> </td>
                                <td valign="top"><html:textarea property="clauseDescription" cols="53" rows="3" value="<%=blclausedesc%>" onkeyup="toUppercase(this)" readonly="true" styleClass="textareahighlightgrey"/></td>
                                <td  valign="top"><table border="0"><tr class="textlabelsBold">
                                            <td><html:checkbox property="fclHouseBlGoCollect"   name="cubeFclObj" onclick="housechkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>',null,event);" onmouseout="tooltip.hide();"></html:checkbox></td>
                                            <td><bean:message key="form.FCLPortsConfiguration.fclHouseBLGoCollect" />(Y/N)</td>
                                            <td style="width: 120px">
                                            </td>
                                            <td><bean:message key="form.FCLPortsConfiguration.insuranceAllowed" />
                                                <html:radio property="insuranceAllowed" name="cubeFclObj" value="Y" onclick="insuranceAllowed(this)"/>Yes
                                                <html:radio property="insuranceAllowed" name="cubeFclObj" value="N" onclick="insuranceAllowed(this)"/>No
                                            </td>
                                        </tr><tr class="textlabelsBold">
                                            <td valign="top"><html:checkbox property="srvcFcl"   name="cubeFclObj" onclick="chkall()"></html:checkbox></td>
                                            <td valign="top"><bean:message key="form.FCLPortsConfiguration.ServiceFCL" /></td>
                                            <td style="width: 120px">
                                            </td>
                                            <td align="left"><bean:message key="form.FCLPortsConfiguration.CurrentAdjFactor"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <html:text styleClass="textlabelsBoldForTextBox" property="amountCurrentAdjFactor"  maxlength="5" size="5" value="<%=currentAdjFactor%>" onkeypress="getDecimals(this,1,event)"/></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <bean:message key="form.FCLPortsConfiguration.Brandfield" />
                                                <html:radio property="brandField" name="cubeFclObj" value="Econocaribe" onclick="brandField(this)"/>Econocaribe
                                                <html:radio property="brandField" name="cubeFclObj"  value="Ecu Worldwide" onclick="brandField(this)"/>Ecu Worldwide
                                            </td>
                                        </tr>
                                    </table></td>
                                <td colspan="2">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="6" style="border-bottom:  1px solid #dcdcdc">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>&nbsp;</td>
                                <td><bean:message key="form.FCLPortsConfiguration.Rule"/></td>
                                <td colspan="4" valign="top"><table border="0" ><tr class="textlabelsBold">
                                            <td><bean:message key="form.FCLPortsConfiguration.Amount(1)" /></td>
                                            <td style="padding-left: 80px;"><bean:message key="form.FCLPortsConfiguration.TierAmount"/></td>
                                        <tr></table></td>
                            </tr>

                            <tr class="textlabelsBold">
                                <td><bean:message key="form.FCLPortsConfiguration.RouteByAgentAdmin"/></td>
                                <td><html:select property="ruleRouteByAgentAdmin" styleClass="verysmalldropdownStyleForText" value="<%=rule1%>"
                                             onchange="enabletieramount1(this)">
                                        <html:optionsCollection name="ruleList"/>
                                    </html:select></td>
                                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold">
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteByAgentAdmin"  maxlength="8" size="8" value="<%=amount1%>" onkeypress="getDecimal(this,5,event)"/></td>
                                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteByAgentAdmin" styleId="tieramount1" maxlength="8" value="<%=tierAmount1 %>" disabled="true"  styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                                        <tr></table></td>
                            </tr>

                            <tr class="textlabelsBold">
                                <td><bean:message key="form.FCLPortsConfiguration.RouteByAgentCommn"/></td>
                                <td><html:select property="ruleRouteByAgentCommn" styleClass="verysmalldropdownStyleForText" value="<%=rule2%>" onchange="enabletieramount2(this)">
                                        <html:optionsCollection name="ruleList"/>
                                    </html:select></td>
                                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold">
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteByAgentCommn"  maxlength="8" size="8" value="<%=amount2 %>" onkeypress="getDecimal(this,5,event)"/></td>
                                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteByAgentCommn"  maxlength="8" styleId="tieramount2" value="<%=tierAmount2 %>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                                        <tr></table></td>
                            </tr>

                            <tr class="textlabelsBold">
                                <td>Not Routed By Agent Admin</td>
                                <td><html:select property="ruleRouteNotAgentAdmin" styleClass="verysmalldropdownStyleForText" value="<%=rule3%>"  onchange="enabletieramount3(this)">
                                        <html:optionsCollection name="ruleList"/>
                                    </html:select></td>
                                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold"><tr>
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteNotAgentAdmin"  maxlength="8" size="8" value="<%=amount3%>" onkeypress="getDecimal(this,5,event)"/></td>
                                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteNotAgentAdmin"  maxlength="8"  value="<%=tierAmount3%>" disabled="true" styleId="tieramount3" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                                        </tr></table></td>
                            </tr>

                            <tr class="textlabelsBold">
                                <td>Not Routed BY Agent Commn</td>
                                <td><html:select property="ruleRouteNotAgentCommn" styleClass="verysmalldropdownStyleForText" value="<%=rule4%>"  onchange="enabletieramount4(this)">
                                        <html:optionsCollection name="ruleList"/>
                                    </html:select></td>
                                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold"><tr>
                                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteNotAgentCommn"  maxlength="8" size="8" value="<%=amount4%>" onkeypress="getDecimal(this,5,event)"/></td>
                                            <td style="padding-right: 120px;"><html:text property="tierAmountRouteNotAgentCommn"  maxlength="8"  value="<%=tierAmount4%>" disabled="true" styleId="tieramount4" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                                        </tr></table></td>
                            </tr>

                            <tr>
                                <td colspan="6" style="border-bottom:1px solid #dcdcdc">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td colspan="6"><table width="100%">
                                        <tr class="textlabelsBold">
                                            <td height="20" class="textlabelsBold">Validity Clause Option </td>
                                            <td><html:radio property="quoteClause" value="S" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
                                            <td><bean:message key="form.FCLPortsConfiguration.Standard" /> </td>
                                            <td><bean:message key="form.FCLPortsConfiguration.Validity" /> </td>
                                            <td><html:radio property="quoteClause" value="T" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
                                            <td><bean:message key="form.FCLPortsConfiguration.Temporary" /></td>
                                            <td><bean:message key="form.FCLPortsConfiguration.validuntilthedatespecified" /> </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td><html:radio property="quoteClause" value="N" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
                                            <td><bean:message key="form.FCLPortsConfiguration.None" /></td>
                                        </tr>
                                    </table></td>
                            </tr>
                            <tr class="textlabelsBold" id ="date">
                                <td valign="top">FCL Validity Clause Date</td>
                                <td valign="top"><html:text styleClass="textlabelsBoldForTextBox" property="txtCal" styleId="txtcal" size="15" value="<%=txtCal%>"/>&nbsp;<img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>

                            </tr>
                            <tr class="textlabelsBold" id="text">
                                <td valign="top">FCL Quote Temporary Remark </td>
                                <td colspan="5">
                                    <html:textarea  property="temporaryText" rows="3" cols="110" value="<%=temporaryText%>" onkeyup="limitText(this.form.temporaryText,79)" styleClass="textareastyle"></html:textarea>
                                </td>
                            </tr>
                            <tr class="textlabelsBold" id="text">
                                <td valign="top">Origin Quote Remarks </td>
                                <td colspan="5">
                                    <html:textarea  property="originRemarks" rows="3" cols="110" value="<%=originRemarks%>" onkeyup="limitText(this,200)" styleClass="textareastyle"></html:textarea>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" style="border-bottom:1px solid #dcdcdc">
                                    &nbsp;
                                </td>
                            </tr>

                            <tr class="textlabelsBold">
                                <td  valign="top">GRI Expiration Date </td>
                                <td  valign="top"><html:text styleClass="textlabelsBoldForTextBox" property="txtCal1" styleId="txtcal1" size="15" value="<%=txtCal1%>"/>&nbsp;<img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);" /></td>

                            </tr>
                            <tr class="textlabelsBold">
                                <td colspan="3" valign="top"><bean:message key="form.FCLPortsConfiguration.SpecialRemarks" />
                                    <html:checkbox property="specialRemarks"  name="cubeFclObj" onclick="spchkall()" ></html:checkbox>
                                    <bean:message key="form.FCLPortsConfiguration.PrintRemarks" /></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td valign="top">Special GRI Remarks for Quotation</td>
                                <td colspan="5"><html:textarea  property="specialRemarksforQuotation"  cols="110" rows="3" value="<%=fclPortobj.getSpecialRemarksForQuot() %>"   onkeyup="limitText(this.form.specialRemarksforQuotation,320)" styleClass="textareastyle"/></td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    &nbsp;
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <%--<tr>
                    <td>
                      <table width="100%" border="0">
	    <tr class="style2">
    		<td>BL Release Clause </td>
    		<td><bean:message key="form.FCLPortsConfiguration.ClauseDescription" /></td>
    		<td align="right"><bean:message key="form.FCLPortsConfiguration.ServiceFCL" /></td>
    	    <td align="right"><html:checkbox property="srvcFcl"   name="cubeFclObj" onclick="chkall()"></html:checkbox></td>
 			<td align="right">
 			<span onmouseover="tooltip.show('<strong><bean:message key="form.FCLPortsConfiguration.CubeandWtMandatoryonFCLB/LS" /> </strong>');" onmouseout="tooltip.hide();">CubeWtMandatoryFCL B/LS</span>
    	    <td><html:checkbox property="cubeWtMandatoryFcl"   name="cubeFclObj" onclick="cubechkall()"></html:checkbox></td>
                       </tr>
  	  <tr class="style2">
    		<td valign="top"><html:select property="blClauses" styleClass="verysmalldropdownStyle" onchange="getclause()" value="<%=blclause%>">
      					 <html:optionsCollection name="clauseList"/>          
        				 </html:select></td>
                           <td><html:textarea property="clauseDescription" cols="53" rows="3" value="<%=blclausedesc%>" onkeyup="toUppercase(this)" readonly="true" styleClass="areahighlightgrey"/></td>
                           <td align="right"><bean:message key="form.FCLPortsConfiguration.FCLSSBLGOCollect" /></td>
    	   <td><html:checkbox property="fclSsBlGoCollect"  name="cubeFclObj" onclick="fclschkall()" ></html:checkbox></td> 
    	   <td align="right"><bean:message key="form.FCLPortsConfiguration.fclHouseBLGoCollect" /> </td>
    	   <td><html:checkbox property="fclHouseBlGoCollect"   name="cubeFclObj" onclick="housechkall()"></html:checkbox></td>
  	 </tr>
                 </table>
                 </td>
                </tr>

                <tr>
                  <td>
	<table width="100%" border="0" >
  	<tr class="style2">
    	<td></td>
    	<td class="style2" scope="col"><div align="left"><bean:message key="form.FCLPortsConfiguration.Rule"/></div></td>
    	<td class="style2" scope="col"><div align="left"><bean:message key="form.FCLPortsConfiguration.Amount(1)" /></div></td>
    	<td class="style2" scope="col"><div align="left"><bean:message key="form.FCLPortsConfiguration.TierAmount"/> </div></td>
                   </tr>
  	<tr>
  		<td  class="style2"><bean:message key="form.FCLPortsConfiguration.RouteByAgentAdmin"/> </td>
    	<td><html:select property="ruleRouteByAgentAdmin" styleClass="verysmalldropdownStyle" value="<%=rule1%>" onchange="enabletieramount1(this)">
      		<html:optionsCollection name="ruleList"/>          
        	</html:select></td>
    	<td><html:text property="amountRouteByAgentAdmin"  maxlength="8" size="8" value="<%=amount1%>" onkeypress="getDecimal(this,5,event)"/></td>
    	<td><html:text property="tierAmountRouteByAgentAdmin" styleId="tieramount1"  maxlength="8" value="<%=tierAmount1 %>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                    </tr>
  	<tr class="style2">
    	<td class="style2"><bean:message key="form.FCLPortsConfiguration.RouteByAgentCommn"/></td>
    	<td><html:select property="ruleRouteByAgentCommn" styleClass="verysmalldropdownStyle" value="<%=rule2%>" onchange="enabletieramount2(this)">
      		<html:optionsCollection name="ruleList"/>          
        	</html:select></td>
    	<td><html:text property="amountRouteByAgentCommn"  maxlength="8" size="8" value="<%=amount2 %>" onkeypress="getDecimal(this,5,event)"/></td>
    	<td><html:text property="tierAmountRouteByAgentCommn" styleId="tieramount2"  maxlength="8" value="<%=tierAmount2%>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
    	<td> </td>

  	</tr>
  	<tr>
    	<td class="style2">Not Routed By Agent Admin</td>
    	<td><html:select property="ruleRouteNotAgentAdmin" styleClass="verysmalldropdownStyle" value="<%=rule3%>" onchange="enabletieramount3(this)">
      		<html:optionsCollection name="ruleList"/>          
        	</html:select></td>
    	<td><html:text property="amountRouteNotAgentAdmin"  maxlength="8" size="8" value="<%=amount3%>" onkeypress="getDecimal(this,5,event)"/></td>
    	<td><html:text property="tierAmountRouteNotAgentAdmin" styleId="tieramount3" maxlength="8" value="<%=tierAmount3%>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
    	<td></td>

  	</tr>
  	<tr>
    	<td class="style2">Not Routed By Agent Commn</td>
    	<td><html:select property="ruleRouteNotAgentCommn" styleClass="verysmalldropdownStyle" value="<%=rule4%>" onchange="enabletieramount4(this)">
      		<html:optionsCollection name="ruleList"/>          
        	</html:select></td>
    	<td><html:text property="amountRouteNotAgentCommn"  maxlength="8" size="8" value="<%=amount4%>" onkeypress="getDecimal(this,5,event)"/></td>
    	<td><html:text property="tierAmountRouteNotAgentCommn" styleId="tieramount4"  maxlength="8" value="<%=tierAmount4%>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
    	<td></td>

 	</tr>
  	<tr>
    	<td class="style2"><bean:message key="form.FCLPortsConfiguration.CurrentAdjFactor"/> </td>

    	<td><html:text property="amountCurrentAdjFactor"  maxlength="5" size="15" value="<%=currentAdjFactor%>" onkeypress="getDecimals(this,1,event)"/></td>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
  	</tr>
                </table>
                </td>
                </tr>

<tr>
  <td>
<table>  
	<tr class="style2">
    	<td><div align="center"></div></td>
  	</tr>
 	<tr class="style2">
    	<td><div align="left"><bean:message key="form.FCLPortsConfiguration.SpecialRemarks" /> </div></td>
    	<td colspan="2" class="style2"><div align="left"><html:checkbox property="specialRemarks"  name="cubeFclObj" onclick="spchkall()" ></html:checkbox></div></td>
    	<td><bean:message key="form.FCLPortsConfiguration.PrintRemarks" /></td>
  	</tr>
  	</table>
</td>
</tr>

<tr>
  <td>
  	<table>
  	<tr class="style2">

    	<td colspan="5">Special Remarks for Quotation </td>
    	<td colspan="6"><html:textarea property="specialRemarksforQuotation" cols="80" rows="4" value="<%=fclPortobj.getSpecialRemarksForQuot() %>" onkeyup="limitText(this.form.specialRemarksforQuotation,this.form.countdown,300)" styleClass="textareastyle"/></td>
  	</tr>
  </table>
  </td>
  </tr>

<tr>
    <td>
    <table width="100%"  border="0">
	<tr class="style2">
  		<td class="style2"><bean:message key="form.FCLPortsConfiguration.QuoteClause" />  </td>
  		<td><html:radio property="quoteClause" value="standard" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
  		<td><bean:message key="form.FCLPortsConfiguration.Standard" /> </td>
  		 <td><bean:message key="form.FCLPortsConfiguration.Validity" /> </td>
  	</tr>
	<tr class="style2">
  		<td ></td>
  		<td><html:radio property="quoteClause" value="temporary" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
  		<td><bean:message key="form.FCLPortsConfiguration.Temporary" /></td>
  		<td><bean:message key="form.FCLPortsConfiguration.validuntilthedatespecified" /> </td>

  	</tr>
	<tr class="style2">
		<td ></td>
		<td><html:radio property="quoteClause" value="none" name="cubeWtMandatoryFclObj" onclick="getradio(this)"></html:radio></td>
		<td><bean:message key="form.FCLPortsConfiguration.None" /></td>
		<td></td>
	</tr>
</table>
</td>
</tr>

<tr>
   <td>
     <table>	
	   <tr id="date">
	 	     <td  class="style2" >FCL Quote Date</td>
            <td  align="left"><html:text property="txtCal" styleId="txtcal" value="<%=txtCal%>"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
       </tr>
     <tr id="text" class="style2">
     	<td>FCL Quote Temporary Remark </td>
  		<td><html:textarea property="temporaryText" value="<%=temporaryText%>" onkeyup="limitText(this.form.temporaryText,this.form.countdown,79)" styleClass="textareastyle"></html:textarea></td>
  	 </tr>
         <tr>
             <td  class="style2">Expiration Date </td>
             <td  align="left"><html:text property="txtCal1" styleId="txtcal1" value="<%=txtCal1%>"/><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);" /></td>

         </tr>
</table>
</td>
</tr>--%>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

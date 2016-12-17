<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
         java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.RefTerminalTemp,com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.FCLPortConfiguration,com.gp.cong.logisoft.domain.AgencyInfo,java.text.SimpleDateFormat,com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="cubeFclObj" class="com.gp.cong.logisoft.struts.form.EditFclPortsConfigForm" scope="request"></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%

String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();
List ruleList=new ArrayList();
List clauseList=new ArrayList();
RefTerminalTemp terminalobj=null;
String terminalName="";
String terminalNo="";
String linemanager="";
List agencyInfoListForFCL = null;

FCLPortConfiguration fclPortObj = null;
User userObj=null;

GenericCode genObjForClauses=null;
String clauses="";
GenericCode genObjRuleRouteByAgentAdmin = null;
GenericCode genObjRuleRouteByAgentCommn = null;
GenericCode genObjRuleRouteNotAgentAdmin = null;
GenericCode genObjRuleRouteNotAgentCommn = null;
String transhipment="";
String defaultDischarge="";
String clauseDesc="";
FCLPortConfiguration cubeWtMandatoryFclObj=new FCLPortConfiguration();

AgencyInfo agencyDefaultObj=new AgencyInfo();
String specialRemarksForQuot="";
String txtCal="";
String temporaryText="";
String originRemarks = "";
String modify="";
String rule1="0";
String rule2="0";
String rule3="0";
String rule4="0";
String amount1="";
String amount2="";
String amount3="";
String amount4="";
String tierAmount1="";
String tierAmount2="";
String tierAmount3="";
String tierAmount4="";
String txtCal1="";
// change from hyd
String srvcFcl="";
String cubeWtMandatoryFcl="";
String fclSsBlGoCollect="";
String currentAdjFactor="";
String fclHouseBlGoCollect="";
String insuranceAllowed="";
String specialRemarks="";
String defaultMasterSettings="";
String brandField="";
 	
 String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
 request.setAttribute("companyCode", companyCode);
     
        if(session.getAttribute("fclPortObjConfiguration")!=null)
        {
        fclPortObj=(FCLPortConfiguration)session.getAttribute("fclPortObjConfiguration");
                if(fclPortObj != null)
                {
                        cubeWtMandatoryFclObj.setQuoteClause(fclPortObj.getQuoteClause());
                        terminalobj = fclPortObj.getTrmnum();
                        if(terminalobj != null)
                        {
                                terminalNo = terminalobj.getTrmnum();
                                terminalName = terminalobj.getTerminalLocation();
                        }
                        /*userObj=fclPortObj.getLineManager();
                        if(userObj != null)
                        {
                                linemanager=userObj.getFirstName()+" "+userObj.getLastName();
                        }*/
                        genObjForClauses=fclPortObj.getBlClauseId();
                        if(genObjForClauses != null)
                        {
                                clauses=genObjForClauses.getId().toString();
                                clauseDesc=genObjForClauses.getCodedesc();
                        }
                        if(fclPortObj.getTemporaryDate()!=null)
                        {
                        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
			
                        txtCal=sdf.format(fclPortObj.getTemporaryDate());
                        }
                        if(fclPortObj.getExpirationDate()!=null)
                        {
                        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");

                        txtCal1=sdf.format(fclPortObj.getExpirationDate());
                        }
                        /*if(fclPortObj.getTranshipment()!=null )
                        if(fclPortObj.getTranshipment()!=null )
        {
         transhipment=fclPortObj.getTranshipment().getShedulenumber()+"-"+fclPortObj.getTranshipment().getPortname();
        }
        if(fclPortObj.getDefaultPortOfDischarge()!=null )
        {
         defaultDischarge=fclPortObj.getDefaultPortOfDischarge().getShedulenumber()+"-"+fclPortObj.getDefaultPortOfDischarge().getPortname();
        }*/
        if(fclPortObj.getTemporaryText()!=null)
        {
        temporaryText=fclPortObj.getTemporaryText();
        }
        if(fclPortObj.getOriginRemarks()!=null)
        {
        originRemarks=fclPortObj.getOriginRemarks();
        }
	
                        genObjRuleRouteByAgentAdmin=fclPortObj.getRadmRule();
                        if(genObjRuleRouteByAgentAdmin != null)
                        {
                                rule1=genObjRuleRouteByAgentAdmin.getId().toString();
                        }
                        genObjRuleRouteByAgentCommn=fclPortObj.getRcomRule();
                        if(genObjRuleRouteByAgentCommn != null)
                        {
                                rule2=genObjRuleRouteByAgentCommn.getId().toString();
                        }
			
                        genObjRuleRouteNotAgentAdmin=fclPortObj.getNadmRule();
                        if(genObjRuleRouteNotAgentAdmin != null)
                        {
                                rule3=genObjRuleRouteNotAgentAdmin.getId().toString();
                        }
                        genObjRuleRouteNotAgentCommn=fclPortObj.getNcomRule();
                        if(genObjRuleRouteNotAgentCommn != null)
                        {
                                rule4=genObjRuleRouteNotAgentCommn.getId().toString();
                        }
                        // change code from hyd.
                        //updated by hyd.
 
    srvcFcl=fclPortObj.getSrvcFcl();
   
    if(srvcFcl!=null && srvcFcl.equals("Y"))
    {
    //cubeFclObj.setSrvcFcl("on");
       cubeFclObj.setSrvcFcl("Y");
    }
    else
    {
    //cubeFclObj.setSrvcFcl("off");
        cubeFclObj.setSrvcFcl("N");
    }
    
    cubeWtMandatoryFcl= fclPortObj.getCubeWtMandatoryFcl();
    if(cubeWtMandatoryFcl!=null && cubeWtMandatoryFcl.equals("Y"))
    {
    //cubeFclObj.setCubeWtMandatoryFcl("on");
    cubeFclObj.setCubeWtMandatoryFcl("Y");
    }
    else{
    //cubeFclObj.setCubeWtMandatoryFcl("off");
    cubeFclObj.setCubeWtMandatoryFcl("N");
    }
    
    fclSsBlGoCollect=fclPortObj.getFclSsBlGoCollect();
    if(fclSsBlGoCollect!=null&& fclSsBlGoCollect.equals("Y"))
    {
    cubeFclObj.setFclSsBlGoCollect("Y");
    }
    else if(fclSsBlGoCollect!=null&& fclSsBlGoCollect.equals("X")){
    cubeFclObj.setFclSsBlGoCollect("X");
    }else{
    cubeFclObj.setFclSsBlGoCollect("N");
    }
    
    fclHouseBlGoCollect=fclPortObj.getFclHouseBlGoCollect();
    if(fclHouseBlGoCollect!=null && fclHouseBlGoCollect.equals("Y"))
    {
     //cubeFclObj.setFclHouseBlGoCollect("on");
     cubeFclObj.setFclHouseBlGoCollect("Y");
    }
    else
    {
     //cubeFclObj.setFclHouseBlGoCollect("off");
     cubeFclObj.setFclHouseBlGoCollect("N");
    }
    insuranceAllowed=fclPortObj.getInsuranceAllowed();
    if(insuranceAllowed.equals("Y")) {
        cubeFclObj.setInsuranceAllowed("Y");
        } else {
        cubeFclObj.setInsuranceAllowed("N");
        }
    specialRemarks=fclPortObj.getSpecialRemarks();
 	 
         if(specialRemarks!=null && specialRemarks.equals("Y"))
         {
          cubeFclObj.setSpecialRemarks("on");
         }
         else
         {
        cubeFclObj.setSpecialRemarks("off");
         }
    defaultMasterSettings=fclPortObj.getDefaultMasterSettings();
    
    if(defaultMasterSettings!=null && defaultMasterSettings.equals("N")){
        cubeFclObj.setDefaultMasterSettings("N");
        }else{
        cubeFclObj.setDefaultMasterSettings("Y");
        }
	brandField = fclPortObj.getBrandField();
            if (brandField != null && brandField.equals("Ecu Worldwide")) {
         cubeFclObj.setBrandField("Ecu Worldwide");
            } else if (brandField != null && brandField.equals("OTI")) {
                cubeFclObj.setBrandField("OTI");
            } else {
         cubeFclObj.setBrandField("Econocaribe");   
        }
                        //End of Rules
                        if(fclPortObj.getRadmAm() != null)
                        {
                                amount1=fclPortObj.getRadmAm().toString();
                        }
                        if(fclPortObj.getRadmTierAmt() != null)
                        {
                                tierAmount1=fclPortObj.getRadmTierAmt().toString();
                        }
                        if(fclPortObj.getRcomAm() != null)
                        {
                                amount2=fclPortObj.getRcomAm().toString();
                        }
                        if(fclPortObj.getRcomTierAmt() !=null)
                        {
                                tierAmount2=fclPortObj.getRcomTierAmt().toString();
                        }
                        if(fclPortObj.getNadmAm() != null)
                        {
                                amount3=fclPortObj.getNadmAm().toString();
                        }
                        if(fclPortObj.getNadmTierAmt() != null)
                        {
                                tierAmount3=fclPortObj.getNadmTierAmt().toString();
                        }
                        if(fclPortObj.getNcomAm() != null)
                        {
                                amount4=fclPortObj.getNcomAm().toString();
                        }
                        if(fclPortObj.getNcomTierAmt() !=null)
                        {
                                tierAmount4=fclPortObj.getNcomTierAmt().toString();
                        }
                        if(fclPortObj.getCurrentAdjFactor() != null)
                        {
                                currentAdjFactor=fclPortObj.getCurrentAdjFactor().toString();
                        }
			
                        if(fclPortObj.getSpecialRemarksForQuot() !=null)
                        {
                                specialRemarksForQuot=fclPortObj.getSpecialRemarksForQuot();
                        }
                        if(fclPortObj.getLineManager() !=null){
                                linemanager=fclPortObj.getLineManager();
                        }
                        if(fclPortObj.getTranshipment() !=null){
                                transhipment=fclPortObj.getTranshipment();
                        }
                        if(fclPortObj.getDefaultPortOfDischarge() !=null){
                                defaultDischarge=fclPortObj.getDefaultPortOfDischarge();
                        }
                }
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
                session.setAttribute("agencyInfoListForFCLAdd",agencyInfoListForFCL);
        }else{
            session.removeAttribute("agencyInfoListForFCLAdd");
        }
	
        agencyDefaultObj.setDefaultValue("N");
        request.setAttribute("agencyDefaultObj",agencyDefaultObj);
        modify = (String) session.getAttribute("modifyforports");
        request.setAttribute("cubeWtMandatoryFclObj",cubeWtMandatoryFclObj);
if (session.getAttribute("view") != null) {
                modify = (String) session.getAttribute("view");
        }
        session.setAttribute("agencyfcl","edit");
        String quoteClause=cubeWtMandatoryFclObj.getQuoteClause();
	
%>

<head>
    <title>FCL Ports Configuration</title>
    <%@include file="../includes/baseResources.jsp" %>
    <script language="javascript" src="<%=path%>/js/common.js"></script>
    <script language="javascript" type="text/javascript">
        function submit1()
        {
 				
            document.EditFclPortsConfig.buttonValue.value="terminalSelected";
            document.EditFclPortsConfig.submit();
        }
        function toUppercase(obj)
        {
            obj.value = obj.value.toUpperCase();
        }
        function getclause()
        {
            document.EditFclPortsConfig.buttonValue.value="clauseSelected";
            document.EditFclPortsConfig.submit();
 				
        }
        var newwindow = '';
        function openAgencyInfo() {
            if (!newwindow.closed && newwindow.location)
            {
                newwindow.location.href = "<%=path%>/jsps/datareference/agencyInfoForFcl.jsp";
            }
            else
            {
                newwindow=window.open("<%=path%>/jsps/datareference/agencyInfoForFcl.jsp","","width=950,height=450");
                if (!newwindow.opener) newwindow.opener = self;
            }
            if (window.focus) {newwindow.focus()}
         
            return false;
        }
        function enabletieramount1(obj)
        {
            if(obj.value=="316" || obj.value=="319")
            {
                var disD=document.getElementById("tieramount1");
                disD.disabled=false;
                disD.className="textlabelsBoldForTextBox";
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
                disD.className="textlabelsBoldForTextBox";
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
                disD.className="textlabelsBoldForTextBox";
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
                disD.className="textlabelsBoldForTextBox";
            }
            else
            {
                var disK=document.getElementById("tieramount4");
 				 	
                disK.disabled=true;
                disK.className="areahighlightgrey";
            }
        }
 			
        function disabled(val1,val2)
        {
            if(val1 == 0 || val1== 3)
            {
                var imgs = document.getElementsByTagName('img');
       
                for(var k=0; k<imgs.length; k++)
                {
                    if(imgs[k].id!="note")
                    {
                        imgs[k].style.visibility = 'hidden';
                    }
                }
                var input = document.getElementsByTagName("input");
                for(i=0; i<input.length; i++)
                {
                    if(input[i].id != "buttonValue" && input[i].name!="terminalName" && input[i].name!="tierAmountRouteByAgentAdmin"
                        && input[i].name!="tierAmountRouteByAgentCommn" && input[i].name!="tierAmountRouteNotAgentAdmin" && input[i].name!="tierAmountRouteNotAgentCommn")
                    {
                        input[i].readOnly=true;
                        input[i].style.color="blue";

                    }
                }
                var textarea = document.getElementsByTagName("textarea");
                for(i=0; i<textarea.length; i++)
                {
	 	
                    if(textarea[i].name!="clauseDescription")
                    {
                        textarea[i].readOnly=true;
                        textarea[i].style.color="blue";


                    }
                }
                var select = document.getElementsByTagName("select");
   		
                for(i=0; i<select.length; i++)
                {
                    select[i].disabled=true;
                    select[i].style.backgroundColor="blue";


	  		
                }
            }
            disabled1(val2);
        }
        function confirmnote()
        {
            document.EditFclPortsConfig.buttonValue.value="note";
            document.EditFclPortsConfig.submit();
        }
        function limitText(limitField, limitNum) {
            limitField.value = limitField.value.toUpperCase();
            if (limitField.value.length > limitNum) {
                limitField.value = limitField.value.substring(0, limitNum);
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
        function disabled1(val)
        {
            if(val=="S")
            {
        <%--document.getElementById("text").style.visibility='hidden';--%>
        <%--document.getElementById("date").style.visibility='visible';--%>

                }
                else if(val=="T")
                {
                    document.getElementById("text").style.visibility='visible';

                    document.getElementById("date").style.visibility='visible';

                }
                else if(val=="N")
                {
                    document.getElementById("text").style.visibility='hidden';

                    document.getElementById("date").style.visibility='hidden';

                }
            }
            // functions for radio buttons ...........
            function chkall(objval1){
                if(objval1.value == "Y")
                {

                    document.EditFclPortsConfig.srvcFcl.value="Y";
    
                    //document.EditFclPortsConfig.srvcFcl.focus();
    
                    //return false;
                }else{
                    document.EditFclPortsConfig.srvcFcl.value="N";
                }
            }
            function cubechkall(objval2){
                if(objval2.value == "Y")
                {
     
                    document.EditFclPortsConfig.cubeWtMandatoryFcl.value="Y";
     
                    //document.EditFclPortsConfig.cubeWtMandatoryFcl.focus();
    
                    //return false;
                }else{
                    document.EditFclPortsConfig.cubeWtMandatoryFcl.value="N";
                }
            }
            function fclschkall(objval4){
                if(objval4.value == "Y")
                {
     
                    document.EditFclPortsConfig.fclSsBlGoCollect.value="Y";
     
                    //document.EditFclPortsConfig.fclSsBlGoCollect.focus();
    
                    //return false;
                }else if(objval4.value == "X"){
                    document.EditFclPortsConfig.fclSsBlGoCollect.value="X";
                }else{
                    document.EditFclPortsConfig.fclSsBlGoCollect.value="N";
                }
            }
            function housechkall(objval3){
                if(objval3.value == "Y")
                {
     
                    document.EditFclPortsConfig.fclHouseBlGoCollect.value="Y";
     
                    //document.EditFclPortsConfig.fclHouseBlGoCollect.focus();
    
                    //return false;
                }else{
                    document.EditFclPortsConfig.fclHouseBlGoCollect.value="N";
                }
            }
            function insuranceAllowed(data){
                if(data.value == "Y"){
                    document.EditFclPortsConfig.insuranceAllowed.value="Y";
                }else{
                    document.EditFclPortsConfig.insuranceAllowed.value="N";
                }
            }
            function brandField(data,companyCode){
               
             if(data.value === "Econocaribe" && companyCode === '03'){
                 document.EditFclPortsConfig.brandField.value="Econocaribe";
             }else if(data.value === "OTI" && companyCode === '02'){
             document.EditFclPortsConfig.brandField.value="OTI"; 
             }else{
                document.EditFclPortsConfig.brandField.value="Ecu Worldwide"; 
             }
         }
            function spchkall(){
                if(document.EditFclPortsConfig.specialRemarks.checked)
                {
     
                    document.EditFclPortsConfig.specialRemarks.value="Y";
     
                    document.EditFclPortsConfig.specialRemarks.focus();
    
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

                document.EditFclPortsConfig.submit();
                return false;
            }
            function addAgencyInfo(){
                var schnum = document.getElementById('schedNum').value;
                GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForFcl.jsp?relay=add&schnum='+schnum,400,900)
            }
            function editFclPortsConfig(){
                //alert("I am MAin Method");
                window.location.href="<%=path%>/jsps/datareference/editFclPortsConfig.jsp";
            }
            function isDecimal(obj){
                if(/[^0-9.]+|\d{1,2}$/.test(obj.value)){
                    obj.value=obj.value.replace(/[^0-9.]+/g,'');
                    if(obj.value.indexOf(".")>=0){
                        var values = obj.value.split(".");
                        if(values[1].length>=2){
                            obj.value=values[0]+"."+values[1].substr(0,2)
                        }
                    }
                }
                else{
                    obj.value=obj.value.replace(/[^0-9.]+/g,'');
                }
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
    <body  class="whitebackgrnd" onload="disabled('<%=modify%>','<%=quoteClause%>')" onkeydown="preventBack()">
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
        <html:form action="/editFclPortsConfig" name="EditFclPortsConfig" styleId="editFclPortsConfig" type="com.gp.cong.logisoft.struts.form.EditFclPortsConfigForm" scope="request">
            <table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr>
                    <td colspan="2" style="padding-top: 5px;padding-bottom: 5px;">
                        <table  width="100%" cellpadding="2" cellspacing="2" border="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Agency Info
                            <span style="padding-left: 810px;"><input type="button" class="buttonStyleNew" id="agencyInfo" 
                                                                      value="Agency Info" onclick="addAgencyInfo()"/>
                            </span>
                </tr>
                <tr>
                    <td>
                        <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
                            <%
                                        int i = 0;
                                        int k = 0;
                            %>
                            <display:table name="<%=agencyInfoListForFCL%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="portexceptiontable" style="width:100%">
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
                                            if (agencyInfoListForFCL != null && agencyInfoListForFCL.size() > 0) {
                                                AgencyInfo agencyInfoObj = (AgencyInfo) agencyInfoListForFCL.get(i);
                                                if (agencyInfoObj != null) {
                                                    customerObj = agencyInfoObj.getAgentId();
                                                    consigneeObj = agencyInfoObj.getConsigneeId();
                                                    if (customerObj != null) {
                                                        agentAcountNo = customerObj.getAccountno();
                                                        agntName = customerObj.getAccountName();
                                                    }
                                                    if (consigneeObj != null) {
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
                                <display:column title="Default" property="defaultValue">
                                    <bean:message key="form.LCLPortsConfiguration.RadioDisplayTagY" />
                                </display:column>
                                <display:column title="action" >
                                    <input type="button" class="buttonStyleNew" style="width: 50px;" value="Rules" onclick="openAgencyRules('<%=i%>')" >
                                </display:column>
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
<tr class="tableHeadingNew"><td>General Information${ports.id}</td>
    <td align="right"> <input type="button" class="buttonStyleNew" id="note" value="Note"  onclick="confirmnote()" disabled="true"/></td>
</tr>
<tr>
    <td colspan="2" >
        <table width="100%" cellpadding="2" cellspacing="0" border="0">
            <tr>
                <td class="textlabelsBold">Terminal No</td>
                <td class="textlabelsBold">Terminal Location</td>
                <td class="textlabelsBold">Line Manager</td>
                <td class="textlabelsBold">Final Delivery To</td>
                <td class="textlabelsBold">Default Port Of Discharge</td>
            </tr>
            <tr>
                <td><html:text styleClass="textlabelsBoldForTextBox"   property="terminalNo" styleId="terminalNumber" value="<%=terminalNo%>" readonly="true" style="width:110px" />
                    &nbsp;<img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1( '<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button='+'editfcl','windows')" >
                </td>
                <td><html:text  property="terminalName" value="<%=terminalName%>" maxlength="100" styleClass="areahighlightgrey" readonly="true" style="width:120px"/>

                </td>
                <td><html:text styleClass="textlabelsBoldForTextBox" property="lineManager" value="<%=linemanager%>" style="width:120px" />
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
<%--                <tr>
                    <td colspan="2" style="padding-top: 5px;padding-bottom: 5px;">
                        <table  width="100%" cellpadding="2" cellspacing="2" border="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Agency Info
                            <span style="padding-left: 663px;"><input type="button" class="buttonStyleNew" id="agencyInfo" value="Agency Info" onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/agencyInfoForFcl.jsp?relay='+'add',400,660)"/>
                            </span>
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
                                CustomerTemp customerObj = null;
                        CustomerTemp consigneeObj=null;
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
                                        agentAcountNo = customerObj.getAccountNo().toString();
                                        agntName = customerObj.getAccountName();
                                        }
                                if(consigneeObj!=null)
                                {
                                        conAcctNo = consigneeObj.getAccountNo();
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
                                <display:column title="Default" property="defaultValue">
                                    <bean:message key="form.LCLPortsConfiguration.RadioDisplayTagY" />
                                </display:column>
                                <display:column title="action" >
                                    <input type="button" class="buttonStyleNew" style="width: 50px;" value="Rules" onclick="openAgencyRules('<%=i%>')" >
                                </display:column>
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
    </tr>--%>
<tr><td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" >
            <tr class="textlabelsBold">
                <td valign="top">BL Release Clause </td>
                <td valign="top"><html:select property="blClauses" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="<%=clauses%>" onchange="getclause()">
                        <html:optionsCollection name="clauseList"/>
                    </html:select></td>
                <td valign="top"><table><tr class="textlabelsBold">
                            <td><bean:message key="form.FCLPortsConfiguration.CubeandWtMandatoryonFCLB/LS" />(Y/N)</td>
                            <td>
                                <%--<html:checkbox property="cubeWtMandatoryFcl" name="cubeFclObj" onclick="cubechkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox>--%>
                                <html:radio property="cubeWtMandatoryFcl" name="cubeFclObj" onclick="cubechkall(this)" value="Y"/>Yes
                                <html:radio property="cubeWtMandatoryFcl" name="cubeFclObj" onclick="cubechkall(this)" value="N"/>No
                            </td>

                            <td style="width: 60px">
                            </td>
                            <%--<td ><html:checkbox property="fclSsBlGoCollect"  name="cubeFclObj" onclick="fclschkall()" ></html:checkbox></td>--%>
                            <td align="right"><bean:message key="form.FCLPortsConfiguration.FCLSSBLGOCollect" /></td>
                            <td>
                                <html:radio property="fclSsBlGoCollect" name="cubeFclObj" onclick="cubechkall(this)" value="Y"/>Yes
                                <html:radio property="fclSsBlGoCollect" name="cubeFclObj" onclick="cubechkall(this)" value="N"/>No
                                <html:radio property="fclSsBlGoCollect" name="cubeFclObj" onclick="cubechkall(this)" value="X"/>Collect
                            </td>
                            <%--<td align="right">
                                <html:select property="fclSsBlGoCollect" style="width:120px;" styleClass="verysmalldropdownStyleForText" value="${fclPortObj.fclSsBlGoCollect}" name="cubeFclObj">
                                      <html:optionsCollection name="SSBLGOCollectList"/>
                                </html:select>
                            </td>--%>
                        </tr></table></td>
            </tr>
            <tr class="textlabelsBold">
                <td valign="top"><bean:message key="form.FCLPortsConfiguration.ClauseDescription" /> </td>
                <td valign="top"><html:textarea   property="clauseDescription" styleId="clauseDesc" cols="53" rows="4"  value="<%=clauseDesc%>"  onkeyup="toUppercase(this)" readonly="true" styleClass="textareahighlightgrey"/></td>
                <td valign="top"><table border="0"><tr class="textlabelsBold">
                            <td ><bean:message key="form.FCLPortsConfiguration.fclHouseBLGoCollect" />(Y/N)</td>
                            <td >
                                <%-- <html:checkbox property="fclHouseBlGoCollect"   name="cubeFclObj" onclick="housechkall()" onmouseover="tooltip.show('<strong>Check For Yes and UnCheck For No</strong>');" onmouseout="tooltip.hide();"></html:checkbox>--%>
                                <html:radio property="fclHouseBlGoCollect" name="cubeFclObj" value="Y" onclick="housechkall(this)"/>Yes
                                <html:radio property="fclHouseBlGoCollect" name="cubeFclObj" value="N" onclick="housechkall(this)"/>No
                            </td>
                            <td style="width:120px"></td>
                            <td align="right" colspan="2"><bean:message key="form.FCLPortsConfiguration.insuranceAllowed" />
                                <html:radio property="insuranceAllowed" name="cubeFclObj" value="Y" onclick="insuranceAllowed(this)"/>Yes
                                <html:radio property="insuranceAllowed" name="cubeFclObj" value="N" onclick="insuranceAllowed(this)"/>No
                            </td>

                        </tr><tr class="textlabelsBold">
                            <td valign="top"><bean:message key="form.FCLPortsConfiguration.ServiceFCL" />(Y/N)</td>
                            <td valign="top" >
                                <%--<html:checkbox property="srvcFcl" name="cubeFclObj" onclick="chkall()"></html:checkbox>--%>
                                <html:radio property="srvcFcl" name="cubeFclObj" value="Y" onclick="chkall(this)"/>Yes
                                <html:radio property="srvcFcl" name="cubeFclObj" value="N" onclick="chkall(this)"/>No
                            </td>

                            <td>
                            </td>
                            <td><bean:message key="form.FCLPortsConfiguration.CurrentAdjFactor"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <html:text styleClass="textlabelsBoldForTextBox" property="amountCurrentAdjFactor"  maxlength="5" size="5" value="<%=currentAdjFactor%>" onkeyup="isDecimal(this)"/></td>
                            <td><bean:message key="form.FCLPortsConfiguration.UseDefaultMasterSettings"/></td>
                            <td>
                                <html:radio property="defaultMasterSettings" name="cubeFclObj" value="Y"/>Yes
                                <html:radio property="defaultMasterSettings" name="cubeFclObj" value="N"/>No
                            </td>

                        </tr>
                        <tr class="textlabelsBold">
                            <td valign ="top"><bean:message key="form.FCLPortsConfiguration.Brandfield" /></td>
                            <td>
                        <c:choose>
                            <c:when test="${companyCode == '02'}">
                                 <html:radio property="brandField" name="cubeFclObj" value="OTI" onclick="brandField(this,'${companyCode}')"/>OTI 
                           </c:when>
                            <c:otherwise>
                                <html:radio property="brandField" name="cubeFclObj" value="Econocaribe" onclick="brandField(this,'${companyCode}')"/>Econocaribe 
                            </c:otherwise>
                          </c:choose>
                                <html:radio property="brandField" name="cubeFclObj" value="Ecu Worldwide" onclick="brandField(this,'${companyCode}'})"/> Ecu Worldwide
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
                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteByAgentAdmin"  maxlength="8" size="8" value="<%=amount1%>" onkeyup="isDecimal(this)"/></td>
                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteByAgentAdmin" styleId="tieramount1" maxlength="8" value="<%=tierAmount1 %>" disabled="true"  styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                        <tr></table></td>
            </tr>
            <tr class="textlabelsBold">
                <td><bean:message key="form.FCLPortsConfiguration.RouteByAgentCommn"/></td>
                <td><html:select property="ruleRouteByAgentCommn" styleClass="verysmalldropdownStyleForText" value="<%=rule2%>" onchange="enabletieramount2(this)">
                        <html:optionsCollection name="ruleList"/>
                    </html:select></td>
                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold">
                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteByAgentCommn"  maxlength="8" size="8" value="<%=amount2 %>"  onkeyup="isDecimal(this)"/></td>
                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteByAgentCommn"  maxlength="8" styleId="tieramount2" value="<%=tierAmount2 %>" disabled="true" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                        <tr></table></td>
            </tr>
            <tr class="textlabelsBold">
                <td>Not Routed By Agent Admin</td>
                <td><html:select property="ruleRouteNotAgentAdmin" styleClass="verysmalldropdownStyleForText" value="<%=rule3%>"  onchange="enabletieramount3(this)">
                        <html:optionsCollection name="ruleList"/>
                    </html:select></td>
                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold"><tr>
                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteNotAgentAdmin"  maxlength="8" size="8" value="<%=amount3%>" onkeyup="isDecimal(this)"/></td>
                            <td style="padding-right: 120px;"><html:text  property="tierAmountRouteNotAgentAdmin"  maxlength="8"  value="<%=tierAmount3%>" disabled="true" styleId="tieramount3" styleClass="areahighlightgrey" onkeypress="getDecimal(this,5,event)"/></td>
                        </tr></table></td>
            </tr>
            <tr class="textlabelsBold">
                <td>Not Routed BY Agent Commn</td>
                <td><html:select property="ruleRouteNotAgentCommn" styleClass="verysmalldropdownStyleForText" value="<%=rule4%>"  onchange="enabletieramount4(this)">
                        <html:optionsCollection name="ruleList"/>
                    </html:select></td>
                <td colspan="4" valign="top"><table border="0" width="100%"><tr class="textlabelsBold"><tr>
                            <td><html:text styleClass="textlabelsBoldForTextBox" property="amountRouteNotAgentCommn"  maxlength="8" size="8" value="<%=amount4%>" onkeyup="isDecimal(this)"/></td>
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
                    <html:textarea  property="temporaryText" styleId="tempRemark" rows="3" cols="110" value="<%=temporaryText%>" onkeyup="limitText(this,79)" styleClass="textareastyle"></html:textarea>
                </td>
            </tr>
            <tr class="textlabelsBold" id="text">
                <td valign="top">Origin Quote Remarks </td>
                <td colspan="5">
                    <html:textarea  property="originRemarks" styleId="quoteRemarks" rows="3" cols="110" value="<%=originRemarks%>" onkeyup="limitText(this,200)" styleClass="textareastyle"></html:textarea>
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
                <td colspan="5"><html:textarea  property="specialRemarksforQuotation" styleId="griRemarks" cols="110" rows="3" value="<%=specialRemarksForQuot%>"   onkeyup="limitText(this,320)" styleClass="textareastyle"/></td>
            </tr>
            <tr>
                <td colspan="6">
                    &nbsp;
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>

<input type="hidden" value="${ports.id}" id="schedNum"/>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<script>
    function openAgencyRules(id){
        GB_show('AgencyRules', '<%=path%>/agencyRules.do?search=get&index='+id,310,660)
    }
</script>
</html:form>		
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

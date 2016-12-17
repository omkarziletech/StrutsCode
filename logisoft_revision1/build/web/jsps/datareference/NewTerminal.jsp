<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,
         com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.hibernate.dao.UnLocationDAO,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<jsp:useBean id="termForm" class="com.gp.cong.logisoft.struts.form.NewTerminalForm" scope="request"></jsp:useBean>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            DBUtil dbUtil = new DBUtil();
            request.setAttribute("terminaltypelist", dbUtil.getGenericCodeList(18, "yes", "Select Terminal Type"));
//request.setAttribute("countrylist",dbUtil.getGenericCodeList(11,"yes","Select Country"));
            request.setAttribute("acflist", dbUtil.getacfList());

            RefTerminal terminal = new RefTerminal();
            termForm.setAirsrvc("off");
            //request.getAttribute("airsrvc");
            request.setAttribute("term", terminal);

            String countryId = "";
            String cityId = "";
            String unLoc = "";
            String state = "";
            String terminalType = "0";
            String chargeCode = "";
            String brlChgCode = "";
            String over10kchgcode = "";
            String over20kchgcode = "";
            String docChgCode = "";
//String extension1="";
//String extension2="";
//String extension3="";
            String unLocationCode1 = "";
            String message = "";
            String Termianname = "";
            String acctno = "";
            String docDeptEmail = "";
            String exportsBillingTerminalEmail = "";
            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            if (session.getAttribute("terminal") != null) {
                terminal = (RefTerminal) session.getAttribute("terminal");

                if (terminal.getAirsrvc() != null && terminal.getAirsrvc().equals("Y")) {
                    termForm.setAirsrvc("on");

                } else {
                    termForm.setAirsrvc("off");
                }

                if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getCountryId() != null) {
                    countryId = ((GenericCode) terminal.getUnLocation().getCountryId()).getCodedesc();
                }

                if (terminal != null) {
                    Termianname = terminal.getTrmnam();
                }
                if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getStateId() != null && terminal.getUnLocation().getStateId().getCode() != null) {
                    state = ((GenericCode) terminal.getUnLocation().getStateId()).getCode();
                }
                if (terminal != null && terminal.getUnLocation() != null && terminal.getUnLocation().getUnLocationName() != null) {
                    cityId = terminal.getUnLocation().getUnLocationName();
                }
                if (terminal != null && terminal.getGenericCode() != null && terminal.getGenericCode().getCodedesc() != null) {
                    terminalType = terminal.getGenericCode().getId().toString();
                }
                if (terminal != null && terminal.getGenericCode1() != null && terminal.getGenericCode1().getCode() != null) {
                    chargeCode = terminal.getGenericCode1().getCode().toString();
                }
                if (terminal != null && terminal.getGenericCode2() != null && terminal.getGenericCode2().getCode() != null) {
                    brlChgCode = terminal.getGenericCode2().getCode();
                }
                if (terminal != null && terminal.getGenericCode3() != null && terminal.getGenericCode3().getCode() != null) {
                    over10kchgcode = terminal.getGenericCode3().getCode();
                }
                if (terminal != null && terminal.getGenericCode4() != null && terminal.getGenericCode4().getCode() != null) {
                    over20kchgcode = terminal.getGenericCode4().getCode();
                }
                if (terminal != null && terminal.getGenericCode5() != null && terminal.getGenericCode5().getCode() != null) {
                    docChgCode = terminal.getGenericCode5().getCode();
                }
                if (terminal.getUnLocCode() != null) {
                    unLoc = terminal.getUnLocCode();
                }
                if (terminal.getUnLocationCode1() != null) {
                    unLocationCode1 = terminal.getUnLocationCode1();
                }
                if (terminal.getTpacctno() != null) {
                    acctno = terminal.getTpacctno();
                }
                if (terminal.getExportsBillingTerminalEmail() != null) {
                    exportsBillingTerminalEmail = terminal.getExportsBillingTerminalEmail().toLowerCase();
                }
            }

%>
<html> 
    <head>
        <base href="<%=basePath%>">
        <title>JSP for TerminalManagementForm form</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">

        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script language="javascript">
            start = function(){
                serializeForm();
            }
            window.onload = start;
        </script>

        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript" src="<%=path%>/windowfiles/dhtmlwindow.js"></script>

        <script >
            function openmypage(){ //Define arbitrary function to run desired DHTML Window widget codes
                ajaxwin=dhtmlwindow.open("ajaxbox", "ajax", "<%=path%>/jsps/datareference/searchpopup.jsp?button='+'searchterminalcity'", "#3: Ajax Win Title", "width=450px,height=300px,left=300px,top=100px,resize=1,scrolling=1")
                ajaxwin.onclose=function(){return window.confirm("Close window 3?")} //Run custom code when window is about to be closed
            }
        </script>

        <script type="text/javascript">
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
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

                document.newTerminalForm.submit();
                return false;
            }
            function save()
            {
                if(document.newTerminalForm.termNo.value=="")
                {
                    alert("Please enter the term no");
                    document.newTerminalForm.termNo.value="";
                    document.newTerminalForm.termNo.focus();
                    return;
                }
                var val=document.newTerminalForm.termNo.value
                if(val.match(" "))
                {
                    alert("WhiteSpace is not allowed for term no");
                    return;
                }
                if(isAlpha(document.newTerminalForm.termNo.value)==false)
                {
                    alert("Special Characters not allowed for Term No.");
                    document.newTerminalForm.termNo.value="";
                    document.newTerminalForm.termNo.focus();
                    return;
                }
                /*if(document.newTerminalForm.name.value=="")
        {
                alert("Please enter the term name");
                document.newTerminalForm.name.value="";
                document.newTerminalForm.name.focus();
                return;
        }*/
	
                /*if(document.newTerminalForm.terminalType.value=="0")
        {
                alert("Please enter the terminal type");
                document.newTerminalForm.terminalType.value="";
                document.newTerminalForm.terminalType.focus();
                return;
        }
                 */
                /*if(document.newTerminalForm.city.value=="")
        {
                alert("Please enter the city");
                document.newTerminalForm.city.value="";
                document.newTerminalForm.city.focus();
                return;
        }
        if(document.newTerminalForm.addressLine1.value=="")
        {
                alert("Please enter the Address1");
                document.newTerminalForm.addressLine1.value="";
                document.newTerminalForm.addressLine1.focus();
                return;
        }*/
                if(document.newTerminalForm.addressLine1.value.length>50)
                {
                    alert("Please enter the Address1");
                    document.newTerminalForm.addressLine1.value="";
                    document.newTerminalForm.addressLine1.focus();
                    return;
                }
                if(document.newTerminalForm.addressLine2.value!="" && document.newTerminalForm.addressLine2.value.length>26)
                {
                    alert("Please enter the Address2");
                    document.newTerminalForm.addressLine2.value="";
                    document.newTerminalForm.addressLine2.focus();
                    return;
                }
                if(IsNumeric(document.newTerminalForm.zip.value)==false)
                {
                    alert("Zipcode should be Numeric.");
                    document.newTerminalForm.zip.value="";
                    document.newTerminalForm.zip.focus();
                    return;
                }
                //   var value=document.newTerminalForm.phoneNo1.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Phone number1 dont start with white space");
                //					return;
                //				}
                //			}
  
                if(IsNumeric(document.newTerminalForm.phoneNo1.value.replace(/ /g,''))==false)
                {
                    alert("Telephone Number1 should be Numeric.");
                    document.newTerminalForm.phoneNo1.value="";
                    document.newTerminalForm.phoneNo1.focus();
                    return;
                }
   
                //   var value=document.newTerminalForm.phoneNo2.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Phone number2 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.phoneNo2.value.replace(/ /g,''))==false)
                {
                    alert("Telephone Number2 should be Numeric.");
                    document.newTerminalForm.phoneNo2.value="";
                    document.newTerminalForm.phoneNo2.focus();
                    return;
                }
   
                //   var value=document.newTerminalForm.phoneNo3.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Phone number3 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.phoneNo3.value.replace(/ /g,''))==false)
                {
                    alert("Telephone Number3 should be Numeric.");
                    document.newTerminalForm.phoneNo3.value="";
                    document.newTerminalForm.phoneNo3.focus();
                    return;
                }
     
                //     var value=document.newTerminalForm.faxNo1.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Fax number1 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.faxNo1.value.replace(/ /g,''))==false)
                {
                    alert("Fax Number should be Numeric.");
                    document.newTerminalForm.faxNo1.value="";
                    document.newTerminalForm.faxNo1.focus();
                    return;
                }
     
                //   var value=document.newTerminalForm.faxNo2.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Fax number2 dont start with white space");
                //					return;
                //				}
                //			}
                if(IsNumeric(document.newTerminalForm.faxNo2.value.replace(/ /g,''))==false)
                {
                    alert("Fax Number should be Numeric.");
                    document.newTerminalForm.faxNo2.value="";
                    document.newTerminalForm.faxNo2.focus();
                    return;
                }
     
   
 
                //   var value=document.newTerminalForm.faxNo3.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Fax number3 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.faxNo3.value.replace(/ /g,''))==false)
                {
                    alert("Fax Number3 should be Numeric.");
                    document.newTerminalForm.faxNo3.value="";
                    document.newTerminalForm.faxNo3.focus();
                    return;
                }
     
                //   var value=document.newTerminalForm.faxNo4.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Fax number4 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.faxNo4.value.replace(/ /g,''))==false)
                {
                    alert("Fax Number should be Numeric.");
                    document.newTerminalForm.faxNo4.value="";
                    document.newTerminalForm.faxNo4.focus();
                    return;
                }
                /*   if(document.newTerminalForm.faxNo4.value!="" && document.newTerminalForm.faxNo4.value.length<13)
   {
   alert("Fax Number should be 13 Digits");
   document.newTerminalForm.faxNo4.value="";
   document.newTerminalForm.faxNo4.focus();
   return;
   } */
                //   var value=document.newTerminalForm.faxNo5.value;
                //
                //			for(var i=0;i< value.length;i++)
                //			{
                //				if(value.indexOf(" ") != -1)
                //				{
                //					alert("Please Fax number5 dont start with white space");
                //					return;
                //				}
                //			}
   
                if(IsNumeric(document.newTerminalForm.faxNo5.value.replace(/ /g,''))==false)
                {
                    alert("Fax Number should be Numeric.");
                    document.newTerminalForm.faxNo5.value="";
                    document.newTerminalForm.phoneNo3.focus();
                    return;
                }
   
                if(isAlpha(document.newTerminalForm.govSchCode.value)==false)
                {
                    alert("Special Characters not allowed for GovSchCode.");
                    document.newTerminalForm.govSchCode.value="";
                    document.newTerminalForm.govSchCode.focus();
                    return;
                }
                if(document.newTerminalForm.notes.value!="" && document.newTerminalForm.notes.value.length>140)
                {
                    alert("Notes should be 140 letters");
                    document.newTerminalForm.notes.value="";
                    document.newTerminalForm.notes.focus();
                    return;
                }
                document.newTerminalForm.buttonValue.value="add";
                document.newTerminalForm.submit();
            }
            // from hyd. to clear the textfields
            function clearDefault(el) {
                //alert("from the function");
                if (el.value == "")
                {
                    document.newTerminalForm.state.value="";
                    document.newTerminalForm.country.value="";
                }
            }
   
            function terminalcontact()
            {
                /*if(document.newTerminalForm.termNo.value=="")
        {
                alert("Please enter the term no");
                document.newTerminalForm.termNo.value="";
                document.newTerminalForm.termNo.focus();
                return;
        }
        var val=document.newTerminalForm.termNo.value
           if(val.match(" "))
           {
                alert("WhiteSpace is not allowed for term no");
                return;
           }
   if(isAlpha(document.newTerminalForm.termNo.value)==false)
   {
         alert("Special Characters not allowed for Term No.");
                 document.newTerminalForm.termNo.value="";
         document.newTerminalForm.termNo.focus();
         return;
   } 
   if(document.newTerminalForm.name.value=="")
        {
                alert("Please enter the term name");
                document.newTerminalForm.name.value="";
                document.newTerminalForm.name.focus();
                return;
        }
        var val=document.newTerminalForm.name.value
           if(val.match(" "))
           {
                alert("WhiteSpace is not allowed for term name");
                return;
           }
   if(isSpecial(document.newTerminalForm.name.value)==false)
   {
         alert("Special Characters not allowed for Term Name.");
                 document.newTerminalForm.name.value="";
         document.newTerminalForm.name.focus();
         return;
   }   
   if(document.newTerminalForm.terminalType.value=="0")
        {
                alert("Please select the terminal type");
                document.newTerminalForm.terminalType.value="";
                document.newTerminalForm.terminalType.focus();
                return;
        }  */
                document.newTerminalForm.buttonValue.value="terminalcontact";
                document.newTerminalForm.submit();
            }
            function chkall(){
                if(document.newTerminalForm.airsrvc.checked)
                {
     
                    document.newTerminalForm.airsrvc.value="Y";
     
                    document.newTerminalForm.airsrvc.focus();
    
                    return false;
                }
            }
    
            function cancel()
            {
                document.newTerminalForm.buttonValue.value="cancel";
                var result = confirm("Would you like to save the changes ? ");
                if(result)
                {
                    save();
                }
                document.newTerminalForm.submit();
            }
            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                } else {
                    limitCount.value = limitNum - limitField.value.length;
                }
 
            }
 
            function searchcity(){
 
                document.newTerminalForm.buttonValue.value="searchcity";
                document.newTerminalForm.submit();
            }
 
            /*function phonevalid(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.phoneNo1.value.length>10) || IsNumeric(document.newTerminalForm.phoneNo1.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.phoneNo1.value="";
                        document.newTerminalForm.phoneNo1.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}
 
 function phonevalid1(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.phoneNo2.value.length>10) || IsNumeric(document.newTerminalForm.phoneNo2.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.phoneNo2.value="";
                        document.newTerminalForm.phoneNo2.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function phonevalid2(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.phoneNo3.value.length>10) || IsNumeric(document.newTerminalForm.phoneNo3.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.phoneNo3.value="";
                        document.newTerminalForm.phoneNo3.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function faxvalid(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.faxNo1.value.length>10) || IsNumeric(document.newTerminalForm.faxNo1.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.faxNo1.value="";
                        document.newTerminalForm.faxNo1.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function faxvalid1(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.faxNo2.value.length>10) || IsNumeric(document.newTerminalForm.faxNo2.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.faxNo2.value="";
                        document.newTerminalForm.faxNo2.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function faxvalid2(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.faxNo3.value.length>10) || IsNumeric(document.newTerminalForm.faxNo3.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.faxNo3.value="";
                        document.newTerminalForm.faxNo3.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function faxvalid3(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.faxNo4.value.length>10) || IsNumeric(document.newTerminalForm.faxNo4.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.faxNo4.value="";
                        document.newTerminalForm.faxNo4.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}   
function faxvalid4(obj)
{ 
if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
{
    
                if((document.newTerminalForm.faxNo5.value.length>10) || IsNumeric(document.newTerminalForm.faxNo5.value.replace(/ /g,''))==false)
                {
                 alert("please enter the only 10 digits and numerics only");
                  document.newTerminalForm.faxNo5.value="";
                        document.newTerminalForm.faxNo5.focus();
                 }//document.addWarehouse.phone.value.length=6;
}
 else{
 getIt(obj);
  
}
}*/
            function zipcode1(obj)
            {
                if(document.newTerminalForm.country.value=="" && document.newTerminalForm.country.value!="UNITED STATES")
                {
  
                    if((document.newTerminalForm.zip.value.length>5) || IsNumeric(document.newTerminalForm.zip.value.replace(/ /g,''))==false)
                    {
                        alert("please enter the only 5 digits and numerics only");
                        document.newTerminalForm.zip.value="";
                        document.newTerminalForm.zip.focus();
                    }//document.addWarehouse.phone.value.length=6;
                }
                else{
                    getzip(obj);
  
                }
            }
            function getPortName(ev)
            {
                if(event.keyCode==9 || event.keyCode==13){
                    var params = new Array();
                    params['requestFor'] = "ScheduleCode";
                    params['scheduleCode'] = document.newTerminalForm.govSchCode.value;
                    var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populatePortName");
                }
            }
            function populatePortName(type, data, evt) {
                if(data){
                    document.getElementById("terminalLocation").value=data.portname;
                    if(data.scheduleSuffix){
                        document.getElementById("scheduleSuffix").value=data.scheduleSuffix;
                    }else{
                        document.getElementById("scheduleSuffix").value="";
                    }
   		
                }
            }
            function getCountry(ev){
                if(event.keyCode==9 || event.keyCode==13){
			   
                    var params = new Array();
                    params['requestFor'] = "country";
                    params['city'] = document.newTerminalForm.city.value;
			
                    var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
				 
                    var req = dojo.io.bind(bindArgs);
			 
                    dojo.event.connect(req, "load", this, "populateCountryAndState");
				
                }
            }
			 
            function populateCountryAndState(type, data, evt) {
                if(data){
                    document.getElementById("country").value=data.country;
                    if(data.state){
                        document.getElementById("state").value=data.state;
                    }else{
                        document.getElementById("state").value="";
                    }
                }
            }
            
            function fillCountryAndState(){
                var array = new Array();
                var city = document.newTerminalForm.city.value;
                array = city.split('/');
                document.newTerminalForm.city.value = array[0];
                document.newTerminalForm.country.value = array[1];
                if(undefined != array[2] && null != array[2]){
                    document.newTerminalForm.unLocCode.value = array[2];
                }else{
                    document.newTerminalForm.state.value = array[2];
                }
                if(undefined != array[3] && null != array[3]){
                    document.newTerminalForm.state.value = array[3];
                }

            }
            function check(){
                var first=new Array();
                var second;
                var third=new Array();
                var acctno = document.newTerminalForm.acctno.value;
                first=acctno.split('>');
                second=first[1];
                third=second.split(',');
                document.newTerminalForm.acctno.value=third[0];
            }
             function addTerminalManagers(turmNo) {
                var url = "${path}" + "/terminalManagement.do?buttonValue=addTerminalUser&turmNo="+turmNo;
                GB_show("TerminalManager", url + "&", 400, 800);
            }
        </script>
    </head>
    <body class="whitebackgrnd"  >
        <html:form action="/newTerminal"  name="newTerminalForm" type="com.gp.cong.logisoft.struts.form.NewTerminalForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Terminal Detail</td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" id="managers" value="TerminalManagers" onclick="addTerminalManagers('<%=terminal.getTrmnum()%>')">
                        <input type="button" class="buttonStyleNew" value="Save" onclick="save()"/>
                        <input type="button" class="buttonStyleNew" value="Go Back" onclick="cancel()"/></td>
                    
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr class="textlabels">
                                <td >Terminal No<br></td>
                                <td><html:text property="termNo" value="<%=terminal.getTrmnum()%>" styleClass="varysizeareahighlightgrey" readonly="true"
                                           onkeyup="toUppercase(this)" maxlength="5" style="width:118px"/></td>
                                <td class="textlabels">Account Number<br></td>
                                <td><input name="acctno" id="acctno" value="<%=acctno%>" style="width:100px"/>
                                    <input type="hidden" id="acctno_check" value="<%=acctno%>"/>
                                    <div id="acctno_choices"  style="display: none; width:200px;" width="auto" align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("acctno","acctno_choices","","",
                                        "<%=path%>/actions/getTradingPartnerAccountNo.jsp?tabName=TERMINAL&from=0","check()","");
                                    </script>
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr class="textlabels">
                                <td>Gov Sch code</td>
                                <td><input name="govSchCode" id="govSchCode" value="<%if (terminal.getGovSchCode() != null) {
                                                terminal.getGovSchCode();
                                            }%>" onkeydown="getPortName(this.value)" style="width:118px" maxlength="5"/>
                                    <%--		<dojo:autoComplete formId="newTerminalForm" textboxId="govSchCode" action="<%=path%>/actions/getPorts.jsp?tabName=NEW_TERMINAL" /></td>--%>
                                <td>Terminal Location </td>
                                <td width="100px">
                                    <table>
                                        <tr>
                                            <td>
                                                <html:text property="terminalLocation"  value="<%=terminal.getTerminalLocation()%>"  maxlength="50" style="width:118px"/>
                                            </td>
                                            <td class="textlabels">
                                                UNLOC
                                            </td>
                                            <td><input name="unLocationCode1" id="unLocationCode1" value="<%=unLocationCode1%>"  style="width:50px"/>
                                                <input type="hidden" id="unLocationCode1_check" value="<%=unLocationCode1%>"/>
                                                <div id="unLocationCode1_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("unLocationCode1","unLocationCode1_choices","","unLocationCode1_check",
                                                    "<%=path%>/actions/getPorts.jsp?tabName=NEW_TERMINAL&from=0&isDojo=false","");
                                                </script>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td class="textlabels">Terminal Type</td>
                                <td colspan="2"><html:select property="terminalType" styleClass="selectboxstyle" value="<%=terminalType%>" style="width:118px">
                                        <html:optionsCollection name="terminaltypelist"/> </html:select></td>
                                </tr>
                                <tr>
                                    <td class="textlabels">Schedule Suffix </td>
                                    <td><html:text property="scheduleSuffix" value="<%=terminal.getScheduleSuffix()%>" readonly="true" styleClass="varysizeareahighlightgrey" style="width:118px"/></td>
                                <td class="textlabels">Phone Number</td>
                                <td><html:text property="phoneNo1" value="<%=terminal.getPhnnum1()%>"  maxlength="20" styleClass="areahighlightwhite" size="7"/>
                                    <html:text property="extension1" value="<%=terminal.getExtension1()%>" maxlength="4" size="2" styleClass="areahighlightwhite"/></td>
                                <td class="textlabels">Care Of </td>
                                <td><html:text property="careof" value="<%=terminal.getCareof()%>"  onkeyup="toUppercase(this)" maxlength="35" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Name</td>
                                <td><html:text property="name" value="<%= Termianname%>" styleClass="areahighlightyellow1" onkeyup="toUppercase(this)" maxlength="30" style="width:118px"/></td>
                                <td class="textlabels" >Address Line1</td>
                                <td><html:text property="addressLine1" value="<%=terminal.getAddres1()%>" styleClass="areahighlightyellow1"
                                           onkeyup="toUppercase(this)" maxlength="25" style="width:118px"/> </td>
                                <td class="textlabels">Address Line2 </td>
                                <td><html:text property="addressLine2"  styleClass="areahighlightyellow1"  value="<%=terminal.getAddres2()%>" onkeyup="toUppercase(this)" maxlength="25" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">City &nbsp; </td>
                                <td width="100px">
                                    <table>
                                        <tr>
                                            <td>
                                                <input  name="city" id="city" value="<%=cityId%>"  style="width:118px"/>
                                                <%--                             <dojo:autoComplete formId="newTerminalForm" textboxId="city" action="<%=path%>/actions/getCity.jsp?tabName=NEW_TERMINAL&from=0"/>--%>
                                                <%--                               <input type="hidden" id="city_check" value="<%=cityId %>"/>--%>
                                                <div id="city_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("city","city_choices","","",
                                                    "<%=path%>/actions/getCity.jsp?tabName=NEW_TERMINAL&from=0","fillCountryAndState()","");
                                                </script>
                                            </td>
                                            <td class="textlabels">
                                                UNLOC
                                            </td>
                                            <td><input name="unLocCode" id="unLocCode" value="<%=unLoc%>"  style="width:50px"/>
                                                <input type="hidden" id="unLocCode_check" value="<%=unLoc%>"/>
                                                <div id="unLocCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("unLocCode","unLocCode_choices","","unLocCode_check",
                                                    "<%=path%>/actions/getPorts.jsp?tabName=NEW_TERMINAL&from=0&isDojo=false","");
                                                </script>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td class="textlabels">Country</td>
                                <td><html:text property="country" value="<%=countryId%>" styleClass="varysizeareahighlightgrey" readonly="true" style="width:118px"></html:text></td>
                                <td class="textlabels">Zip</td>
                                <td><html:text property="zip" value="<%=terminal.getZipcde()%>"  maxlength="10" onkeyup="zipcode1(this)" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">State</td>
                                <td ><html:text property="state" value="<%=state%>" styleClass="varysizeareahighlightgrey" readonly="true" style="width:118px"/></td>
                                <td class="textlabels">ImportsCont Email</td>
                                <td class="textlabels"><html:text property="importsContacts"  value="<%=terminal.getImportsContactEmail()%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">General Ledger#</td>
                                <td><html:text property="generalLedger"  value="<%=terminal.getLedgerNo()%>" onkeypress="return checkIts(event)" maxlength="2" style="width:118px"/></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Import Doc</td>
                                <td><html:text property="phoneNo2" value="<%=terminal.getPhnnum2()%>"  maxlength="20" styleClass="areahighlightwhite" size="7"/>
                                    <html:text property="extension2" value="<%=terminal.getExtension2()%>" maxlength="4" size="2" styleClass="areahighlightwhite"/></td>
                                <td class="textlabels">Fax Number</td>
                                <td valign="top">
                                    <html:text property="faxNo1" style="width:118px" value="<%=terminal.getFaxnum1()%>" maxlength="20" /></td>
                                <td class="textlabels">Import Doc</td>
                                <td><html:text property="faxNo4" style="width:118px" value="<%=terminal.getFaxnum4()%>"  maxlength="20" />
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td><html:text property="phoneNo3" value="<%=terminal.getPhnnum3()%>"  maxlength="20" styleClass="areahighlightwhite" size="7"/>
                                    <html:text property="extension3" value="<%=terminal.getExtension3()%>" maxlength="4" size="2" styleClass="areahighlightwhite"/></td>
                                <td>&nbsp;</td>
                                <td><html:text property="faxNo2" style="width:118px" value="<%=terminal.getFaxnum2()%>"  maxlength="20" /></td>
                                <td >&nbsp;</td>
                                <td><html:text property="faxNo5" style="width:118px" value="<%=terminal.getFaxnum5()%>"  maxlength="20" /></td>
                            </tr>
                            <tr>
                                <td class="textlabels">Charge Code
                                <td><input  name="chargeCode" id="chargeCode" value="<%=chargeCode%>" style="width:118px"/>
                                    <%--                    <dojo:autoComplete formId="newTerminalForm" textboxId="chargeCode" action="<%=path%>/actions/getChargeCode.jsp?id=1"/></td>--%>
                                    <input type="hidden" id="chargeCode_check" value="<%=chargeCode%>"/>
                                    <div id="chargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("chargeCode","chargeCode_choices","","chargeCode_check",
                                        "<%=path%>/actions/getChargeCode.jsp?from=1&tabName=ADD_TERMINAL&isDojo=false","");
                                    </script>
                                </td>
                                <td>&nbsp;</td>
                                <td><html:text property="faxNo3" style="width:118px" value="<%=terminal.getFaxnum3()%>"  maxlength="20" /></td>
                                <td class="textlabels">Brl Charge Code</td>
                                <td><input  name="brlChargeCode" style="width:118px" id="brlChargeCode" value="<%=brlChgCode%>" />
                                    <%--                <dojo:autoComplete formId="newTerminalForm" textboxId="brlChargeCode" action="<%=path%>/actions/getChargeCode.jsp?id=2"/></td>--%>
                                    <input type="hidden" id="brlChargeCode_check" value="<%=brlChgCode%>"/>
                                    <div id="brlChargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("brlChargeCode","brlChargeCode_choices","","brlChargeCode_check",
                                        "<%=path%>/actions/getChargeCode.jsp?from=2&tabName=ADD_TERMINAL&isDojo=false","");
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabels">Doc Charge Code</td>
                                <td><input  name="docChargeCode" id="docChargeCode" value="<%=docChgCode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="newTerminalForm" textboxId="docChargeCode" action="<%=path%>/actions/getChargeCode.jsp?id=5"/></td> --%>
                                    <input type="hidden" id="docChargeCode_check" value="<%=docChgCode%>"/>
                                    <div id="docChargeCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("docChargeCode","docChargeCode_choices","","docChargeCode_check",
                                        "<%=path%>/actions/getChargeCode.jsp?from=5&tabName=ADD_TERMINAL&isDojo=false","");
                                    </script>
                                </td>
                                <td class="textlabels">Ovr 10k Chg Code</td>
                                <td class="textlabels"><input  name="ovr10kChgCode" id="ovr10kChgCode" value="<%=over10kchgcode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="newTerminalForm" textboxId="ovr10kChgCode" action="<%=path%>/actions/getChargeCode.jsp?id=3"/></td> --%>
                                    <input type="hidden" id="ovr10kChgCode_check" value="<%=over10kchgcode%>"/>
                                    <div id="ovr10kChgCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("ovr10kChgCode","ovr10kChgCode_choices","","ovr10kChgCode_check",
                                        "<%=path%>/actions/getChargeCode.jsp?from=3&tabName=ADD_TERMINAL&isDojo=false","");
                                    </script>
                                <td class="textlabels">Ovr 20k Chg Code </td>
                                <td class="textlabels"><input  name="ovr20kChgCode" id="ovr20kChgCode" value="<%=over20kchgcode%>" style="width:118px"/>
                                    <%--                <dojo:autoComplete formId="newTerminalForm" textboxId="ovr20kChgCode" action="<%=path%>/actions/getChargeCode.jsp?id=4"/></td> --%>
                                    <input type="hidden" id="ovr20kChgCode_check" value="<%=over20kchgcode%>"/>
                                    <div id="ovr20kChgCode_choices"  style="display: none;width: 5px;"  align="right"  class="newAutocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocomplete("ovr20kChgCode","ovr20kChgCode_choices","","ovr20kChgCode_check",
                                        "<%=path%>/actions/getChargeCode.jsp?from=4&tabName=ADD_TERMINAL&isDojo=false","");
                                    </script>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabels">Printer Model</td>
                                <td><html:text property="printerModel" value="<%=terminal.getPrintermodel()%>" onkeyup="toUppercase(this)" maxlength="20" style="width:118px"/></td>
                                <td class="textlabels"> Notes</td>
                                <td><html:textarea property="notes" styleClass="textareastyle" cols="25" value="<%=terminal.getNotes()%>" onkeyup="limitText(this.form.notes,this.form.countdown,140)" style="width:118px"/></td>
                                <td class="textlabels">Active/CTC/FTF </td>
                                <td class="textlabels"><html:select property="acf" styleClass="smallesdropdownStyle" value="<%=terminal.getActyon()%>" style="width:45px">
                                        <html:optionsCollection name="acflist"/></html:select>
                                    <html:checkbox property="airsrvc"  name="termForm" onclick="chkall()" /> Air SVC </td>
                            </tr>
                            <tr>
                                <td class="textlabels">FCL Document Dept Email</td>
                                <td><html:text property="docDeptEmail" styleId="docDeptEmail" value="<%=docDeptEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                                <td class="textlabels">FCL Exports Issuing Terminal</td>
                                <td class="textlabels">
                                 <input type="radio" name="fclExportIssuingTerminal" value="Y" Id="fclExportIssuingTerminalY" />Y
                                 <input type="radio" name="fclExportIssuingTerminal" value="N" Id="fclExportIssuingTerminalN" checked/>N
                               </td>
                                <td width="17%" class="textlabels">Exports Billing Terminal Email</td>
                                <td><html:text property="exportsBillingTerminalEmail" styleId="exportsBillingTerminalEmail" value="<%=exportsBillingTerminalEmail%>" onkeyup="toUppercase(this)" maxlength="50" style="width:118px"/></td>
                            </tr>
                            
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>

</html>

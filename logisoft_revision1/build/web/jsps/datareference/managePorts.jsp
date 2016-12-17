
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
         java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.PortsBean,com.gp.cong.logisoft.domain.PortsTemp"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@ include file="/jsps/includes/baseResources.jsp"%>
<%@ include file="/jsps/includes/resources.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(session.getAttribute("trade")!=null)
{
session.removeAttribute("trade");
}
DBUtil dbUtil=new DBUtil();
List countryList=new ArrayList();
PortsTemp scheduleobj = new PortsTemp(); 
String buttonValue="";
List portList=new ArrayList();
String schduleCode = "" ;
String eciPortCode = "" ;
String portName = "" ;
String pierCode = "" ;
String unCode = "" ;
String countryName = "" ;

String message="";
Ports ports=new Ports();
ports.setType("D");
String modify="";
ports.setStart("match");

if(session.getAttribute("scheduleobj")!= null)
{
   scheduleobj = (PortsTemp)session.getAttribute("scheduleobj") ;
   schduleCode = scheduleobj.getShedulenumber();
   eciPortCode =  scheduleobj.getEciportcode();
   portName = scheduleobj.getPortname();
   pierCode =  scheduleobj.getPiercode();
   unCode = scheduleobj.getUnLocationCode();
   countryName = scheduleobj.getCountryName();
   session.removeAttribute("scheduleobj");
}
if(session.getAttribute("buttonValue")!=null)
{
        buttonValue=(String)session.getAttribute("buttonValue");
}
if(request.getAttribute("buttonValue")!=null)
{
        buttonValue=(String)request.getAttribute("buttonValue");
}

PortsBean portBean=new PortsBean();
if(request.getAttribute("portBean")!=null)
{
portBean=(PortsBean)request.getAttribute("portBean");
ports.setType(portBean.getType());
ports.setStart(portBean.getStart());
}
if(request.getParameter("modify")!= null)
{
        modify=(String)request.getParameter("modify");
        session.setAttribute("modifyforports",modify);
}
else
{
        modify=(String)session.getAttribute("modifyforports");
}

if(request.getParameter("programid")!= null && session.getAttribute("processinfoforports")==null)
{
        buttonValue="searchall";
  	
}

request.setAttribute("ports",ports);


if(session.getAttribute("message")!=null)
{
message=(String)session.getAttribute("message");
}


if(countryList != null)
{
        countryList=dbUtil.getGenericCodeList(new Integer(11),"yes","Select Country");
        request.setAttribute("countryList",countryList);
}	
if(session.getAttribute("portList")!=null)
{
portList=(List)session.getAttribute("portList");
if(portList.size() ==1)
{
  scheduleobj = (PortsTemp)portList.get(0);
   schduleCode = scheduleobj.getShedulenumber();
   eciPortCode =  scheduleobj.getEciportcode();
   portName = scheduleobj.getPortname();
   pierCode =  scheduleobj.getPiercode();
   unCode = scheduleobj.getUnLocationCode();
   countryName = scheduleobj.getCountryName();
   //session.removeAttribute("portList");
}
}
//if(buttonValue.equals("searchall")){
  //      session.setAttribute("PortCaption","Port and Schedule {All Records}");
    //    portList=dbUtil.getAllPorts();
        
        if(request.getParameter("programid")!=null)
{
String programId=request.getParameter("programid");
        session.setAttribute("processinfoforports",programId);
        }
//}

String msg="";
if(request.getAttribute("message")!=null)
{
        msg=(String)request.getAttribute("message");
}	
String editPath=path+"/managePorts.do";
%>
<html> 
    <head>
        <title>Manage Ports</title>   
        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
            dojo.require("dojo.io.*");
            dojo.require("dojo.event.*");
            dojo.require("dojo.html.*");
             start = function(){
                loadFunction();
            }
            window.onload=start;
        </script>     
      
        <script type="text/javascript">
		function loadFunction(){
				//parent.parent.autoDisable('searchPorts');
		}
            var commonChargesVerticalSlide;
            window.addEvent('domready', function() {
                commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
                commonChargesVerticalSlide.toggle();
                $('commonChargesToggle').addEvent('click', function(e){
                    commonChargesVerticalSlide.toggle();
                });
            });
            var newwindow = '';
            function addform() {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/datareference/PortCode.jsp";
                }
                else
                {
                    newwindow=window.open("<%=path%>/jsps/datareference/PortCode.jsp","","width=600,height=150");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }
            function searchform(val)
            {
				//parent.parent.call('SearchPorts');
                document.managePorts.sheduleCode.value = val
                document.managePorts.buttonValue.value="search";
                document.managePorts.submit();
            }
            
            function searchallform()
            {
                document.managePorts.buttonValue.value="searchall";
         		
                document.managePorts.submit();
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function displaytagcolor()
            {
                var datatableobj = document.getElementById('carrierstable');
                for(i=0; i<datatableobj.rows.length; i++)
                {
                    var tablerowobj = datatableobj.rows[i];
                    if(i%2==0)
                    {
                        tablerowobj.bgColor='#FFFFFF';
                    }
                    else
                    {
                        tablerowobj.bgColor='#E6F2FF';
                    }
                }
				
            }
            function initRowHighlighting()
            {
                if (!document.getElementById('carrierstable')){ return; }
                var tables = document.getElementById('carrierstable');
                attachRowMouseEvents(tables.rows);
            }

            function attachRowMouseEvents(rows)
            {
                for(var i =1; i < rows.length; i++)
                {
                    var row = rows[i];
                    row.onmouseover =	function()
                    {
                        this.className = 'rowin';
                    }
                    row.onmouseout =	function()
                    {
                        this.className = '';
                    }
                    row.onclick= function()
                    {
                    }
                }
		
            }
            function setWarehouseStyle()
            {
                if(document.managePorts.buttonValue.value=="searchall")
                {
                    var x=document.getElementById('carrierstable').rows[0].cells;
		  
                    x[0].className="sortable sorted order1";
                }
                if(document.managePorts.buttonValue.value=="search")
                {
                    var input = document.getElementsByTagName("input");
                    if(!input[0].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[1].className="sortable sorted order1";
                    }
                    else if(!input[1].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[3].className="sortable sorted order1";
                    }
                    else if(!input[2].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[0].className="sortable sorted order1";
                    }
                    else if(!input[3].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[4].className="sortable sorted order1";
                    }
                    else if(!input[4].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[7].className="sortable sorted order1";
                    }
                    else if(!input[5].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[7].className="sortable sorted order1";
                    }
                    else if(!input[6].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[5].className="sortable sorted order1";
                    }
                    else if(!input[7].value=="")
                    {
                        var x=document.getElementById('carrierstable').rows[0].cells;
                        x[6].className="sortable sorted order1";
                    }
                }
            }
            function disabled(val)
            {
                if(val == 0)
                {
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++)
                    {
                        if(imgs[k].id != "showall" && imgs[k].id!="search")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                }
                var tables = document.getElementById('carrierstable');
                if(tables!=null)
                {
                    //displaytagcolor();
                    //initRowHighlighting();
                    setWarehouseStyle();
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
                mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
                mywindow.moveTo(200,180);
                return false;
            }
			
            function searchSchedule()
            {
                if(event.keyCode == 13)
                {
                    window.open("<%=path%>/jsps/datareference/searchScheduleCode.jsp?scheduleCode="+ document.managePorts.sheduleCode.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
                }
            }
			
            function searchcity()
            {
                if(event.keyCode == 13)
                {
                    window.open("<%=path%>/jsps/datareference/searchScheduleCode.jsp?city="+ document.managePorts.portName.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
                }
            }
            function getPortAddPage()
            {
                window.location.href="<%=path%>/jsps/datareference/portDetailsFrame.jsp";
            }
            function getSchedule(ev){
                if(event.keyCode==9 || event.keyCode==13){
                    var params = new Array();
                    params['requestFor'] = "ScheduleCode";
                    params['scheduleCode'] = document.managePorts.sheduleCode.value;
                    var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populateScheduleInfo");
                }
            }
            function populateScheduleInfo(type, data, evt) {
                if(data){
                    if(data.eciportcode)
                    {
                        document.getElementById("eciPortCode").value=data.eciportcode;
                    }
                    else
                    {
                        document.getElementById("eciPortCode").value="";
                    }
                    if(data.portname)
                    {
                        document.getElementById("portName").value=data.portname;
                    }
                    else
                    {
                        document.getElementById("portName").value="";
                    }
                    if(data.piercode)
                    {
                        document.getElementById("pierCode").value=data.piercode;
                    }
                    else
                    {
                        document.getElementById("pierCode").value="";
                    }
                    if(data.uncode)
                    {
                        document.getElementById("cityCode").value=data.uncode;
                    }
                    if(data.countryname)
                    {
                        document.getElementById("country").value=data.countryname;
                    }
                    else
                    {
                        document.getElementById("country").value="";
                    }
                }
            }
            function getCountry(ev){
                if(event.keyCode==9 || event.keyCode==13){
			   
                    var params = new Array();
                    params['requestFor'] = "country";
                    params['city'] = document.addWarehouse.city.value;
			
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
		function restart(){
		//parent.parent.call('SearchPorts');
		 document.managePorts.buttonValue.value="clearSearch";
                document.managePorts.submit();
		}
        </script>       
    </head>

    <body class="whitebackgrnd" >
        <html:form action="/managePorts" name="managePorts" type="com.gp.cong.logisoft.struts.form.ManagePortsForm" scope="request">
            <font color="blue" size="2"><%=message%></font>
         <%if(portList==null ||portList.size()==0 ){ %>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Port and Schedule Codes</td>
                    <td align="right"><input type="button" class="buttonStyleNew" id="addnew" value="Add New" onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/PortCode.jsp?relay='+'add',150,600)"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr class="textlabelsBold">
                                <td>City Name</td>                          
                                <td>
                                    <html:text property="portName" value="${managePortsForm.portName}" styleId="portName" size="20" styleClass="textlabelsBoldForTextBox"/>
                                    <input name="pn_check" id="pn_check" type="hidden" />
                                    <div id="pn_choices" style="display: none" class="autocomplete"></div>
                                    <script type="text/javascript">
                                           initAutocomplete("portName","pn_choices","poName","pn_check","<%=path%>/servlet/AutoCompleterServlet?action=portName&textFieldId=portName","searchform(document.managePorts.sheduleCode.value)");
                                    </script>                        
                                    <input type="hidden" name="poName" id="poName" /> 
                                </td>
                                <td><bean:message key="form.ManagePortsForm.ECIPortCode" /></td>
                                <td>&nbsp;<html:text property="eciPortCode"  value="${managePortsForm.eciPortCode}"
                                           styleClass="textlabelsBoldForTextBox" maxlength="3" onkeyup="toUppercase(this)" 
                                           /></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Schedule Code </td>
                                <td>
                                    <input  name="sheduleCode" id="sheduleCode"
                                            value="${managePortsForm.sheduleCode}"
                                            onkeydown="getSchedule(this.value)" 
                                            Class="textlabelsBoldForTextBox"  maxlength="5" />
                            <dojo:autoComplete formId="managePorts" textboxId="sheduleCode" action="<%=path%>/actions/getPorts.jsp?tabName=MANAGE_PORTS"/></td>
                        <td>Pier Abbreviation</td>
                        <td>&nbsp;<html:text property="pierCode"  value="${managePortsForm.pierCode}"
                                   styleClass="textlabelsBoldForTextBox" maxlength="20" onkeyup="toUppercase(this)" 
                                   />
                        </td>
                        </tr>

                <tr class="textlabelsBold">
                    <td>Un Loc Code</td>
                    <td><html:text property="cityCode" value="${managePortsForm.cityCode}" 
                    styleClass="textlabelsBoldForTextBox" maxlength="50" onkeyup="toUppercase(this)"/></td>
                    <td><bean:message key="form.ManagePortsForm.Country" /></td>
                    <td>&nbsp;<html:text property="country"  value="${managePortsForm.country}"
                    styleClass="textlabelsBoldForTextBox" maxlength="20" /></td>

                </tr>
                <tr  class="textlabelsBold">
                    <td colspan="2" rowspan="2">
                        <table>
<%--                            <tr class="textlabelsBold">--%>
<%--                                <td >Match Only</td>--%>
<%--                                <td align="center"><html:radio property="start" value="match" name="ports"></html:radio></td>--%>
<%--                                <td >Start List At</td>--%>
<%--                                <td align="center"><html:radio property="start" value="starts" name="ports"></html:radio></td>--%>
<%--                            </tr>--%>
<tr  class="textlabelsBold">
<td> Show Entire List</td>
<td align="center">
<html:checkbox property="start" value="starts" name="managePortsForm" ></html:checkbox>
</td></tr>
<%--<tr><td class="textlabelsBold">SCH Code </td>--%>
<%-- <td align="center"><html:checkbox property="SCHCode" value="SCHCode"  name="managePortsForm"/></td></tr>--%>
<%--<tr><td class="textlabelsBold">ECI Code</td>--%>
<%--<td align="center"><html:checkbox  property="ECICode" value="ECICode" name="managePortsForm"/></td></tr>--%>
<%--<tr>--%>

                        </table>
                       
                    </td>
                   <%-- <td>Limit</td>
                    <td><html:select property="limit" styleClass="verysmalldropdownStyleForText" value="${managePortsForm.limit}" >
                    	<html:option value="250">250</html:option>
                    	<html:option value="500">500</html:option>
                    	<html:option value="1000">1000</html:option>
                    </html:select>
                     </td> --%>
                     </tr>
                <tr>
                    <td colspan="2">
                                    <input type="button" class="buttonStyleNew" id="search" value="Search" onclick="searchform(document.managePorts.sheduleCode.value)" style="cursor: pointer; cursor: hand;"/>
<%--                                    <input type="button" class="buttonStyleNew" id="showall" value="Show All" onclick="searchallform()"/>--%>
                                </td>
                </tr>
                <tr>

                    <td colspan="4" align="center">
                        <table>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>


        </td>
    </tr>
</table>

<table>	
    <tr>
        <td>&nbsp;</td>
    </tr>
</table>

<%}else{
 %>
<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew ">
             <tr class="tableHeadingNew">
                  <td>
                      <c:if test="${not empty managePortsForm.sheduleCode}">
                          <b class="textlabelsBoldForTextBox">SCH Code-><c:out value="${managePortsForm.sheduleCode}"></c:out></b>,
                      </c:if>
                      <c:if test="${not empty managePortsForm.eciPortCode}">
                          <b class="textlabelsBoldForTextBox">ECI Code-><c:out value="${managePortsForm.eciPortCode}"></c:out></b>,
                      </c:if>
                      <c:if test="${not empty managePortsForm.portName}">
                          <b class="textlabelsBoldForTextBox">ECI Code-><c:out value="${managePortsForm.portName}"></c:out></b>,
                      </c:if>
                      <c:if test="${not empty managePortsForm.pierCode}">
                          <b class="textlabelsBoldForTextBox">ECI Code-><c:out value="${managePortsForm.pierCode}"></c:out></b>,
                      </c:if>
                      <c:if test="${not empty managePortsForm.cityCode}">
                          <b class="textlabelsBoldForTextBox">ECI Code-><c:out value="${managePortsForm.cityCode}"></c:out></b>,
                      </c:if>
                      <c:if test="${not empty managePortsForm.country}">
                          <b class="textlabelsBoldForTextBox">ECI Code-><c:out value="${managePortsForm.country}"></c:out></b>,
                      </c:if>
                      </td>
<table width="100%" border="0" class="tableBorderNew" cellpadding="0" cellspacing="0">
    <tr class="tableHeadingNew"><td>List Of Ports and Schedule Codes</td><td align="right">
    <input type="button" class="buttonStyleNew" id="clear" value="Restart" onclick="restart()"/></td>
        <td align="right">
            <%-- <a id="commonChargesToggle" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> --%>
        </td>

    </tr>
    <tr>
        <td colspan="2">
            <%--     <div id="common_Charges_vertical_slide">--%>
<%--            <div id="divtablesty1" class="scrolldisplaytable" >--%>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                    <%
                    int i=0;
                    %>
                    <display:table name="<%=portList%>" class="displaytagstyle" id="carrierstable" pagesize="<%=pageSize%>" style="width:100%" sort="list">
                        <display:setProperty name="paging.banner.some_items_found">
                            <span class="pagebanner">
                                <font color="blue">{0}</font> Ports Details Displayed,For more Ports Details  click on Page Numbers.
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
                        <display:setProperty name="paging.banner.item_name" value="Ports"/>
                        <display:setProperty name="paging.banner.items_name" value="Ports"/>
                        <%
                        PortsTemp port=null;
                        String scheduleCode="";
                        String controlNo="",link=null,name=null,cityName=null;
  		
                        if(portList!=null && portList.size()>0)
                        {
                        port=(PortsTemp)portList.get(i);
                        if(port.getShedulenumber()!=null)
                        {
                        scheduleCode=port.getShedulenumber();
                        }
                        if(port.getControlNo()!=null)
                        {
                        scheduleCode+="-"+port.getControlNo();
                        }
                        if(port.getPortname()!=null){
                        cityName=port.getPortname();
                        name = StringUtils.abbreviate(cityName,16) ;
                        }
                        link = editPath+"?param="+port.getId();
                        }
                         %>
                         <display:column title="City Name" href="<%=editPath%>"  paramId="paramid" sortable="true" headerClass="sortable"
                paramProperty="id"><span onmouseover="tooltip.show('<strong><%=cityName%></strong> ',null,event);" onmouseout="tooltip.hide();"><%=name%></span> </display:column>
                            <display:column property="countryName" title="Country Name"  sortable="true"></display:column>
                            <display:column property="unLocationCode" title="Un Loc Code" sortable="true"></display:column>
                            <display:column property="eciportcode" title="ECI Port Code" sortable="true" headerClass="sortable"/>
                             <display:column title="Schd-Cntrl No"  sortable="true" headerClass="sortable"><%=scheduleCode%></display:column>
                             <display:column property="piercode" title="Pier Abbr" sortable="true" />
                             <display:column title="Actions">
                            <span onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"> <img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href='<%=link %>' "/> </span>
                            </display:column>
                            <% i++;%>
                 </display:table>
                </table>
          <%--  </div>
            </div>
            --%></td>
    </tr>
</table>
<%} %>
<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
<script>
disabled('<%=modify%>')</script>
</html:form>
</body>

<%--<%@include file="../includes/baseResourcesForJS.jsp" %>--%>
</html>



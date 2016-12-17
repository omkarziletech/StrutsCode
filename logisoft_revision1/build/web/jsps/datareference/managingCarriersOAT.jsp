<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.beans.SearchCarriersBean,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.CarriersOrLineTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
String buttonValue="";
List carrierList=null;
List carriertypeList=new ArrayList();
CarriersOrLine carriers=new CarriersOrLine();
carriers.setMatch("starts");
SearchCarriersBean sBean=new SearchCarriersBean();
String modify=null;
String msg="";
String carrierCode = "";
String carrierName = "";
String  carrierType = "0";
String addPage="";
CarriersOrLineTemp carrierobj = new CarriersOrLineTemp(); 
if(session.getAttribute("carrierobj") != null){
carrierobj = (CarriersOrLineTemp)session.getAttribute("carrierobj");
carrierCode = carrierobj.getCarriercode();
carrierName = carrierobj.getCarriername();
if(carrierobj.getCarriertype() != null && carrierobj.getCarriertype().getId() != null){
 carrierType = carrierobj.getCarriertype().getId().toString();
}
session.removeAttribute("carrierobj");
}
if(request.getAttribute("message")!=null){
        msg=(String)request.getAttribute("message");
}
if(carriertypeList != null){
        carriertypeList=dbUtil.getGenericCodeList(new Integer(17),"yes","Select Carrier");
        request.setAttribute("carriertypeList",carriertypeList);
}
if(request.getAttribute("buttonValue")!=null){
        buttonValue=(String)request.getAttribute("buttonValue");
} 
if(request.getParameter("modify")!= null){
        session.setAttribute("modifyformanagingcarriers",request.getParameter("modify"));
        modify=(String)session.getAttribute("modifyformanagingcarriers");
}else{
        modify=(String)session.getAttribute("modifyformanagingcarriers");
}
if(request.getAttribute("scBean")!=null){
        sBean=(SearchCarriersBean)request.getAttribute("scBean");
        carriers.setMatch(sBean.getMatch());
}
if(session.getAttribute("carrierList")!=null){
        carrierList=(List)session.getAttribute("carrierList");
        if(carrierList.size()==1){
                 carrierobj = (CarriersOrLineTemp)carrierList.get(0);
                 carrierCode = carrierobj.getCarriercode();
         carrierName = carrierobj.getCarriername();
         if(carrierobj.getCarriertype() != null && carrierobj.getCarriertype().getId() != null){
                         carrierType = carrierobj.getCarriertype().getId().toString();
                }
        }
} 
if(request.getParameter("programid")!= null && session.getAttribute("processinfoforcarriers")==null){
        buttonValue="searchall";
}
if(buttonValue.equals("searchall")){
        carrierList=dbUtil.getAllCarriers();
        if(request.getParameter("programid")!=null){
                session.setAttribute("processinfoforcarriers",request.getParameter("programid"));
        }
        session.setAttribute("carrierList",carrierList);
        session.setAttribute("mangingcarrierCaption","List of Managing Carriers");
}
  request.setAttribute("carriers",carriers);
  String editPath=path+"/managingCarriersOAT.do";
if(session.getAttribute("addPage")!=null){
        addPage=(String)session.getAttribute("addPage");
        session.removeAttribute("addPage");
}
%>
<html>
    <head>
        <title>Managing carriers Ocean,Air,Truck</title>
        <%@include file="../includes/baseResources.jsp" %>
        <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
            dojo.require("dojo.io.*");
            dojo.require("dojo.event.*");
            dojo.require("dojo.html.*");
        </script>
        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
        <script type="text/javascript">
		
            var commonChargesVerticalSlide;
            window.addEvent('domready', function() {
                commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
                commonChargesVerticalSlide.toggle();
                $('commonChargesToggle').addEvent('click', function(e){
                    commonChargesVerticalSlide.toggle();
                });
            });
            function disabled(val,val1){
                if(val == 0){
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++){
                        if(imgs[k].id != "showall" && imgs[k].id!="search"){
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                }
                var datatableobj = document.getElementById('carriertable');
                if(datatableobj!=null){
                    //displaytagcolor();
                    //initRowHighlighting();
                    //setCarrierStyle();
                }
                        if(val1=="addPage"){
                    window.location.href="<%= path%>/jsps/datareference/newManagingCarriersOAT.jsp";
                }
            }
            function setCarrierStyle(){
                if(document.managingCarriersOAT.buttonValue.value=="searchall"){
                    var x=document.getElementById('carriertable').rows[0].cells;
                    x[0].className="sortable sorted order1";
                }
                if(document.managingCarriersOAT.buttonValue.value=="search"){
                    var input = document.getElementsByTagName("input");
                    var select = document.getElementsByTagName("select");
                    if(!input[0].value==""){
                        var x=document.getElementById('carriertable').rows[0].cells;
                        x[0].className="sortable sorted order1";
                    }else if(!input[1].value==""){
                        var x=document.getElementById('carriertable').rows[0].cells;
                        x[1].className="sortable sorted order1";
                    }
                    else if(!input[2].value==""){
                        var x=document.getElementById('carriertable').rows[0].cells;
                        x[3].className="sortable sorted order1";
                    }else if(!select[0].value=="0"){
                        var x=document.getElementById('carriertable').rows[0].cells;
                        x[2].className="sortable sorted order1";
                    }
                }
            }
            function displaytagcolor(){
                var datatableobj = document.getElementById('carriertable');
                for(i=0; i<datatableobj.rows.length; i++){
                    var tablerowobj = datatableobj.rows[i];
                    if(i%2==0){
                        tablerowobj.bgColor='#FFFFFF';
                    }else{
                        tablerowobj.bgColor='#E6F2FF';
                    }
                }
            }
            function toUppercase(obj){
                obj.value = obj.value.toUpperCase();
            }
            function initRowHighlighting(){
                if (!document.getElementById('carriertable')){ return; }
                var tables = document.getElementById('carriertable');
                attachRowMouseEvents(tables.rows);
            }
            function attachRowMouseEvents(rows){
                for(var i =1; i < rows.length; i++){
                    var row = rows[i];
                    row.onmouseover =	function() {
                        this.className = 'rowin';
                    }
                    row.onmouseout =	function(){
                        this.className = '';
                    }
                    row.onclick= function(){
                    }
                }
            }
            function searchallform(){
                document.managingCarriersOAT.buttonValue.value="searchall";
                document.managingCarriersOAT.submit();
            }
            function print(){
                document.managingCarriersOAT.buttonValue.value="print";
                document.managingCarriersOAT.submit();
            }
            var newwindow = '';
            function addform() {
                        if (!newwindow.closed && newwindow.location){
                            newwindow.location.href = "<%=path%>/jsps/datareference/carrierCode.jsp";
                }else{
                    newwindow=window.open("<%=path%>/jsps/datareference/carrierCode.jsp","","width=350,height=100");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }
            function searchform(val){
                if(document.managingCarriersOAT.carriercode.value==""){
                    if(document.managingCarriersOAT.carriername.value==""){
                        if(document.managingCarriersOAT.carriertype.value==""){
                            if(document.managingCarriersOAT.SCAC.value==""){
                                alert("Please Enter Any Search Criteria");
                                document.managingCarriersOAT.warehouseCode.focus();
                                return;
                            }
                        }
                    }
                }
                document.managingCarriersOAT.carriercode.value=val
                document.managingCarriersOAT.buttonValue.value="search";
                document.managingCarriersOAT.submit();
            }
            function carriertype1(){
                document.managingCarriersOAT.match[0].checked=true;
                document.managingCarriersOAT.match[1].disabled=true;
                return;
            }
            function popup1(mylink, windowname){
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
            // searchCarrierCode
            function searchCarrierCode(){
                        if(event.keyCode==13){
                    window.open("<%= path%>/jsps/datareference/searchCarrierCode.jsp?carriercode="+ document.managingCarriersOAT.carriercode.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400" );
                }
            }
            // searchCarrierName
            function searchCarrierName(){
                        if(event.keyCode==13){
                    window.open("<%= path%>/jsps/datareference/searchCarrierCode.jsp?carriername="+ document.managingCarriersOAT.carriername.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400" );
                }
            }
                    function test(){
                window.location.href="<%= path%>/jsps/datareference/newManagingCarriersOAT.jsp";
            }
            function getCarrier(ev){
                document.getElementById("carriertype").value="0";
                if(event.keyCode==9 || event.keyCode==13){
                    var params = new Array();
                    params['requestFor'] = "SSLineName";
                    params['ssLineNumber'] = document.managingCarriersOAT.carriercode.value;
                            var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populateCarriersDetailsDojo");
                }
            }
            function populateCarriersDetailsDojo(type, data, evt) {
                if(data){
                    if(data.ssLineName){
                        document.getElementById("carriername").value=data.ssLineName;
                    }else{
                        document.getElementById("carriername").value="";
                    }if(data.ssLineType){
                        if(IsNumeric(data.ssLineType)==true){
                            document.getElementById("carriertype").value=data.ssLineType;
                        }
                    }else{alert(data.ssLineType);
                        document.getElementById("carriertype").value="0";
                    }if(data.ssLineScac){
                        document.getElementById("SCAC").value=data.ssLineScac;
                    }else{
                        document.getElementById("SCAC").value="";
                    }
                }
            }
            function getCarrier1(ev){
                document.getElementById("carriertype").value="0";
                if(event.keyCode==9 || event.keyCode==13){
                    var params = new Array();
                    params['requestFor'] = "SSLineNo";
                    params['ssLineName'] = document.managingCarriersOAT.carriername.value;
                            var bindArgs = {
                        url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populateCarriersDetailsDojo1");
                }
            }
            function populateCarriersDetailsDojo1(type, data, evt) {
                if(data){
                    if(data.ssLineNo){
                        document.getElementById("carriercode").value=data.ssLineNo;
                    }else{
                        document.getElementById("carriercode").value="";
                    }if(data.ssLineType){
                        if(IsNumeric(data.ssLineType)==true){
                            document.getElementById("carriertype").value=data.ssLineType;
                        }
                    }else{alert(data.ssLineType);
                        document.getElementById("carriertype").value="0";
                    }if(data.ssLineScac){
                        document.getElementById("SCAC").value=data.ssLineScac;
                    }else{
                        document.getElementById("SCAC").value="";
                    }
                }
            }

        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd"  >
        <html:form action="/managingCarriersOAT" name="managingCarriersOAT"   type="com.gp.cong.logisoft.struts.form.ManagingCarriersOATForm" scope="request">
            <font color="blue" size="4"><%=msg%></font>
            <table width="100%">
                <td>
                    <table border="0" cellpadding="3" cellspacing="0" width="100%" class="tableBorderNew">
                        <tr class="tableHeadingNew"><bean:message key="form.managingCarriersOATForm.search"/></tr>
                        <tr class="style2" >
                            <td ><bean:message key="form.managingCarriersOATForm.carriercode" /></td>
                            <td><input  name="carriercode" id="carriercode" value="<%=carrierCode%>" maxlength="15" size="30" onkeydown="getCarrier(this.value)" />
                        <dojo:autoComplete formId="managingCarriersOAT" textboxId="carriercode" action="<%=path%>/actions/getSsLineName.jsp?tabName=MANAGING_CARRIERS_OAT&from=0"/></td>
                <td><bean:message key="form.managingCarriersOATForm.carriername"/></td>
                <td>
                    <input  name="carriername" id="carriername" value="<%=carrierName%>" maxlength="15" size="30" onkeydown="getCarrier1(this.value)" />
                <dojo:autoComplete formId="managingCarriersOAT" textboxId="carriername" action="<%=path%>/actions/getSsLineNo.jsp?tabName=MANAGING_CARRIERS_OAT&from=0"/>
            </td>

        </tr>
        <tr class="style2">
            <td><bean:message key="form.managingCarriersOATForm.carriertype"/></td>
            <td><html:select property="carriertype" styleClass="verysmalldropdownStyle" value="<%=carrierType%>" onchange="carriertype1()" style="width=205px">
                    <html:optionsCollection name="carriertypeList"/>
                </html:select></td>
            <td><bean:message key="form.managingCarriersOATForm.scac"/></td>
            <td><html:text property="SCAC" value="<%=carrierobj.getSCAC()%>" size="30" onkeyup="toUppercase(this)"/></td>
        </tr >
        <tr>
            <td colspan="4"  class="textlabels"><html:radio property="match" value="match" name="carriers" ><bean:message key="form.managingCarriersOATForm.match"/></html:radio>
                <html:radio property="match" value="starts" name="carriers" ><bean:message key="form.managingCarriersOATForm.start"/></html:radio></td>
        </tr>
        <tr>
            <td colspan="4" align="center">
                <input type="button" class="buttonStyleNew" value="Search" onclick="searchform(document.managingCarriersOAT.carriercode.value)"/>
                <input type="button" class="buttonStyleNew" value="Show All" onclick="searchallform()" name="showall"/>
                <%--	<input type="button" class="buttonStyleNew" value="Print" onclick="print()" /> --%>
                <input type="button" class="buttonStyleNew" value="Add New" onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/carrierCode.jsp?button='+'code',150,600)"/>
            </td>
        </tr>
    </table>
    
    <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0" >
        <tr class="tableHeadingNew"><td><%=session.getAttribute("mangingcarrierCaption")  %></td>
        </tr>
        <tr>
            <td>
    	<div  class="scrolldisplaytable" >
                        <%
                        int i=0;
                        %>
                        <display:table name="<%=carrierList%>" class="displaytagStyleNew"  pagesize="<%=pageSize%>" id="carriertable" sort="list" style="width:100%">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner">
                                    <font color="blue">{0}</font> Carrier Details Displayed,For more Carrier Details  click on Page Numbers.
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
                            <display:setProperty name="paging.banner.item_name" value="CarriersOrLine"/>
                            <display:setProperty name="paging.banner.items_name" value="CarriersOrLines"/>
                            <%	String carriertype=null;
                                    String  link=null;
                                    if(carrierList != null && carrierList.size()>0) {
                                            CarriersOrLineTemp carrierlineobj=(CarriersOrLineTemp)carrierList.get(i);
                                            GenericCode genericobj=carrierlineobj.getCarriertype();
                                            carriertype=genericobj.getCodedesc();
                                            link = editPath+"?param="+carrierlineobj.getId();
                                    }
                            %>
                            <display:column property="carriername" title="CARRIER NAME" sortable="true" />
                            <display:column  title="CARRIER TYPE" class="align" sortable="true"><%=carriertype%></display:column>
                            <display:column property="SCAC" title="SCAC" sortable="true"/>
                            <display:column property="carriercode" href="<%=editPath%>"  paramId="paramid" paramProperty="carriercode"  title="CARRIER CODE" sortable="true" />
                            <display:column title="Actions">
                                <span onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"> <img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href='<%=link %>' "/> </span>
                                </display:column>
                                <% i++;%>
                            </display:table>
                </div>
                <%--</div>
                --%></td>
        </tr>
    </table>
    <html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
    <script>disabled('<%=modify%>','<%=addPage%>')</script>
</html:form>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

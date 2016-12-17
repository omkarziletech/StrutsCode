<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.beans.TerminalBean,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.domain.RefTerminalTemp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String buttonValue="";
DBUtil dbUtil=new DBUtil();
List terminalList=null;
String modify=null;
String message="";
String termType = "";
String termNum = "";
String termLoc = "";
String city = "";

RefTerminalTemp term=new RefTerminalTemp();
term.setMatch("starts");
RefTerminalTemp refTerminalobj=new RefTerminalTemp();

TerminalBean terminalBean=new TerminalBean();


if( session.getAttribute("refTerminalobj") != null)
{
 refTerminalobj = (RefTerminalTemp)session.getAttribute("refTerminalobj");
 termNum = refTerminalobj.getTrmnum();
 termLoc = refTerminalobj.getTerminalLocation();
 city = refTerminalobj.getCity1();
   if(refTerminalobj.getGenericCode() != null && refTerminalobj.getGenericCode().getId()!= null)
   {
     termType = refTerminalobj.getGenericCode().getId().toString();
    
   }
   session.removeAttribute("refTerminalobj");
}


 
if(request.getAttribute("terminalBean")!=null)
{
terminalBean=(TerminalBean)request.getAttribute("terminalBean");
term.setMatch(terminalBean.getMatch());
}
request.setAttribute("term",term);
if(request.getAttribute("buttonValue")!=null)
{
 buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("terminalList")!=null)
{
terminalList=(List)session.getAttribute("terminalList");
if(terminalList.size()==1)
{
 refTerminalobj = (RefTerminalTemp)terminalList.get(0);
  termNum = refTerminalobj.getTrmnum();
 termLoc = refTerminalobj.getTerminalLocation();
 city = refTerminalobj.getCity1();
   if(refTerminalobj.getTerminalType() != null && refTerminalobj.getGenericCode().getId()!= null)
   {
     termType = refTerminalobj.getGenericCode().getId().toString();
    
   }
  
   //session.removeAttribute("terminalList");
}
}
if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}

if(request.getParameter("modify")!= null)
{
 modify=(String)request.getParameter("modify");
 session.setAttribute("modifyforterminal",modify);
}
else
{
        modify=(String)session.getAttribute("modifyforterminal");
}

if(request.getParameter("programid")!= null && session.getAttribute("processinfoforterminal")==null)
{
        buttonValue="searchall";
  	
session.setAttribute("Terminalcaption","Terminal {All Records}");
}
if(buttonValue.equals("searchall"))
{
        terminalList=dbUtil.getAllTerminal();
        session.setAttribute("terminalList",terminalList);
        if(request.getParameter("programid")!=null)
{
String programId=request.getParameter("programid");
        session.setAttribute("processinfoforterminal",programId);
        }
        //session.setAttribute("Terminalcaption","Terminal {All Records}");
}
request.setAttribute("terminaltypelist",dbUtil.getGenericCodeList(18,"yes","Select Terminal Type"));
String editPath=path+"/terminalManagement.do";


%>
<html> 
    <head>
        <base href="<%=basePath%>">
        <title>JSP for TerminalManagementForm form</title>
        <%@include file="../includes/baseResources.jsp" %>
        <link rel="stylesheet" type="text/css" href="<%=path%>/css/style.css">
        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript">
            var newwindow = '';
            function addform1()
            {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/datareference/TerminalCode.jsp";
                }
                else
                {
                    newwindow=window.open("<%=path%>/jsps/datareference/TerminalCode.jsp","","width=400,height=120");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }
            function searchform(val)
            {
                if(document.terminalManagementForm.terminalId.value=="")
                {
                    if(document.terminalManagementForm.terminalName.value=="")
                    {
                        if(document.terminalManagementForm.city.value=="")
                        {
                            if(document.terminalManagementForm.terminalType.value=="0")
                            {
                                alert("Please enter anyone to search");
                                return;
                            }
                        }
                    }
                }
                document.terminalManagementForm.terminalId.value = val;
                document.terminalManagementForm.buttonValue.value="search";
                document.terminalManagementForm.submit();
            }
            function searchallform()
            {
                document.terminalManagementForm.buttonValue.value="searchall";
                document.terminalManagementForm.submit();
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
                var tables = document.getElementById('terminaltable');
                if(tables!=null)
                {
                    displaytagcolor();
                    initRowHighlighting();
                    setWarehouseStyle();
                }
            }
            function displaytagcolor()
            {
                var datatableobj = document.getElementById('terminaltable');
		
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
                if (!document.getElementById('terminaltable')){ return; }
                var tables = document.getElementById('terminaltable');
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
                if(document.terminalManagementForm.buttonValue.value=="searchall")
                {
                    var x=document.getElementById('terminaltable').rows[0].cells;
                    x[0].className="sortable sorted order1";
                }
                if(document.terminalManagementForm.buttonValue.value=="search")
                {
                    var input = document.getElementsByTagName("input");
                    var select = document.getElementsByTagName("select");
                    if(!input[0].value=="")
                    {
                        var x=document.getElementById('terminaltable').rows[0].cells;
                        x[0].className="sortable sorted order1";
                    }
                    else if(!input[1].value=="")
                    {
                        var x=document.getElementById('terminaltable').rows[0].cells;
                        x[1].className="sortable sorted order1";
                    }
                    else if(!input[2].value=="")
                    {
                        var x=document.getElementById('terminaltable').rows[0].cells;
                        x[2].className="sortable sorted order1";
                    }
                    else if (!select[0].value=="0")
                    {
                        var x=document.getElementById('terminaltable').rows[0].cells;
                        x[3].className="sortable sorted order1";
                    }
                }
            }
            function terminaltype1()
            {
                document.terminalManagementForm.match[0].checked=true;
                document.terminalManagementForm.match[1].disabled=true;
                return;
            }

            function terminaltype1()
            {
                document.terminalManagementForm.match[0].checked=true;
                document.terminalManagementForm.match[1].disabled=true;
                return;
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
				
            function searchbynumber()
            {
						 
                if(event.keyCode==13)
                {
                    window.open("<%= path%>/jsps/datareference/searchTerminalNumber.jsp?termno="+ document.terminalManagementForm.terminalId.value ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
                }
            }
            function searchbyloc()
            {
						 
                if(event.keyCode==13)
                {
                    window.open("<%= path%>/jsps/datareference/searchTerminalNumber.jsp?termloc="+ document.terminalManagementForm.terminalName.value,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400" );
                }
            }
            function getAddPage(){
                window.location.href = "<%=path%>/jsps/datareference/NewTerminal.jsp";
            }
            function TerPrintList()
            {
                document.terminalManagementForm.buttonValue.value="TerPrintList";
                document.terminalManagementForm.submit();
            }
        </script>
    </head>
    <%@include file="../includes/resources.jsp" %>
    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
        <html:form action="/terminalManagement" scope="request">
            <font color="blue" size="4"><%=message%></font>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left" class="headerbluelarge">Managing ECI Offices/Terminals </td>
                </tr>
            </table>

            <table width="100%" class="tableBorderNew" cellpadding="2" cellspacing="0" border="0">
                <tr class="tableHeadingNew">Search Criteria</tr>
                <tr>
                    <td  class="style2">Terminal Number&nbsp;<img border="0" src="<%=path%>/img/search1.gif" style="cursor: pointer; cursor: hand;" onclick="return popup1('<%=path%>/jsps/datareference/searchTerminalNumber.jsp?termno='+ document.terminalManagementForm.terminalId.value,'windows')"/></td>
                    <td><html:text property="terminalId" value="<%=refTerminalobj.getTrmnum()%>"  onkeyup="toUppercase(this)"  onkeypress="searchbynumber()" maxlength="3" size="3"></html:text></td>
                    <td class="style2">Terminal Location</td>
                    <td><html:text property="terminalName" value="<%=refTerminalobj.getTerminalLocation()%>"  onkeyup="toUppercase(this)" onkeypress="searchbyloc()" maxlength="100" size="22"/></td>
                    <td class="style2">Match Only<html:radio property="match" value="match" name="term"></html:radio></td>
                    <td class="style2">Start list at<html:radio property="match" value="starts" name="term"></html:radio></td>
                </tr>
                <tr>
                    <td class="style2">City</td>
                    <td><html:text property="city" value="<%=refTerminalobj.getCity1()%>" size="22"/></td>
                    <td class="style2" >Terminal Type</td>
                    <%
 			
 			
                    %>
                    <td><html:select property="terminalType"  value="<%=termType%>" onchange="terminaltype1()" >
                            <html:optionsCollection name="terminaltypelist" />
                        </html:select></td>
                    <td><img src="<%=path%>/img/search1.gif" id="search" border="0" onclick="searchform(document.terminalManagementForm.terminalId.value)" style="cursor: pointer; cursor: hand;"/></td>
                    <td><input type="button" class="buttonStyleNew" id="showall" value="Show All" onclick="searchallform()"/></td>                  
                    <td><input type="button" class="buttonStyleNew" value="Print" onclick="TerPrintList()"/></td>                  
                    <td> &nbsp;<input type="button" class="buttonStyleNew" id="addnew" value="Add New"
                                      onclick="return GB_show('Terminal Code', '<%=path%>/jsps/datareference/TerminalCode.jsp',150,600)"/></td>
                </tr>
            </table>
            <br style="height: 5px" />
            <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0" border="0">
                <tr><td height="15"  class="tableHeadingNew"><%=session.getAttribute("Terminalcaption") %></td></tr>
                <tr>
                    <td align="left" scope="row">
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <table  border="0" cellpadding="0" cellspacing="0" >
                                <display:table name="<%=terminalList%>"  class="displaytagstyle" id="terminaltable" sort="list" style="width:100%">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Terminals details displayed,For more Terminals click on page numbers.
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
                                    <display:setProperty name="paging.banner.item_name" value="RefTerminal"/>
                                    <display:setProperty name="paging.banner.items_name" value="RefTerminal"/>
                                    <display:column href="<%=editPath%>" paramId="param" paramProperty="trmnum"><%="More Info"%></display:column>
                                    <display:column property="terminalLocation" title="TERMINAL LOCATION" sortable="true"/>
                                    <display:column property="city1" title="CITY" sortable="true"/>
                                    <display:column property="codeDesc" title="TERMINAL TYPE" sortable="true"></display:column>
                                    <display:column title="TERMINAL NUMBER" href="<%=editPath%>" paramId="paramid" paramProperty="trmnum" sortable="true">
                                        ${terminaltable.trmnum} ${terminaltable.unLocationCode1}
                                    </display:column>
                                  <%--  <display:column property="unLocationCode1" title="UN LOC CODE" sortable="true"/>
                                    <display:column property="managerName" title="Manager Name" sortable="true"/>
                                    <display:column property="managerEmail" title="Manager Email" sortable="true"/>--%>
                                </display:table>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" value="<%=buttonValue%>"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


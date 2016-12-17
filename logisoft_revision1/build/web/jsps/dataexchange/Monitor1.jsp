<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.domain.Usecases,com.gp.cong.logisoft.domain.DataExchangeTransaction,com.gp.cong.logisoft.beans.MonitorBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List useCaseList=null;
DBUtil dbUtil=new DBUtil();
MonitorBean mBean=new MonitorBean();
if(request.getAttribute("monitorBean")!=null)
{
mBean=(MonitorBean)request.getAttribute("monitorBean");
}
String buttonValue="searchall";
if(request.getAttribute("buttonValue")!=null)
{
    buttonValue=(String)request.getAttribute("buttonValue");
}
if(session.getAttribute("useCaseList")!=null)
{
useCaseList=(List)session.getAttribute("useCaseList");
}
if(buttonValue.equals("searchall"))
{
 useCaseList=dbUtil.getAllUseCases();
 session.setAttribute("useCaseList",useCaseList);
}
request.setAttribute("usecaseidlist",dbUtil.getAllUseCaseId());
request.setAttribute("flowlist",dbUtil.getFlowList());
request.setAttribute("statuslist",dbUtil.getStatasList());
request.setAttribute("hourslist",dbUtil.getHours());
request.setAttribute("minuteslist",dbUtil.getMinutes());

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<html> 
    <head>
        <base href="<%=basePath%>">
        <title>JSP for SearchUseCaseForm form</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">

        <link rel="stylesheet" type="text/css" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
        <link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
        <script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script>
            function search1form()
            {

                document.monitor1Form.buttonValue.value="searchall";
                document.monitor1Form.submit();
            }
            function searchform()
            {
                if(document.monitor1Form.useCaseId.value=="0")
                {
                    if(document.monitor1Form.txtCal.value=="")
                    {

                        if(document.monitor1Form.docSetKeyValue.value=="")
                        {
                            if(document.monitor1Form.flowFrom.value=="0")
                            {
                                if(document.monitor1Form.status.value=="0")
                                {
                                    alert("please select any search criteria");
                                    return;
                                }
                            }
                        }
                    }}

                if(document.monitor1Form.txtCal.value!="")
                {
                    if(document.monitor1Form.hours.value=="0")
                    {
                        alert("please select the date");
                        return;
                    }
                }
                if(document.monitor1Form.txtCal.value!="")
                {
                    if(isValidDate(document.monitor1Form.txtCal.value)==false)
                    {
                        document.monitor1Form.txtCal.value="";
                        document.monitor1Form.txtCal.focus();
                        return;
                    }
                }
                document.monitor1Form.buttonValue.value="search";
                document.monitor1Form.submit();
            }
            function displaytagcolor()
            {
                var datatableobj = document.getElementById('monitortable');
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
        </script>
        <%@include file="../includes/baseResources.jsp" %>

    </head>
    <body class="whitebackgrnd" onload="">
        <html:form action="/monitor1" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">

                <tr>
                    <td class="headerbluesmall">&nbsp;&nbsp;Search Criteria </td>
                </tr>

                <tr>
                    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr height="8">

                            </tr>
                            <tr>
                                <td><table width="100%" border="0" cellspacing="0">
                                        <%
                                        if(mBean.getTxtCal()==null)
                                        {
                                        mBean.setTxtCal("");
                                        }
                                        if(mBean.getHours()==null)
                                        {
                                        mBean.setHours("0");
                                        }
                                         if(mBean.getMinutes()==null)
                                        {
                                        mBean.setMinutes("0");
                                        }
                                        %>
                                        <tr>
                                            <td class="style2">Use Case Id </td>
                                            <td><html:select property="useCaseId" styleClass="selectboxstyle">
                                                    <html:optionsCollection name="usecaseidlist"/>
                                                </html:select></td>
                                            <td></td>
                                            <td class="style2">Date</td>
                                            <td><html:text property="txtCal" styleId="txtcal" value="<%=mBean.getTxtCal()%>"/></td>
                                            <td></td>
                                            <td><img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
                                            <td><table><tr>
                                                        <td class="style2" >Time </td>
                                                        <td><html:select property="hours" value="<%=mBean.getHours()%>">
                                                                <html:optionsCollection name="hourslist" />
                                                            </html:select></td>
                                                        <td class="style2" >Hrs</td>
                                                        <td><html:select property="minutes" value="<%=mBean.getMinutes()%>">
                                                                <html:optionsCollection name="minuteslist" />
                                                            </html:select></td>
                                                        <td class="style2">Mins</td>
                                                    </tr></table></td>
                                            <td class="style2" >&nbsp;</td>
                                        </tr>

                                        <%
                                         if(mBean.getDocSetKeyValue()==null)
                                     {
                                     mBean.setDocSetKeyValue("");
                                     }
                                      if(mBean.getFlowFrom()==null)
                                     {
                                     mBean.setFlowFrom("select FlowFrom");
                                     }
                                      if(mBean.getStatus()==null)
                                     {
                                     mBean.setStatus("select Status");
                                     }
                                        %>
                                        <tr>
                                            <td class="style2">Doc Set Key Value</td>
                                            <td><html:text property="docSetKeyValue" value="<%=mBean.getDocSetKeyValue()%>"/></td>
                                            <td></td>
                                            <td class="style2">Flow From</td>
                                            <td><html:select property="flowFrom" styleClass="selectboxstyle" value="<%=mBean.getFlowFrom()%>">
                                                    <html:optionsCollection name="flowlist" />
                                                </html:select></td>
                                            <td></td>
                                            <td class="style2">Status</td>
                                            <td><br><html:select property="status" styleClass="selectboxstyle" value="<%=mBean.getStatus()%>">

                                                </html:select></td>
                                            <td >&nbsp;</td>
                                           <td><img src="<%=path%>/img/search1.gif" id="search" border="0" onclick="searchform()"
                                                     style="cursor: pointer; cursor: hand;"/>
                                            </td>                                              
                                            <td >&nbsp;</td>                                       
                                            <td><input type="button" class="buttonStyleNew" value="Show All" onclick="search1form()" style="cursor: pointer; cursor: hand;"/></td>
                                        </tr>

                                    </table></td>
                            </tr>
                        </table></td>
                </tr>
                <tr>
                    <td height="15"  class="headerbluesmall">&nbsp;&nbsp;List of Data Exchange </td>
                </tr>
                <tr>
                    <td>
                        <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" >


                                <display:table name="<%=useCaseList%>" pagesize="<%=pageSize %>" class="displaytagstyle"  style="width:100%" sort="list" id="monitortable" >

                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Usecases details displayed,For more Usecases click on page numbers.
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
                                    <display:setProperty name="paging.banner.item_name" value="DataExchangeTransaction"/>
                                    <display:setProperty name="paging.banner.items_name" value="DataExchangeTransactions"/>

                                    <display:column property="usecaseCode" title="&nbsp;&nbsp;&nbsp;Use Case ID" sortable="true"/>

                                    <display:column property="usecaseName" title="&nbsp;&nbsp;&nbsp;Name" sortable="true"/>

                                    <display:column property="docSetKeyValue" title="&nbsp;&nbsp;&nbsp;Doc Set Key Value" sortable="true"/>

                                    <display:column property="flowFrom" title="&nbsp;&nbsp;&nbsp;Flow From" sortable="true"/>

                                    <display:column property="status" title="&nbsp;&nbsp;&nbsp;Status" sortable="true"/>

                                    <display:column property="useCaseDate" title="&nbsp;&nbsp;&nbsp;Date" sortable="true"/>

                                </display:table>
                            </table></div></td>
                </tr>



                <html:hidden property="buttonValue"/>

            </table>
        </html:form>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.beans.BatchesBean,java.util.*,com.gp.cvst.logisoft.domain.Batch"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
BatchesBean batchesBean=new BatchesBean();
batchesBean.setCopy("search");
if(session.getAttribute("search1")!=null)
{
session.removeAttribute("search1");
}
if(request.getParameter("batchId")!=null)
{
session.setAttribute("batchId",request.getParameter("batchId"));
}
String buttonValue = (String)request.getAttribute("buttonValue");
if(null==buttonValue || !buttonValue.trim().equals("search")){
    if(session.getAttribute("copyList")!=null){
        session.removeAttribute("copyList");
    }
}
if(request.getAttribute("batchesBean")!=null)
{
batchesBean=(BatchesBean)request.getAttribute("batchesBean");

batchesBean.setCopy(batchesBean.getCopy());
}
String copy=batchesBean.getCopy();
request.setAttribute("batchesBean",batchesBean);
List batchLst=new ArrayList();
String ab=(String)request.getAttribute("buttonValue");
if(ab!=null && ab.equals("completed"))
{
%>    
<script>
    self.close();
    opener.location.href="<%=path%>/jsps/Accounting/JournalEntry.jsp";
</script>
<%}

if(session.getAttribute("copyList")!=null)
{
batchLst=(List)session.getAttribute("copyList");
}
session.setAttribute("search","copy");
String editPath=path+"/copyBatch.do";
%>
<html> 
    <head>
        <base href="<%=basePath%>">
        <title>JSP for City selectionform</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">

        <%@include file="../includes/baseResources.jsp" %>

        <script type="text/javascript">
            function search(val)
            {
                if(val=='search')
                {
                    document.getElementById("search").style.visibility = 'visible';
                }
                else if(val=='new')
                {
                    document.getElementById("search").style.visibility = 'hidden';
                }
            }
            function new1(fromAction){
                if(fromAction=="Copy"){
                    document.copyBatchForm.buttonValue.value="new"
                }else{
                    document.copyBatchForm.buttonValue.value="newAndReverse"
                }
                document.copyBatchForm.submit();
            }
            function search1() {
                document.copyBatchForm.buttonValue.value="search"
                document.copyBatchForm.submit();
            }
	
	
        </script>
    </head>
    <c:if test="${not empty param.fromAction}">
        <c:set var="fromAction" value="${param.fromAction}"/>
    </c:if>
    <c:if test="${not empty param.batchId}">
        <c:set var="batchId" value="${param.batchId}"/>
    </c:if>
    <body class="whitebackgrnd" onload="search('<%=copy%>')">
        <html:form action="/copyBatch?fromAction=${fromAction}&batchId=${batchId}" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <td class="textlabels">

                    <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew">${fromAction} Batch To

                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr class="style2">
                                <td align="center">Existing Batch
                                    <html:radio property="copy" value="search" name="batchesBean"  onclick="search('search')"></html:radio></td>
                                <td align="center">Create New Batch
                                    <html:radio property="copy" value="new" name="batchesBean" onclick="new1('${fromAction}>')"></html:radio></td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class="tableHeadingNew">Search Criteria

                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" id="search">
                            <tr >
                                <td class="textlabels">Batch No</td>
                                <td>
                                    <html:text property="batchno"  value="" size="3"/>
                                </td>
                                <td class="textlabels">Batch Desc</td>
                                <td><html:text property="desc" value=""  />
                                </td>

                                <td><img src="<%=path%>/img/search1.gif" onclick="search1()" /></td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0">

                            <tr class="tableHeadingNew">Search Results

                            </tr>
                        </table>

                        <% int i=0;
                        %>
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <display:table name="<%=batchLst%>" pagesize="<%=pageSize%>" class="displaytagstyle">
                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner">
                                        <font color="blue">{0}</font> Batches displayed,For more Batches click on page numbers.
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
                                <display:setProperty name="paging.banner.item_name" value="Batch"/>
                                <display:setProperty name="paging.banner.items_name" value="Batches"/>
                                <%
                                    String batchno="";
                                    if(batchLst!=null && batchLst.size()>0){
                                        Batch p=(Batch)batchLst.get(i);
                                        if(p.getBatchId()!=null){
                                            batchno=p.getBatchId();
                                        }
                                    }
                                    String iStr=String.valueOf(i);
                                    String tempPath=editPath+"?index="+iStr+"&batchNo="+batchno;
                                %>

                                <display:column title="Batch Number"><a href="<%=tempPath%>&fromAction=${fromAction}"><%=batchno%></a></display:column>

                                <display:column property="batchDesc" title="Description"></display:column>
                                <%i++; %>
                            </display:table>
                        </div>



                    </table>
                </td>
            </table>

            <html:hidden property="buttonValue" styleId="buttonValue"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


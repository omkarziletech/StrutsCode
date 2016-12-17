<%@include file="../includes/jspVariables.jsp" %>
<%@ page import="com.gp.cvst.logisoft.util.*,java.util.*,com.gp.cvst.logisoft.beans.BatchesBean,com.gp.cvst.logisoft.domain.Batch,java.text.DecimalFormat,java.text.NumberFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%
		
String path = request.getContextPath();
String editPath=path+"/batches.do";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
NumberFormat numformat1 = new DecimalFormat("##,###,##0.00");
DBUtil dbUtil=new DBUtil();
request.setAttribute("sourceLedgers",dbUtil.getSourcecodeList(33,"no", "Select Source Code"));
request.setAttribute("batchNumbers",dbUtil.getBatchNumbersList());
List batchesList=(List)session.getAttribute("batchesList");
String message="";
String batchNo="";
if(session.getAttribute("batchNo")!=null)
{
batchNo=(String)session.getAttribute("batchNo");
}
if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");

List postedBatchList=new ArrayList();
if(session.getAttribute("postedBatchList")!=null)
{
postedBatchList=(List)session.getAttribute("postedBatchList");
if(postedBatchList!=null && postedBatchList.size()>0)
{

        %>
        <script>
            window.open("<%=path%>/jsps/Accounting/NonPostBatch.jsp","","width=420,height=150");
         
        </script>
        <%
        }
        }
        }
        %>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1"/>
        <title>GL Batches</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/baseResourcesForJS.jsp" %>
        <script type="text/javascript" src="<%=path%>/dwr/engine.js"></script>
        <script type="text/javascript" src="<%=path%>/dwr/util.js"></script>
        <script type="text/javascript" src="<%=path%>/dwr/interface/BatchesBC.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form action="/batches" name="batchForm"
                   type="com.gp.cvst.logisoft.struts.form.BatchForm" scope="request">
            <html:hidden property="index"/>
            <html:hidden property="buttonValue"/>
            <html:hidden property="readyToPost"/>
            <font color="blue" size="4"><c:out value="${message}"/></font>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>GL Batch</td>
                    <td align="right"><a id="searchToggle" href="#" onclick="Effect.toggle('searchDiv', 'slide'); return false;"><img src="<%=path%>/img/icons/up.gif" border="0"/></a></td>
                </tr>
                <tr>
                    <td>
                        <div id="searchDiv">
                            <div>
                                <table width="100%" cellpadding="3" cellspacing="0">
                                    <tr class="textlabelsBold">
                                        <td style="width: 10%">GL Batch Number</td>
                                        <td style="width: 20%">
                                            <input name="batchno" value="<%=batchNo%>" id="batchno" class="textlabelsBoldForTextBox"/>
                                            <dojo:autoComplete formId="batchForm" textboxId="batchno"  action="<%=path%>/actions/BatchNo.jsp?tabName=BATCHES&from=0"/>
                                        </td>
                                        <td style="width: 15%">Source Ledger</td>
                                        <td>
                                            <html:select property="sourceLedger" onchange="checkStatus(this)" styleClass="dropdown_accounting">
                                                <html:optionsCollection name="sourceLedgers" styleClass="unfixedtextfiledstyle"/>
                                            </html:select>
                                            <span id="subLedgerStatus" style="background-color: graytext;font-size: 15px"></span>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Show Posted and deleted batches</td>
                                        <td>
                                            <html:radio property="status" onclick="showAllBatches()" value="yes">Yes</html:radio>
                                            <html:radio property="status" value="no" onclick="showAllBatches()">No</html:radio>
                                        </td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td colspan="6" align="center">
                                            <input type="button" onclick="searchBatches()" value="Search" class="buttonStyleNew"/>
                                            <input type="button" onclick="showAllBatches()" value="Show All" class="buttonStyleNew"/>
                                            <input type="button" onclick="addnew()" value="Add New" class="buttonStyleNew"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <br/><br/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>List Of GL Batches</td>
                    <td align="right">
                        <a id="batchtoggle" href="#" onclick="Effect.toggle('listDiv', 'slide'); return false;">
                            <img src="<%=path%>/img/icons/up.gif" border="0"/>
                        </a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="listDiv">
                            <div class="scrolldisplaytable">
                                <c:set var="i" value="0"/>
                                <display:table name="${batchesList}" id="glBatch" pagesize="<%=pageSize%>"
                                               class="displaytagstyleNew" style="width:100%">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"> <font color="blue">{0}</font> GL Batch
						details displayed,For more batches click on page numbers. </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner"> One {0} displayed. Page Number </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner"> {0} {1} Displayed, Page Number </span>
                                    </display:setProperty>

                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner"> No Records Found. </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom"/>
                                    <display:setProperty name="paging.banner.item_name" value="GL Batch"/>
                                    <display:setProperty name="paging.banner.items_name" value="GL Batches"/>
                                    <display:column property="batchno" title="GL Batch#" paramId="paramid" href="<%=editPath%>"/>
                                    <display:column property="desc" title="Description"></display:column>
                                    <display:column property="sourceLedger" title="SourceLedger"></display:column>
                                    <display:column property="totalDebit" title="Total Debit"></display:column>
                                    <display:column property="totalCredit" title="Total Credit"></display:column>
                                    <display:column title="Ready to post">
                                        <c:choose>
                                            <c:when test="${!empty glBatch.status && (glBatch.status=='posted' || glBatch.status=='deleted')}">
                                                <c:choose>
                                                    <c:when test="${glBatch.readyToPost=='on'}">
                                                        <input type="checkbox" name="isReadyToPost" checked="checked" disabled="disabled"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="isReadyToPost" disabled="disabled"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${glBatch.readyToPost=='on'}">
                                                        <input type="checkbox" name="isReadyToPost" checked="checked"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="isReadyToPost"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </display:column>
                                    <display:column property="type" title="Batch Type"></display:column>
                                    <display:column property="status" title="status"></display:column>
                                    <display:column title="Actions">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Details</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" border="0" alt="" onclick="showDetails('${glBatch.batchno}')" name="${i}"/>
                                        </span>
                                        <c:choose>
                                            <c:when test="${!empty glBatch.status && (glBatch.status=='posted' || glBatch.status=='deleted')}">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="hotspot" onmouseover="tooltip.show('<strong>Post</strong>',null,event);" onmouseout="tooltip.hide();">
                                                    <img src="<%=path%>/img/icons/pubserv.gif" border="0" alt="" onclick="postBatch(${i},'${glBatch.batchno}')"/>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Print</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/print.gif" border="0" alt="" onclick="printBatch(${i})" name="${i}"/>
                                        </span>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Email</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/send.gif" border="0" alt="" onclick="openMailPopup(${row.batchno})" name="${i}"/>
                                        </span>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>ExportToExcel</strong>',null,event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/trigger.gif" border="0" alt="" onclick="exportToExcel('${i}')"/>
                                        </span>
                                        <c:choose>
                                            <c:when test="${!empty glBatch.status && (glBatch.status=='posted' || glBatch.status=='deleted')}">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
                                                    <img src="<%=path%>/img/icons/delete.gif" border="0" alt="" onclick="deleteBatch(${i},'${glBatch.batchno}')"/>
                                                </span>
                                                <span class="hotspot" onmouseover="tooltip.show('<strong>Update</strong>',null,event);" onmouseout="tooltip.hide();">
                                                    <img src="<%=path%>/img/icons/save.gif" border="0" alt="" onclick="updateBatch(${i})"/>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                        <img alt="Notes" src="<%=path%>/img/icons/info1.gif" border="0" onclick="GB_show('Notes', '<%=path%>/notes.do?moduleId='+'${notesConstants.GL_BATCH}&moduleRefId=${glBatch.batchno}',300,900);"/>
                                    </display:column>
                                    <c:set var="i" value="${i+1}"/>
                                </display:table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="batchId"/>
        </html:form>
    </body>
    <script type="text/javascript">
        dwr.engine.setTextHtmlHandler(dwrSessionError);
        useLogisoftLodingMessage();
        function checkStatus(obj){
            if(trim(obj.value)!="0" && trim(obj.value)!=""){
                BatchesBC.getSubLedgerStatus(obj.value,function(status){
                    if(null!=status){
                        dwr.util.setValue("subLedgerStatus",status);
                    }else{
                        dwr.util.setValue("subLedgerStatus","");
                    }
                });
            }else{
                dwr.util.setValue("subLedgerStatus","");
            }
        }


        function showAllBatches()
        {
            document.batchForm.buttonValue.value="showall";
            document.batchForm.submit();
        }
        function searchBatches()
        {
            if(document.batchForm.batchno.value=="")
            {
                alert("select the batch number");
                return false;
            }
            document.batchForm.buttonValue.value="search";
            document.batchForm.submit();
        }
        function addnew()
        {
            document.batchForm.buttonValue.value="addnew";
            document.batchForm.submit();
        }

        function showDetails(batchId){
            document.batchForm.batchId.value=batchId;
            document.batchForm.buttonValue.value="detail";
            document.batchForm.submit();
        }
        function postBatch(rowIndex,batchNo){
            var isReadyToPost = document.getElementsByName("isReadyToPost");
            if(isReadyToPost[rowIndex].checked){
                if(confirm("Do you want to post this batch no: "+batchNo)){
                    document.batchForm.index.value=rowIndex;
                    document.batchForm.buttonValue.value="post";
                    document.batchForm.submit();
                }
            }else{
                alert("Please make sure the post by checking the ready to post checkbox");
            }
        }
        function printBatch(rowIndex){
            document.batchForm.index.value=rowIndex;
            document.batchForm.buttonValue.value="batchReport";
            document.batchForm.submit();

        }
        function openMailPopup(glBatchNo){
            GB_showFullScreen('Email','<%=path%>/sendEmail.do?id='+ glBatchNo+'&moduleName=Batch');
        }
        function exportToExcel(rowIndex){
            window.location.href="<%=basePath%>servlet/ExportBatchNumberToExcel?batchIndex="+rowIndex;
        }
        function deleteBatch(rowIndex,batchNo){
            if(confirm("Do you want to delete this batch no: "+batchNo)){
                document.batchForm.index.value=rowIndex;
                document.batchForm.buttonValue.value="delete";
                document.batchForm.submit();
            }
        }

        function updateBatch(rowIndex){
            var isReadyToPost = document.getElementsByName("isReadyToPost");
            document.batchForm.index.value=rowIndex;
            if(isReadyToPost[rowIndex].checked){
                document.batchForm.readyToPost.value="on";
            }else{
                document.batchForm.readyToPost.value="off";
            }
            document.batchForm.buttonValue.value="save";
            document.batchForm.submit();
        }   
    </script>
    <c:if test="${!empty fileName}">
        <script type="text/javascript">
            window.parent.showGreyBox("GL Batch Report","<%=path%>/servlet/FileViewerServlet?fileName=${fileName}");
        </script>
    </c:if>
</html>

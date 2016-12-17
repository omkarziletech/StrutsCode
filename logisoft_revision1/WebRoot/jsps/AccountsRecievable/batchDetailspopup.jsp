<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%@page import="com.gp.cong.common.CommonConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
DecimalFormat df = new DecimalFormat("0.00");
String path = request.getContextPath();
String status=null;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
path="../..";
}
 ArBatchDAO arbatchDAO=new ArBatchDAO();
 String ind=request.getParameter("batchNo");
 String batchNo="";
 if(request.getParameter("batchNo")!=null){
  batchNo=(String)request.getParameter("batchNo");
  if(batchNo!=null && !batchNo.equals("")){
          ArBatch arBatch = arbatchDAO.findById(new Integer(batchNo));
          status = arBatch.getStatus();
  }
  
 }
PaymentChecksDAO pcdao= new PaymentChecksDAO();
 List paymentList = null;
 if(!batchNo.equals(""))
paymentList =  pcdao.batchPaymentDetails(batchNo);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Batchitems</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type='text/javascript' src='<%=path%>/dwr/engine.js'></script>
        <script type='text/javascript' src='<%=path%>/dwr/util.js'></script>
        <script type='text/javascript' src='<%=path%>/dwr/interface/ARBatchBC.js'></script>
        <script type='text/javascript' src='<%=path%>/js/common.js'></script>
    </head>
    <body class="whitebackgrnd">
	<div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="<%=path%>/img/icons/newprogress_bar.gif" >
            </form>
        </div>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew">Batch Details for "<%=batchNo%>"</tr>
            <td>
                <%
                List a = new ArrayList();
                if((paymentList !=null  && paymentList.size()>0)){
                int i=0;
                CustAddressDAO caDAO=new CustAddressDAO();
                %>
                <div id="ARinquiryListDiv" >
                    <div id="divtablesty1"  style="border:thin;overflow:auto;width:100%;height:350px;">
                        <display:table name="<%=paymentList%>" pagesize="<%=pageSize%>" class="displaytagstyleNew" style="width:100%" id="batchDetails" sort="list" >
                            <%
                            showpaymentsBean showbean = (showpaymentsBean) paymentList.get(i);
                            String custNum=showbean.getCustNo();
                            String custName=caDAO.getCustomerName(custNum);

                            String adjustAmount= df.format(showbean.getAmount());
                            String amountApplied="";
                            if(showbean.getAmountApplied()!=null)
                            amountApplied=df.format(showbean.getAmountApplied());
                            String bolNo=showbean.getBolNo();
                            String invoiceNo=showbean.getInvoiceNo();
                            String payementAmount=showbean.getPayments();
                            %>
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner">
                                    <font color="blue">{0}</font> Checks displayed,For more code click on page numbers.
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.one_item_found">
                                <span class="pagebanner">
						One {0} Displayed. Page Number
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
                            <display:setProperty name="paging.banner.item_name" value="Check"/>
                            <display:setProperty name="paging.banner.items_name" value="Checks"/>

                            <%if(AccountingConstants.CHECK_PAYMENT.equals(custNum)) {%>
                            <display:column property="custNo" title="Customer#" sortable="true" headerClass="sortable" style="color:blue;"><c:out value="<%=custNum%>"/></display:column>
                            <%}else{ %>
                            <display:column property="custNo" title="Customer#" sortable="true" headerClass="sortable"><c:out value="<%=custNum%>"/></display:column>
                            <%} %>
                            <display:column title="Customer Name" sortable="true" headerClass="sortable"><c:out value="<%=custName%>"/></display:column>
                            <display:column property="chequenumber" title="Check#"  sortable="true" headerClass="sortable"></display:column>
                            <display:column  title="ChkAmt" sortable="true" headerClass="sortable" style="text-align:right"><%=adjustAmount%></display:column>
                            <display:column  title="AmtApp" sortable="true" headerClass="sortable" style="text-align:right">
                                <c:out value="<%=amountApplied%>"/>
			    </display:column>
                            <%i++; %>
			    <c:if test="${param.canEdit}">
				<display:column title="Actions">
				    <span class="hotspot" onmouseover="tooltip.show('<strong>Edit Check</strong>',null,event);" onmouseout="tooltip.hide();">
					<img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editBatchDetails('<%=showbean.getPaymentId()%>','<%=custNum%>')"/>
				    </span>
				    <span class="hotspot" onmouseover="tooltip.show('<strong>Delete Check</strong>',null,event);" onmouseout="tooltip.hide();">
					<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteBatchDetails('<%=showbean.getPaymentId()%>')"/>
				    </span>
				</display:column>
			    </c:if>
                        </display:table>
                    </div>
                </div>
                <%} %>
                <%if(a!=null)
                session.setAttribute("lista",a);
                %>
            </td>
        </table>
    </body>
    <script type="text/javascript">
        dwr.engine.setTextHtmlHandler(dwrSessionError);
        function editBatchDetails(val1,customerNo){
            ARBatchBC.lockBatch('<%=batchNo%>',function(returnData){
                if(null!=returnData && returnData=="Locked"){
		    ARBatchBC.unLockTradingPartner(null,function(data){
			ARBatchBC.lockTradingPartner(customerNo,function(result){
			    if(result=="Locked"){
				parent.parent.getBatchDeatils(val1,'<%=batchNo%>');
			    }else{
				alert(result);
			    }
			});
		    });
                }else if(null!=returnData){
                    alert(returnData);
                }
            });
        }
        function deleteBatchDetails(val1){
            var deleteItem =val1;
            ARBatchBC.lockBatch('<%=batchNo%>',function(returnData){
                if(null!=returnData && returnData=="Locked"){
                    ARBatchBC.deletePaymentCheck(deleteItem,resultOfDeleteProcess);
                }else if(null!=returnData){
                    alert(returnData);
                }
            });
        }
        function resultOfDeleteProcess(data){
            if(data=='success'){
                alert("Payment Check has Deleted Successfully");
                window.location.reload();
			
            }else{
                alert("Sorry.. Payment Check cannot be deleted Now..");
            }
        }
	useLogisoftLodingMessageNew();
    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


<%@page import="com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO"%>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.bc.fcl.FclBlConstants,
	java.text.NumberFormat,java.text.DecimalFormat,com.gp.cvst.logisoft.domain.FclBl,com.gp.cong.logisoft.bc.fcl.FclBlBC,
	com.gp.cvst.logisoft.domain.FclBlCharges,com.gp.cvst.logisoft.domain.FclBlCostCodes,com.gp.cvst.logisoft.domain.CustAddress,
	java.text.SimpleDateFormat,com.gp.cvst.logisoft.beans.TransactionBean"%>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<script type="text/javascript" src="/logisoft/js/jquery/jquery.tooltip.js"></script>
  <script type="text/javascript" src="/logisoft/js/jquery/jquery.qtip-1.0.0-rc3.min.js"></script>
  <link type="text/css" rel="stylesheet" media="screen" href="/logisoft/css/jquery/jquery.tooltip.css" />
    <script type="text/javascript">
            jQuery(document).ready(function() {
                jQuery("[title != '']").not("link").tooltip();
            });
</script>
<%
String path = request.getContextPath();
boolean hasUserLevelAccess = roleDuty.isShowDetailedCharges();
boolean reSendAccruals = roleDuty.isResendAccruals();
String adminCostCode="";
String fFChargeCode="";
String fAEChargeCode="";
String fileNo="";
User userid=null;
String userName=null;
if(session.getAttribute("loginuser")!=null){
  userid=(User)session.getAttribute("loginuser");
  userName = userid.getLoginName();
}
 String view="";
if(session.getAttribute("view")!=null){
  view=(String)session.getAttribute("view");
}
 String path1="";
 String charges="";
 DBUtil dbUtil = new DBUtil();
 NumberFormat numberFormat=new DecimalFormat("###,###,##0.00");
request.setAttribute("printOnBill",dbUtil.getPrintOnBill());
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

List chargelist=null;
List costList=null;
String totalCost="0.00";
double totCost=0.0;
double totalCostOfCollapse=0.0;
double totCostForCharges=0.0;
double totCostForChargesDup=0.0;
double profit=0.0;
String total="0.00";
String billThirdParty="";
FclBl fclBl=new FclBl();
String alterAgent="";
String port="";
String issuingTerminal="";
Double shipperTotal=0.00;
Double notifyTotal=0.00;
String streamShipLine="";
String billThirdPartyAddress="";
Double forwarderTotal=0.00;
Double agentTotal=0.00;
Double consigneeTotal=0.00;
String billThirdPartyName="";
String readyToPOst="";
String check="";
String thirdParty="";
String readyToPost="";
Double thirdPartyTotal=0.00;
String forignAgent = "";
String agent=null;
String bol="";
if(request.getAttribute("bol")!=null){
	bol=(String)request.getAttribute("bol");
}
String msg="";
if(session.getAttribute("autoCostmsg")!=null){
	msg=(String)session.getAttribute("autoCostmsg");
}
String button="",agentNo="",closedBy="";
String routedAgentNo="",routedAgentName="",routedAgentCheckbox="";

if(request.getAttribute("button")!=null){
	button=(String)request.getAttribute("button");
}
if (session.getAttribute("check") != null) {
	check = (String) session.getAttribute("check");
}
FclBlBC fclBlBC=new FclBlBC();
CustAddress custAddress=new CustAddress();
boolean importFlag=false;
if(request.getAttribute("fclBl")!=null){
	fclBl=(FclBl)request.getAttribute("fclBl");
        importFlag=null!=fclBl.getImportFlag() && fclBl.getImportFlag().equalsIgnoreCase("I")?true:false;
        request.setAttribute("billToVendor", dbUtil.getBilltype(importFlag));
	if(fclBl.getFileNo()!=null){
		fileNo=fclBl.getFileNo();
	}
	if(CommonFunctions.isNotNull(fclBl.getAgentNo())){
		agent=fclBl.getAgentNo();
	}
	if(fclBl.getReadyToPost()!=null && fclBl.getReadyToPost().equals("M")){
		readyToPOst=fclBl.getReadyToPost();
	}
	if(fclBl.getRoutedByAgent()!=null && !fclBl.getRoutedByAgent().trim().equals("") ){
		routedAgentName=fclBl.getRoutedByAgent();
		if(!routedAgentName.equals("")){
		  custAddress=fclBlBC.getShipperDetails(routedAgentName);
		}
		routedAgentName=(null!=custAddress.getAcctName()?custAddress.getAcctName():"");
		routedAgentNo=(null!=custAddress.getAcctNo()?custAddress.getAcctNo():"");
	}
	if(fclBl.getTotal()!=null){
		total=numberFormat.format(fclBl.getTotal());
	}
	if(fclBl.getTotalCostCode()!=null){
		//totalCost=numberFormat.format(fclBl.getTotalCostCode());
	}
	if(fclBl.getRoutedAgentCheck()!=null){
	  routedAgentCheckbox=fclBl.getRoutedAgentCheck();
	}
	if(fclBl.getAgent()!=null){
		alterAgent=fclBl.getAgent();
	}
	if(fclBl.getAgentNo()!=null){
	  agentNo=fclBl.getAgentNo();
	}
	if(fclBl.getPortOfLoading()!=null){
		port=fclBl.getPortOfLoading();
	}
	if(fclBl.getTerminal()!=null){
		issuingTerminal=fclBl.getTerminal();
	}
	if(fclBl.getSslineName()!=null){
		streamShipLine=fclBl.getSslineName();
	}
	if(fclBl.getBillThirdPartyAddress()!=null){
		billThirdPartyAddress=fclBl.getBillThirdPartyAddress();
	}
	closedBy=(null!=fclBl.getClosedBy())?fclBl.getClosedBy():"";
}else{
    importFlag = (null != session.getAttribute(ImportBc.sessionName)) ? true : false;
}
request.setAttribute("importFlag", importFlag);
List addchargeslist=null;
if(request.getAttribute("addchargeslist")!=null){
	addchargeslist=(List)request.getAttribute("addchargeslist");
}
List collapseList=new ArrayList();
if(request.getAttribute("consolidatorList")!=null){
	collapseList=(List)request.getAttribute("consolidatorList");
}
List addcostcodeslist=null;
if(request.getAttribute(FclBlConstants.FCLBL_COSTS_LIST)!=null){
    addcostcodeslist=(List)request.getAttribute(FclBlConstants.FCLBL_COSTS_LIST);
    if(!addcostcodeslist.isEmpty()){
        FclBlCostCodes fclBlCostCodes = (FclBlCostCodes) addcostcodeslist.get(0);
        request.setAttribute("hasTransactionType", new FclBlCostCodesDAO().hasTransactionType(fclBlCostCodes.getBolId().toString()));
    }else{
        request.setAttribute("hasTransactionType",true);
    }
}else{
    request.setAttribute("hasTransactionType",true);
}
request.setAttribute("toggleCostTable", null!=addcostcodeslist?addcostcodeslist.isEmpty():true);
List collapseCostList=new ArrayList();
if(request.getAttribute("consolidatorCostList")!=null){
	collapseCostList=(List)request.getAttribute("consolidatorCostList");
}
if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
%>

<%}
if(request.getAttribute("path1")!=null){
	path1=(String)request.getAttribute("path1");
}
request.setAttribute("defaultcurrency",dbUtil.getGenericFCL1(new Integer(32)));
%>
<script language="javascript" type="text/javascript">
 var fclBlPrimaryKey=(trim('${param.bol}')!='')?'${param.bol}':'';
 var toggleCostTable=${toggleCostTable};
 var hasTransactionType=${hasTransactionType};
function addNewCharges(){
    if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
        document.fclBillLaddingform.readyToPost.checked = false;
    }
	if(!saveMessage('Adding Charges')){
  			return false;
  		}
  		 document.fclBillLaddingform.readyToPost.checked = false;
                 var fclSsblGoCollect = document.fclBillLaddingform.fclSsblGoCollect.value;
                 var noFFComm = "false";
        if(document.fclBillLaddingform.houseBL[2].checked && (document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FF ASSIGNED'||
                      document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FF ASSIGNED / B/L PROVIDED' ||
                      document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FRT. FORWARDER ASSIGNED')){
                        noFFComm = "true";
                }
 	GB_show('Charges','<%=path%>/fclBillLadding.do?button=addNewCharges&noFFComm='+noFFComm+'&fclSsblGoCollect='+fclSsblGoCollect+'&bolId='+fclBlPrimaryKey+'&collexpand='+'collapse',350,800);
}
function addCostAndCharges(chargesListSize){
     var fileNo = document.fclBillLaddingform.fileNo.value;
    if(document.fclBillLaddingform.eta.value == '' ){
        alertNew("Please Enter ETA to add Cost and Charges");
    }else{
    if(chargesListSize < 12){
        if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
            document.fclBillLaddingform.readyToPost.checked = false;
        }
        if(!saveMessage('Adding Charges')){
            return false;
        }
        var billTo="";
        if (document.fclBillLaddingform.billToCode[0].checked){
            billTo = "Forwarder";
        }else if(document.fclBillLaddingform.billToCode[1].checked){
            billTo = "Shipper";
        }else if(document.fclBillLaddingform.billToCode[2].checked){
            billTo = "ThirdParty";
        }else if(document.fclBillLaddingform.billToCode[3].checked){
            billTo = "Consignee";
        }
        GB_show('Charges','<%=path%>/jsps/fclQuotes/AddFclBlCostAndCharges.jsp?bolId='+fclBlPrimaryKey+'&billTo='+billTo+'&readyToPost=${fclBl.readyToPost}&importFlag=${importFlag}&fileNo='+fileNo,300,1000);
    }else{
        alertNew("Already reached maximum number of charges");
    }
    }
}

function editcharges(chargesId,val,amount,adjestmentAmount,manualCharge,oldAmount){
       if(document.fclBillLaddingform.readyToPost && document.getElementById("manifestButtonDown")!=null){
           document.fclBillLaddingform.readyToPost.checked = false;
       }
         var noFFComm = "false";
        if(document.fclBillLaddingform.houseBL && document.fclBillLaddingform.houseBL[2] && (document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FF ASSIGNED'||
              document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FF ASSIGNED / B/L PROVIDED' ||
              document.fclBillLaddingform.forwardingAgentName.value.trim()=='NO FRT. FORWARDER ASSIGNED')){
                noFFComm = "true";
        }
 	GB_show('Charges','<%=path%>/fclBillLadding.do?button=chargesEdit&noFFComm='+noFFComm+'&adjestmentAmount='+adjestmentAmount+'&rollUpAmount='+amount+
 	'&bolId='+fclBlPrimaryKey+'&chargesId='+chargesId+'&collexpand='+val+'&manualCharge='+manualCharge+'&oldAmount='+oldAmount,350,1000);
}
function chargesdelete(chargeId){
    if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
        document.fclBillLaddingform.readyToPost.checked = false;
    }
	document.fclBillLaddingform.chargeId.value=chargeId;
	document.fclBillLaddingform.buttonValue.value="deleteCharges";
    confirmYesOrNo("Are you sure you want to delete this charge","Chargedelete");
}
function addNewCosts(){ //costCodeAdd()
	if(!saveMessage('Adding CostCode')){
  			return false;
  		}
         if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
           document.fclBillLaddingform.readyToPost.checked = false;
       }
       var masterbl = document.fclBillLaddingform.newMasterBL.value;
       var fileNo = document.fclBillLaddingform.fileNo.value;
 	GB_show('Add Accruals','<%=path%>/jsps/fclQuotes/AddFclBlCostsPopUp.jsp?bolId='+fclBlPrimaryKey+'&masterBl='+masterbl+'&importFlag='+'${importFlag}' + '&fileNo=' +fileNo,350,1000);
}
function editCosts(costId,amount){
    if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
           document.fclBillLaddingform.readyToPost.checked = false;
       }
       var masterbl = document.fclBillLaddingform.newMasterBL.value;
 	GB_show('Edit Accruals','<%=path%>/fclBillLadding.do?button=editCostDetails&rollUpAmount='+amount+'&bolId='+fclBlPrimaryKey+'&costId='+costId+'&masterBl='+masterbl,350,1000);
}
function FEACalculation(){
    if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
        document.fclBillLaddingform.readyToPost.checked = false;
    }
	document.fclBillLaddingform.bol.value=fclBlPrimaryKey;
	document.fclBillLaddingform.buttonValue.value="FEACalculation";
	if(document.fclBillLaddingform.agentNo.value==''){
           alertNew("Agent Name and Number cannot be empty");
           return;
        }
	if(document.fclBillLaddingform.routedAgentCheck.value=='' && document.fclBillLaddingform.editAgentNameCheck.value !='on'){
           alertNew("Please Select ERT");
           return;
        }
    document.fclBillLaddingform.differenceOfCostAndCharges.value=document.getElementById("displayProfit").innerHTML;
    confirmYesOrNo("Are you sure you want do FAE Calculation","FAECalculation");
}
function deleteCostDetails(costId){
    if(document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown")!=null){
        document.fclBillLaddingform.readyToPost.checked = false;
    }
	document.fclBillLaddingform.costCodeId.value=costId;
	document.fclBillLaddingform.buttonValue.value="deleteCostDetails";
    confirmYesOrNo("Are you sure you want to delete this CostCode","deleteCostCode");
}

function resendcost(costId){
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.fclBillLaddingform.costCodeId.value=costId;
    document.fclBillLaddingform.buttonValue.value="refreshManifestFlag";
    document.fclBillLaddingform.submit();
    
}



function getUpdatedBL(){
  //setTimeout("getUpdatedCompleteBL()",400);
}
function addCommissions(){
	if(document.getElementById("addCommissionButton").style.backgroundColor == "#c10b12"){
		alert("Commissions Added Already!!!");
	}else{
		confirmYesOrNo("Are You Sure","addCommissions");
	}
}
function convertToAccruals(){
	if(document.getElementById("convertToAccrualsButton").style.backgroundColor == "#c10b12"){
		alert("Converted To Accrual Already!!!");
	}else{
		confirmYesOrNo("Are You Sure","convertToAccrual");
	}
}
function displayUserForCommisionOrAccrual(){
	FclBlBC.displayUserForCommisionOrAccrual(fclBlPrimaryKey,function(FclBl){
		if(null!=FclBl){
			if(null!=FclBl.commissionsAddedBy && null!=FclBl.commissionsAddedDate){
				document.getElementById("commissionsAddedById").style.display="block";
				document.getElementById("commissionsAddedDateId").style.display="block";
				document.getElementById("commissionsAddedBy").innerHTML=FclBl.commissionsAddedBy.toUpperCase();
				document.getElementById("commissionsAddedDate").innerHTML=FclBl.commissionDisplayDate;
				document.getElementById("addCommissionButton").style.backgroundColor="#C10B12";
			}else{
				document.getElementById("commissionsAddedById").style.display="none";
				document.getElementById("commissionsAddedDateId").style.display="none";
			}
			if(null!=FclBl.accrualConvertedDate && null!=FclBl.accrualConvertedBy){
				document.getElementById("accrualConvertedById").style.display="block";
				document.getElementById("accrualConvertedDateId").style.display="block";
				document.getElementById("accrualConvertedDate").innerHTML=FclBl.accrualDisplayDate;
				document.getElementById("accrualConvertedBy").innerHTML=FclBl.accrualConvertedBy.toUpperCase();
				document.getElementById("convertToAccrualsButton").style.backgroundColor="#C10B12";
			}else{
				document.getElementById("accrualConvertedById").style.display="none";
				document.getElementById("accrualConvertedDateId").style.display="none";
			}
		}
	});
}
function manifestCost(costId){
	FclBlBC.manifestCost(costId,function(data){
	 if(data!=null ){
	 	alert("Cost got Manifested");
	 	// expand list......
	 	document.getElementById("manifestExpand"+costId).style.display="none";
	    document.getElementById("editExpand"+costId).style.display="none";
	    document.getElementById("deleteExpand"+costId).style.display="none";
	    document.getElementById("accrualsExpand"+costId).value="AC" ;
	    // collaps List ......
	 	document.getElementById("manifestCollaps"+costId).style.display="none";
	    document.getElementById("editCollaps"+costId).style.display="none";
	    document.getElementById("deleteCollaps"+costId).style.display="none";
	    document.getElementById("accrualsCollaps"+costId).value="AC" ;
	 }
	});
}

</script>
</head>

<body>
<html:hidden property="bolId" value="${param.bol}" />

<table width="100%" border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" >
	<tr class="tableHeadingNew" >
		<td>
		<%
	  		if(userid!=null && hasUserLevelAccess){%>
	  		 <img src="<%=path%>/img/icons/up.gif" border="0" onclick="showCharges()" id="chargesExpandImage"/>
	      <%}%>
		Charges </td>
                
		<td align="right" class="textlabelsBold">
                         <c:choose>
                             <c:when test="${importFlag eq true && fclBl.blClosed != 'Y' && fclBl.readyToPost != 'M'}">
                                 <input type="button" value="Add Cost/Charge" class="buttonStyleNew" id="addCostAndCharge" style="width:100px" onclick="addCostAndCharges(<%=collapseList.size()%>)"/>
                             </c:when>
                             <c:otherwise>
                        <input type="button" value="Add" class="buttonStyleNew" id="add" onclick="addNewCharges()"/>
                             </c:otherwise>
                         </c:choose>
		</td>
	</tr>
	<tr >
		<td colspan="2">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
<%
         int i=0;
%>
		<display:table  name="<%=addchargeslist%>"  id="chargestable" class="displaytagstyle" pagesize="<%=pageSize%>" >
			<display:setProperty name="basic.msg.empty_list">
			<span style="display:none;" class="pagebanner" />
			</display:setProperty>
			<display:setProperty name="paging.banner.placement" value="false">
		<span style="display:none;"></span>
		</display:setProperty>
	<%
	String charge="", amount="0.00",billTo="",adjustment="0.00",printBill="",chargeCode="",currency="USD",readyToPost1="",prepaid="",collect="";
	String id="",readOnlyFlag="",chargesRemarks="",bookingFlag="",oldAmount="0.00",totalAmount="0.00";
	TransactionBean transactionBean = new TransactionBean();
	transactionBean.setReadyToPost("off");

        if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("P-Prepaid")) {
		transactionBean.setPcollect("prepaid");
		prepaid="prepaid";
	}
        if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("C-Collect")) {
		transactionBean.setPcollect("collect");
		collect="collect";
	}
        if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("T-Third Party")) {
            billTo = "ThirdParty";
	}
	if(addchargeslist!=null && addchargeslist.size()>0){
		FclBlCharges fclblCharges=(FclBlCharges)addchargeslist.get(i);
		if(fclblCharges.getChargesId()!=null){
			id=fclblCharges.getChargesId().toString();
		}else{
			id=null;
		}
		if(fclblCharges.getPcollect()!=null && fclblCharges.getPcollect().equals("prepaid")){
			prepaid="prepaid";
			collect="";
		}
		if(fclblCharges.getPcollect()!=null && fclblCharges.getPcollect().equals("collect")){
			collect="collect";
			prepaid="";
		}
		if(fclblCharges.getCharges()!=null){
			charge=fclblCharges.getCharges();
		}
		if(fclblCharges.getChargeCode()!=null){
			chargeCode=fclblCharges.getChargeCode();
		}
		if(fclblCharges.getReadyToPost()!=null){
			transactionBean.setReadyToPost(fclblCharges.getReadyToPost());
		}
		if(fclblCharges.getReadyToPost()!=null &&(fclblCharges.getReadyToPost().equals("M")
					|| fclblCharges.getReadyToPost().equals("on"))){
			transactionBean.setReadyToPost("on");
			readyToPost=fclblCharges.getReadyToPost();
			readyToPost1=fclblCharges.getReadyToPost();
		}

		if(null!=fclblCharges.getReadOnlyFlag()){
		  readOnlyFlag=fclblCharges.getReadOnlyFlag();
		}
                if(null!=fclblCharges.getBookingFlag()){
		    bookingFlag=fclblCharges.getBookingFlag();
		}else{
			bookingFlag="";
		}
		if(!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")){
                    amount=numberFormat.format(fclblCharges.getAmount());
                    oldAmount=numberFormat.format(fclblCharges.getOldAmount());
                    if(null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d){
			adjustment = numberFormat.format(fclblCharges.getAdjustment());
                        totalAmount = numberFormat.format(fclblCharges.getAmount()+fclblCharges.getAdjustment());
                    }
                }else if(fclblCharges.getOldAmount()!=null){
                    amount=numberFormat.format(fclblCharges.getOldAmount());
                    if(null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d){
			adjustment = numberFormat.format(fclblCharges.getAdjustment());
                        totalAmount = numberFormat.format(fclblCharges.getOldAmount()+fclblCharges.getAdjustment());
                    }
		}
                totCostForCharges=totCostForCharges+fclblCharges.getAmount();
		if(fclblCharges.getBillTo()!=null){
			billTo=fclblCharges.getBillTo();
		}
		if(fclblCharges.getPrintOnBl()!=null){
			printBill=fclblCharges.getPrintOnBl();
		}
		if(fclblCharges.getCurrencyCode()!=null){
			currency=fclblCharges.getCurrencyCode();
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("ThirdParty")){
			thirdParty=fclblCharges.getBillTo();
			thirdPartyTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Shipper")){
			shipperTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("NotifyParty")){
			notifyTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Agent")){
			agentTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase(FclBlConstants.CONSIGNEE)){
			consigneeTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}

		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Forwarder")){
			forwarderTotal+=(null!=fclblCharges.getAmount())?fclblCharges.getAmount():0.00;
		}

		if(null!=fclblCharges.getChargesRemarks()){
		  chargesRemarks=fclblCharges.getChargesRemarks();
		}

		StringBuilder s=new StringBuilder();
		if(chargesRemarks!=null){
			int index=0;
			char[] commentArray = chargesRemarks.toCharArray();
			for (int j = 0; j < commentArray.length; j++) {
				if(commentArray[j]=='\n'){
					s.append(chargesRemarks.substring(index, j).trim());
					s.append(" ");
					index = j+1;
				}
			}
			s.append(chargesRemarks.substring(index, chargesRemarks.length()).trim());
		}
		s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
		s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		chargesRemarks=s.toString();

	}
	request.setAttribute("transactionBean",transactionBean);
	request.setAttribute("totalAmountExpand",totalAmount);
%>
  <%if(bookingFlag.equalsIgnoreCase("new")){ %>
	  <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
  <%} else if(readOnlyFlag.equalsIgnoreCase("on")){ %>
     <display:column style="visibility:hidden;width:8px;" title=""></display:column>
  <%}else{ %>
     <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
  <%}%>
	 <display:column title="Chg Code" style="width:150px">
		 <input type="text" name="chargeCode<%=i%>" class="BackgrndColorForTextBox" id="chargeCode" readonly="true"
		    value="<%=chargeCode%>"  onkeydown="getComCodeForCharges(this.value,<%=i%>)" size="4" />
     	 <dojo:autoComplete formId="fclBillLaddingform"  textboxId="chargeCode<%=i%>"
		    action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=i%>"/>
     </display:column>
     <display:column title="Chg Code Desc" style="width:350px">
		 <input type="text" name="charge<%=i%>" class="BackgrndColorForTextBox"  id="charge" readonly="true"
		  value="<%=charge%>" onkeydown="getChargeCodeDescription(this.value,<%=i%>)" />
     	 <dojo:autoComplete formId="fclBillLaddingform" textboxId="charge<%=i%>"
		   action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FCL_BL_CHARGES&index=<%=i%>"/>
	 </display:column>
         
	 <display:column title="Amount" style="width:120px">
	 		<html:text styleClass="BackgrndColorForTextBox" property="amount" readonly="true"
	 		   value="<%=amount%>" size="4" ></html:text>
	 </display:column>
	 <display:column title="Adjustment" style="width:100px">
	  	<input type="text" name="adjustment" maxlength="8"  value="<%=adjustment %>"
	  	 class="BackgrndColorForTextBox" readonly="true"  size="4"  />
                <c:if test="${not empty totalAmountExpand && totalAmountExpand != '0.00'}">
                    &nbsp;&nbsp;(Tot $ ${totalAmountExpand})
                </c:if>
     </display:column>
	 <display:column title="Currency" style="width:100px">
	 	<html:text property="currency" value="<%=currency%>"
	 		styleClass="BackgrndColorForTextBox" readonly="true" size="4"/>
     </display:column>
	 <display:column title="Bill To Party" style="width:120px">
	 	<html:text property="billTo" value="<%=billTo%>"
	 		styleClass="BackgrndColorForTextBox" readonly="true" size="8"/>
     </display:column>
	 <display:column title="Print On BL" style="width:120px">
	  	<html:text property="printBl" value="<%=printBill%>"
	 		styleClass="BackgrndColorForTextBox" readonly="true" size="4"/>
     </display:column>
     <display:column title="Action" style="width:100px">

  		<%if(null != chargesRemarks && !chargesRemarks.trim().equals("")){ %>
     		<span class="hotspot" onmouseover="tooltip.showComments('<strong><%=chargesRemarks%></strong>',100,event);"
				onmouseout="tooltip.hideComments();" style="color:black;">
			   <img id="viewgif1"  src="<%=path%>/img/icons/view.gif"/></span>
	    <%}if(!chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)){%>
		    <c:choose>
			<c:when test="${fclBl.readyToPost=='M'}">
			    <c:set var="editTooltip" value="Quick CN"/>
			</c:when>
			<c:otherwise>
			    <c:set var="editTooltip" value="Edit"/>
			</c:otherwise>
		    </c:choose>
                    <%if(!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")){%>
                        <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                             <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment %>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                             id="chargesexpand"/></span>
                    <%}else{%>
                        <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                             <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment %>')" name="<%=i%>"
                             id="chargesexpand"/></span>
                    <%}%>
            <%}if(!readOnlyFlag.equalsIgnoreCase("on") && !chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE) && !chargeCode.equalsIgnoreCase("INSURE")){ %>
                    <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
                            <img src="<%=path%>/img/icons/delete.gif" border="0" onclick="chargesdelete('<%=id%>')" name="<%=i%>"
                            id="chargesDeleteExpand"/> </span>
            <%}%>
	 </display:column>

  	 <display:column title="" style="display:none;" >
		<%if(prepaid.equals("prepaid")){ %>
			<input type="radio" name="pcollect<%=i%>" value="prepaid"  disabled="disabled" checked="checked"/>Prepaid
		<%}else{ %>
			<input type="radio" name="pcollect<%=i%>"  disabled="disabled" value="prepaid"/>Prepaid
		<%} if(collect.equals("collect")){ %>
			<input type="radio" name="pcollect<%=i%>"  disabled="disabled" value="collect" checked="checked"/>Collect
		<%}else{ %>
			<input type="radio" name="pcollect<%=i%>"  disabled="disabled" value="collect"/>Collect
		<%} %>
     </display:column>

	<%i++; %>
    </display:table>
   </table></td>
    </tr>

    <tr>
		<td colspan="2">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
<%
         i=0;
%>
		<display:table  name="<%=collapseList%>"   id="collapsetable" class="displaytagstyle" pagesize="<%=pageSize%>" >
		<display:setProperty name="basic.msg.empty_list">
			<span style="display:none;" class="pagebanner" />
			</display:setProperty>
		<display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
	<%
        String charge = "", amount = "0.00", adjustment = "0.00", billTo = "", printBill = "", chargeCode = "", currency = "USD", readyToPost1 = "", prepaid = "", collect = "";
        String id = "", readOnlyFlag = "", chargesRemarks = "", bookingFlag = "", oldAmount = "0.00", totalAmount = "0.00";
	TransactionBean transactionBean = new TransactionBean();
	transactionBean.setReadyToPost("off");

                            if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("P-Prepaid")) {
		transactionBean.setPcollect("prepaid");
		prepaid="prepaid";
	}
                if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("C-Collect")) {
		transactionBean.setPcollect("collect");
		collect="collect";
	}
                if (fclBl.getHouseBl() != null && fclBl.getHouseBl().equals("T-Third Party")) {
                    billTo = "ThirdParty";
	}
	if(collapseList!=null && collapseList.size()>0){
		FclBlCharges fclblCharges=(FclBlCharges)collapseList.get(i);
		if(fclblCharges.getChargesId()!=null){
			id=fclblCharges.getChargesId().toString();
		}else{
			id=null;
		}
		if(fclblCharges.getPcollect()!=null && fclblCharges.getPcollect().equals("prepaid")){
			prepaid="prepaid";
			collect="";
		}
		if(fclblCharges.getPcollect()!=null && fclblCharges.getPcollect().equals("collect")){
			collect="collect";
			prepaid="";
		}
		if(fclblCharges.getReadyToPost()!=null){
			transactionBean.setReadyToPost(fclblCharges.getReadyToPost());
		}
		if(fclblCharges.getReadyToPost()!=null &&(fclblCharges.getReadyToPost().equals("M")
					|| fclblCharges.getReadyToPost().equals("on"))){
			transactionBean.setReadyToPost("on");
			readyToPost=fclblCharges.getReadyToPost();
			readyToPost1=fclblCharges.getReadyToPost();
		}

			charge=fclblCharges.getCharges();
			chargeCode=fclblCharges.getChargeCode();
		if(null!=fclblCharges.getReadOnlyFlag()){
		  readOnlyFlag=fclblCharges.getReadOnlyFlag();
		}
                if(null!=fclblCharges.getBookingFlag()){
		    bookingFlag=fclblCharges.getBookingFlag();
		}else{
			bookingFlag="";
		}
		if(!readOnlyFlag.equalsIgnoreCase("on")|| bookingFlag.equalsIgnoreCase("new")){
                    amount=numberFormat.format(fclblCharges.getAmount());
                    oldAmount=numberFormat.format(fclblCharges.getOldAmount());
                    if(null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d){
			adjustment = numberFormat.format(fclblCharges.getAdjustment());
                        totalAmount = numberFormat.format(fclblCharges.getAmount()+fclblCharges.getAdjustment());
                    }
                }else if(fclblCharges.getOldAmount()!=null){
                    amount=numberFormat.format(fclblCharges.getOldAmount());
                    if(null != fclblCharges.getAdjustment() && fclblCharges.getAdjustment() != 0d){
			adjustment = numberFormat.format(fclblCharges.getAdjustment());
                        totalAmount = numberFormat.format(fclblCharges.getOldAmount()+fclblCharges.getAdjustment());
                    }
		}
		if(fclblCharges.getBillTo()!=null){
			billTo=fclblCharges.getBillTo();
		}
		if(fclblCharges.getPrintOnBl()!=null){
			printBill=fclblCharges.getPrintOnBl();
		}
		if(fclblCharges.getCurrencyCode()!=null){
			currency=fclblCharges.getCurrencyCode();
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("ThirdParty")){
			thirdParty=fclblCharges.getBillTo();
			//thirdPartyTotal+=fclblCharges.getAmount();
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Shipper")){
			//shipperTotal+=fclblCharges.getAmount();
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Agent")){
			//agentTotal+=fclblCharges.getAmount();
		}
		if(fclblCharges.getBillTo()!=null && fclblCharges.getBillTo().equalsIgnoreCase("Forwarder")){
			//forwarderTotal+=fclblCharges.getAmount();
		}
		if(null!=fclblCharges.getChargesRemarks()){
		  chargesRemarks=fclblCharges.getChargesRemarks();

		}
		StringBuilder s=new StringBuilder();
		if(chargesRemarks!=null){
			int index=0;
			char[] commentArray = chargesRemarks.toCharArray();
			for (int j = 0; j < commentArray.length; j++) {
				if(commentArray[j]=='\n'){
					s.append(chargesRemarks.substring(index, j).trim());
					s.append(" ");
					index = j+1;
				}
			}
			s.append(chargesRemarks.substring(index, chargesRemarks.length()).trim());

		}
		s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
		s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		chargesRemarks=s.toString();
	}
	request.setAttribute("transactionBean",transactionBean);
	request.setAttribute("totalAmountCollapse",totalAmount);
%>

 <%if(bookingFlag.equalsIgnoreCase("new")){ %>
	 <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
  <%} else if(readOnlyFlag.equalsIgnoreCase("on")){ %>
     <display:column style="visibility:hidden;width:8px;" title=""></display:column>
  <%}else{ %>
     <display:column title="" style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
  <%}%>
	 <display:column title="Chg Code" style="width:150px">
		 <input type="text" name="chargeCode<%=i%>" class="BackgrndColorForTextBox" id="chargeCode" readonly="true"
		    value="<%=chargeCode%>"  onkeydown="getComCodeForCharges(this.value,<%=i%>)" size="4" tabindex="-1" />
     	 <dojo:autoComplete formId="fclBillLaddingform"  textboxId="chargeCode<%=i%>"
		    action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=i%>"/>
     </display:column>
     <display:column title="Chg Code Desc" style="width:350px">
		 <input type="text" name="charge<%=i%>" class="BackgrndColorForTextBox"  id="charge" readonly="true"
		  value="<%=charge%>" onkeydown="getChargeCodeDescription(this.value,<%=i%>)" tabindex="-1" />
     	 <dojo:autoComplete formId="fclBillLaddingform" textboxId="charge<%=i%>"
		   action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=FCL_BL_CHARGES&index=<%=i%>"/>
	 </display:column>
	  <display:column title="Amount" style="width:100px">
	 		<html:text styleClass="BackgrndColorForTextBox" property="amount" readonly="true"
	 		   value="<%=amount%>" size="4" tabindex="-1"></html:text>
	 </display:column>
	 <display:column title="Adjustment" style="width:120px">
	 	<fmt:formatNumber type="number"  var="adjustment"  pattern="########0.00" />
	  	<input type="text" name="adjustment" maxlength="8"  value="<%=adjustment %>"
	  	 class="BackgrndColorForTextBox" readonly="true"  size="4" tabindex="-1" />
                <c:if test="${not empty totalAmountCollapse && totalAmountCollapse != '0.00'}">
                    &nbsp;&nbsp;(Tot $ ${totalAmountCollapse})
                </c:if>
     </display:column>
	 <display:column title="Currency" style="width:100px">
	 	<html:text property="currency" value="<%=currency%>"
	 		styleClass="BackgrndColorForTextBox" readonly="true" size="4" tabindex="-1" />
     </display:column>
	 <display:column title="Bill To Party" style="width:120px">
	 	<html:text property="billTo" value="<%=billTo%>"
	 		styleClass="BackgrndColorForTextBox" readonly="true" size="8" tabindex="-1" />
     </display:column>
	 <display:column title="Print On BL" style="width:120px">
	 	<html:text property="printBl" value="<%=printBill%>"
	 	    styleClass="BackgrndColorForTextBox" readonly="true" size="4" maxlength="4" tabindex="-1" />
     </display:column>
     <display:column title="Action" style="width:100px">
  		<%if(null != chargesRemarks && !chargesRemarks.trim().equals("")){ %>
     		<span class="hotspot" onmouseover="tooltip.showComments('<strong><%=chargesRemarks%></strong>',100,event);"
				onmouseout="tooltip.hideComments();" style="color:black;">
			   <img id="viewgif2"  src="<%=path%>/img/icons/view.gif"/></span>
                <%}if(!chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE)){%>
		    <c:choose>
			<c:when test="${fclBl.readyToPost=='M'}">
			    <c:set var="editTooltip" value="Quick CN"/>
			</c:when>
			<c:otherwise>
			    <c:set var="editTooltip" value="Edit"/>
			</c:otherwise>
		    </c:choose>
                    <c:choose>
                        <c:when test="${importFlag eq true}">
                            <%if(!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")){%>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                                     <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','collapse','<%=amount%>','<%=adjustment %>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                                     id="chargesexpandImport"/></span>
                            <%}else{%>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                                     <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment %>')" name="<%=i%>"
                                     id="chargesexpandImport"/></span>
                            <%}%>
                        </c:when>
                        <c:otherwise>
                            <%if(!readOnlyFlag.equalsIgnoreCase("on") || bookingFlag.equalsIgnoreCase("new")){%>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                                     <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','collapse','<%=amount%>','<%=adjustment %>','manualCharge','<%=oldAmount%>')" name="<%=i%>"
                                     id="chargesexpand"/></span>
                            <%}else{%>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>${editTooltip}</strong>',null,event);" onmouseout="tooltip.hide();">
                                     <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editcharges('<%=id%>','expand','<%=amount%>','<%=adjustment %>')" name="<%=i%>"
                                     id="chargesexpand"/></span>
                            <%}%>
                        </c:otherwise>
                    </c:choose>
                <%}if(!readOnlyFlag.equalsIgnoreCase("on") && !chargeCode.equalsIgnoreCase(FclBlConstants.ADVANCESURCHARGECODE) &&  !chargeCode.equalsIgnoreCase("INSURE")){ %>
			<span id="chargesSpanId"  class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
  				<img src="<%=path%>/img/icons/delete.gif" border="0" onclick="chargesdelete('<%=id%>')" name="<%=i%>"
  				id="chargesDeleteCollapse"/> </span>
	 	<%}%>
	 </display:column>

  	 <display:column title="" style="display:none;" >
		<%if(prepaid.equals("prepaid")){ %>
			<input type="radio" name="pcollect<%=i%>" value="prepaid"  disabled="disabled" checked="checked"/>Prepaid
		<%}else{ %>
			<input type="radio" name="pcollect<%=i%>"  disabled="disabled" value="prepaid"/>Prepaid
		<%} if(collect.equals("collect")){ %>
			<input type="radio" name="pcollect<%=i%>"  disabled="disabled" value="collect" checked="checked"/>Collect
		<%}else{ %>
			<input type="radio"  name="pcollect<%=i%>"  disabled="disabled" value="collect"/>Collect
		<%}%>
     </display:column>

	<%i++; %>
    </display:table>
   </table></td>
    </tr>
</table>

                <table width="100%" border="0" id="fclblChargesTable" class="fontsize13">
    <tr class="textlabelsBold">
      <td style="color:green;">Agent :</td>
      <td>
           <input type="text" value="<%=alterAgent%>" size="20" readonly="readonly" id="disAgent"
             class="displayWithBackGroundColorWhite"
              <%if(null != alterAgent && !alterAgent.equals("")){%>
                onmouseover="tooltip.show('<strong><%=alterAgent%></strong>',null,event);" onmouseout="tooltip.hide();"
             <%}%>
                 />
         <input type="text" value="<%=agentNo%>" size="10" readonly="readonly" id="disAgentNo"
            class="displayWithBackGroundColorWhite"/></td>
      <td class="displayWithBackGroundColorRED" align="right">Routed By Agent :</td>
		   <%if(routedAgentName.equals("")){%>
                   <td><span class="displayWithBackGroundColorRED">NO</span></td>
		   <%}else{%>
		     <td colspan="2">
		         <input type="text" value="<%=routedAgentName%>" size="20" readonly="readonly" id="disRoutedAgent"
		           class="displayWithBackGroundColorRED"
                            <%if(null != routedAgentName && !routedAgentName.equals("")){%>
                                onmouseover="tooltip.show('<strong><%=routedAgentName%></strong>',null,event);" onmouseout="tooltip.hide();"
                             <%}%>
                           />
		     <span style="padding-left:20px;">
		         <input type="text" value="<%=routedAgentNo%>" size="10" readonly="readonly"  id="disRoutedAgentNo"
		           class="displayWithBackGroundColorRED"/>
		     </span></td>
		   <%}%>
    </tr>
 	<tr class="textlabelsBold">
 		<td style="color:green;">Origin :</td>
 		<td>
 		      <input type="text" value="${param.terminalName}" size="20" readonly="readonly" id="disOrigin"
 		          class="displayWithBackGroundColorWhite"
                          <c:if test="${not empty param.terminalName}">
                            onmouseover="tooltip.show('<strong>${param.terminalName}</strong>',null,event);"onmouseout="tooltip.hide();"
                          </c:if>
                          /></td>
 		<td style="color:green;" align="right">Destination :</td>
 		<td>
 		      <input type="text" value="${param.finalDestination}" size="20" readonly="readonly" id="disDestination"
 		          class="displayWithBackGroundColorWhite"
                          <c:if test="${not empty param.finalDestination}">
                            onmouseover="tooltip.show('<strong>${param.finalDestination}</strong>',null,event);"onmouseout="tooltip.hide();"
                          </c:if>
                          /></td>
 		<td align="left" style="color:green;">Commodity :</td>
 		<td>
 		     <input type="text" value="${param.commodityCode}" size="10" readonly="readonly" id="disCommodityCode"
 		          class="displayWithBackGroundColorWhite"/></td>
    </tr>
 	<tr class="textlabelsBold">
 		<td style="color:green;padding-right:10px;">Carrier :</td>
 		<td>
 		      <input type="text" value="${param.streamShipLineName}//${param.streamShipLineNo}" size="30"
 		         readonly="readonly"  class="displayWithBackGroundColorWhite" id="disCarrier"
                            <c:if test="${not empty param.streamShipLineName}">
                            onmouseover="tooltip.show('<strong>${param.streamShipLineName}//${param.streamShipLineNo}</strong>',null,event);"onmouseout="tooltip.hide();"
                          </c:if>
                         /></td>
 		<td style="color:green;" align="right">IS TM :</td>
		<td>
		      <input type="text" value="<%=issuingTerminal%>" size="20" readonly="readonly" id="disIssuingTerminal"
		         class="displayWithBackGroundColorWhite"
                            <%if(null != issuingTerminal && !issuingTerminal.equals("")){%>
                                onmouseover="tooltip.show('<strong><%=issuingTerminal%></strong>',null,event);" onmouseout="tooltip.hide();"
                             <%}%>
                         /></td>
    </tr>
    <c:set var="costOfGoods" value="Y" />
    <%if(shipperTotal!=0.0){ %>
	   <tr class="textlabelsBold" id="shipperRow" >
				<td  style="color:green;">Bill To(Shipper):</td>
				<td  style="color:#0000FF;" >
				<input type="text" value="${param.accountName}" size="35" readonly="readonly" id="disShipper"
                                 class="displayBlueWithBackGroundColorWhite"
                                    <c:if test="${not empty param.accountName}">
                                        onmouseover="tooltip.show('<strong>${param.accountName}</strong>',null,event);"onmouseout="tooltip.hide();"
                                   </c:if>
                                /></td>
				<td style="color:#0000FF;" align="right">
					<c:out value="${param.shipper}"></c:out></td>
				<td  style="color:red;" align="right" id="shippertotal">
				<c:out value="<%=numberFormat.format(shipperTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                    <c:set var="costOfGoods" value="N" />
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>
           </tr>
	<%}%>
	<%if(forwarderTotal!=0.0){ %>
                                <tr class="textlabelsBold" id="forwarderRow" >
				<td width="10%" style="color:green;">Bill To(Forwarder):</td>
				<td width="20%" style="color:#0000FF;">
                                <input type="text" value="${param.forwardingAgentName}" size="35" readonly="readonly"
                                class="displayBlueWithBackGroundColorWhite" id="disForwarder"
                                <c:if test="${not empty param.forwardingAgentName}">
                                    onmouseover="tooltip.show('<strong>${param.forwardingAgentName}</strong>',null,event);"onmouseout="tooltip.hide();"
                               </c:if>
                                /></td>
				<td style="color:#0000FF;"  align="right">
					<c:out value="${param.forwardingAgent1}"></c:out></td>
				<td style="color:red;" align="right" id="forwardertotal">
					<c:out value="<%=numberFormat.format(forwarderTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                    <c:set var="costOfGoods" value="N" />
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>
                                </tr>
	<%}%>
	<%if(thirdPartyTotal!=0.0){ %>
                                <tr class="textlabelsBold" id="thirdPartyRow" >
				<td width="10%" style="color:green;">Bill To(Third Party):</td>
				<td width="20%" style="color:#0000FF;">
                                <input type="text" value="${param.thirdPartyName}" size="35" readonly="readonly"
                                class="displayBlueWithBackGroundColorWhite" id="disThirdParty"
                                <c:if test="${not empty param.thirdPartyName}">
                                    onmouseover="tooltip.show('<strong>${param.thirdPartyName}</strong>',null,event);"onmouseout="tooltip.hide();"
                                </c:if>
                                /></td>
				<td  style="color:#0000FF;"  align="right">
					<c:out value="${param.billTrdPrty}"></c:out></td>
				<td style="color:red;" align="right">
					<c:out value="<%=numberFormat.format(thirdPartyTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>
                                </tr>
	 <%}%>
	 <%if(agentTotal!=0.0){ %>
                                <tr class="textlabelsBold" id="agentRow" >
				<td width="10%" style="color:green;">Bill To(Agent):</td>
				<td width="20%" style="color:#0000FF;">
                                <input type="text" value="${param.agent}" size="35" readonly="readonly"
                                class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                                <c:if test="${not empty param.agent}">
                                    onmouseover="tooltip.show('<strong>${param.agent}</strong>',null,event);"onmouseout="tooltip.hide();"
                                </c:if>
                                /></td>
				<td style="color:#0000FF;" align="right"><c:out value="${param.agentNo}"></c:out></td>
				<td style="color:red;;" align="right" id="agenttotal">
					<c:out value="<%=numberFormat.format(agentTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                    <c:set var="costOfGoods" value="N" />
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>
                                </tr>
	  <%}%>
          <%if(consigneeTotal!=0.0){ %>
                                <tr class="textlabelsBold" id="consigneeRow" >
				<td width="10%" style="color:green;">Bill To(Consignee):</td>
				<td width="20%" style="color:#0000FF;">
                                <input type="text" value="${param.consigneeName}" size="35" readonly="readonly"
                                class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                                <c:if test="${not empty param.consigneeName}">
                                    onmouseover="tooltip.show('<strong>${param.consigneeName}</strong>',null,event);"onmouseout="tooltip.hide();"
                                </c:if>
                                /></td>
				<td style="color:#0000FF;" align="right"><c:out value="${param.consigneeNo}"></c:out></td>
				<td style="color:red;;" align="right" id="consigneeTotal">
					<c:out value="<%=numberFormat.format(consigneeTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                    <c:set var="costOfGoods" value="N" />
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>

	   </tr>
	  <%}%>
          <%if(notifyTotal!=0.0){ %>
                                <tr class="textlabelsBold" id="notifyPartyRow" >
				<td width="10%" style="color:green;">Bill To(NotifyParty):</td>
				<td width="20%" style="color:#0000FF;">
                                <input type="text" value="${fclBl.notifyPartyName}" size="35" readonly="readonly"
                                class="displayBlueWithBackGroundColorWhite" id="disAgentName"
                                <c:if test="${not empty fclBl.notifyPartyName}">
                                    onmouseover="tooltip.show('<strong>${fclBl.notifyPartyName}</strong>',null,event);"onmouseout="tooltip.hide();"
                                </c:if>
                                /></td>
				<td style="color:#0000FF;" align="right"><c:out value="${fclBl.notifyParty}"></c:out></td>
				<td style="color:red;;" align="right" id="notifyTotal">
					<c:out value="<%=numberFormat.format(notifyTotal)%>"></c:out></td>
                                <c:if test="${costOfGoods == 'Y'}">
                                    <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                                    <c:set var="costOfGoods" value="N" />
                                      <td align="right" style="color:green;;"> Value Of Goods:</td>
                                      <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                                      </c:if>
                                </c:if>

	   </tr>
	  <%}%>
          
	   <tr class="textlabelsBold" id="newRow" style="display:none">
                <td width="10%" style="color:green;" id="partyLabel"></td>
                <td width="20%" style="color:#0000FF;">
                        <input type="text"  size="35" readonly="readonly"
                        class="displayBlueWithBackGroundColorWhite" id="partyName"/></td>
                <td style="color:#0000FF;" id="partyNumber" align="right"></td>
                <td style="color:red;;" id="partyTotalCharges" align="right"></td>
                <c:if test="${! empty fclBl.costOfGoods && fclBl.costOfGoods != '0.0' &&  fclBl.costOfGoods != '0.00'}">
                  <td align="right" style="color:green;;"> Value Of Goods:</td>
                  <td class="displayWithBackGroundColorWhite">${fclBl.costOfGoods}0</td>
                  </c:if>

	   </tr>
	   <tr class="textlabelsBold" >
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			    <td >&nbsp;</td>
			    <td style="color:blue;;" align="right">----------------</td>
		</tr>
		<tr class="textlabelsBold" >
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			    <td style="color:green;" align="right">Total :

                            </td>
			    <td style="color:red;;" align="right" id="totalOfCharge">
                                <c:if test="${! empty invoiceAmount}">
                                    (Invoice : ${invoiceAmount})&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:if>
			    	<c:out value="${currentSumOfCharges}"></c:out>
                            </td>
                                <input type="hidden" value="${currentSumOfCharges}" id="totCostForCharges"/>
                            <td style="color:red;" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <c:if test="${! empty correctedAmount}">
                                    (Corrected)
                                </c:if>
                            </td>
		</tr>
		<tr class="textlabelsBold" >
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			    <td style="color:green;" align="right">Cost Total : </td>
			    <td style="color:red;" align="right" id="displayCostTotal">${totalCost}</td>
                            <c:if test="${! empty latestCorrection && ! empty latestCorrection.accountName}">
                                <td style="color:green;" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    Billing Now:
                                    <c:choose>
                                        <c:when test="${latestCorrection.correctionType.code == 'B'}">
                                            Collect
                                        </c:when>
                                        <c:otherwise>
                                            Prepaid
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
		</tr>
		<tr class="textlabelsBold" >
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			    <td style="color:green;" align="right" >Profit :</td>
			    <td style="color:red;padding-left:11px;" align="right" id="displayProfit">${profit}</td>
                            <c:if test="${! empty latestCorrection && ! empty latestCorrection.accountName}">
                                <td style="color:green;" align="left"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill To : ${latestCorrection.accountName}</td>
                            </c:if>
		</tr>
</table>
<br>
<table  border="0">
    <tr>
        <td>
            <%if(fileNo.indexOf("-")<0){%>
            <c:if test="${hasTransactionType eq false && toggleCostTable eq false}">
                    <input type="button" value="Preview Accruals" class="buttonStyleNew" style="width:100px"
                       id="previewAccruals" onclick="showAccruals()"/>
            </c:if>
                    <%if(closedBy.equals("")){ %>
                        <input type="button" value="Add Accrual"  class="buttonStyleNew" id="costadd" onclick="addNewCosts()"/>
                        <c:if test="${fclBl.readyToPost ne 'M' && toggleCostTable eq false}">
                            <input type="button" value="Post Accrual Before Manifesting"  style="width:170px" class="buttonStyleNew" id="postCost" onclick="postAccrual('${fclBl.getFileNo()}')"/>
                        </c:if>
                   <%if(agent!=null){%>
                   <c:if test="${importFlag eq false}">
                     <input type="button" value="Add FAE Accruals"  class="buttonStyleNew" style="width:120px"
                        id="faeAdd" onclick="FEACalculation()"/>
                  </c:if>
                <%}%>
                <%}%>
            <%}%>
        </td>
         <c:if test="${importFlag eq false}">
            <td align="right">
         <div id="totalCostFor" style="display: block">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span style="color:green;font-size: 13px; font-weight: bold">Total costs for  &nbsp ${sslBlPrepaidCollectName} :</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span style="color:red;font-size: 13px; font-weight: bold">${totalCostForSSL}</span>
         </div>
            </td>
         </c:if>
            <c:if test="${fclBl.readyToPost eq 'M'}">
                <td class="textlabelsBold">Resend Cost To Blue
                    <html:radio property="resendCostToBlue" value="Yes" name="fclBillLaddingform"   styleId="resendCostToBlueYes" />Yes
                    <html:radio property="resendCostToBlue" value="No" name="fclBillLaddingform"   styleId="resendCostToBlueNo" />No
                </td>
            </c:if>
    </tr>
</table>

<c:if test="${toggleCostTable eq false}">
<div style="display:block" id="accrualsTables">
<table  border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" width="100%">
  <tr class="tableHeadingNew" >
  		<td>
          <%
	  		if(userid!=null && hasUserLevelAccess){%>
	  		 <img src="<%=path%>/img/icons/up.gif" border="0" onclick="showCostCodes()" id="costExpandImage"/>
	      <%} %>
	      <span style="padding-left:10px;">FCL Costs/Accruals</span>
	   </td>
  </tr>
  <tr><%-- ******  EXPAND TABLE FOR COSTS******* --%>
      <td colspan="2">
          <table border="0" cellpadding="0" cellspacing="0" >
            <% int j=0; %>
		  <display:table  name="<%=addcostcodeslist%>"
			class="displaytagstyle" pagesize="<%=pageSize%>" id="expandcosttable" style="border:1px;">
		   <display:setProperty name="basic.msg.empty_list">
			<span style="display:none;" class="pagebanner" />
			</display:setProperty>
		   <display:setProperty name="paging.banner.placement"><span style="display:none;"></span></display:setProperty>

	<%
	String costCodeDesc="";
	String amount="0.00";
	String account="";
	String accountName="";
	String accountSubName="";
	String costCode="";
	String datePaid="";
	String comments="";
	String costComments="";
	String codeid="";
	String costcurrency="USD";
	String bookingFlag="";
	String readOnlyFlag="";
	String manifestFlag="";
	String transactionType="";
        String invoiceNo="";
	TransactionBean transactionBean=new TransactionBean();
	transactionBean.setReadyToPostForCost("off");

	if(addcostcodeslist!=null && addcostcodeslist.size()>0){
		FclBlCostCodes fclBlCostCodes=(FclBlCostCodes)addcostcodeslist.get(j);
		if(fclBlCostCodes.getCodeId()!=null ){
			codeid=fclBlCostCodes.getCodeId().toString();
		}else{
		    codeid="";
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostCode()!=null){
			costCode=fclBlCostCodes.getCostCode();
			if(costCode!=null && costCode.trim().equals(FclBlConstants.FAECODE)){
					if(fclBlCostCodes.getAmount()!=null){
					// chnaged from adming cost to FAE  now adminCostCode=FAE Cost
						adminCostCode=fclBlCostCodes.getAmount().toString();
					}
		    }

			if(costCode!=null && costCode.trim().equals(FclBlConstants.FAECODE)){
			   fAEChargeCode=costCode;
			}
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostCodeDesc()!=null){
			costCodeDesc=fclBlCostCodes.getCostCodeDesc();
			 if(costCodeDesc!=null && costCodeDesc.equalsIgnoreCase(FclBlConstants.FFCODEDESC)){
					fFChargeCode=costCodeDesc;
			}
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getAmount()!=null){
			amount=numberFormat.format(fclBlCostCodes.getAmount());
			//totCost=totCost+fclBlCostCodes.getAmount();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getAccNo()!=null){
			account=fclBlCostCodes.getAccNo();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getAccName()!=null){
			accountName=fclBlCostCodes.getAccName();
					accountSubName=(accountName.length()>5)?accountName.substring(0,5):accountName;
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getDatePaid()!=null){
			datePaid=sdf.format(fclBlCostCodes.getDatePaid());
		}
                if(fclBlCostCodes!=null && fclBlCostCodes.getInvoiceNumber()!=null){
						invoiceNo=fclBlCostCodes.getInvoiceNumber();
              	}
		if(fclBlCostCodes!=null && fclBlCostCodes.getComments()!=null){
			comments=fclBlCostCodes.getComments();
			StringBuilder s=new StringBuilder();
			if(comments!=null){
				int index=0;
				char[] commentArray = comments.toCharArray();
				for (int k = 0; k < commentArray.length; k++) {
					if(commentArray[k]=='\n'){
						s.append(comments.substring(index, k).trim());
						s.append(" ");
						index = k+1;
					}
				}
				s.append(comments.substring(index, comments.length()).trim());

			}
			s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
			s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		    comments=s.toString();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostComments()!=null){
		   	costComments=fclBlCostCodes.getCostComments();
		   	StringBuilder s=new StringBuilder();
			if(costComments!=null){
				int index=0;
				char[] commentArray = costComments.toCharArray();
				for (int k = 0; k < commentArray.length; k++) {
					if(commentArray[k]=='\n'){
						s.append(costComments.substring(index, k).trim());
						s.append(" ");
						index = k+1;
					}
				}
				s.append(costComments.substring(index, costComments.length()).trim());

			}
			s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
			s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		    costComments=s.toString();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCurrencyCode()!=null){
		   costcurrency=fclBlCostCodes.getCurrencyCode();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getReadyToPost()!=null){
			transactionBean.setReadyToPost(fclBlCostCodes.getReadyToPost());
			manifestFlag=fclBlCostCodes.getReadyToPost();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getReadyToPost()!=null &&
				(fclBlCostCodes.getReadyToPost().equals("M") || fclBlCostCodes.getReadyToPost().equals("on"))){
			transactionBean.setReadyToPostForCost("on");
		}
		if(null!=fclBlCostCodes && null!=fclBlCostCodes.getReadOnlyFlag()){
			readOnlyFlag=fclBlCostCodes.getReadOnlyFlag();
		}
                if(null!=fclBlCostCodes && null!=fclBlCostCodes.getTransactionType()){
			transactionType=fclBlCostCodes.getTransactionType();
		}
		if(null!=fclBlCostCodes && null!=fclBlCostCodes.getBookingFlag()){
		   bookingFlag=fclBlCostCodes.getBookingFlag();
		}else{
			bookingFlag="";
		}
		//----PROFIT calculation :-   profit=Total Charge - Total Cost------
		//profit=totCostForCharges-totCost;
	}
	request.setAttribute("transactionBean",transactionBean);
%>
<%--<%if(costReadyToPost.equals("M")|| readOnlyFlag.equalsIgnoreCase("on")){ %>--%>
	<%if(bookingFlag.equalsIgnoreCase("new")){ %>
		<display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
	<%}else if(readOnlyFlag.equalsIgnoreCase("on")){ %>
		<display:column style="visibility:hidden;width:8px;"></display:column>
	<%}else{%>
		<display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
	<%}%>
      	<display:column title="Cost Code" style="width:150px">
                <input type="text" class="BackgrndColorForTextBox" name="costCode<%=j%>"
                        id="costCode<%=j%>" value="<%=costCode%>" onkeydown="getCostCode(this.value,<%=j%>)" size="4" readonly="readonly"/>
        </display:column>
        <display:column title="" >
                <input size="2" class="BackgrndColorForTextBox" name="<%="accrualsExpand"+codeid%>" 
                       readonly="true" value="<%=transactionType%>" />
        </display:column>
      	<display:column title="Cost Code Desc" style="width:350px">
	 		<input type="text" class="BackgrndColorForTextBox" name="costCodeDesc<%=j%>" readonly="true"
	 			id="costCodeDesc<%=j%>" value="<%=costCodeDesc%>"
			    onkeydown="getCostCodeDesc(this.value,<%=j%>)"  size="15"/>

	   </display:column>
       <display:column title="Vendor Name" style="width:130px">
          <span onmouseover="tooltip.show('<strong><%=accountName%></strong>',null,event);"
				onmouseout="tooltip.hide();">
      		<input type="text" class="BackgrndColorForTextBox" name="accountName<%=j%>" id="accountname<%=j%>"
     			 onkeydown="getVesselName(this.value)"  value="<%=accountSubName%>"
     			size="5" readonly="true"/>
          </span>
       </display:column>
       <display:column title="Vendor Account" style="width:100px">
     		<input type="text" class="BackgrndColorForTextBox" name="accountNo<%=j%>" id="accountno<%=j%>"
     		   value="<%=account%>"  onkeydown="getVesselName(this.value)" size="10" readonly="true"/>
      </display:column>
          <display:column title="Invoice Number" style="width:100px">
     		   <input type="text" class="BackgrndColorForTextBox" name="invoiceNumber<%=j%>"
     		        value="<%=invoiceNo%>"  readonly="true" tabindex="-1" />
          </display:column>
	  <display:column title="Amount" style="width:100px">
	       <html:text styleClass="BackgrndColorForTextBox" property="costAmount"  value="<%=amount%>" size="4"
	           readonly="true" ></html:text>
	  </display:column>
	  <display:column title="Currency" style="width:100px">
	       <html:text styleClass="BackgrndColorForTextBox" property="costCurrency" value="<%=costcurrency%>"
	          readonly="true" size="5" >
            </html:text>
      </display:column>
	  <display:column title="Date Paid" style="width:100px">
	       <input type="text" class="BackgrndColorForTextBox"  name="datePaid"  readonly="true"
	          id="txtItemcreatedon1<%=j%>" size="10"  value="<%=datePaid%>" />
	  </display:column>
	  <display:column title="Cheque No" style="width:100px">
	       <html:text styleClass="BackgrndColorForTextBox" property="chequeNumber" value="" disabled="true"
	           size="5"></html:text>
	  </display:column>
	 <display:column style="visibility:hidden;">
		    <input type="hidden" name="codeid<%=j%>" value="<%=codeid%>">
	 </display:column>
     <%j++; %>
    </display:table>
   </table></td>
  </tr>

  <tr><%-- ******  COLLAPSE TABLE FOR COSTS******* --%>
      <td colspan="2">
          <table border="0" cellpadding="0" cellspacing="0"  >
            <% j=0; %>
                       <%
                          Map hashPrepaid = new HashMap();
                          Map hashMapColl = new HashMap();
                       %>
		  <display:table  name="<%=collapseCostList%>" class="displaytagstyle" pagesize="<%=pageSize%>"
		       id="collapseCosttable">
<display:setProperty name="basic.msg.empty_list">
			<span style="display:none;" class="pagebanner" />
			</display:setProperty>
		<display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
	<%
	String costCodeDesc="";
	String amount="0.00";
	String account="";
	String accountName="";
	String accountSubName="";
	String costCode="";
	String datePaid="";
	String comments="";
	String costComments="";
	String codeid="";
	String costcurrency="USD";
	String bookingFlag="";
	String readOnlyFlag="";
	String manifestFlag="";
        String invoiceNo="";
        String transactionType="";
        String readyTopost="";
	TransactionBean transactionBean=new TransactionBean();
	transactionBean.setReadyToPostForCost("off");
      
	if(collapseCostList!=null && collapseCostList.size()>0){
		FclBlCostCodes fclBlCostCodes=(FclBlCostCodes)collapseCostList.get(j);
		if(fclBlCostCodes.getCodeId()!=null ){
			codeid=fclBlCostCodes.getCodeId().toString();
		}else{
		    codeid="";
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostCode()!=null){
			costCode=fclBlCostCodes.getCostCode();                
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostCodeDesc()!=null){
			costCodeDesc=fclBlCostCodes.getCostCodeDesc();              
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getAmount()!=null){
			amount=numberFormat.format(fclBlCostCodes.getAmount());
			totalCostOfCollapse=totalCostOfCollapse+fclBlCostCodes.getAmount();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getAccNo()!=null){
			account=fclBlCostCodes.getAccNo();
		}
                if(fclBlCostCodes!=null && fclBlCostCodes.getReadyToPost()!=null){
                    readyTopost=fclBlCostCodes.getReadyToPost();
                }
		if(fclBlCostCodes!=null && fclBlCostCodes.getAccName()!=null){
			accountName=fclBlCostCodes.getAccName();
			accountSubName=(accountName.length()>5)?accountName.substring(0,5):accountName;
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getDatePaid()!=null){
			datePaid=sdf.format(fclBlCostCodes.getDatePaid());
		}
                if(fclBlCostCodes!=null && fclBlCostCodes.getInvoiceNumber()!=null){
			invoiceNo=fclBlCostCodes.getInvoiceNumber();
              	}
		if(fclBlCostCodes!=null && fclBlCostCodes.getComments()!=null){
			comments=fclBlCostCodes.getComments();
			StringBuilder s=new StringBuilder();
			if(comments!=null){
				int index=0;
				char[] commentArray = comments.toCharArray();
				for (int k = 0; k < commentArray.length; k++) {
					if(commentArray[k]=='\n'){
						s.append(comments.substring(index, k).trim());
						s.append(" ");
						index = k+1;
					}
				}
				s.append(comments.substring(index, comments.length()).trim());
			}
			s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
			s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		    comments=s.toString();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCostComments()!=null){
		   	costComments=fclBlCostCodes.getCostComments();
		   	StringBuilder s=new StringBuilder();
			if(costComments!=null){
				int index=0;
				char[] commentArray = costComments.toCharArray();
				for (int k = 0; k < commentArray.length; k++) {
					if(commentArray[k]=='\n'){
						s.append(costComments.substring(index, k).trim());
						s.append(" ");
						index = k+1;
					}
				}
				s.append(costComments.substring(index, costComments.length()).trim());
			}
			s = new StringBuilder(s.toString().replaceAll("'", "\\\\'"));
			s=new StringBuilder(s.toString().replaceAll("\"", "&quot;"));
		    costComments=s.toString();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getCurrencyCode()!=null){
		   costcurrency=fclBlCostCodes.getCurrencyCode();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getReadyToPost()!=null){
			transactionBean.setReadyToPost(fclBlCostCodes.getReadyToPost());
			manifestFlag=fclBlCostCodes.getReadyToPost();
		}
		if(fclBlCostCodes!=null && fclBlCostCodes.getReadyToPost()!=null &&
				(fclBlCostCodes.getReadyToPost().equals("M") || fclBlCostCodes.getReadyToPost().equals("on"))){
			transactionBean.setReadyToPostForCost("on");
		}
		if(null!=fclBlCostCodes && null!=fclBlCostCodes.getReadOnlyFlag()){
			readOnlyFlag=fclBlCostCodes.getReadOnlyFlag();
		}
		if(null!=fclBlCostCodes && null!=fclBlCostCodes.getTransactionType()){
			transactionType=fclBlCostCodes.getTransactionType();
		}
		if(null!=fclBlCostCodes && null!=fclBlCostCodes.getBookingFlag()){
		   bookingFlag=fclBlCostCodes.getBookingFlag();
		}else{
			bookingFlag="";
		}
	}
	request.setAttribute("transactionBean",transactionBean);
%>
<%--<%if(costReadyToPost.equals("M")|| readOnlyFlag.equalsIgnoreCase("on")){ %>--%>
	<%if(bookingFlag.equalsIgnoreCase("new")){ %>
		<display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</display:column>
	<%}else if(readOnlyFlag.equalsIgnoreCase("on")){ %>
		<display:column style="visibility:hidden;width:8px;"></display:column>
	<%}else{%>
		<display:column style="color:#D200D2;font-weight:bold;font-size:14pt;width:10px;">**</display:column>
	<%}%>
      	<display:column title="Cost Code" style="width:100px">
	 		<input type="text" class="BackgrndColorForTextBox" name="costCode<%=j%>"
	 			id="costCode<%=j%>" value="<%=costCode%>" onkeydown="getCostCode(this.value,<%=j%>)" size="4" readonly="readonly" tabindex="-1" />
      		<dojo:autoComplete formId="fclBillLaddingform" textboxId="costCode<%=j%>"
		   		action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=3&index=<%=j%>"/>
		</display:column>
	
		<display:column title="">
                    <input size="2" class="BackgrndColorForTextBox"  name="<%="accrualsCollaps"+codeid%>" 
                           readonly="true" value="<%=transactionType%>"/>
		</display:column>
      	<display:column title="Cost Code Desc" style="width:350px">
	 		<input type="text" class="BackgrndColorForTextBox" name="costCodeDesc<%=j%>" readonly="true"
	 			id="costCodeDesc<%=j%>" value="<%=costCodeDesc%>"
			    onkeydown="getCostCodeDesc(this.value,<%=j%>)"  size="15" tabindex="-1" />
      		<dojo:autoComplete formId="fclBillLaddingform" textboxId="costCodeDesc<%=j%>"
		   		action="<%=path%>/actions/getChargeCode.jsp?tabName=FCL_BL_CHARGES&from=4&index=<%=j%>" />
	   </display:column>
       <display:column title="Vendor Name" style="width:130px">
          <span onmouseover="tooltip.show('<strong><%=accountName%></strong>',null,event);"
				onmouseout="tooltip.hide();">
      		<input type="text" class="BackgrndColorForTextBox" name="accountName<%=j%>" id="accountname<%=j%>"
     			 onkeydown="getVesselName(this.value)"
     			 size="30"  value="<%=accountName%>" readonly="true" tabindex="-1" />
      		<dojo:autoComplete formId="fclBillLaddingform"  textboxId="accountname<%=j%>"
		   		action="<%=path%>/actions/getCustomerName.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=j%>"/>
		  </span>
       </display:column>
       <display:column title="Vendor Account" style="width:130px">
     		<input type="text" class="BackgrndColorForTextBox" name="accountNo<%=j%>" id="accountno<%=j%>"
     		   value="<%=account%>"  onkeydown="getVesselName(this.value)" size="10" readonly="true" tabindex="-1" />
      		<dojo:autoComplete formId="fclBillLaddingform" textboxId="accountno<%=j%>"
		       action="<%=path%>/actions/getCustomer.jsp?tabName=FCL_BL_CHARGES&from=0&index=<%=j%>"/>
      </display:column>
      <display:column title="Invoice Number" style="width:100px">
     		   <input type="text" class="BackgrndColorForTextBox" name="invoiceNumber<%=j%>"
     		        value="<%=invoiceNo%>"  readonly="true" tabindex="-1" />
      </display:column>
	  <display:column title="Amount" style="width:100px">
	       <html:text styleClass="BackgrndColorForTextBox" property="costAmount"  value="<%=amount%>" size="4"
	           readonly="true" tabindex="-1"></html:text>
	  </display:column>
	  <display:column title="Currency" style="width:100px">
	       <html:text styleClass="BackgrndColorForTextBox" property="costCurrency" value="<%=costcurrency%>"
	          readonly="true" size="5" tabindex="-1">
            </html:text>
      </display:column>
	  <display:column title="Date Paid" style="width:100px">
	       <input type="text" class="BackgrndColorForTextBox"  name="datePaid"  readonly="true"
	          id="txtItemcreatedon1<%=j%>" size="10"  value="<%=datePaid%>" tabindex="-1" />
	  </display:column>
	  <display:column title="Cheque No" style="width:120px" >
	       <html:text styleClass="BackgrndColorForTextBox" property="chequeNumber" value="" disabled="true"
	           size="5" tabindex="-1" ></html:text>
	  </display:column>
	  <display:column title="Actions" style="width:100px">
	  <%if(!costCode.equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
	  	    						&&  !costCode.equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE) && !"AP".equals(transactionType)  
                                                      &&  !"IP".equals(transactionType) &&  !"PN".equals(transactionType)){ %>
	       <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();">
  				<img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editCosts('<%=codeid%>','<%=amount%>')"
  				   id="<%="editCollaps"+codeid%>" name="<%=j%>" /></span>

	  	 <%}if(null != costComments && !costComments.trim().equals("")){ %>
	        <span class="hotspot" onmouseover="tooltip.showComments('<strong><%=costComments%></strong>',100,event);"
					    onmouseout="tooltip.hideComments();" style="color:black;">
				<img id="viewgif4"  src="<%=path%>/img/icons/view.gif"/></span>

		<%}if(!readOnlyFlag.equalsIgnoreCase("on") &&(!costCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCEFFCODE)
	  	    						||  !costCode.trim().equalsIgnoreCase(FclBlConstants.ADVANCESHIPPERCODE))  && !"AP".equals(transactionType) 
                                                                &&  !"IP".equals(transactionType) &&  !"DS".equals(transactionType) &&  !"PN".equals(transactionType)){
	  	    						%>
           <span  class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
                <img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCostDetails('<%=codeid%>')"
                  id="<%="deleteCollaps"+codeid%>"  name="<%=j%>" /></span>
	     <%}else{%>
            <%if(bookingFlag.equalsIgnoreCase("new")){%>
               <span  class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
                <img src="<%=path%>/img/icons/delete.gif" border="0" onclick="deleteCostDetails('<%=codeid%>')"
                  id="<%="deleteCollaps"+codeid%>"  name="<%=j%>" /></span>
                <%}%>            <%}%>
                <%if(readyTopost.equals("M") && reSendAccruals){%>
                               <span class="hotspot" onmouseover="tooltip.show('<strong>Resend</strong>',null,event);" onmouseout="tooltip.hide();">
                                <img src="<%=path%>/img/icons/refresh.gif" border="0" onclick="resendcost('<%=codeid%>')"
                                    id="<%="resendManifestCollaps"+codeid%>"  name="<%=j%>" /></span>
                    <%}%>
	 </display:column>
	 <display:column style="visibility:hidden;">
		    <input type="hidden" name="codeid<%=j%>" value="<%=codeid%>">
	 </display:column>
     <%j++; %>
    </display:table>

   </table></td>
  </tr>
</table>
</div>
</c:if>
       <c:if test="${importFlag eq false}">
           <div id="totalCostForDown" style="float: left;padding-left: 500px;display: none">
            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span style="color:green; font-size: 13px; font-weight: bold">Total costs for  &nbsp ${sslBlPrepaidCollectName} :</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <span style="color:red; font-size: 13px; font-weight: bold">${totalCostForSSL}</span>
            </td>
         </div>
         </c:if>
		<html:hidden property="index"/>
		<html:hidden property="index1"/>
		<html:hidden property="readyToPostCheck"/>
		<html:hidden property="readyToPostCostCheck"/>
		<html:hidden property="costCodeId"/>
		<html:hidden property="chargeId"/>
		<html:hidden property="billToParty" value=""/>
		<%--adminCost,differenceOfCostAndCharges added by 	--%>
		<html:hidden property="adminCost" value="<%=adminCostCode%>"/>
		<html:hidden property="differenceOfCostAndCharges" />
		<html:hidden property="tempFFCostCOde" value="<%=fFChargeCode%>"/>
                <html:hidden property="tempFAECostCOde" value="<%=fAEChargeCode%>"/>
		<input type="hidden" name="totalOfCost" id="totalOfCost" value="<%=numberFormat.format(totCost)%>">
		<input type="hidden" name="profitGained" id="profitGained" value="<%=numberFormat.format(profit)%>">
</body>
<script language="javascript">
    if(toggleCostTable=='false'){
        togglePreviewAccruals();
    }
</script>
</html>


<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.bc.notes.NotesConstants" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String fileNo="";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%@include file="../includes/resources.jsp" %>
    <%@include file="../includes/baseResources.jsp" %>
    
    <title>My JSP 'DisputedFclBl.jsp' starting page</title>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
        <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 0;
                right: 0;
                top: 10%;
            }
        </style>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/ProcessInfoBC.js'></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type='text/javascript' src='${path}/dwr/interface/QuoteDwrBC.js'></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/fcl/fclBl.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
  </head>
  
  <body class="whitebackgrnd">
      <!--DESIGN FOR CONFIRM BOX ---->
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
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()"  style="float: right">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()" >
            </form>
        </div>
        <!--END  CONFIRM BOX ---->
        <div id="commentDiv"   class="comments">
			<table border="1" id="commentTableInfo">
                            <tbody border="0"></tbody>
			</table>
        </div>
        <div id="mask"></div>
        <div id="progressBar" class="progressBar" style="position: absolute;left:40% ;top: 40%;right: 40%;bottom: 60%;display: none;">
            <p class="progressBarHeader"><b style="width: 100%;padding-left: 40px;">Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif"/>
            </div>
        </div>
    <html:form action="/fclBL?accessMode=${param.accessMode}"  name="fclBLForm"
		       type="com.gp.cvst.logisoft.struts.form.FclBLForm" scope="request">
		<html:hidden property="buttonValue"/>
                <html:hidden property="disputedFileNo"/>
        <div id="cover"></div>
        
         <table cellpadding="1" cellspacing="0" class="tableBorderNew" width="100%">
             <tr class="tableHeadingNew">Search Criteria</tr>
             <tr class="textlabelsBold">
                <td>File No</td>
                <td><input type="text" name="fileNo" value="${fclBlForm.fileNo}"
                      class="textlabelsBoldForTextBox"/></td>
                <td>Origin</td>
                <td><input type="text" name="terminal" id="terminal" value="${fclBlForm.terminal}"
                      class="textlabelsBoldForTextBox"/>
                    <input name="terminal_check" id="terminal_check_id"  type="hidden">
                    <div id="terminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                    <script type="text/javascript">
                         initAutocompleteWithFormClear("terminal","terminal_choices","","terminal_check",
                          "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=1&isDojo=false","");
                    </script>
                </td>
                <td>Destination</td>
                <td><input type="text" name="finalDestination" id="finalDestination" value="${fclBlForm.finalDestination}"
                      class="textlabelsBoldForTextBox"/>
                    <input name="final_check" id="final_check_id"  type="hidden">
                    <div id="final_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                    <script type="text/javascript">
                         initAutocompleteWithFormClear("finalDestination","final_choices","","final_check",
                          "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=2&isDojo=false","");
                    </script> 
                </td>
                <td>POL</td>
                <td><input type="text" name="portofladding" id="portofladding" value="${fclBlForm.portofladding}"
                      class="textlabelsBoldForTextBox"/>
                    <input name="pol_check" id="pol_check_id"  type="hidden">
                    <div id="pol_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                    <script type="text/javascript">
                         initAutocompleteWithFormClear("portofladding","pol_choices","","pol_check",
                          "${path}/actions/getUnlocationCode.jsp?tabName=FCL_BL&from=2&isDojo=false","");
                    </script> 
                </td>
            </tr>
            <tr class="textlabelsBold">
                <td>POD</td>
                <td><input type="text" name="portofdischarge" id="portofdischarge" value="${fclBlForm.portofdischarge}"
                          class="textlabelsBoldForTextBox"/>
                    <input name="pod_check" id="pod_check_id"  type="hidden">
                    <div id="pod_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                    <script type="text/javascript">
                         initAutocompleteWithFormClear("portofdischarge","pod_choices","","pod_check",
                          "${path}/actions/getUnlocationCodeDesc.jsp?tabName=FCL_BL&from=3&isDojo=false","");
                    </script>     
                </td>
                <td>SSL</td>
                <td>
                <input type="text" Class="textlabelsBoldForTextBox" name="sslineName" id="sslineName"  size="22" value="${fclBlForm.sslineName}"/>
                <input id="sslname_check" type="hidden" value="${fclBlForm.sslineName}" />
                <div id="sslname_choices" style="display: none" class="autocomplete"></div>
                <script type="text/javascript">
                    initAutocompleteWithFormClear("sslineName","sslname_choices","","sslname_check",
                        "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=7","focusSettingForSSl();","");
                </script>
                </td>
                <td>ETA</td>
               <td><html:text property="eta" onchange="validateDate(this);" value="${fclBlForm.eta}"  
                           styleId="txtetaCalImg" size="16" styleClass="textlabelsBoldForTextBox"/>
                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="etaCalImg"
                       onmousedown="insertDateFromCalendar(this.id,0);" />
                </td>
                <td>ETD</td>
                <td><html:text property="etd" onchange="validateDate(this);" value="${fclBlForm.etd}"  
                           styleId="txtetdCalImg" size="16" styleClass="textlabelsBoldForTextBox"/>
                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="etdCalImg"
                       onmousedown="insertDateFromCalendar(this.id,0);" />
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
               <td align="center" colspan="8">
                   <c:if test="${param.accessMode == '1'}">
                        <input type="button" value="Search" class="buttonStyleNew" onclick="searchDisputed()"/></td>
                   </c:if>
            </tr>    
         </table>
         
         <br>
         <c:if test="${!empty disputedList}">
            <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">List Of Disputed BL</tr>
                <tr>
                    <td colspan="2">
                        <div id="disputedBlDiv">
                            <display:table name="${disputedList}" pagesize="100" id="disputedBlTable" sort="list"
                                               class="displaytagStyleNew" style="width:100%" requestURI="/fclBL.do">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font>
                                            Disputed BL displayed,For more Records click on page numbers.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">One {0} displayed. Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">{0} {1} displayed, Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner">No Records Found.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom"/>
                                    <display:setProperty name="paging.banner.item_name" value="Disputed BL"/>
                                    <display:setProperty name="paging.banner.items_name" value="Disputed BL"/>
                                    <display:column title="File No">
                                        <span style="color:blue;cursor: pointer;text-decoration: underline" onclick="checkLockAndNavigateToBl('${disputedBlTable.fileNo}','blId=${disputedBlTable.bol}&bookingId=${disputedBlTable.bookingNo}&quoteId=${disputedBlTable.quuoteNo}&fileNumber=${disputedBlTable.fileNo}','FCLBL')">
                                            ${disputedBlTable.fileNo}
                                        </span>
                                    </display:column>
                                    <display:column title="Origin">
                                        <span class="hotspot" alt="${disputedBlTable.terminal}" title="${disputedBlTable.terminal}"
                                                 style="color:black;">${disputedBlTable.originCode}
                                        </span>
                                    </display:column>
                                    <display:column title="Destination">
                                        <span class="hotspot" alt="${disputedBlTable.finalDestination}" title="${disputedBlTable.finalDestination}"
                                                 style="color:black;">${disputedBlTable.destinationCode}
                                        </span>
                                    </display:column>
                                    <display:column title="Pol">
                                        <span class="hotspot" alt="${disputedBlTable.portOfLoading}" title="${disputedBlTable.portOfLoading}" 
                                              style="color:black;">${disputedBlTable.polCode}</span>
                                    </display:column>
                                    <display:column title="Pod">
                                        <span class="hotspot" alt="${disputedBlTable.portofDischarge}" title="${disputedBlTable.portofDischarge}"
                                                style="color:black;">${disputedBlTable.podCode}
                                        </span>
                                    </display:column>
                                    <display:column title="SSL" sortable="true">
                                        <span class="hotspot" alt="${disputedBlTable.sslineName}" title="${disputedBlTable.sslineName}"
                                                 style="color:black;">${disputedBlTable.sslineName}
                                        </span>
                                    </display:column>
                                    <display:column title="SSL BL">
                                        ${fn:substring(disputedBlTable.houseBl,0,1)}
                                    </display:column>
                                    <display:column title="Status">
                                        ${disputedBlTable.status}
                                    </display:column> 
                                    <display:column title="ETD">
                                        <fmt:formatDate value="${disputedBlTable.sailDate}" pattern="dd-MMM-yyyy"/>
                                    </display:column>
                                    <display:column title="ETA">
                                        <fmt:formatDate value="${disputedBlTable.eta}" pattern="dd-MMM-yyyy"/>
                                    </display:column>
                                    <display:column title="Ack" >
                                        <c:choose>
                                            <c:when test="${!empty disputedBlTable.ackComments}">
                                                <img id="viewgif" src="${path}/img/icons/darkGreenDot.gif" alt="${disputedBlTable.ackComments}" title="${disputedBlTable.ackComments}"/>
                                            </c:when>
                                            <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${roleDuty.takeOwnershipOfDisputedBL}">
                                                            <img id="viewgif" src="${path}/img/icons/reddot1.gif" onclick="acknowledge('${disputedBlTable.fileNo}')"
                                                                 alt="Click here to take ownership" title="Click here to take ownership"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img id="viewgif" src="${path}/img/icons/reddot1.gif" style="cursor: auto"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                       
                                    </display:column>

                                    <display:column title="Action">   
                                        <span class="hotspot" alt="Notes" title="Notes">
                                            <img src="${path}/img/icons/e_contents_view.gif" border="0"
                                             onclick="return GB_show('Notes','${path}/notes.do?moduleId='+'<%=NotesConstants.FILE%>&itemName=100017&moduleRefId='+'${disputedBlTable.fileNo}&acknowledge='+'${acknowledge}',380,780);" />
                                        </span>
                                        <span class="hotspot" alt="View" title="View">
                                            <img src="${path}/img/icons/preview.gif" border="0" onclick="getAttachmentList('${disputedBlTable.fileNo}')"/>
                                        </span>
                                    </display:column>
                                </display:table>
                        </div>
                    </td>
                </tr>
         </table>
            
       </c:if>
    </html:form>
  </body>
  <script language="javascript">
      function getAttachmentList(fileNo){
        DwrUtil.getSsMasterAtachmentList(fileNo,function(data){
            showPopUp();
            var docListDiv = createHTMLElement("div","docListDiv","100%","70%",document.body);
            dwr.util.setValue("docListDiv", data, { escapeHtml:false });
        });
      }
      function viewFile(fileName) {
        if(fileName.indexOf(".xls")>0 || fileName.indexOf(".doc")>0
            || fileName.indexOf(".mht")>0 || fileName.indexOf(".msg")>0
            || fileName.indexOf(".csv")>0 || fileName.indexOf(".ppt")>0){
            window.open ("${path}/servlet/FileViewerServlet?fileName="+fileName, "","resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
        }else{
            window.parent.showGreyBox("","${path}/servlet/FileViewerServlet?fileName="+fileName);
        }
      }
      function closeDocList() {
        document.body.removeChild(document.getElementById("docListDiv"));
        closePopUp();
      }
      function validateDate(data) {
          if(data.value!=""){
              data.value = data.value.getValidDateTime("/","",false);
              if(data.value==""||data.value.length>10){
                  alertNew("Please enter valid date");
                  data.value="";
                  document.getElementById(data.id).focus();
              }
          }
      }
      function searchDisputed(){
          if(document.fclBLForm.eta.value=="" &&document.fclBLForm.etd.value!=""){
              alertNew("Please Enter ETA");
              return;
          }else if(document.fclBLForm.eta.value!="" &&document.fclBLForm.etd.value==""){
              alertNew("Please Enter ETD");
              return;
          }else{
          document.fclBLForm.buttonValue.value="searchDisputedBl";
          document.fclBLForm.submit();
          }
      }
      function acknowledge(fileNo,ack){
          document.fclBLForm.disputedFileNo.value = fileNo;
	confirmYesOrNo("Are you sure you want to take ownership of this disputed file? Y/N","ack");
      }
      function confirmMessageFunction(id1,id2){
	if(id1=='ack' && id2=='yes'){
            document.fclBLForm.buttonValue.value="ackDisputedBl";
            document.fclBLForm.submit();
        }else if(id1=='ack' && id2=='no'){
            return false;
        }
      }
       function checkLockAndNavigateToBl(fileNumber,path,moduleId){
        var userId='${userId}';
        var itemId='${itemId}';
        var selectedMenu='${selectedMenu}';
        ProcessInfoBC.cheackFileINDB(fileNumber,userId,{callback:function(data){
                if(data!=null && data!=""){
                    if(data == 'sameUser'){
                        alert("File "+fileNumber+ " is already opened in another window");
                        return;
                    }else{
                        alert(fileNumber+" This record is being used by "+data);
                    }
                }else{
                    window.parent.changeChildsFromDisputedBl(path,fileNumber,moduleId,itemId,selectedMenu);
                }
            },async:false});
    }
  </script>
</html>

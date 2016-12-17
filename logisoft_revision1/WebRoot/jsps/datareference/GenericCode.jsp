
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.hibernate.dao.CodetypeDAO,com.gp.cong.logisoft.domain.Codetype,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
            String path = request.getContextPath();
             String codeType="";
            if(null != request.getParameter("codeType") && !request.getParameter("codeType").equals("")){
                codeType = request.getParameter("codeType");
            }else{
                codeType = (String)request.getAttribute("code");
            }

            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            DBUtil dbUtil = new DBUtil();
            request.setAttribute("codeTypeList", dbUtil.getCodesList());
            String message = "";
            if (request.getAttribute("message") != null) {
                message = (String) request.getAttribute("message");
            }
            String genericcode = "";
            if (request.getAttribute("genericcode") != null) {
                genericcode = (String) request.getAttribute("genericcode");
            }
            CodetypeDAO cdao = new CodetypeDAO();
            Codetype cd = new Codetype();
            cd.setDescription("Airport Codes");
            List apcodes = cdao.findByExample(cd);
            int apcode = (CommonFunctions.isNotNullOrNotEmpty(apcodes)) ? ((Codetype) apcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Charge Codes");
            List ccodes = cdao.findByExample(cd);
            int ccode = (CommonFunctions.isNotNullOrNotEmpty(ccodes)) ? ((Codetype) ccodes.get(0)).getCodetypeid() : 0;
            String code = (String) session.getAttribute("code");
            cd.setDescription("Commodity Codes");
            List cpcodes = cdao.findByExample(cd);
            int cpcode = (CommonFunctions.isNotNullOrNotEmpty(cpcodes)) ? ((Codetype) cpcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Correction Notice Codes");
            List cncodes = cdao.findByExample(cd);
            int cncode = (CommonFunctions.isNotNullOrNotEmpty(cncodes)) ? ((Codetype) cncodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("FCL Release Codes");
            List fclcodes = cdao.findByExample(cd);
            int fclcode = (CommonFunctions.isNotNullOrNotEmpty(fclcodes)) ? ((Codetype) fclcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Unit Types");
            List unitcodes = cdao.findByExample(cd);
            int unitcode = (CommonFunctions.isNotNullOrNotEmpty(unitcodes)) ? ((Codetype) unitcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Vessel Codes");
            List vcodes = cdao.findByExample(cd);
            int vcode = (CommonFunctions.isNotNullOrNotEmpty(vcodes)) ? ((Codetype) vcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Loading Instructions Master");
            List lmcodes = cdao.findByExample(cd);
            int lmcode = (CommonFunctions.isNotNullOrNotEmpty(lmcodes)) ? ((Codetype) lmcodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Print Comments");
            List prinCodes = cdao.findByExample(cd);
            int printCode = (CommonFunctions.isNotNullOrNotEmpty(prinCodes)) ? ((Codetype) prinCodes.get(0)).getCodetypeid() : 0;
            cd.setDescription("Pre-defined Remarks");
            List preDefRemarksList = cdao.findByExample(cd);
            int preDefRemarks = (CommonFunctions.isNotNullOrNotEmpty(preDefRemarksList)) ? ((Codetype) preDefRemarksList.get(0)).getCodetypeid() : 0;
            
            if (code == null) {
                code = "0";
            }
            if (genericcode.equals("addgenericcode")) {
                session.setAttribute("getAddPage", "addPage");
            %>
            <script>
                parent.parent.GB_hide();
                parent.parent.getGenericPage('<%=codeType%>');
            </script>
            <%}%>

<html> 
    <head>
        <title>JSP for WarehouseCodeForm form</title>
        <%@include file="../includes/baseResources.jsp" %>

<script language="javascript" type="text/javascript">
function alphanumeric(alphane){
var numeric = alphane;
var flag=0;
    for(var j=0; j<numeric.length; j++){
        var alpha = numeric.charAt(j);
        var hh = alpha.charCodeAt(0);
        if((hh > 47 && hh<59) || (hh > 64 && hh<91) || (hh > 96 && hh<123)){

        }else{
            return false;
        }
    }
    return true;
}
function searchform(){
    if(document.genericCodeForm.codeTypeId.value=="0"){
        alert("Please select the Code Type");
        return;
    }
    if(document.genericCodeForm.codeValue.value==""){
        alert("Please enter the Code");
        return;
    }else if(alphanumeric(document.forms[0].codeValue.value)==false){
        alert("Code should not have special characters");
        document.forms[0].codeValue.focus();
        return false;
    }
    var codes=document.genericCodeForm.codeTypeId.value;
    if(codes==<%=apcode%>){
        if(document.forms[0].codeValue.value.length>3){
            alert("Code should not be more than 3 characters");
            document.forms[0].codeValue.focus();
            return false;

        }
    }
    if(codes==<%=ccode%>){
        if(document.forms[0].codeValue.value.length>4){
            alert("Code should not be more than 4 characters");
            document.forms[0].codeValue.focus();
            return false;

        }
    }
    if(codes==<%=printCode%> || codes==<%=preDefRemarks%>){
        if(document.forms[0].codeValue.value.length!=5){
            alert("Code should be 5 characters");
            document.forms[0].codeValue.focus();
            return false;
        }
    }
    if(document.forms[0].codeValue.value.toLowerCase()=="null"){
        alert("Enter valid code");
        document.forms[0].codeValue.focus();
        return false;
    }
document.genericCodeForm.buttonValue.value="search";
document.genericCodeForm.submit();
}
function codelength(){
var codes=document.genericCodeForm.codeTypeId.value;
    if(codes==<%=ccode%>){
        document.genericCodeForm.codeValue.maxLength=4;
    }else if(codes==<%=apcode%>){
        document.genericCodeForm.codeValue.maxLength=3;
    }else if(codes==<%=cpcode%>){
        document.genericCodeForm.codeValue.maxLength=6;
    }else if (codes==<%=cncode%>){
        document.genericCodeForm.codeValue.maxLength=2;
        document.genericCodeForm.codeValue.size=2;
    }else if(codes==<%=fclcode%>){
        document.genericCodeForm.codeValue.maxLength=5;
    }else if(codes==<%=unitcode%>){
        document.genericCodeForm.codeValue.maxLength=5;
    }else if(codes==<%=vcode%>){
        document.genericCodeForm.codeValue.maxLength=5;
    }else if(codes==<%=lmcode%>){
        document.genericCodeForm.codeValue.maxLength=3;
    }
}
function toUppercase(obj){
    obj.value = obj.value.toUpperCase();
}
</script>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/genericCode" scope="request">
            <table width="100%"  border="0" cellpadding="0" cellspacing="0" align="center">
                <tr><td><font color="blue" size="2"><%=message%></font></td></tr>
                <tr height="8">
                </tr>
                <tr>
                    <td>
                        <table class="tableBorderNew" width="100%">
                            <tr class="tableHeadingNew"><font style="font-weight: bold">Add Generic Code</font></tr>
                            <tr class="style2">
                                <td>Code Type</td>
                                <td>
                                    <html:select property="codeTypeId" styleClass="textlabelsBoldForTextBox" onchange="codelength()" value="<%=codeType%>">
                                        <html:optionsCollection name="codeTypeList"/>
                                    </html:select>
                                </td>
                                <td class="style2">Code</td>
                                <td>
                                    <html:text property="codeValue" size="10" maxlength="10"  style="text-transform: uppercase"
                                               styleClass="textlabelsBoldForTextBox">
                                    </html:text>
                                </td>

                                <td>
                                    <input type="button" class="buttonStyleNew" value="Submit" id="search" onclick="searchform()"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                &nbsp;
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>


            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" />
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



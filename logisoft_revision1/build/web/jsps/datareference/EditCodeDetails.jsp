<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List, com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.hibernate.dao.CodetypeDAO"%>

<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.gp.cong.logisoft.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "ht
    tp://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>CodeDetails</title>
        <%
                    String path = request.getContextPath();
                    if (path == null) {
                        path = "../..";
                    }
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    String code = (String) request.getAttribute("code");
                    if (code == null) {
                        code = "0";
                    }
                    request.setAttribute("code", code);
                    GenericCode editDetails = new GenericCode();

                    if (session.getAttribute("editDetails") != null) {
                        editDetails = (GenericCode) session.getAttribute("editDetails");
                    }
                    if (editDetails != null && editDetails.getCodetypeid() != 0) {
                        code = editDetails.getCodetypeid().toString();
                    }
                    CodetypeDAO cdao = new CodetypeDAO();
                    Codetype cd = new Codetype();
                    String view = "";
                    if (request.getAttribute("view") != null) {
                        view = (String) request.getAttribute("view");
                    }

                    cd.setDescription("Airport Codes");
                    List apcodes = cdao.findByExample(cd);
                    int apcode = (CommonFunctions.isNotNullOrNotEmpty(apcodes)) ? ((Codetype) apcodes.get(0)).getCodetypeid() : 0;

                    cd.setDescription("Charge Codes");
                    List ccodes = cdao.findByExample(cd);
                    int ccode = (CommonFunctions.isNotNullOrNotEmpty(ccodes)) ? ((Codetype) ccodes.get(0)).getCodetypeid() : 0;

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
                    int vcode = (CommonFunctions.isNotNullOrNotEmpty(unitcodes)) ? ((Codetype) vcodes.get(0)).getCodetypeid() : 0;

                    cd.setDescription("Print Comments");
                    List prinCodes = cdao.findByExample(cd);
                    int printCode = (CommonFunctions.isNotNullOrNotEmpty(prinCodes)) ? ((Codetype) prinCodes.get(0)).getCodetypeid() : 0;
                    request.setAttribute("printCode", printCode);
        %>


        <%@include file="../includes/baseResources.jsp" %>

        <%
                    String msg = "";
                    String modify = null;
                    DBUtil dbUtil = new DBUtil();

                    request.setAttribute("codeTypeList", dbUtil.getCodesList());

                    List codeDetails = (List) session.getAttribute("codeDetails");
                    List<String> editDetailsList = (List) session.getAttribute("editDetailsList");

                    List cityList = null;
                    cityList = (List) request.getAttribute("cityList");

                    request.setAttribute("cityList", cityList);
                    request.setAttribute("countryList", dbUtil.getCountryList());
                    request.setAttribute("regionList", dbUtil.getRegionList());
                    request.setAttribute("uomCodes", dbUtil.getUomCodes());

                    if (request.getAttribute("message") != null) {
                        msg = (String) request.getAttribute("message");
                    }
                    modify = (String) session.getAttribute("modifyforCodeDetails");

                    if (session.getAttribute("view") != null) {
                        modify = (String) session.getAttribute("view");
                    }
        %>
        <script type="text/javascript">
            function citySelected(){
                if(document.CodeDetailsForm.column3.value=="0"){
                    alert("please select city");
                    document.CodeDetailsForm.column4.value="";
                    return false;
                }
                document.CodeDetailsForm.buttonValue.value="editcityselected";
                document.CodeDetailsForm.submit();
            }
            function alphanumeric(alphane){
                var numeric = alphane;
                var flag=0;
                for(var j=0; j<numeric.length; j++){
                    var alpha = numeric.charAt(j);
                    var hh = alpha.charCodeAt(0);
                    if((hh > 47 && hh<59) || (hh > 64 && hh<91) || (hh > 96 && hh<123)) {

                    }
                    else{
                        return false;
                    }
                }
                return true;
            }

            function save1(){
                if(document.CodeDetailsForm.code.value=="0")
                {
                    alert("Please select Code Type");
                    document.CodeDetailsForm.code.options[<%=code%>].selected=true;
                    document.CodeDetailsForm.code.focus();
                    return false;

                }
                else	if(document.forms[0].codevalue.value=="")
                {

                    alert("Enter Code");
                    document.forms[0].codevalue.focus();
                    return false;
                }
                else if(alphanumeric(document.forms[0].codevalue.value)==false)
                {
                    alert("Code should not have special characters");
                    document.forms[0].codevalue.focus();
                    return false;
                }
                if(document.forms[0].codevalue.value.toLowerCase()=="null")
                {
                    alert("Enter valid code");
                    document.forms[0].codevalue.focus();
                    return false;
                }
                else if(document.forms[0].codevalue.value.length>10)
                {
                    alert("Code should be less than 10 characters");
                    document.forms[0].codevalue.focus();
                    return false;

                }
                if(document.forms[0].column1.value=="")
                {
                    alert("Enter Code Description");
                    document.forms[0].column1.focus();
                    return false;
                }
                if(document.forms[0].column1.value.length>250)
                {
                    alert("Code description cannot be more than 250 characters");
                    document.forms[0].column1.focus();
                    return false;
                }
                if(document.forms[0].column2 && document.forms[0].column2.value != 'undefined' && null != document.forms[0].column2.value){
                    if(document.forms[0].column2.value.length > 1000){
                        alert("FCL QuoteRemarks cannot be more than 1000 characters");
                        document.forms[0].column2.focus();
                        return false;
                    }else{
                        document.forms[0].column2.value=document.forms[0].column2.value.toUpperCase();
                    }
                }
                if(document.forms[0].column3 && document.forms[0].column3.value != 'undefined' && null != document.forms[0].column3.value){
                    if(document.forms[0].column3.value.length > 1000)
                    {
                        alert("FCL BookingRemarks cannot be more than 1000 characters");
                        document.forms[0].column3.focus();
                        return false;
                    }else{
                        document.forms[0].column3.value=document.forms[0].column3.value.toUpperCase();
                    }
                }
                if(document.forms[0].column5 && document.forms[0].column5.value != 'undefined' && null != document.forms[0].column5.value){
                    if(document.forms[0].column5.value.length > 400)
                    {
                        alert("LCL Clause cannot be more than 400 characters");
                        document.forms[0].column1.focus();
                        return false;
                    }else{
                        document.forms[0].column5.value=document.forms[0].column5.value.toUpperCase();
                    }
                }
                var x=<%=code%>;

                if(x=="10")
                {
                    if(document.forms[0].column2.value=="0")
                    {
                        alert("please enter country");
                        return false;
                    }
                }
                if(x=="11")
                {
                    if(document.forms[0].column2.value=="0")
                    {
                        alert("please enter region");
                        return false;
                    }
                }
                document.forms[0].buttonValue.value="update";
                document.CodeDetailsForm.submit();

            }
            function funsubmit()
            {
                document.CodeDetailsForm.code.disabled=true;
            }


            function fundelete()
            {


                document.CodeDetailsForm.buttonValue.value="delete";

                document.CodeDetailsForm.submit();

            }

            function cancelbtn(){
                document.CodeDetailsForm.buttonValue.value="cancel";
                document.CodeDetailsForm.submit();
            }
            function confirmnote()
            {
                document.CodeDetailsForm.buttonValue.value="note";
                document.CodeDetailsForm.submit();
            }
            function disabled(val1,val2)
            {
                if(val1 == 0 || val1== 3)
                {

                    var imgs = document.getElementsByTagName('img');

                    for(var k=0; k<imgs.length; k++)
                    {

                        if(imgs[k].id != "cancel" && imgs[k].id !="note")
                        {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for(i=0; i<input.length; i++)
                    {
                        input[i].readOnly=true;
                        input[i].style.backgroundColor="#CCEBFF";
                    }
                    var inputTextArea = document.getElementsByTagName("textarea");
                    for(i=0; i<inputTextArea.length; i++)
                    {
                        inputTextArea[i].readOnly=true;
                        inputTextArea[i].style.backgroundColor="#CCEBFF";
                    }
                    var select = document.getElementsByTagName("select");

                    for(i=0; i<select.length; i++)
                    {
                        select[i].disabled=true;
                        select[i].style.backgroundColor="#CCEBFF";
                    }
                    document.getElementById("save").style.visibility = 'hidden';
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if(val1 == 1)
                {
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if(val1==3 && val2!="")
                {
                    alert(val2);
                }
                load();
            }
            function load() {
                if(<%=code%>==<%=apcode%>){
                    alert("hello");
                    document.CodeDetailsForm.column1.maxLength=33;
                }else if(<%=code%>==<%=ccode%>){
                    document.CodeDetailsForm.column1.maxLength=15;
                    document.CodeDetailsForm.column2.maxLength=2;
                    document.CodeDetailsForm.column3.maxLength=4;
                    document.CodeDetailsForm.column4.maxLength=2;
                }else if(<%=code%>==<%=cpcode%>){
                    document.CodeDetailsForm.column1.maxLength=75;
                    document.CodeDetailsForm.column2.maxLength=25;
                    document.CodeDetailsForm.column3.maxLength=75;
                    document.CodeDetailsForm.column5.maxLength        =10;
                }else if(<%=code%>==<%=cncode%>){
                    document.CodeDetailsForm.column1.maxLength=75;
                } else if(<%=code%> ==<%=fclcode%>){
                    document.CodeDetailsForm.column1.maxLength=150;
                }else if(<%=code%>==<%=unitcode%>){
                    document.CodeDetailsForm.column1.maxLength=30        ;
                }else if(<%=code%>==<%=vcode%>){
                    document.CodeDetailsForm.column1.maxLength=30;
                    document.CodeDetailsForm.column2.maxLength=15;
                    document.CodeDetailsForm.column3.maxLength=20;
                    document.CodeDetailsForm.column4.maxLength=8;
                    document.CodeDetailsForm.column5.maxLength=8;
                }
            }
            function countrySelected()
            {
                if(document.CodeDetailsForm.column2.value=="0")
                {
                    alert("please select Country");
                    document.CodeDetailsForm.column3.value="";
                    return false;
                }
                document.CodeDetailsForm.buttonValue.value="editcountry";
                document.CodeDetailsForm.submit();
            }
            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }


        </script>
        <script language="javascript">
            start = function(){
                serializeForm();
            }
            window.onload = start;
        </script>
    </head>
    <body class="whitebackgrnd" onkeydown="preventBack()">
        <html:errors/>
        <html:form  action="/egc" name="CodeDetailsForm" type="com.gp.cong.logisoft.struts.form.CodeDetailsForm" scope="request" styleId="editGenericCode">
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0"class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Code Details</td>
                    <td align="right">
                        <c:if test="${edit == 'yes'}">
                            <input type="button"  class="buttonStyleNew" id="save" value="Save" onclick="save1()"/>
                        </c:if>
                        <input type="button" class="buttonStyleNew" id="cancel" value="Go Back" onclick="cancelbtn()"/>
                        <%--<input type="button" class="buttonStyleNew" id="delete" value="Delete" onclick="fundelete()"/>--%>
                        <%-- <input type="button" class="buttonStyleNew" id="note" value="Note" onclick="confirmnote()" disabled="true"/>--%>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellspacing="3" cellpadding="0">
                            <tr>
                                <td width="30%"  class="textlabels">Code Type</td>
                                <td ><html:select property="code" disabled="true" value="<%=code%>" style="width:225px">
                                        <html:optionsCollection name="codeTypeList"   /> </html:select></td>
                                </tr>
                                <tr>
                                <%if (!(codeDetails == null)) {
                                %>
                                <td class="textlabels"><%= ((LabelValueBean) ((codeDetails).get(0))).getValue()%></td>
                                <%
                                            }
                                %>
                                <% %>
                                <%if (!(codeDetails == null)) {
                                                String label = ((LabelValueBean) (codeDetails.get(0))).getLabel();
                                                if (label.equals("Textbox")) {%>
                                <td><input name="codevalue"  type="text"
                                           value='<%=null != editDetailsList.get(0) ? editDetailsList.get(0) : ""%>' readonly="readonly"
                                           class="textlabelsBoldForTextBoxDisabledLook" maxlength="10"  size="10" onkeyup="toUppercase(this)"/>
                                </td>
                                <%} else {%><td><input type="radio"/>
                                </td>
                                <%	}
                                            }%>
                            </tr>
                            <%
                                        if (!(codeDetails == null)) {
                            %>
                            <tr>
                                <td class="textlabels"><%=((LabelValueBean) ((codeDetails).get(1))).getValue()%></td>
                                <c:choose>
                                    <c:when test="${code == '39'}">
                                        <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 250)"
                                                      class="textlabelsBoldForTextBox"><%=(null != editDetailsList.get(1) ? (String) editDetailsList.get(1).trim() : "")%></textarea>
                                        </td>
                                    </c:when>
                                    <c:when test="${code == '56'}">
                                        <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 50)"
                                                      class="textlabelsBoldForTextBox"><%=(null != editDetailsList.get(1) ? (String) editDetailsList.get(1).trim() : "")%></textarea>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 250)"
                                                      class="textlabelsBoldForTextBox"  style="text-transform: uppercase"><%=(null != editDetailsList.get(1) ? (String) editDetailsList.get(1).trim() : "")%></textarea>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                            <%
                                                                        int j = codeDetails.size();
                                                                        int i = 2;
                                                                        while (i < j) {
                                                                            String labelValue = ((LabelValueBean) ((codeDetails).get(i))).getValue();
                            %>
                            <tr class="textlabels">
                                <td class="textlabels"><%=labelValue%></td>
                                <%
                                                                    String label = ((LabelValueBean) (codeDetails.get(i))).getLabel();
                                                                    if (label.equals("Textbox")) {
                                                                        if (labelValue.equals("State")) {%>
                                <td><input name="column<%=i%>"  style="width:220px" type="text" readonly="readonly"
                                           value='<%=null != editDetailsList.get(i) ? editDetailsList.get(i) : ""%>' onkeyup="toUppercase(this)" class="textlabelsBoldForTextBox" />
                                </td>
                                <%} else {%>
                                <td><input name="column<%=i%>" style="width:220px"
                                           type="text" value='<%=null != editDetailsList.get(i) ? editDetailsList.get(i) : ""%>'  class="textlabelsBoldForTextBox"  onkeyup="toUppercase(this)"/>
                                </td>
                                <%}
                                                                          } else if (label.equals("radio")) {
                                                                              if (labelValue.equals("Commodity for")) {%>
                                <%if ("FCL".equals(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>" checked="checked" value="FCL"/>FCL
                                    <input type="radio" name="column<%=i%>" value="LCL" />LCL
                                    <input type="radio" name="column<%=i%>" value="BOTH" />BOTH
                                </td>
                                <%} else if ("LCL".equals(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>"  value="FCL"/>FCL
                                    <input type="radio" name="column<%=i%>" checked="checked" value="LCL" />LCL
                                    <input type="radio" name="column<%=i%>" value="BOTH" />BOTH
                                </td>
                                <%} else if ("BOTH".equals(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>"  value="FCL"/>FCL
                                    <input type="radio" name="column<%=i%>"  value="LCL" />LCL
                                    <input type="radio" name="column<%=i%>" value="BOTH" checked="checked"/>BOTH
                                </td>
                                <%} else {%>
                                <td><input type="radio" name="column<%=i%>"  value="FCL"/>FCL
                                    <input type="radio" name="column<%=i%>"  value="LCL" checked="checked"/>LCL
                                    <input type="radio" name="column<%=i%>" value="BOTH" />BOTH
                                </td>
                                <%}
                                                    } else if (labelValue.equals("Comments for")) {%>
                                <%if ("Original".equalsIgnoreCase(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>" checked="checked" value="ORIGINAL"/>ORIGINAL
                                    <input type="radio" name="column<%=i%>" value="NON-NEGOTIABLE"/>NON-NEGOTIABLE
                                    <input type="radio" name="column<%=i%>" value="BOTH" />BOTH
                                </td>
                                <%} else if ("Non-Negotiable".equalsIgnoreCase(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>"  value="ORIGINAL"/>ORIGINAL
                                    <input type="radio" name="column<%=i%>" checked="checked" value="NON-NEGOTIABLE"/>NON-NEGOTIABLE
                                    <input type="radio" name="column<%=i%>" value="BOTH" />BOTH
                                </td>
                                <%} else if ("Both".equalsIgnoreCase(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>"  value="ORIGINAL"/>ORIGINAL
                                    <input type="radio" name="column<%=i%>"  value="NON-NEGOTIABLE"/>NON-NEGOTIABLE
                                    <input type="radio" name="column<%=i%>" value="BOTH" checked="checked"/>BOTH
                                </td>
                                <%} else {%>
                                <td><input type="radio" name="column<%=i%>"  value="ORIGINAL"/>ORIGINAL
                                    <input type="radio" name="column<%=i%>"  value="NON-NEGOTIABLE"/>NON-NEGOTIABLE
                                    <input type="radio" name="column<%=i%>" value="BOTH" checked="checked"/>BOTH
                                </td>
                                <%}
                                                    } else {%>
                                <c:set var="defaultColor" value="white"/>
                                <%if (labelValue.equals("Vendor Optional(FCL)") || labelValue.equals("Vendor Optional(LCL)")) {%>
                                <c:set var="defaultColor" value="red"/>
                                <%}%>
                                <%if ("Y".equals(editDetailsList.get(i))) {%>
                                <td><input type="radio" name="column<%=i%>" checked="checked" value="Y"/>Y
                                    <input type="radio" name="column<%=i%>" value="N" /><font color="${defaultColor}">N</font>
                                </td>
                                <%} else {%>
                                <td><input type="radio" name="column<%=i%>"  value="Y"/>Y
                                    <input type="radio" name="column<%=i%>" checked="checked" value="N" /><font color="${defaultColor}">N</font>
                                </td>
                                <%}
                                                    }%>
                                <%} else if (label.equals("Listbox")) {
                                                                              String s = "column" + i;
                                                                              if (labelValue.equals("Country")) {
                                %>
                                <td><html:select property="<%=s%>" onchange="countrySelected()" value="<%=editDetailsList.get(i)%>">
                                        <html:optionsCollection name="countryList" styleClass="textfieldstyle" />
                                    </html:select></td>
                                    <%}
                                                                                  if (labelValue.equals("Region Codes")) {%>
                                <td><html:select property="<%=s%>" value="<%=(String) editDetailsList.get(i)%>" styleClass="textfieldstyle">

                                        <html:optionsCollection name="regionList" styleClass="textfieldstyle" />
                                    </html:select></td>
                                    <%				}
                                                                                  if (labelValue.equals("City")) {
                                    %>
                                <td><html:select property="<%=s%>" onchange="citySelected()" value="<%=editDetailsList.get(i)%>" style="width:220px">
                                        <html:optionsCollection name="cityList" styleClass="textfieldstyle" />
                                    </html:select></td>
                                    <%}
                                                                                  if (labelValue.equals("Cost/Sell Flag(FCL)") || labelValue.equals("Cost/Sell Flag(LCL)")) {
                                    %>
                                <td><html:select property="<%=s%>"  value="<%=editDetailsList.get(i)%>" styleClass="textfieldstyle">
                                        <html:option value="CS" style="color:red">COST AND SELL</html:option>
                                        <html:option value="C">ONLY COST</html:option>
                                        <html:option value="S">ONLY SELL</html:option>

                                    </html:select></td>
                                    <%}
                                                                                  if (labelValue.equals("Disable Cost Code(FCL)") || labelValue.equals("Disable Cost Code(LCL)")) {
                                    %>
                                <td><html:select property="<%=s%>"  value="<%=editDetailsList.get(i)%>" styleClass="textfieldstyle">
                                        <html:option value="" style="color:red">None</html:option>
                                        <html:option value="E">Export</html:option>
                                        <html:option value="I">Import</html:option>
                                        <html:option value="B">Both</html:option>

                                    </html:select></td>
                                    <%}
                                                                                  if (labelValue.equals("UOM 1")) {
                                    %>
                                <td><html:select property="<%=s%>"  value="<%=editDetailsList.get(i)%>" style="width:220px" styleClass="textfieldstyle">
                                        <html:optionsCollection name="uomCodes" styleClass="textfieldstyle" />
                                    </html:select></td>
                                    <%}
                                                                                  if (labelValue.equals("UOM 2")) {
                                    %>
                                <td><html:select property="<%=s%>" value="<%=editDetailsList.get(i)%>" style="width:220px" styleClass="textfieldstyle">
                                        <html:optionsCollection name="uomCodes" styleClass="textfieldstyle" />
                                    </html:select></td>
                                    <%}

                                                                              } else if (label.equalsIgnoreCase("Textarea")) {
                                    %>      	<td>
                                    <textarea name="column<%=i%>" rows="7" style="text-transform: uppercase" cols="60" class="textlabelsBoldForTextBox"
                                              ><%=null != editDetailsList.get(i) ? editDetailsList.get(i) : ""%></textarea>

                                </td>
                                <%      	}
                                                                    // for quote remarks.
%>
                            </tr><%i++;

                                            }
                                        }%>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="codeTypeId" id="codeTypeId" value="${code}"/>
        </html:form>
        <%if (view.equals("3")) {%>
        <script>
            window.parent.disableFieldsWhileLocking(document.getElementById("editGenericCode"));
        </script>
        <%}%>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

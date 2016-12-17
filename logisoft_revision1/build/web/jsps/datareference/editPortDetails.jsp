<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
         java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.RefTerminal"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:useBean id="portsair" class="com.gp.cong.logisoft.struts.form.EditPortDetailsForm" scope="request"></jsp:useBean>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld"  prefix="c"%>
<%
            String path = request.getContextPath();

            DBUtil dbUtil = new DBUtil();
            String cityName = "";
            String unCityCode = "";
            Ports portObj = null;
            List regionCodeList = new ArrayList();
            String country = "";
            String regionCode = "";
            String pierId = "";
            String portNameAbbr = "";
            Ports ports = new Ports();
            String modify = "";
            String controlNo = "";
            String rPortName = "";
            String type = "";
            Ports portobject = new Ports();
            Ports p = null;
            if (session.getAttribute("ports") != null) {
                portObj = (Ports) session.getAttribute("ports");
                if (portObj != null) {
                    if (portObj.getUncitycode() != null) {
                        UnLocation unLocationObj = portObj.getUncitycode();
                        unCityCode = unLocationObj.getUnLocationCode();
                    }
                    if (portObj.getPortname() != null) {
                        cityName = portObj.getPortname();
                    }

                    type = portObj.getType();

                    if (type != null && type.equals("K")) {
                        portsair.setType("on");
                    } else {
                        portsair.setType("off");
                    }

                    GenericCode genObjForUn = portObj.getRegioncode();
                    if (genObjForUn != null) {
                        regionCode = genObjForUn.getId().toString();
                    }
                    if (portObj.getRateFromPierCode() != null) {
                        pierId = portObj.getRateFromPierCode().getShedulenumber();
                        controlNo = portObj.getRateFromPierCode().getControlNo();
                        rPortName = portObj.getRateFromPierCode().getPortname();
                    }
                    if (portObj.getDrABBR() != null) {
                        portNameAbbr = portObj.getDrABBR().toString();
                    }
                    if (portObj.getCountryName() != null) {
                        country = portObj.getCountryName();
                    }
                }
            }

            if (regionCodeList != null) {
                regionCodeList = dbUtil.getGenericCodeList(new Integer(19), "yes", "Select Region Code");
                request.setAttribute("regionCodeList", regionCodeList);
            }

            modify = (String) session.getAttribute("modifyforports");
            if (session.getAttribute("view") != null) {
                modify = (String) session.getAttribute("view");

            }
            session.setAttribute("search", "editport");
            if (session.getAttribute("portobj") != null) {
                p = (Ports) session.getAttribute("portobj");
                session.removeAttribute("portobj");
                cityName = p.getPortname();
                if (p.getUncitycode() != null && p.getUncitycode().getId() != null) {
                    unCityCode = p.getUncitycode().getUnLocationCode();
                }
                country = p.getCountryName();
            }
            if (session.getAttribute("portobject") != null) {
                portobject = (Ports) session.getAttribute("portobject");
                pierId = portobject.getShedulenumber();
                controlNo = portobject.getControlNo();
                rPortName = portobject.getPortname();
                session.setAttribute("portobject", portobject);
            }
%>


<html> 
    <head>

        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" type="text/javascript">
		
            function submit1(){
                document.editPortDetails.buttonValue.value="unCityCodeSelected";
                document.editPortDetails.submit();
            }
            function piercode(){
                document.editPortDetails.buttonValue.value="pierCodeSelected";
                document.editPortDetails.submit();
            }
            function toUppercase(obj){
                obj.value = obj.value.toUpperCase();
            }
            function enableISO(obj){
                document.getElementById("countryiso").value="";
                if(document.editPortDetails.type.checked==false){
                    var disD=document.getElementById("countryiso");
                    disD.disabled=true;
                    disD.className="BackgrndColorForTextBox";
                }else if(document.editPortDetails.type.checked) {
                    var disK=document.getElementById("countryiso");
                    disK.disabled=false;
                    disK.className="textlabelsBoldForTextBox";
                }
            }
            function cancel(){
                document.editPortDetails.buttonValue.value="cancel";
                document.editPortDetails.submit();
            }
            function confirmnote(){
                document.editPortDetails.buttonValue.value="note";
                document.editPortDetails.submit();
            }
            function disabled(val1,val2){
                if(val1 == 0 || val1== 3){
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++){
                        if(imgs[k].id!="note"){
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for(i=0; i<input.length; i++){
                        if(input[i].id != "buttonValue" && input[i].name!="unCityCode" && input[i].name!="country" && input[i].name!="ratePierCode"
                            && input[i].name!="rateControlNo" && input[i].name!="ratePortName" ){
                            input[i].readOnly=true;
                            input[i].style.color="blue";
                        }
                    }
                    var select = document.getElementsByTagName("select");
                    for(i=0; i<select.length; i++){
                        select[i].disabled=true;
                        select[i].style.backgroundColor="blue";
                    }
                }
                enableISO1(val2);
            }
            function enableISO1(obj){
                if(obj=="null" || obj=="D") {
                    var disD=document.getElementById("countryiso");
                    disD.disabled=true;
                    disD.className="BackgrndColorForTextBox";
                }else if(obj=="K") {
                    var disK=document.getElementById("countryiso");
                    disK.disabled=false;
                    disK.className="textlabelsBoldForTextBox";
                }
            }
            function popup1(mylink, windowname){
                if (!window.focus)return true;
                var href;
                if (typeof(mylink) == 'string')
                    href=mylink;
                else
                    href=mylink.href;
                window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                document.editPortDetails.submit();
                return false;
            }
            // onLoad="disabled('<%=modify%>')" from the body tag
	
            function searchcity(){
                document.editPortDetails.buttonValue.value="searchcity";
                document.editPortDetails.submit();
            }
		
        </script>
    </head>

    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=type%>')" onkeydown="preventBack()">
        <html:form action="/editPortDetails" name="editPortDetails" styleId="editPortDetails" type="com.gp.cong.logisoft.struts.form.EditPortDetailsForm" scope="request">

            <input type="hidden" id="h1">

            <table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Port Details</td>
                    <td align="right"><input type="button" class="buttonStyleNew" id="note" value="Note" onclick="confirmnote()"
                                             disabled="true"/></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellpadding="2" cellspacing="0">
                            <tr class="textlabelsBold">
                                <td>City Name</td>
                                <td><html:text property="portName" value="<%=cityName%>" onkeyup="searchcity()"
                                           styleClass="textlabelsBoldForTextBox" style="width:150px"/>
                                    <img border="0" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button='+'editportcity&cityname='+document.editPortDetails.portName.value,'windows')">
                                </td>
                                <td>UN City Code</td>
                                <td><html:text property="unCityCode"  value="<%=unCityCode%>" readonly="true"
                                           styleClass="textlabelsBoldForTextBox" style="width:150px"/></td>
                                <td>Port City</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ports.portCity == 'Y'}">
                                            <input type="radio" name="portCity" value="Y" checked>Y&nbsp;
                                            <input type="radio" name="portCity" value="N">N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="portCity" value="Y">Y&nbsp;
                                            <input type="radio" name="portCity" value="N" checked>N
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                            </tr>
                            <tr class="textlabelsBold">
                                <td>Country</td>
                                <td><html:text property="country" styleClass="BackgrndColorForTextBox" value="<%=country%>"
                                           readonly="true" onfocus="this.blur();" style="width:150px"/></td>
                                <td>Pier Abbreviation</td>
                                <td><html:text property="pierCode" value="<%=portObj.getPiercode()%>" styleClass="textlabelsBoldForTextBox"
                                           maxlength="3" style="width:150px" onkeyup="toUppercase(this)"/></td>
                                <td >HS Code</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ports.hscode == 'Y'}">
                                            <input type="radio" name="hscode" value="Y" checked>Y&nbsp;
                                            <input type="radio" name="hscode" value="N">N
                                            <input type="radio" name="hscode" value="M" >M
                                            <input type="radio" name="hscode" value="D" >D
                                        </c:when>
                                        <c:when test="${ports.hscode == 'N'}">
                                            <input type="radio" name="hscode" value="Y" >Y&nbsp;
                                            <input type="radio" name="hscode" value="N" checked>N
                                            <input type="radio" name="hscode" value="M" >M
                                            <input type="radio" name="hscode" value="D" >D
                                        </c:when>
                                        <c:when test="${ports.hscode == 'D'}">
                                            <input type="radio" name="hscode" value="Y" >Y&nbsp;
                                            <input type="radio" name="hscode" value="N">N
                                            <input type="radio" name="hscode" value="M" >M
                                            <input type="radio" name="hscode" value="D" checked>D
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="hscode" value="Y">Y&nbsp;
                                            <input type="radio" name="hscode" value="N" >N
                                            <input type="radio" name="hscode" value="M" checked>M
                                            <input type="radio" name="hscode" value="D" >D
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                            </tr>
                            <tr class="textlabelsBold">
                                <td>ECI Port Code</td>
                                <td><html:text property="eciPortCode" value="<%=portObj.getEciportcode()%>" styleClass="textlabelsBoldForTextBox"
                                           maxlength="3" style="width:150px" onkeyup="toUppercase(this)"/></td>
                                <td>D/K(K-Checked)</td>
                                <td align="left"><html:checkbox property="type" name="portsair" onclick="enableISO(this)"></html:checkbox></td>
                                <td >NCM NO</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ports.ncmno == 'Y'}">
                                            <input type="radio" name="ncmno" value="Y" checked>Y&nbsp;
                                            <input type="radio" name="ncmno" value="N">N
                                        </c:when>
                                        <c:otherwise>
                                            <input type="radio" name="ncmno" value="Y" >Y&nbsp;
                                            <input type="radio" name="ncmno" value="N" checked>N
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Country ISO</td>
                                <td><html:text property="countryISO" styleId="countryiso" value="<%=portObj.getIsocode()%>" styleClass="textlabelsBoldForTextBox"
                                           onkeyup="toUppercase(this)" maxlength="2" style="width:150px"/></td>
                                <td>Region Codes</td>
                                <td><html:select property="regionCodes" styleClass="verysmalldropdownStyleForText" value="<%=regionCode%>"
                                             style="width:150px">
                                        <html:optionsCollection name="regionCodeList"/></html:select></td>
                                <td>Omit 2 Letter Country Code</td>
                                <td>
                                    <c:choose>
                                          <c:when test="${ports.omit2LetterCountryCode == 'Y'}">
                                              <input type="radio" name="omit2LetterCountryCode" value="Y" checked>Y&nbsp;
                                              <input type="radio" name="omit2LetterCountryCode" value="N">N
                                          </c:when>
                                          <c:otherwise>
                                               <input type="radio" name="omit2LetterCountryCode" value="Y">Y&nbsp;
                                              <input type="radio" name="omit2LetterCountryCode" value="N" checked>N
                                          </c:otherwise>
                                      </c:choose>
                                </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Port Name Abbreviation</td>
                                    <td><html:text property="portNameAbbr" value="<%=portNameAbbr%>" maxlength="4" styleClass="textlabelsBoldForTextBox"
                                           style="width:150px"/></td>
                                <td>Rates From Pier Code</td>
                                <td><html:text property="ratePierCode" value="<%=pierId%>" readonly="true" styleClass="textlabelsBoldForTextBox"
                                           style="width:150px"/>
                                    <img border="0" id="search" src="<%=path%>/img/icons/display.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'editportcity','windows')" >
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Rates From Control Number</td>
                                <td><html:text property="rateControlNo" value="<%=controlNo%>" styleClass="BackgrndColorForTextBox"
                                           readonly="true" onfocus="this.blur();" style="width:150px"/></td>
                                <td>Rates from Port Name</td>
                                <td><html:text property="ratePortName" value="<%=rPortName%>" styleClass="BackgrndColorForTextBox"
                                           readonly="true" onfocus="this.blur();" style="width:150px"/></td>
                            </tr>
                        </table>
                    </td></tr>
            </table>

            <html:hidden property="buttonValue" styleId="buttonValue"/>

        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

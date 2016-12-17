<%@include file="../includes/jspVariables.jsp" %>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<jsp:directive.page import="com.gp.cong.common.CommonConstants"/>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*"%>
<%@ page import="com.gp.cong.logisoft.hibernate.dao.*" %>
<%@ page import="com.gp.cong.logisoft.domain.*" %>

<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>



<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String code = null;
if(null != request.getParameter("codeType") && !request.getParameter("codeType").equals("")){
    code = request.getParameter("codeType");
}else{
    code = (String)request.getAttribute("code");
}
request.setAttribute("code", code);
if(code==null){
	code="0";
}
%>
<%
DBUtil dbUtil=new DBUtil();
request.setAttribute("codeTypeList",dbUtil.getCodesList());
List cityList=null;
if(request.getAttribute("cityList")!=null)
{
 cityList=(List)request.getAttribute("cityList");
}
else
{
GenericCodeDAO gcdao=new GenericCodeDAO();
GenericCode gc=new GenericCode();
gc.setCode("US");
List<GenericCode> countryList= gcdao.findByExample(gc);
GenericCode countryId=countryList.get(0);
request.setAttribute("cityList", dbUtil.getCityList(countryId));
}
request.setAttribute("countryList",dbUtil.getCountryList());
request.setAttribute("regionList",dbUtil.getRegionList());
request.setAttribute("uomCodes",dbUtil.getUomCodes());
List codeDetails=(List)session.getAttribute("codeDetails");
request.setAttribute("codeDetails",codeDetails);
String state=(String)request.getAttribute("state");
if(state==null){
	state="";
}
List lst=new ArrayList();
lst.add(0,"");
lst.add(1,"");
lst.add(2,"");
lst.add(3,"");
lst.add(4,"");
lst.add(5,"");
lst.add(6,"");
lst.add(7,"");
String codevalue="";
if(!(session.getAttribute("codevalue")==null)){
codevalue=(String)session.getAttribute("codevalue");
}
if(!(request.getAttribute("column1")==null)){
	String column1=(String)request.getAttribute("column1");
	lst.add(1,column1);
}
String column2="";
if(!(session.getAttribute("column2")==null)){
	 column2=(String)session.getAttribute("column2");
	lst.add(2,column2);

}
if(!(request.getAttribute("column3")==null)){
	String column3=(String)request.getAttribute("column3");
	lst.add(3,column3);
}
if(!(request.getAttribute("column4")==null)){
	String column4=(String)request.getAttribute("column4");
	lst.add(4,column4);
}
if(!(request.getAttribute("column5")==null)){
	String column5=(String)request.getAttribute("column5");
	lst.add(5,column5);
}
if(!(request.getAttribute("column6")==null)){
	String column6=(String)request.getAttribute("column6");
	lst.add(6,column6);
}
if(!(request.getAttribute("column7")==null)){
	String column7=(String)request.getAttribute("column7");
	lst.add(7,column7);
}
//added on 03-mar-08
CodetypeDAO cdao=new CodetypeDAO();
Codetype cd=new Codetype();
cd.setDescription("Airport Codes");
List apcodes=cdao.findByExample(cd);
int apcode=(CommonFunctions.isNotNullOrNotEmpty(apcodes))?((Codetype)apcodes.get(0)).getCodetypeid():0;
cd.setDescription("Charge Codes");
List ccodes=cdao.findByExample(cd);
int ccode=(CommonFunctions.isNotNullOrNotEmpty(ccodes))?((Codetype)ccodes.get(0)).getCodetypeid():0;
cd.setDescription("Commodity Codes");
List cpcodes =cdao.findByExample(cd);
int cpcode = (CommonFunctions.isNotNullOrNotEmpty(cpcodes))?((Codetype)cpcodes.get(0)).getCodetypeid():0;
cd.setDescription("Correction Notice Codes");
List cncodes= cdao.findByExample(cd);
int cncode =(CommonFunctions.isNotNullOrNotEmpty(cncodes))?((Codetype)cncodes.get(0)).getCodetypeid():0;
cd.setDescription("FCL Release Codes");
List fclcodes =cdao.findByExample(cd);
int fclcode = (CommonFunctions.isNotNullOrNotEmpty(fclcodes))?((Codetype)fclcodes.get(0)).getCodetypeid():0;
cd.setDescription("Unit Types");
List unitcodes =cdao.findByExample(cd);
int unitcode = (CommonFunctions.isNotNullOrNotEmpty(unitcodes))?((Codetype)unitcodes.get(0)).getCodetypeid():0;
cd.setDescription("Vessel Codes");
List vcodes =cdao.findByExample(cd);
int vcode = (CommonFunctions.isNotNullOrNotEmpty(vcodes))?((Codetype)vcodes.get(0)).getCodetypeid():0;
cd.setDescription("Loading Instructions Master");
List lmcodes =cdao.findByExample(cd);
int lmcode = (CommonFunctions.isNotNullOrNotEmpty(lmcodes))?((Codetype)lmcodes.get(0)).getCodetypeid():0;
cd.setDescription("Print Comments");
List prinCodes = cdao.findByExample(cd);
int printCode = (CommonFunctions.isNotNullOrNotEmpty(prinCodes)) ? ((Codetype) prinCodes.get(0)).getCodetypeid() : 0;
request.setAttribute("printCode", printCode);
 
 
//added till here on 03-mar-08
%>

<html>
  <head>
<title>New Managing Carriers OAT</title>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<script language="javascript">
            start = function(){         
                serializeForm();
              }
            window.onload = start;
</script>

<script type="text/javascript">
	function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
function alphanumeric(alphane)
 {
	 var numeric = alphane;
	 var flag=0;
	 for(var j=0; j<numeric.length; j++)
		{
		  var alpha = numeric.charAt(j);
		  var hh = alpha.charCodeAt(0);
		  if((hh > 47 && hh<59) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
		  {
		  	
		  }
		else
			{
			 return false;
		  }
		}
	return true;	
		
 
}

   function submit1()
   {
   if(document.CodeDetailsForm.code.value=="0")
   {
     alert("Please select Code Type");
     document.CodeDetailsForm.code.focus();
     return false;
      } 
   
   	document.forms[0].buttonValue.value="codeselected";
        document.CodeDetailsForm.submit();
   }
   
  
   	 function cancelbtn()
    {
     
     document.CodeDetailsForm.buttonValue.value="newcancel";
     document.CodeDetailsForm.submit();
   }
  function load()
   {
   
  if(<%=code%>==<%=apcode%>)
  {
 
  document.CodeDetailsForm.column1.maxLength=30;
  // document.CodeDetailsForm.column1.value.toUppercase(this);
  }  
  else if(<%=code%>==<%=ccode%>)
  {
  document.CodeDetailsForm.column1.maxLength=15;
  document.CodeDetailsForm.column2.maxLength=2;
   
  document.CodeDetailsForm.column3.maxLength=4;
  document.CodeDetailsForm.column4.maxLength=2;
 
   
  }
  else if(<%=code%>==<%=cpcode%>)
  {
   document.CodeDetailsForm.column1.maxLength=75;
  
    document.CodeDetailsForm.column2.maxLength=25;
   
    document.CodeDetailsForm.column3.maxLength=75;
    
    document.CodeDetailsForm.column5.maxLength=10;
   
    
  }
  else if(<%=code%>==<%=cncode%>){
  document.CodeDetailsForm.column1.maxLength=75;
  }
  else if(<%=code%> ==<%=fclcode%>)
  {
  document.CodeDetailsForm.column1.maxLength=150;
  }
  else if(<%=code%>==<%=unitcode%>)
		   {
		   document.CodeDetailsForm.column1.maxLength=30;
		   }
		   else if(<%=code%>==<%=vcode%>)
  { 
  
   document.CodeDetailsForm.column1.maxLength=30;
  
    document.CodeDetailsForm.column2.maxLength=15;
    
    document.CodeDetailsForm.column3.maxLength=20;
    
    document.CodeDetailsForm.column4.maxLength=8;
   
    document.CodeDetailsForm.column5.maxLength=8;
   
  }
       else if(<%=code%>==<%=lmcode%>)
       {
        document.CodeDetailsForm.column1.maxLength=10;
        
      }
}
   function citySelected()
   {
   		document.CodeDetailsForm.buttonValue.value="cityselected";
     	document.CodeDetailsForm.submit();	 
    }
    function countrySelected()
    {
    	document.CodeDetailsForm.buttonValue.value="countryselected";
     	document.CodeDetailsForm.submit();	
    }
    function regionSelected()
    {
    	document.CodeDetailsForm.buttonValue.value="regionselected";
     	document.CodeDetailsForm.submit();	
    }
function save(){
    if(document.CodeDetailsForm.column2 && document.CodeDetailsForm.column2.value != 'undefined' &&
        null != document.CodeDetailsForm.column2.value){
        if(document.CodeDetailsForm.column2.value.length > 1000){
            alert("FCL QuoteRemarks cannot be more than 1000 characters");
            document.CodeDetailsForm.column2.focus();
            return false;
    }else{
        document.CodeDetailsForm.column2.value=document.CodeDetailsForm.column2.value.toUpperCase();
    }
}
    if(document.CodeDetailsForm.column3 && document.CodeDetailsForm.column3.value != 'undefined' &&
        null != document.CodeDetailsForm.column3.value){
        if(document.CodeDetailsForm.column3.value.length > 1000){
            alert("FCL BookingRemarks cannot be more than 1000 characters");
            document.CodeDetailsForm.column3.focus();
            return false;
    }else{
        document.CodeDetailsForm.column3.value=document.CodeDetailsForm.column3.value.toUpperCase();
    }
}
    if(document.CodeDetailsForm.column5 && document.CodeDetailsForm.column5.value != 'undefined' &&
        null != document.CodeDetailsForm.column5.value){
        if(document.CodeDetailsForm.column5.value.length > 400){
           alert("LCL Clause cannot be more than 400 characters");
            document.CodeDetailsForm.column5.focus();
            return false;
    }else{
        document.CodeDetailsForm.column5.value=document.CodeDetailsForm.column5.value.toUpperCase();
    }
}
    	document.CodeDetailsForm.buttonValue.value="save";
     	document.CodeDetailsForm.submit();	
}
</script>
</head>
<body class="whitebackgrnd">
<html:errors/>
<html:form  action="/gc" name="CodeDetailsForm" type="com.gp.cong.logisoft.struts.form.CodeDetailsForm" scope="request">
<html:hidden property="buttonValue"/>
 <html:hidden  property="codeTypeId" value="<%=code%>"/>
 <table width="100%" border="0" cellspacing="2" cellpadding="2" class="tableBorderNew">
  <tr class="tableHeadingNew"><td>Code Details</td>
  <td align="right"> <input type="button" class="buttonStyleNew" value="Save" onclick="save()"/>
	<input type="button" class="buttonStyleNew" value="Go Back" onclick="cancelbtn()"/></td>
  </tr>
    <tr>
      <td class="textlabels">Code Type</td>
      <td>
 <html:select property="code" onchange="submit1()" disabled="true" value="<%=code%>" style="width:215px">
        <html:optionsCollection name="codeTypeList" styleClass="textfieldstyle" />
</html:select></td>
<tr></tr>
<%if(!(codeDetails==null) && (codeDetails).get(0)!=null)
	{
		
		%><td class="textlabels"><%= ((LabelValueBean)((codeDetails).get(0))).getValue()%></td>
<%	} %>  
<%if(!(codeDetails==null))
	{
		String label= ((LabelValueBean)(codeDetails.get(0))).getLabel();
		if(label.equals("Textbox"))
		{%>
		<td><input name="codevalue"   class="textlabelsBoldForTextBox" 
		type="text" value="<%=codevalue%>" maxlength="10"  size="10" readonly="readonly"/>
		</td>
		<%}else
		{ %><td><input type="radio"/>
		</td>
<%	} }%> 
    
<%
	if(!(codeDetails==null))
{
%>    <tr>
      <td class="textlabels"><%=((LabelValueBean)((codeDetails).get(1))).getValue() %>
          <c:choose>
              <c:when test="${code == '39'}">
                  <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 200)"
                    class="textlabelsBoldForTextBox"></textarea>
              </td>
              </c:when>
              <c:when test="${code == '56'}">
                  <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 50)"
                    class="textlabelsBoldForTextBox"></textarea>
              </td>
              </c:when>
              <c:otherwise>
                  <td><textarea name="column1" rows="7" cols="60"  onkeypress="return checkTextAreaLimit(this, 200)"
                    class="textlabelsBoldForTextBox"  style="text-transform: uppercase"></textarea></td>
              </c:otherwise>
          </c:choose>
      </tr>
      <tr class="textlabels">
       <%int j=codeDetails.size();
      int i=2;
      while(i<j)
      {
      	String labelValue=((LabelValueBean)((codeDetails).get(i))).getValue();
      	 %>
      	<td class="textlabels"><%=labelValue %></td>
	<%String label= ((LabelValueBean)(codeDetails.get(i))).getLabel();
      	 	
		if(label.equals("Textbox"))
		{ 
		 if(labelValue.equals("State"))
		  // changes do on march 07
		{
		%>	
			<td><input name="column<%=i%>"  readonly="readonly" type="text" 
			value="<%=state%>" style="width:215px"  class="textlabelsBoldForTextBox"/>
			</td>
		<%}
		else
		
		{
		if((Integer.parseInt(code)== ccode))
		 {
		 	%>
			<td><input name="column<%=i%>"   
			 type="text" value="<%=state%>" onkeypress="return checkIts(event)" style="width:215px"  class="textlabelsBoldForTextBox"/>
			</td>
		<%}
		else if((Integer.parseInt(code)== vcode))
		{
		%>
			<td><input name="column<%=i%>"  size="20"  type="text" value="<%=state%>"  class="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" style="width:215px"/>
			</td>
		<%}else if((Integer.parseInt(code)== cpcode))
		{
		%>
			<td><input name="column<%=i%>"  size="20"  class="textlabelsBoldForTextBox"  type="text" value="<%=state%>" onkeyup="toUppercase(this)" style="width:215px"/>
			</td>
		<%}
		else
		{
		%>
			<td><input name="column<%=i%>" type="text" value="<%=lst.get(i)%>" style="width:215px" />
			</td>
		<%}}}
		
		 // done on mar-07-08 HYD.
		
		else if(label.equals("radio")){
		 if(labelValue.equals("Commodity for")){%>
			<td class="textlabels"><input type="radio" name="column<%=i%>" checked="checked" value="FCL"/>FCL
                    <input type="radio" name="column<%=i%>" value="LCL" />LCL
                    </td>
                <%}else{%>
                        <c:set var="defaultColor" value="white"/>
                      <%if(labelValue.equals("Vendor Optional(FCL)") || labelValue.equals("Vendor Optional(LCL)")){%>
                          <c:set var="defaultColor" value="red"/>
                      <%}%>
                      <%if(labelValue.equals("FCL Bullet")){%>
                      <td class="textlabels"><input type="radio" name="column<%=i%>" value="Y"/>Y
			<input type="radio" name="column<%=i%>" checked="checked" value="N" /><font color="${defaultColor}">N</font>
                      </td>
                      <%}else{%>
                      <td class="textlabels"><input type="radio" name="column<%=i%>" checked="checked" value="Y"/>Y
			<input type="radio" name="column<%=i%>" value="N" /><font color="${defaultColor}">N</font>
                      </td>
                      <%}%>
                <%}
                 }else if(label.equals("Listbox")){
                    String s="column"+i;
                            if(labelValue.equals("Country"))
                 {%>
      	<td><html:select property="<%=s%>" onchange="countrySelected()" value="<%=(String)lst.get(i)%>" style="width:215px">
      	
      	<html:optionsCollection name="countryList"   />
      	</html:select></td>
      	<%}
      	if(labelValue.equals("Region Codes"))
      	{
      	 %>
      	<td><html:select property="<%=s%>" value="<%=(String)lst.get(i)%>" style="width:215px">
      	
      	<html:optionsCollection name="regionList"   />
      	</html:select></td>
      	<%}
      	
      	 if(labelValue.equals("City"))
      	{ 
      	%>
      	<td>
      	<html:select property="<%=s%>" onchange="citySelected()"  value="<%=(String)lst.get(i)%>">
      	<html:optionsCollection name="cityList"   />
      	</html:select></td>
      	<%}
                    	      	if(labelValue.equals("Cost/Sell Flag(FCL)") || labelValue.equals("Cost/Sell Flag(LCL)")){
 %>
	      	<td><html:select property="<%=s%>"  value="" styleClass="textfieldstyle">
                        <html:option value="CS" style="color:red">COST AND SELL</html:option>
	      	<html:option value="C">ONLY COST</html:option>
	      	<html:option value="S">ONLY SELL</html:option>

	      	</html:select></td>
	      	<%}
               if(labelValue.equals("Disable Cost Code(FCL)") || labelValue.equals("Disable Cost Code(LCL)")){
 %>
	      	<td><html:select property="<%=s%>"  value="" styleClass="textfieldstyle">
	      	<html:option value="" style="color:red">None</html:option>
                 <html:option value="E">Export</html:option>
	      	<html:option value="I">Import</html:option>
	      	<html:option value="B">Both</html:option>

	      	</html:select></td>
	      	<%}
                    if(labelValue.equals("UOM 1")){
 %>
	      	<td><html:select property="<%=s%>"   style="width:220px" styleClass="textfieldstyle">
	      	<html:optionsCollection name="uomCodes" styleClass="textfieldstyle" />
	      	</html:select></td>
	      	<%}
               if(labelValue.equals("UOM 2")){
 %>
	      	<td><html:select property="<%=s%>"  style="width:220px" styleClass="textfieldstyle">
	      	<html:optionsCollection name="uomCodes" styleClass="textfieldstyle" />
	      	</html:select></td>
	      	<%}
      	} else if(label.equalsIgnoreCase("Textarea")){%>
                <td><textarea name="column<%=i%>" rows="7" cols="60" class="textlabelsBoldForTextBox" style="text-transform: uppercase" ></textarea></td>

<%      	}
	// for quote remarks.
%>
      	</tr>
      	 <%i++;}}%>
      	 </table>
        
      	
</html:form>
<script>

</script>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


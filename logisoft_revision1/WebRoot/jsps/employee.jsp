<%-- 
    Document   : employee.jsp
    Created on : Dec 14, 2016, 11:28:30 AM
    Author     : omi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" type="text/css" href="" />
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<script type="text/javascript" src="${path}/js/userFunction.js"></script>

<html>
 <%@include file="/taglib.jsp" %>     <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee</title>
 <style>
  .container{
      width:100%;
      background-color:#ccc;
         
     }     
     
input[type=text], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}

input[type=submit] {
    width: 100%;
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

input[type=submit]:hover {
    background-color: #45a049;
}

div {
    border-radius: 5px;
    background-color: #f2f2f2;
    padding: 20px;
}
</style>
    </head>
    <body>
    <h1>Hello </h1><c:if test="${not empty employeeForm.name}">
        <label style="font-weight:bold;"> i am added </label>  ${employeeForm.name}
   
    </c:if>
    <div class="container">
    <div>
    <cong:form action="employeeInfo.do" name="employeeForm" id="employeeForm">
        
        <label>Name       </label><cong:text name="name" id="name"></cong:text>
        <label>Last Name  </label><cong:text name="lastName" id="lastName"></cong:text>
        <label style="font-weight:bold;">Address:-   </label><br><br>
        <label>Phone      </label> <cong:text name="phoneNumber" id="phoneNumber"></cong:text>
        
        <label>Email      </label> <cong:text name="mail" id="mail"></cong:text>
          <c:set var="index" value="0"></c:set>
        <cong:link href="employeeInfo.do?action=addRow">Add Row</cong:link>
      
        <c:if test="${not empty employeeForm.addressList}">
        <cong:table>
        <c:forEach var="item" items="${employeeForm.addressList}" varStatus="status">
        <cong:tr>
            <cong:td> <cong:text name="addressList[${index}].country"></cong:text></cong:td>
            <cong:td> <cong:text name="addressList[${index}].zipcode" ></cong:text></cong:td>
            <cong:td><cong:link href="employeeInfo.do?action=removeRow">Remove Row</cong:link> </cong:td>       
        </cong:tr>  
         <c:set var="index" value="${index+1}"></c:set>
        </c:forEach>
        </cong:table>    
         </c:if>
        <cong:submit name="action" value="save"></cong:submit>
        
    </cong:form>
        </div>
        </div>
    
    </body>
</html>

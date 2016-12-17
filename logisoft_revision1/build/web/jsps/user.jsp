<%-- 
    Document   : search
    Created on : Dec 8, 2016, 11:38:55 PM
    Author     : omi
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" type="text/css" href="css/userform.css" />
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<script type="text/javascript" src="${path}/js/userFunction.js"></script>

<html>
    <%@include file="/taglib.jsp" %> 
   
    <body>     
         <div class="container">
             <div class="main">
                <cong:table cellspacing="5px" border="1" style="padding-top:30px;">  
                <cong:form name="personForm" id="personForm" action="inputvalue.do">
                <cong:tr><cong:td>User Id     </cong:td><cong:td> <cong:text id="id" name="id"/></cong:td></cong:tr>
                <cong:tr ><cong:td>First Name  </cong:td><cong:td> <cong:text id="name" name="name"/></cong:td></cong:tr>
                <cong:tr ><cong:td>Last Name   </cong:td><cong:td> <cong:text id="lname" name="lname"/></cong:td></cong:tr> 
                <cong:tr ><cong:td>Gender <cong:radio name="gender" value="male">Male</cong:radio>
                                          <cong:radio name="gender" value="female">Female</cong:radio></cong:td></cong:tr>
                <cong:tr ><cong:td>User Post   </cong:td><cong:td> <cong:text id="post" name="post"/></cong:td></cong:tr>
                <cong:tr ><cong:td>Phone       </cong:td><cong:td> <cong:text id="phone" name="phone"/></cong:td></cong:tr>
              <%--  <cong:tr ><cong:td>Have you Voter Id Card<cong:checkbox name="extraDetailList" value="VoterId"></cong:checkbox></cong:td></cong:tr>
                <cong:tr ><cong:td>Have you Driving License<cong:checkbox name="extraDetailList" value="DrivingLicense"></cong:checkbox></cong:td></cong:tr> --%>
                <cong:tr><cong:td><cong:submit name="action" value="add"/></cong:td></cong:tr>
            </cong:form>	
        </cong:table>      
        <br>
        </div>
        <c:if test="${not empty personForm.userList}">
            <h5 style="padding-left:425px;">  Display Records</h5>
            <%--        <h3>Search By Name :- </h3>
                    <cong:hidden name="action" id="action"></cong:hidden>
                        <cong:select name="byName" id="byName" onChange="search.searchByName()" style="align:center;width:10%">
                            <cong:option value="">select</cong:option>   
                            <c:forEach var="item"  items="${personForm.userList}"> 
                                <cong:option value="${item.name}">${item.name}</cong:option>            
                            </c:forEach> 
                        </cong:select>             --%>

            <br>

            <cong:table cellspacing="5px" align="center" border="1" width="50%">
                <cong:tr><cong:th>Id</cong:th><cong:th>Name</cong:th><cong:th>Last Name</cong:th><cong:th>Gender</cong:th><cong:th>Post</cong:th><cong:th>Phone</cong:th></cong:tr>
                <cong:form name="personForm" id="personForm" action="inputvalue.do">
                    <c:forEach var="item" items="${personForm.userList}">
                        <cong:tr>
                            <cong:td>${item.id}</cong:td>
                            <cong:td>${item.name}</cong:td>
                            <cong:td>${item.lname}</cong:td> 
                            <cong:td>${item.gender}</cong:td>
                            <cong:td>${item.post}</cong:td>
                            <cong:td>${item.phone}</cong:td>    

                            <cong:td><cong:link href="inputvalue.do?action=delete&id=${item.id}">Delete</cong:link></cong:td>                         
                            <cong:td><cong:link href="inputvalue.do?action=edit&id=${item.id}">Edit</cong:link></cong:td>                         

                        </cong:tr>            
                    </c:forEach> 
                </cong:form>
            </cong:table>
        </c:if>
        <br>
        <cong:form name="personForm" id="personForm" action="inputvalue.do">
            ByName :-   <cong:text name="name" ></cong:text>
            <cong:submit name="action" value="searchName"/>
        </cong:form>
        <c:if test="${not empty personForm.userSearchList}">    
            <h5 style="padding-left:425px;">Display Search Records</h5>   
            <cong:table cellspacing="5px" align="center" border="1" width="50%">

                <c:forEach var="Item" items="${personForm.userSearchList}">  
                    <cong:tr>  <cong:td>${Item.id}</cong:td>
                        <cong:td>${Item.name}</cong:td>
                        <cong:td>${Item.lname}</cong:td> 
                        <cong:td>${item.gender}</cong:td>
                        <cong:td>${Item.post}</cong:td>
                        <cong:td>${Item.phone}</cong:td>   
                    </cong:tr>  
                </c:forEach>

            </cong:table>
        </c:if>
         </div>
    </body>
</html>
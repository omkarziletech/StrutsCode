<%-- 
    Document   : origins
    Created on : Mar 1, 2013, 5:45:33 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 998px;">
    <tr>
	<th>
    <div class="float-left">Origin List</div>
    <div class="float-right">
	<a href="javascript: closeOrigins()">
	    <img alt="Close Origin List" src="${path}/images/icons/close.png"/>
	</a>
    </div>
</th>
</tr>
<tr>
    <td class="label table-head-background">
	<span class="float-left" style="margin: 0 0 0 5px;">
	    <input type="checkbox" name="checkAllOrigins" id="checkAllOrigins" onclick="checkAllOrigins(this);">
	    <label for="checkAllOrigins">ALL</label>
	</span>
	<span class="float-left" style="margin: 1px 0 0 15px;">
	    <input type="button" value="Get Rates" class="button" onclick="showRatesForDestination();"/>
	</span>
    </td>
</tr>
<tr>
    <td>
	<div style="width: 990px;overflow: auto;height: 290px;">
	    <table class="table" style="margin: 0;border: none;">
		<tr>
		    <c:set var="previousRegion" value=""/>
		    <c:forEach var="origins" items="${originsList}">
			<td>
			    <ul class="label list-style-none">
				<c:forEach var="origin" items="${origins}">
				    <c:if test="${previousRegion != origin.region}">
					<li class="table-head-background" style="color: blue;">
					    <input type="checkbox" name="regionCheck" 
						   id="regionCheck${origin.region}" value="${origin.region}" onclick="checkRegion(this);">
					    <label for="regionCheck${origin.region}">${origin.region}</label>
					</li>
					<c:set var="previousRegion" value="${origin.region}"/>
				    </c:if>
				    <li>
					<input type="checkbox" name="originCheck"
					       region="${origin.region}" id="originCheck${origin.name}" value="${origin.id}">
					<label for="originCheck${origin.name}">${origin.name}</label>
				    </li>
				</c:forEach>
			    </ul>
			</td>
		    </c:forEach>
		</tr>
	    </table>
	</div>
    </td>
</tr>
</table>
<%-- 
    Document   : email
    Created on : Aug 29, 2012, 2:50:13 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div style="width: 798px;height: 448px;margin: 1px;overflow: auto;">
    <table class="table">
	<thead>
	    <tr>
		<th colspan="2">
		    <div class="float-left">Dispute Email</div>
		    <div class="float-right">
			<a href="javascript: closeEmail()">
			    <img alt="Close Email" src="${path}/images/icons/close.png"/>
			</a>
		    </div>
		</th>
	    </tr>
	</thead>
	<tbody>
	    <tr>
		<td class="label width-60px">User</td>
		<td>
		    <input type="text" name="loginName" id="loginName" style="600px" class="float-left jqte required" validate="user:Please enter valid user"/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-60px">To</td>
		<td>
		    <input type="text" name="toAddress" id="toAddress" style="background-color:#C9E5F2;600px"
			   class="float-left jqte required" validate="email:Please enter valid to address" readonly/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-60px">CC</td>
		<td>
		    <input type="text" name="ccAddress" id="ccAddress" style="600px"
			   class="float-left jqte" validate="email:Please enter valid cc address:optional"/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-60px">BCC</td>
		<td>
		    <input type="text" name="bccAddress" id="bccAddress" style="600px"
			   class="float-left jqte" validate="email:Please enter valid bcc address:optional"/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-60px">Subject</td>
		<td>
		    <input type="text" name="subject" id="subject" style="600px"
			   class="float-left jqte required" validate="subject:Please enter subject"/>
		</td>
	    </tr>
	    <tr>
		<td class="label width-60px">Body</td>
		<td>
		    <textarea name="body" id="body"></textarea>
		</td>
	    </tr>
	    <tr>
		<td colspan="2" class="align-center">
		    <input type="button" value="Send" class="button" onclick="sendEmail()"/>
		    <input type="button" value="Cancel" class="button" onclick="closeEmail()"/>
		</td>
	    </tr>
	</tbody>
    </table>
</div>
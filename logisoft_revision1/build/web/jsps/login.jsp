<%--
    Document   : login
    Created on : Jul 19, 2010, 12:32:07 PM
    Author     : Lakshmi Naryanan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta name="keywords" content=""/>
        <meta name="description" content=" "/>
        <meta name="author" content="LogiwareInc."/>
        <title>Logiware</title>
        <link href="${path}/css/layout/login-page.css" type="text/css" rel="stylesheet"/>
        <script src="${path}/js/jquery/jquery-1.3.1.js" type="text/javascript" ></script>
        <script src="${path}/js/jquery/jquery.caps.js" type="text/javascript" ></script>
    </head>    
    <body class="body" style="overflow: hidden">
        <c:choose>
            <c:when test="${not empty loginuser}">
                <c:redirect url="/index"/>
            </c:when>
            <c:otherwise>
                <html:form action="/login" method="post" name="loginForm" styleId="loginForm" type="com.logiware.form.LoginForm" scope="request">
                    <div class="body-container">
			<div id="main-container">
			    <div id="login-curve">
				<div id="login-info">
				    <div id="login-info-inner">
					<div id="logo">
					    <img src="${path}${dao:getProperty('application.company.logo')}" alt="Logo" width="190px"/>
					</div>
				    </div>
				</div>                                      
				<div id="login">
				    <div id="login-inner">
					<div id="error" style="color: red;font-weight: bolder;padding-left: 20px;padding-top: 30px">
					    <html:errors/>
					    <c:if test="${not empty loginForm.failureMessage}">
						<c:out value="${loginForm.failureMessage}"/>
					    </c:if>
					</div>
					<div id="login-field">
					    <div class="field-div">
						<span class="field-name"> User Name </span>
						<span class="input-field"><html:text property="userName" styleId="userName"/></span>
					    </div>
					    <div class="field-div">
						<span class="field-name">Password </span>
						<span class="input-field"><html:password property="password" styleId="password"/></span>
					    </div>
					    <div class="but-div">
						<div class="button-left">
						    <span class="button-right">
							<html:submit value="Login" styleClass="but-style" style="outline: none"/>
						    </span>
						</div>
						<div class="button-left">
						    <span class="button-right">
							<html:reset value="Cancel" styleClass="but-style"/>
						    </span>
						</div>
					    </div>
					</div>
				    </div>
				</div>
			    </div>
			</div>
			<div id="light">
			    <img src="${path}/images/login/light.jpg" alt="Light"/>
			</div>
		    </div>
                    <html:hidden property="action"/>
                    <html:hidden property="loginAgain"/>
                </html:form>
                <script type="text/javascript">
                    $(document).ready(function(){
                       $("#loginForm").submit(function(){
                           document.loginForm.action.value="Login";
                       });
                    });
                    document.loginForm.setAttribute("autocomplete", "off");
                    $("#userName").focus();
                </script>
                <c:if test="${not empty loginForm.warningMessage}">
                    <script type="text/javascript">
                        function loginAgain(){
                            document.loginForm.loginAgain.value=true;
                            document.loginForm.action.value="Login";
                            document.loginForm.submit();
                        }
                        if(confirm("You already logged on. Do you want to end the existing session?")){
                            loginAgain();
                        }
                    </script>
                </c:if>
            </c:otherwise>
        </c:choose>
    </body>
    <c:if test="${empty loginuser}">
        <script type="text/javascript">
            if(window.parent && window.parent.document.getElementById("homeFrame")){
                window.parent.location="${path}/jsps/login.jsp";
            }else if(window.parent.parent && window.parent.parent.document.getElementById("homeFrame")){
                window.parent.parent.location="${path}/jsps/login.jsp";
            }else if(window.parent.parent.parent && window.parent.parent.parent.document.getElementById("homeFrame")){
                window.parent.parent.parent.location="${path}/jsps/login.jsp";
            }
        </script>
    </c:if>
</html>

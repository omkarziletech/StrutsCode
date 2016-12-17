<%--
    Document   : home
    Created on : Jul 19, 2010, 12:32:07 PM
    Author     : Lakshmi Naryanan
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://cong.logiwareinc.com/tagutils" prefix="tagutils"%>
 <link rel="stylesheet" type="text/css" href="css/userform.css" />
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta name="keywords" content=""/>
        <meta name="description" content=" "/>
        <meta name="author" content="LogiwareInc."/>
        <title>Logiware</title>
        <%@include file="includes/jspVariables.jsp"%>
        <%@include file="includes/baseResources.jsp"%>
        <%@include file="includes/resources.jsp"%>
        <link type="text/css" href="${path}/css/default/style.css" rel="stylesheet"></link>
        <link type="text/css" href="${path}/css/layout/home.css" rel="stylesheet"></link>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"></link>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/css/common.css" />
        <%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
        <style>
            #GB_overlay {
                z-index: 32000;
            }
            #GB_window {
                z-index: 32500;
            }
        </style>
        <script type="text/javascript">
            function showMask() {
                var mask = document.getElementById("mask");
                mask.style.display = "block";
                mask.style.opacity = 0.5;
                mask.style.filter = "alpha(opacity=" + 50 + ")";
            }
            function hideMask() {
                var mask = document.getElementById("mask");
                mask.style.display = "none";
            }
            function showProgressBar() {
                showMask();
                document.getElementById('progressBar').style.display = "block";
            }
            function hideProgressBar() {
                hideMask();
                document.getElementById('progressBar').style.display = "none";
            }

            function changeHashOnLoad() {
                window.location.href += "#";
                setTimeout("changeHashAgain()", "50");
            }

            function changeHashAgain() {
                window.location.href += "1";
            }

            var storedHash = window.location.hash;
            window.setInterval(function () {
                if (window.location.hash != storedHash) {
                    window.location.hash = storedHash;
                }
            }, 50);
            function changeFramSize() {
                jQuery("ul.htabs li").find("a").each(function () {
                var index = jQuery(this).attr("tabindex");
                var height =jQuery(document).height() - jQuery("#header-container").height() - jQuery("#footer-container").height() - 110;
                jQuery("#tab" + index).height(height);
                });
            }
        </script>
    </head>

    <body>
        <%@include file="preloader.jsp"%>
        <div id="ConfirmYesOrNo" class="alert" style="display:none">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="ConfirmNavigateOk()"/>
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="ConfirmNavigateCancel()"/>
            </form>
        </div>
        <div id="mask"></div>
        <div id="progressBar" class="progressBar" style="position: absolute;left:40% ;top: 40%;right: 40%;bottom: 60%;display: none;">
            <p class="progressBarHeader"><b style="width: 100%;padding-left: 40px;">Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif"/>
            </div>
        </div>
        <html:form action="/home" method="post" name="homeForm" styleId="homeForm" type="com.logiware.form.HomeForm" scope="request">
            <div id="main-container">
                <div id="header-container">
                    <div id="logo">
                        <a href="javascript:showHome()"><img alt="" src="${path}${dao:getProperty('application.company.logo')}"/></a>
                    </div><!-- Logo part ends here -->
                    <div id="menu">
                        <ul>
                            <li class="firstnode"><a href="javascript:showHome()">Home</a></li>
                                <%--<li><a href="javascript: void(0)" onclick="GB_showFullScreen('Dashboard','${path}/dashboard.do')">Dashboard</a></li>--%>
                                <c:forEach items="${homeForm.menus}" var="menu">
                                    <c:choose>
                                        <c:when test="${homeForm.selectedMenu==menu.key}">
                                        <li class="selected">
                                            <a href="javascript: void(0)"><c:out value="${menu.key}"/></a>
                                            <ul>
                                                <c:forEach var="subMenu" items="${menu.value}">
                                                    <c:choose>
                                                        <c:when test="${tagutils:instanceOf(subMenu.value,'java.util.Map')}">
                                                            <li>
                                                                <a href="javascript: void(0)" class="sub-menu-ind"><c:out value="${subMenu.key}"/></a>
                                                                <ul>
                                                                    <c:forEach var="secondMenu" items="${subMenu.value}">
                                                                        <li><a href="javascript:showChild(${secondMenu.key},'${menu.key}')"><c:out value="${secondMenu.value}"/></a></li>
                                                                        </c:forEach>
                                                                </ul>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li><a href="javascript:showChild(${subMenu.key},'${menu.key}')"><c:out value="${subMenu.value}"/></a></li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                            </ul>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li>
                                            <a href="javascript: void(0)"><c:out value="${menu.key}"/></a>
                                            <ul>
                                                <c:forEach var="subMenu" items="${menu.value}">
                                                    <c:choose>
                                                        <c:when test="${tagutils:instanceOf(subMenu.value,'java.util.Map')}">
                                                            <li>
                                                                <a href="javascript: void(0)" class="sub-menu-ind"><c:out value="${subMenu.key}"/></a>
                                                                <ul>
                                                                    <c:forEach var="secondMenu" items="${subMenu.value}">
                                                                        <li><a href="javascript:showChild(${secondMenu.key},'${menu.key}')"><c:out value="${secondMenu.value}"/></a></li>
                                                                        </c:forEach>
                                                                </ul>
                                                            </li>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <li><a href="javascript:showChild(${subMenu.key},'${menu.key}')"><c:out value="${subMenu.value}"/></a></li>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                            </ul>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </ul>
                    </div><!-- Menu part ends here -->
                    <div id="login-info">
                        <div id="server-info">
                            Terminal : <span id="terminalinfo"><c:out value="${homeForm.terminalInfo}"/></span>
                        </div>
                        <div id="user-info">
                            Welcome <span id="username"> <c:out value="${homeForm.userName}"/> </span>  (<span id="rolename"><c:out value="${homeForm.roleName}"/></span>)
                        </div>
                        <div id="logout">
                            <a href="javascript:showLightBox('Help','${path}/servlet/FileViewerServlet?fileName=${dao:getProperty('application.help')}')">Help</a>&nbsp;&nbsp;&nbsp;
                            <a href="javascript:showLightBox('User Manual','${path}/servlet/FileViewerServlet?fileName=${dao:getProperty('application.usermanual')}')">User manual</a>&nbsp;&nbsp;&nbsp;
                            <a href="javascript:this.disabled=true;logout()">Logout</a>
                        </div>
                    </div><!-- Login info part ends here -->
                </div><!-- Header part ends here -->              
              
                <a href="searchFormPage.do?action=newForm" class="link" style="background-color: #f44336; color: white;  padding: 14px 25px;
    text-align: center;
    text-decoration: none;
    display: inline-block;">Click Me </a>
    
                <br></br>
                
                 <a href="searchFormPage.do?action=newEmployeePage" class="link" style="background-color: #f44336; color: white;  padding: 14px 25px;
    text-align: center;
    text-decoration: none;
    display: inline-block;">Go to Employee Page </a>
                
               
                
                <div id="body-container">
                    <c:choose>
                        <c:when test="${not empty homeForm.tabs}">
                            <div id="container">
                                <ul class="htabs" onclick="changeFramSize()">
                                    <c:forEach items="${homeForm.tabs}" var="tabItem">
                                        <li><a href="javascript: void(0)" tabindex="${tabItem.key}">${tabItem.value.itemDesc}</a></li>
                                        </c:forEach>
                                </ul>
                                <c:forEach items="${homeForm.tabs}" var="tabItem">
                                    <div class='pane'>
                                        <iframe frameborder="0" id="tab${tabItem.key}" title="${tabItem.value.itemDesc}" name="${tabItem.key}" src='' width='100%' height='0' onload="closePreloading()"></iframe>
                                        <input type="hidden" id="src${tabItem.key}" value="${tabItem.value.src}"/>
                                    </div>
                                </c:forEach>
                                <c:if test="${empty selectedTab}">
                                    <c:set var="selectedTab" value="0"/>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="process-container">
                                <div id="process-container-inner">
                                    <div id="process-curve">
                                        <div id="process-curve-top-home"></div>
                                        <div id="process-curve-mid">
                                            <div class="info-box2">
                                                <c:if test="${roleDuty.showFollowUpTasks}">
                                                    <div>
                                                        <table width="98%" cellpadding="0" cellspacing="1">
                                                            <tr>
                                                                <td class="info-box-title">Follow Up Tasks</td>
                                                                <td style="color:DodgerBlue;font-weight: 700;">
                                                                    <html:radio property="showNotes" styleId="showNotes" value="last3days"/>Last 3 days
                                                                </td>
                                                                <td style="color:DodgerBlue;font-weight: 700;">
                                                                    <html:radio property="showNotes" styleId="showNotes" value="next3days"/>Next 3 days
                                                                </td>
                                                                <td style="color:DodgerBlue;font-weight: 700;">
                                                                    <html:radio property="showNotes" styleId="showNotes" value="all"/>All
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </div>
                                                    <div id="followUpTasks">
                                                        <%@include file="/jsps/home/followUpTasks.jsp"%>
                                                    </div>
                                                </c:if>
                                            </div><!-- news and event ends here -->
                                        </div>
                                        <div id="process-curve-bottom"> </div>
                                    </div> <!-- Process curve ends here -->
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="footer-container">
                    <div id="version-info">Version : ${dao:getProperty('application.version')} </div>
                    <span class="copy"> 
                        ${dao:getProperty('application.fclBl.print.companyFullName')} - © Copyright  ${currentYear} : Logiware Inc
                    </span>
                    <div id="power-info"></div>
                </div>
            </div> <!-- Main container part ends here -->
            <html:hidden property="action" styleId="action" value=""/>
            <html:hidden property="itemId" styleId="itemId"/>
            <html:hidden property="selectedMenu" styleId="selectedMenu"/>
            <html:hidden property="tabDesc" styleId="tabDesc" value=""/>
            <html:hidden property="recordId" styleId="recordId"/>
            <html:hidden property="moduleId" styleId="moduleId"/>
            <html:hidden property="customerNumber" styleId="customerNumber"/>
            <html:hidden property="followUpTaskId" styleId="followUpTaskId"/>
            <input type="hidden" id="selectedTab" value="${selectedTab}"/>

            <!-- these hidden field used for only LCL EXPORT-->
            <html:hidden property="pickVoyId" styleId="pickVoyId"/>
            <html:hidden property="detailId" styleId="detailId"/>
            <html:hidden property="unitSsId" styleId="unitSsId"/>
            <html:hidden property="toScreenName" styleId="toScreenName"/>
            <html:hidden property="consolidateId" styleId="consolidateId"/>
            <html:hidden property="fromScreen" styleId="fromScreen"/>
            <html:hidden property="filterByChanges" styleId="filterByChanges"/>
            <html:hidden property="inTransitDr" styleId="inTransitDr"/>
            <html:hidden property="callBack" styleId="callBack"/>
            <!-- These hidden for Follow Up notes in home screen -->
            <html:hidden property="fileNumber" styleId="fileNumber"/>
            <html:hidden property="userId" styleId="userId"/>
            <html:hidden property="lclFileType" styleId="lclFileType"/>
            <html:hidden property="screenName" styleId="screenName"/>
            <html:hidden property="module" styleId="module"/>
            <html:hidden property="reference" styleId="reference"/>
            <html:hidden property="accessMode" styleId="accessMode"/>
            <input type="hidden" id="followupFlag" value="${followupFlag}"/>
        </html:form>
    </body>
    <script  type="text/javascript" src="${path}/js/tab/tab.js"></script>
    <script type="text/javascript" src="${path}/js/Hashtable.js"></script>
    <script type="text/javascript" src="${path}/js/home.js"></script>
</html>
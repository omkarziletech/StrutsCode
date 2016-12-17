<%-- 
    Document   : fileSearch1
    Created on : Nov 7, 2011, 7:12:42 PM
    Author     : Thamizh
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://cong.logiwareinc.com/tagutils" prefix="tagutils"%>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>

        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <link rel="stylesheet" type="text/css" href="/logisoft/css/default/style.css" title="default" ></link>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
        <meta name="keywords" content=""/>
        <meta name="description" content=" "/>
        <meta name="author" content="LogiwareInc."/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <title>Logiware</title>
        <style type="text/css">
            .pane{
                width: 100%;
                float: left;
            }
            a{
                outline : none;
            }

            .htabs{
                background: url(${path}/images/second-menu.gif) repeat-x;
                float: left;
                width: 100%;
                height: 27px;
                margin: 0;
            }

            .htabs li{
                float: left;
                margin: 0;
                height: 25px;
                line-height: 27px;
                list-style: none;
                padding: 0;
                font-size: 13px;
                color: #000;
                text-align: center;
                border-right:1px solid #568ea1;
            }
            .htabs li a{
                color:#000;
                text-decoration: none;
                padding:0 10px;
            }
            .htabs li:hover{
                background:url(${path}/images/second-menu-selected.gif) repeat-x;
            }

            .htabs li.selected{
                height: 25px;
                background:url(${path}/images/second-menu-selected.gif) repeat-x;
            }

            .htabs li.selected a{
                color:#000;
            }

            body,img,ul,li,table{
                margin:0;
                padding:0;
            }

            img{
                border:0;
            }

            body{
                background:#fefeff url(${path}/images/bageback.gif) repeat-x;
                font-family:Tahoma ;
                font-size:12px;
                height: 100%;
            }

            #main-container{
                width:100%;
                height:100%;
            }

            #header-container{
                float:left;
                width:100%;
                background:url(${path}/images/menuback.png) repeat-x;
                height:76px;
                margin:0 0 10px 0;
                top: 0;
            }


            #logo{
                float:left;
		background:url(${path}/images/logo/logo-back.png) repeat-x;
            }

            #menu{
                float:left;
            }


            #menu ul{
                float:left;
                height:32px;
                list-style:none;
                margin:27px 0 0 0;
                color:#a29f9f;
                border-right: 1px solid #fff;
                border-left: 1px solid #d3d3d3;
            }

            #menu ul a{
                color:#000;
                text-decoration:none;
                height:32px;
                width:76px;
                float:left;
                padding:0 10px;
                font-size:11px;
                font-weight: bold;
                white-space:nowrap;
                text-align:center;
            }

            #menu ul a:hover{
                color:#fff;
            }

            #menu ul  li{
                float:left;
                border-left: 1px solid #fff;
                border-right: 1px solid #d3d3d3;
                height:32px;
                line-height:32px;
            }

            #menu ul  li:hover a {
                background-color:#05b1eb;
                color:#fff;
            }
            #menu ul  li.selected{
                background:url(${path}/images/selected-menu.jpg);
                color:#fff;
            }

            #menu ul  li:hover ul {
                display:block;
            }

            #curve-mid-new{
                width:98%;
                padding:1%;
                color:#dfdfdf;
                float:left;
            }
            /********************************/


            #menu ul  li ul {
                width:200px;
                position:absolute;
                border:none;
                display:none;
                margin-left:-1px;
                z-index:1000;
            }
            *+html #menu ul  li ul { /*IE7 hake */
                                     clear:both;
                                     margin-left: -98px;
            }

            #menu ul  li ul  li{
                float:left;
                width:200px;
                height:30px;
                border-left: 1px solid #fff;
                border-right: 1px solid #fff;
                border-top: 1px solid #ebf8ff;
                border-bottom: 1px solid  #036a8d;
                margin:0;
                background-color:#05b1eb;
                color:#fff;
            }

            #menu ul  li ul  li a{
                padding:0 10px;
                float:left;
                width:180px;
                height:30px;
                margin:0;
                background-color:#05b1eb;
                color:#fff;
            }
            #menu ul  li ul li a:hover {
                background-color:darkgray;
                color:#000;
            }



            #menu ul li ul li ul {
                width:200px;
                display:none !important;
                position:absolute;
                margin:0 0 0 200px;

            }
            *+html #menu ul li ul li ul { /*IE7 hake */
                                          clear:both;
                                          margin-left:0;
                                          position:absolute;
            }
            #menu ul li ul li ul li {
                background-color:#05b1eb !important;
            }

            #menu ul li ul li:hover ul {
                display:block !important;
                background-color: transparent;
            }
            #menu ul li a.sub-menu-ind{
                background-image: url(${path}/images/sub-menu-bul.gif);
                background-repeat:no-repeat;
                background-position:180px center;
            }

            #menu ul li:hover a.sub-menu-ind{
                background-image: url(${path}/images/sub-menu-bul.gif);
                background-repeat:no-repeat;
                background-position:180px center;
            }
            #login-info{
                float:right;
                background:url(${path}/images/login-ingo-back.png) no-repeat left;
                width:250px;
                height:76px;
                color:#fff;
                padding:0 10px 0 0;
            }

            #server-info{
                float:left;
                width:100%;
                text-align:right;
                line-height:33px;
            }

            #user-info{
                float:left;
                color:#000;
                width:100%;
                text-align:right;
                line-height:20px;
            }

            #logout{
                float:left;
                width:100%;
                text-align:right;
                line-height:23px;
            }

            #logout a{
                color:#fff;
            }
            /******************************************************/
            #process-container{
                width:1001px;
                margin:0 auto;
            }
            #container{
                border:1px solid #137587;
                float:left;
                margin:0 1%;
                width:97%;
            }

            #process-container-inner{
                width:100%;
                float:left;
                position: relative;
            }

            #process-curve{
                width:100%;
                float:left;
            }

            #process-curve-top{
                background:url(${path}/images/body-curve-top.gif) no-repeat;
                width:100%;
                height:8px;
                float:left;
            }

            #process-curve-top-home{
                background:url(${path}/images/banner.png) no-repeat;
                width:100%;
                height:314px;
                float:left;
            }


            #process-curve-mid{
                width:999px;
                border-left:1px solid #c3c1c1;
                border-right:1px solid #c3c1c1;
                float:left;
                background:#fff;
            }

            #newsandevent{
                float:left;
                background:url(${path}/images/curve-back.gif) repeat-x;
                height:231px;
                width:480px;
                margin:0 0 0 15px;
            }

            #newsandevent-title{
                float:left;
                background:url(${path}/images/news-title.gif) no-repeat left;
                height:231px;
                width:37px;
            }

            #newsandevent-info{
                float:left;
                background:url(${path}/images/curve-right.gif) no-repeat right;
                height:231px;
                width:431px;
                padding:0 12px 0 0;
            }


            #todo-list{
                float:left;
                background:url(${path}/images/curve-back.gif) repeat-x;
                height:231px;
                width:480px;
                margin:0 0 0 10px;
            }

            #todo-list-title{
                float:left;
                background:url(${path}/images/news-title.gif) no-repeat left;
                height:231px;
                width:37px;
            }

            #todo-list-info{
                float:left;
                background:url(${path}/images/curve-right.gif) no-repeat right;
                height:231px;
                width:431px;
                padding:0 12px 0 0;
            }

            .infolist{
                width:100%;
                float:left;
            }

            .infolist ul{
                width:100%;
                float:left;
                list-style:none;
                margin:10px 0 0 0;
            }

            .infolist ul li{
                width:100%;
                float:left;
                margin: 0  0 20px  0;

            }

            .date-info{
                background:url(${path}/images/calender.gif) no-repeat center center;
                height:50px;
                width:30px;
                line-height:18px;
                text-align:center;
                float:left;
                font-family:verdana;
                font-size:10px;
                color:#959595;
            }

            .info-txt{
                float:left;
                width:380px;
                margin:10px 0 0 10px;
            }

            #process-curve-bottom{
                background:url(${path}/images/body-curve-bottom.gif) no-repeat;
                width:100%;
                height:8px;
                float:left;
            }



            /*****************************************************/

            #footer-container{
                float:left;
                width:100%;
                background:url(${path}/images/foot-back.gif) repeat-x;
                height:42px;
                line-height:42px;
                text-align:center;
                margin:10px 0 0 0;
                position: fixed;
                bottom: 0;
            }

            #version-info{
                float:left;
                width:255px;
                height:42px;
                background:url(${path}/images/foot-left.gif) no-repeat left;
                text-align:left;
                padding:0 0 0 30px;
            }

            .copy{
                text-align:center;
            }

            *+html .copy{ /*IE7 hake */
                          text-align:center;
                          float:left;
            }


            #power-info{
                float:right;
                width:280px;
                height:42px;
                background:url(${path}/images/foot-right.gif) no-repeat right;

            }

            /********************************/
            .empty{
                clear:both;
            }
            #mask {
                display: none;
                position: absolute;
                overflow: visible;
                vertical-align: super;
                left: 0px;
                top: 0px;
                width: 100%;
                height: 100%;
                background: #000;
                opacity: 0.5;
                -moz-opacity: 0.5;
                -khtml-opacity: 0.5;
            }
            #menu ul li ul li.sub-indicator{
                background :#05B1EB url(${path}/images/sum-menu-bul.gif) right center no-repeat !important;
                z-index: 1000;
            }
        </style>
        <script src="${path}/js/jquery/jquery.js" type="text/javascript" ></script>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript">
            function showMask() {
                var mask = document.getElementById("mask");
                mask.style.display = "block";
                mask.style.opacity = 0.5;
                mask.style.filter = "alpha(opacity="+50+")";
            }
            function hideMask() {
                var mask = document.getElementById("mask");
                mask.style.display = "none";
            }
            function showProgressBar(){
                showMask();
                document.getElementById('progressBar').style.display="block";
            }
            function hideProgressBar(){
                hideMask();
                document.getElementById('progressBar').style.display="none";
            }
            
        </script>
    </head>

    <body>
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
        <html:form action="/home" method="post" name="homeForm" type="com.logiware.form.HomeForm" scope="request">
            <div id="main-container">
                <div id="header-container">
                    <div id="logo">
                        <a href="javascript:showHome()"><img alt="" src="${path}<bean:message bundle="defaultSettings" key="application.company.logo"/>"/></a>
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
                            <a href="javascript:this.disabled=true;logout()">Logout</a>
                        </div>
                    </div><!-- Login info part ends here -->
                </div><!-- Header part ends here -->


                <c:choose>
                    <c:when test="${not empty homeForm.tabs}">
                        <div id="container">
                            <ul class="htabs">
                                <c:forEach items="${homeForm.tabs}" var="tabItem">
                                    <li><a href="javascript: void(0)" tabindex="${tabItem.key}">${tabItem.value.itemDesc}</a></li>
                                </c:forEach>
                            </ul>
                            <c:forEach items="${homeForm.tabs}" var="tabItem">
                                <div class='pane'>
                                    <iframe frameborder="0" id="tab${tabItem.key}" title="${tabItem.value.itemDesc}" name="${tabItem.key}" src='' width='100%' height='0' onload="hideProgressBar()"></iframe>
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

                                    <div id="process-curve-top-home"> </div>

                                    <div id="process-curve-mid">

                                        <div id="newsandevent">
                                            <div id="newsandevent-title">	</div>

                                            <div id="newsandevent-info">
                                                <div class="infolist">

                                                </div><!-- Info list ends here -->
                                            </div><!-- news and eventinfo ends here -->
                                        </div><!-- news and event ends here -->



                                        <div id="todo-list">
                                            <div id="todo-list-title">	</div>

                                            <div id="todo-list-info">
                                                <div class="infolist">

                                                </div><!-- Info list ends here -->
                                            </div><!-- todo-list info ends here -->
                                        </div><!-- todo-list ends here -->



                                    </div>

                                    <div id="process-curve-bottom"> </div>


                                </div> <!-- Process curve ends here -->

                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div id="footer-container">
                    <div id="version-info">Version : <bean:message bundle="defaultSettings" key="application.version"/> </div>
                    <span class="copy"> 
			<bean:message bundle="defaultSettings" key="application.fclBl.print.companyFullName"/> - © Copyright  ${currentYear} : Logiware Inc
		    </span>
                    <div id="power-info"></div>
                </div>
            </div> <!-- Main container part ends here -->
            <html:hidden property="action" value=""/>
            <html:hidden property="itemId"/>
            <html:hidden property="selectedMenu"/>
            <html:hidden property="tabDesc" value=""/>
            <html:hidden property="recordId"/>
            <html:hidden property="moduleId"/>
        </html:form>
    </body>
    <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
    <script type="text/javascript" src="${path}/js/Hashtable.js"></script>
    <script type="text/javascript">
        function showChild(itemId,selectedMenu){
	    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
	    if(undefined != index && document.getElementById("tab"+index)){
		var frameRef = document.getElementById("tab"+index).contentWindow;
		var tab = jQuery("#tab"+index).attr("title");
		if(undefined != tab && tab=="Trading Partner"){
                    if(frameRef.frames.length > 0){
                        if(frameRef.frames[0].name == 'geniframe'){
                            alert('Please Click Go Back');
                            return;
                            // for future use
			    <%--if(!(frameRef.frames("geniframe").catcher())){
				navigateAway(itemId,selectedMenu,"in Addresses tab");
			    }else{
				if(!(frameRef.frames("acciframe").catcher())){
				    navigateAway(itemId,selectedMenu,"in General Info tab");
				}else{
				    if(!(frameRef.frames("aciframe").catcher())){
					navigateAway(itemId,selectedMenu,"in AR Config tab");
				    }else{
					if(!(frameRef.frames("vendorframe").catcher())){
					    navigateAway(itemId,selectedMenu,"in AP Config tab");
					}else{
					    if(!(frameRef.frames("custiframe").catcher())){
						navigateAway(itemId,selectedMenu,"in Contact Config tab");
					    }else{
						showTabs(itemId,selectedMenu);
					    }
					}
				    }
				}
			    }--%>
                        }else{
			    showTabs(itemId,selectedMenu);
			}
                    }else{
                        showTabs(itemId,selectedMenu);
                    }
		}else if (typeof frameRef.catcher == 'function') {
		    if(undefined != tab && (tab=="FCL BL" || tab=="IMPORT FCL BL")){
                        frameRef.saveBlOnNavigate();
		    }
		    if(!(frameRef.catcher())){
                        navigateAway(itemId,selectedMenu,"");
		    }else{
                        showTabs(itemId,selectedMenu);
		    }
		}else{
                    showTabs(itemId,selectedMenu);
		}
	    }else{
                showTabs(itemId,selectedMenu);
            }
        }
        function navigateAway(id,menu,tabName){
            itemId=id;
            selectedMenu =menu;
            confirmYesOrNo('One or more form values on this page have changed '+tabName+'. Are you sure you want to navigate away from this page',"Navigate");
        }
        function showTabs(id,menu){
            document.homeForm.action.value="showTabs";
            document.homeForm.itemId.value=id;
            document.homeForm.selectedMenu.value=menu;
            document.homeForm.submit();
        }
        function confirmNavigateFunction(id1,id2){
            if( id2=='no'){
                //no need do anything
            }else if(id1=='Navigate' && id2=='yes'){
                document.homeForm.action.value="showTabs";
                document.homeForm.itemId.value=itemId;
                document.homeForm.selectedMenu.value=selectedMenu;
                document.homeForm.submit();
            }
        }
        function changeChilds(path,recordId,moduleId){
            showProgressBar();
            var startsWith = path.substring(0, 1);
            if(startsWith.indexOf("&")<0){
                startsWith = "&";
            }else{
                startsWith = ""
            }
            window.location = "${path}/home.do?action=changeTabs&itemId="+document.homeForm.itemId.value+"&selectedMenu="+document.homeForm.selectedMenu.value+startsWith+path+"&recordId="+recordId+"&moduleId="+moduleId;
        }
	function changeChildsFromDisputedBl(path,recordId,moduleId,itemId){
            showProgressBar();
            var startsWith = path.substring(0, 1);
            if(startsWith.indexOf("&")<0){
                startsWith = "&";
            }else{
                startsWith = ""
            }
            window.location = "${path}/home.do?action=changeTabs&itemId="+itemId+"&selectedMenu=Exports"+startsWith+path+"&recordId="+recordId+"&moduleId="+moduleId;
        }
        function changeChildsByDesc(path){
            showProgressBar();
            var startsWith = path.substring(0, 1);
            if(startsWith.indexOf("&")<0){
                startsWith = "&";
            }else{
                startsWith = ""
            }
            window.location = "${path}/home.do?action=changeTabsByTabName&itemId="+document.homeForm.itemId.value+"&selectedMenu="+document.homeForm.selectedMenu.value+startsWith+path;
        }
        function showHome(){
            document.homeForm.action.value="showHome";
            document.homeForm.itemId.value="";
            document.homeForm.selectedMenu.value="";
            document.homeForm.submit();
        }
        function gotoTradingPartner(accountNumber,selectedTab){
            window.location = "${path}/home.do?action=gotoTradingPartner&accountNumber="+accountNumber+"&selectedTab="+selectedTab;
        }
        function logout(){
            window.location = "${path}/logout.do";
        }
        function showGreyBox(caption,path){
            var height = jQuery(document).height()-50;
            var width = jQuery(document).width()-100;
            var version = jQuery.browser.version;
            if(jQuery.browser.msie && parseFloat(version)>=9.0){
                window.open(path,"mywindow","menubar=1,resizable=1,width="+width+",height="+height);
            }else{
                GB_show(caption,path,height,width);
            }
        }
        var links = document.getElementsByTagName("a");
        for(var i =0;i<links.length;i++){
            links[i].onmouseover = function(){
                window.status='';
                return true;
            }
        }
        var quoteCache = new Hashtable();
        var bookingCache = new Hashtable();
        var blCache = new Hashtable();
        var dataStorage = new Hashtable();
        function makeFormBorderless(form) {
            var element;
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if(element.type == "text" || element.type == "textarea" || element.type=="select-one"){
                    element.style.border=0;
                    if(element.type == "select-one"){
                        element.disabled = true;
                    }
		    element.style.backgroundColor="#CCEBFF";
                    element.readOnly = true;
                    element.tabIndex = -1;
                    element.className="textlabelsBoldForTextBox";
                }else if( element.type=="checkbox" || element.type=="radio"){
                    element.style.border=0;
                    element.disabled = true;
                    element.className="textlabelsBoldForTextBox";
                }else if(element.type == "button"){
                    if(element.value=="Save"){
                        element.style.visibility="hidden";
                    }
                }
            }
            return false;
        }

        // Create a 'get' query string with the data from a given form
        function gatherFormData(form,cacheFor) {
            var element;
            // For each form element, extract the name and value
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if (element.type == "text" || element.type == "password" || element.type == "textarea" || element.type == "hidden"){
                    dataStorage.put(element.name,escape(element.value));
                }else if (element.type.indexOf("select") != -1) {
                    for (var j = 0; j < element.options.length; j++) {
                        if (element.options[j].selected == true) {
                            dataStorage.put(element.name,element.options[element.selectedIndex].value);
                        }
                    }
                } else if (element.type == "checkbox" && element.checked){
                    dataStorage.put(element.name,element.value);
                } else if (element.type == "radio" && element.checked == true){
                    dataStorage.put(element.name,element.value);
                }
            }

            if(cacheFor=='QUOTE'){
                quoteCache = dataStorage;
            }else if(cacheFor=='BOOKING'){
                bookingCache = dataStorage;
            }else if(cacheFor=='BL'){
                blCache = dataStorage;
            }
            return false;
        }

        function restoreFormData(form,cacheFor) {
            var element;
            if(cacheFor=='QUOTE'){
                dataStorage = quoteCache;
            }else if(cacheFor=='BOOKING'){
                dataStorage = bookingCache;
            }else if(cacheFor=='BL'){
                dataStorage = blCache;
            }
            if(dataStorage.size()==0){
                return false;
            }
            // For each form element, extract the name and value
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if(unescape(dataStorage.get(element.name))!="undefined"){
                    if (element.type == "text" || element.type == "password" || element.type == "textarea" || element.type == "hidden"){
                        element.value =  unescape(dataStorage.get(element.name));
                    }else if (element.type.indexOf("select") != -1) {
                        for (var j = 0; j < element.options.length; j++) {
                            if (element.options[j].value == unescape(dataStorage.get(element.name))) {
                                element.selectedIndex=j;
                            }
                        }
                    } else if (element.type == "checkbox" && element.checked){
                        element.value = unescape(dataStorage.get(element.name));
                    } else if (element.type == "radio" && element.checked == true){
                        element.value = unescape(dataStorage.get(element.name));
                    }
                }
            }
            return false;
        }

        function disableFieldsWhileLocking(form) {
            var element;
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if(element.type == "text" || element.type == "textarea" || element.type=="select-one"){
                    element.style.border=0;
                    if(element.type == "select-one"){
                        element.disabled = true;
                    }
                    element.readOnly = true;
                    element.tabIndex = -1;
                    if(element.type == "text" || element.type == "textarea"){
                        element.style.backgroundColor="#CCEBFF";
                        if(element.id == "SSBooking"||element.id == "vessel"||element.id == "ssVoy"||element.id == "txtcal313"||element.id == "txtcal71"||element.id == "txtcal22"
			    ||element.id =="txtcal5" ||element.id =="fowardername" ||element.id =="txtcal13"||element.id == "portOfDischarge"||element.id == "originTerminal" ||element.id == "issuingTerminal"){
			    element.style.borderLeft = "red 2px solid";
                        }
                    }else{
                        element.className="textlabelsBoldForTextBox";
                    }
                }else if(element.type=="checkbox" || element.type=="radio"){
                    element.style.border=0;
                    element.disabled = true;
                    element.className="textlabelsBoldForTextBox";
                }else if(element.type == "button"){
                    if(element.value!="Go Back"){
                        element.style.visibility="hidden";
                    }
                }
            }
            return false;
        }
        jQuery(document).ready(function(){
            var initial = Number("${selectedTab}");
            jQuery("ul.htabs").tabs("> .pane",{effect: 'fade',current:'selected',initialIndex:initial,onClick:function(){
                    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
                    var src = jQuery("#src"+index).val();
                    if(jQuery("#tab"+index).attr("src")==''){
                        jQuery("#tab"+index).attr("src", src);
                        showProgressBar();
                    }
                    jQuery("#tab"+index).height(jQuery(document).height()-jQuery("#header-container").height()-jQuery("#footer-container").height()-40);
                }
            });

        });
        function changeHeight(){
            jQuery("ul.htabs li").find("a").each(function(){
                var index = jQuery(this).attr("tabindex");
                jQuery("#tab"+index).height(jQuery(document).height()-jQuery("#header-container").height()-jQuery("#footer-container").height()-40);
            });
        }

        function addNewCustomer(fromField,accountName,accountNo){
	    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
	    var frameRef = document.frames("tab"+index);
	    if(fromField != undefined && fromField!=null && fromField!='' && fromField=='shipperName'){
		frameRef.document.EditBookingsForm.shipperName.value=accountName;
		frameRef.getShipperInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='fowardername'){
		frameRef.document.EditBookingsForm.fowardername.value=accountName;
		frameRef.getForwarderInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='consigneename'){
		frameRef.document.EditBookingsForm.consigneename.value=accountName;
		frameRef.getConsigneeInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='spottingAccountName'){
		frameRef.document.EditBookingsForm.spottingAccountName.value=accountName;
		frameRef.getSpottingInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='truckerName'){
		frameRef.document.EditBookingsForm.truckerName.value=accountName;
		frameRef.getTruckerInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='shipperNameBL'){
		frameRef.document.fclBillLaddingform.accountName.value=accountName;
		frameRef.document.getElementById("shipper").value = accountNo;
		frameRef.document.getElementById("houseShipperAddress").value = "";
		frameRef.getHouseShipperInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='consigneeNameBL'){
		frameRef.document.fclBillLaddingform.consigneeName.value=accountName;
		frameRef.document.getElementById("consignee").value = accountNo;
		frameRef.document.getElementById("streamShipConsignee").value = "";
		frameRef.getHouseConsigneeInForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='forwardingAgentNameBL'){
		frameRef.document.fclBillLaddingform.forwardingAgentName.value=accountName;
		frameRef.document.getElementById("forwardingAgent1").value = accountNo;
		frameRef.document.getElementById("forwardingAgentno").value = "";
		frameRef.getForwardingInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='notifyPartyNameBL'){
		frameRef.document.fclBillLaddingform.notifyPartyName.value=accountName;
		frameRef.document.getElementById("notifyParty").value = accountNo;
		frameRef.document.getElementById("streamshipNotifyParty").value = "";
		frameRef.getHouseNotifyPartyInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='shipperNameMasterBL'){
		frameRef.document.fclBillLaddingform.houseName.value=accountName;
		frameRef.document.fclBillLaddingform.houseShipper.value=accountNo;
		frameRef.document.fclBillLaddingform.houseShipper1.value = "";
		frameRef.getMasterShipperInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='houseConsigneeNameBL'){
		frameRef.document.fclBillLaddingform.houseConsigneeName.value=accountName;
		frameRef.document.getElementById("houseConsignee").value = accountNo;
		frameRef.document.getElementById("houseConsignee1").value = "";
		frameRef.getMasterConsigneeInfoForPopUp();
	    }else if(fromField != undefined && fromField!=null && fromField!='' && fromField=='houseNotifyPartyNameBL'){
		frameRef.document.fclBillLaddingform.houseNotifyPartyName.value=accountName;
		frameRef.document.getElementById("houseNotifyParty").value = accountNo;
		frameRef.document.getElementById("houseNotifyPartyaddress").value = "";
		frameRef.getMasterNotifyInfoForPopUp();
	    }
	    GB_hide();
        }
    </script>
</html>

<%@page import="com.gp.cong.struts.LoadLogisoftProperties"%>
<%@page import="com.gp.cong.logisoft.domain.RoleDuty"%>
<%@ page language="java" import="java.util.*"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@page import="com.gp.cong.logisoft.beans.LoginUser"%>
<%@page import="com.gp.cong.logisoft.domain.RoleDuty"%>
<%@page import="java.util.Hashtable"%>
<%@include file="init.jsp" %>

<%!    
    String mandatoryFieldForHazMat = "Mandatory Fields Needed<br>1)UN Number<BR>2)Proper Shipping Name<BR>3)Outer Packing Pieces<BR>4)Outer Pack Composition<BR>5)Outer Packaging Type<BR>6)IMO ClassCode(Primary)<BR>7)Gross Weight";
%>
<%
            RoleDuty roleDuty = (RoleDuty) session.getAttribute("roleDuty");
            if (null == roleDuty) {
                roleDuty = new RoleDuty();
            }
%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%
            Integer pageSize = new Integer(LoadLogisoftProperties.getProperty("pageSize"));
            request.setAttribute("pageSize", pageSize);
%>

<div id="bubble_tooltip" style="display: none;">
    <div class="bubble_top"><span></span></div>
    <div class="bubble_middle"><span id="bubble_tooltip_content"></span></div>
    <div class="bubble_bottom"></div>
</div>
<div id="bubble_tooltip_small" style="display: none;">
    <div class="bubble_small_top"><span></span></div>
    <div class="bubble_small_middle"><span id="bubble_tooltip_content_small"></span></div>
    <div class="bubble_small_bottom"></div>
</div>
<div id="bubble_tooltip_ForTop" style="display: none;">
    <div class="bubble_top_ForTop"><span></span></div>
    <div class="bubble_middle_ForTop"><span id="bubble_tooltip_content_ForTop"></span></div>
    <div class="bubble_bottom_ForTop"></div>
</div>

<script type="text/javascript">
    var rootPath = "${pageContext.request.contextPath}";
    /**
     * @description - Checking Session Expiration for Dwr calls
     *                It should be fired only if "dwr.engine.setTextHtmlHandler(dwrSessionError)"
     *                added in the jsp page which is using Dwr calls
     * @author - Lakshminarayanan V
     */
    function dwrSessionError(){
        alert("Your session has expired, please login again.");
        window.parent.recordId = null;
        window.parent.moduleId = null;
        window.location = rootPath+"/jsps/login.jsp";
    }

    /**
     * @description - change select box to text box
     *                and virtualy show select box as readonly
     * @author - Lakshminarayanan V
     */
    function changeSelectBoxOnViewMode(){
        var tags = document.getElementsByTagName('select');
        var oldObjects = new Array();
        var newObjects = new Array();
        var hiddenObjects = new Array();
        for(i=0;i<tags.length;i++){
            oldObject = tags[i];
            if(oldObject.disabled && oldObject.id != 'specialEqpmt'&& oldObject.id != 'specialEqpmtUnit' && oldObject.id !='routedAgentCheck'){
                var hiddenObject = document.createElement('input');
                hiddenObject.type="hidden";
                hiddenObject.value = oldObject[oldObject.selectedIndex].value;
                if(oldObject.name) hiddenObject.name = oldObject.name;
                if(oldObject.id) hiddenObject.id = oldObject.id;
                var newObject = document.createElement('input');
                newObject.value = oldObject[oldObject.selectedIndex].text;
                newObject.readOnly = true;
                newObject.tabIndex = -1;
                newObject.style.width = oldObject.style.width;
                if(oldObject.name) newObject.name = oldObject.name+"_readonly";
                if(oldObject.id) newObject.id = oldObject.id+"_readonly";
                newObject.className = "textlabelsBoldForTextBoxDisabledLook";
                oldObjects[oldObjects.length]=oldObject;
                newObjects[newObjects.length]=newObject;
                hiddenObjects[hiddenObjects.length]=hiddenObject;
            }
        }
        for(i=0;i<oldObjects.length;i++){
            oldObjects[i].parentNode.appendChild(hiddenObjects[i]);
            oldObjects[i].parentNode.replaceChild(newObjects[i],oldObjects[i]);
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if ( imgs[k].id == "expandIcon" || imgs[k].id == "collapseIcon") {
                imgs[k].style.visibility = "visible";
            }
        }
    }

    /**
     * @description - Prevent BackSpace
     * @author - Lakshminarayanan V
     */
    var isNS = (navigator.appName == "Netscape") ? 1 : 0;
    var EnableRightClick = 0;
    if(isNS)
        document.captureEvents(Event.MOUSEDOWN||Event.MOUSEUP);
    function keydownhandler(){
        var type=event.srcElement.type;
        var keyCode=event.keyCode;
        var target=event.srcElement;
        var readOnly=target.getAttribute('readonly');
        return (keyCode != 8 && keyCode != 13)
            || (type == 'text' && keyCode != 13 && readOnly == false )
            || (type == 'password') || (type == 'textarea' && readOnly == false )
            || (type == 'submit' && keyCode == 13) || keyCode==116;
    }
    function mischandler(){
        if(EnableRightClick==1){
            return true;
        }
        else {
            return false;
        }
    }
    function mousehandler(e){
        if(EnableRightClick==1){
            return true;
        }
        var myevent = (isNS) ? e : event;
        var eventbutton = (isNS) ? myevent.which : myevent.button;
        if((eventbutton==2)||(eventbutton==3)) return false;
    }
    function keyhandler(e) {
        var myevent = (isNS) ? e : window.event;
        if (myevent.keyCode==96)
            EnableRightClick = 1;
        return;
    }
//    document.oncontextmenu = mischandler;
//    document.onkeypress = keyhandler;
//    document.onmousedown = mousehandler;
//    document.onmouseup = mousehandler;
//  document.onkeydown = keydownhandler;
    window.onresize = function(){
        if(document.getElementById("header-container")){
            changeHeight();
        }else if(window.parent.document.getElementById("header-container")){
            window.parent.changeHeight();
        }else if(window.parent.parent.document.getElementById("header-container")){
            window.parent.parent.changeHeight();
        }
    }
    /**
     * @description - Prevent Browser Back Button
     */
    function preventBack(){
       window.history.forward();
    }
    setTimeout("preventBack()", 10);
    window.onunload=function(){
       null
    };
</script>

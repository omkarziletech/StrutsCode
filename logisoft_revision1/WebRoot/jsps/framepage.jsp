<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/resources.jsp" %>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 

<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Logiware</title>

        <script>
            var disable;
            function call(ev){
                document.frames["tabframe"].document.getElementById('newProgressBar1').style.display = 'none';
                document.getElementById('newProgressBarFrame').style.display="block";
                disable=ev;
                setTimeout("autoDisable()",20500);
            }
            function call2(ev){
                if(disable!='undefined' && disable != null && disable == ev){
                    document.frames["tabframe"].document.getElementById('newProgressBar1').style.display = 'none';
                    document.getElementById('newProgressBarFrame').style.display="none";
                }
            }
            function autoDisable(){
                document.frames["tabframe"].document.getElementById('newProgressBar1').style.display = 'none';
                document.getElementById('newProgressBarFrame').style.display="none";
            }
            window.onbeforeunload = doUnload;
            function doUnload(){
                window.location = "<%=path%>/logout.do";
            }

        </script>
        <script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
        <script language="JavaScript" type="text/javascript" src="<%=path%>/js/Hashtable.js"></script>

        <script language="javascript">
            var breadCrumbText='';
            var quoteCache = new Hashtable();
            var bookingCache = new Hashtable();
            var blCache = new Hashtable();
	
	
            window.addEvent('domready', function() {
                //--horizontal
                var myHorizontalSlide = new Fx.Slide('horizontal_slide', {mode: 'horizontal'});
                $('h_slidein').addEvent('click', function(e){
                    document.getElementById("treemenu").style.height='500px';
                    document.getElementById("treemenu").style.width='230';
                    document.getElementById("sideMenu").style.width='17%';
                    document.getElementById("mainContent").style.width='83%';

                    e.stop();
                    myHorizontalSlide.slideIn();
                });

                $('h_slideout').addEvent('click', function(e){
                    document.getElementById("treemenu").style.height='500px';
                    document.getElementById("sideMenu").style.width='0px';
                    document.getElementById("mainContent").style.width='100%';
                    e.stop();
                    myHorizontalSlide.slideOut();
                });

            });


            function writeBreadCrumb(){
                if(breadCrumbText!=undefined){
                    document.getElementById("breadCrumb").innerHTML ="<span style='padding-left:10px' class='style3'>"+breadCrumbText+"</span>";
                }
            }

            function setBreadCrumb(breadCrumb){
                this.breadCrumbText = breadCrumb;
            }


            function clearBreadCrumb(){
                document.getElementById("breadCrumb").innerHTML="";
            }

            var dataStorage = new Hashtable();
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

            function makeFormBorderless(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if(element.type == "text" || element.type == "textarea" || element.type=="select-one"){
                        element.style.border=0;
                        if(element.type == "select-one"){
                            element.disabled = true;
                            element.style.backgroundColor="#CCEBFF";
                        }else{
                            element.readOnly = true;
                            element.tabIndex = -1;
                        }
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

            function disableFieldsWhileLocking(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if(element.type == "text" || element.type == "textarea" || element.type=="select-one"){
                        element.style.border=0;
                        if(element.type == "select-one"){
                            element.disabled = true;
                        }else{
                            element.readOnly = true;
                            element.tabIndex = -1;
                        }
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
                        element.tabIndex = -1;
                        element.className="textlabelsBoldForTextBox";
                    }else if(element.type == "button"){
                        if(element.value!="Go Back"){
                            element.style.visibility="hidden";
                        }
                    }
                }
                return false;
            }
            function viewFile(caption,path){
                var height = document.body.offsetHeight-50;
                var width = document.body.offsetWidth-100;
                GB_show(caption,path,height,width);
            }

        </script>


    </head>

    <div id="newProgressBarFrame" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <body marginheight="0" topmargin="2" marginwidth="0" class="whitebackgrnd">

        <table width="100%" border="0"  cellspacing="0" cellpadding="0"  height="100%">
            <tr>
                <td colspan="2" height="50px" >
                    <%@ include file="topFrame.jsp" %>
                </td>
            </tr>

            <tr>
                <td>
                    <div class="marginbottom">
                        <a id="h_slideout" href="#" onClick="writeBreadCrumb();"><img src="<%=path%>/img/icons/previous.gif" border="0" /></a> <a id="h_slidein" href="#" onClick="clearBreadCrumb();"><img src="<%=path%>/img/icons/next.gif" border="0" /></a>
                    </div>
                </td>
                <td>
                    <div id="breadCrumb"></div>
                </td>

            </tr>
            <tr>

                <td width="250px" valign="top" style="height:100%" id="sideMenu">
                    <div id="horizontal_slide">
                        <iframe src="<%=request.getContextPath()%>/jsps/admin/treemenu.jsp" name="treemenu" id="treemenu" width="100%" height="100%" frameborder="0"></iframe>
                    </div>
                </td>
                <td width="75%"  valign="top" id="mainContent">
                    <iframe src="<%=request.getContextPath()%>/jsps/Tab.jsp" name="tabframe" width="100%" id="tabframe" height="100%" frameborder="0"></iframe>

                </td>

            </tr>
        </table>

    </body>
</html>



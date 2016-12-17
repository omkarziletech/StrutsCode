<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.domain.User, com.gp.cvst.logisoft.hibernate.dao.*,com.gp.cong.logisoft.domain.Role,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.ProcessInfo,com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO"/>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.ArInvoice"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.Item"/>
<jsp:directive.page import="com.gp.cong.common.CommonConstants"/>
<%@page import="com.gp.cong.logisoft.bc.fcl.ItemMasterBC"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@page import="com.gp.cong.logisoft.util.TabMenu"%>
<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@include file="includes/jspVariables.jsp" %>

<%

       
        User user = CommonFunctions.isNotNull(session.getAttribute("loginuser"))?(User) session.getAttribute("loginuser"):null;
        if(user==null){
         	response.sendRedirect(request.getContextPath() + "/jsps/login.jsp");
         }
        DBUtil dbUtil = new DBUtil();
        ItemMasterBC itemMasterBC = new ItemMasterBC();
        Role role = user.getRole();
        TabMenu tabMenu = new TabMenu();
        String folderId =(request.getParameter("folderId") != null)?request.getParameter("folderId"):
        					(null!=request.getAttribute("folderId")?(String) request.getAttribute("folderId"):"1");
        Integer no = (folderId != null && !folderId.equals(""))?Integer.parseInt(folderId):0;
        String bolId = request.getParameter("bolid");
        String bokingId = request.getParameter("bokingid");
        String tabName = (request.getParameter("tabName") != null)?request.getParameter("tabName"):"";
        String path = request.getContextPath();        
        itemMasterBC.getImportPage(folderId,session);// sessting session request if the page is navigation for imports
       
        // ids coming from Import search page:------------
       
        List tabList = tabMenu.getTabStucture(request, no,user, tabName);
        
        if (null != bokingId && !bokingId.equals("0") && !"null".equals(bokingId)) {
            BookingFclDAO bookingFclDAO = new BookingFclDAO();
            String quoteId = bookingFclDAO.getQuoteNo(request.getParameter("bokingid"));
            request.setAttribute("QuotationId", quoteId);
        }
        if (session.getAttribute(CommonConstants.UNIQUE_ITEM) == null) {           
            Map<String, String> addUniqueCode = new HashMap<String, String>();
            com.gp.cong.logisoft.util.GenerateMenu generateMenu = new com.gp.cong.logisoft.util.GenerateMenu();
            Map<Item, List> menuMap = generateMenu.getMenuStructure();
            Set menuMapSet = menuMap.entrySet();
            for (Iterator it = menuMapSet.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();               
                List<Item> childTreeStructure = (List) entry.getValue();
                for (int i = 0; i < childTreeStructure.size(); i++) {
                    Item item = (Item) childTreeStructure.get(i);
                    List ListOfItem = tabMenu.getUniqueCode(request, item.getItemId(), role.getRoleId());
                    for (int k = 0; k < ListOfItem.size(); k++) {
                        Item newItem = (Item) ListOfItem.get(k);
                        addUniqueCode.put(newItem.getUniqueCode(),
                                "folderId=" + item.getItemId() + "&linkid=" + newItem.getItemId());
                    }

                }
            }
            //seeting session for unique item ides
            session.setAttribute(CommonConstants.UNIQUE_ITEM, addUniqueCode);
        }
        if (session.getAttribute("trade") == null) {
				  dbUtil.removeSessions(session);
        }
%>
<html>
    <head>
        <title>LCL Receivings</title>
        <script type="text/javascript">
            var djConfig = { isDebug: true };
        </script>
        <style type="text/css">
            /* the CSS is used to position our DIV in the center of the web page */
            #example { width:100%; padding:4px;height: 100%; }
        </style>
        <%@include file="includes/baseResources.jsp" %>
        <%@include file="includes/resources.jsp" %>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/js/JQuery-InnerFade/reset.css" type="text/css" media="all" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/js/JQuery-InnerFade/fonts.css"  type="text/css" media="all" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/dojo/dojo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/lfx/html.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/ver2/image_change.js"></script>
        <script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript">
            dojo.require("dojo.widget.TabContainer");
            dojo.require("dojo.widget.LinkPane");
            dojo.require("dojo.widget.ContentPane");
            dojo.require("dojo.widget.LayoutContainer");
            dojo.require("dojo.widget.Checkbox");


        </script>

        <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/DwrUtil.js'></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-shadow2/jquery-1.3.2.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-shadow2/liquid-canvas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-shadow2/liquid-canvas-plugins.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-shadow2/excanvas.js"></script>
        <script type="text/javascript">

            $(window).load(function() {
                $("#cell").liquidCanvas(
                "[shadow border gradient] => roundedRect{radius:30}");
            });
        </script>
        <script>
            function changeTab(){

            }
            function unload()
            {
                //$(".mainTabContainer").dropShadow({right: 10, bottom: 10, blur: 3});
                //window.showModelessDialog()('login.jsp')
                //alert("starting loading tabs");
            }
            function showTabs(){
                if(document.getElementById('newProgressBar1')!=null)
                    document.getElementById('newProgressBar1').style.display = 'none';
            }

        </script>
        <style type="text/css">

            .dojoTabPaneWrapper {
                padding : 1px 1px 1px 1px;
            }
        </style>
    </head>

    <div id="newProgressBar1" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader"><b style="width: 100%;padding-left: 45px;">Processing......Please Wait</b></p>
        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <body marginwidth="0" topmargin="0"  marginheight="0" class="whitebackgrnd"  onload=unload();>

        <form name="tabform" >
            <%
        if (request.getParameter("linkid") != null) {
            String str = (String) request.getParameter("linkid");
            String selecetedContainer = "tab" + str;
            %>
            <div id="mainTabContainer" dojoType="TabContainer" class="mainTabContainer" selectedChild="<%=selecetedContainer%>"  >
                <%
                    if (request.getParameter("tabName") != null) {
                %>
                <script>
                    document.getElementById('newProgressBar1').style.display="none";
                </script>
                <%            } else if (!request.getParameter("linkid").equals("56")) {
                %>
                <script>
                    document.getElementById('newProgressBar1').style.display="block";
                </script>
                <%}
                %>
                <%
                } else if (request.getAttribute("linkid") != null) {

                    String str = (String) request.getAttribute("linkid");
                    String selecetedContainer = "tab" + str;
                %>
                <div id="mainTabContainer" dojoType="TabContainer" class="mainTabContainer"
                     selectedChild="<%=selecetedContainer%>" >
                    <%
                } else {
                    %>
                    <div id="mainTabContainer"   dojoType="TabContainer" class="mainTabContainer"  >


                        <%}
            for (int i = 0; i < tabList.size(); i++) {
                String tabStr = (String) tabList.get(i);
                if (bolId != null && !"null".equals(bolId) && request.getParameter("fclLinkId") != null) {
                        if(bolId.equals("0")){
                        	String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
		                            String url = "src='" + path + "/fclBL.do?newFclBL=newFclBL'";
		                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
		                            tabStr = tabStr1 + url + tabStr2;
                        }else{
		                        if (tabStr.indexOf("id=tab" + request.getParameter("fclLinkId")) > -1) {
		                            String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
		                            String url = "src='" + path + "/fclBL.do?paramid=" + bolId + "'";
		                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
		                            tabStr = tabStr1 + url + tabStr2;
		                        }
                        }
                }
                String fileNo = request.getParameter("fileno");
                if (fileNo != null && ! fileNo.equals("null") && request.getParameter("linkid") != null) {
                        if (tabStr.indexOf("id=tab" + request.getParameter("linkid")) > -1) {
                            String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
                            String url = "src='" + path + "/fclBL.do?paramid=" + fileNo + "'";
                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
                            tabStr = tabStr1 + url + tabStr2;
                        }                    
                }
                if (bokingId != null && !bokingId.equals("null") && request.getParameter("bokLinkid") != null) {
                       if(bokingId.equalsIgnoreCase("0")){
	                       if (tabStr.indexOf("id=tab" + request.getParameter("bokLinkid")) > -1) {
	                            String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
	                            String url = "src='" + path + "/searchBookings.do?newbooking=newbooking'";
	                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
	                            tabStr = tabStr1 + url + tabStr2;
	                        }
                       }else{
                        if (tabStr.indexOf("id=tab" + request.getParameter("bokLinkid")) > -1) {
                            String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
                            String url = "src='" + path + "/searchBookings.do?paramid=" + bokingId + "'";
                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
                            tabStr = tabStr1 + url + tabStr2;
                        }
                       }
                                           
                }
            if ((request.getParameter("quoteid") != null || request.getAttribute("QuotationId") != null) && request.getParameter("quoteLinkId") != null) {
                    String stringNullValue = "";
                    if (request.getParameter("quoteid") != null && !request.getParameter("quoteid").equalsIgnoreCase("null")) {
                        stringNullValue = (String) request.getParameter("quoteid");
                    } else if (request.getAttribute("QuotationId") != null) {
                        stringNullValue = request.getAttribute("QuotationId").toString();
                    }
                    if (stringNullValue.equals("0")) {
                        if (tabStr.indexOf("id=tab" + request.getParameter("quoteLinkId")) > -1) {
                            String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
                            String url = "src='" + path + "/searchquotation.do?addQuote=addQuote'";
                            String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
                            tabStr = tabStr1 + url + tabStr2;
                        }
                    } else {
                        if (!stringNullValue.equals("null")) {
                            if (tabStr.indexOf("id=tab" + request.getParameter("quoteLinkId")) > -1) {
                                String tabStr1 = tabStr.substring(0, tabStr.indexOf("src="));
                                String url = "src='" + path + "/searchquotation.do?paramid1=" + stringNullValue + "'";
                                String tabStr2 = " " + tabStr.substring(tabStr.indexOf("width="));
                                tabStr = tabStr1 + url + tabStr2;
                            }
                        }
                    }
                }

  %>
                        <%=tabStr%>
                        <%
            }
            if (tabList.size() == 0) {
                        %>
                        <div id='emptyText' align='center' style='display:block;padding-top:5px;'>
                            <table width="99%" border="0" align="center" cellpadding="4" cellspacing="0" class="tableBorderNew" style="margin-top:150px">
                                <tr class="tableHeadingNew" height="90%">Welcome !!! Whats new in the Beta release 1.1</tr>

                                <tr>
                                    <td height="200px" id="headerImageCell" align="left" style="color:black;background: no-repeat scroll left bottom;">
                                        <SCRIPT language='JavaScript'>
                                            document.getElementById('headerImageCell').style.background = "url('"+document.write(layoutQ)+"')"
                                        </SCRIPT>
                                        <div style="padding-left:400px;margin-top:-100px;">
                                            <h2>Easier - And More Fun !</h2>
                                            <p style="text-align: ">Get more done in less time, in style! </p>
                                            <div class="clear"></div>
                                        </div>
                                    </td>
                                </tr>

                                <tr><td class="textlabels" width="80%"><b>Side Bar Navigation</b></td></tr>
                                <tr><td class="textlabels" style="border-bottom:dotted 1px #666666;">The traditional tree is replaced by accordion.Now get rid of the
                                        multiple clicks and so on. <br/>Use the mouse to explore the new accordion features and the links.</td></tr>
                                <tr><td class="textlabels" width="100%"><b>User Interface</b></td></tr>
                                <tr><td class="textlabels" style="border-bottom:dotted 1px #666666;">The fun is all set for the perfect background for beta application. <br/>Stationery adds a splash of color and helps you to turn an ordinary message into an extraordinary one..</td></tr>
                                <tr><td class="textlabels" width="100%"><b>Icons</b></td></tr>
                                <tr><td class="textlabels" style="border-bottom:dotted 1px #666666;">An image express a thousand of words. <br/>We took efforts to make a realistic icons in expressing the right setting for the functionalities.</td></tr>
                                <tr><td class="textlabels" style="padding-bottom:2px;">&nbsp;</td></tr>

                            </table>
                        </div>
                        <script>showTabs();</script>
                        <%}%>

                    </div>
                </div>
        </form>
    </body>
</html>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <c:set var="custAddress" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=sendToCustomerPage"/>
        <c:set var="generalInfo" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=sendToGeneralInformationPage"/>
        <c:set var="arConfig" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=arConfigPage"/>
        <c:set var="apConfig" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=sendToVendorPage"/>
        <c:set var="contactConfig" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=contactconfig"/>
        <c:set var="consigneeInfo" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=consigneeInfo"/>
        <c:set var="ctsInfo" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=ctsInfo"/>
        <c:set var="reportsInfo" value="${path}/tradingPartner.do?buttonValue=getFormValues&view=${view}&tradingPartnerId=${tradingPartnerForm.accountNo}&forward=reportConfig"/>
        <title>Trading Partner Tabs</title>
        <%@include file="includes/jspVariables.jsp" %>
        <%@include file="includes/baseResources.jsp" %>
        <script src="${path}/js/jquery/jquery.js" type="text/javascript"></script>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
        <script type="text/javascript">
            $(document).ready(function () {
                var initial = 0;
                var index = 0;
                var selectedTab = "${param.selectedTab}";
                $("ul.htabs li a").each(function () {
                    var tab = $(this).attr("rel");
                    if ($.trim(tab) == $.trim(selectedTab)) {
                        initial = index;
                    }
                    index++;
                })
                $("ul.htabs").tabs("> .pane", {effect: 'fade', current: 'selected', initialIndex: initial, onClick: function () {
                        var frameid = $("ul.htabs li.selected").find("a").attr("title");
                        $("#" + frameid).height($(document).height() - 60);
                        if (frameid == 'consigneeInfo') {
                            $("#" + frameid).contents().find("#notifyPartyAddress").focus();
                            $("#" + frameid).contents().find("#portNumber").focus();
                        }
                        if (frameid == 'aciframe') {
                            $("#" + frameid).contents().find("#acctReceive").focus();
                            $("#" + frameid).contents().find("#holdComment").focus();
                            $("#" + frameid).contents().find("#statements").focus();
                        }
                    }
                });
            });
        </script>
    </head>
    <body>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="container">
            <ul class="htabs">
                <c:if test="${roleDuty.tpShowAddress}">
                    <li><a rel="Address" href="javascript: void(0)" title="geniframe">Addresses</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowGeneralInfo}">
                    <li><a rel="General Info" href="javascript: void(0)" title="acciframe">General Info</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowArConfig}">
                    <li><a rel="AR Config" href="javascript: void(0)" title="aciframe">AR Config</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowApConfig}">
                    <li><a rel="AP Config" href="javascript: void(0)" title="vendorframe">AP Config</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowContactConfig}">
                    <li><a rel="Contact Config" href="javascript: void(0)" title="custiframe">Contact Config</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowConsigneeInfo}">
                    <li><a rel="Consignee Info" href="javascript: void(0)" title="consigneeInfo">Consignee Info</a></li>
                    </c:if>
                    <c:if test="${roleDuty.tpShowCtsConfig}">
                    <li><a rel="CTS Info" href="javascript: void(0)" title="ctsInfo">CTS Info</a></li>
                    </c:if>
                <li><a rel="Reports" href="javascript: void(0)" title="reportsInfo">Reports</a></li>
            </ul>
            <c:if test="${roleDuty.tpShowAddress}">
                <div class='pane'>
                    <iframe frameborder="0"  name="geniframe" id="geniframe" src='${custAddress}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowGeneralInfo}">
                <div class='pane'>
                    <iframe frameborder="0"  name="acciframe" id="acciframe" src='${generalInfo}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowArConfig}">
                <div class='pane'>
                    <iframe frameborder="0"  name="aciframe" id="aciframe" src='${arConfig}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowApConfig}">
                <div class='pane'>
                    <iframe frameborder="0"  name="vendorframe" id="vendorframe" src='${apConfig}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowContactConfig}">
                <div class='pane'>
                    <iframe frameborder="0"  name="custiframe" id="custiframe" src='${contactConfig}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowConsigneeInfo}">
                <div class='pane'>
                    <iframe frameborder="0"  name="consigneeInfo" id="consigneeInfo" src='${consigneeInfo}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <c:if test="${roleDuty.tpShowCtsConfig}">
                <div class='pane'>
                    <iframe frameborder="0"  name="ctsInfo" id="ctsInfo" src='${ctsInfo}' width='100%' height='0'></iframe>
                </div>
            </c:if>
            <div class='pane'>
                <iframe frameborder="0"  name="reportsInfo" id="reportsInfo" src='${reportsInfo}' width='100%' height='0'></iframe>
            </div>
        </div>
    </body>
</html>

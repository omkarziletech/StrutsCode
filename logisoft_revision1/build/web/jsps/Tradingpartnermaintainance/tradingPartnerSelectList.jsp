
<jsp:directive.page language="java" import="com.gp.cong.logisoft.util.DBUtil"/>
<jsp:directive.page language="java" import="org.apache.commons.lang3.StringUtils"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.domain.User,
                    java.util.*,org.apache.struts.util.LabelValueBean"/>
<%@page import="com.gp.cong.common.CommonConstants"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<bean:define id="Accounting" type="String">
    <bean:message key="AccountingDepartmentRole"/>
</bean:define>
<%
    User user = null;
    String loginUserRole = null;
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
        loginUserRole = user.getRole().getRoleDesc();
    }

    String accountName = null, accountNo = null, customer = null, master = "", eciAccountNo = "", sslineNumber = "", vendorShipperFrtfwd = "";
    String eciAcctNo2 = "", eciAcctNo3 = "";
    if (session.getAttribute("tradingPartnerId") != null) {
        customer = (String) session.getAttribute("tradingPartnerId");
        String stringTokens[] = StringUtils.splitPreserveAllTokens(customer, "\\");
        if (stringTokens != null && stringTokens.length > 1) {
            accountName = stringTokens[0];
            accountNo = stringTokens[1];
            master = stringTokens[2];
        }
    } else if (session.getAttribute(TradingPartnerConstants.TRADINGPARTNER) != null) {
        TradingPartner tradingPartner = (TradingPartner) session.getAttribute(TradingPartnerConstants.TRADINGPARTNER);
        accountName = tradingPartner.getAccountName();
        accountNo = tradingPartner.getAccountno();
        master = tradingPartner.getMaster();
        if (tradingPartner.getEciAccountNo() != null) {
            eciAccountNo = tradingPartner.getEciAccountNo();
        }
        if (tradingPartner.getECIFWNO() != null) {
            eciAcctNo2 = tradingPartner.getECIFWNO();
        }
        if (tradingPartner.getECIVENDNO() != null) {
            eciAcctNo3 = tradingPartner.getECIVENDNO();
        }
        sslineNumber = tradingPartner.getSslineNumber();
        vendorShipperFrtfwd = tradingPartner.getVendorShipperFrtfwdNo();
        if("master".equalsIgnoreCase(tradingPartner.getType())){
            request.setAttribute("master", true);
        }else{
            request.setAttribute("master", false);
        }
    }

    DBUtil dbUtil = new DBUtil();
// this request was for custome page  
    request.setAttribute("mastertypelist", dbUtil.getMasterCodeList());

// these request are for add contact page
    request.setAttribute("acodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "A"));
    request.setAttribute("bcodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "B"));
    request.setAttribute("ccodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "C"));
    request.setAttribute("dcodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "D"));
    request.setAttribute("ecodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "E"));
    request.setAttribute("fcodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "F"));
    request.setAttribute("gcodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "G"));
    request.setAttribute("hcodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "H"));
    request.setAttribute("icodelist", dbUtil.getspecialcodelist(22, "yes", "Select Special Code", "I"));

// these request ware for AR configuration page 
    request.setAttribute("creditRateList", dbUtil.getGenericCodeList(29, "yes", null));
    request.setAttribute("statements", dbUtil.getGenericCodeList(30, "yes", "Select Statements"));
//request.setAttribute("arcodelist",dbUtil.getUserList(24,"no","Select AR Contact Code"));
    request.setAttribute("creditStatusList", dbUtil.getGenericCodeList(43, "yes", null));
    request.setAttribute("Schedulelist", dbUtil.getScheduleList());

// these request ware for AP configuration page 
    request.setAttribute("creditTermList", dbUtil.getGenericCodeList(29, "yes", "Select Credit Terms"));
    request.setAttribute("arcodelist", dbUtil.getUserListBasedOnRole(CommonConstants.ROLE_NAME_COLLECTOR));

// this  request was for AP paymentpopup
    request.setAttribute("Paymentlist", dbUtil.getPaymentList());

// this  request was for AP General information....
    request.setAttribute("salescodelist", dbUtil.getGenericCodeList(23, "no", "Select Sales Code"));

// All subtypes should show as Kris told on 03/06/2010
    request.setAttribute("subTypeList", dbUtil.getSubTypeList());

    request.setAttribute("shippingCodeList", dbUtil.getGenericCodeCodesList(55));
    request.setAttribute("accountNo", accountNo);
%>
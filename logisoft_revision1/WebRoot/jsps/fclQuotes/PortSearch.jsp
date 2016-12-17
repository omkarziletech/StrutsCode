<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String label = "";
    if (request.getAttribute("textName") != null) {
        label = (String) request.getAttribute("textName");
    }


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>My JSP 'PortSearch.jsp' starting page</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/ajax.js"></script>
        <script type="text/javascript">
            function init() {
                setTimeout("setFocus()", 150);
            }
            function setFocus() {
                document.getElementById('region').focus();
            }

            function getCountry() {
                var region = document.getElementById('region').value;
                if (document.getElementById('fclOrigin').value != "" && document.getElementById('NonRated').value != "true") {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.FclBuyDAO",
                            methodName: "getDestinationCountriesForOriginFromDwr",
                            param1: document.getElementById('fclOrigin').value,
                            param2: region,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCountry(data);
                        }
                    });
                } else if (document.getElementById('fclDestination').value != "" && document.getElementById('NonRated').value != "true") {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.FclBuyDAO",
                            methodName: "getOriginCountriesForDestinationforDwr",
                            param1: document.getElementById('fclDestination').value,
                            param2: region,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCountry(data);
                        }
                    });
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getAllCountryByRegion",
                            param1: region,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCountry(data);
                        }
                    });
                }
            }
            function loadCountry(data) {
                resetAllValues();
                var options = [];
                jQuery.each(data, function (index) {
                    options.push("<option value='" + data[index].value + "'>" + data[index].label + "</option>");
                });
                jQuery("#country").html(options.join(''));
                jQuery("#city").html("");
            }

            function getUNLocation() {
                var region = document.getElementById('region').value;
                var region = "1";
                var country = document.getElementById('country').value;
                var from = document.getElementById('from').value;
                var typeOfmove = document.getElementById('typeOfmove').value;
            <%--	 	if(from=='pol'){--%>
                var fclOrigin = document.getElementById('fclOrigin').value;
                var fclDestination = document.getElementById("fclDestination").value;
                var NonRated = document.getElementById('NonRated').value;
                if (fclOrigin != null && fclOrigin != "" && NonRated != "true") {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.FclBuyDAO",
                            methodName: "getDestinationsForOriginFromDwr",
                            param1: fclOrigin,
                            param2: country,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                } else if (fclDestination != null && fclDestination != "" && NonRated != "true") {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.FclBuyDAO",
                            methodName: "getOriginsForDestinationforDwr",
                            param1: fclDestination,
                            param2: country,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                } else if (from == 'destination') {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getAllUnLocationByRegionCodeAndCountryForDestinationService",
                            param1: region,
                            param2: country,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                } else if (from == 'terminal') {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getAllUnLocationByRegionCodeAndCountryforiginService",
                            param1: region,
                            param2: country,
                            param3: typeOfmove,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                } else if (from == 'pod' || from == 'pol') {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getAllUnLocationByRegionCodeAndCountryforiginServiceByPortCity",
                            param1: region,
                            param2: country,
                            param3: typeOfmove,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getAllUnLocationByRegionCodeAndCountry",
                            param1: region,
                            param2: country,
                            dataType: "json"
                        },
                        success: function (data) {
                            loadCity(data);
                        }
                    });
                }
            }

            function loadCity(data) {
                resetAllValues();
                var options = [];
                jQuery.each(data, function (index) {
                    options.push("<option value='" + data[index].value + "'>" + data[index].label + "</option>");
                });
                jQuery("#city").html(options.join(''));
            }
            function assignTextValue() {
                var from = document.getElementById("from").value;
                var city = document.getElementById("city").value;
                var validateSpecialRemark = document.getElementById("validateSpecialRemark").value;
                if (validateSpecialRemark == "true") {
                    if (!document.getElementById("accecptRemarks").checked) {
                        alert("Please Accept Special Remarks");
                        return false;
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                                methodName: "getPortsForAgentInfoWithDefault1",
                                param1: document.getElementById("city").value
                            },
                            success: function (data) {
                                getAgent(data);
                            }
                        });
                        return false;
                    }
                } else {
                    if ((from == "destination" || from == "pod") && validateSpecialRemark != "false") {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                                methodName: "getSpecialRemarks",
                                param1: city
                            },
                            success: function (data) {
                                showSpecialRemarks(data);
                            }
                        });
                        return false;
                    }
                }
                var textName = document.getElementById("textName").value;
                parent.parent.document.getElementById(textName).value = document.getElementById("city").value;
                var setFocus = document.getElementById("setFocus").value;
                if (setFocus != "undefined" && setFocus != "" && undefined != parent.parent.document.getElementById(setFocus)) {
                    parent.parent.document.getElementById(setFocus).focus();
                }
                var boolean = false;
                if (textName == "portofdischarge" || textName == "finalDestination") {
                    boolean = true;
                    var portName = document.getElementById("city").value;
                    var index = portName.indexOf(" /");
                    var newPortName = portName.substring(0, index);
                    if (textName == "portofdischarge") {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cvst.logisoft.hibernate.dao.FclBlDAO",
                                methodName: "getClauseForPortname",
                                param1: newPortName,
                                dataType: "json"
                            },
                            success: function (data) {
                                onlyForFclBL(data);
                            }
                        });
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                                methodName: "getPortsForAgentInfoWithDefault1",
                                param1: document.getElementById("city").value
                            },
                            success: function (data) {
                                getAgent(data);
                            }
                        });
                    }
                }
                if (textName == "doorOrigin") {
                    parent.parent.document.getElementById("originZip").style.visibility = "visible";
                    parent.parent.document.getElementById("zipLookUp").style.visibility = "visible";
                }
                if (boolean == false) {
                    parent.parent.GB_hide();
                }
                /*if(undefined != parent.parent.disableAutoFF() && null != arent.parent.disableAutoFF()){
                 parent.parent.disableAutoFF();
                 }*/
            }
            function onlyForFclBL(data) {
                if (data[0] != undefined) {
                    parent.parent.document.getElementById("blClause").value = data[0];
                    parent.parent.document.getElementById("clauseDescription").value = data[1];
                }
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                        methodName: "getPortsForAgentInfoWithDefault1",
                        param1: document.getElementById("city").value
                    },
                    success: function (data) {
                        getAgent(data);
                    }
                });
            }
            function trim(stringToTrim) {
                return stringToTrim.replace(/^\s+|\s+$/g, "");
            }
            var remarksForParent;
            function showSpecialRemarks(data) {

                var data1 = trim(data);
                if (data1 != '') {
                    remarksForParent = data;
                    /*var remarks = data;
                     var remarks1 = remarks.substring(0,160);
                     var remarks2=remarks.substring(160,320);
                     
                     var a=parent.parent.document.getElementById("remarks").value;
                     parent.parent.document.getElementById("remarks").value = remarks1;
                     parent.parent.document.getElementById('remarks1').value = remarks2;
                     parent.parent.document.getElementById("remarks").style.height=80;
                     parent.parent.document.getElementById("remarks").style.cols=40;
                     parent.parent.document.getElementById("remarks").style.rows=5;
                     parent.parent.document.getElementById("remarks").style.width='90%';
                     parent.parent.document.getElementById("remarks").style.border=0;
                     parent.parent.document.getElementById("remarks").readOnly = true;
                     parent.parent.document.getElementById("remarks").className="bodybackgrnd";
                     parent.parent.document.getElementById("remarks1").style.height=80;
                     parent.parent.document.getElementById("remarks1").style.cols=40;
                     parent.parent.document.getElementById("remarks1").style.rows=5;
                     parent.parent.document.getElementById("remarks1").style.width='100%';
                     parent.parent.document.getElementById("remarks1").style.border=0;
                     parent.parent.document.getElementById("remarks1").readOnly = true;
                     parent.parent.document.getElementById("remarks1").className="bodybackgrnd";*/
                    document.getElementById('specialRemark').style.display = "block";
                    document.getElementById('validateSpecialRemark').value = "true";
                    document.getElementById('remark').innerHTML = data;
                } else {
                    /*parent.parent.document.getElementById("remarks").style.height=10;
                     parent.parent.document.getElementById("remarks").value = '';
                     parent.parent.document.getElementById("remarks1").style.height=10;
                     parent.parent.document.getElementById("remarks1").value = '';*/
                    remarksForParent = "";
                    document.getElementById('validateSpecialRemark').value = "false";
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName: "getPortsForAgentInfoWithDefault1",
                            param1: document.getElementById("city").value
                        },
                        success: function (data) {
                            getAgent(data);
                        }
                    });
                    return false;
                }
            }
            function getAgent(data) {
                document.getElementById('agent').value = data;
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                        methodName: "getPortsForAgentInfoWithDefault2",
                        param1: document.getElementById("city").value
                    },
                    success: function (data) {
                        getAgentName(data);
                    }
                });
            }
            function getAgentName(data) {
                var textName = document.getElementById('textName').value;
                if (textName == 'finalDestination' && parent.parent.document.getElementById("portofDischarge").value != "") {
                } else {
                    parent.parent.document.getElementById("agentNo").value = document.getElementById("agent").value;
                    parent.parent.document.getElementById("agent").value = data;
                }
                parent.parent.document.getElementById(textName).value = document.getElementById('city').value;
                //parent.parent.document.getElementById("portofDischarge").value = document.getElementById('city').value;
                var setFocus = document.getElementById('setFocus').value;
                if (setFocus != undefined && setFocus != "") {
                    if (setFocus == 'zip') {
                        if (parent.parent.document.getElementById('originZip').style.visibility == 'hidden') {
                            parent.parent.document.getElementById('noOfDays').focus();
                        } else {
                            parent.parent.document.getElementById(setFocus).focus();
                        }
                    } else {
                        parent.parent.document.getElementById(setFocus).focus();
                    }
                }
                parent.parent.GB_hide();
                parent.parent.call();
                parent.parent.assignRemarksValue(remarksForParent);
            }

            function resetAllValues() {
                document.getElementById('specialRemark').style.display = "none";
                document.getElementById('validateSpecialRemark').value = "";
            }
        </script>

    </head>
    <%@include file="../includes/resources.jsp" %>
    <body onload="init();" class="whitebackgrnd">
        <form name="portSearch">
            <table align="center" width="90%" border="0" cellpadding="0"
                   cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" height="10%">
                    <td>

                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr class="tableHeadingNew">
                                <%if (label.equalsIgnoreCase("portofDischarge")) { %>
                                <td>Destination Search</td>
                                <%} else if (label.equalsIgnoreCase("isTerminal")) { %>
                                <td>Origin Search</td>
                                <%} else {%>
                                <td>Search City</td>
                                <%}%>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td valign="top">

                        <table border="0" cellpading="10" cellspacing="10">
                            <tr class="textlabelsBold">
                                <td align="left">
                                    <b>Region : </b></td><td width="100">
                                    <html:select property="region" style="width:120px" styleId="region" value="" onchange="getCountry()">
                                            <html:option value="0">Select One</html:option>
                                        <html:option value="1">All</html:option>
                                        <html:optionsCollection name="regions" />
                                    </html:select>
                                </td>
                                <td>
                                    <b>Country : </b></td>
                                <td>
                                    <select name="country" id="country" style="width:120px" onchange="getUNLocation()">
                                        <option>Select One</option>
                                    </select>
                                </td>
                                <%--<html:select property="region" style="width:120px" styleId="country" value="" onchange="getUNLocation()" styleClass="textlabelsBoldForTextBox">
                                               <html:option value="0">Select One</html:option>
                                               <html:optionsCollection name="regions"/>
                                       </html:select>--%>
                                <td>
                                    <b>City : </b></td>
                                <td><select name="city" id="city" style="width:170px" onchange="resetAllValues();" class="textlabelsBoldForTextBox"></select></td>
                            </tr>
                            <tr class="textlabelsBold"><td colspan="6" align="center">
                                    <span id="specialRemark" style="display:none">
                                        <textarea rows="4" cols="100" id="remark" name="remark" readonly="true" class="textlabelsBoldForTextBox"></textarea>
                                        <br/>
                                        I have read and understood the remarks
                                        <input type="checkbox" name="accecptRemarks">
                                    </span></td>
                            </tr>
                            <tr>
                                <td colspan="6" align="center"><input type="button" class="buttonStyleNew" value="OK" onClick="assignTextValue()"/></td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
            <input type="hidden" name="textName" id="textName" value="${textName}"/>
            <input type="hidden" name="setFocus" id="setFocus" value="${setFocus}"/>
            <input type="hidden" name="from" id="from" value="${from}"/>
            <input type="hidden" name="validateSpecialRemark" id="validateSpecialRemark"/>
            <input type="hidden" name="agent" value="${agent}" id="agent">
            <input type="hidden"  value="${typeOfmove}" id="typeOfmove">
            <input type="hidden" value="${fclOrigin}" name="fclOrigin" id="fclOrigin">
            <input type="hidden" value="${fclDestination}" name="fclDestination" id="fclDestination">
            <input type="hidden" value="${NonRated}" name="NonRated" id="NonRated">
        </form>
    </body>
</html>

<%@page import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"%>
<%@page import="com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="com.gp.cong.logisoft.domain.lcl.GroupedCity"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.lcl.GroupedCityDAO"%>
<%@page import="com.gp.cong.logisoft.domain.UnLocation"%>
<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.UnLocationDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%
            try {
                String groupCity = null;
                String functionName = null;
                String disableDojo = null;
                String countryId = null;
                String unLocationName = null;
                String cityId = null;
                if (request.getParameter("tabName") != null) {
                    functionName = request.getParameter("tabName");
                }
                if (functionName.equals("UNLOCATION")) {
                    if (request.getParameter("countryId") != null
                            && request.getParameter("from") != null
                            && request.getParameter("from").equals("0")) {
                        countryId = request.getParameter("countryId");
                        cityId = request.getParameter("cityId");
                        unLocationName = request.getParameter("groupCity");
                    }
                }
                StringBuffer accountNoArray = new StringBuffer();
                accountNoArray.append("<ul>");
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                GroupedCityDAO groupCityDAO = new GroupedCityDAO();
                if (countryId != null && !countryId.trim().equals("")) {
                    List list = null;
                    if (functionName.equals("UNLOCATION")) {
                        List<UnLocation> groupCityList = null;
                        List<GroupedCity> cityList = null;
                        if (cityId != null && !cityId.trim().equals("")) {
                            groupCityList = unLocationDAO.getGroupCityList(Integer.parseInt(countryId), Integer.parseInt(cityId),unLocationName);
                        }
                        Iterator iter = groupCityList.iterator();
                        while (iter.hasNext()) {
                            String state = "";
                            Object[] obj = (Object[]) iter.next();
                            String unLocName = obj[0].toString();
                            String unLocCode = obj[1].toString();
                            String country = obj[3].toString();
                            GenericCode GenericCode = new GenericCodeDAO().findById(Integer.parseInt(country));
                            if (countryId != null && countryId != "") {
                                accountNoArray.append("<li id='"+unLocName+"'>");
                                if (GenericCode != null && GenericCode.getCodedesc()!=null &&  !GenericCode.getCodedesc().equals("") && unLocName!=null && !unLocName.equals("") && unLocCode!=null && !unLocCode.equals("")) {
                                    accountNoArray.append(unLocName + "/" + GenericCode.getCodedesc()
                                            + "/(" + unLocCode + ")");
                                }
                                accountNoArray.append("</li>");
                            }
                        }
                    }
                }
                accountNoArray.append("</ul>");
                if (null != disableDojo) {//-----This is to disable the dojo in a textBox when a checkBox is clicked.----
                    out.println("");
                } else {
                    out.println(accountNoArray.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }








%>
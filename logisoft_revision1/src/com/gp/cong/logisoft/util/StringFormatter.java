package com.gp.cong.logisoft.util;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import java.util.Date;

public class StringFormatter {

    UnLocationDAO unLocationDAO = new UnLocationDAO();
    PortsDAO portsDAO = new PortsDAO();

    public String findForManagement(String terminal) throws Exception {
        String origin = null;
        String unLocationCode = "";
        unLocationCode = orgDestStringFormatter(terminal);
        terminal = getTerminalFromInputStringr(terminal);
        UnLocation unLocation = null;
        if (null != unLocationCode && null != terminal) {
            List originList = unLocationDAO.findForManagement(
                    unLocationCode.trim(), terminal.trim());
            if (originList != null && originList.size() > 0) {
                unLocation = (UnLocation) originList.get(0);
                origin = unLocation.getId().toString();

            }
        }
        return origin;
    }

    /**
     * @param terminal
     * @param request
     * @return this method tokinzing for booking origin ,
     */
    public String findForManagement(String terminal, HttpServletRequest request) throws Exception {
        String origin = null;
        String unLocationCode = "";
        unLocationCode = orgDestStringFormatter(terminal);
        terminal = getTerminalFromInputStringr(terminal);
        if (null != unLocationCode && null != terminal) {
            List originList = unLocationDAO.findForManagement(
                    unLocationCode.trim(), terminal.trim());
            if (originList != null && originList.size() > 0) {
                UnLocation unLocation = (UnLocation) originList.get(0);
                origin = unLocation.getId().toString();
                if (request != null) {
                    request.setAttribute("destination", unLocation.getCountryId()
                            .getCodedesc()
                            + "/" + unLocation.getUnLocationName());
                }
            }
        }
        return origin;
    }

    public List tokenizingOriginandDestination(String terminal) throws Exception {
        String origin = null;
        String unLocationCode = "";
        unLocationCode = orgDestStringFormatter(terminal);
        terminal = getTerminalFromInputStringr(terminal);
        List destList = null;
        if (null != unLocationCode && null != terminal) {
            destList = portsDAO.findForUnlocCodeAndPortNameforPortsTemp(
                    unLocationCode.trim(), terminal.trim());
        }
        return destList;
    }

    public Quotation findForManagementForDestination(String terminal,
            String destination, Quotation quotation) throws Exception {
        if (CommonUtils.isNotEmpty(quotation.getZip())) {
            quotation.setTypeofMove("DOOR TO PORT");
        } else {
            List destList = tokenizingOriginandDestination(destination);
            if (CommonFunctions.isNotNullOrNotEmpty(destList)) {
                PortsTemp portsTempDest = (PortsTemp) destList.get(0);
                List originList = tokenizingOriginandDestination(terminal);
                if (null != originList && null != originList.get(0)) {
                    PortsTemp portsTemp = (PortsTemp) originList.get(0);
                    if (portsTemp.getPortCity() != null
                            && portsTemp.getPortCity().equals("Y")
                            && portsTempDest.getPortCity() != null
                            && portsTempDest.getPortCity().equals("Y")) {
                        quotation.setTypeofMove("PORT TO PORT");
                    }
                }
            }
        }
        return quotation;
    }

    public String getBreketValue(String inputString) throws Exception {
        if (inputString != null && inputString.indexOf("(") > -1 && inputString.lastIndexOf(")") > -1) {
            return inputString.substring(inputString.lastIndexOf("(") + 1,
                    inputString.lastIndexOf(")"));
        } else {
            return inputString;
        }
    }

    public String getDestinationCodeWithBracket(String inputString) throws Exception {
        if (inputString != null && inputString.indexOf("(") > -1) {
            return inputString.substring(inputString.lastIndexOf("("));
        } else {
            return inputString;
        }
    }

    public String compareStatus(String status, FclBl fclBl) throws Exception {
        if (fclBl != null) {
            status = (CommonFunctions.isNotNull(fclBl.getFclInttgra())) ? ((status.indexOf("G") > -1) || (status.indexOf("I") > -1) ? status
                    : status + fclBl.getFclInttgra() + ",")
                    : status;
            status = (CommonFunctions.isNotNull(fclBl.getSailDate())) ? ((status.indexOf("S") > -1) ? status
                    : status + setFclBlStatus(fclBl.getSailDate(), "S", 0))
                    : (status.indexOf("S") > -1) ? status.replace(",S", ",") : status;
            status = (CommonFunctions.isNotNull(fclBl.getReadyToEDI())) ? ((status.indexOf("E") > -1) ? status
                    : status + "E" + ",")
                    : (status.indexOf("E") > -1) ? status.replace(",E", ",") : status;
            status = (fclBl.getMaster() != null && fclBl.getMaster()
                    .equalsIgnoreCase("Yes")) ? ((status.indexOf("RM") > -1) ? status
                    : status + "RM" + ",")
                    : (status.indexOf("RM") > -1) ? status.replace(",RM", ",") : status;
            status = (CommonFunctions.isNotNull(fclBl.getBlClosed())) ? ((status.indexOf("CL") > -1) ? status
                    : status + "CL" + ",")
                    : (status.indexOf("CL") > -1) ? status.replace(",CL", ",") : status;
            status = (CommonFunctions.isNotNull(fclBl.getBlVoid())) ? ((status.indexOf("V") > -1) ? status
                    : status + "V" + ",")
                    : (status.indexOf("V") > -1) ? status.replace(",V", ",").replace("V,", "") : status;
            status = (CommonFunctions.isNotNull(fclBl.getBlAudit())) ? ((status.indexOf("A") > -1) ? status
                    : status + "A" + ",")
                    : (status.indexOf("A") > -1) ? status.replace(",A", ",") : status;
            status = ("P".equals(fclBl.getFileType())) ? ((status.indexOf("P") > -1) ? status
                    : status + "P" + ",")
                    : (status.indexOf("P") > -1) ? status.replace(",P", ",") : status;
            status = ("N".equals(fclBl.getRatesNonRates())) ? ((status.indexOf("NR") > -1) ? status
                    : status + "NR" + ",")
                    : (status.indexOf("NR") > -1) ? status.replace(",NR", ",") : status;

        }
        return status;
    }

    public String setFclBlStatus(Object dateObject, String mark, int delta) throws Exception {
        Date edtDate = null;
        String strDate = "";
        if (dateObject instanceof Date) {
            edtDate = (Date) dateObject;
        } else if (dateObject instanceof String) {
            strDate = (String) dateObject;
            edtDate = DateUtils.parseDate(strDate, "yyyy-MM-dd hh:mm:ss");
        }
        edtDate = DateUtils.formatDateAndParseToDate(edtDate);
        long diff = new DBUtil().getDaysBetweenTwoDays(edtDate, DateUtils.formatDateAndParseToDate(new Date()));
        if (diff >= 0) {
            return (diff + delta) + mark + ",";
        }
        return null;
    }

    /**
     * @param portOrOrgin
     * @return getting unloca code
     */
    public static String orgDestStringFormatter(String portOrOrgin) throws Exception {
        String returnString = null;
        if (CommonFunctions.isNotNull(portOrOrgin)) {

            int index = portOrOrgin.lastIndexOf("(");
            if (portOrOrgin.equals("Minneapolis/St Paul Apt/(USMSP/MN)")) {
                returnString = "USMSP";
            } else if (portOrOrgin.equals("Coussol/Fos sur Mer/(FRCOU)")) {
                returnString = "FRCOU";
            } else if (index != -1) {
                returnString = portOrOrgin.substring(
                        portOrOrgin.lastIndexOf("(") + 1, portOrOrgin
                        .lastIndexOf(")"));
            } else {
                returnString = "";
            }
        }
        return returnString;
    }

    /**
     * @param portOfOrigin
     * @return getting terminal from input String
     */
    public static String getTerminalFromInputStringr(String portOfOrigin) throws Exception {
        if (null != portOfOrigin) {
            int index = portOfOrigin.indexOf("/");
            if (portOfOrigin.equals("Minneapolis/St Paul Apt/(USMSP/MN)")) {
                portOfOrigin = "Minneapolis/St Paul Apt";
            } else if (portOfOrigin.equals("Coussol/Fos sur Mer/(FRCOU)")) {
                portOfOrigin = "Coussol/Fos sur Mer";
            } else if (index != -1) {
                int i = portOfOrigin.indexOf("/");
                if (i != -1) {
                    String a[] = portOfOrigin.split("/");
                    portOfOrigin = a[0];
                }
            } else if (portOfOrigin.indexOf("(") > -1) {
                portOfOrigin = portOfOrigin.substring(0, portOfOrigin.indexOf("(") - 1);
            }
        }
        return portOfOrigin;
    }

    public String getFormattedComment(String comment) {
        StringBuilder rateComment = new StringBuilder();
        if (comment != null) {
            int index = 0;
            char[] commentArray = comment.toCharArray();
            for (int i = 0; i < commentArray.length; i++) {
                if (commentArray[i] == '\n') {
                    rateComment.append(comment.substring(index, i).trim());
                    rateComment.append(" ");
                    index = i + 1;
                }
            }
            rateComment.append(comment.substring(index, comment.length())
                    .trim());
        }
        rateComment = new StringBuilder(rateComment.toString().replaceAll("'",
                "\\\\'"));
        return rateComment.toString().replaceAll("\"", "&quot;");
    }

    public static String relpaceString(String inputString, String oldChar, String newChar) throws Exception {
        DBUtil bUtil = new DBUtil();
        if (inputString.indexOf("),") > -1) {
            return inputString.replace(oldChar, newChar);
        } else {
            return bUtil.getData(inputString, 80);
        }
    }

    public static String formatString(String state, String city, String code, String stateCode, boolean flag) throws Exception {
        city = (city != null) ? city.replace("'", "''") : city;
        String string = "";
        if (flag) {
            if (state != null) {
                string += city + "/" + state;
            } else {
                string += city + "/" + code;
            }
        } else {
            if (state != null && stateCode != null) {
                string += city + "/" + state + "/" + stateCode + "(" + code + ")";
            } else if (stateCode != null) {
                string += city + "/" + stateCode + "(" + code + ")";
            } else if (state != null) {
                string += city + "/" + state + "(" + code + ")";
            } else {
                string += city + "/" + "(" + code + ")";
            }
        }
        return string;
    }

    public static boolean isNumeric(String testCase) throws Exception {
        boolean result = false;
        char[] testCharArray = testCase.toCharArray();
        for (int i = 0; i < testCharArray.length; i++) {
            if (Character.isDigit(testCase.charAt(i))) {
                result = true;
            } else {
                result = false;
            }

        }
        return result;
    }

    public static String formatForDestination(UnLocation unLocation) throws Exception {
        String city = "";
        if (unLocation.getStateId() != null) {
            city = unLocation.getUnLocationName() + "/" + unLocation.getStateId().getCode() + "/"
                    + unLocation.getCountryId().getCodedesc() + "(" + (String) unLocation.getUnLocationCode() + ")";
        } else {
            city = unLocation.getUnLocationName() + "/" + unLocation.getCountryId().getCodedesc() + "(" + (String) unLocation.getUnLocationCode() + ")";
        }
        return city;
    }

    public static String formatForOrigin(UnLocation unLocation) {
        String city = "";
        if (unLocation.getStateId() != null) {
            city = unLocation.getUnLocationName() + "/" + unLocation.getStateId().getCode() + "(" + (String) unLocation.getUnLocationCode() + ")";
        } else {
            city = unLocation.getUnLocationName() + "/" + "(" + (String) unLocation.getUnLocationCode() + ")";
        }
        return city;
    }

    public static String formatForPolPodDoorOrgDoorDest(UnLocation unLocation) {
        String city = "";
        if (unLocation.getStateId() != null) {
            city = unLocation.getUnLocationName() + "/" + unLocation.getStateId().getCode() + "/(" + unLocation.getUnLocationCode() + ")";
        } else {
            city = unLocation.getUnLocationName() + "/(" + unLocation.getUnLocationCode() + ")";
        }
        return city;
    }

    public static String getIssuingTerminal(String issueingTerminal) {
        String billOfLaddingNo = null;
        if (issueingTerminal != null) {
            int i = issueingTerminal.lastIndexOf("-");
            if (i != -1) {
                String issTerm[] = issueingTerminal.split("-");
                billOfLaddingNo = issTerm[1];
            }
        }
        return billOfLaddingNo;
    }
    // formatting bolid

    public String getBolid(String issueingTermunal, String destination, String fileNo) throws Exception {
        String billOfLaddingNo = "";
        if (issueingTermunal != null) {
	    billOfLaddingNo = getIssuingTerminal(issueingTermunal) + "-";
	}
        if (destination != null) {
            StringFormatter stringFormatter = new StringFormatter();
            billOfLaddingNo += stringFormatter.getDestinationCodeWithBracket(destination) + "-";
            billOfLaddingNo = billOfLaddingNo.replace("(", "");
            billOfLaddingNo = billOfLaddingNo.replace(")", "");
        }
        billOfLaddingNo += "04" + "-" + fileNo;
        return billOfLaddingNo;
    }
}

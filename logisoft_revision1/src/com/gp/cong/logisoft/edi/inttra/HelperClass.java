package com.gp.cong.logisoft.edi.inttra;

import com.gp.cong.common.CommonUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.gp.cong.logisoft.edi.dbUtil.DBUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperClass {

    public String getRoutingInstruction(String route) throws Exception {
        String routingInstruction = "";
        ResultSet rs = getResutlSet(SQLStatements.FCLRCL, route);
        while (rs.next()) {
            routingInstruction = rs.getString(1);
        }
        return routingInstruction;
    }

    public String getSizeLegend(String id) throws Exception {
        String equipmentType = "";
        ResultSet rs = getResutlSet(SQLStatements.SIZE_LEGEND, id);
        while (rs.next()) {
            equipmentType = rs.getString(1);
        }
        return equipmentType;
    }

    public String getMoveType(String moveType) throws Exception {
        String movetype = "";
        ResultSet rs = getResutlSet(SQLStatements.MOVE_TYPE, moveType);
        while (rs.next()) {
            movetype = rs.getString(1);
        }
        return movetype;
    }

    public String getVesselName(String vessel) throws Exception {
        String vessalName = "";
        ResultSet rs = getResutlSet(SQLStatements.VESSEL, vessel);
        while (rs.next()) {
            vessalName = rs.getString(1);
        }
        return vessalName;

    }

    public String getBookingDetails(String fileNo, String type) throws Exception {
        String hazardous = "";
        String shipmentDate = "";
        String issTerm = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        ResultSet rs = getResutlSet(SQLStatements.BOOKING, fileNo);
        while (rs.next()) {
            hazardous = rs.getString("hazmat");
            shipmentDate = sdf.format(rs.getDate("BookingDate"));
            issTerm = rs.getString("issuing_terminal");
        }
        if (type.equals("hazmat")) {
            return hazardous;
        } else if (type.equals("bkgDate")) {
            return shipmentDate;
        } else {
            return issTerm;
        }
    }

    public String getComments(String fileNo) throws Exception {
        String Comments = "";
        ResultSet rs = getResutlSet(SQLStatements.QUOTATION, fileNo);
        while (rs.next()) {
            Comments = rs.getString("comment");
        }
        return Comments;
    }

    public String conCat(String string1, String string2) throws Exception {
        String address = "";
        boolean flag = false;
        if (string1 != null && !string1.trim().equals("")) {
            address = address + string1;
            flag = true;
        }
        if (string2 != null && !string2.trim().equals("")) {
            if (flag) {
                address = address + ",";
            }
            address = address + string2;
        }
        return address;

    }

    public String getSSLineNo(String ssline) throws Exception {
        ResultSet rs = getResutlSet(SQLStatements.SSLINE, ssline);
        while (rs.next()) {
            ssline = rs.getString(1);
        }
        return ssline;
    }

    public String getScac(String ssline, String type) throws Exception {
        String scac = "";
        String contractNo = "";
        ResultSet rs = getResutlSet(SQLStatements.CARRIER_SCAC, ssline);
        while (rs.next()) {
            scac = rs.getString(1);
            contractNo = rs.getString(2);
        }
        if (type.equals("scac")) {
            return scac;
        } else {
            return contractNo;
        }
    }

    public String getUserEmail(String itmnum) throws Exception {
        String userEmail = "";
        ResultSet rs = getResutlSet(SQLStatements.USER_EMAIL, itmnum);
        while (rs.next()) {
            userEmail = rs.getString(1);
        }
        return userEmail;
    }

    public String getCountry(String id) throws Exception {
        String state = "";
        ResultSet rs = getResutlSet(SQLStatements.STATE, id);
        while (rs.next()) {
            state = rs.getString(1);
        }
        return state;
    }

    public Integer getDocVersion(String fileName) throws Exception {
        String count = "";
        Integer version = 1;
        ResultSet rs = getResutlSet(SQLStatements.COUNT_VERSION, fileName);
        while (rs.next()) {
            count = rs.getString(1);
            version = Integer.parseInt(count);
            version++;
        }
        return version;
    }

    public ResultSet getResutlSet(String query, String... params) throws Exception {
        DBUtil dbUtil = new DBUtil();
        Connection conn = dbUtil.getLogisoftConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        ps = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            ps.setString(i + 1, params[i]);
        }
        rs = ps.executeQuery();
        return rs;
    }

    public List<String> splitString(String reference, int limit) throws Exception {
        List<String> stringList = new ArrayList<String>();
        int beginIndex = 0;
        int endIndex = limit;
        while (reference.length() > endIndex) {
            stringList.add(reference.subSequence(beginIndex, endIndex).toString());
            beginIndex = endIndex;
            endIndex += limit;
        }
        stringList.add(reference.substring(beginIndex).toString());

        return stringList;
    }

    public List<String> wrapText(String reference) throws Exception {
        List<String> stringList = new ArrayList<String>();
        Pattern wrapText = Pattern.compile(".{0,33}(?:\\S(?:-| |$)|$)");
        Matcher matcher = wrapText.matcher(reference);
        while (matcher.find()) {
            if (CommonUtils.isNotEmpty(matcher.group())) {
                stringList.add(matcher.group());
            }
        }

        return stringList;
    }

    public List<String> wrapAddress(String reference) {
        List l = new ArrayList();
        if (null != reference) {
            String[] split = reference.split("\n");
            for (int i = 0; i < split.length; i++) {
                l.add(split[i]);
            }
        }
        return l;
    }

    public String wrapDescription(String reference) {
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(reference);
        if (m.find()) {
            return m.replaceAll(" ");
        }
        return reference;
    }

    public List splitDescrption(List descriptionList, int size) {
        StringBuilder sb = new StringBuilder("");
        List resultList = new ArrayList();
        int row = 0;
        boolean added = false;
        for (int i = 0; i < descriptionList.size(); i++) {
            String str = (String) descriptionList.get(i);
            row++;
            sb.append(str);
            if (row == size) {
                added = true;
                row = 0;
                resultList.add(sb.toString());
                sb = new StringBuilder("");
            } else {
                sb.append("\n");
                added = false;
            }
        }
        if (!added) {
            resultList.add(sb.toString());
        }
        return resultList;
    }
}

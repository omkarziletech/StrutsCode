package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.struts.util.LabelValueBean;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC;
import com.gp.cong.logisoft.domain.AesHistory;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.SedSchedulebDetails;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.AesHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.struts.LoadEdiProperties;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.apache.log4j.Logger;

public class SedFilingBC {

    private static final Logger log = Logger.getLogger(SedFilingBC.class);

    public List getFieldList(Integer codeTypeId, String field) throws Exception {
        List typeList = new GenericCodeDAO().findByCodeTypeid(codeTypeId);
        List resultList = new ArrayList();
        //resultList.add(new LabelValueBean("Select "+field, ""));
        for (Iterator iter = typeList.iterator(); iter.hasNext();) {
            GenericCode genericCode = (GenericCode) iter.next();
            if ((CommonUtils.isEqual(genericCode.getCode(), "OS") && CommonUtils.isEqual(field, "Export Code"))
                    || (CommonUtils.isEqual(genericCode.getCode(), "C33") && CommonUtils.isEqual(field, "License Code"))) {
                resultList.add(0, new LabelValueBean(genericCode.getCode() + ":" + genericCode.getCodedesc(), genericCode.getCode()));
            } else {
                resultList.add(new LabelValueBean(genericCode.getCode() + ":" + genericCode.getCodedesc(), genericCode.getCode()));
            }
        }

        return resultList;
    }

    public String createFlatFile(SedFilings sedFilings) throws Exception {
        String fileLocation = getFileLocationForAesDetails(sedFilings.getTrnref());
        FclBl fclBl = new FclBlDAO().getFileNoObject(sedFilings.getShpdr());
        List vehicalList = new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref());
        List schedList = new SedSchedulebDetailsDAO().groupBySchedB(sedFilings.getTrnref());
        FileWriter out = new FileWriter(fileLocation);
        out.write(writeRow(sedFilings, fclBl, schedList, vehicalList));
        out.close();
        return fileLocation;
    }

    public String createFlatFileForLcl(SedFilings sedFilings, String doorOrigin, LclBooking lclBooking) throws Exception {
        String fileLocation = getFileLocationForAesDetails(sedFilings.getTrnref());
        List vehicalList = new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref());
        List schedList = new SedSchedulebDetailsDAO().groupBySchedB(sedFilings.getTrnref());
        FileWriter out = new FileWriter(fileLocation);
        out.write(writeRowForLcl(sedFilings, lclBooking, schedList, vehicalList, doorOrigin));
        out.close();
        return fileLocation;
    }

    public String getFileLocationForAesDetails(String bolNo) throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        String outputFileName = "";
        if (osName.contains("linux")) {
            outputFileName = LoadEdiProperties.getProperty("linuxAesOutBoundFile");
        } else {
            outputFileName = LoadEdiProperties.getProperty("aesOutBoundFile");
        }
        if (CommonUtils.isNotEmpty(outputFileName)) {
            File file = new File(outputFileName);

            if (!file.exists()) {
                file.mkdirs();
            }
        }
        outputFileName = outputFileName.trim() + "aes-" + bolNo + ".sed";
        return outputFileName;
    }

    public String writeRow(SedFilings sed, FclBl fclBl, List schedList, List vehicalList) throws Exception {
        StringBuilder sb = new StringBuilder();
        StringBuilder sub = new StringBuilder();
        String ultimateConsigneeCountry = sed.getCntdes();
        String ultimateConsigneeState = sed.getConsta();
        String shipperCountry = sed.getExpctry();
        String shipperState = sed.getExpsta();
        String originState = sed.getOrgsta();
        String transactionRefNum = sed.getTrnref();
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(transactionRefNum);
        int fileNoIndex = transactionRefNum.indexOf("-");
        Calendar now = Calendar.getInstance();
        if (null == sed.getAesSubmitYear()) {
            sed.setAesSubmitYear(String.valueOf(now.get(Calendar.YEAR)));
        }
        if (m.find()) {
            String multiFile = transactionRefNum.substring(fileNoIndex + 1).substring(0, 1);
            transactionRefNum = transactionRefNum.substring(0, fileNoIndex) + multiFile + "-" + sed.getAesSubmitYear() + transactionRefNum.substring(fileNoIndex + 2);
        } else {
            transactionRefNum = transactionRefNum.substring(0, fileNoIndex) + "-" + sed.getAesSubmitYear() + transactionRefNum.substring(fileNoIndex);
        }
        String destinationCountry = sed.getCntdes();
        if ("PR".equalsIgnoreCase(sed.getCntdes())) {
            ultimateConsigneeCountry = "US";
            ultimateConsigneeState = "PR";
            destinationCountry = "PR";
        }
        String originCountryCode = new UnLocationBC().getCountryCode((null != sed.getOrigin() && sed.getOrigin().lastIndexOf("(") > -1)
                ? sed.getOrigin().substring(sed.getOrigin().lastIndexOf("(") + 1,
                sed.getOrigin().length() - 1) : "");
        if ("PR".equalsIgnoreCase(originCountryCode)) {
            shipperCountry = "US";
            shipperState = "PR";
            originState = "PR";
            destinationCountry = "US";
        }
        if (CommonUtils.isNotEmpty(fclBl.getDoorOfOrigin())) {
            shipperCountry = "US";
        }
        if (null != sed.getAesDisabledFlag() && sed.getAesDisabledFlag().equalsIgnoreCase("D")) {
            sb.append("S").append("Y".equals(sed.getRelate()) ? "1" : "2").append("10".equals(sed.getModtrn()) ? "1" : "40".equals(sed.getModtrn()) ? "3" : "20".equals(sed.getModtrn()) ? "6" : "").append(appendSpace(transactionRefNum, 17)).append(sed.getRouted()).append(appendSpace(originState, 2)).append(appendSpace(sed.getScac(), 4)).append(appendSpace(destinationCountry, 2)).append("10".equals(sed.getModtrn()) ? appendSpace(sed.getVesnam(), 23) : appendSpace(null != fclBl ? fclBl.getSslineName() : "", 23)).append(appendSpace("", 2)).append(formatSchedNoForFcl(sed.getExppnm(), 4)).append(appendZeros(sed.getUpptna(), 5)).append(appendSpace(DateUtils.formatDate(sed.getDepdat(), "yyMMdd"), 6)).append("Y".equals(sed.getWaiver()) ? "1" : "0").append("Y".equals(sed.getHazard()) ? "1" : "0").append(appendSpace("", 5)).append("Y".equals(sed.getInbind()) ? "1" : "0").append(appendSpace(CommonUtils.isNotEmpty(sed.getInbtyp()) ? sed.getInbtyp() : "0", 1)).append(" ").append(appendSpace(sed.getInbnd(), 15)).append("D").append(appendSpace(sed.getBkgnum(), 30)).append(appendSpace(sed.getInbent(), 23)).append(appendSpace(sed.getFtzone(), 7)).append(appendSpace(sed.getCONTYP(), 1));
        } else {
            sb.append("S").append("Y".equals(sed.getRelate()) ? "1" : "2").append("10".equals(sed.getModtrn()) ? "1" : "40".equals(sed.getModtrn()) ? "3" : "20".equals(sed.getModtrn()) ? "6" : "").append(appendSpace(transactionRefNum, 17)).append(sed.getRouted()).append(appendSpace(originState, 2)).append(appendSpace(sed.getScac(), 4)).append(appendSpace(destinationCountry, 2)).append("10".equals(sed.getModtrn()) ? appendSpace(sed.getVesnam(), 23) : appendSpace(null != fclBl ? fclBl.getSslineName() : "", 23)).append(appendSpace("", 2)).append(formatSchedNoForFcl(sed.getExppnm(), 4)).append(appendZeros(sed.getUpptna(), 5)).append(appendSpace(DateUtils.formatDate(sed.getDepdat(), "yyMMdd"), 6)).append("Y".equals(sed.getWaiver()) ? "1" : "0").append("Y".equals(sed.getHazard()) ? "1" : "0").append(appendSpace("", 5)).append("Y".equals(sed.getInbind()) ? "1" : "0").append(appendSpace(CommonUtils.isNotEmpty(sed.getInbtyp()) ? sed.getInbtyp() : "0", 1)).append(" ").append(appendSpace(sed.getInbnd(), 15)).append(" ").append(appendSpace(sed.getBkgnum(), 30)).append(appendSpace(sed.getInbent(), 23)).append(appendSpace(sed.getFtzone(), 7)).append(appendSpace(sed.getCONTYP(), 1));
        }
        sb.append("\n");
        sb.append("R").append(appendSpace(sed.getEmail(), 79));
        sb.append("\n");
        if (CommonUtils.isNotEmpty(sed.getUnitno())) {
            sb.append("E").append(appendSpace(null != sed.getUnitno() ? sed.getUnitno().replace("-", "") : "", 14)).append(appendSpace("", 15)).append(appendSpace("", 4));
            sb.append("\n");
        }
        sb.append("TE").append(appendSpace(null != sed.getExpirs() ? sed.getExpirs().replace("-", "") : "", 9)).append(appendSpace(sed.getExpicd(), 1)).append(appendSpace(sed.getExpnam(), 30)).append(appendSpace(sed.getExpcfn(), 13)).append(" ").append(appendSpace(sed.getExpcln(), 20)).append(formatAddress(sed.getExpadd(), 64)).append(appendSpace(sed.getExpcpn().replace("-", ""), 10)).append(appendSpace(sed.getExpcty(), 25)).append(appendSpace(shipperState, 20)).append(appendSpace(shipperCountry, 2)).append(appendSpace(sed.getExpzip(), 9)).append(getIrsSuffix(sed.getExpirs()));
        sb.append("\n");
        sb.append("TC").append(appendSpace("", 9)).append(appendSpace("", 1)).append(appendSpace(sed.getConnam(), 30)).append(appendSpace(sed.getConcfn(), 13)).append(" ").append(appendSpace(sed.getConcln(), 20)).append(formatAddress(sed.getConadd(), 64)).append(appendSpace(sed.getConcpn().replace("-", ""), 10)).append(appendSpace(sed.getConcty(), 25)).append(appendSpace(ultimateConsigneeState, 20)).append(appendSpace(ultimateConsigneeCountry, 2)).append(appendSpace(sed.getConpst(), 9)).append("  ");
        sb.append("\n");
        if (CommonUtils.isNotEmpty(vehicalList) && CommonUtils.isNotEmpty(schedList)) {
            for (Object object : schedList) {
                Object[] sched = (Object[]) object;
                String quantities1 = null != sched[0] ? sched[0].toString() : "";
                String quantities2 = null != sched[1] ? sched[1].toString() : "";
                String weight = null != sched[2] ? sched[2].toString() : "";
                String value = null != sched[3] ? sched[3].toString() : "";
                String exportReference = null != sched[4] ? sched[4].toString() : "";
                String description1 = null != sched[5] ? sched[5].toString() : "";
                String units1 = null != sched[6] ? sched[6].toString() : "";
                String units2 = null != sched[7] ? sched[7].toString() : "";
                String licenseType = null != sched[8] ? sched[8].toString() : "";
                String exportLicense = null != sched[9] ? sched[9].toString() : "";
                String domesticOrForeign = null != sched[10] ? sched[10].toString() : "";
                String eccn = null != sched[11] ? sched[11].toString() : "";
                String schedBNumber = null != sched[12] ? sched[12].toString() : "";
                String totalLicenseValue = null != sched[18] ? sched[18].toString() : "";
                sb.append("L").append(appendSpace(exportReference, 2)).append(appendSpace(description1, 45)).append(appendSpace("", 1)).append(appendSpace("", 10)).append(appendSpace(schedBNumber, 10)).append(appendSpace(units1, 3)).append(appendZeros(quantities1, 10)).append(appendZeros(value, 10)).append(appendSpace(units2, 3)).append(appendZeros(quantities2, 10)).append(appendZeros(weight, 10)).append(appendSpace("", 4)).append(appendSpace(licenseType, 3)).append(appendSpace(exportLicense, 12)).append("D".equals(domesticOrForeign) ? "1" : "2").append(appendSpace("", 44)).append(appendSpace(eccn, 5)).append(appendZeros(totalLicenseValue, 10));
                sb.append("\n");
            }
            for (Iterator it = vehicalList.iterator(); it.hasNext();) {
                SedSchedulebDetails schedB = (SedSchedulebDetails) it.next();
                if (CommonUtils.isNotEmpty(schedB.getVehicleIdNumber())) {
                    sub.append("V").append(appendSpace(schedB.getVehicleIdType(), 1)).append(appendSpace(schedB.getVehicleIdNumber(), 25)).append(appendSpace(schedB.getVehicleTitleNumber(), 15)).append(appendSpace(schedB.getVehicleState(), 2));
                    sub.append("\n");
                }
            }
        }
        sb.append(sub);
        return sb.toString();
    }

    public String writeRowForLcl(SedFilings sed, LclBooking lclBooking, List schedList, List vehicalList, String doorOrigin) throws Exception {
        StringBuilder sb = new StringBuilder();
        StringBuilder sub = new StringBuilder();
        String ultimateConsigneeCountry = sed.getCntdes();
        String ultimateConsigneeState = sed.getConsta();
        String shipperCountry = sed.getExpctry();
        String shipperState = sed.getExpsta();
        String originState = sed.getOrgsta();
        String transactionRefNum = sed.getTrnref();
        if (sed.getOrigin() != null) {
            originState = sed.getOrigin().substring(sed.getOrigin().indexOf("/") + 1, sed.getOrigin().indexOf("("));
        }
        String destinationCountry = sed.getCntdes();
        if ("PR".equalsIgnoreCase(sed.getCntdes())) {
            ultimateConsigneeCountry = "US";
            ultimateConsigneeState = "PR";
            destinationCountry = "PR";
        }
        String originCountryCode = new UnLocationBC().getCountryCode((null != sed.getOrigin() && sed.getOrigin().lastIndexOf("(") > -1)
                ? sed.getOrigin().substring(sed.getOrigin().lastIndexOf("(") + 1,
                sed.getOrigin().length() - 1) : "");
        if ("PR".equalsIgnoreCase(originCountryCode)) {
            shipperCountry = "US";
            shipperState = "PR";
            originState = "PR";
            destinationCountry = "US";
        }
        Calendar now = Calendar.getInstance();
        if (null == sed.getAesSubmitYear()) {
            sed.setAesSubmitYear(String.valueOf(now.get(Calendar.YEAR)));
        }
        transactionRefNum = transactionRefNum.substring(0, 7) + sed.getAesSubmitYear() + "-" + transactionRefNum.substring(7);
        if (CommonUtils.isNotEqual(ultimateConsigneeCountry, "US")) {
            ultimateConsigneeState = "";
        }

        if (CommonUtils.isNotEmpty(doorOrigin)) {
            shipperCountry = "US";
        }
        sb.append("S").append("Y".equals(sed.getRelate()) ? "1" : "2").append("10".equals(sed.getModtrn()) ? "1" : "3");
        sb.append(appendSpace(transactionRefNum, 17)).append(sed.getRouted()).append(appendSpace(originState, 2));
        sb.append(appendSpace(sed.getScac(), 4)).append(appendSpace(destinationCountry, 2));
        sb.append("10".equals(sed.getModtrn()) ? appendSpace(sed.getVesnam(), 23) : appendSpace((null != lclBooking.getBookedSsHeaderId() && null != lclBooking.getBookedSsHeaderId().getVesselSsDetail()) ? lclBooking.getBookedSsHeaderId().getVesselSsDetail().getSpAcctNo().getAccountno() : "", 23));
        sb.append(appendSpace("", 2)).append(formatSchedNo(sed.getExppnm(), 4)).append(appendZeros(sed.getUpptna(), 5));
        sb.append(appendSpace(DateUtils.formatDate(sed.getDepdat(), "yyMMdd"), 6));
        sb.append("Y".equals(sed.getWaiver()) ? "1" : "0").append("Y".equals(sed.getHazard()) ? "1" : "0");
        sb.append(appendSpace("", 5)).append("Y".equals(sed.getInbind()) ? "1" : "0");
        sb.append(appendSpace(CommonUtils.isNotEmpty(sed.getInbtyp()) ? sed.getInbtyp() : "0", 1)).append(" ");
        sb.append(appendSpace(sed.getInbnd(), 15)).append(" ").append(appendSpace(sed.getBkgnum(), 30));
        sb.append(appendSpace(sed.getInbent(), 23));
        sb.append(appendSpace(sed.getFtzone(), 7)).append(appendSpace(sed.getCONTYP(), 1));
        sb.append("\n");
        sb.append("R").append(appendSpace(sed.getEmail(), 79));
        sb.append("\n");
        if (CommonUtils.isNotEmpty(sed.getUnitno())) {
            sb.append("E").append(appendSpace(null != sed.getUnitno() ? sed.getUnitno().replace("-", "") : "", 14)).append(appendSpace("", 15)).append(appendSpace("", 4));
            sb.append("\n");
        }
        sb.append("TE").append(appendSpace(null != sed.getExpirs() ? sed.getExpirs().replace("-", "") : "", 9)).append(appendSpace((null != sed.getExpicd() && !sed.getExpicd().equals("")) ? sed.getExpicd() : "", 1)).append(appendSpace(sed.getExpnam(), 30)).append(appendSpace(sed.getExpcfn().toUpperCase(), 13)).append(" ").append(appendSpace(null != sed.getExpcln() ? sed.getExpcln().toUpperCase() : "", 20)).append(formatAddress(sed.getExpadd(), 64)).append(appendSpace(null != sed.getExpcpn() ? sed.getExpcpn().replace("-", "") : "", 10)).append(appendSpace(sed.getExpcty(), 25)).append(appendSpace(shipperState.toUpperCase(), 20)).append(appendSpace(null != originCountryCode ? originCountryCode : "", 2)).append(appendSpace(sed.getExpzip(), 9)).append(getIrsSuffix(sed.getExpirs()));
        sb.append("\n");
        sb.append("TC").append(appendSpace("", 9));
        sb.append(appendSpace("", 1)).append(appendSpace(sed.getConnam(), 30)).append(appendSpace(sed.getConcfn(), 13)).append(" ").append(appendSpace(sed.getConcln(), 20)).append(formatAddress(sed.getConadd(), 64)).append(appendSpace(sed.getConcpn().replace("-", ""), 10)).append(appendSpace(sed.getConcty(), 25)).append(appendSpace(ultimateConsigneeState, 20)).append(appendSpace(ultimateConsigneeCountry, 2)).append(appendSpace(sed.getConpst(), 9)).append("  ");
        sb.append("\n");
        if (CommonUtils.isNotEmpty(vehicalList) && CommonUtils.isNotEmpty(schedList)) {
            for (Object object : schedList) {
                Object[] sched = (Object[]) object;
                String quantities1 = null != sched[0] ? sched[0].toString() : "";
                String quantities2 = null != sched[1] ? sched[1].toString() : "";
                String weight = null != sched[2] ? sched[2].toString() : "";
                String value = null != sched[3] ? sched[3].toString() : "";
                String exportReference = null != sched[4] ? sched[4].toString() : "";
                String description1 = null != sched[5] ? sched[5].toString().toUpperCase() : "";
                String units1 = null != sched[6] ? sched[6].toString() : "";
                String units2 = null != sched[7] ? sched[7].toString() : "";
                String licenseType = null != sched[8] ? sched[8].toString() : "";
                String exportLicense = null != sched[9] ? sched[9].toString() : "";
                String domesticOrForeign = null != sched[10] ? sched[10].toString() : "";
                String eccn = null != sched[11] ? sched[11].toString() : "";
                String schedBNumber = null != sched[12] ? sched[12].toString() : "";
                String totalLicenseValue = null != sched[18] ? sched[18].toString() : "";
                sb.append("L").append(appendSpace(exportReference, 2)).append(appendSpace(description1, 45));
                sb.append(appendSpace("", 1)).append(appendSpace("", 10)).append(appendSpace(schedBNumber, 10));
                sb.append(appendSpace(units1, 3)).append(appendZeros(quantities1, 10)).append(appendZeros(value, 10));
                sb.append(appendSpace(units2, 3)).append(appendZeros(quantities2, 10)).append(appendZeros(weight, 10));
                sb.append(appendSpace("", 4)).append(appendSpace(licenseType, 3)).append(appendSpace(exportLicense, 12));
                sb.append("D".equals(domesticOrForeign) ? "1" : "2").append(appendSpace("", 44));
                sb.append(appendSpace(eccn, 5)).append(appendZeros(totalLicenseValue, 10));
                sb.append("\n");
            }
            for (Iterator it = vehicalList.iterator(); it.hasNext();) {
                SedSchedulebDetails schedB = (SedSchedulebDetails) it.next();
                if (CommonUtils.isNotEmpty(schedB.getVehicleIdNumber())) {
                    sub.append("V").append(appendSpace(schedB.getVehicleIdType(), 1)).append(appendSpace(schedB.getVehicleIdNumber(), 25)).append(appendSpace(schedB.getVehicleTitleNumber(), 15)).append(appendSpace(schedB.getVehicleState(), 2));
                    sub.append("\n");
                }
            }
        }
        sb.append(sub);
//            sb.append("C"+"  "+"  "+appendSpace(sed.getVoyvoy(), 5)+formatSchedNo(sed.getExppnm(),4)+formatSchedNo(sed.getUpptna(),5)+appendSpace(sed.getBkgnum(), 20)+appendSpace(fclBl.getSslineNo(), 20)+appendSpace(sed.getFrtnum(), 20));
        return sb.toString();
    }

    public String appendSpace(String value, int actualLength) {
        if (null != value) {
            int spaceToappend = actualLength - value.length();
            if (actualLength < value.length()) {
                value = value.substring(0, value.length() + spaceToappend);
            } else {
                for (int i = 0; i < spaceToappend; i++) {
                    value = value + " ";
                }
            }
        } else {
            value = "";
            for (int i = 0; i < actualLength; i++) {
                value = value + " ";
            }
        }
        return value;
    }

    public String formatAddress(String value, int actualLength) {
        if (null != value) {
            value = value.replace("\r\n", " ").replace("\n", " ");
            int spaceToappend = actualLength - value.length();
            if (actualLength < value.length()) {
                value = value.substring(0, value.length() + spaceToappend);
            } else {
                for (int i = 0; i < spaceToappend; i++) {
                    value = value + " ";
                }
            }
        } else {
            value = "";
            for (int i = 0; i < actualLength; i++) {
                value = value + " ";
            }
        }
        return appendSpace(value, actualLength);
    }

    public String appendZeros(String value, int actualLength) {
        int spaceToappend = actualLength - value.length();
        if (null != value) {
            if (actualLength < value.length()) {
                value = value.substring(0, value.length() + spaceToappend);
            } else {
                for (int i = 0; i < spaceToappend; i++) {
                    value = "0" + value;
                }
            }
        } else {
            value = "";
            for (int i = 0; i < actualLength; i++) {
                value = "0" + value;
            }
        }
        return value;
    }

    public String formatSchedNo(String value, int length) {
        if (CommonUtils.isNotEmpty(value)) {
            if (value.startsWith("0")) {
                return appendSpace(value.substring(1), 4);
            }
        }
        return appendSpace(value, length);
    }

    public String formatSchedNoForFcl(String value, int length) {
        if (CommonUtils.isNotEmpty(value)) {
            return appendSpace(value.substring(1), length);
        }
        return "";
    }

    public void readAesResponse() throws Exception {
        String aesFilesFolder = "";
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("linux")) {
            aesFilesFolder = LoadEdiProperties.getProperty("linuxAesResponseFile");
        } else {
            aesFilesFolder = LoadEdiProperties.getProperty("aesResopnseFile");
        }
        if (null != aesFilesFolder) {
            File folder = new File(aesFilesFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File processedFolder = new File(aesFilesFolder + "processed/" + dateFolder);
            if (!processedFolder.exists()) {
                processedFolder.mkdirs();
            }
            File unprocessedFolder = new File(aesFilesFolder + "unprocessed/" + dateFolder);
            if (!unprocessedFolder.exists()) {
                unprocessedFolder.mkdirs();
            }
            File[] filesInFolder = folder.listFiles();
            TreeMap<String, String> fileMap = new TreeMap<String, String>();
            for (File filesInFolder1 : filesInFolder) {
                if (filesInFolder1.isFile()) {
                    String aesResponseFileName = filesInFolder1.getName();
                    String key = "";
                    if (aesResponseFileName.contains(".aes")) {
                        key = aesResponseFileName.substring(0, aesResponseFileName.lastIndexOf(".aes"));
                        fileMap.put(key, aesResponseFileName);
                    } else if (aesResponseFileName.contains(".resp")) {
                        key = aesResponseFileName.substring(0, aesResponseFileName.lastIndexOf(".resp"));
                        fileMap.put(key, aesResponseFileName);
                    }
                }
            }
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                String fileName = entry.getValue();
                if (fileName.contains(".aes")) {
                    InputStream aesinInputStream = new FileInputStream(aesFilesFolder + fileName);
                    try {
                        readAesResponseFile(aesinInputStream, aesFilesFolder, fileName);
                        aesinInputStream.close();
                        File deleteFile = new File(aesFilesFolder + fileName);
                        if (deleteFile.exists()) {
                            deleteFile.renameTo(new File(aesFilesFolder + "processed/" + dateFolder + deleteFile.getName()));
                            deleteFile.deleteOnExit();
                        }
                    } catch (JDBCConnectionException e) {
                        throw e;
                    } catch (Exception e) {
                        aesinInputStream.close();
                        File deleteFile = new File(aesFilesFolder + fileName);
                        if (deleteFile.exists()) {
                            deleteFile.renameTo(new File(aesFilesFolder + "unprocessed/" + dateFolder + deleteFile.getName()));
                            deleteFile.deleteOnExit();
                        }
                    }
                } else if (fileName.contains(".resp")) {
                    InputStream respInputStream = new FileInputStream(aesFilesFolder + fileName);
                    try {
                        readRespResponseFile(respInputStream, aesFilesFolder, fileName);
                        respInputStream.close();
                        File deleteFile = new File(aesFilesFolder + fileName);
                        if (deleteFile.exists()) {
                            deleteFile.renameTo(new File(aesFilesFolder + "processed/" + dateFolder + deleteFile.getName()));
                            deleteFile.deleteOnExit();
                        }
                    } catch (JDBCConnectionException e) {
                        throw e;
                    } catch (Exception e) {
                        respInputStream.close();
                        File deleteFile = new File(aesFilesFolder + fileName);
                        if (deleteFile.exists()) {
                            deleteFile.renameTo(new File(aesFilesFolder + "unprocessed/" + dateFolder + deleteFile.getName()));
                            deleteFile.deleteOnExit();
                        }
                    }
                }
            }
        }
    }

    public void readAesResponseFile(InputStream is, String aesFilesFolder, String aesResponseFileName) throws Exception {
        List<String> contents = FileUtils.readLines(new File(aesFilesFolder + aesResponseFileName));
        SedFilingsDAO sedFilingsDAO = new SedFilingsDAO();
        // Iterate the result to print each line of the file.
        String itn = "";
        String shipmentNumber = "";
        String status = "";
        String exception = "";
        Pattern p = Pattern.compile("[a-zA-Z]");
        for (String line : contents) {
            if (!"".equals(line.trim())) {
                if (line.startsWith("S")) {
                    if (line.length() > 28) {
                        shipmentNumber = line.substring(12, 29).trim();
                    } else if (line.length() > 12) {
                        shipmentNumber = line.substring(12).trim();
                    }
                    Matcher m = p.matcher(shipmentNumber.trim());
                    if (m.find()) {
                        if (line.length() > 43) {
                            itn = line.substring(29, 44).trim();
                        } else if (line.length() > 29) {
                            itn = line.substring(29).trim();
                        }
                    } else {
                        if (line.length() > 43) {
                            itn = line.substring(29, 44).trim();
                        } else if (line.length() > 29) {
                            itn = line.substring(29).trim();
                        }
                    }
                }
                if (line.startsWith("M")) {
                    if (line.substring(10).startsWith("SHIPMENT")) {
                        if (line.length() > 49) {
                            status = line.substring(10, 50).trim();
                        } else {
                            status = line.substring(10).trim();
                        }
                    } else {
                        if (line.length() > 49) {
                            exception = line.substring(10, 50).trim();
                        } else {
                            exception = line.substring(10).trim();
                        }
                    }
                }
            }
        }
        if (CommonUtils.isNotEmpty(shipmentNumber)) {
            log.info("shipmentNumber " + shipmentNumber);
            String fileNo = shipmentNumber.contains("-") ? shipmentNumber.substring(0, shipmentNumber.indexOf("-")) : shipmentNumber;
            if (fileNo.length() > 6) {
                fileNo = fileNo.substring(2);
            }
            log.info("shipmentNumber0 " + shipmentNumber);
            //String fileNo = shipmentNumber.contains("-") && shipmentNumber.length() > 2 ? shipmentNumber.substring(2, shipmentNumber.indexOf("-")) : shipmentNumber;
            String aesSumitedYear = shipmentNumber.contains("-") && shipmentNumber.length() > 2 ? shipmentNumber.substring(shipmentNumber.indexOf("-") + 1, shipmentNumber.lastIndexOf("-")) : shipmentNumber;
            Matcher match = p.matcher(shipmentNumber.trim());
            log.info("shipmentNumber1 " + shipmentNumber);
            if (match.find()) {
                shipmentNumber = shipmentNumber.replace("-" + aesSumitedYear, "").trim();
                log.info("shipmentNumber2 " + shipmentNumber);
                if (fileNo.length() > 6) {
                    shipmentNumber = shipmentNumber.substring(0, 8) + "-" + shipmentNumber.substring(8, 9) + shipmentNumber.substring(9);
                    fileNo = fileNo.substring(0, 6) + "-" + fileNo.substring(6);
                }
            } else {
                shipmentNumber = shipmentNumber.replace("-" + aesSumitedYear, "").trim();
                log.info("shipmentNumber3 " + shipmentNumber);
            }
            log.info("shipmentNumber4 " + shipmentNumber);
            FclBlDAO fclBlDAO = new FclBlDAO();
            FclBl fclBl = fclBlDAO.getFileNoObject(fileNo);
            LclFileNumber lclFileNumber = null;
            if (null == fclBl) {
                lclFileNumber = new LclFileNumberDAO().getByProperty("fileNumber", fileNo);
            }
            FclAESDetails fclAESDetails = new FclAESDetails();
            boolean hasAes = false;
            SedFilings sedFilings = sedFilingsDAO.findByTrnref(shipmentNumber.trim());
            if (null != sedFilings) {
                sedFilings.setItn(itn);
                if (CommonUtils.isNotEmpty(exception)) {
                    sedFilings.setStatus("E");
                } else if (status.contains("SHIPMENT")) {
                    sedFilings.setStatus("C");
                }
                AesHistory aesHistory = new AesHistory();
                if (null != fclBl) {
                    aesHistory.setBolId(fclBl.getBol());
                    aesHistory.setFclBlNo(fclBl.getBolId());
                    for (Object object : fclBl.getFclAesDetails()) {
                        FclAESDetails aes = (FclAESDetails) object;
                        if (null != aes.getAesDetails() && aes.getAesDetails().equalsIgnoreCase(itn)) {
                            hasAes = true;
                            break;
                        }
                    }
                    if (!hasAes && CommonUtils.isNotEmpty(itn)) {
                        fclAESDetails.setAesDetails(itn);
                        fclAESDetails.setFileNo(fileNo);
                        fclAESDetails.setTrailerNoId(fclBl.getBol());
                        fclBl.getFclAesDetails().add(fclAESDetails);
                        fclBlDAO.update(fclBl);
                    }
                } else if (null != lclFileNumber) {
                    //aesHistory.setBolId(lclFileNumber.getId());
                    if (CommonUtils.isNotEmpty(itn)) {
                        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
                        Lcl3pRefNo lcl3pRefNo = lcl3pRefNoDAO.getLcl3PRefDetails(lclFileNumber.getId(), "AES_ITNNUMBER", itn);
                        if (lcl3pRefNo == null) {
                            lcl3pRefNo = new Lcl3pRefNo();
                            lcl3pRefNo.setLclFileNumber(lclFileNumber);
                            lcl3pRefNo.setType("AES_ITNNUMBER");
                            lcl3pRefNo.setReference(itn);
                            lcl3pRefNoDAO.save(lcl3pRefNo);
                        }
                    }

                    aesHistory.setFclBlNo(lclFileNumber.getFileNumber());
                } else {
                    aesHistory.setFclBlNo(shipmentNumber);
                }
                aesHistory.setFileNumber(shipmentNumber);
                aesHistory.setFileNo(fileNo);
                aesHistory.setAesException(exception);
                aesHistory.setItn(itn);
                aesHistory.setStatus(status);
                aesHistory.setProcessedDate(new Date());
                byte[] bs = IOUtils.toByteArray(is);
                Blob blob = fclBlDAO.getSession().getLobHelper().createBlob(bs);
                aesHistory.setResponseFile(blob);
                new AesHistoryDAO().save(aesHistory);
                sedFilingsDAO.update(sedFilings);
            }
        }
    }

    public void readRespResponseFile(InputStream is, String aesFilesFolder, String aesResponseFileName) throws Exception {
        List<String> contents = FileUtils.readLines(new File(aesFilesFolder + aesResponseFileName));
        // Iterate the result to print each line of the file.
        String itn = "";
        String shipmentNumber = "";
        String status = "";
        String exception = "";
        if (null != contents && contents.size() > 0) {
            contents.remove(0);
        }
        for (String line : contents) {
            if (null != line && line.length() > 0) {
                String key = StringUtils.substringBefore(line, " ");
                shipmentNumber = StringUtils.substringBefore(key, "-") + "-" + (StringUtils.substringAfterLast(key, "-").substring(0, 2));
                status += StringUtils.substringAfterLast(line, key).trim() + " ";
            }
        }
        status = StringUtils.removeEnd(status, " ");
        if (CommonUtils.isNotEmpty(shipmentNumber)) {
            FclBlDAO fclBlDAO = new FclBlDAO();
            String fileNo = shipmentNumber.contains("-") ? shipmentNumber.substring(0, shipmentNumber.indexOf("-")) : shipmentNumber;
            if (fileNo.length() > 6) {
                fileNo = fileNo.substring(2);
            }
            //String fileNo = shipmentNumber.contains("-") && shipmentNumber.length() > 2 ? shipmentNumber.substring(2, shipmentNumber.indexOf("-")) : shipmentNumber;
            FclBl fclBl = fclBlDAO.getFileNoObject(fileNo);
            LclFileNumber lclFileNumber = null;
            if (null == fclBl) {
                lclFileNumber = new LclFileNumberDAO().getByProperty("fileNumber", fileNo);
            }
            FclAESDetails fclAESDetails = new FclAESDetails();
            SedFilingsDAO sedFilingsDAO = new SedFilingsDAO();
            boolean hasAes = false;
            SedFilings sedFilings = sedFilingsDAO.findByTrnref(shipmentNumber.trim());
            if (null != sedFilings) {
                sedFilings.setItn(itn);
                if (status.contains("SUCCESSFULLY PROCESSED")) {
                    sedFilings.setStatus("P");
                } else {
                    sedFilings.setStatus("E");
                }
                AesHistory aesHistory = new AesHistory();
                if (null != fclBl) {
                    aesHistory.setBolId(fclBl.getBol());
                    aesHistory.setFclBlNo(fclBl.getBolId());
                    for (Object object : fclBl.getFclAesDetails()) {
                        FclAESDetails aes = (FclAESDetails) object;
                        if (null != aes.getAesDetails() && aes.getAesDetails().equalsIgnoreCase(itn)) {
                            hasAes = true;
                            break;
                        }
                    }
                    if (!hasAes && CommonUtils.isNotEmpty(itn)) {
                        fclAESDetails.setAesDetails(itn);
                        fclAESDetails.setFileNo(fileNo);
                        fclAESDetails.setTrailerNoId(fclBl.getBol());
                        fclBl.getFclAesDetails().add(fclAESDetails);
                        fclBlDAO.update(fclBl);
                    }
                } else if (null != lclFileNumber) {
                    //aesHistory.setBolId(lclFileNumber.getId());
                    aesHistory.setFclBlNo(lclFileNumber.getFileNumber());
                }
                aesHistory.setFileNumber(shipmentNumber);
                aesHistory.setAesException(exception);
                aesHistory.setFileNo(fileNo);
                aesHistory.setItn(itn);
                aesHistory.setStatus(status);
                aesHistory.setProcessedDate(new Date());
                byte[] bs = IOUtils.toByteArray(is);
                Blob blob = fclBlDAO.getSession().getLobHelper().createBlob(bs);
                aesHistory.setResponseFile(blob);
                new AesHistoryDAO().save(aesHistory);
                sedFilingsDAO.update(sedFilings);
            }
        }
    }

    public String validateAes(SedFilings sed) throws Exception {
        String mandatory = "";
        if (CommonUtils.isEmpty(sed.getCntdes())) {
            mandatory = mandatory + "--> Please Enter Destination<br>";
        }
        if (CommonUtils.isEmpty(sed.getOrgsta())) {
            mandatory = mandatory + "--> Please Enter Origin<br>";
        }
        if (CommonUtils.isEmpty(sed.getExppnm())) {
            mandatory = mandatory + "--> Please Enter Port of Loading<br>";
        }
        if (CommonUtils.isEmpty(sed.getUpptna())) {
            mandatory = mandatory + "--> Please Enter Port of Discharge<br>";
        }
        if (CommonUtils.isEmpty(sed.getScac())) {
            mandatory = mandatory + "--> Please Enter SCAC Code<br>";
        }
        if (CommonUtils.isEmpty(sed.getVesnam())) {
            mandatory = mandatory + "--> Please Enter Vessel Name<br>";
        }
        if (CommonUtils.isEmpty(sed.getVoyvoy())) {
            mandatory = mandatory + "--> Please Enter Voyage Number<br>";
        }
        if (CommonUtils.isEmpty(sed.getEmail())) {
            mandatory = mandatory + "--> Please Enter Email<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpnam())) {
            mandatory = mandatory + "--> Please Enter Shipper Name<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpadd())) {
            mandatory = mandatory + "--> Please Enter Shipper Address<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpcty())) {
            mandatory = mandatory + "--> Please Enter Shipper City<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpsta())) {
            mandatory = mandatory + "--> Please Enter Shipper State<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpzip())) {
            mandatory = mandatory + "--> Please Enter Shipper Zip<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpcfn())) {
            mandatory = mandatory + "--> Please Enter Shipper First Name<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpcpn())) {
            mandatory = mandatory + "--> Please Enter Shipper Phone<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpirs())) {
            mandatory = mandatory + "--> Please Enter Shipper IRS Number<br>";
        } else if (sed.getExpirs().replace("-", "").length() != 9 && sed.getExpirs().replace("-", "").length() != 11 && sed.getExpirs().replace("-", "").length() != 13) {
            mandatory = mandatory + "--> Shipper IRS# Length Should be 9 or 11 or 13<br>";
        }
        if (CommonUtils.isEmpty(sed.getExpicd())) {
            mandatory = mandatory + "--> Please Enter Type in USPPI Section<br>";
        }
        if (CommonUtils.isEmpty(sed.getConnam())) {
            mandatory = mandatory + "--> Please Enter Consignee Name<br>";
        }
        if (CommonUtils.isEmpty(sed.getConadd())) {
            mandatory = mandatory + "--> Please Enter Consignee Address<br>";
        }
        if (CommonUtils.isEmpty(sed.getConcty())) {
            mandatory = mandatory + "--> Please Enter Consignee City<br>";
        }
        if ("N".equalsIgnoreCase(sed.getExppoa()) && "N".equalsIgnoreCase(sed.getConpoa())) {
            mandatory = mandatory + "--> Please Select Either Shipper or Consignee POA<br>";
        }
        if (CommonUtils.isEmpty(sed.getRouted())) {
            mandatory = mandatory + "--> Please Select Routed Transaction<br>";
        }
        if (CommonUtils.isEmpty(sed.getHazard())) {
            mandatory = mandatory + "--> Please Select Hazardous<br>";
        }
        if (CommonUtils.isNotEmpty(sed.getInbnd())) {
            if (CommonUtils.isEmpty(sed.getInbent()) && CommonUtils.isEmpty(sed.getFtzone())) {
                mandatory = mandatory + "--> Please Enter either Foreign Trade Zone or Inbond Entry# <br>";
            }
        }
        return mandatory;
    }

    public String getIrsSuffix(String irs) {
        if (CommonUtils.isNotEmpty(irs)) {
            irs = irs.replace("-", "");
            if (irs.length() > 9) {
                if (irs.length() == 10) {
                    return appendSpace(irs.substring(9), 2);
                } else {
                    return appendSpace(irs.substring(9, 11), 2);
                }
            }
        }
        return appendSpace("", 2);
    }
}

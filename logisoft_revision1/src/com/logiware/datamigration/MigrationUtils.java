package com.logiware.datamigration;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class MigrationUtils {

    /**
     * getStackTrace - get full stack trace from throwable
     * @param throwable
     * @return stackTrace - String
     */
    private static final Logger log = Logger.getLogger(MigrationUtils.class);

    public static String getStackTrace(Throwable throwable)throws Exception {
	Writer writer = new StringWriter();
	PrintWriter printWriter = new PrintWriter(writer);
	throwable.printStackTrace(printWriter);
	String stackTrace = writer.toString();
	    writer.close();
	printWriter.close();
	return stackTrace;
    }

    /**
     * formatAmount - format amount on US currency format
     * @param amount
     * @return formattedAmount - String
     */
    public static String formatAmount(double amount)throws Exception {
	NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
	return formatter.format(amount);
    }

    /**
     * roundAmount - round amount to two decimal
     * @param amount
     * @return roundedAmount - double
     */
    public static Double roundAmount(double amount)throws Exception {
	NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
	String roundedAmount = formatter.format(amount);
	return Double.parseDouble(roundedAmount.replace(",", ""));
    }

    public static Double difference(Double double1, Double double2)throws Exception {
	return BigDecimal.valueOf(roundAmount(double1)).subtract(BigDecimal.valueOf(roundAmount(double2))).doubleValue();
    }

    public static boolean between(Double value, Double lhs, Double rhs)throws Exception {
	return BigDecimal.valueOf(value).compareTo(BigDecimal.valueOf(lhs)) >= 0 && BigDecimal.valueOf(value).compareTo(BigDecimal.valueOf(rhs)) <= 0;
    }

    /**
     * createFolder -	create if not exists
     * @param folder
     */
    public static void createFolder(File folder) {
	if (!folder.exists()) {
	    folder.mkdirs();
	}
    }

    /**
     * emptyFolder -	empty the if exists
     * @param folder
     */
    public static void emptyFolder(File folder) {
	if (folder.exists()) {
	    File[] files = folder.listFiles(new FilenameFilter("csv"));
	    if (null != files && files.length > 0) {
		for (File file : files) {
		    file.delete();
		}
	    }
	}
    }

    /**
     * closeFtp - close ftp client
     * @param ftpClient
     * @throws Exception
     */
    public static void closeFtp(FTPClient ftpClient) throws Exception {
	if (null != ftpClient) {
	    ftpClient.logout();
	    ftpClient.abort();
	}
    }

    /**
     * closeInputStream - close input stream
     * @param inputStream
     * @throws Exception
     */
    public static void closeInputStream(InputStream inputStream) throws Exception {
	if (null != inputStream) {
	    inputStream.close();
	}
    }

    /**
     * closeStatement - close output stream
     * @param outputStream
     * @throws Exception
     */
    public static void closeOutputStream(OutputStream outputStream) throws Exception {
	if (null != outputStream) {
	    outputStream.close();
	}
    }

    /**
     * closeStatement - close the statement if exists
     * @param statement
     * @throws SQLException
     */
    public static void closeStatement(PreparedStatement statement) throws SQLException {
	if (null != statement) {
	    statement.close();
	}
    }

    /**
     * closeResult - close the result if exists
     * @param result
     * @throws SQLException
     */
    public static void closeResult(ResultSet result) throws SQLException {
	if (null != result) {
	    result.close();
	}
    }

    /**
     * parseDate - parse the date from the source using the pattern
     * @param source
     * @param pattern
     * @return date - Date
     * @throws Exception
     */
    public static Date parseDate(String source, String pattern) throws Exception {
	if (null != source && !source.trim().isEmpty() && !source.equals("0")) {
	    DateFormat format = new SimpleDateFormat(pattern);
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(format.parse(source));
	    return new Date(calendar.getTimeInMillis());
	}
	return null;
    }

    /**
     * formatDate - format the date from the source using the pattern
     * @param source
     * @param pattern
     * @return date - String
     * @throws Exception
     */
    public static String formatDate(Date source, String pattern) throws Exception {
	if (null != source) {
	    DateFormat format = new SimpleDateFormat(pattern);
	    return format.format(source);
	}
	return null;
    }

    /**
     * isEmpty - check whether the string is null or empty
     * @param string
     * @return true or false
     */
    public static boolean isEmpty(String string) {
	return null == string || string.trim().isEmpty();
    }

    /**
     * isEmpty - check whether the integer is null or zero
     * @param integer
     * @return true or false
     */
    public static boolean isEmpty(Integer integer) {
	return null == integer || integer == 0;
    }

    /**
     * isEmpty - check whether the double is null or zero
     * @param dbl
     * @return true or false
     */
    public static boolean isEmpty(Double dbl)throws Exception {
	return null == dbl || BigDecimal.valueOf(roundAmount(dbl)).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * isNotEmpty - check whether the string is not null and not empty
     * @param str
     * @return true or false
     */
    public static boolean isNotEmpty(String str) {
	return null != str && !str.trim().isEmpty();
    }

    /**
     * isNotEmpty - check whether the integer is not null and not zero
     * @param integer
     * @return true or false
     */
    public static boolean isNotEmpty(Integer integer) {
	return null != integer && integer != 0;
    }

    /**
     * isNotEmpty - check whether the integer is not null and not zero
     * @param dbl
     * @return true or false
     */
    public static boolean isNotEmpty(Double dbl)throws Exception {
	return null != dbl && BigDecimal.valueOf(roundAmount(dbl)).compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * isEqual - check whether the given two string parameters are equal or not
     * @param str1
     * @param str2
     * @return true or false
     */
    public static boolean isEqual(String str1, String str2) {
	return (null == str1 && null == str2) || (null != str1 && null != str2 && str1.trim().equals(str2.trim()));
    }

    /**
     * isEqual - check whether the given two string parameters are equal or not by ignoring case
     * @param str1
     * @param str2
     * @return true or false
     */
    public static boolean isEqualIgnoreCase(String str1, String str2) {
	return (null == str1 && null == str2) || (null != str1 && null != str2 && str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    /**
     * isEqual - check the first string is equal to any of the other two
     * @param str1
     * @param str2
     * @param str3
     * @return true or false
     */
    public static boolean isEqual(String str1, String str2, String str3) {
	return isEqual(str1, str2) || isEqual(str1, str3);
    }

    /**
     * isEqualIgnoreCase - check the first string is equal to any of the other two by ignoring case
     * @param str1
     * @param str2
     * @param str3
     * @return
     */
    public static boolean isEqualIgnoreCase(String str1, String str2, String str3) {
	return isEqualIgnoreCase(str1, str2) || isEqualIgnoreCase(str1, str3);
    }

    /**
     * isEqual - check whether the given two integer parameters are equal or not
     * @param integer1
     * @param integer2
     * @return true or false
     */
    public static boolean isEqual(Integer integer1, Integer integer2) {
	return (null == integer1 && null == integer2)
		|| (null != integer1 && null != integer2 && integer1 == integer2);
    }

    /**
     * isEqual - check whether the given two double parameters are equal or not
     * @param double1
     * @param double2
     * @return true or false
     */
    public static boolean isEqual(Double double1, Double double2)throws Exception {
	return (null == double1 && null == double2) || (null != double1 && null != double2
		&& BigDecimal.valueOf(roundAmount(double1)).compareTo(BigDecimal.valueOf(roundAmount(double2))) == 0);
    }

    /**
     * isNotEqual - check whether the given two string parameters are not equal or not
     * @param str1
     * @param str2
     * @return true or false
     */
    public static boolean isNotEqual(String str1, String str2) {
	return (null == str1 && null != str2) || (null != str1 && null == str2) || (null != str1 && null != str2 && !str1.trim().equals(str2.trim()));
    }

    /**
     * isNotEqual - check whether the given two string parameters are not equal or not by ignoring case
     * @param str1
     * @param str2
     * @return true or false
     */
    public static boolean isNotEqualIgnoreCase(String str1, String str2) {
	return (null == str1 && null != str2) || (null != str1 && null == str2)
		|| (null != str1 && null != str2 && !str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    /**
     * isNotEqual - check whether the given two string parameters are not equal or not by ignoring empty
     * @param str1
     * @param str2
     * @return true or false
     */
    public static boolean isNotEqualIgnoreEmpty(String str1, String str2) {
	return (null == str1 && null != str2) || (null != str1 && null == str2)
		|| (null != str1 && !str1.trim().isEmpty() && null != str2 && !str2.trim().isEmpty()
		&& !str1.toUpperCase().trim().equals(str2.toUpperCase().trim()));
    }

    /**
     * isNotEqual - check whether the given two integer parameters are not equal or not
     * @param integer1
     * @param integer2
     * @return true or false
     */
    public static boolean isNotEqual(Integer integer1, Integer integer2) {
	return (null == integer1 && null != integer2) || (null != integer1 && null == integer2)
		|| (null != integer1 && null != integer2 && integer1 != integer2);
    }

    /**
     * isNotEqual - check whether the given double string parameters are not equal or not
     * @param double1
     * @param double2
     * @return true or false
     */
    public static boolean isNotEqual(Double double1, Double double2)throws Exception {
	return (null == double1 && null != double2) || (null != double1 && null == double2) || (null != double1 && null != double2
		&& BigDecimal.valueOf(roundAmount(double1)).compareTo(BigDecimal.valueOf(roundAmount(double2))) != 0);
    }

    /**
     * isStartsWith - check whether the string is starts with the prefix value
     * @param str
     * @param prefix
     * @return true or false
     */
    public static boolean isStartsWith(String str, String prefix) {
	return null != str && str.startsWith(prefix);
    }

    /**
     * in - check whether the string is present in the group of comma separated strings
     * @param strs
     * @param str
     * @return true or false
     */
    public static boolean in(String strs, String str) {
	return null != strs && null != str && Arrays.asList(strs.split(",")).contains(str);
    }

    /**
     * in - check whether the string is present in the group of strings
     * @param str
     * @param strs
     * @return true or false
     */
    public static boolean in(String str, String... strs) {
	return null != strs && null != str && Arrays.asList(strs).contains(str);
    }

    /**
     * in - check whether the integer is present in the group of integers
     * @param integer
     * @param integers
     * @return true or false
     */
    public static boolean in(Integer integer, Integer... integers) {
	return null != integers && null != integer && Arrays.asList(integers).contains(integer);
    }

    /**
     * in - check whether the integer is present in the group of integers
     * @param dbl
     *            param integers
     * @return true or false
     */
    public static boolean in(Double dbl, Double... dbls) {
	return null != dbls && null != dbl && Arrays.asList(dbls).contains(dbl);
    }

    /**
     * in - check whether the integer value is between two given values
     * @param integer
     * @param lhs
     * @param rhs
     * @return true or false
     */
    public static boolean between(Integer integer, Integer lhs, Integer rhs) {
	return null != integer && null != lhs && null != rhs && (integer >= lhs && integer <= rhs);
    }

    /**
     * isAllNotEqual - check whether the given string parameters are not equal or not
     * @param str
     * @param strs
     * @return true or false
     */
    public static boolean isAllNotEqual(String str, String... strs) {
	return null != str && null != strs && !Arrays.asList(strs).contains(str);
    }

    /**
     * indexOf - index of string in the List of string
     * @param strings
     * @param string
     * @return -1 if strings is null or correct index of string
     */
    public static int indexOf(List<String> strings, String string) {
	if (strings == null) {
	    return -1;
	}
	return strings.indexOf(string);
    }

    /**
     * createErrorFile - create error file from the source row
     * @param readRow
     * @param errorFile
     * @throws Exception
     */
    public static void createErrorFile(DataRow readRow, File errorFile) throws Exception {
	DataFile writeData = DataFile.createWriter("8859_1", false);
	writeData.setDataFormat(new CSVFormat());
	writeData.open(errorFile);
	DataRow errorRow = writeData.next();
	for (int index = 0; index < readRow.size(); index++) {
	    errorRow.add(readRow.getString(index));
	}
	closeCsvFile(writeData);
    }

    /**
     * copyFile - copy file from source to destination
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
	FileChannel sourceChannel = null;
	FileChannel destinationChannel = null;
	try {
	    if (destination.exists()) {
		destination.delete();
	    }
	    sourceChannel = new FileInputStream(source).getChannel();
	    destinationChannel = new FileOutputStream(destination).getChannel();
	    int maxCount = (64 * 1024 * 1024) - (32 * 1024);// magic number for Windows, 64Mb - 32Kb
	    long size = sourceChannel.size();
	    long position = 0;
	    while (position < size) {
		position += sourceChannel.transferTo(position, maxCount, destinationChannel);
	    }
	} catch (IOException e) {
	    throw e;
	} finally {
	    if (null != sourceChannel) {
		sourceChannel.close();
	    }
	    if (null != destinationChannel) {
		destinationChannel.close();
	    }
	}
    }

    /**
     * moveFile - move file from source to destination
     * @param source
     * @param destination
     * @throws Exception
     */
    public static void moveFile(File source, File destination) throws Exception {
	if (destination.exists()) {
	    destination.delete();
	}
	source.renameTo(destination);
    }

    /**
     * copyRemoteFiles - copy files from remote using ftp
     * @param ip
     * @param userName
     * @param password
     * @param sourcePath
     * @param destinationPath
     * @throws Exception
     */
    public static void copyRemoteFiles(String ip, String userName, String password, String sourcePath, String destinationPath) throws Exception {
	OutputStream outputStream = null;
	FTPClient ftpClient = null;
	try {
	    File destination = new File(destinationPath);
	    ftpClient = new FTPClient();
	    log.info("Connecting to ftp..." + new Timestamp(System.currentTimeMillis()));
	    ftpClient.connect(ip);
	    ftpClient.login(userName, password);
	    FTPFile[] files = ftpClient.listFiles(sourcePath);
	    if (null != files && files.length > 0) {
		log.info("Moving files " + sourcePath + " to " + destinationPath + " started..." + new Timestamp(System.currentTimeMillis()));
		for (FTPFile file : files) {
		    outputStream = new FileOutputStream(new File(destination, file.getName()));
		    String sourceFilePath = sourcePath + file.getName();
		    ftpClient.retrieveFile(sourceFilePath, outputStream);
		    closeOutputStream(outputStream);
		    ftpClient.deleteFile(sourceFilePath);
		}
		log.info("Moving files from " + sourcePath + " to " + destinationPath + " finished..." + new Timestamp(System.currentTimeMillis()));
	    } else {
		log.info("No files found in " + sourcePath);
	    }
	} catch (Exception e) {
	    log.info("Moving files from " + sourcePath + " to " + destinationPath + " failed..." + new Timestamp(System.currentTimeMillis()));
	    throw e;
	} finally {
	    closeFtp(ftpClient);
	    closeOutputStream(outputStream);
	}
    }

    /**
     * moveLocalFiles - moving files from source to destination in local system
     * @param sourcePath
     * @param destinationPath
     * @throws Exception
     */
    public static void moveLocalFiles(String sourcePath, String destinationPath) throws Exception {
	    //Get all csv files from open ar based on filenamefilter
	    File source = new File(sourcePath);
	    File destination = new File(destinationPath);
	    File[] files = source.listFiles(new FilenameFilter("csv"));
	    if (null != files && files.length > 0) {
		for (File file : files) {
		    moveFile(file, new File(destination, file.getName()));
		}
	    } else {
	    }
    }

    /**
     * closeCsvFile - close the csv file
     * @param csvFile
     * @throws Exception
     */
    public static void closeCsvFile(DataFile csvFile) throws Exception {
	if (null != csvFile) {
	    csvFile.close();
	}
    }

    /**
     * getTerminal - get terminal based on suffix, shipmentType,terminals and drcpt
     * @param suffix
     * @param shipmentType
     * @param billingTerminal
     * @param loadingTerminal
     * @param drcptTerminal
     * @param drcpt
     * @return terminal - map
     */
    public static Map<String, String> getTerminal(String suffix, String shipmentType,
	    String billingTerminal, String loadingTerminal, String drcptTerminal, String drcpt) {
	Map<String, String> terminal = new HashMap<String, String>();
	if ("05".equals(drcpt)) {
	    terminal.put("terminalNumber", drcptTerminal);
	    terminal.put("terminalType", "lcl_export_dockreceipt");
	} else if (isEqualIgnoreCase(suffix, "B")) {
	    terminal.put("terminalNumber", billingTerminal);
	    terminal.put("terminalType", "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_billing"
		    : "FCLI".equalsIgnoreCase(shipmentType) ? "fcl_import_billing"
		    : "LCLE".equalsIgnoreCase(shipmentType) ? "lcl_export_billing"
		    : "LCLI".equalsIgnoreCase(shipmentType) ? "lcl_import_billing"
		    : "AIRE".equalsIgnoreCase(shipmentType) ? "air_export_billing"
                    : "AIRI".equalsIgnoreCase(shipmentType) ? "air_import_billing"
		    : "air_export_billing");
            
                      //   B-FCLE,FCLI,LCLE,LCLI,AIRE,AIRI

                      //   L- FCLE,FCLI,LCLE,LCLI,AIRE,AIRI,INLE

                      //   D- FCLE,LCLE,AIRE
            
	} else if (isEqualIgnoreCase(suffix, "L")) {
	    terminal.put("terminalNumber", loadingTerminal);
	    terminal.put("terminalType", "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_loading" 
                    : "FCLI".equalsIgnoreCase(shipmentType) ? "fcl_import_loading"
		    : "LCLE".equalsIgnoreCase(shipmentType) ? "lcl_export_loading"
		    : "LCLI".equalsIgnoreCase(shipmentType) ? "lcl_import_loading"
		    : "AIRE".equalsIgnoreCase(shipmentType) ? "air_export_loading"
                    : "AIRI".equalsIgnoreCase(shipmentType) ? "air_import_loading"  
		    : "inland_export_loading");
	} else {
	    terminal.put("terminalNumber", drcptTerminal);
	    terminal.put("terminalType", "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_dockreceipt"
		    : "LCLE".equalsIgnoreCase(shipmentType) ? "13".equals(drcpt) ? "lcl_export_billing"
		    : "lcl_export_dockreceipt"
		    : "air_export_dockreceipt");
	}
	return terminal;
    }

    /**
     * isBillToParty - validate the bill to party for FCL BL
     * @param account
     * @param billToCode
     * @param shipper
     * @param forwarder
     * @param consignee
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, String billToCode, String shipper, String forwarder, String consignee, String agent, String thirdParty) {
	String billToParty = "S".equals(billToCode) ? shipper
		: "A".equals(billToCode) ? agent
		: "T".equals(billToCode) ? thirdParty
		: "F".equals(billToCode) ? forwarder
		: "C".equals(billToCode) ? consignee
		: "";
	return account.equals(billToParty);
    }

    /**
     * isBillToParty - validate bill to party for FCL BL Corrections
     * @param account
     * @param prevCnBillToParty
     * @param cnBillToCode
     * @param bsBillToCode
     * @param shipper
     * @param forwarder
     * @param consignee
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, String prevCnBillToParty, String cnBillToCode, String bsBillToCode,
	    String shipper, String forwarder, String consignee, String agent, String thirdParty) {
	String billToParty = account.equals(prevCnBillToParty) ? prevCnBillToParty
		: "S".equals(cnBillToCode) ? shipper
		: "A".equals(cnBillToCode) ? agent
		: "T".equals(cnBillToCode) ? thirdParty
		: "F".equals(cnBillToCode) ? forwarder
		: "C".equals(cnBillToCode) ? consignee
		: "";
	return account.equals(billToParty);
    }

    /**
     * isBillToParty - validate bill to party for FCL BL Corrections
     * @param account
     * @param invoiceAmount
     * @param prevCnBillToParty
     * @param newBillToParty
     * @param bsBillToCode
     * @param shipper
     * @param forwarder
     * @param consignee
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, double invoiceAmount, String prevCnBillToParty, String newBillToParty,
	    String cnBillToCode, String bsBillToCode, String shipper, String forwarder, String consignee, String agent, String thirdParty) {
	String billToParty = invoiceAmount > 0 && account.equals(newBillToParty) ? newBillToParty
		: account.equals(prevCnBillToParty) && !"C".equals(bsBillToCode) ? prevCnBillToParty
		: "S".equals(bsBillToCode) ? shipper
		: "A".equals(bsBillToCode) ? agent
		: "T".equals(bsBillToCode) ? thirdParty
		: "F".equals(bsBillToCode) ? forwarder
		: "C".equals(bsBillToCode) ? consignee
		: prevCnBillToParty;
	return account.equals(billToParty);
    }

    /**
     * isBillToParty - validate bill to party for LCL or AIR BL Corrections
     * @param account
     * @param bsPcbCode
     * @param pcb
     * @param pcbCode
     * @param billToCode
     * @param prevCnBillToParty
     * @param shipper
     * @param forwarder
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, String bsPcbCode, String pcb, String pcbCode, String billToCode,
	    String prevCnBillToParty, String shipper, String forwarder, String agent, String thirdParty) {
	String billToParty = account.equals(prevCnBillToParty) ? prevCnBillToParty
		: (("B".equals(bsPcbCode) && "P".equals(pcb)) || "P".equals(bsPcbCode)) ? "S".equals(billToCode) ? shipper
		: "F".equals(billToCode) ? forwarder
		: "T".equals(billToCode) ? thirdParty
		: ""
		: ("C".equals(bsPcbCode) || (("B".equals(bsPcbCode) || "P".equals(bsPcbCode)) && "C".equals(pcb))) ? agent
		: "";
	return account.equals(billToParty);
    }

    /**
     * isBillToParty - validate bill to party for LCL or AIR BL Corrections
     * @param account
     * @param invoiceAmount
     * @param newBillToParty
     * @param bsPcbCode
     * @param pcb
     * @param pcbCode
     * @param bsBillToCode
     * @param prevCnBillToParty
     * @param shipper
     * @param forwarder
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, double invoiceAmount, String newBillToParty,
	    String bsPcbCode, String pcb, String pcbCode, String bsBillToCode, String prevCnBillToParty,
	    String shipper, String forwarder, String agent, String thirdParty) {
	String billToParty = invoiceAmount > 0 && account.equals(newBillToParty) ? newBillToParty
		: account.equals(prevCnBillToParty) ? prevCnBillToParty
		: (("B".equals(bsPcbCode) && "C".equals(pcb)) || ("P".equals(bsPcbCode) && "C".equals(pcb)) || "C".equals(bsPcbCode)) ? agent
		: ((("B".equals(bsPcbCode) || "B".equals(pcbCode)) && "P".equals(pcb)) || "P".equals(pcbCode)) ? "S".equals(bsBillToCode) ? shipper
		: "F".equals(bsBillToCode) ? forwarder
		: "T".equals(bsBillToCode) ? thirdParty
		: ""
		: prevCnBillToParty;
	return account.equals(billToParty);
    }

    /**
     * isBillToParty - validate bill to party for LCL or AIR BL
     * @param account
     * @param pcbCode
     * @param pcb
     * @param billToCode
     * @param shipper
     * @param forwarder
     * @param agent
     * @param thirdParty
     * @return boolean
     */
    public static boolean isBillToParty(String account, String pcbCode, String pcb, String billToCode,
	    String shipper, String forwarder, String agent, String thirdParty) {
	String billToParty = ("C".equals(pcbCode) || ("B".equals(pcbCode) && "C".equals(pcb))) ? agent
		: (("B".equals(pcbCode) && "P".equals(pcb)) || "P".equals(pcbCode)) ? "S".equals(billToCode) ? shipper
		: "F".equals(billToCode) ? forwarder
		: "T".equals(billToCode) ? thirdParty
		: ""
		: "";
	return account.equals(billToParty);
    }
}

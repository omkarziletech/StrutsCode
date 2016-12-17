package com.logiware.datamigration;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class LoadOpenArToLogiware {

    //Global fields
    private static final Logger log = Logger.getLogger(LoadOpenArToLogiware.class);
    private String reprocessLog = "success";
    private Properties prop;
    private Connection bsConn;
    private Connection lwConn;
    private StringBuilder errors = new StringBuilder();
    private StringBuilder warnings = new StringBuilder();
    private Map<String, Object> values = new HashMap<String, Object>();
    private int reconnectCount = 0;
    private int totalReconnectCount = 0;
    private boolean canLoadAr;
    private String propertyPath = "/com/logiware/datamigration/dbconnection.properties";

    /**
     * loadProperties - load the properties like db connection details,ftp connection details
     * @throws IOException
     */
    private void loadProperties() throws Exception {
	prop = new Properties();
	prop.load(getClass().getResourceAsStream(propertyPath));
    }

    /**
     * connectToBluescreen - setup connection with bluescreen
     * @throws Exception
     */
    private void connectToBluescreen() throws Exception {
	try {
	    log.info("Connecting to Bluescreen database..." + new Timestamp(System.currentTimeMillis()));
	    Class.forName(prop.getProperty("driver"));
	    String url = prop.getProperty("url2");
	    String username = prop.getProperty("username2");
	    String password = prop.getProperty("password2");
	    bsConn = DriverManager.getConnection(url, username, password);
	    log.info("Bluescreen database connected successfully...");
	} catch (SQLException e) {
	    reconnectToBluescreen();
	} catch (Exception e) {
	    log.info("Bluescreen database connection failed...");
	    throw e;
	}
    }

    /**
     * connectToLogiware - setup connection with logiware
     * @throws Exception
     */
    private void connectToLogiware() throws Exception {
	try {
	    log.info("Connecting to Logiware database..." + new Timestamp(System.currentTimeMillis()));
	    Class.forName(prop.getProperty("driver"));
	    String url = prop.getProperty("url1");
	    String username = prop.getProperty("username1");
	    String password = prop.getProperty("password1");
	    lwConn = DriverManager.getConnection(url, username, password);
	    log.info("Logiware database connected successfully...");
	} catch (SQLException e) {
	    reconnectToLogiware();
	} catch (Exception e) {
	    log.info("Logiware database connection failed...");
	    throw e;
	}
    }

    /**
     * reconnectToBluescreen - setup connection with bluescreen
     * @throws Exception
     */
    private void reconnectToBluescreen() throws Exception {
	try {
	    if (totalReconnectCount < 10) {
		reconnectCount++;
		totalReconnectCount++;
		Thread.sleep(2 * 1000);
		log.info("Reconnecting Bluescreen database..." + new Timestamp(System.currentTimeMillis()));
		Class.forName(prop.getProperty("driver"));
		String url = prop.getProperty("url2");
		String username = prop.getProperty("username2");
		String password = prop.getProperty("password2");
		bsConn = DriverManager.getConnection(url, username, password);
		log.info("Bluescreen database reconnected successfully...");
		reconnectCount = 0;
	    } else {
		throw new Exception();
	    }
	} catch (SQLException e) {
	    if (reconnectCount < 5) {
		reconnectToBluescreen();
	    } else {
		reconnectCount = 0;
		throw new Exception(MigrationUtils.getStackTrace(e));
	    }
	} catch (Exception e) {
	    log.info("Bluescreen database reconnection failed...");
	    throw e;
	}
    }

    /**
     * reconnectToLogiware - setup connection with logiware
     * @throws Exception
     */
    private void reconnectToLogiware() throws Exception {
	try {
	    if (totalReconnectCount < 10) {
		reconnectCount++;
		totalReconnectCount++;
		Thread.sleep(2 * 1000);
		log.info("reconnecting Logiware database..." + new Timestamp(System.currentTimeMillis()));
		Class.forName(prop.getProperty("driver"));
		String url = prop.getProperty("url1");
		String username = prop.getProperty("username1");
		String password = prop.getProperty("password1");
		lwConn = DriverManager.getConnection(url, username, password);
		log.info("Logiware database reconnected successfully...");
		reconnectCount = 0;
	    } else {
		throw new Exception();
	    }
	} catch (SQLException e) {
	    if (reconnectCount < 5) {
		reconnectToLogiware();
	    } else {
		reconnectCount = 0;
		throw new Exception(MigrationUtils.getStackTrace(e));
	    }
	} catch (Exception e) {
	    log.info("Logiware database reconnection failed...");
	    throw e;
	}
    }

    /**
     * connect - setup connection to both bluescreen and logiware
     * @throws Exception
     */
    private void connect() throws Exception {
	connectToBluescreen();
	connectToLogiware();
    }

    /**
     * disconnectToLogiware - abort connection with logiware
     * @throws Exception
     */
    private void disconnectToLogiware() throws Exception {
	try {
	    log.info("Disconnecting to Logiware database..." + new Timestamp(System.currentTimeMillis()));
	    lwConn.close();
	    log.info("Logiware database disconnected successfully...");
	} catch (Exception e) {
	    log.info("Logiware database disconnection failed...");
	    throw e;
	}
    }

    /**
     * disconnectToBluescreen - abort connection with bluescreen
     * @throws Exception
     */
    private void disconnectToBluescreen() throws Exception {
	try {
	    log.info("Disconnecting to Bluescreen database..." + new Timestamp(System.currentTimeMillis()));
	    bsConn.close();
	    log.info("Bluescreen database disconnected successfully...");
	} catch (Exception e) {
	    log.info("Bluescreen database disconnection failed...");
	    throw e;
	}
    }

    /**
     * disconnect - abort connection to both bluescreen and logiware
     * @throws Exception
     */
    private void disconnect() throws Exception {
	disconnectToLogiware();
	disconnectToBluescreen();
    }

    /**
     * changeTradingPartner - change trading partner for disabled account
     * database - logiware and table - trading_partner, cust_accounting, genericcode_dup
     * @param forwardAccount
     * @throws Exception
     */
    private void changeTradingPartner(String forwardAccount) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    String customerName = null;
	    String customerNumber = null;
	    String creditHold = null;
	    StringBuilder query = new StringBuilder("select trim(tp.acct_name) as cust_name,trim(tp.acct_no) as cust_no,");
	    query.append("if(ge.codedesc='In Good Standing','N','Y') as credit_hold");
	    query.append(" from trading_partner tp");
	    query.append(" left join cust_accounting ca on ca.acct_no=tp.acct_no");
	    query.append(" left join genericcode_dup ge on ge.id=ca.credit_status");
	    query.append(" where tp.type!='master' and tp.acct_no=? limit 1");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, forwardAccount);
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next()) {
		customerName = lwResult.getString("cust_name");
		customerNumber = lwResult.getString("cust_no");
		creditHold = lwResult.getString("credit_hold");
	    }
	    if (MigrationUtils.isNotEmpty(customerName) && MigrationUtils.isNotEmpty(customerNumber) && MigrationUtils.isNotEmpty(creditHold)) {
		values.put("customerName", customerName);
		values.put("customerNumber", customerNumber);
		values.put("creditHold", creditHold);
	    } else {
		canLoadAr = false;
		errors.append("<li>No trading partner found for this logiware account - ").append(forwardAccount).append("</li>");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    changeTradingPartner(forwardAccount);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setTradingPartner - setting trading partner from blue screen account
     * database - logiware and table - trading_partner, cust_accounting, genericcode_dup
     * @param bsAccountNo
     * @throws Exception
     */
    private void setTradingPartner(String bsAccountNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsAccountNo", bsAccountNo);
	    String customerName = null;
	    String customerNumber = null;
	    String creditHold = null;
	    String forwardAccount = null;
	    StringBuilder query = new StringBuilder("select trim(tp.acct_name) as cust_name,trim(tp.acct_no) as cust_no,");
	    query.append("if(ge.codedesc='In Good Standing','N','Y') as credit_hold,");
	    query.append("if(tp.disabled='Y' and tp.forward_account!='',tp.forward_account,'') as forward_account");
	    query.append(" from trading_partner tp");
	    query.append(" left join cust_accounting ca on ca.acct_no=tp.acct_no");
	    query.append(" left join genericcode_dup ge on ge.id=ca.credit_status");
	    query.append(" where type!='master' and eci_acct_no=? limit 1");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, bsAccountNo);
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next()) {
		customerName = lwResult.getString("cust_name");
		customerNumber = lwResult.getString("cust_no");
		creditHold = lwResult.getString("credit_hold");
		forwardAccount = lwResult.getString("forward_account");
	    } else {
		MigrationUtils.closeResult(lwResult);
		MigrationUtils.closeStatement(lwStatement);
		query = new StringBuilder("select trim(tp.acct_name) as cust_name,trim(tp.acct_no) as cust_no,");
		query.append("if(ge.codedesc='In Good Standing','N','Y') as credit_hold,");
		query.append("if(tp.disabled='Y' and tp.forward_account!='',tp.forward_account,'') as forward_account");
		query.append(" from trading_partner tp");
		query.append(" left join cust_accounting ca on ca.acct_no=tp.acct_no");
		query.append(" left join genericcode_dup ge on ge.id=ca.credit_status");
		query.append(" where type!='master' and ecifwno=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsAccountNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    customerName = lwResult.getString("cust_name");
		    customerNumber = lwResult.getString("cust_no");
		    creditHold = lwResult.getString("credit_hold");
		    forwardAccount = lwResult.getString("forward_account");
		}
	    }
	    if (MigrationUtils.isNotEmpty(forwardAccount)) {
		changeTradingPartner(forwardAccount);
	    } else if (MigrationUtils.isNotEmpty(customerName) && MigrationUtils.isNotEmpty(customerNumber) && MigrationUtils.isNotEmpty(creditHold)) {
		values.put("customerName", customerName);
		values.put("customerNumber", customerNumber);
		values.put("creditHold", creditHold);
	    } else {
		canLoadAr = false;
		errors.append("<li>No trading partner found for this blue screen account - ").append(bsAccountNo).append("</li>");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setTradingPartner(bsAccountNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setInvoiceOrBl - setting bill of lading and invoice number
     * @param billOfLading
     * @param invoiceNumber
     */
    private void setInvoiceOrBl(String billOfLading, String invoiceNumber) {
	values.put("billOfLading", MigrationUtils.isNotEmpty(billOfLading) ? billOfLading : invoiceNumber);
	values.put("invoiceNumber", invoiceNumber);
    }

    /**
     * setInvoiceAmount - set invoice amount
     * @param invoiceAmount
     */
    private void setInvoiceAmount(double invoiceAmount) throws Exception {
	values.put("invoiceAmount", MigrationUtils.roundAmount(invoiceAmount));
    }

    /**
     * setTransactionType - set transaction type using blue screen transaction type
     * @param type
     */
    private void setTransactionType(String type) {
	String transactype = "0".equals(type) ? "VOID" : "1".equals(type) ? "LCL BL" : "3".equals(type) ? "CN" : "4".equals(type) ? "INV" : "FCL BL";
	values.put("transactionType", transactype);
    }

    /**
     * setCreatedDate - set created date from the bluescreen created date
     * @param date
     * @throws Exception
     */
    private void setCreatedDate(String date) throws Exception {
	values.put("createdDate", MigrationUtils.isNotEmpty(date) ? MigrationUtils.parseDate(date, "yyMMdd") : new Date(System.currentTimeMillis()));
    }

    /**
     * setTransactionDate - set transaction date from the bluescreen invoice date
     * @param bsTransactionDate
     * @throws Exception
     */
    private void setTransactionDate(String bsTransactionDate) throws Exception {
	values.put("transactionDate", MigrationUtils.parseDate(bsTransactionDate, "yyyyMMdd"));
    }

    /**
     * isPeriodOpen - checking the period is open or not for the given invoice date
     * database - logiware and table - fiscal_period
     * @param invoiceDate
     * @return boolean - status
     * @throws Exception
     */
    private boolean isOpenPeriod(String invoiceDate) throws Exception {
	boolean status = false;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    StringBuilder query = new StringBuilder("select status from fiscal_period");
	    query.append(" where date_format(start_date,'%Y%m%d')<='").append(invoiceDate).append("'");
	    query.append(" and date_format(end_date,'%Y%m%d')>='").append(invoiceDate).append("'");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwResult = lwStatement.executeQuery();
	    status = lwResult.next() && "Open".equalsIgnoreCase(lwResult.getString("status")) ? true : false;
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    status = isOpenPeriod(invoiceDate);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return status;
	}
    }

    /**
     * getNextOpenPeriodDate - getting the next open period's start date
     * database - logiware and table - fiscal_period
     * @param invoiceDate
     * @return String - startDate
     * @throws Exception
     */
    private String getNextOpenPeriodDate(String invoiceDate) throws Exception {
	String startDate = null;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    String periodDis = invoiceDate.replaceAll("[^0-9]", "").substring(0, 6);
	    StringBuilder query = new StringBuilder("select date_format(start_date,'%Y%m%d') as start_date from fiscal_period");
	    query.append(" where status='Open' and period_dis rlike '[0-9]+$' and period_dis>'").append(periodDis).append("'");
	    query.append(" order by year,period limit 1");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwResult = lwStatement.executeQuery();
	    startDate = lwResult.next() ? lwResult.getString("start_date") : null;
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    startDate = getNextOpenPeriodDate(invoiceDate);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return startDate;
	}
    }

    /**
     * setPostedDate - set posted date from the invoice date
     * @param date
     * @throws Exception
     */
    private void setPostedDate(String date) throws Exception {
	Date postedDate = MigrationUtils.parseDate(isOpenPeriod(date) ? date : getNextOpenPeriodDate(date), "yyyyMMdd");
	values.put("postedDate", postedDate);
    }

    /**
     * setInvoiceDate - set invoice date from the bluescreen invoice date
     * @param bsInvoiceDate
     * @throws Exception
     */
    private void setInvoiceDate(String bsTransactionDate) throws Exception {
	values.put("invoiceDate", MigrationUtils.parseDate(bsTransactionDate, "yyyyMMdd"));
    }

    /**
     * setBookingNumber - setting booking number using either invoice number or bl number
     * database - bluescreen and table - invdrs
     * @param invoiceNumber
     *                      param billOfLading
     * @throws Exception
     */
    private void setBookingNumber(String billOfLading, String invoiceNumber) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    if (MigrationUtils.isNotEmpty(invoiceNumber)) {
		String query = "select trim(drcnum) as booking_number from invdrs where cmpnum=? and trmnum=? and invnum=? limit 1";
		bsStatement = bsConn.prepareStatement(query);
		bsStatement.setString(1, invoiceNumber.substring(0, 2));
		bsStatement.setString(2, invoiceNumber.substring(2, 4));
		bsStatement.setString(3, invoiceNumber.substring(4));
		bsResult = bsStatement.executeQuery();
		values.put("bookingNumber", bsResult.next() ? bsResult.getString("booking_number") : null);
	    } else if (MigrationUtils.isStartsWith(invoiceNumber, "04")) {
		values.put("bookingNumber", billOfLading);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setBookingNumber(billOfLading, invoiceNumber);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setShipper - set shipper from blue screen shipper account
     * database - logiware and table - trading_partner
     * @param bsShipperNo
     * @throws Exception
     */
    private void setShipper(String bsShipperNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsShipperNo", bsShipperNo);
	    if (MigrationUtils.isNotEmpty(bsShipperNo) && MigrationUtils.isNotEqual(bsShipperNo, "00000")) {
		String shipperName = null;
		String shipperNumber = null;
		StringBuilder query = new StringBuilder();
		query.append("select trim(tp.acct_name) as shipper_name,trim(tp.acct_no) as shipper_no");
		query.append(" from trading_partner tp where type!='master' and eci_acct_no=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsShipperNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    shipperName = lwResult.getString("shipper_name");
		    shipperNumber = lwResult.getString("shipper_no");
		}
		if (MigrationUtils.isNotEmpty(shipperName) && MigrationUtils.isNotEmpty(shipperNumber)) {
		    values.put("shipperName", shipperName);
		    values.put("shipperNumber", shipperNumber);
		} else {
		    warnings.append("<li>No shipper found for this  blue screen shipper account - ").append(bsShipperNo).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setShipper(bsShipperNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setConsignee - set consignee from blue screen consignee account
     * database - logiware and table - trading_partner
     * @param bsConsigneeNo
     * @throws Exception
     */
    private void setConsignee(String bsConsigneeNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsConsigneeNo", bsConsigneeNo);
	    if (MigrationUtils.isNotEmpty(bsConsigneeNo) && MigrationUtils.isNotEqual(bsConsigneeNo, "00000")) {
		String consigneeName = null;
		String consigneeNumber = null;
		StringBuilder query = new StringBuilder();
		query.append("select trim(tp.acct_name) as consignee_name,trim(tp.acct_no) as consignee_no from trading_partner tp");
		query.append(" where type!='master' and ecifwno=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsConsigneeNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    consigneeName = lwResult.getString("consignee_name");
		    consigneeNumber = lwResult.getString("consignee_no");
		}
		if (MigrationUtils.isNotEmpty(consigneeName) && MigrationUtils.isNotEmpty(consigneeNumber)) {
		    values.put("consigneeName", consigneeName);
		    values.put("consigneeNumber", consigneeNumber);
		} else {
		    warnings.append("<li>No consignee found for this  blue screen consignee account - ").append(bsConsigneeNo).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setConsignee(bsConsigneeNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setForwarder - set forwarder from blue screen forwarder account
     * database - logiware and table - trading_partner
     * @param bsForwarderNo
     * @throws Exception
     */
    private void setForwarder(String bsForwarderNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsForwarderNo", bsForwarderNo);
	    if (MigrationUtils.isNotEmpty(bsForwarderNo) && MigrationUtils.isNotEqual(bsForwarderNo, "00000")) {
		String forwarderName = null;
		String forwarderNumber = null;
		StringBuilder query = new StringBuilder();
		query.append("select trim(tp.acct_name) as forwarder_name,trim(tp.acct_no) as forwarder_no from trading_partner tp");
		query.append(" where type!='master' and eci_acct_no=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsForwarderNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    forwarderName = lwResult.getString("forwarder_name");
		    forwarderNumber = lwResult.getString("forwarder_no");
		}
		if (MigrationUtils.isNotEmpty(forwarderName) && MigrationUtils.isNotEmpty(forwarderNumber)) {
		    values.put("forwarderName", forwarderName);
		    values.put("forwarderNumber", forwarderNumber);
		} else {
		    warnings.append("<li>No forwarder found for this  blue screen forwarder account - ").append(bsForwarderNo).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setForwarder(bsForwarderNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setAgent - set agent from blue screen agent account
     * database - logiware and table - trading_partner
     * @param bsAgentNo
     * @throws Exception
     */
    private void setAgent(String bsAgentNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsAgentNo", bsAgentNo);
	    if (MigrationUtils.isNotEmpty(bsAgentNo) && MigrationUtils.isNotEqual(bsAgentNo, "00000")) {
		String agentName = null;
		String agentNumber = null;
		StringBuilder query = new StringBuilder();
		query.append("select trim(tp.acct_name) as agent_name,trim(tp.acct_no) as agent_no");
		query.append(" from trading_partner tp where type!='master' and eci_acct_no=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsAgentNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    agentName = lwResult.getString("agent_name");
		    agentNumber = lwResult.getString("agent_no");
		}
		if (MigrationUtils.isNotEmpty(agentName) && MigrationUtils.isNotEmpty(agentNumber)) {
		    values.put("agentName", agentName);
		    values.put("agentNumber", agentNumber);
		} else {
		    warnings.append("<li>No agent found for this  blue screen agent account - ").append(bsAgentNo).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setAgent(bsAgentNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * getRegion - get region from port number
     * database - bluescreen and table - ports
     * @param portNumber
     * @return
     * @throws Exception
     */
    private String getRegion(String portNumber) throws Exception {
	String region = null;
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    StringBuilder query = new StringBuilder("select trim(descod) as region from ports where prtnum=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, portNumber);
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		region = bsResult.getString("region");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    region = getRegion(portNumber);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	    return region;
	}
    }

    /**
     * setAgent - set agent from blue screen port number and final destination number
     * database - bluescreen and table - ports
     * @param portNumber
     * @param finalDestination
     * @param isFcl
     * @throws Exception
     */
    private void setAgent(String portNumber, String finalDestination, boolean isFcl) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    String bsAgentNo = null;
	    String regions = "01,02,03,09,11,12,13";
	    String region = getRegion(portNumber);
	    if (MigrationUtils.in(regions, region)
		    && MigrationUtils.isNotEqual(portNumber, finalDestination)
		    && MigrationUtils.isNotEqualIgnoreEmpty(finalDestination, "000")) {
		StringBuilder query = new StringBuilder("select trim(agentn) as agent_no");
		query.append(" from ports where ").append(isFcl ? "schedk" : "prtnum").append("=?");
		bsStatement = bsConn.prepareStatement(query.toString());
		bsStatement.setString(1, finalDestination);
		bsResult = bsStatement.executeQuery();
		if (bsResult.next()) {
		    bsAgentNo = bsResult.getString("agent_no");
		}
		if (MigrationUtils.isNotEmpty(bsAgentNo)) {
		    setAgent(bsAgentNo);
		} else {
		    warnings.append("<li>No blue screen agent found for this final destination - ").append(finalDestination).append("</li>");
		}
	    } else {
		StringBuilder query = new StringBuilder("select trim(agentn) as agent_no from ports where prtnum=?");
		bsStatement = bsConn.prepareStatement(query.toString());
		bsStatement.setString(1, portNumber);
		bsResult = bsStatement.executeQuery();
		if (bsResult.next()) {
		    bsAgentNo = bsResult.getString("agent_no");
		}
		if (MigrationUtils.isNotEmpty(bsAgentNo)) {
		    setAgent(bsAgentNo);
		} else {
		    warnings.append("<li>No blue screen agent found for this port number - ").append(portNumber).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setAgent(portNumber, finalDestination, isFcl);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setThirdParty - set third party from blue screen third party account
     * database - logiware and table - trading_partner
     * @param bsThirdPartyNo
     * @throws Exception
     */
    private void setThirdParty(String bsThirdPartyNo) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    values.put("bsThirdPartyNo", bsThirdPartyNo);
	    if (MigrationUtils.isNotEmpty(bsThirdPartyNo) && MigrationUtils.isNotEqual(bsThirdPartyNo, "00000")) {
		StringBuilder query = new StringBuilder();
		String thirdPartyName = null;
		String thirdPartyNumber = null;
		query.append("select trim(acct_name) as third_party_name,trim(acct_no) as third_party_no");
		query.append(" from trading_partner where type!='master' and eci_acct_no=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, bsThirdPartyNo);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    thirdPartyName = lwResult.getString("third_party_name");
		    thirdPartyNumber = lwResult.getString("third_party_no");
		}
		if (MigrationUtils.isNotEmpty(thirdPartyName) && MigrationUtils.isNotEmpty(thirdPartyNumber)) {
		    values.put("thirdPartyName", thirdPartyName);
		    values.put("thirdPartyNumber", thirdPartyNumber);
		} else {
		    warnings.append("<li>No third party found for this  blue screen third party account - ").append(bsThirdPartyNo).append("</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setThirdParty(bsThirdPartyNo);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setSteamShipLine - set steam ship line using blue screen ssline number
     * database - logiware and table - trading_partner
     * @param sslineNumber
     * @throws Exception
     */
    private void setSteamShipLine(String sslineNumber) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    if (MigrationUtils.isNotEqual(sslineNumber, "00000")) {
		lwStatement = lwConn.prepareStatement("select acct_no from trading_partner where ssline_number=? and type!='master'");
		lwStatement.setString(1, sslineNumber);
		lwResult = lwStatement.executeQuery();
		if (lwResult.next()) {
		    values.put("steamShipLine", lwResult.getString("acct_no"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    setSteamShipLine(sslineNumber);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setSteamShipLineFromContainer - set steam ship line using container number
     * database - bluescreen and table - unithd
     * @param containerNumber
     * @throws Exception
     */
    private void setSteamShipLineFromContainer(String containerNumber) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bResult = null;
	try {
	    if (MigrationUtils.isNotEmpty(containerNumber)) {
		String query = "select u.ssline as steam_ship_line from unithd u where u.trlnum=?";
		bsStatement = bsConn.prepareStatement(query);
		bsStatement.setString(1, containerNumber);
		bResult = bsStatement.executeQuery();
		if (bResult.next()) {
		    setSteamShipLine(bResult.getString("steam_ship_line"));
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setSteamShipLineFromContainer(containerNumber);
	} finally {
	    MigrationUtils.closeResult(bResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setVoyage - set voyage number using bl number
     * database - bluescreen and table - dract
     * @param billOfLading
     * @param shipmentType
     * @throws Exception
     */
    private void setVoyage(String billOfLading, String shipmentType) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    StringBuilder query = new StringBuilder("select concat(inlotm,inldtm,inlvy) as voyage_no,inlotm as loading_terminal");
	    query.append(" from dract where term=? and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7, 13));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		if (MigrationUtils.isNotEmpty(bsResult.getString("voyage_no"))) {
		    values.put("voyageNumber", bsResult.getString("voyage_no"));
		}
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")) {
		    if (MigrationUtils.isNotEmpty(bsResult.getString("loading_terminal"))) {
			values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		    } else if (MigrationUtils.isNotEmpty((String) values.get("voyageNumber"))) {
			values.put("loadingTerminal", ((String) values.get("voyageNumber")).substring(0, 2));
		    }
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setVoyage(billOfLading, shipmentType);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setContainer - set container and seal no
     * database - bluescreen and table - fcltrl
     * @param billOfLading
     * @throws Exception
     */
    private void setContainer(String billOfLading) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    //Get container no and seal no
	    StringBuilder query = new StringBuilder("select group_concat(fcltrl) as container_no,group_concat(fclsl) as seal_no");
	    query.append(" from fcltrl where terml=?  and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setContainer(billOfLading);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * getChargeCode - get logiware charge code from blue screen charge code
     * database - logiware and table - gl_mapping
     * @param bsChargeCode
     * @param shipmentType
     * @return
     * @throws Exception
     */
    private String getChargeCode(String bsChargeCode, String shipmentType) throws Exception {
	String chargeCode = null;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    StringBuilder query = new StringBuilder("select charge_code from gl_mapping");
	    query.append(" where transaction_type='AR' and rev_exp='R' and bluescreen_chargecode=? and  shipment_type=? limit 1");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, bsChargeCode);
	    lwStatement.setString(2, shipmentType);
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next() && MigrationUtils.isNotEmpty(lwResult.getString("charge_code"))) {
		chargeCode = lwResult.getString("charge_code");
	    } else {
		errors.append("<li>No logiware charge code found for this blue screen charge code - ").append(bsChargeCode);
		errors.append(" and shipment type - ").append(shipmentType).append("</li>");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    chargeCode = getChargeCode(bsChargeCode, shipmentType);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return chargeCode;
	}
    }

    /**
     * deriveGlAccount - derive gl account from blue screen charge code and shipment type
     * database - logiware and table - gl_mapping,terminal_gl_mapping and account_details
     * @param drcpt
     * @param chargeCode
     * @param shipmentType
     * @param billingTerminal
     * @param loadingTerminal
     * @param drcptTerminal
     * @return
     * @throws Exception
     */
    private String deriveGlAccount(String drcpt, String chargeCode, String shipmentType, String billingTerminal, String loadingTerminal, String drcptTerminal) throws Exception {
	String glAccount = null;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    if (MigrationUtils.isNotEmpty(chargeCode)) {
		StringBuilder derivedGlAccount = new StringBuilder();
		String prefix = getCompanyCode();
		derivedGlAccount.append(prefix).append("-");
		StringBuilder query = new StringBuilder("select gl_acct,derive_yn,suffix_value from gl_mapping");
		query.append(" where transaction_type='AR' and rev_exp='R' and charge_code=? and  shipment_type=? limit 1");
		lwStatement = lwConn.prepareStatement(query.toString());
		lwStatement.setString(1, chargeCode);
		lwStatement.setString(2, shipmentType);
		lwResult = lwStatement.executeQuery();
		String account = null;
		String derive = null;
		String suffix = null;
		boolean isWrongChargeCode = false;
		if (lwResult.next()) {
		    account = lwResult.getString("gl_acct");
		    derive = lwResult.getString("derive_yn");
		    suffix = lwResult.getString("suffix_value");
		} else {
		    isWrongChargeCode = true;
		    errors.append("<li>Charge code - ").append(chargeCode).append(" is not mapped in logiware</li>");
		}
		MigrationUtils.closeResult(lwResult);
		MigrationUtils.closeStatement(lwStatement);
		if (!isWrongChargeCode) {
		    derivedGlAccount.append(account).append("-");
		    if (MigrationUtils.isNotEqualIgnoreCase(derive, "F") && MigrationUtils.isNotEqualIgnoreCase(derive, "N")) {
			Map<String, String> terminal = MigrationUtils.getTerminal(suffix, shipmentType, billingTerminal, loadingTerminal, drcptTerminal, drcpt);
			String terminalNumber = terminal.get("terminalNumber");
			String terminalType = terminal.get("terminalType");
			query = new StringBuilder("select right(concat('0',").append(terminalType).append("),2) as suffix");
			query.append(" from terminal_gl_mapping where terminal = ?");
			lwStatement = lwConn.prepareStatement(query.toString());
			lwStatement.setString(1, terminalNumber);
			lwResult = lwStatement.executeQuery();
			if (lwResult.next() && MigrationUtils.isNotEmpty(lwResult.getString("suffix"))) {
			    suffix = lwResult.getString("suffix");
			    MigrationUtils.closeResult(lwResult);
			    MigrationUtils.closeStatement(lwStatement);
			    derivedGlAccount.append(suffix);
			    lwStatement = lwConn.prepareStatement("select account from account_details where account=?");
			    lwStatement.setString(1, derivedGlAccount.toString());
			    lwResult = lwStatement.executeQuery();
			    if (lwResult.next()) {
				glAccount = lwResult.getString("account");
			    } else {
				errors.append("<li>GL account - ").append(derivedGlAccount.toString());
				errors.append(" derived from charge code - ").append(chargeCode).append(" and suffix value - ").append(suffix);
				errors.append(" and ").append(terminalType).append(" - ").append(terminalNumber);
				errors.append(" and shipment type - ").append(shipmentType).append(" not found in the logiware</li>");
			    }
			} else {
			    terminalType = "B".equals(suffix) ? "billing terminal" : "L".equals(suffix) ? "loading terminal" : "dock receipt";
			    errors.append("<li>Terminal not mapped with GL for charge code - ").append(chargeCode);
			    errors.append(" and shipment type - ").append(shipmentType).append(" and suffix value - ").append(suffix);
			    errors.append(" and ").append(terminalType).append(" - ").append(terminalNumber).append(" in logiware</li>");
			}
			MigrationUtils.closeResult(lwResult);
			MigrationUtils.closeStatement(lwStatement);
		    } else {
			derivedGlAccount.append(suffix);
			lwStatement = lwConn.prepareStatement("select account from account_details where account=?");
			lwStatement.setString(1, derivedGlAccount.toString());
			lwResult = lwStatement.executeQuery();
			if (lwResult.next()) {
			    glAccount = lwResult.getString("account");
			} else {
			    errors.append("<li>GL account - ").append(derivedGlAccount.toString());
			    errors.append(" derived  from charge code - ").append(chargeCode);
			    errors.append(" and shipment type - ").append(shipmentType).append(" not found in the logiware</li>");
			}
		    }
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return glAccount;
	}
    }

    /**
     * getCompanyCode - get the company code from system rule
     * database - logiware and table - system_rules
     * @return
     * @throws Exception
     */
    private String getCompanyCode() throws Exception {
	String companyCode = null;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    String query = "select Rule_Name from system_rules where Rule_Code='CompanyCode'";
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next() && MigrationUtils.isNotEmpty(lwResult.getString("Rule_Name"))) {
		companyCode = lwResult.getString("Rule_Name");
	    } else {
		errors.append("<li>Company Code is not found in Logiware</li>");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    companyCode = getCompanyCode();
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return companyCode;
	}
    }

    /**
     * setInvSubledgers - set INV subledgers from bluescreen invoice number and invoice amount
     * database - bluescreen and table - invoic
     * @param invoiceNumber
     * @param invoiceAmount
     * @throws Exception
     */
    private void setInvSubledgers(String invoiceNumber, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    String prefix = getCompanyCode();
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select chg01,chg02,chg03,chg04,chg05,chg06,chg07,chg08,chg09,chg10,chg11,chg12,");
	    query.append("gln01,gln02,gln03,gln04,gln05,gln06,gln07,gln08,gln09,gln10,gln11,gln12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append("  from invoic where cmpnum=? and trmnum=? and invnum=? ");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, invoiceNumber.substring(0, 2));
	    bsStatement.setString(2, invoiceNumber.substring(2, 4));
	    bsStatement.setString(3, invoiceNumber.substring(4));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    String bsGlAccount = bsResult.getString("gln" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEmpty(bsGlAccount) && MigrationUtils.isNotEmpty(amount)) {
			String glAccount = prefix + "-" + bsGlAccount.substring(2, 6) + "-" + bsGlAccount.substring(6, 8);
			if (MigrationUtils.isNotEmpty(glAccount)) {
			    Subledger subledger = new Subledger();
			    subledger.setBluescreenChargeCode(bsChargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(amount);
			    subledger.setShipmentType("INV");
			    subledgers.add(subledger);
			    totalAmount += amount;
			}
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else if (subledgers.isEmpty()) {
		errors.append("<li>All INV charges are missing in bluescreen</li>");
	    } else {
		errors.append("<li>Sum of all INV charges ").append(MigrationUtils.formatAmount(totalAmount));
		errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setInvSubledgers(invoiceNumber, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclVoidSubledgers - set FCL void subledgers from bluescreen account, bl number and invoice amount
     * database - bluescreen and table - fclvod
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @param fclvodAmount
     * @throws Exception
     */
    private void setFclVoidSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount, double fclvodAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclhed where terml=? and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    String billToCode = bsResult.getString("blt" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isNotEmpty(billToCode)
			    && MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isBillToParty(bsAccountNo, billToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setBluescreenChargeCode(bsChargeCode);
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(-amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount -= amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all FCL Void charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(fclvodAmount)) {
		    errors.append("<li>Sum of all FCL Void charges ").append(MigrationUtils.formatAmount(fclvodAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All FCL Void Charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount, fclvodAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclVoidSubledgers - set FCL void subledgers from bluescreen account, bl number and invoice amount
     * database - bluescreen and table - fclvod
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @throws Exception
     */
    private void setFclVoidSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclvod where terml=? and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    String billToCode = bsResult.getString("blt" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isNotEmpty(billToCode)
			    && MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isBillToParty(bsAccountNo, billToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setBluescreenChargeCode(bsChargeCode);
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(-amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount -= amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setFclVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclCnSubledgers - set FCL CN subledgers from bluescreen account, bl number, cnNumber and invoice amount
     * database - bluescreen and table - fclhed,cnoths
     * @param bsAccountNo
     * @param billOfLading
     * @param cnNumber
     * @param invoiceAmount
     * @param oldTotalAmount
     * @throws Exception
     */
    private void setFclCnSubledgers(String bsAccountNo, String billOfLading, String cnNumber, double invoiceAmount, double oldTotalAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	PreparedStatement cnStatement = null;
	ResultSet cnResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclvod where terml=? and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		List<String> bsChargeCodes = new ArrayList<String>();
		List<Double> bsAmounts = new ArrayList<Double>();
		List<String> bsBltCodes = new ArrayList<String>();
		String prevCnBillToParty = "";
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    bsChargeCodes.add(bsResult.getString("chg" + suffix));
		    bsAmounts.add(bsResult.getDouble("amt" + suffix));
		    bsBltCodes.add(bsResult.getString("blt" + suffix));
		}
		StringBuilder cnQuery = new StringBuilder("select trim(cnotc) as correction_number,trim(cntype) as correction_type,");
		cnQuery.append("trim(pcbcod)  as cn_pcb_code,trim(bltocd) as cn_bill_to_code,trim(nbilto) as new_bill_to,");
		cnQuery.append("trim(ccd01) as chg01,trim(ccd02) as chg02,trim(ccd03) as chg03,trim(ccd04) as chg04,");
		cnQuery.append("trim(ccd05) as chg05,trim(ccd06) as chg06,trim(ccd07) as chg07,trim(ccd08) as chg08,");
		cnQuery.append("trim(ccd09) as chg09,trim(ccd10) as chg10,trim(ccd11) as chg11,trim(ccd12) as chg12,");
		cnQuery.append("trim(pcn01) as pcn01,trim(pcn02) as pcn02,trim(pcn03) as pcn03,trim(pcn04) as pcn04,");
		cnQuery.append("trim(pcn05) as pcn05,trim(pcn06) as pcn06,trim(pcn07) as pcn07,trim(pcn08) as pcn08,");
		cnQuery.append("trim(pcn09) as pcn09,trim(pcn10) as pcn10,trim(pcn11) as pcn11,trim(pcn12) as pcn12,");
		cnQuery.append("cna01 as amt01,cna02 as amt02,cna03 as amt03,cna04 as amt04,cna05 as amt05,cna06 as amt06,");
		cnQuery.append("cna07 as amt07,cna08 as amt08,cna09 as amt09,cna10 as amt10,cna11 as amt11,cna12 as amt12");
		cnQuery.append(" from cnoths where blkey=? order by cndate,cnotc");
		cnStatement = bsConn.prepareStatement(cnQuery.toString());
		cnStatement.setString(1, billOfLading);
		cnResult = cnStatement.executeQuery();
		while (cnResult.next()) {
		    String correctionNumber = cnResult.getString("correction_number");
		    String correctionType = cnResult.getString("correction_type");
		    String newBillToParty = cnResult.getString("new_bill_to");
		    if (cnNumber.equals(correctionNumber)) {
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double newAmount = cnResult.getDouble("amt" + suffix);
				double correctionAmount = newAmount - originalAmount;
				String cnBillToCode = cnResult.getString("pcn" + suffix);
				String bsBillToCode = bsBltCodes.get(suffixCount - 1);
				if (MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, prevCnBillToParty,
					cnBillToCode, bsBillToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)
					&& ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000"))
					|| (MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000")))) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setBluescreenChargeCode(cnChargeCode);
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			} else {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double correctionAmount = invoiceAmount < 0 ? -originalAmount : originalAmount;
				String bsBillToCode = bsBltCodes.get(suffixCount - 1);
				String cnBillToCode = cnResult.getString("pcn" + suffix);
				if (MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, invoiceAmount, prevCnBillToParty, newBillToParty,
					cnBillToCode, bsBillToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)
					&& ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000"))
					|| (MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000")))) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setBluescreenChargeCode(cnChargeCode);
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			}
			break;
		    } else {
			prevCnBillToParty = newBillToParty;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    bsChargeCodes.clear();
			    bsAmounts.clear();
			    bsBltCodes.clear();
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				bsChargeCodes.add(cnResult.getString("chg" + suffix));
				bsAmounts.add(cnResult.getDouble("amt" + suffix));
				bsBltCodes.add(cnResult.getString("pcn" + suffix));
			    }
			}
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all FCL CN charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(oldTotalAmount)) {
		    errors.append("<li>Sum of all FCL CN charges ").append(MigrationUtils.formatAmount(oldTotalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All FCL CN charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclCnSubledgers(bsAccountNo, billOfLading, cnNumber, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(cnResult);
	    MigrationUtils.closeStatement(cnStatement);
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclCnSubledgers - set FCL CN subledgers from bluescreen account, bl number, cnNumber and invoice amount
     * database - bluescreen and table - fclhed,cnoths
     * @param bsAccountNo
     * @param billOfLading
     * @param cnNumber
     * @param invoiceAmount
     * @throws Exception
     */
    private void setFclCnSubledgers(String bsAccountNo, String billOfLading, String cnNumber, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	PreparedStatement cnStatement = null;
	ResultSet cnResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclhed where terml=? and drcpt=? limit 1");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		List<String> bsChargeCodes = new ArrayList<String>();
		List<Double> bsAmounts = new ArrayList<Double>();
		List<String> bsBltCodes = new ArrayList<String>();
		String prevCnBillToParty = "";
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    bsChargeCodes.add(bsResult.getString("chg" + suffix));
		    bsAmounts.add(bsResult.getDouble("amt" + suffix));
		    bsBltCodes.add(bsResult.getString("blt" + suffix));
		}
		StringBuilder cnQuery = new StringBuilder("select trim(cnotc) as correction_number,trim(cntype) as correction_type,");
		cnQuery.append("trim(pcbcod)  as cn_pcb_code,trim(bltocd) as cn_bill_to_code,trim(nbilto) as new_bill_to,");
		cnQuery.append("trim(ccd01) as chg01,trim(ccd02) as chg02,trim(ccd03) as chg03,trim(ccd04) as chg04,");
		cnQuery.append("trim(ccd05) as chg05,trim(ccd06) as chg06,trim(ccd07) as chg07,trim(ccd08) as chg08,");
		cnQuery.append("trim(ccd09) as chg09,trim(ccd10) as chg10,trim(ccd11) as chg11,trim(ccd12) as chg12,");
		cnQuery.append("trim(pcn01) as pcn01,trim(pcn02) as pcn02,trim(pcn03) as pcn03,trim(pcn04) as pcn04,");
		cnQuery.append("trim(pcn05) as pcn05,trim(pcn06) as pcn06,trim(pcn07) as pcn07,trim(pcn08) as pcn08,");
		cnQuery.append("trim(pcn09) as pcn09,trim(pcn10) as pcn10,trim(pcn11) as pcn11,trim(pcn12) as pcn12,");
		cnQuery.append("cna01 as amt01,cna02 as amt02,cna03 as amt03,cna04 as amt04,cna05 as amt05,cna06 as amt06,");
		cnQuery.append("cna07 as amt07,cna08 as amt08,cna09 as amt09,cna10 as amt10,cna11 as amt11,cna12 as amt12");
		cnQuery.append(" from cnoths where blkey=? order by cndate,cnotc");
		cnStatement = bsConn.prepareStatement(cnQuery.toString());
		cnStatement.setString(1, billOfLading);
		cnResult = cnStatement.executeQuery();
		while (cnResult.next()) {
		    String correctionNumber = cnResult.getString("correction_number");
		    String correctionType = cnResult.getString("correction_type");
		    String newBillToParty = cnResult.getString("new_bill_to");
		    if (cnNumber.equals(correctionNumber)) {
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double newAmount = cnResult.getDouble("amt" + suffix);
				double correctionAmount = newAmount - originalAmount;
				String cnBillToCode = cnResult.getString("pcn" + suffix);
				String bsBillToCode = bsBltCodes.get(suffixCount - 1);
				if (MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, prevCnBillToParty,
					cnBillToCode, bsBillToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)
					&& ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000"))
					|| (MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000")))) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setBluescreenChargeCode(cnChargeCode);
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			} else {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double correctionAmount = invoiceAmount < 0 ? -originalAmount : originalAmount;
				String bsBillToCode = bsBltCodes.get(suffixCount - 1);
				String cnBillToCode = cnResult.getString("pcn" + suffix);
				if (MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, invoiceAmount, prevCnBillToParty, newBillToParty,
					cnBillToCode, bsBillToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)
					&& ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000"))
					|| (MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000")))) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setBluescreenChargeCode(cnChargeCode);
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			}
			break;
		    } else {
			prevCnBillToParty = newBillToParty;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    bsChargeCodes.clear();
			    bsAmounts.clear();
			    bsBltCodes.clear();
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				bsChargeCodes.add(cnResult.getString("chg" + suffix));
				bsAmounts.add(cnResult.getDouble("amt" + suffix));
				bsBltCodes.add(cnResult.getString("pcn" + suffix));
			    }
			}
		    }
		}

	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setFclCnSubledgers(bsAccountNo, billOfLading, cnNumber, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclCnSubledgers(bsAccountNo, billOfLading, cnNumber, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(cnResult);
	    MigrationUtils.closeStatement(cnStatement);
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclSubledgers - set FCL subledgers from void using bluescreen account, bl number and invoice amount
     * database - bluescreen and table - fclvod
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @param originalTotalAmount
     * @throws Exception
     */
    private void setFclSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount, double originalTotalAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclvod where terml=? and drcpt=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    String billToCode = bsResult.getString("blt" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isNotEmpty(billToCode)
			    && MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isBillToParty(bsAccountNo, billToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setBluescreenChargeCode(bsChargeCode);
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount += amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all FCL charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(originalTotalAmount)) {
		    errors.append("<li>Sum of all FCL charges ").append(MigrationUtils.formatAmount(originalTotalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All FCL charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclSubledgers(bsAccountNo, billOfLading, invoiceAmount, originalTotalAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclSubledgers - set FCL subledgers from bluescreen account, bl number and invoice amount
     * database - bluescreen and table - fclhed
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @throws Exception
     */
    private void setFclSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(consl='A' or (itmnum=63 and prtnum=30),'FCLI','FCLE') as shipment_type,");
	    query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
	    query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_no,trim(prtnum) as port_no,");
	    query.append("trim(exprf1) as customer_reference_number,trim(etadte) as eta,trim(expcar) as vessel_name,trim(vessel) as vessel_no,");
	    query.append("trim(ssppcl) as bl_terms,if(datman=''||datman='0','N','Y') as manifest_flag,trim(ssline) as steam_ship_line,");
	    query.append("trim(hshnum) as shipper_no,trim(hconum) as consignee_no,trim(fwdnum) as forwarder_no,");
	    query.append("trim(altag) as agent_no,trim(destn) as destination,trim(billto) as third_party_no,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(blt01) as blt01,trim(blt02) as blt02,trim(blt03) as blt03,trim(blt04) as blt04,");
	    query.append("trim(blt05) as blt05,trim(blt06) as blt06,trim(blt07) as blt07,trim(blt08) as blt08,");
	    query.append("trim(blt09) as blt09,trim(blt10) as blt10,trim(blt11) as blt11,trim(blt12) as blt12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from fclhed where terml=? and drcpt=? limit 1");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(5, 7));
	    bsStatement.setString(2, billOfLading.substring(7));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage_no"));
		values.put("port", bsResult.getString("port_no"));
		values.put("destination", bsResult.getString("port_no"));//Need to clarify
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("vesselNumber", bsResult.getString("vessel_no"));
		values.put("blTerms", bsResult.getString("bl_terms"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLine(bsResult.getString("steam_ship_line"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), true);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		setContainer(billOfLading);
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsConsigneeNo = (String) values.get("bsConsigneeNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String shipmentType = (String) values.get("shipmentType");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    String billToCode = bsResult.getString("blt" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isNotEmpty(billToCode)
			    && MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isBillToParty(bsAccountNo, billToCode, bsShipperNo, bsForwarderNo, bsConsigneeNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(null, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setBluescreenChargeCode(bsChargeCode);
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount += amount;
			}
		    }
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setFclSubledgers(bsAccountNo, billOfLading, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclSubledgers(bsAccountNo, billOfLading, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setFclBlValues - set FCL values from bl number
     * database - bluescreen and table - fclhed
     * @param billOfLading
     * @throws Exception
     */
    private void setFclBlValues(String logType, String bsAccountNo, String billOfLading, String type, String cnNumber, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    if (MigrationUtils.isNotEqual(logType, "warning")) {
		if (MigrationUtils.isEqual(type, "0")) {
		    setFclVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount);
		} else if (MigrationUtils.isEqual(type, "3")) {
		    setFclCnSubledgers(bsAccountNo, billOfLading, cnNumber, invoiceAmount);
		} else {
		    setFclSubledgers(bsAccountNo, billOfLading, invoiceAmount);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setFclBlValues(logType, bsAccountNo, billOfLading, type, cnNumber, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirVoidSubledgers - set LCL or AIR Void subledgers from bluescreen account, bl number and invoice amount
     * database - bluescreen and table - blvoid
     * database - logiware and table - ports,agency_info and trading_partner
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @param blvoidAmount
     * @throws Exception
     */
    private void setLclOrAirVoidSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount, double blvoidAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from histry where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
            String drcpt = billOfLading.substring(5, 7);
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (MigrationUtils.between(bsResult.getInt("billing_terminal"), 60, 69)
			&& MigrationUtils.in(billOfLading.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		String pcbCode = bsResult.getString("pcb_code");
		String billToCode = bsResult.getString("blt_code");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String pcb = bsResult.getString("pcb" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isBillToParty(bsAccountNo, pcbCode, pcb, billToCode, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(-amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount -= amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all LCL or AIR Void charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(blvoidAmount)) {
		    errors.append("<li>Sum of all LCL or AIR Void charges ").append(MigrationUtils.formatAmount(blvoidAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All LCL or AIR Void Charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount, blvoidAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirVoidSubledgers - set LCL or AIR Void subledgers from bluescreen account, bl number and invoice amount
     * database - bluescreen and table - blvoid
     * database - logiware and table - ports,agency_info and trading_partner
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @throws Exception
     */
    private void setLclOrAirVoidSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from blvoid where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		String drcpt = billOfLading.substring(5, 7);
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (MigrationUtils.between(bsResult.getInt("billing_terminal"), 60, 69)
			&& MigrationUtils.in(billOfLading.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		String pcbCode = bsResult.getString("pcb_code");
		String billToCode = bsResult.getString("blt_code");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String pcb = bsResult.getString("pcb" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isBillToParty(bsAccountNo, pcbCode, pcb, billToCode, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(-amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount -= amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setLclOrAirVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirCnSubledgers - set LCL or AIR CN subledgers from bluescreen account, bl number, correctionNumberFromFile and invoice amount
     * database - bluescreen and table - histry,cnoths
     * @param bsAccountNo
     * @param billOfLading
     * @param cnNumberFromFile
     * @param invoiceAmount
     * @param oldTotalAmount
     * @throws Exception
     */
    private void setLclOrAirCnSubledgers(String bsAccountNo, String billOfLading, String cnNumberFromFile, double invoiceAmount, double oldTotalAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	PreparedStatement cnStatement = null;
	ResultSet cnResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from blvoid where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		String drcpt = billOfLading.substring(5, 7);
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (MigrationUtils.between(bsResult.getInt("billing_terminal"), 60, 69)
			&& MigrationUtils.in(billOfLading.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		boolean pcbSpaceCheck = false;
		String prevCnPcbCode = "";
		String prevCnBilltoCode = "";
		String prevCnBilltoParty = "";
		String billToCode = bsResult.getString("blt_code");
		String pcbCode = bsResult.getString("pcb_code");
		String bsPcbCode = bsResult.getString("pcb_code");
		List<String> bsChargeCodes = new ArrayList<String>();
		List<Double> bsAmounts = new ArrayList<Double>();
		List<String> bsPcbCodes = new ArrayList<String>();
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String bsPcbCheck = bsResult.getString("pcb" + suffix);
		    bsChargeCodes.add(bsChargeCode);
		    bsAmounts.add(bsResult.getDouble("amt" + suffix));
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") && MigrationUtils.isEmpty(bsPcbCheck)) {
			bsPcbCodes.add(MigrationUtils.isNotEqual(bsPcbCode, "B") ? bsPcbCode : "");
			pcbSpaceCheck = true;
		    } else {
			bsPcbCodes.add(bsPcbCheck);
		    }
		}
		StringBuilder cnQuery = new StringBuilder("select trim(cnotc) as correction_number,trim(cntype) as correction_type,");
		cnQuery.append("trim(pcbcod)  as cn_pcb_code,trim(bltocd) as cn_bill_to_code,trim(nbilto) as new_bill_to,");
		cnQuery.append("trim(ccd01) as chg01,trim(ccd02) as chg02,trim(ccd03) as chg03,trim(ccd04) as chg04,");
		cnQuery.append("trim(ccd05) as chg05,trim(ccd06) as chg06,trim(ccd07) as chg07,trim(ccd08) as chg08,");
		cnQuery.append("trim(ccd09) as chg09,trim(ccd10) as chg10,trim(ccd11) as chg11,trim(ccd12) as chg12,");
		cnQuery.append("trim(pcn01) as pcn01,trim(pcn02) as pcn02,trim(pcn03) as pcn03,trim(pcn04) as pcn04,");
		cnQuery.append("trim(pcn05) as pcn05,trim(pcn06) as pcn06,trim(pcn07) as pcn07,trim(pcn08) as pcn08,");
		cnQuery.append("trim(pcn09) as pcn09,trim(pcn10) as pcn10,trim(pcn11) as pcn11,trim(pcn12) as pcn12,");
		cnQuery.append("cna01 as amt01,cna02 as amt02,cna03 as amt03,cna04 as amt04,cna05 as amt05,cna06 as amt06,");
		cnQuery.append("cna07 as amt07,cna08 as amt08,cna09 as amt09,cna10 as amt10,cna11 as amt11,cna12 as amt12");
		cnQuery.append(" from cnoths where blkey=? order by cndate,cnotc");
		cnStatement = bsConn.prepareStatement(cnQuery.toString());
		cnStatement.setString(1, billOfLading);
		cnResult = cnStatement.executeQuery();
		while (cnResult.next()) {
		    String correctionNumber = cnResult.getString("correction_number");
		    String correctionType = cnResult.getString("correction_type");
		    String cnPcbCode = cnResult.getString("cn_pcb_code");
		    String newBillToParty = cnResult.getString("new_bill_to");
		    String cnBillToCode = cnResult.getString("cn_bill_to_code");
		    if (cnNumberFromFile.equals(correctionNumber)) {
			bsPcbCode = MigrationUtils.isNotEqualIgnoreEmpty(prevCnPcbCode, "0000") ? prevCnPcbCode
				: MigrationUtils.isNotEqualIgnoreEmpty(cnPcbCode, "0000") ? cnPcbCode : bsPcbCode;
			billToCode = MigrationUtils.isNotEqual(bsPcbCode, "B")
				&& MigrationUtils.isNotEqualIgnoreEmpty(prevCnBilltoCode, "0000") ? prevCnBilltoCode : billToCode;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(cnChargeCode, "0139")) {
				    cnChargeCode = "0001";
				}
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double newAmount = cnResult.getDouble("amt" + suffix);
				double correctionAmount = newAmount - originalAmount;
				if (pcbSpaceCheck && pcbCode.equals("C")) {
				    billToCode = cnBillToCode;
				}
				String pcb = MigrationUtils.isNotEmpty(cnResult.getString("pcn" + suffix))
					? cnResult.getString("pcn" + suffix) : bsPcbCodes.get(suffixCount - 1);
				if ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") || MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000"))
					&& MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, bsPcbCode, pcb, pcbCode,
					billToCode, prevCnBilltoParty, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			} else {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double correctionAmount = invoiceAmount < 0 ? -originalAmount : originalAmount;
				String pcb = MigrationUtils.isNotEmpty(cnResult.getString("pcn" + suffix))
					? cnResult.getString("pcn" + suffix) : bsPcbCodes.get(suffixCount - 1);
				if ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") || MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000"))
					&& MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, invoiceAmount, newBillToParty, bsPcbCode, pcb, pcbCode,
					billToCode, prevCnBilltoParty, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			}
			break;
		    } else {
			prevCnBilltoParty = newBillToParty;
			prevCnBilltoCode = cnBillToCode;
			prevCnPcbCode = cnPcbCode;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    bsChargeCodes.clear();
			    bsAmounts.clear();
			    bsPcbCodes.clear();
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				bsChargeCodes.add(cnResult.getString("chg" + suffix));
				bsAmounts.add(cnResult.getDouble("amt" + suffix));
				bsPcbCodes.add(cnResult.getString("pcn" + suffix));
			    }
			}
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all LCL or AIR CN charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(oldTotalAmount)) {
		    errors.append("<li>Sum of all LCL or AIR CN charges ").append(MigrationUtils.formatAmount(oldTotalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All LCL or AIR CN charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirCnSubledgers(bsAccountNo, billOfLading, cnNumberFromFile, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(cnResult);
	    MigrationUtils.closeStatement(cnStatement);
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirCnSubledgers - set LCL or AIR CN subledgers from bluescreen account, bl number, correctionNumberFromFile and invoice amount
     * database - bluescreen and table - histry,cnoths
     * @param bsAccountNo
     * @param billOfLading
     * @param cnNumberFromFile
     * @param invoiceAmount
     * @throws Exception
     */
    private void setLclOrAirCnSubledgers(String bsAccountNo, String billOfLading, String cnNumberFromFile, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	PreparedStatement cnStatement = null;
	ResultSet cnResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from histry where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		String drcpt = billOfLading.substring(5, 7);
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (MigrationUtils.between(bsResult.getInt("billing_terminal"), 60, 69)
			&& MigrationUtils.in(billOfLading.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		boolean pcbSpaceCheck = false;
		String prevCnPcbCode = "";
		String prevCnBilltoCode = "";
		String prevCnBilltoParty = "";
		String billToCode = bsResult.getString("blt_code");
		String pcbCode = bsResult.getString("pcb_code");
		String bsPcbCode = bsResult.getString("pcb_code");
		List<String> bsChargeCodes = new ArrayList<String>();
		List<Double> bsAmounts = new ArrayList<Double>();
		List<String> bsPcbCodes = new ArrayList<String>();
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String bsPcbCheck = bsResult.getString("pcb" + suffix);
		    bsChargeCodes.add(bsChargeCode);
		    bsAmounts.add(bsResult.getDouble("amt" + suffix));
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") && MigrationUtils.isEmpty(bsPcbCheck)) {
			bsPcbCodes.add(MigrationUtils.isNotEqual(bsPcbCode, "B") ? bsPcbCode : "");
			pcbSpaceCheck = true;
		    } else {
			bsPcbCodes.add(bsPcbCheck);
		    }
		}
		StringBuilder cnQuery = new StringBuilder("select trim(cnotc) as correction_number,trim(cntype) as correction_type,");
		cnQuery.append("trim(pcbcod)  as cn_pcb_code,trim(bltocd) as cn_bill_to_code,trim(nbilto) as new_bill_to,");
		cnQuery.append("trim(ccd01) as chg01,trim(ccd02) as chg02,trim(ccd03) as chg03,trim(ccd04) as chg04,");
		cnQuery.append("trim(ccd05) as chg05,trim(ccd06) as chg06,trim(ccd07) as chg07,trim(ccd08) as chg08,");
		cnQuery.append("trim(ccd09) as chg09,trim(ccd10) as chg10,trim(ccd11) as chg11,trim(ccd12) as chg12,");
		cnQuery.append("trim(pcn01) as pcn01,trim(pcn02) as pcn02,trim(pcn03) as pcn03,trim(pcn04) as pcn04,");
		cnQuery.append("trim(pcn05) as pcn05,trim(pcn06) as pcn06,trim(pcn07) as pcn07,trim(pcn08) as pcn08,");
		cnQuery.append("trim(pcn09) as pcn09,trim(pcn10) as pcn10,trim(pcn11) as pcn11,trim(pcn12) as pcn12,");
		cnQuery.append("cna01 as amt01,cna02 as amt02,cna03 as amt03,cna04 as amt04,cna05 as amt05,cna06 as amt06,");
		cnQuery.append("cna07 as amt07,cna08 as amt08,cna09 as amt09,cna10 as amt10,cna11 as amt11,cna12 as amt12");
		cnQuery.append(" from cnoths where blkey=? order by cndate,cnotc");
		cnStatement = bsConn.prepareStatement(cnQuery.toString());
		cnStatement.setString(1, billOfLading);
		cnResult = cnStatement.executeQuery();
		while (cnResult.next()) {
		    String correctionNumber = cnResult.getString("correction_number");
		    String correctionType = cnResult.getString("correction_type");
		    String cnPcbCode = cnResult.getString("cn_pcb_code");
		    String newBillToParty = cnResult.getString("new_bill_to");
		    String cnBillToCode = cnResult.getString("cn_bill_to_code");
		    if (cnNumberFromFile.equals(correctionNumber)) {
			bsPcbCode = MigrationUtils.isNotEqualIgnoreEmpty(prevCnPcbCode, "0000") ? prevCnPcbCode
				: MigrationUtils.isNotEqualIgnoreEmpty(cnPcbCode, "0000") ? cnPcbCode : bsPcbCode;
			billToCode = MigrationUtils.isNotEqual(bsPcbCode, "B")
				&& MigrationUtils.isNotEqualIgnoreEmpty(prevCnBilltoCode, "0000") ? prevCnBilltoCode : billToCode;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double newAmount = cnResult.getDouble("amt" + suffix);
				double correctionAmount = newAmount - originalAmount;
				if (pcbSpaceCheck && pcbCode.equals("C")) {
				    billToCode = cnBillToCode;
				}
				String pcb = MigrationUtils.isNotEmpty(cnResult.getString("pcn" + suffix))
					? cnResult.getString("pcn" + suffix) : bsPcbCodes.get(suffixCount - 1);
				if ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") || MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000"))
					&& MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, cnPcbCode, pcb, pcbCode,
					billToCode, prevCnBilltoParty, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			} else {
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				String bsChargeCode = bsChargeCodes.get(suffixCount - 1);
				String cnChargeCode = cnResult.getString("chg" + suffix);
				double originalAmount = bsAmounts.get(suffixCount - 1);
				double correctionAmount = invoiceAmount < 0 ? -originalAmount : originalAmount;
				String pcb = MigrationUtils.isNotEmpty(cnResult.getString("pcn" + suffix))
					? cnResult.getString("pcn" + suffix) : bsPcbCodes.get(suffixCount - 1);
				if ((MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000") || MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000"))
					&& MigrationUtils.isNotEmpty(correctionAmount)
					&& MigrationUtils.isBillToParty(bsAccountNo, invoiceAmount, newBillToParty, bsPcbCode, pcb, pcbCode,
					billToCode, prevCnBilltoParty, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
				    cnChargeCode = MigrationUtils.isNotEqualIgnoreEmpty(cnChargeCode, "0000") ? cnChargeCode : bsChargeCode;
				    String chargeCode = getChargeCode(cnChargeCode, shipmentType);
				    String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
				    if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
					Subledger subledger = new Subledger();
					subledger.setChargeCode(chargeCode);
					subledger.setGlAccount(glAccount);
					subledger.setAmount(correctionAmount);
					subledger.setShipmentType(shipmentType);
					subledgers.add(subledger);
					totalAmount += correctionAmount;
				    }
				}
				if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
				    break;
				}
			    }
			}
			break;
		    } else {
			prevCnBilltoParty = MigrationUtils.isNotEqualIgnoreEmpty(newBillToParty, "00000") ? newBillToParty : prevCnBilltoParty;
			prevCnBilltoCode = MigrationUtils.isNotEmpty(cnBillToCode) ? cnBillToCode : prevCnBilltoCode;
			prevCnPcbCode = MigrationUtils.isNotEmpty(cnPcbCode) ? cnPcbCode : prevCnPcbCode;
			if (MigrationUtils.isEqual(correctionType, "A", "AY")) {
			    bsChargeCodes.clear();
			    bsAmounts.clear();
			    bsPcbCodes.clear();
			    for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
				String suffix = String.format("%02d", suffixCount);
				bsChargeCodes.add(cnResult.getString("chg" + suffix));
				bsAmounts.add(cnResult.getDouble("amt" + suffix));
				bsPcbCodes.add(cnResult.getString("pcn" + suffix));
			    }
			}
		    }
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setLclOrAirCnSubledgers(bsAccountNo, billOfLading, cnNumberFromFile, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirCnSubledgers(bsAccountNo, billOfLading, cnNumberFromFile, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(cnResult);
	    MigrationUtils.closeStatement(cnStatement);
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirSubledgers - set LCL or AIR subledgers from void using bluescreen account, bl number and invoice amount
     * database - bluescreen and table - blvoid
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @param oldTotalAmount
     * @throws Exception
     */
    private void setLclOrAirSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount, double oldTotalAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from blvoid where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
	    while (bsResult.next()) {
		totalAmount = 0d;
		subledgers.clear();
		String drcpt = billOfLading.substring(5, 7);
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (MigrationUtils.between(bsResult.getInt("billing_terminal"), 60, 69)
			&& MigrationUtils.in(billOfLading.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		String pcbCode = bsResult.getString("pcb_code");
		String billToCode = bsResult.getString("blt_code");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String pcb = bsResult.getString("pcb" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isBillToParty(bsAccountNo, pcbCode, pcb, billToCode, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount += amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
		if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		    break;
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		if (MigrationUtils.isNotEmpty(totalAmount)) {
		    errors.append("<li>Sum of all LCL or AIR charges ").append(MigrationUtils.formatAmount(totalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else if (MigrationUtils.isNotEmpty(oldTotalAmount)) {
		    errors.append("<li>Sum of all LCL or AIR charges ").append(MigrationUtils.formatAmount(oldTotalAmount));
		    errors.append(" is not equal to invoice amount ").append(MigrationUtils.formatAmount(invoiceAmount)).append("</li>");
		} else {
		    errors.append("<li>All LCL or AIR charges are missing in bluescreen</li>");
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirSubledgers(bsAccountNo, billOfLading, invoiceAmount, oldTotalAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirSubledgers - set LCL or AIR subledgers from bluescreen account,bl number and invoice amount
     * database - bluescreen and table - histry
     * database - logiware and table - ports,agency_info and trading_partner
     * @param bsAccountNo
     * @param billOfLading
     * @param invoiceAmount
     * @throws Exception
     */
    private void setLclOrAirSubledgers(String bsAccountNo, String billOfLading, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    double totalAmount = 0d;
	    List<Subledger> subledgers = new ArrayList<Subledger>();
	    StringBuilder query = new StringBuilder("select if(blterm>79,if(domfor='D','AIRI','AIRE'),");
	    query.append("if(domfor='D' and blterm<80 and blterm!=5 and blterm!=35");
	    query.append(" and ((port<=30 and port!=10 and port!=20) or (blterm=60 and drterm=13)),'LCLI','LCLE')) as shipment_type,");
	    query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
	    query.append("concat(glterm,port,cntvoy,cntsuf) as voyage,trim(exref1) as customer_reference_number,arrdat as eta,vesnam as vessel_name,");
	    query.append("port as port_no,contmn as container_no,seal as seal_no,if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
	    query.append("shpnum as shipper_no,connum as consignee_no,fwdnum as forwarder_no,altagt as agent_no,blfdes as destination,");
	    query.append("trim(btacct) as third_party_no,trim(pcbcod) as pcb_code,trim(billto) as blt_code,");
	    query.append("trim(chg01) as chg01,trim(chg02) as chg02,trim(chg03) as chg03,trim(chg04) as chg04,");
	    query.append("trim(chg05) as chg05,trim(chg06) as chg06,trim(chg07) as chg07,trim(chg08) as chg08,");
	    query.append("trim(chg09) as chg09,trim(chg10) as chg10,trim(chg11) as chg11,trim(chg12) as chg12,");
	    query.append("trim(pcb01) as pcb01,trim(pcb02) as pcb02,trim(pcb03) as pcb03,trim(pcb04) as pcb04,");
	    query.append("trim(pcb05) as pcb05,trim(pcb06) as pcb06,trim(pcb07) as pcb07,trim(pcb08) as pcb08,");
	    query.append("trim(pcb09) as pcb09,trim(pcb10) as pcb10,trim(pcb11) as pcb11,trim(pcb12) as pcb12,");
	    query.append("amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
	    query.append(" from histry where blterm=? and port=? and drterm=? and drnum=? and drsuff=?");
	    bsStatement = bsConn.prepareStatement(query.toString());
	    bsStatement.setString(1, billOfLading.substring(0, 2));
	    bsStatement.setString(2, billOfLading.substring(2, 5));
	    bsStatement.setString(3, billOfLading.substring(5, 7));
	    bsStatement.setString(4, billOfLading.substring(7, 13));
	    bsStatement.setString(5, billOfLading.substring(13));
	    bsResult = bsStatement.executeQuery();
	    if (bsResult.next()) {
		String drcpt = billOfLading.substring(5, 7);
		String shipmentType = bsResult.getString("shipment_type");
		values.put("shipmentType", bsResult.getString("shipment_type"));
		values.put("billingTerminal", bsResult.getString("billing_terminal"));
		values.put("loadingTerminal", bsResult.getString("loading_terminal"));
		values.put("drcptTerminal", bsResult.getString("drcpt_terminal"));
		values.put("voyageNumber", bsResult.getString("voyage"));
		values.put("customerReferenceNumber", bsResult.getString("customer_reference_number"));
		values.put("eta", MigrationUtils.parseDate(bsResult.getString("eta"), "yyyyMMdd"));
		values.put("vesselName", bsResult.getString("vessel_name"));
		values.put("port", bsResult.getString("port_no"));
		values.put("containerNumber", bsResult.getString("container_no"));
		values.put("sealNumber", bsResult.getString("seal_no"));
		values.put("manifestFlag", bsResult.getString("manifest_flag"));
		setSteamShipLineFromContainer(bsResult.getString("container_no"));
		setShipper(bsResult.getString("shipper_no"));
		setConsignee(bsResult.getString("consignee_no"));
		setForwarder(bsResult.getString("forwarder_no"));
		if (MigrationUtils.isNotEqualIgnoreEmpty(bsResult.getString("agent_no"), "00000")) {
		    //Getting agent name and agent number directly from trading partner
		    setAgent(bsResult.getString("agent_no"));
		} else {
		    //Getting agent name and agent number indirectly from trading partner using port and destination
		    setAgent(bsResult.getString("port_no"), bsResult.getString("destination"), false);
		}
		setThirdParty(bsResult.getString("third_party_no"));
		List<String> portList = new ArrayList<String>();
		portList.add("001");
		portList.add("008");
		portList.add("009");
		portList.add("015");
		portList.add("016");
		portList.add("017");
		portList.add("018");
		portList.add("019");
		portList.add("030");
		int blTerm = MigrationUtils.isEmpty(bsResult.getString("billing_terminal")) ? 0 : Integer.parseInt(bsResult.getString("billing_terminal"));
		if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
			|| (blTerm >= 60 && blTerm <= 69 && portList.contains(bsResult.getString("port_no")))) {
		    setVoyage(billOfLading, shipmentType);
		}
		String bsShipperNo = (String) values.get("bsShipperNo");
		String bsForwarderNo = (String) values.get("bsForwarderNo");
		String bsAgentNo = (String) values.get("bsAgentNo");
		String bsThirdPartyNo = (String) values.get("bsThirdPartyNo");
		String billingTerminal = (String) values.get("billingTerminal");
		String loadingTerminal = (String) values.get("loadingTerminal");
		String drcptTerminal = (String) values.get("drcptTerminal");
		String pcbCode = bsResult.getString("pcb_code");
		String billToCode = bsResult.getString("blt_code");
		for (int suffixCount = 1; suffixCount <= 12; suffixCount++) {
		    String suffix = String.format("%02d", suffixCount);
		    String bsChargeCode = bsResult.getString("chg" + suffix);
		    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLE") && MigrationUtils.isEqualIgnoreCase(bsChargeCode, "0139")) {
			bsChargeCode = "0001";
		    }
		    String pcb = bsResult.getString("pcb" + suffix);
		    double amount = bsResult.getDouble("amt" + suffix);
		    if (MigrationUtils.isNotEqualIgnoreEmpty(bsChargeCode, "0000")
			    && MigrationUtils.isNotEmpty(amount)
			    && MigrationUtils.isBillToParty(bsAccountNo, pcbCode, pcb, billToCode, bsShipperNo, bsForwarderNo, bsAgentNo, bsThirdPartyNo)) {
			String chargeCode = getChargeCode(bsChargeCode, shipmentType);
			String glAccount = deriveGlAccount(drcpt, chargeCode, shipmentType, billingTerminal, loadingTerminal, drcptTerminal);
			if (MigrationUtils.isNotEmpty(glAccount) && MigrationUtils.isNotEmpty(chargeCode)) {
			    Subledger subledger = new Subledger();
			    subledger.setChargeCode(chargeCode);
			    subledger.setGlAccount(glAccount);
			    subledger.setAmount(amount);
			    subledger.setShipmentType(shipmentType);
			    subledgers.add(subledger);
			    totalAmount += amount;
			}
		    }
		    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
			break;
		    }
		}
	    }
	    if (MigrationUtils.isEqual(totalAmount, invoiceAmount)) {
		values.put("subledgers", subledgers);
	    } else {
		setLclOrAirSubledgers(bsAccountNo, billOfLading, invoiceAmount, totalAmount);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirSubledgers(bsAccountNo, billOfLading, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * setLclOrAirBlValues - set LCL or AIR values from bl number
     * database - bluescreen and table - histry or blvoid
     * @param billOfLading
     * @throws Exception
     */
    private void setLclOrAirBlValues(String logType, String bsAccountNo, String billOfLading, String type, String correctionNumber, double invoiceAmount) throws Exception {
	PreparedStatement bsStatement = null;
	ResultSet bsResult = null;
	try {
	    if (MigrationUtils.isNotEqual(logType, "warning")) {
		if (MigrationUtils.isEqual(type, "0")) {
		    setLclOrAirVoidSubledgers(bsAccountNo, billOfLading, invoiceAmount);
		} else if (MigrationUtils.isEqual(type, "3")) {
		    setLclOrAirCnSubledgers(bsAccountNo, billOfLading, correctionNumber, invoiceAmount);
		} else {
		    setLclOrAirSubledgers(bsAccountNo, billOfLading, invoiceAmount);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToBluescreen();
	    setLclOrAirBlValues(logType, bsAccountNo, billOfLading, type, correctionNumber, invoiceAmount);
	} finally {
	    MigrationUtils.closeResult(bsResult);
	    MigrationUtils.closeStatement(bsStatement);
	}
    }

    /**
     * checkNewArOrNot - check whether the invoice is new one or not
     * database - logiware and table - transaction
     * @return boolean - true or false
     * @throws Exception
     */
    private boolean isNewAr() throws Exception {
	boolean isNewAr = true;
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    String customerNumber = (String) values.get("customerNumber");
	    String billOfLading = (String) values.get("billOfLading");
	    String invoiceNumber = (String) values.get("billOfLading");
	    StringBuilder query = new StringBuilder("select count(*) as rowCount");
	    query.append(" from transaction where transaction_type='AR'");;
	    query.append(" and cust_no=? and (bill_ladding_no=?  or invoice_number=?)");;
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, customerNumber);
	    if (MigrationUtils.isNotEmpty(billOfLading)) {
		lwStatement.setString(2, billOfLading);
		lwStatement.setString(3, billOfLading);
	    } else {
		lwStatement.setString(2, invoiceNumber);
		lwStatement.setString(3, invoiceNumber);
	    }
	    lwResult = lwStatement.executeQuery();
	    isNewAr = lwResult.next() && lwResult.getInt("rowCount") > 0 ? false : true;
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    isNewAr = isNewAr();
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	    return isNewAr;
	}
    }

    /**
     * insertTransaction - insert the invoice in logiware database
     * database - logiware and table - transaction
     * @throws Exception
     */
    private void insertTransaction() throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    StringBuilder insertQuery = new StringBuilder("insert into transaction");
	    insertQuery.append(" (cust_name,cust_no,Bill_Ladding_No,Invoice_number,Transaction_date,posted_date,Transaction_type,");
	    insertQuery.append("Transaction_amt,Balance,Balance_In_Process,Currency_code,Bill_to,Credit_Hold,booking_no,Voyage_No,manifest_flag,");
	    insertQuery.append("shipper_name,shipper_no,Cons_name,Cons_no,Fwd_name,Fwd_no,Third_Pty_name,Third_Pty_no,Agent_name,Agent_no,");
	    insertQuery.append("Destination,customer_reference_no,vessel_name,Container_No,steam_ship_line,seal_no,eta,Created_On,drcpt)");
	    insertQuery.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    lwStatement = lwConn.prepareStatement(insertQuery.toString());
	    lwStatement.setString(1, (String) values.get("customerName"));
	    lwStatement.setString(2, (String) values.get("customerNumber"));
	    lwStatement.setString(3, (String) values.get("billOfLading"));
	    lwStatement.setString(4, (String) values.get("invoiceNumber"));
	    lwStatement.setDate(5, (Date) values.get("transactionDate"));
	    lwStatement.setDate(6, (Date) values.get("postedDate"));
	    lwStatement.setString(7, "AR");
	    lwStatement.setDouble(8, (Double) values.get("invoiceAmount"));
	    lwStatement.setDouble(9, (Double) values.get("invoiceAmount"));
	    lwStatement.setDouble(10, (Double) values.get("invoiceAmount"));
	    lwStatement.setString(11, "USD");
	    lwStatement.setString(12, "Y");
	    lwStatement.setString(13, (String) values.get("creditHold"));
	    lwStatement.setString(14, (String) values.get("bookingNumber"));
	    lwStatement.setString(15, (String) values.get("voyageNumber"));
	    lwStatement.setString(16, (String) values.get("manifestFlag"));
	    lwStatement.setString(17, (String) values.get("shipperName"));
	    lwStatement.setString(18, (String) values.get("shipperNumber"));
	    lwStatement.setString(19, (String) values.get("consigneeName"));
	    lwStatement.setString(20, (String) values.get("consigneeNumber"));
	    lwStatement.setString(21, (String) values.get("forwarderName"));
	    lwStatement.setString(22, (String) values.get("forwarderNumber"));
	    lwStatement.setString(23, (String) values.get("thirdPartyName"));
	    lwStatement.setString(24, (String) values.get("thirdPartyNumber"));
	    lwStatement.setString(25, (String) values.get("agentName"));
	    lwStatement.setString(26, (String) values.get("agentNumber"));
	    lwStatement.setString(27, (String) values.get("destination"));
	    lwStatement.setString(28, (String) values.get("customerReferenceNumber"));
	    lwStatement.setString(29, (String) values.get("vesselName"));
	    lwStatement.setString(30, (String) values.get("containerNumber"));
	    lwStatement.setString(31, (String) values.get("steamShipLine"));
	    lwStatement.setString(32, (String) values.get("sealNumber"));
	    lwStatement.setDate(33, (Date) values.get("eta"));
	    lwStatement.setTimestamp(34, new Timestamp(System.currentTimeMillis()));
            lwStatement.setString(35, (String) values.get("drcpt"));
	    lwStatement.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    insertTransaction();
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * updateTransaction - update the invoice in logiware database
     * database - logiware and table - transaction
     * @throws Exception
     */
    private void updateTransaction(boolean isArLoaded) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    String customerNumber = (String) values.get("customerNumber");
	    String billOfLading = (String) values.get("billOfLading");
	    String invoiceNumber = (String) values.get("invoiceNumber");
	    StringBuilder query = new StringBuilder("select transaction_id,Transaction_date,Invoice_number from transaction");
	    query.append(" where cust_no=? and (bill_ladding_no=?  or invoice_number=?) and transaction_type='AR' order by transaction_id limit 1");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, customerNumber);
	    if (MigrationUtils.isNotEmpty(billOfLading)) {
		lwStatement.setString(2, billOfLading);
		lwStatement.setString(3, billOfLading);
	    } else {
		lwStatement.setString(2, invoiceNumber);
		lwStatement.setString(3, invoiceNumber);
	    }
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next()) {
		int transactionId = lwResult.getInt("transaction_id");
		String oldInvoiceNumber = lwResult.getString("Invoice_number");
		if (MigrationUtils.isNotEqualIgnoreCase(oldInvoiceNumber, "PRE PAYMENT")) {
		    Date invoiceDate = lwResult.getDate("Transaction_date");
		    values.put("invoiceDate", invoiceDate);
		}
		lwResult.close();
		lwStatement.close();
		StringBuilder updateQuery = new StringBuilder("update transaction");
		updateQuery.append(" set shipper_name=?,shipper_no=?,Cons_name=?,Cons_no=?,Fwd_name=?,Fwd_no=?,");
		updateQuery.append("Third_Pty_name=?,Third_Pty_no=?,Agent_name=?,Agent_no=?,Credit_Hold=?,");
		updateQuery.append("vessel_name=?,Container_No=?,steam_ship_line=?,seal_no=?,eta=?,manifest_flag=?,");
		updateQuery.append("booking_no=?,Voyage_No=?,customer_reference_no=?,Updated_On=?,drcpt=?");
		if (!isArLoaded) {
		    updateQuery.append(",transaction_amt=transaction_amt+?,balance=balance+?,balance_in_process=balance_in_process+?");
		}
		updateQuery.append(" where transaction_id=").append(transactionId);
		lwStatement = lwConn.prepareStatement(updateQuery.toString());
		lwStatement.setString(1, (String) values.get("shipperName"));
		lwStatement.setString(2, (String) values.get("shipperNumber"));
		lwStatement.setString(3, (String) values.get("consigneeName"));
		lwStatement.setString(4, (String) values.get("consigneeNumber"));
		lwStatement.setString(5, (String) values.get("forwarderName"));
		lwStatement.setString(6, (String) values.get("forwarderNumber"));
		lwStatement.setString(7, (String) values.get("thirdPartyName"));
		lwStatement.setString(8, (String) values.get("thirdPartyNumber"));
		lwStatement.setString(9, (String) values.get("agentName"));
		lwStatement.setString(10, (String) values.get("agentNumber"));
		lwStatement.setString(11, (String) values.get("creditHold"));
		lwStatement.setString(12, (String) values.get("vesselName"));
		lwStatement.setString(13, (String) values.get("containerNumber"));
		lwStatement.setString(14, (String) values.get("steamShipLine"));
		lwStatement.setString(15, (String) values.get("sealNumber"));
		lwStatement.setDate(16, (Date) values.get("eta"));
		lwStatement.setString(17, (String) values.get("manifestFlag"));
		lwStatement.setString(18, (String) values.get("bookingNumber"));
		lwStatement.setString(19, (String) values.get("voyageNumber"));
		lwStatement.setString(20, (String) values.get("customerReferenceNumber"));
		lwStatement.setTimestamp(21, new Timestamp(System.currentTimeMillis()));
                lwStatement.setString(22, (String) values.get("drcpt"));
                if (!isArLoaded) {
                    lwStatement.setDouble(23, (Double) values.get("invoiceAmount"));
                    lwStatement.setDouble(24, (Double) values.get("invoiceAmount"));
                    lwStatement.setDouble(25, (Double) values.get("invoiceAmount"));
                }
		lwStatement.executeUpdate();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    updateTransaction(isArLoaded);
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * insertHistory - insert history into logiware database
     * database - logiware and table - ar_transaction_history
     * @throws Exception
     */
    private void insertHistory() throws Exception {
	PreparedStatement lwStatement = null;
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("insert into ar_transaction_history");
	    query.append(" (customer_number,bl_number,invoice_number,voyage_number,invoice_date,");
	    query.append("transaction_date,posted_date,transaction_amount,transaction_type,customer_reference_number,created_date,created_by)");
	    query.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, (String) values.get("customerNumber"));
	    lwStatement.setString(2, (String) values.get("billOfLading"));
	    lwStatement.setString(3, (String) values.get("invoiceNumber"));
	    lwStatement.setString(4, (String) values.get("voyageNumber"));
	    lwStatement.setDate(5, (Date) values.get("invoiceDate"));
	    lwStatement.setDate(6, (Date) values.get("transactionDate"));
	    lwStatement.setDate(7, (Date) values.get("postedDate"));
	    lwStatement.setDouble(8, (Double) values.get("invoiceAmount"));
	    lwStatement.setString(9, (String) values.get("transactionType"));
	    lwStatement.setString(10, (String) values.get("customerReferenceNumber"));
	    lwStatement.setDate(11, (Date) values.get("createdDate"));
	    lwStatement.setString(12, "System");
	    lwStatement.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    insertHistory();
	} catch (Exception e) {
	    throw e;
	} finally {
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * insertSubledger - insert subledger into logiware database
     * database - logiware and table - transaction_ledger
     * @throws Exception
     */
    private void insertSubledger() throws Exception {
	PreparedStatement lwStatement = null;
	try {
	    StringBuilder insertQuery = new StringBuilder("insert into transaction_ledger");
	    insertQuery.append(" (cust_name,cust_no,Bill_Ladding_No,Invoice_number,Transaction_date,posted_date,Transaction_type,");
	    insertQuery.append("Transaction_amt,Balance,Balance_In_Process,Currency_code,Bill_to,status,booking_no,Voyage_No,manifest_flag,");
	    insertQuery.append("shipper_name,shipper_no,Cons_name,Cons_no,Fwd_name,Fwd_no,Third_Pty_name,Third_Pty_no,Agent_name,Agent_no,");
	    insertQuery.append("Destination,customer_reference_no,Vessel_No,Container_No,BL_Terms,");
	    insertQuery.append("bluescreen_chargecode,Charge_Code,GL_account_number,shipment_type,Subledger_Source_code,Created_On,drcpt)");
	    insertQuery.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    List<Subledger> subledgers = (List<Subledger>) values.get("subledgers");
	    for (Subledger subledger : subledgers) {
		lwStatement = lwConn.prepareStatement(insertQuery.toString());
		lwStatement.setString(1, (String) values.get("customerName"));
		lwStatement.setString(2, (String) values.get("customerNumber"));
		lwStatement.setString(3, (String) values.get("billOfLading"));
		lwStatement.setString(4, (String) values.get("invoiceNumber"));
		lwStatement.setDate(5, (Date) values.get("invoiceDate"));
		lwStatement.setDate(6, (Date) values.get("postedDate"));
		lwStatement.setString(7, "AR");
		lwStatement.setDouble(8, subledger.getAmount());
		lwStatement.setDouble(9, subledger.getAmount());
		lwStatement.setDouble(10, subledger.getAmount());
		lwStatement.setString(11, "USD");
		lwStatement.setString(12, "Y");
		lwStatement.setString(13, "Open");
		lwStatement.setString(14, (String) values.get("bookingNumber"));
		lwStatement.setString(15, (String) values.get("voyageNumber"));
		lwStatement.setString(16, (String) values.get("manifestFlag"));
		lwStatement.setString(17, (String) values.get("shipperName"));
		lwStatement.setString(18, (String) values.get("shipperNumber"));
		lwStatement.setString(19, (String) values.get("consigneeName"));
		lwStatement.setString(20, (String) values.get("consigneeNumber"));
		lwStatement.setString(21, (String) values.get("forwarderName"));
		lwStatement.setString(22, (String) values.get("forwarderNumber"));
		lwStatement.setString(23, (String) values.get("thirdPartyName"));
		lwStatement.setString(24, (String) values.get("thirdPartyNumber"));
		lwStatement.setString(25, (String) values.get("agentName"));
		lwStatement.setString(26, (String) values.get("agentNumber"));
		lwStatement.setString(27, (String) values.get("destination"));
		lwStatement.setString(28, (String) values.get("customerReferenceNumber"));
		lwStatement.setString(29, (String) values.get("vesselNumber"));
		lwStatement.setString(30, (String) values.get("containerNumber"));
		lwStatement.setString(31, (String) values.get("blTerms"));
		lwStatement.setString(32, subledger.getBluescreenChargeCode());
		lwStatement.setString(33, subledger.getChargeCode());
		lwStatement.setString(34, subledger.getGlAccount());
		lwStatement.setString(35, subledger.getShipmentType());
		if (MigrationUtils.isEqual((String) values.get("transactionType"), "CN")) {
		    lwStatement.setString(36, "AR-CN");
		} else {
		    lwStatement.setString(36, "AR-" + subledger.getShipmentType());
		}
		lwStatement.setTimestamp(37, new Timestamp(System.currentTimeMillis()));
                lwStatement.setString(38, (String) values.get("drcpt"));
		lwStatement.executeUpdate();
		lwStatement.close();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    insertSubledger();
	} finally {
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * insertLog - insert log in logiware database
     * database - logiware and table - ar_migration_log
     * @param fileName
     * @param lineNumber
     * @param logType
     * @throws Exception
     */
    private void insertLog(String fileName, int lineNumber, String logType) throws Exception {
	PreparedStatement lwStatement = null;
	try {
	    StringBuilder query = new StringBuilder("insert into ar_migration_log");
	    query.append(" (blue_screen_account,customer_number,bl_number,invoice_number,log_type,error,");
	    query.append(" reported_date,file_name,file_line_number,no_of_reprocess,ar_loaded)");
	    query.append(" values(?,?,?,?,?,?,?,?,?,?,?)");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, (String) values.get("bsAccountNo"));
	    lwStatement.setString(2, (String) values.get("customerNumber"));
	    lwStatement.setString(3, (String) values.get("billOfLading"));
	    lwStatement.setString(4, (String) values.get("invoiceNumber"));
	    lwStatement.setString(5, logType);
	    lwStatement.setString(6, errors.append(warnings).toString());
	    lwStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
	    lwStatement.setString(8, fileName);
	    lwStatement.setInt(9, lineNumber);
	    lwStatement.setInt(10, 0);
	    lwStatement.setBoolean(11, canLoadAr);
	    lwStatement.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    insertLog(fileName, lineNumber, logType);
	} finally {
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * insertReprocessLog - insert log in logiware database
     * database - logiware and table - ar_migration_reprocess_log
     * @param id
     * @param logType
     * @throws Exception
     */
    private void insertReprocessLog(Integer id, String logType) throws Exception {
	PreparedStatement lwStatement = null;
	try {
	    StringBuilder query = new StringBuilder("insert into ar_migration_reprocess_log");
	    query.append(" (ar_migration_log_id,log_type,error,reported_date)");
	    query.append(" values(?,?,?,?)");
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setInt(1, id);
	    lwStatement.setString(2, logType);
	    lwStatement.setString(3, errors.append(warnings).toString());
	    lwStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	    lwStatement.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    insertReprocessLog(id, logType);
	} finally {
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * updateLog - update log status and number of reprocess
     * database - logiware and table - ar_migration_log
     * @param id
     * @param logType
     * @throws Exception
     */
    private void updateLog(Integer id, String logType) throws Exception {
	PreparedStatement lwStatement = null;
	try {
	    insertReprocessLog(id, logType);
	    StringBuilder query = new StringBuilder("update ar_migration_log");
	    query.append(" set log_type=?,ar_loaded=?,no_of_reprocess=no_of_reprocess+1");
	    query.append(" where id=").append(id);
	    lwStatement = lwConn.prepareStatement(query.toString());
	    lwStatement.setString(1, logType);
	    lwStatement.setBoolean(2, canLoadAr);
	    lwStatement.executeUpdate();
	} catch (SQLException e) {
	    e.printStackTrace();
	    reconnectToLogiware();
	    updateLog(id, logType);
	} finally {
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * setValues - extract data from csv as well as bluescreen db and set it to map
     * @param readRow
     * @param logType
     * @throws Exception
     */
    private void setValues(DataRow readRow, String logType) throws Exception {
	String bsAccountNo = readRow.getString(1);
	String bsCreatedDate = readRow.getString(5);
	double invoiceAmount = readRow.getDouble(6);
	String type = readRow.getString(7);
	String correctionNumber = readRow.getString(10);
	String billOfLading = readRow.getString(12);
	String invoiceNumber = readRow.getString(13);
	String bsTransactionDate = readRow.getString(15);
	setInvoiceOrBl(billOfLading, invoiceNumber);
	setTradingPartner(bsAccountNo);
	setInvoiceAmount(invoiceAmount);
	setTransactionType(type);
	setCreatedDate(bsCreatedDate);
	setTransactionDate(bsTransactionDate);
	setInvoiceDate(bsTransactionDate);
	setPostedDate(bsTransactionDate);
	setBookingNumber(billOfLading, invoiceNumber);
	if (MigrationUtils.isEqual(type, "4")) {
	    if (MigrationUtils.isNotEqual(logType, "warning")) {
		setInvSubledgers(invoiceNumber, invoiceAmount);
	    }
	} else {
         values.put("drcpt", StringUtils.substring(billOfLading.trim(),7 ,13));
	    if (billOfLading.substring(5).startsWith("04")) {
		setFclBlValues(logType, bsAccountNo, billOfLading, type, correctionNumber, invoiceAmount);
	    } else {
		setLclOrAirBlValues(logType, bsAccountNo, billOfLading, type, correctionNumber, invoiceAmount);
	    }
	}
    }

    /**
     * loadData - load data from csv files into logiware
     * @throws Exception
     */
    private void loadData() throws Exception {
	File preprocessedDir = new File(prop.getProperty("openarPreprocessed"));
	File processedDir = new File(prop.getProperty("openarProcessed"));
	File errorDir = new File(prop.getProperty("openarError"));
	File[] files = preprocessedDir.listFiles(new FilenameFilter("csv"));
	Arrays.sort(files, new FileComparator());
	for (File preprocessedFile : files) {
	    DataFile readData = DataFile.createReader("8859_1");
	    readData.setDataFormat(new CSVFormat());
	    readData.open(preprocessedFile);
	    int lineNumber = 1;
	    for (DataRow readRow = readData.next(); readRow != null; readRow = readData.next()) {
		totalReconnectCount = 0;
		errors = new StringBuilder();
		warnings = new StringBuilder();
		values.clear();
		canLoadAr = true;
		if (readRow.size() >= 17) {
		    String companyCode = readRow.getString(16);
		    double invoiceAmount = readRow.getDouble(6);
		    if (MigrationUtils.isNotEqual(companyCode, "01") && MigrationUtils.isNotEmpty(invoiceAmount)) {
			try {
			    setValues(readRow, null);
			    if (canLoadAr) {
				if (isNewAr()) {
				    insertTransaction();
				} else {
				    updateTransaction(false);
				}
				insertHistory();
			    }
			    if (MigrationUtils.isNotEmpty(errors.toString())) {
				warnings = new StringBuilder();
				insertLog(preprocessedFile.getName(), lineNumber, "error");
				MigrationUtils.createErrorFile(readRow, new File(errorDir, preprocessedFile.getName().replace(".csv", "") + lineNumber + ".csv"));
			    } else {
				List<Subledger> subledgers = (List<Subledger>) values.get("subledgers");
				if (null == subledgers || subledgers.isEmpty()) {
				    errors.append("<li>All charges are missing in bluescreen</li>");
				    warnings = new StringBuilder();
				    insertLog(preprocessedFile.getName(), lineNumber, "error");
				    MigrationUtils.createErrorFile(readRow, new File(errorDir, preprocessedFile.getName().replace(".csv", "") + lineNumber + ".csv"));
				} else {
				    insertSubledger();
				    if (MigrationUtils.isNotEmpty(warnings.toString())) {
					insertLog(preprocessedFile.getName(), lineNumber, "warning");
					MigrationUtils.createErrorFile(readRow, new File(errorDir, preprocessedFile.getName().replace(".csv", "") + lineNumber + ".csv"));
				    } else {
					insertLog(preprocessedFile.getName(), lineNumber, "processed");
				    }
				}
			    }
			} catch (Exception e) {
			    errors.append("<li>").append(MigrationUtils.getStackTrace(e)).append("</li>");
			    warnings = new StringBuilder();
			    insertLog(preprocessedFile.getName(), lineNumber, "error");
			    MigrationUtils.createErrorFile(readRow, new File(errorDir, preprocessedFile.getName().replace(".csv", "") + lineNumber + ".csv"));
			}
		    }
		}
		lineNumber++;
	    }
	    MigrationUtils.closeCsvFile(readData);
	    MigrationUtils.moveFile(preprocessedFile, new File(processedDir, preprocessedFile.getName()));
	}
    }

    /**
     * migrate - initiate the migration process
     * @throws Exception
     */
    private void run() throws Exception {
	try {
	    //Load properties from properties file
	    loadProperties();
	    //Connect to bluescreen and logiware databases
	    connect();
	    //Get the preprocessed,processed and error folders
	    MigrationUtils.createFolder(new File(prop.getProperty("openarPreprocessed")));
	    MigrationUtils.emptyFolder(new File(prop.getProperty("openarPreprocessed")));//Required to avoid duplicates
	    MigrationUtils.createFolder(new File(prop.getProperty("openarProcessed")));
	    MigrationUtils.createFolder(new File(prop.getProperty("openarError")));
	    if (MigrationUtils.isEqualIgnoreCase("ftp", prop.getProperty("useFtp"))) {
		//Copy csv files from Remote folder using ftp to preprocessed folder
		String ip = prop.getProperty("ftpIp");
		String userName = prop.getProperty("ftpUserName");
		String password = prop.getProperty("ftpPassword");
		String ftpOpenArFileFolder = prop.getProperty("ftpOpenArFileFolder");
		MigrationUtils.copyRemoteFiles(ip, userName, password, ftpOpenArFileFolder, prop.getProperty("openarPreprocessed"));
	    } else {
		//Move csv files from local folder to preprocessed folder
		MigrationUtils.moveLocalFiles(prop.getProperty("openarCsv"), prop.getProperty("openarPreprocessed"));
	    }
	    //Load data from csv files
	    loadData();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    //disconnect from bluescreen and logiware databases
	    disconnect();
	}
    }

    /**
     * reloadData - load data from csv file into logiware database with the help of bluescreen counterpart
     * @param id
     * @throws Exception
     */
    private void reloadData(Integer id) throws Exception {
	PreparedStatement lwStatement = null;
	ResultSet lwResult = null;
	try {
	    File lwReprocessedDir = new File(prop.getProperty("openarReprocessed"));
	    File lwErrorDir = new File(prop.getProperty("openarError"));
	    String query = "select * from ar_migration_log where id=? and (log_type='error' or log_type='warning')";
	    lwStatement = lwConn.prepareStatement(query);
	    lwStatement.setInt(1, id);
	    lwResult = lwStatement.executeQuery();
	    if (lwResult.next()) {
		String fileName = lwResult.getString("file_name");
		String lineNumber = lwResult.getString("file_line_number");
		boolean isArLoaded = lwResult.getBoolean("ar_loaded");
		String logType = lwResult.getString("log_type");
		File errorFile = new File(lwErrorDir, fileName.replace(".csv", "") + lineNumber + ".csv");
		if (errorFile.exists()) {
		    boolean canMove = true;
		    DataFile csvFile = DataFile.createReader("8859_1");
		    csvFile.setDataFormat(new CSVFormat());
		    csvFile.open(errorFile);
		    DataRow row = csvFile.next();
		    if (null != row && row.size() >= 17) {
			reprocessLog = "success";
			totalReconnectCount = 0;
			errors = new StringBuilder();
			warnings = new StringBuilder();
			values.clear();
			canLoadAr = true;
			String companyCode = row.getString(16);
			double invoiceAmount = row.getDouble(6);
			if (MigrationUtils.isNotEqual(companyCode, "01") && MigrationUtils.isNotEmpty(invoiceAmount)) {
			    setValues(row, logType);
			    try {
				if (canLoadAr) {
				    if (MigrationUtils.isEqual(logType, "warning")) {
					if (isArLoaded) {
					    updateTransaction(isArLoaded);
					}
					if (MigrationUtils.isNotEmpty(warnings.toString())) {
					    updateLog(id, "warning");
					    reprocessLog = warnings.toString();
					    canMove = false;
					} else {
					    updateLog(id, "re-processed");
					}
				    } else {
					if (isArLoaded) {
					    updateTransaction(isArLoaded);
					} else {
					    if (isNewAr()) {
						insertTransaction();
					    } else {
						updateTransaction(isArLoaded);
					    }
					    insertHistory();
					}
					if (MigrationUtils.isNotEmpty(errors.toString())) {
					    warnings = new StringBuilder();
					    updateLog(id, "error");
					    reprocessLog = errors.toString();
					    canMove = false;
					} else {
					    insertSubledger();
					    if (MigrationUtils.isNotEmpty(warnings.toString())) {
						updateLog(id, "warning");
						reprocessLog = warnings.toString();
						canMove = false;
					    } else {
						updateLog(id, "re-processed");
					    }
					}
				    }
				} else {
				    warnings = new StringBuilder();
				    updateLog(id, "error");
				    reprocessLog = errors.toString();
				    canMove = false;
				}
			    } catch (Exception e) {
				errors.append("<li>").append(MigrationUtils.getStackTrace(e)).append("</li>");
				warnings = new StringBuilder();
				updateLog(id, "error");
				reprocessLog = errors.toString();
				canMove = false;
			    }
			}
		    }
		    csvFile.close();
		    MigrationUtils.closeCsvFile(csvFile);
		    if (canMove) {
			MigrationUtils.moveFile(errorFile, new File(lwReprocessedDir, errorFile.getName()));
		    }
		} else {
		    errors.append("<li>Error file not found</li>");
		    updateLog(id, "error");
		    reprocessLog = errors.append(warnings).toString();
		}
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    MigrationUtils.closeResult(lwResult);
	    MigrationUtils.closeStatement(lwStatement);
	}
    }

    /**
     * migrate - initiate the migration process
     * @throws Exception
     */
    private void loadSingleError(Integer id) throws Exception {
	try {
	    //Load properties from properties file
	    loadProperties();
	    //Connect to bluescreen and logiware databases
	    connect();
	    //Get the processed and error folders
	    MigrationUtils.createFolder(new File(prop.getProperty("openarProcessed")));
	    MigrationUtils.createFolder(new File(prop.getProperty("openarError")));
	    //Load data from csv files
	    reloadData(id);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    //disconnect from bluescreen and logiware databases
	    disconnect();
	}
    }

    /**
     * reprocessSingleError - reprocess single loading error based on id of the log
     * @param id
     * @throws Exception
     */
    public String reprocessSingleError(Integer id) throws Exception {
	loadSingleError(id);
	return reprocessLog;
    }

    /**
     * reprocessAllErrors - reprocessing all the errors based on log status
     * @param reprocessLimit
     * @throws Exception
     */
    private void loadAllErrors() throws Exception {
	PreparedStatement errorStatement = null;
	ResultSet errorResult = null;
	try {
	    //Load properties from properties file
	    loadProperties();
	    //Connect to bluescreen and logiware databases
	    connect();
	    //Get the processed and error folders
	    MigrationUtils.createFolder(new File(prop.getProperty("openarProcessed")));
	    MigrationUtils.createFolder(new File(prop.getProperty("openarError")));
	    //Get all the errors
	    StringBuilder query = new StringBuilder("select * from ar_migration_log");
	    query.append(" where (log_type='error' or log_type='warning')");
	    errorStatement = lwConn.prepareStatement(query.toString());
	    errorResult = errorStatement.executeQuery();
	    while (errorResult.next()) {
		Integer id = errorResult.getInt("id");
		try {
		    reloadData(id);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    MigrationUtils.closeResult(errorResult);
	    MigrationUtils.closeStatement(errorStatement);
	    disconnect();
	}
    }

    /**
     * reprocessAllErrors - reprocess all the errors based on log status
     * @param reprocessLimit
     * @throws Exception
     */
    public void reprocessAllErrors() throws Exception {
	loadAllErrors();
    }
}

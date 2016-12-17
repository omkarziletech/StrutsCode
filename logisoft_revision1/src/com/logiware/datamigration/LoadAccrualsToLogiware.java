package com.logiware.datamigration;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataRow;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class LoadAccrualsToLogiware {

    //Global fields
    private static final Logger log = Logger.getLogger(LoadAccrualsToLogiware.class);
    private String reprocessLog = null;
    private Properties prop;
    private Connection bsConn;
    private Connection lwConn;
    private StringBuilder errors = new StringBuilder();
    private StringBuilder warnings = new StringBuilder();
    private final DataMap<String, Object> data = new DataMapImpl<String, Object>();
    private final String propertyPath = "/com/logiware/datamigration/dbconnection.properties";
    private final List<String> headers = new ArrayList<String>();

    /**
     * loadProperties - load the properties like db connection details,ftp
     * connection details
     *
     * @throws Exception
     */
    private void loadProperties() throws Exception {
        prop = new Properties();
        prop.load(getClass().getResourceAsStream(propertyPath));
    }

    /**
     * connectToBluescreen - setup connection with bluescreen
     *
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
        } catch (ClassNotFoundException e) {
            log.info("Bluescreen database connection failed...");
            throw e;
        } catch (SQLException e) {
            log.info("Bluescreen database connection failed...");
            throw e;
        }
    }

    /**
     * connectToLogiware - setup connection with logiware
     *
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
        } catch (ClassNotFoundException e) {
            log.info("Logiware database connection failed...");
            throw e;
        } catch (SQLException e) {
            log.info("Logiware database connection failed...");
            throw e;
        }
    }

    /**
     * connect - setup connection to both bluescreen and logiware
     *
     * @throws Exception
     */
    private void connect() throws Exception {
        connectToBluescreen();
        connectToLogiware();
    }

    /**
     * addCsvHeader - add headers to get the value of the fields by its name
     *
     * @throws Exception
     */
    private void addCsvHeader() throws Exception {
        headers.add("type");
        headers.add("askfld");
        headers.add("cntrl");
        headers.add("cstcde");
        headers.add("amount");
        headers.add("paiddt");
        headers.add("eacode");
        headers.add("gltrml");
        headers.add("vendnm");
        headers.add("vend");
        headers.add("invnum");
        headers.add("invdat");
        headers.add("entdat");
        headers.add("entrby");
        headers.add("enttim");
        headers.add("postby");
        headers.add("posted");
        headers.add("posttm");
        headers.add("chknum");
        headers.add("venref");
        headers.add("expvoy");
        headers.add("inlvoy");
        headers.add("unit");
        headers.add("cstdr");
        headers.add("updtby");
        headers.add("upddat");
        headers.add("updtim");
        headers.add("postdt");
        headers.add("mstvn");
        headers.add("uadrs1");
        headers.add("uadrs2");
        headers.add("uacity");
        headers.add("uastat");
        headers.add("uazip");
        headers.add("uaphn");
        headers.add("uafax");
        headers.add("editky");
        headers.add("appddt");
        headers.add("appdby");
        headers.add("appdtm");
        headers.add("autocd");
        headers.add("cmmnts");
        headers.add("mstck");
        headers.add("agtvn");
        headers.add("key022");
        headers.add("faecde");
        headers.add("actcod");
        headers.add("cutamt");
        headers.add("cutdte");
        headers.add("cuttim");
        headers.add("voidby");
        headers.add("voiddt");
        headers.add("voidtm");
    }

    /**
     * extractRow - extract row from the csv file
     *
     * @param row - data row from csv file
     */
    private void extractData(DataRow row) {
        int rowIndex = 0;
        for (String header : headers) {
            data.putString(header, row, rowIndex);
            rowIndex++;
        }
        StringBuilder apcostkey = new StringBuilder();
        apcostkey.append(data.getString("type")).append(data.getString("askfld")).append(data.getString("cntrl"));
        data.putString("apcostkey", apcostkey.toString());
        data.putString("bsCostCode", data.getString("cstcde"));
        data.putString("invoiceNumber", data.getString("invnum"));
        data.putDouble("accrualAmount", Double.parseDouble(data.getString("amount")));
        data.putString("checkNumber", data.getString("checknum"));
        data.putString("inlandVoyageNumber", data.getString("inlvoy"));
        data.putString("flag", data.getString("actcod"));
    }

    /**
     * extractRow - extract row from the csv file
     *
     * @param row - data row from csv file
     */
    private void extractData(ResultSet row) throws Exception {
        for (String header : headers) {
            data.putString(header, row);
        }
        StringBuilder apcostkey = new StringBuilder();
        apcostkey.append(data.getString("type")).append(data.getString("askfld")).append(data.getString("cntrl"));
        data.putString("apcostkey", apcostkey.toString());
        data.putString("bsCostCode", data.getString("cstcde"));
        data.putString("invoiceNumber", data.getString("invnum"));
        data.putDouble("accrualAmount", Double.parseDouble(data.getString("amount")));
        data.putString("checkNumber", data.getString("checknum"));
        data.putString("inlandVoyageNumber", data.getString("inlvoy"));
        data.putString("flag", data.getString("actcod"));
    }

    private void deleteAccrual() throws Exception {
        PreparedStatement bsStatement = null;
        try {
            String apcostkey = data.getString("apcostkey");
            StringBuilder query = new StringBuilder();
            query.append("delete from transaction_ledger");
            query.append(" where transaction_type='AC' and status='Open' and apcostkey=?");
            bsStatement = lwConn.prepareStatement(query.toString());
            bsStatement.setString(1, apcostkey);
            bsStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void findAccrualStatus() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String apcostkey = data.getString("apcostkey");
            StringBuilder query = new StringBuilder();
            query.append("select if(count(*)>=1,status,'New') as status");
            query.append(" from transaction_ledger");
            query.append(" where transaction_type='AC' and apcostkey=?");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, apcostkey);
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                data.putString("status", lwResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * setBluescreenVendor - set bluescreen vendor
     */
    private void setBluescreenVendor() {
        if (MigrationUtils.isEqualIgnoreCase(data.getString("eacode"), "A")
                && MigrationUtils.isNotEqualIgnoreEmpty(data.getString("agtvn"), "00000")
                && MigrationUtils.isAllNotEqual(data.getString("cstcde"), "012", "112")) {
            data.putString("bsVendorNumber", data.getString("agtvn"));
        } else {
            data.putString("bsVendorNumber", data.getString("vend"));
        }
    }

    /**
     * changeLogiwareVendor - changing logiware vendor for disabled account
     * database - logiware and table - trading_partner
     *
     * @throws Exception
     */
    private void changeLogiwareVendor(String forwardAccount) throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String vendorName = null;
            String vendorNumber = null;
            StringBuilder query = new StringBuilder();
            query.append("select trim(tp.acct_name) as vendor_name,trim(tp.acct_no) as vendor_number");
            query.append(" from trading_partner tp");
            query.append(" where tp.type!='master' and tp.acct_type like '%V%' and tp.acct_no=? limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, forwardAccount);
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                vendorName = lwResult.getString("vendor_name");
                vendorNumber = lwResult.getString("vendor_number");
            }
            if (MigrationUtils.isNotEmpty(vendorNumber)) {
                data.putString("vendorName", vendorName);
                data.putString("vendorNumber", vendorNumber);
            } else {
                errors.append("<li>No trading partner found for this logiware account - ").append(forwardAccount).append("</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * setLogiwareVendor - setting logiware vendor from bluescreen vendor
     * database - logiware and table - trading_partner
     *
     * @throws Exception
     */
    private void setLogiwareVendor() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String bsVendorNumber = data.getString("bsVendorNumber");
            String vendorName = null;
            String vendorNumber = null;
            String forwardAccount = null;
            StringBuilder query = new StringBuilder();
            query.append("select trim(tp.acct_name) as vendor_name,trim(tp.acct_no) as vendor_number,");
            query.append("if(tp.disabled='Y' and tp.forward_account!='',tp.forward_account,'') as forward_account");
            query.append(" from trading_partner tp");
            query.append(" where tp.type!='master' and tp.acct_type like '%V%' and tp.ecivendno=? limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, bsVendorNumber);
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                vendorName = lwResult.getString("vendor_name");
                vendorNumber = lwResult.getString("vendor_number");
                forwardAccount = lwResult.getString("forward_account");
            }
            if (MigrationUtils.isEmpty(vendorNumber)) {
                MigrationUtils.closeResult(lwResult);
                MigrationUtils.closeStatement(lwStatement);
                query = new StringBuilder();
                query.append("select trim(tp.acct_name) as vendor_name,trim(tp.acct_no) as vendor_number,");
                query.append("if(tp.disabled='Y' and tp.forward_account!='',tp.forward_account,'') as forward_account");
                query.append(" from trading_partner tp");
                query.append(" where tp.type!='master' and tp.acct_type like '%V%' and tp.eci_acct_no=? limit 1");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, bsVendorNumber);
                lwResult = lwStatement.executeQuery();
                if (lwResult.next()) {
                    vendorName = lwResult.getString("vendor_name");
                    vendorNumber = lwResult.getString("vendor_number");
                    forwardAccount = lwResult.getString("forward_account");
                }
            }

            if (MigrationUtils.isEmpty(vendorNumber)) {
                MigrationUtils.closeResult(lwResult);
                MigrationUtils.closeStatement(lwStatement);
                query = new StringBuilder();
                query.append("select trim(tp.acct_name) as vendor_name,trim(tp.acct_no) as vendor_number,");
                query.append("if(tp.disabled='Y' and tp.forward_account!='',tp.forward_account,'') as forward_account");
                query.append(" from trading_partner tp");
                query.append(" where tp.type!='master' and tp.acct_type like '%V%' and tp.ssline_number=? limit 1");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, bsVendorNumber);
                lwResult = lwStatement.executeQuery();
                if (lwResult.next()) {
                    vendorName = lwResult.getString("vendor_name");
                    vendorNumber = lwResult.getString("vendor_number");
                    forwardAccount = lwResult.getString("forward_account");
                }
            }
            if (MigrationUtils.isNotEmpty(forwardAccount)) {
                changeLogiwareVendor(forwardAccount);
            } else if (MigrationUtils.isNotEmpty(vendorNumber)) {
                data.putString("vendorName", vendorName);
                data.putString("vendorNumber", vendorNumber);
            } else {
                errors.append("<li>No trading partner found for this bluescreen vendor - ").append(bsVendorNumber).append("</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * setFclContainerNumber - set container number for FCL file database -
     * bluescreen and table - fcltrl
     */
    private void setFclContainerNumber() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String askfld = data.getString("askfld");
            StringBuilder query = new StringBuilder();
            query.append("select group_concat(fcltrl) as container_number");
            query.append(" from fcltrl");
            query.append(" where terml=? and drcpt=?");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, askfld.substring(0, 2));
            bsStatement.setString(2, askfld.substring(2).trim());
            bsResult = bsStatement.executeQuery();
            if (bsResult.next()) {
                data.putString("containerNumber", bsResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setFclData() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String askfld = data.getString("askfld");
            StringBuilder query = new StringBuilder();
            query.append("select trim(concat(itmnum,prtnum,terml,drcpt)) as bl_number,");
            query.append("if(datman=''||datman='0','N','Y') as manifest_flag,if(consl='A','FCLI','FCLE') as shipment_type,");
            query.append("date(if(consl='A',if((etadte!='0' and etadte!=''),etadte,null),");
            query.append("if((etddte!='0' and etddte!=''),etddte,null))) as transaction_date,");
            query.append("trim(itmnum) as billing_terminal,trim(cvytm) as loading_terminal,trim(terml) as drcpt_terminal,");
            query.append("trim(concat(cvytm,cvypt,cvyvy)) as voyage_number,trim(bookn) as booking_number");
            query.append(" from fclhed");
            query.append(" where terml=? and drcpt=? limit 1");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, askfld.substring(0, 2));
            bsStatement.setString(2, askfld.substring(2).trim());
            bsResult = bsStatement.executeQuery();
            if (bsResult.next()) {
                data.putString("blNumber", bsResult);
                data.putString("manifestFlag", bsResult);
                data.putString("shipmentType", bsResult);
                data.putDate("transactionDate", bsResult);
                data.putString("billingTerminal", bsResult);
                data.putString("loadingTerminal", bsResult);
                data.putString("drcptTerminal", bsResult);
                data.putString("voyageNumber", bsResult);
                data.putString("bookingNumber", bsResult);
            }
            setFclContainerNumber();
            if (MigrationUtils.isEmpty(data.getString("blNumber"))) {
                warnings.append("<li>BL Information is missing</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setLclOrAirBlNumber() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String askfld = data.getString("askfld");
            StringBuilder query = new StringBuilder();
            query.append("select trim(concat(blterm,port,drterm,drnum,drsuff)) as bl_number");
            query.append(" from bldr");
            query.append(" where dr1=? and dr2=?");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, askfld.substring(0, 2));
            bsStatement.setString(2, askfld.substring(2).trim());
            bsResult = bsStatement.executeQuery();
            if (bsResult.next()) {
                data.putString("blNumber", bsResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    /**
     * setLclOrAirVoyageNumber - set voyage number using bill of lading database
     * - bluescreen and table - dract
     *
     * @throws Exception
     */
    private void setLclOrAirVoyageNumber() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String blNumber = data.getString("blNumber");
            if (MigrationUtils.isNotEmpty(blNumber) && blNumber.length() >= 13) {
                String shipmentType = data.getString("shipmentType");
                StringBuilder query = new StringBuilder();
                query.append("select concat(inlotm,inldtm,inlvy) as voyage_number,inlotm as loading_terminal");
                query.append(" from dract");
                query.append(" where term=? and drcpt=?");
                bsStatement = bsConn.prepareStatement(query.toString());
                bsStatement.setString(1, blNumber.substring(5, 7));
                bsStatement.setString(2, blNumber.substring(7, 13).trim());
                bsResult = bsStatement.executeQuery();
                if (bsResult.next()) {
                    if (MigrationUtils.isNotEmpty(data.getString("inlvoy"))) {
                        data.putString("voyageNumber", data.getString("inlvoy"));
                    } else if (MigrationUtils.isNotEmpty(bsResult.getString("voyage_number"))) {
                        data.putString("voyageNumber", bsResult);
                    }
                    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")) {
                        if (MigrationUtils.isNotEmpty(data.getString("voyageNumber"))) {
                            data.putString("loadingTerminal", data.getString("voyageNumber").substring(0, 2));
                        } else if (MigrationUtils.isNotEmpty(bsResult.getString("loading_terminal"))) {
                            data.putString("loadingTerminal", bsResult);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    /**
     * setBookingNumber - set booking number using bill of lading database -
     * bluescreen and table - dract
     *
     * @throws Exception
     */
    private void setLclOrAirBookingNumber() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String voyageNumber = data.getString("voyageNumber");
            if (MigrationUtils.isNotEmpty(voyageNumber) && voyageNumber.length() >= 5) {
                StringBuilder query = new StringBuilder();
                query.append("select trim(booknn) as booking_number");
                query.append(" from voyage");
                query.append(" where ltrmln=? and prtnum=? and voyagn=?");
                bsStatement = bsConn.prepareStatement(query.toString());
                bsStatement.setString(1, voyageNumber.substring(0, 2));
                bsStatement.setString(2, voyageNumber.substring(2, 5));
                bsStatement.setString(3, voyageNumber.substring(5).trim());
                bsResult = bsStatement.executeQuery();
                if (bsResult.next()) {
                    data.putString("bookingNumber", bsResult);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setLclOrAirData() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            setLclOrAirBlNumber();
            String blNumber = data.getString("blNumber");
            if (MigrationUtils.isNotEmpty(blNumber) && blNumber.length() >= 13) {
                StringBuilder query = new StringBuilder();
                query.append("select if(mnfdte='' || mnfdte='0','N','Y') as manifest_flag,");
                query.append("if(blterm>79,if(domfor='D','AIRI','AIRE'),");
                query.append("if((domfor='D' and blterm<80 and blterm!=5 and blterm!=35),'LCLI','LCLE')) as shipment_type,");
                query.append("date(if(((blterm>79 and domfor='D') or (domfor='D' and blterm<80 and blterm!=5 and blterm!=35)),");
                query.append("if((mnfdte!='0' and mnfdte!=''),mnfdte,if((arrdat!='0' and arrdat!=''),arrdat,null)),");
                query.append("if((saildt!='0' and saildt!=''),saildt,null))) as transaction_date,");
                query.append("trim(blterm) as billing_terminal,trim(glterm) as loading_terminal,trim(drterm) as drcpt_terminal,");
                query.append("concat(glterm,port,cntvoy,cntsuf) as voyage_number,trim(contmn) as container_number,");
                query.append("convert(blterm,signed) as terminal_number");
                query.append(" from histry");
                query.append(" where blterm=? and port=? and drterm=? and drnum=? and drsuff=? limit 1");
                bsStatement = bsConn.prepareStatement(query.toString());
                bsStatement.setString(1, blNumber.substring(0, 2));
                bsStatement.setString(2, blNumber.substring(2, 5));
                bsStatement.setString(3, blNumber.substring(5, 7));
                bsStatement.setString(4, blNumber.substring(7, 13));
                bsStatement.setString(5, blNumber.substring(13).trim());
                bsResult = bsStatement.executeQuery();
                if (bsResult.next()) {
                    data.putString("manifestFlag", bsResult);
                    data.putString("shipmentType", bsResult);
                    data.putDate("transactionDate", bsResult);
                    data.putString("billingTerminal", bsResult);
                    data.putString("loadingTerminal", bsResult);
                    data.putString("drcptTerminal", bsResult);
                    data.putString("voyageNumber", bsResult);
                    data.putString("containerNumber", bsResult);
                    String shipmentType = data.getString("shipmentType");
                    if (MigrationUtils.isEqualIgnoreCase(shipmentType, "LCLI")
                            || (MigrationUtils.between(bsResult.getInt("terminal_number"), 60, 69)
                            && MigrationUtils.in(blNumber.substring(2, 5), "001", "008", "009", "015", "016", "017", "018", "019", "030"))) {
                        setLclOrAirVoyageNumber();
                    }
                    setLclOrAirBookingNumber();
                }
            }
            if (MigrationUtils.isEmpty(data.getString("blNumber"))) {
                warnings.append("<li>BL Information is missing</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setFclOrLclOrAirData() throws Exception {
        data.putString("dockReceipt", StringUtils.substring(data.getString("askfld").trim(),2 ,8));
        if (MigrationUtils.isStartsWith(data.getString("askfld"), "04")) {
            setFclData();
        } else {
            setLclOrAirData();
        }
        if (MigrationUtils.isEmpty(data.getString("containerNumber")) && MigrationUtils.isNotEmpty(data.getString("unit"))) {
            data.putString("containerNumber", data.getString("unit"));
        }
        if (MigrationUtils.isNotEmpty(data.getString("expvoy"))) {
            data.putString("voyageNumber", data.getString("expvoy"));
        } else if (MigrationUtils.isNotEmpty(data.getString("inlvoy"))) {
            data.putString("voyageNumber", data.getString("inlvoy"));
        }
    }

    private void setExportVoyageData() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String askfld = data.getString("askfld");
            StringBuilder query = new StringBuilder();
            query.append("select date(if((saildt!='0' and saildt!=''),saildt,null)) as transaction_date");
            query.append(" from voyage");
            query.append(" where ltrmln=? and prtnum=? and voyagn=?");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, askfld.substring(0, 2));
            bsStatement.setString(2, askfld.substring(2, 5));
            bsStatement.setString(3, askfld.substring(5, 10).trim());
            bsResult = bsStatement.executeQuery();
            if (bsResult.next()) {
                data.putString("manifestFlag", "Y");
                data.putDate("transactionDate", bsResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setExportContainerData() throws Exception {
        PreparedStatement bsSatement = null;
        ResultSet bsResult = null;
        try {
            String unit = data.getString("unit");
            if (MigrationUtils.isNotEmpty(unit) && MigrationUtils.isEqual(unit.length(), 28)) {
                StringBuilder query = new StringBuilder();
                query.append("select trim(mstrbl) as master_bl_number");
                query.append(" from unithd");
                query.append(" where trlnum=? and datent=?");
                bsSatement = bsConn.prepareStatement(query.toString());
                bsSatement.setString(1, unit.substring(0, 20).trim());
                bsSatement.setString(2, unit.substring(20).trim());
                bsResult = bsSatement.executeQuery();
                if (bsResult.next()) {
                    data.putString("masterBlNumber", bsResult);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsSatement);
        }
    }

    private void setExportVoyageOrContainerData() throws Exception {
        String askfld = data.getString("askfld");
        Integer terminalNumber = Integer.parseInt(askfld.substring(0, 2));
        String shipmentType = MigrationUtils.between(terminalNumber, 60, 69) ? "LCLI" : terminalNumber < 80 ? "LCLE" : "AIRE";
        data.putString("shipmentType", shipmentType);
        setExportVoyageData();
        setExportContainerData();
        data.putString("containerNumber", data.getString("unit"));
        data.putString("voyageNumber", data.getString("expvoy"));
        data.putString("loadingTerminal", data.getString("expvoy").substring(0, 2));
        data.putString("billingTerminal", data.getString("expvoy").substring(0, 2));
        data.putString("drcptTerminal", data.getString("expvoy").substring(0, 2));
    }

    private void setImportContainerData() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String unit = data.getString("unit");
            StringBuilder query = new StringBuilder();
            query.append("select trim(mstrbl) as master_bl_number,date(if(istpdt!='' and istpdt!='0',istpdt,null)) as transaction_date");
            query.append(" from unithd");
            query.append(" where trlnum=? and datent=?");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, unit.substring(0, 20).trim());
            bsStatement.setString(2, unit.substring(20).trim());
            bsResult = bsStatement.executeQuery();
            Date transactionDate = null;
            if (bsResult.next()) {
                data.putString("manifestFlag", "Y");
                data.putString("masterBlNumber", bsResult);
                transactionDate = bsResult.getDate("transaction_date");
                data.putDate("transactionDate", transactionDate);
            }
            if (null == transactionDate) {
                MigrationUtils.closeResult(bsResult);
                MigrationUtils.closeStatement(bsStatement);
                String askfld = data.getString("askfld");
                query = new StringBuilder();
                query.append("select date(if(arvdte!='' and arvdte!='0',arvdte,null)) as transaction_date");
                query.append(" from inlvoy");
                query.append(" where otrml=? and dtrml=? and inlvy=?");
                bsStatement = bsConn.prepareStatement(query.toString());
                bsStatement.setString(1, askfld.substring(0, 2));
                bsStatement.setString(2, askfld.substring(2, 4));
                bsStatement.setString(3, askfld.substring(4, 8).trim());
                bsResult = bsStatement.executeQuery();
                if (bsResult.next()) {
                    data.putString("manifestFlag", "Y");
                    data.putDate("transactionDate", bsResult);
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setInlandVoyageData() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        try {
            String askfld = data.getString("askfld");
            StringBuilder query = new StringBuilder();
            query.append("select date(if(datedp!='' and datedp!='0',datedp,null)) as transaction_date");
            query.append(" from inlvoy");
            query.append(" where otrml=? and dtrml=? and inlvy=?");
            bsStatement = bsConn.prepareStatement(query.toString());
            bsStatement.setString(1, askfld.substring(0, 2));
            bsStatement.setString(2, askfld.substring(2, 4));
            bsStatement.setString(3, askfld.substring(4, 8).trim());
            bsResult = bsStatement.executeQuery();
            if (bsResult.next()) {
                data.putString("manifestFlag", "Y");
                data.putDate("transactionDate", bsResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
        }
    }

    private void setInlandVoyageOrImportContainerData() throws Exception {
        String askfld = data.getString("askfld");
        Integer terminalNumber = Integer.parseInt(askfld.substring(0, 2));
        String shipmentType = MigrationUtils.between(terminalNumber, 60, 69) ? "LCLI" : terminalNumber >= 80 ? "AIRI" : "INLE";
        data.putString("shipmentType", shipmentType);
        if (MigrationUtils.isEqual(shipmentType, "LCLI")) {
            setImportContainerData();
        } else {
            setInlandVoyageData();
        }
        data.putString("containerNumber", data.getString("unit"));
        data.putString("voyageNumber", data.getString("inlvoy"));
        data.putString("loadingTerminal", data.getString("inlvoy").substring(0, 2));
        data.putString("billingTerminal", data.getString("inlvoy").substring(0, 2));
        data.putString("drcptTerminal", data.getString("inlvoy").substring(0, 2));
    }

    /**
     * getCompanyCode - get the company code from system rule database -
     * logiware and table - system_rules
     *
     * @return String - companyCode
     * @throws Exception
     */
    private void setGLAccountPrefix() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select rule_name as prefix");
            queryBuilder.append(" from system_rules");
            queryBuilder.append(" where rule_code='CompanyCode' and rule_name!=''");
            lwStatement = lwConn.prepareStatement(queryBuilder.toString());
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                data.putString("prefix", lwResult, "prefix");
            } else {
                errors.append("<li>Company Code required for GL account prefix is not found in Logiware</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void setGLAccountSuffix() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String derive = data.getString("derive");
            String suffix = data.getString("suffix");
            if (MigrationUtils.isAllNotEqual(derive, "F", "N") && MigrationUtils.in(suffix, "B", "L", "D", "F")) {
                String bsCostCode = data.getString("bsCostCode");
                String lwCostCode = data.getString("lwCostCode");
                String shipmentType = data.getString("shipmentType");
                String billingTerminal = data.getString("billingTerminal");
                String loadingTerminal = data.getString("loadingTerminal");
                String drcptTerminal = data.getString("drcptTerminal");
                String drcpt = data.getString("drcpt");
                String terminalNumber;
                String terminalType;
                if (MigrationUtils.isEqual(drcpt, "05")) {
                    terminalNumber = drcptTerminal;
                    terminalType = "lcl_export_dockreceipt";
                } else if (MigrationUtils.isEqualIgnoreCase(suffix, "B")) {
                    terminalNumber = billingTerminal;
                    terminalType = "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_billing"
                            : "FCLI".equalsIgnoreCase(shipmentType) ? "fcl_import_billing"
                            : "LCLE".equalsIgnoreCase(shipmentType) ? "lcl_export_billing"
                            : "LCLI".equalsIgnoreCase(shipmentType) ? "lcl_import_billing"
                            : "AIRE".equalsIgnoreCase(shipmentType) ? "air_export_billing"
                            : "AIRI".equalsIgnoreCase(shipmentType) ? "air_import_billing"
                            : "air_export_billing";

                    //  B-FCLE,FCLI,LCLE,LCLI,AIRE,AIRI
                    //   L- FCLE,FCLI,LCLE,LCLI,AIRE,AIRI,INLE
                    //   D- FCLE,LCLE,AIRE
                } else if (MigrationUtils.isEqualIgnoreCase(suffix, "L")) {
                    terminalNumber = loadingTerminal;
                    terminalType = "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_loading"
                            : "FCLI".equalsIgnoreCase(shipmentType) ? "fcl_import_loading"
                            : "LCLE".equalsIgnoreCase(shipmentType) ? "lcl_export_loading"
                            : "LCLI".equalsIgnoreCase(shipmentType) ? "lcl_import_loading"
                            : "AIRE".equalsIgnoreCase(shipmentType) ? "air_export_loading"
                            : "AIRI".equalsIgnoreCase(shipmentType) ? "air_import_loading"
                            : "inland_export_loading";
                } else {
                    terminalNumber = drcptTerminal;
                    terminalType = "FCLE".equalsIgnoreCase(shipmentType) ? "fcl_export_dockreceipt"
                            : "LCLE".equalsIgnoreCase(shipmentType) ? "13".equals(drcpt) ? "lcl_export_billing"
                            : "lcl_export_dockreceipt"
                            : "air_export_dockreceipt";
                }
                StringBuilder query = new StringBuilder();
                query.append("select right(concat('0',").append(terminalType).append("),2) as suffix");
                query.append(" from terminal_gl_mapping");
                query.append(" where terminal=? and ").append(terminalType).append("!=''");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, terminalNumber);
                lwResult = lwStatement.executeQuery();
                data.putString("terminal", terminalNumber);
                if (lwResult.next()) {
                    data.putString("suffix", lwResult);
                    data.putString("glAccountTerminal", terminalNumber);
                } else {
                    terminalType = "B".equals(suffix) ? "billing terminal" : "L".equals(suffix) ? "loading terminal" : "dock receipt";
                    errors.append("<li>Terminal not mapped with GL for");
                    errors.append(" bluescreen cost code - ").append(bsCostCode).append(" and logiware cost code - ").append(lwCostCode);
                    errors.append(" and shipment type - ").append(shipmentType).append(" and suffix value - ").append(suffix);
                    errors.append(" and ").append(terminalType).append(" - ").append(terminalNumber).append(" in logiware</li>");
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void setGLAccountNumber() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String suffix = data.getString("suffix");
            if (MigrationUtils.isAllNotEqual(suffix, "B", "L", "D", "F")) {
                String bsCostCode = data.getString("bsCostCode");
                String lwCostCode = data.getString("lwCostCode");
                String shipmentType = data.getString("shipmentType");
                String derive = data.getString("derive");
                String prefix = data.getString("prefix");
                String account = data.getString("account");
                StringBuilder glAccount = new StringBuilder(prefix).append("-").append(account).append("-").append(suffix);
                StringBuilder query = new StringBuilder();
                query.append("select account as gl_account_number");
                query.append(" from account_details");
                query.append(" where account=?");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, glAccount.toString());
                lwResult = lwStatement.executeQuery();
                if (lwResult.next()) {
                    data.putString("glAccountNumber", lwResult);
                } else {
                    errors.append("<li>GL account - ").append(glAccount).append(" derived from bluescreen cost code - ").append(bsCostCode);
                    errors.append(" and logiware cost code - ").append(lwCostCode).append(" and suffix value - ").append(suffix);
                    if (MigrationUtils.isAllNotEqual(derive, "F", "N") && MigrationUtils.in(suffix, "B", "L", "D", "F")) {
                        errors.append(" and ").append("B".equals(suffix) ? "billing terminal" : "L".equals(suffix) ? "loading terminal" : "dock receipt");
                        errors.append(" - ").append(data.getString("glAccountTerminal"));
                    }
                    errors.append(" and shipment type - ").append(shipmentType).append(" not found in logiware</li>");
                }
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * getCostCode - get logiware cost code from bluescreen cost code database -
     * logiware and table - gl_mapping
     *
     * @throws Exception
     */
    private void setCostCodeAndGLAccount() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String bsCostCode = data.getString("bsCostCode");
            String shipmentType = data.getString("shipmentType");
            StringBuilder query = new StringBuilder();
            query.append("select charge_code as lw_cost_code,gl_acct as account,derive_yn as derive,suffix_value as suffix");
            query.append(" from gl_mapping");
            query.append(" where transaction_type='AC' and rev_exp='E' and bluescreen_chargecode=? and shipment_type=? limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, bsCostCode);
            lwStatement.setString(2, shipmentType);
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                setGLAccountPrefix();
                data.putString("lwCostCode", lwResult);
                data.putString("account", lwResult);
                data.putString("derive", lwResult);
                data.putString("suffix", lwResult);
                setGLAccountSuffix();
                setGLAccountNumber();
            } else {
                errors.append("<li>No logiware cost code mapped for this bluescreen cost code - ").append(bsCostCode);
                errors.append(" and shipment type - ").append(shipmentType).append("</li>");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private boolean isNewCostCode() throws SQLException {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select charge_code as old_cost_code");
            query.append(" from transaction_ledger");
            query.append(" where transaction_type='AC'");
            query.append(" and apcostkey=?");
            query.append(" order by transaction_id limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, data.getString("apcostkey"));
            lwResult = lwStatement.executeQuery();
            return !(lwResult.next() && MigrationUtils.isNotEmpty(lwResult.getString("old_cost_code")));
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void createData() throws Exception {
        setBluescreenVendor();
        setLogiwareVendor();
        String type = data.getString("type");
        if (MigrationUtils.isEqual(type, "4")) {
            setFclOrLclOrAirData();
        } else if (MigrationUtils.isEqual(type, "5")) {
            setExportVoyageOrContainerData();
        } else if (MigrationUtils.isEqual(type, "6")) {
            setLclOrAirData();
            setInlandVoyageOrImportContainerData();
        }
        if (MigrationUtils.isEqual(data.getString("voyageNumber"), "00000")) {
            data.putString("voyageNumber", "");
        }
        if (MigrationUtils.isNotEmpty(data.getString("shipmentType")) && isNewCostCode()) {
            setCostCodeAndGLAccount();
        }
        if (null == data.getDate("transactionDate")) {
            warnings.append("<li>Reporting date is missing</li>");
        }
    }

    private void insertAuditNotes() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("insert into notes");
            query.append(" (module_id,item_name,module_ref_id,note_desc,note_type,updated_by,updatedate)");
            query.append(" values(?");
            for (int i = 1; i < 7; i++) {
                query.append(",?");
            }
            query.append(")");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, "ACCRUALS");
            lwStatement.setString(2, "ACCRUALS");
            lwStatement.setString(3, data.getString("moduleRefId"));
            lwStatement.setString(4, data.getString("notesDesc"));
            lwStatement.setString(5, "auto");
            lwStatement.setString(6, "System");
            lwStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            lwStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void createUpdateAccrualNote() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String oldLogType = data.getString("oldLogType");
            String enteredBy = data.getString("entrby");
            if (!"warning".equalsIgnoreCase(oldLogType)) {
                StringBuilder query = new StringBuilder();
                query.append("select transaction_id as module_ref_id,bill_ladding_no as old_bl_number,charge_code as old_cost_code,");
                query.append("sailing_date as old_reporting_date,drcpt as old_dock_receipt,voyage_no as old_voyage_number,transaction_amt as old_amount");
                query.append(" from transaction_ledger");
                query.append(" where transaction_type='AC' and apcostkey=?");
                query.append(" order by transaction_id limit 1");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, data.getString("apcostkey"));
                lwResult = lwStatement.executeQuery();
                if (lwResult.next()) {
                    data.putString("moduleRefId", lwResult);
                    data.putString("oldBlNumber", lwResult);
                    data.putString("oldCostCode", lwResult);
                    data.putString("oldReportingDate", lwResult);
                    data.putString("oldDockReceipt", lwResult);
                    data.putString("oldVoyageNumber", lwResult);
                    data.putDouble("oldAmount", lwResult);
                    StringBuilder notesDesc = new StringBuilder();
                    boolean addAnd = false;
                    String status = data.getString("status");
                    String newCostCode = data.getString("lwCostCode");
                    String oldCostCode = data.getString("oldCostCode");

                    if (MigrationUtils.isNotEmpty(newCostCode)) {
                        if (MigrationUtils.isNotEmpty(oldCostCode) && MigrationUtils.isNotEqualIgnoreCase(oldCostCode, newCostCode)) {
                            if (MigrationUtils.isNotEqualIgnoreCase(status, "AS")
                                    && MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))
                                    && MigrationUtils.isNotEqualIgnoreCase(newCostCode, oldCostCode)) {
                                notesDesc.append("Cost Code changed from ").append(oldCostCode).append(" to ").append(newCostCode);
                                addAnd = true;
                            }
                        } else if (MigrationUtils.isEmpty(oldCostCode)) {
                            notesDesc.append("Cost Code - ").append(newCostCode).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newBlNumber = data.getString("blNumber");
                    String oldBlNumber = data.getString("oldBlNumber");
                    if (MigrationUtils.isNotEmpty(newBlNumber)) {
                        if (MigrationUtils.isNotEmpty(oldBlNumber) && MigrationUtils.isNotEqualIgnoreCase(oldBlNumber, newBlNumber)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("B/L changed from ").append(oldBlNumber).append(" to ").append(newBlNumber);
                            addAnd = true;
                        } else if (MigrationUtils.isEmpty(oldBlNumber)) {
                            notesDesc.append(addAnd ? " and " : "").append("B/L - ").append(newBlNumber).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newDockReceipt = data.getString("dockReceipt");
                    String oldDockReceipt = data.getString("oldDockReceipt");
                    if (MigrationUtils.isNotEmpty(newDockReceipt)) {
                        if (MigrationUtils.isNotEmpty(oldDockReceipt) && MigrationUtils.isNotEqualIgnoreCase(oldDockReceipt, newDockReceipt)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("Dock Receipt changed from ").append(oldDockReceipt).append(" to ").append(newDockReceipt);
                            addAnd = true;
                        } else if (MigrationUtils.isEmpty(oldDockReceipt)) {
                            notesDesc.append(addAnd ? " and " : "").append("Dock Receipt - ").append(newDockReceipt).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newVoyageNumber = data.getString("voyageNumber");
                    String oldVoyageNumber = data.getString("oldVoyageNumber");
                    if (MigrationUtils.isNotEmpty(newVoyageNumber)) {
                        if (MigrationUtils.isNotEmpty(oldVoyageNumber) && MigrationUtils.isNotEqualIgnoreCase(oldVoyageNumber, newVoyageNumber)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("Voyage changed from ").append(oldVoyageNumber).append(" to ").append(newVoyageNumber);
                            addAnd = true;
                        } else if (MigrationUtils.isEmpty(oldVoyageNumber)) {
                            notesDesc.append(addAnd ? " and " : "").append("Voyage - ").append(newVoyageNumber).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newAmount = MigrationUtils.formatAmount(data.getDouble("accrualAmount"));
                    String oldAmount = MigrationUtils.formatAmount(data.getDouble("oldAmount"));
                    if (MigrationUtils.isNotEmpty(oldAmount)
                            && MigrationUtils.isNotEmpty(newAmount) && MigrationUtils.isNotEqualIgnoreCase(oldAmount, newAmount)
                            && MigrationUtils.in(status, "Open", "EDI Dispute", "Dispute")) {
                        notesDesc.append(addAnd ? " and " : "").append("Amount changed from ").append(oldAmount).append(" to ").append(newAmount);
                    }
                    if (MigrationUtils.isNotEmpty(notesDesc.toString())) {
                        notesDesc.append(" on ").append(MigrationUtils.formatDate(new Date(System.currentTimeMillis()), "MM/dd/yyyy"));
                        notesDesc.append(" by ").append(enteredBy);
                        data.putString("notesDesc", notesDesc.toString());
                        insertAuditNotes();
                    }
                }
            } else {
                StringBuilder query = new StringBuilder();
                query.append("select transaction_id as module_ref_id,bill_ladding_no as old_bl_number,");
                query.append("sailing_date as old_reporting_date,drcpt as old_dock_receipt,voyage_no as old_voyage_number");
                query.append(" from transaction_ledger");
                query.append(" where transaction_type='AC' and apcostkey=?");
                query.append(" order by transaction_id limit 1");
                lwStatement = lwConn.prepareStatement(query.toString());
                lwStatement.setString(1, data.getString("apcostkey"));
                lwResult = lwStatement.executeQuery();
                if (lwResult.next()) {
                    data.putString("moduleRefId", lwResult);
                    data.putString("oldBlNumber", lwResult);
                    data.putString("oldReportingDate", lwResult);
                    data.putString("oldDockReceipt", lwResult);
                    data.putString("oldVoyageNumber", lwResult);
                    StringBuilder notesDesc = new StringBuilder();
                    boolean addAnd = false;
                    String newBlNumber = data.getString("blNumber");
                    String oldBlNumber = data.getString("oldBlNumber");
                    if (MigrationUtils.isNotEmpty(newBlNumber)) {
                        if (MigrationUtils.isNotEmpty(oldBlNumber) && MigrationUtils.isNotEqualIgnoreCase(oldBlNumber, newBlNumber)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("B/L changed from ").append(oldBlNumber).append(" to ").append(newBlNumber);
                            addAnd = true;
                        } else if (MigrationUtils.isEmpty(oldBlNumber)) {
                            notesDesc.append(addAnd ? " and " : "").append("B/L - ").append(newBlNumber).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newDockReceipt = data.getString("dockReceipt");
                    String oldDockReceipt = data.getString("oldDockReceipt");
                    if (MigrationUtils.isNotEmpty(newDockReceipt)) {
                        if (MigrationUtils.isNotEmpty(oldDockReceipt) && MigrationUtils.isNotEqualIgnoreCase(oldDockReceipt, newDockReceipt)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("Dock Receipt changed from ").append(oldDockReceipt).append(" to ").append(newDockReceipt);
                            addAnd = true;
                        } else if (MigrationUtils.isEmpty(oldDockReceipt)) {
                            notesDesc.append(addAnd ? " and " : "").append("Dock Receipt - ").append(newDockReceipt).append(" is added");
                            addAnd = true;
                        }
                    }
                    String newVoyageNumber = data.getString("voyageNumber");
                    String oldVoyageNumber = data.getString("oldVoyageNumber");
                    if (MigrationUtils.isNotEmpty(newVoyageNumber)) {
                        if (MigrationUtils.isNotEmpty(oldVoyageNumber) && MigrationUtils.isNotEqualIgnoreCase(oldVoyageNumber, newVoyageNumber)) {
                            notesDesc.append(addAnd ? " and " : "");
                            notesDesc.append("Voyage changed from ").append(oldVoyageNumber).append(" to ").append(newVoyageNumber);
                        } else if (MigrationUtils.isEmpty(oldVoyageNumber)) {
                            notesDesc.append(addAnd ? " and " : "").append("Voyage - ").append(newVoyageNumber).append(" is added");
                        }
                    }
                    if (MigrationUtils.isNotEmpty(notesDesc.toString())) {
                        notesDesc.append(" on ").append(MigrationUtils.formatDate(new Date(System.currentTimeMillis()), "MM/dd/yyyy"));
                        notesDesc.append(" by ").append(enteredBy);
                        data.putString("notesDesc", notesDesc.toString());
                        insertAuditNotes();
                    }
                }

            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void updateAccrual() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            String oldLogType = data.getString("oldLogType");
            if (MigrationUtils.isNotEqualIgnoreCase(oldLogType, "warning")) {
                String status = data.getString("status");
                String apcostkey = data.getString("apcostkey");
                createUpdateAccrualNote();
                StringBuilder query = new StringBuilder();
                query.append("update transaction_ledger");
                query.append(" set bill_ladding_no=?,cheque_number=?,manifest_flag=?,drcpt=?,");
                query.append("voyage_no=?,container_no=?,booking_no=?,master_bl=?,bill_to=?");
                if (MigrationUtils.isNotEqual(status, "AS")) {
                    query.append(",updated_on=?");
                    query.append(",updated_by=?");
                }
                if (MigrationUtils.isEqualIgnoreCase(status, "Open")) {
                    query.append(",cust_name=?,cust_no=?,invoice_number=?");
                }
                if (MigrationUtils.in(status, "Open", "EDI Dispute", "Dispute")) {
                    query.append(",transaction_amt=?,balance=?,balance_in_process=?");
                }
                if (MigrationUtils.isNotEmpty(data.getString("lwCostCode"))
                        && MigrationUtils.isNotEqualIgnoreCase(status, "AS")
                        && MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))
                        && MigrationUtils.isNotEqualIgnoreCase(data.getString("lwCostCode"), data.getString("oldCostCode"))) {
                    query.append(",bluescreen_chargecode=?,shipment_type=?,charge_code=?,gl_account_number=?");
                    if (MigrationUtils.isNotEmpty(data.getString("terminal")) || MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))) {
                        query.append(",terminal=?");
                    }
                }
                if (MigrationUtils.isEmpty(data.getString("oldReportingDate"))) {
                    query.append(",sailing_date=?");
                }
                query.append(" where transaction_type='AC' and apcostkey=?");
                lwStatement = lwConn.prepareStatement(query.toString());
                int index = 1;
                lwStatement.setString(index, data.getString("blNumber"));
                index++;
                lwStatement.setString(index, data.getString("checkNumber"));
                index++;
                lwStatement.setString(index, data.getString("manifestFlag"));
                index++;
                lwStatement.setString(index, data.getString("dockReceipt"));
                index++;
                lwStatement.setString(index, data.getString("voyageNumber"));
                index++;
                lwStatement.setString(index, data.getString("containerNumber"));
                index++;
                lwStatement.setString(index, data.getString("bookingNumber"));
                index++;
                lwStatement.setString(index, data.getString("masterBlNumber"));
                index++;
                lwStatement.setString(index, data.getString("billTo"));
                if (MigrationUtils.isNotEqual(status, "AS")) {
                    index++;
                    lwStatement.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
                    index++;
                    lwStatement.setNull(index, Types.INTEGER);
                }
                if (MigrationUtils.isEqualIgnoreCase(status, "Open")) {
                    index++;
                    lwStatement.setString(index, data.getString("vendorName"));
                    index++;
                    lwStatement.setString(index, data.getString("vendorNumber"));
                    index++;
                    lwStatement.setString(index, data.getString("invoiceNumber"));
                }
                if (MigrationUtils.in(status, "Open", "EDI Dispute", "Dispute")) {
                    index++;
                    lwStatement.setDouble(index, data.getDouble("accrualAmount"));
                    index++;
                    lwStatement.setDouble(index, data.getDouble("accrualAmount"));//for balance
                    index++;
                    lwStatement.setDouble(index, data.getDouble("accrualAmount"));//for balance in process
                }
                if (MigrationUtils.isNotEmpty(data.getString("lwCostCode"))
                        && MigrationUtils.isNotEqualIgnoreCase(status, "AS")
                        && MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))
                        && MigrationUtils.isNotEqualIgnoreCase(data.getString("lwCostCode"), data.getString("oldCostCode"))) {
                    index++;
                    lwStatement.setString(index, data.getString("bsCostCode"));
                    index++;
                    lwStatement.setString(index, data.getString("shipmentType"));
                    index++;
                    lwStatement.setString(index, data.getString("lwCostCode"));
                    index++;
                    lwStatement.setString(index, data.getString("glAccountNumber"));
                    if (MigrationUtils.isNotEmpty(data.getString("terminal"))) {
                        index++;
                        lwStatement.setString(index, data.getString("terminal"));
                    } else if (MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))) {
                        index++;
                        lwStatement.setString(index, StringUtils.substringAfterLast(data.getString("glAccountNumber"), "-"));
                    }
                }
                if (MigrationUtils.isEmpty(data.getString("oldReportingDate"))) {
                    index++;
                    lwStatement.setDate(index, data.getDate("transactionDate"));// for reporting date
                }
                index++;
                lwStatement.setString(index, apcostkey);
                lwStatement.executeUpdate();
            } else {
                String status = data.getString("status");
                String apcostkey = data.getString("apcostkey");
                createUpdateAccrualNote();
                StringBuilder query = new StringBuilder();
                query.append("update transaction_ledger");
                query.append(" set bill_ladding_no=?,cheque_number=?,manifest_flag=?,drcpt=?,");
                query.append("voyage_no=?,container_no=?,booking_no=?,master_bl=?,bill_to=?");
                if (MigrationUtils.isNotEqual(status, "AS")) {
                    query.append(",updated_on=?");
                    query.append(",updated_by=?");
                }
                if (MigrationUtils.isEqualIgnoreCase(status, "Open")) {
                    query.append(",invoice_number=?");
                }
                if (MigrationUtils.isEmpty(data.getString("oldReportingDate"))) {
                    query.append(",sailing_date=?");
                }
                query.append(" where transaction_type='AC' and apcostkey=?");
                lwStatement = lwConn.prepareStatement(query.toString());
                int index = 1;
                lwStatement.setString(index, data.getString("blNumber"));
                index++;
                lwStatement.setString(index, data.getString("checkNumber"));
                index++;
                lwStatement.setString(index, data.getString("manifestFlag"));
                index++;
                lwStatement.setString(index, data.getString("dockReceipt"));
                index++;
                lwStatement.setString(index, data.getString("voyageNumber"));
                index++;
                lwStatement.setString(index, data.getString("containerNumber"));
                index++;
                lwStatement.setString(index, data.getString("bookingNumber"));
                index++;
                lwStatement.setString(index, data.getString("masterBlNumber"));
                index++;
                lwStatement.setString(index, data.getString("billTo"));
                if (MigrationUtils.isNotEqual(status, "AS")) {
                    index++;
                    lwStatement.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
                    index++;
                    lwStatement.setNull(index, Types.INTEGER);
                }
                if (MigrationUtils.isEqualIgnoreCase(status, "Open")) {
                    index++;
                    lwStatement.setString(index, data.getString("invoiceNumber"));
                }
                if (MigrationUtils.isEmpty(data.getString("oldReportingDate"))) {
                    index++;
                    lwStatement.setDate(index, data.getDate("transactionDate"));// for reporting date
                }
                index++;
                lwStatement.setString(index, apcostkey);
                lwStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void createNewAccrualNote() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select transaction_id as module_ref_id");
            query.append(" from transaction_ledger");
            query.append(" where transaction_type='AC' and apcostkey=?");
            query.append(" order by transaction_id limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, data.getString("apcostkey"));
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                data.putString("moduleRefId", lwResult);
                String enteredBy = data.getString("entrby");
                if (MigrationUtils.isNotEmpty(enteredBy)) {
                    StringBuilder bsNotesDesc = new StringBuilder();
                    bsNotesDesc.append("Cost is entered by ");
                    bsNotesDesc.append(enteredBy);
                    bsNotesDesc.append(" in bluescreen");
                    String enteredDate = MigrationUtils.formatDate(MigrationUtils.parseDate(data.getString("entdat"), "yyyyMMdd"), "MM/dd/yyyy");
                    bsNotesDesc.append(" on ").append(enteredDate);
                    data.putString("notesDesc", bsNotesDesc.toString());
                    insertAuditNotes();
                }
                StringBuilder notesDesc = new StringBuilder();
                notesDesc.append("Accrual Created for");
                boolean addAnd = false;
                if (MigrationUtils.isNotEmpty(data.getString("lwCostCode"))) {
                    notesDesc.append(" Cost Code - ").append(data.getString("lwCostCode"));
                    addAnd = true;
                }
                if (MigrationUtils.isNotEmpty(data.getString("blNumber"))) {
                    notesDesc.append(addAnd ? " and" : "").append(" B/L - ").append(data.getString("blNumber"));
                    addAnd = true;
                }
                if (MigrationUtils.isNotEmpty(data.getString("dockReceipt"))) {
                    notesDesc.append(addAnd ? " and" : "").append(" Dock Receipt - ").append(data.getString("dockReceipt"));
                    addAnd = true;
                }
                if (MigrationUtils.isNotEmpty(data.getString("voyageNumber"))) {
                    notesDesc.append(addAnd ? " and" : "").append(" Voyage - ").append(data.getString("voyageNumber"));
                    addAnd = true;
                }
                notesDesc.append(addAnd ? " and" : "").append(" amount ").append(MigrationUtils.formatAmount(data.getDouble("accrualAmount")));
                notesDesc.append(" on ").append(MigrationUtils.formatDate(new Date(System.currentTimeMillis()), "MM/dd/yyyy"));
                notesDesc.append(" by ").append(enteredBy);
                data.putString("notesDesc", notesDesc.toString());
                insertAuditNotes();
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void insertAccrual() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("insert into transaction_ledger");
            query.append(" (cust_name,cust_no,invoice_number,subledger_source_code,transaction_type,status,");
            query.append("bill_ladding_no,bluescreen_chargecode,shipment_type,charge_code,gl_account_number,");
            query.append("transaction_amt,balance,balance_in_process,transaction_date,sailing_date,cheque_number,");
            query.append("manifest_flag,drcpt,voyage_no,container_no,booking_no,master_bl,bill_to,inlvoy,apcostkey,");
            query.append("currency_code,created_on");
            if (MigrationUtils.isNotEmpty(data.getString("terminal")) || MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))) {
                query.append(",terminal");
            }
            query.append(")");
            query.append(" values(?");
            for (int i = 1; i < 28; i++) {
                query.append(",?");
            }
            if (MigrationUtils.isNotEmpty(data.getString("terminal")) || MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))) {
                query.append(",?");
            }
            query.append(")");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, data.getString("vendorName"));
            lwStatement.setString(2, data.getString("vendorNumber"));
            lwStatement.setString(3, data.getString("invoiceNumber"));
            lwStatement.setString(4, "ACC");
            lwStatement.setString(5, "AC");
            if (data.getString("bsCostCode").equals("176")) {
                lwStatement.setString(6, "inactive");
            } else {
                lwStatement.setString(6, "Open");
            }
            lwStatement.setString(7, data.getString("blNumber"));
            lwStatement.setString(8, data.getString("bsCostCode"));
            lwStatement.setString(9, data.getString("shipmentType"));
            lwStatement.setString(10, data.getString("lwCostCode"));
            lwStatement.setString(11, data.getString("glAccountNumber"));
            lwStatement.setDouble(12, data.getDouble("accrualAmount"));
            lwStatement.setDouble(13, data.getDouble("accrualAmount"));//for balance
            lwStatement.setDouble(14, data.getDouble("accrualAmount"));//for balance in process
            lwStatement.setDate(15, null != data.getDate("transactionDate") ? data.getDate("transactionDate") : new Date(System.currentTimeMillis()));
            lwStatement.setDate(16, data.getDate("transactionDate"));//for reporting date
            lwStatement.setString(17, data.getString("checkNumber"));
            lwStatement.setString(18, data.getString("manifestFlag"));
            lwStatement.setString(19, data.getString("dockReceipt"));
            lwStatement.setString(20, data.getString("voyageNumber"));
            lwStatement.setString(21, data.getString("containerNumber"));
            lwStatement.setString(22, data.getString("bookingNumber"));
            lwStatement.setString(23, data.getString("masterBlNumber"));
            lwStatement.setString(24, data.getString("billTo"));
            lwStatement.setString(25, data.getString("inlandVoyageNumber"));
            lwStatement.setString(26, data.getString("apcostkey"));
            lwStatement.setString(27, "USD");
            lwStatement.setTimestamp(28, new Timestamp(System.currentTimeMillis()));
            if (MigrationUtils.isNotEmpty(data.getString("terminal"))) {
                lwStatement.setString(29, data.getString("terminal"));
            } else if (MigrationUtils.isNotEmpty(data.getString("glAccountNumber"))) {
                lwStatement.setString(29, StringUtils.substringAfterLast(data.getString("glAccountNumber"), "-"));
            }
            lwStatement.executeUpdate();
            createNewAccrualNote();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void insertLog() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("insert into accrual_migration_log");
            query.append(" (bluescreen_vendor,vendor_number,invoice_number,bl_number,amount,");
            query.append("dock_receipt,voyage_number,container_number,bluescreen_key,");
            query.append("log_type,error,reported_date,file_name)");
            query.append(" values(?");
            for (int i = 1; i < 13; i++) {
                query.append(",?");
            }
            query.append(")");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, data.getString("bsVendorNumber"));
            lwStatement.setString(2, data.getString("vendorNumber"));
            lwStatement.setString(3, data.getString("invoiceNumber"));
            lwStatement.setString(4, data.getString("blNumber"));
            lwStatement.setDouble(5, data.getDouble("accrualAmount"));
            lwStatement.setString(6, data.getString("dockReceipt"));
            lwStatement.setString(7, data.getString("voyageNumber"));
            lwStatement.setString(8, data.getString("containerNumber"));
            lwStatement.setString(9, data.getString("apcostkey"));
            lwStatement.setString(10, data.getString("logType"));
            lwStatement.setString(11, errors.append(warnings).toString());
            lwStatement.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
            lwStatement.setString(13, data.getString("fileName"));
            lwStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void findLogId() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("select id as log_id");
            query.append(" from accrual_migration_log");
            query.append(" order by id desc limit 1");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                data.putInteger("logId", lwResult);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void insertErrorFile() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            findLogId();
            StringBuilder query = new StringBuilder();
            query.append("insert into accrual_migration_error_file");
            query.append(" (accrual_migration_log_id,type,askfld,cntrl,cstcde,amount,paiddt,eacode,");
            query.append("gltrml,vendnm,vend,invnum,invdat,entdat,entrby,enttim,postby,posted,posttm,");
            query.append("chknum,venref,expvoy,inlvoy,unit,cstdr,updtby,upddat,updtim,postdt,mstvn,");
            query.append("uadrs1,uadrs2,uacity,uastat,uazip,uaphn,uafax,editky,appddt,appdby,appdtm,autocd,");
            query.append("cmmnts,mstck,agtvn,key022,faecde,actcod,cutamt,cutdte,cuttim,voidby,voiddt,voidtm)");
            query.append(" values(?");
            for (int i = 1; i <= headers.size(); i++) {
                query.append(",?");
            }
            query.append(")");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setInt(1, data.getInteger("logId"));
            for (int index = 2; index <= headers.size() + 1; index++) {
                lwStatement.setString(index, data.getString(headers.get(index - 2)));
            }
            lwStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void insertReprocessLog() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            StringBuilder query = new StringBuilder("insert into accrual_migration_reprocess_log");
            query.append(" (accrual_migration_log_id,log_type,error,reported_date)");
            query.append(" values(?,?,?,?)");
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setInt(1, data.getInteger("accrualMigrationLogId"));
            lwStatement.setString(2, data.getString("logType"));
            lwStatement.setString(3, errors.append(warnings).toString());
            lwStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            lwStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private void updateLog() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            StringBuilder query = new StringBuilder();
            query.append("update accrual_migration_log");
            query.append(" set vendor_number=?,bl_number=?,voyage_number=?,container_number=?,log_type=?,no_of_reprocess=no_of_reprocess+1");
            query.append(" where id=").append(data.getInteger("accrualMigrationLogId"));
            lwStatement = lwConn.prepareStatement(query.toString());
            lwStatement.setString(1, data.getString("vendorNumber"));
            lwStatement.setString(2, data.getString("blNumber"));
            lwStatement.setString(3, data.getString("voyageNumber"));
            lwStatement.setString(4, data.getString("containerNumber"));
            lwStatement.setString(5, data.getString("logType"));
            lwStatement.executeUpdate();
            insertReprocessLog();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * loadData - load data from csv files into logiware
     *
     * @throws Exception
     */
    private void loadData() throws Exception {
        File preprocessedDir = new File(prop.getProperty("accrualPreprocessed"));
        File processedDir = new File(prop.getProperty("accrualProcessed"));
        File[] files = preprocessedDir.listFiles(new FilenameFilter("csv"));
        Arrays.sort(files, new FileComparator());
        for (File preprocessedFile : files) {
            DataFile csvFile = DataFile.createReader("8859_1");
            csvFile.setDataFormat(new CSVFormat());
            csvFile.open(preprocessedFile);
            for (DataRow row = csvFile.next(); null != row; row = csvFile.next()) {
                try {
                    data.clear();
                    errors = new StringBuilder();
                    warnings = new StringBuilder();
                    if (MigrationUtils.isEqual(row.size(), headers.size())) {
                        data.putString("fileName", preprocessedFile.getName());
                        extractData(row);
                        String posted = data.getString("posted");
                        String postedDate = data.getString("postdt");
                        String flag = data.getString("flag");
                        if (MigrationUtils.isEqualIgnoreCase(flag, "DEL")) {
                            deleteAccrual();
                            if (MigrationUtils.isNotEmpty(errors.toString())) {
                                warnings = new StringBuilder();
                                data.putString("logType", "error");
                                insertLog();
                                insertErrorFile();
                            } else {
                                data.putString("logType", "processed");
                                insertLog();
                            }
                        } else if (MigrationUtils.isAllNotEqual(posted, "P", "R", "*") && MigrationUtils.in(postedDate, "00000000", "0")) {
                            findAccrualStatus();
                            String status = data.getString("status");
                            createData();
                            if (MigrationUtils.isEqualIgnoreCase(status, "New")) {
                                if (MigrationUtils.isNotEmpty(errors.toString())) {
                                    warnings = new StringBuilder();
                                    data.putString("logType", "error");
                                    insertLog();
                                    insertErrorFile();
                                } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                                    insertAccrual();
                                    data.putString("logType", "warning");
                                    insertLog();
                                    insertErrorFile();
                                } else {
                                    insertAccrual();
                                    data.putString("logType", "processed");
                                    insertLog();
                                }
                            } else if (MigrationUtils.isNotEmpty(errors.toString())) {
                                warnings = new StringBuilder();
                                data.putString("logType", "error");
                                insertLog();
                                insertErrorFile();
                            } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                                updateAccrual();
                                data.putString("logType", "warning");
                                insertLog();
                                insertErrorFile();
                            } else {
                                updateAccrual();
                                data.putString("logType", "processed");
                                insertLog();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.info(e);
                }
            }
            MigrationUtils.closeCsvFile(csvFile);
            MigrationUtils.moveFile(preprocessedFile, new File(processedDir, preprocessedFile.getName()));
        }
    }

    /**
     * loadData - load data from csv files into logiware
     *
     * @throws Exception
     */
    private void reloadData() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            String query = "select * from accrual_migration_error_file where accrual_migration_log_id=?";
            lwStatement = lwConn.prepareStatement(query);
            lwStatement.setInt(1, data.getInteger("accrualMigrationLogId"));
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                extractData(lwResult);
                String flag = data.getString("flag");
                if (MigrationUtils.isEqualIgnoreCase(flag, "DEL")) {
                    deleteAccrual();
                    if (MigrationUtils.isNotEmpty(errors.toString())) {
                        warnings = new StringBuilder();
                        data.putString("logType", "error");
                        reprocessLog = errors.toString();
                    } else {
                        data.putString("logType", "re-processed");
                        reprocessLog = "success";
                        updateLog();
                    }
                } else {
                    findAccrualStatus();
                    String status = data.getString("status");
                    createData();
                    if (MigrationUtils.isEqualIgnoreCase(status, "New")) {
                        if (MigrationUtils.isEqualIgnoreCase(data.getString("logType"), "warning")) {
                            data.putString("logType", "re-processed");
                            reprocessLog = "success";
                        } else if (MigrationUtils.isNotEmpty(errors.toString())) {
                            warnings = new StringBuilder();
                            data.putString("logType", "error");
                            reprocessLog = errors.toString();
                        } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                            insertAccrual();
                            data.putString("logType", "warning");
                            reprocessLog = warnings.toString();
                        } else {
                            insertAccrual();
                            data.putString("logType", "re-processed");
                            reprocessLog = "success";
                        }
                        updateLog();
                    } else {
                        if (MigrationUtils.isNotEmpty(errors.toString())) {
                            warnings = new StringBuilder();
                            data.putString("logType", "error");
                            reprocessLog = errors.toString();
                        } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                            updateAccrual();
                            data.putString("logType", "warning");
                            reprocessLog = warnings.toString();
                        } else {
                            updateAccrual();
                            data.putString("logType", "re-processed");
                            reprocessLog = "success";
                        }
                        updateLog();
                    }
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
     * loadData - load data from csv files into logiware
     *
     * @throws Exception
     */
    private void reloadData(Integer id) throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            data.putInteger("accrualMigrationLogId", id);
            String query = "select log_type from accrual_migration_log where id=?";
            lwStatement = lwConn.prepareStatement(query);
            lwStatement.setInt(1, id);
            lwResult = lwStatement.executeQuery();
            if (lwResult.next()) {
                data.putString("logType", lwResult);
                data.putString("oldLogType", data.getString("logType"));
                reloadData();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    /**
     * disconnectToLogiware - abort connection with logiware
     *
     * @throws Exception
     */
    private void disconnectToLogiware() throws Exception {
        try {
            log.info("Disconnecting to Logiware database..." + new Timestamp(System.currentTimeMillis()));
            lwConn.close();
            log.info("Logiware database disconnected successfully...");
        } catch (SQLException e) {
            log.info("Logiware database disconnection failed...");
            throw e;
        }
    }

    /**
     * disconnectToBluescreen - abort connection with bluescreen
     *
     * @throws Exception
     */
    private void disconnectToBluescreen() throws Exception {
        try {
            log.info("Disconnecting to Bluescreen database..." + new Timestamp(System.currentTimeMillis()));
            bsConn.close();
            log.info("Bluescreen database disconnected successfully...");
        } catch (SQLException e) {
            log.info("Bluescreen database disconnection failed...");
            throw e;
        }
    }

    /**
     * disconnect - abort connection to both bluescreen and logiware
     *
     * @throws Exception
     */
    private void disconnect() throws Exception {
        disconnectToLogiware();
        disconnectToBluescreen();
    }

    /**
     * reprocessSingleError - reprocess single loading error based on id of the
     * log
     *
     * @param id
     * @return reprocessLog
     * @throws Exception
     */
    public String reprocessSingleError(Integer id) throws Exception {
        try {
            //Load properties from properties file
            loadProperties();
            //Connect to bluescreen and logiware databases
            connect();
            addCsvHeader();
            reloadData(id);
            return reprocessLog;
        } catch (Exception e) {
            throw e;
        } finally {
            //disconnect from bluescreen and logiware databases
            disconnect();
        }
    }

    /**
     * reprocessAllErrors - reprocess all the errors based on log status
     *
     * @throws Exception
     */
    public void reprocessAllErrors() throws Exception {
        PreparedStatement errorStatement = null;
        ResultSet errorResult = null;
        try {
            //Load properties from properties file
            loadProperties();
            //Connect to bluescreen and logiware databases
            connect();
            addCsvHeader();
            //Get all the errors
            StringBuilder query = new StringBuilder("select id from accrual_migration_log");
            query.append(" where (log_type='error' or (log_type='warning' and no_of_reprocess <= 200))");
            errorStatement = lwConn.prepareStatement(query.toString());
            errorResult = errorStatement.executeQuery();
            while (errorResult.next()) {
                try {
                    data.clear();
                    errors = new StringBuilder();
                    warnings = new StringBuilder();
                    reloadData(errorResult.getInt("id"));
                } catch (Exception e) {
                    log.info(e);
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

    private void setGroupConcatMaxLength() throws Exception {
        PreparedStatement lwStatement = null;
        try {
            String query = "set session group_concat_max_len=128*128*128*1024";
            lwStatement = lwConn.prepareStatement(query);
            lwStatement.execute();
        } catch (SQLException e) {
            throw e;
        } finally {
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    private String getOldApcostKeys() throws Exception {
        PreparedStatement lwStatement = null;
        ResultSet lwResult = null;
        try {
            setGroupConcatMaxLength();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select group_concat(concat(\"'\",apcostkey,\"'\")) as apcostkey");
            queryBuilder.append(" from transaction_ledger");
            queryBuilder.append(" where transaction_type ='AC' and apcostkey!='COMMISSION'");
            lwStatement = lwConn.prepareStatement(queryBuilder.toString());
            lwResult = lwStatement.executeQuery();
            return lwResult.next() ? lwResult.getString("apcostkey") : null;
        } catch (Exception e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(lwResult);
            MigrationUtils.closeStatement(lwStatement);
        }
    }

    public Integer loadMissingAccruals() throws Exception {
        PreparedStatement bsStatement = null;
        ResultSet bsResult = null;
        int count = 0;
        try {
            loadProperties();
            connect();
            addCsvHeader();
            String oldApcostKeys = getOldApcostKeys();
            //setMaxAllowedPacket();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select type,askfld,cntrl,cstcde,amount,paiddt,eacode,gltrml,vendnm,vend,invnum,invdat,entdat,entrby,");
            queryBuilder.append("enttim,postby,posted,posttm,chknum,venref,expvoy,inlvoy,unit,cstdr,updtby,upddat,updtim,postdt,mstvn,");
            queryBuilder.append("uadrs1,uadrs2,uacity,uastat,uazip,uaphn,uafax,editky,appddt,appdby,appdtm,autocd,cmmnts,mstck,agtvn,");
            queryBuilder.append("key022,faecde,actcod,cutamt,cutdte,cuttim,voidby,voiddt,voidtm");
            queryBuilder.append(" from apcost");
            queryBuilder.append(" where posted!='P' and posted!='R' and posted!='*'");
            queryBuilder.append(" and (postdt='00000000' or postdt='0')");
            queryBuilder.append(" and (actcod!='DEL' or actcod='')");
            queryBuilder.append(" and concat(type,askfld,cntrl) not in (").append(oldApcostKeys).append(")");
            bsStatement = bsConn.prepareStatement(queryBuilder.toString());
            bsResult = bsStatement.executeQuery();
            while (bsResult.next()) {
                try {
                    data.clear();
                    errors = new StringBuilder();
                    warnings = new StringBuilder();
                    extractData(bsResult);
                    findAccrualStatus();
                    String status = data.getString("status");
                    if (MigrationUtils.isEqualIgnoreCase(status, "New")) {
                        count++;
                        createData();
                        if (MigrationUtils.isNotEmpty(errors.toString())) {
                            warnings = new StringBuilder();
                            data.putString("logType", "error");
                            insertLog();
                            insertErrorFile();
                        } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                            insertAccrual();
                            data.putString("logType", "warning");
                            insertLog();
                            insertErrorFile();
                        } else {
                            insertAccrual();
                            data.putString("logType", "processed");
                            insertLog();
                        }
                    } else {
                        count++;
                        createData();
                        if (MigrationUtils.isNotEmpty(errors.toString())) {
                            warnings = new StringBuilder();
                            data.putString("logType", "error");
                            insertLog();
                            insertErrorFile();
                        } else if (MigrationUtils.isNotEmpty(warnings.toString())) {
                            updateAccrual();
                            data.putString("logType", "warning");
                            insertLog();
                            insertErrorFile();
                        } else {
                            updateAccrual();
                            data.putString("logType", "processed");
                            insertLog();
                        }
                    }
                } catch (Exception e) {
                    log.info(e);
                }
            }
            return count;
        } catch (Exception e) {
            throw e;
        } finally {
            MigrationUtils.closeResult(bsResult);
            MigrationUtils.closeStatement(bsStatement);
            disconnect();
        }
    }

    public static void main(String[] args) throws Exception {
        new LoadAccrualsToLogiware().reprocessAllErrors();
    }
}

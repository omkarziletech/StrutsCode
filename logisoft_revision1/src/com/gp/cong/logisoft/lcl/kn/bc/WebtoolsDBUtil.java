package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cong.logisoft.kn.beans.Booking;
import com.gp.cong.logisoft.kn.beans.BookingHaz;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.mysql.jdbc.CommunicationsException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author Rajesh
 */
public class WebtoolsDBUtil {

    private static Logger log = Logger.getLogger(WebtoolsDBUtil.class);
    private static String driver = PropertyUtils.getProperty("driver");
    private static String url = PropertyUtils.getProperty("webtools_db_url");
    private static String userName = PropertyUtils.getProperty("webtools_db_username");
    private static String password = PropertyUtils.getProperty("webtools_db_password");
    private static Connection connection = getConnction();

    public static Connection getConnction() {
        try {
            Class.forName(driver);
            connection = null != connection ? connection : DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException exception) {
            log.error(exception);
        } catch (SQLException exception) {
            log.error(exception);
        }
        return connection;
    }

    public String[] saveBooking(Map<String, String> valueMap) throws CommunicationsException, IllegalArgumentException, Exception {
        synchronized (this) {
            CSVUtils csvUtil = new CSVUtils();
            if (null != connection) {
                String bkgNo = null;
                Booking bkg = null;
                String[] returnValues = new String[5];
                BookingEnvelopeDao bkgEnvelopeDao = new BookingEnvelopeDao();
                try {
                    bkg = bkgEnvelopeDao.getBooking(valueMap);
                    if (null != bkg) {
                        if (null == bkg.getOriginTerminal() || "".equals(bkg.getOriginTerminal().trim())) {
                            throw new IllegalArgumentException("Orgin terminal is unknown");
                        } else if (null == bkg.getSenderMappingId() || "".equals(bkg.getSenderMappingId().trim())) {
                            throw new IllegalArgumentException("Sender ID is not mapped");
                        }
                        String csvPath = LoadLogisoftProperties.getProperty("csv_dispatch_path");
                        returnValues[1] = bkg.getOriginTerminal();
                        CallableStatement callableStatement = connection.prepareCall(QueryUtil.createBookingNo());
                        callableStatement.registerOutParameter(1, Types.INTEGER);
                        callableStatement.executeUpdate();
                        bkgNo = String.format("%06d", callableStatement.getInt(1));
                        returnValues[0] = bkgNo;
                        valueMap.put("bkgNo", bkgNo);
                        bkg.setDockReceiptNo(bkgNo);
                        String query = QueryUtil.saveBooking(bkg).toString();
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        csvUtil.createBookingCSV(csvPath + bkgNo + ".csv", bkg);
                    }
                } catch (SQLException exception) {
                    log.error(exception);
                } catch (NullPointerException exception) {
                    log.error(exception);
                }
                return returnValues;
            } else {
                throw new CommunicationsException(null, 0, 0, null);
            }
        }
    }

    public void saveBookingHaz(Map<String, String> valueMap, String... values) {
        CSVUtils csvUtil = new CSVUtils();
        BookingEnvelopeDao bkgEnvelopeDao = new BookingEnvelopeDao();
        try {
            String csvPath = LoadLogisoftProperties.getProperty("csv_dispatch_path");
            List<BookingHaz> bkgHazes = bkgEnvelopeDao.getBookingHaz(valueMap);
            for (BookingHaz bkgHaz : bkgHazes) {
                String query = QueryUtil.saveBookingHaz(valueMap, bkgHaz).toString();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeUpdate();
            }
            if (!bkgHazes.isEmpty()) {
                csvUtil.createHazCSV(csvPath + values[0] + "_haz.csv", bkgHazes, values);
            }
        } catch (SQLException exception) {
            log.error(exception);
        } catch (NullPointerException exception) {
            log.error(exception);
        } catch (Exception exception) {
            log.error(exception);
        }
    }
}

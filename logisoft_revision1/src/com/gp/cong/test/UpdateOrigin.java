package com.gp.cong.test;

import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class UpdateOrigin {

    private static final Logger log = Logger.getLogger(UpdateOrigin.class);
    Connection conn;
    FileWriter fileWriter = null;
    List<String> SSlineList = new ArrayList<String>();

    public static void main(String[] args) {
	new UpdateOrigin();
    }

    public UpdateOrigin() {
	try {
	    File file = new File("sslineNumber.txt");
	    file.createNewFile();
	    fileWriter = new FileWriter(file);
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	    String url = "jdbc:mysql://cong:3306/econocaribe_qa_test";
	    conn = DriverManager.getConnection(url, "econo_logisoft", "cong2006");
	    //doTests();
	    //convertSSLineToAccountNo();
	    formatOrginAndDestination();
	    conn.close();
	} catch (ClassNotFoundException ex) {
	    System.err.println(ex.getMessage());
	} catch (IllegalAccessException ex) {
	    System.err.println(ex.getMessage());
	} catch (InstantiationException ex) {
	    System.err.println(ex.getMessage());
	} catch (SQLException ex) {
	    System.err.println(ex.getMessage());
	} catch (Exception e) {
	    log.info("UpdateOrigin() Failed in UpdateOrigin",e);
	}
    }

    private void formatOrginAndDestination() {
	// String query = "SELECT DISTINCT(ssline_no) FROM fcl_buy WHERE ssline_no IS NOT NULL AND ssline_no!=''";
	String query = "select quote_id,origin_terminal,plor,plod,destination_port,finaldestination,"
		+ "door_origin,door_destination FROM quotation";

	try {
	    Statement st = conn.createStatement();
	    ResultSet rs = st.executeQuery(query);
	    while (rs.next()) {
		String originTerminal = rs.getString("origin_terminal");
		String polr = rs.getString("plor");
		String pold = rs.getString("plod");
		String destination = rs.getString("destination_port");
		String finalDestination = rs.getString("finaldestination");
		String doorOFOrigin = rs.getString("door_origin");
		String doorOFDestination = rs.getString("door_destination");
		String quoteId = rs.getString("quote_id");
		if (originTerminal != null && originTerminal.indexOf("(") < 0) {
		    String originTerminalToInsert = getStringForOrigin(originTerminal, true);
		    if (originTerminalToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set origin_terminal=\"" + originTerminalToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
		if (polr != null && polr.indexOf("(") < 0) {
		    String polrToInsert = getStringForOrigin(polr, true);
		    if (polrToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set plor=\"" + polrToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
		if (doorOFOrigin != null && doorOFOrigin.indexOf("(") < 0) {
		    String doorOFOriginToInsert = getStringForOrigin(doorOFOrigin, true);
		    if (doorOFOriginToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set door_origin=\"" + doorOFOriginToInsert + "\" where quote_id=" + quoteId);
		    }
		}
		if (pold != null && pold.indexOf("(") < 0) {
		    String poldTerminalToInsert = getStringForOrigin(pold, false);
		    if (poldTerminalToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set plod=\"" + poldTerminalToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
		if (destination != null && destination.indexOf("(") < 0) {
		    String destinationToInsert = getStringForOrigin(destination, false);
		    if (destinationToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set destination_port=\"" + destinationToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
		if (finalDestination != null && finalDestination.indexOf("(") < 0) {
		    String finalDestinationToInsert = getStringForOrigin(finalDestination, false);
		    if (finalDestinationToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set finaldestination=\"" + finalDestinationToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
		if (doorOFDestination != null && doorOFDestination.indexOf("(") < 0) {
		    String doorOFDestinationToInsert = getStringForOrigin(doorOFDestination, false);
		    if (doorOFDestinationToInsert != null) {
			Statement updateStatement = conn.createStatement();
			updateStatement.executeUpdate("update quotation set door_destination=\"" + doorOFDestinationToInsert
				+ "\" where quote_id=" + quoteId);
		    }
		}
	    }
	} catch (SQLException ex) {
	    System.err.println(ex.getMessage());
	} catch (Exception e) {
	    log.info("Cannot CLosed connection....",e);
	}
    }

    String getStringForOrigin(String inputString, boolean flag) {
	String outputString = null;
	String unlocCode = null;
	String formatToTokenized = "/[A-Z]*/";
	String value = "";
	try {
	    unlocCode = inputString.substring(inputString.lastIndexOf("/") + 1);
	    if (unlocCode != null) {
		outputString = getUnloccode(unlocCode, flag);
	    }
	    if (outputString == null) {
		Pattern pattern = Pattern.compile(formatToTokenized);
		Matcher matcher = pattern.matcher(inputString);
		while (matcher.find()) {
		    value += matcher.group();
		}
		if (value.indexOf("/") > -1) {
		    unlocCode = value.substring(value.indexOf("/") + 1, value.lastIndexOf("/"));
		}
		outputString = getUnloccode(unlocCode, flag);
	    }
	} catch (Exception e) {
	    log.info("Cannot CLosed connection....",e);
	}
	return outputString;
    }

    public String getUnloccode(String unlocCode, boolean flag) {
	String outputString = null;
	try {
	    String query = "SELECT * FROM un_location WHERE un_loc_code='" + unlocCode + "'";
	    Statement st = conn.createStatement();
	    ResultSet rs = st.executeQuery(query);
	    while (rs.next()) {
		String unlocaName = rs.getString("un_loc_name");
		String unlocaCode = rs.getString("un_loc_code");
		String stateCode = rs.getString("statecode");
		String countryCode = rs.getString("countrycode");
		String stateGetFromGenericCodeDup = null;
		if (flag) {
		    stateGetFromGenericCodeDup = getGenericCodeDesc(stateCode, true);
		} else {
		    stateGetFromGenericCodeDup = getGenericCodeDesc(countryCode, false);
		}
		if (stateGetFromGenericCodeDup != null) {
		    outputString = unlocaName + "/" + stateGetFromGenericCodeDup
			    + "(" + unlocaCode + ")";
		} else {
		    outputString = unlocaName + "/(" + unlocaCode + ")";
		}

	    }
	} catch (Exception e) {
	    // TODO: handle exception
	}
	return outputString;
    }

    public String getGenericCodeDesc(String genericId, boolean flag) throws Exception {

	String stateGetFromGenericCodeDup = null;
	String query = "SELECT * FROM genericcode_dup WHERE id=" + genericId;
	Statement genericStatement = conn.createStatement();
	ResultSet genericResultSet = genericStatement.executeQuery(query);
	if (genericResultSet.next()) {
	    if (flag) {
		stateGetFromGenericCodeDup = genericResultSet.getString("code");
	    } else {
		stateGetFromGenericCodeDup = genericResultSet.getString("codedesc");
	    }
	}

	return stateGetFromGenericCodeDup;
    }
}
  //}
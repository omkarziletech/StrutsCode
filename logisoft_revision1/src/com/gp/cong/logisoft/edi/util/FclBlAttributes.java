package com.gp.cong.logisoft.edi.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class FclBlAttributes {
	private String move_type;
	private String ss_bldestinationchargesprecol;
	private String stream_ship_bl;
	private String aes;
	private String fclbl_clause;
	private String Port;
	private String PortofDischarge;
	private String Port_of_Loading;
	private String Terminal;
	private String shipper_name;
	private String shipper_address;
	private String consignee_name;
	private String consignee_address;
	private String notify_party_name;
	private String Streamship_notify_party;
	private String BookingNo;
	private String Export_reference;
	private String billing_terminal;
	private String Vessel;
	private String Voyages;
	private String ssline_no;
	private String bol;

	public FclBlAttributes(ResultSet rs)throws Exception {
			for(Field field:this.getClass().getDeclaredFields()){
				field.set(this, rs.getString(field.getName()));
			}
	}

	public String getMove_type() {
		return move_type;
	}

	public String getSs_bldestinationchargesprecol() {
		return ss_bldestinationchargesprecol;
	}

	public String getStream_ship_bl() {
		return stream_ship_bl;
	}

	public String getAes() {
		return aes;
	}

	public String getFclbl_clause() {
		return fclbl_clause;
	}

	public String getPort() {
		return Port;
	}

	public String getPortofDischarge() {
		return PortofDischarge;
	}

	public String getPort_of_Loading() {
		return Port_of_Loading;
	}

	public String getTerminal() {
		return Terminal;
	}

	public String getShipper_name() {
		return shipper_name;
	}

	public String getShipper_address() {
		return shipper_address;
	}

	public String getConsignee_name() {
		return consignee_name;
	}

	public String getConsignee_address() {
		return consignee_address;
	}

	public String getNotify_party_name() {
		return notify_party_name;
	}

	public String getStreamship_notify_party() {
		return Streamship_notify_party;
	}

	public String getBookingNo() {
		return BookingNo;
	}

	public String getBol() {
		return bol;
	}
	public String getVessel() {
		return Vessel;
	}

	public String getVoyages() {
		return Voyages;
	}

	public String getSsline_no() {
		return ssline_no;
	}

	public String getBilling_terminal() {
		return billing_terminal;
	}

	public String getExport_reference() {
		return Export_reference;
	}
}

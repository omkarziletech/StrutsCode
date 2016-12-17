package com.gp.cong.logisoft.bc.accounting;

public interface APPaymentConstants {
	// Constants for File Header Record
	public final static String FILE_HEADER_RECORD_TYPE = "1";
	public final static String FILE_HEADER_PRIORITY_CODE = "01";
	public final static String IMMEDIATE_DESTINATION = "bTTTTAAAAC"; 
	public final static String FILE_ID_MODIFIER = "A";
	public final static String RECORD_SIZE = "094";
	public final static String BLOCKING_FACTOR = "10";
	public final static String FORMAT_CODE = "1";
	
	//Constants for Batch Header Record
	public final static String BATCH_HEADER_RECORD_TYPE = "5";
	public final static String BATCH_HEADER_SERVICE_CLASS_CODE ="225";
	public final static String STANDARD_ENTRY_CLASS_CODE = "CTX"; 
	public final static String ORIGINATOR_STATUS_CODE = "1";
	
	//Constants for CCD Entry Detail Records
	public final static String CCD_ENTRY_DETAIL_RECORD_TYPE = "6";
	public final static String CCD_TRANSACTION_CODE = "27";
	public final static String CCD_ADDENDA_RECORD_INDICATOR = "0";
	
	//Constants for PPD Entry Detail Records
	public final static String PPD_ENTRY_DETAIL_RECORD_TYPE = "6";
	public final static String PPD_TRANSACTION_CODE = "27";
	public final static String PPD_ADDENDA_RECORD_INDICATOR = "0";
	
	//Constants for Batch Control Record
	public final static String BATCH_CONTROL_RECORD_TYPE = "8";
	public final static String BATCH_ENTRY_ADDENDA_CODE ="000002";
	
	//Constants for File Control Record
	public final static String FILE_CONTROL_RECORD_TYPE = "9"; 
}

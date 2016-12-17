package com.gp.cong.logisoft.lcl.kn.bc;

/**
 *
 * @author Rajesh
 */
public enum SortByEnum {

    bookingNumber("bkg_number"), senderId("sender_id"), customerControlCode("customer_control_code"),
    senderMappingId("codedesc"), origin("cfs_origin"), destination("cfs_Destination"),
    amsflag("ams_Flag"), aesflag("aes_Flag"), bookingDate("bkg_date"), pieces("pieces"), weight("weight"),
    volume("volume"), vesselVoyage("vessel_voyage_id"), vessel("vessel_name"), imoNumber("imo_number"),
    voyage("voyage"), etd("sail.etd"), eta("sail.eta"), createdOn("created_on");
    private String field;

    public String getField() {
        return field;
    }

    private SortByEnum(String field) {
        this.field = field;
    }
}

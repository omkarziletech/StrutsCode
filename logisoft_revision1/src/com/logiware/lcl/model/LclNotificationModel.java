package com.logiware.lcl.model;

/**
 *
 * @author Lucky
 */
public class LclNotificationModel {

    private String fileNumberIds;
    private String fileNumbers;
    private Long ssDetailId;
    private Long unitId;
    private String unitNo;
    private String fromName;
    private String fromAddress;
    private Long id;
    private String toEmailFax;
    private Integer userId;

    public String getFileNumberIds() {
        return fileNumberIds;
    }

    public void setFileNumberIds(String fileNumberIds) {
        this.fileNumberIds = fileNumberIds;
    }

    public String getFileNumbers() {
        return fileNumbers;
    }

    public void setFileNumbers(String fileNumbers) {
        this.fileNumbers = fileNumbers;
    }

    public Long getSsDetailId() {
        return ssDetailId;
    }

    public void setSsDetailId(Long ssDetailId) {
        this.ssDetailId = ssDetailId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getToEmailFax() {
        return toEmailFax;
    }

    public void setToEmailFax(String toEmailFax) {
        this.toEmailFax = toEmailFax;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

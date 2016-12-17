/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import com.gp.cong.common.DateUtils;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author VinothS
 */
public class LclInlandVoyageInfoBean implements Serializable {

    private String scheduleNo;
    private Date sailDate;//std
    private String acctName;//ssLine
    private String vesselName;//spRefName
    private String ssVoy;//spRefNo
    private String departurePier;
    private String unitNo;
    private String size;
    private Long unitSsId;
    private Long headerId;
    private String origin;
    private String destination;

    public LclInlandVoyageInfoBean() {
    }

    public LclInlandVoyageInfoBean(Object[] obj) throws Exception {
        int index = 0;
        scheduleNo = null == obj[index] ? null : obj[index].toString();
        index++;
        String strDate = null == obj[index] ? null : obj[index].toString();
        if (null != strDate && !strDate.equals("0")) {
            sailDate = DateUtils.parseStringToDateWithTime(strDate);
        }
        index++;
        acctName = null == obj[index] ? null : obj[index].toString();
        index++;
        vesselName = null == obj[index] ? null : obj[index].toString();
        index++;
        ssVoy = null == obj[index] ? null : obj[index].toString();
        index++;
        departurePier = null == obj[index] ? null : obj[index].toString();
        index++;
        unitNo = null == obj[index] ? null : obj[index].toString();
        index++;
        size = null == obj[index] ? null : obj[index].toString();
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getDeparturePier() {
        return departurePier;
    }

    public void setDeparturePier(String departurePier) {
        this.departurePier = departurePier;
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getSsVoy() {
        return ssVoy;
    }

    public void setSsVoy(String ssVoy) {
        this.ssVoy = ssVoy;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Long getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(Long unitSsId) {
        this.unitSsId = unitSsId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author lakshh
 */
public class LclBookingVoyageBean implements Serializable {

    private String ssHeaderId;
    private String voyageNo;
    private String pooUnlocCode;
    private String fdUnloCode;
    private String carrierName;
    private String carrierAcctNo;
    private String ssVoyage;
    private String vesselName;
    private String polUnlocCode;
    private String podUnloCode;
    private String departPier;
    private Date sailingDate;
    private Date etaPod;
    private Date originLrd;
    private Date loadLrd;
    private Date podAtEta;
    private Date fdEta;
    private Date polLoadLrd;
    private long ttPolPod;
    private long ttPooFd;
    private String relaylrdOverride;
    private String relayttOverride;
    private String scheduleK;
    private String departSched;

    public LclBookingVoyageBean() {
    }

    public LclBookingVoyageBean(Object[] obj, Integer poo, Integer pol, Integer pod,
            Integer fd, LclBookingPlanBean lclBookingPlanBean) throws Exception {
        int index = 0;
        ssHeaderId = null == obj[index] ? null : obj[index].toString();
        index++;
        voyageNo = null == obj[index] ? null : obj[index].toString();
        index++;
        pooUnlocCode = null == obj[index] ? null : obj[index].toString();
        index++;
        fdUnloCode = null == obj[index] ? null : obj[index].toString();
        index++;
        carrierName = null == obj[index] ? null : obj[index].toString();
        index++;
        carrierAcctNo = null == obj[index] ? null : obj[index].toString();
        index++;
        ssVoyage = null == obj[index] ? null : obj[index].toString();
        index++;
        vesselName = null == obj[index] ? null : obj[index].toString();
        index++;
        polUnlocCode = null == obj[index] ? null : obj[index].toString();
        index++;
        podUnloCode = null == obj[index] ? null : obj[index].toString();
        index++;
        departPier = null == obj[index] ? null : obj[index].toString();
        index++;

        String strSailDate = null == obj[index] ? null : obj[index].toString();
        if (null != strSailDate && !strSailDate.equals("0")) {
            sailingDate = DateUtils.parseStringToDate(strSailDate);
            //  strSailDate = DateUtils.formatDate(DateUtils.parseStringToDate(strSailDate), "dd-MMM-yyyy");
        }

        index++;
        String strEtaPod = null == obj[index] ? null : obj[index].toString();
        if (null != strEtaPod && !strEtaPod.equals("0")) {
            etaPod = DateUtils.parseStringToDateWithTime(strEtaPod);
        }
        index++;
        relaylrdOverride = null == obj[index] ? null : obj[index].toString();
        index++;
        relayttOverride = null == obj[index] ? null : obj[index].toString();
        index++;
        scheduleK = null == obj[index] ? null : obj[index].toString();
        index++;
        departSched = null == obj[index] ? null : obj[index].toString();
        index++;
        /**
         * ***************************************************
         * POL Loading LRD
         * This is always computed.
         * ***************************************************
         */
        Calendar calLoadingLRD = Calendar.getInstance();
        calLoadingLRD.setTime(sailingDate);

        /**
         * POL Cut-off Days Before Departure
         * Use LRD override if present otherwise use the relay CO_DBD.
         */
        calLoadingLRD.add(Calendar.DATE, -(null != relaylrdOverride ? Integer.parseInt(relaylrdOverride) : lclBookingPlanBean.getPol_co_dbd()));

        /* Don't permit Weekend days (Sat, Sun) */
        loadLrd = calLoadingLRD.getTime();
        if (loadLrd.getDay() == 0) {
            calLoadingLRD.add(Calendar.DATE, -2);
        } else if (loadLrd.getDay() == 6) {
            calLoadingLRD.add(Calendar.DATE, -1);
        }

        /**
         * POL Cut-Off Day of Week (CO_DOW)
         * Logic assumes Sat/Sun are never specified in CO_DOW.
         */
        loadLrd = calLoadingLRD.getTime();
        if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPol_co_dow())) {
            int mDays = 0;

            if (loadLrd.getDay() > lclBookingPlanBean.getPol_co_dow()) {
                /* Backup to the Cut-Off Day of Week in the CURRENT week */
                mDays = loadLrd.getDay() - lclBookingPlanBean.getPol_co_dow();
            } else if (loadLrd.getDay() < lclBookingPlanBean.getPol_co_dow()) {
                /* Backup to the Cut-Off Day of Week in the PREVIOUS week */
                mDays = 7 - (lclBookingPlanBean.getPol_co_dow() - loadLrd.getDay());
            }

            calLoadingLRD.add(Calendar.DATE, -mDays);
        }

        /* Check for Alternate LRD */
        Date altDate = new LclBookingPlanDAO().getAltDateByOriginalDate(poo, pol, "LRD", calLoadingLRD.getTime());
        calLoadingLRD.setTime(altDate);

        /* Set Cut-Off Time Of Day (CO_TOD) */
        if (null != lclBookingPlanBean.getPol_co_tod()) {
            calLoadingLRD.add(Calendar.HOUR, lclBookingPlanBean.getPol_co_tod().getHours());
            calLoadingLRD.add(Calendar.MINUTE, lclBookingPlanBean.getPol_co_tod().getMinutes());
            calLoadingLRD.add(Calendar.SECOND, lclBookingPlanBean.getPol_co_tod().getSeconds());
        }

        loadLrd = calLoadingLRD.getTime();

        /**
         * ***************************************************
         * Origin LRD (Last Receive Date)
         * Computed when POO is not the same as POL.
         *
         * @ticket 0009282, 0011597
         * ***************************************************
         */
        originLrd = loadLrd;
        if (poo.intValue() != pol.intValue()) {
            originLrd = DateUtils.formatDateAndParseToDate(loadLrd);
            /* POO is not the same as POL, compute it. */
            Calendar calOriginLRD = Calendar.getInstance();
            calOriginLRD.setTime(originLrd);

            /* Subtract POO -> POL Transit Time (in days) */
            calOriginLRD.add(Calendar.DATE, -lclBookingPlanBean.getPoo_transit_time());

            /**
             * POO Cut-Off Day of Week (CO_DOW)
             * Logic assumes Sat/Sun are never specified in CO_DOW.
             */
            originLrd = calOriginLRD.getTime();
            if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPoo_co_dow())) {
                int mDays = 0;

                if (originLrd.getDay() > lclBookingPlanBean.getPoo_co_dow()) {
                    /* Backup to the Cut-Off Day of Week in the CURRENT week */
                    mDays = originLrd.getDay() - lclBookingPlanBean.getPoo_co_dow();
                } else if (originLrd.getDay() < lclBookingPlanBean.getPoo_co_dow()) {
                    /* Backup to the Cut-Off Day of Week in the PREVIOUS week */
                    mDays = 7 - (lclBookingPlanBean.getPoo_co_dow() - originLrd.getDay());
                }

                calOriginLRD.add(Calendar.DATE, -mDays);
            }

            /* Check for Alternate LRD */
            altDate = new LclBookingPlanDAO().getAltDateByOriginalDate(poo, pol, "LRD", calOriginLRD.getTime());
            calOriginLRD.setTime(altDate);

            /* Set Cut-Off Time Of Day (CO_TOD) */
            if (null != lclBookingPlanBean.getPoo_co_tod()) {
                calOriginLRD.add(Calendar.HOUR, lclBookingPlanBean.getPoo_co_tod().getHours());
                calOriginLRD.add(Calendar.MINUTE, lclBookingPlanBean.getPoo_co_tod().getMinutes());
                calOriginLRD.add(Calendar.SECOND, lclBookingPlanBean.getPoo_co_tod().getSeconds());
            }

            originLrd = calOriginLRD.getTime();
        } // Origin LRD

        /**
         * ***************************************************
         * POD ETA
         * ***************************************************
         */
        Calendar calPODETA = Calendar.getInstance();
        calPODETA.setTime(sailingDate);
        calPODETA.add(Calendar.DATE, +(null != relayttOverride ? Integer.parseInt(relayttOverride) : lclBookingPlanBean.getPol_transit_time()));
        podAtEta = calPODETA.getTime();

        /**
         * ***************************************************
         * FD ETA
         * ***************************************************
         */
        fdEta = podAtEta;
        if (pod.intValue() != fd.intValue()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(podAtEta);
            cal.add(Calendar.DATE, lclBookingPlanBean.getFd_transit_time());
            fdEta = cal.getTime();
            /**
             * NOTE:
             * lcl_booking.pod_fd_tt must be added to this datetime value
             * elsewhere.
             */
        }

        if (podAtEta != null && sailingDate != null) {
            ttPolPod = DateUtils.daysBetween(sailingDate, podAtEta);
        }

        if (originLrd != null && fdEta != null) {
            ttPooFd = DateUtils.daysBetween(originLrd, fdEta);
        }
    }

    public String getCarrierAcctNo() {
        return carrierAcctNo;
    }

    public void setCarrierAcctNo(String carrierAcctNo) {
        this.carrierAcctNo = carrierAcctNo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDepartPier() {
        return departPier;
    }

    public void setDepartPier(String departPier) {
        this.departPier = departPier;
    }

    public String getFdUnloCode() {
        return fdUnloCode;
    }

    public void setFdUnloCode(String fdUnloCode) {
        this.fdUnloCode = fdUnloCode;
    }

    public Date getOriginLrd() {
        return originLrd;
    }

    public void setOriginLrd(Date originLrd) {
        this.originLrd = originLrd;
    }

    public String getPodUnloCode() {
        return podUnloCode;
    }

    public void setPodUnloCode(String podUnloCode) {
        this.podUnloCode = podUnloCode;
    }

    public String getPolUnlocCode() {
        return polUnlocCode;
    }

    public void setPolUnlocCode(String polUnlocCode) {
        this.polUnlocCode = polUnlocCode;
    }

    public String getPooUnlocCode() {
        return pooUnlocCode;
    }

    public void setPooUnlocCode(String pooUnlocCode) {
        this.pooUnlocCode = pooUnlocCode;
    }

    public Date getEtaPod() {
        return etaPod;
    }

    public void setEtaPod(Date etaPod) {
        this.etaPod = etaPod;
    }

    public Date getFdEta() {
        return fdEta;
    }

    public void setFdEta(Date fdEta) {
        this.fdEta = fdEta;
    }

    public Date getPodAtEta() {
        return podAtEta;
    }

    public void setPodAtEta(Date podAtEta) {
        this.podAtEta = podAtEta;
    }

    public Date getSailingDate() {
        return sailingDate;
    }

    public void setSailingDate(Date sailingDate) {
        this.sailingDate = sailingDate;
    }

    public String getSsHeaderId() {
        return ssHeaderId;
    }

    public void setSsHeaderId(String ssHeaderId) {
        this.ssHeaderId = ssHeaderId;
    }

    public String getSsVoyage() {
        return ssVoyage;
    }

    public void setSsVoyage(String ssVoyage) {
        this.ssVoyage = ssVoyage;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public Date getPolLoadLrd() {
        return polLoadLrd;
    }

    public void setPolLoadLrd(Date polLoadLrd) {
        this.polLoadLrd = polLoadLrd;
    }

    public long getTtPolPod() {
        return ttPolPod;
    }

    public void setTtPolPod(long ttPolPod) {
        this.ttPolPod = ttPolPod;
    }

    public long getTtPooFd() {
        return ttPooFd;
    }

    public void setTtPooFd(long ttPooFd) {
        this.ttPooFd = ttPooFd;
    }

    public Date getLoadLrd() {
        return loadLrd;
    }

    public void setLoadLrd(Date loadLrd) {
        this.loadLrd = loadLrd;
    }

    public String getRelaylrdOverride() {
        return relaylrdOverride;
    }

    public void setRelaylrdOverride(String relaylrdOverride) {
        this.relaylrdOverride = relaylrdOverride;
    }

    public String getRelayttOverride() {
        return relayttOverride;
    }

    public void setRelayttOverride(String relayttOverride) {
        this.relayttOverride = relayttOverride;
    }

    public String getScheduleK() {
        return scheduleK;
    }

    public void setScheduleK(String scheduleK) {
        this.scheduleK = scheduleK;
    }

    public String getDepartSched() {
        return departSched;
    }

    public void setDepartSched(String departSched) {
        this.departSched = departSched;
    }
      
}

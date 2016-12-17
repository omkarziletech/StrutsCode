/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.model;

import java.util.Date;

/**
 *
 * @author lakshh
 */
public class LclBookingPlanBean {

    private Integer poo_id;
    private String poo_code;
    private String poo_name;
    private Integer poo_co_dow;
    private Date poo_co_tod;
    private Integer poo_transit_time;
    private Integer pol_id;
    private String pol_code;
    private String pol_name;
    private Integer pol_co_dbd;
    private Integer pol_co_dow;
    private Date pol_co_tod;
    private Integer pol_transit_time;
    private Integer pod_id;
    private String pod_code;
    private String pod_name;
    private Integer fd_id;
    private String fd_code;
    private String fd_name;
    private Integer fd_transit_time;
    private Integer total_transit_time;
//    private String pooId;
//    private String pooCode;
//    private String pooName;
//    private String pooCoDow;
//    private String pooCoTod;
//    private String pooTransitTime;
//    private String polId;
//    private String polCode;
//    private String polName;
//    private String polCoDbd;
//    private Integer polCoDow;
//    private String polCoTod;
//    private String polTransitTime;
//    private String podId;
//    private String podCode;
//    private String podName;
//    private String fdId;
//    private String fdCode;
//    private String fdName;
//    private String fdTransitTime;
//    private String totalTransitTime;

    public LclBookingPlanBean() {
    }

    public LclBookingPlanBean(Object[] obj) {
//        int index = 0;
//        pooId = null == obj[index] ? null : obj[index].toString();
//        index++;
//        pooCode = null == obj[index] ? null : obj[index].toString();
//        index++;
//        pooName = null == obj[index] ? null : obj[index].toString();
//        index++;
//        pooCoDow = null == obj[index] ? null : obj[index].toString();
//        index++;
//        pooCoTod = null == obj[index] ? null : obj[index].toString();
//        index++;
//        pooTransitTime = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polId = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polCode = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polName = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polCoDbd = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polCoDow = null == obj[index] ? null : Integer.parseInt(obj[index].toString());
//        index++;
//        polCoTod = null == obj[index] ? null : obj[index].toString();
//        index++;
//        polTransitTime = null == obj[index] ? null : obj[index].toString();
//        index++;
//        podId = null == obj[index] ? null : obj[index].toString();
//        index++;
//        podCode = null == obj[index] ? null : obj[index].toString();
//        index++;
//        podName = null == obj[index] ? null : obj[index].toString();
//        index++;
//        fdId = null == obj[index] ? null : obj[index].toString();
//        index++;
//        fdCode = null == obj[index] ? null : obj[index].toString();
//        index++;
//        fdName = null == obj[index] ? null : obj[index].toString();
//        index++;
//        fdTransitTime = null == obj[index] ? null : obj[index].toString();
//        index++;
//        totalTransitTime = null == obj[index] ? null : obj[index].toString();
    }

    public String getFd_code() {
        return fd_code;
    }

    public void setFd_code(String fd_code) {
        this.fd_code = fd_code;
    }

    public Integer getFd_id() {
        return fd_id;
    }

    public void setFd_id(Integer fd_id) {
        this.fd_id = fd_id;
    }

    public String getFd_name() {
        return fd_name;
    }

    public void setFd_name(String fd_name) {
        this.fd_name = fd_name;
    }

    public Integer getFd_transit_time() {
        return fd_transit_time;
    }

    public void setFd_transit_time(Integer fd_transit_time) {
        this.fd_transit_time = fd_transit_time;
    }

    public String getPod_code() {
        return pod_code;
    }

    public void setPod_code(String pod_code) {
        this.pod_code = pod_code;
    }

    public Integer getPod_id() {
        return pod_id;
    }

    public void setPod_id(Integer pod_id) {
        this.pod_id = pod_id;
    }

    public String getPod_name() {
        return pod_name;
    }

    public void setPod_name(String pod_name) {
        this.pod_name = pod_name;
    }

    public Integer getPol_co_dbd() {
        return pol_co_dbd;
    }

    public void setPol_co_dbd(Integer pol_co_dbd) {
        this.pol_co_dbd = pol_co_dbd;
    }

    public String getPol_code() {
        return pol_code;
    }

    public void setPol_code(String pol_code) {
        this.pol_code = pol_code;
    }

    public Integer getPol_id() {
        return pol_id;
    }

    public void setPol_id(Integer pol_id) {
        this.pol_id = pol_id;
    }

    public String getPol_name() {
        return pol_name;
    }

    public void setPol_name(String pol_name) {
        this.pol_name = pol_name;
    }

    public Integer getPol_transit_time() {
        return pol_transit_time;
    }

    public void setPol_transit_time(Integer pol_transit_time) {
        this.pol_transit_time = pol_transit_time;
    }

    public Integer getPoo_co_dow() {
        return poo_co_dow;
    }

    public void setPoo_co_dow(Integer poo_co_dow) {
        this.poo_co_dow = poo_co_dow;
    }

    public String getPoo_code() {
        return poo_code;
    }

    public void setPoo_code(String poo_code) {
        this.poo_code = poo_code;
    }

    public Integer getPoo_id() {
        return poo_id;
    }

    public void setPoo_id(Integer poo_id) {
        this.poo_id = poo_id;
    }

    public String getPoo_name() {
        return poo_name;
    }

    public void setPoo_name(String poo_name) {
        this.poo_name = poo_name;
    }

    public Integer getPoo_transit_time() {
        return poo_transit_time;
    }

    public void setPoo_transit_time(Integer poo_transit_time) {
        this.poo_transit_time = poo_transit_time;
    }

    public Integer getTotal_transit_time() {
        return total_transit_time;
    }

    public void setTotal_transit_time(Integer total_transit_time) {
        this.total_transit_time = total_transit_time;
    }

    public Integer getPol_co_dow() {
        return pol_co_dow;
    }

    public void setPol_co_dow(Integer pol_co_dow) {
        this.pol_co_dow = pol_co_dow;
    }

    public Date getPol_co_tod() {
        return pol_co_tod;
    }

    public void setPol_co_tod(Date pol_co_tod) {
        this.pol_co_tod = pol_co_tod;
    }

    public Date getPoo_co_tod() {
        return poo_co_tod;
    }

    public void setPoo_co_tod(Date poo_co_tod) {
        this.poo_co_tod = poo_co_tod;
    }
}

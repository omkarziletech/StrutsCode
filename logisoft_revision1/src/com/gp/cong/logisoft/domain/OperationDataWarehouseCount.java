package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class OperationDataWarehouseCount implements Serializable {

    private Integer id;
    private String year;
    private String month;
    private Integer quoteCount;
    private Integer bookingCount;
    private Integer blCount;
    private Double percentBkgQuote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getQuoteCount() {
        return quoteCount;
    }

    public void setQuoteCount(Integer quoteCount) {
        this.quoteCount = quoteCount;
    }

    public Integer getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(Integer bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Integer getBlCount() {
        return blCount;
    }

    public void setBlCount(Integer blCount) {
        this.blCount = blCount;
    }

    public Double getPercentBkgQuote() {
        return percentBkgQuote;
    }

    public void setPercentBkgQuote(Double percentBkgQuote) {
        this.percentBkgQuote = percentBkgQuote;
    }
}

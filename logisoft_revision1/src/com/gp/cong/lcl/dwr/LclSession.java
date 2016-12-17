/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.dwr;

import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.beans.StopoffsBean;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHazmat;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cvst.logisoft.struts.form.lcl.SessionLclSearchForm;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lakshh
 */
public class LclSession implements Serializable {

    private String actualCommodity;
    private Long fileId;
    private boolean cargoReceived;
    private String consolidated;
    private String consolidatedFileId;
    private String searchResult;
    private SessionLclSearchForm searchForm;
    private String freeFormatValue;
    private List<LclBookingPieceDetail> bookingDetailList;
    private List<LclQuotePieceDetail> quoteDetailList;
    private List<LclBlPieceDetail> blDetailList;
    private List<LclBookingPiece> commodityList;
    private List<LclQuotePiece> quoteCommodityList;
    private List<LclBlPiece> blCommodityList;
    private List<LclBookingAc> bookingAcList;
    private List<LclQuoteAc> quoteAcList;
    private List<LclBookingHazmat> bookingHazmatList;
    private List<LclQuoteHazmat> quoteHazmatList;
    private String searchFileNo;
    //webservises properties
    private List carrierList;
    private List carrierCostList;
    private Map xmlObjMap;
    private String selectedMenu;
    private boolean isArGlmappingFlag;
    private String glMappingBlueCode;
    private List<RoutingOptionsBean> routingOptionsList;
    private List<StopoffsBean> stopOffList;
    private String printFaxRadioLclBl;
    private String overRiddedRemarks;
    private boolean includeDestfees;

    public List<LclBookingAc> getBookingAcList() {
        return bookingAcList;
    }

    public void setBookingAcList(List<LclBookingAc> bookingAcList) {
        this.bookingAcList = bookingAcList;
    }

    public String getSearchFileNo() {
        return searchFileNo;
    }

    public void setSearchFileNo(String searchFileNo) {
        this.searchFileNo = searchFileNo;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public List<LclQuoteHazmat> getQuoteHazmatList() {
        return quoteHazmatList;
    }

    public void setQuoteHazmatList(List<LclQuoteHazmat> quoteHazmatList) {
        this.quoteHazmatList = quoteHazmatList;
    }

    public List<LclBookingHazmat> getBookingHazmatList() {
        return bookingHazmatList;
    }

    public void setBookingHazmatList(List<LclBookingHazmat> bookingHazmatList) {
        this.bookingHazmatList = bookingHazmatList;
    }

    public List<LclQuotePiece> getQuoteCommodityList() {
        return quoteCommodityList;
    }

    public void setQuoteCommodityList(List<LclQuotePiece> quoteCommodityList) {
        this.quoteCommodityList = quoteCommodityList;
    }

    public List<LclBookingPiece> getCommodityList() {
        return commodityList;
    }

    public void setCommodityList(List<LclBookingPiece> commodityList) {
        this.commodityList = commodityList;
    }

    public List<LclBlPiece> getBlCommodityList() {
        return blCommodityList;
    }

    public void setBlCommodityList(List<LclBlPiece> blCommodityList) {
        this.blCommodityList = blCommodityList;
    }

    public String getFreeFormatValue() {
        return freeFormatValue;
    }

    public void setFreeFormatValue(String freeFormatValue) {
        this.freeFormatValue = freeFormatValue;
    }

    public String getConsolidated() {
        return consolidated;
    }

    public void setConsolidated(String consolidated) {
        this.consolidated = consolidated;
    }

    public String getConsolidatedFileId() {
        return consolidatedFileId;
    }

    public void setConsolidatedFileId(String consolidatedFileId) {
        this.consolidatedFileId = consolidatedFileId;
    }

    public String getActualCommodity() {
        return actualCommodity;
    }

    public void setActualCommodity(String actualCommodity) {
        this.actualCommodity = actualCommodity;
    }

    public List<LclBlPieceDetail> getBlDetailList() {
        return blDetailList;
    }

    public void setBlDetailList(List<LclBlPieceDetail> blDetailList) {
        this.blDetailList = blDetailList;
    }

    public List<LclBookingPieceDetail> getBookingDetailList() {
        return bookingDetailList;
    }

    public void setBookingDetailList(List<LclBookingPieceDetail> bookingDetailList) {
        this.bookingDetailList = bookingDetailList;
    }

    public List<LclQuotePieceDetail> getQuoteDetailList() {
        return quoteDetailList;
    }

    public void setQuoteDetailList(List<LclQuotePieceDetail> quoteDetailList) {
        this.quoteDetailList = quoteDetailList;
    }

    public boolean isCargoReceived() {
        return cargoReceived;
    }

    public void setCargoReceived(boolean cargoReceived) {
        this.cargoReceived = cargoReceived;
    }

    public List getCarrierList() {
        return carrierList;
    }

    public void setCarrierList(List carrierList) {
        this.carrierList = carrierList;
    }

    public Map getXmlObjMap() {
        return xmlObjMap;
    }

    public void setXmlObjMap(Map xmlObjMap) {
        this.xmlObjMap = xmlObjMap;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public SessionLclSearchForm getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(SessionLclSearchForm searchForm) {
        this.searchForm = searchForm;
    }

    public List<RoutingOptionsBean> getRoutingOptionsList() {
        return routingOptionsList;
    }

    public void setRoutingOptionsList(List<RoutingOptionsBean> routingOptionsList) {
        this.routingOptionsList = routingOptionsList;
    }

    public String getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(String selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public List<LclQuoteAc> getQuoteAcList() {
        return quoteAcList;
    }

    public void setQuoteAcList(List<LclQuoteAc> quoteAcList) {
        this.quoteAcList = quoteAcList;
    }

    public List getCarrierCostList() {
        return carrierCostList;
    }

    public void setCarrierCostList(List carrierCostList) {
        this.carrierCostList = carrierCostList;
    }

    public boolean isIsArGlmappingFlag() {
        return isArGlmappingFlag;
    }

    public void setIsArGlmappingFlag(boolean isArGlmappingFlag) {
        this.isArGlmappingFlag = isArGlmappingFlag;
    }

    public String getGlMappingBlueCode() {
        return glMappingBlueCode;
    }

    public void setGlMappingBlueCode(String glMappingBlueCode) {
        this.glMappingBlueCode = glMappingBlueCode;
    }

    public List<StopoffsBean> getStopOffList() {
        return stopOffList;
    }

    public void setStopOffList(List<StopoffsBean> stopOffList) {
        this.stopOffList = stopOffList;
    }

    public String getPrintFaxRadioLclBl() {
        return printFaxRadioLclBl;
    }

    public void setPrintFaxRadioLclBl(String printFaxRadioLclBl) {
        this.printFaxRadioLclBl = printFaxRadioLclBl;
    }

    public String getOverRiddedRemarks() {
        return overRiddedRemarks;
    }

    public void setOverRiddedRemarks(String overRiddedRemarks) {
        this.overRiddedRemarks = overRiddedRemarks;
    }

    public boolean isIncludeDestfees() {
        return includeDestfees;
    }

    public void setIncludeDestfees(boolean includeDestfees) {
        this.includeDestfees = includeDestfees;
    }

    
}

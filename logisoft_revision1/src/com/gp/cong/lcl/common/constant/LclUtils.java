package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.GENERIC_EVENT_CODE_CORRECTION_NOTES;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_ACCRUALS;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.LCL_IMPORT;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_FL;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_MAX;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_MIN;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_PCT;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_VOLUME;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UNIT_PER_UOM_WEIGHT;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.RATE_UOM_M;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.REMARKS_TYPE_LOADING_REMARKS;
import static com.gp.cong.lcl.common.constant.LclCommonConstant._3PARTY_TYPE_OVR;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.LclBookingModel;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.CreditDebitNote;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTa;
import com.gp.cong.logisoft.domain.lcl.LclBookingAcTrans;
import com.gp.cong.logisoft.domain.lcl.LclBookingDispo;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsContact;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import com.gp.cong.logisoft.hibernate.dao.CreditDebitNoteDAO;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingAcTaDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRelayDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBookingForm;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.accounting.model.ManifestModel;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

public class LclUtils extends BaseHibernateDAO implements ConstantsInterface, LclReportConstants, LclCommonConstant {

    private String highVolumeMessage;
    private List<LclBookingAc> lclBookingAcList;
    private List<LclQuoteAc> lclQuoteAcList;
    private StringBuilder fileNumbers = new StringBuilder();

    public StringBuilder getFileNumbers() {
        return fileNumbers;
    }

    public void setFileNumbers(StringBuilder fileNumbers) {
        this.fileNumbers = fileNumbers;
    }

    public List<LclBookingAc> getLclBookingAcList() {
        return lclBookingAcList;
    }

    public void setLclBookingAcList(List<LclBookingAc> lclBookingAcList) {
        this.lclBookingAcList = lclBookingAcList;
    }

    public List<LclQuoteAc> getLclQuoteAcList() {
        return lclQuoteAcList;
    }

    public void setLclQuoteAcList(List<LclQuoteAc> lclQuoteAcList) {
        this.lclQuoteAcList = lclQuoteAcList;
    }

    public String getHighVolumeMessage() {
        return highVolumeMessage;
    }

    public void setHighVolumeMessage(String highVolumeMessage) {
        this.highVolumeMessage = highVolumeMessage;
    }

    public String getConcatenatedPortOfOrigin(LclBooking lclBooking) {
        if (lclBooking.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName()) && null != lclBooking.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getStateId().getCode())
                    && CommonUtils.isEmpty(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + ", " + lclBooking.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName())
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode()) && null != lclBooking.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getStateId().getCode())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + ", " + lclBooking.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationName())
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfOrigin().getUnLocationName() + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
            }
            return builder.toString().toUpperCase();
        } else {
            return "";
        }
    }

    public String getConcatenatedOriginByUnlocation(UnLocation unlocation) {
        if (unlocation != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && null != unlocation.getCountryId()
                    && CommonUtils.isNotEmpty(unlocation.getCountryId().getCodedesc()) && CommonUtils.isEmpty(unlocation.getUnLocationCode())) {
                builder.append(unlocation.getUnLocationName().toUpperCase() + "/");
                if (unlocation.getCountryId().getCodedesc().equalsIgnoreCase("UNITED STATES") && unlocation.getStateId() != null
                        && unlocation.getStateId().getCode() != null && !unlocation.getStateId().getCode().trim().equals("")) {
                    builder.append(unlocation.getStateId().getCode().toUpperCase());
                } else {
                    builder.append(unlocation.getCountryId().getCodedesc().toUpperCase());
                }
            } else if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && CommonUtils.isNotEmpty(unlocation.getUnLocationCode())
                    && null != unlocation.getCountryId() && CommonUtils.isNotEmpty(unlocation.getCountryId().getCodedesc())) {
                builder.append(unlocation.getUnLocationName().toUpperCase() + "/");
                if (unlocation.getCountryId().getCodedesc().equalsIgnoreCase("UNITED STATES") && unlocation.getStateId() != null
                        && unlocation.getStateId().getCode() != null && !unlocation.getStateId().getCode().trim().equals("")) {
                    builder.append(unlocation.getStateId().getCode().toUpperCase());
                } else {
                    builder.append(unlocation.getCountryId().getCodedesc().toUpperCase());
                }
                builder.append("(" + unlocation.getUnLocationCode().toUpperCase() + ")");
            } else if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && CommonUtils.isNotEmpty(unlocation.getUnLocationCode())) {
                builder.append(unlocation.getUnLocationName().toUpperCase() + "(" + unlocation.getUnLocationCode().toUpperCase() + ")");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getConcatenatedOriginByWithOutCode(UnLocation unlocation) {
        if (unlocation != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && null != unlocation.getCountryId()
                    && CommonUtils.isNotEmpty(unlocation.getCountryId().getCodedesc()) && CommonUtils.isEmpty(unlocation.getUnLocationCode())) {
                builder.append(unlocation.getUnLocationName().toUpperCase() + "/");
                if (unlocation.getCountryId().getCodedesc().equalsIgnoreCase("UNITED STATES") && unlocation.getStateId() != null
                        && unlocation.getStateId().getCode() != null && !unlocation.getStateId().getCode().trim().equals("")) {
                    builder.append(unlocation.getStateId().getCode().toUpperCase());
                } else {
                    builder.append(unlocation.getCountryId().getCodedesc().toUpperCase());
                }
            } else if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && CommonUtils.isNotEmpty(unlocation.getUnLocationCode())
                    && null != unlocation.getCountryId() && CommonUtils.isNotEmpty(unlocation.getCountryId().getCodedesc())) {
                builder.append(unlocation.getUnLocationName().toUpperCase() + "/");
                if (unlocation.getCountryId().getCodedesc().equalsIgnoreCase("UNITED STATES") && unlocation.getStateId() != null
                        && unlocation.getStateId().getCode() != null && !unlocation.getStateId().getCode().trim().equals("")) {
                    builder.append(unlocation.getStateId().getCode().toUpperCase());
                } else {
                    builder.append(unlocation.getCountryId().getCodedesc().toUpperCase());
                }
            } else if (CommonUtils.isNotEmpty(unlocation.getUnLocationName()) && CommonUtils.isNotEmpty(unlocation.getUnLocationCode())) {
                builder.append(unlocation.getUnLocationName().toUpperCase());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getBlConcatenatedPortOfOrigin(LclBl lclBl) {
        if (lclBl.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationName()) && null != lclBl.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getStateId().getCode()) && CommonUtils.isEmpty(lclBl.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBl.getPortOfOrigin().getUnLocationName() + "," + lclBl.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationCode()) && null != lclBl.getPortOfOrigin().getStateId() && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getStateId().getCode())) {
                builder.append(lclBl.getPortOfOrigin().getUnLocationName() + "," + lclBl.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclBl.getPortOfOrigin().getUnLocationName() + "(" + lclBl.getPortOfOrigin().getUnLocationCode() + ")");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getQuoteConcatPortOfOrigin(LclQuote lclQuote) {
        if (lclQuote.getPortOfOrigin() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && null != lclQuote.getPortOfOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getStateId().getCode()) && CommonUtils.isEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + ", " + lclQuote.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode()) && null != lclQuote.getPortOfOrigin().getStateId() && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getStateId().getCode())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + ", " + lclQuote.getPortOfOrigin().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                builder.append(lclQuote.getPortOfOrigin().getUnLocationName() + "(" + lclQuote.getPortOfOrigin().getUnLocationCode() + ")");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getConcatenatedFinalDestination(LclBooking lclBooking) {
        if (lclBooking.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && null != lclBooking.getFinalDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getStateId().getCode()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + "/" + lclBooking.getFinalDestination().getStateId().getCode() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName()) && lclBooking.getFinalDestination().getCountryId() != null
                    && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + "/" + lclBooking.getFinalDestination().getCountryId().getCodedesc() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
            } else if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                builder.append(lclBooking.getFinalDestination().getUnLocationName() + '(' + lclBooking.getFinalDestination().getUnLocationCode() + ')');
            }
            return builder.toString().toUpperCase();
        } else {
            return "";
        }
    }

    public String getQuoteConcatenatedFinalDestination(LclQuote lclQuote) {
        if (lclQuote.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName())
                    && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName() + ", " + lclQuote.getFinalDestination().getCountryId().getCodedesc());
            }
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName() + ", " + lclQuote.getFinalDestination().getCountryId().getCodedesc());
            } else if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName());
            } else if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                builder.append(lclQuote.getFinalDestination().getUnLocationName());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getBLConcatenatedPortOfDestination(LclBl lclBl) {
        if (lclBl.getFinalDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationName())
                    && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBl.getPortOfDestination().getUnLocationName() + ", " + lclBl.getPortOfDestination().getCountryId().getCodedesc());
            }
            if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getCountryId().getCodedesc())) {
                builder.append(lclBl.getFinalDestination().getUnLocationName() + ", " + lclBl.getPortOfDestination().getCountryId().getCodedesc());
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclBl.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBl.getPortOfDestination().getUnLocationName());
            } else if (CommonUtils.isNotEmpty(lclBl.getPortOfDestination().getUnLocationCode()) && CommonUtils.isEmpty(lclBl.getPortOfDestination().getCountryId().getCodedesc()) && CommonUtils.isEmpty(lclBl.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBl.getPortOfDestination().getUnLocationName());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public String getConcatenatedPortOfDestination(LclBooking lclBooking) {
        if (lclBooking.getPortOfDestination() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && null != lclBooking.getPortOfDestination().getStateId()
                    && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getStateId().getCode()) && CommonUtils.isEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + ", " + lclBooking.getPortOfDestination().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())
                    && null != lclBooking.getPortOfDestination().getStateId() && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getStateId().getCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + ", " + lclBooking.getPortOfDestination().getStateId().getCode());
            } else if (CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationName()) && CommonUtils.isNotEmpty(lclBooking.getPortOfDestination().getUnLocationCode())) {
                builder.append(lclBooking.getPortOfDestination().getUnLocationName() + "(" + lclBooking.getPortOfDestination().getUnLocationCode() + ")");
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public List getRolledUpCharges(List<LclBookingPiece> lclCommodityList, List<LclBookingAc> lclBookingAcList, String engmet, String pdfFormatLabel) {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            if (!chargesInfoMap.containsKey(lclBookingAc.getArglMapping().getChargeCode())) {
                formatLabelCharge(lclCommodityList, lclBookingAc, engmet);
                lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                if (!lclBookingAc.isManualEntry() && lclBookingAc.getRatePerUnitUom() != null && lclBookingAc.getRateUom() != null && lclBookingAc.getArglMapping() != null
                        && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightImperial());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightImperial());
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeImperial());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeImperial());
                                }
                            }

                        } else if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightMetric());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightMetric());
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeMetric());
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeMetric());
                                }
                            }
                        }
                    }
                }
                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAc);
            } else {
                LclBookingAc lclBookingAcFromMap = (LclBookingAc) chargesInfoMap.get(lclBookingAc.getArglMapping().getChargeCode());
                if (!lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("FL") || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("TTBARR")) {
                    BigDecimal total = new BigDecimal(lclBookingAc.getArAmount().doubleValue() + lclBookingAcFromMap.getRolledupCharges().doubleValue());
                    total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                    lclBookingAcFromMap.setRolledupCharges(total);
                }
                if (lclBookingAcFromMap.getRatePerUnitUom() != null && lclBookingAcFromMap.getRateUom() != null && lclBookingAcFromMap.getArglMapping() != null
                        && lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 100) * calculatedWeight;
                                }
                            }
                        } else if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                                }
                            }
                        }
                        if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure()) && !CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                            calculatedMeasure = lclBookingAcFromMap.getTotalMeasure().doubleValue() * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        minchg = lclBookingAc.getRateFlatMinimum().doubleValue();
                        lclBookingAcFromMap.setRatePerVolumeUnit(lclBookingAc.getRatePerVolumeUnit());
                        lclBookingAcFromMap.setRatePerWeightUnit(lclBookingAc.getRatePerWeightUnit());
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedMeasure).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(minchg).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                if (pdfFormatLabel.equalsIgnoreCase("Yes")) {
                    formatLabelChargeBookingPDF(lclBookingAcFromMap, engmet);
                } else {
                    formatLabelCharge(lclCommodityList, lclBookingAcFromMap, engmet);
                }
                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAcFromMap);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public List<ManifestModel> getRolledUpChargesAccounting(List<ManifestModel> lclManifestList, boolean isManifest,
            User user, String buttonValue, String realPath, String shipmentType) throws Exception {
        Map<String, ManifestModel> manifestMap = new LinkedHashMap();
        //Map<String, String> mailMap = new LinkedHashMap();
        //LclPrintUtil lclPrintUtil = new LclPrintUtil();
        String key = "";
        //String keyMail = "";
        //String fileLocation = null;
        for (int i = 0; i < lclManifestList.size(); i++) {
            ManifestModel manifestModal = lclManifestList.get(i);
            manifestModal.setManifest(isManifest);
            key = manifestModal.getFileId().toString() + "_" + manifestModal.getChargeCode() + "_" + manifestModal.getBillToParty();
//            if (manifestModal.getShipmentType().equalsIgnoreCase("LCLI") && buttonValue != null && buttonValue.equalsIgnoreCase("CS")) {
//                keyMail = manifestModal.getFileId().toString();
//                if (!mailMap.containsKey(keyMail)) {
//                    fileLocation = lclPrintUtil.createImportBkgReport(keyMail, manifestModal.getDockReceipt(), DOCUMENTLCLIMPORTSARRIVALNOTICE, realPath,null);
//                    if (CommonUtils.isNotEmpty(manifestModal.getArrivalNoticeEmail())) {
//                        sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + manifestModal.getDockReceipt(), "Email", "Pending",
//                                manifestModal.getArrivalNoticeEmail(), manifestModal.getDockReceipt(), SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                "", "", user);
//                    }
//                    if (CommonUtils.isNotEmpty(manifestModal.getArrivalNoticeFax())) {
//                        String fax[] = manifestModal.getArrivalNoticeFax().split(",");
//                        for (int j = 0; j < fax.length; j++) {
//                            sendMailWithoutPrintConfig(fileLocation, SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                    DOCUMENTLCLIMPORTSARRIVALNOTICE + "For File#" + manifestModal.getDockReceipt(), "Fax", "Pending",
//                                    fax[j], manifestModal.getDockReceipt(), SCREENNAMELCLIMPORTBOOKINGREPORT,
//                                    "", "", user);
//                        }
//                    }
//                    mailMap.put(keyMail, keyMail);
//                }
//            }// Don't Remove this comment line..this may required in future
            if (!manifestMap.containsKey(key)) {
                if (!fileNumbers.toString().contains(manifestModal.getDockReceipt())) {
                    fileNumbers.append("'").append(manifestModal.getDockReceipt()).append("',");
                }
                manifestMap.put(key, manifestModal);
            } else if (manifestModal.getShipmentType().equalsIgnoreCase("LCLI")) {
                ManifestModel manifestModalFromMap = (ManifestModel) manifestMap.get(key);
                manifestModalFromMap.setAmount((manifestModalFromMap.getAmount() + manifestModal.getAmount()));
                manifestMap.put(key, manifestModalFromMap);
            } else if (!manifestModal.getRatePerUnitUOM().equalsIgnoreCase("FL") && !manifestModal.getRatePerUnitUOM().equalsIgnoreCase("PCT")) {
                ManifestModel manifestModalFromMap = (ManifestModel) manifestMap.get(key);
                if (manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRW") || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRV")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("FRM")) {
                    manifestModalFromMap.setAmount((manifestModalFromMap.getAmount() + manifestModal.getAmount())
                            - manifestModalFromMap.getAdjustmentAmount());
                } else if (manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("W") || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("V")
                        || manifestModalFromMap.getRatePerUnitUOM().equalsIgnoreCase("M")) {
                    manifestModalFromMap.setAmount(manifestModal.getAmount());
                }
                manifestMap.put(key, manifestModalFromMap);
            }
        }
        List<ManifestModel> rolledChargesList = new ArrayList(manifestMap.values());
        if (CommonUtils.isNotEmpty(fileNumbers) && fileNumbers.toString().contains(",")) {
            fileNumbers.deleteCharAt(fileNumbers.length() - 1);
        }
        return rolledChargesList;
    }

    public List getRolledUpChargesForBookingCorrections(List<LclBookingAc> lclBookingAcList) {
        Map chargesInfoMap = new LinkedHashMap();
        Double minchg = 0.0;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            if (!chargesInfoMap.containsKey(lclBookingAc.getArglMapping().getChargeCode())) {
                lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
                if (!lclBookingAc.isManualEntry() && lclBookingAc.getRatePerUnitUom() != null && lclBookingAc.getRateUom() != null && lclBookingAc.getArglMapping() != null
                        && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightImperial());
                            } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightImperial());
                            }
                            if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeImperial());
                            } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeImperial());
                            }
                        } else if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getActualWeightMetric());
                            } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                lclBookingAc.setTotalWeight(lclBookingAc.getLclBookingPiece().getBookedWeightMetric());
                            }
                            if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getActualVolumeMetric());
                            } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                lclBookingAc.setTotalMeasure(lclBookingAc.getLclBookingPiece().getBookedVolumeMetric());
                            }
                        }

                    }
                }
                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAc);
            } else {
                LclBookingAc lclBookingAcFromMap = (LclBookingAc) chargesInfoMap.get(lclBookingAc.getArglMapping().getChargeCode());
                if (!lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("FL") || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("TTBARR")) {
                    BigDecimal total = new BigDecimal(lclBookingAc.getArAmount().doubleValue() + lclBookingAcFromMap.getRolledupCharges().doubleValue());
                    total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
                    lclBookingAcFromMap.setRolledupCharges(total);
                }
                if (lclBookingAcFromMap.getRatePerUnitUom() != null && lclBookingAcFromMap.getRateUom() != null && lclBookingAcFromMap.getArglMapping() != null
                        && lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode() != null && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0032")
                        && !lclBookingAcFromMap.getArglMapping().getBlueScreenChargeCode().equals("0232")) {
                    if (lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("V") || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("W")
                            || lclBookingAcFromMap.getRatePerUnitUom().equalsIgnoreCase("M")) {
                        if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("I")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightImperial() != null && lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightImperial().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeImperial().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeImperial().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 100) * calculatedWeight;
                                }
                            }
                        } else if (lclBookingAcFromMap.getRateUom().equalsIgnoreCase("M")) {
                            if (lclBookingAc.getLclBookingPiece() != null) {
                                if (lclBookingAc.getLclBookingPiece().getActualWeightMetric() != null && lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualWeightMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight())) {
                                        lclBookingAcFromMap.setTotalWeight(new BigDecimal(lclBookingAcFromMap.getTotalWeight().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedWeightMetric().doubleValue()));
                                    }
                                }
                                if (lclBookingAc.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getActualVolumeMetric().doubleValue()));
                                    }
                                } else if (lclBookingAc.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                                    if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure())) {
                                        lclBookingAcFromMap.setTotalMeasure(new BigDecimal(lclBookingAcFromMap.getTotalMeasure().doubleValue()
                                                + lclBookingAc.getLclBookingPiece().getBookedVolumeMetric().doubleValue()));
                                    }
                                }
                                if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalWeight()) && !CommonUtils.isEmpty(lclBookingAc.getRatePerWeightUnit())) {
                                    calculatedWeight = (lclBookingAcFromMap.getTotalWeight().doubleValue() / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                                }
                            }
                        }
                        if (!CommonUtils.isEmpty(lclBookingAcFromMap.getTotalMeasure()) && !CommonUtils.isEmpty(lclBookingAc.getRatePerVolumeUnit())) {
                            calculatedMeasure = lclBookingAcFromMap.getTotalMeasure().doubleValue() * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                        }
                        minchg = lclBookingAc.getRateFlatMinimum().doubleValue();
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= minchg) {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(calculatedMeasure).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclBookingAcFromMap.setRolledupCharges(new BigDecimal(minchg).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
                chargesInfoMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAcFromMap);
            }
        }
        List rolledChargesList = new ArrayList(chargesInfoMap.values());
        return rolledChargesList;
    }

    public List getFormattedLabelCharges(List<LclBookingPiece> lclCommodityList, List<LclBookingAc> lclBookingAcList, String engmet, String pdfFormatLabel, HttpServletRequest request) {
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
            if (pdfFormatLabel.equalsIgnoreCase("Yes")) {
                formatLabelChargeBookingPDF(lclBookingAc, engmet);
            } else {
                formatLabelCharge(lclCommodityList, lclBookingAc, engmet);
            }
        }
        return lclBookingAcList;
    }

    public void formatLabelCharge(List<LclBookingPiece> lclCommodityList, LclBookingAc lclBookingAc, String engmet) {
        if (lclBookingAc.getRatePerUnitUom() != null && !lclBookingAc.getRatePerUnitUom().trim().equals("")) {
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                        String ratePerUnit = lclBookingAc.getRatePerWeightUnit() != null ? lclBookingAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclBookingAc.getRatePerWeightUnitDiv() != null ? lclBookingAc.getRatePerWeightUnitDiv().toString() : "";
                        lclBookingAc.setLabel2("(" + ratePerUnit + " PER " + ratePeUnitDiv + " CIF)");
                    } else {
                        lclBookingAc.setLabel2("$" + lclBookingAc.getArAmount().toString() + " FLAT RATE.");
                    }
                }
                if (lclBookingAc.getArglMapping().getChargeCode() != null && (lclBookingAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    String ratePerWeightUnit = lclBookingAc.getRatePerWeightUnit() != null ? lclBookingAc.getRatePerWeightUnit().toString() : "";
                    lclBookingAc.setLabel2("$" + ratePerWeightUnit + " PER BARREL.");
                }

            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclBookingAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclBookingAc.setLabel1("*** VOLUME ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclBookingAc.setLabel1("*** MINIMUM ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclBookingAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclBookingAc.getRatePerUnit().doubleValue() * 100);
                    lclBookingAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclBookingAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclBookingAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclBookingAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    boolean manualEntry = lclBookingAc.isManualEntry();
                    if (engmet.equalsIgnoreCase("E")) {
                        if (lclCommodityList.size() > 1 && lclBookingAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclBookingAc.setLabel2("MULTIPLE");
                        } else {
                            if (manualEntry && lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                                lclBookingAc.setLabel2("$" + convertToCft(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CFT, "
                                        + convertToLbs(lclBookingAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclBookingAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclBookingAc.getRateFlatMinimum()
                                        + " MINIMUM)");
                            } else {
                                lclBookingAc.setLabel2("$" + lclBookingAc.getRatePerVolumeUnit() + " CFT, " + lclBookingAc.getRatePerWeightUnit()
                                        + "/" + lclBookingAc.getRatePerWeightUnitDiv() + " LBS, ($" + lclBookingAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    } else if (engmet.equalsIgnoreCase("M")) {
                        if (lclCommodityList.size() > 1 && lclBookingAc.getArglMapping().getBlueScreenChargeCode().equalsIgnoreCase("0001")) {
                            lclBookingAc.setLabel2("MULTIPLE");
                        } else {
                            if (manualEntry && lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                                lclBookingAc.setLabel2("$" + convertToCbm(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CBM, "
                                        + convertToKgs(lclBookingAc.getRatePerWeightUnit().doubleValue()) + "/"
                                        + lclBookingAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclBookingAc.getRateFlatMinimum() + " MINIMUM)");
                            } else {
                                lclBookingAc.setLabel2("$" + lclBookingAc.getRatePerVolumeUnit() + " CBM, " + lclBookingAc.getRatePerWeightUnit()
                                        + "/" + lclBookingAc.getRatePerWeightUnitDiv() + " KGS, ($" + lclBookingAc.getRateFlatMinimum() + " MINIMUM)");
                            }
                        }
                    }
                }
                if (lclBookingAc.isManualEntry() && null != lclBookingAc.getRatePerWeightUnit() && null != lclBookingAc.getRatePerWeightUnit()
                        && lclBookingAc.getRatePerUnit().doubleValue() > 0.00 && lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00
                        && lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                     if (lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0351")) {
                     lclBookingAc.setLabel2(lclBookingAc.getLabel2());
                     }else{
                     lclBookingAc.setLabel2("$" + lclBookingAc.getRatePerUnit().toString() + " FLAT RATE + <br/>" + lclBookingAc.getLabel2());     
                     }
                }
            }

        }
    }

    public void calculateRates(RefTerminal refterminal, Ports ports, List lclBookingPiecesList, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;
            for (int i = 0; i < lclBookingPiecesList.size(); i++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(i);
                if (lclBookingPiece != null && !lclBookingPiece.isIsBarrel()) {
                    if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclBookingPiece.getActualVolumeMetric().doubleValue();
                    } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclBookingPiece.getBookedVolumeMetric().doubleValue();
                    }

                }
            }
            for (int i = 0; i < lclBookingPiecesList.size(); i++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(i);
                if (lclBookingPiece != null && !lclBookingPiece.isIsBarrel()) {
                    String commodityCode = new String();
                    if (fileNumberId != null && fileNumberId > 0 && null!=lclBookingPiece.getCommodityType()) {
                        commodityCode = lclBookingPiece.getCommodityType().getCode();
                    } else {
                        commodityCode = lclBookingPiece.getCommNo();
                    }
                    if (commodityCode == null) {
                        commodityCode = lclBookingPiece.getCommNo();
                    }
                    if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                        Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                        lclBookingPiece = this.changeCommodityRates(ofratecongObj, lclBookingPiece, engmet);
                        lclBookingPiecesList.set(i, lclBookingPiece);
                        if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                                && lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().isHighVolumeDiscount()) {
                            GenericCode genericcode = new GenericCodeDAO().findById(ports.getRegioncode().getId());
                            if (genericcode.getCode() != null && !genericcode.getCode().equals("01") && !genericcode.getCode().equals("02")
                                    && !genericcode.getCode().equals("03") && !genericcode.getCode().equals("09") && !genericcode.getCode().equals("10")
                                    && !genericcode.getCode().equals("11") && !genericcode.getCode().equals("12") && !genericcode.getCode().equals("13")) {
                                checkDiscount(ofratecongObj, lclBookingPiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                            }
                        }
                    }

                }
            }
        }

    }

    public void calculateRatesForBl(RefTerminal refterminal, Ports ports, List lclBlPiecesList, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;
            for (int i = 0; i < lclBlPiecesList.size(); i++) {
                LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(i);
                if (lclBlPiece != null && !lclBlPiece.isIsBarrel()) {
                    if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclBlPiece.getActualVolumeMetric().doubleValue();
                    } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclBlPiece.getBookedVolumeMetric().doubleValue();
                    }

                }
            }

            for (int i = 0; i < lclBlPiecesList.size(); i++) {
                LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(i);
                if (lclBlPiece != null && !lclBlPiece.isIsBarrel()) {
                    String commodityCode = new String();
                    if (fileNumberId != null && fileNumberId > 0) {
                        commodityCode = lclBlPiece.getCommodityType().getCode();
                    } else {
                        commodityCode = lclBlPiece.getCommNo();
                    }
                    if (CommonUtils.isEmpty(commodityCode)) {
                        commodityCode = lclBlPiece.getCommNo();
                    }
                    if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                        Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                        lclBlPiece = this.changeCommodityBlRates(ofratecongObj, lclBlPiece, engmet);
                        lclBlPiecesList.set(i, lclBlPiece);
                        if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                                && Integer.parseInt(ports.getEciportcode()) >= 640 && Integer.parseInt(ports.getEciportcode()) <= 899
                                && lclBlPiece.getCommodityType() != null && lclBlPiece.getCommodityType().isHighVolumeDiscount()) {
                            checkDiscountForBl(ofratecongObj, lclBlPiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                        }
                    }

                }
            }
        }

    }

    public void calculateRatesForPiece(RefTerminal refterminal, Ports ports, LclBookingPiece lclBookingPiece, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;

            if (lclBookingPiece != null) {
                if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclBookingPiece.getActualVolumeMetric().doubleValue();
                } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                }
                String commodityCode = new String();
                if (fileNumberId != null && fileNumberId > 0) {
                    commodityCode = lclBookingPiece.getCommodityType().getCode();
                } else {
                    commodityCode = lclBookingPiece.getCommNo();
                }
                if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                    Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                    lclBookingPiece = this.changeCommodityRates(ofratecongObj, lclBookingPiece, engmet);
                    if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                            && Integer.parseInt(ports.getEciportcode()) >= 640 && Integer.parseInt(ports.getEciportcode()) <= 899
                            && lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().isHighVolumeDiscount()) {
                        checkDiscount(ofratecongObj, lclBookingPiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                    }
                }

            }
        }
    }

    public void calculateRatesForBlPiece(RefTerminal refterminal, Ports ports, LclBlPiece lclBlPiece, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;

            if (lclBlPiece != null) {
                if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclBlPiece.getActualVolumeMetric().doubleValue();
                } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclBlPiece.getBookedVolumeMetric().doubleValue();
                }
                String commodityCode = new String();
                if (fileNumberId != null && fileNumberId > 0) {
                    commodityCode = lclBlPiece.getCommodityType().getCode();
                } else {
                    commodityCode = lclBlPiece.getCommNo();
                }
                if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                    Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                    lclBlPiece = this.changeCommodityBlRates(ofratecongObj, lclBlPiece, engmet);
                    if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                            && Integer.parseInt(ports.getEciportcode()) >= 640 && Integer.parseInt(ports.getEciportcode()) <= 899
                            && lclBlPiece.getCommodityType() != null && lclBlPiece.getCommodityType().isHighVolumeDiscount()) {
                        checkDiscountForBl(ofratecongObj, lclBlPiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                    }
                }

            }
        }
    }

    public void calculateQuoteRatesForPiece(RefTerminal refterminal, Ports ports, LclQuotePiece lclQuotePiece, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;

            if (lclQuotePiece != null) {
                if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclQuotePiece.getActualVolumeMetric().doubleValue();
                } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalcftcbm = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                }
                String commodityCode = new String();
                if (fileNumberId != null && fileNumberId > 0) {
                    commodityCode = lclQuotePiece.getCommodityType().getCode();
                } else {
                    commodityCode = lclQuotePiece.getCommNo();
                }
                if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                    Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                    lclQuotePiece = this.changeCommodityRatesForQuotes(ofratecongObj, lclQuotePiece, engmet);
                    if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                            && Integer.parseInt(ports.getEciportcode()) >= 640 && Integer.parseInt(ports.getEciportcode()) <= 899
                            && lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().isHighVolumeDiscount()) {
                        checkDiscountForQuotes(ofratecongObj, lclQuotePiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                    }
                }

            }
        }
    }

    /**
     * Giving the discount for the commodity
     *
     * @param commBean
     * @param visit
     * @return
     */
    public void checkDiscount(Object commodityOfRateObj[], LclBookingPiece lclBookingPiece, String commodityCode, String engmet,
            Double totalcftcbm, LCLRatesDAO lclratesdao) {
        NumberUtils numberUtils = new NumberUtils();
        if (engmet != null && !engmet.trim().equals("")) {
            if (engmet.equals("E")) {
                if (totalcftcbm >= 10) {
                    if (lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals("")) {
                        lclBookingPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBookingPiece.getPerCftCbm()) - 0.14));
                        highVolumeMessage = "High Vol Disc ($0.14) applies";
                    }
                    if (lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("") /*&& lbskgsrate != 0.0*/) {
                        lclBookingPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBookingPiece.getPerLbsKgs()) - 0.14));
                        highVolumeMessage = "High Vol Disc ($0.14) applies";
                    }
                }
            } else if (engmet.equals("M")) {
                if (totalcftcbm >= 10 /*&& cftcbmrate != 0.0*/) {
                    lclBookingPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBookingPiece.getPerCftCbm()) - 5));
                    lclBookingPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBookingPiece.getPerLbsKgs()) - 5));
                    highVolumeMessage = "High Vol Disc ($5) applies";
                }//end of totalcftcbm greater than 10 checking
            }// end of metric checking
        }//end of port null checking
    }
    //end of method

    public void checkDiscountForBl(Object commodityOfRateObj[], LclBlPiece lclBlPiece, String commodityCode, String engmet,
            Double totalcftcbm, LCLRatesDAO lclratesdao) {
        NumberUtils numberUtils = new NumberUtils();
        if (engmet != null && !engmet.trim().equals("")) {
            if (engmet.equals("E")) {
                if (totalcftcbm >= 10) {
                    if (lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                        lclBlPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBlPiece.getPerCftCbm()) - 0.14));
                    }

                    if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("") /*&& lbskgsrate != 0.0*/) {
                        lclBlPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBlPiece.getPerLbsKgs()) - 0.14));
                    }
                }
            } else if (engmet.equals("M")) {
                if (totalcftcbm >= 10 /*&& cftcbmrate != 0.0*/) {
                    lclBlPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBlPiece.getPerCftCbm()) - 5));
                    lclBlPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclBlPiece.getPerLbsKgs()) - 5));
                }//end of totalcftcbm greater than 10 checking
            }// end of metric checking
        }//end of port null checking
    }//end of method

    public void calculateRatesForQuotes(RefTerminal refterminal, Ports ports, List lclBlPiecesList, LCLRatesDAO lclratesdao, String engmet, Long fileNumberId) throws Exception {
        if (engmet != null && !engmet.trim().equals("")) {
            Double totalcftcbm = 0.0;
            for (int i = 0; i < lclBlPiecesList.size(); i++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclBlPiecesList.get(i);
                if (lclQuotePiece != null && !lclQuotePiece.isIsBarrel()) {
                    if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclQuotePiece.getActualVolumeMetric().doubleValue();
                    } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        totalcftcbm = totalcftcbm + lclQuotePiece.getBookedVolumeMetric().doubleValue();
                    }
                }
            }
            for (int i = 0; i < lclBlPiecesList.size(); i++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclBlPiecesList.get(i);
                if (lclQuotePiece != null && !lclQuotePiece.isIsBarrel()) {
                    String commodityCode = new String();
                    if (fileNumberId != null && fileNumberId > 0) {
                        commodityCode = lclQuotePiece.getCommodityType().getCode();
                    } else {
                        commodityCode = lclQuotePiece.getCommNo();
                    }
                    if (commodityCode == null) {
                        commodityCode = lclQuotePiece.getCommNo();
                    }
                    if (refterminal != null && ports != null && refterminal.getTrmnum() != null && ports.getEciportcode() != null) {
                        Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(refterminal.getTrmnum(), ports.getEciportcode(), commodityCode);
                        lclQuotePiece = this.changeCommodityRatesForQuotes(ofratecongObj, lclQuotePiece, engmet);
                        lclBlPiecesList.set(i, lclQuotePiece);
                        if (Integer.parseInt(refterminal.getTrmnum()) >= 37 && Integer.parseInt(refterminal.getTrmnum()) <= 57
                                && Integer.parseInt(ports.getEciportcode()) >= 640 && Integer.parseInt(ports.getEciportcode()) <= 899
                                && lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().isHighVolumeDiscount()) {
                            checkDiscountForQuotes(ofratecongObj, lclQuotePiece, commodityCode, engmet, totalcftcbm, lclratesdao);
                        }
                    }
                }
            }
        }
    }

    /**
     * Giving the discount for the commodity
     *
     * @param commBean
     * @param visit
     * @return
     */
    public void checkDiscountForQuotes(Object commodityOfRateObj[], LclQuotePiece lclQuotePiece, String commodityCode, String engmet,
            Double totalcftcbm, LCLRatesDAO lclratesdao) {
        NumberUtils numberUtils = new NumberUtils();
        if (engmet != null && !engmet.trim().equals("")) {
            if (engmet.equals("E")) {
                if (totalcftcbm >= 10) {
                    if (lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                        lclQuotePiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclQuotePiece.getPerCftCbm()) - 0.14));
                        highVolumeMessage = "High Vol Disc ($0.14) applies";
                    }
                    if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("") /*&& lbskgsrate != 0.0*/) {
                        lclQuotePiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclQuotePiece.getPerLbsKgs()) - 0.14));
                        highVolumeMessage = "High Vol Disc ($0.14) applies";
                    }
                }
            } else if (engmet.equals("M")) {
                if (totalcftcbm >= 10 /*&& cftcbmrate != 0.0*/) {
                    if (lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                        lclQuotePiece.setPerCftCbm(numberUtils.convertToTwoDecimal(Double.parseDouble(lclQuotePiece.getPerCftCbm()) - 5));
                        highVolumeMessage = "High Vol Disc ($5) applies";
                    }
                    if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")) {
                        lclQuotePiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(Double.parseDouble(lclQuotePiece.getPerLbsKgs()) - 5));
                        highVolumeMessage = "High Vol Disc ($5) applies";
                    }
                }//end of totalcftcbm greater than 10 checking
            }// end of metric checking
        }//end of port null checking
    }//end of method

    /**
     * Calculating the rate based on the ports and commodity
     *
     * @param commodityOfRateObj
     * @param commBean
     * @param ports
     * @return
     */
    public LclQuotePiece changeCommodityRatesForQuotes(Object commodityOfRateObj[], LclQuotePiece lclQuotePiece, String engmet) {
        if (engmet != null && !engmet.trim().equals("")) {
            if (commodityOfRateObj != null && commodityOfRateObj.length > 0) {
                NumberUtils numberUtils = new NumberUtils();
                if (engmet.equalsIgnoreCase("E")) {
                    if (commodityOfRateObj[0] != null && !commodityOfRateObj[0].toString().trim().equals("")
                            && !commodityOfRateObj[0].toString().equals("0.0")) {
                        Double engcft = Double.parseDouble(commodityOfRateObj[0].toString());
                        lclQuotePiece.setPerCftCbm(numberUtils.convertToTwoDecimal(engcft));
                    }
                    if (commodityOfRateObj[1] != null && !commodityOfRateObj[1].toString().trim().equals("")
                            && !commodityOfRateObj[1].toString().equals("0.0")) {
                        Double engwgt = Double.parseDouble(commodityOfRateObj[1].toString());
                        lclQuotePiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(engwgt));
                    }
                }
                if (engmet.equalsIgnoreCase("M")) {
                    if (commodityOfRateObj[2] != null && !commodityOfRateObj[2].toString().trim().equals("")
                            && !commodityOfRateObj[2].toString().equals("0.0")) {
                        Double metcft = Double.parseDouble(commodityOfRateObj[2].toString());
                        lclQuotePiece.setPerCftCbm(numberUtils.convertToTwoDecimal(metcft));
                    }
                    if (commodityOfRateObj[3] != null && !commodityOfRateObj[3].toString().trim().equals("")
                            && !commodityOfRateObj[3].toString().equals("0.0")) {
                        Double metwgt = Double.parseDouble(commodityOfRateObj[3].toString());
                        lclQuotePiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(metwgt));
                    }
                }
                if (commodityOfRateObj[4] != null && !commodityOfRateObj[4].toString().trim().equals("")
                        && !commodityOfRateObj[4].toString().equals("0.0")) {
                    Double ofminchg = Double.parseDouble(commodityOfRateObj[4].toString());
                    lclQuotePiece.setOfratemin(numberUtils.convertToTwoDecimal(ofminchg));
                }
            }
        }
        return lclQuotePiece;
    }

    /**
     * Calculating the rate based on the ports and commodity
     *
     * @param commodityOfRateObj
     * @param commBean
     * @param ports
     * @return
     */
    public LclBookingPiece changeCommodityRates(Object commodityOfRateObj[], LclBookingPiece lclBookingPiece, String engmet) {

        if (engmet != null && !engmet.trim().equals("")) {
            if (commodityOfRateObj != null && commodityOfRateObj.length > 0) {
                NumberUtils numberUtils = new NumberUtils();
                if (engmet.equalsIgnoreCase("E")) {
                    if (commodityOfRateObj[0] != null && !commodityOfRateObj[0].toString().trim().equals("")
                            && !commodityOfRateObj[0].toString().equals("0.0")) {
                        Double engcft = Double.parseDouble(commodityOfRateObj[0].toString());
                        lclBookingPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(engcft));
                    }
                    if (commodityOfRateObj[1] != null && !commodityOfRateObj[1].toString().trim().equals("")
                            && !commodityOfRateObj[1].toString().equals("0.0")) {
                        Double engwgt = Double.parseDouble(commodityOfRateObj[1].toString());
                        lclBookingPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(engwgt));
                    }
                }
                if (engmet.equalsIgnoreCase("M")) {
                    if (commodityOfRateObj[2] != null && !commodityOfRateObj[2].toString().trim().equals("")
                            && !commodityOfRateObj[2].toString().equals("0.0")) {
                        Double metcft = Double.parseDouble(commodityOfRateObj[2].toString());
                        lclBookingPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(metcft));
                    }
                    if (commodityOfRateObj[3] != null && !commodityOfRateObj[3].toString().trim().equals("")
                            && !commodityOfRateObj[3].toString().equals("0.0")) {
                        Double metwgt = Double.parseDouble(commodityOfRateObj[3].toString());
                        lclBookingPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(metwgt));
                    }
                }
                if (commodityOfRateObj[4] != null && !commodityOfRateObj[4].toString().trim().equals("")
                        && !commodityOfRateObj[4].toString().equals("0.0")) {
                    Double ofminchg = Double.parseDouble(commodityOfRateObj[4].toString());
                    lclBookingPiece.setOfratemin(numberUtils.convertToTwoDecimal(ofminchg));
                }
            }
        }
        return lclBookingPiece;
    }

    public LclBlPiece changeCommodityBlRates(Object commodityOfRateObj[], LclBlPiece lclBlPiece, String engmet) {

        if (engmet != null && !engmet.trim().equals("")) {
            if (commodityOfRateObj != null && commodityOfRateObj.length > 0) {
                NumberUtils numberUtils = new NumberUtils();
                if (engmet.equalsIgnoreCase("E")) {
                    if (commodityOfRateObj[0] != null && !commodityOfRateObj[0].toString().trim().equals("")
                            && !commodityOfRateObj[0].toString().equals("0.0")) {
                        Double engcft = Double.parseDouble(commodityOfRateObj[0].toString());
                        lclBlPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(engcft));
                    }
                    if (commodityOfRateObj[1] != null && !commodityOfRateObj[1].toString().trim().equals("")
                            && !commodityOfRateObj[1].toString().equals("0.0")) {
                        Double engwgt = Double.parseDouble(commodityOfRateObj[1].toString());
                        lclBlPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(engwgt));
                    }
                }
                if (engmet.equalsIgnoreCase("M")) {
                    if (commodityOfRateObj[2] != null && !commodityOfRateObj[2].toString().trim().equals("")
                            && !commodityOfRateObj[2].toString().equals("0.0")) {
                        Double metcft = Double.parseDouble(commodityOfRateObj[2].toString());
                        lclBlPiece.setPerCftCbm(numberUtils.convertToTwoDecimal(metcft));
                    }
                    if (commodityOfRateObj[3] != null && !commodityOfRateObj[3].toString().trim().equals("")
                            && !commodityOfRateObj[3].toString().equals("0.0")) {
                        Double metwgt = Double.parseDouble(commodityOfRateObj[3].toString());
                        lclBlPiece.setPerLbsKgs(numberUtils.convertToTwoDecimal(metwgt));
                    }
                }
                if (commodityOfRateObj[4] != null && !commodityOfRateObj[4].toString().trim().equals("")
                        && !commodityOfRateObj[4].toString().equals("0.0")) {
                    Double ofminchg = Double.parseDouble(commodityOfRateObj[4].toString());
                    lclBlPiece.setOfratemin(numberUtils.convertToTwoDecimal(ofminchg));
                }
            }
        }
        return lclBlPiece;
    }

    public void clearLCLSession(LclSession lclSession) {
        lclSession.setCommodityList(null);
        lclSession.setRoutingOptionsList(null);
        lclSession.setOverRiddedRemarks(null);
    }//end of method

    public Double distFrom(Double lat1, Double lng1, Double lat2, Double lng2) {
        Double earthRadius = 3958.75;
        Double dLat = Math.toRadians(lat2 - lat1);
        Double dLng = Math.toRadians(lng2 - lng1);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double dist = earthRadius * c;
        return dist;
    }

    public void updateLCLUnitSSStatus(Long unitSSId, String status, User user) throws Exception {
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        LclUnitSs lclunitss = lclunitssdao.findById(unitSSId);
        lclunitss.setStatus(status);
        lclunitss.setModifiedBy(user);
        lclunitss.setModifiedDatetime(new Date());
        lclunitssdao.update(lclunitss);
    }

//    public void updateLCLUnitManifestStatus(LclAddVoyageForm lclAddVoyageForm, LclSsHeader lclssheader, User user) throws Exception {
//        LclUnitSsManifestDAO lclunitssmanifestdao = new LclUnitSsManifestDAO();
//        LclUnitSsManifest lclunitssmanifest = lclunitssmanifestdao.getLclUnitSSManifestByHeader(lclAddVoyageForm.getUnitId(), lclssheader.getId());
//        Date d = new Date();
//        int userId = user.getUserId();
//        if (lclunitssmanifest == null) {
//            lclunitssmanifest = new LclUnitSsManifest();
//            lclunitssmanifest.setManifestedByUser(new User(userId));
//            lclunitssmanifest.setManifestedDatetime(d);
//            lclunitssmanifest.setEnteredByUser(user);
//            lclunitssmanifest.setModifiedByUser(user);
//            lclunitssmanifest.setEnteredDatetime(d);
//            lclunitssmanifest.setModifiedDatetime(d);
//            lclunitssmanifest.setCalculatedBlCount(0);
//            lclunitssmanifest.setCalculatedDrCount(0);
//            lclunitssmanifest.setCalculatedTotalPieces(0);
//            lclunitssmanifest.setCalculatedVolumeImperial(new BigDecimal(0.00));
//            lclunitssmanifest.setCalculatedVolumeMetric(new BigDecimal(0.00));
//            lclunitssmanifest.setCalculatedWeightImperial(new BigDecimal(0.00));
//            lclunitssmanifest.setCalculatedWeightMetric(new BigDecimal(0.00));
//            lclunitssmanifest.setLclUnit(new LclUnit(new Long(lclAddVoyageForm.getUnitId())));
//            lclunitssmanifest.setLclSsHeader(lclssheader);
//            lclunitssmanifestdao.saveOrUpdate(lclunitssmanifest);
//        }else{
//            lclunitssmanifest.setManifestedByUser(new User(userId));
//            lclunitssmanifest.setManifestedDatetime(d);
//            lclunitssmanifest.setModifiedByUser(user);
//            lclunitssmanifest.setModifiedDatetime(d);
//            lclunitssmanifestdao.saveOrUpdate(lclunitssmanifest);
//        }
//    }
    public String getDeleteVoyage(User user) throws Exception {
        String deleteVoyage = "N";
        if (user.getRole() != null && user.getRole().getRoleDesc() != null && !user.getRole().getRoleDesc().trim().equals("")) {
            RoleDuty roleDuty = new RoleDutyDAO().getByProperty("roleName", user.getRole().getRoleDesc());
            if (roleDuty != null && roleDuty.isDeleteVoyage() == true) {
                deleteVoyage = "Y";
            }
        }
        return deleteVoyage;
    }

    public Double calculateTotalWithoutInsuranceByBookingAcList(List<LclBookingAc> lclBookingAclist) {
        Double total = 0.0;
        if (lclBookingAclist != null && lclBookingAclist.size() > 0) {
            for (int i = 0; i < lclBookingAclist.size(); i++) {
                LclBookingAc lclBookingAc = lclBookingAclist.get(i);
                if (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null
                        && !lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                    total = total + lclBookingAc.getRolledupCharges().doubleValue();
                }
            }
        }
        return total;
    }

    public Double calculateTotalWithoutInsuranceByQuoteAcList(List<LclQuoteAc> lclQuoteAclist) {
        Double total = 0.0;
        if (lclQuoteAclist != null && lclQuoteAclist.size() > 0) {
            for (int i = 0; i < lclQuoteAclist.size(); i++) {
                LclQuoteAc lclQuoteAc = lclQuoteAclist.get(i);
                if (lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getBlueScreenChargeCode() != null
                        && !lclQuoteAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                    total = total + lclQuoteAc.getRolledupCharges().doubleValue();
                }
            }
        }
        return total;
    }

    public String calculateTotalByBookingAcList(List<LclBookingAc> lclBookingAclist) {
        Double total = 0.0;
        Double adjustmentTotal = 0.0;
        if (lclBookingAclist != null && lclBookingAclist.size() > 0) {
            for (int i = 0; i < lclBookingAclist.size(); i++) {
                LclBookingAc lclBookingAc = lclBookingAclist.get(i);
                total = total + lclBookingAc.getRolledupCharges().doubleValue();
                if (lclBookingAc.getAdjustmentAmount() != null) {
                    adjustmentTotal = adjustmentTotal + lclBookingAc.getAdjustmentAmount().doubleValue();
                }
            }
        }
        return NumberUtils.convertToTwoDecimal(total + adjustmentTotal);
    }

    public void setWeighMeasureForBooking(HttpServletRequest request, List<LclBookingPiece> lclCommodityList, Ports ports) {
        BigDecimal weightValues = new BigDecimal(0.00);
        BigDecimal volumeValues = new BigDecimal(0.00);
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            for (LclBookingPiece lclBookingPiece : lclCommodityList) {
                if (ports != null && ports.getEngmet() != null) {
                    if (ports.getEngmet().equals("E")) {
                        if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightValues = lclBookingPiece.getActualWeightImperial().add(weightValues);
                            request.setAttribute("weightValues", weightValues + " LBS");
                        } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightValues = lclBookingPiece.getBookedWeightImperial().add(weightValues);
                            request.setAttribute("weightValues", weightValues + " LBS");
                        }
                        if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            volumeValues = lclBookingPiece.getActualVolumeImperial().add(volumeValues);
                            request.setAttribute("cubeValues", volumeValues + " CFT");
                        } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            volumeValues = lclBookingPiece.getBookedVolumeImperial().add(volumeValues);
                            request.setAttribute("cubeValues", volumeValues + " CFT");
                        }
                    } else if (ports.getEngmet().equals("M")) {
                        if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightValues = lclBookingPiece.getActualWeightMetric().add(weightValues);
                            request.setAttribute("weightValues", weightValues + " KGS");
                        } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightValues = lclBookingPiece.getBookedWeightMetric().add(weightValues);
                            request.setAttribute("weightValues", weightValues + " KGS");
                        }
                        if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            volumeValues = lclBookingPiece.getActualVolumeMetric().add(volumeValues);
                            request.setAttribute("cubeValues", volumeValues + " CBM");
                        } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            volumeValues = lclBookingPiece.getBookedVolumeMetric().add(volumeValues);
                            request.setAttribute("cubeValues", volumeValues + " CBM");
                        }
                    }
                }
                if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null) {
                    request.setAttribute("commodityNumber", lclBookingPiece.getCommodityType().getCode());
                }
            }
        }
    }

    public void setWeighMeasureForImportBooking(HttpServletRequest request, List<LclBookingPiece> lclCommodityList, Ports ports) throws Exception {
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                request.setAttribute("weightValues", lclBookingPiece.getActualWeightMetric() + " KGS");
            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                request.setAttribute("weightValues", lclBookingPiece.getBookedWeightMetric() + " KGS");
            }
            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                request.setAttribute("cubeValues", lclBookingPiece.getActualVolumeMetric() + " CBM");
            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                request.setAttribute("cubeValues", lclBookingPiece.getBookedVolumeMetric() + " CBM");
            }
            if (lclBookingPiece.getCommodityType() != null && lclBookingPiece.getCommodityType().getCode() != null) {
                request.setAttribute("commodityNumber", lclBookingPiece.getCommodityType().getCode());
            }
        }
    }

    public void setRolledUpChargesForBooking(List<LclBookingAc> chargeList, HttpServletRequest request, Long longFileId, LclCostChargeDAO lclCostChargeDAO,
            List<LclBookingPiece> lclCommodityList, String billingType, String engmet, String pdfFormatLabel) throws Exception {
        if (chargeList == null || chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        } else if (CommonUtils.isNotEmpty(lclCommodityList)) {
            if (lclCommodityList.size() == 1) {
                lclBookingAcList = getFormattedLabelCharges(lclCommodityList, chargeList, engmet, pdfFormatLabel, request);
                request.setAttribute("chargeList", lclBookingAcList);
                if (longFileId != null && longFileId > 0) {
                    request.setAttribute("totalCharges", lclCostChargeDAO.getTotalLclCostByFileNumber(longFileId));
                    request.setAttribute("totalCostCharges", lclCostChargeDAO.getTotalLclCostAmountByFileNumber(longFileId));
                }
            } else {
                lclBookingAcList = getRolledUpCharges(lclCommodityList, chargeList, engmet, pdfFormatLabel);
                request.setAttribute("chargeList", lclBookingAcList);
                request.setAttribute("totalCharges", calculateTotalByBookingAcList(lclBookingAcList));
                if (longFileId != null && longFileId > 0) {
                    request.setAttribute("totalCostCharges", lclCostChargeDAO.getTotalLclCostAmountByFileNumber(longFileId));
                }
            }
        } else {
            lclBookingAcList = getFormattedLabelCharges(new ArrayList<LclBookingPiece>(), chargeList, engmet, pdfFormatLabel, request);
            request.setAttribute("chargeList", lclBookingAcList);
            if (longFileId != null && longFileId > 0) {
                request.setAttribute("totalCharges", lclCostChargeDAO.getTotalLclCostByFileNumber(longFileId));
                request.setAttribute("totalCostCharges", lclCostChargeDAO.getTotalLclCostAmountByFileNumber(longFileId));
            }
        }
        if (billingType != null) {
            if (billingType.equalsIgnoreCase("P")) {
                request.setAttribute("billingMethod", "PREPAID");
            } else if (billingType.equalsIgnoreCase("C")) {
                request.setAttribute("billingMethod", "COLLECT");
            } else {
                request.setAttribute("billingMethod", "BOTH");
            }
        }
    }

    public void formatLabelChargeBookingPDF(LclBookingAc lclBookingAc, String engmet) {
        if (lclBookingAc.getRatePerUnitUom() != null && !lclBookingAc.getRatePerUnitUom().trim().equals("")) {
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FL")) {
                if (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null) {
                    if (lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("0006")) {
                        String ratePerUnit = lclBookingAc.getRatePerWeightUnit() != null ? lclBookingAc.getRatePerWeightUnit().toString() : "";
                        String ratePeUnitDiv = lclBookingAc.getRatePerWeightUnitDiv() != null ? lclBookingAc.getRatePerWeightUnitDiv().toString() : "";
                        lclBookingAc.setLabel2("(" + ratePerUnit + " PER "
                                + ratePeUnitDiv + " CIF)");
                    } else {
                        lclBookingAc.setLabel2("FLAT RATE:  " + "$" + lclBookingAc.getArAmount().toString());
                    }
                }
                if (lclBookingAc.getArglMapping().getChargeCode() != null && (lclBookingAc.getArglMapping().getChargeCode().equals("OFBARR")
                        || lclBookingAc.getArglMapping().getChargeCode().equals("TTBARR"))) {
                    lclBookingAc.setLabel2("PER BARREL:  " + "$" + lclBookingAc.getRatePerWeightUnit());
                }

            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W")) {
                lclBookingAc.setLabel1("*** TO WEIGHT ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")) {
                lclBookingAc.setLabel1("*** VOLUME ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M")) {
                lclBookingAc.setLabel1("*** MINIMUM ***");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                if (lclBookingAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclBookingAc.getRatePerUnit().doubleValue() * 100);
                    lclBookingAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                }
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                lclBookingAc.setLabel1("* O/F - TO WEIGHT *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                lclBookingAc.setLabel1("* O/F - VOLUME *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                lclBookingAc.setLabel1("* O/F - MINIMUM *");
            }
            if (lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("V")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRW")
                    || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclBookingAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                if (engmet != null) {
                    if (engmet.equalsIgnoreCase("E")) {
                        lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CFT " + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue())
                                + "/" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnitDiv().doubleValue()) + " LBS ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " Min)");
                    } else if (engmet.equalsIgnoreCase("M")) {
                        lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CBM " + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue())
                                + "/" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnitDiv().doubleValue()) + " KGS ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " Min)");
                    }
                }
            }
        }
    }

    public void getFormattedConsolidatedList(HttpServletRequest request, Long longFileId) throws Exception {
        if (CommonUtils.isNotEmpty(longFileId)) {
            List<LclBookingModel> consolidateList = null;
            LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
            List consolidateFileList = consolidateDAO.getConsolidatesFiles(longFileId);
            if (null != consolidateFileList && !consolidateFileList.isEmpty()) {
                consolidateList = consolidateDAO.getConsolidateBkgList(consolidateFileList);
            }
            User loginUser = (User) request.getSession().getAttribute("loginuser");
            boolean check = new RoleDutyDAO().getRoleDetails("enable_batch_hs_code", loginUser.getRole().getRoleId());
            if (consolidateList != null && consolidateList.size() > 0 && check) {
                request.setAttribute("enableBatchHsCode", "true");
            }
           
            boolean flag = new RoleDutyDAO().getRoleDetails("enable_batch_hot_code", loginUser.getRole().getRoleId());
            if(consolidateList != null && consolidateList.size() > 0 && flag){
               request.setAttribute("enableBatchHotCode", "true"); 
            }
            request.setAttribute("consolidateList", consolidateList);
            request.setAttribute("consolidateBlId", new LCLBlDAO().findConsolidateBl(longFileId));
        }
    }

    public BigDecimal convertToLbs(double val) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        String convertedLbs = df2.format(val * 2.2046);
        return new BigDecimal(convertedLbs);
    }

    public BigDecimal convertToKgs(double val) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        String convertedKgs = df2.format(val / 2.2046);
        return new BigDecimal(convertedKgs);
    }

    public BigDecimal convertToKgsForImports(double val) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        String convertedKgs = df2.format(val / 2.2046);
        Double test = Double.parseDouble(convertedKgs) / 10;
        BigDecimal myBigDecimal = new BigDecimal(test).setScale(2, RoundingMode.DOWN);
//        myBigDecimal.setScale(2, RoundingMode.HALF_UP);
        return myBigDecimal;
    }

    public BigDecimal convertToCbm(double val) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        String convertedCbm = df2.format(val / 35.314);
        return new BigDecimal(convertedCbm);
    }

    public BigDecimal convertToCft(double val) {
        DecimalFormat df2 = new DecimalFormat("########0.00");
        String convertedCft = df2.format(val * 35.314);
        return new BigDecimal(convertedCft);
    }

    public int getCharOccurrence(String str) {
        int count = 0;
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (("" + ch[i]).equalsIgnoreCase(",")) {
                count++;
            }
        }
        return count;
    }

    public void setRelayTTDBD(HttpServletRequest request, Integer pol, Integer pod) throws Exception {
        LclRelayDAO lclRelayDAO = new LclRelayDAO();
        Object[] lclRelayObject = lclRelayDAO.getRelayTTDBD(pol, pod);
        if (lclRelayObject != null && lclRelayObject.length > 0) {
            if (lclRelayObject[0] != null && !lclRelayObject[0].toString().trim().equals("")) {
                request.setAttribute("polPodTT", lclRelayObject[0].toString());
            }
            if (lclRelayObject[1] != null && !lclRelayObject[1].toString().trim().equals("")) {
                request.setAttribute("co_dbd", lclRelayObject[1].toString());
            }
            if (lclRelayObject[2] != null && !lclRelayObject[2].toString().trim().equals("")) {
                request.setAttribute("co_tod", lclRelayObject[2].toString());
            }
        }

    }

    public void insertLCLRemarks(Long fileId, String remarks, String type, User user) throws Exception {
        LclRemarks lclRemarks = new LclRemarks();
        Date d = new Date();
        lclRemarks.setLclFileNumber(new LclFileNumber(fileId));
        lclRemarks.setEnteredBy(user);
        lclRemarks.setModifiedBy(user);
        lclRemarks.setModifiedDatetime(d);
        lclRemarks.setEnteredDatetime(d);
        lclRemarks.setRemarks(remarks);
        lclRemarks.setType(type);
        new LclRemarksDAO().save(lclRemarks);
    }

    public void insertLCLCorrectionRemarks(String type, Long fileId, String blNo, String text, User user) throws Exception {
        StringBuilder remarks = new StringBuilder(CommonConstants.getEventMap().get(GENERIC_EVENT_CODE_CORRECTION_NOTES));
        remarks.append(" ");
        remarks.append(blNo);
        remarks.append(" ");
        remarks.append(text);
        insertLCLRemarks(fileId, remarks.toString(), type, user);
    }

    public boolean insertLCLRemark(String fileNumber, String remarks, String type, User user) throws Exception {
        try {
            LclRemarks lclRemarks = new LclRemarks();
            Date d = new Date();
            lclRemarks.setLclFileNumber(findLclFileNumber(fileNumber));
            lclRemarks.setModifiedDatetime(d);
            lclRemarks.setEnteredDatetime(d);
            lclRemarks.setRemarks(remarks);
            lclRemarks.setType(type);

            lclRemarks.setEnteredBy(user);
            lclRemarks.setModifiedBy(user);
            if (lclRemarks.getLclFileNumber() != null) {
                new LclRemarksDAO().saveLclremarkCts(lclRemarks);
                return true;
            } else {
                return false;

            }
        } catch (Exception e) {
            return false;
        }

    }

    public LclFileNumber findLclFileNumber(String fileNumber) throws Exception {
        try {
            Criteria criteria = getCurrentSession().createCriteria(LclFileNumber.class);
            criteria.add(Restrictions.eq("fileNumber", fileNumber.trim()));
            return (LclFileNumber) criteria.setMaxResults(1).uniqueResult();
        } catch (Exception e) {
            org.hibernate.Session session = (org.hibernate.Session) getSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(LclFileNumber.class);
            criteria.add(Restrictions.eq("fileNumber", fileNumber.trim()));
            LclFileNumber lfn = (LclFileNumber) criteria.setMaxResults(1).uniqueResult();
            session.getTransaction().commit();
            return lfn;
        }
    }

    public void insertLCLConsolidation(Long ida, Long idb, User user,
            LclConsolidateDAO lclConsolidateDAO, Date d) throws Exception {
        LclConsolidate lclConsolidate = new LclConsolidate();
        lclConsolidate.setLclFileNumberA(new LclFileNumber(ida));
        lclConsolidate.setLclFileNumberB(new LclFileNumber(idb));
        lclConsolidate.setEnteredBy(user);
        lclConsolidate.setModifiedBy(user);
        lclConsolidate.setEnteredDatetime(d);
        lclConsolidate.setModifiedDatetime(d);
        lclConsolidateDAO.save(lclConsolidate);
    }

    public LclBookingAc saveBookingAc(Long fileId, GlMapping apGlmapping, GlMapping arGlmapping, BigDecimal apAmount, BigDecimal arAmount, String billto,
            String unitMeasure, BigDecimal flatRateMin, BigDecimal adjAmt, String invoiceNo, String vendorNo, User user) throws Exception {
        LclBookingAc bookingAc = new LclBookingAc();
        LclFileNumberDAO fileNoDao = new LclFileNumberDAO();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        LclCostChargeDAO bookingAcDao = new LclCostChargeDAO();
        Date now = new Date();
        bookingAc.setLclFileNumber(fileNoDao.findById(fileId));
        bookingAc.setManualEntry(true);
        bookingAc.setTransDatetime(now);
        bookingAc.setApglMapping(apGlmapping);
        bookingAc.setArglMapping(arGlmapping);
        bookingAc.setApBillToParty(billto);
        bookingAc.setRatePerUnitUom(unitMeasure);
        bookingAc.setRateFlatMinimum(flatRateMin);
        bookingAc.setAdjustmentAmount(adjAmt);
        bookingAc.setApAmount(apAmount);
        bookingAc.setCostFlatrateAmount(apAmount);
        bookingAc.setArAmount(arAmount);
        bookingAc.setPostedDateTime(now);
        bookingAc.setInvoiceNumber(invoiceNo);
        bookingAc.setSupAcct(tpDao.findById(vendorNo));
        bookingAc.setPrintOnBl(true);
        bookingAc.setBundleIntoOf(false);
        bookingAc.setRelsToInv(false);
        bookingAc.setEnteredDatetime(now);
        bookingAc.setModifiedDatetime(now);
        bookingAc.setEnteredBy(user);
        bookingAc.setModifiedBy(user);
        bookingAcDao.save(bookingAc);
        return bookingAc;
    }

    public LclSsAc saveSsAc(Long unitssId, GlMapping apGlmapping, GlMapping arGlmapping, BigDecimal apAmount, BigDecimal arAmount,
            String invoiceNo, String vendorNo, User user) throws Exception {
        LclSsAc ssAc = new LclSsAc();
        LclUnitSsDAO unitssDao = new LclUnitSsDAO();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        Date now = new Date();
        LclUnitSs unitss = unitssDao.findById(unitssId);
        ssAc.setLclUnitSs(unitss);
        ssAc.setLclSsHeader(unitss.getLclSsHeader());
        ssAc.setManualEntry(true);
        ssAc.setTransDatetime(now);
        ssAc.setApGlMappingId(apGlmapping);
        ssAc.setArGlMappingId(arGlmapping);
        ssAc.setApAmount(apAmount);
        ssAc.setArAmount(arAmount);
        ssAc.setTransDatetime(now);
        ssAc.setApTransType(TRANSACTION_TYPE_ACCRUALS);
        ssAc.setApAcctNo(tpDao.findById(vendorNo));
        ssAc.setEnteredDatetime(now);
        ssAc.setModifiedDatetime(now);
        ssAc.setEnteredByUserId(user);
        ssAc.setModifiedByUserId(user);
        if (CommonUtils.isNotEmpty(invoiceNo)) {
            ssAc.setApReferenceNo(invoiceNo.toUpperCase());
        } else {
            ssAc.setApReferenceNo("");
        }
        unitssDao.save(ssAc);
        return ssAc;
    }

    public void insertLclBookingAc(LclBookingAc lclBookingAc, Long fileId, GlMapping glmapping, BigDecimal pickupCharge, User user,
            HttpServletRequest request, String ratePerUnitUom, String rateUom, Boolean manualEntry) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        lclBookingAc.setLclFileNumber(new LclFileNumber(fileId));
        lclBookingAc.setArAmount(pickupCharge);
        lclBookingAc.setRatePerUnit(pickupCharge);
        lclBookingAc.setArglMapping(glmapping);
        lclBookingAc.setManualEntry(manualEntry);
        lclBookingAc.setTransDatetime(new Date());
        lclBookingAc.setRatePerUnitUom(ratePerUnitUom);
        lclBookingAc.setRateUom(rateUom);
        lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
        lclBookingAc.setEnteredBy(user);
        lclBookingAc.setModifiedBy(user);
        lclBookingAc.setEnteredDatetime(new Date());
        lclBookingAc.setModifiedDatetime(new Date());
        lclBookingAc.setBundleIntoOf(false);
        lclBookingAc.setPrintOnBl(true);
        lclCostChargeDAO.saveOrUpdate(lclBookingAc);
    }

    public void insertLclQuoteAc(LclQuoteAc lclQuoteAc, Long fileId, GlMapping glmapping, BigDecimal pickupCharge, User user, HttpServletRequest request, String ratePerUnitUom, String rateUom, Boolean manualEntry) throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        lclQuoteAc.setLclFileNumber(new LclFileNumber(fileId));
        lclQuoteAc.setArAmount(pickupCharge);
        lclQuoteAc.setRatePerUnit(pickupCharge);
        lclQuoteAc.setArglMapping(glmapping);
        lclQuoteAc.setManualEntry(manualEntry);
        lclQuoteAc.setTransDatetime(new Date());
        lclQuoteAc.setRatePerUnitUom(ratePerUnitUom);
        lclQuoteAc.setRateUom(rateUom);
        lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
        lclQuoteAc.setEnteredBy(user);
        lclQuoteAc.setModifiedBy(user);
        lclQuoteAc.setEnteredDatetime(new Date());
        lclQuoteAc.setModifiedDatetime(new Date());
        lclQuoteAc.setBundleIntoOf(false);
        lclQuoteAc.setPrintOnBl(true);
        lclQuoteAcDAO.saveOrUpdate(lclQuoteAc);
    }

    //Imports Rates for Booking
    public void setImportRolledUpChargesForBooking(List<LclBookingAc> chargeList, HttpServletRequest request, Long longFileId, LclCostChargeDAO lclCostChargeDAO,
            List<LclBookingPiece> lclCommodityList, String billingType, String engmet, String vendorNo) throws Exception {
        TransactionDAO transactionDAO = new TransactionDAO();
        String totalArBalanceAmount = "";
        if (CommonUtils.isNotEmpty(lclCommodityList)) {
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            totalArBalanceAmount = transactionDAO.getLclARBalance(lclBookingPiece.getLclFileNumber().getFileNumber(), lclBookingPiece.getLclFileNumber().getFileNumber());
            request.setAttribute("totalArBalanceAmount", totalArBalanceAmount);
        }
        if (chargeList.isEmpty()) {
            request.setAttribute("rateErrorMessage", "No Rates Found.");
        } else {
            chargeList = getImportFormattedLabelCharges(chargeList);
        }
        request.setAttribute("chargeList", chargeList);
        if (longFileId != null && longFileId > 0) {
            String balanceAmt = new LclBookingAcTaDAO().getPaymentBalanceAmt(longFileId);
            BigDecimal paymentTotal = lclCostChargeDAO.getPaymentTotalByFileNumber(longFileId);
            BigDecimal totalCollectChargesAmt = lclCostChargeDAO.getTotalAmountByCollect(longFileId, "A");
            BigDecimal totalPrepaidChargesAmt = lclCostChargeDAO.getTotalAmountByPrepaid(longFileId, "A");
            Boolean isPaymentType = new LCLBookingAcTransDAO().getPaymentType(longFileId);
            request.setAttribute("totalCollectChargesAmt", totalCollectChargesAmt);
            request.setAttribute("totalPrepaidChargesAmt", totalPrepaidChargesAmt);
            request.setAttribute("totalCollectChargesAmtC", lclCostChargeDAO.getTotalAmountByCollectAll(longFileId, "C"));
            request.setAttribute("totalCollectChargesAmtT", lclCostChargeDAO.getTotalAmountByCollectAll(longFileId, "T"));
            request.setAttribute("totalCollectChargesAmtA", lclCostChargeDAO.getTotalAmountByCollectAll(longFileId, "A"));
            request.setAttribute("totalCollectChargesAmtN", lclCostChargeDAO.getTotalAmountByCollectAll(longFileId, "N"));
            request.setAttribute("totalCollectChargesAmtW", lclCostChargeDAO.getTotalAmountByCollectAll(longFileId, "W"));
            request.setAttribute("totalCharges", lclCostChargeDAO.getTotalLclCostByFileNumber(longFileId));
            request.setAttribute("totalCostAmt", lclCostChargeDAO.getTotalCostAmt(longFileId));
            request.setAttribute("balanceAmt", balanceAmt);
            String paymentStatus = "button-style1";
            if (isPaymentType) {
                paymentStatus = "0.00".equalsIgnoreCase(totalArBalanceAmount) ? "button-style1green" : "button-style1red";
            } else if (balanceAmt != null && CommonUtils.isEmpty(Double.parseDouble(balanceAmt.replaceAll(",", "")))) {
                paymentStatus = "button-style1green";
            } else if (balanceAmt != null && paymentTotal != null && (paymentTotal.doubleValue() - Double.parseDouble(balanceAmt.replaceAll(",", "")) > 0
                    || Double.parseDouble(balanceAmt.replaceAll(",", "")) - paymentTotal.doubleValue() > 0)) {
                paymentStatus = "button-style1orange";
            }
            request.setAttribute("paymentStatus", paymentStatus);
        }
    }

    public List getImportFormattedLabelCharges(List<LclBookingAc> lclBookingAcList) {
        int count = 0;
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            if (null != lclBookingAcList.get(i).getArglMapping() && lclBookingAcList.get(i).getArglMapping().getChargeCode().equals("OFIMP")
                    && !lclBookingAcList.get(i).isManualEntry()) {
                Collections.swap(lclBookingAcList, 0, count);
            } else {
                count++;
            }
            lclBookingAc.setRolledupCharges(lclBookingAc.getArAmount());
            importFormatLabelCharge(lclBookingAc);
        }
        return lclBookingAcList;
    }

    public void importFormatLabelCharge(LclBookingAc lclBookingAc) {
        if (lclBookingAc.getRatePerUnitUom() != null && !lclBookingAc.getRatePerUnitUom().trim().equals("")) {
            if (RATE_UNIT_PER_UOM_WEIGHT.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                lclBookingAc.setLabel1("*** TO WEIGHT ***");
            } else if (RATE_UNIT_PER_UOM_VOLUME.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                lclBookingAc.setLabel1("*** VOLUME ***");
            } else if (RATE_UNIT_PER_UOM_MIN.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                lclBookingAc.setLabel1("*** MINIMUM ***");
            } else if (RATE_UNIT_PER_UOM_MAX.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                lclBookingAc.setLabel1("*** MAXIMUM ***");
            }
            if (lclBookingAc.getArglMapping() != null && lclBookingAc.getArglMapping().getBlueScreenChargeCode() != null) {
                if (RATE_UNIT_PER_UOM_FL.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                    lclBookingAc.setLabel2("$" + lclBookingAc.getArAmount().doubleValue() + " FLAT RATE.");
                } else if (RATE_UNIT_PER_UOM_PCT.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom()) && lclBookingAc.getRatePerUnit() != null) {
                    int ratePercentage = (int) (lclBookingAc.getRatePerUnit().doubleValue() * 100);
                    lclBookingAc.setLabel2(String.valueOf(ratePercentage) + " PERCENT OF THE B/L.");
                } else if (lclBookingAc.getArglMapping().getBlueScreenChargeCode().equals("600") && lclBookingAc.getRatePerVolumeUnit().toString().equals("12.00")
                        && lclBookingAc.getRatePerWeightUnit().toString().equals("0.00") && RATE_UNIT_PER_UOM_VOLUME.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())) {
                    lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CBM,0.00/1000.00 KGS, ($0.00 MINIMUM/0.00 MAXIMUM)");
                } else if (!RATE_UNIT_PER_UOM_FL.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom()) && !RATE_UNIT_PER_UOM_PCT.equalsIgnoreCase(lclBookingAc.getRatePerUnitUom())
                        && RATE_UOM_M.equalsIgnoreCase(lclBookingAc.getRateUom())) {
                    lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CBM, "
                            + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()) + "/1000.00 KGS,"
                            + " ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " MINIMUM/"
                            + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMaximum() != null ? lclBookingAc.getRateFlatMaximum().doubleValue() : 0.00) + " MAXIMUM)");
                } else {
                    lclBookingAc.setLabel2("$" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) + " CFT, "
                            + NumberUtils.convertToTwoDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()) + "/100.00 LBS, "
                            + " ($" + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) + " MINIMUM/"
                            + NumberUtils.convertToTwoDecimal(lclBookingAc.getRateFlatMaximum() != null ? lclBookingAc.getRateFlatMaximum().doubleValue() : 0.00) + " MAXIMUM)");
                }
            }
        }
    }

    public LclSsDetail getPickedOnVoyageDetails(Long fileId) throws Exception {
        List<LclBookingPiece> lclBkPceList = null;
        LclBookingPiece lclBkPce = new LclBookingPiece();
        LclBookingPieceDAO lclBkPcedao = new LclBookingPieceDAO();
        List<LclBookingPieceUnit> lclBkPceUtList = null;
        LclBookingPieceUnit lclBkPcUt = new LclBookingPieceUnit();
        LclBookingPieceUnitDAO lclBkPceUtdao = new LclBookingPieceUnitDAO();
        LclUnitSs lclUnitSs = new LclUnitSs();
        LclUnitSsDAO lclUnitSSdao = new LclUnitSsDAO();
        List<LclSsDetail> lclSsDetailList = null;
        LclSsDetailDAO lclSsDetaildao = new LclSsDetailDAO();
        lclBkPceList = lclBkPcedao.findByProperty("lclFileNumber.id", fileId);
        if (!lclBkPceList.isEmpty()) {
            lclBkPce = (LclBookingPiece) lclBkPceList.get(0);
            lclBkPceUtList = lclBkPceUtdao.findByProperty("lclBookingPiece.id", lclBkPce.getId());
            if (!lclBkPceUtList.isEmpty()) {
                lclBkPcUt = (LclBookingPieceUnit) lclBkPceUtList.get(0);
                if (null != lclBkPcUt && lclBkPcUt.getLclUnitSs() != null) {
                    lclSsDetailList = lclSsDetaildao.findByProperty("lclSsHeader", lclBkPcUt.getLclUnitSs().getLclSsHeader());
                    if (!lclSsDetailList.isEmpty()) {
                        for (int i = 0; i < lclSsDetailList.size(); i++) {
                            if (lclSsDetailList.get(i).getTransMode().equalsIgnoreCase("V")) {
                                return (LclSsDetail) lclSsDetailList.get(i);
                            }
                        }

                    }
                }
            }
        }
        return null;
    }

    public List<LCLCorrectionChargeBean> getFormattedCorrectionChargesImports(List<LclBookingAc> lclBookingAcList, Long correctionId, Long fileId,
            Integer bookingPieceCount, LCLCorrectionNoticeBean lclCorrectionNoticeBean, String viewMode, String billTo) throws Exception {
        List<LCLCorrectionChargeBean> lclCorrectionChargeList = new ArrayList<LCLCorrectionChargeBean>();
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        Map<String, String> chargesMap = new LinkedHashMap();
        Set<String> newIds = new HashSet();
        boolean found = false, addCorrectionChargeBean = false;
        String billToCode = "";
        if (CommonUtils.isEmpty(viewMode)) {
            addCorrectionChargeBean = true;
        }
        if (bookingPieceCount > 1) {
            lclBookingAcList = getRolledUpChargesForBookingCorrections(lclBookingAcList);
        }
        for (int i = 0; i < lclBookingAcList.size(); i++) {
            LclBookingAc lclBookingAc = lclBookingAcList.get(i);
            String newAmount = NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue());
            if (CommonUtils.isNotEmpty(lclBookingAc.getArBillToParty())) {
                if (viewMode != null && viewMode.equalsIgnoreCase("view")) {
                    BigInteger count = lclCorrectionChargeDAO.getNewChargeCount(correctionId, lclBookingAc.getArglMapping().getId(), "0.00", newAmount, fileId, 0);
                    if (count.intValue() == 0) {
                        addCorrectionChargeBean = true;
                    }
                }
                if (addCorrectionChargeBean) {
                    String addedAmount = NumberUtils.convertToTwoDecimal(lclBookingAc.getArAmount().doubleValue() + lclBookingAc.getAdjustmentAmount().doubleValue());
                    String key = lclBookingAc.getArglMapping().getId() + "_" + String.valueOf(addedAmount) + lclBookingAc.getArBillToParty() + lclBookingAc.getId();
                    if (!lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase(CommonConstants.FFCOMM_CHARGECODE)
                            && !lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase(CommonConstants.PBA_CHARGECODE) && !chargesMap.containsKey(key)) {
                        chargesMap.put(key, key);
                        LCLCorrectionChargeBean lclCorrectionChargeBean = new LCLCorrectionChargeBean();
                        lclCorrectionChargeBean.setChargeId(lclBookingAc.getArglMapping().getId());
                        lclCorrectionChargeBean.setChargeCode(lclBookingAc.getArglMapping().getChargeCode());
                        lclCorrectionChargeBean.setChargeDescriptions(lclBookingAc.getArglMapping().getChargeDescriptions());
                        if (CommonUtils.isNotEmpty(correctionId)) {
                            Object amount[] = lclCorrectionChargeDAO.getCurrentCorrection(correctionId, lclBookingAc.getArglMapping().getId(),
                                    addedAmount, newAmount, viewMode, lclBookingAc.getId(), "Imports");
                            if (amount != null && amount.length > 0) {
                                if (amount[0] != null) {
                                    found = true;
                                    lclCorrectionChargeBean.setOldAmount(new BigDecimal(amount[0].toString()));
                                }
                                if (amount[1] != null) {
                                    found = true;
                                    lclCorrectionChargeBean.setNewAmount(new BigDecimal(amount[1].toString()));
                                }
                                if (amount[3] != null && !amount[3].toString().trim().equals("")) {
                                    lclCorrectionChargeBean.setCorrectionChargeId(Long.parseLong(amount[3].toString()));
                                    newIds.add(amount[3].toString());
                                }
                                lclCorrectionChargeBean.setBillToPartyLabel(setBillToPartyForCorrectionCharges(amount[2].toString()));
                                lclCorrectionChargeBean.setDelete(true);
                            }
                            if (viewMode != null && viewMode.equalsIgnoreCase("view")) {
                                if (!found) {
                                    Object[] oldAmount = lclCorrectionChargeDAO.getLatestCorrectionOldAmount(fileId, lclBookingAc.getArglMapping().getId(),
                                            lclBookingAc.getArBillToParty(), correctionId);
                                    if (oldAmount != null && oldAmount[1] != null && Double.parseDouble(oldAmount[1].toString()) > 0.00) {
                                        found = true;
                                        lclCorrectionChargeBean.setOldAmount(new BigDecimal(oldAmount[1].toString()));
                                    }
                                }
                            }
                        }
                        if (!found) {
                            billToCode = CommonUtils.isNotEmpty(billTo) ? billTo : CommonUtils.isNotEmpty(new LCLCorrectionChargeDAO().getNewBillToCode(correctionId)) ? new LCLCorrectionChargeDAO().getNewBillToCode(correctionId) : lclBookingAc.getArBillToParty();
                            if (bookingPieceCount > 1) {
                                lclCorrectionChargeBean.setOldAmount(lclBookingAc.getRolledupCharges().add(lclBookingAc.getAdjustmentAmount()));
                            } else {
                                lclCorrectionChargeBean.setOldAmount(lclBookingAc.getArAmount().add(lclBookingAc.getAdjustmentAmount()));
                            }
                            lclCorrectionChargeBean.setBillToPartyLabel(setBillToPartyForCorrectionCharges(billToCode));
                            lclCorrectionChargeBean.setOldBillToCode(lclBookingAc.getArBillToParty());
                        }
                        if (lclCorrectionChargeBean.getNewAmount() != null && lclCorrectionChargeBean.getOldAmount() != null) {
                            lclCorrectionChargeBean.setDifferenceAmount(lclCorrectionChargeBean.getNewAmount().subtract(lclCorrectionChargeBean.getOldAmount()));
                        }
                        lclCorrectionChargeBean.setLclBookingAcId(lclBookingAc.getId());
                        if (NumberUtils.isNotZero(lclCorrectionChargeBean.getNewAmount())
                                || NumberUtils.isNotZero(lclCorrectionChargeBean.getOldAmount())) {
                            lclCorrectionChargeList.add(lclCorrectionChargeBean);
                        }
                    }
                    found = false;
                    if (viewMode != null && viewMode.equalsIgnoreCase("view")) {
                        addCorrectionChargeBean = false;
                    }
                }
            }
        }
        if (CommonUtils.isEmpty(viewMode)) {
            List<Object[]> newChargesList = lclCorrectionChargeDAO.getAllNewCorrectionCharges(fileId);
            if (CommonUtils.isNotEmpty(newChargesList)) {
                for (Object[] newcharge : newChargesList) {
                    String key = newcharge[0].toString() + "_" + newcharge[3].toString() + newcharge[4].toString();
                    if (!chargesMap.containsKey(key) && !newIds.contains(newcharge[5].toString())) {
                        LCLCorrectionChargeBean lclCorrectionChargeBean = new LCLCorrectionChargeBean();
                        lclCorrectionChargeBean.setChargeId(Integer.parseInt(newcharge[0].toString()));
                        lclCorrectionChargeBean.setChargeCode(newcharge[1].toString());
                        if (newcharge[2] != null) {
                            lclCorrectionChargeBean.setChargeDescriptions(newcharge[2].toString());
                        }
                        lclCorrectionChargeBean.setOldAmount(new BigDecimal("0.00"));
                        lclCorrectionChargeBean.setNewAmount(new BigDecimal(newcharge[3].toString()));
                        lclCorrectionChargeBean.setDifferenceAmount(lclCorrectionChargeBean.getNewAmount());
                        lclCorrectionChargeBean.setDelete(true);
                        lclCorrectionChargeBean.setBillToPartyLabel(setBillToPartyForCorrectionCharges(newcharge[4].toString()));
                        lclCorrectionChargeBean.setCorrectionChargeId(Long.parseLong(newcharge[5].toString()));
                        lclCorrectionChargeList.add(lclCorrectionChargeBean);
                    }
                }
            }
        }
        return lclCorrectionChargeList;
    }

    public List<LCLCorrectionChargeBean> getFormattedCorrectionChargesPdf(Long correctionId) throws Exception {
        List<LCLCorrectionChargeBean> lclCorrectionChargeBeanList = new ArrayList<LCLCorrectionChargeBean>();
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        List<Object[]> lclCorrectionChargeList = lclCorrectionChargeDAO.getAllCorrectionChargesByCorrectionId(correctionId);
        if (CommonUtils.isNotEmpty(lclCorrectionChargeList)) {
            for (Object[] lclCorrectionCharge : lclCorrectionChargeList) {
                LCLCorrectionChargeBean lclCorrectionChargeBean = new LCLCorrectionChargeBean();
                lclCorrectionChargeBean.setChargeCode(lclCorrectionCharge[0].toString());
                lclCorrectionChargeBean.setOldAmount(new BigDecimal(lclCorrectionCharge[1].toString()));
                lclCorrectionChargeBean.setNewAmount(new BigDecimal(lclCorrectionCharge[2].toString()));
                lclCorrectionChargeBean.setDifferenceAmount(lclCorrectionChargeBean.getNewAmount().subtract(lclCorrectionChargeBean.getOldAmount()));
                lclCorrectionChargeBeanList.add(lclCorrectionChargeBean);
            }
        }
        return lclCorrectionChargeBeanList;
    }

    public String setBillToPartyForCorrectionCharges(String billToParty) {
        String label = null;
        if (billToParty.equalsIgnoreCase("F")) {
            label = "Forwarder";
        } else if (billToParty.equalsIgnoreCase("S")) {
            label = "Shipper";
        } else if (billToParty.equalsIgnoreCase("T")) {
            label = "ThirdParty";
        } else if (billToParty.equalsIgnoreCase("A")) {
            label = "Agent";
        } else if (billToParty.equalsIgnoreCase("C")) {
            label = "Consignee";
        } else if (billToParty.equalsIgnoreCase("N")) {
            label = "Notify";
        } else if (billToParty.equalsIgnoreCase("W")) {
            label = "Warehouse";
        }
        return label;
    }

    public void insertLCLBookingAcTrans(LclBookingAc lclBookingAc, String transType, String entryType, String referenceNumber, BigDecimal amount, User user) throws Exception {
        LclBookingAcTrans lclBookingAcTrans = new LclBookingAcTrans();
        LCLBookingAcTransDAO lclBookingAcTransDAO = new LCLBookingAcTransDAO();
        Date d = new Date();
        lclBookingAcTrans.setLclFileNumber(lclBookingAc.getLclFileNumber());
        lclBookingAcTrans.setTransType(transType);
        lclBookingAcTrans.setTransDatetime(d);
        lclBookingAcTrans.setEntryType(entryType);
        lclBookingAcTrans.setReferenceNo(referenceNumber);
        lclBookingAcTrans.setAmount(amount);
        lclBookingAcTrans.setEnteredBy(user);
        lclBookingAcTrans.setModifiedBy(user);
        lclBookingAcTrans.setModifiedDatetime(d);
        lclBookingAcTrans.setEnteredDatetime(d);
        lclBookingAcTransDAO.save(lclBookingAcTrans);
        LclBookingAcTa lclBookingAcTa = new LclBookingAcTa();
        lclBookingAcTa.setLclBookingAcTrans(lclBookingAcTrans);
        lclBookingAcTa.setLclBookingAc(lclBookingAc);
        lclBookingAcTa.setAmount(new BigDecimal(0.00));
        lclBookingAcTa.setEnteredByUserId(user.getUserId());
        lclBookingAcTa.setEnteredDatetime(d);
        lclBookingAcTa.setModifiedByUserId(user.getUserId());
        lclBookingAcTa.setModifiedDatetime(d);
        new LclBookingAcTaDAO().saveOrUpdate(lclBookingAcTa);
    }

    public void setMailTransactionsDetails(String name, String type, User user, String label, String status, Date date, String moduleId, Long moduleRefId) throws Exception {
        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
        PrintConfigDAO printConfigDAO = new PrintConfigDAO();
        emailSchedulerVO.setFileLocation("");
        emailSchedulerVO.setName(name);
        emailSchedulerVO.setSubject("");
        emailSchedulerVO.setType(type);
        emailSchedulerVO.setStatus(status);
        emailSchedulerVO.setToAddress("");
        emailSchedulerVO.setFromName("");
        emailSchedulerVO.setFromAddress("");
        emailSchedulerVO.setEmailDate(date);
        emailSchedulerVO.setTextMessage("");
        emailSchedulerVO.setModuleName(name);
        emailSchedulerVO.setUserName(user.getLoginName());
        emailSchedulerVO.setModuleId(moduleId);
        emailSchedulerVO.setHtmlMessage("");
        emailSchedulerVO.setPrinterName(printConfigDAO.isPrintAllowed(printConfigDAO.findDocumentIdByName(CommonConstants.LABEL_PRINT, "LCLBooking"), user.getUserId()));
        if (CommonUtils.isNotEmpty(label)) {
            emailSchedulerVO.setPrintCopy(Integer.parseInt(label));
        }
        emailSchedulerVO.setModuleReferenceId(moduleRefId);
        new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
    }

    public void sendMailWithoutPrintConfig(String fileLocation, String name, String subject, String type, String status, String toAddress,
            String moduleId, String moduleName, String textMessage, String htmlMessage, User user) throws Exception {
        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
        emailSchedulerVO.setFileLocation(fileLocation);
        emailSchedulerVO.setName(name);
        emailSchedulerVO.setSubject(subject);
        emailSchedulerVO.setType(type);
        emailSchedulerVO.setStatus(status);
        emailSchedulerVO.setToAddress(toAddress);
        emailSchedulerVO.setFromName(user.getFirstName());
        emailSchedulerVO.setFromAddress(user.getEmail());
        emailSchedulerVO.setEmailDate(new Date());
        emailSchedulerVO.setTextMessage(textMessage);
        emailSchedulerVO.setModuleName(moduleName);
        emailSchedulerVO.setUserName(user.getLoginName());
        emailSchedulerVO.setToName(user.getLoginName());
        emailSchedulerVO.setModuleId(moduleId);
        emailSchedulerVO.setHtmlMessage(htmlMessage);
        new EmailschedulerDAO().saveOrUpdate(emailSchedulerVO);
    }

    public void insertLclBookingDispo(Long fileId, Disposition dispo, LclUnit unitId, Long ssDetailId, User user, UnLocation unLocID) throws Exception {
        LclBookingDispo lclBookingDispo = new LclBookingDispo();
        Date d = new Date();
        lclBookingDispo.setLclFileNumber(new LclFileNumber(fileId));
        lclBookingDispo.setDisposition(dispo);
        lclBookingDispo.setDispositionDatetime(d);
        lclBookingDispo.setEnteredBy(user);
        lclBookingDispo.setEnteredDatetime(d);
        lclBookingDispo.setModifiedBy(user);
        lclBookingDispo.setModifiedDatetime(d);
        if (null != unitId && null != ssDetailId) {
            lclBookingDispo.setLclUnit(unitId);
            lclBookingDispo.setLclSsDetail(new LclSsDetail(ssDetailId));
        }
        if (null != unLocID) {
            lclBookingDispo.setUnLocation(unLocID);
        }
        new LclBookingDispoDAO().save(lclBookingDispo);
    }

    public String deriveLclBlNo(String fileId) throws Exception {
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        return lclFileNumberDAO.deriveLclBlNoByFileId(fileId);
    }

    public void updateLclFileNumbers(String fileNumbers, boolean isManifest, String selectedMenu) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_file_number set status  = '");
        if (isManifest) {
            queryBuilder.append("M");
        } else {
            if (selectedMenu.equalsIgnoreCase("LCLE")) {
                queryBuilder.append("L");
            } else {
                queryBuilder.append("B");
            }
        }
        queryBuilder.append("' where file_number IN(").append(fileNumbers).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void insertlclBkgpieceUnit(Long bkgpieceId, Long unitssId, int userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO lcl_booking_piece_unit(booking_piece_id,lcl_unit_ss_id,loaded_datetime,entered_datetime,entered_by_user_id,modified_datetime,modified_by_user_id) VALUES (");
        sb.append(bkgpieceId).append(",").append(unitssId).append(",SYSDATE(),").append("SYSDATE(),").append(userId).append(",").append("SYSDATE(),").append(userId).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
    }

    public void setTotalTransitdays(HttpServletRequest request, Date etaDate, Date etdDate) throws Exception {
        long timeDiff = Math.abs(etaDate.getTime() - etdDate.getTime());
        long totalDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
        request.setAttribute("totalTransitdays", totalDays);

    }

    public String getBillingTypeErrorMessage(Long unitSSId, String selectedMenu, String postFlag) throws Exception {
        String key = null;
        StringBuilder errorMessage = new StringBuilder();
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        Map<String, ImportsManifestBean> manifestMap = new LinkedHashMap();
        List<ImportsManifestBean> drList = lclunitssdao.getChargesBillingType(unitSSId, selectedMenu, postFlag);
        if (CommonUtils.isNotEmpty(drList)) {
            for (ImportsManifestBean manifestBean : drList) {
                if (CommonUtils.isNotEmpty(manifestBean.getBillToParty())) {
                    key = manifestBean.getFileId().toString() + "_" + manifestBean.getBillToParty();
                    if (!manifestMap.containsKey(key)) {
                        if (CommonUtils.isEmpty(manifestBean.getCustomerNumber())) {
                            if (selectedMenu.equalsIgnoreCase("Imports")) {
                                if (manifestBean.getBillToParty().equalsIgnoreCase("N")) {
                                    errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Notify is required. <br>");
                                }
                            } else {
//                                if (manifestBean.getBillToParty().equalsIgnoreCase("F")) {
//                                    errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Forwarder is required in BL. <br>");
//                                } else 
                                    if (manifestBean.getBillToParty().equalsIgnoreCase("S")) {
                                    errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Shipper is required in BL. <br>");
                                }
                            }
                            if (selectedMenu.equalsIgnoreCase("Exports") && manifestBean.getBillToParty().equalsIgnoreCase("A")) {
                                errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Agent is required in BL. <br>");
                            } else if (manifestBean.getBillToParty().equalsIgnoreCase("T")) {
                                errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Third Party is required ");
                                if (selectedMenu.equalsIgnoreCase("Exports")) {
                                    errorMessage.append(" in BL ");
                                }
                                errorMessage.append(". <br>");
                            } else if (manifestBean.getBillToParty().equalsIgnoreCase("C")) {
                                errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Consignee is required ");
                                if (selectedMenu.equalsIgnoreCase("Exports")) {
                                    errorMessage.append(" in BL ");
                                }
                                errorMessage.append(". <br>");
                            }
                        }
                        manifestMap.put(key, manifestBean);
                    }
                } else {
                    errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Charge Code ").append(manifestBean.getChargeCode()).append(" ").append("Bill To Party is Empty. <br>");
                }
            }
        }
        return errorMessage.toString();
    }

    public String getDrBillingTypeErrorMessage(Long unitSSId) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
        List<ImportsManifestBean> drList = lclunitssdao.getDRSBillingType(unitSSId);
        if (CommonUtils.isNotEmpty(drList)) {
            for (ImportsManifestBean manifestBean : drList) {
                if (CommonUtils.isNotEmpty(manifestBean.getBillToParty()) && CommonUtils.isEmpty(manifestBean.getCustomerNumber())) {
                    if (manifestBean.getBillToParty().equalsIgnoreCase("C")) {
                        errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Consignee is required. <br>");
                    } else if (manifestBean.getBillToParty().equalsIgnoreCase("N")) {
                        errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Notify is required. <br>");
                    } else if (manifestBean.getBillToParty().equalsIgnoreCase("T")) {
                        errorMessage.append("File# ").append(manifestBean.getFileNo()).append(" ").append("Third Party is required. <br>");
                    }
                }
            }
        }
        return errorMessage.toString();
    }

    public String getFieldNameForImportsBP(String billToParty) throws Exception {
        String fieldName = "agent_acct_no";
        if (billToParty.equalsIgnoreCase("T")) {
            fieldName = "third_party_acct_no";
        } else if (billToParty.equalsIgnoreCase("C")) {
            fieldName = "cons_acct_no";
        } else if (billToParty.equalsIgnoreCase("N")) {
            fieldName = "noty_acct_no";
        }
        return fieldName;
    }

    public void setCorrection(HttpServletRequest request, Long fileId) throws Exception {
        if (CommonUtils.isNotEmpty(fileId)) {
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            if (lclCorrectionDAO.getCorrectionCountByFileId(fileId) > 0) {
                request.setAttribute("correctionClassName", "green-background");
            } else {
                request.setAttribute("correctionClassName", "button-style1");
            }
        }
    }

    public void insertCreditDebitNote(String fileNumber, String correctionNumber, String customerName, String customerNumber,
            String debitCreditNote) throws Exception {
        CreditDebitNoteDAO creditDebitNoteDAO = new CreditDebitNoteDAO();
        CreditDebitNote creditDebitNote = new CreditDebitNote();
        creditDebitNote.setBolid(fileNumber);
        creditDebitNote.setCorrectionNumber(correctionNumber);
        creditDebitNote.setCustomerName(customerName);
        creditDebitNote.setDebitCreditNote(debitCreditNote);
        creditDebitNote.setCustomerNumber(customerNumber);
        creditDebitNoteDAO.save(creditDebitNote);
    }

    public String getCustAddressContactDetails(String acctNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        if (acctNo != null && !acctNo.equals("")) {
            CustAddress custAddress = custAddressDAO.findByAccountNoAndPrime(acctNo);
            if (custAddress != null && custAddress.getAddress1() != null && !custAddress.getAddress1().equals("")) {
                sb.append(custAddress.getAddress1().toUpperCase()).append("\n");
                if (custAddress.getCity1() != null && !custAddress.getCity1().equals("")) {
                    sb.append(custAddress.getCity1().toUpperCase());
                }
                if (custAddress.getState() != null && !custAddress.getState().equals("")) {
                    sb.append(custAddress.getState().toUpperCase());
                }
                if (custAddress.getZip() != null && !custAddress.getZip().equals("")) {
                    sb.append(custAddress.getZip()).append("\n");
                }
                if (custAddress.getPhone() != null && !custAddress.getPhone().equals("")) {
                    sb.append(custAddress.getPhone()).append("\n");
                }
                if (custAddress.getFax() != null && !custAddress.getFax().equals("")) {
                    sb.append(custAddress.getFax()).append("\n");
                }
            }
        }
        return sb.toString();
    }

    public String getConcatenatedLclContactAddress(LclContact lclContact) throws Exception {
        StringBuilder contactAddress = new StringBuilder();
        if (lclContact != null) {
            if (CommonUtils.isNotEmpty(lclContact.getAddress())) {
                contactAddress.append(lclContact.getAddress()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getCity())) {
                contactAddress.append(lclContact.getCity());
            }
            if (CommonUtils.isNotEmpty(lclContact.getState())) {
                contactAddress.append(",  ").append(lclContact.getState());
            }
            if (CommonUtils.isNotEmpty(lclContact.getZip())) {
                contactAddress.append(",  ").append(lclContact.getZip()).append("\n");
            } else {
                contactAddress.append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getPhone1())) {
                contactAddress.append("PHONE: ").append(lclContact.getPhone1()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getFax1())) {
                contactAddress.append("FAX: ").append(lclContact.getFax1()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getEmail1())) {
                contactAddress.append("EMAIL: ").append("\n");
                contactAddress.append(lclContact.getEmail1());
            }
        }
        return contactAddress.toString();
    }

    /* this will set voyage information */
    public LclBookingPieceUnit setVoyage(LclBookingPiece bkgPiece, LCLBookingForm bkgForm, HttpServletRequest request) throws Exception {
        List<LclBookingPieceUnit> bkgPieceUnits = bkgPiece.getLclBookingPieceUnitList();
        LclBookingPieceUnit bkgPieceUnit = bkgPieceUnits.isEmpty() ? null : bkgPieceUnits.get(0);
        if (null != bkgPieceUnit) {
            LclUnitSs unitss = bkgPieceUnit.getLclUnitSs();
            LclSsHeader ssHeader = unitss.getLclSsHeader();
            String locName = ssHeader.getOrigin().getUnLocationName();
            String locCode = ssHeader.getOrigin().getUnLocationCode();
            LclUnit unit = unitss.getLclUnit();
            List<LclUnitSsImports> unitssImports = unit.getLclUnitSsImportsList();
            LclUnitSsImports unitssImp = unitssImports.isEmpty() ? null : unitssImports.get(0);
            LclSsDetail ssDetail = ssHeader.getLclSsDetailList().isEmpty() ? null : ssHeader.getLclSsDetailList().get(0);
            String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(unitss.getLclUnit().getId(), ssDetail.getId());
            if (CommonUtils.isNotEmpty(disposition)) {
                bkgForm.setDisposition(disposition);
            }
            bkgForm.setUnitId(String.valueOf(unitss.getLclUnit().getId()));
            bkgForm.setUnitSsId(String.valueOf(unitss.getId()));
            bkgForm.setHeaderId(String.valueOf(ssHeader.getId()));
            bkgForm.setImpEciVoyage(ssHeader.getScheduleNo());
            bkgForm.setImpUnitNo(unit.getUnitNo());
            bkgForm.setImpPier((null != locName ? locName.toUpperCase() : "") + "(" + locCode + ")");
            if (null != ssDetail) {
                bkgForm.setImpVesselName(ssDetail.getSpReferenceName());
                bkgForm.setImpSsVoyage(ssDetail.getSpReferenceNo());
                bkgForm.setImpSsLine(null != ssDetail.getSpAcctNo()
                        ? ssDetail.getSpAcctNo().getAccountName() : "");
                bkgForm.setImpVesselArrival(null != ssDetail.getSta()
                        ? DateUtils.formatStringDateToAppFormatMMM(ssDetail.getSta()) : "");
                bkgForm.setImpSailDate(null != ssDetail.getStd()
                        ? DateUtils.formatStringDateToAppFormatMMM(ssDetail.getStd()) : "");
            }
            if (null != unitssImp) {
                bkgForm.setUnitItNo(unitssImp.getItNo());
                bkgForm.setImpCFSWareName(null != unitssImp.getCfsWarehouseId()
                        ? unitssImp.getCfsWarehouseId().getWarehouseName() : "");
                bkgForm.setUnitItPort(null != unitssImp.getItPortId()
                        ? unitssImp.getItPortId().getUnLocationName() : "");
                bkgForm.setUnitItDate(null != unitssImp.getItDatetime()
                        ? DateUtils.formatStringDateToAppFormatMMM(unitssImp.getItDatetime()) : "");
                bkgForm.setImportApproxDue(null != unitssImp.getApproxDueDate()
                        ? DateUtils.formatStringDateToAppFormatMMM(unitssImp.getApproxDueDate()) : "");
                request.setAttribute("lastFdDate", null != unitssImp.getLastFreeDate()
                        ? DateUtils.formatStringDateToAppFormatMMM(unitssImp.getLastFreeDate()) : "");
            }
        }
        return bkgPieceUnit;
    }

    public boolean isCorrectionFound(String unitSsId) throws Exception {
        boolean result = false;
        if (CommonUtils.isNotEmpty(unitSsId)) {
            LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            Integer count = lclCorrectionDAO.getVoidedCorrectionCountByFileIds(lclUnitSsDAO.getConcatenatedFileIds(unitSsId), "0");
            if (count > 0) {
                result = true;
            }
        }
        return result;
    }

    public void setUnknowDestValues(HttpServletRequest request) throws Exception {
        UnLocationDAO unlocationdao = new UnLocationDAO();
        UnLocation unlocation = unlocationdao.getUnlocation(CommonConstants.UNKNOWN_DEST);
        if (null != unlocation && CommonUtils.isNotEmpty(unlocation.getId())) {
            request.setAttribute("unknownDest", this.getConcatenatedOriginByUnlocation(unlocation));
            request.setAttribute("unknownDestId", unlocation.getId());
        }
    }

    public LclUnitSs getLclUnitSSFromHeaderList(Long unitssId, LclSsHeader lclssheader) throws Exception {
        for (int i = 0; i < lclssheader.getLclUnitSsList().size(); i++) {
            LclUnitSs lclUnitSs = lclssheader.getLclUnitSsList().get(i);
            if (lclUnitSs.getId().longValue() == unitssId) {
                return lclUnitSs;
            }
        }
        return null;
    }

    public void inbondDetails(Long fileId, HttpServletRequest request) throws Exception {
        String inbondNumber = "";
        LclInbondsDAO lclInbondsDAO = new LclInbondsDAO();
        List<LclInbond> lclInbondList = lclInbondsDAO.findByProperty("lclFileNumber.id", fileId);
        if (!lclInbondList.isEmpty()) {
            for (LclInbond li : lclInbondList) {
                String inbondDatetime = "";
                String inbondPort = "";
                if (li.getInbondDatetime() != null) {
                    inbondDatetime = DateUtils.formatStringDateToAppFormatMMM(li.getInbondDatetime());
                }
                if (li.getInbondPort() != null && li.getInbondPort().getUnLocationName() != null) {
                    inbondPort = li.getInbondPort().getUnLocationName();
                }
                inbondNumber += li.getInbondType() + " " + li.getInbondNo() + " " + inbondPort + " " + inbondDatetime + "<br/>";
            }
            request.setAttribute("inbondNumber", inbondNumber.toUpperCase());
            request.setAttribute("inbondListSize", lclInbondList.size());
            request.setAttribute("inbondList", lclInbondList);
            LclInbond lclInbond = lclInbondList.get(0);
            String inbondDatetime = "";
            if (lclInbond != null && lclInbond.getInbondDatetime() != null) {
                inbondDatetime = DateUtils.formatStringDateToAppFormatMMM(lclInbond.getInbondDatetime());
            }
            request.setAttribute("lclInbond", lclInbond);
            request.setAttribute("inbondDatetime", inbondDatetime);
        }
    }

    public String getEdiStatus(String fileNumber) throws Exception {
        String ediStatus = "";
        EdiDAO edidao = new EdiDAO();
        LogFileEdi edifile = edidao.getfilestatus(fileNumber);
        if (null != edifile) {
            String type = edifile.getMessageType();
            String status = edifile.getStatus();
            if (null != type && null != status) {
                if (type.equals("304") && status.equals("success")) {
                    ediStatus = "yellow";
                } else if (type.equals("304") && status.equals("failure")) {
                    ediStatus = "red";
                } else if (type.equals("997") && status.equals("success")) {
                    ediStatus = "green";
                } else if (type.equals("997") && status.equals("failure")) {
                    ediStatus = "red";
                }
            }
        }
        return ediStatus;
    }

    public void reverseAllImportsCorrections(String correctionIds, User user) throws Exception {
        LCLCorrectionChargeDAO lclCorrectionChargeDAO = new LCLCorrectionChargeDAO();
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        LclBooking lclBooking = null;
        String[] correctionIdArray = correctionIds.split(",");
        for (String correctionId : correctionIdArray) {
            Long longCorrectionId = Long.parseLong(correctionId);
            List<Object[]> lclCorrectionChargeList = lclCorrectionChargeDAO.getAllCorrectionChargesForReverse(longCorrectionId);
            LclCorrection lclCorrection = lclCorrectionDAO.findById(longCorrectionId);
            lclBooking = new LCLBookingDAO().findById(lclCorrection.getLclFileNumber().getId());
            for (Object[] lclCorrectionCharge : lclCorrectionChargeList) {
                LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
                Object instance = lclCostChargeDAO.getIdMebyFileNumberAndChargeId(lclCorrection.getLclFileNumber().getId(), Integer.parseInt(lclCorrectionCharge[0].toString()),
                        lclCorrectionCharge[1].toString(), "R", lclCorrectionCharge[2].toString());
                if (Double.parseDouble(lclCorrectionCharge[1].toString()) > 0.00) {
                    lclCostChargeDAO.updateChargesForReversal(instance.toString(), user.getUserId(), lclCorrectionCharge);
                } else {
                    lclCostChargeDAO.deleteChargesById(instance.toString());
                }
            }
            lclManifestDAO.createLclCorrections("Imports", user, false, lclCorrection, false, lclBooking);
        }
        lclCorrectionDAO.reverseCorrections(correctionIds, user.getUserId());
    }

    public String getConcatenatedAccountDetails(LclContact lclContact) throws Exception {
        StringBuilder accountDetails = new StringBuilder();
        if (lclContact != null) {
            if (lclContact.getCompanyName() != null && !"".equals(lclContact.getCompanyName())) {
                accountDetails.append(lclContact.getCompanyName()).append("\n");
            }
            if (lclContact.getAddress() != null && !"".equals(lclContact.getAddress())) {
                accountDetails.append(lclContact.getAddress()).append("\n");
            }
        }
        return accountDetails.toString();
    }
    
    public StringBuilder cbmcftConversion(StringBuilder sb, String label, String value) {//dims
        Double result = 0.0d;
        Double val = Double.parseDouble(value);
        if (label.equals("I")) {
            result = val * 2.54;
        }
        if (label.equals("M")) {
            result = val * 0.3937;
        }
        sb.append(String.format("%.2f", result));
        return sb;
    }

    public List getTrmNumandEciPortCode(String pooUnloCode, String polUnloCode,
            String podUnloCode, String fdUnloCode, String rateType) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        if (null != pooUnloCode && !"".equalsIgnoreCase(pooUnloCode)) {
            queryStr.append("SELECT trmnum ,'','POO' FROM terminal ");
            queryStr.append("WHERE unLocationCode1='").append(pooUnloCode);
            queryStr.append("' AND actyon ='").append(rateType).append("'");
            queryStr.append(" UNION ALL ");
        }
        if (null != polUnloCode && !"".equalsIgnoreCase(polUnloCode)) {
            queryStr.append("SELECT trmnum ,'','POL' FROM terminal ");
            queryStr.append("WHERE unLocationCode1='").append(polUnloCode);
            queryStr.append("' AND actyon ='").append(rateType).append("'");
            queryStr.append(" UNION ALL ");
        }
        if (null != podUnloCode && !"".equalsIgnoreCase(podUnloCode)) {
            queryStr.append(" SELECT eciportcode,'','POD' FROM ports ");
            queryStr.append(" WHERE unlocationcode='").append(podUnloCode).append("' ");
            queryStr.append(" UNION ALL ");
        }
        queryStr.append(" SELECT eciportcode,engmet,'FD' FROM ports ");
        queryStr.append(" WHERE unlocationcode='").append(fdUnloCode).append("' ");
        return getCurrentSession().createSQLQuery(queryStr.toString()).list();
    }

    public LclContact getContactForExportBl(LclContact bookingContact, User thisUser, LclFileNumber lclFileNumber, String contactType) throws Exception {
        LclContact oldcontact = new LCLContactDAO().getContact(lclFileNumber.getId(), contactType);
        LclContact newContact = oldcontact == null
                ? new LclContact(null, "", new Date(), new Date(), thisUser, thisUser, lclFileNumber) : oldcontact;
        StringBuilder sb = new StringBuilder();
        if (null != bookingContact) {
            if (CommonUtils.isNotEmpty(bookingContact.getAddress())) {
                sb.append(bookingContact.getAddress()).append("\n");
            }
            if (CommonUtils.isNotEmpty(bookingContact.getCity())) {
                sb.append(bookingContact.getCity()).append(",");
            }
            if (CommonUtils.isNotEmpty(bookingContact.getState())) {
                sb.append(bookingContact.getState());
            }
            if (CommonUtils.isNotEmpty(bookingContact.getCountry()) && 
              !contactType.equalsIgnoreCase("blShipper") && !contactType.equalsIgnoreCase("blForwarder")) {
                sb.append(",").append(bookingContact.getCountry()).append("  ");
            }else{
                sb.append("  ");
            }
            if (CommonUtils.isNotEmpty(bookingContact.getZip())) {
                sb.append(bookingContact.getZip());
            }
            if (CommonUtils.isNotEmpty(bookingContact.getPhone1())) {
                sb.append("\n").append("PHONE :").append(bookingContact.getPhone1());
            }
            newContact.setRemarks(contactType);
            newContact.setCompanyName(bookingContact.getCompanyName());
            newContact.setAddress(sb.toString());
        }
        return newContact;
    }

    public LclContact setContactDataForBooking(LclContact bookingContact, LclContact existingContact, User loginUser, LclFileNumber lclFileNumber, String remarksType) throws Exception {
        bookingContact.setAddress(null != existingContact.getAddress() ? existingContact.getAddress().toUpperCase() : "");
        bookingContact.setCity(null != existingContact.getCity() ? existingContact.getCity().toUpperCase() : "");
        bookingContact.setState(null != existingContact.getState() ? existingContact.getState().toUpperCase() : "");
        bookingContact.setCountry(null != existingContact.getCountry() ? existingContact.getCountry().toUpperCase() : "");
        bookingContact.setPhone1(null != existingContact.getPhone1() ? existingContact.getPhone1().toUpperCase() : "");
        bookingContact.setFax1(null != existingContact.getFax1() ? existingContact.getFax1().toUpperCase() : "");
        bookingContact.setZip(null != existingContact.getZip() ? existingContact.getZip().toUpperCase() : "");
        bookingContact.setEmail1(existingContact.getEmail1());
        bookingContact.setLclFileNumber(lclFileNumber);
        bookingContact.setRemarks(remarksType);
        bookingContact.setEnteredBy(loginUser);
        bookingContact.setModifiedBy(loginUser);
        bookingContact.setModifiedDatetime(new Date());
        bookingContact.setEnteredDatetime(new Date());
        return bookingContact;
    }

    public void addOverSizeRemarks(String uom1, String actualHeight, String actualWidth, String actualLength,
            Long fileNumberId, HttpServletRequest request, LclSession lclSession, User loginUser) throws Exception {
        Double height = 0.00;
        Double width = 0.00;
        Double length = 0.00;
        if (uom1.equalsIgnoreCase("I")) {
            height = Double.parseDouble(actualHeight);
            width = Double.parseDouble(actualWidth);
            length = Double.parseDouble(actualLength);
        } else {
            height = Double.parseDouble(actualHeight) * 0.3937;
            width = Double.parseDouble(actualWidth) * 0.3937;
            length = Double.parseDouble(actualLength) * 0.3937;
        }
        if (height > 79 || width > 79 || length > 72) {
            LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
            GenericCodeDAO genericCodeDao = new GenericCodeDAO();
            GenericCode genericCode = genericCodeDao.findByPropertyForChargeCode("codetypeid", 57, _3PARTY_TYPE_OVR);
            LclBookingHotCode LclBookingHotCode = lclBookingHotCodeDAO.getHotCodeByFileIDCode(fileNumberId,
                    genericCode.getCode() + "/" + genericCode.getCodedesc().toUpperCase());
            if (LclBookingHotCode == null) {
                lclBookingHotCodeDAO.saveHotCode(fileNumberId,
                        genericCode.getCode() + "/" + genericCode.getCodedesc().toUpperCase(), loginUser.getUserId());
            }
        }
        String remarks = "";
        if (height > 79) {
            remarks = "H-" + height.intValue() + " ";
        }
        if (length > 72) {
            remarks += "L-" + length.intValue() + " ";
        }
        if (width > 79) {
            remarks += "W-" + width.intValue();
        }
        if (CommonUtils.isNotEmpty(remarks)) {
            if (CommonUtils.isNotEmpty(fileNumberId)) {
                Date d = new Date();
                LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
                LclRemarks lclRemarks = lclRemarksDAO.getRemarks(fileNumberId, REMARKS_TYPE_LOADING_REMARKS, "");
                if (lclRemarks == null) {
                    lclRemarks = new LclRemarks();
                    lclRemarks.setLclFileNumber(new LclFileNumber(fileNumberId));
                    lclRemarks.setEnteredBy(loginUser);
                    lclRemarks.setEnteredDatetime(d);
                    lclRemarks.setType(REMARKS_TYPE_LOADING_REMARKS);
                }
                lclRemarks.setRemarks(remarks);
                lclRemarks.setModifiedBy(loginUser);
                lclRemarks.setModifiedDatetime(d);
                lclRemarksDAO.saveOrUpdate(lclRemarks);
            } else {
                HttpSession session = request.getSession();
                lclSession.setOverRiddedRemarks(remarks);
                session.setAttribute("lclSession", lclSession);
            }
        }
    }

    public List<LabelValueBean> getRemarksTypeList() {
        List<LabelValueBean> remarksTypeList = new ArrayList<LabelValueBean>();
        remarksTypeList.add(new LabelValueBean("Manual Notes", "manualNotes"));
        remarksTypeList.add(new LabelValueBean("Show All", "showAll"));
        remarksTypeList.add(new LabelValueBean("Show Void", "showVoid"));
        remarksTypeList.add(new LabelValueBean("FollowupDate Exists", "followUpDateExist"));
        remarksTypeList.add(new LabelValueBean("FollowupDate Past", "followUpDatePast"));
        remarksTypeList.add(new LabelValueBean("Auto Notes", "autoNotes"));
        remarksTypeList.add(new LabelValueBean("Events", "events"));
        remarksTypeList.add(new LabelValueBean("Disputed Notes", "disputedNotes"));
        remarksTypeList.add(new LabelValueBean("Tracking Notes", "trackingNotes"));
        remarksTypeList.add(new LabelValueBean("Special Notes", "specialNotes"));
        remarksTypeList.add(new LabelValueBean("CTS", "cts"));
        remarksTypeList.add(new LabelValueBean("EDISTG", "edistg"));
        return remarksTypeList;
    }

    public String getHazmatFreeFormData(String dbName, String hazId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select hazmat_declarations from ").append(dbName).append(" where id =:hazmatId ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("hazmatId", hazId);
        return null != query.uniqueResult() ? query.uniqueResult().toString() : "";
    }

    public void updateHazmatFreeFromData(String dbName, String hazId, String hazmatValue) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(dbName).append(" set hazmat_declarations =:hazmatValue where id =:hazmatId ");
        SQLQuery query = new BaseHibernateDAO().getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("hazmatId", hazId);
        query.setParameter("hazmatValue", hazmatValue);
        query.executeUpdate();
    }

    public String appendHazmatClass() throws Exception {
        List hazmatClassDecList = getHazmatClassDesc();
        StringBuilder appendStr = new StringBuilder();
        appendStr.append("<table border=0>");
        if (null != hazmatClassDecList && !hazmatClassDecList.isEmpty()) {
            for (Object l : hazmatClassDecList) {
                Object[] col = (Object[]) l;
                appendStr.append("<tr><td><font color='red'>");
                appendStr.append(col[0].toString());
                appendStr.append("</font></td><td>");
                appendStr.append(col[1].toString());
                appendStr.append("</td></tr>");
            }
        }
        appendStr.append("</table>");
        return appendStr.toString();
    }

    private List getHazmatClassDesc() throws Exception {
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT imoccd,CONCAT(loadcd,'-', descrp) AS hazmatClass ");
        queryStr.append(" FROM ").append(databaseSchema).append(".ldgins ");
        queryStr.append(" WHERE imoccd IN  ");
        queryStr.append(" (SELECT gd.code  FROM genericcode_dup gd  ");
        queryStr.append(" WHERE gd.codetypeid =  ");
        queryStr.append(" (SELECT codetypeid FROM codetype  ");
        queryStr.append(" WHERE description = 'Primary ClassCodes')) ORDER BY imoccd ASC ");
        return getCurrentSession().createSQLQuery(queryStr.toString()).list();
    }

    public List getBillingTypeByLclE(String billToParty, String billingType) throws Exception {
        List billingTypeList = new ArrayList();
        if ("P".equalsIgnoreCase(billToParty)) {
//            if ("F".equalsIgnoreCase(billingType)) {
//            } else if ("S".equalsIgnoreCase(billingType)) {
            billingTypeList.add(new LabelValueBean("Select", ""));
            billingTypeList.add(new LabelValueBean("Forwarder", "F"));
            billingTypeList.add(new LabelValueBean("Shipper", "S"));
            billingTypeList.add(new LabelValueBean("Third Party", "T"));
//            } else {
//            }
        } else if ("C".equalsIgnoreCase(billToParty)) {
            billingTypeList.add(new LabelValueBean("Agent", "A"));
        } else if ("B".equalsIgnoreCase(billToParty)) {
            billingTypeList.add(new LabelValueBean("Select", ""));
            billingTypeList.add(new LabelValueBean("Forwarder", "F"));
            billingTypeList.add(new LabelValueBean("Shipper", "S"));
            billingTypeList.add(new LabelValueBean("Third Party", "T"));
            billingTypeList.add(new LabelValueBean("Agent", "A"));
        }
        return billingTypeList;
    }

    public String getConcatenatedLclImportsContactAddress(CustomerAddress lclContact) throws Exception {
        StringBuilder contactAddress = new StringBuilder();
        if (lclContact != null) {
            if (CommonUtils.isNotEmpty(lclContact.getAddress1())) {
                contactAddress.append(lclContact.getAddress1()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getCity2())) {
                contactAddress.append(lclContact.getCity2());
            }
            if (CommonUtils.isNotEmpty(lclContact.getState())) {
                contactAddress.append(",  ").append(lclContact.getState());
            }
            if (CommonUtils.isNotEmpty(lclContact.getZip())) {
                contactAddress.append(",  ").append(lclContact.getZip()).append("\n");
            } else {
                contactAddress.append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getPhone())) {
                contactAddress.append("PHONE: ").append(lclContact.getPhone()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getFax())) {
                contactAddress.append("FAX: ").append(lclContact.getFax()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclContact.getEmail1())) {
                contactAddress.append("EMAIL: ").append("\n");
                contactAddress.append(lclContact.getEmail1());
            }
        }
        return contactAddress.toString();
    }

    public void setTemBillToPartyList(HttpServletRequest request, String moduleName) {
        List<LclBookingAc> formatBillToPartyList = new ArrayList();
        List<LclBookingAc> lclBookingAcChargeList = (List<LclBookingAc>) request.getAttribute("chargeList");
        if (null != lclBookingAcChargeList) {
            for (LclBookingAc lclBookingAc : lclBookingAcChargeList) {
                if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                    lclBookingAc.setTempArBillToParty(CommonUtils.in(lclBookingAc.getArBillToParty(), "F", "S") ? "A"
                            : lclBookingAc.getArBillToParty());
                } else {
                    lclBookingAc.setTempArBillToParty(CommonUtils.in(lclBookingAc.getArBillToParty(), "W", "A") ? "S"
                            : CommonUtils.in(lclBookingAc.getArBillToParty(), "C", "N") ? "A" : lclBookingAc.getArBillToParty());
                }
                formatBillToPartyList.add(lclBookingAc);
            }
            request.setAttribute("chargeList", formatBillToPartyList);
        }
    }
    
    public String getConcatenatedSSmasterAccountDetails(LclSsContact lclSsContact) throws Exception {
        StringBuilder accountDetails = new StringBuilder();
        if (lclSsContact != null) {
            if (lclSsContact.getCompanyName() != null && !"".equals(lclSsContact.getCompanyName())) {
                accountDetails.append(lclSsContact.getCompanyName()).append("\n");
            }
            if (lclSsContact.getAddress() != null && !"".equals(lclSsContact.getAddress())) {
                accountDetails.append(lclSsContact.getAddress()).append("\n");
            }
        }
        return accountDetails.toString();
    }
    
    public String getContainerSize(Long unitId) throws Exception {
        String[] description = new LclUnitSsDAO().getLclUnitId(unitId);
        String desc = "";
        if (CommonUtils.isNotEmpty(description[0])) {
            desc = description[0];
        } else {
            if (description[1].equalsIgnoreCase("LCL")) {
                desc = description[1].toUpperCase();

            } else {
                desc = description[1].substring(0, 4).toUpperCase();
            }
        }
        return desc;
    }
}

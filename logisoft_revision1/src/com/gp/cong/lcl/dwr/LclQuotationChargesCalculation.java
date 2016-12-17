/**
 * @author Saravanan
 *
 */
package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.beans.ChargesInfoBean;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.beans.TotalPercentageBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclWarehsDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.common.dao.PropertyDAO;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public class LclQuotationChargesCalculation implements LclCommonConstant {

    private String CIFValue;
    private String pcb;
    private Double totalOceanFreight = 0.0, totalBarrelCharges = 0.0, totalBarellTTA = 0.0;
    private Double rolledUpWeight = 0.00, rolledUpMeasure = 0.00, rolledUpMinchg = 0.00;
    private Double totalWeight = 0.0;
    private Double totalMeasure = 0.0;
    private String rateAmount;
    private Double insurt;
    private Double insamt;
    private Double insMinChg;
    private Long totalcuft;
    private Integer totalpieces;
    private String databaseSchema;
    DecimalFormat df = new DecimalFormat("#########.00");
    private Map chargesInfoMap = new HashMap();
    private Map totalPerMap = new HashMap();
    private List<ChargesInfoBean> quoteRateList = new ArrayList();
    private List<LclQuoteAc> quoteAcList = new ArrayList();
    private List<ChargesInfoBean> chargesInfoList = new ArrayList();
    private String ttChargeCode1;
    private String ttChargeCode2;
    private String portDifferential;
    private LCLRatesDAO lclratesdao;
    private LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
    private PortsDAO portsdao = new PortsDAO();
    private GlMappingDAO glmappingdao = new GlMappingDAO();
    private LclQuotePieceDAO lclquotepiecedao = new LclQuotePieceDAO();
    private LCLQuoteDAO lCLQuoteDAO = new LCLQuoteDAO();
    private int countWithBarrell = 0, countWithoutBarrell = 0;
    private String engmet, toZip, pickupDisable;
    private BigDecimal weight = new BigDecimal(0.000);
    private BigDecimal measure = new BigDecimal(0.000);
    private BigDecimal zerobigdecimalvalue = new BigDecimal(0.00);
    private LclUtils lclUtils = new LclUtils();
    private ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
    private String ofratebasis = new String();
    private String stdchgratebasis = new String();
    private String highVolumeMessage = new String();
    private Double measureForI;
    private Double weightForI;
    private Double measureForM;
    private Double weightForM;
    private String rateuom;
    private String label1;
    private String label2;
    private String label;
    private Ports ports;
    private LclFileNumber lclFileNumber;
    private Double totalSpotWeight = 0.0;
    private boolean ttFound = false;
    private boolean isArGlmappingFlag = false;
    private String glMappingBlueChgCode;

    public List<LclQuoteAc> getQuoteAcList() {
        return quoteAcList;
    }

    public void setQuoteAcList(List<LclQuoteAc> quoteAcList) {
        this.quoteAcList = quoteAcList;
    }

    public Ports getPorts() {
        return ports;
    }

    public void setPorts(Ports ports) {
        this.ports = ports;
    }

    public String getOfratebasis() {
        return ofratebasis;
    }

    public void setOfratebasis(String ofratebasis) {
        this.ofratebasis = ofratebasis;
    }

    public String getStdchgratebasis() {
        return stdchgratebasis;
    }

    public void setStdchgratebasis(String stdchgratebasis) {
        this.stdchgratebasis = stdchgratebasis;
    }

    public String getHighVolumeMessage() {
        return highVolumeMessage;
    }

    public void setHighVolumeMessage(String highVolumeMessage) {
        this.highVolumeMessage = highVolumeMessage;
    }

    public Double getMeasureForI() {
        return measureForI;
    }

    public void setMeasureForI(Double measureForI) {
        this.measureForI = measureForI;
    }

    public Double getMeasureForM() {
        return measureForM;
    }

    public void setMeasureForM(Double measureForM) {
        this.measureForM = measureForM;
    }

    public String getRateuom() {
        return rateuom;
    }

    public void setRateuom(String rateuom) {
        this.rateuom = rateuom;
    }

    public Double getWeightForI() {
        return weightForI;
    }

    public void setWeightForI(Double weightForI) {
        this.weightForI = weightForI;
    }

    public Double getWeightForM() {
        return weightForM;
    }

    public void setWeightForM(Double weightForM) {
        this.weightForM = weightForM;
    }

    public String getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(String rateAmount) {
        this.rateAmount = rateAmount;
    }

    public Double getTotalSpotWeight() {
        return totalSpotWeight;
    }

    public void setTotalSpotWeight(Double totalSpotWeight) {
        this.totalSpotWeight = totalSpotWeight;
    }

    public boolean isIsArGlmappingFlag() {
        return isArGlmappingFlag;
    }

    public void setIsArGlmappingFlag(boolean isArGlmappingFlag) {
        this.isArGlmappingFlag = isArGlmappingFlag;
    }

    public String getGlMappingBlueChgCode() {
        return glMappingBlueChgCode;
    }

    public void setGlMappingBlueChgCode(String glMappingBlueChgCode) {
        this.glMappingBlueChgCode = glMappingBlueChgCode;
    }

    /**
     *
     * @param lclQuotePiecesList - List Of Selected Commodities
     * @param origin - Selected Origin
     * @param dest - Selected Destination
     * @param ports - Port for Selected Destination
     * @param comminfobean - Selected Commodity Information
     * @param carrier - Selected Carrier for Pick up
     * @param overDimList - Selected Over Dimensional
     */
    public void calculateRates(String origin, String destination, String pol, String pod, Long fileNumberId, List lclQuotePiecesList, User user,
            String pickupYesNo, String insuranceYesNo, BigDecimal valueOfGoods, String rateType, String buttonValue, RoutingOptionsBean routingbean,
            String pickupReadyDate, String fromZip, HttpSession session, Boolean calcHeavy, String deliveryMetro, String pcBoth, String unLocationCode,
            String radioValue, HttpServletRequest request) throws Exception {
        String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";

        // this logic applied for mirror city which included in unlocation 
        UnLocation originCode = new UnLocationDAO().getUnlocation(origin);
        UnLocation fdCode = new UnLocationDAO().getUnlocation(destination);
        UnLocation polCode = new UnLocationDAO().getUnlocation(pol);
        UnLocation podCode = new UnLocationDAO().getUnlocation(pod);

        origin = originCode != null && originCode.getLclRatedSourceId() != null ? originCode.getLclRatedSourceId().getUnLocationCode() : origin;
        destination = fdCode != null && fdCode.getLclRatedSourceId() != null ? fdCode.getLclRatedSourceId().getUnLocationCode() : destination;
        pol = polCode != null && polCode.getLclRatedSourceId() != null ? polCode.getLclRatedSourceId().getUnLocationCode() : pol;
        pod = podCode != null && podCode.getLclRatedSourceId() != null ? podCode.getLclRatedSourceId().getUnLocationCode() : pod;
        session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();

        boolean unbarrell = false;
        LclBookingExport lclBookingExport = null;
        LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        pickupDisable = LoadLogisoftProperties.getProperty("application.enableCTS");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        LclWarehsDAO lclWarehsDAO = new LclWarehsDAO();
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        Zipcode zipcode = null;
        RefTerminal refterminal = null;
        LclQuotePad lclQuotePad = null;
        if (buttonValue.equalsIgnoreCase("C")) {
            refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                toZip = refterminal.getZipcde();
                pooorigin = refterminal.getTrmnum();
                ofratebasis += pooorigin + "-";
            }
        } else if (buttonValue.equalsIgnoreCase("R")) {
            refterminal = refterminaldao.findById(origin);
            if (pickupDisable.equalsIgnoreCase("Y")) {
                lclWarehsDAO = new LclWarehsDAO(databaseSchema);
                Object warehs[] = lclWarehsDAO.LclDeliverCargo(unLocationCode, "W");
                if (null != warehs && null != warehs[5]) {
                    toZip = warehs[5].toString();
                    routingbean.setToZip(toZip);
                }
            }
            pooorigin = origin;
        }
        RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(pol, rateType);
        if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
            polorigin = refterminalpol.getTrmnum();
        }
        String comnum = new String();
        Object[] stdchargesObj = null;
        ports = portsdao.getByProperty("unLocationCode", destination);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            destinationfd = ports.getEciportcode();
            if (buttonValue.equalsIgnoreCase("C")) {
                ofratebasis += destinationfd + "-";
            }
            engmet = ports.getEngmet();
        }
        Ports portspod = portsdao.getByProperty("unLocationCode", pod);
        if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
            destinationpod = portspod.getEciportcode();
        }
        if ((refterminal != null || refterminalpol != null) && (ports != null || portspod != null)) {
            lclUtils.calculateRatesForQuotes(refterminal, ports, lclQuotePiecesList, lclratesdao, engmet, fileNumberId);
            if (buttonValue.equalsIgnoreCase("C")) {
                highVolumeMessage = lclUtils.getHighVolumeMessage();
            }
            //Get the count of the selected Commodity
            calculateCount(lclQuotePiecesList);
            //Check the Fields Of T&T Charges
            if (refterminal != null && refterminal.getGenericCode1() != null && refterminal.getGenericCode1().getCode() != null
                    && !refterminal.getGenericCode1().getCode().trim().equals("")) {
                this.setTtChargeCode1(refterminal.getGenericCode1().getCode());
            }
            boolean includedestinationfee = false;
            if (fileNumberId != null && fileNumberId > 0) {
                lclBookingExport = lclBookingExportDAO.getLclBookingExportByFileNumber(fileNumberId);
            }
            if (null != lclBookingExport) {
                if (lclBookingExport.isIncludeDestfees()) {
                    includedestinationfee = lclBookingExport.isIncludeDestfees();
                }
            } else {
                includedestinationfee = lclSession.isIncludeDestfees();
            }
            //Calculate the Ocean Freight Rate
            calculateOfRate(countWithBarrell, pooorigin, polorigin, destinationpod, destinationfd, fileNumberId, lclQuotePiecesList, user, refterminal, buttonValue, routingbean);
            int index = 0;
            //Loop through the selected Commodity
            for (int j = 0; j < lclQuotePiecesList.size(); j++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
                if (fileNumberId != null && fileNumberId > 0 && lclQuotePiece.getCommodityType() != null) {
                    comnum = lclQuotePiece.getCommodityType().getCode();
                } else {
                    comnum = lclQuotePiece.getCommNo();
                }
                if (buttonValue.equalsIgnoreCase("C") && j == 0) {
                    ofratebasis += comnum;
                }
                //Checking whether the object has commodity code
                //need to check
                if (lclQuotePiece.getActualWeightImperial() != null) {
                    weight = weight.add(lclQuotePiece.getActualWeightImperial());
                } else if (lclQuotePiece.getBookedWeightImperial() != null) {
                    weight = weight.add(lclQuotePiece.getBookedWeightImperial());
                }
                if (lclQuotePiece.getActualVolumeImperial() != null) {
                    measure = measure.add(lclQuotePiece.getActualVolumeImperial());
                } else if (lclQuotePiece.getBookedVolumeImperial() != null) {
                    measure = measure.add(lclQuotePiece.getBookedVolumeImperial());
                }
                if ((lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")
                        && lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals(""))
                        || lclQuotePiece.isIsBarrel()) {
                    //ports = dest.getPorts();
                    index++;
                    LclQuotePiece commbeanforextralen = (LclQuotePiece) lclQuotePiecesList.get(0);

                    if (!lclQuotePiece.isIsBarrel()) {
                        unbarrell = true;
                        /**
                         * Calculating total weights and total measures for
                         * calculating rates other than Ocean Freight
                         */
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if (engmet != null) {
                            if (engmet.equals("E")) {
                                if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                                } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                                }
                                if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                                } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                                }
                            } else if (engmet.equals("M")) {
                                if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                                } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                                }
                                if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                                } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                                }
                            }
                        }
                        if (j == 0) {
                            measureForI = weightMeasure;
                            weightForI = weightDouble;
                            measureForM = weightMeasure;
                            weightForM = weightDouble;
                            rateuom = engmet;
                        }

                        //calculate the Total Weight Of Commodities
                        totalWeight = totalWeight + weightDouble;
                        //calculate the Total Measure Of Commodities
                        totalMeasure = totalMeasure + weightMeasure;
                        //Getting  the stdchg object for corresponding commodity
                        //Get the destination FD from visit
                        //Getting the stdcharges using pooorigin and destinationfd and comnum
                        stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, comnum);
                        String stdChgRateBasis = ofratebasis;
                        //stdcharges = stdcharges = stdchargesdao.findByOrgnDestComCde(pooorigin, destinationfd, comnum);
                        //If the object is null Getting the stdcharges using pooorigin and destinationfd and 0 commodity code
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, "000000");
                            stdChgRateBasis = pooorigin + "-" + destinationfd + "-000000";
                        }//end of stdcharges null checking
                        //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
                        if (stdchargesObj == null) {
                            //Get the destination POD from visit
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationpod, comnum);
                            stdChgRateBasis = pooorigin + "-" + destinationpod + "-" + comnum;
                        }
                        //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationpod, "000000");
                            stdChgRateBasis = pooorigin + "-" + destinationpod + "-000000";
                        }
                        //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationfd, comnum);
                            stdChgRateBasis = polorigin + "-" + destinationfd + "-" + comnum;
                        }
                        //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationfd, "000000");
                            stdChgRateBasis = polorigin + "-" + destinationfd + "-000000";
                        }
                        //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationpod, comnum);
                            stdChgRateBasis = polorigin + "-" + destinationpod + "-" + comnum;

                        }
                        //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationpod, "000000");
                            stdChgRateBasis = polorigin + "-" + destinationpod + "-000000";
                        }

                        lclQuotePiece.setStdchgRateBasis(stdChgRateBasis);
                        if (j == 0) {
                            this.setStdchgratebasis(stdChgRateBasis);
                        }
                    } //end of barrell if condition
                    //If the object is not null performing the rate calculation
                    if (stdchargesObj != null && !lclQuotePiece.isIsBarrel()) {
                        //Getting All the Charge Codes For Calulating Rates
                        String chgcode = getAddedChgCode(stdchargesObj, lclQuotePiece, lclQuotePiecesList, insuranceYesNo, valueOfGoods, calcHeavy, buttonValue, deliveryMetro, pcBoth, ports, includedestinationfee);
                        if (chgcode != null && !chgcode.trim().equals("")) {
                            //lclQuotePiece.setPerCftCbm(lclQuotePiece.getPerCftCbm());
                            //lclQuotePiece.setPerLbsKgs(lclQuotePiece.getPerLbsKgs());
                            StringTokenizer tokenizer = new StringTokenizer(chgcode, ",");
                            while (tokenizer.hasMoreTokens()) {
                                String splittedchgcode = tokenizer.nextToken();
                                List prtChgsCommodityList = lclratesdao.findByChdcod(
                                        pooorigin, destinationfd, comnum, splittedchgcode);
                                List prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                        pooorigin, destinationfd, "000000", splittedchgcode);
                                if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                                        && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                                    prtChgsCommodityList = lclratesdao.findByChdcod(
                                            pooorigin, destinationpod, comnum, splittedchgcode);
                                    prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                            pooorigin, destinationpod, "000000", splittedchgcode);
                                }
                                if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                                        && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                                    prtChgsCommodityList = lclratesdao.findByChdcod(
                                            polorigin, destinationfd, comnum, splittedchgcode);
                                    prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                            polorigin, destinationfd, "000000", splittedchgcode);
                                }
                                if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                                        && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                                    prtChgsCommodityList = lclratesdao.findByChdcod(
                                            polorigin, destinationpod, comnum, splittedchgcode);
                                    prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                            polorigin, destinationpod, "000000", splittedchgcode);
                                }
                                //Compare  portcharges list and zero portcharges list and get the final test
                                List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
                                //Check portChargesList null
                                if (portChargesList != null && portChargesList.size() > 0) {
                                    //Loop through portChargesList
                                    for (int i = 0; i < portChargesList.size(); i++) {
                                        Object[] portcharges = (Object[]) portChargesList.get(i);
                                        //Calculate Rates for all portcharges
                                        setRates(portcharges, lclQuotePiece, comnum, index, countWithoutBarrell, fileNumberId, routingbean, buttonValue);
                                    }//end of portcharges for loop
                                }//end of if condition portChargesList null checking
                            }//end of sttingtokenizer while loop
                        }//end of if condition chgcode

                    } //end of if condition stdcharges null checking

                } //end of if commodity code null
            } //end of commodity info list for
            if (buttonValue.equalsIgnoreCase("C")) {
                modifyOfrate(fileNumberId);
            }
            rolledUpWeight = 0.00;
            rolledUpMeasure = 0.00;
            rolledUpMinchg = 0.00;
            if (chargesInfoMap != null && chargesInfoMap.size() > 0) {
                Iterator chargesInfoIterator = chargesInfoMap.values().iterator();
                while (chargesInfoIterator.hasNext()) {
                    ChargesInfoBean cinfobean = (ChargesInfoBean) chargesInfoIterator.next();
                    if (cinfobean.getChargesDesc()!= null) {
                        if (!cinfobean.getChargesDesc().equals("OCEAN FREIGHT")) {
                            chargesInfoList.add(cinfobean);
                        } else {
                            rolledUpWeight += cinfobean.getWeightRate().doubleValue();
                            rolledUpMeasure += cinfobean.getMeasureRate().doubleValue();
                            rolledUpMinchg = cinfobean.getMinCharge().doubleValue();
                        }
                        if (CommonUtils.isNotEmpty(cinfobean.getChargesDesc()) && cinfobean.getChargesDesc().substring(0, 2).equalsIgnoreCase("TT")) {
                            rolledUpWeight += cinfobean.getWeightRate().doubleValue();
                            rolledUpMeasure += cinfobean.getMeasureRate().doubleValue();
                        }
                    }
                }
                if (buttonValue.equalsIgnoreCase("R")) {
                    routingbean.setOfrateAmount("$" + NumberUtils.convertToTwoDecimal(rolledUpMeasure) + "/$"
                            + NumberUtils.convertToTwoDecimal(rolledUpWeight) + "/$" + NumberUtils.convertToTwoDecimal(rolledUpMinchg) + " Min");
                }
            }
            if (insuranceYesNo != null && insuranceYesNo.equalsIgnoreCase("Y") && valueOfGoods != null) {
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                calculateInsuaranceChargeForRadio(pooorigin, polorigin, destinationfd, destinationpod, lclQuotePiecesList,
                        valueOfGoods.doubleValue(), user, fileNumberId, null, glmapping, buttonValue);
                //calculateInsurance(valueOfGoods.doubleValue(), buttonValue, null, fileNumberId, lclQuotePiecesList, null, glmapping);
            }
            /*
             }
             }//end of commodity info list if
             ChargesInfoBean chargesInfoBean = new ChargesInfoBean();
             Double documentAmount = 0.0;
             if(bookingInfoBean.getDocChargesYesorNo() != null && !bookingInfoBean.getDocChargesYesorNo().equals("") && bookingInfoBean.getDocChargesYesorNo().equals("Yes")) {
             if (bookingInfoBean.getDocamount() != null && !bookingInfoBean.getDocamount().equals("")) {
             documentAmount = bookingInfoBean.getDocamount();
             }
             chargesInfoBean.setChargesDesc("DOCUMENTATION CHARGE");
             chargesInfoBean.setChargeCode(78);
             BigDecimal d = new BigDecimal(documentAmount);
             chargesInfoBean.setRate(df.format(d));
             chargesInfoBean.setLabel1("");
             chargesInfoBean.setLabel2("");
             chargesInfoList.add(chargesInfoBean);
             addLinesForCharges();
             addTotal(comminfobean, bookingInfoBean, visit);*/

            String realPath = session.getServletContext().getRealPath("/xml/");
            String fileName = "ctsresponse" + session.getId() + ".xml";
            CallCTSWebServices ctsweb = new CallCTSWebServices();

            if (buttonValue.equalsIgnoreCase("R")) {
                lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip, pickupReadyDate,
                        "" + weight, "" + measure, "CARRIER_CHARGE", null, "Exports");
            } else if (buttonValue.equalsIgnoreCase("C")) {
                if (fileNumberId != null && fileNumberId > 0) {
                    lclQuotePad = lclQuotePadDAO.getLclQuotePadByFileNumber(fileNumberId);
                }
                if (pickupDisable.equalsIgnoreCase("Y") && CommonUtils.isNotEmpty(pickupYesNo) && pickupYesNo.equalsIgnoreCase("Y")
                        && lclQuotePad != null && CommonUtils.isNotEmpty(lclQuotePad.getScac()) && CommonUtils.isNotEmpty(pickupReadyDate)) {
                    lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip,
                            pickupReadyDate, "" + weight, "" + measure, "CARRIER_CHARGE", "CARRIER_COST", "Exports");
                }
            }
            List carrierList = lclSession.getCarrierList();
            List<Carrier> carrierCostList = lclSession.getCarrierCostList();
            if (buttonValue.equalsIgnoreCase("C")) {
                if (carrierList != null && carrierList.size() > 0 && carrierCostList != null && carrierCostList.size() > 0) {
                    savePickupCharge(fileNumberId, user, carrierList, carrierCostList, lclQuotePad);
                } else if (fileNumberId != null && fileNumberId > 0 && lclQuotePad != null) {
                    lclQuotePadDAO.update(lclQuotePad);
                }
                saveCharges(fileNumberId, user, buttonValue, includedestinationfee);
                reCalculateManualCharges(fileNumberId, user);
                lclSession.setIsArGlmappingFlag(isArGlmappingFlag);
                lclSession.setGlMappingBlueCode(glMappingBlueChgCode);
                if (unbarrell) {
                    if (totalPerMap != null && totalPerMap.size() > 0) {
                        addTotalPercentage(fileNumberId, user);
                    }
                }
            } else if (buttonValue.equalsIgnoreCase("R")) {
                routingbean.setQuoteRateList(quoteRateList);
                if (pickupDisable.equalsIgnoreCase("Y")) {
                    if (carrierList != null && carrierList.size() > 0) {
                        routingbean.setTotalAmount(NumberUtils.convertToTwoDecimal(calculateTotalByQuoteRateList()));
                        routingbean.setHiddenTotalAmount(routingbean.getTotalAmount());
                        String propertyValue = new LoadLogisoftProperties().getProperty("CTS carriers use lowest rates");
                        int defaultIndex = CommonUtils.isEmpty(propertyValue) || "0".equalsIgnoreCase(propertyValue)
                                ? 1 : Integer.parseInt(propertyValue) - 1;
                        defaultIndex = carrierList.size() > defaultIndex ? defaultIndex : 1;
                        defaultIndex=carrierList.size()==1 ?1:defaultIndex;
                        Carrier carrier =(Carrier)carrierList.get(defaultIndex);
                        
                        if (carrier != null) {
                            if (CommonUtils.isNotEmpty(carrier.getFinalcharge())) {
                                routingbean.setCtsAmount("$" + carrier.getFinalcharge());
                                Double total = Double.parseDouble(routingbean.getTotalAmount()) + Double.parseDouble(carrier.getFinalcharge());
                                routingbean.setTotalAmount(NumberUtils.convertToTwoDecimal(total));
                                routingbean.setHiddenTotalAmount(routingbean.getTotalAmount());
                            }
                            if (CommonUtils.isNotEmpty(carrier.getScac())) {
                                routingbean.setScac("(" + carrier.getScac() + ")");
                            }
                            if (CommonUtils.isNotEmpty(carrier.getDays())) {
                                routingbean.setDays(carrier.getDays());
                            }
                        }

                    }
                }
            }
        }//end of refterminal and ports if condition
    }//end of method

    public void calculateCount(List lclQuotePiecesList) {
        if (lclQuotePiecesList != null && lclQuotePiecesList.size() > 0) {
            for (int j = 0; j < lclQuotePiecesList.size(); j++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
                if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")
                        && lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                    countWithBarrell++;
                    if (!lclQuotePiece.isIsBarrel()) {
                        countWithoutBarrell++;
                    }
                }
            }
        }
    }

    /**
     * @param stdcharges - Stdcharge for the commodity
     * @param comminfobean - commodity information
     * @param lclQuotePiecesList - Commodity List
     * @param ports
     * @param overDimList
     * @return - Charge Codes with comma seperated values This method returns a
     * colletion of charge codes with comm seperated values
     */
    public String getAddedChgCode(Object[] stdcharges, LclQuotePiece lclQuotePiece,
            List lclQuotePiecesList, String insuranceYesNo, BigDecimal valueOfGoods, Boolean calcHeavy, String buttonValue, String deliveryMetro, String pcBoth, Ports ports, boolean includedestinationfee) throws Exception {
        String chgcod = "";
        String overDimChgCdeForWeight = "";
        String overDimChgCdeForMeasure = "";
        String pcb = new String();
        boolean densecargofound = false;
        if (null != calcHeavy && calcHeavy) {
            // if (buttonValue.equalsIgnoreCase("C") && calcHeavy != null && calcHeavy.equals(true)) {
            //one piece weighing more than 4000 lbs (convert 2000 kgs to lbs and get 4409 lbs, for example) Mantis#7818
            overDimChgCdeForWeight = getChargeCodeForHeavyLiftCharge(lclQuotePiecesList);
            //Dims 180 inches Mantis#7822
            overDimChgCdeForMeasure = getchgCdeForExtraLengthCharge(lclQuotePiece.getLclQuotePieceDetailList());
            // }
            //total shipment is 1000 kgs or greaterand the ratio of kgs to cbm is 1000 or greater Mantis#7826
            densecargofound = getchgCdeForDenseCargo(lclQuotePiecesList);
            if (densecargofound) {
                chgcod = chgcod + "0251,";
            }
        }
        //Concat Charge Codes with commas
        if (stdcharges != null) {
            if (stdcharges[0] != null && !stdcharges[0].toString().trim().equals("") && Integer.parseInt(stdcharges[0].toString()) > 0
                    && !stdcharges[0].equals("0015") && !stdcharges[0].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[0].toString()) + ",";
                pcb = stdcharges[12].toString();
            }
            if (stdcharges[1] != null && !stdcharges[1].toString().trim().equals("") && Integer.parseInt(stdcharges[1].toString()) > 0
                    && !stdcharges[1].equals("0015") && !stdcharges[1].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[1].toString()) + ",";
                pcb = stdcharges[13].toString();
            }
            if (stdcharges[2] != null && !stdcharges[2].toString().trim().equals("") && Integer.parseInt(stdcharges[2].toString()) > 0
                    && !stdcharges[2].equals("0015") && !stdcharges[2].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[2].toString()) + ",";
                pcb = stdcharges[14].toString();
            }
            if (stdcharges[3] != null && !stdcharges[3].toString().trim().equals("") && Integer.parseInt(stdcharges[3].toString()) > 0
                    && !stdcharges[3].equals("0015") && !stdcharges[3].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[3].toString()) + ",";
                pcb = stdcharges[15].toString();
            }
            if (stdcharges[4] != null && !stdcharges[4].toString().trim().equals("") && Integer.parseInt(stdcharges[4].toString()) > 0
                    && !stdcharges[4].equals("0015") && !stdcharges[4].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[4].toString()) + ",";
                pcb = stdcharges[16].toString();
            }
            if (stdcharges[5] != null && !stdcharges[5].toString().trim().equals("") && Integer.parseInt(stdcharges[5].toString()) > 0
                    && !stdcharges[5].equals("0015") && !stdcharges[5].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[5].toString()) + ",";
                pcb = stdcharges[17].toString();
            }
            if (stdcharges[6] != null && !stdcharges[6].toString().trim().equals("") && Integer.parseInt(stdcharges[6].toString()) > 0
                    && !stdcharges[6].equals("0015") && !stdcharges[6].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[6].toString()) + ",";
                pcb = stdcharges[18].toString();
            }
            if (stdcharges[7] != null && !stdcharges[7].toString().trim().equals("") && Integer.parseInt(stdcharges[7].toString()) > 0
                    && !stdcharges[7].equals("0015") && !stdcharges[7].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[7].toString()) + ",";
                pcb = stdcharges[19].toString();
            }
            if (stdcharges[8] != null && !stdcharges[8].toString().trim().equals("") && Integer.parseInt(stdcharges[8].toString()) > 0
                    && !stdcharges[8].equals("0015") && !stdcharges[8].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[8].toString()) + ",";
                pcb = stdcharges[20].toString();
            }
            if (stdcharges[9] != null && !stdcharges[9].toString().trim().equals("") && Integer.parseInt(stdcharges[9].toString()) > 0
                    && !stdcharges[9].equals("0015") && !stdcharges[9].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[9].toString()) + ",";
                pcb = stdcharges[21].toString();
            }
            if (stdcharges[10] != null && !stdcharges[10].toString().trim().equals("") && Integer.parseInt(stdcharges[10].toString()) > 0
                    && !stdcharges[10].equals("0015") && !stdcharges[10].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[10].toString()) + ",";
                pcb = stdcharges[22].toString();
            }
            if (stdcharges[11] != null && !stdcharges[11].toString().trim().equals("") && Integer.parseInt(stdcharges[11].toString()) > 0
                    && !stdcharges[11].equals("0015") && !stdcharges[11].equals("0060")) {
                chgcod = chgcod + Integer.parseInt(stdcharges[11].toString()) + ",";
                pcb = stdcharges[23].toString();
            }

        }

        if (lclQuotePiece.isHazmat()) {
            chgcod = chgcod + "119,";
        }
        if (insuranceYesNo != null && insuranceYesNo.equalsIgnoreCase("Y") && valueOfGoods != null) {
            chgcod = chgcod + "6,";
        }

        if (deliveryMetro != null) {
            if (deliveryMetro.equalsIgnoreCase("O")) {
                chgcod = chgcod + "0060,";
            } else if (deliveryMetro.equalsIgnoreCase("I")) {
                chgcod = chgcod + "0015,";
            }
        }
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(ports.getUnLocationCode());
        if (ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")) {
            if (pcBoth != null && pcBoth.equalsIgnoreCase("C")) {
                String destid = ports.getEciportcode();
                if (destid != null) {
                    if (destid.equals("050") || destid.equals("060")) {
                        chgcod = chgcod + "95,";
                    } else {
                        chgcod = chgcod + "5,";
                    }
                }
            }
        }
        if (overDimChgCdeForWeight != null && !overDimChgCdeForWeight.trim().equals("")) {
            chgcod = chgcod + overDimChgCdeForWeight + ",";
        }
        if (overDimChgCdeForMeasure != null && !overDimChgCdeForMeasure.trim().equals("")) {
            chgcod = chgcod + overDimChgCdeForMeasure + ",";
        }
        if (includedestinationfee) {
            chgcod = chgcod + "350," + "351,";
        }
        if (chgcod != null && !chgcod.equals("")) {
            chgcod = chgcod.substring(0, chgcod.length() - 1);
        }
        this.setPcb(pcb);
        return chgcod;
    }

    /**
     *
     * @param portChargesList - Port Charges List got by Commodity Number
     * @param portChargesZeroList - Port Charges List got by Zero Commodity
     * @return Merged PortCharges List This method compares two List and merges
     * two list depending on requirement
     */
    public List getPortChargesList(List portChargesList, List portChargesZeroList) {
        if (portChargesList != null && portChargesZeroList != null
                && portChargesList.size() > 0 && portChargesZeroList.size() > 0) {
            for (int i = 0; i < portChargesZeroList.size(); i++) {
                Object[] portChargeOuter = (Object[]) portChargesZeroList.get(i);
                for (int j = 0; j < portChargesList.size(); j++) {
                    Object[] portChargeInner = (Object[]) portChargesList.get(j);
                    if (portChargeOuter[0] != null && portChargeOuter[1] != null
                            && portChargeOuter[0].equals(portChargeInner[0])) {
                        portChargesZeroList.remove(i);
                        continue;
                    }
                }
            }
        }
        if (portChargesList == null) {
            portChargesList = new ArrayList();
        }
        if (portChargesZeroList != null && portChargesZeroList.size() > 0) {
            for (int i = 0; i < portChargesZeroList.size(); i++) {
                Object[] portCharges = (Object[]) portChargesZeroList.get(i);
                portChargesList.add(portCharges);
            }
        }
        return portChargesList;
    }

    /**
     * This method calculates the rates for all commodities
     */
    public void setRates(Object[] portcharges, LclQuotePiece lclQuotePiece, String comnum,
            int index, int count, Long fileNumberId, RoutingOptionsBean routingOptionsBean, String buttonValue) throws Exception {
        String chdCode = null;
        Integer chgType = null;
        String blueScreenChargeCode = null;
        if (portcharges != null) {
            if (portcharges[0] != null && !portcharges[0].toString().trim().equals("")) {
                chdCode = portcharges[0].toString();
            }
            if (portcharges[0] != null && !portcharges[1].toString().trim().equals("")) {
                chgType = Integer.parseInt(portcharges[1].toString());
            }
            //Charges charges = null;
            Double cbmrt = 0.0;
            Double kgsrt = 0.0;
            String chargesDesc = new String();
            if (chgType != null && chgType > 0) {
                if (chdCode != null) {
                    GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chdCode, "LCLE", "AR");
                    if (glmapping != null && glmapping.getChargeCode() != null) {
                        blueScreenChargeCode = glmapping.getChargeCode();
                        if (glmapping.getChargeCode().equalsIgnoreCase("PRTDIF")) {
                            this.setPortDifferential(glmapping.getChargeCode());
                        }
                        chargesDesc = glmapping.getChargeCode();
                    }
                }
                /**
                 * Calculating rates depending on various charge types
                 */
                //Calculating Rates if charge type is 1 (FLAT RATE)
                if (chgType == 1) {
                    Double flatrate = 0.0;
                    if (portcharges[2] != null && !portcharges[2].toString().trim().equals("")) {
                        flatrate = Double.parseDouble(portcharges[2].toString());
                    }
                    ChargesInfoBean cinfobean = new ChargesInfoBean();
                    cinfobean.setChargeType(chgType);
                    cinfobean.setChargesDesc(chargesDesc);
                    cinfobean.setCommodityCode(comnum);
                    cinfobean.setChargeCode(chdCode);
                    BigDecimal d = new BigDecimal(flatrate);
                    cinfobean.setRate(d);
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setRatePerUnit(d);
                    cinfobean.setRatePerUnitUom("FL");
                    cinfobean.setBookingPieceId(lclQuotePiece.getId());
                    if (count == index) {
                        if (!chargesInfoMap.containsKey(chargesDesc)) {
                            chargesInfoMap.put(chargesDesc, cinfobean);
                            quoteRateList.add(cinfobean);
                        }
                    } else {
                        quoteRateList.add(cinfobean);
                    }
                }//end of charge type 1 if condition
                //Calculating Rates if charge type is 2 and keep it in list

                if (chgType == 2) {
                    Double totper = 0.0;
                    Double minchg = 0.0;
                    if (portcharges[3] != null && !portcharges[3].toString().trim().equals("")) {
                        totper = Double.parseDouble(portcharges[3].toString());
                    }
                    if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")) {
                        minchg = Double.parseDouble(portcharges[6].toString());
                    }
                    if (count == index) {
                        if (!totalPerMap.containsKey(chargesDesc)) {
                            TotalPercentageBean totbean = new TotalPercentageBean();
                            totbean.setDesc(chargesDesc);
                            totbean.setMinchg(minchg);
                            totbean.setTotper(totper);
                            totbean.setChargeCode(chdCode);
                            totbean.setCommodityCode(comnum);
                            totbean.setChargeType(chgType);
                            totalPerMap.put(chargesDesc, totbean);
                        }
                    }
                }//end of charge type 2 if condition
                //Calculating Rates if charge type is 3 (O/F Rate and other)

                if (chgType == 3) {
                    if (chdCode != null) {
                        // calculating with total weight and total measure
                        if (!chdCode.equals("0001")) {
                            String rateUOM = new String();
                            Double cuftrt = 0.0;
                            Double wghtrt = 0.0;
                            Double minchg = 0.0;
                            Double ttcbmcftrate = 0.0;
                            Double ttlbskgsrate = 0.0;
                            Double ttcbmcft = 0.0;
                            Double ttlbskgs = 0.0;
                            Double calculatedWeight = 0.0;
                            Double calculatedMeasure = 0.0;
                            Double finalValue = 0.0;
                            BigDecimal weightDiv = null;
                            BigDecimal measureDiv = new BigDecimal(1000);
                            if (portcharges[4] != null && !portcharges[4].toString().trim().equals("")) {
                                cuftrt = Double.parseDouble(portcharges[4].toString());
                            }
                            if (portcharges[5] != null && !portcharges[5].toString().trim().equals("")) {
                                wghtrt = Double.parseDouble(portcharges[5].toString());
                            }
                            if (portcharges[9] != null && !portcharges[9].toString().trim().equals("")) {
                                cbmrt = Double.parseDouble(portcharges[9].toString());
                            }
                            if (portcharges[10] != null && !portcharges[10].toString().trim().equals("")) {
                                kgsrt = Double.parseDouble(portcharges[10].toString());
                            }
                            if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")) {
                                minchg = Double.parseDouble(portcharges[6].toString());
                            }
                            if (engmet != null) {
                                if (engmet.equalsIgnoreCase("E")) {
                                    weightDiv = new BigDecimal(100);
                                    //if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 100) * wghtrt;
                                    calculatedMeasure = totalMeasure * cuftrt;
                                    //}
                                    ttcbmcftrate = cuftrt;
                                    ttlbskgsrate = wghtrt;
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    weightDiv = new BigDecimal(1000);
                                    //if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 1000) * kgsrt;
                                    calculatedMeasure = totalMeasure * cbmrt;
                                    //}
                                    ttcbmcftrate = cbmrt;
                                    ttlbskgsrate = kgsrt;
                                }
                            }
                            // if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                            if (calculatedWeight >= calculatedMeasure) {
                                finalValue = calculatedWeight;
                                rateUOM = "W";
                            } else {
                                finalValue = calculatedMeasure;
                                rateUOM = "V";
                            }
                            if (finalValue <= minchg) {
                                finalValue = minchg;
                                rateUOM = "M";
                            }
//                            } else {
//                                finalValue = calculatedMeasure;
//                                rateUOM = "V";
//                                if (finalValue <= minchg) {
//                                    finalValue = minchg;
//                                    rateUOM = "M";
//                                }
//                            }
                            BigDecimal d = new BigDecimal(finalValue);
                            ChargesInfoBean cinfobean = new ChargesInfoBean();
                            cinfobean.setRatePerWeightUnit(new BigDecimal(ttlbskgsrate));
                            cinfobean.setRatePerWeightUnitDiv(weightDiv);
                            cinfobean.setRatePerVolumeUnit(new BigDecimal(ttcbmcftrate));
                            cinfobean.setRatePerVolumeUnitDiv(measureDiv);
                            cinfobean.setRatePerUnit(d);
                            cinfobean.setRatePerUnitUom(rateUOM);
                            cinfobean.setChargeCode(chdCode);
                            cinfobean.setChargesDesc(chargesDesc);
                            cinfobean.setChargeType(chgType);
                            cinfobean.setCommodityCode(comnum);
                            if (rateUOM.equals("W")) {
                                cinfobean.setRatePerUnitDiv(cinfobean.getRatePerWeightUnitDiv());
                            } else if (rateUOM.equals("V")) {
                                cinfobean.setRatePerUnitDiv(cinfobean.getRatePerVolumeUnitDiv());
                            }
                            cinfobean.setRate(d);
                            if (engmet != null) {
                                if (engmet.equalsIgnoreCase("E")) {
                                    cinfobean.setMeasureRate(cuftrt);
                                    cinfobean.setWeightRate(wghtrt);
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    cinfobean.setMeasureRate(cbmrt);
                                    cinfobean.setWeightRate(kgsrt);
                                }
                            }
                            cinfobean.setMinCharge(new BigDecimal(minchg));
                            cinfobean.setPcb(this.getPcb());
                            cinfobean.setBookingPieceId(lclQuotePiece.getId());
                            /*boolean ttchargecode1found = this.getTtChargeCode1() != null
                             && this.getTtChargeCode1().equals(blueScreenChargeCode);
                             boolean ttchargecode2found = this.getTtChargeCode2() != null
                             && this.getTtChargeCode2().equals(blueScreenChargeCode);
                             boolean portDifferentialfound = this.getPortDifferential() != null
                             && !this.getPortDifferential().equals("0000")
                             && this.getPortDifferential().equals(chdCode);*/
                            if (count == index) {
                                /*if (ttchargecode1found || ttchargecode2found || portDifferentialfound) {
                                 Double flatRateMinimum = 0.0;
                                 ChargesInfoBean ofratecinfobean = (ChargesInfoBean) chargesInfoMap.get("OCEAN FREIGHT");
                                 Double addedofrate = ofratecinfobean.getRate().doubleValue() + finalValue;
                                 ofratecinfobean.setRate(new BigDecimal(addedofrate));
                                 ttcbmcft = ofratecinfobean.getRatePerVolumeUnit().doubleValue() + ttcbmcftrate;
                                 ttlbskgs = ofratecinfobean.getRatePerWeightUnit().doubleValue() + ttlbskgsrate;
                                 if (ofratecinfobean.getMinCharge() != null && minchg != null) {
                                 flatRateMinimum = ofratecinfobean.getMinCharge().doubleValue() + minchg;
                                 ofratecinfobean.setMinCharge(new BigDecimal(flatRateMinimum));
                                 }
                                 ofratecinfobean.setRatePerWeightUnit(new BigDecimal(ttlbskgs));
                                 ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(ttcbmcft));
                                 if (buttonValue.equalsIgnoreCase("R")) {
                                 routingOptionsBean.setOfrateAmount("$" + NumberUtils.convertToTwoDecimal(ttcbmcft) + "/$"
                                 + NumberUtils.convertToTwoDecimal(ttlbskgs) + "/$" + NumberUtils.convertToTwoDecimal(flatRateMinimum) + " Min");
                                 }
                                 chargesInfoMap.put("OCEAN FREIGHT", ofratecinfobean);
                                
                                 } else { */
                                chargesInfoMap.put(chargesDesc, cinfobean);
                                quoteRateList.add(cinfobean);
//                                }
                            } else {
                                quoteRateList.add(cinfobean);
                            }
                        }//end of else condition
                    }//end of chdcode null if condition
                }//end of charge type 3 if condition
                //If charge type is 4 calculting Insurance
                if (chgType == 4 && !lclQuotePiece.isIsBarrel()) {
                    if (portcharges[7] != null && !portcharges[7].toString().trim().equals("")) {
                        insurt = Double.parseDouble(portcharges[7].toString());
                        this.setInsurt(insurt);
                    }
                    if (portcharges[8] != null && !portcharges[8].toString().trim().equals("")) {
                        insamt = Double.parseDouble(portcharges[8].toString());
                        this.setInsamt(insamt);
                    }
                }//end of charge type 4 if condition*/
            }
        }
    }

    /**
     *
     * @param lclQuotePiece
     * @param carrier This method calculates the total percentage and add it to
     * charges list
     */
    public void addTotalPercentage(Long fileNumberId, User user) throws Exception {
        Iterator totPerIter = totalPerMap.values().iterator();
        while (totPerIter.hasNext()) {
            TotalPercentageBean totbean = (TotalPercentageBean) totPerIter.next();
            Double finalValue = 0.0;
            if (totbean.getTotper() != null) {
                finalValue = calculateTotalForTotalPercentage(fileNumberId) * totbean.getTotper();
            }
            if (totbean.getMinchg() != null && totbean.getMinchg() > finalValue) {
                finalValue = totbean.getMinchg();
            }
            BigDecimal d = new BigDecimal(finalValue);
            ChargesInfoBean cinfobean = new ChargesInfoBean();
            cinfobean.setChargesDesc(totbean.getDesc());
            cinfobean.setRate(d);
            cinfobean.setChargeCode(totbean.getChargeCode());
            cinfobean.setCommodityCode(totbean.getCommodityCode());
            cinfobean.setChargeType(totbean.getChargeType());
            cinfobean.setMinCharge(new BigDecimal(totbean.getMinchg()));
            cinfobean.setRatePerUnitUom("PCT");
            cinfobean.setRatePerUnit(new BigDecimal(totbean.getTotper()));
            saveCAFCharge(cinfobean, fileNumberId, user);
//            chargesInfoList.add(cinfobean);
//            quoteRateList.add(cinfobean);
        }
    }

    /**
     * @param A - Entered Goods value This method calculates the insurance and
     * adds it to the charges list
     */
    public String calculateInsurance(Double A, String buttonValue, User user, Long fileNumberId, List<LclQuotePiece> lclCommodityList, LclQuoteAc lclQuoteAc, GlMapping glMapping) throws Exception {
        Double CIFValue = 0.0;
        Double B = 0.0;
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            if (lclCommodityList.size() == 1) {
                B = lclquoteacdao.getTotalLclCostWithoutParticularCharge(fileNumberId, glMapping.getId());
            } else {
                List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(fileNumberId, LCL_EXPORT);
                List<LclQuoteAc> rolledUpChargesList = exportQuoteUtils.getRolledUpChargesForQuote(lclCommodityList, chargeList, engmet, "No");
                B = lclUtils.calculateTotalWithoutInsuranceByQuoteAcList(rolledUpChargesList);

            }
        }
        if (B == 0.00) {
            B = calculateTotal();
        }
        Double C = 0.0;
        Double D = 0.0;
        Double insurt = 0.0;
        Double insamt = 0.0;
        Double insurance = 0.0;
        if (this.getInsurt() != null) {
            insurt = this.getInsurt();
        }
        if (this.getInsamt() != null) {
            insamt = this.getInsamt();
        }
        if (insamt != 0.0 && insurt != 0.0) {
            C = ((A + B) / insamt) * insurt;
        }
        D = (10.0 / 100.0) * (A + B + C);
        CIFValue = A + B + C + D;
        this.setCIFValue(String.valueOf(Math.round(CIFValue)));
        String cif = String.valueOf(Math.round(CIFValue));
        lCLQuoteDAO.updateCIF(fileNumberId, cif);
        if (insamt != null) {
            insurance = (CIFValue / 100) * insurt;
        }
        if(insurance<=insMinChg){
            insurance=insMinChg;
        }
        if (buttonValue.equalsIgnoreCase("I")) {
            Date d = new Date();
            if (lclQuoteAc == null) {
                lclQuoteAc = new LclQuoteAc();
                lclQuoteAc.setApBillToParty("C");
                lclQuoteAc.setArBillToParty("C");
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclQuoteAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclQuoteAc.setArglMapping(glMapping);
                lclQuoteAc.setEnteredBy(user);
                lclQuoteAc.setEnteredDatetime(d);
                lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclQuoteAc.setRatePerUnitUom("FL");
            }
            BigDecimal insuranceBigDecimal = new BigDecimal(insurance);
            lclQuoteAc.setTransDatetime(d);
            lclQuoteAc.setArAmount(insuranceBigDecimal);
            lclQuoteAc.setModifiedBy(user);
            lclQuoteAc.setModifiedDatetime(d);
            lclQuoteAc.setRatePerWeightUnit(new BigDecimal(insurt));
            lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(insamt));
            lclQuoteAc.setBundleIntoOf(false);
            lclQuoteAc.setPrintOnBl(true);
            lclquoteacdao.saveOrUpdate(lclQuoteAc);
        } else {
            ChargesInfoBean cinfobean = new ChargesInfoBean();
            cinfobean.setChargesDesc("INSURANCE");
            cinfobean.setRate(new BigDecimal(insurance));
            cinfobean.setChargeCode("0006");
            cinfobean.setChargeType(4);
            cinfobean.setRatePerUnitUom("FL");
            cinfobean.setRatePerWeightUnit(new BigDecimal(insurt));
            cinfobean.setRatePerWeightUnitDiv(new BigDecimal(insamt));
            chargesInfoList.add(cinfobean);
            quoteRateList.add(cinfobean);
            chargesInfoMap.put("INSURANCE", cinfobean);
        }
        return cif;
    }

    public Double calculateTotal() {
        Double total = 0.0;
        if (chargesInfoList != null && chargesInfoList.size() > 0) {
            for (int i = 0; i < chargesInfoList.size(); i++) {
                ChargesInfoBean cinfobean = (ChargesInfoBean) chargesInfoList.get(i);
                total = total + cinfobean.getRate().doubleValue();
            }
        }
        return total;
    }

    public Double calculateTotalByQuoteRateList() {
        Double total = 0.00;
        if (quoteRateList != null && quoteRateList.size() > 0) {
            for (int i = 0; i < quoteRateList.size(); i++) {
                ChargesInfoBean cinfobean = (ChargesInfoBean) quoteRateList.get(i);
                total = total + cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        return total;
    }

    public Double calculateTotalForSpotRate(String isTTCheck) {
        Double totalSpotMeasure = 0.0;
        if (chargesInfoMap != null && chargesInfoMap.size() > 0) {
            Iterator chargesInfoIterator = chargesInfoMap.values().iterator();
            while (chargesInfoIterator.hasNext()) {
                ChargesInfoBean cinfobean = (ChargesInfoBean) chargesInfoIterator.next();
                if (isTTCheck.equalsIgnoreCase("true") && CommonUtils.isNotEmpty(cinfobean.getChargesDesc()) && cinfobean.getChargesDesc().substring(0, 2).equalsIgnoreCase("TT")) {
                    totalSpotMeasure = cinfobean.getRatePerVolumeUnit().doubleValue();
                    totalSpotWeight = cinfobean.getRatePerWeightUnit().doubleValue();
                    ttFound = true;
                    break;
                } else if (cinfobean.getRatePerUnitUom().equalsIgnoreCase("V") || cinfobean.getRatePerUnitUom().equalsIgnoreCase("W")
                        || cinfobean.getRatePerUnitUom().equalsIgnoreCase("M")) {
                    totalSpotMeasure = totalSpotMeasure + cinfobean.getRatePerVolumeUnit().doubleValue();
                    totalSpotWeight = totalSpotWeight + cinfobean.getRatePerWeightUnit().doubleValue();
                }
            }
        }
        return totalSpotMeasure;
    }

    /**
     * This method calculates the total for total percentage
     *
     * @return
     */
    public Double calculateTotalForTotalPercentage(Long fileNumberId) throws Exception {
        Double total = 0.0;
        List<LclQuotePiece> lclQuotePieceList = null;
        if (fileNumberId == null || fileNumberId == 0) {
            if (chargesInfoList != null && chargesInfoList.size() > 0) {
                for (int i = 0; i < chargesInfoList.size(); i++) {
                    ChargesInfoBean cinfobean = (ChargesInfoBean) chargesInfoList.get(i);
                    String chargesDesc = cinfobean.getChargesDesc();
                    if (totalPerMap != null && !totalPerMap.containsKey(chargesDesc)) {
                        total = total + cinfobean.getRate().doubleValue();
                    }
                }
            }
        } else {
            lclQuotePieceList = lclquotepiecedao.findByProperty("lclFileNumber.id", fileNumberId);
            if (lclQuotePieceList.size() == 1) {
                total = Double.parseDouble(lclquoteacdao.getTotalLclCostByFileNumber(fileNumberId));
            } else {
                List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostByFileNumberAsc(fileNumberId, LCL_EXPORT);
                List<LclQuoteAc> lclQuoteAcList = exportQuoteUtils.getRolledUpChargesForQuote(lclQuotePieceList, chargeList, ports.getEngmet(), "No");
                total = Double.parseDouble(exportQuoteUtils.calculateTotalByQuoteAcList(lclQuoteAcList));
            }
        }
        return total;
    }

    public String getChargeCodeForHeavyLiftCharge(List lclQuotePiecesList) {
        Double avgWeight = 0.0;
        int pieces = 1;
        String chargeCode = "";
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        boolean thirdCndnSatisfied = false;
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount() != 0) {
                pieces = lclQuotePiece.getBookedPieceCount();
            } else {
                pieces = 1;
            }
            if (lclQuotePiece.getBookedWeightImperial() != null
                    && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                avgWeight = lclQuotePiece.getBookedWeightImperial().doubleValue() / pieces;
                if (avgWeight >= 4000 && avgWeight < 6000) {
                    firstCndnSatisfied = true;
                } else if (avgWeight >= 6000 && avgWeight < 8000) {
                    secondCndnSatisfied = true;
                } else if (avgWeight >= 8000) {
                    thirdCndnSatisfied = true;
                }
            }
        }
        if (thirdCndnSatisfied) {
            chargeCode = "0241";
        } else if (secondCndnSatisfied) {
            chargeCode = "0240";
        } else if (firstCndnSatisfied) {
            chargeCode = "0031";
        }
        return chargeCode;
    }

    public String getchgCdeForExtraLengthCharge(List<LclQuotePieceDetail> lclQuotePieceDetailList) {
        String chargeCode = "";
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        if (lclQuotePieceDetailList != null && lclQuotePieceDetailList.size() > 0) {
            for (int j = 0; j < lclQuotePieceDetailList.size(); j++) {
                LclQuotePieceDetail lclQuotePieceDetail = (LclQuotePieceDetail) lclQuotePieceDetailList.get(j);
                if (lclQuotePieceDetail.getActualLength() != null) {
                    if (lclQuotePieceDetail.getActualLength().doubleValue() >= 180
                            && lclQuotePieceDetail.getActualLength().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclQuotePieceDetail.getActualLength().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclQuotePieceDetail.getActualHeight() != null) {
                    if (lclQuotePieceDetail.getActualHeight().doubleValue() >= 180
                            && lclQuotePieceDetail.getActualHeight().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclQuotePieceDetail.getActualHeight().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclQuotePieceDetail.getActualWidth() != null) {
                    if (lclQuotePieceDetail.getActualWidth().doubleValue() >= 180
                            && lclQuotePieceDetail.getActualWidth().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclQuotePieceDetail.getActualWidth().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (secondCndnSatisfied) {
                    chargeCode = "242";
                } else if (firstCndnSatisfied) {
                    chargeCode = "32";
                }
            }
        }
        return chargeCode;
    }

    public boolean getchgCdeForDenseCargo(List lclQuotePieceList) throws Exception {
        boolean cndnSatisfied = false;
        if (engmet != null && engmet.equalsIgnoreCase("M")
                && lclQuotePieceList != null && lclQuotePieceList.size() > 0) {
            Double totalkgs = 0.0;
            Double totalcbm = 0.0;
            for (int j = 0; j < lclQuotePieceList.size(); j++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePieceList.get(j);
                if (lclQuotePiece.getBookedWeightMetric() != null
                        && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                    totalkgs += lclQuotePiece.getBookedWeightMetric().doubleValue();
                }
                if (lclQuotePiece.getBookedVolumeMetric() != null
                        && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalcbm += lclQuotePiece.getBookedVolumeMetric().doubleValue();
                }
            }//end of for loop
            String denseCargo = LoadLogisoftProperties.getProperty("DenseCargoFee");
            Double denseCargoKgs = null != denseCargo ? Double.parseDouble(denseCargo) : 1000;
            if (totalkgs >= denseCargoKgs && totalcbm != 0) {
                Double kgscbmratio = totalkgs / totalcbm;
                if (kgscbmratio >= 1000) {
                    cndnSatisfied = true;
                }
            }
        }//end of port if condition
        return cndnSatisfied;
    }

    public double convertToKgs(double val) {
        double result = 0.0;
        result = val / 2.20462;
        return result;
    }

    public String getCIFValue() {
        return CIFValue;
    }

    public void setCIFValue(String value) {
        CIFValue = value;
    }

    public String getPcb() {
        return pcb;
    }

    public void setPcb(String pcb) {
        this.pcb = pcb;
    }

    public List getQuoteRateList() {
        return quoteRateList;
    }

    public void setQuoteRateList(List quoteRateList) {
        this.quoteRateList = quoteRateList;
    }

    public Double getInsamt() {
        return insamt;
    }

    public void setInsamt(Double insamt) {
        this.insamt = insamt;
    }

    public Double getInsurt() {
        return insurt;
    }

    public void setInsurt(Double insurt) {
        this.insurt = insurt;
    }

    public Double getInsMinChg() {
        return insMinChg;
    }

    public void setInsMinChg(Double insMinChg) {
        this.insMinChg = insMinChg;
    }
    
    public List getChargesInfoList() {
        return chargesInfoList;
    }

    public void setChargesInfoList(List chargesInfoList) {
        this.chargesInfoList = chargesInfoList;
    }

    public Long getTotalcuft() {
        return totalcuft;
    }

    public void setTotalcuft(Long totalcuft) {
        this.totalcuft = totalcuft;
    }

    public Integer getTotalpieces() {
        return totalpieces;
    }

    public void setTotalpieces(Integer totalpieces) {
        this.totalpieces = totalpieces;
    }

    public String getTtChargeCode1() {
        return ttChargeCode1;
    }

    public void setTtChargeCode1(String ttChargeCode1) {
        this.ttChargeCode1 = ttChargeCode1;
    }

    public String getTtChargeCode2() {
        return ttChargeCode2;
    }

    public void setTtChargeCode2(String ttChargeCode2) {
        this.ttChargeCode2 = ttChargeCode2;
    }

    /**
     *
     * @param lclQuotePiecesList
     * @param count
     * @param ports
     * @param loctn This method Calculates OfRate and add the OfRate to the List
     */
    public void calculateOfRate(int count, String pooorigin, String polorigin, String destinationpod, String destinationfd,
            Long fileNumberId, List lclQuotePiecesList, User user, RefTerminal refterminal, String buttonValue, RoutingOptionsBean routingbean) throws Exception {
        Double minchg = 0.0;
        Double weight = 0.0;
        Double measure = 0.0;
        Double rateWeight = 0.0;
        Double rateMeasure = 0.0;
        String rateUOM = new String();
        int index = 0;
        BigDecimal dblBareel = new BigDecimal(0.00);
        BigDecimal dblTTA = new BigDecimal(0.00);
        BigDecimal weightDiv = null;
        BigDecimal measureDiv = new BigDecimal(1000);
        boolean ofratefound = false, spotratefound = false;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        Double finalValue = 0.0;
        String commodityCode = new String();
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            if (fileNumberId != null && fileNumberId > 0 && lclQuotePiece.getCommodityType() != null) {
                commodityCode = lclQuotePiece.getCommodityType().getCode();
            } else {
                commodityCode = lclQuotePiece.getCommNo();
                if (lclQuotePiece.isIsBarrel()) {
//                    index = 1;
                }
            }
            if (commodityCode == null) {
                commodityCode = lclQuotePiece.getCommNo();
            }
            boolean lbsKgsFound = false;
            if (lclQuotePiece.isIsBarrel()) {
                Object[] barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                if (barellObj == null) {
                    barellObj = lclratesdao.getBarrelRate(polorigin, destinationpod, commodityCode);
                }
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclQuotePiece.getBookedPieceCount().intValue();
                            }
                            if (lclQuotePiece.getActualPieceCount() != null && lclQuotePiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclQuotePiece.getActualPieceCount().intValue();
                            }
                        }
                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclQuotePiece.getBookedPieceCount().intValue();
                            }
                            if (lclQuotePiece.getActualPieceCount() != null && lclQuotePiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclQuotePiece.getActualPieceCount().intValue();
                            }

                        }
                    }
                }
            }//end of  isIsBarrel if condition
            //need to check here
            else {
                if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")
                        && lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                    index++;
                    ofratefound = true;
                    lbsKgsFound = true;
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            weightDiv = new BigDecimal(100);
                            if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weight = lclQuotePiece.getActualWeightImperial().doubleValue();
                            } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weight = lclQuotePiece.getBookedWeightImperial().doubleValue();
                            }
                            if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                measure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                            } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                measure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            weightDiv = new BigDecimal(1000);
                            if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weight = lclQuotePiece.getActualWeightMetric().doubleValue();
                            } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weight = lclQuotePiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                measure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                            } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                measure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                            }
                        }
                    }
                    if (lclQuotePiece.getOfratemin() != null && !lclQuotePiece.getOfratemin().trim().equals("")) {
                        minchg = Double.parseDouble(lclQuotePiece.getOfratemin());
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, commodityCode);
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, "000000");
                    }

                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, commodityCode);
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, "000000");
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, commodityCode);
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, "000000");
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, commodityCode);
                    }
                    if (minchg == 0.0) {
                        minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, "000000");
                    }
                    if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")) {
                        rateWeight = Double.parseDouble(lclQuotePiece.getPerLbsKgs());
                    }
                    if (lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                        rateMeasure = Double.parseDouble(lclQuotePiece.getPerCftCbm());
                    }
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            calculatedWeight = (weight / 100) * rateWeight;
                            if (weight >= 10000 && weight < 20000) {
                                if (refterminal != null && refterminal.getGenericCode3() != null && refterminal.getGenericCode3().getCode() != null && !refterminal.getGenericCode3().getCode().trim().equals("")) {
                                    this.setTtChargeCode2(refterminal.getGenericCode3().getCode());
                                }
                            } else if (weight >= 20000 && weight < 30000) {
                                if (refterminal != null && refterminal.getGenericCode4() != null && refterminal.getGenericCode4().getCode() != null && !refterminal.getGenericCode4().getCode().trim().equals("")) {
                                    this.setTtChargeCode2(refterminal.getGenericCode4().getCode());
                                }
                            }
                        } else if (engmet.equals("M")) {
                            calculatedWeight = (weight / 1000) * rateWeight;
                            Double convertedWeight = lclUtils.convertToLbs(weight).doubleValue();
                            if (convertedWeight >= 10000 && convertedWeight < 20000) {
                                if (refterminal != null && refterminal.getGenericCode3() != null && refterminal.getGenericCode3().getCode() != null && !refterminal.getGenericCode3().getCode().trim().equals("")) {
                                    this.setTtChargeCode2(refterminal.getGenericCode3().getCode());
                                }
                            } else if (weight >= 20000 && weight < 30000) {
                                if (refterminal != null && refterminal.getGenericCode4() != null && refterminal.getGenericCode4().getCode() != null && !refterminal.getGenericCode4().getCode().trim().equals("")) {
                                    this.setTtChargeCode2(refterminal.getGenericCode4().getCode());
                                }
                            }
                        }
                    }
                    calculatedMeasure = measure * rateMeasure;

                    if (calculatedWeight > calculatedMeasure) {
                        finalValue = calculatedWeight;
                        //label1 = "* O/F - TO WEIGHT *";
                        rateUOM = "FRW";
                    } else {
                        finalValue = calculatedMeasure;
                        //label1 = "* O/F - VOLUME *";
                        rateUOM = "FRV";
                    }
                    if (finalValue <= minchg) {
                        finalValue = minchg;
                        //label1 = "* O/F - MINIMUM *";
                        rateUOM = "FRM";
                    }
                    totalOceanFreight = totalOceanFreight + finalValue;
                }//end of lclBookingPiece.getPerLbsKgs() else condition
            }//end of isIsBarrel() else condition
            if (count == index) {
                if (ofratefound && lbsKgsFound) {
                    BigDecimal d = new BigDecimal(finalValue);
                    ChargesInfoBean cinfobean = new ChargesInfoBean();
                    cinfobean.setRatePerUnit(d);
                    cinfobean.setRatePerUnitUom(rateUOM);
                    cinfobean.setRatePerWeightUnit(new BigDecimal(rateWeight));
                    cinfobean.setRatePerWeightUnitDiv(weightDiv);
                    cinfobean.setRatePerVolumeUnit(new BigDecimal(rateMeasure));
                    cinfobean.setRatePerVolumeUnitDiv(measureDiv);
                    if (rateUOM.equals("FRW")) {
                        cinfobean.setRatePerUnitDiv(cinfobean.getRatePerWeightUnitDiv());
                    } else if (rateUOM.equals("FRV")) {
                        cinfobean.setRatePerUnitDiv(cinfobean.getRatePerVolumeUnitDiv());
                    }
                    cinfobean.setChargeCode("0001");
                    cinfobean.setChargesDesc("OCEAN FREIGHT");
                    cinfobean.setCommodityCode(lclQuotePiece.getCommodityType().getCode());
                    cinfobean.setChargeType(3);
                    cinfobean.setRate(d);
                    cinfobean.setMeasureRate(rateMeasure);
                    cinfobean.setWeightRate(rateWeight);
                    cinfobean.setMinCharge(new BigDecimal(minchg));
                    //cinfobean.setLabel1(label1);
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setBookingPieceId(lclQuotePiece.getId());
                    chargesInfoMap.put("OCEAN FREIGHT", cinfobean);
                    chargesInfoList.add(cinfobean);
                    quoteRateList.add(cinfobean);
                }
                if (totalBarrelCharges > 0.00) {
                    BigDecimal bigBarrelCharges = new BigDecimal(totalBarrelCharges);
                    ChargesInfoBean cinfobeanForBarell = new ChargesInfoBean();
                    String blueChgCodeBarr = glmappingdao.blueChgCodeForBarrel("OFBARR", "AR", "LCLE");
                    if (blueChgCodeBarr != null && blueChgCodeBarr != "") {
                        cinfobeanForBarell.setChargeCode(blueChgCodeBarr);
                    } else {
                        cinfobeanForBarell.setChargeCode("OFBARR");
                    }
                    cinfobeanForBarell.setChargesDesc("BARELL OFRATE");
                    cinfobeanForBarell.setCommodityCode(lclQuotePiece.getCommodityType().getCode());
                    // cinfobean.setChargeType(3);
                    cinfobeanForBarell.setRate(bigBarrelCharges);
                    cinfobeanForBarell.setRatePerUnitUom("FL");
                    cinfobeanForBarell.setRatePerWeightUnit(dblBareel);
                    cinfobeanForBarell.setBookingPieceId(lclQuotePiece.getId());
                    chargesInfoList.add(cinfobeanForBarell);
                    quoteRateList.add(cinfobeanForBarell);
                }
                if (totalBarellTTA > 0.00) {
                    BigDecimal bigBarrelTTACharges = new BigDecimal(totalBarellTTA);
                    ChargesInfoBean cinfobeanForBarellTTA = new ChargesInfoBean();
                    String blueChgCodeBarr = glmappingdao.blueChgCodeForBarrel("TTBARR", "AR", "LCLE");
                    if (blueChgCodeBarr != null && blueChgCodeBarr != "") {
                        cinfobeanForBarellTTA.setChargeCode(blueChgCodeBarr);
                    } else {
                        cinfobeanForBarellTTA.setChargeCode("TTBARR");
                    }
                    cinfobeanForBarellTTA.setChargesDesc("BARELL TTA");
                    cinfobeanForBarellTTA.setCommodityCode(lclQuotePiece.getCommodityType().getCode());
                    cinfobeanForBarellTTA.setRatePerWeightUnit(dblTTA);
                    cinfobeanForBarellTTA.setRate(bigBarrelTTACharges);
                    cinfobeanForBarellTTA.setRatePerUnitUom("FL");
                    cinfobeanForBarellTTA.setBookingPieceId(lclQuotePiece.getId());
                    chargesInfoList.add(cinfobeanForBarellTTA);
                    quoteRateList.add(cinfobeanForBarellTTA);
                }
            } else if (lbsKgsFound) {
                BigDecimal d = new BigDecimal(finalValue);
                ChargesInfoBean cinfobean = new ChargesInfoBean();
                cinfobean.setRatePerUnit(d);
                cinfobean.setRatePerUnitUom(rateUOM);
                cinfobean.setRatePerWeightUnit(new BigDecimal(rateWeight));
                cinfobean.setRatePerWeightUnitDiv(weightDiv);
                cinfobean.setRatePerVolumeUnit(new BigDecimal(rateMeasure));
                cinfobean.setRatePerVolumeUnitDiv(measureDiv);
                cinfobean.setChargeCode("0001");
                cinfobean.setChargesDesc("OCEAN FREIGHT");
                cinfobean.setCommodityCode(lclQuotePiece.getCommodityType().getCode());
                cinfobean.setChargeType(3);
                cinfobean.setRate(d);
                cinfobean.setMeasureRate(rateMeasure);
                cinfobean.setWeightRate(rateWeight);
                cinfobean.setMinCharge(new BigDecimal(minchg));
                //cinfobean.setLabel1(label1);
                cinfobean.setPcb(this.getPcb());
                cinfobean.setBookingPieceId(lclQuotePiece.getId());
                quoteRateList.add(cinfobean);
            }

            // }//end of if condition lclBookingPiece.getPerLbsKgs()
        }//end of for loop
    }//end of method

    public void calculateOfRateForCommodity(String origin, String destination, String pol, String pod, String rateType, Long fileNumberId,
            LclQuotePiece lclQuotePiece, User user) throws Exception {
        String pooorigin = "", polorigin = "", destinationfd = "", destinationpod = "";
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        if (rateType != null && rateType.equalsIgnoreCase("R")) {
            rateType = "Y";
        }
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            pooorigin = refterminal.getTrmnum();
        }
        RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(pol, rateType);
        if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
            polorigin = refterminalpol.getTrmnum();
        }
        Ports ports = portsdao.getByProperty("unLocationCode", destination);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            destinationfd = ports.getEciportcode();
            engmet = ports.getEngmet();
        }
        Ports portspod = portsdao.getByProperty("unLocationCode", pod);
        if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
            destinationpod = portspod.getEciportcode();
        }
        if ((refterminal != null || refterminalpol != null) && (ports != null || portspod != null)) {
            String commodityCode = new String();
            if (fileNumberId != null && fileNumberId > 0 && lclQuotePiece.getCommodityType() != null) {
                commodityCode = lclQuotePiece.getCommodityType().getCode();
            } else {
                commodityCode = lclQuotePiece.getCommNo();
            }
            lclUtils.calculateQuoteRatesForPiece(refterminal, ports, lclQuotePiece, lclratesdao, engmet, fileNumberId);
            Double minchg = 0.0;
            Double weight = 0.0;
            Double measure = 0.0;
            Double rateWeight = 0.0;
            Double rateMeasure = 0.0;
            String rateUOM = new String();
            BigDecimal dblBareel = new BigDecimal(0.00);
            BigDecimal dblTTA = new BigDecimal(0.00);
            BigDecimal weightDiv = null;
            BigDecimal measureDiv = new BigDecimal(1000);
            boolean ofratefound = false;
            Double calculatedWeight = 0.0;
            Double calculatedMeasure = 0.0;
            Double finalValue = 0.0;
            if (lclQuotePiece.isIsBarrel()) {
                Object[] barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                if (barellObj == null) {
                    barellObj = lclratesdao.getBarrelRate(polorigin, destinationpod, commodityCode);
                }
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclQuotePiece.getBookedPieceCount().intValue());
                            }
                            if (lclQuotePiece.getActualPieceCount() != null && lclQuotePiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclQuotePiece.getActualPieceCount().intValue());
                            }
                        }
                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclQuotePiece.getBookedPieceCount().intValue());
                            }
                            if (lclQuotePiece.getActualPieceCount() != null && lclQuotePiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclQuotePiece.getActualPieceCount().intValue());
                            }
                        }
                    }
                }
            }//end of  isIsBarrel if condition
            else {
                ofratefound = true;
                if (engmet != null) {
                    if (engmet.equals("E")) {
                        weightDiv = new BigDecimal(100);
                        if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weight = lclQuotePiece.getActualWeightImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weight = lclQuotePiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            measure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            measure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if (engmet.equals("M")) {
                        weightDiv = new BigDecimal(1000);
                        if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weight = lclQuotePiece.getActualWeightMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weight = lclQuotePiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            measure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            measure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                if (lclQuotePiece.getOfratemin() != null && !lclQuotePiece.getOfratemin().trim().equals("")) {
                    minchg = Double.parseDouble(lclQuotePiece.getOfratemin());
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, commodityCode);
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, "000000");
                }

                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, commodityCode);
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, "000000");
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, commodityCode);
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, "000000");
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, commodityCode);
                }
                if (minchg == 0.0) {
                    minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, "000000");
                }
                if (lclQuotePiece.getPerLbsKgs() != null && !lclQuotePiece.getPerLbsKgs().trim().equals("")) {
                    rateWeight = Double.parseDouble(lclQuotePiece.getPerLbsKgs());
                }
                if (lclQuotePiece.getPerCftCbm() != null && !lclQuotePiece.getPerCftCbm().trim().equals("")) {
                    rateMeasure = Double.parseDouble(lclQuotePiece.getPerCftCbm());
                }
                if (engmet != null) {
                    if (engmet.equals("E")) {
                        calculatedWeight = (weight / 100) * rateWeight;
                        if (weight >= 10000 && weight < 20000) {
                            if (refterminal.getGenericCode3().getCode() != null && !refterminal.getGenericCode3().getCode().trim().equals("")) {
                                this.setTtChargeCode2(refterminal.getGenericCode3().getCode());
                            }
                        } else if (weight >= 20000 && weight < 30000) {
                            if (refterminal.getGenericCode4().getCode() != null && !refterminal.getGenericCode4().getCode().trim().equals("")) {
                                this.setTtChargeCode2(refterminal.getGenericCode4().getCode());
                            }
                        }
                    } else if (engmet.equals("M")) {
                        calculatedWeight = (weight / 1000) * rateWeight;
                        Double convertedWeight = lclUtils.convertToLbs(weight).doubleValue();
                        if (convertedWeight >= 10000 && convertedWeight < 20000) {
                            if (refterminal.getGenericCode3() != null && refterminal.getGenericCode3().getCode() != null && !refterminal.getGenericCode3().getCode().trim().equals("")) {
                                this.setTtChargeCode2(refterminal.getGenericCode3().getCode());
                            }
                        } else if (weight >= 20000 && weight < 30000) {
                            if (refterminal.getGenericCode4() != null && refterminal.getGenericCode4().getCode() != null && !refterminal.getGenericCode4().getCode().trim().equals("")) {
                                this.setTtChargeCode2(refterminal.getGenericCode4().getCode());
                            }
                        }
                    }
                }
                calculatedMeasure = measure * rateMeasure;
                if (calculatedWeight > calculatedMeasure) {
                    finalValue = calculatedWeight;
                    rateUOM = "FRW";
                } else {
                    finalValue = calculatedMeasure;
                    rateUOM = "FRV";
                }
                if (finalValue <= minchg) {
                    finalValue = minchg;
                    rateUOM = "FRM";
                }
            }//end of isIsBarrel() else condition
            if (fileNumberId != null && fileNumberId > 0) {
                if (ofratefound) {
                    BigDecimal d = new BigDecimal(finalValue);
                    ChargesInfoBean cinfobean = new ChargesInfoBean();
                    cinfobean.setRatePerUnit(new BigDecimal(0));
                    cinfobean.setRatePerUnitUom(rateUOM);
                    cinfobean.setRatePerWeightUnit(new BigDecimal(rateWeight));
                    cinfobean.setRatePerWeightUnitDiv(weightDiv);
                    cinfobean.setRatePerVolumeUnit(new BigDecimal(rateMeasure));
                    cinfobean.setRatePerVolumeUnitDiv(measureDiv);
                    if (rateUOM.equals("FRW")) {
                        cinfobean.setRatePerUnitDiv(cinfobean.getRatePerWeightUnitDiv());
                    } else if (rateUOM.equals("FRV")) {
                        cinfobean.setRatePerUnitDiv(cinfobean.getRatePerVolumeUnitDiv());
                    }
                    cinfobean.setChargeCode("0001");
                    cinfobean.setChargesDesc("OCEAN FREIGHT");
                    cinfobean.setCommodityCode(commodityCode);
                    cinfobean.setChargeType(3);
                    cinfobean.setRate(new BigDecimal(0));
                    cinfobean.setMeasureRate(rateMeasure);
                    cinfobean.setWeightRate(rateWeight);
                    cinfobean.setMinCharge(new BigDecimal(minchg));
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setBookingPieceId(lclQuotePiece.getId());
                    chargesInfoMap.put("OCEAN FREIGHT", cinfobean);
                    chargesInfoList.add(cinfobean);
                    quoteRateList.add(cinfobean);
                } else {
                    if (totalBarrelCharges > 0.00) {
                        BigDecimal bigBarrelCharges = new BigDecimal(totalBarrelCharges);
                        ChargesInfoBean cinfobeanForBarell = new ChargesInfoBean();
                        String blueChgCodeBarr = glmappingdao.blueChgCodeForBarrel("OFBARR", "AR", "LCLE");
                        if (blueChgCodeBarr != null && blueChgCodeBarr != "") {
                            cinfobeanForBarell.setChargeCode(blueChgCodeBarr);
                        } else {
                            cinfobeanForBarell.setChargeCode("OFBARR");
                        }
                        cinfobeanForBarell.setChargesDesc("BARELL OFRATE");
                        cinfobeanForBarell.setCommodityCode(commodityCode);
                        cinfobeanForBarell.setRatePerWeightUnit(dblBareel);
                        cinfobeanForBarell.setRate(bigBarrelCharges);
                        cinfobeanForBarell.setRatePerUnitUom("FL");
                        cinfobeanForBarell.setBookingPieceId(lclQuotePiece.getId());
                        chargesInfoList.add(cinfobeanForBarell);
                        quoteRateList.add(cinfobeanForBarell);
                    }
                    if (totalBarellTTA > 0.00) {
                        BigDecimal bigBarrelTTACharges = new BigDecimal(totalBarellTTA);
                        ChargesInfoBean cinfobeanForBarellTTA = new ChargesInfoBean();
                        String blueChgCodeBarr = glmappingdao.blueChgCodeForBarrel("TTBARR", "AR", "LCLE");
                        if (blueChgCodeBarr != null && blueChgCodeBarr != "") {
                            cinfobeanForBarellTTA.setChargeCode(blueChgCodeBarr);
                        } else {
                            cinfobeanForBarellTTA.setChargeCode("TTBARR");
                        }
                        cinfobeanForBarellTTA.setChargesDesc("BARELL TTA");
                        cinfobeanForBarellTTA.setCommodityCode(commodityCode);
                        cinfobeanForBarellTTA.setRatePerWeightUnit(dblTTA);
                        cinfobeanForBarellTTA.setRate(bigBarrelTTACharges);
                        cinfobeanForBarellTTA.setRatePerUnitUom("FL");
                        cinfobeanForBarellTTA.setBookingPieceId(lclQuotePiece.getId());
                        chargesInfoList.add(cinfobeanForBarellTTA);
                        quoteRateList.add(cinfobeanForBarellTTA);
                    }
                }

                saveCharges(fileNumberId, user, "C", false);
            } else {
                if (ofratefound) {
                    lclQuotePiece.setOfrateamount(NumberUtils.convertToTwoDecimal(rateMeasure) + "/$"
                            + NumberUtils.convertToTwoDecimal(rateWeight) + "/$"
                            + NumberUtils.convertToTwoDecimal(minchg));
                } else {
                    if (totalBarrelCharges > 0.00 && dblBareel != null) {
                        lclQuotePiece.setOfrateamount(dblBareel.toString());
                    }
                    if (totalBarellTTA > 0.00 && dblTTA != null) {
                        lclQuotePiece.setOfrateamount(dblTTA.toString());
                    }
                }
            }

        }//end of refterminal if condition
    }//end of method

    public String getPortDifferential() {
        return portDifferential;
    }

    public void setPortDifferential(String portDifferential) {
        this.portDifferential = portDifferential;
    }

    public void saveCharges(Long fileNumberId, User user, String buttonValue, boolean includedestinationfee) throws Exception {
        Map adjustmentMap = null;
        if (!buttonValue.equalsIgnoreCase("CH")) {
            if (fileNumberId != null && fileNumberId > 0) {
                adjustmentMap = new HashMap();
                List<LclQuoteAc> lclQuoteAcList = lclquoteacdao.getLclCostByFileNumberME(fileNumberId, false);
                lclquoteacdao.deleteLclCostByFileNumber(fileNumberId, "E");
                LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
                for (LclQuoteAc lclQuoteAc : lclQuoteAcList) {
                    if (lclQuoteAc != null) {
                        String remarks = "DELETED -> Charge Code -> " + lclQuoteAc.getArglMapping().getChargeCode() + " Charge Amount -> " + lclQuoteAc.getArAmount();
                        lclRemarksDAO.insertLclRemarks(fileNumberId, REMARKS_QT_AUTO_NOTES, remarks, user.getUserId());
                        if (lclQuoteAc.getAdjustmentAmount().doubleValue() != 0.00) {
                            adjustmentMap.put(lclQuoteAc.getArglMapping().getChargeCode(), lclQuoteAc);
                        }
                    }
                }
            }
        }
        if (quoteRateList != null && quoteRateList.size() > 0) {
            GlMapping glmapping = null;
            String code = "";
            for (ChargesInfoBean validGl : quoteRateList) {
                glmapping = glmappingdao.findByBlueScreenChargeCode(validGl.getChargeCode(), "LCLE", "AR");
                if (glmapping == null) {
                    code += code.isEmpty() ? "" : ", ";
                    code += validGl.getChargeCode();
                    glMappingBlueChgCode = code;
                    isArGlmappingFlag = true;
                }
            }
            if (!isArGlmappingFlag) {
                for (int i = 0; i < quoteRateList.size(); i++) {
                    ChargesInfoBean cinfobean = (ChargesInfoBean) quoteRateList.get(i);
                    LclQuoteAc lclQuoteAc = new LclQuoteAc();
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        if (lclFileNumber == null) {
                            lclFileNumber = new LclFileNumber(fileNumberId);
                        }
                        lclQuoteAc.setLclFileNumber(lclFileNumber);
                        lclQuoteAc.setApBillToParty("C");
                        lclQuoteAc.setArBillToParty("C");
                    }
                    if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
                        if (cinfobean.getChargeCode().equalsIgnoreCase("OFBARR") || cinfobean.getChargeCode().equalsIgnoreCase("TTBARR")) {
                            glmapping = glmappingdao.getByProperty("chargeCode", cinfobean.getChargeCode());
                        } else {
                            glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
                        }

                        lclQuoteAc.setArglMapping(glmapping);
                        lclQuoteAc.setApglMapping(glmapping);
                    }
                    lclQuoteAc.setTransDatetime(new Date());
                    // lclQuoteAc.setApAmount(cinfobean.getRate());
                    if (cinfobean.getRatePerUnitUom() != null) {
                        if (cinfobean.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                            lclQuoteAc.setRatePerUnit(cinfobean.getRatePerUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclQuoteAc.setRatePerUnit(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    if (cinfobean.getRate() != null) {
                        lclQuoteAc.setArAmount(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclQuoteAc.setEnteredBy(user);
                    lclQuoteAc.setModifiedBy(user);
                    lclQuoteAc.setEnteredDatetime(new Date());
                    lclQuoteAc.setModifiedDatetime(new Date());
                    lclQuoteAc.setRateUom(engmet);
                    if (CommonUtils.isNotEmpty(lclQuoteAc.getArglMapping().getChargeCode()) && lclQuoteAc.getArglMapping().getChargeCode().substring(0, 2).equalsIgnoreCase("TT")) {//T&T charge has auto check
                        lclQuoteAc.setBundleIntoOf(true);
                    } else {
                        lclQuoteAc.setBundleIntoOf(false);
                    }
                    lclQuoteAc.setPrintOnBl(true);
                    if (includedestinationfee && (lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTBL")
                            || lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTWM"))) {
                        lclQuoteAc.setManualEntry(true);
                    }
                    if (engmet != null && engmet.equalsIgnoreCase("E")) {
                        lclQuoteAc.setRateUom("I");
                    }
                    if (cinfobean.getRatePerWeightUnit() != null) {
                        lclQuoteAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerWeightUnitDiv() != null) {
                        lclQuoteAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerVolumeUnit() != null) {
                        lclQuoteAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerVolumeUnitDiv() != null) {
                        lclQuoteAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclQuoteAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
                    if (cinfobean.getMinCharge() != null) {
                        lclQuoteAc.setRateFlatMinimum(cinfobean.getMinCharge().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerUnitDiv() != null) {
                        lclQuoteAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getBookingPieceId() != null && cinfobean.getBookingPieceId() > 0) {
                        lclQuoteAc.setLclQuotePiece(lclquotepiecedao.findById(cinfobean.getBookingPieceId()));
                    }
                    if (adjustmentMap != null && adjustmentMap.size() > 0 && adjustmentMap.containsKey(lclQuoteAc.getArglMapping().getChargeCode())) {
                        LclQuoteAc lclQuoteAcFromMap = (LclQuoteAc) adjustmentMap.get(lclQuoteAc.getArglMapping().getChargeCode());
                        lclQuoteAc.setAdjustmentAmount(lclQuoteAcFromMap.getAdjustmentAmount());
                    } else {
                        lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                    }
                    if (fileNumberId != null && fileNumberId > 0) {
                        lclquoteacdao.save(lclQuoteAc);
                    } else {
                        quoteAcList.add(lclQuoteAc);
                    }
                }
            }
        }
    }

    public void savePickupCharge(Long fileNumberId, User user, List<Carrier> carrierList, List<Carrier> carrierCostList, LclQuotePad lclQuotePad) throws Exception {
        LclQuotePadDAO lclQuotePadDAO = new LclQuotePadDAO();
        BigDecimal zeroBigDecimal = BigDecimal.ZERO;
        String inlandChargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        if (fileNumberId != null && fileNumberId > 0 && CommonUtils.isNotEmpty(inlandChargeCode)) {
            LclQuoteAc lclQuoteAc = lclquoteacdao.getLclQuoteAcByChargeCode(fileNumberId, inlandChargeCode);
            if (lclQuoteAc == null && lclQuotePad != null && lclQuotePad.getScac() != null && !lclQuotePad.getScac().trim().equalsIgnoreCase("")) {
                lclQuoteAc = new LclQuoteAc();
                if (lclFileNumber == null) {
                    lclFileNumber = new LclFileNumber(fileNumberId);
                }
                lclQuoteAc.setLclFileNumber(lclFileNumber);
                for (int j = 0; j < carrierList.size(); j++) {
                    Carrier carrier = carrierList.get(j);
                    if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(lclQuotePad.getScac())
                            && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                        BigDecimal d = new BigDecimal(carrier.getFinalcharge());
                        lclQuoteAc.setArAmount(d);
                    }
                }
                for (int j = 0; j < carrierCostList.size(); j++) {
                    Carrier carrier = carrierCostList.get(j);
                    if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(lclQuotePad.getScac())
                            && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                        BigDecimal pickupCost = new BigDecimal(carrier.getFinalcharge());
                        lclQuoteAc.setApAmount(pickupCost);
                    }
                }
                GlMapping gp = new GlMappingDAO().findByBlueScreenChargeCode("0012", "LCLE", "AR");
                lclQuoteAc.setArglMapping(gp);
                lclQuoteAc.setTransDatetime(new Date());
                lclQuoteAc.setManualEntry(false);
                lclQuoteAc.setRatePerUnitUom("FL");
                lclQuoteAc.setRateUom("I");
                lclQuoteAc.setAdjustmentAmount(zeroBigDecimal);
                lclQuoteAc.setEnteredBy(user);
                lclQuoteAc.setModifiedBy(user);
                lclQuoteAc.setEnteredDatetime(new Date());
                lclQuoteAc.setModifiedDatetime(new Date());
                if (lclQuoteAc.getArAmount() == null) {
                    lclQuoteAc.setArAmount(zeroBigDecimal);
                }
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setPrintOnBl(true);
                lclquoteacdao.saveOrUpdate(lclQuoteAc);
                lclQuotePad.setLclQuoteAc(lclQuoteAc);
                lclQuotePadDAO.saveOrUpdate(lclQuotePad);
            }
        }
    }

    public String calculateInsuaranceChargeForRadio(String pooTrmNum, String polTrmNum, String fdEciPortCode, String podEciPortCode,
            List<LclQuotePiece> lclQuotePiecesList, Double valueOfGoods, User user, Long fileNumberId,
            LclQuoteAc lclQuoteAc, GlMapping glMapping, String buttonValue) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            Object[] portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooTrmNum, fdEciPortCode, "000000");
            //end of stdcharges null checking
            //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooTrmNum, podEciPortCode, "000000");
            }  //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(polTrmNum, fdEciPortCode, "000000");
            }  //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(polTrmNum, podEciPortCode, "000000");
            }
            if (portChargeArray != null && portChargeArray.length > 0) {
                if (portChargeArray[0] != null && !portChargeArray[0].toString().trim().equals("")) {
                    insurt = Double.parseDouble(portChargeArray[0].toString());
                    this.setInsurt(insurt);
                }
                if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                    insamt = Double.parseDouble(portChargeArray[1].toString());
                    this.setInsamt(insamt);
                }
                if (portChargeArray[2] != null && !portChargeArray[2].toString().trim().equals("")) {
                   Double minChg = Double.parseDouble(portChargeArray[2].toString());
                    this.setInsMinChg(minChg);
                }
            }
        }
        String cif = calculateInsurance(valueOfGoods, buttonValue, user, fileNumberId, lclQuotePiecesList, lclQuoteAc, glMapping);
        return cif;
    }

    public void calculateChargeForDeliveyMetro(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclQuotePiece> lclQuotePiecesList, User user, Long fileNumberId, String engmet, String oldBlueScreenChargeCode, String billToParty) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(oldBlueScreenChargeCode, "LCLE", "AR");
        String blueScreenChargeCode = new String();
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            LclQuoteAc lclQuoteAc = lclquoteacdao.getLclCostByChargeCodeQuotePiece(fileNumberId, oldBlueScreenChargeCode, false, lclQuotePiece.getId());
            Date d = new Date();
            if (lclQuoteAc == null) {
                lclQuoteAc = new LclQuoteAc();
                lclQuoteAc.setApBillToParty(billToParty);
                lclQuoteAc.setArBillToParty(billToParty);
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclQuoteAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclQuoteAc.setEnteredBy(user);
                lclQuoteAc.setEnteredDatetime(d);
                lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclQuoteAc.setLclQuotePiece(lclQuotePiece);
            }
            lclQuoteAc.setTransDatetime(d);
            lclQuoteAc.setModifiedBy(user);
            lclQuoteAc.setModifiedDatetime(d);
            Double weightDouble = 0.00;
            Double weightMeasure = 0.00;
            if (engmet != null) {
                if (engmet.equals("E")) {
                    if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                    } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                    }
                    if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                    } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                    }

                } else if (engmet.equals("M")) {
                    if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                    } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                    }
                    if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                    } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                    }
                }
            }
            //calculate the Total Weight Of Commodities
            totalWeight = totalWeight + weightDouble;
            //calculate the Total Measure Of Commodities
            totalMeasure = totalMeasure + weightMeasure;
            if (oldBlueScreenChargeCode.equals("0015")) {
                blueScreenChargeCode = "0060";
            } else {
                blueScreenChargeCode = "0015";
            }
            Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, lclQuotePiece.getCommodityType().getCode(), blueScreenChargeCode);
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", blueScreenChargeCode);
            }//end of stdcharges null checking
            //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, lclQuotePiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, lclQuotePiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, lclQuotePiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, "000000", blueScreenChargeCode);
            }
            if (portChargeArray != null && portChargeArray.length > 0) {
                Integer chgType = null;
                if (portChargeArray[0] != null && !portChargeArray[0].toString().trim().equals("")) {
                    chgType = Integer.parseInt(portChargeArray[0].toString());
                }
                if (chgType != null) {
                    if (chgType == 1) {
                        Double flatrate = 0.0;
                        if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                            flatrate = Double.parseDouble(portChargeArray[1].toString());
                        }
                        lclQuoteAc.setRateUom(engmet);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclQuoteAc.setRateUom("I");
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
                        lclQuoteAc.setRatePerUnit(bd);
                        lclQuoteAc.setArAmount(bd);
                        lclQuoteAc.setArglMapping(glMapping);
                        lclQuoteAc.setRatePerUnitUom("FL");
                    }//end of charge type 1 if condition
                    //String chdCode = null;
                    if (chgType == 3) {
                        String rateUOM = new String();
                        Double cuftrt = 0.0;
                        Double wghtrt = 0.0;
                        Double cbmrt = 0.0;
                        Double kgsrt = 0.0;
                        Double minchg = 0.0;
                        Double calculatedWeight = 0.0;
                        Double calculatedMeasure = 0.0;
                        Double finalValue = 0.0;
                        BigDecimal weightDiv = null;
                        BigDecimal measureDiv = new BigDecimal(1000);
                        if (portChargeArray[2] != null && !portChargeArray[2].toString().trim().equals("")) {
                            cuftrt = Double.parseDouble(portChargeArray[2].toString());
                        }
                        if (portChargeArray[3] != null && !portChargeArray[3].toString().trim().equals("")) {
                            wghtrt = Double.parseDouble(portChargeArray[3].toString());
                        }
                        if (portChargeArray[5] != null && !portChargeArray[5].toString().trim().equals("")) {
                            cbmrt = Double.parseDouble(portChargeArray[5].toString());
                        }
                        if (portChargeArray[6] != null && !portChargeArray[6].toString().trim().equals("")) {
                            kgsrt = Double.parseDouble(portChargeArray[6].toString());
                        }
                        if (portChargeArray[4] != null && !portChargeArray[4].toString().trim().equals("")) {
                            minchg = Double.parseDouble(portChargeArray[4].toString());
                        }
                        if (engmet != null) {
                            if (engmet.equalsIgnoreCase("E")) {
                                weightDiv = new BigDecimal(100);
                                calculatedWeight = (totalWeight / 100) * wghtrt;
                                calculatedMeasure = totalMeasure * cuftrt;
                                lclQuoteAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                            } else if (engmet.equalsIgnoreCase("M")) {
                                weightDiv = new BigDecimal(1000);
                                calculatedWeight = (totalWeight / 1000) * kgsrt;
                                calculatedMeasure = totalMeasure * cbmrt;
                                lclQuoteAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
                            }
                        }
                        if (calculatedWeight >= calculatedMeasure) {
                            finalValue = calculatedWeight;
                            rateUOM = "W";
                        } else {
                            finalValue = calculatedMeasure;
                            rateUOM = "V";
                        }
                        if (finalValue <= minchg) {
                            finalValue = minchg;
                            rateUOM = "M";
                        }
                        BigDecimal bdf = new BigDecimal(finalValue);
                        lclQuoteAc.setRatePerWeightUnitDiv(weightDiv);
                        lclQuoteAc.setRatePerVolumeUnitDiv(measureDiv);
                        lclQuoteAc.setRateUom(engmet);
                        lclQuoteAc.setBundleIntoOf(false);
                        lclQuoteAc.setPrintOnBl(true);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclQuoteAc.setRateUom("I");
                        }
                        lclQuoteAc.setRatePerUnit(bdf);
                        lclQuoteAc.setArAmount(bdf);
                        glMapping = glmappingdao.findByBlueScreenChargeCode(blueScreenChargeCode, "LCLE", "AR");
                        lclQuoteAc.setArglMapping(glMapping);
                        lclQuoteAc.setRatePerUnitUom(rateUOM);
                        lclQuoteAc.setRateFlatMinimum(new BigDecimal(minchg));
                    }//end of charge type 3 if condition
                }
                lclQuoteAc.setArBillToParty(billToParty);
                lclQuoteAc.setApBillToParty(billToParty);
                lclquoteacdao.saveOrUpdate(lclQuoteAc);
            }

        }
    }

    public void calculateChargeForCalcHeavy(String pooTrmNum, String polTrmNum, String fdEciPortCode, String podEciPortCode,
            Boolean calcHeavy, List lclQuotePiecesList, Long fileNumberId, User user, String fdEngmet) throws Exception {
        if (calcHeavy != null && calcHeavy.equals(true) && CommonUtils.isNotEmpty(lclQuotePiecesList)) {
            databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            lclratesdao = new LCLRatesDAO(databaseSchema);
            String chgcod = new String();
            String comnum = new String();
            String overDimChgCdeForWeight = new String();
            String overDimChgCdeForMeasure = new String();
            boolean densecargofound = false;
            engmet = fdEngmet;
            LclBookingExport lclBookingExport = null;
            LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
            boolean includedestinationfee = false;
            if (fileNumberId != null && fileNumberId > 0) {
                lclBookingExport = lclBookingExportDAO.getLclBookingExportByFileNumber(fileNumberId);
            }
            if (lclBookingExport.isIncludeDestfees()) {
                includedestinationfee = lclBookingExport.isIncludeDestfees();
            }
            //Loop through the selected Commodity
            for (int j = 0; j < lclQuotePiecesList.size(); j++) {
                LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
                if (fileNumberId != null && fileNumberId > 0) {
                    comnum = lclQuotePiece.getCommodityType().getCode();
                } else {
                    comnum = lclQuotePiece.getCommNo();
                }
                Double weightDouble = 0.00;
                Double weightMeasure = 0.00;
                if (engmet != null) {
                    if ("E".equalsIgnoreCase(engmet)) {
                        if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if ("M".equalsIgnoreCase(engmet)) {
                        if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                //calculate the Total Weight Of Commodities
                totalWeight = totalWeight + weightDouble;
                //calculate the Total Measure Of Commodities
                totalMeasure = totalMeasure + weightMeasure;
                overDimChgCdeForWeight = getChargeCodeForHeavyLiftCharge(lclQuotePiecesList);
                overDimChgCdeForMeasure = getchgCdeForExtraLengthCharge(lclQuotePiece.getLclQuotePieceDetailList());
                densecargofound = getchgCdeForDenseCargo(lclQuotePiecesList);
                if (densecargofound) {
                    chgcod = chgcod + "0251,";
                }
                if (overDimChgCdeForWeight != null && !overDimChgCdeForWeight.trim().equals("")) {
                    chgcod = chgcod + overDimChgCdeForWeight + ",";
                }
                if (overDimChgCdeForMeasure != null && !overDimChgCdeForMeasure.trim().equals("")) {
                    chgcod = chgcod + overDimChgCdeForMeasure + ",";
                }
                if (chgcod != null && !chgcod.equals("")) {
                    chgcod = chgcod.substring(0, chgcod.length() - 1);
                }
                if (chgcod != null && !chgcod.trim().equals("")) {
                    String[] chargeCodeArray = chgcod.split(",");
                    for (int i = 0; i < chargeCodeArray.length; i++) {
                        String splittedchgcode = chargeCodeArray[i];
                        List prtChgsCommodityList = lclratesdao.findByChdcod(
                                pooTrmNum, fdEciPortCode, comnum, splittedchgcode);
                        List prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                pooTrmNum, fdEciPortCode, "000000", splittedchgcode);
                        if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                                && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                            prtChgsCommodityList = lclratesdao.findByChdcod(
                                    pooTrmNum, podEciPortCode, comnum, splittedchgcode);
                            prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                    pooTrmNum, podEciPortCode, "000000", splittedchgcode);
                        }
                        if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                                && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                            prtChgsCommodityList = lclratesdao.findByChdcod(
                                    polTrmNum, fdEciPortCode, comnum, splittedchgcode);
                            prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                    polTrmNum, fdEciPortCode, "000000", splittedchgcode);
                        }
                        if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                                && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                            prtChgsCommodityList = lclratesdao.findByChdcod(
                                    polTrmNum, podEciPortCode, comnum, splittedchgcode);
                            prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                    polTrmNum, podEciPortCode, "000000", splittedchgcode);
                        }
                        List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
                        //Check portChargesList null
                        if (portChargesList != null && portChargesList.size() > 0) {
                            //Loop through portChargesList
                            for (int k = 0; k < portChargesList.size(); k++) {
                                Object[] portcharges = (Object[]) portChargesList.get(k);
                                //Calculate Rates for all portcharges
                                setRates(portcharges, lclQuotePiece, comnum, k, lclQuotePiecesList.size(), fileNumberId, null, "");
                            }//end of portcharges for loop
                        }
                    }
                }
            }
            saveCharges(fileNumberId, user, "CH", includedestinationfee);
        }
    }

    public void modifyOfrate(Long fileNumberId) throws Exception {
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        Object[] spotRateList = lclQuoteDAO.getAllSpotRateValues(fileNumberId);
        BigDecimal calculatedWeight = BigDecimal.ZERO;
        BigDecimal calculatedMeasure = BigDecimal.ZERO;
        Double wmRate = 0.00;
        if (spotRateList != null && spotRateList.length > 0) {
            if (spotRateList[0] != null && spotRateList[0].toString().equals("true")) {
                Double totalSpotMeasure = calculateTotalForSpotRate(spotRateList[3].toString());
                ChargesInfoBean ofratecinfobean = (ChargesInfoBean) chargesInfoMap.get("OCEAN FREIGHT");
                if (spotRateList[2] != null && !spotRateList[2].toString().trim().equals("")
                        && Double.parseDouble(spotRateList[2].toString()) > 0.00 && null != ofratecinfobean) {
                    Double rate = Double.parseDouble(spotRateList[2].toString());
                    ofratecinfobean.setRatePerWeightUnit(new BigDecimal(rate - totalSpotWeight));
                    if (engmet != null) {
                        if (engmet.equalsIgnoreCase("E")) {
                            calculatedWeight = new BigDecimal((ofratecinfobean.getRatePerWeightUnit().doubleValue() / 100) * totalWeight);
                        } else {
                            calculatedWeight = new BigDecimal((ofratecinfobean.getRatePerWeightUnit().doubleValue() / 1000) * totalWeight);
                        }
                    }
                    if (spotRateList[3].toString().equalsIgnoreCase("true")) {
                        if (ttFound) {
                            wmRate = rate - totalSpotWeight;
                        } else {
                            wmRate = rate;
                        }
                    } else {
                        wmRate = rate - totalSpotWeight;
                    }
                    ofratecinfobean.setRatePerWeightUnit(new BigDecimal(wmRate));
                    if (spotRateList[1] == null || spotRateList[1].toString().trim().equals("") || Double.parseDouble(spotRateList[1].toString()) == 0.00) {
                        ofratecinfobean.setRatePerVolumeUnit(zerobigdecimalvalue);
                        if (calculatedWeight.doubleValue() > ofratecinfobean.getMinCharge().doubleValue()) {
                            ofratecinfobean.setRate(calculatedWeight);
                            ofratecinfobean.setRatePerUnitUom("FRW");
                        } else {
                            ofratecinfobean.setRate(ofratecinfobean.getMinCharge());
                            ofratecinfobean.setRatePerUnitUom("FRM");
                        }
                    }
                }
                if (spotRateList[1] != null && !spotRateList[1].toString().trim().equals("")
                        && Double.parseDouble(spotRateList[1].toString()) > 0.00 && null != ofratecinfobean) {
                    Double rate = Double.parseDouble(spotRateList[1].toString());
                    if (spotRateList[2] != null && !spotRateList[2].toString().trim().equals("")) {
                        ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(Double.parseDouble(spotRateList[2].toString()) - totalSpotMeasure));
                    } else {
                        ofratecinfobean.setRatePerVolumeUnit(new BigDecimal("0.00"));
                    }
                    if (spotRateList[3].toString().equalsIgnoreCase("true")) {
                        if (ttFound) {
                            wmRate = rate - totalSpotMeasure;
                        } else {
                            wmRate = rate;
                        }
                    } else {
                        wmRate = rate - totalSpotMeasure;
                    }
                    ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(wmRate));
                    calculatedMeasure = new BigDecimal((ofratecinfobean.getRatePerVolumeUnit().doubleValue()) * totalMeasure);
                    if (spotRateList[2] == null || spotRateList[2].toString().trim().equals("") || Double.parseDouble(spotRateList[2].toString()) == 0.00) {
                        ofratecinfobean.setRatePerWeightUnit(zerobigdecimalvalue);
                        if (calculatedMeasure.doubleValue() > ofratecinfobean.getMinCharge().doubleValue()) {
                            ofratecinfobean.setRate(calculatedMeasure);
                            ofratecinfobean.setRatePerUnitUom("FRV");
                        } else {
                            ofratecinfobean.setRate(ofratecinfobean.getMinCharge());
                            ofratecinfobean.setRatePerUnitUom("FRM");
                        }
                    }
                    if ((spotRateList[1] != null && !spotRateList[1].toString().trim().equals("") && Double.parseDouble(spotRateList[1].toString()) > 0.00)
                            && (spotRateList[2] != null && !spotRateList[2].toString().trim().equals("") && Double.parseDouble(spotRateList[2].toString()) > 0.00)) {
                        if (calculatedMeasure.doubleValue() > calculatedWeight.doubleValue() && calculatedMeasure.doubleValue() > ofratecinfobean.getMinCharge().doubleValue()) {
                            ofratecinfobean.setRate(calculatedMeasure);
                            ofratecinfobean.setRatePerUnitUom("FRV");
                        } else if (calculatedWeight.doubleValue() > calculatedMeasure.doubleValue() && calculatedWeight.doubleValue() > ofratecinfobean.getMinCharge().doubleValue()) {
                            ofratecinfobean.setRate(calculatedWeight);
                            ofratecinfobean.setRatePerUnitUom("FRW");
                        } else {
                            ofratecinfobean.setRate(ofratecinfobean.getMinCharge());
                            ofratecinfobean.setRatePerUnitUom("FRM");
                        }

                    }
                }
            }
        }
    }

    public List<LclQuoteAc> quickQuoteChargesCalculation(String pooorigin, String destinationfd, String pooId, String fdId, String commodityNo, String rateType, String hazmat) throws Exception {
        String pol = "", pod = "", polorigin = "", destinationpod = "";
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = new String();
        String UOM = "";
        String rateUOM = "";
        Double rateWeight = 0.0;
        Double rateVolume = 0.0;
        Double minchg = 0.0;
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        RefTerminal refterminal = null;
        if (rateType.equals("R")) {
            rateType = "Y";
        }
        refterminal = refterminaldao.getTerminalByUnLocation(pooorigin, rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            pooorigin = refterminal.getTrmnum();
            ofratebasis += pooorigin + "-";
        }
        LclBookingPlanBean bookingPlanBean = new LclBookingPlanDAO().getRelay(Integer.parseInt(pooId), Integer.parseInt(fdId), "N");
        if (bookingPlanBean != null && CommonUtils.isNotEmpty(bookingPlanBean.getPod_code())
                && CommonUtils.isNotEmpty(bookingPlanBean.getPol_code())) {
            pol = bookingPlanBean.getPol_code();
            pod = bookingPlanBean.getPod_code();
            RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(pol, rateType);
            if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
                polorigin = refterminalpol.getTrmnum();
            }
        }
        String comnum = new String();
        Object[] stdchargesObj = null;
        ports = portsdao.getByProperty("unLocationCode", destinationfd);

        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            destinationfd = ports.getEciportcode();
            ofratebasis += destinationfd + "-";
            engmet = ports.getEngmet();
        }
        Ports portspod = portsdao.getByProperty("unLocationCode", pod);
        if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
            destinationpod = portspod.getEciportcode();
        }
        NumberUtils numberUtils = new NumberUtils();
        GlMapping glmapping = null;
        if (CommonUtils.isNotEmpty(engmet)) {
            if (engmet.equals("E")) {
                label1 = " CFT, ";
                label2 = " LBS, ";
            } else {
                label1 = " CBM,";
                label2 = " KGS, ";
            }
        }
        if (refterminal != null && refterminal.getGenericCode1() != null && refterminal.getGenericCode1().getCode() != null && !refterminal.getGenericCode1().getCode().trim().equals("")) {
            this.setTtChargeCode1(refterminal.getGenericCode1().getCode());
        }
        //Calculate the Ocean Freight Rate
        Object[] ofratecongObj = lclratesdao.findByOrgnDestComCdeOfrate(pooorigin, ports.getEciportcode(), commodityNo);
        Double weight = 0.0;
        Double calculatedWeight = 0.0;
        Double measure = 0.0;
        Double calculatedMeasure = 0.0;
        Double finalValue = 0.0;
        BigDecimal weightDiv = BigDecimal.ZERO;
        if (ofratecongObj != null) {
            if (ofratecongObj[4] != null && !ofratecongObj[4].toString().trim().equals("")) {
                minchg = Double.parseDouble(ofratecongObj[4].toString());
            }
            if (engmet.equals("E")) {
                weightDiv = new BigDecimal(100);
                if (ofratecongObj[0] != null && !ofratecongObj[0].toString().trim().equals("")) {
                    rateVolume = Double.parseDouble(ofratecongObj[0].toString());
                }
                if (ofratecongObj[1] != null && !ofratecongObj[1].toString().trim().equals("")) {
                    rateWeight = Double.parseDouble(ofratecongObj[1].toString());
                }
            } else {
                weightDiv = new BigDecimal(1000);
                if (ofratecongObj[2] != null && !ofratecongObj[2].toString().trim().equals("")) {
                    rateVolume = Double.parseDouble(ofratecongObj[2].toString());
                }
                if (ofratecongObj[3] != null && !ofratecongObj[3].toString().trim().equals("")) {
                    rateWeight = Double.parseDouble(ofratecongObj[3].toString());
                }
            }
            if (engmet != null) {
                if (engmet.equals("E")) {
                    calculatedWeight = (weight / 100) * rateWeight;
                } else if (engmet.equals("M")) {
                    calculatedWeight = (weight / 1000) * rateWeight;
                }
            }
            calculatedMeasure = measure * rateVolume;
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, commodityNo);
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationfd, "000000");
            }

            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, commodityNo);
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(pooorigin, destinationpod, "000000");
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, commodityNo);
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationfd, "000000");
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, commodityNo);
            }
            if (minchg == 0.0) {
                minchg = lclratesdao.findMinchgByOrgDestComcdeChdcod(polorigin, destinationpod, "000000");
            }
            if (calculatedWeight > calculatedMeasure) {
                finalValue = calculatedWeight;
                label = "* O/F - TO WEIGHT *";
                rateUOM = "FRW";
            } else {
                finalValue = calculatedMeasure;
                label = "* O/F - VOLUME *";
                rateUOM = "FRV";
            }
            if (finalValue <= minchg) {
                finalValue = minchg;
                label = "* O/F - MINIMUM *";
                rateUOM = "FRM";
            }
            glmapping = glmappingdao.findByBlueScreenChargeCode("0001", "LCLE", "AR");
            addLclQuoteAc(rateUOM, glmapping, CommonConstants.OFR_BLUESCREEN_CHARGECODE, CommonConstants.OFR_BLUESCREEN_CHARGEDESC, label, "$" + numberUtils.convertToTwoDecimal(rateVolume) + "" + label1 + "" + numberUtils.convertToTwoDecimal(rateWeight) + "/" + numberUtils.convertToTwoDecimal(weightDiv.doubleValue()) + "" + label2 + " ($" + numberUtils.convertToTwoDecimal(minchg) + " MINIMUM)", numberUtils.convertToTwoDecimal(minchg), rateVolume, rateWeight, weightDiv);
            ofratebasis += commodityNo;
            stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, commodityNo);
            String stdChgRateBasis = ofratebasis;
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, "000000");
                stdChgRateBasis = pooorigin + "-" + destinationfd + "-000000";
            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationpod, commodityNo);
                stdChgRateBasis = pooorigin + "-" + destinationpod + "-" + comnum;
            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationpod, "000000");
                stdChgRateBasis = pooorigin + "-" + destinationpod + "-000000";
            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationfd, commodityNo);
                stdChgRateBasis = polorigin + "-" + destinationfd + "-" + comnum;
            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationfd, "000000");
                stdChgRateBasis = polorigin + "-" + destinationfd + "-000000";
            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationpod, commodityNo);
                stdChgRateBasis = polorigin + "-" + destinationpod + "-" + comnum;

            }
            if (stdchargesObj == null) {
                stdchargesObj = lclratesdao.findByOrgnDestComCde(polorigin, destinationpod, "000000");
                stdChgRateBasis = polorigin + "-" + destinationpod + "-000000";
            }

            this.setStdchgratebasis(stdChgRateBasis);
            String chgcode = "";
            if (stdchargesObj != null) {
                chgcode = chgcode + stdchargesObj[0].toString() + "," + stdchargesObj[1].toString() + "," + stdchargesObj[2].toString() + "," + stdchargesObj[3].toString() + ","
                        + stdchargesObj[4].toString() + "," + stdchargesObj[5].toString() + "," + stdchargesObj[6].toString() + "," + stdchargesObj[7].toString() + "," + stdchargesObj[8].toString() + ","
                        + stdchargesObj[9].toString() + "," + stdchargesObj[10].toString() + "," + stdchargesObj[11].toString();
            }
            if (chgcode != null && !chgcode.trim().equals("")) {
                StringTokenizer tokenizer = new StringTokenizer(chgcode, ",");
                while (tokenizer.hasMoreTokens()) {
                    String splittedchgcode = tokenizer.nextToken();
                    List prtChgsCommodityList = lclratesdao.findByChdcod(
                            pooorigin, destinationfd, commodityNo, splittedchgcode);
                    List prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                            pooorigin, destinationfd, "000000", splittedchgcode);
                    if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                            && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(
                                pooorigin, destinationpod, commodityNo, splittedchgcode);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                pooorigin, destinationpod, "000000", splittedchgcode);
                    }
                    if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                            && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(
                                polorigin, destinationfd, commodityNo, splittedchgcode);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                polorigin, destinationfd, "000000", splittedchgcode);
                    }
                    if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                            && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(
                                polorigin, destinationpod, commodityNo, splittedchgcode);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                                polorigin, destinationpod, "000000", splittedchgcode);
                    }
                    List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
                    if (portChargesList != null && portChargesList.size() > 0) {
                        for (int i = 0; i < portChargesList.size(); i++) {
                            Object[] portcharges = (Object[]) portChargesList.get(i);
                            String chdCode = null;
                            Integer chgType = null;
                            String blueScreenChargeCode = null;
                            if (portcharges != null) {
                                if (portcharges[0] != null && !portcharges[0].toString().trim().equals("")) {
                                    chdCode = portcharges[0].toString();
                                }
                                if (portcharges[0] != null && !portcharges[1].toString().trim().equals("")) {
                                    chgType = Integer.parseInt(portcharges[1].toString());
                                }
                                String chargesDesc = new String();
                                Double cbmrt = 0.0;
                                Double kgsrt = 0.0;
                                if (chgType != null && chgType > 0) {
                                    if (chdCode != null) {
                                        glmapping = glmappingdao.findByBlueScreenChargeCode(chdCode, "LCLE", "AR");
                                        if (glmapping != null && glmapping.getChargeCode() != null) {
                                            blueScreenChargeCode = glmapping.getChargeCode();
                                            if (glmapping.getChargeCode().equalsIgnoreCase("PRTDIF")) {
                                                this.setPortDifferential(glmapping.getChargeCode());
                                            }
                                            chargesDesc = glmapping.getChargeCode();
                                        }
                                    }
                                    if (chgType == 1) {
                                        Double flatrate = 0.0;
                                        if (portcharges[2] != null && !portcharges[2].toString().trim().equals("")) {
                                            flatrate = Double.parseDouble(portcharges[2].toString());
                                        }
                                        addLclQuoteAc("FL", glmapping, chdCode, chargesDesc, "", "$" + numberUtils.convertToTwoDecimal(flatrate) + " FLAT RATE.", numberUtils.convertToTwoDecimal(flatrate), 0.00, 0.00, BigDecimal.ZERO);
                                    }

                                    if (chgType == 3) {
                                        if (chdCode != null) {
                                            if (!chdCode.equals("0001")) {
                                                Double cuftrt = 0.0;
                                                Double wghtrt = 0.0;
                                                Double ttcbmcftrate = 0.0;
                                                Double ttlbskgsrate = 0.0;
                                                Double ttcbmcft = 0.0;
                                                Double ttlbskgs = 0.0;
                                                BigDecimal measureDiv = new BigDecimal(1000);
                                                if (portcharges[4] != null && !portcharges[4].toString().trim().equals("")) {
                                                    cuftrt = Double.parseDouble(portcharges[4].toString());
                                                }
                                                if (portcharges[5] != null && !portcharges[5].toString().trim().equals("")) {
                                                    wghtrt = Double.parseDouble(portcharges[5].toString());
                                                }
                                                if (portcharges[9] != null && !portcharges[9].toString().trim().equals("")) {
                                                    cbmrt = Double.parseDouble(portcharges[9].toString());
                                                }
                                                if (portcharges[10] != null && !portcharges[10].toString().trim().equals("")) {
                                                    kgsrt = Double.parseDouble(portcharges[10].toString());
                                                }
                                                if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")) {
                                                    minchg = Double.parseDouble(portcharges[6].toString());
                                                }
                                                if (engmet != null) {
                                                    if (engmet.equalsIgnoreCase("E")) {
                                                        weightDiv = new BigDecimal(100);
                                                        if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                                            calculatedWeight = (totalWeight / 100) * wghtrt;
                                                            calculatedMeasure = totalMeasure * cuftrt;
                                                        }
                                                        ttcbmcftrate = cuftrt;
                                                        ttlbskgsrate = wghtrt;
                                                    } else if (engmet.equalsIgnoreCase("M")) {
                                                        weightDiv = new BigDecimal(1000);
                                                        if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                                            calculatedWeight = (totalWeight / 1000) * kgsrt;
                                                            calculatedMeasure = totalMeasure * cbmrt;
                                                        }
                                                        ttcbmcftrate = cbmrt;
                                                        ttlbskgsrate = kgsrt;
                                                    }
                                                }
                                                if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                                    if (calculatedWeight >= calculatedMeasure) {
                                                        finalValue = calculatedWeight;
                                                        rateUOM = "W";
                                                    } else {
                                                        finalValue = calculatedMeasure;
                                                        rateUOM = "V";
                                                    }
                                                    if (finalValue <= minchg) {
                                                        finalValue = minchg;
                                                        rateUOM = "M";
                                                    }
                                                } else {
                                                    finalValue = calculatedMeasure;
                                                    rateUOM = "V";
                                                    if (finalValue <= minchg) {
                                                        finalValue = minchg;
                                                        rateUOM = "M";
                                                    }
                                                }

                                                if (rateUOM.equalsIgnoreCase("W")) {
                                                    label = "*** TO WEIGHT ***";
                                                }
                                                if (rateUOM.equalsIgnoreCase("V")) {
                                                    label = "*** VOLUME ***";
                                                }
                                                if (rateUOM.equalsIgnoreCase("M")) {
                                                    label = "*** MINIMUM ***";
                                                }
                                                if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")) {
                                                    minchg = Double.parseDouble(portcharges[6].toString());
                                                }
                                                boolean ttchargecode1found = this.getTtChargeCode1() != null
                                                        && this.getTtChargeCode1().equals(blueScreenChargeCode);
                                                boolean portDifferentialfound = this.getPortDifferential() != null
                                                        && !this.getPortDifferential().equals("0000")
                                                        && this.getPortDifferential().equals(chdCode);
                                                if (ttchargecode1found || portDifferentialfound) {
                                                    Double flatRateMinimum = 0.0;
                                                    Double ratePervolumeUnit = 0.0;
                                                    Double ratePerWeightUnit = 0.0;
                                                    for (int k = 0; k < quoteAcList.size(); k++) {
                                                        LclQuoteAc ofratecinfobean = (LclQuoteAc) quoteAcList.get(k);
                                                        if (ofratecinfobean.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                                                            if (ofratecinfobean != null && ofratecinfobean.getRateFlatMinimum() != null && minchg != null) {
                                                                flatRateMinimum = ofratecinfobean.getRateFlatMinimum().doubleValue() + minchg;
                                                                ratePervolumeUnit = ofratecinfobean.getRatePerVolumeUnit().doubleValue() + ttcbmcftrate;
                                                                ratePerWeightUnit = ofratecinfobean.getRatePerWeightUnit().doubleValue() + ttlbskgsrate;
                                                                rateAmount = "$" + numberUtils.convertToTwoDecimal(ofratecinfobean.getRatePerVolumeUnit().doubleValue()) + "" + label1 + "" + numberUtils.convertToTwoDecimal(ofratecinfobean.getRatePerWeightUnit().doubleValue()) + "/" + numberUtils.convertToTwoDecimal(weightDiv.doubleValue()) + "" + label2 + " ($" + numberUtils.convertToTwoDecimal(ofratecinfobean.getRateFlatMinimum().doubleValue()) + " MINIMUM)" + "\n"
                                                                        + "$" + numberUtils.convertToTwoDecimal(ttcbmcftrate) + "" + label1 + "" + numberUtils.convertToTwoDecimal(ttlbskgsrate) + "/" + numberUtils.convertToTwoDecimal(weightDiv.doubleValue()) + "" + label2 + " ($" + numberUtils.convertToTwoDecimal(minchg) + " MINIMUM)";
                                                                ofratecinfobean.setRateFlatMinimum(new BigDecimal(flatRateMinimum));
                                                                ofratecinfobean.setArAmount(new BigDecimal(flatRateMinimum));
                                                                ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(ratePervolumeUnit));
                                                                ofratecinfobean.setRatePerWeightUnit(new BigDecimal(ratePerWeightUnit));
                                                                ofratecinfobean.setLabel2("$" + numberUtils.convertToTwoDecimal(ratePervolumeUnit) + "" + label1 + "" + numberUtils.convertToTwoDecimal(ratePerWeightUnit) + "/" + numberUtils.convertToTwoDecimal(weightDiv.doubleValue()) + "" + label2 + " ($" + numberUtils.convertToTwoDecimal(flatRateMinimum) + " MINIMUM)");
                                                                quoteAcList.set(k, ofratecinfobean);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    addLclQuoteAc(rateUOM, glmapping, chdCode, chargesDesc, label, "$" + numberUtils.convertToTwoDecimal(ttcbmcftrate) + "" + label1 + "" + numberUtils.convertToTwoDecimal(ttlbskgsrate) + "/" + numberUtils.convertToTwoDecimal(weightDiv.doubleValue()) + "" + label2 + " ($" + numberUtils.convertToTwoDecimal(minchg) + " MINIMUM)", numberUtils.convertToTwoDecimal(minchg), ttcbmcftrate, ttlbskgsrate, weightDiv);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (hazmat.equals("true")) {
                chgcod = chgcod + "119,";
                Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, commodityNo, "0119");
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", "0119");
                }//end of stdcharges null checking
                //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, commodityNo, "0119");
                } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, commodityNo, "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, commodityNo, "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, "000000", "0119");
                }
                glmapping = glmappingdao.findByBlueScreenChargeCode("0119", "LCLE", "AR");
                if (portChargeArray != null && portChargeArray.length > 0) {
                    Integer chgType = null;
                    if (portChargeArray[0] != null && !portChargeArray[0].toString().trim().equals("")) {
                        chgType = Integer.parseInt(portChargeArray[0].toString());
                    }
                    if (chgType != null) {
                        if (chgType == 1) {
                            Double flatrate = 0.0;
                            if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                                flatrate = Double.parseDouble(portChargeArray[1].toString());
                            }
                            if (engmet != null) {
                                UOM = engmet;
                            }
                            if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                UOM = "I";
                            }
                            minchg = flatrate;
                            rateUOM = "FL";
                        }
                        addLclQuoteAc("FL", glmapping, chgcod, "", "", "$" + numberUtils.convertToTwoDecimal(minchg) + " FLAT RATE.", numberUtils.convertToTwoDecimal(minchg), 0.00, 0.00, BigDecimal.ZERO);
                    }
                }
            }//end Haz
        }
        return quoteAcList;
    }

    public void addLclQuoteAc(String rateUom, GlMapping glmapping, String chgcode, String chgDesc, String label1, String label2, String chargeAmount, Double ratePerVolume, Double ratePerweight, BigDecimal weightDiv) throws Exception {
        LclQuoteAc lclQuoteAc = new LclQuoteAc();
        lclQuoteAc.setRatePerUnitUom(rateUom);
        lclQuoteAc.setArAmount(new BigDecimal(chargeAmount));
        lclQuoteAc.setRateFlatMinimum(new BigDecimal(chargeAmount));
        lclQuoteAc.setArglMapping(glmapping);
        lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(ratePerVolume));
        lclQuoteAc.setRatePerWeightUnit(new BigDecimal(ratePerweight));
        lclQuoteAc.setRatePerWeightUnitDiv(weightDiv);
        lclQuoteAc.setLabel1(label1);
        lclQuoteAc.setLabel2(label2);
        quoteAcList.add(lclQuoteAc);
    }

    public void calculateHazmatChargeForRadio(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclQuotePiece> lclQuotePiecesList, LclQuotePiece lclQuotePiece, String engmet, User user, Long fileNumberId,
            LclQuoteAc lclQuoteAc, GlMapping glMapping) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = new String();
        Double weightDouble = 0.00;
        Double weightMeasure = 0.00;
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            if (lclQuotePiece.isHazmat()) {
                chgcod = chgcod + "119,";
                lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
                 if (engmet != null) {
                    if ("E".equalsIgnoreCase(engmet)) {
                        if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getActualWeightImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getActualVolumeImperial().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if ("M".equalsIgnoreCase(engmet)) {
                        if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getActualWeightMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclQuotePiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getActualVolumeMetric().doubleValue();
                        } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclQuotePiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                 //calculate the Total Weight Of Commodities
                totalWeight = totalWeight + weightDouble;
                //calculate the Total Measure Of Commodities
                totalMeasure = totalMeasure + weightMeasure;
                Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, lclQuotePiece.getCommNo(), "0119");
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", "0119");
                }//end of stdcharges null checking
                //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, lclQuotePiece.getCommNo(), "0119");
                } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, lclQuotePiece.getCommNo(), "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, lclQuotePiece.getCommNo(), "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, "000000", "0119");
                }
                if (portChargeArray != null && portChargeArray.length > 0) {
                    Integer chgType = null;
                    if (portChargeArray[0] != null && !portChargeArray[0].toString().trim().equals("")) {
                        chgType = Integer.parseInt(portChargeArray[0].toString());
                    }
                    if (chgType != null) {
                        if (chgType == 1) {
                            Double flatrate = 0.0;
                            if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                                flatrate = Double.parseDouble(portChargeArray[1].toString());
                            }
                            if (engmet != null) {
                                lclQuoteAc.setRateUom(engmet);
                            }
                            if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                lclQuoteAc.setRateUom("I");
                            }
                            BigDecimal bd = new BigDecimal(flatrate);
                            if (lclQuoteAc != null) {
                                lclQuoteAc.setRatePerUnit(bd);
                                lclQuoteAc.setArAmount(bd);
                                lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                                lclQuoteAc.setRatePerUnitUom("FL");
                                lclQuoteAc.setRateUom(engmet);
                                lclQuoteAc.setBundleIntoOf(false);
                                lclQuoteAc.setPrintOnBl(true);
                                if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                    lclQuoteAc.setRateUom("I");
                                }
                                lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                                lclQuoteAc.setArglMapping(glMapping);
                            }
                        }//end of charge type 1 if condition
                        //String chdCode = null;
                        if (chgType == 3) {
                            String rateUOM = new String();
                            Double cuftrt = 0.0;
                            Double wghtrt = 0.0;
                            Double cbmrt = 0.0;
                            Double kgsrt = 0.0;
                            Double minchg = 0.0;
                            Double calculatedWeight = 0.0;
                            Double calculatedMeasure = 0.0;
                            Double finalValue = 0.0;
                            BigDecimal weightDiv = null;
                            BigDecimal measureDiv = new BigDecimal(1000);
                            if (portChargeArray[2] != null && !portChargeArray[2].toString().trim().equals("")) {
                                cuftrt = Double.parseDouble(portChargeArray[2].toString());
                            }
                            if (portChargeArray[3] != null && !portChargeArray[3].toString().trim().equals("")) {
                                wghtrt = Double.parseDouble(portChargeArray[3].toString());
                            }
                            if (portChargeArray[5] != null && !portChargeArray[5].toString().trim().equals("")) {
                                cbmrt = Double.parseDouble(portChargeArray[5].toString());
                            }
                            if (portChargeArray[6] != null && !portChargeArray[6].toString().trim().equals("")) {
                                kgsrt = Double.parseDouble(portChargeArray[6].toString());
                            }
                            if (portChargeArray[4] != null && !portChargeArray[4].toString().trim().equals("")) {
                                minchg = Double.parseDouble(portChargeArray[4].toString());
                            }
                                
                            if (engmet != null) {
                                if (engmet.equalsIgnoreCase("E")) {
                                    weightDiv = new BigDecimal(100);
                                    calculatedWeight = (totalWeight / 100) * wghtrt;
                                    calculatedMeasure = totalMeasure * cuftrt;
                                    lclQuoteAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                    lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    weightDiv = new BigDecimal(1000);
                                    calculatedWeight = (totalWeight / 1000) * kgsrt;
                                    calculatedMeasure = totalMeasure * cbmrt;
                                    lclQuoteAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                    lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
                                }
                            }
                            if (calculatedWeight >= calculatedMeasure) {
                                finalValue = calculatedWeight;
                                rateUOM = "W";
                            } else {
                                finalValue = calculatedMeasure;
                                rateUOM = "V";
                            }
                            if (finalValue <= minchg) {
                                finalValue = minchg;
                                rateUOM = "M";
                            }
                            BigDecimal bdf = new BigDecimal(finalValue);
                            lclQuoteAc.setRatePerWeightUnitDiv(weightDiv);
                            lclQuoteAc.setRatePerVolumeUnitDiv(measureDiv);
                            lclQuoteAc.setRateUom(engmet);
                            lclQuoteAc.setBundleIntoOf(false);
                            lclQuoteAc.setPrintOnBl(true);
                            if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                lclQuoteAc.setRateUom("I");
                            }
                            lclQuoteAc.setRatePerUnit(bdf);
                            lclQuoteAc.setArAmount(bdf);
                            lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                            lclQuoteAc.setArglMapping(glMapping);
                            lclQuoteAc.setRatePerUnitUom(rateUOM);
                            lclQuoteAc.setRateFlatMinimum(new BigDecimal(minchg));
                        }//end of charge type 3 if condition
                    }
                    lclquoteacdao.saveOrUpdate(lclQuoteAc);
                }
            }
        }
    }

    public void calculateCAFChargeForRadio(String pooTrmNum, String polTrmNum, String fdEciPortCode, String podEciPortCode,
            List<LclQuotePiece> lclQuotePiecesList, String pcBoth, User user, Long fileNumberId,
            String fdUnLocCode, String fdEngmet) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = null;
        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
            Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(fdUnLocCode);
            if (ncl[2] != null && ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")
                    && null != fdEciPortCode && !"".equalsIgnoreCase(fdEciPortCode) && pcBoth != null
                    && pcBoth.equalsIgnoreCase("C")) {
                if (fdEciPortCode.equals("050") || fdEciPortCode.equals("060")) {
                    chgcod = "0095";
                } else {
                    chgcod = "0005";
                }
            }
            List prtChgsCommodityList = lclratesdao.findByChdcod(
                    pooTrmNum, fdEciPortCode, lclQuotePiece.getCommodityType().getCode(), chgcod);
            List prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                    pooTrmNum, fdEciPortCode, "000000", chgcod);
            if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        pooTrmNum, podEciPortCode, lclQuotePiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        pooTrmNum, podEciPortCode, "000000", chgcod);
            }
            if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        polTrmNum, fdEciPortCode, lclQuotePiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        polTrmNum, fdEciPortCode, "000000", chgcod);
            }
            if ((prtChgsCommodityList == null || prtChgsCommodityList.isEmpty())
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.isEmpty())) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        polTrmNum, podEciPortCode, lclQuotePiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        polTrmNum, podEciPortCode, "000000", chgcod);
            }
            List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
            if (portChargesList != null && portChargesList.size() > 0) {
                Double total = 0.0;
                if (lclQuotePiecesList.size() == 1) {
                    total = lclquoteacdao.getTotalWithoutParticularChargeCode(fileNumberId, "CAF");
                } else {
                    List<LclQuoteAc> chargeList = lclquoteacdao.getLclCostWithoutCAF(fileNumberId);
                    List<LclQuoteAc> lclQuoteAcList = exportQuoteUtils.getRolledUpChargesForQuote(lclQuotePiecesList, chargeList, fdEngmet, "No");
                    total = Double.parseDouble(exportQuoteUtils.calculateTotalByQuoteAcList(lclQuoteAcList));
                }
                Double finalValue = 0.0;
                Double minchg = 0.0;
                Object[] portcharges = (Object[]) portChargesList.get(0);
                if (portcharges[3] != null && !portcharges[3].toString().trim().equals("")) {
                    minchg = Double.parseDouble(portcharges[3].toString());
                }
                if (portcharges[3] != null && !portcharges[3].toString().trim().equals("")) {
                    finalValue = total * minchg;
                }
                if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")
                        && Double.parseDouble(portcharges[6].toString()) > finalValue) {
                    finalValue = Double.parseDouble(portcharges[6].toString());
                }
                LclQuoteAc lclQuoteAc = lclquoteacdao.findByBlueScreenChargeCode(fileNumberId, chgcod, false);
                if (lclQuoteAc == null) {
                    lclQuoteAc = new LclQuoteAc();
                }
                BigDecimal bdf = new BigDecimal(finalValue);
                BigDecimal min = new BigDecimal(minchg);
                lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(0.00));
                lclQuoteAc.setRatePerVolumeUnitDiv(new BigDecimal(0.00));
                lclQuoteAc.setRateUom(fdEngmet);
                if (fdEngmet != null && fdEngmet.equalsIgnoreCase("E")) {
                    lclQuoteAc.setRateUom("I");
                }
                lclQuoteAc.setRatePerUnit(min.setScale(2, BigDecimal.ROUND_HALF_UP));
                lclQuoteAc.setArAmount(bdf.setScale(2, BigDecimal.ROUND_HALF_UP));
                lclQuoteAc.setAdjustmentAmount(new BigDecimal(0.00));
                GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(chgcod, "LCLE", "AR");
                lclQuoteAc.setArglMapping(glMapping);
                lclQuoteAc.setRatePerUnitUom("PCT");
                lclQuoteAc.setRateFlatMinimum(new BigDecimal(0.00));
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAc.setEnteredBy(user);
                lclQuoteAc.setEnteredDatetime(new Date());
                lclQuoteAc.setModifiedBy(user);
                lclQuoteAc.setModifiedDatetime(new Date());
                lclQuoteAc.setTransDatetime(new Date());
                lclQuoteAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                lclquoteacdao.saveOrUpdate(lclQuoteAc);
            }
        }
    }

    public void saveCAFCharge(ChargesInfoBean cinfobean, Long fileNumberId, User user) throws Exception {
        LclQuoteAc lclQuoteAc = new LclQuoteAc();
        lclQuoteAc.setApBillToParty("C");
        lclQuoteAc.setArBillToParty("C");
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            if (lclFileNumber == null) {
                lclFileNumber = new LclFileNumber(fileNumberId);
            }
            lclQuoteAc.setLclFileNumber(lclFileNumber);
        }
        if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
            GlMapping glmapping = null;
            if (cinfobean.getChargeCode().equalsIgnoreCase("OFBARR") || cinfobean.getChargeCode().equalsIgnoreCase("TTBARR")) {
                glmapping = glmappingdao.getByProperty("chargeCode", cinfobean.getChargeCode());
            } else {
                glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
            }
            lclQuoteAc.setArglMapping(glmapping);
            lclQuoteAc.setApglMapping(glmapping);
        }
        lclQuoteAc.setTransDatetime(new Date());
        if (cinfobean.getRatePerUnitUom() != null) {
            if (cinfobean.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                lclQuoteAc.setRatePerUnit(cinfobean.getRatePerUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                lclQuoteAc.setRatePerUnit(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        if (cinfobean.getRate() != null) {
            lclQuoteAc.setArAmount(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclQuoteAc.setEnteredBy(user);
        lclQuoteAc.setModifiedBy(user);
        lclQuoteAc.setEnteredDatetime(new Date());
        lclQuoteAc.setModifiedDatetime(new Date());
        lclQuoteAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclQuoteAc.setRateUom(engmet);
        if (engmet != null && engmet.equalsIgnoreCase("E")) {
            lclQuoteAc.setRateUom("I");
        }
        if (cinfobean.getRatePerWeightUnit() != null) {
            lclQuoteAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerWeightUnitDiv() != null) {
            lclQuoteAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnit() != null) {
            lclQuoteAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnitDiv() != null) {
            lclQuoteAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclQuoteAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
        if (cinfobean.getMinCharge() != null) {
            lclQuoteAc.setRateFlatMinimum(cinfobean.getMinCharge().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerUnitDiv() != null) {
            lclQuoteAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getBookingPieceId() != null && cinfobean.getBookingPieceId() > 0) {
            lclQuoteAc.setLclQuotePiece(lclquotepiecedao.findById(cinfobean.getBookingPieceId()));
        }
        if (CommonUtils.isNotEmpty(lclQuoteAc.getArglMapping().getChargeCode()) && lclQuoteAc.getArglMapping().getChargeCode().substring(0, 2).equalsIgnoreCase("TT")) {//T&T charge has auto check
            lclQuoteAc.setBundleIntoOf(true);
        } else {
            lclQuoteAc.setBundleIntoOf(false);
        }
        lclQuoteAc.setPrintOnBl(true);
        if (fileNumberId != null && fileNumberId > 0) {
            lclquoteacdao.save(lclQuoteAc);
        } else {
            quoteAcList.add(lclQuoteAc);
        }
    }

    public void reCalculateManualCharges(Long fileNumberId, User user) throws Exception {
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
            Double calculatedWeight = 0.00;
            Double calculatedMeasure = 0.00;
            //String shipmentType = "", pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
            List<LclQuoteAc> quoteAcList = lclQuoteAcDAO.getLclCostByFileNumberME(fileNumberId, true);
            List<LclQuotePiece> lclBookingPiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);

            if (CommonUtils.isNotEmpty(quoteAcList)) {
                for (LclQuoteAc quoteAc : quoteAcList) {

                    quoteAc.setRatePerWeightUnit(null != quoteAc.getRatePerWeightUnit()
                            ? new BigDecimal(quoteAc.getRatePerWeightUnit().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setRatePerVolumeUnit(null != quoteAc.getRatePerVolumeUnit()
                            ? new BigDecimal(quoteAc.getRatePerVolumeUnit().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setRateFlatMinimum(null != quoteAc.getRateFlatMinimum()
                            ? new BigDecimal(quoteAc.getRateFlatMinimum().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setArAmount(null != quoteAc.getRatePerUnit()
                            ? new BigDecimal(quoteAc.getRatePerUnit().doubleValue()) : BigDecimal.ZERO);

                    double calculateWeiMea = 0.00;
                    if (quoteAc.getRatePerWeightUnit().doubleValue() > 0.00 || quoteAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                            totalMeasure = 0.00;
                            totalWeight = 0.00;
                            for (LclQuotePiece piece : lclBookingPiecesList) {
                                Double weightDouble = 0.00;
                                Double weightMeasure = 0.00;
                                if (null != quoteAc.getRateUom()) {
                                    if (("I").equalsIgnoreCase(quoteAc.getRateUom())) {
                                        if (piece.getBookedWeightImperial() != null && piece.getBookedWeightImperial().doubleValue() != 0.00) {
                                            weightDouble = piece.getBookedWeightImperial().doubleValue();
                                        }
                                        if (piece.getBookedVolumeImperial() != null && piece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                            weightMeasure = piece.getBookedVolumeImperial().doubleValue();
                                        }
                                    } else if (("M").equalsIgnoreCase(quoteAc.getRateUom())) {
                                        if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                                            weightDouble = piece.getBookedWeightMetric().doubleValue();//kgs
                                        }
                                        if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                            weightMeasure = piece.getBookedVolumeMetric().doubleValue();//cbm
                                        }
                                    }
                                    totalWeight = totalWeight + weightDouble;
                                    totalMeasure = totalMeasure + weightMeasure;
                                }
                            }
                        }
                        if (engmet != null && !"".equalsIgnoreCase(engmet)) {
                            if (engmet.equals("E")) {
                                if (quoteAc.getRateUom().equalsIgnoreCase("M")) {
                                    calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(quoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                    calculatedMeasure = totalMeasure * lclUtils.convertToCft(quoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                                } else {
                                    calculatedWeight = (totalWeight / 100) * quoteAc.getRatePerWeightUnit().doubleValue();
                                    calculatedMeasure = totalMeasure * quoteAc.getRatePerVolumeUnit().doubleValue();
                                }
                                quoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                            } else if (engmet.equals("M")) {
                                if (quoteAc.getRateUom().equalsIgnoreCase("I")) {
                                    calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(quoteAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                    calculatedMeasure = totalMeasure * lclUtils.convertToCbm(quoteAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                                } else {
                                    calculatedWeight = (totalWeight / 1000) * quoteAc.getRatePerWeightUnit().doubleValue();
                                    calculatedMeasure = totalMeasure * quoteAc.getRatePerVolumeUnit().doubleValue();
                                }
                                quoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                            }
                        }//end of else if engmet
                        quoteAc.setRatePerUnitDiv(quoteAc.getRatePerWeightUnitDiv());
                        quoteAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= quoteAc.getRateFlatMinimum().doubleValue()) {
                            calculateWeiMea = calculatedWeight;
                            quoteAc.setRatePerUnitUom("W");
                            quoteAc.setRatePerUnitDiv(quoteAc.getRatePerVolumeUnitDiv());
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= quoteAc.getRateFlatMinimum().doubleValue()) {
                            calculateWeiMea = calculatedMeasure;
                            quoteAc.setRatePerUnitUom("V");
                            quoteAc.setRatePerUnitDiv(quoteAc.getRatePerVolumeUnitDiv());
                        } else {
                            calculateWeiMea = quoteAc.getRateFlatMinimum().doubleValue();
                            quoteAc.setRatePerUnitUom("M");
                        }
                        if(quoteAc.getArglMapping().getChargeCode().equals("DESTWM")){
                        quoteAc.setArAmount(new BigDecimal(quoteAc.getArAmount().doubleValue()));
                        }else{
                        quoteAc.setArAmount(new BigDecimal(calculateWeiMea + quoteAc.getArAmount().doubleValue()));
                        }
                    }

                    quoteAc.setCostWeight(null != quoteAc.getCostWeight()
                            ? new BigDecimal(quoteAc.getCostWeight().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setCostMeasure(null != quoteAc.getCostMeasure()
                            ? new BigDecimal(quoteAc.getCostMeasure().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setCostMinimum(null != quoteAc.getCostMinimum()
                            ? new BigDecimal(quoteAc.getCostMinimum().doubleValue()) : BigDecimal.ZERO);
                    quoteAc.setApAmount(null != quoteAc.getCostFlatrateAmount()
                            ? new BigDecimal(quoteAc.getCostFlatrateAmount().doubleValue()) : BigDecimal.ZERO);

                    if (quoteAc.getCostWeight().doubleValue() > 0.00 || quoteAc.getCostMeasure().doubleValue() > 0.00) {
                        if (engmet != null && !"".equalsIgnoreCase(engmet)) {
                            if (engmet.equals("E")) {
                                if (quoteAc.getRateUom().equalsIgnoreCase("M")) {
                                    calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(quoteAc.getCostWeight().doubleValue()).doubleValue();
                                    calculatedMeasure = totalMeasure * lclUtils.convertToCft(quoteAc.getCostMeasure().doubleValue()).doubleValue();
                                } else {
                                    calculatedWeight = (totalWeight / 100) * quoteAc.getCostWeight().doubleValue();
                                    calculatedMeasure = totalMeasure * quoteAc.getCostMeasure().doubleValue();
                                }
                            } else if (engmet.equals("M")) {
                                if (quoteAc.getRateUom().equalsIgnoreCase("I")) {
                                    calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(quoteAc.getCostWeight().doubleValue()).doubleValue();
                                    calculatedMeasure = totalMeasure * lclUtils.convertToCbm(quoteAc.getCostMeasure().doubleValue()).doubleValue();
                                } else {
                                    calculatedWeight = (totalWeight / 1000) * quoteAc.getCostWeight().doubleValue();
                                    calculatedMeasure = totalMeasure * quoteAc.getCostMeasure().doubleValue();
                                }
                            }
                        }//end of else if engmet
                        double calculateCostWeiMei = 0.00;
                        if (calculatedWeight >= calculatedMeasure && calculatedWeight >= quoteAc.getCostMinimum().doubleValue()) {
                            calculateCostWeiMei = calculatedWeight;
                        } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= quoteAc.getCostMinimum().doubleValue()) {
                            calculateCostWeiMei = calculatedMeasure;
                        } else {
                            calculateCostWeiMei = quoteAc.getCostMinimum().doubleValue();
                        }
                        quoteAc.setApAmount(new BigDecimal(calculateCostWeiMei + quoteAc.getApAmount().doubleValue()));
                    }
                    if (quoteAc.getArglMapping().isDestinationServices()
                            && !quoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("DTHC PREPAID")) {
                        String chargeCode = quoteAc.getArglMapping().getChargeCode();
                        BigDecimal costAmount = quoteAc.getApAmount();
                        BigDecimal destinationDiff = quoteAc.getArAmount().subtract(quoteAc.getApAmount());
                        String min = new PropertyDAO().getProperty(chargeCode.equalsIgnoreCase("ONCARR")
                                ? "Destination Services O/C Min Profit" : "Destination Services DAP/DDP/Delivery min profit");
                        String max = new PropertyDAO().getProperty(chargeCode.equalsIgnoreCase("ONCARR")
                                ? "Destination Services O/C Max Profit" : "Destination Services DAP/DDP/Delivery max profit");
                        int profitMin = !"".equalsIgnoreCase(min) ? Integer.parseInt(min) : 0;
                        int profitMax = !"".equalsIgnoreCase(min) ? Integer.parseInt(max) : 0;
                        if (destinationDiff.intValue() <= profitMin) {
                            quoteAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMin));
                        } else if (destinationDiff.intValue() >= profitMax) {
                            quoteAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMax));
                        } else {
                            quoteAc.setArAmount(destinationDiff);
                        }
                    }
                    quoteAc.setTransDatetime(new Date());
                    quoteAc.setModifiedBy(user);
                    quoteAc.setModifiedDatetime(new Date());
                    lclQuoteAcDAO.saveOrUpdate(quoteAc);
                }
            }
        }
    }

    public void calculaterelayTTCharge(String pooorigin, String polorigin,
            List<LclQuotePiece> lclQuotePiecesList, String engmet, User user, Long fileNumberId,
            LclQuoteAc lclQuoteAc, GlMapping glMapping, String BlueScreenTTRevChgCode, HttpServletRequest request) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = new String();
        if (CommonUtils.isNotEmpty(lclQuotePiecesList)) {
            totalMeasure = 0.00;
            totalWeight = 0.00;
            for (LclQuotePiece piece : lclQuotePiecesList) {
                Double weightDouble = 0.00;
                Double weightMeasure = 0.00;
                if (null != lclQuoteAc.getRateUom()) {
                    if (("I").equalsIgnoreCase(lclQuoteAc.getRateUom())) {
                        if (piece.getActualWeightImperial() != null && piece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightDouble = piece.getActualWeightImperial().doubleValue();
                        } else if (piece.getBookedWeightImperial() != null && piece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightDouble = piece.getBookedWeightImperial().doubleValue();
                        }
                        if (piece.getActualVolumeImperial() != null && piece.getActualVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = piece.getActualVolumeImperial().doubleValue();
                        } else if (piece.getBookedVolumeImperial() != null && piece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = piece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if (("M").equalsIgnoreCase(lclQuoteAc.getRateUom())) {
                        if (piece.getActualWeightMetric() != null && piece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightDouble = piece.getActualWeightMetric().doubleValue();
                        } else if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightDouble = piece.getBookedWeightMetric().doubleValue();//kgs
                        }
                        if (piece.getActualVolumeMetric() != null && piece.getActualVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = piece.getActualVolumeMetric().doubleValue();
                        } else if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = piece.getBookedVolumeMetric().doubleValue();//cbm
                        }
                    }
                    totalWeight = totalWeight + weightDouble;
                    totalMeasure = totalMeasure + weightMeasure;
                }
            }
        }

        for (int j = 0; j < lclQuotePiecesList.size(); j++) {
            LclQuotePiece lclBookingPiece = (LclQuotePiece) lclQuotePiecesList.get(j);
            ChargesInfoBean cinfobean = new ChargesInfoBean();
            Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, polorigin, lclBookingPiece.getCommodityType().getCode(), BlueScreenTTRevChgCode);
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, polorigin, "000000", BlueScreenTTRevChgCode);
            }//e stdcharges using pooorigin and destinationpod and commodity
            if (portChargeArray != null && portChargeArray.length > 0) {
                Integer chgType = null;
                if (portChargeArray[0] != null && !portChargeArray[0].toString().trim().equals("")) {
                    chgType = Integer.parseInt(portChargeArray[0].toString());
                }
                if (chgType != null) {
                    if (chgType == 1) {
                        Double flatrate = 0.0;
                        if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                            flatrate = Double.parseDouble(portChargeArray[1].toString());
                        }
                        if (engmet != null) {
                            lclQuoteAc.setRateUom(engmet);
                        }
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclQuoteAc.setRateUom("I");
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
                        if (lclQuoteAc != null) {
                            lclQuoteAc.setRatePerUnit(bd);
                            lclQuoteAc.setArAmount(bd);

                            lclQuoteAc.setRatePerUnitUom("FL");
                        }
                    }//end of charge type 1 if condition
                    //String chdCode = null;
                    if (chgType == 3) {
                        String rateUOM = new String();
                        Double cuftrt = 0.0;
                        Double wghtrt = 0.0;
                        Double cbmrt = 0.0;
                        Double kgsrt = 0.0;
                        Double minchg = 0.0;
                        Double calculatedWeight = 0.0;
                        Double calculatedMeasure = 0.0;
                        Double finalValue = 0.0;
                        BigDecimal weightDiv = null;
                        BigDecimal measureDiv = new BigDecimal(1000);
                        if (portChargeArray[2] != null && !portChargeArray[2].toString().trim().equals("")) {
                            cuftrt = Double.parseDouble(portChargeArray[2].toString());
                        }
                        if (portChargeArray[3] != null && !portChargeArray[3].toString().trim().equals("")) {
                            wghtrt = Double.parseDouble(portChargeArray[3].toString());
                        }
                        if (portChargeArray[5] != null && !portChargeArray[5].toString().trim().equals("")) {
                            cbmrt = Double.parseDouble(portChargeArray[5].toString());
                        }
                        if (portChargeArray[6] != null && !portChargeArray[6].toString().trim().equals("")) {
                            kgsrt = Double.parseDouble(portChargeArray[6].toString());
                        }
                        if (portChargeArray[4] != null && !portChargeArray[4].toString().trim().equals("")) {
                            minchg = Double.parseDouble(portChargeArray[4].toString());
                        }
                        if (engmet != null) {
                            if (engmet.equalsIgnoreCase("E")) {
                                weightDiv = new BigDecimal(100);
                                calculatedWeight = (totalWeight / 100) * wghtrt;
                                calculatedMeasure = totalMeasure * cuftrt;
                                lclQuoteAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                                lclQuoteAc.setRatePerUnitDiv(new BigDecimal(100));
                                lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(100));

                            } else if (engmet.equalsIgnoreCase("M")) {
                                weightDiv = new BigDecimal(1000);
                                calculatedWeight = (totalWeight / 1000) * kgsrt;
                                calculatedMeasure = totalMeasure * cbmrt;

                                lclQuoteAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
                                lclQuoteAc.setRatePerUnitDiv(new BigDecimal(1000));
                                lclQuoteAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                            }
                        }
                        if (calculatedWeight >= calculatedMeasure) {
                            finalValue = calculatedWeight;
                            rateUOM = "W";
                        } else {
                            finalValue = calculatedMeasure;
                            rateUOM = "V";
                        }
                        if (finalValue <= minchg) {
                            finalValue = minchg;
                            rateUOM = "M";
                        }
                        BigDecimal d = new BigDecimal(finalValue);
                        lclQuoteAc.setRatePerUnit(d);
                        if (rateUOM.equals("W")) {
                            lclQuoteAc.setRatePerUnitDiv(cinfobean.getRatePerWeightUnitDiv());
                        } else if (rateUOM.equals("V")) {
                            lclQuoteAc.setRatePerUnitDiv(cinfobean.getRatePerVolumeUnitDiv());
                        }
                        lclQuoteAc.setRateFlatMinimum(new BigDecimal(minchg));
                        Date dat = new Date();
                        lclQuoteAc.setArAmount(d);
                        lclQuoteAc.setBundleIntoOf(false);
                        lclQuoteAc.setModifiedBy(user);
                        lclQuoteAc.setModifiedDatetime(dat);
                        lclQuoteAc.setPrintOnBl(true);
                        lclQuoteAc.setRatePerUnitUom(rateUOM);
                        lclQuoteAc.setTransDatetime(dat);
                        lclquoteacdao.saveOrUpdate(lclQuoteAc);
                    }
                }

            } else if (CommonUtils.isNotEmpty(lclQuoteAc.getId())) {
                lclQuoteAc.setRatePerWeightUnit(new BigDecimal(0.00));
                lclQuoteAc.setRatePerVolumeUnit(new BigDecimal(0.00));
                lclQuoteAc.setRateUom("I");
                lclQuoteAc.setRatePerUnit(new BigDecimal(0.000));
                Date dat = new Date();
                BigDecimal d = new BigDecimal(0.00);
                lclQuoteAc.setArAmount(d);
                lclQuoteAc.setBundleIntoOf(false);
                lclQuoteAc.setModifiedBy(user);
                lclQuoteAc.setModifiedDatetime(dat);
                lclQuoteAc.setPrintOnBl(true);
                lclQuoteAc.setTransDatetime(dat);
                lclquoteacdao.saveOrUpdate(lclQuoteAc);
            }
        }
    }
}

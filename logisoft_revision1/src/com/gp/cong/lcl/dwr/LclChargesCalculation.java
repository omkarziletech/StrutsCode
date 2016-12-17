/**
 * @author Saravanan
 *
 */
package com.gp.cong.lcl.dwr;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
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
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclWarehsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class LclChargesCalculation {

    private static final Logger log = Logger.getLogger(LclChargesCalculation.class);
    private String CIFValue;
    private String pcb;
    private Double totalOceanFreight = 0.0, totalBarrelCharges = 0.0, totalBarellTTA = 0.0;
    private Double totalWeight = 0.0;
    private Double totalMeasure = 0.0;
    private Double flatrate = 0.0;
    private Double insurt = 0.0;
    private Double insamt = 0.0;
    private Double insMinChg = 0.0;
    private Double rolledUpWeight = 0.00, rolledUpMeasure = 0.00, rolledUpMinchg = 0.00;
    private Long totalcuft;
    private Integer totalpieces;
    private String databaseSchema;
    private DecimalFormat df = new DecimalFormat("#########.00");
    private Map chargesInfoMap = new HashMap();
    private Map totalPerMap = new HashMap();
    private List<ChargesInfoBean> quoteRateList = new ArrayList();
    private List<ChargesInfoBean> chargesInfoList = new ArrayList();
    private List<LclBookingAc> bookingAcList = new ArrayList();
    private String ttChargeCode1;
    private String ttChargeCode2;
    private String portDifferential;
    private LCLRatesDAO lclratesdao;
    private LclCostChargeDAO lclcostchargedao = new LclCostChargeDAO();
    private PortsDAO portsdao = new PortsDAO();
    private GlMappingDAO glmappingdao = new GlMappingDAO();
    private LclBookingPieceDAO lclbookingpiecedao = new LclBookingPieceDAO();
    private int countWithBarrell = 0, countWithoutBarrell = 0;
    private String engmet, toZip, pickupDisable;
    private BigDecimal weight = new BigDecimal(0.000);
    private BigDecimal measure = new BigDecimal(0.000);
    private BigDecimal zerobigdecimalvalue = new BigDecimal(0.00);
    private LclUtils lclUtils = new LclUtils();
    private String ofratebasis = new String();
    private String stdchgratebasis = new String();
    private String highVolumeMessage = new String();
    private Double measureForI;
    private Double weightForI;
    private Double measureForM;
    private Double weightForM;
    private String rateuom;
    private Ports ports;
    private RefTerminal refterminal;
    private LclFileNumber lclFileNumber;
    private Double totalSpotWeight = 0.0;
    private boolean ttFound = false;
    private boolean isArGlmappingFlag = false;
    private String glMappingBlueChgCode;
    private String billingType;
    private String classchgcode;
    private List destchargesInfoList = new ArrayList();

    public RefTerminal getRefterminal() {
        return refterminal;
    }

    public void setRefterminal(RefTerminal refterminal) {
        this.refterminal = refterminal;
    }

    public Ports getPorts() {
        return ports;
    }

    public void setPorts(Ports ports) {
        this.ports = ports;
    }

    public String getHighVolumeMessage() {
        return highVolumeMessage;
    }

    public void setHighVolumeMessage(String highVolumeMessage) {
        this.highVolumeMessage = highVolumeMessage;
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

    public Double getMeasureForI() {
        return measureForI;
    }

    public void setMeasureForI(Double measureForI) {
        this.measureForI = measureForI;
    }

    public Double getWeightForI() {
        return weightForI;
    }

    public void setWeightForI(Double weightForI) {
        this.weightForI = weightForI;
    }

    public Double getMeasureForM() {
        return measureForM;
    }

    public void setMeasureForM(Double measureForM) {
        this.measureForM = measureForM;
    }

    public Double getWeightForM() {
        return weightForM;
    }

    public void setWeightForM(Double weightForM) {
        this.weightForM = weightForM;
    }

    public String getRateuom() {
        return rateuom;
    }

    public void setRateuom(String rateuom) {
        this.rateuom = rateuom;
    }

    public String getPortDifferential() {
        return portDifferential;
    }

    public void setPortDifferential(String portDifferential) {
        this.portDifferential = portDifferential;
    }

    public List<LclBookingAc> getBookingAcList() {
        return bookingAcList;
    }

    public void setBookingAcList(List<LclBookingAc> bookingAcList) {
        this.bookingAcList = bookingAcList;
    }

    public Double getTotalSpotWeight() {
        return totalSpotWeight;
    }

    public void setTotalSpotWeight(Double totalSpotWeight) {
        this.totalSpotWeight = totalSpotWeight;
    }

    /**
     *
     * @param lclBookingPiecesList - List Of Selected Commodities
     * @param origin - Selected Origin
     * @param dest - Selected Destination
     * @param ports - Port for Selected Destination
     * @param comminfobean - Selected Commodity Information
     * @param carrier - Selected Carrier for Pick up
     * @param overDimList - Selected Over Dimensional
     */
    public void calculateRates(String origin, String destination, String pol, String pod, Long fileNumberId, List lclBookingPiecesList, User user,
            String pickupYesNo, String insuranceYesNo, BigDecimal valueOfGoods, String rateType, String buttonValue, RoutingOptionsBean routingbean,
            String pickupReadyDate, String fromZip, HttpSession session, Boolean calcHeavy, String deliveryMetro, String pcBoth, String unLocationCode,
            String radioValue, HttpServletRequest request, String billToParty) throws Exception {
        log.info("calculateRates started. " + new Date());
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

        boolean unbarrell = false;
        billingType = pcBoth;
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        pickupDisable = LoadLogisoftProperties.getProperty("application.enableCTS");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        LclWarehsDAO lclWarehsDAO = new LclWarehsDAO();
        Zipcode zipcode = null;
        LclBookingPad lclBookingPad = null;
        LclBookingExport lclBookingExport = null;
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
        session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();

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
                if (null != warehs && warehs[5] != null) {
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
        log.info("refterminal " + refterminal);
        log.info("refterminalpol " + refterminalpol);
        log.info("ports " + ports);
        log.info("portspod " + portspod);
        log.info("engmet " + engmet);
        if ((refterminal != null || refterminalpol != null) && (ports != null || portspod != null)) {
            log.info("lclUtils.calculateRates ");
            lclUtils.calculateRates(refterminal, ports, lclBookingPiecesList, lclratesdao, engmet, fileNumberId);
            if (buttonValue.equalsIgnoreCase("C")) {
                highVolumeMessage = lclUtils.getHighVolumeMessage();
            }
            //Get the count of the selected Commodity
            calculateCount(lclBookingPiecesList);
            //Check the Fields Of T&T Charges

            if (refterminal != null && refterminal.getGenericCode1() != null && refterminal.getGenericCode1().getCode() != null && !refterminal.getGenericCode1().getCode().trim().equals("")) {
                this.setTtChargeCode1(refterminal.getGenericCode1().getCode());
            }
            //Calculate the Ocean Freight Rate
            calculateOfRate(countWithBarrell, pooorigin, polorigin, destinationpod, destinationfd, fileNumberId, lclBookingPiecesList, user, refterminal, buttonValue, routingbean);
            int index = 0;
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
            //Loop through the selected Commodity
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                if (fileNumberId != null && fileNumberId > 0 && lclBookingPiece.getCommodityType() != null) {
                    comnum = lclBookingPiece.getCommodityType().getCode();
                } else {
                    comnum = lclBookingPiece.getCommNo();
                }
                if (buttonValue.equalsIgnoreCase("C") && j == 0) {
                    ofratebasis += comnum;
                }
                String classChargeCode = null;
                //Object[] classchargeCode = lclratesdao.getOfcertCommodityList(comnum);
                //Object[] classchargeCode = lclratesdao.destinationCode(comnum);
                //Checking whether the object has commodity code
                //need to check
                if (lclBookingPiece.getActualWeightImperial() != null) {
                    weight = weight.add(lclBookingPiece.getActualWeightImperial());
                } else if (lclBookingPiece.getBookedWeightImperial() != null) {
                    weight = weight.add(lclBookingPiece.getBookedWeightImperial());
                }
                if (lclBookingPiece.getActualVolumeImperial() != null) {
                    measure = measure.add(lclBookingPiece.getActualVolumeImperial());
                } else if (lclBookingPiece.getBookedVolumeImperial() != null) {
                    measure = measure.add(lclBookingPiece.getBookedVolumeImperial());
                }
                if ((lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("")
                        && lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals(""))
                        || lclBookingPiece.isIsBarrel()) {
                    index++;

                    if (!lclBookingPiece.isIsBarrel()) {
                        unbarrell = true;
                        /**
                         * Calculating total weights and total measures for
                         * calculating rates other than Ocean Freight
                         */
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if (engmet != null) {
                            if (engmet.equals("E")) {
                                if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                                }
                                if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                                }

                            } else if (engmet.equals("M")) {
                                if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                                }
                                if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
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
                        //Getting the stdcharges using pooorigin and destinationfd and comnum
                        stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, comnum);
                        String stdChgRateBasis = ofratebasis;
                        //If the object is null Getting the stdcharges using pooorigin and destinationfd and 0 commodity code
                        if (stdchargesObj == null) {
                            stdchargesObj = lclratesdao.findByOrgnDestComCde(pooorigin, destinationfd, "000000");
                            stdChgRateBasis = pooorigin + "-" + destinationfd + "-000000";
                        }//end of stdcharges null checking
                        //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
                        if (stdchargesObj == null) {
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
                        lclBookingPiece.setStdchgRateBasis(stdChgRateBasis);
                        if (j == 0) {
                            this.setStdchgratebasis(stdChgRateBasis);
                        }
                    } //end of barrell if condition
                    //If the object is not null performing the rate calculation
                    log.info("stdchargesObj " + stdchargesObj);
                    if (stdchargesObj != null && !lclBookingPiece.isIsBarrel()) {
//                       if (classchargeCode != null) {
//                            classchgcode = getChgCode(classchargeCode);
//                        }
                        //Getting All the Charge Codes For Calculating Rates
                        String chgcode = getAddedChgCode(stdchargesObj, lclBookingPiece, lclBookingPiecesList, insuranceYesNo, valueOfGoods, calcHeavy, buttonValue, deliveryMetro, pcBoth, ports, includedestinationfee);
                        //chgcode = chgcode + "," + classchgcode;
                        log.info("added charge code. " + chgcode);
                        if (chgcode != null && !chgcode.trim().equals("")) {
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
                                log.info("final portChargesList. " + portChargesList);
                                //Check portChargesList null
                                if (portChargesList != null && portChargesList.size() > 0) {
                                    //Loop through portChargesList
                                    for (int i = 0; i < portChargesList.size(); i++) {
                                        Object[] portcharges = (Object[]) portChargesList.get(i);
                                        //Calculate Rates for all portcharges
                                        setRates(portcharges, lclBookingPiece, comnum, index, countWithoutBarrell, fileNumberId,
                                                routingbean, buttonValue);
                                    }//end of portcharges for loop
                                }//end of if condition portChargesList null checking
                            }//end of stringtokenizer while loop
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
                    if (CommonUtils.isNotEmpty(cinfobean.getChargesDesc())) {
                        if (!cinfobean.getChargesDesc().equals("OCEAN FREIGHT")) {
                            chargesInfoList.add(cinfobean);
                            if (classchgcode != null && !classchgcode.equals("")) {
                                StringTokenizer tokenizer = new StringTokenizer(classchgcode, ",");
                                while (tokenizer.hasMoreTokens()) {
                                    String splittedchgcode = tokenizer.nextToken();
                                    Integer code = Integer.parseInt(splittedchgcode);
                                    if (cinfobean.getChargeCode() != null && (cinfobean.getChargeCode().equals(code))) {
                                        chargesInfoList.remove(cinfobean);
                                        destchargesInfoList.add(cinfobean);
                                        setDestchargesInfoList(destchargesInfoList);
                                    }
                                }
                            }
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
            }
            if (buttonValue.equalsIgnoreCase("R")) {
                routingbean.setOfrateAmount("$" + NumberUtils.convertToTwoDecimal(rolledUpMeasure) + "/$"
                        + NumberUtils.convertToTwoDecimal(rolledUpWeight) + "/$" + NumberUtils.convertToTwoDecimal(rolledUpMinchg) + " Min");
            }
            if (insuranceYesNo != null && insuranceYesNo.equalsIgnoreCase("Y") && valueOfGoods != null) {
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                calculateInsuranceCharge(pooorigin, polorigin, destinationfd, destinationpod, lclBookingPiecesList, valueOfGoods.doubleValue(), user, fileNumberId,
                        null, glmapping, request, billToParty, buttonValue);
                // calculateInsurance(valueOfGoods.doubleValue(), buttonValue, null, fileNumberId,
                //      lclBookingPiecesList, null, glmapping, request, billToParty);
            }
            String realPath = session.getServletContext().getRealPath("/xml/");
            String fileName = "ctsresponse" + session.getId() + ".xml";
            CallCTSWebServices ctsweb = new CallCTSWebServices();
            if (buttonValue.equalsIgnoreCase("R")) {
                lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip, pickupReadyDate,
                        "" + weight, "" + measure, "CARRIER_CHARGE", null, "Exports");
            } else if (buttonValue.equalsIgnoreCase("C")) {
                if (fileNumberId != null && fileNumberId > 0) {
                    lclBookingPad = lclBookingPadDAO.getLclBookingPadByFileNumber(fileNumberId);
                }
                if (pickupDisable.equalsIgnoreCase("Y") && CommonUtils.isNotEmpty(pickupYesNo) && pickupYesNo.equalsIgnoreCase("Y")
                        && lclBookingPad != null && CommonUtils.isNotEmpty(lclBookingPad.getScac()) && CommonUtils.isNotEmpty(pickupReadyDate)) {
                    lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip,
                            pickupReadyDate, "" + weight, "" + measure, "CARRIER_CHARGE", "CARRIER_COST", "Exports");
                }
            }
            List<Carrier> carrierList = lclSession.getCarrierList();
            List<Carrier> carrierCostList = lclSession.getCarrierCostList();
            if (buttonValue.equalsIgnoreCase("C")) {
//                if (carrierList != null && carrierList.size() > 0 && carrierCostList != null && carrierCostList.size() > 0) { // inland charge added commented
//                    savePickupCharge(fileNumberId, user, carrierList, carrierCostList, lclBookingPad);
//                } else if (fileNumberId != null && lclBookingPad != null) {
//                    lclBookingPadDAO.update(lclBookingPad);
//                }
                // Ports portspol = portsdao.getByProperty("unLocationCode", pol);
                // String destinationpol="";
                //if (portspol != null && portspol.getEciportcode() != null && !portspol.getEciportcode().trim().equals("")) {
                //    destinationpol = portspol.getEciportcode();
                //}
                //    GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0059", "LCLE", "AR");
                // calculaterelayTTCharge(pooorigin, destinationpol,lclBookingPiecesList,
                //    engmet, user, fileNumberId, new LclBookingAc(), glmapping, request);
                saveCharges(fileNumberId, user, buttonValue, billToParty, includedestinationfee);
                if (fileNumberId != null && fileNumberId > 0) {
                    reCalculateManualCharges(fileNumberId, user);
                }
                lclSession.setIsArGlmappingFlag(isArGlmappingFlag);
                lclSession.setGlMappingBlueCode(glMappingBlueChgCode);
                if (unbarrell) {
                    if (totalPerMap != null && totalPerMap.size() > 0) {
                        addTotalPercentage(fileNumberId, user, billToParty);
                    }
                }
            } else if (buttonValue.equalsIgnoreCase("R")) {
                routingbean.setQuoteRateList(quoteRateList);

                if (pickupDisable.equalsIgnoreCase("Y")) {
                    if (carrierList != null && carrierList.size() > 0) {
                        routingbean.setTotalAmount(NumberUtils.convertToTwoDecimal(calculateTotalByQuoteRateList()));
                        routingbean.setHiddenTotalAmount(routingbean.getTotalAmount());
                        String propertyValue = LoadLogisoftProperties.getProperty("CTS carriers use lowest rates");
                        int defaultRateIndex = CommonUtils.isEmpty(propertyValue) || "0".equalsIgnoreCase(propertyValue)
                                ? 1 : Integer.parseInt(propertyValue) - 1;
                        defaultRateIndex = carrierList.size() > defaultRateIndex ? defaultRateIndex : 1;
                        defaultRateIndex = carrierList.size() == 1 ? 1 : defaultRateIndex;
                        Carrier carrier = (Carrier) carrierList.get(defaultRateIndex);
                        if (carrier != null) {
                            if (CommonUtils.isNotEmpty(carrier.getFinalcharge())) {
                                if (buttonValue.equalsIgnoreCase("R")) {
                                    routingbean.setCtsAmount("$" + carrier.getFinalcharge());
                                    Double total = Double.parseDouble(routingbean.getTotalAmount()) + Double.parseDouble(carrier.getFinalcharge());
                                    routingbean.setTotalAmount(NumberUtils.convertToTwoDecimal(total));
                                    routingbean.setHiddenTotalAmount(routingbean.getTotalAmount());
                                }
                            }
                            if (CommonUtils.isNotEmpty(carrier.getScac())) {
                                if (buttonValue.equalsIgnoreCase("R")) {
                                    routingbean.setScac("(" + carrier.getScac() + ")");
                                }
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

    public void calculateCount(List lclBookingPiecesList) {
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                if (lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("")
                        && lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals("")) {
                    countWithBarrell++;
                    if (!lclBookingPiece.isIsBarrel()) {
                        countWithoutBarrell++;
                    }
                }
            }
        }
    }

    /**
     * @param stdcharges - Stdcharge for the commodity
     * @param comminfobean - commodity information
     * @param lclBookingPiecesList - Commodity List
     * @param ports
     * @param overDimList
     * @return - Charge Codes with comma seperated values This method returns a
     * colletion of charge codes with comm seperated values
     */
    public String getAddedChgCode(Object[] stdcharges, LclBookingPiece lclBookingPiece,
            List lclBookingPiecesList, String insuranceYesNo, BigDecimal valueOfGoods, Boolean calcHeavy, String buttonValue,
            String deliveryMetro, String pcBoth, Ports ports, boolean includedestinationfee) throws Exception {
        String chgcod = "";
        String pcb = new String();
        String overDimChgCdeForWeight = "";
        String overDimChgCdeForMeasure = "";
        boolean densecargofound = false;
        if (calcHeavy != null && calcHeavy) {
            //one piece weighing more than 4000 lbs (convert 2000 kgs to lbs and get 4409 lbs, for example) Mantis#7818
            overDimChgCdeForWeight = getChargeCodeForHeavyLiftCharge(lclBookingPiecesList);
            //Dims 180 inches Mantis#7822
            overDimChgCdeForMeasure = getchgCdeForExtraLengthCharge(lclBookingPiece.getLclBookingPieceDetailList());
            //total shipment is 1000 kgs or greaterand the ratio of kgs to cbm is 1000 or greater Mantis#7826
            densecargofound = getchgCdeForDenseCargoFee(lclBookingPiecesList);
            if (densecargofound) {
                chgcod = chgcod + "251,";
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
        if (lclBookingPiece.isHazmat()) {
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
        if (overDimChgCdeForWeight != null && !overDimChgCdeForWeight.trim().equals("")) {
            chgcod = chgcod + overDimChgCdeForWeight + ",";
        }
        if (overDimChgCdeForMeasure != null && !overDimChgCdeForMeasure.trim().equals("")) {
            chgcod = chgcod + overDimChgCdeForMeasure + ",";
        }
        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(ports.getUnLocationCode());
        if (ncl[2] != null && ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")) {
            if (pcBoth != null && (pcBoth.equalsIgnoreCase("C") || pcBoth.equalsIgnoreCase("B"))) {
                String destid = ports.getEciportcode();
                if (destid != null) {
                    chgcod = chgcod + "5,";
                }
            }
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
    public void setRates(Object[] portcharges, LclBookingPiece lclBookingPiece, String comnum, int index, int count, Long fileNumberId,
            RoutingOptionsBean routingOptionsBean, String buttonValue) throws Exception {
        log.info("set rates method starts ");
        String chdCode = null;
        Integer chgType = null;
        String blueScreenChargeCode = null;
        // Object[] spotRateList =new LCLBookingDAO().getAllSpotRateValues(fileNumberId);
        // if(spotRateList != null && spotRateList.length>0){
        //  if(spotRateList[0]!=null && spotRateList[0].toString().equals("true")){
        //     if(chdCode.equals("0059")){
        //                       chdCode=null;
        //   }
        //   }
        //  }
        if (portcharges != null) {
            if (portcharges[0] != null && !portcharges[0].toString().trim().equals("")) {
                chdCode = portcharges[0].toString();
            }
            if (portcharges[0] != null && !portcharges[1].toString().trim().equals("")) {
                chgType = Integer.parseInt(portcharges[1].toString());
            }
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
                    cinfobean.setBookingPieceId(lclBookingPiece.getId());
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
                                    // if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 100) * wghtrt;
                                    calculatedMeasure = totalMeasure * cuftrt;
                                    // }
                                    ttcbmcftrate = cuftrt;
                                    ttlbskgsrate = wghtrt;
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    weightDiv = new BigDecimal(1000);
                                    // if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 1000) * kgsrt;
                                    calculatedMeasure = totalMeasure * cbmrt;
                                    //  }
                                    ttcbmcftrate = cbmrt;
                                    ttlbskgsrate = kgsrt;
                                }
                            }
                            //        if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
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
                            cinfobean.setBookingPieceId(lclBookingPiece.getId());
                            /*boolean ttchargecode1found = this.getTtChargeCode1() != null
                             && this.getTtChargeCode1().equals(blueScreenChargeCode);
                             boolean ttchargecode2found = this.getTtChargeCode2() != null
                             && this.getTtChargeCode2().equals(blueScreenChargeCode);
                             boolean portDifferentialfound = this.getPortDifferential() != null
                             && !this.getPortDifferential().equals("0000")
                             && this.getPortDifferential().equals(chdCode);
                             if (count == index) {
                             if ((ttchargecode1found || ttchargecode2found || portDifferentialfound)) {
                             Double flatRateMinimum = 0.0;
                             ChargesInfoBean ofratecinfobean = (ChargesInfoBean) chargesInfoMap.get("OCEAN FREIGHT");
                             Double addedofrate = ofratecinfobean.getRate().doubleValue() + finalValue;
                             ofratecinfobean.setRate(new BigDecimal(addedofrate));
                             ttcbmcft = ofratecinfobean.getRatePerVolumeUnit().doubleValue() + ttcbmcftrate;
                             ttlbskgs = ofratecinfobean.getRatePerWeightUnit().doubleValue() + ttlbskgsrate;
                             if (ofratecinfobean.getMinCharge() != null && minchg != null) {
                             flatRateMinimum = ofratecinfobean.getMinCharge().doubleValue() + minchg;
                             }
                             ofratecinfobean.setMinCharge(new BigDecimal(flatRateMinimum));
                             ofratecinfobean.setRatePerWeightUnit(new BigDecimal(ttlbskgs));
                             ofratecinfobean.setRatePerWeightUnitDiv(weightDiv);
                             ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(ttcbmcft));
                             ofratecinfobean.setRatePerVolumeUnitDiv(measureDiv);
                             if (buttonValue.equalsIgnoreCase("R")) {
                             routingOptionsBean.setOfrateAmount("$" + NumberUtils.convertToTwoDecimal(ttcbmcft) + "/$"
                             + NumberUtils.convertToTwoDecimal(ttlbskgs) + "/$" + NumberUtils.convertToTwoDecimal(flatRateMinimum) + " Min");
                             }
                             chargesInfoMap.put("OCEAN FREIGHT", ofratecinfobean);
                            
                             } else { */
                            chargesInfoMap.put(chargesDesc, cinfobean);
                            quoteRateList.add(cinfobean);
                            /*}
                             } else {
                             quoteRateList.add(cinfobean);
                             }*/
                        }//end of else condition
                    }//end of chdcode null if condition
                }//end of charge type 3 if condition
                //If charge type is 4 calculting Insurance
                if (chgType == 4) {
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
     * @param lclBookingPiece
     * @param carrier This method calculates the total percentage and add it to
     * charges list
     */
    public void addTotalPercentage(Long fileNumberId, User user, String billToParty) throws Exception {
        Iterator totPerIter = totalPerMap.values().iterator();
        boolean isCollectChargeContain = true;
        while (totPerIter.hasNext()) {
            TotalPercentageBean totbean = (TotalPercentageBean) totPerIter.next();
            Double finalValue = 0.0;
            if (totbean.getTotper() != null) {
                double collect_amount = lclcostchargedao.getBookingTotalCollectChages(fileNumberId);
                if (!billingType.equalsIgnoreCase("p")) {
                    finalValue = collect_amount * totbean.getTotper();
                    isCollectChargeContain = collect_amount > 0;
                }
            }
            if (totbean.getMinchg() != null && totbean.getMinchg() > finalValue) {
                finalValue = totbean.getMinchg();
            }
            if (isCollectChargeContain) {
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
                saveCAFCharge(cinfobean, fileNumberId, user, billToParty);
            }
            //chargesInfoList.add(cinfobean);
            //quoteRateList.add(cinfobean);
        }
    }

    /**
     * @param A - Entered Goods value This method calculates the insurance and
     * adds it to the charges list
     */
    public String calculateInsurance(Double A, String buttonValue, User user,
            Long fileNumberId, List<LclBookingPiece> lclCommodityList, LclBookingAc lclBookingAc,
            GlMapping glMapping, HttpServletRequest request, String billToParty) throws Exception {
        Double CIFValue = 0.0;
        Double B = 0.00;
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            if (lclCommodityList.size() == 1) {
                B = lclcostchargedao.getTotalLclCostWithoutParticularCharge(fileNumberId, glMapping.getId());
            } else {
                List<LclBookingAc> chargeList = lclcostchargedao.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_EXPORT);
                List<LclBookingAc> rolledUpChargesList = lclUtils.getRolledUpCharges(lclCommodityList, chargeList, engmet, "No");
                B = lclUtils.calculateTotalWithoutInsuranceByBookingAcList(rolledUpChargesList);

            }
        }
        if (B == 0.00) {
            B = calculateTotal();
        }
        Double C = 0.0;
        Double D = 0.0;
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
        new LCLBookingDAO().updateCIF(fileNumberId, cif);
        if (insamt != null) {
            insurance = (CIFValue / 100) * insurt;
        }
        if (insurance <= insMinChg) {
            insurance = insMinChg;
        }
        if (buttonValue.equalsIgnoreCase("I")) {
            Date d = new Date();
            if (lclBookingAc == null) {
                lclBookingAc = new LclBookingAc();
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclBookingAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclBookingAc.setArglMapping(glMapping);
                lclBookingAc.setEnteredBy(user);
                lclBookingAc.setEnteredDatetime(d);
                lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclBookingAc.setRatePerUnitUom("FL");
            }
            lclBookingAc.setArBillToParty(billToParty);
            BigDecimal insuranceBigDecimal = new BigDecimal(insurance);
            lclBookingAc.setTransDatetime(d);
            lclBookingAc.setArAmount(insuranceBigDecimal);
            lclBookingAc.setModifiedBy(user);
            lclBookingAc.setModifiedDatetime(d);
            lclBookingAc.setRatePerWeightUnit(new BigDecimal(insurt));
            lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(insamt));
            lclBookingAc.setBundleIntoOf(false);
            lclBookingAc.setPrintOnBl(true);
            lclcostchargedao.saveOrUpdate(lclBookingAc);
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

    public Double calculateTotalByQuoteRateList() {
        Double total = 0.00;
        if (quoteRateList != null && quoteRateList.size() > 0) {
            for (int i = 0; i < quoteRateList.size(); i++) {
                ChargesInfoBean cinfobean = quoteRateList.get(i);
                total = total + cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        return total;
    }

    /**
     * This method calculates the total for total percentage
     *
     * @return
     */
    public Double calculateTotalForTotalPercentage(Long fileNumberId) throws Exception {
        Double total = 0.0;
        List<LclBookingPiece> lclBookingPieceList = null;
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
            lclBookingPieceList = lclbookingpiecedao.findByProperty("lclFileNumber.id", fileNumberId);
            if (lclBookingPieceList.size() == 1) {
                total = lclcostchargedao.getTotalLclCostByFileNumber(fileNumberId).doubleValue();
            } else {
                List<LclBookingAc> chargeList = lclcostchargedao.getLclCostByFileNumberAsc(fileNumberId, LclCommonConstant.LCL_EXPORT);
                List<LclBookingAc> lclBookingAcList = lclUtils.getRolledUpCharges(lclBookingPieceList, chargeList, ports.getEngmet(), "No");
                total = Double.parseDouble(lclUtils.calculateTotalByBookingAcList(lclBookingAcList));
            }
        }
        return total;
    }

    public String getChargeCodeForHeavyLiftCharge(List lclBookingPiecesList) {
        Double avgWeight = 0.0;
        int pieces = 1;
        String chargeCode = "";
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        boolean thirdCndnSatisfied = false;
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
            if (lclBookingPiece.getActualPieceCount() != null && lclBookingPiece.getActualPieceCount() != 0) {
                pieces = lclBookingPiece.getActualPieceCount();
            } else if (lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount() != 0) {
                pieces = lclBookingPiece.getBookedPieceCount();
            } else {
                pieces = 1;
            }
            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                avgWeight = lclBookingPiece.getActualWeightImperial().doubleValue() / pieces;
            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                avgWeight = lclBookingPiece.getBookedWeightImperial().doubleValue() / pieces;
            }
            if (avgWeight >= 4000 && avgWeight < 6000) {
                firstCndnSatisfied = true;
            } else if (avgWeight >= 6000 && avgWeight < 8000) {
                secondCndnSatisfied = true;
            } else if (avgWeight >= 8000) {
                thirdCndnSatisfied = true;
            }
        }
        if (thirdCndnSatisfied) {
            chargeCode = "241";
        } else if (secondCndnSatisfied) {
            chargeCode = "240";
        } else if (firstCndnSatisfied) {
            chargeCode = "31";
        }
        return chargeCode;
    }

    public String getchgCdeForExtraLengthCharge(List<LclBookingPieceDetail> lclBookingPieceDetailList) {
        String chargeCode = new String();
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        if (lclBookingPieceDetailList != null && lclBookingPieceDetailList.size() > 0) {
            for (int j = 0; j < lclBookingPieceDetailList.size(); j++) {
                LclBookingPieceDetail lclBookingPieceDetail = (LclBookingPieceDetail) lclBookingPieceDetailList.get(j);
                if (lclBookingPieceDetail.getActualLength() != null) {
                    if (lclBookingPieceDetail.getActualLength().doubleValue() >= 180 && lclBookingPieceDetail.getActualLength().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBookingPieceDetail.getActualLength().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclBookingPieceDetail.getActualHeight() != null) {
                    if (lclBookingPieceDetail.getActualHeight().doubleValue() >= 180 && lclBookingPieceDetail.getActualHeight().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBookingPieceDetail.getActualHeight().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclBookingPieceDetail.getActualWidth() != null) {
                    if (lclBookingPieceDetail.getActualWidth().doubleValue() >= 180 && lclBookingPieceDetail.getActualWidth().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBookingPieceDetail.getActualWidth().doubleValue() >= 360) {
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

    public boolean getchgCdeForDenseCargoFee(List lclBookingPieceList) throws Exception {
        boolean cndnSatisfied = false;
        if (engmet != null && engmet.equalsIgnoreCase("M")
                && lclBookingPieceList != null && lclBookingPieceList.size() > 0) {
            Double totalkgs = 0.0;
            Double totalcbm = 0.0;
            for (int j = 0; j < lclBookingPieceList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPieceList.get(j);
                if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                    totalkgs += lclBookingPiece.getActualWeightMetric().doubleValue();
                } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                    totalkgs += lclBookingPiece.getBookedWeightMetric().doubleValue();
                }
                if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                    totalcbm += lclBookingPiece.getActualVolumeMetric().doubleValue();
                } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                    totalcbm += lclBookingPiece.getBookedVolumeMetric().doubleValue();
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

    public List getDestchargesInfoList() {
        return destchargesInfoList;
    }

    public void setDestchargesInfoList(List destchargesInfoList) {
        this.destchargesInfoList = destchargesInfoList;
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
     * @param lclBookingPiecesList
     * @param count
     * @param ports
     * @param loctn This method Calculates OfRate and add the OfRate to the List
     */
    public void calculateOfRate(int count, String pooorigin, String polorigin, String destinationpod, String destinationfd,
            Long fileNumberId, List lclBookingPiecesList, User user, RefTerminal refterminal, String buttonValue, RoutingOptionsBean routingbean) throws Exception {
        log.info("calculateOfRate ");
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
        boolean ofratefound = false;
        Double calculatedWeight = 0.0;
        Double calculatedMeasure = 0.0;
        Double finalValue = 0.0;
        String commodityCode = new String();
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
            if (fileNumberId != null && fileNumberId > 0 && lclBookingPiece.getCommodityType() != null) {
                commodityCode = lclBookingPiece.getCommodityType().getCode();
            } else {
                commodityCode = lclBookingPiece.getCommNo();
                if (lclBookingPiece.isIsBarrel()) {
//                    index = 1;
                }
            }
            if (commodityCode == null) {
                commodityCode = lclBookingPiece.getCommNo();
            }
            boolean lbsKgsFound = false;
            if (lclBookingPiece.isIsBarrel()) {
                Object[] barellObj;
                if (pooorigin != null && !pooorigin.equals("")) {
                    barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                } else {
                    barellObj = lclratesdao.getBarrelRate(polorigin, destinationpod, commodityCode);
                }
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclBookingPiece.getBookedPieceCount().intValue();
                            }
                            if (lclBookingPiece.getActualPieceCount() != null && lclBookingPiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclBookingPiece.getActualPieceCount().intValue();
                            }
                        }
                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclBookingPiece.getBookedPieceCount().intValue();
                            }
                            if (lclBookingPiece.getActualPieceCount() != null && lclBookingPiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclBookingPiece.getActualPieceCount().intValue();
                            }
                        }
                    }
                }
            }//end of  isIsBarrel if condition
            //}
            //need to check here
            else {
                if (lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("")
                        && lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals("")) {
                    index++;
                    ofratefound = true;
                    lbsKgsFound = true;
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            weightDiv = new BigDecimal(100);
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weight = lclBookingPiece.getActualWeightImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weight = lclBookingPiece.getBookedWeightImperial().doubleValue();
                            }
                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                measure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                measure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            weightDiv = new BigDecimal(1000);
                            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weight = lclBookingPiece.getActualWeightMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weight = lclBookingPiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                measure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                measure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                            }
                        }
                    }
                    if (lclBookingPiece.getOfratemin() != null && !lclBookingPiece.getOfratemin().trim().equals("")) {
                        minchg = Double.parseDouble(lclBookingPiece.getOfratemin());
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
                    if (lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("")) {
                        rateWeight = Double.parseDouble(lclBookingPiece.getPerLbsKgs());
                    }
                    if (lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals("")) {
                        rateMeasure = Double.parseDouble(lclBookingPiece.getPerCftCbm());
                    }

                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            calculatedWeight = (weight / 100) * rateWeight;

                        } else if (engmet.equals("M")) {
                            calculatedWeight = (weight / 1000) * rateWeight;
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
                    cinfobean.setCommodityCode(lclBookingPiece.getCommodityType().getCode());
                    cinfobean.setChargeType(3);
                    cinfobean.setRate(d);
                    cinfobean.setMeasureRate(rateMeasure);
                    cinfobean.setWeightRate(rateWeight);
                    cinfobean.setMinCharge(new BigDecimal(minchg));
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setBookingPieceId(lclBookingPiece.getId());
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
                    cinfobeanForBarell.setCommodityCode(lclBookingPiece.getCommodityType().getCode());
                    cinfobeanForBarell.setRate(bigBarrelCharges);
                    cinfobeanForBarell.setRatePerUnitUom("FL");
                    cinfobeanForBarell.setRatePerWeightUnit(dblBareel);
                    cinfobeanForBarell.setBookingPieceId(lclBookingPiece.getId());
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
                    cinfobeanForBarellTTA.setCommodityCode(lclBookingPiece.getCommodityType().getCode());
                    cinfobeanForBarellTTA.setRatePerWeightUnit(dblTTA);
                    cinfobeanForBarellTTA.setRate(bigBarrelTTACharges);
                    cinfobeanForBarellTTA.setRatePerUnitUom("FL");
                    cinfobeanForBarellTTA.setBookingPieceId(lclBookingPiece.getId());
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
                cinfobean.setCommodityCode(lclBookingPiece.getCommodityType().getCode());
                cinfobean.setChargeType(3);
                cinfobean.setRate(d);
                cinfobean.setMeasureRate(rateMeasure);
                cinfobean.setWeightRate(rateWeight);
                cinfobean.setMinCharge(new BigDecimal(minchg));
                cinfobean.setPcb(this.getPcb());
                cinfobean.setBookingPieceId(lclBookingPiece.getId());
                quoteRateList.add(cinfobean);
            }
        }//end of for loop
    }//end of method

    public void calculateOfRateForCommodity(String origin, String destination,
            String pol, String pod, String rateType, Long fileNumberId,
            LclBookingPiece lclBookingPiece, User user, String billToParty) throws Exception {
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
            if (fileNumberId != null && fileNumberId > 0) {
                commodityCode = lclBookingPiece.getCommodityType().getCode();
            } else {
                commodityCode = lclBookingPiece.getCommNo();
            }
            lclUtils.calculateRatesForPiece(refterminal, ports, lclBookingPiece, lclratesdao, engmet, fileNumberId);
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
            if (lclBookingPiece.isIsBarrel()) {
                Object[] barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                if (barellObj == null) {
                    barellObj = lclratesdao.getBarrelRate(polorigin, destinationpod, commodityCode);
                }
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclBookingPiece.getBookedPieceCount().intValue());
                            }
                            if (lclBookingPiece.getActualPieceCount() != null && lclBookingPiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclBookingPiece.getActualPieceCount().intValue());
                            }
                        }
                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclBookingPiece.getBookedPieceCount().intValue());
                            }
                            if (lclBookingPiece.getActualPieceCount() != null && lclBookingPiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclBookingPiece.getActualPieceCount().intValue());
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
                        if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weight = lclBookingPiece.getActualWeightImperial().doubleValue();
                        } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weight = lclBookingPiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            measure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                        } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            measure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if (engmet.equals("M")) {
                        weightDiv = new BigDecimal(1000);
                        if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weight = lclBookingPiece.getActualWeightMetric().doubleValue();
                        } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weight = lclBookingPiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                        } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                if (lclBookingPiece.getOfratemin() != null && !lclBookingPiece.getOfratemin().trim().equals("")) {
                    minchg = Double.parseDouble(lclBookingPiece.getOfratemin());
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
                if (lclBookingPiece.getPerLbsKgs() != null && !lclBookingPiece.getPerLbsKgs().trim().equals("")) {
                    rateWeight = Double.parseDouble(lclBookingPiece.getPerLbsKgs());
                }
                if (lclBookingPiece.getPerCftCbm() != null && !lclBookingPiece.getPerCftCbm().trim().equals("")) {
                    rateMeasure = Double.parseDouble(lclBookingPiece.getPerCftCbm());
                }

                if (engmet != null) {
                    if (engmet.equals("E")) {
                        calculatedWeight = (weight / 100) * rateWeight;
                    } else if (engmet.equals("M")) {
                        calculatedWeight = (weight / 1000) * rateWeight;
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
                    cinfobean.setBookingPieceId(lclBookingPiece.getId());
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
                        cinfobeanForBarell.setBookingPieceId(lclBookingPiece.getId());
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
                        cinfobeanForBarellTTA.setBookingPieceId(lclBookingPiece.getId());
                        chargesInfoList.add(cinfobeanForBarellTTA);
                        quoteRateList.add(cinfobeanForBarellTTA);
                    }
                }
                saveCharges(fileNumberId, user, "C", billToParty, false);
            } else {
                if (ofratefound) {
                    lclBookingPiece.setOfrateamount(NumberUtils.convertToTwoDecimal(rateMeasure) + "/$"
                            + NumberUtils.convertToTwoDecimal(rateWeight) + "/$"
                            + NumberUtils.convertToTwoDecimal(minchg));
                } else {
                    if (totalBarrelCharges > 0.00 && dblBareel != null) {
                        lclBookingPiece.setOfrateamount(dblBareel.toString());
                    }
                    if (totalBarellTTA > 0.00 && dblTTA != null) {
                        lclBookingPiece.setOfrateamount(dblTTA.toString());
                    }
                }
            }

        }//end of refterminal if condition
    }//end of method

    public void saveCharges(Long fileNumberId, User user, String buttonValue, String billToParty, boolean includedestinationfee) throws Exception {
        log.info("save charges ");
        Map adjustmentMap = null;
        if (!buttonValue.equalsIgnoreCase("CH")) {
            if (fileNumberId != null && fileNumberId > 0) {
                List<LclBookingAc> lclBookingAcList = lclcostchargedao.getLclCostByFileNumberME(fileNumberId, false);
                LclRemarksDAO remarksDAO = new LclRemarksDAO();
                lclcostchargedao.deleteLclCostByFileNumber(fileNumberId, LclCommonConstant.LCL_EXPORT);
                adjustmentMap = new HashMap();
                for (LclBookingAc lclBookingAc : lclBookingAcList) {
                    if (lclBookingAc != null) {
                        String chargeCode = lclBookingAc.getArglMapping().getChargeCode();
                        BigDecimal amount = lclBookingAc.getArAmount();
                        String remarks = "DELETED -> Charge Code -> " + chargeCode + " Charge Amount -> " + amount;
                        remarksDAO.insertLclRemarks(fileNumberId, LclCommonConstant.REMARKS_DR_AUTO_NOTES, remarks, user.getUserId());
                        if (lclBookingAc.getAdjustmentAmount().doubleValue() != 0.00) {
                            adjustmentMap.put(lclBookingAc.getArglMapping().getChargeCode(), lclBookingAc);
                        }
                    }
                }
            }
        }
        log.info("deleted all charges ");
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
                    ChargesInfoBean cinfobean = quoteRateList.get(i);
                    LclBookingAc lclBookingAc = new LclBookingAc();
                    if (billingType.equalsIgnoreCase("P") || billingType.equalsIgnoreCase("B")) {
                        String billTo = CommonUtils.isNotEmpty(billToParty) ? billToParty : "F";
                        lclBookingAc.setArBillToParty(billTo);
                        lclBookingAc.setApBillToParty(billTo);
                    } else if (billingType.equalsIgnoreCase("C")) {
                        String billTo = CommonUtils.isNotEmpty(billToParty) ? billToParty : "A";
                        lclBookingAc.setApBillToParty(billTo);
                        lclBookingAc.setArBillToParty(billTo);
                    } else if (CommonUtils.isNotEmpty(billToParty)) {
                        lclBookingAc.setArBillToParty(billToParty);
                        lclBookingAc.setApBillToParty(billToParty);
                    }
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        lclBookingAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                    }
                    if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
                        if (cinfobean.getChargeCode().equalsIgnoreCase("OFBARR") || cinfobean.getChargeCode().equalsIgnoreCase("TTBARR")) {
                            glmapping = glmappingdao.getByProperty("chargeCode", cinfobean.getChargeCode());
                        } else {
                            glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
                        }
                        lclBookingAc.setArglMapping(glmapping);
                        lclBookingAc.setApglMapping(glmapping);
                    }
                    lclBookingAc.setTransDatetime(new Date());
                    if (cinfobean.getRatePerUnitUom() != null) {
                        if (cinfobean.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                            lclBookingAc.setRatePerUnit(cinfobean.getRatePerUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else {
                            lclBookingAc.setRatePerUnit(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    if (cinfobean.getRate() != null) {
                        lclBookingAc.setArAmount(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclBookingAc.setEnteredBy(user);
                    lclBookingAc.setModifiedBy(user);
                    lclBookingAc.setEnteredDatetime(new Date());
                    lclBookingAc.setModifiedDatetime(new Date());

                    lclBookingAc.setRateUom(engmet);
                    if (engmet != null && engmet.equalsIgnoreCase("E")) {
                        lclBookingAc.setRateUom("I");
                    }
                    if (cinfobean.getRatePerWeightUnit() != null) {
                        lclBookingAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerWeightUnitDiv() != null) {
                        lclBookingAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerVolumeUnit() != null) {
                        lclBookingAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerVolumeUnitDiv() != null) {
                        lclBookingAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    lclBookingAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
                    if (cinfobean.getMinCharge() != null) {
                        lclBookingAc.setRateFlatMinimum(cinfobean.getMinCharge().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getRatePerUnitDiv() != null) {
                        lclBookingAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (cinfobean.getBookingPieceId() != null && cinfobean.getBookingPieceId() > 0) {
                        lclBookingAc.setLclBookingPiece(lclbookingpiecedao.findById(cinfobean.getBookingPieceId()));
                    }
                    if (lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())
                            && lclBookingAc.getArglMapping().getChargeCode().substring(0, 2).equalsIgnoreCase("TT")) {//T&T charge has auto check
                        lclBookingAc.setBundleIntoOf(true);
                    } else {
                        lclBookingAc.setBundleIntoOf(false);
                    }
                    lclBookingAc.setPrintOnBl(true);
                    if (includedestinationfee && lclBookingAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode())
                           && (lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTBL")
                            || lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTWM"))) {
                        lclBookingAc.setManualEntry(true);
                    }
                    if (adjustmentMap != null && adjustmentMap.size() > 0 && adjustmentMap.containsKey(lclBookingAc.getArglMapping().getChargeCode())) {
                        LclBookingAc lclBookingAcFromMap = (LclBookingAc) adjustmentMap.get(lclBookingAc.getArglMapping().getChargeCode());
                        lclBookingAc.setAdjustmentAmount(lclBookingAcFromMap.getAdjustmentAmount());
                    } else {
                        lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                    }
                    if (fileNumberId != null && fileNumberId > 0) {
                        log.info("inserting charges ");
                        lclcostchargedao.save(lclBookingAc);
                    } else {
                        bookingAcList.add(lclBookingAc);
                    }
                }//end of for loop
            }
        }//end of if quoteratelist condition
    }

    public void savePickupCharge(Long fileNumberId, User user, List<Carrier> carrierList, List<Carrier> carrierCostList, LclBookingPad lclBookingPad) throws Exception {
        LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
        BigDecimal zeroBigDecimal = BigDecimal.ZERO;
        String inlandChargeCode = LoadLogisoftProperties.getProperty("InlandChargeCode");
        if (fileNumberId != null && fileNumberId > 0 && CommonUtils.isNotEmpty(inlandChargeCode)) {
            LclBookingAc lclBookingAc = lclcostchargedao.getLclBookingAcByChargeCode(fileNumberId, inlandChargeCode);
            if (lclBookingAc == null && lclBookingPad != null && lclBookingPad.getScac() != null && !lclBookingPad.getScac().trim().equalsIgnoreCase("")) {
                lclBookingAc = new LclBookingAc();
                if (lclFileNumber == null) {
                    lclFileNumber = new LclFileNumber(fileNumberId);
                }
                lclBookingAc.setLclFileNumber(lclFileNumber);
                for (int j = 0; j < carrierList.size(); j++) {
                    Carrier carrier = carrierList.get(j);
                    if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(lclBookingPad.getScac())
                            && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                        BigDecimal d = new BigDecimal(carrier.getFinalcharge());
                        lclBookingAc.setArAmount(d);
                    }
                }
                for (int j = 0; j < carrierCostList.size(); j++) {
                    Carrier carrierCost = carrierCostList.get(j);
                    if (carrierCost != null && carrierCost.getScac() != null && carrierCost.getScac().trim().equalsIgnoreCase(lclBookingPad.getScac())
                            && carrierCost.getFinalcharge() != null && !carrierCost.getFinalcharge().trim().equals("")) {
                        BigDecimal pickupCost = new BigDecimal(carrierCost.getFinalcharge());
                        lclBookingAc.setApAmount(pickupCost);
                    }
                }
                GlMapping gp = new GlMappingDAO().findByBlueScreenChargeCode("0012", "LCLE", "AR");
                lclBookingAc.setArglMapping(gp);
                lclBookingAc.setTransDatetime(new Date());
                lclBookingAc.setManualEntry(false);
                lclBookingAc.setRatePerUnitUom("FL");
                lclBookingAc.setRateUom("I");
                lclBookingAc.setAdjustmentAmount(zeroBigDecimal);
                lclBookingAc.setEnteredBy(user);
                lclBookingAc.setModifiedBy(user);
                lclBookingAc.setEnteredDatetime(new Date());
                lclBookingAc.setModifiedDatetime(new Date());
                if (lclBookingAc.getArAmount() == null) {
                    lclBookingAc.setArAmount(zeroBigDecimal);
                }
                lclBookingAc.setBundleIntoOf(false);
                lclBookingAc.setPrintOnBl(true);
                lclcostchargedao.saveOrUpdate(lclBookingAc);
                lclBookingPad.setLclBookingAc(lclBookingAc);
                lclBookingPadDAO.saveOrUpdate(lclBookingPad);
            }
        }
    }

    public String calculateInsuranceCharge(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBookingPiece> lclBookingPiecesList, Double valueOfGoods, User user, Long fileNumberId,
            LclBookingAc lclBookingAc, GlMapping glMapping, HttpServletRequest request, String billToParty, String buttonValue) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
            Object[] portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooorigin, destinationfd, "000000");
            //end of stdcharges null checking
            //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooorigin, destinationpod, "000000");
            }
            //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(polorigin, destinationfd, "000000");
            }
            //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.calculateInsuranceChargeCode(polorigin, destinationpod, "000000");
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
                    Double minchg = Double.parseDouble(portChargeArray[2].toString());
                    this.setInsMinChg(minchg);
                }  

            }
        }
        String cif = calculateInsurance(valueOfGoods, buttonValue, user, fileNumberId, lclBookingPiecesList, lclBookingAc, glMapping, request, billToParty);
        return cif;
    }

    public void calculateHazmatChargeForRadio(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBookingPiece> lclBookingPiecesList, LclBookingPiece lclBookingPiece, String engmet, User user, Long fileNumberId,
            LclBookingAc lclBookingAc, GlMapping glMapping, HttpServletRequest request, String billToParty) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = new String();
        Double weightDouble = 0.00;
        Double weightMeasure = 0.00;
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            if (lclBookingPiece.isHazmat()) {
                chgcod = chgcod + "119,";
                lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                if (engmet != null) {
                            if (engmet.equals("E")) {
                                if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                                }
                                if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                                }

                            } else if (engmet.equals("M")) {
                                if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                                }
                                if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                                } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                                }
                            }
                        }
                //calculate the Total Weight Of Commodities
                totalWeight = totalWeight + weightDouble;

                //calculate the Total Measure Of Commodities
                totalMeasure = totalMeasure + weightMeasure;
                Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, lclBookingPiece.getCommNo(), "0119");
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", "0119");
                }//end of stdcharges null checking
                //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, lclBookingPiece.getCommNo(), "0119");
                } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, lclBookingPiece.getCommNo(), "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", "0119");
                } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
                if (portChargeArray == null || portChargeArray.length == 0) {
                    portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, lclBookingPiece.getCommNo(), "0119");
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
                                lclBookingAc.setRateUom(engmet);
                            }
                            if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                lclBookingAc.setRateUom("I");
                            }
                            BigDecimal bd = new BigDecimal(flatrate);
                            if (lclBookingAc != null) {
                                lclBookingAc.setRatePerUnit(bd);
                                lclBookingAc.setArAmount(bd);
                                lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
                                lclBookingAc.setArglMapping(glMapping);
                                lclBookingAc.setRatePerUnitUom("FL");
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
                                    lclBookingAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                    lclBookingAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    weightDiv = new BigDecimal(1000);
                                    calculatedWeight = (totalWeight / 1000) * kgsrt;
                                    calculatedMeasure = totalMeasure * cbmrt;
                                    lclBookingAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                    lclBookingAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
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
                            lclBookingAc.setRatePerWeightUnitDiv(weightDiv);
                            lclBookingAc.setRatePerVolumeUnitDiv(measureDiv);
                            lclBookingAc.setRateUom(engmet);
                            if (engmet != null && engmet.equalsIgnoreCase("E")) {
                                lclBookingAc.setRateUom("I");
                            }
                            lclBookingAc.setRatePerUnit(bdf);
                            lclBookingAc.setArAmount(bdf);
                            lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
                            glMapping = glmappingdao.findByBlueScreenChargeCode("0119", "LCLE", "AR");
                            lclBookingAc.setArglMapping(glMapping);
                            lclBookingAc.setRatePerUnitUom(rateUOM);
                            lclBookingAc.setRateFlatMinimum(new BigDecimal(minchg));
                        }//end of charge type 3 if condition
                    }
                    lclBookingAc.setArBillToParty(billToParty);
                    lclBookingAc.setBundleIntoOf(false);
                    lclBookingAc.setPrintOnBl(true);
                    lclcostchargedao.saveOrUpdate(lclBookingAc);
                }
            }
        }
    }

    public void calculateChargeForDeliveyMetro(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBookingPiece> lclBookingPiecesList, User user, Long fileNumberId, String engmet, String oldBlueScreenChargeCode, String billToParty) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(oldBlueScreenChargeCode, "LCLE", "AR");
        String blueScreenChargeCode = new String();
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
            LclBookingAc lclBookingAc = lclcostchargedao.getLclCostByChargeCodeBookingPiece(fileNumberId, oldBlueScreenChargeCode, false, lclBookingPiece.getId());
            Date d = new Date();
            if (lclBookingAc == null) {
                lclBookingAc = new LclBookingAc();
                lclBookingAc.setApBillToParty(billToParty);
                lclBookingAc.setArBillToParty(billToParty);
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclBookingAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclBookingAc.setEnteredBy(user);
                lclBookingAc.setEnteredDatetime(d);
                lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclBookingAc.setLclBookingPiece(lclBookingPiece);
            }
            lclBookingAc.setTransDatetime(d);
            lclBookingAc.setModifiedBy(user);
            lclBookingAc.setModifiedDatetime(d);
            Double weightDouble = 0.00;
            Double weightMeasure = 0.00;
            if (engmet != null) {
                if (engmet.equals("E")) {
                    if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                    } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                    }
                    if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                    } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                    }

                } else if (engmet.equals("M")) {
                    if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                    } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                    }
                    if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                    } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
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
            Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, lclBookingPiece.getCommodityType().getCode(), blueScreenChargeCode);
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", blueScreenChargeCode);
            }//end of stdcharges null checking
            //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, lclBookingPiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, lclBookingPiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, lclBookingPiece.getCommodityType().getCode(), blueScreenChargeCode);
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
                        lclBookingAc.setRateUom(engmet);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclBookingAc.setRateUom("I");
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
                        lclBookingAc.setRatePerUnit(bd);
                        lclBookingAc.setArAmount(bd);
                        lclBookingAc.setArglMapping(glMapping);
                        lclBookingAc.setRatePerUnitUom("FL");
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
                                lclBookingAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                lclBookingAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                            } else if (engmet.equalsIgnoreCase("M")) {
                                weightDiv = new BigDecimal(1000);
                                calculatedWeight = (totalWeight / 1000) * kgsrt;
                                calculatedMeasure = totalMeasure * cbmrt;
                                lclBookingAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                lclBookingAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
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
                        lclBookingAc.setRatePerWeightUnitDiv(weightDiv);
                        lclBookingAc.setRatePerVolumeUnitDiv(measureDiv);
                        lclBookingAc.setRateUom(engmet);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclBookingAc.setRateUom("I");
                        }
                        lclBookingAc.setRatePerUnit(bdf);
                        lclBookingAc.setArAmount(bdf);
                        glMapping = glmappingdao.findByBlueScreenChargeCode(blueScreenChargeCode, "LCLE", "AR");
                        lclBookingAc.setArglMapping(glMapping);
                        lclBookingAc.setRatePerUnitUom(rateUOM);
                        lclBookingAc.setRateFlatMinimum(new BigDecimal(minchg));
                    }//end of charge type 3 if condition
                }
                lclBookingAc.setArBillToParty(billToParty);
                lclBookingAc.setApBillToParty(billToParty);
                lclBookingAc.setBundleIntoOf(false);
                lclBookingAc.setPrintOnBl(true);
                lclcostchargedao.saveOrUpdate(lclBookingAc);
            }

        }
    }

    public void calculateChargeForCalcHeavy(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            Boolean calcHeavy, List lclBookingPiecesList, Long fileNumberId,
            User user, RefTerminal refterminal, Ports ports, String billToParty) throws Exception {

        if (calcHeavy != null && calcHeavy.equals(true) && CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            lclratesdao = new LCLRatesDAO(databaseSchema);
            String chgcod = new String();
            String comnum = new String();
            String overDimChgCdeForWeight = new String();
            String overDimChgCdeForMeasure = new String();
            boolean densecargofound = false;
            engmet = ports.getEngmet();
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
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                if (fileNumberId != null && fileNumberId > 0) {
                    comnum = lclBookingPiece.getCommodityType().getCode();
                } else {
                    comnum = lclBookingPiece.getCommNo();
                }
                Double weightDouble = 0.00;
                Double weightMeasure = 0.00;
                if (engmet != null) {
                    if (engmet.equals("E")) {
                        if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclBookingPiece.getActualWeightImperial().doubleValue();
                        } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weightDouble = lclBookingPiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclBookingPiece.getActualVolumeImperial().doubleValue();
                        } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            weightMeasure = lclBookingPiece.getBookedVolumeImperial().doubleValue();
                        }

                    } else if (engmet.equals("M")) {
                        if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclBookingPiece.getActualWeightMetric().doubleValue();
                        } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weightDouble = lclBookingPiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclBookingPiece.getActualVolumeMetric().doubleValue();
                        } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            weightMeasure = lclBookingPiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                //calculate the Total Weight Of Commodities
                totalWeight = totalWeight + weightDouble;
                //calculate the Total Measure Of Commodities
                totalMeasure = totalMeasure + weightMeasure;
                overDimChgCdeForWeight = getChargeCodeForHeavyLiftCharge(lclBookingPiecesList);
                overDimChgCdeForMeasure = getchgCdeForExtraLengthCharge(lclBookingPiece.getLclBookingPieceDetailList());
                densecargofound = getchgCdeForDenseCargoFee(lclBookingPiecesList);
                if (densecargofound) {
                    chgcod = chgcod + "251,";
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
                        List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
                        //Check portChargesList null
                        if (portChargesList != null && portChargesList.size() > 0) {
                            //Loop through portChargesList
                            for (int k = 0; k < portChargesList.size(); k++) {
                                Object[] portcharges = (Object[]) portChargesList.get(k);
                                //Calculate Rates for all portcharges
                                setRates(portcharges, lclBookingPiece, comnum, k, lclBookingPiecesList.size(), fileNumberId, null, "");
                            }//end of portcharges for loop
                        }
                    }
                }
            }
            saveCharges(fileNumberId, user, "CH", billToParty, includedestinationfee);
        }
    }

    public void modifyOfrate(Long fileNumberId) throws Exception {
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        Object[] spotRateList = lclBookingDAO.getAllSpotRateValues(fileNumberId);
        BigDecimal calculatedWeight = BigDecimal.ZERO;
        BigDecimal calculatedMeasure = BigDecimal.ZERO;
        Double wmRate = 0.00;
        if (spotRateList != null && spotRateList.length > 0 && null != spotRateList[1] && null != spotRateList[2]) {
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

                    ofratecinfobean.setRatePerVolumeUnit(new BigDecimal(Double.parseDouble(spotRateList[2].toString()) - totalSpotMeasure));
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
                }
                if (null != spotRateList[1] && null != spotRateList[2] && (!spotRateList[1].toString().trim().equals("") && Double.parseDouble(spotRateList[1].toString()) > 0.00)
                        && (!spotRateList[2].toString().trim().equals("") && Double.parseDouble(spotRateList[2].toString()) > 0.00)
                        && null != ofratecinfobean) {
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

    public void calculateCAFCharge(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBookingPiece> lclBookingPiecesList, String pcBoth, User user, Long fileNumberId,
            HttpServletRequest request, Ports ports, String billToParty) throws Exception {
        LclBookingAc lclBookingAc = lclcostchargedao.findByChargeCode(fileNumberId, false, "LCLE", "CAF");
        double collect_amount = lclcostchargedao.getBookingTotalCollectChages(fileNumberId);
        if (collect_amount > 0) {
            billToParty = "A"; // the caf charge will be agent only. 
            databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            lclratesdao = new LCLRatesDAO(databaseSchema);
            String chgcod = null;

            LclBookingExport lclBookingExport = null;
            LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();

            LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
            Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(ports.getUnLocationCode());
            if(ncl[2] != null){
            if (ncl[2].toString().equalsIgnoreCase("y")) {
                for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
                    chgcod = "0005";
                    boolean includedestinationfee = false;
                    if (fileNumberId != null && fileNumberId > 0) {
                        lclBookingExport = lclBookingExportDAO.getLclBookingExportByFileNumber(fileNumberId);
                    }
                    if (lclBookingExport.isIncludeDestfees()) {
                        includedestinationfee = lclBookingExport.isIncludeDestfees();
                    }
                    List prtChgsCommodityList = lclratesdao.findByChdcod(pooorigin, destinationfd, lclBookingPiece.getCommodityType().getCode(), chgcod);
                    List prtChgsZeroCommodityList = lclratesdao.findByChdcod(pooorigin, destinationfd, "000000", chgcod);
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(pooorigin, destinationpod, lclBookingPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(pooorigin, destinationpod, "000000", chgcod);
                    }
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(polorigin, destinationfd, lclBookingPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(polorigin, destinationfd, "000000", chgcod);
                    }
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao.findByChdcod(polorigin, destinationpod, lclBookingPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao.findByChdcod(polorigin, destinationpod, "000000", chgcod);
                    }

                    List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);

                    if (!portChargesList.isEmpty()) {
                        Double finalValue = 0.0;
                        Double minchg = 0.0;
                        Object[] portcharges = (Object[]) portChargesList.get(0);
                        if (portcharges[3] != null && !portcharges[3].toString().trim().equals("")) {
                            minchg = Double.parseDouble(portcharges[3].toString());
                        }
                        if (portcharges[3] != null && !portcharges[3].toString().trim().equals("")) {
                            finalValue = collect_amount * minchg;
                        }
                        if (portcharges[6] != null && !portcharges[6].toString().trim().equals("")
                                && Double.parseDouble(portcharges[6].toString()) > finalValue) {
                            finalValue = Double.parseDouble(portcharges[6].toString());
                        }
                        if (lclBookingAc == null) {
                            lclBookingAc = new LclBookingAc();
                            lclBookingAc.setEnteredBy(user);
                            lclBookingAc.setEnteredDatetime(new Date());
                            lclBookingAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                            lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(0.00));
                            lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(0.00));
                            lclBookingAc.setAdjustmentAmount(new BigDecimal(0.00));
                            lclBookingAc.setManualEntry(false);
                        }
                        BigDecimal bdf = new BigDecimal(finalValue);
                        BigDecimal min = new BigDecimal(minchg);
                        lclBookingAc.setRateUom(ports.getEngmet());
                        if (ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("E")) {
                            lclBookingAc.setRateUom("I");
                        }
                        lclBookingAc.setRatePerUnit(min.setScale(2, BigDecimal.ROUND_HALF_UP));
                        lclBookingAc.setArAmount(bdf.setScale(2, BigDecimal.ROUND_HALF_UP));
                        GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(chgcod, "LCLE", "AR");
                        lclBookingAc.setArglMapping(glMapping);
                        lclBookingAc.setArBillToParty(billToParty);
                        lclBookingAc.setRatePerUnitUom("PCT");
                        lclBookingAc.setRateFlatMinimum(new BigDecimal(0.00));
                        lclBookingAc.setBundleIntoOf(false);
                        lclBookingAc.setPrintOnBl(true);
                        lclBookingAc.setModifiedBy(user);
                        lclBookingAc.setModifiedDatetime(new Date());
                        lclBookingAc.setTransDatetime(new Date());
                        lclcostchargedao.saveOrUpdate(lclBookingAc);
                    }
                }
            }
        }
        } else {
            if (lclBookingAc != null) {
                String desc = "DELETED -> Charge Code -> " + lclBookingAc.getArglMapping().getChargeCode()
                        + " Charge Amount -> " + lclBookingAc.getArAmount();
                lclcostchargedao.delete(lclBookingAc);
                new LclRemarksDAO().insertLclRemarks(fileNumberId, "DR-AutoNotes", desc, user.getUserId());
            }
        }
    }

    public void saveCAFCharge(ChargesInfoBean cinfobean, Long fileNumberId, User user, String billToParty) throws Exception {
        LclBookingAc lclBookingAc = new LclBookingAc();
        if (CommonUtils.isEmpty(billToParty)) {
            billToParty = "A";
        }
        lclBookingAc.setApBillToParty(billToParty);
        lclBookingAc.setArBillToParty(billToParty);
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            if (lclFileNumber == null) {
                lclFileNumber = new LclFileNumber(fileNumberId);
            }
            lclBookingAc.setLclFileNumber(lclFileNumber);
        }
        if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
            GlMapping glmapping = null;
            if (cinfobean.getChargeCode().equalsIgnoreCase("OFBARR") || cinfobean.getChargeCode().equalsIgnoreCase("TTBARR")) {
                glmapping = glmappingdao.getByProperty("chargeCode", cinfobean.getChargeCode());
            } else {
                glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
            }
            lclBookingAc.setArglMapping(glmapping);
            lclBookingAc.setApglMapping(glmapping);
        }
        lclBookingAc.setTransDatetime(new Date());
        if (cinfobean.getRatePerUnitUom() != null) {
            if (cinfobean.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
                lclBookingAc.setRatePerUnit(cinfobean.getRatePerUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                lclBookingAc.setRatePerUnit(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        if (cinfobean.getRate() != null) {
            lclBookingAc.setArAmount(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclBookingAc.setEnteredBy(user);
        lclBookingAc.setModifiedBy(user);
        lclBookingAc.setEnteredDatetime(new Date());
        lclBookingAc.setModifiedDatetime(new Date());
        lclBookingAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclBookingAc.setRateUom(engmet);
        if (engmet != null && engmet.equalsIgnoreCase("E")) {
            lclBookingAc.setRateUom("I");
        }
        if (cinfobean.getRatePerWeightUnit() != null) {
            lclBookingAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerWeightUnitDiv() != null) {
            lclBookingAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnit() != null) {
            lclBookingAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnitDiv() != null) {
            lclBookingAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclBookingAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
        if (cinfobean.getMinCharge() != null) {
            lclBookingAc.setRateFlatMinimum(cinfobean.getMinCharge().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerUnitDiv() != null) {
            lclBookingAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getBookingPieceId() != null && cinfobean.getBookingPieceId() > 0) {
            lclBookingAc.setLclBookingPiece(lclbookingpiecedao.findById(cinfobean.getBookingPieceId()));
        }
        if (CommonUtils.isNotEmpty(lclBookingAc.getArglMapping().getChargeCode()) && lclBookingAc.getArglMapping().getChargeCode().substring(0, 2).equalsIgnoreCase("TT")) {//T&T charge has auto check
            lclBookingAc.setBundleIntoOf(true);
        } else {
            lclBookingAc.setBundleIntoOf(false);
        }
        lclBookingAc.setPrintOnBl(true);
        if (fileNumberId != null && fileNumberId > 0) {
            lclcostchargedao.save(lclBookingAc);
        } else {
            bookingAcList.add(lclBookingAc);
        }
    }//end of for loop

    public void reCalculateManualCharges(Long fileNumberId, User user) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        Double calculatedWeight = 0.00;
        Double calculatedMeasure = 0.00;
        //String shipmentType = "", pooTrmnum = "", polTrmnum = "", podEciPortCode = "", fdEciPortCode = "", fdEngmet = "";
        List<LclBookingAc> lclBookingAcList = lclCostChargeDAO.getByFileNumberME(fileNumberId, true);
        List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);

        if (CommonUtils.isNotEmpty(lclBookingAcList)) {
            for (LclBookingAc lclBookingAc : lclBookingAcList) {

                lclBookingAc.setRatePerWeightUnit(null != lclBookingAc.getRatePerWeightUnit()
                        ? new BigDecimal(lclBookingAc.getRatePerWeightUnit().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setRatePerVolumeUnit(null != lclBookingAc.getRatePerVolumeUnit()
                        ? new BigDecimal(lclBookingAc.getRatePerVolumeUnit().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setRateFlatMinimum(null != lclBookingAc.getRateFlatMinimum()
                        ? new BigDecimal(lclBookingAc.getRateFlatMinimum().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setArAmount(null != lclBookingAc.getRatePerUnit()
                        ? new BigDecimal(lclBookingAc.getRatePerUnit().doubleValue()) : BigDecimal.ZERO);

                double calculateWeiMea = 0.00;
                if (lclBookingAc.getRatePerWeightUnit().doubleValue() > 0.00 || lclBookingAc.getRatePerVolumeUnit().doubleValue() > 0.00) {
                    if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
                        totalMeasure = 0.00;
                        totalWeight = 0.00;
                        for (LclBookingPiece piece : lclBookingPiecesList) {
                            Double weightDouble = 0.00;
                            Double weightMeasure = 0.00;
                            if (null != lclBookingAc.getRateUom()) {
                                if (("I").equalsIgnoreCase(lclBookingAc.getRateUom())) {
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
                                } else if (("M").equalsIgnoreCase(lclBookingAc.getRateUom())) {
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
                    if (engmet != null && !"".equalsIgnoreCase(engmet)) {
                        if (engmet.equals("E")) {
                            if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                                calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 100) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                                calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                            }
                            lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                        } else if (engmet.equals("M")) {
                            if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                                calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getRatePerWeightUnit().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getRatePerVolumeUnit().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 1000) * lclBookingAc.getRatePerWeightUnit().doubleValue();
                                calculatedMeasure = totalMeasure * lclBookingAc.getRatePerVolumeUnit().doubleValue();
                            }
                            lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                        }
                    }//end of else if engmet
                    lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerWeightUnitDiv());
                    lclBookingAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                    if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                        calculateWeiMea = calculatedWeight;
                        lclBookingAc.setRatePerUnitUom("W");
                        lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                    } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getRateFlatMinimum().doubleValue()) {
                        calculateWeiMea = calculatedMeasure;
                        lclBookingAc.setRatePerUnitUom("V");
                        lclBookingAc.setRatePerUnitDiv(lclBookingAc.getRatePerVolumeUnitDiv());
                    } else {
                        calculateWeiMea = lclBookingAc.getRateFlatMinimum().doubleValue();
                        lclBookingAc.setRatePerUnitUom("M");
                    }
                    if (lclBookingAc.getArglMapping().getChargeCode().equals("DESTWM")) {
                        lclBookingAc.setArAmount(new BigDecimal(lclBookingAc.getArAmount().doubleValue()));
                    } else {
                        lclBookingAc.setArAmount(new BigDecimal(calculateWeiMea + lclBookingAc.getArAmount().doubleValue()));
                    }
                }

                lclBookingAc.setCostWeight(null != lclBookingAc.getCostWeight()
                        ? new BigDecimal(lclBookingAc.getCostWeight().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setCostMeasure(null != lclBookingAc.getCostMeasure()
                        ? new BigDecimal(lclBookingAc.getCostMeasure().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setCostMinimum(null != lclBookingAc.getCostMinimum()
                        ? new BigDecimal(lclBookingAc.getCostMinimum().doubleValue()) : BigDecimal.ZERO);
                lclBookingAc.setApAmount(null != lclBookingAc.getCostFlatrateAmount()
                        ? new BigDecimal(lclBookingAc.getCostFlatrateAmount().doubleValue()) : BigDecimal.ZERO);

                if (lclBookingAc.getCostWeight().doubleValue() > 0.00 || lclBookingAc.getCostMeasure().doubleValue() > 0.00) {
                    if (engmet != null && !"".equalsIgnoreCase(engmet)) {
                        if (engmet.equals("E")) {
                            if (lclBookingAc.getRateUom().equalsIgnoreCase("M")) {
                                calculatedWeight = (totalWeight / 100) * lclUtils.convertToLbs(lclBookingAc.getCostWeight().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCft(lclBookingAc.getCostMeasure().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 100) * lclBookingAc.getCostWeight().doubleValue();
                                calculatedMeasure = totalMeasure * lclBookingAc.getCostMeasure().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            if (lclBookingAc.getRateUom().equalsIgnoreCase("I")) {
                                calculatedWeight = (totalWeight / 1000) * lclUtils.convertToKgs(lclBookingAc.getCostWeight().doubleValue()).doubleValue();
                                calculatedMeasure = totalMeasure * lclUtils.convertToCbm(lclBookingAc.getCostMeasure().doubleValue()).doubleValue();
                            } else {
                                calculatedWeight = (totalWeight / 1000) * lclBookingAc.getCostWeight().doubleValue();
                                calculatedMeasure = totalMeasure * lclBookingAc.getCostMeasure().doubleValue();
                            }
                        }
                    }//end of else if engmet
                    double calculateCostWeiMei = 0.00;
                    if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBookingAc.getCostMinimum().doubleValue()) {
                        calculateCostWeiMei = calculatedWeight;
                    } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBookingAc.getCostMinimum().doubleValue()) {
                        calculateCostWeiMei = calculatedMeasure;
                    } else {
                        calculateCostWeiMei = lclBookingAc.getCostMinimum().doubleValue();
                    }
                    lclBookingAc.setApAmount(new BigDecimal(calculateCostWeiMei + lclBookingAc.getApAmount().doubleValue()));
                }
                if (lclBookingAc.getArglMapping().isDestinationServices()
                        && !lclBookingAc.getArglMapping().getChargeCode().equalsIgnoreCase("DTHC PREPAID")) {
                    String chargeCode = lclBookingAc.getArglMapping().getChargeCode();
                    BigDecimal costAmount = lclBookingAc.getApAmount();
                    BigDecimal destinationDiff = lclBookingAc.getArAmount().subtract(lclBookingAc.getApAmount());
                    String min = new PropertyDAO().getProperty(chargeCode.equalsIgnoreCase("ONCARR")
                            ? "Destination Services O/C Min Profit" : "Destination Services DAP/DDP/Delivery min profit");
                    String max = new PropertyDAO().getProperty(chargeCode.equalsIgnoreCase("ONCARR")
                            ? "Destination Services O/C Max Profit" : "Destination Services DAP/DDP/Delivery max profit");
                    int profitMin = !"".equalsIgnoreCase(min) ? Integer.parseInt(min) : 0;
                    int profitMax = !"".equalsIgnoreCase(min) ? Integer.parseInt(max) : 0;
                    if (destinationDiff.intValue() <= profitMin) {
                        lclBookingAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMin));
                    } else if (destinationDiff.intValue() >= profitMax) {
                        lclBookingAc.setArAmount(new BigDecimal(costAmount.doubleValue() + profitMax));
                    } else {
                        lclBookingAc.setArAmount(destinationDiff);
                    }
                }
                lclBookingAc.setTransDatetime(new Date());
                lclBookingAc.setModifiedBy(user);
                lclBookingAc.setModifiedDatetime(new Date());
                lclCostChargeDAO.saveOrUpdate(lclBookingAc);
            }
        }
    }

    public void calculaterelayTTCharge(String pooorigin, String polorigin, List<LclBookingPiece> lclBookingPiecesList,
            String engmet, User user, Long fileNumberId, LclBookingAc lclBookingAc, GlMapping glMapping,
            String BlueScreenTTRevChgCode, HttpServletRequest request) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        if (CommonUtils.isNotEmpty(lclBookingPiecesList)) {
            totalMeasure = 0.00;
            totalWeight = 0.00;
            for (LclBookingPiece piece : lclBookingPiecesList) {
                Double weightDouble = 0.00;
                Double weightMeasure = 0.00;
                if (null != lclBookingAc.getRateUom()) {
                    if (("I").equalsIgnoreCase(lclBookingAc.getRateUom())) {
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
                    } else if (("M").equalsIgnoreCase(lclBookingAc.getRateUom())) {
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
        for (int j = 0; j < lclBookingPiecesList.size(); j++) {
            LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
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
                            lclBookingAc.setRateUom(engmet);
                        }
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclBookingAc.setRateUom("I");
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
                        if (lclBookingAc != null) {
                            lclBookingAc.setRatePerUnit(bd);
                            lclBookingAc.setArAmount(bd);

                            lclBookingAc.setRatePerUnitUom("FL");
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
                        Double flatrate = 0.0;
                        if (portChargeArray[1] != null && !portChargeArray[1].toString().trim().equals("")) {
                            flatrate = Double.parseDouble(portChargeArray[1].toString());
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
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
                                lclBookingAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                lclBookingAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                                lclBookingAc.setRateUom("I");
                                lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                                lclBookingAc.setRatePerUnitDiv(new BigDecimal(100));

                            } else if (engmet.equalsIgnoreCase("M")) {
                                weightDiv = new BigDecimal(1000);
                                calculatedWeight = (totalWeight / 1000) * kgsrt;
                                calculatedMeasure = totalMeasure * cbmrt;

                                lclBookingAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                lclBookingAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
                                lclBookingAc.setRateUom(engmet);
                                lclBookingAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                                lclBookingAc.setRatePerUnitDiv(new BigDecimal(1000));
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
                        lclBookingAc.setRatePerUnit(d);
                        lclBookingAc.setRatePerUnitUom(rateUOM);
                        lclBookingAc.setRateFlatMinimum(new BigDecimal(minchg));
                        Date dat = new Date();
                        lclBookingAc.setArAmount(d);
                        lclBookingAc.setBundleIntoOf(false);
                        lclBookingAc.setModifiedBy(user);
                        lclBookingAc.setModifiedDatetime(dat);
                        lclBookingAc.setPrintOnBl(true);
                        lclBookingAc.setTransDatetime(dat);
                        lclcostchargedao.saveOrUpdate(lclBookingAc);
                    }
                }
            } else if (CommonUtils.isNotEmpty(lclBookingAc.getId())) {
                lclBookingAc.setRatePerWeightUnit(new BigDecimal(0.00));
                lclBookingAc.setRatePerVolumeUnit(new BigDecimal(0.00));
                lclBookingAc.setRateUom("I");
                lclBookingAc.setRatePerUnit(new BigDecimal(0.000));
                Date dat = new Date();
                BigDecimal d = new BigDecimal(0.00);
                lclBookingAc.setArAmount(d);
                lclBookingAc.setBundleIntoOf(false);
                lclBookingAc.setModifiedBy(user);
                lclBookingAc.setModifiedDatetime(dat);
                lclBookingAc.setPrintOnBl(true);
                lclBookingAc.setTransDatetime(dat);
                lclcostchargedao.saveOrUpdate(lclBookingAc);
            }
        }
    }

    public String[] getCAFCalculationContent(LclBooking lclBooking) throws Exception {
        String result[] = new String[4];
        PortsDAO portsDAO = new PortsDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        String rateType = "R".equalsIgnoreCase(lclBooking.getRateType()) ? "Y" : lclBooking.getRateType();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfOrigin().getUnLocationCode(), rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            result[0] = refterminal.getTrmnum();
        }
        RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBooking.getPortOfLoading().getUnLocationCode(), rateType);
        if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
            result[1] = refterminalpol.getTrmnum();
        }
        Ports portspod = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
        if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
            result[2] = portspod.getEciportcode();
        }
        Ports ports = portsDAO.getByProperty("unLocationCode", lclBooking.getFinalDestination().getUnLocationCode());
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            result[3] = ports.getEciportcode();
        }
        return result;
    }

    public String calculateInsuranceChargeForBl(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBlPiece> lclBlPiecesList, Double valueOfGoods, User user, Long fileNumberId,
            LclBlAc lclBlAc, GlMapping glMapping, HttpServletRequest request, String billToParty, String buttonValue) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        Object[] portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooorigin, destinationfd, "000000");
        //end of stdcharges null checking
        //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
        if (portChargeArray == null || portChargeArray.length == 0) {
            portChargeArray = lclratesdao.calculateInsuranceChargeCode(pooorigin, destinationpod, "000000");
        }
        //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
        if (portChargeArray == null || portChargeArray.length == 0) {
            portChargeArray = lclratesdao.calculateInsuranceChargeCode(polorigin, destinationfd, "000000");
        }
        //If the object is null Getting the stdcharges using polorigin and destinationpod and 0 commodity
        if (portChargeArray == null || portChargeArray.length == 0) {
            portChargeArray = lclratesdao.calculateInsuranceChargeCode(polorigin, destinationpod, "000000");
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
        String cif = calculateInsurance(valueOfGoods, buttonValue, user, fileNumberId, lclBlPiecesList, lclBlAc, glMapping, request, billToParty);
        return cif;
    }

    public String calculateInsurance(Double A, String buttonValue, User user,
            Long fileNumberId, List<LclBlPiece> lclCommodityList, LclBlAc lclBlAc,
            GlMapping glMapping, HttpServletRequest request, String billToParty) throws Exception {
        Double CIFValue = 0.0;
        Double B = 0.00;
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            if (lclCommodityList.size() == 1) {
                B = lclcostchargedao.getTotalLclBlCostWithoutParticularCharge(fileNumberId, glMapping.getId());
            } else {
                List<LclBlAc> chargeList = new LclBlAcDAO().getLclCostByFileNumberAsc(fileNumberId);
                List<LclBlAc> rolledUpChargesList = new BlUtils().getRolledUpChargesForBl(lclCommodityList, chargeList, engmet, null, true);
                B = new BlUtils().calculateTotalWithoutInsuranceByBlAcList(rolledUpChargesList);

            }
        }
        if (B == 0.00) {
            B = calculateTotal();
        }
        Double C = 0.0;
        Double D = 0.0;
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
        new LCLBlDAO().updateCIF(fileNumberId, cif);
        if (insamt != null) {
            insurance = (CIFValue / 100) * insurt;
        }
        if(insurance<=insMinChg){
            insurance=insMinChg;
        }
        if (buttonValue.equalsIgnoreCase("I")) {
            Date d = new Date();
            if (lclBlAc == null) {
                lclBlAc = new LclBlAc();
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclBlAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclBlAc.setArglMapping(glMapping);
                lclBlAc.setEnteredBy(user);
                lclBlAc.setEnteredDatetime(d);
                lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclBlAc.setRatePerUnitUom("FL");
            }
            lclBlAc.setArBillToParty(billToParty);
            BigDecimal insuranceBigDecimal = new BigDecimal(insurance);
            lclBlAc.setTransDatetime(d);
            lclBlAc.setArAmount(insuranceBigDecimal);
            lclBlAc.setModifiedBy(user);
            lclBlAc.setModifiedDatetime(d);
            lclBlAc.setRatePerWeightUnit(new BigDecimal(insurt));
            lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(insamt));
            lclBlAc.setBundleIntoOf(false);
            lclBlAc.setPrintOnBl(true);
            lclcostchargedao.saveOrUpdate(lclBlAc);
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

    public void calculateCAFChargeForBl(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBlPiece> lclBlPiecesList, String pcBoth, User user, Long fileNumberId,
            HttpServletRequest request, Ports ports, String billToParty) throws Exception {
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        String chgcod = null;
        LclBookingExport lclBookingExport = null;
        LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
        for (int j = 0; j < lclBlPiecesList.size(); j++) {
            boolean includedestinationfee = false;
            if (fileNumberId != null && fileNumberId > 0) {
                lclBookingExport = lclBookingExportDAO.getLclBookingExportByFileNumber(fileNumberId);
            }
            if (lclBookingExport.isIncludeDestfees()) {
                includedestinationfee = lclBookingExport.isIncludeDestfees();
            }
            LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
            LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
            Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(ports.getUnLocationCode());
            if (ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")) {
                if (pcBoth != null && pcBoth.equalsIgnoreCase("C")) {
                    String destid = ports.getEciportcode();
                    if (destid != null) {
                        if (destid.equals("050") || destid.equals("060")) {
                            chgcod = "0095";
                        } else {
                            chgcod = "0005";
                        }
                    }
                }
            }
            List prtChgsCommodityList = lclratesdao.findByChdcod(
                    pooorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), chgcod);
            List prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                    pooorigin, destinationfd, "000000", chgcod);
            if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        pooorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        pooorigin, destinationpod, "000000", chgcod);
            }
            if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        polorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        polorigin, destinationfd, "000000", chgcod);
            }
            if ((prtChgsCommodityList == null || prtChgsCommodityList.size() == 0)
                    && (prtChgsZeroCommodityList == null || prtChgsZeroCommodityList.size() == 0)) {
                prtChgsCommodityList = lclratesdao.findByChdcod(
                        polorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), chgcod);
                prtChgsZeroCommodityList = lclratesdao.findByChdcod(
                        polorigin, destinationpod, "000000", chgcod);
            }
            List portChargesList = getPortChargesList(prtChgsCommodityList, prtChgsZeroCommodityList);
            if (portChargesList != null && portChargesList.size() > 0) {
                Double total = 0.0;
                if (lclBlPiecesList.size() == 1) {
                    total = lclcostchargedao.getBlTotalWithoutParticularChargeCode(fileNumberId, "CAF");
                } else {
                    List<LclBlAc> chargeList = new LclBlAcDAO().getLclCostByFileNumberAsc(fileNumberId);
                    List<LclBlAc> rolledUpChargesList = new BlUtils().getRolledUpChargesForBl(lclBlPiecesList, chargeList, engmet, null, true);
                    total = Double.parseDouble(new BlUtils().calculateTotalByBlAcList(rolledUpChargesList));
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
                LclBlAc lclBlAc = lclcostchargedao.manaualBlChargeValidate(fileNumberId, "CAF", false);
                if (lclBlAc == null) {
                    lclBlAc = new LclBlAc();
                    lclBlAc.setEnteredBy(user);
                    lclBlAc.setEnteredDatetime(new Date());
                    lclBlAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                    lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(0.00));
                    lclBlAc.setRatePerVolumeUnitDiv(new BigDecimal(0.00));
                    lclBlAc.setAdjustmentAmount(new BigDecimal(0.00));
                    lclBlAc.setManualEntry(false);
                }
                BigDecimal bdf = new BigDecimal(finalValue);
                BigDecimal min = new BigDecimal(minchg);
                lclBlAc.setRateUom(ports.getEngmet());
                if (ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("E")) {
                    lclBlAc.setRateUom("I");
                }
                lclBlAc.setRatePerUnit(min.setScale(2, BigDecimal.ROUND_HALF_UP));
                lclBlAc.setArAmount(bdf.setScale(2, BigDecimal.ROUND_HALF_UP));
                GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(chgcod, "LCLE", "AR");
                lclBlAc.setArglMapping(glMapping);
                lclBlAc.setArBillToParty(billToParty);
                lclBlAc.setRatePerUnitUom("PCT");
                lclBlAc.setRateFlatMinimum(new BigDecimal(0.00));
                lclBlAc.setBundleIntoOf(false);
                lclBlAc.setPrintOnBl(true);
                lclBlAc.setModifiedBy(user);
                lclBlAc.setModifiedDatetime(new Date());
                lclBlAc.setTransDatetime(new Date());
                lclcostchargedao.saveOrUpdate(lclBlAc);
            }
        }
    }

    public String getChgCode(Object[] classchargecode) {
        String chgcod = new String();
        if (classchargecode[0] != null && !classchargecode[0].toString().trim().equals("") && Integer.parseInt(classchargecode[0].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[0].toString()) + ",";
        }
        if (classchargecode[1] != null && !classchargecode[1].toString().trim().equals("") && Integer.parseInt(classchargecode[1].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[1].toString()) + ",";
        }
        if (classchargecode[2] != null && !classchargecode[2].toString().trim().equals("") && Integer.parseInt(classchargecode[2].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[2].toString()) + ",";
        }
        if (classchargecode[3] != null && !classchargecode[3].toString().trim().equals("") && Integer.parseInt(classchargecode[3].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[3].toString()) + ",";
        }
        if (classchargecode[4] != null && !classchargecode[4].toString().trim().equals("") && Integer.parseInt(classchargecode[4].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[4].toString()) + ",";
        }
        if (classchargecode[5] != null && !classchargecode[5].toString().trim().equals("") && Integer.parseInt(classchargecode[5].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[5].toString()) + ",";
        }
        if (classchargecode[6] != null && !classchargecode[6].toString().trim().equals("") && Integer.parseInt(classchargecode[6].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[6].toString()) + ",";
        }
        if (classchargecode[7] != null && !classchargecode[7].toString().trim().equals("") && Integer.parseInt(classchargecode[7].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[7].toString()) + ",";
        }
        if (classchargecode[8] != null && !classchargecode[8].toString().trim().equals("") && Integer.parseInt(classchargecode[8].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[8].toString()) + ",";
        }
        if (classchargecode[9] != null && !classchargecode[9].toString().trim().equals("") && Integer.parseInt(classchargecode[9].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[9].toString()) + ",";
        }
        if (classchargecode[10] != null && !classchargecode[10].toString().trim().equals("") && Integer.parseInt(classchargecode[10].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[10].toString()) + ",";
        }
        if (classchargecode[11] != null && !classchargecode[11].toString().trim().equals("") && Integer.parseInt(classchargecode[11].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[11].toString()) + ",";
        }
        if (classchargecode[12] != null && !classchargecode[12].toString().trim().equals("") && Integer.parseInt(classchargecode[12].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[12].toString()) + ",";
        }
        if (classchargecode[13] != null && !classchargecode[13].toString().trim().equals("") && Integer.parseInt(classchargecode[13].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[13].toString()) + ",";
        }
        if (classchargecode[14] != null && !classchargecode[14].toString().trim().equals("") && Integer.parseInt(classchargecode[14].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[14].toString()) + ",";
        }
        if (classchargecode[15] != null && !classchargecode[15].toString().trim().equals("") && Integer.parseInt(classchargecode[15].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[15].toString()) + ",";
        }
        if (classchargecode[16] != null && !classchargecode[16].toString().trim().equals("") && Integer.parseInt(classchargecode[16].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[16].toString()) + ",";
        }
        if (classchargecode[17] != null && !classchargecode[17].toString().trim().equals("") && Integer.parseInt(classchargecode[17].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[17].toString()) + ",";
        }
        if (classchargecode[18] != null && !classchargecode[18].toString().trim().equals("") && Integer.parseInt(classchargecode[18].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[18].toString()) + ",";
        }
        if (classchargecode[19] != null && !classchargecode[19].toString().trim().equals("") && Integer.parseInt(classchargecode[19].toString()) > 0) {
            chgcod = chgcod + Integer.parseInt(classchargecode[19].toString()) + ",";
        }
        return chgcod;
    }
}

/**
 * @author Nagendra
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
import com.gp.cong.logisoft.domain.lcl.LclBookingExport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
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

public class LclBlChargesCalculation {
    private String CIFValue;
    private String pcb;
    private Double totalOceanFreight = 0.0, totalBarrelCharges = 0.0, totalBarellTTA = 0.0;
    private Double totalWeight = 0.0;
    private Double totalMeasure = 0.0;
    private Double flatrate = 0.0;
    private Double insurt;
    private Double insamt;
    private Double insMinChg;
    private Long totalcuft;
    private Integer totalpieces;
    private String databaseSchema;
    private DecimalFormat df = new DecimalFormat("#########.00");
    private Map chargesInfoMap = new HashMap();
    private Map totalPerMap = new HashMap();
    private List quoteRateList = new ArrayList();
    private List<ChargesInfoBean> chargesInfoList = new ArrayList();
    //private Integer ttChargeCode1;
    //private Integer ttChargeCode2;
    //private String portDifferential;
    private LCLRatesDAO lclratesdao;
    private LclBlAcDAO lclBlAcdao = new LclBlAcDAO();
    private PortsDAO portsdao = new PortsDAO();
    private GlMappingDAO glmappingdao = new GlMappingDAO();
    private LclBLPieceDAO lclblpiecedao = new LclBLPieceDAO();
    private int countWithBarrell = 0, countWithoutBarrell = 0;
    private String engmet, toZip;
    private BigDecimal weight = new BigDecimal(0.000);
    private BigDecimal measure = new BigDecimal(0.000);
    private CommonUtils commonutils = new CommonUtils();
    private LclUtils lclUtils = new LclUtils();
    private String ofratebasis = new String();
    private String stdchgratebasis = new String();
    private Ports ports;
    private RefTerminal refterminal;
    private String billCode;
    private String billToParty;
    private User currentUser;
    private Long fileId = 0l;

    public Ports getPorts() {
        return ports;
    }

    public void setPorts(Ports ports) {
        this.ports = ports;
    }

    public RefTerminal getRefterminal() {
        return refterminal;
    }

    public void setRefterminal(RefTerminal refterminal) {
        this.refterminal = refterminal;
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

    public void calculateRates(String origin, String destination, String pol, String pod, Long fileNumberId, List lclBlPiecesList, User user,
            String pickupYesNo, String insuranceYesNo, BigDecimal valueOfGoods, String rateType, String buttonValue, RoutingOptionsBean routingbean,
            String pickupReadyDate, String fromZip, HttpSession session, Boolean calcHeavy, String deliveryMetro, String pcBoth, String unLocationCode,
            String radioValue, HttpServletRequest request,boolean isConvertBl, boolean rateTypeFlag) throws Exception {
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
        billCode = pcBoth;
        billToParty = radioValue;
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        currentUser = user;
        fileId = fileNumberId;
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        LclBookingExport lclBookingExport = null;
        LclBookingExportDAO lclBookingExportDAO = new LclBookingExportDAO();
        if (buttonValue.equalsIgnoreCase("C")) {
            refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
                ofratebasis += pooorigin + "-";
            }
        } else if (buttonValue.equalsIgnoreCase("R")) {
            refterminal = refterminaldao.findById(origin);
            toZip = refterminal.getZipcde();
            routingbean.setToZip(toZip);
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
        if (refterminal != null && ports != null) {
            lclUtils.calculateRatesForBl(refterminal, ports, lclBlPiecesList, lclratesdao, engmet, fileNumberId);
            //Get the count of the selected Commodity
            calculateCountForBl(lclBlPiecesList);
            //Check the Fields Of T&T Charges
       /*
             if (loctn.getTtcode() != null && !loctn.getTtcode().trim().equals("")) {
             this.setTtChargeCode1(Integer.parseInt(loctn.getTtcode()));
             }*/
            //Calculate the Ocean Freight Rate
            calculateOfRate(countWithBarrell, pooorigin, polorigin, destinationpod, destinationfd, fileNumberId, lclBlPiecesList, user, refterminal, buttonValue, routingbean);
            int index = 0;
            if (fileNumberId != null && fileNumberId > 0) {
                lclBookingExport = lclBookingExportDAO.getLclBookingExportByFileNumber(fileNumberId);
            }
            boolean includedestinationfee = false;
            if (null != lclBookingExport) {
                if (lclBookingExport.isIncludeDestfees()) {
                    includedestinationfee = lclBookingExport.isIncludeDestfees();
                }
            }
            //Loop through the selected Commodity
            for (int j = 0; j < lclBlPiecesList.size(); j++) {
                LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
                if (fileNumberId != null && fileNumberId > 0) {
                    comnum = lclBlPiece.getCommodityType().getCode();
                } else {
                    comnum = lclBlPiece.getCommNo();
                }
                if (CommonUtils.isEmpty(comnum)) {
                    comnum = lclBlPiece.getCommNo();
                }
                if (buttonValue.equalsIgnoreCase("C") && j == 0) {
                    ofratebasis += comnum;
                }
                //Checking whether the object has commodity code
                //need to check
                if (lclBlPiece.getActualWeightImperial() != null) {
                    weight = weight.add(lclBlPiece.getActualWeightImperial());
                } else if (lclBlPiece.getBookedWeightImperial() != null) {
                    weight = weight.add(lclBlPiece.getBookedWeightImperial());
                }
                if (lclBlPiece.getActualVolumeImperial() != null) {
                    measure = measure.add(lclBlPiece.getActualVolumeImperial());
                } else if (lclBlPiece.getBookedVolumeImperial() != null) {
                    measure = measure.add(lclBlPiece.getBookedVolumeImperial());
                }
                if ((lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")
                        && lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals(""))
                        || lclBlPiece.isIsBarrel()) {
                    index++;
                    LclBlPiece commbeanforextralen = (LclBlPiece) lclBlPiecesList.get(0);
                    if (!lclBlPiece.isIsBarrel()) {
                        unbarrell = true;
                        /**
                         * Calculating total weights and total measures for
                         * calculating rates other than Ocean Freight
                         */
                        Double weightDouble = 0.00;
                        Double weightMeasure = 0.00;
                        if (engmet != null) {
                            if (engmet.equals("E")) {
                                if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBlPiece.getActualWeightImperial().doubleValue();
                                } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                    weightDouble = lclBlPiece.getBookedWeightImperial().doubleValue();
                                }
                                if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBlPiece.getActualVolumeImperial().doubleValue();
                                } else if (lclBlPiece.getBookedVolumeImperial() != null && lclBlPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                    weightMeasure = lclBlPiece.getBookedVolumeImperial().doubleValue();
                                }
                            } else if (engmet.equals("M")) {
                                if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBlPiece.getActualWeightMetric().doubleValue();
                                } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                    weightDouble = lclBlPiece.getBookedWeightMetric().doubleValue();
                                }
                                if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBlPiece.getActualVolumeMetric().doubleValue();
                                } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                    weightMeasure = lclBlPiece.getBookedVolumeMetric().doubleValue();
                                }
                            }
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
                        lclBlPiece.setStdchgRateBasis(stdChgRateBasis);
                        if (j == 0) {
                            this.setStdchgratebasis(stdChgRateBasis);
                        }
                    } //end of barrell if condition
                    //If the object is not null performing the rate calculation
                    if (stdchargesObj != null || lclBlPiece.isIsBarrel()) {
                        //Getting All the Charge Codes For Calculating Rates
                        String chgcode = getAddedChgCode(stdchargesObj, lclBlPiece, lclBlPiecesList, insuranceYesNo, valueOfGoods, pcBoth, ports, includedestinationfee, deliveryMetro, isConvertBl);
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
                                //Check portChargesList null
                                if (portChargesList != null && portChargesList.size() > 0) {
                                    //Loop through portChargesList
                                    for (int i = 0; i < portChargesList.size(); i++) {
                                        Object[] portcharges = (Object[]) portChargesList.get(i);
                                        //Calculate Rates for all portcharges
                                        setRates(portcharges, lclBlPiece, comnum, index, countWithoutBarrell, commbeanforextralen,
                                                fileNumberId, user, pooorigin, destinationfd, routingbean, buttonValue);
                                    }//end of portcharges for loop
                                }//end of if condition portChargesList null checking
                            }//end of stringtokenizer while loop
                        }//end of if condition chgcode

                    } //end of if condition stdcharges null checking
                } //end of if commodity code null

            } //end of commodity info list for

            if (chargesInfoMap != null && chargesInfoMap.size() > 0) {
                Iterator chargesInfoIterator = chargesInfoMap.values().iterator();
                while (chargesInfoIterator.hasNext()) {
                    ChargesInfoBean cinfobean = (ChargesInfoBean) chargesInfoIterator.next();
                    if (cinfobean.getChargesDesc() != null) {
                        if (!cinfobean.getChargesDesc().equals("OCEAN FREIGHT")) {
                            chargesInfoList.add(cinfobean);
                        }
                    }
                }
            }

            /*
             if (pickupYesNo != null && pickupYesNo.equalsIgnoreCase("Y")) {
             addPickup(fileNumberId);
             }*/
            if (insuranceYesNo != null && insuranceYesNo.equalsIgnoreCase("Y") && valueOfGoods != null) {
                GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode("0006", "LCLE", "AR");
                calculateInsuranceChargeForBl(pooorigin, polorigin, destinationfd, destinationpod, valueOfGoods.doubleValue(), fileNumberId, lclBlPiecesList, glmapping);
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
            if (buttonValue.equalsIgnoreCase("C")) {
                saveCharges(fileNumberId, user, buttonValue, includedestinationfee);
                if (unbarrell) {
                    if (totalPerMap != null && totalPerMap.size() > 0) {
                        addTotalPercentage(fileNumberId);
                    }
                }
                if ((fileNumberId != null && fileNumberId > 0) && !rateTypeFlag) {
                    reCalculateManualCharges(fileNumberId, user);
                }
            } else if (buttonValue.equalsIgnoreCase("R")) {
                String realPath = session.getServletContext().getRealPath("/xml/");
                String fileName = "ctsresponse" + session.getId() + ".xml";
                CallCTSWebServices ctsweb = new CallCTSWebServices();
                LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip,
                        pickupReadyDate, "" + weight, "" + measure, "CARRIER_CHARGE", "CARRIER_COST", "Exports");
                List carrierList = lclSession.getCarrierList();
                if (carrierList != null && carrierList.size() > 0) {
                    Carrier carrier = null;
                    if (carrierList.size() == 1) {
                        carrier = (Carrier) carrierList.get(0);
                    } else {
                        carrier = (Carrier) carrierList.get(1);
                    }

                    if (carrier != null && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                        routingbean.setCtsAmount("$" + carrier.getFinalcharge());
                    }
                    if (carrier != null && carrier.getScac() != null && !carrier.getScac().trim().equals("")) {
                        routingbean.setScac("(" + carrier.getScac() + ")");
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

    public void calculateCountForBl(List lclBlPiecesList) {
        if (lclBlPiecesList != null && lclBlPiecesList.size() > 0) {
            for (int j = 0; j < lclBlPiecesList.size(); j++) {
                LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
                if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")
                        && lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                    countWithBarrell++;
                    if (!lclBlPiece.isIsBarrel()) {
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
    public String getAddedChgCode(Object[] stdcharges, LclBlPiece lclBlPiece,
            List lclBlPiecesList, String insuranceYesNo, BigDecimal valueOfGoods, String pcBoth, Ports ports, boolean includedestinationfee, String deliveryMetro, boolean isConvertBl) throws Exception {
        String chgcod = new String();
        String pcb = new String();

        String overDimChgCdeForWeight = getchgCdeForOverDimWeight(lclBlPiecesList);
        String overDimChgCdeForMeasure = getchgCdeForOverDimMeasure(lclBlPiece.getLclBlPieceDetailList());
        boolean densecargofound = getchgCdeForDenseCargo(lclBlPiecesList);
        if (densecargofound) {
            chgcod = chgcod + "251,";
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

        if (lclBlPiece.getHazmat() && lclBlPiece.getCommodityType() != null) {
            chgcod = chgcod + "119,";
        }
        if (insuranceYesNo != null && insuranceYesNo.equalsIgnoreCase("Y") && valueOfGoods != null) {
            chgcod = chgcod + "6,";
        }/*
         if (ports.getNclcts() != null && ports.getNclcts().equalsIgnoreCase("y")) {
         if (comminfobean.getBillingMethod() != null && comminfobean.getBillingMethod().equalsIgnoreCase("C")) {
         String destid = ports.getId().getPrtnum();
         if (destid != null) {
         if (destid.equals("050") || destid.equals("060")) {
         chgcod = chgcod + "95,";
         } else {
         chgcod = chgcod + "5,";
         }
         }
         }
         }
         }
         */

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
        if (ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")) {
            if (pcBoth != null && (pcBoth.equalsIgnoreCase("C") || pcBoth.equalsIgnoreCase("B"))) {
                String destid = ports.getEciportcode();
                if (destid != null) {
                    chgcod = chgcod + "5,";
                }
            }
        }
        if (!isConvertBl && includedestinationfee) {
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
    public void setRates(Object[] portcharges, LclBlPiece lclBlPiece, String comnum, int index, int count, LclBlPiece commbeanforextralen,
            Long fileNumberId, User user, String pooorigin, String destinationfd, RoutingOptionsBean routingOptionsBean, String buttonValue) throws Exception {
        String chdCode = null;
        Integer chgType = null;
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
            Double insurt = 0.0;
            Double insamt = 0.0;
            String chargesDesc = new String();
            if (chgType != null && chgType > 0) {
                if (chdCode != null) {
                    GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(chdCode, "LCLE", "AR");
                    if (glmapping != null && glmapping.getChargeCode() != null) {
                        /*
                         if (glmapping.getChargeCode().equalsIgnoreCase("PRTDIF")) {
                         this.setPortDifferential(glmapping.getChargeCode());
                         }*/
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
                    // cinfobean.setLabel2("(" + df.format(d) + " FLAT RATE)");
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setRatePerUnit(d);
                    cinfobean.setRatePerUnitUom("FL");
                    cinfobean.setBookingPieceId(lclBlPiece.getId());
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
                            //String label1 = new String();
                            //String label2 = new String();
                            String rateUOM = new String();
                            Double cuftrt = 0.0;
                            Double wghtrt = 0.0;
                            Double minchg = 0.0;
                            Double ttcbmcftrate = 0.0;
                            Double ttlbskgsrate = 0.0;
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
                                    //    if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 100) * wghtrt;
                                    calculatedMeasure = totalMeasure * cuftrt;
                                    // }
                                    /*
                                     else {
                                     if (overdiminfo != null && overdiminfo.getTotalcuft() != null
                                     && !overdiminfo.getTotalcuft().equals("")) {
                                     double weightextralen = Double.parseDouble(commbeanforextralen.getLbskgsConvertedValue());
                                     int piecesextralen = 1;
                                     if (commbeanforextralen.getPieces() != null
                                     && !commbeanforextralen.getPieces().trim().equals("")) {
                                     piecesextralen = Integer.parseInt(commbeanforextralen.getPieces());
                                     }
                                     double measureextralen = overdiminfo.getTotalcuft();
                                     calculatedWeight = (weightextralen / piecesextralen / 100) * wghtrt;
                                     calculatedMeasure = measureextralen * cuftrt;
                                     }
                                     }*/
                                    //label2 = "(" + df.format(cuftrt) + "/CFT or ";
                                    //label2 = label2 + df.format(wghtrt) + "/100 LBS)";
                                    ttcbmcftrate = cuftrt;
                                    ttlbskgsrate = wghtrt;
                                } else if (engmet.equalsIgnoreCase("M")) {
                                    weightDiv = new BigDecimal(1000);
                                    //  if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                                    calculatedWeight = (totalWeight / 1000) * kgsrt;
                                    calculatedMeasure = totalMeasure * cbmrt;
                                    //  }
                                    /*
                                     else {
                                     if (overdiminfo != null && overdiminfo.getTotalcbm() != null
                                     && !overdiminfo.getTotalcbm().equals("")) {
                                     double weightextralen = Double.parseDouble(commbeanforextralen.getLbskgsConvertedValue());
                                     int piecesextralen = 1;
                                     if (commbeanforextralen.getPieces() != null
                                     && !commbeanforextralen.getPieces().trim().equals("")) {
                                     piecesextralen = Integer.parseInt(commbeanforextralen.getPieces());
                                     }
                                     double measureextralen = overdiminfo.getTotalcbm();
                                     calculatedWeight = (weightextralen / piecesextralen / 1000) * kgsrt;
                                     calculatedMeasure = measureextralen * cbmrt;
                                     }
                                     }*/
                                    //label2 = "(" + df.format(cbmrt) + " CBM) or ";
                                    //label2 = label2 + "(" + df.format(kgsrt) + "/1000 KGS)";
                                    ttcbmcftrate = cbmrt;
                                    ttlbskgsrate = kgsrt;
                                }
                            }
                            // if (!chdCode.equals("0032") && !chdCode.equals("0242")) {
                            if (calculatedWeight >= calculatedMeasure) {
                                finalValue = calculatedWeight;
                                //label1 = "*** TO WEIGHT ***";
                                rateUOM = "W";
                            } else {
                                finalValue = calculatedMeasure;
                                //label1 = "*** VOLUME ***";
                                rateUOM = "V";
                            }
                            if (finalValue <= minchg) {
                                finalValue = minchg;
                                //label1 = "*** MINIMUM ***";
                                rateUOM = "M";
                            }
//                            } else {
//                                finalValue = calculatedMeasure;
//                                //label1 = "*** VOLUME ***";
//                                rateUOM = "V";
//                                if (finalValue <= minchg) {
//                                    finalValue = minchg;
//                                    //label1 = "*** MINIMUM ***";
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
                            //cinfobean.setLabel1(label1);
                            //cinfobean.setLabel2(label2);
                            cinfobean.setPcb(this.getPcb());
                            cinfobean.setBookingPieceId(lclBlPiece.getId());
                            chargesInfoMap.put(chargesDesc, cinfobean);
                            quoteRateList.add(cinfobean);
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
    public void addTotalPercentage(Long fileNumberId) throws Exception {
        Iterator totPerIter = totalPerMap.values().iterator();
        boolean isCollectChargeContain = true;
        while (totPerIter.hasNext()) {
            TotalPercentageBean totbean = (TotalPercentageBean) totPerIter.next();
            Double finalValue = 0.0;
            if (totbean.getTotper() != null) {
                double collect_amount = lclBlAcdao.getBLTotalCollectChages(fileNumberId);
                if (!billCode.equalsIgnoreCase("p")) {
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
                saveCAFCharge(cinfobean, fileNumberId, currentUser, billToParty);
            }
//            chargesInfoList.add(cinfobean);
//            quoteRateList.add(cinfobean);
        }
    }

    public void saveCAFCharge(ChargesInfoBean cinfobean, Long fileNumberId, User user, String billToParty) throws Exception {
        LclBlAc lclBlAc = new LclBlAc();
        if(CommonUtils.isEmpty(billToParty)){
            billToParty = "A";
        }
        lclBlAc.setApBillToParty(billToParty);
        lclBlAc.setArBillToParty(billToParty);
        lclBlAc.setLclFileNumber(new LclFileNumber(fileNumberId));
        if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
            GlMapping glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
            lclBlAc.setArglMapping(glmapping);
            lclBlAc.setApglMapping(glmapping);
        }
        lclBlAc.setTransDatetime(new Date());
        if (cinfobean.getRatePerUnitUom() != null && cinfobean.getRatePerUnitUom().equalsIgnoreCase("PCT")) {
            lclBlAc.setRatePerUnit(cinfobean.getRatePerUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRate() != null) {
            lclBlAc.setArAmount(cinfobean.getRate().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclBlAc.setEnteredBy(user);
        lclBlAc.setModifiedBy(user);
        lclBlAc.setEnteredDatetime(new Date());
        lclBlAc.setModifiedDatetime(new Date());
        lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
        lclBlAc.setRateUom(engmet);
        if (engmet != null && engmet.equalsIgnoreCase("E")) {
            lclBlAc.setRateUom("I");
        }
        if (cinfobean.getRatePerWeightUnit() != null) {
            lclBlAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerWeightUnitDiv() != null) {
            lclBlAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnit() != null) {
            lclBlAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerVolumeUnitDiv() != null) {
            lclBlAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        lclBlAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
        if (cinfobean.getMinCharge() != null) {
            lclBlAc.setRateFlatMinimum(cinfobean.getMinCharge().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (cinfobean.getRatePerUnitDiv() != null) {
            lclBlAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        List<LclBlPiece> pieceList = lclblpiecedao.findByProperty("lclFileNumber.id", fileNumberId);
        lclBlAc.setLclBlPiece(pieceList.get(0));
        lclBlAc.setBundleIntoOf(false);
        lclBlAc.setPrintOnBl(true);
        lclBlAcdao.save(lclBlAc);
    }

    /**
     * @param A - Entered Goods value This method calculates the insurance and
     * adds it to the charges list
     */
    public void calculateInsuranceChargeForBl(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            Double valueOfGoods, Long fileNumberId, List<LclBlPiece> lclCommodityList, GlMapping glMapping) throws Exception {
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
        calculateInsurance(valueOfGoods, fileId, quoteRateList, glMapping);
//        calculateInsurance(valueOfGoods);
    }

    public void calculateInsurance(Double A, Long fileNumberId, List<LclBlPiece> lclCommodityList, GlMapping glMapping) throws Exception {
        Double CIFValue = 0.0;
        Double B = 0.00;
        LclCostChargeDAO lclcostchargedao = new LclCostChargeDAO();
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
        if (insamt != null) {
            insurance = (CIFValue / 100) * insurt;
        }
        if(insurance<=insMinChg){
            insurance=insMinChg;
        }
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

    /**
     * This method calculates the total for total percentage
     *
     * @return
     */
    public Double calculateTotalForTotalPercentage(Long fileNumberId) throws Exception {
        Double total = 0.0;
        List<LclBlPiece> lclBlPiecesList = lclblpiecedao.findByProperty("lclFileNumber.id", fileNumberId);
        List<LclBlAc> lclBlAclist = new LclBlAcDAO().getLclCostByFileNumberAsc(fileNumberId);
        if (lclBlPiecesList.size() == 1) {
            total = new LclBlAcDAO().getTotalWithoutParticularChargeCode(fileNumberId, "CAF");
        } else {
            total = Double.parseDouble(new BlUtils().calculateTotalByBlAcList(lclBlAclist));
        }
        return total;
    }

    public String getchgCdeForOverDimWeight(List lclBlPiecesList) {
        Double avgWeight = 0.0;
        int pieces = 1;
        String chargeCode = new String();
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        boolean thirdCndnSatisfied = false;
        for (int j = 0; j < lclBlPiecesList.size(); j++) {
            LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
            if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")
                    && lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                if (lclBlPiece.getActualPieceCount() != null && lclBlPiece.getActualPieceCount() != 0) {
                    pieces = lclBlPiece.getActualPieceCount();
                } else if (lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount() != 0) {
                    pieces = lclBlPiece.getBookedPieceCount();
                } else {
                    pieces = 1;
                }
                //if (portstemp.getEngmet().equals("E")) {
                if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                    avgWeight = lclBlPiece.getActualWeightImperial().doubleValue() / pieces;
                } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                    avgWeight = lclBlPiece.getBookedWeightImperial().doubleValue() / pieces;
                }
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
            chargeCode = "241";
        } else if (secondCndnSatisfied) {
            chargeCode = "240";
        } else if (firstCndnSatisfied) {
            chargeCode = "31";
        }
        return chargeCode;
    }

    public String getchgCdeForOverDimMeasure(List<LclBlPieceDetail> lclBlPieceDetailList) {
        String chargeCode = new String();
        boolean firstCndnSatisfied = false;
        boolean secondCndnSatisfied = false;
        if (lclBlPieceDetailList != null && lclBlPieceDetailList.size() > 0) {
            for (int j = 0; j < lclBlPieceDetailList.size(); j++) {
                LclBlPieceDetail lclBlPieceDetail = (LclBlPieceDetail) lclBlPieceDetailList.get(j);
                if (lclBlPieceDetail.getActualLength() != null) {
                    if (lclBlPieceDetail.getActualLength().doubleValue() >= 180 && lclBlPieceDetail.getActualLength().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBlPieceDetail.getActualLength().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclBlPieceDetail.getActualHeight() != null) {
                    if (lclBlPieceDetail.getActualHeight().doubleValue() >= 180 && lclBlPieceDetail.getActualHeight().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBlPieceDetail.getActualHeight().doubleValue() >= 360) {
                        secondCndnSatisfied = true;
                    }
                }
                if (lclBlPieceDetail.getActualWidth() != null) {
                    if (lclBlPieceDetail.getActualWidth().doubleValue() >= 180 && lclBlPieceDetail.getActualWidth().doubleValue() < 360) {
                        firstCndnSatisfied = true;
                    }
                    if (lclBlPieceDetail.getActualWidth().doubleValue() >= 360) {
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

    public boolean getchgCdeForDenseCargo(List lclBlPiecesList) {
        boolean cndnSatisfied = false;
        if (engmet != null && engmet.equalsIgnoreCase("M")) {
            if (lclBlPiecesList != null && lclBlPiecesList.size() > 0) {
                Double totalkgs = 0.0;
                Double totalcbm = 0.0;
                for (int j = 0; j < lclBlPiecesList.size(); j++) {
                    LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
                    if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                        totalkgs += lclBlPiece.getActualWeightMetric().doubleValue();
                    } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                        totalkgs += lclBlPiece.getBookedWeightMetric().doubleValue();
                    }
                    if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        totalcbm += lclBlPiece.getActualVolumeMetric().doubleValue();
                    } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        totalcbm += lclBlPiece.getBookedVolumeMetric().doubleValue();
                    }
                }//end of for loop
                if (totalkgs >= 5000 && totalcbm != 0) {
                    Double kgscbmratio = totalkgs / totalcbm;
                    if (kgscbmratio >= 1000) {
                        cndnSatisfied = true;
                    }
                }
            }//end of commodityList if condition
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

    /**
     *
     * @param lclBookingPiecesList
     * @param count
     * @param ports
     * @param loctn This method Calculates OfRate and add the OfRate to the List
     */
    public void calculateOfRate(int count, String pooorigin, String polorigin, String destinationpod, String destinationfd,
            Long fileNumberId, List lclBlPiecesList, User user, RefTerminal refterminal, String buttonValue, RoutingOptionsBean routingbean) throws Exception {
        Double minchg = 0.0;
        Double weight = 0.0;
        Double measure = 0.0;
        Double rateWeight = 0.0;
        Double rateMeasure = 0.0;
        //String label1 = new String();
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
        for (int j = 0; j < lclBlPiecesList.size(); j++) {
            LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
            if (fileNumberId != null && fileNumberId > 0) {
                commodityCode = lclBlPiece.getCommodityType().getCode();
            } else {
                commodityCode = lclBlPiece.getCommNo();
            }
            if (CommonUtils.isEmpty(commodityCode)) {
                commodityCode = lclBlPiece.getCommNo();
            }
            boolean lbsKgsFound = false;
            if (lclBlPiece.isIsBarrel()) {
                Object[] barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclBlPiece.getBookedPieceCount().intValue();
                            }
                            if (lclBlPiece.getActualPieceCount() != null && lclBlPiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = dblBareel.doubleValue() * lclBlPiece.getActualPieceCount().intValue();
                            }

                        }

                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclBlPiece.getBookedPieceCount().intValue();
                            }
                            if (lclBlPiece.getActualPieceCount() != null && lclBlPiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = dblTTA.doubleValue() * lclBlPiece.getActualPieceCount().intValue();
                            }

                        }
                    }
                }
            }//end of  isIsBarrel if condition
            //}
            //need to check here
            else {
                if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")
                        && lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                    index++;
                    ofratefound = true;
                    lbsKgsFound = true;
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            weightDiv = new BigDecimal(100);
                            if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                weight = lclBlPiece.getActualWeightImperial().doubleValue();
                            } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                weight = lclBlPiece.getBookedWeightImperial().doubleValue();
                            }
                            if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                measure = lclBlPiece.getActualVolumeImperial().doubleValue();
                            } else if (lclBlPiece.getBookedVolumeImperial() != null && lclBlPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                measure = lclBlPiece.getBookedVolumeImperial().doubleValue();
                            }
                        } else if (engmet.equals("M")) {
                            weightDiv = new BigDecimal(1000);
                            if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                weight = lclBlPiece.getActualWeightMetric().doubleValue();
                            } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                weight = lclBlPiece.getBookedWeightMetric().doubleValue();
                            }
                            if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                measure = lclBlPiece.getActualVolumeMetric().doubleValue();
                            } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                measure = lclBlPiece.getBookedVolumeMetric().doubleValue();
                            }
                        }
                    }
                    if (lclBlPiece.getOfratemin() != null && !lclBlPiece.getOfratemin().trim().equals("")) {
                        minchg = Double.parseDouble(lclBlPiece.getOfratemin());
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
                    if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")) {
                        rateWeight = Double.parseDouble(lclBlPiece.getPerLbsKgs());
                    }
                    if (lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                        rateMeasure = Double.parseDouble(lclBlPiece.getPerCftCbm());
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
                    cinfobean.setCommodityCode(lclBlPiece.getCommodityType().getCode());
                    cinfobean.setChargeType(3);
                    cinfobean.setRate(d);
                    cinfobean.setMeasureRate(rateMeasure);
                    cinfobean.setWeightRate(rateWeight);
                    cinfobean.setMinCharge(new BigDecimal(minchg));
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setBookingPieceId(lclBlPiece.getId());
                    chargesInfoMap.put("OCEAN FREIGHT", cinfobean);
                    chargesInfoList.add(cinfobean);
                    quoteRateList.add(cinfobean);
                    if (buttonValue.equalsIgnoreCase("R")) {
                        routingbean.setOfrateAmount("$" + NumberUtils.convertToTwoDecimal(rateMeasure) + "/$"
                                + NumberUtils.convertToTwoDecimal(rateWeight) + "/$" + NumberUtils.convertToTwoDecimal(minchg) + " Min");
                    }
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
                    cinfobeanForBarell.setCommodityCode(lclBlPiece.getCommodityType().getCode());
                    cinfobeanForBarell.setRate(bigBarrelCharges);
                    cinfobeanForBarell.setRatePerUnitUom("FL");
                    cinfobeanForBarell.setRatePerWeightUnit(dblBareel);
                    cinfobeanForBarell.setBookingPieceId(lclBlPiece.getId());
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
                    cinfobeanForBarellTTA.setCommodityCode(lclBlPiece.getCommodityType().getCode());
                    cinfobeanForBarellTTA.setRatePerWeightUnit(dblTTA);
                    cinfobeanForBarellTTA.setRate(bigBarrelTTACharges);
                    cinfobeanForBarellTTA.setRatePerUnitUom("FL");
                    cinfobeanForBarellTTA.setBookingPieceId(lclBlPiece.getId());
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
                cinfobean.setCommodityCode(lclBlPiece.getCommodityType().getCode());
                cinfobean.setChargeType(3);
                cinfobean.setRate(d);
                cinfobean.setMeasureRate(rateMeasure);
                cinfobean.setWeightRate(rateWeight);
                cinfobean.setMinCharge(new BigDecimal(minchg));
                cinfobean.setPcb(this.getPcb());
                cinfobean.setBookingPieceId(lclBlPiece.getId());
                quoteRateList.add(cinfobean);
            }

        }//end of for loop
    }//end of method

    public void calculateOfRateForCommodityForBl(String origin, String destination, String pol, String pod, String rateType, Long fileNumberId,
            LclBlPiece lclBlPiece, User user) throws Exception {
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
        if (refterminal != null && ports != null) {
            String commodityCode = new String();
            if (fileNumberId != null && fileNumberId > 0) {
                commodityCode = lclBlPiece.getCommodityType().getCode();
            } else {
                commodityCode = lclBlPiece.getCommNo();
            }
            if (CommonUtils.isEmpty(commodityCode)) {
                commodityCode = lclBlPiece.getCommNo();
            }
            lclUtils.calculateRatesForBlPiece(refterminal, ports, lclBlPiece, lclratesdao, engmet, fileNumberId);
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

            if (lclBlPiece.isIsBarrel()) {
                Object[] barellObj = lclratesdao.getBarrelRate(pooorigin, destinationfd, commodityCode);
                if (barellObj != null && barellObj.length > 0) {
                    if (barellObj[0] != null) {
                        dblBareel = (BigDecimal) barellObj[0];
                        if (dblBareel.doubleValue() > 0.00) {
                            if (lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclBlPiece.getBookedPieceCount().intValue());
                            }
                            if (lclBlPiece.getActualPieceCount() != null && lclBlPiece.getActualPieceCount().intValue() > 0) {
                                totalBarrelCharges = totalBarrelCharges + (dblBareel.doubleValue() * lclBlPiece.getActualPieceCount().intValue());
                            }
                        }

                    }
                    if (barellObj[1] != null) {
                        dblTTA = (BigDecimal) barellObj[1];
                        if (dblTTA.doubleValue() > 0.00) {
                            if (lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getBookedPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclBlPiece.getBookedPieceCount().intValue());
                            }
                            if (lclBlPiece.getActualPieceCount() != null && lclBlPiece.getActualPieceCount().intValue() > 0) {
                                totalBarellTTA = totalBarellTTA + (dblTTA.doubleValue() * lclBlPiece.getActualPieceCount().intValue());
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
                        if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                            weight = lclBlPiece.getActualWeightImperial().doubleValue();
                        } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                            weight = lclBlPiece.getBookedWeightImperial().doubleValue();
                        }
                        if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                            measure = lclBlPiece.getActualVolumeImperial().doubleValue();
                        } else if (lclBlPiece.getBookedVolumeImperial() != null && lclBlPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                            measure = lclBlPiece.getBookedVolumeImperial().doubleValue();
                        }
                    } else if (engmet.equals("M")) {
                        weightDiv = new BigDecimal(1000);
                        if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                            weight = lclBlPiece.getActualWeightMetric().doubleValue();
                        } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                            weight = lclBlPiece.getBookedWeightMetric().doubleValue();
                        }
                        if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBlPiece.getActualVolumeMetric().doubleValue();
                        } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                            measure = lclBlPiece.getBookedVolumeMetric().doubleValue();
                        }
                    }
                }
                if (lclBlPiece.getOfratemin() != null && !lclBlPiece.getOfratemin().trim().equals("")) {
                    minchg = Double.parseDouble(lclBlPiece.getOfratemin());
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
                if (lclBlPiece.getPerLbsKgs() != null && !lclBlPiece.getPerLbsKgs().trim().equals("")) {
                    rateWeight = Double.parseDouble(lclBlPiece.getPerLbsKgs());
                }
                if (lclBlPiece.getPerCftCbm() != null && !lclBlPiece.getPerCftCbm().trim().equals("")) {
                    rateMeasure = Double.parseDouble(lclBlPiece.getPerCftCbm());
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
                    cinfobean.setCommodityCode(commodityCode);
                    cinfobean.setChargeType(3);
                    cinfobean.setRate(d);
                    cinfobean.setMeasureRate(rateMeasure);
                    cinfobean.setWeightRate(rateWeight);
                    cinfobean.setMinCharge(new BigDecimal(minchg));
                    cinfobean.setPcb(this.getPcb());
                    cinfobean.setBookingPieceId(lclBlPiece.getId());
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
                        cinfobeanForBarell.setBookingPieceId(lclBlPiece.getId());
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
                        cinfobeanForBarellTTA.setBookingPieceId(lclBlPiece.getId());
                        chargesInfoList.add(cinfobeanForBarellTTA);
                        quoteRateList.add(cinfobeanForBarellTTA);
                    }
                }
                saveCharges(fileNumberId, user, "C", false);
            } else {
                if (ofratefound) {
                    lclBlPiece.setOfrateamount(rateMeasure + "/$" + rateWeight);
                } else {
                    if (totalBarrelCharges > 0.00 && dblBareel != null) {
                        lclBlPiece.setOfrateamount(dblBareel.toString());
                    }
                    if (totalBarellTTA > 0.00 && dblTTA != null) {
                        lclBlPiece.setOfrateamount(dblTTA.toString());
                    }
                }
            }

        }//end of refterminal if condition
    }//end of method

    public void saveCharges(Long fileNumberId, User user, String buttonValue, boolean includedestinationfee) throws Exception {
        Map adjustmentMap = null, adjustComment = null;
        if (!buttonValue.equalsIgnoreCase("CH")) {
            if (fileNumberId != null && fileNumberId > 0) {
                List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclCostByFileNumberME(fileNumberId, false);
                lclBlAcdao.deleteLclCostByFileNumber(fileNumberId);
                LclFileNumber lclFileNumber = new LclFileNumberDAO().findById(fileNumberId);
                adjustmentMap = new HashMap();
                adjustComment = new HashMap();
                for (LclBlAc lclBlAc : lclBlAcList) {
                    if (lclBlAc != null) {
                        String chargeCode = lclBlAc.getArglMapping().getChargeCode();
                        BigDecimal amount = lclBlAc.getArAmount();
                        LclRemarks lclRemarks = new LclRemarks();
                        lclRemarks.setLclFileNumber(lclFileNumber);
                        lclRemarks.setEnteredBy(user);
                        lclRemarks.setEnteredDatetime(new Date());
                        lclRemarks.setModifiedBy(user);
                        lclRemarks.setModifiedDatetime(new Date());
                        lclRemarks.setRemarks("DELETED -> Charge Code -> " + chargeCode + " Charge Amount -> " + amount);
                        lclRemarks.setType(LclCommonConstant.REMARKS_BL_AUTO_NOTES);
                        new LclRemarksDAO().saveOrUpdate(lclRemarks);
                        if (lclBlAc.getAdjustmentAmount().doubleValue() != 0.00) {
                            adjustmentMap.put(lclBlAc.getArglMapping().getChargeCode(), lclBlAc.getAdjustmentAmount());
                            adjustComment.put(lclBlAc.getArglMapping().getChargeCode(), lclBlAc.getAdjustmentComment());
                        }
                    }
                }
            }
            if (quoteRateList != null && quoteRateList.size() > 0) {
                for (int i = 0; i < quoteRateList.size(); i++) {
                    ChargesInfoBean cinfobean = (ChargesInfoBean) quoteRateList.get(i);
                    LclBlAc lclBlAc = new LclBlAc();
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        lclBlAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                    }
                    if (CommonUtils.isNotEmpty(billToParty)) {
                        lclBlAc.setApBillToParty(billToParty);
                        lclBlAc.setArBillToParty(billToParty);
                    }
                    if (CommonUtils.isNotEmpty(cinfobean.getChargeCode())) {
                        GlMapping glmapping = null;
                        if (cinfobean.getChargeCode().equalsIgnoreCase("OFBARR") || cinfobean.getChargeCode().equalsIgnoreCase("TTBARR")) {
                            glmapping = glmappingdao.getByProperty("chargeCode", cinfobean.getChargeCode());
                        } else {
                            glmapping = glmappingdao.findByBlueScreenChargeCode(cinfobean.getChargeCode(), "LCLE", "AR");
                        }

                        lclBlAc.setArglMapping(glmapping);
                        lclBlAc.setApglMapping(glmapping);
                    }
                    lclBlAc.setTransDatetime(new Date());
                    lclBlAc.setApAmount(null);
                    lclBlAc.setRatePerUnit(cinfobean.getRate());
                    lclBlAc.setArAmount(cinfobean.getRate());
                    lclBlAc.setEnteredBy(user);
                    lclBlAc.setModifiedBy(user);
                    lclBlAc.setEnteredDatetime(new Date());
                    lclBlAc.setModifiedDatetime(new Date());
                    lclBlAc.setRateUom(engmet);
                    if (engmet != null && engmet.equalsIgnoreCase("E")) {
                        lclBlAc.setRateUom("I");
                    }
                    lclBlAc.setRatePerWeightUnit(cinfobean.getRatePerWeightUnit());
                    lclBlAc.setRatePerWeightUnitDiv(cinfobean.getRatePerWeightUnitDiv());
                    lclBlAc.setRatePerVolumeUnit(cinfobean.getRatePerVolumeUnit());
                    lclBlAc.setRatePerVolumeUnitDiv(cinfobean.getRatePerVolumeUnitDiv());
                    lclBlAc.setRatePerUnitUom(cinfobean.getRatePerUnitUom());
                    lclBlAc.setRateFlatMinimum(cinfobean.getMinCharge());
                    lclBlAc.setRatePerUnitDiv(cinfobean.getRatePerUnitDiv());
                    if (cinfobean.getBookingPieceId() != null && cinfobean.getBookingPieceId() > 0) {
                        lclBlAc.setLclBlPiece(lclblpiecedao.findById(cinfobean.getBookingPieceId()));
                    }
                    if (CommonUtils.isNotEmpty(lclBlAc.getArglMapping().getChargeCode()) && lclBlAc.getArglMapping().getChargeCode().substring(0, 2).equalsIgnoreCase("TT")) {//T&T charge has auto check
                        lclBlAc.setBundleIntoOf(true);
                    } else {
                        lclBlAc.setBundleIntoOf(false);
                    }
                    if (null != adjustmentMap && adjustmentMap.size() > 0 && adjustmentMap.containsKey(lclBlAc.getArglMapping().getChargeCode())) {
                        lclBlAc.setAdjustmentAmount(new BigDecimal(adjustmentMap.get(lclBlAc.getArglMapping().getChargeCode()).toString()));
                        lclBlAc.setAdjustmentComment(adjustComment != null && adjustComment.get(lclBlAc.getArglMapping().getChargeCode()) != null
                                ? adjustComment.get(lclBlAc.getArglMapping().getChargeCode()).toString() : "");
                    } else {
                        lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
                    }

                    lclBlAc.setPrintOnBl(true);
                    if (includedestinationfee && (lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTBL")
                            || lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("DESTWM"))) {
                        lclBlAc.setManualEntry(true);
                    }
                    lclBlAcdao.save(lclBlAc);
                }
            }
        }
    }

    public void calculateCAFCharge(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBlPiece> lclBlPiecesList, String pcBoth, User user, Long fileNumberId,
            HttpServletRequest request, Ports ports, String billToParty) throws Exception {
        LclBlAc lclBlAc = lclBlAcdao.findByChargeCode(fileNumberId, false, "LCLE", "CAF");
        double collect_amount = lclBlAcdao.getBLTotalCollectChages(fileNumberId);
        if (collect_amount > 0) {
            billToParty = "A";
            databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
            lclratesdao = new LCLRatesDAO(databaseSchema);
            String chgcod = null;
            LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
            Object ncl[] = lclPortConfigurationDAO.lclPortConfiguration(ports.getUnLocationCode());
            if (ncl[2].toString() != null && ncl[2].toString().equalsIgnoreCase("y")) {
                List<LclBlAc> chargeList = lclBlAcdao.getLclCostWithoutCAF(fileNumberId);
                List consolidatedlist = new LclConsolidateDAO().getConsolidatesFiles(fileNumberId);
                List<LclBlAc> lclBlAcList = new BlUtils().getRolledUpChargesForBl(lclBlPiecesList,
                        chargeList, ports.getEngmet(), consolidatedlist, true);
                for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                    chgcod = "0005";
                    List prtChgsCommodityList = lclratesdao
                            .findByChdcod(pooorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), chgcod);
                    List prtChgsZeroCommodityList = lclratesdao
                            .findByChdcod(pooorigin, destinationfd, "000000", chgcod);
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao
                                .findByChdcod(pooorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao
                                .findByChdcod(pooorigin, destinationpod, "000000", chgcod);
                    }
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao
                                .findByChdcod(polorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao
                                .findByChdcod(polorigin, destinationfd, "000000", chgcod);
                    }
                    if (prtChgsCommodityList.isEmpty() && prtChgsZeroCommodityList.isEmpty()) {
                        prtChgsCommodityList = lclratesdao
                                .findByChdcod(polorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), chgcod);
                        prtChgsZeroCommodityList = lclratesdao
                                .findByChdcod(polorigin, destinationpod, "000000", chgcod);
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
                        lclBlAcdao.saveOrUpdate(lclBlAc);
                    }
                }
            }
        } else {
            if (lclBlAc != null) {
                String desc = "DELETED -> Charge Code -> " + lclBlAc.getArglMapping().getChargeCode()
                        + " Charge Amount -> " + lclBlAc.getArAmount();
                lclBlAcdao.delete(lclBlAc);
                new LclRemarksDAO().insertLclRemarks(fileNumberId, "BL-AutoNotes", desc, user.getUserId());
            }
        }
    }

    public String[] getCAFCalculationContent(LclBl lclBl) throws Exception {
        String result[] = new String[4];
        PortsDAO portsDAO = new PortsDAO();
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        String rateType = "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType();
        RefTerminal refterminal = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfOrigin().getUnLocationCode(), rateType);
        if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
            result[0] = refterminal.getTrmnum();
        }
        RefTerminal refterminalpol = refterminaldao.getTerminalByUnLocation(lclBl.getPortOfLoading().getUnLocationCode(), rateType);
        if (refterminalpol != null && refterminalpol.getTrmnum() != null && !refterminalpol.getTrmnum().trim().equals("")) {
            result[1] = refterminalpol.getTrmnum();
        }
        Ports portspod = portsDAO.getByProperty("unLocationCode", lclBl.getPortOfDestination().getUnLocationCode());
        if (portspod != null && portspod.getEciportcode() != null && !portspod.getEciportcode().trim().equals("")) {
            result[2] = portspod.getEciportcode();
        }
        if (null != lclBl.getFinalDestination()) {
            Ports ports = portsDAO.getByProperty("unLocationCode", lclBl.getFinalDestination().getUnLocationCode());
            if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
                result[3] = ports.getEciportcode();
            }
        }
        return result;
    }

    public void reCalculateManualCharges(Long fileNumberId, User user) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        Double calculatedWeight = 0.00;
        Double calculatedMeasure = 0.00;
        List<LclBlAc> lclBlAcList = new LclBlAcDAO().getLclCostByFileNumberME(fileNumberId, Boolean.TRUE);
        List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
        if (CommonUtils.isNotEmpty(lclBlAcList)) {
            for (LclBlAc lclBlAc : lclBlAcList) {
                if (!CommonUtils.isEmpty(lclBlAc.getRatePerWeightUnit())
                        || !CommonUtils.isEmpty(lclBlAc.getRatePerVolumeUnit())) {
                    if (CommonUtils.isNotEmpty(lclBlPiecesList)) {
                        totalMeasure = 0.00;
                        totalWeight = 0.00;
                        for (LclBlPiece piece : lclBlPiecesList) {
                            Double weightDouble = 0.00;
                            Double weightMeasure = 0.00;
                            if (engmet != null) {
                                if (engmet.equals("E")) {
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
                                } else if (engmet.equals("M")) {
                                    if (piece.getActualWeightMetric() != null && piece.getActualWeightMetric().doubleValue() != 0.00) {
                                        weightDouble = piece.getActualWeightMetric().doubleValue();
                                    } else if (piece.getBookedWeightMetric() != null && piece.getBookedWeightMetric().doubleValue() != 0.00) {
                                        weightDouble = piece.getBookedWeightMetric().doubleValue();
                                    }
                                    if (piece.getActualVolumeMetric() != null && piece.getActualVolumeMetric().doubleValue() != 0.00) {
                                        weightMeasure = piece.getActualVolumeMetric().doubleValue();
                                    } else if (piece.getBookedVolumeMetric() != null && piece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                        weightMeasure = piece.getBookedVolumeMetric().doubleValue();
                                    }
                                }
                                totalWeight = totalWeight + weightDouble;
                                totalMeasure = totalMeasure + weightMeasure;
                            }
                        }
                    }
                    if (engmet != null) {
                        if (engmet.equals("E")) {
                            calculatedWeight = (totalWeight / 100) * lclBlAc.getRatePerWeightUnit().doubleValue();
                            lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(100));
                        } else if (engmet.equals("M")) {
                            calculatedWeight = (totalWeight / 1000) * lclBlAc.getRatePerWeightUnit().doubleValue();
                            lclBlAc.setRatePerWeightUnitDiv(new BigDecimal(1000));
                        }
                    }//end of else if engmet
                    if(lclBlAc.getArglMapping().getChargeCode().equals("DESTWM")){
                    calculatedMeasure = lclBlAc.getRatePerVolumeUnit().doubleValue();
                    }else{
                    calculatedMeasure = totalMeasure * lclBlAc.getRatePerVolumeUnit().doubleValue();    
                    }
                    lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerWeightUnitDiv());
                    lclBlAc.setRatePerVolumeUnitDiv(new BigDecimal(1000));
                    if (calculatedWeight >= calculatedMeasure && calculatedWeight >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                        lclBlAc.setArAmount(new BigDecimal(calculatedWeight));
                        lclBlAc.setRatePerUnitUom("W");
                        lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());
                    } else if (calculatedMeasure >= calculatedWeight && calculatedMeasure >= lclBlAc.getRateFlatMinimum().doubleValue()) {
                        lclBlAc.setArAmount(new BigDecimal(calculatedMeasure));
                        lclBlAc.setRatePerUnitUom("V");
                        lclBlAc.setRatePerUnitDiv(lclBlAc.getRatePerVolumeUnitDiv());
                    } else {
                        lclBlAc.setArAmount(lclBlAc.getRateFlatMinimum());
                        lclBlAc.setRatePerUnitUom("M");
                    }
                }
                if (!CommonUtils.isEmpty(lclBlAc.getRatePerUnit()) && CommonUtils.isEmpty(lclBlAc.getRatePerWeightUnit())
                        && CommonUtils.isEmpty(lclBlAc.getRatePerVolumeUnit())) {
                    lclBlAc.setRatePerUnitUom("FL");
                    lclBlAc.setArAmount(lclBlAc.getRatePerUnit());
                }
                lclBlAc.setBundleIntoOf(false);
                lclBlAc.setPrintOnBl(true);
                lclBlAc.setLclFileNumber(new LclFileNumber(fileId));
                lclBlAc.setTransDatetime(new Date());
                lclBlAc.setModifiedBy(user);
                lclBlAc.setModifiedDatetime(new Date());
                lclCostChargeDAO.saveOrUpdate(lclBlAc);
            }
        }
    }

    public void calculateChargeForDeliveyMetro(String pooorigin, String polorigin, String destinationfd, String destinationpod,
            List<LclBlPiece> lclBlPiecesList, User user, Long fileNumberId, String engmet, String oldBlueScreenChargeCode, String billToParty) throws Exception {
        LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
        databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        lclratesdao = new LCLRatesDAO(databaseSchema);
        GlMapping glMapping = glmappingdao.findByBlueScreenChargeCode(oldBlueScreenChargeCode, "LCLE", "AR");
        String blueScreenChargeCode = new String();
        for (int j = 0; j < lclBlPiecesList.size(); j++) {
            LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(j);
            LclBlAc lclBlAc = lclBlAcdao.getLclCostByChargeCodeBlPiece(fileNumberId, oldBlueScreenChargeCode, false, lclBlPiece.getId());
            Date d = new Date();
            if (lclBlAc == null) {
                lclBlAc = new LclBlAc();
                lclBlAc.setApBillToParty(billToParty);
                lclBlAc.setArBillToParty(billToParty);
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    lclBlAc.setLclFileNumber(new LclFileNumber(fileNumberId));
                }
                lclBlAc.setEnteredBy(user);
                lclBlAc.setEnteredDatetime(d);
                lclBlAc.setAdjustmentAmount(BigDecimal.ZERO);
                lclBlAc.setLclBlPiece(lclBlPiece);
            }
            lclBlAc.setTransDatetime(d);
            lclBlAc.setModifiedBy(user);
            lclBlAc.setModifiedDatetime(d);
            Double weightDouble = 0.00;
            Double weightMeasure = 0.00;
            if (engmet != null) {
                if (engmet.equals("E")) {
                    if (lclBlPiece.getActualWeightImperial() != null && lclBlPiece.getActualWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclBlPiece.getActualWeightImperial().doubleValue();
                    } else if (lclBlPiece.getBookedWeightImperial() != null && lclBlPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                        weightDouble = lclBlPiece.getBookedWeightImperial().doubleValue();
                    }
                    if (lclBlPiece.getActualVolumeImperial() != null && lclBlPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclBlPiece.getActualVolumeImperial().doubleValue();
                    } else if (lclBlPiece.getBookedVolumeImperial() != null && lclBlPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                        weightMeasure = lclBlPiece.getBookedVolumeImperial().doubleValue();
                    }

                } else if (engmet.equals("M")) {
                    if (lclBlPiece.getActualWeightMetric() != null && lclBlPiece.getActualWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclBlPiece.getActualWeightMetric().doubleValue();
                    } else if (lclBlPiece.getBookedWeightMetric() != null && lclBlPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                        weightDouble = lclBlPiece.getBookedWeightMetric().doubleValue();
                    }
                    if (lclBlPiece.getActualVolumeMetric() != null && lclBlPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclBlPiece.getActualVolumeMetric().doubleValue();
                    } else if (lclBlPiece.getBookedVolumeMetric() != null && lclBlPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                        weightMeasure = lclBlPiece.getBookedVolumeMetric().doubleValue();
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
            Object[] portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), blueScreenChargeCode);
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationfd, "000000", blueScreenChargeCode);
            }//end of stdcharges null checking
            //If the object is null Getting the stdcharges using pooorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using pooorigin and destinationpod and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(pooorigin, destinationpod, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, lclBlPiece.getCommodityType().getCode(), blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationfd and 0 commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationfd, "000000", blueScreenChargeCode);
            } //If the object is null Getting the stdcharges using polorigin and destinationpod and commodity
            if (portChargeArray == null || portChargeArray.length == 0) {
                portChargeArray = lclratesdao.findByChdcodForDeliveryMetro(polorigin, destinationpod, lclBlPiece.getCommodityType().getCode(), blueScreenChargeCode);
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
                        lclBlAc.setRateUom(engmet);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclBlAc.setRateUom("I");
                        }
                        BigDecimal bd = new BigDecimal(flatrate);
                        lclBlAc.setRatePerUnit(bd);
                        lclBlAc.setArAmount(bd);
                        lclBlAc.setArglMapping(glMapping);
                        lclBlAc.setRatePerUnitUom("FL");
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
                                lclBlAc.setRatePerWeightUnit(new BigDecimal(wghtrt));
                                lclBlAc.setRatePerVolumeUnit(new BigDecimal(cuftrt));
                            } else if (engmet.equalsIgnoreCase("M")) {
                                weightDiv = new BigDecimal(1000);
                                calculatedWeight = (totalWeight / 1000) * kgsrt;
                                calculatedMeasure = totalMeasure * cbmrt;
                                lclBlAc.setRatePerWeightUnit(new BigDecimal(cbmrt));
                                lclBlAc.setRatePerVolumeUnit(new BigDecimal(kgsrt));
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
                        lclBlAc.setRatePerWeightUnitDiv(weightDiv);
                        lclBlAc.setRatePerVolumeUnitDiv(measureDiv);
                        lclBlAc.setRateUom(engmet);
                        if (engmet != null && engmet.equalsIgnoreCase("E")) {
                            lclBlAc.setRateUom("I");
                        }
                        lclBlAc.setRatePerUnit(bdf);
                        lclBlAc.setArAmount(bdf);
                        glMapping = glmappingdao.findByBlueScreenChargeCode(blueScreenChargeCode, "LCLE", "AR");
                        lclBlAc.setArglMapping(glMapping);
                        lclBlAc.setRatePerUnitUom(rateUOM);
                        lclBlAc.setRateFlatMinimum(new BigDecimal(minchg));
                    }//end of charge type 3 if condition
                }
                lclBlAc.setArBillToParty(billToParty);
                lclBlAc.setApBillToParty(billToParty);
                lclBlAc.setBundleIntoOf(false);
                lclBlAc.setPrintOnBl(true);
                lclCostChargeDAO.saveOrUpdate(lclBlAc);
            }

        }
    }

}

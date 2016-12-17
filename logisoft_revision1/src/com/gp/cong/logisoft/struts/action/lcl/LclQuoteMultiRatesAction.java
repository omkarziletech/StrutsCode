/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.beans.RoutingOriginBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.lcl.comparator.LclRoutingOriginComparator;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.dwr.LclQuotationChargesCalculation;
import com.gp.cong.lcl.webservices.dom.Carrier;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclWarehsDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuoteMultiRateForm;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Owner
 */
public class LclQuoteMultiRatesAction extends LogiwareDispatchAction {

    private static String LCL_MULTIRATES = "lclmultirates";
    private static String LCL_ROUTING_CARRIER = "lclroutingcarrier";
    private LclUtils lclUtils = new LclUtils();

    public ActionForward displayRates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteMultiRateForm lclQuoteMultiRateForm = (LclQuoteMultiRateForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        Long fileNumberId = lclQuoteMultiRateForm.getFileNumberId();
        //if (lclSession.getRoutingOptionsList() == null || lclSession.getRoutingOptionsList().size() == 0) {
        String fromZip = "", commodityCode = "", rateTypeStr = "";
        LclBookingPlanDAO lclbookingplandao = new LclBookingPlanDAO();
        String origin = lclQuoteMultiRateForm.getOrigin();
        String destination = lclQuoteMultiRateForm.getDestination();
        String sailDate = "";
        String pol = new String();
        String pod = new String();
        boolean hazmat = false;
        String cfcl = (null!= lclQuoteMultiRateForm.getCfcl()) ? "C" :"E" ;
        if (lclQuoteMultiRateForm.getZip() != null && !lclQuoteMultiRateForm.getZip().trim().equals("")) {
            String[] zip = lclQuoteMultiRateForm.getZip().split("-");
            fromZip = zip[0];
            request.setAttribute("zip", fromZip);
            request.setAttribute("doorOrigin", zip[1]);
        }
        if (lclQuoteMultiRateForm.getDestinationValue() != null && !lclQuoteMultiRateForm.getDestinationValue().trim().equals("")) {
            request.setAttribute("destination", lclQuoteMultiRateForm.getDestinationValue());
        }
        List lclQuotePiecesList = null;
        if (fileNumberId != null && fileNumberId > 0) {
            lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
        } else if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
            lclQuotePiecesList = lclSession.getQuoteCommodityList();
        }
        String rateType = lclQuoteMultiRateForm.getRateType();
        if (rateType != null) {
            if (rateType.equalsIgnoreCase("Y")) {
                rateTypeStr = "Retail";
            } else if (rateType.equalsIgnoreCase("C")) {
                rateTypeStr = "Coload";
            } else if (rateType.equalsIgnoreCase("F")) {
                rateTypeStr = "Foreign to Foreign";
            }
        }
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclratesdao = new LCLRatesDAO(databaseSchema);
        RefTerminalDAO refterminaldao = new RefTerminalDAO();
        PortsDAO portsdao = new PortsDAO();
        String pooorigin = new String();
        RefTerminal refterminal = null;
        if (origin != null && !origin.trim().equals("")) {
            refterminal = refterminaldao.getTerminalByUnLocation(origin, rateType);
            if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                pooorigin = refterminal.getTrmnum();
            }
        }
        Ports ports = portsdao.getByProperty("unLocationCode", destination);
        if (ports != null && ports.getEciportcode() != null && !ports.getEciportcode().trim().equals("")) {
            String comnumArray = new String();
            if (lclQuotePiecesList != null && lclQuotePiecesList.size() > 0) {
                request.setAttribute("isCommodityFlag", true);
                String commodityCodeOne = new String();
                String commodityCodeMany = new String();
                String commodityCftCbm = new String();
                String commodityLbsKgs = new String();
                if (lclQuotePiecesList.size() > 1) {
                    request.setAttribute("showPlus", "Y");
                }
                for (int i = 0; i < lclQuotePiecesList.size(); i++) {
                    LclQuotePiece lclQuotePiece = (LclQuotePiece) lclQuotePiecesList.get(i);
                    if (lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().getCode() != null
                            && !lclQuotePiece.getCommodityType().getCode().trim().equals("")) {
                        comnumArray = comnumArray + lclQuotePiece.getCommodityType().getCode() + ",";
                    }
                    if (fileNumberId != null && fileNumberId > 0) {
                        if (i == 0) {
                            commodityCodeOne = lclQuotePiece.getCommodityType().getDescEn() + "(" + lclQuotePiece.getCommodityType().getCode() + ")";
                            commodityCodeMany = lclQuotePiece.getCommodityType().getDescEn() + "(" + lclQuotePiece.getCommodityType().getCode() + ")<br>";
                            if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclQuotePiece.getActualWeightImperial().toString();
                            } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclQuotePiece.getBookedWeightImperial().toString();
                            }
                            if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclQuotePiece.getActualVolumeImperial().toString();
                            } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclQuotePiece.getBookedVolumeImperial().toString();
                            }
                            if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclQuotePiece.getActualWeightMetric().toString();
                            } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclQuotePiece.getBookedWeightMetric().toString();
                            }
                            if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclQuotePiece.getActualVolumeMetric().toString();
                            } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclQuotePiece.getBookedVolumeMetric().toString();
                            }
                        } else {
                            commodityCodeMany = commodityCodeMany + lclQuotePiece.getCommodityType().getDescEn() + "(" + lclQuotePiece.getCommodityType().getCode() + ")<br>";
                        }
                    } else {
                        comnumArray = comnumArray + lclQuotePiece.getCommNo() + ",";
                        if (i == 0) {
                            commodityCodeOne = lclQuotePiece.getCommName() + "(" + lclQuotePiece.getCommNo() + ")";
                            commodityCodeMany = lclQuotePiece.getCommName() + "(" + lclQuotePiece.getCommNo() + ")<br>";
                            if (lclQuotePiece.getActualWeightImperial() != null && lclQuotePiece.getActualWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclQuotePiece.getActualWeightImperial().toString();
                            } else if (lclQuotePiece.getBookedWeightImperial() != null && lclQuotePiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclQuotePiece.getBookedWeightImperial().toString();
                            }
                            if (lclQuotePiece.getActualVolumeImperial() != null && lclQuotePiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclQuotePiece.getActualVolumeImperial().toString();
                            } else if (lclQuotePiece.getBookedVolumeImperial() != null && lclQuotePiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclQuotePiece.getBookedVolumeImperial().toString();
                            }
                            if (lclQuotePiece.getActualWeightMetric() != null && lclQuotePiece.getActualWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclQuotePiece.getActualWeightMetric().toString();
                            } else if (lclQuotePiece.getBookedWeightMetric() != null && lclQuotePiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclQuotePiece.getBookedWeightMetric().toString();
                            }
                            if (lclQuotePiece.getActualVolumeMetric() != null && lclQuotePiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclQuotePiece.getActualVolumeMetric().toString();
                            } else if (lclQuotePiece.getBookedVolumeMetric() != null && lclQuotePiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclQuotePiece.getBookedVolumeMetric().toString();
                            }
                        } else {
                            commodityCodeMany = commodityCodeMany + lclQuotePiece.getCommName() + "(" + lclQuotePiece.getCommNo() + ")<br>";
                        }
                    }
                    if (lclQuotePiece.isHazmat()) {
                        hazmat = true;
                    }
                }//end of for loop
                request.setAttribute("commodityLbsKgs", commodityLbsKgs);
                request.setAttribute("commodityCftCbm", commodityCftCbm);
                request.setAttribute("commodityCodeOne", commodityCodeOne);
                request.setAttribute("commodityCodeMany", commodityCodeMany);
            }//end of lclBookingPiecesList if condition
            List locnam = lclratesdao.getDistinctOriginWithRates(rateType, pooorigin, ports.getEciportcode(), comnumArray);
            if (!comnumArray.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                List commcdeList = Arrays.asList(comnumArray.split(","));
                List noRatesCommodity = new GlMappingDAO().checkCommodityInOfrate(comnumArray);
                for (Object comm1 : commcdeList) {
                    sb.append(!noRatesCommodity.contains(comm1) ? comm1 + "," : "");
                }
                request.setAttribute("noRatesCommities", sb.toString());
            }
            Zipcode zipcode = null;
            LclWarehsDAO lclWarehsDAo = new LclWarehsDAO();
            UnLocationDAO unlocationdao = new UnLocationDAO();
            List<RoutingOptionsBean> routingOptionsList = new ArrayList<RoutingOptionsBean>();
            if (locnam != null && locnam.size() > 0) {
                if (CommonUtils.isNotEmpty(fromZip)) {
                    ZipCodeDAO zipcodedao = new ZipCodeDAO();
                    zipcode = zipcodedao.findByZip(fromZip);
                }

                List<RoutingOriginBean> routingOriginBeanList = new ArrayList<RoutingOriginBean>();
                String trmnumArray = new String();
                String trmnumArrayForDS = new String();
                for (int i = 0; i < locnam.size(); i++) {
                    Object[] locnamObject = (Object[]) locnam.get(i);
                    Double miles = 0.00;
                    if (zipcode != null && zipcode.getLat() != null && zipcode.getLng() != null && !zipcode.getLat().trim().equals("")
                            && !zipcode.getLng().trim().equals("")) {
                        miles = lclUtils.distFrom(Double.parseDouble(zipcode.getLat()), Double.parseDouble(zipcode.getLng()),
                                Double.parseDouble(locnamObject[4].toString()), Double.parseDouble(locnamObject[5].toString()));
                    }
                    RoutingOriginBean routingOriginBean = new RoutingOriginBean(locnamObject, miles);
                    routingOriginBeanList.add(routingOriginBean);
                    if (locnamObject != null && locnamObject[0] != null && !locnamObject[0].toString().trim().equals("")) {
                        routingOriginBean.setTrmnum(locnamObject[0].toString());
                        trmnumArray = trmnumArray + locnamObject[0].toString() + ",";
                    }
                    if (locnamObject != null && locnamObject[6] != null && !locnamObject[6].toString().trim().equals("")) {
                        routingOriginBean.setUnLocationcode(locnamObject[6].toString());
                    }
                    //end of locnamObject if condition
                }//end of for loop

                Collections.sort(routingOriginBeanList, new LclRoutingOriginComparator());
                if (routingOriginBeanList != null && routingOriginBeanList.size() > 0) {
                    int size = routingOriginBeanList.size();
                    if (size > 3) {
                        size = 3;
                    }
                    for (int i = 0; i < size; i++) {
                        RoutingOriginBean routingOriginBean = routingOriginBeanList.get(i);
                        sailDate = "";
                        RoutingOptionsBean routingoptionsbean = new RoutingOptionsBean();
                        routingoptionsbean.setOrigin(routingOriginBean.getPortname());
                        if (routingOriginBean.getStatecode() != null && !routingOriginBean.getStatecode().trim().equals("")) {
                            routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + "/" + routingOriginBean.getStatecode()
                                    + "(" + routingOriginBean.getUnLocationcode() + ")");
                        }
                        if (routingOriginBean.getMiles() != null && routingOriginBean.getMiles() > 0.00) {
                            routingoptionsbean.setMiles(" ("
                                    + NumberUtils.convertToTwoDecimal(routingOriginBean.getMiles()) + " miles)");
                        }
                        if (routingOriginBean.getTrmnum() != null && !routingOriginBean.getTrmnum().trim().equals("")
                                && lclQuoteMultiRateForm.getFd() != null && !lclQuoteMultiRateForm.getFd().trim().equals("")) {
                            UnLocation unlocation = null;
                            Integer pooId = routingOriginBean.getUnLocationId();
                            trmnumArrayForDS += pooId + ",";
                            Integer polId = 0;
                            Integer fdId = Integer.parseInt(lclQuoteMultiRateForm.getFd());
                            LclBookingPlanBean bookingPlanBean = lclbookingplandao.getRelay(pooId, fdId, "N");
                            if (bookingPlanBean != null) {
                                if (CommonUtils.isNotEmpty(bookingPlanBean.getPol_id())) {
                                    unlocation = unlocationdao.findById(bookingPlanBean.getPol_id());
                                    pol = unlocation.getUnLocationCode();
                                    polId = unlocation.getId();
                                    routingoptionsbean.setPortOfLoading(unlocation);
                                    if (bookingPlanBean.getPol_code() != null && !bookingPlanBean.getPol_code().trim().equals("")) {
                                        routingoptionsbean.setPolRelay(bookingPlanBean.getPol_code());
                                        routingoptionsbean.setPolRelayName(bookingPlanBean.getPol_name());
                                        if (unlocation.getStateId() != null && unlocation.getStateId().getCode() != null
                                                && !unlocation.getStateId().getCode().trim().equals("")) {
                                            routingoptionsbean.setPolRelayName(routingoptionsbean.getPolRelayName() + "/" + unlocation.getStateId().getCode());
                                        }//end of unlocation.getStateId() if condition
                                    }//end of bookingPlanBean.getPolCode() if condition
                                    if (bookingPlanBean.getPoo_code() != null && !bookingPlanBean.getPoo_code().trim().equals("")) {
                                        routingoptionsbean.setPooRelay(bookingPlanBean.getPoo_code());
                                        routingoptionsbean.setPooRelayName(bookingPlanBean.getPoo_name());
                                        unlocation = unlocationdao.findById(bookingPlanBean.getPoo_id());
                                        routingoptionsbean.setPortOfOrigin(unlocation);
                                        if (unlocation.getStateId() != null && unlocation.getStateId().getCode() != null
                                                && !unlocation.getStateId().getCode().trim().equals("")) {
                                            routingoptionsbean.setPooRelayName(routingoptionsbean.getPooRelayName() + "/" + unlocation.getStateId().getCode());
                                        }
                                    }//end of bookingPlanBean.getPooCode() if condition
                                    if (bookingPlanBean.getPod_code() != null && !bookingPlanBean.getPod_code().trim().equals("")) {
                                        routingoptionsbean.setPodRelay(bookingPlanBean.getPod_code());
                                        routingoptionsbean.setPodRelayName(bookingPlanBean.getPod_name());
                                    }//end of bookingPlanBean.getPodCode() if condition
                                    if (bookingPlanBean.getFd_code() != null && !bookingPlanBean.getFd_code().trim().equals("")) {
                                        routingoptionsbean.setFdRelay(bookingPlanBean.getFd_code());
                                        routingoptionsbean.setFdRelayName(bookingPlanBean.getFd_name());
                                        unlocation = unlocationdao.findById(bookingPlanBean.getFd_id());
                                        routingoptionsbean.setFinalDestination(unlocation);
                                        if (unlocation.getStateId() != null && unlocation.getStateId().getCode() != null
                                                && !unlocation.getStateId().getCode().trim().equals("")) {
                                            routingoptionsbean.setFdRelayName(routingoptionsbean.getFdRelayName() + "/" + unlocation.getStateId().getCode());
                                        }
                                    }//end of bookingPlanBean.getFdCode() if condition
                                } else {
                                    unlocation = unlocationdao.findById(pooId);
                                    pol = unlocation.getUnLocationCode();
                                    polId = unlocation.getId();
                                    routingoptionsbean.setPortOfOrigin(unlocation);
                                    routingoptionsbean.setPortOfLoading(unlocation);
                                }//end of bookingPlanBean.getPolId() if condition
                                if (CommonUtils.isNotEmpty(bookingPlanBean.getPod_id())) {
                                    unlocation = unlocationdao.findById(bookingPlanBean.getPod_id());
                                    routingoptionsbean.setPortOfDestination(unlocation);
                                    pod = unlocation.getUnLocationCode();
                                    if (unlocation.getStateId() != null && unlocation.getStateId().getCode() != null
                                            && !unlocation.getStateId().getCode().trim().equals("")) {
                                        routingoptionsbean.setPodRelayName(routingoptionsbean.getPodRelayName() + "/" + unlocation.getStateId().getCode());
                                    }

                                } else {
                                    unlocation = unlocationdao.findById(fdId);
                                    routingoptionsbean.setPortOfDestination(unlocation);
                                    routingoptionsbean.setFinalDestination(unlocation);
                                }//end of bookingPlanBean.getPodId() if condition
                            }//end of bookingPlanBean null condition
                            LclBookingVoyageBean voyageBean = null;
                            if (unlocation != null && unlocation.getId() != null && bookingPlanBean != null) {
                                voyageBean = lclbookingplandao.getSailingSchedule(pooId, polId, unlocation.getId(), fdId, "V", bookingPlanBean,cfcl);
                            }
                            if (voyageBean != null) {
                                if (voyageBean.getOriginLrd() != null) {
                                    routingoptionsbean.setNextLrd(DateUtils.formatDateToDDMMMYYYYHHMM(voyageBean.getOriginLrd()));
                                }
                                if (CommonUtils.isNotEmpty(voyageBean.getTtPooFd())) {
                                    routingoptionsbean.setTransitTime(String.valueOf(voyageBean.getTtPooFd()));
                                }
                                if (voyageBean.getSailingDate() != null) {
                                    sailDate = DateUtils.formatStringDateToAppFormatMMM(voyageBean.getSailingDate());
                                }
                            }//end of voyageBean if condition
                            if ("".equalsIgnoreCase(sailDate)) {
                                sailDate = DateUtils.formatStringDateToAppFormatMMM(new Date());
                            }
                            if (lclQuotePiecesList != null && lclQuotePiecesList.size() > 0) {
                                LclQuotationChargesCalculation lclQuoteChargesCalculation = new LclQuotationChargesCalculation();
                                lclQuoteChargesCalculation.calculateRates(routingOriginBean.getTrmnum(), lclQuoteMultiRateForm.getDestination(), pol, pod, fileNumberId, lclQuotePiecesList, null,
                                        "N", "N", null, rateType, "R", routingoptionsbean, sailDate, fromZip, session, null, null, null, routingOriginBean.getUnLocationcode(), null, request);
                                routingoptionsbean.setFromZip(fromZip);
                                routingoptionsbean.setSailDate(sailDate);
                            }
                        }//end of if condition routingOriginBean.getTrmnum()
                        routingoptionsbean.setRelayType("R");
                        routingOptionsList.add(routingoptionsbean);

                    }//end of size for loop
                }//end of routingOriginBeanList if condition
                trmnumArray = trmnumArray.substring(0, trmnumArray.length() - 1);
                List trmnumArrayList = lclratesdao.getUnLocationArrayByTrmnumArray(trmnumArray);
                if (trmnumArrayList != null && trmnumArrayList.size() > 0) {
                    String unLocationIdArray = new String();
                    for (int i = 0; i < locnam.size(); i++) {
                        Object unlocationObject = (Object) trmnumArrayList.get(i);
                        if (unlocationObject != null && !unlocationObject.toString().trim().equals("")) {
                            unLocationIdArray = unLocationIdArray + unlocationObject.toString() + ",";
                        }//end of unlocationObject if condition
                    }//end of for loop
                    unLocationIdArray = unLocationIdArray.substring(0, unLocationIdArray.length() - 1);
                    UnLocation fdUnloction = unlocationdao.getUnlocation(lclQuoteMultiRateForm.getDestination());
                    if (fdUnloction != null && fdUnloction.getId() != null && fdUnloction.getId() > 0) {
                        if (trmnumArrayForDS != null && !trmnumArrayForDS.trim().equals("")) {
                            trmnumArrayForDS = trmnumArrayForDS.substring(0, trmnumArrayForDS.length() - 1);
                        }
                        List directSailingList = lclratesdao.getDirectSailingList(fdUnloction.getId(), unLocationIdArray, trmnumArrayForDS);
                        if (directSailingList != null && directSailingList.size() > 0) {
                            Integer polId = 0, podId = 0, fdId = 0;
                            for (int i = 0; i < directSailingList.size(); i++) {
                                Object[] directSailingObject = (Object[]) directSailingList.get(i);
                                RoutingOptionsBean routingoptionsbean = new RoutingOptionsBean();
                                if (directSailingObject[3] != null && !directSailingObject[3].toString().trim().equals("")) {
                                    routingoptionsbean.setOrigin(directSailingObject[3].toString());
                                }
                                if (directSailingObject[4] != null && !directSailingObject[4].toString().trim().equals("")) {
                                    routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + "/" + directSailingObject[4].toString());
                                }
                                if (directSailingObject[0] != null && !directSailingObject[0].toString().trim().equals("")) {
                                    routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + "(" + directSailingObject[0].toString() + ")");
                                    UnLocation pooUnlocCode = unlocationdao.getUnlocation(directSailingObject[0].toString());
                                    routingoptionsbean.setPortOfOrigin(pooUnlocCode);
                                    if (directSailingObject[1] != null && !directSailingObject[0].toString().trim().equalsIgnoreCase(directSailingObject[1].toString())) {
                                        UnLocation polUnlocation = unlocationdao.getUnlocation(directSailingObject[1].toString());
                                        routingoptionsbean.setPortOfLoading(polUnlocation);
                                        pol = polUnlocation.getUnLocationCode();
                                        polId = polUnlocation.getId();
                                    } else {
                                        routingoptionsbean.setPortOfLoading(pooUnlocCode);
                                        pol = pooUnlocCode.getUnLocationCode();
                                        polId = pooUnlocCode.getId();
                                    }
                                }
                                if (directSailingObject[2] != null && !directSailingObject[2].toString().trim().equals("")) {
                                    UnLocation podUnlocation = unlocationdao.getUnlocation(directSailingObject[2].toString());
                                    routingoptionsbean.setPortOfDestination(podUnlocation);
                                    pod = podUnlocation.getUnLocationCode();
                                    routingoptionsbean.setRoutingType("DIRECT");
                                    podId = podUnlocation.getId();
                                    if (directSailingObject[7] != null && !directSailingObject[2].toString().trim().equalsIgnoreCase(directSailingObject[7].toString())) {
                                        routingoptionsbean.setRoutingType(routingoptionsbean.getRoutingType() + " TO " + podUnlocation.getUnLocationName().toUpperCase());
                                        UnLocation fdUnlocation = unlocationdao.getUnlocation(directSailingObject[7].toString());
                                        fdId = fdUnlocation.getId();
                                        routingoptionsbean.setFinalDestination(fdUnlocation);
                                    } else {
                                        fdId = podUnlocation.getId();
                                        routingoptionsbean.setFinalDestination(podUnlocation);
                                    }
                                }
                                if (directSailingObject[5] != null && !directSailingObject[5].toString().trim().equals("")) {
                                    routingoptionsbean.setTransitTime(directSailingObject[5].toString());
                                }
                                Double miles = 0.00;
                                if (zipcode != null && zipcode.getLat() != null && zipcode.getLng() != null && !zipcode.getLat().trim().equals("")
                                        && !zipcode.getLng().trim().equals("")) {
                                    miles = lclUtils.distFrom(Double.parseDouble(zipcode.getLat()), Double.parseDouble(zipcode.getLng()),
                                            Double.parseDouble(directSailingObject[8].toString()), Double.parseDouble(directSailingObject[9].toString()));
                                }
                                sailDate = "";
                                if (miles != null && miles > 0.00) {
                                    routingoptionsbean.setMiles(" ("
                                            + NumberUtils.convertToTwoDecimal(miles) + " miles)");
                                }
                                LclBookingPlanBean lclBookingPlanBean = lclbookingplandao.getRelay(routingoptionsbean.getPortOfOrigin().getId(),
                                        routingoptionsbean.getFinalDestination().getId(), "N");
                                LclBookingVoyageBean voyageBean = lclbookingplandao.getSailingSchedule(Integer.parseInt(directSailingObject[6].toString()),
                                        polId, podId, fdId, "V", lclBookingPlanBean,cfcl);
                                if (voyageBean != null) {
                                    if (voyageBean.getOriginLrd() != null) {
                                        routingoptionsbean.setNextLrd(DateUtils.formatDateToDDMMMYYYYHHMM(voyageBean.getOriginLrd()));
                                    }
                                    if (voyageBean.getSailingDate() != null) {
                                        sailDate = DateUtils.formatStringDateToAppFormatMMM(voyageBean.getSailingDate());
                                    }
                                }
                                if ("".equalsIgnoreCase(sailDate)) {
                                    sailDate = DateUtils.formatStringDateToAppFormatMMM(new Date());
                                }
                                refterminal = refterminaldao.getTerminalByUnLocation(directSailingObject[0].toString(), rateType);
                                if (refterminal != null && refterminal.getTrmnum() != null && !refterminal.getTrmnum().trim().equals("")) {
                                    pooorigin = refterminal.getTrmnum();
                                }
                                LclQuotationChargesCalculation lclQuoteChargesCalculation = new LclQuotationChargesCalculation();
                                if (lclQuotePiecesList != null && lclQuotePiecesList.size() > 0) {
                                    lclQuoteChargesCalculation.calculateRates(refterminal.getTrmnum(), lclQuoteMultiRateForm.getDestination(), pol, pod, fileNumberId, lclQuotePiecesList, null,
                                            "N", "N", null, rateType, "R", routingoptionsbean, sailDate, fromZip, session, null, null, null, refterminal.getUnLocationCode1(), null, request);
                                    routingoptionsbean.setFromZip(fromZip);
                                    routingoptionsbean.setSailDate(sailDate);
                                }
                                routingoptionsbean.setRelayType("D");
                                routingOptionsList.add(routingoptionsbean);
                            }//end of for loop
                        }//end of directSailingList if condition
                    }//end of unlocation if condition
                }//end of trmnumArrayList if condition
            }//end of locnam if condition
            lclSession.setRoutingOptionsList(routingOptionsList);
        }//end of ports if condition
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("hazmat", hazmat);
        request.setAttribute("fileNumberId", fileNumberId);
        request.setAttribute("rateType", rateTypeStr);
        request.setAttribute("pickupDisable", LoadLogisoftProperties.getProperty("application.enableCTS"));
        return mapping.findForward(LCL_MULTIRATES);
    }//end of method

    public ActionForward carrier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteMultiRateForm lclQuoteMultiRateForm = (LclQuoteMultiRateForm) form;
        if (request.getParameter("index") != null && !request.getParameter("index").trim().equals("")) {
            int index = Integer.parseInt(request.getParameter("index"));
            String moduleId = request.getParameter("moduleId");
            HttpSession session = request.getSession();
            String toZip = "", fromZip = "", sailDate = "";
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setXmlObjMap(null);
            lclSession.setCarrierList(null);
            session.setAttribute("lclSession", lclSession);
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            if (routingOptionsList != null && routingOptionsList.size() > 0) {
                Long fileNumberId = lclQuoteMultiRateForm.getFileNumberId();
                List<LclQuotePiece> lclQuotePiecesList = null;
                if (fileNumberId != null && fileNumberId > 0) {
                    lclQuotePiecesList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                } else {
                    if (lclSession != null && lclSession.getQuoteCommodityList() != null && lclSession.getQuoteCommodityList().size() > 0) {
                        lclQuotePiecesList = lclSession.getQuoteCommodityList();
                    }

                }
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(index);
                toZip = routingOptionsBean.getToZip();
                fromZip = routingOptionsBean.getFromZip();
                sailDate = routingOptionsBean.getSailDate();
                BigDecimal weight = new BigDecimal(0.000);
                BigDecimal measure = new BigDecimal(0.000);
                for (LclQuotePiece lbp : lclQuotePiecesList) {
                    if (lbp.getActualWeightImperial() != null) {
                        weight = weight.add(lbp.getActualWeightImperial());
                    } else if (lbp.getBookedWeightImperial() != null) {
                        weight = weight.add(lbp.getBookedWeightImperial());
                    }
                    if (lbp.getActualVolumeImperial() != null) {
                        measure = measure.add(lbp.getActualVolumeImperial());
                    } else if (lbp.getBookedVolumeImperial() != null) {
                        measure = measure.add(lbp.getBookedVolumeImperial());
                    }
                }
                String realPath = session.getServletContext().getRealPath("/xml/");
                String fileName = "ctsresponse" + session.getId() + ".xml";
                CallCTSWebServices ctsweb = new CallCTSWebServices();
                lclSession = ctsweb.processCTSWebService(lclSession, realPath, fileName, fromZip, toZip, sailDate,
                        "" + weight, "" + measure, "CARRIER_CHARGE", "CARRIER_COST", "Exports");
                session.setAttribute("lclSession", lclSession);
                request.setAttribute("fileNumberId", fileNumberId);
                request.setAttribute("index", index);
                request.setAttribute("moduleId", moduleId);
            }
        }
        return mapping.findForward(LCL_ROUTING_CARRIER);
    }

    public ActionForward setCarrier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<Carrier> carrierCostList = lclSession.getCarrierCostList();
        String pickUpCost = "";
        if (request.getParameter("index") != null && !request.getParameter("index").trim().equals("")) {
            String pickupCharge = request.getParameter("pickupCharge");
            String scac = request.getParameter("scac");
            //  String pickupCost = request.getParameter("pickupCost");
            int index = Integer.parseInt(request.getParameter("index"));
            if (!carrierCostList.isEmpty()) {
                for (int j = 0; j < carrierCostList.size(); j++) {
                    Carrier carrier = carrierCostList.get(j);
                    if (carrier != null && carrier.getScac() != null && carrier.getScac().trim().equalsIgnoreCase(scac)
                            && carrier.getFinalcharge() != null && !carrier.getFinalcharge().trim().equals("")) {
                        pickUpCost = carrier.getFinalcharge();
                        break;
                    }
                }
            }
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            if (routingOptionsList != null && routingOptionsList.size() > 0) {
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(index);
                routingOptionsBean.setCtsAmount("$" + pickupCharge);
                routingOptionsBean.setPickupCost(new BigDecimal(pickUpCost));
                routingOptionsBean.setScac("(" + scac + ")");
                routingOptionsList.set(index, routingOptionsBean);
            }
            lclSession.setRoutingOptionsList(routingOptionsList);
        }
        session.setAttribute("lclSession", lclSession);
        return mapping.findForward(LCL_MULTIRATES);
    }
}

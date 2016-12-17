/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.webservices.CallCTSWebServices;
import com.gp.cong.logisoft.beans.RoutingOptionsBean;
import com.gp.cong.logisoft.beans.RoutingOriginBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.lcl.comparator.LclRoutingOriginComparator;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclMultiRateForm;
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
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Owner
 */
public class LclMultiRatesAction extends LogiwareDispatchAction {

    private static String LCL_MULTIRATES = "lclmultirates";
    private static String LCL_ROUTING_CARRIER = "lclroutingcarrier";
    private LclUtils lclUtils = new LclUtils();

    public ActionForward displayRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclMultiRateForm lclMultiRateForm = (LclMultiRateForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        Long fileNumberId = lclMultiRateForm.getFileNumberId();
        String fromZip = "", commodityCode = "", rateTypeStr = "";
        String origin = lclMultiRateForm.getOrigin();
        String destination = lclMultiRateForm.getDestination();
        String sailDate = new String();
        String pol = new String();
        String pod = new String();
        boolean hazmat = false;
        String cfcl = (null!=lclMultiRateForm.getCfcl()) ? "C" :"E" ;
        if (lclMultiRateForm.getZip() != null && !lclMultiRateForm.getZip().trim().equals("")) {
            String[] zip = lclMultiRateForm.getZip().split("-");
            fromZip = zip[0];
            request.setAttribute("zip", fromZip);
            request.setAttribute("doorOrigin", zip[1]);
        }
        if (lclMultiRateForm.getDestinationValue() != null && !lclMultiRateForm.getDestinationValue().trim().equals("")) {
            request.setAttribute("destination", lclMultiRateForm.getDestinationValue());
        }
        List lclBookingPiecesList = null;
        if (fileNumberId != null && fileNumberId > 0) {
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
        } else if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
            lclBookingPiecesList = lclSession.getCommodityList();
        }
        String rateType = lclMultiRateForm.getRateType();
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
            if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                String commodityCodeOne = new String();
                String commodityCodeMany = new String();
                String commodityCftCbm = new String();
                String commodityLbsKgs = new String();
                if (lclBookingPiecesList.size() > 1) {
                    request.setAttribute("showPlus", "Y");
                }
                for (int i = 0; i < lclBookingPiecesList.size(); i++) {
                    LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(i);
                    if (fileNumberId != null && fileNumberId > 0) {
                        comnumArray = comnumArray + lclBookingPiece.getCommodityType().getCode() + ",";
                        if (i == 0) {
                            commodityCodeOne = lclBookingPiece.getCommodityType().getDescEn() + "(" + lclBookingPiece.getCommodityType().getCode() + ")";
                            commodityCodeMany = lclBookingPiece.getCommodityType().getDescEn() + "(" + lclBookingPiece.getCommodityType().getCode() + ")<br>";
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclBookingPiece.getActualWeightImperial().toString();
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclBookingPiece.getBookedWeightImperial().toString();
                            }
                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclBookingPiece.getActualVolumeImperial().toString();
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclBookingPiece.getBookedVolumeImperial().toString();
                            }
                            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclBookingPiece.getActualWeightMetric().toString();
                            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclBookingPiece.getBookedWeightMetric().toString();
                            }
                            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclBookingPiece.getActualVolumeMetric().toString();
                            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclBookingPiece.getBookedVolumeMetric().toString();
                            }
                        } else {
                            commodityCodeMany = commodityCodeMany + lclBookingPiece.getCommodityType().getDescEn() + "(" + lclBookingPiece.getCommodityType().getCode() + ")<br>";
                        }
                    } else {
                        comnumArray = comnumArray + lclBookingPiece.getCommNo() + ",";
                        if (i == 0) {
                            commodityCodeOne = lclBookingPiece.getCommName() + "(" + lclBookingPiece.getCommNo() + ")";
                            commodityCodeMany = lclBookingPiece.getCommName() + "(" + lclBookingPiece.getCommNo() + ")<br>";
                            if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclBookingPiece.getActualWeightImperial().toString();
                            } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                                commodityLbsKgs = lclBookingPiece.getBookedWeightImperial().toString();
                            }
                            if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclBookingPiece.getActualVolumeImperial().toString();
                            } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                                commodityCftCbm = lclBookingPiece.getBookedVolumeImperial().toString();
                            }
                            if (lclBookingPiece.getActualWeightMetric() != null && lclBookingPiece.getActualWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclBookingPiece.getActualWeightMetric().toString();
                            } else if (lclBookingPiece.getBookedWeightMetric() != null && lclBookingPiece.getBookedWeightMetric().doubleValue() != 0.00) {
                                commodityLbsKgs = commodityLbsKgs + "/" + lclBookingPiece.getBookedWeightMetric().toString();
                            }
                            if (lclBookingPiece.getActualVolumeMetric() != null && lclBookingPiece.getActualVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclBookingPiece.getActualVolumeMetric().toString();
                            } else if (lclBookingPiece.getBookedVolumeMetric() != null && lclBookingPiece.getBookedVolumeMetric().doubleValue() != 0.00) {
                                commodityCftCbm = commodityCftCbm + "/" + lclBookingPiece.getBookedVolumeMetric().toString();
                            }
                        } else {
                            commodityCodeMany = commodityCodeMany + lclBookingPiece.getCommName() + "(" + lclBookingPiece.getCommNo() + ")<br>";
                        }
                    }
                    if (lclBookingPiece.isHazmat()) {
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
            UnLocationDAO unlocationdao = new UnLocationDAO();
            List<RoutingOptionsBean> routingOptionsList = new ArrayList<RoutingOptionsBean>();
            if (locnam != null && locnam.size() > 0) {
                if (fromZip != null && !fromZip.trim().equals("")) {
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
                        RoutingOptionsBean routingoptionsbean = new RoutingOptionsBean();
                        routingoptionsbean.setOrigin(routingOriginBean.getPortname());
                        if (routingOriginBean.getStatecode() != null && !routingOriginBean.getStatecode().trim().equals("")) {
                            routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + "/" + routingOriginBean.getStatecode());
                        }
                        if (routingOriginBean.getMiles() != null && routingOriginBean.getMiles() > 0.00) {
                            routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + " ("
                                    + NumberUtils.convertToTwoDecimal(routingOriginBean.getMiles()) + " miles)");
                        }
                        if (routingOriginBean.getTrmnum() != null && !routingOriginBean.getTrmnum().trim().equals("")
                                && lclMultiRateForm.getFd() != null && !lclMultiRateForm.getFd().trim().equals("")) {
                            LclBookingPlanDAO lclbookingplandao = new LclBookingPlanDAO();
                            UnLocation unlocation = null;
                            Integer pooId = routingOriginBean.getUnLocationId();
                            trmnumArrayForDS += pooId + ",";
                            Integer polId = 0;
                            Integer fdId = Integer.parseInt(lclMultiRateForm.getFd());
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
                            } //end of bookingPlanBean null condition
                            sailDate = "";
                            LclBookingVoyageBean voyageBean = null;
                            if (unlocation != null && unlocation.getId() != null && null != bookingPlanBean) {
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
                            System.out.println("sailDate=="+sailDate);
                            if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                                lclChargesCalculation.calculateRates(routingOriginBean.getTrmnum(), lclMultiRateForm.getDestination(), pol, pod, fileNumberId, lclBookingPiecesList, null,
                                        "N", "N", null, rateType, "R", routingoptionsbean, sailDate, fromZip, session, null, null, null,
                                        routingOriginBean.getUnLocationcode(), null, request, lclMultiRateForm.getBillToParty());
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
                    UnLocation unlocation = unlocationdao.getUnlocation(lclMultiRateForm.getDestination());
                    if (unlocation != null && unlocation.getId() != null && unlocation.getId() > 0) {
                        if (trmnumArrayForDS != null && !trmnumArrayForDS.trim().equals("")) {
                            trmnumArrayForDS = trmnumArrayForDS.substring(0, trmnumArrayForDS.length() - 1);
                        }
                        List directSailingList = lclratesdao.getDirectSailingList(unlocation.getId(), unLocationIdArray, trmnumArrayForDS);
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
                                    unlocation = unlocationdao.getUnlocation(directSailingObject[0].toString());
                                    routingoptionsbean.setPortOfOrigin(unlocation);
                                    if (directSailingObject[1] != null && !directSailingObject[0].toString().trim().equalsIgnoreCase(directSailingObject[1].toString())) {
                                        unlocation = unlocationdao.getUnlocation(directSailingObject[1].toString());
                                    }
                                    routingoptionsbean.setPortOfLoading(unlocation);
                                    pol = unlocation.getUnLocationCode();
                                    polId = unlocation.getId();
                                }
                                if (directSailingObject[2] != null && !directSailingObject[2].toString().trim().equals("")) {
                                    unlocation = unlocationdao.getUnlocation(directSailingObject[2].toString());
                                    routingoptionsbean.setPortOfDestination(unlocation);
                                    pod = unlocation.getUnLocationCode();
                                    routingoptionsbean.setRoutingType("DIRECT");
                                    podId = unlocation.getId();
                                    if (directSailingObject[7] != null && !directSailingObject[2].toString().trim().equalsIgnoreCase(directSailingObject[7].toString())) {
                                        routingoptionsbean.setRoutingType(routingoptionsbean.getRoutingType() + " TO " + unlocation.getUnLocationName().toUpperCase());
                                        unlocation = unlocationdao.getUnlocation(directSailingObject[7].toString());
                                    }
                                    fdId = unlocation.getId();
                                    routingoptionsbean.setFinalDestination(unlocation);
                                }
                                if (directSailingObject[5] != null && !directSailingObject[5].toString().trim().equals("")) {
                                    routingoptionsbean.setTransitTime(directSailingObject[5].toString());
                                }
                                sailDate = "";
                                Double miles = 0.00;
                                if (zipcode != null && zipcode.getLat() != null && zipcode.getLng() != null && !zipcode.getLat().trim().equals("")
                                        && !zipcode.getLng().trim().equals("")) {
                                    miles = lclUtils.distFrom(Double.parseDouble(zipcode.getLat()), Double.parseDouble(zipcode.getLng()),
                                            Double.parseDouble(directSailingObject[8].toString()), Double.parseDouble(directSailingObject[9].toString()));
                                }
                                if (miles != null && miles > 0.00) {
                                    routingoptionsbean.setOrigin(routingoptionsbean.getOrigin() + " ("
                                            + NumberUtils.convertToTwoDecimal(miles) + " miles)");
                                }
                                LclBookingPlanDAO lclbookingplandao = new LclBookingPlanDAO();
                                LclBookingPlanBean lclBookingPlanBean = lclbookingplandao.getRelay(polId, fdId, "N");
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
                                LclChargesCalculation lclChargesCalculation = new LclChargesCalculation();
                                if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
                                    lclChargesCalculation.calculateRates(refterminal.getTrmnum(), lclMultiRateForm.getDestination(), pol, pod, fileNumberId, lclBookingPiecesList, null,
                                            "N", "N", null, rateType, "R", routingoptionsbean, sailDate, fromZip, session, null,
                                            null, null, refterminal.getUnLocationCode1(), null, request, lclMultiRateForm.getBillToParty());
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
        LclMultiRateForm lclMultiRateForm = (LclMultiRateForm) form;
        if (request.getParameter("index") != null && !request.getParameter("index").trim().equals("")) {
            int index = Integer.parseInt(request.getParameter("index"));
            HttpSession session = request.getSession();
            String toZip = "", fromZip = "", sailDate = "";
            LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setXmlObjMap(null);
            lclSession.setCarrierList(null);
            session.setAttribute("lclSession", lclSession);
            List<RoutingOptionsBean> routingOptionsList = lclSession.getRoutingOptionsList();
            if (routingOptionsList != null && routingOptionsList.size() > 0) {
                Long fileNumberId = lclMultiRateForm.getFileNumberId();
                List<LclBookingPiece> lclBookingPiecesList = null;
                if (fileNumberId != null && fileNumberId > 0) {
                    lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", fileNumberId);
                } else {
                    if (lclSession != null && lclSession.getCommodityList() != null && lclSession.getCommodityList().size() > 0) {
                        lclBookingPiecesList = lclSession.getCommodityList();
                    }

                }
                RoutingOptionsBean routingOptionsBean = routingOptionsList.get(index);
                toZip = routingOptionsBean.getToZip();
                fromZip = routingOptionsBean.getFromZip();
                sailDate = routingOptionsBean.getSailDate();
                BigDecimal weight = new BigDecimal(0.000);
                BigDecimal measure = new BigDecimal(0.000);
                for (LclBookingPiece lbp : lclBookingPiecesList) {
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
            }
        }
        return mapping.findForward(LCL_ROUTING_CARRIER);
    }
}

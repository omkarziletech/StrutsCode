/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.EculineEdiBean;
import com.gp.cong.logisoft.beans.EculineEdiBlBean;
import com.gp.cong.logisoft.beans.EculineEdiCargoDetailsBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import com.gp.cong.logisoft.domain.lcl.CargoDetails;
import com.gp.cong.logisoft.domain.lcl.ContainerInfo;
import com.gp.cong.logisoft.domain.lcl.EculineEdi;
import com.gp.cong.logisoft.domain.lcl.EculinePackageTypeMapping;
import com.gp.cong.logisoft.domain.lcl.EculineTradingPartnerMapping;
import com.gp.cong.logisoft.domain.lcl.EculineUnLocationMapping;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.ContainerInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineEdiDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculinePackageTypeMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineTradingPartnerMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineUnLocationMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiBlInfoForm;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rajesh
 */
public class BolUtils implements LclCommonConstant {

    private static final Integer VESSEL_CODE = 14;

    public void bolDetails(HttpServletRequest request, String id) throws Exception {
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        List<EculineEdiCargoDetailsBean> bolDetails = eculineEdiDao.getBolDetails(id);
        EculineEdiCargoDetailsBean ecdbn = new EculineEdiCargoDetailsBean();
        if (!bolDetails.isEmpty()) {
            ecdbn = bolDetails.get(0);
        }
        //Shipper
        if (CommonUtils.isNotEmpty(ecdbn.getShipperNad())) {
            if (CommonUtils.isNotEmpty(ecdbn.getEmShipperCode())) {
                request.setAttribute("isShipperMapped", "success-indicator");
            } else if (CommonUtils.isEmpty(ecdbn.getEmShipperCode()) && CommonUtils.isEmpty(ecdbn.getShipperCode())) {
                request.setAttribute("isShipperMapped", "error-indicator");
            }
        }
        //Consignee
        if (CommonUtils.isNotEmpty(ecdbn.getConsNad())) {
            if (CommonUtils.isNotEmpty(ecdbn.getEmConsCode())) {
                request.setAttribute("isConsMapped", "success-indicator");
            } else if (CommonUtils.isEmpty(ecdbn.getEmConsCode()) && CommonUtils.isEmpty(ecdbn.getConsCode())) {
                request.setAttribute("isConsMapped", "error-indicator");
            }
        }
        //Notify1
        if (CommonUtils.isNotEmpty(ecdbn.getNotify1Nad())) {
            if (CommonUtils.isNotEmpty(ecdbn.getEmNotify1Code())) {
                request.setAttribute("isNotify1Mapped", "success-indicator");
            } else if (CommonUtils.isEmpty(ecdbn.getEmNotify1Code()) && CommonUtils.isEmpty(ecdbn.getNotify1Code())) {
                request.setAttribute("isNotify1Mapped", "error-indicator");
            }
        }
        //Notify2
        if (CommonUtils.isNotEmpty(ecdbn.getNotify2Nad())) {
            if (CommonUtils.isNotEmpty(ecdbn.getEmNotify2Code())) {
                request.setAttribute("isNotify2Mapped", "success-indicator");
            } else if (CommonUtils.isEmpty(ecdbn.getEmNotify2Code()) && CommonUtils.isEmpty(ecdbn.getNotify2Code())) {
                request.setAttribute("isNotify2Mapped", "error-indicator");
            }
        }
        request.setAttribute("bl", bolDetails);
    }

    public void voyageDetails(HttpServletRequest request, String id) throws Exception {
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        GenericCodeDAO genericcodeDao = new GenericCodeDAO();
        Map<String, Object> filters = new HashMap<String, Object>();
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", id);
        List<EculineEdiBlBean> blInfoList = eculineEdiDao.getBillOflading(values);
        EculineEdiBlBean.setBlIds(eculineEdiDao.validateBl());
        filters.put("edi.`id`", id);
        List<EculineEdiBean> eculineEdiList = eculineEdiDao.search(filters, 1);
        EculineEdiBean eculineEdiBean = new EculineEdiBean();
        if (!eculineEdiList.isEmpty()) {
            eculineEdiBean = eculineEdiList.get(0);
        }
        request.setAttribute("blInfoList", blInfoList);
        // Validation
        GenericCode gc = genericcodeDao.getGenericCode(eculineEdiBean.getVesselName(), VESSEL_CODE);
        TradingPartner tp = tpDao.getTpBySsl(eculineEdiBean.getSslineNo());
        if (tp == null && null != eculineEdiBean.getSslineNo()) {
            tp = tpDao.findById(eculineEdiBean.getSslineNo());
        }
        if (gc == null) {
            request.setAttribute("vessel", "error-indicator");
        }
        if (tp == null) {
            request.setAttribute("sslineNo", "error-indicator");
        }
        request.setAttribute("ecu", eculineEdiBean);
    }

    public void updateBol(LclEculineEdiBlInfoForm blInfoForm, HttpServletRequest request, User thisUser) throws Exception {
        String id = blInfoForm.getId();
        String shipper, cons, notify1, notify2;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        List<CargoDetails> cargoDetailsCollection = new ArrayList<CargoDetails>();
        BlInfo bl = eculineEdiDao.getBol(id);
        // Parties
        shipper = blInfoForm.isChkManualSh() == true
                ? ((CommonUtils.isNotEmpty(blInfoForm.getManualShipper()) && blInfoForm.getManualShipper().length() > 20)
                        ? blInfoForm.getManualShipper().substring(0, 20) : blInfoForm.getManualShipper()) : blInfoForm.getShipperCode();
        cons = blInfoForm.isChkManualCo() == true
                ? ((CommonUtils.isNotEmpty(blInfoForm.getManualCons()) && blInfoForm.getManualCons().length() > 20)
                        ? blInfoForm.getManualCons().substring(0, 20) : blInfoForm.getManualCons()) : blInfoForm.getConsCode();
        notify1 = blInfoForm.isChkManualNo1() == true
                ? ((CommonUtils.isNotEmpty(blInfoForm.getManualNotify1()) && blInfoForm.getManualNotify1().length() > 20)
                        ? blInfoForm.getManualNotify1().substring(0, 20) : blInfoForm.getManualNotify1()) : blInfoForm.getNotify1Code();
        notify2 = blInfoForm.isChkManualNo2() == true
                ? CommonUtils.isNotEmpty(blInfoForm.getManualNotify2()) && blInfoForm.getManualNotify2().length() > 20
                        ? blInfoForm.getManualNotify2().substring(0, 20) : blInfoForm.getManualNotify2() : blInfoForm.getNotify2Code();
        // Shipper
        bl.setShipperCode(shipper);
        bl.setShipperNad(CommonUtils.isNotEmpty(blInfoForm.getShipperNad()) && blInfoForm.getShipperNad().length() > 250 ? blInfoForm.getShipperNad().substring(0, 250) : blInfoForm.getShipperNad());
        bl.setManualShipper(blInfoForm.isChkManualSh());
        // Consignee
        bl.setConsCode(cons);
        bl.setConsNad(CommonUtils.isNotEmpty(blInfoForm.getConsNad()) && blInfoForm.getConsNad().length() > 250 ? blInfoForm.getConsNad().substring(0, 250) : blInfoForm.getConsNad());
        bl.setManualCons(blInfoForm.isChkManualCo());
        // Notify1
        bl.setNotify1Code(notify1);
        bl.setNotify1Nad(CommonUtils.isNotEmpty(blInfoForm.getNotify1Nad()) && blInfoForm.getNotify1Nad().length() > 250 ? blInfoForm.getNotify1Nad().substring(0, 250) : blInfoForm.getNotify1Nad());
        bl.setManualNotify1(blInfoForm.isChkManualNo1());
        // Notify2
        bl.setNotify2Code(notify2);
        bl.setNotify2Nad(CommonUtils.isNotEmpty(blInfoForm.getNotify2Nad()) && blInfoForm.getNotify2Nad().length() > 250 ? blInfoForm.getNotify2Nad().substring(0, 250) : blInfoForm.getNotify2Nad());
        bl.setManualNotify2(blInfoForm.isChkManualNo2());
        // POL
        bl.setPolUncode(blInfoForm.getPolUncode());
        bl.setPolDesc(CommonUtils.isNotEmpty(blInfoForm.getPolDesc()) && blInfoForm.getPolDesc().length() > 50 ? blInfoForm.getPolDesc().substring(0, 50) : blInfoForm.getPolDesc());
        // POD
        bl.setPodUncode(blInfoForm.getPodUncode());
        bl.setPodDesc(CommonUtils.isNotEmpty(blInfoForm.getPodDesc()) && blInfoForm.getPodDesc().length() > 50 ? blInfoForm.getPodDesc().substring(0, 50) : blInfoForm.getPodDesc());
        // Others
        bl.setPoddeliveryUncode(CommonUtils.isNotEmpty(blInfoForm.getPoddeliveryUncode()) && blInfoForm.getPoddeliveryUncode().length() > 100 ? blInfoForm.getPoddeliveryUncode().substring(0, 100) : blInfoForm.getPoddeliveryUncode());
        bl.setPoddeliveryDesc(CommonUtils.isNotEmpty(blInfoForm.getPoddeliveryDesc()) && blInfoForm.getPoddeliveryDesc().length() > 100 ? blInfoForm.getPoddeliveryDesc().substring(0, 100) : blInfoForm.getPoddeliveryDesc());
        String preCarriageBy = CommonUtils.isNotEmpty(blInfoForm.getPrecarriageBy()) ? blInfoForm.getPrecarriageBy().toUpperCase() : "";
        bl.setPrecarriageBy(preCarriageBy.length() > 50 ? preCarriageBy.substring(0, 50) : preCarriageBy);
        bl.setPorUncode(blInfoForm.getPorUncode());
        bl.setPlaceOfReceipt(CommonUtils.isNotEmpty(blInfoForm.getPlaceOfReceipt()) && blInfoForm.getPlaceOfReceipt().length() > 100 ? blInfoForm.getPlaceOfReceipt().substring(0, 100) : blInfoForm.getPlaceOfReceipt());
        bl.setUpdatedByUser(thisUser);
        bl.setUpdatedDate(new Date());
        // Container details
        int i = 0;
        EculinePackageTypeMappingDAO eculinePackageTypeMappingDAO = new EculinePackageTypeMappingDAO();
        PackageTypeDAO packageTypeDAO = new PackageTypeDAO();
        for (CargoDetails cargo : bl.getCargoDetailsCollection()) {
            cargo.setPackageAmount(Integer.parseInt(blInfoForm.getPackageAmount()[i]));
            //*******************Automatically Mapping of Package Description***
            PackageType packageType = packageTypeDAO.findPackage(blInfoForm.getPackageDesc()[i]);
            if (packageType != null && !"".equals(blInfoForm.getEcuPackageDesc()[i])) {
                EculinePackageTypeMapping eculinePackageTypeMapping = eculinePackageTypeMappingDAO.getByProperty("packageDesc", blInfoForm.getEcuPackageDesc()[i]);
                if (eculinePackageTypeMapping == null) {
                    eculinePackageTypeMapping = new EculinePackageTypeMapping();
                    eculinePackageTypeMapping.setPackageDesc(blInfoForm.getEcuPackageDesc()[i]);
                    eculinePackageTypeMapping.setPackageType(packageType);
                    eculinePackageTypeMappingDAO.save(eculinePackageTypeMapping);
                }
            }
            //******************************************************************
            cargo.setPackageDesc(blInfoForm.getPackageDesc()[i]);
            cargo.setGoodDesc(blInfoForm.getGoodDesc()[i]);
            cargo.setMarksNoDesc(blInfoForm.getMarksNo()[i]);
            cargo.setDischargeInstruction(blInfoForm.getDischargeInstruction()[i]);
            cargo.setWeightValues(new BigDecimal(blInfoForm.getWeightValues()[i]));
            cargo.setVolumeValues(new BigDecimal(blInfoForm.getVolumeValues()[i]));
            if (CommonUtils.isNotEmpty(blInfoForm.getCommercialValue()[i])) {
                cargo.setCommercialValue(Integer.parseInt(blInfoForm.getCommercialValue()[i]));
            }
            cargo.setCurrency(blInfoForm.getCurrency()[i]);
            cargo.setUpdatedByUser(thisUser);
            cargo.setUpdatedDate(new Date());
            cargoDetailsCollection.add(cargo);
            i++;
        }
        bl.setCargoDetailsCollection(cargoDetailsCollection);
        eculineEdiDao.getCurrentSession().clear();
        eculineEdiDao.update(bl);
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("blInfoId", bl.getId());
        eculineEdiDao.adjudicate("AdjudicateBlInfoById", values);
        values.clear();
        values.put("eculineEdiId", bl.getContainerInfo().getEculineEdi().getId());
        eculineEdiDao.adjudicate("AdjudicateEculineEdiById", values);

        request.setAttribute("hblNo", bl.getBlNo());
        //************MAPPING OF ADDRESSES OF SHIPPER,CONSIGNEE & NOTIFY********
        mappingAddress(blInfoForm);
        //************MAPPING OF UN Location code OF POL,POD & Delivery ********
        mappingUnLocationDesc(blInfoForm);
    }

    public void mappingUnLocationDesc(LclEculineEdiBlInfoForm blInfoForm) throws Exception {
        EculineUnLocationMappingDAO eculineUnLocationMappingDAO = new EculineUnLocationMappingDAO();
        EculineUnLocationMapping eculineUnLocationMapping = null;
        //*******************Automatically Mapping of POL unLocation Code Description
        eculineUnLocationMapping = eculineUnLocationMappingDAO.getByProperty("unLocationDesc", blInfoForm.getEcuPOLDesc());
        if (CommonUtils.isEmpty(blInfoForm.getEcuPolUncode())) {
            if (eculineUnLocationMapping == null && CommonUtils.isNotEmpty(blInfoForm.getPolUncode()) && CommonUtils.isNotEmpty(blInfoForm.getEcuPOLDesc())) {
                EculineUnLocationMapping eculineUnLocationMappingPOL = new EculineUnLocationMapping();
                eculineUnLocationMappingPOL.setUnLocationDesc(blInfoForm.getEcuPOLDesc());
                eculineUnLocationMappingPOL.setUnLocationCode(blInfoForm.getPolUncode());
                eculineUnLocationMappingDAO.save(eculineUnLocationMappingPOL);
            }
        }
        //*******************Automatically Mapping of POD unLocation Code Description
        eculineUnLocationMapping = eculineUnLocationMappingDAO.getByProperty("unLocationDesc", blInfoForm.getEcuPODDesc());
        if (CommonUtils.isEmpty(blInfoForm.getEcuPodUncode())) {
            if (eculineUnLocationMapping == null && CommonUtils.isNotEmpty(blInfoForm.getPodUncode()) && CommonUtils.isNotEmpty(blInfoForm.getEcuPODDesc())) {
                EculineUnLocationMapping eculineUnLocationMappingPOD = new EculineUnLocationMapping();
                eculineUnLocationMappingPOD.setUnLocationDesc(blInfoForm.getEcuPODDesc());
                eculineUnLocationMappingPOD.setUnLocationCode(blInfoForm.getPodUncode());
                eculineUnLocationMappingDAO.save(eculineUnLocationMappingPOD);
            }
        }

        //*******************Automatically Mapping of Delivery unLocation Code Description
        eculineUnLocationMapping = eculineUnLocationMappingDAO.getByProperty("unLocationDesc", blInfoForm.getEcuDeliverDesc());
        if (CommonUtils.isEmpty(blInfoForm.getEcuPoddeliveryUncode())) {
            if (eculineUnLocationMapping == null && CommonUtils.isNotEmpty(blInfoForm.getPoddeliveryUncode()) && CommonUtils.isNotEmpty(blInfoForm.getEcuDeliverDesc())) {
                EculineUnLocationMapping eculineUnLocationMappingDelivery = new EculineUnLocationMapping();
                eculineUnLocationMappingDelivery.setUnLocationDesc(blInfoForm.getEcuDeliverDesc());
                eculineUnLocationMappingDelivery.setUnLocationCode(blInfoForm.getPoddeliveryUncode());
                eculineUnLocationMappingDAO.save(eculineUnLocationMappingDelivery);
            }
        }
    }

    public void mappingAddress(LclEculineEdiBlInfoForm blInfoForm) throws Exception {
        String shipper, cons, notify1, notify2;
        EculineTradingPartnerMappingDAO eculineTradingPartnerMappingDAO = new EculineTradingPartnerMappingDAO();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        shipper = blInfoForm.isChkManualSh() == true ? blInfoForm.getManualShipper() : blInfoForm.getShipperCode();
        cons = blInfoForm.isChkManualCo() == true ? blInfoForm.getManualCons() : blInfoForm.getConsCode();
        notify1 = blInfoForm.isChkManualNo1() == true ? blInfoForm.getManualNotify1() : blInfoForm.getNotify1Code();
        notify2 = blInfoForm.isChkManualNo2() == true ? blInfoForm.getManualNotify2() : blInfoForm.getNotify2Code();
        //*******************Automatically Mapping of Shipper Address Description
        if (CommonUtils.isEmpty(blInfoForm.getEcuShipCode())) {
            if (blInfoForm.isChkManualSh() == false) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(shipper);
                EculineTradingPartnerMapping eculineTradingPartnerMapping = eculineTradingPartnerMappingDAO.getByProperty("address", blInfoForm.getEcuShipperAdd());
                if (eculineTradingPartnerMapping == null && tradingPartner != null && shipper != "" && blInfoForm.getEcuShipperAdd() != "") {
                    EculineTradingPartnerMapping eculineTradingPartnerMappingShip = new EculineTradingPartnerMapping();
                    eculineTradingPartnerMappingShip.setAddress(blInfoForm.getEcuShipperAdd());
                    eculineTradingPartnerMappingShip.setTradingPartner(tradingPartner);
                    eculineTradingPartnerMappingDAO.save(eculineTradingPartnerMappingShip);
                }
            }
        }
        //*******************Automatically Mapping of CONSIGNEE Address Description
        if (CommonUtils.isEmpty(blInfoForm.getEcuConCode())) {
            if (blInfoForm.isChkManualCo() == false) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(cons);
                EculineTradingPartnerMapping eculineTradingPartnerMapping = eculineTradingPartnerMappingDAO.getByProperty("address", blInfoForm.getEcuConsigneeAdd());
                if (eculineTradingPartnerMapping == null && tradingPartner != null && cons != "" && blInfoForm.getEcuConsigneeAdd() != "") {
                    EculineTradingPartnerMapping eculineTradingPartnerMappingCon = new EculineTradingPartnerMapping();
                    eculineTradingPartnerMappingCon.setAddress(blInfoForm.getEcuConsigneeAdd());
                    eculineTradingPartnerMappingCon.setTradingPartner(tradingPartner);
                    eculineTradingPartnerMappingDAO.save(eculineTradingPartnerMappingCon);
                }
            }
        }
        /*
        //*******************Automatically Mapping of NOTIFY Address Description
        if (CommonUtils.isEmpty(blInfoForm.getEcuNotify1Code())) {
            if (blInfoForm.isChkManualNo1() == false) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(notify1);
                EculineTradingPartnerMapping eculineTradingPartnerMapping = eculineTradingPartnerMappingDAO.getByProperty("address", blInfoForm.getEcuNotify1Add());
                if (eculineTradingPartnerMapping == null && tradingPartner != null && notify1 != "" && blInfoForm.getEcuNotify1Add() != "") {
                    EculineTradingPartnerMapping eculineTradingPartnerMappingNot1 = new EculineTradingPartnerMapping();
                    eculineTradingPartnerMappingNot1.setAddress(blInfoForm.getEcuNotify1Add());
                    eculineTradingPartnerMappingNot1.setTradingPartner(tradingPartner);
                    eculineTradingPartnerMappingDAO.save(eculineTradingPartnerMappingNot1);
                }
            }
        }
        //*******************Automatically Mapping of NOTIFY2 Address Description
        if (CommonUtils.isEmpty(blInfoForm.getEcuNotify2Code())) {
            if (blInfoForm.isChkManualNo2() == false) {
                TradingPartner tradingPartner = tradingPartnerDAO.findById(notify2);
                EculineTradingPartnerMapping eculineTradingPartnerMapping = eculineTradingPartnerMappingDAO.getByProperty("address", blInfoForm.getEcuNotify2Add());
                if (eculineTradingPartnerMapping == null && tradingPartner != null && notify2 != "" && blInfoForm.getEcuNotify2Add() != "") {
                    EculineTradingPartnerMapping eculineTradingPartnerMappingNot2 = new EculineTradingPartnerMapping();
                    eculineTradingPartnerMappingNot2.setAddress(blInfoForm.getEcuNotify2Add());
                    eculineTradingPartnerMappingNot2.setTradingPartner(tradingPartner);
                    eculineTradingPartnerMappingDAO.save(eculineTradingPartnerMappingNot2);
                }
            }
        }
        
        */
    }

    public void voyageDetails(HttpServletRequest request, Map<String, String> values) throws Exception {
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        GenericCodeDAO genericcodeDao = new GenericCodeDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        Map<String, Object> filters = new HashMap<String, Object>();
        List<EculineEdiBlBean> blInfoList = eculineEdiDao.getBillOflading(values);
        EculineEdiBlBean.setBlIds(eculineEdiDao.validateBl());
        filters.put("edi.`id`", values.get("id"));
        List<EculineEdiBean> eculineEdiList = eculineEdiDao.search(filters, 1);
        EculineEdiBean eculineEdiBean = new EculineEdiBean();
        if (!eculineEdiList.isEmpty()) {
            eculineEdiBean = eculineEdiList.get(0);
        }
        request.setAttribute("blInfoList", blInfoList);
        // Validation
        GenericCode gc = genericcodeDao.getGenericCode(eculineEdiBean.getVesselName(), VESSEL_CODE);
        TradingPartner tp = tpDao.getTpBySsl(eculineEdiBean.getSslineNo());
        if (tp == null && null != eculineEdiBean.getSslineNo()) {
            tp = tpDao.findById(eculineEdiBean.getSslineNo());
            eculineEdiBean.setSsLineBluescreenNo(tp.getSslineNumber());
        }
        if (gc == null) {
            request.setAttribute("vessel", "error-indicator");
        }
        if (tp == null) {
            request.setAttribute("sslineNo", "error-indicator");
        }
        UnitType unitType = new UnitTypeDAO().getUnitTypeByDesc(eculineEdiBean.getContSize());
        if (unitType == null) {
            request.setAttribute("containerSize", "error-indicator");
        }
        //**********************************************************************
        //**************Setting Agent Value*************************************
        if (values.get("setAgentValFlag") == null) {
            values.put("setAgentValFlag", "false");
        }
        if (values.get("setAgentValFlagFromVoyage") == null) {
            values.put("setAgentValFlagFromVoyage", "false");
        }
        if (values.get("setAgentValFlag").equals("true") && CommonUtils.isNotEmpty(values.get("polCode"))) {
            String agentNo = new PortsDAO().getDefaultAgentNo(values.get("polCode"), "I");
            if (CommonUtils.isNotEmpty(agentNo)) {
                eculineEdiBean.setAgentNo(agentNo);
                eculineEdiBean.setPolUncode(values.get("polCode"));
            } else {
                eculineEdiBean.setAgentNo("");
                eculineEdiBean.setPolUncode(values.get("polCode"));
            }
        }
        Integer orginId = unLocationDAO.getUnLocIdByUnLocCode(eculineEdiBean.getPolUncode());
        Integer destinationId = unLocationDAO.getUnLocIdByUnLocCode(eculineEdiBean.getPodUncode());
        eculineEdiBean.setOriginId(orginId != null ? BigInteger.valueOf(orginId) : BigInteger.ZERO);
        eculineEdiBean.setDestinationId(destinationId != null ? BigInteger.valueOf(destinationId) : BigInteger.ZERO);
        //From Voyage********************
        if (CommonUtils.isEmpty(eculineEdiBean.getAgentNo()) && values.get("setAgentValFlagFromVoyage").equals("true") && CommonUtils.isNotEmpty(eculineEdiBean.getPolUncode())) {
            String agentNo = new PortsDAO().getDefaultAgentNo(eculineEdiBean.getPolUncode(), "I");
            if (CommonUtils.isNotEmpty(agentNo)) {
                eculineEdiBean.setAgentNo(agentNo);
                eculineEdiBean.setPolUncode(eculineEdiBean.getPolUncode());
            } else {
                eculineEdiBean.setPolUncode(eculineEdiBean.getPolUncode());
                eculineEdiBean.setAgentNo("");
            }
        }
        //**********************************************************************
        request.setAttribute("ecu", eculineEdiBean);
    }

    public Date format(String dateString) throws Exception {
        Date date = DateUtils.parseToDateForMonthMMM(dateString);
        String dateStr = DateUtils.formatDateTimeToDateTimeString(date);
        return DateUtils.parseStringToDateWithTime(dateStr);
    }

    public void approveBol(Map<String, String> values, HttpServletRequest request, User user) throws Exception {
        Date now = new Date();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        LclUnitSsDAO unitSsDao = new LclUnitSsDAO();
        EculineEdiBc eculineEdiBc = new EculineEdiBc();
        ContainerInfoDao containerDao = new ContainerInfoDao();
        BlInfoDao blDao = new BlInfoDao();

        String ecuId = values.get("ecuId");
        Long blId = Long.parseLong(values.get("blId"));
        EculineEdi eculineEdi = eculineEdiDao.SearchById(Long.parseLong(ecuId));
        List<ContainerInfo> containers = eculineEdi.getContainerInfoCollection();
        String containerNo = containers.isEmpty() ? "" : containers.get(0).getCntrName();
        Long headerId = eculineEdi.getUnitSs().getLclSsHeader().getId();
        BlInfo bl = blDao.findById(blId);
        ContainerInfo container = containerDao.getContainer(containerNo, eculineEdi.getId());
        //booking details
        List<LclBookingPiece> bkgPieces = eculineEdiBc.setBooking(eculineEdi, container, bl, user, request);
        LclUnitSs unitSs = unitSsDao.getUnitSs(headerId, containerNo);
        eculineEdiBc.setBkgPieceUnit(eculineEdi, unitSs, bkgPieces, user);
        //set approved
        bl.setApproved(true);
        bl.setUpdatedByUser(user);
        bl.setUpdatedDate(now);
        bl.setCreatedByUser(user);
        bl.setCreatedDate(now);
        blDao.update(bl);
    }

    public void checkBkgStatusSave(LclEculineEdiBlInfoForm lclEculineEdiBlInfoForm, HttpServletRequest request, LclFileNumberDAO lclFilenumberDAO) throws Exception {
        lclEculineEdiBlInfoForm.setFileNumber(lclEculineEdiBlInfoForm.getFileNumber().toUpperCase());
        BlInfo blInfo = new BlInfoDao().findById(Long.valueOf(lclEculineEdiBlInfoForm.getId()));
        EculineEdi eculineEdi = new EculineEdiDao().getVoyDetails(lclEculineEdiBlInfoForm.getVoyNo(), lclEculineEdiBlInfoForm.getContainerNo());
        Object[] lclFileNumberObject = lclFilenumberDAO.checkImpBkg(lclEculineEdiBlInfoForm.getFileNumber());
        if (lclFileNumberObject != null && lclFileNumberObject.length > 0 && eculineEdi != null) {
            String state = lclFileNumberObject[1].toString();
            if (!state.equalsIgnoreCase("Q")) {
                if (lclFileNumberObject[2] != null && !lclFileNumberObject[2].equals("")) {
                    Integer polId = Integer.parseInt(lclFileNumberObject[2].toString());
                    UnLocation unLocationPol = new UnLocationDAO().findById(polId);
                    if (lclFileNumberObject[3] != null && !lclFileNumberObject[3].equals("")) {
                        Integer podId = Integer.parseInt(lclFileNumberObject[3].toString());
                        UnLocation unLocationPod = new UnLocationDAO().findById(podId);
                        if (CommonUtils.isEqual(unLocationPol.getUnLocationCode(), eculineEdi.getPolUncode()) && CommonUtils.isEqual(unLocationPod.getUnLocationCode(), eculineEdi.getPodUncode())) {
                            pickFileNumber(lclEculineEdiBlInfoForm, request, Long.parseLong(lclFileNumberObject[0].toString()));
                        } else {
                            lclEculineEdiBlInfoForm.setMessage("D/R# " + lclEculineEdiBlInfoForm.getFileNumber() + " POL and POD does not matched");
                        }
                    } else {
                        lclEculineEdiBlInfoForm.setMessage("POD is Required for this D/R# " + lclEculineEdiBlInfoForm.getFileNumber());
                    }
                } else {
                    lclEculineEdiBlInfoForm.setMessage("POL is Required for this D/R# " + lclEculineEdiBlInfoForm.getFileNumber());
                }
            } else {
                lclEculineEdiBlInfoForm.setMessage("D/R# " + lclEculineEdiBlInfoForm.getFileNumber() + " Not Converted To Booking");
            }
        } else {
            lclEculineEdiBlInfoForm.setMessage("D/R# " + lclEculineEdiBlInfoForm.getFileNumber() + " does not exists");
        }
        request.setAttribute("lclEculineEdiBlInfoForm", lclEculineEdiBlInfoForm);
    }

    private void pickFileNumber(LclEculineEdiBlInfoForm lclEculineEdiBlInfoForm, HttpServletRequest request, Long fileNumberId) throws Exception {
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        List<LclBookingPiece> lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileNumberId);
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (int j = 0; j < lclBookingPiecesList.size(); j++) {
                LclBookingPiece lclBookingPiece = (LclBookingPiece) lclBookingPiecesList.get(j);
                List<LclBookingPieceUnit> lclBookingPieceUnitlist = new LclBookingPieceUnitDAO().findByProperty("lclBookingPiece.id", lclBookingPiece.getId());
                // List<BlInfo> BlInfoList = new BlInfoDao().findByProperty("fileNo.id", fileNumberId);
                if (lclBookingPieceUnitlist.isEmpty()) {
                    BlInfo blInfo = new BlInfoDao().findById(Long.valueOf(lclEculineEdiBlInfoForm.getId()));
                    blInfo.setFileNo(new LclFileNumber(fileNumberId));
//                    blInfo.setApproved(true);
                    new BlInfoDao().update(blInfo);
                    lclEculineEdiBlInfoForm.setMessage("D/R# Successfully Linked !");
                } else {
                    lclEculineEdiBlInfoForm.setMessage("D/R# " + lclEculineEdiBlInfoForm.getFileNumber() + " already Picked");
                }
            }
        } else {
            lclEculineEdiBlInfoForm.setMessage("D/R# " + lclEculineEdiBlInfoForm.getFileNumber() + " Commodity is not exists");
        }
    }
}

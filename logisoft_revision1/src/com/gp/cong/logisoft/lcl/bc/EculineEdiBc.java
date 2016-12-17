package com.gp.cong.logisoft.lcl.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLImportChargeCalc;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.beans.EculineEdiBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import com.gp.cong.logisoft.domain.lcl.CargoDetails;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.ContainerInfo;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.EculineEdi;
import com.gp.cong.logisoft.domain.lcl.EculinePackageTypeMapping;
import com.gp.cong.logisoft.domain.lcl.EculineTradingPartnerMapping;
import com.gp.cong.logisoft.domain.lcl.EculineUnLocationMapping;
import com.gp.cong.logisoft.domain.lcl.EculineUnitTypeMapping;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineEdiDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculinePackageTypeMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineTradingPartnerMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineUnLocationMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineUnitTypeMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.PackageTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.struts.action.lcl.LclUnitsRates;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.thread.LclFileNumberThread;
import com.logiware.thread.LclVoyageNumberThread;
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
public class EculineEdiBc implements LclCommonConstant {

    public static final String WEIGHT = "weight_values";
    public static final String VOLUME = "volume_values";
    private static final Integer WAREHOUSE_ID = 17;

    // set scheduled service (aka voyage) header details
    public LclSsHeader setSsHeader(EculineEdi eculineEdi, User user) throws Exception {
        LclSsHeader ssHeader = new LclSsHeader();
        UnLocationDAO unLocationDao = new UnLocationDAO();
        LclSsHeaderDAO ssHeaderDao = new LclSsHeaderDAO();
        UnLocation pol = unLocationDao.getUnlocation(eculineEdi.getPolUncode());
        UnLocation pod = unLocationDao.getUnlocation(eculineEdi.getPodUncode());
        ssHeader.setDestination(pod);
        ssHeader.setOrigin(pol);
        ssHeader.setTransMode(TRANS_MODE_VESSEL);
        ssHeader.setServiceType(SERVICE_MODE_IMPORT);
        ssHeader.setBillingTerminal(eculineEdi.getTerminal());
        if (CommonUtils.isEmpty(ssHeader.getScheduleNo())) {
            LclVoyageNumberThread thread = new LclVoyageNumberThread();
            ssHeader.setScheduleNo(thread.getVoyageNumber());
        }
        ssHeader.setStatus(STATUS_PENDING);
        ssHeader.setDatasource(DATA_SOURCE_LOGIWARE);
        ssHeader.setEnteredDatetime(new Date());
        ssHeader.setModifiedDatetime(new Date());
        ssHeader.setEnteredBy(user);
        ssHeader.setModifiedBy(user);
        ssHeader.setOwnerUserId(user);
        ssHeaderDao.save(ssHeader);
        return ssHeader;
    }

    // set scheduled service (aka voyage) details
    public LclSsDetail setSsDetails(EculineEdi eculineEdi, LclSsHeader ssHeader, User user) throws Exception {
        UnLocationDAO unLocationDao = new UnLocationDAO();
        LclSsDetailDAO ssDetailDao = new LclSsDetailDAO();
        LclSsDetail ssDetail = new LclSsDetail();
        ssDetail.setLclSsHeader(ssHeader);
        ssDetail.setTransMode(TRANS_MODE_VESSEL);
        ssDetail.setDeparture(unLocationDao.getUnlocation(eculineEdi.getPolUncode()));
        ssDetail.setArrival(unLocationDao.getUnlocation(eculineEdi.getPodUncode()));
        ssDetail.setDepartureLocation(eculineEdi.getPolUncode());
        ssDetail.setArrivalLocation(eculineEdi.getPodUncode());
        ssDetail.setSpReferenceNo(eculineEdi.getVoyNo());
        ssDetail.setSpReferenceName(eculineEdi.getVesselName());
        ssDetail.setSpAcctNo(eculineEdi.getSsline());
        ssDetail.setStd(eculineEdi.getEtd());
        ssDetail.setSta(eculineEdi.getEta());
        ssDetail.setDocReceived(eculineEdi.getDocReceived());
        ssDetail.setEnteredDatetime(new Date());
        ssDetail.setModifiedDatetime(new Date());
        ssDetail.setEnteredBy(user);
        ssDetail.setModifiedBy(user);
        ssDetailDao.save(ssDetail);
        return ssDetail;
    }

    // set Unit (aka Container) details
    public List<LclUnit> setUnit(EculineEdi eculineEdi, User user) throws Exception {
        LclUnit unit = null;
        LclUnitDAO unitDao = new LclUnitDAO();
        List<LclUnit> unitList = new ArrayList<LclUnit>();
        String unitNo = null;
        for (ContainerInfo container : eculineEdi.getContainerInfoCollection()) {
            unitNo = container.getCntrName();
            unit = unitDao.getUnit(unitNo);
            if (null == unit) {
                unit = new LclUnit();
                unit.setUnitNo(unitNo);
                unit.setUnitType(getUnitType(container.getCntrType()));
                unit.setEnteredDatetime(new Date());
                unit.setModifiedDatetime(new Date());
                unit.setEnteredBy(user);
                unit.setModifiedBy(user);
                unitDao.saveOrUpdate(unit);
            }
            unitList.add(unit);
        }
        return unitList;
    }

    public void setMappingContainerSize(EculineEdi eculineEdi) throws Exception {
        for (ContainerInfo container : eculineEdi.getContainerInfoCollection()) {
            UnitType unitType = new UnitTypeDAO().getUnitTypeByDesc(container.getCntrType());
            if (unitType == null) {
                EculineUnitTypeMapping eculineUnitTypeMapping = (EculineUnitTypeMapping) new EculineUnitTypeMappingDAO().getByProperty("cntrType", container.getCntrType());
                if (eculineUnitTypeMapping != null) {
                    container.setCntrType(eculineUnitTypeMapping.getUnitType().getDescription());
                }
            }
        }
    }
    // set UnitSS details

    public List<LclUnitSs> setUnitSs(EculineEdi eculineEdi, List<LclUnit> unitList, LclSsHeader ssHeader, User user) throws Exception {
        LclUnitSs unitSs = new LclUnitSs();
        LclUnitSsDAO unitSsDao = new LclUnitSsDAO();
        List<LclUnitSs> unitSsList = new ArrayList<LclUnitSs>();
        for (LclUnit unit : unitList) {
            unitSs.setLclUnit(unit);
            unitSs.setLclSsHeader(ssHeader);
            unitSs.setStatus(STATUS_EMPTY);
            unitSs.setEdi(Boolean.TRUE);
            unitSs.setEnteredDatetime(new Date());
            unitSs.setModifiedDatetime(new Date());
            unitSs.setEnteredBy(user);
            unitSs.setModifiedBy(user);
            unitSsList.add(unitSs);
            unitSsDao.save(unitSs);
        }
        return unitSsList;

    }

    public void setUnitCost(LclUnitSs lclUnitSs, ContainerInfo container, User user, HttpServletRequest request) throws Exception {
        List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        Date d = new Date();
        List<ImportsManifestBean> autoCostList = new LclUnitsRates().findAutoCosts(lclUnitSs.getLclSsHeader().getOrigin().getId(),
                lclUnitSs.getLclSsHeader().getDestination().getId(), container.getWarehouse().getId(), lclUnitSs.getLclUnit().getUnitType().getId());
        if (null != autoCostList && !autoCostList.isEmpty()) {
            for (ImportsManifestBean autoCost : autoCostList) {
                if (CommonUtils.isNotEmpty(autoCost.getChargeId()) && CommonUtils.isNotEmpty(autoCost.getAgentNo()) && autoCost.getTotalIPI() != null && lclUnitSs.getLclSsHeader() != null) {
                    // set auto cost values
                    LclSsAc lclSsAc = new LclSsAc();
                    lclSsAc.setApAmount(autoCost.getTotalIPI().divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    lclSsAc.setApTransType(ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
                    lclSsAc.setApAcctNo(tradingPartnerDAO.findById(autoCost.getAgentNo()));
                    GlMapping glmapping = new GlMappingDAO().findById(Integer.valueOf(autoCost.getChargeId().intValue()));
                    lclSsAc.setApGlMappingId(glmapping);
                    lclSsAc.setArGlMappingId(glmapping);
                    lclSsAc.setArAmount(BigDecimal.ZERO);
                    lclSsAc.setArAcctNo(tradingPartnerDAO.findById(autoCost.getAgentNo()));
                    lclSsAc.setManualEntry(false);
                    lclSsAc.setTransDatetime(d);
                    lclSsAc.setLclSsHeader(lclUnitSs.getLclSsHeader());
                    lclSsAc.setLclUnitSs(lclUnitSs);
                    lclSsAc.setModifiedByUserId(user);
                    lclSsAc.setEnteredByUserId(user);
                    lclSsAc.setModifiedDatetime(d);
                    lclSsAc.setEnteredDatetime(d);
                    new LclSsAcDAO().save(lclSsAc);
                    lclSsAcList.add(lclSsAc);
                }
            }
        }
        new LclManifestDAO().createLclAccrualsforAutoCosting(lclSsAcList);
    }

    // set UnitSSDispo details
    public List<LclUnitSsDispo> setUnitSsDispo(EculineEdi eculineEdi, LclSsDetail ssDetail, List<LclUnit> unitList, User user) throws Exception {
        LclUnitSsDispo unitSsDispo = new LclUnitSsDispo();
        LclUnitSsDispoDAO unitSsDispoDAO = new LclUnitSsDispoDAO();
        List<LclUnitSsDispo> unitSsDispoList = new ArrayList<LclUnitSsDispo>();
        Disposition dispo = new DispositionDAO().findByEliteCode(DISPOSITION_DATA);
        for (LclUnit unit : unitList) {
            unitSsDispo.setLclUnit(unit);
            unitSsDispo.setLclSsDetail(ssDetail);
            unitSsDispo.setDisposition(dispo);
            unitSsDispo.setDispositionDatetime(new Date());
            unitSsDispo.setEnteredDatetime(new Date());
            unitSsDispo.setModifiedDatetime(new Date());
            unitSsDispo.setEnteredBy(user);
            unitSsDispo.setModifiedBy(user);
            unitSsDispoList.add(unitSsDispo);
            unitSsDispoDAO.save(unitSsDispo);
        }
        return unitSsDispoList;

    }

    // set UnitSS imports details and Unit warehouse details
    public List<LclUnitSsImports> setUnitSsImports(EculineEdi eculineEdi, LclSsHeader ssHeader, List<LclUnit> unitList, User user) throws Exception {
        LclUnitSsImports unitSsImports = new LclUnitSsImports();
        LclUnitWhse lclUnitWhse = null;
        LclUnitSsImportsDAO unitSsImportsDao = new LclUnitSsImportsDAO();
        LclUnitWhseDAO unitWhseDao = new LclUnitWhseDAO();
        List<LclUnitSsImports> unitSsImportsList = new ArrayList<LclUnitSsImports>();
        List<LclUnitWhse> unitWarehouses = new ArrayList<LclUnitWhse>();
        List<ContainerInfo> containers = eculineEdi.getContainerInfoCollection();
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        for (LclUnit unit : unitList) {
            for (ContainerInfo container : containers) {
                if (container.getCntrName().equals(unit.getUnitNo())) {
                    unitSsImports.setLclUnit(unit);
                    unitSsImports.setLclSsHeader(ssHeader);
                    unitSsImports.setCfsWarehouseId(container.getWarehouse());
                    unitSsImports.setOriginAcctNo(container.getAgent());
                    unitSsImports.setEnteredDatetime(new Date());
                    unitSsImports.setModifiedDatetime(new Date());
                    unitSsImports.setEnteredBy(user);
                    unitSsImports.setModifiedBy(user);
                    unitSsImportsList.add(unitSsImports);
                    unitSsImportsDao.save(unitSsImports);
                    //set unit warehouse****************************************
                    lclUnitWhse = new LclUnitWhseDAO().getLclUnitWhseDetails(unit.getId(), ssHeader.getId());
                    if (lclUnitWhse == null) {
                        lclUnitWhse = new LclUnitWhse();
                        lclUnitWhse.setSealNoIn(container.getSealNo());
                        lclUnitWhse.setEnteredBy(user);
                        lclUnitWhse.setEnteredDatetime(new Date());
                        lclUnitWhse.setModifiedBy(user);
                        lclUnitWhse.setModifiedDatetime(new Date());
                    }
                    lclUnitWhse.setLclSsHeader(ssHeader);
                    lclUnitWhse.setLclUnit(unit);
                    Integer warehse = warehouseDAO.warehouseNo(ssHeader.getDestination().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(warehse)) {
                        lclUnitWhse.setWarehouse(warehouseDAO.findById(warehse));
                    }
                    unitWarehouses.add(lclUnitWhse);
                    unitWhseDao.save(lclUnitWhse);
                    //**********************************************************
                }
            }
        }
        return unitSsImportsList;
    }

    // set UnitSS manifest details
    public List<LclUnitSsManifest> setUnitSsManifest(EculineEdi eculineEdi, LclSsHeader ssHeader, List<LclUnit> unitList, User user) throws Exception {
        LclUnitSsManifest unitSsManifest = new LclUnitSsManifest();
        LclUnitSsManifestDAO unitSsManifestDao = new LclUnitSsManifestDAO();
        List<LclUnitSsManifest> unitSsManifestList = new ArrayList<LclUnitSsManifest>();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        LclUtils util = new LclUtils();
        BigDecimal weight, volume;
        for (LclUnit unit : unitList) {
            for (ContainerInfo container : eculineEdi.getContainerInfoCollection()) {
                if (unit.getUnitNo().equals((container.getCntrName()))) {
                    weight = eculineEdiDao.sum(container.getId(), WEIGHT);
                    volume = eculineEdiDao.sum(container.getId(), VOLUME);
                    unitSsManifest.setLclUnit(unit);
                    unitSsManifest.setLclSsHeader(ssHeader);
                    unitSsManifest.setCalculatedWeightImperial(util.convertToLbs(Double.parseDouble(weight.toString())));
                    unitSsManifest.setCalculatedWeightMetric(weight);
                    unitSsManifest.setCalculatedVolumeImperial(util.convertToCft(Double.parseDouble(volume.toString())));
                    unitSsManifest.setCalculatedVolumeMetric(volume);
                    unitSsManifest.setMasterbl(container.getMasterBl().toUpperCase());
                    unitSsManifest.setCalculatedDrCount(Integer.parseInt(eculineEdiDao.countDr(container.getId()).toString()));
                    unitSsManifest.setEnteredDatetime(new Date());
                    unitSsManifest.setModifiedDatetime(new Date());
                    unitSsManifest.setEnteredByUser(user);
                    unitSsManifest.setModifiedByUser(user);
                    unitSsManifestList.add(unitSsManifest);
                    unitSsManifestDao.save(unitSsManifest);
                }
            }
        }
        return unitSsManifestList;
    }

    //set booking and booking pieces values
    public List<LclBookingPiece> setBooking(EculineEdi eculineEdi, ContainerInfo container, BlInfo bl, User user, HttpServletRequest request) throws Exception {
        LclFileNumber lclFileNumber = null;
        LCLBookingDAO bkgDao = new LCLBookingDAO();
        LclBooking bkg = null;
        List<LclBookingPiece> bkgPieces = new ArrayList<LclBookingPiece>();
        BlInfoDao blInfoDao = new BlInfoDao();
        UnLocationDAO unLocationDao = new UnLocationDAO();
        LclBookingPiece bkgPiece = null;
        LclBookingPieceDAO bkgPieceDao = new LclBookingPieceDAO();
        PackageTypeDAO packageDao = new PackageTypeDAO();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        LclUtils util = new LclUtils();
        TradingPartner ship = null;
        TradingPartner cons = null;
        TradingPartner noty1 = null;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        if (bl.getFileNo() != null) {
            lclFileNumber = bl.getFileNo();
            bkgPieces.addAll(bkgPieceDao.findByProperty("lclFileNumber.id", bl.getFileNo().getId()));
            saveBookingImport(eculineEdi, container, bl, user, lclFileNumber);
            saveBookingImportAms(eculineEdi, container, bl, user, lclFileNumber);
            bl.setFileNo(lclFileNumber);
            bl.setUpdatedByUser(user);
            bl.setUpdatedDate(new Date());
            blInfoDao.update(bl);
        } else {
            LclFileNumberThread thread = new LclFileNumberThread();
            String fileNumber = thread.getFileNumber();
            Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
            if (fileNumberId == 0) {
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            } else {
                thread = new LclFileNumberThread();
                fileNumber = thread.getFileNumber();
                lclFileNumber = lclFileNumberDAO.createFileNumber(fileNumber, "B");
            }
            if (!lclFileNumberDAO.getSession().getTransaction().isActive()) {
                lclFileNumberDAO.getSession().getTransaction().begin();
            }
            bkg = new LclBooking();
            bkgPiece = new LclBookingPiece();
            if (CommonUtils.isNotEmpty(bl.getShipperCode())) {
                ship = tpDao.findById(bl.getShipperCode());
                bkg.setShipAcct(ship);
            }
            if (CommonUtils.isNotEmpty(bl.getConsCode())) {
                cons = tpDao.findById(bl.getConsCode());
                bkg.setConsAcct(cons);
            }
            if (CommonUtils.isNotEmpty(bl.getNotify1Code())) {
                noty1 = tpDao.findById(bl.getNotify1Code());
                bkg.setNotyAcct(noty1);
            }
            LclContact shipper = setContact(bl.getShipperNad(), ship, lclFileNumber, user, SHIPPER);
            LclContact consignee = setContact(bl.getConsNad(), cons, lclFileNumber, user, CONSIGNEE);
            LclContact notify1 = setContact(bl.getNotify1Nad(), noty1, lclFileNumber, user, NOTIFY);
            LclContact agent = setContact("", container.getAgent(), lclFileNumber, user, SUPPLIER);
            LclContact client = setContact("", null, lclFileNumber, user, CONTACT_TYPE_CLIENT);
            LclContact notify2 = setContact("", null, lclFileNumber, user, CONTACT_TYPE_NOTIFY2);
            LclContact forwarder = setContact("", null, lclFileNumber, user, FORWARDER);
            bkg.setFileNumberId(lclFileNumber.getId());
            bkg.setLclFileNumber(lclFileNumber);

            bkg.setTransMode(TRANS_MODE_VESSEL);
            //Setting Unlocation****************************************************
            UnLocation pod = unLocationDao.getUnlocation(bl.getPodUncode());
            bkg.setPortOfOrigin(CommonUtils.isNotEmpty(bl.getPorUncode()) ? unLocationDao.getUnlocation(bl.getPorUncode()) : null);
            bkg.setPortOfLoading(unLocationDao.getUnlocation(bl.getPolUncode()));
            bkg.setPortOfDestination(pod);
            if (CommonUtils.isNotEmpty(bl.getPoddeliveryUncode())) {
                bkg.setFinalDestination(unLocationDao.getUnlocation(bl.getPoddeliveryUncode()));
            } else {
                bkg.setFinalDestination(pod);
            }
            bkg.setRateType(RATETYPE_FTF);
            bkg.setTerminal(eculineEdi.getTerminal());
            bkg.setBillingType(BILLTYPE_COLLECT);
            bkg.setShipContact(shipper);
            bkg.setConsContact(consignee);
            bkg.setNotyContact(notify1);
            bkg.setSupContact(agent);
            bkg.setClientContact(client);
            bkg.setNotify2Contact(notify2);
            bkg.setFwdContact(forwarder);
            bkg.setDefaultAgent(true);
            bkg.setAgentAcct(container.getAgent());
            bkg.setAgentContact(agent);
            bkg.setSupAcct(container.getAgent());
            bkg.setEnteredDatetime(new Date());
            bkg.setModifiedDatetime(new Date());
            bkg.setBillToParty(BILLTYPE_COLLECT);
            bkg.setEnteredBy(user);
            bkg.setModifiedBy(user);
            //Creating trashipment booking if place of delivery is foreign *********
            if (!(bl.getPoddeliveryUncode().startsWith("US")) && !(bl.getPoddeliveryUncode().startsWith("CA")) && !(bl.getPoddeliveryUncode().startsWith("PR"))) {
                UnLocation podLoc = (UnLocation) new UnLocationDAO().getUnlocation(bl.getPodUncode());
                UnLocation delivery = (UnLocation) new UnLocationDAO().getUnlocation(bl.getPoddeliveryUncode());
                if (podLoc != null && delivery != null) {
                    LclBookingPlanBean lbpb = new LclBookingPlanDAO().getRelay(podLoc.getId(), delivery.getId(), "Y");
                    if (lbpb != null && lbpb.getPol_id() != null && lbpb.getPod_id() != null) {
                        bkg.setBookingType(LCL_TRANSHIPMENT_TYPE);
            } else {
                bkg.setBookingType(SERVICE_MODE_IMPORT);
            }
                } else {
                    bkg.setBookingType(SERVICE_MODE_IMPORT);
                }
            } else {
                bkg.setBookingType(SERVICE_MODE_IMPORT);
            }
            //**********************************************************************
            //**********Set Commodity Type***********
            GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
            CommodityType commodityType = null;
            String[] commodity = null;
            if (null != noty1) {
                commodity = generalInformationDAO.getCommodity(noty1.getAccountno(), "Notify", null);
            }
            if ((null == commodity || CommonUtils.isEmpty(commodity[0])) && null != cons) {
                commodity = generalInformationDAO.getCommodity(cons.getAccountno(), "Consignee", null);
            }
            if ((null == commodity || CommonUtils.isEmpty(commodity[0])) && null != container.getAgent()) {
                commodity = generalInformationDAO.getCommodity(container.getAgent().getAccountno(), "Agent", (null != noty1 ? noty1.getAccountno() : null));
            }
            if (null != commodity && CommonUtils.isNotEmpty(commodity[0])) {
                commodityType = new commodityTypeDAO().getByProperty("code", commodity[0]);
            }
            PortsDAO portsDAO = new PortsDAO();
            LCLImportRatesDAO lclImportRatesDAO = new LCLImportRatesDAO();
            if (commodityType != null) {
                String ert = null;
                String orgUncode = null != bkg.getPortOfOrigin() ? bkg.getPortOfOrigin().getUnLocationCode() : "";
                String polUncode = null != bkg.getPortOfLoading() ? bkg.getPortOfLoading().getUnLocationCode() : "";
                String podUncode = null != bkg.getPortOfDestination() ? bkg.getPortOfDestination().getUnLocationCode() : "";
                if (CommonUtils.isNotEmpty(orgUncode)) {
                    String[] origin = portsDAO.getSchedNoAndRegion(orgUncode);
                    String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
                    ert = lclImportRatesDAO.getERT(origin, destination, commodityType.getCode());
                }
                if (CommonUtils.isEmpty(ert)) {
                    String[] origin = portsDAO.getSchedNoAndRegion(polUncode);
                    String[] destination = portsDAO.getSchedNoAndRegion(podUncode);
                    ert = lclImportRatesDAO.getERT(origin, destination, commodityType.getCode());
                }
                if ("Y".equalsIgnoreCase(ert)) {
                    bkg.setRtdTransaction(true);
                } else {
                    bkg.setRtdTransaction(false);
                }
                bkgPiece.setCommodityType(commodityType);
            }
            //**************************************
            bkgDao.saveOrUpdate(bkg);
            //Creating commodity ***************************************************
            bkgPiece.setLclFileNumber(lclFileNumber);
            bkgPiece.setPersonalEffects(PERSONAL_EFFECTS_NO);
            CargoDetails cargoFirst = bl.getCargoDetailsCollection().get(0);
            bkgPiece.setPackageType(packageDao.findPackage(cargoFirst.getPackageDesc()));
            if (CommonUtils.isNotEmpty(cargoFirst.getGoodDesc())) {
                bkgPiece.setPieceDesc(cargoFirst.getGoodDesc().substring(0,
                        cargoFirst.getGoodDesc().length() >= 254 ? 254 : cargoFirst.getGoodDesc().length()));
            }
            if (CommonUtils.isNotEmpty(cargoFirst.getMarksNoDesc())) {
                bkgPiece.setMarkNoDesc(cargoFirst.getMarksNoDesc().substring(0,
                        cargoFirst.getMarksNoDesc().length() >= 254 ? 254 : cargoFirst.getMarksNoDesc().length()));
            }
            int totalPieceCount = 0;
            double totalBWI = 0.0, toatalBWM = 0.0, totalBVI = 0.0, totalBVM = 0.0;
            for (CargoDetails cargo : bl.getCargoDetailsCollection()) {
                totalPieceCount = totalPieceCount + cargo.getPackageAmount();
                totalBWI = totalBWI + util.convertToLbs(Double.parseDouble(cargo.getWeightValues().toString())).doubleValue();
                toatalBWM = toatalBWM + cargo.getWeightValues().doubleValue();
                totalBVI = totalBVI + util.convertToCft(Double.parseDouble(cargo.getVolumeValues().toString())).doubleValue();
                totalBVM = totalBVM + cargo.getVolumeValues().doubleValue();
            }
            bkgPiece.setBookedPieceCount(totalPieceCount);
            bkgPiece.setBookedWeightImperial(BigDecimal.valueOf(totalBWI));
            bkgPiece.setBookedWeightMetric(BigDecimal.valueOf(toatalBWM));
            bkgPiece.setBookedVolumeImperial(BigDecimal.valueOf(totalBVI));
            bkgPiece.setBookedVolumeMetric(BigDecimal.valueOf(totalBVM));
            bkgPiece.setEnteredDatetime(new Date());
            bkgPiece.setModifiedDatetime(new Date());
            bkgPiece.setEnteredBy(user);
            bkgPiece.setModifiedBy(user);
            bkgPieces.add(bkgPiece);
            bkgPieceDao.getCurrentSession().clear();
            bkgPieceDao.saveOrUpdate(bkgPiece);
            //**********************************************************************
            saveBookingImport(eculineEdi, container, bl, user, lclFileNumber);
            saveBookingImportAms(eculineEdi, container, bl, user, lclFileNumber);
            //Charges Calculation
            String transhipment = "N";
            String origin = "";
            String portOfLoad = "";
            String poortOfDest = "";
            String destination = "";
            if (!(bl.getPoddeliveryUncode().startsWith("US")) && !(bl.getPoddeliveryUncode().startsWith("CA"))
                    && !(bl.getPoddeliveryUncode().startsWith("PR"))) {
                transhipment = "Y";
            }
            if (bkg.getPortOfOrigin() != null) {
                origin = bkg.getPortOfOrigin().getUnLocationCode();
            }
            if (bkg.getPortOfLoading() != null) {
                portOfLoad = bkg.getPortOfLoading().getUnLocationCode();
            }
            if (bkg.getPortOfDestination() != null) {
                poortOfDest = bkg.getPortOfDestination().getUnLocationCode();
            }
            if (bkg.getFinalDestination() != null) {
                destination = bkg.getFinalDestination().getUnLocationCode();
            }
            LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
            lclImportChargeCalc.ImportRateCalculation(origin, portOfLoad, poortOfDest, destination, transhipment, bkg.getBillingType(),
                    bkg.getBillToParty(), bkg.getSupAcct().getAccountno(), String.valueOf(container.getWarehouse().getId()),
                    bkg.getFileNumberId(), bkgPieces, request, user, String.valueOf(container.getEculineEdi().getUnitSs().getId()));
            bl.setFileNo(lclFileNumber);
            bl.setUpdatedByUser(user);
            bl.setUpdatedDate(new Date());
            blInfoDao.update(bl);
        }
        if(bkg !=null && bkg.getSupAcct() !=null && bkg.getSupAcct().getAccountno() !=null) {
        String newBrand = new TradingPartnerDAO().getBusinessUnit(container.getAgent().getAccountno());
        new LclFileNumberDAO().updateEconoEculine(newBrand, String.valueOf(bkg.getFileNumberId()));
        }
        //LCL Remarks
        util.insertLCLRemarks(bl.getFileNo().getId(), "EDI: D/R created by Eculine EDI", REMARKS_DR_AUTO_NOTES, user);
        if (bl.getNoDocsOriginal() > 0) {
            util.insertLCLRemarks(bl.getFileNo().getId(), "EDI: Original B/L Required", REMARKS_DR_AUTO_NOTES, user);
        }

        if (bl.getNoDocsOriginal() == 0) {
            util.insertLCLRemarks(bl.getFileNo().getId(), "EDI: Express Release", REMARKS_DR_AUTO_NOTES, user);
        }

        if (bl.getPoddeliveryDesc() != null && !bl.getPoddeliveryDesc().equals("")) {
            util.insertLCLRemarks(bl.getFileNo().getId(), "EDI: Place of Delivery ->" + bl.getPoddeliveryDesc(), REMARKS_DR_AUTO_NOTES, user);
        }

        return bkgPieces;
    }

    public void saveBookingImport(EculineEdi eculineEdi, ContainerInfo container, BlInfo bl,
            User user, LclFileNumber fileNo) throws Exception {
        LclBookingImport bkgImport = null;
        LclBookingImportDAO bkgImportDao = new LclBookingImportDAO();
        if (bl.getFileNo() != null) {
            bkgImport = bkgImportDao.getByProperty("lclFileNumber.id", bl.getFileNo().getId());
        }
        if (bkgImport == null) {
            bkgImport = new LclBookingImport();
        }
        WarehouseDAO warehouseDao = new WarehouseDAO();
        bkgImport.setLclFileNumber(fileNo);
        bkgImport.setFileNumberId(fileNo.getId());
        bkgImport.setDestWhse(warehouseDao.findById(WAREHOUSE_ID));
        Boolean value = bkgImport.getDeclaredValueEstimated();
        Boolean weight = bkgImport.getDeclaredWeightEstimated();
        bkgImport.setDeclaredValueEstimated(null != value ? value : false);
        bkgImport.setDeclaredWeightEstimated(null != weight ? weight : false);
        bkgImport.setExpressReleaseClause(bl.getNoDocsOriginal() != null && bl.getNoDocsOriginal().equals(0) ? true : false);
        //bkgImport.setScac(eculineEdi.getScacCode());
        if (CommonUtils.isNotEmpty(bl.getBlNo())) {
            bkgImport.setSubHouseBl(bl.getBlNo().replace("/", ""));
        }
        //Creating trashipment booking if place of delivery is foreign *********
        if (!(bl.getPoddeliveryUncode().startsWith("US")) && !(bl.getPoddeliveryUncode().startsWith("CA")) && !(bl.getPoddeliveryUncode().startsWith("PR"))) {
            bkgImport.setTransShipment(true);
            new LclDwr().fillAesItnValues(bkgImport.getFileNumberId().toString(), "AES_EXCEPTION");
            UnLocation pod = (UnLocation) new UnLocationDAO().getUnlocation(bl.getPodUncode());
            UnLocation delivery = (UnLocation) new UnLocationDAO().getUnlocation(bl.getPoddeliveryUncode());
            LclBookingPlanBean lbpb = new LclBookingPlanDAO().getRelay(pod.getId(), delivery.getId(), "Y");
            if (lbpb != null) {
                if (lbpb.getPol_id() != null) {
                    bkgImport.setUsaPortOfExit(new UnLocation(lbpb.getPol_id()));
                }

                if (lbpb.getPod_id() != null) {
                    bkgImport.setForeignPortOfDischarge(new UnLocation(lbpb.getPod_id()));
                }
            }
        }
        //**********************************************************************
        bkgImport.setEnteredDatetime(new Date());
        bkgImport.setModifiedDatetime(new Date());
        bkgImport.setEnteredBy(user);
        bkgImport.setModifiedBy(user);
        bkgImportDao.saveOrUpdate(bkgImport);
    }

    public void saveBookingImportAms(EculineEdi eculineEdi, ContainerInfo container, BlInfo bl,
            User user, LclFileNumber fileNo) throws Exception {
        LclBookingImportAms lclBookingImportAms = null;
        BigInteger ScacAndAms = new LclBookingImportAmsDAO().getAmsAndScac(bl.getAmsScac(), bl.getBlNo().replace("/", ""));
        if (bl.getFileNo() != null) {
            lclBookingImportAms = new LclBookingImportAmsDAO().getByProperty("lclFileNumber.id", bl.getFileNo().getId());
        }
        if (lclBookingImportAms == null) {
            lclBookingImportAms = new LclBookingImportAms();
        }
        lclBookingImportAms.setAmsNo(bl.getBlNo().replace("/", ""));
        lclBookingImportAms.setPieces(0);
        lclBookingImportAms.setScac(bl.getAmsScac());
        lclBookingImportAms.setLclFileNumber(fileNo);
        lclBookingImportAms.setEnteredByUserId(user);
        lclBookingImportAms.setModifiedByUserId(user);
        lclBookingImportAms.setModifiedDatetime(new Date());
        lclBookingImportAms.setEnteredDatetime(new Date());
        if (ScacAndAms.intValue() == 0) {
            new LclBookingImportAmsDAO().saveOrUpdate(lclBookingImportAms);
        }
    }

    public void setMappingPackageType(BlInfo bl) throws Exception {
        for (CargoDetails cargo : bl.getCargoDetailsCollection()) {
            PackageType packageType = new PackageTypeDAO().findByDesc(cargo.getPackageDesc());
            if (packageType == null) {
                EculinePackageTypeMapping eculinePackageTypeMapping = (EculinePackageTypeMapping) new EculinePackageTypeMappingDAO().getByProperty("packageDesc", cargo.getPackageDesc());
                if (eculinePackageTypeMapping != null) {
                    cargo.setPackageDesc(eculinePackageTypeMapping.getPackageType().getDescription());
                }
            }
        }
    }

    public void setMappingAddressses(BlInfo bl) throws Exception {
        EculineTradingPartnerMappingDAO eculineTradingPartnerMappingDAO = new EculineTradingPartnerMappingDAO();
        //*******************Automatically Mapping of Shipper Address Description
        if (bl.getShipperCode() == null || bl.getShipperCode().equals("")) {
            EculineTradingPartnerMapping eculineTradingPartnerMappingShip = (EculineTradingPartnerMapping) eculineTradingPartnerMappingDAO.getByProperty("address", bl.getShipperNad());
            if (eculineTradingPartnerMappingShip != null) {
                bl.setShipperNad(this.getAddress(eculineTradingPartnerMappingShip.getTradingPartner().getAccountno()));
                bl.setShipperCode(eculineTradingPartnerMappingShip.getTradingPartner().getAccountno());
            }
        }
        //*******************Automatically Mapping of CONSIGNEE Address Description
        if (bl.getConsCode() == null || bl.getConsCode().equals("")) {
            EculineTradingPartnerMapping eculineTradingPartnerMappingCon = (EculineTradingPartnerMapping) eculineTradingPartnerMappingDAO.getByProperty("address", bl.getConsNad());
            if (eculineTradingPartnerMappingCon != null) {
                bl.setConsNad(this.getAddress(eculineTradingPartnerMappingCon.getTradingPartner().getAccountno()));
                bl.setConsCode(eculineTradingPartnerMappingCon.getTradingPartner().getAccountno());
            }
        }
        //*******************Automatically Mapping of NOTIFY Address Description
        if (bl.getNotify1Code() == null || bl.getNotify1Code().equals("")) {
            EculineTradingPartnerMapping eculineTradingPartnerMappingNot1 = (EculineTradingPartnerMapping) eculineTradingPartnerMappingDAO.getByProperty("address", bl.getNotify1Nad());
            if (eculineTradingPartnerMappingNot1 != null) {
                bl.setNotify1Nad(this.getAddress(eculineTradingPartnerMappingNot1.getTradingPartner().getAccountno()));
                bl.setNotify1Code(eculineTradingPartnerMappingNot1.getTradingPartner().getAccountno());
            }
        }
        //*******************Automatically Mapping of NOTIFY2 Address Description
        if (bl.getNotify2Code() == null || bl.getNotify2Code().equals("")) {
            EculineTradingPartnerMapping eculineTradingPartnerMappingNot2 = (EculineTradingPartnerMapping) eculineTradingPartnerMappingDAO.getByProperty("address", bl.getNotify2Nad());
            if (eculineTradingPartnerMappingNot2 != null) {
                bl.setNotify2Nad(this.getAddress(eculineTradingPartnerMappingNot2.getTradingPartner().getAccountno()));
                bl.setNotify2Code(eculineTradingPartnerMappingNot2.getTradingPartner().getAccountno());
            }
        }
    }

    public void setMappingUnLocationDesc(BlInfo bl) throws Exception {
        EculineUnLocationMappingDAO eculineUnLocationMappingDAO = new EculineUnLocationMappingDAO();
        //*******************Automatically Mapping of POL unLocation Code Description
        if (CommonUtils.isEmpty(bl.getPolUncode())) {
            EculineUnLocationMapping eculineUnLocationMappingPOL = (EculineUnLocationMapping) eculineUnLocationMappingDAO.getByProperty("unLocationDesc", bl.getPolDesc());
            if (eculineUnLocationMappingPOL != null) {
                bl.setPolUncode(eculineUnLocationMappingPOL.getUnLocationCode());
                bl.setPolDesc(eculineUnLocationMappingPOL.getUnLocationDesc());
            }
        }
        //*******************Automatically Mapping of POD unLocation Code Description
        if (CommonUtils.isEmpty(bl.getPodUncode())) {
            EculineUnLocationMapping eculineUnLocationMappingPOD = (EculineUnLocationMapping) eculineUnLocationMappingDAO.getByProperty("unLocationDesc", bl.getPodDesc());
            if (eculineUnLocationMappingPOD != null) {
                bl.setPodUncode(eculineUnLocationMappingPOD.getUnLocationCode());
                bl.setPodDesc(eculineUnLocationMappingPOD.getUnLocationDesc());
            }
        }
        //*******************Automatically Mapping of Delivery unLocation Code Description
        if (CommonUtils.isEmpty(bl.getPoddeliveryUncode())) {
            EculineUnLocationMapping eculineUnLocationMappingDelivery = (EculineUnLocationMapping) eculineUnLocationMappingDAO.getByProperty("unLocationDesc", bl.getPoddeliveryDesc());
            if (eculineUnLocationMappingDelivery != null) {
                bl.setPoddeliveryUncode(eculineUnLocationMappingDelivery.getUnLocationCode());
                bl.setPoddeliveryDesc(eculineUnLocationMappingDelivery.getUnLocationDesc());
            }
        }
    }

    public String getAddress(String accountNo) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        CustAddress custAddress = custAddressDAO.findByAccountNoAndPrime(accountNo);
        StringBuilder address = new StringBuilder();
        address.append(custAddress.getAddress1());
        address.append(", ");
        address.append(custAddress.getCity1());
        address.append(", ");
        address.append(custAddress.getState());
        address.append(", ");
        address.append(custAddressDAO.getCountry(custAddress.getCountry()));
        address.append(", ");
        address.append(custAddress.getZip());
        return address.toString();
    }

    public void setBkgPieceUnit(EculineEdi eculineEdi, LclUnitSs unitSs, List<LclBookingPiece> bkgPieces,
            User user) throws Exception {
        LclBookingPieceUnit pieceUnit = new LclBookingPieceUnit();
        LclBookingPieceUnitDAO pieceUnitDao = new LclBookingPieceUnitDAO();
        for (LclBookingPiece bkgPiece : bkgPieces) {
            pieceUnit.setLclBookingPiece(bkgPiece);
            pieceUnit.setLclUnitSs(unitSs);
            pieceUnit.setEnteredDatetime(new Date());
            pieceUnit.setModifiedDatetime(new Date());
            pieceUnit.setEnteredBy(user);
            pieceUnit.setModifiedBy(user);
            pieceUnitDao.save(pieceUnit);
        }
    }

    public String buildPath(Map<String, String> values) throws Exception {
        StringBuilder pathBuilder = new StringBuilder();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        Map<String, Object> filters = new HashMap<String, Object>();
        //get EculineEdiBean
        filters.put("`edi`.id", values.get("ecuId"));
        List<EculineEdiBean> eculineEdiList = eculineEdiDao.search(filters, 1);
        EculineEdiBean eculineEdiBean = new EculineEdiBean();
        if (!eculineEdiList.isEmpty()) {
            eculineEdiBean = eculineEdiList.get(0);
        }
        //build redirect action path
        pathBuilder.append(values.get("path"));
        pathBuilder.append("?methodName=editVoyage");
        pathBuilder.append("&headerId=").append(values.get("headerId"));
        pathBuilder.append("&unitId=").append(values.get("unitId"));
        if (null != eculineEdiBean) {
            pathBuilder.append("&pol=").append(eculineEdiBean.getOrigin());
            pathBuilder.append("&pod=").append(eculineEdiBean.getDestination());
            pathBuilder.append("&originalOriginId=").append(eculineEdiBean.getOriginId());
            pathBuilder.append("&originalDestinationId=").append(eculineEdiBean.getDestinationId());
            pathBuilder.append("&originalOriginName=").append(eculineEdiBean.getOrigin());
            pathBuilder.append("&originalDestinationName=").append(eculineEdiBean.getDestination());
        }
        return pathBuilder.toString();
    }

    private LclContact setContact(String nad, TradingPartner tp, LclFileNumber fileNo, User user, String type) throws Exception {
        LclContact contact = new LclContact();
        LCLContactDAO contactDao = new LCLContactDAO();
        if (null == tp && null != nad && nad.length() > 30) {
            contact.setCompanyName(nad.substring(0, 29));
            contact.setAddress(nad.substring(29));
            contact.setRemarks(type);
        } else if (null != tp) {
            contact.setCompanyName(tp.getAccountName());
            contact.setAddress(tp.getAddress());
        }
        contact.setEnteredDatetime(new Date());
        contact.setModifiedDatetime(new Date());
        contact.setEnteredBy(user);
        contact.setModifiedBy(user);
        contact.setTradingPartner(tp);
        contact.setLclFileNumber(fileNo);
        contactDao.save(contact);
        return contact;
    }

    //########other common methods########
    // get UnitType based on Unit (aka container) type
    public UnitType getUnitType(String containerType) throws Exception {
        UnitTypeDAO unitTypeDao = new UnitTypeDAO();
        UnitType unitType = unitTypeDao.getUnitTypeByDesc(containerType);
        return unitType;
    }
}

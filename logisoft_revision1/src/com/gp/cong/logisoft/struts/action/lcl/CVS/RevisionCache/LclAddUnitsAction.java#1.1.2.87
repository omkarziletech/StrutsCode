package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.dwr.LclDwr;
import com.gp.cong.logisoft.beans.BookingUnitsBean;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.Disposition;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddUnitsForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class LclAddUnitsAction extends LogiwareDispatchAction {

    private static String ADD_UNITS = "addUnits";

    public ActionForward goBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("displaySearch");
    }

    public ActionForward editUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        if (lclAddUnitsForm.getUnitId() != null && lclAddUnitsForm.getUnitId() > 0) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnitSsDAO lclunitssdao = new LclUnitSsDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            LclUnitWhse lclUnitWhse = new LclUnitWhseDAO().getLclUnitWhseFirstRecord(lclAddUnitsForm.getUnitId(), lclAddUnitsForm.getHeaderId());
            LclUnitSs lclunitss = new LclUnitSs();
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitssId())) {
                lclunitss = lclunitssdao.findById(lclAddUnitsForm.getUnitssId());
            }
            if (lclunitss.getDrayageProvided()) {
                lclAddUnitsForm.setDrayageProvided("Y");
            } else {
                lclAddUnitsForm.setDrayageProvided("N");
            }
            if (lclunitss.getIntermodalProvided()) {
                lclAddUnitsForm.setIntermodalProvided("Y");
            } else {
                lclAddUnitsForm.setIntermodalProvided("N");
            }
            if (lclunitss.getStopOff()) {
                lclAddUnitsForm.setStopoff("Y");
            } else {
                lclAddUnitsForm.setStopoff("N");
            }
            if (CommonUtils.isNotEmpty(lclunitss.getChassisNo())) {
                lclAddUnitsForm.setChasisNo(lclunitss.getChassisNo());
            }
            if (CommonUtils.isNotEmpty(lclunitss.getSUHeadingNote())) {
                lclAddUnitsForm.setSealNo(lclunitss.getSUHeadingNote());
            }
            if (null == lclunitss.getSolasTareWeightKGS()) {
                lclAddUnitsForm.setCargoWeightKgs(lclunit.getTareWeightMetric() == null ? 0.0 : lclunit.getTareWeightMetric().doubleValue());
            }
            if (null == lclunitss.getSolasTareWeightLBS()) {
                lclAddUnitsForm.setCargoWeightLbs(lclunit.getTareWeightImperial() == null ? 0.0 : lclunit.getTareWeightImperial().doubleValue());
            }
            if (CommonUtils.isNotEmpty(lclunit.getComments())) {
                lclAddUnitsForm.setComments(lclunit.getComments().toUpperCase());
            }
            if (lclunit != null) {
                lclAddUnitsForm.setUnitNo(lclunit.getUnitNo());
                if (lclunit.getUnitType() != null && lclunit.getUnitType().getId() != null
                        && lclunit.getUnitType().getId() > 0) {
                    lclAddUnitsForm.setUnitType(lclunit.getUnitType().getId());
                }
                if (lclunit.getHazmatPermitted() || lclAddUnitsForm.isHazStatus()) {
                    lclAddUnitsForm.setHazmatPermitted("Y");
                } else {
                    lclAddUnitsForm.setHazmatPermitted("N");
                }
                if (lclunit.getRefrigerated()) {
                    lclAddUnitsForm.setRefrigerationPermitted("Y");
                } else {
                    lclAddUnitsForm.setRefrigerationPermitted("N");
                }
                if (lclunit.getGrossWeightImperial() != null && lclunit.getGrossWeightImperial().doubleValue() > 0.00) {
                    lclAddUnitsForm.setGrossWeightImperial(lclunit.getGrossWeightImperial().toString());
                }
                if (lclunit.getGrossWeightMetric() != null && lclunit.getGrossWeightMetric().doubleValue() > 0.00) {
                    lclAddUnitsForm.setGrossWeightMetric(lclunit.getGrossWeightMetric().toString());
                }
                if (lclunit.getVolumeImperial() != null && lclunit.getVolumeImperial().doubleValue() > 0.00) {
                    lclAddUnitsForm.setVolumeImperial(lclunit.getVolumeImperial().toString());
                }
                if (lclunit.getVolumeMetric() != null && lclunit.getVolumeMetric().doubleValue() > 0.00) {
                    lclAddUnitsForm.setVolumeMetric(lclunit.getVolumeMetric().toString());
                }
                if (lclunit.getRemarks() != null && !lclunit.getRemarks().trim().equals("")) {
                    lclAddUnitsForm.setRemarks(lclunit.getRemarks());
                }
                if (lclunitss != null) {
                    lclAddUnitsForm.setCob(lclunitss.getCob() ? "Yes" : "No");
                    if (CommonUtils.isNotEmpty(lclunitss.getSpBookingNo())) {
                        lclAddUnitsForm.setBookingNumber(lclunitss.getSpBookingNo());
                        String documentStatus = new LclSSMasterBlDAO().getMasterBlDocumentStatus(lclunitss.getSpBookingNo(), lclunitss.getLclSsHeader().getId().toString());
                        lclAddUnitsForm.setReceivedMaster(!"".equalsIgnoreCase(documentStatus) ? documentStatus : "N");
                    } else {
                        lclAddUnitsForm.setReceivedMaster("N");
                    }
                    if (null != lclunitss.getSolasDunnageWeightLBS()) {
                        lclAddUnitsForm.setDunnageWeightLbs(lclunitss.getSolasDunnageWeightLBS().doubleValue());
                    }
                    if (null != lclunitss.getSolasDunnageWeightKGS()) {
                        lclAddUnitsForm.setDunnageWeightKgs(lclunitss.getSolasDunnageWeightKGS().doubleValue());
                    }
                    if (null != lclunitss.getSolasCargoWeightKGS()) {
                        lclAddUnitsForm.setCargoWeightKgs(lclunitss.getSolasCargoWeightKGS().doubleValue());
                    }
                    if (null != lclunitss.getSolasCargoWeightLBS()) {
                        lclAddUnitsForm.setCargoWeightLbs(lclunitss.getSolasCargoWeightLBS().doubleValue());
                    }
                    if (null != lclunitss.getSolasTareWeightKGS()) {
                        lclAddUnitsForm.setTareWeightKgs(lclunitss.getSolasTareWeightKGS().doubleValue());
                    }
                    if (null != lclunitss.getSolasTareWeightLBS()) {
                        lclAddUnitsForm.setTareWeightLbs(lclunitss.getSolasTareWeightLBS().doubleValue());
                    }
                    if (CommonUtils.isNotEmpty(lclunitss.getSolasVerificationSign())) {
                        lclAddUnitsForm.setVerificationSignature(lclunitss.getSolasVerificationSign());
                    }
                    if (null != lclunitss.getSolasVerificationDate()) {
                        lclAddUnitsForm.setVerificationDate(DateUtils.formatDate(lclunitss.getSolasVerificationDate(), "dd-MMM-yyyy"));
                    }
                }
                request.setAttribute("modDate", lclunit.getModifiedDatetime());
            }
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitssId())) {
                viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
            } else if ("unassignedContainers".equalsIgnoreCase(lclAddUnitsForm.getFilterByChanges())) {
                List<LclUnitWhse> lclWarehouseList = new LclUnitWhseDAO().findByProperty("lclUnit.id", lclunit.getId());
                lclAddUnitsForm.setSealNo(lclWarehouseList.get(0).getSealNoOut() == null ? "" : lclWarehouseList.get(0).getSealNoOut());
                lclAddUnitsForm.setChasisNo(lclWarehouseList.get(0).getChassisNoIn() == null ? "" : lclWarehouseList.get(0).getChassisNoIn());
                lclAddUnitsForm.setLoadedBy(lclWarehouseList.get(0).getStuffedByUser() == null ? "" : lclWarehouseList.get(0).getStuffedByUser().getFirstName()
                        + " " + lclWarehouseList.get(0).getStuffedByUser().getLastName());
                lclAddUnitsForm.setLoaddeByUserId(lclWarehouseList.get(0).getStuffedByUser() == null ? "" : String.valueOf(lclWarehouseList.get(0).getStuffedByUser().getUserId()));
                request.setAttribute("lclWarehouseList", lclWarehouseList);
            }
            if (lclUnitWhse != null && lclUnitWhse.getStuffedByUser() != null) {
                lclAddUnitsForm.setLoadedBy(lclUnitWhse.getStuffedByUser().getFirstName() + " " + lclUnitWhse.getStuffedByUser().getLastName());
                lclAddUnitsForm.setLoaddeByUserId(String.valueOf(lclUnitWhse.getStuffedByUser().getUserId()));
            }
        } else {
            lclAddUnitsForm.setDrayageProvided("N");
            lclAddUnitsForm.setIntermodalProvided("N");
            lclAddUnitsForm.setStopoff("N");
            lclAddUnitsForm.setHazmatPermitted("N");
            lclAddUnitsForm.setRefrigerationPermitted("N");
            lclAddUnitsForm.setReceivedMaster("N");
        }
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        request.setAttribute("unitsReopened", lclAddUnitsForm.getUnitsReopened());
        setUnitValues(lclAddUnitsForm, request);
        return mapping.findForward(ADD_UNITS);
    }

    public ActionForward unAssignedUnit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        UnitTypeDAO unitTypeDAO = new UnitTypeDAO();
        List<LabelValueBean> list = unitTypeDAO.getUnassignedUnit(lclAddUnitsForm.getWareHouseId());
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitssId())) {
            LclUnit lclunit = new LclUnitDAO().findById(lclAddUnitsForm.getUnitId());
            viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
        }
        setUnitValues(lclAddUnitsForm, request);
        request.setAttribute("unAssignList", list);
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        request.setAttribute("setLabel", "true");
        return mapping.findForward(ADD_UNITS);
    }

    public ActionForward addWareHouse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitWhse lclUnitWhse = null;
        LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
        Date d = new Date();
        if (lclAddUnitsForm.getUnitWarehouseId() != null && lclAddUnitsForm.getUnitWarehouseId() > 0) {
            lclUnitWhse = lclunitwhsedao.findById(lclAddUnitsForm.getUnitWarehouseId());
        } else {
            lclUnitWhse = new LclUnitWhse();
            lclUnitWhse.setEnteredBy(getCurrentUser(request));
            lclUnitWhse.setEnteredDatetime(d);
        }

        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getArrivedDateTime())) {
            lclUnitWhse.setArrivedDatetime(DateUtils.parseDate(lclAddUnitsForm.getArrivedDateTime(), "dd-MMM-yyyy"));
        } else {
            lclUnitWhse.setArrivedDatetime(null);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getDepartedDateTime())) {
            lclUnitWhse.setDepartedDatetime(DateUtils.parseDate(lclAddUnitsForm.getDepartedDateTime(), "dd-MMM-yyyy"));
        } else {
            lclUnitWhse.setDepartedDatetime(null);
        }
        lclUnitWhse.setLocation(CommonUtils.isNotEmpty(lclAddUnitsForm.getLocation())
                ? lclAddUnitsForm.getLocation().toUpperCase() : null);
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getWarehouseId())) {
            if (lclUnitWhse.getWarehouse() == null) {
                WarehouseDAO warehousedao = new WarehouseDAO();
                Warehouse warehouse = warehousedao.findById(lclAddUnitsForm.getWarehouseId());
                lclUnitWhse.setWarehouse(warehouse);
            } else if (lclAddUnitsForm.getWarehouseId().intValue() != lclUnitWhse.getWarehouse().getId().intValue()) {
                WarehouseDAO warehousedao = new WarehouseDAO();
                Warehouse warehouse = warehousedao.findById(lclAddUnitsForm.getWarehouseId());
                lclUnitWhse.setWarehouse(warehouse);
            }
        }
        lclUnitWhse.setModifiedBy(getCurrentUser(request));
        lclUnitWhse.setModifiedDatetime(d);
        lclUnitWhse.setSealNoOut(lclAddUnitsForm.getSealNoOut());
        request.setAttribute("sealNumber", lclUnitWhse.getSealNoOut());
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getHeaderId())) {
            lclUnitWhse.setLclSsHeader(new LclSsHeader(lclAddUnitsForm.getHeaderId()));
        } else {
            lclUnitWhse.setLclSsHeader(null);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            if (lclAddUnitsForm.getUnitWarehouseId() != null && lclAddUnitsForm.getUnitWarehouseId() > 0) {
                lclunitwhsedao.update(lclUnitWhse);
            } else {
                lclUnitWhse.setLclUnit(lclunit);
                lclunitwhsedao.save(lclUnitWhse);
            }
            viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
        }
        setUnitValues(lclAddUnitsForm, request);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward addDisposition(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        User loginUser = getCurrentUser(request);
        LclBookingDispoDAO bookingDispoDAO = new LclBookingDispoDAO();
        LclUnitSsDispo lclUnitSsDispo = null;
        List<LclSsDetail> ssDetailList = null;
        LclUnitSsDispoDAO lclunitssDispdao = new LclUnitSsDispoDAO();
        Date d = new Date();
        Disposition disposition = null;
        if (lclAddUnitsForm.getUnitDispoId() != null && lclAddUnitsForm.getUnitDispoId() > 0) {
            lclUnitSsDispo = lclunitssDispdao.findById(lclAddUnitsForm.getUnitDispoId());
        }
        if (lclUnitSsDispo == null) {
            lclUnitSsDispo = new LclUnitSsDispo();
            lclUnitSsDispo.setEnteredBy(loginUser);
            lclUnitSsDispo.setEnteredDatetime(d);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getDispositionId())) {
            disposition = new DispositionDAO().findById(lclAddUnitsForm.getDispositionId());
            lclUnitSsDispo.setDisposition(disposition);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getDispositionDateTime())) {
            lclUnitSsDispo.setDispositionDatetime(DateUtils.parseDate(lclAddUnitsForm.getDispositionDateTime(), "dd-MMM-yyyy hh:mm:ss a"));
        } else {
            lclUnitSsDispo.setDispositionDatetime(null);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getHeaderId())) {
            ssDetailList = new LclSsDetailDAO().findByProperty("lclSsHeader.id", lclAddUnitsForm.getHeaderId());
            lclUnitSsDispo.setLclSsDetail(ssDetailList.get(0));
        }
        lclUnitSsDispo.setModifiedBy(loginUser);
        lclUnitSsDispo.setModifiedDatetime(d);
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
            LclUnitDAO lclunitdao = new LclUnitDAO();
            LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
            lclUnitSsDispo.setLclUnit(lclunit);
            lclunitssDispdao.saveOrUpdate(lclUnitSsDispo);

            if ("RCVD".equalsIgnoreCase(lclAddUnitsForm.getDisposition())) {
                List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(lclAddUnitsForm.getUnitssId());
                if (CommonUtils.isNotEmpty(pickedDrList)) {
                    for (ManifestBean picked : pickedDrList) {
                        // ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
                        //notificationDAO.insertCodeJBkgContactsByDispo(picked.getFileId(), loginUser.getUserId());
                        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getHeaderId()) && CommonUtils.isNotEmpty(lclAddUnitsForm.getHeaderId())) {
                            LclSsDetail lclSsDetail = new LclSsDetailDAO().getLclDetailByArrivalId(lclAddUnitsForm.getHeaderId(), lclAddUnitsForm.getUnLocationId().intValue());
                            Warehouse wareHouse = null;
                            if (lclSsDetail != null && lclSsDetail.getArrival() != null) {
                                RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(lclSsDetail.getArrival().getUnLocationCode(), "Y");
                                wareHouse = new WarehouseDAO().getWareHouseBywarehsNo(terminal != null ? "W" + terminal.getTrmnum() : "");
                                lclSsDetail.setDestuffingWarehouse(wareHouse);
                                new LclSsDetailDAO().saveOrUpdate(lclSsDetail);
                                bookingDispoDAO.insertBookingDispoForRCVD(picked.getFileId(),
                                        disposition.getId(),
                                        lclunit != null ? lclunit.getId().intValue() : null,
                                        lclSsDetail.getId().intValue(),
                                        loginUser.getUserId(),
                                        lclSsDetail.getArrival().getId(),
                                        wareHouse != null ? wareHouse.getId() : null);
                            }
                        }
                    }
                }
            } else {
                List<ManifestBean> pickedDrList = new ExportUnitQueryUtils().getPickedDrList(lclAddUnitsForm.getUnitssId());
                for (ManifestBean picked : pickedDrList) {
                    bookingDispoDAO.insertBookingDispo(picked.getFileId(), disposition.getId(),
                            lclAddUnitsForm.getUnitssId().intValue(),
                            ssDetailList.get(0).getId().intValue(), loginUser.getUserId(), null);
                    //ExportNotificationDAO notificationDAO = new ExportNotificationDAO();
                    //notificationDAO.insertCodeJBkgContactsByDispo(picked.getFileId(), loginUser.getUserId());
                }
            }
            setUnitValues(lclAddUnitsForm, request);
            viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
        }
        return mapping.findForward("addUnits");
    }

    public ActionForward deleteWareHouse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
        if (lclAddUnitsForm.getUnitWarehouseId() != null && lclAddUnitsForm.getUnitWarehouseId() > 0) {
            LclUnitWhse lclUnitWhse = lclunitwhsedao.findById(lclAddUnitsForm.getUnitWarehouseId());
            lclunitwhsedao.delete(lclUnitWhse);
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
                LclUnitDAO lclunitdao = new LclUnitDAO();
                LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
                viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
            }
        }
        setUnitValues(lclAddUnitsForm, request);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward deleteDisposition(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnitSsDispoDAO lclunitSsDispdao = new LclUnitSsDispoDAO();
        LclUnitSsDispo lclunitSsDisp = new LclUnitSsDispo();
        LclBookingDispoDAO lclBookingDispoDAO = new LclBookingDispoDAO();
        List<BookingUnitsBean> listPicked = null;
        LclUnitSsDispo lclUnitSsDispo = new LclUnitSsDispoDAO().getByProperty("id", lclAddUnitsForm.getUnitDispoId());
        if (lclAddUnitsForm.getFilterByChanges().equalsIgnoreCase("lclDomestic") && lclUnitSsDispo != null
                && "RCVD".equalsIgnoreCase(lclUnitSsDispo.getDisposition().getDescription())) {
            List<LclSsDetail> ssDetailList = new LclSsDetailDAO().findByProperty("lclSsHeader.id", lclAddUnitsForm.getHeaderId());
            LclUnitSs lclUnitSs = new LclUnitSsDAO().getLclUnitSSByLclUnitHeader(lclAddUnitsForm.getUnitId(),
                    ssDetailList.get(0).getLclSsHeader().getId());
            listPicked = new LclUnitDAO().getStuffedListByUnit(lclUnitSs.getId());
            for (BookingUnitsBean li : listPicked) {
                if ("PICKED".equalsIgnoreCase(li.getStatus()) || "HAZ,PICKED".equalsIgnoreCase(li.getStatus())) {
                    lclBookingDispoDAO.delteLclBookingDispo(li.getFileId(), lclAddUnitsForm.getUnitId(), lclUnitSsDispo.getDisposition().getId());
                }
            }
        }
        if (lclAddUnitsForm.getUnitDispoId() != null && lclAddUnitsForm.getUnitDispoId() > 0) {
            lclunitSsDisp = lclunitSsDispdao.findById(lclAddUnitsForm.getUnitDispoId());
            lclunitSsDispdao.delete(lclunitSsDisp);
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
                LclUnitDAO lclunitdao = new LclUnitDAO();
                LclUnit lclunit = lclunitdao.findById(lclAddUnitsForm.getUnitId());
                viewWareHouseAndDisposition(lclAddUnitsForm, lclunit, request);
            }
        }
        setUnitValues(lclAddUnitsForm, request);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward addUnits(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        request.setAttribute("lclAddUnitsForm", lclAddUnitsForm);
        return mapping.findForward("displayNewUnit");
    }

     public ActionForward saveUnassignedContainer(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAddUnitsForm lclAddUnitsForm = (LclAddUnitsForm) form;
        LclUnit lclunit = null;
        Date d = new Date();
        LclUnitDAO unitDAO = new LclUnitDAO();
        LclUnit unit = new LclUnitDAO().getUnit(lclAddUnitsForm.getUnitNo());
        if (unit != null) {
            lclunit = unitDAO.findById(unit.getId());
        }
        if (lclunit == null) {
            lclunit = new LclUnit();
            lclunit.setEnteredBy(getCurrentUser(request));
            lclunit.setEnteredDatetime(d);
        }
        lclunit.setUnitNo(!CommonUtils.isEmpty(lclAddUnitsForm.getUnitNo()) ? lclAddUnitsForm.getUnitNo().toUpperCase() : "");
        lclunit.setModifiedBy(getCurrentUser(request));
        lclunit.setModifiedDatetime(d);

        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getHazmatPermitted())) {
            lclunit.setHazmatPermitted(lclAddUnitsForm.getHazmatPermitted().equalsIgnoreCase("Y") ? true : false);
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getRefrigerationPermitted())) {
            lclunit.setRefrigerated(lclAddUnitsForm.getRefrigerationPermitted().equalsIgnoreCase("Y") ? true : false);
        }
        //lclunit.setRemarks(!CommonUtils.isEmpty(lclAddUnitsForm.getRemarks()) ? lclAddUnitsForm.getRemarks() : "");
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getUnitId())) {
            lclunit.setComments(CommonUtils.isNotEmpty(lclAddUnitsForm.getComments()) ? lclAddUnitsForm.getComments().toUpperCase()
                    : "");
        } else {
            lclunit.setComments(CommonUtils.isNotEmpty(lclAddUnitsForm.getComments()) ? lclAddUnitsForm.getComments().toUpperCase()
                    : lclunit.getComments());
        }

        UnitType unitType = new UnitTypeDAO().findById(lclAddUnitsForm.getUnitType());
        lclunit.setRemarks(lclAddUnitsForm.getRemarks().toUpperCase());
        lclunit.setUnitType(unitType);
        lclunit.setTareWeightImperial(new BigDecimal(lclAddUnitsForm.getCargoWeightLbs()));
        lclunit.setTareWeightMetric(new BigDecimal(lclAddUnitsForm.getCargoWeightKgs()));
        unitDAO.saveOrUpdate(lclunit);

        // Saving WareHouse Details        
        if (lclAddUnitsForm.getWarehouseId() != null && CommonUtils.isEmpty(lclAddUnitsForm.getUnitId())) {
            WarehouseDAO warehousedao = new WarehouseDAO();
            Warehouse warehouse = warehousedao.findById(lclAddUnitsForm.getWarehouseId());
            LclUnitWhse lclunitwhse = new LclUnitWhse();
            lclunitwhse.setLclUnit(lclunit);
            lclunitwhse.setWarehouse(warehouse);
            lclunitwhse.setEnteredBy(getCurrentUser(request));
            lclunitwhse.setEnteredDatetime(d);
            lclunitwhse.setModifiedBy(getCurrentUser(request));
            lclunitwhse.setModifiedDatetime(d);
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getLoaddeByUserId())) {
              lclunitwhse.setStuffedByUser(new UserDAO().findById(Integer.parseInt(lclAddUnitsForm.getLoaddeByUserId())));
            }
            lclunitwhse.setSealNoOut(lclAddUnitsForm.getSealNo());
            lclunitwhse.setChassisNoIn(lclAddUnitsForm.getChasisNo());
            new LclUnitWhseDAO().saveOrUpdate(lclunitwhse);
        } 
        else {
            LclUnitWhse lclUnitWhse = new LclUnitWhseDAO()
                    .getLclUnitWhse(lclAddUnitsForm.getWarehouseId(), lclAddUnitsForm.getUnitId());
            lclUnitWhse.setWarehouse(lclUnitWhse.getWarehouse());
            lclUnitWhse.setLclUnit(lclunit);
            if (CommonUtils.isNotEmpty(lclAddUnitsForm.getLoaddeByUserId())) {
                lclUnitWhse.setStuffedByUser(new UserDAO().findById(Integer.parseInt(lclAddUnitsForm.getLoaddeByUserId())));
            }else{
                 lclUnitWhse.setStuffedByUser(null);
            }
            lclUnitWhse.setSealNoOut(lclAddUnitsForm.getSealNo());
            lclUnitWhse.setChassisNoIn(lclAddUnitsForm.getChasisNo());
            lclUnitWhse.setModifiedBy(getCurrentUser(request));
            lclUnitWhse.setModifiedDatetime(d);
            new LclUnitWhseDAO().saveOrUpdate(lclUnitWhse);
        }
        // ---------------------  End

        return mapping.findForward("voyageResuleSearchPage");
    }


    public void viewWareHouseAndDisposition(LclAddUnitsForm lclAddUnitsForm,
            LclUnit lclunit, HttpServletRequest request) throws Exception {
        List<LclUnitWhse> lclWarehouseList = null;
        if ("unassignedContainers".equalsIgnoreCase(lclAddUnitsForm.getFilterByChanges())) {
            lclWarehouseList = new LclUnitWhseDAO().findByProperty("lclUnit.id", lclunit.getId());
            request.setAttribute("lclWarehouseList", lclWarehouseList);
        } else {
            lclWarehouseList = new LclUnitWhseDAO().getwhseListByUnitHeaderId(lclunit.getId(), lclAddUnitsForm.getHeaderId());
            request.setAttribute("lclWarehouseList", lclWarehouseList);
        }
        Long ssDetailId = new LclSsDetailDAO().getIdbyAsc(lclAddUnitsForm.getHeaderId());
        if (CommonUtils.isNotEmpty(ssDetailId)) {
            request.setAttribute("modDate", lclunit.getModifiedDatetime());
            List<LclUnitSsDispo> lclDispositionList = new LclUnitSsDispoDAO().getDispositionList(lclunit.getId(), ssDetailId);
            request.setAttribute("lclDispositionList", lclDispositionList);
        }
    }

    public void setUnitValues(LclAddUnitsForm lclAddUnitsForm,
            HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getHeaderId())) {
            request.setAttribute("arrivalCityList", new LclSsDetailDAO().getLclDetailArrivalCities(lclAddUnitsForm.getHeaderId()));
        }
        if (CommonUtils.isNotEmpty(lclAddUnitsForm.getFilterByChanges())) {
            Integer dispoId = new LclDwr().disposDesc(lclAddUnitsForm.getFilterByChanges());
            if (dispoId != null) {
                lclAddUnitsForm.setDispositionDateTime(DateUtils.formatDate(new Date(), "dd-MMM-yyyy hh:mm:ss a"));
                if (lclAddUnitsForm.getFilterByChanges().equalsIgnoreCase("lclExport")
                        || lclAddUnitsForm.getFilterByChanges().equalsIgnoreCase("lclCfcl")) {
                    lclAddUnitsForm.setDispositionId(dispoId);
                    lclAddUnitsForm.setDisposition("ACTV");
                } else if (lclAddUnitsForm.getFilterByChanges().equalsIgnoreCase("lclDomestic")) {
                    lclAddUnitsForm.setDispositionId(dispoId);
                    lclAddUnitsForm.setDisposition("LDIN");
                }
            }
        }
    }

    public ActionForward customDltMoveToYard(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("unitId", request.getParameter("unitId"));
        request.setAttribute("sealNo", request.getParameter("sealNo"));
        request.setAttribute("chassisNo", request.getParameter("chassisNo"));
        request.setAttribute("loadedByUserId", request.getParameter("loadedByUserId"));
        return mapping.findForward("lclUnitDltMove");
    }
}

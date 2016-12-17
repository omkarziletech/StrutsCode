package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.beans.EculineEdiBean;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.BlInfo;
import com.gp.cong.logisoft.domain.lcl.ContainerInfo;
import com.gp.cong.logisoft.domain.lcl.EculineEdi;
import com.gp.cong.logisoft.domain.lcl.EculineUnitTypeMapping;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.BlInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.ContainerInfoDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineEdiDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.EculineUnitTypeMappingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.lcl.bc.BolUtils;
import com.gp.cong.logisoft.lcl.bc.EculineEdiBc;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiForm;
import com.logiware.common.form.SessionForm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Rajesh
 */
public class LclEculineEdiAction extends LogiwareDispatchAction {

    public static final Integer VESSEL_CODE = 14;

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        Map<String, Object> filters = new HashMap<String, Object>();
        // Search filters
        if (CommonUtils.isNotEmpty(lclEculineEdiForm.getContainerNo())) {
            filters.put("ci.`cntr_name`", lclEculineEdiForm.getContainerNo());
            request.setAttribute("contNo", lclEculineEdiForm.getContainerNo());
        }
        if (CommonUtils.isNotEmpty(lclEculineEdiForm.getPolUncode())) {
            filters.put("edi.`pol_uncode`", lclEculineEdiForm.getPolUncode());
            request.setAttribute("pol", lclEculineEdiForm.getPolUncode());
        }
        if (CommonUtils.isNotEmpty(lclEculineEdiForm.getPodUncode())) {
            filters.put("edi.`pod_uncode`", lclEculineEdiForm.getPodUncode());
            request.setAttribute("pod", lclEculineEdiForm.getPodUncode());
        }
        if (CommonUtils.isNotEmpty(lclEculineEdiForm.getReferenceNo())) {
            filters.put("edi.`header_ref`", lclEculineEdiForm.getReferenceNo());
            request.setAttribute("refNo", lclEculineEdiForm.getReferenceNo());
        }
        if (CommonUtils.isNotEmpty(lclEculineEdiForm.getInvoiceNumber())) {
            filters.put("ei.`invoice_number`", lclEculineEdiForm.getInvoiceNumber());
            request.setAttribute("invoiceNumber", lclEculineEdiForm.getInvoiceNumber());
        }
        if ("on".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            request.setAttribute("containerApproved", true);
            request.setAttribute("containerUnApproved", true);
            request.setAttribute("containerreadyToApproved", true);
        } else if ("on".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("approvedOnly", true);
            request.setAttribute("containerApproved", true);
            request.setAttribute("containerUnApproved", false);
            request.setAttribute("containerreadyToApproved", false);
        } else if ("off".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("unapprovedOnly", true);
            request.setAttribute("containerApproved", false);
            request.setAttribute("containerUnApproved", true);
            request.setAttribute("containerreadyToApproved", false);
        } else if ("off".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("readyToApproveOnly", true);
            request.setAttribute("containerApproved", false);
            request.setAttribute("containerUnApproved", false);
            request.setAttribute("containerreadyToApproved", true);
        } else if ("off".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("ignoreApproved", true);
            request.setAttribute("containerApproved", false);
            request.setAttribute("containerUnApproved", true);
            request.setAttribute("containerreadyToApproved", true);
        } else if ("on".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("ignoreUnapproved", true);
            request.setAttribute("containerApproved", true);
            request.setAttribute("containerUnApproved", false);
            request.setAttribute("containerreadyToApproved", true);
        } else if ("on".equalsIgnoreCase(lclEculineEdiForm.getApproved())
                && "on".equalsIgnoreCase(lclEculineEdiForm.getUnapproved())
                && "off".equalsIgnoreCase(lclEculineEdiForm.getReadyToApproved())) {
            filters.put("ignoreReadyToApprove", true);
            request.setAttribute("containerApproved", true);
            request.setAttribute("containerUnApproved", true);
            request.setAttribute("containerreadyToApproved", false);
        } else {
            request.setAttribute("containerApproved", false);
            request.setAttribute("containerUnApproved", false);
            request.setAttribute("containerreadyToApproved", false);
        }
        SessionForm oldEculineEdiForm = new SessionForm();
        PropertyUtils.copyProperties(oldEculineEdiForm, lclEculineEdiForm);
        request.getSession().setAttribute("oldEculineEdiForm", oldEculineEdiForm);
        List<EculineEdiBean> eculineEdiList = eculineEdiDao.search(filters, lclEculineEdiForm.getLimit());
        request.setAttribute("eculineEdiList", eculineEdiList);
        return mapping.findForward("success");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        lclEculineEdiForm.setLimit(25);
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        SessionForm oldEculineEdiForm = new SessionForm();
        PropertyUtils.copyProperties(oldEculineEdiForm, lclEculineEdiForm);
        request.getSession().setAttribute("oldEculineEdiForm", oldEculineEdiForm);
        List<EculineEdiBean> eculineEdiList = eculineEdiDao.search(null, lclEculineEdiForm.getLimit());
        request.setAttribute("unitNumber", request.getParameter("unitNumber"));
        request.setAttribute("voyNumber", request.getParameter("voyNumber"));
        request.setAttribute("unitId", request.getParameter("unitId"));
        request.setAttribute("containerApproved", true);
        request.setAttribute("containerUnApproved", true);
        request.setAttribute("containerreadyToApproved", true);
        request.setAttribute("eculineEdiList", eculineEdiList);
        return mapping.findForward("success");
    }

    public ActionForward goBackToMainScrn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        SessionForm oldEculineEdiForm = (SessionForm) request.getSession().getAttribute("oldEculineEdiForm");
        PropertyUtils.copyProperties(lclEculineEdiForm, oldEculineEdiForm);
        request.setAttribute("unitNumber", request.getParameter("unitNumber"));
        request.setAttribute("voyNumber", request.getParameter("voyNumber"));
        request.setAttribute("unitId", request.getParameter("unitId"));
        request.setAttribute("containerApproved", true);
        request.setAttribute("containerUnApproved", true);
        request.setAttribute("containerreadyToApproved", true);
        request.setAttribute("lclEculineEdiForm", lclEculineEdiForm);
        return search(mapping, lclEculineEdiForm, request, response);
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclEculineEdiForm lclEculineEdiForm = new LclEculineEdiForm();
        request.setAttribute("lclEculineEdiForm", lclEculineEdiForm);
        request.setAttribute("containerApproved", true);
        request.setAttribute("containerUnApproved", true);
        request.setAttribute("containerreadyToApproved", true);
        return mapping.findForward("success");
    }

    public ActionForward getVoyageDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        BolUtils bolUtils = new BolUtils();
        String id = request.getParameter("id");
        request.setAttribute("hblNo", request.getParameter("hblNo"));
        request.setAttribute("invoiceReq", request.getParameter("isInvoiceReq"));
        values.put("id", id);
        values.put("setAgentValFlagFromVoyage", "true");
        bolUtils.voyageDetails(request, values);
        return mapping.findForward("container");
    }

    public ActionForward openContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        BolUtils bolUtils = new BolUtils();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        String fileId = request.getParameter("fileId");
        String ecuId = eculineEdiDao.getEcuId("file_number_id", fileId);
        request.setAttribute("fileId", fileId);
        values.put("fileId", fileId);
        values.put("id", ecuId);
        bolUtils.voyageDetails(request, values);
        User user = getCurrentUser(request);
        new DBUtil().releaseLockByRecordIdAndModuleId(new LclFileNumberDAO().getFileNumberByFileId(fileId), "LCL FILE", user.getUserId());
        return mapping.findForward("container");
    }

    public ActionForward viewXmlFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        EculineEdi eculineEdi = new EculineEdiDao().SearchById(id);
        response.addHeader("Content-Disposition", "inline; filename=" + eculineEdi.getFileName());
        String contentType = CommonUtils.getMimeType(eculineEdi.getFileName(), eculineEdi.getFile());
        response.setContentType(contentType + ";charset=utf-8");
        IOUtils.write(eculineEdi.getFile(), response.getOutputStream());
        return null;
    }

    public List scacHbl(String ecuId) throws Exception {
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        String containerInfoId = eculineEdiDao.getContainerInfoId(ecuId);
        List scacList = eculineEdiDao.getScacHbl(containerInfoId);
        return scacList;
    }

    public ActionForward approveVoy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        synchronized (LclEculineEdiAction.class) {
            LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
            String voyNo = lclEculineEdiForm.getVoyNo();
            String ecuId = lclEculineEdiForm.getId();
            EculineEdiBc eculineEdiBc = new EculineEdiBc();
            EculineEdi eculineEdi = eculineEdiDao.SearchById(Long.parseLong(ecuId));
            User user = getCurrentUser(request);
            //check Already Exists Voyage In Lcl or not ****************************
            if (eculineEdiDao.checkAlreadyExistsVoyageInLcl(voyNo, lclEculineEdiForm.getContainerNo())) {
                request.setAttribute("approved", "Combination of Voyage # : " + voyNo + " and Conatiner :" + lclEculineEdiForm.getContainerNo() + " already exist, so You cannot approve.");
                SessionForm oldEculineEdiForm = (SessionForm) request.getSession().getAttribute("oldEculineEdiForm");
                PropertyUtils.copyProperties(lclEculineEdiForm, oldEculineEdiForm);
                request.setAttribute("lclEculineEdiForm", lclEculineEdiForm);
                return search(mapping, lclEculineEdiForm, request, response);
            }
            //***********Mapping Container Size*************************************
            eculineEdiBc.setMappingContainerSize(eculineEdi);
            //ss details
            LclSsHeader ssHeader = eculineEdiBc.setSsHeader(eculineEdi, user);
            LclSsDetail ssDetail = eculineEdiBc.setSsDetails(eculineEdi, ssHeader, user);
            //unit details
            List<LclUnit> units = eculineEdiBc.setUnit(eculineEdi, user);
            List<LclUnitSs> unitSses = eculineEdiBc.setUnitSs(eculineEdi, units, ssHeader, user);
            eculineEdiBc.setUnitSsDispo(eculineEdi, ssDetail, units, user);
            eculineEdiBc.setUnitSsImports(eculineEdi, ssHeader, units, user);
            eculineEdiBc.setUnitSsManifest(eculineEdi, ssHeader, units, user);
            //set approved
            eculineEdi.setApproved(true);
            eculineEdi.setUnitSs(null != unitSses ? unitSses.get(0) : null);
            eculineEdi.setCreatedByUser(user);
            eculineEdi.setCreatedDate(new Date());
            eculineEdi.setUpdatedByUser(user);
            eculineEdi.setUpdatedDate(new Date());
            eculineEdiDao.update(eculineEdi);
            new LclUnitSsRemarksDAO().insertLclunitRemarks(ssHeader.getId(), units.get(0).getId(), "auto", "EDI: Voyage created by Eculine Edi", user.getUserId());
            //approving all bls assigned to eculineEdi******************************
            //**********************************************************************
            Date now = new Date();
            String containerNo = lclEculineEdiForm.getContainerNo();
            BlInfoDao blDao = new BlInfoDao();
            LclUnitSsDAO unitSsDao = new LclUnitSsDAO();
            Long headerId = eculineEdi.getUnitSs().getLclSsHeader().getId();
            ContainerInfo container = new ContainerInfoDao().getContainer(containerNo, eculineEdi.getId());
            List<BlInfo> blInfoList = container.getBlInfoCollection();
            LclUnitSs unitSs = unitSsDao.getUnitSs(headerId, container.getCntrName());
            for (BlInfo bl : blInfoList) {
//                if (bl.getFileNo() != null && (!bl.isApproved())) {
//                    bl.setApproved(true);
//                } else {
                //***************Mapping Package Type*******************************
                eculineEdiBc.setMappingPackageType(bl);
                //************MAPPING OF ADDRESSES OF SHIPPER,CONSIGNEE & NOTIFY****
                eculineEdiBc.setMappingAddressses(bl);
                //************MAPPING OF UnLocation OF POL,POD & Delivery***********
                eculineEdiBc.setMappingUnLocationDesc(bl);
                //booking details
                List<LclBookingPiece> bkgPieces = eculineEdiBc.setBooking(eculineEdi, container, bl, user, request);
                eculineEdiBc.setBkgPieceUnit(eculineEdi, unitSs, bkgPieces, user);
                bl.setApproved(true);
//                }
                //set approved
                bl.setUpdatedByUser(user);
                bl.setUpdatedDate(now);
                bl.setCreatedByUser(user);
                bl.setCreatedDate(now);
                blDao.update(bl);
            }
            if (CommonUtils.isNotEmpty(lclEculineEdiForm.getAutoCostFlag()) && lclEculineEdiForm.getAutoCostFlag().equalsIgnoreCase("Y")) {
                eculineEdiBc.setUnitCost(unitSs, container, user, request);
            }
            //display all list
            request.setAttribute("approved", "Voyage # : " + voyNo + " approved successfully");
            SessionForm oldEculineEdiForm = (SessionForm) request.getSession().getAttribute("oldEculineEdiForm");
            PropertyUtils.copyProperties(lclEculineEdiForm, oldEculineEdiForm);
            request.setAttribute("lclEculineEdiForm", lclEculineEdiForm);
            return search(mapping, lclEculineEdiForm, request, response);
        }
    }

    public ActionForward approveBkg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        Date now = new Date();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        LclUnitSsDAO unitSsDao = new LclUnitSsDAO();
        EculineEdiBc eculineEdiBc = new EculineEdiBc();
        ContainerInfoDao containerDao = new ContainerInfoDao();
        BlInfoDao blDao = new BlInfoDao();
        BolUtils bolUtils = new BolUtils();

        String voyNo = lclEculineEdiForm.getVoyNo();
        Long blId = Long.parseLong(lclEculineEdiForm.getBlToApprove());
        EculineEdi eculineEdi = eculineEdiDao.getVoyDetails(voyNo, lclEculineEdiForm.getContainerNo());
        String ecuId = String.valueOf(eculineEdi.getId());
        User user = getCurrentUser(request);
        String containerNo = lclEculineEdiForm.getContainerNo();
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
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", ecuId);
        bolUtils.voyageDetails(request, values);
        return mapping.findForward("container");
    }

    public ActionForward updateContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        TradingPartnerDAO tpDao = new TradingPartnerDAO();
        WarehouseDAO warehouseDao = new WarehouseDAO();
        TerminalDAO terminalDao = new TerminalDAO();
        BolUtils bolUtils = new BolUtils();
        EculineEdi eculineEdi = eculineEdiDao.getVoyDetails(lclEculineEdiForm.getVoyNo(), lclEculineEdiForm.getContainerNo());
        RefTerminal terminal = null;
        if (null != lclEculineEdiForm.getTerminalNo()) {
            terminal = terminalDao.findByTerminalNo(lclEculineEdiForm.getTerminalNo());
        }
        if (null != eculineEdi) {
            List<ContainerInfo> containerList = eculineEdi.getContainerInfoCollection();
            for (ContainerInfo containerInfo : containerList) {
                containerInfo.setUpdatedByUser(getCurrentUser(request));
                containerInfo.setUpdatedDate(new Date());
                //*******************Automatically Mapping of container size/unit Size
                if (CommonUtils.isEmpty(lclEculineEdiForm.getUnitTypeId()) && CommonUtils.isNotEmpty(lclEculineEdiForm.getContSize()) && CommonUtils.isNotEmpty(lclEculineEdiForm.getEcuContSize())) {
                    UnitType unitType = new UnitTypeDAO().getUnitTypeByDesc(lclEculineEdiForm.getContSize());
                    if (null != unitType) {
                        EculineUnitTypeMapping eculineUnitTypeMapping = new EculineUnitTypeMapping();
                        eculineUnitTypeMapping.setCntrType(lclEculineEdiForm.getEcuContSize());
                        eculineUnitTypeMapping.setUnitType(unitType);
                        new EculineUnitTypeMappingDAO().save(eculineUnitTypeMapping);
                    }
                }
                //**************************************************************
                containerInfo.setCntrType(lclEculineEdiForm.getContSize());
                containerInfo.setSealNo(lclEculineEdiForm.getSealNo());
                containerInfo.setMasterBl(lclEculineEdiForm.getMasterBl().toUpperCase());
                containerInfo.setCntrRemarks(lclEculineEdiForm.getContainerRemarks());
                if (CommonUtils.isNotEmpty(lclEculineEdiForm.getAgentNo())) {
                    containerInfo.setAgent(tpDao.findById(lclEculineEdiForm.getAgentNo()));
                }
                if (CommonUtils.isNotEmpty(lclEculineEdiForm.getWarehouseNo())) {
                    containerInfo.setWarehouse(warehouseDao.findById(Integer.parseInt(lclEculineEdiForm.getWarehouseNo())));
                    containerInfo.setWarehouseAddress(lclEculineEdiForm.getWarehsAddress());
                }
            }
            if (CommonUtils.isNotEmpty(lclEculineEdiForm.getSslineNo())) {
                eculineEdi.setSsline(tpDao.findById(lclEculineEdiForm.getSslineNo()));
            }
            eculineEdi.setEta(bolUtils.format(lclEculineEdiForm.getArvlDate()));
            eculineEdi.setEtd(bolUtils.format(lclEculineEdiForm.getSailDate()));
            eculineEdi.setPolUncode(lclEculineEdiForm.getPolUncode());
            eculineEdi.setPodUncode(lclEculineEdiForm.getPodUncode());
            eculineEdi.setVesselName(lclEculineEdiForm.getVesselName());
            eculineEdi.setLloydsNo(lclEculineEdiForm.getLloydsNo());
            eculineEdi.setHeaderRef(lclEculineEdiForm.getRefNo());
            eculineEdi.setSenderCode(lclEculineEdiForm.getSender());
            eculineEdi.setSenderEmail(lclEculineEdiForm.getSenderEmail());
            eculineEdi.setReceiverCode(lclEculineEdiForm.getReceiver());
            eculineEdi.setReceiverEmail(lclEculineEdiForm.getReceiverEmail());
            eculineEdi.setTerminal(terminal);
            eculineEdi.setUpdatedByUser(getCurrentUser(request));
            eculineEdi.setUpdatedDate(new Date());
            eculineEdi.setVesselErrorCheck(lclEculineEdiForm.isVesselErrorCheck());
            eculineEdi.setDocReceived(CommonUtils.isNotEmpty(lclEculineEdiForm.getDocReceived())
                    ? bolUtils.format(lclEculineEdiForm.getDocReceived()) : null);
            eculineEdiDao.update(eculineEdi);
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("eculineEdiId", eculineEdi.getId());
            eculineEdiDao.adjudicate("AdjudicateEculineEdiById", values);
            String ecuId = String.valueOf(eculineEdi.getId());
            Map<String, String> filters = new HashMap<String, String>();
            filters.put("id", ecuId);
            bolUtils.voyageDetails(request, filters);
        }
        return mapping.findForward("container");
    }

    public ActionForward openEciVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String ecuId = request.getParameter("id");
        String headerId = request.getParameter("headerId");
        String unitId = request.getParameter("unitId");
        Map<String, String> values = new HashMap<String, String>();
        EculineEdiBc eculineEdiBc = new EculineEdiBc();
        final ActionForward action = mapping.findForward("eciVoyage");
        ActionForward redirectAction = new ActionForward();
        redirectAction.setName(action.getName());
        values.put("path", action.getPath());
        values.put("ecuId", ecuId);
        values.put("headerId", headerId);
        values.put("unitId", unitId);
        String redirectPath = eculineEdiBc.buildPath(values);
        redirectAction.setPath(redirectPath);
        redirectAction.setRedirect(true);
        return redirectAction;
    }

    public ActionForward openNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        request.setAttribute("eculineForm", lclEculineEdiForm);
        request.setAttribute("audits", eculineEdiDao.getNotesDetails(lclEculineEdiForm.getVoyNo(),
                lclEculineEdiForm.getContainerNo(), lclEculineEdiForm.getRefNo(), LclCommonConstant.REMARKS_TYPE_AUTO));
        return mapping.findForward("notes");
    }

    public ActionForward updateAgentValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        BolUtils bolUtils = new BolUtils();
        EculineEdi eculineEdi = eculineEdiDao.getVoyDetails(lclEculineEdiForm.getVoyNo(), lclEculineEdiForm.getContainerNo());
        String ecuId = String.valueOf(eculineEdi.getId());
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", ecuId);
        values.put("polCode", lclEculineEdiForm.getPolUncode());
        values.put("setAgentValFlag", lclEculineEdiForm.getSetAgentValFlag());
        bolUtils.voyageDetails(request, values);
        return mapping.findForward("container");
    }

    public ActionForward refreshContainerPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        BolUtils bolUtils = new BolUtils();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        EculineEdi eculineEdi = eculineEdiDao.getVoyDetails(lclEculineEdiForm.getVoyNo(), lclEculineEdiForm.getContainerNo());
        String ecuId = String.valueOf(eculineEdi.getId());
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", ecuId);
        bolUtils.voyageDetails(request, values);
        return mapping.findForward("container");
    }

    public ActionForward unLinkDR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEculineEdiForm lclEculineEdiForm = (LclEculineEdiForm) form;
        BlInfoDao blInfoDao = new BlInfoDao();
        BolUtils bolUtils = new BolUtils();
        EculineEdiDao eculineEdiDao = new EculineEdiDao();
        BlInfo blInfo = blInfoDao.findById(Long.parseLong(request.getParameter("blId")));
        blInfo.setFileNo(null);
        blInfoDao.update(blInfo);
        EculineEdi eculineEdi = eculineEdiDao.getVoyDetails(lclEculineEdiForm.getVoyNo(), lclEculineEdiForm.getContainerNo());
        String ecuId = String.valueOf(eculineEdi.getId());
        Map<String, String> values = new HashMap<String, String>();
        values.put("id", ecuId);
        bolUtils.voyageDetails(request, values);
        return mapping.findForward("container");
    }
}

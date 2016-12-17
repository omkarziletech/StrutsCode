/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.SedFilings;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.SedFilingsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.bc.fcl.SedFilingBC;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.SedSchedulebDetails;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAesDetailsForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Owner
 */
public class LclAesDetailsAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        SedFilings sedFilings = new SedFilings();
        Ports ports = null;
        PortsDAO portsDAO = new PortsDAO();
        String fileNumber = request.getParameter("fileNumber");
        String fileNumberId = request.getParameter("fileNumberId");
        Long fileId = Long.parseLong(fileNumberId);
        LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", fileId);
        bookingDAO.getCurrentSession().evict(lclBooking);
        List lclInbond = new LclInbondsDAO().executeQuery("from LclInbond where lclFileNumber.id=" + fileId + " order by id desc");
        if (CommonUtils.isNotEmpty(lclInbond) && lclInbond.size() > 0) {
            LclInbond Inbond = (LclInbond) lclInbond.get(0);
            sedFilings.setInbnd(Inbond.getInbondNo());
        }
        sedFilings.setEntrdt(new Date());
        sedFilings.setStatus("New");
        User user = (User) request.getSession().getAttribute("loginuser");
        sedFilings.setEntnam(user.getFirstName());
        if (lclBooking.getConsAcct() != null) {
            sedFilings.setConadd(lclBooking.getConsContact().getAddress());
            sedFilings.setConcty(lclBooking.getConsContact().getCity());
            sedFilings.setConnam(lclBooking.getConsContact().getCompanyName());
            sedFilings.setConpst(lclBooking.getConsContact().getState());
            lclAesDetailsForm.setConsZip(lclBooking.getConsContact().getZip());
            sedFilings.setConctry(lclBooking.getConsContact().getCountry());
            sedFilings.setConcpn(lclBooking.getShipContact().getPhone1());
            sedFilings.setConnum(lclBooking.getConsAcct().getAccountno());
            if (lclBooking.getConsAcct().getGeneralInfo() != null) {
                sedFilings.setConpoa(lclBooking.getConsAcct().getGeneralInfo().getPoa());
            }
        }
        if (lclBooking.getShipAcct() != null) {
            GeneralInformation generalInformation = new GeneralInformationDAO().getGeneralInformationByAccountNumber(lclBooking.getShipAcct().getAccountno());
            sedFilings.setExpadd(lclBooking.getShipContact().getAddress());
            sedFilings.setExpcty(lclBooking.getShipContact().getCity());
            sedFilings.setExpnam(lclBooking.getShipContact().getCompanyName());
            sedFilings.setExpsta(lclBooking.getShipContact().getState());
            sedFilings.setExpzip(lclBooking.getShipContact().getZip());
            sedFilings.setExpcpn(lclBooking.getShipContact().getPhone1());
            sedFilings.setExpnum(lclBooking.getShipAcct().getAccountno());
            if (lclBooking.getShipAcct().getGeneralInfo() != null) {
                sedFilings.setExppoa(lclBooking.getShipAcct().getGeneralInfo().getPoa());
            }
            if (generalInformation != null) {
                if (CommonUtils.isNotEmpty(generalInformation.getIdType())) {
                    if (generalInformation.getIdType().equals("E")) {
                        sedFilings.setExpicd(generalInformation.getIdType());
                        sedFilings.setExpirs(generalInformation.getIdText());
                    } else if (generalInformation.getIdType().equals("S")) {
                        sedFilings.setExpicd("T");
                        sedFilings.setExpirs(generalInformation.getIdText());
                    } else {
                        if (CommonUtils.isNotEmpty(generalInformation.getDunsNo())) {
                            sedFilings.setExpirs(generalInformation.getDunsNo());
                            sedFilings.setExpicd("D");
                        } else {
                            sedFilings.setExpirs(generalInformation.getIdText());
                        }
                    }
                } else {
                    if (CommonUtils.isNotEmpty(generalInformation.getDunsNo())) {
                        sedFilings.setExpirs(generalInformation.getDunsNo());
                        sedFilings.setExpicd("D");
                    } else {
                        sedFilings.setExpirs(generalInformation.getIdText());
                    }
                }
            }
        }
        sedFilings.setEmail(user.getEmail());
        if (lclBooking != null && lclBooking.getPortOfOrigin() != null && lclBooking.getPortOfOrigin().getStateId() != null) {
            sedFilings.setOrigin(lclBooking.getPortOfOrigin().getUnLocationName() + "/" + lclBooking.getPortOfOrigin().getStateId().getCode() + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
            sedFilings.setOrgsta(lclBooking.getPortOfOrigin().getStateId().getCode());
        } else {
            sedFilings.setOrigin(lclBooking.getPortOfOrigin().getUnLocationName() + "/" + "(" + lclBooking.getPortOfOrigin().getUnLocationCode() + ")");
        }
        sedFilings.setDestn(lclBooking.getFinalDestination().getUnLocationName() + "/" + lclBooking.getFinalDestination().getCountryId().getCodedesc() + "(" + lclBooking.getFinalDestination().getUnLocationCode() + ")");
        sedFilings.setCntdes(lclBooking.getFinalDestination().getCountryId().getCode());
        if (lclBooking.getPortOfDestination() != null) {
            lclAesDetailsForm.setPodId(lclBooking.getPortOfDestination().getId());
        }
        if (lclBooking.getPortOfLoading() != null) {
            lclAesDetailsForm.setPolId(lclBooking.getPortOfLoading().getId());
        }
        sedFilings.setDepdat(CommonUtils.isNotEmpty(lclAesDetailsForm.getDepDate()) ? DateUtils.parseDate(lclAesDetailsForm.getDepDate(), "MM/dd/yyyy") : null);
        if (lclBooking.getPortOfDestination() != null && lclBooking.getPortOfLoading() != null) {
            sedFilings.setPod(lclBooking.getPortOfDestination().getUnLocationName() + "/" + lclBooking.getPortOfDestination().getCountryId().getCodedesc() + "(" + lclBooking.getPortOfDestination().getUnLocationCode() + ")");
            ports = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfDestination().getUnLocationCode());
            if (ports != null && ports.getShedulenumber() != null && !ports.getShedulenumber().equals("")) {
                sedFilings.setUpptna(ports.getShedulenumber());
            }
            sedFilings.setPol(lclBooking.getPortOfLoading().getUnLocationName() + "/" + lclBooking.getPortOfLoading().getStateId().getCode() + "(" + lclBooking.getPortOfLoading().getUnLocationCode() + ")");
            ports = portsDAO.getByProperty("unLocationCode", lclBooking.getPortOfLoading().getUnLocationCode());
            if (ports != null && ports.getShedulenumber() != null && !ports.getShedulenumber().equals("")) {
                sedFilings.setExppnm(ports.getShedulenumber());
            }
        }
        sedFilings.setShpdr(lclBooking.getLclFileNumber().getFileNumber());
        LclSsHeader bookedHeader = lclBooking.getBookedSsHeaderId();

        String vesselName = "";
        String ssLineNo = "";

        ExportVoyageSearchModel pickedVoyageDetails = new LclUnitSsDAO().getPickedVoyageByVessel(lclBooking.getLclFileNumber().getId(), "E");
        if (pickedVoyageDetails != null && CommonUtils.isNotEmpty(pickedVoyageDetails.getUnitSsId())) {
            LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedVoyageDetails.getUnitSsId()));
            LclSsDetail ssDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            ssLineNo = ssDetail.getSpAcctNo().getSslineNumber();
            vesselName = ssDetail.getSpReferenceName();
            sedFilings.setVoyvoy(ssDetail.getSpReferenceNo());
            sedFilings.setUnitno(lclUnitSs.getLclUnit().getUnitNo());
            sedFilings.setDepdat(DateUtils.formatDateAndParseToDate(ssDetail.getStd()));
        } else if (null != bookedHeader && null != bookedHeader.getVesselSsDetail()) {
            LclSsDetail bookedSsDetail = bookedHeader.getVesselSsDetail();
            ssLineNo = bookedSsDetail.getSpAcctNo().getSslineNumber();
            vesselName = bookedSsDetail.getSpReferenceName();
            sedFilings.setVoyvoy(bookedSsDetail.getSpReferenceNo());
            sedFilings.setDepdat(DateUtils.formatDateAndParseToDate(bookedSsDetail.getStd()));
        }
        sedFilings.setVesnam(vesselName);
        if (null != vesselName && !"".equalsIgnoreCase(vesselName)) {
            String vesselNo = new GenericCodeDAO().getCodeByCodeType(vesselName, "Vessel Codes");
            sedFilings.setVesnum(null != vesselNo ? vesselNo : "");
        }
        if (null != ssLineNo && !"".equalsIgnoreCase(ssLineNo)) {
            String scacCode = new CarriersOrLineDAO().getCarrierScacCode(ssLineNo, "");
            sedFilings.setScac(null != scacCode ? scacCode : "");
        }

        int fileNo = new SedFilingsDAO().getSedCount(fileNumber);
        int fileNum = fileNo + 1;
        if (fileNo < 9) {
            sedFilings.setTrnref(lclBooking.getLclFileNumber().getFileNumber() + "-" + "0" + fileNum);
            sedFilings.setBkgnum(lclBooking.getLclFileNumber().getFileNumber() + "-" + "0" + fileNum);
        } else {
            sedFilings.setTrnref(lclBooking.getLclFileNumber().getFileNumber() + "-" + fileNum);
            sedFilings.setBkgnum(lclBooking.getLclFileNumber().getFileNumber() + "-" + fileNum);
        }
        request.setAttribute("lclAesDetailsForm", lclAesDetailsForm);
        request.setAttribute("sedFilings", sedFilings);
        return mapping.findForward("aesDetails");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        User thisUser = getCurrentUser(request);
        String fileNumber = request.getParameter("shpdr");
        if (lclAesDetailsForm.getSedFilings().getStatus().toUpperCase().equals("NEW")) {
            lclAesDetailsForm.getSedFilings().setStatus("N");
        } else if (lclAesDetailsForm.getSedFilings().getStatus().toUpperCase().equals("SENT")) {
            lclAesDetailsForm.getSedFilings().setStatus("S");
        } else if (lclAesDetailsForm.getSedFilings().getStatus().toUpperCase().equals("COMPLETED")) {
            lclAesDetailsForm.getSedFilings().setStatus("C");
        } else if (lclAesDetailsForm.getSedFilings().getStatus().toUpperCase().equals("PENDING")) {
            lclAesDetailsForm.getSedFilings().setStatus("P");
        }
        SedFilings sedFilings = lclAesDetailsForm.getSedFilings();
        if (sedFilings == null) {
            sedFilings = new SedFilings();
        }
        sedFilings.setEnteredBy(thisUser);
        sedFilings.setModifiedBy(thisUser);
        new SedFilingsDAO().saveAndReturn(sedFilings);
        if (fileNumber != null && !fileNumber.equals("")) {
            List<SedFilings> aesList = new ArrayList<SedFilings>();
            List list = new SedFilingsDAO().getSedFilingsList(fileNumber);
            if (null != list) {
                for (Object object : list) {
                    sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                    if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref()))) {
                        sedFilings.setSched(true);
                    }
                    aesList.add(sedFilings);
                }
            }
            request.setAttribute("aesDetailsList", list);
        }
        return mapping.findForward("aesList");
    }

    public ActionForward saveSchedBDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String fileNumber = lclAesDetailsForm.getSedFilings().getShpdr();
        String trnref = lclAesDetailsForm.getSedFilings().getTrnref();
        SedSchedulebDetails sedSchedulebDetails = lclAesDetailsForm.getSchedB();
        if (sedSchedulebDetails == null) {
            sedSchedulebDetails = new SedSchedulebDetails();
        }
        new SedSchedulebDetailsDAO().saveAndReturn(sedSchedulebDetails);
        List list = new SedSchedulebDetailsDAO().findByDr(fileNumber, trnref);
        request.setAttribute("trnref", trnref);
        request.setAttribute("shpdr", fileNumber);
        request.setAttribute("schedList", list);
        request.setAttribute("sed", list.size());
        request.setAttribute("exportCodeList", new SedFilingBC().getFieldList(69, "Export Code"));
        request.setAttribute("licenseCodeList", new SedFilingBC().getFieldList(67, "License Code"));
        request.setAttribute("stateCodeList", new SedFilingBC().getFieldList(70, "Vehicle State"));
        return mapping.findForward("schedBDetails");
    }

    public ActionForward editAes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String fileNumber = request.getParameter("trnref");
        String aesId = request.getParameter("id");
        int id = Integer.parseInt(aesId);
        SedFilings sedFilings = new SedFilingsDAO().findById(id);
        PropertyUtils.copyProperties(lclAesDetailsForm, sedFilings);
        if (sedFilings.getStatus().equals("N")) {
            sedFilings.setStatus("New");
        } else if (sedFilings.getStatus().equals("S")) {
            sedFilings.setStatus("Sent");
        }
        request.setAttribute("sedFilings", sedFilings);
        return mapping.findForward("aesDetails");
    }

    public ActionForward editSchedB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String fileNumber = request.getParameter("trnref");
        String shpdr = fileNumber.substring(0, fileNumber.indexOf('-'));
        request.setAttribute("shpdr", shpdr);
        String aesId = request.getParameter("id");
        int id = Integer.parseInt(aesId);
        SedSchedulebDetails schedB = new SedSchedulebDetailsDAO().findById(id);
        PropertyUtils.copyProperties(lclAesDetailsForm, schedB);
        request.setAttribute("schedB", schedB);
        List list = new SedSchedulebDetailsDAO().findByTrnref(fileNumber);
        request.setAttribute("schedList", list);
        request.setAttribute("sed", list.size());
        request.setAttribute("trnref", fileNumber);
        request.setAttribute("exportCodeList", new SedFilingBC().getFieldList(69, "Export Code"));
        request.setAttribute("licenseCodeList", new SedFilingBC().getFieldList(67, "License Code"));
        request.setAttribute("stateCodeList", new SedFilingBC().getFieldList(70, "Vehicle State"));
        request.setAttribute("newItemFlag", true);
        return mapping.findForward("schedBDetails");
    }

    public ActionForward displaySchedB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String fileNumber = request.getParameter("fileNumber");
        String trnref = request.getParameter("trnref");
        request.setAttribute("shpdr", fileNumber);
        request.setAttribute("trnref", trnref);
        request.setAttribute("exportCodeList", new SedFilingBC().getFieldList(69, "Export Code"));
        request.setAttribute("licenseCodeList", new SedFilingBC().getFieldList(67, "License Code"));
        request.setAttribute("stateCodeList", new SedFilingBC().getFieldList(70, "Vehicle State"));
        List list = new SedSchedulebDetailsDAO().findByDr(fileNumber, trnref);
        request.setAttribute("schedList", list);
        request.setAttribute("sed", list.size());
        return mapping.findForward("schedBDetails");
    }

    public ActionForward closeAes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String aesId = request.getParameter("id");
        int id = Integer.parseInt(aesId);
        if (CommonUtils.isNotEmpty(id)) {
            SedFilings sedFilings = new SedFilingsDAO().findById(id);
            List<SedSchedulebDetails> schedB = new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref());
            for (SedSchedulebDetails sed : schedB) {
                new SedSchedulebDetailsDAO().delete(sed);
            }
            new SedFilingsDAO().delete(sedFilings);
        }
        String fileNo = request.getParameter("shpdr");
        if (fileNo != null && !fileNo.equals("")) {
            List<SedFilings> aesList = new ArrayList<SedFilings>();
            List list = new SedFilingsDAO().getSedFilingsList(fileNo);
            if (null != list) {
                for (Object object : list) {
                    SedFilings sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                    if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref()))) {
                        sedFilings.setSched(true);
                    }
                    aesList.add(sedFilings);
                }
            }
            request.setAttribute("aesDetailsList", list);
        }
        return mapping.findForward("aesList");
    }

    public ActionForward deleteSchedB(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        int id = Integer.parseInt(request.getParameter("id"));
        if (CommonUtils.isNotEmpty(id)) {
            SedSchedulebDetails schedB = new SedSchedulebDetailsDAO().findById(id);
            new SedSchedulebDetailsDAO().delete(schedB);
        }
        String fileNo = request.getParameter("fileNumber");
        String trnref = request.getParameter("trnref");
        String shpdr = trnref.substring(0, trnref.indexOf('-'));
        request.setAttribute("shpdr", shpdr);
        List list = new SedSchedulebDetailsDAO().findByTrnref(trnref);
        request.setAttribute("exportCodeList", new SedFilingBC().getFieldList(69, "Export Code"));
        request.setAttribute("licenseCodeList", new SedFilingBC().getFieldList(67, "License Code"));
        request.setAttribute("stateCodeList", new SedFilingBC().getFieldList(70, "Vehicle State"));
        request.setAttribute("schedList", list);
        request.setAttribute("shpdr", shpdr);
        request.setAttribute("sed", list.size());
        return mapping.findForward("schedBDetails");
    }

    public ActionForward closeSched(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclAesDetailsForm lclAesDetailsForm = (LclAesDetailsForm) form;
        String fileNo = request.getParameter("shpdr");
        if (fileNo != null && !fileNo.equals("")) {
            List<SedFilings> aesList = new ArrayList<SedFilings>();
            List list = new SedFilingsDAO().getSedFilingsList(fileNo);
            if (null != list) {
                for (Object object : list) {
                    SedFilings sedFilings = (SedFilings) object;
                    sedFilings.setItnStatus(new LogFileEdiDAO().findITNStatusFileNo(sedFilings.getTrnref()));
                    if (CommonUtils.isNotEmpty(new SedSchedulebDetailsDAO().findByTrnref(sedFilings.getTrnref()))) {
                        sedFilings.setSched(true);
                    }
                    aesList.add(sedFilings);
                }
            }
            request.setAttribute("aesDetailsList", list);
        }
        return mapping.findForward("aesList");
    }
}

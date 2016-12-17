package com.logiware.domestic;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.reports.CTSBolPdfCreator;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.domestic.form.BookingForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import org.apache.log4j.Logger;

public class BookingAction extends LogiwareDispatchAction {

    private static final Logger logg = Logger.getLogger(BookingAction.class);

    public ActionForward newBooking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        if(CommonUtils.isNotEmpty(bookingForm.getBookingNumber())){
            User loginUser = (User) request.getSession().getAttribute("loginuser");
            List<DomesticBooking> bookList = new DomesticBookingDAO().searchDomesticBooking(loginUser, bookingForm);
            if(null != bookList){
                request.setAttribute("booking", (DomesticBooking)bookList.get(0));
            }
            request.setAttribute("domesticBookingList", bookList);
        }
        request.setAttribute("userNameList",  new UserDAO().getDomesticUser());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("domesticBookingList", new DomesticBookingDAO().searchDomesticBooking(loginUser, bookingForm));
        request.setAttribute("userNameList",  new UserDAO().getDomesticUser());
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editBooking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingForm.getBookingId()));
        List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingForm.getBookingId()));
        request.setAttribute("booking", domesticBooking);
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        return mapping.findForward(EDIT_BOOKING);
    }

    public ActionForward updateBooking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingForm.getBookingId()));
        updateBooking(domesticBooking, bookingForm);
        List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingForm.getBookingId()));
        request.setAttribute("booking", domesticBooking);
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        return mapping.findForward(EDIT_BOOKING);
    }
    public ActionForward editPurchaseOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        DomesticPurchaseOrder domesticPurchaseOrder = new DomesticPurchaseOrderDAO().findById(Integer.parseInt(bookingForm.getPurchaseOrderId()));
        DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingForm.getBookingId()));
        updateBooking(domesticBooking, bookingForm);
        List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingForm.getBookingId()));
        request.setAttribute("booking", domesticBooking);
        request.setAttribute("order", domesticPurchaseOrder);
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        return mapping.findForward(EDIT_BOOKING);
    }
    public ActionForward updatePurchaseOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        DomesticPurchaseOrder domesticPurchaseOrder = new DomesticPurchaseOrderDAO().findById(Integer.parseInt(bookingForm.getPurchaseOrderId()));
        DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingForm.getBookingId()));
        List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingForm.getBookingId()));
        updatePurchaseOrder(domesticPurchaseOrder, bookingForm,purchaseOrderList);
        request.setAttribute("booking", domesticBooking);
        request.setAttribute("purchaseOrderList", purchaseOrderList);
        return mapping.findForward(EDIT_BOOKING);
    }
    public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookingForm bookingForm = (BookingForm) form;
        DomesticBooking domesticBooking = new DomesticBookingDAO().findById(Integer.parseInt(bookingForm.getBookingId()));
        List<DomesticPurchaseOrder> purchaseOrderList = new DomesticPurchaseOrderDAO().findByProperty("bookingId.id", Integer.parseInt(bookingForm.getBookingId()));
        String fileLocation = LoadLogisoftProperties.getProperty("reportLocation") + "/DomesticBOL";
        File dir = new File(fileLocation);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = fileLocation + "/BOL_" + domesticBooking.getBookingNumber() + ".pdf";
        String contextPath = this.servlet.getServletContext().getRealPath("/");
        new CTSBolPdfCreator().createReport(domesticBooking,purchaseOrderList, fileName, contextPath);
        PrintWriter out = response.getWriter();
        out.print(fileName);
        out.flush();
        out.close();
        return null;
    }
    private void updateBooking(DomesticBooking domesticBooking, BookingForm bookingForm) {
        domesticBooking.setShipperName(bookingForm.getShipperName());
        domesticBooking.setShipperAddress1(bookingForm.getShipperAddress());
        domesticBooking.setShipperCity(bookingForm.getShipperCity());
        domesticBooking.setShipperState(bookingForm.getShipperState());
        domesticBooking.setShipperZipcode(bookingForm.getShipperZip());
        domesticBooking.setShipperContactName(bookingForm.getShipperContactName());
        domesticBooking.setShipperContactPhone(bookingForm.getShipperPhone());
        domesticBooking.setShipperContactFax(bookingForm.getShipperFax());
        domesticBooking.setShipperContactEmail(bookingForm.getShipperEmail());
        domesticBooking.setConsigneeName(bookingForm.getConsigneeName());
        domesticBooking.setConsigneeAddress1(bookingForm.getConsigneeAddress());
        domesticBooking.setConsigneeCity(bookingForm.getConsigneeCity());
        domesticBooking.setConsigneeState(bookingForm.getConsigneeState());
        domesticBooking.setConsigneeZipcode(bookingForm.getConsigneeZip());
        domesticBooking.setConsigneeContactName(bookingForm.getConsigneeContactName());
        domesticBooking.setConsigneeContactPhone(bookingForm.getConsigneePhone());
        domesticBooking.setConsigneeContactFax(bookingForm.getConsigneeFax());
        domesticBooking.setConsigneeContactEmail(bookingForm.getConsigneeEmail());
        domesticBooking.setCarrierName(bookingForm.getCarrierName());
        domesticBooking.setScac(bookingForm.getScac());
        domesticBooking.setOriginName(bookingForm.getOriginName());
        domesticBooking.setOriginCode(bookingForm.getOriginCode());
        domesticBooking.setOriginAddress(bookingForm.getOriginAddress());
        domesticBooking.setOriginCity(bookingForm.getOriginCity());
        domesticBooking.setOriginState(bookingForm.getOriginState());
        domesticBooking.setOriginZip(bookingForm.getOriginZip());
        domesticBooking.setOriginPhone(bookingForm.getOriginPhone());
        domesticBooking.setOriginFax(bookingForm.getOriginFax());
        domesticBooking.setDestinationName(bookingForm.getDestinationName());
        domesticBooking.setDestinationCode(bookingForm.getDestinationCode());
        domesticBooking.setDestinationAddress(bookingForm.getDestinationAddress());
        domesticBooking.setDestinationCity(bookingForm.getDestinationCity());
        domesticBooking.setDestinationState(bookingForm.getDestinationState());
        domesticBooking.setDestinationZip(bookingForm.getDestinationZip());
        domesticBooking.setDestinationPhone(bookingForm.getDestinationPhone());
        domesticBooking.setDestinationFax(bookingForm.getDestinationFax());
        domesticBooking.setBilltoName(bookingForm.getBilltoName());
        domesticBooking.setBilltoState(bookingForm.getBilltoState());
        domesticBooking.setBilltoCity(bookingForm.getBilltoCity());
        domesticBooking.setBilltoAddress1(bookingForm.getBilltoAddress());
        domesticBooking.setBilltoZipcode(bookingForm.getBilltoZip());
        domesticBooking.setShipperReference(bookingForm.getShipperReference());
        domesticBooking.setConsigneeReference(bookingForm.getConsigneeReference());
        domesticBooking.setCarrierNemonic(bookingForm.getCarrierNemonic());
        domesticBooking.setProNumber(bookingForm.getProNumber());
        try {
            new DomesticBookingDAO().update(domesticBooking);
        } catch (Exception ex) {
            logg.info("Exception on class BookingAction in method updateBooking" + new Date(), ex);
        }
    }

    private void updatePurchaseOrder(DomesticPurchaseOrder domesticPurchaseOrder, BookingForm bookingForm,List<DomesticPurchaseOrder> purchaseOrderList) {
        domesticPurchaseOrder.setPurchaseOrderNo(bookingForm.getPurchaseOrderNo());
        domesticPurchaseOrder.setPackageType(bookingForm.getPackageType());
        domesticPurchaseOrder.setPackageQuantity(bookingForm.getPackageQuantity());
        domesticPurchaseOrder.setWeight(null != bookingForm.getWeight()?Double.parseDouble(bookingForm.getWeight()):0d);
        domesticPurchaseOrder.setExtraInfo(bookingForm.getExtraInfo());
        domesticPurchaseOrder.setProductName(bookingForm.getProductName());
        domesticPurchaseOrder.setDescription(bookingForm.getDescription());
        domesticPurchaseOrder.setDescription(bookingForm.getDescription());
        domesticPurchaseOrder.setHazmatNumber(bookingForm.getHazmatNumber());
        domesticPurchaseOrder.setNmfc(bookingForm.getNmfc());
        domesticPurchaseOrder.setClasses(bookingForm.getClasses());
        domesticPurchaseOrder.setHandlingUnitType(bookingForm.getHandlingUnitType());
        domesticPurchaseOrder.setHandlingUnitQuantity(bookingForm.getHandlingUnitQuantity());
        domesticPurchaseOrder.setLength(bookingForm.getLength());
        domesticPurchaseOrder.setWidth(bookingForm.getWidth());
        domesticPurchaseOrder.setHeight(bookingForm.getHeight());
        try {
            new DomesticPurchaseOrderDAO().update(domesticPurchaseOrder);
            if(purchaseOrderList.size() > 1){
                for (DomesticPurchaseOrder order : purchaseOrderList) {
                    order.setPurchaseOrderNo(bookingForm.getPurchaseOrderNo());
                    new DomesticPurchaseOrderDAO().update(order);
                }
            }
        } catch (Exception ex) {
            logg.info("Exception on class BookingAction in method updatePurchaseOrder" + new Date(), ex);
        }
    }
}

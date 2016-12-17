package com.gp.cvst.logisoft.struts.lcl.kn.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope;
import com.gp.cong.logisoft.hibernate.dao.lcl.kn.BookingEnvelopeDao;
import com.gp.cong.logisoft.kn.beans.BookingBean;
import com.gp.cong.logisoft.lcl.kn.bc.BookingUtils;
import com.gp.cong.logisoft.lcl.kn.bc.CustomerBookingCSVCreator;
import com.gp.cong.logisoft.lcl.kn.bc.PDFUtils;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cvst.logisoft.struts.lcl.kn.form.SearchForm;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Palraj
 */
public class SearchAction extends LogiwareDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        SearchForm searchForm = (SearchForm) form;
        BookingEnvelopeDao bookingEnvelopeDao = new BookingEnvelopeDao();
//        BookingUtils bookingUtils = new BookingUtils();
//        Map<String, String> filters = bookingUtils.getSearchFilters(searchForm);
        List<BookingBean> bookings = bookingEnvelopeDao.search(searchForm);
        if (session != null) {
            session.setAttribute("bookings", bookings);
        }
        request.setAttribute("bookings", bookings);
        request.setAttribute("searchForm", searchForm);
        return mapping.findForward("success");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        SearchForm searchForm = (SearchForm) form;
        BookingEnvelopeDao bookingEnvelopeDao = new BookingEnvelopeDao();
        List<BookingBean> bookings = bookingEnvelopeDao.search(searchForm);
        if (session != null) {
            session.setAttribute("bookings", bookings);
        }
        request.setAttribute("bookings", bookings);
        request.setAttribute("searchForm", searchForm);
        return mapping.findForward("success");
    }

    public ActionForward reProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchForm searchForm = (SearchForm) form;
        BookingUtils bookingUtils = new BookingUtils();
        BookingEnvelopeDao bookingEnvelopeDao = new BookingEnvelopeDao();
        BookingEnvelope bkgEnvelope = bookingEnvelopeDao.searchById(Long.parseLong(searchForm.getBookingEnvelopeId()));
        bookingUtils.persistIntoWebtools(bkgEnvelope);
        List<BookingBean> bookings = bookingEnvelopeDao.search(null);
        request.setAttribute("bookings", bookings);
        request.setAttribute("searchForm", searchForm);
        return mapping.findForward("success");
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("bookings");
        }
        request.setAttribute("bookingType", "bookings");
        return mapping.findForward("success");
    }

    public ActionForward showFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        BookingEnvelope booking = new BookingEnvelopeDao().searchById(Long.parseLong(id));
        String fileName = type.equals("br") ? booking.getBookingRequestFileName() : "Booking_Confirmation";
        byte[] file = type.equals("br") ? booking.getBookingRequestFile() : booking.getBookingConfirmationFile();
        response.addHeader("Content-Disposition", "inline; filename=" + fileName);
        String contentType = CommonUtils.getMimeType(fileName, file);
        response.setContentType(contentType + ";charset=utf-8");
        IOUtils.write(file, response.getOutputStream());
        return null;
    }

    public ActionForward showPDF(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        PDFUtils pdfUtils = new PDFUtils();
        pdfUtils.createPDF(request, response, id);
        return null;
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        PrintWriter out = response.getWriter();
        try {
            List<BookingBean> bookings = (List<BookingBean>) request.getSession().getAttribute("bookings");
            String fileName = new CustomerBookingCSVCreator().createCSV(user, bookings);
            out.print(fileName);
        } catch (Exception e) {
            throw e;
        } finally {
            out.flush();
            out.close();
        }
        return null;
    }
}

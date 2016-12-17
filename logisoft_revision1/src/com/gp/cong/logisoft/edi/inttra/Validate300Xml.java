package com.gp.cong.logisoft.edi.inttra;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Balaji.E
 */
public class Validate300Xml {

    public String validateXml(String fileNo, String createOrChange, HttpServletRequest request) throws Exception {
        EdiDAO ediDAO = new EdiDAO();
        StringBuilder errorMessage = new StringBuilder();
        String polCode = "";
        String porName = "";
        String porCode = "";
        String plodCode = "";
        String moveType = "";
        String nvoMove = "";
        String docIdentifier = "";
        String podCode = "";
        String scac = "";
        String carrierScac = "";

        try {
            Properties prop = new Properties();
            BookingFclDAO bookingFclDAO = new BookingFclDAO();
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            BookingFcl booking = bookingFclDAO.findbyFileNo(fileNo);
            String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
            docIdentifier = "04" + fileNo;
            moveType = booking.getLineMove();
            nvoMove = booking.getMoveType();
            if (isNotNull(booking.getOriginTerminal()) && booking.getOriginTerminal().contains("/")) {
                if (booking.getOriginTerminal().lastIndexOf("(") != -1 && booking.getOriginTerminal().lastIndexOf(")") != -1) {
                    porCode = booking.getOriginTerminal().substring(booking.getOriginTerminal().lastIndexOf("(") + 1, booking.getOriginTerminal().lastIndexOf(")"));;
                }
            }
            if (createOrChange.equals("change")) {
                if (ediDAO.isValidForCancelOrAmendment(fileNo,"301")) {
                    errorMessage.append("--> Cannot Send Amendment Booking Request Before Booking Confirmation(301)<br>");
                }
            }
            if (moveType.equals("DOOR TO DOOR") || moveType.equals("DOOR TO PORT") || moveType.equals("DOOR TO RAIL")) {
                if (!isNotNull(porCode)) {
                    errorMessage.append("--> Please Enter Place Of Receipt<br>");
                }
            }

            if (isNotNull(booking.getPortofOrgin()) && booking.getPortofOrgin().contains("/")) {
                if (booking.getPortofOrgin().lastIndexOf("(") != -1 && booking.getPortofOrgin().lastIndexOf(")") != -1) {
                    polCode = booking.getPortofOrgin().substring(booking.getPortofOrgin().lastIndexOf("(") + 1, booking.getPortofOrgin().lastIndexOf(")"));;
                }
            }

            if (null == booking.getCargoReadyDate() || (null != booking.getCargoReadyDate() && booking.getCargoReadyDate().toString().isEmpty())) {
                errorMessage.append("--> Please Enter Cargo ready Date<br>");
            }
            if (!isNotNull(polCode)) {
                errorMessage.append("--> Please Enter Port Of Loading<br>");
            }

            if (isNotNull(booking.getDestination()) && booking.getDestination().contains("/")) {
                if (booking.getDestination().lastIndexOf("(") != -1 && booking.getDestination().lastIndexOf(")") != -1) {
                    podCode = booking.getDestination().substring(booking.getDestination().lastIndexOf("(") + 1, booking.getDestination().lastIndexOf(")"));
                }
            }

            if (isNotNull(booking.getPortofDischarge()) && booking.getPortofDischarge().contains("/")) {
                if (booking.getPortofDischarge().lastIndexOf("(") != -1 && booking.getPortofDischarge().lastIndexOf(")") != -1) {
                    plodCode = booking.getPortofDischarge().substring(booking.getPortofDischarge().lastIndexOf("(") + 1, booking.getPortofDischarge().lastIndexOf(")"));
                }
            }

            if (moveType.equals("DOOR TO DOOR") || moveType.equals("PORT TO DOOR") || moveType.equals("RAIL TO DOOR")) {
                if (!isNotNull(plodCode)) {
                    errorMessage.append("--> Please Enter Place Of Delivery<br>");
                }
            }
            boolean isAllowedType=true;
            if (!moveType.equals("00") && !moveType.equals("PORT TO PORT") && !moveType.equals("RAIL TO PORT") && !moveType.equals("DOOR TO PORT")) {
                errorMessage.append("--> EDI Allowed only for PORT TO PORT, RAIL TO PORT, DOOR TO PORT Line Move Types<br>");
                isAllowedType=false;
            }
            
            if(!moveType.equals("PORT TO PORT") && null==booking.getEtd()){
                errorMessage.append("--> Please Select ETD <br>");
            }
            
            if (isNotNull(booking.getLineMove()) && !"00".equals(booking.getLineMove())) {
                if (isAllowedType && booking.getLineMove().startsWith("DOOR")) {
                    if (isNotNull(nvoMove) && !nvoMove.startsWith("DOOR")) {
                        errorMessage.append("--> Please Select NVO Move type Starts with DOOR <br>");
                    }
                    if (booking.getPositioningDate() == null || booking.getPositioningDate() != null && booking.getPositioningDate().toString().isEmpty()
                            || booking.getLoadcontact() == null || booking.getLoadcontact() != null && booking.getLoadcontact().isEmpty()
                            || booking.getLoadphone() == null || booking.getLoadphone() != null && booking.getLoadphone().isEmpty()
                            || booking.getSpottingAccountName() == null || booking.getSpottingAccountName() != null && booking.getSpottingAccountName().isEmpty()
                            || booking.getSpottingAccountNo() == null || booking.getSpottingAccountNo() != null && booking.getSpottingAccountNo().isEmpty()
                            || booking.getAddressForExpPositioning() == null || booking.getAddressForExpPositioning() != null && booking.getAddressForExpPositioning().isEmpty()) {
                        
                        errorMessage.append("--> Please Enter Spotting date,Spotting Contact,Spotting Phone,Spotting Address<br>");
                    }

                }
            } else {
                errorMessage.append("--> Please Select LineMove<br>");
            }
            if (!isNotNull(booking.getGoodsDescription())) {
                errorMessage.append("--> Please Enter Goods Description<br>");
            }
            if (createOrChange.equals("create")){
                if(isNotNull(booking.getSSBookingNo())){
                    errorMessage.append("--> Please Clear SS Bkg # Field <br>");
                }
                if(isNotNull(booking.getVessel())){
                    errorMessage.append("--> Please Clear Vessel Field<br>");
                }
                if(isNotNull(booking.getVoyageCarrier())){
                    errorMessage.append("--> Please Clear SS Voy Field <br>");
                }
            }
            if (isNotNull(booking.getHazmat()) && booking.getHazmat().equalsIgnoreCase("Y") && booking.getHazmatSet().isEmpty()) {
                errorMessage.append("--> Please Enter Un Number,Shipping Name,IMOClassCode for Hazmat <br>");
            }
            if (booking.getGoodsDescription().length() > 1024) {
                errorMessage.append("--> Goods Description Cannot be more than 1024 Charcters <br>");
            }
            if (CommonUtils.isExcludeEdiCharacter(booking.getGoodsDescription(), excludeCharcter)) {
                errorMessage.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from Good Description<br>");
            }
            HttpSession session = request.getSession(true);
            User user = (User) session.getAttribute("loginuser");
            User userInfo = new UserDAO().getUserInfo(user.getUserId());
            if (!isNotNull(userInfo.getEmail()) && !isNotNull(userInfo.getTelephone()) && !isNotNull(user.getFax())) {
                errorMessage.append("--> Please Enter User Email or Fax or Phone <br>");
            }

            if (isNotNull(booking.getSSLine())) {
                carrierScac = ediDAO.getSsLine(booking.getSSLine());
                if (null != carrierScac && !carrierScac.trim().equals("") && !carrierScac.trim().equals("00000")) {
                    scac = ediDAO.getScacOrContract(carrierScac, "SCAC");
                }
            }

            if (!isNotNull(scac)) {
                errorMessage.append("--> Carrier Scac Code(SSLINE) is not matching<br>");
            } else if (scac.length() < 2 || scac.length() > 4) {
                errorMessage.append("--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>");
            }
            if (new BookingfclUnitsDAO().isBookingWithoutContainer(booking.getBookingNumber())) {
                errorMessage.append("--> Please select at least one Container in Cost & Charges Tab<br>");
            }

            if (isNotNull(errorMessage.toString())) {
                return "<span color: #000080;font-size: 10px;>Error Message</span><br>" + errorMessage.toString();
            } else {
                return "No Error";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isNotNull(String field) {
        return null != field && !field.trim().equals("");
    }
}

package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.kn.beans.Booking;
import com.gp.cong.logisoft.kn.beans.BookingHaz;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import org.apache.log4j.Logger;
import org.semanticdesktop.aperture.util.DateUtil;

/**
 *
 * @author Rajesh
 */
public class CSVUtils {

    private static Logger log = Logger.getLogger(CSVUtils.class);

    public synchronized void createBookingCSV(String fileName, Booking bkg) {
        FileWriter bookingWriter = null;
        try {
            bookingWriter = new FileWriter(fileName);
            bookingWriter.append(createBookingBody(bkg));
        } catch (IOException exception) {
            log.error(exception);
        } catch (Exception exception) {
            log.error(exception);
        } finally {
            try {
                bookingWriter.close();
            } catch (IOException exception) {
                log.error(exception);
            }
        }
    }

    public void createHazCSV(String fileName, List<BookingHaz> bkgHazes, String... values) {
        FileWriter bookingWriter = null;
        try {
            bookingWriter = new FileWriter(fileName);
            bookingWriter.append(createHazBody(bkgHazes, values));
        } catch (IOException exception) {
            log.error(exception);
        } finally {
            try {
                bookingWriter.close();
            } catch (IOException exception) {
                log.error(exception);
            }
        }
    }

    private StringBuilder createBookingBody(Booking bkg) throws Exception {
        StringBuilder bkgBuilder = new StringBuilder();
        Format decimalFormat = new DecimalFormat("0000.000");
        String flashPoint = bkg.getFlshpt();
        String logCompany = bkg.getLogcmp();
        Integer fPoint = null != flashPoint ? Integer.parseInt(flashPoint) : 0;
        Integer logCmp = null != logCompany ? Integer.parseInt(logCompany) : 0;
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getOriginTerminal())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getDockReceiptNo())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getBkgTerminal())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPortNo())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getVoyageNumber())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getBilling())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShpref())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperCar())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperAddress1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperAddress2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperCity())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperState())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperCountry())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperPos())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperPhone())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShipperFax())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdName())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdCar())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdAddress1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdAddress2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdCity())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdState())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdCountry())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdPos())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdPhone())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFwdFax())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConName())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConCar())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConAddress1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConAddress2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConCity())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConState())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConCountry())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConPos())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConPhone())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConFax())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyName())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyCar())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyAddress1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyAddress2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyCity())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyState())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyCountry())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyzip())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyPhone())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getNotifyFax())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%05d", bkg.getPc1()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%05d", bkg.getPc2()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%05d", bkg.getPc3()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%05d", bkg.getPc4()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPct1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPct2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPct3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPct4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getWgt1()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getWgt2()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getWgt3()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getWgt4()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getWtp1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getWtp2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getWtp3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getWtp4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getMsr1()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getMsr2()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getMsr3()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(bkg.getMsr4()))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getMtp1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getMtp2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getMtp3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getMtp4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCm1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCm2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCm3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCm4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getHazFlag())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPickupFlag())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getBonded())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getLicnsd())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getRtngds())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getElcexd())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getXtnnm1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getXtnnm2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPshpnm())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getTchmnm())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getHazClass())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getUnnumb())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPkggrp())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%04d", fPoint))).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFlshtm())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getOutpkg())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getInnpkg())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getOthpk1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getOthpk2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getLmtqty())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getMarplt())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getRptqty())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getInhhaz())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getInhzon())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getEmgcon())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getAttch1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getAttch2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getAttch3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getLogip())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getLogses())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getLogid())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(String.format("%05d", logCmp))).append("\",");
        bkgBuilder.append("\"").append(DateUtils.formatDate(bkg.getLogtme(), "yyyy-MM-dd HH:mm:ss")).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmpnum())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getContact())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPucomp())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPuadr1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPucity())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPustat())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPuzipc())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote("")).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPuphon())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPufax())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPuhrs())).append("\",");
        bkgBuilder.append("\"").append(DateUtils.formatDate(bkg.getPudate(), "yyyy-MM-dd")).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPucmmt())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getEcquot())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getEcqtnm())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmmnt1())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmmnt2())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmmnt3())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmmnt4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmmnt5())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCmptyp())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getCnteml())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getOvdims())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getEcidte())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPoo())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPol())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getPod())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFd())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getGr())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getFrtref())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getShpref())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(bkg.getConref())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCarrier())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getScac())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getDays())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getToZipCode())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getPuEmail())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getComNum1())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getComNum2())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getComNum3())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getComNum4())).append("\",");
        bkgBuilder.append("\"").append(replaceDoubleQuote(decimalFormat.format(1999))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getMsRate2()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getMsRate3()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getMsRate4()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getWtRate1()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getWtRate2()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getWtRate3()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getWtRate4()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode1())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode2())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode3())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode4())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode5())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode6())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode7())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode8())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode9())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode10())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode11())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCcode12())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt1()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt2()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt3()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt4()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt5()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt6()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt7()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt8()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt9()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt10()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt11()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(decimalFormat.format(bkg.getCamnt12()))).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getName10())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getUpdatedUser())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getUpdatedDate())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getUpdatedTime())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getStatus())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getPoNumber())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getNcNumber1())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getNcNumber2())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getNcNumber3())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getNcNumber4())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getNcNumber5())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getTemplateNumber())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getDocCharges())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getType())).append("\",");
        bkgBuilder.append("\"").append(0.00).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCfclPort())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getYesInbond())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getCommodityTariff())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getRateType())).append("\",");
        bkgBuilder.append("\"").append(replaceEmptyOnNull(bkg.getDrLabel())).append("\",");
        return bkgBuilder;
    }

    private StringBuilder createHazBody(List<BookingHaz> bkgHazes, String... values) {
        StringBuilder hazBuilder = new StringBuilder();
        for (int index = 0; index < bkgHazes.size(); index++) {
            BookingHaz bkgHaz = bkgHazes.get(index);
            if (index != 0) {
                hazBuilder.append("\n");
            }
            hazBuilder.append("\"").append(replaceDoubleQuote(values[1])).append("\","); // origin terminal no
            hazBuilder.append("\"").append(replaceDoubleQuote(values[0])).append("\","); // webtools booking number
            hazBuilder.append("\"").append(replaceEmptyOnNull(String.format("%3d", index))).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getUnNumber())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getProperShippingName())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getTechChemicalName())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getHazClass())).append("\",");
            hazBuilder.append("\"").append(bkgHaz.getImoSubsidiaryClass()).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getPackageGroup())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getFlashPoint())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getOuterPackPiece())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getOuterPackComp())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getOuterPackType())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInnerPackPiece())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInnerPackComp())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInnerPackType())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInnerPackWgtVol())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInnerPackUm())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getTotalNetWgt())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getTotalGrossWgt())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getTotalVol())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getEmergencyContact())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getEmergencyPhoneNo())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getEmsCode())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getImoSecondaryClass())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getFlashPointFlag())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getReportTableQuantity())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getMarinePollutant())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getExpectedQuantity())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLimitedQuantity())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInhalatHaz())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getInhalatHaz())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getResidue())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getFreeFormat())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine1())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine2())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine3())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine4())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine5())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine6())).append("\",");
            hazBuilder.append("\"").append(replaceEmptyOnNull(bkgHaz.getLine7())).append("\",");
        }
        return hazBuilder;
    }

    private String replaceDoubleQuote(String value) {
        value = (null != value) ? value.replace("\"", "'") : "";
        return value;
    }

    private String replaceEmptyOnNull(String value) {
        value = (null != value) ? value.replace("\"", "'") : "";
        return value;
    }
}

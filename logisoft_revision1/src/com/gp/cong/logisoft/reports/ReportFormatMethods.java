package com.gp.cong.logisoft.reports;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

public class ReportFormatMethods extends PdfPageEventHelper {

    public Font blackBoldFont = new Font(1, 9.0F, 0, Color.BLACK);
    public Font whiteBoldFont = new Font(1, 9.0F, 0, Color.WHITE);
    public Font blackBoldFont2 = new Font(1, 9.0F, 1, Color.BLACK);
    public Font RedBoldFont = new Font(1, 9.0F, 1, Color.RED);
    public Font blackBoldFont8 = new Font(1, 8.0F, 1, Color.BLACK);
    public Font redBoldFont2 = new Font(1, 9.0F, 1, Color.RED);
    public Font redNormalFont7 = new Font(1, 7.0f, 0, Color.RED);
    public Font redBoldFont7 = new Font(1, 7.0f, 1, Color.RED);
    public Font blackBoldFontForHeading = new Font(1, 18.0F, 1, Color.BLACK);
    public Font blackBoldFontForHeadingNormal = new Font(1, 13.0F, 0, Color.BLACK);
    public Font blackBoldFontForHeadingFcLBL = new Font(1, 20.0F, 0, Color.BLACK);
    public Font RedBoldFontForHeading = new Font(1, 20.0F, 0, Color.RED);
    public Font greenBoldFont2 = new Font(1, 9.0F, 1, Color.decode("#114F16"));
    public Font blackBoldFont5 = new Font(1, 9.0F, 1, Color.BLACK);
    public Font redBoldFont5 = new Font(1, 9.0F, 1, Color.RED);
    public Font blackBoldFontSize6 = new Font(1, 6.0F, 1, Color.BLACK);
    public Font blackBoldFontSize7 = new Font(1, 7.0F, 1, Color.BLACK);
    public Font blackBoldGreyFont = new Font(1, 10.0F, 0, Color.LIGHT_GRAY);
    public Font blackBoldFont1 = new Font(1, 10.0F, 1, Color.BLACK);
    public Font blackBoldFontheading = new Font(1, 14.0F, 1, Color.BLACK);  
    public Font blackBoldFont3 = new Font(1, 10.0F, 0, Color.BLACK);
    public Font blackBoldFont7 = new Font(1, 8.0F, 0, Color.BLACK);
    public Font blackBoldFont6 = new Font(1, 11.0F, 1, Color.BLACK);
    public Font greenBoldFont3 = new Font(1, 10.0F, 0, Color.BLACK);
    public Font blackBoldFontItalic = new Font(1, 10.0F, 2, Color.BLACK);
    public Font blackBoldFont4 = new Font(1, 13.0F, 0, Color.BLACK);
    public Font blackBoldFontWithUnderline = new Font(1, 9.0F, 4, Color.black);
    public Font headingFont = new Font(1, 10.0F, 1, Color.BLACK);
    public Font fileNoFont = new Font(1, 10.0F, 1, Color.BLUE);
    public Font smallHeadingFont = new Font(1, 8.0F, 0, Color.BLACK);
    public Font verySmallHeadingFont = new Font(1, 6.0F, 1, Color.BLACK);
    public Font headingFontSize8 = new Font(1, 8.0F, 1, Color.BLACK);
    public Font headingFontSize9 = new Font(1, 9.0F, 1, Color.BLACK);
    public Font smallBlackFontForAR = new Font(1, 6.0F, 1, Color.BLACK);
    public Font smallRedFontForAR = new Font(1, 6.0F, 0, Color.RED);
    public Font headingFontForAR = new Font(1, 16.0F, 1, Color.BLACK);
    public Font headingFont1 = new Font(1, 14.0F, 0, Color.BLACK);
    public Font headingFont3 = new Font(1, 12.0F, 1, Color.decode("#114F16"));
    public Font headingFontRed = new Font(1, 13.0F, 1, Color.RED);
    public Font blackNormalEnlargedFont = new Font(1, 15.0F, 0, Color.BLACK);
    public Font titleFont = new Font(1, 12.0F, 1, Color.BLACK);
    public Font blackBoldHeadingFont = new Font(1, 7.0F, 1, Color.BLACK);
    public Font blackFont = new Font(1, 9.0F, 0, Color.BLACK);
    public Font redFont = new Font(1, 9.0F, 0, Color.RED);
    public Font smallBlackFont = new Font(1, 6.0F, 0, Color.BLACK);
    public Font smallRedFont = new Font(1, 6.0F, 0, Color.RED);
    public Font blackFontForAR = new Font(1, 8.0F, 0, Color.BLACK);
    public Font redFontForAR = new Font(1, 8.0F, 0, Color.RED);
    public Font blackFontForFclBl = new Font(1, 8.0F, 1, Color.BLACK);
    public Font blackFontForFclAr = new Font(1, 7.5F, 1, Color.BLACK);
    public Font blackFontBold = new Font(1, 8.0F, 1, Color.BLACK);
    public Font RedFontForAR = new Font(1, 8.0F, 1, Color.RED);
    public Font GreenFont8 = new Font(1, 8.0F, 1, Color.decode("#114F16"));
    public Font GreenFont6 = new Font(1, 6.0F, 1, Color.decode("#114F16"));
    public Font redFontSize6 = new Font(1, 6.0F, 0, Color.RED);
    public Font blackFontSize6 = new Font(1, 6.0F, 0, Color.BLACK);
    public Font boldHeadingFont = new Font(1, 20.0F, 5, Color.LIGHT_GRAY);
    public Font customerStatementFont = new Font(1, 9.0F, 2, Color.BLACK);
    public Font customerStatementBoldFont = new Font(1, 9.0F, 1, Color.BLACK);
    public Font customerStatementArialBoldFont = new Font(1, 10.0F, 1, Color.BLACK);
    public Font customerStatementTimesBoldFont = new Font(2, 11.0F, 1, Color.BLACK);
    public Font textFontForPayment = new Font(1, 7.0F, 0, Color.BLACK);
    public Font textFontForBatch = new Font(1, 9.0F, 0, Color.BLACK);
    public Font headingFontForBLURL = new Font(1, 7.0F, 1, Color.BLUE);
    public Font headingFontForBL = new Font(1, 14.0F, 0, Color.BLACK);
    public Font headingFontForBLConfirmOnBoard = new Font(0, 18.0F, 1, Color.BLACK);
    public Font labelForConfirmOnBoard = new Font(0, 12.0F, 0, Color.BLACK);
    public Font boldLabelForConfirmOnBoard = new Font(0, 12.0F, 1, Color.BLACK);
    public Font italicLabelForPrint = new Font(1, 10.0F, 3, Color.BLACK);
    public Font dataFontForBL = new Font(1, 8.0F, 0, Color.BLACK);
    public Font redBoldFont = new Font(1, 9.0F, 0, Color.RED);
    public Font checkPrintingFont = new Font(1, 10.0F, 0, Color.BLACK);
    public Font addressFont = new Font(1, 9.0F, 0, Color.BLACK);
    public Font checkPrintingFont11 = new Font(1, 11.0F, 0, Color.BLACK);
    public Font italicFontTextDisplay = new Font(1, 9.0F, 2, Color.BLACK);
    public Font blackBoldFontForHeadingFcLBL1 = new Font(1, 10F, 0, Color.BLACK);
    public Font bottomTextFontSub = new Font(1, 9F, Font.NORMAL, Color.BLACK);
    public Font bottom3TextFontHead = new Font(1, 11F, 0, Color.BLACK);;
    public Font bottomTextFontSubLin =new Font(Font.HELVETICA, 6, Font.NORMAL, Color.BLACK);
    public Font blackNormalCourierFont8f = new Font(Font.COURIER, 9f, Font.NORMAL);
    public Font blackBoldFontBlue = new Font(1, 11.0F, 1, Color.BLUE);
    float[] twoColumnDefinitionSize = {50.0F, 50.0F};
    float[] threeColumnDefinitionSize = {33.330002F, 33.330002F, 33.330002F};
    float[] fourColumnDefinitionSize = {26.0F, 29.0F, 15.0F, 30.0F};
    NumberFormat numb = new DecimalFormat("###,###,##0.00");
    SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");

    public PdfPTable makeTable(int rows) {
        PdfPTable table = new PdfPTable(rows);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setBorderWidth(0.0F);
        return table;
    }

    public PdfPCell makeCell(Phrase phrase, int alignment) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(0);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValue(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValueRedFont(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.RedBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftValueRedFont(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(text, this.RedBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValueSmallFont(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.blackBoldFont8);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValueSmallFontWithoutColon(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(text, this.blackBoldFont8);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValueGreen(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.greenBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderValue(Date text) {
        String date = "";
        if (text != null) {
            date = this.dateformat.format(text);
        }
        Phrase phrase = new Phrase(": " + date, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightLeftBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightLeftBottomBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightBottomBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightBottomBorderRedFont(String text) {
        Phrase phrase = new Phrase(text, this.RedFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightWithRightLeftBottomBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderBold(String text) {
        Phrase phrase = new Phrase(text, this.blackFontBold);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderWhiteFont(String text) {
        Phrase phrase = new Phrase(text, this.whiteBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorder8Font(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setPaddingRight(2.0F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderWithBoldfont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont6);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderBoldfont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderWithBigFont(String text) {
        Phrase phrase = new Phrase(text, this.blackNormalEnlargedFont);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderWithGreyBackground(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellRightWithBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenter(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont1);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }
    public PdfPCell makeCellCenterWithBoldFontUnderline(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontheading);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthBottom(0.1F);
        return cell;
    }

    public PdfPCell makeCellLeftWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont1);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.headingFont3);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForHeading(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontForHeading);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForHeadingWithNormalFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontForHeadingNormal);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForHeadingFclBL(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontForHeadingFcLBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }
    public PdfPCell makeCellleftNoBorderForHeadingFclBLWithBlack(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontForHeadingFcLBL1);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForHeadingWithRedFont(String text) {
        Phrase phrase = new Phrase(text, this.RedBoldFontForHeading);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderFontSize6(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontSize6);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderFontSize6(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontSize6);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderFontSize7(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontSize7);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorder1(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterNoBorderWithBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderWithSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont7);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderWithBoldFontWithGreen(String text) {
        Phrase phrase = new Phrase(text, this.greenBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderBoldFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderWithBoldFontWithItalic(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontItalic);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderWithBoldFont4(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont4);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftWithColor(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellCenterNoBorder(String text) {
        PdfPCell cell = makeCellCenter(text);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBorder(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithBorderGray(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderColor(Color.GRAY);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellrightwithBorderGray(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderColor(Color.GRAY);
        cell.setPadding(4.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellwithBorder(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.textFontForPayment);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderSmallFontCorier(String text) {
        Phrase phrase = new Phrase(text, new Font(0, 8.0F, 0, Color.BLACK));
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderHeadingFont(String text) {
        Phrase phrase = new Phrase(text, this.titleFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderHeadingFontFclBl(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderHeadingFontFclBlConrimOnBoard(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBLConfirmOnBoard);
        PdfPCell cell = makeCell(phrase, 1);

        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderValue(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBottomBorderValue(Date text) {
        String date = "";
        if (text != null) {
            date = String.valueOf(text);
        }
        Phrase phrase = new Phrase(": " + date, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightForDate(Date text) {
        SimpleDateFormat sid = new SimpleDateFormat("MM/dd/yyyy");
        String date = sid.format(text);
        Phrase phrase = new Phrase("Date :" + date, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithLeftBottomTopBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.4F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithLeftTopBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithLeftBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithLeftBottomBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellrightwithLeftBottomTopBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.4F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithUnderLine(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithUnderLineSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.smallHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithUnderLineVerySmallFont(String text) {
        Phrase phrase = new Phrase(text, this.verySmallHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightWithUnderLineVerySmallFont(String text) {
        Phrase phrase = new Phrase(text, this.verySmallHeadingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellcenterwithUnderLineSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.smallHeadingFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellrightwithUnderLineSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.smallHeadingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftWithBont8Font(String text) {
        Phrase phrase = new Phrase(text, this.headingFontSize8);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterwithUnderLine(String text) {
        Phrase phrase = new Phrase(text, this.headingFontSize8);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightwithUnderLine(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthTop(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleHeading(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForRefFileNo(String text) {
        Phrase phrase = new Phrase(text, this.fileNoFont);
        PdfPCell cell = makeCell(phrase, 1);

        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleHeadingAR(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForDouble(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleAR(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleHeadingWithBacgGround(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellCenterForDouble(Double value) {
        String value1 = String.valueOf(value);
        Phrase phrase = new Phrase(value1, this.blackFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForDouble(Double value) {
        String value1 = String.valueOf(value);
        Phrase phrase = new Phrase(value1, this.blackBoldFont4);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderFclBL(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderFclBL(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setLeading(0.5F, 0.5F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterNoBorderFclBL(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderFclBLUnderLined(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderFclBL(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 2);
//        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderFclBLNormalFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont7);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderFclBLUnderLined(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForFclBl);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithBorder(String text) {
        PdfPCell cell = makeCellLeftNoBorder(text);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithBorderFclBl(String text) {
        PdfPCell cell = makeCellLeftNoBorderFclBL(text);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftPalatinoWithBorderFclBl(String text, Font font) {
        PdfPCell cell = makeCellLeftNoBorderPalatinoFclBl(text, font);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderWithUnderline(String text) {
        Phrase phrase = new Phrase(text, this.blackFontBold);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderPalatinoWithUnderline(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }
    public PdfPCell makeCellCenterNoBorderPalatinoWithUnderline(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderPalatinoWithUnderline(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderWithDots(String text) {
        StringBuilder buffer = new StringBuilder("");
        if (text != null) {
            int length = 100 - text.length();
            for (int i = 0; i < length; i++) {
                buffer.append("-");
            }
            text = text + buffer.toString();
        }
        PdfPCell cell = makeCellLeftNoBorderBold(text);
        cell.setFixedHeight(10.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderBold(String text) {
        Phrase phrase = new Phrase(text, this.blackFontBold);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderPalatinoFclBl(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 0);
//        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterNoBorderPalatinoFclBl(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightNoBorderPalatinoFclBl(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftRightBorderPalatinoFclBl(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftRightBorderPalatinoFclBl(Paragraph text, Font font) {
        PdfPCell cell = makeCell(text, 0);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightBorderPalatinoFclBl(String text, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderPlatinoFont(String text) {
        Phrase phrase = new Phrase(text, this.blackFontBold);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftBoldWithLeftTopBorder(String text) {
        PdfPCell cell = makeCellLeftNoBorderBold(text);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftPalatinoWithLeftTopBorder(String text, Font font) {
        PdfPCell cell = makeCellLeftNoBorderPalatinoFclBl(text, font);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightPalatinoWithLeftTopBorder(String text, Font font) {
        PdfPCell cell = makeCellRightNoBorderPalatinoFclBl(text, font);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftBoldWithLeftBorder(String text) {
        PdfPCell cell = makeCellLeftNoBorderBold(text);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithLeftBorderFcLBl(String text, Font font) {
        PdfPCell cell = makeCellLeftNoBorderPalatinoFclBl(text, font);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightWithLeftBorderFcLBl(String text, Font font) {
        PdfPCell cell = makeCellRightNoBorderPalatinoFclBl(text, font);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightBorderBold(String text) {
        PdfPCell cell = makeCellLeftNoBorderBold(text);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightTopBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithRightBorderColored(String text) {
        Phrase phrase = new Phrase(text, this.RedFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithNoBorderColored(String text) {
        Phrase phrase = new Phrase(text, this.RedFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftWithNoBorderColoredGreen(String text) {
        Phrase phrase = new Phrase(text, this.GreenFont8);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderForDouble(Double number) {
        String text = this.numb.format(number);
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftwithBorderBottom(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleBottomWidth(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForHeadingLineBottom(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightForHeadingLineBottom(String text) {
        Phrase phrase = new Phrase(text, this.boldHeadingFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.0F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorder(0);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderWithGreyBackground(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellLeftWithGreyBackground(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorder(0);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellLeftWithGreyBackgroundBold(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorder(0);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    public PdfPCell makeCellLeftForDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String text = sdf.format(date);
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorder(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String text = sdf.format(date);
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithColon(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithColonInddmmyyFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithoutColon(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderInDateMonthYearFormatWithTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.redBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }
 
    public PdfPCell makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithRedFont(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.redBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftForDateWithColon(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String colon = ": ";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightForDateWithColon(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String colon = "Date :";
        String text = "";
        if (date != null) {
            text = sdf.format(date);
        }
        String dateString = colon + text;
        Phrase phrase = new Phrase(dateString, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public String getCurrentTime() {
        Formatter fmt = new Formatter();
        Calendar cal = Calendar.getInstance();

        fmt.format("%tr", new Object[]{cal});
        return fmt.toString();
    }

    public PdfPCell makeCellLeftNoBorderCheckNull(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(text, this.blackFontBold);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellleftBold(String text) {
        Phrase phrase = new Phrase(text, this.customerStatementTimesBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightWithBorderAL(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithUnderLineCS(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleText(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderBottom(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderTop(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterForDoubleNoBorderTop(String text) {
        Phrase phrase = new Phrase(text, this.blackFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setPaddingBottom(3.3F);
        cell.setPaddingTop(1.0F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndRight(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndRightAndBottom(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndBottom(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightBorder(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightBorderForData(String text) {
        Phrase phrase = new Phrase(text, this.dataFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellRightBorderAlignRightBL(String text) {
        Phrase phrase = new Phrase(text, this.dataFontForBL);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellBorderTop(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndBottomAlignCenterBL(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellDataCenter(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 1);
        return cell;
    }

    public PdfPCell makeCellBorderTopAlignRightBL(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellNoAlignRightBL(String text) {
        Phrase phrase = new Phrase(text, this.dataFontForBL);
        PdfPCell cell = makeCell(phrase, 2);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndLeft(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }

    public PdfPCell makeCellNoBorders(String text) {
        Phrase phrase = new Phrase(text, this.headingFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        return cell;
    }

    public PdfPCell makeCellNoBordersForData(String text) {
        Phrase phrase = new Phrase(text, this.dataFontForBL);
        PdfPCell cell = makeCell(phrase, 0);
        return cell;
    }

    public PdfPCell makeCellBorderTopAndBottomBL(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftAndRightBorderForAR(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderTopForAR(String text) {
        Phrase phrase = new Phrase(text, this.headingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderForAR(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellCenterWithBorderForAR(String text) {
        Phrase phrase = new Phrase(text, this.blackFont);
        PdfPCell cell = makeCell(phrase, 1);
        return cell;
    }

    public PdfPCell ArBatchLeftTopBpttomLine(String text) {
        Phrase phrase = new Phrase(text, this.textFontForBatch);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightTopBpttomLine(String text) {
        Phrase phrase = new Phrase(text, this.textFontForBatch);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }

    public PdfPCell ArBatchLeftLine(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightLeftLine(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchLeftLineForValue(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightLeftLineForValue(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightLeftLineForValueInRed(String text) {
        Phrase phrase = new Phrase(text, this.redBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightForTotal(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchLeftLineForValueTotal(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell ArBatchRightLeftLineForValueForTotal(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 2);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftNoBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 5);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderFont9(String text) {
        Phrase phrase = new Phrase(text, this.blackFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderForCheckPrintingFont8(String text) {
        Phrase phrase = new Phrase(text, this.blackFontForAR);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderForCheckPrintingFont6(String text) {
        Phrase phrase = new Phrase(text, this.blackFontSize6);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellNoBorderForPaymentText(String text) {
        Phrase phrase = new Phrase(text, this.textFontForPayment);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellTopBottomBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftBorderForPaymentDetails(String text) {
        Phrase phrase = new Phrase(text, this.textFontForPayment);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        return cell;
    }

    public PdfPCell makeCellCenterNoBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont2);
        PdfPCell cell = makeCell(phrase, 1);
        cell.setBorderWidthLeft(0.0F);
        return cell;
    }

    public PdfPCell makeCellRightBottomBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellBottomBorderForPayment(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldHeadingFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }

    public PdfPCell makeCellWithBorder(String text, int alignment) {
        Phrase phrase = new Phrase(text, this.textFontForBatch);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell makeCellForBatch(Phrase phrase, int alignment) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell makeCellWithBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCell(String text, int alignment, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    public PdfPCell makeCellForMaster(String text, int alignment, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        cell.setBorderWidthBottom(0.0F);
        cell.setBorderWidthTop(0.0F);
        cell.setBorderWidthLeft(0.0F);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCell(String text, int horizontalAlignment, int verticalAlignment, Font font, int border) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setBorder(border);
        return cell;
    }

    public PdfPCell makeCell(String text, int horizontalAlignment, int verticalAlignment, Font font, float leftBorder, float rightBorder, float topBorder, float bottomBorder, Color backgroundColor) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setBorderWidthBottom(bottomBorder);
        cell.setBorderWidthTop(topBorder);
        cell.setBorderWidthLeft(leftBorder);
        cell.setBorderWidthRight(rightBorder);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    public PdfPCell makeCell(String text, int horizontalAlignment, int verticalAlignment, Font font, int border, Color backgroundColor) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    public PdfPCell makeCell(String text, int horizontalAlignment, Font font, int border, Color backgroundColor) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    public PdfPCell makeCell(String text, int horizontalAlignment, Font font, int border) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(5);
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        return cell;
    }

    public Font createFont(BaseFont baseFont, float size, int style, Color color) {
        return new Font(baseFont, size, style, color);
    }

    public PdfPCell makeCellleftNoBorderWithBoldFontWithBorder(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFont3);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.6F);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthTop(0.6F);
        return cell;
    }

    public PdfPCell makeCellleftwithWordsUnderLinedSmallFont(String text) {
        Phrase phrase = new Phrase(text, this.blackBoldFontWithUnderline);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthBottom(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderConfirmeOnBoard(String text, boolean bold, String align) {
        Phrase phrase = null;
        if (bold) {
            phrase = new Phrase(text, this.boldLabelForConfirmOnBoard);
        } else {
            phrase = new Phrase(text, this.labelForConfirmOnBoard);
        }
        PdfPCell cell = makeCell(phrase, 1);
        if ((align != null) && (align.equalsIgnoreCase("left"))) {
            cell = makeCell(phrase, 0);
        } else if ((align != null) && (align.equalsIgnoreCase("right"))) {
            cell = makeCell(phrase, 2);
        }
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderConfirmeOnBoard(String text, boolean bold, String align, int fontSize) {
        Phrase phrase = null;
        if (bold) {
            phrase = new Phrase(text, new Font(0, fontSize, 1, Color.BLACK));
        } else {
            phrase = new Phrase(text, new Font(0, fontSize, 0, Color.BLACK));
        }
        PdfPCell cell = makeCell(phrase, 1);
        if ((align != null) && (align.equalsIgnoreCase("left"))) {
            cell = makeCell(phrase, 0);
        } else if ((align != null) && (align.equalsIgnoreCase("right"))) {
            cell = makeCell(phrase, 2);
        }
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPCell makeCellLeftNoBorderConfirmeOnBoard(String text, boolean bold, String align, String border) {
        Phrase phrase = null;
        if (bold) {
            phrase = new Phrase(text, this.boldLabelForConfirmOnBoard);
        } else {
            phrase = new Phrase(text, this.labelForConfirmOnBoard);
        }
        PdfPCell cell = makeCell(phrase, 1);
        if ((align != null) && (align.equalsIgnoreCase("left"))) {
            cell = makeCell(phrase, 0);
        } else if ((align != null) && (align.equalsIgnoreCase("right"))) {
            cell = makeCell(phrase, 2);
        }
        if ((border != null) && (border.equalsIgnoreCase("bottom"))) {
            cell.setBorderWidthBottom(0.6F);
        }
        cell.setBorderWidthRight(0.0F);
        return cell;
    }

    public PdfPTable createEmptyRows(PdfPTable pdfPTable, int columns, int rows) {
        rows *= columns;
        for (int k = 0; k < rows; k++) {
            pdfPTable.addCell(makeCellRightNoBorderFclBL(" "));
        }
        return pdfPTable;
    }

    public PdfPTable createRemeaningEmptyRows(PdfPTable pdfPTable, int columns, Integer limitRows) {
        ArrayList list = pdfPTable.getRows();
        if ((list != null) && (list.size() < limitRows.intValue())) {
            int size = list.size();
            limitRows = Integer.valueOf(limitRows.intValue() - size);
            limitRows = Integer.valueOf(limitRows.intValue() * columns);
            for (int k = 0; k < limitRows.intValue(); k++) {
                pdfPTable.addCell(makeCellRightNoBorderFclBL(" "));
            }
        }
        return pdfPTable;
    }

    public PdfPCell makeCellWithHeader(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithLabelItalic(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithTopBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthTop(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithBottomBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithoutBottomBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthBottom(0.0F);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithTopandLeftBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthLeft(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithTopandRightBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthRight(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithHeaderWithoutBorder(String header, String info) {
        PdfPCell pCell = new PdfPCell();
        Phrase headerPhrase = new Phrase(header, this.blackBoldFont8);
        pCell.addElement(headerPhrase);
        Phrase phrase = new Phrase(info, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        return pCell;
    }

    public PdfPCell makeCellWithNormal(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithBottomBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithRightBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthRight(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithLeftBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithRightAndBottomBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithLeftAndBottomBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithoutLeftAndBottomBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthTop(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithoutTopandBottomBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthTop(0.0F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithoutTopBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthTop(0.0F);
        return pCell;
    }

    public PdfPCell makeCellWithNormalWithoutBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthTop(0.0F);
        return pCell;
    }

    public PdfPCell makeCellWithBold(String contentBold) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(contentBold, this.blackBoldFont8);
        pCell.addElement(phrase);
        return pCell;
    }

    public PdfPCell makeCellWithBoldWithLeftBorder(String contentBold) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(contentBold, this.blackBoldFont8);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithBoldWithLeftAndBottomBorder(String contentBold) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(contentBold, this.blackBoldFont8);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthTop(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithBoldWithoutRightAndBottomBorder(String contentBold) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(contentBold, this.blackBoldFont8);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthBottom(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthTop(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithoutRightBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthLeft(0.6F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithoutLeftBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthRight(0.6F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }

    public PdfPCell makeCellWithoutRightAndLeftBorder(String content) {
        PdfPCell pCell = new PdfPCell();
        Phrase phrase = new Phrase(content, this.blackFontForAR);
        pCell.addElement(phrase);
        pCell.setBorderWidthRight(0.0F);
        pCell.setBorderWidthLeft(0.0F);
        pCell.setBorderWidthTop(0.6F);
        pCell.setBorderWidthBottom(0.6F);
        return pCell;
    }
        public PdfPCell makeCell(String text, int horizontalAlignment, Font font, float border) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(5);
        cell.setBorderWidthBottom(border);
        cell.setBorderWidthLeft(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        return cell;
    }
        
       public PdfPCell createEmptyCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        return cell;
    }
    public PdfPCell makeCellLeftNoBorderValueForFCLVGM(String text) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase(": " + text, this.redBoldFont5);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setBorderWidthRight(0.0F);
        return cell;
    }
        }

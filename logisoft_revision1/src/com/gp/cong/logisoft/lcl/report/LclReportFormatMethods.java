package com.gp.cong.logisoft.lcl.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;

public class LclReportFormatMethods extends PdfPageEventHelper {

    protected Document document;
    protected PdfWriter pdfWriter;
    protected String contextPath;
    protected PdfPTable contentTable;
    protected PdfPTable table;
    protected PdfPCell cell;
    protected PdfTemplate pageTemplate;
    protected Color GREEN = Color.decode("#00FF00");
    protected Color TEAL = Color.decode("#008080");
    protected Color LAVENDAR = Color.decode("#EBDDE2");
    protected Color DARK_ASH = Color.decode("#C3FDB8");
    protected Color LIGHT_ASH = Color.decode("#DCDCDC");
    public Font headingBlueBoldFont = FontFactory.getFont("Arial", 14f, Font.NORMAL, new BaseColor(00, 51, 153));
    public Font arialBoldFont14Size = FontFactory.getFont("Arial", 14f, Font.BOLD);
    public Font headingBoldFont = FontFactory.getFont("Arial", 10f, Font.BOLD);
    public Font arialFontSize10Normal = FontFactory.getFont("Arial", 10f, Font.NORMAL);
    public Font headingBoldFontSmall = FontFactory.getFont("Arial", 7f, Font.BOLD);
    public Font bodyNormalFont = FontFactory.getFont("Arial", 7.5f, Font.NORMAL);
    public Font blackBoldFont = FontFactory.getFont("Arial", 10f, Font.BOLD);
    public Font dateBoldFont = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
    public Font emailBoldFont = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    public Font voyageBlueFont = FontFactory.getFont("Arial", 8f, Font.BOLD, new BaseColor(00, 00, 153));
    public Font voyageGreenFont = FontFactory.getFont("Arial", 9f, Font.BOLD, new BaseColor(00, 102, 00));
    public Font commoditydescBoldFont = FontFactory.getFont("Arial", 9f, Font.BOLD);
    public Font unitExceptionBoldFont = FontFactory.getFont("Arial", 12f, Font.BOLD);
    public Font commodityNormalFont = FontFactory.getFont("Arial", 9f, Font.NORMAL);
    public Font resultComFont = FontFactory.getFont("Arial", 8.5f, Font.NORMAL);
    public Font blackContentNormalFont = FontFactory.getFont("Arial", 8.5f, Font.NORMAL);
    public Font blackContentBoldFont = FontFactory.getFont("Arial", 8f, Font.BOLD);
    public Font blackBoldFont75F = FontFactory.getFont("Arial", 7.5f, Font.BOLD);
    public Font boxRedFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED);
    public Font boxRedFontBig = new Font(FontFamily.HELVETICA, 15f, Font.BOLD, BaseColor.RED);
    public Font blackBoldFontSize6 = new Font(FontFamily.COURIER, 6.5f, Font.NORMAL);
    public Font courierBoldFont6 = new Font(FontFamily.COURIER, 6.5f, Font.BOLD);
    public Font blackCourierFont8 = new Font(FontFamily.COURIER, 8f, Font.NORMAL);
    // public Font blackBoldFontSize7 = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    public Font blackBoldFontSize8 = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    public Font blackBoldFontSize10 = new Font(FontFamily.COURIER, 8, Font.NORMAL);
    public Font blackItalicFontSize10 = new Font(FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.BLACK);
    public Font ratessubHeadingQuote = FontFactory.getFont("Arial", 9f, Font.BOLD);
    public Font fontCompNormalSub = FontFactory.getFont("Arial", 8.5f, Font.NORMAL);
    public Font labelRatesQuote = FontFactory.getFont("Arial", 9f, Font.NORMAL);
    public Font mainHeadingQuote = FontFactory.getFont("Arial", 10f, Font.BOLD, BaseColor.WHITE);
    public Font totalFontQuote = new Font(FontFamily.COURIER, 9f, Font.BOLD);
    public Font fontgreenCont = FontFactory.getFont("Arial", 9f, Font.BOLD, new BaseColor(00, 128, 00));
    public Font contentNormalFont = FontFactory.getFont("Arial", 7f, Font.NORMAL);
    public Font contenthBoldFont = FontFactory.getFont("Arial", 16f, Font.BOLD, new BaseColor(00, 00, 255));
    public Font calculateRatesQuote = new Font(FontFamily.COURIER, 10, Font.BOLD);
    public Font fontCompNormalSubblue = FontFactory.getFont("Arial", 8.5f, Font.NORMAL, new BaseColor(25, 00, 155));
    public Font contentBLNormalFont = new Font(FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    public Font contentheadingNormalFont = new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    public Font headingblackBoldFont = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
    protected Font blackNormalFont9 = new Font(FontFamily.COURIER, 9.5f, Font.NORMAL);
    protected Font blackNormalCourierFont10f = new Font(FontFamily.COURIER, 10.5f, Font.NORMAL);
    protected Font blackBoldCourierFont10f = new Font(FontFamily.COURIER, 12f, Font.NORMAL);
    protected Font blackBoldFont9 = new Font(FontFamily.TIMES_ROMAN, 9f, Font.NORMAL);
    public Font blackBoldFontSize7 = FontFactory.getFont("Arial", 7f, Font.BOLD);
    public Font blackBoldFont14 = FontFactory.getFont("Arial", 14f, Font.NORMAL);
    public Font blackNormalFont7 = FontFactory.getFont("Arial", 7f, Font.NORMAL);
    public Font blackNormalFont13 = FontFactory.getFont("Arial", 13f, Font.NORMAL);
    public Font blackBoldFont65 = FontFactory.getFont("Arial", 6f, Font.NORMAL);
    public Font blackBoldFont20 = FontFactory.getFont("Arial", 18f, Font.NORMAL);
    public Font blackNormalCourierFont12f = new Font(FontFamily.COURIER, 12f, Font.NORMAL);
    public Font blackNormalCourierFont18f = new Font(FontFamily.COURIER, 18f, Font.BOLD);
    public Font blackboldCourierFont11f = new Font(FontFamily.COURIER, 11f, Font.BOLD);
    public Font blackNormalCourierFont9f = new Font(FontFamily.COURIER, 9f, Font.NORMAL);
    public Font voyageBlueFontItalic9 = FontFactory.getFont("Arial", 9f, Font.BOLDITALIC, new BaseColor(00, 00, 128));
    public Font blueNormalFont8 = FontFactory.getFont("Arial", 8f, Font.BOLD, new BaseColor(00, 00, 255));
    public Font blueNormalFont9 = FontFactory.getFont("Arial", 9f, Font.BOLDITALIC, new BaseColor(00, 00, 255));
    public Font blueBoldFont15 = FontFactory.getFont("Arial", 15f, Font.BOLD, new BaseColor(00, 00, 255));
    public Font blueBoldFont11 = FontFactory.getFont("Arial", 11f, Font.BOLD, new BaseColor(00, 00, 255));
    public Font blueNormalFont13 = FontFactory.getFont("Arial", 13f, Font.NORMAL, new BaseColor(00, 00, 255));
    public Font blueNormalFont14 = FontFactory.getFont("Arial", 14f, Font.NORMAL, new BaseColor(00, 00, 255));
    public Font redBoldFont10 = FontFactory.getFont("Arial", 9f, Font.BOLD, new BaseColor(178, 0, 0));
    public Font blueNormalFontArial9 = FontFactory.getFont("Arial", 9f, Font.NORMAL, new BaseColor(0, 0, 128));
    public Font greenCourierFont9 = new Font(FontFamily.COURIER, 9f, Font.BOLD, new BaseColor(00, 102, 00));
    public Font boldRedFont = FontFactory.getFont("Arial", 8f, Font.BOLD, BaseColor.RED);
    public Font boldBlockUnderline = new Font(FontFamily.COURIER, 12f, Font.UNDERLINE, BaseColor.BLACK);

    protected PdfPCell createCell(String text, Font font, int horizontalAlignment, int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, float borderLine, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setPaddingTop(-1.5f);
        cell.setBorder(border);
        cell.setBorderWidthBottom(borderLine);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        cell.setVerticalAlignment(alignment);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, int colspan, int alignment) {
        cell = new PdfPCell(new Phrase(text, font));
        cell.setPaddingTop(-1.5f);
        cell.setBorder(border);
        cell.setColspan(colspan);
        cell.setVerticalAlignment(alignment);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, float paddingTop, Font font, int border, float borderLine, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setPaddingTop(paddingTop);
        cell.setBorder(border);
        cell.setBorderWidthBottom(borderLine);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        cell.setVerticalAlignment(alignment);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell1(String text, Font font, int border, float borderLine, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setPaddingTop(-3f);
        cell.setBorder(border);
        cell.setBorderWidthBottom(borderLine);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        cell.setVerticalAlignment(alignment);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell2(String text, Font font, int border, float borderLine, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setPaddingTop(-1f);
        cell.setBorder(border);
        cell.setBorderWidthBottom(borderLine);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        cell.setVerticalAlignment(alignment);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, float borderLineTop) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setPadding(0f);
        cell.setPaddingTop(-20f);
        cell.setBorder(border);
        cell.setBorderWidthTop(borderLineTop);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
//        cell.setVerticalAlignment(alignment);
//        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, int border, float paddingTop) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        // cell.setLeading(7f,1f);
        cell.setBorder(border);
        cell.setPaddingTop(paddingTop);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        // cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCell(String text, float borderLine, float paddingTop, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(border);
        cell.setBorderWidthBottom(borderLine);
        cell.setPaddingBottom(paddingTop);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        // cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCell(String text, int border, float paddingTop, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        // cell.setLeading(7f,1f);
        cell.setBorder(border);
        cell.setPaddingTop(paddingTop);
        // cell.setBorderWidthTop(1f);
        // cell.setBorderWidthRight(1f);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        // cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCelltopLeftPadding(String text, int border, float paddingTop, float paddingLeft, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingLeft(paddingLeft);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCell(String text, int border, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setPaddingTop(-3f);
        cell.setPaddingLeft(-2.5f);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCellTopRightPadding(String text, int border, float paddingTop, float paddingRight, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setPaddingRight(paddingRight);
        cell.setPaddingTop(paddingTop);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createTextCell(String text, float padding, float borderWidth, int border, Font font) {
        if (text == null || text == "") {
            text = "";
        }
        cell = new PdfPCell();
        cell.setBorder(border);
        Paragraph p = new Paragraph(padding, text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthRight(borderWidth);
        cell.addElement(p);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border) {
        cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        return cell;
    }
    
    protected PdfPCell createEmptyCell(int border, float borderWidth) {
        cell = new PdfPCell(new Paragraph(8f, "", blackBoldFontSize6));
        cell.setBorder(border);
        cell.setBorderWidthRight(borderWidth);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border, float borderWidth, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        cell.setColspan(colspan);
        if (border == Rectangle.TOP) {
            cell.setPaddingTop(2 * borderWidth);
            cell.setBorderWidthTop(borderWidth);
        } else if (border == Rectangle.RIGHT) {
            cell.setPaddingRight(2 * borderWidth);
            cell.setBorderWidthRight(borderWidth);
        } else if (border == Rectangle.BOTTOM) {
            cell.setPaddingBottom(2 * borderWidth);
            cell.setBorderWidthBottom(borderWidth);
        } else if (border == Rectangle.LEFT) {
            cell.setPaddingLeft(2 * borderWidth);
            cell.setBorderWidthLeft(borderWidth);
        } else {
            cell.setPadding(2 * borderWidth);
            cell.setBorderWidth(borderWidth);
        }
        return cell;
    }
//    protected PdfPCell createTextCell(String text, Font font, int border, float borderWidth) {
//        PdfPCell cell = new PdfPCell(new Phrase(text, font));
//        cell.setBorder(border);
//        if (border == Rectangle.TOP) {
//            cell.setPaddingTop(2 * borderWidth);
//            cell.setBorderWidthTop(borderWidth);
//        } else if (border == Rectangle.RIGHT) {
//            cell.setPaddingRight(2 * borderWidth);
//            cell.setBorderWidthRight(borderWidth);
//        } else if (border == Rectangle.BOTTOM) {
//            cell.setPaddingBottom(2 * borderWidth);
//            cell.setBorderWidthBottom(borderWidth);
//        } else if (border == Rectangle.LEFT) {
//            cell.setPaddingLeft(2 * borderWidth);
//            cell.setBorderWidthLeft(borderWidth);
//        } else if (border == Rectangle.BOX) {
//            cell.setPadding(2 * borderWidth);
//            cell.setBorderWidth(borderWidth);
//        }
//        // cell.setBorderColor(Color.BLACK);
//        // cell.setBackgroundColor(Color.WHITE);
//        cell.setVerticalAlignment(Element.ALIGN_TOP);
//        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        return cell;
//    }

    public PdfPCell makeCell(Phrase phrase, int alignment) {
        cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(0);
        return cell;
    }

    public PdfPCell makeCellLeftBorderValue(String text, int colspan) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, this.blackBoldFontSize7);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(0.5f);
        cell.setPaddingBottom(1f);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }

    public PdfPCell makeCellLeftBottomBorderValue(String text, int colspan, float paddingTop, float paddingBottom, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingBottom(paddingBottom);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }

    public PdfPCell makeCellTopBottomBorderValue(String text, int colspan, float paddingTop, float paddingBottom, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingBottom(paddingBottom);
        cell.setBorderWidthBottom(0.6F);
        return cell;
    }
//    public PdfPCell makeCellTopBottomBorderValue(String text, int colspan, float paddingTop, float paddingBottom,Font font) {
//        if (text == null) {
//            text = "";
//        }
//        Phrase phrase = new Phrase("" + text, font);
//        PdfPCell cell = makeCell(phrase, 0);
//        cell.setColspan(colspan);
//        cell.setPaddingTop(paddingTop);
//        cell.setPaddingBottom(paddingBottom);
//        cell.setBorderWidthBottom(0.6F);
//        cell.setBorderWidthTop(0.6F);
//        return cell;
//    }

    public PdfPCell makeCellNoBorderValue(String text, int colspan, float paddingTop, float paddingBottom) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, this.blackBoldFontSize7);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(paddingTop);
        cell.setPaddingBottom(paddingBottom);
        return cell;
    }

    public PdfPCell makeCellLeftTopNoBorderFont(String text, float paddingleft, float paddingTop, Font font) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, 0);
        cell.setPaddingLeft(paddingleft);
        cell.setPaddingTop(paddingTop);
        cell.setColspan(2);
        return cell;
    }

    public PdfPCell makeCellNoBorderFont(String text, float paddingTop, int colspan, Font font) {
        Paragraph phrase = new Paragraph(text, font);
        cell = makeCell(phrase, 0);
        cell.setPaddingTop(paddingTop);
        cell.setColspan(colspan);
        return cell;
    }

    public PdfPCell makeCellBottomBorderValue(String text, int colspan, float paddingTop, float borderWidthBottom, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(paddingTop);
        // cell.setPaddingLeft(-1f);
        cell.setBorderWidthBottom(borderWidthBottom);
        return cell;
    }

    public PdfPCell makeCellNoBorderFontalign(String text, float paddingTop, int alignment, int colspan, Font font) {
        if (text == null) {
            text = "";
        }
        Paragraph para = new Paragraph("" + text, font);
        cell = makeCell(para, 0);
        cell.setHorizontalAlignment(alignment);
        cell.setPaddingTop(paddingTop);
        cell.setColspan(colspan);
        return cell;
    }

    public PdfPCell makeCell(String text, int colspan, float paddingTop, float padding, float borderWidthBottom, float borderWidthTop, float borderWidthLeft, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(paddingTop);
        cell.setPadding(padding);
        cell.setBorderWidthTop(borderWidthTop);
        cell.setBorderWidthLeft(borderWidthLeft);
        cell.setBorderWidthBottom(borderWidthBottom);
        return cell;
    }

    public PdfPCell makeCellNoBorderValue(String text, int colspan, float padding, float paddingBottom, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        cell = makeCell(phrase, 0);
        cell.setPadding(padding);
        cell.setColspan(colspan);
        cell.setPaddingBottom(paddingBottom);
        return cell;
    }

    public PdfPCell makeCellLeftBorderValue(String text, int colspan, Font font) {
        if (text == null) {
            text = "";
        }
        Phrase phrase = new Phrase("" + text, font);
        cell = makeCell(phrase, 0);
        cell.setColspan(colspan);
        cell.setPaddingTop(0.5f);
        cell.setPaddingBottom(1f);
        cell.setBorderWidthLeft(0.6F);
        return cell;
    }
    
    protected PdfPCell createCell(String text, Font font, int border, float borderLine, int alignment) {
        cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }
    protected PdfPCell makeBorder(PdfPCell cell,float left, float right, float top, float bottom) {
        cell.setBorderWidthBottom(bottom);
        cell.setBorderWidthTop(top);
        cell.setBorderWidthLeft(left);
        cell.setBorderWidthRight(right);
        return cell;
    }
     protected PdfPCell createEmptyCellWithBorder() {
        cell = new PdfPCell(new Phrase(""));
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthTop(0.6f);
        return cell;
    }
}

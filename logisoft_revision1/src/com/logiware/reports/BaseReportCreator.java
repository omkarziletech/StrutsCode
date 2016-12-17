package com.logiware.reports;

import com.gp.cong.common.NumberUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lakshmi Naryanan
 */
public class BaseReportCreator extends PdfPageEventHelper {

    protected Document document;
    protected PdfWriter writer;
    protected String contextPath;
    protected PdfPTable headerTable;
    protected PdfPTable contentTable;
    protected PdfTemplate pageTemplate;
    protected PdfPTable outerTable;
    protected Color GREEN = Color.decode("#114F16");
    protected Color TEAL = Color.decode("#008080");
    protected Color LAVENDAR = Color.decode("#EBDDE2");
    protected Color DARK_ASH = Color.decode("#C3FDB8");
    protected Color LIGHT_ASH = Color.decode("#DCDCDC");
    protected BaseFont helvFont;
    protected Font blackTimesBoldFont11 = new Font(Font.TIMES_ROMAN, 11f, Font.BOLD, Color.BLACK);
    protected Font headerGreenBoldFont12 = new Font(Font.HELVETICA, 12f, Font.BOLD, GREEN);
    protected Font headerBoldFont16 = new Font(Font.HELVETICA, 16f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont15 = new Font(Font.HELVETICA, 15f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont14 = new Font(Font.HELVETICA, 14f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont13 = new Font(Font.HELVETICA, 13f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont12 = new Font(Font.HELVETICA, 12f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont11 = new Font(Font.HELVETICA, 11f, Font.BOLD, Color.BLACK);
    protected Font headerBoldFont10 = new Font(Font.HELVETICA, 10f, Font.BOLD, Color.BLACK);
    protected Font blackBoldFont10 = new Font(Font.HELVETICA, 10f, Font.BOLD, Color.BLACK);
    protected Font redBoldFont10 = new Font(Font.HELVETICA, 10f, Font.BOLD, Color.RED);
    protected Font blackNormalFont10 = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.BLACK);
    protected Font redNormalFont10 = new Font(Font.HELVETICA, 10f, Font.NORMAL, Color.RED);
    protected Font blackBoldFont9 = new Font(Font.HELVETICA, 9f, Font.BOLD, Color.BLACK);
    protected Font redBoldFont9 = new Font(Font.HELVETICA, 9f, Font.BOLD, Color.RED);
    protected Font blackNormalFont9 = new Font(Font.HELVETICA, 9f, Font.NORMAL, Color.BLACK);
    protected Font redNormalFont9 = new Font(Font.HELVETICA, 8f, Font.NORMAL, Color.RED);
    protected Font blackBoldFont8 = new Font(Font.HELVETICA, 8f, Font.BOLD, Color.BLACK);
    protected Font redBoldFont8 = new Font(Font.HELVETICA, 8f, Font.BOLD, Color.RED);
    protected Font blackNormalFont8 = new Font(Font.HELVETICA, 8f, Font.NORMAL, Color.BLACK);
    protected Font redNormalFont8 = new Font(Font.HELVETICA, 8f, Font.NORMAL, Color.RED);
    protected Font blackBoldFont7 = new Font(Font.HELVETICA, 7f, Font.BOLD, Color.BLACK);
    protected Font blackNormalFont7 = new Font(Font.HELVETICA, 7f, Font.NORMAL, Color.BLACK);
    protected Font redNormalFont7 = new Font(Font.HELVETICA, 7f, Font.NORMAL, Color.RED);
    protected Font blackBoldFont6 = new Font(Font.HELVETICA, 6f, Font.BOLD, Color.BLACK);
    protected Font blackNormalFont6 = new Font(Font.HELVETICA, 6f, Font.NORMAL, Color.BLACK);
    protected Font redNormalFont6 = new Font(Font.HELVETICA, 6f, Font.NORMAL, Color.RED);
    protected Font blueNormalFont7 = new Font(Font.HELVETICA, 7f, Font.NORMAL, Color.BLUE);
    protected Font blueNormalFont6 = new Font(Font.HELVETICA, 6f, Font.NORMAL, Color.BLUE);
    protected Font redNormalFont20 = new Font(Font.HELVETICA, 20f, Font.NORMAL, Color.RED);
    protected Font blackNormalFont11 = new Font(Font.HELVETICA, 11f, Font.NORMAL, Color.BLACK);
    protected Font blackNormalFont5 = new Font(Font.HELVETICA, 5f, Font.NORMAL, Color.BLACK);
    public Font headingFontRed = new Font(Font.HELVETICA, 13f, Font.BOLD, Color.RED);
    protected Font headerBoldFontBlue = new Font(Font.HELVETICA, 10f, Font.BOLD, Color.BLUE);

    protected PdfPCell createHeaderCell(String text, Font font, int horizontalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(DARK_ASH);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createHeaderCell(String text, Font font, int horizontalAlignment, int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        cell.setColspan(colspan);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border, Color backgroundColor, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
        cell.setColspan(colspan);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    protected PdfPCell createEmptyCell(int border, float borderWidth) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
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

    protected PdfPCell createEmptyCell(int border, Color backgroundColor, float borderWidth) {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(border);
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
        cell.setBackgroundColor(backgroundColor);
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

    protected PdfPCell createEmptyCell(int border, Color backgroundColor, float borderWidth, int colspan) {
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
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

    protected PdfPCell createImageCell(Image image) {
        PdfPCell cell = new PdfPCell(image);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    protected PdfPCell createImageCell(Image image, int horizontalAlignment) {
        PdfPCell cell = new PdfPCell(image);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, float borderWidth) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
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
        } else if (border == Rectangle.BOX) {
            cell.setPadding(2 * borderWidth);
            cell.setBorderWidth(borderWidth);
        }
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, float borderWidth, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
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
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int horizontalAlignment, int border, float borderWidth) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
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
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int horizontalAlignment, int border, float borderWidth, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
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
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int horizontalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int horizontalAlignment, int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font font, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(StringUtils.contains(amount, "-") ? "(" + amount.replace("-", "") + ")" : amount, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font font, int border) {
        String amount = NumberUtils.formatNumber(amountInDbl);
        PdfPCell cell = new PdfPCell(new Phrase(StringUtils.contains(amount, "-") ? "(" + amount.replace("-", "") + ")" : amount, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font blackFont, Font redFont, int border) {
        Phrase phrase = null;
        if (StringUtils.contains(amount, "-")) {
            phrase = new Phrase("(" + amount.replace("-", "") + ")", redFont);
        } else if (StringUtils.contains(amount, "(")) {
            phrase = new Phrase(amount, redFont);
        } else {
            phrase = new Phrase(amount, blackFont);
        }
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font blackFont, Font redFont, int border) {
        PdfPCell cell = null;
        String amount = NumberUtils.formatNumber(amountInDbl);
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font blackFont, Font redFont, int border, Color backgroundColor) {
        PdfPCell cell = null;
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font blackFont, Font redFont, int border, Color backgroundColor) {
        PdfPCell cell = null;
        String amount = NumberUtils.formatNumber(amountInDbl);
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(long longAmount, Font blackFont, Font redFont, int border, Color backgroundColor) {
        String amount = NumberUtils.formatNumber(longAmount, "###,###,##0");
        PdfPCell cell;
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font font, int horizontalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(StringUtils.contains(amount, "-") ? "(" + amount.replace("-", "") + ")" : amount, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font font, int horizontalAlignment, int border) {
        String amount = NumberUtils.formatNumber(amountInDbl);
        PdfPCell cell = new PdfPCell(new Phrase(StringUtils.contains(amount, "-") ? "(" + amount.replace("-", "") + ")" : amount, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font blackFont, Font redFont, int horizontalAlignment, int border) {
        Phrase phrase = null;
        if (StringUtils.contains(amount, "-")) {
            phrase = new Phrase("(" + amount.replace("-", "") + ")", redFont);
        } else if (StringUtils.contains(amount, "(")) {
            phrase = new Phrase(amount, redFont);
        } else {
            phrase = new Phrase(amount, blackFont);
        }
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font blackFont, Font redFont, int horizontalAlignment, int border) {
        PdfPCell cell = null;
        String amount = NumberUtils.formatNumber(amountInDbl);
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(String amount, Font blackFont, Font redFont, int border, int horizontalAlignment, Color backgroundColor) {
        PdfPCell cell = null;
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createAmountCell(double amountInDbl, Font blackFont, Font redFont, int border, int horizontalAlignment, Color backgroundColor) {
        PdfPCell cell = null;
        String amount = NumberUtils.formatNumber(amountInDbl);
        if (amount.contains("-")) {
            cell = new PdfPCell(new Phrase("(" + amount.replace("-", "") + ")", redFont));
        } else {
            cell = new PdfPCell(new Phrase(amount, blackFont));
        }
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createCell(String text, int horizontalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createCell(String text, Font font, int horizontalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createCell(String text, Font font, int horizontalAlignment, int border, Color backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(backgroundColor);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(horizontalAlignment);
        return cell;
    }

    protected PdfPCell createTextCell(String text, Font font, int horizontalAlignment, int verticalAlignment, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(border);
        cell.setBorderColor(Color.BLACK);
        cell.setBackgroundColor(Color.WHITE);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        return cell;
    }
}

package com.gp.cong.common;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

public class itext extends  ReportFormatMethods {
	public void creatReport() throws FileNotFoundException, DocumentException{
		Font whiteBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
		Document d=new Document();
		d.setMargins(20, 20, 10, 10);
		PdfWriter p= PdfWriter.getInstance(d, new FileOutputStream("c:/test.pdf"));
		d.open();
		 PdfPTable table =new PdfPTable(1);
		 Phrase phrase = new Phrase("HELLO WORD", whiteBoldFont);
		 PdfPCell cell=makeCellleftNoBorder("HITESh");
		 cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		 table.addCell(cell);
		
		 d.add(table);
		d.close();
	}
	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		itext g=new itext();
		g.creatReport();
	}
}

package com.gp.cong.test;

import java.io.*;
import jxl.*;
import java.util.*;
import jxl.Workbook;
import jxl.write.Number;

import jxl.write.*;

import org.apache.log4j.Logger;

class create {

    private static final Logger log = Logger.getLogger(create.class);

    public static void main(String[] args) {
	try {
	    String filename = "input.xls";
	    WorkbookSettings ws = new WorkbookSettings();
	    ws.setLocale(new Locale("en", "EN"));
	    WritableWorkbook workbook =
		    Workbook.createWorkbook(new File(filename), ws);
	    WritableSheet s = workbook.createSheet("Sheet1", 0);
	    WritableSheet s1 = workbook.createSheet("Sheet1", 0);
	    writeDataSheet(s);
	    // writeImageSheet(s1);
	    workbook.write();
	    workbook.close();
	} catch (IOException e) {
	    log.info("Exception happened  " + new Date(),e);
	} catch (WriteException e) {
	    log.info("Exception happened  " + new Date(),e);
	}
    }

    private static void writeDataSheet(WritableSheet s)
	    throws WriteException {

	/* Format the Font */
	WritableFont wf = new WritableFont(WritableFont.ARIAL,
		10, WritableFont.BOLD);
	WritableCellFormat cf = new WritableCellFormat(wf);
	cf.setWrap(true);

	/* Creates Label and writes date to one cell of sheet*/
	Label l = new Label(0, 0, "Date", cf);
	s.addCell(l);
	WritableCellFormat cf1 =
		new WritableCellFormat(DateFormats.FORMAT9);

	DateTime dt =
		new DateTime(0, 1, new Date(), cf1, DateTime.GMT);

	s.addCell(dt);

	/* Creates Label and writes float number to one cell of sheet*/
	l = new Label(2, 0, "Float", cf);
	s.addCell(l);
	WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
	Number n = new Number(2, 1, 3.1415926535, cf2);
	s.addCell(n);

	n = new Number(2, 2, -3.1415926535, cf2);
	s.addCell(n);

	/* Creates Label and writes float number upto 3 
	 decimal to one cell of sheet */
	l = new Label(3, 0, "3dps", cf);
	s.addCell(l);
	NumberFormat dp3 = new NumberFormat("#.###");
	WritableCellFormat dp3cell = new WritableCellFormat(dp3);
	n = new Number(3, 1, 3.1415926535, dp3cell);
	s.addCell(n);

	/* Creates Label and adds 2 cells of sheet*/
	l = new Label(4, 0, "Add 2 cells", cf);
	s.addCell(l);
	n = new Number(4, 1, 10);
	s.addCell(n);
	n = new Number(4, 2, 16);
	s.addCell(n);
	Formula f = new Formula(4, 3, "E1+E2");
	s.addCell(f);

	/* Creates Label and multipies value of one cell of sheet by 2*/
	l = new Label(5, 0, "Multipy by 2", cf);
	s.addCell(l);
	n = new Number(5, 1, 10);
	s.addCell(n);
	f = new Formula(5, 2, "F1 * 3");
	s.addCell(f);

	/* Creates Label and divide value of one cell of sheet by 2.5 */
	l = new Label(6, 0, "Divide", cf);
	s.addCell(l);
	n = new Number(6, 1, 12);
	s.addCell(n);
	f = new Formula(6, 2, "F1/2.5");
	s.addCell(f);
    }

    private static void writeImageSheet(WritableSheet s)
	    throws WriteException {
	/* Creates Label and writes image to one cell of sheet*/
	Label l = new Label(0, 0, "Image");
	s.addCell(l);
	// WritableImage wi = new WritableImage(0, 3, 5, 7, new File("image.png"));
	// s.addImage(wi);

	/* Creates Label and writes hyperlink to one cell of sheet*/
	l = new Label(0, 15, "HYPERLINK");
	s.addCell(l);
	Formula f = new Formula(1, 15,
		"HYPERLINK(\"http://www.andykhan.com/jexcelapi\", "
		+ "\"JExcelApi Home Page\")");
	s.addCell(f);

    }
}

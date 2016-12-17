package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author logiware
 */
public class ApStatementReportExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private ApReportsForm apReportsForm;

    public ApStatementReportExcelCreator(ApReportsForm apReportsForm) {
        this.apReportsForm = apReportsForm;
    }

    private void writeStatementHeader(CustomerBean vendor, Map<String, CustomerBean> agingBuckets) throws Exception {
        boolean agentFlag = false;
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
        String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
        createRow();
        createHeaderCell(companyName, headerCellStyleLeftBold);
        if (apReportsForm.isEcuLineReport() || agentFlag) {
            mergeCells(rowIndex, rowIndex, 0, 13);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 11);
        }
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        StringBuilder addressess = new StringBuilder();
        addressess.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
        createHeaderCell(addressess.toString(), subHeaderOneCellStyleLeftBold);
        if (apReportsForm.isEcuLineReport() || agentFlag) {
            mergeCells(rowIndex, rowIndex, 0, 13);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 11);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Credit Statement", headerCellStyleCenterBold);
        if (apReportsForm.isEcuLineReport() || agentFlag) {
            mergeCells(rowIndex, rowIndex, 0, 13);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 11);
        }
        row.setHeightInPoints(20);
        CustomerBean apAgingBuckets = agingBuckets.get("apAgingBuckets");
        CustomerBean arAgingBuckets = agingBuckets.get("arAgingBuckets");
        double apBalance = 0d;
        double acBalance = 0d;
        boolean hasArAging = false;
        if (null != vendor || null != apAgingBuckets || null != arAgingBuckets) {
            String currentApAging = "";
            String thirtyOneToSixtyDaysApAging = "";
            String sixtyOneToNinetyDaysApAging = "";
            String greaterThanNinetyDaysApAging = "";
            String totalApAging = "";
            String currentArAging = "";
            String thirtyOneToSixtyDaysArAging = "";
            String sixtyOneToNinetyDaysArAging = "";
            String greaterThanNinetyDaysArAging = "";
            String totalArAging = "";
            String address = "";
            String customerNumber = "";
            createRow();
            resetColumnIndex();
            if (null != vendor) {
                if (StringUtils.contains(vendor.getAccountType(), "A")
                        || StringUtils.contains(vendor.getAccountType(), "E") || StringUtils.contains(vendor.getAccountType(), "I")) {
                    agentFlag = true;
                }
                String customerName = vendor.getCustomerName();
                address = null != vendor.getAddress() ? vendor.getAddress() : "";
                customerNumber = "Account#: " + vendor.getCustomerNumber();
                createHeaderCell(customerName, subHeaderOneCellStyleLeftBold);
                mergeCells(rowIndex, rowIndex, 0, 1);
            }
            if (null != arAgingBuckets) {
                createHeaderCell("AR SUMMARY", subHeaderOneCellStyleLeftBold);
                createHeaderCell("AMOUNT", subHeaderOneCellStyleLeftBold);
            }
            if (null != apAgingBuckets) {
                createHeaderCell("AP SUMMARY", subHeaderOneCellStyleLeftBold);
                createHeaderCell("AMOUNT", subHeaderOneCellStyleLeftBold);
                createEmptyCell(subHeaderOneCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (null != vendor) {
                createHeaderCell(address, subHeaderTwoCellStyleLeftBold);
                mergeCells(rowIndex, rowIndex, 0, 1);
            }
            if (null != arAgingBuckets) {
                hasArAging = true;
                currentArAging = "$" + arAgingBuckets.getCurrent();
                thirtyOneToSixtyDaysArAging = "$" + arAgingBuckets.getThirtyOneToSixtyDays();
                sixtyOneToNinetyDaysArAging = "$" + arAgingBuckets.getSixtyOneToNintyDays();
                greaterThanNinetyDaysArAging = "$" + arAgingBuckets.getGreaterThanNintyDays();
                totalArAging = "$" + arAgingBuckets.getTotal();
                createHeaderCell("CURRENT", subHeaderTwoCellStyleLeftBold);
                if (currentArAging.contains("-")) {
                    createHeaderCell(currentArAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(currentArAging, subHeaderTwoCellStyleRightNormal);
                }
            }
            if (null != apAgingBuckets) {
                currentApAging = "$" + apAgingBuckets.getCurrent();
                thirtyOneToSixtyDaysApAging = "$" + apAgingBuckets.getThirtyOneToSixtyDays();
                sixtyOneToNinetyDaysApAging = "$" + apAgingBuckets.getSixtyOneToNintyDays();
                greaterThanNinetyDaysApAging = "$" + apAgingBuckets.getGreaterThanNintyDays();
                totalApAging = "$" + apAgingBuckets.getTotal();
                createHeaderCell("CURRENT", subHeaderTwoCellStyleLeftBold);
                if (currentApAging.contains("-")) {
                    createHeaderCell(currentApAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(currentApAging, subHeaderTwoCellStyleRightNormal);
                }
                createEmptyCell(subHeaderTwoCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (null != arAgingBuckets) {
                if (null != vendor) {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("31-60 DAYS", subHeaderOneCellStyleLeftBold);
                if (currentArAging.contains("-")) {
                    createHeaderCell(thirtyOneToSixtyDaysArAging.replace("-", ""), subHeaderOneCellStyleRightNormal);
                } else {
                    createHeaderCell(thirtyOneToSixtyDaysArAging, subHeaderOneCellStyleRightNormal);
                }
            }
            if (null != apAgingBuckets) {
                if (null != vendor && null == arAgingBuckets) {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("31-60 DAYS", subHeaderOneCellStyleLeftBold);
                if (thirtyOneToSixtyDaysApAging.contains("-")) {
                    createHeaderCell(thirtyOneToSixtyDaysApAging.replace("-", ""), subHeaderOneCellStyleRightNormal);
                } else {
                    createHeaderCell(thirtyOneToSixtyDaysApAging, subHeaderOneCellStyleRightNormal);
                }
                createEmptyCell(subHeaderOneCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (null != arAgingBuckets) {
                if (null != vendor) {
                    createEmptyCell(subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("61-90 DAYS", subHeaderTwoCellStyleLeftBold);
                if (sixtyOneToNinetyDaysArAging.contains("-")) {
                    createHeaderCell(sixtyOneToNinetyDaysArAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(sixtyOneToNinetyDaysArAging, subHeaderTwoCellStyleRightNormal);
                }
            }
            if (null != apAgingBuckets) {
                if (null != vendor && null == arAgingBuckets) {
                    createEmptyCell(subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("61-90 DAYS", subHeaderTwoCellStyleLeftBold);
                if (sixtyOneToNinetyDaysApAging.contains("-")) {
                    createHeaderCell(sixtyOneToNinetyDaysApAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(sixtyOneToNinetyDaysApAging, subHeaderTwoCellStyleRightNormal);
                }
                createEmptyCell(subHeaderTwoCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (null != arAgingBuckets) {
                if (null != vendor) {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell(">90 DAYS", subHeaderOneCellStyleLeftBold);
                if (greaterThanNinetyDaysArAging.contains("-")) {
                    createHeaderCell(greaterThanNinetyDaysArAging.replace("-", ""), subHeaderOneCellStyleRightNormal);
                } else {
                    createHeaderCell(greaterThanNinetyDaysArAging, subHeaderOneCellStyleRightNormal);
                }
            }
            if (null != apAgingBuckets) {
                if (null != vendor && null == arAgingBuckets) {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell(">90 DAYS", subHeaderOneCellStyleLeftBold);
                if (greaterThanNinetyDaysApAging.contains("-")) {
                    createHeaderCell(greaterThanNinetyDaysApAging.replace("-", ""), subHeaderOneCellStyleRightNormal);
                } else {
                    createHeaderCell(greaterThanNinetyDaysApAging, subHeaderOneCellStyleRightNormal);
                }
                createEmptyCell(subHeaderOneCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (null != arAgingBuckets) {
                if (null != vendor) {
                    createHeaderCell(customerNumber, subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("TOTAL", subHeaderTwoCellStyleLeftBold);
                if (totalArAging.contains("-")) {
                    createHeaderCell(totalArAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(totalArAging, subHeaderTwoCellStyleRightNormal);
                }
            }
            if (null != apAgingBuckets) {
                if (null != vendor && null == arAgingBuckets) {
                    createEmptyCell(subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("TOTAL", subHeaderTwoCellStyleLeftBold);
                if (totalApAging.contains("-")) {
                    createHeaderCell(totalApAging.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(totalApAging, subHeaderTwoCellStyleRightNormal);
                }
                apBalance = Double.parseDouble(apAgingBuckets.getOutstandingPayables().replace(",", ""));
                acBalance = Double.parseDouble(apAgingBuckets.getOutstandingAccruals().replace(",", ""));
                createEmptyCell(subHeaderTwoCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 13);
                    }
                } else {
                    if (null != arAgingBuckets && null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 2, 11);
                    }
                }
            }
            createRow();
            resetColumnIndex();
            if (hasArAging) {
                double arBalance = 0d;
                if (null != vendor) {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 3);
                } else {
                    createEmptyCell(subHeaderOneCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                if (null != arAgingBuckets) {
                    arBalance = Double.parseDouble(arAgingBuckets.getTotal().replace(",", ""));
                }
                createHeaderCell("AP-AR", subHeaderOneCellStyleLeftBold);
                String netAmt1 = NumberUtils.formatNumber(apBalance + arBalance, "$###,###,##0.00");
                if (netAmt1.contains("-")) {
                    createHeaderCell(netAmt1.replace("-", ""), subHeaderOneCellStyleRightNormal);
                } else {
                    createHeaderCell(netAmt1, subHeaderOneCellStyleRightNormal);
                }
                createEmptyCell(subHeaderOneCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    }
                } else {
                    if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    }
                }
                createRow();
                resetColumnIndex();
                if (null != vendor) {
                    createEmptyCell(subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 3);
                } else {
                    createEmptyCell(subHeaderTwoCellStyleLeftBold);
                    mergeCells(rowIndex, rowIndex, 0, 1);
                }
                createHeaderCell("AP-AR-AC", subHeaderTwoCellStyleLeftBold);
                String netAmt2 = NumberUtils.formatNumber(arBalance + apBalance + acBalance, "$###,###,##0.00");
                if (netAmt2.contains("-")) {
                    createHeaderCell(netAmt2.replace("-", ""), subHeaderTwoCellStyleRightNormal);
                } else {
                    createHeaderCell(netAmt2, subHeaderTwoCellStyleRightNormal);
                }
                createEmptyCell(subHeaderTwoCellStyleLeftBold);
                if (apReportsForm.isEcuLineReport() || agentFlag) {
                    if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 13);
                    } else {
                        mergeCells(rowIndex, rowIndex, 4, 13);
                    }
                } else {
                    if (null != vendor) {
                        mergeCells(rowIndex, rowIndex, 6, 11);
                    } else {
                        mergeCells(rowIndex, rowIndex, 4, 11);
                    }
                }
                createRow();
                resetColumnIndex();
            }
        }
        createHeaderCell("Statement Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), subHeaderOneCellStyleLeftBold);
        if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
            mergeCells(rowIndex, rowIndex, 0, 1);
        } else {
            if (apReportsForm.isEcuLineReport() || agentFlag) {
                mergeCells(rowIndex, rowIndex, 0, 13);
            } else {
                mergeCells(rowIndex, rowIndex, 0, 11);
            }
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
            if (CommonUtils.isEqualIgnoreCase(apReportsForm.getApSpecialist(), "ALL")) {
                createHeaderCell("User : " + apReportsForm.getApSpecialist(), subHeaderOneCellStyleLeftNormal);
            } else {
                String userLoginName = new UserDAO().getLoginName(Integer.parseInt(apReportsForm.getApSpecialist()));
                createHeaderCell("User : " + (null != userLoginName ? userLoginName : ""), subHeaderOneCellStyleLeftNormal);
            }
            if (apReportsForm.isEcuLineReport() || agentFlag) {
                mergeCells(rowIndex, rowIndex, 2, 13);
            } else {
                mergeCells(rowIndex, rowIndex, 2, 11);
            }
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("ACCOUNT DETAIL", headerCellStyleCenterBold);
        if (apReportsForm.isEcuLineReport() || agentFlag) {
            mergeCells(rowIndex, rowIndex, 0, 13);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 11);
        }
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        if (apReportsForm.isEcuLineReport()) {
            createHeaderCell("Vendor#", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
            createHeaderCell("Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Reference", tableHeaderCellStyleCenterBold);
            createHeaderCell("Booking#", tableHeaderCellStyleCenterBold);
            createHeaderCell("Econo Reference", tableHeaderCellStyleCenterBold);
            createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
            createHeaderCell("Approved", tableHeaderCellStyleCenterBold);
            createHeaderCell("Balance", tableHeaderCellStyleCenterBold);
            createHeaderCell("Type", tableHeaderCellStyleCenterBold);
            createHeaderCell("Ap Specialist", tableHeaderCellStyleCenterBold);
            createHeaderCell("Query Code", tableHeaderCellStyleCenterBold);
            createHeaderCell("Ecu Comments", tableHeaderCellStyleCenterBold);
            createHeaderCell("Econo Comments", tableHeaderCellStyleCenterBold);
        } else {
            createHeaderCell("Vendor#", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
            createHeaderCell("Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Vendor Reference", tableHeaderCellStyleCenterBold);
            createHeaderCell("Booking#", tableHeaderCellStyleCenterBold);
            createHeaderCell("Econo Reference", tableHeaderCellStyleCenterBold);
            createHeaderCell("Invoice Amount", tableHeaderCellStyleCenterBold);
            createHeaderCell("Payment/ Adjustment", tableHeaderCellStyleCenterBold);
            createHeaderCell("Balance", tableHeaderCellStyleCenterBold);
            createHeaderCell("Type", tableHeaderCellStyleCenterBold);
            createHeaderCell("Ap Specialist", tableHeaderCellStyleCenterBold);
            createHeaderCell("Comments", tableHeaderCellStyleCenterBold);
            if (agentFlag) {
                createHeaderCell("Consignee", tableHeaderCellStyleCenterBold);
                createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
            }
        }
    }

    private void writeHeader(CustomerBean vendorDetails, Map<String, CustomerBean> agingBuckets) throws Exception {
        writeStatementHeader(vendorDetails, agingBuckets);
    }

    private void writeStatementContent(List<AccountingBean> transactions, CustomerBean vendor) throws Exception {
        boolean agentFlag = false;
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
        if (apReportsForm.isEcuLineReport()) {
            int rowCount = 0;
            double totalInvoiceAmount = 0d;
            for (AccountingBean transaction : transactions) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(transaction.getCustomerNumber(), textCellStyle);
                createTextCell(transaction.getCustomerName(), textCellStyle);
                createTextCell(transaction.getFormattedDate(), textCellStyle);
                createTextCell(transaction.getInvoiceOrBl(), textCellStyle);
                createTextCell(transaction.getBookingNumber(), textCellStyle);
                createTextCell(transaction.getCustomerReference(), textCellStyle);
                double invoiceAmount = Double.parseDouble(transaction.getFormattedBalance().replaceAll(",", ""));
                totalInvoiceAmount += invoiceAmount;
                createDoubleCell(invoiceAmount, doubleCellStyle);
                createDoubleCell(0d, doubleCellStyle);
                createDoubleCell(invoiceAmount, doubleCellStyle);
                createTextCell(transaction.getTransactionType(), textCellStyle);
                createTextCell(transaction.getApSpecialist(), textCellStyle);
                createTextCell("", textCellStyle);
                createTextCell("", textCellStyle);
                StringBuilder comments = new StringBuilder();
                if (CommonUtils.isNotEmpty(transaction.getInvoiceNotes())) {
                    int count = 1;
                    for (String comment : StringUtils.splitByWholeSeparator(transaction.getInvoiceNotes(), "<--->")) {
                        comments.append(count).append(") ").append(comment).append("\n");
                        count++;
                    }
                }
                createTextCell(comments.toString(), textCellStyle);
                rowCount++;
            }
            setColumnAutoSize();
            createRow();
            resetColumnIndex();
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createTextCell("Grand Total", darkAshCellStyleRightBold);
            createDoubleCell(totalInvoiceAmount, darkAshCellStyleRightBold);
            createDoubleCell(0d, darkAshCellStyleRightBold);
            createDoubleCell(totalInvoiceAmount, darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
        } else {
            int rowCount = 0;
            double totalInvoiceAmount = 0d;
            double totalPayAdjAmount = 0d;
            double totalBalance = 0d;
            for (AccountingBean transaction : transactions) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(transaction.getCustomerNumber(), textCellStyle);
                createTextCell(transaction.getCustomerName(), textCellStyle);
                createTextCell(transaction.getFormattedDate(), textCellStyle);
                createTextCell(transaction.getInvoiceOrBl(), textCellStyle);
                createTextCell(transaction.getBookingNumber(), textCellStyle);
                createTextCell(transaction.getCustomerReference(), textCellStyle);
                double invoiceAmount = Double.parseDouble(transaction.getFormattedAmount().replaceAll(",", ""));
                totalInvoiceAmount += invoiceAmount;
                double payAdjAmount = -Double.parseDouble(transaction.getFormattedPayment().replaceAll(",", ""));
                totalPayAdjAmount += payAdjAmount;
                double balance = Double.parseDouble(transaction.getFormattedBalance().replaceAll(",", ""));
                totalBalance += balance;
                createDoubleCell(invoiceAmount, doubleCellStyle);
                createDoubleCell(payAdjAmount, doubleCellStyle);
                createDoubleCell(balance, doubleCellStyle);
                createTextCell(transaction.getTransactionType(), textCellStyle);
                createTextCell(transaction.getApSpecialist(), textCellStyle);
                StringBuilder comments = new StringBuilder();
                if (CommonUtils.isNotEmpty(transaction.getInvoiceNotes())) {
                    int count = 1;
                    for (String comment : StringUtils.splitByWholeSeparator(transaction.getInvoiceNotes(), "<--->")) {
                        comments.append(count).append(") ").append(comment).append("\n");
                        count++;
                    }
                }
                createTextCell(comments.toString(), textCellStyle);
                if (agentFlag) {
                    createTextCell(transaction.getConsignee(), textCellStyle);
                    createTextCell(transaction.getVoyage(), textCellStyle);
                }
                rowCount++;
            }
            setColumnAutoSize();
            createRow();
            resetColumnIndex();
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createTextCell("Grand Total", darkAshCellStyleRightBold);
            createDoubleCell(totalInvoiceAmount, darkAshCellStyleRightBold);
            createDoubleCell(totalPayAdjAmount, darkAshCellStyleRightBold);
            createDoubleCell(totalBalance, darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            if (agentFlag) {
                createEmptyCell(darkAshCellStyleRightBold);
                createEmptyCell(darkAshCellStyleRightBold);
            }
        }
        createRow();
        resetColumnIndex();
        StringBuilder apSpecialistDetails = new StringBuilder();
        if (null != vendor && CommonUtils.isNotEmpty(vendor.getApSpecialist())) {
            apSpecialistDetails.append(vendor.getApSpecialist()).append("(").append(vendor.getEmail()).append(")");
        }
        createHeaderCell(companyName + " Account Contact: " + apSpecialistDetails.toString(), subHeaderOneCellStyleLeftBold);
        if (apReportsForm.isEcuLineReport() || agentFlag) {
            mergeCells(rowIndex, rowIndex, 0, 13);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 11);
        }
    }

    private void writeContent(List<AccountingBean> transactions, CustomerBean vendorDetails) throws Exception {
        writeStatementContent(transactions, vendorDetails);
    }

    public String createExcel(List<AccountingBean> transactions, CustomerBean vendorDetails, Map<String, CustomerBean> agingBuckets) throws Exception {
        try {
            String sheetName = "AP Statement";
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ApReports/Statement/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            if (apReportsForm.isAllCustomers()) {
                fileName.append("AllCustomer");
            } else if (CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
                fileName.append("ApSpecialist");
            } else {
                fileName.append(apReportsForm.getVendorNumber());
            }
            fileName.append(".xlsx");
            init(fileName.toString(), sheetName);
            writeHeader(vendorDetails, agingBuckets);
            writeContent(transactions, vendorDetails);
            writeIntoFile();
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
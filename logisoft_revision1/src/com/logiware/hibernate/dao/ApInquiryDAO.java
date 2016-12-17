/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.ApInquiryForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;

/**
 *
 * @author lakshh
 */
public class ApInquiryDAO extends BaseHibernateDAO implements ConstantsInterface {

    public String getDocReceiptsForVoyage(String voyageNo, String transactionType) {
	StringBuilder queryBuilder = new StringBuilder("select distinct(concat(\"'\",drcpt,\"'\")) from ");
	if (transactionType.equals(TRANSACTION_TYPE_ACCRUALS)) {
	    queryBuilder.append("transaction_ledger");
	} else {
	    queryBuilder.append("transaction");
	}
	queryBuilder.append(" where transaction_type='").append(transactionType).append("'");
	queryBuilder.append(" and drcpt != '' and voyage_no like '%").append(voyageNo).append("%'");
	List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	if (CommonUtils.isNotEmpty(result)) {
	    return result.toString().replace("[", "(").replace("]", ")");
	}
	return null;
    }

    public String buildApQuery(ApInquiryForm apInquiryForm) throws Exception {
	StringBuilder queryBuilder = new StringBuilder(" from transaction tr join trading_partner tp on tp.acct_no = tr.cust_no");
	queryBuilder.append(" where tr.Transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
	if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
		queryBuilder.append(" and tr.Container_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
		queryBuilder.append(" and tr.drcpt like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_HOUSE_BILL)) {
		queryBuilder.append(" and tr.Bill_Ladding_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_SUB_HOUSE_BILL)) {
		queryBuilder.append(" and tr.Sub_House_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
		queryBuilder.append(" and (tr.Voyage_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		String drcpts = getDocReceiptsForVoyage(apInquiryForm.getSearchBy().trim(), TRANSACTION_TYPE_ACCOUNT_PAYABLE);
		if (CommonUtils.isNotEmpty(drcpts)) {
		    queryBuilder.append(" or tr.drcpt in ").append(drcpts);
		}
		queryBuilder.append(")");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_MASTER_BILL)) {
		queryBuilder.append(" and tr.Master_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_BOOKING_NUMBER)) {
		queryBuilder.append(" and tr.booking_no like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)) {
		queryBuilder.append(" and (tr.Cheque_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.ach_batch_sequence like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.Ap_Batch_Id like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.Ar_Batch_Id like '%").append(apInquiryForm.getSearchValue().trim()).append("%')");
		queryBuilder.append(" and tr.Status = '").append(STATUS_PAID).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT)) {
		String checkNumber = getCheckNumberFromCheckAmount(apInquiryForm.getSearchValue().trim().replaceAll(",", ""));
		queryBuilder.append(" and (tr.Cheque_number in ").append(checkNumber);
		queryBuilder.append(" or tr.ach_batch_sequence in ").append(checkNumber).append(")");
		queryBuilder.append(" and tr.Status = '").append(STATUS_PAID).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)) {
		queryBuilder.append(" and tr.Invoice_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_AMOUNT)) {
		queryBuilder.append(" and tr.Transaction_amt = ").append(apInquiryForm.getSearchValue().replaceAll("[^0-9.]", ""));
	    }
	    if (CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
		queryBuilder.append(" and tr.Status = '").append(STATUS_REJECT).append("'");
	    } else if (CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), NO)) {
		queryBuilder.append(" and tr.Status != '").append(STATUS_REJECT).append("'");
	    }
	} else {
	    if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
		if (CommonUtils.isEqual(apInquiryForm.getShowAssociatedCompanies(), YES)) {
		    queryBuilder.append(" and (tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no='").append(apInquiryForm.getVendorNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		}
	    }
	    if (CommonUtils.isNotEmpty(apInquiryForm.getInvoiceAmount()) && CommonUtils.isNotEmpty(apInquiryForm.getInvoiceOperator())) {
		queryBuilder.append(" and tr.Transaction_amt").append(apInquiryForm.getInvoiceOperator()).append(apInquiryForm.getInvoiceAmount());
	    }
	    boolean hasOtherStatus = false;
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), ONLY)
		    || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
		String status = CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), ONLY) ? STATUS_READY_TO_PAY : STATUS_REJECT;
		queryBuilder.append(" and tr.Status = '").append(status).append("'");
	    } else {
		queryBuilder.append(" and (");
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), STATUS_OPEN)) {
		    queryBuilder.append(" tr.Status = '").append(STATUS_OPEN).append("'");
		    queryBuilder.append(" or tr.Status = '").append(STATUS_PENDING).append("'");
		    hasOtherStatus = true;
		} else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), STATUS_PAID)) {
		    queryBuilder.append(" (tr.Status = '").append(STATUS_PAID).append("' and tr.Balance = 0)");
		    hasOtherStatus = true;
		} else {
		    queryBuilder.append(" (tr.Status = '").append(STATUS_OPEN).append("'");
		    queryBuilder.append(" or tr.Status = '").append(STATUS_PENDING).append("'");
		    queryBuilder.append(" or tr.Status = '").append(STATUS_HOLD).append("'");
		    queryBuilder.append(" or (tr.Status = '").append(STATUS_PAID).append("' and tr.Balance = 0))");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or ");
		    }
		    queryBuilder.append(" tr.Status = '").append(STATUS_READY_TO_PAY).append("'");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or ");
		    }
		    queryBuilder.append(" tr.Status = '").append(STATUS_REJECT).append("'");
		    hasOtherStatus = true;
		}
		queryBuilder.append(")");
	    }
	    if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate()) || CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
		String searchDate = null;
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), INVOICE_DATE)) {
		    searchDate = "tr.Transaction_date";
		} else {
		    searchDate = "tr.check_date";
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
		    queryBuilder.append(" and date_format(").append(searchDate).append(",'%m/%d/%Y') >= '").append(apInquiryForm.getFromDate()).append("'");
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
		    queryBuilder.append(" and date_format(").append(searchDate).append(",'%m/%d/%Y') <= '").append(apInquiryForm.getToDate()).append("'");
		}
	    }
	}
	queryBuilder.append(" group by tr.cust_no,if(tr.Invoice_number!='',tr.Invoice_number,tr.Bill_Ladding_No),tr.Status");
	queryBuilder.append(" order by ");
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), PAYMENT_DATE)) {
	    queryBuilder.append("tr.check_date,");
	}
	queryBuilder.append("tr.cust_no,if(tr.Invoice_number!='',tr.Invoice_number,tr.Bill_Ladding_No),tr.Status");
	return queryBuilder.toString();
    }

    public String buildAcQuery(ApInquiryForm apInquiryForm) {
	StringBuilder queryBuilder = new StringBuilder(" from transaction_ledger tl join trading_partner tp on tp.acct_no = tl.cust_no");
	queryBuilder.append(" where tl.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
	if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    queryBuilder.append(" and tl.accruals_correction_flag is null");
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
		queryBuilder.append(" and tl.Container_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
		queryBuilder.append(" and tl.drcpt like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_HOUSE_BILL)) {
		queryBuilder.append(" and tl.Bill_Ladding_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_SUB_HOUSE_BILL)) {
		queryBuilder.append(" and tl.Sub_House_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
		queryBuilder.append(" and (tl.Voyage_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		String drcpts = getDocReceiptsForVoyage(apInquiryForm.getSearchBy().trim(), TRANSACTION_TYPE_ACCRUALS);
		if (CommonUtils.isNotEmpty(drcpts)) {
		    queryBuilder.append(" or tl.drcpt in ").append(drcpts);
		}
		queryBuilder.append(")");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_MASTER_BILL)) {
		queryBuilder.append(" and tl.Master_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_BOOKING_NUMBER)) {
		queryBuilder.append(" and tl.booking_no like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)) {
		queryBuilder.append(" and tl.Cheque_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT)) {
		String checkNumber = getCheckNumberFromCheckAmount(apInquiryForm.getSearchValue().trim().replaceAll(",", ""));
		queryBuilder.append(" and tl.Cheque_number in ").append(checkNumber);
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)) {
		queryBuilder.append(" and tl.Invoice_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_AMOUNT)) {
		queryBuilder.append(" and tl.Transaction_amt = ").append(apInquiryForm.getSearchValue().replaceAll("[^0-9.]", ""));
	    }
	} else {
	    if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
		if (CommonUtils.isEqual(apInquiryForm.getShowAssociatedCompanies(), YES)) {
		    queryBuilder.append(" and (tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no='").append(apInquiryForm.getVendorNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		}
	    }
	    queryBuilder.append(" and tl.accruals_correction_flag is null");
	    if (CommonUtils.isNotEmpty(apInquiryForm.getInvoiceAmount()) && CommonUtils.isNotEmpty(apInquiryForm.getInvoiceOperator())) {
		queryBuilder.append(" and tl.Transaction_amt").append(apInquiryForm.getInvoiceOperator()).append(apInquiryForm.getInvoiceAmount());
	    }
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)) {
		queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
		queryBuilder.append(" or tl.Status='").append(STATUS_EDI_IN_PROGRESS).append("'");
		queryBuilder.append(" or tl.Status='").append(STATUS_IN_PROGRESS).append("')");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)) {
		queryBuilder.append(" and (tl.Status='").append(STATUS_ASSIGN).append("'");
		queryBuilder.append(" or tl.Status='").append(STATUS_EDI_ASSIGNED).append("')");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY)) {
		queryBuilder.append(" and tl.Status='").append(STATUS_INACTIVE).append("'");
	    } else {
		queryBuilder.append(" and (");
		boolean hasOtherStatus = false;
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), YES)) {
		    queryBuilder.append(" tl.status='").append(STATUS_OPEN).append("'");
		    queryBuilder.append(" or tl.status='").append(STATUS_EDI_IN_PROGRESS).append("'");
		    queryBuilder.append(" or tl.status='").append(STATUS_IN_PROGRESS).append("'");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or");
		    }
		    queryBuilder.append(" (tl.status='").append(STATUS_ASSIGN).append("'");
		    queryBuilder.append(" or tl.status='").append(STATUS_EDI_ASSIGNED).append("')");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or");
		    }
		    queryBuilder.append(" tl.status='").append(STATUS_INACTIVE).append("'");
		}
		queryBuilder.append(")");
	    }
	}
	queryBuilder.append(" order by ");
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), PAYMENT_DATE)) {
	    queryBuilder.append("tl.check_date,");
	}
	queryBuilder.append("tl.cust_no,if(tl.Invoice_number!='',tl.Invoice_number,if(tl.Bill_Ladding_No!='',");
	queryBuilder.append("tl.Bill_Ladding_No,if(tl.drcpt!='',tl.drcpt,tl.Voyage_No))),tl.Status");
	return queryBuilder.toString();
    }

    public String buildArQuery(ApInquiryForm apInquiryForm) throws Exception {
	StringBuilder queryBuilder = new StringBuilder(" from transaction tr join trading_partner tp on tp.acct_no = tr.cust_no");
	queryBuilder.append(" where tr.Transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CONTAINER)) {
		queryBuilder.append(" and tr.Container_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_DOCK_RECEIPT)) {
		queryBuilder.append(" and tr.drcpt like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_HOUSE_BILL)) {
		queryBuilder.append(" and tr.Bill_Ladding_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_SUB_HOUSE_BILL)) {
		queryBuilder.append(" and tr.Sub_House_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_VOYAGE)) {
		queryBuilder.append(" and (tr.Voyage_No like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		String drcpts = getDocReceiptsForVoyage(apInquiryForm.getSearchBy().trim(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
		if (CommonUtils.isNotEmpty(drcpts)) {
		    queryBuilder.append(" or tr.drcpt in ").append(drcpts);
		}
		queryBuilder.append(")");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_MASTER_BILL)) {
		queryBuilder.append(" and tr.Master_BL like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_BOOKING_NUMBER)) {
		queryBuilder.append(" and tr.booking_no like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_NUMBER)) {
		queryBuilder.append(" and (tr.Cheque_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.ach_batch_sequence like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.Ap_Batch_Id like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" or tr.Ar_Batch_Id like '%").append(apInquiryForm.getSearchValue().trim()).append("%')");
		queryBuilder.append(" and tr.Status = '").append(STATUS_PAID).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_CHECK_AMOUNT)) {
		String checkNumber = getCheckNumberFromCheckAmount(apInquiryForm.getSearchValue().trim().replaceAll(",", ""));
		queryBuilder.append(" and (tr.Cheque_number ").append(checkNumber);
		queryBuilder.append(" or tr.ach_batch_sequence ").append(checkNumber).append(")");
		queryBuilder.append(" and tr.Status = '").append(STATUS_PAID).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)) {
		queryBuilder.append(" and tr.Invoice_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_AMOUNT)) {
		queryBuilder.append(" and tr.Transaction_amt = ").append(apInquiryForm.getSearchValue().replaceAll("[^0-9.]", ""));
	    }
	} else {
	    if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
		if (CommonUtils.isEqual(apInquiryForm.getShowAssociatedCompanies(), YES)) {
		    queryBuilder.append(" and (tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no='").append(apInquiryForm.getVendorNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no='").append(apInquiryForm.getVendorNumber()).append("'");
		}
	    }
	    if (CommonUtils.isNotEmpty(apInquiryForm.getInvoiceAmount()) && CommonUtils.isNotEmpty(apInquiryForm.getInvoiceOperator())) {
		queryBuilder.append(" and tr.Transaction_amt").append(apInquiryForm.getInvoiceOperator()).append(apInquiryForm.getInvoiceAmount());
	    }
	    if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate()) || CommonUtils.isNotEmpty(apInquiryForm.getToDate())) {
		String searchDate = null;
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), INVOICE_DATE)) {
		    searchDate = "tr.Transaction_date";
		} else {
		    searchDate = "tr.check_date";
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
		    queryBuilder.append(" and date_format(").append(searchDate).append(",'%m/%d/%Y') >= '").append(apInquiryForm.getFromDate()).append("'");
		}
		if (CommonUtils.isNotEmpty(apInquiryForm.getFromDate())) {
		    queryBuilder.append(" and date_format(").append(searchDate).append(",'%m/%d/%Y') <= '").append(apInquiryForm.getToDate()).append("'");
		}
	    }
	}
	boolean hasOtherStatus = false;
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), ONLY)
		|| CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
	    String status = CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), ONLY) ? STATUS_READY_TO_PAY : STATUS_REJECT;
	    queryBuilder.append(" and tr.Status = '").append(status).append("'");
	} else {
	    queryBuilder.append(" and (");
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), STATUS_OPEN)) {
		queryBuilder.append(" ((tr.Status = '").append(STATUS_OPEN).append("' or tr.Status is null or tr.Status='') and tr.Balance<>0)");
		hasOtherStatus = true;
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInvoices(), STATUS_PAID)) {
		queryBuilder.append(" ((tr.Status = '").append(STATUS_PAID).append("' or tr.Status is null or tr.Status='') and tr.Balance=0)");
		hasOtherStatus = true;
	    } else {
		queryBuilder.append(" ((tr.Status = '").append(STATUS_OPEN).append("' or tr.Status is null or tr.Status='') and tr.Balance<>0)");
		queryBuilder.append(" or ((tr.Status = '").append(STATUS_PAID).append("' or tr.Status is null or tr.Status='') and tr.Balance=0)");
		queryBuilder.append(" or tr.Status = '").append(STATUS_HOLD).append("'");
		hasOtherStatus = true;
	    }
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowReadyToPayInvoices(), YES)) {
		if (hasOtherStatus) {
		    queryBuilder.append(" or ");
		}
		queryBuilder.append(" tr.Status = '").append(STATUS_READY_TO_PAY).append("'");
		hasOtherStatus = true;
	    }
	    if (CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), YES)) {
		if (hasOtherStatus) {
		    queryBuilder.append(" or ");
		}
		queryBuilder.append(" tr.Status = '").append(STATUS_REJECT).append("'");
	    }
	    queryBuilder.append(")");
	}
	queryBuilder.append(" group by tr.cust_no,if(tr.Invoice_number!='',tr.Invoice_number,tr.Bill_Ladding_No),tr.Status");
	queryBuilder.append(" order by ");
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), PAYMENT_DATE)) {
	    queryBuilder.append("tr.check_date,");
	}
	queryBuilder.append("tr.cust_no,if(tr.Invoice_number!='',tr.Invoice_number,tr.Bill_Ladding_No),tr.Status");
	return queryBuilder.toString();
    }

    public String buildApInvoiceQuery(ApInquiryForm apInquiryForm) {
	if (CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
		&& CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    return null;
	}
	StringBuilder queryBuilder = new StringBuilder(" from ap_invoice ap join trading_partner tp on tp.acct_no = ap.account_number");
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
		&& CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    queryBuilder.append(" and ap.invoice_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
	    queryBuilder.append(" and (ap.status='").append(STATUS_OPEN).append("'");
	    queryBuilder.append(" or ap.status='").append(STATUS_IN_PROGRESS).append("'");
	    queryBuilder.append(" or ap.status='").append(STATUS_REJECT).append("'");
	    queryBuilder.append(" or ap.status = '").append(STATUS_DISPUTE).append("'");
	    queryBuilder.append(" or ap.status = '").append(STATUS_VOID).append("')");
	} else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_AMOUNT)) {
	    queryBuilder.append(" and ap.invoice_number!=''");
	    queryBuilder.append(" and ap.invoice_amount = ").append(apInquiryForm.getSearchValue().replaceAll("[^0-9.]", ""));
	    queryBuilder.append(" and (ap.status='").append(STATUS_OPEN).append("'");
	    queryBuilder.append(" or ap.status='").append(STATUS_IN_PROGRESS).append("'");
	    queryBuilder.append(" or ap.status='").append(STATUS_REJECT).append("'");
	    queryBuilder.append(" or ap.status = '").append(STATUS_DISPUTE).append("'");
	    queryBuilder.append(" or ap.status = '").append(STATUS_VOID).append("')");
	} else {
	    if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
		if (CommonUtils.isEqual(apInquiryForm.getShowAssociatedCompanies(), YES)) {
		    queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
		}
	    }
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)) {
		queryBuilder.append(" and (ap.status='").append(STATUS_OPEN).append("'");
		queryBuilder.append(" or ap.status='").append(STATUS_IN_PROGRESS).append("')");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
		queryBuilder.append(" and ap.status='").append(STATUS_REJECT).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)) {
		queryBuilder.append(" and ap.status = '").append(STATUS_DISPUTE).append("'");
	    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
		queryBuilder.append(" and ap.status = '").append(STATUS_VOID).append("'");
	    } else {
		queryBuilder.append(" and (");
		boolean hasOtherStatus = false;
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)) {
		    queryBuilder.append(" ap.status='").append(STATUS_OPEN).append("'");
		    queryBuilder.append(" or ap.status='").append(STATUS_IN_PROGRESS).append("'");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or");
		    }
		    queryBuilder.append(" ap.status='").append(STATUS_REJECT).append("'");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or");
		    }
		    queryBuilder.append(" ap.status='").append(STATUS_DISPUTE).append("'");
		    hasOtherStatus = true;
		}
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), YES)) {
		    if (hasOtherStatus) {
			queryBuilder.append(" or");
		    }
		    queryBuilder.append(" ap.status='").append(STATUS_VOID).append("'");
		}
		queryBuilder.append(")");
	    }
	}
	queryBuilder.append(" group by ap.account_number,ap.invoice_number,ap.status");
	queryBuilder.append(" order by ap.account_number,ap.invoice_number,ap.status");
	return queryBuilder.toString();
    }

    public String buildEdiInvoiceQuery(ApInquiryForm apInquiryForm) {
	if (!CommonUtils.in(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER, SEARCH_BY_INVOICE_AMOUNT)
		&& CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
	    return null;
	} else {
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append(" from edi_invoice edi");
	    queryBuilder.append(" join trading_partner tp");
	    queryBuilder.append(" on tp.acct_no = edi.vendor_number");
	    if (CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
		    && CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)) {
		queryBuilder.append(" where edi.invoice_number!=''");
		queryBuilder.append(" and edi.invoice_number like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
		queryBuilder.append(" and (edi.status='").append(STATUS_EDI_OPEN).append("'");
		queryBuilder.append(" or edi.status='").append(STATUS_EDI_IN_PROGRESS).append("'");
		queryBuilder.append(" or edi.status='").append(STATUS_EDI_DISPUTE).append("'");
		queryBuilder.append(" or edi.status = '").append(STATUS_EDI_READY_TO_POST).append("'");
		queryBuilder.append(" or edi.status = '").append(STATUS_EDI_READY_TO_POST_FULLY_MAPPED).append("')");
	    } else if (CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
		    && CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_AMOUNT)) {
		queryBuilder.append(" where edi.invoice_number!=''");
		queryBuilder.append(" and edi.invoice_amount = ").append(apInquiryForm.getSearchValue().replaceAll("[^0-9.]", ""));
		queryBuilder.append(" and (edi.status='").append(STATUS_EDI_OPEN).append("'");
		queryBuilder.append(" or edi.status='").append(STATUS_EDI_IN_PROGRESS).append("'");
		queryBuilder.append(" or edi.status='").append(STATUS_EDI_DISPUTE).append("'");
		queryBuilder.append(" or edi.status = '").append(STATUS_EDI_READY_TO_POST).append("'");
		queryBuilder.append(" or edi.status = '").append(STATUS_EDI_READY_TO_POST_FULLY_MAPPED).append("')");
	    } else {
		if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
		    if (CommonUtils.isEqual(apInquiryForm.getShowAssociatedCompanies(), YES)) {
			queryBuilder.append(" and (tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
			queryBuilder.append(" or tp.masteracct_no = '").append(apInquiryForm.getVendorNumber()).append("')");
		    } else {
			queryBuilder.append(" and tp.acct_no = '").append(apInquiryForm.getVendorNumber()).append("'");
		    }
		}
		queryBuilder.append(" where edi.invoice_number!=''");
		if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)) {
		    queryBuilder.append(" and (edi.status='").append(STATUS_EDI_OPEN).append("'");
		    queryBuilder.append(" or edi.status='").append(STATUS_EDI_IN_PROGRESS).append("')");
		} else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)) {
		    queryBuilder.append(" and edi.status = '").append(STATUS_EDI_DISPUTE).append("'");
		} else {
		    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)) {
			queryBuilder.append(" and (edi.status='").append(STATUS_EDI_OPEN).append("'");
			if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)) {
			    queryBuilder.append(" and edi.status = '").append(STATUS_EDI_DISPUTE).append("'");
			}
			queryBuilder.append(" or edi.status='").append(STATUS_EDI_IN_PROGRESS).append("')");
		    } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)) {
			queryBuilder.append(" and edi.status = '").append(STATUS_EDI_DISPUTE).append("'");
		    }
		}
	    }
	    queryBuilder.append(" group by edi.vendor_number,edi.invoice_number,edi.status");
	    queryBuilder.append(" order by edi.vendor_number,edi.invoice_number,edi.status");
	    return queryBuilder.toString();
	}
    }

    public Integer getTotalPageSize(ApInquiryForm apInquiryForm, String arQuery, String apQuery, String acQuery, String apInvoiceQuery, String ediInvoiceQuery) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select count(totalRows) from (");
	boolean hasUnion = false;
	if (null != apQuery) {
	    queryBuilder.append("(select tr.Transaction_ID as totalRows").append(apQuery).append(")");
	    hasUnion = true;
	}
	if (null != acQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tl.Transaction_ID as totalRows").append(acQuery).append(")");
	    hasUnion = true;
	}
	if (null != arQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tr.Transaction_ID as totalRows").append(arQuery).append(")");
	    hasUnion = true;
	}
	if (null != apInvoiceQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select ap.id as totalRows").append(apInvoiceQuery).append(")");
	    hasUnion = true;
	}
	if (null != ediInvoiceQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select edi.id as totalRows").append(ediInvoiceQuery).append(")");
	}
	queryBuilder.append(") as tbl");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    public List<AccountingBean> search(ApInquiryForm apInquiryForm, String apQuery, String acQuery, String arQuery, String apInvoiceQuery, String ediInvoiceQuery) {
	List<AccountingBean> apInquiryList = new ArrayList<AccountingBean>();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select tbl.acctNo,tbl.acctName,tbl.invoiceOrBl,date_format(tbl.invoiceDate,'%m/%d/%Y') as invoiceDate,");
	queryBuilder.append("date_format(tbl.dueDate,'%m/%d/%Y') as dueDate,format(tbl.invoiceAmount,2) as invoiceAmount,format(tbl.balance,2) as balance,");
	queryBuilder.append("tbl.checkNumber as checkNumber,date_format(tbl.clearedDate,'%m/%d/%Y') as clearedDate,date_format(tbl.paymentDate,'%m/%d/%Y') as paymentDate,");
	queryBuilder.append("tbl.transactionType,tbl.customerReference,");
	queryBuilder.append("if(tbl.transactionType='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	queryBuilder.append(" and tbl.balance=0,'").append(STATUS_PAID).append("',tbl.status) as status,");
	queryBuilder.append("tbl.correctionNotice,tbl.id,");
	queryBuilder.append("tbl.dockReceipt,tbl.voyage,tbl.costCode,tbl.invoiceNumber,tbl.blNumber,");
	queryBuilder.append("tbl.subType,tbl.costId,if(count(doc.document_id)>0,'true','false') as hasDocuments,");
	queryBuilder.append("if(count(note.id)>0,'true','false') as manualNotes");
	queryBuilder.append(" from (");
	queryBuilder.append("select tbl.acctNo,tbl.acctName,tbl.invoiceOrBl,tbl.invoiceDate as invoiceDate,");
	queryBuilder.append("tbl.dueDate,tbl.invoiceAmount,tbl.balance as balance,");
	queryBuilder.append("tbl.checkNumber,tbl.clearedDate,tbl.paymentDate,");
	queryBuilder.append("tbl.transactionType,tbl.customerReference,");
	queryBuilder.append("tbl.status as status,");
	queryBuilder.append("tbl.correctionNotice,tbl.id,");
	queryBuilder.append("tbl.dockReceipt,tbl.voyage,tbl.costCode,tbl.invoiceNumber,tbl.blNumber,");
	queryBuilder.append("tbl.subType,tbl.costId,tbl.note_module_id,tbl.note_ref_id");
	queryBuilder.append(" from (");
	boolean hasUnion = false;
	if (null != apQuery) {
	    queryBuilder.append("(select tp.acct_no as acctNo,tp.acct_name as acctName,");
	    queryBuilder.append("if(tr.Invoice_number!='',tr.Invoice_number,tr.Bill_Ladding_No) as invoiceOrBl,tr.Transaction_date as invoiceDate,");
	    queryBuilder.append("tr.Due_Date as dueDate,sum(tr.Transaction_amt) as invoiceAmount,sum(tr.Balance) as balance,");
	    queryBuilder.append("if(tr.ach_batch_sequence!='',concat('ACH - ',tr.ach_batch_sequence),tr.Cheque_number) as checkNumber,");
	    queryBuilder.append("tr.cleared_date as clearedDate,tr.check_date as paymentDate,tr.Transaction_type as transactionType,");
	    queryBuilder.append("tr.Customer_Reference_No as customerReference,tr.Status as status,tr.correction_notice as correctionNotice,");
	    queryBuilder.append("cast(group_concat(tr.Transaction_ID) as char) as id,null as dockReceipt,null as voyage,null as costCode,");
	    queryBuilder.append("tr.Invoice_number as invoiceNumber,tr.Bill_Ladding_No as blNumber,if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
	    queryBuilder.append("null as costId,cast('AP_INVOICE' as char) as note_module_id,concat(tp.acct_no,'-',tr.invoice_number) as note_ref_id");
	    queryBuilder.append(apQuery).append(")");
	    hasUnion = true;
	}
	if (null != acQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tp.acct_no as acctNo,tp.acct_name as acctName,");
	    queryBuilder.append("if(tl.Invoice_number!='',tl.Invoice_number,tl.Bill_Ladding_No) as invoiceOrBl,tl.Transaction_date as invoiceDate,");
	    queryBuilder.append("tl.Due_Date as dueDate,tl.Transaction_amt as invoiceAmount,tl.Balance as balance,");
	    queryBuilder.append("tl.Cheque_number as checkNumber,tl.cleared_date as clearedDate,tl.check_date as paymentDate,");
	    queryBuilder.append("tl.Transaction_type as transactionType,tl.Customer_Reference_No as customerReference,tl.Status as status,");
	    queryBuilder.append("tl.correction_notice as correctionNotice,cast(tl.Transaction_ID as char) as id,");
	    queryBuilder.append("tl.drcpt as dockReceipt,tl.Voyage_No as voyage,tl.Charge_Code as costCode,");
	    queryBuilder.append("tl.Invoice_number as invoiceNumber,tl.Bill_Ladding_No as blNumber,if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
	    queryBuilder.append("cast(tl.cost_id as char) as costId,cast('ACCRUALS' as char) as note_module_id,cast(tl.Transaction_ID as char) as note_ref_id");
	    queryBuilder.append(acQuery).append(")");
	    hasUnion = true;
	}
	if (null != arQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tp.acct_no as acctNo,tp.acct_name as acctName,");
	    queryBuilder.append("if(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number) as invoiceOrBl,tr.Transaction_date as invoiceDate,");
	    queryBuilder.append("tr.Due_Date as dueDate,sum(tr.Transaction_amt) as invoiceAmount,sum(tr.Balance) as balance,");
	    queryBuilder.append("if(tr.ach_batch_sequence!='',concat('ACH - ',tr.ach_batch_sequence),tr.Cheque_number) as checkNumber,");
	    queryBuilder.append("tr.cleared_date as clearedDate,tr.check_date as paymentDate,tr.Transaction_type as transactionType,");
	    queryBuilder.append("tr.Customer_Reference_No as customerReference,tr.Status as status,tr.correction_notice as correctionNotice,");
	    queryBuilder.append("cast(group_concat(tr.Transaction_ID) as char) as id,null as dockReceipt,null as voyage,null as costCode,");
	    queryBuilder.append("tr.Invoice_number as invoiceNumber,tr.Bill_Ladding_No as blNumber,if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
	    queryBuilder.append("null as costId,cast('AR_INVOICE' as char) as note_module_id");
	    queryBuilder.append(",concat(tp.acct_no,'-',if(tr.Bill_Ladding_No!='',tr.Bill_Ladding_No,tr.Invoice_number)) as note_ref_id");
	    queryBuilder.append(arQuery).append(")");
	    hasUnion = true;
	}
	if (null != apInvoiceQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tp.acct_no as acctNo,tp.acct_name as acctName,ap.invoice_number as invoiceOrBl,ap.date as invoiceDate,");
	    queryBuilder.append("ap.due_date as dueDate,sum(ap.invoice_amount) as invoiceAmount,sum(ap.invoice_amount) as balance,null as checkNumber,");
	    queryBuilder.append("null as clearedDate,null as paymentDate,null as transactionType,null as customerReference,");
	    queryBuilder.append("if(ap.status='Open','I',ap.status) as status,null as correctionNotice,cast(group_concat(ap.id) as char) as id,");
	    queryBuilder.append("null as dockReceipt,null as voyage,null as costCode,ap.invoice_number as invoiceNumber,null as blNumber,");
	    queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
	    queryBuilder.append("null as costId,cast('AP_INVOICE' as char) as note_module_id,concat(tp.acct_no,'-',ap.invoice_number) as note_ref_id");
	    queryBuilder.append(apInvoiceQuery).append(")");
	    hasUnion = true;
	}
	if (null != ediInvoiceQuery) {
	    if (hasUnion) {
		queryBuilder.append(" union ");
	    }
	    queryBuilder.append("(select tp.acct_no as acctNo,tp.acct_name as acctName,edi.invoice_number as invoiceOrBl,edi.invoice_date as invoiceDate,");
	    queryBuilder.append("edi.invoice_date as dueDate,sum(edi.invoice_amount) as invoiceAmount,sum(edi.invoice_amount) as balance,null as checkNumber,");
	    queryBuilder.append("null as clearedDate,null as paymentDate,null as transactionType,null as customerReference,");
	    queryBuilder.append("edi.status as status,null as correctionNotice,cast(group_concat(edi.id) as char) as id,");
	    queryBuilder.append("null as dockReceipt,null as voyage,null as costCode,edi.invoice_number as invoiceNumber,null as blNumber,");
	    queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type = 'Overhead','Y','N') as subType,");
	    queryBuilder.append("null as costId,cast('AP_INVOICE' as char) as note_module_id,concat(tp.acct_no,'-',edi.invoice_number) as note_ref_id");
	    queryBuilder.append(ediInvoiceQuery).append(")");
	}
	queryBuilder.append(") as tbl");
	if (CommonUtils.isNotEmpty(apInquiryForm.getSortBy()) && CommonUtils.isNotEmpty(apInquiryForm.getOrderBy())) {
	    queryBuilder.append(" order by ").append(apInquiryForm.getSortBy()).append(" ").append(apInquiryForm.getOrderBy());
	} else {
	    queryBuilder.append(" order by ");
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), PAYMENT_DATE)) {
		queryBuilder.append("tbl.paymentDate,");
	    }
	    queryBuilder.append("tbl.acctNo,tbl.invoiceOrBl,tbl.status");
	}
	if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getAction(), "getForApInquiry")) {
	    int start = (apInquiryForm.getCurrentPageSize() * (apInquiryForm.getPageNo() - 1));
	    int end = apInquiryForm.getCurrentPageSize();
	    queryBuilder.append(" limit ").append(start).append(",").append(end);
	}
	queryBuilder.append(") as tbl");
	queryBuilder.append(" left join document_store_log doc on (doc.screen_name='INVOICE' and doc.document_name='INVOICE'");
	queryBuilder.append(" and doc.document_id=concat(tbl.acctNo,'-',invoiceOrBl))");
	queryBuilder.append(" left join notes note on tbl.note_module_id=note.module_id");
	queryBuilder.append(" and tbl.note_ref_id=note.module_ref_id and note.note_type='Manual'");
	queryBuilder.append(" group by tbl.acctno,tbl.invoiceorbl,tbl.transactionType,tbl.id");
	if (CommonUtils.isNotEmpty(apInquiryForm.getSortBy()) && CommonUtils.isNotEmpty(apInquiryForm.getOrderBy())) {
	    queryBuilder.append(" order by ").append(apInquiryForm.getSortBy()).append(" ").append(apInquiryForm.getOrderBy());
	} else {
	    queryBuilder.append(" order by ");
	    if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchDateBy(), PAYMENT_DATE)) {
		queryBuilder.append("tbl.paymentDate,");
	    }
	    queryBuilder.append("tbl.acctNo,tbl.invoiceOrBl,tbl.status");
	}
	SQLQuery query = getSession().createSQLQuery(queryBuilder.toString());
	query.addScalar("acctNo", StringType.INSTANCE).addScalar("acctName", StringType.INSTANCE).addScalar("invoiceOrBl", StringType.INSTANCE);
	query.addScalar("invoiceDate", StringType.INSTANCE).addScalar("dueDate", StringType.INSTANCE).addScalar("invoiceAmount", StringType.INSTANCE);
	query.addScalar("balance", StringType.INSTANCE).addScalar("checkNumber", StringType.INSTANCE).addScalar("clearedDate", StringType.INSTANCE);
	query.addScalar("paymentDate", StringType.INSTANCE).addScalar("transactionType", StringType.INSTANCE).addScalar("customerReference", StringType.INSTANCE);
	query.addScalar("status", StringType.INSTANCE).addScalar("correctionNotice", StringType.INSTANCE).addScalar("id", StringType.INSTANCE);
	query.addScalar("dockReceipt", StringType.INSTANCE).addScalar("voyage", StringType.INSTANCE).addScalar("costCode", StringType.INSTANCE);
	query.addScalar("invoiceNumber", StringType.INSTANCE).addScalar("blNumber", StringType.INSTANCE).addScalar("subType", StringType.INSTANCE);
	query.addScalar("costId", StringType.INSTANCE).addScalar("hasDocuments", StringType.INSTANCE).addScalar("manualNotes", StringType.INSTANCE);
	List<Object> result = query.list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    AccountingBean accountingBean = new AccountingBean();
	    accountingBean.setCustomerNumber((String) col[0]);
	    accountingBean.setCustomerName((String) col[1]);
	    accountingBean.setInvoiceOrBl((String) col[2]);
	    accountingBean.setFormattedDate((String) col[3]);
	    accountingBean.setFormattedDueDate((String) col[4]);
	    accountingBean.setFormattedAmount((String) col[5]);
	    accountingBean.setFormattedBalance((String) col[6]);
	    accountingBean.setCheckNumber((String) col[7]);
	    accountingBean.setClearedDate((String) col[8]);
	    accountingBean.setFormattedPaymentDate((String) col[9]);
	    accountingBean.setTransactionType((String) col[10]);
	    accountingBean.setCustomerReference((String) col[11]);
	    accountingBean.setStatus((String) col[12]);
	    accountingBean.setCorrectionNotice((String) col[13]);
	    accountingBean.setTransactionId((String) col[14]);
	    accountingBean.setDockReceipt((String) col[15]);
	    accountingBean.setVoyage((String) col[16]);
	    accountingBean.setChargeCode((String) col[17]);
	    accountingBean.setInvoiceNumber((String) col[18]);
	    accountingBean.setBillOfLadding((String) col[19]);
	    accountingBean.setSubType((String) col[20]);
	    accountingBean.setCostId((String) col[21]);
	    accountingBean.setHasDocuments(Boolean.valueOf((String) col[22]));
	    accountingBean.setManualNotes(Boolean.valueOf((String) col[23]));
	    apInquiryList.add(accountingBean);
	}
	return apInquiryList;
    }

    private CustomerBean getCustomerDetails(String customerNumber) {
	CustomerBean customerBean = new CustomerBean();
	StringBuilder queryBuilder = new StringBuilder("select tp.acct_no as acctNo,tp.acct_name as acctName,tp.sub_type as subType,");
	queryBuilder.append("concat(if(ca.address1!='',concat(ca.address1,'\n'),''),");
	queryBuilder.append("if(ca.city1!='' && ca.state!='' && country.codedesc!='',concat(ca.city1,', ',ca.state,', ',country.codedesc),");
	queryBuilder.append("if(ca.city1!='' && ca.state!='',concat(ca.city1,', ',ca.state),");
	queryBuilder.append("if(ca.city1!='' && country.codedesc!='',concat(ca.city1,', ',country.codedesc),");
	queryBuilder.append("if(ca.city1!='',ca.city1,if(ca.state!='',ca.state,if(country.codedesc!='',country.codedesc,''))))))) as address,");
	queryBuilder.append("ca.zip as zip,ve.company as contactName,ve.phone as phone,ve.fax as fax,ve.email as email,");
	queryBuilder.append("format(if(ve.c_limit!='',ve.c_limit,0),2) as creditLimit,creditTerm.codedesc as creditTerm");
	queryBuilder.append(" from trading_partner tp join vendor_info ve on ve.cust_accno=tp.acct_no");
	queryBuilder.append(" left join cust_address ca on ca.acct_no=tp.acct_no");
	queryBuilder.append(" left join genericcode_dup country on country.id=ca.country");
	queryBuilder.append(" left join genericcode_dup creditTerm on creditTerm.id=ve.credit_terms");
	queryBuilder.append(" where tp.acct_no='").append(customerNumber).append("'");
	queryBuilder.append(" order by field(ca.prime,'on') desc limit 1");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if (null != result) {
	    Object[] col = (Object[]) result;
	    customerBean.setCustomerNumber((String) col[0]);
	    customerBean.setCustomerName((String) col[1]);
	    customerBean.setSubType((String) col[2]);
	    customerBean.setAddress((String) col[3]);
	    customerBean.setZipCode((String) col[4]);
	    customerBean.setContact((String) col[5]);
	    customerBean.setPhone((String) col[6]);
	    customerBean.setFax((String) col[7]);
	    customerBean.setEmail((String) col[8]);
	    customerBean.setCreditLimit((String) col[9]);
	    customerBean.setCreditTerm((String) col[10]);
	}
	return customerBean;
    }

    public CustomerBean getCustomerDetails(ApInquiryForm apInquiryForm, String arQuery, String apQuery, String acQuery, String apInvoiceQuery, String ediInvoiceQuery) {
	CustomerBean customerBean = new CustomerBean();
	if (CommonUtils.isNotEmpty(apInquiryForm.getVendorNumber())) {
	    customerBean = getCustomerDetails(apInquiryForm.getVendorNumber());
	}
	if (null != apQuery || null != acQuery || null != arQuery || null != apInvoiceQuery) {
	    StringBuilder queryBuilder = new StringBuilder("select format(if(sum(tbl.0_30days)!='',sum(tbl.0_30days),0),2),");
	    queryBuilder.append("format(if(sum(tbl.31_60days),sum(tbl.31_60days),0),2),format(if(sum(tbl.61_90days),sum(tbl.61_90days),0),2),");
	    queryBuilder.append("format(if(sum(tbl.91plusdays),sum(tbl.91plusdays),0),2),format(if(sum(tbl.total),sum(tbl.total),0),2)");
	    queryBuilder.append(",format(if(sum(tbl.acBalance),sum(tbl.acBalance),0),2),format(if(sum(tbl.arBalance)!='',-sum(tbl.arBalance),0),2),");
	    queryBuilder.append("format(if(sum(tbl.total)-sum(tbl.arBalance)!='',sum(tbl.total)-sum(tbl.arBalance),0),2) from (");
	    boolean hasUnion = false;
	    if (null != apQuery) {
		queryBuilder.append("(select sum(if(datediff(current_date(),tr.Transaction_date)>=0");
		queryBuilder.append(" and datediff(current_date(),tr.Transaction_date)<=30,tr.Balance,0)) as 0_30days,");
		queryBuilder.append("sum(if(datediff(current_date(),tr.Transaction_date)>=31");
		queryBuilder.append(" and datediff(current_date(),tr.Transaction_date)<=60,tr.Balance,0)) as 31_60days,");
		queryBuilder.append("sum(if(datediff(current_date(),tr.Transaction_date)>=61");
		queryBuilder.append(" and datediff(current_date(),tr.Transaction_date)<=90,tr.Balance,0)) as 61_90days,");
		queryBuilder.append("sum(if(datediff(current_date(),tr.Transaction_date)>=91,tr.Balance,0)) as 91plusdays,");
		queryBuilder.append("sum(tr.Balance) as total,0 as acBalance,0 as arBalance").append(apQuery).append(")");
		hasUnion = true;
	    }
	    if (null != acQuery) {
		if (hasUnion) {
		    queryBuilder.append(" union ");
		}
		queryBuilder.append("(select sum(if(datediff(current_date(),tl.Transaction_date)>=0");
		queryBuilder.append(" and datediff(current_date(),tl.Transaction_date)<=30");
		queryBuilder.append(" and tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as 0_30days,");
		queryBuilder.append("sum(if(datediff(current_date(),tl.Transaction_date)>=31");
		queryBuilder.append(" and datediff(current_date(),tl.Transaction_date)<=60");
		queryBuilder.append(" and tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as 31_60days,");
		queryBuilder.append("sum(if(datediff(current_date(),tl.Transaction_date)>=61");
		queryBuilder.append(" and datediff(current_date(),tl.Transaction_date)<=90");
		queryBuilder.append(" and tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as 61_90days,");
		queryBuilder.append("sum(if(datediff(current_date(),tl.Transaction_date)>=91");
		queryBuilder.append(" and tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as 91plusdays,");
		queryBuilder.append("sum(if(tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as total,");
		queryBuilder.append("sum(if(tl.Status!='").append(STATUS_EDI_ASSIGNED).append("'");
		queryBuilder.append(" and tl.Status!='").append(STATUS_ASSIGN).append("',tl.Balance,0)) as acBalance,");
		queryBuilder.append("0 as arBalance").append(acQuery).append(")");
		hasUnion = true;
	    }
	    if (null != arQuery) {
		if (hasUnion) {
		    queryBuilder.append(" union ");
		}
		queryBuilder.append("(select 0 as 0_30days,0 as 31_60days,0 as 61_90days,0 as 91plusdays,");
		queryBuilder.append(" 0 as total,0 as acBalance,sum(tr.Balance) as arBalance").append(arQuery).append(")");
		hasUnion = true;
	    }
	    if (null != apInvoiceQuery) {
		if (hasUnion) {
		    queryBuilder.append(" union ");
		}
		queryBuilder.append("(select sum(if(datediff(current_date(),ap.date)>=0");
		queryBuilder.append(" and datediff(current_date(),ap.date)<=30,ap.invoice_amount,0)) as 0_30days,");
		queryBuilder.append("sum(if(datediff(current_date(),ap.date)>=31");
		queryBuilder.append(" and datediff(current_date(),ap.date)<=60,ap.invoice_amount,0)) as 31_60days,");
		queryBuilder.append("sum(if(datediff(current_date(),ap.date)>=61");
		queryBuilder.append(" and datediff(current_date(),ap.date)<=90,ap.invoice_amount,0)) as 61_90days,");
		queryBuilder.append("sum(if(datediff(current_date(),ap.date)>=91,ap.invoice_amount,0)) as 91plusdays,");
		queryBuilder.append("sum(ap.invoice_amount) as total,0 as acBalance,0 as arBalance").append(apInvoiceQuery).append(")");
		hasUnion = true;
	    }
	    if (null != ediInvoiceQuery) {
		if (hasUnion) {
		    queryBuilder.append(" union ");
		}
		queryBuilder.append("(select sum(if(datediff(current_date(),edi.invoice_date)>=0");
		queryBuilder.append(" and datediff(current_date(),edi.invoice_date)<=30,edi.invoice_amount,0)) as 0_30days,");
		queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date)>=31");
		queryBuilder.append(" and datediff(current_date(),edi.invoice_date)<=60,edi.invoice_amount,0)) as 31_60days,");
		queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date)>=61");
		queryBuilder.append(" and datediff(current_date(),edi.invoice_date)<=90,edi.invoice_amount,0)) as 61_90days,");
		queryBuilder.append("sum(if(datediff(current_date(),edi.invoice_date)>=91,edi.invoice_amount,0)) as 91plusdays,");
		queryBuilder.append("sum(edi.invoice_amount) as total,0 as acBalance,0 as arBalance").append(ediInvoiceQuery).append(")");
	    }
	    queryBuilder.append(") as tbl");
	    Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	    if (null != result) {
		Object[] col = (Object[]) result;
		String current = (String) col[0];
		String thirtyToSixtyDays = (String) col[1];
		String sixtyOneToNinetyDays = (String) col[2];
		String greaterThanNinetyDays = (String) col[3];
		String total = (String) col[4];
		String acBalance = (String) col[5];
		String arBalance = (String) col[6];
		String netAmount = (String) col[7];
		if (current.contains("-")) {
		    StringBuilder fmtCurrent = new StringBuilder("<font color='red'>");
		    fmtCurrent.append("(").append(current.replace("-", "")).append(")");
		    fmtCurrent.append("</font>");
		    customerBean.setCurrent(fmtCurrent.toString());
		} else {
		    customerBean.setCurrent(current);
		}
		if (thirtyToSixtyDays.contains("-")) {
		    StringBuilder fmtThirtyToSixtyDays = new StringBuilder("<font color='red'>");
		    fmtThirtyToSixtyDays.append("(").append(thirtyToSixtyDays.replace("-", "")).append(")");
		    fmtThirtyToSixtyDays.append("</font>");
		    customerBean.setThirtyOneToSixtyDays(fmtThirtyToSixtyDays.toString());
		} else {
		    customerBean.setThirtyOneToSixtyDays(thirtyToSixtyDays);
		}
		if (sixtyOneToNinetyDays.contains("-")) {
		    StringBuilder fmtSixtyOneToNintyDays = new StringBuilder("<font color='red'>");
		    fmtSixtyOneToNintyDays.append("(").append(sixtyOneToNinetyDays.replace("-", "")).append(")");
		    fmtSixtyOneToNintyDays.append("</font>");
		    customerBean.setSixtyOneToNintyDays(fmtSixtyOneToNintyDays.toString());
		} else {
		    customerBean.setSixtyOneToNintyDays(sixtyOneToNinetyDays);
		}
		if (greaterThanNinetyDays.contains("-")) {
		    StringBuilder fmtGreaterThanNintyDays = new StringBuilder("<font color='red'>");
		    fmtGreaterThanNintyDays.append("(").append(greaterThanNinetyDays.replace("-", "")).append(")");
		    fmtGreaterThanNintyDays.append("</font>");
		    customerBean.setGreaterThanNintyDays(fmtGreaterThanNintyDays.toString());
		} else {
		    customerBean.setGreaterThanNintyDays(greaterThanNinetyDays);
		}
		if (total.contains("-")) {
		    StringBuilder fmtTotal = new StringBuilder("<font color='red'>");
		    fmtTotal.append("(").append(total.replace("-", "")).append(")");
		    fmtTotal.append("</font>");
		    customerBean.setTotal(fmtTotal.toString());
		} else {
		    customerBean.setTotal(total);
		}
		if (arBalance.contains("-")) {
		    StringBuilder fmtArBalance = new StringBuilder("<font color='red'>");
		    fmtArBalance.append("(").append(arBalance.replace("-", "")).append(")");
		    fmtArBalance.append("</font>");
		    customerBean.setOutstandingRecievables(fmtArBalance.toString());
		} else {
		    customerBean.setOutstandingRecievables(arBalance);
		}
		if (acBalance.contains("-")) {
		    StringBuilder fmtAcBalance = new StringBuilder("<font color='red'>");
		    fmtAcBalance.append("(").append(acBalance.replace("-", "")).append(")");
		    fmtAcBalance.append("</font>");
		    customerBean.setOutstandingAccruals(fmtAcBalance.toString());
		} else {
		    customerBean.setOutstandingAccruals(acBalance);
		}
		if (netAmount.contains("-")) {
		    StringBuilder fmtNetAmount = new StringBuilder("<font color='red'>");
		    fmtNetAmount.append("(").append(netAmount.replace("-", "")).append(")");
		    fmtNetAmount.append("</font>");
		    customerBean.setNetAmount1(fmtNetAmount.toString());
		} else {
		    customerBean.setNetAmount1(netAmount);
		}
	    }
	}
	return customerBean;
    }

    public String getCheckNumberFromCheckAmount(String checkAmount) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select distinct(concat(\"'\",check_number,\"'\"))");
	queryBuilder.append(" from ap_payment_check chk where chk.check_amount=").append(checkAmount);
	List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "(").replace("]", ")") : null;
    }
}

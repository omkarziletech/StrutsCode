package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.ImportSearchBean;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclSearchForm;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author palraj
 */
public class ImportSearchDAO extends BaseHibernateDAO {

    private String buildQuoteByQueryForBooking() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("  join `lcl_quote` qt");
        queryBuilder.append("    on (");
        queryBuilder.append("      qt.`entered_by_user_id` in (");
        queryBuilder.append("        select ");
        queryBuilder.append("          ud.`user_id`");
        queryBuilder.append("        from");
        queryBuilder.append("          `user_details` ud");
        queryBuilder.append("        where");
        queryBuilder.append("          ud.`login_name` = :bkQuoteBy");
        queryBuilder.append("      )");
        queryBuilder.append("      and fn.`id` = qt.`file_number_id`");
        queryBuilder.append("    ) ");
        return queryBuilder.toString();
    }

    private String buildSalesCodeQuery(String paramName) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  g.acct_no ");
        queryBuilder.append("from");
        queryBuilder.append("  genericcode_dup s");
        queryBuilder.append("  join cust_general_info g");
        queryBuilder.append("    on (s.id = g.sales_code)");
        queryBuilder.append("where");
        queryBuilder.append("  s.code in :").append(paramName);
        queryBuilder.append("  and s.codetypeid = (select `codetypeid` from `codetype` where `description` = 'Sales Code' limit 1)");
        return queryBuilder.toString();
    }

    private String buildBookingConditions(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
            queryBuilder.append("  and bk.`client_acct_no` = :bkClientNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
            queryBuilder.append("  and bk.`ship_acct_no` = :bkShipperNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
            if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
                queryBuilder.append("  and (");
                queryBuilder.append("    bk.`cons_acct_no` = :bkConsigneeNo");
                queryBuilder.append("    or bk.`cons_acct_no` in (").append(buildSalesCodeQuery("bkSalesCode")).append(")");
                queryBuilder.append("  )");
            } else {
                queryBuilder.append("  and bk.`cons_acct_no` = :bkConsigneeNo");
            }
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
            queryBuilder.append("  and bk.`cons_acct_no` in (").append(buildSalesCodeQuery("bkSalesCode")).append(")");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
            queryBuilder.append("  and bk.`agent_acct_no` = :bkForeignAgentNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getCountryCode())) {
            queryBuilder.append("  and bk.`poo_id` = :bkOrigin");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPolCountryCode())) {
            queryBuilder.append("  and bk.`pol_id` = :bkPol");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
            queryBuilder.append("  and bk.`pod_id` = :bkPod");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
            queryBuilder.append("  and bk.`fd_id` = :bkDestination");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
            queryBuilder.append("  and bk.`poo_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :bkOriginRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
            queryBuilder.append("  and bk.`fd_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = = :bkDestinationRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
            queryBuilder.append("  and bk.`billing_terminal` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      tm.`trmnum`");
            queryBuilder.append("     from");
            queryBuilder.append("       `terminal` tm");
            queryBuilder.append("     where");
            queryBuilder.append("        tm.`terminal_location` = :bkIssuingTerminal");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
            queryBuilder.append("  and bk.`entered_by_user_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      ud.`user_id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `user_details` ud");
            queryBuilder.append("     where");
            queryBuilder.append("        ud.`login_name` = :bookedBy");
            queryBuilder.append("    )");
        }
        return queryBuilder.toString();
    }

    private String buildQuoteConditions(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
            queryBuilder.append("  and qt.`client_acct_no` = :qtClientNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
            queryBuilder.append("  and qt.`ship_acct_no` = :qtShipperNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
            queryBuilder.append("  and qt.`cons_acct_no` = :qtConsigneeNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
            if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
                queryBuilder.append("  and (");
                queryBuilder.append("    qt.`cons_acct_no` = :bkConsigneeNo");
                queryBuilder.append("    or qt.`cons_acct_no` in (").append(buildSalesCodeQuery("qtSalesCode")).append(")");
                queryBuilder.append("  )");
            } else {
                queryBuilder.append("  and qt.`cons_acct_no` = :bkConsigneeNo");
            }
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
            queryBuilder.append("  and qt.`cons_acct_no` in (").append(buildSalesCodeQuery("qtSalesCode")).append(")");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
            queryBuilder.append("  and qt.`agent_acct_no` = :qtForeignAgentNo");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getCountryCode())) {
            queryBuilder.append("  and qt.`poo_id` = :qtOrigin");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPolCountryCode())) {
            queryBuilder.append("  and qt.`pol_id` = :qtPol");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
            queryBuilder.append("  and qt.`pod_id` = :qtPod");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
            queryBuilder.append("  and qt.`fd_id` = :qtDestination");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
            queryBuilder.append("  and qt.`poo_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :qtOriginRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
            queryBuilder.append("  and qt.`fd_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      un.`id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `ports` p");
            queryBuilder.append("       join `un_location` un");
            queryBuilder.append("         on (p.`unlocationcode` = un.`un_loc_code`)");
            queryBuilder.append("     where");
            queryBuilder.append("        p.`regioncode` = :qtDestinationRegion");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
            queryBuilder.append("  and qt.`billing_terminal` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      tm.`trmnum`");
            queryBuilder.append("     from");
            queryBuilder.append("       `terminal` tm");
            queryBuilder.append("     where");
            queryBuilder.append("        tm.`terminal_location` = :qtIssuingTerminal");
            queryBuilder.append("    )");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
            queryBuilder.append("  and qt.`entered_by_user_id` in (");
            queryBuilder.append("    select ");
            queryBuilder.append("      ud.`user_id`");
            queryBuilder.append("     from");
            queryBuilder.append("       `user_details` ud");
            queryBuilder.append("     where");
            queryBuilder.append("        ud.`login_name` = :quoteBy");
            queryBuilder.append("    )");
        }
        return queryBuilder.toString();
    }

    private String buildFilterQuery(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclSearchForm.getFileNumber())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  fn.`created_datetime` as entered_datetime ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_file_number` fn ");
            queryBuilder.append("where");
            queryBuilder.append("  fn.`file_number` like :fileNumber");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getContainerNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_unit` u");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (u.`id` = us.`unit_id`)");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  u.`unit_no` like :unitNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslineNo())) {
            queryBuilder.append("select ");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_ss_detail` sd");
            queryBuilder.append("  join `lcl_ss_header` sh");
            queryBuilder.append("    on (sd.`ss_header_id` = sh.`id`)");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (");
            queryBuilder.append("      sh.`id` = us.`ss_header_id`");
            queryBuilder.append("    ) ");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      bk.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.status not in ('X', 'RF')");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  sd.`sp_acct_no` like :sslineNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getMasterBl())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_unit_ss_manifest` usm");
            queryBuilder.append("  join `lcl_unit_ss` us");
            queryBuilder.append("    on (usm.`ss_header_id` = us.`ss_header_id`)");
            queryBuilder.append("  join `lcl_booking_piece_unit` bpu ");
            queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
            queryBuilder.append("  join `lcl_booking_piece` bp");
            queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  usm.`masterbl` like :masterBl");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getInbondNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_inbond` inb");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      inb.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  inb.`inbond_no` like :bkInbondNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_inbond` inb");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      inb.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  inb.`inbond_no` like :qtInbondNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getWarehouseDocNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` whse");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      whse.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  whse.`type` = 'WH' ");
            queryBuilder.append("  and whse.`reference` like :bkWarehouseNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` whse");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      whse.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  whse.`type` = 'WH' ");
            queryBuilder.append("  and whse.`reference` like :qtWarehouseNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getCustomerPo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` cp");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      cp.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  cp.`type` = 'CP' ");
            queryBuilder.append("  and cp.`reference` like :bkCustomerPo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` cp");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      cp.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  cp.`type` = 'CP' ");
            queryBuilder.append("  and cp.`reference` like :qtCustomerPo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getTrackingNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` tr");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      tr.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  tr.`type` = 'TR' ");
            queryBuilder.append("  and tr.`reference` like :bkTrackingNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_3p_ref_no` tr");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      tr.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  tr.`type` = 'TR' ");
            queryBuilder.append("  and tr.`reference` like :qtTrackingNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSubHouse())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_booking_import` bki");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bki.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  bki.`sub_house_bl` like :bkSubHouseBl");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_quote_import` qti");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      qti.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  qti.`sub_house_bl` like :qtSubHouseBl");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getAmsHBL())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_booking_import_ams` bkia");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bkia.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  bkia.`ams_no` like :bkAmsNo");
            queryBuilder.append(" union ");
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  qt.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_quote_import_ams` qtia");
            queryBuilder.append("  join `lcl_quote` qt");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      qtia.`file_number_id` = qt.`file_number_id`");
            queryBuilder.append("      and qt.`quote_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (");
            queryBuilder.append("      qt.`file_number_id` = fn.`id`");
            queryBuilder.append("      and fn.`state` = 'Q'");
            queryBuilder.append("    ) ");
            queryBuilder.append("where");
            queryBuilder.append("  qtia.`ams_no` like :qtAmsNo");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getIpiLoadNo())) {
            queryBuilder.append("select");
            queryBuilder.append("  fn.`id`,");
            queryBuilder.append("  bk.`entered_datetime` ");
            queryBuilder.append("from");
            queryBuilder.append("  `lcl_booking_import` bki");
            queryBuilder.append("  join `lcl_booking` bk");
            queryBuilder.append("    on ( ");
            queryBuilder.append("      bki.`file_number_id` = bk.`file_number_id`");
            queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
            queryBuilder.append("    )");
            queryBuilder.append("  join `lcl_file_number` fn");
            queryBuilder.append("    on (bk.`file_number_id` = fn.`id`) ");
            queryBuilder.append("where");
            queryBuilder.append("  bki.`ipi_load_no` like :ipiLoadNo");
        } else {
            if ("ALL".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('I', 'T')");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
                if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                    String startDate = DateUtils.parseToMYSQLFormat(lclSearchForm.getStartDate());
                    String endDate = DateUtils.parseToMYSQLFormat(lclSearchForm.getEndDate());
                    queryBuilder.append(" and bk.`entered_datetime` ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                }
                if (CommonUtils.isEmpty(lclSearchForm.getBookedBy())) {
                    queryBuilder.append(" union ");
                    queryBuilder.append("select ");
                    queryBuilder.append("  fn.`id`,");
                    queryBuilder.append("  qt.`entered_datetime` ");
                    queryBuilder.append("from");
                    queryBuilder.append("  `lcl_quote` qt ");
                    queryBuilder.append("  join `lcl_file_number` fn");
                    queryBuilder.append("    on (");
                    queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                    queryBuilder.append("      and fn.`state` = 'Q'");
                    queryBuilder.append("      and fn.status not in ('X', 'RF')");
                    queryBuilder.append("    ) ");
                    queryBuilder.append("where");
                    queryBuilder.append("  qt.`quote_type` in ('I', 'T')");
                    queryBuilder.append(buildQuoteConditions(lclSearchForm));
                    if (CommonUtils.isNotEmpty(lclSearchForm.getStartDate()) && CommonUtils.isNotEmpty(lclSearchForm.getEndDate())) {
                        String startDate = DateUtils.parseToMYSQLFormat(lclSearchForm.getStartDate());
                        String endDate = DateUtils.parseToMYSQLFormat(lclSearchForm.getEndDate());
                        queryBuilder.append(" and qt.`entered_datetime` ").append(" between '").append(startDate).append("' and '").append(endDate).append("'");
                    }
                }
            } else if ("IWB".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                if ("OFF".equalsIgnoreCase(lclSearchForm.getFilterByInventory())) {
                    queryBuilder.append("      and fn.status in ('WV', 'WU')");
                } else {
                    queryBuilder.append("      and fn.status in ('WV', 'WU', 'B')");
                }
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('I', 'T')");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            } else if ("Q".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  qt.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_quote` qt ");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.`state` = 'Q'");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                queryBuilder.append("where");
                queryBuilder.append("  qt.`quote_type` in ('I', 'T')");
                queryBuilder.append(buildQuoteConditions(lclSearchForm));
            } else if ("B".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('I', 'T')");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            } else if ("X".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking` bk");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status = 'X'");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bk.`booking_type` in ('I', 'T')");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
                if (CommonUtils.isEmpty(lclSearchForm.getBookedBy())) {
                    queryBuilder.append(" union ");
                    queryBuilder.append("select ");
                    queryBuilder.append("  fn.`id`,");
                    queryBuilder.append("  qt.`entered_datetime` ");
                    queryBuilder.append("from");
                    queryBuilder.append("  `lcl_quote` qt ");
                    queryBuilder.append("  join `lcl_file_number` fn");
                    queryBuilder.append("    on (");
                    queryBuilder.append("      qt.`file_number_id` = fn.`id`");
                    queryBuilder.append("      and fn.`state` = 'Q'");
                    queryBuilder.append("      and fn.status = 'X'");
                    queryBuilder.append("    ) ");
                    queryBuilder.append("where");
                    queryBuilder.append("  qt.`quote_type` in ('I', 'T')");
                    queryBuilder.append(buildQuoteConditions(lclSearchForm));
                }
            } else if ("INAVAL".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `disposition` disp");
                queryBuilder.append("  join `lcl_unit_ss_dispo` usd");
                queryBuilder.append("    on (disp.`id` = usd.`disposition_id`)");
                queryBuilder.append("  join `lcl_ss_detail` sd");
                queryBuilder.append("    on (usd.`ss_detail_id` = sd.`id`)");
                queryBuilder.append("  join `lcl_ss_header` sh");
                queryBuilder.append("    on (sd.`ss_header_id` = sh.`id`)");
                queryBuilder.append("  join `lcl_unit_ss` us");
                queryBuilder.append("    on (");
                queryBuilder.append("      sh.`id` = us.`ss_header_id`");
                queryBuilder.append("      and usd.`unit_id` = us.`unit_id`");
                queryBuilder.append("    ) ");
                queryBuilder.append("  join `lcl_booking_piece_unit` bpu");
                queryBuilder.append("    on (us.`id` = bpu.`lcl_unit_ss_id`)");
                queryBuilder.append("  join `lcl_booking_piece` bp");
                queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`)");
                queryBuilder.append("  join `lcl_booking` bk");
                queryBuilder.append("    on ( ");
                queryBuilder.append("      bp.`file_number_id` = bk.`file_number_id`");
                queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
                queryBuilder.append("    )");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  disp.elite_code = 'AVAL'");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            } else if ("COH".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking_import` bki");
                queryBuilder.append("  join `lcl_booking` bk");
                queryBuilder.append("    on ( ");
                queryBuilder.append("      bki.`file_number_id` = bk.`file_number_id`");
                queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
                queryBuilder.append("    )");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bki.`cargo_on_hold` is not null");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            } else if ("CGO".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking_import` bki");
                queryBuilder.append("  join `lcl_booking` bk");
                queryBuilder.append("    on ( ");
                queryBuilder.append("      bki.`file_number_id` = bk.`file_number_id`");
                queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
                queryBuilder.append("    )");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bki.`cargo_general_order` is not null");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            } else if ("CNR".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append("select ");
                queryBuilder.append("  fn.`id`,");
                queryBuilder.append("  bk.`entered_datetime` ");
                queryBuilder.append("from");
                queryBuilder.append("  `lcl_booking_import` bki");
                queryBuilder.append("  join `lcl_booking` bk");
                queryBuilder.append("    on ( ");
                queryBuilder.append("      bki.`file_number_id` = bk.`file_number_id`");
                queryBuilder.append("      and bk.`booking_type` in ('I', 'T')");
                queryBuilder.append("    )");
                queryBuilder.append("  join `lcl_file_number` fn");
                queryBuilder.append("    on (");
                queryBuilder.append("      bk.`file_number_id` = fn.`id`");
                queryBuilder.append("      and fn.status not in ('X', 'RF')");
                queryBuilder.append("    ) ");
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    queryBuilder.append(buildQuoteByQueryForBooking());
                }
                queryBuilder.append("where");
                queryBuilder.append("  bki.`cargo_on_hold` is null");
                queryBuilder.append("  or bki.`cargo_general_order` is null");
                queryBuilder.append("  or bki.`freight_released_datetime` is null");
                queryBuilder.append("  or bki.`payment_release_received` is null");
                queryBuilder.append("  or (");
                queryBuilder.append("    bki.`original_bl_received` is null");
                queryBuilder.append("    and bki.`express_release` = false");
                queryBuilder.append("  )");
                queryBuilder.append(buildBookingConditions(lclSearchForm));
            }
            queryBuilder.append(" order by entered_datetime desc, id desc");
            if (!"IWB".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
                queryBuilder.append(" limit ").append(lclSearchForm.getLimit());
            }
        }
        return queryBuilder.toString();
    }

    private StringBuilder getSortBy(LclSearchForm lclSearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if ("status".equalsIgnoreCase(lclSearchForm.getSortByValue())) {
            if ("up".equals(lclSearchForm.getSearchType())) {
                queryBuilder.append(" order by fn.`state` asc, fn.`status` asc, onHold asc");
            } else {
                queryBuilder.append(" order by fn.`state` desc, fn.`status` desc, onHold desc");
            }
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSortByValue())) {
            if ("up".equals(lclSearchForm.getSearchType())) {
                queryBuilder.append(" order by ").append(lclSearchForm.getSortByValue()).append(" asc");
            } else {
                queryBuilder.append(" order by ").append(lclSearchForm.getSortByValue()).append(" desc");
            }
        } else {
            queryBuilder.append(" order by fn.`entered_datetime` desc, fn.`file_number_id` desc");
        }
        return queryBuilder;
    }

    private String buildQuery(LclSearchForm lclSearchForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  fn.`file_number_id` as fileNumberId,");
        queryBuilder.append("  fn.`file_number` as fileNumber,");
        queryBuilder.append("  fn.short_ship as shortShip,");
        queryBuilder.append("  fn.booking_type as bookingType,");
        queryBuilder.append("  fn.`quote` as quote,");
        queryBuilder.append("  fn.`booking` as booking,");
        queryBuilder.append("  fn.`transshipment` as transshipment,");
        queryBuilder.append("  fn.`state` as state,");
        queryBuilder.append("  fn.`status` as status,");
        queryBuilder.append("  `UserDetailsGetLoginNameByID` (fn.`booked_by`) as bookedBy,");
        queryBuilder.append("  `UserDetailsGetLoginNameByID` (fn.`quote_by`) as quoteBy,");
        queryBuilder.append("  fn.`quote_complete` as quoteComplete,");
        queryBuilder.append("  coalesce(fn.`hazmat`, 0) as hazmat,");
        queryBuilder.append("  coalesce(");
        queryBuilder.append("    (select ");
        queryBuilder.append("      if(cs.`codedesc` = 'Credit Hold', 1, 0) ");
        queryBuilder.append("    from");
        queryBuilder.append("      `genericcode_dup` cs ");
        queryBuilder.append("      join `cust_accounting` ca ");
        queryBuilder.append("        on (cs.`id` = ca.`credit_status`) ");
        queryBuilder.append("    where ca.`acct_no` = if(");
        queryBuilder.append("        fn.`bill_to_party` = 'C',");
        queryBuilder.append("        fn.`cons_acct_no`,");
        queryBuilder.append("        if(");
        queryBuilder.append("          fn.`bill_to_party` = 'A',");
        queryBuilder.append("          fn.`agent_acct_no`,");
        queryBuilder.append("          if(");
        queryBuilder.append("            fn.`bill_to_party` = 'T',");
        queryBuilder.append("            fn.`third_party_acct_no`,");
        queryBuilder.append("            fn.`noty_acct_no`");
        queryBuilder.append("          )");
        queryBuilder.append("        )");
        queryBuilder.append("      ) ");
        queryBuilder.append("    limit 1),");
        queryBuilder.append("    0");
        queryBuilder.append("  ) as onHold,");
        queryBuilder.append("  coalesce(fn.`client_pwk_recvd`, 0) as clientPwkReceived,");
        queryBuilder.append("  date_format(fn.sta, '%d-%b-%Y') as eta,");
        queryBuilder.append("  d.`elite_code` as dispoCode,");
        queryBuilder.append("  d.`description` as dispoDesc,");
        queryBuilder.append("  fn.`piece` as piece,");
        queryBuilder.append("  format(fn.`weight`, 3) as weight,");
        queryBuilder.append("  format(fn.`volume`, 3) as volume,");
        queryBuilder.append("  `UnLocationGetCodeByID` (fn.`poo_id`) as originUncode,");
        queryBuilder.append("  `UnLocationGetNameStateCntryByID` (fn.`poo_id`) as origin,");
        queryBuilder.append("  `UnLocationGetCodeByID` (fn.`pol_id`) as polUncode,");
        queryBuilder.append("  `UnLocationGetNameStateCntryByID` (fn.`pol_id`) as pol,");
        queryBuilder.append("  `UnLocationGetCodeByID` (fn.`pod_id`) as podUncode,");
        queryBuilder.append("  `UnLocationGetNameStateCntryByID` (fn.`pod_id`) as pod,");
        queryBuilder.append("  `UnLocationGetCodeByID` (fn.`fd_id`) as destinationUncode,");
        queryBuilder.append("  `UnLocationGetNameStateCntryByID` (fn.`fd_id`) as destination,");
        queryBuilder.append("  coalesce(fn.`relay_override`, 0) as relayOverride,");
        queryBuilder.append("  coalesce(fn.`poo_pickup`, 0) as pickup,");
        queryBuilder.append("  coalesce(");
        queryBuilder.append("    (select ");
        queryBuilder.append("     pkup.pickup_city  ");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_booking_pad` pkup ");
        queryBuilder.append("    where pkup.`file_number_id` = fn.`file_number_id` limit 1),");
        queryBuilder.append("    (select ");
        queryBuilder.append("      pkup.`pickup_city` ");
        queryBuilder.append("    from");
        queryBuilder.append("      `lcl_quote_pad` pkup ");
        queryBuilder.append("    where pkup.`file_number_id` = fn.`file_number_id` limit 1)");
        queryBuilder.append("  ) as pickupCity,");
        queryBuilder.append("  date_format(fn.`picked_up_datetime`, '%d-%b-%Y') as pickedUpDateTime,");
        queryBuilder.append("  ship.`acct_name` as shipName,");
        queryBuilder.append("  ship.`acct_no` as shipNo,");
        queryBuilder.append("  shipc.`address` as shipAddress,");
        queryBuilder.append("  shipc.`city` as shipCity,");
        queryBuilder.append("  shipc.`state` as shipState,");
        queryBuilder.append("  shipc.`zip` as shipZip,");
        queryBuilder.append("  cons.`acct_name` as consName,");
        queryBuilder.append("  cons.`acct_no` as consNo,");
        queryBuilder.append("  consc.`address` as consAddress,");
        queryBuilder.append("  consc.`city` as consCity,");
        queryBuilder.append("  consc.`state` as consState,");
        queryBuilder.append("  consc.`zip` as consZip,");
        queryBuilder.append("  `TerminalGetLocationByNo` (fn.`billing_terminal`) as billingTerminal,");
        queryBuilder.append("  (select if(fn.state <> 'Q', ");
        queryBuilder.append("   (select group_concat(hot.code separator '<br>') from lcl_booking_hot_code hot where hot.file_number_id = fn.file_number_id ) , ");
        queryBuilder.append("   (select group_concat(hot.code separator '<br>') from lcl_quote_hot_code hot where hot.file_number_id = fn.file_number_id ) ");
        queryBuilder.append("    )) as hotCodes, ");
        queryBuilder.append("  date_format(fn.destuffed_datetime, '%d-%b-%Y') AS strippedDate ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    fn.`file_number_id`,");
        queryBuilder.append("    fn.`file_number`,");
        queryBuilder.append("    fn.short_ship,");
        queryBuilder.append("    fn.booking_type,");
        queryBuilder.append("    fn.`quote`,");
        queryBuilder.append("    fn.`booking`,");
        queryBuilder.append("    fn.`transshipment`,");
        queryBuilder.append("    fn.`state`,");
        queryBuilder.append("    fn.`status`,");
        queryBuilder.append("    fn.`booked_by`,");
        queryBuilder.append("    fn.`quote_by`,");
        queryBuilder.append("    fn.`quote_complete`,");
        queryBuilder.append("    fn.`hazmat`,");
        queryBuilder.append("    fn.`bill_to_party`,");
        queryBuilder.append("    fn.`agent_acct_no`,");
        queryBuilder.append("    fn.`third_party_acct_no`,");
        queryBuilder.append("    fn.`noty_acct_no`,");
        queryBuilder.append("    fn.`client_pwk_recvd`,");
        queryBuilder.append("    sd.`sta`,");
        queryBuilder.append("    max(usd.`id`) as usd_id,");
        queryBuilder.append("    fn.`piece`,");
        queryBuilder.append("    fn.`weight`,");
        queryBuilder.append("    fn.`volume`,");
        queryBuilder.append("    fn.`poo_id`,");
        queryBuilder.append("    fn.`pol_id`,");
        queryBuilder.append("    fn.`pod_id`,");
        queryBuilder.append("    fn.`fd_id`,");
        queryBuilder.append("    fn.`relay_override`,");
        queryBuilder.append("    fn.`poo_pickup`,");
        queryBuilder.append("    fn.`picked_up_datetime`,");
        queryBuilder.append("    fn.`ship_acct_no`,");
        queryBuilder.append("    fn.`ship_contact_id`,");
        queryBuilder.append("    fn.`cons_acct_no`,");
        queryBuilder.append("    fn.`cons_contact_id`,");
        queryBuilder.append("    fn.`billing_terminal`,");
        queryBuilder.append("    fn.`entered_datetime`, ");
        queryBuilder.append("    luw.`destuffed_datetime` ");
        queryBuilder.append("  from");
        queryBuilder.append("    (select ");
        queryBuilder.append("      fn.`file_number_id`,");
        queryBuilder.append("      fn.`file_number`,");
        queryBuilder.append("      fn.short_ship,");
        queryBuilder.append("      fn.booking_type,");
        queryBuilder.append("      fn.`quote`,");
        queryBuilder.append("      fn.`booking`,");
        queryBuilder.append("      fn.`transshipment`,");
        queryBuilder.append("      fn.`state`,");
        queryBuilder.append("      fn.`status`,");
        queryBuilder.append("      fn.`booked_by`,");
        queryBuilder.append("      fn.`quote_by`,");
        queryBuilder.append("      fn.`quote_complete`,");
        queryBuilder.append("      max(bp.`hazmat`) as hazmat,");
        queryBuilder.append("      fn.`bill_to_party`,");
        queryBuilder.append("      fn.`agent_acct_no`,");
        queryBuilder.append("      fn.`third_party_acct_no`,");
        queryBuilder.append("      fn.`noty_acct_no`,");
        queryBuilder.append("      fn.`client_pwk_recvd`,");
        queryBuilder.append("      bp.`id` as booking_piece_id,");
        queryBuilder.append("      sum(bp.`booked_piece_count`) as piece,");
        if ("I".equalsIgnoreCase(lclSearchForm.getCommodity())) {
            queryBuilder.append("      sum(bp.`booked_weight_imperial`) as weight,");
            queryBuilder.append("      sum(bp.`booked_volume_imperial`) as volume,");
        } else {
            queryBuilder.append("      sum(bp.`booked_weight_metric`) as weight,");
            queryBuilder.append("      sum(bp.`booked_volume_metric`) as volume,");
        }
        queryBuilder.append("      fn.`poo_id`,");
        queryBuilder.append("      fn.`pol_id`,");
        queryBuilder.append("      fn.`pod_id`,");
        queryBuilder.append("      fn.`fd_id`,");
        queryBuilder.append("      fn.`relay_override`,");
        queryBuilder.append("      fn.`poo_pickup`,");
        queryBuilder.append("      fn.`picked_up_datetime`,");
        queryBuilder.append("      fn.`ship_acct_no`,");
        queryBuilder.append("      fn.`ship_contact_id`,");
        queryBuilder.append("      fn.`cons_acct_no`,");
        queryBuilder.append("      fn.`cons_contact_id`,");
        queryBuilder.append("      fn.`billing_terminal`,");
        queryBuilder.append("      fn.`entered_datetime` ");
        queryBuilder.append("    from");
        queryBuilder.append("      (select ");
        queryBuilder.append("        fn.`id` as file_number_id,");
        queryBuilder.append("        fn.`file_number`,");
        queryBuilder.append("        fn.short_ship,");
        queryBuilder.append("        bk.booking_type,");
        queryBuilder.append("        if(qt.`file_number_id` is not null, 1, 0) as quote,");
        queryBuilder.append("        if(bk.`file_number_id` is not null, 1, 0) as booking,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bki.`transshipment` and bk.`booking_type` = 'T',");
        queryBuilder.append("          fn.`state` = 'Q' and qti.`transshipment` and qt.`quote_type` = 'T',");
        queryBuilder.append("          0");
        queryBuilder.append("        ) as transshipment,");
        queryBuilder.append("        fn.`state`,");
        queryBuilder.append("        fn.`status`,");
        queryBuilder.append("        bk.`entered_by_user_id` as booked_by,");
        queryBuilder.append("        qt.`entered_by_user_id` as quote_by,");
        queryBuilder.append("        coalesce(qt.`quote_complete`, 0) as quote_complete,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`bill_to_party`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`bill_to_party`, null)");
        queryBuilder.append("        ) as bill_to_party,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`ship_acct_no`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`ship_acct_no`, null)");
        queryBuilder.append("        ) as ship_acct_no,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`cons_acct_no`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`cons_acct_no`, null)");
        queryBuilder.append("        ) as cons_acct_no,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`agent_acct_no`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`agent_acct_no`, null)");
        queryBuilder.append("        ) as agent_acct_no,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`third_party_acct_no`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`third_party_acct_no`, null)");
        queryBuilder.append("        ) as third_party_acct_no,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`noty_acct_no`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`noty_acct_no`, null)");
        queryBuilder.append("        ) as noty_acct_no,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`ship_contact_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`ship_contact_id`, null)");
        queryBuilder.append("        ) as ship_contact_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`cons_contact_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`cons_contact_id`, null)");
        queryBuilder.append("        ) as cons_contact_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`client_pwk_recvd`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`client_pwk_recvd`, null)");
        queryBuilder.append("        ) as client_pwk_recvd,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`poo_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`poo_id`, null)");
        queryBuilder.append("        ) as poo_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`pol_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`pol_id`, null)");
        queryBuilder.append("        ) as pol_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`pod_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`pod_id`, null)");
        queryBuilder.append("        ) as pod_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`fd_id`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`fd_id`, null)");
        queryBuilder.append("        ) as fd_id,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`relay_override`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`relay_override`, null)");
        queryBuilder.append("        ) as relay_override,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`poo_pickup`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`poo_door`, null)");
        queryBuilder.append("        ) as poo_pickup,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bki.`picked_up_datetime`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qti.`picked_up_datetime`, null)");
        queryBuilder.append("        ) as picked_up_datetime,");
        queryBuilder.append("        coalesce(");
        queryBuilder.append("          bk.`billing_terminal`,");
        queryBuilder.append("          if(fn.`state` = 'Q', qt.`billing_terminal`, null)");
        queryBuilder.append("        ) as billing_terminal,");
        queryBuilder.append("        filter.`entered_datetime` ");
        queryBuilder.append("      from");
        queryBuilder.append("        (").append(buildFilterQuery(lclSearchForm)).append(") as filter ");
        queryBuilder.append("          join `lcl_file_number` fn ");
        queryBuilder.append("            on (");
        queryBuilder.append("              filter.`id` = fn.`id`");
        queryBuilder.append("            ) ");
        queryBuilder.append("          left join `lcl_booking` bk ");
        queryBuilder.append("            on (fn.`id` = bk.`file_number_id` and bk.`booking_type` in ('I', 'T')) ");
        queryBuilder.append("          left join `lcl_booking_import` bki ");
        queryBuilder.append("            on (fn.`id` = bki.`file_number_id`) ");
        queryBuilder.append("          left join `lcl_quote` qt ");
        queryBuilder.append("            on (fn.`id` = qt.`file_number_id` and qt.`quote_type` in ('I', 'T')) ");
        queryBuilder.append("          left join `lcl_quote_import` qti ");
        queryBuilder.append("            on (fn.`id` = qti.`file_number_id`) ");
        queryBuilder.append("        where (");
        queryBuilder.append("            bk.`file_number_id` is not null ");
        queryBuilder.append("            or qt.`file_number_id` is not null");
        queryBuilder.append("          ) ");
        queryBuilder.append("        group by fn.`id`) as fn ");
        queryBuilder.append("        left join `lcl_booking_piece` bp ");
        queryBuilder.append("          on (fn.`file_number_id` = bp.`file_number_id`) ");
        queryBuilder.append("      group by fn.`file_number_id`) as fn ");
        queryBuilder.append("      left join `lcl_booking_piece_unit` bpu ");
        queryBuilder.append("        on (fn.`booking_piece_id` = bpu.`booking_piece_id`) ");
        queryBuilder.append("      left join `lcl_unit_ss` us ");
        queryBuilder.append("        on (bpu.`lcl_unit_ss_id` = us.`id`) ");
        queryBuilder.append("      left join `lcl_unit_whse` luw ");
        queryBuilder.append("        on (us.`ss_header_id` = luw.`ss_header_id`) ");
        queryBuilder.append("      left join lcl_ss_detail sd ");
        queryBuilder.append("        on (us.`ss_header_id` = sd.`ss_header_id`) ");
        queryBuilder.append("      left join `lcl_unit` u ");
        queryBuilder.append("        on (us.`unit_id` = u.`id`) ");
        queryBuilder.append("      left join `lcl_unit_ss_dispo` usd ");
        queryBuilder.append("        on (");
        queryBuilder.append("          sd.`id` = usd.`ss_detail_id` ");
        queryBuilder.append("          and u.`id` = usd.`unit_id`");
        queryBuilder.append("        ) ");
        queryBuilder.append("    group by fn.`file_number_id`) as fn ");
        queryBuilder.append("    left join `lcl_unit_ss_dispo` usd ");
        queryBuilder.append("      on (fn.`usd_id` = usd.`id`) ");
        queryBuilder.append("    left join `disposition` d ");
        queryBuilder.append("      on (usd.`disposition_id` = d.`id`) ");
        queryBuilder.append("    left join `trading_partner` ship ");
        queryBuilder.append("      on fn.`ship_acct_no` = ship.`acct_no` ");
        queryBuilder.append("    left join `lcl_contact` shipc ");
        queryBuilder.append("      on (fn.`ship_contact_id` = shipc.`id`) ");
        queryBuilder.append("    left join `trading_partner` cons ");
        queryBuilder.append("      on fn.`cons_acct_no` = cons.`acct_no` ");
        queryBuilder.append("    left join `lcl_contact` consc ");
        queryBuilder.append("      on (fn.`cons_contact_id` = consc.`id`) ");
        queryBuilder.append("group by fn.`file_number_id` ");
        queryBuilder.append(getSortBy(lclSearchForm));
        return queryBuilder.toString();
    }

    public List<ImportSearchBean> search(LclSearchForm lclSearchForm) throws Exception {
        if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy()) && "Q".equalsIgnoreCase(lclSearchForm.getFilterBy())) {
            return new ArrayList<ImportSearchBean>();
        }
        SQLQuery query = getSession().createSQLQuery(buildQuery(lclSearchForm));
        if (CommonUtils.isNotEmpty(lclSearchForm.getFileNumber())) {
            query.setString("fileNumber", lclSearchForm.getFileNumber() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getContainerNo())) {
            query.setString("unitNo", lclSearchForm.getContainerNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSslineNo())) {
            query.setString("sslineNo", lclSearchForm.getSslineNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getMasterBl())) {
            query.setString("masterBl", lclSearchForm.getMasterBl() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getInbondNo())) {
            query.setString("bkInbondNo", lclSearchForm.getInbondNo() + "%");
            query.setString("qtInbondNo", lclSearchForm.getInbondNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getWarehouseDocNo())) {
            query.setString("bkWarehouseNo", lclSearchForm.getWarehouseDocNo() + "%");
            query.setString("qtWarehouseNo", lclSearchForm.getWarehouseDocNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getCustomerPo())) {
            query.setString("bkCustomerPo", lclSearchForm.getCustomerPo() + "%");
            query.setString("qtCustomerPo", lclSearchForm.getCustomerPo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getTrackingNo())) {
            query.setString("bkTrackingNo", lclSearchForm.getTrackingNo() + "%");
            query.setString("qtTrackingNo", lclSearchForm.getTrackingNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getSubHouse())) {
            query.setString("bkSubHouseBl", lclSearchForm.getSubHouse() + "%");
            query.setString("qtSubHouseBl", lclSearchForm.getSubHouse() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getAmsHBL())) {
            query.setString("bkAmsNo", lclSearchForm.getAmsHBL() + "%");
            query.setString("qtAmsNo", lclSearchForm.getAmsHBL() + "%");
        } else if (CommonUtils.isNotEmpty(lclSearchForm.getIpiLoadNo())) {
            query.setString("ipiLoadNo", lclSearchForm.getIpiLoadNo() + "%");
        } else {
            if (CommonUtils.in(lclSearchForm.getFilterBy(), "ALL", "X", "IWB", "B", "INAVAL", "COH", "CGO", "CNR")) {
                if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
                    query.setString("bkClientNo", lclSearchForm.getClientNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
                    query.setString("bkShipperNo", lclSearchForm.getShipperNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
                    query.setString("bkConsigneeNo", lclSearchForm.getConsigneeNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
                    query.setParameterList("bkSalesCode", lclSearchForm.getSalesCode().split(","));
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
                    query.setString("bkForeignAgentNo", lclSearchForm.getForeignAgentAccount());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCountryCode())) {
                    query.setString("bkOrigin", lclSearchForm.getCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPolCountryCode())) {
                    query.setString("bkPol", lclSearchForm.getPolCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
                    query.setString("bkPod", lclSearchForm.getPodCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
                    query.setString("bkDestination", lclSearchForm.getDestCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
                    query.setString("bkOriginRegion", lclSearchForm.getOriginRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
                    query.setString("bkDestinationRegion", lclSearchForm.getDestinationRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
                    query.setString("bkIssuingTerminal", lclSearchForm.getIssuingTerminal());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getBookedBy())) {
                    query.setString("bookedBy", lclSearchForm.getBookedBy());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    query.setString("bkQuoteBy", lclSearchForm.getCreatedBy());
                }
            }

            if ("Q".equalsIgnoreCase(lclSearchForm.getFilterBy())
                    || (CommonUtils.in(lclSearchForm.getFilterBy(), "ALL", "X") && CommonUtils.isEmpty(lclSearchForm.getBookedBy()))) {
                if (CommonUtils.isNotEmpty(lclSearchForm.getClientNo())) {
                    query.setString("qtClientNo", lclSearchForm.getClientNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getShipperNo())) {
                    query.setString("qtShipperNo", lclSearchForm.getShipperNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getConsigneeNo())) {
                    query.setString("qtConsigneeNo", lclSearchForm.getConsigneeNo());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getSalesCode())) {
                    query.setParameterList("qtSalesCode", lclSearchForm.getSalesCode().split(","));
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getForeignAgentAccount())) {
                    query.setString("qtForeignAgentNo", lclSearchForm.getForeignAgentAccount());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCountryCode())) {
                    query.setString("qtOrigin", lclSearchForm.getCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPolCountryCode())) {
                    query.setString("qtPol", lclSearchForm.getPolCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getPodCountryCode())) {
                    query.setString("qtPod", lclSearchForm.getPodCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestCountryCode())) {
                    query.setString("qtDestination", lclSearchForm.getDestCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getOriginRegion())) {
                    query.setString("qtOriginRegion", lclSearchForm.getOriginRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getDestinationRegion())) {
                    query.setString("qtDestinationRegion", lclSearchForm.getDestinationRegion());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getIssuingTerminal())) {
                    query.setString("qtIssuingTerminal", lclSearchForm.getIssuingTerminal());
                }
                if (CommonUtils.isNotEmpty(lclSearchForm.getCreatedBy())) {
                    query.setString("quoteBy", lclSearchForm.getCreatedBy());

                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(ImportSearchBean.class));
        query.addScalar("fileNumberId", LongType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("shortShip", BooleanType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("quote", BooleanType.INSTANCE);
        query.addScalar("booking", BooleanType.INSTANCE);
        query.addScalar("transshipment", BooleanType.INSTANCE);
        query.addScalar("state", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("bookedBy", StringType.INSTANCE);
        query.addScalar("quoteBy", StringType.INSTANCE);
        query.addScalar("quoteComplete", BooleanType.INSTANCE);
        query.addScalar("hazmat", BooleanType.INSTANCE);
        query.addScalar("onHold", BooleanType.INSTANCE);
        query.addScalar("clientPwkReceived", BooleanType.INSTANCE);
        query.addScalar("eta", StringType.INSTANCE);
        query.addScalar("dispoCode", StringType.INSTANCE);
        query.addScalar("dispoDesc", StringType.INSTANCE);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("weight", StringType.INSTANCE);
        query.addScalar("volume", StringType.INSTANCE);
        query.addScalar("originUncode", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("polUncode", StringType.INSTANCE);
        query.addScalar("podUncode", StringType.INSTANCE);
        query.addScalar("pol", StringType.INSTANCE);
        query.addScalar("pod", StringType.INSTANCE);
        query.addScalar("destinationUncode", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        query.addScalar("relayOverride", BooleanType.INSTANCE);
        query.addScalar("pickup", BooleanType.INSTANCE);
        query.addScalar("pickupCity", StringType.INSTANCE);
        query.addScalar("pickedUpDateTime", StringType.INSTANCE);
        query.addScalar("shipName", StringType.INSTANCE);
        query.addScalar("shipNo", StringType.INSTANCE);
        query.addScalar("shipAddress", StringType.INSTANCE);
        query.addScalar("shipCity", StringType.INSTANCE);
        query.addScalar("shipState", StringType.INSTANCE);
        query.addScalar("shipZip", StringType.INSTANCE);
        query.addScalar("consName", StringType.INSTANCE);
        query.addScalar("shipZip", StringType.INSTANCE);
        query.addScalar("consNo", StringType.INSTANCE);
        query.addScalar("consAddress", StringType.INSTANCE);
        query.addScalar("consCity", StringType.INSTANCE);
        query.addScalar("consState", StringType.INSTANCE);
        query.addScalar("consZip", StringType.INSTANCE);
        query.addScalar("billingTerminal", StringType.INSTANCE);
        query.addScalar("hotCodes", StringType.INSTANCE);
        query.addScalar("strippedDate", StringType.INSTANCE);
        return query.list();
    }
}

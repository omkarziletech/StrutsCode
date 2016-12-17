package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.struts.lcl.kn.form.SearchForm;
import java.util.Date;

/**
 *
 * @author palraj.p
 */
public class BookingQueryBuilder {

    private static final String DD_MM_YYYY_HH_MM_SS = "dd-MMM-yyyy HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public StringBuilder search(SearchForm searchForm) throws Exception {
        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("select env.id as bookingId,");
        searchQuery.append(" env.sender_id as senderId,");
        searchQuery.append(" env.remarks as remarks,");
        searchQuery.append(" date_format(bkg.bkg_date,'%d-%b-%Y') as bookingDate,");
        searchQuery.append(" bkg.bkg_number AS bookingNumber,");
        searchQuery.append(" bkg.bkg_type as bookingType,");
        searchQuery.append(" bkg.request_type as requestType,");
        searchQuery.append(" bkg.cfs_origin as cfsOrigin,");
        searchQuery.append(" bkg.cfs_Destination as cfsDestination,");
        searchQuery.append(" bkg.ams_Flag as amsFlag,");
        searchQuery.append(" bkg.aes_Flag as aesFlag,");
        searchQuery.append(" bkg.customer_control_code as customerControlCode,");
        searchQuery.append(" cargo.pieces as pieces,");
        searchQuery.append(" cargo.weight as weight,");
        searchQuery.append(" cargo.volume as volume,");
        searchQuery.append(" sail.vessel_voyage_id as VesselVoyageId,");
        searchQuery.append(" sail.vessel_name as VesselName,");
        searchQuery.append(" sail.imo_number as IMONumber,");
        searchQuery.append(" sail.voyage as Voyage,");
        searchQuery.append(" date_format(sail.etd,'%d-%b-%Y') as etd,");
        searchQuery.append(" date_format(sail.eta,'%d-%b-%Y') as eta,");
        searchQuery.append(" pickup.company_name as companyName,");
        searchQuery.append(" date_format(env.created_on,'%d-%b-%Y') as createdOn,");
        searchQuery.append(" gc.codedesc as senderMappingId");
        searchQuery.append(" from kn_bkg_envelope env");
        searchQuery.append(" join kn_bkg_details bkg on env.id = bkg.envelope_id");
        searchQuery.append(" join kn_cargo_details cargo on bkg.id = cargo.bkg_id");
        searchQuery.append(" join kn_sailing_details sail on bkg.id = sail.bkg_id");
        searchQuery.append(" left join kn_pickup_details pickup on bkg.id = pickup.bkg_id");
        searchQuery.append(" left join kn_haz_details haz on cargo.id = haz.cargo_id");
        searchQuery.append(" left join genericcode_dup gc on bkg.customer_control_code = gc.code");
        searchQuery.append(addSearchFilter(searchForm));
        return searchQuery;
    }

    public StringBuilder addSearchFilter(SearchForm searchForm) throws Exception {
        Date startDate = DateUtils.parseDate(searchForm.getStartDate(), DD_MM_YYYY_HH_MM_SS);
        Date endDate = DateUtils.parseDate(searchForm.getEndDate(), DD_MM_YYYY_HH_MM_SS);
        String sDate = DateUtils.formatDate(startDate, YYYY_MM_DD_HH_MM_SS);
        String eDate = DateUtils.formatDate(endDate, YYYY_MM_DD_HH_MM_SS);
        StringBuilder filterBuilder = new StringBuilder();
        if (CommonUtils.isEmpty(searchForm.getSearchBy()) && searchForm.getAction().equals("display")) {
            filterBuilder.append(" GROUP BY created_on ORDER BY created_on DESC LIMIT 100");
        } else if (CommonUtils.isNotEmpty(searchForm.getSearchBy()) && !"all".equals(searchForm.getSearchBy())) {
            filterBuilder.append(" where");
            if (CommonUtils.isNotEmpty(searchForm.getBkgNo())) {
                filterBuilder.append(" bkg.bkg_number = '").append(searchForm.getBkgNo()).append("'");
            } else if (CommonUtils.isNotEmpty(sDate) && CommonUtils.isNotEmpty(eDate)) {
                filterBuilder.append(" env.created_on between '").append(sDate).append("'");
                filterBuilder.append(" and '").append(eDate).append("'");
            }
            filterBuilder.append(getSortBy(searchForm));
            if (CommonUtils.isNotEmpty(searchForm.getLimitRecord())) {
                filterBuilder.append(" limit ").append(searchForm.getLimitRecord());
            }
        } else {
            filterBuilder.append(getSortBy(searchForm));
            if (!searchForm.getSearchBy().equals("all")) {
                filterBuilder.append(" limit 100");
            }
        }
        return filterBuilder;
    }

    private StringBuilder getSortBy(SearchForm searchForm) {
        StringBuilder sortByBuilder = new StringBuilder();
        String sortBy = "createdOn";
        if (CommonUtils.isNotEmpty(searchForm.getSortBy())) {
            sortBy = searchForm.getSortBy();
        }
        sortByBuilder.append(" order by ");
        sortByBuilder.append(SortByEnum.valueOf(sortBy).getField());
        if (CommonUtils.isNotEmpty(searchForm.getSearchType()) && "up".equals(searchForm.getSearchType())) {
            sortByBuilder.append(" asc");
        } else {
            sortByBuilder.append(" desc");
        }
        return sortByBuilder;
    }
}

package com.gp.cvst.logisoft.reports.data;

import java.util.HashMap;
import java.util.List;
import com.gp.cvst.logisoft.reports.dto.ReportDTO;

public class EconoHelper {

    public static String SUCCESS_LCL = "successlcl";
    public static String SUCCESS_LCLFCLAGENT = "successlclfclagent";
    public static String SUCCESS_LCLFCLUSER = "successlclfcluser";
    public static String SUCCESS_FCL = "successfcl";
    public static String LCLPATH = "/faces/lclquote/recentquote.jsf";
    public static String FCLPATH = "/faces/fclquote/fclhome.jsf";
    public static String ADMINPATH = "/faces/admin/administrationFrame.jsf";
    public static String LCLFCL_FAILURE = "failurelclfcl";
    public static String LOGIN_FAILURE = "loginfailure";
    public static String ACTIVATION_FAILURE = "activationfailure";
    public static String LOGIN_VIEW = "/faces/redirecttologin.jsf";
    public static String SESSION_EXPIRED = "/faces/SessionExpired.jsf";
    public static String REPORT_DTO_SESSION_KEY = "RDTO";
    public static String LOGOUT = "logout";
    // name of visit variable in session
    public static String VISIT_KEY = "visit";
    public static String QUOTE_MESSAGES_LIST = "quotemessageslist";
    public static String QUOTE_PARAMETERS = "quoteParameters";

    public static ReportDTO sendToReport(HashMap parameters, boolean isDataSourceRequired,
            String compiledReportName, boolean shouldISaveFile,
            String fileName, List dtos) {

        ReportDTO rdto = new ReportDTO();

        //		 setting up rdto!
        rdto.setParameters(parameters);
        rdto.setDataSourceRequired(isDataSourceRequired);
        rdto.setCompiledReport("/reports/" + compiledReportName);
        rdto.setSaveFile(shouldISaveFile);
        rdto.setFileName(fileName);

        // setting up a new data source implementation
        ReportDataSource reportDataSource = new ReportDataSourceImpl();

        // setting up the query provider for the datasource
        QueryProvider qp = new MessageQueryProvider();
        // set the query provider for datasource
        reportDataSource.setQueryProvider(qp);
        // pass the criteria to the data source
        MessageCriteriaSet mcs = new MessageCriteriaSet();
        // set the dtos to be used as the data
        mcs.setMessages(dtos);
        // set the message criteria set
        reportDataSource.setCriteriaSet(mcs);
        // create the new datasource
        rdto.setReportSource(new ReportSource(reportDataSource));

        return rdto;

    }
}

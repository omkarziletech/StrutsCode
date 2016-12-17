<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.FiscalPeriod, com.gp.cvst.logisoft.domain.Batch,com.gp.cvst.logisoft.domain.JournalEntry,com.gp.cvst.logisoft.domain.LineItem,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cvst.logisoft.beans.BatchesBean,java.text.*"%>
<%@ page import="java.util.*,com.gp.cvst.logisoft.struts.form.JournalEntryForm"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<html>
    <head>
        <title>Journal Entry</title>
        <style type="text/css">
            .drillDownDiv{
                border: 2px solid thin;
                height: 80px;
                width: 200px;
                font-size: 12px;
                font-family: Arial;
                overflow: auto;
            }
        </style>
        <%
        DBUtil dbUtil=new DBUtil();
        String journal1 = null;
        String path = request.getContextPath();

        String cancelline="";
         NumberFormat number = new DecimalFormat("###,###,##0.00");
         JournalEntryForm journalEntryForm=new JournalEntryForm();
         if(request.getAttribute("journalEntryForm")!=null){
         journalEntryForm=(JournalEntryForm)request.getAttribute("journalEntryForm");
         }else{
         journalEntryForm.setReverse("off");
         request.setAttribute("journalEntryForm",journalEntryForm);
         }
        if(session.getAttribute("trade")!=null)
        {
        session.removeAttribute("trade");
        }
        if(session.getAttribute("journal") != null)
         {
           journal1 = session.getAttribute("journal").toString();
           session.removeAttribute("journal");
         }
        if(session.getAttribute("cancelline")!=null)
        {
        cancelline=(String)session.getAttribute("cancelline");
        }
        BatchesBean batchesBean=new BatchesBean();
        batchesBean.setReverse("off");
        String batchno="";
        String bdesc="";
        String  btotalcredit="";
        String btotaldebit="";
        String bstatus="";
        String jebatchno="";
        String jeid="";
        String jedesc="";
        String jeperiod="0";
        String jesourcecode="";
        GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
        String jescdesc="";
        String jedebit="";
        String jecredit="";
        String jememo="";
        String jedate="";
        String flag="";
        String itemNo="";
        String reference="";
        String refDesc="";
        String account="";
        String accountDesc="";
        String lineDebit="";
        String lineCredit="";
        String currency="0";
        String buttonValue="";
        String message="";
        String search="";
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
        jesourcecode="11285";
        GenericCode gen=genericCodeDAO.findById(Integer.parseInt(jesourcecode));
        jescdesc=gen.getCodedesc();
        List jLIst=new ArrayList();
        HashMap lmap=new HashMap();
        List llist=new ArrayList();
        if(session.getAttribute("search")!=null)
        {
        search=(String)session.getAttribute("search");
        }
        else if(session.getAttribute("search1")==null)
        {
         if(journal1 !=null)
         {
           buttonValue ="";
         }else{
        buttonValue="load";
        }
        }
        JournalEntry journalEntry=new JournalEntry();

        Batch batch=new Batch();

        List journalEntryList=new ArrayList();
        List lineItemList=new ArrayList();
        LineItem line=new LineItem();

        if( journal1 != null)
        {
          request.removeAttribute("buttonValue");
            session.removeAttribute("buttonValue");
        }
        if(session.getAttribute("buttonValue")!=null)
        {
        buttonValue=(String)session.getAttribute("buttonValue");
        }
        if(request.getAttribute("buttonValue")!=null)
        {
        buttonValue=(String)request.getAttribute("buttonValue");
        }

        if(buttonValue.equals("lastbatchdec") || buttonValue.equals("batchinc") || buttonValue.equals("lastbatchinc")
         || buttonValue.equals("journaldec") || buttonValue.equals("lastjournaldec") || buttonValue.equals("journalinc") || buttonValue.equals("lastjournalinc")
         || buttonValue.equals("sourcecode") || buttonValue.equals("addline") ||buttonValue.equals("addaccount") || buttonValue.equals("linepopup")
         || buttonValue.equals("addlinepop") || buttonValue.equals("reverse") || buttonValue.equals("accountNo") ||
          buttonValue.equals("") || buttonValue.equals("accountDesc") || journal1 != null || buttonValue.equals("savebatch")|| buttonValue.equals("batchdetails")
          ||buttonValue.equals("print")|| buttonValue.equals("copy")||buttonValue.equals("saveprint"))
        {
        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();

        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        if(batch.getStatus()!=null)
        {
        bstatus=batch.getStatus();
                if(buttonValue.equals("reverse") && bstatus.equals("posted")){
                        bstatus="open";
                }
        }
        }
        if(session.getAttribute("journalEntry")!=null)
        {
        journalEntry=(JournalEntry)session.getAttribute("journalEntry");

        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        if(journalEntry.getJournalEntryDesc()!=null)
        {
        jedesc=journalEntry.getJournalEntryDesc();
        }
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        if(journalEntry.getPeriod()!=null && journalEntry.getPeriod()!=null)
        {
        jeperiod=journalEntry.getPeriod();
        }
        if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getId()!=null)
        {
        jesourcecode=journalEntry.getSourceCode().getId().toString();
        jescdesc=journalEntry.getSourceCode().getCodedesc();
        }
        if(journalEntry.getCredit()!=null)
        {
        jecredit=number.format(journalEntry.getCredit());
        }
        if(journalEntry.getDebit()!=null)
        {
        jedebit=number.format(journalEntry.getDebit());
        }
        if(journalEntry.getMemo()!=null)
        {
        jememo=journalEntry.getMemo();
        }
        if(journalEntry.getFlag()!=null)
        {
        flag=journalEntry.getFlag();
        }
        }
        if(session.getAttribute("lineItemList")!=null)
        {
        lineItemList=(List)session.getAttribute("lineItemList");

        }
        if(session.getAttribute("line")!=null)
        {
        line=(LineItem)session.getAttribute("line");
        if(line.getLineItemId()!=null)
        {
        itemNo=line.getLineItemId();
        }
        if(line.getReference()!=null)
        {
        reference=line.getReference();
        }
        if(line.getReferenceDesc()!=null)
        {
        refDesc=line.getReferenceDesc();
        }
        if(line.getAccount()!=null)
        {
        account=line.getAccount();
        accountDesc=line.getAccountDesc();
        }
        if(line.getDebit()!=null)
        {
        lineDebit=line.getDebit().toString();
        }
        if(line.getCredit()!=null)
        {
        lineCredit=line.getCredit().toString();
        }
        if(line.getCurrency()!=null)
        {
        currency=line.getCurrency();
        }
        }
        }
        if(buttonValue.equals("addbatch"))
        {

        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        batchno=batch.getBatchId().toString();
        bstatus=batch.getStatus();
        btotaldebit=number.format(batch.getTotalDebit());
        btotalcredit=number.format(batch.getTotalCredit());
        }
        jesourcecode="11285";
        GenericCode gen1=genericCodeDAO.findById(Integer.parseInt(jesourcecode));

        jescdesc=gen1.getCodedesc();
        }
        if(buttonValue.equals("addjournal")||buttonValue.equals("addbatch"))
        {

        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();
        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        if(batch.getStatus()!=null)
        {
        bstatus=batch.getStatus();
        }
        }

        if(session.getAttribute("journalEntry")!=null)
        {
        journalEntry=(JournalEntry)session.getAttribute("journalEntry");
        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        jesourcecode="11285";
        GenericCode gen1=genericCodeDAO.findById(Integer.parseInt(jesourcecode));
        jedebit=number.format(journalEntry.getDebit());
        jecredit=number.format(journalEntry.getCredit());
        jescdesc=gen1.getCodedesc();
        FiscalPeriod fiscalPeriod = dbUtil.getPeriodForCurrentDate();
        if(fiscalPeriod !=null){
                jeperiod=fiscalPeriod.getPeriodDis();
        }
        jeperiod=journalEntry.getPeriod();
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        }
        if(session.getAttribute("lineItemList")!=null)
        {
        lineItemList=(List)session.getAttribute("lineItemList");
        }
        if(session.getAttribute("line")!=null)
        {
        line=(LineItem)session.getAttribute("line");
        if(line.getLineItemId()!=null)
        {
        itemNo=line.getLineItemId();
        }
        }
        }
        if(buttonValue.equals("savejournal"))
        {
        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();
        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        if(batch.getStatus()!=null)
        {
        bstatus=batch.getStatus();
        }
        }

        if(session.getAttribute("journalEntry")!=null)
        {
        journalEntry=(JournalEntry)session.getAttribute("journalEntry");
        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        if(journalEntry.getJournalEntryDesc()!=null)
        {
        jedesc=journalEntry.getJournalEntryDesc();
        }
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        if(journalEntry.getPeriod()!=null && journalEntry.getPeriod()!=null)
        {
        jeperiod=journalEntry.getPeriod();
        }
        if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getId()!=null)
        {
        jesourcecode=journalEntry.getSourceCode().getId().toString();
        jescdesc=journalEntry.getSourceCode().getCodedesc();
        }
        if(journalEntry.getCredit()!=null)
        {
        jecredit=number.format(journalEntry.getCredit());
        }
        if(journalEntry.getDebit()!=null)
        {
        jedebit=number.format(journalEntry.getDebit());
        }
        if(journalEntry.getMemo()!=null)
        {
        jememo=journalEntry.getMemo();
        }
        if(journalEntry.getFlag()!=null)
        {
        flag=journalEntry.getFlag();
        }
        }
        if(session.getAttribute("line")!=null)
        {
        line=(LineItem)session.getAttribute("line");
        if(line.getLineItemId()!=null)
        {
        itemNo=line.getLineItemId();
        }
        }
        }

        if(buttonValue.equals("additemline"))
        {
        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();
        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        if(batch.getStatus()!=null)
        {
        bstatus=batch.getStatus();
        }
        }

        if(session.getAttribute("journalEntry")!=null)
        {
        journalEntry=(JournalEntry)session.getAttribute("journalEntry");
        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        if(journalEntry.getJournalEntryDesc()!=null)
        {
        jedesc=journalEntry.getJournalEntryDesc();
        }
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        if(journalEntry.getPeriod()!=null && journalEntry.getPeriod()!=null)
        {
        jeperiod=journalEntry.getPeriod();
        }
        if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getId()!=null)
        {
        jesourcecode=journalEntry.getSourceCode().getId().toString();
        jescdesc=journalEntry.getSourceCode().getCodedesc();
        }
        if(journalEntry.getCredit()!=null)
        {
        jecredit=number.format(journalEntry.getCredit());
        }
        if(journalEntry.getDebit()!=null)
        {
        jedebit=number.format(journalEntry.getDebit());
        }
        if(journalEntry.getMemo()!=null)
        {
        jememo=journalEntry.getMemo();
        }
        if(journalEntry.getFlag()!=null)
        {
        flag=journalEntry.getFlag();
        }
        }
        if(session.getAttribute("line")!=null)
        {
        line=(LineItem)session.getAttribute("line");
        if(line.getLineItemId()!=null)
        {
        itemNo=line.getLineItemId();
        }
        }
        }
        if((cancelline.equals("") && buttonValue.equals("load") && session.getAttribute("search1")==null))
        {
        if(session.getAttribute("batch")!=null){
        session.removeAttribute("batch");
        }
        List batchList=dbUtil.batchList();
        session.setAttribute("batchList",batchList);
        if(batchList.size()>0)
        {
        batch=(Batch)batchList.get(batchList.size()-1);
        session.setAttribute("batch",batch);
        }
        }
        if(search.equals("searchbatch") || search.equals("copy"))
        {
        if(session.getAttribute("line")!=null)
        {
        session.removeAttribute("line");
        }
        }

        if(buttonValue.equals("uploadJournalEntry") || buttonValue.equals("load") || search.equals("searchbatch") || buttonValue.equals("detail") || buttonValue.equals("journaldelete") || buttonValue.equals("delete") || buttonValue.equals("saveline"))
        {
        HashMap hashMap=new HashMap();

        batch=new Batch();

        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");
        if(batch!=null)
        {
        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();
        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        if(batch.getStatus()!=null)
        {
        bstatus=batch.getStatus();
        }
        }
        }
        if(batch.getJournalEntrySet()!=null)
        {
        Iterator iter=batch.getJournalEntrySet().iterator();
        while(iter.hasNext())
        {
                JournalEntry journal=(JournalEntry)iter.next();
                hashMap.put(journal.getJournalEntryId(),journal);
                jLIst.add(journal.getJournalEntryId());

        }
        Collections.sort(jLIst);
        for(int i=0;i<jLIst.size();i++)
        {
        JournalEntry jEntry=(JournalEntry)hashMap.get(jLIst.get(i));
        journalEntryList.add(jEntry);
        }

        session.removeAttribute("journalEntryList");
        session.setAttribute("journalEntryList",journalEntryList);
        }

        if(journalEntryList.size()>0)
        {
        journalEntry=(JournalEntry)journalEntryList.get(journalEntryList.size()-1);
        session.setAttribute("journalEntry",journalEntry);
        if(journalEntry!=null)
        {
        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        if(journalEntry.getJournalEntryDesc()!=null)
        {
        jedesc=journalEntry.getJournalEntryDesc();
        }
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        if(journalEntry.getPeriod()!=null && journalEntry.getPeriod()!=null)
        {
        jeperiod=journalEntry.getPeriod();

        }
        if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getId()!=null)
        {
        jesourcecode=journalEntry.getSourceCode().getId().toString();
        jescdesc=journalEntry.getSourceCode().getCodedesc();

        }
        if(journalEntry.getCredit()!=null && !journalEntry.getCredit().equals(""))
        {
        jecredit=number.format(journalEntry.getCredit());
        }
        if(journalEntry.getDebit()!=null && !journalEntry.getDebit().equals(""))
        {
        jedebit=number.format(journalEntry.getDebit());
        }
        if(journalEntry.getMemo()!=null)
        {
        jememo=journalEntry.getMemo();
        }
        if(journalEntry.getFlag()!=null)
        {
        flag=journalEntry.getFlag();
        }
        if(journalEntry.getLineItemSet()!=null)
        {
        Iterator iter=journalEntry.getLineItemSet().iterator();
        while(iter.hasNext())
        {
                LineItem lineItem=(LineItem)iter.next();
                lmap.put(lineItem.getLineItemId(),lineItem);
                llist.add(lineItem.getLineItemId());

        }
        Collections.sort(llist);
        for(int i=0;i<llist.size();i++)
        {
        LineItem lItem=(LineItem)lmap.get(llist.get(i));
        lineItemList.add(lItem);
        }
        session.setAttribute("lineItemList",lineItemList);

        }
        }
        }
        else
        {
        jeid="";
        jedesc="";
        jeperiod="0";
        jesourcecode="";

        jescdesc="";
        jedebit="";
        jecredit="";
        jememo="";
        jedate="";
        }
        }
        String noOfBatches=(String)dbUtil.getNumberOfBatches();
        String noOfJournalEntry=(String)dbUtil.getNumberOfJournalEntry(batch.getBatchId());

        //if(buttonValue.equals("addjournal"))
        //{
        //jeperiod="";
        //}
        request.setAttribute("periodList",dbUtil.getperiodList2(jeperiod));




        request.setAttribute("sourceCodeList",dbUtil.getSourcecodeList(33,"no", "Select Source Code"));
        request.setAttribute("currencyList",dbUtil.getSourcecodeList(32,"yes","Select Currency"));
        String editPath=path+"/journalEntry.do";
        List lineList=new ArrayList();

        if(!buttonValue.equals("onchangeline") && !buttonValue.equals("autoreverse"))
        {
        if(session.getAttribute("lineItemList")!=null)
        {
        lineItemList=(List)session.getAttribute("lineItemList");

        }
        if(lineItemList!=null && lineItemList.size()>0)
        {
        for(int i=0;i<lineItemList.size();i++)
        {
        LineItem l1=(LineItem)lineItemList.get(i);


        String je=dbUtil.lineitemidid(l1.getLineItemId());

        if(je.equals(jeid))
        {

        lineList.add(l1);
        }
        }

        session.setAttribute("lineItemList",lineList);
        }
        }
        if(session.getAttribute("search1")!=null || buttonValue.equals("onchangeline"))
        {

        if(session.getAttribute("batch")!=null)
        {
        batch=(Batch)session.getAttribute("batch");

        if(batch.getBatchId()!=null)
        {
        batchno=batch.getBatchId().toString();
        }
        if(batch.getBatchDesc()!=null)
        {
        bdesc=batch.getBatchDesc();
        }
        if(batch.getTotalDebit()!=null)
        {
        btotaldebit=number.format(batch.getTotalDebit());
        }
        if(batch.getTotalCredit()!=null)
        {
        btotalcredit=number.format(batch.getTotalCredit());
        }
        }
        if(session.getAttribute("journalEntry")!=null)
        {
        journalEntry=(JournalEntry)session.getAttribute("journalEntry");

        if(journalEntry.getJournalEntryId()!=null)
        {
        jeid=journalEntry.getJournalEntryId();
        }
        if(journalEntry.getJournalEntryDesc()!=null)
        {
        jedesc=journalEntry.getJournalEntryDesc();
        }
        if(journalEntry.getJeDate()!=null)
        {
        jedate=sdf.format(journalEntry.getJeDate()).toString();
        }
        if(journalEntry.getPeriod()!=null && journalEntry.getPeriod()!=null)
        {
        jeperiod=journalEntry.getPeriod();
        }
        if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getId()!=null)
        {
        jesourcecode=journalEntry.getSourceCode().getId().toString();
        jescdesc=journalEntry.getSourceCode().getCodedesc();
        }
        if(journalEntry.getCredit()!=null && !journalEntry.getCredit().equals(""))
        {
        jecredit=number.format(journalEntry.getCredit());
        }
        if(journalEntry.getDebit()!=null && !journalEntry.getDebit().equals(""))
        {
        jedebit=number.format(journalEntry.getDebit());
        }
        if(journalEntry.getMemo()!=null)
        {
        jememo=journalEntry.getMemo();
        }
        if(journalEntry.getFlag()!=null)
        {
        flag=journalEntry.getFlag();
        }
        currency = "USD";
        }
        if(session.getAttribute("line")!=null)
        {
        line=(LineItem)session.getAttribute("line");
        if(line.getLineItemId()!=null)
        {
        itemNo=line.getLineItemId();
        }
        if(line.getReference()!=null)
        {
        reference=line.getReference();
        }
        if(line.getReferenceDesc()!=null)
        {
        refDesc=line.getReferenceDesc();
        }

        if(line.getAccount()!=null)
        {
        account=line.getAccount();
        accountDesc=line.getAccountDesc();
        }
        if(line.getDebit()!=null)
        {
        lineDebit=line.getDebit().toString();
        }
        if(line.getCredit()!=null)
        {
        lineCredit=line.getCredit().toString();
        }
        if(line.getCurrency()!=null)
        {
        currency=line.getCurrency();
        }
        }


        }
        if(session.getAttribute("lineItemList")!=null)
        {
        lineItemList=(List)session.getAttribute("lineItemList");
        }
        session.setAttribute("journalId",jeid);
        String lineId="";

        if(session.getAttribute("itemNo")!=null)
        {
        lineId=(String)session.getAttribute("itemNo");
        }

        if(journal1 != null)
        {
                 if(journalEntry.getLineItemSet()!=null)
                        {
                                Iterator iter=journalEntry.getLineItemSet().iterator();
                        while(iter.hasNext())
                        {
                        LineItem lineItem=(LineItem)iter.next();
                        lmap.put(lineItem.getLineItemId(),lineItem);
                        llist.add(lineItem.getLineItemId());

                        }
                        Collections.sort(llist);
                        for(int i=0;i<llist.size();i++)
                        {
                        LineItem lItem=(LineItem)lmap.get(llist.get(i));
                        lineItemList.add(lItem);
                        }
                        session.setAttribute("lineItemList",lineItemList);

                        }
        }
        if(buttonValue.equals("autoreverse") && null!=session.getAttribute("autoReverseLineItemList")){
            lineItemList=(List)session.getAttribute("autoReverseLineItemList");
            session.removeAttribute("autoReverseLineItemList");
            batch=(Batch)session.getAttribute("batch");
            batchno = batch.getBatchId();
            bdesc=batch.getBatchDesc();
            bstatus="open";
            btotaldebit=number.format(batch.getTotalDebit());
            btotalcredit=number.format(batch.getTotalCredit());
            journalEntry=(JournalEntry)session.getAttribute("journalEntry");
            jeid=journalEntry.getJournalEntryId();
            jedesc=journalEntry.getJournalEntryDesc();
            jedate=sdf.format(journalEntry.getJeDate()).toString();
            jeperiod=journalEntry.getPeriod();
            jesourcecode=journalEntry.getSourceCode().getId().toString();
            jescdesc=journalEntry.getSourceCode().getCodedesc();
            jecredit=number.format(journalEntry.getCredit());
            jedebit=number.format(journalEntry.getDebit());
            jememo=journalEntry.getMemo();
            flag="";
            search="";
            currency = "USD";
        }

        %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/util.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/AccountDetailsDAO.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/BatchDAO.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/DwrUtil.js'></script>

        <script type="text/javascript">
            dwr.engine.setTextHtmlHandler(dwrSessionError);
        </script>
        <script language="javascript" src="<%=path%>/js/NumberFormat154.js"></script>
	<script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
        </script>

        <script language="javascript" type="text/javascript">


            function journaldelete(){
                if(document.JournalEntryForm.bstatus.value=="deleted"){
                    alert("Cannot delete, because this batch has been posted");
                    return;
                }
                if(document.JournalEntryForm.journalid.value==""){
                    alert("There is no Journal Entry to Delete");
                    return;
                }
                var result = confirm('Are you sure you want to delete Journal entry?');
                if(result){			document.JournalEntryForm.buttonValue.value="journaldelete";
                    document.JournalEntryForm.submit();
                }
            }
            function confirmdelete1(){
                var result = confirm('This Terminal is associated to user. So cannot delete this Terminal');
                return result
            }

            function subone(val){
                document.JournalEntryForm.batch.value=val;
                document.JournalEntryForm.buttonValue.value="batchdec";
                document.JournalEntryForm.submit();
            }
            function subone1(val)
            {
                document.JournalEntryForm.batch.value=val;
                document.JournalEntryForm.buttonValue.value="lastbatchdec";
                document.JournalEntryForm.submit();
            }

            function addoneje(val)
            {
                document.JournalEntryForm.journalid.value=val;
                document.JournalEntryForm.buttonValue.value="journalinc";
                document.JournalEntryForm.submit();
            }
            function addoneje1(val)
            {
                document.JournalEntryForm.journalid.value=val;
                document.JournalEntryForm.buttonValue.value="lastjournalinc";
                document.JournalEntryForm.submit();
            }
            function suboneje(val)
            {
                document.JournalEntryForm.journalid.value=val;
                document.JournalEntryForm.buttonValue.value="journaldec";
                document.JournalEntryForm.submit();
            }
            function suboneje1(val)
            {
                document.JournalEntryForm.journalid.value=val;
                document.JournalEntryForm.buttonValue.value="lastjournaldec";
                document.JournalEntryForm.submit();
            }
            function addonebatch(val)
            {
                document.JournalEntryForm.batch.value=val;
                document.JournalEntryForm.buttonValue.value="batchinc";
                document.JournalEntryForm.submit();
            }
            function addonebatch1(val)
            {
                document.JournalEntryForm.batch.value=val;
                document.JournalEntryForm.buttonValue.value="lastbatchinc";
                document.JournalEntryForm.submit();
            }
            function addform(val){
                if(document.JournalEntryForm.displineItemId!=undefined){
                    if(document.JournalEntryForm.displineItemId.length!=undefined){
                        for(var i=0;i<document.JournalEntryForm.displineItemId.length;i++){
                            var textstr = "document.JournalEntryForm.dispaccount"+(i);
                            var textobj = eval(textstr);
                            if(textobj!='undefined' && textobj.value!=''){
                                if(textobj.value.indexOf('-')==-1){
                                    alert('Invalid Account number entered - Please retry');
                                    textobj.select();
                                    textobj.focus();
                                    return false;
                                }
                            }
                        }
                    }else{

                        var textstr = "document.JournalEntryForm.dispaccount"+(0);
                        var textobj = eval(textstr);
                        if(textobj!='undefined' && textobj.value!=''){
                            if(textobj.value.indexOf('-')==-1){
                                alert('Invalid Account number entered - Please retry');
                                textobj.select();
                                textobj.focus();
                                return false;
                            }
                        }
                    }
                }
                if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value)||
                    (document.JournalEntryForm.jedebit.value=="0.00" && document.JournalEntryForm.jecredit.value=="0.00")){
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    return;
                }
                if(document.JournalEntryForm.jper.value==0){
                    alert("Cannot Save JE - Period must be entered");
                    return;
                }
                document.JournalEntryForm.batch.value=val;
                document.JournalEntryForm.buttonValue.value="addbatch";
                document.JournalEntryForm.submit();
            }



            function batchsave(){
                var flag=false;
                //new code added by vasan
                if(document.JournalEntryForm.displineItemId.length!=undefined){
                    for(var i=0;i<document.JournalEntryForm.displineItemId.length;i++){
                        var textstr = "document.JournalEntryForm.dispaccount"+(i);
                        var textobj = eval(textstr);
                        if(textobj!='undefined' && textobj.value!=''){
                            flag=true;
                            if(textobj.value.indexOf('-')==-1){
                                alert('Invalid Account number entered - Please retry');
                                textobj.select();
                                textobj.focus();
                                return false;
                            }
                        }
                    }
                }else{

                    var textstr = "document.JournalEntryForm.dispaccount"+(0);
                    var textobj = eval(textstr);
                    if(textobj!='undefined' && textobj.value!=''){
                        flag=true;
                        if(textobj.value.indexOf('-')==-1){
                            alert('Invalid Account number entered - Please retry');
                            textobj.select();
                            textobj.focus();
                            return false;

                        }
                    }
                }

                if(!flag){
                    alert("Cannot Save - batch contains a JE with no line items");
                    return false;
                }
                if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value)||
                    (document.JournalEntryForm.jedebit.value=="0.00" && document.JournalEntryForm.jecredit.value=="0.00")){
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    return;
                }
                if(document.JournalEntryForm.journalid.value==" "){
                    if(document.JournalEntryForm.jper.value=="0")
                    {
                        alert("Please Enter the Period");
                        return;
                    }
                }
                var x=document.getElementsByName("dispdebit");
                var y=0;

                var z=document.getElementsByName("dispcredit");
                var setFlag="";


                for(y=0; y<x.length; y++)
                {
                    if(setFlag!='bothFull')
                    {

                        if((x[y].value!="0.00" && !x[y].value=="") && (z[y].value!="0.00" && !z[y].value==""))
                        {
                            setFlag ="bothFull";
                        }
                    }


                }
                if(setFlag=="bothEmpty")
                {
                    alert("Please enter either Debit or Credit");

                    return;
                }

                if(document.JournalEntryForm.jper.value==0){
                    alert("Cannot Save JE - Period must be entered");
                    return;
                }

                document.getElementById('save').style.visibility = 'hidden'
                document.JournalEntryForm.buttonValue.value="savebatch";

                document.JournalEntryForm.submit();
            }
            function getsourcecode()
            {
                document.JournalEntryForm.buttonValue.value="sourcecode";
                document.JournalEntryForm.submit();
            }
            function saveline()
            {
                if(document.JournalEntryForm.journalid.value=="")
                {
                    alert("Please enter the Journal Entry Number");
                    return;
                }
                if(document.JournalEntryForm.displineItemId!=undefined){
                    if(document.JournalEntryForm.displineItemId.length!=undefined){
                        for(var i=0;i<document.JournalEntryForm.displineItemId.length;i++){
                            var textstr = "document.JournalEntryForm.dispaccount"+(i);
                            var textobj = eval(textstr);
                            if(textobj!='undefined' && textobj.value!=''){
                                if(textobj.value.indexOf('-')==-1){
                                    alert('Invalid Account number entered - Please retry');
                                    textobj.select();
                                    textobj.focus();
                                    return false;
                                }
                            }
                        }
                    }else{

                        var textstr = "document.JournalEntryForm.dispaccount"+(0);
                        var textobj = eval(textstr);
                        if(textobj!='undefined' && textobj.value!=''){
                            if(textobj.value.indexOf('-')==-1){
                                alert('Invalid Account number entered - Please retry');
                                textobj.select();
                                textobj.focus();
                                return false;
                            }
                        }
                    }
                }
                if(document.JournalEntryForm.jper.value==0){
                    alert("Cannot Save JE - Period must be entered");
                    return;
                }
                document.JournalEntryForm.buttonValue.value="addline";
                document.JournalEntryForm.journalid.focus();
                document.JournalEntryForm.submit();
            }
            function addaccount()
            {
                document.JournalEntryForm.buttonValue.value="addaccount";
                document.JournalEntryForm.submit();
            }
            function addjournal()
            {
                if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value)||
                    (document.JournalEntryForm.jedebit.value=="0.00" && document.JournalEntryForm.jecredit.value=="0.00"))
                {
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    return;
                }
                if(document.JournalEntryForm.bstatus.value=="deleted")
                {
                    alert("Cannot add, because this batch has been deleted");
                    return;
                }

                if(document.JournalEntryForm.journalid.value!=""){
                    if(document.JournalEntryForm.jper.value==0){
                        alert("Cannot Save JE - Period must be entered");
                        return;
                    }
                }
                document.JournalEntryForm.buttonValue.value="addjournal";
                document.JournalEntryForm.submit();
            }
            function savejournal()
            {
                if(document.JournalEntryForm.bstatus.value=="deleted")
                {
                    alert("Cannot save, because this batch has been posted");
                    return;
                }
                if(document.JournalEntryForm.jper.value==0){
                    alert("Cannot Save JE - Period must be entered");
                    return;
                }
                document.JournalEntryForm.buttonValue.value="savejournal";
                document.JournalEntryForm.submit();
            }
            function addline(val)
            {
                if(document.JournalEntryForm.bstatus.value=="deleted")
                {
                    alert("Cannot add line, because this batch has been posted");
                    return;
                }
                if(document.JournalEntryForm.jper.value==0){
                    alert("Cannot Save JE - Period must be entered");
                    return;
                }
                document.JournalEntryForm.journalid.value=val;
                document.JournalEntryForm.buttonValue.value="additemline";
                document.JournalEntryForm.submit();
            }
            function popup1(mylink, windowname)
            {
                var flag="false";
                if(document.JournalEntryForm.bstatus.value=="deleted")
                {
                    alert("Cannot Copy, because this batch has been posted");
                    return;
                }
                if(document.JournalEntryForm.journalid.value=="")
                {
                    alert("Please enter the JournalEntry Id");
                    return;
                }
                else if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value)||
                    (document.JournalEntryForm.jedebit.value=="0.00" && document.JournalEntryForm.jecredit.value=="0.00"))
                {
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    return;
                }
                else
                {
                    var x=document.getElementsByName("dispdebit");
                    var y=0;
                    var z=document.getElementsByName("dispcredit");
                    if(x.length==0 && y.length==0)
                    {
                        alert("Please add the line items");
                        return;
                    }
                    else
                    {
                        for(y=0;y<x.length;y++)
                        {
                            if(x[y].value=="0.00" && z[y].value=="0.00")
                            {
                                flag="true";
                            }
                            else
                            {
                                flag="false";
                                break;
                            }
                        }
                    }
                    if(flag=="true")
                    {
                        alert("Please enter the debit and credit for the line items");
                        return;
                    }
                }
                if(flag=="false")
                {
                    if (!window.focus)return true;
                    var href;
                    if (typeof(mylink) == 'string')
                        href=mylink;
                    else
                        href=mylink.href;
                    mywindow=window.open(href, windowname, 'width=800,height=600,scrollbars=yes');
                    mywindow.moveTo(200,180);
                    document.JournalEntryForm.buttonValue.value="copy";
                    document.JournalEntryForm.submit();
                    return false;
                }
            }
            function autoreverse()
            {
                var flag="false";
                if(document.JournalEntryForm.journalid.value=="")
                {
                    alert("Please enter the JournalEntry Id");
                    return;
                }
                else if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value))
                {
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    return;
                }
                else
                {
                    var x=document.getElementsByName("dispdebit");
                    var y=0;
                    var z=document.getElementsByName("dispcredit");
                    if(x.length==0 && y.length==0)
                    {
                        alert("Please add the line items");
                        return;
                    }
                    else
                    {
                        for(y=0;y<x.length;y++)
                        {
                            if(x[y].value=="0.00" && z[y].value=="0.00")
                            {
                                flag="true";
                            }
                            else
                            {
                                flag="false";
                                break;
                            }
                        }
                    }
                    if(flag=="true")
                    {
                        alert("Please enter the debit and credit for the line items");
                        return;
                    }
                }
                if(flag=="false")
                {
                    if(document.JournalEntryForm.jper.value==0){
                        alert("Cannot Save JE - Period must be entered");
                        return;
                    }
                    document.JournalEntryForm.buttonValue.value="autoreverse";
                    document.JournalEntryForm.submit();
                }
            }
            function reverse1(val)
            {
                var flag="false";
                if(document.JournalEntryForm.journalid.value=="")
                {
                    alert("Please this Journal Entry cannot be reversed. because it is not having Journal Entry");
                    return;
                }
                else
                {
                    var x=document.getElementsByName("dispdebit");
                    var y=0;
                    var z=document.getElementsByName("dispcredit");
                    if(x.length==0 && y.length==0)
                    {
                        alert("Please add the line items");
                        return;
                    }
                    else
                    {
                        for(y=0;y<x.length;y++)
                        {
                            if(x[y].value=="0.00" && z[y].value=="0.00")
                            {
                                flag="true";
                            }
                            else
                            {
                                flag="false";
                                break;
                            }
                        }
                    }
                    if(flag=="true")
                    {
                        alert("Please enter the debit and credit for the line items");
                        return;
                    }
                }
                if(flag=="false")
                {
                    if (!window.focus)return true;
                    var href='<%=path%>/jsps/Accounting/CopyBatch.jsp?batchId=<%=batchno%>&fromAction=Reverse';
                    mywindow=window.open(href, 'windows', 'width=800,height=600,scrollbars=yes');
                    mywindow.moveTo(200,180);
                    return false;
                }
            }



            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                } else {
                    limitCount.value = limitNum - limitField.value.length;
                }

            }
            function popup2(mylink, windowname)
            {
                if (!window.focus)return true;
                var href;
                if (typeof(mylink) == 'string')
                    href=mylink;
                else
                    href=mylink.href;
                mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                mywindow.moveTo(200,180);
                document.JournalEntryForm.buttonValue.value="";

                document.JournalEntryForm.submit();
                return false;
            }
            function disable(val,val1,val2)
            {


                if((val1!=null && val1!=" " && (val1=="posted" || val1=="deleted"))) // || val=="detail")
                {


                    var imgs = document.getElementsByTagName('img');

                    for(var k=0; k<imgs.length; k++)
                    {
                        if(val1=="posted" || val=="detail")
                        {
                            if(imgs[k].id != "backward" && imgs[k].id!="previous"  && imgs[k].id!="next" && imgs[k].id!="forward" && imgs[k].id!="reverse"
                                && imgs[k].id!="jebackward" && imgs[k].id!="jeprevious" && imgs[k].id!="jenext" && imgs[k].id!="jeforward" && imgs[k].id!="search"
                                && imgs[k].id!="add"  && imgs[k].id!="copy" )
                            {
                                imgs[k].style.visibility = 'hidden';
                            }
                        }
                        else if(val=="detail")
                        {
                            if(imgs[k].id != "backward" && imgs[k].id!="previous"  && imgs[k].id!="next" && imgs[k].id!="forward" && imgs[k].id!="search")
                            {
                                imgs[k].style.visibility = 'hidden';
                            }
                        }

                        else
                        {
                            if(imgs[k].id != "backward" && imgs[k].id!="previous"  && imgs[k].id!="next" && imgs[k].id!="forward"
                                && imgs[k].id!="jebackward" && imgs[k].id!="jeprevious" && imgs[k].id!="jenext" && imgs[k].id!="jeforward" && imgs[k].id!="search"
                                && imgs[k].id!="add")
                            {
                                imgs[k].style.visibility = 'hidden';
                            }
                        }


                        if(imgs[k].id == 'prev')
                        {
                            imgs[k].style.visibility = 'visible';
                        }

                    }
                    var input = document.getElementsByTagName("input");

                    for(i=0; i<input.length; i++)
                    {

                        if(input[i].id=='jedelete' || input[i].id=='jeadd' || input[i].id=='addline' || input[i].id=='save')
                        {
                            input[i].style.visibility='hidden';
                        }
                        input[i].readOnly=true;
                        input[i].style.color="blue";

                    }
                    var textarea = document.getElementsByTagName("textarea");
                    for(i=0; i<textarea.length; i++)
                    {
                        textarea[i].readOnly=true;
                        textarea[i].style.color="blue";

                    }
                    var select = document.getElementsByTagName("select");

                    for(i=0; i<select.length; i++)
                    {
                        select[i].disabled=true;
                        select[i].style.backgroundColor="blue";

                    }

                }
                else
                {
                    var img = document.getElementById('prev');
                    if(img.id == 'prev')
                    {
                        img.style. visibility = 'hidden';
                    }


                }

                if(val=="accountDesc") {
                    for(var i=0;i<document.JournalEntryForm.displineItemId.length;i++) {
                        if(document.JournalEntryForm.displineItemId[i].value==val2){
                            document.JournalEntryForm.dispdebit[i].focus();
                            document.JournalEntryForm.dispdebit[i].select();
                        }
                    }
                }


            }




            function linedelete(obj,val)
            {
                if(document.JournalEntryForm.bstatus.value=="deleted")
                {
                    alert("Cannot Delete, because this batch has been posted");
                    return;
                }
                var rowindex=obj.parentNode.parentNode.rowIndex;
                var x=document.getElementById('lineitemtable').rows[val+1].cells;

                document.JournalEntryForm.index.value=obj.name;

                document.JournalEntryForm.buttonValue.value="delete";


                var result = confirm("Are you sure you want to delete this Line Item");
                if(result)
                {
                    document.JournalEntryForm.submit();
                }
            }
            function getKey()
            {
                for(var i=0;i<document.JournalEntryForm.displineItemId.length;i++)
                {

                    if(document.JournalEntryForm.displineItemId[i].value==document.JournalEntryForm.hiddenItemNo.value)
                    {

                        var textstr = "document.JournalEntryForm.dispaccount"+(i+1);

                        var textobj = eval(textstr);

                        if(textobj==undefined)
                        {
                            saveline();
                        }
                        else
                        {
                            if(document.JournalEntryForm.dispdebit[i].value!='0.00' && document.JournalEntryForm.dispdebit[i].value!="")
                            {

                                textobj.focus();
                            }

                            else
                            {

                                document.JournalEntryForm.dispcredit[i].focus();
                            }
                        }

                    }
                }

            }

            function getKey4(){
                var jecredit=0;
                for(var i=0;i<document.JournalEntryForm.dispcredit.length;i++){
                    var myRegExp = new RegExp(",","g");
                    document.JournalEntryForm.dispcredit[i].value=document.JournalEntryForm.dispcredit[i].value.replace(myRegExp,"");
                    jecredit=jecredit+parseFloat(document.JournalEntryForm.dispcredit[i].value);
                    document.JournalEntryForm.dispcredit[i].value=new NumberFormat(document.JournalEntryForm.dispcredit[i].value).toFormatted();
                }
                jecredit=new NumberFormat(jecredit).toFormatted();
                document.JournalEntryForm.jecredit.value=jecredit;
            }

            function previous1() {
                document.JournalEntryForm.buttonValue.value="previous";
                document.JournalEntryForm.submit();
            }
            var z=0;
            var oldValue='';
            function getbatchdetails(){
                if(z==0){
                    oldValue=document.JournalEntryForm.batch.value;
                    z++;
                }
                if(event.keyCode==9 || event.keyCode==13){
                    BatchDAO.findBatchsearchforDojo(document.JournalEntryForm.batch.value,'',result);
                }
            }
            function result(data){
                if(data.length == 0){
                    alert("No records found");
                    var obj=document.getElementById("batch")
                    obj.value=oldValue;
                    obj.focus();
                    return;
                }else{
                    document.JournalEntryForm.buttonValue.value="batchdetails";
                    document.JournalEntryForm.submit();
                }
            }
            function print(){
                if((document.JournalEntryForm.jedebit.value!=document.JournalEntryForm.jecredit.value)||
                    (document.JournalEntryForm.jedebit.value=="0.00" && document.JournalEntryForm.jecredit.value=="0.00")){
                    alert("Journal Entry debits and credits are out of balance.  Cannot add new Batch");
                    //Journal Entry debits and credits are out of balance.  Cannot add new Batch
                    return;
                }

                if(document.JournalEntryForm.bstatus.value=='posted'){
                    document.JournalEntryForm.buttonValue.value="print";
                }else{
                    if(document.JournalEntryForm.jper.value==0){
                        alert("Cannot Save JE - Period must be entered");
                        return;
                    }
                    document.JournalEntryForm.buttonValue.value="saveprint";
                }
                document.JournalEntryForm.submit();
            }

            function getDebitTotal(ele){
		if(isNaN(ele.value) || trim(ele.value)==""){
		    ele.value=0.00;
		}
                var jedebit=0;
                for(var i=0;i<document.JournalEntryForm.dispdebit.length;i++){
                    var myRegExp = new RegExp(",","g");
                    var dispdebit=document.JournalEntryForm.dispdebit[i].value.replace(myRegExp,"");
                    jedebit=jedebit+parseFloat(dispdebit);
                    document.JournalEntryForm.dispdebit[i].value=new NumberFormat(document.JournalEntryForm.dispdebit[i].value).toFormatted();
                }
                document.JournalEntryForm.jedebit.value=new NumberFormat(jedebit).toFormatted();
            }
            function openMailPopup(batchNo){
                GB_show('Email','<%=path%>/sendEmail.do?id='+batchNo+'&moduleName=JournalEntry',455,650);
            }
            function setfocus()
            {
                var datatableobj = document.getElementById('lineitemtable');
                var j;
                if(datatableobj!=null){
                    for(i=0; i<datatableobj.rows.length; i++){
                        var textstr = "document.JournalEntryForm.dispaccount"+i;
                        var textobj = eval(textstr);
                        if(textobj!=null && textobj.value==''){
                            textobj.select();
                            textobj.focus();
                            j = i;
                            break;
                        }
                    }
                }
                var textstr = "document.JournalEntryForm.dispaccount"+j;
                var textobj = eval(textstr);
                textobj.select();
                textobj.focus();
            }
            function backToBatch(){
                document.JournalEntryForm.buttonValue.value="backToBatch";
                document.JournalEntryForm.submit();
            }


        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd"><br>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:errors/>
        <html:form action="/journalEntry" enctype="multipart/form-data" scope="request">
	    <div class="error">${error}</div>
	    <div class="message">${message}</div>
            <%if(session.getAttribute("buttonValue")!=null)
            {
            session.removeAttribute("buttonValue");
            } %>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="margin: 0 0 5px 0;">

                <tr class="textlabelsBold">

                    <td valign="top" colspan="7" align="right" style="padding-top:10px;">


			<html:file property="journalEntrySheet" styleId="journalEntrySheet" styleClass="textlabelsBoldForTextBox"/>
                        <input type="button" name="upload" value="Upload" class="buttonStyleNew" onclick="uploadJournalEntry()"/>
                        <input type="button" name="search" value="Print" class="buttonStyleNew" onclick="print()"/>
			<%if(null!=batchno && !batchno.trim().equals("")){%>
			    <input type="button" name="email" value="Email" class="buttonStyleNew" onclick="openMailPopup('<%=batchno%>')"/>
			<%}%>
                        <%if(buttonValue.equals("detail")){ %>
                        <input type="button" name="search" value="Back To Batch" class="buttonStyleNew" style="width:90px;" onclick="backToBatch()">
                        <%} %>
                        <input type="button" name="goBack" value="Go Back" class="buttonStyleNew" onclick="previous1()" id="prev"/>
			
                        <br></td>
                </tr>
            </table>

            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew"  >

                <tr >
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr class="tableHeadingNew"><td>Journal Entry Form<br></td>
                                <td align="right">
                                    <input type="button" name="search" onclick="addform('<%=batchno%>')" value="Add New Batch" class="buttonStyleNew" style="width:90px;"/>
                                    <a id="batchtoggle" href="#" onclick="Effect.toggle('batch_vertical_slide', 'slide'); return false;"><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="batch_vertical_slide">
                            <div>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">


                                    <tr class="textlabelsBold">
                                        <td>Total Batches:<%=noOfBatches%><br></td>

                                    </tr>
                                    <tr class="textlabelsBold" >
                                        <td width="18%">Batch Number <br></td>
                                        <td>
                                            <img src="<%=path%>/img/backward.gif" height="14" onclick="subone1('<%=batchno%>')" id="backward" class="control-buttons"/>
                                            <img src="<%=path%>/img/previous1.gif" height="14" onclick="subone('<%=batchno%>')" id="previous" class="control-buttons"/>
                                            <input name="batch" id="batch" onkeydown="getbatchdetails()" value="<%=batchno%>" size="10" class="textlabelsBoldForTextBox control-buttons"/>
                                            <img src="<%=path%>/img/next1.gif" height="14" onclick="addonebatch('<%=batchno%>')" id="next" class="control-buttons"/>
                                            <img src="<%=path%>/img/forw.gif" height="14" onclick="addonebatch1('<%=batchno%>')" id="forward" class="control-buttons" />
                                    <dojo:autoComplete formId="JournalEntryForm" textboxId="batch"  action="<%=path%>/actions/BatchNo.jsp?tabName=JOURNAL_ENTRY&from=1"/>
                                    </td>
                                    <td width="10%" align="left">&nbsp;<img src="<%=path%>/img/icons/search.gif" height="16" onclick="return popup2('<%=path%>/jsps/Accounting/Batchpopup.jsp?button='+'searchbatch','windows')" id="search"/><br></td>
                                    <td colspan="2">Description
                                        <html:text property="bdesc" value="<%=bdesc%>" styleClass="textlabelsBoldForTextBox" size="34"/>
                                        <br></td>
                                    <td  width="15%" align="right"><br></td>
                                    <td><br></td>
                                    </tr>
                                    <tr height="8%"></tr>

                                    <tr class="textlabelsBold">
                                        <td>Debit Total <br></td>
                                        <td><html:text property="btotaldebit" value="<%=btotaldebit%>" styleClass="areahighlightgrey" readonly="true" style="text-align:right;width:120px" ></html:text><br></td>
                                        <td><br></td>
                                        <td>Credit Total
                                            <html:text property="btotalcredit" value="<%=btotalcredit%>" styleClass="areahighlightgrey" readonly="true" style="text-align:right;width:120px"></html:text>
                                            Status
                                            <html:text property="bstatus" value="<%=bstatus%>" styleClass="areahighlightgrey" readonly="true" style="width:70px"></html:text>
                                            <br></td>
                                    </tr>

                                </table>
                            </div>
                        </div>
                    </td>
                </tr>

            </table>
            <br/><br/>



            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">

                <tr class="tableHeadingNew">
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew"><td>List of Journal Entry<br></td>
                                <td align="right"><a id="jetoggle" href="#" onclick="Effect.toggle('journalentry_vertical_slide', 'slide'); return false;"><img src="<%=path%>/img/icons/up.gif" border="0" /></a></td>
                            </tr>
                        </table>
                    </td>
                </tr>


                <tr>
                    <td>
                        <div id="journalentry_vertical_slide">
                            <div>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">


                                    <tr class="textlabelsBold">
                                        <td width="2%"  class="textlabelsBold" colspan="2">Total Journal Entry:<%=noOfJournalEntry%><br></td>

                                        <td valign="top" colspan="5" align="right" style="padding-top:3px;" class="textlabelsBold" > Auto Reverse
                                            <%

                                            if(!search.equals("copy") && (bstatus.equals("deleted") ||bstatus.equals("posted") || flag.equals("A")))
                                            { %>
                                            <input type="checkbox" name="myCheck" id="myCheck" checked="checked" disabled="disabled"/>
                                            <%}else
        { %>
                                            <html:checkbox property="reverse" styleClass="bigcheck" onclick="autoreverse()" name="journalEntryForm"></html:checkbox>
                                            <%} %>
                                            <%

                                            if(bstatus.equals("posted") && !flag.equals("R"))
                                            { %>
                                            <input type="button" name="search" value="Reverse" class="buttonStyleNew" onclick="reverse1('<%=jeperiod%>')"/>


                                            <%} %>
                                            <input type="button" name="search" style="margin-bottom:2px" value="Copy" class="buttonStyleNew" onclick="return popup1('<%=path%>/jsps/Accounting/CopyBatch.jsp?batchId=<%=batchno%>&fromAction=Copy','windows')"/>
                                            <input type="button" name="search" style="margin-bottom:2px" value="Add New" class="buttonStyleNew" onclick="addjournal()" id="jeadd"/>
                                            <input type="button" name="search" style="margin-bottom:2px" value="Delete" class="buttonStyleNew" onclick="journaldelete()" id="jedelete"/>
                                            <br></td>
                                    </tr>
                                    <tr height="10">
                                    </tr>

                                    <tr class="textlabelsBold" >
                                        <td width="10%">Entry Number <br></td>
                                        <td width="25%">
                                            <img src="<%=path%>/img/backward.gif"  height="14" onclick="suboneje1('<%=jeid%>')" id="jebackward"  class="control-buttons" />
                                            <img src="<%=path%>/img/previous1.gif" height="14" onclick="suboneje('<%=jeid%>')" id="jeprevious"  class="control-buttons"/>
                                            <html:text property="journalid" value="<%=jeid%>" styleClass="areahighlightgrey control-buttons" readonly="true" style="width:80px"></html:text>
                                            <img src="<%=path%>/img/next1.gif"  height="14" onclick="addoneje('<%=jeid%>')" id="jenext"  class="control-buttons"/>
                                            <img src="<%=path%>/img/forw.gif"  height="14" onclick="addoneje1('<%=jeid%>')" id="jeforward"  class="control-buttons"/>
											<input type="button" value="Export to Excel" onclick="exportToExcel()" class="buttonStyleNew" style="width:100px;"/>
										</td>
                                        <td width="10%" align="left">Description<br></td>
                                        <td width="30%" align="left"><html:text property="jedesc" value="<%=jedesc%>" size="25" styleClass="textlabelsBoldForTextBox"></html:text><br></td>
                                        <td width="10%" align="left">Period <br></td>
                                        <td width="20%">
                                            <c:choose>
                                                <c:when test="${roleDuty.journalEntryClosedPeriod}">
                                                    <html:text property="jper" styleClass="textlabelsBoldForTextBox" style="width:100px;" value="<%=jeperiod%>"/>
                                                    <input type="hidden" id="jperValid" name="jperValid"  value="<%=jeperiod%>"/>
                                                    <div id="periodChoices" style="display: none" class="newAutoComplete"></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <html:select property="jper" value="<%=jeperiod%>" styleClass="textlabelsBoldForTextBox" style="width:100px;">
                                                        <html:optionsCollection   name="periodList" styleClass="unfixedtextfiledstyle" />
                                                    </html:select>
                                                </c:otherwise>
                                            </c:choose>
                                            <br></td>
                                    </tr>
                                    <tr height="8%"></tr>
                                    <tr class="textlabelsBold" >
                                        <td>Date<br></td>
                                        <td><html:text property="txtCal" styleId="txtcal" styleClass="textlabelsBoldForTextBox float-left" size="15" value="<%=jedate%>"/>
                                            <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" class="calendar-img" height="16" align="top" id="cal" onmousedown="insertDateFromCalendar(this.id,0);" /><br></td>
                                        <td align="left" >Memo<br></td>
                                        <td align="left"><html:textarea property="jememo" value="<%=jememo%>" styleClass="textlabelsBoldForTextBox float-left" rows="2" onkeyup="limitText(this.form.jememo,this.form.countdown,200)" cols="31"></html:textarea></td>
                                        <td align="left">Source Code<br></td>
                                        <td >
                                            <html:select property="jesourcecode" value="<%=jesourcecode%>" onchange="getsourcecode()" styleClass="dropdown_accounting" style="width:100px;">
                                                <html:optionsCollection name="sourceCodeList" styleClass="unfixedtextfiledstyle" />
                                            </html:select><br></td>
                                    </tr>
                                    <tr class="textlabelsBold" >
                                        <td>Debit<br></td>
                                        <td><html:text property="jedebit" value="<%=jedebit%>" styleClass="areahighlightgrey" readonly="true" style="text-align:right;width:120px"></html:text><br></td>
                                        <td>Credit<br></td>
                                        <td><html:text property="jecredit" value="<%=jecredit%>" styleClass="areahighlightgrey" readonly="true" style="text-align:right"></html:text><br></td>
                                        <td  align="left">Source Desc<br></td>
                                        <td align="left"><html:text property="jescdesc" value="<%=jescdesc%>" styleClass="areahighlightgrey" readonly="true"></html:text><br></td>

                                    </tr>

                                </table>
                            </div>
                        </div>
                    </td>
                </tr>

            </table>
            <br/><br/>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" id="lineItem" style="float:left">

                <tr class="tableHeadingNew">
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew"><td>List of Line Items</td>
                                <td align="right">
                                    <input type="button" name="search" onclick="saveline()" value="Add Line Item" class="buttonStyleNew" style="width:80px;" id="addline"/>
                                    <a id="toggle" href="#" onclick="toggleDivs(this)">
                                        <input type="hidden" id="lineItemSlide" value="yes"/>
                                        <img src="<%=path%>/img/icons/up.gif" border="0" />
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>


                <tr>
                    <td>
                        <div id="lineitem_vertical_slide">
                            <div>
                                <table width="100%" border="0" cellpadding="0" cellspacing="0">

                                    <tr>
                                        <td>
                                            <div id="divtablesty1" class="scrolldisplaytable">
                                                <table border="0" cellpadding="0" cellspacing="0">
                                                    <% int i=0;

                                                    %>

                                                    <display:table  name="<%=lineItemList%>" pagesize="2000"  id="lineitemtable" class="displaytagstyleNew">

                                                        <display:setProperty name="paging.banner.some_items_found">
                                                            <span class="pagebanner">
                                                                <font color="blue">{0}</font> LineItem details displayed,For more LineItems click on page numbers.
                                                                <br>
                                                            </span>
                                                        </display:setProperty>
                                                        <display:setProperty name="paging.banner.one_item_found">
                                                            <span class="pagebanner">
						One {0} displayed. Page Number
                                                            </span>
                                                        </display:setProperty>
                                                        <display:setProperty name="paging.banner.all_items_found">
                                                            <span class="pagebanner">
						{0} {1} Displayed, Page Number

                                                            </span>
                                                        </display:setProperty>

                                                        <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
                                                            </span>
                                                        </display:setProperty>
                                                        <display:setProperty name="paging.banner.placement" value="bottom" />
                                                        <display:setProperty name="paging.banner.item_name" value="LineItem"/>
                                                        <display:setProperty name="paging.banner.items_name" value="LineItems"/>
                                                        <%
                                                        String currency1="";
                                                        String lineItemId="";
                                                        String dispreference="";
                                                        String disprefdesc="";
                                                        String dispaccount="";
                                                        String dispaccdisc="";
                                                        String dispcredit="0.00";
                                                        String dispdebit="0.00";
                                                        if(lineItemList!=null && lineItemList.size()>0)
                                                        {
                                                                LineItem line1=(LineItem)lineItemList.get(i);

                                                                if(line1.getLineItemId()!=null)
                                                                {
                                                                        lineItemId=line1.getLineItemId();
                                                                }
                                                                if(line1.getReference()!=null)
                                                                {
                                                                dispreference=line1.getReference();
                                                                }
                                                                if(line1.getReferenceDesc()!=null)
                                                                {
                                                                disprefdesc=line1.getReferenceDesc();
                                                                }
                                                                if(line1.getAccount()!=null)
                                                                {
                                                                dispaccount=line1.getAccount();
                                                                }

                                                                if(line1.getAccountDesc()!=null)
                                                                {
                                                                dispaccdisc=line1.getAccountDesc();

                                                                }
                                                                if(line1.getDebit()!=null )
                                                                {
                                                                        dispdebit=number.format(line1.getDebit());
                                                                }
                                                                if(line1.getCredit()!=null)
                                                                {
                                                                        dispcredit=number.format(line1.getCredit());
                                                                }
                                                                if(line1.getCurrency()!=null)
                                                                {
                                                                        currency1=line1.getCurrency();
                                                                }
                                                        }
                                                        %>
                                                        <display:column style="vertical-align: top">
                                                            <img src="<%=path%>/img/icons/toggle.gif" id="imageToggle<%=i%>" style="display: block;cursor: pointer;" onclick="drillDown('<%=i%>')"/>
                                                            <img src="<%=path%>/img/icons/toggle_collapse.gif" id="collapse<%=i%>" style="display: none;cursor: pointer;" onclick="drillDownClose('<%=i%>')"/>
                                                        </display:column>
                                                        <display:column title="Acct No"  style="vertical-align: top">
                                                            <input type="text" name="dispaccount" id="dispaccount<%=i%>" value="<%=dispaccount%>"  class="textlabelsBoldForTextBox"/>
							    <input type="hidden" name="dispaccountvalid<%=i%>" id="dispaccountvalid<%=i%>" value="<%=dispaccount%>"/>
							    <div class="newAutoComplete" id="dispaccountdiv<%=i%>"></div>
							    <script type="text/javascript">
									AjaxAutocompleter("dispaccount<%=i%>","dispaccountdiv<%=i%>","","dispaccountvalid<%=i%>",rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=dispaccount<%=i%>","triggerTab('dispdebit<%=i%>')");
								    </script>
                                                            <div id="drillDownSlide<%=i%>" style="display: none;">
                                                                <div id="drillDown<%=i%>" class="drillDownDiv"></div>
                                                            </div>
                                                        </display:column>
                                                        <display:column title="Debit" style="vertical-align: top">
							    <input type="text" name="dispdebit" id="dispdebit<%=i%>" value="<%=dispdebit%>" onkeyup="gotoNextGlAccount(<%=i%>)"
									     style="text-align:right" class="textlabelsBoldForTextBox" maxlength="10" onblur="getDebitTotal(this)"/>
							</display:column>
                                                        <display:column title="Credit"  style="vertical-align: top">
							    <input type="text" name="dispcredit" id="dispcredit<%=i%>" value="<%=dispcredit%>" onkeyup="gotoNextGlAccount(<%=i%>)"
									     style="text-align:right" class="textlabelsBoldForTextBox" maxlength="10" onblur="getKey4()"/>
							</display:column>
                                                        <display:column title="Item No"  style="vertical-align: top">
							    <html:text property="displineItemId" value="<%=lineItemId%>" readonly="true" styleClass="areahighlightgrey" style="width:110px;"/>
							</display:column>
                                                        <display:column title="Reference"  style="vertical-align: top">
							    <html:text property="dispreference" value="<%=dispreference%>" styleClass="textlabelsBoldForTextBox" />
							</display:column>
                                                        <display:column title="Description"  style="vertical-align: top">
							    <html:text  property="dispreferenceDesc" styleId="dispreferenceDesc<%=i%>" value="<%=disprefdesc%>" styleClass="textlabelsBoldForTextBox" />
							</display:column>
                                                        <display:column title="Currency"  style="vertical-align: top">
							    <html:text property="dispcurrency" readonly="true" value="<%=currency1%>" styleClass="textlabelsBoldForTextBox" size="3"/>
							</display:column>
                                                        <display:column title="Actions"  style="vertical-align: top">
                                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();" >
                                                                <img src="<%=path%>/img/icons/delete.gif" border="0" alt="" onclick="linedelete(this,<%=i%>)" name="<%=i%>"  value="<%=i%>"/> </span>
                                                            </display:column>
                                                            <%i++; %>
                                                        </display:table>
                                                </table></div></td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>

            <table width="100%"  style="float:left">
                <tr>
                    <td valign="top" colspan="20" align="center" style="padding-top:10px;" class="style2">
                        <input type="button" name="search" onclick="batchsave()" value="Save" class="buttonStyleNew" id="save"/>
                        <input type="button" name="search" onclick="addform('<%=batchno%>')" value="Add New Batch" class="buttonStyleNew" style="width:90px;"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
            <html:hidden property="hiddenjeperiod" styleId="jeperiod" value="<%=jeperiod%>"/>
            <html:hidden property="hiddenItemNo" value="<%=lineId%>"/>
            <html:hidden property="index"/>
            <!--  This is for the mailer -->
            <input type="hidden" name="mailToAddress" />
            <input type="hidden" name="mailCcAddress" />
            <input type="hidden" name="mailBccAddress" />
            <input type="hidden" name="mailSubject" />
            <input type="hidden" name="mailBody" />
        </html:form>
    </body>
    <%
            if(session.getAttribute("from")!=null){
                    if(((String)session.getAttribute("from")).equals("directFromBatch")){
    %>
    <script language="JavaScript1.2">addjournal();</script>
    <%
}
session.removeAttribute("from");
}
    %>
    <c:if test="${fileName!=null}">
        <script type="text/javascript">
            window.parent.showGreyBox("Journal Entry Report","<%=path%>/servlet/FileViewerServlet?fileName=${fileName}");
        </script>
    </c:if>
    <script type="text/javascript" src="<%=path%>/js/common.js"></script>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
    <script type="text/javascript">
        disable('<%=buttonValue%>','<%=bstatus%>','<%=lineId%>');
        //setfocus();
        function drillDown(index){
            var glAccountNumber = document.getElementById('dispaccount'+index).value;
            var lineItemNumber = document.getElementsByName('displineItemId');
            var checkIndex = document.getElementById("index").value;
            if(null!=checkIndex && trim(checkIndex)!=""){
                Effect.SlideUp('drillDownSlide'+checkIndex);
                document.getElementById('imageToggle'+checkIndex).style.display="block";
                document.getElementById('collapse'+checkIndex).style.display="none";
            }
            document.getElementById('imageToggle'+index).style.display="none";
            document.getElementById('collapse'+index).style.display="block";
            DwrUtil.drillDownForJE(glAccountNumber,lineItemNumber[index].value,function(data){
                if(data){
                    document.getElementById('drillDown'+index).innerHTML = data;
                    Effect.SlideDown('drillDownSlide'+index);
                    document.getElementById("index").value=index;
                    Effect.SlideDown('drillDownSlide'+index);
                }
            });
        }
        function drillDownClose(index){
            document.getElementById('imageToggle'+index).style.display="block";
            document.getElementById('collapse'+index).style.display="none";
            Effect.SlideUp('drillDownSlide'+index);
            document.getElementById("index").value="";
        }

        function toggleDivs(obj){
            if(obj.id=="toggle"){
                if(document.getElementById('lineItemSlide').value=="yes"){
                    Effect.SlideUp('lineitem_vertical_slide');
                    document.getElementById('lineItemSlide').value="no";
                }else{
                    Effect.SlideDown('lineitem_vertical_slide');
                    Effect.SlideDown('lineitem_vertical_slide');
                    document.getElementById('lineItemSlide').value="yes";
                }
            }
        }
        if(document.getElementById("periodChoices")){
            AjaxAutocompleter("jper", "periodChoices","", "jperValid", rootPath+"/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=jper&from=JE","","");
        }

	function exportToExcel(){
	    document.JournalEntryForm.buttonValue.value="exportToExcel";
	    document.JournalEntryForm.submit();
	}
	function uploadJournalEntry(){
	    if(document.getElementById("journalEntrySheet").value==""){
		alert("Please include Journal Entry Sheet");
	    }else{
		document.JournalEntryForm.buttonValue.value="uploadJournalEntry";
		document.JournalEntryForm.submit();
	    }
	}

	function triggerTab(nextEle){
	    document.getElementById(nextEle).focus();
	    if(document.getElementById(nextEle).value=="0.00"){
		document.getElementById(nextEle).value="";
	    }
	}
	function gotoNextGlAccount(index){
	    if(event.keyCode==13){
		index++;
		var ele = document.getElementById("dispaccount"+index);
		if(null!=ele && undefined!=ele){
		    ele.focus();
		}
	    }
	}
    </script>
</html>


<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<%@taglib prefix="fmt" uri="/WEB-INF/fmt-1_0-rt.tld" %>
<head>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>

    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <meta name="keywords" content="" />
    <meta name="description" content=" " />
    <meta name="author" content="LogiwareInc." />
    <script type="text/javascript">
        $(document).ready(function() {
            /* color Box */
            $(".osdNo").colorbox({iframe:true, width:"60%", height:"370px", title:"OSD"});
            $(".hsCode").colorbox({iframe:true, width:"60%", height:"370px", title:"HS Code"});
            $(".unitSsRemark").colorbox({iframe:true, width:"60%", height:"68%", title:"Remarks"});
            $(".noteNo").colorbox({iframe:true, width:"60%", height:"370px", title:"Notes"});
            $(".contact").colorbox({iframe:true, width:"90%", height:"90%", title:"Contact"});
            $(".cargo").colorbox({iframe:true, width:"40%", height:"50%", title:"Cargo Received"});
            $(".ncmNo").colorbox({iframe:true, width:"60%", height:"370px", title:"NCM Number"});
            $(".hazmat").colorbox({iframe:true, width:"90%", height:"90%", title:"Haz.Mat"});
            $(".aes").colorbox({iframe:true, width:"90%", height:"90%", title:"AES details"});
            $(".invoice").colorbox({iframe:true, width:"80%", height:"97%", title:"AR Invoice"});
            $(".fax").colorbox({iframe:true, width:"75%", height:"90%", title:"Print/Fax/Email"});
            $(".tracking").colorbox({iframe:true, width:"75%", height:"90%", title:"Tracking"});
            $(".cargopopup").colorbox({iframe:true, width:"75%", height:"90%", title:"Cargo Received"});
            $(".scan").colorbox({iframe:true, width:"80%", height:"90%", title:"Scan/Attach"});
            $(".importRelease").colorbox({iframe:true, width:"80%", height:"90%", title:"Import Release"});
            $(".inbonds").colorbox({iframe:true, width:"80%", height:"80%",title:"Inbonds"});
            $(".consolidate").colorbox({iframe:true, width:"80%", height:"80%",title:"Consolidate"});
            $(".Terminate").colorbox({iframe:true, width:"30%", height:"50%", title:"Terminate"});
            $(".editSSDetails").colorbox({iframe:true, width:"80%", height:"80%", title:"Edit Detail"});
            $(".editSSMasterDetails").colorbox({iframe:true, width:"80%", height:"80%", title:"Edit SS Master Detail"});
            $(".editWarehouse").colorbox({iframe:true, width:"70%", height:"50%", title:"<span style=color:blue>Edit Warehouse</span>"});
            $(".disposition").colorbox({iframe:true, width:"100%", height:"90%", title:"<span style=color:blue>Disposition</span>"});
            $(".lclDomTermination").colorbox({iframe:true, width:"30%", height:"50%", title:"Terminate"});
            $(".lclDomTermination1").colorbox({iframe:true, width:"30%", height:"50%", title:"Terminate"});
            $(".quickDR").colorbox({iframe:true, width:"90%", height:"70%", title:"Quick Booking"});
             $(".editUnits").colorbox({iframe:true, width:"100%", height:"90%", title:"Edit Unit"});
            $(".callbacks").colorbox({
                onOpen:function(){ alert('onOpen: colorbox is about to open'); },
                onLoad:function(){ alert('onLoad: colorbox has started to load the targeted content'); },
                onComplete:function(){ alert('onComplete: colorbox has displayed the loaded content'); },
                onCleanup:function(){ alert('onCleanup: colorbox has begun the close process'); },
                onClosed:function(){ alert('onClosed: colorbox has completely closed'); }
            });
        });
                
    </script>
</head>

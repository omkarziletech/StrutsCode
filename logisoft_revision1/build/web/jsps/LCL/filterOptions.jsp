<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <%@page import="com.gp.cong.logisoft.domain.User"%>
    <%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
    <%@include file="init.jsp" %>
    <%@include file="colorBox.jsp"%>
    <%@include file="/taglib.jsp"%>
    <body>
        <h1 style="color:#000000">Logic for Inventory Filter Options in Logiware LCL</h1>

        NOTE: For all of these Inventory filters, the word inventory means shipments that have not yet been exported out of
        the USA. So the results list will ALWAYS EXCLUDE shipments that have been Loaded, have already sailed, are at the POD
        or the FD, or have been picked up, canceled, or terminated.

        <h2 style="color:red">Inventory All </h2>– This option will show all bookings (both received and Not yet received), and will be further filtered by
        any one or more of the Relay cities, i.e. the POO, POL, POD, FD. The user must fill in at least one of these cities, but can
        use up to all 4. The filter must match on whatever they put in. The current location of the cargo can be ignored EXCEPT
        when the user fills in a POO, in that case, the current location MUST match the POO. Also, The POO synonyms list
        must always apply, so if they put in Chicago for example, it would include cities like Detroit, in addition to Chicago.
        <br/>
        <h2 style="color:red">Inventory Booked</h2> - This is the same as Inventory all, except it will only display items that Are in “B” status meaning they
        have not yet been received.
        <br/>
        <h2 style="color:red"> Inventory Received</h2> - This is the same is Inventory All, except it will only display items that are NOT in “B” status,
        meaning they have been received at the warehouse.
        <br/>
        <h2 style="color:red">Inventory In Transit to POL</h2> – This view will only show items where the Current Location = In Transit.
        The user MUST enter at least a POL, but has the option of also entering the POO, POD, and FD.
        If the user enters only a POL, it simply shows all shipments that are currently In Transit to the POL. They can further limit
        this result by inputting a POD.
        If the user enters a POO and a POL, then it will return items in transit to POL, that originated at POO (or its synonyms).
        This view can also be further limited by inputting a POD (and FD if necessary).
        <br/>
        <h2 style="color:red">Inventory at POL</h2> – This view will require the user to select a POL, and then it will only show shipments where the
        Current Location matches the POL. The user can further narrow the search by inputting a POO, POD, or FD.
        <br/>
        <h2 style="color:red"> BL Pool</h2> – Show all BLs that have been started, but not yet posted. The user can then choose to see by owner, with
        a “Me” option.
        <br/>
        <h2 style="color:red">Loaded with No B/L</h2> – This view shows all shipments that have already been loaded, but a B/L has not yet been posted
        against the shipment.
        <br/>
        <br/>
    </body>
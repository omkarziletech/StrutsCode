<%-- 
    Document   : applicationHeader
    Created on : Nov 7, 2011, 4:34:28 PM
    Author     : Thamizh
--%>

<div id="header-container">

    <div id="logo">
        <a href=""> <img src="images/logo-lagiware.png" alt="logo"/>  </a>
    </div><!-- Logo part ends here -->

    <div id="menu">
        <ul>
            <li class="firstnode"><a href="javascript:showHome()">Home</a></li>
            <li><a href="javascript: void(0)">Accounting</a>
                <ul>
                    <li><a href="javascript:showChild(174,'Accounting')">Accounts Payable</a></li>
                    <li><a href="javascript:showChild(164,'Accounting')">Accounts Receivable</a></li>
                    <li><a href="javascript:showChild(160,'Accounting')">General Ledger</a></li>
                    <li><a href="javascript:showChild(36,'Accounting')">Trading Partner Maintenance</a></li>
                </ul>
            </li>
            <li>
                <a href="javascript: void(0)">Exports</a>
                <ul>
                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">FCL</a>
                        <ul>

                            <li><a href="javascript:showChild(55,'Exports')">Quotes, Bookings, and BLs</a></li>

                            <li><a href="javascript:showChild(205,'Exports')">BL Correction Notice Pool</a></li>

                            <li><a href="javascript:showChild(218,'Exports')">SS Masters Disputed</a></li>

                        </ul>
                    </li>

                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">LCL</a>
                        <ul>
                            <li><a href="search.html"> Search </a></li>
                            <li><a href="lcl-quote.html"> Quote </a></li>
                            <li><a href="index.html"> Bookings, and BLs</a></li>
                            <li><a href="container-management.html"> Container Management</a></li>
                        </ul>
                    </li>
                    <li><a href="javascript:showChild(248,'Exports')">Inttra Inbound SI (EDI)</a></li>
                    <li><a href="javascript:showChild(36,'Exports')">Trading Partner Maintenance</a></li>
                </ul>
            </li>

            <li>
                <a href="javascript: void(0)">Imports</a>
                <ul>

                    <li><a class="sub-menu-ind" href="javascript: void(0)">FCL</a>
                        <ul>
                            <li><a href="javascript:showChild(221,'Imports')">Quotes, Bookings, and BLs</a></li>
                        </ul>
                    </li>

                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">LCL</a>
                        <ul>
                            <li><a href="javascript:showChild(55,'Exports')"> Bookings, and BLs</a></li>
                        </ul>
                    </li>
                    <li><a href="javascript:showChild(36,'Imports')">Trading Partner Maintenance</a></li>
                </ul>
            </li>

            <li>
                <a href="javascript: void(0)">Utilities</a>
                <ul>
                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">Admin</a>
                        <ul>

                            <li><a href="javascript:showChild(88,'Utilities')">Menu Management</a></li>

                            <li><a href="javascript:showChild(192,'Utilities')">Utilities/Scheduler</a></li>

                            <li><a href="javascript:showChild(213,'Utilities')">Users Online</a></li>

                        </ul>
                    </li>
                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">Data Exchange</a>
                        <ul>
                            <li><a href="javascript:showChild(116,'Utilities')">Data Exchange Management</a></li>
                        </ul>
                    </li>

                    <li>
                        <a class="sub-menu-ind" href="javascript: void(0)">Data Management</a>
                        <ul>

                            <li><a href="javascript:showChild(26,'Utilities')">Voyage Management</a></li>

                            <li><a href="javascript:showChild(31,'Utilities')">Rate Management</a></li>

                            <li><a href="javascript:showChild(40,'Utilities')">Reference Data Management</a></li>

                        </ul>
                    </li>
                </ul>
            </li>



        </ul>
    </div><!-- Menu part ends here -->


    <div id="login-info">
        <div id="server-info">
            Terminal : <span id="terminalinfo">17 - ATLANTA, GA</span>
        </div>

        <div id="user-info">
            Welcome <span id="username"> Tamilmani </span>  ( <span id="rolename">admin</span>)
        </div>

        <div id="logout">
            <a href="">Logout</a>
        </div>

    </div><!-- Login info part ends here -->

</div><!-- Header part ends here -->
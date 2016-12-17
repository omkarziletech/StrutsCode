function releaseRoleDuty(){
    var releaseFlag=false;
    var roleId=$('#userRoleId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO",
            methodName: "getRoleDetails",
            param1: "bkgvoyage_releaseDr",
            param2: roleId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            releaseFlag = data;
        }
    });
    return releaseFlag;
}

function bkgContactRoleDuty(){
    var releaseFlag=false;
    var roleId=$('#userRoleId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO",
            methodName: "getRoleDetails",
            param1: "lcl_bookingcontact",
            param2: roleId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            releaseFlag = data;
        }
    });
    return releaseFlag;
}
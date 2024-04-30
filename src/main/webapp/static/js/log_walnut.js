function detailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitselectJsonById('+row.id+',1)\">详细</a>'
    ].join("")
}

function logdetailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitPatientLogById('+row.id+',1)\"><i class="fa text-navy" ></i>详细</a>'
    ].join("")
}

//表格超链接显示
function LogFormatter(value, row, index){
    var ss="";
    if (row.medicalInfo!=null){
        ss=row.medicalInfo;
    }
    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.medId+',0)\">'+ss+'</a></div>'
    ].join("")
}
function LogFormatter2(value, row, index){
    var ss="";
    if (row.patientInfo!=null){
        ss=row.patientInfo;
    }
    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.patId+',1)\">'+ss+'</a></div>'
    ].join("")
}

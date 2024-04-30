//表格超链接显示
function LogFormatter(value, row, index){
    return [
        '<a onclick=\"submitselectJsonById('+row.id+',3)\">'+row.requestdetails+'</a>'
    ].join("")

}
function LogFormatter2(value, row, index){
    return [
        '<div class="wrap-logs"><a onclick=\"submitPatientLogById('+row.id+',\'反向校验\')\"><i class="fa text-navy" ></i>'+row.reqjson+'</a></div>'
    ].join("")

}

function reqjsonLogFormatter(value, row, index){
    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.id+',4)\">'+row.responsedetails+'</a></div>'
    ].join("")
}

function reqjsonLogFormatter2(value, row, index){
    return [
        '<div class="wrap-logs"><a onclick=\"submitPatientLogById('+row.id+',1)\"><i class="fa text-navy" ></i>'+row.rspjson+'</a></div>'
    ].join("")
}

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



function detailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitselectJsonById('+row.id+',3)\">请求详细</a>'
    ].join("")
}

function logdetailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitPatientLogById('+row.id+',\'反向校验\')\"><i class="fa text-navy" ></i>请求详细</a>'
    ].join("")
}

function rspjsonLogFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.id+',3)\">'+row.requestdetails+'</a>/div>'
    ].join("")

}

function rspjsonFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitPatientLogById('+row.id+',\'反向校验\')\"><i class="fa text-navy" ></i>'+row.reqjson+'</a></div>'
    ].join("")

}
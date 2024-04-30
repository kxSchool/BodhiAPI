function detailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitselectJsonById('+row.id+',3)\">详细</a>'
    ].join("")
}

function rspjsonLogFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.id+',3)\">'+row.responsedetails+'</a></div>'
    ].join("")

}

function reqjsonLogFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.id+',4)\">'+row.responsedetails+'</a></div>'
    ].join("")

}
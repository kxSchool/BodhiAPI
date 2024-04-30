function ingoreAiError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreAiError',
        data: JSON.stringify(params),
        contentType:"application/json;charset=UTF-8",
        dataType:'json',
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function(data){
            var code=data.code;
            if(code==200){
                alert("修改成功");
                $("#tb_patientLogs").bootstrapTable('refresh');
                window.parent.myfunction();
            }else{
                alert("修改失败");
            }
        },
        error:function(){
            alert("取数失败！");
        }
    });
}

function detailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitselectJsonById('+row.id+',5)\">详细</a>'
    ].join("")
}


function rspjsonFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitPatientLogById('+row.id+',1)\"><i class="fa text-navy" ></i>'+row.rspjson+'</a></div>'
    ].join("")

}

function oprateAiSlideLogFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_aislide.html\", 9, "+row.reqjson+");'>接口调试</a>";
    if(  row.state!=0 && row.state!=3  && row.state!=4 ){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>标志异常</a>" ;
    }
    return [html].join("")
}

function oprateAiSlideFormatter(value, row, index) {
    var params={
        "slideNo":"",
        "pathNo":row.pathNo
    };
    var html="<a onclick='addMenuTab2(\"interfaceTest_aislide.html\", 9, "+JSON.stringify(params)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreAiError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreAiError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function logdetailsFormatter(value, row, index) {
    return [
        '<a onclick=\"submitPatientLogById('+row.id+',1)\"><i class="fa text-navy" ></i>详细</a>'
    ].join("")
}
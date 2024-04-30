function logdetailsFormatter(value, row, index) {

    return [
        '<a onclick=\"submitPatientLogById('+row.id+',1)\"><i class="fa text-navy" ></i>详细</a>'
    ].join("")

}

function oprateMedicalLogFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_patientRegister.html\", 6, \""+row.reqdate+"\");'>接口调试</a>";
    if(  row.state!=0 && row.state!=3  && row.state!=4 ){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>标志异常</a>" ;
    }
    return [html].join("")
}

function LogFormatter(value, row, index){

    return [
        '<div class="wrap-logs"><a onclick=\"submitselectJsonById('+row.id+',0)\">'+row.rspJson+'</a></div>'
    ].join("")
}

function addFunctionAlty(value, row, index) {
    return [
        '<button class="showDetail" type="button" className="btn btn-primary">详细</button><button class="statistics" type="button" className="btn btn-primary">统计</button>'
    ].join('');
}

function oprateMedicalFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_patientRegister.html\", 6, \""+row.req_updateDateTime+"\");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreMedicalError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreMedicalError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function ingoreMedicalError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreMedicalError',
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
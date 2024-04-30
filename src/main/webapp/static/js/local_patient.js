function stateFormatterLog(value, row, index) {
    var html;
    if(row.state==0){
        html='<i class="fa fa-check text-navy"></i> 正常';
    }else if (row.state==1){
        html='<i class="fa fa-close text-danger"></i> 系统异常';
    }else if(row.state==2){
        html='<i class="fa fa-close text-danger"></i> 接口异常';
    }else if(row.state==3 || row.state==4){
        html='<i class="fa fa-check text-warning"></i> 忽略异常';
    }
    return [
        html
    ].join("")
}

function stateFormatter(value, row, index) {
    var html;
    if(row.logstate==0){
        html='<i class="fa fa-check text-navy"></i> 正常';
    }else if (row.logstate==1){
        html='<i class="fa fa-close text-danger"></i> 系统异常';
    }else if(row.logstate==2){
        html='<i class="fa fa-close text-danger"></i> 接口异常';
    }else if(row.logstate==3 || row.logstate==4){
        html='<i class="fa fa-check text-warning"></i> 忽略异常';
    }else{
        html='<i class="fa fa-check text-navy"></i> 正常';
    }
    return [
        html
    ].join("")
}

function oprateFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_patient.html\", 3, \""+row.req_updateDateTime+"\");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function oprateLogFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_patient.html\", 3, \""+row.reqdate+"\");'>接口调试</a>";
    if(  row.state!=0 && row.state!=3  && row.state!=4 ){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreLogError("+row.id+")'>标志异常</a>" ;
    }
    return [html].join("")
}

function ingoreLogError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreLogError',
        data: JSON.stringify(params),
        dataType:'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function(data){
            var code=data.code;
            if(code==200){
                alert("修改成功");
                $("#tb_debug").bootstrapTable('refresh');
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

function ingoreError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreError',
        data: JSON.stringify(params),
        dataType:'json',
        contentType: "application/json; charset=UTF-8",
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

function ingoreWandaLogError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreLogError',
        data: JSON.stringify(params),
        dataType:'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function(data){
            var code=data.code;
            if(code==200){
                alert("修改成功");
                $("#tb_debug").bootstrapTable('refresh');
                window.parent.bodyfunction();
            }else{
                alert("修改失败");
            }
        },
        error:function(){
            alert("取数失败！");
        }
    });
}

function ingoreWandaError(id){
    var params={"id":id};
    $.ajax({
        type: "POST",
        url: '/langjiaInterface/ingoreWandaError',
        data: JSON.stringify(params),
        dataType:'json',
        contentType: "application/json; charset=UTF-8",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("Authorization", handleLocalStorage('get', 'Authorization'));
        },
        success: function(data){
            var code=data.code;
            if(code==200){
                alert("修改成功");
                $("#tb_patientLogs").bootstrapTable('refresh');
                window.parent.bodyfunction();
            }else{
                alert("修改失败");
            }
        },
        error:function(){
            alert("取数失败！");
        }
    });
}

// function oprateRecheckLogFormatter(value, row, index) {
//     var html="<a onclick='addMenuTab(\"interface_local_filecheck.html\", \"反向校验对接调试\", "+JSON.stringify(row.reqjson, null, 2)+");'>接口调试</a>";
//     if(  row.state!=0 && row.state!=3  && row.state!=4 ){
//         html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
//     }else if(row.state==3 || row.state==4){
//         html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
//     }
//     return [html].join("")
// }
//
// function oprateRecheckFormatter(value, row, index) {
//     var html="<a onclick='addMenuTab(\"interface_local_filecheck.html\", \"反向校验对接调试\", "+JSON.stringify(row.requestdetails, null, 2)+");'>接口调试</a>";
//     if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
//         html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
//     }else if(row.logstate==3 || row.logstate==4){
//         html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
//     }
//     return [
//         html
//     ].join("")
// }

function getTokenFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_wanda_token.html\", 12, \""+row.req_updateDateTime+"\");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function getTokenFormatterDebug(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_wanda_token.html\", 12, \"\");'>接口调试</a>";
    if(row.state!=null && row.state!=0 && row.state!=3  && row.state!=4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function pushPatientFormatter(value, row, index) {
    var ss=JSON.parse(row.pushJson);
    var dd=JSON.stringify(ss)
    var patientInfo2=dd
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushpatient.html\", 15, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")

}
function pushPatientFormatterDebug(value, row, index) {
    var ss=JSON.parse(row.pushJson);
    var dd=JSON.stringify(ss)
    var patientInfo2=dd
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushpatient.html\", 15, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.state!=null && row.state!=0 && row.state!=3  && row.state!=4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function pushMedicalInfoFormmater(value, row, index) {
    var ss=JSON.parse(row.pushJson);
    var dd=JSON.stringify(ss)
    var patientInfo2=dd
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushmedical.html\", 18, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")

}
function getWalnutInfoFormmater(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_walnutPath.html\", 30, \""+row.searchDate+"\");'>接口调试</a>";

    return [
        html
    ].join("")

}
function getWalnutInfoFormmaterDebug(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interfaceTest_walnutPath.html\", 30, \""+row.reqdate+"\");'>接口调试</a>";
    if(row.state!=null && row.state!=0 && row.state!=3  && row.state!=4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")

}
function pushMedicalInfoFormmaterDebug(value, row, index) {
    var ss=JSON.parse(row.pushJson);
    var dd=JSON.stringify(ss)
    var patientInfo2=dd
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushmedical.html\", 18, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.state!=null && row.state!=0 && row.state!=3  && row.state!=4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function pushFileFormatter(value, row, index) {
    var params={
        "slideNo":"",
        "pathNo":row.pathid
    };
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushfile.html\", 21, "+JSON.stringify(params, null, 2)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function pushFileCheckFormatter(value, row, index) {
    var ss;
    var dd="";
    if(row.requestdetails.search("body") != -1 ){
        ss=JSON.parse(row.requestdetails).body;
        dd=JSON.stringify(ss);
    }
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushfilecheck.html\", 24, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}
function pushFileCheckFormatterDebug(value, row, index) {
    var ss;
    var dd="";
    if(row.reqjson.search("body") != -1 ){
        ss=JSON.parse(row.reqjson).body;
        dd=JSON.stringify(ss);
    }
    var html="<a onclick='addMenuTab2(\"interface_wanda_pushfilecheck.html\", 24, "+JSON.stringify(dd)+");'>接口调试</a>";
    if(row.state!=null && row.state!=0 && row.state!=3  && row.state!=4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function localFileCheckFormatterDebug(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interface_local_filecheck.html\", 27, "+JSON.stringify(row.reqjson, null, 2)+");'>接口调试</a>";
    if(  row.state!=0 && row.state!=3  && row.state!=4 ){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>忽略异常</a>" ;
    }else if(row.state==3 || row.state==4){
        html=html+" | <a onclick='ingoreWandaLogError("+row.id+")'>标志异常</a>" ;
    }
    return [html].join("")
}

function localFileCheckFormatter(value, row, index) {
    var html="<a onclick='addMenuTab2(\"interface_local_filecheck.html\", 27, "+JSON.stringify(row.requestdetails, null, 2)+");'>接口调试</a>";
    if(row.logstate!=null && row.logstate!=0 && row.logstate!=3  && row.logstate!=4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>忽略异常</a>" ;
    }else if(row.logstate==3 || row.logstate==4){
        html=html+" | <a onclick='ingoreWandaError("+row.id+")'>标志异常</a>" ;
    }
    return [
        html
    ].join("")
}

function trim(s) {
    return s.replace(/(^\s*)|(\s*$)/g, "");
}

$('#clearLog').click(function () {
    $("#pathNoTemp").val("");
    $("#dateStartTemp").val("");
    $("#dateEndTemp").val("");
    $("#pathNo").val("");
    $("#dateStart").val("");
    $("#dateEnd").val("");
    return false;
});

$('#clearLangjia').click(function () {
    $("#dateStart").val("");
    $("#dateEnd").val("");
    $("#dateStart").val("");
    $("#dateEnd").val("");
    return false;
});

$('#clear').click(function () {
    $("#pathNoTemp").val("");
    $("#dateStartTemp").val("");
    $("#dateEndTemp").val("");
    $("#pathNo").val("");
    $("#dateStart").val("");
    $("#dateEnd").val("");
    return false;
});

$('#clearDebug').click(function () {
    $("#pathNoTempDebug").val("");
    $("#dateStartTempDebug").val("");
    $("#dateEndTempDebug").val("");
    $("#pathNoDebug").val("");
    $("#dateStartDebug").val("");
    $("#dateEndDebug").val("");
    return false;
});

$('#clearLangjia').click(function () {
    $("#pathNoTemp").val("");
    $("#dateStartTemp").val("");
    $("#dateEndTemp").val("");
    $("#pathNo").val("");
    $("#dateStart").val("");
    $("#dateEnd").val("");
    return false;
});

$('#clearLangjiaDebug').click(function () {
    $("#pathNoTempDebug").val("");
    $("#dateStartTempDebug").val("");
    $("#dateEndTempDebug").val("");
    $("#pathNoDebug").val("");
    $("#dateStartDebug").val("");
    $("#dateEndDebug").val("");
    return false;
});

$("#inputForm").validate(
    {
        rules:{},
        submitHandler: function () {
            $("#pathNoTemp").val($("#pathNo").val());
            $("#dateStartTemp").val($("#dateStart").val());
            $("#dateEndTemp").val($("#dateEnd").val());
            $("#tb_patientLogs").bootstrapTable('refresh');
        }
    });
$("#inputFormDebug").validate(
    {
        rules:{},
        submitHandler: function () {
            $("#pathNoTempDebug").val($("#pathNoDebug").val());
            $("#dateStartTempDebug").val($("#dateStartDebug").val());
            $("#dateEndTempDebug").val($("#dateEndDebug").val());
            $("#tb_debug").bootstrapTable('refresh');
        }
    });
function addMenuTab2(dataUrl, menuName, dataIndex) {
    return window.parent.addMenuTab2(dataUrl, menuName, dataIndex);
}

$("#searchFileForm").validate(
    {
        rules:{},
        submitHandler: function () {
            $("#pathNoTemp").val($("#pathNo").val());
            var $options = $("select option:selected");
            var optionselect;//选择的列表
            if ($options.length != 0) {
                optionselect = $options[0].value;
            }
            $("#pushStateTemp").val(optionselect);
            $("#tb_patientLogs").bootstrapTable('refresh');
        }
    });

function submitPatientLogById(id,state) {
    ajaxGet('/langjiaInterface/patientLogById','id='+id+'&state='+state, function (data) {
            var result = JSON.stringify(data, null, 2);//将字符串转换成json对象
            var newwindow = window.open("about:blank");
            newwindow.document.write(result);
        }
    )
}

function submitselectJsonById(id,state){
    ajaxGet('/langjiaInterface/selectJsonById','id='+id+'&state='+state, function (data) {
            var result = JSON.stringify(data, null, 2);//将字符串转换成json对象
            var newwindow = window.open("about:blank");
            newwindow.document.write(result);
        }
    )
}
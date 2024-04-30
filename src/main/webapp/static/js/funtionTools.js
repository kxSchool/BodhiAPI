function generateXML() {
    var msgCode = $('#MsgCode').val();
    if (msgCode.startsWith('TJ')) {
        var xml ="&lt;?xml version=\"1.0\" encoding=\"utf-8\"?&gt;\n" +
        "&lt;soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"&gt;\n" +
        "   &lt;soap12:Body&gt;\n"
        var body1= "    &lt;/soap12:Body&gt;\n" +
        "&lt;/soap12:Envelope&gt;";
        var body2="        &lt;"+msgCode+" xmlns=\"http://winning.com.cn/tjgl\"&gt;\n"
        var body3="        &lt;/"+msgCode+"&gt;\n"
        var body4="          &lt;strXml&gt;&lt;![CDATA[&lt;RisReport&gt;\n";
        var body5="          &lt;/RisReport>]]&gt;&lt;/strXml&gt;\n"
        var param = "";
        $(".XMLState").each(function () {
            param = param.concat("            &lt;", $(this).attr('name'), "&gt;", $(this).val(), "&lt;", "/", $(this).attr('name'), "&gt;\n");
        });
        if (msgCode=="TJ_ReleaseRisReport"){
            xml = xml.concat(body2,body4,param,body5,body3,body1);
            $("#toastrOptions").html(xml);

        }else {
            xml = xml.concat(body2,param,body3,body1);
            $("#toastrOptions").html(xml);
        }
    } else {
        var xml = '&lt;?xml version="1.0" encoding="utf-8"?&gt;\n' +
            '&lt;soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://www.w3.org/2003/05/soap-envelope"&gt;\n' +
            '  &lt;soap12:Body&gt;\n' +
            '    &lt;SendEmr xmlns="http://www.winning.com.cn/"&gt;\n' +
            '      &lt;MsgCode&gt';
        var body1 = '&lt;/MsgCode&gt;\n' +
            '      &lt;SendXml&gt;\n' +
            '          &lt;![CDATA[&lt;NewDataSet&gt;&lt;Table1&gt;';
        var body2 = '&lt;/Table1&gt;&lt;/NewDataSet&gt;]]&gt;\n' +
            '      &lt;/SendXml&gt;\n' +
            '      &lt;UserCode&gt;kingstar&lt;/UserCode&gt;\n' +
            '    &lt;/SendEmr&gt;\n' +
            '  &lt;/soap12:Body&gt;\n' +
            '&lt;/soap12:Envelope&gt;';
        var param = "";
        $(".XMLState").each(function () {
            param = param.concat("&lt;", $(this).attr('name'), "&gt;", $(this).val(), "&lt;", "/", $(this).attr('name'), "&gt;");
        });
        xml = xml.concat(msgCode, body1, param, body2);
        $("#toastrOptions").html(xml);
    }

}

$("#generateXML").validate({
    rules: {repno: "required", repno2: "required"},
    submitHandler: function () {
        generateXML();
    }
});

function reqjsonLogFormatter(value, row, index) {
    var html = '<div class="wrap-logs"><a href="/hisInterface/showDetails?id=' + row.id + '&type=1" class="wrap-logs" target="_blank">' + row.reqParam + '</a></div>';
    return [html].join("")
}

function rspjsonLogFormatter(value, row, index) {
    var html = '<div class="wrap-logs"><a href="/hisInterface/showDetails?id=' + row.id + '&type=0" class="wrap-logs" target="_blank">' + row.respParam + '</a></div>';
    return [html].join("")
}

function stateFormatterLog(value, row, index) {
    var html;
    if (row.state == 0) {
        html = '<i class="fa fa-check text-navy"></i> 正常';
    } else if (row.state == 1) {
        html = '<i class="fa fa-close text-danger"></i> 系统异常';
    } else if (row.state == 2) {
        html = '<i class="fa fa-close text-danger"></i> 接口异常';
    } else if (row.state == 3 || row.state == 4) {
        html = '<i class="fa fa-check text-warning"></i> 忽略异常';
    }
    return [
        html
    ].join("")
}

//状态标识（'0:正常  1:系统异常  2:请求接口异常  3:系统忽略异常 4:接口忽略异常）
function ingnoereFormatter(value, row, index) {
    var state;
    var html = "<a onclick='addMenuTab2(\"interface_local_filecheck.html\", 27, " + JSON.stringify(row.reqjson, null, 2) + ");'>接口调试</a>";
    if (row.state != 0 && row.state != 3 && row.state != 4) {
        if (row.state == 1) {
            state = 3
        } else {
            state = 4;
        }
        html = html + " | <a onclick='ingoreWandaLogError(" + row.id + "," + state + ")'>忽略异常</a>";
    } else if (row.state == 3 || row.state == 4) {
        if (row.state == 3) {
            state = 1;
        } else {
            state = 2;
        }
        html = html + " | <a onclick='ingoreWandaLogError(" + row.id + "," + state + ")'>标志异常</a>";
    }
    return [html].join("")
}

function ingoreWandaLogError(id, state) {
    var params = {"id": id, "state": state};
    $.ajax({
        type: "POST",
        url: '/hisInterface/updataState2',
        data: JSON.stringify(params),
        contentType: "application/json;charset=UTF-8",
        dataType: 'json',
        success: function (data) {
            var code = data.code;
            if (code == 200) {
                alert("修改成功");
                $("#tb_debugLogs").bootstrapTable('refresh');
            } else {
                alert("修改失败");
            }
        },
        error: function () {
            alert("取数失败！");
        }
    });
}

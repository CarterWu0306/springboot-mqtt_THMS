$(".confirm").click(function(){
    var status="";
    var alarmStatus="";
    if ($("#status").find("option:selected").text()=="设备离线") {
        status="0";
    }else if ($("#status").find("option:selected").text()=="设备在线"){
        status="1";
    }

    if ($("#alarmStatus").find("option:selected").text()=="正常"){
        alarmStatus="0";
    } else if ($("#alarmStatus").find("option:selected").text()=="警报中") {
        alarmStatus="1";
    }

    var jsonData={"deviceId":$(".deviceId").val(),
        "devcName":$(".devcName").val(),
        "clientId":$(".clientId").val(),
        "code":$(".code").val(),
        "type":$(".type").val(),
        "status":status,
        "alarmStatus":alarmStatus,
        "createPerson":$(".createPerson").val(),
        "dutyPerson":$(".dutyPerson").val(),
        "descript":$(".descript").val()
    };
    $.ajax({
        type: "POST",
        url:'addConfirm',
        data:jsonData,// 你的formid
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        error: function(request) {

        },
        success: function(data) {
            var curidx=parent.layer.getFrameIndex(window.name); //获取当前窗口的name
            if(data.code=="0"){
                parent.layer.close(curidx);
                parent.layer.msg(data.msg);
            }else{
                parent.layer.msg(data.msg);
            }
        }
    });
});

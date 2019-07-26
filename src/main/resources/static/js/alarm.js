function sel() {

    var deviceParam="";
    var startParam="";
    var endParam="";

    var devcName = document.getElementById("devcName").value;
    var daterange = document.getElementById("daterange").value;

    if (devcName!="请选择设备名称") {
        deviceParam=devcName;
    }

    if (!(daterange == "" || daterange == null || daterange == undefined)){
        var strs = daterange.split(" - ")
        var startTime = strs[0];
        var endTime = strs[1];
        startParam=startTime;
        endParam=endTime;
    }

    location.replace('http://localhost:8080/alarm?'+'startParam='+startParam+'&endParam='+endParam+'&deviceParam='+deviceParam);
}


layui.use('laydate', function(){
    var laydate = layui.laydate;

    //日期时间范围
    laydate.render({
        elem: '#daterange'
        ,type: 'datetime'
        ,range: true
    });
});